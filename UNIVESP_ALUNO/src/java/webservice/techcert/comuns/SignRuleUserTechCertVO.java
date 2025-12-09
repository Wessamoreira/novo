package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class SignRuleUserTechCertVO {
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

    @SerializedName("signatureDate")
    private String signatureDate;

    @SerializedName("hasRefused")
    private Boolean hasRefused;

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

    public String getSignatureDate() {
        return signatureDate;
    }

    public void setSignatureDate(String signatureDate) {
        this.signatureDate = signatureDate;
    }

    public Boolean getHasRefused() {
        return hasRefused;
    }

    public void setHasRefused(Boolean hasRefused) {
        this.hasRefused = hasRefused;
    }
}
