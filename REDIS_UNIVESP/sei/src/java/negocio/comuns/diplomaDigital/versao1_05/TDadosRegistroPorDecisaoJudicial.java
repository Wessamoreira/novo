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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Tipo de dados do registro do diploma digital para registro por decisão judicial
 * 
 * <p>Classe Java de TDadosRegistroPorDecisaoJudicial complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDadosRegistroPorDecisaoJudicial">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IesRegistradora" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosIesRegistradora"/>
 *         &lt;element name="LivroRegistro" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TLivroRegistroNSF"/>
 *         &lt;element name="IdDocumentacaoAcademica">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}ID">
 *               &lt;pattern value="ReqDip[0-9]{44}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Seguranca" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TSeguranca"/>
 *         &lt;element name="InformacoesProcessoJudicial" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInformacoesProcessoJudicial"/>
 *         &lt;element name="DeclaracoesRegistradoraAcercaProcesso" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDeclaracoesAcercaProcesso" minOccurs="0"/>
 *         &lt;element name="InformacoesAdicionais" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString" minOccurs="0"/>
 *         &lt;element name="Assinantes" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInfoAssinantes" minOccurs="0"/>
 *         &lt;sequence maxOccurs="unbounded">
 *           &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}ID">
 *             &lt;pattern value="RDip[0-9]{44}"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDadosRegistroPorDecisaoJudicial", propOrder = {
    "iesRegistradora",
    "livroRegistro",
    "idDocumentacaoAcademica",
    "seguranca",
    "informacoesProcessoJudicial",
    "declaracoesRegistradoraAcercaProcesso",
    "informacoesAdicionais",
    "assinantes",
    "signature"
})
public class TDadosRegistroPorDecisaoJudicial {

    @XmlElement(name = "IesRegistradora", required = true)
    protected TDadosIesRegistradora iesRegistradora;
    @XmlElement(name = "LivroRegistro", required = true)
    protected TLivroRegistroNSF livroRegistro;
    @XmlElement(name = "IdDocumentacaoAcademica", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String idDocumentacaoAcademica;
    @XmlElement(name = "Seguranca", required = true)
    protected TSeguranca seguranca;
    @XmlElement(name = "InformacoesProcessoJudicial", required = true)
    protected TInformacoesProcessoJudicial informacoesProcessoJudicial;
    @XmlElement(name = "DeclaracoesRegistradoraAcercaProcesso")
    protected TDeclaracoesAcercaProcesso declaracoesRegistradoraAcercaProcesso;
    @XmlElement(name = "InformacoesAdicionais")
    protected String informacoesAdicionais;
    @XmlElement(name = "Assinantes")
    protected TInfoAssinantes assinantes;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected List<SignatureType> signature;
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;

    /**
     * Obtém o valor da propriedade iesRegistradora.
     * 
     * @return
     *     possible object is
     *     {@link TDadosIesRegistradora }
     *     
     */
    public TDadosIesRegistradora getIesRegistradora() {
        return iesRegistradora;
    }

    /**
     * Define o valor da propriedade iesRegistradora.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosIesRegistradora }
     *     
     */
    public void setIesRegistradora(TDadosIesRegistradora value) {
        this.iesRegistradora = value;
    }

    /**
     * Obtém o valor da propriedade livroRegistro.
     * 
     * @return
     *     possible object is
     *     {@link TLivroRegistroNSF }
     *     
     */
    public TLivroRegistroNSF getLivroRegistro() {
        return livroRegistro;
    }

    /**
     * Define o valor da propriedade livroRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link TLivroRegistroNSF }
     *     
     */
    public void setLivroRegistro(TLivroRegistroNSF value) {
        this.livroRegistro = value;
    }

    /**
     * Obtém o valor da propriedade idDocumentacaoAcademica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdDocumentacaoAcademica() {
        return idDocumentacaoAcademica;
    }

    /**
     * Define o valor da propriedade idDocumentacaoAcademica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdDocumentacaoAcademica(String value) {
        this.idDocumentacaoAcademica = value;
    }

    /**
     * Obtém o valor da propriedade seguranca.
     * 
     * @return
     *     possible object is
     *     {@link TSeguranca }
     *     
     */
    public TSeguranca getSeguranca() {
        return seguranca;
    }

    /**
     * Define o valor da propriedade seguranca.
     * 
     * @param value
     *     allowed object is
     *     {@link TSeguranca }
     *     
     */
    public void setSeguranca(TSeguranca value) {
        this.seguranca = value;
    }

    /**
     * Obtém o valor da propriedade informacoesProcessoJudicial.
     * 
     * @return
     *     possible object is
     *     {@link TInformacoesProcessoJudicial }
     *     
     */
    public TInformacoesProcessoJudicial getInformacoesProcessoJudicial() {
        return informacoesProcessoJudicial;
    }

    /**
     * Define o valor da propriedade informacoesProcessoJudicial.
     * 
     * @param value
     *     allowed object is
     *     {@link TInformacoesProcessoJudicial }
     *     
     */
    public void setInformacoesProcessoJudicial(TInformacoesProcessoJudicial value) {
        this.informacoesProcessoJudicial = value;
    }

    /**
     * Obtém o valor da propriedade declaracoesRegistradoraAcercaProcesso.
     * 
     * @return
     *     possible object is
     *     {@link TDeclaracoesAcercaProcesso }
     *     
     */
    public TDeclaracoesAcercaProcesso getDeclaracoesRegistradoraAcercaProcesso() {
        return declaracoesRegistradoraAcercaProcesso;
    }

    /**
     * Define o valor da propriedade declaracoesRegistradoraAcercaProcesso.
     * 
     * @param value
     *     allowed object is
     *     {@link TDeclaracoesAcercaProcesso }
     *     
     */
    public void setDeclaracoesRegistradoraAcercaProcesso(TDeclaracoesAcercaProcesso value) {
        this.declaracoesRegistradoraAcercaProcesso = value;
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
     * Obtém o valor da propriedade assinantes.
     * 
     * @return
     *     possible object is
     *     {@link TInfoAssinantes }
     *     
     */
    public TInfoAssinantes getAssinantes() {
        return assinantes;
    }

    /**
     * Define o valor da propriedade assinantes.
     * 
     * @param value
     *     allowed object is
     *     {@link TInfoAssinantes }
     *     
     */
    public void setAssinantes(TInfoAssinantes value) {
        this.assinantes = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signature property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignature().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SignatureType }
     * 
     * 
     */
    public List<SignatureType> getSignature() {
        if (signature == null) {
            signature = new ArrayList<SignatureType>();
        }
        return this.signature;
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

}
