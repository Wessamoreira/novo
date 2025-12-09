package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoRegraFrequenciaEquivalenciaEnum {
	
	MEDIA_FREQUENCIA, FREQUENCIA_PROPORCIONAL, SOMAR_FREQUENCIA, MAIOR_FREQUENCIA;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoRegraFrequenciaEquivalenciaEnum_"+this.name());
	}
	
	public String getSiglaApresentar(){
		switch (this) {
		case MAIOR_FREQUENCIA:
			return "MF";			
		case SOMAR_FREQUENCIA:
			return "SF";			
		case FREQUENCIA_PROPORCIONAL:
			return "FP";			
		case MEDIA_FREQUENCIA:
			return "MD";			
		default:
			return "--";
		}
	}

}
