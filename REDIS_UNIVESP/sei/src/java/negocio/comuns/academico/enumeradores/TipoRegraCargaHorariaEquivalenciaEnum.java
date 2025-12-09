package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoRegraCargaHorariaEquivalenciaEnum {
	
	SOMAR_CARGA_HORARIA, CARGA_HORARIA_GRADE, MAIOR_CARGA_HORARIA, MEDIA_CARGA_HORARIA;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoRegraCargaHorariaEquivalenciaEnum_"+this.name());
	}
	
	public String getSiglaApresentar(){
		switch (this) {
		case CARGA_HORARIA_GRADE:
			return "CHM";			
		case MAIOR_CARGA_HORARIA:
			return "MCH";			
		case SOMAR_CARGA_HORARIA:
			return "SCH";			
		case MEDIA_CARGA_HORARIA:
			return "MD";			
		default:
			return "--";
		}
	}

}
