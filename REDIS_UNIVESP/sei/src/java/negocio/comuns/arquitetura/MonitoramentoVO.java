package negocio.comuns.arquitetura;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Reponsável por manter os dados da entidade Usuario. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class MonitoramentoVO extends SuperVO {

    private Integer codigoUsuario;
    private String nomeUsuario;
    private List<String> acoes;
    private Date dataUltimoAcesso;
    public static final long serialVersionUID = 1L;

    public MonitoramentoVO() {
        super();
    }

    /**
     * @return the nomeUsuario
     */
    public String getNomeUsuario() {
        return nomeUsuario;
    }

    /**
     * @param nomeUsuario the nomeUsuario to set
     */
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    /**
     * @return the dataUltimoAcesso
     */
    public Date getDataUltimoAcesso() {
        return dataUltimoAcesso;
    }

    /**
     * @param dataUltimoAcesso the dataUltimoAcesso to set
     */
    public void setDataUltimoAcesso(Date dataUltimoAcesso) {
        this.dataUltimoAcesso = dataUltimoAcesso;
    }

    /**
     * @return the acoes
     */
    public List<String> getAcoes() {
        if (acoes == null) {
            acoes = new ArrayList();
        }
        return acoes;
    }

    /**
     * @param acoes the acoes to set
     */
    public void setAcoes(List<String> acoes) {
        this.acoes = acoes;
    }

    /**
     * @return the codigoUsuario
     */
    public Integer getCodigoUsuario() {
        return codigoUsuario;
    }

    /**
     * @param codigoUsuario the codigoUsuario to set
     */
    public void setCodigoUsuario(Integer codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }
}
