package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.Uteis;

public enum OperacaoDeVinculoEstagioEnum {
	INDEFERIR,
	AGUARDANDO_ASSINATURA,
	NOVO_TERMO_ESTAGIO_ASSINATURA;
	
	public boolean isIndeferir() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(OperacaoDeVinculoEstagioEnum.INDEFERIR.name());
	}
	
	public boolean isAguardandoAssinatura() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(OperacaoDeVinculoEstagioEnum.AGUARDANDO_ASSINATURA.name());
	}
	
	public boolean isNovoTermoEstagioAssinatura() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(OperacaoDeVinculoEstagioEnum.NOVO_TERMO_ESTAGIO_ASSINATURA.name());
	}
		

}
