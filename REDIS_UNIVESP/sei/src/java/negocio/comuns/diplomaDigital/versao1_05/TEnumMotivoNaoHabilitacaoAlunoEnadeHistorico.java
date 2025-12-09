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
 * <p>Classe Java de TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico">
 *   &lt;restriction base="{http://portal.mec.gov.br/diplomadigital/arquivos-em-xsd}TString">
 *     &lt;enumeration value="Estudante não habilitado ao Enade em razão do calendário do ciclo avaliativo"/>
 *     &lt;enumeration value="Estudante não habilitado ao Enade em razão da natureza do projeto pedagógico do curso"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico")
@XmlEnum
public enum TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico {

    @XmlEnumValue("Estudante n\u00e3o habilitado ao Enade em raz\u00e3o do calend\u00e1rio do ciclo avaliativo")
    ESTUDANTE_NÃO_HABILITADO_AO_ENADE_EM_RAZÃO_DO_CALENDÁRIO_DO_CICLO_AVALIATIVO("Estudante n\u00e3o habilitado ao Enade em raz\u00e3o do calend\u00e1rio do ciclo avaliativo"),
    @XmlEnumValue("Estudante n\u00e3o habilitado ao Enade em raz\u00e3o da natureza do projeto pedag\u00f3gico do curso")
    ESTUDANTE_NÃO_HABILITADO_AO_ENADE_EM_RAZÃO_DA_NATUREZA_DO_PROJETO_PEDAGÓGICO_DO_CURSO("Estudante n\u00e3o habilitado ao Enade em raz\u00e3o da natureza do projeto pedag\u00f3gico do curso"),
    @XmlEnumValue("Calend\u00e1rio Trienal")
    CALENDARIO_TRIENAL("Calend\u00e1rio Trienal");
    private final String value;

    TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico fromValue(String v) {
        for (TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico c: TEnumMotivoNaoHabilitacaoAlunoEnadeHistorico.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
