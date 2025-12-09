package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;

public class ConfiguracaoLdapVO extends SuperVO {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5362348338591088648L;
	private Integer codigo;
    private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
    private String hostnameLdap;
    private String portaLdap;
    private String dnLdap;
    private String senhaLdap;
    private String dcLdap;
    private String formatoSenhaLdap;
    private String dominio;
    private String diretorio;
    private String grupo;
    private String prefixoSenha;
    private Boolean itemEmEdicao;
    private String urlLoginAD;
    private String urlLogoutAD;
    private String urlIdentificadorAD;
    private String diretorioInativacao;

    public ConfiguracaoLdapVO() {
        super();
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
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

    public String getHostnameLdap() {
        if (hostnameLdap == null) {
            hostnameLdap = "";
        }
        return hostnameLdap;
    }

    public void setHostnameLdap(String hostnameLdap) {
        this.hostnameLdap = hostnameLdap;
    }

    public String getPortaLdap() {
        if (portaLdap == null) {
            portaLdap = "";
        }
        return portaLdap;
    }

    public void setPortaLdap(String portaLdap) {
        this.portaLdap = portaLdap;
    }

    public String getDnLdap() {
        if (dnLdap == null) {
            dnLdap = "";
        }
        return dnLdap;
    }

    public void setDnLdap(String dnLdap) {
        this.dnLdap = dnLdap;
    }

    public String getSenhaLdap() {
        if (senhaLdap == null) {
            senhaLdap = "";
        }
        return senhaLdap;
    }

    public void setSenhaLdap(String senhaLdap) {
        this.senhaLdap = senhaLdap;
    }

    public String getDcLdap() {
        if (dcLdap == null) {
            dcLdap = "";
        }
        return dcLdap;
    }

    public void setDcLdap(String dcLdap) {
        this.dcLdap = dcLdap;
    }

    public String getFormatoSenhaLdap() {
        if (formatoSenhaLdap == null) {
            formatoSenhaLdap = "UTF-16LE";
        }
        return formatoSenhaLdap;
    }

    public void setFormatoSenhaLdap(String formatoSenhaLdap) {
        this.formatoSenhaLdap = formatoSenhaLdap;
    }
    
    public boolean isFormatoSenhaLdapUTF16LE() {
    	return getFormatoSenhaLdap().equals("UTF-16LE");
    }
    
    public boolean isFormatoSenhaLdapSHA1() {
    	return getFormatoSenhaLdap().equals("SHA");
    }
    
    public boolean isFormatoSenhaLdapSHA256() {
    	return getFormatoSenhaLdap().equals("SHA256");
    }
    
    public boolean isFormatoSenhaLdapMSCHAPV2() {
    	return getFormatoSenhaLdap().equals("MSCHAPV2");
    }
    

    public String getDominio() {
        if (dominio == null) {
            dominio = "";
        }
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public String getDiretorio() {
        if (diretorio == null) {
            diretorio = "";
        }
        return diretorio;
    }

    public void setDiretorio(String diretorio) {
        this.diretorio = diretorio;
    }

    public String getGrupo() {
    	 if (grupo == null) {
    		 grupo = "";
         }
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getPrefixoSenha() {
		 if (prefixoSenha == null) {
			 prefixoSenha = "";
         }
		return prefixoSenha;
	}

	public void setPrefixoSenha(String prefixoSenha) {
		this.prefixoSenha = prefixoSenha;
	}

	public Boolean getItemEmEdicao() {
    	if(itemEmEdicao == null) {
    		itemEmEdicao = false;
    	}
        return itemEmEdicao;
    }

    public void setItemEmEdicao(Boolean itemEmEdicao) {
        this.itemEmEdicao = itemEmEdicao;
    }

    public String getUrlLoginAD() {
        if (urlLoginAD == null) {
            urlLoginAD = "";
        }
        return urlLoginAD;
    }

    public void setUrlLoginAD(String urlLoginAD) {
        this.urlLoginAD = urlLoginAD;
    }

    public String getUrlLogoutAD() {
    	if (urlLogoutAD == null) {
    		urlLogoutAD = "";
        }
		return urlLogoutAD;
	}

	public void setUrlLogoutAD(String urlLogoutAD) {
		this.urlLogoutAD = urlLogoutAD;
	}

	public String getUrlIdentificadorAD() {
        if (urlIdentificadorAD == null) {
            urlIdentificadorAD = "";
        }
        return urlIdentificadorAD;
    }

    public void setUrlIdentificadorAD(String urlIdentificadorAD) {
        this.urlIdentificadorAD = urlIdentificadorAD;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((dominio == null) ? 0 : dominio.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfiguracaoLdapVO other = (ConfiguracaoLdapVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (dominio == null) {
			if (other.dominio != null)
				return false;
		} else if (!dominio.equals(other.dominio))
			return false;
		return true;
	}

    public String getDiretorioInativacao() {
        if (diretorioInativacao == null) {
            diretorioInativacao = "";
        }
        return diretorioInativacao;
    }

    public void setDiretorioInativacao(String diretorioInativacao) {
        this.diretorioInativacao = diretorioInativacao;
    }
}
