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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Etiqueta que qualifica a Unidade Curricular para fins de cômputo da integralização curricular. 
 * 				Caso NumeroHorasParaIntegralizacao esteja presente, este número de horas será utilizado para fins de contabilização de carga horária.
 * 				Caso NumeroHorasParaIntegralizacao não esteja presente, será usado a carga horária da Unidade Curricular.
 * 
 * <p>Classe Java de TEtiqueta complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TEtiqueta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Codigo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="CargaHorariaEmHoraAula" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THoraAula" minOccurs="0"/>
 *           &lt;element name="CargaHorariaEmHoraRelogio" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THoraRelogio"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TEtiqueta", propOrder = {
    "codigo",
    "cargaHorariaEmHoraAula",
    "cargaHorariaEmHoraRelogio"
})
public class TEtiqueta {

    @XmlElement(name = "Codigo", required = true)
    protected String codigo;
    @XmlElement(name = "CargaHorariaEmHoraAula")
    @XmlSchemaType(name = "unsignedInt")
    protected Long cargaHorariaEmHoraAula;
    @XmlElement(name = "CargaHorariaEmHoraRelogio")
    protected BigDecimal cargaHorariaEmHoraRelogio;

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
     * Obtém o valor da propriedade cargaHorariaEmHoraAula.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCargaHorariaEmHoraAula() {
        return cargaHorariaEmHoraAula;
    }

    /**
     * Define o valor da propriedade cargaHorariaEmHoraAula.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCargaHorariaEmHoraAula(Long value) {
        this.cargaHorariaEmHoraAula = value;
    }

    /**
     * Obtém o valor da propriedade cargaHorariaEmHoraRelogio.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCargaHorariaEmHoraRelogio() {
        return cargaHorariaEmHoraRelogio;
    }

    /**
     * Define o valor da propriedade cargaHorariaEmHoraRelogio.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCargaHorariaEmHoraRelogio(BigDecimal value) {
        this.cargaHorariaEmHoraRelogio = value;
    }

}
