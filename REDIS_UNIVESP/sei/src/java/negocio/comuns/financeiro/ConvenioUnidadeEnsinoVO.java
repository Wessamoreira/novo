package negocio.comuns.financeiro;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.financeiro.Convenio;

/**
 * Reponsável por manter os dados da entidade ConvenioUnidadeEnsino. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see Convenio
 */
public class ConvenioUnidadeEnsinoVO extends SuperVO {

    private Integer convenio;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ConvenioUnidadeEnsino</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ConvenioUnidadeEnsinoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ConvenioUnidadeEnsinoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ConvenioUnidadeEnsinoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Unidades Ensino Cobertas pelo Convênio) deve ser informado.");
        }
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>ConvenioUnidadeEnsino</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>ConvenioUnidadeEnsino</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
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
