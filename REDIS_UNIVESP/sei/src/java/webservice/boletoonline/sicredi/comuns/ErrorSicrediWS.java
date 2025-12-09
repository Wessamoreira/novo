package webservice.boletoonline.sicredi.comuns;

import java.util.ArrayList;
import java.util.List;

import webservice.boletoonline.itau.comuns.ErrorItauItemWS;

public class ErrorSicrediWS {
	
	private String codigo;
	private String mensagem;
	private String parametro;
	

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

	
	

	public String getParametro() {
		return parametro;
	}

	public void setParametro(String parametro) {
		this.parametro = parametro;
	}
}
