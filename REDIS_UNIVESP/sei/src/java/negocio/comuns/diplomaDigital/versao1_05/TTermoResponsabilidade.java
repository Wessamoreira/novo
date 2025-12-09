//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.20 às 10:57:34 AM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Tipo Termo Responsabilidade
 * 
 * <p>Classe Java de TTermoResponsabilidade complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TTermoResponsabilidade">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Nome" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TNome"/>
 *         &lt;element name="CPF" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCpf"/>
 *         &lt;element name="Cargo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;element name="AtoDesignacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TPdfA" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TTermoResponsabilidade", propOrder = {
    "nome",
    "cpf",
    "cargo",
    "atoDesignacao"
})
public class TTermoResponsabilidade {

    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "CPF", required = true)
    protected String cpf;
    @XmlElement(name = "Cargo", required = true)
    protected String cargo;
    @XmlElement(name = "AtoDesignacao")
    protected byte[] atoDesignacao;

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
     * Obtém o valor da propriedade cargo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCargo() {
        return cargo;
    }

    /**
     * Define o valor da propriedade cargo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCargo(String value) {
        this.cargo = value;
    }

    /**
     * Obtém o valor da propriedade atoDesignacao.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getAtoDesignacao() {
        return atoDesignacao;
    }

    /**
     * Define o valor da propriedade atoDesignacao.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setAtoDesignacao(byte[] value) {
        this.atoDesignacao = value;
    }

}
