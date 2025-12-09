package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.facade.jdbc.financeiro.ContaPagar;

/**
 * Reponsável por manter os dados da entidade ContaPagarPagamento. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see ContaPagar
 */
public class ContaPagarPagamentoVO extends ChequeVO {

    private transient Integer codigo;
    private Integer contaPagar;
    private Date data;
    private Integer negociacaoPagamento;
    private transient Integer pagamento;
    private Double valorTotalPagamento;
    private String tipoPagamento;
    private FormaPagamentoVO formaPagamento;
    private UsuarioVO responsavel;
    private String motivo;
    private FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO;
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ContaPagarPagamento</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public ContaPagarPagamentoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ContaPagarPagamentoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ContaPagarPagamentoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getFormaPagamento() == null) || (obj.getFormaPagamento().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo FORMA DE PAGAMENTO (Conta Receber) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {

        return;

    }

    public String getTipoPagamento_Apresentar() {
        if (tipoPagamento.equals("CR")) {
            return "Crédito";
        }
        if (tipoPagamento.equals("DE")) {
            return "Débito";
        }
        if (tipoPagamento.equals("ES")) {
            return "Estorno";
        }
        return tipoPagamento;
    }

    public Boolean getApresentarMotivo() {
        if (getMotivo().equals("")) {
            return false;
        }
        return true;
    }

    public FormaPagamentoVO getFormaPagamento() {
        if (formaPagamento == null) {
            formaPagamento = new FormaPagamentoVO();
        }
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamentoVO formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getMotivo() {
        if (motivo == null) {
            motivo = "";
        }
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Integer getNegociacaoPagamento() {
        if (negociacaoPagamento == null) {
            negociacaoPagamento = 0;
        }
        return negociacaoPagamento;
    }

    public void setNegociacaoPagamento(Integer negociacaoPagamento) {
        this.negociacaoPagamento = negociacaoPagamento;
    }

    public Integer getPagamento() {
        if (pagamento == null) {
            pagamento = 0;
        }
        return pagamento;
    }

    public void setPagamento(Integer pagamento) {
        this.pagamento = pagamento;
    }

    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return responsavel;
    }

    public void setResponsavel(UsuarioVO responsavel) {
        this.responsavel = responsavel;
    }

    public String getTipoPagamento() {
        if (tipoPagamento == null) {
            tipoPagamento = "";
        }
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public Double getValorTotalPagamento() {
        if (valorTotalPagamento == null) {
            valorTotalPagamento = 0.0;
        }
        return valorTotalPagamento;
    }

    public void setValorTotalPagamento(Double valorTotalPagamento) {
        this.valorTotalPagamento = valorTotalPagamento;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getDataComHora(data));
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getContaPagar() {
        if (contaPagar == null) {
            contaPagar = 0;
        }
        return (contaPagar);
    }

    public void setContaPagar(Integer contaPagar) {
        this.contaPagar = contaPagar;
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
    
    public FormaPagamentoNegociacaoPagamentoVO getFormaPagamentoNegociacaoPagamentoVO() {
    	 if (formaPagamentoNegociacaoPagamentoVO == null) {
    		 formaPagamentoNegociacaoPagamentoVO = new FormaPagamentoNegociacaoPagamentoVO();
         }
		return formaPagamentoNegociacaoPagamentoVO;
	}

	public void setFormaPagamentoNegociacaoPagamentoVO(FormaPagamentoNegociacaoPagamentoVO formaPagamentoNegociacaoPagamentoVO) {
		this.formaPagamentoNegociacaoPagamentoVO = formaPagamentoNegociacaoPagamentoVO;
	}

	public Boolean getApresentarOpcaoVisualizarDadosCheque() {
    	return getFormaPagamento().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor());
    }
}
