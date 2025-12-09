package webservice.boletoonline.caixaEconomicaFederal.classes;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("soapenv:Envelope")
public class SoapenvEnvelopeCaixaVO {

	@XStreamAlias("soapenv:Header")
	public String soapenvHeader;	
	
	@XStreamAsAttribute
	public String xmlns1 ;
	
	@XStreamAsAttribute
	public String xmlns2 ;
	
	@XStreamAsAttribute
	public String xmlns3 ;
	
	
	
	@XStreamAlias("soapenv:Body")
	public SoapEnvBodyCaixaEconomicaVO soapenvBodyCaixaEconomicaVO;
	
	
	public SoapenvEnvelopeCaixaVO () {		
		setSoapenvBodyCaixaEconomicaVO(new SoapEnvBodyCaixaEconomicaVO());
		setSoapenvHeader("");	
	}
	
	public SoapEnvBodyCaixaEconomicaVO getSoapenvBodyCaixaEconomicaVO() {
		return soapenvBodyCaixaEconomicaVO;
	}
	
	public void setSoapenvBodyCaixaEconomicaVO(SoapEnvBodyCaixaEconomicaVO soapenvBodyCaixaEconomicaVO) {
		this.soapenvBodyCaixaEconomicaVO = soapenvBodyCaixaEconomicaVO;
	}

	public String getSoapenvHeader() {
		return soapenvHeader;
	}
	
	public void setSoapenvHeader(String soapenvHeader) {
		this.soapenvHeader = soapenvHeader;
	}

	public String getXmlns1() {
		return xmlns1;
	}

	public void setXmlns1(String xmlns1) {
		this.xmlns1 = xmlns1;
	}

	public String getXmlns2() {
		return xmlns2;
	}

	public void setXmlns2(String xmlns2) {
		this.xmlns2 = xmlns2;
	}

	

	public String getXmlns3() {
		return xmlns3;
	}

	public void setXmlns3(String xmlns3) {
		this.xmlns3 = xmlns3;
	}
   
	
	
	
	
}
