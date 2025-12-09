package negocio.comuns.contabil.enumeradores;

import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum TipoRegraContabilEnum {
	RECEBIMENTO,
	PAGAMENTO,
	MOVIMENTACAO_FINANCEIRA,
	DESCONTO,
	NOTA_FISCAL_ENTRADA_IMPOSTO,
	NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO,
	SACADO,
	JURO_MULTA_PAGAR,
	DESCONTO_PAGAR,
	JURO_MULTA_ACRESCIMO,
	CARTAO_CREDITO,
	TAXA_CARTAOES;
	
	
	 public boolean isRecebimento(){
		 return Uteis.isAtributoPreenchido(name()) && name().equals(TipoRegraContabilEnum.RECEBIMENTO.name());
	 }
	 public boolean isPagamento(){
		 return Uteis.isAtributoPreenchido(name()) && name().equals(TipoRegraContabilEnum.PAGAMENTO.name());
	 }
	 public boolean isMovimentacaoFinanceira(){
		 return Uteis.isAtributoPreenchido(name()) && name().equals(TipoRegraContabilEnum.MOVIMENTACAO_FINANCEIRA.name());
	 }
	 public boolean isDesconto(){
		 return Uteis.isAtributoPreenchido(name()) && name().equals(TipoRegraContabilEnum.DESCONTO.name());
	 }
	 public boolean isDescontoPagar(){
		 return Uteis.isAtributoPreenchido(name()) && name().equals(TipoRegraContabilEnum.DESCONTO_PAGAR.name());
	 }
	 public boolean isCartaoCredito(){
		 return Uteis.isAtributoPreenchido(name()) && name().equals(TipoRegraContabilEnum.CARTAO_CREDITO.name());
	 }
	 public boolean isTaxaCartoes(){
		 return Uteis.isAtributoPreenchido(name()) && name().equals(TipoRegraContabilEnum.TAXA_CARTAOES.name());
	 }
	 public boolean isJuroMultaAcrescimo(){
		 return Uteis.isAtributoPreenchido(name()) && name().equals(TipoRegraContabilEnum.JURO_MULTA_ACRESCIMO.name());
	 }
	 public boolean isJuroMultaPagar(){
		 return Uteis.isAtributoPreenchido(name()) && name().equals(TipoRegraContabilEnum.JURO_MULTA_PAGAR.name());
	 }
	 public boolean isNotaFiscaEntradaImposto(){
		 return Uteis.isAtributoPreenchido(name()) && name().equals(TipoRegraContabilEnum.NOTA_FISCAL_ENTRADA_IMPOSTO.name());
	 }
	 public boolean isNotaFiscaEntradaCategoriaProduto(){
		 return Uteis.isAtributoPreenchido(name()) && name().equals(TipoRegraContabilEnum.NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO.name());
	 }	 
	 public boolean isSacado(){
		 return Uteis.isAtributoPreenchido(name()) && name().equals(TipoRegraContabilEnum.SACADO.name());
	 }
	 public boolean isNotaFiscaEntrada(){
		 return isNotaFiscaEntradaCategoriaProduto() || isNotaFiscaEntradaImposto();
	 }

}
