package negocio.comuns.arquitetura;

import negocio.comuns.utilitarias.Constantes;

public class ConfiguracaoSamlVO extends SuperVO {

    public static final long serialVersionUID = 1L;
    private Integer codigoConfiguracaoSistema;
    private Integer codigo;
    private String identificador;
    private String dominio;
    private String urlLogon;
    private String urlLogin;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDominio() {
        if (dominio == null) {
            dominio = Constantes.EMPTY;
        }
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public String getUrlLogon() {
        if (urlLogon == null) {
            urlLogon = Constantes.EMPTY;
        }
        return urlLogon;
    }

    public void setUrlLogon(String urlLogon) {
        this.urlLogon = urlLogon;
    }

    public String getUrlLogin() {
        if (urlLogin == null) {
            urlLogin = Constantes.EMPTY;
        }
        return urlLogin;
    }

    public void setUrlLogin(String urlLogin) {
        this.urlLogin = urlLogin;
    }

    public String getIdentificador() {
        if (identificador == null) {
            identificador = Constantes.EMPTY;
        }
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Integer getCodigoConfiguracaoSistema() {
        if (codigoConfiguracaoSistema == null) {
            codigoConfiguracaoSistema = 0;
        }
        return codigoConfiguracaoSistema;
    }

    public void setCodigoConfiguracaoSistema(Integer codigoConfiguracaoSistema) {
        this.codigoConfiguracaoSistema = codigoConfiguracaoSistema;
    }
}
