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
 * Tipo Documentação Comprobatória para emissões por decisão judicial
 * 
 * <p>Classe Java de TDocumentacaoComprobatoriaPorDecisaoJudicial complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDocumentacaoComprobatoriaPorDecisaoJudicial">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;choice>
 *           &lt;element name="Documento">
 *             &lt;complexType>
 *               &lt;simpleContent>
 *                 &lt;extension base="&lt;http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd>TPdfA">
 *                   &lt;attribute name="tipo" use="required" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TTipoDocumentacao" />
 *                   &lt;attribute name="observacoes" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" />
 *                 &lt;/extension>
 *               &lt;/simpleContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="Documento_Indisponivel">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;attribute name="tipo" use="required" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TTipoDocumentacao" />
 *                   &lt;attribute name="observacoes" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" />
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
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
@XmlType(name = "TDocumentacaoComprobatoriaPorDecisaoJudicial", propOrder = {
    "documentoOrDocumentoIndisponivel"
})
public class TDocumentacaoComprobatoriaPorDecisaoJudicial {

    @XmlElements({
        @XmlElement(name = "Documento", type = TDocumentacaoComprobatoriaPorDecisaoJudicial.Documento.class),
        @XmlElement(name = "Documento_Indisponivel", type = TDocumentacaoComprobatoriaPorDecisaoJudicial.DocumentoIndisponivel.class)
    })
    protected List<Object> documentoOrDocumentoIndisponivel;

    /**
     * Gets the value of the documentoOrDocumentoIndisponivel property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the documentoOrDocumentoIndisponivel property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDocumentoOrDocumentoIndisponivel().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TDocumentacaoComprobatoriaPorDecisaoJudicial.Documento }
     * {@link TDocumentacaoComprobatoriaPorDecisaoJudicial.DocumentoIndisponivel }
     * 
     * 
     */
    public List<Object> getDocumentoOrDocumentoIndisponivel() {
        if (documentoOrDocumentoIndisponivel == null) {
            documentoOrDocumentoIndisponivel = new ArrayList<Object>();
        }
        return this.documentoOrDocumentoIndisponivel;
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


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="tipo" use="required" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TTipoDocumentacao" />
     *       &lt;attribute name="observacoes" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class DocumentoIndisponivel {

        @XmlAttribute(name = "tipo", required = true)
        protected TTipoDocumentacao tipo;
        @XmlAttribute(name = "observacoes")
        protected String observacoes;

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
