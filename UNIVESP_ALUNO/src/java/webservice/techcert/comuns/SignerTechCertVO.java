package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SignerTechCertVO {
    @SerializedName("subjectName")
    private String subjectName;

    @SerializedName("emailAddress")
    private String emailAddress;

    @SerializedName("issuerName")
    private String issuerName;

    @SerializedName("identifier")
    private String identifier;

    @SerializedName("companyName")
    private String companyName;

    @SerializedName("companyIdentifier")
    private String companyIdentifier;

    @SerializedName("isElectronic")
    private Boolean isElectronic;

    @SerializedName("isTimestamp")
    private Boolean isTimestamp;

    @SerializedName("signingTime")
    private String signingTime; // ou OffsetDateTime, se preferir

    @SerializedName("certificateThumbprint")
    private String certificateThumbprint;

    @SerializedName("evidences")
    private EvidencesTechCertVO evidences;

    @SerializedName("attributeCertificates")
    private List<AttributeCertificateTechCertVO> attributeCertificates;

    @SerializedName("validationResults")
    private ValidationResultsTechCertVO validationResults;

    @SerializedName("validityStart")
    private String validityStart;

    @SerializedName("validityEnd")
    private String validityEnd;

    @SerializedName("signatureTimestamps")
    private List<SignatureTimestampTechCertVO> signatureTimestamps;

    @SerializedName("archiveTimestamps")
    private List<ArchiveTimestampTechCertVO> archiveTimestamps;

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getIssuerName() {
        return issuerName;
    }

    public void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyIdentifier() {
        return companyIdentifier;
    }

    public void setCompanyIdentifier(String companyIdentifier) {
        this.companyIdentifier = companyIdentifier;
    }

    public Boolean getElectronic() {
        return isElectronic;
    }

    public void setElectronic(Boolean electronic) {
        isElectronic = electronic;
    }

    public Boolean getTimestamp() {
        return isTimestamp;
    }

    public void setTimestamp(Boolean timestamp) {
        isTimestamp = timestamp;
    }

    public String getSigningTime() {
        return signingTime;
    }

    public void setSigningTime(String signingTime) {
        this.signingTime = signingTime;
    }

    public String getCertificateThumbprint() {
        return certificateThumbprint;
    }

    public void setCertificateThumbprint(String certificateThumbprint) {
        this.certificateThumbprint = certificateThumbprint;
    }

    public EvidencesTechCertVO getEvidences() {
        return evidences;
    }

    public void setEvidences(EvidencesTechCertVO evidences) {
        this.evidences = evidences;
    }

    public List<AttributeCertificateTechCertVO> getAttributeCertificates() {
        return attributeCertificates;
    }

    public void setAttributeCertificates(List<AttributeCertificateTechCertVO> attributeCertificates) {
        this.attributeCertificates = attributeCertificates;
    }

    public ValidationResultsTechCertVO getValidationResults() {
        return validationResults;
    }

    public void setValidationResults(ValidationResultsTechCertVO validationResults) {
        this.validationResults = validationResults;
    }

    public String getValidityStart() {
        return validityStart;
    }

    public void setValidityStart(String validityStart) {
        this.validityStart = validityStart;
    }

    public String getValidityEnd() {
        return validityEnd;
    }

    public void setValidityEnd(String validityEnd) {
        this.validityEnd = validityEnd;
    }

    public List<SignatureTimestampTechCertVO> getSignatureTimestamps() {
        return signatureTimestamps;
    }

    public void setSignatureTimestamps(List<SignatureTimestampTechCertVO> signatureTimestamps) {
        this.signatureTimestamps = signatureTimestamps;
    }

    public List<ArchiveTimestampTechCertVO> getArchiveTimestamps() {
        return archiveTimestamps;
    }

    public void setArchiveTimestamps(List<ArchiveTimestampTechCertVO> archiveTimestamps) {
        this.archiveTimestamps = archiveTimestamps;
    }
}
