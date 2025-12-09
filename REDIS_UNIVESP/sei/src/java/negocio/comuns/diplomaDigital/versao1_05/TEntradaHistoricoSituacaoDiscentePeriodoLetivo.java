//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.25 às 05:38:08 PM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de TEntradaHistoricoSituacaoDiscentePeriodoLetivo complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TEntradaHistoricoSituacaoDiscentePeriodoLetivo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PeriodoLetivo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;group ref="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TSituacaoDiscente"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TEntradaHistoricoSituacaoDiscentePeriodoLetivo", propOrder = {
    "periodoLetivo",
    "trancamento",
    "matriculadoEmDisciplina",
    "licenca",
    "intercambioInternacional",
    "intercambioNacional",
    "desistencia",
    "abandono",
    "jubilado",
    "formado",
    "outraSituacao"
})
public class TEntradaHistoricoSituacaoDiscentePeriodoLetivo {

    @XmlElement(name = "PeriodoLetivo", required = true)
    protected String periodoLetivo;
    @XmlElement(name = "Trancamento")
    protected TVazio trancamento;
    @XmlElement(name = "MatriculadoEmDisciplina")
    protected TVazio matriculadoEmDisciplina;
    @XmlElement(name = "Licenca")
    protected TVazio licenca;
    @XmlElement(name = "IntercambioInternacional")
    protected TSituacaoIntercambio intercambioInternacional;
    @XmlElement(name = "IntercambioNacional")
    protected TSituacaoIntercambio intercambioNacional;
    @XmlElement(name = "Desistencia")
    protected TVazio desistencia;
    @XmlElement(name = "Abandono")
    protected TVazio abandono;
    @XmlElement(name = "Jubilado")
    protected TVazio jubilado;
    @XmlElement(name = "Formado")
    protected TSituacaoFormado formado;
    @XmlElement(name = "OutraSituacao")
    protected String outraSituacao;

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
     * Obtém o valor da propriedade trancamento.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getTrancamento() {
        return trancamento;
    }

    /**
     * Define o valor da propriedade trancamento.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setTrancamento(TVazio value) {
        this.trancamento = value;
    }

    /**
     * Obtém o valor da propriedade matriculadoEmDisciplina.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getMatriculadoEmDisciplina() {
        return matriculadoEmDisciplina;
    }

    /**
     * Define o valor da propriedade matriculadoEmDisciplina.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setMatriculadoEmDisciplina(TVazio value) {
        this.matriculadoEmDisciplina = value;
    }

    /**
     * Obtém o valor da propriedade licenca.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getLicenca() {
        return licenca;
    }

    /**
     * Define o valor da propriedade licenca.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setLicenca(TVazio value) {
        this.licenca = value;
    }

    /**
     * Obtém o valor da propriedade intercambioInternacional.
     * 
     * @return
     *     possible object is
     *     {@link TSituacaoIntercambio }
     *     
     */
    public TSituacaoIntercambio getIntercambioInternacional() {
        return intercambioInternacional;
    }

    /**
     * Define o valor da propriedade intercambioInternacional.
     * 
     * @param value
     *     allowed object is
     *     {@link TSituacaoIntercambio }
     *     
     */
    public void setIntercambioInternacional(TSituacaoIntercambio value) {
        this.intercambioInternacional = value;
    }

    /**
     * Obtém o valor da propriedade intercambioNacional.
     * 
     * @return
     *     possible object is
     *     {@link TSituacaoIntercambio }
     *     
     */
    public TSituacaoIntercambio getIntercambioNacional() {
        return intercambioNacional;
    }

    /**
     * Define o valor da propriedade intercambioNacional.
     * 
     * @param value
     *     allowed object is
     *     {@link TSituacaoIntercambio }
     *     
     */
    public void setIntercambioNacional(TSituacaoIntercambio value) {
        this.intercambioNacional = value;
    }

    /**
     * Obtém o valor da propriedade desistencia.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getDesistencia() {
        return desistencia;
    }

    /**
     * Define o valor da propriedade desistencia.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setDesistencia(TVazio value) {
        this.desistencia = value;
    }

    /**
     * Obtém o valor da propriedade abandono.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getAbandono() {
        return abandono;
    }

    /**
     * Define o valor da propriedade abandono.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setAbandono(TVazio value) {
        this.abandono = value;
    }

    /**
     * Obtém o valor da propriedade jubilado.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getJubilado() {
        return jubilado;
    }

    /**
     * Define o valor da propriedade jubilado.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setJubilado(TVazio value) {
        this.jubilado = value;
    }

    /**
     * Obtém o valor da propriedade formado.
     * 
     * @return
     *     possible object is
     *     {@link TSituacaoFormado }
     *     
     */
    public TSituacaoFormado getFormado() {
        return formado;
    }

    /**
     * Define o valor da propriedade formado.
     * 
     * @param value
     *     allowed object is
     *     {@link TSituacaoFormado }
     *     
     */
    public void setFormado(TSituacaoFormado value) {
        this.formado = value;
    }

    /**
     * Obtém o valor da propriedade outraSituacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutraSituacao() {
        return outraSituacao;
    }

    /**
     * Define o valor da propriedade outraSituacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutraSituacao(String value) {
        this.outraSituacao = value;
    }

}
