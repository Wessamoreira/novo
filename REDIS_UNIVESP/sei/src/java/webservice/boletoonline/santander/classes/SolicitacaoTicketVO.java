package webservice.boletoonline.santander.classes;

public class SolicitacaoTicketVO {

	public String soapenvHeader;
	public SoapenvBodyVO soapenvBody;

	public SolicitacaoTicketVO () {
		setSoapenvBody(new SoapenvBodyVO());
	}
	
	public String getSoapenvHeader() {
		return soapenvHeader;
	}
	public void setSoapenvHeader(String soapenvHeader) {
		this.soapenvHeader = soapenvHeader;
	}
	public SoapenvBodyVO getSoapenvBody() {
		return soapenvBody;
	}
	public void setSoapenvBody(SoapenvBodyVO soapenvBody) {
		this.soapenvBody = soapenvBody;
	}

}
