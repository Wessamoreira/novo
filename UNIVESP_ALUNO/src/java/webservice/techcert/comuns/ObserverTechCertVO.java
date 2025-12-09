package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class ObserverTechCertVO {
    @SerializedName("id")
    private String id;

    @SerializedName("user")
    private UserTechCertVO user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserTechCertVO getUser() {
        return user;
    }

    public void setUser(UserTechCertVO user) {
        this.user = user;
    }
}
