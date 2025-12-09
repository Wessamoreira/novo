package negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class InclusaoDisciplinaForaGradeVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private MatriculaVO matriculaVO;
	private List<DisciplinaForaGradeVO> disciplinaForaGradeVOs;
	
	public InclusaoDisciplinaForaGradeVO() {
		
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

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public List<DisciplinaForaGradeVO> getDisciplinaForaGradeVOs() {
		if (disciplinaForaGradeVOs == null) {
			disciplinaForaGradeVOs = new ArrayList<DisciplinaForaGradeVO>(0);
		}
		return disciplinaForaGradeVOs;
	}

	public void setDisciplinaForaGradeVOs(List<DisciplinaForaGradeVO> disciplinaForaGradeVOs) {
		this.disciplinaForaGradeVOs = disciplinaForaGradeVOs;
	}

}
