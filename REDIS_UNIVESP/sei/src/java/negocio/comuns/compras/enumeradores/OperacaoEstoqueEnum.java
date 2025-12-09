package negocio.comuns.compras.enumeradores;

import negocio.comuns.utilitarias.Uteis;

/*
 * Pedro Andrade	
 */
public enum OperacaoEstoqueEnum {
	INCLUIR, EXCLUIR;
	
	public boolean isIncluir(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(OperacaoEstoqueEnum.INCLUIR.name());
	}
	
	public boolean isExcluir(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(OperacaoEstoqueEnum.EXCLUIR.name());
	}

}
