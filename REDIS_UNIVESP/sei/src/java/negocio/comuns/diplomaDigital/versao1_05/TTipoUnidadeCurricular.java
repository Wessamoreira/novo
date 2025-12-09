//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.09.29 às 03:15:12 PM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de TTipoUnidadeCurricular.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TTipoUnidadeCurricular">
 *   &lt;restriction base="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString">
 *     &lt;enumeration value="Disciplina"/>
 *     &lt;enumeration value="Módulo"/>
 *     &lt;enumeration value="Atividade"/>
 *     &lt;enumeration value="Estágio"/>
 *     &lt;enumeration value="Trabalho de Conclusão de Curso"/>
 *     &lt;enumeration value="Monografia"/>
 *     &lt;enumeration value="Artigo"/>
 *     &lt;enumeration value="Projeto"/>
 *     &lt;enumeration value="Produto"/>
 *     &lt;enumeration value="Atividade Complementar"/>
 *     &lt;enumeration value="Atividade de Extensão"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TTipoUnidadeCurricular")
@XmlEnum
public enum TTipoUnidadeCurricular {

    @XmlEnumValue("Disciplina")
    DISCIPLINA("Disciplina"),
    @XmlEnumValue("M\u00f3dulo")
    MÓDULO("M\u00f3dulo"),
    @XmlEnumValue("Atividade")
    ATIVIDADE("Atividade"),
    @XmlEnumValue("Est\u00e1gio")
    ESTÁGIO("Est\u00e1gio"),
    @XmlEnumValue("Trabalho de Conclus\u00e3o de Curso")
    TRABALHO_DE_CONCLUSÃO_DE_CURSO("Trabalho de Conclus\u00e3o de Curso"),
    @XmlEnumValue("Monografia")
    MONOGRAFIA("Monografia"),
    @XmlEnumValue("Artigo")
    ARTIGO("Artigo"),
    @XmlEnumValue("Projeto")
    PROJETO("Projeto"),
    @XmlEnumValue("Produto")
    PRODUTO("Produto"),
    @XmlEnumValue("Atividade Complementar")
    ATIVIDADE_COMPLEMENTAR("Atividade Complementar"),
    @XmlEnumValue("Atividade de Extens\u00e3o")
    ATIVIDADE_DE_EXTENSÃO("Atividade de Extens\u00e3o");
    private final String value;

    TTipoUnidadeCurricular(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TTipoUnidadeCurricular fromValue(String v) {
        for (TTipoUnidadeCurricular c: TTipoUnidadeCurricular.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
