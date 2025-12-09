package webservice.certisign.comuns;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;

public class CertiSignCallBackSignatureValidateRSVO {
	
	
   //callback ValidateSinatures atributes 
	private String documentName ;
	private String key;
	private Boolean isValid ;	
	private List<CertiSignCallBackSignaturePessoaRSVO> signatures ;
	private List<CertiSignCallBackSignaturePessoaRSVO> electronicSignatures;

	
	@XmlElement(name = "documentName")
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	
	@XmlElement(name = "key")
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	@XmlElement(name = "isValid")
	public Boolean getIsValid() {
		return isValid;
	}
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
	
	@XmlElement(name = "signatures")
	public List<CertiSignCallBackSignaturePessoaRSVO> getSignatures() {
		return signatures;
	}
	public void setSignatures(List<CertiSignCallBackSignaturePessoaRSVO> signatures) {
		this.signatures = signatures;
	}
	
	@XmlElement(name = "electronicSignatures")
	public List<CertiSignCallBackSignaturePessoaRSVO> getElectronicSignatures() {
		return electronicSignatures;
	}
	public void setElectronicSignatures(List<CertiSignCallBackSignaturePessoaRSVO> electronicSignatures) {
		this.electronicSignatures = electronicSignatures;
	}
	
	
	
	

}
