package webservice.nfse.londrina;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("GerarNota")
public class GerarNota {
	
	@XStreamAsAttribute
	@XStreamAlias("xmlns:tns")
	private String tns = "http://iss.londrina.pr.gov.br/ws/v1_03";
	
	@XStreamAlias("DescricaoRps")
	private TcDescricaoRps tcDescricaoRps;

	public TcDescricaoRps getTcDescricaoRps() {
		return tcDescricaoRps;
	}

	public void setTcDescricaoRps(TcDescricaoRps tcDescricaoRps) {
		this.tcDescricaoRps = tcDescricaoRps;
	}

	@Override
	public String toString() {
		return "GerarNota [tns=" + tns + ", tcDescricaoRps=" + tcDescricaoRps + "]";
	}
}
