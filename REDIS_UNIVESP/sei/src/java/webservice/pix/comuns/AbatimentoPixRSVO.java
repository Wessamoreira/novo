package webservice.pix.comuns;

public class AbatimentoPixRSVO {
	
	
	/**
	 * Valores aceito somnete 1 ou 2 
	 * Sendo 1 para Valor Fixo
	 * Sendo 2 para Percentual
	 */
	private Integer modalidade;
	/**
	 * pattern: \d{1,10}\.\d{2}
	 */
	private String valorPerc;
	
	public Integer getModalidade() {
		return modalidade;
	}

	public void setModalidade(Integer modalidade) {
		this.modalidade = modalidade;
	}

	public String getValorPerc() {
		return valorPerc;
	}

	public void setValorPerc(String valorPerc) {
		this.valorPerc = valorPerc;
	}

}
