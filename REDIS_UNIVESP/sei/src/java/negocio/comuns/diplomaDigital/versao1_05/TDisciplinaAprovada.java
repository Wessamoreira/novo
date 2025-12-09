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


/**
 * <p>Classe Java de TDisciplinaAprovada complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDisciplinaAprovada">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="FormaIntegralizacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TFormaIntegralizacao"/>
 *           &lt;element name="OutraFormaIntegralizacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
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
@XmlType(name = "TDisciplinaAprovada", propOrder = {
    "formaIntegralizacao",
    "outraFormaIntegralizacao"
})
public class TDisciplinaAprovada {

    @XmlElement(name = "FormaIntegralizacao")
    @XmlSchemaType(name = "string")
    protected TFormaIntegralizacao formaIntegralizacao;
    @XmlElement(name = "OutraFormaIntegralizacao")
    protected String outraFormaIntegralizacao;

    /**
     * Obtém o valor da propriedade formaIntegralizacao.
     * 
     * @return
     *     possible object is
     *     {@link TFormaIntegralizacao }
     *     
     */
    public TFormaIntegralizacao getFormaIntegralizacao() {
        return formaIntegralizacao;
    }

    /**
     * Define o valor da propriedade formaIntegralizacao.
     * 
     * @param value
     *     allowed object is
     *     {@link TFormaIntegralizacao }
     *     
     */
    public void setFormaIntegralizacao(TFormaIntegralizacao value) {
        this.formaIntegralizacao = value;
    }

    /**
     * Obtém o valor da propriedade outraFormaIntegralizacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutraFormaIntegralizacao() {
        return outraFormaIntegralizacao;
    }

    /**
     * Define o valor da propriedade outraFormaIntegralizacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutraFormaIntegralizacao(String value) {
        this.outraFormaIntegralizacao = value;
    }

}
