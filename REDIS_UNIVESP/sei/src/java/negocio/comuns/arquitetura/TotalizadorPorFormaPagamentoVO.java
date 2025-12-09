package negocio.comuns.arquitetura;

import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;

public class TotalizadorPorFormaPagamentoVO {

	private FormaPagamentoVO formaPagamentoVO;
	private OperadoraCartaoVO operadoraCartaoVO;
	private String nomeApresentacao;
	private Double valor;

	public String getNomeApresentacao() {
		if (nomeApresentacao == null) {
			nomeApresentacao = "";
		}
		return nomeApresentacao;
	}

	public void setNomeApresentacao(String nomeApresentacao) {
		this.nomeApresentacao = nomeApresentacao;
	}

	public FormaPagamentoVO getFormaPagamentoVO() {
		if (formaPagamentoVO == null) {
			formaPagamentoVO = new FormaPagamentoVO();
		}
		return formaPagamentoVO;
	}

	public void setFormaPagamentoVO(FormaPagamentoVO formaPagamentoVO) {
		this.formaPagamentoVO = formaPagamentoVO;
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

	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

}
