package webservice.nfse.anapolis;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("GerarNfseEnvio")
public class GerarNfse {
	
	@XStreamAsAttribute
	private String xmlns = "http://nfse.goiania.go.gov.br/xsd/nfse_gyn_v02.xsd";
	
//	@XStreamAsAttribute
//	@XStreamAlias("xmlns:xsi")
//	private String xsi = "http://www.w3.org/2001/XMLSchema-instance";
//	
//	@XStreamAsAttribute
//	@XStreamAlias("xmlns:xsd")
//	private String xsd = "http://www.w3.org/2001/XMLSchema";
	
	private Rps Rps;

	public GerarNfse() {
		
	}

	@Override
	public String toString() {
		return "GerarNfseEnvio [Rps=" + Rps + "]";
	}

	public Rps getRps() {
		return Rps;
	}

	public void setRps(Rps Rps) {
		this.Rps = Rps;
	}
}
