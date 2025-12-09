package negocio.comuns.contabil.enumeradores;

import java.util.Collections;
import java.util.Comparator;

import negocio.comuns.faturamento.nfe.NotaFiscalEntradaRecebimentoCompraVO;

public enum OrdenarNotaFiscalEntradaRecebimentoCompraEnum implements Comparator<NotaFiscalEntradaRecebimentoCompraVO> {

	COMPRA_RECEBIMENTOCOMPRA() {
		@Override
		public int compare(NotaFiscalEntradaRecebimentoCompraVO one, NotaFiscalEntradaRecebimentoCompraVO other) {
			int resultado = one.getRecebimentoCompraVO().getCompra().getCodigo().compareTo(other.getRecebimentoCompraVO().getCompra().getCodigo());
			return resultado == 0 ? one.getRecebimentoCompraVO().getCodigo().compareTo(other.getRecebimentoCompraVO().getCodigo()) : resultado;
		}
	};

	public abstract int compare(NotaFiscalEntradaRecebimentoCompraVO one, NotaFiscalEntradaRecebimentoCompraVO other);

	public Comparator<NotaFiscalEntradaRecebimentoCompraVO> asc() {
		return this;
	}

	public Comparator<NotaFiscalEntradaRecebimentoCompraVO> desc() {
		return Collections.reverseOrder(this);
	}

}
