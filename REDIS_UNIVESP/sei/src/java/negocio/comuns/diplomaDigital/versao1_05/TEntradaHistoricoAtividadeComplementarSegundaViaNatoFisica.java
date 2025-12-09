//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.25 às 05:38:08 PM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de TEntradaHistoricoAtividadeComplementarSegundaViaNatoFisica complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TEntradaHistoricoAtividadeComplementarSegundaViaNatoFisica">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CodigoAtividadeComplementar" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodigoUnidadeCurricular" minOccurs="0"/>
 *         &lt;element name="DataInicio" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="DataFim" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="DataRegistro" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData" minOccurs="0"/>
 *         &lt;element name="TipoAtividadeComplementar" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;element name="Descricao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" minOccurs="0"/>
 *         &lt;element name="CargaHorariaEmHoraRelogio" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THoraRelogioComEtiqueta" maxOccurs="unbounded"/>
 *         &lt;element name="DocentesResponsaveisPelaValidacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDocentes"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TEntradaHistoricoAtividadeComplementarSegundaViaNatoFisica", propOrder = {
    "codigoAtividadeComplementar",
    "dataInicio",
    "dataFim",
    "dataRegistro",
    "tipoAtividadeComplementar",
    "descricao",
    "cargaHorariaEmHoraRelogio",
    "docentesResponsaveisPelaValidacao"
})
public class TEntradaHistoricoAtividadeComplementarSegundaViaNatoFisica {

    @XmlElement(name = "CodigoAtividadeComplementar")
    protected String codigoAtividadeComplementar;
    @XmlElement(name = "DataInicio", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataInicio;
    @XmlElement(name = "DataFim", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataFim;
    @XmlElement(name = "DataRegistro")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataRegistro;
    @XmlElement(name = "TipoAtividadeComplementar", required = true)
    protected String tipoAtividadeComplementar;
    @XmlElement(name = "Descricao")
    protected String descricao;
    @XmlElement(name = "CargaHorariaEmHoraRelogio", required = true)
    protected List<THoraRelogioComEtiqueta> cargaHorariaEmHoraRelogio;
    @XmlElement(name = "DocentesResponsaveisPelaValidacao", required = true)
    protected TDocentes docentesResponsaveisPelaValidacao;

    /**
     * Obtém o valor da propriedade codigoAtividadeComplementar.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoAtividadeComplementar() {
        return codigoAtividadeComplementar;
    }

    /**
     * Define o valor da propriedade codigoAtividadeComplementar.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoAtividadeComplementar(String value) {
        this.codigoAtividadeComplementar = value;
    }

    /**
     * Obtém o valor da propriedade dataInicio.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataInicio() {
        return dataInicio;
    }

    /**
     * Define o valor da propriedade dataInicio.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataInicio(XMLGregorianCalendar value) {
        this.dataInicio = value;
    }

    /**
     * Obtém o valor da propriedade dataFim.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataFim() {
        return dataFim;
    }

    /**
     * Define o valor da propriedade dataFim.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataFim(XMLGregorianCalendar value) {
        this.dataFim = value;
    }

    /**
     * Obtém o valor da propriedade dataRegistro.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataRegistro() {
        return dataRegistro;
    }

    /**
     * Define o valor da propriedade dataRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataRegistro(XMLGregorianCalendar value) {
        this.dataRegistro = value;
    }

    /**
     * Obtém o valor da propriedade tipoAtividadeComplementar.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoAtividadeComplementar() {
        return tipoAtividadeComplementar;
    }

    /**
     * Define o valor da propriedade tipoAtividadeComplementar.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoAtividadeComplementar(String value) {
        this.tipoAtividadeComplementar = value;
    }

    /**
     * Obtém o valor da propriedade descricao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define o valor da propriedade descricao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescricao(String value) {
        this.descricao = value;
    }

    /**
     * Gets the value of the cargaHorariaEmHoraRelogio property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cargaHorariaEmHoraRelogio property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCargaHorariaEmHoraRelogio().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link THoraRelogioComEtiqueta }
     * 
     * 
     */
    public List<THoraRelogioComEtiqueta> getCargaHorariaEmHoraRelogio() {
        if (cargaHorariaEmHoraRelogio == null) {
            cargaHorariaEmHoraRelogio = new ArrayList<THoraRelogioComEtiqueta>();
        }
        return this.cargaHorariaEmHoraRelogio;
    }

    /**
     * Obtém o valor da propriedade docentesResponsaveisPelaValidacao.
     * 
     * @return
     *     possible object is
     *     {@link TDocentes }
     *     
     */
    public TDocentes getDocentesResponsaveisPelaValidacao() {
        return docentesResponsaveisPelaValidacao;
    }

    /**
     * Define o valor da propriedade docentesResponsaveisPelaValidacao.
     * 
     * @param value
     *     allowed object is
     *     {@link TDocentes }
     *     
     */
    public void setDocentesResponsaveisPelaValidacao(TDocentes value) {
        this.docentesResponsaveisPelaValidacao = value;
    }

}
