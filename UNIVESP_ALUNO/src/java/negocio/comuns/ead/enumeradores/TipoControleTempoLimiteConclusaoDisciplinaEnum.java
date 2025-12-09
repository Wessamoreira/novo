package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoControleTempoLimiteConclusaoDisciplinaEnum {

	NR_DIAS_FIXO, NR_DIAS_POR_HORA_DA_DISCIPLINA_CH, NR_DIAS_POR_CREDITO,PERIODO_CALENDARIO_LETIVO;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoControleTempoLimiteConclusaoDisciplinasEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
	
	public boolean isNrDiasFixo(){
		return this.name().equals(TipoControleTempoLimiteConclusaoDisciplinaEnum.NR_DIAS_FIXO.name());
	}
	public boolean isNrDiasPorCargaHorariaDisciplina(){
		return this.name().equals(TipoControleTempoLimiteConclusaoDisciplinaEnum.NR_DIAS_POR_HORA_DA_DISCIPLINA_CH.name());
	}
	public boolean isNrDiasPorCreditoDisciplina(){
		return this.name().equals(TipoControleTempoLimiteConclusaoDisciplinaEnum.NR_DIAS_POR_CREDITO.name());
	}
	public boolean isPeriodoCalendarioLetivo(){
		return this.name().equals(TipoControleTempoLimiteConclusaoDisciplinaEnum.PERIODO_CALENDARIO_LETIVO.name());
	}

}
