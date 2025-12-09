/**
 * CreditCardTransaction.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class CreditCardTransaction  implements java.io.Serializable {
    private java.lang.Long amountInCents;

    private java.lang.Integer captureDelayInMinutes;

    private CreditCardBrandEnum creditCardBrandEnum;

    private java.lang.String creditCardNumber;

    private CreditCardOperationEnum creditCardOperationEnum;

    private java.lang.Integer expMonth;

    private java.lang.Integer expYear;

    private java.lang.String holderName;

    private java.lang.Long iataAmountInCents;

    private java.lang.Integer installmentCount;

    private java.lang.String instantBuyKey;

    private java.lang.Integer paymentMethodCode;

    private java.lang.String securityCode;

    private java.lang.String thirdPartyMerchantKey;

    private java.lang.String transactionReference;

    private Recurrency recurrency;

    public CreditCardTransaction() {
    }

    public CreditCardTransaction(
           java.lang.Long amountInCents,
           java.lang.Integer captureDelayInMinutes,
           CreditCardBrandEnum creditCardBrandEnum,
           java.lang.String creditCardNumber,
           CreditCardOperationEnum creditCardOperationEnum,
           java.lang.Integer expMonth,
           java.lang.Integer expYear,
           java.lang.String holderName,
           java.lang.Long iataAmountInCents,
           java.lang.Integer installmentCount,
           java.lang.String instantBuyKey,
           java.lang.Integer paymentMethodCode,
           java.lang.String securityCode,
           java.lang.String thirdPartyMerchantKey,
           java.lang.String transactionReference,
           Recurrency recurrency) {
           this.amountInCents = amountInCents;
           this.captureDelayInMinutes = captureDelayInMinutes;
           this.creditCardBrandEnum = creditCardBrandEnum;
           this.creditCardNumber = creditCardNumber;
           this.creditCardOperationEnum = creditCardOperationEnum;
           this.expMonth = expMonth;
           this.expYear = expYear;
           this.holderName = holderName;
           this.iataAmountInCents = iataAmountInCents;
           this.installmentCount = installmentCount;
           this.instantBuyKey = instantBuyKey;
           this.paymentMethodCode = paymentMethodCode;
           this.securityCode = securityCode;
           this.thirdPartyMerchantKey = thirdPartyMerchantKey;
           this.transactionReference = transactionReference;
           this.recurrency = recurrency;
    }


    /**
     * Gets the amountInCents value for this CreditCardTransaction.
     * 
     * @return amountInCents
     */
    public java.lang.Long getAmountInCents() {
        return amountInCents;
    }


    /**
     * Sets the amountInCents value for this CreditCardTransaction.
     * 
     * @param amountInCents
     */
    public void setAmountInCents(java.lang.Long amountInCents) {
        this.amountInCents = amountInCents;
    }


    /**
     * Gets the captureDelayInMinutes value for this CreditCardTransaction.
     * 
     * @return captureDelayInMinutes
     */
    public java.lang.Integer getCaptureDelayInMinutes() {
        return captureDelayInMinutes;
    }


    /**
     * Sets the captureDelayInMinutes value for this CreditCardTransaction.
     * 
     * @param captureDelayInMinutes
     */
    public void setCaptureDelayInMinutes(java.lang.Integer captureDelayInMinutes) {
        this.captureDelayInMinutes = captureDelayInMinutes;
    }


    /**
     * Gets the creditCardBrandEnum value for this CreditCardTransaction.
     * 
     * @return creditCardBrandEnum
     */
    public CreditCardBrandEnum getCreditCardBrandEnum() {
        return creditCardBrandEnum;
    }


    /**
     * Sets the creditCardBrandEnum value for this CreditCardTransaction.
     * 
     * @param creditCardBrandEnum
     */
    public void setCreditCardBrandEnum(CreditCardBrandEnum creditCardBrandEnum) {
        this.creditCardBrandEnum = creditCardBrandEnum;
    }


    /**
     * Gets the creditCardNumber value for this CreditCardTransaction.
     * 
     * @return creditCardNumber
     */
    public java.lang.String getCreditCardNumber() {
        return creditCardNumber;
    }


    /**
     * Sets the creditCardNumber value for this CreditCardTransaction.
     * 
     * @param creditCardNumber
     */
    public void setCreditCardNumber(java.lang.String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }


    /**
     * Gets the creditCardOperationEnum value for this CreditCardTransaction.
     * 
     * @return creditCardOperationEnum
     */
    public CreditCardOperationEnum getCreditCardOperationEnum() {
        return creditCardOperationEnum;
    }


    /**
     * Sets the creditCardOperationEnum value for this CreditCardTransaction.
     * 
     * @param creditCardOperationEnum
     */
    public void setCreditCardOperationEnum(CreditCardOperationEnum creditCardOperationEnum) {
        this.creditCardOperationEnum = creditCardOperationEnum;
    }


    /**
     * Gets the expMonth value for this CreditCardTransaction.
     * 
     * @return expMonth
     */
    public java.lang.Integer getExpMonth() {
        return expMonth;
    }


    /**
     * Sets the expMonth value for this CreditCardTransaction.
     * 
     * @param expMonth
     */
    public void setExpMonth(java.lang.Integer expMonth) {
        this.expMonth = expMonth;
    }


    /**
     * Gets the expYear value for this CreditCardTransaction.
     * 
     * @return expYear
     */
    public java.lang.Integer getExpYear() {
        return expYear;
    }


    /**
     * Sets the expYear value for this CreditCardTransaction.
     * 
     * @param expYear
     */
    public void setExpYear(java.lang.Integer expYear) {
        this.expYear = expYear;
    }


    /**
     * Gets the holderName value for this CreditCardTransaction.
     * 
     * @return holderName
     */
    public java.lang.String getHolderName() {
        return holderName;
    }


    /**
     * Sets the holderName value for this CreditCardTransaction.
     * 
     * @param holderName
     */
    public void setHolderName(java.lang.String holderName) {
        this.holderName = holderName;
    }


    /**
     * Gets the iataAmountInCents value for this CreditCardTransaction.
     * 
     * @return iataAmountInCents
     */
    public java.lang.Long getIataAmountInCents() {
        return iataAmountInCents;
    }


    /**
     * Sets the iataAmountInCents value for this CreditCardTransaction.
     * 
     * @param iataAmountInCents
     */
    public void setIataAmountInCents(java.lang.Long iataAmountInCents) {
        this.iataAmountInCents = iataAmountInCents;
    }


    /**
     * Gets the installmentCount value for this CreditCardTransaction.
     * 
     * @return installmentCount
     */
    public java.lang.Integer getInstallmentCount() {
        return installmentCount;
    }


    /**
     * Sets the installmentCount value for this CreditCardTransaction.
     * 
     * @param installmentCount
     */
    public void setInstallmentCount(java.lang.Integer installmentCount) {
        this.installmentCount = installmentCount;
    }


    /**
     * Gets the instantBuyKey value for this CreditCardTransaction.
     * 
     * @return instantBuyKey
     */
    public java.lang.String getInstantBuyKey() {
        return instantBuyKey;
    }


    /**
     * Sets the instantBuyKey value for this CreditCardTransaction.
     * 
     * @param instantBuyKey
     */
    public void setInstantBuyKey(java.lang.String instantBuyKey) {
        this.instantBuyKey = instantBuyKey;
    }


    /**
     * Gets the paymentMethodCode value for this CreditCardTransaction.
     * 
     * @return paymentMethodCode
     */
    public java.lang.Integer getPaymentMethodCode() {
        return paymentMethodCode;
    }


    /**
     * Sets the paymentMethodCode value for this CreditCardTransaction.
     * 
     * @param paymentMethodCode
     */
    public void setPaymentMethodCode(java.lang.Integer paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }


    /**
     * Gets the securityCode value for this CreditCardTransaction.
     * 
     * @return securityCode
     */
    public java.lang.String getSecurityCode() {
        return securityCode;
    }


    /**
     * Sets the securityCode value for this CreditCardTransaction.
     * 
     * @param securityCode
     */
    public void setSecurityCode(java.lang.String securityCode) {
        this.securityCode = securityCode;
    }


    /**
     * Gets the thirdPartyMerchantKey value for this CreditCardTransaction.
     * 
     * @return thirdPartyMerchantKey
     */
    public java.lang.String getThirdPartyMerchantKey() {
        return thirdPartyMerchantKey;
    }


    /**
     * Sets the thirdPartyMerchantKey value for this CreditCardTransaction.
     * 
     * @param thirdPartyMerchantKey
     */
    public void setThirdPartyMerchantKey(java.lang.String thirdPartyMerchantKey) {
        this.thirdPartyMerchantKey = thirdPartyMerchantKey;
    }


    /**
     * Gets the transactionReference value for this CreditCardTransaction.
     * 
     * @return transactionReference
     */
    public java.lang.String getTransactionReference() {
        return transactionReference;
    }


    /**
     * Sets the transactionReference value for this CreditCardTransaction.
     * 
     * @param transactionReference
     */
    public void setTransactionReference(java.lang.String transactionReference) {
        this.transactionReference = transactionReference;
    }


    /**
     * Gets the recurrency value for this CreditCardTransaction.
     * 
     * @return recurrency
     */
    public Recurrency getRecurrency() {
        return recurrency;
    }


    /**
     * Sets the recurrency value for this CreditCardTransaction.
     * 
     * @param recurrency
     */
    public void setRecurrency(Recurrency recurrency) {
        this.recurrency = recurrency;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreditCardTransaction)) return false;
        CreditCardTransaction other = (CreditCardTransaction) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.amountInCents==null && other.getAmountInCents()==null) || 
             (this.amountInCents!=null &&
              this.amountInCents.equals(other.getAmountInCents()))) &&
            ((this.captureDelayInMinutes==null && other.getCaptureDelayInMinutes()==null) || 
             (this.captureDelayInMinutes!=null &&
              this.captureDelayInMinutes.equals(other.getCaptureDelayInMinutes()))) &&
            ((this.creditCardBrandEnum==null && other.getCreditCardBrandEnum()==null) || 
             (this.creditCardBrandEnum!=null &&
              this.creditCardBrandEnum.equals(other.getCreditCardBrandEnum()))) &&
            ((this.creditCardNumber==null && other.getCreditCardNumber()==null) || 
             (this.creditCardNumber!=null &&
              this.creditCardNumber.equals(other.getCreditCardNumber()))) &&
            ((this.creditCardOperationEnum==null && other.getCreditCardOperationEnum()==null) || 
             (this.creditCardOperationEnum!=null &&
              this.creditCardOperationEnum.equals(other.getCreditCardOperationEnum()))) &&
            ((this.expMonth==null && other.getExpMonth()==null) || 
             (this.expMonth!=null &&
              this.expMonth.equals(other.getExpMonth()))) &&
            ((this.expYear==null && other.getExpYear()==null) || 
             (this.expYear!=null &&
              this.expYear.equals(other.getExpYear()))) &&
            ((this.holderName==null && other.getHolderName()==null) || 
             (this.holderName!=null &&
              this.holderName.equals(other.getHolderName()))) &&
            ((this.iataAmountInCents==null && other.getIataAmountInCents()==null) || 
             (this.iataAmountInCents!=null &&
              this.iataAmountInCents.equals(other.getIataAmountInCents()))) &&
            ((this.installmentCount==null && other.getInstallmentCount()==null) || 
             (this.installmentCount!=null &&
              this.installmentCount.equals(other.getInstallmentCount()))) &&
            ((this.instantBuyKey==null && other.getInstantBuyKey()==null) || 
             (this.instantBuyKey!=null &&
              this.instantBuyKey.equals(other.getInstantBuyKey()))) &&
            ((this.paymentMethodCode==null && other.getPaymentMethodCode()==null) || 
             (this.paymentMethodCode!=null &&
              this.paymentMethodCode.equals(other.getPaymentMethodCode()))) &&
            ((this.securityCode==null && other.getSecurityCode()==null) || 
             (this.securityCode!=null &&
              this.securityCode.equals(other.getSecurityCode()))) &&
            ((this.thirdPartyMerchantKey==null && other.getThirdPartyMerchantKey()==null) || 
             (this.thirdPartyMerchantKey!=null &&
              this.thirdPartyMerchantKey.equals(other.getThirdPartyMerchantKey()))) &&
            ((this.transactionReference==null && other.getTransactionReference()==null) || 
             (this.transactionReference!=null &&
              this.transactionReference.equals(other.getTransactionReference()))) &&
            ((this.recurrency==null && other.getRecurrency()==null) || 
             (this.recurrency!=null &&
              this.recurrency.equals(other.getRecurrency())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getAmountInCents() != null) {
            _hashCode += getAmountInCents().hashCode();
        }
        if (getCaptureDelayInMinutes() != null) {
            _hashCode += getCaptureDelayInMinutes().hashCode();
        }
        if (getCreditCardBrandEnum() != null) {
            _hashCode += getCreditCardBrandEnum().hashCode();
        }
        if (getCreditCardNumber() != null) {
            _hashCode += getCreditCardNumber().hashCode();
        }
        if (getCreditCardOperationEnum() != null) {
            _hashCode += getCreditCardOperationEnum().hashCode();
        }
        if (getExpMonth() != null) {
            _hashCode += getExpMonth().hashCode();
        }
        if (getExpYear() != null) {
            _hashCode += getExpYear().hashCode();
        }
        if (getHolderName() != null) {
            _hashCode += getHolderName().hashCode();
        }
        if (getIataAmountInCents() != null) {
            _hashCode += getIataAmountInCents().hashCode();
        }
        if (getInstallmentCount() != null) {
            _hashCode += getInstallmentCount().hashCode();
        }
        if (getInstantBuyKey() != null) {
            _hashCode += getInstantBuyKey().hashCode();
        }
        if (getPaymentMethodCode() != null) {
            _hashCode += getPaymentMethodCode().hashCode();
        }
        if (getSecurityCode() != null) {
            _hashCode += getSecurityCode().hashCode();
        }
        if (getThirdPartyMerchantKey() != null) {
            _hashCode += getThirdPartyMerchantKey().hashCode();
        }
        if (getTransactionReference() != null) {
            _hashCode += getTransactionReference().hashCode();
        }
        if (getRecurrency() != null) {
            _hashCode += getRecurrency().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreditCardTransaction.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardTransaction"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amountInCents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "AmountInCents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("captureDelayInMinutes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CaptureDelayInMinutes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creditCardBrandEnum");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardBrandEnum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardBrandEnum"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creditCardNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creditCardOperationEnum");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardOperationEnum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardOperationEnum"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expMonth");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ExpMonth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expYear");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ExpYear"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("holderName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "HolderName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("iataAmountInCents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "IataAmountInCents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("installmentCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "InstallmentCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instantBuyKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "InstantBuyKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("paymentMethodCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "PaymentMethodCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("securityCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "SecurityCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("thirdPartyMerchantKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ThirdPartyMerchantKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "TransactionReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recurrency");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "Recurrency"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "Recurrency"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
