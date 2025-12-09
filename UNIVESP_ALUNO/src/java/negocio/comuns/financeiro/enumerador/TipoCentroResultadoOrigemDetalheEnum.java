package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */

public enum TipoCentroResultadoOrigemDetalheEnum {	
	
	VALOR_BASE,	
	JURO,
	MULTA,
	ACRESCIMO,
	REAJUSTE_PRECO,
	REAJUSTE_POR_ATRASO,
	DESCONTO_RECEBIMENTO,
	DESCONTO_ALUNO,
	DESCONTO_CONVENIO,
	DESCONTO_PROGRESSIVO,
	DESCONTO_RATEIO,
	DESCONTO_INSTITUICAO,
	DESCONTO_MANUAL,
	DESCONTO_CUSTEADO_CONTA_RECEBER;
	
	public boolean isValorBase(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemDetalheEnum.VALOR_BASE.name());
	}
	
	public boolean isJuro(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemDetalheEnum.JURO.name());
	}
	
	public boolean isMulta(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemDetalheEnum.MULTA.name());
	}
	
	public boolean isAcrescimo(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemDetalheEnum.ACRESCIMO.name());
	}
	
	public boolean isReajustePreco(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemDetalheEnum.REAJUSTE_PRECO.name());
	}
	
	public boolean isReajustePorAtraso(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemDetalheEnum.REAJUSTE_POR_ATRASO.name());
	}
	
	public boolean isDescontoAluno(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemDetalheEnum.DESCONTO_ALUNO.name());
	}
	
	public boolean isDescontoConvenio(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemDetalheEnum.DESCONTO_CONVENIO.name());
	}
	
	public boolean isDescontoProgressivo(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemDetalheEnum.DESCONTO_PROGRESSIVO.name());
	}
	
	public boolean isDescontoManual(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemDetalheEnum.DESCONTO_MANUAL.name());
	}
	
	public boolean isDescontoRateio(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemDetalheEnum.DESCONTO_RATEIO.name());
	}
	
	public boolean isDescontoInstituicao(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemDetalheEnum.DESCONTO_INSTITUICAO.name());
	}
	
	public boolean isDescontoCusteadoContaReceber(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemDetalheEnum.DESCONTO_CUSTEADO_CONTA_RECEBER.name());
	}
	
	public boolean isDescontoRecebimento(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoCentroResultadoOrigemDetalheEnum.DESCONTO_RECEBIMENTO.name());
	}
	
	public boolean isTipoDetalheSoma(){
		return isValorBase() || isAcrescimo() || isMulta() || isJuro() || isReajustePreco();
	}
	
	public boolean isTipoDetalheSubtrair(){
		return isDescontoAluno() || isDescontoConvenio() || isDescontoCusteadoContaReceber() || isDescontoManual() || isDescontoInstituicao() 
				|| isDescontoProgressivo() || isDescontoRateio() || isDescontoRecebimento();
	}
	
	
	 	

}
