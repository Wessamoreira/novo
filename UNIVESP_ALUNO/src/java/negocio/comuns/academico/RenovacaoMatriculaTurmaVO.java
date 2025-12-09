package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.enumeradores.SituacaoRenovacaoTurmaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;

public class RenovacaoMatriculaTurmaVO extends ProgressBarVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private CursoVO cursoVO;
	private TurmaVO turmaVO;
	private UsuarioVO responsavel;
	private List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> renovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs;
	private List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> renovacaoMatriculaTurmaMatriculaPeriodoSucessoVOs;
	private List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> renovacaoMatriculaTurmaMatriculaPeriodoErroVOs;
	private Date data;
	private Date dataRenovacao;
	private String ano;
	private String semestre;
	private String aprovadoSemestre;
	private Boolean renovarApenasAlunosRenovacaoAutomatica;
	private boolean liberadoInclusaoTurmaOutroUnidadeEnsino = false;
	private boolean liberadoInclusaoTurmaOutroCurso = false ;
	private boolean liberadoInclusaoTurmaOutroMatrizCurricular = true;
	private Date dataInicioProcessamento;
	private Date dataTerminoProcessamento;

	private GradeCurricularVO gradeCurricularAtual;
//	private PlanoFinanceiroCursoVO planoFinanceiroCursoAtual;
//	private CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoAtual;
	
	private ProcessoMatriculaVO processoMatriculaRenovar;
	private PeriodoLetivoVO periodoLetivoRenovar;
	private TurmaVO turmaRenovar;
//	private PlanoFinanceiroCursoVO planoFinanceiroCursoRenovar;
//	private CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoRenovar;
//	private Boolean manterCondicaoPagamentoAtual;
	
	private Map<String, ProcessoMatriculaVO> mapaProcessoMatriculaRenovar;
	private Map<String, PeriodoLetivoVO> mapaPeriodoLetivo;
	private Map<String, TurmaVO> mapaTurmaRenovar;
//	private Map<String, CondicaoPagamentoPlanoFinanceiroCursoVO> mapaCondicaoPagamentoPlanoFinanceiroCursoVO;
	private Map<String, ProcessoMatriculaCalendarioVO> mapaProcessoMatriculaCalendarioVO;
	
	private SituacaoRenovacaoTurmaEnum situacao;
	private Integer qtdeRenovacaoAGerada;
	private Integer qtdeRenovacaoGerada;
	private Integer qtdeRenovacaoErro;
	private String mensagemErro;

	/**
	 * Transiente
	 * 
	 * @return
	 */
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO;
	private List<RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO> renovacaoMatriculaTurmaGradeDisciplinaCompostaVOs;
	

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public String getAno() {
		if (ano == null) {
			ano = Uteis.getAnoDataAtual();
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = Uteis.getSemestreAtual();
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getAprovadoSemestre() {
		if (aprovadoSemestre == null) {
			aprovadoSemestre = "";
		}
		return aprovadoSemestre;
	}

	public void setAprovadoSemestre(String aprovadoSemestre) {
		this.aprovadoSemestre = aprovadoSemestre;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getData_Apresentar() {
		if (Uteis.isAtributoPreenchido(getData())) {
			return Uteis.getDataComHora(data);
		}
		return "";
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

	public List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> getRenovacaoMatriculaTurmaMatriculaPeriodoSucessoVOs() {
		if (renovacaoMatriculaTurmaMatriculaPeriodoSucessoVOs == null) {
			renovacaoMatriculaTurmaMatriculaPeriodoSucessoVOs = new ArrayList<RenovacaoMatriculaTurmaMatriculaPeriodoVO>(0);
		}
		return renovacaoMatriculaTurmaMatriculaPeriodoSucessoVOs;
	}

	public void setRenovacaoMatriculaTurmaMatriculaPeriodoSucessoVOs(List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> renovacaoMatriculaTurmaMatriculaPeriodoSucessoVOs) {
		this.renovacaoMatriculaTurmaMatriculaPeriodoSucessoVOs = renovacaoMatriculaTurmaMatriculaPeriodoSucessoVOs;
	}

	public Date getDataInicioProcessamento() {
		return dataInicioProcessamento;
	}

	public void setDataInicioProcessamento(Date dataInicioProcessamento) {
		this.dataInicioProcessamento = dataInicioProcessamento;
	}

	public Date getDataTerminoProcessamento() {
		return dataTerminoProcessamento;
	}

	public void setDataTerminoProcessamento(Date dataTerminoProcessamento) {
		this.dataTerminoProcessamento = dataTerminoProcessamento;
	}

//	public PlanoFinanceiroCursoVO getPlanoFinanceiroCursoAtual() {
//		if (planoFinanceiroCursoAtual == null) {
//			planoFinanceiroCursoAtual = new PlanoFinanceiroCursoVO();
//		}
//		return planoFinanceiroCursoAtual;
//	}
//
//	public void setPlanoFinanceiroCursoAtual(PlanoFinanceiroCursoVO planoFinanceiroCursoAtual) {
//		this.planoFinanceiroCursoAtual = planoFinanceiroCursoAtual;
//	}
//
//	public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCursoAtual() {
//		if (condicaoPagamentoPlanoFinanceiroCursoAtual == null) {
//			condicaoPagamentoPlanoFinanceiroCursoAtual = new CondicaoPagamentoPlanoFinanceiroCursoVO();
//		}
//		return condicaoPagamentoPlanoFinanceiroCursoAtual;
//	}
//
//	public void setCondicaoPagamentoPlanoFinanceiroCursoAtual(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoAtual) {
//		this.condicaoPagamentoPlanoFinanceiroCursoAtual = condicaoPagamentoPlanoFinanceiroCursoAtual;
//	}

	public TurmaVO getTurmaRenovar() {
		if (turmaRenovar == null) {
			turmaRenovar = new TurmaVO();
		}
		return turmaRenovar;
	}

	public void setTurmaRenovar(TurmaVO turmaRenovar) {
		this.turmaRenovar = turmaRenovar;
	}

	public ProcessoMatriculaVO getProcessoMatriculaRenovar() {
		if (processoMatriculaRenovar == null) {
			processoMatriculaRenovar = new ProcessoMatriculaVO();
		}
		return processoMatriculaRenovar;
	}

	public void setProcessoMatriculaRenovar(ProcessoMatriculaVO processoMatriculaRenovar) {
		this.processoMatriculaRenovar = processoMatriculaRenovar;
	}

//	public Boolean getManterCondicaoPagamentoAtual() {
//		if (manterCondicaoPagamentoAtual == null) {
//			manterCondicaoPagamentoAtual = Boolean.FALSE;
//		}
//		return manterCondicaoPagamentoAtual;
//	}
//
//	public void setManterCondicaoPagamentoAtual(Boolean manterCondicaoPagamentoAtual) {
//		this.manterCondicaoPagamentoAtual = manterCondicaoPagamentoAtual;
//	}

	public Boolean getRenovarApenasAlunosRenovacaoAutomatica() {
		if (renovarApenasAlunosRenovacaoAutomatica == null) {
			renovarApenasAlunosRenovacaoAutomatica = Boolean.TRUE;
		}
		return renovarApenasAlunosRenovacaoAutomatica;
	}

	public void setRenovarApenasAlunosRenovacaoAutomatica(Boolean renovarApenasAlunosRenovacaoAutomatica) {
		this.renovarApenasAlunosRenovacaoAutomatica = renovarApenasAlunosRenovacaoAutomatica;
	}
	

	public boolean isLiberadoInclusaoTurmaOutroUnidadeEnsino() {
		return liberadoInclusaoTurmaOutroUnidadeEnsino;
	}

	public void setLiberadoInclusaoTurmaOutroUnidadeEnsino(boolean liberadoInclusaoTurmaOutroUnidadeEnsino) {
		this.liberadoInclusaoTurmaOutroUnidadeEnsino = liberadoInclusaoTurmaOutroUnidadeEnsino;
	}

	public boolean isLiberadoInclusaoTurmaOutroCurso() {
		return liberadoInclusaoTurmaOutroCurso;
	}

	public void setLiberadoInclusaoTurmaOutroCurso(boolean liberadoInclusaoTurmaOutroCurso) {
		this.liberadoInclusaoTurmaOutroCurso = liberadoInclusaoTurmaOutroCurso;
	}

	public boolean isLiberadoInclusaoTurmaOutroMatrizCurricular() {
		return liberadoInclusaoTurmaOutroMatrizCurricular;
	}

	public void setLiberadoInclusaoTurmaOutroMatrizCurricular(boolean liberadoInclusaoTurmaOutroMatrizCurricular) {
		this.liberadoInclusaoTurmaOutroMatrizCurricular = liberadoInclusaoTurmaOutroMatrizCurricular;
	}

//	public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCursoRenovar() {
//		if (condicaoPagamentoPlanoFinanceiroCursoRenovar == null) {
//			condicaoPagamentoPlanoFinanceiroCursoRenovar = new CondicaoPagamentoPlanoFinanceiroCursoVO();
//		}
//		return condicaoPagamentoPlanoFinanceiroCursoRenovar;
//	}
//
//	public void setCondicaoPagamentoPlanoFinanceiroCursoRenovar(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoRenovar) {
//		this.condicaoPagamentoPlanoFinanceiroCursoRenovar = condicaoPagamentoPlanoFinanceiroCursoRenovar;
//	}

	public SituacaoRenovacaoTurmaEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoRenovacaoTurmaEnum.AGUARDANDO_PROCESSAMENTO;
		}
		return situacao;
	}

	public void setSituacao(SituacaoRenovacaoTurmaEnum situacao) {
		this.situacao = situacao;
	}

	public Integer getQtdeRenovacaoAGerada() {
		if (qtdeRenovacaoAGerada == null) {
			qtdeRenovacaoAGerada = 0;
		}
		return qtdeRenovacaoAGerada;
	}

	public void setQtdeRenovacaoAGerada(Integer qtdeRenovacaoAGerada) {
		this.qtdeRenovacaoAGerada = qtdeRenovacaoAGerada;
	}

	public Integer getQtdeRenovacaoGerada() {
		if (qtdeRenovacaoGerada == null) {
			qtdeRenovacaoGerada = 0;
		}
		return qtdeRenovacaoGerada;
	}

	public void setQtdeRenovacaoGerada(Integer qtdeRenovacaoGerada) {
		this.qtdeRenovacaoGerada = qtdeRenovacaoGerada;
	}

	public Integer getQtdeRenovacaoErro() {
		if (qtdeRenovacaoErro == null) {
			qtdeRenovacaoErro = 0;
		}
		return qtdeRenovacaoErro;
	}

	public void setQtdeRenovacaoErro(Integer qtdeRenovacaoErro) {
		this.qtdeRenovacaoErro = qtdeRenovacaoErro;
	}

	public String getMensagemErro() {
		if (mensagemErro == null) {
			mensagemErro = "";
		}
		return mensagemErro;
	}

	public void setMensagemErro(String mensagemErro) {
		this.mensagemErro = mensagemErro;
	}

	public List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> getRenovacaoMatriculaTurmaMatriculaPeriodoErroVOs() {
		if (renovacaoMatriculaTurmaMatriculaPeriodoErroVOs == null) {
			renovacaoMatriculaTurmaMatriculaPeriodoErroVOs = new ArrayList<RenovacaoMatriculaTurmaMatriculaPeriodoVO>(0);
		}
		return renovacaoMatriculaTurmaMatriculaPeriodoErroVOs;
	}

	public void setRenovacaoMatriculaTurmaMatriculaPeriodoErroVOs(List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> renovacaoMatriculaTurmaMatriculaPeriodoErroVOs) {
		this.renovacaoMatriculaTurmaMatriculaPeriodoErroVOs = renovacaoMatriculaTurmaMatriculaPeriodoErroVOs;
	}

	public GradeCurricularVO getGradeCurricularAtual() {
		if (gradeCurricularAtual == null) {
			gradeCurricularAtual = new GradeCurricularVO();
		}
		return gradeCurricularAtual;
	}

	public void setGradeCurricularAtual(GradeCurricularVO gradeCurricularAtual) {
		this.gradeCurricularAtual = gradeCurricularAtual;
	}

	public PeriodoLetivoVO getPeriodoLetivoRenovar() {
		if (periodoLetivoRenovar == null) {
			periodoLetivoRenovar = new PeriodoLetivoVO();
		}
		return periodoLetivoRenovar;
	}

	public void setPeriodoLetivoRenovar(PeriodoLetivoVO periodoLetivoRenovar) {
		this.periodoLetivoRenovar = periodoLetivoRenovar;
	}

	public List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> getRenovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs() {
		if (renovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs == null) {
			renovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs = new ArrayList<RenovacaoMatriculaTurmaMatriculaPeriodoVO>(0);
		}
		return renovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs;
	}

	public void setRenovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs(List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> renovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs) {
		this.renovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs = renovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs;
	}

	public Date getDataRenovacao() {
		if (dataRenovacao == null) {
			dataRenovacao = new Date();
		}
		return dataRenovacao;
	}

	public void setDataRenovacao(Date dataRenovacao) {
		this.dataRenovacao = dataRenovacao;
	}

//	public PlanoFinanceiroCursoVO getPlanoFinanceiroCursoRenovar() {
//		if (planoFinanceiroCursoRenovar == null) {
//			planoFinanceiroCursoRenovar = new PlanoFinanceiroCursoVO();
//		}
//		return planoFinanceiroCursoRenovar;
//	}
//
//	public void setPlanoFinanceiroCursoRenovar(PlanoFinanceiroCursoVO planoFinanceiroCursoRenovar) {
//		this.planoFinanceiroCursoRenovar = planoFinanceiroCursoRenovar;
//	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if (unidadeEnsinoCursoVO == null) {
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
	}

	public ProcessoMatriculaCalendarioVO getProcessoMatriculaCalendarioVO() {
		if (processoMatriculaCalendarioVO == null) {
			processoMatriculaCalendarioVO = new ProcessoMatriculaCalendarioVO();
		}
		return processoMatriculaCalendarioVO;
	}

	public void setProcessoMatriculaCalendarioVO(ProcessoMatriculaCalendarioVO processoMatriculaCalendarioVO) {
		this.processoMatriculaCalendarioVO = processoMatriculaCalendarioVO;
	}

	public Boolean getApresentarAcompanhamentoProcessamento() {
		return getSituacao().equals(SituacaoRenovacaoTurmaEnum.EM_PROCESSAMENTO);
	}

	public String getLabelProcessamento() {
		if (getSituacao().equals(SituacaoRenovacaoTurmaEnum.AGUARDANDO_PROCESSAMENTO)) {
			return "Iniciando Processamento";
		}
		if (getSituacao().equals(SituacaoRenovacaoTurmaEnum.ERRO_PROCESSAMENTO)) {
			return "Falha no Processamento";
		}
		if (getSituacao().equals(SituacaoRenovacaoTurmaEnum.PROCESSAMENTO_CONCLUIDO)) {
			return "Processamento Concluído (" + getQtdeRenovacaoGerada() + " renovações, " + getQtdeRenovacaoErro() + " erros renovação) ";
		}
		if (getSituacao().equals(SituacaoRenovacaoTurmaEnum.PROCESSAMENTO_INTERROMPIDO)) {
			return "Processamento Interrompido (" + (getQtdeRenovacaoAGerada() - (getQtdeRenovacaoGerada() + getQtdeRenovacaoErro())) + " pendências, " + getQtdeRenovacaoGerada() + " renovações, " + getQtdeRenovacaoErro() + " erros renovação) ";
		}
		return "Renovando Matrícula " + (getQtdeRenovacaoGerada() + getQtdeRenovacaoErro()) + " de " + getQtdeRenovacaoAGerada();
	}

	public Integer getQtdeProcessada() {
		return getQtdeRenovacaoGerada() + getQtdeRenovacaoErro() == 0 ? 1 : getQtdeRenovacaoGerada() + getQtdeRenovacaoErro();
	}

	public Integer getQtdeMatriculaRenovada() {
		return getRenovacaoMatriculaTurmaMatriculaPeriodoSucessoVOs().size();
	}

	public Integer getQtdeMatriculaErroRenovacao() {
		return getRenovacaoMatriculaTurmaMatriculaPeriodoErroVOs().size();
	}

	public Integer getQtdeMatriculaNaoRenovado() {
		return getRenovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs().size();
	}

	public List<RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO> getRenovacaoMatriculaTurmaGradeDisciplinaCompostaVOs() {
		if (renovacaoMatriculaTurmaGradeDisciplinaCompostaVOs == null) {
			renovacaoMatriculaTurmaGradeDisciplinaCompostaVOs = new ArrayList<RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO>(0);
		}
		return renovacaoMatriculaTurmaGradeDisciplinaCompostaVOs;
	}

	public void setRenovacaoMatriculaTurmaGradeDisciplinaCompostaVOs(List<RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO> renovacaoMatriculaTurmaGradeDisciplinaCompostaVOs) {
		this.renovacaoMatriculaTurmaGradeDisciplinaCompostaVOs = renovacaoMatriculaTurmaGradeDisciplinaCompostaVOs;
	}
	
	

	public Map<String, ProcessoMatriculaVO> getMapaProcessoMatriculaRenovar() {
		if (mapaProcessoMatriculaRenovar == null) {
			mapaProcessoMatriculaRenovar = new HashMap<>();
		}
		return mapaProcessoMatriculaRenovar;
	}

	public void setMapaProcessoMatriculaRenovar(Map<String, ProcessoMatriculaVO> mapaProcessoMatriculaRenovar) {
		this.mapaProcessoMatriculaRenovar = mapaProcessoMatriculaRenovar;
	}

	public Map<String, PeriodoLetivoVO> getMapaPeriodoLetivo() {
		if (mapaPeriodoLetivo == null) {
			mapaPeriodoLetivo = new HashMap<>();
		}
		return mapaPeriodoLetivo;
	}

	public void setMapaPeriodoLetivo(Map<String, PeriodoLetivoVO> mapaPeriodoLetivo) {
		this.mapaPeriodoLetivo = mapaPeriodoLetivo;
	}

	public Map<String, TurmaVO> getMapaTurmaRenovar() {
		if (mapaTurmaRenovar == null) {
			mapaTurmaRenovar = new HashMap<>();
		}
		return mapaTurmaRenovar;
	}

	public void setMapaTurmaRenovar(Map<String, TurmaVO> mapaTurmaRenovar) {
		this.mapaTurmaRenovar = mapaTurmaRenovar;
	}	

//	public Map<String, CondicaoPagamentoPlanoFinanceiroCursoVO> getMapaCondicaoPagamentoPlanoFinanceiroCursoVO() {
//		if (mapaCondicaoPagamentoPlanoFinanceiroCursoVO == null) {
//			mapaCondicaoPagamentoPlanoFinanceiroCursoVO = new HashMap<>();
//		}
//		return mapaCondicaoPagamentoPlanoFinanceiroCursoVO;
//	}
//
//	public void setMapaCondicaoPagamentoPlanoFinanceiroCursoVO(Map<String, CondicaoPagamentoPlanoFinanceiroCursoVO> mapaCondicaoPagamentoPlanoFinanceiroCursoVO) {
//		this.mapaCondicaoPagamentoPlanoFinanceiroCursoVO = mapaCondicaoPagamentoPlanoFinanceiroCursoVO;
//	}

	public Map<String, ProcessoMatriculaCalendarioVO> getMapaProcessoMatriculaCalendarioVO() {
		if (mapaProcessoMatriculaCalendarioVO == null) {
			mapaProcessoMatriculaCalendarioVO = new HashMap<>();
		}
		return mapaProcessoMatriculaCalendarioVO;
	}

	public void setMapaProcessoMatriculaCalendarioVO(Map<String, ProcessoMatriculaCalendarioVO> mapaProcessoMatriculaCalendarioVO) {
		this.mapaProcessoMatriculaCalendarioVO = mapaProcessoMatriculaCalendarioVO;
	}
	

}
