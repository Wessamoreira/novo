package jobs;

import java.util.concurrent.Callable;

import org.apache.commons.lang.StringUtils;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoArquivoRetorno;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

public class JobProcessarArquivoRetornoPagar extends ControleAcesso implements Callable<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8427721840987665565L;
	private ControleCobrancaPagarVO controleCobrancaPagarVO;
	private ConfiguracaoFinanceiroVO configuracaoFinanceiroSistemaVO;
	private UsuarioVO usuarioVO;
	private ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO;
	private ComunicacaoInternaVO comunicacaoInternaVO;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;

	public JobProcessarArquivoRetornoPagar(ControleCobrancaPagarVO controleCobrancaPagarVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroSistemaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) {
		this.controleCobrancaPagarVO = controleCobrancaPagarVO;
		this.configuracaoFinanceiroSistemaVO = configuracaoFinanceiroSistemaVO;
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
		this.usuarioVO = usuario;
	}

	public Object call() throws Exception {
		try {
			getControleCobrancaPagarVO().setBancoVO(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(getControleCobrancaPagarVO().getBancoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioVO()));
			getFacadeFactory().getControleCobrancaPagarFacade().processarArquivo(getControleCobrancaPagarVO(), getConfiguracaoGeralSistemaVO(), getConfiguracaoFinanceiroSistemaVO(), getUsuarioVO());
			getControleCobrancaPagarVO().setListaContaCorrenteArquivoRetorno(getFacadeFactory().getContaCorrenteFacade().consultarPorBancoPorNumeroContaCorrentePorDigitoContaCorrente(getControleCobrancaPagarVO().getBancoVO().getNrBanco(), StringUtils.stripStart(getControleCobrancaPagarVO().getRegistroHeaderPagarVO().getNumeroConta(), "0"), getControleCobrancaPagarVO().getRegistroHeaderPagarVO().getDigitoConta(), "", getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_COMBOBOX	, usuarioVO));
			if(!Uteis.isAtributoPreenchido(getControleCobrancaPagarVO().getListaContaCorrenteArquivoRetorno())){
				throw new Exception("Não foi localizado no sistema uma conta corrente com os filtros usados banco, número conta corrente "+StringUtils.stripStart(getControleCobrancaPagarVO().getRegistroHeaderPagarVO().getNumeroConta(), "0")+" e dígito conta corrente "+getControleCobrancaPagarVO().getRegistroHeaderPagarVO().getDigitoConta()+".");
			}else if(getControleCobrancaPagarVO().getListaContaCorrenteArquivoRetorno().size() == 1){
				getControleCobrancaPagarVO().setContaCorrenteVO(getControleCobrancaPagarVO().getListaContaCorrenteArquivoRetorno().get(0));	
			}
			getFacadeFactory().getControleCobrancaPagarFacade().persistir(getControleCobrancaPagarVO(), true, getUsuarioVO(), getConfiguracaoGeralSistemaVO());
			Uteis.ARQUIVOS_CONTROLE_COBRANCA.remove(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaPagarVO().getNomeArquivo_Apresentar(), getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo()));
			Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaPagarVO().getNomeArquivo_Apresentar(), getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor());
			if (getControleCobrancaPagarVO().getContaPagarRegistroArquivoVOs().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_ControleCobranca_nenhumaContaParaBaixa"));
			}
			//enviarEmail();
		} catch (Exception e) {
			if (Uteis.ARQUIVOS_CONTROLE_COBRANCA.containsKey(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaPagarVO().getNomeArquivo_Apresentar(), getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo())) && !Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaPagarVO().getNomeArquivo_Apresentar(), getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor())) {
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.remove(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaPagarVO().getNomeArquivo_Apresentar(), getControleCobrancaPagarVO().getUnidadeEnsinoVO().getCodigo()));
			}
			// System.out.println("JobProcessarAR Erro:" + e.getMessage());
			throw e;
		} finally {
			// controleCobrancaVO = null;			
			configuracaoFinanceiroSistemaVO = null;
			usuarioVO = null;
			configuracaoGeralSistemaVO = null;
			comunicacaoInternaVO = null;
			comunicadoInternoDestinatarioVO = null;
		}
		return getControleCobrancaPagarVO();
	}

	public void enviarEmail() throws Exception {
		if (getControleCobrancaPagarVO().getResponsavel().getPessoa() == null || getControleCobrancaPagarVO().getResponsavel().getPessoa().getCodigo().equals(0)) {
			return;
		} else {
			getConfiguracaoGeralSistemaVO().setConfiguracoesVO(getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoPadraoSemControleAcesso());
			setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoConfiguracoes(getConfiguracaoGeralSistemaVO().getConfiguracoesVO().getCodigo(), false, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
			getComunicacaoInternaVO().setResponsavel(getUsuarioVO().getPessoa());
			getComunicacaoInternaVO().setAssunto("Arquivo de Retorno");
			getComunicacaoInternaVO().setEnviarEmail(true);
			getComunicacaoInternaVO().setTipoDestinatario("FU");
			getComunicacaoInternaVO().setTipoMarketing(false);
			getComunicacaoInternaVO().setTipoLeituraObrigatoria(false);
			getComunicacaoInternaVO().setDigitarMensagem(true);
			getComunicacaoInternaVO().setRemoverCaixaSaida(false);
			getComunicacaoInternaVO().setMensagem("Arquivo: " + getControleCobrancaPagarVO().getNomeArquivo_Apresentar() + " - Situação: " + SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getDescricao());
			adicionarDestinatarios();
			if (!getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().isEmpty()) {
				getFacadeFactory().getComunicacaoInternaFacade().incluir(getComunicacaoInternaVO(), false, getUsuarioVO(), getConfiguracaoGeralSistemaVO(),null);
			}
		}
	}

	public void adicionarDestinatarios() throws Exception {
		getComunicadoInternoDestinatarioVO().setDestinatario(getUsuarioVO().getPessoa());
		getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno("LE");
		getComunicadoInternoDestinatarioVO().setDataLeitura(null);
		getComunicadoInternoDestinatarioVO().setCiJaRespondida(false);
		getComunicadoInternoDestinatarioVO().setCiJaLida(false);
		getComunicadoInternoDestinatarioVO().setRemoverCaixaEntrada(false);
		getComunicadoInternoDestinatarioVO().setMensagemMarketingLida(false);
		getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
	}

	public ControleCobrancaPagarVO getControleCobrancaPagarVO() {
		return controleCobrancaPagarVO;
	}

	public void setControleCobrancaPagarVO(ControleCobrancaPagarVO controleCobrancaPagarVO) {
		this.controleCobrancaPagarVO = controleCobrancaPagarVO;
	}

	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroSistemaVO() {
		return configuracaoFinanceiroSistemaVO;
	}

	public void setConfiguracaoFinanceiroSistemaVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroSistemaVO) {
		this.configuracaoFinanceiroSistemaVO = configuracaoFinanceiroSistemaVO;
	}

	

	public UsuarioVO getUsuarioVO() {
		return usuarioVO;
	}

	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	public ComunicadoInternoDestinatarioVO getComunicadoInternoDestinatarioVO() {
		return comunicadoInternoDestinatarioVO;
	}

	public void setComunicadoInternoDestinatarioVO(ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO) {
		this.comunicadoInternoDestinatarioVO = comunicadoInternoDestinatarioVO;
	}

	public ComunicacaoInternaVO getComunicacaoInternaVO() {
		return comunicacaoInternaVO;
	}

	public void setComunicacaoInternaVO(ComunicacaoInternaVO comunicacaoInternaVO) {
		this.comunicacaoInternaVO = comunicacaoInternaVO;
	}

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		return configuracaoGeralSistemaVO;
	}

	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}

}
