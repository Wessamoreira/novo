package webservice.certisign.comuns;

public class DeadlineRSVO {

	private String signatureDeadlineDate;
	private String time;
	private String timezoneId;
	private Integer timeType;
	
	public DeadlineRSVO() {
		
	}
	
	public String getSignatureDeadlineDate() {
		return signatureDeadlineDate;
	}
	public void setSignatureDeadlineDate(String signatureDeadlineDate) {
		this.signatureDeadlineDate = signatureDeadlineDate;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTimezoneId() {
		return timezoneId;
	}
	public void setTimezoneId(String timezoneId) {
		this.timezoneId = timezoneId;
	}
	public Integer getTimeType() {
		return timeType;
	}
	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
	}
}
