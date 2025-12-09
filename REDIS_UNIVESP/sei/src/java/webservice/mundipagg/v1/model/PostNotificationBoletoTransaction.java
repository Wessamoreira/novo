package webservice.mundipagg.v1.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class PostNotificationBoletoTransaction implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 18121806576484160L;
	
	@XmlElement(name="AmountInCents")
	private long amountInCents;
	@XmlElement(name="AmountPaidInCents")
	private long amountPaidInCents;
	@XmlElement(name="BoletoExpirationDate")
	private Date boletoExpirationDate;
	@XmlElement(name="NossoNumero")
	private String nossoNumero;
	@XmlElement(name="StatusChangedDate")
	private Date statusChangedDate;
	@XmlElement(name="TransactionKey")
	private String transactionKey;
	@XmlElement(name="TransactionReference")
	private String transactionReference;
	@XmlElement(name="PreviousBoletoTransactionStatus")
	private String previousBoletoTransactionStatus;
	@XmlElement(name="PreviousBoletoTransactionStatus")
	private String boletoTransactionStatus;
	public long getAmountInCents() {
		return amountInCents;
	}
	public void setAmountInCents(long amountInCents) {
		this.amountInCents = amountInCents;
	}
	public Date getBoletoExpirationDate() {
		return boletoExpirationDate;
	}
	public long getAmountPaidInCents() {
		return amountPaidInCents;
	}
	public void setAmountPaidInCents(long amountPaidInCents) {
		this.amountPaidInCents = amountPaidInCents;
	}
	public void setBoletoExpirationDate(Date boletoExpirationDate) {
		this.boletoExpirationDate = boletoExpirationDate;
	}
	public String getNossoNumero() {
		return nossoNumero;
	}
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}
	public Date getStatusChangedDate() {
		return statusChangedDate;
	}
	public void setStatusChangedDate(Date statusChangedDate) {
		this.statusChangedDate = statusChangedDate;
	}
	public String getTransactionKey() {
		return transactionKey;
	}
	public void setTransactionKey(String transactionKey) {
		this.transactionKey = transactionKey;
	}
	public String getTransactionReference() {
		return transactionReference;
	}
	public void setTransactionReference(String transactionReference) {
		this.transactionReference = transactionReference;
	}
	public String getPreviousBoletoTransactionStatus() {
		return previousBoletoTransactionStatus;
	}
	public void setPreviousBoletoTransactionStatus(
			String previousBoletoTransactionStatus) {
		this.previousBoletoTransactionStatus = previousBoletoTransactionStatus;
	}
	public String getBoletoTransactionStatus() {
		return boletoTransactionStatus;
	}
	public void setBoletoTransactionStatus(String boletoTransactionStatus) {
		this.boletoTransactionStatus = boletoTransactionStatus;
	}
	@Override
	public String toString() {
		return "PostNotificationBoletoTransaction [amountInCents="
				+ amountInCents + ", amountPaidInCents=" + amountPaidInCents
				+ ", boletoExpirationDate=" + boletoExpirationDate
				+ ", nossoNumero=" + nossoNumero + ", statusChangedDate="
				+ statusChangedDate + ", transactionKey=" + transactionKey
				+ ", transactionReference=" + transactionReference
				+ ", previousBoletoTransactionStatus="
				+ previousBoletoTransactionStatus
				+ ", boletoTransactionStatus=" + boletoTransactionStatus + "]";
	}
}
