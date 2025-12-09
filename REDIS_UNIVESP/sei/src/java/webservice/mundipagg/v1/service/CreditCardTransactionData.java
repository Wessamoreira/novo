/**
 * CreditCardTransactionData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class CreditCardTransactionData  implements java.io.Serializable {
    private java.lang.String acquirerAuthorizationCode;

    private java.lang.String acquirerName;

    private java.lang.Long amountInCents;

    private java.lang.Long authorizedAmountInCents;

    private java.lang.Long capturedAmountInCents;

    private java.util.Calendar createDate;

    private CreditCardBrandEnum creditCardBrandEnum;

    private java.lang.String creditCardNumber;

    private CreditCardTransactionStatusEnum creditCardTransactionStatusEnum;

    private java.lang.String customStatus;

    private java.util.Calendar dueDate;

    private java.lang.Integer installmentCount;

    private java.lang.String instantBuyKey;

    private java.lang.Boolean isReccurency;

    private java.lang.Long refundedAmountInCents;

    private java.lang.String transactionIdentifier;

    private java.lang.String transactionKey;

    private java.lang.String transactionReference;

    private java.lang.String uniqueSequentialNumber;

    private java.lang.Long voidedAmountInCents;

    public CreditCardTransactionData() {
    }

    public CreditCardTransactionData(
           java.lang.String acquirerAuthorizationCode,
           java.lang.String acquirerName,
           java.lang.Long amountInCents,
           java.lang.Long authorizedAmountInCents,
           java.lang.Long capturedAmountInCents,
           java.util.Calendar createDate,
           CreditCardBrandEnum creditCardBrandEnum,
           java.lang.String creditCardNumber,
           CreditCardTransactionStatusEnum creditCardTransactionStatusEnum,
           java.lang.String customStatus,
           java.util.Calendar dueDate,
           java.lang.Integer installmentCount,
           java.lang.String instantBuyKey,
           java.lang.Boolean isReccurency,
           java.lang.Long refundedAmountInCents,
           java.lang.String transactionIdentifier,
           java.lang.String transactionKey,
           java.lang.String transactionReference,
           java.lang.String uniqueSequentialNumber,
           java.lang.Long voidedAmountInCents) {
           this.acquirerAuthorizationCode = acquirerAuthorizationCode;
           this.acquirerName = acquirerName;
           this.amountInCents = amountInCents;
           this.authorizedAmountInCents = authorizedAmountInCents;
           this.capturedAmountInCents = capturedAmountInCents;
           this.createDate = createDate;
           this.creditCardBrandEnum = creditCardBrandEnum;
           this.creditCardNumber = creditCardNumber;
           this.creditCardTransactionStatusEnum = creditCardTransactionStatusEnum;
           this.customStatus = customStatus;
           this.dueDate = dueDate;
           this.installmentCount = installmentCount;
           this.instantBuyKey = instantBuyKey;
           this.isReccurency = isReccurency;
           this.refundedAmountInCents = refundedAmountInCents;
           this.transactionIdentifier = transactionIdentifier;
           this.transactionKey = transactionKey;
           this.transactionReference = transactionReference;
           this.uniqueSequentialNumber = uniqueSequentialNumber;
           this.voidedAmountInCents = voidedAmountInCents;
    }


    /**
     * Gets the acquirerAuthorizationCode value for this CreditCardTransactionData.
     * 
     * @return acquirerAuthorizationCode
     */
    public java.lang.String getAcquirerAuthorizationCode() {
        return acquirerAuthorizationCode;
    }


    /**
     * Sets the acquirerAuthorizationCode value for this CreditCardTransactionData.
     * 
     * @param acquirerAuthorizationCode
     */
    public void setAcquirerAuthorizationCode(java.lang.String acquirerAuthorizationCode) {
        this.acquirerAuthorizationCode = acquirerAuthorizationCode;
    }


    /**
     * Gets the acquirerName value for this CreditCardTransactionData.
     * 
     * @return acquirerName
     */
    public java.lang.String getAcquirerName() {
        return acquirerName;
    }


    /**
     * Sets the acquirerName value for this CreditCardTransactionData.
     * 
     * @param acquirerName
     */
    public void setAcquirerName(java.lang.String acquirerName) {
        this.acquirerName = acquirerName;
    }


    /**
     * Gets the amountInCents value for this CreditCardTransactionData.
     * 
     * @return amountInCents
     */
    public java.lang.Long getAmountInCents() {
        return amountInCents;
    }


    /**
     * Sets the amountInCents value for this CreditCardTransactionData.
     * 
     * @param amountInCents
     */
    public void setAmountInCents(java.lang.Long amountInCents) {
        this.amountInCents = amountInCents;
    }


    /**
     * Gets the authorizedAmountInCents value for this CreditCardTransactionData.
     * 
     * @return authorizedAmountInCents
     */
    public java.lang.Long getAuthorizedAmountInCents() {
        return authorizedAmountInCents;
    }


    /**
     * Sets the authorizedAmountInCents value for this CreditCardTransactionData.
     * 
     * @param authorizedAmountInCents
     */
    public void setAuthorizedAmountInCents(java.lang.Long authorizedAmountInCents) {
        this.authorizedAmountInCents = authorizedAmountInCents;
    }


    /**
     * Gets the capturedAmountInCents value for this CreditCardTransactionData.
     * 
     * @return capturedAmountInCents
     */
    public java.lang.Long getCapturedAmountInCents() {
        return capturedAmountInCents;
    }


    /**
     * Sets the capturedAmountInCents value for this CreditCardTransactionData.
     * 
     * @param capturedAmountInCents
     */
    public void setCapturedAmountInCents(java.lang.Long capturedAmountInCents) {
        this.capturedAmountInCents = capturedAmountInCents;
    }


    /**
     * Gets the createDate value for this CreditCardTransactionData.
     * 
     * @return createDate
     */
    public java.util.Calendar getCreateDate() {
        return createDate;
    }


    /**
     * Sets the createDate value for this CreditCardTransactionData.
     * 
     * @param createDate
     */
    public void setCreateDate(java.util.Calendar createDate) {
        this.createDate = createDate;
    }


    /**
     * Gets the creditCardBrandEnum value for this CreditCardTransactionData.
     * 
     * @return creditCardBrandEnum
     */
    public CreditCardBrandEnum getCreditCardBrandEnum() {
        return creditCardBrandEnum;
    }


    /**
     * Sets the creditCardBrandEnum value for this CreditCardTransactionData.
     * 
     * @param creditCardBrandEnum
     */
    public void setCreditCardBrandEnum(CreditCardBrandEnum creditCardBrandEnum) {
        this.creditCardBrandEnum = creditCardBrandEnum;
    }


    /**
     * Gets the creditCardNumber value for this CreditCardTransactionData.
     * 
     * @return creditCardNumber
     */
    public java.lang.String getCreditCardNumber() {
        return creditCardNumber;
    }


    /**
     * Sets the creditCardNumber value for this CreditCardTransactionData.
     * 
     * @param creditCardNumber
     */
    public void setCreditCardNumber(java.lang.String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }


    /**
     * Gets the creditCardTransactionStatusEnum value for this CreditCardTransactionData.
     * 
     * @return creditCardTransactionStatusEnum
     */
    public CreditCardTransactionStatusEnum getCreditCardTransactionStatusEnum() {
        return creditCardTransactionStatusEnum;
    }


    /**
     * Sets the creditCardTransactionStatusEnum value for this CreditCardTransactionData.
     * 
     * @param creditCardTransactionStatusEnum
     */
    public void setCreditCardTransactionStatusEnum(CreditCardTransactionStatusEnum creditCardTransactionStatusEnum) {
        this.creditCardTransactionStatusEnum = creditCardTransactionStatusEnum;
    }


    /**
     * Gets the customStatus value for this CreditCardTransactionData.
     * 
     * @return customStatus
     */
    public java.lang.String getCustomStatus() {
        return customStatus;
    }


    /**
     * Sets the customStatus value for this CreditCardTransactionData.
     * 
     * @param customStatus
     */
    public void setCustomStatus(java.lang.String customStatus) {
        this.customStatus = customStatus;
    }


    /**
     * Gets the dueDate value for this CreditCardTransactionData.
     * 
     * @return dueDate
     */
    public java.util.Calendar getDueDate() {
        return dueDate;
    }


    /**
     * Sets the dueDate value for this CreditCardTransactionData.
     * 
     * @param dueDate
     */
    public void setDueDate(java.util.Calendar dueDate) {
        this.dueDate = dueDate;
    }


    /**
     * Gets the installmentCount value for this CreditCardTransactionData.
     * 
     * @return installmentCount
     */
    public java.lang.Integer getInstallmentCount() {
        return installmentCount;
    }


    /**
     * Sets the installmentCount value for this CreditCardTransactionData.
     * 
     * @param installmentCount
     */
    public void setInstallmentCount(java.lang.Integer installmentCount) {
        this.installmentCount = installmentCount;
    }


    /**
     * Gets the instantBuyKey value for this CreditCardTransactionData.
     * 
     * @return instantBuyKey
     */
    public java.lang.String getInstantBuyKey() {
        return instantBuyKey;
    }


    /**
     * Sets the instantBuyKey value for this CreditCardTransactionData.
     * 
     * @param instantBuyKey
     */
    public void setInstantBuyKey(java.lang.String instantBuyKey) {
        this.instantBuyKey = instantBuyKey;
    }


    /**
     * Gets the isReccurency value for this CreditCardTransactionData.
     * 
     * @return isReccurency
     */
    public java.lang.Boolean getIsReccurency() {
        return isReccurency;
    }


    /**
     * Sets the isReccurency value for this CreditCardTransactionData.
     * 
     * @param isReccurency
     */
    public void setIsReccurency(java.lang.Boolean isReccurency) {
        this.isReccurency = isReccurency;
    }


    /**
     * Gets the refundedAmountInCents value for this CreditCardTransactionData.
     * 
     * @return refundedAmountInCents
     */
    public java.lang.Long getRefundedAmountInCents() {
        return refundedAmountInCents;
    }


    /**
     * Sets the refundedAmountInCents value for this CreditCardTransactionData.
     * 
     * @param refundedAmountInCents
     */
    public void setRefundedAmountInCents(java.lang.Long refundedAmountInCents) {
        this.refundedAmountInCents = refundedAmountInCents;
    }


    /**
     * Gets the transactionIdentifier value for this CreditCardTransactionData.
     * 
     * @return transactionIdentifier
     */
    public java.lang.String getTransactionIdentifier() {
        return transactionIdentifier;
    }


    /**
     * Sets the transactionIdentifier value for this CreditCardTransactionData.
     * 
     * @param transactionIdentifier
     */
    public void setTransactionIdentifier(java.lang.String transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
    }


    /**
     * Gets the transactionKey value for this CreditCardTransactionData.
     * 
     * @return transactionKey
     */
    public java.lang.String getTransactionKey() {
        return transactionKey;
    }


    /**
     * Sets the transactionKey value for this CreditCardTransactionData.
     * 
     * @param transactionKey
     */
    public void setTransactionKey(java.lang.String transactionKey) {
        this.transactionKey = transactionKey;
    }


    /**
     * Gets the transactionReference value for this CreditCardTransactionData.
     * 
     * @return transactionReference
     */
    public java.lang.String getTransactionReference() {
        return transactionReference;
    }


    /**
     * Sets the transactionReference value for this CreditCardTransactionData.
     * 
     * @param transactionReference
     */
    public void setTransactionReference(java.lang.String transactionReference) {
        this.transactionReference = transactionReference;
    }


    /**
     * Gets the uniqueSequentialNumber value for this CreditCardTransactionData.
     * 
     * @return uniqueSequentialNumber
     */
    public java.lang.String getUniqueSequentialNumber() {
        return uniqueSequentialNumber;
    }


    /**
     * Sets the uniqueSequentialNumber value for this CreditCardTransactionData.
     * 
     * @param uniqueSequentialNumber
     */
    public void setUniqueSequentialNumber(java.lang.String uniqueSequentialNumber) {
        this.uniqueSequentialNumber = uniqueSequentialNumber;
    }


    /**
     * Gets the voidedAmountInCents value for this CreditCardTransactionData.
     * 
     * @return voidedAmountInCents
     */
    public java.lang.Long getVoidedAmountInCents() {
        return voidedAmountInCents;
    }


    /**
     * Sets the voidedAmountInCents value for this CreditCardTransactionData.
     * 
     * @param voidedAmountInCents
     */
    public void setVoidedAmountInCents(java.lang.Long voidedAmountInCents) {
        this.voidedAmountInCents = voidedAmountInCents;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreditCardTransactionData)) return false;
        CreditCardTransactionData other = (CreditCardTransactionData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.acquirerAuthorizationCode==null && other.getAcquirerAuthorizationCode()==null) || 
             (this.acquirerAuthorizationCode!=null &&
              this.acquirerAuthorizationCode.equals(other.getAcquirerAuthorizationCode()))) &&
            ((this.acquirerName==null && other.getAcquirerName()==null) || 
             (this.acquirerName!=null &&
              this.acquirerName.equals(other.getAcquirerName()))) &&
            ((this.amountInCents==null && other.getAmountInCents()==null) || 
             (this.amountInCents!=null &&
              this.amountInCents.equals(other.getAmountInCents()))) &&
            ((this.authorizedAmountInCents==null && other.getAuthorizedAmountInCents()==null) || 
             (this.authorizedAmountInCents!=null &&
              this.authorizedAmountInCents.equals(other.getAuthorizedAmountInCents()))) &&
            ((this.capturedAmountInCents==null && other.getCapturedAmountInCents()==null) || 
             (this.capturedAmountInCents!=null &&
              this.capturedAmountInCents.equals(other.getCapturedAmountInCents()))) &&
            ((this.createDate==null && other.getCreateDate()==null) || 
             (this.createDate!=null &&
              this.createDate.equals(other.getCreateDate()))) &&
            ((this.creditCardBrandEnum==null && other.getCreditCardBrandEnum()==null) || 
             (this.creditCardBrandEnum!=null &&
              this.creditCardBrandEnum.equals(other.getCreditCardBrandEnum()))) &&
            ((this.creditCardNumber==null && other.getCreditCardNumber()==null) || 
             (this.creditCardNumber!=null &&
              this.creditCardNumber.equals(other.getCreditCardNumber()))) &&
            ((this.creditCardTransactionStatusEnum==null && other.getCreditCardTransactionStatusEnum()==null) || 
             (this.creditCardTransactionStatusEnum!=null &&
              this.creditCardTransactionStatusEnum.equals(other.getCreditCardTransactionStatusEnum()))) &&
            ((this.customStatus==null && other.getCustomStatus()==null) || 
             (this.customStatus!=null &&
              this.customStatus.equals(other.getCustomStatus()))) &&
            ((this.dueDate==null && other.getDueDate()==null) || 
             (this.dueDate!=null &&
              this.dueDate.equals(other.getDueDate()))) &&
            ((this.installmentCount==null && other.getInstallmentCount()==null) || 
             (this.installmentCount!=null &&
              this.installmentCount.equals(other.getInstallmentCount()))) &&
            ((this.instantBuyKey==null && other.getInstantBuyKey()==null) || 
             (this.instantBuyKey!=null &&
              this.instantBuyKey.equals(other.getInstantBuyKey()))) &&
            ((this.isReccurency==null && other.getIsReccurency()==null) || 
             (this.isReccurency!=null &&
              this.isReccurency.equals(other.getIsReccurency()))) &&
            ((this.refundedAmountInCents==null && other.getRefundedAmountInCents()==null) || 
             (this.refundedAmountInCents!=null &&
              this.refundedAmountInCents.equals(other.getRefundedAmountInCents()))) &&
            ((this.transactionIdentifier==null && other.getTransactionIdentifier()==null) || 
             (this.transactionIdentifier!=null &&
              this.transactionIdentifier.equals(other.getTransactionIdentifier()))) &&
            ((this.transactionKey==null && other.getTransactionKey()==null) || 
             (this.transactionKey!=null &&
              this.transactionKey.equals(other.getTransactionKey()))) &&
            ((this.transactionReference==null && other.getTransactionReference()==null) || 
             (this.transactionReference!=null &&
              this.transactionReference.equals(other.getTransactionReference()))) &&
            ((this.uniqueSequentialNumber==null && other.getUniqueSequentialNumber()==null) || 
             (this.uniqueSequentialNumber!=null &&
              this.uniqueSequentialNumber.equals(other.getUniqueSequentialNumber()))) &&
            ((this.voidedAmountInCents==null && other.getVoidedAmountInCents()==null) || 
             (this.voidedAmountInCents!=null &&
              this.voidedAmountInCents.equals(other.getVoidedAmountInCents())));
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
        if (getAcquirerAuthorizationCode() != null) {
            _hashCode += getAcquirerAuthorizationCode().hashCode();
        }
        if (getAcquirerName() != null) {
            _hashCode += getAcquirerName().hashCode();
        }
        if (getAmountInCents() != null) {
            _hashCode += getAmountInCents().hashCode();
        }
        if (getAuthorizedAmountInCents() != null) {
            _hashCode += getAuthorizedAmountInCents().hashCode();
        }
        if (getCapturedAmountInCents() != null) {
            _hashCode += getCapturedAmountInCents().hashCode();
        }
        if (getCreateDate() != null) {
            _hashCode += getCreateDate().hashCode();
        }
        if (getCreditCardBrandEnum() != null) {
            _hashCode += getCreditCardBrandEnum().hashCode();
        }
        if (getCreditCardNumber() != null) {
            _hashCode += getCreditCardNumber().hashCode();
        }
        if (getCreditCardTransactionStatusEnum() != null) {
            _hashCode += getCreditCardTransactionStatusEnum().hashCode();
        }
        if (getCustomStatus() != null) {
            _hashCode += getCustomStatus().hashCode();
        }
        if (getDueDate() != null) {
            _hashCode += getDueDate().hashCode();
        }
        if (getInstallmentCount() != null) {
            _hashCode += getInstallmentCount().hashCode();
        }
        if (getInstantBuyKey() != null) {
            _hashCode += getInstantBuyKey().hashCode();
        }
        if (getIsReccurency() != null) {
            _hashCode += getIsReccurency().hashCode();
        }
        if (getRefundedAmountInCents() != null) {
            _hashCode += getRefundedAmountInCents().hashCode();
        }
        if (getTransactionIdentifier() != null) {
            _hashCode += getTransactionIdentifier().hashCode();
        }
        if (getTransactionKey() != null) {
            _hashCode += getTransactionKey().hashCode();
        }
        if (getTransactionReference() != null) {
            _hashCode += getTransactionReference().hashCode();
        }
        if (getUniqueSequentialNumber() != null) {
            _hashCode += getUniqueSequentialNumber().hashCode();
        }
        if (getVoidedAmountInCents() != null) {
            _hashCode += getVoidedAmountInCents().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreditCardTransactionData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardTransactionData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acquirerAuthorizationCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "AcquirerAuthorizationCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acquirerName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "AcquirerName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amountInCents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "AmountInCents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authorizedAmountInCents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "AuthorizedAmountInCents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("capturedAmountInCents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CapturedAmountInCents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreateDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
        elemField.setFieldName("creditCardTransactionStatusEnum");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardTransactionStatusEnum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardTransactionStatusEnum"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CustomStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dueDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "DueDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
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
        elemField.setFieldName("isReccurency");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "IsReccurency"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("refundedAmountInCents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "RefundedAmountInCents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionIdentifier");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "TransactionIdentifier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "TransactionKey"));
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
        elemField.setFieldName("uniqueSequentialNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "UniqueSequentialNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("voidedAmountInCents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "VoidedAmountInCents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
