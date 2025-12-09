package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttributeCertificateTechCertVO {

    @SerializedName("certificateData")
    private String certificateData;

    @SerializedName("issuer")
    private String issuer;

    @SerializedName("serialNumber")
    private String serialNumber;

    @SerializedName("notBefore")
    private String notBefore;

    @SerializedName("notAfter")
    private String notAfter;

    @SerializedName("attributes")
    private List<AttributeTechCertVO> attributes;

    public String getCertificateData() {
        return certificateData;
    }

    public void setCertificateData(String certificateData) {
        this.certificateData = certificateData;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(String notBefore) {
        this.notBefore = notBefore;
    }

    public String getNotAfter() {
        return notAfter;
    }

    public void setNotAfter(String notAfter) {
        this.notAfter = notAfter;
    }

    public List<AttributeTechCertVO> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributeTechCertVO> attributes) {
        this.attributes = attributes;
    }
}
