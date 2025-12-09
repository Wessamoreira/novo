package negocio.comuns.estagio.enumeradores;

import negocio.comuns.utilitarias.Uteis;

public enum SituacaoAdicionalEstagioEnum {
	NENHUM,
	INDEFERIDO_ALUNO,
	INDEFERIDO_FACILITADOR,
	PENDENTE_SOLICITACAO_ASSINATURA, 
	ASSINATURA_PENDENTE,
	DEFERIDO,
	AGUARDANDO_RELATORIO_FINAL;
	
	
	public boolean isPendenteSolicitacaoAssinatura() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoAdicionalEstagioEnum.PENDENTE_SOLICITACAO_ASSINATURA.name());
	}
	
	public boolean isAssinaturaPendente() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoAdicionalEstagioEnum.ASSINATURA_PENDENTE.name());
	}
	
	public boolean isAguardandoRelatorioFinal() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoAdicionalEstagioEnum.AGUARDANDO_RELATORIO_FINAL.name());
	}
	
	public boolean isIndeferidoAluno() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoAdicionalEstagioEnum.INDEFERIDO_ALUNO.name());
	}
	
	public boolean isIndeferidoFacilitador() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoAdicionalEstagioEnum.INDEFERIDO_FACILITADOR.name());
	}
	
	public boolean isDeferido() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoAdicionalEstagioEnum.DEFERIDO.name());
	}
	
	public boolean isNenhum() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(SituacaoAdicionalEstagioEnum.NENHUM.name());
	}

}
