//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.25 às 05:38:08 PM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Tipo que define o conjunto de informações referentes a um Histórico Escolar Digital
 * 
 * <p>Classe Java de TInfHistoricoEscolar complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TInfHistoricoEscolar">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Aluno" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosDiplomado"/>
 *         &lt;choice>
 *           &lt;element name="DadosCurso" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosMinimoCurso"/>
 *           &lt;element name="DadosCursoNSF" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosMinimoCursoNSF"/>
 *         &lt;/choice>
 *         &lt;element name="IesEmissora" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosIesEmissora"/>
 *         &lt;element name="HistoricoEscolar" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THistoricoEscolar"/>
 *         &lt;element name="SegurancaHistorico" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TSegurancaHistorico"/>
 *         &lt;element name="InformacoesAdicionais" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="versao" use="required" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TVersao" />
 *       &lt;attribute name="ambiente" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAmb" default="Produção" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TInfHistoricoEscolar", propOrder = {
    "aluno",
    "dadosCurso",
    "dadosCursoNSF",
    "iesEmissora",
    "historicoEscolar",
    "segurancaHistorico",
    "informacoesAdicionais"
})
public class TInfHistoricoEscolar {

    @XmlElement(name = "Aluno", required = true)
    protected TDadosDiplomado aluno;
    @XmlElement(name = "DadosCurso")
    protected TDadosMinimoCurso dadosCurso;
    @XmlElement(name = "DadosCursoNSF")
    protected TDadosMinimoCursoNSF dadosCursoNSF;
    @XmlElement(name = "IesEmissora", required = true)
    protected TDadosIesEmissora iesEmissora;
    @XmlElement(name = "HistoricoEscolar", required = true)
    protected THistoricoEscolar historicoEscolar;
    @XmlElement(name = "SegurancaHistorico", required = true)
    protected TSegurancaHistorico segurancaHistorico;
    @XmlElement(name = "InformacoesAdicionais")
    protected String informacoesAdicionais;
    @XmlAttribute(name = "versao", required = true)
    protected String versao;
    @XmlAttribute(name = "ambiente")
    protected TAmb ambiente;

    /**
     * Obtém o valor da propriedade aluno.
     * 
     * @return
     *     possible object is
     *     {@link TDadosDiplomado }
     *     
     */
    public TDadosDiplomado getAluno() {
        return aluno;
    }

    /**
     * Define o valor da propriedade aluno.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosDiplomado }
     *     
     */
    public void setAluno(TDadosDiplomado value) {
        this.aluno = value;
    }

    /**
     * Obtém o valor da propriedade dadosCurso.
     * 
     * @return
     *     possible object is
     *     {@link TDadosMinimoCurso }
     *     
     */
    public TDadosMinimoCurso getDadosCurso() {
        return dadosCurso;
    }

    /**
     * Define o valor da propriedade dadosCurso.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosMinimoCurso }
     *     
     */
    public void setDadosCurso(TDadosMinimoCurso value) {
        this.dadosCurso = value;
    }

    /**
     * Obtém o valor da propriedade dadosCursoNSF.
     * 
     * @return
     *     possible object is
     *     {@link TDadosMinimoCursoNSF }
     *     
     */
    public TDadosMinimoCursoNSF getDadosCursoNSF() {
        return dadosCursoNSF;
    }

    /**
     * Define o valor da propriedade dadosCursoNSF.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosMinimoCursoNSF }
     *     
     */
    public void setDadosCursoNSF(TDadosMinimoCursoNSF value) {
        this.dadosCursoNSF = value;
    }

    /**
     * Obtém o valor da propriedade iesEmissora.
     * 
     * @return
     *     possible object is
     *     {@link TDadosIesEmissora }
     *     
     */
    public TDadosIesEmissora getIesEmissora() {
        return iesEmissora;
    }

    /**
     * Define o valor da propriedade iesEmissora.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosIesEmissora }
     *     
     */
    public void setIesEmissora(TDadosIesEmissora value) {
        this.iesEmissora = value;
    }

    /**
     * Obtém o valor da propriedade historicoEscolar.
     * 
     * @return
     *     possible object is
     *     {@link THistoricoEscolar }
     *     
     */
    public THistoricoEscolar getHistoricoEscolar() {
        return historicoEscolar;
    }

    /**
     * Define o valor da propriedade historicoEscolar.
     * 
     * @param value
     *     allowed object is
     *     {@link THistoricoEscolar }
     *     
     */
    public void setHistoricoEscolar(THistoricoEscolar value) {
        this.historicoEscolar = value;
    }

    /**
     * Obtém o valor da propriedade segurancaHistorico.
     * 
     * @return
     *     possible object is
     *     {@link TSegurancaHistorico }
     *     
     */
    public TSegurancaHistorico getSegurancaHistorico() {
        return segurancaHistorico;
    }

    /**
     * Define o valor da propriedade segurancaHistorico.
     * 
     * @param value
     *     allowed object is
     *     {@link TSegurancaHistorico }
     *     
     */
    public void setSegurancaHistorico(TSegurancaHistorico value) {
        this.segurancaHistorico = value;
    }

    /**
     * Obtém o valor da propriedade informacoesAdicionais.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInformacoesAdicionais() {
        return informacoesAdicionais;
    }

    /**
     * Define o valor da propriedade informacoesAdicionais.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInformacoesAdicionais(String value) {
        this.informacoesAdicionais = value;
    }

    /**
     * Obtém o valor da propriedade versao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersao() {
        return versao;
    }

    /**
     * Define o valor da propriedade versao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersao(String value) {
        this.versao = value;
    }

    /**
     * Obtém o valor da propriedade ambiente.
     * 
     * @return
     *     possible object is
     *     {@link TAmb }
     *     
     */
    public TAmb getAmbiente() {
        if (ambiente == null) {
            return TAmb.PRODUÇÃO;
        } else {
            return ambiente;
        }
    }

    /**
     * Define o valor da propriedade ambiente.
     * 
     * @param value
     *     allowed object is
     *     {@link TAmb }
     *     
     */
    public void setAmbiente(TAmb value) {
        this.ambiente = value;
    }

}
