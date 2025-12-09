package negocio.comuns.eventos;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade MembroComunidade. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class MembroComunidadeVO extends SuperVO {

    private Integer codigo;
    private String instituicaoEnsinoOrigem;
    private String formacao;
    private String nivelFormacao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO pessoa;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>MembroComunidade</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public MembroComunidadeVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>MembroComunidadeVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(MembroComunidadeVO obj) throws ConsistirException {
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setInstituicaoEnsinoOrigem("");
        setFormacao("");
        setNivelFormacao("");
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>MembroComunidade</code>).
     */
    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return (pessoa);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>MembroComunidade</code>).
     */
    public void setPessoa(PessoaVO obj) {
        this.pessoa = obj;
    }

    public String getNivelFormacao() {
        return (nivelFormacao);
    }

    public void setNivelFormacao(String nivelFormacao) {
        this.nivelFormacao = nivelFormacao;
    }

    public String getFormacao() {
        return (formacao);
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }

    public String getInstituicaoEnsinoOrigem() {
        return (instituicaoEnsinoOrigem);
    }

    public void setInstituicaoEnsinoOrigem(String instituicaoEnsinoOrigem) {
        this.instituicaoEnsinoOrigem = instituicaoEnsinoOrigem;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
