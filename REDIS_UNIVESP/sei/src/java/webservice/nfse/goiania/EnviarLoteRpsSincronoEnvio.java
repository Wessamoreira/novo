package webservice.nfse.goiania;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("EnviarLoteRpsSincronoEnvio")
public class EnviarLoteRpsSincronoEnvio {

	//http://www.abrasf.org.br/nfse.xsd
	@XStreamAsAttribute
	private String xmlns = "http://www.abrasf.org.br/nfse.xsd";
	 
	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsi")
	private String xsi = "http://www.w3.org/2001/XMLSchema-instance";
	
	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsd")
	private String xsd = "http://www.w3.org/2001/XMLSchema";
	
	private LoteRps LoteRps;

	public EnviarLoteRpsSincronoEnvio() {
	}

	@Override
	public String toString() {
		return "EnviarLoteRpsSincronoEnvio [LoteRps=" + LoteRps + "]";
	}

	public void setLoteRps(LoteRps loteRps) {
		LoteRps = loteRps;		
	}

	/**
	 * @return the loteRps
	 */
	public LoteRps getLoteRps() {		
		return LoteRps;
	}
	
	

}