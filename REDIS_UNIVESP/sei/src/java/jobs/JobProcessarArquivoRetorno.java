package jobs;

import java.util.Date;
import java.util.concurrent.Callable;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.financeiro.RegistroArquivoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoArquivoRetorno;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

public class JobProcessarArquivoRetorno extends ControleAcesso implements Callable<Object> {

    private ControleCobrancaVO controleCobrancaVO;
    private ConfiguracaoFinanceiroVO configuracaoFinanceiroSistemaVO;
    private RegistroArquivoVO registroArquivoVO;
    private String caminhoPastaArquivosCobranca;
    private UsuarioVO usuarioVO;
    private ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO;
    private ComunicacaoInternaVO comunicacaoInternaVO;
    private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;

    public JobProcessarArquivoRetorno(ControleCobrancaVO controleCobranca, String caminhoPastaArquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroSistemaVO, UsuarioVO usuario) {
        this.controleCobrancaVO = controleCobranca;
        this.caminhoPastaArquivosCobranca = caminhoPastaArquivo;
        this.configuracaoFinanceiroSistemaVO = configuracaoFinanceiroSistemaVO;
        this.usuarioVO = usuario;
    }

    public Object call() throws Exception {
        try {
        	if(getControleCobrancaVO().getRegistroArquivoVO().getCodigo() > 0){
        		getFacadeFactory().getRegistroArquivoFacade().excluir(getControleCobrancaVO().getRegistroArquivoVO(), false, getUsuarioVO());
        		getControleCobrancaVO().getRegistroArquivoVO().setCodigo(0);
        	}
            setRegistroArquivoVO(getFacadeFactory().getControleCobrancaFacade().processarArquivo(getControleCobrancaVO(), getCaminhoPastaArquivosCobranca(), getConfiguracaoFinanceiroSistemaVO(), getUsuarioVO()));
			if (getRegistroArquivoVO().getRegistroDetalheVOs().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_ControleCobranca_nenhumaContaParaBaixa"));
			}
			if (!getControleCobrancaVO().getRegistroArquivoVO().getRegistroHeader().getLinhaHeader().equals("")) {
				Integer codigoArquivoProcessadoExistente = getFacadeFactory().getRegistroHeaderFacade().verificarHeaderArquivoProcessado(getRegistroArquivoVO().getRegistroHeader().getLinhaHeader(), getRegistroArquivoVO().getCodigo());
				if (codigoArquivoProcessadoExistente > 0) {
					getControleCobrancaVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs().clear();
					getControleCobrancaVO().getRegistroArquivoVO().getRegistroDetalheVOs().clear();
					throw new Exception(UteisJSF.internacionalizar("msg_ControleCobranca_arquivoJaProcessado") + codigoArquivoProcessadoExistente);
				}
			}
			getControleCobrancaVO().setContaCorrenteVO(getRegistroArquivoVO().getContaCorrenteVO());
            if (getControleCobrancaVO().isNovoObj().booleanValue()) {
                getControleCobrancaVO().setDataProcessamento(new Date());
                getFacadeFactory().getControleCobrancaFacade().incluirSemBaixarContas(getControleCobrancaVO(), getRegistroArquivoVO(), getUsuarioVO());
            } else {
            	if(getRegistroArquivoVO().getCodigo() == 0){
            		getFacadeFactory().getRegistroArquivoFacade().incluir(getRegistroArquivoVO(), false, getUsuarioVO());
            		getControleCobrancaVO().setRegistroArquivoVO(getRegistroArquivoVO());
            	}
                getFacadeFactory().getControleCobrancaFacade().alterar(getControleCobrancaVO(),getUsuarioVO());
            }
            
//            Uteis.ARQUIVOS_CONTROLE_COBRANCA.remove(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo()));
//            Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor());
            getControleCobrancaVO().setRegistroArquivoVO(getRegistroArquivoVO());
            enviarEmail();
        } catch (Exception e) {
            if (Uteis.ARQUIVOS_CONTROLE_COBRANCA.containsKey(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo())) && !Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor())) {
                Uteis.ARQUIVOS_CONTROLE_COBRANCA.remove(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo()));
            }
            //System.out.println("JobProcessarAR Erro:" + e.getMessage());
            throw e;
        } finally {
            //controleCobrancaVO = null;
            caminhoPastaArquivosCobranca = null;
            configuracaoFinanceiroSistemaVO = null;
            usuarioVO = null;
            configuracaoGeralSistemaVO = null;
            comunicacaoInternaVO = null;
            comunicadoInternoDestinatarioVO = null;
        }
        return getControleCobrancaVO();
    }

    public void enviarEmail() {
        try {
            if (getControleCobrancaVO().getResponsavel().getPessoa() == null || getControleCobrancaVO().getResponsavel().getPessoa().getCodigo().equals(0)) {
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
                getComunicacaoInternaVO().setMensagem("Arquivo: " + getControleCobrancaVO().getNomeArquivo() + " - Situação: " + SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getDescricao());
                adicionarDestinatarios();
//                getUsuarioResponsavelEnviarMensagemProfessorVO().setPessoa(comunicacaoInternaVO.getResponsavel());
//                setUsuarioResponsavelEnviarMensagemProfessorVO(getUsuarioFacade().consultarPorPessoa(getUsuarioResponsavelEnviarMensagemProfessorVO().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null));
//                getComunicacaoInternaVO().setComunicadoInternoDestinatarioVOs(null);
                if (!getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().isEmpty()) {
                    getFacadeFactory().getComunicacaoInternaFacade().incluir(getComunicacaoInternaVO(), false, getUsuarioVO(), getConfiguracaoGeralSistemaVO(),null);
                }
            }
        } catch (Exception e) {
            //System.out.println("JobProcessarAr Erro:" + e.getMessage());
        }
    }

    public void adicionarDestinatarios() throws Exception {
        try {
            getComunicadoInternoDestinatarioVO().setDestinatario(getUsuarioVO().getPessoa());
            getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno("LE");
            getComunicadoInternoDestinatarioVO().setDataLeitura(null);
            getComunicadoInternoDestinatarioVO().setCiJaRespondida(false);
            getComunicadoInternoDestinatarioVO().setCiJaLida(false);
            getComunicadoInternoDestinatarioVO().setRemoverCaixaEntrada(false);
            getComunicadoInternoDestinatarioVO().setMensagemMarketingLida(false);
            getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
        } catch (Exception e) {
            //System.out.println("JobProcessarAr Erro:" + e.getMessage());
        }
    }

    public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroSistemaVO() {
        if (configuracaoFinanceiroSistemaVO == null) {
            configuracaoFinanceiroSistemaVO = new ConfiguracaoFinanceiroVO();
        }
        return configuracaoFinanceiroSistemaVO;
    }

    public void setConfiguracaoFinanceiroSistemaVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroSistemaVO) {
        this.configuracaoFinanceiroSistemaVO = configuracaoFinanceiroSistemaVO;
    }

    public String getCaminhoPastaArquivosCobranca() {
        if (caminhoPastaArquivosCobranca == null) {
            caminhoPastaArquivosCobranca = "";
        }
        return caminhoPastaArquivosCobranca;
    }

    public void setCaminhoPastaArquivosCobranca(String caminhoPastaArquivosCobranca) {
        this.caminhoPastaArquivosCobranca = caminhoPastaArquivosCobranca;
    }

    public RegistroArquivoVO getRegistroArquivoVO() {
        if (registroArquivoVO == null) {
            registroArquivoVO = new RegistroArquivoVO();
        }
        return registroArquivoVO;
    }

    public void setRegistroArquivoVO(RegistroArquivoVO registroArquivoVO) {
        this.registroArquivoVO = registroArquivoVO;
    }

    public UsuarioVO getUsuarioVO() {
        if (usuarioVO == null) {
            usuarioVO = new UsuarioVO();
        }
        return usuarioVO;
    }

    public void setUsuarioVO(UsuarioVO usuarioVO) {
        this.usuarioVO = usuarioVO;
    }

    public ControleCobrancaVO getControleCobrancaVO() {
        if (controleCobrancaVO == null) {
            controleCobrancaVO = new ControleCobrancaVO();
        }
        return controleCobrancaVO;
    }

    public void setControleCobrancaVO(ControleCobrancaVO controleCobrancaVO) {
        this.controleCobrancaVO = controleCobrancaVO;
    }

    public ComunicacaoInternaVO getComunicacaoInternaVO() {
        if (comunicacaoInternaVO == null) {
            comunicacaoInternaVO = new ComunicacaoInternaVO();
        }
        return comunicacaoInternaVO;
    }

    public void setComunicacaoInternaVO(ComunicacaoInternaVO comunicacaoInternaVO) {
        this.comunicacaoInternaVO = comunicacaoInternaVO;
    }

    public ComunicadoInternoDestinatarioVO getComunicadoInternoDestinatarioVO() {
        if (comunicadoInternoDestinatarioVO == null) {
            comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
        }
        return comunicadoInternoDestinatarioVO;
    }

    public void setComunicadoInternoDestinatarioVO(ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO) {
        this.comunicadoInternoDestinatarioVO = comunicadoInternoDestinatarioVO;
    }

    public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
        if (configuracaoGeralSistemaVO == null) {
            configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
        }
        return configuracaoGeralSistemaVO;
    }

    public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
        this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
    }
}
