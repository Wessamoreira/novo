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
 * Informações sobre tramitação de processos EMEC
 * 
 * <p>Classe Java de TInformacoesTramitacaoEMEC complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TInformacoesTramitacaoEMEC">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NumeroProcesso" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNumeroPositivo"/>
 *         &lt;element name="TipoProcesso" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;element name="DataCadastro" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="DataProtocolo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TInformacoesTramitacaoEMEC", propOrder = {
    "numeroProcesso",
    "tipoProcesso",
    "dataCadastro",
    "dataProtocolo"
})
public class TInformacoesTramitacaoEMEC {

    @XmlElement(name = "NumeroProcesso")
    @XmlSchemaType(name = "unsignedInt")
    protected long numeroProcesso;
    @XmlElement(name = "TipoProcesso", required = true)
    protected String tipoProcesso;
    @XmlElement(name = "DataCadastro", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataCadastro;
    @XmlElement(name = "DataProtocolo", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataProtocolo;

    /**
     * Obtém o valor da propriedade numeroProcesso.
     * 
     */
    public long getNumeroProcesso() {
        return numeroProcesso;
    }

    /**
     * Define o valor da propriedade numeroProcesso.
     * 
     */
    public void setNumeroProcesso(long value) {
        this.numeroProcesso = value;
    }

    /**
     * Obtém o valor da propriedade tipoProcesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoProcesso() {
        return tipoProcesso;
    }

    /**
     * Define o valor da propriedade tipoProcesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoProcesso(String value) {
        this.tipoProcesso = value;
    }

    /**
     * Obtém o valor da propriedade dataCadastro.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataCadastro() {
        return dataCadastro;
    }

    /**
     * Define o valor da propriedade dataCadastro.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataCadastro(XMLGregorianCalendar value) {
        this.dataCadastro = value;
    }

    /**
     * Obtém o valor da propriedade dataProtocolo.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataProtocolo() {
        return dataProtocolo;
    }

    /**
     * Define o valor da propriedade dataProtocolo.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataProtocolo(XMLGregorianCalendar value) {
        this.dataProtocolo = value;
    }

}
