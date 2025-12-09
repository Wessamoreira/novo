package negocio.comuns.elastic;

import java.io.Serializable;
import java.util.Date;

public class ExcecaoElasticVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Date criado;
	private String origem;
	private String mensagem;
	
	public Date getCriado() {
		return criado;
	}
	
	public void setCriado(Date criado) {
		this.criado = criado;
	}
	
	public String getOrigem() {
		if (origem == null) {
			origem = "";
		}
		return origem;
	}
	
	public void setOrigem(String origem) {
		this.origem = origem;
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
	
}
