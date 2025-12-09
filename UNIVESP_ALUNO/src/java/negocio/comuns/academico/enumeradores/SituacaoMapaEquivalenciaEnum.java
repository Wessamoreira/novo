package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoMapaEquivalenciaEnum {
	
	EM_CONSTRUCAO, ATIVO, INATIVO;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_SituacaoMapaEquivalenciaEnum_"+this.name());
	}

}
