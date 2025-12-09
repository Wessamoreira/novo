package negocio.comuns.basico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum RegiaoEnum {
	NORTE, NORDESTE, CENTRO_OESTE, SUDESTE, SUL;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_RegiaoEnum_"+this.name());
	}
}
