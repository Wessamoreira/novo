package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.UteisJSF;

public enum ParcelasSeremRecebidasMatriculaRenovacaoOnlineEnum {

	PRIMEIRA_PARCELA, TODAS_PARCELAS_GERADAS;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_ParcelasSeremRecebidasMatriculaRenovacaoOnlineEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
}
