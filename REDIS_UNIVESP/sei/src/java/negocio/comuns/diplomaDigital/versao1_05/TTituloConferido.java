//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.25 às 05:38:08 PM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de TTituloConferido complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TTituloConferido">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Titulo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TTitulo"/>
 *         &lt;element name="OutroTitulo">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString">
 *               &lt;whiteSpace value="collapse"/>
 *               &lt;maxLength value="255"/>
 *               &lt;minLength value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TTituloConferido", propOrder = {
    "titulo",
    "outroTitulo"
})
public class TTituloConferido {

    @XmlElement(name = "Titulo")
    @XmlSchemaType(name = "string")
    protected TTitulo titulo;
    @XmlElement(name = "OutroTitulo")
    protected String outroTitulo;

    /**
     * Obtém o valor da propriedade titulo.
     * 
     * @return
     *     possible object is
     *     {@link TTitulo }
     *     
     */
    public TTitulo getTitulo() {
        return titulo;
    }

    /**
     * Define o valor da propriedade titulo.
     * 
     * @param value
     *     allowed object is
     *     {@link TTitulo }
     *     
     */
    public void setTitulo(TTitulo value) {
        this.titulo = value;
    }

    /**
     * Obtém o valor da propriedade outroTitulo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutroTitulo() {
        return outroTitulo;
    }

    /**
     * Define o valor da propriedade outroTitulo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutroTitulo(String value) {
        this.outroTitulo = value;
    }

}
