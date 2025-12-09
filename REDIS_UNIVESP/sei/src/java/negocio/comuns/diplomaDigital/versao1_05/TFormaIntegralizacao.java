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
 * <p>Classe Java de TFormaIntegralizacao.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TFormaIntegralizacao">
 *   &lt;restriction base="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString">
 *     &lt;enumeration value="Cursado"/>
 *     &lt;enumeration value="Validado"/>
 *     &lt;enumeration value="Aproveitado"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TFormaIntegralizacao")
@XmlEnum
public enum TFormaIntegralizacao {

    @XmlEnumValue("Cursado")
    CURSADO("Cursado"),
    @XmlEnumValue("Validado")
    VALIDADO("Validado"),
    @XmlEnumValue("Aproveitado")
    APROVEITADO("Aproveitado");
    private final String value;

    TFormaIntegralizacao(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TFormaIntegralizacao fromValue(String v) {
        for (TFormaIntegralizacao c: TFormaIntegralizacao.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
