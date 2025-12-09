//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.25 às 05:38:08 PM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * Informações sobre a participação no ENADE
 * 
 * <p>Classe Java de TEnade complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TEnade">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;choice>
 *           &lt;element name="Habilitado" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInformacoesEnade"/>
 *           &lt;element name="NaoHabilitado" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEnadeNaoHabilitado"/>
 *           &lt;element name="Irregular" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInformacoesEnade"/>
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
@XmlType(name = "TEnade", propOrder = {
    "habilitadoOrNaoHabilitadoOrIrregular"
})
public class TEnade {

    @XmlElementRefs({
        @XmlElementRef(name = "Habilitado", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "Irregular", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "NaoHabilitado", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<? extends Object>> habilitadoOrNaoHabilitadoOrIrregular;

    /**
     * Gets the value of the habilitadoOrNaoHabilitadoOrIrregular property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the habilitadoOrNaoHabilitadoOrIrregular property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHabilitadoOrNaoHabilitadoOrIrregular().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link TInformacoesEnade }{@code >}
     * {@link JAXBElement }{@code <}{@link TInformacoesEnade }{@code >}
     * {@link JAXBElement }{@code <}{@link TEnadeNaoHabilitado }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends Object>> getHabilitadoOrNaoHabilitadoOrIrregular() {
        if (habilitadoOrNaoHabilitadoOrIrregular == null) {
            habilitadoOrNaoHabilitadoOrIrregular = new ArrayList<JAXBElement<? extends Object>>();
        }
        return this.habilitadoOrNaoHabilitadoOrIrregular;
    }

}
