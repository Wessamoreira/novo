package negocio.comuns.contabil.enumeradores;

import java.util.Collections;
import java.util.Comparator;

import negocio.comuns.contabil.ConfiguracaoContabilRegraVO;


public enum OrdenarConfiguracaoContabilRegraEnum implements Comparator<ConfiguracaoContabilRegraVO> {
	
	ATRIBUTO_PREENCHIDO() {
		@Override
		public int compare(ConfiguracaoContabilRegraVO one, ConfiguracaoContabilRegraVO other) {
			return one.getQdtAtributosPreenchido().compareTo(other.getQdtAtributosPreenchido());
		}		
	};
	

	public abstract int compare(ConfiguracaoContabilRegraVO one, ConfiguracaoContabilRegraVO other);

	public Comparator<ConfiguracaoContabilRegraVO> asc() {
		return this;
	}

	public Comparator<ConfiguracaoContabilRegraVO> desc() {
		return Collections.reverseOrder(this);
	}

}
