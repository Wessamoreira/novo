package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class DocumentsCreateTechCertVO {

    @SerializedName("uploadId")
    private String uploadId;

    @SerializedName("documentId")
    private String documentId;

    @SerializedName("attachments")
    private String attachments;

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }
}
