package negocio.comuns.compras.enumeradores;

import java.util.Collections;
import java.util.Comparator;

import negocio.comuns.compras.ItemCotacaoVO;

public enum OrdenarItemCotacaoEnum implements Comparator<ItemCotacaoVO> {

	PRECO_UNITATIO() {
		@Override
		public int compare(ItemCotacaoVO one, ItemCotacaoVO other) {
			return one.getPrecoUnitario().compareTo(other.getPrecoUnitario());
		}
	},
	PRODUTO() {
		@Override
		public int compare(ItemCotacaoVO one, ItemCotacaoVO other) {
			return one.getProduto().getNome().compareTo(other.getProduto().getNome());
		}
	};

	public abstract int compare(ItemCotacaoVO one, ItemCotacaoVO other);

	public Comparator<ItemCotacaoVO> asc() {
		return this;
	}

	public Comparator<ItemCotacaoVO> desc() {
		return Collections.reverseOrder(this);
	}

}
