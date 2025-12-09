package webservice.boletoonline.itau.comuns;

public class DebitoVO {
	
	private String agencia_debito;
	private String conta_debito;
	private String digito_verificador_conta_debito;
	
	public String getAgencia_debito() {
		if(agencia_debito == null) {
			agencia_debito = "";
		}
		return agencia_debito;
	}
	
	public void setAgencia_debito(String agencia_debito) {
		this.agencia_debito = agencia_debito;
	}
	
	public String getConta_debito() {
		if(conta_debito == null) {
			conta_debito = "";
		}
		return conta_debito;
	}
	
	public void setConta_debito(String conta_debito) {
		this.conta_debito = conta_debito;
	}
	
	public String getDigito_verificador_conta_debito() {
		if(digito_verificador_conta_debito == null) {
			digito_verificador_conta_debito = "";
		}
		return digito_verificador_conta_debito;
	}
	
	public void setDigito_verificador_conta_debito(String digito_verificador_conta_debito) {
		this.digito_verificador_conta_debito = digito_verificador_conta_debito;
	}
}
