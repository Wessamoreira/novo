package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoDocumentacaoEnum {

//    TERMO_RESPONSABILIDADE,
    DOCUMENTO_IDENTIDADE_DO_ALUNO,
    PROVA_CONCLUSAO_ENSINO_MEDIO,
//    HISTORICO_ESCOLAR,
    PROVA_COLACAO,
    COMPROVACAO_ESTAGIO_CURRICULAR,
    CERTIDAO_NASCIMENTO,
    CERTIDAO_CASAMENTO,
    TITULO_ELEITOR,
    ATO_NATURALIZACAO,
    OUTROS;

    public String getValorApresentar() {
        return UteisJSF.internacionalizar("enum_TipoDocumentacaoEnum_" + this.name());
    }

}
