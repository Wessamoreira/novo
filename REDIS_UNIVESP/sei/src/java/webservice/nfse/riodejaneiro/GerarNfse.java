package webservice.nfse.riodejaneiro;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("GerarNfseEnvio")
public class GerarNfse {
	
	@XStreamAsAttribute
	private String xmlns = "http://notacarioca.rio.gov.br/WSNacional/XSD/1/nfse_pcrj_v01.xsd";
	
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
