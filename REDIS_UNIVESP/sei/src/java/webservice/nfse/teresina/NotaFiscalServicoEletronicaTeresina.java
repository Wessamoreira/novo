package webservice.nfse.teresina;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.faturamento.nfe.ConexaoNFSE;
import negocio.comuns.crm.enumerador.TipoEmpresaEnum;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;
import negocio.comuns.faturamento.nfe.enumeradores.SituacaoNotaFiscalSaidaEnum;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisNfe;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import webservice.nfse.NotaFiscalServicoEletronicaInterfaceFacade;
import webservice.nfse.teresina.ReqEnvioLoteRPS.Cabecalho;

@Repository
@Scope(value = "singleton")
@Lazy
@Transactional(readOnly = true)
public class NotaFiscalServicoEletronicaTeresina extends ControleAcesso implements NotaFiscalServicoEletronicaInterfaceFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static String idEntidade;

	public NotaFiscalServicoEletronicaTeresina() throws Exception {
		super();
		setIdEntidade("NotaFiscalServicoEletronicaTeresina");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		NotaFiscalServicoEletronicaTeresina.idEntidade = idEntidade;
	}

	@Override
	public void enviarXmlEnvio(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado, UsuarioVO usuarioVO) throws Exception {
		try {
			ReqEnvioLoteRPS reqEnvioLoteRPS = new ReqEnvioLoteRPS();
			reqEnvioLoteRPS = montarXmlEnvio(notaFiscalSaidaVO, usuarioVO);
			Nfse builder =  Nfse.nfse();		
			notaFiscalSaidaVO.setXmlEnvio(builder.withReqRPS(reqEnvioLoteRPS).asGerarNFSEXMLUberlandia("1"+ notaFiscalSaidaVO.getFornecedorVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim() +Uteis.preencherComZerosPosicoesVagas(notaFiscalSaidaVO.getNumeroNota(), 16), notaFiscalSaidaVO, configuracaoRespositoriArquivo));
			getFacadeFactory().getNotaFiscalSaidaFacade().gravarXmlEnvio(notaFiscalSaidaVO.getCodigo(), notaFiscalSaidaVO.getXmlEnvio(), usuarioVO);
			notaFiscalSaidaVO.setMensagemRetorno(ConexaoNFSE.conexaoNFSEUberlandia(notaFiscalSaidaVO, notaFiscalSaidaVO.getXmlEnvio(), UteisNfe.getCaminhoCertificado(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), configuracaoRespositoriArquivo), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaCertificado(), configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + "ca.jks", notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaUnidadeCertificadora(), usuarioVO));
			getFacadeFactory().getNotaFiscalSaidaFacade().alterarMensagemRetorno(notaFiscalSaidaVO, usuarioVO);
			notaFiscalSaidaVO.setDataStuacao(new Date());
			notaFiscalSaidaVO.setMensagemRetorno(notaFiscalSaidaVO.getMensagemRetorno().replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
			if (notaFiscalSaidaVO.getMensagemRetorno().contains("<Sucesso>true</Sucesso>")) {				
				String numeroLote = notaFiscalSaidaVO.getMensagemRetorno().substring(notaFiscalSaidaVO.getMensagemRetorno().indexOf("<NumeroLote>"), notaFiscalSaidaVO.getMensagemRetorno().indexOf("</NumeroLote>"));
				notaFiscalSaidaVO.setRecibo(numeroLote.replace("<NumeroLote>", ""));
				String codigoVerificacao = notaFiscalSaidaVO.getMensagemRetorno().substring(notaFiscalSaidaVO.getMensagemRetorno().lastIndexOf("<CodigoVerificacao>"), notaFiscalSaidaVO.getMensagemRetorno().indexOf("</CodigoVerificacao>"));
				codigoVerificacao = codigoVerificacao.replace("<CodigoVerificacao>", "");
				notaFiscalSaidaVO.setIdentificadorReceita(codigoVerificacao);
				getFacadeFactory().getNotaFiscalSaidaFacade().gravarRecibo(notaFiscalSaidaVO.getCodigo(), notaFiscalSaidaVO.getRecibo(), usuarioVO);
				getFacadeFactory().getNotaFiscalSaidaFacade().gravarSituacaoEnvio(notaFiscalSaidaVO, SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor(), "", notaFiscalSaidaVO.getDataStuacao(), notaFiscalSaidaVO.getIdentificadorReceita(), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioVO);
				notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor());
				notaFiscalSaidaVO.setLinkAcesso(notaFiscalSaidaVO.getWebServicesNFSEEnum().getValorApresentar().replace("{0}", codigoVerificacao).replace("{1}", notaFiscalSaidaVO.getWebServicesNFSEEnum().getCodigoCidadePadraoSiafi().toString()).replace("{2}", Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getUnidadeEnsinoVO().getInscMunicipal().replace(".", "").replace("-", ""))));
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
	
//	public void consultarLoteNFSEUberlandia(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado, UsuarioVO usuarioVO) throws Exception {
//		ReqConsultaLoteRPS reqConsultaLoteRPS = new ReqConsultaLoteRPS();
//		webservice.nfse.teresina.ReqConsultaLoteRPS.Cabecalho cabecalho = new webservice.nfse.teresina.ReqConsultaLoteRPS.Cabecalho();
//		cabecalho.setCodCidade(notaFiscalSaidaVO.getWebServicesNFSEEnum().getCodigoCidadePadraoSiafi());
//		cabecalho.setCPFCNPJRemetente(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim());
//		cabecalho.setVersao(1);
//		cabecalho.setNumeroLote(notaFiscalSaidaVO.getRecibo());
//		reqConsultaLoteRPS.setCabecalho(cabecalho);
//		Nfse builder =  Nfse.nfse();		
//		try {
//			String xmlConsulta = builder.withReqConsultaRPS(reqConsultaLoteRPS).asGerarNFSEXMLConsultaUberlandia("1"+ notaFiscalSaidaVO.getFornecedorVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim() +Uteis.preencherComZerosPosicoesVagas(notaFiscalSaidaVO.getNumeroNota(), 16), notaFiscalSaidaVO, configuracaoRespositoriArquivo);
//			String xmlRetornoConsulta = ConexaoNFSE.conexaoNFSEConsultaUberlandia(notaFiscalSaidaVO, xmlConsulta, UteisNfe.getCaminhoCertificado(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), configuracaoRespositoriArquivo), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaCertificado(), configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + "ca.jks", notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaUnidadeCertificadora(), usuarioVO);
//			xmlRetornoConsulta.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
//			if (xmlRetornoConsulta.contains("<Sucesso>true</Sucesso>")) {
//				String codigoVerificacao = notaFiscalSaidaVO.getMensagemRetorno().substring(notaFiscalSaidaVO.getMensagemRetorno().indexOf("<CodigoVerificacao>"), notaFiscalSaidaVO.getMensagemRetorno().indexOf("</CodigoVerificacao>"));
//				notaFiscalSaidaVO.setLinkAcesso(notaFiscalSaidaVO.getWebServicesNFSEEnum().getValorApresentar().replace("{0}", codigoVerificacao).replace("{1}", notaFiscalSaidaVO.getWebServicesNFSEEnum().getCodigoCidadePadraoSiafi().toString()).replace("{2}", Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getUnidadeEnsinoVO().getInscMunicipal().replace(".", "").replace("-", ""))));
//				notaFiscalSaidaVO.setIdentificadorReceita(codigoVerificacao);
//				getFacadeFactory().getNotaFiscalSaidaFacade().gravarRecibo(notaFiscalSaidaVO.getCodigo(), notaFiscalSaidaVO.getRecibo(), usuarioVO);
//				getFacadeFactory().getNotaFiscalSaidaFacade().gravarSituacaoEnvio(notaFiscalSaidaVO.getCodigo(), SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor(), "", notaFiscalSaidaVO.getDataStuacao(), notaFiscalSaidaVO.getIdentificadorReceita(), usuarioVO);
//				notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor());
//				//TODO
//			} else {
//				notaFiscalSaidaVO.setMensagemRetorno(xmlRetornoConsulta);
//				getFacadeFactory().getNotaFiscalSaidaFacade().gravarSituacaoEnvio(notaFiscalSaidaVO.getCodigo(), SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor(), "", notaFiscalSaidaVO.getDataStuacao(), "", usuarioVO);
//				getFacadeFactory().getNotaFiscalSaidaFacade().alterarMensagemRetorno(notaFiscalSaidaVO, usuarioVO);
//				notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
//				throw new Exception(xmlRetornoConsulta);
//			}
//		} catch (Exception e) {
//			progressBar.setStatus("NF-e n° " + notaFiscalSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
//			autorizado = e.getMessage();
//			notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
//			notaFiscalSaidaVO.setDataEmissao(new Date());
//			getFacadeFactory().getNotaFiscalSaidaFacade().gravarSituacaoEnvio(notaFiscalSaidaVO.getCodigo(), SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor(), notaFiscalSaidaVO.getProtocolo(), notaFiscalSaidaVO.getDataStuacao(), notaFiscalSaidaVO.getIdentificadorReceita(), usuarioVO);
//			notaFiscalSaidaVO.setMensagemRetorno(autorizado);
//			getFacadeFactory().getNotaFiscalSaidaFacade().alterarMensagemRetorno(notaFiscalSaidaVO, usuarioVO);
//			throw e;
//		}
//	}
	
	public ReqEnvioLoteRPS montarXmlEnvio(NotaFiscalSaidaVO notaFiscalSaidaVO,UsuarioVO usuarioVO) throws Exception{
		String nomeCurso = getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(notaFiscalSaidaVO.getMatricula(), false, usuarioVO).getNome();
		ReqEnvioLoteRPS reqEnvioLoteRPS = new ReqEnvioLoteRPS();
		Cabecalho cabecalho = new Cabecalho();
		cabecalho.setCodCidade(notaFiscalSaidaVO.getWebServicesNFSEEnum().getCodigoCidadePadraoSiafi());
		cabecalho.setCPFCNPJRemetente(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim());
		cabecalho.setRazaoSocialRemetente(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getUnidadeEnsinoVO().getRazaoSocial()));
		cabecalho.setTransacao(true);
		Calendar calendar = GregorianCalendar.getInstance();
		cabecalho.setDtInicio(DateConverter.getConverted2(calendar));
		cabecalho.setDtFim(DateConverter.getConverted2(calendar));
		cabecalho.setQtdRPS(1);
		cabecalho.setValorTotalServicos(new BigDecimal(notaFiscalSaidaVO.getValorTotal()));
		cabecalho.setValorTotalDeducoes(new BigDecimal(0.0));
		cabecalho.setVersao(1);
		cabecalho.setMetodoEnvio(TpMetodoEnvio.WS);
		cabecalho.setVersaoComponente("");
		reqEnvioLoteRPS.setCabecalho(cabecalho);
		TpLote tpLote = new TpLote();
		tpLote.setId("lote:"+notaFiscalSaidaVO.getNumero()+"ABCDZ");
		TpRPS tpRPS = new TpRPS();
		TpItens tpItens = new TpItens();
		tpRPS.setId("rps:"+notaFiscalSaidaVO.getNumero());
		String assinatura = "00000"+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getUnidadeEnsinoVO().getInscMunicipal().replace(".", "").replace("-", ""))+"NF 0000000"+notaFiscalSaidaVO.getNumeroNota()+""+Uteis.getDataAplicandoFormatacao(new Date(), "yyyyMMdd")+"T NS000000000000"+Uteis.getValorMonetarioParaIntegracao_SemPontoNemVirgula(notaFiscalSaidaVO.getValorTotal())+"0000000000000000"+notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoTributacaoMunicipio()+""+notaFiscalSaidaVO.getUnidadeEnsinoVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim();
		tpRPS.setAssinatura(DigestUtils.shaHex(assinatura));
		tpRPS.setInscricaoMunicipalPrestador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getUnidadeEnsinoVO().getInscMunicipal().replace(".", "").replace("-", "")));
		tpRPS.setRazaoSocialPrestador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getUnidadeEnsinoVO().getRazaoSocial()));
		tpRPS.setTipoRPS(TpTipoRPS.RPS);
		tpRPS.setSerieRPS(TpSerieRPS.NF);
		tpRPS.setNumeroRPS(notaFiscalSaidaVO.getNumero());
		tpRPS.setDataEmissaoRPS(DateConverter.getConverted(calendar));
		tpRPS.setSituacaoRPS(TpSituacaoRPS.N);
		tpRPS.setNumeroRPSSubstituido(0);
		tpRPS.setNumeroNFSeSubstituida(0);
		tpRPS.setDataEmissaoNFSeSubstituida("1900-01-01");
		tpRPS.setSeriePrestacao("99");
		tpRPS.setInscricaoMunicipalTomador("0000000");
		if(notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor())) {
			tpRPS.setRazaoSocialTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getNome()));
			tpRPS.setTipoLogradouroTomador("AV");
			tpRPS.setLogradouroTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getEndereco()));
			tpRPS.setNumeroEnderecoTomador("0");
			tpRPS.setComplementoEnderecoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getComplemento()));
			tpRPS.setTipoBairroTomador("");
			tpRPS.setBairroTomador("");
			tpRPS.setCidadeTomador(notaFiscalSaidaVO.getWebServicesNFSEEnum().getCodigoCidadePadraoSiafi().toString());
			tpRPS.setCidadeTomadorDescricao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getCidade().getNome()));
			tpRPS.setCEPTomador(notaFiscalSaidaVO.getFornecedorVO().getCEP().replace(".", "").replace("-", ""));
			tpRPS.setDDDTomador("031");
			tpRPS.setTelefoneTomador(notaFiscalSaidaVO.getFornecedorVO().getTelefones().replace("(", "").replace(")", "").replace("-", "").substring(0, 2));
			if(!notaFiscalSaidaVO.getFornecedorVO().getEmail().equals("")) {
				tpRPS.setEmailTomador(notaFiscalSaidaVO.getFornecedorVO().getEmail());				
			} else {
				tpRPS.setEmailTomador("-");				
			}
			tpRPS.setDescricaoRPS("REFERENTE A SERVICO PRESTADO NO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()) + " AO  FORNECEDOR(A) "+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getNome().toUpperCase())+" DO CURSO "+Uteis.removerCaracteresEspeciais3(nomeCurso.toUpperCase())+" REFERENTE AO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()));						
			tpItens.setDiscriminacaoServico("REFERENTE A SERVICO PRESTADO NO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()) + " AO  FORNECEDOR(A) "+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getNome().toUpperCase())+" DO CURSO "+Uteis.removerCaracteresEspeciais3(nomeCurso.toUpperCase())+" REFERENTE AO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()));
			if(notaFiscalSaidaVO.getFornecedorVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
				tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getFornecedorVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim());
				tpRPS.setValorPIS(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() / 100))));
				tpRPS.setValorCOFINS(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() / 100))));
				tpRPS.setValorINSS(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() / 100))));
				//tpRPS.setValorIR();
				tpRPS.setValorCSLL(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() / 100))));
			} else {
				tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getFornecedorVO().getCPF().replace("-", "").replace(".", "").trim());	
			}
		} else if(notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
			tpRPS.setRazaoSocialTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getNome()));
			tpRPS.setTipoLogradouroTomador("AV");
			tpRPS.setLogradouroTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getEndereco()));
			tpRPS.setNumeroEnderecoTomador("0");
			tpRPS.setComplementoEnderecoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getComplemento()));
			tpRPS.setTipoBairroTomador("");
			tpRPS.setBairroTomador("");
			tpRPS.setCidadeTomador(notaFiscalSaidaVO.getWebServicesNFSEEnum().getCodigoCidadePadraoSiafi().toString());
			tpRPS.setCidadeTomadorDescricao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getCidade().getNome()));
			tpRPS.setCEPTomador(notaFiscalSaidaVO.getParceiroVO().getCEP().replace(".", "").replace("-", ""));
			tpRPS.setDDDTomador("031");
			tpRPS.setTelefoneTomador(notaFiscalSaidaVO.getParceiroVO().getTelefones().replace("(", "").replace(")", "").replace("-", "").substring(0, 2));
			if(!notaFiscalSaidaVO.getParceiroVO().getEmail().equals("")) {
				tpRPS.setEmailTomador(notaFiscalSaidaVO.getParceiroVO().getEmail());				
			} else {
				tpRPS.setEmailTomador("-");				
			}
			tpRPS.setDescricaoRPS("REFERENTE A SERVICO PRESTADO NO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()) + " AO  FORNECEDOR(A) "+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getNome().toUpperCase())+" DO CURSO "+Uteis.removerCaracteresEspeciais3(nomeCurso.toUpperCase())+" REFERENTE AO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()));						
			tpItens.setDiscriminacaoServico("REFERENTE A SERVICO PRESTADO NO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()) + " AO  FORNECEDOR(A) "+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getNome().toUpperCase())+" DO CURSO "+Uteis.removerCaracteresEspeciais3(nomeCurso.toUpperCase())+" REFERENTE AO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()));
			if(notaFiscalSaidaVO.getParceiroVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
				tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getParceiroVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim());
				if (notaFiscalSaidaVO.getParceiroVO().getPossuiAliquotaEmissaoNotaEspecifica()) {
					if(notaFiscalSaidaVO.getParceiroVO().getPis() > 0) {
						tpRPS.setValorPIS(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getPis() / 100))));
					}
					if(notaFiscalSaidaVO.getParceiroVO().getCofins() > 0) {
						tpRPS.setValorCOFINS(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getCofins() / 100))));
					}
					if(notaFiscalSaidaVO.getParceiroVO().getInss() > 0 ) {
						tpRPS.setValorINSS(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getInss() / 100))));
					}
					if(notaFiscalSaidaVO.getParceiroVO().getCsll() > 0) {
						tpRPS.setValorCSLL(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getCsll() / 100))));
					}					
				} else {	
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() > 0) {
						tpRPS.setValorPIS(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() / 100))));
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() > 0) {
						tpRPS.setValorCOFINS(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() / 100))));
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() > 0) {
						tpRPS.setValorINSS(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() / 100))));
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() > 0) {
						tpRPS.setValorCSLL(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() / 100))));
					}
				}
			} else {
				tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getParceiroVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim());
			}
		} if (notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor())) {
			tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getResponsavelFinanceiro().getCPF().replace("-", "").replace("/", "").replace(".", "").trim());
			tpRPS.setRazaoSocialTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getResponsavelFinanceiro().getNome()));
			tpRPS.setTipoLogradouroTomador("AV");
			tpRPS.setLogradouroTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getResponsavelFinanceiro().getEndereco()));
			tpRPS.setNumeroEnderecoTomador("0");
			tpRPS.setComplementoEnderecoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getResponsavelFinanceiro().getComplemento()));
			tpRPS.setTipoBairroTomador("");
			tpRPS.setBairroTomador("");
			tpRPS.setCidadeTomador(notaFiscalSaidaVO.getWebServicesNFSEEnum().getCodigoCidadePadraoSiafi().toString());
			tpRPS.setCidadeTomadorDescricao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getResponsavelFinanceiro().getCidade().getNome()));
			tpRPS.setCEPTomador(notaFiscalSaidaVO.getResponsavelFinanceiro().getCEP().replace(".", "").replace("-", ""));
			tpRPS.setDDDTomador("031");
			tpRPS.setTelefoneTomador("92145485");
			if(!notaFiscalSaidaVO.getResponsavelFinanceiro().getEmail().equals("")) {
				tpRPS.setEmailTomador(notaFiscalSaidaVO.getResponsavelFinanceiro().getEmail());				
			} else {
				tpRPS.setEmailTomador("-");				
			}
			tpRPS.setDescricaoRPS("REFERENTE A SERVICO PRESTADO NO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()) + " AO  FORNECEDOR(A) "+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getResponsavelFinanceiro().getNome().toUpperCase())+" DO CURSO "+Uteis.removerCaracteresEspeciais3(nomeCurso.toUpperCase())+" REFERENTE AO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()));						
			tpItens.setDiscriminacaoServico("REFERENTE A SERVICO PRESTADO NO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()) + " AO  FORNECEDOR(A) "+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getResponsavelFinanceiro().getNome().toUpperCase())+" DO CURSO "+Uteis.removerCaracteresEspeciais3(nomeCurso.toUpperCase())+" REFERENTE AO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()));
		} else {
			tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getPessoaVO().getCPF().replace("-", "").replace("/", "").replace(".", "").trim());
			tpRPS.setRazaoSocialTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getNome()));
			tpRPS.setTipoLogradouroTomador("AV");
			tpRPS.setLogradouroTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getEndereco()));
			tpRPS.setNumeroEnderecoTomador("0");
			tpRPS.setComplementoEnderecoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getComplemento()));
			tpRPS.setTipoBairroTomador("");
			tpRPS.setBairroTomador("");
			tpRPS.setCidadeTomador(notaFiscalSaidaVO.getWebServicesNFSEEnum().getCodigoCidadePadraoSiafi().toString());
			tpRPS.setCidadeTomadorDescricao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getCidade().getNome()));
			tpRPS.setCEPTomador(notaFiscalSaidaVO.getPessoaVO().getCEP().replace(".", "").replace("-", ""));
			tpRPS.setDDDTomador("031");
			tpRPS.setTelefoneTomador("92145485");
			if(!notaFiscalSaidaVO.getPessoaVO().getEmail().equals("")) {
				tpRPS.setEmailTomador(notaFiscalSaidaVO.getPessoaVO().getEmail());				
			} else {
				tpRPS.setEmailTomador("-");				
			}
			tpRPS.setDescricaoRPS("REFERENTE A SERVICO PRESTADO NO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()) + " AO  FORNECEDOR(A) "+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getNome().toUpperCase())+" DO CURSO "+Uteis.removerCaracteresEspeciais3(nomeCurso.toUpperCase())+" REFERENTE AO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()));						
			tpItens.setDiscriminacaoServico("REFERENTE A SERVICO PRESTADO NO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()) + " AO  FORNECEDOR(A) "+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getNome().toUpperCase())+" DO CURSO "+Uteis.removerCaracteresEspeciais3(nomeCurso.toUpperCase())+" REFERENTE AO MES DE "+Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()));
		}
		tpRPS.setCodigoAtividade(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoTributacaoMunicipio());
		tpRPS.setAliquotaAtividade(new BigDecimal(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getIss()));
		tpRPS.setTipoRecolhimento(TpTipoRecolhimento.A);
		tpRPS.setMunicipioPrestacao(notaFiscalSaidaVO.getWebServicesNFSEEnum().getCodigoCidadePadraoSiafi().toString());
		tpRPS.setMunicipioPrestacaoDescricao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCidade().getNome()));
		tpRPS.setOperacao(TpOperacao.A);
		tpRPS.setTributacao(TpTributacao.T);
		tpRPS.setValorPIS(new BigDecimal(0.0));
		tpRPS.setValorCOFINS(new BigDecimal(0.0));
		tpRPS.setValorINSS(new BigDecimal(0.0));
		tpRPS.setValorIR(new BigDecimal(0.0));
		tpRPS.setValorCSLL(new BigDecimal(0.0));
		tpRPS.setAliquotaPIS(new BigDecimal(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis()));
		tpRPS.setAliquotaCOFINS(new BigDecimal(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins()));
		tpRPS.setAliquotaINSS(new BigDecimal(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss()));
		tpRPS.setAliquotaIR(new BigDecimal(0.0));
		tpRPS.setAliquotaCSLL(new BigDecimal(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll()));
		tpRPS.setDDDPrestador("031");
		tpRPS.setTelefonePrestador(notaFiscalSaidaVO.getUnidadeEnsinoVO().getTelComercial1().replace("(", "").replace(")", "").replace("-", "").substring(0, 2));
		tpRPS.setMotCancelamento("");
		tpItens.setQuantidade(new BigDecimal(1));
		tpItens.setValorUnitario(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal())));
		tpItens.setValorTotal(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal())));
		tpItens.setTributavel(TpItemTributavel.S);
		TpListaItens tpListaItens = new TpListaItens();
		tpListaItens.setItem(tpItens);
		tpRPS.setItens(tpListaItens);
		tpLote.setRps(tpRPS);
		reqEnvioLoteRPS.setLote(tpLote);
		return  reqEnvioLoteRPS;
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
