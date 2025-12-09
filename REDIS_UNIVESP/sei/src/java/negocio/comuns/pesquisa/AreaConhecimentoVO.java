package negocio.comuns.pesquisa;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade AreaConhecimento. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */

@XmlRootElement(name = "areaConhecimentoVO")
public class AreaConhecimentoVO extends SuperVO {

	private Integer codigo;
    private String nome;
    private String descricao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>AreaConhecimento </code>.
     */
    private AreaConhecimentoVO areaConhecimentoPrincipal;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>AreaConhecimento</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public AreaConhecimentoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>AreaConhecimentoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(AreaConhecimentoVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Área de Conhecimento) deve ser informado.");
        }
        if (obj.getCodigo().intValue() != 0) {
            if (obj.getCodigo().equals(obj.getAreaConhecimentoPrincipal().getCodigo())) {
                throw new ConsistirException("O campo ÁREA CONHECIMENTO PRINCIPAL (Área de Conhecimento) não pode ser a própria área de conhecimento.");
            }
        }
    }

    /**
     * Retorna o objeto da classe <code>AreaConhecimento</code> relacionado com
     * (<code>AreaConhecimento</code>).
     */
    public AreaConhecimentoVO getAreaConhecimentoPrincipal() {
        if (areaConhecimentoPrincipal == null) {
            areaConhecimentoPrincipal = new AreaConhecimentoVO();
        }
        return (areaConhecimentoPrincipal);
    }

    /**
     * Define o objeto da classe <code>AreaConhecimento</code> relacionado com (
     * <code>AreaConhecimento</code>).
     */
    public void setAreaConhecimentoPrincipal(AreaConhecimentoVO obj) {
        this.areaConhecimentoPrincipal = obj;
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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

    @XmlElement(name = "codigo")
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
