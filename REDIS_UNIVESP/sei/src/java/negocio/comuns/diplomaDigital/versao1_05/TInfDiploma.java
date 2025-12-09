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
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Tipo Diploma Digital
 * 
 * <p>Classe Java de TInfDiploma complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TInfDiploma">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;sequence>
 *           &lt;choice>
 *             &lt;element name="DadosDiploma" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosDiploma"/>
 *             &lt;element name="DadosDiplomaNSF" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosDiplomaNSF"/>
 *           &lt;/choice>
 *           &lt;choice>
 *             &lt;element name="DadosRegistro" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosRegistro"/>
 *             &lt;element name="DadosRegistroNSF" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosRegistroNSF"/>
 *           &lt;/choice>
 *         &lt;/sequence>
 *         &lt;sequence>
 *           &lt;element name="DadosDiplomaPorDecisaoJudicial" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosDiplomaPorDecisaoJudicial"/>
 *           &lt;element name="DadosRegistroPorDecisaoJudicial" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosRegistroPorDecisaoJudicial"/>
 *         &lt;/sequence>
 *       &lt;/choice>
 *       &lt;attribute name="versao" use="required" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TVersao" />
 *       &lt;attribute name="id" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}ID">
 *             &lt;pattern value="VDip[0-9]{44}"/>
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
@XmlType(name = "TInfDiploma", propOrder = {
    "dadosDiploma",
    "dadosDiplomaNSF",
    "dadosRegistro",
    "dadosRegistroNSF",
    "dadosDiplomaPorDecisaoJudicial",
    "dadosRegistroPorDecisaoJudicial"
})
public class TInfDiploma {

    @XmlElement(name = "DadosDiploma")
    protected TDadosDiploma dadosDiploma;
    @XmlElement(name = "DadosDiplomaNSF")
    protected TDadosDiplomaNSF dadosDiplomaNSF;
    @XmlElement(name = "DadosRegistro")
    protected TDadosRegistro dadosRegistro;
    @XmlElement(name = "DadosRegistroNSF")
    protected TDadosRegistroNSF dadosRegistroNSF;
    @XmlElement(name = "DadosDiplomaPorDecisaoJudicial")
    protected TDadosDiplomaPorDecisaoJudicial dadosDiplomaPorDecisaoJudicial;
    @XmlElement(name = "DadosRegistroPorDecisaoJudicial")
    protected TDadosRegistroPorDecisaoJudicial dadosRegistroPorDecisaoJudicial;
    @XmlAttribute(name = "versao", required = true)
    protected String versao;
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;
    @XmlAttribute(name = "ambiente")
    protected TAmb ambiente;

    /**
     * Obtém o valor da propriedade dadosDiploma.
     * 
     * @return
     *     possible object is
     *     {@link TDadosDiploma }
     *     
     */
    public TDadosDiploma getDadosDiploma() {
        return dadosDiploma;
    }

    /**
     * Define o valor da propriedade dadosDiploma.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosDiploma }
     *     
     */
    public void setDadosDiploma(TDadosDiploma value) {
        this.dadosDiploma = value;
    }

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
     * Obtém o valor da propriedade dadosRegistro.
     * 
     * @return
     *     possible object is
     *     {@link TDadosRegistro }
     *     
     */
    public TDadosRegistro getDadosRegistro() {
        return dadosRegistro;
    }

    /**
     * Define o valor da propriedade dadosRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosRegistro }
     *     
     */
    public void setDadosRegistro(TDadosRegistro value) {
        this.dadosRegistro = value;
    }

    /**
     * Obtém o valor da propriedade dadosRegistroNSF.
     * 
     * @return
     *     possible object is
     *     {@link TDadosRegistroNSF }
     *     
     */
    public TDadosRegistroNSF getDadosRegistroNSF() {
        return dadosRegistroNSF;
    }

    /**
     * Define o valor da propriedade dadosRegistroNSF.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosRegistroNSF }
     *     
     */
    public void setDadosRegistroNSF(TDadosRegistroNSF value) {
        this.dadosRegistroNSF = value;
    }

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
     * Obtém o valor da propriedade dadosRegistroPorDecisaoJudicial.
     * 
     * @return
     *     possible object is
     *     {@link TDadosRegistroPorDecisaoJudicial }
     *     
     */
    public TDadosRegistroPorDecisaoJudicial getDadosRegistroPorDecisaoJudicial() {
        return dadosRegistroPorDecisaoJudicial;
    }

    /**
     * Define o valor da propriedade dadosRegistroPorDecisaoJudicial.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosRegistroPorDecisaoJudicial }
     *     
     */
    public void setDadosRegistroPorDecisaoJudicial(TDadosRegistroPorDecisaoJudicial value) {
        this.dadosRegistroPorDecisaoJudicial = value;
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
