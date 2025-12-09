package controle.arquitetura;

/**
 * Classe responsável por implementar a interação entre os componentes JSF da
 * página ajudaCons.jsp com as funcionalidades da classe
 * <code>ArtefatoAjudaVO</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @author Paulo Taucci
 * @see SuperControle
 * @see ArtefatoAjuda
 * @see ArtefatoAjudaVO
 */
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.arquitetura.FavoritoVO;
import negocio.comuns.arquitetura.PermissaoAcessoMenuVO;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;

@Controller("FavoritoControle")
@Scope("session")
@Lazy
public class FavoritoControle extends SuperControle implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1236885753738663547L;
	private String pagina;
    private String booleanMarcarFavoritar;
    private String scriptExecutar;
    private String propertMenu;
    private TipoVisaoEnum tipoVisao;
    private String icone;
    private String removerControlador;

    public FavoritoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    @SuppressWarnings("rawtypes")
	public void favoritar() {
        try {
            LoginControle login = (LoginControle) getControlador("LoginControle");
            PermissaoAcessoMenuVO permissaoAcessoMenuVO = new PermissaoAcessoMenuVO();
            if (login != null) {
                permissaoAcessoMenuVO = login.getPermissaoAcessoMenuVO();
                try {
                    Class permissaoMenu = Class.forName(permissaoAcessoMenuVO.getClass().getName());
                    @SuppressWarnings("unchecked")
					Method metodoSet = permissaoMenu.getMethod("set" + getBooleanMarcarFavoritar(), Boolean.class);
                    metodoSet.invoke(permissaoAcessoMenuVO, Boolean.TRUE);
                } catch (Exception e) {
                    //System.out.println("Erro:" + e.getMessage());
                }
            }
            FavoritoVO fav = new FavoritoVO();
            fav.setPagina(getPagina());
            fav.setBooleanMarcarFavoritar(getBooleanMarcarFavoritar());
            fav.setScriptExecutar(getScriptExecutar());
            fav.setPropertMenu(getPropertMenu());
            fav.setUsuario(getUsuarioLogado().getCodigo());
            fav.setTipoVisao(getTipoVisao());
            fav.setIcone(getIcone());
            fav.setRemoverControlador(getRemoverControlador());
            getFacadeFactory().getFavoritoFacade().incluir(fav, false, getUsuarioLogado());
            getUsuarioLogado().getListaFavoritos().add(fav);
            Ordenacao.ordenarLista(getUsuarioLogado().getListaFavoritos(), "propertMenu");
        } catch (Exception e) {
        	setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    @SuppressWarnings("rawtypes")
    public void desfavoritar() {
        try {
            LoginControle login = (LoginControle) getControlador("LoginControle");            
            if (login != null) {                
                try {
                    Class permissaoMenu = Class.forName(login.getPermissaoAcessoMenuVO().getClass().getName());
                    @SuppressWarnings("unchecked")
                    Method metodoSet = permissaoMenu.getMethod("set" + getBooleanMarcarFavoritar(), Boolean.class);
                    metodoSet.invoke(login.getPermissaoAcessoMenuVO(), Boolean.FALSE);
                } catch (Exception e) {
                    //System.out.println("Erro:" + e.getMessage());
                }
            }
            FavoritoVO fav = new FavoritoVO();
            fav.setPagina(getPagina());
            fav.setBooleanMarcarFavoritar(getBooleanMarcarFavoritar());
            fav.setScriptExecutar(getScriptExecutar());
            fav.setUsuario(getUsuarioLogado().getCodigo());
            fav.setTipoVisao(getTipoVisao());
            //FavoritoVO fav2 = getFacadeFactory().getFavoritoFacade().consultarPorPagina(getPagina(), getUsuarioLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            Iterator<FavoritoVO> i = getUsuarioLogado().getListaFavoritos().iterator();
            while (i.hasNext()) {
                FavoritoVO favorito = (FavoritoVO)i.next();
                if (favorito.getPagina().equals(getPagina())) {
                    getUsuarioLogado().getListaFavoritos().remove(favorito);
                    break;
                }
            }            
            getFacadeFactory().getFavoritoFacade().excluir(fav);
        } catch (Exception e) {
        	setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    @SuppressWarnings("rawtypes")
    public void desfavoritarLista() {
        try {
            
            LoginControle login = (LoginControle) getControlador("LoginControle");
            PermissaoAcessoMenuVO permissaoAcessoMenuVO = new PermissaoAcessoMenuVO();
            if (login != null) {
                permissaoAcessoMenuVO = login.getPermissaoAcessoMenuVO();
                try {
                    Class permissaoMenu = Class.forName(permissaoAcessoMenuVO.getClass().getName());
                    @SuppressWarnings("unchecked")
                    Method metodoSet = permissaoMenu.getMethod("set" + getBooleanMarcarFavoritar(), Boolean.class);
                    metodoSet.invoke(permissaoAcessoMenuVO, Boolean.FALSE);
                } catch (Exception e) {
                    //System.out.println("Erro:" + e.getMessage());
                }
            }
            FavoritoVO fav = new FavoritoVO();
            fav.setPagina(getPagina());
            fav.setBooleanMarcarFavoritar(getBooleanMarcarFavoritar());
            fav.setScriptExecutar(getScriptExecutar());
            fav.setUsuario(getUsuarioLogado().getCodigo());
            fav.setTipoVisao(getTipoVisao());
            //FavoritoVO fav2 = getFacadeFactory().getFavoritoFacade().consultarPorPagina(getPagina(), getUsuarioLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            Iterator<FavoritoVO> i = getUsuarioLogado().getListaFavoritos().iterator();
            while (i.hasNext()) {
                FavoritoVO favorito = (FavoritoVO)i.next();
                if (favorito.getPagina().equals(getPagina())) {
                    getUsuarioLogado().getListaFavoritos().remove(favorito);
                    break;
                }
            }
            getFacadeFactory().getFavoritoFacade().excluir(fav);
        } catch (Exception e) {
            //System.out.println("Erro:" + e.getMessage());
        }
    }

    /**
     * @return the pagina
     */
    public String getPagina() {
        if (pagina == null) {
            pagina = "";
        }
        return pagina;
    }

    /**
     * @param pagina the pagina to set
     */
    public void setPagina(String pagina) {
        this.pagina = pagina;
    }

    /**
     * @return the booleanMarcarFavoritar
     */
    public String getBooleanMarcarFavoritar() {
        if (booleanMarcarFavoritar == null) {
            booleanMarcarFavoritar = "";
        }
        return booleanMarcarFavoritar;
    }

    /**
     * @param booleanMarcarFavoritar the booleanMarcarFavoritar to set
     */
    public void setBooleanMarcarFavoritar(String booleanMarcarFavoritar) {
        this.booleanMarcarFavoritar = booleanMarcarFavoritar;
    }

    /**
     * @return the scriptExecutar
     */
    public String getScriptExecutar() {
        if (scriptExecutar == null) {
            scriptExecutar = "";
        }
        return scriptExecutar;
    }

    /**
     * @param scriptExecutar the scriptExecutar to set
     */
    public void setScriptExecutar(String scriptExecutar) {
        this.scriptExecutar = scriptExecutar;
    }

    /**
     * @return the propertMenu
     */
    public String getPropertMenu() {
        if (propertMenu == null) {
            propertMenu = "";
        }
        return propertMenu;
    }

    /**
     * @param propertMenu the propertMenu to set
     */
    public void setPropertMenu(String propertMenu) {
        this.propertMenu = propertMenu;
    }

	public TipoVisaoEnum getTipoVisao() {
		if(tipoVisao == null) {
			tipoVisao =  TipoVisaoEnum.ADMINISTRATIVA;
		}
		return tipoVisao;
	}

	public void setTipoVisao(TipoVisaoEnum tipoVisao) {
		this.tipoVisao = tipoVisao;
	}

	public String getIcone() {
		if(icone == null) {
			icone = "fa fa-star iconeDesfavoritar";
		}
		return icone;
	}

	public void setIcone(String icone) {
		this.icone = icone;
	}

	public String getRemoverControlador() {
		if(removerControlador == null) {
			removerControlador = "";
		}
		return removerControlador;
	}

	public void setRemoverControlador(String removerControlador) {
		this.removerControlador = removerControlador;
	}
    
    
}
