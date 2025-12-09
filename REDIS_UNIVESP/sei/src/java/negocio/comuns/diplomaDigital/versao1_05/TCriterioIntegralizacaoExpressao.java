//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.09.29 às 03:15:12 PM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Tipo que define um critério de integralização que é atingindo quando 
 * 				as cargas horárias calculadas a partir da expressão posta atingem a Carga Horária
 * 				Mínima, limitada a Carga Horária Máxima.
 * 			
 * 
 * <p>Classe Java de TCriterioIntegralizacaoExpressao complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TCriterioIntegralizacaoExpressao">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Codigo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TId"/>
 *         &lt;element name="Expressao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TExpressao"/>
 *         &lt;element name="CargasHorariasCriterio" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCriterioLimitesCargas"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TCriterioIntegralizacaoExpressao", propOrder = {
    "codigo",
    "expressao",
    "cargasHorariasCriterio"
})
public class TCriterioIntegralizacaoExpressao {

    @XmlElement(name = "Codigo", required = true)
    protected String codigo;
    @XmlElement(name = "Expressao", required = true)
    protected TExpressao expressao;
    @XmlElement(name = "CargasHorariasCriterio", required = true)
    protected TCriterioLimitesCargas cargasHorariasCriterio;

    /**
     * Obtém o valor da propriedade codigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Define o valor da propriedade codigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigo(String value) {
        this.codigo = value;
    }

    /**
     * Obtém o valor da propriedade expressao.
     * 
     * @return
     *     possible object is
     *     {@link TExpressao }
     *     
     */
    public TExpressao getExpressao() {
        return expressao;
    }

    /**
     * Define o valor da propriedade expressao.
     * 
     * @param value
     *     allowed object is
     *     {@link TExpressao }
     *     
     */
    public void setExpressao(TExpressao value) {
        this.expressao = value;
    }

    /**
     * Obtém o valor da propriedade cargasHorariasCriterio.
     * 
     * @return
     *     possible object is
     *     {@link TCriterioLimitesCargas }
     *     
     */
    public TCriterioLimitesCargas getCargasHorariasCriterio() {
        return cargasHorariasCriterio;
    }

    /**
     * Define o valor da propriedade cargasHorariasCriterio.
     * 
     * @param value
     *     allowed object is
     *     {@link TCriterioLimitesCargas }
     *     
     */
    public void setCargasHorariasCriterio(TCriterioLimitesCargas value) {
        this.cargasHorariasCriterio = value;
    }

}
