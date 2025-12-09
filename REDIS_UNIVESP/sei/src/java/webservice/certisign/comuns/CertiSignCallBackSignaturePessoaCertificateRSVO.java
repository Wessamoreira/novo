package webservice.certisign.comuns;

import javax.xml.bind.annotation.XmlElement;

public class CertiSignCallBackSignaturePessoaCertificateRSVO {

	
	private  String signer;	
	private String individualIdentificationCode;
	private String  fiscalCode;
	private String  dni;
	private String  pyCI;
	private String  email;
	private String  validity;
	private String  birthDate;
	
	
	@XmlElement(name = "signer")
	public String getSigner() {
		return signer;
	}
	public void setSigner(String signer) {
		this.signer = signer;
	}
	
	@XmlElement(name = "individualIdentificationCode")
	public String getIndividualIdentificationCode() {
		return individualIdentificationCode;
	}
	public void setIndividualIdentificationCode(String individualIdentificationCode) {
		this.individualIdentificationCode = individualIdentificationCode;
	}
	
	@XmlElement(name = "fiscalCode")
	public String getFiscalCode() {
		return fiscalCode;
	}
	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}
	
	@XmlElement(name = "dni")
	public String getDni() {
		return dni;
	}
	public void setDni(String dni) {
		this.dni = dni;
	}
	
	@XmlElement(name = "pyCI")
	public String getPyCI() {
		return pyCI;
	}
	public void setPyCI(String pyCI) {
		this.pyCI = pyCI;
	}
	
	@XmlElement(name = "email")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@XmlElement(name = "validity")
	public String getValidity() {
		return validity;
	}
	public void setValidity(String validity) {
		this.validity = validity;
	}
	
	@XmlElement(name = "birthDate")
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
}
