package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DocumentsCallBackTechCertVO {

    @SerializedName("type")
    private String type;

    @SerializedName("data")
    private DataWebHookDocumentsTechCertVO data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataWebHookDocumentsTechCertVO getData() {
        return data;
    }

    public void setData(DataWebHookDocumentsTechCertVO data) {
        this.data = data;
    }
}
