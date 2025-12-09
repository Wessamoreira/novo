package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;


public enum NivelComplexidadeQuestaoEnum {
    
    FACIL, MEDIO, DIFICIL;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_NivelComplexidadeQuestaoEnum_"+this.name());
    }
    
    public String getName() {
    	return this.name();
    }
    
    public boolean isFacil() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(NivelComplexidadeQuestaoEnum.FACIL.name());
	}
    
    public boolean isMedio() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(NivelComplexidadeQuestaoEnum.MEDIO.name());
    }
    
    public boolean isDificil() {
    	return Uteis.isAtributoPreenchido(name()) && name().equals(NivelComplexidadeQuestaoEnum.DIFICIL.name());
    }
}
