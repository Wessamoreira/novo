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
 * <p>Classe Java de TEntradaHistoricoEstagioSegundaViaNatoFisica complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TEntradaHistoricoEstagioSegundaViaNatoFisica">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CodigoUnidadeCurricular" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodigoUnidadeCurricular"/>
 *         &lt;element name="DataInicio" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="DataFim" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="Concedente" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TConcedenteEstagio" minOccurs="0"/>
 *         &lt;element name="Descricao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" minOccurs="0"/>
 *         &lt;element name="CargaHorariaEmHorasRelogio" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THoraRelogioComEtiqueta" maxOccurs="unbounded"/>
 *         &lt;element name="DocentesOrientadores" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDocentes"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TEntradaHistoricoEstagioSegundaViaNatoFisica", propOrder = {
    "codigoUnidadeCurricular",
    "dataInicio",
    "dataFim",
    "concedente",
    "descricao",
    "cargaHorariaEmHorasRelogio",
    "docentesOrientadores"
})
public class TEntradaHistoricoEstagioSegundaViaNatoFisica {

    @XmlElement(name = "CodigoUnidadeCurricular", required = true)
    protected String codigoUnidadeCurricular;
    @XmlElement(name = "DataInicio", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataInicio;
    @XmlElement(name = "DataFim", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataFim;
    @XmlElement(name = "Concedente")
    protected TConcedenteEstagio concedente;
    @XmlElement(name = "Descricao")
    protected String descricao;
    @XmlElement(name = "CargaHorariaEmHorasRelogio", required = true)
    protected List<THoraRelogioComEtiqueta> cargaHorariaEmHorasRelogio;
    @XmlElement(name = "DocentesOrientadores", required = true)
    protected TDocentes docentesOrientadores;

    /**
     * Obtém o valor da propriedade codigoUnidadeCurricular.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoUnidadeCurricular() {
        return codigoUnidadeCurricular;
    }

    /**
     * Define o valor da propriedade codigoUnidadeCurricular.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoUnidadeCurricular(String value) {
        this.codigoUnidadeCurricular = value;
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
     * Obtém o valor da propriedade concedente.
     * 
     * @return
     *     possible object is
     *     {@link TConcedenteEstagio }
     *     
     */
    public TConcedenteEstagio getConcedente() {
        return concedente;
    }

    /**
     * Define o valor da propriedade concedente.
     * 
     * @param value
     *     allowed object is
     *     {@link TConcedenteEstagio }
     *     
     */
    public void setConcedente(TConcedenteEstagio value) {
        this.concedente = value;
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
     * Gets the value of the cargaHorariaEmHorasRelogio property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cargaHorariaEmHorasRelogio property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCargaHorariaEmHorasRelogio().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link THoraRelogioComEtiqueta }
     * 
     * 
     */
    public List<THoraRelogioComEtiqueta> getCargaHorariaEmHorasRelogio() {
        if (cargaHorariaEmHorasRelogio == null) {
            cargaHorariaEmHorasRelogio = new ArrayList<THoraRelogioComEtiqueta>();
        }
        return this.cargaHorariaEmHorasRelogio;
    }

    /**
     * Obtém o valor da propriedade docentesOrientadores.
     * 
     * @return
     *     possible object is
     *     {@link TDocentes }
     *     
     */
    public TDocentes getDocentesOrientadores() {
        return docentesOrientadores;
    }

    /**
     * Define o valor da propriedade docentesOrientadores.
     * 
     * @param value
     *     allowed object is
     *     {@link TDocentes }
     *     
     */
    public void setDocentesOrientadores(TDocentes value) {
        this.docentesOrientadores = value;
    }

}
