package negocio.comuns.blackboard.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum CourseRoleIdEnum {
	Instructor,
	BbFacilitator,
	TeachingAssistant,
	CourseBuilder,
	Grader,
	Student,
	Guest;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_CourseRoleIdEnum_"+this.name());
	}
	
	public CourseRoleIdEnum get(TipoSalaAulaBlackboardPessoaEnum tipoSalaAulaBlackboardPessoaEnum) {
		switch (tipoSalaAulaBlackboardPessoaEnum) {
		case ALUNO:
			return Student;
		case FACILITADOR:
			return BbFacilitator;
		case ORIENTADOR:
			return TeachingAssistant;
		case PROFESSOR:
			return Instructor;		
		default:
			return null;
		}
	}
}
