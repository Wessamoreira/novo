package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade Turma. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class DisciplinaAbonoVO extends SuperVO {

    private Integer codigo;
    private MatriculaVO matricula;
    private Boolean faltaAbonada;
    private Boolean faltaJustificada;
    private DisciplinaVO disciplina;
    private Integer abonoFalta;
    private RegistroAulaVO registroAula;
    
    private String descricaoMotivoAbonoJustificativa;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Turma</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public DisciplinaAbonoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>TurmaVO</code>. Todos os tipos de consistência de dados são e devem
     * ser implementadas neste método. São validações típicas: verificação de
     * campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(DisciplinaAbonoVO obj) throws ConsistirException {

        if (obj.getDisciplina() == null || obj.getDisciplina().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo DISCIPLINA (Abono Falta) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setAbonoFalta(0);
        setFaltaAbonada(Boolean.FALSE);
    }

    /**
     * @return the codigo
     */
    public Integer getCodigo() {
        return codigo;
    }

    /**
     * @param codigo
     *            the codigo to set
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the matricula
     */
    public MatriculaVO getMatricula() {
        if (matricula == null) {
            matricula = new MatriculaVO();
        }
        return matricula;
    }

    /**
     * @param matricula
     *            the matricula to set
     */
    public void setMatricula(MatriculaVO matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the disciplina
     */
    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    /**
     * @param disciplina
     *            the disciplina to set
     */
    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }

    /**
     * @return the abonoFalta
     */
    public Integer getAbonoFalta() {
        return abonoFalta;
    }

    /**
     * @param abonoFalta
     *            the abonoFalta to set
     */
    public void setAbonoFalta(Integer abonoFalta) {
        this.abonoFalta = abonoFalta;
    }

    /**
     * @return the registroAula
     */
    public RegistroAulaVO getRegistroAula() {
        if (registroAula == null) {
            registroAula = new RegistroAulaVO();
        }
        return registroAula;
    }

    /**
     * @param registroAula
     *            the registroAula to set
     */
    public void setRegistroAula(RegistroAulaVO registroAula) {
        this.registroAula = registroAula;
    }

    /**
     * @return the faltaAbonada
     */
    public Boolean getFaltaAbonada() {
        if (faltaAbonada == null) {
            faltaAbonada = Boolean.FALSE;
        }
        return faltaAbonada;
    }

    /**
     * @param faltaAbonada
     *            the faltaAbonada to set
     */
    public void setFaltaAbonada(Boolean faltaAbonada) {
        this.faltaAbonada = faltaAbonada;
    }

    /**
     * @return the descricaoMotivoAbonoJustificativa
     */
    public String getDescricaoMotivoAbonoJustificativa() {
        if (descricaoMotivoAbonoJustificativa == null) {
            descricaoMotivoAbonoJustificativa = "";
        }
        return descricaoMotivoAbonoJustificativa;
    }

    /**
     * @param descricaoMotivoAbonoJustificativa the descricaoMotivoAbonoJustificativa to set
     */
    public void setDescricaoMotivoAbonoJustificativa(String descricaoMotivoAbonoJustificativa) {
        this.descricaoMotivoAbonoJustificativa = descricaoMotivoAbonoJustificativa;
    }

    /**
     * @return the faltaJustificada
     */
    public Boolean getFaltaJustificada() {
        if (faltaJustificada == null) {
            faltaJustificada = Boolean.FALSE;
        }
        return faltaJustificada;
    }

    /**
     * @param faltaJustificada the faltaJustificada to set
     */
    public void setFaltaJustificada(Boolean faltaJustificada) {
        this.faltaJustificada = faltaJustificada;
    }
}
