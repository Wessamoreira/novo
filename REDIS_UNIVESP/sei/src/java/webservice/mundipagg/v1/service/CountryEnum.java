/**
 * CountryEnum.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public class CountryEnum implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected CountryEnum(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Brazil = "Brazil";
    public static final java.lang.String _USA = "USA";
    public static final java.lang.String _Argentina = "Argentina";
    public static final java.lang.String _Bolivia = "Bolivia";
    public static final java.lang.String _Chile = "Chile";
    public static final java.lang.String _Colombia = "Colombia";
    public static final java.lang.String _Uruguay = "Uruguay";
    public static final java.lang.String _Mexico = "Mexico";
    public static final java.lang.String _Paraguay = "Paraguay";
    public static final CountryEnum Brazil = new CountryEnum(_Brazil);
    public static final CountryEnum USA = new CountryEnum(_USA);
    public static final CountryEnum Argentina = new CountryEnum(_Argentina);
    public static final CountryEnum Bolivia = new CountryEnum(_Bolivia);
    public static final CountryEnum Chile = new CountryEnum(_Chile);
    public static final CountryEnum Colombia = new CountryEnum(_Colombia);
    public static final CountryEnum Uruguay = new CountryEnum(_Uruguay);
    public static final CountryEnum Mexico = new CountryEnum(_Mexico);
    public static final CountryEnum Paraguay = new CountryEnum(_Paraguay);
    public java.lang.String getValue() { return _value_;}
    public static CountryEnum fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        CountryEnum enumeration = (CountryEnum)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static CountryEnum fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(CountryEnum.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CountryEnum"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
