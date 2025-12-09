//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.04.27 às 10:26:01 AM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Informações sobre um diploma registrado
 * 
 * <p>Classe Java de TDiplomaFiscalizadoRegistradora complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDiplomaFiscalizadoRegistradora">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CodigoDiploma" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodigoValidacao"/>
 *         &lt;element name="CPFDetentor" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCpf"/>
 *         &lt;element name="CodigoEMECEmissora" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodIESMEC"/>
 *         &lt;element name="CodigoEMECCurso" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodCursoMEC" minOccurs="0"/>
 *         &lt;element name="DadosRegistro" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TLivroRegistro"/>
 *         &lt;element name="IdDocumentacaoAcademica">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}ID">
 *               &lt;pattern value="ReqDip[0-9]{44}"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
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
@XmlType(name = "TDiplomaFiscalizadoRegistradora", propOrder = {
    "codigoDiploma",
    "cpfDetentor",
    "codigoEMECEmissora",
    "codigoEMECCurso",
    "dadosRegistro",
    "idDocumentacaoAcademica"
})
public class TDiplomaFiscalizadoRegistradora {

    @XmlElement(name = "CodigoDiploma", required = true)
    protected String codigoDiploma;
    @XmlElement(name = "CPFDetentor", required = true)
    protected String cpfDetentor;
    @XmlElement(name = "CodigoEMECEmissora")
    @XmlSchemaType(name = "unsignedInt")
    protected long codigoEMECEmissora;
    @XmlElement(name = "CodigoEMECCurso")
    @XmlSchemaType(name = "unsignedInt")
    protected Long codigoEMECCurso;
    @XmlElement(name = "DadosRegistro", required = true)
    protected TLivroRegistro dadosRegistro;
    @XmlElement(name = "IdDocumentacaoAcademica", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String idDocumentacaoAcademica;

    /**
     * Obtém o valor da propriedade codigoDiploma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoDiploma() {
        return codigoDiploma;
    }

    /**
     * Define o valor da propriedade codigoDiploma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoDiploma(String value) {
        this.codigoDiploma = value;
    }

    /**
     * Obtém o valor da propriedade cpfDetentor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCPFDetentor() {
        return cpfDetentor;
    }

    /**
     * Define o valor da propriedade cpfDetentor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCPFDetentor(String value) {
        this.cpfDetentor = value;
    }

    /**
     * Obtém o valor da propriedade codigoEMECEmissora.
     * 
     */
    public long getCodigoEMECEmissora() {
        return codigoEMECEmissora;
    }

    /**
     * Define o valor da propriedade codigoEMECEmissora.
     * 
     */
    public void setCodigoEMECEmissora(long value) {
        this.codigoEMECEmissora = value;
    }

    /**
     * Obtém o valor da propriedade codigoEMECCurso.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodigoEMECCurso() {
        return codigoEMECCurso;
    }

    /**
     * Define o valor da propriedade codigoEMECCurso.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodigoEMECCurso(Long value) {
        this.codigoEMECCurso = value;
    }

    /**
     * Obtém o valor da propriedade dadosRegistro.
     * 
     * @return
     *     possible object is
     *     {@link TLivroRegistro }
     *     
     */
    public TLivroRegistro getDadosRegistro() {
        return dadosRegistro;
    }

    /**
     * Define o valor da propriedade dadosRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link TLivroRegistro }
     *     
     */
    public void setDadosRegistro(TLivroRegistro value) {
        this.dadosRegistro = value;
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

}
