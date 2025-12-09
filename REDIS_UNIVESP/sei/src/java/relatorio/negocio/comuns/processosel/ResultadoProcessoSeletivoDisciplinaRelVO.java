/**
 * 
 */
package relatorio.negocio.comuns.processosel;

import java.io.Serializable;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.processosel.ResultadoDisciplinaProcSeletivoVO;

/**
 * @author Carlos Eugênio
 *
 */
public class ResultadoProcessoSeletivoDisciplinaRelVO extends SuperVO implements Serializable{

	
	private static final long serialVersionUID = 1L;
	private ResultadoDisciplinaProcSeletivoVO resultadoDisciplinaProcSeletivoVO;

	public ResultadoProcessoSeletivoDisciplinaRelVO() {
		super();
	}

	public ResultadoDisciplinaProcSeletivoVO getResultadoDisciplinaProcSeletivoVO() {
		if (resultadoDisciplinaProcSeletivoVO == null) {
			resultadoDisciplinaProcSeletivoVO = new ResultadoDisciplinaProcSeletivoVO();
		}
		return resultadoDisciplinaProcSeletivoVO;
	}

	public void setResultadoDisciplinaProcSeletivoVO(ResultadoDisciplinaProcSeletivoVO resultadoDisciplinaProcSeletivoVO) {
		this.resultadoDisciplinaProcSeletivoVO = resultadoDisciplinaProcSeletivoVO;
	}
		
}
