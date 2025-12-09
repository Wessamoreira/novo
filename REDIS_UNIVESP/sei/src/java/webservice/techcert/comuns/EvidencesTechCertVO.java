package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EvidencesTechCertVO {

    @SerializedName("ipAddress")
    private String ipAddress;

    @SerializedName("authenticationTypes")
    private List<String> authenticationTypes;

    @SerializedName("accountVerifiedEmail")
    private String accountVerifiedEmail;

    @SerializedName("authenticatedEmail")
    private String authenticatedEmail;

    @SerializedName("authenticatedPhoneNumberLastDigits")
    private String authenticatedPhoneNumberLastDigits;

    @SerializedName("authenticatedApplication")
    private AuthenticatedApplicationTechCertVO authenticatedApplication;

    @SerializedName("authenticatedSelfie")
    private String authenticatedSelfie;

    @SerializedName("authenticatedPix")
    private String authenticatedPix;

    @SerializedName("livenessData")
    private String livenessData;

    @SerializedName("geolocation")
    private GeolocationTechCertVO geolocation;

    @SerializedName("timestamp")
    private String timestamp; // ou OffsetDateTime

    @SerializedName("evidencesSha256")
    private String evidencesSha256;

    @SerializedName("authenticatedPhoneNumber")
    private String authenticatedPhoneNumber;

    @SerializedName("file")
    private String file;

    @SerializedName("fileTicket")
    private String fileTicket;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public List<String> getAuthenticationTypes() {
        return authenticationTypes;
    }

    public void setAuthenticationTypes(List<String> authenticationTypes) {
        this.authenticationTypes = authenticationTypes;
    }

    public String getAccountVerifiedEmail() {
        return accountVerifiedEmail;
    }

    public void setAccountVerifiedEmail(String accountVerifiedEmail) {
        this.accountVerifiedEmail = accountVerifiedEmail;
    }

    public String getAuthenticatedEmail() {
        return authenticatedEmail;
    }

    public void setAuthenticatedEmail(String authenticatedEmail) {
        this.authenticatedEmail = authenticatedEmail;
    }

    public String getAuthenticatedPhoneNumberLastDigits() {
        return authenticatedPhoneNumberLastDigits;
    }

    public void setAuthenticatedPhoneNumberLastDigits(String authenticatedPhoneNumberLastDigits) {
        this.authenticatedPhoneNumberLastDigits = authenticatedPhoneNumberLastDigits;
    }

    public AuthenticatedApplicationTechCertVO getAuthenticatedApplication() {
        return authenticatedApplication;
    }

    public void setAuthenticatedApplication(AuthenticatedApplicationTechCertVO authenticatedApplication) {
        this.authenticatedApplication = authenticatedApplication;
    }

    public String getAuthenticatedSelfie() {
        return authenticatedSelfie;
    }

    public void setAuthenticatedSelfie(String authenticatedSelfie) {
        this.authenticatedSelfie = authenticatedSelfie;
    }

    public String getAuthenticatedPix() {
        return authenticatedPix;
    }

    public void setAuthenticatedPix(String authenticatedPix) {
        this.authenticatedPix = authenticatedPix;
    }

    public String getLivenessData() {
        return livenessData;
    }

    public void setLivenessData(String livenessData) {
        this.livenessData = livenessData;
    }

    public GeolocationTechCertVO getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(GeolocationTechCertVO geolocation) {
        this.geolocation = geolocation;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEvidencesSha256() {
        return evidencesSha256;
    }

    public void setEvidencesSha256(String evidencesSha256) {
        this.evidencesSha256 = evidencesSha256;
    }

    public String getAuthenticatedPhoneNumber() {
        return authenticatedPhoneNumber;
    }

    public void setAuthenticatedPhoneNumber(String authenticatedPhoneNumber) {
        this.authenticatedPhoneNumber = authenticatedPhoneNumber;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileTicket() {
        return fileTicket;
    }

    public void setFileTicket(String fileTicket) {
        this.fileTicket = fileTicket;
    }
}
