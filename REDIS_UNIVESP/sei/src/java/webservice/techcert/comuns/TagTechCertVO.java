package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class TagTechCertVO {
    @SerializedName("id")
    private String id;

    @SerializedName("label")
    private String label;

    @SerializedName("value")
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
