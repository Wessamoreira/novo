package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum EtapaTCCEnum {

	PLANO_TCC, ELABORACAO_TCC, AVALIACAO_TCC;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_EtapaTCCEnum_"+this.name());				
	}
	
	public String getNome(){
		switch (this) {
		case PLANO_TCC:
			return "Plano";
		case ELABORACAO_TCC:
			return "Elaboração";
		case AVALIACAO_TCC:
			return "Avaliação";
		default:
			return "";
		}				
	}
}
