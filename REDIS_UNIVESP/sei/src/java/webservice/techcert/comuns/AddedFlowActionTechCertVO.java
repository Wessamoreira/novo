package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddedFlowActionTechCertVO {
    @SerializedName("user")
    private UserTechCertVO user;

    @SerializedName("signRuleUsers")
    private List<SignRuleUserTechCertVO> signRuleUsers;

    @SerializedName("type")
    private String type;

    @SerializedName("step")
    private int step;

    @SerializedName("numberRequiredSignatures")
    private Integer numberRequiredSignatures;

    @SerializedName("ruleName")
    private String ruleName;

    @SerializedName("allowRuleFlowToContinueIfRefused")
    private boolean allowRuleFlowToContinueIfRefused;

    @SerializedName("title")
    private String title;

//    @SerializedName("prePositionedMarks")
//    private List<PrePositionedMark> prePositionedMarks;

    @SerializedName("allowElectronicSignature")
    private boolean allowElectronicSignature;

    @SerializedName("requireSmsAuthenticationToSignElectronically")
    private boolean requireSmsAuthenticationToSignElectronically;

    @SerializedName("requireWhatsappAuthenticationToSignElectronically")
    private boolean requireWhatsappAuthenticationToSignElectronically;

    @SerializedName("requireAuthenticatorAppToSignElectronically")
    private boolean requireAuthenticatorAppToSignElectronically;

    @SerializedName("requireSelfieAuthenticationToSignElectronically")
    private boolean requireSelfieAuthenticationToSignElectronically;

    @SerializedName("requireDatavalidAuthenticationToSignElectronically")
    private boolean requireDatavalidAuthenticationToSignElectronically;

    @SerializedName("requirePixAuthenticationToSignElectronically")
    private boolean requirePixAuthenticationToSignElectronically;

    @SerializedName("requireLivenessAuthenticationToSignElectronically")
    private boolean requireLivenessAuthenticationToSignElectronically;

    @SerializedName("requireIdScanAuthenticationToSignElectronically")
    private boolean requireIdScanAuthenticationToSignElectronically;

    @SerializedName("disableEmailAuthenticationToSignElectronically")
    private boolean disableEmailAuthenticationToSignElectronically;

    @SerializedName("requiredCertificateTypeToSign")
    private String requiredCertificateTypeToSign;

    @SerializedName("requireCompanyCertificate")
    private boolean requireCompanyCertificate;

    @SerializedName("requiredCompanyIdentifier")
    private String requiredCompanyIdentifier;

    @SerializedName("requiredCertificateHolderTypeToSign")
    private String requiredCertificateHolderTypeToSign;

    @SerializedName("requireEmailAuthenticationToSignElectronically")
    private boolean requireEmailAuthenticationToSignElectronically;

//    @SerializedName("xadesOptions")
//    private XadesOptions xadesOptions;

    @SerializedName("signatureInitialsMode")
    private String signatureInitialsMode;

    public UserTechCertVO getUser() {
        return user;
    }

    public void setUser(UserTechCertVO user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Integer getNumberRequiredSignatures() {
        return numberRequiredSignatures;
    }

    public void setNumberRequiredSignatures(Integer numberRequiredSignatures) {
        this.numberRequiredSignatures = numberRequiredSignatures;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public boolean isAllowRuleFlowToContinueIfRefused() {
        return allowRuleFlowToContinueIfRefused;
    }

    public void setAllowRuleFlowToContinueIfRefused(boolean allowRuleFlowToContinueIfRefused) {
        this.allowRuleFlowToContinueIfRefused = allowRuleFlowToContinueIfRefused;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAllowElectronicSignature() {
        return allowElectronicSignature;
    }

    public void setAllowElectronicSignature(boolean allowElectronicSignature) {
        this.allowElectronicSignature = allowElectronicSignature;
    }

    public boolean isRequireSmsAuthenticationToSignElectronically() {
        return requireSmsAuthenticationToSignElectronically;
    }

    public void setRequireSmsAuthenticationToSignElectronically(boolean requireSmsAuthenticationToSignElectronically) {
        this.requireSmsAuthenticationToSignElectronically = requireSmsAuthenticationToSignElectronically;
    }

    public boolean isRequireWhatsappAuthenticationToSignElectronically() {
        return requireWhatsappAuthenticationToSignElectronically;
    }

    public void setRequireWhatsappAuthenticationToSignElectronically(boolean requireWhatsappAuthenticationToSignElectronically) {
        this.requireWhatsappAuthenticationToSignElectronically = requireWhatsappAuthenticationToSignElectronically;
    }

    public boolean isRequireAuthenticatorAppToSignElectronically() {
        return requireAuthenticatorAppToSignElectronically;
    }

    public void setRequireAuthenticatorAppToSignElectronically(boolean requireAuthenticatorAppToSignElectronically) {
        this.requireAuthenticatorAppToSignElectronically = requireAuthenticatorAppToSignElectronically;
    }

    public boolean isRequireSelfieAuthenticationToSignElectronically() {
        return requireSelfieAuthenticationToSignElectronically;
    }

    public void setRequireSelfieAuthenticationToSignElectronically(boolean requireSelfieAuthenticationToSignElectronically) {
        this.requireSelfieAuthenticationToSignElectronically = requireSelfieAuthenticationToSignElectronically;
    }

    public boolean isRequireDatavalidAuthenticationToSignElectronically() {
        return requireDatavalidAuthenticationToSignElectronically;
    }

    public void setRequireDatavalidAuthenticationToSignElectronically(boolean requireDatavalidAuthenticationToSignElectronically) {
        this.requireDatavalidAuthenticationToSignElectronically = requireDatavalidAuthenticationToSignElectronically;
    }

    public boolean isRequirePixAuthenticationToSignElectronically() {
        return requirePixAuthenticationToSignElectronically;
    }

    public void setRequirePixAuthenticationToSignElectronically(boolean requirePixAuthenticationToSignElectronically) {
        this.requirePixAuthenticationToSignElectronically = requirePixAuthenticationToSignElectronically;
    }

    public boolean isRequireLivenessAuthenticationToSignElectronically() {
        return requireLivenessAuthenticationToSignElectronically;
    }

    public void setRequireLivenessAuthenticationToSignElectronically(boolean requireLivenessAuthenticationToSignElectronically) {
        this.requireLivenessAuthenticationToSignElectronically = requireLivenessAuthenticationToSignElectronically;
    }

    public boolean isRequireIdScanAuthenticationToSignElectronically() {
        return requireIdScanAuthenticationToSignElectronically;
    }

    public void setRequireIdScanAuthenticationToSignElectronically(boolean requireIdScanAuthenticationToSignElectronically) {
        this.requireIdScanAuthenticationToSignElectronically = requireIdScanAuthenticationToSignElectronically;
    }

    public boolean isDisableEmailAuthenticationToSignElectronically() {
        return disableEmailAuthenticationToSignElectronically;
    }

    public void setDisableEmailAuthenticationToSignElectronically(boolean disableEmailAuthenticationToSignElectronically) {
        this.disableEmailAuthenticationToSignElectronically = disableEmailAuthenticationToSignElectronically;
    }

    public String getRequiredCertificateTypeToSign() {
        return requiredCertificateTypeToSign;
    }

    public void setRequiredCertificateTypeToSign(String requiredCertificateTypeToSign) {
        this.requiredCertificateTypeToSign = requiredCertificateTypeToSign;
    }

    public boolean isRequireCompanyCertificate() {
        return requireCompanyCertificate;
    }

    public void setRequireCompanyCertificate(boolean requireCompanyCertificate) {
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

    public String getSignatureInitialsMode() {
        return signatureInitialsMode;
    }

    public void setSignatureInitialsMode(String signatureInitialsMode) {
        this.signatureInitialsMode = signatureInitialsMode;
    }

    public List<SignRuleUserTechCertVO> getSignRuleUsers() {
        return signRuleUsers;
    }

    public void setSignRuleUsers(List<SignRuleUserTechCertVO> signRuleUsers) {
        this.signRuleUsers = signRuleUsers;
    }

    public boolean isRequireEmailAuthenticationToSignElectronically() {
        return requireEmailAuthenticationToSignElectronically;
    }

    public void setRequireEmailAuthenticationToSignElectronically(boolean requireEmailAuthenticationToSignElectronically) {
        this.requireEmailAuthenticationToSignElectronically = requireEmailAuthenticationToSignElectronically;
    }
}
