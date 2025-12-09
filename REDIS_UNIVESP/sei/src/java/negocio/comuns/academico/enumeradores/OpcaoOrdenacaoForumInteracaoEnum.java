package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum OpcaoOrdenacaoForumInteracaoEnum {
    DATA_INTERACAO, MAIS_GOSTADOS, MINHAS_INTERACOES;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_OpcaoOrdenacaoForumInteracaoEnum_"+this.name());
    }
}
