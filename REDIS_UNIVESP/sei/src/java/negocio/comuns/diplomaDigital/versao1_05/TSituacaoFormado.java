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
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java de TSituacaoFormado complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TSituacaoFormado">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DataConclusaoCurso" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="DataColacaoGrau" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *         &lt;element name="DataExpedicaoDiploma" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TData"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSituacaoFormado", propOrder = {
    "dataConclusaoCurso",
    "dataColacaoGrau",
    "dataExpedicaoDiploma"
})
public class TSituacaoFormado {

    @XmlElement(name = "DataConclusaoCurso", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataConclusaoCurso;
    @XmlElement(name = "DataColacaoGrau", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataColacaoGrau;
    @XmlElement(name = "DataExpedicaoDiploma", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataExpedicaoDiploma;

    /**
     * Obtém o valor da propriedade dataConclusaoCurso.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataConclusaoCurso() {
        return dataConclusaoCurso;
    }

    /**
     * Define o valor da propriedade dataConclusaoCurso.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataConclusaoCurso(XMLGregorianCalendar value) {
        this.dataConclusaoCurso = value;
    }

    /**
     * Obtém o valor da propriedade dataColacaoGrau.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataColacaoGrau() {
        return dataColacaoGrau;
    }

    /**
     * Define o valor da propriedade dataColacaoGrau.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataColacaoGrau(XMLGregorianCalendar value) {
        this.dataColacaoGrau = value;
    }

    /**
     * Obtém o valor da propriedade dataExpedicaoDiploma.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataExpedicaoDiploma() {
        return dataExpedicaoDiploma;
    }

    /**
     * Define o valor da propriedade dataExpedicaoDiploma.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataExpedicaoDiploma(XMLGregorianCalendar value) {
        this.dataExpedicaoDiploma = value;
    }

}
