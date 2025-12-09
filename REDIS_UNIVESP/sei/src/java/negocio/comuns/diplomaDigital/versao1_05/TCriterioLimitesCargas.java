//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.09.29 às 03:15:12 PM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Tipo que defina horários lista de critérios de integralização
 * 
 * <p>Classe Java de TCriterioLimitesCargas complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TCriterioLimitesCargas">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="CargaHorariaMinima" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THoraRelogio"/>
 *             &lt;element name="CargaHorariaMaxima" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THoraRelogio"/>
 *             &lt;element name="CargaHorariaParaTotal" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THoraRelogio" minOccurs="0"/>
 *           &lt;/sequence>
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
@XmlType(name = "TCriterioLimitesCargas", propOrder = {
    "cargaHorariaMinima",
    "cargaHorariaMaxima",
    "cargaHorariaParaTotal"
})
public class TCriterioLimitesCargas {

    @XmlElement(name = "CargaHorariaMinima")
    protected BigDecimal cargaHorariaMinima;
    @XmlElement(name = "CargaHorariaMaxima")
    protected BigDecimal cargaHorariaMaxima;
    @XmlElement(name = "CargaHorariaParaTotal")
    protected BigDecimal cargaHorariaParaTotal;

    /**
     * Obtém o valor da propriedade cargaHorariaMinima.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCargaHorariaMinima() {
        return cargaHorariaMinima;
    }

    /**
     * Define o valor da propriedade cargaHorariaMinima.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCargaHorariaMinima(BigDecimal value) {
        this.cargaHorariaMinima = value;
    }

    /**
     * Obtém o valor da propriedade cargaHorariaMaxima.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCargaHorariaMaxima() {
        return cargaHorariaMaxima;
    }

    /**
     * Define o valor da propriedade cargaHorariaMaxima.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCargaHorariaMaxima(BigDecimal value) {
        this.cargaHorariaMaxima = value;
    }

    /**
     * Obtém o valor da propriedade cargaHorariaParaTotal.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCargaHorariaParaTotal() {
        return cargaHorariaParaTotal;
    }

    /**
     * Define o valor da propriedade cargaHorariaParaTotal.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCargaHorariaParaTotal(BigDecimal value) {
        this.cargaHorariaParaTotal = value;
    }

}
