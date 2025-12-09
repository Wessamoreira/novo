package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade ProgramacaoAula. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ProgramacaoAulaVO extends SuperVO {

    private Integer codigo;
    private String diaSemana;
    private Integer aulaInicio;
    private Integer aulaFim;
    private String local;
    private Boolean laboratorial;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Turma </code>.
     */
    private TurmaVO turma;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Disciplina </code>.
     */
    private DisciplinaVO disciplina;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO professor;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Turno </code>.
     */
    private TurnoVO turno;
    private HorarioTurmaVO horarioTurma;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ProgramacaoAula</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ProgramacaoAulaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ProgramacaoAulaVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ProgramacaoAulaVO obj) throws ConsistirException {
        if ((obj.getTurma() == null) || (obj.getTurma().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TURMA (Programar Aulas Turma) deve ser informado.");
        }
        if ((obj.getDisciplina() == null) || (obj.getDisciplina().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo DISCIPLINA (Programar Aulas Turma) deve ser informado.");
        }
        if ((obj.getProfessor() == null) || (obj.getProfessor().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PROFESSOR (Programar Aulas Turma) deve ser informado.");
        }
        if ((obj.getDiaSemana() == null) || (obj.getDiaSemana().equals(""))) {
            throw new ConsistirException("O campo DIA SEMANA (Programar Aulas Turma) deve ser informado.");
        }
        if ((obj.getAulaInicio() == null) || (obj.getAulaInicio().intValue() == 0)) {
            throw new ConsistirException("O campo AULA INÍCIO (Programar Aulas Turma) deve ser informado.");
        }
        if ((obj.getAulaFim() == null) || (obj.getAulaFim().intValue() == 0)) {
            throw new ConsistirException("O campo AULA FIM (Programar Aulas Turma) deve ser informado.");
        }
        if (obj.getAulaFim().intValue() < obj.getAulaInicio().intValue()) {
            throw new ConsistirException("O campo AULA FIM (Programar Aulas Turma) não pode ser menor que o campo AULA INÍCIO (Programar Aulas Turma).");
        }
        if ((obj.getLocal() == null) || (obj.getLocal().equals(""))) {
            throw new ConsistirException("O campo LOCAL (Programar Aulas Turma) deve ser informado.");
        }
        if ((obj.getTurno() == null) || (obj.getTurno().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TURNO (Programar Aulas Turma) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(new Integer(0));
        setDiaSemana("");
        setAulaInicio(new Integer(0));
        setAulaFim(new Integer(0));
        setLocal("");
        setLaboratorial(Boolean.FALSE);
    }

    public HorarioTurmaVO getHorarioTurma() {
        if (horarioTurma == null) {
            horarioTurma = new HorarioTurmaVO();
        }
        return horarioTurma;
    }

    public void setHorarioTurma(HorarioTurmaVO horarioTurma) {
        this.horarioTurma = horarioTurma;
    }

    /**
     * Retorna o objeto da classe <code>Turno</code> relacionado com (
     * <code>ProgramacaoAula</code>).
     */
    public TurnoVO getTurno() {
        if (turno == null) {
            turno = new TurnoVO();
        }
        return (turno);
    }

    /**
     * Define o objeto da classe <code>Turno</code> relacionado com (
     * <code>ProgramacaoAula</code>).
     */
    public void setTurno(TurnoVO obj) {
        this.turno = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ProgramacaoAula</code>).
     */
    public PessoaVO getProfessor() {
        if (professor == null) {
            professor = new PessoaVO();
        }
        return (professor);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>ProgramacaoAula</code>).
     */
    public void setProfessor(PessoaVO obj) {
        this.professor = obj;
    }

    /**
     * Retorna o objeto da classe <code>Disciplina</code> relacionado com (
     * <code>ProgramacaoAula</code>).
     */
    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return (disciplina);
    }

    /**
     * Define o objeto da classe <code>Disciplina</code> relacionado com (
     * <code>ProgramacaoAula</code>).
     */
    public void setDisciplina(DisciplinaVO obj) {
        this.disciplina = obj;
    }

    /**
     * Retorna o objeto da classe <code>Turma</code> relacionado com (
     * <code>ProgramacaoAula</code>).
     */
    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return (turma);
    }

    /**
     * Define o objeto da classe <code>Turma</code> relacionado com (
     * <code>ProgramacaoAula</code>).
     */
    public void setTurma(TurmaVO obj) {
        this.turma = obj;
    }

    public Boolean getLaboratorial() {
        return (laboratorial);
    }

    public Boolean isLaboratorial() {
        return (laboratorial);
    }

    public void setLaboratorial(Boolean laboratorial) {
        this.laboratorial = laboratorial;
    }

    public String getLocal() {
        return (local);
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getAulaFim() {
        return (aulaFim);
    }

    public void setAulaFim(Integer aulaFim) {
        this.aulaFim = aulaFim;
    }

    public Integer getAulaInicio() {
        return (aulaInicio);
    }

    public void setAulaInicio(Integer aulaInicio) {
        this.aulaInicio = aulaInicio;
    }

    public String getDiaSemana() {
        return (diaSemana);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getDiaSemana_Apresentar() {
        if (diaSemana.equals("01")) {
            return "Domingo";
        }
        if (diaSemana.equals("02")) {
            return "Segunda";
        }
        if (diaSemana.equals("03")) {
            return "Terça";
        }
        if (diaSemana.equals("04")) {
            return "Quarta";
        }
        if (diaSemana.equals("05")) {
            return "Quinta";
        }
        if (diaSemana.equals("06")) {
            return "Sexta";
        }
        if (diaSemana.equals("07")) {
            return "Sábado";
        }
        return (diaSemana);
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
