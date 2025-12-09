//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.10.04 às 06:09:36 PM BRT 
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
 * Tipo que define o conjunto de informações referentes a Lista de Diplomas Anulados
 * 
 * <p>Classe Java de TInfListaDiplomasAnulados complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TInfListaDiplomasAnulados">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NumeroDeSequencia" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNumeroPositivo"/>
 *         &lt;element name="IESRegistradora" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosIesRegistradora"/>
 *         &lt;element name="DiplomasAnulados" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDiplomasAnulados"/>
 *         &lt;element name="DataMaximaProximaAtualizacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
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
@XmlType(name = "TInfListaDiplomasAnulados", propOrder = {
    "numeroDeSequencia",
    "iesRegistradora",
    "diplomasAnulados",
    "dataMaximaProximaAtualizacao"
})
public class TInfListaDiplomasAnulados {

    @XmlElement(name = "NumeroDeSequencia")
    @XmlSchemaType(name = "unsignedInt")
    protected long numeroDeSequencia;
    @XmlElement(name = "IESRegistradora", required = true)
    protected TDadosIesRegistradora iesRegistradora;
    @XmlElement(name = "DiplomasAnulados", required = true)
    protected TDiplomasAnulados diplomasAnulados;
    @XmlElement(name = "DataMaximaProximaAtualizacao", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataMaximaProximaAtualizacao;
    @XmlAttribute(name = "versao", required = true)
    protected String versao;
    @XmlAttribute(name = "ambiente")
    protected TAmb ambiente;

    /**
     * Obtém o valor da propriedade numeroDeSequencia.
     * 
     */
    public long getNumeroDeSequencia() {
        return numeroDeSequencia;
    }

    /**
     * Define o valor da propriedade numeroDeSequencia.
     * 
     */
    public void setNumeroDeSequencia(long value) {
        this.numeroDeSequencia = value;
    }

    /**
     * Obtém o valor da propriedade iesRegistradora.
     * 
     * @return
     *     possible object is
     *     {@link TDadosIesRegistradora }
     *     
     */
    public TDadosIesRegistradora getIESRegistradora() {
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
    public void setIESRegistradora(TDadosIesRegistradora value) {
        this.iesRegistradora = value;
    }

    /**
     * Obtém o valor da propriedade diplomasAnulados.
     * 
     * @return
     *     possible object is
     *     {@link TDiplomasAnulados }
     *     
     */
    public TDiplomasAnulados getDiplomasAnulados() {
        return diplomasAnulados;
    }

    /**
     * Define o valor da propriedade diplomasAnulados.
     * 
     * @param value
     *     allowed object is
     *     {@link TDiplomasAnulados }
     *     
     */
    public void setDiplomasAnulados(TDiplomasAnulados value) {
        this.diplomasAnulados = value;
    }

    /**
     * Obtém o valor da propriedade dataMaximaProximaAtualizacao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataMaximaProximaAtualizacao() {
        return dataMaximaProximaAtualizacao;
    }

    /**
     * Define o valor da propriedade dataMaximaProximaAtualizacao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataMaximaProximaAtualizacao(XMLGregorianCalendar value) {
        this.dataMaximaProximaAtualizacao = value;
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
