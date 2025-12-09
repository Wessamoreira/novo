package negocio.comuns.basico;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Endereco. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "endereco")
public class EnderecoVO extends SuperVO {

    private Integer codigo;
    private String cep;
    private String logradouro;
    private String completo;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Bairro </code>.
     */
    private BairroVO bairrocodigo;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Endereco</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public EnderecoVO() {
        super();
        inicializarDados();
    }

    public static void validarCEP(String cep) throws Exception {
        if (cep.length() == 8 || cep.length() == 10) {
        	return;
        } else {
            throw new Exception("Digite um CEP válido. Exemplo: 99.999-999");
        }
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da
     * classe <code>EnderecoVO</code>.
     */
    public static void validarUnicidade(List<EnderecoVO> lista, EnderecoVO obj) throws ConsistirException {
        for (EnderecoVO repetido : lista) {
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>EnderecoVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(EnderecoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getBairrocodigo() == null) || (obj.getBairrocodigo().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo BAIRRO (Endereço) deve ser informado.");
        }
        if (obj.getCep().equals("")) {
            throw new ConsistirException("O campo CEP (Endereço) deve ser informado.");
        }
        if (obj.getLogradouro().equals("")) {
            throw new ConsistirException("O campo LOGRADOURO (Endereço) deve ser informado.");
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
        setCep(getCep().toUpperCase());
        setLogradouro(getLogradouro().toUpperCase());
        setCompleto(getCompleto().toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(null);
        setCep("");
        setLogradouro("");
        setCompleto("");
    }

    /**
     * Retorna o objeto da classe <code>Bairro</code> relacionado com (
     * <code>Endereco</code>).
     */
    @XmlElement(name = "bairro")
    public BairroVO getBairrocodigo() {
        if (bairrocodigo == null) {
            bairrocodigo = new BairroVO();
        }
        return (bairrocodigo);
    }

    /**
     * Define o objeto da classe <code>Bairro</code> relacionado com (
     * <code>Endereco</code>).
     */
    public void setBairrocodigo(BairroVO obj) {
        this.bairrocodigo = obj;
    }

    public String getCompleto() {
        if (completo == null) {
            completo = "";
        }
        return (completo);
    }

    public void setCompleto(String completo) {
        this.completo = completo;
    }

    @XmlElement(name = "logradouro")
    public String getLogradouro() {
        if (logradouro == null) {
            logradouro = "";
        }
        return (logradouro);
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getCep() {
        if (cep == null) {
            cep = "";
        }
        return (cep);
    }

    public void setCep(String cep) {
        this.cep = cep;
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
