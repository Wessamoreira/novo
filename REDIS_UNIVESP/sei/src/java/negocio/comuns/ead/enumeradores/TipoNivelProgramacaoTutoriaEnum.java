package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Victor Hugo 13/11/2014
 */
public enum TipoNivelProgramacaoTutoriaEnum {

	DISCIPLINA, UNIDADE_ENSINO, NIVEL_EDUCACIONAL, TURMA, CURSO;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoNivelProgramacaoTutoriaEnum_" + this.name());
	}
	
	public String getName() {
		return this.name();
	}
	
	public boolean isDisciplina() {
		return this.name().equals(TipoNivelProgramacaoTutoriaEnum.DISCIPLINA.name());
	}
	
	public boolean isUnidadeEnsino() {
		return this.name().equals(TipoNivelProgramacaoTutoriaEnum.UNIDADE_ENSINO.name());
	}
	
	public boolean isNivelEducacional() {
		return this.name().equals(TipoNivelProgramacaoTutoriaEnum.NIVEL_EDUCACIONAL.name());
	}
	public boolean isTurma() {
		return this.name().equals(TipoNivelProgramacaoTutoriaEnum.TURMA.name());
	}
	public boolean isCurso() {
		return this.name().equals(TipoNivelProgramacaoTutoriaEnum.CURSO.name());
	}
}
