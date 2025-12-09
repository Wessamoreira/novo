package negocio.comuns.contabil.enumeradores;

import java.util.Collections;
import java.util.Comparator;

import negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemVO;

public enum OrdenarNotaFiscalEntradaItemEnum implements Comparator<NotaFiscalEntradaItemVO>{
	CODIGO() {
		@Override
		public int compare(NotaFiscalEntradaItemVO one, NotaFiscalEntradaItemVO other) {
			return one.getCodigo().compareTo(other.getCodigo());
		}		
	};
	

	public abstract int compare(NotaFiscalEntradaItemVO one, NotaFiscalEntradaItemVO other);

	public Comparator<NotaFiscalEntradaItemVO> asc() {
		return this;
	}

	public Comparator<NotaFiscalEntradaItemVO> desc() {
		return Collections.reverseOrder(this);
	}

}
