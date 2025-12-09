package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.Uteis;

public enum TipoAssinaturaDocumentoEnum {
	
	NENHUM,
	ELETRONICA,
	CERTIFICADO_DIGITAL;
	
	public boolean isNenhum(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoAssinaturaDocumentoEnum.NENHUM.name()); 
	}
	
	public boolean isEletronica(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoAssinaturaDocumentoEnum.ELETRONICA.name()); 
	}
	
	public boolean isCertificadoDigital(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoAssinaturaDocumentoEnum.CERTIFICADO_DIGITAL.name()); 
	}
	

}
