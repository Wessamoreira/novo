package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DocumentFlowTechCertVO {
    @SerializedName("addedFlowActions")
    private List<AddedFlowActionTechCertVO> addedFlowActions;

    @SerializedName("editedFlowActions")
    private List<EditedFlowActionTechCertVO> editedFlowActions;

    @SerializedName("deletedFlowActionIds")
    private List<String> deletedFlowActionIds;

    @SerializedName("addedObservers")
    private List<AddedObserverTechCertVO> addedObservers;

    @SerializedName("editedObservers")
    private List<EditedObserverTechCertVO> editedObservers;

    @SerializedName("deletedObserverIds")
    private List<String> deletedObserverIds;

    public List<AddedFlowActionTechCertVO> getAddedFlowActions() {
        return addedFlowActions;
    }

    public void setAddedFlowActions(List<AddedFlowActionTechCertVO> addedFlowActions) {
        this.addedFlowActions = addedFlowActions;
    }

    public List<EditedFlowActionTechCertVO> getEditedFlowActions() {
        return editedFlowActions;
    }

    public void setEditedFlowActions(List<EditedFlowActionTechCertVO> editedFlowActions) {
        this.editedFlowActions = editedFlowActions;
    }

    public List<String> getDeletedFlowActionIds() {
        return deletedFlowActionIds;
    }

    public void setDeletedFlowActionIds(List<String> deletedFlowActionIds) {
        this.deletedFlowActionIds = deletedFlowActionIds;
    }

    public List<AddedObserverTechCertVO> getAddedObservers() {
        return addedObservers;
    }

    public void setAddedObservers(List<AddedObserverTechCertVO> addedObservers) {
        this.addedObservers = addedObservers;
    }

    public List<EditedObserverTechCertVO> getEditedObservers() {
        return editedObservers;
    }

    public void setEditedObservers(List<EditedObserverTechCertVO> editedObservers) {
        this.editedObservers = editedObservers;
    }

    public List<String> getDeletedObserverIds() {
        return deletedObserverIds;
    }

    public void setDeletedObserverIds(List<String> deletedObserverIds) {
        this.deletedObserverIds = deletedObserverIds;
    }
}
