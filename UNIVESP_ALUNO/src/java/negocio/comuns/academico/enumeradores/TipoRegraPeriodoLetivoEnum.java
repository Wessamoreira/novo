package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoRegraPeriodoLetivoEnum {

	PERIODO_MAIS_ANTIGO, PERIODO_MAIS_ATUAL, PERIODO_MATRIZ_CURRICULAR;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoRegraPeriodoLetivoEnum_"+this.name());
	}
	
	public String getSiglaApresentar(){
		switch (this) {
		case PERIODO_MAIS_ANTIGO:
			return "PAN";			
		case PERIODO_MAIS_ATUAL:
			return "PAT";			
		case PERIODO_MATRIZ_CURRICULAR:
			return "PMC";			
					
		default:
			return "--";
		}
	}
	
}
