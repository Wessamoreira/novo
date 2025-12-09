package webservice.nfse.natal;

public class Prestador {
	
	private String Cnpj;
	
	private String InscricaoMunicipal;
	
	public Prestador() {
		Cnpj = "";
	}

	public String getCpfCnpj() {
		return Cnpj;
	}

	public void setCnpj(String Cnpj) {
		this.Cnpj = Cnpj;
	}

	public String getInscricaoMunicipal() {
		return InscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		InscricaoMunicipal = inscricaoMunicipal;
	}
}
