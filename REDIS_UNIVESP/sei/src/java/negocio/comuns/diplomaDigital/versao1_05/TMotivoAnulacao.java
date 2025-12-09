//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2022.04.27 às 11:07:34 AM BRT 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de TMotivoAnulacao.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TMotivoAnulacao">
 *   &lt;restriction base="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString">
 *     &lt;enumeration value="Erro de Fato"/>
 *     &lt;enumeration value="Erro de Direito"/>
 *     &lt;enumeration value="Decisão Judicial"/>
 *     &lt;enumeration value="Reemissão para Complemento de Informação"/>
 *     &lt;enumeration value="Reemissão para Inclusão de Habilitação"/>
 *     &lt;enumeration value="Reemissão para Anotaçao de Registro"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TMotivoAnulacao")
@XmlEnum
public enum TMotivoAnulacao {

    @XmlEnumValue("Erro de Fato")
    ERRO_DE_FATO("Erro de Fato"),
    @XmlEnumValue("Erro de Direito")
    ERRO_DE_DIREITO("Erro de Direito"),
    @XmlEnumValue("Decis\u00e3o Judicial")
    DECISÃO_JUDICIAL("Decis\u00e3o Judicial"),
    @XmlEnumValue("Reemiss\u00e3o para Complemento de Informa\u00e7\u00e3o")
    REEMISSÃO_PARA_COMPLEMENTO_DE_INFORMAÇÃO("Reemiss\u00e3o para Complemento de Informa\u00e7\u00e3o"),
    @XmlEnumValue("Reemiss\u00e3o para Inclus\u00e3o de Habilita\u00e7\u00e3o")
    REEMISSÃO_PARA_INCLUSÃO_DE_HABILITAÇÃO("Reemiss\u00e3o para Inclus\u00e3o de Habilita\u00e7\u00e3o"),
    @XmlEnumValue("Reemiss\u00e3o para Anota\u00e7ao de Registro")
    REEMISSÃO_PARA_ANOTAÇAO_DE_REGISTRO("Reemiss\u00e3o para Anota\u00e7ao de Registro");
    private final String value;

    TMotivoAnulacao(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public String getValue() {
    	return value;
    }
    
    public static TMotivoAnulacao fromValue(String v) {
        for (TMotivoAnulacao c: TMotivoAnulacao.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
