package negocio.comuns.processosel.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoEscopoQuestionarioRequerimentoEnum {
	
	REQUERENTE, DEPARTAMENTO; 
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoEscopoQuestionarioRequerimentoEnum_"+this.name());
	}
	
}
