package negocio.comuns.faturamento.nfe.enumeradores;

import negocio.comuns.utilitarias.Uteis;
/**
 * 
 * @author Pedro Otimize
 *
 */
public enum TipoOrigemDestinoNaturezaOperacaoEnum {
	MESMO_ESTADO, OUTRO_ESTADO;
	
	public boolean isMesmoEstado() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoOrigemDestinoNaturezaOperacaoEnum.MESMO_ESTADO.name());
	}

	public boolean isOutroEstado() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoOrigemDestinoNaturezaOperacaoEnum.OUTRO_ESTADO.name());
	}

}
