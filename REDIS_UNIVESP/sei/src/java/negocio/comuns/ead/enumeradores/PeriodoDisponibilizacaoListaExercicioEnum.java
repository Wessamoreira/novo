package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum PeriodoDisponibilizacaoListaExercicioEnum {
    
    INDETERMINADO, PERIODO;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_PeriodoDisponibilizacaoListaExercicioEnum_"+this.name());
    } 
    
    public String getName() {
    	return this.name();
    }
}
