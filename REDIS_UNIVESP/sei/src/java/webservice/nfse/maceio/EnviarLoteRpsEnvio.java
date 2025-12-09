package webservice.nfse.maceio;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("ns2:EnviarLoteRpsEnvio")
public class EnviarLoteRpsEnvio {

	@XStreamAsAttribute
	@XStreamAlias("xmlns:ns2")
	private String xmlns = "http://www.ginfes.com.br/servico_enviar_lote_rps_envio_v03.xsd";
	
	@XStreamAsAttribute
	@XStreamAlias("xmlns:ns3")
	private String xmlnsTipos = "http://www.ginfes.com.br/tipos_v03.xsd";
	
	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsd")
	private String xsd = "http://www.w3.org/2001/XMLSchema";
	
	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsi")
	private String xsi = "http://www.w3.org/2001/XMLSchema-instance";
	
	@XStreamAlias("ns2:LoteRps")
	private LoteRps LoteRps;

	public LoteRps getLoteRps() {
		return LoteRps;
	}

	public void setLoteRps(LoteRps loteRps) {
		LoteRps = loteRps;
	}

	@Override
	public String toString() {
		return "EnviarLoteRpsEnvio [LoteRps=" + LoteRps + "]";
	}
}