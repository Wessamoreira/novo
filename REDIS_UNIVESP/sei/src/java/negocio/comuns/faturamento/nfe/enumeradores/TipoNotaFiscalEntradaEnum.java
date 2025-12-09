package negocio.comuns.faturamento.nfe.enumeradores;

import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author Pedro Otimize
 *
 */
public enum TipoNotaFiscalEntradaEnum {
	PRODUTO, 
	SERVICO, 
	IMPOSTO;

	public boolean isProduto() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoNotaFiscalEntradaEnum.PRODUTO.name());
	}

	public boolean isServico() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoNotaFiscalEntradaEnum.SERVICO.name());
	}

	public boolean isImposto() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoNotaFiscalEntradaEnum.IMPOSTO.name());
	}

}
