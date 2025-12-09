//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.25 às 05:38:08 PM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Tipo carga horária em Hora Aula ou em Hora Relógio
 * 
 * <p>Classe Java de TCargaHorariaComEtiqueta complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TCargaHorariaComEtiqueta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="HoraAula" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THoraAula"/>
 *         &lt;element name="HoraRelogio" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THoraRelogio"/>
 *       &lt;/choice>
 *       &lt;attribute name="etiqueta" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TCargaHorariaComEtiqueta", propOrder = {
    "horaAula",
    "horaRelogio"
})
public class TCargaHorariaComEtiqueta {

    @XmlElement(name = "HoraAula")
    @XmlSchemaType(name = "unsignedInt")
    protected Long horaAula;
    @XmlElement(name = "HoraRelogio")
    protected BigDecimal horaRelogio;
    @XmlAttribute(name = "etiqueta")
    protected String etiqueta;

    /**
     * Obtém o valor da propriedade horaAula.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getHoraAula() {
        return horaAula;
    }

    /**
     * Define o valor da propriedade horaAula.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setHoraAula(Long value) {
        this.horaAula = value;
    }

    /**
     * Obtém o valor da propriedade horaRelogio.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getHoraRelogio() {
        return horaRelogio;
    }

    /**
     * Define o valor da propriedade horaRelogio.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setHoraRelogio(BigDecimal value) {
        this.horaRelogio = value;
    }

    /**
     * Obtém o valor da propriedade etiqueta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Define o valor da propriedade etiqueta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEtiqueta(String value) {
        this.etiqueta = value;
    }

}
