/**
 * ShoppingCartItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class ShoppingCartItem  implements java.io.Serializable {
    private java.lang.String description;

    private java.lang.String itemReference;

    private java.lang.String name;

    private java.lang.Integer quantity;

    private java.lang.Integer totalCostInCents;

    private java.lang.Integer unitCostInCents;

    public ShoppingCartItem() {
    }

    public ShoppingCartItem(
           java.lang.String description,
           java.lang.String itemReference,
           java.lang.String name,
           java.lang.Integer quantity,
           java.lang.Integer totalCostInCents,
           java.lang.Integer unitCostInCents) {
           this.description = description;
           this.itemReference = itemReference;
           this.name = name;
           this.quantity = quantity;
           this.totalCostInCents = totalCostInCents;
           this.unitCostInCents = unitCostInCents;
    }


    /**
     * Gets the description value for this ShoppingCartItem.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this ShoppingCartItem.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the itemReference value for this ShoppingCartItem.
     * 
     * @return itemReference
     */
    public java.lang.String getItemReference() {
        return itemReference;
    }


    /**
     * Sets the itemReference value for this ShoppingCartItem.
     * 
     * @param itemReference
     */
    public void setItemReference(java.lang.String itemReference) {
        this.itemReference = itemReference;
    }


    /**
     * Gets the name value for this ShoppingCartItem.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this ShoppingCartItem.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the quantity value for this ShoppingCartItem.
     * 
     * @return quantity
     */
    public java.lang.Integer getQuantity() {
        return quantity;
    }


    /**
     * Sets the quantity value for this ShoppingCartItem.
     * 
     * @param quantity
     */
    public void setQuantity(java.lang.Integer quantity) {
        this.quantity = quantity;
    }


    /**
     * Gets the totalCostInCents value for this ShoppingCartItem.
     * 
     * @return totalCostInCents
     */
    public java.lang.Integer getTotalCostInCents() {
        return totalCostInCents;
    }


    /**
     * Sets the totalCostInCents value for this ShoppingCartItem.
     * 
     * @param totalCostInCents
     */
    public void setTotalCostInCents(java.lang.Integer totalCostInCents) {
        this.totalCostInCents = totalCostInCents;
    }


    /**
     * Gets the unitCostInCents value for this ShoppingCartItem.
     * 
     * @return unitCostInCents
     */
    public java.lang.Integer getUnitCostInCents() {
        return unitCostInCents;
    }


    /**
     * Sets the unitCostInCents value for this ShoppingCartItem.
     * 
     * @param unitCostInCents
     */
    public void setUnitCostInCents(java.lang.Integer unitCostInCents) {
        this.unitCostInCents = unitCostInCents;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ShoppingCartItem)) return false;
        ShoppingCartItem other = (ShoppingCartItem) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.itemReference==null && other.getItemReference()==null) || 
             (this.itemReference!=null &&
              this.itemReference.equals(other.getItemReference()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.quantity==null && other.getQuantity()==null) || 
             (this.quantity!=null &&
              this.quantity.equals(other.getQuantity()))) &&
            ((this.totalCostInCents==null && other.getTotalCostInCents()==null) || 
             (this.totalCostInCents!=null &&
              this.totalCostInCents.equals(other.getTotalCostInCents()))) &&
            ((this.unitCostInCents==null && other.getUnitCostInCents()==null) || 
             (this.unitCostInCents!=null &&
              this.unitCostInCents.equals(other.getUnitCostInCents())));
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
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getItemReference() != null) {
            _hashCode += getItemReference().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getQuantity() != null) {
            _hashCode += getQuantity().hashCode();
        }
        if (getTotalCostInCents() != null) {
            _hashCode += getTotalCostInCents().hashCode();
        }
        if (getUnitCostInCents() != null) {
            _hashCode += getUnitCostInCents().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ShoppingCartItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ShoppingCartItem"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("itemReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "ItemReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quantity");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "Quantity"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalCostInCents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "TotalCostInCents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unitCostInCents");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "UnitCostInCents"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
