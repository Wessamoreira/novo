package webservice.boletoonline.bancoBrasil.comuns;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;

public class ErrorBbWS {
	
	private String codigo;
	private String mensagem;
	private String error;
	private String error_description;
	private List<ErrorBbItemWS> errors;
	private List<ErrorBbItemWS> erros;

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

	public List<ErrorBbItemWS> getErrors() {
		if(errors == null) {
			errors = new ArrayList<ErrorBbItemWS>();	
		}
		return errors;
	}

	public void setErrors(List<ErrorBbItemWS> errors) {
		this.errors = errors;
	}
	
	
	public List<ErrorBbItemWS> getErros() {
		if(erros == null) {
			erros = new ArrayList<ErrorBbItemWS>();	
		}
		return erros;
	}

	public void setErros(List<ErrorBbItemWS> erros) {
		this.erros = erros;
	}
	
	public String getError() {
		if(error == null) {
			error ="" ;
		}
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getError_description() {
		if(error_description == null ) {
			error_description =""; 
		}
		return error_description;
	}

	public void setError_description(String error_description) {
		this.error_description = error_description;
	}
	
	public String getMensagemCampos() {
		StringBuilder sb = new StringBuilder("");		
		if(!getErros().isEmpty()) {
			getErros().stream().forEach(p-> {
				sb.append(" codigo Erro : ").append(p.getCodigo());
				sb.append(" mensagem :" ).append(p.getMensagem());		
			});			
		}else if(!getErrors().isEmpty()){
			getErrors().stream().forEach(p-> {
				sb.append(" codigo Erro : ").append(p.getCode());
				sb.append(" mensagem :" ).append(p.getMensage());		
			});
		}else if(Uteis.isAtributoPreenchido(getError()) && Uteis.isAtributoPreenchido(getError_description())) {			
			sb.append(getError_description());	
			sb.append("Credenciais Inválidas");			
		}else {
			sb.append("");
		}		
		return sb.toString();
	}

}
