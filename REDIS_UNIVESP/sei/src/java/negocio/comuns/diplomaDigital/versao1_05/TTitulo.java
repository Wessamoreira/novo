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
 * <p>Classe Java de TTitulo.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TTitulo">
 *   &lt;restriction base="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString">
 *     &lt;enumeration value="Licenciado"/>
 *     &lt;enumeration value="Tecnólogo"/>
 *     &lt;enumeration value="Bacharel"/>
 *     &lt;enumeration value="Médico"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TTitulo")
@XmlEnum
public enum TTitulo {

    @XmlEnumValue("Licenciado")
    LICENCIADO("Licenciado"),
    @XmlEnumValue("Tecn\u00f3logo")
    TECNÓLOGO("Tecn\u00f3logo"),
    @XmlEnumValue("Bacharel")
    BACHAREL("Bacharel"),
    @XmlEnumValue("M\u00e9dico")
    MÉDICO("M\u00e9dico");
    private final String value;

    TTitulo(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TTitulo fromValue(String v) {
        for (TTitulo c: TTitulo.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
