/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.DiaSemana;

/**
 *
 * @author Otimize-Not
 */
public class TurnoHorarioVO extends SuperVO {

    private Integer codigo;
    private Integer turno;
    private Integer numeroAula;
    private String horarioInicioAula;
    private String horarioFinalAula;
    private Integer duracaoAula;
    private DiaSemana diaSemana;
    private Boolean intervalo;
    private StringBuilder descricao;
    public static final long serialVersionUID = 1L;

    public String getDescricaoHorario() {
        if (descricao == null) {
            descricao = new StringBuilder();
            return descricao.append(numeroAula).append("ª Aula (").append(horarioInicioAula).append(" até ").append(horarioFinalAula).append(")").toString();
        }
        return descricao.toString();
    }

    public Boolean getIntervalo() {
        if (intervalo == null) {
            intervalo = false;
        }
        return intervalo;
    }

    public void setIntervalo(Boolean intervalo) {
        this.intervalo = intervalo;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
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

    public Integer getDuracaoAula() {
        if (duracaoAula == null) {
            duracaoAula = 0;
        }
        return duracaoAula;
    }

    public void setDuracaoAula(Integer duracaoAula) {
        this.duracaoAula = duracaoAula;
    }

    public String getHorarioFinalAula() {
        if (horarioFinalAula == null) {
            horarioFinalAula = "";
        }
        return horarioFinalAula;
    }

    public void setHorarioFinalAula(String horarioFinalAula) {
        this.horarioFinalAula = horarioFinalAula;
    }

    public String getHorarioInicioAula() {
        if (horarioInicioAula == null) {
            horarioInicioAula = "";
        }
        return horarioInicioAula;
    }

    public void setHorarioInicioAula(String horarioInicioAula) {
        this.horarioInicioAula = horarioInicioAula;
    }

    public Integer getNumeroAula() {
        if (numeroAula == null) {
            numeroAula = 0;
        }
        return numeroAula;
    }

    public void setNumeroAula(Integer numeroAula) {
        this.numeroAula = numeroAula;
    }

    public Integer getTurno() {
        if (turno == null) {
            turno = 0;
        }
        return turno;
    }

    public void setTurno(Integer turno) {
        this.turno = turno;
    }
}
