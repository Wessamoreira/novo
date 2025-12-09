package webservice.arquitetura;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "infoItemWSVO")
public class InfoItemWSVO {

	private String campo;
	private String mensagem;
	private String valor;

	@XmlElement(name = "campo")
	public String getCampo() {
		if (campo == null) {
			campo = "";
		}
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	@XmlElement(name = "mensagem")
	public String getMensagem() {
		if (mensagem == null) {
			mensagem = "";
		}
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	@XmlElement(name = "valor")
	public String getValor() {
		if (valor == null) {
			valor = "";
		}
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
