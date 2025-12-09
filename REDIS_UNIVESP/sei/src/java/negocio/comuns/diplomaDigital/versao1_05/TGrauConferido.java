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
 * <p>Classe Java de TGrauConferido.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TGrauConferido">
 *   &lt;restriction base="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString">
 *     &lt;enumeration value="Tecnólogo"/>
 *     &lt;enumeration value="Bacharelado"/>
 *     &lt;enumeration value="Licenciatura"/>
 *     &lt;enumeration value="Curso sequencial"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TGrauConferido")
@XmlEnum
public enum TGrauConferido {

    @XmlEnumValue("Tecn\u00f3logo")
    TECNÓLOGO("Tecn\u00f3logo"),
    @XmlEnumValue("Bacharelado")
    BACHARELADO("Bacharelado"),
    @XmlEnumValue("Licenciatura")
    LICENCIATURA("Licenciatura"),
    @XmlEnumValue("Curso sequencial")
    CURSO_SEQUENCIAL("Curso sequencial");
    private final String value;

    TGrauConferido(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TGrauConferido fromValue(String v) {
        for (TGrauConferido c: TGrauConferido.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
