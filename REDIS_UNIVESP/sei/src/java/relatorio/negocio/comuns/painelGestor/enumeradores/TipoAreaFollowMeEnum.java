package relatorio.negocio.comuns.painelGestor.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum TipoAreaFollowMeEnum {
    
    DIRETORIA_FINANCEIRA,
    DIRETORIA_COMERCIAL,
    DIRETORIA_ADMINISTRATIVA,
    DIRETORIA_ACADEMICA;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoAreaFollowMeEnum_"+this.name());
    }

}
