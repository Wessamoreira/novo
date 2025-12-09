package webservice.certisign.comuns;

import java.util.Date;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import webservice.DateAdapterMobile;

public class CertiSignCallBackSignaturePessoaRSVO {
	
		
	    
		private String date;
		private CertiSignCallBackSignaturePessoaCertificateRSVO  certificate;
		private String evidencesHash ;
		private String user;
		private String identifier;
		private String nameIdentifier;
		private String imageUri;
		private String hasImage;		
		
		
		
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		
		
		public String getEvidencesHash() {
			return evidencesHash;
		}
		public void setEvidencesHash(String evidencesHash) {
			this.evidencesHash = evidencesHash;
		}
		
		
		public String getUser() {
			return user;
		}
		public void setUser(String user) {
			this.user = user;
		}
		
		
		public String getIdentifier() {
			return identifier;
		}
		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}
		
		
		public String getNameIdentifier() {
			return nameIdentifier;
		}
		public void setNameIdentifier(String nameIdentifier) {
			this.nameIdentifier = nameIdentifier;
		}
		
		
		public String getImageUri() {
			return imageUri;
		}
		public void setImageUri(String imageUri) {
			this.imageUri = imageUri;
		}
		
		
		public String getHasImage() {
			return hasImage;
		}
		public void setHasImage(String hasImage) {
			this.hasImage = hasImage;
		}
		public CertiSignCallBackSignaturePessoaCertificateRSVO getCertificate() {
			return certificate;
		}
		public void setCertificate(CertiSignCallBackSignaturePessoaCertificateRSVO certificate) {
			this.certificate = certificate;
		}
		
		

}
