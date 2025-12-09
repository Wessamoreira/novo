
package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de TSituacaoAlunoHistorico.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TSituacaoAlunoHistorico">
 *   &lt;restriction base="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString">
 *     &lt;enumeration value="Aprovado"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TSituacaoAlunoHistorico", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd")
@XmlEnum
public enum TSituacaoAlunoHistorico {

    @XmlEnumValue("Aprovado")
    APROVADO("Aprovado");
    private final String value;

    TSituacaoAlunoHistorico(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TSituacaoAlunoHistorico fromValue(String v) {
        for (TSituacaoAlunoHistorico c: TSituacaoAlunoHistorico.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
