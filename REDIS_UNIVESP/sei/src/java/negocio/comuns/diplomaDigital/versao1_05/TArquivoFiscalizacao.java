//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.04.27 às 10:26:01 AM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Lista de Diplomas Emitidos e Registrados em posse da IES para fiscalização pelo MEC
 * 
 * <p>Classe Java de TArquivoFiscalizacao complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TArquivoFiscalizacao">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="infArquivoFiscalizacaoEmissora" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInfArquivoFiscalizacaoEmissora"/>
 *           &lt;element name="infArquivoFiscalizacaoRegistradora" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInfArquivoFiscalizacaoRegistradora"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TArquivoFiscalizacao", propOrder = {
    "infArquivoFiscalizacaoEmissora",
    "infArquivoFiscalizacaoRegistradora",
    "signature"
})
public class TArquivoFiscalizacao {

    protected TInfArquivoFiscalizacaoEmissora infArquivoFiscalizacaoEmissora;
    protected TInfArquivoFiscalizacaoRegistradora infArquivoFiscalizacaoRegistradora;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected SignatureType signature;

    /**
     * Obtém o valor da propriedade infArquivoFiscalizacaoEmissora.
     * 
     * @return
     *     possible object is
     *     {@link TInfArquivoFiscalizacaoEmissora }
     *     
     */
    public TInfArquivoFiscalizacaoEmissora getInfArquivoFiscalizacaoEmissora() {
        return infArquivoFiscalizacaoEmissora;
    }

    /**
     * Define o valor da propriedade infArquivoFiscalizacaoEmissora.
     * 
     * @param value
     *     allowed object is
     *     {@link TInfArquivoFiscalizacaoEmissora }
     *     
     */
    public void setInfArquivoFiscalizacaoEmissora(TInfArquivoFiscalizacaoEmissora value) {
        this.infArquivoFiscalizacaoEmissora = value;
    }

    /**
     * Obtém o valor da propriedade infArquivoFiscalizacaoRegistradora.
     * 
     * @return
     *     possible object is
     *     {@link TInfArquivoFiscalizacaoRegistradora }
     *     
     */
    public TInfArquivoFiscalizacaoRegistradora getInfArquivoFiscalizacaoRegistradora() {
        return infArquivoFiscalizacaoRegistradora;
    }

    /**
     * Define o valor da propriedade infArquivoFiscalizacaoRegistradora.
     * 
     * @param value
     *     allowed object is
     *     {@link TInfArquivoFiscalizacaoRegistradora }
     *     
     */
    public void setInfArquivoFiscalizacaoRegistradora(TInfArquivoFiscalizacaoRegistradora value) {
        this.infArquivoFiscalizacaoRegistradora = value;
    }

    /**
     * Obtém o valor da propriedade signature.
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Define o valor da propriedade signature.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

}
