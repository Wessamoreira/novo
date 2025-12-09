package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum FormatoCargaHorariaXmlEnum {
	
	HORA_AULA		("HORA_AULA",		"Hora Aula"),
	HORA_RELOGIO	("HORA_RELOGIO", 	"Hora Relógio");

	String valor;
	String descricao;

	FormatoCargaHorariaXmlEnum(String valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public static FormatoCargaHorariaXmlEnum getEnum(String valor) {
		FormatoCargaHorariaXmlEnum[] valores = values();
		for (FormatoCargaHorariaXmlEnum obj : valores) {
			if (obj.getValor().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		FormatoCargaHorariaXmlEnum obj = getEnum(valor);
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
