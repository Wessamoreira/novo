package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class DocumentsTechCertVO {
    @SerializedName("checksumMd5")
    private String checksumMd5;

    @SerializedName("isDeleted")
    private Boolean isDeleted;

    @SerializedName("flowActions")
    private List<FlowActionsTechCertVO> flowActions;

    @SerializedName("observers")
    private List<ObserverTechCertVO> observers;

    @SerializedName("attachments")
    private List<AttachmentTechCertVO> attachments;

    @SerializedName("permissions")
    private PermissionsTechCertVO permissions;

    @SerializedName("notifiedEmails")
    private List<String> notifiedEmails;

    @SerializedName("key")
    private String key;

    @SerializedName("hideDownloadOptionForPendingDocuments")
    private Boolean hideDownloadOptionForPendingDocuments;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("filename")
    private String filename;

    @SerializedName("fileSize")
    private Long fileSize;

    @SerializedName("mimeType")
    private String mimeType;

    @SerializedName("hasSignature")
    private Boolean hasSignature;

    @SerializedName("status")
    private String status;

    @SerializedName("isConcluded")
    private Boolean isConcluded;

    @SerializedName("folder")
    private FolderTechCertVO folder;

    @SerializedName("organization")
    private OrganizationTechCertVO organization;

    @SerializedName("creationDate")
    private String creationDate;

    @SerializedName("updateDate")
    private String updateDate;

    @SerializedName("expirationDate")
    private String expirationDate;

    @SerializedName("expirationDateWithoutTime")
    private String expirationDateWithoutTime;

    @SerializedName("createdBy")
    private CreatedByTechCertVO createdBy;

    @SerializedName("description")
    private String description;

    @SerializedName("forceCadesSignature")
    private Boolean forceCadesSignature;

    @SerializedName("isScanned")
    private Boolean isScanned;

    @SerializedName("isEnvelope")
    private Boolean isEnvelope;

    @SerializedName("statusUpdatedBy")
    private StatusUpdatedByTechCertVO statusUpdatedBy;

    @SerializedName("statusUpdateReason")
    private String statusUpdateReason;

    @SerializedName("tags")
    private List<TagTechCertVO> tags;

    @SerializedName("signatureType")
    private String signatureType;

    @SerializedName("securityContext")
    private SecurityContextTechCertVO securityContext;

    private Boolean isDeletedDocumentTechCert;

    private Boolean isDocumentCancelledTechCert;

    public String getChecksumMd5() {
        return checksumMd5;
    }

    public void setChecksumMd5(String checksumMd5) {
        this.checksumMd5 = checksumMd5;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public List<FlowActionsTechCertVO> getFlowActions() {
        return flowActions;
    }

    public void setFlowActions(List<FlowActionsTechCertVO> flowActions) {
        this.flowActions = flowActions;
    }

    public List<AttachmentTechCertVO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentTechCertVO> attachments) {
        this.attachments = attachments;
    }

    public PermissionsTechCertVO getPermissions() {
        return permissions;
    }

    public void setPermissions(PermissionsTechCertVO permissions) {
        this.permissions = permissions;
    }

    public List<String> getNotifiedEmails() {
        return notifiedEmails;
    }

    public void setNotifiedEmails(List<String> notifiedEmails) {
        this.notifiedEmails = notifiedEmails;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getHideDownloadOptionForPendingDocuments() {
        return hideDownloadOptionForPendingDocuments;
    }

    public void setHideDownloadOptionForPendingDocuments(Boolean hideDownloadOptionForPendingDocuments) {
        this.hideDownloadOptionForPendingDocuments = hideDownloadOptionForPendingDocuments;
    }

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

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Boolean getHasSignature() {
        return hasSignature;
    }

    public void setHasSignature(Boolean hasSignature) {
        this.hasSignature = hasSignature;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getConcluded() {
        return isConcluded;
    }

    public void setConcluded(Boolean concluded) {
        isConcluded = concluded;
    }

    public FolderTechCertVO getFolder() {
        return folder;
    }

    public void setFolder(FolderTechCertVO folder) {
        this.folder = folder;
    }

    public OrganizationTechCertVO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationTechCertVO organization) {
        this.organization = organization;
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

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getExpirationDateWithoutTime() {
        return expirationDateWithoutTime;
    }

    public void setExpirationDateWithoutTime(String expirationDateWithoutTime) {
        this.expirationDateWithoutTime = expirationDateWithoutTime;
    }

    public CreatedByTechCertVO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedByTechCertVO createdBy) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getForceCadesSignature() {
        return forceCadesSignature;
    }

    public void setForceCadesSignature(Boolean forceCadesSignature) {
        this.forceCadesSignature = forceCadesSignature;
    }

    public Boolean getScanned() {
        return isScanned;
    }

    public void setScanned(Boolean scanned) {
        isScanned = scanned;
    }

    public Boolean getEnvelope() {
        return isEnvelope;
    }

    public void setEnvelope(Boolean envelope) {
        isEnvelope = envelope;
    }

    public StatusUpdatedByTechCertVO getStatusUpdatedBy() {
        return statusUpdatedBy;
    }

    public void setStatusUpdatedBy(StatusUpdatedByTechCertVO statusUpdatedBy) {
        this.statusUpdatedBy = statusUpdatedBy;
    }

    public String getStatusUpdateReason() {
        return statusUpdateReason;
    }

    public void setStatusUpdateReason(String statusUpdateReason) {
        this.statusUpdateReason = statusUpdateReason;
    }

    public List<TagTechCertVO> getTags() {
        return tags;
    }

    public void setTags(List<TagTechCertVO> tags) {
        this.tags = tags;
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

    public Boolean getDeletedDocumentTechCert() {
        if (isDeletedDocumentTechCert == null) {
            isDeletedDocumentTechCert = false;
        }
        return isDeletedDocumentTechCert;
    }

    public void setDeletedDocumentTechCert(Boolean deletedDocumentTechCert) {
        isDeletedDocumentTechCert = deletedDocumentTechCert;
    }

    public List<ObserverTechCertVO> getObservers() {
        return observers;
    }

    public void setObservers(List<ObserverTechCertVO> observers) {
        this.observers = observers;
    }

    public Boolean getDocumentCancelledTechCert() {
        if (isDocumentCancelledTechCert == null) {
            isDocumentCancelledTechCert = false;
        }
        return isDocumentCancelledTechCert;
    }

    public void setDocumentCancelledTechCert(Boolean documentCancelledTechCert) {
        isDocumentCancelledTechCert = documentCancelledTechCert;
    }
}
