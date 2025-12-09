/**
 * CurrencyIsoEnum.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlEnum(String.class)
public class CurrencyIsoEnum implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected CurrencyIsoEnum(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _BRL = "BRL";
    public static final java.lang.String _EUR = "EUR";
    public static final java.lang.String _USD = "USD";
    public static final java.lang.String _ARS = "ARS";
    public static final java.lang.String _BOB = "BOB";
    public static final java.lang.String _CLP = "CLP";
    public static final java.lang.String _COP = "COP";
    public static final java.lang.String _UYU = "UYU";
    public static final java.lang.String _MXN = "MXN";
    public static final java.lang.String _PYG = "PYG";
    public static final CurrencyIsoEnum BRL = new CurrencyIsoEnum(_BRL);
    public static final CurrencyIsoEnum EUR = new CurrencyIsoEnum(_EUR);
    public static final CurrencyIsoEnum USD = new CurrencyIsoEnum(_USD);
    public static final CurrencyIsoEnum ARS = new CurrencyIsoEnum(_ARS);
    public static final CurrencyIsoEnum BOB = new CurrencyIsoEnum(_BOB);
    public static final CurrencyIsoEnum CLP = new CurrencyIsoEnum(_CLP);
    public static final CurrencyIsoEnum COP = new CurrencyIsoEnum(_COP);
    public static final CurrencyIsoEnum UYU = new CurrencyIsoEnum(_UYU);
    public static final CurrencyIsoEnum MXN = new CurrencyIsoEnum(_MXN);
    public static final CurrencyIsoEnum PYG = new CurrencyIsoEnum(_PYG);
    public java.lang.String getValue() { return _value_;}
    public static CurrencyIsoEnum fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        CurrencyIsoEnum enumeration = (CurrencyIsoEnum)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static CurrencyIsoEnum fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CurrencyIsoEnum.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CurrencyIsoEnum"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
