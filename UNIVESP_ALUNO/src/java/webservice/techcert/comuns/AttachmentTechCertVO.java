package webservice.techcert.comuns;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

public class AttachmentTechCertVO {
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

    @SerializedName("creationDate")
    private String creationDate;

    @SerializedName("updateDate")
    private String updateDate;

    @SerializedName("createdBy")
    @JsonIgnoreProperties
    private CreatedByTechCertVO createdBy;

    @SerializedName("isPrivate")
    private Boolean isPrivate;

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

    public CreatedByTechCertVO getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedByTechCertVO createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }
}
