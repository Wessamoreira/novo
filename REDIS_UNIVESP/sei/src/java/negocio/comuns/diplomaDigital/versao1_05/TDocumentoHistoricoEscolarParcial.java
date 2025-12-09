//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.10.04 às 08:40:31 AM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Documento de Histórico Escolar Digital
 * 
 * <p>Classe Java de TDocumentoHistoricoEscolarDigital complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDocumentoHistoricoEscolarDigital">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="infHistoricoEscolar" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInfHistoricoEscolar"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDocumentoHistoricoEscolarParcial", propOrder = {
    "infHistoricoEscolar",
    "signature"
})
@XmlRootElement(name = "DocumentoHistoricoEscolarParcial")
public class TDocumentoHistoricoEscolarParcial extends TDocumentoHistoricoEscolarDigital {

    @XmlElement(required = true)
    protected TInfHistoricoEscolar infHistoricoEscolar;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected List<SignatureType> signature;

    /**
     * Obtém o valor da propriedade infHistoricoEscolar.
     * 
     * @return
     *     possible object is
     *     {@link TInfHistoricoEscolar }
     *     
     */
    public TInfHistoricoEscolar getInfHistoricoEscolar() {
        return infHistoricoEscolar;
    }

    /**
     * Define o valor da propriedade infHistoricoEscolar.
     * 
     * @param value
     *     allowed object is
     *     {@link TInfHistoricoEscolar }
     *     
     */
    public void setInfHistoricoEscolar(TInfHistoricoEscolar value) {
        this.infHistoricoEscolar = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signature property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignature().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SignatureType }
     * 
     * 
     */
    public List<SignatureType> getSignature() {
        if (signature == null) {
            signature = new ArrayList<SignatureType>();
        }
        return this.signature;
    }

}
