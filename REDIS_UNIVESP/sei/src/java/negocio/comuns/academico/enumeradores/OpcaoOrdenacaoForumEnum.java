package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum OpcaoOrdenacaoForumEnum {
    
    TEMA, ULTIMA_ATUALIZACAO , ANTIGO , NOVO;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_OpcaoOrdenacaoForumEnum_"+this.name());
    }
        
    
    
}
