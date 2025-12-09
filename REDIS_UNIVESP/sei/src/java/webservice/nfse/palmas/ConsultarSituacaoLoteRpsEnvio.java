package webservice.nfse.palmas;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("ConsultarSituacaoLoteRpsEnvio")
public class ConsultarSituacaoLoteRpsEnvio {
	
	
	@XStreamAsAttribute
	private String xmlns = "http://www.abrasf.org.br/ABRASF/arquivos/nfse.xsd";

	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsd")
	private String xsd = "http://www.w3.org/2001/XMLSchema";
	
	
	@XStreamAsAttribute
	@XStreamAlias("xmlns:xsi")
	private String xsi = "http://www.w3.org/2001/XMLSchema-instance";
	
	 
	private Prestador Prestador;
	String Protocolo;

	public ConsultarSituacaoLoteRpsEnvio() {
	}

	@Override
	public String toString() {
		return "ConsultarLoteRpsEnvio [Prestador=" + Prestador + " Protocolo="+Protocolo+"]";
	}

	public void setPrestador(Prestador Prestador) {
		this.Prestador = Prestador;		
	}

	public void setProtocolo(String protocolo) {
		Protocolo = protocolo;
	}
	
	

}