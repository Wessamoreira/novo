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
 * 
 * 				Determina as equivalencias de UnidadeCurricular. 
 * 				Para ser equivalente todos os CodigosUnidadeEquivalente de pelo menos
 * 				uma UnidadesCurricularesEquivalente devem estar presentes no histórico.
 * 			
 * 
 * <p>Classe Java de TEquivalenciaUnidadesCurriculares complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TEquivalenciaUnidadesCurriculares">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="UnidadesCurricularesEquivalente">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence maxOccurs="unbounded">
 *                   &lt;element name="CodigoUnidadeEquivalente" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodigoUnidadeCurricular"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TEquivalenciaUnidadesCurriculares", propOrder = {
    "unidadesCurricularesEquivalente"
})
public class TEquivalenciaUnidadesCurriculares {

    @XmlElement(name = "UnidadesCurricularesEquivalente", required = true)
    protected List<TEquivalenciaUnidadesCurriculares.UnidadesCurricularesEquivalente> unidadesCurricularesEquivalente;

    /**
     * Gets the value of the unidadesCurricularesEquivalente property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the unidadesCurricularesEquivalente property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUnidadesCurricularesEquivalente().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TEquivalenciaUnidadesCurriculares.UnidadesCurricularesEquivalente }
     * 
     * 
     */
    public List<TEquivalenciaUnidadesCurriculares.UnidadesCurricularesEquivalente> getUnidadesCurricularesEquivalente() {
        if (unidadesCurricularesEquivalente == null) {
            unidadesCurricularesEquivalente = new ArrayList<TEquivalenciaUnidadesCurriculares.UnidadesCurricularesEquivalente>();
        }
        return this.unidadesCurricularesEquivalente;
    }


    /**
     * <p>Classe Java de anonymous complex type.
     * 
     * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence maxOccurs="unbounded">
     *         &lt;element name="CodigoUnidadeEquivalente" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodigoUnidadeCurricular"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "codigoUnidadeEquivalente"
    })
    public static class UnidadesCurricularesEquivalente {

        @XmlElement(name = "CodigoUnidadeEquivalente", required = true)
        protected List<String> codigoUnidadeEquivalente;

        /**
         * Gets the value of the codigoUnidadeEquivalente property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the codigoUnidadeEquivalente property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCodigoUnidadeEquivalente().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getCodigoUnidadeEquivalente() {
            if (codigoUnidadeEquivalente == null) {
                codigoUnidadeEquivalente = new ArrayList<String>();
            }
            return this.codigoUnidadeEquivalente;
        }

    }

}
