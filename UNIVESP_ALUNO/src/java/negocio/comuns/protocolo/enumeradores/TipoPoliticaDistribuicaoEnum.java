package negocio.comuns.protocolo.enumeradores;

import java.util.ArrayList;
import java.util.List;

import jakarta.faces. model.SelectItem;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoPoliticaDistribuicaoEnum {
	DISTRIBUICAO_CIRCULAR, DISTRIBUICAO_QUANTITATIVA;

	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoPoliticaDistribuicaoEnum_" + this.name());
	}

	private static List<SelectItem> listaSelectItemTipoPoliticaDistribuicao;

	public static List<SelectItem> getListaSelectItemTipoPoliticaDistribuicao() {
		if (listaSelectItemTipoPoliticaDistribuicao == null) {
			listaSelectItemTipoPoliticaDistribuicao = new ArrayList<SelectItem>(0);
			for (TipoPoliticaDistribuicaoEnum tipoPoliticaDistribuicaoEnum : TipoPoliticaDistribuicaoEnum.values()) {
				listaSelectItemTipoPoliticaDistribuicao.add(new SelectItem(tipoPoliticaDistribuicaoEnum.name(), tipoPoliticaDistribuicaoEnum.getValorApresentar()));
			}
		}
		return listaSelectItemTipoPoliticaDistribuicao;
	}
}
