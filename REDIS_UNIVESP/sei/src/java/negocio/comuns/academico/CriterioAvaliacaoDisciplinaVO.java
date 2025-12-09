package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;

public class CriterioAvaliacaoDisciplinaVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4875085803179111673L;
	private Integer codigo;
	private CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivo;
	private DisciplinaVO disciplina;
	private List<CriterioAvaliacaoDisciplinaEixoIndicadorVO> criterioAvaliacaoDisciplinaEixoIndicadorVOs;
	
	
	private Integer ordem;
	private Integer ordemDisciplina;
	
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public CriterioAvaliacaoPeriodoLetivoVO getCriterioAvaliacaoPeriodoLetivo() {
		if (criterioAvaliacaoPeriodoLetivo == null) {
			criterioAvaliacaoPeriodoLetivo = new CriterioAvaliacaoPeriodoLetivoVO();
		}
		return criterioAvaliacaoPeriodoLetivo;
	}
	public void setCriterioAvaliacaoPeriodoLetivo(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivo) {
		this.criterioAvaliacaoPeriodoLetivo = criterioAvaliacaoPeriodoLetivo;
	}
	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}
	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}
	
	
	
	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}
	
	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	public List<CriterioAvaliacaoDisciplinaEixoIndicadorVO> getCriterioAvaliacaoDisciplinaEixoIndicadorVOs() {
		if (criterioAvaliacaoDisciplinaEixoIndicadorVOs == null) {
			criterioAvaliacaoDisciplinaEixoIndicadorVOs = new ArrayList<CriterioAvaliacaoDisciplinaEixoIndicadorVO>(0);
		}
		return criterioAvaliacaoDisciplinaEixoIndicadorVOs;
	}
	public void setCriterioAvaliacaoDisciplinaEixoIndicadorVOs(List<CriterioAvaliacaoDisciplinaEixoIndicadorVO> criterioAvaliacaoDisciplinaEixoIndicadorVOs) {
		this.criterioAvaliacaoDisciplinaEixoIndicadorVOs = criterioAvaliacaoDisciplinaEixoIndicadorVOs;
	}
	
	
	public Integer getQtdeEixo(){
		return getCriterioAvaliacaoDisciplinaEixoIndicadorVOs().size();
	}
	
	public Integer getOrdemDisciplina() {
		if (ordemDisciplina == null) {
			ordemDisciplina = 0;
		}
		return ordemDisciplina;
	}
	
	public void setOrdemDisciplina(Integer ordemDisciplina) {
		this.ordemDisciplina = ordemDisciplina;
	}
	
	
}
