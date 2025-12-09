package negocio.comuns.blackboard;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.SuperVO;

public class SugestaoFacilitadorBlackboardVO extends SuperVO {
	
	private static final long serialVersionUID = 3819202488359126563L;
	
	private DisciplinaVO disciplinaVO;
	
	private CursoVO cursoVO;
	
	private Integer quantidadeSalas;
	
	private Integer salasComFacilitadores;
	
	private Integer salasSemFacilitadores;
	
	private Integer facilitadoresDisponiveis;

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public Integer getQuantidadeSalas() {
		if (quantidadeSalas == null) {
			quantidadeSalas = 0;
		}
		return quantidadeSalas;
	}

	public void setQuantidadeSalas(Integer quantidadeSalas) {
		this.quantidadeSalas = quantidadeSalas;
	}

	public Integer getSalasComFacilitadores() {
		if (salasComFacilitadores == null) {
			salasComFacilitadores = 0;
		}
		return salasComFacilitadores;
	}

	public void setSalasComFacilitadores(Integer salasComFacilitadores) {
		this.salasComFacilitadores = salasComFacilitadores;
	}

	public Integer getSalasSemFacilitadores() {
		if (salasSemFacilitadores == null) {
			salasSemFacilitadores = 0;
		}
		return salasSemFacilitadores;
	}

	public void setSalasSemFacilitadores(Integer salasSemFacilitadores) {
		this.salasSemFacilitadores = salasSemFacilitadores;
	}

	public Integer getFacilitadoresDisponiveis() {
		if (facilitadoresDisponiveis == null) {
			facilitadoresDisponiveis = 0;
		}
		return facilitadoresDisponiveis;
	}

	public void setFacilitadoresDisponiveis(Integer facilitadoresDisponiveis) {
		this.facilitadoresDisponiveis = facilitadoresDisponiveis;
	}

}
