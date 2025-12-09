package webservice.boletoonline.santander.classes;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public class SoapenvEnvelopeVO {

	public String soapenvHeader;
	@XStreamAsAttribute
	public String xmlns1 = "http://schemas.xmlsoap.org/soap/envelope/";
	@XStreamAsAttribute
	public String xmlns2 = "http://impl.webservice.dl.app.bsbr.altec.com/";
	public SoapenvBodyVO soapenvBody;
	
	public SoapenvEnvelopeVO () {
		setSoapenvBody(new SoapenvBodyVO());
		setSoapenvHeader("");
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
