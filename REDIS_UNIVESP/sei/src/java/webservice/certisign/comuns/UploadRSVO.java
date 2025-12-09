package webservice.certisign.comuns;

public class UploadRSVO {
	
	private String id;
	private String name;
	private String uploadId;
	private String bytes;
	private String mimeType;
	private String type;

	public String getId() {		
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {		
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUploadId() {		
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getBytes() {
		return bytes;
	}

	public void setBytes(String bytes) {
		this.bytes = bytes;
	}

	public String getMimeType() {
		if (mimeType == null) {
			mimeType = "";
		}
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getType() {
		if (type == null) {
			type = "";
		}
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}	
	
	
	
	

}
