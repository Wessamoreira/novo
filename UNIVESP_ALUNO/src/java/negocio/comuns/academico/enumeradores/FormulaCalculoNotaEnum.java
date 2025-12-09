/**
 * 
 */
package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Wellington Rodrigues
 *
 */
public enum FormulaCalculoNotaEnum {

	MEDIA, SOMATORIO, FORMULA_CALCULO;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_FormulaCalculoNotaEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}

	public static FormulaCalculoNotaEnum getEnum(String valor) {
		FormulaCalculoNotaEnum[] valores = values();
		for (FormulaCalculoNotaEnum obj : valores) {
			if (obj.name().equals(valor)) {
				return obj;
			}
		}
		return null;
	}

	public static String getDescricao(String valor) {
		FormulaCalculoNotaEnum obj = getEnum(valor);
		if (obj != null) {
			return obj.getValorApresentar();
		}
		return valor;
	}

}
