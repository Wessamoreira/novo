package negocio.comuns.patrimonio.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum FormaEntradaPatrimonioEnum {

	COMPRA, DOACAO, PERMUTA, SUBSTITUICAO, OUTRA;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_"+this.getClass().getCanonicalName()+"_"+this.name());
	}
}
