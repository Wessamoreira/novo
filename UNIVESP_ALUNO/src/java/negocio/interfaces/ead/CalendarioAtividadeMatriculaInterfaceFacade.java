package negocio.interfaces.ead;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.CalendarioLancamentoNotaVO;
import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.AvaliacaoOnlineVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.ConfiguracaoEADVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeEnum;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.ead.enumeradores.TipoNivelProgramacaoTutoriaEnum;
import negocio.comuns.ead.enumeradores.TipoOrigemEnum;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import webservice.servicos.objetos.DataEventosRSVO;

/*
 * @author Victor Hugo 02/10/2014
 */
public interface CalendarioAtividadeMatriculaInterfaceFacade {

	void incluir(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void validarDados(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO) throws Exception;

	void persistir(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void alterar(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	CalendarioAtividadeMatriculaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<CalendarioAtividadeMatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	CalendarioAtividadeMatriculaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<CalendarioAtividadeMatriculaVO> consultarCalendarioAtividadeMatriculaVisaoAluno(Integer codigoMatriculaPeriodoTurmaDisciplina, UsuarioVO usuario) throws Exception;

	void realizarCriacaoCalendarioAtividadeMatriculaAvaliacaoOnline(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO,  CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuarioVO) throws Exception;

	void consultarCalendarioAtividadeMatriculasOndeSituacaoAvaliacaoOnlineMatriculaEmRealizacao() throws Exception;

	List<CalendarioAtividadeMatriculaVO> consultarCalendarioAtividadeMatriculaTelaGestaoAvaliacaoOnline(String matricula, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, String ano, String semestre, 
			Date datainicio1, Date dataInicio2, String situacaoAvaliacaoOnline, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatriculaEnum, String tituloRea, UsuarioVO usuario) throws Exception;

	CalendarioAtividadeMatriculaVO consultarPorCodigoMatriculaPeriodoTurmaDisciplinaTipoCalendarioAtividade(Integer codigoMatriculaPeriodoTurmaDisciplina, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void verificarExistenciaCalendariosAtividadesMatriculas(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean forcarLiberacao, UsuarioVO usuarioVO) throws Exception;

	Boolean consultarPorCodigoMatriculaPeriodo(Integer codigoMatriculaPeriodo, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, UsuarioVO usuarioLogado) throws Exception;

	void realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAvaliacaoOnline(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Double totalPontoAtingido, Double porcentagemConteudo, ConfiguracaoEADVO configuracaoEADVO, UsuarioVO usuarioLogado) throws Exception;

	CalendarioAtividadeMatriculaVO consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividade(Integer codigoMatriculaPeriodoTurmaDisciplina, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, TipoOrigemEnum tipoOrigem, String codOrigem, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	void alterarSituacaoCalendarioAtividadeMatriculaQuandoPrazoEncerrado() throws Exception;

	List<CalendarioAtividadeMatriculaVO> consultarCalendariosDoDia(Integer codigoMatriculaPeriodoTurmaDisciplina, Integer codigoMatriculaPeriodo, Date data) throws Exception;

	void verificarRealizacaoProximaDisciplinaAprovadoAvaliacaoOnline(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Date dataRealizacaoAvaliacao, ConfiguracaoEADVO configuracaoEADVO, UsuarioVO usuarioLogado) throws Exception;

	List<CalendarioAtividadeMatriculaVO> consultarPorCodigoMatriculaPerido(Integer codigoMatriculaPeriodo, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	Integer consultarQuantidadeCalendariosPeriodoRealizacaoAvaliacaoOnlineNaoConcluidosMatriculaPeriodoTurmaDisciplina(Integer codigoMatriculaPeriodoTurmaDisciplinaVO);

	Boolean consultarPorCodigoMatriculaPeriodoTurmaDisciplinaOuMatriculaPeriodoETipoCalendarioAtividadeCalendarioExpirado(Integer codigoMatriculaPeriodoTurmaDisciplina, Integer codigoMatriculaPeriodo, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, Boolean consultarPorMatriculaPeriodo, UsuarioVO usuarioLogado) throws Exception;

	void executarAntecipacaoOuProrrogacaoCalendarioAtividadeMatricula(List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs, String tipoOperacao, String tipoDataAlteracao, String tipoContagem, Integer nrDias, boolean movimentarPeriodoAutomaticamente, UnidadeEnsinoVO unidadeEnsinoLogado, Date dataInicio, Date dataTermino, UsuarioVO usuarioVO) throws Exception;

	List<CalendarioAtividadeMatriculaVO> consultarCalendarioAtividadeMatricula(String matricula, Integer curso, TurmaVO turmaVO, Integer disciplina, String ano, String semestre, Boolean realizado, Date datainicio, Date datatermino, String situacaoAtividade, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividade, String tipoFiltroPeriodo, String tipoFiltroConsulta, String tituloRea, Integer codigoAtividadeDiscursiva, UsuarioVO usuario) throws Exception;

	List<CalendarioAtividadeMatriculaVO> consultarAlunosDoTutorPorTurmaCursoDisciplinaUnidadeEnsinoMonitoramentoEAD(TipoNivelProgramacaoTutoriaEnum tipoNivelProgramacaoTutoria,  TurmaVO turma, Integer codigoUnidadeEnsino, DisciplinaVO disciplina, Integer codigoCurso, Integer codigoProfessor, Boolean estudando, Boolean concluiram, boolean validarAno, String ano, String semestre, Integer codigoTemaAssunto, Integer codigoItemParametro, PeriodicidadeEnum periodicidade, Date dataInicio, Date dataFim, FiltroRelatorioAcademicoVO filtroAcademicoVO, MatriculaVO matriculaVO, Boolean situacaoAvaliacaoOnlineAprovado, Boolean situacaoAvaliacaoOnlineReprovado,  Boolean situacaoAvaliacaoOnlineAguardandoExecucao, Boolean situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado, Boolean situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada, Integer codigoAvaliacaoOnlineRea , Boolean situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor, Boolean situacaoAtividadeDiscursivaAguardandoRespostaAluno, Boolean situacaoAtividadeDiscursivaAvaliada, Integer codigoAtividadeDiscursiva , Boolean situacaoDuvidaTutorAguardandoRespostaProfessor, Boolean situacaoDuvidaTutorAguardandoRespostaAluno, Integer codigoTutor, Double percentualInicio, Double percentualFim, Integer limit, Integer offSet, UsuarioVO usuarioVO) throws Exception;

	Boolean verificarExistenciaPorCodigoMatriculaPeriodoTurmaDisciplinaTipoCalendarioAtividade(
			Integer codigoMatriculaPeriodoTurmaDisciplina,
			TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, 
			TipoOrigemEnum tipoOrigem, String codigoOrigem, int nivelMontarDados,
			UsuarioVO usuarioLogado) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 10 de nov de 2016 
	 * @param matricula
	 * @return
	 * @throws Exception 
	 */
	List<DataEventosRSVO> consultarCalendariosEADAplicativo(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) throws Exception;

	/*Boolean consultarPorCodigoMatriculaPeriodoPorTipoCalendarioAtividadeEnumPorCodigoOrigem(Integer codigoMatriculaPeriodo, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, String codigoOrigem, UsuarioVO usuarioLogado) throws Exception;*/

	/*CalendarioAtividadeMatriculaVO consultarPorCodigoMatriculaPeriodoTurmaDisciplinaPorTipoCalendarioAtividadePorCodigoOrigem(Integer codigoMatriculaPeriodoTurmaDisciplina, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, String codigoOrigem, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;*/

	List<CalendarioAtividadeMatriculaVO> realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAvaliacaoOnlineRea(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, ConfiguracaoEADVO configuracaoEADVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional, UsuarioVO usuarioLogado) throws Exception ;
	
	void realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAvaliacaoOnlinePorTurma(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO,  AvaliacaoOnlineVO avaliacaoOnline, boolean forcarAlteracao, UsuarioVO usuarioLogado) throws Exception; 

	void excluirCalendarioTipoAvaliacaoOnlineReaPorConteudoUnidadePaginaRecursoEducacional(
			ConteudoUnidadePaginaRecursoEducacionalVO obj, boolean verificarAcesso, UsuarioVO usuarioVO)
			throws Exception;
	public void exluirPorAvaliacaoOnlineTipoUsoTurma(String codOrigem, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	void alterarCalendarioAtividadeMatriculaAndExlcuirAvaliacaoOnlineMatricula(
			List<CalendarioAtividadeMatriculaVO> listaCalendario, boolean verificarAcesso, UsuarioVO usuarioVO)
			throws Exception;

	Integer consultarTotalResgistro(TipoNivelProgramacaoTutoriaEnum tipoNivelProgramacaoTutoria, TurmaVO turma,
			Integer codigoUnidadeEnsino, Integer codigoDisciplina, Integer codigoCurso, Integer codigoProfessor,
			Boolean estudando, Boolean concluiram, boolean validarAno, String ano, String semestre,
			Integer codigoTemaAssunto, Integer codigoItemParametro, PeriodicidadeEnum periodicidade, Date dataInicio, Date dataFim, FiltroRelatorioAcademicoVO filtroAcademicoVO, MatriculaVO matriculaVO, Boolean situacaoAvaliacaoOnlineAprovado, Boolean situacaoAvaliacaoOnlineReprovado, Boolean situacaoAvaliacaoOnlineAguardandoExecucao, Boolean situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado, Boolean situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada, Integer codigoAvaliacaoOnlineRea, Boolean situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor, Boolean situacaoAtividadeDiscursivaAguardandoRespostaAluno, Boolean situacaoAtividadeDiscursivaAvaliada, Integer codigoAtividadeDiscursiva, Boolean situacaoDuvidaTutorAguardandoRespostaProfessor, Boolean situacaoDuvidaTutorAguardandoRespostaAluno, Integer codigoTutor, Double percentualInicio, Double percentualFim, UsuarioVO usuarioVO) throws Exception;

	Boolean consultarSeExisteCalendarioAtividadeParaAluno(Integer codigoMatriculaPeriodoTurmaDisciplina,
			TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula,
			SituacaoAtividadeEnum situacaoAtividade, TipoOrigemEnum tipoOrigem, String codigoOrigem,
			UsuarioVO usuarioLogado) throws Exception;

	void consultarEVincularCalendarioAtividadeMatriculaAvaliacaoOnlineSemPeriodoLiberacaoPorDependenciaCalendarioLancamentoNota(CalendarioLancamentoNotaVO calendarioLancamentoNotaVO, UsuarioVO usuarioVO, Boolean atualizarCalendarioAtividadeMatriculaComPeriodo) throws Exception;
	
	public void realizarGeracaoCalendarioAtividadeMatriculaAcessoConteudoEstudo(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, ConfiguracaoEADVO configuracaoEADVO, UsuarioVO usuarioLogado, ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO) throws Exception;
	
	public void executarInicializacaoDataInicioCalendarioAtividadeMatricula(ConfiguracaoEADVO configuracaoEADVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioLogado) throws Exception;
	//public Date realizarGeracaoDataInicioCalendarioAtividadeMatricula(ConfiguracaoEADVO configuracaoEADVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioLogado) throws Exception;

	public void alterarDataCalendarioAtividadeMatricula(final CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	public List<CalendarioAtividadeMatriculaVO> consultarAtividadeDiscursivaCodOrigem(Integer codOrigem, TipoOrigemEnum tipoOrigem,int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	List<CalendarioAtividadeMatriculaVO> consultarCalendariosDoDiaPorMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date data) throws Exception;	
	
	CalendarioAtividadeMatriculaVO consultarPorAvaliacaoOnlineMatricula(Integer avaliacaoOnlineMatricula, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	public Date consultarDataFimAtividadeDiscursiva(Integer codOrigem, String matriculaAluno,UsuarioVO usuarioLogado) throws Exception;

	List<CalendarioAtividadeMatriculaVO> consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividades(
			Integer codigoMatriculaPeriodoTurmaDisciplina,
			TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, TipoOrigemEnum tipoOrigem,
			String codOrigem, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

	CalendarioAtividadeMatriculaVO consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividadeUltimo(
			Integer codigoMatriculaPeriodoTurmaDisciplina,
			TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, TipoOrigemEnum tipoOrigem,
			String codOrigem, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;

}
