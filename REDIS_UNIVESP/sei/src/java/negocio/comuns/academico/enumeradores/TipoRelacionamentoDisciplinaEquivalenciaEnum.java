package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoRelacionamentoDisciplinaEquivalenciaEnum {
	
	RELACIONAMENTO_E, RELACIONAMENTO_OU;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoRelacionamentoDisciplinaEquivalenciaEnum_"+this.name());
	}
	
	public String getSiglaApresentar(){
		switch (this) {
		case RELACIONAMENTO_E:
			return "E";			
		case RELACIONAMENTO_OU:
			return "OU";			
				
					
		default:
			return "--";
		}
	}
}
