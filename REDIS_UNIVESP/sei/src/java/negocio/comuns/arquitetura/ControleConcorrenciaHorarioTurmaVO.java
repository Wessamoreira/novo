/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.arquitetura;

import java.util.Date;

/**
 *
 * @author Carlos
 */
public class ControleConcorrenciaHorarioTurmaVO extends SuperVO {

    private Integer codigoProfessor;
    private String nomeProfessor;
    private String nomeMetodo;
    private UsuarioVO usuarioVO;
    private Date data;
    public static final long serialVersionUID = 1L;

    public ControleConcorrenciaHorarioTurmaVO() {
    }

    /**
     * @return the codigoProfessor
     */
    public Integer getCodigoProfessor() {
        if (codigoProfessor == null) {
            codigoProfessor = 0;
        }
        return codigoProfessor;
    }

    /**
     * @param codigoProfessor the codigoProfessor to set
     */
    public void setCodigoProfessor(Integer codigoProfessor) {
        this.codigoProfessor = codigoProfessor;
    }

    /**
     * @return the nomeMetodo
     */
    public String getNomeMetodo() {
        if (nomeMetodo == null) {
            nomeMetodo = "";
        }
        return nomeMetodo;
    }

    /**
     * @param nomeMetodo the nomeMetodo to set
     */
    public void setNomeMetodo(String nomeMetodo) {
        this.nomeMetodo = nomeMetodo;
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
     * @return the data
     */
    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @return the nomeProfessor
     */
    public String getNomeProfessor() {
        if (nomeProfessor == null) {
            nomeProfessor = "";
        }
        return nomeProfessor;
    }

    /**
     * @param nomeProfessor the nomeProfessor to set
     */
    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }
}
