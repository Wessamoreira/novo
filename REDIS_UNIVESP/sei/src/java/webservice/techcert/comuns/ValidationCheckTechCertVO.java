package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class ValidationCheckTechCertVO {
    @SerializedName("type")
    private String type;

    @SerializedName("message")
    private String message;

    @SerializedName("detail")
    private String detail;

    @SerializedName("innerValidationResults")
    private ValidationResultsTechCertVO innerValidationResults;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ValidationResultsTechCertVO getInnerValidationResults() {
        return innerValidationResults;
    }

    public void setInnerValidationResults(ValidationResultsTechCertVO innerValidationResults) {
        this.innerValidationResults = innerValidationResults;
    }
}
