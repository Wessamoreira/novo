package webservice.arquitetura;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "infoWSVO")
public class InfoWSVO {

	private int codigo;
	private String mensagem;
	private List<InfoItemWSVO> campos;
	/**
	 * Campos 
	 */
	private long timestamp;
	private int status;
	private String error;
	private String message;
	private int code;
	
	
	
	
	
	public InfoWSVO() {
		super();
	}	
	
	public InfoWSVO(Status status, String mensagem ) {
		super();
		this.codigo = status.getStatusCode();
		this.status = status.getStatusCode();
		this.mensagem = mensagem;
	}

	@XmlElement(name = "codigo")
	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	
	@XmlElement(name = "code")
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
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

	@XmlElement(name = "campos")
	public List<InfoItemWSVO> getCampos() {
		if (campos == null) {
			campos = new ArrayList<InfoItemWSVO>();
		}
		return campos;
	}

	public void setCampos(List<InfoItemWSVO> campos) {
		this.campos = campos;
	}

	@XmlElement(name = "mensagemCampos")
	public String getMensagemCampos() {
		StringBuilder sb = new StringBuilder("");
		getCampos().stream().forEach(p -> {
			sb.append(" campo = ").append(p.getCampo());
			sb.append(" mensagem =").append(p.getMensagem());
			sb.append(" valor = ").append(p.getValor());
		});
		return sb.toString();
	}

	@XmlElement(name = "timestamp")
	public long getTimestamp() {	
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@XmlElement(name = "status")
	public int getStatus() {		
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@XmlElement(name = "error")
	public String getError() {
		if (error == null) {
			error = "";
		}
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	@XmlElement(name = "message")
	public String getMessage() {
		if (message == null) {
			message = "";
		}
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
