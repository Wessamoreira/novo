package negocio.comuns.faturamento.nfe;

import negocio.comuns.arquitetura.SuperVO;

public class NotaFiscalSaidaPagamentoVO extends SuperVO {
	
	private String formaPagamento;
	private String condicaoPagamento;
	private String orcamento;
	
	public String getFormaPagamento() {
		if (formaPagamento == null) {
			formaPagamento = "";
		}
		return formaPagamento;
	}
	
	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	
	public String getCondicaoPagamento() {
		if (condicaoPagamento == null) {
			condicaoPagamento = "";
		}
		return condicaoPagamento;
	}
	
	public void setCondicaoPagamento(String condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	public String getOrcamento() {
		if (orcamento == null) {
			orcamento = "";
		}
		return orcamento;
	}

	public void setOrcamento(String orcamento) {
		this.orcamento = orcamento;
	}
	
    public NotaFiscalSaidaPagamentoVO clonar() {
    	NotaFiscalSaidaPagamentoVO pagamento = new NotaFiscalSaidaPagamentoVO();
    	
    	pagamento.setFormaPagamento(getFormaPagamento());
    	pagamento.setCondicaoPagamento(getCondicaoPagamento());
    	pagamento.setOrcamento(getOrcamento());
    
    	return pagamento;
    }

}
