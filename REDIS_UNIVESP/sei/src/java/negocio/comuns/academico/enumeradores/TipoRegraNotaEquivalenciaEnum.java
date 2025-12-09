package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoRegraNotaEquivalenciaEnum {
	
	MEDIA_NOTA, MAIOR_NOTA, SOMAR_NOTA, FORMULA_CALCULO;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoRegraNotaEquivalenciaEnum_"+this.name());
	}
	
	public String getSiglaApresentar(){
		switch (this) {
		case FORMULA_CALCULO:
			return "FC";			
		case MAIOR_NOTA:
			return "MN";			
		case SOMAR_NOTA:
			return "SN";			
		case MEDIA_NOTA:
			return "MD";			
		default:
			return "--";
		}
	}
}
