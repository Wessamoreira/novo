package webservice.pix.comuns;

public class JuroPixRSVO {
	
	/*
	 * Valores aceito de 1 ate 8
	 * Sendo 1 valor dias corridos
	 * Sendo 2 percentual ao dia corrido
	 * Sendo 3 percentual ao mes dias corridos
	 * Sendo 4 percentual ao ano dias corridos
	 * Sendo 5 valor dia uteis
	 * Sendo 6 percentual ao dia uteis
	 * Sendo 7 percentual ao mes dias uteis
	 * Sendo 8 percentual ao ano dias uteis 
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
