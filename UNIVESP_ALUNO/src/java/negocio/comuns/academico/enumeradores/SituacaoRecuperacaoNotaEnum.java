package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoRecuperacaoNotaEnum {

	EM_RECUPERACAO, NOTA_RECUPERADA, NOTA_NAO_RECUPERADA, SEM_RECUPERACAO, TODAS;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_SituacaoRecuperacaoNotaEnum_"+this.name());
	}
}
