package webservice.nfse.florianopolis;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.faturamento.nfe.ConexaoNFSE;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaVO;
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
public class NotaFiscalServicoEletronicaFlorianopolis extends ControleAcesso implements NotaFiscalServicoEletronicaInterfaceFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static String idEntidade;

	public NotaFiscalServicoEletronicaFlorianopolis() throws Exception {
		super();
		setIdEntidade("NotaFiscalServicoEletronicaFlorianopolis");
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		NotaFiscalServicoEletronicaFlorianopolis.idEntidade = idEntidade;
	}

	@Override
	public void enviarXmlEnvio(NotaFiscalSaidaVO notaFiscalSaidaVO, ConfiguracaoGeralSistemaVO configuracaoRespositoriArquivo, ProgressBarVO progressBar, String autorizado, UsuarioVO usuarioVO) throws Exception {
		try {
			TcInfRequisicao tcInfRequisicao = new TcInfRequisicao();
			tcInfRequisicao = montarXmlEnvio(notaFiscalSaidaVO, usuarioVO);
			Nfse builder =  Nfse.nfse();		
			notaFiscalSaidaVO.setXmlEnvio(builder.withTcInfRequisicao(tcInfRequisicao).asGerarNFSEXML("1"+ notaFiscalSaidaVO.getFornecedorVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim() +Uteis.preencherComZerosPosicoesVagas(notaFiscalSaidaVO.getNumeroNota(), 16), notaFiscalSaidaVO, configuracaoRespositoriArquivo));
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
				notaFiscalSaidaVO.setLinkAcesso("http://udigital.uberlandia.mg.gov.br/nfse/");
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

	
	public TcInfRequisicao montarXmlEnvio(NotaFiscalSaidaVO notaFiscalSaidaVO,UsuarioVO usuarioVO) throws Exception{
		TcInfRequisicao tcInfRequisicao = new TcInfRequisicao();
		tcInfRequisicao.setVersao(new BigDecimal("2.0"));
		tcInfRequisicao.setTipoSistema(0);
		// BEGIN AEDF
		tcInfRequisicao.getAEDF().setAEDF("000000");
		tcInfRequisicao.getAEDF().setTipoAedf(TsTipoAedf.NORMAL);
		// END AEDF
//		tcInfRequisicao.setDataEmissao(value);
		tcInfRequisicao.setCFPS(801);
		// BEGIN DADOS SERVIÇOS
		TcItemServico itemServico = new TcItemServico();
		itemServico.setIdCNAE(801);
		itemServico.setCodigoAtividade(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCodigoTributacaoMunicipio());
		TcIdentificacaoTomador tcIdentificacaoTomador = new TcIdentificacaoTomador();
		if(notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor())) {
			tcIdentificacaoTomador = montarDadosSacado(notaFiscalSaidaVO, notaFiscalSaidaVO.getFornecedorVO(), itemServico);
		} else if(notaFiscalSaidaVO.getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
			tcIdentificacaoTomador = montarDadosSacado(notaFiscalSaidaVO, notaFiscalSaidaVO.getParceiroVO(), itemServico);
		} else {
			tcIdentificacaoTomador = montarDadosSacado(notaFiscalSaidaVO, notaFiscalSaidaVO.getPessoaVO(), itemServico);
		}
		itemServico.setCST(0);
		itemServico.setAliquota(new BigDecimal(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getIssqn() / 100));
		itemServico.setValorUnitario(new BigDecimal(notaFiscalSaidaVO.getValorTotal()));
		itemServico.setQuantidade(1);
		itemServico.setValorTotal(new BigDecimal(notaFiscalSaidaVO.getValorTotal()));
		tcInfRequisicao.getDadosServico().getItemServico().add(itemServico);
		tcInfRequisicao.getDadosServico().setBaseCalculo(new BigDecimal(notaFiscalSaidaVO.getValorTotal()));
		tcInfRequisicao.getDadosServico().setValorISSQN(new BigDecimal(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getIssqn() / 100)));
		tcInfRequisicao.getDadosServico().setValorTotalServicos(new BigDecimal(notaFiscalSaidaVO.getValorTotal()));
		// END DADOS SERVIÇOS
		return null;
	}
	
	public String executarCriacaoAssinatura(NotaFiscalSaidaVO notaFiscalSaidaVO) throws Exception {
		StringBuilder assinatura = new StringBuilder();
		assinatura.append("000"+Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getUnidadeEnsinoVO().getInscMunicipal().replace(".", "").replace("-", ""))+"NF");
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
	
	public TcIdentificacaoTomador montarDadosSacado(NotaFiscalSaidaVO notaFiscalSaidaVO, Object tipoPessoa, TcItemServico itemServico) throws Exception {
		TcIdentificacaoTomador tcIdentificacaoTomador = new TcIdentificacaoTomador();
//		if(tipoPessoa.equals(notaFiscalSaidaVO.getPessoaVO())) {
//			tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getPessoaVO().getCPF().replace("-", "").replace("/", "").replace(".", "").trim());
//			tpRPS.setRazaoSocialTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getNome()));
//			tpRPS.setTipoLogradouroTomador("Rua");
//			tpRPS.setLogradouroTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getEndereco()));
//			tpRPS.setNumeroEnderecoTomador("0");
//			tpRPS.setComplementoEnderecoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getComplemento()));
//			tpRPS.setTipoBairroTomador("Bairro");
//			tpRPS.setBairroTomador("");
//			tpRPS.setCidadeTomador(notaFiscalSaidaVO.getPessoaVO().getCidade().getCodigoSiafi());
//			tpRPS.setCidadeTomadorDescricao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getPessoaVO().getCidade().getNome()));
//			tpRPS.setCEPTomador(notaFiscalSaidaVO.getPessoaVO().getCEP().replace(".", "").replace("-", ""));
//			tpRPS.setDDDTomador("00");
//			tpRPS.setTelefoneTomador("00000000");
//			if(!notaFiscalSaidaVO.getPessoaVO().getEmail().equals("")) {
//				tpRPS.setEmailTomador(notaFiscalSaidaVO.getPessoaVO().getEmail());				
//			} else {
//				tpRPS.setEmailTomador("-");				
//			}
//			tpRPS.setValorPIS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(0.00, 2));
//			tpRPS.setValorCOFINS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(0.00, 2));
//			tpRPS.setValorINSS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(0.00, 2));
//			tpRPS.setValorIR(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(0.00, 2));
//			tpRPS.setValorCSLL(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(0.00, 2));
//			tpRPS.setAliquotaPIS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(0.00, 4));
//			tpRPS.setAliquotaCOFINS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(0.00, 4));
//			tpRPS.setAliquotaINSS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(0.00, 4));
//			tpRPS.setAliquotaIR(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(0.00, 4));
//			tpRPS.setAliquotaCSLL(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(0.00, 4));
//			tpItens.setDiscriminacaoServico(Uteis.removerCaracteresEspeciais3(montarDescrimicaoNotaFiscalServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTextoPadraoDescriminacaoServicoNotaFiscal(), new Object[]{notaFiscalSaidaVO.getPessoaVO().getNome(), nomeCurso, notaFiscalSaidaVO.getTipoPessoa(), Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao())})));
//			assinatura = assinatura+Uteis.getPreencherComZeroEsquerda(notaFiscalSaidaVO.getPessoaVO().getCPF().replace("-", "").replace("/", "").replace(".", "").trim(), 14);
//		} else if(tipoPessoa.equals(notaFiscalSaidaVO.getParceiroVO())) {
//			tpRPS.setRazaoSocialTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getNome()));
//			tpRPS.setTipoLogradouroTomador("AV");
//			tpRPS.setLogradouroTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getEndereco()));
//			tpRPS.setNumeroEnderecoTomador("0");
//			tpRPS.setComplementoEnderecoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getComplemento()));
//			tpRPS.setTipoBairroTomador("BAIRRO");
//			tpRPS.setBairroTomador("");
//			tpRPS.setCidadeTomador(notaFiscalSaidaVO.getParceiroVO().getCidade().getCodigoSiafi());
//			tpRPS.setCidadeTomadorDescricao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getParceiroVO().getCidade().getNome()));
//			tpRPS.setCEPTomador(notaFiscalSaidaVO.getParceiroVO().getCEP().replace(".", "").replace("-", ""));
//			tpRPS.setDDDTomador("00");
//			tpRPS.setTelefoneTomador("00000000");			
//			if(!notaFiscalSaidaVO.getParceiroVO().getEmail().equals("")) {
//				tpRPS.setEmailTomador(notaFiscalSaidaVO.getParceiroVO().getEmail());				
//			} else {
//				tpRPS.setEmailTomador("-");				
//			}
//			tpItens.setDiscriminacaoServico(Uteis.removerCaracteresEspeciais3(montarDescrimicaoNotaFiscalServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTextoPadraoDescriminacaoServicoNotaFiscal(), new Object[]{notaFiscalSaidaVO.getParceiroVO().getNome(), nomeCurso, notaFiscalSaidaVO.getTipoPessoa(), Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao())})));
//			if(notaFiscalSaidaVO.getParceiroVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
//				tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getParceiroVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim());
//				if (notaFiscalSaidaVO.getParceiroVO().getPossuiAliquotaEmissaoNotaEspecifica()) {
//					if(notaFiscalSaidaVO.getParceiroVO().getPis() > 0) {
//						tpRPS.setValorPIS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getPis() / 100), 2));
//					}
//					if(notaFiscalSaidaVO.getParceiroVO().getCofins() > 0) {
//						tpRPS.setValorCOFINS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getCofins() / 100), 2));
//					}
//					if(notaFiscalSaidaVO.getParceiroVO().getInss() > 0 ) {
//						tpRPS.setValorINSS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getInss() / 100), 2));
//					}
//					if(notaFiscalSaidaVO.getParceiroVO().getCsll() > 0) {
//						tpRPS.setValorCSLL(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getParceiroVO().getCsll() / 100), 2));
//					}
//					tpRPS.setAliquotaPIS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getParceiroVO().getPis(), 4));
//					tpRPS.setAliquotaCOFINS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getParceiroVO().getCofins(), 4));
//					tpRPS.setAliquotaINSS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getParceiroVO().getInss(), 4));
//					tpRPS.setAliquotaIR(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(0.00, 4));
//					tpRPS.setAliquotaCSLL(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getParceiroVO().getCsll(), 4));
//				} else {	
//					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() > 0) {
//						tpRPS.setValorPIS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() / 100), 2));
//					}
//					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() > 0) {
//						tpRPS.setValorCOFINS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() / 100), 2));
//					}
//					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() > 0) {
//						tpRPS.setValorINSS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() / 100), 2));
//					}
//					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() > 0) {
//						tpRPS.setValorCSLL(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() / 100), 2));
//					}
//					if(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAliquotaIR() > 0) {
//						tpRPS.setValorIR(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAliquotaIR() / 100), 2));
//					}
//					tpRPS.setAliquotaPIS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis(), 4));
//					tpRPS.setAliquotaCOFINS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins(), 4));
//					tpRPS.setAliquotaINSS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss(), 4));
//					tpRPS.setAliquotaIR(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAliquotaIR(), 4));
//					tpRPS.setAliquotaCSLL(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll(), 4));
//				}
//				assinatura = assinatura+Uteis.getPreencherComZeroEsquerda(notaFiscalSaidaVO.getFornecedorVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim(), 14);
//			} else {
//				tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getParceiroVO().getCPF().replace("-", "").replace("/", "").replace(".", "").trim());
//				assinatura = assinatura+Uteis.getPreencherComZeroEsquerda(notaFiscalSaidaVO.getFornecedorVO().getCPF().replace("-", "").replace("/", "").replace(".", "").trim(), 14);
//			}
//		} else {
//			tpRPS.setRazaoSocialTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getNome()));
//			tpRPS.setTipoLogradouroTomador("AV");
//			tpRPS.setLogradouroTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getEndereco()));
//			tpRPS.setNumeroEnderecoTomador("0");
//			tpRPS.setComplementoEnderecoTomador(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getComplemento()));
//			tpRPS.setTipoBairroTomador("BAIRRO");
//			tpRPS.setBairroTomador("");
//			tpRPS.setCidadeTomador(notaFiscalSaidaVO.getFornecedorVO().getCidade().getCodigoSiafi());
//			tpRPS.setCidadeTomadorDescricao(Uteis.removerCaracteresEspeciais3(notaFiscalSaidaVO.getFornecedorVO().getCidade().getNome()));
//			tpRPS.setCEPTomador(notaFiscalSaidaVO.getFornecedorVO().getCEP().replace(".", "").replace("-", ""));
//			tpRPS.setDDDTomador("00");
//			tpRPS.setTelefoneTomador("00000000");
//			if(!notaFiscalSaidaVO.getFornecedorVO().getEmail().equals("")) {
//				tpRPS.setEmailTomador(notaFiscalSaidaVO.getFornecedorVO().getEmail());				
//			} else {
//				tpRPS.setEmailTomador("-");				
//			}
//			tpItens.setDiscriminacaoServico(Uteis.removerCaracteresEspeciais3(montarDescrimicaoNotaFiscalServico(notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getTextoPadraoDescriminacaoServicoNotaFiscal(), new Object[]{notaFiscalSaidaVO.getFornecedorVO().getNome(), nomeCurso, notaFiscalSaidaVO.getTipoPessoa(), Uteis.getMesReferenciaData(notaFiscalSaidaVO.getDataEmissao())})));
//			if(notaFiscalSaidaVO.getFornecedorVO().getTipoEmpresa().equals(TipoEmpresaEnum.JU.name())) {
//				tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getFornecedorVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim());
//				tpRPS.setValorPIS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getPis() / 100), 2));
//				tpRPS.setValorCOFINS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCofins() / 100), 2));
//				tpRPS.setValorINSS(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getInss() / 100), 2));
//				tpRPS.setValorIR(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getAliquotaIR() / 100), 2));
//				tpRPS.setValorCSLL(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(notaFiscalSaidaVO.getValorTotal() * (notaFiscalSaidaVO.getUnidadeEnsinoVO().getConfiguracaoNotaFiscalVO().getCsll() / 100), 2));
//				assinatura = assinatura+Uteis.getPreencherComZeroEsquerda(notaFiscalSaidaVO.getFornecedorVO().getCNPJ().replace("-", "").replace("/", "").replace(".", "").trim(), 14);
//			} else {
//				tpRPS.setCPFCNPJTomador(notaFiscalSaidaVO.getFornecedorVO().getCPF().replace("-", "").replace(".", "").trim());	
//				assinatura = assinatura+Uteis.getPreencherComZeroEsquerda(notaFiscalSaidaVO.getFornecedorVO().getCPF().replace("-", "").replace("/", "").replace(".", "").trim(), 14);
//			}
//		}
		return tcIdentificacaoTomador;
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
