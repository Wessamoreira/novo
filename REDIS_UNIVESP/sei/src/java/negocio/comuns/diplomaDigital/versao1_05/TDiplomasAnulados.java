//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.04.27 às 11:07:34 AM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Lista de Diplomas Anulados com Data de Anulação e Motivo 
 * 
 * <p>Classe Java de TDiplomasAnulados complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDiplomasAnulados">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DiplomaAnulado" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDiplomaAnulado" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDiplomasAnulados", propOrder = {
    "diplomaAnulado"
})
public class TDiplomasAnulados {

    @XmlElement(name = "DiplomaAnulado")
    protected List<TDiplomaAnulado> diplomaAnulado;

    /**
     * Gets the value of the diplomaAnulado property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the diplomaAnulado property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDiplomaAnulado().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TDiplomaAnulado }
     * 
     * 
     */
    public List<TDiplomaAnulado> getDiplomaAnulado() {
        if (diplomaAnulado == null) {
            diplomaAnulado = new ArrayList<TDiplomaAnulado>();
        }
        return this.diplomaAnulado;
    }

}
