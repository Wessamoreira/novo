package webservice.nfse.joaopessoa;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("nfse:EnviarLoteRpsSincronoEnvio")
public class EnviarLoteRpsEnvio {

	@XStreamAsAttribute
	@XStreamAlias("xmlns:nfse")
	private String xmlns = "http://www.abrasf.org.br/nfse.xsd";
	 
//	@XStreamAsAttribute
//	@XStreamAlias("xmlns:xsi")
//	private String xsi = "http://www.w3.org/2001/XMLSchema-instance";
//	
//	@XStreamAsAttribute
//	@XStreamAlias("xmlns:xsd")
//	private String xsd = "http://www.w3.org/2001/XMLSchema";
	
	@XStreamAlias("nfse:LoteRps")
	private LoteRps LoteRps;

	public EnviarLoteRpsEnvio() {
	}

	@Override
	public String toString() {
		return "EnviarLoteRpsEnvio [LoteRps=" + LoteRps + "]";
	}

	public void setLoteRps(LoteRps loteRps) {
		LoteRps = loteRps;		
	}

}