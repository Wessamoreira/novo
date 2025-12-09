/**
 * BoletoTransactionData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class BoletoTransactionData  implements java.io.Serializable {
    private java.lang.Long amountInCents;

    private java.lang.Long amountPaidInCents;

    private java.lang.String bankNumber;

    private java.lang.String barcode;

    private BoletoTransactionStatusEnum boletoTransactionStatusEnum;

    private java.lang.String boletoUrl;

    private java.util.Calendar createDate;

    private java.lang.String customStatus;

    private java.util.Calendar expirationDate;

    private java.lang.String nossoNumero;

    private java.lang.String transactionKey;

    private java.lang.String transactionReference;

    public BoletoTransactionData() {
    }

    public BoletoTransactionData(
           java.lang.Long amountInCents,
           java.lang.Long amountPaidInCents,
           java.lang.String bankNumber,
           java.lang.String barcode,
           BoletoTransactionStatusEnum boletoTransactionStatusEnum,
           java.lang.String boletoUrl,
           java.util.Calendar createDate,
           java.lang.String customStatus,
           java.util.Calendar expirationDate,
           java.lang.String nossoNumero,
           java.lang.String transactionKey,
           java.lang.String transactionReference) {
           this.amountInCents = amountInCents;
           this.amountPaidInCents = amountPaidInCents;
           this.bankNumber = bankNumber;
           this.barcode = barcode;
           this.boletoTransactionStatusEnum = boletoTransactionStatusEnum;
           this.boletoUrl = boletoUrl;
           this.createDate = createDate;
           this.customStatus = customStatus;
           this.expirationDate = expirationDate;
           this.nossoNumero = nossoNumero;
           this.transactionKey = transactionKey;
           this.transactionReference = transactionReference;
    }


    /**
     * Gets the amountInCents value for this BoletoTransactionData.
     * 
     * @return amountInCents
     */
    public java.lang.Long getAmountInCents() {
        return amountInCents;
    }


    /**
     * Sets the amountInCents value for this BoletoTransactionData.
     * 
     * @param amountInCents
     */
    public void setAmountInCents(java.lang.Long amountInCents) {
        this.amountInCents = amountInCents;
    }


    /**
     * Gets the amountPaidInCents value for this BoletoTransactionData.
     * 
     * @return amountPaidInCents
     */
    public java.lang.Long getAmountPaidInCents() {
        return amountPaidInCents;
    }


    /**
     * Sets the amountPaidInCents value for this BoletoTransactionData.
     * 
     * @param amountPaidInCents
     */
    public void setAmountPaidInCents(java.lang.Long amountPaidInCents) {
        this.amountPaidInCents = amountPaidInCents;
    }


    /**
     * Gets the bankNumber value for this BoletoTransactionData.
     * 
     * @return bankNumber
     */
    public java.lang.String getBankNumber() {
        return bankNumber;
    }


    /**
     * Sets the bankNumber value for this BoletoTransactionData.
     * 
     * @param bankNumber
     */
    public void setBankNumber(java.lang.String bankNumber) {
        this.bankNumber = bankNumber;
    }


    /**
     * Gets the barcode value for this BoletoTransactionData.
     * 
     * @return barcode
     */
    public java.lang.String getBarcode() {
        return barcode;
    }


    /**
     * Sets the barcode value for this BoletoTransactionData.
     * 
     * @param barcode
     */
    public void setBarcode(java.lang.String barcode) {
        this.barcode = barcode;
    }


    /**
     * Gets the boletoTransactionStatusEnum value for this BoletoTransactionData.
     * 
     * @return boletoTransactionStatusEnum
     */
    public BoletoTransactionStatusEnum getBoletoTransactionStatusEnum() {
        return boletoTransactionStatusEnum;
    }


    /**
     * Sets the boletoTransactionStatusEnum value for this BoletoTransactionData.
     * 
     * @param boletoTransactionStatusEnum
     */
    public void setBoletoTransactionStatusEnum(BoletoTransactionStatusEnum boletoTransactionStatusEnum) {
        this.boletoTransactionStatusEnum = boletoTransactionStatusEnum;
    }


    /**
     * Gets the boletoUrl value for this BoletoTransactionData.
     * 
     * @return boletoUrl
     */
    public java.lang.String getBoletoUrl() {
        return boletoUrl;
    }


    /**
     * Sets the boletoUrl value for this BoletoTransactionData.
     * 
     * @param boletoUrl
     */
    public void setBoletoUrl(java.lang.String boletoUrl) {
        this.boletoUrl = boletoUrl;
    }


    /**
     * Gets the createDate value for this BoletoTransactionData.
     * 
     * @return createDate
     */
    public java.util.Calendar getCreateDate() {
        return createDate;
    }


    /**
     * Sets the createDate value for this BoletoTransactionData.
     * 
     * @param createDate
     */
    public void setCreateDate(java.util.Calendar createDate) {
        this.createDate = createDate;
    }


    /**
     * Gets the customStatus value for this BoletoTransactionData.
     * 
     * @return customStatus
     */
    public java.lang.String getCustomStatus() {
        return customStatus;
    }


    /**
     * Sets the customStatus value for this BoletoTransactionData.
     * 
     * @param customStatus
     */
    public void setCustomStatus(java.lang.String customStatus) {
        this.customStatus = customStatus;
    }


    /**
     * Gets the expirationDate value for this BoletoTransactionData.
     * 
     * @return expirationDate
     */
    public java.util.Calendar getExpirationDate() {
        return expirationDate;
    }


    /**
     * Sets the expirationDate value for this BoletoTransactionData.
     * 
     * @param expirationDate
     */
    public void setExpirationDate(java.util.Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }


    /**
     * Gets the nossoNumero value for this BoletoTransactionData.
     * 
     * @return nossoNumero
     */
    public java.lang.String getNossoNumero() {
        return nossoNumero;
    }


    /**
     * Sets the nossoNumero value for this BoletoTransactionData.
     * 
     * @param nossoNumero
     */
    public void setNossoNumero(java.lang.String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }


    /**
     * Gets the transactionKey value for this BoletoTransactionData.
     * 
     * @return transactionKey
     */
    public java.lang.String getTransactionKey() {
        return transactionKey;
    }


    /**
     * Sets the transactionKey value for this BoletoTransactionData.
     * 
     * @param transactionKey
     */
    public void setTransactionKey(java.lang.String transactionKey) {
        this.transactionKey = transactionKey;
    }


    /**
     * Gets the transactionReference value for this BoletoTransactionData.
     * 
     * @return transactionReference
     */
    public java.lang.String getTransactionReference() {
        return transactionReference;
    }


    /**
     * Sets the transactionReference value for this BoletoTransactionData.
     * 
     * @param transactionReference
     */
    public void setTransactionReference(java.lang.String transactionReference) {
        this.transactionReference = transactionReference;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BoletoTransactionData)) return false;
        BoletoTransactionData other = (BoletoTransactionData) obj;
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
            ((this.amountPaidInCents==null && other.getAmountPaidInCents()==null) || 
             (this.amountPaidInCents!=null &&
              this.amountPaidInCents.equals(other.getAmountPaidInCents()))) &&
            ((this.bankNumber==null && other.getBankNumber()==null) || 
             (this.bankNumber!=null &&
              this.bankNumber.equals(other.getBankNumber()))) &&
            ((this.barcode==null && other.getBarcode()==null) || 
             (this.barcode!=null &&
              this.barcode.equals(other.getBarcode()))) &&
            ((this.boletoTransactionStatusEnum==null && other.getBoletoTransactionStatusEnum()==null) || 
             (this.boletoTransactionStatusEnum!=null &&
              this.boletoTransactionStatusEnum.equals(other.getBoletoTransactionStatusEnum()))) &&
            ((this.boletoUrl==null && other.getBoletoUrl()==null) || 
             (this.boletoUrl!=null &&
              this.boletoUrl.equals(other.getBoletoUrl()))) &&
            ((this.createDate==null && other.getCreateDate()==null) || 
             (this.createDate!=null &&
              this.createDate.equals(other.getCreateDate()))) &&
            ((this.customStatus==null && other.getCustomStatus()==null) || 
             (this.customStatus!=null &&
              this.customStatus.equals(other.getCustomStatus()))) &&
            ((this.expirationDate==null && other.getExpirationDate()==null) || 
             (this.expirationDate!=null &&
              this.expirationDate.equals(other.getExpirationDate()))) &&
            ((this.nossoNumero==null && other.getNossoNumero()==null) || 
             (this.nossoNumero!=null &&
              this.nossoNumero.equals(other.getNossoNumero()))) &&
            ((this.transactionKey==null && other.getTransactionKey()==null) || 
             (this.transactionKey!=null &&
              this.transactionKey.equals(other.getTransactionKey()))) &&
            ((this.transactionReference==null && other.getTransactionReference()==null) || 
             (this.transactionReference!=null &&
              this.transactionReference.equals(other.getTransactionReference())));
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
        if (getAmountPaidInCents() != null) {
            _hashCode += getAmountPaidInCents().hashCode();
        }
        if (getBankNumber() != null) {
            _hashCode += getBankNumber().hashCode();
        }
        if (getBarcode() != null) {
            _hashCode += getBarcode().hashCode();
        }
        if (getBoletoTransactionStatusEnum() != null) {
            _hashCode += getBoletoTransactionStatusEnum().hashCode();
        }
        if (getBoletoUrl() != null) {
            _hashCode += getBoletoUrl().hashCode();
        }
        if (getCreateDate() != null) {
            _hashCode += getCreateDate().hashCode();
        }
        if (getCustomStatus() != null) {
            _hashCode += getCustomStatus().hashCode();
        }
        if (getExpirationDate() != null) {
            _hashCode += getExpirationDate().hashCode();
        }
        if (getNossoNumero() != null) {
            _hashCode += getNossoNumero().hashCode();
        }
        if (getTransactionKey() != null) {
            _hashCode += getTransactionKey().hashCode();
        }
        if (getTransactionReference() != null) {
            _hashCode += getTransactionReference().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BoletoTransactionData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BoletoTransactionData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amountInCents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "AmountInCents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amountPaidInCents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "AmountPaidInCents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BankNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("barcode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "Barcode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("boletoTransactionStatusEnum");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BoletoTransactionStatusEnum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BoletoTransactionStatusEnum"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("boletoUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BoletoUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("customStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CustomStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("expirationDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ExpirationDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nossoNumero");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "NossoNumero"));
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
