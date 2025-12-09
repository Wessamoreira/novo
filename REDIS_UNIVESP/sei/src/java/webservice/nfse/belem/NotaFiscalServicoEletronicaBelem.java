package webservice.nfse.belem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.enumerador.TipoEmpresaEnum;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;
import negocio.comuns.faturamento.nfe.enumeradores.SituacaoNotaFiscalSaidaEnum;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import webservice.nfse.NotaFiscalServicoEletronicaInterfaceFacade;
import webservice.nfse.belem.ReqEnvioLoteRPS.Cabecalho;

@Repository
@Scope(value = "singleton")
@Lazy
@Transactional(readOnly = true)
public class NotaFiscalServicoEletronicaBelem extends ControleAcesso implements NotaFiscalServicoEletronicaInterfaceFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static String idEntidade;

	public NotaFiscalServicoEletronicaBelem() throws Exception {
		super();
		setIdEntidade("NotaFiscalServicoEletronicaUberlandia");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		NotaFiscalServicoEletronicaBelem.idEntidade = idEntidade;
	}

	@Override
	public void enviarXmlEnvio(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado, UsuarioVO usuarioVO) throws Exception {
		try {
			ReqEnvioLoteRPS reqEnvioLoteRPS = new ReqEnvioLoteRPS();
			reqEnvioLoteRPS = montarXmlEnvio(notaFiscalSaidaVO, usuarioVO);
			Nfse builder =  Nfse.nfse();		
			notaFiscalSaidaVO.setXmlEnvio(builder.withReqRPS(reqEnvioLoteRPS).asGerarNFSEXML("1"+ notaFiscalSaidaVO.getFornecedorVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim() +Uteis.preencherComZerosPosicoesVagas(notaFiscalSaidaVO.getNumeroNota(), 16), notaFiscalSaidaVO, configuracaoRespositoriArquivo));
			getFacadeFactory().getNotaFiscalSaidaFacade().gravarXmlEnvio(notaFiscalSaidaVO.getCodigo(), notaFiscalSaidaVO.getXmlEnvio(), usuarioVO);
			LoteRps enviarSincrono = new LoteRpsServiceLocator().getLoteRps();
			notaFiscalSaidaVO.setMensagemRetorno(enviarSincrono.enviar(notaFiscalSaidaVO.getXmlEnvio()));
			getFacadeFactory().getNotaFiscalSaidaFacade().alterarMensagemRetorno(notaFiscalSaidaVO, usuarioVO);
			notaFiscalSaidaVO.setDataStuacao(new Date());
			notaFiscalSaidaVO.setMensagemRetorno(notaFiscalSaidaVO.getMensagemRetorno().replaceAll("&lt;", "<").replaceAll("&gt;", ">"));
			if (notaFiscalSaidaVO.getMensagemRetorno().contains("<Sucesso>true</Sucesso>")) {				
				consultarLoteRps(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
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
	
	public ReqEnvioLoteRPS montarXmlEnvio(NotaFiscalSaidaVO notaFiscalSaidaVO,UsuarioVO usuarioVO) throws Exception{
		BigDecimal valorTotalDeducoes = new BigDecimal(0.0);
		String nomeCurso = getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(notaFiscalSaidaVO.getMatricula(), false, usuarioVO).getNome();
		ReqEnvioLoteRPS reqEnvioLoteRPS = new ReqEnvioLoteRPS();
		Cabecalho cabecalho = new Cabecalho();
		cabecalho.setCodCidade("0427");
		cabecalho.setCPFCNPJRemetente(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim());
		cabecalho.setRazaoSocialRemetente(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getUnidadeEnsinoVO().getRazaoSocial()));
		cabecalho.setTransacao("");
		Calendar calendar = GregorianCalendar.getInstance();
		cabecalho.setDtInicio(DateConverter.getConverted2(calendar));
		cabecalho.setDtFim(DateConverter.getConverted2(calendar));
		cabecalho.setQtdRPS(1);
		cabecalho.setValorTotalServicos(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal(), 2));
		cabecalho.setVersao(1);
		cabecalho.setMetodoEnvio(TpMetodoEnvio.WS);
		reqEnvioLoteRPS.setCabecalho(cabecalho);
		TpLote tpLote = new TpLote();
		tpLote.setId("lote:1ABCDZ");
		TpRPS tpRPS = new TpRPS();
		TpItens tpItens = new TpItens();
		tpRPS.setId("rps:"+notaFiscalSaidaVO.getNumero());
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
		String assinatura = executarCriacaoAssinatura(notaFiscalSaidaVO);
		if(notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor())) {
			tpRPS.setAssinatura(DigestUtils.shaHex(montarDadosSacado(notaFiscalSaidaVO, notaFiscalSaidaVO.getFornecedorVO(), tpRPS, assinatura, tpItens, nomeCurso.replace("&", "E"))));
		} else if(notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
			tpRPS.setAssinatura(DigestUtils.shaHex(montarDadosSacado(notaFiscalSaidaVO, notaFiscalSaidaVO.getParceiroVO(), tpRPS, assinatura, tpItens, nomeCurso.replace("&", "E"))));
		} else {
			tpRPS.setAssinatura(DigestUtils.shaHex(montarDadosSacado(notaFiscalSaidaVO, notaFiscalSaidaVO.getPessoaVO(), tpRPS, assinatura, tpItens, nomeCurso.replace("&", "E"))));
		}
		tpRPS.setDescricaoRPS("REFERENTE A SERVICO PRESTADO");	
		tpRPS.setCodigoAtividade(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoTributacaoMunicipio());
		tpRPS.setAliquotaAtividade(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getIssqn(), 2));
		tpRPS.setTipoRecolhimento(TpTipoRecolhimento.A);
		tpRPS.setMunicipioPrestacao("000"+notaFiscalSaidaVO.getWebServicesNFSEEnum().getCodigoCidadePadraoSiafi().toString());
		tpRPS.setMunicipioPrestacaoDescricao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getUnidadeEnsinoVO().getCidade().getNome()).toUpperCase());
		tpRPS.setOperacao(TpOperacao.A);
		tpRPS.setTributacao(TpTributacao.T);
		cabecalho.setValorTotalDeducoes(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(valorTotalDeducoes.doubleValue(), 2));
		tpRPS.setDDDPrestador("091");
		tpRPS.setTelefonePrestador(notaFiscalSaidaVO.getUnidadeEnsinoVO().getTelComercial1().replace("(", "").replace(")", "").replace("-", "").substring(0, 2));
		tpRPS.setMotCancelamento("");
		tpRPS.setCPFCNPJIntermediario("");
		tpRPS.setSerieRPSSubstituido("");
		tpItens.setQuantidade(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(1.00, 4));
		tpItens.setValorUnitario(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal(), 4));
		tpItens.setValorTotal(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal(), 2));
		tpItens.setTributavel(TpItemTributavel.S);
		tpRPS.setDeducoes(new ArrayList<TpDeducoes>(1));
		TpListaItens tpListaItens = new TpListaItens();
		tpListaItens.setItem(tpItens);
		tpRPS.setItens(tpListaItens);
		tpLote.setRps(tpRPS);
		reqEnvioLoteRPS.setLote(tpLote);
		return  reqEnvioLoteRPS;
	}

	public String executarCriacaoAssinatura(NotaFiscalSaidaVO notaFiscalSaidaVO) throws Exception {
		StringBuilder assinatura = new StringBuilder();
		assinatura.append("0000"+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getUnidadeEnsinoVO().getInscMunicipal().replace(".", "").replace("-", ""))+"NF");
		assinatura.append("   "+Uteis.getPreencherComZeroEsquerda(notaFiscalSaidaVO.getNumeroNota(), 12)+""+Uteis.getDataAplicandoFormatacao(new Date(), "yyyyMMdd")+"T ");
		assinatura.append("NN"+Uteis.getPreencherComZeroEsquerda(Uteis.getValorMonetarioParaIntegracao_SemPontoNemVirgula(notaFiscalSaidaVO.getValorTotal()), 15));
		assinatura.append("0000000000000000"+notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoTributacaoMunicipio());
		return assinatura.toString();
	}
	
	/**
	 * Nessa Sequencia
	 * NOME_ALUNO, NOME_CURSO, TIPO_PESSOA, COMPETENCIA, DESCRICAO_CONVENIO
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
		mensagem = mensagem.replaceAll(TagsMensagemAutomaticaEnum.DESCRICAO_CONVENIO.name(), parametros[4].toString().trim());
		return mensagem;
	}
	
	public String montarDadosSacado(NotaFiscalSaidaVO notaFiscalSaidaVO, Object tipoPessoa, TpRPS tpRPS, String assinatura, TpItens tpItens, String nomeCurso) throws Exception {
		String datasCompetenciasContaReceberNotaFiscal = getFacadeFactory().getNotaFiscalSaidaFacade().concatenarDatasCompetenciasContaReceberNotaFiscal(notaFiscalSaidaVO.getNotaFiscalSaidaServicoVOs());
		if(tipoPessoa.equals(notaFiscalSaidaVO.getPessoaVO())) {
			tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getPessoaVO().getCPF().replace("-", "").replace("/", "").replace(".", "").trim());
			tpRPS.setRazaoSocialTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getNome()));
			tpRPS.setTipoLogradouroTomador("Rua");
			tpRPS.setLogradouroTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getEndereco()));
			tpRPS.setNumeroEnderecoTomador("0");
			tpRPS.setComplementoEnderecoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getComplemento()));
			tpRPS.setTipoBairroTomador("Bairro");
			tpRPS.setBairroTomador("");
			tpRPS.setCidadeTomador(notaFiscalSaidaVO.getPessoaVO().getCidade().getCodigoSiafi());
			tpRPS.setCidadeTomadorDescricao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getCidade().getNome()));
			tpRPS.setCEPTomador(notaFiscalSaidaVO.getPessoaVO().getCEP().replace(".", "").replace("-", ""));
			tpRPS.setDDDTomador("00");
			tpRPS.setTelefoneTomador("00000000");
			if(!notaFiscalSaidaVO.getPessoaVO().getEmail().equals("")) {
				tpRPS.setEmailTomador(notaFiscalSaidaVO.getPessoaVO().getEmail());				
			} else {
				tpRPS.setEmailTomador("-");				
			}
			tpRPS.setValorPIS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(0.00, 2));
			tpRPS.setValorCOFINS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(0.00, 2));
			tpRPS.setValorINSS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(0.00, 2));
			tpRPS.setValorIR(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(0.00, 2));
			tpRPS.setValorCSLL(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(0.00, 2));
			tpRPS.setAliquotaPIS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(0.00, 4));
			tpRPS.setAliquotaCOFINS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(0.00, 4));
			tpRPS.setAliquotaINSS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(0.00, 4));
			tpRPS.setAliquotaIR(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(0.00, 4));
			tpRPS.setAliquotaCSLL(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(0.00, 4));
			tpItens.setDiscriminacaoServico(Uteis.removerCaracteresEspeciais3(montarDescrimicaoNotaFiscalServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTextoPadraoDescriminacaoServicoNotaFiscal(), new Object[]{notaFiscalSaidaVO.getPessoaVO().getNome(), nomeCurso, notaFiscalSaidaVO.getTipoPessoa(), Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()), notaFiscalSaidaVO.getNomesConvenios()})));
			assinatura = assinatura+Uteis.getPreencherComZeroEsquerda(notaFiscalSaidaVO.getPessoaVO().getCPF().replace("-", "").replace("/", "").replace(".", "").trim(), 14);
		} else if(tipoPessoa.equals(notaFiscalSaidaVO.getParceiroVO())) {
			tpRPS.setRazaoSocialTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getNome()));
			tpRPS.setTipoLogradouroTomador("AV");
			tpRPS.setLogradouroTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getEndereco()));
			tpRPS.setNumeroEnderecoTomador("0");
			tpRPS.setComplementoEnderecoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getComplemento()));
			tpRPS.setTipoBairroTomador("BAIRRO");
			tpRPS.setBairroTomador("");
			tpRPS.setCidadeTomador(notaFiscalSaidaVO.getParceiroVO().getCidade().getCodigoSiafi());
			tpRPS.setCidadeTomadorDescricao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getCidade().getNome()));
			tpRPS.setCEPTomador(notaFiscalSaidaVO.getParceiroVO().getCEP().replace(".", "").replace("-", ""));
			tpRPS.setDDDTomador("00");
			tpRPS.setTelefoneTomador("00000000");			
			if(!notaFiscalSaidaVO.getParceiroVO().getEmail().equals("")) {
				tpRPS.setEmailTomador(notaFiscalSaidaVO.getParceiroVO().getEmail());				
			} else {
				tpRPS.setEmailTomador("-");				
			}
			tpItens.setDiscriminacaoServico(Uteis.removerCaracteresEspeciais3(montarDescrimicaoNotaFiscalServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTextoPadraoDescriminacaoServicoNotaFiscal(), new Object[]{notaFiscalSaidaVO.getParceiroVO().getNome(), nomeCurso, notaFiscalSaidaVO.getTipoPessoa(), Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()), notaFiscalSaidaVO.getNomesConvenios()})));
			if(notaFiscalSaidaVO.getParceiroVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
				tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getParceiroVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim());
				if (notaFiscalSaidaVO.getParceiroVO().getPossuiAliquotaEmissaoNotaEspecifica()) {
					if(notaFiscalSaidaVO.getParceiroVO().getPis() > 0) {
						tpRPS.setValorPIS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getPis() / 100), 2));
					}
					if(notaFiscalSaidaVO.getParceiroVO().getCofins() > 0) {
						tpRPS.setValorCOFINS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getCofins() / 100), 2));
					}
					if(notaFiscalSaidaVO.getParceiroVO().getInss() > 0 ) {
						tpRPS.setValorINSS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getInss() / 100), 2));
					}
					if(notaFiscalSaidaVO.getParceiroVO().getCsll() > 0) {
						tpRPS.setValorCSLL(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getCsll() / 100), 2));
					}
					tpRPS.setAliquotaPIS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getParceiroVO().getPis(), 4));
					tpRPS.setAliquotaCOFINS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getParceiroVO().getCofins(), 4));
					tpRPS.setAliquotaINSS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getParceiroVO().getInss(), 4));
					tpRPS.setAliquotaIR(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(0.00, 4));
					tpRPS.setAliquotaCSLL(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getParceiroVO().getCsll(), 4));
				} else {	
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() > 0) {
						tpRPS.setValorPIS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() / 100), 2));
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() > 0) {
						tpRPS.setValorCOFINS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() / 100), 2));
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() > 0) {
						tpRPS.setValorINSS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() / 100), 2));
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() > 0) {
						tpRPS.setValorCSLL(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() / 100), 2));
					}
					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAliquotaIR() > 0) {
						tpRPS.setValorIR(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAliquotaIR() / 100), 2));
					}
					tpRPS.setAliquotaPIS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis(), 4));
					tpRPS.setAliquotaCOFINS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins(), 4));
					tpRPS.setAliquotaINSS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss(), 4));
					tpRPS.setAliquotaIR(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAliquotaIR(), 4));
					tpRPS.setAliquotaCSLL(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll(), 4));
				}
				assinatura = assinatura+Uteis.getPreencherComZeroEsquerda(notaFiscalSaidaVO.getFornecedorVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim(), 14);
			} else {
				tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getParceiroVO().getCPF().replace("-", "").replace("/", "").replace(".", "").trim());
				assinatura = assinatura+Uteis.getPreencherComZeroEsquerda(notaFiscalSaidaVO.getFornecedorVO().getCPF().replace("-", "").replace("/", "").replace(".", "").trim(), 14);
			}
		} else {
			tpRPS.setRazaoSocialTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getNome()));
			tpRPS.setTipoLogradouroTomador("AV");
			tpRPS.setLogradouroTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getEndereco()));
			tpRPS.setNumeroEnderecoTomador("0");
			tpRPS.setComplementoEnderecoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getComplemento()));
			tpRPS.setTipoBairroTomador("BAIRRO");
			tpRPS.setBairroTomador("");
			tpRPS.setCidadeTomador(notaFiscalSaidaVO.getFornecedorVO().getCidade().getCodigoSiafi());
			tpRPS.setCidadeTomadorDescricao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getCidade().getNome()));
			tpRPS.setCEPTomador(notaFiscalSaidaVO.getFornecedorVO().getCEP().replace(".", "").replace("-", ""));
			tpRPS.setDDDTomador("00");
			tpRPS.setTelefoneTomador("00000000");
			if(!notaFiscalSaidaVO.getFornecedorVO().getEmail().equals("")) {
				tpRPS.setEmailTomador(notaFiscalSaidaVO.getFornecedorVO().getEmail());				
			} else {
				tpRPS.setEmailTomador("-");				
			}
			tpItens.setDiscriminacaoServico(Uteis.removerCaracteresEspeciais3(montarDescrimicaoNotaFiscalServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTextoPadraoDescriminacaoServicoNotaFiscal(), new Object[]{notaFiscalSaidaVO.getFornecedorVO().getNome(), nomeCurso, notaFiscalSaidaVO.getTipoPessoa(), Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao()), notaFiscalSaidaVO.getNomesConvenios()})));
			if(notaFiscalSaidaVO.getFornecedorVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
				tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getFornecedorVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim());
				tpRPS.setValorPIS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() / 100), 2));
				tpRPS.setValorCOFINS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() / 100), 2));
				tpRPS.setValorINSS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() / 100), 2));
				tpRPS.setValorIR(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAliquotaIR() / 100), 2));
				tpRPS.setValorCSLL(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula2(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() / 100), 2));
				assinatura = assinatura+Uteis.getPreencherComZeroEsquerda(notaFiscalSaidaVO.getFornecedorVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim(), 14);
			} else {
				tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getFornecedorVO().getCPF().replace("-", "").replace(".", "").trim());	
				assinatura = assinatura+Uteis.getPreencherComZeroEsquerda(notaFiscalSaidaVO.getFornecedorVO().getCPF().replace("-", "").replace("/", "").replace(".", "").trim(), 14);
			}
		}
		return assinatura;
	}
	
	@Override
	public void consultarLoteRps(NotaFiscalSaidaVO notaFiscalSaidaVO,ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado, UsuarioVO usuarioVO) throws Exception{
		try {
			String cnpj = notaFiscalSaidaVO.getUnidadeEnsinoVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim();
			ReqConsultaLoteRPS reqConsultaLoteRPS = new ReqConsultaLoteRPS();
			ReqConsultaLoteRPS.Cabecalho cabecalho = new ReqConsultaLoteRPS.Cabecalho();
			cabecalho.setCodCidade("0427");
			cabecalho.setVersao(1);
			cabecalho.setCPFCNPJRemetente(cnpj);
			cabecalho.setNumeroLote(notaFiscalSaidaVO.getMensagemRetorno().substring(notaFiscalSaidaVO.getMensagemRetorno().indexOf("<NumeroLote>")+"<NumeroLote>".length(), notaFiscalSaidaVO.getMensagemRetorno().indexOf("</NumeroLote>")));
			reqConsultaLoteRPS.setCabecalho(cabecalho);
			String retorno = "";
			String xmlEnvioConsulta="";
			Nfse builder =  Nfse.nfse();
			xmlEnvioConsulta = builder.withReqConsultaRPS(reqConsultaLoteRPS).asGerarNFSEXMLConsulta();
			progressBar.setStatus("Consultando NFS-e n° " + notaFiscalSaidaVO.getNumeroNota() + " ( " + progressBar.getProgresso() + " de " + progressBar.getMaxValue() + " ) ");
			Thread.sleep(10000);
			LoteRps consultar = new LoteRpsServiceLocator().getLoteRps();
			retorno = consultar.consultarLote(xmlEnvioConsulta);
			if ((retorno.contains("<Sucesso>true</Sucesso>")) || (retorno.contains("<Sucesso>false</Sucesso>") && retorno.contains("<Erro>"))) {
				notaFiscalSaidaVO.setMensagemRetorno(retorno);
			} else {
				for (int i = 0; i < 2; i++) {
					Thread.sleep(2000);
					retorno = consultar.consultarLote(xmlEnvioConsulta);
					notaFiscalSaidaVO.setMensagemRetorno(retorno);
					if ((retorno.contains("<Sucesso>true</Sucesso>")) || (retorno.contains("<Sucesso>false</Sucesso>") && retorno.contains("<Erro>"))) {
						break;
					}
				}				
			}
			validarLoteRps(notaFiscalSaidaVO, configuracaoRespositoriArquivo, progressBar, autorizado, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	public static void validarLoteRps(NotaFiscalSaidaVO notaFiscalSaidaVO,ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado, UsuarioVO usuarioVO) throws Exception {
		try {
			notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor());
			getFacadeFactory().getNotaFiscalSaidaFacade().gravarSituacaoEnvio(notaFiscalSaidaVO, SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor(), "", notaFiscalSaidaVO.getDataStuacao(), notaFiscalSaidaVO.getIdentificadorReceita(), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioVO);
			if (notaFiscalSaidaVO.getMensagemRetorno().contains("<Sucesso>true</Sucesso>")) {
				String numeroNota = notaFiscalSaidaVO.getMensagemRetorno().substring(notaFiscalSaidaVO.getMensagemRetorno().indexOf("<NumeroRPS>"), notaFiscalSaidaVO.getMensagemRetorno().indexOf("</NumeroRPS>"));
				numeroNota = numeroNota.replace("<NumeroRPS>", "");
				String codigoVerificacao = notaFiscalSaidaVO.getMensagemRetorno().substring(notaFiscalSaidaVO.getMensagemRetorno().lastIndexOf("<CodigoVerificacao>"), notaFiscalSaidaVO.getMensagemRetorno().indexOf("</CodigoVerificacao>"));
				codigoVerificacao = codigoVerificacao.replace("<CodigoVerificacao>", "");
				notaFiscalSaidaVO.setIdentificadorReceita(codigoVerificacao);
				notaFiscalSaidaVO.setRecibo(numeroNota);
				getFacadeFactory().getNotaFiscalSaidaFacade().gravarRecibo(notaFiscalSaidaVO.getCodigo(), notaFiscalSaidaVO.getRecibo(), usuarioVO);
				getFacadeFactory().getNotaFiscalSaidaFacade().gravarSituacaoEnvio(notaFiscalSaidaVO, SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor(), "", notaFiscalSaidaVO.getDataStuacao(), notaFiscalSaidaVO.getIdentificadorReceita(), notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO(), usuarioVO);
				notaFiscalSaidaVO.setSituacao(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor());
				notaFiscalSaidaVO.setLinkAcesso(notaFiscalSaidaVO.getWebServicesNFSEEnum().getValorApresentar().replace("{0}", notaFiscalSaidaVO.getUnidadeEnsinoVO().getInscMunicipal()).replace("{1}", notaFiscalSaidaVO.getRecibo()).replace("{2}", notaFiscalSaidaVO.getIdentificadorReceita()));
			} else if (notaFiscalSaidaVO.getMensagemRetorno().contains("203")) {
				return;
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