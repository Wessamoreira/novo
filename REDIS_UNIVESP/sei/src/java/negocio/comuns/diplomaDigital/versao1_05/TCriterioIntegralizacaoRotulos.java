//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.09.29 às 03:15:12 PM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Tipo que define um critério de integralização que é atingindo quando o somatório de cargas horárias das Unidades Curriculares
 * 				com etiquetas e tipo de unidade curricular atinge a Carga Horária Mínima, limitada a Carga Horária Máxima
 * 
 * <p>Classe Java de TCriterioIntegralizacaoRotulos complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TCriterioIntegralizacaoRotulos">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Codigo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TId"/>
 *         &lt;element name="UnidadeCurricular" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TTipoUnidadeCurricular" minOccurs="0"/>
 *         &lt;element name="Etiqueta" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "TCriterioIntegralizacaoRotulos", propOrder = {
    "codigo",
    "unidadeCurricular",
    "etiqueta",
    "cargasHorariasCriterio"
})
public class TCriterioIntegralizacaoRotulos {

    @XmlElement(name = "Codigo", required = true)
    protected String codigo;
    @XmlElement(name = "UnidadeCurricular")
    @XmlSchemaType(name = "string")
    protected TTipoUnidadeCurricular unidadeCurricular;
    @XmlElement(name = "Etiqueta")
    protected List<String> etiqueta;
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
     * Obtém o valor da propriedade unidadeCurricular.
     * 
     * @return
     *     possible object is
     *     {@link TTipoUnidadeCurricular }
     *     
     */
    public TTipoUnidadeCurricular getUnidadeCurricular() {
        return unidadeCurricular;
    }

    /**
     * Define o valor da propriedade unidadeCurricular.
     * 
     * @param value
     *     allowed object is
     *     {@link TTipoUnidadeCurricular }
     *     
     */
    public void setUnidadeCurricular(TTipoUnidadeCurricular value) {
        this.unidadeCurricular = value;
    }

    /**
     * Gets the value of the etiqueta property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the etiqueta property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEtiqueta().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getEtiqueta() {
        if (etiqueta == null) {
            etiqueta = new ArrayList<String>();
        }
        return this.etiqueta;
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
