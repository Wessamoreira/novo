package negocio.comuns.ead;

import negocio.comuns.arquitetura.SuperVO;

public class ParametrosGraficoComparativoMeusColegasVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double percentualAcertou;

	public Double getPercentualAcertou() {
		if(percentualAcertou == null) {
			percentualAcertou = 0.0;
		}
		return percentualAcertou;
	}

	public void setPercentualAcertou(Double percentualAcertou) {
		this.percentualAcertou = percentualAcertou;
	}
}
