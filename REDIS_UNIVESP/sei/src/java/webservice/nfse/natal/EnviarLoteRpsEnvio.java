package webservice.nfse.natal;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("EnviarLoteRpsEnvio")
public class EnviarLoteRpsEnvio {
	
	
	@XStreamAsAttribute
	private String xmlns = "http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd";

	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsd")
	private String xsd = "http://www.w3.org/2001/XMLSchema";
	
	
	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsi")
	private String xsi = "http://www.w3.org/2001/XMLSchema-instance";
	
	 
	private LoteRps LoteRps;

	public EnviarLoteRpsEnvio() {
	}

	@Override
	public String toString() {
		return "EnviarLoteRpsSincronoEnvio [LoteRps=" + LoteRps + "]";
	}

	public void setLoteRps(LoteRps loteRps) {
		LoteRps = loteRps;		
	}

	public LoteRps getLoteRps() {
		return LoteRps;
	}

}