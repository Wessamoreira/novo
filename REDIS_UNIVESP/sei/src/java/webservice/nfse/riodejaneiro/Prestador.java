package webservice.nfse.riodejaneiro;

public class Prestador {
	
	private String Cnpj;
	
	private String InscricaoMunicipal;
	
	public Prestador() {
		
	}

	public String getInscricaoMunicipal() {
		return InscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		InscricaoMunicipal = inscricaoMunicipal;
	}

	public String getCnpj() {
		return Cnpj;
	}

	public void setCnpj(String cnpj) {
		Cnpj = cnpj;
	}
}
