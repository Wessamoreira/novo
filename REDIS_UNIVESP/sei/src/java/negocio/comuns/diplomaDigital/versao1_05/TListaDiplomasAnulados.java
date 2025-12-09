//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.04.27 às 11:07:34 AM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Lista de Diplomas com Registro anulado por Registradora
 * 
 * <p>Classe Java de TListaDiplomasAnulados complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TListaDiplomasAnulados">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="infListaDiplomasAnulados" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInfListaDiplomasAnulados"/>
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
@XmlType(name = "TListaDiplomasAnulados", propOrder = {
    "infListaDiplomasAnulados",
    "signature"
})

@XmlRootElement(name = "ListaDiplomasAnulados")
public class TListaDiplomasAnulados {

    @XmlElement(required = true)
    protected TInfListaDiplomasAnulados infListaDiplomasAnulados;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected SignatureType signature;

    /**
     * Obtém o valor da propriedade infListaDiplomasAnulados.
     * 
     * @return
     *     possible object is
     *     {@link TInfListaDiplomasAnulados }
     *     
     */
    public TInfListaDiplomasAnulados getInfListaDiplomasAnulados() {
        return infListaDiplomasAnulados;
    }

    /**
     * Define o valor da propriedade infListaDiplomasAnulados.
     * 
     * @param value
     *     allowed object is
     *     {@link TInfListaDiplomasAnulados }
     *     
     */
    public void setInfListaDiplomasAnulados(TInfListaDiplomasAnulados value) {
        this.infListaDiplomasAnulados = value;
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
