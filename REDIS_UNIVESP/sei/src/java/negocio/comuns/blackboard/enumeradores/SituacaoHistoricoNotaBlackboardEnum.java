package negocio.comuns.blackboard.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoHistoricoNotaBlackboardEnum {

    APURADO,
    NAO_LOCALIZADO,
    EM_AUDITORIA,
    DEFERIDO,
    INDEFERIDO,
    ERRO;

    public String getValorApresentar() {
        return UteisJSF.internacionalizar("enum_SituacaoHistoricoNotaBlackboardEnum_"+this.name());
    }

}
