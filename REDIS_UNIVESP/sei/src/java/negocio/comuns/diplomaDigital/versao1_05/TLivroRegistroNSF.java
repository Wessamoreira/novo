//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.25 às 05:38:08 PM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Dados do livro
 * 
 * <p>Classe Java de TLivroRegistroNSF complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TLivroRegistroNSF">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LivroRegistro" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodLivroRegistro" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="NumeroRegistro" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TId"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element name="NumeroFolhaDoDiploma" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNumFolhaDoDiploma"/>
 *             &lt;element name="NumeroSequenciaDoDiploma" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNumSequenciaDiploma"/>
 *             &lt;element name="NumeroRegistro" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TId" minOccurs="0"/>
 *           &lt;/sequence>
 *         &lt;/choice>
 *         &lt;element name="ProcessoDoDiploma" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodProcessoDoDiploma" minOccurs="0"/>
 *         &lt;element name="DataColacaoGrau" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="DataExpedicaoDiploma" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="DataRegistroDiploma" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="ResponsavelRegistro">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Nome" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNome"/>
 *                   &lt;element name="CPF" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCpf"/>
 *                   &lt;element name="IDouNumeroMatricula" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TId" minOccurs="0"/>
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
@XmlType(name = "TLivroRegistroNSF", propOrder = {
    "content"
})
public class TLivroRegistroNSF {

    @XmlElementRefs({
        @XmlElementRef(name = "DataExpedicaoDiploma", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DataRegistroDiploma", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "LivroRegistro", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ProcessoDoDiploma", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "ResponsavelRegistro", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "NumeroSequenciaDoDiploma", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DataColacaoGrau", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "NumeroRegistro", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "NumeroFolhaDoDiploma", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> content;

    /**
     * Obtém o restante do modelo do conteúdo. 
     * 
     * <p>
     * Você está obtendo esta propriedade "catch-all" pelo seguinte motivo: 
     * O nome do campo "NumeroRegistro" é usado por duas partes diferentes de um esquema. Consulte: 
     * linha 527 de file:/C:/Sei/workspace/8.0.0.13M_DIPLOMA_1.05/src/java/negocio/comuns/utilitarias/diplomadigital/leiauteDiplomaDigital_v1.05.xsd
     * linha 522 de file:/C:/Sei/workspace/8.0.0.13M_DIPLOMA_1.05/src/java/negocio/comuns/utilitarias/diplomadigital/leiauteDiplomaDigital_v1.05.xsd
     * <p>
     * Para eliminar esta propriedade, aplique uma personalização de propriedade a uma 
     * das seguintes declarações, a fim de alterar seus nomes: 
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link TLivroRegistroNSF.ResponsavelRegistro }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getContent() {
        if (content == null) {
            content = new ArrayList<JAXBElement<?>>();
        }
        return this.content;
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
     *         &lt;element name="Nome" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNome"/>
     *         &lt;element name="CPF" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCpf"/>
     *         &lt;element name="IDouNumeroMatricula" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TId" minOccurs="0"/>
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
        "nome",
        "cpf",
        "iDouNumeroMatricula"
    })
    public static class ResponsavelRegistro {

        @XmlElement(name = "Nome", required = true)
        protected String nome;
        @XmlElement(name = "CPF", required = true)
        protected String cpf;
        @XmlElement(name = "IDouNumeroMatricula")
        protected String iDouNumeroMatricula;

        /**
         * Obtém o valor da propriedade nome.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNome() {
            return nome;
        }

        /**
         * Define o valor da propriedade nome.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNome(String value) {
            this.nome = value;
        }

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
         * Obtém o valor da propriedade iDouNumeroMatricula.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getIDouNumeroMatricula() {
            return iDouNumeroMatricula;
        }

        /**
         * Define o valor da propriedade iDouNumeroMatricula.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setIDouNumeroMatricula(String value) {
            this.iDouNumeroMatricula = value;
        }

    }

}
