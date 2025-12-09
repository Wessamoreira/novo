/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 * 
 * @author Diego
 */
public enum RegimeCurso {

	ANUAL("AN", "Anual"), SEMESTRAL("SE", "Semestral"), INTEGRAL("IN", "Integral");

	String valor;
	String descricao;

	RegimeCurso(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static RegimeCurso getEnum(String valor) {
		RegimeCurso[] valores = values();
		for (RegimeCurso obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		RegimeCurso obj = getEnum(valor);
		if (obj != null) {
			return obj.getDescricao();
		}
		return valor;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
