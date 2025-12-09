package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade HorarioTurmaProfessorDisciplina.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see HorarioTuma
 */
public class HorarioTurmaProfessorDisciplinaVO extends SuperVO {

    private Integer codigo;
    private String horarios;
    private Integer horarioTurma;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Turma </code>.
     */
    private TurmaVO turma;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO professor;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Disciplina </code>.
     */
    private DisciplinaVO disciplina;
    private Date data;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>HorarioTurmaProfessorDisciplina</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente
     * seus atributos (Classe VO).
     */
    public HorarioTurmaProfessorDisciplinaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>HorarioTurmaProfessorDisciplinaVO</code>. Todos os tipos de
     * consistência de dados são e devem ser implementadas neste método. São
     * validações típicas: verificação de campos obrigatórios, verificação de
     * valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(HorarioTurmaProfessorDisciplinaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getTurma() == null) || (obj.getTurma().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TURMA (Horário Turma Professor Disciplina) deve ser informado.");
        }
        if ((obj.getProfessor() == null) || (obj.getProfessor().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PROFESSOR (Horário Turma Professor Disciplina) deve ser informado.");
        }
        if ((obj.getDisciplina() == null) || (obj.getDisciplina().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo DISCIPLINA (Horário Turma Professor Disciplina) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(new Integer(0));
        setHorarios("");
    }

    /**
     * Retorna o objeto da classe <code>Disciplina</code> relacionado com (
     * <code>HorarioTurmaProfessorDisciplina</code>).
     */
    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return (disciplina);
    }

    /**
     * Define o objeto da classe <code>Disciplina</code> relacionado com (
     * <code>HorarioTurmaProfessorDisciplina</code>).
     */
    public void setDisciplina(DisciplinaVO obj) {
        this.disciplina = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>HorarioTurmaProfessorDisciplina</code>).
     */
    public PessoaVO getProfessor() {
        if (professor == null) {
            professor = new PessoaVO();
        }
        return (professor);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>HorarioTurmaProfessorDisciplina</code>).
     */
    public void setProfessor(PessoaVO obj) {
        this.professor = obj;
    }

    /**
     * Retorna o objeto da classe <code>Turma</code> relacionado com (
     * <code>HorarioTurmaProfessorDisciplina</code>).
     */
    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return (turma);
    }

    /**
     * Define o objeto da classe <code>Turma</code> relacionado com (
     * <code>HorarioTurmaProfessorDisciplina</code>).
     */
    public void setTurma(TurmaVO obj) {
        this.turma = obj;
    }

    public Integer getHorarioTurma() {
        return (horarioTurma);
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setHorarioTurma(Integer horarioTurma) {
        this.horarioTurma = horarioTurma;
    }

    public String getHorarios() {
        return (horarios);
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
