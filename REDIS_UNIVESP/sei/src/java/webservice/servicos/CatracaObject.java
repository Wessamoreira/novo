package webservice.servicos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "catraca")
public class CatracaObject {

	private String ip;
	private String serie;
	private Integer resolucao;
	private String mensagemLiberar;
	private String mensagemBloquear;
	private Integer duracaoMensagem;
	private Integer duracaoDesbloquear;
	private String direcao;

	@XmlElement(name = "ip")
	public String getIp() {
		if (ip == null) {
			ip = "";
		}
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@XmlElement(name = "serie")
	public String getSerie() {
		if (serie == null) {
			serie = "";
		}
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	@XmlElement(name = "resolucao")
	public Integer getResolucao() {
		if (resolucao == null) {
			resolucao = 0;
		}
		return resolucao;
	}

	public void setResolucao(Integer resolucao) {
		this.resolucao = resolucao;
	}

	@XmlElement(name = "mensagemLiberar")
	public String getMensagemLiberar() {
		if (mensagemLiberar == null) {
			mensagemLiberar = "";
		}
		return mensagemLiberar;
	}

	public void setMensagemLiberar(String mensagemLiberar) {
		this.mensagemLiberar = mensagemLiberar;
	}

	@XmlElement(name = "mensagemBloquear")
	public String getMensagemBloquear() {
		if (mensagemBloquear == null) {
			mensagemBloquear = "";
		}
		return mensagemBloquear;
	}

	public void setMensagemBloquear(String mensagemBloquear) {
		this.mensagemBloquear = mensagemBloquear;
	}

	@XmlElement(name = "duracaoMensagem")
	public Integer getDuracaoMensagem() {
		if (duracaoMensagem == null) {
			duracaoMensagem = 0;
		}
		return duracaoMensagem;
	}

	public void setDuracaoMensagem(Integer duracaoMensagem) {
		this.duracaoMensagem = duracaoMensagem;
	}

	@XmlElement(name = "duracaoDesbloquear")
	public Integer getDuracaoDesbloquear() {
		if (duracaoDesbloquear == null) {
			duracaoDesbloquear = 0;
		}
		return duracaoDesbloquear;
	}

	public void setDuracaoDesbloquear(Integer duracaoDesbloquear) {
		this.duracaoDesbloquear = duracaoDesbloquear;
	}

	@XmlElement(name = "direcao")
	public String getDirecao() {
		if (direcao == null) {
			direcao = "";
		}
		return direcao;
	}

	public void setDirecao(String direcao) {
		this.direcao = direcao;
	}

}
