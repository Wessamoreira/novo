package negocio.comuns.secretaria;

import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;

public class HistoricoGradeVO extends SuperVO {
	
	private Integer codigo;
	private TransferenciaMatrizCurricularVO transferenciaMatrizCurricularVO;
	private DisciplinaVO disciplinaVO;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private String situacao;
	private Double mediaFinal;
	private Double frequencia;
	private MatriculaPeriodoVO matriculaPeriodoApresentarHistoricoVO;
	private DisciplinaVO disciplinaEquivalenteVO;
	private Integer disciplinasAproveitadas;
	private Integer transferenciaEntradaDisciplinaAproveitada;
	private String anoHistorico;
	private String semestreHistorico;
	private String instituicao;
	private CidadeVO cidade;
	private Integer cargaHorariaAproveitamentoDisciplina;
	private Integer cargaHorariaCursada;
	private ConfiguracaoAcademicoNotaConceitoVO mediaFinalNotaConceito;	
	private static final long serialVersionUID = 1L;
	private Boolean isentarMediaFinal;
	
	public HistoricoGradeVO() {
		
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
		if (frequencia == null) {
			frequencia = 0.0;
		}
		return frequencia;
	}

	public void setFrequencia(Double frequencia) {
		this.frequencia = frequencia;
	}

	public DisciplinaVO getDisciplinaEquivalenteVO() {
		if (disciplinaEquivalenteVO == null) {
			disciplinaEquivalenteVO = new DisciplinaVO();
		}
		return disciplinaEquivalenteVO;
	}

	public void setDisciplinaEquivalenteVO(DisciplinaVO disciplinaEquivalenteVO) {
		this.disciplinaEquivalenteVO = disciplinaEquivalenteVO;
	}

	public Integer getDisciplinasAproveitadas() {
		if(disciplinasAproveitadas == null){
			disciplinasAproveitadas = 0;
		}
		return disciplinasAproveitadas;
	}

	public void setDisciplinasAproveitadas(Integer disciplinasAproveitadas) {
		this.disciplinasAproveitadas = disciplinasAproveitadas;
	}

	public Integer getTransferenciaEntradaDisciplinaAproveitada() {
		if(transferenciaEntradaDisciplinaAproveitada == null){
			transferenciaEntradaDisciplinaAproveitada = 0;
		}
		return transferenciaEntradaDisciplinaAproveitada;
	}

	public void setTransferenciaEntradaDisciplinaAproveitada(Integer transferenciaEntradaDisciplinaAproveitada) {
		this.transferenciaEntradaDisciplinaAproveitada = transferenciaEntradaDisciplinaAproveitada;
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

	public String getInstituicao() {
		if(instituicao == null){
			instituicao = "";
		}
		return instituicao;
	}

	public void setInstituicao(String instituicao) {
		this.instituicao = instituicao;
	}

	public CidadeVO getCidade() {
		if(cidade == null){
			cidade = new CidadeVO();
		}
		return cidade;
	}

	public void setCidade(CidadeVO cidade) {
		this.cidade = cidade;
	}

	public Integer getCargaHorariaAproveitamentoDisciplina() {
		if(cargaHorariaAproveitamentoDisciplina == null){
			cargaHorariaAproveitamentoDisciplina = 0;
		}
		return cargaHorariaAproveitamentoDisciplina;
	}

	public void setCargaHorariaAproveitamentoDisciplina(Integer cargaHorariaAproveitamentoDisciplina) {
		this.cargaHorariaAproveitamentoDisciplina = cargaHorariaAproveitamentoDisciplina;
	}

	public Integer getCargaHorariaCursada() {
		if(cargaHorariaCursada == null){
			cargaHorariaCursada = 0;
		}
		return cargaHorariaCursada;
	}

	public void setCargaHorariaCursada(Integer cargaHorariaCursada) {
		this.cargaHorariaCursada = cargaHorariaCursada;
	}

	public Boolean getIsentarMediaFinal() {
		if (isentarMediaFinal == null) {
			isentarMediaFinal = false;
		}
		return isentarMediaFinal;
	}

	public void setIsentarMediaFinal(Boolean isentarMediaFinal) {
		this.isentarMediaFinal = isentarMediaFinal;
	}
	
	
}
