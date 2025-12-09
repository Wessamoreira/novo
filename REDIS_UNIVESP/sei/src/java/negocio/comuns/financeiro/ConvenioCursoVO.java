package negocio.comuns.financeiro;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.financeiro.Convenio;

/**
 * Reponsável por manter os dados da entidade ConvenioCurso. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see Convenio
 */
public class ConvenioCursoVO extends SuperVO {

    private Integer convenio;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Curso </code>.
     */
    private CursoVO curso;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ConvenioCurso</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ConvenioCursoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ConvenioCursoVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ConvenioCursoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getCurso() == null) || (obj.getCurso().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CURSO (Cursos Cobertos pelo Convênio) deve ser informado.");
        }
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (
     * <code>ConvenioCurso</code>).
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (
     * <code>ConvenioCurso</code>).
     */
    public void setCurso(CursoVO obj) {
        this.curso = obj;
    }

    public Integer getConvenio() {
        if (convenio == null) {
            convenio = 0;
        }
        return (convenio);
    }

    public void setConvenio(Integer convenio) {
        this.convenio = convenio;
    }
}
