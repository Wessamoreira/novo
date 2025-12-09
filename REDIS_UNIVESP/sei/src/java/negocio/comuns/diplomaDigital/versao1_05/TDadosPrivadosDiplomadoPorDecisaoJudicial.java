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
 * Dados do Diplomado para emissão devido a decisão judicial.
 * 
 * <p>Classe Java de TDadosPrivadosDiplomadoPorDecisaoJudicial complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TDadosPrivadosDiplomadoPorDecisaoJudicial">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="Filiacao" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TFiliacao"/>
 *           &lt;element name="Filiacao_Indisponivel" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TVazio"/>
 *         &lt;/choice>
 *         &lt;choice>
 *           &lt;element name="HistoricoEscolar" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THistoricoEscolarSegundaVia"/>
 *           &lt;element name="HistoricoEscolar_Indisponivel" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TVazio"/>
 *         &lt;/choice>
 *         &lt;element name="InformacoesProcessoJudicial" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TInformacoesProcessoJudicial"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TDadosPrivadosDiplomadoPorDecisaoJudicial", propOrder = {
    "filiacao",
    "filiacaoIndisponivel",
    "historicoEscolar",
    "historicoEscolarIndisponivel",
    "informacoesProcessoJudicial"
})
public class TDadosPrivadosDiplomadoPorDecisaoJudicial {

    @XmlElement(name = "Filiacao")
    protected TFiliacao filiacao;
    @XmlElement(name = "Filiacao_Indisponivel")
    protected TVazio filiacaoIndisponivel;
    @XmlElement(name = "HistoricoEscolar")
    protected THistoricoEscolarSegundaVia historicoEscolar;
    @XmlElement(name = "HistoricoEscolar_Indisponivel")
    protected TVazio historicoEscolarIndisponivel;
    @XmlElement(name = "InformacoesProcessoJudicial", required = true)
    protected TInformacoesProcessoJudicial informacoesProcessoJudicial;

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
     * Obtém o valor da propriedade filiacaoIndisponivel.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getFiliacaoIndisponivel() {
        return filiacaoIndisponivel;
    }

    /**
     * Define o valor da propriedade filiacaoIndisponivel.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setFiliacaoIndisponivel(TVazio value) {
        this.filiacaoIndisponivel = value;
    }

    /**
     * Obtém o valor da propriedade historicoEscolar.
     * 
     * @return
     *     possible object is
     *     {@link THistoricoEscolarSegundaVia }
     *     
     */
    public THistoricoEscolarSegundaVia getHistoricoEscolar() {
        return historicoEscolar;
    }

    /**
     * Define o valor da propriedade historicoEscolar.
     * 
     * @param value
     *     allowed object is
     *     {@link THistoricoEscolarSegundaVia }
     *     
     */
    public void setHistoricoEscolar(THistoricoEscolarSegundaVia value) {
        this.historicoEscolar = value;
    }

    /**
     * Obtém o valor da propriedade historicoEscolarIndisponivel.
     * 
     * @return
     *     possible object is
     *     {@link TVazio }
     *     
     */
    public TVazio getHistoricoEscolarIndisponivel() {
        return historicoEscolarIndisponivel;
    }

    /**
     * Define o valor da propriedade historicoEscolarIndisponivel.
     * 
     * @param value
     *     allowed object is
     *     {@link TVazio }
     *     
     */
    public void setHistoricoEscolarIndisponivel(TVazio value) {
        this.historicoEscolarIndisponivel = value;
    }

    /**
     * Obtém o valor da propriedade informacoesProcessoJudicial.
     * 
     * @return
     *     possible object is
     *     {@link TInformacoesProcessoJudicial }
     *     
     */
    public TInformacoesProcessoJudicial getInformacoesProcessoJudicial() {
        return informacoesProcessoJudicial;
    }

    /**
     * Define o valor da propriedade informacoesProcessoJudicial.
     * 
     * @param value
     *     allowed object is
     *     {@link TInformacoesProcessoJudicial }
     *     
     */
    public void setInformacoesProcessoJudicial(TInformacoesProcessoJudicial value) {
        this.informacoesProcessoJudicial = value;
    }

}
