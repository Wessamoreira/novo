package webservice.nfse.joaopessoa;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class CpfCnpj {
	
	@XStreamAlias("nfse:Cnpj")
	private String Cnpj;
	
	@XStreamAlias("nfse:Cpf")
	private String Cpf;
	
	public CpfCnpj() {
	}

	public void setCpf(String cpf) {
		Cpf = cpf;
	}

	public String getCnpj() {
		return Cnpj;
	}

	public void setCnpj(String cnpj) {
		Cnpj = cnpj;
	}

	public String getCpf() {
		return Cpf;
	}

	@Override
	public String toString() {
		return "CpfCnpj [Cnpj=" + Cnpj + ", Cpf=" + Cpf + "]";
	}
	
}