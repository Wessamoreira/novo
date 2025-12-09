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


/**
 * Dados do curso de universidades fora do sistema federal - flexibiliza algumas exigências
 * 
 * <p>Classe Java de TDadosCursoNSF complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDadosCursoNSF">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NomeCurso" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="CodigoCursoEMEC" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodCursoMEC"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element name="SemCodigoCursoEMEC" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInformacoesTramitacaoEMEC"/>
 *           &lt;/sequence>
 *         &lt;/choice>
 *         &lt;element name="Habilitacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THabilitacao" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Modalidade" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TModalidadeCursoNSF"/>
 *         &lt;element name="TituloConferido" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TTituloConferido"/>
 *         &lt;element name="GrauConferido" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TGrauConferido"/>
 *         &lt;element name="EnderecoCurso" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEndereco"/>
 *         &lt;element name="Polo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TPolo" minOccurs="0"/>
 *         &lt;element name="Autorizacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAtoRegulatorioComOuSemEMEC" minOccurs="0"/>
 *         &lt;element name="Reconhecimento" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAtoRegulatorioComOuSemEMEC" minOccurs="0"/>
 *         &lt;element name="RenovacaoReconhecimento" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAtoRegulatorioComOuSemEMEC" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDadosCursoNSF", propOrder = {
    "nomeCurso",
    "codigoCursoEMEC",
    "semCodigoCursoEMEC",
    "habilitacao",
    "modalidade",
    "tituloConferido",
    "grauConferido",
    "enderecoCurso",
    "polo",
    "autorizacao",
    "reconhecimento",
    "renovacaoReconhecimento"
})
public class TDadosCursoNSF {

    @XmlElement(name = "NomeCurso", required = true)
    protected String nomeCurso;
    @XmlElement(name = "CodigoCursoEMEC")
    @XmlSchemaType(name = "unsignedInt")
    protected Long codigoCursoEMEC;
    @XmlElement(name = "SemCodigoCursoEMEC")
    protected TInformacoesTramitacaoEMEC semCodigoCursoEMEC;
    @XmlElement(name = "Habilitacao")
    protected List<THabilitacao> habilitacao;
    @XmlElement(name = "Modalidade", required = true)
    protected String modalidade;
    @XmlElement(name = "TituloConferido", required = true)
    protected TTituloConferido tituloConferido;
    @XmlElement(name = "GrauConferido", required = true)
    @XmlSchemaType(name = "string")
    protected TGrauConferido grauConferido;
    @XmlElement(name = "EnderecoCurso", required = true)
    protected TEndereco enderecoCurso;
    @XmlElement(name = "Polo")
    protected TPolo polo;
    @XmlElement(name = "Autorizacao")
    protected TAtoRegulatorioComOuSemEMEC autorizacao;
    @XmlElement(name = "Reconhecimento")
    protected TAtoRegulatorioComOuSemEMEC reconhecimento;
    @XmlElement(name = "RenovacaoReconhecimento")
    protected TAtoRegulatorioComOuSemEMEC renovacaoReconhecimento;

    /**
     * Obtém o valor da propriedade nomeCurso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeCurso() {
        return nomeCurso;
    }

    /**
     * Define o valor da propriedade nomeCurso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeCurso(String value) {
        this.nomeCurso = value;
    }

    /**
     * Obtém o valor da propriedade codigoCursoEMEC.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodigoCursoEMEC() {
        return codigoCursoEMEC;
    }

    /**
     * Define o valor da propriedade codigoCursoEMEC.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodigoCursoEMEC(Long value) {
        this.codigoCursoEMEC = value;
    }

    /**
     * Obtém o valor da propriedade semCodigoCursoEMEC.
     * 
     * @return
     *     possible object is
     *     {@link TInformacoesTramitacaoEMEC }
     *     
     */
    public TInformacoesTramitacaoEMEC getSemCodigoCursoEMEC() {
        return semCodigoCursoEMEC;
    }

    /**
     * Define o valor da propriedade semCodigoCursoEMEC.
     * 
     * @param value
     *     allowed object is
     *     {@link TInformacoesTramitacaoEMEC }
     *     
     */
    public void setSemCodigoCursoEMEC(TInformacoesTramitacaoEMEC value) {
        this.semCodigoCursoEMEC = value;
    }

    /**
     * Gets the value of the habilitacao property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the habilitacao property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHabilitacao().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link THabilitacao }
     * 
     * 
     */
    public List<THabilitacao> getHabilitacao() {
        if (habilitacao == null) {
            habilitacao = new ArrayList<THabilitacao>();
        }
        return this.habilitacao;
    }

    /**
     * Obtém o valor da propriedade modalidade.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModalidade() {
        return modalidade;
    }

    /**
     * Define o valor da propriedade modalidade.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModalidade(String value) {
        this.modalidade = value;
    }

    /**
     * Obtém o valor da propriedade tituloConferido.
     * 
     * @return
     *     possible object is
     *     {@link TTituloConferido }
     *     
     */
    public TTituloConferido getTituloConferido() {
        return tituloConferido;
    }

    /**
     * Define o valor da propriedade tituloConferido.
     * 
     * @param value
     *     allowed object is
     *     {@link TTituloConferido }
     *     
     */
    public void setTituloConferido(TTituloConferido value) {
        this.tituloConferido = value;
    }

    /**
     * Obtém o valor da propriedade grauConferido.
     * 
     * @return
     *     possible object is
     *     {@link TGrauConferido }
     *     
     */
    public TGrauConferido getGrauConferido() {
        return grauConferido;
    }

    /**
     * Define o valor da propriedade grauConferido.
     * 
     * @param value
     *     allowed object is
     *     {@link TGrauConferido }
     *     
     */
    public void setGrauConferido(TGrauConferido value) {
        this.grauConferido = value;
    }

    /**
     * Obtém o valor da propriedade enderecoCurso.
     * 
     * @return
     *     possible object is
     *     {@link TEndereco }
     *     
     */
    public TEndereco getEnderecoCurso() {
        return enderecoCurso;
    }

    /**
     * Define o valor da propriedade enderecoCurso.
     * 
     * @param value
     *     allowed object is
     *     {@link TEndereco }
     *     
     */
    public void setEnderecoCurso(TEndereco value) {
        this.enderecoCurso = value;
    }

    /**
     * Obtém o valor da propriedade polo.
     * 
     * @return
     *     possible object is
     *     {@link TPolo }
     *     
     */
    public TPolo getPolo() {
        return polo;
    }

    /**
     * Define o valor da propriedade polo.
     * 
     * @param value
     *     allowed object is
     *     {@link TPolo }
     *     
     */
    public void setPolo(TPolo value) {
        this.polo = value;
    }

    /**
     * Obtém o valor da propriedade autorizacao.
     * 
     * @return
     *     possible object is
     *     {@link TAtoRegulatorioComOuSemEMEC }
     *     
     */
    public TAtoRegulatorioComOuSemEMEC getAutorizacao() {
        return autorizacao;
    }

    /**
     * Define o valor da propriedade autorizacao.
     * 
     * @param value
     *     allowed object is
     *     {@link TAtoRegulatorioComOuSemEMEC }
     *     
     */
    public void setAutorizacao(TAtoRegulatorioComOuSemEMEC value) {
        this.autorizacao = value;
    }

    /**
     * Obtém o valor da propriedade reconhecimento.
     * 
     * @return
     *     possible object is
     *     {@link TAtoRegulatorioComOuSemEMEC }
     *     
     */
    public TAtoRegulatorioComOuSemEMEC getReconhecimento() {
        return reconhecimento;
    }

    /**
     * Define o valor da propriedade reconhecimento.
     * 
     * @param value
     *     allowed object is
     *     {@link TAtoRegulatorioComOuSemEMEC }
     *     
     */
    public void setReconhecimento(TAtoRegulatorioComOuSemEMEC value) {
        this.reconhecimento = value;
    }

    /**
     * Obtém o valor da propriedade renovacaoReconhecimento.
     * 
     * @return
     *     possible object is
     *     {@link TAtoRegulatorioComOuSemEMEC }
     *     
     */
    public TAtoRegulatorioComOuSemEMEC getRenovacaoReconhecimento() {
        return renovacaoReconhecimento;
    }

    /**
     * Define o valor da propriedade renovacaoReconhecimento.
     * 
     * @param value
     *     allowed object is
     *     {@link TAtoRegulatorioComOuSemEMEC }
     *     
     */
    public void setRenovacaoReconhecimento(TAtoRegulatorioComOuSemEMEC value) {
        this.renovacaoReconhecimento = value;
    }

}
