package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.Uteis;

public enum GestaoContasPagarOperacaoEnum {
	ALTERACAO, CANCELAMENTO, ESTORNO_CANCELAMENTO, EXCLUSAO, PAGAMENTO, ESTORNO_PAGAMENTO;

	public boolean isAlteracao() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(ALTERACAO.name());
	}

	public boolean isCancelamento() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(CANCELAMENTO.name());
	}

	public boolean isEstornoCancelamento() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(ESTORNO_CANCELAMENTO.name());
	}

	public boolean isExclusao() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(EXCLUSAO.name());
	}

	public boolean isPagamento() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(PAGAMENTO.name());
	}

	public boolean isEstornoPagamento() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(ESTORNO_PAGAMENTO.name());
	}

}
