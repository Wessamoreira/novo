package integracoes.cartao.cielo.sdk;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;

public class SaleFormaPagamentoNegociacaoRecebimentoVO {
	
	private Sale sale;
	private String numeroCartao;
	private List<FormaPagamentoNegociacaoRecebimentoVO> listaFormaPagamentoNegociacaoRecebimentoVOs;
	

	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public String getNumeroCartao() {
		if (numeroCartao == null) {
			numeroCartao = "";
		}
		return numeroCartao;
	}

	public void setNumeroCartao(String numeroCartao) {
		this.numeroCartao = numeroCartao;
	}

	public List<FormaPagamentoNegociacaoRecebimentoVO> getListaFormaPagamentoNegociacaoRecebimentoVOs() {
		if (listaFormaPagamentoNegociacaoRecebimentoVOs == null) {
			listaFormaPagamentoNegociacaoRecebimentoVOs = new ArrayList<>();
		}
		return listaFormaPagamentoNegociacaoRecebimentoVOs;
	}

	public void setListaFormaPagamentoNegociacaoRecebimentoVOs(List<FormaPagamentoNegociacaoRecebimentoVO> listaFormaPagamentoNegociacaoRecebimentoVOs) {
		this.listaFormaPagamentoNegociacaoRecebimentoVOs = listaFormaPagamentoNegociacaoRecebimentoVOs;
	}

	@Override
	public String toString() {
		return "SaleFormaPagamentoNegociacaoRecebimentoVO [numeroCartao=" + numeroCartao + "]";
	}

}
