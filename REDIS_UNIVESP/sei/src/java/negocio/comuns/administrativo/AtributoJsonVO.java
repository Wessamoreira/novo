package negocio.comuns.administrativo;

import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;

public class AtributoJsonVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String atributo;
	private String valor;
	
	public String getAtributo() {
		if (atributo == null) {
			atributo = "";
		}
		return atributo;
	}
	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}
	public String getValor() {
		if (valor == null) {
			valor = "";
		}
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}

}
