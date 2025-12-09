package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Wellington - 8 de dez de 2015
 *
 */
public enum LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum {

	LAYOUT_1, LAYOUT_2, TEXTO_PADRAO;

	public String getValor() {
		return this.name();
	}

	public String getDescricao() {
		return UteisJSF.internacionalizar("enum_LayoutPadraoTermoReconhecimentoDividaCondicaoRenegociacaoEnum_" + this.name());
	}

}
