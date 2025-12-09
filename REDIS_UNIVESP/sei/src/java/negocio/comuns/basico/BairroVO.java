package negocio.comuns.basico;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Bairro. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "bairro")
public class BairroVO extends SuperVO {

    private Integer codigo;
    private String descricao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Cidade </code>.
     */
    private CidadeVO cidade;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Bairro</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public BairroVO() {
        super();
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da
     * classe <code>BairroVO</code>.
     */
    public static void validarUnicidade(List<BairroVO> lista, BairroVO obj) throws ConsistirException {
        for (BairroVO repetido : lista) {
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>BairroVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(BairroVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getCidade() == null) || (obj.getCidade().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CIDADE (BAIRRO) deve ser informado.");
        }
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO (BAIRRO) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        setDescricao(getDescricao().toUpperCase());
    }

    /**
     * Retorna o objeto da classe <code>Cidade</code> relacionado com (
     * <code>Bairro</code>).
     */
    @XmlElement(name = "cidade")
    public CidadeVO getCidade() {
        if (cidade == null) {
            cidade = new CidadeVO();
        }
        return (cidade);
    }

    /**
     * Define o objeto da classe <code>Cidade</code> relacionado com (
     * <code>Bairro</code>).
     */
    public void setCidade(CidadeVO obj) {
        this.cidade = obj;
    }

    @XmlElement(name = "descricao") 
    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
