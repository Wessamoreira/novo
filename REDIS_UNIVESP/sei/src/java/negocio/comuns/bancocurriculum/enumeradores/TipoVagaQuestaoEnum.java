package negocio.comuns.bancocurriculum.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoVagaQuestaoEnum {
	TEXTUAL, UNICA_ESCOLHA, MULTIPLA_ESCOLHA;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoVagaQuestaoEnum_"+this.name());
	}
}
