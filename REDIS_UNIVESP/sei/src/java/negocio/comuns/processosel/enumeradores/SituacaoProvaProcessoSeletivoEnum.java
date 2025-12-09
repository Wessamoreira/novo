package negocio.comuns.processosel.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoProvaProcessoSeletivoEnum {

	ATIVA, INATIVA, EM_ELABORACAO;

	public String getName() {
		return name();
	}

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoProvaProcessoSeletivoEnum_" + this.name());
	}

}
