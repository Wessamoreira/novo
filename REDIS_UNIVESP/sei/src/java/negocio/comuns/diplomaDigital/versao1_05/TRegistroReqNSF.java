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
 * Tipo Requisição de Registro de Diploma Digital
 * 
 * <p>Classe Java de TRegistroReqNSF complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TRegistroReqNSF">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DadosDiplomaNSF" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosDiplomaNSF"/>
 *         &lt;element name="DadosPrivadosDiplomado" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosPrivadosDiplomado"/>
 *         &lt;element name="TermoResponsabilidadeEmissora" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TTermoResponsabilidade" minOccurs="0"/>
 *         &lt;element name="DocumentacaoComprobatoria" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDocumentacaoComprobatoria" minOccurs="0"/>
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
@XmlType(name = "TRegistroReqNSF", propOrder = {
    "dadosDiplomaNSF",
    "dadosPrivadosDiplomado",
    "termoResponsabilidadeEmissora",
    "documentacaoComprobatoria"
})
public class TRegistroReqNSF {

    @XmlElement(name = "DadosDiplomaNSF", required = true)
    protected TDadosDiplomaNSF dadosDiplomaNSF;
    @XmlElement(name = "DadosPrivadosDiplomado", required = true)
    protected TDadosPrivadosDiplomado dadosPrivadosDiplomado;
    @XmlElement(name = "TermoResponsabilidadeEmissora")
    protected TTermoResponsabilidade termoResponsabilidadeEmissora;
    @XmlElement(name = "DocumentacaoComprobatoria")
    protected TDocumentacaoComprobatoria documentacaoComprobatoria;
    @XmlAttribute(name = "versao", required = true)
    protected String versao;
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;
    @XmlAttribute(name = "ambiente")
    protected TAmb ambiente;

    /**
     * Obtém o valor da propriedade dadosDiplomaNSF.
     * 
     * @return
     *     possible object is
     *     {@link TDadosDiplomaNSF }
     *     
     */
    public TDadosDiplomaNSF getDadosDiplomaNSF() {
        return dadosDiplomaNSF;
    }

    /**
     * Define o valor da propriedade dadosDiplomaNSF.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosDiplomaNSF }
     *     
     */
    public void setDadosDiplomaNSF(TDadosDiplomaNSF value) {
        this.dadosDiplomaNSF = value;
    }

    /**
     * Obtém o valor da propriedade dadosPrivadosDiplomado.
     * 
     * @return
     *     possible object is
     *     {@link TDadosPrivadosDiplomado }
     *     
     */
    public TDadosPrivadosDiplomado getDadosPrivadosDiplomado() {
        return dadosPrivadosDiplomado;
    }

    /**
     * Define o valor da propriedade dadosPrivadosDiplomado.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosPrivadosDiplomado }
     *     
     */
    public void setDadosPrivadosDiplomado(TDadosPrivadosDiplomado value) {
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
     *     {@link TDocumentacaoComprobatoria }
     *     
     */
    public TDocumentacaoComprobatoria getDocumentacaoComprobatoria() {
        return documentacaoComprobatoria;
    }

    /**
     * Define o valor da propriedade documentacaoComprobatoria.
     * 
     * @param value
     *     allowed object is
     *     {@link TDocumentacaoComprobatoria }
     *     
     */
    public void setDocumentacaoComprobatoria(TDocumentacaoComprobatoria value) {
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
