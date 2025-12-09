package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade RegistroAula. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ProfessorMinistrouAulaTurmaVO extends SuperVO {

    private Integer codigo;
    private Date data;
//    private Integer cargaHoraria;
//    private String conteudo;
//    private Date dataRegistroAula;
//    private String tipoAula;
//    private String diaSemana;
    private String semestre;
    private String ano;
    private Boolean titular;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>FrequenciaAula</code>.
     */
//    private List frequenciaAulaVOs;
    private TurmaVO turma;
    private DisciplinaVO disciplina;
//    private HorarioTurmaVO horarioTurma;
//    private PessoaVO professor;
    private FuncionarioVO professor;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private UsuarioVO responsavelRegistro;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>RegistroAula</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ProfessorMinistrouAulaTurmaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>RegistroAulaVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ProfessorMinistrouAulaTurmaVO obj) throws ConsistirException {
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Definir Professor Titular) deve ser informado.");
        }
        if ((obj.getTurma() == null) || (obj.getTurma().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TURMA (Definir Professor Titular) deve ser informado.");
        }
        if ((obj.getDisciplina() == null) || (obj.getDisciplina().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo DISCIPLINA (Definir Professor Titular) deve ser informado.");
        }
        if ((obj.getProfessor() == null) || (obj.getProfessor().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PROFESSOR (Definir Professor Titular) deve ser informado.");
        }
        if ((obj.getResponsavelRegistro() == null)
                || (obj.getResponsavelRegistro().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL (Definir Professor Titular) deve ser informado.");
        }
        if (obj.getTurma().getSemestral()) {
            if (obj.getSemestre().equals("")) {
                throw new ConsistirException("O campo SEMESTRE (Definir Professor Titular) deve ser informado");
            }
            if (obj.getAno().equals("")) {
                throw new ConsistirException("O campo ANO (Definir Professor Titular) deve ser informado");
            }
        } else if (obj.getTurma().getAnual()) {
            if (obj.getAno().equals("")) {
                throw new ConsistirException("O campo ANO (Definir Professor Titular) deve ser informado");
            }
            obj.setSemestre("");
        } else if (obj.getTurma().getIntegral()) {
            obj.setAno("");
            obj.setSemestre("");
        }

    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setData(new Date());
//        setCargaHoraria(0);
//        setConteudo("");
//        setDataRegistroAula(new Date());
//        setTipoAula("P");
//        setDiaSemana("");
        setSemestre("");
        setTitular(Boolean.FALSE);
        setAno(Uteis.getAnoDataAtual4Digitos());
//        montarDiaSemanaAula();
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>FrequenciaAulaVO</code> ao List <code>frequenciaAulaVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>FrequenciaAula</code> - getMatricula().getMatricula() - como
     * identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>FrequenciaAulaVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
//    public void adicionarObjFrequenciaAulaVOs(FrequenciaAulaVO obj) throws Exception {
//        FrequenciaAulaVO.validarDados(obj);
//        int index = 0;
//        Iterator i = getFrequenciaAulaVOs().iterator();
//        while (i.hasNext()) {
//            FrequenciaAulaVO objExistente = (FrequenciaAulaVO) i.next();
//            if (objExistente.getMatricula().getMatricula().equals(obj.getMatricula().getMatricula())) {
//                getFrequenciaAulaVOs().set(index, obj);
//                return;
//            }
//            index++;
//        }
//        getFrequenciaAulaVOs().add(obj);
//    }
    /**
     * Operação responsável por excluir um objeto da classe
     * <code>FrequenciaAulaVO</code> no List <code>frequenciaAulaVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>FrequenciaAula</code> - getMatricula().getMatricula() - como
     * identificador (key) do objeto no List.
     *
     * @param matricula
     *            Parâmetro para localizar e remover o objeto do List.
     */
//    public void excluirObjFrequenciaAulaVOs(String matricula) throws Exception {
//        int index = 0;
//        Iterator i = getFrequenciaAulaVOs().iterator();
//        while (i.hasNext()) {
//            FrequenciaAulaVO objExistente = (FrequenciaAulaVO) i.next();
//            if (objExistente.getMatricula().getMatricula().equals(matricula)) {
//                getFrequenciaAulaVOs().remove(index);
//                return;
//            }
//            index++;
//        }
//    }
    /**
     * Operação responsável por consultar um objeto da classe
     * <code>FrequenciaAulaVO</code> no List <code>frequenciaAulaVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>FrequenciaAula</code> - getMatricula().getMatricula() - como
     * identificador (key) do objeto no List.
     *
     * @param matricula
     *            Parâmetro para localizar o objeto do List.
     */
//    public FrequenciaAulaVO consultarObjFrequenciaAulaVO(String matricula) throws Exception {
//        Iterator i = getFrequenciaAulaVOs().iterator();
//        while (i.hasNext()) {
//            FrequenciaAulaVO objExistente = (FrequenciaAulaVO) i.next();
//            if (objExistente.getMatricula().getMatricula().equals(matricula)) {
//                return objExistente;
//            }
//        }
//        return null;
//    }
//
//    public boolean getPermiteAlterarCargaHoraria() {
//        if (getTipoAula().equals("P")) {
//            return false;
//        }
//        return true;
//    }
    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public FuncionarioVO getProfessor() {
        if (professor == null) {
            professor = new FuncionarioVO();
        }
        return professor;
    }

    public void setProfessor(FuncionarioVO professor) {
        this.professor = professor;
    }

//    public PessoaVO getProfessor() {
//        if (professor == null) {
//            professor = new PessoaVO();
//        }
//        return professor;
//    }
//
//    public void setProfessor(PessoaVO professor) {
//        this.professor = professor;
//    }
    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
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

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>RegistroAula</code>).
     */
    public UsuarioVO getResponsavelRegistro() {
        if (responsavelRegistro == null) {
            responsavelRegistro = new UsuarioVO();
        }
        return (responsavelRegistro);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>RegistroAula</code>).
     */
    public void setResponsavelRegistro(UsuarioVO obj) {
        this.responsavelRegistro = obj;
    }

    public Date getData() {
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(data));
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the titular
     */
    public Boolean getTitular() {
        return titular;
    }

    /**
     * @param titular the titular to set
     */
    public void setTitular(Boolean titular) {
        this.titular = titular;
    }
}
