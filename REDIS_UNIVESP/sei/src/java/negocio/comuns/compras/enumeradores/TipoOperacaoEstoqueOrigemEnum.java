package negocio.comuns.compras.enumeradores;

import negocio.comuns.utilitarias.Uteis;

public enum TipoOperacaoEstoqueOrigemEnum {
	MOVIMENTACAO_ESTOQUE, 
	DEVOLUCAO_COMPRA_ITEM,
	RECEBIMENTO_COMPRA_ITEM,
	ENTREGA_REQUISICAO_ITEM;

	public boolean isMovimentacaoEstoque() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoOperacaoEstoqueOrigemEnum.MOVIMENTACAO_ESTOQUE.name());
	}

	public boolean isEntregaRequisicaoItem() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoOperacaoEstoqueOrigemEnum.ENTREGA_REQUISICAO_ITEM.name());
	}
	
	public boolean isDevolucaoCompraItem() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoOperacaoEstoqueOrigemEnum.DEVOLUCAO_COMPRA_ITEM.name());
	}
	
	public boolean isRecebimentoCompraItem() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoOperacaoEstoqueOrigemEnum.RECEBIMENTO_COMPRA_ITEM.name());
	}

}
