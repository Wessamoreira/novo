package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum AvaliarNaoAvaliarEnum {

	AVALIAR, NAO_AVALIAR;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_AvaliarNaoAvaliarEnum_"+this.name());				
	}
	public String getValorApresentarRelatorio(){
		if(this.equals(AVALIAR)){
			return "S";
		}
		return "";					
	}
}
