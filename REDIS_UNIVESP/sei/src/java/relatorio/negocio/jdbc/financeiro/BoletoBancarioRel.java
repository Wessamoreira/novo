package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.enumerador.TipoOrigemInteracaoEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberAgrupadaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.ModeloBoletoVO;
import negocio.comuns.financeiro.PlanoDescontoContaReceberVO;
import negocio.comuns.financeiro.PlanoFinanceiroAlunoDescricaoDescontosVO;
import negocio.comuns.financeiro.enumerador.LayoutArquivoIntegracaoFinanceiraEnum;
import negocio.comuns.financeiro.enumerador.TagModeloBoletoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.comuns.utilitarias.dominios.TipoBoletoBancario;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.facade.jdbc.crm.InteracaoWorkflow;
import negocio.facade.jdbc.financeiro.ContaReceber;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.BoletoBancarioRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.interfaces.financeiro.BoletoBancarioRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.DiarioRel;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;
import webservice.boletoonline.RegistroOnlineBoleto;

@Repository
@Scope("singleton")
@Lazy
public class BoletoBancarioRel extends SuperRelatorio implements BoletoBancarioRelInterfaceFacade {

	/**
	 *
	 */
	private static final long serialVersionUID = -6697153788215310754L;

	public void validarDadosFiltro(String matricula, Integer curso, Integer turma, Integer unidadeEnsino, String valorConsultaFiltro, Integer responsavelFinanceiro) throws Exception {
		if (valorConsultaFiltro.equals("curso")) {
			if (curso == 0) {
				throw new Exception("O campo CURSO deve ser informado");
			}
		}
		if (valorConsultaFiltro.equals("turma")) {
			if (turma == 0) {
				throw new Exception("O campo TURMA deve ser informado");
			}
		}
		if (valorConsultaFiltro.equals("aluno")) {
			if (matricula.equals("")) {
				throw new Exception("O campo MATRÍCULA deve ser informado");
			}
		}
		if (valorConsultaFiltro.equals("unidadeEnsino")) {
			if (unidadeEnsino == 0) {
				throw new Exception("O campo UNIDADE DE ENSINO deve ser informado");
			}
		}
		if (valorConsultaFiltro.equals("responsavelFinanceiro")) {
			if (unidadeEnsino == 0) {
				throw new Exception("O campo UNIDADE DE ENSINO deve ser informado");
			}
			if (responsavelFinanceiro == 0) {
				throw new Exception("O campo RESPONSÁVEL FINANCEIRO deve ser informado");
			}
		}
	}

	public List<BoletoBancarioRelVO> emitirRelatorioLista(Boolean trazerApenasAlunosAtivos, Integer codigo, String matricula, String ano, String semestre, String parcela, Integer curso, Integer turma, Date dataInicio, Date dataFim, Integer unidadeEnsino, String valorConsultaFiltro, Integer codigoRenegociacao, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer responsavelFinanceiro, Boolean permiteEmitirBoletoRecebido) throws Exception {
		return emitirRelatorioLista(trazerApenasAlunosAtivos, codigo, matricula, ano, semestre, parcela, curso, turma, dataInicio, dataFim, unidadeEnsino, valorConsultaFiltro, 0, usuarioVO, "", codigoRenegociacao, configuracaoFinanceiroVO, responsavelFinanceiro, permiteEmitirBoletoRecebido, null, null);
	}	
	
	@SuppressWarnings("static-access")
	public List<BoletoBancarioRelVO> emitirRelatorioLista(Boolean trazerApenasAlunosAtivos, Integer codigo, String matricula, String ano, String semestre, String parcela, Integer curso, Integer turma, Date dataInicio, Date dataFim, Integer unidadeEnsino, String valorConsultaFiltro, Integer centroReceita, UsuarioVO usuarioVO, String tipoImpressaoBoleto, Integer codigoRenegociacao, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer responsavelFinanceiro, Boolean permiteEmitirBoletoRecebido, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
		ConfiguracaoFinanceiroVO configuracaoFinanceiroComBaseContaReceber = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoFinanceiraComBaseContaReceberCasoExista(codigo);
		if (!Uteis.isAtributoPreenchido(configuracaoFinanceiroComBaseContaReceber) && Uteis.isAtributoPreenchido(configuracaoFinanceiroVO)) {
			configuracaoFinanceiroComBaseContaReceber = configuracaoFinanceiroVO ;
		}
		if (!Uteis.isAtributoPreenchido(configuracaoFinanceiroComBaseContaReceber)) {
			configuracaoFinanceiroComBaseContaReceber =  getAplicacaoControle().getConfiguracaoFinanceiroVO(0);
		}
		List<PlanoDescontoContaReceberVO> listaPlanoDescontoContaReceber = null;
		List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroAlunoDescricaoDescontos = null;
		List<PlanoDescontoVO> listaPlanoDesconto = new ArrayList<PlanoDescontoVO>(0);
		List<ConvenioVO> listaConvenios = new ArrayList<ConvenioVO>(0);
		try {
			validarDadosFiltro(matricula, curso, turma, unidadeEnsino, valorConsultaFiltro, responsavelFinanceiro);
			List<BoletoBancarioRelVO> boletoBancarioRelVOsFinal = new ArrayList<BoletoBancarioRelVO>(); 
			List<BoletoBancarioRelVO> boletoBancarioRelVOs = executarConsultaParametrizada(trazerApenasAlunosAtivos, codigo, null, matricula, ano, semestre, parcela, curso, turma, dataInicio, dataFim, unidadeEnsino, usuarioVO, tipoImpressaoBoleto, codigoRenegociacao, responsavelFinanceiro, centroReceita, permiteEmitirBoletoRecebido, filtroRelatorioFinanceiroVO, filtroRelatorioAcademicoVO);
			
			Map<Integer, List<PlanoDescontoContaReceberVO>> planoDescontoContaReceberVOs = getFacadeFactory().getPlanoDescontoContaReceberFacade().consultarPlanoDescontoContaRecberParaGeracaoBoleto(boletoBancarioRelVOs);
			adicionarCamposReferenteModeloBoletos(boletoBancarioRelVOs, usuarioVO, configuracaoFinanceiroComBaseContaReceber);
			for (BoletoBancarioRelVO boletoBancarioRelVO : boletoBancarioRelVOs) {
				String descricaoConta = boletoBancarioRelVO.getContareceber_descricaopagamento();
				if (!Uteis.isAtributoPreenchido(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe())) {
					processoIntegracaoFinanceiroDetalhe(boletoBancarioRelVO, listaPlanoDesconto, listaConvenios, planoDescontoContaReceberVOs, listaPlanoDescontoContaReceber, listaPlanoFinanceiroAlunoDescricaoDescontos, unidadeEnsino,usuarioVO, configuracaoFinanceiroComBaseContaReceber);
				} else {
					if (getFacadeFactory().getIntegracaoFinanceiroFacade().realizarVerificacaoProcessamentoIntegracaoFinanceira()) {
						throw new Exception("Prezado, a emissão dos boletos está indisponível temporariamente, tente mais tarde.");
					}
					gerarInstrucaoBoletoContaReceberIntegracaoFinanceiro(boletoBancarioRelVO, configuracaoFinanceiroComBaseContaReceber);
				}
				if(boletoBancarioRelVO.getContareceber_tipoorigem().equals("OUT") || (boletoBancarioRelVO.getContareceber_tipoorigem().equals("BIB"))) {
					StringBuilder descricaoPagamento = new StringBuilder("");
					
					if (Uteis.isAtributoPreenchido(planoDescontoContaReceberVOs)) {
						if (planoDescontoContaReceberVOs.containsKey(boletoBancarioRelVO.getContareceber_codigo())) {
							for (PlanoDescontoContaReceberVO p : planoDescontoContaReceberVOs.get(boletoBancarioRelVO.getContareceber_codigo())) {
								listaPlanoDesconto.add(p.getPlanoDescontoVO());
							}
						}
					}
					listaPlanoFinanceiroAlunoDescricaoDescontos = getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(false, boletoBancarioRelVO.getContareceber_valorBaseContaReceber(), boletoBancarioRelVO.getContareceber_tipoDescontoAluno(), boletoBancarioRelVO.getContareceber_percDescontoAluno(), boletoBancarioRelVO.getContareceber_valorDescontoAluno(), boletoBancarioRelVO.getDescontoValidoatedataparcela(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getContareceber_dataoriginalvencimento(), boletoBancarioRelVO.getOrdemDescontos(), null, listaPlanoDesconto, listaConvenios, 0, boletoBancarioRelVO.getContareceber_usadescontocompostoplanodesconto(), configuracaoFinanceiroComBaseContaReceber, Boolean.FALSE, null, boletoBancarioRelVO.getContareceber_matriculaaluno(), boletoBancarioRelVO.getCondicao_aplicarCalculoComBaseDescontosCalculados(), boletoBancarioRelVO.getCodigoCidadeUnidadeEnsino());
					if (!boletoBancarioRelVO.getModeloboleto_utilizarDescricaoDescontoPersonalizado()) {
						if (!boletoBancarioRelVO.getPagamento1Parte1().trim().isEmpty() || !boletoBancarioRelVO.getPagamento1Parte2().trim().isEmpty()) {
							descricaoPagamento.append(boletoBancarioRelVO.getPagamento1Parte1())
									.append(boletoBancarioRelVO.getPagamento1Parte2())
									.append(boletoBancarioRelVO.getPagamento1Parte3())
									.append(boletoBancarioRelVO.getPagamento1Parte4());
						}
						if (!boletoBancarioRelVO.getPagamento2Parte1().trim().isEmpty()) {
							if(descricaoPagamento.length() > 0) {
								descricaoPagamento.append("<p>");
							}
							descricaoPagamento.append(boletoBancarioRelVO.getPagamento2Parte1())
									.append(boletoBancarioRelVO.getPagamento2Parte2())
									.append(boletoBancarioRelVO.getPagamento2Parte3())
									.append(boletoBancarioRelVO.getPagamento2Parte4());
						}
						if (!boletoBancarioRelVO.getPagamento3Parte1().trim().isEmpty()) {
							if(descricaoPagamento.length() > 0) {
								descricaoPagamento.append("<p>");
							}
							descricaoPagamento.append(boletoBancarioRelVO.getPagamento3Parte1())
									.append(boletoBancarioRelVO.getPagamento3Parte2())
									.append(boletoBancarioRelVO.getPagamento3Parte3())
									.append(boletoBancarioRelVO.getPagamento3Parte4());
						}
						if (!boletoBancarioRelVO.getPagamento4Parte1().trim().isEmpty()) {
							if(descricaoPagamento.length() > 0) {
								descricaoPagamento.append("<p>");
							}
							descricaoPagamento.append(boletoBancarioRelVO.getPagamento4Parte1())
									.append(boletoBancarioRelVO.getPagamento4Parte2())
									.append(boletoBancarioRelVO.getPagamento4Parte3())
									.append(boletoBancarioRelVO.getPagamento4Parte4());
						}
						boletoBancarioRelVO.setContareceber_descricaopagamento(descricaoPagamento.toString());
						if(Uteis.isAtributoPreenchido(descricaoConta)) {						
							boletoBancarioRelVO.setContareceber_descricaopagamento(boletoBancarioRelVO.getContareceber_descricaopagamento()+"<p>"+descricaoConta);
						}
					}else if(Uteis.isAtributoPreenchido(listaPlanoFinanceiroAlunoDescricaoDescontos)) {
						boletoBancarioRelVO.gerarIntrucoesDescontos(boletoBancarioRelVO, listaPlanoFinanceiroAlunoDescricaoDescontos, listaPlanoDesconto, configuracaoFinanceiroVO);
						if(boletoBancarioRelVO.getModeloboleto_instrucao1().trim().isEmpty()) {
							descricaoPagamento.append(boletoBancarioRelVO.getModeloboleto_instrucao1());
						}
						if(boletoBancarioRelVO.getModeloboleto_instrucao2().trim().isEmpty()) {
							if(descricaoPagamento.length() > 0) {
								descricaoPagamento.append("<p>");
							}
							descricaoPagamento.append(boletoBancarioRelVO.getModeloboleto_instrucao2());
						}
						if(boletoBancarioRelVO.getModeloboleto_instrucao3().trim().isEmpty()) {
							if(descricaoPagamento.length() > 0) {
								descricaoPagamento.append("<p>");
							}
							descricaoPagamento.append(boletoBancarioRelVO.getModeloboleto_instrucao3());
						}
						if(boletoBancarioRelVO.getModeloboleto_instrucao4().trim().isEmpty()) {
							if(descricaoPagamento.length() > 0) {
								descricaoPagamento.append("<p>");
							}
							descricaoPagamento.append(boletoBancarioRelVO.getModeloboleto_instrucao3());
						}
						if(boletoBancarioRelVO.getModeloboleto_instrucao5().trim().isEmpty()) {
							if(descricaoPagamento.length() > 0) {
								descricaoPagamento.append("<p>");
							}
							descricaoPagamento.append(boletoBancarioRelVO.getModeloboleto_instrucao5());
						}
						if(boletoBancarioRelVO.getModeloboleto_instrucao6().trim().isEmpty()) {
							if(descricaoPagamento.length() > 0) {
								descricaoPagamento.append("<p>");
							}
							descricaoPagamento.append(boletoBancarioRelVO.getModeloboleto_instrucao6());
						}
					}
					
				}
				preencherMantenedoraContaReceber(usuarioVO, boletoBancarioRelVO);
				listaPlanoDesconto.clear();
				listaConvenios.clear();

			}
			validarCodigoBarrasParaGeracaoBarCode(boletoBancarioRelVOs);
			// 033935786000007986792216400101734470102

			for (BoletoBancarioRelVO corrente : boletoBancarioRelVOs) {
				if (listaPlanoFinanceiroAlunoDescricaoDescontos == null) {
					listaPlanoFinanceiroAlunoDescricaoDescontos = new ArrayList<PlanoFinanceiroAlunoDescricaoDescontosVO>(0);
				}
				if (!configuracaoFinanceiroComBaseContaReceber.getGerarBoletoComDescontoSemValidade() && (!corrente.getContareceber_tipoorigem().equals("MAT") && !corrente.getContareceber_tipoorigem().equals("MEN") && !corrente.getContareceber_tipoorigem().equals("NCR")) && (corrente.getContareceber_descontoprogressivo() == 0)) {
					corrente.limparInstrucoesNaoUtilizadas(corrente, listaPlanoFinanceiroAlunoDescricaoDescontos);
				}
				if (corrente.getContareceber_tipoorigem().equals("OUT")) {
					corrente.setModeloboleto_utilizarDescricaoDescontoPersonalizado(Boolean.TRUE);
				}
				try {
	            	// No cadastro do parceiro é permitido informar se o parceito permite emissão de boleto de aluno vinculado ao parceiro. 
	            	if (!getFacadeFactory().getContaReceberFacade().permiteEmitirBoletoAlunoVinculadoParceiro(corrente.getContareceber_codigo())) {
	            		throw new Exception("Não é permitido a impressão desse boleto (nosso número: " + corrente.getContareceber_nossonumero() + ") para este Convênio/Parceiro. Conta a Receber vinculada a um Parceiro que bloqueia a emissão de boleto!");
	            	}
	            	// No cadastro do parceiro é permitido informar se o parceito permite remessa de boleto para aluno vinculado ao parceiro. 
	            	if (getFacadeFactory().getContaReceberFacade().permiteRemessaBoletoAlunoVinculadoParceiro(corrente.getContareceber_codigo())) {
						RegistroOnlineBoleto registrar = new RegistroOnlineBoleto();
						registrar.enviarBoletoRemessaOnlineLista(corrente, configuracaoFinanceiroComBaseContaReceber, usuarioVO, getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema());
	            	}
					boletoBancarioRelVOsFinal.add(corrente);
					getFacadeFactory().getContaReceberFacade().alterarBooleanEmissaoBoletoRealizada(corrente.getContareceber_codigo(), true, usuarioVO);
				} catch (Exception e) {
					throw e;
				}
			}
			
			return boletoBancarioRelVOsFinal;
		} catch (Exception e) {
			throw new ConsistirException(e.getMessage());
		} finally {
			if (listaPlanoDescontoContaReceber != null) {
				listaPlanoDescontoContaReceber.clear();
			}
			listaPlanoDescontoContaReceber = null;
			if (listaPlanoFinanceiroAlunoDescricaoDescontos != null) {
				listaPlanoFinanceiroAlunoDescricaoDescontos.clear();
			}
			listaPlanoFinanceiroAlunoDescricaoDescontos = null;
			listaPlanoDesconto.clear();
			listaConvenios.clear();
		}
	}

	private void preencherMantenedoraContaReceber(UsuarioVO usuarioVO, BoletoBancarioRelVO boletoBancarioRelVO) throws Exception {		
		if (boletoBancarioRelVO.getUtilizarDadosMatrizBoleto()) {
			UnidadeEnsinoVO unid = getFacadeFactory().getUnidadeEnsinoFacade().consultarSeExisteUnidadeMatriz(true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			if (!Uteis.isAtributoPreenchido(unid)) {
				throw new Exception(UteisJSF.internacionalizar("prt_BoletoBancarioRel_erroTurmaUtilizaDadosMatrizBoletoSemUnidadeEnsinoMatriz"));
			}
			unid = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorChavePrimariaDadosBasicosBoleto(unid.getCodigo(), usuarioVO);
			boletoBancarioRelVO.setContareceber_cnpjMantenedora(unid.getCNPJ());
			boletoBancarioRelVO.setContareceber_razaoSocialMantenedora(unid.getRazaoSocial());
			boletoBancarioRelVO.setContareceber_mantenedora(unid.getMantenedora());
			boletoBancarioRelVO.setContareceber_foneMantenedora(unid.getTelComercial1());
			boletoBancarioRelVO.setEnderecoUnidadeEnsino(unid.getEndereco());
			boletoBancarioRelVO.setTelefoneUnidadeEnsino(unid.getTelComercial1());
			boletoBancarioRelVO.setSetorUnidadeEnsino(unid.getSetor());
			boletoBancarioRelVO.setComplementoUnidadeEnsino(unid.getComplemento());
			boletoBancarioRelVO.setCepUnidadeEnsino(unid.getCEP());
			boletoBancarioRelVO.setNumeroUnidadeEnsino(unid.getNumero());
			boletoBancarioRelVO.setCidadeUnidadeEnsino(unid.getCidade().getNome());
			boletoBancarioRelVO.setEstadoUnidadeEnsino(unid.getCidade().getEstado().getNome());
			
		} else if (boletoBancarioRelVO.getContacorrente_utilizaDadosInformadosCCparaGeracaoBoleto()) {
			ContaCorrenteVO cc = (ContaCorrenteVO) getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(boletoBancarioRelVO.getContareceber_contacorrente(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			boletoBancarioRelVO.setContareceber_cnpjMantenedora(cc.getCNPJ());
			boletoBancarioRelVO.setContareceber_razaoSocialMantenedora(cc.getRazaoSocial());
			boletoBancarioRelVO.setContareceber_mantenedora(cc.getMantenedora());
			boletoBancarioRelVO.setContareceber_foneMantenedora(cc.getTelComercial1());
			boletoBancarioRelVO.setEnderecoUnidadeEnsino(cc.getEndereco());
			boletoBancarioRelVO.setTelefoneUnidadeEnsino(cc.getTelComercial1());
			boletoBancarioRelVO.setSetorUnidadeEnsino(cc.getSetor());
			boletoBancarioRelVO.setComplementoUnidadeEnsino(cc.getComplemento());
			boletoBancarioRelVO.setCepUnidadeEnsino(cc.getCEP());
			boletoBancarioRelVO.setNumeroUnidadeEnsino(cc.getNumeroEnd());
			boletoBancarioRelVO.setCidadeUnidadeEnsino(cc.getCidade().getNome());
			boletoBancarioRelVO.setEstadoUnidadeEnsino(cc.getCidade().getEstado().getNome());
		}
	}

	private List<PlanoFinanceiroAlunoDescricaoDescontosVO> validarBoletoComContaReceberDoTipoOrigemOutros(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroAlunoDescricaoDescontos, List<PlanoDescontoVO> listaPlanoDesconto, List<ConvenioVO> listaConvenios, BoletoBancarioRelVO boletoBancarioRelVO, Map<Integer, List<PlanoDescontoContaReceberVO>> planoDescontoContaReceberVOs, UsuarioVO usuarioVO) throws Exception {
		if (boletoBancarioRelVO.getContareceber_tipoorigem().equals("OUT")) {
			Date dataReferenciaConsiderarBaixaTituloAposVcto = Uteis.getDataFutura(boletoBancarioRelVO.getContareceber_datavencimento(), Calendar.DAY_OF_MONTH, 1);
			List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaComDescontoAposVcto = getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAlunoPadrao(false, boletoBancarioRelVO.getContareceber_valorBaseContaReceber(), boletoBancarioRelVO.getContareceber_tipodesconto(), boletoBancarioRelVO.getContareceber_valordesconto(), boletoBancarioRelVO.getContareceber_valordesconto(),  boletoBancarioRelVO.getDescontoValidoatedataparcela(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getOrdemDescontos(), null, listaPlanoDesconto, listaConvenios, 0, boletoBancarioRelVO.getContareceber_usadescontocompostoplanodesconto(), configuracaoFinanceiroVO.getVencimentoDescontoProgressivoDiaUtil(), Boolean.TRUE, dataReferenciaConsiderarBaixaTituloAposVcto, boletoBancarioRelVO.getContareceber_matriculaaluno(), boletoBancarioRelVO.getCondicao_aplicarCalculoComBaseDescontosCalculados(), boletoBancarioRelVO.getCodigoCidadeUnidadeEnsino());
			getFacadeFactory().getContaReceberFacade().validarRegrasParaFinanciamentoProprioConvenioImprimirBoleto(planoDescontoContaReceberVOs.get(boletoBancarioRelVO.getContareceber_codigo()), listaComDescontoAposVcto, boletoBancarioRelVO.getContareceber_codigo(), boletoBancarioRelVO.getContareceber_matriculaaluno(), boletoBancarioRelVO.getContareceber_tipoorigem(),  new Date());
			if ((listaComDescontoAposVcto != null) && (listaComDescontoAposVcto.size() > 0)) {
				listaPlanoFinanceiroAlunoDescricaoDescontos = new ArrayList<PlanoFinanceiroAlunoDescricaoDescontosVO>(0);
				if (!listaPlanoFinanceiroAlunoDescricaoDescontos.isEmpty() && listaPlanoFinanceiroAlunoDescricaoDescontos.get(listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 1).getDiaNrAntesVencimento().equals(0)) {
					listaPlanoFinanceiroAlunoDescricaoDescontos.get(listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 1).setReferentePlanoFinanceiroAteVencimento(true);
				}
				PlanoFinanceiroAlunoDescricaoDescontosVO planoAposVcto = listaComDescontoAposVcto.get(listaComDescontoAposVcto.size() - 1);
				planoAposVcto.setReferentePlanoFinanceiroAposVcto(Boolean.TRUE);
				listaPlanoFinanceiroAlunoDescricaoDescontos.add(planoAposVcto);
			}
			boletoBancarioRelVO.gerarIntrucoesDescontos(boletoBancarioRelVO, listaPlanoFinanceiroAlunoDescricaoDescontos, listaPlanoDesconto, configuracaoFinanceiroVO);
			String descricaoFinal = boletoBancarioRelVO.getPagamento1Parte1() + boletoBancarioRelVO.getPagamento1Parte2() + boletoBancarioRelVO.getPagamento1Parte3() + boletoBancarioRelVO.getPagamento1Parte4() + "\n";
			descricaoFinal += boletoBancarioRelVO.getContareceber_descricaopagamento();
			boletoBancarioRelVO.setContareceber_descricaopagamento(descricaoFinal);
		}
		return listaPlanoFinanceiroAlunoDescricaoDescontos;
	}

	private void processoIntegracaoFinanceiroDetalhe(BoletoBancarioRelVO boletoBancarioRelVO, List<PlanoDescontoVO> listaPlanoDesconto, List<ConvenioVO> listaConvenios, Map<Integer, List<PlanoDescontoContaReceberVO>> planoDescontoContaReceberVOs, List<PlanoDescontoContaReceberVO> listaPlanoDescontoContaReceber, List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroAlunoDescricaoDescontos, Integer unidadeEnsino, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		DescontoProgressivoVO descontoProgressivoVO = null;
		if (configuracaoFinanceiroVO.getVencimentoParcelaDiaUtil()) {
			boletoBancarioRelVO.getContareceber_dataoriginalvencimento();
			boletoBancarioRelVO.getContareceber_datavencimentodiautil();
			boletoBancarioRelVO.setContareceber_datavencimentodiautil(getFacadeFactory().getContaReceberFacade().obterDataVerificandoDiaUtil(boletoBancarioRelVO.getContareceber_dataoriginalvencimento(), boletoBancarioRelVO.getCodigoCidadeUnidadeEnsino(), null));
			boletoBancarioRelVO.setContareceber_datavencimento(boletoBancarioRelVO.getContareceber_datavencimentodiautil());
		}
		realizarAlteracaoDadosBoletoComRemessaOnlineAtualizandoVencimentoValor(boletoBancarioRelVO, configuracaoFinanceiroVO, usuarioVO);
		if (planoDescontoContaReceberVOs.containsKey(boletoBancarioRelVO.getContareceber_codigo())) {
			listaPlanoDesconto = new ArrayList<PlanoDescontoVO>(0);
			listaConvenios = new ArrayList<ConvenioVO>(0);
			for (PlanoDescontoContaReceberVO p : planoDescontoContaReceberVOs.get(boletoBancarioRelVO.getContareceber_codigo())) {
				if (p.getIsConvenio()) {
					listaConvenios.add(p.getConvenio());
				} else {
					listaPlanoDesconto.add(p.getPlanoDescontoVO());
				}
			}
		} else {
			listaPlanoDesconto.clear();
			listaConvenios.clear();
		}

		if (boletoBancarioRelVO.getContareceber_descontoprogressivo() != 0 && !configuracaoFinanceiroVO.getGerarBoletoComDescontoSemValidade()) {
			// Variável responsável por controlar se a parcela é matrícula ou não.
			// ////////////////// NOVO ///////////////////////
			// Pegar o número de dias de variação da data de vencimento na Matricula Periodo Vencimento.
			// Integer diasVariacaoDataVencimento = getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarDiasVariacaoDataVencimentoPeloCodigoContaReceber(boletoBancarioRelVO.getContareceber_codigo(),null);
			// getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(boletoBancarioRelVO.getContareceber_descontoprogressivo(),null);
			descontoProgressivoVO = new DescontoProgressivoVO();
			descontoProgressivoVO.setNovoObj(false);
			descontoProgressivoVO.setCodigo(boletoBancarioRelVO.getContareceber_descontoprogressivo());
			descontoProgressivoVO.setUtilizarDiaFixo(boletoBancarioRelVO.getDescontoprogressivo_utilizarDiaFixo());
			descontoProgressivoVO.setUtilizarDiaUtil(boletoBancarioRelVO.getDescontoprogressivo_utilizarDiaUtil());
			if (boletoBancarioRelVO.getContareceber_contaEditadaManualmente()) {
				descontoProgressivoVO.setDescontoProgressivoEditadoManualmenteContaReceber(Boolean.TRUE);
			}
			descontoProgressivoVO.setDiaLimite1(boletoBancarioRelVO.getDescontoprogressivo_dialimite1());
			descontoProgressivoVO.setDiaLimite2(boletoBancarioRelVO.getDescontoprogressivo_dialimite2());
			descontoProgressivoVO.setDiaLimite3(boletoBancarioRelVO.getDescontoprogressivo_dialimite3());
			descontoProgressivoVO.setDiaLimite4(boletoBancarioRelVO.getDescontoprogressivo_dialimite4());
			descontoProgressivoVO.setPercDescontoLimite1(boletoBancarioRelVO.getDescontoprogressivo_percdescontolimite1());
			descontoProgressivoVO.setPercDescontoLimite2(boletoBancarioRelVO.getDescontoprogressivo_percdescontolimite2());
			descontoProgressivoVO.setPercDescontoLimite3(boletoBancarioRelVO.getDescontoprogressivo_percdescontolimite3());
			descontoProgressivoVO.setPercDescontoLimite4(boletoBancarioRelVO.getDescontoprogressivo_valordescontolimite4());
			descontoProgressivoVO.setValorDescontoLimite1(boletoBancarioRelVO.getDescontoprogressivo_valordescontolimite1());
			descontoProgressivoVO.setValorDescontoLimite2(boletoBancarioRelVO.getDescontoprogressivo_valordescontolimite2());
			descontoProgressivoVO.setValorDescontoLimite3(boletoBancarioRelVO.getDescontoprogressivo_valordescontolimite3());
			descontoProgressivoVO.setValorDescontoLimite4(boletoBancarioRelVO.getDescontoprogressivo_valordescontolimite4());

			if (descontoProgressivoVO.getUtilizarDiaFixo()) {
				listaPlanoFinanceiroAlunoDescricaoDescontos = getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(boletoBancarioRelVO.getContareceber_tipoorigem().equals("MAT"), boletoBancarioRelVO.getContareceber_valorBaseContaReceber(), boletoBancarioRelVO.getContareceber_tipodesconto(), boletoBancarioRelVO.getContareceber_valordesconto(), boletoBancarioRelVO.getContareceber_valordesconto(),  boletoBancarioRelVO.getDescontoValidoatedataparcela(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getContareceber_dataoriginalvencimento(), boletoBancarioRelVO.getOrdemDescontos(), descontoProgressivoVO, listaPlanoDesconto, listaConvenios, 0, boletoBancarioRelVO.getContareceber_usadescontocompostoplanodesconto(), configuracaoFinanceiroVO, Boolean.FALSE, null, boletoBancarioRelVO.getContareceber_matriculaaluno(), boletoBancarioRelVO.getCondicao_aplicarCalculoComBaseDescontosCalculados(), boletoBancarioRelVO.getCodigoCidadeUnidadeEnsino());
			} else {
				listaPlanoFinanceiroAlunoDescricaoDescontos = getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(boletoBancarioRelVO.getContareceber_tipoorigem().equals("MAT"), boletoBancarioRelVO.getContareceber_valorBaseContaReceber(), boletoBancarioRelVO.getContareceber_tipodesconto(), boletoBancarioRelVO.getContareceber_valordesconto(), boletoBancarioRelVO.getContareceber_valordesconto(),  boletoBancarioRelVO.getDescontoValidoatedataparcela(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getContareceber_dataoriginalvencimento(), boletoBancarioRelVO.getOrdemDescontos(), descontoProgressivoVO, listaPlanoDesconto, listaConvenios, boletoBancarioRelVO.getDiasVariacaoDataVencimento(), boletoBancarioRelVO.getContareceber_usadescontocompostoplanodesconto(), configuracaoFinanceiroVO, Boolean.FALSE, null, boletoBancarioRelVO.getContareceber_matriculaaluno(), boletoBancarioRelVO.getCondicao_aplicarCalculoComBaseDescontosCalculados(), boletoBancarioRelVO.getCodigoCidadeUnidadeEnsino());
			}
			getFacadeFactory().getContaReceberFacade().validarRegrasParaFinanciamentoProprioConvenioImprimirBoleto(planoDescontoContaReceberVOs.get(boletoBancarioRelVO.getContareceber_codigo()), listaPlanoFinanceiroAlunoDescricaoDescontos,  boletoBancarioRelVO.getContareceber_codigo(), boletoBancarioRelVO.getContareceber_matriculaaluno(),  boletoBancarioRelVO.getContareceber_tipoorigem(),   new Date());						
			getFacadeFactory().getContaReceberFacade().adicionarDescontoRateioEmPlanoFinanceiroAlunoDescricaoDescontosVO(listaPlanoFinanceiroAlunoDescricaoDescontos, boletoBancarioRelVO.getContareceber_valorBaseContaReceber(), boletoBancarioRelVO.getContareceber_valorCusteadoContaReceber(), boletoBancarioRelVO.getContareceber_valorDescontoRateio(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getContareceber_datavencimento());

			if (!listaPlanoFinanceiroAlunoDescricaoDescontos.isEmpty() && listaPlanoFinanceiroAlunoDescricaoDescontos.get(listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 1).getValorDescontoAluno() > 0 && boletoBancarioRelVO.getDescontoValidoatedataparcela() && boletoBancarioRelVO.getContareceber_tipoorigem().equals("MEN")) {
				/**
				 * Criado por Rodrigo Wind 30/04/15 Adicionado esta regra abaixo pois quando no plano financeiro do aluno o desconto do aluno é valido até a data de vencimento e o mesmo possua desconto após o vencimento e não possua um plano de desconto até o vencimento é necessário adicionar na lista listaPlanoFinanceiroAlunoDescricaoDescontos mais um objeto que refere ao desconto após o vencimento sem este desconto não é apresentado no boleto bancário a linha na descrição dos desconto "Após Vencimento"
				 */
				if (!listaPlanoFinanceiroAlunoDescricaoDescontos.get(listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 1).getReferentePlanoFinanceiroAposVcto()) {
					// Abaixo chamamos o método que gera a lista de descontos, de forma a garantir que gere
					// a condição de pagamento que será aplicada para o aluno após o vencimento do boleto
					// Esta descrição é útil quando existem descontos que vencem na data de vencimento. Ou seja,
					// nestes casos é importante que o SEI imprima os valores a serem adotados após o pagamento
					// do boleto.
					Date dataReferenciaConsiderarBaixaTituloAposVcto = Uteis.getDataFutura(boletoBancarioRelVO.getContareceber_datavencimento(), Calendar.DAY_OF_MONTH, 1);
					List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaComDescontoAposVcto = getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAlunoPadrao(false, boletoBancarioRelVO.getContareceber_valorBaseContaReceber(), boletoBancarioRelVO.getContareceber_tipodesconto(), boletoBancarioRelVO.getContareceber_valordesconto(), boletoBancarioRelVO.getContareceber_valordesconto(), boletoBancarioRelVO.getDescontoValidoatedataparcela(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getOrdemDescontos(), null, listaPlanoDesconto, listaConvenios, 0, boletoBancarioRelVO.getContareceber_usadescontocompostoplanodesconto(), configuracaoFinanceiroVO.getVencimentoDescontoProgressivoDiaUtil(), Boolean.TRUE, dataReferenciaConsiderarBaixaTituloAposVcto, boletoBancarioRelVO.getContareceber_matriculaaluno(), boletoBancarioRelVO.getCondicao_aplicarCalculoComBaseDescontosCalculados(), boletoBancarioRelVO.getCodigoCidadeUnidadeEnsino());
					if ((listaComDescontoAposVcto != null) && (listaComDescontoAposVcto.size() > 0)) {
						if (!listaPlanoFinanceiroAlunoDescricaoDescontos.isEmpty() && listaPlanoFinanceiroAlunoDescricaoDescontos.get(listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 1).getDiaNrAntesVencimento().equals(0)) {
							listaPlanoFinanceiroAlunoDescricaoDescontos.get(listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 1).setReferentePlanoFinanceiroAteVencimento(true);
						}
						PlanoFinanceiroAlunoDescricaoDescontosVO planoAposVcto = listaComDescontoAposVcto.get(listaComDescontoAposVcto.size() - 1);
						planoAposVcto.setReferentePlanoFinanceiroAposVcto(Boolean.TRUE);
						listaPlanoFinanceiroAlunoDescricaoDescontos.add(planoAposVcto);
					}
				}

				/**
				 * Adicionada regra caso no planoFinanceiroAluno esteja marcado como descontoValidoAteDataParcela zere o valor do desconto do aluno, devido a existir uma insconsistência no ato de realizar os cálculos financeiros da ContaReceber e o mesmo não poder ser solucionado através da passagem deste parâmetro do PlanoFinanceiroAluno.
				 */
				for (PlanoFinanceiroAlunoDescricaoDescontosVO obj : listaPlanoFinanceiroAlunoDescricaoDescontos) {
					if (obj.getReferentePlanoFinanceiroAposVcto()) {
						obj.setValorDescontoAluno(0.0);
					}
					if (obj.getTipoOrigemDesconto().equals(PlanoFinanceiroAlunoDescricaoDescontosVO.TIPODESCONTOPADRAO) && obj.getDiaNrAntesVencimento() == 0 && obj.getReferentePlanoFinanceiroAposVcto()) {
						obj.setValorDescontoAluno(0.0);
					}
				}
			}
			if (configuracaoFinanceiroVO.getVencimentoParcelaDiaUtil()) {
				if (configuracaoFinanceiroVO.getAlterarDataVencimentoParcelaDiaUtil()) {
					ContaReceberVO obj = new ContaReceberVO();
					obj.setCodigo(boletoBancarioRelVO.getContareceber_codigo());
					getFacadeFactory().getContaReceberFacade().carregarDados(obj, NivelMontarDados.TODOS, configuracaoFinanceiroVO, usuarioVO);
					obj.setDataVencimento(boletoBancarioRelVO.getContareceber_datavencimentodiautil());
					ContaCorrenteVO contaCorrenteVO = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
					obj.criarBoleto(contaCorrenteVO);
				} else {
					boletoBancarioRelVO.setContareceber_datavencimento(boletoBancarioRelVO.getContareceber_dataoriginalvencimento());
				}
			}
			//Adiciona o valor de Reajuste da Diferença de parcela Recebida ou Enviada na Remessa
//	        if (!boletoBancarioRelVO.getContaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().equals(BigDecimal.ZERO) 
//	        		|| !boletoBancarioRelVO.getContaReceber_valorIndiceReajuste().equals(BigDecimal.ZERO)
//	        		|| (Uteis.getData(boletoBancarioRelVO.getContareceber_dataoriginalvencimento()) != Uteis.getData(boletoBancarioRelVO.getContareceber_datavencimento())) ) {
//	        	if (!boletoBancarioRelVO.getContaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().equals(BigDecimal.ZERO)) {
//	        		boletoBancarioRelVO.setContareceber_valorBaseContaReceber(boletoBancarioRelVO.getContareceber_valorBaseContaReceber() + boletoBancarioRelVO.getContaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue());
//	        		boletoBancarioRelVO.setContareceber_valor(boletoBancarioRelVO.getContareceber_valor() + boletoBancarioRelVO.getContaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue());
//	        	}
//	        	regerarCodigoBarraTaxaBoleto(boletoBancarioRelVO, null);
//	        	
//	        }
			realizarAdicaoValoresAdicionaisBoleto(boletoBancarioRelVO, false);
			boletoBancarioRelVO.gerarIntrucoesDescontos(boletoBancarioRelVO, listaPlanoFinanceiroAlunoDescricaoDescontos, listaPlanoDesconto, configuracaoFinanceiroVO);
			descontoProgressivoVO = null;
			if (listaPlanoDescontoContaReceber != null) {
				listaPlanoDescontoContaReceber.clear();
			}
			listaPlanoDescontoContaReceber = null;
			if (listaPlanoFinanceiroAlunoDescricaoDescontos != null) {
				listaPlanoFinanceiroAlunoDescricaoDescontos.clear();
			}
			listaPlanoFinanceiroAlunoDescricaoDescontos = null;

			// ///////////// ANTIGO ////////////////
			// boletoBancarioRelVO.gerarIntrucoesPagamento(boletoBancarioRelVO);
		} else if (!configuracaoFinanceiroVO.getGerarBoletoComDescontoSemValidade()) {
			// adicionarCamposReferenteModeloBoleto(boletoBancarioRelVO, usuarioVO, configuracaoFinanceiroVO);
			criarListaDescontosSemDescontoProgressivo(boletoBancarioRelVO, configuracaoFinanceiroVO.getGerarBoletoComDescontoSemValidade(), configuracaoFinanceiroVO, listaPlanoDesconto, listaConvenios, planoDescontoContaReceberVOs, usuarioVO);
		}

		if (configuracaoFinanceiroVO.getGerarBoletoComDescontoSemValidade()) {
			// adicionarCamposReferenteModeloBoleto(boletoBancarioRelVO, usuarioVO, configuracaoFinanceiroVO);
			executarCalculoValorBoletoDescontoSemValidade(boletoBancarioRelVO, listaPlanoDesconto, listaConvenios, planoDescontoContaReceberVOs, configuracaoFinanceiroVO, usuarioVO);
		}
		if (configuracaoFinanceiroVO.getVencimentoParcelaDiaUtil()) {
			if (configuracaoFinanceiroVO.getAlterarDataVencimentoParcelaDiaUtil() || (getFacadeFactory().getContaReceberFacade().permiteRemessaBoletoAlunoVinculadoParceiro(boletoBancarioRelVO.getContareceber_codigo()) && 
                    boletoBancarioRelVO.getContareceber_situacao().equals("AR") && boletoBancarioRelVO.getContacorrente_habilitarRegistroRemessaOnline())) {
				ContaReceberVO obj = new ContaReceberVO();
				obj.setCodigo(boletoBancarioRelVO.getContareceber_codigo());
				getFacadeFactory().getContaReceberFacade().carregarDados(obj, NivelMontarDados.TODOS, configuracaoFinanceiroVO, usuarioVO);
				obj.setDataVencimento(boletoBancarioRelVO.getContareceber_datavencimentodiautil());
				if(boletoBancarioRelVO.getDataVencimentoRemessaOnline() != null) {
					obj.setDataVencimento(boletoBancarioRelVO.getDataVencimentoRemessaOnline());
			    }
				if(!Uteis.isAtributoPreenchido(boletoBancarioRelVO)) {
					boletoBancarioRelVO.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
				} 
				
				obj.criarBoleto(boletoBancarioRelVO.getContaCorrenteVO());
			}else if(boletoBancarioRelVO.getDataVencimentoRemessaOnline() != null) {
				boletoBancarioRelVO.setContareceber_datavencimento(boletoBancarioRelVO.getDataVencimentoRemessaOnline());
		    }else {
				boletoBancarioRelVO.setContareceber_datavencimento(boletoBancarioRelVO.getContareceber_dataoriginalvencimento());
			}
		}
	}

	public void executarCalculoValorBoletoDescontoSemValidade(BoletoBancarioRelVO boletoBancarioRelVO, List<PlanoDescontoVO> listaPlanoDesconto, List<ConvenioVO> listaConvenios, Map<Integer, List<PlanoDescontoContaReceberVO>> planoDescontoContaReceberVOs, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		boolean eMatricula = boletoBancarioRelVO.getContareceber_tipoorigem().equals("MAT")? true:false;
		DescontoProgressivoVO descontoProgressivoVO = null;
		if (boletoBancarioRelVO.getContareceber_descontoprogressivo() != null && boletoBancarioRelVO.getContareceber_descontoprogressivo() > 0) {
			descontoProgressivoVO = new DescontoProgressivoVO();
			descontoProgressivoVO.setNovoObj(false);
			descontoProgressivoVO.setCodigo(boletoBancarioRelVO.getContareceber_descontoprogressivo());
			descontoProgressivoVO.setUtilizarDiaFixo(boletoBancarioRelVO.getDescontoprogressivo_utilizarDiaFixo());
			descontoProgressivoVO.setUtilizarDiaUtil(boletoBancarioRelVO.getDescontoprogressivo_utilizarDiaUtil());
			if (boletoBancarioRelVO.getContareceber_contaEditadaManualmente()) {
				descontoProgressivoVO.setDescontoProgressivoEditadoManualmenteContaReceber(Boolean.TRUE);
			}
			descontoProgressivoVO.setDiaLimite1(boletoBancarioRelVO.getDescontoprogressivo_dialimite1());
			descontoProgressivoVO.setDiaLimite2(boletoBancarioRelVO.getDescontoprogressivo_dialimite2());
			descontoProgressivoVO.setDiaLimite3(boletoBancarioRelVO.getDescontoprogressivo_dialimite3());
			descontoProgressivoVO.setDiaLimite4(boletoBancarioRelVO.getDescontoprogressivo_dialimite4());
			descontoProgressivoVO.setPercDescontoLimite1(boletoBancarioRelVO.getDescontoprogressivo_percdescontolimite1());
			descontoProgressivoVO.setPercDescontoLimite2(boletoBancarioRelVO.getDescontoprogressivo_percdescontolimite2());
			descontoProgressivoVO.setPercDescontoLimite3(boletoBancarioRelVO.getDescontoprogressivo_percdescontolimite3());
			descontoProgressivoVO.setPercDescontoLimite4(boletoBancarioRelVO.getDescontoprogressivo_valordescontolimite4());
			descontoProgressivoVO.setValorDescontoLimite1(boletoBancarioRelVO.getDescontoprogressivo_valordescontolimite1());
			descontoProgressivoVO.setValorDescontoLimite2(boletoBancarioRelVO.getDescontoprogressivo_valordescontolimite2());
			descontoProgressivoVO.setValorDescontoLimite3(boletoBancarioRelVO.getDescontoprogressivo_valordescontolimite3());
			descontoProgressivoVO.setValorDescontoLimite4(boletoBancarioRelVO.getDescontoprogressivo_valordescontolimite4());
		}

		List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade = new ArrayList<PlanoFinanceiroAlunoDescricaoDescontosVO>(0);
		List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroAlunoDescricaoDescontosComValidade = new ArrayList<PlanoFinanceiroAlunoDescricaoDescontosVO>(0);
		List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroAlunoDescricaoDescontos = null;
		
			listaPlanoFinanceiroAlunoDescricaoDescontos = getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(eMatricula, boletoBancarioRelVO.getContareceber_valorBaseContaReceber(), 
				boletoBancarioRelVO.getContareceber_tipodesconto(), boletoBancarioRelVO.getContareceber_percDescontoAluno(), boletoBancarioRelVO.getContareceber_valordesconto(),  boletoBancarioRelVO.getDescontoValidoatedataparcela(),
				boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getContareceber_dataoriginalvencimento(), boletoBancarioRelVO.getOrdemDescontos(), descontoProgressivoVO, 
				listaPlanoDesconto, listaConvenios, boletoBancarioRelVO.getDiasVariacaoDataVencimento(), boletoBancarioRelVO.getContareceber_usadescontocompostoplanodesconto(), 
				configuracaoFinanceiroVO, 
				false, new Date(), boletoBancarioRelVO.getContareceber_matriculaaluno(), boletoBancarioRelVO.getCondicao_aplicarCalculoComBaseDescontosCalculados(), boletoBancarioRelVO.getCodigoCidadeUnidadeEnsino());
			
			listaPlanoFinanceiroAlunoDescricaoDescontos = ContaReceber.realizarValidacaoGeracaoPlanoFinanceiroAlunoDescricaoDescontoAposVencimento(listaPlanoFinanceiroAlunoDescricaoDescontos, Uteis.getDataFutura(boletoBancarioRelVO.getContareceber_datavencimento(), Calendar.DAY_OF_MONTH, 1), boletoBancarioRelVO.getContareceber_datavencimento(),  boletoBancarioRelVO.getContareceber_tipoorigem(),boletoBancarioRelVO.getDescontoValidoatedataparcela(), configuracaoFinanceiroVO, boletoBancarioRelVO.getCodigoCidadeUnidadeEnsino() );
			
			for(PlanoFinanceiroAlunoDescricaoDescontosVO planoFinanceiroAlunoDescricaoDescontosVO: listaPlanoFinanceiroAlunoDescricaoDescontos) {				
				if(planoFinanceiroAlunoDescricaoDescontosVO.getReferentePlanoFinanceiroAposVcto() || 
						(planoFinanceiroAlunoDescricaoDescontosVO.getDiaNrAntesVencimento() <= 0 && !planoFinanceiroAlunoDescricaoDescontosVO.getReferentePlanoFinanceiroAteVencimento())) {
					planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoProgressivo(0.0);
					if(boletoBancarioRelVO.getDescontoValidoatedataparcela()){
						planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoAluno(0.0);
					}
					listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade.add(planoFinanceiroAlunoDescricaoDescontosVO);
				}else {
					planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoConvenio(0.0);
					planoFinanceiroAlunoDescricaoDescontosVO.getListaDescontosConvenio().clear();
					if(!boletoBancarioRelVO.getDescontoValidoatedataparcela()){
						planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoAluno(0.0);
					}
					listaPlanoFinanceiroAlunoDescricaoDescontosComValidade.add(planoFinanceiroAlunoDescricaoDescontosVO);
				}
			}
			Double valorDescontoInstitucionalSemValidade = 0.0;
			if(!listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade.isEmpty()) {
				valorDescontoInstitucionalSemValidade = listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade.get(0).getValorDescontoInstituicao();
			}
			for(PlanoFinanceiroAlunoDescricaoDescontosVO planoFinanceiroAlunoDescricaoDescontosVO: listaPlanoFinanceiroAlunoDescricaoDescontosComValidade) {
				planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoInstituicao(planoFinanceiroAlunoDescricaoDescontosVO.getValorDescontoInstituicao()-valorDescontoInstitucionalSemValidade);
				if(planoFinanceiroAlunoDescricaoDescontosVO.getValorDescontoInstituicao() < 0) {
					planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoInstituicao(0.0);
				}
			}
		
		for (PlanoFinanceiroAlunoDescricaoDescontosVO plano : listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade) {
			plano.setValorDescontoRateio(boletoBancarioRelVO.getContareceber_valorDescontoRateio());
		}
		getFacadeFactory().getContaReceberFacade().validarRegrasParaFinanciamentoProprioConvenioImprimirBoleto(planoDescontoContaReceberVOs.get(boletoBancarioRelVO.getContareceber_codigo()), listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade,  boletoBancarioRelVO.getContareceber_codigo(), boletoBancarioRelVO.getContareceber_matriculaaluno(),  boletoBancarioRelVO.getContareceber_tipoorigem(),  new Date());
		getFacadeFactory().getContaReceberFacade().adicionarDescontoRateioEmPlanoFinanceiroAlunoDescricaoDescontosVO(listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade, boletoBancarioRelVO.getContareceber_valorBaseContaReceber(),  boletoBancarioRelVO.getContareceber_valorCusteadoContaReceber(),boletoBancarioRelVO.getContareceber_valorDescontoRateio(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getContareceber_datavencimento());
		/**
		 * Adicionada regra caso no planoFinanceiroAluno esteja marcado como descontoValidoAteDataParcela zere o valor do desconto do aluno, devido a existir uma insconsistência no ato de realizar os cálculos financeiros da ContaReceber e o mesmo não poder ser solucionado através da passagem deste parâmetro do PlanoFinanceiroAluno.
		 */
		Double descontoSemValidade = 0.0;
		if (listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade != null && !listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade.isEmpty()) {
			PlanoFinanceiroAlunoDescricaoDescontosVO p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade.get(0);
			descontoSemValidade = p.executarCalculoValorTotalDesconto();
		}
		
		
//		if(boletoBancarioRelVO.getContareceber_tipoorigem().equals(TipoOrigemContaReceber.MATRICULA.getValor()) 
//				|| boletoBancarioRelVO.getContareceber_tipoorigem().equals(TipoOrigemContaReceber.MENSALIDADE.getValor()) 
//				|| boletoBancarioRelVO.getContareceber_tipoorigem().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO.getValor()) 
//				|| boletoBancarioRelVO.getContareceber_tipoorigem().equals(TipoOrigemContaReceber.NEGOCIACAO.getValor())){ 
			if (boletoBancarioRelVO.getDescontoValidoatedataparcela()) {
//				Double descontoAluno = 0.0;
							
				for (PlanoFinanceiroAlunoDescricaoDescontosVO obj : listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade) {
//					descontoAluno = obj.getValorDescontoAluno();
					obj.setValorDescontoAluno(0.0);
				}
				for (PlanoFinanceiroAlunoDescricaoDescontosVO obj : listaPlanoFinanceiroAlunoDescricaoDescontosComValidade) {
					obj.setValorBase(obj.getValorBase()-descontoSemValidade);
//					obj.setValorDescontoAluno(descontoAluno);
					obj.setValorDescontoRateio(0.0);
				}
			} else if (!boletoBancarioRelVO.getDescontoValidoatedataparcela()) {
				for (PlanoFinanceiroAlunoDescricaoDescontosVO obj : listaPlanoFinanceiroAlunoDescricaoDescontosComValidade) {
					obj.setValorBase(obj.getValorBase()-descontoSemValidade);
					obj.setValorDescontoAluno(0.0);
					obj.setValorDescontoRateio(0.0);
				}
			}
//		}


		PlanoFinanceiroAlunoDescricaoDescontosVO p = null;
		if (listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade != null && !listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade.isEmpty()) {
			p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade.get(0);
			boletoBancarioRelVO.setContareceber_valor(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula).doubleValue());
			boletoBancarioRelVO.setContareceber_valorBaseContaReceber(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula).doubleValue());
			realizarAdicaoValoresAdicionaisBoleto(boletoBancarioRelVO, true);
//			if (boletoBancarioRelVO.getContareceber_acrescimo() == 0.0) {
//				regerarCodigoBarraTaxaBoleto(boletoBancarioRelVO, null);
//			}

		}else {		
			realizarAdicaoValoresAdicionaisBoleto(boletoBancarioRelVO, false);
		}
//		if (boletoBancarioRelVO.getContareceber_acrescimo() > 0 || boletoBancarioRelVO.getContareceber_valorIndiceReajustePorAtraso() != 0.0) {
//			boletoBancarioRelVO.setContareceber_valor(boletoBancarioRelVO.getContareceber_valor() + boletoBancarioRelVO.getContareceber_acrescimo() + boletoBancarioRelVO.getContareceber_valorIndiceReajustePorAtraso());
//			regerarCodigoBarraTaxaBoleto(boletoBancarioRelVO, null);
//		}
//		if (boletoBancarioRelVO.getContareceber_valorIndiceReajustePorAtraso() != 0.0) {
//			boletoBancarioRelVO.setContareceber_valor(boletoBancarioRelVO.getContareceber_valor() +  boletoBancarioRelVO.getContareceber_valorIndiceReajustePorAtraso());
//			regerarCodigoBarraTaxaBoleto(boletoBancarioRelVO, null);
//		}

		boletoBancarioRelVO.setPagamento1Parte1("");
		boletoBancarioRelVO.setPagamento1Parte2("");
		boletoBancarioRelVO.setPagamento2Parte1("");
		boletoBancarioRelVO.setPagamento2Parte2("");
		boletoBancarioRelVO.setPagamento3Parte1("");
		boletoBancarioRelVO.setPagamento3Parte2("");
		getFacadeFactory().getContaReceberFacade().validarRegrasParaFinanciamentoProprioConvenioImprimirBoleto(planoDescontoContaReceberVOs.get(boletoBancarioRelVO.getContareceber_codigo()), listaPlanoFinanceiroAlunoDescricaoDescontosComValidade,  boletoBancarioRelVO.getContareceber_codigo(), boletoBancarioRelVO.getContareceber_matriculaaluno(),  boletoBancarioRelVO.getContareceber_tipoorigem(),  new Date());
		boletoBancarioRelVO.gerarIntrucoesDescontos(boletoBancarioRelVO, listaPlanoFinanceiroAlunoDescricaoDescontosComValidade, listaPlanoDesconto, configuracaoFinanceiroVO);
	}

	// Método criado para calcular os descontos mesmo não tendo
	// descontoProgressivo
	public void criarListaDescontosSemDescontoProgressivo(BoletoBancarioRelVO boletoBancarioRelVO, Boolean gerarBoletoComDescontoSemValidade, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, List<PlanoDescontoVO> listaPlanoDesconto, List<ConvenioVO> listaConvenios, Map<Integer, List<PlanoDescontoContaReceberVO>> planoDescontoContaReceberVOs, UsuarioVO usuarioVO) throws Exception {
		boolean eMatricula = false;
		if (boletoBancarioRelVO.getContareceber_tipoorigem().equals("MAT")) {
			eMatricula = true;
		}
		// Pegar o número de dias de variação da data de vencimento na Matricula
		// Periodo Vencimento.
		// Integer diasVariacaoDataVencimento =
		// getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarDiasVariacaoDataVencimentoPeloCodigoContaReceber(boletoBancarioRelVO.getContareceber_codigo(),
		// null);

		List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroAlunoDescricaoDescontos = getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(eMatricula, boletoBancarioRelVO.getContareceber_valorBaseContaReceber(), boletoBancarioRelVO.getContareceber_tipoDescontoAluno(), boletoBancarioRelVO.getContareceber_percDescontoAluno(), boletoBancarioRelVO.getContareceber_valorDescontoAluno(), boletoBancarioRelVO.getDescontoValidoatedataparcela(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getContareceber_dataoriginalvencimento(), boletoBancarioRelVO.getOrdemDescontos(), null, listaPlanoDesconto, listaConvenios, 0, boletoBancarioRelVO.getContareceber_usadescontocompostoplanodesconto(), configuracaoFinanceiroVO, Boolean.FALSE, null, boletoBancarioRelVO.getContareceber_matriculaaluno(), boletoBancarioRelVO.getCondicao_aplicarCalculoComBaseDescontosCalculados(), boletoBancarioRelVO.getCodigoCidadeUnidadeEnsino());
		getFacadeFactory().getContaReceberFacade().validarRegrasParaFinanciamentoProprioConvenioImprimirBoleto(planoDescontoContaReceberVOs.get(boletoBancarioRelVO.getContareceber_codigo()), listaPlanoFinanceiroAlunoDescricaoDescontos,  boletoBancarioRelVO.getContareceber_codigo(), boletoBancarioRelVO.getContareceber_matriculaaluno(),  boletoBancarioRelVO.getContareceber_tipoorigem(),  new Date());
		
		listaPlanoFinanceiroAlunoDescricaoDescontos = ContaReceber.realizarValidacaoGeracaoPlanoFinanceiroAlunoDescricaoDescontoAposVencimento(listaPlanoFinanceiroAlunoDescricaoDescontos, Uteis.getDataFutura(boletoBancarioRelVO.getContareceber_datavencimento(), Calendar.DAY_OF_MONTH, 1), boletoBancarioRelVO.getContareceber_datavencimento(),  boletoBancarioRelVO.getContareceber_tipoorigem(),boletoBancarioRelVO.getDescontoValidoatedataparcela(), configuracaoFinanceiroVO, boletoBancarioRelVO.getCodigoCidadeUnidadeEnsino());
		
		if (!listaPlanoFinanceiroAlunoDescricaoDescontos.isEmpty() && listaPlanoFinanceiroAlunoDescricaoDescontos.get(listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 1).getValorDescontoAluno() > 0 && boletoBancarioRelVO.getDescontoValidoatedataparcela() && boletoBancarioRelVO.getContareceber_tipoorigem().equals("MEN")) {
			/**
			 * Criado por Rodrigo Wind 30/04/15 Adicionado esta regra abaixo pois quando no plano financeiro do aluno o desconto do aluno é valido até a data de vencimento e o mesmo possua desconto após o vencimento e não possua um plano de desconto até o vencimento é necessário adicionar na lista listaPlanoFinanceiroAlunoDescricaoDescontos mais um objeto que refere ao desconto após o vencimento sem este desconto não é apresentado no boleto bancário a linha na descrição dos desconto "Após Vencimento"
			 */
			if (!listaPlanoFinanceiroAlunoDescricaoDescontos.get(listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 1).getReferentePlanoFinanceiroAposVcto()) {
				// Abaixo chamamos o método que gera a lista de descontos, de forma a garantir que gere
				// a condição de pagamento que será aplicada para o aluno após o vencimento do boleto
				// Esta descrição é útil quando existem descontos que vencem na data de vencimento. Ou seja,
				// nestes casos é importante que o SEI imprima os valores a serem adotados após o pagamento
				// do boleto.
				Date dataReferenciaConsiderarBaixaTituloAposVcto = Uteis.getDataFutura(boletoBancarioRelVO.getContareceber_datavencimento(), Calendar.DAY_OF_MONTH, 1);
				List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaComDescontoAposVcto = getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAlunoPadrao(false, boletoBancarioRelVO.getContareceber_valorBaseContaReceber(), boletoBancarioRelVO.getContareceber_tipodesconto(), boletoBancarioRelVO.getContareceber_valordesconto(), boletoBancarioRelVO.getContareceber_valordesconto(), boletoBancarioRelVO.getDescontoValidoatedataparcela(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getOrdemDescontos(), null, listaPlanoDesconto, listaConvenios, 0, boletoBancarioRelVO.getContareceber_usadescontocompostoplanodesconto(), configuracaoFinanceiroVO.getVencimentoDescontoProgressivoDiaUtil(), Boolean.TRUE, dataReferenciaConsiderarBaixaTituloAposVcto, boletoBancarioRelVO.getContareceber_matriculaaluno(), boletoBancarioRelVO.getCondicao_aplicarCalculoComBaseDescontosCalculados(), boletoBancarioRelVO.getCodigoCidadeUnidadeEnsino());
				if ((listaComDescontoAposVcto != null) && (listaComDescontoAposVcto.size() > 0)) {
					if (!listaPlanoFinanceiroAlunoDescricaoDescontos.isEmpty() && listaPlanoFinanceiroAlunoDescricaoDescontos.get(listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 1).getDiaNrAntesVencimento().equals(0)) {
						listaPlanoFinanceiroAlunoDescricaoDescontos.get(listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 1).setReferentePlanoFinanceiroAteVencimento(true);
						listaPlanoFinanceiroAlunoDescricaoDescontos.get(listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 1).setDataLimiteAplicacaoDesconto(boletoBancarioRelVO.getContareceber_datavencimento());
					}
					PlanoFinanceiroAlunoDescricaoDescontosVO planoAposVcto = listaComDescontoAposVcto.get(listaComDescontoAposVcto.size() - 1);
					planoAposVcto.setReferentePlanoFinanceiroAposVcto(Boolean.TRUE);
					planoAposVcto.setValorDescontoAluno(0.0);
					listaPlanoFinanceiroAlunoDescricaoDescontos.add(planoAposVcto);
				}
			}
		}
		getFacadeFactory().getContaReceberFacade().adicionarDescontoRateioEmPlanoFinanceiroAlunoDescricaoDescontosVO(listaPlanoFinanceiroAlunoDescricaoDescontos, boletoBancarioRelVO.getContareceber_valorBaseContaReceber(),  boletoBancarioRelVO.getContareceber_valorCusteadoContaReceber(),boletoBancarioRelVO.getContareceber_valorDescontoRateio(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getContareceber_datavencimento());
		//Adiciona o valor de Reajuste da Diferença de parcela Recebida ou Enviada na Remessa
//        if (!boletoBancarioRelVO.getContaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().equals(BigDecimal.ZERO) 
//        		|| !boletoBancarioRelVO.getContaReceber_valorIndiceReajuste().equals(BigDecimal.ZERO)
//        		|| (Uteis.getData(boletoBancarioRelVO.getContareceber_dataoriginalvencimento()) != Uteis.getData(boletoBancarioRelVO.getContareceber_datavencimento()))) {
//        	if (!boletoBancarioRelVO.getContaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().equals(BigDecimal.ZERO)) {
//        		boletoBancarioRelVO.setContareceber_valorBaseContaReceber(boletoBancarioRelVO.getContareceber_valorBaseContaReceber() + boletoBancarioRelVO.getContaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue());
//        		boletoBancarioRelVO.setContareceber_valor(boletoBancarioRelVO.getContareceber_valor() + boletoBancarioRelVO.getContaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue());
//        	}
//        	regerarCodigoBarraTaxaBoleto(boletoBancarioRelVO, null);
//        }
		realizarAdicaoValoresAdicionaisBoleto(boletoBancarioRelVO, false);
		boletoBancarioRelVO.gerarIntrucoesDescontosSemDescontoProgressivo(boletoBancarioRelVO, listaPlanoFinanceiroAlunoDescricaoDescontos, listaPlanoDesconto, gerarBoletoComDescontoSemValidade, configuracaoFinanceiroVO);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see relatorio.negocio.jdbc.financeiro.BoletoBancarioRelInterfaceFacade# executarConsultaParametrizada(java.lang.Integer , int)
	 */
	public List<BoletoBancarioRelVO> executarConsultaParametrizada(Boolean trazerApenasAlunosAtivos, Integer contaReceber, List<Integer> listaContaReceber, String matricula, String ano, String semestre, String parcela, Integer curso, Integer turma, Date dataInicio, Date dataFim, Integer unidadeEnsino, UsuarioVO usuarioVO, String tipoImpressaoBoleto, Integer codigoRenegociacao, Integer responsavelFinanceiro, Integer centroReceita, Boolean permiteEmitirBoletoRecebido, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
		StringBuilder selectStr = new StringBuilder();
		List<Object> parametros = new ArrayList<Object>(0);
		selectStr.append(" select * from (");
		selectStr.append(" SELECT  ");
		// Comentado pelo fato de na Processus o aluno possuir mais de uma planoFinanceiroAluno para o mesmo periodo
		// Então estava duplicando boletos para o mesmo codigo de conta a receber
		// Foi posto então para pegar os dados da propria conta a receber cujos valores são atualizados pela Job do Rodrigo
		// Carlos 13/02/2014
		/**
		 * Comentado para utilizar a informação que está no conta a receber, pois existe um recurso que ao repassar pela matricula não alterar as contas a receber vencidas, o que ocasiona em divergência de dados.
		 * 
		 * @author Wellington Rodrigues - 24/08/2015
		 */
//        selectStr.append(" case when contareceber.tipoorigem = 'MAT' then planofinanceiroaluno.valorDescontoMatricula else ");
//        selectStr.append(" case when contareceber.tipoorigem = 'MEN' then planofinanceiroaluno.valorDescontoParcela else ");
//        selectStr.append(" contareceber.valorDesconto end end as valorDescontoParcela, ");
//        
//        selectStr.append(" case when contareceber.tipoorigem = 'MAT' then planofinanceiroaluno.tipoDescontoMatricula else ");
//        selectStr.append(" case when contareceber.tipoorigem = 'MEN' then planofinanceiroaluno.tipoDescontoParcela else ");
//        selectStr.append(" contareceber.tipoDesconto end end as tipoDescontoParcela, ");
//        
//        selectStr.append(" case when contareceber.tipoorigem = 'MAT' then planofinanceiroaluno.percDescontoMatricula else ");
//        selectStr.append(" case when contareceber.tipoorigem = 'MEN' then planofinanceiroaluno.percDescontoParcela else ");
//        selectStr.append(" contareceber.valorDesconto end end as percDescontoParcela, ");
        
        selectStr.append(" contareceber.valorDesconto as valorDescontoParcela, contareceber.acrescimo as contareceber_acrescimo, contareceber.valorIndiceReajustePorAtraso as contareceber_valorIndiceReajustePorAtraso, contareceber.registrocobrancacontareceber as contareceber_registrocobrancacontareceber, ");
        selectStr.append(" contareceber.tipodesconto as tipoDescontoParcela, contareceber.valordescontorateio, ");
        selectStr.append(" case when contareceber.tipodesconto = 'PO' then contareceber.valorDesconto else 0.0 end as percDescontoParcela, ");
        
        selectStr.append(" contareceber.codigo as contareceber_codigo, contareceber.codigobarra as contareceber_codigobarra, contareceber.matriculaaluno as contareceber_matriculaaluno, ");
        selectStr.append(" contareceber.data as contareceber_data, contareceber.tipoorigem as contareceber_tipoorigem, contareceber.situacao as contareceber_situacao, contareceber.descricaopagamento as contareceber_descricaopagamento, ");
        selectStr.append(" contareceber.descontoprogressivo as contareceber_descontoprogressivo, contareceber.datavencimento as contareceber_datavencimento, contareceber.tipopessoa as contaReceber_tipoPessoa, ");
        selectStr.append(" contareceber.valor as contareceber_valor, contareceber.valorBaseContaReceber as contareceber_valorBaseContaReceber, contareceber.valorCusteadoContaReceber as contareceber_valorCusteadoContaReceber, contareceber.valordesconto as contareceber_valordesconto, contareceber.tipodesconto as contareceber_tipodesconto, ");
        selectStr.append(" contareceber.ordemConvenio as contareceber_ordemConvenio, contareceber.ordemConvenioValorCheio as contareceber_ordemConvenioValorCheio, ");
        selectStr.append(" contareceber.ordemDescontoAluno as contareceber_ordemDescontoAluno, contareceber.ordemDescontoAlunoValorCheio as contareceber_ordemDescontoAlunoValorCheio, ");
        selectStr.append(" contareceber.ordemDescontoProgressivo as contareceber_ordemDescontoProgressivo, contareceber.usadescontocompostoplanodesconto as contareceber_usadescontocompostoplanodesconto, ");
        selectStr.append(" contareceber.ordemDescontoProgressivoValorCheio as contareceber_ordemDescontoProgressivoValorCheio, contareceber.ordemPlanoDesconto as contareceber_ordemPlanoDesconto, ");
        selectStr.append(" contareceber.ordemPlanoDescontoValorCheio as contareceber_ordemPlanoDescontoValorCheio, contareceber.juro as contareceber_juro, ");
        
        selectStr.append(" contareceber.contaEditadaManualmente as contareceber_contaEditadaManualmente, contareceber.diaLimite1 as contareceber_diaLimite1, contareceber.diaLimite2 as contareceber_diaLimite2, ");
        selectStr.append(" contareceber.diaLimite3 as contareceber_diaLimite3, contareceber.diaLimite4 as contareceber_diaLimite4, contareceber.utilizarDescontoProgressivoManual as contareceber_utilizarDescontoProgressivoManual, ");
        
        selectStr.append(" contareceber.juroporcentagem as contareceber_juroporcentagem, contareceber.multa as contareceber_multa, contareceber.multaporcentagem as contareceber_multaporcentagem, ");
        selectStr.append(" contareceber.nrdocumento as contareceber_nrdocumento, contareceber.nossonumero as contareceber_nossonumero, contareceber.parcela as contareceber_parcela, ");
        selectStr.append(" contareceber.origemnegociacaoreceber as contareceber_origemnegociacaoreceber, contacorrente.utilizaDadosInformadosCCparaGeracaoBoleto as contacorrente_utilizaDadosInformadosCCparaGeracaoBoleto, contareceber.contacorrente as contareceber_contacorrente, ");
        selectStr.append(" contareceber.descontoinstituicao as contareceber_descontoinstituicao, contareceber.descontoconvenio as contareceber_descontoconvenio, ");
        selectStr.append(" contareceber.linhadigitavelcodigobarras as contareceber_linhadigitavelcodigobarras, contacorrente.agencia as contacorrente_agencia, ");
        selectStr.append(" contareceber.unidadeensinoFinanceira as contareceber_unidadeensinoFinanceira, ");
        selectStr.append(" contacorrente.convenio as contacorrente_convenio, contacorrente.habilitarRegistroRemessaOnline as contacorrente_habilitarRegistroRemessaOnline, contacorrente.codigocedente as  contacorrente_codigocedente, contacorrente.digitocodigocedente as  contacorrente_digitocodigocedente, ");
        selectStr.append(" contacorrente.especieTituloBoleto as contacorrente_especieTituloBoleto, ");
        selectStr.append(" contaCorrente.taxaBoleto as contaCorrente_taxaBoleto, contacorrente.numero as contacorrente_numero, contacorrente.carteiraRegistrada as contacorrente_carteiraRegistrada, contacorrente.carteira as contacorrente_carteira, agencia.numeroagencia as agencia_numeroagencia, pessoa.nome as pessoa_nome, ");
        selectStr.append(" pessoa.isentarTaxaBoleto as pessoa_isentarTaxaBoleto, pessoa.endereco as pessoa_endereco, pessoa.complemento as pessoa_complemento, pessoa.numero as pessoa_numero, pessoa.setor as pessoa_setor, pessoa.cep as pessoa_cep, pessoa.cidade as pessoa_cidade, pessoa.cpf as pessoa_cpf, ");
        selectStr.append(" cidade.nome as cidade_nome, cidade.estado as cidade_estado, estado.nome as estado_nome, agencia.banco as agencia_banco, banco.nome as banco_nome, banco.nrbanco as banco_nrbanco, banco.digito as banco_digito,   ");
        selectStr.append(" banco.modeloBoletoMatricula as modeloBoletoMatricula_codigo, banco.modeloBoletoMensalidade as modeloBoletoMensalidade_codigo, banco.modeloBoletoMaterialDidatico as modeloBoletoMaterialDidatico_codigo,  banco.modeloBoletoRequerimento as modeloBoletoRequerimento_codigo, banco.modeloBoletoProcessoSeletivo as modeloBoletoProcessoSeletivo_codigo, banco.modeloBoletoOutros as modeloBoletoOutros_codigo, banco.modeloboletorenegociacao as modeloboletorenegociacao_codigo, banco.modeloGeracaoBoleto as modeloGeracaoBoleto, ");
        selectStr.append(" descontoprogressivo.dialimite1 as descontoprogressivo_dialimite1, descontoprogressivo.dialimite2 as descontoprogressivo_dialimite2, ");
        selectStr.append(" descontoprogressivo.dialimite3 as descontoprogressivo_dialimite3, descontoprogressivo.dialimite4 as descontoprogressivo_dialimite4, descontoprogressivo.utilizarDiaFixo as descontoprogressivo_utilizarDiaFixo, descontoprogressivo.utilizarDiaUtil as descontoprogressivo_utilizarDiaUtil, ");
        selectStr.append(" descontoprogressivo.percdescontolimite1 as descontoprogressivo_percdescontolimite1, descontoprogressivo.percdescontolimite2 as descontoprogressivo_percdescontolimite2, ");
        selectStr.append(" descontoprogressivo.percdescontolimite3 as descontoprogressivo_percdescontolimite3, descontoprogressivo.percdescontolimite4 as descontoprogressivo_percdescontolimite4, ");
        selectStr.append(" descontoprogressivo.valordescontolimite1 as descontoprogressivo_valordescontolimite1, descontoprogressivo.valordescontolimite2 as descontoprogressivo_valordescontolimite2, ");
        selectStr.append(" descontoprogressivo.valordescontolimite3 as descontoprogressivo_valordescontolimite3, descontoprogressivo.valordescontolimite4 as descontoprogressivo_valordescontolimite4, ");
        selectStr.append(" unidadeensino.numero as numeroUnidadeEnsino, unidadeensino.endereco as enderecoUnidadeEnsino, unidadeensino.telcomercial1 as telefoneUnidadeEnsino, unidadeensino.setor as setorUnidadeEnsino, unidadeensino.cep as cepUnidadeEnsino, unidadeensino.complemento as complUnidadeEnsino, cityUnidadeEnsino.nome as cidadeUnidadeEnsino, ufUnidadeEnsino.nome as estadoUnidadeEnsino, unidadeensino.telcomercial1 as contareceber_fonemantenedora, ");
        selectStr.append(" unidadeensino.mantenedora as contareceber_mantenedora, unidadeensino.razaosocial as contareceber_razaoSocialMantenedora,  unidadeensino.cnpj as contareceber_cnpj, contareceber.tipoboleto as tiboboletocontareceber, turno.nome as turnoMatricula,");        
        selectStr.append(" case when (parceiro.razaosocial <> '') then parceiro.razaosocial else parceiro.nome end parceiro_razaosocial, parceiro.isentarTaxaBoleto as parceiro_isentarTaxaBoleto, ");
        selectStr.append(" matriculaPeriodo.turma as codTurmaBase, matricula.situacao as situacaoMatricula,  ");
        selectStr.append(" case when contareceber.tipoorigem <> 'IPS' then curso.nome else (select curso.nome from inscricao inner join unidadeensinocurso unc on unc.codigo = inscricao.cursoopcao1 inner join curso on curso.codigo = unc.curso where inscricao.codigo = contareceber.codorigem::Integer) end as nomeCurso, ");
        selectStr.append(" agencia.digito AS agencia_digito, contacorrente.digito as contacorrente_digito, parceiro.telcomercial1 AS telparceiro, ");
        selectStr.append(" parceiro.endereco AS enderecoparceiro, parceiro.setor AS setorparceiro, parceiro.cep AS cepparceiro, ");
        selectStr.append(" parceiro.emitirBoletoEmNomeBeneficiado AS emitirBoletoEmNomeBeneficiadoparceiro,  ");
        selectStr.append(" case when (parceiro.cnpj <> '') then parceiro.cnpj else parceiro.cpf end cnpjparceiro, ");
        selectStr.append(" cidadeParceiro.nome as cidadeParceiro, estadoParceiro.nome as estadoParceiro, planofinanceiroaluno.descontoValidoatedataparcela, matriculaPeriodo.ano as anoMatriculaPeriodo, matriculaPeriodo.semestre as semestreMatriculaPeriodo, identificadorturma, utilizarDadosMatrizBoleto, ");
        selectStr.append(" matriculaperiodovencimento.diasVariacaoDataVencimento, responsavelFinanceiro.nome as responsavelFinanceiro_nome, responsavelFinanceiro.endereco as responsavelFinanceiro_endereco, responsavelFinanceiro.complemento as responsavelFinanceiro_complemento, responsavelFinanceiro.numero as responsavelFinanceiro_numero, ");
        selectStr.append(" responsavelFinanceiro.isentarTaxaBoleto as responsavelFinanceiro_isentarTaxaBoleto, responsavelFinanceiro.setor as responsavelFinanceiro_setor, responsavelFinanceiro.cep as responsavelFinanceiro_cep, responsavelFinanceiro.cidade as responsavelFinanceiro_cidade, ");
        selectStr.append(" responsavelFinanceiro.cpf as responsavelFinanceiro_cpf, cidadeRF.nome as cidadeRF_nome, cidadeRF.estado as cidadeRF_estado, estadoRF.nome as estadoRF_nome, ");
        selectStr.append(" pais.isentarTaxaBoleto as reponsavel_isentarTaxaBoleto, pais.nome as reponsavel_Financeiro, pais.cpf as responsavel_cpf, pais.endereco as responsavel_endereco, pais.numero as responsavel_numero, pais.complemento as responsavel_complemento, ");
        selectStr.append(" pais.setor as responsavel_setor, pais.cep as responsavel_cep, pais.cidade as responsavel_cidade, ");
        selectStr.append(" cidadePais.nome as cidadePais_nome, cidadePais.estado as cidadePais_estado, estadoPais.nome as estadoPais_nome,  ");
        selectStr.append(" fornecedor.codigo as fornecedor_codigo, fornecedor.nome as fornecedor_nome, fornecedor.razaoSocial as fornecedor_razaoSocial,   ");
        selectStr.append(" fornecedor.endereco as fornecedor_endereco, fornecedor.setor as fornecedor_setor, fornecedor.numero as fornecedor_numero, fornecedor.isentarTaxaBoleto as fornecedor_isentarTaxaBoleto,   ");
        selectStr.append(" fornecedor.complemento as fornecedor_complemento, fornecedor.CEP as fornecedor_CEP, fornecedor.CPF as fornecedor_CPF, fornecedor.CNPJ as fornecedor_CNPJ,   ");
        selectStr.append(" cidadeFornecedor.nome as cidadeFornecedor_nome,cidadeFornecedor.codigo as cidadeFornecedor_codigo,  estadoFornecedor.nome as estadoFornecedor_nome, estadoFornecedor.codigo as estadoFornecedor_codigo, ");
        selectStr.append(" fornecedor.tipoEmpresa as fornecedor_tipoEmpresa, condicao.aplicarCalculoComBaseDescontosCalculados,");
        selectStr.append(" contareceber.processamentointegracaofinanceiradetalhe, processamentointegracaofinanceiradetalhe.valor as \"processamentointegracaofinanceiradetalhe.valor\", processamentointegracaofinanceiradetalhe.juro as \"processamentointegracaofinanceiradetalhe.juro\", processamentointegracaofinanceiradetalhe.multa as \"processamentointegracaofinanceiradetalhe.multa\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.acrescimo as \"processamentointegracaofinanceiradetalhe.acrescimo\", processamentointegracaofinanceiradetalhe.desconto as \"processamentointegracaofinanceiradetalhe.desconto\", processamentointegracaofinanceiradetalhe.bolsa as \"processamentointegracaofinanceiradetalhe.bolsa\", processamentointegracaofinanceiradetalhe.valorreceber as \"processamentointegracaofinanceiradetalhe.valorreceber\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.codigopessoafinanceiro as \"processamentointegracaofinanceiradetalhe.codigopessoafinanceiro\" , ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.dataMaximaPagamento as \"processamentointegracaofinanceiradetalhe.dataMaximaPagamento\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.dataVencimentoBolsa as \"processamentointegracaofinanceiradetalhe.dataVencimentoBolsa\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.descontoPontualidade1 as \"processamentointegracaofinanceiradetalhe.descontoPontualidade1\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.dataVencimentoDescPontualidade1 as \"integracaofinanceira.dataVencimentoDescPontualidade1\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.descontoPontualidade2 as \"processamentointegracaofinanceiradetalhe.descontoPontualidade2\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.dataVencimentoDescPontualidade2 as \"integracaofinanceira.dataVencimentoDescPontualidade2\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.descontoPontualidade3 as \"processamentointegracaofinanceiradetalhe.descontoPontualidade3\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.dataVencimentoDescPontualidade3 as \"integracaofinanceira.dataVencimentoDescPontualidade3\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.descontoPontualidade4 as \"processamentointegracaofinanceiradetalhe.descontoPontualidade4\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.dataVencimentoDescPontualidade4 as \"integracaofinanceira.dataVencimentoDescPontualidade4\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.jurosApresentar as \"processamentointegracaofinanceiradetalhe.jurosApresentar\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.multaApresentar as \"processamentointegracaofinanceiradetalhe.multaApresentar\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.tipoLayoutArquivo as \"processamentointegracaofinanceiradetalhe.tipoLayoutArquivo\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.controleCliente as \"processamentointegracaofinanceiradetalhe.controleCliente\",  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe.dataVencimento as \"processamentointegracaofinanceiradetalhe.dataVencimento\", unidadeensino.cidade as \"unidadeensino.cidade\", contareceber.valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa,  ");
        selectStr.append(" contareceber.valorIndiceReajuste AS \"contareceber.valorIndiceReajuste\", contacorrente.cnab as contacorrente_cnab, parceiro.isentarmulta as parceiro_isentarmulta, parceiro.isentarjuro as parceiro_isentarjuro ");
        selectStr.append(" from contareceber ");
        selectStr.append(" inner join contacorrente on contareceber.contacorrente = contacorrente.codigo ");
        selectStr.append(" inner join agencia on agencia.codigo = contacorrente.agencia ");
        selectStr.append(" inner join banco on banco.codigo = agencia.banco ");
        selectStr.append(" inner join unidadeensino on contareceber.unidadeensinoFinanceira = unidadeensino.codigo ");
        selectStr.append(" left join processamentointegracaofinanceiradetalhe on processamentointegracaofinanceiradetalhe.codigo = contareceber.processamentointegracaofinanceiradetalhe ");
        selectStr.append(" left join descontoprogressivo on contareceber.descontoprogressivo = descontoprogressivo.codigo ");
        selectStr.append(" left join matricula on contareceber.matriculaaluno = matricula.matricula ");
        
        //selectStr.append(" left join planofinanceiroaluno on planofinanceiroaluno.matricula = matricula.matricula ");
        selectStr.append(" left join planofinanceiroaluno on planofinanceiroaluno.matriculaperiodo = contareceber.matriculaperiodo ");
        
        selectStr.append(" left join turno on turno.codigo = matricula.turno ");
        selectStr.append(" left join curso on matricula.curso = curso.codigo ");
        selectStr.append(" left join matriculaperiodo on matriculaperiodo.codigo = contareceber.matriculaperiodo ");
        selectStr.append(" left join condicaopagamentoPlanoFinanceiroCurso condicao on condicao.codigo = matriculaPeriodo.condicaopagamentoPlanoFinanceiroCurso ");
        selectStr.append(" left join parceiro on contareceber.parceiro = parceiro.codigo ");
        selectStr.append(" left join cidade as cidadeParceiro on cidadeParceiro.codigo=parceiro.cidade ");
        selectStr.append(" left join estado as estadoParceiro on cidadeParceiro.estado = estadoParceiro.codigo ");
        selectStr.append(" left join pessoa on (contareceber.pessoa = pessoa.codigo or matricula.aluno = pessoa.codigo) ");
        selectStr.append(" left join filiacao on filiacao.codigo = (select f.codigo from  filiacao f where f.aluno=pessoa.codigo and f.responsavelFinanceiro = true limit 1)");
        selectStr.append(" left join pessoa as pais ON filiacao.pais = pais.codigo ");
        selectStr.append(" left join pessoa as responsavelFinanceiro ON responsavelFinanceiro.codigo = contaReceber.responsavelFinanceiro ");
        selectStr.append(" left join cidade as cidadeRF on cidadeRF.codigo=responsavelFinanceiro.cidade ");
        selectStr.append(" left join estado as estadoRF on estadoRF.codigo=cidadeRF.estado ");
        selectStr.append(" left join cidade on cidade.codigo=pessoa.cidade ");
        selectStr.append(" left join estado on cidade.estado = estado.codigo ");
        selectStr.append(" left join cidade as cidadePais on cidadePais.codigo=pais.cidade ");
        selectStr.append(" left join estado as estadoPais on cidadePais.estado = estadoPais.codigo ");
        selectStr.append(" left join cidade cityUnidadeEnsino on cityUnidadeEnsino.codigo = unidadeensino.cidade ");
        selectStr.append(" left join estado ufUnidadeEnsino on ufUnidadeEnsino.codigo = cityUnidadeEnsino.estado ");
        selectStr.append(" left join turma on turma.codigo = matriculaperiodo.turma ");
        selectStr.append(" left join fornecedor on fornecedor.codigo = contaReceber.fornecedor ");
        selectStr.append(" left join cidade cidadeFornecedor on cidadeFornecedor.codigo = fornecedor.cidade ");
        selectStr.append(" left join estado estadoFornecedor on estadoFornecedor.codigo = cidadeFornecedor.estado ");
        selectStr.append(" left join matriculaperiodovencimento on matriculaperiodovencimento.contareceber = contareceber.codigo and matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo");
        selectStr.append(" where 1=1 ");
        if (!permiteEmitirBoletoRecebido) {
        	selectStr.append(" and contareceber.situacao = 'AR' ");
        }
        selectStr.append(" and contacorrente.bloquearemissaoboleto = false ");
		if(filtroRelatorioFinanceiroVO != null) {
			selectStr.append(" and ").append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "contareceber"));
		}
		if(filtroRelatorioAcademicoVO != null) {
			selectStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		}
        String and = " and ";
        if (trazerApenasAlunosAtivos != null && trazerApenasAlunosAtivos) {
            selectStr.append(and).append(" matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' ");
        }
        if (contaReceber != null && !contaReceber.equals(0) && listaContaReceber == null) {
            selectStr.append(and).append(" contareceber.codigo = ").append(contaReceber);
        } else if (listaContaReceber != null && !listaContaReceber.isEmpty()) {
        	boolean virgula = false;
        	selectStr.append(and).append(" contareceber.registrocobrancacontareceber is null and contareceber.codigo in(");
        	for (Integer codigoContaReceberAgrupada : listaContaReceber) {
        		if (!virgula) {
        			selectStr.append(codigoContaReceberAgrupada);
        		} else {
        			selectStr.append(", ").append(codigoContaReceberAgrupada);
        		}
        		virgula = true;
			}
        	selectStr.append(") ");
        } else {
        	selectStr.append(and).append(" contareceber.registrocobrancacontareceber is null ");
            if (ano != null && !ano.equals("")) {
                selectStr.append(and).append(" (case when contareceber.matriculaperiodo is not null and contareceber.matriculaperiodo > 0 then matriculaperiodo.ano = ? else extract(year from contareceber.datavencimento) = ? end)" );
                parametros.add(ano);
                parametros.add(Integer.valueOf(ano));
            }
            if (semestre != null && !semestre.equals("")) {
                selectStr.append(and).append(" (case when contareceber.matriculaperiodo is not null and contareceber.matriculaperiodo > 0 then matriculaperiodo.semestre = ? ");
                selectStr.append(semestre.equals("1") ? "else extract(month from contareceber.datavencimento) < 7 end) " : "else extract(month from contareceber.datavencimento) >= 7 end) ");
                parametros.add(semestre);
            }
            if (parcela != null && !parcela.equals("")) {
                selectStr.append(and).append(" contareceber.parcela = ? ");
                parametros.add(parcela.trim());
            }
            if (matricula != null && !matricula.equals("")) {
                selectStr.append(and).append(" matricula.matricula = ? ");
                parametros.add(matricula);
            }
            if (curso != null && !curso.equals(0)) {
                selectStr.append(and).append(" curso.codigo = ").append(curso);
            }
            if (turma != null && !turma.equals(0)) {
                selectStr.append(and).append(" (case when contareceber.matriculaperiodo is not null and contareceber.matriculaperiodo > 0 then matriculaperiodo.turma = ").append(turma);
                selectStr.append(" else case when contareceber.turma is not null and contareceber.turma > 0 then contareceber.turma = ").append(turma).append(" end end) ");
            }
            if (dataInicio != null) {
                selectStr.append(and).append(" contareceber.dataVencimento >= '").append(dataInicio).append("'");
            }
            if (dataFim != null) {
                selectStr.append(and).append(" contareceber.dataVencimento <= '").append(dataFim).append("'");
            }
            if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
                selectStr.append(and).append(" contareceber.unidadeensino = ").append(unidadeEnsino).append("");
            }
            if (responsavelFinanceiro != null && !responsavelFinanceiro.equals(0)) {
                selectStr.append(and).append(" (pessoa.codigo = ").append(responsavelFinanceiro).append(" or responsavelFinanceiro.codigo = ").append(responsavelFinanceiro).append(" or pais.codigo = ").append(responsavelFinanceiro).append(")");
            }
            if (tipoImpressaoBoleto.equals("boletoParceiro")) {
                selectStr.append(and).append(" ((contareceber.tipoorigem = 'BCC' or contareceber.tipoorigem = 'NCR') AND contareceber.parceiro is not null)");
            } else if (tipoImpressaoBoleto.equals("boletoAluno")) {
                selectStr.append(and).append(" contareceber.tipoorigem <> 'BCC' AND contareceber.parceiro is null");
            }
            if (codigoRenegociacao != null && !codigoRenegociacao.equals(0)) {
                selectStr.append(and).append(" contareceber.codigoRenegociacao = ").append(codigoRenegociacao).append("");
            }
            if(centroReceita != 0) {
            	selectStr.append(and).append("exists (select centroresultadoorigem.codigo from centroresultadoorigem where codorigem::int = contareceber.codigo and tipocentroresultadoorigem = 'CONTA_RECEBER' and centroreceita = ").append(centroReceita).append(")");
            }
            selectStr.append(" AND (select formapagamentonegociacaorecebimentocartaocredito.codigo from formapagamentonegociacaorecebimentocartaocredito where contareceber = contareceber.codigo ");
            selectStr.append(" and tipofinanciamento = 'INSTITUICAO' and situacao <> 'CF' limit 1) is null ");
        }

        selectStr.append(" ) as t ");
        selectStr.append(" group by valorDescontoParcela, valordescontorateio,  tipoDescontoParcela, percDescontoParcela, contareceber_codigo, contareceber_codigobarra, contareceber_matriculaaluno, contareceber_data,  contareceber_tipoorigem, contareceber_situacao,  contareceber_descricaopagamento, contareceber_registrocobrancacontareceber, ");
        selectStr.append(" contareceber_descontoprogressivo, contareceber_datavencimento, contaReceber_tipoPessoa, contareceber_valor, contareceber_valorCusteadoContaReceber, contareceber_valorBaseContaReceber, contareceber_valordesconto,  contareceber_tipodesconto,  ");
        selectStr.append(" contareceber_ordemConvenio, contareceber_ordemConvenioValorCheio, contareceber_ordemDescontoAluno, contareceber_ordemDescontoAlunoValorCheio, contareceber_acrescimo, contareceber_valorIndiceReajustePorAtraso, ");
        selectStr.append(" contareceber_ordemDescontoProgressivo, contareceber_usadescontocompostoplanodesconto, contareceber_ordemDescontoProgressivoValorCheio, contareceber_ordemPlanoDesconto,  ");
        selectStr.append(" contareceber_ordemPlanoDescontoValorCheio, contareceber_juro, contareceber_juroporcentagem, contareceber_multa, contareceber_multaporcentagem,  ");
        
        selectStr.append(" contareceber_contaEditadaManualmente, contareceber_diaLimite1, contareceber_diaLimite2, ");
        selectStr.append(" contareceber_diaLimite3, contareceber_diaLimite4, contareceber_utilizarDescontoProgressivoManual, ");
        
        selectStr.append(" contareceber_nrdocumento,  contareceber_nossonumero, contareceber_parcela, contareceber_origemnegociacaoreceber, contacorrente_utilizaDadosInformadosCCparaGeracaoBoleto, contareceber_contacorrente, contacorrente_especieTituloBoleto, ");
        selectStr.append(" contareceber_descontoinstituicao,  contareceber_descontoconvenio, contareceber_linhadigitavelcodigobarras, tiboboletocontareceber,	                   ");
        selectStr.append(" contareceber_unidadeensinoFinanceira, ");
        selectStr.append(" contacorrente_agencia, contacorrente_convenio, contacorrente_habilitarRegistroRemessaOnline, contacorrente_codigocedente, contacorrente_digitocodigocedente, contacorrente_numero, contacorrente_carteira, contacorrente_digito, agencia_numeroagencia, agencia_banco, agencia_digito ");
        selectStr.append(" , banco_nome,  banco_nrbanco, banco_digito, descontoprogressivo_dialimite1, descontoprogressivo_dialimite2, descontoprogressivo_utilizarDiaFixo, descontoprogressivo_utilizarDiaUtil,   ");
        selectStr.append(" descontoprogressivo_dialimite3, descontoprogressivo_dialimite4, descontoprogressivo_percdescontolimite1, descontoprogressivo_percdescontolimite2,  ");
        selectStr.append(" descontoprogressivo_percdescontolimite3, descontoprogressivo_percdescontolimite4, situacaoMatricula, descontoValidoatedataparcela ");
        selectStr.append(" , parceiro_razaosocial, telparceiro, enderecoparceiro, emitirBoletoEmNomeBeneficiadoparceiro, setorparceiro,  cepparceiro, cnpjparceiro, parceiro_isentarTaxaBoleto  ");
        selectStr.append(" ,numeroUnidadeEnsino, enderecoUnidadeEnsino, telefoneUnidadeEnsino, setorUnidadeEnsino, cepUnidadeEnsino, complUnidadeEnsino, contareceber_fonemantenedora, contareceber_mantenedora, contareceber_cnpj ");
        selectStr.append(" , turnoMatricula , pessoa_nome, pessoa_endereco, pessoa_setor, pessoa_cep, pessoa_cidade, pessoa_cpf, nomeCurso, diasVariacaoDataVencimento ");
        selectStr.append(" , cidade_nome, cidade_estado , cidadeParceiro, identificadorTurma, reponsavel_Financeiro, cidadeUnidadeEnsino, estadoUnidadeEnsino , estado_nome , estadoParceiro, codTurmaBase, anoMatriculaPeriodo, semestreMatriculaPeriodo, ");
        selectStr.append(" descontoprogressivo_valordescontolimite1, descontoprogressivo_valordescontolimite2, ");
        selectStr.append(" descontoprogressivo_valordescontolimite3, descontoprogressivo_valordescontolimite4, t.modeloboletomensalidade_codigo, t.modeloboletomatricula_codigo, t.modeloboletorequerimento_codigo, t.modeloboletoprocessoseletivo_codigo, t.modeloboletooutros_codigo, t.modeloboletorenegociacao_codigo,  modeloGeracaoBoleto, ");
        selectStr.append(" responsavelFinanceiro_nome, responsavelFinanceiro_endereco, utilizarDadosMatrizBoleto, contareceber_razaoSocialMantenedora, ");
        selectStr.append(" responsavelFinanceiro_setor, responsavelFinanceiro_cep, responsavelFinanceiro_cidade, ");
        selectStr.append(" responsavelFinanceiro_cpf, cidadeRF_nome, cidadeRF_estado , estadoRF_nome, ");
        selectStr.append(" responsavel_cpf, responsavel_endereco, responsavel_setor, responsavel_cep, responsavel_cidade,  ");
        selectStr.append(" fornecedor_codigo, fornecedor_nome, fornecedor_razaoSocial,   ");
        selectStr.append(" fornecedor_endereco, fornecedor_setor, fornecedor_numero, fornecedor_isentarTaxaBoleto,  ");
        selectStr.append(" fornecedor_complemento, fornecedor_CEP, fornecedor_CPF, fornecedor_CNPJ,   ");
        selectStr.append(" cidadeFornecedor_nome, cidadeFornecedor_codigo, estadoFornecedor_nome, estadoFornecedor_codigo, fornecedor_tipoEmpresa, contacorrente_carteiraregistrada, ");
        
        selectStr.append(" cidadePais_nome, cidadePais_estado, estadoPais_nome, reponsavel_isentarTaxaBoleto, responsavelFinanceiro_isentarTaxaBoleto, contaCorrente_taxaBoleto, pessoa_isentarTaxaBoleto, responsavelFinanceiro_complemento, responsavel_complemento, pessoa_complemento, responsavelFinanceiro_numero, responsavel_numero, pessoa_numero, ");
        selectStr.append(" aplicarCalculoComBaseDescontosCalculados,  ");
        selectStr.append(" processamentointegracaofinanceiradetalhe, \"processamentointegracaofinanceiradetalhe.valor\", \"processamentointegracaofinanceiradetalhe.juro\", \"processamentointegracaofinanceiradetalhe.multa\",  ");
        selectStr.append(" \"processamentointegracaofinanceiradetalhe.acrescimo\", \"processamentointegracaofinanceiradetalhe.desconto\", \"processamentointegracaofinanceiradetalhe.bolsa\", \"processamentointegracaofinanceiradetalhe.valorreceber\",  ");
        selectStr.append(" \"processamentointegracaofinanceiradetalhe.codigopessoafinanceiro\",  ");
        selectStr.append(" \"processamentointegracaofinanceiradetalhe.dataMaximaPagamento\",  ");
        selectStr.append(" \"processamentointegracaofinanceiradetalhe.dataVencimentoBolsa\",  ");
        selectStr.append(" \"processamentointegracaofinanceiradetalhe.descontoPontualidade1\",  ");
        selectStr.append(" \"integracaofinanceira.dataVencimentoDescPontualidade1\",  ");
        selectStr.append(" \"processamentointegracaofinanceiradetalhe.descontoPontualidade2\",  ");
        selectStr.append(" \"integracaofinanceira.dataVencimentoDescPontualidade2\",  ");
        selectStr.append(" \"processamentointegracaofinanceiradetalhe.descontoPontualidade3\",  ");
        selectStr.append(" \"integracaofinanceira.dataVencimentoDescPontualidade3\",  ");
        selectStr.append(" \"processamentointegracaofinanceiradetalhe.descontoPontualidade4\",  ");
        selectStr.append(" \"integracaofinanceira.dataVencimentoDescPontualidade4\",  ");
        selectStr.append(" \"processamentointegracaofinanceiradetalhe.jurosApresentar\",  ");
        selectStr.append(" \"processamentointegracaofinanceiradetalhe.multaApresentar\",  ");
        selectStr.append(" \"processamentointegracaofinanceiradetalhe.controleCliente\",  ");
        selectStr.append(" \"processamentointegracaofinanceiradetalhe.dataVencimento\",  ");
        selectStr.append(" \"processamentointegracaofinanceiradetalhe.tipoLayoutArquivo\",  ");
        selectStr.append(" \"unidadeensino.cidade\", valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa, modeloBoletoMaterialDidatico_codigo, \"contareceber.valorIndiceReajuste\", contacorrente_cnab, parceiro_isentarmulta, parceiro_isentarjuro ");                        
        selectStr.append(" order by nomeCurso, turnoMatricula, pessoa_nome, fornecedor_nome, contareceber_datavencimento");
       //System.out.println(selectStr.toString());
        boolean validarRegistroCobranca = (contaReceber != null && !contaReceber.equals(0) && listaContaReceber == null);
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString(), parametros.toArray()), validarRegistroCobranca, usuarioVO);
    }

	public List<String> executarConsultaParcelasFiltro(String filtrarPor, Integer contaReceber, String matricula, String ano, String semestre, Integer curso, Integer turma, Integer unidadeEnsino, Integer responsavelFinanceiro, Integer centroReceita, Date dataInicio, Date dataFim, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
		StringBuilder selectStr = new StringBuilder();
		selectStr.append("SELECT distinct contareceber.parcela ");
		selectStr.append("from contareceber ");
		selectStr.append("inner join contacorrente on contareceber.contacorrente = contacorrente.codigo ");		
		selectStr.append("left join filiacao on filiacao.codigo = (select f.codigo from  filiacao f where f.aluno = contareceber.pessoa and f.responsavelFinanceiro = true limit 1)");
		selectStr.append("left join descontoprogressivo on contareceber.descontoprogressivo = descontoprogressivo.codigo ");
		selectStr.append("inner join agencia on agencia.codigo = contacorrente.agencia ");
		selectStr.append("left join matricula on contareceber.matriculaaluno = matricula.matricula ");
		selectStr.append("left join pessoa on contareceber.pessoa = pessoa.codigo ");
		selectStr.append("left join cidade on cidade.codigo=pessoa.cidade ");
		selectStr.append("left join estado on cidade.estado = estado.codigo ");
		selectStr.append("inner join unidadeensino on contareceber.unidadeensino = unidadeensino.codigo ");
		selectStr.append("left join matriculaperiodo on matriculaperiodo.codigo = contareceber.matriculaperiodo ");		
		selectStr.append("left join turma on matriculaperiodo.turma = turma.codigo ");
		selectStr.append("left join curso on matricula.curso = curso.codigo ");
		selectStr.append("inner join banco on banco.codigo = agencia.banco ");
		selectStr.append("where contareceber.situacao = 'AR' ");
		String and = " and ";
		if (contaReceber != null && !contaReceber.equals(0)) {
			selectStr.append(and).append(" contareceber.codigo = ").append(contaReceber);
			and = " and ";
		} else {
			if(filtroRelatorioFinanceiroVO != null) {
				selectStr.append(and).append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "contareceber"));
				and = " and ";
			}
			if(filtroRelatorioAcademicoVO != null && filtrarPor.equals("turma")) {
				selectStr.append(and).append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
				and = " and ";
			}
			if (ano != null && !ano.equals("") && !filtrarPor.equals("responsavelFinanceiro")) {
				selectStr.append(and).append(" matriculaperiodo.ano = '").append(ano).append("'");
				and = " and ";
			}			
			if (semestre != null && !semestre.equals("") && !filtrarPor.equals("responsavelFinanceiro")) {
				selectStr.append(and).append(" matriculaperiodo.semestre = '").append(semestre).append("'");
				and = " and ";
			}
			if (matricula != null && !matricula.equals("") && filtrarPor.equals("aluno")) {
				selectStr.append(and).append(" matricula.matricula = '").append(matricula).append("'");
				and = " and ";
			}
			if (Uteis.isAtributoPreenchido(responsavelFinanceiro) && filtrarPor.equals("responsavelFinanceiro")) {
				selectStr.append(and).append(" (contareceber.pessoa = ").append(responsavelFinanceiro).append(" or contareceber.responsavelFinanceiro = ").append(responsavelFinanceiro).append(" or filiacao.pais = ").append(responsavelFinanceiro).append(")");
				and = " and ";
			}
			if (curso != null && !curso.equals(0)) {
				selectStr.append(and).append(" curso.codigo = ").append(curso);
				and = " and ";
			}
			if (turma != null && !turma.equals(0) && filtrarPor.equals("turma")) {
				selectStr.append(and).append(" turma.codigo = '").append(turma).append("'");
				and = " and ";
			}
			if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
				selectStr.append(and).append(" unidadeEnsino.codigo = '").append(unidadeEnsino).append("'");
				and = " and ";
			}
			if(Uteis.isAtributoPreenchido(centroReceita)) {
				selectStr.append(and).append(" contareceber.centroreceita = '").append(unidadeEnsino).append("'");
				and = " and ";
			}
			if (dataInicio != null) {
				selectStr.append(and).append(" contareceber.dataVencimento >= '").append(dataInicio).append("'");
				and = " and ";
			}
			if (dataFim != null) {
				selectStr.append(and).append(" contareceber.dataVencimento <= '").append(dataFim).append("'");				
			}
			
			selectStr.append("order by contareceber.parcela desc");
		}

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());

		List<String> listaResultado = new ArrayList<String>();
		while (resultado.next()) {
			listaResultado.add(resultado.getString(1));
		}
		return listaResultado;

	}

	public List<String> executarConsultaParcelasFiltro(Integer codigoSacado, Date dataIni, Date dataFim, Integer unidadeEnsino) throws Exception {
		StringBuilder selectStr = new StringBuilder();
		selectStr.append("SELECT DISTINCT contareceber.parcela ");
		selectStr.append("FROM contareceber ");
		selectStr.append("LEFT JOIN funcionario ON (funcionario.codigo = contareceber.funcionario) ");
		selectStr.append("LEFT JOIN parceiro ON (parceiro.codigo = contareceber.parceiro) ");
		selectStr.append("LEFT JOIN pessoa AS pessoafuncionario ON (pessoafuncionario.codigo = funcionario.pessoa) ");
		selectStr.append("LEFT JOIN matricula ON (matricula.matricula = contareceber.matriculaaluno) ");
		selectStr.append("LEFT JOIN pessoa AS pessoamatricula ON (matricula.aluno = pessoamatricula.codigo) ");
		selectStr.append("LEFT JOIN pessoa AS pessoacANDidato ON (contareceber.cANDidato = pessoacANDidato.codigo) ");
		selectStr.append("LEFT JOIN pessoa ON (contareceber.pessoa = pessoa.codigo) ");
		selectStr.append("LEFT JOIN pessoa as responsavelFinanceiro ON (contareceber.responsavelFinanceiro = responsavelFinanceiro.codigo) ");
		selectStr.append("inner JOIN contareceberrecebimento on contareceber.codigo = contareceberrecebimento.contareceber ");
		selectStr.append("inner JOIN unidadeensino on contareceber.unidadeensinofinanceira = unidadeensino.codigo ");
		selectStr.append("where contareceber.situacao = 'RE' ");
		 
		if (dataIni != null) {
			selectStr.append(" AND contareceberrecebimento.datarecebimento >= '").append(Uteis.getDataBD0000(dataIni)).append("' ");
		}
		if (dataFim != null) {
			selectStr.append(" AND contareceberrecebimento.datarecebimento <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		}
		selectStr.append("  AND (pessoafuncionario.codigo = ").append(codigoSacado).append(" OR ");
		selectStr.append("  parceiro.codigo = ").append(codigoSacado).append(" OR ");
		selectStr.append("  pessoamatricula.codigo = ").append(codigoSacado).append(" OR ");
		selectStr.append("  pessoacANDidato.codigo = ").append(codigoSacado).append(" OR ");
		selectStr.append("  responsavelFinanceiro.codigo = ").append(codigoSacado).append(" OR ");
		selectStr.append("  pessoa.codigo = ").append(codigoSacado).append(")  ");

		if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
			selectStr.append(" AND unidadeEnsino.codigo = '").append(unidadeEnsino).append("'");
		}
		selectStr.append("ORDER BY contareceber.parcela desc");

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());

		List<String> listaResultado = new ArrayList<String>();
		while (resultado.next()) {
			listaResultado.add(resultado.getString(1));
		}
		return listaResultado;

	}

	public List<String> executarConsultaParcelasTurma(Integer codigoTurma, Date dataIni, Date dataFim) throws Exception {
		StringBuilder selectStr = new StringBuilder();
		selectStr.append("SELECT DISTINCT contareceber.parcela ");
		selectStr.append("FROM contareceber ");
		selectStr.append("inner JOIN contareceberrecebimento on contareceber.codigo = contareceberrecebimento.contareceber ");
		selectStr.append("left JOIN matriculaperiodo on contareceber.matriculaperiodo = matriculaperiodo.codigo ");
		selectStr.append("left JOIN turma on turma.codigo = matriculaperiodo.turma ");
		selectStr.append("where contareceber.situacao = 'RE' and (Turma.codigo = " + codigoTurma + " or contareceber.turma = " + codigoTurma + ") ");

		if (dataIni != null) {
			selectStr.append(" AND contareceberrecebimento.datarecebimento >= '").append(Uteis.getDataBD0000(dataIni)).append("' ");
		}
		if (dataFim != null) {
			selectStr.append(" AND contareceberrecebimento.datarecebimento <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		}

		selectStr.append("ORDER BY contareceber.parcela desc");

		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());

		List<String> listaResultado = new ArrayList<String>();
		while (resultado.next()) {
			listaResultado.add(resultado.getString(1));
		}
		return listaResultado;

	}

	public List<BoletoBancarioRelVO> montarDadosConsulta(SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		List<BoletoBancarioRelVO> boletoBancarioRelVOs = new ArrayList<BoletoBancarioRelVO>(0);
		while (dadosSQL.next()) {
			boletoBancarioRelVOs.add(montarDados(dadosSQL, usuarioVO));
		}
		return boletoBancarioRelVOs;
	}

    public List<BoletoBancarioRelVO> montarDadosConsulta(SqlRowSet dadosSQL, boolean validarRegistroCobranca, UsuarioVO usuarioVO) throws Exception {
        List<BoletoBancarioRelVO> boletoBancarioRelVOs = new ArrayList<BoletoBancarioRelVO>(0);
        while (dadosSQL.next()) {
        	if (validarRegistroCobranca && dadosSQL.getInt("contareceber_registrocobrancacontareceber") > 0) {
        		throw new Exception("Boleto não pode ser impresso, pois foi enviado para cobrança!");
        	}
            boletoBancarioRelVOs.add(montarDados(dadosSQL, usuarioVO));
        }
        return boletoBancarioRelVOs;
    }
    
    /**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>BoletoBancarioRelVO</code>.
	 *
	 * @return O objeto da classe <code>BoletoBancarioRelVO</code> com os dados devidamente montados.
	 */
	public BoletoBancarioRelVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuarioVO) throws Exception {
		BoletoBancarioRelVO obj = new BoletoBancarioRelVO();
		obj.setContareceber_codigo(dadosSQL.getInt("contareceber_codigo"));
		obj.setContareceber_codigobarra(dadosSQL.getString("contareceber_codigobarra"));
		obj.setContareceber_matriculaaluno(dadosSQL.getString("contareceber_matriculaaluno"));
		obj.setContareceber_data(dadosSQL.getDate("contareceber_data"));
		obj.setContareceber_tipoorigem(dadosSQL.getString("contareceber_tipoorigem"));
		obj.setContareceber_situacao(dadosSQL.getString("contareceber_situacao"));
		obj.setContareceber_descricaopagamento(dadosSQL.getString("contareceber_descricaopagamento"));
		obj.setContareceber_datavencimento(dadosSQL.getDate("contareceber_datavencimento"));
		obj.setContareceber_acrescimo(Uteis.arrendondarForcando2CadasDecimais(dadosSQL.getDouble("contareceber_acrescimo")));
		obj.setContareceber_valorIndiceReajustePorAtraso(Uteis.arrendondarForcando2CadasDecimais(dadosSQL.getDouble("contareceber_valorIndiceReajustePorAtraso")));
		obj.setContareceber_valor(Uteis.arrendondarForcando2CadasDecimais(dadosSQL.getDouble("contareceber_valor")));
		
		obj.setContareceber_valorCusteadoContaReceber(dadosSQL.getDouble("contareceber_valorCusteadoContaReceber"));
		obj.setContareceber_valorDescontoRateio(dadosSQL.getDouble("valordescontorateio"));
		obj.setContareceber_valorBaseContaReceber(dadosSQL.getDouble("contareceber_valorBaseContaReceber"));
		obj.setContaReceber_tipoPessoa(dadosSQL.getString("contaReceber_tipoPessoa"));
		obj.getProcessoIntegracaoFinanceiroDetalhe().setCodigo(dadosSQL.getInt("processamentointegracaofinanceiradetalhe"));
		
		obj.setContareceber_contaEditadaManualmente(dadosSQL.getBoolean("contareceber_contaEditadaManualmente"));
		obj.setContareceber_diaLimite1(dadosSQL.getInt("contareceber_diaLimite1"));
		obj.setContareceber_diaLimite2(dadosSQL.getInt("contareceber_diaLimite2"));
		obj.setContareceber_diaLimite3(dadosSQL.getInt("contareceber_diaLimite3"));
		obj.setContareceber_diaLimite4(dadosSQL.getInt("contareceber_diaLimite4"));
		obj.setContareceber_utilizarDescontoProgressivoManual(dadosSQL.getBoolean("contareceber_utilizarDescontoProgressivoManual"));		
		
		if (obj.getProcessoIntegracaoFinanceiroDetalhe().getCodigo() > 0) {
			obj.setContareceber_valor(dadosSQL.getDouble("contareceber_valor"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setBolsa(dadosSQL.getDouble("processamentointegracaofinanceiradetalhe.bolsa"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setDesconto(dadosSQL.getDouble("processamentointegracaofinanceiradetalhe.desconto"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setJuro(dadosSQL.getDouble("processamentointegracaofinanceiradetalhe.juro"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setMulta(dadosSQL.getDouble("processamentointegracaofinanceiradetalhe.multa"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setValor(dadosSQL.getDouble("processamentointegracaofinanceiradetalhe.valor"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setAcrescimo(dadosSQL.getDouble("processamentointegracaofinanceiradetalhe.acrescimo"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setValorReceber(dadosSQL.getDouble("processamentointegracaofinanceiradetalhe.valorReceber"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setDataMaximaPagamento(dadosSQL.getDate("processamentointegracaofinanceiradetalhe.dataMaximaPagamento"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setCodigoPessoaFinanceiro(dadosSQL.getString("processamentointegracaofinanceiradetalhe.codigoPessoaFinanceiro"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setDataVencimentoBolsa(dadosSQL.getDate("processamentointegracaofinanceiradetalhe.dataVencimentoBolsa"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setDescontoPontualidade1(dadosSQL.getDouble("processamentointegracaofinanceiradetalhe.descontoPontualidade1"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setDescontoPontualidade2(dadosSQL.getDouble("processamentointegracaofinanceiradetalhe.descontoPontualidade2"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setDescontoPontualidade3(dadosSQL.getDouble("processamentointegracaofinanceiradetalhe.descontoPontualidade3"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setDescontoPontualidade4(dadosSQL.getDouble("processamentointegracaofinanceiradetalhe.descontoPontualidade4"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setDataVencimentoDescPontualidade1(dadosSQL.getDate("integracaofinanceira.dataVencimentoDescPontualidade1"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setDataVencimentoDescPontualidade2(dadosSQL.getDate("integracaofinanceira.dataVencimentoDescPontualidade2"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setDataVencimentoDescPontualidade3(dadosSQL.getDate("integracaofinanceira.dataVencimentoDescPontualidade3"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setDataVencimentoDescPontualidade4(dadosSQL.getDate("integracaofinanceira.dataVencimentoDescPontualidade4"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setDataVencimento(dadosSQL.getDate("processamentointegracaofinanceiradetalhe.dataVencimento"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setJurosApresentar(dadosSQL.getString("processamentointegracaofinanceiradetalhe.jurosApresentar"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setMultaApresentar(dadosSQL.getString("processamentointegracaofinanceiradetalhe.multaApresentar"));
			obj.getProcessoIntegracaoFinanceiroDetalhe().setControleCliente(dadosSQL.getString("processamentointegracaofinanceiradetalhe.controleCliente"));
			obj.setContareceber_nrdocumento(dadosSQL.getString("processamentointegracaofinanceiradetalhe.controleCliente"));
			if (dadosSQL.getString("processamentointegracaofinanceiradetalhe.tipoLayoutArquivo") != null) {
				obj.getProcessoIntegracaoFinanceiroDetalhe().setTipoLayoutArquivo(LayoutArquivoIntegracaoFinanceiraEnum.valueOf(dadosSQL.getString("processamentointegracaofinanceiradetalhe.tipoLayoutArquivo")));
			}
			if (obj.getProcessoIntegracaoFinanceiroDetalhe().getTipoLayoutArquivo().equals(LayoutArquivoIntegracaoFinanceiraEnum.LAYOUT_DESC_DETALHADO)) {
				obj.getProcessoIntegracaoFinanceiroDetalhe().setDesconto(0.0);
				obj.getProcessoIntegracaoFinanceiroDetalhe().setValorReceber(obj.getProcessoIntegracaoFinanceiroDetalhe().getValor());
			}
		}
		if (obj.getContareceber_valorCusteadoContaReceber() > 0) {
			obj.setContareceber_descricaoValorCusteadoContaReceber("* Parcela com o valor custeado de R$ " + Uteis.formatarDecimalDuasCasas(Uteis.arrendondarForcando2CadasDecimais(obj.getContareceber_valorCusteadoContaReceber())) + " por convênio. Valor original da parcela: R$ " + Uteis.formatarDecimalDuasCasas(Uteis.arrendondarForcando2CadasDecimais(obj.getContareceber_valor() + obj.getContareceber_valorCusteadoContaReceber())));
		} else {
			obj.setContareceber_descricaoValorCusteadoContaReceber("");
		}
		obj.setContareceber_valordesconto(dadosSQL.getDouble("contareceber_valordesconto"));
		obj.setContareceber_tipodesconto(dadosSQL.getString("contareceber_tipodesconto"));
		obj.setContareceber_juro(dadosSQL.getDouble("contareceber_juro"));
		obj.setContareceber_juroporcentagem(dadosSQL.getDouble("contareceber_juroporcentagem"));
		obj.setContareceber_multa(dadosSQL.getDouble("contareceber_multa"));
		obj.setContareceber_multaporcentagem(dadosSQL.getDouble("contareceber_multaporcentagem"));
		obj.setContareceber_nrdocumento(dadosSQL.getString("contareceber_nrdocumento"));
		if (obj.getProcessoIntegracaoFinanceiroDetalhe().getCodigo() > 0) {
			obj.setContareceber_nrdocumento(dadosSQL.getString("processamentointegracaofinanceiradetalhe.controleCliente"));
		}
		obj.setContareceber_nossonumero(dadosSQL.getString("contareceber_nossonumero"));
		obj.setContareceber_parcela(dadosSQL.getString("contareceber_parcela"));
		obj.setContareceber_origemnegociacaoreceber(dadosSQL.getInt("contareceber_origemnegociacaoreceber"));
		obj.setContacorrente_utilizaDadosInformadosCCparaGeracaoBoleto(dadosSQL.getBoolean("contacorrente_utilizaDadosInformadosCCparaGeracaoBoleto"));
		obj.setContacorrente_agencia(dadosSQL.getInt("contacorrente_agencia"));
		obj.setContacorrente_carteira(dadosSQL.getString("contacorrente_carteira"));
		obj.setContaCorrente_carteiraRegistrada(dadosSQL.getBoolean("contacorrente_carteiraregistrada"));
		obj.setContacorrente_convenio(dadosSQL.getString("contacorrente_convenio"));
		obj.setContacorrente_habilitarRegistroRemessaOnline(dadosSQL.getBoolean("contacorrente_habilitarRegistroRemessaOnline"));
		obj.setContacorrente_codigocedente(dadosSQL.getString("contacorrente_codigocedente"));
		obj.setContacorrente_digitocodigocedente(dadosSQL.getString("contacorrente_digitocodigocedente"));
		obj.setContareceber_contacorrente(dadosSQL.getInt("contareceber_contacorrente"));
		obj.setContareceber_descontoinstituica(dadosSQL.getDouble("contareceber_descontoinstituicao"));
		obj.setContareceber_descontoconvenio(dadosSQL.getDouble("contareceber_descontoconvenio"));
		obj.setContareceber_razaoSocialMantenedora(dadosSQL.getString("contareceber_razaoSocialMantenedora"));
		obj.setContareceber_usadescontocompostoplanodesconto(dadosSQL.getBoolean("contareceber_usadescontocompostoplanodesconto"));
		obj.setContareceber_valorDescontoAluno(dadosSQL.getDouble("valorDescontoParcela"));
		obj.setContareceber_tipoDescontoAluno(dadosSQL.getString("tipoDescontoParcela"));
		obj.setContareceber_percDescontoAluno(dadosSQL.getDouble("percDescontoParcela"));
		obj.setEspecieDoc(Uteis.isAtributoPreenchido(dadosSQL.getString("contacorrente_especieTituloBoleto")) ? dadosSQL.getString("contacorrente_especieTituloBoleto") : dadosSQL.getString("contareceber_tipoorigem"));
		obj.setDescontoValidoatedataparcela(dadosSQL.getBoolean("descontoValidoatedataparcela"));
		obj.setCondicao_aplicarCalculoComBaseDescontosCalculados(dadosSQL.getBoolean("aplicarCalculoComBaseDescontosCalculados"));
		obj.setContaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa(dadosSQL.getBigDecimal("valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa"));
		obj.setContaReceber_valorIndiceReajuste(dadosSQL.getBigDecimal("contareceber.valorIndiceReajuste"));
		obj.setMoeda("R$");
		if (dadosSQL.getString("agencia_digito").equals("")) {
			obj.setAgencia_numeroagencia(dadosSQL.getString("agencia_numeroagencia"));
		} else if (!dadosSQL.getString("agencia_digito").equals("") && (dadosSQL.getString("banco_nrbanco") != null) && dadosSQL.getString("banco_nrbanco").equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco())) {
			obj.setAgencia_numeroagencia(dadosSQL.getString("agencia_numeroagencia"));
		} else {
			obj.setAgencia_numeroagencia(dadosSQL.getString("agencia_numeroagencia") + "-" + dadosSQL.getString("agencia_digito"));
		}
		if (dadosSQL.getString("contacorrente_digito").equals("")) {
			obj.setContacorrente_numero(dadosSQL.getString("contacorrente_numero"));
		} else {
			obj.setContacorrente_numero(dadosSQL.getString("contacorrente_numero") + "-" + dadosSQL.getString("contacorrente_digito"));
		}
		obj.setPessoa_nome(dadosSQL.getString("pessoa_nome"));
		if (dadosSQL.getString("responsavelFinanceiro_nome") != null && !dadosSQL.getString("responsavelFinanceiro_nome").trim().isEmpty()) {
			obj.setResponsavelFinanceiro(dadosSQL.getString("responsavelFinanceiro_nome"));
		} else if (dadosSQL.getString("reponsavel_Financeiro") != null && !dadosSQL.getString("reponsavel_Financeiro").trim().isEmpty()) {
			obj.setResponsavelFinanceiro(dadosSQL.getString("reponsavel_Financeiro"));
		} else {
			obj.setResponsavelFinanceiro("");
		}
		if (dadosSQL.getString("responsavelFinanceiro_cpf") != null && !dadosSQL.getString("responsavelFinanceiro_cpf").trim().isEmpty()) {
			obj.setPessoa_cpf(dadosSQL.getString("responsavelFinanceiro_cpf"));
		} else if (dadosSQL.getString("responsavel_cpf") != null && !dadosSQL.getString("responsavel_cpf").trim().isEmpty()) {
			obj.setPessoa_cpf(dadosSQL.getString("responsavel_cpf"));
		} else {
			obj.setPessoa_cpf(dadosSQL.getString("pessoa_cpf"));
		}
		if (dadosSQL.getString("responsavelFinanceiro_cep") != null && !dadosSQL.getString("responsavelFinanceiro_cep").trim().isEmpty()) {
			obj.setPessoa_setor(dadosSQL.getString("responsavelFinanceiro_setor"));
			obj.setPessoa_endereco(dadosSQL.getString("responsavelFinanceiro_endereco"));
			obj.setPessoa_complemento(dadosSQL.getString("responsavelFinanceiro_complemento"));
			obj.setPessoa_numero(dadosSQL.getString("responsavelFinanceiro_numero"));
			obj.setPessoa_cep(dadosSQL.getString("responsavelFinanceiro_cep"));
			obj.setPessoa_cidade(dadosSQL.getInt("responsavelFinanceiro_cidade"));
			obj.setCidade_nome(dadosSQL.getString("cidadeRF_nome"));
			obj.setCidade_estado(dadosSQL.getInt("cidadeRF_estado"));
			obj.setEstado_nome(dadosSQL.getString("estadoRF_nome"));
		} else if (dadosSQL.getString("responsavel_cep") != null && !dadosSQL.getString("responsavel_cep").trim().isEmpty()) {
			obj.setPessoa_setor(dadosSQL.getString("responsavel_setor"));
			obj.setPessoa_endereco(dadosSQL.getString("responsavel_endereco"));
			obj.setPessoa_complemento(dadosSQL.getString("responsavel_complemento"));
			obj.setPessoa_numero(dadosSQL.getString("responsavel_numero"));
			obj.setPessoa_cep(dadosSQL.getString("responsavel_cep"));
			obj.setPessoa_cidade(dadosSQL.getInt("responsavel_cidade"));
			obj.setCidade_nome(dadosSQL.getString("cidadePais_nome"));
			obj.setCidade_estado(dadosSQL.getInt("cidadePais_estado"));
			obj.setEstado_nome(dadosSQL.getString("estadoPais_nome"));
		} else {
			obj.setPessoa_setor(dadosSQL.getString("pessoa_setor"));
			obj.setPessoa_endereco(dadosSQL.getString("pessoa_endereco"));
			obj.setPessoa_complemento(dadosSQL.getString("pessoa_complemento"));
			obj.setPessoa_numero(dadosSQL.getString("pessoa_numero"));
			obj.setPessoa_cep(dadosSQL.getString("pessoa_cep"));
			obj.setPessoa_cidade(dadosSQL.getInt("pessoa_cidade"));
			obj.setCidade_nome(dadosSQL.getString("cidade_nome"));
			obj.setCidade_estado(dadosSQL.getInt("cidade_estado"));
			obj.setEstado_nome(dadosSQL.getString("estado_nome"));
		}
		if (dadosSQL.getInt("fornecedor_codigo") > 0) {
			if (dadosSQL.getString("fornecedor_tipoEmpresa").equals("FI")) {
				obj.setPessoa_nome(dadosSQL.getString("fornecedor_nome"));
				obj.setPessoa_cpf(dadosSQL.getString("fornecedor_CPF"));
			} else {
				if (dadosSQL.getString("fornecedor_razaoSocial").trim().isEmpty()) {
					obj.setPessoa_nome(dadosSQL.getString("fornecedor_nome"));
				} else {
					obj.setPessoa_nome(dadosSQL.getString("fornecedor_razaoSocial"));
				}
				obj.setPessoa_cpf(dadosSQL.getString("fornecedor_CNPJ"));
			}
			obj.setPessoa_setor(dadosSQL.getString("fornecedor_setor"));
			obj.setPessoa_endereco(dadosSQL.getString("fornecedor_endereco"));
			obj.setPessoa_cep(dadosSQL.getString("fornecedor_CEP"));
			obj.setPessoa_cidade(dadosSQL.getInt("cidadeFornecedor_codigo"));
			obj.setCidade_nome(dadosSQL.getString("cidadeFornecedor_nome"));
			obj.setCidade_estado(dadosSQL.getInt("estadoFornecedor_codigo"));
			obj.setEstado_nome(dadosSQL.getString("estadoFornecedor_nome"));
		}

		obj.setAgencia_banco(dadosSQL.getInt("agencia_banco"));
		obj.setBanco_nome(dadosSQL.getString("banco_nome"));
		if (dadosSQL.getString("modeloGeracaoBoleto").equals("SICOB")) {
			obj.setBanco_nrbanco(dadosSQL.getString("banco_nrbanco") + "-S");
		} else if (dadosSQL.getString("modeloGeracaoBoleto").equals("SICOB15")) {
			obj.setBanco_nrbanco(dadosSQL.getString("banco_nrbanco") + "-N15");
		} else {
			obj.setBanco_nrbanco(dadosSQL.getString("banco_nrbanco"));
		}
		obj.setBanco_digito(dadosSQL.getString("banco_digito"));
		if (obj.getBanco_nrbanco().equals(Bancos.BRADESCO.getNumeroBanco())) {
			/*
			 * Formato: XYNNNNNNNNNNNNNNN-D, onde: X Modalidade/Carteira de Cobrança (1-Registrada/2-Sem Registro) Y Emissão do boleto (4-Beneficiário) NNNNNNNNNNNNNNN Nosso Número (15 posições livres do Beneficiário) D *Dígito Verificador
			 */
			obj.setDigitoVerificadorNossoNumero(Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteBradesco(obj.getContacorrente_carteira(), obj.getContareceber_nossonumero()));
			// obj.setDigitoVerificadorNossoNumero(Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteCaixaEconomica(obj.getContacorrente_carteira() + "4", obj.getContareceber_nossonumero()));
			// obj.setContacorrente_numero(obj.getContacorrente_codigocedente() + "-" + Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteCaixaEconomica("", obj.getContacorrente_codigocedente()));
		}
		if (obj.getBanco_nrbanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco())) {
			/*
			 * Formato: XYNNNNNNNNNNNNNNN-D, onde: X Modalidade/Carteira de Cobrança (1-Registrada/2-Sem Registro) Y Emissão do boleto (4-Beneficiário) NNNNNNNNNNNNNNN Nosso Número (15 posições livres do Beneficiário) D *Dígito Verificador
			 */
			obj.setDigitoVerificadorNossoNumero(Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteCaixaEconomica(obj.getContacorrente_carteira() + "4", obj.getContareceber_nossonumero()));
			obj.setContacorrente_numero(obj.getContacorrente_codigocedente() + "-" + Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteCaixaEconomica("", obj.getContacorrente_codigocedente()));
		}
		if (obj.getBanco_nrbanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL_SICOB.getNumeroBanco())) {
			/*
			 * Formato: XYNNNNNNNNNNNNNNN-D, onde: X Modalidade/Carteira de Cobrança (1-Registrada/2-Sem Registro) Y Emissão do boleto (4-Beneficiário) NNNNNNNNNNNNNNN Nosso Número (15 posições livres do Beneficiário) D *Dígito Verificador
			 */
			obj.setDigitoVerificadorNossoNumero(Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteCaixaEconomica("82", obj.getContareceber_nossonumero()));
			obj.setContacorrente_numero(obj.getContacorrente_codigocedente() + "-" + Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteCaixaEconomica("", obj.getContacorrente_codigocedente()));
		}
		if (obj.getBanco_nrbanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL_SICOB_15.getNumeroBanco())) {
			/*
			 * Formato: XYNNNNNNNNNNNNNNN-D, onde: X Modalidade/Carteira de Cobrança (1-Registrada/2-Sem Registro) Y Emissão do boleto (4-Beneficiário) NNNNNNNNNNNNNNN Nosso Número (15 posições livres do Beneficiário) D *Dígito Verificador
			 */
			obj.setDigitoVerificadorNossoNumero(Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteCaixaEconomica("8", obj.getContareceber_nossonumero()));
			obj.setContacorrente_numero(obj.getContacorrente_codigocedente() + "-" + Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteCaixaEconomica("", obj.getContacorrente_codigocedente()));
		}
		if (obj.getBanco_nrbanco().equals(Bancos.ITAU.getNumeroBanco())) {
			obj.setDigitoVerificadorNossoNumero(String.valueOf(Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteITAU(obj.getAgencia_numeroagencia(), obj.getContacorrente_numero(), obj.getContacorrente_carteira(), obj.getContareceber_nossonumero())));
			obj.setContacorrente_numero(obj.getContacorrente_codigocedente() + "-" + String.valueOf(Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteITAU(obj.getAgencia_numeroagencia(), obj.getContacorrente_numero(), obj.getContacorrente_carteira(), obj.getContacorrente_codigocedente())));
			obj.setContacorrente_codigocedente(dadosSQL.getString("contacorrente_numero") + "-" + dadosSQL.getString("contacorrente_digito"));
		}
		if (obj.getBanco_nrbanco().equals(Bancos.BANESTE.getNumeroBanco())) {
			obj.setDigitoVerificadorNossoNumero(obj.getContareceber_nossonumero().substring(8, 10));
			//obj.setContareceber_nossonumero(obj.getContareceber_nossonumero().substring(0, 8));
			obj.setContacorrente_numero(obj.getContacorrente_codigocedente() + "-" + String.valueOf(Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteITAU(obj.getAgencia_numeroagencia(), obj.getContacorrente_numero(), obj.getContacorrente_carteira(), obj.getContacorrente_codigocedente())));
			obj.setContacorrente_codigocedente(dadosSQL.getString("contacorrente_numero") + "-" + dadosSQL.getString("contacorrente_digito"));
		}

		obj.setContareceber_linhadigitavelcodigobarras(dadosSQL.getString("contareceber_linhadigitavelcodigobarras"));
		obj.setContareceber_descontoprogressivo(dadosSQL.getInt("contareceber_descontoprogressivo"));
		obj.setDescontoprogressivo_utilizarDiaFixo(dadosSQL.getBoolean("descontoprogressivo_utilizarDiaFixo"));
		obj.setDescontoprogressivo_utilizarDiaUtil(dadosSQL.getBoolean("descontoprogressivo_utilizarDiaUtil"));
		// MERGE EDIGAR
		obj.setDescontoprogressivo_dialimite1(dadosSQL.getInt("descontoprogressivo_dialimite1"));
		obj.setDescontoprogressivo_dialimite2(dadosSQL.getInt("descontoprogressivo_dialimite2"));
		obj.setDescontoprogressivo_dialimite3(dadosSQL.getInt("descontoprogressivo_dialimite3"));
		obj.setDescontoprogressivo_dialimite4(dadosSQL.getInt("descontoprogressivo_dialimite4"));

		if ((obj.getContareceber_contaEditadaManualmente()) &&
		    (obj.getContareceber_utilizarDescontoProgressivoManual())) {
			obj.setDescontoprogressivo_dialimite1(obj.getContareceber_diaLimite1());
			obj.setDescontoprogressivo_dialimite2(obj.getContareceber_diaLimite2());
			obj.setDescontoprogressivo_dialimite3(obj.getContareceber_diaLimite3());
			obj.setDescontoprogressivo_dialimite4(obj.getContareceber_diaLimite4());
		}
			
		obj.setDescontoprogressivo_percdescontolimite1(dadosSQL.getDouble("descontoprogressivo_percdescontolimite1"));
		obj.setDescontoprogressivo_percdescontolimite2(dadosSQL.getDouble("descontoprogressivo_percdescontolimite2"));
		obj.setDescontoprogressivo_percdescontolimite3(dadosSQL.getDouble("descontoprogressivo_percdescontolimite3"));
		obj.setDescontoprogressivo_percdescontolimite4(dadosSQL.getDouble("descontoprogressivo_percdescontolimite4"));
		obj.setDescontoprogressivo_valordescontolimite1(dadosSQL.getDouble("descontoprogressivo_valordescontolimite1"));
		obj.setDescontoprogressivo_valordescontolimite2(dadosSQL.getDouble("descontoprogressivo_valordescontolimite2"));
		obj.setDescontoprogressivo_valordescontolimite3(dadosSQL.getDouble("descontoprogressivo_valordescontolimite3"));
		obj.setDescontoprogressivo_valordescontolimite4(dadosSQL.getDouble("descontoprogressivo_valordescontolimite4"));
		obj.setContareceber_mantenedora(dadosSQL.getString("contareceber_mantenedora"));
		obj.setContareceber_cnpjMantenedora(dadosSQL.getString("contareceber_cnpj"));
		obj.setContareceber_foneMantenedora(dadosSQL.getString("contareceber_fonemantenedora"));
		obj.setTipoBoletoContaReceber(dadosSQL.getString("tiboboletocontareceber"));
		obj.setDiasVariacaoDataVencimento(dadosSQL.getInt("diasVariacaoDataVencimento"));
		
		// TurmaVO turma = new TurmaVO();
		// turma.setCodigo(new Integer(dadosSQL.getInt("codTurmaBase")));
		// if (turma.getCodigo() != null && turma.getCodigo().intValue() != 0) {
		// getFacadeFactory().getTurmaFacade().carregarDados(turma,
		// NivelMontarDados.BASICO, usuarioVO);
		// }
		obj.setNomeCurso(dadosSQL.getString("nomeCurso"));
		obj.setTurmaBase(dadosSQL.getString("identificadorturma"));
		obj.setUtilizarDadosMatrizBoleto(dadosSQL.getBoolean("utilizarDadosMatrizBoleto"));
		ContaReceberVO contaObterOrdemDesconto = new ContaReceberVO();
		contaObterOrdemDesconto.setOrdemConvenio(dadosSQL.getInt("contareceber_ordemConvenio"));
		contaObterOrdemDesconto.setOrdemConvenioValorCheio(dadosSQL.getBoolean("contareceber_ordemConvenioValorCheio"));
		contaObterOrdemDesconto.setOrdemDescontoAluno(dadosSQL.getInt("contareceber_ordemDescontoAluno"));
		contaObterOrdemDesconto.setOrdemDescontoAlunoValorCheio(dadosSQL.getBoolean("contareceber_ordemDescontoAlunoValorCheio"));
		contaObterOrdemDesconto.setOrdemDescontoProgressivo(dadosSQL.getInt("contareceber_ordemDescontoProgressivo"));
		contaObterOrdemDesconto.setOrdemDescontoProgressivoValorCheio(dadosSQL.getBoolean("contareceber_ordemDescontoProgressivoValorCheio"));
		contaObterOrdemDesconto.setOrdemPlanoDesconto(dadosSQL.getInt("contareceber_ordemPlanoDesconto"));
		contaObterOrdemDesconto.setOrdemPlanoDescontoValorCheio(dadosSQL.getBoolean("contareceber_ordemPlanoDescontoValorCheio"));
		obj.setOrdemDescontos(contaObterOrdemDesconto.obterOrdemAplicacaoDescontosPadraoAtual());
		
		if(obj.getContaReceber_tipoPessoa().equals("PA")){
			if(!Uteis.isAtributoPreenchido(obj.getPessoa_nome())
				|| !dadosSQL.getBoolean("emitirBoletoEmNomeBeneficiadoparceiro")){
				obj.setParceiro_razaosocial(dadosSQL.getString("parceiro_razaosocial"));		
				obj.setEnderecoParceiro(dadosSQL.getString("enderecoparceiro"));
				obj.setSetorParceiro(dadosSQL.getString("setorparceiro"));
				obj.setCepParceiro(dadosSQL.getString("cepparceiro"));
				obj.setCnpjParceiro(dadosSQL.getString("cnpjparceiro"));
				obj.setTelParceiro(dadosSQL.getString("telparceiro"));
				obj.setCidadeParceiro(dadosSQL.getString("cidadeParceiro"));
				obj.setEstadoParceiro(dadosSQL.getString("estadoParceiro"));
			}else if(dadosSQL.getBoolean("emitirBoletoEmNomeBeneficiadoparceiro")){
				obj.setContaReceber_tipoPessoa("AL");
			}
		}
		obj.setEnderecoUnidadeEnsino(dadosSQL.getString("enderecoUnidadeEnsino"));
		obj.setTelefoneUnidadeEnsino(dadosSQL.getString("telefoneUnidadeEnsino"));
		obj.setSetorUnidadeEnsino(dadosSQL.getString("setorUnidadeEnsino"));
		obj.setCepUnidadeEnsino(dadosSQL.getString("cepUnidadeEnsino"));
		obj.setCodigoCidadeUnidadeEnsino(dadosSQL.getInt("unidadeensino.cidade"));
		obj.setComplementoUnidadeEnsino(dadosSQL.getString("complUnidadeEnsino"));
		obj.setCidadeUnidadeEnsino(dadosSQL.getString("cidadeUnidadeEnsino"));
		obj.setEstadoUnidadeEnsino(dadosSQL.getString("estadoUnidadeEnsino"));
		obj.setNumeroUnidadeEnsino(dadosSQL.getString("numeroUnidadeEnsino"));
		obj.setTurno(dadosSQL.getString("turnoMatricula"));
		if ((dadosSQL.getString("situacaoMatricula") != null) && (!dadosSQL.getString("situacaoMatricula").equals(""))) {
			obj.setSituacaoMatricula(SituacaoVinculoMatricula.getDescricao((dadosSQL.getString("situacaoMatricula")).toString()));
		} else {
			obj.setSituacaoMatricula("");
		}
		obj.setAno(dadosSQL.getString("anoMatriculaPeriodo"));
		obj.setSemestre(dadosSQL.getString("semestreMatriculaPeriodo"));

		obj.setModeloBoletoMatricula_codigo(dadosSQL.getInt("modeloBoletoMatricula_codigo"));
		obj.setModeloBoletoMensalidade_codigo(dadosSQL.getInt("modeloBoletoMensalidade_codigo"));
		obj.setModeloBoletoMaterialDidatico_codigo(dadosSQL.getInt("modeloBoletoMaterialDidatico_codigo"));
		obj.setModeloBoletoOutros_codigo(dadosSQL.getInt("modeloBoletoOutros_codigo"));
		obj.setModeloBoletoProcessoSeletivo_codigo(dadosSQL.getInt("modeloBoletoProcessoSeletivo_codigo"));
		obj.setModeloBoletoRequerimento_codigo(dadosSQL.getInt("modeloBoletoRequerimento_codigo"));
		obj.setModeloBoletoRenegociacao_codigo(dadosSQL.getInt("modeloboletorenegociacao_codigo"));
		obj.setContaCorrenteCnab(dadosSQL.getString("contacorrente_cnab"));
		obj.setParceiroIsentarMulta(dadosSQL.getBoolean("parceiro_isentarmulta"));
		obj.setParceiroIsentarJuro(dadosSQL.getBoolean("parceiro_isentarjuro"));

		if (!Uteis.isAtributoPreenchido(obj.getProcessoIntegracaoFinanceiroDetalhe())) {
			obj.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContareceber_contacorrente(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			if (dadosSQL.getDouble("contaCorrente_taxaBoleto") > 0.0 && (!dadosSQL.getBoolean("responsavelFinanceiro_isentarTaxaBoleto") && !dadosSQL.getBoolean("reponsavel_isentarTaxaBoleto") && !dadosSQL.getBoolean("pessoa_isentarTaxaBoleto") && !dadosSQL.getBoolean("parceiro_isentarTaxaBoleto") && !dadosSQL.getBoolean("fornecedor_isentarTaxaBoleto"))) {
				getFacadeFactory().getContaReceberFacade().incluirValorTaxaBoleto(obj.getContareceber_codigo(), dadosSQL.getDouble("contaCorrente_taxaBoleto"), usuarioVO);
				obj.setContareceber_valor(obj.getContareceber_valor() + dadosSQL.getDouble("contaCorrente_taxaBoleto"));
				regerarCodigoBarraTaxaBoleto(obj,  usuarioVO);
			} else {
				getFacadeFactory().getContaReceberFacade().incluirValorTaxaBoleto(obj.getContareceber_codigo(), 0.0, usuarioVO);
			}
			if (obj.getContareceber_codigobarra().length() < 44) {
				regerarCodigoBarraTaxaBoleto(obj, usuarioVO);
				getFacadeFactory().getContaReceberFacade().registrarLinhaDigitavelCodigoBarrasBoletoNossoNumeroContaReceber(obj.getContareceber_codigo(), obj.getContareceber_nossonumero(), obj.getContareceber_nrdocumento(), obj.getContareceber_codigobarra(), obj.getContareceber_linhadigitavelcodigobarras(), usuarioVO);
			}
		}
		obj.preencherParcelaPorConfiguracaoFinanceira_Apresentar(getAplicacaoControle().getConfiguracaoFinanceiroVO(dadosSQL.getInt("contareceber_unidadeensinoFinanceira")));
		return obj;
	}

	public void realizarPreenchimentoBoletoBancarioContaAgrupadaRelVO(BoletoBancarioRelVO obj, BoletoBancarioRelVO boletoBancarioRelVO, ContaReceberAgrupadaVO contaReceberAgrupadaVO) throws Exception {
		obj.setContareceber_valordesconto(obj.getContareceber_valordesconto() + boletoBancarioRelVO.getContareceber_valordesconto());
		obj.setContareceber_tipodesconto(boletoBancarioRelVO.getContareceber_tipodesconto());
		obj.setContareceber_juro(obj.getContareceber_juro() + boletoBancarioRelVO.getContareceber_juro());
		obj.setContareceber_juroporcentagem(boletoBancarioRelVO.getContareceber_juroporcentagem());
		obj.setContareceber_multa(obj.getContareceber_multa() + boletoBancarioRelVO.getContareceber_multa());
		obj.setContareceber_multaporcentagem(boletoBancarioRelVO.getContareceber_multaporcentagem());
		obj.setContareceber_nrdocumento(boletoBancarioRelVO.getContareceber_nrdocumento());
		obj.setContareceber_mantenedora(boletoBancarioRelVO.getContareceber_mantenedora());
		obj.setContareceber_cnpjMantenedora(boletoBancarioRelVO.getContareceber_cnpjMantenedora());
		obj.setContareceber_foneMantenedora(boletoBancarioRelVO.getContareceber_foneMantenedora());
		obj.setTipoBoletoContaReceber(boletoBancarioRelVO.getTipoBoletoContaReceber());
		obj.setNomeCurso(boletoBancarioRelVO.getNomeCurso());
		obj.setTurmaBase(boletoBancarioRelVO.getTurmaBase());
		obj.setEnderecoParceiro(boletoBancarioRelVO.getEnderecoParceiro());
		obj.setSetorParceiro(boletoBancarioRelVO.getSetorParceiro());
		obj.setCepParceiro(boletoBancarioRelVO.getCepParceiro());
		obj.setCnpjParceiro(boletoBancarioRelVO.getCnpjParceiro());
		obj.setTelParceiro(boletoBancarioRelVO.getTelParceiro());
		obj.setCidadeParceiro(boletoBancarioRelVO.getCidadeParceiro());
		obj.setEstadoParceiro(boletoBancarioRelVO.getEstadoParceiro());
		obj.setTelefoneUnidadeEnsino(boletoBancarioRelVO.getTelefoneUnidadeEnsino());
		obj.setEnderecoUnidadeEnsino(boletoBancarioRelVO.getEnderecoUnidadeEnsino());
		obj.setSetorUnidadeEnsino(boletoBancarioRelVO.getSetorUnidadeEnsino());
		obj.setCepUnidadeEnsino(boletoBancarioRelVO.getCepUnidadeEnsino());
		obj.setComplementoUnidadeEnsino(boletoBancarioRelVO.getComplementoUnidadeEnsino());
		obj.setCidadeUnidadeEnsino(boletoBancarioRelVO.getCidadeUnidadeEnsino());
		obj.setEstadoUnidadeEnsino(boletoBancarioRelVO.getEstadoUnidadeEnsino());
		obj.setNumeroUnidadeEnsino(boletoBancarioRelVO.getNumeroUnidadeEnsino());
		obj.setTurno(boletoBancarioRelVO.getTurno());
		if ((boletoBancarioRelVO.getSituacaoMatricula() != null) && (!boletoBancarioRelVO.getSituacaoMatricula().equals(""))) {
			obj.setSituacaoMatricula(SituacaoVinculoMatricula.getDescricao((boletoBancarioRelVO.getSituacaoMatricula()).toString()));
		} else {
			obj.setSituacaoMatricula("");
		}
		obj.setAno(boletoBancarioRelVO.getAno());
		obj.setSemestre(boletoBancarioRelVO.getSemestre());

		obj.setModeloBoletoMatricula_codigo(boletoBancarioRelVO.getModeloBoletoMatricula_codigo());
		obj.setModeloBoletoMensalidade_codigo(boletoBancarioRelVO.getModeloBoletoMensalidade_codigo());
		obj.setModeloBoletoMaterialDidatico_codigo(boletoBancarioRelVO.getModeloBoletoMaterialDidatico_codigo());
		obj.setModeloBoletoOutros_codigo(boletoBancarioRelVO.getModeloBoletoOutros_codigo());
		obj.setModeloBoletoProcessoSeletivo_codigo(boletoBancarioRelVO.getModeloBoletoProcessoSeletivo_codigo());
		obj.setModeloBoletoRequerimento_codigo(boletoBancarioRelVO.getModeloBoletoRequerimento_codigo());
		obj.setModeloBoletoRenegociacao_codigo(boletoBancarioRelVO.getModeloBoletoRenegociacao_codigo());
		obj.setModeloBoletoBiblioteca_codigo(boletoBancarioRelVO.getModeloBoletoBiblioteca_codigo());
		obj.setContareceber_tipoorigem(boletoBancarioRelVO.getContareceber_tipoorigem());
		obj.setContareceber_situacao(boletoBancarioRelVO.getContareceber_situacao());

	}

	public void realizarPreenchimentoBoletoBancarioContaAgrupadaRelVO(BoletoBancarioRelVO obj, ContaReceberAgrupadaVO contaReceberAgrupadaVO, UsuarioVO usuario) throws Exception {
		obj.setContareceber_codigo(contaReceberAgrupadaVO.getCodigo());
		obj.setContareceber_codigobarra(contaReceberAgrupadaVO.getCodigoBarra());
		obj.setContareceber_matriculaaluno(contaReceberAgrupadaVO.getMatricula().getMatricula());
		obj.setContareceber_data(contaReceberAgrupadaVO.getDataAgrupamento());

		obj.setContareceber_descricaopagamento(obj.getContareceber_descricaopagamento());
		obj.setContareceber_datavencimento(contaReceberAgrupadaVO.getDataVencimento());
		obj.setContareceber_valor(contaReceberAgrupadaVO.getValorTotal());

		obj.setContareceber_nossonumero(contaReceberAgrupadaVO.getNossoNumero());
		obj.setContareceber_parcela("1/1");
		obj.setContacorrente_utilizaDadosInformadosCCparaGeracaoBoleto(contaReceberAgrupadaVO.getContaCorrente().getUtilizaDadosInformadosCCparaGeracaoBoleto());
		obj.setContacorrente_agencia(contaReceberAgrupadaVO.getContaCorrente().getAgencia().getCodigo());
		obj.setContacorrente_carteira(contaReceberAgrupadaVO.getContaCorrente().getCarteira());
		obj.setContaCorrente_carteiraRegistrada(contaReceberAgrupadaVO.getContaCorrente().getCarteiraRegistrada());
		obj.setContacorrente_convenio(contaReceberAgrupadaVO.getContaCorrente().getConvenio());
		obj.setContacorrente_habilitarRegistroRemessaOnline(contaReceberAgrupadaVO.getContaCorrente().getHabilitarRegistroRemessaOnline());
		obj.setContacorrente_codigocedente(contaReceberAgrupadaVO.getContaCorrente().getCodigoCedente());
		obj.setContacorrente_digitocodigocedente(contaReceberAgrupadaVO.getContaCorrente().getDigitoCodigoCedente());
		obj.setContareceber_contacorrente(contaReceberAgrupadaVO.getContaCorrente().getCodigo());
		// obj.setContareceber_descontoinstituica(dadosSQL.getDouble("contareceber_descontoinstituicao"));
		// obj.setContareceber_descontoconvenio(dadosSQL.getDouble("contareceber_descontoconvenio"));
		obj.setContareceber_razaoSocialMantenedora(contaReceberAgrupadaVO.getUnidadeEnsino().getRazaoSocial());
		// obj.setContareceber_usadescontocompostoplanodesconto(dadosSQL.getBoolean("contareceber_usadescontocompostoplanodesconto"));
		// obj.setContareceber_valorDescontoAluno(dadosSQL.getDouble("valorDescontoParcela"));
		// obj.setContareceber_tipoDescontoAluno(dadosSQL.getString("tipoDescontoParcela"));
		// obj.setContareceber_percDescontoAluno(dadosSQL.getDouble("percDescontoParcela"));
		// obj.setEspecieDoc(dadosSQL.getString("contareceber_tipoorigem"));
		// obj.setDescontoValidoatedataparcela(dadosSQL.getBoolean("descontoValidoatedataparcela"));
		// obj.setCondicao_aplicarCalculoComBaseDescontosCalculados(dadosSQL.getBoolean("aplicarCalculoComBaseDescontosCalculados"));
		obj.setMoeda("R$");
		if (contaReceberAgrupadaVO.getContaCorrente().getAgencia().getDigito().equals("")) {
			obj.setAgencia_numeroagencia(contaReceberAgrupadaVO.getContaCorrente().getAgencia().getNumeroAgencia());
		} else if (!contaReceberAgrupadaVO.getContaCorrente().getAgencia().getDigito().equals("") && (contaReceberAgrupadaVO.getContaCorrente().getAgencia().getBanco().getNrBanco() != null) && contaReceberAgrupadaVO.getContaCorrente().getAgencia().getBanco().getNrBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco())) {
			obj.setAgencia_numeroagencia(contaReceberAgrupadaVO.getContaCorrente().getAgencia().getNumeroAgencia());
		} else {
			obj.setAgencia_numeroagencia(contaReceberAgrupadaVO.getContaCorrente().getAgencia().getNumeroAgencia() + "-" + contaReceberAgrupadaVO.getContaCorrente().getAgencia().getDigito());
		}
		if (contaReceberAgrupadaVO.getContaCorrente().getDigito().equals("")) {
			obj.setContacorrente_numero(contaReceberAgrupadaVO.getContaCorrente().getNumero());
		} else {
			obj.setContacorrente_numero(contaReceberAgrupadaVO.getContaCorrente().getNumero() + "-" + contaReceberAgrupadaVO.getContaCorrente().getDigito());
		}
		obj.setPessoa_nome(contaReceberAgrupadaVO.getPessoa().getNome());
		obj.setResponsavelFinanceiro("");
		obj.setPessoa_cpf(contaReceberAgrupadaVO.getPessoa().getCPF());
		obj.setPessoa_setor(contaReceberAgrupadaVO.getPessoa().getSetor());
		obj.setPessoa_endereco(contaReceberAgrupadaVO.getPessoa().getEndereco());
		obj.setPessoa_cep(contaReceberAgrupadaVO.getPessoa().getCEP());
		obj.setPessoa_cidade(contaReceberAgrupadaVO.getPessoa().getCidade().getCodigo());
		obj.setCidade_nome(contaReceberAgrupadaVO.getPessoa().getCidade().getNome());
		obj.setCidade_estado(contaReceberAgrupadaVO.getPessoa().getCidade().getEstado().getCodigo());
		obj.setEstado_nome(contaReceberAgrupadaVO.getPessoa().getCidade().getEstado().getNome());

		obj.setAgencia_banco(contaReceberAgrupadaVO.getContaCorrente().getAgencia().getBanco().getCodigo());
		obj.setBanco_nome(contaReceberAgrupadaVO.getContaCorrente().getAgencia().getBanco().getNome());
		obj.setBanco_nrbanco(contaReceberAgrupadaVO.getContaCorrente().getAgencia().getBanco().getNrBanco());
		obj.setBanco_digito(contaReceberAgrupadaVO.getContaCorrente().getAgencia().getBanco().getDigito());
		if (obj.getBanco_nrbanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco())) {
			obj.setDigitoVerificadorNossoNumero(Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteCaixaEconomica(obj.getContacorrente_carteira(), obj.getContareceber_nossonumero()));
			obj.setContacorrente_numero(obj.getContacorrente_codigocedente() + "-" + Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteCaixaEconomica("", obj.getContacorrente_codigocedente()));
		}
		if (obj.getBanco_nrbanco().equals(Bancos.ITAU.getNumeroBanco())) {
			obj.setDigitoVerificadorNossoNumero(String.valueOf(Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteITAU(obj.getAgencia_numeroagencia(), obj.getContacorrente_numero(), obj.getContacorrente_carteira(), obj.getContareceber_nossonumero())));
			obj.setContacorrente_numero(obj.getContacorrente_codigocedente() + "-" + String.valueOf(Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteITAU(obj.getAgencia_numeroagencia(), obj.getContacorrente_numero(), obj.getContacorrente_carteira(), obj.getContacorrente_codigocedente())));
			obj.setContacorrente_codigocedente(contaReceberAgrupadaVO.getContaCorrente().getCodigoCedente() + "-" + contaReceberAgrupadaVO.getContaCorrente().getDigito());
		}

		obj.setContareceber_linhadigitavelcodigobarras(contaReceberAgrupadaVO.getLinhaDigitavelCodigoBarras());
		// obj.setContareceber_descontoprogressivo(dadosSQL.getInt("contareceber_descontoprogressivo"));
		// obj.setDescontoprogressivo_utilizarDiaFixo(dadosSQL.getBoolean("descontoprogressivo_utilizarDiaFixo"));
		// obj.setDescontoprogressivo_utilizarDiaUtil(dadosSQL.getBoolean("descontoprogressivo_utilizarDiaUtil"));
		// obj.setDescontoprogressivo_dialimite1(dadosSQL.getInt("descontoprogressivo_dialimite1"));
		// obj.setDescontoprogressivo_dialimite2(dadosSQL.getInt("descontoprogressivo_dialimite2"));
		// obj.setDescontoprogressivo_dialimite3(dadosSQL.getInt("descontoprogressivo_dialimite3"));
		// obj.setDescontoprogressivo_dialimite4(dadosSQL.getInt("descontoprogressivo_dialimite4"));
		// obj.setDescontoprogressivo_percdescontolimite1(dadosSQL.getDouble("descontoprogressivo_percdescontolimite1"));
		// obj.setDescontoprogressivo_percdescontolimite2(dadosSQL.getDouble("descontoprogressivo_percdescontolimite2"));
		// obj.setDescontoprogressivo_percdescontolimite3(dadosSQL.getDouble("descontoprogressivo_percdescontolimite3"));
		// obj.setDescontoprogressivo_percdescontolimite4(dadosSQL.getDouble("descontoprogressivo_percdescontolimite4"));
		// obj.setDescontoprogressivo_valordescontolimite1(dadosSQL.getDouble("descontoprogressivo_valordescontolimite1"));
		// obj.setDescontoprogressivo_valordescontolimite2(dadosSQL.getDouble("descontoprogressivo_valordescontolimite2"));
		// obj.setDescontoprogressivo_valordescontolimite3(dadosSQL.getDouble("descontoprogressivo_valordescontolimite3"));
		// obj.setDescontoprogressivo_valordescontolimite4(dadosSQL.getDouble("descontoprogressivo_valordescontolimite4"));

		// obj.setUtilizarDadosMatrizBoleto(dadosSQL.getBoolean("utilizarDadosMatrizBoleto"));
		// ContaReceberVO contaObterOrdemDesconto = new ContaReceberVO();
		// contaObterOrdemDesconto.setOrdemConvenio(dadosSQL.getInt("contareceber_ordemConvenio"));
		// contaObterOrdemDesconto.setOrdemConvenioValorCheio(dadosSQL.getBoolean("contareceber_ordemConvenioValorCheio"));
		// contaObterOrdemDesconto.setOrdemDescontoAluno(dadosSQL.getInt("contareceber_ordemDescontoAluno"));
		// contaObterOrdemDesconto.setOrdemDescontoAlunoValorCheio(dadosSQL.getBoolean("contareceber_ordemDescontoAlunoValorCheio"));
		// contaObterOrdemDesconto.setOrdemDescontoProgressivo(dadosSQL.getInt("contareceber_ordemDescontoProgressivo"));
		// contaObterOrdemDesconto.setOrdemDescontoProgressivoValorCheio(dadosSQL.getBoolean("contareceber_ordemDescontoProgressivoValorCheio"));
		// contaObterOrdemDesconto.setOrdemPlanoDesconto(dadosSQL.getInt("contareceber_ordemPlanoDesconto"));
		// contaObterOrdemDesconto.setOrdemPlanoDescontoValorCheio(dadosSQL.getBoolean("contareceber_ordemPlanoDescontoValorCheio"));
		// obj.setOrdemDescontos(contaObterOrdemDesconto.obterOrdemAplicacaoDescontosPadraoAtual());

		if (contaReceberAgrupadaVO.getContaCorrente().getTaxaBoleto() > 0.0) {
			// && (!dadosSQL.getBoolean("responsavelFinanceiro_isentarTaxaBoleto")
			// && !dadosSQL.getBoolean("reponsavel_isentarTaxaBoleto")
			// && !dadosSQL.getBoolean("pessoa_isentarTaxaBoleto")
			// && !dadosSQL.getBoolean("parceiro_isentarTaxaBoleto")
			// && !dadosSQL.getBoolean("fornecedor_isentarTaxaBoleto")))
			//
			getFacadeFactory().getContaReceberFacade().incluirValorTaxaBoleto(obj.getContareceber_codigo(), contaReceberAgrupadaVO.getContaCorrente().getTaxaBoleto(), usuario);
			obj.setContareceber_valor(obj.getContareceber_valor() + contaReceberAgrupadaVO.getContaCorrente().getTaxaBoleto());
			regerarCodigoBarraTaxaBoleto(obj, null);
		} else {
			getFacadeFactory().getContaReceberFacade().incluirValorTaxaBoleto(obj.getContareceber_codigo(), 0.0, usuario);
		}

	}

	public static void regerarCodigoBarraTaxaBoleto(BoletoBancarioRelVO boletoBancarioRelVO, UsuarioVO usuarioVO) throws Exception {
		ContaReceberVO obj = new ContaReceberVO();
		obj.setData(boletoBancarioRelVO.getContareceber_data());
		if(boletoBancarioRelVO.getDataVencimentoRemessaOnline() != null) {
			obj.setDataVencimento(boletoBancarioRelVO.getDataVencimentoRemessaOnline());
		}else {
			obj.setDataVencimento(boletoBancarioRelVO.getContareceber_datavencimento());
		}
		if(!Uteis.isAtributoPreenchido(boletoBancarioRelVO.getContaCorrenteVO())) {
			obj.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(boletoBancarioRelVO.getContareceber_contacorrente(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			boletoBancarioRelVO.setContaCorrenteVO(obj.getContaCorrenteVO());
		}else {
			obj.setContaCorrenteVO(boletoBancarioRelVO.getContaCorrenteVO());
		}
		obj.setValor(boletoBancarioRelVO.getContareceber_valor());
		obj.setNossoNumero(boletoBancarioRelVO.getContareceber_nossonumero());
		obj.criarBoleto(obj.getContaCorrenteVO());
		boletoBancarioRelVO.setContareceber_linhadigitavelcodigobarras(obj.getLinhaDigitavelCodigoBarras());
		boletoBancarioRelVO.setContareceber_codigobarra(obj.getCodigoBarra());
		boletoBancarioRelVO.setContareceber_nossonumero(obj.getNossoNumero());
		

	}

	public List<BoletoBancarioRelVO> adicionarCamposReferenteModeloBoletos(List<BoletoBancarioRelVO> boletoBancarioRelVOs, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO config) throws Exception {
		if (!Uteis.isAtributoPreenchido(config)) {
			config = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO, null);
		}
		ModeloBoletoVO modeloBoletoVO = null;
		for (BoletoBancarioRelVO boletoBancarioRelVO : boletoBancarioRelVOs) {
			modeloBoletoVO = definirModeloBoleto(boletoBancarioRelVO, usuarioVO, config);
			if (modeloBoletoVO != null && !modeloBoletoVO.getCodigo().equals(0)) {
				// boletoBancarioRelVO.setModeloboleto_imagem(modeloBoletoVO.getImagem());
				boletoBancarioRelVO.setModeloboleto_localpagamento(modeloBoletoVO.getLocalPagamento());
				boletoBancarioRelVO.setModeloboleto_apenasDescontoInstrucaoBoleto(modeloBoletoVO.getApenasDescontoInstrucaoBoleto());
				boletoBancarioRelVO.setOcultarCodBarraLinhaDigitavel(modeloBoletoVO.getOcultarCodBarraLinhaDigitavel());
				if (!modeloBoletoVO.getUtilizarDescricaoDescontoPersonalizado()) {
					if (boletoBancarioRelVO.getContareceber_tipoorigem().equals("REQ") || boletoBancarioRelVO.getContareceber_tipoorigem().equals("IPS") || boletoBancarioRelVO.getContareceber_tipoorigem().equals("OUT") || boletoBancarioRelVO.getContareceber_tipoorigem().equals("BIB") || boletoBancarioRelVO.getContareceber_tipoorigem().equals("DCH") || boletoBancarioRelVO.getContareceber_tipoorigem().equals("CTR")) {
						boletoBancarioRelVO.setModeloboleto_observacoesgerais1(boletoBancarioRelVO.getContareceber_descricaopagamento() + "<p>" + Uteis.substituirTagCasoExista(modeloBoletoVO.getObservacoesGerais1(), boletoBancarioRelVO.getContareceber_valor(), boletoBancarioRelVO.getContareceber_multa(), boletoBancarioRelVO.getContareceber_juro())+ "</p>");
						boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getContareceber_descricaopagamento() + "<p>" + Uteis.substituirTagCasoExista(modeloBoletoVO.getObservacoesGerais2(), boletoBancarioRelVO.getContareceber_valor(), boletoBancarioRelVO.getContareceber_multa(), boletoBancarioRelVO.getContareceber_juro())+ "</p>");
						boletoBancarioRelVO.setContareceber_descricaopagamento(boletoBancarioRelVO.getContareceber_descricaopagamento() + "<p>" + Uteis.substituirTagCasoExista(modeloBoletoVO.getObservacoesGerais1(), boletoBancarioRelVO.getContareceber_valor(), boletoBancarioRelVO.getContareceber_multa(), boletoBancarioRelVO.getContareceber_juro())+ "</p>");
					} else {
						boletoBancarioRelVO.setModeloboleto_observacoesgerais1(Uteis.substituirTagCasoExista(modeloBoletoVO.getObservacoesGerais1(), boletoBancarioRelVO.getContareceber_valor(),boletoBancarioRelVO.getContareceber_multa(), boletoBancarioRelVO.getContareceber_juro()));
						boletoBancarioRelVO.setModeloboleto_observacoesgerais2(Uteis.substituirTagCasoExista(modeloBoletoVO.getObservacoesGerais2(), boletoBancarioRelVO.getContareceber_valor(), boletoBancarioRelVO.getContareceber_multa(), boletoBancarioRelVO.getContareceber_juro()));
					}
				} else {
					boletoBancarioRelVO.setModeloboleto_utilizarDescricaoDescontoPersonalizado(modeloBoletoVO.getUtilizarDescricaoDescontoPersonalizado());
					boletoBancarioRelVO.setModeloboleto_textoTopo(modeloBoletoVO.getTextoTopo().replace(TagModeloBoletoEnum.DATA_MAXIMO_RECEBIMENTO.getValor(), boletoBancarioRelVO.getDataMaximoRecebimento()).replace(TagModeloBoletoEnum.CODIGO_ADMINISTRATIVO.getValor(), boletoBancarioRelVO.getCodigoAdministrativo()).replace(TagModeloBoletoEnum.NR_PARCELA.getValor(), boletoBancarioRelVO.getContareceber_parcela()));
					boletoBancarioRelVO.setModeloboleto_instrucao1(modeloBoletoVO.getInstrucao1());
					boletoBancarioRelVO.setModeloboleto_instrucao2(modeloBoletoVO.getInstrucao2());
					boletoBancarioRelVO.setModeloboleto_instrucao3(modeloBoletoVO.getInstrucao3());
					boletoBancarioRelVO.setModeloboleto_instrucao4(modeloBoletoVO.getInstrucao4());
					boletoBancarioRelVO.setModeloboleto_instrucao5(modeloBoletoVO.getInstrucao5());
					boletoBancarioRelVO.setModeloboleto_instrucao6(modeloBoletoVO.getInstrucao6());
					boletoBancarioRelVO.setModeloboleto_textoRodape(modeloBoletoVO.getTextoRodape().replace(TagModeloBoletoEnum.CODIGO_ADMINISTRATIVO.getValor(), boletoBancarioRelVO.getCodigoAdministrativo()).replace(TagModeloBoletoEnum.DATA_MAXIMO_RECEBIMENTO.getValor(), boletoBancarioRelVO.getDataMaximoRecebimento()).replace(TagModeloBoletoEnum.NR_PARCELA.getValor(), boletoBancarioRelVO.getContareceber_parcela()));
					boletoBancarioRelVO.setModeloboleto_textoTopoInferior(modeloBoletoVO.getTextoTopoInferior().replace(TagModeloBoletoEnum.DATA_MAXIMO_RECEBIMENTO.getValor(), boletoBancarioRelVO.getDataMaximoRecebimento()).replace(TagModeloBoletoEnum.CODIGO_ADMINISTRATIVO.getValor(), boletoBancarioRelVO.getCodigoAdministrativo()).replace(TagModeloBoletoEnum.NR_PARCELA.getValor(), boletoBancarioRelVO.getContareceber_parcela()));
					boletoBancarioRelVO.setModeloboleto_instrucao1Inferior(modeloBoletoVO.getInstrucao1Inferior());
					boletoBancarioRelVO.setModeloboleto_instrucao2Inferior(modeloBoletoVO.getInstrucao2Inferior());
					boletoBancarioRelVO.setModeloboleto_instrucao3Inferior(modeloBoletoVO.getInstrucao3Inferior());
					boletoBancarioRelVO.setModeloboleto_instrucao4Inferior(modeloBoletoVO.getInstrucao4Inferior());
					boletoBancarioRelVO.setModeloboleto_instrucao5Inferior(modeloBoletoVO.getInstrucao5Inferior());
					boletoBancarioRelVO.setModeloboleto_instrucao6Inferior(modeloBoletoVO.getInstrucao6Inferior());
					boletoBancarioRelVO.setModeloboleto_textoRodapeInferior(modeloBoletoVO.getTextoRodapeInferior().replace(TagModeloBoletoEnum.CODIGO_ADMINISTRATIVO.getValor(), boletoBancarioRelVO.getCodigoAdministrativo()).replace(TagModeloBoletoEnum.DATA_MAXIMO_RECEBIMENTO.getValor(), boletoBancarioRelVO.getDataMaximoRecebimento()));
				}
			} else {
				throw new ConsistirException("É necessário configurar os modelos de boleto no Cadastro do Banco");
			}
		}
		return boletoBancarioRelVOs;
	}

	public ModeloBoletoVO definirModeloBoleto(BoletoBancarioRelVO boletoBancarioRelVO, UsuarioVO usuarioVO, ConfiguracaoFinanceiroVO config) throws Exception {
		ModeloBoletoVO modeloBoletoVO = null;
		String tipoBoleto;
		tipoBoleto = TipoBoletoBancario.getNomeNaBase(boletoBancarioRelVO.getTipoBoletoContaReceber());
		modeloBoletoVO = config.getModeloBoleto(tipoBoleto);
		if (modeloBoletoVO.getCodigo() != null && modeloBoletoVO.getCodigo() > 0) {
			modeloBoletoVO = getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(modeloBoletoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		}
		if (tipoBoleto.equals("modeloBoletoMatricula")) {
			if (boletoBancarioRelVO.getModeloBoletoMatricula_codigo() != null && boletoBancarioRelVO.getModeloBoletoMatricula_codigo() != 0) {
				modeloBoletoVO = getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(boletoBancarioRelVO.getModeloBoletoMatricula_codigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			}
		}
		if (tipoBoleto.equals("modeloBoletoRenegociacao")) {
			if (boletoBancarioRelVO.getModeloBoletoRenegociacao_codigo() != null && boletoBancarioRelVO.getModeloBoletoRenegociacao_codigo() != 0) {
				modeloBoletoVO = getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(boletoBancarioRelVO.getModeloBoletoRenegociacao_codigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			}
		}
		if (tipoBoleto.equals("modeloBoletoMensalidade")) {
			if (boletoBancarioRelVO.getModeloBoletoMensalidade_codigo() != null && boletoBancarioRelVO.getModeloBoletoMensalidade_codigo() != 0) {
				modeloBoletoVO = getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(boletoBancarioRelVO.getModeloBoletoMensalidade_codigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			}
		}
		if (tipoBoleto.equals("modeloBoletoMaterialDidatico")) {
			if (boletoBancarioRelVO.getModeloBoletoMaterialDidatico_codigo() != null && boletoBancarioRelVO.getModeloBoletoMaterialDidatico_codigo() != 0) {
				modeloBoletoVO = getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(boletoBancarioRelVO.getModeloBoletoMaterialDidatico_codigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			}
		}
		if (tipoBoleto.equals("modeloBoletoOutros")) {
			if (boletoBancarioRelVO.getModeloBoletoOutros_codigo() != null && boletoBancarioRelVO.getModeloBoletoOutros_codigo() != 0) {
				modeloBoletoVO = getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(boletoBancarioRelVO.getModeloBoletoOutros_codigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			}
		}
		if (tipoBoleto.equals("modeloBoletoProcessoSeletivo")) {
			if (boletoBancarioRelVO.getModeloBoletoProcessoSeletivo_codigo() != null && boletoBancarioRelVO.getModeloBoletoProcessoSeletivo_codigo() != 0) {
				modeloBoletoVO = getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(boletoBancarioRelVO.getModeloBoletoProcessoSeletivo_codigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			}
		}
		if (tipoBoleto.equals("modeloBoletoRequerimento")) {
			if (boletoBancarioRelVO.getModeloBoletoRequerimento_codigo() != null && boletoBancarioRelVO.getModeloBoletoRequerimento_codigo() != 0) {
				modeloBoletoVO = getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(boletoBancarioRelVO.getModeloBoletoRequerimento_codigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			}
		}
		if (tipoBoleto.equals("modeloBoletoBiblioteca")) {
			if (boletoBancarioRelVO.getModeloBoletoBiblioteca_codigo() != null && boletoBancarioRelVO.getModeloBoletoBiblioteca_codigo() != 0) {
				modeloBoletoVO = getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(boletoBancarioRelVO.getModeloBoletoBiblioteca_codigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			}
		}
		return modeloBoletoVO;
	}

	public void validarCodigoBarrasParaGeracaoBarCode(List<BoletoBancarioRelVO> boletoBancarioRelVOs) throws Exception {
		for (BoletoBancarioRelVO boletoBancarioRelVO : boletoBancarioRelVOs) {
			if (boletoBancarioRelVO.getContareceber_codigobarra().equals("")) {
				throw new ConsistirException("O boleto  do sacado: (" + boletoBancarioRelVO.getPessoa_nome() + ") cujo código da conta a receber é: (" + boletoBancarioRelVO.getContareceber_codigo() + " ) está com código de barras inválido");
			}
		}
	}

	/**
	 * Operação reponsável por retornar o arquivo (caminho e nome) correspondente ao design do relatório criado pelo IReport.
	 */
	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getDesignIReportRelatorioBoletoSegundo() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeBoletoSegundo() + ".jrxml");
	}

	public static String getDesignIReportRelatorioCaixaEconomica() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeCaixaEconomica() + ".jrxml");
	}
	
	public static String getDesignIReportRelatorioBanestes() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeBanestes() + ".jrxml");
	}

	public static String getDesignIReportRelatorioCaixaEconomicaSicob() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeCaixaEconomicaSicob() + ".jrxml");
	}

	public static String getDesignIReportRelatorioBoletoSegundoCaixaEconomica() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeBoletoSegundoCaixaEconomica() + ".jrxml");
	}

	public static String getDesignIReportRelatorioCarne() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeCarne() + ".jrxml");
	}

	public static String getDesignIReportRelatorioCarneCaixaSicob() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeCarne() + "CaixaSicob.jrxml");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see relatorio.negocio.jdbc.financeiro.BoletoBancarioRelInterfaceFacade# getCaminhoBaseRelatorio()
	 */
	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return ("BoletoBancario");
	}

	public static String getIdEntidadeBoletoSegundo() {
		return ("BoletoBancarioSegundo");
	}

	public static String getIdEntidadeCaixaEconomica() {
		return ("BoletoBancarioCaixaEconomica");
	}
	
	public static String getIdEntidadeBanestes() {
		return ("BoletoBancarioBanestes");
	}

	public static String getIdEntidadeCaixaEconomicaSicob() {
		return ("BoletoBancarioCaixaEconomicaSicob");
	}

	public static String getIdEntidadeBoletoSegundoCaixaEconomica() {
		return ("BoletoBancarioSegundoCaixaEconomica");
	}

	public static String getIdEntidadeCarne() {
		return ("CarneBancario");
	}

	@SuppressWarnings("static-access")
	public List<BoletoBancarioRelVO> emitirRelatorioListaContaAgrupada(Boolean trazerApenasAlunosAtivos, ContaReceberAgrupadaVO contaReceberAgrupadaVO, String matricula, String ano, String semestre, String parcela, Integer curso, Integer turma, Date dataInicio, Date dataFim, Integer unidadeEnsino, String valorConsultaFiltro, Integer centroReceita, UsuarioVO usuarioVO, String tipoImpressaoBoleto, Integer codigoRenegociacao, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer responsavelFinanceiro, Boolean permiteEmitirBoletoRecebido) throws Exception {
		List<PlanoDescontoContaReceberVO> listaPlanoDescontoContaReceber = null;
		DescontoProgressivoVO descontoProgressivoVO = null;
		Boolean eMatricula = null;
		List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroAlunoDescricaoDescontos = null;
		List<PlanoDescontoVO> listaPlanoDesconto = new ArrayList<PlanoDescontoVO>(0);
		List<ConvenioVO> listaConvenios = new ArrayList<ConvenioVO>(0);
		List<Integer> listaCodigoContaReceber = new ArrayList<Integer>(0);
		BoletoBancarioRelVO boletoBancarioContaAgrupadaRelVO = new BoletoBancarioRelVO();
		try {
			validarDadosFiltro(matricula, curso, turma, unidadeEnsino, valorConsultaFiltro, responsavelFinanceiro);
			contaReceberAgrupadaVO.criarBoleto(contaReceberAgrupadaVO.getContaCorrente());
			listaCodigoContaReceber = getFacadeFactory().getContaReceberFacade().consultarContaReceberPorContaAgrupada(contaReceberAgrupadaVO.getCodigo());
			List<BoletoBancarioRelVO> boletoBancarioRelVOs = executarConsultaParametrizada(trazerApenasAlunosAtivos, contaReceberAgrupadaVO.getCodigo(), listaCodigoContaReceber, matricula, ano, semestre, parcela, curso, turma, dataInicio, dataFim, unidadeEnsino, usuarioVO, tipoImpressaoBoleto, codigoRenegociacao, responsavelFinanceiro, centroReceita, permiteEmitirBoletoRecebido, null, null);
			Map<Integer, List<PlanoDescontoContaReceberVO>> planoDescontoContaReceberVOs = getFacadeFactory().getPlanoDescontoContaReceberFacade().consultarPlanoDescontoContaRecberParaGeracaoBoleto(boletoBancarioRelVOs);
			adicionarCamposReferenteModeloBoletos(boletoBancarioRelVOs, usuarioVO, configuracaoFinanceiroVO);
			for (BoletoBancarioRelVO boletoBancarioRelVO : boletoBancarioRelVOs) {
				eMatricula = false;
				if (boletoBancarioRelVO.getContareceber_tipoorigem().equals("MAT")) {
					eMatricula = true;
				}

				if (planoDescontoContaReceberVOs.containsKey(boletoBancarioRelVO.getContareceber_codigo())) {
					listaPlanoDesconto = new ArrayList<PlanoDescontoVO>(0);
					listaConvenios = new ArrayList<ConvenioVO>(0);
					for (PlanoDescontoContaReceberVO p : planoDescontoContaReceberVOs.get(boletoBancarioRelVO.getContareceber_codigo())) {

						if (p.getIsConvenio()) {
							listaConvenios.add(p.getConvenio());
						} else {
							listaPlanoDesconto.add(p.getPlanoDescontoVO());
						}
					}
				} else {
					listaPlanoDesconto.clear();
					listaConvenios.clear();
				}
				if (boletoBancarioRelVO.getContareceber_descontoprogressivo() != 0 && !configuracaoFinanceiroVO.getGerarBoletoComDescontoSemValidade()) {
					// Variável responsável por controlar se a parcela é
					// matrícula ou não.

					// ////////////////// NOVO ///////////////////////

					// Pegar o número de dias de variação da data de vencimento
					// na Matricula Periodo Vencimento.
					// Integer diasVariacaoDataVencimento =
					// getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarDiasVariacaoDataVencimentoPeloCodigoContaReceber(boletoBancarioRelVO.getContareceber_codigo(),
					// null);
					// getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(boletoBancarioRelVO.getContareceber_descontoprogressivo(),
					// null);
					descontoProgressivoVO = new DescontoProgressivoVO();
					descontoProgressivoVO.setNovoObj(false);
					descontoProgressivoVO.setCodigo(boletoBancarioRelVO.getContareceber_descontoprogressivo());
					descontoProgressivoVO.setUtilizarDiaFixo(boletoBancarioRelVO.getDescontoprogressivo_utilizarDiaFixo());
					descontoProgressivoVO.setUtilizarDiaUtil(boletoBancarioRelVO.getDescontoprogressivo_utilizarDiaUtil());
					if (boletoBancarioRelVO.getContareceber_utilizarDescontoProgressivoManual()) {
						descontoProgressivoVO.setDescontoProgressivoEditadoManualmenteContaReceber(Boolean.TRUE);
					}
					descontoProgressivoVO.setDiaLimite1(boletoBancarioRelVO.getDescontoprogressivo_dialimite1());
					descontoProgressivoVO.setDiaLimite2(boletoBancarioRelVO.getDescontoprogressivo_dialimite2());
					descontoProgressivoVO.setDiaLimite3(boletoBancarioRelVO.getDescontoprogressivo_dialimite3());
					descontoProgressivoVO.setDiaLimite4(boletoBancarioRelVO.getDescontoprogressivo_dialimite4());
					descontoProgressivoVO.setPercDescontoLimite1(boletoBancarioRelVO.getDescontoprogressivo_percdescontolimite1());
					descontoProgressivoVO.setPercDescontoLimite2(boletoBancarioRelVO.getDescontoprogressivo_percdescontolimite2());
					descontoProgressivoVO.setPercDescontoLimite3(boletoBancarioRelVO.getDescontoprogressivo_percdescontolimite3());
					descontoProgressivoVO.setPercDescontoLimite4(boletoBancarioRelVO.getDescontoprogressivo_valordescontolimite4());
					descontoProgressivoVO.setValorDescontoLimite1(boletoBancarioRelVO.getDescontoprogressivo_valordescontolimite1());
					descontoProgressivoVO.setValorDescontoLimite2(boletoBancarioRelVO.getDescontoprogressivo_valordescontolimite2());
					descontoProgressivoVO.setValorDescontoLimite3(boletoBancarioRelVO.getDescontoprogressivo_valordescontolimite3());
					descontoProgressivoVO.setValorDescontoLimite4(boletoBancarioRelVO.getDescontoprogressivo_valordescontolimite4());

					listaPlanoFinanceiroAlunoDescricaoDescontos = getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(eMatricula, boletoBancarioRelVO.getContareceber_valorBaseContaReceber(), boletoBancarioRelVO.getContareceber_tipodesconto(), boletoBancarioRelVO.getContareceber_valordesconto(), boletoBancarioRelVO.getContareceber_valordesconto(), boletoBancarioRelVO.getDescontoValidoatedataparcela(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getContareceber_dataoriginalvencimento(), boletoBancarioRelVO.getOrdemDescontos(), descontoProgressivoVO, listaPlanoDesconto, listaConvenios, boletoBancarioRelVO.getDiasVariacaoDataVencimento(), boletoBancarioRelVO.getContareceber_usadescontocompostoplanodesconto(), configuracaoFinanceiroVO, Boolean.FALSE, null, boletoBancarioRelVO.getContareceber_matriculaaluno(), boletoBancarioRelVO.getCondicao_aplicarCalculoComBaseDescontosCalculados(), boletoBancarioRelVO.getCodigoCidadeUnidadeEnsino());
					getFacadeFactory().getContaReceberFacade().adicionarDescontoRateioEmPlanoFinanceiroAlunoDescricaoDescontosVO(listaPlanoFinanceiroAlunoDescricaoDescontos, boletoBancarioRelVO.getContareceber_valorBaseContaReceber(),  boletoBancarioRelVO.getContareceber_valorCusteadoContaReceber(), boletoBancarioRelVO.getContareceber_valorDescontoRateio(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getContareceber_datavencimento());
					inicializarContaReceberListaDesconto(boletoBancarioRelVO.getContareceber_codigo(), listaPlanoFinanceiroAlunoDescricaoDescontos);

					if (boletoBancarioRelVO.getDescontoValidoatedataparcela()) {
						if (boletoBancarioRelVO.getContareceber_valordesconto() != 0) {
							PlanoFinanceiroAlunoDescricaoDescontosVO obj = new PlanoFinanceiroAlunoDescricaoDescontosVO();
							obj.setDiaNrAntesVencimento(0);
							obj.setDataInicioAplicacaoDesconto(Uteis.obterDataFutura(boletoBancarioRelVO.getContareceber_datavencimento(), 2));
							obj.setDataLimiteAplicacaoDesconto(boletoBancarioRelVO.getContareceber_datavencimento());
							obj.setValorBase(boletoBancarioRelVO.getContareceber_valor());
							obj.setValorDescontoConvenio(boletoBancarioRelVO.getContareceber_descontoconvenio());
							obj.setValorDescontoInstituicao(boletoBancarioRelVO.getContareceber_descontoinstituica());
							obj.setValorDescontoAluno(0.0);
							obj.setTipoOrigemDesconto(obj.TIPODESCONTOPADRAO);
							obj.setApresentarDescricaoBoleto(Boolean.TRUE);
							listaPlanoFinanceiroAlunoDescricaoDescontos.add(obj);

						}
					}
					boletoBancarioContaAgrupadaRelVO.getListaPlanoFinanceiroAlunoDescricaoDescontos().addAll(listaPlanoFinanceiroAlunoDescricaoDescontos);
					descontoProgressivoVO = null;
					if (listaPlanoDescontoContaReceber != null) {
						listaPlanoDescontoContaReceber.clear();
					}
					listaPlanoDescontoContaReceber = null;
					if (listaPlanoFinanceiroAlunoDescricaoDescontos != null) {
						listaPlanoFinanceiroAlunoDescricaoDescontos.clear();
					}
					listaPlanoFinanceiroAlunoDescricaoDescontos = null;

				} else {
					listaPlanoFinanceiroAlunoDescricaoDescontos = getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(eMatricula, boletoBancarioRelVO.getContareceber_valorBaseContaReceber(), boletoBancarioRelVO.getContareceber_tipodesconto(), boletoBancarioRelVO.getContareceber_valordesconto(), boletoBancarioRelVO.getContareceber_valordesconto(), boletoBancarioRelVO.getDescontoValidoatedataparcela(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getOrdemDescontos(), descontoProgressivoVO, listaPlanoDesconto, listaConvenios, boletoBancarioRelVO.getDiasVariacaoDataVencimento(), boletoBancarioRelVO.getContareceber_usadescontocompostoplanodesconto(), configuracaoFinanceiroVO, Boolean.FALSE, null, boletoBancarioRelVO.getContareceber_matriculaaluno(), boletoBancarioRelVO.getCondicao_aplicarCalculoComBaseDescontosCalculados(), boletoBancarioRelVO.getCodigoCidadeUnidadeEnsino());
					getFacadeFactory().getContaReceberFacade().adicionarDescontoRateioEmPlanoFinanceiroAlunoDescricaoDescontosVO(listaPlanoFinanceiroAlunoDescricaoDescontos, boletoBancarioRelVO.getContareceber_valorBaseContaReceber(),  boletoBancarioRelVO.getContareceber_valorCusteadoContaReceber(), boletoBancarioRelVO.getContareceber_valorDescontoRateio(), boletoBancarioRelVO.getContareceber_datavencimento(), boletoBancarioRelVO.getContareceber_datavencimento());
					inicializarContaReceberListaDesconto(boletoBancarioRelVO.getContareceber_codigo(), listaPlanoFinanceiroAlunoDescricaoDescontos);
					boletoBancarioContaAgrupadaRelVO.getListaPlanoFinanceiroAlunoDescricaoDescontos().addAll(listaPlanoFinanceiroAlunoDescricaoDescontos);
				}

				if (boletoBancarioRelVO.getUtilizarDadosMatrizBoleto()) {
					UnidadeEnsinoVO unid = getFacadeFactory().getUnidadeEnsinoFacade().consultarSeExisteUnidadeMatriz(true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
					if (unid.getCodigo().intValue() != 0) {
						unid = getAplicacaoControle().getUnidadeEnsinoVO(unid.getCodigo(), usuarioVO);						
						boletoBancarioRelVO.setContareceber_cnpjMantenedora(unid.getCNPJ());
						boletoBancarioRelVO.setContareceber_razaoSocialMantenedora(unid.getRazaoSocial());
						boletoBancarioRelVO.setContareceber_mantenedora(unid.getMantenedora());
						boletoBancarioRelVO.setContareceber_foneMantenedora(unid.getTelComercial1());
					}
				} else {
					if (boletoBancarioRelVO.getContacorrente_utilizaDadosInformadosCCparaGeracaoBoleto()) {
						ContaCorrenteVO cc = (ContaCorrenteVO) getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(boletoBancarioRelVO.getContareceber_contacorrente(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
						boletoBancarioRelVO.setContareceber_cnpjMantenedora(cc.getCNPJ());
						boletoBancarioRelVO.setContareceber_razaoSocialMantenedora(cc.getRazaoSocial());
						boletoBancarioRelVO.setContareceber_mantenedora(cc.getMantenedora());
						boletoBancarioRelVO.setContareceber_foneMantenedora(cc.getTelComercial1());
					}
				}
				listaPlanoDesconto.clear();
				listaConvenios.clear();
				realizarPreenchimentoBoletoBancarioContaAgrupadaRelVO(boletoBancarioContaAgrupadaRelVO, boletoBancarioRelVO, contaReceberAgrupadaVO);
			}
			Ordenacao.ordenarLista(boletoBancarioContaAgrupadaRelVO.getListaPlanoFinanceiroAlunoDescricaoDescontos(), "dataLimiteAplicacaoDesconto");
			boletoBancarioContaAgrupadaRelVO.gerarIntrucoesDescontosContaAgrupada(boletoBancarioContaAgrupadaRelVO.getListaPlanoFinanceiroAlunoDescricaoDescontos(), listaPlanoDesconto, contaReceberAgrupadaVO.getValorTotal());
			validarCodigoBarrasParaGeracaoBarCode(boletoBancarioRelVOs);
			for (BoletoBancarioRelVO corrente : boletoBancarioRelVOs) {
				getFacadeFactory().getContaReceberFacade().alterarBooleanEmissaoBoletoRealizada(corrente.getContareceber_codigo(), true, usuarioVO);
			}
			List<BoletoBancarioRelVO> listaBoletoBancarioContaAgrupadaRelVos = new ArrayList<BoletoBancarioRelVO>(0);
			realizarPreenchimentoBoletoBancarioContaAgrupadaRelVO(boletoBancarioContaAgrupadaRelVO, contaReceberAgrupadaVO, usuarioVO);
			listaBoletoBancarioContaAgrupadaRelVos.add(boletoBancarioContaAgrupadaRelVO);
			return listaBoletoBancarioContaAgrupadaRelVos;
		} catch (Exception e) {
			throw new ConsistirException(e.getMessage());
		} finally {
			descontoProgressivoVO = null;
			if (listaPlanoDescontoContaReceber != null) {
				listaPlanoDescontoContaReceber.clear();
			}
			listaPlanoDescontoContaReceber = null;
			eMatricula = null;
			if (listaPlanoFinanceiroAlunoDescricaoDescontos != null) {
				listaPlanoFinanceiroAlunoDescricaoDescontos.clear();
			}
			listaPlanoFinanceiroAlunoDescricaoDescontos = null;
			listaPlanoDesconto.clear();
			listaConvenios.clear();
		}
	}

	public void inicializarContaReceberListaDesconto(Integer codigoContaReceber, List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroAlunoDescricaoDescontos) {
		for (PlanoFinanceiroAlunoDescricaoDescontosVO planoFinanceiroAlunoDescricaoDescontosVO : listaPlanoFinanceiroAlunoDescricaoDescontos) {
			planoFinanceiroAlunoDescricaoDescontosVO.setCodigoContaReceber(codigoContaReceber);
		}
	}
	
	
	@Override
	public String getObterDesign(String tipoBoleto, BoletoBancarioRelVO boletoBancarioRelVO){
		String design = "";
		if (tipoBoleto.equals("boleto")) {
            if (boletoBancarioRelVO != null && boletoBancarioRelVO.getBanco_nrbanco().equals("104")) {
                design = BoletoBancarioRel.getDesignIReportRelatorioCaixaEconomica();
            } else if (boletoBancarioRelVO != null && boletoBancarioRelVO.getBanco_nrbanco().equals("341")) {
            	design = BoletoBancarioRel.getDesignIReportRelatorioItau();
            } else if (boletoBancarioRelVO != null && (boletoBancarioRelVO.getBanco_nrbanco().equals("237") || boletoBancarioRelVO.getBanco_nrbanco().equals("707"))) {
				design = BoletoBancarioRel.getDesignIReportRelatorioBradesco();
            } else if (boletoBancarioRelVO != null && boletoBancarioRelVO.getBanco_nrbanco().equals("104-S")) {
            	design = BoletoBancarioRel.getDesignIReportRelatorioCaixaEconomicaSicob();
            } else if (boletoBancarioRelVO != null && boletoBancarioRelVO.getBanco_nrbanco().equals("104-N15")) {
            	design = BoletoBancarioRel.getDesignIReportRelatorioCaixaEconomica();
            } else if (boletoBancarioRelVO != null && boletoBancarioRelVO.getBanco_nrbanco().equals("001")) {
            	design = BoletoBancarioRel.getDesignIReportRelatorioBB();
            } else if (boletoBancarioRelVO != null && boletoBancarioRelVO.getBanco_nrbanco().equals("422")) {
            	design = BoletoBancarioRel.getDesignIReportRelatorioSafra();
            } else if (boletoBancarioRelVO != null && boletoBancarioRelVO.getBanco_nrbanco().equals("021")) {
            	design = BoletoBancarioRel.getDesignIReportRelatorioBanestes();
            } else {
                design = BoletoBancarioRel.getDesignIReportRelatorio();
            }                
        } else if (tipoBoleto.equals("boletoSegundo")) {
            if (boletoBancarioRelVO != null && boletoBancarioRelVO.getBanco_nrbanco().equals("104")) {
                design = BoletoBancarioRel.getDesignIReportRelatorioBoletoSegundoCaixaEconomica();
            } else if (boletoBancarioRelVO != null && boletoBancarioRelVO.getBanco_nrbanco().equals("341")) {
            	design = BoletoBancarioRel.getDesignIReportRelatorioBoletoSegundoItau();
            } else if (boletoBancarioRelVO != null && (boletoBancarioRelVO.getBanco_nrbanco().equals("237") || boletoBancarioRelVO.getBanco_nrbanco().equals("707"))) {
				design = BoletoBancarioRel.getDesignIReportRelatorioBoletoSegundoBradesco();
			} else if (boletoBancarioRelVO != null && boletoBancarioRelVO.getBanco_nrbanco().equals("104-S")) {
            	design = BoletoBancarioRel.getDesignIReportRelatorioCaixaEconomicaSicob();
            } else if (boletoBancarioRelVO != null && boletoBancarioRelVO.getBanco_nrbanco().equals("104-N15")) {
            	design = BoletoBancarioRel.getDesignIReportRelatorioBoletoSegundoCaixaEconomica();
            } else {
                design = BoletoBancarioRel.getDesignIReportRelatorioBoletoSegundo();
            }                
        } else {
        	if (boletoBancarioRelVO != null && boletoBancarioRelVO.getBanco_nrbanco().equals("104-S")) {
        		design = BoletoBancarioRel.getDesignIReportRelatorioCarneCaixaSicob();                
        	} else {
        		design = BoletoBancarioRel.getDesignIReportRelatorioCarne();
        	}
        }
        return design;
	}
	

	@Override
	public void realizarImpressaoPDF(List<BoletoBancarioRelVO> boletoBancarioRelVOs, SuperParametroRelVO superParametroRelVO, String versaoSistema, String valorModelo, UsuarioVO usuarioVO) throws Exception {
		if (!boletoBancarioRelVOs.isEmpty()) {
			BoletoBancarioRelVO boletoBancarioRelVO = (BoletoBancarioRelVO) boletoBancarioRelVOs.get(0);
			String design = getObterDesign(valorModelo, boletoBancarioRelVO);			
			String logoBanco = boletoBancarioRelVO.getObterLogoBanco(getCaminhoPastaWeb());
			superParametroRelVO.getParametros().put("parametro1", logoBanco);
			superParametroRelVO.setNomeDesignIreport(design);
			superParametroRelVO.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			superParametroRelVO.setSubReport_Dir(BoletoBancarioRel.getCaminhoBaseRelatorio());
			superParametroRelVO.setNomeUsuario(usuarioVO.getNome());
			superParametroRelVO.setTituloRelatorio("Recibo do Sacado");
			superParametroRelVO.setListaObjetos(boletoBancarioRelVOs);
			superParametroRelVO.setCaminhoBaseRelatorio(DiarioRel.getCaminhoBaseRelatorio());
			superParametroRelVO.setVersaoSoftware(versaoSistema);
			superParametroRelVO.setQuantidade(boletoBancarioRelVOs.size());
			UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorContaReceberDadosLogoRelatorio(boletoBancarioRelVO.getContareceber_codigo(), null);

			ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsadaUnidadEnsino(unidadeEnsinoVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO);
			if (configuracaoGeralSistemaVO == null) {
				configuracaoGeralSistemaVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS_APLICATIVO, usuarioVO, null);
			}
			if (unidadeEnsinoVO != null && unidadeEnsinoVO.getExisteLogoRelatorio()) {
				superParametroRelVO.getParametros().put("logoPadraoRelatorio", configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + unidadeEnsinoVO.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + unidadeEnsinoVO.getNomeArquivoLogoRelatorio());
			}
			if (unidadeEnsinoVO != null && unidadeEnsinoVO.getExisteLogo()) {
				superParametroRelVO.getParametros().put("logoCliente", configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + unidadeEnsinoVO.getCaminhoBaseLogo().replaceAll("\\\\", "/") + "/" + unidadeEnsinoVO.getNomeArquivoLogo());
			}
		} else {
			throw new Exception(UteisJSF.internacionalizar("msg_relatorio_sem_dados"));
		}
	}

	public void gerarInstrucaoBoletoContaReceberIntegracaoFinanceiro(BoletoBancarioRelVO boletoBancarioRelVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		if (boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getTipoLayoutArquivo().equals(LayoutArquivoIntegracaoFinanceiraEnum.LAYOUT_DESC_GERAL)) {
			String style = "";// "style=\"height:200px;width:100%;overflow:auto;display:block\"";
			if (!boletoBancarioRelVO.getModeloboleto_utilizarDescricaoDescontoPersonalizado()) {
				if (boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getTipoLayoutArquivo().equals(LayoutArquivoIntegracaoFinanceiraEnum.LAYOUT_DESC_GERAL)) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais1("<div " + style + " >");
					boletoBancarioRelVO.setModeloboleto_observacoesgerais1(boletoBancarioRelVO.getModeloboleto_observacoesgerais1() + "<p>Este boleto pode ser pago até o dia " + Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()) + ".</p>");
					boletoBancarioRelVO.setModeloboleto_observacoesgerais1(boletoBancarioRelVO.getModeloboleto_observacoesgerais1() + "<p>" + "Pagamento da mensalidade " + boletoBancarioRelVO.getContareceber_parcela() + ".</p></div>");
					boletoBancarioRelVO.setModeloboleto_observacoesgerais1(boletoBancarioRelVO.getModeloboleto_observacoesgerais1() + "<p>CÓDIGO ADMINISTRATIVO: " + boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getCodigoPessoaFinanceiro() + ".</p></div>");
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2("<div " + style + " >");
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + "Este boleto pode ser pago até o dia " + Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()) + ".</p>");
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + "<p>Pagamento da mensalidade " + boletoBancarioRelVO.getContareceber_parcela() + ".</p>");
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + "<p>Este boleto não quita débitos anteriores.</p>");
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + "<p>Não receber após o dia " + Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()) + ".</p>");
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + "<p>Em caso de dúvidas entre em contato por telefone: " + boletoBancarioRelVO.getTelefoneUnidadeEnsino() + ".</p>");
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + "<p>NÃO RECEBER VALOR MENOR QUE (=) VALOR COBRADO.</p>");
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + "<p>LEIA ATENTAMENTE AS INSTRUÇÕES ACIMA.</p></div>");
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + "<p>CÓDIGO ADMINISTRATIVO: " + boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getCodigoPessoaFinanceiro() + ".</p></div>");
				}
			} else {
				ModeloBoletoVO modelo = new ModeloBoletoVO();
				String valorTotalDesconto = Uteis.formatarDoubleParaMoeda(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDesconto() + boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getBolsa());
				String percDescontoProgressivo = Uteis.formatarDecimal(0.0, "#0.0000");
				String valorDescontoProgressivo = Uteis.formatarDoubleParaMoeda(0.0);
				String valorDescontoAluno = Uteis.formatarDoubleParaMoeda(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDesconto());
				String valorDescontoInstituicao = Uteis.formatarDoubleParaMoeda(0.0);
				String valorDescontoConvenio = Uteis.formatarDoubleParaMoeda(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getBolsa());
				String valorTotal = Uteis.formatarDoubleParaMoeda(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getValorReceber());

				boletoBancarioRelVO.setModeloboleto_observacoesgerais1("<div " + style + " >");
				String tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_textoTopo(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais1(boletoBancarioRelVO.getModeloboleto_observacoesgerais1() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_instrucao1(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais1(boletoBancarioRelVO.getModeloboleto_observacoesgerais1() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_instrucao2(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais1(boletoBancarioRelVO.getModeloboleto_observacoesgerais1() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_instrucao3(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais1(boletoBancarioRelVO.getModeloboleto_observacoesgerais1() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_instrucao4(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais1(boletoBancarioRelVO.getModeloboleto_observacoesgerais1() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_instrucao5(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais1(boletoBancarioRelVO.getModeloboleto_observacoesgerais1() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_instrucao6(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais1(boletoBancarioRelVO.getModeloboleto_observacoesgerais1() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_textoRodape(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais1(boletoBancarioRelVO.getModeloboleto_observacoesgerais1() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				boletoBancarioRelVO.setModeloboleto_observacoesgerais1(boletoBancarioRelVO.getModeloboleto_observacoesgerais1() + "</div>");

				boletoBancarioRelVO.setModeloboleto_observacoesgerais2("<div " + style + " >");
				tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_textoTopoInferior(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_instrucao1Inferior(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_instrucao2Inferior(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_instrucao3Inferior(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_instrucao4Inferior(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_instrucao5Inferior(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_instrucao6Inferior(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				tag = modelo.substituirTags(boletoBancarioRelVO.getModeloboleto_textoRodapeInferior(), Uteis.getData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()), valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, boletoBancarioRelVO.getCodigoAdministrativo(), "0,00");
				if (!Uteis.removeHTML(tag).trim().isEmpty()) {
					boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + tag.substring(tag.indexOf("<body>") + 6, tag.indexOf("</body>")));
				}
				boletoBancarioRelVO.setModeloboleto_observacoesgerais2(boletoBancarioRelVO.getModeloboleto_observacoesgerais2() + "</div>");
			}
		} else if (boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getTipoLayoutArquivo().equals(LayoutArquivoIntegracaoFinanceiraEnum.LAYOUT_DESC_DETALHADO) || boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getTipoLayoutArquivo().equals(LayoutArquivoIntegracaoFinanceiraEnum.LAYOUT_DESC_GERAL_2)) {
			List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroAlunoDescricaoDescontos = getFacadeFactory().getIntegracaoFinanceiroFacade().realizarGeracaoPlanoDescontoDescricaoAluno(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe(), configuracaoFinanceiroVO);
			boletoBancarioRelVO.setDescontoValidoatedataparcela(false);
			boletoBancarioRelVO.setDescontoprogressivo_dialimite1(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataVencimentoDescPontualidade1() != null ? Uteis.getDiaMesData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataVencimentoDescPontualidade1()) : null);
			boletoBancarioRelVO.setDescontoprogressivo_dialimite2(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataVencimentoDescPontualidade2() != null ? Uteis.getDiaMesData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataVencimentoDescPontualidade2()) : null);
			boletoBancarioRelVO.setDescontoprogressivo_dialimite3(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataVencimentoDescPontualidade3() != null ? Uteis.getDiaMesData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataVencimentoDescPontualidade3()) : null);
			boletoBancarioRelVO.setDescontoprogressivo_dialimite4(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataVencimentoDescPontualidade4() != null ? Uteis.getDiaMesData(boletoBancarioRelVO.getProcessoIntegracaoFinanceiroDetalhe().getDataVencimentoDescPontualidade4()) : null);
			boletoBancarioRelVO.gerarIntrucoesDescontos(boletoBancarioRelVO, listaPlanoFinanceiroAlunoDescricaoDescontos, new ArrayList<PlanoDescontoVO>(0), configuracaoFinanceiroVO);
		}

	}

	public static String getDesignIReportRelatorioBradesco() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeBradesco() + ".jrxml");
	}

	public static String getDesignIReportRelatorioBoletoSegundoBradesco() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeBoletoSegundoBradesco() + ".jrxml");
	}

	public static String getIdEntidadeBradesco() {
		return ("BoletoBancarioBradesco");
	}

	public static String getIdEntidadeBoletoSegundoBradesco() {
		return ("BoletoBancarioSegundoBradesco");
	}

	public static String getDesignIReportRelatorioItau() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeItau() + ".jrxml");
	}

	public static String getDesignIReportRelatorioBB() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeBB() + ".jrxml");
	}

	public static String getDesignIReportRelatorioSafra() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeSafra() + ".jrxml");
	}

	public static String getDesignIReportRelatorioBoletoSegundoItau() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeBoletoSegundoCaixaEconomica() + ".jrxml");
	}

	public static String getIdEntidadeItau() {
		return ("BoletoBancarioItau");
	}

	public static String getIdEntidadeBB() {
		return ("BoletoBancarioBB");
	}

	public static String getIdEntidadeSafra() {
		return ("BoletoBancarioSafra");
	}

	public static String getIdEntidadeBoletoSegundoItau() {
		return ("BoletoBancarioSegundoItau");
	}
	
	public void validarImpressaoBoletoAluno(Integer codigoContaReceber, BoletoBancarioRelVO boletoBancarioRelVO, String origemRotina, Integer codigoUsuario) throws Exception {
		if(Uteis.isAtributoPreenchido(codigoUsuario)
			&&	!getFacadeFactory().getContaReceberFacade().validarSeExisteContaParaUsuarioComCodigoContaReceber(codigoUsuario, boletoBancarioRelVO.getContareceber_codigo())){
        	InteracaoWorkflowVO interacao = new InteracaoWorkflowVO();
        	interacao.getResponsavel().setCodigo(codigoUsuario);
        	interacao.setDataInicio(new Date());
        	interacao.setHoraInicio(Uteis.getHoraAtualComSegundos());
        	interacao.setDataTermino(new Date());
        	interacao.setHoraTermino(Uteis.getHoraAtualComSegundos());
        	interacao.setTipoOrigemInteracao(TipoOrigemInteracaoEnum.IMPRESSAO_BOLETO);
        	interacao.setIdentificadorOrigem(boletoBancarioRelVO.getContareceber_parcela());
        	interacao.setCodigoEntidadeOrigem(boletoBancarioRelVO.getContareceber_codigo());
    		interacao.setObservacao("Impressão de Boleto tentando imprimir codigoContaReceber- " +codigoContaReceber + " porem ao passar pela rotina de emitirRelatorioLista boleto veio " +boletoBancarioRelVO.getContareceber_codigo()+"- origem rotina - " +origemRotina);
    		new InteracaoWorkflow().incluirSemValidarDados(interacao, new UsuarioVO()); 
        	throw new Exception("Não foi possivel realizar a imprensão do boleto no momente por favor tentar daqui alguns minutos.");
        }
		
	}

	public void realizarAdicaoValoresAdicionaisBoleto(BoletoBancarioRelVO boletoBancarioRelVO, boolean forcarRegeracao) throws Exception {
	  if ((!boletoBancarioRelVO.getContaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().equals(BigDecimal.ZERO) 
      		|| !boletoBancarioRelVO.getContaReceber_valorIndiceReajuste().equals(BigDecimal.ZERO)
      		|| boletoBancarioRelVO.getContareceber_valorIndiceReajustePorAtraso() != 0.0
      		|| boletoBancarioRelVO.getContareceber_acrescimo() != 0.0
      		|| forcarRegeracao
      		|| (Uteis.getData(boletoBancarioRelVO.getContareceber_dataoriginalvencimento()) != Uteis.getData(boletoBancarioRelVO.getContareceber_datavencimento()))) ) {
		  
		  boletoBancarioRelVO.getContareceber_totalAcrescimo();
		  boletoBancarioRelVO.setContareceber_valorBaseContaReceber(boletoBancarioRelVO.getContareceber_valorBaseContaReceber() + boletoBancarioRelVO.getContareceber_totalAcrescimo());
  		  boletoBancarioRelVO.setContareceber_valor(boletoBancarioRelVO.getContareceber_valor() + boletoBancarioRelVO.getContareceber_totalAcrescimo());
  		  if(boletoBancarioRelVO.getDataVencimentoRemessaOnline() != null) {
			boletoBancarioRelVO.setContareceber_datavencimento(boletoBancarioRelVO.getDataVencimentoRemessaOnline());
  		  }
  		  regerarCodigoBarraTaxaBoleto(boletoBancarioRelVO, null);
	  }
	}
	
	private void realizarAlteracaoDadosBoletoComRemessaOnlineAtualizandoVencimentoValor(BoletoBancarioRelVO boletoBancarioRelVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception{
		if(boletoBancarioRelVO.getContacorrente_habilitarRegistroRemessaOnline() && !boletoBancarioRelVO.getContareceber_datavencimento().after(Uteis.obterDataAntiga(new Date(), 1)) && !Uteis.isAtributoPreenchido(boletoBancarioRelVO.getContaCorrenteVO())) {
			boletoBancarioRelVO.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(boletoBancarioRelVO.getContareceber_contacorrente(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));			
		}
		if(boletoBancarioRelVO.getContaCorrenteVO().getPermiteGerarRemessaOnlineBoletoVencido() &&
				(
				(boletoBancarioRelVO.getContareceber_tipoorigem().equals(TipoOrigemContaReceber.BIBLIOTECA.getValor())
				&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaBibliotecaAut()
				&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaBoletoVencidoBiblioteca()
				&& (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineBiblioteca().equals(0) 
				|| (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineBiblioteca() > 0 && Uteis.nrDiasEntreDatas(Uteis.getDataMinutosHoraDia(new Date()), Uteis.getDataMinutosHoraDia(boletoBancarioRelVO.getContareceber_datavencimento())) <= boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineBiblioteca()
				)))
				|| (boletoBancarioRelVO.getContareceber_tipoorigem().equals(TipoOrigemContaReceber.BOLSA_CUSTEADA_CONVENIO.getValor())
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaBoletoVencidoConvenio()
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaConvenioAut()
						&& (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaConvenio().equals(0) 
						|| (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaConvenio() > 0 && Uteis.nrDiasEntreDatas(Uteis.getDataMinutosHoraDia(new Date()), Uteis.getDataMinutosHoraDia(boletoBancarioRelVO.getContareceber_datavencimento())) <= boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaConvenio()
						)))				
				|| (boletoBancarioRelVO.getContareceber_tipoorigem().equals(TipoOrigemContaReceber.CONTRATO_RECEITA.getValor())
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaBoletoVencidoContratoReceita()
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaContratoReceitaAut()
						&& (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaContratoReceita().equals(0) 
						|| (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaContratoReceita() > 0 && Uteis.nrDiasEntreDatas(Uteis.getDataMinutosHoraDia(new Date()), Uteis.getDataMinutosHoraDia(boletoBancarioRelVO.getContareceber_datavencimento())) <= boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaContratoReceita()
						)))
				|| (boletoBancarioRelVO.getContareceber_tipoorigem().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor())
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaBoletoVencidoDevCheque()
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaDevChequeAut()
						&& (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineDevCheque().equals(0) 
						|| (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineDevCheque() > 0 && Uteis.nrDiasEntreDatas(Uteis.getDataMinutosHoraDia(new Date()), Uteis.getDataMinutosHoraDia(boletoBancarioRelVO.getContareceber_datavencimento())) <= boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineDevCheque()
						)))
				|| (boletoBancarioRelVO.getContareceber_tipoorigem().equals(TipoOrigemContaReceber.INCLUSAOREPOSICAO.getValor())
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaBoletoVencidoInclusaoReposicao()
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaInclusaoReposicaoAut()
						&& (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaInclusaoExclusao().equals(0) 
								|| (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaInclusaoExclusao() > 0 && Uteis.nrDiasEntreDatas(Uteis.getDataMinutosHoraDia(new Date()), Uteis.getDataMinutosHoraDia(boletoBancarioRelVO.getContareceber_datavencimento())) <= boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaInclusaoExclusao()
										)))
				|| (boletoBancarioRelVO.getContareceber_tipoorigem().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO.getValor())
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaBoletoVencidoMateriaDidatico()
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaMateriaDidaticoAut()
						&& (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaMaterialDidatico().equals(0) 
								|| (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaMaterialDidatico() > 0 && Uteis.nrDiasEntreDatas(Uteis.getDataMinutosHoraDia(new Date()), Uteis.getDataMinutosHoraDia(boletoBancarioRelVO.getContareceber_datavencimento())) <= boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaMaterialDidatico()
										)))
				|| (boletoBancarioRelVO.getContareceber_tipoorigem().equals(TipoOrigemContaReceber.MATRICULA.getValor())
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaBoletoVencidoMatricula()
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaMatriculaAut()
						&& (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineMatricula().equals(0) 
								|| (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineMatricula() > 0 && Uteis.nrDiasEntreDatas(Uteis.getDataMinutosHoraDia(new Date()), Uteis.getDataMinutosHoraDia(boletoBancarioRelVO.getContareceber_datavencimento())) <= boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineMatricula()
										)))
				|| (boletoBancarioRelVO.getContareceber_tipoorigem().equals(TipoOrigemContaReceber.MENSALIDADE.getValor())
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaBoletoVencidoParcelas()
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaParcelasAut()
						&& (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineParcela().equals(0) 
								|| (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineParcela() > 0 && Uteis.nrDiasEntreDatas(Uteis.getDataMinutosHoraDia(new Date()), Uteis.getDataMinutosHoraDia(boletoBancarioRelVO.getContareceber_datavencimento())) <= boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineParcela()
										)))
				|| (boletoBancarioRelVO.getContareceber_tipoorigem().equals(TipoOrigemContaReceber.NEGOCIACAO.getValor())
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaBoletoVencidoNegociacao()
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaNegociacaoAut()
						&& (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineNegociacao().equals(0) 
								|| (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineNegociacao() > 0 && Uteis.nrDiasEntreDatas(Uteis.getDataMinutosHoraDia(new Date()), Uteis.getDataMinutosHoraDia(boletoBancarioRelVO.getContareceber_datavencimento())) <= boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineNegociacao()
										)))
				|| (boletoBancarioRelVO.getContareceber_tipoorigem().equals(TipoOrigemContaReceber.OUTROS.getValor())
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaBoletoVencidoOutros()
						&& boletoBancarioRelVO.getContaCorrenteVO().getGerarRemessaOutrosAut()
						&& (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineOutros().equals(0) 
								|| (boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineOutros() > 0 && Uteis.nrDiasEntreDatas(Uteis.getDataMinutosHoraDia(new Date()), Uteis.getDataMinutosHoraDia(boletoBancarioRelVO.getContareceber_datavencimento())) <= boletoBancarioRelVO.getContaCorrenteVO().getQtdeDiasVencidoPermitirRemessaOnlineOutros()
										)))			
				)
				&& boletoBancarioRelVO.getContacorrente_habilitarRegistroRemessaOnline()			
				&& !boletoBancarioRelVO.getContareceber_datavencimento().after(Uteis.obterDataAntiga(new Date(), 1))) {
			
			
				List<ContaCorrenteVO> contaCorrenteVOs =  new ArrayList<ContaCorrenteVO>(0);
				contaCorrenteVOs.add(boletoBancarioRelVO.getContaCorrenteVO());
				ControleRemessaContaReceberVO controleRemessaContaReceberVO = getFacadeFactory().getControleRemessaContaReceberFacade().consultaRapidaContaArquivoRemessaPorNossoNumeroContaReceber(contaCorrenteVOs, boletoBancarioRelVO.getContareceber_nossonumero());
				if(!Uteis.isAtributoPreenchido(controleRemessaContaReceberVO) 
						|| (Uteis.isAtributoPreenchido(controleRemessaContaReceberVO) 
								&& Uteis.nrDiasEntreDatas(Uteis.getDataMinutosHoraDia(new Date()), Uteis.getDataMinutosHoraDia(controleRemessaContaReceberVO.getDataVencimento())) >= boletoBancarioRelVO.getContaCorrenteVO().getQtdDiasBaixaAutTitulo())) {
					ContaReceberVO obj = new ContaReceberVO();
					obj.setCodigo(boletoBancarioRelVO.getContareceber_codigo());
					getFacadeFactory().getContaReceberFacade().carregarDados(obj, configuracaoFinanceiroVO, usuarioVO);
					obj.setRealizandoRecebimento(true);
					Date novaDataVencimento = new Date();
					if (configuracaoFinanceiroVO.getVencimentoParcelaDiaUtil()) {
						novaDataVencimento = getFacadeFactory().getContaReceberFacade().obterDataVerificandoDiaUtil(
								novaDataVencimento,
								boletoBancarioRelVO.getCodigoCidadeUnidadeEnsino(), null);
					}
					obj.getCalcularValorFinal(novaDataVencimento, configuracaoFinanceiroVO, true, novaDataVencimento,
							usuarioVO);
					obj.setAcrescimo(obj.getAcrescimo() + obj.getValorJuroCalculado() + obj.getValorMultaCalculado());
					boletoBancarioRelVO.setContareceber_acrescimo(obj.getAcrescimo());
					boletoBancarioRelVO.setDataMaximoRecebimento(Uteis.getData(novaDataVencimento));
					boletoBancarioRelVO.setValorAcrescimoRemessaOnline(Uteis.arrendondarForcando2CadasDecimais(obj.getValorJuroCalculado() + obj.getValorMultaCalculado()));
					boletoBancarioRelVO.setDataVencimentoRemessaOnline(novaDataVencimento);
					obj.setDataVencimento(novaDataVencimento);
					if (Uteis.isAtributoPreenchido(obj.getNossoNumero()) && Uteis.isAtributoPreenchido(controleRemessaContaReceberVO)) {
						obj.setNossoNumero("");
						getFacadeFactory().getContaReceberFacade().gerarNumeroDoc(obj, obj.getContaCorrenteVO().getAgencia().getBanco().getNrBanco(), usuarioVO);
					}
					obj.criarBoleto(boletoBancarioRelVO.getContaCorrenteVO());
					getFacadeFactory().getContaReceberFacade().alterarNossoNumeroLinhaDigitavel(obj.getCodigo(), obj.getNossoNumero(), obj.getLinhaDigitavelCodigoBarras(), obj.getCodigoBarra(), usuarioVO);
					boletoBancarioRelVO.setContareceber_codigobarra(obj.getCodigoBarra());
					boletoBancarioRelVO.setContareceber_linhadigitavelcodigobarras(obj.getLinhaDigitavelCodigoBarras());
					boletoBancarioRelVO.setContareceber_nossonumero(obj.getNossoNumero());
					boletoBancarioRelVO.setNossonumero(obj.getNossoNumero());
					boletoBancarioRelVO.setContaReceberVO(obj);
				}else if(Uteis.isAtributoPreenchido(controleRemessaContaReceberVO) 						
						&& controleRemessaContaReceberVO.getDataVencimento().after(boletoBancarioRelVO.getContareceber_datavencimento())) {		
					
					ContaReceberVO obj = new ContaReceberVO();
					obj.setCodigo(boletoBancarioRelVO.getContareceber_codigo());
					getFacadeFactory().getContaReceberFacade().carregarDados(obj, configuracaoFinanceiroVO, usuarioVO);
					obj.setRealizandoRecebimento(true);
					obj.getCalcularValorFinal(controleRemessaContaReceberVO.getDataVencimento(), configuracaoFinanceiroVO, true, controleRemessaContaReceberVO.getDataVencimento(), usuarioVO);
					obj.setAcrescimo(obj.getAcrescimo() + obj.getValorJuroCalculado() + obj.getValorMultaCalculado());
					boletoBancarioRelVO.setContareceber_acrescimo(obj.getAcrescimo());
					boletoBancarioRelVO.setDataMaximoRecebimento(Uteis.getData(controleRemessaContaReceberVO.getDataVencimento()));
					boletoBancarioRelVO.setValorAcrescimoRemessaOnline(Uteis.arrendondarForcando2CadasDecimais(obj.getValorJuroCalculado() + obj.getValorMultaCalculado()));
					boletoBancarioRelVO.setDataVencimentoRemessaOnline(controleRemessaContaReceberVO.getDataVencimento());
					if(boletoBancarioRelVO.getDataVencimentoRemessaOnline() != null) {
						boletoBancarioRelVO.setContareceber_datavencimento(boletoBancarioRelVO.getDataVencimentoRemessaOnline());
			  		  }
					boletoBancarioRelVO.setContaReceberVO(obj);
				}
			}
		
		}

}