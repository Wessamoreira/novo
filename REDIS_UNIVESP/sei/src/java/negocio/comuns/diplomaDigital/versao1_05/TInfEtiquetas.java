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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Tipo que define informações sobre as etiquetas usadas neste currículo para classificação das unidades curriculares
 * 
 * <p>Classe Java de TInfEtiquetas complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TInfEtiquetas">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Etiqueta" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDadoEtiqueta" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TInfEtiquetas", propOrder = {
    "etiqueta"
})
public class TInfEtiquetas {

    @XmlElement(name = "Etiqueta", required = true)
    protected List<TDadoEtiqueta> etiqueta;

    /**
     * Gets the value of the etiqueta property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the etiqueta property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEtiqueta().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TDadoEtiqueta }
     * 
     * 
     */
    public List<TDadoEtiqueta> getEtiqueta() {
        if (etiqueta == null) {
            etiqueta = new ArrayList<TDadoEtiqueta>();
        }
        return this.etiqueta;
    }

}
