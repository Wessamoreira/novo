package relatorio.negocio.comuns.financeiro;

public class ContasRecebimentoRelVO {

	private String contaCorrente;
	private String contaCorrenteRecebimento;
	private String formaPagamento;
	private Double valorRecebido;
	 private String nomeBanco;

	public String getContaCorrenteRecebimento() {
		if (contaCorrenteRecebimento == null) {
			contaCorrenteRecebimento = "";
		}
		return contaCorrenteRecebimento;
	}

	public void setContaCorrenteRecebimento(String contaCorrenteRecebimento) {
		this.contaCorrenteRecebimento = contaCorrenteRecebimento;
	}
	
	public String getContaCorrente() {
		if (contaCorrente == null) {
			contaCorrente = "";
		}
		return contaCorrente;
	}
	
	public void setContaCorrente(String contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	public String getFormaPagamento() {
		if (formaPagamento == null) {
			formaPagamento = "";
		}
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Double getValorRecebido() {
		if (valorRecebido == null) {
			valorRecebido = 0.0;
		}
		return valorRecebido;
	}

	public void setValorRecebido(Double valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public String getNomeBanco() {
		if (nomeBanco == null) {
			nomeBanco = "";
		}
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

}