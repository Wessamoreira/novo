package webservice.mundipagg.v1.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "StatusNotification")
public class StatusNotification implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4713315853878866807L;
	
	 @XmlElement(name="AmountInCents")
	private long amountInCents;
	 
	 @XmlElement(name="AmountPaidInCents")
	private long amountPaidInCents;
	
	 @XmlElement(name="BoletoTransaction")
	private PostNotificationBoletoTransaction boletoTransaction;
	
	 @XmlElement(name="CreditCardTransaction")
	private PostNotificationCreditCardTransaction creditCardTransaction;

	 @XmlElement(name="MerchantKey")
	private String merchantkey;

	 @XmlElement(name="OrderKey")
	private String orderKey;
	
	 @XmlElement(name="OrderReference")
	private String orderReference;

	 @XmlElement(name="OrderStatus")
	private String orderStatus;

	 
	public long getAmountInCents() {
		return amountInCents;
	}

	public void setAmountInCents(long amountInCents) {
		this.amountInCents = amountInCents;
	}

	public long getAmountPaidInCents() {
		return this.amountPaidInCents;
	}

	public void setAmountPaidInCents(long amountPaidInCents) {
		this.amountPaidInCents = amountPaidInCents;
	}

	public PostNotificationBoletoTransaction getBoletoTransaction() {
		return boletoTransaction;
	}

	public void setBoletoTransaction(
			PostNotificationBoletoTransaction boletoTransaction) {
		this.boletoTransaction = boletoTransaction;
	}

	public PostNotificationCreditCardTransaction getCreditCardTransaction() {
		return creditCardTransaction;
	}

	public void setCreditCardTransaction(
			PostNotificationCreditCardTransaction creditCardTransaction) {
		this.creditCardTransaction = creditCardTransaction;
	}

	public String getMerchantkey() {
		return merchantkey;
	}

	public void setMerchantkey(String merchantkey) {
		this.merchantkey = merchantkey;
	}

	public String getOrderKey() {
		return orderKey;
	}

	public void setOrderKey(String orderKey) {
		this.orderKey = orderKey;
	}

	public String getOrderReference() {
		return orderReference;
	}

	public void setOrderReference(String orderReference) {
		this.orderReference = orderReference;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public String toString() {
		return "StatusNotification [amountInCents=" + amountInCents
				+ ", amountPaidInCents=" + amountPaidInCents
				+ ", boletoTransaction=" + boletoTransaction
				+ ", creditCardTransaction=" + creditCardTransaction
				+ ", merchantkey=" + merchantkey + ", orderKey=" + orderKey
				+ ", orderReference=" + orderReference + ", orderStatus="
				+ orderStatus + "]";
	}	
	

}
