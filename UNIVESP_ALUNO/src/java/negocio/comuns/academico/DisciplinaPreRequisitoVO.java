package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.academico.Disciplina;

/**
 * Reponsável por manter os dados da entidade DisciplinaPreRequisito. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Disciplina
 */
public class DisciplinaPreRequisitoVO extends SuperVO {

    private Integer codigo;
    private Integer gradeDisciplina;
    private Integer gradeDisciplinaComposta;
    private Integer gradeCurricularGrupoOptativaDisciplina;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Disciplina </code>.
     */
    private DisciplinaVO disciplina;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>DisciplinaPreRequisito</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public DisciplinaPreRequisitoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>DisciplinaPreRequisitoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(DisciplinaPreRequisitoVO obj) throws ConsistirException {
        if ((obj.getDisciplina() == null) || (obj.getDisciplina().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PRÉ-REQUISITO (Grade Disciplina) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(new Integer(0));
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

    public Integer getGradeDisciplina() {
        return gradeDisciplina;
    }

    public void setGradeDisciplina(Integer gradeDisciplina) {
        this.gradeDisciplina = gradeDisciplina;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getOrdenacao() {
        return getDisciplina().getNome();
    }
    
    public Integer getGradeDisciplinaComposta() {
        return gradeDisciplinaComposta;
    }

    public void setGradeDisciplinaComposta(Integer gradeDisciplinaComposta) {
        this.gradeDisciplinaComposta = gradeDisciplinaComposta;
    }
    
    public Integer getGradeCurricularGrupoOptativaDisciplina() {
		return gradeCurricularGrupoOptativaDisciplina;
	}

	public void setGradeCurricularGrupoOptativaDisciplina(Integer gradeCurricularGrupoOptativaDisciplina) {
		this.gradeCurricularGrupoOptativaDisciplina = gradeCurricularGrupoOptativaDisciplina;
	}
    
}
