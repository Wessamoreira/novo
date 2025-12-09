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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de TSituacaoIntercambio complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TSituacaoIntercambio">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Instituicao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" minOccurs="0"/>
 *         &lt;element name="Pais" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" minOccurs="0"/>
 *         &lt;element name="NomeProgramaIntercambio" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSituacaoIntercambio", propOrder = {
    "instituicao",
    "pais",
    "nomeProgramaIntercambio"
})
public class TSituacaoIntercambio {

    @XmlElement(name = "Instituicao")
    protected String instituicao;
    @XmlElement(name = "Pais")
    protected String pais;
    @XmlElement(name = "NomeProgramaIntercambio")
    protected String nomeProgramaIntercambio;

    /**
     * Obtém o valor da propriedade instituicao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstituicao() {
        return instituicao;
    }

    /**
     * Define o valor da propriedade instituicao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstituicao(String value) {
        this.instituicao = value;
    }

    /**
     * Obtém o valor da propriedade pais.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPais() {
        return pais;
    }

    /**
     * Define o valor da propriedade pais.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPais(String value) {
        this.pais = value;
    }

    /**
     * Obtém o valor da propriedade nomeProgramaIntercambio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeProgramaIntercambio() {
        return nomeProgramaIntercambio;
    }

    /**
     * Define o valor da propriedade nomeProgramaIntercambio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeProgramaIntercambio(String value) {
        this.nomeProgramaIntercambio = value;
    }

}
