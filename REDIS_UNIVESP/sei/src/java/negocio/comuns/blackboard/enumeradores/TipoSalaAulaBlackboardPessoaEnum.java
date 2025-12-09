package negocio.comuns.blackboard.enumeradores;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

public enum TipoSalaAulaBlackboardPessoaEnum {
	NENHUM,
	ALUNO,
	PROFESSOR,
	FACILITADOR,
	ORIENTADOR;
	
	public boolean isAluno(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSalaAulaBlackboardPessoaEnum.ALUNO.name()); 
	}
	
	public boolean isProfessor(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSalaAulaBlackboardPessoaEnum.PROFESSOR.name()); 
	}

	public boolean isFacilitador(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSalaAulaBlackboardPessoaEnum.FACILITADOR.name()); 
	}
	
	public boolean isOrientador(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoSalaAulaBlackboardPessoaEnum.ORIENTADOR.name()); 
	}
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoSalaAulaBlackboardPessoaEnum_"+this.name());
	}
	
}
