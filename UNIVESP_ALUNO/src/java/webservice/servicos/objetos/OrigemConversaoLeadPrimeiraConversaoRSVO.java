package webservice.servicos.objetos;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "conversion_origin")
public class OrigemConversaoLeadPrimeiraConversaoRSVO {
	
	private String fonte;
	private String meio;
	private String valor;
	private String campanha;
	private String canal;
	
	@XmlElement(name = "source")
	public String getFonte() {
		if(fonte == null)
			fonte = "";
		
		return fonte;
	}
	public void setFonte(String fonte) {
		this.fonte = fonte;
	}
	
	@XmlElement(name = "medium")
	public String getMeio() {
		if(meio == null)
			meio = "";
		
		return meio;
	}
	public void setMeio(String meio) {
		this.meio = meio;
	}
	
	@XmlElement(name = "value")
	public String getValor() {
		if(valor == null)
			valor = "";
		
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
	@XmlElement(name = "campaign")
	public String getCampanha() {
		if(campanha == null)
			campanha = "";
		
		return campanha;
	}
	public void setCampanha(String campanha) {
		this.campanha = campanha;
	}
	
	@XmlElement(name = "channel")
	public String getCanal() {
		if(canal == null)
			canal = "";
		
		return canal;
	}
	public void setCanal(String canal) {
		this.canal = canal;
	}
	
}