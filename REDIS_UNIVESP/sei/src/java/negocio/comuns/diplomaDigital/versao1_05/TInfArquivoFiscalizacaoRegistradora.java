//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.10.04 às 06:09:59 PM BRT 
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
 * <p>Classe Java de TInfArquivoFiscalizacaoRegistradora complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TInfArquivoFiscalizacaoRegistradora">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DataInicioFiscalizacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="IESRegistradora" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosIesRegistradora"/>
 *         &lt;element name="DiplomasFiscalizados" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDiplomasFiscalizadosRegistradora"/>
 *         &lt;element name="DataFimFiscalizacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
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
@XmlType(name = "TInfArquivoFiscalizacaoRegistradora", propOrder = {
    "dataInicioFiscalizacao",
    "iesRegistradora",
    "diplomasFiscalizados",
    "dataFimFiscalizacao"
})
public class TInfArquivoFiscalizacaoRegistradora {

    @XmlElement(name = "DataInicioFiscalizacao", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataInicioFiscalizacao;
    @XmlElement(name = "IESRegistradora", required = true)
    protected TDadosIesRegistradora iesRegistradora;
    @XmlElement(name = "DiplomasFiscalizados", required = true)
    protected TDiplomasFiscalizadosRegistradora diplomasFiscalizados;
    @XmlElement(name = "DataFimFiscalizacao", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataFimFiscalizacao;
    @XmlAttribute(name = "versao", required = true)
    protected String versao;
    @XmlAttribute(name = "ambiente")
    protected TAmb ambiente;

    /**
     * Obtém o valor da propriedade dataInicioFiscalizacao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataInicioFiscalizacao() {
        return dataInicioFiscalizacao;
    }

    /**
     * Define o valor da propriedade dataInicioFiscalizacao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataInicioFiscalizacao(XMLGregorianCalendar value) {
        this.dataInicioFiscalizacao = value;
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
     * Obtém o valor da propriedade diplomasFiscalizados.
     * 
     * @return
     *     possible object is
     *     {@link TDiplomasFiscalizadosRegistradora }
     *     
     */
    public TDiplomasFiscalizadosRegistradora getDiplomasFiscalizados() {
        return diplomasFiscalizados;
    }

    /**
     * Define o valor da propriedade diplomasFiscalizados.
     * 
     * @param value
     *     allowed object is
     *     {@link TDiplomasFiscalizadosRegistradora }
     *     
     */
    public void setDiplomasFiscalizados(TDiplomasFiscalizadosRegistradora value) {
        this.diplomasFiscalizados = value;
    }

    /**
     * Obtém o valor da propriedade dataFimFiscalizacao.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataFimFiscalizacao() {
        return dataFimFiscalizacao;
    }

    /**
     * Define o valor da propriedade dataFimFiscalizacao.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataFimFiscalizacao(XMLGregorianCalendar value) {
        this.dataFimFiscalizacao = value;
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
