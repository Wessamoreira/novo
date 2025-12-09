package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Wellington - 15 de jan de 2016
 *
 */
public enum TipoControleComposicaoEnum {

	ESTUDAR_TODAS_COMPOSTAS, ESTUDAR_QUANTIDADE_MAXIMA_COMPOSTA;

	public String getValor() {
		return this.name();
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_TipoControleComposicaoEnum_" + this.name());
	}

}
