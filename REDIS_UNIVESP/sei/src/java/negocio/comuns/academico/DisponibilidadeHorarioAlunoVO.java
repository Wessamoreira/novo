package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;

public class DisponibilidadeHorarioAlunoVO extends SuperVO {

    private String horario;
    private PessoaVO professorSegunda;
    private PessoaVO professorTerca;
    private PessoaVO professorQuarta;
    private PessoaVO professorQuinta;
    private PessoaVO professorSexta;
    private PessoaVO professorSabado;
    private PessoaVO professorDomingo;
    private TurmaVO turmaSegunda;
    private TurmaVO turmaTerca;
    private TurmaVO turmaQuarta;
    private TurmaVO turmaQuinta;
    private TurmaVO turmaSexta;
    private TurmaVO turmaSabado;
    private TurmaVO turmaDomingo;
    private DisciplinaVO disciplinaSegunda;
    private DisciplinaVO disciplinaTerca;
    private DisciplinaVO disciplinaQuarta;
    private DisciplinaVO disciplinaQuinta;
    private DisciplinaVO disciplinaSexta;
    private DisciplinaVO disciplinaSabado;
    private DisciplinaVO disciplinaDomingo;
    private Integer nrAula;
    private String diaSemana;
    public static final long serialVersionUID = 1L;

    public DisponibilidadeHorarioAlunoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setNrAula(new Integer(0));
        setDiaSemana("");
    }

    public String getProfessorTurmaSegunda() {
        String x = "Professor: " + getProfessorSegunda().getNome() + "\nTurma: "
                + getTurmaSegunda().getIdentificadorTurma();
        return x;
    }

    public Boolean verificaDisponibilidadeAluno(String diaSemana, DisciplinaVO disciplina, TurmaVO turma, Integer professor) {
        if (diaSemana.equals("01")) {
            return verificaDisponibilidadeAlunoDomingo(disciplina, turma, professor);
        }
        if (diaSemana.equals("02")) {
            return verificaDisponibilidadeAlunoSegunda(disciplina, turma, professor);
        }
        if (diaSemana.equals("03")) {
            return verificaDisponibilidadeAlunoTerca(disciplina, turma, professor);
        }
        if (diaSemana.equals("04")) {
            return verificaDisponibilidadeAlunoQuarta(disciplina, turma, professor);
        }
        if (diaSemana.equals("05")) {
            return verificaDisponibilidadeAlunoQuinta(disciplina, turma, professor);
        }
        if (diaSemana.equals("06")) {
            return verificaDisponibilidadeAlunoSexta(disciplina, turma, professor);
        }
        if (diaSemana.equals("07")) {
            return verificaDisponibilidadeAlunoSabado(disciplina, turma, professor);
        }
        return false;
    }

    public Boolean verificaDisponibilidadeAlunoDomingo(DisciplinaVO disciplina, TurmaVO turma, Integer professor) {

        if (getDisciplinaDomingo().getCodigo().intValue() != 0) {
            return false;
        }
        setDisciplinaDomingo(disciplina);
        setTurmaDomingo(turma);
        getProfessorDomingo().setCodigo(professor);
        return true;

    }

    public Boolean verificaDisponibilidadeAlunoSegunda(DisciplinaVO disciplina, TurmaVO turma, Integer professor) {

        if (getDisciplinaSegunda().getCodigo().intValue() != 0) {
            return false;
        }
        setDisciplinaSegunda(disciplina);
        setTurmaSegunda(turma);
        getProfessorSegunda().setCodigo(professor);
        return true;
    }

    public Boolean verificaDisponibilidadeAlunoTerca(DisciplinaVO disciplina, TurmaVO turma, Integer professor) {

        if (getDisciplinaTerca().getCodigo().intValue() != 0) {
            return false;
        }
        setDisciplinaTerca(disciplina);
        setTurmaTerca(turma);
        getProfessorTerca().setCodigo(professor);
        return true;
    }

    public Boolean verificaDisponibilidadeAlunoQuarta(DisciplinaVO disciplina, TurmaVO turma, Integer professor) {

        if (getDisciplinaQuarta().getCodigo().intValue() != 0) {
            return false;
        }
        setDisciplinaQuarta(disciplina);
        setTurmaQuarta(turma);
        getProfessorQuarta().setCodigo(professor);
        return true;
    }

    public Boolean verificaDisponibilidadeAlunoQuinta(DisciplinaVO disciplina, TurmaVO turma, Integer professor) {

        if (getDisciplinaQuinta().getCodigo().intValue() != 0) {
            return false;
        }
        setDisciplinaQuinta(disciplina);
        setTurmaQuinta(turma);
        getProfessorQuinta().setCodigo(professor);
        return true;
    }

    public Boolean verificaDisponibilidadeAlunoSexta(DisciplinaVO disciplina, TurmaVO turma, Integer professor) {

        if (getDisciplinaSexta().getCodigo().intValue() != 0) {
            return false;
        }
        setDisciplinaSexta(disciplina);
        setTurmaSexta(turma);
        getProfessorSexta().setCodigo(professor);
        return true;
    }

    public Boolean verificaDisponibilidadeAlunoSabado(DisciplinaVO disciplina, TurmaVO turma, Integer professor) {

        if (getDisciplinaSabado().getCodigo().intValue() != 0) {
            return false;
        }
        setDisciplinaSabado(disciplina);
        setTurmaSabado(turma);
        getProfessorSabado().setCodigo(professor);

        return true;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public DisciplinaVO getDisciplinaDomingo() {
        if (disciplinaDomingo == null) {
            disciplinaDomingo = new DisciplinaVO();
        }
        return disciplinaDomingo;
    }

    public void setDisciplinaDomingo(DisciplinaVO disciplinaDomingo) {
        this.disciplinaDomingo = disciplinaDomingo;
    }

    public DisciplinaVO getDisciplinaQuarta() {
        if (disciplinaQuarta == null) {
            disciplinaQuarta = new DisciplinaVO();
        }
        return disciplinaQuarta;
    }

    public void setDisciplinaQuarta(DisciplinaVO disciplinaQuarta) {
        this.disciplinaQuarta = disciplinaQuarta;
    }

    public DisciplinaVO getDisciplinaQuinta() {
        if (disciplinaQuinta == null) {
            disciplinaQuinta = new DisciplinaVO();
        }
        return disciplinaQuinta;
    }

    public void setDisciplinaQuinta(DisciplinaVO disciplinaQuinta) {
        this.disciplinaQuinta = disciplinaQuinta;
    }

    public DisciplinaVO getDisciplinaSabado() {
        if (disciplinaSabado == null) {
            disciplinaSabado = new DisciplinaVO();
        }
        return disciplinaSabado;
    }

    public void setDisciplinaSabado(DisciplinaVO disciplinaSabado) {
        this.disciplinaSabado = disciplinaSabado;
    }

    public DisciplinaVO getDisciplinaSegunda() {
        if (disciplinaSegunda == null) {
            disciplinaSegunda = new DisciplinaVO();
        }
        return disciplinaSegunda;
    }

    public void setDisciplinaSegunda(DisciplinaVO disciplinaSegunda) {
        this.disciplinaSegunda = disciplinaSegunda;
    }

    public DisciplinaVO getDisciplinaSexta() {
        if (disciplinaSexta == null) {
            disciplinaSexta = new DisciplinaVO();
        }
        return disciplinaSexta;
    }

    public void setDisciplinaSexta(DisciplinaVO disciplinaSexta) {
        this.disciplinaSexta = disciplinaSexta;
    }

    public DisciplinaVO getDisciplinaTerca() {
        if (disciplinaTerca == null) {
            disciplinaTerca = new DisciplinaVO();
        }
        return disciplinaTerca;
    }

    public void setDisciplinaTerca(DisciplinaVO disciplinaTerca) {
        this.disciplinaTerca = disciplinaTerca;
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

    public PessoaVO getProfessorDomingo() {
        if (professorDomingo == null) {
            professorDomingo = new PessoaVO();
        }
        return professorDomingo;
    }

    public void setProfessorDomingo(PessoaVO professorDomingo) {
        this.professorDomingo = professorDomingo;
    }

    public PessoaVO getProfessorQuarta() {
        if (professorQuarta == null) {
            professorQuarta = new PessoaVO();
        }
        return professorQuarta;
    }

    public void setProfessorQuarta(PessoaVO professorQuarta) {
        this.professorQuarta = professorQuarta;
    }

    public PessoaVO getProfessorQuinta() {
        if (professorQuinta == null) {
            professorQuinta = new PessoaVO();
        }
        return professorQuinta;
    }

    public void setProfessorQuinta(PessoaVO professorQuinta) {
        this.professorQuinta = professorQuinta;
    }

    public PessoaVO getProfessorSabado() {
        if (professorSabado == null) {
            professorSabado = new PessoaVO();
        }
        return professorSabado;
    }

    public void setProfessorSabado(PessoaVO professorSabado) {
        this.professorSabado = professorSabado;
    }

    public PessoaVO getProfessorSegunda() {
        if (professorSegunda == null) {
            professorSegunda = new PessoaVO();
        }
        return professorSegunda;
    }

    public void setProfessorSegunda(PessoaVO professorSegunda) {
        this.professorSegunda = professorSegunda;
    }

    public PessoaVO getProfessorSexta() {
        if (professorSexta == null) {
            professorSexta = new PessoaVO();
        }
        return professorSexta;
    }

    public void setProfessorSexta(PessoaVO professorSexta) {
        this.professorSexta = professorSexta;
    }

    public PessoaVO getProfessorTerca() {
        if (professorTerca == null) {
            professorTerca = new PessoaVO();
        }
        return professorTerca;
    }

    public void setProfessorTerca(PessoaVO professorTerca) {
        this.professorTerca = professorTerca;
    }

    public TurmaVO getTurmaDomingo() {
        if (turmaDomingo == null) {
            turmaDomingo = new TurmaVO();
        }
        return turmaDomingo;
    }

    public void setTurmaDomingo(TurmaVO turmaDomingo) {
        this.turmaDomingo = turmaDomingo;
    }

    public TurmaVO getTurmaQuarta() {
        if (turmaQuarta == null) {
            turmaQuarta = new TurmaVO();
        }
        return turmaQuarta;
    }

    public void setTurmaQuarta(TurmaVO turmaQuarta) {
        this.turmaQuarta = turmaQuarta;
    }

    public TurmaVO getTurmaQuinta() {
        if (turmaQuinta == null) {
            turmaQuinta = new TurmaVO();
        }
        return turmaQuinta;
    }

    public void setTurmaQuinta(TurmaVO turmaQuinta) {
        this.turmaQuinta = turmaQuinta;
    }

    public TurmaVO getTurmaSabado() {
        if (turmaSabado == null) {
            turmaSabado = new TurmaVO();
        }
        return turmaSabado;
    }

    public void setTurmaSabado(TurmaVO turmaSabado) {
        this.turmaSabado = turmaSabado;
    }

    public TurmaVO getTurmaSegunda() {
        if (turmaSegunda == null) {
            turmaSegunda = new TurmaVO();
        }
        return turmaSegunda;
    }

    public void setTurmaSegunda(TurmaVO turmaSegunda) {
        this.turmaSegunda = turmaSegunda;
    }

    public TurmaVO getTurmaSexta() {
        if (turmaSexta == null) {
            turmaSexta = new TurmaVO();
        }
        return turmaSexta;
    }

    public void setTurmaSexta(TurmaVO turmaSexta) {
        this.turmaSexta = turmaSexta;
    }

    public TurmaVO getTurmaTerca() {
        if (turmaTerca == null) {
            turmaTerca = new TurmaVO();
        }
        return turmaTerca;
    }

    public void setTurmaTerca(TurmaVO turmaTerca) {
        this.turmaTerca = turmaTerca;
    }
}
