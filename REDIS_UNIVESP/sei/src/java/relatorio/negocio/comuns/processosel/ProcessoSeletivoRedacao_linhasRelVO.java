package relatorio.negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class ProcessoSeletivoRedacao_linhasRelVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer numero;

	public Integer getNumero() {
		if (numero == null) {
			numero = 0;
		}
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
}
