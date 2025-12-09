//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.09.29 às 03:15:12 PM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Tipo que define o conjunto de informações referentes ao Currículo Escolar de um Curso
 * 
 * <p>Classe Java de TInfCurriculoEscolar complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TInfCurriculoEscolar">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CodigoCurriculo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodigoCurriculo"/>
 *         &lt;element name="DataCurriculo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="MinutosRelogioDaHoraAula" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNumeroPositivo"/>
 *         &lt;element name="NomeParaAreas" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="DadosCurso" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosMinimoCurso"/>
 *           &lt;element name="DadosCursoNSF" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosMinimoCursoNSF"/>
 *         &lt;/choice>
 *         &lt;element name="IesEmissora" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosIesEmissora"/>
 *         &lt;element name="infEtiquetas" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInfEtiquetas"/>
 *         &lt;element name="infAreas" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInfAreas"/>
 *         &lt;element name="infEstruturaCurricular" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInfEstruturaCurricular"/>
 *         &lt;element name="infEstruturaAtividadesComplementares" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInfEstruturaAtividadesComplementares" minOccurs="0"/>
 *         &lt;element name="infCriteriosIntegralizacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInfCriteriosIntegralizacao"/>
 *         &lt;element name="SegurancaCurriculo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TSegurancaCurriculo"/>
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
@XmlType(name = "TInfCurriculoEscolar", propOrder = {
    "codigoCurriculo",
    "dataCurriculo",
    "minutosRelogioDaHoraAula",
    "nomeParaAreas",
    "dadosCurso",
    "dadosCursoNSF",
    "iesEmissora",
    "infEtiquetas",
    "infAreas",
    "infEstruturaCurricular",
    "infEstruturaAtividadesComplementares",
    "infCriteriosIntegralizacao",
    "segurancaCurriculo",
    "informacoesAdicionais"
})
public class TInfCurriculoEscolar {

    @XmlElement(name = "CodigoCurriculo", required = true)
    protected String codigoCurriculo;
    @XmlElement(name = "DataCurriculo", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataCurriculo;
    @XmlElement(name = "MinutosRelogioDaHoraAula")
    @XmlSchemaType(name = "unsignedInt")
    protected long minutosRelogioDaHoraAula;
    @XmlElement(name = "NomeParaAreas")
    protected String nomeParaAreas;
    @XmlElement(name = "DadosCurso")
    protected TDadosMinimoCurso dadosCurso;
    @XmlElement(name = "DadosCursoNSF")
    protected TDadosMinimoCursoNSF dadosCursoNSF;
    @XmlElement(name = "IesEmissora", required = true)
    protected TDadosIesEmissora iesEmissora;
    @XmlElement(required = true)
    protected TInfEtiquetas infEtiquetas;
    @XmlElement(required = true)
    protected TInfAreas infAreas;
    @XmlElement(required = true)
    protected TInfEstruturaCurricular infEstruturaCurricular;
    protected TInfEstruturaAtividadesComplementares infEstruturaAtividadesComplementares;
    @XmlElement(required = true)
    protected TInfCriteriosIntegralizacao infCriteriosIntegralizacao;
    @XmlElement(name = "SegurancaCurriculo", required = true)
    protected TSegurancaCurriculo segurancaCurriculo;
    @XmlElement(name = "InformacoesAdicionais")
    protected String informacoesAdicionais;
    @XmlAttribute(name = "versao", required = true)
    protected String versao;
    @XmlAttribute(name = "ambiente")
    protected TAmb ambiente;

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
     * Obtém o valor da propriedade dataCurriculo.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataCurriculo() {
        return dataCurriculo;
    }

    /**
     * Define o valor da propriedade dataCurriculo.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataCurriculo(XMLGregorianCalendar value) {
        this.dataCurriculo = value;
    }

    /**
     * Obtém o valor da propriedade minutosRelogioDaHoraAula.
     * 
     */
    public long getMinutosRelogioDaHoraAula() {
        return minutosRelogioDaHoraAula;
    }

    /**
     * Define o valor da propriedade minutosRelogioDaHoraAula.
     * 
     */
    public void setMinutosRelogioDaHoraAula(long value) {
        this.minutosRelogioDaHoraAula = value;
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
     * Obtém o valor da propriedade infEtiquetas.
     * 
     * @return
     *     possible object is
     *     {@link TInfEtiquetas }
     *     
     */
    public TInfEtiquetas getInfEtiquetas() {
        return infEtiquetas;
    }

    /**
     * Define o valor da propriedade infEtiquetas.
     * 
     * @param value
     *     allowed object is
     *     {@link TInfEtiquetas }
     *     
     */
    public void setInfEtiquetas(TInfEtiquetas value) {
        this.infEtiquetas = value;
    }

    /**
     * Obtém o valor da propriedade infAreas.
     * 
     * @return
     *     possible object is
     *     {@link TInfAreas }
     *     
     */
    public TInfAreas getInfAreas() {
        return infAreas;
    }

    /**
     * Define o valor da propriedade infAreas.
     * 
     * @param value
     *     allowed object is
     *     {@link TInfAreas }
     *     
     */
    public void setInfAreas(TInfAreas value) {
        this.infAreas = value;
    }

    /**
     * Obtém o valor da propriedade infEstruturaCurricular.
     * 
     * @return
     *     possible object is
     *     {@link TInfEstruturaCurricular }
     *     
     */
    public TInfEstruturaCurricular getInfEstruturaCurricular() {
        return infEstruturaCurricular;
    }

    /**
     * Define o valor da propriedade infEstruturaCurricular.
     * 
     * @param value
     *     allowed object is
     *     {@link TInfEstruturaCurricular }
     *     
     */
    public void setInfEstruturaCurricular(TInfEstruturaCurricular value) {
        this.infEstruturaCurricular = value;
    }

    /**
     * Obtém o valor da propriedade infEstruturaAtividadesComplementares.
     * 
     * @return
     *     possible object is
     *     {@link TInfEstruturaAtividadesComplementares }
     *     
     */
    public TInfEstruturaAtividadesComplementares getInfEstruturaAtividadesComplementares() {
        return infEstruturaAtividadesComplementares;
    }

    /**
     * Define o valor da propriedade infEstruturaAtividadesComplementares.
     * 
     * @param value
     *     allowed object is
     *     {@link TInfEstruturaAtividadesComplementares }
     *     
     */
    public void setInfEstruturaAtividadesComplementares(TInfEstruturaAtividadesComplementares value) {
        this.infEstruturaAtividadesComplementares = value;
    }

    /**
     * Obtém o valor da propriedade infCriteriosIntegralizacao.
     * 
     * @return
     *     possible object is
     *     {@link TInfCriteriosIntegralizacao }
     *     
     */
    public TInfCriteriosIntegralizacao getInfCriteriosIntegralizacao() {
        return infCriteriosIntegralizacao;
    }

    /**
     * Define o valor da propriedade infCriteriosIntegralizacao.
     * 
     * @param value
     *     allowed object is
     *     {@link TInfCriteriosIntegralizacao }
     *     
     */
    public void setInfCriteriosIntegralizacao(TInfCriteriosIntegralizacao value) {
        this.infCriteriosIntegralizacao = value;
    }

    /**
     * Obtém o valor da propriedade segurancaCurriculo.
     * 
     * @return
     *     possible object is
     *     {@link TSegurancaCurriculo }
     *     
     */
    public TSegurancaCurriculo getSegurancaCurriculo() {
        return segurancaCurriculo;
    }

    /**
     * Define o valor da propriedade segurancaCurriculo.
     * 
     * @param value
     *     allowed object is
     *     {@link TSegurancaCurriculo }
     *     
     */
    public void setSegurancaCurriculo(TSegurancaCurriculo value) {
        this.segurancaCurriculo = value;
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
