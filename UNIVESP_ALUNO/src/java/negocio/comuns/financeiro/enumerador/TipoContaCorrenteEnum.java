package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.Uteis;

public enum TipoContaCorrenteEnum {
	CAIXA, CORRENTE, APLICACAO;
	
	public boolean isCaixa(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoContaCorrenteEnum.CAIXA.name());
	}
	
	public boolean isCorrente(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoContaCorrenteEnum.CORRENTE.name());
	}
	
	public boolean isAplicacao(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoContaCorrenteEnum.APLICACAO.name());
	}
	

}
