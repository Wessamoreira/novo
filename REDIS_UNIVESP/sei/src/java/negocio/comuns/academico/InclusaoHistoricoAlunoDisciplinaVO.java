package negocio.comuns.academico;

import negocio.comuns.arquitetura.SuperVO;

public class InclusaoHistoricoAlunoDisciplinaVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4941322009799800012L;
	private Integer codigo;
	private InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO;	
	private HistoricoVO historicoVO;	
	private MatriculaVO matriculaAproveitarDisciplinaVO;
	private MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO;
	/**
	 * Transiente
	 */
	private Boolean incluir;

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public InclusaoHistoricoAlunoVO getInclusaoHistoricoAlunoVO() {
		if(inclusaoHistoricoAlunoVO == null){
			inclusaoHistoricoAlunoVO = new InclusaoHistoricoAlunoVO();
		}
		return inclusaoHistoricoAlunoVO;
	}

	public void setInclusaoHistoricoAlunoVO(InclusaoHistoricoAlunoVO inclusaoHistoricoAlunoVO) {
		this.inclusaoHistoricoAlunoVO = inclusaoHistoricoAlunoVO;
	}

	public HistoricoVO getHistoricoVO() {
		if(historicoVO == null){
			historicoVO = new HistoricoVO();
		}
		return historicoVO;
	}

	public void setHistoricoVO(HistoricoVO historicoVO) {
		this.historicoVO = historicoVO;
	}

	public Boolean getIncluir() {
		if(incluir == null){
			incluir = false;
		}
		return incluir;
	}

	public void setIncluir(Boolean incluir) {
		this.incluir = incluir;
	}

	public MatriculaVO getMatriculaAproveitarDisciplinaVO() {
		if(matriculaAproveitarDisciplinaVO == null){
			matriculaAproveitarDisciplinaVO = new MatriculaVO();
		}
		return matriculaAproveitarDisciplinaVO;
	}

	public void setMatriculaAproveitarDisciplinaVO(MatriculaVO matriculaAproveitarDisciplinaVO) {
		this.matriculaAproveitarDisciplinaVO = matriculaAproveitarDisciplinaVO;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (historicoVO.getDisciplina().getCodigo().hashCode());
		result = prime * result + (inclusaoHistoricoAlunoVO.getMatriculaVO().getMatricula().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InclusaoHistoricoAlunoDisciplinaVO other = (InclusaoHistoricoAlunoDisciplinaVO) obj;
		if (historicoVO == null) {
			if (other.historicoVO != null)
				return false;
		} else if (!(historicoVO.getDisciplina().getCodigo().equals(other.historicoVO.getDisciplina().getCodigo()) 
				&& historicoVO.getAnoHistorico().equals(other.historicoVO.getAnoHistorico())
				&& historicoVO.getSemestreHistorico().equals(other.historicoVO.getSemestreHistorico())))
			return false;
			
		if (inclusaoHistoricoAlunoVO == null) {
			if (other.inclusaoHistoricoAlunoVO != null)
				return false;
		} else if (!inclusaoHistoricoAlunoVO.getMatriculaVO().getMatricula().equals(other.inclusaoHistoricoAlunoVO.getMatriculaVO().getMatricula()))
			return false;
		return true;
	}

	public MapaEquivalenciaDisciplinaVO getMapaEquivalenciaDisciplinaVO() {
		if(mapaEquivalenciaDisciplinaVO == null){
			mapaEquivalenciaDisciplinaVO = new MapaEquivalenciaDisciplinaVO();
		}		
		return mapaEquivalenciaDisciplinaVO;
	}

	public void setMapaEquivalenciaDisciplinaVO(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) {
		this.mapaEquivalenciaDisciplinaVO = mapaEquivalenciaDisciplinaVO;
	}
	
	

}
