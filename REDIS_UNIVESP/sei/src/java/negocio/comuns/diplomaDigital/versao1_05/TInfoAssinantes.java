//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.25 às 05:38:08 PM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Informações de cargo dos assinantes
 * 
 * <p>Classe Java de TInfoAssinantes complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TInfoAssinantes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Assinante" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CPF" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCpf"/>
 *                   &lt;choice>
 *                     &lt;element name="Cargo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCargosAssinantes"/>
 *                     &lt;element name="OutroCargo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *                   &lt;/choice>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TInfoAssinantes", propOrder = {
    "assinante"
})
public class TInfoAssinantes {

    @XmlElement(name = "Assinante", required = true)
    protected List<TInfoAssinantes.Assinante> assinante;

    /**
     * Gets the value of the assinante property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the assinante property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAssinante().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TInfoAssinantes.Assinante }
     * 
     * 
     */
    public List<TInfoAssinantes.Assinante> getAssinante() {
        if (assinante == null) {
            assinante = new ArrayList<TInfoAssinantes.Assinante>();
        }
        return this.assinante;
    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="CPF" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCpf"/>
     *         &lt;choice>
     *           &lt;element name="Cargo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCargosAssinantes"/>
     *           &lt;element name="OutroCargo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
     *         &lt;/choice>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "cpf",
        "cargo",
        "outroCargo"
    })
    public static class Assinante {

        @XmlElement(name = "CPF", required = true)
        protected String cpf;
        @XmlElement(name = "Cargo")
        @XmlSchemaType(name = "string")
        protected TCargosAssinantes cargo;
        @XmlElement(name = "OutroCargo")
        protected String outroCargo;

        /**
         * Obtém o valor da propriedade cpf.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCPF() {
            return cpf;
        }

        /**
         * Define o valor da propriedade cpf.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCPF(String value) {
            this.cpf = value;
        }

        /**
         * Obtém o valor da propriedade cargo.
         * 
         * @return
         *     possible object is
         *     {@link TCargosAssinantes }
         *     
         */
        public TCargosAssinantes getCargo() {
            return cargo;
        }

        /**
         * Define o valor da propriedade cargo.
         * 
         * @param value
         *     allowed object is
         *     {@link TCargosAssinantes }
         *     
         */
        public void setCargo(TCargosAssinantes value) {
            this.cargo = value;
        }

        /**
         * Obtém o valor da propriedade outroCargo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOutroCargo() {
            return outroCargo;
        }

        /**
         * Define o valor da propriedade outroCargo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOutroCargo(String value) {
            this.outroCargo = value;
        }

    }

}
