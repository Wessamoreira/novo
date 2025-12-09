//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.09.29 às 03:15:12 PM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Tipo que define os possíveis operadores usados para definição de uma expressao
 * 			
 * 
 * <p>Classe Java de TExpressao complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TExpressao">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Soma" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TCodigos"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TExpressao", propOrder = {
    "soma"
})
public class TExpressao {

    @XmlElement(name = "Soma")
    protected TCodigos soma;

    /**
     * Obtém o valor da propriedade soma.
     * 
     * @return
     *     possible object is
     *     {@link TCodigos }
     *     
     */
    public TCodigos getSoma() {
        return soma;
    }

    /**
     * Define o valor da propriedade soma.
     * 
     * @param value
     *     allowed object is
     *     {@link TCodigos }
     *     
     */
    public void setSoma(TCodigos value) {
        this.soma = value;
    }

}
