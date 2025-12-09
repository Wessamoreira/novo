package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class EditedObserverTechCertVO {
    @SerializedName("observerId")
    private String observerId;

    @SerializedName("emailAddress")
    private String emailAddress;

    public String getObserverId() {
        return observerId;
    }

    public void setObserverId(String observerId) {
        this.observerId = observerId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
