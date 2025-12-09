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
 * Tipo que define uma lista de atividades complementar
 * 
 * <p>Classe Java de TAtividadesComplementares complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TAtividadesComplementares">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Atividade" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAtividadeComplementar" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TAtividadesComplementares", propOrder = {
    "atividade"
})
public class TAtividadesComplementares {

    @XmlElement(name = "Atividade", required = true)
    protected List<TAtividadeComplementar> atividade;

    /**
     * Gets the value of the atividade property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the atividade property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAtividade().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TAtividadeComplementar }
     * 
     * 
     */
    public List<TAtividadeComplementar> getAtividade() {
        if (atividade == null) {
            atividade = new ArrayList<TAtividadeComplementar>();
        }
        return this.atividade;
    }

}
