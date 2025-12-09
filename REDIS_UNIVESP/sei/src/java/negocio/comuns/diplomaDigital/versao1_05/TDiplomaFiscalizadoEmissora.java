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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Informaçãoes sobre um diploma emitido
 * 
 * <p>Classe Java de TDiplomaFiscalizadoEmissora complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDiplomaFiscalizadoEmissora">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CodigoDiploma" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodigoValidacao"/>
 *         &lt;element name="CPFDetentor" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCpf"/>
 *         &lt;element name="CodigoEMECCurso" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodCursoMEC" minOccurs="0"/>
 *         &lt;element name="DataEmissao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="DataRegistro" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="URLXMLdoDiplomado" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THttpsURL"/>
 *         &lt;element name="URLRVDD" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THttpsURL"/>
 *         &lt;element name="URLXMLdeRegistroAcademico" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THttpsURL" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDiplomaFiscalizadoEmissora", propOrder = {
    "codigoDiploma",
    "cpfDetentor",
    "codigoEMECCurso",
    "dataEmissao",
    "dataRegistro",
    "urlxmLdoDiplomado",
    "urlrvdd",
    "urlxmLdeRegistroAcademico"
})
public class TDiplomaFiscalizadoEmissora {

    @XmlElement(name = "CodigoDiploma", required = true)
    protected String codigoDiploma;
    @XmlElement(name = "CPFDetentor", required = true)
    protected String cpfDetentor;
    @XmlElement(name = "CodigoEMECCurso")
    @XmlSchemaType(name = "unsignedInt")
    protected Long codigoEMECCurso;
    @XmlElement(name = "DataEmissao", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataEmissao;
    @XmlElement(name = "DataRegistro", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataRegistro;
    @XmlElement(name = "URLXMLdoDiplomado", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String urlxmLdoDiplomado;
    @XmlElement(name = "URLRVDD", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String urlrvdd;
    @XmlElement(name = "URLXMLdeRegistroAcademico")
    @XmlSchemaType(name = "anyURI")
    protected String urlxmLdeRegistroAcademico;

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
     * Obtém o valor da propriedade dataEmissao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataEmissao() {
        return dataEmissao;
    }

    /**
     * Define o valor da propriedade dataEmissao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataEmissao(XMLGregorianCalendar value) {
        this.dataEmissao = value;
    }

    /**
     * Obtém o valor da propriedade dataRegistro.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataRegistro() {
        return dataRegistro;
    }

    /**
     * Define o valor da propriedade dataRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataRegistro(XMLGregorianCalendar value) {
        this.dataRegistro = value;
    }

    /**
     * Obtém o valor da propriedade urlxmLdoDiplomado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURLXMLdoDiplomado() {
        return urlxmLdoDiplomado;
    }

    /**
     * Define o valor da propriedade urlxmLdoDiplomado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURLXMLdoDiplomado(String value) {
        this.urlxmLdoDiplomado = value;
    }

    /**
     * Obtém o valor da propriedade urlrvdd.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURLRVDD() {
        return urlrvdd;
    }

    /**
     * Define o valor da propriedade urlrvdd.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURLRVDD(String value) {
        this.urlrvdd = value;
    }

    /**
     * Obtém o valor da propriedade urlxmLdeRegistroAcademico.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURLXMLdeRegistroAcademico() {
        return urlxmLdeRegistroAcademico;
    }

    /**
     * Define o valor da propriedade urlxmLdeRegistroAcademico.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURLXMLdeRegistroAcademico(String value) {
        this.urlxmLdeRegistroAcademico = value;
    }

}
