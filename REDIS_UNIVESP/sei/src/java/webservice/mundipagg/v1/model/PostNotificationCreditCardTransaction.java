package webservice.mundipagg.v1.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class PostNotificationCreditCardTransaction implements Serializable {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = -7297044434810668713L;
	
	 @XmlElement(name="Acquirer")
	private String acquirer;
	 @XmlElement(name="AmountInCents")
	private long amountInCents;
	 @XmlElement(name="AuthorizedAmountInCents")
	private long authorizedAmountInCents;
	 @XmlElement(name="CapturedAmountInCents")
	private long capturedAmountInCents;
	 @XmlElement(name="CreditCardBrand")
	private String creditCardBrand;
	 @XmlElement(name="RefundedAmountInCents")
	private long refundedAmountInCents;
	 @XmlElement(name="StatusChangedDate")
	private Date statusChangedDate;
	 @XmlElement(name="TransactionIdentifier")
	private String transactionIdentifier;
	 @XmlElement(name="TransactionKey")
	private String transactionKey;
	 @XmlElement(name="TransactionReference")
	private String transactionReference;
	 @XmlElement(name="UniqueSequentialNumber")
	private String uniqueSequentialNumber;
	 @XmlElement(name="VoidedAmountInCents")
	private long voidedAmountInCents;
	 @XmlElement(name="PreviousCreditCardTransactionStatus")
	private String previousCreditCardTransactionStatus;
	 @XmlElement(name="CreditCardTransactionStatus")
	private String creditCardTransactionStatus;
	 
	public String getAcquirer() {
		return acquirer;
	}
	public void setAcquirer(String acquirer) {
		this.acquirer = acquirer;
	}
	public long getAmountInCents() {
		return amountInCents;
	}
	public void setAmountInCents(long amountInCents) {
		this.amountInCents = amountInCents;
	}
	public long getAuthorizedAmountInCents() {
		return authorizedAmountInCents;
	}
	public void setAuthorizedAmountInCents(long authorizedAmountInCents) {
		this.authorizedAmountInCents = authorizedAmountInCents;
	}
	public long getCapturedAmountInCents() {
		return capturedAmountInCents;
	}
	public void setCapturedAmountInCents(long capturedAmountInCents) {
		this.capturedAmountInCents = capturedAmountInCents;
	}
	public String getCreditCardBrand() {
		return creditCardBrand;
	}
	public void setCreditCardBrand(String creditCardBrand) {
		this.creditCardBrand = creditCardBrand;
	}
	public long getRefundedAmountInCents() {
		return refundedAmountInCents;
	}
	public void setRefundedAmountInCents(long refundedAmountInCents) {
		this.refundedAmountInCents = refundedAmountInCents;
	}
	public Date getStatusChangedDate() {
		return statusChangedDate;
	}
	public void setStatusChangedDate(Date statusChangedDate) {
		this.statusChangedDate = statusChangedDate;
	}
	public String getTransactionIdentifier() {
		return transactionIdentifier;
	}
	public void setTransactionIdentifier(String transactionIdentifier) {
		this.transactionIdentifier = transactionIdentifier;
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
	public String getUniqueSequentialNumber() {
		return uniqueSequentialNumber;
	}
	public void setUniqueSequentialNumber(String uniqueSequentialNumber) {
		this.uniqueSequentialNumber = uniqueSequentialNumber;
	}
	public long getVoidedAmountInCents() {
		return voidedAmountInCents;
	}
	public void setVoidedAmountInCents(long voidedAmountInCents) {
		this.voidedAmountInCents = voidedAmountInCents;
	}
	public String getPreviousCreditCardTransactionStatus() {
		return previousCreditCardTransactionStatus;
	}
	public void setPreviousCreditCardTransactionStatus(
			String previousCreditCardTransactionStatus) {
		this.previousCreditCardTransactionStatus = previousCreditCardTransactionStatus;
	}
	public String getCreditCardTransactionStatus() {
		return creditCardTransactionStatus;
	}
	public void setCreditCardTransactionStatus(String creditCardTransactionStatus) {
		this.creditCardTransactionStatus = creditCardTransactionStatus;
	}
	@Override
	public String toString() {
		return "PostNotificationCreditCardTransaction [acquirer=" + acquirer
				+ ", amountInCents=" + amountInCents
				+ ", authorizedAmountInCents=" + authorizedAmountInCents
				+ ", capturedAmountInCents=" + capturedAmountInCents
				+ ", creditCardBrand=" + creditCardBrand
				+ ", refundedAmountInCents=" + refundedAmountInCents
				+ ", statusChangedDate=" + statusChangedDate
				+ ", transactionIdentifier=" + transactionIdentifier
				+ ", transactionKey=" + transactionKey
				+ ", transactionReference=" + transactionReference
				+ ", uniqueSequentialNumber=" + uniqueSequentialNumber
				+ ", voidedAmountInCents=" + voidedAmountInCents
				+ ", previousCreditCardTransactionStatus="
				+ previousCreditCardTransactionStatus
				+ ", creditCardTransactionStatus="
				+ creditCardTransactionStatus + "]";
	}
}
