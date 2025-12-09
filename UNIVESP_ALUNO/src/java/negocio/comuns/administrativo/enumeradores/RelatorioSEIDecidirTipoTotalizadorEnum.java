package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum RelatorioSEIDecidirTipoTotalizadorEnum {
	NENHUM, SOMAR, CONTAR, MEDIA, TEXTO;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_RelatorioSEIDecidirTipoTotalizadorEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
}
