package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.facade.jdbc.basico.Pessoa;

/**
 * Reponsável por manter os dados da entidade DisponibilidadeHorario. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Pessoa
 */
public class DisponibilidadeHorarioVO extends SuperVO {

    private DisciplinaVO disciplina;
    private TurmaVO turma;
    private Integer professor;
    private DiaSemana diaSemana;
    private Integer nrAula;
    private String intervaloHora;
    private String aula;
    private TurnoVO turno;
    private Boolean disponivelHorario;
    private Boolean existeAula;
    
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
    
    public static final long serialVersionUID = 1L;

    public DisponibilidadeHorarioVO getClone() {
        DisponibilidadeHorarioVO novoObj = new DisponibilidadeHorarioVO();
        novoObj.setAula(getAula());
        novoObj.setDisponivelHorario(getDisponivelHorario());
        novoObj.setIntervaloHora(getIntervaloHora());
        novoObj.setNrAula(getNrAula());
        novoObj.getTurno().setCodigo(getTurno().getCodigo());
        novoObj.setProfessor(getProfessor());
        novoObj.setDisciplina(getDisciplina());
        return novoObj;
    }

    /**
     * Construtor padrão da classe <code>DisponibilidadeHorario</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public DisponibilidadeHorarioVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setDiaSemana(DiaSemana.DOMINGO);
        setDisponivelHorario(false);
        setNrAula(nrAula);
        setProfessor(0);
        setIntervaloHora("");
        setAula("");
        setExisteAula(false);
    }

    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
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

    public Boolean getExisteAula() {
        return existeAula;
    }

    public void setExisteAula(Boolean existeAula) {
        this.existeAula = existeAula;
    }

    public String getNomeTurmaDisciplina() {
        String nomeTurmaDisciplina = "";
        if (getDisciplina().getCodigo().intValue() > 0) {
            if (getTurma().getCodigo().intValue() > 0) {
                nomeTurmaDisciplina = "Em Aula";
            }
        } else if (getDisciplina().getCodigo().intValue() == -1
                || (getDisciplina().getCodigo().intValue() == 0 && getDisponivelHorario())) {
            nomeTurmaDisciplina = "Indisponível Permanentemente";
        } else if (getDisciplina().getCodigo().intValue() == 0) {
            nomeTurmaDisciplina = "Horário Livre";
        }
        return nomeTurmaDisciplina;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }

    public String getIntervaloHora() {
        return intervaloHora;
    }

    public void setIntervaloHora(String intervaloHora) {
        this.intervaloHora = intervaloHora;
    }

    public Boolean getDisponivelHorario() {

        return disponivelHorario;
    }

    public void setDisponivelHorario(Boolean disponivelHorario) {
        this.disponivelHorario = disponivelHorario;
    }

    public Integer getNrAula() {
        return nrAula;
    }

    public void setNrAula(Integer nrAula) {
        this.nrAula = nrAula;
    }

    /**
     * Retorna o objeto da classe <code>Turno</code> relacionado com (
     * <code>DisponibilidadeHorario</code>).
     */
    public TurnoVO getTurno() {
        if (turno == null) {
            turno = new TurnoVO();
        }
        return (turno);
    }

    /**
     * Define o objeto da classe <code>Turno</code> relacionado com (
     * <code>DisponibilidadeHorario</code>).
     */
    public void setTurno(TurnoVO obj) {
        this.turno = obj;
    }

    public DiaSemana getDiaSemana() {
        return (diaSemana);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getDiaSemana_Apresentar() {
        return (diaSemana.getDescricao());
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Integer getProfessor() {
        return (professor);
    }

    public void setProfessor(Integer professor) {
        this.professor = professor;
    }

    public String getDisponivel() {
        return "Disponível";
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
}
