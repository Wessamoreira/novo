package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.enumeradores.TipoEstatisticaTurmaDisciplinaEnum;

public class TurmaDisciplinaEstatisticaAlunoVO {

	private String ano;
	private String semestre;
	private Integer qtdeAluno;
	private TurmaDisciplinaVO turmaDisciplinaVO;
	private TurmaDisciplinaCompostaVO turmaDisciplinaCompostaVO;
	private TipoEstatisticaTurmaDisciplinaEnum tipoEstatisticaTurmaDisciplinaEnum;
	private List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs;		
	
	public TurmaDisciplinaEstatisticaAlunoVO(TipoEstatisticaTurmaDisciplinaEnum tipoEstatisticaTurmaDisciplinaEnum) {
		super();
		this.tipoEstatisticaTurmaDisciplinaEnum = tipoEstatisticaTurmaDisciplinaEnum;
	}
	
	public String getAno() {
		if(ano == null){
			ano = "";
		}
		return ano;
	}
	public void setAno(String ano) {
		this.ano = ano;
	}
	public String getSemestre() {
		if(semestre == null){
			semestre = "";
		}
		return semestre;
	}
	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	public Integer getQtdeAluno() {
		if(qtdeAluno == null){
			qtdeAluno = 0;
		}
		return qtdeAluno;
	}
	public void setQtdeAluno(Integer qtdeAluno) {
		this.qtdeAluno = qtdeAluno;
	}
	public TurmaDisciplinaVO getTurmaDisciplinaVO() {
		if(turmaDisciplinaVO == null){
			turmaDisciplinaVO = new TurmaDisciplinaVO();
		}
		return turmaDisciplinaVO;
	}
	public void setTurmaDisciplinaVO(TurmaDisciplinaVO turmaDisciplinaVO) {
		this.turmaDisciplinaVO = turmaDisciplinaVO;
	}
	public List<MatriculaPeriodoTurmaDisciplinaVO> getMatriculaPeriodoTurmaDisciplinaVOs() {
		if(matriculaPeriodoTurmaDisciplinaVOs == null){
			matriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return matriculaPeriodoTurmaDisciplinaVOs;
	}
	public void setMatriculaPeriodoTurmaDisciplinaVOs(
			List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) {
		this.matriculaPeriodoTurmaDisciplinaVOs = matriculaPeriodoTurmaDisciplinaVOs;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ano == null) ? 0 : ano.hashCode());
		result = prime * result + ((semestre == null) ? 0 : semestre.hashCode());
		result = prime * result + ((turmaDisciplinaVO == null) ? 0 : turmaDisciplinaVO.getDisciplina().getCodigo().hashCode());
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
		TurmaDisciplinaEstatisticaAlunoVO other = (TurmaDisciplinaEstatisticaAlunoVO) obj;
		if (ano == null) {
			if (other.ano != null)
				return false;
		} else if (!ano.equals(other.ano))
			return false;
		if (semestre == null) {
			if (other.semestre != null)
				return false;
		} else if (!semestre.equals(other.semestre))
			return false;
		if (turmaDisciplinaVO == null) {
			if (other.turmaDisciplinaVO != null)
				return false;
		}else if (turmaDisciplinaCompostaVO == null) {
				if (other.turmaDisciplinaCompostaVO != null)
					return false;
		} else if ((turmaDisciplinaVO != null && !turmaDisciplinaVO.getDisciplina().getCodigo().equals(other.turmaDisciplinaVO.getDisciplina().getCodigo())) || 
				(turmaDisciplinaCompostaVO != null && !turmaDisciplinaCompostaVO.getGradeDisciplinaCompostaVO().getCodigo().equals(other.turmaDisciplinaCompostaVO.getGradeDisciplinaCompostaVO().getCodigo())))
			return false;
		return true;
	}
	
	public TurmaDisciplinaCompostaVO getTurmaDisciplinaCompostaVO() {
		if(turmaDisciplinaCompostaVO == null) {
			turmaDisciplinaCompostaVO =  new TurmaDisciplinaCompostaVO();
		}
		return turmaDisciplinaCompostaVO;
	}
	
	public void setTurmaDisciplinaCompostaVO(TurmaDisciplinaCompostaVO turmaDisciplinaCompostaVO) {
		this.turmaDisciplinaCompostaVO = turmaDisciplinaCompostaVO;
	}
	public TipoEstatisticaTurmaDisciplinaEnum getTipoEstatisticaTurmaDisciplinaEnum() {
		if(tipoEstatisticaTurmaDisciplinaEnum == null) {
			tipoEstatisticaTurmaDisciplinaEnum =  TipoEstatisticaTurmaDisciplinaEnum.MODALIDADE;
		}
		return tipoEstatisticaTurmaDisciplinaEnum;
	}
	public void setTipoEstatisticaTurmaDisciplinaEnum(
			TipoEstatisticaTurmaDisciplinaEnum tipoEstatisticaTurmaDisciplinaEnum) {
		this.tipoEstatisticaTurmaDisciplinaEnum = tipoEstatisticaTurmaDisciplinaEnum;
	}
	
	
}
