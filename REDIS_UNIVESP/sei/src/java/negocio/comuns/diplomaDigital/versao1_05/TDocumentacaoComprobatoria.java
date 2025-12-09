//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.20 às 10:57:34 AM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * Tipo Documentação Comprobatória
 * 
 * <p>Classe Java de TDocumentacaoComprobatoria complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDocumentacaoComprobatoria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Documento" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd>TPdfA">
 *                 &lt;attribute name="tipo" use="required" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TTipoDocumentacao" />
 *                 &lt;attribute name="observacoes" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
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
@XmlType(name = "TDocumentacaoComprobatoria", propOrder = {
    "documento"
})
public class TDocumentacaoComprobatoria {

	@XmlElements({
        @XmlElement(name = "Documento", type = TDocumentacaoComprobatoria.Documento.class)
    })
    protected List<Object> documento;

    /**
     * Gets the value of the documento property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documento property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumento().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TDocumentacaoComprobatoria.Documento }
     * 
     * 
     */
    public List<Object> getDocumento() {
        if (documento == null) {
            documento = new ArrayList<Object>();
        }
        return this.documento;
    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd>TPdfA">
     *       &lt;attribute name="tipo" use="required" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TTipoDocumentacao" />
     *       &lt;attribute name="observacoes" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class Documento {

        @XmlValue
        protected byte[] value;
        @XmlAttribute(name = "tipo", required = true)
        protected TTipoDocumentacao tipo;
        @XmlAttribute(name = "observacoes")
        protected String observacoes;

        /**
         * Documento PDF/A segundo os padrões ISO 19005-1:2005 codificado em Base64
         * 
         * @return
         *     possible object is
         *     byte[]
         */
        public byte[] getValue() {
            return value;
        }

        /**
         * Define o valor da propriedade value.
         * 
         * @param value
         *     allowed object is
         *     byte[]
         */
        public void setValue(byte[] value) {
            this.value = value;
        }

        /**
         * Obtém o valor da propriedade tipo.
         * 
         * @return
         *     possible object is
         *     {@link TTipoDocumentacao }
         *     
         */
        public TTipoDocumentacao getTipo() {
            return tipo;
        }

        /**
         * Define o valor da propriedade tipo.
         * 
         * @param value
         *     allowed object is
         *     {@link TTipoDocumentacao }
         *     
         */
        public void setTipo(TTipoDocumentacao value) {
            this.tipo = value;
        }

        /**
         * Obtém o valor da propriedade observacoes.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getObservacoes() {
            return observacoes;
        }

        /**
         * Define o valor da propriedade observacoes.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setObservacoes(String value) {
            this.observacoes = value;
        }

    }

}
