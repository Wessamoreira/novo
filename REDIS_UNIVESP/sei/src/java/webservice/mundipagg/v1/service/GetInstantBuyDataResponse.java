/**
 * GetInstantBuyDataResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class GetInstantBuyDataResponse  implements java.io.Serializable {
    private java.lang.Integer creditCardDataCount;

    private java.lang.Long mundiPaggTimeInMilliseconds;

    private java.lang.String requestKey;

    private java.lang.Boolean success;

    private CreditCardData[] creditCardDataCollection;

    private MundiPaggSuggestion mundiPaggSuggestion;

    private ErrorReport errorReport;

    public GetInstantBuyDataResponse() {
    }

    public GetInstantBuyDataResponse(
           java.lang.Integer creditCardDataCount,
           java.lang.Long mundiPaggTimeInMilliseconds,
           java.lang.String requestKey,
           java.lang.Boolean success,
           CreditCardData[] creditCardDataCollection,
           MundiPaggSuggestion mundiPaggSuggestion,
           ErrorReport errorReport) {
           this.creditCardDataCount = creditCardDataCount;
           this.mundiPaggTimeInMilliseconds = mundiPaggTimeInMilliseconds;
           this.requestKey = requestKey;
           this.success = success;
           this.creditCardDataCollection = creditCardDataCollection;
           this.mundiPaggSuggestion = mundiPaggSuggestion;
           this.errorReport = errorReport;
    }


    /**
     * Gets the creditCardDataCount value for this GetInstantBuyDataResponse.
     * 
     * @return creditCardDataCount
     */
    public java.lang.Integer getCreditCardDataCount() {
        return creditCardDataCount;
    }


    /**
     * Sets the creditCardDataCount value for this GetInstantBuyDataResponse.
     * 
     * @param creditCardDataCount
     */
    public void setCreditCardDataCount(java.lang.Integer creditCardDataCount) {
        this.creditCardDataCount = creditCardDataCount;
    }


    /**
     * Gets the mundiPaggTimeInMilliseconds value for this GetInstantBuyDataResponse.
     * 
     * @return mundiPaggTimeInMilliseconds
     */
    public java.lang.Long getMundiPaggTimeInMilliseconds() {
        return mundiPaggTimeInMilliseconds;
    }


    /**
     * Sets the mundiPaggTimeInMilliseconds value for this GetInstantBuyDataResponse.
     * 
     * @param mundiPaggTimeInMilliseconds
     */
    public void setMundiPaggTimeInMilliseconds(java.lang.Long mundiPaggTimeInMilliseconds) {
        this.mundiPaggTimeInMilliseconds = mundiPaggTimeInMilliseconds;
    }


    /**
     * Gets the requestKey value for this GetInstantBuyDataResponse.
     * 
     * @return requestKey
     */
    public java.lang.String getRequestKey() {
        return requestKey;
    }


    /**
     * Sets the requestKey value for this GetInstantBuyDataResponse.
     * 
     * @param requestKey
     */
    public void setRequestKey(java.lang.String requestKey) {
        this.requestKey = requestKey;
    }


    /**
     * Gets the success value for this GetInstantBuyDataResponse.
     * 
     * @return success
     */
    public java.lang.Boolean getSuccess() {
        return success;
    }


    /**
     * Sets the success value for this GetInstantBuyDataResponse.
     * 
     * @param success
     */
    public void setSuccess(java.lang.Boolean success) {
        this.success = success;
    }


    /**
     * Gets the creditCardDataCollection value for this GetInstantBuyDataResponse.
     * 
     * @return creditCardDataCollection
     */
    public CreditCardData[] getCreditCardDataCollection() {
        return creditCardDataCollection;
    }


    /**
     * Sets the creditCardDataCollection value for this GetInstantBuyDataResponse.
     * 
     * @param creditCardDataCollection
     */
    public void setCreditCardDataCollection(CreditCardData[] creditCardDataCollection) {
        this.creditCardDataCollection = creditCardDataCollection;
    }


    /**
     * Gets the mundiPaggSuggestion value for this GetInstantBuyDataResponse.
     * 
     * @return mundiPaggSuggestion
     */
    public MundiPaggSuggestion getMundiPaggSuggestion() {
        return mundiPaggSuggestion;
    }


    /**
     * Sets the mundiPaggSuggestion value for this GetInstantBuyDataResponse.
     * 
     * @param mundiPaggSuggestion
     */
    public void setMundiPaggSuggestion(MundiPaggSuggestion mundiPaggSuggestion) {
        this.mundiPaggSuggestion = mundiPaggSuggestion;
    }


    /**
     * Gets the errorReport value for this GetInstantBuyDataResponse.
     * 
     * @return errorReport
     */
    public ErrorReport getErrorReport() {
        return errorReport;
    }


    /**
     * Sets the errorReport value for this GetInstantBuyDataResponse.
     * 
     * @param errorReport
     */
    public void setErrorReport(ErrorReport errorReport) {
        this.errorReport = errorReport;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetInstantBuyDataResponse)) return false;
        GetInstantBuyDataResponse other = (GetInstantBuyDataResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.creditCardDataCount==null && other.getCreditCardDataCount()==null) || 
             (this.creditCardDataCount!=null &&
              this.creditCardDataCount.equals(other.getCreditCardDataCount()))) &&
            ((this.mundiPaggTimeInMilliseconds==null && other.getMundiPaggTimeInMilliseconds()==null) || 
             (this.mundiPaggTimeInMilliseconds!=null &&
              this.mundiPaggTimeInMilliseconds.equals(other.getMundiPaggTimeInMilliseconds()))) &&
            ((this.requestKey==null && other.getRequestKey()==null) || 
             (this.requestKey!=null &&
              this.requestKey.equals(other.getRequestKey()))) &&
            ((this.success==null && other.getSuccess()==null) || 
             (this.success!=null &&
              this.success.equals(other.getSuccess()))) &&
            ((this.creditCardDataCollection==null && other.getCreditCardDataCollection()==null) || 
             (this.creditCardDataCollection!=null &&
              java.util.Arrays.equals(this.creditCardDataCollection, other.getCreditCardDataCollection()))) &&
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
        if (getCreditCardDataCount() != null) {
            _hashCode += getCreditCardDataCount().hashCode();
        }
        if (getMundiPaggTimeInMilliseconds() != null) {
            _hashCode += getMundiPaggTimeInMilliseconds().hashCode();
        }
        if (getRequestKey() != null) {
            _hashCode += getRequestKey().hashCode();
        }
        if (getSuccess() != null) {
            _hashCode += getSuccess().hashCode();
        }
        if (getCreditCardDataCollection() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCreditCardDataCollection());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCreditCardDataCollection(), i);
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
        new org.apache.axis.description.TypeDesc(GetInstantBuyDataResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "GetInstantBuyDataResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("creditCardDataCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardDataCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mundiPaggTimeInMilliseconds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "MundiPaggTimeInMilliseconds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
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
        elemField.setFieldName("creditCardDataCollection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardDataCollection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardData"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardData"));
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
