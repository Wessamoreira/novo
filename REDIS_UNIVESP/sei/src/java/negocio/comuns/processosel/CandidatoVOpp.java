package negocio.comuns.processosel;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade Candidato. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class CandidatoVOpp extends SuperVO {

    private Integer codigo;
    private String nomePai;
    private String nomeMae;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO pessoa;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Candidato</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public CandidatoVOpp() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>CandidatoVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(CandidatoVOpp obj) throws ConsistirException {
        if (obj.getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo CÓDIGO (Candidato) deve ser informado.");
        }
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>Candidato</code>).
     */
    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return (pessoa);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>Candidato</code>).
     */
    public void setPessoa(PessoaVO obj) {
        this.pessoa = obj;
    }

    public String getNomeMae() {
        if (nomeMae == null) {
            nomeMae = "";
        }
        return (nomeMae);
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getNomePai() {
        if (nomePai == null) {
            nomePai = "";
        }
        return (nomePai);
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
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
