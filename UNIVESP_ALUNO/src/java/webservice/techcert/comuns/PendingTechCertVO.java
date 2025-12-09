package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class PendingTechCertVO {
    @SerializedName("signerId")
    private String signerId;

    @SerializedName("signRuleId")
    private String signRuleId;

    @SerializedName("approverId")
    private String approverId;


    public String getSignerId() {
        return signerId;
    }

    public void setSignerId(String signerId) {
        this.signerId = signerId;
    }

    public String getSignRuleId() {
        return signRuleId;
    }

    public void setSignRuleId(String signRuleId) {
        this.signRuleId = signRuleId;
    }

    public String getApproverId() {
        return approverId;
    }

    public void setApproverId(String approverId) {
        this.approverId = approverId;
    }
}
