//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.25 às 05:38:08 PM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de TEntradaHistoricoDisciplina complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TEntradaHistoricoDisciplina">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CodigoDisciplina" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodigoUnidadeCurricular"/>
 *         &lt;element name="NomeDisciplina" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;element name="PeriodoLetivo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;element name="CargaHoraria" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCargaHorariaComEtiqueta" maxOccurs="unbounded"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="Nota" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNota"/>
 *           &lt;element name="NotaAteCem" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNotaAteCem"/>
 *           &lt;element name="Conceito" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TConceito"/>
 *           &lt;element name="ConceitoRM" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TConceitoRM"/>
 *           &lt;element name="ConceitoEspecificoDoCurso" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;/choice>
 *         &lt;choice>
 *           &lt;element name="Aprovado" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDisciplinaAprovada"/>
 *           &lt;element name="Pendente" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TVazio"/>
 *           &lt;element name="Reprovado" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TVazio"/>
 *         &lt;/choice>
 *         &lt;element name="Docentes" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDocentes"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TEntradaHistoricoDisciplina", propOrder = {
    "codigoDisciplina",
    "nomeDisciplina",
    "periodoLetivo",
    "cargaHoraria",
    "nota",
    "notaAteCem",
    "conceito",
    "conceitoRM",
    "conceitoEspecificoDoCurso",
    "aprovado",
    "pendente",
    "reprovado",
    "docentes"
})
public class TEntradaHistoricoDisciplina {

    @XmlElement(name = "CodigoDisciplina", required = true)
    protected String codigoDisciplina;
    @XmlElement(name = "NomeDisciplina", required = true)
    protected String nomeDisciplina;
    @XmlElement(name = "PeriodoLetivo", required = true)
    protected String periodoLetivo;
    @XmlElement(name = "CargaHoraria", required = true)
    protected List<TCargaHorariaComEtiqueta> cargaHoraria;
    @XmlElement(name = "Nota")
    protected BigDecimal nota;
    @XmlElement(name = "NotaAteCem")
    protected BigDecimal notaAteCem;
    @XmlElement(name = "Conceito")
    protected String conceito;
    @XmlElement(name = "ConceitoRM")
    @XmlSchemaType(name = "string")
    protected TConceitoRM conceitoRM;
    @XmlElement(name = "ConceitoEspecificoDoCurso")
    protected String conceitoEspecificoDoCurso;
    @XmlElement(name = "Aprovado")
    protected TDisciplinaAprovada aprovado;
    @XmlElement(name = "Pendente")
    protected TVazio pendente;
    @XmlElement(name = "Reprovado")
    protected TVazio reprovado;
    @XmlElement(name = "Docentes", required = true)
    protected TDocentes docentes;

    /**
     * Obtém o valor da propriedade codigoDisciplina.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoDisciplina() {
        return codigoDisciplina;
    }

    /**
     * Define o valor da propriedade codigoDisciplina.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoDisciplina(String value) {
        this.codigoDisciplina = value;
    }

    /**
     * Obtém o valor da propriedade nomeDisciplina.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    /**
     * Define o valor da propriedade nomeDisciplina.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeDisciplina(String value) {
        this.nomeDisciplina = value;
    }

    /**
     * Obtém o valor da propriedade periodoLetivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeriodoLetivo() {
        return periodoLetivo;
    }

    /**
     * Define o valor da propriedade periodoLetivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeriodoLetivo(String value) {
        this.periodoLetivo = value;
    }

    /**
     * Gets the value of the cargaHoraria property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cargaHoraria property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCargaHoraria().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TCargaHorariaComEtiqueta }
     * 
     * 
     */
    public List<TCargaHorariaComEtiqueta> getCargaHoraria() {
        if (cargaHoraria == null) {
            cargaHoraria = new ArrayList<TCargaHorariaComEtiqueta>();
        }
        return this.cargaHoraria;
    }

    /**
     * Obtém o valor da propriedade nota.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNota() {
        return nota;
    }

    /**
     * Define o valor da propriedade nota.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNota(BigDecimal value) {
        this.nota = value;
    }

    /**
     * Obtém o valor da propriedade notaAteCem.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNotaAteCem() {
        return notaAteCem;
    }

    /**
     * Define o valor da propriedade notaAteCem.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNotaAteCem(BigDecimal value) {
        this.notaAteCem = value;
    }

    /**
     * Obtém o valor da propriedade conceito.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConceito() {
        return conceito;
    }

    /**
     * Define o valor da propriedade conceito.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConceito(String value) {
        this.conceito = value;
    }

    /**
     * Obtém o valor da propriedade conceitoRM.
     * 
     * @return
     *     possible object is
     *     {@link TConceitoRM }
     *     
     */
    public TConceitoRM getConceitoRM() {
        return conceitoRM;
    }

    /**
     * Define o valor da propriedade conceitoRM.
     * 
     * @param value
     *     allowed object is
     *     {@link TConceitoRM }
     *     
     */
    public void setConceitoRM(TConceitoRM value) {
        this.conceitoRM = value;
    }

    /**
     * Obtém o valor da propriedade conceitoEspecificoDoCurso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConceitoEspecificoDoCurso() {
        return conceitoEspecificoDoCurso;
    }

    /**
     * Define o valor da propriedade conceitoEspecificoDoCurso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConceitoEspecificoDoCurso(String value) {
        this.conceitoEspecificoDoCurso = value;
    }

    /**
     * Obtém o valor da propriedade aprovado.
     * 
     * @return
     *     possible object is
     *     {@link TDisciplinaAprovada }
     *     
     */
    public TDisciplinaAprovada getAprovado() {
        return aprovado;
    }

    /**
     * Define o valor da propriedade aprovado.
     * 
     * @param value
     *     allowed object is
     *     {@link TDisciplinaAprovada }
     *     
     */
    public void setAprovado(TDisciplinaAprovada value) {
        this.aprovado = value;
    }

    /**
     * Obtém o valor da propriedade pendente.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getPendente() {
        return pendente;
    }

    /**
     * Define o valor da propriedade pendente.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setPendente(TVazio value) {
        this.pendente = value;
    }

    /**
     * Obtém o valor da propriedade reprovado.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getReprovado() {
        return reprovado;
    }

    /**
     * Define o valor da propriedade reprovado.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setReprovado(TVazio value) {
        this.reprovado = value;
    }

    /**
     * Obtém o valor da propriedade docentes.
     * 
     * @return
     *     possible object is
     *     {@link TDocentes }
     *     
     */
    public TDocentes getDocentes() {
        return docentes;
    }

    /**
     * Define o valor da propriedade docentes.
     * 
     * @param value
     *     allowed object is
     *     {@link TDocentes }
     *     
     */
    public void setDocentes(TDocentes value) {
        this.docentes = value;
    }

}
