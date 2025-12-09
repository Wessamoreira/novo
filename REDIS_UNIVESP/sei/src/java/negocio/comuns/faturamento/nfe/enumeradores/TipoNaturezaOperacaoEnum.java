package negocio.comuns.faturamento.nfe.enumeradores;

import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author Pedro Otimize
 *
 */
public enum TipoNaturezaOperacaoEnum {
	SAIDA, ENTRADA;
	
	public boolean isSaida() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoNaturezaOperacaoEnum.SAIDA.name());
	}

	public boolean isEntrada() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoNaturezaOperacaoEnum.ENTRADA.name());
	}
}
