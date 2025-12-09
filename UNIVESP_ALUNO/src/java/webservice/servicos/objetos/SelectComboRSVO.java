package webservice.servicos.objetos;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "selectComboBox")
public class SelectComboRSVO {
	
	private String chave ;
	private String valor ;	
	
	

	public SelectComboRSVO(String chave, String valor) {
		
		this.chave = chave;
		this.valor = valor;
	}
      public SelectComboRSVO() {	
		
	}
	@XmlElement(name = "chave")
	public String getChave() {
		return chave;
	}
	public void setChave(String chave) {
		this.chave = chave;
	}
	@XmlElement(name = "valor")
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
	
}
