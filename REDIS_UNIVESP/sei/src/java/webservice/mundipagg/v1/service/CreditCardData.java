/**
 * CreditCardData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class CreditCardData  implements java.io.Serializable {
    private CreditCardBrandEnum creditCardBrandEnum;

    private java.lang.String creditCardNumber;

    private java.lang.String instantBuyKey;

    private java.lang.Boolean isExpiredCreditCard;

    public CreditCardData() {
    }

    public CreditCardData(
           CreditCardBrandEnum creditCardBrandEnum,
           java.lang.String creditCardNumber,
           java.lang.String instantBuyKey,
           java.lang.Boolean isExpiredCreditCard) {
           this.creditCardBrandEnum = creditCardBrandEnum;
           this.creditCardNumber = creditCardNumber;
           this.instantBuyKey = instantBuyKey;
           this.isExpiredCreditCard = isExpiredCreditCard;
    }


    /**
     * Gets the creditCardBrandEnum value for this CreditCardData.
     * 
     * @return creditCardBrandEnum
     */
    public CreditCardBrandEnum getCreditCardBrandEnum() {
        return creditCardBrandEnum;
    }


    /**
     * Sets the creditCardBrandEnum value for this CreditCardData.
     * 
     * @param creditCardBrandEnum
     */
    public void setCreditCardBrandEnum(CreditCardBrandEnum creditCardBrandEnum) {
        this.creditCardBrandEnum = creditCardBrandEnum;
    }


    /**
     * Gets the creditCardNumber value for this CreditCardData.
     * 
     * @return creditCardNumber
     */
    public java.lang.String getCreditCardNumber() {
        return creditCardNumber;
    }


    /**
     * Sets the creditCardNumber value for this CreditCardData.
     * 
     * @param creditCardNumber
     */
    public void setCreditCardNumber(java.lang.String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }


    /**
     * Gets the instantBuyKey value for this CreditCardData.
     * 
     * @return instantBuyKey
     */
    public java.lang.String getInstantBuyKey() {
        return instantBuyKey;
    }


    /**
     * Sets the instantBuyKey value for this CreditCardData.
     * 
     * @param instantBuyKey
     */
    public void setInstantBuyKey(java.lang.String instantBuyKey) {
        this.instantBuyKey = instantBuyKey;
    }


    /**
     * Gets the isExpiredCreditCard value for this CreditCardData.
     * 
     * @return isExpiredCreditCard
     */
    public java.lang.Boolean getIsExpiredCreditCard() {
        return isExpiredCreditCard;
    }


    /**
     * Sets the isExpiredCreditCard value for this CreditCardData.
     * 
     * @param isExpiredCreditCard
     */
    public void setIsExpiredCreditCard(java.lang.Boolean isExpiredCreditCard) {
        this.isExpiredCreditCard = isExpiredCreditCard;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreditCardData)) return false;
        CreditCardData other = (CreditCardData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.creditCardBrandEnum==null && other.getCreditCardBrandEnum()==null) || 
             (this.creditCardBrandEnum!=null &&
              this.creditCardBrandEnum.equals(other.getCreditCardBrandEnum()))) &&
            ((this.creditCardNumber==null && other.getCreditCardNumber()==null) || 
             (this.creditCardNumber!=null &&
              this.creditCardNumber.equals(other.getCreditCardNumber()))) &&
            ((this.instantBuyKey==null && other.getInstantBuyKey()==null) || 
             (this.instantBuyKey!=null &&
              this.instantBuyKey.equals(other.getInstantBuyKey()))) &&
            ((this.isExpiredCreditCard==null && other.getIsExpiredCreditCard()==null) || 
             (this.isExpiredCreditCard!=null &&
              this.isExpiredCreditCard.equals(other.getIsExpiredCreditCard())));
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
        if (getCreditCardBrandEnum() != null) {
            _hashCode += getCreditCardBrandEnum().hashCode();
        }
        if (getCreditCardNumber() != null) {
            _hashCode += getCreditCardNumber().hashCode();
        }
        if (getInstantBuyKey() != null) {
            _hashCode += getInstantBuyKey().hashCode();
        }
        if (getIsExpiredCreditCard() != null) {
            _hashCode += getIsExpiredCreditCard().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CreditCardData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
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
        elemField.setFieldName("instantBuyKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "InstantBuyKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isExpiredCreditCard");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "IsExpiredCreditCard"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
