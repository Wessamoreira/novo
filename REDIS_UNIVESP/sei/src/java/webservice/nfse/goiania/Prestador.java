package webservice.nfse.goiania;

public class Prestador {
	
	private CpfCnpj CpfCnpj;
	
	private String InscricaoMunicipal;
	
	public Prestador() {
		CpfCnpj = new CpfCnpj();
	}

	public CpfCnpj getCpfCnpj() {
		return CpfCnpj;
	}

	public void setCpfCnpj(CpfCnpj cpfCnpj) {
		CpfCnpj = cpfCnpj;
	}

	public String getInscricaoMunicipal() {
		return InscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		InscricaoMunicipal = inscricaoMunicipal;
	}
}
