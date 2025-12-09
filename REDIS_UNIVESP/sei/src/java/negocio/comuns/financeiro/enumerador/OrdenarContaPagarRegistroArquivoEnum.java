package negocio.comuns.financeiro.enumerador;

import java.util.Collections;
import java.util.Comparator;

import negocio.comuns.financeiro.ContaPagarRegistroArquivoVO;


public enum OrdenarContaPagarRegistroArquivoEnum implements Comparator<ContaPagarRegistroArquivoVO>{	
	
	OBSERVACAO_NOME() {
		@Override
		public int compare(ContaPagarRegistroArquivoVO one, ContaPagarRegistroArquivoVO other) {
			int resultado = one.getContaPagarVO().getObservacao().compareTo(other.getContaPagarVO().getObservacao());
			return resultado == 0 ? one.getContaPagarVO().getFavorecido_Apresentar().compareTo(other.getContaPagarVO().getFavorecido_Apresentar()) : resultado;
		}		
	};
	

	public abstract int compare(ContaPagarRegistroArquivoVO one, ContaPagarRegistroArquivoVO other);

	public Comparator<ContaPagarRegistroArquivoVO> asc() {
		return this;
	}

	public Comparator<ContaPagarRegistroArquivoVO> desc() {
		return Collections.reverseOrder(this);
	}

}
