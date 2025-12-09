package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

public class TurmaDisciplinaInclusaoSugeridaVO extends SuperVO {

	private Integer codigo;
	private TurmaVO turmaVO;
	private TurmaDisciplinaVO turmaDisciplinaVO;
	private static final long serialVersionUID = 1L;

	public TurmaDisciplinaVO getTurmaDisciplinaVO() {
		if (turmaDisciplinaVO == null) {
			turmaDisciplinaVO = new TurmaDisciplinaVO();
		}
		return turmaDisciplinaVO;
	}

	public void setTurmaDisciplinaVO(TurmaDisciplinaVO turmaDisciplinaVO) {
		this.turmaDisciplinaVO = turmaDisciplinaVO;
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

}
