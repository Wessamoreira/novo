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
 * Tipo que define uma unidade curricular
 * 
 * <p>Classe Java de TUnidadeCurricular complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TUnidadeCurricular">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Tipo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TTipoUnidadeCurricular"/>
 *         &lt;element name="Codigo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodigoUnidadeCurricular"/>
 *         &lt;element name="Nome" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;element name="CargaHorariaEmHoraAula" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THoraAula" minOccurs="0"/>
 *         &lt;element name="CargaHorariaEmHoraRelogio" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THoraRelogio"/>
 *         &lt;element name="Ementa" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEmenta" minOccurs="0"/>
 *         &lt;element name="Fase" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" minOccurs="0"/>
 *         &lt;element name="Equivalencias" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEquivalenciaUnidadesCurriculares" minOccurs="0"/>
 *         &lt;element name="PreRequisitos" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TPreRequisitosUnidadesCurriculares" minOccurs="0"/>
 *         &lt;element name="Etiquetas" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEtiquetas" minOccurs="0"/>
 *         &lt;element name="Areas" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAreas" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TUnidadeCurricular", propOrder = {
    "tipo",
    "codigo",
    "nome",
    "cargaHorariaEmHoraAula",
    "cargaHorariaEmHoraRelogio",
    "ementa",
    "fase",
    "equivalencias",
    "preRequisitos",
    "etiquetas",
    "areas"
})
public class TUnidadeCurricular {

    @XmlElement(name = "Tipo", required = true)
    @XmlSchemaType(name = "string")
    protected TTipoUnidadeCurricular tipo;
    @XmlElement(name = "Codigo", required = true)
    protected String codigo;
    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "CargaHorariaEmHoraAula")
    @XmlSchemaType(name = "unsignedInt")
    protected Long cargaHorariaEmHoraAula;
    @XmlElement(name = "CargaHorariaEmHoraRelogio", required = true)
    protected BigDecimal cargaHorariaEmHoraRelogio;
    @XmlElement(name = "Ementa")
    protected TEmenta ementa;
    @XmlElement(name = "Fase")
    protected String fase;
    @XmlElement(name = "Equivalencias")
    protected TEquivalenciaUnidadesCurriculares equivalencias;
    @XmlElement(name = "PreRequisitos")
    protected TPreRequisitosUnidadesCurriculares preRequisitos;
    @XmlElement(name = "Etiquetas")
    protected TEtiquetas etiquetas;
    @XmlElement(name = "Areas")
    protected TAreas areas;

    /**
     * Obtém o valor da propriedade tipo.
     * 
     * @return
     *     possible object is
     *     {@link TTipoUnidadeCurricular }
     *     
     */
    public TTipoUnidadeCurricular getTipo() {
        return tipo;
    }

    /**
     * Define o valor da propriedade tipo.
     * 
     * @param value
     *     allowed object is
     *     {@link TTipoUnidadeCurricular }
     *     
     */
    public void setTipo(TTipoUnidadeCurricular value) {
        this.tipo = value;
    }

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
     * Obtém o valor da propriedade nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o valor da propriedade nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
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

    /**
     * Obtém o valor da propriedade ementa.
     * 
     * @return
     *     possible object is
     *     {@link TEmenta }
     *     
     */
    public TEmenta getEmenta() {
        return ementa;
    }

    /**
     * Define o valor da propriedade ementa.
     * 
     * @param value
     *     allowed object is
     *     {@link TEmenta }
     *     
     */
    public void setEmenta(TEmenta value) {
        this.ementa = value;
    }

    /**
     * Obtém o valor da propriedade fase.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFase() {
        return fase;
    }

    /**
     * Define o valor da propriedade fase.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFase(String value) {
        this.fase = value;
    }

    /**
     * Obtém o valor da propriedade equivalencias.
     * 
     * @return
     *     possible object is
     *     {@link TEquivalenciaUnidadesCurriculares }
     *     
     */
    public TEquivalenciaUnidadesCurriculares getEquivalencias() {
        return equivalencias;
    }

    /**
     * Define o valor da propriedade equivalencias.
     * 
     * @param value
     *     allowed object is
     *     {@link TEquivalenciaUnidadesCurriculares }
     *     
     */
    public void setEquivalencias(TEquivalenciaUnidadesCurriculares value) {
        this.equivalencias = value;
    }

    /**
     * Obtém o valor da propriedade preRequisitos.
     * 
     * @return
     *     possible object is
     *     {@link TPreRequisitosUnidadesCurriculares }
     *     
     */
    public TPreRequisitosUnidadesCurriculares getPreRequisitos() {
        return preRequisitos;
    }

    /**
     * Define o valor da propriedade preRequisitos.
     * 
     * @param value
     *     allowed object is
     *     {@link TPreRequisitosUnidadesCurriculares }
     *     
     */
    public void setPreRequisitos(TPreRequisitosUnidadesCurriculares value) {
        this.preRequisitos = value;
    }

    /**
     * Obtém o valor da propriedade etiquetas.
     * 
     * @return
     *     possible object is
     *     {@link TEtiquetas }
     *     
     */
    public TEtiquetas getEtiquetas() {
        return etiquetas;
    }

    /**
     * Define o valor da propriedade etiquetas.
     * 
     * @param value
     *     allowed object is
     *     {@link TEtiquetas }
     *     
     */
    public void setEtiquetas(TEtiquetas value) {
        this.etiquetas = value;
    }

    /**
     * Obtém o valor da propriedade areas.
     * 
     * @return
     *     possible object is
     *     {@link TAreas }
     *     
     */
    public TAreas getAreas() {
        return areas;
    }

    /**
     * Define o valor da propriedade areas.
     * 
     * @param value
     *     allowed object is
     *     {@link TAreas }
     *     
     */
    public void setAreas(TAreas value) {
        this.areas = value;
    }

}
