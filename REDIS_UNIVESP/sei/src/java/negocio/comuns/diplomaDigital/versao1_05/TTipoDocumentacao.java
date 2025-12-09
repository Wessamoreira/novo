//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.8-b130911.1802 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2023.01.20 às 10:57:34 AM BRST 
//


package negocio.comuns.diplomaDigital.versao1_05;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de TTipoDocumentacao.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * <p>
 * <pre>
 * &lt;simpleType name="TTipoDocumentacao">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;whiteSpace value="collapse"/>
 *     &lt;enumeration value="DocumentoIdentidadeDoAluno"/>
 *     &lt;enumeration value="ProvaConclusaoEnsinoMedio"/>
 *     &lt;enumeration value="ProvaColacao"/>
 *     &lt;enumeration value="ComprovacaoEstagioCurricular"/>
 *     &lt;enumeration value="CertidaoNascimento"/>
 *     &lt;enumeration value="CertidaoCasamento"/>
 *     &lt;enumeration value="TituloEleitor"/>
 *     &lt;enumeration value="AtoNaturalizacao"/>
 *     &lt;enumeration value="Outros"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TTipoDocumentacao")
@XmlEnum
public enum TTipoDocumentacao {

    @XmlEnumValue("DocumentoIdentidadeDoAluno")
    DOCUMENTO_IDENTIDADE_DO_ALUNO("DocumentoIdentidadeDoAluno"),
    @XmlEnumValue("ProvaConclusaoEnsinoMedio")
    PROVA_CONCLUSAO_ENSINO_MEDIO("ProvaConclusaoEnsinoMedio"),
    @XmlEnumValue("ProvaColacao")
    PROVA_COLACAO("ProvaColacao"),
    @XmlEnumValue("ComprovacaoEstagioCurricular")
    COMPROVACAO_ESTAGIO_CURRICULAR("ComprovacaoEstagioCurricular"),
    @XmlEnumValue("CertidaoNascimento")
    CERTIDAO_NASCIMENTO("CertidaoNascimento"),
    @XmlEnumValue("CertidaoCasamento")
    CERTIDAO_CASAMENTO("CertidaoCasamento"),
    @XmlEnumValue("TituloEleitor")
    TITULO_ELEITOR("TituloEleitor"),
    @XmlEnumValue("AtoNaturalizacao")
    ATO_NATURALIZACAO("AtoNaturalizacao"),
    @XmlEnumValue("Outros")
    OUTROS("Outros");
    private final String value;

    TTipoDocumentacao(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TTipoDocumentacao fromValue(String v) {
        for (TTipoDocumentacao c: TTipoDocumentacao.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
