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
 * Dados da IES registradora
 * 
 * <p>Classe Java de TDadosIesRegistradora complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDadosIesRegistradora">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Nome" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNomeIES"/>
 *         &lt;element name="CodigoMEC" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodIESMEC"/>
 *         &lt;element name="CNPJ" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCnpj"/>
 *         &lt;element name="Endereco" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEndereco"/>
 *         &lt;element name="Credenciamento" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAtoRegulatorioComOuSemEMEC"/>
 *         &lt;element name="Recredenciamento" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAtoRegulatorioComOuSemEMEC" minOccurs="0"/>
 *         &lt;element name="RenovacaoDeRecredenciamento" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAtoRegulatorioComOuSemEMEC" minOccurs="0"/>
 *         &lt;element name="AtoRegulatorioAutorizacaoRegistro" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAtoRegulatorio" minOccurs="0"/>
 *         &lt;element name="Mantenedora">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="RazaoSocial" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TRazaoSocial"/>
 *                   &lt;element name="CNPJ" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCnpj"/>
 *                   &lt;element name="Endereco" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEndereco"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
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
@XmlType(name = "TDadosIesRegistradora", propOrder = {
    "nome",
    "codigoMEC",
    "cnpj",
    "endereco",
    "credenciamento",
    "recredenciamento",
    "renovacaoDeRecredenciamento",
    "atoRegulatorioAutorizacaoRegistro",
    "mantenedora"
})
public class TDadosIesRegistradora {

    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "CodigoMEC")
    @XmlSchemaType(name = "unsignedInt")
    protected long codigoMEC;
    @XmlElement(name = "CNPJ", required = true)
    protected String cnpj;
    @XmlElement(name = "Endereco", required = true)
    protected TEndereco endereco;
    @XmlElement(name = "Credenciamento", required = true)
    protected TAtoRegulatorioComOuSemEMEC credenciamento;
    @XmlElement(name = "Recredenciamento")
    protected TAtoRegulatorioComOuSemEMEC recredenciamento;
    @XmlElement(name = "RenovacaoDeRecredenciamento")
    protected TAtoRegulatorioComOuSemEMEC renovacaoDeRecredenciamento;
    @XmlElement(name = "AtoRegulatorioAutorizacaoRegistro")
    protected TAtoRegulatorio atoRegulatorioAutorizacaoRegistro;
    @XmlElement(name = "Mantenedora", required = true)
    protected TDadosIesRegistradora.Mantenedora mantenedora;

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
     */
    public long getCodigoMEC() {
        return codigoMEC;
    }

    /**
     * Define o valor da propriedade codigoMEC.
     * 
     */
    public void setCodigoMEC(long value) {
        this.codigoMEC = value;
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
     * Obtém o valor da propriedade credenciamento.
     * 
     * @return
     *     possible object is
     *     {@link TAtoRegulatorioComOuSemEMEC }
     *     
     */
    public TAtoRegulatorioComOuSemEMEC getCredenciamento() {
        return credenciamento;
    }

    /**
     * Define o valor da propriedade credenciamento.
     * 
     * @param value
     *     allowed object is
     *     {@link TAtoRegulatorioComOuSemEMEC }
     *     
     */
    public void setCredenciamento(TAtoRegulatorioComOuSemEMEC value) {
        this.credenciamento = value;
    }

    /**
     * Obtém o valor da propriedade recredenciamento.
     * 
     * @return
     *     possible object is
     *     {@link TAtoRegulatorioComOuSemEMEC }
     *     
     */
    public TAtoRegulatorioComOuSemEMEC getRecredenciamento() {
        return recredenciamento;
    }

    /**
     * Define o valor da propriedade recredenciamento.
     * 
     * @param value
     *     allowed object is
     *     {@link TAtoRegulatorioComOuSemEMEC }
     *     
     */
    public void setRecredenciamento(TAtoRegulatorioComOuSemEMEC value) {
        this.recredenciamento = value;
    }

    /**
     * Obtém o valor da propriedade renovacaoDeRecredenciamento.
     * 
     * @return
     *     possible object is
     *     {@link TAtoRegulatorioComOuSemEMEC }
     *     
     */
    public TAtoRegulatorioComOuSemEMEC getRenovacaoDeRecredenciamento() {
        return renovacaoDeRecredenciamento;
    }

    /**
     * Define o valor da propriedade renovacaoDeRecredenciamento.
     * 
     * @param value
     *     allowed object is
     *     {@link TAtoRegulatorioComOuSemEMEC }
     *     
     */
    public void setRenovacaoDeRecredenciamento(TAtoRegulatorioComOuSemEMEC value) {
        this.renovacaoDeRecredenciamento = value;
    }

    /**
     * Obtém o valor da propriedade atoRegulatorioAutorizacaoRegistro.
     * 
     * @return
     *     possible object is
     *     {@link TAtoRegulatorio }
     *     
     */
    public TAtoRegulatorio getAtoRegulatorioAutorizacaoRegistro() {
        return atoRegulatorioAutorizacaoRegistro;
    }

    /**
     * Define o valor da propriedade atoRegulatorioAutorizacaoRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link TAtoRegulatorio }
     *     
     */
    public void setAtoRegulatorioAutorizacaoRegistro(TAtoRegulatorio value) {
        this.atoRegulatorioAutorizacaoRegistro = value;
    }

    /**
     * Obtém o valor da propriedade mantenedora.
     * 
     * @return
     *     possible object is
     *     {@link TDadosIesRegistradora.Mantenedora }
     *     
     */
    public TDadosIesRegistradora.Mantenedora getMantenedora() {
        return mantenedora;
    }

    /**
     * Define o valor da propriedade mantenedora.
     * 
     * @param value
     *     allowed object is
     *     {@link TDadosIesRegistradora.Mantenedora }
     *     
     */
    public void setMantenedora(TDadosIesRegistradora.Mantenedora value) {
        this.mantenedora = value;
    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="RazaoSocial" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TRazaoSocial"/>
     *         &lt;element name="CNPJ" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCnpj"/>
     *         &lt;element name="Endereco" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEndereco"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "razaoSocial",
        "cnpj",
        "endereco"
    })
    public static class Mantenedora {

        @XmlElement(name = "RazaoSocial", required = true)
        protected String razaoSocial;
        @XmlElement(name = "CNPJ", required = true)
        protected String cnpj;
        @XmlElement(name = "Endereco", required = true)
        protected TEndereco endereco;

        /**
         * Obtém o valor da propriedade razaoSocial.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRazaoSocial() {
            return razaoSocial;
        }

        /**
         * Define o valor da propriedade razaoSocial.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRazaoSocial(String value) {
            this.razaoSocial = value;
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

    }

}
