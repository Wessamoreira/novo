
package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Tipo situação do aluno no ENADE. Deve-se utilizar a Situação excplicitada na enumeração a menos que não esteja disposível. Neste caso deve-se usar a opção "Outros
 * 
 * <p>Classe Java de TSituacaoAlunoEnadeHistorico complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TSituacaoAlunoEnadeHistorico">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Situacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEnumSituacaoAlunoEnadeHistorico"/>
 *         &lt;element name="OutraSituacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TSituacaoAlunoEnadeHistorico", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd", propOrder = {
    "situacao",
    "outraSituacao"
})
public class TSituacaoAlunoEnadeHistorico {

    @XmlElement(name = "Situacao", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd")
    @XmlSchemaType(name = "string")
    protected TEnumSituacaoAlunoEnadeHistorico situacao;
    @XmlElement(name = "OutraSituacao", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd")
    protected String outraSituacao;

    /**
     * Obtém o valor da propriedade situacao.
     * 
     * @return
     *     possible object is
     *     {@link TEnumSituacaoAlunoEnadeHistorico }
     *     
     */
    public TEnumSituacaoAlunoEnadeHistorico getSituacao() {
        return situacao;
    }

    /**
     * Define o valor da propriedade situacao.
     * 
     * @param value
     *     allowed object is
     *     {@link TEnumSituacaoAlunoEnadeHistorico }
     *     
     */
    public void setSituacao(TEnumSituacaoAlunoEnadeHistorico value) {
        this.situacao = value;
    }

    /**
     * Obtém o valor da propriedade outraSituacao.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutraSituacao() {
        return outraSituacao;
    }

    /**
     * Define o valor da propriedade outraSituacao.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutraSituacao(String value) {
        this.outraSituacao = value;
    }

}
