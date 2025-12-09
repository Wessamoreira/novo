package negocio.comuns.financeiro.enumerador;

import java.util.Collections;
import java.util.Comparator;

import negocio.comuns.financeiro.DetalhamentoValorContaVO;

public enum OrdenarCentroResultadoOrigemDetalheEnum implements Comparator<DetalhamentoValorContaVO>{

	PESO() {
		@Override
		public int compare(DetalhamentoValorContaVO one, DetalhamentoValorContaVO other) {
			return one.getOrdemApresentacao().compareTo(other.getOrdemApresentacao());
		}
	};

	public abstract int compare(DetalhamentoValorContaVO one, DetalhamentoValorContaVO other);

	public Comparator<DetalhamentoValorContaVO> asc() {
		return this;
	}

	public Comparator<DetalhamentoValorContaVO> desc() {
		return Collections.reverseOrder(this);
	}


}
