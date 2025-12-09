package webservice.nfse.belohorizonte;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
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
import negocio.comuns.utilitarias.faturamento.nfe.AmbienteNfeEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisNfe;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import webservice.nfse.NotaFiscalServicoEletronicaInterfaceFacade;

@Repository
@Scope(value = "singleton")
@Lazy
@Transactional(readOnly = true)
public class NotaFiscalServicoEletronicaBeloHorizonte extends ControleAcesso implements NotaFiscalServicoEletronicaInterfaceFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static String idEntidade;

	public NotaFiscalServicoEletronicaBeloHorizonte() throws Exception {
		super();
		setIdEntidade("NotaFiscalServicoEletronicaBeloHorizonte");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		NotaFiscalServicoEletronicaBeloHorizonte.idEntidade = idEntidade;
	}

	@Override
	public void enviarXmlEnvio(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado, UsuarioVO usuarioVO) throws Exception {
		try {
			Nfse builder =  Nfse.nfse();	
			LoteRps loteRps = montarXmlEnvio(notaFiscalSaidaVO, usuarioVO);
			String cnpj = notaFiscalSaidaVO.getUnidadeEnsinoVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim();
			notaFiscalSaidaVO.setXmlEnvio(builder.withLoteRps(loteRps).asGerarNFSEXML(notaFiscalSaidaVO, configuracaoRespositoriArquivo, "1"+ cnpj +Uteis.preencherComZerosPosicoesVagas(notaFiscalSaidaVO.getNumeroNota(), 16)));
			getFacadeFactory().getNotaFiscalSaidaFacade().gravarXmlEnvio(notaFiscalSaidaVO.getCodigo(), notaFiscalSaidaVO.getXmlEnvio(), usuarioVO);
			notaFiscalSaidaVO.setMensagemRetorno(ConexaoNFSE.conexaoNFSEBeloHorizonte(notaFiscalSaidaVO, notaFiscalSaidaVO.getWebServicesNFSEEnum().getTipoAcaoServicoNFSEEnum().name(), notaFiscalSaidaVO.getXmlEnvio(), UteisNfe.getCaminhoCertificado(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), configuracaoRespositoriArquivo), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaCertificado(), configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + "ca.jks", notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaUnidadeCertificadora(), usuarioVO));
			getFacadeFactory().getNotaFiscalSaidaFacade().alterarMensagemRetorno(notaFiscalSaidaVO, usuarioVO);
			notaFiscalSaidaVO.setDataStuacao(new Date());
			notaFiscalSaidaVO.setMensagemRetorno(notaFiscalSaidaVO.getMensagemRetorno().replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
			if (notaFiscalSaidaVO.getMensagemRetorno().contains("<CodigoVerificacao>")) {
				String numeroNota = notaFiscalSaidaVO.getMensagemRetorno().substring(notaFiscalSaidaVO.getMensagemRetorno().indexOf("<Numero>"), notaFiscalSaidaVO.getMensagemRetorno().indexOf("</Numero>"));
				numeroNota = numeroNota.replace("<Numero>", "");
				String codigoVerificacao = notaFiscalSaidaVO.getMensagemRetorno().substring(notaFiscalSaidaVO.getMensagemRetorno().lastIndexOf("<CodigoVerificacao>"), notaFiscalSaidaVO.getMensagemRetorno().indexOf("</CodigoVerificacao>"));
				codigoVerificacao = codigoVerificacao.replace("<CodigoVerificacao>", "");
				notaFiscalSaidaVO.setIdentificadorReceita(codigoVerificacao);
				notaFiscalSaidaVO.setRecibo(numeroNota);
				getFacadeFactory().getNotaFiscalSaidaFacade().gravarRecibo(notaFiscalSaidaVO.getCodigo(), notaFiscalSaidaVO.getRecibo(), usuarioVO);
				getFacadeFactory().getNotaFiscalSaidaFacade().gravarSituacaoEnvio(notaFiscalSaidaVO, SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor(), "", notaFiscalSaidaVO.getDataStuacao(), notaFiscalSaidaVO.getIdentificadorReceita(), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioVO);
				notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor());
				notaFiscalSaidaVO.setLinkAcesso("http://www.pbh.gov.br/bhissdigital/portal/index.php");
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
	
	public LoteRps montarXmlEnvio(NotaFiscalSaidaVO notaFiscalSaidaVO,UsuarioVO usuarioVO) throws Exception{
		String nomeCurso = getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(notaFiscalSaidaVO.getMatricula(), false, usuarioVO).getNome();
		String codigoTributacaoMunicipo = "";
		if (!notaFiscalSaidaVO.getUnidadeEnsinoVO().getCodigoTributacaoMunicipio().equals("")) {
			codigoTributacaoMunicipo = notaFiscalSaidaVO.getUnidadeEnsinoVO().getCodigoTributacaoMunicipio();
		} else {
			codigoTributacaoMunicipo = notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoTributacaoMunicipio();
		}
		String datasCompetenciasContaReceberNotaFiscal = getFacadeFactory().getNotaFiscalSaidaFacade().concatenarDatasCompetenciasContaReceberNotaFiscal(notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs());
		Servico servico = Nfse.servico()
				.withValorServicos(notaFiscalSaidaVO.getValorTotal())	
				.withValorDeducoes(0.0)
				.withValorPis(0.0)
				.withValorCofins(0.0)
				.withValorInss(0.0)
				.withValorIr(0.0)
				.withValorCsll(0.0)
				.issNaoRetido()
				.withValorIss(0.0)
				.withOutrasRetencoes(0.0)
				.withAliquota(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getIssqn())
				.withDescontoIncondicionado(0.0)
				.withDescontoCondicionado(0.0)
				.withCodigoTributacaoMunicipio(codigoTributacaoMunicipo)				
				.withCodigoMunicipio(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoMunicipio())
				.build();
		Tomador tomador = Nfse.tomador().build();
		if(notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor())) {
			tomador.setRazaoSocial(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getNome()));
			tomador.getEndereco().setEndereco(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getEndereco()));
			tomador.getEndereco().setNumero(0);
			tomador.getEndereco().setBairro(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getSetor()));
			tomador.getEndereco().setComplemento(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getComplemento()));
			tomador.getEndereco().setUf(notaFiscalSaidaVO.getFornecedorVO().getCidade().getEstado().getSigla());
			tomador.getEndereco().setCep(notaFiscalSaidaVO.getFornecedorVO().getCEP().replace(".", "").replace("-", ""));
			if (!Uteis.isAtributoPreenchido(notaFiscalSaidaVO.getFornecedorVO().getCidade().getCodigoIBGE())) {
				throw new Exception("Código IBGE da cidade do tomador não cadastrado! (" + notaFiscalSaidaVO.getFornecedorVO().getCidade().getNome() + ")");
			}
			tomador.getEndereco().setCodigoMunicipio(notaFiscalSaidaVO.getFornecedorVO().getCidade().getCodigoIBGE());
			servico.setDiscriminacao(Uteis.removerCaracteresEspeciais3(montarDescrimicaoNotaFiscalServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTextoPadraoDescriminacaoServicoNotaFiscal(), new Object[]{notaFiscalSaidaVO.getFornecedorVO().getNome(), nomeCurso.replace("&", "E"), notaFiscalSaidaVO.getTipoPessoa_apresentar(), datasCompetenciasContaReceberNotaFiscal})));			
			if(notaFiscalSaidaVO.getFornecedorVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
				tomador.getIdentificacaoTomador().getCpfCnpj().setCnpj(notaFiscalSaidaVO.getFornecedorVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim());
				servico.getValores().setValorPis(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() / 100)));
				servico.getValores().setValorCofins(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() / 100)));
				servico.getValores().setValorInss(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() / 100)));
				servico.getValores().setValorCsll(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() / 100)));
			} else {
				tomador.getIdentificacaoTomador().getCpfCnpj().setCpf(notaFiscalSaidaVO.getFornecedorVO().getCPF().replace("-", "").replace(".", "").trim());	
			}
		} else if(notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
			tomador.setRazaoSocial(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getNome()));
			tomador.getEndereco().setEndereco(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getEndereco()));
			tomador.getEndereco().setNumero(0);
			tomador.getEndereco().setBairro(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getSetor()));
			tomador.getEndereco().setComplemento(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getComplemento()));
			tomador.getEndereco().setUf(notaFiscalSaidaVO.getParceiroVO().getCidade().getEstado().getSigla());
			tomador.getEndereco().setCep(notaFiscalSaidaVO.getParceiroVO().getCEP().replace(".", "").replace("-", ""));
			if (!Uteis.isAtributoPreenchido(notaFiscalSaidaVO.getParceiroVO().getCidade().getCodigoIBGE())) {
				throw new Exception("Código IBGE da cidade do tomador não cadastrado! (" + notaFiscalSaidaVO.getParceiroVO().getCidade().getNome() + ")");
			}
			tomador.getEndereco().setCodigoMunicipio(notaFiscalSaidaVO.getParceiroVO().getCidade().getCodigoIBGE());
			servico.setDiscriminacao(Uteis.removerCaracteresEspeciais3(montarDescrimicaoNotaFiscalServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTextoPadraoDescriminacaoServicoNotaFiscal(), new Object[]{notaFiscalSaidaVO.getParceiroVO().getNome(), nomeCurso.replace("&", "E"), notaFiscalSaidaVO.getTipoPessoa_apresentar(), datasCompetenciasContaReceberNotaFiscal})));			
			if(notaFiscalSaidaVO.getParceiroVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
				tomador.getIdentificacaoTomador().getCpfCnpj().setCnpj(notaFiscalSaidaVO.getParceiroVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim());
				if (notaFiscalSaidaVO.getParceiroVO().getPossuiAliquotaEmissaoNotaEspecifica()) {
					if(notaFiscalSaidaVO.getParceiroVO().getIssqn() > 0) {
						servico.getValores().setAliquota(notaFiscalSaidaVO.getParceiroVO().getIssqn());
						servico.getValores().setValorIss(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getIssqn() / 100)));						
					}
					if(notaFiscalSaidaVO.getParceiroVO().getPis() > 0) {
						servico.getValores().setValorPis(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getPis() / 100)));
					}
					if(notaFiscalSaidaVO.getParceiroVO().getCofins() > 0) {
						servico.getValores().setValorCofins(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getCofins() / 100)));						
					}
					if(notaFiscalSaidaVO.getParceiroVO().getInss() > 0 ) {
						servico.getValores().setValorInss(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getInss() / 100)));						
					}
					if(notaFiscalSaidaVO.getParceiroVO().getCsll() > 0) {
						servico.getValores().setValorCsll(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getCsll() / 100)));
					}					
				} else {	
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getIssqn() > 0) {
						servico.getValores().setValorIss(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getIssqn() / 100)));						
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() > 0) {
						servico.getValores().setValorPis(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() / 100)));						
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() > 0) {
						servico.getValores().setValorCofins(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() / 100)));						
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() > 0) {
						servico.getValores().setValorInss(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() / 100)));						
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() > 0) {
						servico.getValores().setValorCsll(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() / 100)));						
					}
				}
			} else {
				tomador.getIdentificacaoTomador().getCpfCnpj().setCpf(notaFiscalSaidaVO.getParceiroVO().getCPF().replace("-", "").replace(".", "").trim());	
			}
		} else {
			tomador.setRazaoSocial(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getNome()));
			tomador.getEndereco().setEndereco(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getEndereco()));
			tomador.getEndereco().setNumero(0);
			tomador.getEndereco().setBairro(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getSetor())); 
			tomador.getEndereco().setComplemento(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getComplemento()));
			tomador.getEndereco().setUf(notaFiscalSaidaVO.getPessoaVO().getCidade().getEstado().getSigla());
			tomador.getEndereco().setCep(notaFiscalSaidaVO.getPessoaVO().getCEP().replace(".", "").replace("-", ""));
			tomador.getIdentificacaoTomador().getCpfCnpj().setCpf(notaFiscalSaidaVO.getPessoaVO().getCPF().replace("-", "").replace(".", "").trim());
			if (!Uteis.isAtributoPreenchido(notaFiscalSaidaVO.getPessoaVO().getCidade().getCodigoIBGE())) {
				throw new Exception("Código IBGE da cidade do tomador não cadastrado! (" + notaFiscalSaidaVO.getPessoaVO().getCidade().getNome() + ")");
			}
			tomador.getEndereco().setCodigoMunicipio(notaFiscalSaidaVO.getPessoaVO().getCidade().getCodigoIBGE());
			servico.setDiscriminacao(Uteis.removerCaracteresEspeciais3(montarDescrimicaoNotaFiscalServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTextoPadraoDescriminacaoServicoNotaFiscal(), new Object[]{notaFiscalSaidaVO.getPessoaVO().getNome(), nomeCurso.replace("&", "E"), notaFiscalSaidaVO.getTipoPessoa_apresentar(), datasCompetenciasContaReceberNotaFiscal})));			
		}
		servico.setItemListaServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoItemListaServico());
		servico.setCodigoMunicipio(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCidade().getCodigoIBGE());
		String cnpj = notaFiscalSaidaVO.getUnidadeEnsinoVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim();
		Prestador prestador = Nfse.prestador()
				.withCnpj(cnpj)	
				.withInscricaoMunicipal(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getUnidadeEnsinoVO().getInscMunicipal()))
				.build();
		Calendar dataCompetencia = Calendar.getInstance();
		dataCompetencia.setTime(new Date());
		Rps rps = Nfse.rps()
				.withNumero(notaFiscalSaidaVO.getNumero())				
				.tipoRps()
				.withSerie(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum().equals(AmbienteNfeEnum.HOMOLOGACAO) ? Serie.TESTE : Serie.NFE)
				.withServico(servico)				
				.withPrestador(prestador)
				.withTomador(tomador)				
				.withData(dataCompetencia)	
				.build();
		rps.getInfDeclaracaoPrestacaoServico().getIdentificacaoRps().setNumero(notaFiscalSaidaVO.getNumero());
		rps.getInfDeclaracaoPrestacaoServico().setId("rps:"+notaFiscalSaidaVO.getNumeroNota()+"ABCDZ");
		rps.getInfDeclaracaoPrestacaoServico().setOptanteSimplesNacional(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoRegimeTributarioEnum().equals(CodigoRegimeTributarioEnum.SIMPLES_NACIONAL) ? SimNao.SIM : SimNao.NAO);
		rps.getInfDeclaracaoPrestacaoServico().setIncentivadorCultural(SimNao.NAO);
		rps.getInfDeclaracaoPrestacaoServico().setStatus(Status.NORMAL);
		rps.getInfDeclaracaoPrestacaoServico().setNaturezaOperacao("1");
		//rps.getInfDeclaracaoPrestacaoServico().setRegimeEspecialTributacao(RegimeEspecialTributacao.ME_EPP_SIMPLES_NACIONAL);
		rps.getInfDeclaracaoPrestacaoServico().getIdentificacaoRps().setSerie(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum().equals(AmbienteNfeEnum.HOMOLOGACAO) ? Serie.TESTE : Serie.NFE);
		LoteRps loteRps = Nfse.loteNfse()
				.withCnpj(cnpj)
				.withIdLote("Lote"+(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum().equals(AmbienteNfeEnum.HOMOLOGACAO) ? Serie.TESTE : Serie.NFE)+notaFiscalSaidaVO.getNumeroNota()+notaFiscalSaidaVO.getNumero().toString()+"rps:"+notaFiscalSaidaVO.getNumeroNota()+"ABCDZ")
				.withInscricaoMunicipal(notaFiscalSaidaVO.getUnidadeEnsinoVO().getInscMunicipal())
				.withNumeroLote(notaFiscalSaidaVO.getNumero())
				.v1_00()
				.withQuantidade(1)
				.addRps(rps)
				.build();
		return  loteRps;
	}

	/**
	 * Nessa Sequencia
	 * NOME_ALUNO, NOME_CURSO, TIPO_PESSOA, COMPETENCIA
	 * @param mensagem
	 * @param parametros
	 * @return
	 * @throws Exception
	 */
	public String montarDescrimicaoNotaFiscalServico(String mensagem, final Object... parametros) throws Exception {
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), parametros[0].toString().trim());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), parametros[1].toString().trim().toUpperCase());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.TIPO_PESSOA.name(), parametros[2].toString().trim());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.COMPETENCIA.name(), parametros[3].toString().trim());
		return mensagem;
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
