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
 * Informações sobre Docente responsável pela Entrada no Histórico
 * 
 * <p>Classe Java de TDocente complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDocente">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Nome" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNome"/>
 *         &lt;element name="Titulacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TTitulacao"/>
 *         &lt;element name="Lattes" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TLattes" minOccurs="0"/>
 *         &lt;element name="CPF" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCpf" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDocente", propOrder = {
    "nome",
    "titulacao",
    "lattes",
    "cpf"
})
public class TDocente {

    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "Titulacao", required = true)
    @XmlSchemaType(name = "string")
    protected TTitulacao titulacao;
    @XmlElement(name = "Lattes")
    protected String lattes;
    @XmlElement(name = "CPF")
    protected String cpf;

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
     * Obtém o valor da propriedade titulacao.
     * 
     * @return
     *     possible object is
     *     {@link TTitulacao }
     *     
     */
    public TTitulacao getTitulacao() {
        return titulacao;
    }

    /**
     * Define o valor da propriedade titulacao.
     * 
     * @param value
     *     allowed object is
     *     {@link TTitulacao }
     *     
     */
    public void setTitulacao(TTitulacao value) {
        this.titulacao = value;
    }

    /**
     * Obtém o valor da propriedade lattes.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLattes() {
        return lattes;
    }

    /**
     * Define o valor da propriedade lattes.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLattes(String value) {
        this.lattes = value;
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

}
