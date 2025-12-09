package webservice.nfse.londrina;

import java.io.File;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.faturamento.nfe.ConexaoNFSE;
import negocio.comuns.crm.enumerador.TipoEmpresaEnum;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;
import negocio.comuns.faturamento.nfe.enumeradores.CodigoRegimeTributarioEnum;
import negocio.comuns.faturamento.nfe.enumeradores.SituacaoNotaFiscalSaidaEnum;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisNfe;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import webservice.nfse.NotaFiscalServicoEletronicaInterfaceFacade;

@Repository
@Scope(value = "singleton")
@Lazy
@Transactional(readOnly = true)
public class NotaFiscalServicoEletronicaLondrina extends ControleAcesso implements NotaFiscalServicoEletronicaInterfaceFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static String idEntidade;

	public NotaFiscalServicoEletronicaLondrina() throws Exception {
		super();
		setIdEntidade("NotaFiscalServicoEletronicaLondrina");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		NotaFiscalServicoEletronicaLondrina.idEntidade = idEntidade;
	}

	@Override
	public void enviarXmlEnvio(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado, UsuarioVO usuarioVO) throws Exception {
		try {
			Nfse builder =  Nfse.nfse();	
			GerarNota gerarNota = montarXmlEnvio(notaFiscalSaidaVO, usuarioVO);
			String cnpj = notaFiscalSaidaVO.getUnidadeEnsinoVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim();
			notaFiscalSaidaVO.setXmlEnvio(builder.withGerarNota(gerarNota).asGerarNFSEXML(notaFiscalSaidaVO, configuracaoRespositoriArquivo));
			getFacadeFactory().getNotaFiscalSaidaFacade().gravarXmlEnvio(notaFiscalSaidaVO.getCodigo(), notaFiscalSaidaVO.getXmlEnvio(), usuarioVO);
			notaFiscalSaidaVO.setMensagemRetorno(ConexaoNFSE.conexaoNFSELondrina(notaFiscalSaidaVO, notaFiscalSaidaVO.getWebServicesNFSEEnum().getTipoAcaoServicoNFSEEnum().name(), notaFiscalSaidaVO.getXmlEnvio(), UteisNfe.getCaminhoCertificado(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), configuracaoRespositoriArquivo), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaCertificado(), configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + "ca.jks", notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaUnidadeCertificadora(), usuarioVO));
			getFacadeFactory().getNotaFiscalSaidaFacade().alterarMensagemRetorno(notaFiscalSaidaVO, usuarioVO);
			notaFiscalSaidaVO.setDataStuacao(new Date());
			notaFiscalSaidaVO.setMensagemRetorno(notaFiscalSaidaVO.getMensagemRetorno().replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
			if (notaFiscalSaidaVO.getMensagemRetorno().contains("<Codigo>L000</Codigo><Mensagem>NORMAL</Mensagem>")) {
				String numeroNota = notaFiscalSaidaVO.getMensagemRetorno().substring(notaFiscalSaidaVO.getMensagemRetorno().indexOf("<InfNfse><Numero>"), notaFiscalSaidaVO.getMensagemRetorno().indexOf("</Numero>"));
				numeroNota = numeroNota.replace("<InfNfse><Numero>", "");
				String codigoVerificacao = notaFiscalSaidaVO.getMensagemRetorno().substring(notaFiscalSaidaVO.getMensagemRetorno().lastIndexOf("<CodigoVerificacao>"), notaFiscalSaidaVO.getMensagemRetorno().indexOf("</CodigoVerificacao>"));
				codigoVerificacao = codigoVerificacao.replace("<CodigoVerificacao>", "");
				notaFiscalSaidaVO.setIdentificadorReceita(codigoVerificacao);
				notaFiscalSaidaVO.setRecibo(numeroNota);
				getFacadeFactory().getNotaFiscalSaidaFacade().gravarRecibo(notaFiscalSaidaVO.getCodigo(), notaFiscalSaidaVO.getRecibo(), usuarioVO);
				getFacadeFactory().getNotaFiscalSaidaFacade().gravarSituacaoEnvio(notaFiscalSaidaVO, SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor(), "", notaFiscalSaidaVO.getDataStuacao(), notaFiscalSaidaVO.getIdentificadorReceita(), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioVO);
				notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor());
				notaFiscalSaidaVO.setLinkAcesso(notaFiscalSaidaVO.getWebServicesNFSEEnum().getValorApresentar().replace("{0}", notaFiscalSaidaVO.getUnidadeEnsinoVO().getInscMunicipal()).replace("{1}", notaFiscalSaidaVO.getRecibo()).replace("{2}", notaFiscalSaidaVO.getIdentificadorReceita()));
			} else {
				getFacadeFactory().getNotaFiscalSaidaFacade().gravarSituacaoEnvio(notaFiscalSaidaVO, SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor(), "", notaFiscalSaidaVO.getDataStuacao(), "", notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioVO);
				getFacadeFactory().getNotaFiscalSaidaFacade().alterarMensagemRetorno(notaFiscalSaidaVO, usuarioVO);
				notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
			}
		} catch (Exception e) {
			progressBar.setStatus("NF-e n° " + notaFiscalSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
			autorizado = e.getMessage();
			notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
			notaFiscalSaidaVO.setDataEmissao(new Date());
			getFacadeFactory().getNotaFiscalSaidaFacade().gravarSituacaoEnvio(notaFiscalSaidaVO, SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor(), notaFiscalSaidaVO.getProtocolo(), notaFiscalSaidaVO.getDataStuacao(), notaFiscalSaidaVO.getIdentificadorReceita(), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioVO);
			notaFiscalSaidaVO.setMensagemRetorno(autorizado);
			getFacadeFactory().getNotaFiscalSaidaFacade().alterarMensagemRetorno(notaFiscalSaidaVO, usuarioVO);
			throw e;
		}
	}
	
	public GerarNota montarXmlEnvio(NotaFiscalSaidaVO notaFiscalSaidaVO,UsuarioVO usuarioVO) throws Exception{
		GerarNota gerarNota = new GerarNota();
		String nomeCurso = getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(notaFiscalSaidaVO.getMatricula(), false, usuarioVO).getNome();
		String codigoTributacaoMunicipo = "";
		if (!notaFiscalSaidaVO.getUnidadeEnsinoVO().getCodigoTributacaoMunicipio().equals("")) {
			codigoTributacaoMunicipo = notaFiscalSaidaVO.getUnidadeEnsinoVO().getCodigoTributacaoMunicipio();
		} else {
			codigoTributacaoMunicipo = notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoTributacaoMunicipio();
		}
		String cnpj = notaFiscalSaidaVO.getUnidadeEnsinoVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim();
		gerarNota.setTcDescricaoRps(new TcDescricaoRps());
		gerarNota.getTcDescricaoRps().setCcm(0);
		gerarNota.getTcDescricaoRps().setCnpj(cnpj);
		gerarNota.getTcDescricaoRps().setCpf("");
		gerarNota.getTcDescricaoRps().setSenha("");
		gerarNota.getTcDescricaoRps().setServico(801);
		gerarNota.getTcDescricaoRps().setSituacao("tp");
		gerarNota.getTcDescricaoRps().setAliquota(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getIssqn(), 2));
		gerarNota.getTcDescricaoRps().setCodMunicipioPrestacaoServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCidade().getCodigoIBGE());
		gerarNota.getTcDescricaoRps().setCodPaisPrestacaoServico("01058");
		gerarNota.getTcDescricaoRps().setIncentivoFiscal(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoRegimeTributarioEnum().equals(CodigoRegimeTributarioEnum.SIMPLES_NACIONAL) ? 1 : null);
		gerarNota.getTcDescricaoRps().setValor(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(notaFiscalSaidaVO.getValorTotal(), 2));
		gerarNota.getTcDescricaoRps().setBase(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(notaFiscalSaidaVO.getValorTotal(), 2));
		if(notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor())) {
			gerarNota.getTcDescricaoRps().setTomadorRazao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getNome()));
			gerarNota.getTcDescricaoRps().setTomadorEmail(notaFiscalSaidaVO.getFornecedorVO().getEmail());
			gerarNota.getTcDescricaoRps().setTomadorEndereco(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getEndereco()));
			gerarNota.getTcDescricaoRps().setTomadorNumero("0");
			gerarNota.getTcDescricaoRps().setTomadorBairro(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getSetor()));
			gerarNota.getTcDescricaoRps().setTomadorComplemento(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getComplemento()));
			gerarNota.getTcDescricaoRps().setTomadorCEP(notaFiscalSaidaVO.getFornecedorVO().getCEP().replace(".", "").replace("-", ""));
			gerarNota.getTcDescricaoRps().setDescricaoNF("REFERENTE A SERVICO PRESTADO NO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()) + " AO  FORNECEDOR(A) "+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getNome().toUpperCase())+" DO CURSO "+Uteis.removerCaracteresEspeciais3(nomeCurso.toUpperCase())+" REFERENTE AO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()));						
			if (!Uteis.isAtributoPreenchido(notaFiscalSaidaVO.getFornecedorVO().getCidade().getCodigoIBGE())) {
				throw new Exception("Código IBGE da cidade do tomador não cadastrado! (" + notaFiscalSaidaVO.getFornecedorVO().getCidade().getNome() + ")");
			}
			gerarNota.getTcDescricaoRps().setTomadorCodCidade(notaFiscalSaidaVO.getFornecedorVO().getCidade().getCodigoIBGE());
			if(notaFiscalSaidaVO.getFornecedorVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
				gerarNota.getTcDescricaoRps().setTomadorTipo(3);
				gerarNota.getTcDescricaoRps().setTomadorCnpj(notaFiscalSaidaVO.getFornecedorVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim());
				if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() > 0) {
					gerarNota.getTcDescricaoRps().setPis(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() / 100), 2));
				}
				if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() > 0) {
					gerarNota.getTcDescricaoRps().setCofins(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() / 100), 2));
				}
				if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() > 0) {
					gerarNota.getTcDescricaoRps().setInss(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() / 100), 2));
				}
				if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() > 0) {
					gerarNota.getTcDescricaoRps().setCsll(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() / 100), 2));
				}			
			} else {
				gerarNota.getTcDescricaoRps().setTomadorTipo(2);
				gerarNota.getTcDescricaoRps().setTomadorCnpj(notaFiscalSaidaVO.getFornecedorVO().getCPF().replace("-", "").replace(".", "").trim());	
			}
		} else if(notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
			gerarNota.getTcDescricaoRps().setTomadorRazao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getNome()));
			gerarNota.getTcDescricaoRps().setTomadorEmail(notaFiscalSaidaVO.getParceiroVO().getEmail());
			gerarNota.getTcDescricaoRps().setTomadorEndereco(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getEndereco()));
			gerarNota.getTcDescricaoRps().setTomadorNumero("0");
			gerarNota.getTcDescricaoRps().setTomadorBairro(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getSetor()));
			gerarNota.getTcDescricaoRps().setTomadorComplemento(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getComplemento()));
			gerarNota.getTcDescricaoRps().setTomadorCEP(notaFiscalSaidaVO.getParceiroVO().getCEP().replace(".", "").replace("-", ""));
			gerarNota.getTcDescricaoRps().setDescricaoNF("REFERENTE A SERVICO PRESTADO NO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()) + " AO  PARCEIRO(A) "+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getNome().toUpperCase())+" DO CURSO "+Uteis.removerCaracteresEspeciais3(nomeCurso.toUpperCase())+" REFERENTE AO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()));
			if (!Uteis.isAtributoPreenchido(notaFiscalSaidaVO.getParceiroVO().getCidade().getCodigoIBGE())) {
				throw new Exception("Código IBGE da cidade do tomador não cadastrado! (" + notaFiscalSaidaVO.getParceiroVO().getCidade().getNome() + ")");
			}
			gerarNota.getTcDescricaoRps().setTomadorCodCidade(notaFiscalSaidaVO.getParceiroVO().getCidade().getCodigoIBGE());
			if(notaFiscalSaidaVO.getParceiroVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
				gerarNota.getTcDescricaoRps().setTomadorCnpj(notaFiscalSaidaVO.getParceiroVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim());
				gerarNota.getTcDescricaoRps().setTomadorTipo(3);
				if (notaFiscalSaidaVO.getParceiroVO().getPossuiAliquotaEmissaoNotaEspecifica()) {
					if(notaFiscalSaidaVO.getParceiroVO().getPis() > 0) {
						gerarNota.getTcDescricaoRps().setPis(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getPis() / 100), 2));
					}
					if(notaFiscalSaidaVO.getParceiroVO().getCofins() > 0) {
						gerarNota.getTcDescricaoRps().setCofins(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getCofins() / 100), 2));						
					}
					if(notaFiscalSaidaVO.getParceiroVO().getInss() > 0 ) {
						gerarNota.getTcDescricaoRps().setInss(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getInss() / 100), 2));						
					}
					if(notaFiscalSaidaVO.getParceiroVO().getCsll() > 0) {
						gerarNota.getTcDescricaoRps().setCsll(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getCsll() / 100), 2));
					}					
				} else {	
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() > 0) {
						gerarNota.getTcDescricaoRps().setPis(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() / 100), 2));
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() > 0) {
						gerarNota.getTcDescricaoRps().setCofins(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() / 100), 2));
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() > 0) {
						gerarNota.getTcDescricaoRps().setInss(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() / 100), 2));
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() > 0) {
						gerarNota.getTcDescricaoRps().setCsll(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaStr(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() / 100), 2));
					}
				}
			} else {
				gerarNota.getTcDescricaoRps().setTomadorCnpj(notaFiscalSaidaVO.getParceiroVO().getCPF().replace("-", "").replace(".", "").trim());	
				gerarNota.getTcDescricaoRps().setTomadorTipo(2);
			}
		} if (notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor())) {
			gerarNota.getTcDescricaoRps().setTomadorRazao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getResponsavelFinanceiro().getNome()));
			gerarNota.getTcDescricaoRps().setTomadorEmail(notaFiscalSaidaVO.getResponsavelFinanceiro().getEmail());
			gerarNota.getTcDescricaoRps().setTomadorEndereco(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getResponsavelFinanceiro().getEndereco()));
			gerarNota.getTcDescricaoRps().setTomadorNumero("0");
			gerarNota.getTcDescricaoRps().setTomadorBairro(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getResponsavelFinanceiro().getSetor())); 
			gerarNota.getTcDescricaoRps().setTomadorComplemento(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getResponsavelFinanceiro().getComplemento()));
			gerarNota.getTcDescricaoRps().setTomadorCEP(notaFiscalSaidaVO.getResponsavelFinanceiro().getCEP().replace(".", "").replace("-", ""));
			gerarNota.getTcDescricaoRps().setTomadorCnpj(notaFiscalSaidaVO.getResponsavelFinanceiro().getCPF().replace("-", "").replace(".", "").trim());
			gerarNota.getTcDescricaoRps().setTomadorTipo(2);
			gerarNota.getTcDescricaoRps().setDescricaoNF("REFERENTE A SERVICO PRESTADO NO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()) + " AO  ALUNO(A) "+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getResponsavelFinanceiro().getNome().toUpperCase())+" DO CURSO "+Uteis.removerCaracteresEspeciais3(nomeCurso.toUpperCase())+" REFERENTE AO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()));			
			if (!Uteis.isAtributoPreenchido(notaFiscalSaidaVO.getResponsavelFinanceiro().getCidade().getCodigoIBGE())) {
				throw new Exception("Código IBGE da cidade do tomador não cadastrado! (" + notaFiscalSaidaVO.getResponsavelFinanceiro().getCidade().getNome() + ")");
			}
			gerarNota.getTcDescricaoRps().setTomadorCodCidade(notaFiscalSaidaVO.getResponsavelFinanceiro().getCidade().getCodigoIBGE());
			gerarNota.getTcDescricaoRps().setPis("");
			gerarNota.getTcDescricaoRps().setCofins("");
			gerarNota.getTcDescricaoRps().setInss("");
			gerarNota.getTcDescricaoRps().setCsll("");
		} else {
			gerarNota.getTcDescricaoRps().setTomadorRazao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getNome()));
			gerarNota.getTcDescricaoRps().setTomadorEmail(notaFiscalSaidaVO.getPessoaVO().getEmail());
			gerarNota.getTcDescricaoRps().setTomadorEndereco(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getEndereco()));
			gerarNota.getTcDescricaoRps().setTomadorNumero("0");
			gerarNota.getTcDescricaoRps().setTomadorBairro(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getSetor())); 
			gerarNota.getTcDescricaoRps().setTomadorComplemento(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getComplemento()));
			gerarNota.getTcDescricaoRps().setTomadorCEP(notaFiscalSaidaVO.getPessoaVO().getCEP().replace(".", "").replace("-", ""));
			gerarNota.getTcDescricaoRps().setTomadorCnpj(notaFiscalSaidaVO.getPessoaVO().getCPF().replace("-", "").replace(".", "").trim());
			gerarNota.getTcDescricaoRps().setTomadorTipo(2);
			gerarNota.getTcDescricaoRps().setDescricaoNF("REFERENTE A SERVICO PRESTADO NO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()) + " AO  ALUNO(A) "+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getNome().toUpperCase())+" DO CURSO "+Uteis.removerCaracteresEspeciais3(nomeCurso.toUpperCase())+" REFERENTE AO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()));			
			if (!Uteis.isAtributoPreenchido(notaFiscalSaidaVO.getPessoaVO().getCidade().getCodigoIBGE())) {
				throw new Exception("Código IBGE da cidade do tomador não cadastrado! (" + notaFiscalSaidaVO.getPessoaVO().getCidade().getNome() + ")");
			}
			gerarNota.getTcDescricaoRps().setTomadorCodCidade(notaFiscalSaidaVO.getPessoaVO().getCidade().getCodigoIBGE());
			gerarNota.getTcDescricaoRps().setPis("");
			gerarNota.getTcDescricaoRps().setCofins("");
			gerarNota.getTcDescricaoRps().setInss("");
			gerarNota.getTcDescricaoRps().setCsll("");
		}
		gerarNota.getTcDescricaoRps().setRpsNum(null);
		gerarNota.getTcDescricaoRps().setRpsSerie(null);
		gerarNota.getTcDescricaoRps().setRpsDia(null);
		gerarNota.getTcDescricaoRps().setRpsMes(null);
		gerarNota.getTcDescricaoRps().setRpsAno(null);
		gerarNota.getTcDescricaoRps().setNfseSubstituida(null);
		gerarNota.getTcDescricaoRps().setRpsSubstituido(null);
		return gerarNota;
	}

	@Override
	public String montarDescrimicaoNotaFiscalServico(String mensagem, Object[] parametros) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void consultarLoteRps(NotaFiscalSaidaVO notaFiscalSaidaVO,
			ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado,
			UsuarioVO usuarioVO) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelar(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo,
			UsuarioVO usuarioVO) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void imprimirDanfe(NotaFiscalSaidaVO notaFiscalSaidaVO,
			ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, UsuarioVO usuarioVO) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
}
