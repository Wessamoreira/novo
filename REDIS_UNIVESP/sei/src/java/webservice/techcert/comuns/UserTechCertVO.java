package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class UserTechCertVO {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("identifier")
    private String identifier;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("createdBySubscriptionId")
    private String createdBySubscriptionId;

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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreatedBySubscriptionId() {
        return createdBySubscriptionId;
    }

    public void setCreatedBySubscriptionId(String createdBySubscriptionId) {
        this.createdBySubscriptionId = createdBySubscriptionId;
    }
}
