package webservice.boletoonline.itau.comuns;

public class ErrorItauItemWS {
	
	private String campo;
	private String mensagem;
	private String valor;

	public String getCampo() {
		if(campo == null) {
			campo = "";	
		}
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getMensagem() {
		if(mensagem == null) {
			mensagem = "";	
		}
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getValor() {
		if(valor == null) {
			valor = "";	
		}
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}
