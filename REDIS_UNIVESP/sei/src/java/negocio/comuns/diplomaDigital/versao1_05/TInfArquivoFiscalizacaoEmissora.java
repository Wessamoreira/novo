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
 * Tipo que define o conjunto de informações referentes ao Arquivo de Fiscalização da Emissora
 * 
 * <p>Classe Java de TInfArquivoFiscalizacaoEmissora complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TInfArquivoFiscalizacaoEmissora">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DataInicioFiscalizacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="IESEmissora" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadosIesEmissora"/>
 *         &lt;element name="DiplomasFiscalizados" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDiplomasFiscalizadosEmissora"/>
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
@XmlType(name = "TInfArquivoFiscalizacaoEmissora", propOrder = {
    "dataInicioFiscalizacao",
    "iesEmissora",
    "diplomasFiscalizados",
    "dataFimFiscalizacao"
})
public class TInfArquivoFiscalizacaoEmissora {

    @XmlElement(name = "DataInicioFiscalizacao", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataInicioFiscalizacao;
    @XmlElement(name = "IESEmissora", required = true)
    protected TDadosIesEmissora iesEmissora;
    @XmlElement(name = "DiplomasFiscalizados", required = true)
    protected TDiplomasFiscalizadosEmissora diplomasFiscalizados;
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
     * Obtém o valor da propriedade iesEmissora.
     * 
     * @return
     *     possible object is
     *     {@link TDadosIesEmissora }
     *     
     */
    public TDadosIesEmissora getIESEmissora() {
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
    public void setIESEmissora(TDadosIesEmissora value) {
        this.iesEmissora = value;
    }

    /**
     * Obtém o valor da propriedade diplomasFiscalizados.
     * 
     * @return
     *     possible object is
     *     {@link TDiplomasFiscalizadosEmissora }
     *     
     */
    public TDiplomasFiscalizadosEmissora getDiplomasFiscalizados() {
        return diplomasFiscalizados;
    }

    /**
     * Define o valor da propriedade diplomasFiscalizados.
     * 
     * @param value
     *     allowed object is
     *     {@link TDiplomasFiscalizadosEmissora }
     *     
     */
    public void setDiplomasFiscalizados(TDiplomasFiscalizadosEmissora value) {
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
