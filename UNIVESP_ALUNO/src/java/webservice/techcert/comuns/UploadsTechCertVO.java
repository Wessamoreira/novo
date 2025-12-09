package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class UploadsTechCertVO {

    @SerializedName("id")
    private String id;

    @SerializedName("size")
    private String size;

    @SerializedName("digest")
    private String digest;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }
}
