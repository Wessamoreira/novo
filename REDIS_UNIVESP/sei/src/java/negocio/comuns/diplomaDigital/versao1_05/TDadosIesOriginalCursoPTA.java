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


/**
 * Em caso da emissão de segunda via de Diploma ocorrer a partir de acervo de outra instituição absorvida pela IES
 * 				Emissora por meio de Processo de Transferência Assistida, deve-se incluir a informação da IES de origem
 * 
 * <p>Classe Java de TDadosIesOriginalCursoPTA complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDadosIesOriginalCursoPTA">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Nome" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNomeIES"/>
 *         &lt;choice>
 *           &lt;element name="CodigoMEC" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodIESMEC"/>
 *           &lt;element name="CodigoMEC_Indisponivel" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TVazio"/>
 *         &lt;/choice>
 *         &lt;element name="CNPJ" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCnpj" minOccurs="0"/>
 *         &lt;element name="Endereco" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEndereco" minOccurs="0"/>
 *         &lt;element name="Descredenciamento" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAtoRegulatorio"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDadosIesOriginalCursoPTA", propOrder = {
    "nome",
    "codigoMEC",
    "codigoMECIndisponivel",
    "cnpj",
    "endereco",
    "descredenciamento"
})
public class TDadosIesOriginalCursoPTA {

    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "CodigoMEC")
    @XmlSchemaType(name = "unsignedInt")
    protected Long codigoMEC;
    @XmlElement(name = "CodigoMEC_Indisponivel")
    protected TVazio codigoMECIndisponivel;
    @XmlElement(name = "CNPJ")
    protected String cnpj;
    @XmlElement(name = "Endereco")
    protected TEndereco endereco;
    @XmlElement(name = "Descredenciamento", required = true)
    protected TAtoRegulatorio descredenciamento;

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
     * Obtém o valor da propriedade codigoMEC.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodigoMEC() {
        return codigoMEC;
    }

    /**
     * Define o valor da propriedade codigoMEC.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodigoMEC(Long value) {
        this.codigoMEC = value;
    }

    /**
     * Obtém o valor da propriedade codigoMECIndisponivel.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getCodigoMECIndisponivel() {
        return codigoMECIndisponivel;
    }

    /**
     * Define o valor da propriedade codigoMECIndisponivel.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setCodigoMECIndisponivel(TVazio value) {
        this.codigoMECIndisponivel = value;
    }

    /**
     * Obtém o valor da propriedade cnpj.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCNPJ() {
        return cnpj;
    }

    /**
     * Define o valor da propriedade cnpj.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCNPJ(String value) {
        this.cnpj = value;
    }

    /**
     * Obtém o valor da propriedade endereco.
     * 
     * @return
     *     possible object is
     *     {@link TEndereco }
     *     
     */
    public TEndereco getEndereco() {
        return endereco;
    }

    /**
     * Define o valor da propriedade endereco.
     * 
     * @param value
     *     allowed object is
     *     {@link TEndereco }
     *     
     */
    public void setEndereco(TEndereco value) {
        this.endereco = value;
    }

    /**
     * Obtém o valor da propriedade descredenciamento.
     * 
     * @return
     *     possible object is
     *     {@link TAtoRegulatorio }
     *     
     */
    public TAtoRegulatorio getDescredenciamento() {
        return descredenciamento;
    }

    /**
     * Define o valor da propriedade descredenciamento.
     * 
     * @param value
     *     allowed object is
     *     {@link TAtoRegulatorio }
     *     
     */
    public void setDescredenciamento(TAtoRegulatorio value) {
        this.descredenciamento = value;
    }

}
