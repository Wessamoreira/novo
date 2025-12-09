package webservice.nfse.anapolis;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("ConsultarSituacaoLoteRpsEnvio")
public class ConsultarSituacaoLoteRpsEnvio {
	
	@XStreamAsAttribute
	private String xmlns = "http://www.issnetonline.com.br/webserviceabrasf/vsd/servico_consultar_situacao_lote_rps_envio.xsd";

	@XStreamAsAttribute
	@XStreamAlias("xmlns:tc")
	private String tc = "http://www.issnetonline.com.br/webserviceabrasf/vsd/tipos_complexos.xsd";
	 
	private Prestador Prestador;
	String Protocolo;

	public ConsultarSituacaoLoteRpsEnvio() {
	}

	@Override
	public String toString() {
		return "ConsultarSituacaoLoteRpsEnvio [Prestador=" + Prestador + " Protocolo="+Protocolo+"]";
	}

	public void setPrestador(Prestador Prestador) {
		this.Prestador = Prestador;		
	}

	public void setProtocolo(String protocolo) {
		Protocolo = protocolo;
	}
	
	

}