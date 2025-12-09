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
 * <p>Classe Java de TAmb.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TAmb">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;whiteSpace value="preserve"/>
 *     &lt;enumeration value="Produção"/>
 *     &lt;enumeration value="Homologação"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TAmb")
@XmlEnum
public enum TAmb {

    @XmlEnumValue("Produ\u00e7\u00e3o")
    PRODUÇÃO("Produ\u00e7\u00e3o"),
    @XmlEnumValue("Homologa\u00e7\u00e3o")
    HOMOLOGAÇÃO("Homologa\u00e7\u00e3o");
    private final String value;

    TAmb(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TAmb fromValue(String v) {
        for (TAmb c: TAmb.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
