package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Wellington - 13 de out de 2015
 *
 */
public enum TipoSubTurmaEnum {

	GERAL, TEORICA, PRATICA;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoSubTurmaEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
}
