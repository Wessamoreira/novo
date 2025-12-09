/**
 * RetryOrderRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class RetryOrderRequest  implements java.io.Serializable {
    private java.lang.String merchantKey;

    private java.lang.String orderKey;

    private java.lang.String orderReference;

    private java.lang.String requestKey;

    private RetryOrderCreditCardTransactionRequest[] retryOrderCreditCardTransactionRequestCollection;

    public RetryOrderRequest() {
    }

    public RetryOrderRequest(
           java.lang.String merchantKey,
           java.lang.String orderKey,
           java.lang.String orderReference,
           java.lang.String requestKey,
           RetryOrderCreditCardTransactionRequest[] retryOrderCreditCardTransactionRequestCollection) {
           this.merchantKey = merchantKey;
           this.orderKey = orderKey;
           this.orderReference = orderReference;
           this.requestKey = requestKey;
           this.retryOrderCreditCardTransactionRequestCollection = retryOrderCreditCardTransactionRequestCollection;
    }


    /**
     * Gets the merchantKey value for this RetryOrderRequest.
     * 
     * @return merchantKey
     */
    public java.lang.String getMerchantKey() {
        return merchantKey;
    }


    /**
     * Sets the merchantKey value for this RetryOrderRequest.
     * 
     * @param merchantKey
     */
    public void setMerchantKey(java.lang.String merchantKey) {
        this.merchantKey = merchantKey;
    }


    /**
     * Gets the orderKey value for this RetryOrderRequest.
     * 
     * @return orderKey
     */
    public java.lang.String getOrderKey() {
        return orderKey;
    }


    /**
     * Sets the orderKey value for this RetryOrderRequest.
     * 
     * @param orderKey
     */
    public void setOrderKey(java.lang.String orderKey) {
        this.orderKey = orderKey;
    }


    /**
     * Gets the orderReference value for this RetryOrderRequest.
     * 
     * @return orderReference
     */
    public java.lang.String getOrderReference() {
        return orderReference;
    }


    /**
     * Sets the orderReference value for this RetryOrderRequest.
     * 
     * @param orderReference
     */
    public void setOrderReference(java.lang.String orderReference) {
        this.orderReference = orderReference;
    }


    /**
     * Gets the requestKey value for this RetryOrderRequest.
     * 
     * @return requestKey
     */
    public java.lang.String getRequestKey() {
        return requestKey;
    }


    /**
     * Sets the requestKey value for this RetryOrderRequest.
     * 
     * @param requestKey
     */
    public void setRequestKey(java.lang.String requestKey) {
        this.requestKey = requestKey;
    }


    /**
     * Gets the retryOrderCreditCardTransactionRequestCollection value for this RetryOrderRequest.
     * 
     * @return retryOrderCreditCardTransactionRequestCollection
     */
    public RetryOrderCreditCardTransactionRequest[] getRetryOrderCreditCardTransactionRequestCollection() {
        return retryOrderCreditCardTransactionRequestCollection;
    }


    /**
     * Sets the retryOrderCreditCardTransactionRequestCollection value for this RetryOrderRequest.
     * 
     * @param retryOrderCreditCardTransactionRequestCollection
     */
    public void setRetryOrderCreditCardTransactionRequestCollection(RetryOrderCreditCardTransactionRequest[] retryOrderCreditCardTransactionRequestCollection) {
        this.retryOrderCreditCardTransactionRequestCollection = retryOrderCreditCardTransactionRequestCollection;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RetryOrderRequest)) return false;
        RetryOrderRequest other = (RetryOrderRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.merchantKey==null && other.getMerchantKey()==null) || 
             (this.merchantKey!=null &&
              this.merchantKey.equals(other.getMerchantKey()))) &&
            ((this.orderKey==null && other.getOrderKey()==null) || 
             (this.orderKey!=null &&
              this.orderKey.equals(other.getOrderKey()))) &&
            ((this.orderReference==null && other.getOrderReference()==null) || 
             (this.orderReference!=null &&
              this.orderReference.equals(other.getOrderReference()))) &&
            ((this.requestKey==null && other.getRequestKey()==null) || 
             (this.requestKey!=null &&
              this.requestKey.equals(other.getRequestKey()))) &&
            ((this.retryOrderCreditCardTransactionRequestCollection==null && other.getRetryOrderCreditCardTransactionRequestCollection()==null) || 
             (this.retryOrderCreditCardTransactionRequestCollection!=null &&
              java.util.Arrays.equals(this.retryOrderCreditCardTransactionRequestCollection, other.getRetryOrderCreditCardTransactionRequestCollection())));
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
        if (getMerchantKey() != null) {
            _hashCode += getMerchantKey().hashCode();
        }
        if (getOrderKey() != null) {
            _hashCode += getOrderKey().hashCode();
        }
        if (getOrderReference() != null) {
            _hashCode += getOrderReference().hashCode();
        }
        if (getRequestKey() != null) {
            _hashCode += getRequestKey().hashCode();
        }
        if (getRetryOrderCreditCardTransactionRequestCollection() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRetryOrderCreditCardTransactionRequestCollection());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRetryOrderCreditCardTransactionRequestCollection(), i);
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
        new org.apache.axis.description.TypeDesc(RetryOrderRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "RetryOrderRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("merchantKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "MerchantKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "OrderKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "OrderReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "RequestKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retryOrderCreditCardTransactionRequestCollection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "RetryOrderCreditCardTransactionRequestCollection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "RetryOrderCreditCardTransactionRequest"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "RetryOrderCreditCardTransactionRequest"));
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
