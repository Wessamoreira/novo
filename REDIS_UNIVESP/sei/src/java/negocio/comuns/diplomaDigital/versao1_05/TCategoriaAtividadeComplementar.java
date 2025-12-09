//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.09.29 às 03:15:12 PM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Tipo que define um conjunto de atividades complementares associados a uma mesma categoria
 * 
 * <p>Classe Java de TCategoriaAtividadeComplementar complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType name="TCategoriaAtividadeComplementar">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Codigo" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TId"/>
 *         &lt;element name="Nome" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString"/>
 *         &lt;element name="LimiteCargaHorariaEmHoraRelogio" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}THoraRelogio" minOccurs="0"/>
 *         &lt;element name="Atividades" type="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TAtividadesComplementares"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TCategoriaAtividadeComplementar", propOrder = {
    "codigo",
    "nome",
    "limiteCargaHorariaEmHoraRelogio",
    "atividades"
})
public class TCategoriaAtividadeComplementar {

    @XmlElement(name = "Codigo", required = true)
    protected String codigo;
    @XmlElement(name = "Nome", required = true)
    protected String nome;
    @XmlElement(name = "LimiteCargaHorariaEmHoraRelogio")
    protected BigDecimal limiteCargaHorariaEmHoraRelogio;
    @XmlElement(name = "Atividades", required = true)
    protected TAtividadesComplementares atividades;

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
     * Obtém o valor da propriedade limiteCargaHorariaEmHoraRelogio.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLimiteCargaHorariaEmHoraRelogio() {
        return limiteCargaHorariaEmHoraRelogio;
    }

    /**
     * Define o valor da propriedade limiteCargaHorariaEmHoraRelogio.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLimiteCargaHorariaEmHoraRelogio(BigDecimal value) {
        this.limiteCargaHorariaEmHoraRelogio = value;
    }

    /**
     * Obtém o valor da propriedade atividades.
     * 
     * @return
     *     possible object is
     *     {@link TAtividadesComplementares }
     *     
     */
    public TAtividadesComplementares getAtividades() {
        return atividades;
    }

    /**
     * Define o valor da propriedade atividades.
     * 
     * @param value
     *     allowed object is
     *     {@link TAtividadesComplementares }
     *     
     */
    public void setAtividades(TAtividadesComplementares value) {
        this.atividades = value;
    }

}
