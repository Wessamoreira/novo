/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import java.io.Serializable;

import negocio.comuns.academico.enumeradores.HorarioAlunoProfessorTurmaEnum;
import negocio.comuns.basico.PessoaVO;

/**
 *
 * @author Otimize-Not
 */
public class CalendarioHorarioAulaHoraVO implements Serializable {

    private Integer nrAula;
    private String horario;
    private HorarioAlunoProfessorTurmaEnum horarioAlunoProfessorTurma;
    private PessoaVO professor;
    private DisciplinaVO disciplina;
    private TurmaVO turma;
    public static final long serialVersionUID = 1L;

    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }

    public String getHorario() {
        if (horario == null) {
            horario = "";
        }
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public HorarioAlunoProfessorTurmaEnum getHorarioAlunoProfessorTurma() {
        if (horarioAlunoProfessorTurma == null) {
            horarioAlunoProfessorTurma = HorarioAlunoProfessorTurmaEnum.TURMA;
        }
        return horarioAlunoProfessorTurma;
    }

    public void setHorarioAlunoProfessorTurma(HorarioAlunoProfessorTurmaEnum horarioAlunoProfessorTurma) {
        this.horarioAlunoProfessorTurma = horarioAlunoProfessorTurma;
    }

    public Integer getNrAula() {
        if (nrAula == null) {
            nrAula = 0;
        }
        return nrAula;
    }

    public void setNrAula(Integer nrAula) {
        this.nrAula = nrAula;
    }

    public PessoaVO getProfessor() {
        if (professor == null) {
            professor = new PessoaVO();
        }
        return professor;
    }

    public void setProfessor(PessoaVO professor) {
        this.professor = professor;
    }

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public String getDadosHorario() {
        if (getHorarioAlunoProfessorTurma().equals(HorarioAlunoProfessorTurmaEnum.TURMA)
                && !getProfessor().getNome().trim().isEmpty() && !getDisciplina().getNome().trim().isEmpty()) {
            return "Prof:" + getProfessor().getNome() + "\nDisc: " + getDisciplina().getNome();
        }
        if (getHorarioAlunoProfessorTurma().equals(HorarioAlunoProfessorTurmaEnum.ALUNO)
                && !getProfessor().getNome().trim().isEmpty() && !getDisciplina().getNome().trim().isEmpty()
                && !getTurma().getIdentificadorTurma().trim().isEmpty()) {
            return "Turma: " + getTurma().getIdentificadorTurma() + "\nProf: " + getProfessor().getNome() + "\nDisc: " + getDisciplina().getNome();
        }
        if (getHorarioAlunoProfessorTurma().equals(HorarioAlunoProfessorTurmaEnum.PROFESSOR)
                && !getTurma().getIdentificadorTurma().trim().isEmpty() && !getDisciplina().getNome().trim().isEmpty()) {
            return "Turma: " + getTurma().getIdentificadorTurma() + "\nDisc: " + getDisciplina().getNome();
        }
        return "";
    }
}
