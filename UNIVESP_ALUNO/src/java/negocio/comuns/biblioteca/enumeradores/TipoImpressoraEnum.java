package negocio.comuns.biblioteca.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoImpressoraEnum {

	TERMICA_FISCAL, TERMICA_NAO_FISCAL, MATRICIAL, LASER, JATO_TINTA;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoImpressoraEnum_"+this.name());
				
	}
	
}
