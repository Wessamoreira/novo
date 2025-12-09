package relatorio.negocio.comuns.administrativo;

import java.util.Date;

public class ComunicacaoInternaRelVO {
	
	private String remetente;
	private String destinatario;
	private String assunto;
	private String mensagem;
	private Date dataEnvio;
	private Date dataLeitura;
	private Boolean leituraRegistrada;
	
	public String getRemetente() {
		if (remetente == null) {
			remetente = "";
		}
		return remetente;
	}
	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}
	public String getDestinatario() {
		if (destinatario == null) {
			destinatario = "";
		}
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String getAssunto() {
		if (assunto == null) {
			assunto = "";
		}
		return assunto;
	}
	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}
	public String getMensagem() {
		if (mensagem == null) {
			mensagem = "";
		}
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	public Date getDataEnvio() {
		if (dataEnvio == null) {
			dataEnvio = new Date();
		}
		return dataEnvio;
	}
	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}
	public Date getDataLeitura() {
		if (dataLeitura == null) {
			dataLeitura = new Date();
		}
		return dataLeitura;
	}
	public void setDataLeitura(Date dataLeitura) {
		this.dataLeitura = dataLeitura;
	}
	public Boolean getLeituraRegistrada() {
		if (leituraRegistrada == null) {
			leituraRegistrada = Boolean.FALSE;
		}
		return leituraRegistrada;
	}
	public void setLeituraRegistrada(Boolean leituraRegistrada) {
		this.leituraRegistrada = leituraRegistrada;
	}

}
