package negocio.comuns.basico.enumeradores;

import negocio.comuns.utilitarias.Uteis;

public enum ProvedorDeAssinaturaEnum {
	SEI, 
	CERTISIGN,
	IMPRENSAOFICIAL,
	TECHCERT;
	
	
	public boolean isProvedorSei(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(ProvedorDeAssinaturaEnum.SEI.name()); 
	}
	
	public boolean isProvedorCertisign(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(ProvedorDeAssinaturaEnum.CERTISIGN.name()); 
	}
	
	public boolean isProvedorImprensaOficial(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(ProvedorDeAssinaturaEnum.IMPRENSAOFICIAL.name()); 
	}

	public boolean isProvedorTechCert(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(ProvedorDeAssinaturaEnum.TECHCERT.name());
	}
}
