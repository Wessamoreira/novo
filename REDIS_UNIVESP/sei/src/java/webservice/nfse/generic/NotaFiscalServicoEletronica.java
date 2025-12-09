package webservice.nfse.generic;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.LoginControle;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.crm.enumerador.TipoEmpresaEnum;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaServicoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;
import negocio.comuns.faturamento.nfe.enumeradores.CodigoRegimeTributarioEnum;
import negocio.comuns.faturamento.nfe.enumeradores.SituacaoNotaFiscalSaidaEnum;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.AmbienteNfeEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import webservice.nfse.NotaFiscalServicoEletronicaInterfaceFacade;

@Repository
@Scope(value = "singleton")
@Lazy
@Transactional(readOnly = true)
public class NotaFiscalServicoEletronica extends ControleAcesso implements NotaFiscalServicoEletronicaInterfaceFacade {
	private static final long serialVersionUID = 1L;
	
	protected static String idEntidade;

	public NotaFiscalServicoEletronica() throws Exception {
		super();
		setIdEntidade("NotaFiscalServicoEletronica");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		NotaFiscalServicoEletronica.idEntidade = idEntidade;
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void imprimirDanfe(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, UsuarioVO usuarioVO) throws Exception {
		NFSeVO nfseVO = montarNFSeVO(notaFiscalSaidaVO, configuracaoRespositoriArquivo, usuarioVO);
		ClienteNotaFiscalServicoEletronica cliente = new ClienteNotaFiscalServicoEletronica();
		nfseVO.setNumeroNFSe(notaFiscalSaidaVO.getNumero());
		int inicio = notaFiscalSaidaVO.getMensagemRetorno().indexOf("<CodigoVerificacao>") + 19;
		int fim = notaFiscalSaidaVO.getMensagemRetorno().indexOf("</CodigoVerificacao>");
		String cv = notaFiscalSaidaVO.getMensagemRetorno().substring(inicio, fim);
		nfseVO.setCodigoVerificacao(cv);
		NFSeVO retorno = cliente.imprimir(nfseVO, getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesWebserviceNFe().getUrlWebserviceNFSe());
		if (retorno.getUrlVisualizacao() != null) {
			notaFiscalSaidaVO.setLinkAcesso(retorno.getUrlVisualizacao());
			getFacadeFactory().getNotaFiscalSaidaFacade().gravarLinkAcesso(notaFiscalSaidaVO, usuarioVO);
		}
	}
	
	@Override
	public void enviarXmlEnvio(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado, UsuarioVO usuarioVO) throws Exception {
		try {
			NFSeVO nfseVO = montarNFSeVO(notaFiscalSaidaVO, configuracaoRespositoriArquivo, usuarioVO);
			ClienteNotaFiscalServicoEletronica cliente = new ClienteNotaFiscalServicoEletronica();
			NFSeVO retorno = cliente.gerarNFSe(nfseVO, getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesWebserviceNFe().getUrlWebserviceNFSe());
			
			notaFiscalSaidaVO.setXmlEnvio(retorno.getEnvioXML());
			notaFiscalSaidaVO.setMensagemRetorno(retorno.getRetornoXML());
			notaFiscalSaidaVO.setDataEmissao(new Date());
			notaFiscalSaidaVO.setDataStuacao(new Date());
			StringBuilder mensagens = new StringBuilder();
			if(retorno.getMensagens() != null) {
				for (String msg : retorno.getMensagens()) {
					if (!msg.trim().isEmpty()) {
						mensagens.append(msg + " ");
					}
				}
			} else {
				mensagens.append("Não foi possível obter a mensagem de retorno.");
			}
			StringBuilder mensagensAlerta = new StringBuilder();
			if(retorno.getMensagensAlerta() != null) {
				for (String msg : retorno.getMensagensAlerta()) {
					if (!msg.trim().isEmpty()) {
						mensagensAlerta.append(msg + " ");
					}
				}
			}
			if (mensagens.toString().trim().isEmpty() &&
					retorno.getNumeroNFSe() > 0l &&
					retorno.getCodigoVerificacao() != null &&
					!retorno.getCodigoVerificacao().trim().isEmpty()) {
				notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor());
				notaFiscalSaidaVO.setNumero(retorno.getNumeroNFSe());
				notaFiscalSaidaVO.setProtocolo(retorno.getProtocolo());
				notaFiscalSaidaVO.setLinkAcesso(retorno.getUrlVisualizacao());
				notaFiscalSaidaVO.setMensagemRetorno(mensagensAlerta.toString());
				progressBar.setStatus("NF-e n° " + notaFiscalSaidaVO.getNumeroNota() + " AUTORIZADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
			} else {
				notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.REJEITADA.getValor());
				notaFiscalSaidaVO.setMensagemRetorno(mensagens.toString() + " " + mensagensAlerta.toString());
				progressBar.setStatus("NF-e n° " + notaFiscalSaidaVO.getNumeroNota() + " REJEITADA ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
			}
			getFacadeFactory().getNotaFiscalSaidaFacade().gravarEnvioNFSeWebservice(notaFiscalSaidaVO, usuarioVO);
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
	
	@Override
	public void cancelar(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, UsuarioVO usuarioVO) throws Exception {
		try {
			NFSeVO nfseVO = montarNFSeVO(notaFiscalSaidaVO, configuracaoRespositoriArquivo, usuarioVO);
			nfseVO.setJustificativaCancelamento(JustificativaCancelamentoEnum.SERVICO_NAO_PRESTADO);
			ClienteNotaFiscalServicoEletronica cliente = new ClienteNotaFiscalServicoEletronica();
			NFSeVO retorno = cliente.cancelar(nfseVO, getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesWebserviceNFe().getUrlWebserviceNFSe());
			StringBuilder mensagens = new StringBuilder();
			for (String msg : retorno.getMensagens()) {
				if (!msg.trim().isEmpty()) {
					mensagens.append(msg + " ");
				}
			}
			if (mensagens.toString().trim().isEmpty() &&
					retorno.getNumeroNFSe() > 0l &&
					retorno.getCodigoVerificacao() != null &&
					!retorno.getCodigoVerificacao().trim().isEmpty() &&
					retorno.getDataCancelamento() != null) {
				notaFiscalSaidaVO.setXmlCancelamento(retorno.getCancelamentoXML());
				notaFiscalSaidaVO.setProtocoloCancelamento(retorno.getProtocolo());
				notaFiscalSaidaVO.setDataStuacao(retorno.getDataCancelamento());
				notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.CANCELADA.getValor());
				getFacadeFactory().getNotaFiscalSaidaFacade().gravarSituacaoCancelamento(notaFiscalSaidaVO.getCodigo(), SituacaoNotaFiscalSaidaEnum.CANCELADA.getValor(), notaFiscalSaidaVO.getProtocoloCancelamento(), notaFiscalSaidaVO.getXmlCancelamento(), notaFiscalSaidaVO.getMotivoCancelamento(), usuarioVO);
				notaFiscalSaidaVO.setMensagemRetorno(retorno.getRetornoXML());
				getFacadeFactory().getNotaFiscalSaidaFacade().alterarMensagemRetorno(notaFiscalSaidaVO, usuarioVO);
				getFacadeFactory().getNotaFiscalSaidaFacade().gravarLogContasVinculadas(notaFiscalSaidaVO, usuarioVO);
				for (NotaFiscalSaidaServicoVO nfssVO : notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs()) {
					getFacadeFactory().getContaReceberRecebimentoFacade().removerVinculoNotaFiscalSaidaServicoContaReceberRecebimento(nfssVO.getCodigo(), usuarioVO);
					getFacadeFactory().getContaReceberFacade().removerVinculoNotaFiscalSaidaServicoContaReceber(nfssVO.getCodigo(), usuarioVO);
				}
//				notaFiscalSaidaVO.setLinkAcesso(retorno.getUrlVisualizacao());
			} else {
				notaFiscalSaidaVO.setMensagemRetorno(mensagens.toString());
			}
		} catch (Exception e) {
			notaFiscalSaidaVO.setMensagemRetorno(e.getMessage());
			throw e;
		}
	}
	
	private NFSeVO montarNFSeVO(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, UsuarioVO usuarioVO) throws Exception {
		NFSeVO nfseVO = new NFSeVO();
		
		// Certificado
	    String caminhoCertificado = configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() +
	    		File.separator + notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getArquivoVO().getPastaBaseArquivo() +
	    		File.separator + notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getArquivoVO().getNome();
		nfseVO.setCertificatePath(caminhoCertificado);
		nfseVO.setCertificatePassword(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getSenhaCertificado());
		if (AmbienteNfeEnum.PRODUCAO.equals(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum())) {
			nfseVO.setAmbiente(AmbienteEnum.PRODUCAO);
		} else {
			nfseVO.setAmbiente(AmbienteEnum.HOMOLOGACAO);
		}
		
		// Prestador
		nfseVO.setCnpjPrestador(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCNPJ());
		nfseVO.setInscMunicipal(notaFiscalSaidaVO.getUnidadeEnsinoVO().getInscMunicipal());
		CidadeVO cidade = notaFiscalSaidaVO.getUnidadeEnsinoVO().getCidadeNFSe();
		nfseVO.setCodigoIBGEMunicPrest(Integer.valueOf(cidade.getCodigoIBGE()));

		if ("1716109".equals(cidade.getCodigoIBGE())) { // Paraiso do Tocantins TO
			nfseVO.setCodigoBacenPaisPrest(1058);
		} else if ("2111300".equals(cidade.getCodigoIBGE())) { // São Luís MA
			nfseVO.setToken("A96FC3DDFB057BF128FB183732E5B06C");
			nfseVO.setCodigoSIAFIMunicPrest(921);
			nfseVO.setCodigoSIAFIMunicTomador(921);
			nfseVO.setDescricaoMunicPrest(cidade.getNome());
			nfseVO.setRazSocialPrestador(notaFiscalSaidaVO.getUnidadeEnsinoVO().getRazaoSocial());
			nfseVO.setTransacao(true);
			nfseVO.setVersaoSchema("1");
			nfseVO.setTipoRecolhimento(TipoRecolhimentoEnum.A_RECEBER);
			nfseVO.setOperacao(OperacaoEnum.SEM_DEDUCAO);
			nfseVO.setTributacao(TributacaoEnum.TRIBUTAVEL);
			nfseVO.setAliquotaPis(new BigDecimal(notaFiscalSaidaVO.getAliquotaPis()));
			nfseVO.setAliquotaCofins(new BigDecimal(notaFiscalSaidaVO.getAliquotaCofins()));
			nfseVO.setAliquotaInss(new BigDecimal(notaFiscalSaidaVO.getAliquotaInss()));
			nfseVO.setAliquotaIr(new BigDecimal(notaFiscalSaidaVO.getAliquotaIr()));
			nfseVO.setAliquotaCsll(new BigDecimal(notaFiscalSaidaVO.getAliquotaCsll()));
			nfseVO.setQuantidade(new BigDecimal(1));
			nfseVO.setValorUnitario(new BigDecimal(notaFiscalSaidaVO.getValorTotal()));
			nfseVO.setValorTotal(new BigDecimal(notaFiscalSaidaVO.getValorTotal()));
			nfseVO.setTributavel(TributavelEnum.ITEM_TRIBUTAVEL);
		} else if ("3170206".equals(cidade.getCodigoIBGE())) { // Uberlândia MG
			nfseVO.setCodigoSIAFIMunicPrest(5403);
			nfseVO.setCodigoSIAFIMunicTomador(5403);
			nfseVO.setDescricaoMunicPrest(cidade.getNome());
			nfseVO.setRazSocialPrestador(notaFiscalSaidaVO.getUnidadeEnsinoVO().getRazaoSocial());
			nfseVO.setTransacao(true);
			nfseVO.setVersaoSchema("1");
			nfseVO.setTipoRecolhimento(TipoRecolhimentoEnum.A_RECEBER);
			nfseVO.setOperacao(OperacaoEnum.SEM_DEDUCAO);
			nfseVO.setTributacao(TributacaoEnum.TRIBUTAVEL);
			nfseVO.setAliquotaPis(new BigDecimal(notaFiscalSaidaVO.getAliquotaPis()));
			nfseVO.setAliquotaCofins(new BigDecimal(notaFiscalSaidaVO.getAliquotaCofins()));
			nfseVO.setAliquotaInss(new BigDecimal(notaFiscalSaidaVO.getAliquotaInss()));
			nfseVO.setAliquotaIr(new BigDecimal(notaFiscalSaidaVO.getAliquotaIr()));
			nfseVO.setAliquotaCsll(new BigDecimal(notaFiscalSaidaVO.getAliquotaCsll()));
			nfseVO.setQuantidade(new BigDecimal(1));
			nfseVO.setValorUnitario(new BigDecimal(notaFiscalSaidaVO.getValorTotal()));
			nfseVO.setValorTotal(new BigDecimal(notaFiscalSaidaVO.getValorTotal()));
			nfseVO.setTributavel(TributavelEnum.ITEM_TRIBUTAVEL);
		} else if ("3106200".equals(cidade.getCodigoIBGE())) { // Belo Horizonte MG
			nfseVO.setCodigoBacenPaisPrest(1058);
			nfseVO.setDescricaoMunicPrest(cidade.getNome());
			nfseVO.setRazSocialPrestador(notaFiscalSaidaVO.getUnidadeEnsinoVO().getRazaoSocial());
		} else if ("3550308".equals(cidade.getCodigoIBGE())) { // São Paulo SP
			nfseVO.setVersaoSchema("1");
			nfseVO.setTributacao(TributacaoEnum.TRIBUTAVEL);
			nfseVO.setValorDeducoes(BigDecimal.ZERO);
			nfseVO.setPercentualCargaTributaria(new BigDecimal(notaFiscalSaidaVO.getPercentualCargaTributaria()));
			nfseVO.setValorCargaTributaria(new BigDecimal(notaFiscalSaidaVO.getValorCargaTributaria()));
			nfseVO.setFonteCargaTributaria(notaFiscalSaidaVO.getFonteCargaTributaria());
		}
		
		// Emissão
		if (Uteis.isAtributoPreenchido(notaFiscalSaidaVO.getLote())) {
			nfseVO.setNumeroLote(Integer.valueOf(notaFiscalSaidaVO.getLote()));
		} else {
			nfseVO.setNumeroLote(0);
		}
		nfseVO.setNumeroRPS(notaFiscalSaidaVO.getNumeroRPS());
		nfseVO.setSerie(notaFiscalSaidaVO.getSerie());
		nfseVO.setTipoRPS(TipoRPSEnum.RECIBO_PROVISORIO_DE_SERVICOS);
		nfseVO.setStatus(StatusRPSEnum.NORMAL);
		if (!"3550308".equals(cidade.getCodigoIBGE())) { // São Paulo SP
			if (!notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoTributacaoMunicipio().trim().isEmpty()) {
				nfseVO.setCodigoTributacaoMunicipio(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoTributacaoMunicipio().trim());
			} else {
				throw new Exception("Código Tributação Município do prestador não cadastrado!");
			}
		}
		Calendar dataHoraEmissao = Calendar.getInstance();
		if (Uteis.isAtributoPreenchido(notaFiscalSaidaVO.getDataEmissaoRetroativa())) {
			dataHoraEmissao.setTime(notaFiscalSaidaVO.getDataEmissaoRetroativa());
		} else {
			dataHoraEmissao.setTime(new Date());
		}
		dataHoraEmissao.add(Calendar.HOUR, notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getFusoHorario());
		nfseVO.setDataHoraEmissao(dataHoraEmissao.getTime());
		nfseVO.setQuantidadeRps(1);
		nfseVO.setNaturezaOperacao(notaFiscalSaidaVO.getNaturezaOperacaoEnum()); // NaturezaOperacaoEnum.TRIBUTACAO_NO_MUNICIPIO
		if (!CodigoRegimeTributarioEnum.REGIME_NORMAL.equals(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoRegimeTributarioEnum())) {
			nfseVO.setOptanteSimplesNacional(true);
		} else {
			nfseVO.setOptanteSimplesNacional(false);
		}
		nfseVO.setIncentivadorCultural(notaFiscalSaidaVO.getIsIncentivadorCultural());
		nfseVO.setRegimeEspecialTributacao(notaFiscalSaidaVO.getRegimeEspecialTributacaoEnum());
		String codigoCnae = notaFiscalSaidaVO.getCodigoCNAE().replace("-", "").replace("/", "").replace(".", "").trim();
		if (!codigoCnae.isEmpty()) {
			nfseVO.setCnae(Integer.valueOf(codigoCnae));
		}
		nfseVO.setDataHoraCompetencia(new Date());
		if ("1716109".equals(cidade.getCodigoIBGE()) || // Paraiso do Tocantins TO
				"2919553".equals(cidade.getCodigoIBGE())) { // Luis Eduardo Magalhaes BA
			nfseVO.setExigibilidadeISS(ExigibilidadeISSEnum.EXIGIVEL);
			if (AmbienteNfeEnum.PRODUCAO.equals(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAmbienteNfeEnum())) {
				nfseVO.setCodigoIBGEMunicIncidencia(Integer.valueOf(cidade.getCodigoIBGE()));
			} else {
				nfseVO.setCodigoIBGEMunicIncidencia(9999999);
			}
		}
		
		// Valores
		if ("3106200".equals(cidade.getCodigoIBGE())) { // Belo Horizonte MG
			nfseVO.setListaServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoItemListaServico());
		} else {
			nfseVO.setListaServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoItemListaServico().replace(".",  "").trim());
		}
		nfseVO.setIssRetido(notaFiscalSaidaVO.getIssRetido());
//		if ("2919553".equals(cidade.getCodigoIBGE())) { // Luis Eduardo Magalhaes BA
//			nfseVO.setAliquota(new BigDecimal(notaFiscalSaidaVO.getAliquotaIssqn()/100.0));
//		} else {
			nfseVO.setAliquota(new BigDecimal(notaFiscalSaidaVO.getAliquotaIssqn()));
//		}
//		nfseVO.setValorIss(new BigDecimal(notaFiscalSaidaVO.getTotalIssqn()));
		nfseVO.setValorIss(new BigDecimal(notaFiscalSaidaVO.getValorTotal()*(notaFiscalSaidaVO.getAliquotaIssqn()/100)));
		nfseVO.setValorPis(new BigDecimal(notaFiscalSaidaVO.getValorTotalPIS()));
		nfseVO.setValorCofins(new BigDecimal(notaFiscalSaidaVO.getValorTotalCOFINS()));
		nfseVO.setValorInss(new BigDecimal(notaFiscalSaidaVO.getValorTotalINSS()));
		nfseVO.setValorIr(new BigDecimal(notaFiscalSaidaVO.getValorTotalIRRF()));
		nfseVO.setValorCsll(new BigDecimal(notaFiscalSaidaVO.getValorTotalCSLL()));
		nfseVO.setValorServicos(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal())));
		nfseVO.setValorBaseCalc(new BigDecimal(Uteis.arrendondarForcando2CadasDecimais(notaFiscalSaidaVO.getValorTotal())));
		nfseVO.setValorLiquido(new BigDecimal(notaFiscalSaidaVO.getValorLiquido()));
		nfseVO.setValorOutras(BigDecimal.ZERO);
		nfseVO.setValorDescontoIncondicionado(BigDecimal.ZERO);
		nfseVO.setValorDescontoCondicionado(BigDecimal.ZERO);
		
		String discriminacao = Uteis.removerCaracteresEspeciais3(montarDescrimicaoNotaFiscalServico(
			notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTextoPadraoDescriminacaoServicoNotaFiscal(),
			new Object[] {notaFiscalSaidaVO.getFornecedorVO().getNome(),
				getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(notaFiscalSaidaVO.getMatricula(), false,
					usuarioVO).getNome().replace("&", "E"),	notaFiscalSaidaVO.getTipoPessoa(),
					Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()), notaFiscalSaidaVO.getNomesConvenios()}));
		if (discriminacao.length() > 80) {
			nfseVO.setDiscriminacao(discriminacao.substring(0, 80));
		} else {
			nfseVO.setDiscriminacao(discriminacao);
		}
		
		// Tomador
		if (!notaFiscalSaidaVO.getTelefone().trim().isEmpty()) {
			String telefone = notaFiscalSaidaVO.getTelefone().replace("(", "").replace(")", "").replace("-", "").replace(" ", "").trim();
			if (telefone.length() > 7 && telefone.length() < 12) {
				nfseVO.setFoneTomador(telefone);
			}
		}
		nfseVO.setCodigoBacenPaisTomador(1058);
		if (notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor())) {
			if(notaFiscalSaidaVO.getFornecedorVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
				nfseVO.setCpfCnpjTomador(Uteis.removeCaractersEspeciais(notaFiscalSaidaVO.getFornecedorVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim()));
				nfseVO.setInscMunicTomador(Uteis.removeCaractersEspeciais(notaFiscalSaidaVO.getFornecedorVO().getInscMunicipal().replace("-", "").replace(".", "").trim()));
			} else {
				nfseVO.setCpfCnpjTomador(Uteis.removeCaractersEspeciais(notaFiscalSaidaVO.getFornecedorVO().getCPF().replace("-", "").replace(".", "").trim()));
			}
			nfseVO.setRazSocialTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getNome()));
			nfseVO.setEnderecoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getEndereco()));
			nfseVO.setComplementoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getComplemento()));
			if (notaFiscalSaidaVO.getFornecedorVO().getNumero().replace("ª", "").replace("º", "").isEmpty()) {
				nfseVO.setNumEndTomador("SN");
			} else {
				nfseVO.setNumEndTomador(notaFiscalSaidaVO.getFornecedorVO().getNumero().replace("ª", "").replace("º", ""));
			}
			nfseVO.setBairroTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getSetor()));
			nfseVO.setCepTomador(Integer.valueOf(notaFiscalSaidaVO.getFornecedorVO().getCEP().replace(".", "").replace("-", "")));
			if (Uteis.getValidaEmail(notaFiscalSaidaVO.getFornecedorVO().getEmail().trim())) {
				nfseVO.setEmailTomador(notaFiscalSaidaVO.getFornecedorVO().getEmail().trim());
			}
			if (notaFiscalSaidaVO.getFornecedorVO().getCidade().getCodigoIBGE().trim().isEmpty()) {
				throw new Exception("Código IBGE da cidade do tomador não cadastrado!");
			}
			nfseVO.setCodigoIBGEMunicTomador(Integer.valueOf(notaFiscalSaidaVO.getFornecedorVO().getCidade().getCodigoIBGE()));
			nfseVO.setUfTomador(UFEnum.valueOf(notaFiscalSaidaVO.getFornecedorVO().getCidade().getEstado().getSigla()));
		} else if (notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
			nfseVO.setCpfCnpjTomador(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getCPF().replace("-", "").replace("/", "").replace(".", "").trim());
			nfseVO.setRazSocialTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getNome()));
			nfseVO.setEnderecoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getEndereco()));
			nfseVO.setComplementoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getComplemento()));
			if (notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getNumero().replace("ª", "").replace("º", "").isEmpty()) {
				nfseVO.setNumEndTomador("SN");
			} else {
				nfseVO.setNumEndTomador(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getNumero().replace("ª", "").replace("º", ""));
			}
			nfseVO.setBairroTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getSetor()));
			nfseVO.setCepTomador(Integer.valueOf(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getCEP().replace(".", "").replace("-", "")));
			if (Uteis.getValidaEmail(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getEmail().trim())) {
				nfseVO.setEmailTomador(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getEmail().trim());
			}
			if (notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getCidade().getCodigoIBGE().trim().isEmpty()) {
				throw new Exception("Código IBGE da cidade do tomador não cadastrado!");
			}
			nfseVO.setCodigoIBGEMunicTomador(Integer.valueOf(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getCidade().getCodigoIBGE()));
			nfseVO.setUfTomador(UFEnum.valueOf(notaFiscalSaidaVO.getFuncionarioVO().getPessoa().getCidade().getEstado().getSigla()));
		} else if (notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
			if (notaFiscalSaidaVO.getParceiroVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
				nfseVO.setCpfCnpjTomador(notaFiscalSaidaVO.getParceiroVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim());
				nfseVO.setInscMunicTomador(Uteis.removeCaractersEspeciais(notaFiscalSaidaVO.getParceiroVO().getInscMunicipal().replace("-", "").replace(".", "").trim()));
			} else {
				nfseVO.setCpfCnpjTomador(notaFiscalSaidaVO.getParceiroVO().getCPF().replace("-", "").replace("/", "").replace(".", "").trim());
			}
			nfseVO.setRazSocialTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getNome()));
			nfseVO.setEnderecoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getEndereco()));
			nfseVO.setComplementoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getComplemento()));
			if (notaFiscalSaidaVO.getParceiroVO().getNumero().replace("ª", "").replace("º", "").isEmpty()) {
				nfseVO.setNumEndTomador("SN");
			} else {
				nfseVO.setNumEndTomador(notaFiscalSaidaVO.getParceiroVO().getNumero().replace("ª", "").replace("º", ""));
			}
			nfseVO.setBairroTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getSetor()));
			nfseVO.setCepTomador(Integer.valueOf(notaFiscalSaidaVO.getParceiroVO().getCEP().replace(".", "").replace("-", "")));
			if (Uteis.getValidaEmail(notaFiscalSaidaVO.getParceiroVO().getEmail().trim())) {
				nfseVO.setEmailTomador(notaFiscalSaidaVO.getParceiroVO().getEmail().trim());
			}
			if (notaFiscalSaidaVO.getParceiroVO().getCidade().getCodigoIBGE().trim().isEmpty()) {
				throw new Exception("Código IBGE da cidade do tomador não cadastrado!");
			}
			nfseVO.setCodigoIBGEMunicTomador(Integer.valueOf(notaFiscalSaidaVO.getParceiroVO().getCidade().getCodigoIBGE()));
			nfseVO.setUfTomador(UFEnum.valueOf(notaFiscalSaidaVO.getParceiroVO().getCidade().getEstado().getSigla()));
		} else if (notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor())) {
			nfseVO.setCpfCnpjTomador(Uteis.removeCaractersEspeciais(notaFiscalSaidaVO.getResponsavelFinanceiro().getCPF().replace("-", "").replace("/", "").replace(".", "").trim()));
			nfseVO.setRazSocialTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getResponsavelFinanceiro().getNome()));
			nfseVO.setEnderecoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getResponsavelFinanceiro().getEndereco()));
			nfseVO.setComplementoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getResponsavelFinanceiro().getComplemento()));
			if (notaFiscalSaidaVO.getResponsavelFinanceiro().getNumero().replace("ª", "").replace("º", "").isEmpty()) {
				nfseVO.setNumEndTomador("SN");
			} else {
				nfseVO.setNumEndTomador(notaFiscalSaidaVO.getResponsavelFinanceiro().getNumero().replace("ª", "").replace("º", ""));
			}
			nfseVO.setBairroTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getResponsavelFinanceiro().getSetor()));
			nfseVO.setCepTomador(Integer.valueOf(Uteis.removeCaractersEspeciais(notaFiscalSaidaVO.getCep().replace(".", "").replace("-", ""))));
			if (Uteis.getValidaEmail(notaFiscalSaidaVO.getResponsavelFinanceiro().getEmail().trim())) {
				nfseVO.setEmailTomador(notaFiscalSaidaVO.getResponsavelFinanceiro().getEmail().trim());
			}
			if (notaFiscalSaidaVO.getResponsavelFinanceiro().getCidade().getCodigoIBGE().trim().isEmpty()) {
				throw new Exception("Código IBGE da cidade do tomador não cadastrado!");
			}
			nfseVO.setCodigoIBGEMunicTomador(Integer.valueOf(notaFiscalSaidaVO.getResponsavelFinanceiro().getCidade().getCodigoIBGE()));
			nfseVO.setUfTomador(UFEnum.valueOf(notaFiscalSaidaVO.getResponsavelFinanceiro().getCidade().getEstado().getSigla()));
		} else {
			nfseVO.setCpfCnpjTomador(Uteis.removeCaractersEspeciais(notaFiscalSaidaVO.getPessoaVO().getCPF().replace("-", "").replace("/", "").replace(".", "").trim()));
			nfseVO.setRazSocialTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getNome()));
			nfseVO.setEnderecoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getEndereco()));
			nfseVO.setComplementoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getComplemento()));
			if (notaFiscalSaidaVO.getPessoaVO().getNumero().replace("ª", "").replace("º", "").isEmpty()) {
				nfseVO.setNumEndTomador("SN");
			} else {
				nfseVO.setNumEndTomador(notaFiscalSaidaVO.getPessoaVO().getNumero().replace("ª", "").replace("º", ""));
			}
			nfseVO.setBairroTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getSetor()));
			nfseVO.setCepTomador(Integer.valueOf(Uteis.removeCaractersEspeciais(notaFiscalSaidaVO.getCep().replace(".", "").replace("-", ""))));
			if (Uteis.getValidaEmail(notaFiscalSaidaVO.getPessoaVO().getEmail().trim())) {
				nfseVO.setEmailTomador(notaFiscalSaidaVO.getPessoaVO().getEmail().trim());
			}
			if (notaFiscalSaidaVO.getPessoaVO().getCidade().getCodigoIBGE().trim().isEmpty()) {
				throw new Exception("Código IBGE da cidade do tomador não cadastrado!");
			}
			nfseVO.setCodigoIBGEMunicTomador(Integer.valueOf(notaFiscalSaidaVO.getPessoaVO().getCidade().getCodigoIBGE()));
			nfseVO.setUfTomador(UFEnum.valueOf(notaFiscalSaidaVO.getPessoaVO().getCidade().getEstado().getSigla()));
		}
		if (!cidade.getRetornaUrlNotaAutorizada()) {
			nfseVO.setCaminhoPDF(configuracaoRespositoriArquivo.getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.NFE.getValue() + File.separator + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue());
			nfseVO.setCaminhoWeb(configuracaoRespositoriArquivo.getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.NFE.getValue() + "/" + PastaBaseArquivoEnum.NOTAS_ENVIADAS.getValue());
			String logo = getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png";
			if (FacesContext.getCurrentInstance() != null) {
				LoginControle loginControle = (LoginControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LoginControle");
				if (loginControle != null && !loginControle.getUrlFisicoLogoUnidadeEnsinoRelatorio().trim().isEmpty()) {
					logo = loginControle.getUrlFisicoLogoUnidadeEnsinoRelatorio();
				}
			}
			nfseVO.setLogoPrestador(logo);
		}
		return nfseVO;
	}

	@Override
	public String montarDescrimicaoNotaFiscalServico(String mensagem, Object[] parametros) throws Exception {
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), parametros[0].toString());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.NOME_CURSO.name(), parametros[1].toString().toUpperCase());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.TIPO_PESSOA.name(), parametros[2].toString());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.COMPETENCIA.name(), parametros[3].toString());
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.DESCRICAO_CONVENIO.name(), parametros[4].toString());
		return mensagem;
	}
	
	@Override
	public void consultarLoteRps(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado, UsuarioVO usuarioVO) throws Exception {
		try {
			progressBar.setStatus("Consultando NFS-e n° " + notaFiscalSaidaVO.getNumeroNota() + " ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
			NFSeVO nfseVO = montarNFSeVO(notaFiscalSaidaVO, configuracaoRespositoriArquivo, usuarioVO);
			ClienteNotaFiscalServicoEletronica cliente = new ClienteNotaFiscalServicoEletronica();
			NFSeVO retorno = cliente.consultarNfseServicoPrestado(nfseVO, getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesWebserviceNFe().getUrlWebserviceNFSe());			
			retorno.getMensagens();
		} catch (Exception e) {
			throw e;
		}
	}
	
}
