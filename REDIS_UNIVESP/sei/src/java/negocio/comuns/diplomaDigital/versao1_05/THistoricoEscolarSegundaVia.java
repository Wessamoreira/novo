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
 * Dados do historico para segundas vias de históricos nato físicos
 * 
 * <p>Classe Java de THistoricoEscolarSegundaVia complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="THistoricoEscolarSegundaVia">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CodigoCurriculo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodigoCurriculo" minOccurs="0"/>
 *         &lt;element name="ElementosHistorico" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TElementosHistoricoSegundaViaNatoFisico"/>
 *         &lt;element name="NomeParaAreas" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" minOccurs="0"/>
 *         &lt;element name="Areas" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAreasComNome" minOccurs="0"/>
 *         &lt;element name="DataEmissaoHistorico" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="HoraEmissaoHistorico" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THora"/>
 *         &lt;element name="SituacaoAtualDiscente" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TSituacaoAtualDiscente"/>
 *         &lt;element name="ENADE" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEnade" minOccurs="0"/>
 *         &lt;element name="CargaHorariaCursoIntegralizada" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCargaHoraria"/>
 *         &lt;element name="CargaHorariaCurso" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCargaHoraria"/>
 *         &lt;element name="IngressoCurso" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Data" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *                   &lt;element name="FormaAcesso" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TFormaAcessoCurso" maxOccurs="unbounded"/>
 *                   &lt;element name="AnoMesProcessoSeletivo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAnoMes" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "THistoricoEscolarSegundaVia", propOrder = {
    "codigoCurriculo",
    "elementosHistorico",
    "nomeParaAreas",
    "areas",
    "dataEmissaoHistorico",
    "horaEmissaoHistorico",
    "situacaoAtualDiscente",
    "enade",
    "cargaHorariaCursoIntegralizada",
    "cargaHorariaCurso",
    "ingressoCurso"
})
public class THistoricoEscolarSegundaVia {

    @XmlElement(name = "CodigoCurriculo")
    protected String codigoCurriculo;
    @XmlElement(name = "ElementosHistorico", required = true)
    protected TElementosHistoricoSegundaViaNatoFisico elementosHistorico;
    @XmlElement(name = "NomeParaAreas")
    protected String nomeParaAreas;
    @XmlElement(name = "Areas")
    protected TAreasComNome areas;
    @XmlElement(name = "DataEmissaoHistorico", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataEmissaoHistorico;
    @XmlElement(name = "HoraEmissaoHistorico", required = true)
    protected String horaEmissaoHistorico;
    @XmlElement(name = "SituacaoAtualDiscente", required = true)
    protected TSituacaoAtualDiscente situacaoAtualDiscente;
    @XmlElement(name = "ENADE")
    protected TEnade enade;
    @XmlElement(name = "CargaHorariaCursoIntegralizada", required = true)
    protected TCargaHoraria cargaHorariaCursoIntegralizada;
    @XmlElement(name = "CargaHorariaCurso", required = true)
    protected TCargaHoraria cargaHorariaCurso;
    @XmlElement(name = "IngressoCurso")
    protected THistoricoEscolarSegundaVia.IngressoCurso ingressoCurso;

    /**
     * Obtém o valor da propriedade codigoCurriculo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoCurriculo() {
        return codigoCurriculo;
    }

    /**
     * Define o valor da propriedade codigoCurriculo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoCurriculo(String value) {
        this.codigoCurriculo = value;
    }

    /**
     * Obtém o valor da propriedade elementosHistorico.
     * 
     * @return
     *     possible object is
     *     {@link TElementosHistoricoSegundaViaNatoFisico }
     *     
     */
    public TElementosHistoricoSegundaViaNatoFisico getElementosHistorico() {
        return elementosHistorico;
    }

    /**
     * Define o valor da propriedade elementosHistorico.
     * 
     * @param value
     *     allowed object is
     *     {@link TElementosHistoricoSegundaViaNatoFisico }
     *     
     */
    public void setElementosHistorico(TElementosHistoricoSegundaViaNatoFisico value) {
        this.elementosHistorico = value;
    }

    /**
     * Obtém o valor da propriedade nomeParaAreas.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeParaAreas() {
        return nomeParaAreas;
    }

    /**
     * Define o valor da propriedade nomeParaAreas.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeParaAreas(String value) {
        this.nomeParaAreas = value;
    }

    /**
     * Obtém o valor da propriedade areas.
     * 
     * @return
     *     possible object is
     *     {@link TAreasComNome }
     *     
     */
    public TAreasComNome getAreas() {
        return areas;
    }

    /**
     * Define o valor da propriedade areas.
     * 
     * @param value
     *     allowed object is
     *     {@link TAreasComNome }
     *     
     */
    public void setAreas(TAreasComNome value) {
        this.areas = value;
    }

    /**
     * Obtém o valor da propriedade dataEmissaoHistorico.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataEmissaoHistorico() {
        return dataEmissaoHistorico;
    }

    /**
     * Define o valor da propriedade dataEmissaoHistorico.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataEmissaoHistorico(XMLGregorianCalendar value) {
        this.dataEmissaoHistorico = value;
    }

    /**
     * Obtém o valor da propriedade horaEmissaoHistorico.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHoraEmissaoHistorico() {
        return horaEmissaoHistorico;
    }

    /**
     * Define o valor da propriedade horaEmissaoHistorico.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHoraEmissaoHistorico(String value) {
        this.horaEmissaoHistorico = value;
    }

    /**
     * Obtém o valor da propriedade situacaoAtualDiscente.
     * 
     * @return
     *     possible object is
     *     {@link TSituacaoAtualDiscente }
     *     
     */
    public TSituacaoAtualDiscente getSituacaoAtualDiscente() {
        return situacaoAtualDiscente;
    }

    /**
     * Define o valor da propriedade situacaoAtualDiscente.
     * 
     * @param value
     *     allowed object is
     *     {@link TSituacaoAtualDiscente }
     *     
     */
    public void setSituacaoAtualDiscente(TSituacaoAtualDiscente value) {
        this.situacaoAtualDiscente = value;
    }

    /**
     * Obtém o valor da propriedade enade.
     * 
     * @return
     *     possible object is
     *     {@link TEnade }
     *     
     */
    public TEnade getENADE() {
        return enade;
    }

    /**
     * Define o valor da propriedade enade.
     * 
     * @param value
     *     allowed object is
     *     {@link TEnade }
     *     
     */
    public void setENADE(TEnade value) {
        this.enade = value;
    }

    /**
     * Obtém o valor da propriedade cargaHorariaCursoIntegralizada.
     * 
     * @return
     *     possible object is
     *     {@link TCargaHoraria }
     *     
     */
    public TCargaHoraria getCargaHorariaCursoIntegralizada() {
        return cargaHorariaCursoIntegralizada;
    }

    /**
     * Define o valor da propriedade cargaHorariaCursoIntegralizada.
     * 
     * @param value
     *     allowed object is
     *     {@link TCargaHoraria }
     *     
     */
    public void setCargaHorariaCursoIntegralizada(TCargaHoraria value) {
        this.cargaHorariaCursoIntegralizada = value;
    }

    /**
     * Obtém o valor da propriedade cargaHorariaCurso.
     * 
     * @return
     *     possible object is
     *     {@link TCargaHoraria }
     *     
     */
    public TCargaHoraria getCargaHorariaCurso() {
        return cargaHorariaCurso;
    }

    /**
     * Define o valor da propriedade cargaHorariaCurso.
     * 
     * @param value
     *     allowed object is
     *     {@link TCargaHoraria }
     *     
     */
    public void setCargaHorariaCurso(TCargaHoraria value) {
        this.cargaHorariaCurso = value;
    }

    /**
     * Obtém o valor da propriedade ingressoCurso.
     * 
     * @return
     *     possible object is
     *     {@link THistoricoEscolarSegundaVia.IngressoCurso }
     *     
     */
    public THistoricoEscolarSegundaVia.IngressoCurso getIngressoCurso() {
        return ingressoCurso;
    }

    /**
     * Define o valor da propriedade ingressoCurso.
     * 
     * @param value
     *     allowed object is
     *     {@link THistoricoEscolarSegundaVia.IngressoCurso }
     *     
     */
    public void setIngressoCurso(THistoricoEscolarSegundaVia.IngressoCurso value) {
        this.ingressoCurso = value;
    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Data" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
     *         &lt;element name="FormaAcesso" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TFormaAcessoCurso" maxOccurs="unbounded"/>
     *         &lt;element name="AnoMesProcessoSeletivo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAnoMes" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "data",
        "formaAcesso",
        "anoMesProcessoSeletivo"
    })
    public static class IngressoCurso {

        @XmlElement(name = "Data", required = true)
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar data;
        @XmlElement(name = "FormaAcesso", required = true)
        @XmlSchemaType(name = "string")
        protected List<TFormaAcessoCurso> formaAcesso;
        @XmlElement(name = "AnoMesProcessoSeletivo")
        protected String anoMesProcessoSeletivo;

        /**
         * Obtém o valor da propriedade data.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getData() {
            return data;
        }

        /**
         * Define o valor da propriedade data.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setData(XMLGregorianCalendar value) {
            this.data = value;
        }

        /**
         * Gets the value of the formaAcesso property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the formaAcesso property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getFormaAcesso().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TFormaAcessoCurso }
         * 
         * 
         */
        public List<TFormaAcessoCurso> getFormaAcesso() {
            if (formaAcesso == null) {
                formaAcesso = new ArrayList<TFormaAcessoCurso>();
            }
            return this.formaAcesso;
        }

        /**
         * Obtém o valor da propriedade anoMesProcessoSeletivo.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAnoMesProcessoSeletivo() {
            return anoMesProcessoSeletivo;
        }

        /**
         * Define o valor da propriedade anoMesProcessoSeletivo.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAnoMesProcessoSeletivo(String value) {
            this.anoMesProcessoSeletivo = value;
        }

    }

}
