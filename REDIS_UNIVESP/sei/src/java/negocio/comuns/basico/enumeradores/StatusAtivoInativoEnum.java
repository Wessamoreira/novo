package negocio.comuns.basico.enumeradores;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;


public enum StatusAtivoInativoEnum {
    
	NENHUM, ATIVO, INATIVO;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_StatusAtivoInativoEnum_"+this.name());
    }
    
    public boolean isAtivo(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(StatusAtivoInativoEnum.ATIVO.name()); 
	}
    
    public boolean isInativo(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(StatusAtivoInativoEnum.INATIVO.name()); 
    }

}
