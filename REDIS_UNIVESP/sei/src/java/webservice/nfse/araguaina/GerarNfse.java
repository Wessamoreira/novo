package webservice.nfse.araguaina;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("GerarNfseEnvio")
public class GerarNfse {

	@XStreamAsAttribute
	@XStreamAlias("xmlns")
	private String xmlns = "http://www.abrasf.org.br/nfse.xsd";

	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsi")
	private String xsi = "http://www.w3.org/2001/XMLSchema-instance";

	@XStreamAsAttribute
	@XStreamAlias("xsi:schemaLocation")
	private String xsd = "http://www.abrasf.org.br/nfse.xsd";

	@XStreamAlias("Rps")
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
