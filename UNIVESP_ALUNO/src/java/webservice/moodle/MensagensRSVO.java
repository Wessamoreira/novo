package webservice.moodle;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import negocio.comuns.utilitarias.Uteis;

public class MensagensRSVO {

	
	@SerializedName("mensagens")
	private List<MensagensItemRSVO> mensagens;
	@SerializedName("numeroMensagensEnviadas")
	private Integer numeroMensagensEnviadas;
	@SerializedName("emailsNaoLocalizados")
	private List<String> emailsNaoLocalizados;
	
	public List<MensagensItemRSVO> getMensagens() {
		if (mensagens == null)
			mensagens = new ArrayList<>();
		return mensagens;
	}

	public void setMensagens(List<MensagensItemRSVO> mensagens) {
		this.mensagens = mensagens;
	}

	public Integer getNumeroMensagensEnviadas() {
		if (numeroMensagensEnviadas == null) {
			numeroMensagensEnviadas = 0;
		}
		return numeroMensagensEnviadas;
	}

	public void setNumeroMensagensEnviadas(Integer numeroMensagensEnviadas) {
		this.numeroMensagensEnviadas = numeroMensagensEnviadas;
	}
	
	public List<String> getEmailsNaoLocalizados() {
		if (emailsNaoLocalizados == null) {
			emailsNaoLocalizados = new ArrayList<>();
		}
		return emailsNaoLocalizados;
	}

	public void setEmailsNaoLocalizados(List<String> emailsNaoLocalizados) {
		this.emailsNaoLocalizados = emailsNaoLocalizados;
	}
	
	public void incrementarNumeroMensagensEnviadas(int incremento) {
		setNumeroMensagensEnviadas(getNumeroMensagensEnviadas() + incremento);
	}
	
	public void adicionarEmailNaoLocalizado(String email) {
		if (Uteis.isAtributoPreenchido(email) 
				&& getEmailsNaoLocalizados().stream().noneMatch(email::equals) 
				&& getEmailsNaoLocalizados().add(email));
	}
	
	public MensagensRSVO getRetornoWebService() {
		setMensagens(new ArrayList<>());
		return this;
	}
	
	public void inicializarDadosRetornoEnvioMensagens() {
		setEmailsNaoLocalizados(new ArrayList<>());
		setNumeroMensagensEnviadas(0);
	}
}