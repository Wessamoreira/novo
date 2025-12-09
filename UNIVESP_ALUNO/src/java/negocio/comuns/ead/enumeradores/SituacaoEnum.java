package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoEnum {
	ATIVO, FINALIZADO, EM_CONSTRUCAO, INATIVO;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
	
	public boolean isAtivo(){
    	return name().equals(SituacaoEnum.ATIVO.name());
    }
    
    public boolean isInativo(){
    	return name().equals(SituacaoEnum.INATIVO.name());
    }
    
    public boolean isEmElaboracao(){
    	return name().equals(SituacaoEnum.EM_CONSTRUCAO.name());
    }
    
    public boolean isFinalizado(){
    	return name().equals(SituacaoEnum.FINALIZADO.name());
    }
}
