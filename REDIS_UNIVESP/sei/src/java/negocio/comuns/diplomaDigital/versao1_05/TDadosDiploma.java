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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Tipo Diploma Digital
 * 
 * <p>Classe Java de TDadosDiploma complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDadosDiploma">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Diplomado" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosDiplomado"/>
 *         &lt;element name="DataConclusao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData" minOccurs="0"/>
 *         &lt;element name="DadosCurso" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosCurso"/>
 *         &lt;element name="DadosIesOriginalCursoPTA" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosIesOriginalCursoPTA" minOccurs="0"/>
 *         &lt;element name="IesEmissora" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosIesEmissora"/>
 *         &lt;element name="Assinantes" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInfoAssinantes" minOccurs="0"/>
 *         &lt;sequence maxOccurs="unbounded">
 *           &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature"/>
 *         &lt;/sequence>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}ID">
 *             &lt;pattern value="Dip[0-9]{44}"/>
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
@XmlType(name = "TDadosDiploma", propOrder = {
    "diplomado",
    "dataConclusao",
    "dadosCurso",
    "dadosIesOriginalCursoPTA",
    "iesEmissora",
    "assinantes",
    "signature"
})
public class TDadosDiploma {

    @XmlElement(name = "Diplomado", required = true)
    protected TDadosDiplomado diplomado;
    @XmlElement(name = "DataConclusao")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataConclusao;
    @XmlElement(name = "DadosCurso", required = true)
    protected TDadosCurso dadosCurso;
    @XmlElement(name = "DadosIesOriginalCursoPTA")
    protected TDadosIesOriginalCursoPTA dadosIesOriginalCursoPTA;
    @XmlElement(name = "IesEmissora", required = true)
    protected TDadosIesEmissora iesEmissora;
    @XmlElement(name = "Assinantes")
    protected TInfoAssinantes assinantes;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected List<SignatureType> signature;
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;

    /**
     * Obtém o valor da propriedade diplomado.
     * 
     * @return
     *     possible object is
     *     {@link TDadosDiplomado }
     *     
     */
    public TDadosDiplomado getDiplomado() {
        return diplomado;
    }

    /**
     * Define o valor da propriedade diplomado.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosDiplomado }
     *     
     */
    public void setDiplomado(TDadosDiplomado value) {
        this.diplomado = value;
    }

    /**
     * Obtém o valor da propriedade dataConclusao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataConclusao() {
        return dataConclusao;
    }

    /**
     * Define o valor da propriedade dataConclusao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataConclusao(XMLGregorianCalendar value) {
        this.dataConclusao = value;
    }

    /**
     * Obtém o valor da propriedade dadosCurso.
     * 
     * @return
     *     possible object is
     *     {@link TDadosCurso }
     *     
     */
    public TDadosCurso getDadosCurso() {
        return dadosCurso;
    }

    /**
     * Define o valor da propriedade dadosCurso.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosCurso }
     *     
     */
    public void setDadosCurso(TDadosCurso value) {
        this.dadosCurso = value;
    }

    /**
     * Obtém o valor da propriedade dadosIesOriginalCursoPTA.
     * 
     * @return
     *     possible object is
     *     {@link TDadosIesOriginalCursoPTA }
     *     
     */
    public TDadosIesOriginalCursoPTA getDadosIesOriginalCursoPTA() {
        return dadosIesOriginalCursoPTA;
    }

    /**
     * Define o valor da propriedade dadosIesOriginalCursoPTA.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosIesOriginalCursoPTA }
     *     
     */
    public void setDadosIesOriginalCursoPTA(TDadosIesOriginalCursoPTA value) {
        this.dadosIesOriginalCursoPTA = value;
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
