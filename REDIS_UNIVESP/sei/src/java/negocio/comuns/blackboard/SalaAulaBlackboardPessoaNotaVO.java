package negocio.comuns.blackboard;

import negocio.comuns.arquitetura.SuperVO;

public class SalaAulaBlackboardPessoaNotaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9123624657154997484L;
	private Double nota;
	private SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO;

	public Double getNota() {
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
	}	

	public SalaAulaBlackboardPessoaVO getSalaAulaBlackboardPessoaVO() {
		if (salaAulaBlackboardPessoaVO == null) {
			salaAulaBlackboardPessoaVO = new SalaAulaBlackboardPessoaVO();
		}
		return salaAulaBlackboardPessoaVO;
	}

	public void setSalaAulaBlackboardPessoaVO(SalaAulaBlackboardPessoaVO salaAulaBlackboardPessoaVO) {
		this.salaAulaBlackboardPessoaVO = salaAulaBlackboardPessoaVO;
	}

}
