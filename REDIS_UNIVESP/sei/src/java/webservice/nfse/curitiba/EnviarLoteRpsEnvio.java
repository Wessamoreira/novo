package webservice.nfse.curitiba;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("EnviarLoteRpsEnvio")
public class EnviarLoteRpsEnvio {

	@XStreamAsAttribute
	private String xmlns = "http://isscuritiba.curitiba.pr.gov.br/iss/nfse.xsd";
	 
	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsi")
	private String xsi = "http://www.w3.org/2001/XMLSchema-instance";
	
	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsd")
	private String xsd = "http://www.w3.org/2001/XMLSchema";
	
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

}