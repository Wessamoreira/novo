/**
 * QueryOrderResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class QueryOrderResponse  implements java.io.Serializable {
    private java.lang.Long mundiPaggTimeInMilliseconds;

    private java.lang.Integer orderDataCount;

    private java.lang.String requestKey;

    private java.lang.Boolean success;

    private OrderData[] orderDataCollection;

    private MundiPaggSuggestion mundiPaggSuggestion;

    private ErrorReport errorReport;

    public QueryOrderResponse() {
    }

    public QueryOrderResponse(
           java.lang.Long mundiPaggTimeInMilliseconds,
           java.lang.Integer orderDataCount,
           java.lang.String requestKey,
           java.lang.Boolean success,
           OrderData[] orderDataCollection,
           MundiPaggSuggestion mundiPaggSuggestion,
           ErrorReport errorReport) {
           this.mundiPaggTimeInMilliseconds = mundiPaggTimeInMilliseconds;
           this.orderDataCount = orderDataCount;
           this.requestKey = requestKey;
           this.success = success;
           this.orderDataCollection = orderDataCollection;
           this.mundiPaggSuggestion = mundiPaggSuggestion;
           this.errorReport = errorReport;
    }


    /**
     * Gets the mundiPaggTimeInMilliseconds value for this QueryOrderResponse.
     * 
     * @return mundiPaggTimeInMilliseconds
     */
    public java.lang.Long getMundiPaggTimeInMilliseconds() {
        return mundiPaggTimeInMilliseconds;
    }


    /**
     * Sets the mundiPaggTimeInMilliseconds value for this QueryOrderResponse.
     * 
     * @param mundiPaggTimeInMilliseconds
     */
    public void setMundiPaggTimeInMilliseconds(java.lang.Long mundiPaggTimeInMilliseconds) {
        this.mundiPaggTimeInMilliseconds = mundiPaggTimeInMilliseconds;
    }


    /**
     * Gets the orderDataCount value for this QueryOrderResponse.
     * 
     * @return orderDataCount
     */
    public java.lang.Integer getOrderDataCount() {
        return orderDataCount;
    }


    /**
     * Sets the orderDataCount value for this QueryOrderResponse.
     * 
     * @param orderDataCount
     */
    public void setOrderDataCount(java.lang.Integer orderDataCount) {
        this.orderDataCount = orderDataCount;
    }


    /**
     * Gets the requestKey value for this QueryOrderResponse.
     * 
     * @return requestKey
     */
    public java.lang.String getRequestKey() {
        return requestKey;
    }


    /**
     * Sets the requestKey value for this QueryOrderResponse.
     * 
     * @param requestKey
     */
    public void setRequestKey(java.lang.String requestKey) {
        this.requestKey = requestKey;
    }


    /**
     * Gets the success value for this QueryOrderResponse.
     * 
     * @return success
     */
    public java.lang.Boolean getSuccess() {
        return success;
    }


    /**
     * Sets the success value for this QueryOrderResponse.
     * 
     * @param success
     */
    public void setSuccess(java.lang.Boolean success) {
        this.success = success;
    }


    /**
     * Gets the orderDataCollection value for this QueryOrderResponse.
     * 
     * @return orderDataCollection
     */
    public OrderData[] getOrderDataCollection() {
        return orderDataCollection;
    }


    /**
     * Sets the orderDataCollection value for this QueryOrderResponse.
     * 
     * @param orderDataCollection
     */
    public void setOrderDataCollection(OrderData[] orderDataCollection) {
        this.orderDataCollection = orderDataCollection;
    }


    /**
     * Gets the mundiPaggSuggestion value for this QueryOrderResponse.
     * 
     * @return mundiPaggSuggestion
     */
    public MundiPaggSuggestion getMundiPaggSuggestion() {
        return mundiPaggSuggestion;
    }


    /**
     * Sets the mundiPaggSuggestion value for this QueryOrderResponse.
     * 
     * @param mundiPaggSuggestion
     */
    public void setMundiPaggSuggestion(MundiPaggSuggestion mundiPaggSuggestion) {
        this.mundiPaggSuggestion = mundiPaggSuggestion;
    }


    /**
     * Gets the errorReport value for this QueryOrderResponse.
     * 
     * @return errorReport
     */
    public ErrorReport getErrorReport() {
        return errorReport;
    }


    /**
     * Sets the errorReport value for this QueryOrderResponse.
     * 
     * @param errorReport
     */
    public void setErrorReport(ErrorReport errorReport) {
        this.errorReport = errorReport;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof QueryOrderResponse)) return false;
        QueryOrderResponse other = (QueryOrderResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.mundiPaggTimeInMilliseconds==null && other.getMundiPaggTimeInMilliseconds()==null) || 
             (this.mundiPaggTimeInMilliseconds!=null &&
              this.mundiPaggTimeInMilliseconds.equals(other.getMundiPaggTimeInMilliseconds()))) &&
            ((this.orderDataCount==null && other.getOrderDataCount()==null) || 
             (this.orderDataCount!=null &&
              this.orderDataCount.equals(other.getOrderDataCount()))) &&
            ((this.requestKey==null && other.getRequestKey()==null) || 
             (this.requestKey!=null &&
              this.requestKey.equals(other.getRequestKey()))) &&
            ((this.success==null && other.getSuccess()==null) || 
             (this.success!=null &&
              this.success.equals(other.getSuccess()))) &&
            ((this.orderDataCollection==null && other.getOrderDataCollection()==null) || 
             (this.orderDataCollection!=null &&
              java.util.Arrays.equals(this.orderDataCollection, other.getOrderDataCollection()))) &&
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
        if (getMundiPaggTimeInMilliseconds() != null) {
            _hashCode += getMundiPaggTimeInMilliseconds().hashCode();
        }
        if (getOrderDataCount() != null) {
            _hashCode += getOrderDataCount().hashCode();
        }
        if (getRequestKey() != null) {
            _hashCode += getRequestKey().hashCode();
        }
        if (getSuccess() != null) {
            _hashCode += getSuccess().hashCode();
        }
        if (getOrderDataCollection() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOrderDataCollection());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOrderDataCollection(), i);
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
        new org.apache.axis.description.TypeDesc(QueryOrderResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "QueryOrderResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mundiPaggTimeInMilliseconds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "MundiPaggTimeInMilliseconds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("orderDataCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "OrderDataCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
        elemField.setFieldName("orderDataCollection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "OrderDataCollection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "OrderData"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "OrderData"));
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
