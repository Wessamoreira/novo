//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.04.27 às 09:44:38 AM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Informações sobre condição do estudante e edição do Enade
 * 
 * <p>Classe Java de TInformacoesEnade complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TInformacoesEnade">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Condicao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEnumCondicaoEnade"/>
 *         &lt;element name="Edicao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAno"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TInformacoesEnade", propOrder = {
    "condicao",
    "edicao",
    "motivo"
})

public class TInformacoesEnade {

    @XmlElement(name = "Condicao", required = true)
    @XmlSchemaType(name = "string")
    protected TEnumCondicaoEnade condicao;
    @XmlElement(name = "Edicao", required = true)
    protected String edicao;
    @XmlElement(name = "Motivo")
    @XmlSchemaType(name = "string")
    protected String motivo;

    /**
     * Obtém o valor da propriedade condicao.
     * 
     * @return
     *     possible object is
     *     {@link TEnumCondicaoEnade }
     *     
     */
    public TEnumCondicaoEnade getCondicao() {
        return condicao;
    }

    /**
     * Define o valor da propriedade condicao.
     * 
     * @param value
     *     allowed object is
     *     {@link TEnumCondicaoEnade }
     *     
     */
    public void setCondicao(TEnumCondicaoEnade value) {
        this.condicao = value;
    }

    /**
     * Obtém o valor da propriedade edicao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEdicao() {
        return edicao;
    }

    /**
     * Define o valor da propriedade edicao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEdicao(String value) {
        this.edicao = value;
    }
    
    public String getMotivo() {
    	return motivo;
	}
    
    public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
			

}
