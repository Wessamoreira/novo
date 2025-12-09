package webservice;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "dados")
public class EnvelopeBoleto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String xml;
	private String urlChamar;
	private String caminhoCertificado;
	private String senhaCertificado;
	
	public String getXml() {
		return xml;
	}
	
	public void setXml(String xml) {
		this.xml = xml;
	}
	
	public String getUrlChamar() {
		return urlChamar;
	}
	
	public void setUrlChamar(String urlChamar) {
		this.urlChamar = urlChamar;
	}
	
	public String getCaminhoCertificado() {
		return caminhoCertificado;
	}
	
	public void setCaminhoCertificado(String caminhoCertificado) {
		this.caminhoCertificado = caminhoCertificado;
	}
	
	public String getSenhaCertificado() {
		return senhaCertificado;
	}
	
	public void setSenhaCertificado(String senhaCertificado) {
		this.senhaCertificado = senhaCertificado;
	}

}
