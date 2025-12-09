package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.academico.PeriodoLetivo;

/**
 * Reponsável por manter os dados da entidade PeriodoDisciplina. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see PeriodoLetivo
 */
public class PeriodoDisciplinaVO extends SuperVO {

    private Integer periodoLetivo;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Disciplina </code>.
     */
    private DisciplinaVO disciplina;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PeriodoDisciplina</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public PeriodoDisciplinaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PeriodoDisciplinaVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PeriodoDisciplinaVO obj) throws ConsistirException {
        if ((obj.getDisciplina() == null) || (obj.getDisciplina().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo DISCIPLINA (Disciplinas Período Letivo) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
    }

    /**
     * Retorna o objeto da classe <code>Disciplina</code> relacionado com (
     * <code>PeriodoDisciplina</code>).
     */
    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return (disciplina);
    }

    /**
     * Define o objeto da classe <code>Disciplina</code> relacionado com (
     * <code>PeriodoDisciplina</code>).
     */
    public void setDisciplina(DisciplinaVO obj) {
        this.disciplina = obj;
    }

    public Integer getPeriodoLetivo() {
        return (periodoLetivo);
    }

    public void setPeriodoLetivo(Integer periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
    }
}
