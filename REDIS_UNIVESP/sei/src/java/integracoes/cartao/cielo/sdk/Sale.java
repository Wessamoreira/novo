package integracoes.cartao.cielo.sdk;

import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;

import com.google.gson.annotations.SerializedName;

public class Sale {
	@SerializedName("MerchantOrderId")
	private String merchantOrderId;

	@SerializedName("Customer")
	private Customer customer;

	@SerializedName("Payment")
	private Payment payment;
	
	@ExcluirJsonAnnotation
	private FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO;

	public Sale(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
	}

	public Customer customer(String name) {
		setCustomer(new Customer(name));

		return getCustomer();
	}

	public Payment payment(Integer amount, Integer installments) {
		setPayment(new Payment(amount, installments));

		return getPayment();
	}

	public Payment payment(Integer amount) {
		return payment(amount, 1);
	}

	public Customer getCustomer() {
		return customer;
	}

	public Sale setCustomer(Customer customer) {
		this.customer = customer;
		return this;
	}

	public String getMerchantOrderId() {
		return merchantOrderId;
	}

	public Sale setMerchantOrderId(String merchantOrderId) {
		this.merchantOrderId = merchantOrderId;
		return this;
	}

	public Payment getPayment() {
		return payment;
	}

	public Sale setPayment(Payment payment) {
		this.payment = payment;
		return this;
	}

	public FormaPagamentoNegociacaoRecebimentoVO getFormaPagamentoNegociacaoRecebimentoVO() {
		if(formaPagamentoNegociacaoRecebimentoVO == null){
			formaPagamentoNegociacaoRecebimentoVO = new FormaPagamentoNegociacaoRecebimentoVO();
		}
		
		return formaPagamentoNegociacaoRecebimentoVO;
	}

	public void setFormaPagamentoNegociacaoRecebimentoVO(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO) {
		this.formaPagamentoNegociacaoRecebimentoVO = formaPagamentoNegociacaoRecebimentoVO;
	}

}