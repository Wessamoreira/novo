package relatorio.negocio.comuns.academico;

import java.util.Date;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Carlos
 */
public class AberturaTurmaRelVO {

    private Date inauguracao;
    private String curso;
    private String turma;
    private String unidadeEnsino;
    private String situacao;
    private Integer numeroMatriculado;

    public Date getInauguracao() {
        return inauguracao;
    }

    public String getInauguracao_Apresentar() {
        return Uteis.getData(inauguracao);
    }

    public void setInauguracao(Date inauguracao) {
        this.inauguracao = inauguracao;
    }

    public String getCurso() {
        if (curso == null) {
            curso = "";
        }
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getTurma() {
        if (turma == null) {
            turma = "";
        }
        return turma;
    }

    public void setTurma(String turma) {
        this.turma = turma;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    public String getSituacao_Apresentar() {
        if (getSituacao().equals("AC")) {
            return "A CONFIRMAR";
        }
        if (getSituacao().equals("AD")) {
            return "ADIADA";
        }
        if (getSituacao().equals("CO")) {
            return "CONFIRMADA";
        }
        if (getSituacao().equals("IN")) {
            return "INAUGURADA";
        }
        if (getSituacao().equals("CA")) {
            return "CANCELADA";
        }
        return (getSituacao());
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = "";
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(String unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public Integer getNumeroMatriculado() {
        if (numeroMatriculado == null) {
            numeroMatriculado = 0;
        }
        return numeroMatriculado;
    }

    public void setNumeroMatriculado(Integer numeroMatriculado) {
        this.numeroMatriculado = numeroMatriculado;
    }
}
