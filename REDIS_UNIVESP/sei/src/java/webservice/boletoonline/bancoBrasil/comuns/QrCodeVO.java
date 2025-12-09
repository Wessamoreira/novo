package webservice.boletoonline.bancoBrasil.comuns;

public class QrCodeVO {
	
	private String url;
	private String txId;
	private String emv;
	
	
	public String getUrl() {
		if(url == null ) {
			url ="";
		}
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTxId() {
		if(txId == null ) {
			txId ="";
		}
		return txId;
	}
	public void setTxId(String txId) {
		this.txId = txId;
	}
	public String getEmv() {
		if(emv == null ) {
			emv ="";
		}
		return emv;
	}
	public void setEmv(String emv) {
		this.emv = emv;
	}

}
