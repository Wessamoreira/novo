package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.VagaTurmaDisciplinaVO;

public class GestaoTurmaDisciplinaRelVO extends GestaoTurmaRelVO  {

	private DisciplinaVO disciplinaVO;
	
	private GestaoTurmaRelVO gestaoTurmaRelVO;
	private List<GestaoTurmaRelVO> gestaoTurmaRelVOs;	
	
	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public List<GestaoTurmaRelVO> getGestaoTurmaRelVOs() {
		if(gestaoTurmaRelVOs == null){
			gestaoTurmaRelVOs = new ArrayList<GestaoTurmaRelVO>(0);
		}
		return gestaoTurmaRelVOs;
	}

	public void setGestaoTurmaRelVOs(List<GestaoTurmaRelVO> gestaoTurmaRelVOs) {
		this.gestaoTurmaRelVOs = gestaoTurmaRelVOs;
	}

	public GestaoTurmaRelVO getGestaoTurmaRelVO() {
		if(gestaoTurmaRelVO == null){
			gestaoTurmaRelVO = new GestaoTurmaRelVO();
		}
		return gestaoTurmaRelVO;
	}

	public void setGestaoTurmaRelVO(GestaoTurmaRelVO gestaoTurmaRelVO) {
		this.gestaoTurmaRelVO = gestaoTurmaRelVO;
	}
		
}
