package negocio.comuns.academico.enumeradores;

public enum DocumentoAssinadoOrigemEnum {
	
NENHUM, CERTISIGN_WS, CERTISIGN_SEI, IMPRENSAOFICIAL_SEI, TECHCERT_SEI, TECHCERT_WS;
	
	public boolean isNenhum() {
		return name()!= null && name().equals(DocumentoAssinadoOrigemEnum.NENHUM.name());
	}
	
	public boolean isCertisignWS() {
		return name()!= null && name().equals(DocumentoAssinadoOrigemEnum.CERTISIGN_WS.name());
	}
	
	public boolean isCertisignSEI() {
		return name()!= null && name().equals(DocumentoAssinadoOrigemEnum.CERTISIGN_SEI.name());
	}

	public boolean isTechCertSEI() {
		return name()!= null && name().equals(DocumentoAssinadoOrigemEnum.TECHCERT_SEI.name());
	}
	
	public boolean isImprensaOficialSEI() {
		return name()!= null && name().equals(DocumentoAssinadoOrigemEnum.IMPRENSAOFICIAL_SEI.name());
	}

}
