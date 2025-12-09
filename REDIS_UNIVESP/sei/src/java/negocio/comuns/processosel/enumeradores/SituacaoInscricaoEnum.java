package negocio.comuns.processosel.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoInscricaoEnum {

	ATIVO, CANCELADO_OUTRA_INSCRICAO, NAO_COMPARECEU, CANCELADO;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoInscricaoEnum_" + this.name());
	}

}
