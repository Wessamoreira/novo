package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum TipoGeracaoListaExercicioEnum {

    RANDOMICO, FIXO;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_TipoGeracaoListaExercicioEnum_"+this.name());
    }
    
    public String getName() {
    	return this.name();
    }
}
