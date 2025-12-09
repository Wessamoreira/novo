//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.09.29 às 03:15:12 PM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Documento descritivo de um Currículo Escolar de um Projeto Pedagógico de Curso (PPC).
 * 
 * <p>Classe Java de TCurriculoEscolar complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TCurriculoEscolar">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="infCurriculoEscolar" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInfCurriculoEscolar"/>
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
@XmlType(name = "TCurriculoEscolar", propOrder = {
    "infCurriculoEscolar",
    "signature"
})
@XmlRootElement(name = "CurriculoEscolar")
public class TCurriculoEscolar {

    @XmlElement(required = true)
    protected TInfCurriculoEscolar infCurriculoEscolar;
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    protected String id;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected List<SignatureType> signature;

    /**
     * Obtém o valor da propriedade infCurriculoEscolar.
     * 
     * @return
     *     possible object is
     *     {@link TInfCurriculoEscolar }
     *     
     */
    public TInfCurriculoEscolar getInfCurriculoEscolar() {
        return infCurriculoEscolar;
    }

    /**
     * Define o valor da propriedade infCurriculoEscolar.
     * 
     * @param value
     *     allowed object is
     *     {@link TInfCurriculoEscolar }
     *     
     */
    public void setInfCurriculoEscolar(TInfCurriculoEscolar value) {
        this.infCurriculoEscolar = value;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
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
