package webservice.nfse.generic;

public enum CidadesWebServiceNFSEEnum {
	
	MANAUS_AM("1302603", false, true),
	CUIABA_MT("5103403", true, true),
	PARAISO_DO_TOCANTINS_TO("1716109", true, true),
	BELO_HORIZONTE_MG("3106200", true, false),
	UBERLANDIA_MG("3170206", true, false),
	SAO_LUIS_MA("2111300", false, false),
	LUIS_EDUARDO_MAGALHAES_BA("2919553", true, true),
	SAO_PAULO_SP("3550308", true, true);
	
	private String codigoIBGE;
	private Boolean possuiWebserviceCancelamento;
	private Boolean retornaUrlNotaAutorizada; // Caso não retorne url, será gerado um pdf
	
	CidadesWebServiceNFSEEnum(String codigoIBGE, Boolean possuiWebserviceCancelamento, Boolean retornaUrlNotaAutorizada) {
		this.codigoIBGE = codigoIBGE;
		this.possuiWebserviceCancelamento = possuiWebserviceCancelamento;
		this.retornaUrlNotaAutorizada = retornaUrlNotaAutorizada;
	}
	
	public static CidadesWebServiceNFSEEnum getEnumPorCodigoIBGE(String codigoIBGE) {
		for (CidadesWebServiceNFSEEnum e : CidadesWebServiceNFSEEnum.values()) {
			if (e.getCodigoIBGE().equals(codigoIBGE)) {
				return e;
			}
		}
		return null;
	}

	public String getCodigoIBGE() {
		return codigoIBGE;
	}

	public void setCodigoIBGE(String codigoIBGE) {
		this.codigoIBGE = codigoIBGE;
	}

	public Boolean getPossuiWebserviceCancelamento() {
		return possuiWebserviceCancelamento;
	}

	public void setPossuiWebserviceCancelamento(Boolean possuiWebserviceCancelamento) {
		this.possuiWebserviceCancelamento = possuiWebserviceCancelamento;
	}

	public Boolean getRetornaUrlNotaAutorizada() {
		return retornaUrlNotaAutorizada;
	}

	public void setRetornaUrlNotaAutorizada(Boolean retornaUrlNotaAutorizada) {
		this.retornaUrlNotaAutorizada = retornaUrlNotaAutorizada;
	}

}
