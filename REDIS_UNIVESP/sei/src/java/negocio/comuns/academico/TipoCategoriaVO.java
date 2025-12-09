package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade TipoMidiaCaptacao. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class TipoCategoriaVO extends SuperVO {

    private Integer codigo;
    private String nome;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>TipoMidiaCaptacao</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public TipoCategoriaVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>TipoMidiaCaptacaoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(TipoCategoriaVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Tipo Categoria) deve ser informado.");
        }
    }

    public String getNome() {
    	if (nome == null) {
    		nome = "";
    	}
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
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
