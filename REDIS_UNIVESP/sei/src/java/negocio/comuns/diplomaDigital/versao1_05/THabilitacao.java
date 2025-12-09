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
 * 
 * 				Informações sobre Habilitacao
 * 			
 * 
 * <p>Classe Java de THabilitacao complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="THabilitacao">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NomeHabilitacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;element name="DataHabilitacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "THabilitacao", propOrder = {
    "nomeHabilitacao",
    "dataHabilitacao"
})
public class THabilitacao {

    @XmlElement(name = "NomeHabilitacao", required = true)
    protected String nomeHabilitacao;
    @XmlElement(name = "DataHabilitacao", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataHabilitacao;

    /**
     * Obtém o valor da propriedade nomeHabilitacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeHabilitacao() {
        return nomeHabilitacao;
    }

    /**
     * Define o valor da propriedade nomeHabilitacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeHabilitacao(String value) {
        this.nomeHabilitacao = value;
    }

    /**
     * Obtém o valor da propriedade dataHabilitacao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataHabilitacao() {
        return dataHabilitacao;
    }

    /**
     * Define o valor da propriedade dataHabilitacao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataHabilitacao(XMLGregorianCalendar value) {
        this.dataHabilitacao = value;
    }

}
