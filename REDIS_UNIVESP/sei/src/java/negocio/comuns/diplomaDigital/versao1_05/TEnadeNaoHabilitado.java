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
 * Informações sobre estudantes não habilitados para o ENADE
 * 
 * <p>Classe Java de TEnadeNaoHabilitado complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TEnadeNaoHabilitado">
 *   &lt;complexContent>
 *     &lt;extension base="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInformacoesEnade">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="Motivo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico"/>
 *           &lt;element name="OutroMotivo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TEnadeNaoHabilitado", propOrder = {
    "condicao",
    "edicao",
    "motivo",
    "outroMotivo"
})
public class TEnadeNaoHabilitado {

    @XmlElement(name = "Condicao")
    @XmlSchemaType(name = "string")
    protected TEnumCondicaoEnade condicao;
    @XmlElement(name = "Edicao")
    @XmlSchemaType(name = "string")
    protected String edicao;
    @XmlElement(name = "Motivo")
    @XmlSchemaType(name = "string")
    protected TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico motivo;
    @XmlElement(name = "OutroMotivo")
    @XmlSchemaType(name = "string")
    protected String outroMotivo;

    /**
     * Obtém o valor da propriedade motivo.
     * 
     * @return
     *     possible object is
     *     {@link TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico }
     *     
     */
    public TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico getMotivo() {
        return motivo;
    }

    /**
     * Define o valor da propriedade motivo.
     * 
     * @param value
     *     allowed object is
     *     {@link TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico }
     *     
     */
    public void setMotivo(TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico value) {
        this.motivo = value;
    }

    /**
     * Obtém o valor da propriedade outroMotivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutroMotivo() {
        return outroMotivo;
    }

    /**
     * Define o valor da propriedade outroMotivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutroMotivo(String value) {
        this.outroMotivo = value;
    }
    
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

}
