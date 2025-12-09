/**
 * EmailUpdateToBuyerEnum.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public class EmailUpdateToBuyerEnum implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected EmailUpdateToBuyerEnum(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Yes = "Yes";
    public static final java.lang.String _No = "No";
    public static final java.lang.String _YesIfAuthorized = "YesIfAuthorized";
    public static final java.lang.String _YesIfNotAuthorized = "YesIfNotAuthorized";
    public static final EmailUpdateToBuyerEnum Yes = new EmailUpdateToBuyerEnum(_Yes);
    public static final EmailUpdateToBuyerEnum No = new EmailUpdateToBuyerEnum(_No);
    public static final EmailUpdateToBuyerEnum YesIfAuthorized = new EmailUpdateToBuyerEnum(_YesIfAuthorized);
    public static final EmailUpdateToBuyerEnum YesIfNotAuthorized = new EmailUpdateToBuyerEnum(_YesIfNotAuthorized);
    public java.lang.String getValue() { return _value_;}
    public static EmailUpdateToBuyerEnum fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        EmailUpdateToBuyerEnum enumeration = (EmailUpdateToBuyerEnum)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static EmailUpdateToBuyerEnum fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(EmailUpdateToBuyerEnum.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "EmailUpdateToBuyerEnum"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
