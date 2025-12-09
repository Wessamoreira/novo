package negocio.comuns.arquitetura;

import negocio.comuns.arquitetura.enumeradores.ServicoWebserviceEnum;

public class RegistroWebserviceVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7041072494854498723L;

	private Integer codigo;
	private String jsonRecebido;
	private String jsonRetorno;
	private ServicoWebserviceEnum servico;
	private String ip;
	private String usuario;
	
	
	public RegistroWebserviceVO(ServicoWebserviceEnum servico) {
		super();
		this.servico = servico;
	}
	
	public Integer getCodigo() {
		if(codigo==null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public String getJsonRecebido() {
		if(jsonRecebido==null) {
			jsonRecebido = "";
		}
		return jsonRecebido;
	}
	public void setJsonRecebido(String jsonRecebido) {
		this.jsonRecebido = jsonRecebido;
	}
	public String getJsonRetorno() {
		if(jsonRetorno==null) {
			jsonRetorno = "";
		}
		return jsonRetorno;
	}
	public void setJsonRetorno(String jsonRetorno) {
		this.jsonRetorno = jsonRetorno;
	}
	public ServicoWebserviceEnum getServico() {
		return servico;
	}
	public void setServico(ServicoWebserviceEnum servico) {
		this.servico = servico;
	}
	public String getIp() {
		if(ip==null) {
			ip = "";
		}
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getUsuario() {
		if(usuario==null) {
			usuario = "";
		}
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	
	
	
}
