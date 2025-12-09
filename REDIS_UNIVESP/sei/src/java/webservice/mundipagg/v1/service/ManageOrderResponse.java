/**
 * ManageOrderResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class ManageOrderResponse  implements java.io.Serializable {
    private ManageOrderOperationEnum manageOrderOperationEnum;

    private java.lang.Long mundiPaggTimeInMilliseconds;

    private java.lang.String orderKey;

    private java.lang.String orderReference;

    private OrderStatusEnum orderStatusEnum;

    private java.lang.String requestKey;

    private java.lang.Boolean success;

    private java.lang.String version;

    private CreditCardTransactionResult[] creditCardTransactionResultCollection;

    private BoletoTransactionResult[] boletoTransactionResultCollection;

    private MundiPaggSuggestion mundiPaggSuggestion;

    private ErrorReport errorReport;

    public ManageOrderResponse() {
    }

    public ManageOrderResponse(
           ManageOrderOperationEnum manageOrderOperationEnum,
           java.lang.Long mundiPaggTimeInMilliseconds,
           java.lang.String orderKey,
           java.lang.String orderReference,
           OrderStatusEnum orderStatusEnum,
           java.lang.String requestKey,
           java.lang.Boolean success,
           java.lang.String version,
           CreditCardTransactionResult[] creditCardTransactionResultCollection,
           BoletoTransactionResult[] boletoTransactionResultCollection,
           MundiPaggSuggestion mundiPaggSuggestion,
           ErrorReport errorReport) {
           this.manageOrderOperationEnum = manageOrderOperationEnum;
           this.mundiPaggTimeInMilliseconds = mundiPaggTimeInMilliseconds;
           this.orderKey = orderKey;
           this.orderReference = orderReference;
           this.orderStatusEnum = orderStatusEnum;
           this.requestKey = requestKey;
           this.success = success;
           this.version = version;
           this.creditCardTransactionResultCollection = creditCardTransactionResultCollection;
           this.boletoTransactionResultCollection = boletoTransactionResultCollection;
           this.mundiPaggSuggestion = mundiPaggSuggestion;
           this.errorReport = errorReport;
    }


    /**
     * Gets the manageOrderOperationEnum value for this ManageOrderResponse.
     * 
     * @return manageOrderOperationEnum
     */
    public ManageOrderOperationEnum getManageOrderOperationEnum() {
        return manageOrderOperationEnum;
    }


    /**
     * Sets the manageOrderOperationEnum value for this ManageOrderResponse.
     * 
     * @param manageOrderOperationEnum
     */
    public void setManageOrderOperationEnum(ManageOrderOperationEnum manageOrderOperationEnum) {
        this.manageOrderOperationEnum = manageOrderOperationEnum;
    }


    /**
     * Gets the mundiPaggTimeInMilliseconds value for this ManageOrderResponse.
     * 
     * @return mundiPaggTimeInMilliseconds
     */
    public java.lang.Long getMundiPaggTimeInMilliseconds() {
        return mundiPaggTimeInMilliseconds;
    }


    /**
     * Sets the mundiPaggTimeInMilliseconds value for this ManageOrderResponse.
     * 
     * @param mundiPaggTimeInMilliseconds
     */
    public void setMundiPaggTimeInMilliseconds(java.lang.Long mundiPaggTimeInMilliseconds) {
        this.mundiPaggTimeInMilliseconds = mundiPaggTimeInMilliseconds;
    }


    /**
     * Gets the orderKey value for this ManageOrderResponse.
     * 
     * @return orderKey
     */
    public java.lang.String getOrderKey() {
        return orderKey;
    }


    /**
     * Sets the orderKey value for this ManageOrderResponse.
     * 
     * @param orderKey
     */
    public void setOrderKey(java.lang.String orderKey) {
        this.orderKey = orderKey;
    }


    /**
     * Gets the orderReference value for this ManageOrderResponse.
     * 
     * @return orderReference
     */
    public java.lang.String getOrderReference() {
        return orderReference;
    }


    /**
     * Sets the orderReference value for this ManageOrderResponse.
     * 
     * @param orderReference
     */
    public void setOrderReference(java.lang.String orderReference) {
        this.orderReference = orderReference;
    }


    /**
     * Gets the orderStatusEnum value for this ManageOrderResponse.
     * 
     * @return orderStatusEnum
     */
    public OrderStatusEnum getOrderStatusEnum() {
        return orderStatusEnum;
    }


    /**
     * Sets the orderStatusEnum value for this ManageOrderResponse.
     * 
     * @param orderStatusEnum
     */
    public void setOrderStatusEnum(OrderStatusEnum orderStatusEnum) {
        this.orderStatusEnum = orderStatusEnum;
    }


    /**
     * Gets the requestKey value for this ManageOrderResponse.
     * 
     * @return requestKey
     */
    public java.lang.String getRequestKey() {
        return requestKey;
    }


    /**
     * Sets the requestKey value for this ManageOrderResponse.
     * 
     * @param requestKey
     */
    public void setRequestKey(java.lang.String requestKey) {
        this.requestKey = requestKey;
    }


    /**
     * Gets the success value for this ManageOrderResponse.
     * 
     * @return success
     */
    public java.lang.Boolean getSuccess() {
        return success;
    }


    /**
     * Sets the success value for this ManageOrderResponse.
     * 
     * @param success
     */
    public void setSuccess(java.lang.Boolean success) {
        this.success = success;
    }


    /**
     * Gets the version value for this ManageOrderResponse.
     * 
     * @return version
     */
    public java.lang.String getVersion() {
        return version;
    }


    /**
     * Sets the version value for this ManageOrderResponse.
     * 
     * @param version
     */
    public void setVersion(java.lang.String version) {
        this.version = version;
    }


    /**
     * Gets the creditCardTransactionResultCollection value for this ManageOrderResponse.
     * 
     * @return creditCardTransactionResultCollection
     */
    public CreditCardTransactionResult[] getCreditCardTransactionResultCollection() {
        return creditCardTransactionResultCollection;
    }


    /**
     * Sets the creditCardTransactionResultCollection value for this ManageOrderResponse.
     * 
     * @param creditCardTransactionResultCollection
     */
    public void setCreditCardTransactionResultCollection(CreditCardTransactionResult[] creditCardTransactionResultCollection) {
        this.creditCardTransactionResultCollection = creditCardTransactionResultCollection;
    }


    /**
     * Gets the boletoTransactionResultCollection value for this ManageOrderResponse.
     * 
     * @return boletoTransactionResultCollection
     */
    public BoletoTransactionResult[] getBoletoTransactionResultCollection() {
        return boletoTransactionResultCollection;
    }


    /**
     * Sets the boletoTransactionResultCollection value for this ManageOrderResponse.
     * 
     * @param boletoTransactionResultCollection
     */
    public void setBoletoTransactionResultCollection(BoletoTransactionResult[] boletoTransactionResultCollection) {
        this.boletoTransactionResultCollection = boletoTransactionResultCollection;
    }


    /**
     * Gets the mundiPaggSuggestion value for this ManageOrderResponse.
     * 
     * @return mundiPaggSuggestion
     */
    public MundiPaggSuggestion getMundiPaggSuggestion() {
        return mundiPaggSuggestion;
    }


    /**
     * Sets the mundiPaggSuggestion value for this ManageOrderResponse.
     * 
     * @param mundiPaggSuggestion
     */
    public void setMundiPaggSuggestion(MundiPaggSuggestion mundiPaggSuggestion) {
        this.mundiPaggSuggestion = mundiPaggSuggestion;
    }


    /**
     * Gets the errorReport value for this ManageOrderResponse.
     * 
     * @return errorReport
     */
    public ErrorReport getErrorReport() {
        return errorReport;
    }


    /**
     * Sets the errorReport value for this ManageOrderResponse.
     * 
     * @param errorReport
     */
    public void setErrorReport(ErrorReport errorReport) {
        this.errorReport = errorReport;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ManageOrderResponse)) return false;
        ManageOrderResponse other = (ManageOrderResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.manageOrderOperationEnum==null && other.getManageOrderOperationEnum()==null) || 
             (this.manageOrderOperationEnum!=null &&
              this.manageOrderOperationEnum.equals(other.getManageOrderOperationEnum()))) &&
            ((this.mundiPaggTimeInMilliseconds==null && other.getMundiPaggTimeInMilliseconds()==null) || 
             (this.mundiPaggTimeInMilliseconds!=null &&
              this.mundiPaggTimeInMilliseconds.equals(other.getMundiPaggTimeInMilliseconds()))) &&
            ((this.orderKey==null && other.getOrderKey()==null) || 
             (this.orderKey!=null &&
              this.orderKey.equals(other.getOrderKey()))) &&
            ((this.orderReference==null && other.getOrderReference()==null) || 
             (this.orderReference!=null &&
              this.orderReference.equals(other.getOrderReference()))) &&
            ((this.orderStatusEnum==null && other.getOrderStatusEnum()==null) || 
             (this.orderStatusEnum!=null &&
              this.orderStatusEnum.equals(other.getOrderStatusEnum()))) &&
            ((this.requestKey==null && other.getRequestKey()==null) || 
             (this.requestKey!=null &&
              this.requestKey.equals(other.getRequestKey()))) &&
            ((this.success==null && other.getSuccess()==null) || 
             (this.success!=null &&
              this.success.equals(other.getSuccess()))) &&
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion()))) &&
            ((this.creditCardTransactionResultCollection==null && other.getCreditCardTransactionResultCollection()==null) || 
             (this.creditCardTransactionResultCollection!=null &&
              java.util.Arrays.equals(this.creditCardTransactionResultCollection, other.getCreditCardTransactionResultCollection()))) &&
            ((this.boletoTransactionResultCollection==null && other.getBoletoTransactionResultCollection()==null) || 
             (this.boletoTransactionResultCollection!=null &&
              java.util.Arrays.equals(this.boletoTransactionResultCollection, other.getBoletoTransactionResultCollection()))) &&
            ((this.mundiPaggSuggestion==null && other.getMundiPaggSuggestion()==null) || 
             (this.mundiPaggSuggestion!=null &&
              this.mundiPaggSuggestion.equals(other.getMundiPaggSuggestion()))) &&
            ((this.errorReport==null && other.getErrorReport()==null) || 
             (this.errorReport!=null &&
              this.errorReport.equals(other.getErrorReport())));
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
        if (getManageOrderOperationEnum() != null) {
            _hashCode += getManageOrderOperationEnum().hashCode();
        }
        if (getMundiPaggTimeInMilliseconds() != null) {
            _hashCode += getMundiPaggTimeInMilliseconds().hashCode();
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
        if (getRequestKey() != null) {
            _hashCode += getRequestKey().hashCode();
        }
        if (getSuccess() != null) {
            _hashCode += getSuccess().hashCode();
        }
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        if (getCreditCardTransactionResultCollection() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCreditCardTransactionResultCollection());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCreditCardTransactionResultCollection(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getBoletoTransactionResultCollection() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBoletoTransactionResultCollection());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBoletoTransactionResultCollection(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getMundiPaggSuggestion() != null) {
            _hashCode += getMundiPaggSuggestion().hashCode();
        }
        if (getErrorReport() != null) {
            _hashCode += getErrorReport().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ManageOrderResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ManageOrderResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("manageOrderOperationEnum");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ManageOrderOperationEnum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ManageOrderOperationEnum"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mundiPaggTimeInMilliseconds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "MundiPaggTimeInMilliseconds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
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
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "RequestKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("success");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "Success"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "Version"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creditCardTransactionResultCollection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardTransactionResultCollection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardTransactionResult"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardTransactionResult"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("boletoTransactionResultCollection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BoletoTransactionResultCollection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BoletoTransactionResult"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BoletoTransactionResult"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mundiPaggSuggestion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "MundiPaggSuggestion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "MundiPaggSuggestion"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorReport");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ErrorReport"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ErrorReport"));
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
