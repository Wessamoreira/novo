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
 * Dados do polo
 * 
 * <p>Classe Java de TPolo complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TPolo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Nome" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;element name="Endereco" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEndereco"/>
 *         &lt;choice>
 *           &lt;sequence>
 *             &lt;element name="CodigoEMEC" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodPoloMEC"/>
 *           &lt;/sequence>
 *           &lt;sequence>
 *             &lt;element name="SemCodigoEMEC" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInformacoesTramitacaoEMEC"/>
 *           &lt;/sequence>
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
@XmlType(name = "TPolo", propOrder = {
    "nome",
    "endereco",
    "codigoEMEC",
    "semCodigoEMEC"
})
public class TPolo {

    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "Endereco", required = true)
    protected TEndereco endereco;
    @XmlElement(name = "CodigoEMEC")
    @XmlSchemaType(name = "unsignedInt")
    protected Long codigoEMEC;
    @XmlElement(name = "SemCodigoEMEC")
    protected TInformacoesTramitacaoEMEC semCodigoEMEC;

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
     * Obtém o valor da propriedade codigoEMEC.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodigoEMEC() {
        return codigoEMEC;
    }

    /**
     * Define o valor da propriedade codigoEMEC.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodigoEMEC(Long value) {
        this.codigoEMEC = value;
    }

    /**
     * Obtém o valor da propriedade semCodigoEMEC.
     * 
     * @return
     *     possible object is
     *     {@link TInformacoesTramitacaoEMEC }
     *     
     */
    public TInformacoesTramitacaoEMEC getSemCodigoEMEC() {
        return semCodigoEMEC;
    }

    /**
     * Define o valor da propriedade semCodigoEMEC.
     * 
     * @param value
     *     allowed object is
     *     {@link TInformacoesTramitacaoEMEC }
     *     
     */
    public void setSemCodigoEMEC(TInformacoesTramitacaoEMEC value) {
        this.semCodigoEMEC = value;
    }

}
