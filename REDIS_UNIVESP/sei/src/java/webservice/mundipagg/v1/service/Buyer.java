/**
 * Buyer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package webservice.mundipagg.v1.service;

public class Buyer  implements java.io.Serializable {
    private java.lang.String buyerKey;

    private java.lang.String buyerReference;

    private java.util.Calendar createDateInMerchant;

    private java.lang.String email;

    private java.lang.String facebookId;

    private GenderEnum genderEnum;

    private java.lang.String homePhone;

    private java.lang.String ipAddress;

    private java.util.Calendar lastBuyerUpdateInMerchant;

    private java.lang.String mobilePhone;

    private java.lang.String name;

    private PersonTypeEnum personTypeEnum;

    private java.lang.String taxDocumentNumber;

    private TaxDocumentTypeEnum taxDocumentTypeEnum;

    private java.lang.String twitterId;

    private java.lang.String workPhone;

    private BuyerAddress[] buyerAddressCollection;

    public Buyer() {
    }

    public Buyer(
           java.lang.String buyerKey,
           java.lang.String buyerReference,
           java.util.Calendar createDateInMerchant,
           java.lang.String email,
           java.lang.String facebookId,
           GenderEnum genderEnum,
           java.lang.String homePhone,
           java.lang.String ipAddress,
           java.util.Calendar lastBuyerUpdateInMerchant,
           java.lang.String mobilePhone,
           java.lang.String name,
           PersonTypeEnum personTypeEnum,
           java.lang.String taxDocumentNumber,
           TaxDocumentTypeEnum taxDocumentTypeEnum,
           java.lang.String twitterId,
           java.lang.String workPhone,
           BuyerAddress[] buyerAddressCollection) {
           this.buyerKey = buyerKey;
           this.buyerReference = buyerReference;
           this.createDateInMerchant = createDateInMerchant;
           this.email = email;
           this.facebookId = facebookId;
           this.genderEnum = genderEnum;
           this.homePhone = homePhone;
           this.ipAddress = ipAddress;
           this.lastBuyerUpdateInMerchant = lastBuyerUpdateInMerchant;
           this.mobilePhone = mobilePhone;
           this.name = name;
           this.personTypeEnum = personTypeEnum;
           this.taxDocumentNumber = taxDocumentNumber;
           this.taxDocumentTypeEnum = taxDocumentTypeEnum;
           this.twitterId = twitterId;
           this.workPhone = workPhone;
           this.buyerAddressCollection = buyerAddressCollection;
    }


    /**
     * Gets the buyerKey value for this Buyer.
     * 
     * @return buyerKey
     */
    public java.lang.String getBuyerKey() {
        return buyerKey;
    }


    /**
     * Sets the buyerKey value for this Buyer.
     * 
     * @param buyerKey
     */
    public void setBuyerKey(java.lang.String buyerKey) {
        this.buyerKey = buyerKey;
    }


    /**
     * Gets the buyerReference value for this Buyer.
     * 
     * @return buyerReference
     */
    public java.lang.String getBuyerReference() {
        return buyerReference;
    }


    /**
     * Sets the buyerReference value for this Buyer.
     * 
     * @param buyerReference
     */
    public void setBuyerReference(java.lang.String buyerReference) {
        this.buyerReference = buyerReference;
    }


    /**
     * Gets the createDateInMerchant value for this Buyer.
     * 
     * @return createDateInMerchant
     */
    public java.util.Calendar getCreateDateInMerchant() {
        return createDateInMerchant;
    }


    /**
     * Sets the createDateInMerchant value for this Buyer.
     * 
     * @param createDateInMerchant
     */
    public void setCreateDateInMerchant(java.util.Calendar createDateInMerchant) {
        this.createDateInMerchant = createDateInMerchant;
    }


    /**
     * Gets the email value for this Buyer.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this Buyer.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the facebookId value for this Buyer.
     * 
     * @return facebookId
     */
    public java.lang.String getFacebookId() {
        return facebookId;
    }


    /**
     * Sets the facebookId value for this Buyer.
     * 
     * @param facebookId
     */
    public void setFacebookId(java.lang.String facebookId) {
        this.facebookId = facebookId;
    }


    /**
     * Gets the genderEnum value for this Buyer.
     * 
     * @return genderEnum
     */
    public GenderEnum getGenderEnum() {
        return genderEnum;
    }


    /**
     * Sets the genderEnum value for this Buyer.
     * 
     * @param genderEnum
     */
    public void setGenderEnum(GenderEnum genderEnum) {
        this.genderEnum = genderEnum;
    }


    /**
     * Gets the homePhone value for this Buyer.
     * 
     * @return homePhone
     */
    public java.lang.String getHomePhone() {
        return homePhone;
    }


    /**
     * Sets the homePhone value for this Buyer.
     * 
     * @param homePhone
     */
    public void setHomePhone(java.lang.String homePhone) {
        this.homePhone = homePhone;
    }


    /**
     * Gets the ipAddress value for this Buyer.
     * 
     * @return ipAddress
     */
    public java.lang.String getIpAddress() {
        return ipAddress;
    }


    /**
     * Sets the ipAddress value for this Buyer.
     * 
     * @param ipAddress
     */
    public void setIpAddress(java.lang.String ipAddress) {
        this.ipAddress = ipAddress;
    }


    /**
     * Gets the lastBuyerUpdateInMerchant value for this Buyer.
     * 
     * @return lastBuyerUpdateInMerchant
     */
    public java.util.Calendar getLastBuyerUpdateInMerchant() {
        return lastBuyerUpdateInMerchant;
    }


    /**
     * Sets the lastBuyerUpdateInMerchant value for this Buyer.
     * 
     * @param lastBuyerUpdateInMerchant
     */
    public void setLastBuyerUpdateInMerchant(java.util.Calendar lastBuyerUpdateInMerchant) {
        this.lastBuyerUpdateInMerchant = lastBuyerUpdateInMerchant;
    }


    /**
     * Gets the mobilePhone value for this Buyer.
     * 
     * @return mobilePhone
     */
    public java.lang.String getMobilePhone() {
        return mobilePhone;
    }


    /**
     * Sets the mobilePhone value for this Buyer.
     * 
     * @param mobilePhone
     */
    public void setMobilePhone(java.lang.String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }


    /**
     * Gets the name value for this Buyer.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this Buyer.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the personTypeEnum value for this Buyer.
     * 
     * @return personTypeEnum
     */
    public PersonTypeEnum getPersonTypeEnum() {
        return personTypeEnum;
    }


    /**
     * Sets the personTypeEnum value for this Buyer.
     * 
     * @param personTypeEnum
     */
    public void setPersonTypeEnum(PersonTypeEnum personTypeEnum) {
        this.personTypeEnum = personTypeEnum;
    }


    /**
     * Gets the taxDocumentNumber value for this Buyer.
     * 
     * @return taxDocumentNumber
     */
    public java.lang.String getTaxDocumentNumber() {
        return taxDocumentNumber;
    }


    /**
     * Sets the taxDocumentNumber value for this Buyer.
     * 
     * @param taxDocumentNumber
     */
    public void setTaxDocumentNumber(java.lang.String taxDocumentNumber) {
        this.taxDocumentNumber = taxDocumentNumber;
    }


    /**
     * Gets the taxDocumentTypeEnum value for this Buyer.
     * 
     * @return taxDocumentTypeEnum
     */
    public TaxDocumentTypeEnum getTaxDocumentTypeEnum() {
        return taxDocumentTypeEnum;
    }


    /**
     * Sets the taxDocumentTypeEnum value for this Buyer.
     * 
     * @param taxDocumentTypeEnum
     */
    public void setTaxDocumentTypeEnum(TaxDocumentTypeEnum taxDocumentTypeEnum) {
        this.taxDocumentTypeEnum = taxDocumentTypeEnum;
    }


    /**
     * Gets the twitterId value for this Buyer.
     * 
     * @return twitterId
     */
    public java.lang.String getTwitterId() {
        return twitterId;
    }


    /**
     * Sets the twitterId value for this Buyer.
     * 
     * @param twitterId
     */
    public void setTwitterId(java.lang.String twitterId) {
        this.twitterId = twitterId;
    }


    /**
     * Gets the workPhone value for this Buyer.
     * 
     * @return workPhone
     */
    public java.lang.String getWorkPhone() {
        return workPhone;
    }


    /**
     * Sets the workPhone value for this Buyer.
     * 
     * @param workPhone
     */
    public void setWorkPhone(java.lang.String workPhone) {
        this.workPhone = workPhone;
    }


    /**
     * Gets the buyerAddressCollection value for this Buyer.
     * 
     * @return buyerAddressCollection
     */
    public BuyerAddress[] getBuyerAddressCollection() {
        return buyerAddressCollection;
    }


    /**
     * Sets the buyerAddressCollection value for this Buyer.
     * 
     * @param buyerAddressCollection
     */
    public void setBuyerAddressCollection(BuyerAddress[] buyerAddressCollection) {
        this.buyerAddressCollection = buyerAddressCollection;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Buyer)) return false;
        Buyer other = (Buyer) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.buyerKey==null && other.getBuyerKey()==null) || 
             (this.buyerKey!=null &&
              this.buyerKey.equals(other.getBuyerKey()))) &&
            ((this.buyerReference==null && other.getBuyerReference()==null) || 
             (this.buyerReference!=null &&
              this.buyerReference.equals(other.getBuyerReference()))) &&
            ((this.createDateInMerchant==null && other.getCreateDateInMerchant()==null) || 
             (this.createDateInMerchant!=null &&
              this.createDateInMerchant.equals(other.getCreateDateInMerchant()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.facebookId==null && other.getFacebookId()==null) || 
             (this.facebookId!=null &&
              this.facebookId.equals(other.getFacebookId()))) &&
            ((this.genderEnum==null && other.getGenderEnum()==null) || 
             (this.genderEnum!=null &&
              this.genderEnum.equals(other.getGenderEnum()))) &&
            ((this.homePhone==null && other.getHomePhone()==null) || 
             (this.homePhone!=null &&
              this.homePhone.equals(other.getHomePhone()))) &&
            ((this.ipAddress==null && other.getIpAddress()==null) || 
             (this.ipAddress!=null &&
              this.ipAddress.equals(other.getIpAddress()))) &&
            ((this.lastBuyerUpdateInMerchant==null && other.getLastBuyerUpdateInMerchant()==null) || 
             (this.lastBuyerUpdateInMerchant!=null &&
              this.lastBuyerUpdateInMerchant.equals(other.getLastBuyerUpdateInMerchant()))) &&
            ((this.mobilePhone==null && other.getMobilePhone()==null) || 
             (this.mobilePhone!=null &&
              this.mobilePhone.equals(other.getMobilePhone()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.personTypeEnum==null && other.getPersonTypeEnum()==null) || 
             (this.personTypeEnum!=null &&
              this.personTypeEnum.equals(other.getPersonTypeEnum()))) &&
            ((this.taxDocumentNumber==null && other.getTaxDocumentNumber()==null) || 
             (this.taxDocumentNumber!=null &&
              this.taxDocumentNumber.equals(other.getTaxDocumentNumber()))) &&
            ((this.taxDocumentTypeEnum==null && other.getTaxDocumentTypeEnum()==null) || 
             (this.taxDocumentTypeEnum!=null &&
              this.taxDocumentTypeEnum.equals(other.getTaxDocumentTypeEnum()))) &&
            ((this.twitterId==null && other.getTwitterId()==null) || 
             (this.twitterId!=null &&
              this.twitterId.equals(other.getTwitterId()))) &&
            ((this.workPhone==null && other.getWorkPhone()==null) || 
             (this.workPhone!=null &&
              this.workPhone.equals(other.getWorkPhone()))) &&
            ((this.buyerAddressCollection==null && other.getBuyerAddressCollection()==null) || 
             (this.buyerAddressCollection!=null &&
              java.util.Arrays.equals(this.buyerAddressCollection, other.getBuyerAddressCollection())));
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
        if (getBuyerKey() != null) {
            _hashCode += getBuyerKey().hashCode();
        }
        if (getBuyerReference() != null) {
            _hashCode += getBuyerReference().hashCode();
        }
        if (getCreateDateInMerchant() != null) {
            _hashCode += getCreateDateInMerchant().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getFacebookId() != null) {
            _hashCode += getFacebookId().hashCode();
        }
        if (getGenderEnum() != null) {
            _hashCode += getGenderEnum().hashCode();
        }
        if (getHomePhone() != null) {
            _hashCode += getHomePhone().hashCode();
        }
        if (getIpAddress() != null) {
            _hashCode += getIpAddress().hashCode();
        }
        if (getLastBuyerUpdateInMerchant() != null) {
            _hashCode += getLastBuyerUpdateInMerchant().hashCode();
        }
        if (getMobilePhone() != null) {
            _hashCode += getMobilePhone().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getPersonTypeEnum() != null) {
            _hashCode += getPersonTypeEnum().hashCode();
        }
        if (getTaxDocumentNumber() != null) {
            _hashCode += getTaxDocumentNumber().hashCode();
        }
        if (getTaxDocumentTypeEnum() != null) {
            _hashCode += getTaxDocumentTypeEnum().hashCode();
        }
        if (getTwitterId() != null) {
            _hashCode += getTwitterId().hashCode();
        }
        if (getWorkPhone() != null) {
            _hashCode += getWorkPhone().hashCode();
        }
        if (getBuyerAddressCollection() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getBuyerAddressCollection());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getBuyerAddressCollection(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Buyer.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "Buyer"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("buyerKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BuyerKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("buyerReference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BuyerReference"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("createDateInMerchant");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "CreateDateInMerchant"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "Email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("facebookId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "FacebookId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("genderEnum");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "GenderEnum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "GenderEnum"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("homePhone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "HomePhone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ipAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "IpAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastBuyerUpdateInMerchant");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "LastBuyerUpdateInMerchant"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mobilePhone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "MobilePhone"));
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
        elemField.setFieldName("personTypeEnum");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "PersonTypeEnum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "PersonTypeEnum"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taxDocumentNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "TaxDocumentNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taxDocumentTypeEnum");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "TaxDocumentTypeEnum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "TaxDocumentTypeEnum"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("twitterId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "TwitterId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workPhone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "WorkPhone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("buyerAddressCollection");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BuyerAddressCollection"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BuyerAddress"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/MundiPagg.One.Service.DataContracts", "BuyerAddress"));
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
