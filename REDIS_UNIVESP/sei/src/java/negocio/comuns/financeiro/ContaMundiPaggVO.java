package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;

/**
 * 
 * @author Victor Hugo de Paula Costa 11/03/2015 5.0.4.0
 *
 */
public class ContaMundiPaggVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String rawRequest;
	private String rawResponse;
	private String chaveContaMundiPagg;
	private String smartWalletKey;
	private String merchantKey;
	private String cnpj;

	public Integer getCodigo() {
		if(codigo == null)
			codigo = 0;
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getRawRequest() {
		if(rawRequest == null)
			rawRequest = "";
		return rawRequest;
	}

	public void setRawRequest(String rawRequest) {
		this.rawRequest = rawRequest;
	}

	public String getRawResponse() {
		if(rawResponse == null)
			rawResponse = "";
		return rawResponse;
	}

	public void setRawResponse(String rawResponse) {
		this.rawResponse = rawResponse;
	}

	public String getChaveContaMundiPagg() {
		if(chaveContaMundiPagg == null)
			chaveContaMundiPagg = "";
		return chaveContaMundiPagg;
	}

	public void setChaveContaMundiPagg(String chaveContaMundiPagg) {
		this.chaveContaMundiPagg = chaveContaMundiPagg;
	}

	public String getSmartWalletKey() {
		if(smartWalletKey == null)
			smartWalletKey = "";
		return smartWalletKey;
	}

	public void setSmartWalletKey(String smartWalletKey) {
		this.smartWalletKey = smartWalletKey;
	}

	public String getMerchantKey() {
		if(merchantKey == null) {
			merchantKey = "";
		}
		return merchantKey;
	}

	public void setMerchantKey(String merchantKey) {
		this.merchantKey = merchantKey;
	}

	public String getCnpj() {
		if(cnpj == null) {
			cnpj = "";
		}
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
}
