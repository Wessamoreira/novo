/**
 * Recurrency.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class Recurrency  implements java.io.Serializable {
    private java.util.Calendar dateToStartBilling;

    private FrequencyEnum frequencyEnum;

    private java.lang.Integer interval;

    private java.lang.Boolean oneDollarAuth;

    private java.lang.Integer recurrences;

    public Recurrency() {
    }

    public Recurrency(
           java.util.Calendar dateToStartBilling,
           FrequencyEnum frequencyEnum,
           java.lang.Integer interval,
           java.lang.Boolean oneDollarAuth,
           java.lang.Integer recurrences) {
           this.dateToStartBilling = dateToStartBilling;
           this.frequencyEnum = frequencyEnum;
           this.interval = interval;
           this.oneDollarAuth = oneDollarAuth;
           this.recurrences = recurrences;
    }


    /**
     * Gets the dateToStartBilling value for this Recurrency.
     * 
     * @return dateToStartBilling
     */
    public java.util.Calendar getDateToStartBilling() {
        return dateToStartBilling;
    }


    /**
     * Sets the dateToStartBilling value for this Recurrency.
     * 
     * @param dateToStartBilling
     */
    public void setDateToStartBilling(java.util.Calendar dateToStartBilling) {
        this.dateToStartBilling = dateToStartBilling;
    }


    /**
     * Gets the frequencyEnum value for this Recurrency.
     * 
     * @return frequencyEnum
     */
    public FrequencyEnum getFrequencyEnum() {
        return frequencyEnum;
    }


    /**
     * Sets the frequencyEnum value for this Recurrency.
     * 
     * @param frequencyEnum
     */
    public void setFrequencyEnum(FrequencyEnum frequencyEnum) {
        this.frequencyEnum = frequencyEnum;
    }


    /**
     * Gets the interval value for this Recurrency.
     * 
     * @return interval
     */
    public java.lang.Integer getInterval() {
        return interval;
    }


    /**
     * Sets the interval value for this Recurrency.
     * 
     * @param interval
     */
    public void setInterval(java.lang.Integer interval) {
        this.interval = interval;
    }


    /**
     * Gets the oneDollarAuth value for this Recurrency.
     * 
     * @return oneDollarAuth
     */
    public java.lang.Boolean getOneDollarAuth() {
        return oneDollarAuth;
    }


    /**
     * Sets the oneDollarAuth value for this Recurrency.
     * 
     * @param oneDollarAuth
     */
    public void setOneDollarAuth(java.lang.Boolean oneDollarAuth) {
        this.oneDollarAuth = oneDollarAuth;
    }


    /**
     * Gets the recurrences value for this Recurrency.
     * 
     * @return recurrences
     */
    public java.lang.Integer getRecurrences() {
        return recurrences;
    }


    /**
     * Sets the recurrences value for this Recurrency.
     * 
     * @param recurrences
     */
    public void setRecurrences(java.lang.Integer recurrences) {
        this.recurrences = recurrences;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Recurrency)) return false;
        Recurrency other = (Recurrency) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dateToStartBilling==null && other.getDateToStartBilling()==null) || 
             (this.dateToStartBilling!=null &&
              this.dateToStartBilling.equals(other.getDateToStartBilling()))) &&
            ((this.frequencyEnum==null && other.getFrequencyEnum()==null) || 
             (this.frequencyEnum!=null &&
              this.frequencyEnum.equals(other.getFrequencyEnum()))) &&
            ((this.interval==null && other.getInterval()==null) || 
             (this.interval!=null &&
              this.interval.equals(other.getInterval()))) &&
            ((this.oneDollarAuth==null && other.getOneDollarAuth()==null) || 
             (this.oneDollarAuth!=null &&
              this.oneDollarAuth.equals(other.getOneDollarAuth()))) &&
            ((this.recurrences==null && other.getRecurrences()==null) || 
             (this.recurrences!=null &&
              this.recurrences.equals(other.getRecurrences())));
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
        if (getDateToStartBilling() != null) {
            _hashCode += getDateToStartBilling().hashCode();
        }
        if (getFrequencyEnum() != null) {
            _hashCode += getFrequencyEnum().hashCode();
        }
        if (getInterval() != null) {
            _hashCode += getInterval().hashCode();
        }
        if (getOneDollarAuth() != null) {
            _hashCode += getOneDollarAuth().hashCode();
        }
        if (getRecurrences() != null) {
            _hashCode += getRecurrences().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Recurrency.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "Recurrency"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateToStartBilling");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "DateToStartBilling"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("frequencyEnum");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "FrequencyEnum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "FrequencyEnum"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("interval");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "Interval"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("oneDollarAuth");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "OneDollarAuth"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("recurrences");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "Recurrences"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
