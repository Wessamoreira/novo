package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ValidationResultsTechCertVO {
    @SerializedName("passedChecks")
    private List<ValidationCheckTechCertVO> passedChecks;

    @SerializedName("errors")
    private List<ValidationCheckTechCertVO> errors;

    @SerializedName("warnings")
    private List<ValidationCheckTechCertVO> warnings;

    @SerializedName("isValid")
    private Boolean isValid;

    public List<ValidationCheckTechCertVO> getPassedChecks() {
        return passedChecks;
    }

    public void setPassedChecks(List<ValidationCheckTechCertVO> passedChecks) {
        this.passedChecks = passedChecks;
    }

    public List<ValidationCheckTechCertVO> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationCheckTechCertVO> errors) {
        this.errors = errors;
    }

    public List<ValidationCheckTechCertVO> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<ValidationCheckTechCertVO> warnings) {
        this.warnings = warnings;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
    }
}
