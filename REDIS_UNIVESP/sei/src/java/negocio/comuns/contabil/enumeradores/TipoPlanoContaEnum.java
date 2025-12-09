package negocio.comuns.contabil.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoPlanoContaEnum {
	CREDITO, DEBITO;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoPlanoContaEnum_"+this.name());
	}
	
	public boolean isCredito(){
		return name().equals(TipoPlanoContaEnum.CREDITO.name());
	}
	
	public boolean isDebito(){
		return name().equals(TipoPlanoContaEnum.DEBITO.name());
	}
	
}
