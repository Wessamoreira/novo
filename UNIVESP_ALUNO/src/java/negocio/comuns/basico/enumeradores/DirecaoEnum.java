package negocio.comuns.basico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum DirecaoEnum {

	DIREITA, ESQUERDA, AMBAS;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_DirecaoEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}

	public static DirecaoEnum getEnum(String valor) {
		DirecaoEnum[] valores = values();
		for (DirecaoEnum obj : valores) {
			if (obj.name().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		DirecaoEnum obj = getEnum(valor);
		if (obj != null) {
			return obj.getValorApresentar();
		}
		return valor;
	}

}
