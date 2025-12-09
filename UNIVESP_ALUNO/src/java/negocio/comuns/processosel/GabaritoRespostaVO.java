package negocio.comuns.processosel;

import java.math.BigDecimal;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.SuperVO;
//import negocio.comuns.pesquisa.AreaConhecimentoVO;

public class GabaritoRespostaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private GabaritoVO gabaritoVO;
	private Integer nrQuestao;
	private String respostaCorreta;
	private Boolean anulado;
	private String historicoAnulado;

	/**
	 * Obrigatório apenas quando o <code>TipoGabaritoEnum</code> for
	 * PROCESSO_SELETIVO e atributo controlarGabaritoPorDisciplina estiver marcado
	 * em <code>GabaritoVO</code>.
	 */
	

	private DisciplinaVO disciplinaVO;
	
	private BigDecimal valorNota;

	public GabaritoRespostaVO() {

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

	public GabaritoVO getGabaritoVO() {
		if (gabaritoVO == null) {
			gabaritoVO = new GabaritoVO();
		}
		return gabaritoVO;
	}

	public void setGabaritoVO(GabaritoVO gabaritoVO) {
		this.gabaritoVO = gabaritoVO;
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	

	public BigDecimal getValorNota() {
		if (valorNota == null) {
			valorNota = BigDecimal.ZERO;
		}
		return valorNota;
	}

	public void setValorNota(BigDecimal valorNota) {
		this.valorNota = valorNota;
	}

	

	public Boolean getAnulado() {
		if (anulado == null) {
			anulado = Boolean.FALSE;
		}
		return anulado;
	}

	public void setAnulado(Boolean anulado) {
		this.anulado = anulado;
	}

	public String getHistoricoAnulado() {
		if (historicoAnulado == null) {
			historicoAnulado = "";
		}
		return historicoAnulado;
	}

	public void setHistoricoAnulado(String historicoAnulado) {
		this.historicoAnulado = historicoAnulado;
	}
}
