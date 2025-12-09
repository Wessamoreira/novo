package negocio.comuns.contabil.enumeradores;

import negocio.comuns.utilitarias.Uteis;

/*
 * Pedro Andrade
 */

public enum TipoValorLancamentoContabilEnum { 
	ADIANTAMENTO,
	PAGAMENTO,
	JURO,
	MULTA,
	ACRESCIMO,
	ALUNO,
	PROGRESSIVO,
	INSTITUCIONAL,
	CONVENIO,
	CUSTEADO_CONVENIO,
	RATEIO,
	RECEBIMENTO,
	CONTA_RECEBER,
	CONTA_PAGAR,
	NOTA_FISCAL_ENTRADA_IMPOSTO,
	NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO,
	MOVIMENTACAO_FINANCEIRA,
	CARTAO_CREDITO,
	CHEQUE;
	
	public boolean isContaReceber(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.CONTA_RECEBER.name());
	}
	
	public boolean isContaPagar(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.CONTA_PAGAR.name());
	}
	
	
	public boolean isMovimentacaoFinanceira(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.MOVIMENTACAO_FINANCEIRA.name());
	}
	
	public boolean isJuro(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.JURO.name());
	}
	
	public boolean isMulta(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.MULTA.name());
	}
	public boolean isAcrescimo(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.ACRESCIMO.name());
	}
	
	public boolean isAdiantamento(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.ADIANTAMENTO.name());
	}
	
	public boolean isPagamento(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.PAGAMENTO.name());
	}
	
	public boolean isAluno(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.ALUNO.name());
	}
	
	public boolean isProgressivo(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.PROGRESSIVO.name());
	}
	public boolean isInstitucional(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.INSTITUCIONAL.name());
	}
	public boolean isConvenio(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.CONVENIO.name());
	}
	public boolean isCusteadoConvenio(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.CUSTEADO_CONVENIO.name());
	}
	public boolean isRateio(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.RATEIO.name());
	}
	public boolean isRecebimento(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.RECEBIMENTO.name());
	}
	public boolean isCartaoCredito(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.CARTAO_CREDITO.name());
	}
	public boolean isCheque(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.CHEQUE.name());
	}
	
	public boolean isDesconto(){
		return Uteis.isAtributoPreenchido(name()) &&
		( name().equals(TipoValorLancamentoContabilEnum.ALUNO.name()) ||
		  name().equals(TipoValorLancamentoContabilEnum.PROGRESSIVO.name()) ||
		  name().equals(TipoValorLancamentoContabilEnum.INSTITUCIONAL.name()) ||
		  name().equals(TipoValorLancamentoContabilEnum.CONVENIO.name()) ||
		  name().equals(TipoValorLancamentoContabilEnum.CUSTEADO_CONVENIO.name()) ||
		  name().equals(TipoValorLancamentoContabilEnum.RATEIO.name()) ||
		  name().equals(TipoValorLancamentoContabilEnum.RECEBIMENTO.name()) 
		);
	}
	
	public boolean isNotaFiscalEntradaImposto(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.NOTA_FISCAL_ENTRADA_IMPOSTO.name());
	}
	
	public boolean isNotaFiscalEntradaCategoriaProduto(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoValorLancamentoContabilEnum.NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO.name());
	}
	 

}
