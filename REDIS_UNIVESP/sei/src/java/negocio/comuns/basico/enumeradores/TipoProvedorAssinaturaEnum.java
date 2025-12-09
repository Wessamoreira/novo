package negocio.comuns.basico.enumeradores;

import negocio.comuns.utilitarias.Uteis;

public enum TipoProvedorAssinaturaEnum {
	
	ASSINATURA_DIGITAL, 
	ASSINATURA_ELETRONICA;
	
	
	public boolean isAssinaturaDigital(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoProvedorAssinaturaEnum.ASSINATURA_DIGITAL.name()); 
	}
	
	public boolean isAssinaturaEletronica(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoProvedorAssinaturaEnum.ASSINATURA_ELETRONICA.name()); 
	}

}
