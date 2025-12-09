/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 *
 * @author Carlos
 */
public class TurmaAtualizacaoDisciplinaLogVO extends SuperVO {

    private Integer codigo;
    private Integer codigoTurma;
    private String identificadorTurma;
    private UsuarioVO responsavel;
    private Date data;
    private String disciplinasAdicionadas;
    public static final long serialVersionUID = 1L;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigoTurma() {
        if (codigoTurma == null) {
            codigoTurma = 0;
        }
        return codigoTurma;
    }

    public void setCodigoTurma(Integer codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public String getIdentificadorTurma() {
        if (identificadorTurma == null) {
            identificadorTurma = "";
        }
        return identificadorTurma;
    }

    public void setIdentificadorTurma(String identificadorTurma) {
        this.identificadorTurma = identificadorTurma;
    }

    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return responsavel;
    }

    public void setResponsavel(UsuarioVO responsavel) {
        this.responsavel = responsavel;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDisciplinasAdicionadas() {
        if (disciplinasAdicionadas == null) {
            disciplinasAdicionadas = "";
        }
        return disciplinasAdicionadas;
    }

    public void setDisciplinasAdicionadas(String disciplinasAdicionadas) {
        this.disciplinasAdicionadas = disciplinasAdicionadas;
    }
}
