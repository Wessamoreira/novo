package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum ResultadoEnum {
	SUCESSO, FALHA;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
	
    public boolean isSucesso(){
    	return name().equals(ResultadoEnum.SUCESSO.name());
    }
    
    public boolean isFalha(){
    	return name().equals(ResultadoEnum.FALHA.name());
    }
    
}