package webservice.nfse.araguaina;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class CpfCnpj {
	
	@XStreamAlias("Cnpj")
	private String Cnpj;
	
	@XStreamAlias("Cpf")
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