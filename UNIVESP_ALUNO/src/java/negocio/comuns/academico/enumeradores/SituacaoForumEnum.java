package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum SituacaoForumEnum {
    
	TODOS, ATIVO, INATIVO;    
   
    
    public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoForumEnum_" + this.name());
	}

	public boolean isSituacaoTodos() {
		return name().equals(SituacaoForumEnum.TODOS.name());
	}

	public boolean isSituacaoAtivo() {
		return name().equals(SituacaoForumEnum.ATIVO.name());
	}

	public boolean isSituacaoInativo() {
		return name().equals(SituacaoForumEnum.INATIVO.name());
	}

}
