package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FlowActionsTechCertVO {
    @SerializedName("id")
    private String id;

    @SerializedName("notarizationStatus")
    private String notarizationStatus;

    @SerializedName("notarizationDate")
    private String notarizationDate;

    @SerializedName("notarizationDescription")
    private String notarizationDescription;

    @SerializedName("shouldNotarize")
    private Boolean shouldNotarize;

    @SerializedName("creationDate")
    private String creationDate;

    @SerializedName("pendingDate")
    private String pendingDate;

    @SerializedName("updateDate")
    private String updateDate;

    @SerializedName("user")
    private UserTechCertVO user;

    @SerializedName("numberRequiredSignatures")
    private Integer numberRequiredSignatures;

    // Se precisar mapear signRuleUsers e marks, crie classes VO específicas ou use List<Object>
    @SerializedName("signRuleUsers")
    private List<SignRuleUserTechCertVO> signRuleUsers;

    @SerializedName("marks")
    private List<Object> marks;

    @SerializedName("allowElectronicSignature")
    private Boolean allowElectronicSignature;

    @SerializedName("requireEmailAuthenticationToSignElectronically")
    private Boolean requireEmailAuthenticationToSignElectronically;

    @SerializedName("requireSmsAuthenticationToSignElectronically")
    private Boolean requireSmsAuthenticationToSignElectronically;

    @SerializedName("requireWhatsappAuthenticationToSignElectronically")
    private Boolean requireWhatsappAuthenticationToSignElectronically;

    @SerializedName("requireAuthenticatorAppToSignElectronically")
    private Boolean requireAuthenticatorAppToSignElectronically;

    @SerializedName("requireSelfieAuthenticationToSignElectronically")
    private Boolean requireSelfieAuthenticationToSignElectronically;

    @SerializedName("requireDatavalidAuthenticationToSignElectronically")
    private Boolean requireDatavalidAuthenticationToSignElectronically;

    @SerializedName("requirePixAuthenticationToSignElectronically")
    private Boolean requirePixAuthenticationToSignElectronically;

    @SerializedName("requireLivenessAuthenticationToSignElectronically")
    private Boolean requireLivenessAuthenticationToSignElectronically;

    @SerializedName("requireIdScanAuthenticationToSignElectronically")
    private Boolean requireIdScanAuthenticationToSignElectronically;

    @SerializedName("requiredCertificateTypeToSign")
    private String requiredCertificateTypeToSign;

    @SerializedName("requireCompanyCertificate")
    private Boolean requireCompanyCertificate;

    @SerializedName("requiredCompanyIdentifier")
    private String requiredCompanyIdentifier;

    @SerializedName("requiredCertificateHolderTypeToSign")
    private String requiredCertificateHolderTypeToSign;

    @SerializedName("refusalReason")
    private String refusalReason;

    @SerializedName("signatureInitialsMode")
    private String signatureInitialsMode;

    @SerializedName("isElectronic")
    private Boolean isElectronic;

    @SerializedName("allowRuleFlowToContinueIfRefused")
    private Boolean allowRuleFlowToContinueIfRefused;

    @SerializedName("type")
    private String type;

    @SerializedName("status")
    private String status;

    @SerializedName("step")
    private Integer step;

    @SerializedName("ruleName")
    private String ruleName;

    @SerializedName("title")
    private String title;

    private Boolean isSignRule;

    private SignRuleUserTechCertVO signRuleUserTechCertVOAtual;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotarizationStatus() {
        return notarizationStatus;
    }

    public void setNotarizationStatus(String notarizationStatus) {
        this.notarizationStatus = notarizationStatus;
    }

    public String getNotarizationDate() {
        return notarizationDate;
    }

    public void setNotarizationDate(String notarizationDate) {
        this.notarizationDate = notarizationDate;
    }

    public String getNotarizationDescription() {
        return notarizationDescription;
    }

    public void setNotarizationDescription(String notarizationDescription) {
        this.notarizationDescription = notarizationDescription;
    }

    public Boolean getShouldNotarize() {
        return shouldNotarize;
    }

    public void setShouldNotarize(Boolean shouldNotarize) {
        this.shouldNotarize = shouldNotarize;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getPendingDate() {
        return pendingDate;
    }

    public void setPendingDate(String pendingDate) {
        this.pendingDate = pendingDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public UserTechCertVO getUser() {
        return user;
    }

    public void setUser(UserTechCertVO user) {
        this.user = user;
    }

    public Integer getNumberRequiredSignatures() {
        return numberRequiredSignatures;
    }

    public void setNumberRequiredSignatures(Integer numberRequiredSignatures) {
        this.numberRequiredSignatures = numberRequiredSignatures;
    }

    public List<SignRuleUserTechCertVO> getSignRuleUsers() {
        return signRuleUsers;
    }

    public void setSignRuleUsers(List<SignRuleUserTechCertVO> signRuleUsers) {
        this.signRuleUsers = signRuleUsers;
    }

    public List<Object> getMarks() {
        return marks;
    }

    public void setMarks(List<Object> marks) {
        this.marks = marks;
    }

    public Boolean getAllowElectronicSignature() {
        return allowElectronicSignature;
    }

    public void setAllowElectronicSignature(Boolean allowElectronicSignature) {
        this.allowElectronicSignature = allowElectronicSignature;
    }

    public Boolean getRequireEmailAuthenticationToSignElectronically() {
        return requireEmailAuthenticationToSignElectronically;
    }

    public void setRequireEmailAuthenticationToSignElectronically(Boolean requireEmailAuthenticationToSignElectronically) {
        this.requireEmailAuthenticationToSignElectronically = requireEmailAuthenticationToSignElectronically;
    }

    public Boolean getRequireSmsAuthenticationToSignElectronically() {
        return requireSmsAuthenticationToSignElectronically;
    }

    public void setRequireSmsAuthenticationToSignElectronically(Boolean requireSmsAuthenticationToSignElectronically) {
        this.requireSmsAuthenticationToSignElectronically = requireSmsAuthenticationToSignElectronically;
    }

    public Boolean getRequireWhatsappAuthenticationToSignElectronically() {
        return requireWhatsappAuthenticationToSignElectronically;
    }

    public void setRequireWhatsappAuthenticationToSignElectronically(Boolean requireWhatsappAuthenticationToSignElectronically) {
        this.requireWhatsappAuthenticationToSignElectronically = requireWhatsappAuthenticationToSignElectronically;
    }

    public Boolean getRequireAuthenticatorAppToSignElectronically() {
        return requireAuthenticatorAppToSignElectronically;
    }

    public void setRequireAuthenticatorAppToSignElectronically(Boolean requireAuthenticatorAppToSignElectronically) {
        this.requireAuthenticatorAppToSignElectronically = requireAuthenticatorAppToSignElectronically;
    }

    public Boolean getRequireSelfieAuthenticationToSignElectronically() {
        return requireSelfieAuthenticationToSignElectronically;
    }

    public void setRequireSelfieAuthenticationToSignElectronically(Boolean requireSelfieAuthenticationToSignElectronically) {
        this.requireSelfieAuthenticationToSignElectronically = requireSelfieAuthenticationToSignElectronically;
    }

    public Boolean getRequireDatavalidAuthenticationToSignElectronically() {
        return requireDatavalidAuthenticationToSignElectronically;
    }

    public void setRequireDatavalidAuthenticationToSignElectronically(Boolean requireDatavalidAuthenticationToSignElectronically) {
        this.requireDatavalidAuthenticationToSignElectronically = requireDatavalidAuthenticationToSignElectronically;
    }

    public Boolean getRequirePixAuthenticationToSignElectronically() {
        return requirePixAuthenticationToSignElectronically;
    }

    public void setRequirePixAuthenticationToSignElectronically(Boolean requirePixAuthenticationToSignElectronically) {
        this.requirePixAuthenticationToSignElectronically = requirePixAuthenticationToSignElectronically;
    }

    public Boolean getRequireLivenessAuthenticationToSignElectronically() {
        return requireLivenessAuthenticationToSignElectronically;
    }

    public void setRequireLivenessAuthenticationToSignElectronically(Boolean requireLivenessAuthenticationToSignElectronically) {
        this.requireLivenessAuthenticationToSignElectronically = requireLivenessAuthenticationToSignElectronically;
    }

    public Boolean getRequireIdScanAuthenticationToSignElectronically() {
        return requireIdScanAuthenticationToSignElectronically;
    }

    public void setRequireIdScanAuthenticationToSignElectronically(Boolean requireIdScanAuthenticationToSignElectronically) {
        this.requireIdScanAuthenticationToSignElectronically = requireIdScanAuthenticationToSignElectronically;
    }

    public String getRequiredCertificateTypeToSign() {
        return requiredCertificateTypeToSign;
    }

    public void setRequiredCertificateTypeToSign(String requiredCertificateTypeToSign) {
        this.requiredCertificateTypeToSign = requiredCertificateTypeToSign;
    }

    public Boolean getRequireCompanyCertificate() {
        return requireCompanyCertificate;
    }

    public void setRequireCompanyCertificate(Boolean requireCompanyCertificate) {
        this.requireCompanyCertificate = requireCompanyCertificate;
    }

    public String getRequiredCompanyIdentifier() {
        return requiredCompanyIdentifier;
    }

    public void setRequiredCompanyIdentifier(String requiredCompanyIdentifier) {
        this.requiredCompanyIdentifier = requiredCompanyIdentifier;
    }

    public String getRequiredCertificateHolderTypeToSign() {
        return requiredCertificateHolderTypeToSign;
    }

    public void setRequiredCertificateHolderTypeToSign(String requiredCertificateHolderTypeToSign) {
        this.requiredCertificateHolderTypeToSign = requiredCertificateHolderTypeToSign;
    }

    public String getRefusalReason() {
        return refusalReason;
    }

    public void setRefusalReason(String refusalReason) {
        this.refusalReason = refusalReason;
    }

    public String getSignatureInitialsMode() {
        return signatureInitialsMode;
    }

    public void setSignatureInitialsMode(String signatureInitialsMode) {
        this.signatureInitialsMode = signatureInitialsMode;
    }

    public Boolean getElectronic() {
        return isElectronic;
    }

    public void setElectronic(Boolean electronic) {
        isElectronic = electronic;
    }

    public Boolean getAllowRuleFlowToContinueIfRefused() {
        return allowRuleFlowToContinueIfRefused;
    }

    public void setAllowRuleFlowToContinueIfRefused(Boolean allowRuleFlowToContinueIfRefused) {
        this.allowRuleFlowToContinueIfRefused = allowRuleFlowToContinueIfRefused;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
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

    public Boolean getIsSignRule() {
        if (isSignRule == null){
            isSignRule = Boolean.FALSE;
        }
        return isSignRule;
    }

    public void setIsSignRule(Boolean isSignRule) {
        this.isSignRule = isSignRule;
    }

    public SignRuleUserTechCertVO getSignRuleUserTechCertVOAtual() {
        return signRuleUserTechCertVOAtual;
    }

    public void setSignRuleUserTechCertVOAtual(SignRuleUserTechCertVO signRuleUserTechCertVOAtual) {
        this.signRuleUserTechCertVOAtual = signRuleUserTechCertVOAtual;
    }
}
