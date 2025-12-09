package webservice.nfse.vitoria;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("GerarNfseEnvio")
public class GerarNfse {
	
	@XStreamAsAttribute
	private String xmlns = "http://www.abrasf.org.br/nfse.xsd";
	
//	@XStreamAsAttribute
//	@XStreamAlias("xmlns:xsi")
//	private String xsi = "http://www.w3.org/2001/XMLSchema-instance";
//	
//	@XStreamAsAttribute
//	@XStreamAlias("xmlns:xsd")
//	private String xsd = "http://www.w3.org/2001/XMLSchema";
	
	private LoteRps LoteRps;

	public GerarNfse() {
		
	}

	@Override
	public String toString() {
		return "GerarNfse [LoteRps=" + LoteRps + "]";
	}

	public void setLoteRps(LoteRps loteRps) {
		LoteRps = loteRps;		
	}
}
