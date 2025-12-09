package negocio.comuns.gsuite.enumeradores;


import negocio.comuns.utilitarias.Uteis;

public enum TipoServicoAdminSdkGoogleEnum {
	NENHUM, CRIAR, IMPORTAR, CRIAR_CLASSROOM, CRIAR_BLACKBORD;

	
	public boolean isCriar(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoServicoAdminSdkGoogleEnum.CRIAR.name()); 
	}
	
	public boolean isImportar(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoServicoAdminSdkGoogleEnum.IMPORTAR.name()); 
	}
	
	public boolean isCriarClassroom(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoServicoAdminSdkGoogleEnum.CRIAR_CLASSROOM.name()); 
	}
	
	public boolean isCriarBlackboard(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoServicoAdminSdkGoogleEnum.CRIAR_BLACKBORD.name()); 
	}

}
