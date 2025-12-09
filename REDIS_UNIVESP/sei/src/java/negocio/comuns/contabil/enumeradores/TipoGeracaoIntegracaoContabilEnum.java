package negocio.comuns.contabil.enumeradores;

import negocio.comuns.utilitarias.Uteis;

public enum TipoGeracaoIntegracaoContabilEnum {
	
	UNIDADE_ENSINO, CODIGO_INTEGRACAO;
	
	
	public boolean isUnidadeEnsino(){
    	return  Uteis.isAtributoPreenchido(name()) && name().equals(TipoGeracaoIntegracaoContabilEnum.UNIDADE_ENSINO.name());
    }
	
	public boolean isCodigoIntegracao(){
		return  Uteis.isAtributoPreenchido(name()) && name().equals(TipoGeracaoIntegracaoContabilEnum.CODIGO_INTEGRACAO.name());
	}

}
