/**
 * CreditCardBrandEnum.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class CreditCardBrandEnum implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected CreditCardBrandEnum(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Visa = "Visa";
    public static final java.lang.String _Mastercard = "Mastercard";
    public static final java.lang.String _Hipercard = "Hipercard";
    public static final java.lang.String _Amex = "Amex";
    public static final java.lang.String _Diners = "Diners";
    public static final java.lang.String _Elo = "Elo";
    public static final java.lang.String _Aura = "Aura";
    public static final java.lang.String _Discover = "Discover";
    public static final java.lang.String _CasaShow = "CasaShow";
    public static final java.lang.String _Havan = "Havan";
    public static final CreditCardBrandEnum Visa = new CreditCardBrandEnum(_Visa);
    public static final CreditCardBrandEnum Mastercard = new CreditCardBrandEnum(_Mastercard);
    public static final CreditCardBrandEnum Hipercard = new CreditCardBrandEnum(_Hipercard);
    public static final CreditCardBrandEnum Amex = new CreditCardBrandEnum(_Amex);
    public static final CreditCardBrandEnum Diners = new CreditCardBrandEnum(_Diners);
    public static final CreditCardBrandEnum Elo = new CreditCardBrandEnum(_Elo);
    public static final CreditCardBrandEnum Aura = new CreditCardBrandEnum(_Aura);
    public static final CreditCardBrandEnum Discover = new CreditCardBrandEnum(_Discover);
    public static final CreditCardBrandEnum CasaShow = new CreditCardBrandEnum(_CasaShow);
    public static final CreditCardBrandEnum Havan = new CreditCardBrandEnum(_Havan);
    public java.lang.String getValue() { return _value_;}
    public static CreditCardBrandEnum fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        CreditCardBrandEnum enumeration = (CreditCardBrandEnum)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static CreditCardBrandEnum fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(CreditCardBrandEnum.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardBrandEnum"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
