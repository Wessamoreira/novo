package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoCoordenadorCursoEnum {
	AMBOS, ESTAGIO, GERAL;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoCoordenadorCursoEnum_"+this.name());
	}
}
