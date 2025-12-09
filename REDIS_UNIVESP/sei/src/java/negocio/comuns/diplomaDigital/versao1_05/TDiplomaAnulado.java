//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.04.27 às 11:07:34 AM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Informações de anulação referentes a um Diploma
 * 
 * <p>Classe Java de TDiplomaAnulado complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDiplomaAnulado">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CodigoDiplomaAnulado" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodigoValidacao"/>
 *         &lt;element name="DataAnulacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="MotivoAnulacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TMotivoAnulacao"/>
 *         &lt;element name="AnotacaoAnulacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDiplomaAnulado", propOrder = {
    "codigoDiplomaAnulado",
    "dataAnulacao",
    "motivoAnulacao",
    "anotacaoAnulacao"
})
public class TDiplomaAnulado {

    @XmlElement(name = "CodigoDiplomaAnulado", required = true)
    protected String codigoDiplomaAnulado;
    @XmlElement(name = "DataAnulacao", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataAnulacao;
    @XmlElement(name = "MotivoAnulacao", required = true)
    @XmlSchemaType(name = "string")
    protected TMotivoAnulacao motivoAnulacao;
    @XmlElement(name = "AnotacaoAnulacao")
    protected String anotacaoAnulacao;

    /**
     * Obtém o valor da propriedade codigoDiplomaAnulado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoDiplomaAnulado() {
        return codigoDiplomaAnulado;
    }

    /**
     * Define o valor da propriedade codigoDiplomaAnulado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoDiplomaAnulado(String value) {
        this.codigoDiplomaAnulado = value;
    }

    /**
     * Obtém o valor da propriedade dataAnulacao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataAnulacao() {
        return dataAnulacao;
    }

    /**
     * Define o valor da propriedade dataAnulacao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataAnulacao(XMLGregorianCalendar value) {
        this.dataAnulacao = value;
    }

    /**
     * Obtém o valor da propriedade motivoAnulacao.
     * 
     * @return
     *     possible object is
     *     {@link TMotivoAnulacao }
     *     
     */
    public TMotivoAnulacao getMotivoAnulacao() {
        return motivoAnulacao;
    }

    /**
     * Define o valor da propriedade motivoAnulacao.
     * 
     * @param value
     *     allowed object is
     *     {@link TMotivoAnulacao }
     *     
     */
    public void setMotivoAnulacao(TMotivoAnulacao value) {
        this.motivoAnulacao = value;
    }

    /**
     * Obtém o valor da propriedade anotacaoAnulacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnotacaoAnulacao() {
        return anotacaoAnulacao;
    }

    /**
     * Define o valor da propriedade anotacaoAnulacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnotacaoAnulacao(String value) {
        this.anotacaoAnulacao = value;
    }

}
