package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Victor Hugo 02/10/2014
 */
public enum TipoOrigemEnum {

	ATIVIDADE_DISCURSIVA,
	REQUERIMENTO,
	REA,
	AVALIACAO_ONLINE_TURMA,
	NENHUM;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoOrigemEnum_"+this.name());
	}
	
	public boolean isTipoOrigemAtividadeDiscursiva() {
		return name().equals(TipoOrigemEnum.ATIVIDADE_DISCURSIVA.name());
	}
	
	public boolean isTipoOrigemRea() {
		return name().equals(TipoOrigemEnum.REA.name());
	}
	
	public boolean isAvaliacaoOnlineTurma() {
		return name().equals(TipoOrigemEnum.AVALIACAO_ONLINE_TURMA.name());
	}
}
