package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum TipoQuestaoEnum {

    UNICA_ESCOLHA,
    MULTIPLA_ESCOLHA, 
    TEXTUAL;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoQuestaoEnum_"+this.name());
    }
    
    public String getName() {
    	return this.name();
    }
}
