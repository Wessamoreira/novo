package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class SecurityContextTechCertVO {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("englishName")
    private String englishName;

    @SerializedName("portugueseName")
    private String portugueseName;

    @SerializedName("spanishName")
    private String spanishName;

    @SerializedName("allowDigitalSignature")
    private Boolean allowDigitalSignature;

    @SerializedName("allowedElectronicTypes")
    private AllowedElectronicTypesTechCertVO allowedElectronicTypes;

    @SerializedName("requireLivenessOnSelfieAuthentication")
    private Boolean requireLivenessOnSelfieAuthentication;

    @SerializedName("requireLivenessOnDatavalidAuthentication")
    private Boolean requireLivenessOnDatavalidAuthentication;

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

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getPortugueseName() {
        return portugueseName;
    }

    public void setPortugueseName(String portugueseName) {
        this.portugueseName = portugueseName;
    }

    public String getSpanishName() {
        return spanishName;
    }

    public void setSpanishName(String spanishName) {
        this.spanishName = spanishName;
    }

    public Boolean getAllowDigitalSignature() {
        return allowDigitalSignature;
    }

    public void setAllowDigitalSignature(Boolean allowDigitalSignature) {
        this.allowDigitalSignature = allowDigitalSignature;
    }

    public AllowedElectronicTypesTechCertVO getAllowedElectronicTypes() {
        return allowedElectronicTypes;
    }

    public void setAllowedElectronicTypes(AllowedElectronicTypesTechCertVO allowedElectronicTypes) {
        this.allowedElectronicTypes = allowedElectronicTypes;
    }

    public Boolean getRequireLivenessOnSelfieAuthentication() {
        return requireLivenessOnSelfieAuthentication;
    }

    public void setRequireLivenessOnSelfieAuthentication(Boolean requireLivenessOnSelfieAuthentication) {
        this.requireLivenessOnSelfieAuthentication = requireLivenessOnSelfieAuthentication;
    }

    public Boolean getRequireLivenessOnDatavalidAuthentication() {
        return requireLivenessOnDatavalidAuthentication;
    }

    public void setRequireLivenessOnDatavalidAuthentication(Boolean requireLivenessOnDatavalidAuthentication) {
        this.requireLivenessOnDatavalidAuthentication = requireLivenessOnDatavalidAuthentication;
    }
}
