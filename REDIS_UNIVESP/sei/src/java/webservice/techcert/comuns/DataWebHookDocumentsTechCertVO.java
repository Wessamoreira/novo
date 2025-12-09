package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataWebHookDocumentsTechCertVO {

    @SerializedName("documents")
    private List<DocumentsTechCertVO> documents;

    public List<DocumentsTechCertVO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentsTechCertVO> documents) {
        this.documents = documents;
    }
}
