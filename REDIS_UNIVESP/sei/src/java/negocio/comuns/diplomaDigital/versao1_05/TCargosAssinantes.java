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
 * <p>Classe Java de TCargosAssinantes.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TCargosAssinantes">
 *   &lt;restriction base="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString">
 *     &lt;enumeration value="Reitor"/>
 *     &lt;enumeration value="Reitor em Exercício"/>
 *     &lt;enumeration value="Responsável pelo registro"/>
 *     &lt;enumeration value="Coordenador de Curso"/>
 *     &lt;enumeration value="Subcoordenador de Curso"/>
 *     &lt;enumeration value="Coordenador de Curso em exercício"/>
 *     &lt;enumeration value="Chefe da área de registro de diplomas"/>
 *     &lt;enumeration value="Chefe em exercício da área de registro de diplomas"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TCargosAssinantes")
@XmlEnum
public enum TCargosAssinantes {

    @XmlEnumValue("Reitor")
    REITOR("Reitor"),
    @XmlEnumValue("Reitor em Exerc\u00edcio")
    REITOR_EM_EXERCÍCIO("Reitor em Exerc\u00edcio"),
    @XmlEnumValue("Respons\u00e1vel pelo registro")
    RESPONSÁVEL_PELO_REGISTRO("Respons\u00e1vel pelo registro"),
    @XmlEnumValue("Coordenador de Curso")
    COORDENADOR_DE_CURSO("Coordenador de Curso"),
    @XmlEnumValue("Subcoordenador de Curso")
    SUBCOORDENADOR_DE_CURSO("Subcoordenador de Curso"),
    @XmlEnumValue("Coordenador de Curso em exerc\u00edcio")
    COORDENADOR_DE_CURSO_EM_EXERCÍCIO("Coordenador de Curso em exerc\u00edcio"),
    @XmlEnumValue("Chefe da \u00e1rea de registro de diplomas")
    CHEFE_DA_ÁREA_DE_REGISTRO_DE_DIPLOMAS("Chefe da \u00e1rea de registro de diplomas"),
    @XmlEnumValue("Chefe em exerc\u00edcio da \u00e1rea de registro de diplomas")
    CHEFE_EM_EXERCÍCIO_DA_ÁREA_DE_REGISTRO_DE_DIPLOMAS("Chefe em exerc\u00edcio da \u00e1rea de registro de diplomas");
    private final String value;

    TCargosAssinantes(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TCargosAssinantes fromValue(String v) {
        for (TCargosAssinantes c: TCargosAssinantes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
