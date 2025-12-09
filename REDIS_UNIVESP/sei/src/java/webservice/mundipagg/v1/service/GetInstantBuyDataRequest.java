/**
 * GetInstantBuyDataRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class GetInstantBuyDataRequest  implements java.io.Serializable {
    private java.lang.String buyerKey;

    private java.lang.String instantBuyKey;

    private java.lang.String merchantKey;

    private java.lang.String requestKey;

    public GetInstantBuyDataRequest() {
    }

    public GetInstantBuyDataRequest(
           java.lang.String buyerKey,
           java.lang.String instantBuyKey,
           java.lang.String merchantKey,
           java.lang.String requestKey) {
           this.buyerKey = buyerKey;
           this.instantBuyKey = instantBuyKey;
           this.merchantKey = merchantKey;
           this.requestKey = requestKey;
    }


    /**
     * Gets the buyerKey value for this GetInstantBuyDataRequest.
     * 
     * @return buyerKey
     */
    public java.lang.String getBuyerKey() {
        return buyerKey;
    }


    /**
     * Sets the buyerKey value for this GetInstantBuyDataRequest.
     * 
     * @param buyerKey
     */
    public void setBuyerKey(java.lang.String buyerKey) {
        this.buyerKey = buyerKey;
    }


    /**
     * Gets the instantBuyKey value for this GetInstantBuyDataRequest.
     * 
     * @return instantBuyKey
     */
    public java.lang.String getInstantBuyKey() {
        return instantBuyKey;
    }


    /**
     * Sets the instantBuyKey value for this GetInstantBuyDataRequest.
     * 
     * @param instantBuyKey
     */
    public void setInstantBuyKey(java.lang.String instantBuyKey) {
        this.instantBuyKey = instantBuyKey;
    }


    /**
     * Gets the merchantKey value for this GetInstantBuyDataRequest.
     * 
     * @return merchantKey
     */
    public java.lang.String getMerchantKey() {
        return merchantKey;
    }


    /**
     * Sets the merchantKey value for this GetInstantBuyDataRequest.
     * 
     * @param merchantKey
     */
    public void setMerchantKey(java.lang.String merchantKey) {
        this.merchantKey = merchantKey;
    }


    /**
     * Gets the requestKey value for this GetInstantBuyDataRequest.
     * 
     * @return requestKey
     */
    public java.lang.String getRequestKey() {
        return requestKey;
    }


    /**
     * Sets the requestKey value for this GetInstantBuyDataRequest.
     * 
     * @param requestKey
     */
    public void setRequestKey(java.lang.String requestKey) {
        this.requestKey = requestKey;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetInstantBuyDataRequest)) return false;
        GetInstantBuyDataRequest other = (GetInstantBuyDataRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.buyerKey==null && other.getBuyerKey()==null) || 
             (this.buyerKey!=null &&
              this.buyerKey.equals(other.getBuyerKey()))) &&
            ((this.instantBuyKey==null && other.getInstantBuyKey()==null) || 
             (this.instantBuyKey!=null &&
              this.instantBuyKey.equals(other.getInstantBuyKey()))) &&
            ((this.merchantKey==null && other.getMerchantKey()==null) || 
             (this.merchantKey!=null &&
              this.merchantKey.equals(other.getMerchantKey()))) &&
            ((this.requestKey==null && other.getRequestKey()==null) || 
             (this.requestKey!=null &&
              this.requestKey.equals(other.getRequestKey())));
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
        if (getBuyerKey() != null) {
            _hashCode += getBuyerKey().hashCode();
        }
        if (getInstantBuyKey() != null) {
            _hashCode += getInstantBuyKey().hashCode();
        }
        if (getMerchantKey() != null) {
            _hashCode += getMerchantKey().hashCode();
        }
        if (getRequestKey() != null) {
            _hashCode += getRequestKey().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetInstantBuyDataRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "GetInstantBuyDataRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("buyerKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BuyerKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
        elemField.setFieldName("merchantKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "MerchantKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "RequestKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
