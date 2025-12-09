package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.processosel.PerguntaChecklistVO;
import negocio.comuns.utilitarias.Uteis;

public class PerguntaChecklistOrigemVO extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4847899681479759856L;
	private Integer codigo;
	private PerguntaRespostaOrigemVO perguntaRespostaOrigemVO;
	private PerguntaChecklistVO perguntaChecklistVO;
	private boolean checklist;
	
	public PerguntaChecklistOrigemVO getClone() {
		PerguntaChecklistOrigemVO clone = new PerguntaChecklistOrigemVO();
		clone = (PerguntaChecklistOrigemVO) Uteis.clonar(this);
		clone.setCodigo(0);
		return clone;
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

	public PerguntaRespostaOrigemVO getPerguntaRespostaOrigemVO() {
		if (perguntaRespostaOrigemVO == null) {
			perguntaRespostaOrigemVO = new PerguntaRespostaOrigemVO();
		}
		return perguntaRespostaOrigemVO;
	}

	public void setPerguntaRespostaOrigemVO(PerguntaRespostaOrigemVO perguntaRespostaOrigemVO) {
		this.perguntaRespostaOrigemVO = perguntaRespostaOrigemVO;
	}

	public PerguntaChecklistVO getPerguntaChecklistVO() {
		if (perguntaChecklistVO == null) {
			perguntaChecklistVO = new PerguntaChecklistVO();
		}
		return perguntaChecklistVO;
	}

	public void setPerguntaChecklistVO(PerguntaChecklistVO perguntaChecklistVO) {
		this.perguntaChecklistVO = perguntaChecklistVO;
	}

	public boolean isChecklist() {
		return checklist;
	}

	public void setChecklist(boolean checklist) {
		this.checklist = checklist;
	}

}
