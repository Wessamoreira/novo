package webservice.nfse.natal;

public class IdentificacaoTomador {
	
	private CpfCnpj CpfCnpj;
	private String InscricaoMunicipal;
	
	public IdentificacaoTomador() {
		CpfCnpj = new CpfCnpj();
	}

	public CpfCnpj getCpfCnpj() {
		return CpfCnpj;
	}

	public void setCpfCnpj(CpfCnpj cpfCnpj) {
		CpfCnpj = cpfCnpj;
	}
	
	@Override
	public String toString() {
		return "IdentificacaoTomador [CpfCnpj=" + CpfCnpj + "]";
	}

	public String getInscricaoMunicipal() {
		return InscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		InscricaoMunicipal = inscricaoMunicipal;
	}
	
}
