package webservice.pix.comuns;

public class DescontoDataFixaPixRSVO  {
	
	
	
	/**
	 * title: Data limite para o desconto absoluto da cobrança
	 * example: 2020-04-01
	 * Descontos por pagamento antecipado, com data fixa.
	 *  Matriz com até três elementos, sendo que cada elemento é composto por um par "data e valorPerc", 
	 *  para estabelecer descontos percentuais ou absolutos, até aquela data de pagamento. 
	 *  Trata-se de uma data, no formato yyyy-mm-dd, segundo ISO 8601. 
	 *  A data de desconto obrigatoriamente deverá ser menor que a data de vencimento da cobrança.
	 */
	private String data;
	/*
	 * pattern: \d{1,10}\.\d{2}
	 */
	private String valorPerc;
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getValorPerc() {
		return valorPerc;
	}

	public void setValorPerc(String valorPerc) {
		this.valorPerc = valorPerc;
	}
	
	

}
