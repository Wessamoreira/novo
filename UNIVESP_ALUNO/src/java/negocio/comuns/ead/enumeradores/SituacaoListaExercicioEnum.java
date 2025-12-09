package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum SituacaoListaExercicioEnum {
    ATIVA, INATIVA, EM_ELABORACAO;
    
    public String getValorApresentar(){
        return  UteisJSF.internacionalizar("enum_SituacaoListaExercicioEnum_"+name());
    }
    
    public String getName() {
    	return this.name();
    }
}
