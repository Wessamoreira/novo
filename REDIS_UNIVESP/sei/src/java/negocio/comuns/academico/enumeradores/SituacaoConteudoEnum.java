package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum SituacaoConteudoEnum {

    EM_ELABORACAO, ATIVO, INATIVO;
    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_SituacaoConteudoEnum_"+this.name());
    }
    
    public String getNome() {
		return this.name();
	}
	
    public static SituacaoConteudoEnum getEnum(String valor) {
        SituacaoConteudoEnum[] valores = values();
        for (SituacaoConteudoEnum obj : valores) {
            if (obj.toString().equals(valor)) {
                return obj;
            }
        }
        return null;
    }	
    
    public boolean isAtivo(){
    	return name().equals(SituacaoConteudoEnum.ATIVO.name());
    }
    
    public boolean isInativo(){
    	return name().equals(SituacaoConteudoEnum.INATIVO.name());
    }
    
    public boolean isEmElaboracao(){
    	return name().equals(SituacaoConteudoEnum.EM_ELABORACAO.name());
    }
}
