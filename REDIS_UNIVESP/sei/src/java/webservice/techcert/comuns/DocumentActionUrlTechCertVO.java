package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class DocumentActionUrlTechCertVO {
    @SerializedName("url")
    private String url;

    @SerializedName("embedUrl")
    private String embedUrl;

    private Boolean noPendingActionFoundForEmailAndIdentifier;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmbedUrl() {
        return embedUrl;
    }

    public void setEmbedUrl(String embedUrl) {
        this.embedUrl = embedUrl;
    }

    public Boolean getNoPendingActionFoundForEmailAndIdentifier() {
        if (noPendingActionFoundForEmailAndIdentifier == null) {
            noPendingActionFoundForEmailAndIdentifier = false;
        }
        return noPendingActionFoundForEmailAndIdentifier;
    }

    public void setNoPendingActionFoundForEmailAndIdentifier(Boolean noPendingActionFoundForEmailAndIdentifier) {
        this.noPendingActionFoundForEmailAndIdentifier = noPendingActionFoundForEmailAndIdentifier;
    }
}
