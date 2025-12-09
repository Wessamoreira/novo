package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum TipoCentroResultadoOrigemEnum {
	CONTA_RECEBER,
	CONTA_PAGAR,	
	COMPRA,
	COTACAO,
	ENTREGA_REQUISICAO,
	NOTA_FISCAL_ENTRADA,
	NOTA_FISCAL_ENTRADA_ITEM,
	PARAMETRIZAR_OPERACOES_AUTOMATICAS_CONCILIACAO_ITEM,
	REQUISICAO, 
	ESTOQUE,
	MOVIMENTACAO_ESTOQUE, 
	CONTRATO_DESPESA;
	
	public boolean isParametrizarOperacaoAutomaticasConciliacaoItem(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemEnum.PARAMETRIZAR_OPERACOES_AUTOMATICAS_CONCILIACAO_ITEM.name());
	}
	
	public boolean isContaReceber(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemEnum.CONTA_RECEBER.name());
	}
	

}
