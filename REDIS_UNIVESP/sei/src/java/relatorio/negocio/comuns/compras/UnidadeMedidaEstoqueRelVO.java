/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.compras;

/**
 * 
 * @author Manoel
 */
public class UnidadeMedidaEstoqueRelVO {

	private String siglaUnidade;
	private Double qtdTotal;

	public String getSiglaUnidade() {
		if (siglaUnidade == null) {
			siglaUnidade = "";
		}
		return siglaUnidade;
	}

	public void setSiglaUnidade(String siglaUnidade) {
		this.siglaUnidade = siglaUnidade;
	}

	public Double getQtdTotal() {
		if (qtdTotal == null) {
			qtdTotal = 0.0;
		}
		return qtdTotal;
	}

	public void setQtdTotal(Double qtdTotal) {
		this.qtdTotal = qtdTotal;
	}

}
