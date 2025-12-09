package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.UteisJSF;

public enum FormaPadraoDataBaseCartaoRecorrenteEnum {
	TODOS, DIA_FIXO, VENCIMENTO_PRIMEIRA_FAIXA_DESCONTO;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_FormaPadraoDataBaseCartaoRecorrenteEnum_" + this.name());
	}
}
