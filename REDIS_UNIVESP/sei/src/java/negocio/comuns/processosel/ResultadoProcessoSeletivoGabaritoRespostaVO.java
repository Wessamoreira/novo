package negocio.comuns.processosel;

import negocio.comuns.arquitetura.SuperVO;

public class ResultadoProcessoSeletivoGabaritoRespostaVO extends SuperVO{
	
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO;
	private Integer nrQuestao;
	private String respostaCorreta;
	
	public ResultadoProcessoSeletivoGabaritoRespostaVO() {
		
	}

	public Integer getNrQuestao() {
		if (nrQuestao == null) {
			nrQuestao = 0;
		}
		return nrQuestao;
	}

	public void setNrQuestao(Integer nrQuestao) {
		this.nrQuestao = nrQuestao;
	}

	public String getRespostaCorreta() {
		if (respostaCorreta == null) {
			respostaCorreta = "";
		}
		return respostaCorreta;
	}

	public void setRespostaCorreta(String respostaCorreta) {
		this.respostaCorreta = respostaCorreta;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public ResultadoProcessoSeletivoVO getResultadoProcessoSeletivoVO() {
		if (resultadoProcessoSeletivoVO == null) {
			resultadoProcessoSeletivoVO = new ResultadoProcessoSeletivoVO();
		}
		return resultadoProcessoSeletivoVO;
	}

	public void setResultadoProcessoSeletivoVO(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO) {
		this.resultadoProcessoSeletivoVO = resultadoProcessoSeletivoVO;
	}
	
	
}
