package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.Uteis;

public enum TipoAdminSdkIntegracaoEnum {
	NENHUM, GSUITE, BLACKBOARD;
	
	public boolean isIntegracaoGsuite(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoAdminSdkIntegracaoEnum.GSUITE.name()); 
	}
	
	public boolean isIntegracaoBlackboard(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoAdminSdkIntegracaoEnum.BLACKBOARD.name()); 
	}

}
