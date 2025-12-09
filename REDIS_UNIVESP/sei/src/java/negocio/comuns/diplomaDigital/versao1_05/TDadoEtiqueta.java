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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Tipo que define informações sobre uma das etiquetas usadas neste currículo para classificação das unidades curriculares
 * 
 * <p>Classe Java de TDadoEtiqueta complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDadoEtiqueta">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Codigo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;element name="Nome" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;element name="AplicadoAutomaticamenteUnidadesNaoPertencentesAoCurriculo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TSimNao" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDadoEtiqueta", propOrder = {
    "codigo",
    "nome",
    "aplicadoAutomaticamenteUnidadesNaoPertencentesAoCurriculo"
})
public class TDadoEtiqueta {

    @XmlElement(name = "Codigo", required = true)
    protected String codigo;
    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "AplicadoAutomaticamenteUnidadesNaoPertencentesAoCurriculo")
    @XmlSchemaType(name = "string")
    protected TSimNao aplicadoAutomaticamenteUnidadesNaoPertencentesAoCurriculo;

    /**
     * Obtém o valor da propriedade codigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Define o valor da propriedade codigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigo(String value) {
        this.codigo = value;
    }

    /**
     * Obtém o valor da propriedade nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Define o valor da propriedade nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Obtém o valor da propriedade aplicadoAutomaticamenteUnidadesNaoPertencentesAoCurriculo.
     * 
     * @return
     *     possible object is
     *     {@link TSimNao }
     *     
     */
    public TSimNao getAplicadoAutomaticamenteUnidadesNaoPertencentesAoCurriculo() {
        return aplicadoAutomaticamenteUnidadesNaoPertencentesAoCurriculo;
    }

    /**
     * Define o valor da propriedade aplicadoAutomaticamenteUnidadesNaoPertencentesAoCurriculo.
     * 
     * @param value
     *     allowed object is
     *     {@link TSimNao }
     *     
     */
    public void setAplicadoAutomaticamenteUnidadesNaoPertencentesAoCurriculo(TSimNao value) {
        this.aplicadoAutomaticamenteUnidadesNaoPertencentesAoCurriculo = value;
    }

}
