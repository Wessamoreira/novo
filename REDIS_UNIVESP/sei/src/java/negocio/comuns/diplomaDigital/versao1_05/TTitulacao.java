//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.25 às 05:38:08 PM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de TTitulacao.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TTitulacao">
 *   &lt;restriction base="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString">
 *     &lt;whiteSpace value="preserve"/>
 *     &lt;enumeration value="Tecnólogo"/>
 *     &lt;enumeration value="Graduação"/>
 *     &lt;enumeration value="Especialização"/>
 *     &lt;enumeration value="Mestrado"/>
 *     &lt;enumeration value="Doutorado"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TTitulacao")
@XmlEnum
public enum TTitulacao {

    @XmlEnumValue("Tecn\u00f3logo")
    TECNÓLOGO("Tecn\u00f3logo"),
    @XmlEnumValue("Gradua\u00e7\u00e3o")
    GRADUAÇÃO("Gradua\u00e7\u00e3o"),
    @XmlEnumValue("Especializa\u00e7\u00e3o")
    ESPECIALIZAÇÃO("Especializa\u00e7\u00e3o"),
    @XmlEnumValue("Mestrado")
    MESTRADO("Mestrado"),
    @XmlEnumValue("Doutorado")
    DOUTORADO("Doutorado");
    private final String value;

    TTitulacao(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TTitulacao fromValue(String v) {
        for (TTitulacao c: TTitulacao.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
