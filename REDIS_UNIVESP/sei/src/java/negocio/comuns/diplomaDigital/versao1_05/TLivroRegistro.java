//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.25 às 05:38:08 PM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Dados do livro
 * 
 * <p>Classe Java de TLivroRegistro complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TLivroRegistro">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LivroRegistro" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodLivroRegistro"/>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="NumeroRegistro" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TId"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element name="NumeroFolhaDoDiploma" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNumFolhaDoDiploma"/>
 *             &lt;element name="NumeroSequenciaDoDiploma" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNumSequenciaDiploma"/>
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
@XmlType(name = "TLivroRegistro", propOrder = {
    "livroRegistro",
    "numeroRegistro",
    "numeroFolhaDoDiploma",
    "numeroSequenciaDoDiploma",
    "processoDoDiploma",
    "dataColacaoGrau",
    "dataExpedicaoDiploma",
    "dataRegistroDiploma",
    "responsavelRegistro"
})
public class TLivroRegistro {

    @XmlElement(name = "LivroRegistro", required = true)
    protected String livroRegistro;
    @XmlElement(name = "NumeroRegistro")
    protected String numeroRegistro;
    @XmlElement(name = "NumeroFolhaDoDiploma")
    protected String numeroFolhaDoDiploma;
    @XmlElement(name = "NumeroSequenciaDoDiploma")
    protected String numeroSequenciaDoDiploma;
    @XmlElement(name = "ProcessoDoDiploma")
    protected String processoDoDiploma;
    @XmlElement(name = "DataColacaoGrau", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataColacaoGrau;
    @XmlElement(name = "DataExpedicaoDiploma", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataExpedicaoDiploma;
    @XmlElement(name = "DataRegistroDiploma", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataRegistroDiploma;
    @XmlElement(name = "ResponsavelRegistro", required = true)
    protected TLivroRegistro.ResponsavelRegistro responsavelRegistro;

    /**
     * Obtém o valor da propriedade livroRegistro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLivroRegistro() {
        return livroRegistro;
    }

    /**
     * Define o valor da propriedade livroRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLivroRegistro(String value) {
        this.livroRegistro = value;
    }

    /**
     * Obtém o valor da propriedade numeroRegistro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    /**
     * Define o valor da propriedade numeroRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroRegistro(String value) {
        this.numeroRegistro = value;
    }

    /**
     * Obtém o valor da propriedade numeroFolhaDoDiploma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroFolhaDoDiploma() {
        return numeroFolhaDoDiploma;
    }

    /**
     * Define o valor da propriedade numeroFolhaDoDiploma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroFolhaDoDiploma(String value) {
        this.numeroFolhaDoDiploma = value;
    }

    /**
     * Obtém o valor da propriedade numeroSequenciaDoDiploma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroSequenciaDoDiploma() {
        return numeroSequenciaDoDiploma;
    }

    /**
     * Define o valor da propriedade numeroSequenciaDoDiploma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroSequenciaDoDiploma(String value) {
        this.numeroSequenciaDoDiploma = value;
    }

    /**
     * Obtém o valor da propriedade processoDoDiploma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessoDoDiploma() {
        return processoDoDiploma;
    }

    /**
     * Define o valor da propriedade processoDoDiploma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessoDoDiploma(String value) {
        this.processoDoDiploma = value;
    }

    /**
     * Obtém o valor da propriedade dataColacaoGrau.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataColacaoGrau() {
        return dataColacaoGrau;
    }

    /**
     * Define o valor da propriedade dataColacaoGrau.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataColacaoGrau(XMLGregorianCalendar value) {
        this.dataColacaoGrau = value;
    }

    /**
     * Obtém o valor da propriedade dataExpedicaoDiploma.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataExpedicaoDiploma() {
        return dataExpedicaoDiploma;
    }

    /**
     * Define o valor da propriedade dataExpedicaoDiploma.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataExpedicaoDiploma(XMLGregorianCalendar value) {
        this.dataExpedicaoDiploma = value;
    }

    /**
     * Obtém o valor da propriedade dataRegistroDiploma.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataRegistroDiploma() {
        return dataRegistroDiploma;
    }

    /**
     * Define o valor da propriedade dataRegistroDiploma.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataRegistroDiploma(XMLGregorianCalendar value) {
        this.dataRegistroDiploma = value;
    }

    /**
     * Obtém o valor da propriedade responsavelRegistro.
     * 
     * @return
     *     possible object is
     *     {@link TLivroRegistro.ResponsavelRegistro }
     *     
     */
    public TLivroRegistro.ResponsavelRegistro getResponsavelRegistro() {
        return responsavelRegistro;
    }

    /**
     * Define o valor da propriedade responsavelRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link TLivroRegistro.ResponsavelRegistro }
     *     
     */
    public void setResponsavelRegistro(TLivroRegistro.ResponsavelRegistro value) {
        this.responsavelRegistro = value;
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
