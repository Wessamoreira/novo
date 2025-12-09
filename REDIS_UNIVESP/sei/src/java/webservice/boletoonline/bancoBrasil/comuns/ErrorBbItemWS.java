package webservice.boletoonline.bancoBrasil.comuns;

public class ErrorBbItemWS {
	
	private String codigo;
	private String mensagem;
	private String ocorrencia;
	private String code;
	private String message;
	

	public String getCode() {
		if(code == null) {
			code = "";	
		}
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMensage() {
		if(message == null) {
			message = "";	
		}
		return message;
	}

	public void setMensage(String message) {
		this.message = message;
	}
	public String getCodigo() {
		if(codigo == null) {
			codigo = "";	
		}
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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

	public String getOcorrencia() {
		if(ocorrencia == null ) {
			ocorrencia = "";
		}
		return ocorrencia;
	}

	public void setOcorrencia(String ocorrencia) {
		this.ocorrencia = ocorrencia;
	}

	

}
