package webservice.nfse.joaopessoa;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("nfse:GerarNfseEnvio")
public class GerarNfse {
	
	@XStreamAsAttribute
	@XStreamAlias("xmlns:nfse")
	private String xmlns = "http://www.abrasf.org.br/nfse.xsd";
	
	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsi")
	private String xsi = "http://www.w3.org/2001/XMLSchema-instance";
	
	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsd")
	private String xsd = "http://www.w3.org/2001/XMLSchema";
	
	@XStreamAlias("nfse:Rps")
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
