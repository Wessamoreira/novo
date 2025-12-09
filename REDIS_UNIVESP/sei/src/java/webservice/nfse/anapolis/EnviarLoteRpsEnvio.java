package webservice.nfse.anapolis;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("EnviarLoteRpsEnvio")
public class EnviarLoteRpsEnvio {

	@XStreamAsAttribute
	private String xmlns = "http://www.issnetonline.com.br/webserviceabrasf/vsd/servico_enviar_lote_rps_envio.xsd";
	 
	@XStreamAsAttribute
	@XStreamAlias("xmlns:tc")
	private String xsi = "http://www.issnetonline.com.br/webserviceabrasf/vsd/tipos_complexos.xsd";
	
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