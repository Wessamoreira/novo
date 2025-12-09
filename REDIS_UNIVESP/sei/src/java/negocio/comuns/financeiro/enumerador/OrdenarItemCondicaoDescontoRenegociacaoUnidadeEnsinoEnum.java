package negocio.comuns.financeiro.enumerador;

import java.util.Collections;
import java.util.Comparator;

import negocio.comuns.financeiro.ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO;

public enum OrdenarItemCondicaoDescontoRenegociacaoUnidadeEnsinoEnum implements Comparator<ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO> {

	CODIGO() {
		@Override
		public int compare(ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO one, ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO other) {
			return one.getCodigo().compareTo(other.getCodigo());
		}
	},
	UNIDADE_ENSINO_NOME() {
		@Override
		public int compare(ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO one, ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO other) {
			return one.getUnidadeEnsinoVO().getNome().compareTo(other.getUnidadeEnsinoVO().getNome());
		}
	};

	public abstract int compare(ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO one, ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO other);

	public Comparator<ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO> asc() {
		return this;
	}

	public Comparator<ItemCondicaoDescontoRenegociacaoUnidadeEnsinoVO> desc() {
		return Collections.reverseOrder(this);
	}

}
