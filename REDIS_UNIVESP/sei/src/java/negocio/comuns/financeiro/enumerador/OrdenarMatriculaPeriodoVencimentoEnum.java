package negocio.comuns.financeiro.enumerador;

import java.util.Collections;
import java.util.Comparator;

import negocio.comuns.financeiro.MatriculaPeriodoVencimentoVO;


public enum OrdenarMatriculaPeriodoVencimentoEnum implements Comparator<MatriculaPeriodoVencimentoVO>{
	
	DATA_VENCIMENTO() {
		@Override
		public int compare(MatriculaPeriodoVencimentoVO one, MatriculaPeriodoVencimentoVO other) {
			return one.getDataVencimento().compareTo(other.getDataVencimento()) ;
		}		
	},
	
	DATA_VENCIMENTO_AND_TIPO_ORIGEM() {
		@Override
		public int compare(MatriculaPeriodoVencimentoVO one, MatriculaPeriodoVencimentoVO other) {
			int resultado = one.getDataVencimento().compareTo(other.getDataVencimento()) ;
			return resultado == 0 ?  one.getTipoOrigemMatriculaPeriodoVencimento().compareTo(other.getTipoOrigemMatriculaPeriodoVencimento()) : resultado;
		}		
	};
	

	public abstract int compare(MatriculaPeriodoVencimentoVO one, MatriculaPeriodoVencimentoVO other);
	
	public Comparator<MatriculaPeriodoVencimentoVO> asc() {
		return this;
	}

	public Comparator<MatriculaPeriodoVencimentoVO> desc() {
		return Collections.reverseOrder(this);
	}

}
