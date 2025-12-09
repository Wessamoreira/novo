package webservice.pix.comuns;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;

@XmlRootElement(name = "")
public class WebhookRSVO {
	
	@ExcluirJsonAnnotation
	private List<PixRSVO> pix;	
	private String webhookUrl;
	private String chave;
	private String criacao;
	
	@XmlElement(name = "pix")
	public List<PixRSVO> getPix() {
		return pix;
	}

	public void setPix(List<PixRSVO> pix) {
		this.pix = pix;
	}

	public String getWebhookUrl() {
		return webhookUrl;
	}

	public void setWebhookUrl(String webhookUrl) {
		this.webhookUrl = webhookUrl;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getCriacao() {
		return criacao;
	}

	public void setCriacao(String criacao) {
		this.criacao = criacao;
	}	
	
	
	
	

}
