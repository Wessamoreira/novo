package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum VersaoDiplomaDigitalEnum {
	
	VERSAO_1_05			("1.05", 			"1.05");

	String valor;
	String descricao;

	VersaoDiplomaDigitalEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static VersaoDiplomaDigitalEnum getEnum(String valor) {
		VersaoDiplomaDigitalEnum[] valores = values();
		for (VersaoDiplomaDigitalEnum obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		VersaoDiplomaDigitalEnum obj = getEnum(valor);
		if (obj != null) {
			return UteisJSF.internacionalizar(obj.getDescricao());
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
		return UteisJSF.internacionalizar(descricao);
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
