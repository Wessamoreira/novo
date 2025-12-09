/**
 * CreditCardTransactionResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class CreditCardTransactionResult  implements java.io.Serializable {
    private java.lang.String acquirerMessage;

    private java.lang.String acquirerReturnCode;

    private java.lang.Long amountInCents;

    private java.lang.String authorizationCode;

    private java.lang.Long authorizedAmountInCents;

    private java.lang.Long capturedAmountInCents;

    private java.lang.String creditCardNumber;

    private CreditCardOperationEnum creditCardOperationEnum;

    private CreditCardTransactionStatusEnum creditCardTransactionStatusEnum;

    private java.lang.String customStatus;

    private java.util.Calendar dueDate;

    private java.lang.Long externalTimeInMilliseconds;

    private java.lang.String instantBuyKey;

    private java.lang.Long refundedAmountInCents;

    private java.lang.Boolean success;

    private java.lang.String transactionIdentifier;

    private java.lang.String transactionKey;

    private java.lang.String transactionReference;

    private java.lang.String uniqueSequentialNumber;

    private java.lang.Long voidedAmountInCents;

    private OriginalAcquirerReturnOriginalAcquirerReturnItem[] originalAcquirerReturnCollection;

    public CreditCardTransactionResult() {
    }

    public CreditCardTransactionResult(
           java.lang.String acquirerMessage,
           java.lang.String acquirerReturnCode,
           java.lang.Long amountInCents,
           java.lang.String authorizationCode,
           java.lang.Long authorizedAmountInCents,
           java.lang.Long capturedAmountInCents,
           java.lang.String creditCardNumber,
           CreditCardOperationEnum creditCardOperationEnum,
           CreditCardTransactionStatusEnum creditCardTransactionStatusEnum,
           java.lang.String customStatus,
           java.util.Calendar dueDate,
           java.lang.Long externalTimeInMilliseconds,
           java.lang.String instantBuyKey,
           java.lang.Long refundedAmountInCents,
           java.lang.Boolean success,
           java.lang.String transactionIdentifier,
           java.lang.String transactionKey,
           java.lang.String transactionReference,
           java.lang.String uniqueSequentialNumber,
           java.lang.Long voidedAmountInCents,
           OriginalAcquirerReturnOriginalAcquirerReturnItem[] originalAcquirerReturnCollection) {
           this.acquirerMessage = acquirerMessage;
           this.acquirerReturnCode = acquirerReturnCode;
           this.amountInCents = amountInCents;
           this.authorizationCode = authorizationCode;
           this.authorizedAmountInCents = authorizedAmountInCents;
           this.capturedAmountInCents = capturedAmountInCents;
           this.creditCardNumber = creditCardNumber;
           this.creditCardOperationEnum = creditCardOperationEnum;
           this.creditCardTransactionStatusEnum = creditCardTransactionStatusEnum;
           this.customStatus = customStatus;
           this.dueDate = dueDate;
           this.externalTimeInMilliseconds = externalTimeInMilliseconds;
           this.instantBuyKey = instantBuyKey;
           this.refundedAmountInCents = refundedAmountInCents;
           this.success = success;
           this.transactionIdentifier = transactionIdentifier;
           this.transactionKey = transactionKey;
           this.transactionReference = transactionReference;
           this.uniqueSequentialNumber = uniqueSequentialNumber;
           this.voidedAmountInCents = voidedAmountInCents;
           this.originalAcquirerReturnCollection = originalAcquirerReturnCollection;
    }


    /**
     * Gets the acquirerMessage value for this CreditCardTransactionResult.
     * 
     * @return acquirerMessage
     */
    public java.lang.String getAcquirerMessage() {
        return acquirerMessage;
    }


    /**
     * Sets the acquirerMessage value for this CreditCardTransactionResult.
     * 
     * @param acquirerMessage
     */
    public void setAcquirerMessage(java.lang.String acquirerMessage) {
        this.acquirerMessage = acquirerMessage;
    }


    /**
     * Gets the acquirerReturnCode value for this CreditCardTransactionResult.
     * 
     * @return acquirerReturnCode
     */
    public java.lang.String getAcquirerReturnCode() {
        return acquirerReturnCode;
    }


    /**
     * Sets the acquirerReturnCode value for this CreditCardTransactionResult.
     * 
     * @param acquirerReturnCode
     */
    public void setAcquirerReturnCode(java.lang.String acquirerReturnCode) {
        this.acquirerReturnCode = acquirerReturnCode;
    }


    /**
     * Gets the amountInCents value for this CreditCardTransactionResult.
     * 
     * @return amountInCents
     */
    public java.lang.Long getAmountInCents() {
        return amountInCents;
    }


    /**
     * Sets the amountInCents value for this CreditCardTransactionResult.
     * 
     * @param amountInCents
     */
    public void setAmountInCents(java.lang.Long amountInCents) {
        this.amountInCents = amountInCents;
    }


    /**
     * Gets the authorizationCode value for this CreditCardTransactionResult.
     * 
     * @return authorizationCode
     */
    public java.lang.String getAuthorizationCode() {
        return authorizationCode;
    }


    /**
     * Sets the authorizationCode value for this CreditCardTransactionResult.
     * 
     * @param authorizationCode
     */
    public void setAuthorizationCode(java.lang.String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }


    /**
     * Gets the authorizedAmountInCents value for this CreditCardTransactionResult.
     * 
     * @return authorizedAmountInCents
     */
    public java.lang.Long getAuthorizedAmountInCents() {
        return authorizedAmountInCents;
    }


    /**
     * Sets the authorizedAmountInCents value for this CreditCardTransactionResult.
     * 
     * @param authorizedAmountInCents
     */
    public void setAuthorizedAmountInCents(java.lang.Long authorizedAmountInCents) {
        this.authorizedAmountInCents = authorizedAmountInCents;
    }


    /**
     * Gets the capturedAmountInCents value for this CreditCardTransactionResult.
     * 
     * @return capturedAmountInCents
     */
    public java.lang.Long getCapturedAmountInCents() {
        return capturedAmountInCents;
    }


    /**
     * Sets the capturedAmountInCents value for this CreditCardTransactionResult.
     * 
     * @param capturedAmountInCents
     */
    public void setCapturedAmountInCents(java.lang.Long capturedAmountInCents) {
        this.capturedAmountInCents = capturedAmountInCents;
    }


    /**
     * Gets the creditCardNumber value for this CreditCardTransactionResult.
     * 
     * @return creditCardNumber
     */
    public java.lang.String getCreditCardNumber() {
        return creditCardNumber;
    }


    /**
     * Sets the creditCardNumber value for this CreditCardTransactionResult.
     * 
     * @param creditCardNumber
     */
    public void setCreditCardNumber(java.lang.String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }


    /**
     * Gets the creditCardOperationEnum value for this CreditCardTransactionResult.
     * 
     * @return creditCardOperationEnum
     */
    public CreditCardOperationEnum getCreditCardOperationEnum() {
        return creditCardOperationEnum;
    }


    /**
     * Sets the creditCardOperationEnum value for this CreditCardTransactionResult.
     * 
     * @param creditCardOperationEnum
     */
    public void setCreditCardOperationEnum(CreditCardOperationEnum creditCardOperationEnum) {
        this.creditCardOperationEnum = creditCardOperationEnum;
    }


    /**
     * Gets the creditCardTransactionStatusEnum value for this CreditCardTransactionResult.
     * 
     * @return creditCardTransactionStatusEnum
     */
    public CreditCardTransactionStatusEnum getCreditCardTransactionStatusEnum() {
        return creditCardTransactionStatusEnum;
    }


    /**
     * Sets the creditCardTransactionStatusEnum value for this CreditCardTransactionResult.
     * 
     * @param creditCardTransactionStatusEnum
     */
    public void setCreditCardTransactionStatusEnum(CreditCardTransactionStatusEnum creditCardTransactionStatusEnum) {
        this.creditCardTransactionStatusEnum = creditCardTransactionStatusEnum;
    }


    /**
     * Gets the customStatus value for this CreditCardTransactionResult.
     * 
     * @return customStatus
     */
    public java.lang.String getCustomStatus() {
        return customStatus;
    }


    /**
     * Sets the customStatus value for this CreditCardTransactionResult.
     * 
     * @param customStatus
     */
    public void setCustomStatus(java.lang.String customStatus) {
        this.customStatus = customStatus;
    }


    /**
     * Gets the dueDate value for this CreditCardTransactionResult.
     * 
     * @return dueDate
     */
    public java.util.Calendar getDueDate() {
        return dueDate;
    }


    /**
     * Sets the dueDate value for this CreditCardTransactionResult.
     * 
     * @param dueDate
     */
    public void setDueDate(java.util.Calendar dueDate) {
        this.dueDate = dueDate;
    }


    /**
     * Gets the externalTimeInMilliseconds value for this CreditCardTransactionResult.
     * 
     * @return externalTimeInMilliseconds
     */
    public java.lang.Long getExternalTimeInMilliseconds() {
        return externalTimeInMilliseconds;
    }


    /**
     * Sets the externalTimeInMilliseconds value for this CreditCardTransactionResult.
     * 
     * @param externalTimeInMilliseconds
     */
    public void setExternalTimeInMilliseconds(java.lang.Long externalTimeInMilliseconds) {
        this.externalTimeInMilliseconds = externalTimeInMilliseconds;
    }


    /**
     * Gets the instantBuyKey value for this CreditCardTransactionResult.
     * 
     * @return instantBuyKey
     */
    public java.lang.String getInstantBuyKey() {
        return instantBuyKey;
    }


    /**
     * Sets the instantBuyKey value for this CreditCardTransactionResult.
     * 
     * @param instantBuyKey
     */
    public void setInstantBuyKey(java.lang.String instantBuyKey) {
        this.instantBuyKey = instantBuyKey;
    }


    /**
     * Gets the refundedAmountInCents value for this CreditCardTransactionResult.
     * 
     * @return refundedAmountInCents
     */
    public java.lang.Long getRefundedAmountInCents() {
        return refundedAmountInCents;
    }


    /**
     * Sets the refundedAmountInCents value for this CreditCardTransactionResult.
     * 
     * @param refundedAmountInCents
     */
    public void setRefundedAmountInCents(java.lang.Long refundedAmountInCents) {
        this.refundedAmountInCents = refundedAmountInCents;
    }


    /**
     * Gets the success value for this CreditCardTransactionResult.
     * 
     * @return success
     */
    public java.lang.Boolean getSuccess() {
        return success;
    }


    /**
     * Sets the success value for this CreditCardTransactionResult.
     * 
     * @param success
     */
    public void setSuccess(java.lang.Boolean success) {
        this.success = success;
    }


    /**
     * Gets the transactionIdentifier value for this CreditCardTransactionResult.
     * 
     * @return transactionIdentifier
     */
    public java.lang.String getTransactionIdentifier() {
        return transactionIdentifier;
    }


    /**
     * Sets the transactionIdentifier value for this CreditCardTransactionResult.
     * 
     * @param transactionIdentifier
     */
    public void setTransactionIdentifier(java.lang.String transactionIdentifier) {
        this.transactionIdentifier = transactionIdentifier;
    }


    /**
     * Gets the transactionKey value for this CreditCardTransactionResult.
     * 
     * @return transactionKey
     */
    public java.lang.String getTransactionKey() {
        return transactionKey;
    }


    /**
     * Sets the transactionKey value for this CreditCardTransactionResult.
     * 
     * @param transactionKey
     */
    public void setTransactionKey(java.lang.String transactionKey) {
        this.transactionKey = transactionKey;
    }


    /**
     * Gets the transactionReference value for this CreditCardTransactionResult.
     * 
     * @return transactionReference
     */
    public java.lang.String getTransactionReference() {
        return transactionReference;
    }


    /**
     * Sets the transactionReference value for this CreditCardTransactionResult.
     * 
     * @param transactionReference
     */
    public void setTransactionReference(java.lang.String transactionReference) {
        this.transactionReference = transactionReference;
    }


    /**
     * Gets the uniqueSequentialNumber value for this CreditCardTransactionResult.
     * 
     * @return uniqueSequentialNumber
     */
    public java.lang.String getUniqueSequentialNumber() {
        return uniqueSequentialNumber;
    }


    /**
     * Sets the uniqueSequentialNumber value for this CreditCardTransactionResult.
     * 
     * @param uniqueSequentialNumber
     */
    public void setUniqueSequentialNumber(java.lang.String uniqueSequentialNumber) {
        this.uniqueSequentialNumber = uniqueSequentialNumber;
    }


    /**
     * Gets the voidedAmountInCents value for this CreditCardTransactionResult.
     * 
     * @return voidedAmountInCents
     */
    public java.lang.Long getVoidedAmountInCents() {
        return voidedAmountInCents;
    }


    /**
     * Sets the voidedAmountInCents value for this CreditCardTransactionResult.
     * 
     * @param voidedAmountInCents
     */
    public void setVoidedAmountInCents(java.lang.Long voidedAmountInCents) {
        this.voidedAmountInCents = voidedAmountInCents;
    }


    /**
     * Gets the originalAcquirerReturnCollection value for this CreditCardTransactionResult.
     * 
     * @return originalAcquirerReturnCollection
     */
    public OriginalAcquirerReturnOriginalAcquirerReturnItem[] getOriginalAcquirerReturnCollection() {
        return originalAcquirerReturnCollection;
    }


    /**
     * Sets the originalAcquirerReturnCollection value for this CreditCardTransactionResult.
     * 
     * @param originalAcquirerReturnCollection
     */
    public void setOriginalAcquirerReturnCollection(OriginalAcquirerReturnOriginalAcquirerReturnItem[] originalAcquirerReturnCollection) {
        this.originalAcquirerReturnCollection = originalAcquirerReturnCollection;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreditCardTransactionResult)) return false;
        CreditCardTransactionResult other = (CreditCardTransactionResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.acquirerMessage==null && other.getAcquirerMessage()==null) || 
             (this.acquirerMessage!=null &&
              this.acquirerMessage.equals(other.getAcquirerMessage()))) &&
            ((this.acquirerReturnCode==null && other.getAcquirerReturnCode()==null) || 
             (this.acquirerReturnCode!=null &&
              this.acquirerReturnCode.equals(other.getAcquirerReturnCode()))) &&
            ((this.amountInCents==null && other.getAmountInCents()==null) || 
             (this.amountInCents!=null &&
              this.amountInCents.equals(other.getAmountInCents()))) &&
            ((this.authorizationCode==null && other.getAuthorizationCode()==null) || 
             (this.authorizationCode!=null &&
              this.authorizationCode.equals(other.getAuthorizationCode()))) &&
            ((this.authorizedAmountInCents==null && other.getAuthorizedAmountInCents()==null) || 
             (this.authorizedAmountInCents!=null &&
              this.authorizedAmountInCents.equals(other.getAuthorizedAmountInCents()))) &&
            ((this.capturedAmountInCents==null && other.getCapturedAmountInCents()==null) || 
             (this.capturedAmountInCents!=null &&
              this.capturedAmountInCents.equals(other.getCapturedAmountInCents()))) &&
            ((this.creditCardNumber==null && other.getCreditCardNumber()==null) || 
             (this.creditCardNumber!=null &&
              this.creditCardNumber.equals(other.getCreditCardNumber()))) &&
            ((this.creditCardOperationEnum==null && other.getCreditCardOperationEnum()==null) || 
             (this.creditCardOperationEnum!=null &&
              this.creditCardOperationEnum.equals(other.getCreditCardOperationEnum()))) &&
            ((this.creditCardTransactionStatusEnum==null && other.getCreditCardTransactionStatusEnum()==null) || 
             (this.creditCardTransactionStatusEnum!=null &&
              this.creditCardTransactionStatusEnum.equals(other.getCreditCardTransactionStatusEnum()))) &&
            ((this.customStatus==null && other.getCustomStatus()==null) || 
             (this.customStatus!=null &&
              this.customStatus.equals(other.getCustomStatus()))) &&
            ((this.dueDate==null && other.getDueDate()==null) || 
             (this.dueDate!=null &&
              this.dueDate.equals(other.getDueDate()))) &&
            ((this.externalTimeInMilliseconds==null && other.getExternalTimeInMilliseconds()==null) || 
             (this.externalTimeInMilliseconds!=null &&
              this.externalTimeInMilliseconds.equals(other.getExternalTimeInMilliseconds()))) &&
            ((this.instantBuyKey==null && other.getInstantBuyKey()==null) || 
             (this.instantBuyKey!=null &&
              this.instantBuyKey.equals(other.getInstantBuyKey()))) &&
            ((this.refundedAmountInCents==null && other.getRefundedAmountInCents()==null) || 
             (this.refundedAmountInCents!=null &&
              this.refundedAmountInCents.equals(other.getRefundedAmountInCents()))) &&
            ((this.success==null && other.getSuccess()==null) || 
             (this.success!=null &&
              this.success.equals(other.getSuccess()))) &&
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
              this.voidedAmountInCents.equals(other.getVoidedAmountInCents()))) &&
            ((this.originalAcquirerReturnCollection==null && other.getOriginalAcquirerReturnCollection()==null) || 
             (this.originalAcquirerReturnCollection!=null &&
              java.util.Arrays.equals(this.originalAcquirerReturnCollection, other.getOriginalAcquirerReturnCollection())));
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
        if (getAcquirerMessage() != null) {
            _hashCode += getAcquirerMessage().hashCode();
        }
        if (getAcquirerReturnCode() != null) {
            _hashCode += getAcquirerReturnCode().hashCode();
        }
        if (getAmountInCents() != null) {
            _hashCode += getAmountInCents().hashCode();
        }
        if (getAuthorizationCode() != null) {
            _hashCode += getAuthorizationCode().hashCode();
        }
        if (getAuthorizedAmountInCents() != null) {
            _hashCode += getAuthorizedAmountInCents().hashCode();
        }
        if (getCapturedAmountInCents() != null) {
            _hashCode += getCapturedAmountInCents().hashCode();
        }
        if (getCreditCardNumber() != null) {
            _hashCode += getCreditCardNumber().hashCode();
        }
        if (getCreditCardOperationEnum() != null) {
            _hashCode += getCreditCardOperationEnum().hashCode();
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
        if (getExternalTimeInMilliseconds() != null) {
            _hashCode += getExternalTimeInMilliseconds().hashCode();
        }
        if (getInstantBuyKey() != null) {
            _hashCode += getInstantBuyKey().hashCode();
        }
        if (getRefundedAmountInCents() != null) {
            _hashCode += getRefundedAmountInCents().hashCode();
        }
        if (getSuccess() != null) {
            _hashCode += getSuccess().hashCode();
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
        if (getOriginalAcquirerReturnCollection() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOriginalAcquirerReturnCollection());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOriginalAcquirerReturnCollection(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreditCardTransactionResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardTransactionResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acquirerMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "AcquirerMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("acquirerReturnCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "AcquirerReturnCode"));
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
        elemField.setFieldName("authorizationCode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "AuthorizationCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
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
        elemField.setNillable(false);
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
        elemField.setFieldName("externalTimeInMilliseconds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ExternalTimeInMilliseconds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
        elemField.setFieldName("refundedAmountInCents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "RefundedAmountInCents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("success");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "Success"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("originalAcquirerReturnCollection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "OriginalAcquirerReturnCollection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", ">OriginalAcquirerReturn>OriginalAcquirerReturnItem"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "OriginalAcquirerReturnItem"));
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
