package negocio.comuns.secretaria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MapaRegistroEvasaoCursoMatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoTrancamentoEnum;

public class MapaRegistroEvasaoCursoVO extends SuperVO {

	private static final long serialVersionUID = 5961777122603845539L;
	private Integer codigo;
	private Date dataRegistro;
	private List<MapaRegistroEvasaoCursoMatriculaPeriodoVO> mapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO;
	private List<UnidadeEnsinoVO> unidadeEnsinoVOs;
	private List<CursoVO> cursoVOs;
	private List<TurnoVO> turnoVOs;
	private TipoTrancamentoEnum tipoTrancamentoEnum;
	private String ano;
	private String semestre;
	private String periodicidade;
	private boolean trazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte = false;
	private boolean trazerAlunoTrancadoAbandonadoAnoSemestreBase = false;
	private boolean considerarTrancamentoConsecutivo = false;
	private Integer qtdDisciplinaReprovadas;
	private Integer qtdDiasAlunosSemAcessoAva;
	private Integer qtdTrancamentoEmExcesso;
	private Integer qtdMesAlunosRenovacaoSemAcessoAva;
	private UsuarioVO usuarioResponsavel;
	private MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamento;
	private String justificativa;
	private UnidadeEnsinoVO unidadeEnsinoFiltro;
	private CursoVO cursoFiltro;
	private TurnoVO turnoFiltro;
	private MatriculaVO matriculaFiltro;
	private SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnumFiltro;
	private String nivelEducacional;
	private String anoRegistroEvasao;
	private String semestreRegistroEvasao;

	
	
	public void carregarMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO(List<MatriculaPeriodoVO> matriculaPeriodoVOs) {
		getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO().clear();
		matriculaPeriodoVOs.stream().forEach(p->{
			MapaRegistroEvasaoCursoMatriculaPeriodoVO mractmp = new MapaRegistroEvasaoCursoMatriculaPeriodoVO();
			mractmp.setMapaRegistroEvasaoCursoVO(this);
			mractmp.setMatriculaPeriodoVO(p);
			getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO().add(mractmp);
		});
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

	public Date getDataRegistro() {
		if (dataRegistro == null) {
			dataRegistro = new Date();
		}
		return dataRegistro;
	}

	public void setDataRegistro(Date dataRegistro) {
		this.dataRegistro = dataRegistro;
	}
	
	public String getDataRegistroApresentar() {
		return Uteis.getData(getDataRegistro());
	}

	public List<MapaRegistroEvasaoCursoMatriculaPeriodoVO> getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO() {
		if (mapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO == null) {
			mapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO = new ArrayList<MapaRegistroEvasaoCursoMatriculaPeriodoVO>();
		}
		return mapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO;
	}

	public void setMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO(List<MapaRegistroEvasaoCursoMatriculaPeriodoVO> mapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO) {
		this.mapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO = mapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO;
	}

	public List<UnidadeEnsinoVO> getUnidadeEnsinoVOs() {
		if (unidadeEnsinoVOs == null) {
			unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>();
		}
		return unidadeEnsinoVOs;
	}

	public void setUnidadeEnsinoVOs(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		this.unidadeEnsinoVOs = unidadeEnsinoVOs;
	}

	public List<CursoVO> getCursoVOs() {
		if (cursoVOs == null) {
			cursoVOs = new ArrayList<CursoVO>();
		}
		return cursoVOs;
	}

	public void setCursoVOs(List<CursoVO> cursoVOs) {
		this.cursoVOs = cursoVOs;
	}

	public List<TurnoVO> getTurnoVOs() {
		if (turnoVOs == null) {
			turnoVOs = new ArrayList<TurnoVO>();
		}
		return turnoVOs;
	}

	public void setTurnoVOs(List<TurnoVO> turnoVOs) {
		this.turnoVOs = turnoVOs;
	}

	public TipoTrancamentoEnum getTipoTrancamentoEnum() {
		if (tipoTrancamentoEnum == null) {
			tipoTrancamentoEnum = TipoTrancamentoEnum.NENHUM;
		}
		return tipoTrancamentoEnum;
	}

	public void setTipoTrancamentoEnum(TipoTrancamentoEnum tipoTrancamentoEnum) {
		this.tipoTrancamentoEnum = tipoTrancamentoEnum;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = PeriodicidadeEnum.SEMESTRAL.getValor();
		}
		return periodicidade;
	}

	public void setPeriodicidade(String periodicidade) {
		this.periodicidade = periodicidade;
	}

	public boolean isTrazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte() {
		return trazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte;
	}

	public void setTrazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte(boolean trazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte) {
		this.trazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte = trazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte;
	}

	public boolean isTrazerAlunoTrancadoAbandonadoAnoSemestreBase() {

		return trazerAlunoTrancadoAbandonadoAnoSemestreBase;
	}

	public void setTrazerAlunoTrancadoAbandonadoAnoSemestreBase(boolean trazerAlunoTrancadoAbandonadoAnoSemestreBase) {
		this.trazerAlunoTrancadoAbandonadoAnoSemestreBase = trazerAlunoTrancadoAbandonadoAnoSemestreBase;
	}

	public boolean isConsiderarTrancamentoConsecutivo() {
		return considerarTrancamentoConsecutivo;
	}

	public void setConsiderarTrancamentoConsecutivo(boolean considerarTrancamentoConsecutivo) {
		this.considerarTrancamentoConsecutivo = considerarTrancamentoConsecutivo;
	}

	public Integer getQtdDisciplinaReprovadas() {
		if (qtdDisciplinaReprovadas == null) {
			qtdDisciplinaReprovadas = 0;
		}
		return qtdDisciplinaReprovadas;
	}

	public void setQtdDisciplinaReprovadas(Integer qtdDisciplinaReprovadas) {
		this.qtdDisciplinaReprovadas = qtdDisciplinaReprovadas;
	}

	public Integer getQtdDiasAlunosSemAcessoAva() {
		if (qtdDiasAlunosSemAcessoAva == null) {
			qtdDiasAlunosSemAcessoAva = 0;
		}
		return qtdDiasAlunosSemAcessoAva;
	}

	public void setQtdDiasAlunosSemAcessoAva(Integer qtdDiasAlunosSemAcessoAva) {
		this.qtdDiasAlunosSemAcessoAva = qtdDiasAlunosSemAcessoAva;
	}

	public Integer getQtdTrancamentoEmExcesso() {
		if (qtdTrancamentoEmExcesso == null) {
			qtdTrancamentoEmExcesso = 0;
		}
		return qtdTrancamentoEmExcesso;
	}

	public void setQtdTrancamentoEmExcesso(Integer qtdTrancamentoEmExcesso) {
		this.qtdTrancamentoEmExcesso = qtdTrancamentoEmExcesso;
	}

	public Integer getQtdMesAlunosRenovacaoSemAcessoAva() {
		if (qtdMesAlunosRenovacaoSemAcessoAva == null) {
			qtdMesAlunosRenovacaoSemAcessoAva = 0;
		}
		return qtdMesAlunosRenovacaoSemAcessoAva;
	}

	public void setQtdMesAlunosRenovacaoSemAcessoAva(Integer qtdMesAlunosRenovacaoSemAcessoAva) {
		this.qtdMesAlunosRenovacaoSemAcessoAva = qtdMesAlunosRenovacaoSemAcessoAva;
	}

	public UsuarioVO getUsuarioResponsavel() {
		if (usuarioResponsavel == null) {
			usuarioResponsavel = new UsuarioVO();
		}
		return usuarioResponsavel;
	}

	public void setUsuarioResponsavel(UsuarioVO usuarioResponsavel) {
		this.usuarioResponsavel = usuarioResponsavel;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoFiltro() {
		if (unidadeEnsinoFiltro == null) {
			unidadeEnsinoFiltro = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoFiltro;
	}

	public void setUnidadeEnsinoFiltro(UnidadeEnsinoVO unidadeEnsinoFiltro) {
		this.unidadeEnsinoFiltro = unidadeEnsinoFiltro;
	}

	public CursoVO getCursoFiltro() {
		if (cursoFiltro == null) {
			cursoFiltro = new CursoVO();
		}
		return cursoFiltro;
	}

	public void setCursoFiltro(CursoVO cursoFiltro) {
		this.cursoFiltro = cursoFiltro;
	}

	public TurnoVO getTurnoFiltro() {
		if (turnoFiltro == null) {
			turnoFiltro = new TurnoVO();
		}
		return turnoFiltro;
	}

	public void setTurnoFiltro(TurnoVO turnoFiltro) {
		this.turnoFiltro = turnoFiltro;
	}

	public MatriculaVO getMatriculaFiltro() {
		if (matriculaFiltro == null) {
			matriculaFiltro = new MatriculaVO();
		}
		return matriculaFiltro;
	}

	public void setMatriculaFiltro(MatriculaVO matriculaFiltro) {
		this.matriculaFiltro = matriculaFiltro;
	}
	
	
	
	public SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum getSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnumFiltro() {
		if (situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnumFiltro == null) {
			situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnumFiltro = SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.NENHUM;
		}
		return situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnumFiltro;
	}

	public void setSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnumFiltro(SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnumFiltro) {
		this.situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnumFiltro = situacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnumFiltro;
	}

	public Long getQuantidadeAlunosSelecionados() {
		return getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO().isEmpty() ? 0L : (getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO().stream().filter(p-> p.getMatriculaPeriodoVO().getMatriculaVO().getAlunoSelecionado()
				&& p.getSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum().equals(SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.AGUARDANDO_PROCESSAMENTO)).count());		
	}

	public MotivoCancelamentoTrancamentoVO getMotivoCancelamentoTrancamento() {
		if (motivoCancelamentoTrancamento == null) {
			motivoCancelamentoTrancamento = new MotivoCancelamentoTrancamentoVO();
		}
		return motivoCancelamentoTrancamento;
	}

	public void setMotivoCancelamentoTrancamento(MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamento) {
		this.motivoCancelamentoTrancamento = motivoCancelamentoTrancamento;
	}

	public String getJustificativa() {
		if (justificativa == null) {
			justificativa = "";
		}
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
	
	public Long getQuantidadeRegistroParaEstorno() {
		return getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO().isEmpty() ? 0L : (getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO().stream().filter(p-> p.getSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum().isProcessado()).count());		
	}
	
	public boolean isApresentarBotaoEstornoLote() {
		return Uteis.isAtributoPreenchido(getCodigo()) &&  getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO().stream().anyMatch(p-> p.isApresentarEstorno());
	}
	
	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public Boolean getVerficarQuantidadeAlunosProcessados() {
		return (getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO().stream().anyMatch(p ->p.getSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum().equals(
				SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.AGUARDANDO_PROCESSAMENTO)));
	}
	
	public String getAnoRegistroEvasao() {
		if (anoRegistroEvasao == null) {
			anoRegistroEvasao = Constantes.EMPTY;
		}
		return anoRegistroEvasao;
	}
	
	public void setAnoRegistroEvasao(String anoRegistroEvasao) {
		this.anoRegistroEvasao = anoRegistroEvasao;
	}
	
	public String getSemestreRegistroEvasao() {
		if (semestreRegistroEvasao == null) {
			semestreRegistroEvasao = Constantes.EMPTY;
		}
		return semestreRegistroEvasao;
	}
	
	public void setSemestreRegistroEvasao(String semestreRegistroEvasao) {
		this.semestreRegistroEvasao = semestreRegistroEvasao;
	}
}
