package negocio.comuns.arquitetura.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * 
 * @author Pedro Andrade
 *
 */
public enum FiltroBooleanEnum {
	AMBOS, SIM, NAO;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_FiltroBooleanEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}

	public static FiltroBooleanEnum getEnum(String valor) {
		FiltroBooleanEnum[] valores = values();
		for (FiltroBooleanEnum obj : valores) {
			if (obj.getName().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public Boolean getFiltroEnum() {
		if (getName().equals(FiltroBooleanEnum.AMBOS.name())) {
			return null;
		} else if (getName().equals(FiltroBooleanEnum.SIM.name())) {
			return true;
		}
		return false;
	}

}
