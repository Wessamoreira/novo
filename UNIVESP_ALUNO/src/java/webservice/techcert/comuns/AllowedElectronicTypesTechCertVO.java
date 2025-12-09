package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class AllowedElectronicTypesTechCertVO {
    @SerializedName("sms")
    private Boolean sms;

    @SerializedName("whatsapp")
    private Boolean whatsapp;

    @SerializedName("otp")
    private Boolean otp;

    @SerializedName("selfie")
    private Boolean selfie;

    @SerializedName("datavalid")
    private Boolean datavalid;

    @SerializedName("pix")
    private Boolean pix;

    @SerializedName("email")
    private Boolean email;

    @SerializedName("liveness")
    private Boolean liveness;

    @SerializedName("idScan")
    private Boolean idScan;

    public Boolean getSms() {
        return sms;
    }

    public void setSms(Boolean sms) {
        this.sms = sms;
    }

    public Boolean getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(Boolean whatsapp) {
        this.whatsapp = whatsapp;
    }

    public Boolean getOtp() {
        return otp;
    }

    public void setOtp(Boolean otp) {
        this.otp = otp;
    }

    public Boolean getSelfie() {
        return selfie;
    }

    public void setSelfie(Boolean selfie) {
        this.selfie = selfie;
    }

    public Boolean getDatavalid() {
        return datavalid;
    }

    public void setDatavalid(Boolean datavalid) {
        this.datavalid = datavalid;
    }

    public Boolean getPix() {
        return pix;
    }

    public void setPix(Boolean pix) {
        this.pix = pix;
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }

    public Boolean getLiveness() {
        return liveness;
    }

    public void setLiveness(Boolean liveness) {
        this.liveness = liveness;
    }

    public Boolean getIdScan() {
        return idScan;
    }

    public void setIdScan(Boolean idScan) {
        this.idScan = idScan;
    }
}
