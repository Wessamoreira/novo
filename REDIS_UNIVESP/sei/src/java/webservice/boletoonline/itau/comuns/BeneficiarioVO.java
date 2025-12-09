package webservice.boletoonline.itau.comuns;

public class BeneficiarioVO {
	
	private String cpf_cnpj_beneficiario;
	private String agencia_beneficiario;
	private String conta_beneficiario;
	private String digito_verificador_conta_beneficiario;
	
	public String getCpf_cnpj_beneficiario() {
		if(cpf_cnpj_beneficiario == null) {
			cpf_cnpj_beneficiario = "";
		}
		return cpf_cnpj_beneficiario;
	}
	
	public void setCpf_cnpj_beneficiario(String cpf_cnpj_beneficiario) {
		this.cpf_cnpj_beneficiario = cpf_cnpj_beneficiario;
	}
	
	public String getAgencia_beneficiario() {
		if(agencia_beneficiario == null) {
			agencia_beneficiario = "";
		}
		return agencia_beneficiario;
	}
	
	public void setAgencia_beneficiario(String agencia_beneficiario) {
		this.agencia_beneficiario = agencia_beneficiario;
	}
	
	public String getConta_beneficiario() {
		if(conta_beneficiario == null) {
			conta_beneficiario ="";
		}
		return conta_beneficiario;
	}
	
	public void setConta_beneficiario(String conta_beneficiario) {
		this.conta_beneficiario = conta_beneficiario;
	}
	
	public String getDigito_verificador_conta_beneficiario() {
		if(digito_verificador_conta_beneficiario == null) {
			digito_verificador_conta_beneficiario = "";
		}
		return digito_verificador_conta_beneficiario;
	}
	
	public void setDigito_verificador_conta_beneficiario(String digito_verificador_conta_beneficiario) {
		this.digito_verificador_conta_beneficiario = digito_verificador_conta_beneficiario;
	}
}
