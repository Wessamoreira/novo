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
 * <p>Classe Java de TFormaAcessoCurso.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TFormaAcessoCurso">
 *   &lt;restriction base="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString">
 *     &lt;enumeration value="Vestibular"/>
 *     &lt;enumeration value="Enem"/>
 *     &lt;enumeration value="Avaliação Seriada"/>
 *     &lt;enumeration value="Seleção Simplificada"/>
 *     &lt;enumeration value="Egresso BI/LI"/>
 *     &lt;enumeration value="PEC-G"/>
 *     &lt;enumeration value="Transferência Ex Officio"/>
 *     &lt;enumeration value="Decisão judicial"/>
 *     &lt;enumeration value="Seleção para Vagas Remanescentes"/>
 *     &lt;enumeration value="Seleção para Vagas de Programas Especiais"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TFormaAcessoCurso")
@XmlEnum
public enum TFormaAcessoCurso {

    @XmlEnumValue("Vestibular")
    VESTIBULAR("Vestibular"),
    @XmlEnumValue("Enem")
    ENEM("Enem"),
    @XmlEnumValue("Avalia\u00e7\u00e3o Seriada")
    AVALIAÇÃO_SERIADA("Avalia\u00e7\u00e3o Seriada"),
    @XmlEnumValue("Sele\u00e7\u00e3o Simplificada")
    SELEÇÃO_SIMPLIFICADA("Sele\u00e7\u00e3o Simplificada"),
    @XmlEnumValue("Egresso BI/LI")
    EGRESSO_BI_LI("Egresso BI/LI"),
    @XmlEnumValue("PEC-G")
    PEC_G("PEC-G"),
    @XmlEnumValue("Transfer\u00eancia Ex Officio")
    TRANSFERÊNCIA_EX_OFFICIO("Transfer\u00eancia Ex Officio"),
    @XmlEnumValue("Decis\u00e3o judicial")
    DECISÃO_JUDICIAL("Decis\u00e3o judicial"),
    @XmlEnumValue("Sele\u00e7\u00e3o para Vagas Remanescentes")
    SELEÇÃO_PARA_VAGAS_REMANESCENTES("Sele\u00e7\u00e3o para Vagas Remanescentes"),
    @XmlEnumValue("Sele\u00e7\u00e3o para Vagas de Programas Especiais")
    SELEÇÃO_PARA_VAGAS_DE_PROGRAMAS_ESPECIAIS("Sele\u00e7\u00e3o para Vagas de Programas Especiais");
    private final String value;

    TFormaAcessoCurso(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TFormaAcessoCurso fromValue(String v) {
        for (TFormaAcessoCurso c: TFormaAcessoCurso.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
