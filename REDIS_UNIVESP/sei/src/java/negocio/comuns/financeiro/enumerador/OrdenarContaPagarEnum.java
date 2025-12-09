package negocio.comuns.financeiro.enumerador;

import java.util.Collections;
import java.util.Comparator;

import negocio.comuns.financeiro.ContaPagarVO;

public enum OrdenarContaPagarEnum implements Comparator<ContaPagarVO>{

	CODIGO() {
		@Override
		public int compare(ContaPagarVO one, ContaPagarVO other) {
			return one.getCodigo().compareTo(other.getCodigo());
		}
	},
	
	DATA_VENCIMENTO_LONG() {
		@Override
		public int compare(ContaPagarVO one, ContaPagarVO other) {
			return one.getDataVencimento_Time().compareTo(other.getDataVencimento_Time());
		}
	},
	
	DATA_VENCIMENTO() {
		@Override
		public int compare(ContaPagarVO one, ContaPagarVO other) {
			return one.getDataVencimento().compareTo(other.getDataVencimento());
		}
	};

	public abstract int compare(ContaPagarVO one, ContaPagarVO other);

	public Comparator<ContaPagarVO> asc() {
		return this;
	}

	public Comparator<ContaPagarVO> desc() {
		return Collections.reverseOrder(this);
	}

}
