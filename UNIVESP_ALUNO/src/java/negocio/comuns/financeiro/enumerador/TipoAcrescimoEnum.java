package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoAcrescimoEnum {

	VALOR, PORCENTAGEM;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoAcrescimoEnum_"+this.name());
	}
}
