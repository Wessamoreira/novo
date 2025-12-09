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
 * <p>Classe Java de TEnumCondicaoEnade.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TEnumCondicaoEnade">
 *   &lt;restriction base="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString">
 *     &lt;enumeration value="Ingressante"/>
 *     &lt;enumeration value="Concluinte"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TEnumCondicaoEnade")
@XmlEnum
public enum TEnumCondicaoEnade {

	@XmlEnumValue("Ingressante")
    NENHUM("Ingressante"),
	@XmlEnumValue("Ingressante")
    INGRESSANTE("Ingressante"),
    @XmlEnumValue("Concluinte")
    CONCLUINTE("Concluinte");
    private final String value;

    TEnumCondicaoEnade(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TEnumCondicaoEnade fromValue(String v) {
        for (TEnumCondicaoEnade c: TEnumCondicaoEnade.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
