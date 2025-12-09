package negocio.comuns.secretaria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaMatrizCurricularVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;

public class TransferenciaMatrizCurricularVO extends SuperVO {

	private Integer codigo;
	private UnidadeEnsinoVO unidadeEnsino;
	private UnidadeEnsinoCursoVO unidadeEnsinoCurso;
	private GradeCurricularVO gradeOrigem;
	private GradeCurricularVO gradeMigrar;
	private PeriodoLetivoVO periodoLetivoInicial;
	private PeriodoLetivoVO periodoLetivoFinal;
	private String observacoes;
	private String resultadoProcessamento;
	private List<TransferenciaMatrizCurricularMatriculaVO> listaTransferenciaMatrizCurricularMatricula;
	private UsuarioVO responsavel;
	private MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaUtilizadoGradeMigrar;
	/**
	 * Se marcada esta opção então a rotina de transferência irá gerar uma Mapa
	 * de Equivalencia automáticamente para as disciplinas que o aluno esteja
	 * cursando (somente as disciplinas que ele esteja cursando), vinculando as
	 * mesmas as disciplinas correspondentes na matriz curricular origem do
	 * aluno (disciplina correspondente significa que é uma disciplina de mesmo
	 * código e de mesma carga horária). De forma, que quando o aluno terminar
	 * de cursar a disciplina no período letivo atual, a disciplina
	 * correspondente na matriz destino da migração será automaticamente
	 * "baixada". Isto pode ser importante, para evitar que as matrículas atuais
	 * dos alunos tenham que ser editadas para uma intervenção manual.
	 */
	private Boolean gerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando;
	private Date data;

	/**
	 * ATRIBUTOS TRANSIENTS PARA APRESENTAR AS ESTATISTICAS REFERENTES A
	 * TRANSFERENCIA DE MATRIZ CURRICULAR
	 */
	private Integer nrMatriculas;
	private Integer nrMatriculasMigradas;
	private Integer nrMatriculasPendentes;
	private Integer nrMatriculasComErros;
	private Integer nrMatriculasCanceladas;
	/**
	 * FIM ATRIBUTOS TRANSIENTS
	 */

	/**
	 * ATRIBUTOS TRANSIENTS UTIALIZADOS NO MOMENTO DE PROCESSAR A TRANSFERENCIA
	 * DE MATRIZ CURRICULAR
	 */
	private MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaAtivaOrigem;
	private MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaAtivaDestino;
	private ConfiguracaoAcademicoVO configuracaoAcademicoCursoTransferencia;

	/**
	 * FIM ATRIBUTOS TRANSIENTS
	 */
	private static final long serialVersionUID = 1L;
	private Boolean realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento;
	private Boolean utilizarAnoSemestreAtualDisciplinaAprovada;
	private String anoDisciplinaAprovada;
	private String semestreDisciplinaAprovada;
	
	private String observacaoHistorico;
	private List<String> listaResultadoProcessamento;

	public TransferenciaMatrizCurricularVO() {
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

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return data;
	}

	public String getData_Apresentar() {
		return Uteis.getData(getData());
	}

	public void setData(Date data) {
		this.data = data;
	}

	/**
	 * @return the unidadeEnsino
	 */
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	/**
	 * @param unidadeEnsino
	 *            the unidadeEnsino to set
	 */
	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	/**
	 * @return the unidadeEnsinoCursoVO
	 */
	public UnidadeEnsinoCursoVO getUnidadeEnsinoCurso() {
		if (unidadeEnsinoCurso == null) {
			unidadeEnsinoCurso = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCurso;
	}

	/**
	 * @param unidadeEnsinoCursoVO
	 *            the unidadeEnsinoCursoVO to set
	 */
	public void setUnidadeEnsinoCurso(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCurso = unidadeEnsinoCursoVO;
	}

	/**
	 * @return the gradeOrigem
	 */
	public GradeCurricularVO getGradeOrigem() {
		if (gradeOrigem == null) {
			gradeOrigem = new GradeCurricularVO();
		}
		return gradeOrigem;
	}

	/**
	 * @param gradeOrigem
	 *            the gradeOrigem to set
	 */
	public void setGradeOrigem(GradeCurricularVO gradeOrigem) {
		this.gradeOrigem = gradeOrigem;
	}

	/**
	 * @return the gradeMigrar
	 */
	public GradeCurricularVO getGradeMigrar() {
		if (gradeMigrar == null) {
			gradeMigrar = new GradeCurricularVO();
		}
		return gradeMigrar;
	}

	/**
	 * @param gradeMigrar
	 *            the gradeMigrar to set
	 */
	public void setGradeMigrar(GradeCurricularVO gradeMigrar) {
		this.gradeMigrar = gradeMigrar;
	}

	/**
	 * @return the periodoLetivoInicial
	 */
	public PeriodoLetivoVO getPeriodoLetivoInicial() {
		if (periodoLetivoInicial == null) {
			periodoLetivoInicial = new PeriodoLetivoVO();
		}
		return periodoLetivoInicial;
	}

	/**
	 * @param periodoLetivoInicial
	 *            the periodoLetivoInicial to set
	 */
	public void setPeriodoLetivoInicial(PeriodoLetivoVO periodoLetivoInicial) {
		this.periodoLetivoInicial = periodoLetivoInicial;
	}

	/**
	 * @return the periodoLetivoFinal
	 */
	public PeriodoLetivoVO getPeriodoLetivoFinal() {
		if (periodoLetivoFinal == null) {
			periodoLetivoFinal = new PeriodoLetivoVO();
		}
		return periodoLetivoFinal;
	}

	/**
	 * @param periodoLetivoFinal
	 *            the periodoLetivoFinal to set
	 */
	public void setPeriodoLetivoFinal(PeriodoLetivoVO periodoLetivoFinal) {
		this.periodoLetivoFinal = periodoLetivoFinal;
	}

	/**
	 * @return the listaTransferenciaMatrizCurricularMatricula
	 */
	public List<TransferenciaMatrizCurricularMatriculaVO> getListaTransferenciaMatrizCurricularMatricula() {
		if (listaTransferenciaMatrizCurricularMatricula == null) {
			listaTransferenciaMatrizCurricularMatricula = new ArrayList<TransferenciaMatrizCurricularMatriculaVO>();
		}
		return listaTransferenciaMatrizCurricularMatricula;
	}

	/**
	 * @param listaTransferenciaMatrizCurricularMatricula
	 *            the listaTransferenciaMatrizCurricularMatricula to set
	 */
	public void setListaTransferenciaMatrizCurricularMatricula(List<TransferenciaMatrizCurricularMatriculaVO> listaTransferenciaMatrizCurricularMatricula) {
		this.listaTransferenciaMatrizCurricularMatricula = listaTransferenciaMatrizCurricularMatricula;
	}

	/**
	 * @return the observacoes
	 */
	public String getObservacoes() {
		if (observacoes == null) {
			observacoes = "";
		}
		return observacoes;
	}

	/**
	 * @param observacoes
	 *            the observacoes to set
	 */
	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public Boolean adicionarMatriculaParaTransferenciaMatrizCurricular(MatriculaVO matricula, MatriculaPeriodoVO ultimoMatriculaPeriodoVO, Boolean validarMatriculaExistente) throws Exception {
		if (!matricula.getGradeCurricularAtual().getCodigo().equals(this.getGradeOrigem().getCodigo())) {
			throw new Exception("Este aluno não está matrícula na Matriz Curricular Origem. Matriz Curricular do Aluno: " + matricula.getGradeCurricularAtual().getCodigo() + " - " + matricula.getGradeCurricularAtual().getNome() + ". O mesmo não pode ser adicionado para esta transferência.");
		}
		for (TransferenciaMatrizCurricularMatriculaVO matriculaTrans : getListaTransferenciaMatrizCurricularMatricula()) {
			if (matriculaTrans.getMatriculaVO().getMatricula().equals(matricula.getMatricula())) {
				if (validarMatriculaExistente) {
					throw new Exception("Matrícula já adicionada para à transferência");
				} else {
					// se nao é para validar, simplesmente saímos do método
					// retornando
					// false para indicar que a matricula nao foi adicionada
					return false;
				}
			}
		}
		TransferenciaMatrizCurricularMatriculaVO novaMatriculaTrans = new TransferenciaMatrizCurricularMatriculaVO();
		novaMatriculaTrans.setMatriculaVO(matricula);
		novaMatriculaTrans.setMatriculaPeriodoUltimoPeriodoVO(ultimoMatriculaPeriodoVO);
		getListaTransferenciaMatrizCurricularMatricula().add(novaMatriculaTrans);
		return true;
	}

	/**
	 * @return the nrMatriculas
	 */
	public Integer getNrMatriculas() {
		if (nrMatriculas == null) {
			nrMatriculas = 0;
		}
		return nrMatriculas;
	}

	/**
	 * @param nrMatriculas
	 *            the nrMatriculas to set
	 */
	public void setNrMatriculas(Integer nrMatriculas) {
		this.nrMatriculas = nrMatriculas;
	}

	/**
	 * @return the nrMatriculasMigradas
	 */
	public Integer getNrMatriculasMigradas() {
		if (nrMatriculasMigradas == null) {
			nrMatriculasMigradas = 0;
		}
		return nrMatriculasMigradas;
	}

	/**
	 * @param nrMatriculasMigradas
	 *            the nrMatriculasMigradas to set
	 */
	public void setNrMatriculasMigradas(Integer nrMatriculasMigradas) {
		this.nrMatriculasMigradas = nrMatriculasMigradas;
	}

	/**
	 * @return the nrMatriculasPendentes
	 */
	public Integer getNrMatriculasPendentes() {
		if (nrMatriculasPendentes == null) {
			nrMatriculasPendentes = 0;
		}
		return nrMatriculasPendentes;
	}

	/**
	 * @param nrMatriculasPendentes
	 *            the nrMatriculasPendentes to set
	 */
	public void setNrMatriculasPendentes(Integer nrMatriculasPendentes) {
		this.nrMatriculasPendentes = nrMatriculasPendentes;
	}

	/**
	 * @return the nrMatriculasComErros
	 */
	public Integer getNrMatriculasComErros() {
		if (nrMatriculasComErros == null) {
			nrMatriculasComErros = 0;
		}
		return nrMatriculasComErros;
	}

	/**
	 * @param nrMatriculasComErros
	 *            the nrMatriculasComErros to set
	 */
	public void setNrMatriculasComErros(Integer nrMatriculasComErros) {
		this.nrMatriculasComErros = nrMatriculasComErros;
	}

	/**
	 * @return the nrMatriculasCanceladas
	 */
	public Integer getNrMatriculasCanceladas() {
		if (nrMatriculasCanceladas == null) {
			nrMatriculasCanceladas = 0;
		}
		return nrMatriculasCanceladas;
	}

	/**
	 * @param nrMatriculasCanceladas
	 *            the nrMatriculasCanceladas to set
	 */
	public void setNrMatriculasCanceladas(Integer nrMatriculasCanceladas) {
		this.nrMatriculasCanceladas = nrMatriculasCanceladas;
	}

	public boolean getPodeAlterarTransferencia() {
		if ((getNrMatriculasMigradas().intValue() > 0) || (getNrMatriculasCanceladas().intValue() > 0) || (getNrMatriculasComErros().intValue() > 0)) {
			return false;
		}
		return true;
	}

	public void atualizarEstatisticasTransferenciaMatrizCurricular() {
		nrMatriculas = this.getListaTransferenciaMatrizCurricularMatricula().size();
		nrMatriculasMigradas = 0;
		nrMatriculasComErros = 0;
		nrMatriculasCanceladas = 0;
		for (TransferenciaMatrizCurricularMatriculaVO matriculaTransf : this.getListaTransferenciaMatrizCurricularMatricula()) {
			if (matriculaTransf.getSituacao().equals("RE")) {
				nrMatriculasMigradas++;
			}
			if (matriculaTransf.getSituacao().equals("CA")) {
				nrMatriculasCanceladas++;
			}
			if (matriculaTransf.getSituacao().equals("ER")) {
				nrMatriculasComErros++;
			}
		}
		nrMatriculasPendentes = nrMatriculas - nrMatriculasMigradas;
	}

	/**
	 * @return the mapaEquivalenciaAtivaOrigem
	 */
	public MapaEquivalenciaMatrizCurricularVO getMapaEquivalenciaAtivaOrigem() {
		if (mapaEquivalenciaAtivaOrigem == null) {
			mapaEquivalenciaAtivaOrigem = new MapaEquivalenciaMatrizCurricularVO();
		}
		return mapaEquivalenciaAtivaOrigem;
	}

	/**
	 * @param mapaEquivalenciaAtivaOrigem
	 *            the mapaEquivalenciaAtivaOrigem to set
	 */
	public void setMapaEquivalenciaAtivaOrigem(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaAtivaOrigem) {
		this.mapaEquivalenciaAtivaOrigem = mapaEquivalenciaAtivaOrigem;
	}

	/**
	 * @return the mapaEquivalenciaAtivaDestino
	 */
	public MapaEquivalenciaMatrizCurricularVO getMapaEquivalenciaAtivaDestino() {
		if (mapaEquivalenciaAtivaDestino == null) {
			mapaEquivalenciaAtivaDestino = new MapaEquivalenciaMatrizCurricularVO();
		}
		return mapaEquivalenciaAtivaDestino;
	}

	/**
	 * @param mapaEquivalenciaAtivaDestino
	 *            the mapaEquivalenciaAtivaDestino to set
	 */
	public void setMapaEquivalenciaAtivaDestino(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaAtivaDestino) {
		this.mapaEquivalenciaAtivaDestino = mapaEquivalenciaAtivaDestino;
	}

	/**
	 * @return the resultadoProcessamento
	 */
	public String getResultadoProcessamento() {
		if (resultadoProcessamento == null) {
			resultadoProcessamento = "";
		}
		return resultadoProcessamento;
	}

	/**
	 * @param resultadoProcessamento
	 *            the resultadoProcessamento to set
	 */
	public void setResultadoProcessamento(String resultadoProcessamento) {
		this.resultadoProcessamento = resultadoProcessamento;
	}

	public void adicionarHistoricoResultadoProcessamento(Date data, String responsavel, String descricao) {
		String base = this.getResultadoProcessamento();
		if (base.equals("")) {
			base = base + Uteis.getDataComHora(data) + " - " + descricao + " - Usuário: " + responsavel;
		} else {
			base = "\n" + base + Uteis.getDataComHora(data) + " - " + descricao + " - Usuário: " + responsavel;
		}
		this.setResultadoProcessamento(base);
	}

	/**
	 * @return the configuracaoAcademicoCursoTransferencia
	 */
	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoCursoTransferencia() {
		if (configuracaoAcademicoCursoTransferencia == null) {
			configuracaoAcademicoCursoTransferencia = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoCursoTransferencia;
	}

	/**
	 * @param configuracaoAcademicoCursoTransferencia
	 *            the configuracaoAcademicoCursoTransferencia to set
	 */
	public void setConfiguracaoAcademicoCursoTransferencia(ConfiguracaoAcademicoVO configuracaoAcademicoCursoTransferencia) {
		this.configuracaoAcademicoCursoTransferencia = configuracaoAcademicoCursoTransferencia;
	}

	/**
	 * @return the mapaEquivalenciaUtilizadoGradeMigrar
	 */
	public MapaEquivalenciaMatrizCurricularVO getMapaEquivalenciaUtilizadoGradeMigrar() {
		if (mapaEquivalenciaUtilizadoGradeMigrar == null) {
			mapaEquivalenciaUtilizadoGradeMigrar = new MapaEquivalenciaMatrizCurricularVO();
		}
		return mapaEquivalenciaUtilizadoGradeMigrar;
	}

	public void setMapaEquivalenciaUtilizadoGradeMigrar(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaUtilizadoGradeMigrar) {
		this.mapaEquivalenciaUtilizadoGradeMigrar = mapaEquivalenciaUtilizadoGradeMigrar;
	}

	/**
	 * @return the
	 *         gerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando
	 */
	public Boolean getGerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando() {
		if (gerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando == null) {
			gerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando = Boolean.TRUE;
		}
		return gerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando;
	}

	/**
	 * @param gerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando
	 *            the
	 *            gerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando
	 *            to set
	 */
	public void setGerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando(Boolean gerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando) {
		this.gerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando = gerarEquivalenciasAutomaticasParaDisciplinasAlunoEstejaCursando;
	}

	public Boolean getRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento() {
		if (realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento == null) {
			realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento = false;
		}
		return realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento;
	}

	public void setRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento(Boolean realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento) {
		this.realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento = realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento;
	}

	public Boolean getUtilizarAnoSemestreAtualDisciplinaAprovada() {
		if (utilizarAnoSemestreAtualDisciplinaAprovada == null) {
			utilizarAnoSemestreAtualDisciplinaAprovada = false;
		}
		return utilizarAnoSemestreAtualDisciplinaAprovada;
	}

	public void setUtilizarAnoSemestreAtualDisciplinaAprovada(Boolean utilizarAnoSemestreAtualDisciplinaAprovada) {
		this.utilizarAnoSemestreAtualDisciplinaAprovada = utilizarAnoSemestreAtualDisciplinaAprovada;
	}

	public String getAnoDisciplinaAprovada() {
		if (anoDisciplinaAprovada == null) {
			anoDisciplinaAprovada = Uteis.getAnoDataAtual4Digitos();
		}
		return anoDisciplinaAprovada;
	}

	public void setAnoDisciplinaAprovada(String anoDisciplinaAprovada) {
		this.anoDisciplinaAprovada = anoDisciplinaAprovada;
	}

	public String getSemestreDisciplinaAprovada() {
		if (semestreDisciplinaAprovada == null) {
			semestreDisciplinaAprovada = Uteis.getSemestreAtual();
		}
		return semestreDisciplinaAprovada;
	}

	public void setSemestreDisciplinaAprovada(String semestreDisciplinaAprovada) {
		this.semestreDisciplinaAprovada = semestreDisciplinaAprovada;
	}

	public String getObservacaoHistorico() {
		if (observacaoHistorico == null) {
			observacaoHistorico = "";
		}
		return observacaoHistorico;
	}

	public void setObservacaoHistorico(String observacaoHistorico) {
		this.observacaoHistorico = observacaoHistorico;
	}

	public List<String> getListaResultadoProcessamento() {
		if (listaResultadoProcessamento == null) {
			listaResultadoProcessamento = new ArrayList<>();
		}
		return listaResultadoProcessamento;
	}

	public void setListaResultadoProcessamento(List<String> listaResultadoProcessamento) {
		this.listaResultadoProcessamento = listaResultadoProcessamento;
	}
}
