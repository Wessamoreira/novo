package webservice.boletoonline.itau.comuns;

import java.util.ArrayList;
import java.util.List;

public class ErrorItauWS {
	
	private String codigo;
	private String mensagem;
	private List<ErrorItauItemWS> campos;

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

	public List<ErrorItauItemWS> getCampos() {
		if(campos == null) {
			campos = new ArrayList<ErrorItauItemWS>();	
		}
		return campos;
	}

	public void setCampos(List<ErrorItauItemWS> campos) {
		this.campos = campos;
	}
	
	public String getMensagemCampos() {
		StringBuilder sb = new StringBuilder("");
		getCampos().stream().forEach(p-> {
			sb.append(" campo = ").append(p.getCampo());
			sb.append(" mensagem =" ).append(p.getMensagem());
			sb.append(" valor = ").append(p.getValor());
		});
		return sb.toString();
	}

}
