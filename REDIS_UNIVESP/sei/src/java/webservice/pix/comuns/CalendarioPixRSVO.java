package webservice.pix.comuns;

public class CalendarioPixRSVO {

	/*
	 * Timestamp que indica o momento em que foi criada a cobrança. Respeita o formato definido na RFC 3339.
	 */
	private String criacao;
	/*
	 * Trata-se de uma data, no formato yyyy-mm-dd, segundo ISO 8601. É a data de vencimento da cobrança. A cobrança pode ser honrada até esse dia, inclusive, em qualquer horário do dia.
	 */
	private String dataDeVencimento;
	/*
	 * default: 30 Trata-se da quantidade de dias corridos após o vencimento em que a cobrança poderá ser paga. Caso vencido este período e a cobrança não tenha sido paga, esta continuará ATIVA.
	 */
	private Integer validadeAposVencimento;
	
	private Integer expiracao;

	public String getCriacao() {
		return criacao;
	}

	public void setCriacao(String criacao) {
		this.criacao = criacao;
	}

	public String getDataDeVencimento() {
		return dataDeVencimento;
	}

	public void setDataDeVencimento(String dataDeVencimento) {
		this.dataDeVencimento = dataDeVencimento;
	}

	public Integer getValidadeAposVencimento() {
		return validadeAposVencimento;
	}

	public void setValidadeAposVencimento(Integer validadeAposVencimento) {
		this.validadeAposVencimento = validadeAposVencimento;
	}

	public Integer getExpiracao() {
		return expiracao;
	}

	public void setExpiracao(Integer expiracao) {
		this.expiracao = expiracao;
	}
	
	

}
