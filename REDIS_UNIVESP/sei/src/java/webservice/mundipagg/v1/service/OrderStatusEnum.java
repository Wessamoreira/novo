/**
 * OrderStatusEnum.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class OrderStatusEnum implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected OrderStatusEnum(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Opened = "Opened";
    public static final java.lang.String _Closed = "Closed";
    public static final java.lang.String _Paid = "Paid";
    public static final java.lang.String _Overpaid = "Overpaid";
    public static final java.lang.String _Canceled = "Canceled";
    public static final java.lang.String _PartialPaid = "PartialPaid";
    public static final java.lang.String _WithError = "WithError";
    public static final OrderStatusEnum Opened = new OrderStatusEnum(_Opened);
    public static final OrderStatusEnum Closed = new OrderStatusEnum(_Closed);
    public static final OrderStatusEnum Paid = new OrderStatusEnum(_Paid);
    public static final OrderStatusEnum Overpaid = new OrderStatusEnum(_Overpaid);
    public static final OrderStatusEnum Canceled = new OrderStatusEnum(_Canceled);
    public static final OrderStatusEnum PartialPaid = new OrderStatusEnum(_PartialPaid);
    public static final OrderStatusEnum WithError = new OrderStatusEnum(_WithError);
    public java.lang.String getValue() { return _value_;}
    public static OrderStatusEnum fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        OrderStatusEnum enumeration = (OrderStatusEnum)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static OrderStatusEnum fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(OrderStatusEnum.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "OrderStatusEnum"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
