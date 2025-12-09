/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

/**
 *
 * @author Danilo
 */
public class CursoCensoProfessorVO extends SuperVO {

    private String tipoRegistro;
    private String idCurso;
    public static final long serialVersionUID = 1L;

    public String getIdCurso() {
        if (idCurso == null) {
            idCurso = "";
        }
        return idCurso;
    }

    public void setIdCurso(String idCurso) {
        this.idCurso = idCurso;
    }

    public String getTipoRegistro() {
        if (tipoRegistro == null) {
            tipoRegistro = "";
        }
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }
}
