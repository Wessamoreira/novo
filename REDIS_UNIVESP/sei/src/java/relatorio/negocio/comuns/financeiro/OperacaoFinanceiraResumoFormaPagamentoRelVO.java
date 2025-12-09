/**
 * 
 */
package relatorio.negocio.comuns.financeiro;

import negocio.comuns.compras.FormaPagamentoVO;

/**
 * @author Rodrigo Wind
 *
 */
public class OperacaoFinanceiraResumoFormaPagamentoRelVO {
	/**
	 * @author Rodrigo Wind - 04/11/2015
	 */
	private FormaPagamentoVO formaPagamentoVO;
	private Double totalEntrada;
	private Double totalSaida;
	private Double totalEntradaTroco;
	private Double totalSaidaTroco;
	
	/**
	 * @return the formaPagamentoVO
	 */
	public FormaPagamentoVO getFormaPagamentoVO() {
		if (formaPagamentoVO == null) {
			formaPagamentoVO = new FormaPagamentoVO();
		}
		return formaPagamentoVO;
	}
	/**
	 * @param formaPagamentoVO the formaPagamentoVO to set
	 */
	public void setFormaPagamentoVO(FormaPagamentoVO formaPagamentoVO) {
		this.formaPagamentoVO = formaPagamentoVO;
	}
	/**
	 * @return the totalEntrada
	 */
	public Double getTotalEntrada() {
		if (totalEntrada == null) {
			totalEntrada = 0.0;
		}
		return totalEntrada;
	}
	/**
	 * @param totalEntrada the totalEntrada to set
	 */
	public void setTotalEntrada(Double totalEntrada) {
		this.totalEntrada = totalEntrada;
	}
	/**
	 * @return the totalSaida
	 */
	public Double getTotalSaida() {
		if (totalSaida == null) {
			totalSaida = 0.0;
		}
		return totalSaida;
	}
	/**
	 * @param totalSaida the totalSaida to set
	 */
	public void setTotalSaida(Double totalSaida) {
		this.totalSaida = totalSaida;
	}
	/**
	 * @return the saldo
	 */
	public Double getSaldo() {		
		return (getTotalEntrada() + getTotalEntradaTroco()) - (getTotalSaida() + getTotalSaidaTroco());
	}
	
	public Double getTotalEntradaTroco() {
		if (totalEntradaTroco == null) {
			totalEntradaTroco = 0.0;
		}
		return totalEntradaTroco;
	}
	public void setTotalEntradaTroco(Double totalEntradaTroco) {
		this.totalEntradaTroco = totalEntradaTroco;
	}
	public Double getTotalSaidaTroco() {
		if (totalSaidaTroco == null) {
			totalSaidaTroco = 0.0;
		}
		return totalSaidaTroco;
	}
	public void setTotalSaidaTroco(Double totalSaidaTroco) {
		this.totalSaidaTroco = totalSaidaTroco;
	}
	
}
