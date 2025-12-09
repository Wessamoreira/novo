package webservice.servicos.excepetion;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.ws.rs.core.Response.Status;

/**
 * @author Victor Hugo de Paula Costa - 3 de out de 2016
 *
 */
@XmlRootElement(name = "error")
public class ErrorInfoRSVO {
	/**
	 * @author Victor Hugo de Paula Costa - 3 de out de 2016
	 */

	private String codigo;
	private String campo;
	private String mensagem;
	private int statusCode;
	private String mensagemErro;
	
	public ErrorInfoRSVO() {
		super();
	}

	public ErrorInfoRSVO(Status status, String mensagem ) {
		super();
		this.codigo = status.name();
		this.statusCode = status.getStatusCode();
		this.mensagem = mensagem;
	}

	@XmlElement(name = "codigo")
	public String getCodigo() {
		if (codigo == null) {
			codigo = "";
		}
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	

	@XmlElement(name = "campo")
	public String getCampo() {
		if (campo == null) {
			campo = "";
		}
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	@XmlElement(name = "mensagem")
	public String getMensagem() {
		if (mensagem == null) {
			mensagem = "";
		}
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	@XmlElement(name = "statusCode")
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	
	@XmlElement(name = "mensagemErro")
	public String getMensagemErro() {
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}
	
	
	
	
}
