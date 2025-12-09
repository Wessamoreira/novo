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
 * Informações do processo judicial
 * 
 * <p>Classe Java de TInformacoesProcessoJudicial complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TInformacoesProcessoJudicial">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NumeroProcessoJudicial" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNumeroProcessoJudicial"/>
 *         &lt;element name="NomeJuiz" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;element name="Decisao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TInformacoesProcessoJudicial", propOrder = {
    "numeroProcessoJudicial",
    "nomeJuiz",
    "decisao"
})
public class TInformacoesProcessoJudicial {

    @XmlElement(name = "NumeroProcessoJudicial", required = true)
    protected String numeroProcessoJudicial;
    @XmlElement(name = "NomeJuiz", required = true)
    protected String nomeJuiz;
    @XmlElement(name = "Decisao")
    protected String decisao;

    /**
     * Obtém o valor da propriedade numeroProcessoJudicial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroProcessoJudicial() {
        return numeroProcessoJudicial;
    }

    /**
     * Define o valor da propriedade numeroProcessoJudicial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroProcessoJudicial(String value) {
        this.numeroProcessoJudicial = value;
    }

    /**
     * Obtém o valor da propriedade nomeJuiz.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeJuiz() {
        return nomeJuiz;
    }

    /**
     * Define o valor da propriedade nomeJuiz.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeJuiz(String value) {
        this.nomeJuiz = value;
    }

    /**
     * Obtém o valor da propriedade decisao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDecisao() {
        return decisao;
    }

    /**
     * Define o valor da propriedade decisao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDecisao(String value) {
        this.decisao = value;
    }

}
