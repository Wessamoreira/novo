package negocio.comuns.protocolo.enumeradores;


import negocio.comuns.utilitarias.Uteis;

public enum SituacaoRequerimentoDisciplinasAproveitadasEnum {
	
	AGUARDANDO_ANALISE, DEFERIDO, INDEFERIDO;
	
	public boolean isAguardandoAnalise() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoRequerimentoDisciplinasAproveitadasEnum.AGUARDANDO_ANALISE.name());
	}
	
	public boolean isDeferido() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoRequerimentoDisciplinasAproveitadasEnum.DEFERIDO.name());
	}
	
	public boolean isIndeferido() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoRequerimentoDisciplinasAproveitadasEnum.INDEFERIDO.name());
	}
	
	public String getDescricao() {
		return Uteis.isAtributoPreenchido(name()) ? name() :"";
	}
	

}
