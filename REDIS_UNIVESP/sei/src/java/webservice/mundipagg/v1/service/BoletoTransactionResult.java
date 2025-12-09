/**
 * BoletoTransactionResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class BoletoTransactionResult  implements java.io.Serializable {
    private java.lang.Long amountInCents;

    private java.lang.String barcode;

    private BoletoTransactionStatusEnum boletoTransactionStatusEnum;

    private java.lang.String boletoUrl;

    private java.lang.String customStatus;

    private java.lang.String nossoNumero;

    private java.lang.Boolean success;

    private java.lang.String transactionKey;

    private java.lang.String transactionReference;

    public BoletoTransactionResult() {
    }

    public BoletoTransactionResult(
           java.lang.Long amountInCents,
           java.lang.String barcode,
           BoletoTransactionStatusEnum boletoTransactionStatusEnum,
           java.lang.String boletoUrl,
           java.lang.String customStatus,
           java.lang.String nossoNumero,
           java.lang.Boolean success,
           java.lang.String transactionKey,
           java.lang.String transactionReference) {
           this.amountInCents = amountInCents;
           this.barcode = barcode;
           this.boletoTransactionStatusEnum = boletoTransactionStatusEnum;
           this.boletoUrl = boletoUrl;
           this.customStatus = customStatus;
           this.nossoNumero = nossoNumero;
           this.success = success;
           this.transactionKey = transactionKey;
           this.transactionReference = transactionReference;
    }


    /**
     * Gets the amountInCents value for this BoletoTransactionResult.
     * 
     * @return amountInCents
     */
    public java.lang.Long getAmountInCents() {
        return amountInCents;
    }


    /**
     * Sets the amountInCents value for this BoletoTransactionResult.
     * 
     * @param amountInCents
     */
    public void setAmountInCents(java.lang.Long amountInCents) {
        this.amountInCents = amountInCents;
    }


    /**
     * Gets the barcode value for this BoletoTransactionResult.
     * 
     * @return barcode
     */
    public java.lang.String getBarcode() {
        return barcode;
    }


    /**
     * Sets the barcode value for this BoletoTransactionResult.
     * 
     * @param barcode
     */
    public void setBarcode(java.lang.String barcode) {
        this.barcode = barcode;
    }


    /**
     * Gets the boletoTransactionStatusEnum value for this BoletoTransactionResult.
     * 
     * @return boletoTransactionStatusEnum
     */
    public BoletoTransactionStatusEnum getBoletoTransactionStatusEnum() {
        return boletoTransactionStatusEnum;
    }


    /**
     * Sets the boletoTransactionStatusEnum value for this BoletoTransactionResult.
     * 
     * @param boletoTransactionStatusEnum
     */
    public void setBoletoTransactionStatusEnum(BoletoTransactionStatusEnum boletoTransactionStatusEnum) {
        this.boletoTransactionStatusEnum = boletoTransactionStatusEnum;
    }


    /**
     * Gets the boletoUrl value for this BoletoTransactionResult.
     * 
     * @return boletoUrl
     */
    public java.lang.String getBoletoUrl() {
        return boletoUrl;
    }


    /**
     * Sets the boletoUrl value for this BoletoTransactionResult.
     * 
     * @param boletoUrl
     */
    public void setBoletoUrl(java.lang.String boletoUrl) {
        this.boletoUrl = boletoUrl;
    }


    /**
     * Gets the customStatus value for this BoletoTransactionResult.
     * 
     * @return customStatus
     */
    public java.lang.String getCustomStatus() {
        return customStatus;
    }


    /**
     * Sets the customStatus value for this BoletoTransactionResult.
     * 
     * @param customStatus
     */
    public void setCustomStatus(java.lang.String customStatus) {
        this.customStatus = customStatus;
    }


    /**
     * Gets the nossoNumero value for this BoletoTransactionResult.
     * 
     * @return nossoNumero
     */
    public java.lang.String getNossoNumero() {
        return nossoNumero;
    }


    /**
     * Sets the nossoNumero value for this BoletoTransactionResult.
     * 
     * @param nossoNumero
     */
    public void setNossoNumero(java.lang.String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }


    /**
     * Gets the success value for this BoletoTransactionResult.
     * 
     * @return success
     */
    public java.lang.Boolean getSuccess() {
        return success;
    }


    /**
     * Sets the success value for this BoletoTransactionResult.
     * 
     * @param success
     */
    public void setSuccess(java.lang.Boolean success) {
        this.success = success;
    }


    /**
     * Gets the transactionKey value for this BoletoTransactionResult.
     * 
     * @return transactionKey
     */
    public java.lang.String getTransactionKey() {
        return transactionKey;
    }


    /**
     * Sets the transactionKey value for this BoletoTransactionResult.
     * 
     * @param transactionKey
     */
    public void setTransactionKey(java.lang.String transactionKey) {
        this.transactionKey = transactionKey;
    }


    /**
     * Gets the transactionReference value for this BoletoTransactionResult.
     * 
     * @return transactionReference
     */
    public java.lang.String getTransactionReference() {
        return transactionReference;
    }


    /**
     * Sets the transactionReference value for this BoletoTransactionResult.
     * 
     * @param transactionReference
     */
    public void setTransactionReference(java.lang.String transactionReference) {
        this.transactionReference = transactionReference;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BoletoTransactionResult)) return false;
        BoletoTransactionResult other = (BoletoTransactionResult) obj;
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
            ((this.barcode==null && other.getBarcode()==null) || 
             (this.barcode!=null &&
              this.barcode.equals(other.getBarcode()))) &&
            ((this.boletoTransactionStatusEnum==null && other.getBoletoTransactionStatusEnum()==null) || 
             (this.boletoTransactionStatusEnum!=null &&
              this.boletoTransactionStatusEnum.equals(other.getBoletoTransactionStatusEnum()))) &&
            ((this.boletoUrl==null && other.getBoletoUrl()==null) || 
             (this.boletoUrl!=null &&
              this.boletoUrl.equals(other.getBoletoUrl()))) &&
            ((this.customStatus==null && other.getCustomStatus()==null) || 
             (this.customStatus!=null &&
              this.customStatus.equals(other.getCustomStatus()))) &&
            ((this.nossoNumero==null && other.getNossoNumero()==null) || 
             (this.nossoNumero!=null &&
              this.nossoNumero.equals(other.getNossoNumero()))) &&
            ((this.success==null && other.getSuccess()==null) || 
             (this.success!=null &&
              this.success.equals(other.getSuccess()))) &&
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
        if (getBarcode() != null) {
            _hashCode += getBarcode().hashCode();
        }
        if (getBoletoTransactionStatusEnum() != null) {
            _hashCode += getBoletoTransactionStatusEnum().hashCode();
        }
        if (getBoletoUrl() != null) {
            _hashCode += getBoletoUrl().hashCode();
        }
        if (getCustomStatus() != null) {
            _hashCode += getCustomStatus().hashCode();
        }
        if (getNossoNumero() != null) {
            _hashCode += getNossoNumero().hashCode();
        }
        if (getSuccess() != null) {
            _hashCode += getSuccess().hashCode();
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
        new org.apache.axis.description.TypeDesc(BoletoTransactionResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BoletoTransactionResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amountInCents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "AmountInCents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
        elemField.setFieldName("customStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CustomStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nossoNumero");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "NossoNumero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
