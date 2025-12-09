//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.04.27 às 10:26:01 AM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Lista de Diplomas Registrados 
 * 
 * <p>Classe Java de TDiplomasFiscalizadosRegistradora complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDiplomasFiscalizadosRegistradora">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DiplomaFiscalizado" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TDiplomaFiscalizadoRegistradora" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDiplomasFiscalizadosRegistradora", propOrder = {
    "diplomaFiscalizado"
})
public class TDiplomasFiscalizadosRegistradora {

    @XmlElement(name = "DiplomaFiscalizado", required = true)
    protected List<TDiplomaFiscalizadoRegistradora> diplomaFiscalizado;

    /**
     * Gets the value of the diplomaFiscalizado property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the diplomaFiscalizado property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDiplomaFiscalizado().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TDiplomaFiscalizadoRegistradora }
     * 
     * 
     */
    public List<TDiplomaFiscalizadoRegistradora> getDiplomaFiscalizado() {
        if (diplomaFiscalizado == null) {
            diplomaFiscalizado = new ArrayList<TDiplomaFiscalizadoRegistradora>();
        }
        return this.diplomaFiscalizado;
    }

}
