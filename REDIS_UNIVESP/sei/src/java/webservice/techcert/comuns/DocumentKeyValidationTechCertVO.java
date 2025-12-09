package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocumentKeyValidationTechCertVO {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("filename")
    private String filename;

    @SerializedName("mimeType")
    private String mimeType;

    @SerializedName("createdBy")
    private CreatedByTechCertVO createdBy;

    @SerializedName("isConcluded")
    private Boolean isConcluded;

    @SerializedName("isFile")
    private Boolean isFile;

    @SerializedName("isEnvelope")
    private Boolean isEnvelope;

    @SerializedName("creationDate")
    private String creationDate;

    @SerializedName("updateDate")
    private String updateDate;

    @SerializedName("signers")
    private List<SignerTechCertVO> signers;

    @SerializedName("status")
    private String status;

    @SerializedName("signatureType")
    private String signatureType;

    @SerializedName("securityContext")
    private SecurityContextTechCertVO securityContext;

    private Boolean documentHasNoSignatures;

    private Boolean documentInvalid;

    private Boolean noPendingActionFoundForEmailAndIdentifier;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public CreatedByTechCertVO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedByTechCertVO createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getConcluded() {
        return isConcluded;
    }

    public void setConcluded(Boolean concluded) {
        isConcluded = concluded;
    }

    public Boolean getFile() {
        return isFile;
    }

    public void setFile(Boolean file) {
        isFile = file;
    }

    public Boolean getEnvelope() {
        return isEnvelope;
    }

    public void setEnvelope(Boolean envelope) {
        isEnvelope = envelope;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public List<SignerTechCertVO> getSigners() {
        return signers;
    }

    public void setSigners(List<SignerTechCertVO> signers) {
        this.signers = signers;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSignatureType() {
        return signatureType;
    }

    public void setSignatureType(String signatureType) {
        this.signatureType = signatureType;
    }

    public SecurityContextTechCertVO getSecurityContext() {
        return securityContext;
    }

    public void setSecurityContext(SecurityContextTechCertVO securityContext) {
        this.securityContext = securityContext;
    }

    public Boolean isDocumentHasNoSignatures() {
        if (documentHasNoSignatures == null){
            documentHasNoSignatures= Boolean.FALSE;
        }
        return documentHasNoSignatures;
    }

    public void setDocumentHasNoSignatures(Boolean documentHasNoSignatures) {
        this.documentHasNoSignatures = documentHasNoSignatures;
    }

    public Boolean getDocumentInvalid() {
        if (documentInvalid == null) {
            documentInvalid = Boolean.FALSE;
        }
        return documentInvalid;
    }

    public void setDocumentInvalid(Boolean documentInvalid) {
        this.documentInvalid = documentInvalid;
    }

    public Boolean getNoPendingActionFoundForEmailAndIdentifier() {
        if (noPendingActionFoundForEmailAndIdentifier == null) {
            noPendingActionFoundForEmailAndIdentifier = Boolean.FALSE;
        }
        return noPendingActionFoundForEmailAndIdentifier;
    }

    public void setNoPendingActionFoundForEmailAndIdentifier(Boolean noPendingActionFoundForEmailAndIdentifier) {
        this.noPendingActionFoundForEmailAndIdentifier = noPendingActionFoundForEmailAndIdentifier;
    }
}
