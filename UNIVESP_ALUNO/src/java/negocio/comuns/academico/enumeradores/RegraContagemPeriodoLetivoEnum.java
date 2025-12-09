package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroAndrade
 *
 */
public enum RegraContagemPeriodoLetivoEnum {
	
	ULTIMO_PERIODO,
	TODOS_PERIODO_CURSADO;
	
	public boolean isUltimoPeriodo() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(RegraContagemPeriodoLetivoEnum.ULTIMO_PERIODO.name());
	}
	
	public boolean isTodosPeriodo() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(RegraContagemPeriodoLetivoEnum.TODOS_PERIODO_CURSADO.name());
	}
}
