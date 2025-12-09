package negocio.comuns.arquitetura;

import java.io.File;
import java.io.Serializable;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import jakarta.faces. context.FacesContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.LoginControle;
import negocio.comuns.utilitarias.SpringUtil;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.FacadeManager;

public class SuperArquitetura extends FacadeManager implements Serializable {

    private UsuarioVO usuarioVO;
    private String ipMaquinaLogada;
    // private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
    public String diretorioPastaWeb;
    private static Logger logger;
    public static final long serialVersionUID = 1L;
    
    @Autowired
    private SpringUtil springUtil;
    
    public SpringUtil getSpringUtil() {
		return springUtil;
	}

	public void setSpringUtil(SpringUtil springUtil) {
		this.springUtil = springUtil;
	}

    /**
     * Método responsável por obter o usuário logado no sistema.
     */
    public UsuarioVO getUsuarioLogado() throws Exception {
        setUsuarioVO(getLoginControle().getUsuario());
        return getUsuarioVO();
    }
    
    public AplicacaoControle getAplicacaoControle() {
		return (AplicacaoControle) getSpringUtil().getApplicationContext().getBean(AplicacaoControle.class);
	}

    public LoginControle getLoginControle() throws Exception {
        LoginControle loginControle = (LoginControle) context().getExternalContext().getSessionMap().get("LoginControle");
        return loginControle;
    }

    protected FacesContext context() {
        return (FacesContext.getCurrentInstance());
    }

    protected String internacionalizar(String mensagem) {
        String propriedade = obterArquivoPropriedades(mensagem);
        ResourceBundle bundle = ResourceBundle.getBundle(propriedade, getLocale(), getCurrentLoader(propriedade));
        try {
            return bundle.getString(mensagem);
        } catch (MissingResourceException e) {
            return mensagem;
        }
    }

    public String obterArquivoPropriedades(String mensagem) {
        if (mensagem.startsWith("msg")) {
            return "propriedades.Mensagens";
        } else if (mensagem.startsWith("enum")) {
            return "propriedades.Enum";
        } else if (mensagem.startsWith("prt")) {
            return "propriedades.Aplicacao";
        } else if (mensagem.startsWith("menu")) {
            return "propriedades.Menu";
        } else if (mensagem.startsWith("btn")) {
            return "propriedades.Botoes";
        } else {
            return "propriedades.Mensagens";
        }
    }

    protected static ClassLoader getCurrentLoader(Object fallbackClass) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = fallbackClass.getClass().getClassLoader();
        }
        return loader;
    }

    protected String getMensagemInternalizacao(String mensagemID) {
        String mensagem = "(" + mensagemID + ") Mensagem não localizada nas propriedades de internalização.";
        ResourceBundle bundle = null;
        Locale locale = null;
        String nomeBundle = context().getApplication().getMessageBundle();
        if (nomeBundle != null) {
            locale = context().getViewRoot().getLocale();
            bundle = ResourceBundle.getBundle(nomeBundle, locale, getCurrentLoader(nomeBundle));
            try {
                mensagem = bundle.getString(mensagemID);
                return mensagem;
            } catch (MissingResourceException e) {
                return mensagem;
            }
        }
        return mensagem;
    }

    public Locale getLocale() {
        return context().getViewRoot().getLocale();
    }

    /**
     * @return the usuarioVO
     */
    public UsuarioVO getUsuarioVO() {
        if (usuarioVO == null) {
            usuarioVO = new UsuarioVO();
        }
        return usuarioVO;
    }

    /**
     * @param usuarioVO the usuarioVO to set
     */
    public void setUsuarioVO(UsuarioVO usuarioVO) {
        this.usuarioVO = usuarioVO;
    }

    /**
     * @return the configuracaoGeralSistemaVO
     */
    // public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
    // if (configuracaoGeralSistemaVO == null) {
    // configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
    // }
    // return configuracaoGeralSistemaVO;
    // }
    /**
     * @param configuracaoGeralSistemaVO the configuracaoGeralSistemaVO to set
     */
    // public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
    // this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
    // }
    /**
     * @return the ipMaquinaLogada
     */
    public String getIpMaquinaLogada() {
        if (ipMaquinaLogada == null) {
            ipMaquinaLogada = "";
        }
        return ipMaquinaLogada;
    }

    /**
     * @param ipMaquinaLogada the ipMaquinaLogada to set
     */
    public void setIpMaquinaLogada(String ipMaquinaLogada) {
        this.ipMaquinaLogada = ipMaquinaLogada;
    }

    /**
     * @return the logger
     */
    public static Logger getLogger() {
        if (logger == null) {
            logger = Logger.getLogger(SuperArquitetura.class);
        }
        return logger;
    }

    /**
     * @param aLogger the logger to set
     */
    public static void setLogger(Logger aLogger) {
        logger = aLogger;
    }

    public String getCaminhoPastaWeb() {
    	try {    		
        if (diretorioPastaWeb == null || diretorioPastaWeb.equals("")) {            
            	diretorioPastaWeb = UteisJSF.getCaminhoWeb();                       
        }
    	} catch (Exception e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
        return diretorioPastaWeb;
    }

    public String getCaminhoClassesAplicacao() throws Exception {
        String caminhoBaseAplicacaoPrm = getCaminhoPastaWeb() + File.separator + "WEB-INF" + File.separator + "classes";
        return caminhoBaseAplicacaoPrm;
    }
}
