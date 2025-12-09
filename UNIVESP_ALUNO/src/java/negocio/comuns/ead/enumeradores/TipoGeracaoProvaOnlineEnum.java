package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/*
 * @author Victor Hugo 10/10/2014
 */
public enum TipoGeracaoProvaOnlineEnum {

	NENHUM, RANDOMICO_POR_COMPLEXIDADE, FIXO;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoGeracaoProvaOnlineEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}

	public boolean isRandomicoPorComplexidade() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoGeracaoProvaOnlineEnum.RANDOMICO_POR_COMPLEXIDADE.name());
	}

	public boolean isFixo() {
		return Uteis.isAtributoPreenchido(name()) && name().equals(TipoGeracaoProvaOnlineEnum.FIXO.name());
	}
}
