/**
 * ManageCreditCardTransactionRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class ManageCreditCardTransactionRequest  implements java.io.Serializable {
    private java.lang.Long amountInCents;

    private java.lang.String transactionKey;

    private java.lang.String transactionReference;

    public ManageCreditCardTransactionRequest() {
    }

    public ManageCreditCardTransactionRequest(
           java.lang.Long amountInCents,
           java.lang.String transactionKey,
           java.lang.String transactionReference) {
           this.amountInCents = amountInCents;
           this.transactionKey = transactionKey;
           this.transactionReference = transactionReference;
    }


    /**
     * Gets the amountInCents value for this ManageCreditCardTransactionRequest.
     * 
     * @return amountInCents
     */
    public java.lang.Long getAmountInCents() {
        return amountInCents;
    }


    /**
     * Sets the amountInCents value for this ManageCreditCardTransactionRequest.
     * 
     * @param amountInCents
     */
    public void setAmountInCents(java.lang.Long amountInCents) {
        this.amountInCents = amountInCents;
    }


    /**
     * Gets the transactionKey value for this ManageCreditCardTransactionRequest.
     * 
     * @return transactionKey
     */
    public java.lang.String getTransactionKey() {
        return transactionKey;
    }


    /**
     * Sets the transactionKey value for this ManageCreditCardTransactionRequest.
     * 
     * @param transactionKey
     */
    public void setTransactionKey(java.lang.String transactionKey) {
        this.transactionKey = transactionKey;
    }


    /**
     * Gets the transactionReference value for this ManageCreditCardTransactionRequest.
     * 
     * @return transactionReference
     */
    public java.lang.String getTransactionReference() {
        return transactionReference;
    }


    /**
     * Sets the transactionReference value for this ManageCreditCardTransactionRequest.
     * 
     * @param transactionReference
     */
    public void setTransactionReference(java.lang.String transactionReference) {
        this.transactionReference = transactionReference;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ManageCreditCardTransactionRequest)) return false;
        ManageCreditCardTransactionRequest other = (ManageCreditCardTransactionRequest) obj;
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
        new org.apache.axis.description.TypeDesc(ManageCreditCardTransactionRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ManageCreditCardTransactionRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amountInCents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "AmountInCents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
