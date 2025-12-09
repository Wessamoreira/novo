package negocio.comuns.processosel;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class ColunaGabaritoVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer coluna;
	private List<GabaritoRespostaVO> gabaritoRespostaVOs;
	private List<ResultadoProcessoSeletivoGabaritoRespostaVO> resultadoProcessoSeletivoGabaritoRespostaVOs;
	
	public ColunaGabaritoVO() {
		
	}

	public Integer getColuna() {
		if (coluna == null) {
			coluna = 0;
		}
		return coluna;
	}

	public void setColuna(Integer coluna) {
		this.coluna = coluna;
	}

	public List<GabaritoRespostaVO> getGabaritoRespostaVOs() {
		if (gabaritoRespostaVOs == null) {
			gabaritoRespostaVOs = new ArrayList<GabaritoRespostaVO>(0);
		}
		return gabaritoRespostaVOs;
	}

	public void setGabaritoRespostaVOs(List<GabaritoRespostaVO> gabaritoRespostaVOs) {
		this.gabaritoRespostaVOs = gabaritoRespostaVOs;
	}

	public List<ResultadoProcessoSeletivoGabaritoRespostaVO> getResultadoProcessoSeletivoGabaritoRespostaVOs() {
		if (resultadoProcessoSeletivoGabaritoRespostaVOs == null) {
			resultadoProcessoSeletivoGabaritoRespostaVOs = new ArrayList<ResultadoProcessoSeletivoGabaritoRespostaVO>(0);
		}
		return resultadoProcessoSeletivoGabaritoRespostaVOs;
	}

	public void setResultadoProcessoSeletivoGabaritoRespostaVOs(List<ResultadoProcessoSeletivoGabaritoRespostaVO> resultadoProcessoSeletivoGabaritoRespostaVOs) {
		this.resultadoProcessoSeletivoGabaritoRespostaVOs = resultadoProcessoSeletivoGabaritoRespostaVOs;
	}
	

}
