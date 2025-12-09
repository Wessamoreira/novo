package negocio.comuns.compras.enumeradores;

import java.util.Collections;
import java.util.Comparator;

import negocio.comuns.compras.EstoqueVO;

public enum OrdenarEstoqueEnum implements Comparator<EstoqueVO> {

	CODIGO() {
		@Override
		public int compare(EstoqueVO one, EstoqueVO other) {
			return one.getCodigo().compareTo(other.getCodigo());
		}
	},

	NOME_UNIDADE_ENSINO() {
		@Override
		public int compare(EstoqueVO one, EstoqueVO other) {
			return one.getUnidadeEnsino().getNome().compareTo(other.getUnidadeEnsino().getNome());
		}
	},
	
	QUANTIDADE() {
		@Override
		public int compare(EstoqueVO one, EstoqueVO other) {
			return one.getQuantidade().compareTo(other.getQuantidade());
		}
	},
	
	DATA_ENTRADA() {
		@Override
		public int compare(EstoqueVO one, EstoqueVO other) {
			return one.getDataEntrada().compareTo(other.getDataEntrada());
		}
	},
	
	DATA_ENTRADA_QUANTIDADE() {
		@Override
		public int compare(EstoqueVO one, EstoqueVO other) {
			int resultado =  DATA_ENTRADA.compare(one, other);
			return resultado == 0 ? QUANTIDADE.compare(one, other) : resultado;
		}
	};

	public abstract int compare(EstoqueVO one, EstoqueVO other);

	public Comparator<EstoqueVO> asc() {
		return this;
	}

	public Comparator<EstoqueVO> desc() {
		return Collections.reverseOrder(this);
	}

}
