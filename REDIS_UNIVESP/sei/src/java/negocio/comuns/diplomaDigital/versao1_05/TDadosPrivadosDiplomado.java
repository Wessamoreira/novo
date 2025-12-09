//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.20 às 10:57:34 AM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Dados do Diplomado
 * 
 * <p>Classe Java de TDadosPrivadosDiplomado complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDadosPrivadosDiplomado">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Filiacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TFiliacao"/>
 *         &lt;element name="HistoricoEscolar" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THistoricoEscolar"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDadosPrivadosDiplomado", propOrder = {
    "filiacao",
    "historicoEscolar"
})
public class TDadosPrivadosDiplomado {

    @XmlElement(name = "Filiacao", required = true)
    protected TFiliacao filiacao;
    @XmlElement(name = "HistoricoEscolar", required = true)
    protected THistoricoEscolar historicoEscolar;

    /**
     * Obtém o valor da propriedade filiacao.
     * 
     * @return
     *     possible object is
     *     {@link TFiliacao }
     *     
     */
    public TFiliacao getFiliacao() {
        return filiacao;
    }

    /**
     * Define o valor da propriedade filiacao.
     * 
     * @param value
     *     allowed object is
     *     {@link TFiliacao }
     *     
     */
    public void setFiliacao(TFiliacao value) {
        this.filiacao = value;
    }

    /**
     * Obtém o valor da propriedade historicoEscolar.
     * 
     * @return
     *     possible object is
     *     {@link THistoricoEscolar }
     *     
     */
    public THistoricoEscolar getHistoricoEscolar() {
        return historicoEscolar;
    }

    /**
     * Define o valor da propriedade historicoEscolar.
     * 
     * @param value
     *     allowed object is
     *     {@link THistoricoEscolar }
     *     
     */
    public void setHistoricoEscolar(THistoricoEscolar value) {
        this.historicoEscolar = value;
    }

}
