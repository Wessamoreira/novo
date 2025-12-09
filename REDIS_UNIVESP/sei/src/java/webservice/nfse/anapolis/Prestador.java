package webservice.nfse.anapolis;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Prestador {
	
	@XStreamAlias("tc:CpfCnpj")
	private CpfCnpj CpfCnpj;
	
	@XStreamAlias("tc:InscricaoMunicipal")
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
