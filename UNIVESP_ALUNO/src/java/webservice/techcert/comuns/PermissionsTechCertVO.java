package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class PermissionsTechCertVO {
    @SerializedName("move")
    private Boolean move;

    @SerializedName("editFlow")
    private Boolean editFlow;

    @SerializedName("viewSignedDocumentWithExternalFlowBeforeCompleted")
    private Boolean viewSignedDocumentWithExternalFlowBeforeCompleted;

    @SerializedName("submitExternalFlowFiles")
    private Boolean submitExternalFlowFiles;

    @SerializedName("updateDocumentVersion")
    private Boolean updateDocumentVersion;

    @SerializedName("createAttachments")
    private Boolean createAttachments;

    @SerializedName("deleteAttachments")
    private Boolean deleteAttachments;

    @SerializedName("viewPrivateAttachments")
    private Boolean viewPrivateAttachments;

    @SerializedName("editAttachments")
    private Boolean editAttachments;

    @SerializedName("createMeeting")
    private Boolean createMeeting;

    @SerializedName("deleteMeeting")
    private Boolean deleteMeeting;

    @SerializedName("refuse")
    private Boolean refuse;

    @SerializedName("editInformation")
    private Boolean editInformation;

    @SerializedName("cancel")
    private Boolean cancel;

    @SerializedName("editParticipantInformation")
    private Boolean editParticipantInformation;

    public Boolean getMove() {
        return move;
    }

    public void setMove(Boolean move) {
        this.move = move;
    }

    public Boolean getEditFlow() {
        return editFlow;
    }

    public void setEditFlow(Boolean editFlow) {
        this.editFlow = editFlow;
    }

    public Boolean getViewSignedDocumentWithExternalFlowBeforeCompleted() {
        return viewSignedDocumentWithExternalFlowBeforeCompleted;
    }

    public void setViewSignedDocumentWithExternalFlowBeforeCompleted(Boolean viewSignedDocumentWithExternalFlowBeforeCompleted) {
        this.viewSignedDocumentWithExternalFlowBeforeCompleted = viewSignedDocumentWithExternalFlowBeforeCompleted;
    }

    public Boolean getSubmitExternalFlowFiles() {
        return submitExternalFlowFiles;
    }

    public void setSubmitExternalFlowFiles(Boolean submitExternalFlowFiles) {
        this.submitExternalFlowFiles = submitExternalFlowFiles;
    }

    public Boolean getUpdateDocumentVersion() {
        return updateDocumentVersion;
    }

    public void setUpdateDocumentVersion(Boolean updateDocumentVersion) {
        this.updateDocumentVersion = updateDocumentVersion;
    }

    public Boolean getCreateAttachments() {
        return createAttachments;
    }

    public void setCreateAttachments(Boolean createAttachments) {
        this.createAttachments = createAttachments;
    }

    public Boolean getDeleteAttachments() {
        return deleteAttachments;
    }

    public void setDeleteAttachments(Boolean deleteAttachments) {
        this.deleteAttachments = deleteAttachments;
    }

    public Boolean getViewPrivateAttachments() {
        return viewPrivateAttachments;
    }

    public void setViewPrivateAttachments(Boolean viewPrivateAttachments) {
        this.viewPrivateAttachments = viewPrivateAttachments;
    }

    public Boolean getEditAttachments() {
        return editAttachments;
    }

    public void setEditAttachments(Boolean editAttachments) {
        this.editAttachments = editAttachments;
    }

    public Boolean getCreateMeeting() {
        return createMeeting;
    }

    public void setCreateMeeting(Boolean createMeeting) {
        this.createMeeting = createMeeting;
    }

    public Boolean getDeleteMeeting() {
        return deleteMeeting;
    }

    public void setDeleteMeeting(Boolean deleteMeeting) {
        this.deleteMeeting = deleteMeeting;
    }

    public Boolean getRefuse() {
        return refuse;
    }

    public void setRefuse(Boolean refuse) {
        this.refuse = refuse;
    }

    public Boolean getEditInformation() {
        return editInformation;
    }

    public void setEditInformation(Boolean editInformation) {
        this.editInformation = editInformation;
    }

    public Boolean getCancel() {
        return cancel;
    }

    public void setCancel(Boolean cancel) {
        this.cancel = cancel;
    }

    public Boolean getEditParticipantInformation() {
        return editParticipantInformation;
    }

    public void setEditParticipantInformation(Boolean editParticipantInformation) {
        this.editParticipantInformation = editParticipantInformation;
    }
}
