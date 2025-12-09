//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.04.27 às 09:44:38 AM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Dados mínimos de uma IES
 * 
 * <p>Classe Java de TDadosMinimosIes complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDadosMinimosIes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Nome" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNomeIES"/>
 *         &lt;element name="CodigoMEC" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodIESMEC"/>
 *         &lt;element name="CNPJ" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCnpj"/>
 *         &lt;element name="Mantenedora" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="RazaoSocial" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TRazaoSocial"/>
 *                   &lt;element name="CNPJ" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCnpj"/>
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
@XmlType(name = "TDadosMinimosIes", propOrder = {
    "nome",
    "codigoMEC",
    "cnpj",
    "mantenedora"
})
public class TDadosMinimosIes {

    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "CodigoMEC")
    @XmlSchemaType(name = "unsignedInt")
    protected long codigoMEC;
    @XmlElement(name = "CNPJ", required = true)
    protected String cnpj;
    @XmlElement(name = "Mantenedora")
    protected TDadosMinimosIes.Mantenedora mantenedora;

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
     * Obtém o valor da propriedade codigoMEC.
     * 
     */
    public long getCodigoMEC() {
        return codigoMEC;
    }

    /**
     * Define o valor da propriedade codigoMEC.
     * 
     */
    public void setCodigoMEC(long value) {
        this.codigoMEC = value;
    }

    /**
     * Obtém o valor da propriedade cnpj.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNPJ() {
        return cnpj;
    }

    /**
     * Define o valor da propriedade cnpj.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNPJ(String value) {
        this.cnpj = value;
    }

    /**
     * Obtém o valor da propriedade mantenedora.
     * 
     * @return
     *     possible object is
     *     {@link TDadosMinimosIes.Mantenedora }
     *     
     */
    public TDadosMinimosIes.Mantenedora getMantenedora() {
        return mantenedora;
    }

    /**
     * Define o valor da propriedade mantenedora.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosMinimosIes.Mantenedora }
     *     
     */
    public void setMantenedora(TDadosMinimosIes.Mantenedora value) {
        this.mantenedora = value;
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
     *         &lt;element name="RazaoSocial" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TRazaoSocial"/>
     *         &lt;element name="CNPJ" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCnpj"/>
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
        "razaoSocial",
        "cnpj"
    })
    public static class Mantenedora {

        @XmlElement(name = "RazaoSocial", required = true)
        protected String razaoSocial;
        @XmlElement(name = "CNPJ", required = true)
        protected String cnpj;

        /**
         * Obtém o valor da propriedade razaoSocial.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRazaoSocial() {
            return razaoSocial;
        }

        /**
         * Define o valor da propriedade razaoSocial.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRazaoSocial(String value) {
            this.razaoSocial = value;
        }

        /**
         * Obtém o valor da propriedade cnpj.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getCNPJ() {
            return cnpj;
        }

        /**
         * Define o valor da propriedade cnpj.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCNPJ(String value) {
            this.cnpj = value;
        }

    }

}
