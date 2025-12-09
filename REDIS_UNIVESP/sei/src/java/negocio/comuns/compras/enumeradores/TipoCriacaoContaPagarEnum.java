package negocio.comuns.compras.enumeradores;

import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum TipoCriacaoContaPagarEnum {
	RECEBIMENTO_COMPRA, COMPRA, NOTA_FISCAL_ENTRADA;
	
	public boolean isRecebimentoCompra(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCriacaoContaPagarEnum.RECEBIMENTO_COMPRA.name());
	}
	
	public boolean isCompra(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCriacaoContaPagarEnum.COMPRA.name());
	}
	
	public boolean isNotaFiscalEntrada(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCriacaoContaPagarEnum.NOTA_FISCAL_ENTRADA.name());
	}

}
