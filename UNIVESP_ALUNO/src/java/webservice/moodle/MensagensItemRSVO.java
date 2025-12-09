package webservice.moodle;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.SerializedName;

import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.utilitarias.Uteis;

public class MensagensItemRSVO {

	@SerializedName("email_remetente")
	private String emailRemetente;
	@SerializedName("enviarPorEmail")
	private Boolean enviarPorEmail;
	@SerializedName("enviarSms")
	private Boolean enviarSms;
	@SerializedName("mensagem")
	private String mensagem;
	@SerializedName("assunto")
	private String assunto;
	@SerializedName("sms")
	private String sms;
	@SerializedName("email_destinatarios")
	private String emailDestinatarios;
	@ExcluirJsonAnnotation @JsonIgnore
	private Integer quantidadeDestinatarios;
	@ExcluirJsonAnnotation @JsonIgnore
	private List<String> listaEmailDestinatarios;

	public String getEmailRemetente() {
		if (emailRemetente == null) {
			emailRemetente = "";
		}
		return emailRemetente;
	}

	public void setEmailRemetente(String emailRemetente) {
		this.emailRemetente = emailRemetente;
	}

	public Boolean getEnviarPorEmail() {
		if (enviarPorEmail == null) {
			enviarPorEmail = true;
		}
		return enviarPorEmail;
	}

	public void setEnviarPorEmail(Boolean enviarPorEmail) {
		this.enviarPorEmail = enviarPorEmail;
	}

	public Boolean getEnviarSms() {
		if (enviarSms == null) {
			enviarSms = false;
		}
		return enviarSms;
	}

	public void setEnviarSms(Boolean enviarSms) {
		this.enviarSms = enviarSms;
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

	public String getAssunto() {
		if (assunto == null) {
			assunto = "Mensagem Enviada pelo MOODLE";
		}
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getSms() {
		if (sms == null) {
			sms = "";
		}
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public String getEmailDestinatarios() {
		if (emailDestinatarios == null) {
			emailDestinatarios = "";
		}
		return emailDestinatarios;
	}

	public void setEmailDestinatarios(String emailDestinatarios) {
		this.emailDestinatarios = emailDestinatarios;
	}
	
	public Integer getQuantidadeDestinatarios() {
		if (quantidadeDestinatarios == null && Uteis.isAtributoPreenchido(getListaEmailDestinatarios())) {
			quantidadeDestinatarios = getListaEmailDestinatarios().size();
		}
		return quantidadeDestinatarios;
	}
	
	public List<String> getListaEmailDestinatarios() {
		if (listaEmailDestinatarios == null && Uteis.isAtributoPreenchido(getEmailDestinatarios())) {
			List<String> emailsLocalizados = Stream.of(getEmailDestinatarios().replace(" ", "").split(",")).collect(Collectors.toList());
			if (Uteis.isAtributoPreenchido(emailsLocalizados)) {
				listaEmailDestinatarios = emailsLocalizados;
			}
		}
		return listaEmailDestinatarios;
	}
	
	public void setListaEmailDestinatarios(List<String> listaEmailDestinatarios) {
		this.listaEmailDestinatarios = listaEmailDestinatarios;
	}
}