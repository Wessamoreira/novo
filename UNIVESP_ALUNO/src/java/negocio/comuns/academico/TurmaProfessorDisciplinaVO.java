package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.basico.PessoaVO;

public class TurmaProfessorDisciplinaVO {
	
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private PessoaVO professorVO;
//	private SalaLocalAulaVO sala;
//	private List<HorarioTurmaDiaItemVO> horarioTurmaDiaItemVOs;
	
	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}
	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
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
	public PessoaVO getProfessorVO() {
		if (professorVO == null) {
			professorVO = new PessoaVO();
		}
		return professorVO;
	}
	public void setProfessorVO(PessoaVO professorVO) {
		this.professorVO = professorVO;
	}

	
	
}
