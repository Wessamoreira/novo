package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

public enum RegraDefinicaoPeriodoAvaliacaoOnlineEnum {

	PERIODO_ACESSO_DISCIPLINA, NUMERO_DIA_ESPECIFICO, CALENDARIO_LANCAMENTO_NOTA, PERIODO_DIA_FIXO;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_RegraDefinicaoPeriodoAvaliacaoOnlineEnum_"+this.name());
	}
	
	public boolean isPeriodoAcessoDisciplina() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.PERIODO_ACESSO_DISCIPLINA.name());
	}
	
	public boolean isNumeroDiaEspecifico() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.NUMERO_DIA_ESPECIFICO.name());
	}
	
	public boolean isCalendarioLancamentoNota() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.CALENDARIO_LANCAMENTO_NOTA.name());
	}
	
	public boolean isPeriodoDiaFixo() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.PERIODO_DIA_FIXO.name());
	}

}
