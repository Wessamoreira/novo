package negocio.comuns.academico.enumeradores;

import java.util.Collections;
import java.util.Comparator;
import negocio.comuns.academico.ControleLivroFolhaReciboVO;


public enum OrdenarControleLivroFolhaReciboEnum implements Comparator<ControleLivroFolhaReciboVO> {

	DATA_CADASTRO_LONG() {
		@Override
		public int compare(ControleLivroFolhaReciboVO one, ControleLivroFolhaReciboVO other) {
			return one.getDataCadastro_Time().compareTo(other.getDataCadastro_Time());
		}
	};
	public abstract int compare(ControleLivroFolhaReciboVO one, ControleLivroFolhaReciboVO other);

	public Comparator<ControleLivroFolhaReciboVO> asc() {
		return this;
	}

	public Comparator<ControleLivroFolhaReciboVO> desc() {
		return Collections.reverseOrder(this);
	}

}
