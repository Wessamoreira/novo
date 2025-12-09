//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.25 às 05:38:08 PM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Dados do Diplomado
 * 
 * <p>Classe Java de TDadosDiplomado complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDadosDiplomado">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TId"/>
 *         &lt;group ref="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}GPessoa"/>
 *         &lt;element name="Nacionalidade" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNacionalidade"/>
 *         &lt;element name="Naturalidade" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNaturalidade"/>
 *         &lt;element name="CPF" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCpf"/>
 *         &lt;choice>
 *           &lt;element name="RG" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TRg"/>
 *           &lt;element name="OutroDocumentoIdentificacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TOutroDocumentoIdentificacao"/>
 *         &lt;/choice>
 *         &lt;element name="DataNascimento" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDadosDiplomado", propOrder = {
    "id",
    "nome",
    "nomeSocial",
    "sexo",
    "nacionalidade",
    "naturalidade",
    "cpf",
    "rg",
    "outroDocumentoIdentificacao",
    "dataNascimento"
})
public class TDadosDiplomado {

    @XmlElement(name = "ID", required = true)
    protected String id;
    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "NomeSocial")
    protected String nomeSocial;
    @XmlElement(name = "Sexo", required = true)
    @XmlSchemaType(name = "string")
    protected TSexo sexo;
    @XmlElement(name = "Nacionalidade", required = true)
    protected String nacionalidade;
    @XmlElement(name = "Naturalidade", required = true)
    protected TNaturalidade naturalidade;
    @XmlElement(name = "CPF", required = true)
    protected String cpf;
    @XmlElement(name = "RG")
    protected TRg rg;
    @XmlElement(name = "OutroDocumentoIdentificacao")
    protected TOutroDocumentoIdentificacao outroDocumentoIdentificacao;
    @XmlElement(name = "DataNascimento", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataNascimento;

    /**
     * Obtém o valor da propriedade id.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
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
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Obtém o valor da propriedade nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o valor da propriedade nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Obtém o valor da propriedade nomeSocial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNomeSocial() {
        return nomeSocial;
    }

    /**
     * Define o valor da propriedade nomeSocial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNomeSocial(String value) {
        this.nomeSocial = value;
    }

    /**
     * Obtém o valor da propriedade sexo.
     * 
     * @return
     *     possible object is
     *     {@link TSexo }
     *     
     */
    public TSexo getSexo() {
        return sexo;
    }

    /**
     * Define o valor da propriedade sexo.
     * 
     * @param value
     *     allowed object is
     *     {@link TSexo }
     *     
     */
    public void setSexo(TSexo value) {
        this.sexo = value;
    }

    /**
     * Obtém o valor da propriedade nacionalidade.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNacionalidade() {
        return nacionalidade;
    }

    /**
     * Define o valor da propriedade nacionalidade.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNacionalidade(String value) {
        this.nacionalidade = value;
    }

    /**
     * Obtém o valor da propriedade naturalidade.
     * 
     * @return
     *     possible object is
     *     {@link TNaturalidade }
     *     
     */
    public TNaturalidade getNaturalidade() {
        return naturalidade;
    }

    /**
     * Define o valor da propriedade naturalidade.
     * 
     * @param value
     *     allowed object is
     *     {@link TNaturalidade }
     *     
     */
    public void setNaturalidade(TNaturalidade value) {
        this.naturalidade = value;
    }

    /**
     * Obtém o valor da propriedade cpf.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCPF() {
        return cpf;
    }

    /**
     * Define o valor da propriedade cpf.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCPF(String value) {
        this.cpf = value;
    }

    /**
     * Obtém o valor da propriedade rg.
     * 
     * @return
     *     possible object is
     *     {@link TRg }
     *     
     */
    public TRg getRG() {
        return rg;
    }

    /**
     * Define o valor da propriedade rg.
     * 
     * @param value
     *     allowed object is
     *     {@link TRg }
     *     
     */
    public void setRG(TRg value) {
        this.rg = value;
    }

    /**
     * Obtém o valor da propriedade outroDocumentoIdentificacao.
     * 
     * @return
     *     possible object is
     *     {@link TOutroDocumentoIdentificacao }
     *     
     */
    public TOutroDocumentoIdentificacao getOutroDocumentoIdentificacao() {
        return outroDocumentoIdentificacao;
    }

    /**
     * Define o valor da propriedade outroDocumentoIdentificacao.
     * 
     * @param value
     *     allowed object is
     *     {@link TOutroDocumentoIdentificacao }
     *     
     */
    public void setOutroDocumentoIdentificacao(TOutroDocumentoIdentificacao value) {
        this.outroDocumentoIdentificacao = value;
    }

    /**
     * Obtém o valor da propriedade dataNascimento.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataNascimento() {
        return dataNascimento;
    }

    /**
     * Define o valor da propriedade dataNascimento.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataNascimento(XMLGregorianCalendar value) {
        this.dataNascimento = value;
    }

}
