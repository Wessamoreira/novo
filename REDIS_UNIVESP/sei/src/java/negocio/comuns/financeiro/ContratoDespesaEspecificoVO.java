package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade ContratoDespesaEspecifico. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see ContratoDespesa
 */
public class ContratoDespesaEspecificoVO extends SuperVO {

    private Integer codigo;
    private Integer contratoDespesa;
    private Date dataVencimento;
    private Double valorParcela;
    private Integer nrParcela;
    private String numeroDocumento;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ContratoDespesaEspecifico</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ContratoDespesaEspecificoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ContratoDespesaEspecificoVO</code>. Todos os tipos de consistência
     * de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores
     * válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ContratoDespesaEspecificoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getDataVencimento() == null) {
            throw new ConsistirException("O campo DATA VENCIMENTO (Vencimento Específico) deve ser informado.");
        }

        if (obj.getValorParcela().doubleValue() == 0) {
            throw new ConsistirException("O campo VALOR PARCELA (Vencimento Específico) deve ser informado.");
        }
        /*
         * if (obj.getNrParcela().intValue() == 0) { throw new
         * ConsistirException
         * ("O campo NRº PARCELA (Vencimento Específico) deve ser informado.");
         * }
         */
    }

    public Integer getNrParcela() {
        if (nrParcela == null) {
            nrParcela = 0;
        }
        return (nrParcela);
    }

    public void setNrParcela(Integer nrParcela) {
        this.nrParcela = nrParcela;
    }

    public Double getValorParcela() {
        if (valorParcela == null) {
            valorParcela = 0.0;
        }
        return (valorParcela);
    }

    public void setValorParcela(Double valorParcela) {
        this.valorParcela = valorParcela;
    }

    public Date getDataVencimento() {
        if (dataVencimento == null) {
            dataVencimento = new Date();
        }
        return (dataVencimento);
    }

    public Long getOrdenacao() {
        return (dataVencimento.getTime());
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataVencimento_Apresentar() {
        return (Uteis.getData(dataVencimento));
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Integer getContratoDespesa() {
        if (contratoDespesa == null) {
            contratoDespesa = 0;
        }
        return (contratoDespesa);
    }

    public void setContratoDespesa(Integer contratoDespesa) {
        this.contratoDespesa = contratoDespesa;
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

	public String getNumeroDocumento() {
		if(numeroDocumento == null){
			numeroDocumento = "";
		}
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
    
    
}
