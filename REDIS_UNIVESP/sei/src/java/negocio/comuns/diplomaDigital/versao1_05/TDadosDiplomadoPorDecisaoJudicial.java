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
 * Dados do Diplomado com flexibilizações por decisão judicial
 * 
 * <p>Classe Java de TDadosDiplomadoPorDecisaoJudicial complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDadosDiplomadoPorDecisaoJudicial">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="ID" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TId"/>
 *           &lt;element name="ID_Indisponivel" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TVazio"/>
 *         &lt;/choice>
 *         &lt;element name="Nome" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNome"/>
 *         &lt;element name="NomeSocial" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNome" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="Sexo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TSexo"/>
 *           &lt;element name="Sexo_Indisponivel" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TVazio"/>
 *         &lt;/choice>
 *         &lt;choice>
 *           &lt;element name="Nacionalidade" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNacionalidade"/>
 *           &lt;element name="Nacionalidade_Indisponivel" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TVazio"/>
 *         &lt;/choice>
 *         &lt;choice>
 *           &lt;element name="Naturalidade" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNaturalidade"/>
 *           &lt;element name="Naturalidade_Indisponivel" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TVazio"/>
 *         &lt;/choice>
 *         &lt;element name="CPF" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCpf"/>
 *         &lt;choice>
 *           &lt;element name="RG" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TRg"/>
 *           &lt;element name="OutroDocumentoIdentificacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TOutroDocumentoIdentificacao"/>
 *         &lt;/choice>
 *         &lt;choice>
 *           &lt;element name="DataNascimento" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *           &lt;element name="DataNascimento_Indisponivel" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TVazio"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDadosDiplomadoPorDecisaoJudicial", propOrder = {
    "id",
    "idIndisponivel",
    "nome",
    "nomeSocial",
    "sexo",
    "sexoIndisponivel",
    "nacionalidade",
    "nacionalidadeIndisponivel",
    "naturalidade",
    "naturalidadeIndisponivel",
    "cpf",
    "rg",
    "outroDocumentoIdentificacao",
    "dataNascimento",
    "dataNascimentoIndisponivel"
})
public class TDadosDiplomadoPorDecisaoJudicial {

    @XmlElement(name = "ID")
    protected String id;
    @XmlElement(name = "ID_Indisponivel")
    protected TVazio idIndisponivel;
    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "NomeSocial")
    protected String nomeSocial;
    @XmlElement(name = "Sexo")
    @XmlSchemaType(name = "string")
    protected TSexo sexo;
    @XmlElement(name = "Sexo_Indisponivel")
    protected TVazio sexoIndisponivel;
    @XmlElement(name = "Nacionalidade")
    protected String nacionalidade;
    @XmlElement(name = "Nacionalidade_Indisponivel")
    protected TVazio nacionalidadeIndisponivel;
    @XmlElement(name = "Naturalidade")
    protected TNaturalidade naturalidade;
    @XmlElement(name = "Naturalidade_Indisponivel")
    protected TVazio naturalidadeIndisponivel;
    @XmlElement(name = "CPF", required = true)
    protected String cpf;
    @XmlElement(name = "RG")
    protected TRg rg;
    @XmlElement(name = "OutroDocumentoIdentificacao")
    protected TOutroDocumentoIdentificacao outroDocumentoIdentificacao;
    @XmlElement(name = "DataNascimento")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataNascimento;
    @XmlElement(name = "DataNascimento_Indisponivel")
    protected TVazio dataNascimentoIndisponivel;

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
     * Obtém o valor da propriedade idIndisponivel.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getIDIndisponivel() {
        return idIndisponivel;
    }

    /**
     * Define o valor da propriedade idIndisponivel.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setIDIndisponivel(TVazio value) {
        this.idIndisponivel = value;
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
     * Obtém o valor da propriedade sexoIndisponivel.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getSexoIndisponivel() {
        return sexoIndisponivel;
    }

    /**
     * Define o valor da propriedade sexoIndisponivel.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setSexoIndisponivel(TVazio value) {
        this.sexoIndisponivel = value;
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
     * Obtém o valor da propriedade nacionalidadeIndisponivel.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getNacionalidadeIndisponivel() {
        return nacionalidadeIndisponivel;
    }

    /**
     * Define o valor da propriedade nacionalidadeIndisponivel.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setNacionalidadeIndisponivel(TVazio value) {
        this.nacionalidadeIndisponivel = value;
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
     * Obtém o valor da propriedade naturalidadeIndisponivel.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getNaturalidadeIndisponivel() {
        return naturalidadeIndisponivel;
    }

    /**
     * Define o valor da propriedade naturalidadeIndisponivel.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setNaturalidadeIndisponivel(TVazio value) {
        this.naturalidadeIndisponivel = value;
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

    /**
     * Obtém o valor da propriedade dataNascimentoIndisponivel.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getDataNascimentoIndisponivel() {
        return dataNascimentoIndisponivel;
    }

    /**
     * Define o valor da propriedade dataNascimentoIndisponivel.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setDataNascimentoIndisponivel(TVazio value) {
        this.dataNascimentoIndisponivel = value;
    }

}
