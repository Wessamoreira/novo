package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class EditedFlowActionTechCertVO {
    @SerializedName("flowActionId")
    private String flowActionId;

    @SerializedName("step")
    private int step;

    @SerializedName("participantEmailAddress")
    private String participantEmailAddress;

    @SerializedName("ruleName")
    private String ruleName;

//    @SerializedName("signRuleUsers")
//    private List<EditedSignRuleUser> signRuleUsers;

    @SerializedName("title")
    private String title;

//    @SerializedName("prePositionedMarks")
//    private List<PrePositionedMark> prePositionedMarks;

    @SerializedName("signatureInitialsMode")
    private String signatureInitialsMode;


    public String getFlowActionId() {
        return flowActionId;
    }

    public void setFlowActionId(String flowActionId) {
        this.flowActionId = flowActionId;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getParticipantEmailAddress() {
        return participantEmailAddress;
    }

    public void setParticipantEmailAddress(String participantEmailAddress) {
        this.participantEmailAddress = participantEmailAddress;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSignatureInitialsMode() {
        return signatureInitialsMode;
    }

    public void setSignatureInitialsMode(String signatureInitialsMode) {
        this.signatureInitialsMode = signatureInitialsMode;
    }
}
