package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum ArtefatoEntregaEnum {

	TEXTUAL, UPLOAD_ARQUIVO;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_ArtefatoEntrega_" + this.name());
	}

	public String getName() {
		return this.name();
	}
}
