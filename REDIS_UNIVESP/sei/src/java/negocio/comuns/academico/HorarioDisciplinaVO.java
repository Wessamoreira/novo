package negocio.comuns.academico;

import java.io.Serializable;
import java.util.Date;

import negocio.comuns.utilitarias.dominios.DiaSemana;

public class HorarioDisciplinaVO implements Serializable {

    private Integer professor;
    private DiaSemana diaSemana;
    private Integer nrAula;
    private String horario;
    private String tipoHorario;
    private Date dia;
    public static final long serialVersionUID = 1L;

    public HorarioDisciplinaVO() {
        incializarDados();
    }

    public void incializarDados() {
        setDiaSemana(DiaSemana.DOMINGO);
        setNrAula(0);
        setProfessor(0);
        setHorario("");
        setTipoHorario("");
        setDia(new Date());
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public String getTipoHorario() {
        return tipoHorario;
    }

    public void setTipoHorario(String tipoHorario) {
        this.tipoHorario = tipoHorario;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Integer getNrAula() {
        return nrAula;
    }

    public void setNrAula(Integer nrAula) {
        this.nrAula = nrAula;
    }

    public Integer getProfessor() {
        return professor;
    }

    public void setProfessor(Integer professor) {
        this.professor = professor;
    }
}
