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
 * Ato regulatório
 * 
 * <p>Classe Java de TAtoRegulatorio complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TAtoRegulatorio">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Tipo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TTipoAto"/>
 *         &lt;element name="Numero" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNumeroAto"/>
 *         &lt;element name="Data" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="VeiculoPublicacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" minOccurs="0"/>
 *         &lt;element name="DataPublicacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData" minOccurs="0"/>
 *         &lt;element name="SecaoPublicacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TSecaoPublicacao" minOccurs="0"/>
 *         &lt;element name="PaginaPublicacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TPaginaPublicacao" minOccurs="0"/>
 *         &lt;element name="NumeroDOU" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNumeroDOU" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TAtoRegulatorio", propOrder = {
    "tipo",
    "numero",
    "data",
    "veiculoPublicacao",
    "dataPublicacao",
    "secaoPublicacao",
    "paginaPublicacao",
    "numeroDOU"
})
public class TAtoRegulatorio {

    @XmlElement(name = "Tipo", required = true)
    @XmlSchemaType(name = "string")
    protected TTipoAto tipo;
    @XmlElement(name = "Numero", required = true)
    protected String numero;
    @XmlElement(name = "Data", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar data;
    @XmlElement(name = "VeiculoPublicacao")
    protected String veiculoPublicacao;
    @XmlElement(name = "DataPublicacao")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataPublicacao;
    @XmlElement(name = "SecaoPublicacao")
    @XmlSchemaType(name = "unsignedInt")
    protected Long secaoPublicacao;
    @XmlElement(name = "PaginaPublicacao")
    @XmlSchemaType(name = "unsignedInt")
    protected Long paginaPublicacao;
    @XmlElement(name = "NumeroDOU")
    @XmlSchemaType(name = "unsignedInt")
    protected Long numeroDOU;

    /**
     * Obtém o valor da propriedade tipo.
     * 
     * @return
     *     possible object is
     *     {@link TTipoAto }
     *     
     */
    public TTipoAto getTipo() {
        return tipo;
    }

    /**
     * Define o valor da propriedade tipo.
     * 
     * @param value
     *     allowed object is
     *     {@link TTipoAto }
     *     
     */
    public void setTipo(TTipoAto value) {
        this.tipo = value;
    }

    /**
     * Obtém o valor da propriedade numero.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumero() {
        return numero;
    }

    /**
     * Define o valor da propriedade numero.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumero(String value) {
        this.numero = value;
    }

    /**
     * Obtém o valor da propriedade data.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getData() {
        return data;
    }

    /**
     * Define o valor da propriedade data.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setData(XMLGregorianCalendar value) {
        this.data = value;
    }

    /**
     * Obtém o valor da propriedade veiculoPublicacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVeiculoPublicacao() {
        return veiculoPublicacao;
    }

    /**
     * Define o valor da propriedade veiculoPublicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVeiculoPublicacao(String value) {
        this.veiculoPublicacao = value;
    }

    /**
     * Obtém o valor da propriedade dataPublicacao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataPublicacao() {
        return dataPublicacao;
    }

    /**
     * Define o valor da propriedade dataPublicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataPublicacao(XMLGregorianCalendar value) {
        this.dataPublicacao = value;
    }

    /**
     * Obtém o valor da propriedade secaoPublicacao.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getSecaoPublicacao() {
        return secaoPublicacao;
    }

    /**
     * Define o valor da propriedade secaoPublicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setSecaoPublicacao(Long value) {
        this.secaoPublicacao = value;
    }

    /**
     * Obtém o valor da propriedade paginaPublicacao.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPaginaPublicacao() {
        return paginaPublicacao;
    }

    /**
     * Define o valor da propriedade paginaPublicacao.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPaginaPublicacao(Long value) {
        this.paginaPublicacao = value;
    }

    /**
     * Obtém o valor da propriedade numeroDOU.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumeroDOU() {
        return numeroDOU;
    }

    /**
     * Define o valor da propriedade numeroDOU.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNumeroDOU(Long value) {
        this.numeroDOU = value;
    }

}
