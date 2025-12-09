package negocio.comuns.contabil.enumeradores;

import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum TipoOrigemLancamentoContabilEnum {
	NOTA_FISCAL_ENTRADA,
	RECEBER,
	PAGAR,
	NEGOCIACAO_CONTA_PAGAR,
	CARTAO_CREDITO,
	MOVIMENTACAO_FINANCEIRA;
	
	public boolean isReceber(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoOrigemLancamentoContabilEnum.RECEBER.name());
	}
	
	public boolean isCartaoCredito(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoOrigemLancamentoContabilEnum.CARTAO_CREDITO.name());
	}
	
	public boolean isPagar(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoOrigemLancamentoContabilEnum.PAGAR.name());
	}
	
	public boolean isMovimentacaoFinanceira(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoOrigemLancamentoContabilEnum.MOVIMENTACAO_FINANCEIRA.name());
	}
	
	public boolean isNotaFiscalEntrada(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoOrigemLancamentoContabilEnum.NOTA_FISCAL_ENTRADA.name());
	}	
	
	public boolean isNegocicaoContaPagar(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoOrigemLancamentoContabilEnum.NEGOCIACAO_CONTA_PAGAR.name());
	}	
	
}
