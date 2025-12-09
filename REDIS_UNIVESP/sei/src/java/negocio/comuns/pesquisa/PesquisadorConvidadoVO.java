package negocio.comuns.pesquisa;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade PesquisadorConvidado. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PesquisadorConvidadoVO extends SuperVO {

    private Integer codigo;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>AreaConhecimento </code>.
     */
    private AreaConhecimentoVO areaConhecimento;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO pessoa;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PesquisadorConvidado</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public PesquisadorConvidadoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PesquisadorConvidadoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PesquisadorConvidadoVO obj) throws ConsistirException {
        if (obj.getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo CÓDIGO (Pesquisador Convidado) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PesquisadorConvidado</code>).
     */
    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return (pessoa);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PesquisadorConvidado</code>).
     */
    public void setPessoa(PessoaVO obj) {
        this.pessoa = obj;
    }

    /**
     * Retorna o objeto da classe <code>AreaConhecimento</code> relacionado com
     * (<code>PesquisadorConvidado</code>).
     */
    public AreaConhecimentoVO getAreaConhecimento() {
        if (areaConhecimento == null) {
            areaConhecimento = new AreaConhecimentoVO();
        }
        return (areaConhecimento);
    }

    /**
     * Define o objeto da classe <code>AreaConhecimento</code> relacionado com (
     * <code>PesquisadorConvidado</code>).
     */
    public void setAreaConhecimento(AreaConhecimentoVO obj) {
        this.areaConhecimento = obj;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
