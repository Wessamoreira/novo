/**
 * BoletoTransaction.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class BoletoTransaction  implements java.io.Serializable {
    private java.lang.Long amountInCents;

    private java.lang.String bankNumber;

    private java.lang.Integer daysToAddInBoletoExpirationDate;

    private java.lang.String instructions;

    private java.lang.String nossoNumero;

    private java.lang.String transactionReference;

    public BoletoTransaction() {
    }

    public BoletoTransaction(
           java.lang.Long amountInCents,
           java.lang.String bankNumber,
           java.lang.Integer daysToAddInBoletoExpirationDate,
           java.lang.String instructions,
           java.lang.String nossoNumero,
           java.lang.String transactionReference) {
           this.amountInCents = amountInCents;
           this.bankNumber = bankNumber;
           this.daysToAddInBoletoExpirationDate = daysToAddInBoletoExpirationDate;
           this.instructions = instructions;
           this.nossoNumero = nossoNumero;
           this.transactionReference = transactionReference;
    }


    /**
     * Gets the amountInCents value for this BoletoTransaction.
     * 
     * @return amountInCents
     */
    public java.lang.Long getAmountInCents() {
        return amountInCents;
    }


    /**
     * Sets the amountInCents value for this BoletoTransaction.
     * 
     * @param amountInCents
     */
    public void setAmountInCents(java.lang.Long amountInCents) {
        this.amountInCents = amountInCents;
    }


    /**
     * Gets the bankNumber value for this BoletoTransaction.
     * 
     * @return bankNumber
     */
    public java.lang.String getBankNumber() {
        return bankNumber;
    }


    /**
     * Sets the bankNumber value for this BoletoTransaction.
     * 
     * @param bankNumber
     */
    public void setBankNumber(java.lang.String bankNumber) {
        this.bankNumber = bankNumber;
    }


    /**
     * Gets the daysToAddInBoletoExpirationDate value for this BoletoTransaction.
     * 
     * @return daysToAddInBoletoExpirationDate
     */
    public java.lang.Integer getDaysToAddInBoletoExpirationDate() {
        return daysToAddInBoletoExpirationDate;
    }


    /**
     * Sets the daysToAddInBoletoExpirationDate value for this BoletoTransaction.
     * 
     * @param daysToAddInBoletoExpirationDate
     */
    public void setDaysToAddInBoletoExpirationDate(java.lang.Integer daysToAddInBoletoExpirationDate) {
        this.daysToAddInBoletoExpirationDate = daysToAddInBoletoExpirationDate;
    }


    /**
     * Gets the instructions value for this BoletoTransaction.
     * 
     * @return instructions
     */
    public java.lang.String getInstructions() {
        return instructions;
    }


    /**
     * Sets the instructions value for this BoletoTransaction.
     * 
     * @param instructions
     */
    public void setInstructions(java.lang.String instructions) {
        this.instructions = instructions;
    }


    /**
     * Gets the nossoNumero value for this BoletoTransaction.
     * 
     * @return nossoNumero
     */
    public java.lang.String getNossoNumero() {
        return nossoNumero;
    }


    /**
     * Sets the nossoNumero value for this BoletoTransaction.
     * 
     * @param nossoNumero
     */
    public void setNossoNumero(java.lang.String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }


    /**
     * Gets the transactionReference value for this BoletoTransaction.
     * 
     * @return transactionReference
     */
    public java.lang.String getTransactionReference() {
        return transactionReference;
    }


    /**
     * Sets the transactionReference value for this BoletoTransaction.
     * 
     * @param transactionReference
     */
    public void setTransactionReference(java.lang.String transactionReference) {
        this.transactionReference = transactionReference;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BoletoTransaction)) return false;
        BoletoTransaction other = (BoletoTransaction) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.amountInCents==null && other.getAmountInCents()==null) || 
             (this.amountInCents!=null &&
              this.amountInCents.equals(other.getAmountInCents()))) &&
            ((this.bankNumber==null && other.getBankNumber()==null) || 
             (this.bankNumber!=null &&
              this.bankNumber.equals(other.getBankNumber()))) &&
            ((this.daysToAddInBoletoExpirationDate==null && other.getDaysToAddInBoletoExpirationDate()==null) || 
             (this.daysToAddInBoletoExpirationDate!=null &&
              this.daysToAddInBoletoExpirationDate.equals(other.getDaysToAddInBoletoExpirationDate()))) &&
            ((this.instructions==null && other.getInstructions()==null) || 
             (this.instructions!=null &&
              this.instructions.equals(other.getInstructions()))) &&
            ((this.nossoNumero==null && other.getNossoNumero()==null) || 
             (this.nossoNumero!=null &&
              this.nossoNumero.equals(other.getNossoNumero()))) &&
            ((this.transactionReference==null && other.getTransactionReference()==null) || 
             (this.transactionReference!=null &&
              this.transactionReference.equals(other.getTransactionReference())));
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
        if (getAmountInCents() != null) {
            _hashCode += getAmountInCents().hashCode();
        }
        if (getBankNumber() != null) {
            _hashCode += getBankNumber().hashCode();
        }
        if (getDaysToAddInBoletoExpirationDate() != null) {
            _hashCode += getDaysToAddInBoletoExpirationDate().hashCode();
        }
        if (getInstructions() != null) {
            _hashCode += getInstructions().hashCode();
        }
        if (getNossoNumero() != null) {
            _hashCode += getNossoNumero().hashCode();
        }
        if (getTransactionReference() != null) {
            _hashCode += getTransactionReference().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BoletoTransaction.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BoletoTransaction"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("amountInCents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "AmountInCents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bankNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BankNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("daysToAddInBoletoExpirationDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "DaysToAddInBoletoExpirationDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instructions");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "Instructions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nossoNumero");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "NossoNumero"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "TransactionReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
