package negocio.comuns.arquitetura;

import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;

/**
 * Reponsável por manter os dados da entidade Usuario. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class FavoritoVO extends SuperVO {

    private Integer codigo;
    private String pagina;
    private String booleanMarcarFavoritar;
    private String scriptExecutar;
    private String propertMenu;
    private Integer usuario;
    private TipoVisaoEnum tipoVisao;
    private String icone;
    private String removerControlador;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Usuario</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public FavoritoVO() {
        super();
    }

    /**
     * @return the codigo
     */
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
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
     * @return the usuario
     */
    public Integer getUsuario() {
        if (usuario == null) {
            usuario = 0;
        }
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Integer usuario) {
        this.usuario = usuario;
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