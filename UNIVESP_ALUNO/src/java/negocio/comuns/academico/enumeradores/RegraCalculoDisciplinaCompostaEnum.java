package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum RegraCalculoDisciplinaCompostaEnum {
	MEDIA_FILHA_COMPOSICAO, FUNCAO_COMPOSICAO;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_RegraCalculoDisciplinaCompostaEnum_"+this.name());
	}
}
