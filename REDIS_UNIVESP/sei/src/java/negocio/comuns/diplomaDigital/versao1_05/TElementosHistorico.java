//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.25 às 05:38:08 PM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * Entradas do histórico escolar
 * 
 * <p>Classe Java de TElementosHistorico complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TElementosHistorico">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;choice>
 *           &lt;element name="Disciplina" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEntradaHistoricoDisciplina"/>
 *           &lt;element name="AtividadeComplementar" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEntradaHistoricoAtividadeComplementar"/>
 *           &lt;element name="Estagio" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEntradaHistoricoEstagio"/>
 *           &lt;element name="SituacaoDiscente" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TEntradaHistoricoSituacaoDiscentePeriodoLetivo"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TElementosHistorico", propOrder = {
    "disciplinaOrAtividadeComplementarOrEstagio"
})
public class TElementosHistorico {

    @XmlElements({
        @XmlElement(name = "Disciplina", type = TEntradaHistoricoDisciplina.class),
        @XmlElement(name = "AtividadeComplementar", type = TEntradaHistoricoAtividadeComplementar.class),
        @XmlElement(name = "Estagio", type = TEntradaHistoricoEstagio.class),
        @XmlElement(name = "SituacaoDiscente", type = TEntradaHistoricoSituacaoDiscentePeriodoLetivo.class)
    })
    protected List<Object> disciplinaOrAtividadeComplementarOrEstagio;

    /**
     * Gets the value of the disciplinaOrAtividadeComplementarOrEstagio property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the disciplinaOrAtividadeComplementarOrEstagio property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDisciplinaOrAtividadeComplementarOrEstagio().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TEntradaHistoricoDisciplina }
     * {@link TEntradaHistoricoAtividadeComplementar }
     * {@link TEntradaHistoricoEstagio }
     * {@link TEntradaHistoricoSituacaoDiscentePeriodoLetivo }
     * 
     * 
     */
    public List<Object> getDisciplinaOrAtividadeComplementarOrEstagio() {
        if (disciplinaOrAtividadeComplementarOrEstagio == null) {
            disciplinaOrAtividadeComplementarOrEstagio = new ArrayList<Object>();
        }
        return this.disciplinaOrAtividadeComplementarOrEstagio;
    }

}
