package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoRegraAnoSemestreEquivalenciaEnum {

	ANO_SEMESTRE_ATUAL, MAIOR_ANO_SEMESTRE, MENOR_ANO_SEMESTRE;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoRegraAnoSemestreEquivalenciaEnum_"+this.name());
	}
	
	public String getSiglaApresentar(){
		switch (this) {
		case ANO_SEMESTRE_ATUAL:
			return "AT";			
		case MAIOR_ANO_SEMESTRE:
			return "MA";			
		case MENOR_ANO_SEMESTRE:
			return "ME";			
					
		default:
			return "--";
		}
	}
}
