package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum PeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum {
	
	INDETERMINADO, PERIODO_FIXO;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_PeriodoApresentacaoPoliticaDivulgacaoMatriculaOnlineEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}

}
