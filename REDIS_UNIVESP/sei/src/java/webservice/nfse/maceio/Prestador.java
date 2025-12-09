package webservice.nfse.maceio;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class Prestador {
	
	@XStreamAlias("ns3:Cnpj")
	private String Cnpj;
	
	@XStreamAlias("ns3:InscricaoMunicipal")
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
