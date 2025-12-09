package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.academico.Disciplina;

/**
 * Reponsável por manter os dados da entidade DisciplinaEquivalente. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Disciplina
 */
public class DisciplinaEquivalenteVO extends SuperVO {

    private Integer disciplina;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Disciplina </code>.
     */
    private DisciplinaVO equivalente;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>DisciplinaEquivalente</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public DisciplinaEquivalenteVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>DisciplinaEquivalenteVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(DisciplinaEquivalenteVO obj) throws ConsistirException {
        if ((obj.getEquivalente() == null) || (obj.getEquivalente().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo EQUIVALENTE (Disciplinas Equivalentes) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
    }

    /**
     * Retorna o objeto da classe <code>Disciplina</code> relacionado com (
     * <code>DisciplinaEquivalente</code>).
     */
    public DisciplinaVO getEquivalente() {
        if (equivalente == null) {
            equivalente = new DisciplinaVO();
        }
        return (equivalente);
    }

    /**
     * Define o objeto da classe <code>Disciplina</code> relacionado com (
     * <code>DisciplinaEquivalente</code>).
     */
    public void setEquivalente(DisciplinaVO obj) {
        this.equivalente = obj;
    }

    public Integer getDisciplina() {
        return (disciplina);
    }

    public void setDisciplina(Integer disciplina) {
        this.disciplina = disciplina;
    }
}
