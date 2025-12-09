package negocio.comuns.secretaria;

import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.arquitetura.SuperVO;

public class HistoricoGradeMigradaEquivalenteVO extends SuperVO {
	private Integer codigo;
	private TransferenciaMatrizCurricularVO transferenciaMatrizCurricularVO;
	private DisciplinaVO disciplinaVO;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private String situacao;
	private Double mediaFinal;
	private Double frequencia;
	private String anoHistorico;
	private String semestreHistorico;
	private ConfiguracaoAcademicoNotaConceitoVO mediaFinalNotaConceito;
	private MatriculaPeriodoVO matriculaPeriodoApresentarHistoricoVO;
	private static final long serialVersionUID = 1L;
	
	public HistoricoGradeMigradaEquivalenteVO() {
		
	}

	public TransferenciaMatrizCurricularVO getTransferenciaMatrizCurricularVO() {
		if (transferenciaMatrizCurricularVO == null) {
			transferenciaMatrizCurricularVO = new TransferenciaMatrizCurricularVO();
		}
		return transferenciaMatrizCurricularVO;
	}

	public void setTransferenciaMatrizCurricularVO(
			TransferenciaMatrizCurricularVO transferenciaMatrizCurricularVO) {
		this.transferenciaMatrizCurricularVO = transferenciaMatrizCurricularVO;
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

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Double getMediaFinal() {
		return mediaFinal;
	}

	public void setMediaFinal(Double mediaFinal) {
		this.mediaFinal = mediaFinal;
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
	
	public MatriculaPeriodoVO getMatriculaPeriodoApresentarHistoricoVO() {
		if (matriculaPeriodoApresentarHistoricoVO == null) {
			matriculaPeriodoApresentarHistoricoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoApresentarHistoricoVO;
	}

	public void setMatriculaPeriodoApresentarHistoricoVO(
			MatriculaPeriodoVO matriculaPeriodoApresentarHistoricoVO) {
		this.matriculaPeriodoApresentarHistoricoVO = matriculaPeriodoApresentarHistoricoVO;
	}

	public Double getFrequencia() {
		return frequencia;
	}

	public void setFrequencia(Double frequencia) {
		this.frequencia = frequencia;
	}

	

	public String getAnoHistorico() {
		if(anoHistorico == null){
			anoHistorico = "";
		}
		return anoHistorico;
	}

	public void setAnoHistorico(String anoHistorico) {
		this.anoHistorico = anoHistorico;
	}

	public String getSemestreHistorico() {
		if(semestreHistorico == null){
			semestreHistorico = "";
		}
		return semestreHistorico;
	}

	public void setSemestreHistorico(String semestreHistorico) {
		this.semestreHistorico = semestreHistorico;
	}

	public ConfiguracaoAcademicoNotaConceitoVO getMediaFinalNotaConceito() {
		if(mediaFinalNotaConceito == null){
			mediaFinalNotaConceito = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return mediaFinalNotaConceito;
	}

	public void setMediaFinalNotaConceito(ConfiguracaoAcademicoNotaConceitoVO mediaFinalNotaConceito) {
		this.mediaFinalNotaConceito = mediaFinalNotaConceito;
	}
	
	

}
