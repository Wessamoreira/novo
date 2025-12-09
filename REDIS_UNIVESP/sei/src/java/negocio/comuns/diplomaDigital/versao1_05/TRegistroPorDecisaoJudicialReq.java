//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.20 às 10:57:34 AM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Tipo Requisição de Registro de Diploma Digital emitido por decisão judicial
 * 
 * <p>Classe Java de TRegistroPorDecisaoJudicialReq complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TRegistroPorDecisaoJudicialReq">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DadosDiplomaPorDecisaoJudicial" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosDiplomaPorDecisaoJudicial"/>
 *         &lt;element name="DadosPrivadosDiplomado" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosPrivadosDiplomadoPorDecisaoJudicial"/>
 *         &lt;element name="TermoResponsabilidadeEmissora" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TTermoResponsabilidade" minOccurs="0"/>
 *         &lt;element name="DocumentacaoComprobatoria" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDocumentacaoComprobatoriaPorDecisaoJudicial" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="versao" use="required" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TVersao" />
 *       &lt;attribute name="id" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}ID">
 *             &lt;pattern value="ReqDip[0-9]{44}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="ambiente" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAmb" default="Produção" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TRegistroPorDecisaoJudicialReq", propOrder = {
    "dadosDiplomaPorDecisaoJudicial",
    "dadosPrivadosDiplomado",
    "termoResponsabilidadeEmissora",
    "documentacaoComprobatoria"
})
public class TRegistroPorDecisaoJudicialReq {

    @XmlElement(name = "DadosDiplomaPorDecisaoJudicial", required = true)
    protected TDadosDiplomaPorDecisaoJudicial dadosDiplomaPorDecisaoJudicial;
    @XmlElement(name = "DadosPrivadosDiplomado", required = true)
    protected TDadosPrivadosDiplomadoPorDecisaoJudicial dadosPrivadosDiplomado;
    @XmlElement(name = "TermoResponsabilidadeEmissora")
    protected TTermoResponsabilidade termoResponsabilidadeEmissora;
    @XmlElement(name = "DocumentacaoComprobatoria")
    protected TDocumentacaoComprobatoriaPorDecisaoJudicial documentacaoComprobatoria;
    @XmlAttribute(name = "versao", required = true)
    protected String versao;
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;
    @XmlAttribute(name = "ambiente")
    protected TAmb ambiente;

    /**
     * Obtém o valor da propriedade dadosDiplomaPorDecisaoJudicial.
     * 
     * @return
     *     possible object is
     *     {@link TDadosDiplomaPorDecisaoJudicial }
     *     
     */
    public TDadosDiplomaPorDecisaoJudicial getDadosDiplomaPorDecisaoJudicial() {
        return dadosDiplomaPorDecisaoJudicial;
    }

    /**
     * Define o valor da propriedade dadosDiplomaPorDecisaoJudicial.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosDiplomaPorDecisaoJudicial }
     *     
     */
    public void setDadosDiplomaPorDecisaoJudicial(TDadosDiplomaPorDecisaoJudicial value) {
        this.dadosDiplomaPorDecisaoJudicial = value;
    }

    /**
     * Obtém o valor da propriedade dadosPrivadosDiplomado.
     * 
     * @return
     *     possible object is
     *     {@link TDadosPrivadosDiplomadoPorDecisaoJudicial }
     *     
     */
    public TDadosPrivadosDiplomadoPorDecisaoJudicial getDadosPrivadosDiplomado() {
        return dadosPrivadosDiplomado;
    }

    /**
     * Define o valor da propriedade dadosPrivadosDiplomado.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosPrivadosDiplomadoPorDecisaoJudicial }
     *     
     */
    public void setDadosPrivadosDiplomado(TDadosPrivadosDiplomadoPorDecisaoJudicial value) {
        this.dadosPrivadosDiplomado = value;
    }

    /**
     * Obtém o valor da propriedade termoResponsabilidadeEmissora.
     * 
     * @return
     *     possible object is
     *     {@link TTermoResponsabilidade }
     *     
     */
    public TTermoResponsabilidade getTermoResponsabilidadeEmissora() {
        return termoResponsabilidadeEmissora;
    }

    /**
     * Define o valor da propriedade termoResponsabilidadeEmissora.
     * 
     * @param value
     *     allowed object is
     *     {@link TTermoResponsabilidade }
     *     
     */
    public void setTermoResponsabilidadeEmissora(TTermoResponsabilidade value) {
        this.termoResponsabilidadeEmissora = value;
    }

    /**
     * Obtém o valor da propriedade documentacaoComprobatoria.
     * 
     * @return
     *     possible object is
     *     {@link TDocumentacaoComprobatoriaPorDecisaoJudicial }
     *     
     */
    public TDocumentacaoComprobatoriaPorDecisaoJudicial getDocumentacaoComprobatoria() {
        return documentacaoComprobatoria;
    }

    /**
     * Define o valor da propriedade documentacaoComprobatoria.
     * 
     * @param value
     *     allowed object is
     *     {@link TDocumentacaoComprobatoriaPorDecisaoJudicial }
     *     
     */
    public void setDocumentacaoComprobatoria(TDocumentacaoComprobatoriaPorDecisaoJudicial value) {
        this.documentacaoComprobatoria = value;
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
     * Obtém o valor da propriedade id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Define o valor da propriedade id.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
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
