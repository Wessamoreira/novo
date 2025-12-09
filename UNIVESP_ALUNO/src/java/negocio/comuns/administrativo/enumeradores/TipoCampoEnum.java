package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

public enum TipoCampoEnum {
	TEXTO, INTEIRO, DATA, MES_ANO, DOUBLE, BOOLEAN, MOEDA, BIG_DECIMAL;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoCampoEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
	
	public boolean isTexto(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCampoEnum.TEXTO.name()); 
	}
	
	public boolean isInteiro(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCampoEnum.INTEIRO.name()); 
	}
	
	public boolean isDouble(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCampoEnum.DOUBLE.name()); 
	}

	public boolean isBigDecimal(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCampoEnum.BIG_DECIMAL.name()); 
	}
	
	public boolean isData(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCampoEnum.DATA.name()); 
	}
	
	public boolean isBoolean(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCampoEnum.BOOLEAN.name()); 
	}
}
