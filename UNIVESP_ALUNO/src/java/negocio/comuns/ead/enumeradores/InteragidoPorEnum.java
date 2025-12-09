package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/*
 * @author Victor Hugo 17/09/2014
 */
public enum InteragidoPorEnum {
	
	ALUNO, PROFESSOR;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_InteragidoPorEnum_"+ this.name());
	}
	
	public boolean isAluno() {
		return name().equals(InteragidoPorEnum.ALUNO.name());
	}

	public boolean isProfessor() {
		return name().equals(InteragidoPorEnum.ALUNO.name());
	}
}
