package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.Uteis;

public enum SituacaoQuestionarioRespostaOrigemEnum {
	EM_PREENCHIMENTO, 
	EM_ANALISE, 
	EM_CORRECAO, 
	INDEFERIDO, 
	DEFERIDO;
	
	public boolean isEmPreenchimento() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoQuestionarioRespostaOrigemEnum.EM_PREENCHIMENTO.name());
	}
	
	public boolean isEmAnalise() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoQuestionarioRespostaOrigemEnum.EM_ANALISE.name());
	}
	
	public boolean isEmCorrecao() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoQuestionarioRespostaOrigemEnum.EM_CORRECAO.name());
	}
	
	public boolean isIndeferido() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoQuestionarioRespostaOrigemEnum.INDEFERIDO.name());
	}
	
	public boolean isDeferido() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoQuestionarioRespostaOrigemEnum.DEFERIDO.name());
	}

}
