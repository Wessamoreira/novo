package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum SituacaoQuestaoEnum {

    EM_ELABORACAO, ATIVA, INATIVA, CANCELADA, ANULADA;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_SituacaoQuestaoEnum_"+this.name());
    }
    
    
    public String getName() {
    	return this.name();
    }
    
}
