
package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de TEnumSituacaoAlunoEnadeHistorico.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TEnumSituacaoAlunoEnadeHistorico">
 *   &lt;restriction base="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString">
 *     &lt;enumeration value="IRREGULAR"/>
 *     &lt;enumeration value="Ingressante / Participante"/>
 *     &lt;enumeration value="Dispensado, em razão do calendário trienal"/>
 *     &lt;enumeration value="Concluinte / Participante"/>
 *     &lt;enumeration value="Dispensado pelo MEC"/>
 *     &lt;enumeration value="Dispensado, em razão da natureza do curso"/>
 *     &lt;enumeration value="Dispensado, por razão de ordem pessoal"/>
 *     &lt;enumeration value="Dispensado, por ato da instituição de ensino"/>
 *     &lt;enumeration value="Dispensado, em razão de mobilidade acadêmica no exterior"/>
 *     &lt;enumeration value="Estudante não habilitado ao ENADE em razão do calendário do ciclo avaliativo"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TEnumSituacaoAlunoEnadeHistorico", namespace = "http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd")
@XmlEnum
public enum TEnumSituacaoAlunoEnadeHistorico {

    IRREGULAR("IRREGULAR"),
    @XmlEnumValue("Ingressante / Participante")
    INGRESSANTE_PARTICIPANTE("Ingressante / Participante"),
    @XmlEnumValue("Dispensado, em raz\u00e3o do calend\u00e1rio trienal")
    DISPENSADO_EM_RAZÃO_DO_CALENDÁRIO_TRIENAL("Dispensado, em raz\u00e3o do calend\u00e1rio trienal"),
    @XmlEnumValue("Concluinte / Participante")
    CONCLUINTE_PARTICIPANTE("Concluinte / Participante"),
    @XmlEnumValue("Dispensado pelo MEC")
    DISPENSADO_PELO_MEC("Dispensado pelo MEC"),
    @XmlEnumValue("Dispensado, em raz\u00e3o da natureza do curso")
    DISPENSADO_EM_RAZÃO_DA_NATUREZA_DO_CURSO("Dispensado, em raz\u00e3o da natureza do curso"),
    @XmlEnumValue("Dispensado, por raz\u00e3o de ordem pessoal")
    DISPENSADO_POR_RAZÃO_DE_ORDEM_PESSOAL("Dispensado, por raz\u00e3o de ordem pessoal"),
    @XmlEnumValue("Dispensado, por ato da institui\u00e7\u00e3o de ensino")
    DISPENSADO_POR_ATO_DA_INSTITUIÇÃO_DE_ENSINO("Dispensado, por ato da institui\u00e7\u00e3o de ensino"),
    @XmlEnumValue("Dispensado, em raz\u00e3o de mobilidade acad\u00eamica no exterior")
    DISPENSADO_EM_RAZÃO_DE_MOBILIDADE_ACADÊMICA_NO_EXTERIOR("Dispensado, em raz\u00e3o de mobilidade acad\u00eamica no exterior"),
    @XmlEnumValue("Estudante n\u00e3o habilitado ao ENADE em raz\u00e3o do calend\u00e1rio do ciclo avaliativo")
    ESTUDANTE_NÃO_HABILITADO_AO_ENADE_EM_RAZÃO_DO_CALENDÁRIO_DO_CICLO_AVALIATIVO("Estudante n\u00e3o habilitado ao ENADE em raz\u00e3o do calend\u00e1rio do ciclo avaliativo");
    private final String value;

    TEnumSituacaoAlunoEnadeHistorico(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TEnumSituacaoAlunoEnadeHistorico fromValue(String v) {
        for (TEnumSituacaoAlunoEnadeHistorico c: TEnumSituacaoAlunoEnadeHistorico.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
