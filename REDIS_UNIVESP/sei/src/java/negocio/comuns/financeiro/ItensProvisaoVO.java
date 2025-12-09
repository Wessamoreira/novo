package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.financeiro.ProvisaoCusto;

/**
 * Reponsável por manter os dados da entidade ItensProvisao. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see ProvisaoCusto
 */
public class ItensProvisaoVO extends SuperVO {

    private Integer codigo;
    private Double valor;
    private String nrDocumento;
    private Integer provisaoCusto;
    private String descricao;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ItensProvisao</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ItensProvisaoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ItensProvisaoVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ItensProvisaoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getValor().doubleValue() == 0) {
            throw new ConsistirException("O campo VALOR (Itens da Provisão) deve ser informado.");
        }
        if (obj.getNrDocumento().equals("")) {
            throw new ConsistirException("O campo NR. DOCUMENTO (Itens da Provisão) deve ser informado.");
        }
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO (Itens da Provisão) deve ser informado.");
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
        setNrDocumento(getNrDocumento().toUpperCase());
        setDescricao(getDescricao().toUpperCase());
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

    public Integer getProvisaoCusto() {
        if (provisaoCusto == null) {
            provisaoCusto = 0;
        }
        return (provisaoCusto);
    }

    public void setProvisaoCusto(Integer provisaoCusto) {
        this.provisaoCusto = provisaoCusto;
    }

    public String getNrDocumento() {
        if (nrDocumento == null) {
            nrDocumento = "";
        }
        return (nrDocumento);
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        return (valor);
    }

    public void setValor(Double valor) {
        this.valor = valor;
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
