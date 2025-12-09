package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoTipoAdvertenciaEnum {

	ATIVO, INATIVO;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoTipoAdvertenciaEnum_" + this.name());
	}

}
