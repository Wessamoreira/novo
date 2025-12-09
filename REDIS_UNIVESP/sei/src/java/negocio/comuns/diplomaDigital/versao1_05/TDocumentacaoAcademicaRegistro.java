//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.20 às 10:57:34 AM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Tipo Documentação Acadêmica para Emissão e Registro
 * 
 * <p>Classe Java de TDocumentacaoAcademicaRegistro complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDocumentacaoAcademicaRegistro">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="RegistroReq" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TRegistroReq"/>
 *           &lt;element name="RegistroReqNSF" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TRegistroReqNSF"/>
 *           &lt;element name="RegistroSegundaViaReq" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TRegistroSegundaViaReq"/>
 *           &lt;element name="RegistroPorDecisaoJudicialReq" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TRegistroPorDecisaoJudicialReq"/>
 *         &lt;/choice>
 *         &lt;sequence>
 *           &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDocumentacaoAcademicaRegistro", propOrder = {
    "registroReq",
    "registroReqNSF",
    "registroSegundaViaReq",
    "registroPorDecisaoJudicialReq",
    "signature"
})
@XmlRootElement(name = "DocumentacaoAcademicaRegistro")
public class TDocumentacaoAcademicaRegistro {

    @XmlElement(name = "RegistroReq")
    protected TRegistroReq registroReq;
    @XmlElement(name = "RegistroReqNSF")
    protected TRegistroReqNSF registroReqNSF;
    @XmlElement(name = "RegistroSegundaViaReq")
    protected TRegistroSegundaViaReq registroSegundaViaReq;
    @XmlElement(name = "RegistroPorDecisaoJudicialReq")
    protected TRegistroPorDecisaoJudicialReq registroPorDecisaoJudicialReq;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected SignatureType signature;

    /**
     * Obtém o valor da propriedade registroReq.
     * 
     * @return
     *     possible object is
     *     {@link TRegistroReq }
     *     
     */
    public TRegistroReq getRegistroReq() {
        return registroReq;
    }

    /**
     * Define o valor da propriedade registroReq.
     * 
     * @param value
     *     allowed object is
     *     {@link TRegistroReq }
     *     
     */
    public void setRegistroReq(TRegistroReq value) {
        this.registroReq = value;
    }

    /**
     * Obtém o valor da propriedade registroReqNSF.
     * 
     * @return
     *     possible object is
     *     {@link TRegistroReqNSF }
     *     
     */
    public TRegistroReqNSF getRegistroReqNSF() {
        return registroReqNSF;
    }

    /**
     * Define o valor da propriedade registroReqNSF.
     * 
     * @param value
     *     allowed object is
     *     {@link TRegistroReqNSF }
     *     
     */
    public void setRegistroReqNSF(TRegistroReqNSF value) {
        this.registroReqNSF = value;
    }

    /**
     * Obtém o valor da propriedade registroSegundaViaReq.
     * 
     * @return
     *     possible object is
     *     {@link TRegistroSegundaViaReq }
     *     
     */
    public TRegistroSegundaViaReq getRegistroSegundaViaReq() {
        return registroSegundaViaReq;
    }

    /**
     * Define o valor da propriedade registroSegundaViaReq.
     * 
     * @param value
     *     allowed object is
     *     {@link TRegistroSegundaViaReq }
     *     
     */
    public void setRegistroSegundaViaReq(TRegistroSegundaViaReq value) {
        this.registroSegundaViaReq = value;
    }

    /**
     * Obtém o valor da propriedade registroPorDecisaoJudicialReq.
     * 
     * @return
     *     possible object is
     *     {@link TRegistroPorDecisaoJudicialReq }
     *     
     */
    public TRegistroPorDecisaoJudicialReq getRegistroPorDecisaoJudicialReq() {
        return registroPorDecisaoJudicialReq;
    }

    /**
     * Define o valor da propriedade registroPorDecisaoJudicialReq.
     * 
     * @param value
     *     allowed object is
     *     {@link TRegistroPorDecisaoJudicialReq }
     *     
     */
    public void setRegistroPorDecisaoJudicialReq(TRegistroPorDecisaoJudicialReq value) {
        this.registroPorDecisaoJudicialReq = value;
    }

    /**
     * Obtém o valor da propriedade signature.
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Define o valor da propriedade signature.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

}
