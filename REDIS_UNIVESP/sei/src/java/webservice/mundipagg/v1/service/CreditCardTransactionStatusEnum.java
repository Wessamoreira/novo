/**
 * CreditCardTransactionStatusEnum.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class CreditCardTransactionStatusEnum implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected CreditCardTransactionStatusEnum(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _AuthorizedPendingCapture = "AuthorizedPendingCapture";
    public static final java.lang.String _NotAuthorized = "NotAuthorized";
    public static final java.lang.String _ChargebackPreview = "ChargebackPreview";
    public static final java.lang.String _RefundPreview = "RefundPreview";
    public static final java.lang.String _DepositPreview = "DepositPreview";
    public static final java.lang.String _Captured = "Captured";
    public static final java.lang.String _PartialCapture = "PartialCapture";
    public static final java.lang.String _Refunded = "Refunded";
    public static final java.lang.String _Voided = "Voided";
    public static final java.lang.String _Deposited = "Deposited";
    public static final java.lang.String _OpenedPendindAuth = "OpenedPendindAuth";
    public static final java.lang.String _Chargedback = "Chargedback";
    public static final java.lang.String _PendingVoid = "PendingVoid";
    public static final java.lang.String _Invalid = "Invalid";
    public static final java.lang.String _PartialAuthorize = "PartialAuthorize";
    public static final java.lang.String _PartialRefunded = "PartialRefunded";
    public static final java.lang.String _OverCapture = "OverCapture";
    public static final java.lang.String _PartialVoid = "PartialVoid";
    public static final java.lang.String _PendingRefund = "PendingRefund";
    public static final java.lang.String _UnScheduled = "UnScheduled";
    public static final java.lang.String _WithError = "WithError";
    public static final CreditCardTransactionStatusEnum AuthorizedPendingCapture = new CreditCardTransactionStatusEnum(_AuthorizedPendingCapture);
    public static final CreditCardTransactionStatusEnum NotAuthorized = new CreditCardTransactionStatusEnum(_NotAuthorized);
    public static final CreditCardTransactionStatusEnum ChargebackPreview = new CreditCardTransactionStatusEnum(_ChargebackPreview);
    public static final CreditCardTransactionStatusEnum RefundPreview = new CreditCardTransactionStatusEnum(_RefundPreview);
    public static final CreditCardTransactionStatusEnum DepositPreview = new CreditCardTransactionStatusEnum(_DepositPreview);
    public static final CreditCardTransactionStatusEnum Captured = new CreditCardTransactionStatusEnum(_Captured);
    public static final CreditCardTransactionStatusEnum PartialCapture = new CreditCardTransactionStatusEnum(_PartialCapture);
    public static final CreditCardTransactionStatusEnum Refunded = new CreditCardTransactionStatusEnum(_Refunded);
    public static final CreditCardTransactionStatusEnum Voided = new CreditCardTransactionStatusEnum(_Voided);
    public static final CreditCardTransactionStatusEnum Deposited = new CreditCardTransactionStatusEnum(_Deposited);
    public static final CreditCardTransactionStatusEnum OpenedPendindAuth = new CreditCardTransactionStatusEnum(_OpenedPendindAuth);
    public static final CreditCardTransactionStatusEnum Chargedback = new CreditCardTransactionStatusEnum(_Chargedback);
    public static final CreditCardTransactionStatusEnum PendingVoid = new CreditCardTransactionStatusEnum(_PendingVoid);
    public static final CreditCardTransactionStatusEnum Invalid = new CreditCardTransactionStatusEnum(_Invalid);
    public static final CreditCardTransactionStatusEnum PartialAuthorize = new CreditCardTransactionStatusEnum(_PartialAuthorize);
    public static final CreditCardTransactionStatusEnum PartialRefunded = new CreditCardTransactionStatusEnum(_PartialRefunded);
    public static final CreditCardTransactionStatusEnum OverCapture = new CreditCardTransactionStatusEnum(_OverCapture);
    public static final CreditCardTransactionStatusEnum PartialVoid = new CreditCardTransactionStatusEnum(_PartialVoid);
    public static final CreditCardTransactionStatusEnum PendingRefund = new CreditCardTransactionStatusEnum(_PendingRefund);
    public static final CreditCardTransactionStatusEnum UnScheduled = new CreditCardTransactionStatusEnum(_UnScheduled);
    public static final CreditCardTransactionStatusEnum WithError = new CreditCardTransactionStatusEnum(_WithError);
    public java.lang.String getValue() { return _value_;}
    public static CreditCardTransactionStatusEnum fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        CreditCardTransactionStatusEnum enumeration = (CreditCardTransactionStatusEnum)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static CreditCardTransactionStatusEnum fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(CreditCardTransactionStatusEnum.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreditCardTransactionStatusEnum"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
