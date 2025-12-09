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
 * <p>Classe Java de TTipoAtoComAtoProprio.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TTipoAtoComAtoProprio">
 *   &lt;restriction base="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString">
 *     &lt;enumeration value="Parecer"/>
 *     &lt;enumeration value="Resolução"/>
 *     &lt;enumeration value="Decreto"/>
 *     &lt;enumeration value="Portaria"/>
 *     &lt;enumeration value="Deliberação"/>
 *     &lt;enumeration value="Lei Federal"/>
 *     &lt;enumeration value="Lei Estadual"/>
 *     &lt;enumeration value="Lei Municipal"/>
 *     &lt;enumeration value="Ato Próprio"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TTipoAtoComAtoProprio")
@XmlEnum
public enum TTipoAtoComAtoProprio {

    @XmlEnumValue("Parecer")
    PARECER("Parecer"),
    @XmlEnumValue("Resolu\u00e7\u00e3o")
    RESOLUÇÃO("Resolu\u00e7\u00e3o"),
    @XmlEnumValue("Decreto")
    DECRETO("Decreto"),
    @XmlEnumValue("Portaria")
    PORTARIA("Portaria"),
    @XmlEnumValue("Delibera\u00e7\u00e3o")
    DELIBERAÇÃO("Delibera\u00e7\u00e3o"),
    @XmlEnumValue("Lei Federal")
    LEI_FEDERAL("Lei Federal"),
    @XmlEnumValue("Lei Estadual")
    LEI_ESTADUAL("Lei Estadual"),
    @XmlEnumValue("Lei Municipal")
    LEI_MUNICIPAL("Lei Municipal"),
    @XmlEnumValue("Ato Pr\u00f3prio")
    ATO_PRÓPRIO("Ato Pr\u00f3prio");
    private final String value;

    TTipoAtoComAtoProprio(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TTipoAtoComAtoProprio fromValue(String v) {
        for (TTipoAtoComAtoProprio c: TTipoAtoComAtoProprio.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
