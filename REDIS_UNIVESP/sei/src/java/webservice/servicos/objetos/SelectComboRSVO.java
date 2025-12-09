package webservice.servicos.objetos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
