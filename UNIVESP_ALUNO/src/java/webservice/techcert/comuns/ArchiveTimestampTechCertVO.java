package webservice.techcert.comuns;

import com.google.gson.annotations.SerializedName;

public class ArchiveTimestampTechCertVO {
    @SerializedName("timestamp")
    private String timestamp;

    @SerializedName("fileTicket")
    private String fileTicket;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFileTicket() {
        return fileTicket;
    }

    public void setFileTicket(String fileTicket) {
        this.fileTicket = fileTicket;
    }
}
