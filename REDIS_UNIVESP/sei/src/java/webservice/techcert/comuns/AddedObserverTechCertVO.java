package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class AddedObserverTechCertVO {
    @SerializedName("user")
    private UserTechCertVO user;

    public UserTechCertVO getUser() {
        return user;
    }

    public void setUser(UserTechCertVO user) {
        this.user = user;
    }
}
