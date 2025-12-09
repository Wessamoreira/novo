package negocio.comuns.eventos;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.eventos.TrabalhoSubmetido;

/**
 * Reponsável por manter os dados da entidade AutorTrabalhoSubmetido. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see TrabalhoSubmetido
 */
public class AutorTrabalhoSubmetidoVO extends SuperVO {

    private Integer codigo;
    private Integer trabalhoSubmetido;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO pessoaAutorTrabalhoSubmetido;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>AutorTrabalhoSubmetido</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public AutorTrabalhoSubmetidoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>AutorTrabalhoSubmetidoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(AutorTrabalhoSubmetidoVO obj) throws ConsistirException {
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>AutorTrabalhoSubmetido</code>).
     */
    public PessoaVO getPessoaAutorTrabalhoSubmetido() {
        if (pessoaAutorTrabalhoSubmetido == null) {
            pessoaAutorTrabalhoSubmetido = new PessoaVO();
        }
        return (pessoaAutorTrabalhoSubmetido);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>AutorTrabalhoSubmetido</code>).
     */
    public void setPessoaAutorTrabalhoSubmetido(PessoaVO obj) {
        this.pessoaAutorTrabalhoSubmetido = obj;
    }

    public Integer getTrabalhoSubmetido() {
        return (trabalhoSubmetido);
    }

    public void setTrabalhoSubmetido(Integer trabalhoSubmetido) {
        this.trabalhoSubmetido = trabalhoSubmetido;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
