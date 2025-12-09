/**
 * ManageOrderRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class ManageOrderRequest  implements java.io.Serializable {
    private ManageCreditCardTransactionRequest[] manageCreditCardTransactionCollection;

    private ManageOrderOperationEnum manageOrderOperationEnum;

    private java.lang.String merchantKey;

    private java.lang.String orderKey;

    private java.lang.String orderReference;

    private java.lang.String requestKey;

    public ManageOrderRequest() {
    }

    public ManageOrderRequest(
           ManageCreditCardTransactionRequest[] manageCreditCardTransactionCollection,
           ManageOrderOperationEnum manageOrderOperationEnum,
           java.lang.String merchantKey,
           java.lang.String orderKey,
           java.lang.String orderReference,
           java.lang.String requestKey) {
           this.manageCreditCardTransactionCollection = manageCreditCardTransactionCollection;
           this.manageOrderOperationEnum = manageOrderOperationEnum;
           this.merchantKey = merchantKey;
           this.orderKey = orderKey;
           this.orderReference = orderReference;
           this.requestKey = requestKey;
    }


    /**
     * Gets the manageCreditCardTransactionCollection value for this ManageOrderRequest.
     * 
     * @return manageCreditCardTransactionCollection
     */
    public ManageCreditCardTransactionRequest[] getManageCreditCardTransactionCollection() {
        return manageCreditCardTransactionCollection;
    }


    /**
     * Sets the manageCreditCardTransactionCollection value for this ManageOrderRequest.
     * 
     * @param manageCreditCardTransactionCollection
     */
    public void setManageCreditCardTransactionCollection(ManageCreditCardTransactionRequest[] manageCreditCardTransactionCollection) {
        this.manageCreditCardTransactionCollection = manageCreditCardTransactionCollection;
    }


    /**
     * Gets the manageOrderOperationEnum value for this ManageOrderRequest.
     * 
     * @return manageOrderOperationEnum
     */
    public ManageOrderOperationEnum getManageOrderOperationEnum() {
        return manageOrderOperationEnum;
    }


    /**
     * Sets the manageOrderOperationEnum value for this ManageOrderRequest.
     * 
     * @param manageOrderOperationEnum
     */
    public void setManageOrderOperationEnum(ManageOrderOperationEnum manageOrderOperationEnum) {
        this.manageOrderOperationEnum = manageOrderOperationEnum;
    }


    /**
     * Gets the merchantKey value for this ManageOrderRequest.
     * 
     * @return merchantKey
     */
    public java.lang.String getMerchantKey() {
        return merchantKey;
    }


    /**
     * Sets the merchantKey value for this ManageOrderRequest.
     * 
     * @param merchantKey
     */
    public void setMerchantKey(java.lang.String merchantKey) {
        this.merchantKey = merchantKey;
    }


    /**
     * Gets the orderKey value for this ManageOrderRequest.
     * 
     * @return orderKey
     */
    public java.lang.String getOrderKey() {
        return orderKey;
    }


    /**
     * Sets the orderKey value for this ManageOrderRequest.
     * 
     * @param orderKey
     */
    public void setOrderKey(java.lang.String orderKey) {
        this.orderKey = orderKey;
    }


    /**
     * Gets the orderReference value for this ManageOrderRequest.
     * 
     * @return orderReference
     */
    public java.lang.String getOrderReference() {
        return orderReference;
    }


    /**
     * Sets the orderReference value for this ManageOrderRequest.
     * 
     * @param orderReference
     */
    public void setOrderReference(java.lang.String orderReference) {
        this.orderReference = orderReference;
    }


    /**
     * Gets the requestKey value for this ManageOrderRequest.
     * 
     * @return requestKey
     */
    public java.lang.String getRequestKey() {
        return requestKey;
    }


    /**
     * Sets the requestKey value for this ManageOrderRequest.
     * 
     * @param requestKey
     */
    public void setRequestKey(java.lang.String requestKey) {
        this.requestKey = requestKey;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ManageOrderRequest)) return false;
        ManageOrderRequest other = (ManageOrderRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.manageCreditCardTransactionCollection==null && other.getManageCreditCardTransactionCollection()==null) || 
             (this.manageCreditCardTransactionCollection!=null &&
              java.util.Arrays.equals(this.manageCreditCardTransactionCollection, other.getManageCreditCardTransactionCollection()))) &&
            ((this.manageOrderOperationEnum==null && other.getManageOrderOperationEnum()==null) || 
             (this.manageOrderOperationEnum!=null &&
              this.manageOrderOperationEnum.equals(other.getManageOrderOperationEnum()))) &&
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
        if (getManageCreditCardTransactionCollection() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getManageCreditCardTransactionCollection());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getManageCreditCardTransactionCollection(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getManageOrderOperationEnum() != null) {
            _hashCode += getManageOrderOperationEnum().hashCode();
        }
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
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ManageOrderRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ManageOrderRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("manageCreditCardTransactionCollection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ManageCreditCardTransactionCollection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ManageCreditCardTransactionRequest"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ManageCreditCardTransactionRequest"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("manageOrderOperationEnum");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ManageOrderOperationEnum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ManageOrderOperationEnum"));
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
