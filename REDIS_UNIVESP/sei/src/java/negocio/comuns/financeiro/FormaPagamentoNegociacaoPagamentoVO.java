package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.financeiro.NegociacaoPagamento;

/**
 * Reponsável por manter os dados da entidade Pagamento. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see NegociacaoPagamento
 */
public class FormaPagamentoNegociacaoPagamentoVO extends SuperVO {

	private Integer codigo;
	private Double valor;
	private Integer negociacaoContaPagar;

	private OperadoraCartaoVO operadoraCartaoVO;
	private CategoriaDespesaVO categoriaDespesaVO;
	private ContaCorrenteVO contaCorrenteOperadoraCartaoVO;
	private Integer qtdeParcelasCartaoCredito;
	private String cidadeDataPagamentoPorExtenso;
	private String nomeResponsavel;
	private String nomeAluno;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>FormaPagamento </code>.
	 */
	private FormaPagamentoVO formaPagamento;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>ContaCorrente </code>.
	 */
	private ContaCorrenteVO contaCorrente;
	private ChequeVO cheque;
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>Pagamento</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public FormaPagamentoNegociacaoPagamentoVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>PagamentoVO</code>. Todos os tipos de consistência de dados são e
	 * devem ser implementadas neste método. São validações típicas: verificação
	 * de campos obrigatórios, verificação de valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(FormaPagamentoNegociacaoPagamentoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if ((obj.getFormaPagamento() == null) || (obj.getFormaPagamento().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo FORMA PAGAMENTO (Pagamento) deve ser informado.");
		}
		if (obj.getInformarContaCorrente()) {
			if (obj.getContaCorrente() == null || obj.getContaCorrente().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo CONTA CORRENTE (Pagamento) deve ser informado.");
			}
		}
		if (obj.getInformarContaCaixa()) {
			if (obj.getContaCorrente() == null || obj.getContaCorrente().getCodigo().intValue() == 0) {
				throw new ConsistirException("O campo CAIXA (Pagamento) deve ser informado.");
			}
		}
		if (obj.getFormaPagamento().getTipo().equals("CH")) {
			if (obj.getCheque() == null) {
				throw new ConsistirException("Deve ser informado ao menos um CHEQUE (Pagamento).");
			}
		}
		if (!obj.getFormaPagamento().getTipo().equals("IS") && obj.getValor().doubleValue() == 0) {
			throw new ConsistirException("O campo VALOR (Pagamento) deve ser informado.");
		}
	}

	public Boolean getInformarContaCorrente() {
		if (getFormaPagamento().getTipo() == null) {
			getFormaPagamento().setTipo("");
		}
		if (getFormaPagamento().getTipo().equals("CA") || getFormaPagamento().getTipo().equals("BO") || getFormaPagamento().getTipo().equals("DE") || getFormaPagamento().getTipo().equals("CD") || getFormaPagamento().getTipo().equals("DC")) {
			return true;
		}
		return false;
	}

	public Boolean getIsInformaOperadoraCartao() {
		if (getFormaPagamento().getTipo() == null) {
			getFormaPagamento().setTipo("");
		}
		if (getFormaPagamento().getTipo().equals("CA")) {
			return true;
		}
		return false;
	}

	public Boolean getInformarContaCaixa() {
		if (getFormaPagamento().getTipo() == null) {
			getFormaPagamento().setTipo("");
		}
		if (getFormaPagamento().getTipo().equals("DI") || getFormaPagamento().getTipo().equals("IS")) {
			return true;
		}
		return false;
	}

	public String getTipoFormaPagamento() {

		if (getFormaPagamento().getTipo() == null) {
			getFormaPagamento().setTipo("");
		}
		if (getFormaPagamento().getTipo().equals("CH")) {
			return "RichFaces.$('panelCheque').show()";
		}
		return "";
	}

	public Boolean getInformaValor() {
		if (getFormaPagamento().getTipo() == null) {
			getFormaPagamento().setTipo("");
		}
		if (getFormaPagamento().getTipo().equals("CH")) {
			return false;
		}
		return true;
	}

	public ChequeVO getCheque() {
		if (cheque == null) {
			cheque = new ChequeVO();
		}
		return cheque;
	}

	public void setCheque(ChequeVO cheque) {
		this.cheque = cheque;
	}

	/**
	 * Retorna o objeto da classe <code>ContaCorrente</code> relacionado com (
	 * <code>Pagamento</code>).
	 */
	public ContaCorrenteVO getContaCorrente() {
		if (contaCorrente == null) {
			contaCorrente = new ContaCorrenteVO();
		}
		return (contaCorrente);
	}

	/**
	 * Define o objeto da classe <code>ContaCorrente</code> relacionado com (
	 * <code>Pagamento</code>).
	 */
	public void setContaCorrente(ContaCorrenteVO obj) {
		this.contaCorrente = obj;
	}

	/**
	 * Retorna o objeto da classe <code>FormaPagamento</code> relacionado com (
	 * <code>Pagamento</code>).
	 */
	public FormaPagamentoVO getFormaPagamento() {
		if (formaPagamento == null) {
			formaPagamento = new FormaPagamentoVO();
		}
		return (formaPagamento);
	}

	/**
	 * Define o objeto da classe <code>FormaPagamento</code> relacionado com (
	 * <code>Pagamento</code>).
	 */
	public void setFormaPagamento(FormaPagamentoVO obj) {
		this.formaPagamento = obj;
	}

	public Integer getNegociacaoContaPagar() {
		if (negociacaoContaPagar == null) {
			negociacaoContaPagar = 0;
		}
		return (negociacaoContaPagar);
	}

	public void setNegociacaoContaPagar(Integer negociacaoContaPagar) {
		this.negociacaoContaPagar = negociacaoContaPagar;
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

	public OperadoraCartaoVO getOperadoraCartaoVO() {
		if (operadoraCartaoVO == null) {
			operadoraCartaoVO = new OperadoraCartaoVO();
		}
		return operadoraCartaoVO;
	}

	public void setOperadoraCartaoVO(OperadoraCartaoVO operadoraCartaoVO) {
		this.operadoraCartaoVO = operadoraCartaoVO;
	}

	public CategoriaDespesaVO getCategoriaDespesaVO() {
		if (categoriaDespesaVO == null) {
			categoriaDespesaVO = new CategoriaDespesaVO();
		}
		return categoriaDespesaVO;
	}

	public void setCategoriaDespesaVO(CategoriaDespesaVO categoriaDespesaVO) {
		this.categoriaDespesaVO = categoriaDespesaVO;
	}

	public ContaCorrenteVO getContaCorrenteOperadoraCartaoVO() {
		if (contaCorrenteOperadoraCartaoVO == null) {
			contaCorrenteOperadoraCartaoVO = new ContaCorrenteVO();
		}
		return contaCorrenteOperadoraCartaoVO;
	}

	public void setContaCorrenteOperadoraCartaoVO(ContaCorrenteVO contaCorrenteOperadoraCartaoVO) {
		this.contaCorrenteOperadoraCartaoVO = contaCorrenteOperadoraCartaoVO;
	}

	public Integer getQtdeParcelasCartaoCredito() {
		if (qtdeParcelasCartaoCredito == null) {
			qtdeParcelasCartaoCredito = 0;
		}
		return qtdeParcelasCartaoCredito;
	}

	public void setQtdeParcelasCartaoCredito(Integer qtdeParcelasCartaoCredito) {
		this.qtdeParcelasCartaoCredito = qtdeParcelasCartaoCredito;
	}

	public Boolean getIsInformaQtdeParcelasCartaoCredito() {
		if (getFormaPagamento().getTipo() == null) {
			getFormaPagamento().setTipo("");
		}
		if (getFormaPagamento().getTipo().equals("CA")) {
			return true;
		}
		return false;
	}

	public String getCidadeDataPagamentoPorExtenso() {
		return cidadeDataPagamentoPorExtenso;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public void setCidadeDataPagamentoPorExtenso(String cidadeDataPagamentoPorExtenso) {
		this.cidadeDataPagamentoPorExtenso = cidadeDataPagamentoPorExtenso;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public String getNomeAluno() {
		if (nomeAluno == null) {
			nomeAluno = "";
		}
		return nomeAluno;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}
}
