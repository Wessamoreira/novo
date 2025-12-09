/**
 * OrderData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class OrderData  implements java.io.Serializable {
    private java.util.Calendar createDate;

    private java.lang.String orderKey;

    private java.lang.String orderReference;

    private OrderStatusEnum orderStatusEnum;

    private CreditCardTransactionData[] creditCardTransactionDataCollection;

    private BoletoTransactionData[] boletoTransactionDataCollection;

    public OrderData() {
    }

    public OrderData(
           java.util.Calendar createDate,
           java.lang.String orderKey,
           java.lang.String orderReference,
           OrderStatusEnum orderStatusEnum,
           CreditCardTransactionData[] creditCardTransactionDataCollection,
           BoletoTransactionData[] boletoTransactionDataCollection) {
           this.createDate = createDate;
           this.orderKey = orderKey;
           this.orderReference = orderReference;
           this.orderStatusEnum = orderStatusEnum;
           this.creditCardTransactionDataCollection = creditCardTransactionDataCollection;
           this.boletoTransactionDataCollection = boletoTransactionDataCollection;
    }


    /**
     * Gets the createDate value for this OrderData.
     * 
     * @return createDate
     */
    public java.util.Calendar getCreateDate() {
        return createDate;
    }


    /**
     * Sets the createDate value for this OrderData.
     * 
     * @param createDate
     */
    public void setCreateDate(java.util.Calendar createDate) {
        this.createDate = createDate;
    }


    /**
     * Gets the orderKey value for this OrderData.
     * 
     * @return orderKey
     */
    public java.lang.String getOrderKey() {
        return orderKey;
    }


    /**
     * Sets the orderKey value for this OrderData.
     * 
     * @param orderKey
     */
    public void setOrderKey(java.lang.String orderKey) {
        this.orderKey = orderKey;
    }


    /**
     * Gets the orderReference value for this OrderData.
     * 
     * @return orderReference
     */
    public java.lang.String getOrderReference() {
        return orderReference;
    }


    /**
     * Sets the orderReference value for this OrderData.
     * 
     * @param orderReference
     */
    public void setOrderReference(java.lang.String orderReference) {
        this.orderReference = orderReference;
    }


    /**
     * Gets the orderStatusEnum value for this OrderData.
     * 
     * @return orderStatusEnum
     */
    public OrderStatusEnum getOrderStatusEnum() {
        return orderStatusEnum;
    }


    /**
     * Sets the orderStatusEnum value for this OrderData.
     * 
     * @param orderStatusEnum
     */
    public void setOrderStatusEnum(OrderStatusEnum orderStatusEnum) {
        this.orderStatusEnum = orderStatusEnum;
    }


    /**
     * Gets the creditCardTransactionDataCollection value for this OrderData.
     * 
     * @return creditCardTransactionDataCollection
     */
    public CreditCardTransactionData[] getCreditCardTransactionDataCollection() {
        return creditCardTransactionDataCollection;
    }


    /**
     * Sets the creditCardTransactionDataCollection value for this OrderData.
     * 
     * @param creditCardTransactionDataCollection
     */
    public void setCreditCardTransactionDataCollection(CreditCardTransactionData[] creditCardTransactionDataCollection) {
        this.creditCardTransactionDataCollection = creditCardTransactionDataCollection;
    }


    /**
     * Gets the boletoTransactionDataCollection value for this OrderData.
     * 
     * @return boletoTransactionDataCollection
     */
    public BoletoTransactionData[] getBoletoTransactionDataCollection() {
        return boletoTransactionDataCollection;
    }


    /**
     * Sets the boletoTransactionDataCollection value for this OrderData.
     * 
     * @param boletoTransactionDataCollection
     */
    public void setBoletoTransactionDataCollection(BoletoTransactionData[] boletoTransactionDataCollection) {
        this.boletoTransactionDataCollection = boletoTransactionDataCollection;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof OrderData)) return false;
        OrderData other = (OrderData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.createDate==null && other.getCreateDate()==null) || 
             (this.createDate!=null &&
              this.createDate.equals(other.getCreateDate()))) &&
            ((this.orderKey==null && other.getOrderKey()==null) || 
             (this.orderKey!=null &&
              this.orderKey.equals(other.getOrderKey()))) &&
            ((this.orderReference==null && other.getOrderReference()==null) || 
             (this.orderReference!=null &&
              this.orderReference.equals(other.getOrderReference()))) &&
            ((this.orderStatusEnum==null && other.getOrderStatusEnum()==null) || 
             (this.orderStatusEnum!=null &&
              this.orderStatusEnum.equals(other.getOrderStatusEnum()))) &&
            ((this.creditCardTransactionDataCollection==null && other.getCreditCardTransactionDataCollection()==null) || 
             (this.creditCardTransactionDataCollection!=null &&
              java.util.Arrays.equals(this.creditCardTransactionDataCollection, other.getCreditCardTransactionDataCollection()))) &&
            ((this.boletoTransactionDataCollection==null && other.getBoletoTransactionDataCollection()==null) || 
             (this.boletoTransactionDataCollection!=null &&
              java.util.Arrays.equals(this.boletoTransactionDataCollection, other.getBoletoTransactionDataCollection())));
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
        if (getCreateDate() != null) {
            _hashCode += getCreateDate().hashCode();
        }
        if (getOrderKey() != null) {
            _hashCode += getOrderKey().hashCode();
        }
        if (getOrderReference() != null) {
            _hashCode += getOrderReference().hashCode();
        }
        if (getOrderStatusEnum() != null) {
            _hashCode += getOrderStatusEnum().hashCode();
        }
        if (getCreditCardTransactionDataCollection() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCreditCardTransactionDataCollection());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCreditCardTransactionDataCollection(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getBoletoTransactionDataCollection() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBoletoTransactionDataCollection());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBoletoTransactionDataCollection(), i);
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
        new org.apache.axis.description.TypeDesc(OrderData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "OrderData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreateDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
        elemField.setFieldName("orderStatusEnum");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "OrderStatusEnum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "OrderStatusEnum"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creditCardTransactionDataCollection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardTransactionDataCollection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardTransactionData"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardTransactionData"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("boletoTransactionDataCollection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BoletoTransactionDataCollection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BoletoTransactionData"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BoletoTransactionData"));
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
