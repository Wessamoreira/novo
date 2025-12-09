package webservice.nfse.anapolis;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("tc:CpfCnpj")
public class CpfCnpj {
	
	@XStreamAlias("tc:Cnpj")
	private String Cnpj;
	
	@XStreamAlias("tc:Cpf")
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