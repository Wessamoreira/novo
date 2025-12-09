package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.faces.model.SelectItem;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.academico.RenovarMatriculaControle;
import controle.arquitetura.DataModelo;
import jobs.JobExecutarSincronismoComLdapAoCancelarTransferirMatricula;
import negocio.comuns.academico.AutorizacaoCursoVO;
import negocio.comuns.academico.ColacaoGrauVO;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.ControleLivroFolhaReciboVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaUnidadeEnsinoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.DocumentacaoCursoVO;
import negocio.comuns.academico.DocumentoAssinadoPessoaVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.ExpedicaoDiplomaVO;
import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.HorarioAlunoTurnoVO;
import negocio.comuns.academico.ItemDisciplinaAntigaDisciplinaNovaVO;
import negocio.comuns.academico.LogMatriculaVO;
import negocio.comuns.academico.LogRegistroOperacoesVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MatriculaComHistoricoAlunoVO;
import negocio.comuns.academico.MatriculaControleLivroRegistroDiplomaVO;
import negocio.comuns.academico.MatriculaIntegralizacaoCurricularVO;
import negocio.comuns.academico.MatriculaPeriodoMensalidadeCalculadaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.NumeroMatriculaVO;
import negocio.comuns.academico.ObservacaoComplementarHistoricoAlunoVO;
import negocio.comuns.academico.PendenciaLiberacaoMatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.academico.enumeradores.MotivoSolicitacaoLiberacaoMatriculaEnum;
import negocio.comuns.academico.enumeradores.NomeTurnoCensoEnum;
import negocio.comuns.academico.enumeradores.OperacaoLogRegistroOperacoesEnum;
import negocio.comuns.academico.enumeradores.OrigemFechamentoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoPendenciaLiberacaoMatriculaEnum;
import negocio.comuns.academico.enumeradores.SituacaoRecuperacaoNotaEnum;
import negocio.comuns.academico.enumeradores.TabelaLogRegistroOperacoesEnum;
import negocio.comuns.academico.enumeradores.TipoAssinaturaDocumentoEnum;
import negocio.comuns.academico.enumeradores.TipoAutorizacaoCursoEnum;
import negocio.comuns.academico.enumeradores.TipoContratoMatriculaEnum;
import negocio.comuns.academico.enumeradores.TipoLivroRegistroDiplomaEnum;
import negocio.comuns.academico.enumeradores.TipoRespostaGabaritoEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.academico.enumeradores.TipoTextoEnadeEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.basico.enumeradores.TipoExigenciaDocumentoEnum;
import negocio.comuns.biblioteca.UnidadeEnsinoBibliotecaVO;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.comuns.crm.InteracaoWorkflowHistoricoVO;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeRespostaEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.MapaLancamentoFuturoVO;
import negocio.comuns.financeiro.MatriculaPeriodoVencimentoVO;
import negocio.comuns.financeiro.PerfilEconomicoVO;
import negocio.comuns.financeiro.PlanoFinanceiroAlunoDescricaoDescontosVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.processosel.GabaritoRespostaVO;
import negocio.comuns.processosel.GabaritoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.PeriodoChamadaProcSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoEixoCursoVO;
import negocio.comuns.secretaria.HistoricoGradeMigradaEquivalenteVO;
import negocio.comuns.secretaria.HistoricoGradeVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.comuns.utilitarias.dominios.FormaIngresso;
import negocio.comuns.utilitarias.dominios.JustificativaCensoEnum;
import negocio.comuns.utilitarias.dominios.OrdemHistoricoDisciplina;
import negocio.comuns.utilitarias.dominios.SituacaoColouGrauProgramacaoFormaturaAluno;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.comuns.utilitarias.dominios.SituacaoTransferenciaEntrada;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.comuns.utilitarias.dominios.TipoMobilidadeAcademicaEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoTransferenciaEntrada;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.MatriculaInterfaceFacade;
import relatorio.controle.academico.DocumentacaoMatriculaRelControle;
import relatorio.negocio.comuns.academico.ControleLivroRegistroDiplomaRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import webservice.servicos.MatriculaRSVO;
import webservice.servicos.PeriodoLetivoRSVO;
import webservice.servicos.ProcessoMatriculaRSVO;
import webservice.servicos.TurmaRSVO;
import webservice.servicos.TurnoRSVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>MatriculaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>MatriculaVO</code>. Encapsula toda a interação com o banco de dados.
 *
 * @see MatriculaVO
 * @see ControleAcesso
 */
@Lazy
@Repository
@Scope("singleton")
@SuppressWarnings("unchecked")
public class Matricula extends ControleAcesso implements MatriculaInterfaceFacade {

	protected static String idEntidade;
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static final long serialVersionUID = 1L;

	@SuppressWarnings("OverridableMethodCallInConstructor")
	public Matricula() throws Exception {
		super();
		setIdEntidade("Matricula");
	}

	class ExecutarRegrasContaIntegracaoPorMatricula implements Runnable {

		private MatriculaVO matricula;
		private UsuarioVO usuario;
		private boolean gerarContaGsuite = false;

		public ExecutarRegrasContaIntegracaoPorMatricula(MatriculaVO matricula, UsuarioVO usuario, boolean geraContaGsuite) {
			super();
			this.matricula = matricula;
			this.usuario = usuario;
			this.gerarContaGsuite = geraContaGsuite;
		}

		@Override
		public void run() {
			try {
				Thread.sleep(60000);//Tempo para a matricula termine a sua persistencia para que as validacoes necessario de email seja feita
				if(gerarContaGsuite) {
					getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarGeracaoDaContaGsuitePorMatricula(matricula, usuario);
//					getFacadeFactory().getConfiguracaoSeiBlackboardFacade().realizarSalaAulaOperacaoPorMatricula(matricula, null, usuario);
				}else {
					getFacadeFactory().getConfiguracaoSeiGsuiteFacade().realizarExclusaoDaContaGsuitePorMatricula(matricula, usuario);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void verificarPermissaoUsuarioFuncionalidade(UsuarioVO usuario, String nomeEntidade) throws Exception {
		verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public boolean verificarPermissaoInformarNumeroMatriculaManualmente(UsuarioVO usuario) {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.MATRICULA_PERMITIR_INFORMAR_NUMERO_MATRICULA_MANUALMENTE, usuario);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 *
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>MatriculaVO</code>.
	 */
	public MatriculaVO novo() throws Exception {
		Matricula.incluir(getIdEntidade());
		MatriculaVO obj = new MatriculaVO();
		return obj;
	}

	public void validarConsultarMeusHorarios(UsuarioVO usuario) throws Exception {

		consultar("MeusHorarios", true, usuario);

	}

	public void validarConsultarDocumentoEntregueAluno(UsuarioVO usuario) throws Exception {

		consultar("EntregaDocumento", true, usuario);

	}

	public void validarConsultarMeusContratosAluno(UsuarioVO usuario) throws Exception {

		consultar("MeusContratos", true, usuario);

	}

	public void validarConsultarRenovacaoMatriculaVisaoAluno(UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), true, usuario);
	}

	/**
	 * Verifica existe alguma matricula de uma determinada pessoa que está
	 * inativa. Se existir, joga uma exceção.
	 *
	 * @param codPessoa
	 * @throws Exception
	 */
	public void verificarMatriculaAtivaPorCodigoPessoa(Integer codPessoa, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		List<MatriculaVO> matriculaVOs = consultarMatriculaPorCodigoPessoa(codPessoa, 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuario);
		for (MatriculaVO matriculaVO : matriculaVOs) {
			if (!matriculaVO.getSituacao().equals("AT")) {
				throw new ConsistirException("A MATRÍCULA está INATIVA.");
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificaAlunoJaMatriculado(MatriculaVO obj, boolean permiteInformarTipMatricula, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, boolean validarAlunoMatriculadoWebServiceMatriculaCrm, boolean validarUnidadeEnsino ) throws Exception {
		MatriculaVO matriculaVO = this.consultarPorMatriculaCurso(obj.getCurso().getCodigo(), obj.getAluno().getCodigo(), obj.getMatricula(), validarUnidadeEnsino ?  obj.getUnidadeEnsino().getCodigo() : 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuario);
		ConsistirException ex = new ConsistirException();
		if (matriculaVO != null) {
			if (Uteis.isAtributoPreenchido(obj.getGradeCurricularAtual())) {
				if ((obj.getTipoMatricula().equalsIgnoreCase("EX") && obj.getCurso().getNivelEducacionalPosGraduacao()) && obj.getGradeCurricularAtual().getCodigo().equals(matriculaVO.getGradeCurricularAtual().getCodigo())) {
					if(validarAlunoMatriculadoWebServiceMatriculaCrm) {
						ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_alunoMatriculadoNesteCurso").replace("{0}", obj.getAluno().getNome()));
						ex.setObjetoOrigem(matriculaVO);
						throw ex;
					}
					throw new Exception(UteisJSF.internacionalizar("msg_alunoMatriculadoNesteCurso").replace("{0}", obj.getAluno().getNome()));
				} else if (obj.getTipoMatricula().equalsIgnoreCase("NO")) {
					if(validarAlunoMatriculadoWebServiceMatriculaCrm) {
						ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_alunoMatriculadoNesteCurso").replace("{0}", obj.getAluno().getNome()));
						ex.setObjetoOrigem(matriculaVO);
						throw ex;
					}
					throw new Exception(UteisJSF.internacionalizar("msg_alunoMatriculadoNesteCurso").replace("{0}", obj.getAluno().getNome()));
				}
			} else {
				if (!obj.getTipoMatricula().equalsIgnoreCase("EX") && !permiteInformarTipMatricula) {
					if(validarAlunoMatriculadoWebServiceMatriculaCrm) {
						ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_alunoMatriculadoNesteCurso").replace("{0}", obj.getAluno().getNome()));
						ex.setObjetoOrigem(matriculaVO);
						throw ex;
					}
					throw new Exception(UteisJSF.internacionalizar("msg_alunoMatriculadoNesteCurso").replace("{0}", obj.getAluno().getNome()));
				} else {
					obj.setTipoMatricula("EX");
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Boolean validarUnicidade(String novaMatricula) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT 1 FROM matricula ");
		sql.append(" WHERE matricula = ? ");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), novaMatricula).next();
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(MatriculaVO obj, ProcessoMatriculaCalendarioVO processoMatriculaCalendario, CondicaoPagamentoPlanoFinanceiroCursoVO condicao, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		incluir(obj, null, processoMatriculaCalendario, condicao, configuracaoFinanceiro, null, false, usuario);
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>MatriculaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>MatriculaVO</code> que será gravado no
	 *            banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MatriculaVO obj, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendario, CondicaoPagamentoPlanoFinanceiroCursoVO condicao, ConfiguracaoFinanceiroVO configuracaoFinanceiro, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, boolean transferencia, UsuarioVO usuario) throws Exception {
		this.incluir(obj, matriculaPeriodoVO, processoMatriculaCalendario, condicao, configuracaoFinanceiro, configuracaoGeralSistema, transferencia, false, false,true,  usuario);
	}

	public static void verificarPermissaoUsuarioVisualizarConsultorMatricula(UsuarioVO usuario, String nomeEntidade) throws Exception {
		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public boolean verificarPriorizarConsultorProspect(UsuarioVO usuario) {
		try {
			verificarPermissaoUsuarioVisualizarConsultorMatricula(usuario, "PriorizarConsultorProspectComoPadrao");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final MatriculaVO obj, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendario, CondicaoPagamentoPlanoFinanceiroCursoVO condicao, ConfiguracaoFinanceiroVO configuracaoFinanceiro, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, boolean transferencia, boolean controleAcesso,boolean validarAlunoMatriculadoWebServiceMatriculaCrm, Boolean criarHistoricoTransferenciaEntrada ,UsuarioVO usuario) throws Exception {
		final Date updated = new Date();
		try {
			ControleAcesso.incluir(getIdEntidade(), controleAcesso, usuario);
			if(!obj.getMatriculaOnlineExterna()) {
				MatriculaVO.validarDados(obj);
			}
			if (obj.getGradeCurricularAtual().getCodigo() == null || obj.getGradeCurricularAtual().getCodigo() == 0) {
				obj.setGradeCurricularAtual(matriculaPeriodoVO.getGradeCurricular());
			}
			obj.setTurnoAula(processoMatriculaCalendario.getTurnoAula());
			if (!Uteis.isAtributoPreenchido(obj.getTransferenciaEntrada().getCodigo()) && !transferencia ) {
				obj.setDiaSemanaAula(processoMatriculaCalendario.getDiaSemanaAula());
			}
			// Gerarao de Matricula Com transação separada			
			Thread numMat = new Thread(new GerarNumeroMatricula(obj, Uteis.getAnoData(matriculaPeriodoVO.getData()),usuario));
			numMat.start();
			while(numMat.isAlive()) {
				Thread.sleep(50);
			}		
			if(obj.getMatricula() == null || obj.getMatricula().trim().isEmpty()) {
				throw new Exception("Houve um erro na geração do número da matrícula, entre em contato com a IES.");
			}
			if (!transferencia) {
				verificaAlunoJaMatriculado(obj, false, configuracaoFinanceiro, usuario,  validarAlunoMatriculadoWebServiceMatriculaCrm, false);
			}
			verificarSuspensaoMatricula(obj, configuracaoGeralSistema);			
			realizarMontagenDadosAnoMesSemestreIgressoDataInicioCurso(obj, matriculaPeriodoVO, usuario);
			
			final String sql = "INSERT INTO Matricula( matricula, aluno, unidadeEnsino, curso, data, situacao, situacaoFinanceira, inscricao, usuario, turno, tipoMidiaCaptacao, tranferenciaEntrada, "// 12
					+ "responsavelLiberacaoDesconto, formaingresso, programareservavaga, financiamentoestudantil, apoiosocial, atividadecomplementar, disciplinasProcSeletivo, fezenade, dataenade, "// 9
					+ "notaenade, horascomplementares, enade, nomemonografia, alunoAbandonouCurso, observacaoComplementar, dataInicioCurso, dataConclusaoCurso, localArmazenamentoDocumentosMatricula, "// 9
					+ "notaMonografia, updated, formacaoAcademica, autorizacaoCurso, consultor, tipoMatricula, dataBaseSuspensao, matriculaSuspensa, matriculaSerasa, naoEnviarMensagemCobranca, qtdDisciplinasExtensao,"// 11
					+ "dataLiberacaoPendenciaFinanceira, responsavelLiberacaoPendenciaFinanceira, dataColacaoGrau, mesIngresso, anoIngresso, semestreIngresso, dataCadastro, totalPontoProcSeletivo, gradeCurricularAtual,naoApresentarCenso, notaEnem, classificacaoIngresso, codigoFinanceiroMatricula, dataProcessoSeletivo, bloqueioPorSolicitacaoLiberacaoMatricula, titulacaoOrientadorMonografia,  cargaHorariaMonografia , bolsasAuxilios,autodeclaracaoPretoPardoIndigena,normasMatricula , escolaPublica, "// 18
					+ "diaSemanaAula, turnoAula, nrmatriculacancelada) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?, ? ,?, ?, ? ,?, ?, ?, ? )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getMatricula());
					sqlInserir.setInt(2, obj.getAluno().getCodigo().intValue());
					sqlInserir.setInt(3, obj.getUnidadeEnsino().getCodigo().intValue());
					sqlInserir.setInt(4, obj.getCurso().getCodigo().intValue());
					sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setString(6, obj.getSituacao());
					sqlInserir.setString(7, obj.getSituacaoFinanceira());
					if (obj.getInscricao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(8, obj.getInscricao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(8, 0);
					}
					if(!obj.getUsuario().getCodigo().equals(0)) {
						sqlInserir.setInt(9, obj.getUsuario().getCodigo().intValue());
					} else {
						sqlInserir.setNull(9, 0);
					}
					sqlInserir.setInt(10, obj.getTurno().getCodigo().intValue());
					if (obj.getTipoMidiaCaptacao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(11, obj.getTipoMidiaCaptacao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(11, 0);
					}
					if (obj.getTransferenciaEntrada().getCodigo().intValue() != 0) {
						sqlInserir.setInt(12, obj.getTransferenciaEntrada().getCodigo().intValue());
					} else {
						sqlInserir.setNull(12, 0);
					}
					if (obj.getUsuarioLiberacaoDesconto().getCodigo().intValue() != 0) {
						sqlInserir.setInt(13, obj.getUsuarioLiberacaoDesconto().getCodigo().intValue());
					} else {
						sqlInserir.setNull(13, 0);
					}
					sqlInserir.setString(14, obj.getFormaIngresso().trim());
					sqlInserir.setString(15, obj.getProgramaReservaVaga());
					sqlInserir.setString(16, obj.getFinanciamentoEstudantil());
					sqlInserir.setString(17, obj.getApoioSocial());
					sqlInserir.setString(18, obj.getAtividadeComplementar());
					sqlInserir.setString(19, obj.getDisciplinasProcSeletivo());
					sqlInserir.setBoolean(20, obj.getFezEnade());
					if (Uteis.isAtributoPreenchido(obj.getDataEnade())) {
						sqlInserir.setDate(21, Uteis.getDataJDBC(obj.getDataEnade()));
					} else {
						sqlInserir.setNull(21, 0);
					}

					if (Uteis.isAtributoPreenchido(obj.getNotaEnade())) {
						sqlInserir.setDouble(22, obj.getNotaEnade());
					} else {
						sqlInserir.setNull(22, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getHorasComplementares())) {
						sqlInserir.setDouble(23, obj.getHorasComplementares());
					} else {
						sqlInserir.setNull(23, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getEnadeVO().getCodigo())) {
						sqlInserir.setInt(24, obj.getEnadeVO().getCodigo());
					} else {
						sqlInserir.setNull(24, 0);
					}
					sqlInserir.setString(25, obj.getTituloMonografia());
					sqlInserir.setBoolean(26, obj.getAlunoAbandonouCurso());
					sqlInserir.setString(27, obj.getObservacaoComplementar());
					if (Uteis.isAtributoPreenchido(obj.getDataInicioCurso())) {
						sqlInserir.setDate(28, Uteis.getDataJDBC(obj.getDataInicioCurso()));
					} else {
						sqlInserir.setNull(28, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataConclusaoCurso())) {
						sqlInserir.setDate(29, Uteis.getDataJDBC(obj.getDataConclusaoCurso()));
					} else {
						sqlInserir.setNull(29, 0);
					}
					sqlInserir.setString(30, obj.getLocalArmazenamentoDocumentosMatricula());
					if (obj.getNotaMonografia() == null) {
						sqlInserir.setNull(31, 0);
					} else {
						sqlInserir.setDouble(31, obj.getNotaMonografia());
					}
					sqlInserir.setTimestamp(32, Uteis.getDataJDBCTimestamp(updated));
					if (obj.getFormacaoAcademica().getCodigo().intValue() == 0) {
						sqlInserir.setNull(33, 0);
					} else {
						sqlInserir.setInt(33, obj.getFormacaoAcademica().getCodigo().intValue());
					}
					if (obj.getAutorizacaoCurso().getCodigo().intValue() == 0) {
						sqlInserir.setNull(34, 0);
					} else {
						sqlInserir.setInt(34, obj.getAutorizacaoCurso().getCodigo().intValue());
					}
					if (obj.getConsultor().getCodigo().intValue() == 0) {
						sqlInserir.setNull(35, 0);
					} else {
						sqlInserir.setInt(35, obj.getConsultor().getCodigo().intValue());
					}
					sqlInserir.setString(36, obj.getTipoMatricula());
					if (obj.getDataBaseSuspensao() != null) {
						sqlInserir.setDate(37, Uteis.getDataJDBC(obj.getDataBaseSuspensao()));
					} else {
						sqlInserir.setNull(37, 0);
					}
					if (obj.getMatriculaSuspensa() != null) {
						sqlInserir.setBoolean(38, obj.getMatriculaSuspensa());
					} else {
						sqlInserir.setNull(38, 0);
					}
					if (obj.getMatriculaSerasa() != null) {
						sqlInserir.setBoolean(39, obj.getMatriculaSerasa());
					} else {
						sqlInserir.setNull(39, 0);
					}
					sqlInserir.setBoolean(40, obj.getNaoEnviarMensagemCobranca());
					sqlInserir.setInt(41, obj.getQtdDisciplinasExtensao());
					if (obj.getDataLiberacaoPendenciaFinanceira() != null) {
						sqlInserir.setDate(42, Uteis.getDataJDBC(obj.getDataLiberacaoPendenciaFinanceira()));
					} else {
						sqlInserir.setNull(42, 0);
					}
					sqlInserir.setInt(43, obj.getResponsavelLiberacaoPendenciaFinanceira().getCodigo().intValue());
					if (obj.getDataColacaoGrau() != null) {
						sqlInserir.setDate(44, Uteis.getDataJDBC(obj.getDataColacaoGrau()));
					} else {
						sqlInserir.setNull(44, 0);
					}
					sqlInserir.setString(45, obj.getMesIngresso());
					sqlInserir.setString(46, obj.getAnoIngresso());
					sqlInserir.setString(47, obj.getSemestreIngresso());
					sqlInserir.setDate(48, Uteis.getDataJDBC(new Date()));

					if (obj.getTotalPontoProcSeletivo() != null) {
						sqlInserir.setDouble(49, obj.getTotalPontoProcSeletivo());
					} else {
						sqlInserir.setNull(49, 0);
					}
					sqlInserir.setInt(50, obj.getGradeCurricularAtual().getCodigo());
					sqlInserir.setBoolean(51, obj.getNaoApresentarCenso());				
					sqlInserir.setDouble(52, obj.getNotaEnem());
					if (obj.getClassificacaoIngresso() != null) {
						sqlInserir.setInt(53, obj.getClassificacaoIngresso());
					} else {
						sqlInserir.setNull(53, 0);
					}
					 sqlInserir.setString(54, obj.getCodigoFinanceiroMatricula());
					if (obj.getDataProcessoSeletivo() != null) {
						sqlInserir.setDate(55, Uteis.getDataJDBC(obj.getDataProcessoSeletivo()));
					} else {
						sqlInserir.setNull(55, 0);
					}
					sqlInserir.setBoolean(56, obj.getBloqueioPorSolicitacaoLiberacaoMatricula());
					sqlInserir.setString(57, obj.getTitulacaoOrientadorMonografia());
					sqlInserir.setInt(58, obj.getCargaHorariaMonografia());
					sqlInserir.setBoolean(59, obj.getBolsasAuxilios());
					sqlInserir.setBoolean(60, obj.getAutodeclaracaoPretoPardoIndigena());
					sqlInserir.setBoolean(61, obj.getNormasMatricula());
					sqlInserir.setBoolean(62, obj.getEscolaPublica());
					sqlInserir.setString(63, obj.getDiaSemanaAula().name());
	                sqlInserir.setString(64, obj.getTurnoAula().name());
	                sqlInserir.setString(65, obj.getNrMatriculaCancelada());
					return sqlInserir;
				}
			});
			if (!obj.getTransferenciaEntrada().getTipoTransferenciaEntrada().equals("IN") && obj.getCriarNovoUsuario()) {				
				criarUsuarioVO(obj, usuario, configuracaoGeralSistema);
			}
			if (!executarVerificacaoPessoaTipoAluno(obj.getAluno().getCodigo())) {			
				getFacadeFactory().getPessoaFacade().alterarTipoPessoa(obj.getAluno().getCodigo(), obj.getAluno().getFuncionario(), obj.getAluno().getProfessor(), Boolean.TRUE, Boolean.FALSE, obj.getAluno().getMembroComunidade(), usuario);
			}
			getFacadeFactory().getDocumetacaoMatriculaFacade().incluirDocumetacaoMatriculas(obj, obj.getMatricula(), obj.getDocumetacaoMatriculaVOs(), usuario, configuracaoGeralSistema);

			if (matriculaPeriodoVO != null) {		
				getFacadeFactory().getMatriculaPeriodoFacade().incluirMatriculaPeriodoVOEspecifico(matriculaPeriodoVO, obj, processoMatriculaCalendario, condicao, configuracaoFinanceiro, usuario);
			} else{
				getFacadeFactory().getMatriculaPeriodoFacade().incluirMatriculaPeriodos(obj, processoMatriculaCalendario, condicao, configuracaoFinanceiro, usuario);
			}
			obj.getPlanoFinanceiroAluno().setMatricula(obj.getMatricula());
			obj.getPlanoFinanceiroAluno().setMatriculaPeriodo(matriculaPeriodoVO.getCodigo());
			// //
			// System.out.println("++++++++++++++++++++++ INCLUI PLANO FINANCEIRO ALUNO +++++++++++++++++++++++++++");
			getFacadeFactory().getPlanoFinanceiroAlunoFacade().incluir(obj.getPlanoFinanceiroAluno());
			if (obj.getTransferenciaEntrada().getCodigo().intValue() != 0) {
				gavarMatriculaEmTransferenciaEntrada(obj, matriculaPeriodoVO, usuario);
				if(obj.getCurso().getCodigo().equals(obj.getTransferenciaEntrada().getCurso().getCodigo())) {
					getFacadeFactory().getEstagioFacade().realizarAproveitamentoEstagioTransferenciaUnidadeEnsino(obj, obj.getTransferenciaEntrada().getMatricula(), usuario);
				}
				getFacadeFactory().getSalaAulaBlackboardPessoaFacade().validarTransferenciaInternaMatriculaEnsalada(obj, matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs(), usuario);
			}
			verificarTransferenciaEntrada(obj, configuracaoFinanceiro, usuario);
			if (Uteis.isAtributoPreenchido(obj.getTransferenciaEntrada().getCodigo()) && criarHistoricoTransferenciaEntrada) {
				obj.getTransferenciaEntrada().setMatriculado(true);
				if (!Uteis.isAtributoPreenchido(obj.getTransferenciaEntrada().getTransferenciaEntradaDisciplinasAproveitadasVOs())) {
					obj.getTransferenciaEntrada().setTransferenciaEntradaDisciplinasAproveitadasVOs(getFacadeFactory().getTransferenciaEntradaFacade().buscarDisciplinaSeremAproveitadaTransferenciaInterna(
							obj.getTransferenciaEntrada().getMatricula().getAluno().getCodigo(), obj.getTransferenciaEntrada().getCurso().getCodigo(), obj.getTransferenciaEntrada().getGradeCurricular().getCodigo(), configuracaoFinanceiro, usuario));
				}				
				
				SituacaoMatriculaPeriodoEnum situacaoEnum = SituacaoMatriculaPeriodoEnum.getEnumPorValor(obj.getTransferenciaEntrada().getMatricula().getSituacao());
				if(situacaoEnum.equals(SituacaoMatriculaPeriodoEnum.ABANDONO_CURSO) || situacaoEnum.equals(SituacaoMatriculaPeriodoEnum.TRANCADA)) {
					getFacadeFactory().getMatriculaFacade().alterarSituacaoMatricula(obj.getMatricula(),situacaoEnum.getValor(), usuario);
					obj.setSituacao(situacaoEnum.getValor());
					matriculaPeriodoVO.setSituacaoMatriculaPeriodo(situacaoEnum.getValor());					
					getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodo(matriculaPeriodoVO, null, null, null);
				}
				obj.getTransferenciaEntrada().setMatriculaPeriodo(matriculaPeriodoVO);
				obj.getTransferenciaEntrada().getMatriculaPeriodo().setMatriculaVO(obj);
				getFacadeFactory().getTransferenciaEntradaFacade().criarHistoricoTransferenciaEntrada(obj.getTransferenciaEntrada(), configuracaoFinanceiro, usuario);
				getFacadeFactory().getEstagioFacade().realizarAproveitamentoEstagioDaGradeCurricularAntigaParaNovaGradeCurricularPorTransferenciaEntrada(obj, obj.getTransferenciaEntrada(), "TRANSFERENCIA DE MATRÍCULA", configuracaoGeralSistema, usuario);

			}
			if(obj.getAluno().getRenovacaoAutomatica()) {
				getFacadeFactory().getPessoaFacade().executarAtualizacaoDadosPessoaRenovacaoAutomatica(obj.getAluno(), usuario);
			}
//			incluirLogMatricula(obj, matriculaPeriodoVO, usuario);
			obj.setUpdated(updated);
			getFacadeFactory().getMatriculaFacade().realizarEnvioMensagemConfirmacaoMatriculaValidandoRegrasEntregaDocumentacao(obj, usuario);		
//			getFacadeFactory().getProspectsFacade().realizarVinculoUnidadeEnsinoProspectSemUnidadeEnsinoPorPessoa(obj.getAluno().getCodigo(), obj.getUnidadeEnsino().getCodigo(), usuario);

			getFacadeFactory().getMatriculaFacade().validarDadosPendenciaLiberacaoMatricula(obj, matriculaPeriodoVO, usuario);
			getFacadeFactory().getMatriculaFacade().executarCriacaoRegistroAcademicoAluno(obj, matriculaPeriodoVO ,usuario, configuracaoGeralSistema);			
			getFacadeFactory().getMatriculaFacade().executarProcessamentoRegistroLdapValidandoRegraParaVeterano(obj, matriculaPeriodoVO ,usuario, configuracaoGeralSistema);
			if (Uteis.isAtributoPreenchido(obj.getDocumetacaoMatriculaVOs()) && obj.getDocumetacaoMatriculaVOs().stream().anyMatch(documentacaoMatriculaVO -> Uteis.isAtributoPreenchido(documentacaoMatriculaVO) && documentacaoMatriculaVO.getEntregue() && Uteis.isAtributoPreenchido(documentacaoMatriculaVO.getArquivoVOAssinado()))) {
				getFacadeFactory().getMatriculaPeriodoFacade().realizarAtivacaoMatriculaValidandoRegrasEntregaDocumentoAssinaturaContratoMatricula(obj.getMatricula(), getAplicacaoControle().getConfiguracaoFinanceiroVO(obj.getUnidadeEnsino().getCodigo()), Boolean.TRUE, usuario);
			}
			
			obj.setNovoObj(false);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			if(obj.isGeracaoMatriculaAutomatica()) {
				obj.setMatricula("");
			}
			throw e;
		}
	}

	/**
	 * @param obj
	 * @param matriculaPeriodoVO
	 * @param usuario
	 * @throws Exception
	 */
	public void realizarMontagenDadosAnoMesSemestreIgressoDataInicioCurso(final MatriculaVO obj,
			MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception {
		if (obj.getInscricao() != null && Uteis.isAtributoPreenchido(obj.getInscricao())) {
			realizarMontagemDadosProcSeletivoMatricula(obj, matriculaPeriodoVO, usuario);		
			if (obj.getInscricao().getProcSeletivo().getAno().equals("") && obj.getInscricao().getProcSeletivo().getSemestre().equals("") && obj.getInscricao().getItemProcessoSeletivoDataProva().getCodigo() != null && obj.getInscricao().getItemProcessoSeletivoDataProva().getCodigo() > 0) {
				obj.getInscricao().setItemProcessoSeletivoDataProva(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(obj.getInscricao().getItemProcessoSeletivoDataProva().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
				if (obj.getInscricao().getProcSeletivo().getAno().equals("") && obj.getInscricao().getProcSeletivo().getSemestre().equals("")) {
					obj.setAnoIngresso(Integer.valueOf(Uteis.getAnoData(obj.getInscricao().getItemProcessoSeletivoDataProva().getDataProva())).toString());
					obj.setMesIngresso(MesAnoEnum.getEnum(Integer.valueOf(Uteis.getMesData(obj.getInscricao().getItemProcessoSeletivoDataProva().getDataProva())).toString()).getKey());
					obj.setSemestreIngresso(Uteis.getSemestreData(obj.getInscricao().getItemProcessoSeletivoDataProva().getDataProva()));
				} 
			}else {
				obj.setAnoIngresso(obj.getInscricao().getProcSeletivo().getAno());
				obj.setSemestreIngresso(obj.getInscricao().getProcSeletivo().getSemestre());
				if (obj.getSemestreIngresso().equals("1")) {
					obj.setMesIngresso("01");
				} else {
					obj.setMesIngresso("07");
				}					
			}
		} else if(obj.getTransferenciaEntrada().getTipoTransferenciaEntrada().equals("IN")){				
			obj.setAnoIngresso(obj.getTransferenciaEntrada().getMatricula().getAnoIngresso());
			obj.setMesIngresso(obj.getTransferenciaEntrada().getMatricula().getMesIngresso());
			obj.setSemestreIngresso(obj.getTransferenciaEntrada().getMatricula().getSemestreIngresso());
			obj.setDataInicioCurso(obj.getTransferenciaEntrada().getMatricula().getDataInicioCurso());
			obj.setDiaSemanaAula(obj.getTransferenciaEntrada().getMatricula().getDiaSemanaAula());
			
			
		}else {
			if (!matriculaPeriodoVO.getAno().equals("") && !matriculaPeriodoVO.getSemestre().equals("")) {
				obj.setAnoIngresso(matriculaPeriodoVO.getAno());
				if (matriculaPeriodoVO.getSemestre().equals("1")) {
					obj.setMesIngresso("01");
				} else {
					obj.setMesIngresso("07");
				}
				obj.setSemestreIngresso(matriculaPeriodoVO.getSemestre());
			} else {
				obj.setAnoIngresso(Integer.valueOf(Uteis.getAnoData(obj.getData())).toString());
				obj.setMesIngresso(MesAnoEnum.getEnum(Integer.valueOf(Uteis.getMesData(obj.getData())).toString()).getKey());
				obj.setSemestreIngresso(Uteis.getSemestreData(obj.getData()));
			}
		    obj.setDataInicioCurso(obj.getData());
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void incluirLogMatricula(final MatriculaVO obj, final MatriculaPeriodoVO matriculaPeriodo, final UsuarioVO usuario) throws Exception {
		try {
			final String sql = "INSERT INTO LogMatricula(responsavel, dataAlteracao, matricula, acao ) VALUES (?, ?, ?, ?) ";

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, usuario.getCodigo());
					sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setString(3, obj.getMatricula());
					sqlInserir.setString(4, montarStringAcoes(obj, matriculaPeriodo));
					return sqlInserir;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void incluirLogMatriculaSerasa(final MatriculaVO obj, final String acao, final UsuarioVO usuario) throws Exception {
		try {
			final String sql = "INSERT INTO LogMatricula(responsavel, dataAlteracao, matricula, acao ) VALUES (?, ?, ?, ?) ";

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, usuario.getCodigo());
					sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setString(3, obj.getMatricula());
					sqlInserir.setString(4, acao);
					return sqlInserir;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void incluirLogPreMatricula(final MatriculaVO obj, final MatriculaPeriodoVO matriculaPeriodo, final String acao, final UsuarioVO usuario) throws Exception {
		try {
			final String sql = "INSERT INTO LogMatricula(responsavel, dataAlteracao, matricula, acao ) VALUES (?, ?, ?, ?) ";

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, usuario.getCodigo());
					sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setString(3, obj.getMatricula());
					sqlInserir.setString(4, acao);
					return sqlInserir;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	public List<LogMatriculaVO> consultaLogMatriculaPorMatricula(String matricula, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select logMatricula.matricula, logmatricula.codigo, logmatricula.acao, logmatricula.dataalteracao, logmatricula.responsavel, pessoa.nome as nomeResponsavel from logMatricula ");
		sqlStr.append(" inner join usuario on usuario.codigo = logmatricula.responsavel ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = usuario.pessoa ");
		sqlStr.append(" WHERE matricula = '").append(matricula).append("' ");
		sqlStr.append(" ORDER BY dataAlteracao desc ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<LogMatriculaVO> vetResultado = new ArrayList<LogMatriculaVO>(0);
		while (dadosSQL.next()) {
			LogMatriculaVO obj = new LogMatriculaVO();
			obj.setMatricula(dadosSQL.getString("matricula"));
			obj.setDataAlteracao(dadosSQL.getDate("dataAlteracao"));
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setAcao(dadosSQL.getString("acao"));
			obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
			obj.getResponsavel().setNome(dadosSQL.getString("nomeResponsavel"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public String montarStringAcoes(MatriculaVO obj, MatriculaPeriodoVO matriculaPeriodo) {
		String acoes = "";
		acoes += obj.getMatricula() + " - ";
		acoes += obj.getFormaIngresso() + " - ";
		acoes += " CONSULTOR => ( ";
		acoes += obj.getConsultor().getCodigo() + " - ";
		acoes += " ) ";
		acoes += obj.getUnidadeEnsino().getNome() + " - ";
		acoes += obj.getTipoMatricula() + " - ";
		acoes += obj.getCurso().getNome() + " - ";
		acoes += obj.getTurno().getNome() + " - ";
		acoes += obj.getSituacao() + " - ";
		acoes += obj.getData_Apresentar() + " - ";
		acoes += matriculaPeriodo.getProcessoMatriculaVO().getDescricao() + " - ";
		acoes += matriculaPeriodo.getGradeCurricular().getNome() + " - ";
		acoes += matriculaPeriodo.getPeridoLetivo().getDescricao() + " - ";
		acoes += matriculaPeriodo.getTurma().getIdentificadorTurma() + " - ";
		acoes += matriculaPeriodo.getPlanoFinanceiroCurso().getDescricao() + " - ";
		acoes += matriculaPeriodo.getSituacao() + " - ";
		acoes += matriculaPeriodo.getSituacaoMatriculaPeriodo() + " - ";
		acoes += matriculaPeriodo.getData_Apresentar() + " - ";
		acoes += " DOCUMENTAÇÃO => ( ";
		Iterator e = obj.getDocumetacaoMatriculaVOs().iterator();
		while (e.hasNext()) {
			DocumetacaoMatriculaVO doc = (DocumetacaoMatriculaVO) e.next();
			acoes += doc.getTipoDeDocumentoVO().getNome() + " - " + doc.getEntregue();
		}
		acoes += " ) DISCIPLINAS => ( ";
		Iterator i = matriculaPeriodo.getMatriculaPeriodoTumaDisciplinaVOs().iterator();
		while (i.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO mptd = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
			acoes += mptd.getTurma().getIdentificadorTurma() + " - " + mptd.getDisciplina().getNome() + " - " + mptd.getGradeDisciplinaVO().getCargaHoraria();
		}
		acoes += " ) FINANCEIRO => (";
		Iterator j = matriculaPeriodo.getMatriculaPeriodoVencimentoVOs().iterator();
		while (j.hasNext()) {
			MatriculaPeriodoVencimentoVO mpv = (MatriculaPeriodoVencimentoVO) j.next();
			acoes += " ** " + mpv.getParcela() + " - " + mpv.getDataVencimento_Apresentar() + " - " + mpv.getValor() + " - " + mpv.getValorTotalDesconto() + " - " + mpv.getValorDescontoCalculadoPrimeiraFaixaDescontos() + " - " + mpv.getValorDescontoCalculadoSegundaFaixaDescontos() + " - " + mpv.getValorDescontoCalculadoTerceiraFaixaDescontos() + " - " + mpv.getValorDescontoCalculadoQuartaFaixaDescontos();
		}
		return acoes;
	}

	public void verificarSuspensaoMatricula(MatriculaVO matricula, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) {
		Iterator e = matricula.getDocumetacaoMatriculaVOs().iterator();
		while (e.hasNext()) {
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) e.next();
			if (obj.getGerarSuspensaoMatricula() && !obj.getEntregue()) {
				if (configuracaoGeralSistema.getConfiguracoesVO().getControlarSuspensaoMatriculaPendenciaDocumentos()) {
					matricula.setDataBaseSuspensao(matricula.getData());
				}
			}
		}
		boolean removerSuspensao = true;
		Iterator ite = matricula.getDocumetacaoMatriculaVOs().iterator();
		while (ite.hasNext()) {
			DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) ite.next();
			if (obj.getGerarSuspensaoMatricula() && !obj.getEntregue()) {
				removerSuspensao = false;
				continue;
			}
		}
		if (matricula.getMatriculaSuspensa() != null) {
			if (matricula.getMatriculaSuspensa() && removerSuspensao) {
				matricula.setMatriculaSuspensa(Boolean.FALSE);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoMatriculaEMatriculaPeriodoTransferenciaInterna(MatriculaVO matricula, OrigemFechamentoMatriculaPeriodoEnum origemFechamentoMatriculaPeriodoEnum, Integer codigoOrigemFechamentoMatriculaPeriodo, Date dataFechamentoMatriculaPeriodo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		alterarSituacaoMatricula(matricula.getMatricula(), SituacaoVinculoMatricula.TRANSFERENCIA_INTERNA.getValor(), usuario);
		if (!matricula.getCurso().getIntegral()) {
			MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatriculaSituacoes(matricula.getMatricula(), "'AT', 'PR', 'TR'", false, Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuario);
			if (matriculaPeriodoVO != null && !matriculaPeriodoVO.getCodigo().equals(0)) {
				matriculaPeriodoVO.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_INTERNA.getValor());
				getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodo(matriculaPeriodoVO, origemFechamentoMatriculaPeriodoEnum, codigoOrigemFechamentoMatriculaPeriodo, dataFechamentoMatriculaPeriodo);
				getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().retirarReservaTurmaDisciplina(matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs(), usuario);
				getFacadeFactory().getHistoricoFacade().alterarSituacaoHistoricoPelaMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), "TF", usuario);
			}
		} else {
			List listaMatriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodoPorMatriculaSituacoes(matricula.getMatricula(), "'AT', 'PR', 'TR'", false, Uteis.NIVELMONTARDADOS_COMBOBOX, configuracaoFinanceiroVO, usuario);
			Iterator i = listaMatriculaPeriodoVO.iterator();
			while (i.hasNext()) {
				MatriculaPeriodoVO matriculaPeriodoVO = (MatriculaPeriodoVO)i.next();
				matriculaPeriodoVO.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_INTERNA.getValor());
				getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodo(matriculaPeriodoVO, origemFechamentoMatriculaPeriodoEnum, codigoOrigemFechamentoMatriculaPeriodo, dataFechamentoMatriculaPeriodo);
				getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().retirarReservaTurmaDisciplina(matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs(), usuario);
				getFacadeFactory().getHistoricoFacade().alterarSituacaoHistoricoPelaMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), "TF", usuario);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarUsuarioVO(MatriculaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		UsuarioVO usuarios = null;
		if (configuracaoGeralSistema.getGerarSenhaCpfAluno()) {
			usuarios = getFacadeFactory().getUsuarioFacade().consultarPorUsernameUnico(obj.getAluno().getCPF(), false, Uteis.NIVELMONTARDADOS_TODOS,usuario);
	    } else {
	        usuarios = getFacadeFactory().getUsuarioFacade().consultarPorUsernameUnico(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS,usuario);
	    }
		if(usuarios == null){
	    	usuarios = consultarUsuarioPorPessoa(obj.getAluno().getCodigo(), usuario);
	    }
		if (usuarios == null || usuarios.getCodigo().intValue() == 0) {
			criarNovoUsuario(obj, usuario, configuracaoGeralSistema);
			obj.setConsiderarValidacaoLdapBlackBoard(false);
		} else if(usuarios.getPessoa().getCodigo().equals(obj.getAluno().getCodigo())){
			if (!obj.getAluno().getAluno() && !obj.getAluno().getProfessor() && !obj.getAluno().getFuncionario() && !obj.getAluno().getMembroComunidade() && !obj.getAluno().getCandidato()) {
				getFacadeFactory().getUsuarioFacade().alterarTipoUsuario(obj.getAluno().getCodigo(), "AL", usuario);
			}
			if (usuarios.getTipoUsuario().equals("CA")) {
				getFacadeFactory().getUsuarioFacade().alterarTipoUsuario(obj.getAluno().getCodigo(), "AL", usuario);
			}			
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarNovoUsuario(MatriculaVO obj, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		PerfilAcessoVO perfilAcessoBusca = consultarPerfilAcessoPadrao(obj.getUnidadeEnsino().getCodigo(), usuarioLogado, configuracaoGeralSistema);
		UsuarioVO usuarioIncluir =obj.criarUsuario(perfilAcessoBusca, configuracaoGeralSistema);
		String senhaAntesCriptografia = usuarioIncluir.getSenha();
		usuarioIncluir.setPessoa(obj.getAluno());
		getFacadeFactory().getUsuarioFacade().incluir(usuarioIncluir, false, usuarioLogado);
		ConfiguracaoLdapVO conf = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarPorCodigo(obj.getCurso().getConfiguracaoLdapVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
		if(Uteis.isAtributoPreenchido(conf) && Uteis.isAtributoPreenchido(conf.getPrefixoSenha())) {
			senhaAntesCriptografia = conf.getPrefixoSenha() + Uteis.removerMascara(obj.getAluno().getCPF());	
		}
		PessoaEmailInstitucionalVO emailInstitucional =  getFacadeFactory().getPessoaEmailInstitucionalFacade().incluirPessoaEmailInstitucional(conf, null , true,  usuarioIncluir,usuarioLogado);
		getFacadeFactory().getLdapFacade().executarSincronismoComLdapAoIncluirUsuario(conf, usuarioIncluir, senhaAntesCriptografia, obj ,emailInstitucional,usuarioLogado);
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCriacaoRegistroAcademicoAluno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {		
				
		PessoaVO pessoaComRegistroAcademico = getFacadeFactory().getPessoaFacade().consultarRegistroAcademicoPorPessoa(matriculaVO.getAluno().getCodigo(),false , usuario);
		if(pessoaComRegistroAcademico != null && Uteis.isAtributoPreenchido(pessoaComRegistroAcademico.getRegistroAcademico())) {
			matriculaVO.getAluno().setRegistroAcademico(pessoaComRegistroAcademico.getRegistroAcademico());				
		}else {
			matriculaVO.getAluno().setRegistroAcademico(matriculaVO.getMatricula());
			getFacadeFactory().getPessoaFacade().alterarRegistroAcademico(matriculaVO.getAluno().getCodigo(), matriculaVO.getAluno().getRegistroAcademico());
		}	
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarProcessamentoRegistroLdapValidandoRegraParaVeterano(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioLogado, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {		
		if(matriculaVO.getConsiderarValidacaoLdapBlackBoard()) {			
			UsuarioVO usuarioAlunoAlteracao = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(matriculaVO.getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioLogado);
		    if (!Uteis.isAtributoPreenchido(usuarioAlunoAlteracao.getCodigo())) {		
		    	return ;
		    }
		    ConfiguracaoLdapVO configuracaoLdapPorCurso = getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarPorCodigo(matriculaVO.getCurso().getConfiguracaoLdapVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado);
		    if(!Uteis.isAtributoPreenchido(configuracaoLdapPorCurso)) {
		    	return ;
		    }		     		  
			getFacadeFactory().getLdapFacade().executarProcessamentoAtivacaoRegistroLdapBlackBoard(matriculaVO,configuracaoLdapPorCurso,usuarioAlunoAlteracao,configuracaoGeralSistema,usuarioLogado);				
		}	
    }


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gavarMatriculaEmTransferenciaEntrada(MatriculaVO obj, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception {
		if ((obj.getAproveitamentoDisciplinaVO().getAproveitamentoPrevisto()) && (!obj.getHistoricosAproveitamentoDisciplinaPrevisto().isEmpty())) {
			obj.getAproveitamentoDisciplinaVO().setMatricula(obj);
			obj.getAproveitamentoDisciplinaVO().setMatriculaPeriodo(matriculaPeriodoVO);
			obj.getAproveitamentoDisciplinaVO().setTurno(obj.getTurno());
			getFacadeFactory().getAproveitamentoDisciplinaFacade().alterarAproveitoPrevistoParaEfetivo(obj.getAproveitamentoDisciplinaVO(), usuario);
		}
		if (obj.getTransferenciaEntrada().getTipoTransferenciaEntrada().equals("EX")) {
			getFacadeFactory().getTransferenciaEntradaFacade().alterarMatricula(obj.getTransferenciaEntrada().getCodigo(), obj.getMatricula());
		}
		getFacadeFactory().getTransferenciaEntradaFacade().alterarSituacao(obj.getTransferenciaEntrada().getCodigo(), SituacaoTransferenciaEntrada.EFETIVADO.getValor());
	}

	public PerfilAcessoVO consultarPerfilAcessoPadrao(Integer unidadeEnsino, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		if (configuracaoGeralSistema == null) {
			configuracaoGeralSistema = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsadaUnidadEnsino(unidadeEnsino, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		if (configuracaoGeralSistema == null || configuracaoGeralSistema.getCodigo().intValue() == 0) {
			throw new Exception("Não existe nenhum perfil de acesso padrão cadastrado para vincular a aluno referente a esta matrícula.");
		}
		return configuracaoGeralSistema.getPerfilPadraoAluno();
	}

	public MapaLancamentoFuturoVO popularMapaLancamentoFuturo(MatriculaVO matricula, ProcessoMatriculaCalendarioVO processoMatriculaCalendario) throws Exception {
		MapaLancamentoFuturoVO obj = new MapaLancamentoFuturoVO();
		obj.setMatriculaOrigem(matricula.getMatricula());
		obj.setDataEmissao(matricula.getData());
		obj.setDataPrevisao(processoMatriculaCalendario.getDataFinalMatricula());
		obj.setResponsavel(matricula.getUsuario());
		obj.setTipoOrigem("MA");
		obj.setTipoMapaLancamentoFuturo("MA");
		obj.setValor(matricula.getValorMatricula());
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMatriculaDePendenciaFinanceira(MatriculaVO matricula, UsuarioVO usuario) throws Exception {
		try {
			MapaLancamentoFuturoVO mapa = getFacadeFactory().getMapaLancamentoFuturoFacade().consultarPorMatriculaOrigem(matricula.getMatricula(), "MA", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			if (mapa != null) {
				getFacadeFactory().getMapaLancamentoFuturoFacade().excluir(mapa, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public UsuarioVO consultarUsuarioPorPessoa(Integer pessoa, UsuarioVO usuario) throws Exception {
		UsuarioVO usuarios = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(pessoa, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		return usuarios;
	}

	@Deprecated
	public void validarDadosTransferenciaGradeCurricular(MatriculaVO matriculaVO) throws Exception {
		if (matriculaVO.getMatriculaPeriodoVOs().size() == 1) {
			throw new Exception("A transferência de grade não poderá ser feita porque o aluno possui apenas uma matrícula período. Será necessário matriculá-lo novamente para a mudança de grade curricular.");
		}
	}

	@Deprecated
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarAlteracaoGradeCurricular(List listaMatriculaPeriodoAlunos, List listaDisciplinaEquivalente, GradeCurricularVO gradeOrigem, GradeCurricularVO gradeMigrar, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		Iterator i = listaMatriculaPeriodoAlunos.iterator();
		while (i.hasNext()) {
			MatriculaPeriodoVO matrPer = (MatriculaPeriodoVO) i.next();
			MatriculaVO matricula = new MatriculaVO();
			matricula = matrPer.getMatriculaVO();
			getFacadeFactory().getMatriculaFacade().carregarDados(matricula, NivelMontarDados.TODOS, usuario);
			// validarDadosTransferenciaGradeCurricular(matricula);

			List<HistoricoGradeVO> historicoGradeVOs = new ArrayList<HistoricoGradeVO>(0);
			List<HistoricoGradeMigradaEquivalenteVO> historicoGradeMigradaEquivalenteVOs = new ArrayList<HistoricoGradeMigradaEquivalenteVO>(0);
			historicoGradeVOs = getFacadeFactory().getHistoricoGradeFacade().consultarGradeOrigemAntigaAlunoPorMatricula(matricula.getMatricula(), usuario);
			Boolean usarSituacaoAprovadoAproveitamento = getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorMatriculaCurso(matricula.getMatricula(), usuario);

			boolean primeiraVez = true;
			for (MatriculaPeriodoVO matPer : matricula.getMatriculaPeriodoVOs()) {
				PeriodoLetivoVO periodoLetivoNovo = getFacadeFactory().getPeriodoLetivoFacade().consultarPorPeriodoLetivoGradeCurricular(matPer.getPeridoLetivo().getPeriodoLetivo(), gradeMigrar.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				if (primeiraVez) {
					matrPer = matPer;
					primeiraVez = false;
				}
				if (periodoLetivoNovo.getCodigo().intValue() == 0) {
					// Caso nao seja encontrado periodo letivo correspondente ao
					// anterior na nova grade curricular a aplicação irá jogar
					// o vinculo do perioLetivo da nova grade para o ultimo
					// periodo Ativo do aluno.
					periodoLetivoNovo = getFacadeFactory().getPeriodoLetivoFacade().consultarUltimoPeriodoLetivoGradeCurricular(gradeMigrar.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
					getFacadeFactory().getMatriculaPeriodoFacade().alterarPeriodoLetivoGradeCurricular(matPer.getCodigo(), periodoLetivoNovo.getCodigo(), gradeMigrar.getCodigo());
				} else {
					// update na matriculaPeriodo alterando o periodo letivo e a
					// gade curricular;
					getFacadeFactory().getMatriculaPeriodoFacade().alterarPeriodoLetivoGradeCurricular(matPer.getCodigo(), periodoLetivoNovo.getCodigo(), gradeMigrar.getCodigo());
				}
			}
			// CASO_A FINALIZADO

			// Inicio CASO_B
			// Verificar se Aluno esta aprovado em todas as disciplinas no mapa
			// de equivalencia da grade origem
			if (!listaDisciplinaEquivalente.isEmpty()) {
				Iterator j = listaDisciplinaEquivalente.iterator();
				while (j.hasNext()) {
					ItemDisciplinaAntigaDisciplinaNovaVO item = (ItemDisciplinaAntigaDisciplinaNovaVO) j.next();
					Boolean possuiDisciplinaReprovada = false;

					Iterator k = item.getListaDisciplinaGradeOrigemVOs().iterator();
					while (k.hasNext()) {
						DisciplinaVO disc = (DisciplinaVO) k.next();
						if (!verificaSeAlunoAprovadoEmDisciplinasGradeCurricular(matricula.getMatricula(), disc.getCodigo(), configuracaoFinanceiroVO, usuario)) {
							possuiDisciplinaReprovada = true;
						}
					}
					if (possuiDisciplinaReprovada) {
						criarMatriculaPeriodoTurmaDisciplinasParaDisciplinasReprovadas(matrPer, matricula.getMatricula(), item.getListaDisciplinaGradeOrigemVOs(), item.getListaDisciplinaGradeMigrarVOs(), usarSituacaoAprovadoAproveitamento, historicoGradeMigradaEquivalenteVOs, configuracaoFinanceiroVO, usuario);
						// break;
					} else {
						// calcularMediaDisciplinas
						calcularMediaDisciplinaAprovadas(matrPer, matricula.getMatricula(), item.getListaDisciplinaGradeOrigemVOs(), item.getListaDisciplinaGradeMigrarVOs(), usarSituacaoAprovadoAproveitamento, historicoGradeMigradaEquivalenteVOs, configuracaoFinanceiroVO, usuario);
						// Registrar aluno nas disciplina da grade como aprovado
						// criar objeto MatriculaPeriodoTurmaDisciplinaVO
						// criar objeto historico
					}
				}
			}
			// gravarTransferenciaMatrizCurricular(matricula.getMatricula(),
			// historicoGradeVOs, historicoGradeMigradaEquivalenteVOs,
			// gradeOrigem.getCodigo(), gradeMigrar.getCodigo(), usuario);
		}
	}

	// @Deprecated
	// @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED,
	// rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	// public void gravarTransferenciaMatrizCurricular(String matricula,
	// List<HistoricoGradeVO> historicoGradeVOs,
	// List<HistoricoGradeMigradaEquivalenteVO>
	// historicoGradeMigradaEquivalenteVOs, Integer gradeOrigem, Integer
	// gradeMigrar, UsuarioVO usuario) throws Exception {
	// TransferenciaMatrizCurricularVO obj = new
	// TransferenciaMatrizCurricularVO();
	// obj.setMatricula(matricula);
	// obj.setGradeOrigem(gradeOrigem);
	// obj.setGradeMigrar(gradeMigrar);
	// obj.setResponsavel(usuario);
	// obj.setData(new Date());
	// obj.setHistoricoGradeVOs(historicoGradeVOs);
	// obj.setHistoricoGradeMigradaEquivalenteVOs(historicoGradeMigradaEquivalenteVOs);
	// getFacadeFactory().getTransferenciaMatrizCurricularFacade().incluir(obj,
	// usuario);
	// }

	@Deprecated
	public void calcularMediaDisciplinaAprovadas(MatriculaPeriodoVO matrPer, String matricula, List<DisciplinaVO> listaDisciplinaGradeOrigemVOs, List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinaGradeMigrarVOs, Boolean usarSituacaoAprovadoAproveitamento, List<HistoricoGradeMigradaEquivalenteVO> listaHistoricoGradeMigrarEquivalenteVOs, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		Double somaMedia = 0.0;
		Double somaFrequencia = 0.0;
		Integer quantidadeDisciplina = 0;
		String situacao = "";
		if (usarSituacaoAprovadoAproveitamento) {
			situacao = "AA";
		} else {
			situacao = "AP";
		}
		for (DisciplinaVO disc : listaDisciplinaGradeOrigemVOs) {
			// List<HistoricoVO> hist =
			// getFacadeFactory().getHistoricoFacade().consultarPorMatriculaDisciplinaSituacao(matricula,
			// disc.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
			List<HistoricoVO> hist = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaDisciplinasAprovadas(matricula, disc.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuario);
			matrPer = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaPrimeiraMatriculaPeriodoPorMatricula(matricula, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			for (HistoricoVO h : hist) {
				somaMedia += h.getMediaFinal();
				somaFrequencia += h.getFreguencia();
				// matrPer = h.getMatriculaPeriodo();
			}
			quantidadeDisciplina++;
		}
		Double novaMediaCalculada = somaMedia / quantidadeDisciplina;
		novaMediaCalculada = Uteis.arredondar(novaMediaCalculada, 2, 0);
		Double novaFrequenciaCalculada = somaFrequencia / quantidadeDisciplina;
		novaFrequenciaCalculada = Uteis.arredondar(novaFrequenciaCalculada, 2, 0);
		for (MatriculaPeriodoTurmaDisciplinaVO matrPerTurDisc : listaDisciplinaGradeMigrarVOs) {
			matrPerTurDisc.setAno(matrPer.getAno());
			matrPerTurDisc.setSemestre(matrPer.getSemestre());
			matrPerTurDisc.setMatricula(matricula);
			matrPerTurDisc.setMatriculaPeriodo(matrPer.getCodigo());

			listaHistoricoGradeMigrarEquivalenteVOs.add(realizarCriacaoHistoricoGradeMigradaEquivalente(matrPerTurDisc, novaMediaCalculada, situacao, usuario));
		}
		// getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().incluirMatriculaPeriodoTurmaDisciplinasTransferenciaGradeCurricular(matrPer.getCodigo(),
		// matricula, listaDisciplinaGradeMigrarVOs, novaMediaCalculada,
		// novaFrequenciaCalculada, situacao, 0, "", "", "", 0, 0, 0,
		// configuracaoFinanceiroVO, usuario);
	}

	@Deprecated
	public void criarMatriculaPeriodoTurmaDisciplinasParaDisciplinasReprovadas(MatriculaPeriodoVO matrPer, String matricula, List<DisciplinaVO> listaDisciplinaGradeOrigemVOs, List<MatriculaPeriodoTurmaDisciplinaVO> listaDisciplinaGradeMigrarVOs, Boolean usarSituacaoAprovadoAproveitamento, List<HistoricoGradeMigradaEquivalenteVO> listaHistoricoGradeMigrarEquivalenteVOs, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		// Double mediaAnterior = 0.0;
		// Double frequenciaAnterior = 0.0;
		// String situacao = "";
		// for (DisciplinaVO disc : listaDisciplinaGradeOrigemVOs) {
		// List<HistoricoVO> hist =
		// getFacadeFactory().getHistoricoFacade().consultarPorMatriculaDisciplinaSituacao(matricula,
		// disc.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
		// configuracaoFinanceiroVO, usuario);
		// for (HistoricoVO h : hist) {
		// if (h.getSituacao().equals("TR")) {
		// throw new Exception("A disciplina " + h.getDisciplina().getNome() +
		// ", encontra-se com a situação trancada. Por isso não é possível realizar equivalencia para a mesma.");
		// }
		// // matrPer = h.getMatriculaPeriodo();
		// matrPer =
		// getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaPrimeiraMatriculaPeriodoPorMatricula(matricula,
		// false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		// if (h.getSituacao().equals("RE") || h.getSituacao().equals("RF")) {
		// if (h.getMediaFinal() != null && mediaAnterior.equals(0.0)) {
		// mediaAnterior = Uteis.arredondar(h.getMediaFinal(), 2, 0);
		// frequenciaAnterior = Uteis.arredondar(h.getFreguencia(), 2, 0);
		// situacao = h.getSituacao();
		// } else {
		// if (h.getMediaFinal() != null && mediaAnterior > h.getMediaFinal()) {
		// mediaAnterior = Uteis.arredondar(h.getMediaFinal(), 2, 0);
		// frequenciaAnterior = Uteis.arredondar(h.getFreguencia(), 2, 0);
		// situacao = h.getSituacao();
		// }
		// }
		// }
		// }
		// }
		// if (usarSituacaoAprovadoAproveitamento) {
		// situacao = "AA";
		// }
		// for (MatriculaPeriodoTurmaDisciplinaVO matrPerTurDisc :
		// listaDisciplinaGradeMigrarVOs) {
		// matrPerTurDisc.setAno(matrPer.getAno());
		// matrPerTurDisc.setSemestre(matrPer.getSemestre());
		// matrPerTurDisc.setMatricula(matricula);
		// matrPerTurDisc.setMatriculaPeriodo(matrPer.getCodigo());
		//
		// listaHistoricoGradeMigrarEquivalenteVOs.add(realizarCriacaoHistoricoGradeMigradaEquivalente(matrPerTurDisc,
		// mediaAnterior, situacao, usuario));
		// }
		// getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().incluirMatriculaPeriodoTurmaDisciplinasTransferenciaGradeCurricular(matrPer.getCodigo(),
		// matricula, listaDisciplinaGradeMigrarVOs, mediaAnterior,
		// frequenciaAnterior, situacao, 0, "", "", "", 0, 0, 0,
		// configuracaoFinanceiroVO, usuario);
	}

	@Deprecated
	public HistoricoGradeMigradaEquivalenteVO realizarCriacaoHistoricoGradeMigradaEquivalente(MatriculaPeriodoTurmaDisciplinaVO mptd, Double mediaFinal, String situacao, UsuarioVO usuarioVO) throws Exception {
		HistoricoGradeMigradaEquivalenteVO obj = new HistoricoGradeMigradaEquivalenteVO();
		obj.setDisciplinaVO(mptd.getDisciplina());
		obj.getMatriculaPeriodoVO().setCodigo(mptd.getMatriculaPeriodo());
		obj.setMatriculaPeriodoApresentarHistoricoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(mptd.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		obj.setSituacao(situacao);
		obj.setMediaFinal(mediaFinal);
		return obj;
	}

	public Boolean verificaSeAlunoAprovadoEmDisciplinasGradeCurricular(String matricula, Integer disciplina, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		List hist = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaDisciplinasAprovadas(matricula, disciplina, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuario);
		if (hist.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(MatriculaVO obj, ProcessoMatriculaCalendarioVO processoMatriculaCalendario, CondicaoPagamentoPlanoFinanceiroCursoVO condicao, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		alterar(obj, null, processoMatriculaCalendario, condicao, configuracaoFinanceiro, null, usuario);
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>MatriculaVO</code>. Sempre utiliza a chave primária da classe como
	 * atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>MatriculaVO</code> que será alterada no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final MatriculaVO obj, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendario, CondicaoPagamentoPlanoFinanceiroCursoVO condicao, ConfiguracaoFinanceiroVO configuracaoFinanceiro, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception {
		alterar(obj, matriculaPeriodoVO, processoMatriculaCalendario, condicao, configuracaoFinanceiro, configuracaoGeralSistema, usuario, true);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final MatriculaVO obj, MatriculaPeriodoVO matriculaPeriodoVO, ProcessoMatriculaCalendarioVO processoMatriculaCalendario, CondicaoPagamentoPlanoFinanceiroCursoVO condicao, ConfiguracaoFinanceiroVO configuracaoFinanceiro, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario, boolean controleAcesso) throws Exception {
		try {
			// // System.out.println("===================:1" + new Date() +
			// "===========================");
			ControleAcesso.alterar(getIdEntidade(), controleAcesso, usuario);
			MatriculaVO.validarDados(obj);
			// getBloqueio().lock();
			// verificarSuspensaoMatricula(obj, configuracaoGeralSistema);
			final Date updated = new Date();
			try {
				// Obj.addObserver(this);
				// obj.setChanged();
				// obj.notifyObservers();
				obj.atualizarSituacaoFinanceira();
//				if (verificarPriorizarConsultorProspect(usuario)) {
//					// obter prospect
//					ProspectsVO p = getFacadeFactory().getProspectsFacade().consultarPorCodigoPessoa(obj.getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
//					if (p.getCodigo().intValue() > 0 && p.getConsultorPadrao().getCodigo().intValue() > 0) {
//						// se possuir obter consultor padrao
//						obj.setConsultor(p.getConsultorPadrao());
//					} else {
//						try {
//							// senao tiver setado o consultor padrao - setar
//							// funcionario logado
//							if (obj.getConsultor().getCodigo().intValue() == 0) {
//								FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(usuario.getPessoa().getCodigo(), false, usuario);
//								obj.setConsultor(func);
//							}
//						} catch (Exception e) {
//							obj.setConsultor(new FuncionarioVO());
//						}
//					}
//				} else {
//					if (obj.getConsultor().getCodigo().intValue() == 0) {
//						try {
//							FuncionarioVO func = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(usuario.getPessoa().getCodigo(), false, usuario);
//							obj.setConsultor(func);
//						} catch (Exception e) {
//							obj.setConsultor(new FuncionarioVO());
//						}
//					}
//				}

				final String sql = "UPDATE Matricula set aluno=?, unidadeEnsino=?, curso=?, data=?, situacao=?, situacaoFinanceira=?, inscricao=?, usuario=?, turno=?, " + "tipoMidiaCaptacao=?, tranferenciaEntrada=?, responsavelLiberacaoDesconto=?, formaingresso=?, programareservavaga=?, financiamentoestudantil=?, " + "apoiosocial=?, atividadecomplementar=?, disciplinasprocseletivo=?, fezenade=?, dataenade=?, notaenade=?, horascomplementares=?, enade=?, nomemonografia=?, " + "alunoAbandonouCurso=?, observacaoComplementar=?, dataInicioCurso=?, dataConclusaoCurso=?, localArmazenamentoDocumentosMatricula=?, notaMonografia=?, " + "updated=?, formacaoAcademica=?, autorizacaoCurso=?, consultor=?, tipoMatricula=?, matriculaSuspensa=?, matriculaSerasa=?, naoEnviarMensagemCobranca = ?, qtdDisciplinasExtensao=?, " + "dataLiberacaoPendenciaFinanceira=?, responsavelLiberacaoPendenciaFinanceira=?, dataColacaograu=?,naoApresentarCenso=?, classificacaoIngresso=?, codigoFinanceiroMatricula=?, titulacaoOrientadorMonografia=?, cargaHorariaMonografia=?  WHERE ((matricula = ?))"
						+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

					public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
						PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
						sqlAlterar.setInt(1, obj.getAluno().getCodigo().intValue());
						sqlAlterar.setInt(2, obj.getUnidadeEnsino().getCodigo().intValue());
						sqlAlterar.setInt(3, obj.getCurso().getCodigo().intValue());
						sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getData()));
						sqlAlterar.setString(5, obj.getSituacao());
						sqlAlterar.setString(6, obj.getSituacaoFinanceira());
						if (obj.getInscricao().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(7, obj.getInscricao().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(7, 0);
						}
						sqlAlterar.setInt(8, obj.getUsuario().getCodigo().intValue());
						sqlAlterar.setInt(9, obj.getTurno().getCodigo().intValue());
						if (obj.getTipoMidiaCaptacao().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(10, obj.getTipoMidiaCaptacao().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(10, 0);
						}
						if (obj.getTransferenciaEntrada().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(11, obj.getTransferenciaEntrada().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(11, 0);
						}
						if (obj.getUsuarioLiberacaoDesconto().getCodigo().intValue() != 0) {
							sqlAlterar.setInt(12, obj.getUsuarioLiberacaoDesconto().getCodigo().intValue());
						} else {
							sqlAlterar.setNull(12, 0);
						}
						sqlAlterar.setString(13, obj.getFormaIngresso());
						sqlAlterar.setString(14, obj.getProgramaReservaVaga());
						if (obj.getFinanciamentoEstudantil() == null) {
							sqlAlterar.setNull(15, 0);
						} else {
							sqlAlterar.setString(15, obj.getFinanciamentoEstudantil());
						}
						sqlAlterar.setString(16, obj.getApoioSocial());
						sqlAlterar.setString(17, obj.getAtividadeComplementar());
						sqlAlterar.setString(18, obj.getDisciplinasProcSeletivo());
						sqlAlterar.setBoolean(19, obj.getFezEnade());
						if (Uteis.isAtributoPreenchido(obj.getDataEnade())) {
							sqlAlterar.setDate(20, Uteis.getDataJDBC(obj.getDataEnade()));
						} else {
							sqlAlterar.setNull(20, 0);
						}

						if (Uteis.isAtributoPreenchido(obj.getNotaEnade())) {
							sqlAlterar.setDouble(21, obj.getNotaEnade());
						} else {
							sqlAlterar.setNull(21, 0);
						}
						if (Uteis.isAtributoPreenchido(obj.getHorasComplementares())) {
							sqlAlterar.setDouble(22, obj.getHorasComplementares());
						} else {
							sqlAlterar.setNull(22, 0);
						}
						if (Uteis.isAtributoPreenchido(obj.getEnadeVO().getCodigo())) {
							sqlAlterar.setInt(23, obj.getEnadeVO().getCodigo());
						} else {
							sqlAlterar.setNull(23, 0);
						}
						sqlAlterar.setString(24, obj.getTituloMonografia());
						sqlAlterar.setBoolean(25, obj.getAlunoAbandonouCurso());
						sqlAlterar.setString(26, obj.getObservacaoComplementar());
						if (Uteis.isAtributoPreenchido(obj.getDataInicioCurso())) {
							sqlAlterar.setDate(27, Uteis.getDataJDBC(obj.getDataInicioCurso()));
						} else {
							sqlAlterar.setNull(27, 0);
						}
						if (Uteis.isAtributoPreenchido(obj.getDataConclusaoCurso())) {
							sqlAlterar.setDate(28, Uteis.getDataJDBC(obj.getDataConclusaoCurso()));
						} else {
							sqlAlterar.setNull(28, 0);
						}
						sqlAlterar.setString(29, obj.getLocalArmazenamentoDocumentosMatricula());
						if (obj.getNotaMonografia() == null) {
							sqlAlterar.setNull(30, 0);
						} else {
							sqlAlterar.setDouble(30, obj.getNotaMonografia());
						}
						sqlAlterar.setTimestamp(31, Uteis.getDataJDBCTimestamp(updated));
						if (obj.getFormacaoAcademica().getCodigo().intValue() == 0) {
							sqlAlterar.setNull(32, 0);
						} else {
							sqlAlterar.setInt(32, obj.getFormacaoAcademica().getCodigo().intValue());
						}
						if (obj.getAutorizacaoCurso().getCodigo().intValue() == 0) {
							sqlAlterar.setNull(33, 0);
						} else {
							sqlAlterar.setInt(33, obj.getAutorizacaoCurso().getCodigo().intValue());
						}
						if (obj.getConsultor().getCodigo().intValue() == 0) {
							sqlAlterar.setNull(34, 0);
						} else {
							sqlAlterar.setInt(34, obj.getConsultor().getCodigo().intValue());
						}
						sqlAlterar.setString(35, obj.getTipoMatricula());
						if (obj.getMatriculaSuspensa() != null) {
							sqlAlterar.setBoolean(36, obj.getMatriculaSuspensa());
						} else {
							sqlAlterar.setNull(36, 0);
						}
						if (obj.getMatriculaSerasa() != null) {
							sqlAlterar.setBoolean(37, obj.getMatriculaSerasa());
						} else {
							sqlAlterar.setNull(37, 0);
						}
						sqlAlterar.setBoolean(38, obj.getNaoEnviarMensagemCobranca());
						sqlAlterar.setInt(39, obj.getQtdDisciplinasExtensao());
						if (obj.getDataLiberacaoPendenciaFinanceira() != null) {
							sqlAlterar.setDate(40, Uteis.getDataJDBC(obj.getDataLiberacaoPendenciaFinanceira()));
						} else {
							sqlAlterar.setNull(40, 0);
						}
						sqlAlterar.setInt(41, obj.getResponsavelLiberacaoPendenciaFinanceira().getCodigo().intValue());
						if (obj.getDataColacaoGrau() != null) {
							sqlAlterar.setDate(42, Uteis.getDataJDBC(obj.getDataColacaoGrau()));
						} else {
							sqlAlterar.setNull(42, 0);
						}
						sqlAlterar.setBoolean(43, obj.getNaoApresentarCenso());
                        if (obj.getClassificacaoIngresso() != null) {
                        	sqlAlterar.setInt(44, obj.getClassificacaoIngresso());
                        } else {
                        	sqlAlterar.setNull(44, 0);
                        }
                        sqlAlterar.setString(45, obj.getCodigoFinanceiroMatricula());
                        sqlAlterar.setString(46, obj.getTitulacaoOrientadorMonografia());
                        sqlAlterar.setInt(47, obj.getCargaHorariaMonografia());

						sqlAlterar.setString(48, obj.getMatricula());
						return sqlAlterar;
					}
				});
				// // System.out.println("===================:2" + new Date() +
				// "===========================");
				obj.getPlanoFinanceiroAluno().setMatricula(obj.getMatricula());

				// TODO ((SEI CA37.1)) Modificado para levar em conta o código
				// da matricula periodo, sempre irá ser criado um novo
				// registro de plano financeiro aluno se a matrícula periodo for
				// diferente.
				// getFacadeFactory().getPlanoFinanceiroAlunoFacade().persistir(obj.getPlanoFinanceiroAluno());

				// // System.out.println("===================:3" + new Date() +
				// "===========================");
				getFacadeFactory().getDocumetacaoMatriculaFacade().alterarDocumetacaoMatriculas(obj, usuario, configuracaoGeralSistema ,true);
				// // System.out.println("===================:4" + new Date() +
				// "===========================");
				boolean novaMatriculaPeriodo = matriculaPeriodoVO.getCodigo().equals(0);
				if (matriculaPeriodoVO != null) {
					// Quando é fornecido um objeto especifico da
					// matriculaPeriodoVO,
					// então a rotina,
					// trata de processar somente este matriculaPeriodoVO em
					// especial.
					// Isto é importante,
					// pois um curso pode ter 10 periodos letivos diferentes,
					// que não
					// precisam ser alterados
					// mais, mas que seriam re-processados por esta rotina de
					// forma
					// desnecessaria, haja vista,
					// que na maioria das vezes, deseja-se editar / incluir um
					// único
					// período.
					getFacadeFactory().getMatriculaPeriodoFacade().alterarMatriculaPeriodoVOEspecifico(matriculaPeriodoVO, obj, processoMatriculaCalendario, condicao, configuracaoFinanceiro, usuario);
				} else {
					getFacadeFactory().getMatriculaPeriodoFacade().alterarMatriculaPeriodos(obj, processoMatriculaCalendario, condicao, configuracaoFinanceiro, usuario);
				}
				// getFacadeFactory().getPlanoFinanceiroAlunoLogFacade().realizarCriacaoLogPlanoFinanceiroAluno(obj,
				// matriculaPeriodoVO, usuario);
//				if ((matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getRegerarFinanceiro() && !matriculaPeriodoVO.getCodigo().equals(0))
//						|| matriculaPeriodoVO.getVindoTelaAlteracaoPlanoFinanceiroAluno()
//						|| novaMatriculaPeriodo
//						) {
//					getFacadeFactory().getPlanoFinanceiroAlunoFacade().persistirLevandoEmContaMatriculaPeriodo(obj.getPlanoFinanceiroAluno(), matriculaPeriodoVO.getCodigo());
//					regerarBoletos(matriculaPeriodoVO, configuracaoFinanceiro, usuario);
//				}
				if(obj.getAluno().getRenovacaoAutomatica()) {
					getFacadeFactory().getPessoaFacade().executarAtualizacaoDadosPessoaRenovacaoAutomatica(obj.getAluno(), usuario);
				}
//				incluirLogMatricula(obj, matriculaPeriodoVO, usuario);
				obj.setUpdated(updated);
				//getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemConfirmacaoMatricula(obj, usuario);
//				getFacadeFactory().getProspectsFacade().realizarVinculoUnidadeEnsinoProspectSemUnidadeEnsinoPorPessoa(obj.getAluno().getCodigo(), obj.getUnidadeEnsino().getCodigo(), usuario);
			} finally {
				// obj.deleteObserver(this);
				// getBloqueio().unlock();
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarAlteracaoNumeroMatriculaManualmente(MatriculaVO matricula , String novaMatricula, OperacaoFuncionalidadeVO operacao, UsuarioVO usuario) throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(novaMatricula), "O campo nova matrícula deve ser informado.");
		getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(operacao);
		StringBuilder sqlStr =  new StringBuilder("select fn_alterarmatriculaaluno ('");
		sqlStr.append(matricula.getMatricula()).append("','");
		sqlStr.append(novaMatricula.trim()).append("');");
		getConexao().getJdbcTemplate().execute(sqlStr.toString());
		matricula.setMatricula(novaMatricula.trim());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarGradeCurricularAtual(String matricula, Integer codigoGradeCurricularAtual) throws Exception {
		String sqlStr = "UPDATE Matricula set gradecurricularatual=? WHERE ((matricula = ?))";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { codigoGradeCurricularAtual, matricula });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoMatricula(String matricula, String situacao, UsuarioVO usuario) throws Exception {
		Date updated = null;
		// getBloqueio().lock();
		updated = new Date();
		try {
			MatriculaVO matriculaVO =  new MatriculaVO();
			matriculaVO.setMatricula(matricula);
			carregarDados(matriculaVO, usuario);
			if (situacao.equals("FO")) {
				if(!Uteis.isAtributoPreenchido(matriculaVO.getDataConclusaoCurso())) {
					matriculaVO.setDataConclusaoCurso(consultarDataConclusaoCursoPorMatricula(matricula));
				}
				if(!Uteis.isAtributoPreenchido(matriculaVO.getDataConclusaoCurso())) {
					String sqlStr = "UPDATE Matricula set situacao=?, updated=?, responsavelatualizacaomatriculaformada= ?, dataAtualizacaoMatriculaFormada = ? WHERE ((matricula = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
					getConexao().getJdbcTemplate().update(sqlStr, new Object[] { situacao, Uteis.getDataJDBCTimestamp(updated), usuario.getCodigo(), 
							Uteis.getDataJDBCTimestamp(updated), matricula });
				}else {
					String sqlStr = "UPDATE Matricula set situacao=?, updated=?, responsavelatualizacaomatriculaformada= ?, dataAtualizacaoMatriculaFormada = ?, dataconclusaocurso=?, anoConclusao=?, semestreConclusao=? WHERE ((matricula = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
					getConexao().getJdbcTemplate().update(sqlStr, new Object[] { situacao, Uteis.getDataJDBCTimestamp(updated), usuario.getCodigo(), 
						Uteis.getDataJDBCTimestamp(updated), Uteis.getDataJDBC(matriculaVO.getDataConclusaoCurso()), String.valueOf(Uteis.getAnoData(matriculaVO.getDataConclusaoCurso())), Uteis.getSemestreData(matriculaVO.getDataConclusaoCurso()),  matricula });
				}
				getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodoFormada(matriculaVO, "FO", usuario);
				getFacadeFactory().getFormacaoAcademicaFacade().incluirFormacaoAcademicaMatricula(consultaRapidaPorMatriculaUnica(matricula, 0, false, null), usuario);
				realizarInativacaoCredenciasAlunosFormados(matriculaVO, usuario);
			}else {
				String sqlStr = "UPDATE Matricula set situacao=?, updated=? WHERE ((matricula = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
				getConexao().getJdbcTemplate().update(sqlStr, new Object[] { situacao, Uteis.getDataJDBCTimestamp(updated), matricula });	
				if(matriculaVO.getSituacao().equals("FO") && situacao.equals("AT")) {
					realizarReativacaoCredenciasAlunosFormadosParaAtivo(matriculaVO, usuario);
				}
			}
		} finally {
			updated = null;
			// getBloqueio().unlock();
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTituloNotaMonogradia(String matricula, String titulo, Double nota, UsuarioVO usuarioVO) throws Exception {
		try {

			String sqlStr = "UPDATE Matricula set nomeMonografia=?, notaMonografia=? WHERE (matricula = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sqlStr, new Object[] { titulo, nota, matricula });
		} finally {
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void registrarAdiarBloqueio(MatriculaVO matricula) throws Exception {
		Integer diasAvancar = 30;
		try {
			Date dataUltimaAula = getFacadeFactory().getHorarioTurmaFacade().consultarUltimaDataAulaPorMatriculaConsiderandoReposicao(matricula.getMatricula());
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSLOGIN, null, null);
			if (dataUltimaAula != null) {

				Date dataFutura = Uteis.obterDataAvancada(dataUltimaAula, config.getQtdDiasAcessoAlunoAtivo());
				if (dataFutura.before(new Date())) {
					Long quantidadeDiasEntreDatas = Uteis.nrDiasEntreDatas(new Date(), dataFutura);
					diasAvancar = diasAvancar + quantidadeDiasEntreDatas.intValue();
					//matricula.setQtdDiasAdiarBloqueio(0);
				}
			}
			String sqlStr = "UPDATE Matricula set qtdDiasAdiarBloqueio=? WHERE ((matricula = ?))";
			getConexao().getJdbcTemplate().update(sqlStr, new Object[] { (diasAvancar), matricula.getMatricula() });
		} finally {
			//matricula.setQtdDiasAdiarBloqueio(0);
			matricula.setQtdDiasAdiarBloqueio(diasAvancar);
			// getBloqueio().unlock();
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarObservacaoComplementar(final MatriculaVO obj, final Integer gradecurricular, final String observacaoComplementar ,UsuarioVO usuarioLogado) throws Exception {
		try {

			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					String sqlStr = "UPDATE observacaocomplementarhistoricoaluno set observacao = ? where matricula = ? and gradecurricular = ?" +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);

					PreparedStatement sqlInserir = arg0.prepareStatement(sqlStr);
					sqlInserir.setString(1, observacaoComplementar);
					sqlInserir.setString(2, obj.getMatricula());
					sqlInserir.setInt(3, gradecurricular);
					return sqlInserir;
				}
			}) == 0) {

				getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

					public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
						String sqlStr = "INSERT INTO observacaocomplementarhistoricoaluno (matricula, gradecurricular, observacao) VALUES (?, ?, ?)"  +adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
						PreparedStatement sqlInserir = arg0.prepareStatement(sqlStr);
						sqlInserir.setString(1, obj.getMatricula());
						sqlInserir.setInt(2, gradecurricular);
						sqlInserir.setString(3, observacaoComplementar);
						return sqlInserir;
					}
				});
			}
			;

		} finally {
		}
	}

	public ObservacaoComplementarHistoricoAlunoVO consultarObservacaoComplementarMatricula(MatriculaVO obj, Integer gradecurricular) {
		try {
			StringBuilder sqlStr = new StringBuilder(" SELECT * FROM observacaocomplementarhistoricoaluno ");
			sqlStr.append(" WHERE (observacaocomplementarhistoricoaluno.gradecurricular = ").append(gradecurricular).append(")");
			sqlStr.append(" AND (observacaocomplementarhistoricoaluno.matricula = '").append(obj.getMatricula()).append("')");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (tabelaResultado.next()) {
				ObservacaoComplementarHistoricoAlunoVO observacao = new ObservacaoComplementarHistoricoAlunoVO();
				observacao.setCodigo(tabelaResultado.getInt("codigo"));
				observacao.setMatricula(tabelaResultado.getString("matricula"));
				observacao.setGradeCurricular(tabelaResultado.getInt("gradecurricular"));
				observacao.setObservacao(tabelaResultado.getString("observacao"));
				observacao.setObservacaoTransferenciaMatrizCurricular(tabelaResultado.getString("observacaoTransferenciaMatrizCurricular"));
				observacao.setNovoObj(new Boolean(false));
				return observacao;
			} else {
				return new ObservacaoComplementarHistoricoAlunoVO();
			}
		} catch (Exception e) {
			return new ObservacaoComplementarHistoricoAlunoVO();
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoMatriculaParaIndicarAbandonoCurso(String matricula, Boolean situacaoAbandono) throws Exception {
		final Date updated = new Date();
		// getBloqueio().lock();
		try {
			String sqlStr = "UPDATE Matricula set alunoAbandonouCurso=?, updated=? WHERE ((matricula = ?))";
			getConexao().getJdbcTemplate().update(sqlStr, new Object[] { situacaoAbandono, Uteis.getDataJDBCTimestamp(updated), matricula });
		} finally {
			// updated = null;
			// getBloqueio().unlock();
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoMatriculaFormadaAtualizacao(final MatriculaVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			if(!Uteis.isAtributoPreenchido(obj.getDataConclusaoCurso())) {
				obj.setDataConclusaoCurso(consultarDataConclusaoCursoPorMatricula(obj.getMatricula()));
			}
			final String sql = "UPDATE Matricula set situacao=?, dataAtualizacaoMatriculaFormada=?, responsavelAtualizacaoMatriculaFormada=?, dataconclusaocurso=?, anoConclusao=?, semestreConclusao=? WHERE ((matricula = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(obj.getResponsavelAtualizacaoMatriculaFormada());
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, "FO");
					sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getDataAtualizacaoMatriculaFormada()));
					sqlAlterar.setInt(3, obj.getResponsavelAtualizacaoMatriculaFormada().getCodigo());
					if (Uteis.isAtributoPreenchido(obj.getDataConclusaoCurso())) {
						sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataConclusaoCurso()));
					}else {
						sqlAlterar.setNull(4, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataConclusaoCurso())) {
						sqlAlterar.setString(5, String.valueOf(Uteis.getAnoData(obj.getDataConclusaoCurso())));
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataConclusaoCurso())) {
						sqlAlterar.setString(6, Uteis.getSemestreData(obj.getDataConclusaoCurso()));
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setString(7, obj.getMatricula());

					return sqlAlterar;
				}
			});

			getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodoFormada(obj, "FO", usuarioVO);
			getFacadeFactory().getFormacaoAcademicaFacade().incluirFormacaoAcademicaMatricula(consultaRapidaPorMatriculaUnica(obj.getMatricula(), 0, false, null), obj.getResponsavelAtualizacaoMatriculaFormada());
			realizarInativacaoCredenciasAlunosFormados(obj, usuarioVO);
			obj.setSituacao("FO");
//			UsuarioVO usuarioAlunoVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(obj.getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, obj.getResponsavelAtualizacaoMatriculaFormada());
//			if(Uteis.isAtributoPreenchido(usuarioAlunoVO)) {
//				JobExecutarSincronismoComLdapAoCancelarTransferirMatricula jobExecutarSincronismoComLdapAoCancelarTransferirMatricula = new JobExecutarSincronismoComLdapAoCancelarTransferirMatricula(usuarioAlunoVO, obj, false);
//				Thread jobSincronizarCancelamento = new Thread(jobExecutarSincronismoComLdapAoCancelarTransferirMatricula);
//				jobSincronizarCancelamento.start();
//			}			
//			if ( Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoSeiBlackboardVO())) {
//				PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO =  getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(obj.getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, obj.getResponsavelAtualizacaoMatriculaFormada());
//				if(Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO)) {
//					if(!Uteis.isAtributoPreenchido(obj.getCurso().getConfiguracaoLdapVO())) {
//						obj.getCurso().setConfiguracaoLdapVO(getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorPessoa(obj.getAluno().getCodigo()));
//					}
//					if (!getFacadeFactory().getMatriculaFacade().verificarExisteMultiplasMatriculasAtivasDominioLDAP(obj, usuarioAlunoVO)) {
//						getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarInativacaoPessoaBlack(pessoaEmailInstitucionalVO.getEmail(), usuarioVO);
//					}
//					if(!Uteis.isAtributoPreenchido(obj.getSalaAulaBlackboardVO().getCodigo())) {
//						obj.setSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardPorMatriculaPorTipoSalaBlackboard(obj.getMatricula(), TipoSalaAulaBlackboardEnum.ESTAGIO, usuarioVO));
//					}
//					if(Uteis.isAtributoPreenchido(obj.getSalaAulaBlackboardVO().getCodigo())) {
//						getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().excluirPessoaSalaBlack(obj.getSalaAulaBlackboardVO().getCodigo(), obj.getAluno().getCodigo(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, pessoaEmailInstitucionalVO.getEmail(), usuarioVO);
//					}
//				}			
//			}
		} catch (Exception e) {
			throw e;
		} finally {
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoMatriculaVOParaAtivada(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		try {
			Matricula.alterar(getIdEntidade());
			final Date updated = new Date();
			matriculaPeriodoVO.setSituacaoMatriculaPeriodo("AT");
			if (matriculaVO.getSituacao().equals("PR")) {
				// como estamos ativando a matricula periodo, temos que refletir
				// esta acao para a
				// a matriculaVO, pois a mesma, não pode ficar como
				// pré-matriculada, que e uma situacao inicial
				matriculaVO.setSituacao("AT");
			}

			String sql = "UPDATE Matricula set situacao=?, updated = ? WHERE ((matricula = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { matriculaVO.getSituacao(), Uteis.getDataJDBCTimestamp(updated), matriculaVO.getMatricula() });
			getFacadeFactory().getMatriculaPeriodoFacade().alterarSituacaoMatriculaPeriodoComUsuarioResponsavel( matriculaPeriodoVO, usuario);
			incluirLogPreMatricula(matriculaVO, matriculaPeriodoVO, "Ativação Pré-Matrícula", usuario);
			
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoFinanceiraMatricula(String matricula, String situacaoFinanceira) throws Exception {
		String sql = "UPDATE Matricula set situacaoFinanceira=? WHERE ((matricula = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { situacaoFinanceira, matricula });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarNaoEnviarMensagemCobranca(String matricula, Boolean naoEnviarMensagemCobranca) throws Exception {
		String sql = "UPDATE Matricula set naoEnviarMensagemCobranca=? WHERE ((matricula = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { naoEnviarMensagemCobranca, matricula });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarAlunoConcluiuDisciplinasRegulares(String matricula, Boolean alunoConcluiuDisciplinasRegulares) throws Exception {
		String sql = "UPDATE Matricula set dataAlunoConcluiuDisciplinasRegulares = ?, alunoConcluiuDisciplinasRegulares=? WHERE ((matricula = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { alunoConcluiuDisciplinasRegulares ? Uteis.getDataJDBC(new Date()) : null, alunoConcluiuDisciplinasRegulares, matricula });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarConsultorResponsavelMatricula(String matricula, Integer consultorSubstituido, FuncionarioVO consultorSubstituto, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE Matricula set consultor=? WHERE ((matricula = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { consultorSubstituto.getCodigo(), matricula });
		incluirLogAlteracaoConsultorMatricula(matricula, usuario.getCodigo(), consultorSubstituido, consultorSubstituto.getPessoa().getCodigo(), new Date(), usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataLiberacaoPendenciaFinanceira(String matricula, Date dataLiberacao, Integer usuarioResp, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE Matricula set dataLiberacaoPendenciaFinanceira=?, responsavelLiberacaoPendenciaFinanceira=? WHERE ((matricula = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { dataLiberacao, usuarioResp, matricula });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataNotificacaoAlunoInadimplente(String matricula) throws Exception {
		String sql = "UPDATE Matricula set dataNotifcAlunoInadimplente=? WHERE (matricula = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { new Date(), matricula });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataPrimeiraNotificacaoAlunoInadimplente(String matricula) throws Exception {
		String sql = "UPDATE Matricula set dataPrimeiraNotInadimplencia=? WHERE (matricula = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { new Date(), matricula });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataSegundaNotificacaoAlunoInadimplente(String matricula) throws Exception {
		String sql = "UPDATE Matricula set dataSegundaNotInadimplencia=? WHERE (matricula = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { new Date(), matricula });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataTerceiraNotificacaoAlunoInadimplente(String matricula) throws Exception {
		String sql = "UPDATE Matricula set dataTerceiraNotInadimplencia=? WHERE (matricula = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { new Date(), matricula });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarDataQuartaNotificacaoAlunoInadimplente(String matricula) throws Exception {
		String sql = "UPDATE Matricula set dataQuartaNotInadimplencia=? WHERE (matricula = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { new Date(), matricula });
	}

	public void validarDadosAlteracaoConsultorMatricula(String matricula) throws Exception {
		if (matricula.equals("")) {
			throw new Exception("O campo MATRÍCULA deve ser informado.");
		}
	}

	public void incializarDadosAPartirTransferenciaEntrada(MatriculaVO matricula, TransferenciaEntradaVO obj, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception {
		matricula.inicializarDados();
		UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(obj.getCurso().getCodigo(), obj.getUnidadeEnsino().getCodigo(), obj.getTurno().getCodigo(), usuario);
		if (unidadeEnsinoCursoVO.getCodigo().equals(0)) {
			throw new Exception("Não foi encontrado o curso informado na Transferência de Entrada para esta Unidade/Turno - Desejados.");
		}
		matricula.setAluno(obj.getPessoa());
		matricula.setCurso(obj.getCurso());
		matricula.setUnidadeEnsino(obj.getUnidadeEnsino());
		matricula.setTurno(obj.getTurno());
		matricula.setFormaIngresso(FormaIngresso.TRANSFERENCIA_EXTERNA.getValor());
		matricula.setGradeCurricularAtual(obj.getGradeCurricular());
		matriculaPeriodoVO.setTurma(obj.getTurma());
		matriculaPeriodoVO.setGradeCurricular(obj.getGradeCurricular());
		matriculaPeriodoVO.setPeridoLetivo(obj.getPeridoLetivo());
		matriculaPeriodoVO.setTranferenciaEntrada(obj.getCodigo());
		matriculaPeriodoVO.setUnidadeEnsinoCurso(unidadeEnsinoCursoVO.getCodigo());
		matriculaPeriodoVO.setGradeCurricular(obj.getGradeCurricular());
		matricula.setTipoMidiaCaptacao(obj.getTipoMidiaCaptacao());
		matricula.setTransferenciaEntrada(obj);
		obj.setUnidadeEnsinoCurso(unidadeEnsinoCursoVO.getCodigo());
		inicializarDocumentacaoMatriculaCurso(matricula, usuario);
	}

	public void inicializarDocumentacaoMatriculaCurso(MatriculaVO matricula, UsuarioVO usuario) throws Exception {
		HashMap<Integer, List<DocumetacaoMatriculaVO>> hashDocumentacaoUtilizada = new HashMap<Integer, List<DocumetacaoMatriculaVO>>(0);
		HashMap<Integer, DocumetacaoMatriculaVO> hashDocumentacaoCurso = new HashMap<Integer, DocumetacaoMatriculaVO>(0);
		for (DocumetacaoMatriculaVO documetacaoMatriculaExistente : matricula.getDocumetacaoMatriculaVOs()) {
			if(!hashDocumentacaoUtilizada.containsKey(documetacaoMatriculaExistente.getTipoDeDocumentoVO().getCodigo()) || (Uteis.isAtributoPreenchido(documetacaoMatriculaExistente.getArquivoVO()) || Uteis.isAtributoPreenchido(documetacaoMatriculaExistente.getArquivoVOVerso()))) {
				hashDocumentacaoUtilizada.put(documetacaoMatriculaExistente.getTipoDeDocumentoVO().getCodigo(), new ArrayList<DocumetacaoMatriculaVO>());
			}
			hashDocumentacaoUtilizada.get(documetacaoMatriculaExistente.getTipoDeDocumentoVO().getCodigo()).add(documetacaoMatriculaExistente);
		}
		matricula.setDocumetacaoMatriculaVOs(new ArrayList(0));
		List resultadoConsulta = DocumentacaoCurso.consultarDocumentacaoCursos(matricula, false, usuario);
		Iterator i = resultadoConsulta.iterator();
		while (i.hasNext()) {
			DocumentacaoCursoVO documentacaoCursoVO = (DocumentacaoCursoVO) i.next();
			DocumetacaoMatriculaVO documentacaoMatriculaVO = new DocumetacaoMatriculaVO();
			documentacaoMatriculaVO.setTipoDeDocumentoVO(documentacaoCursoVO.getTipoDeDocumentoVO());
			if (hashDocumentacaoUtilizada.containsKey(documentacaoCursoVO.getTipoDeDocumentoVO().getCodigo())) {
				for(DocumetacaoMatriculaVO documetacaoMatriculaVO : hashDocumentacaoUtilizada.get(documentacaoCursoVO.getTipoDeDocumentoVO().getCodigo())) {
					documetacaoMatriculaVO.setGerarSuspensaoMatricula(documentacaoCursoVO.getGerarSuspensaoMatricula());
					matricula.adicionarObjDocumetacaoMatriculaVOs(documetacaoMatriculaVO);
					hashDocumentacaoCurso.put(documentacaoMatriculaVO.getTipoDeDocumentoVO().getCodigo(), documetacaoMatriculaVO);
				}
			} else {
				if (documentacaoCursoVO.getTipoDeDocumentoVO().getTipoExigenciaDocumento() != null && documentacaoCursoVO.getTipoDeDocumentoVO().getTipoExigenciaDocumento().equals(TipoExigenciaDocumentoEnum.EXIGENCIA_ALUNO)) {
					DocumetacaoMatriculaVO docMatExistente = getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumentacaoMatriculaExigenciaAlunoASerReaproveitado(matricula.getAluno().getCodigo(), documentacaoCursoVO.getTipoDeDocumentoVO().getCodigo(), usuario);
					documentacaoMatriculaVO.setSituacao(docMatExistente.getSituacao());
					if (docMatExistente.getSituacao().equals("OK")) {
						documentacaoMatriculaVO.setEntregue(Boolean.TRUE);
					} else {
						documentacaoMatriculaVO.setEntregue(Boolean.FALSE);
					}
					documentacaoMatriculaVO.setArquivoVO(docMatExistente.getArquivoVO());
					documentacaoMatriculaVO.setArquivoVOVerso(docMatExistente.getArquivoVOVerso());
					documentacaoMatriculaVO.setArquivoVOAssinado(docMatExistente.getArquivoVOAssinado());
					documentacaoMatriculaVO.setUsuario(docMatExistente.getUsuario());
					documentacaoMatriculaVO.setDataEntrega(docMatExistente.getDataEntrega());

				} else {
					documentacaoMatriculaVO.setSituacao("PE");
					documentacaoMatriculaVO.setEntregue(Boolean.FALSE);
				}
			documentacaoMatriculaVO.setGerarSuspensaoMatricula(documentacaoCursoVO.getGerarSuspensaoMatricula());
			matricula.adicionarObjDocumetacaoMatriculaVOs(documentacaoMatriculaVO);
			hashDocumentacaoCurso.put(documentacaoMatriculaVO.getTipoDeDocumentoVO().getCodigo(), documentacaoMatriculaVO);
		}
		}
		/**
		 * Resposável por manter os documentos que já foram entregues e que foram removidos do curso.
		 */
		for (Integer documentacaoUtilizada : hashDocumentacaoUtilizada.keySet()) {
			for(DocumetacaoMatriculaVO docMatUtilizada : hashDocumentacaoUtilizada.get(documentacaoUtilizada)) {
			if (!hashDocumentacaoCurso.containsKey(documentacaoUtilizada) && docMatUtilizada.getEntregue()) {
				matricula.adicionarObjDocumetacaoMatriculaVOs(docMatUtilizada);
			}
		}
		}
		Ordenacao.ordenarLista(matricula.getDocumetacaoMatriculaVOs(), "tipoDeDocumentoVO.nome");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMatriculaSuspensaoDocumentacaoMatricula(final MatriculaVO obj) throws Exception {
		Boolean suspenderMatricula = getFacadeFactory().getDocumetacaoMatriculaFacade().verificaAlunoDevendoDocumentoQueSuspendeMatricula(obj.getMatricula());
		if (!suspenderMatricula) {
			// obj.setDataBaseSuspensao(null);
			obj.setMatriculaSuspensa(false);
			// obj.setDataEnvioNotificacao1(null);
			// obj.setDataEnvioNotificacao2(null);
			// obj.setDataEnvioNotificacao3(null);
			this.alterarMatriculaSuspensao(obj);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMatriculaSuspensao(final MatriculaVO obj) throws Exception {
		final StringBuilder sql = new StringBuilder();
		if (obj.getMatriculaSuspensa()) {
			sql.append("UPDATE Matricula set matriculaSuspensa=?, motivoMatriculaBloqueada=?, responsavelSuspensaoMatricula=?, dataBaseSuspensao=?,  "
					+ " qtdDiasAdiarBloqueio=?, motivoAdiamentoSuspensaoMatricula=?, responsavelAdiamentoSuspensaoMatricula=?, dataAdiamentoSuspensaoMatricula=?"
					+ "WHERE (matricula = ?)");

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setBoolean(1, obj.getMatriculaSuspensa());
					sqlAlterar.setString(2, obj.getMotivoMatriculaBloqueada());
					if (!obj.getResponsavelSuspensaoMatricula().getCodigo().equals(0)) {
						sqlAlterar.setInt(3, obj.getResponsavelSuspensaoMatricula().getCodigo());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setTimestamp(4, Uteis.getDataJDBCTimestamp(new Date()));

					//Anula os Dados do Adiamento
					sqlAlterar.setInt(5, 0);
					sqlAlterar.setNull(6, 0);
					sqlAlterar.setNull(7, 0);
					sqlAlterar.setNull(8, 0);

					sqlAlterar.setString(9, obj.getMatricula());
					return sqlAlterar;
				}
			});

		} else {
			sql.append("UPDATE Matricula set matriculaSuspensa=?, motivoCancelamentoSuspensaoMatricula=?, responsavelCancelamentoSuspensaoMatricula=?, dataCancelamentoSuspensaoMatricula=?, bloqueioPorSolicitacaoLiberacaoMatricula =? WHERE (matricula = ?)");

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setBoolean(1, obj.getMatriculaSuspensa().booleanValue());
					sqlAlterar.setString(2, obj.getMotivoCancelamentoSuspensaoMatricula());
					if (!obj.getResponsavelCancelamentoSuspensaoMatricula().getCodigo().equals(0)) {
						sqlAlterar.setInt(3, obj.getResponsavelCancelamentoSuspensaoMatricula().getCodigo());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setTimestamp(4, Uteis.getDataJDBCTimestamp(new Date()));
					sqlAlterar.setBoolean(5, obj.getBloqueioPorSolicitacaoLiberacaoMatricula());
					sqlAlterar.setString(6, obj.getMatricula());

					return sqlAlterar;
				}
			});

		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMatriculaSerasa(final MatriculaVO obj, final UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE Matricula set matriculaSerasa=?, naoEnviarMensagemCobranca=?, dataAlteracaoMatriculaSerasa=?, usuarioRespAlteracaoMatriculaSerasa=?, matriculaVerificadaSerasa=? WHERE (matricula = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setBoolean(1, obj.getMatriculaSerasa());
				sqlAlterar.setBoolean(2, obj.getNaoEnviarMensagemCobranca());
				sqlAlterar.setDate(3, Uteis.getDataJDBC(obj.getDataAlteracaoMatriculaSerasa()));
				sqlAlterar.setInt(4, usuario.getCodigo());
				sqlAlterar.setBoolean(5, obj.getMatriculaVerificadaSerasa());
				sqlAlterar.setString(6, obj.getMatricula());
				return sqlAlterar;
			}
		});
		boolean alterou = false;
		if (obj.getMatriculaSerasa().booleanValue() && obj.getMatriculaVerificadaSerasa()) {
			alterou = true;
			getFacadeFactory().getContaReceberNegociacaoRecebimentoFacade().alterarSituacaoTodasContaReceberParaNotificacaoSerasaPorMatricula(true, obj.getMatricula(), usuario);
		}
		// campo utilizado para controle da aba que fez a alteração
		String guiaAba = "";
		if (!obj.getGuiaAba().equals("")) {
			guiaAba = obj.getGuiaAba();
		}
		String acao = "Alteração da situação do serasa com os seguintes dados: matriculaserasa =" + obj.getMatriculaSerasa() + "; Não enviar mensagem cobranca = " + obj.getNaoEnviarMensagemCobranca() + "; Data Alteração Matricula Serasa =" + obj.getDataAlteracaoMatriculaSerasa() + "; Usuario =" + usuario.getCodigo() + "; Matricula Verificada Serasa = " + obj.getMatriculaVerificadaSerasa() + "; Alterou situação das contas a receber para notificação serasa por matricula =" + alterou + " - ABA ALTERAÇÃO => " + guiaAba;
		incluirLogMatriculaSerasa(obj, acao, usuario);
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>MatriculaVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>MatriculaVO</code> que será removido no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(MatriculaVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		try {
			Matricula.excluir(getIdEntidade());
			List matriculasPerido = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoPorMatricula(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuario);
			if (!matriculasPerido.isEmpty()) {
				MatriculaPeriodoVO matriculaPeriodo = (MatriculaPeriodoVO) matriculasPerido.get(0);
				getFacadeFactory().getMatriculaDWFacade().incluir(obj.criarMatriculaDW(matriculaPeriodo.getProcessoMatricula(), -1), usuario);
			}
			getFacadeFactory().getDocumetacaoMatriculaFacade().excluirDocumetacaoMatriculas(new ArrayList<DocumetacaoMatriculaVO>(), obj.getMatricula(), usuario);
			getFacadeFactory().getMatriculaPeriodoFacade().excluirMatriculaPeriodos(obj.getMatricula(), configuracaoFinanceiroVO, usuario);
			getFacadeFactory().getPlanoFinanceiroAlunoFacade().excluir(obj.getPlanoFinanceiroAluno());
			if(Uteis.isAtributoPreenchido(obj.getTransferenciaEntrada())) {
				if(!Uteis.isAtributoPreenchido(obj.getTransferenciaEntrada().getCurso().getCodigo())) {
					obj.setTransferenciaEntrada(getFacadeFactory().getTransferenciaEntradaFacade().consultarPorChavePrimaria(obj.getTransferenciaEntrada().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuario));
				}
				if(obj.getCurso().getCodigo().equals(obj.getTransferenciaEntrada().getCurso().getCodigo())) {			
					getFacadeFactory().getEstagioFacade().realizarAproveitamentoEstagioTransferenciaUnidadeEnsino(obj.getTransferenciaEntrada().getMatricula(), obj, usuario);
				}
			}
			String sql = "DELETE FROM Matricula WHERE ((matricula = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getMatricula() });
			UsuarioVO usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(obj.getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			if(Uteis.isAtributoPreenchido(usuarioVO)) {
				JobExecutarSincronismoComLdapAoCancelarTransferirMatricula jobExecutarSincronismoComLdapAoCancelarTransferirMatricula = new JobExecutarSincronismoComLdapAoCancelarTransferirMatricula(usuarioVO, obj, false,usuario);
				Thread jobSincronizarCancelamento = new Thread(jobExecutarSincronismoComLdapAoCancelarTransferirMatricula);
				jobSincronizarCancelamento.start();
			}			
			if ( Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoSeiBlackboardVO())) {
				PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO =  getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(obj.getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, obj.getResponsavelAtualizacaoMatriculaFormada());
				if(Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO)) {
					if(!Uteis.isAtributoPreenchido(obj.getCurso().getConfiguracaoLdapVO())) {
						obj.getCurso().setConfiguracaoLdapVO(getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorPessoa(obj.getAluno().getCodigo()));
					}
					if (!getFacadeFactory().getMatriculaFacade().verificarExisteMultiplasMatriculasAtivasDominioLDAP(obj, usuarioVO)) {
						getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarInativacaoPessoaBlack(pessoaEmailInstitucionalVO.getEmail(), obj.getResponsavelAtualizacaoMatriculaFormada());
					}
				}			
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public void executarDefinarProximoPeriodoLetivoCursar(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuario);
		if (matriculaPeriodoVO == null) {
			throw new Exception("");
		} else {
			if (matriculaPeriodoVO.getSituacaoMatriculaPeriodo().equals("FI")) {
				PeriodoLetivoVO periodoLetivoVO = null;
				if (matriculaVO.getSituacao().equalsIgnoreCase("AT")) {
					if (executarValidarMatriculaAptaAvancarPeriodoLetivo(matriculaVO, matriculaPeriodoVO.getCodigo(), null, usuario)) {
						periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarProximoPeriodoLetivoCurso(matriculaVO.getGradeCurricularAtual().getCodigo(), matriculaVO.getCurso().getCodigo(), matriculaPeriodoVO.getPeridoLetivo().getPeriodoLetivo().intValue() + 1, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
					} else {
						periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarProximoPeriodoLetivoCurso(matriculaVO.getGradeCurricularAtual().getCodigo(), matriculaVO.getCurso().getCodigo(), matriculaPeriodoVO.getPeridoLetivo().getPeriodoLetivo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);

					}
				} else if (matriculaVO.getSituacao().equalsIgnoreCase("TR")) {
					periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarProximoPeriodoLetivoCurso(matriculaVO.getGradeCurricularAtual().getCodigo(), matriculaVO.getCurso().getCodigo(), matriculaPeriodoVO.getPeridoLetivo().getPeriodoLetivo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				}
				if (periodoLetivoVO == null) {
					matriculaPeriodoVO.setPeridoLetivo(new PeriodoLetivoVO());
					throw new Exception("Este aluno já está matriculado no último Periodo Letivo");
				} else {
					matriculaPeriodoVO.setPeridoLetivo(periodoLetivoVO);
					matriculaPeriodoVO.setSituacao("PF");
					matriculaPeriodoVO.setCodigo(0);
					matriculaPeriodoVO.setGradeCurricular(matriculaVO.getGradeCurricularAtual());
					// matriculaPeriodoVO.setContaReceber(0);
					matriculaPeriodoVO.setNrDocumento("");
					matriculaPeriodoVO.setTranferenciaEntrada(0);
					matriculaPeriodoVO.setMatriculaPeriodoTumaDisciplinaVOs(new ArrayList(0));
				}
			} else {
				throw new Exception("Não foi realizado o fechamento periodo letivo deste aluno.");
			}
		}
	}

	public void inicializarDadosRenovacaoMatricula(MatriculaVO matriculaVO, PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		matriculaVO = consultarPorChavePrimaria(matriculaVO.getMatricula(), usuario.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, usuario);
		if (matriculaVO.getSituacao().equalsIgnoreCase("AT") || matriculaVO.getSituacao().equalsIgnoreCase("TR")) {
			executarVerificarPendenciaFinanceira(matriculaVO, configuracaoFinanceiro, usuario);
			if (matriculaVO.getExistePendenciaFinanceira()) {
				return;
			}
			executarDefinarProximoPeriodoLetivoCursar(matriculaVO, matriculaPeriodoVO, configuracaoFinanceiro, usuario);
			planoFinanceiroAlunoVO = getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaMatriculaUnico(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario);
		} else {
			throw new Exception("Esta Matrícula não está apta a ser renovada.");
		}

	}

	public void executarVerificarPendenciaFinanceira(MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		List listaContaReceber2 = getFacadeFactory().getContaReceberFacade().consultarPorPendeciaFinanceiraAluno(matriculaVO.getAluno().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiro, usuario);
		if (listaContaReceber2.isEmpty()) {
			matriculaVO.setListaPendenciaFinanceira(new ArrayList(0));
			return;
		}
		matriculaVO.setListaPendenciaFinanceira(listaContaReceber2);
		PerfilEconomicoVO perfilEconomicoVO = consultarPerfilEconomicoPadrao(matriculaVO, usuario);
		if (perfilEconomicoVO.getCodigo().intValue() != 0) {
			matriculaVO.setPerfilEconomicoVO(perfilEconomicoVO);
			// ConfiguracaoFinanceiroVO obj =
			// getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS,
			// usuario, null);
			ConfiguracaoFinanceiroVO obj = configuracaoFinanceiro;
			matriculaVO.gerarOpcoesPagamento(obj, usuario);
		}
	}

	public PerfilEconomicoVO consultarPerfilEconomicoPadrao(MatriculaVO matriculaVO, UsuarioVO usuario) {
		try {
			Integer codigoPerfilEconomico = matriculaVO.getAluno().getPerfilEconomico().getCodigo();
			if (codigoPerfilEconomico == null || codigoPerfilEconomico.intValue() == 0) {
				codigoPerfilEconomico = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, null).getPerfilEconomicoPadrao().getCodigo();
			}
			PerfilEconomicoVO obj = getFacadeFactory().getPerfilEconomicoFacade().consultarPorChavePrimaria(codigoPerfilEconomico, false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
			codigoPerfilEconomico = null;
			return obj;
		} catch (Exception e) {
			return new PerfilEconomicoVO();
		}
	}

	public boolean executarValidarMatriculaAptaAvancarPeriodoLetivo(MatriculaVO matricula, Integer ultimaMatriculaPeriodo, PeriodoLetivoVO periodoLetivoEstaAvancado, UsuarioVO usuario) throws Exception {
		boolean retorno =  true;
		if (matricula.getCurso().getConfiguracaoAcademico().getNumeroDisciplinaConsiderarReprovadoPeriodoLetivo() > 0 && !matricula.getCurso().getConfiguracaoAcademico().getPermiteEvoluirPeriodoLetivoCasoReprovado()) {
			if(!getFacadeFactory().getHistoricoFacade().executarValidarMatriculaPeriodoExcedeuLimiteMaximoDisciplinaReprovado(matricula.getMatricula(), matricula.getCurso().getConfiguracaoAcademico().getConsiderarDisciplinasReprovadasPeriodosLetivosAnteriores(),  matricula.getCurso().getConfiguracaoAcademico().getNumeroDisciplinaConsiderarReprovadoPeriodoLetivo(), usuario)) {
				return false;
		}
	}
		if(matricula.getCurso().getConfiguracaoAcademico().getControlarAvancoPeriodoPorCreditoOuCH()) {
			if(!matricula.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().validarAlunoAptoAvancarProximoPeriodoLetivo(matricula.getCurso().getConfiguracaoAcademico())) {
				if(!Uteis.isAtributoPreenchido(periodoLetivoEstaAvancado) || (Uteis.isAtributoPreenchido(periodoLetivoEstaAvancado) &&
						!periodoLetivoEstaAvancado.getCodigo().equals(matricula.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().definirPeriodoLetivoEvolucaoAcademicaAluno(matricula.getCurso().getConfiguracaoAcademico()).getCodigo()))){
				return false;
				}
			}
		}
		return retorno;
	}

	public MatriculaVO verificarControleDisciplinaReprovacao(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception {
		if (matriculaVO.getCurso().getConfiguracaoAcademico().getIsPossuiControleDisciplinaReprovacao()) {
			if (!getFacadeFactory().getMatriculaFacade().executarValidarMatriculaAptaAvancarPeriodoLetivo(matriculaVO, matriculaVO.getUltimoMatriculaPeriodoVO().getCodigo(), null, usuario)) {
				if (matriculaVO.getUltimoMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo() > 0) {
					matriculaVO.getUltimoMatriculaPeriodoVO().getPeridoLetivo().setPeriodoLetivo(matriculaVO.getUltimoMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo());
				} else {
					matriculaVO.getUltimoMatriculaPeriodoVO().getPeridoLetivo().setPeriodoLetivo(matriculaVO.getUltimoMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo());
				}
			} else {
				matriculaVO.getUltimoMatriculaPeriodoVO().getPeridoLetivo().setPeriodoLetivo(matriculaVO.getUltimoMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo());
			}
		} else {
			matriculaVO.getUltimoMatriculaPeriodoVO().getPeridoLetivo().setPeriodoLetivo(matriculaVO.getUltimoMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo() + 1);
		}
		return matriculaVO;
	}

	/**
	 * Responsável por realizar uma consulta de <code>Matricula</code> através
	 * do valor do atributo <code>String situacao</code>. Retorna os objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>MatriculaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorSituacao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Matricula WHERE situacao like('" + valorConsulta + "%') ORDER BY situacao";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT * FROM Matricula WHERE situacao like('" + valorConsulta + "%') and unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY situacao";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

//	public List consultarPorFormacaoAcademica(Integer formacaoAcademica, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), false, usuario);
//		String sqlStr = "SELECT * FROM Matricula WHERE formacaoAcademica = " + formacaoAcademica;
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
//	}

//	public List consultarPorAutorizacaoCurso(Integer autorizacaoCurso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
//		ControleAcesso.consultar(getIdEntidade(), false, usuario);
//		String sqlStr = "SELECT * FROM Matricula WHERE autorizacaoCurso = " + autorizacaoCurso;
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
//		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
//	}

	public Boolean consultarPorAutorizacaoCurso(Integer autorizacaoCurso) {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT t.matricula FROM ( ");
		sqlStr.append(" SELECT matricula.matricula FROM Matricula WHERE matricula.autorizacaoCurso = ").append(autorizacaoCurso).append("");
		sqlStr.append(" UNION ");
		sqlStr.append(" SELECT matricula.matricula FROM Matricula WHERE matricula.renovacaoreconhecimento = ").append(autorizacaoCurso).append(" LIMIT 1");
		sqlStr.append(" ) AS t;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	public Integer consultarConsultorMatricula(String matricula) {
		String sqlStr = "SELECT consultor FROM Matricula WHERE matricula = '" + matricula + "' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("consultor");

		}
		return 0;
	}

	public List<MatriculaVO> consultaRapidaAutorizacaoCursoMatricula(Integer autorizacaoCurso,  UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append (" WHERE autorizacaoCurso = ");
		sqlStr.append(autorizacaoCurso);
		sqlStr.append(" or renovacaoreconhecimento = ").append(autorizacaoCurso).append(" ORDER BY pessoa.nome");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		return (montarDadosConsultaBasica(tabelaResultado));
	}

	public MatriculaVO consultarPorAutorizacaoCursoMatricula(Integer autorizacaoCurso, String matricula, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM Matricula WHERE autorizacaoCurso = " + autorizacaoCurso + " and matricula = '" + matricula + "'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados( Matrícula ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public List consultarPorSituacaoFinanceira(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT matricula.matricula, matricula.aluno as aluno, matricula.unidadeEnsino as unidadeEnsino, matricula.curso as curso, matricula.data as data, matricula.dataBaseSuspensa as dataBaseSuspensao, matricula.matriculaSuspensa, matricula.matriculaSerasa, " + " matricula.situacao as situacao, matricula.situacaoFinanceira as situacaoFinanceira, matricula.inscricao as inscricao, matricula.usuario as usuario," + " matricula.turno as turno, matricula.tipoMidiaCaptacao as tipoMidiaCaptacao, matricula.dataLiberacaoPendenciaFinanceira, matricula.responsavelLiberacaoPendenciaFinanceira, matricula.tranferenciaEntrada as tranferenciaEntrada from matricula " + " left join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula where upper (matriculaperiodo.situacao) like('" + valorConsulta + "%') ";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr += " and matricula.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += " ORDER BY matricula.matricula ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public List consultarPorPessoaSituacao(Integer pessoa, String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Matricula WHERE upper (situacao)  = '" + valorConsulta.toUpperCase() + "' and aluno = " + pessoa.intValue() + " ORDER BY situacao";

		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT * FROM Matricula WHERE upper (situacao) = '" + valorConsulta.toUpperCase() + "' and aluno = " + pessoa.intValue() + " and unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY situacao";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public List consultarPorTurma_gradeCurricular(Integer turma, Integer gradeCurricular, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Matricula.* FROM MatriculaPeriodo, Matricula WHERE Matricula.matricula = MatriculaPeriodo.matricula and MatriculaPeriodo.turma = " + turma.intValue() + " and  MatriculaPeriodo.gradeCurricular = " + gradeCurricular.intValue() + " ORDER BY Matricula.matricula";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT Matricula.* FROM MatriculaPeriodo, Matricula WHERE Matricula.matricula = MatriculaPeriodo.matricula and MatriculaPeriodo.turma = " + turma.intValue() + " and  MatriculaPeriodo.gradeCurricular = " + gradeCurricular.intValue() + " and Matricula.unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY Matricula.matricula";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public List consultarPorTurmaPerfilTurma(Integer turma, String ano, String semestre, String situacao, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT Matricula.*, pessoa.nome AS pessoanome FROM Matricula");
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula");
		sqlStr.append(" INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno");
		sqlStr.append(" WHERE Matricula.matricula = MatriculaPeriodo.matricula AND Pessoa.codigo = Matricula.aluno ");
		if (turma != 0) {
			sqlStr.append("AND MatriculaPeriodo.turma = ").append(turma).append(" ");
		}
		if (!ano.equals("")) {
			sqlStr.append("AND MatriculaPeriodo.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("")) {
			sqlStr.append("AND MatriculaPeriodo.semestre = '").append(semestre).append("' ");
		}
		if (!situacao.equals("")) {
			sqlStr.append("AND matricula.situacao = 'AT' ");
		}
		sqlStr.append("ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public List consultarPorCursoTurmaAnoSemestreSituacao(Integer curso, Integer turma, String ano, String semestre, String situacao, String tipoMatricula, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT Matricula.*, pessoa.nome AS pessoanome FROM Matricula");
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula ");
		sqlStr.append(" INNER JOIN turma ON turma.codigo = ").append(turma);
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matriculaperiodo = MatriculaPeriodo.codigo AND ");
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSQLPadraoJoinMatriculaPeriodoTurmaDisciplinaTurma(sqlStr);
		sqlStr.append(" INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno");
		sqlStr.append(" WHERE Matricula.matricula = MatriculaPeriodo.matricula AND Pessoa.codigo = Matricula.aluno ");
		if (curso != 0) {
			sqlStr.append("AND Matricula.curso = ").append(curso).append(" ");
		}
//		if (turma != 0) {
//			sqlStr.append("AND MatriculaPeriodo.turma = ").append(turma).append(" ");
//		}
		if (!ano.equals("")) {
			sqlStr.append("AND MatriculaPeriodo.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("")) {
			sqlStr.append("AND MatriculaPeriodo.semestre = '").append(semestre).append("' ");
		}
		if (!situacao.equals("")) {
			sqlStr.append("AND matricula.situacao = '").append(situacao).append("' ");
		}
		if(situacao.equals("AT")){
			sqlStr.append("AND matriculaperiodo.situacaomatriculaperiodo in ('AT', 'FI', 'FO') ");
		}

		if (!tipoMatricula.equals("")) {
			if (tipoMatricula.equals("NO")) {
				sqlStr.append("AND (matricula.tipoMatricula is null or matricula.tipoMatricula = '").append(tipoMatricula).append("' or matricula.tipoMatricula = '') ");
			} else {
				sqlStr.append("AND matricula.tipoMatricula = '").append(tipoMatricula).append("' ");
			}
		}
		sqlStr.append("ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public List consultarPorCursoTurmaAnoSemestreSituacaoReposicaoInclusao(Integer curso, Integer turma, String ano, String semestre, String situacao, String tipoMatricula, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT Matricula.*, pessoa.nome AS pessoanome FROM Matricula");
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula");
		sqlStr.append(" INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo");
		sqlStr.append(" WHERE mptd.turma <> matriculaperiodo.turma ");
		if (curso != 0) {
			sqlStr.append("AND Matricula.curso = ").append(curso).append(" ");
		}
		if (turma != 0) {
			sqlStr.append("AND MatriculaPeriodo.turma = ").append(turma).append(" ");
		}
		if (!ano.equals("")) {
			sqlStr.append("AND MatriculaPeriodo.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("")) {
			sqlStr.append("AND MatriculaPeriodo.semestre = '").append(semestre).append("' ");
		}
		if (!situacao.equals("")) {
			sqlStr.append("AND matricula.situacao = '").append(situacao).append("' ");
		}
		if (!tipoMatricula.equals("")) {
			if (tipoMatricula.equals("NO")) {
				sqlStr.append("AND (matricula.tipoMatricula is null or matricula.tipoMatricula = '").append(tipoMatricula).append("' or matricula.tipoMatricula = '') ");
			} else {
				sqlStr.append("AND matricula.tipoMatricula = '").append(tipoMatricula).append("' ");
			}
		}
		sqlStr.append("ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public List consultarPorCursoTurmaAnoSemestre(Integer curso, Integer turma, String ano, String semestre, String tipoMatricula, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, String nivelEducacional, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT Matricula.*, pessoa.nome AS pessoanome FROM Matricula");
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula");
		sqlStr.append(" INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno");
		sqlStr.append(" INNER JOIN curso ON Curso.codigo = Matricula.curso");
		sqlStr.append(" WHERE Matricula.matricula = MatriculaPeriodo.matricula AND Pessoa.codigo = Matricula.aluno ");
		if(curso != 0) {
			sqlStr.append(" AND Matricula.curso = ").append(curso).append(" ");
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append("AND Curso.niveleducacional = '").append(nivelEducacional).append("' ");
		}
		if (turma != 0) {
			sqlStr.append("AND MatriculaPeriodo.turma = ").append(turma).append(" ");
		}
		if (!ano.equals("")) {
			sqlStr.append("AND MatriculaPeriodo.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("")) {
			sqlStr.append("AND MatriculaPeriodo.semestre = '").append(semestre).append("' ");
		}
		if (!tipoMatricula.equals("")) {
			if (tipoMatricula.equals("NO")) {
				sqlStr.append("AND (matricula.tipoMatricula is null or matricula.tipoMatricula = '").append(tipoMatricula).append("' or matricula.tipoMatricula = '') ");
			} else {
				sqlStr.append("AND matricula.tipoMatricula = '").append(tipoMatricula).append("' ");
			}
		}
		sqlStr.append("ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public List consultarPorMatricula(String matricula, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT Matricula.*, pessoa.nome AS pessoanome FROM Matricula");
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula");
		sqlStr.append(" INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno");
		sqlStr.append(" WHERE Matricula.matricula = MatriculaPeriodo.matricula AND Pessoa.codigo = Matricula.aluno AND Matricula.matricula like '").append(matricula).append("%' ");
		sqlStr.append("ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public String getNumeroMatriculaPorPessoa(PessoaVO pessoaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		if (pessoaVO.getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
			List<MatriculaVO> matriculas = getFacadeFactory().getMatriculaFacade().consultarMatriculaPorCodigoPessoa(pessoaVO.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, configuracaoFinanceiroVO, usuario);
			return getUtimaMatricula(matriculas).getMatricula();
		} else if (pessoaVO.getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
			return getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(pessoaVO.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario).getMatricula();
		}
		return "";

	}

	public MatriculaVO getUtimaMatricula(List<MatriculaVO> matriculaVOs) {
		MatriculaVO utimaMatricula = new MatriculaVO();
		utimaMatricula.setData(new GregorianCalendar(1900, 01, 01).getTime());
		for (MatriculaVO matricula : matriculaVOs) {
			if (utimaMatricula.getData().before(matricula.getData())) {
				utimaMatricula = matricula;
			}
		}
		return utimaMatricula;
	}

	public MatriculaVO consultarPorInscricao(Integer inscricao, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Matricula.* FROM Matricula WHERE inscricao = " + inscricao.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public MatriculaVO consultarAlunoPorMatricula(String valorConsulta, String tipoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT matricula.* FROM Matricula, Pessoa WHERE lower (matricula.matricula) =( '" + valorConsulta.toLowerCase() + "') and pessoa.codigo = matricula.aluno ";
		if (tipoPessoa != null && !tipoPessoa.equals("")) {
			sqlStr += "and lower (pessoa.tipoPessoa) =('" + tipoPessoa.toLowerCase() + "') ";
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr += "and Matricula.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados(Matrícula).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public List consultaRapidaCompletaPorCodigoPessoa(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (Pessoa.codigo = ").append(pessoa).append(") ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaCompleta(tabelaResultado, usuario);
		// String sqlStr =
		// "SELECT matricula.* FROM Matricula ,Pessoa WHERE pessoa.codigo = matricula.aluno and pessoa.codigo = "
		// + pessoa.intValue();
		// if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
		// sqlStr =
		// "SELECT matricula.* FROM Matricula ,Pessoa WHERE Matricula.aluno = Pessoa.codigo and pessoa.codigo = "
		// + pessoa.intValue() + " and Matricula.unidadeEnsino = " +
		// unidadeEnsino.intValue() + " ";
		// }
		// SqlRowSet tabelaResultado =
		// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		// return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	public List consultaRapidaCompletaPorCodigoPessoaNaoCancelada(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (Pessoa.codigo = ").append(pessoa).append(") AND (matricula.situacao != 'CA') ");
		sqlStr.append(" ORDER BY matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaCompleta(tabelaResultado, usuario);
	}

	/**
	 *  *IMPORTANTE*
	 * O Metodo consultaRapidaBasicaPorCodigoPessoaNaoCancelada deve seguir
	 * as mesmas regras do  consultaRapidaBasicaPorCodigoPessoaAtivasMatriculasFilhoFuncionario
	 **/
	@Override
	public List<MatriculaVO> consultaRapidaBasicaPorCodigoPessoaNaoCancelada(Integer pessoa, Boolean visaoPais, boolean controlarAcesso, boolean validarBloqueioMatricula, boolean validarSuspensaoMatricula, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(true, true, visaoPais);		
		if (visaoPais) {			
			sqlStr.append(" WHERE (filiacao.pais = ").append(pessoa).append(") ");
		} else {
			sqlStr.append(" WHERE (Pessoa.codigo = ").append(pessoa).append(") ");
		}
		sqlStr.append(getWhereMatriculaLiberadasVisaoAlunoPais(validarBloqueioMatricula, validarSuspensaoMatricula, true));		
		sqlStr.append(" ORDER BY situacaoMatriculaOrdenar, ordemAnoSemestre,   matricula.data desc ");
		sqlStr.replace(sqlStr.indexOf("situacaoMatriculaOrdenar,"), sqlStr.indexOf("situacaoMatriculaOrdenar, ")+25, "situacaoMatriculaOrdenar, case when ((curso.periodicidade = 'AN' and extract(year from current_date)::varchar = matriculaperiodo.ano) or (curso.periodicidade = 'SE' and extract(year from current_date)::varchar = matriculaperiodo.ano and case when extract(month from current_date) > 7 then '2' else '1' end = matriculaperiodo.semestre) or (curso.periodicidade = 'IN')) then 0 else 1 end as ordemAnoSemestre,");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, usuario, conf);
	}

	public MatriculaVO consultaRapidaBasicaPorMatriculaNaoCancelada(String matricula, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (matricula.matricula = '").append(matricula).append("') ");
//		sqlStr.append(" WHERE (matricula.matricula = '").append(matricula).append("') AND matricula.situacao not in ('CA', 'TR', 'AC', 'TS','TI') AND (matricula.matriculaSuspensa = false or matricula.matriculaSuspensa is null) ");
		sqlStr.append(" ORDER BY matricula.situacao,  matricula.data desc ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<MatriculaVO> lista = montarDadosConsultaBasicaVerificarBloqueio(tabelaResultado, usuario, conf);
		if (!lista.isEmpty()) {
			return lista.get(0);
		}
		return null;
	}

	public List consultaRapidaCompletaPorCodigoPessoaNaoCancelada(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (Pessoa.codigo = ").append(pessoa).append(") AND (matricula.situacao != 'CA') ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaCompleta(tabelaResultado, usuario);
	}

	public List<MatriculaVO> consultaRapidaPorCodigoPessoa(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Pessoa.codigo = ").append(pessoa).append(") ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" and matricula.situacao <> 'CA' and matricula.situacao <> 'TR' ");
		sqlStr.append(" ORDER BY matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List consultaRapidaPorCodigoPessoaNaoCancelada(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Pessoa.codigo = ").append(pessoa).append(") AND (matricula.situacao != 'CA') ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List consultaRapidaPorCodigoPessoaCurso(Integer pessoa, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Pessoa.codigo = ").append(pessoa).append(") ");
		if ((curso != null) && (curso.intValue() != 0)) {
			sqlStr.append(" AND (Curso.codigo = ").append(curso.intValue()).append(") ");
		}
		if ((unidadeEnsino != null) && (unidadeEnsino.intValue() != 0)) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultarMatriculaPorCodigoPessoa(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT matricula.* FROM Matricula, Pessoa WHERE pessoa.codigo = matricula.aluno and pessoa.codigo = " + pessoa.intValue() + " order by matricula";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT matricula.* FROM Matricula, Pessoa WHERE Matricula.aluno = Pessoa.codigo and pessoa.codigo = " + pessoa.intValue() + " order by matricula";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	/**
	 * Busca matriculas para o censo.
	 *
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @return retorna somente as matriculas cujas situações interessam ao mec
	 * @throws Exception
	 */
	@Deprecated
	public List consultarMatriculasParaCenso(boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select matricula.* " + "from matricula " + "where situacao = '" + SituacaoVinculoMatricula.DESLIGADO.getValor() + "' " + "or situacao = '" + SituacaoVinculoMatricula.INATIVA.getValor() + "' " + "or situacao = '" + SituacaoVinculoMatricula.ATIVA.getValor() + "' " + "or situacao = '" + SituacaoVinculoMatricula.TRANCADA.getValor() + "' " + "or situacao = '" + SituacaoVinculoMatricula.PROVAVEL_FORMANDO.getValor() + "' " + "or situacao = '" + SituacaoVinculoMatricula.FORMADO.getValor() + "'";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));

	}

	public List consultarPorTurma_periodoLetivo(Integer turma, Integer periodoLetivo, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Matricula.* FROM MatriculaPeriodo, Matricula WHERE Matricula.matricula = MatriculaPeriodo.matricula and MatriculaPeriodo.turma = " + turma.intValue() + " and  MatriculaPeriodo.periodoLetivoMatricula = " + periodoLetivo.intValue() + " ORDER BY Matricula.matricula";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT Matricula.* FROM MatriculaPeriodo, Matricula WHERE Matricula.matricula = MatriculaPeriodo.matricula and MatriculaPeriodo.turma = " + turma.intValue() + " and  MatriculaPeriodo.periodoLetivoMatricula = " + periodoLetivo.intValue() + " and Matricula.unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY Matricula.matricula";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Matricula</code> através
	 * do valor do atributo <code>Date data</code>. Retorna os objetos com
	 * valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>MatriculaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Matricula WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT * FROM Matricula WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) and unidadeEnsino =  ORDER BY data";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>Matricula</code> através
	 * do valor do atributo <code>nome</code> da classe <code>Curso</code> Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 *
	 * @return List Contendo vários objetos da classe <code>MatriculaVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<MatriculaVO> consultarPorNomeCurso(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Matricula.* FROM Matricula " + "inner join Curso on Matricula.curso = Curso.codigo " + "WHERE lower (sem_acentos(Curso.nome)) like(sem_acentos(?))  ";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr += " and Matricula.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		sqlStr += " ORDER BY Curso.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	public List consultarPorNomeCursoSituacaoMatricula(String valorConsulta, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Matricula.* FROM Matricula " + "inner join Curso on Matricula.curso = Curso.codigo " + "WHERE lower (Curso.nome) like('" + valorConsulta.toLowerCase() + "%') and matricula.situacao = '" + situacao + "' ";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr += " and Matricula.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		sqlStr += " ORDER BY Curso.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	public List consultarPorNomeCursoNivelEducacional(String valorConsulta, Integer unidadeEnsino, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT Matricula.* FROM Matricula ");
		sqlStr.append("inner join Curso on Matricula.curso = Curso.codigo ");
		sqlStr.append(" WHERE lower (Curso.nome) like('").append(valorConsulta.toLowerCase());
		sqlStr.append("%') AND curso.niveleducacional = '").append(nivelEducacional).append("'");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" and Matricula.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	public List consultarPorNomeCursoTipoRequerimentoSemProgramacaoFormatura(String nomeCurso, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		// sqlStr.append(" SELECT matricula.* FROM matricula");
		// sqlStr.append(" LEFT JOIN curso ON curso.codigo = matricula.curso");
		// sqlStr.append(" LEFT JOIN requerimento ON requerimento.matricula = matricula.matricula");
		// sqlStr.append(" LEFT JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento");
		// sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.matricula = matricula.matricula");
		// sqlStr.append(" WHERE ((programacaoformaturaaluno.colougrau <> 'NI' AND programacaoformaturaaluno.colougrau <> 'SI') OR programacaoformaturaaluno.matricula ISNULL) AND Matricula.curso = Curso.codigo AND tiporequerimento.tipo = '"
		// + tipoRequerimento.toUpperCase() + "' AND lower (Curso.nome) like('"
		// + nomeCurso.toLowerCase() + "%') ");
		sqlStr.append(" SELECT matricula.* FROM matricula ");
		sqlStr.append(" LEFT JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.matricula = matricula.matricula ");
		sqlStr.append(" WHERE ((programacaoformaturaaluno.colougrau <> 'NI')) ");
		sqlStr.append(" AND Matricula.curso = Curso.codigo ");
		sqlStr.append(" AND lower (Curso.nome) like('");
		sqlStr.append(nomeCurso.toLowerCase());
		sqlStr.append("%') ");
		sqlStr.append(" ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY curso.nome;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	public List<MatriculaVO> consultarPorNomeCursoAtivoTrancado(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		if (unidadeEnsino.intValue() == 0 || unidadeEnsino == null) {
			sqlStr.append(" where ((Matricula.situacao = 'AT') or (Matricula.situacao = 'TR') or (Matricula.situacao = 'CF') or (Matricula.situacao = 'FO')) and lower (Curso.nome) like('").append(valorConsulta.toLowerCase()).append("%') ORDER BY Curso.nome");
		} else if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" where ((Matricula.situacao = 'AT') or (Matricula.situacao = 'TR') or (Matricula.situacao = 'CF') or (Matricula.situacao = 'FO')) and lower (Curso.nome) like('").append(valorConsulta.toLowerCase()).append("%') and Matricula.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ORDER BY Curso.nome");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Matricula</code> através
	 * do valor do atributo <code>nome</code> da classe <code>Pessoa</code> Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
	 * de prerarar o List resultante.
	 *
	 * @return List Contendo vários objetos da classe <code>MatriculaVO</code>
	 *         resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<MatriculaVO> consultarPorNomePessoa(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE Matricula.aluno = Pessoa.codigo and sem_acentos(Pessoa.nome) ilike(sem_acentos(?))  ORDER BY Pessoa.nome, matricula.matricula ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = getSQLPadraoConsultaCompleta();
			sqlStr.append(" WHERE Matricula.aluno = Pessoa.codigo and sem_acentos(Pessoa.nome) ilike(sem_acentos(?)) and Matricula.unidadeEnsino = ").append(unidadeEnsino.intValue()).append("  ORDER BY Pessoa.nome, matricula.matricula ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta+"%");
		return montarDadosConsultaCompleta(tabelaResultado, usuario);
	}

	public List consultarPorNomePessoaSituacaoMatricula(String valorConsulta, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Matricula.* FROM Matricula, Pessoa WHERE Matricula.aluno = Pessoa.codigo and sem_acentos(Pessoa.nome) ilike('" + valorConsulta + "%') and matricula.situacao = '" + situacao + "' ";
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr += " and Matricula.unidadeEnsino = " + unidadeEnsino.intValue();
		}
		sqlStr += " ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	public List<MatriculaVO> consultarPorNomePessoaNivelEducacional(String valorConsulta, Integer unidadeEnsino, String nivelEducacinoal, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT * FROM matricula");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso");
		sqlStr.append(" WHERE sem_acentos(pessoa.nome) ilike(sem_acentos(?))");

		if (nivelEducacinoal != null && !nivelEducacinoal.equals("")) {
			sqlStr.append(" AND curso.niveleducacional = '").append(nivelEducacinoal).append("'");
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}
	
	@Override
	public List<MatriculaVO> consultarPorRegistroAcademicoNivelEducacional(String valorConsulta, Integer unidadeEnsino, String nivelEducacinoal, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT * FROM matricula");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso");
		sqlStr.append(" WHERE sem_acentos(pessoa.registroAcademico) ilike(sem_acentos(?))");
		
		if (nivelEducacinoal != null && !nivelEducacinoal.equals("")) {
			sqlStr.append(" AND curso.niveleducacional = '").append(nivelEducacinoal).append("'");
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	public List consultarPorNomePessoaTipoRequerimentoSemProgramacaoFormatura(String nomePessoa, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		sqlStr.append(" SELECT matricula.* FROM matricula ");
		sqlStr.append(" LEFT JOIN pessoa ON pessoa.codigo = matricula.aluno");
		// sqlStr.append(" LEFT JOIN requerimento ON requerimento.matricula = matricula.matricula");
		// sqlStr.append(" LEFT JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.matricula = matricula.matricula");
		sqlStr.append(" WHERE ((programacaoformaturaaluno.colougrau <> 'NI'))");
		// sqlStr.append(" AND programacaoformaturaaluno.colougrau <> 'SI') OR programacaoformaturaaluno.matricula ISNULL) AND tiporequerimento.tipo = '"")
		// + tipoRequerimento.toUpperCase()
		sqlStr.append(" AND sem_acentos(Pessoa.nome) ilike(sem_acentos(?)) ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomePessoa+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	public List<MatriculaVO> consultarPorNomePessoaAtivaOuTrancada(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		if (unidadeEnsino.intValue() == 0 || unidadeEnsino == null) {
			sqlStr.append(" where (sem_acentos(Pessoa.nome)) ILIKE (upper(sem_acentos('").append(valorConsulta).append("%'))) and ((Matricula.situacao = 'AT') or (Matricula.situacao = 'TR') or (Matricula.situacao = 'CF') or (Matricula.situacao = 'FO')) ORDER BY Pessoa.nome ");
		} else if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" where (sem_acentos(Pessoa.nome)) ILIKE(upper(sem_acentos('").append(valorConsulta).append("%'))) and ((Matricula.situacao = 'AT') or (Matricula.situacao = 'TR') or (Matricula.situacao = 'CF') or (Matricula.situacao = 'FO')) and Matricula.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ORDER BY Pessoa.nome");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);

		// String sqlStr =
		// " and  upper(sem_acentos(Pessoa.nome)) like(upper(sem_acentos('%" +
		// valorConsulta
		// +
		// "%'))) and ((Matricula.situacao = 'AT') or (Matricula.situacao = 'TR') or (Matricula.situacao = 'CF') or (Matricula.situacao = 'FO')) ORDER BY Pessoa.nome";
		// if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
		// sqlStr =
		// "SELECT Matricula.* FROM Matricula, Pessoa WHERE Matricula.aluno = Pessoa.codigo and upper(sem_acentos(Pessoa.nome)) like(upper(sem_acentos('%"
		// + valorConsulta
		// +
		// "%'))) and ((Matricula.situacao = 'AT') or (Matricula.situacao = 'TR') or (Matricula.situacao = 'CF') or (Matricula.situacao = 'FO')) and Matricula.unidadeEnsino = "
		// + unidadeEnsino.intValue() + " ORDER BY Pessoa.nome";
		// }
		// SqlRowSet tabelaResultado =
		// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		// return montarDadosConsulta(tabelaResultado, nivelMontarDados,
		// configuracaoFinanceiro, usuario);
	}
	
	@Override
	public List<MatriculaVO> consultarPorRegistroAcademicoPessoaAtivaOuTrancada(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		if (unidadeEnsino.intValue() == 0 || unidadeEnsino == null) {
			sqlStr.append(" where upper(sem_acentos(Pessoa.registroAcademico)) like(upper(sem_acentos('").append(valorConsulta).append("%'))) and ((Matricula.situacao = 'AT') or (Matricula.situacao = 'TR') or (Matricula.situacao = 'CF') or (Matricula.situacao = 'FO')) ORDER BY Pessoa.nome ");
		} else if (unidadeEnsino != null && unidadeEnsino.intValue() != 0 ) {
			sqlStr.append(" where upper(sem_acentos(Pessoa.registroAcademico)) like(upper(sem_acentos('").append(valorConsulta).append("%'))) and ((Matricula.situacao = 'AT') or (Matricula.situacao = 'TR') or (Matricula.situacao = 'CF') or (Matricula.situacao = 'FO')) and Matricula.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ORDER BY Pessoa.registroAcademico");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
		
	}

	public List<MatriculaVO> consultarMatriculaPosGraduacaoPorNome(String valorConsulta, boolean controlarAcesso, Integer nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT matricula.* ");
		sqlStr.append("FROM matricula ");
		sqlStr.append("INNER JOIN pessoa ON (matricula.aluno = pessoa.codigo) ");
		sqlStr.append("INNER JOIN curso ON (matricula.curso = curso.codigo) ");
		sqlStr.append("WHERE (sem_acentos(pessoa.nome)) ILIKE (upper(sem_acentos(?))) AND curso.niveleducacional like('PO')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), "%"+valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	public MatriculaVO consultarMatriculaPosGraduacaoPorMatricula(String valorConsulta, boolean controlarAcesso, Integer nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT matricula.* ");
		sqlStr.append("FROM matricula ");
		sqlStr.append("INNER JOIN pessoa ON (matricula.aluno = pessoa.codigo) ");
		sqlStr.append("INNER JOIN curso ON (matricula.curso = curso.codigo) ");
		sqlStr.append("WHERE matricula.matricula = '").append(valorConsulta).append("' AND curso.niveleducacional like('PO')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	public List<MatriculaVO> consultarPorCPF(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Matricula.* FROM Matricula, Pessoa WHERE Matricula.aluno = Pessoa.codigo and lower (Pessoa.cpf) like('" + valorConsulta.toLowerCase() + "%') ORDER BY Pessoa.nome";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT Matricula.* FROM Matricula, Pessoa WHERE Matricula.aluno = Pessoa.codigo and lower (Pessoa.cpf) like('" + valorConsulta.toLowerCase() + "%') and Matricula.unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY Pessoa.nome";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	public List consultarPorTurnoTipoRequerimentoSemProgramacaoFormatura(Integer turno, String tipoRequerimento, Integer unidadeEnsino, boolean semRequerimento, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		sqlStr.append(" SELECT matricula.* FROM matricula");
		sqlStr.append(" LEFT JOIN turno ON turno.codigo = matricula.turno");
		sqlStr.append(" LEFT JOIN requerimento ON requerimento.matricula = matricula.matricula");
		sqlStr.append(" LEFT JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.matricula = matricula.matricula");
		sqlStr.append(" WHERE ((programacaoformaturaaluno.colougrau <> 'NI' AND programacaoformaturaaluno.colougrau <> 'SI') OR programacaoformaturaaluno.matricula ISNULL)");
		sqlStr.append(" AND turno.codigo = '").append(turno.intValue()).append("'");
		if (!semRequerimento) {
			sqlStr.append(" AND tiporequerimento.tipo = '").append(tipoRequerimento.toUpperCase()).append("'");
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY matricula.matricula;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	public List consultarPorCursoTipoRequerimentoSemProgramacaoFormatura(Integer curso, String tipoRequerimento, Integer unidadeEnsino, boolean semRequerimento, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		sqlStr.append(" SELECT matricula.* FROM matricula");
		sqlStr.append(" LEFT JOIN curso ON curso.codigo = matricula.curso");
		sqlStr.append(" LEFT JOIN requerimento ON requerimento.matricula = matricula.matricula");
		sqlStr.append(" LEFT JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.matricula = matricula.matricula");
		sqlStr.append(" WHERE ((programacaoformaturaaluno.colougrau <> 'NI' AND programacaoformaturaaluno.colougrau <> 'SI') OR programacaoformaturaaluno.matricula ISNULL)");
		sqlStr.append(" AND curso.codigo = '").append(curso.intValue()).append("'");
		if (!semRequerimento) {
			sqlStr.append(" AND tiporequerimento.tipo = '").append(tipoRequerimento.toUpperCase()).append("'");
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY matricula.matricula;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	public List consultaRapidaPorCursoTipoRequerimentoSemProgramacaoFormatura(Integer curso, String tipoRequerimento, Integer unidadeEnsino, String nivelEducacional, boolean semRequerimento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" LEFT JOIN requerimento ON requerimento.matricula = matricula.matricula");
		sqlStr.append(" LEFT JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.matricula = matricula.matricula");
		sqlStr.append(" WHERE (programacaoformaturaaluno.programacaoformatura is null OR ");
		sqlStr.append(" (programacaoformaturaaluno.colougrau <> 'NI' AND programacaoformaturaaluno.colougrau <> 'SI') OR programacaoformaturaaluno.matricula ISNULL)");
		sqlStr.append(" AND curso.codigo = '").append(curso.intValue()).append("'");
		if (!semRequerimento) {
			sqlStr.append(" AND tiporequerimento.tipo = '").append(tipoRequerimento.toUpperCase()).append("'");
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		if (Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" AND Curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		sqlStr.append(" ORDER BY matricula.matricula;");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorTurmaTipoRequerimentoSemProgramacaoFormatura(Integer turma, String tipoRequerimento, Integer unidadeEnsino, String nivelEducacional, boolean semRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" LEFT JOIN requerimento ON requerimento.matricula = matricula.matricula");
		sqlStr.append(" LEFT JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.matricula = matricula.matricula");
		sqlStr.append(" WHERE (programacaoformaturaaluno.programacaoformatura is null OR ");
		sqlStr.append(" (programacaoformaturaaluno.colougrau <> 'NI' AND programacaoformaturaaluno.colougrau <> 'SI') OR programacaoformaturaaluno.matricula ISNULL)");
		sqlStr.append(" AND matriculaperiodo.turma = '").append(turma.intValue()).append("'");
		sqlStr.append(" AND matricula.situacao = 'AT' ");
		if (!semRequerimento) {
			sqlStr.append(" AND tiporequerimento.tipo = '").append(tipoRequerimento.toUpperCase()).append("'");
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		if (Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" AND Curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorTurnoTipoRequerimentoSemProgramacaoFormatura(Integer turno, String tipoRequerimento, Integer unidadeEnsino, String nivelEducacionalSelecionado, boolean semRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" LEFT JOIN requerimento ON requerimento.matricula = matricula.matricula");
		sqlStr.append(" LEFT JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.matricula = matricula.matricula");
		sqlStr.append(" WHERE (programacaoformaturaaluno.programacaoformatura is null OR ");
		sqlStr.append(" (programacaoformaturaaluno.colougrau <> 'NI' AND programacaoformaturaaluno.colougrau <> 'SI') OR programacaoformaturaaluno.matricula ISNULL)");
		sqlStr.append(" AND matricula.turno = '").append(turno.intValue()).append("'");
		sqlStr.append(" AND matricula.situacao = 'AT' ");
		if (!semRequerimento) {
			sqlStr.append(" AND tiporequerimento.tipo = '").append(tipoRequerimento.toUpperCase()).append("'");
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		if (Uteis.isAtributoPreenchido(nivelEducacionalSelecionado)) {
			sqlStr.append(" AND Curso.nivelEducacional = '").append(nivelEducacionalSelecionado).append("' ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorMatriculaTipoRequerimentoSemProgramacaoFormatura(String matricula, String tipoRequerimento, Integer unidadeEnsino, String nivelEducacional, boolean semRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" LEFT JOIN requerimento ON requerimento.matricula = matricula.matricula");
		sqlStr.append(" LEFT JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.matricula = matricula.matricula");
		sqlStr.append(" WHERE (programacaoformaturaaluno.programacaoformatura is null OR ");
		sqlStr.append(" (programacaoformaturaaluno.colougrau <> 'NI' AND programacaoformaturaaluno.colougrau <> 'SI') OR programacaoformaturaaluno.matricula ISNULL)");
		sqlStr.append(" AND (Matricula.matricula like('").append(matricula).append("%')) ");
		sqlStr.append(" AND matricula.situacao = 'AT' ");
		if (!semRequerimento) {
			sqlStr.append(" AND tiporequerimento.tipo = '").append(tipoRequerimento.toUpperCase()).append("'");
		}
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		if (Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" AND Curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorRequerimentoTipoRequerimentoSemProgramacaoFormatura(Integer codigoRequerimento, String tipoRequerimento, Integer unidadeEnsino, String nivelEducacional, boolean semRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" LEFT JOIN requerimento ON requerimento.matricula = matricula.matricula");
		sqlStr.append(" LEFT JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.matricula = matricula.matricula");
		sqlStr.append(" WHERE (programacaoformaturaaluno.programacaoformatura is null OR ");
		sqlStr.append(" (programacaoformaturaaluno.colougrau <> 'NI' AND programacaoformaturaaluno.colougrau <> 'SI') OR programacaoformaturaaluno.matricula ISNULL)");
		sqlStr.append(" AND (requerimento.codigo =  ").append(codigoRequerimento).append(") ");
		sqlStr.append(" AND matricula.situacao = 'AT' ");
		if (!semRequerimento) {
			sqlStr.append(" AND tiporequerimento.tipo = '").append(tipoRequerimento.toUpperCase()).append("'");
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		if (Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" AND Curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List consultarPorMatriculaTipoRequerimentoSemProgramacaoFormatura(String matricula, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		sqlStr.append(" SELECT matricula.* from matricula");
		sqlStr.append(" LEFT JOIN requerimento ON requerimento.matricula = matricula.matricula");
		sqlStr.append(" LEFT JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.matricula = matricula.matricula");
		sqlStr.append(" WHERE matricula.matricula not in(select distinct matricula from programacaoformaturaaluno ) AND matricula.matricula like('").append(matricula.toUpperCase()).append("%')");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY matricula.matricula");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	public List consultarPorRequerimentoTipoRequerimento(Integer codigoRequerimento, String tipoRequerimento, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		sqlStr.append(" SELECT matricula.* from matricula");
		sqlStr.append(" LEFT JOIN requerimento ON requerimento.matricula = matricula.matricula");
		sqlStr.append(" LEFT JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento");
		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.matricula = matricula.matricula");
		sqlStr.append(" WHERE ((programacaoformaturaaluno.colougrau <> 'NI' AND programacaoformaturaaluno.colougrau <> 'SI') OR programacaoformaturaaluno.matricula ISNULL) AND");
		sqlStr.append(" tiporequerimento.tipo = '").append(tipoRequerimento.toUpperCase()).append("' AND requerimento.codigo = ").append(codigoRequerimento.intValue());
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" and Matricula.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		sqlStr.append(" ORDER BY matricula.matricula");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Matricula</code> através
	 * do valor do atributo <code>String matricula</code>. Retorna os objetos,
	 * com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>MatriculaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Matricula WHERE UPPER (matricula) like('" + valorConsulta.toUpperCase() + "%')  ORDER BY matricula";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT * FROM Matricula WHERE UPPER (matricula) like('" + valorConsulta.toUpperCase() + "%') and unidadeEnsino = " + unidadeEnsino.intValue() + "  ORDER BY matricula";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public List executarConsultaPorTurmaCurso(Integer turma, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" SELECT distinct matricula.* FROM matricula");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matricula = matricula.matricula");
		sqlStr.append(" WHERE matriculaperiodoturmadisciplina.turma = ").append(turma).append(" AND curso.codigo = ").append(curso);
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public List executarConsultaPorTurmaCursoGradeCurricularAnoSemestre(Integer turma, Integer curso, String ano, String semestre, Integer gradeCurriculcar, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" SELECT distinct matricula.* FROM matricula");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matricula = matricula.matricula");
		sqlStr.append(" WHERE matriculaperiodo.turma = ").append(turma).append(" AND curso.codigo = ").append(curso);
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		if (!ano.equals("")) {
			sqlStr.append(" AND matriculaperiodo.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" AND matriculaperiodo.semestre = '").append(semestre).append("' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public List<MatriculaVO> consultaRapidaPorTurmaCursoGradeCurricularAnoSemestre(Integer turma, Integer curso, String ano, String semestre, Integer gradeCurricular, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaDistinct();
		sqlStr.append(" LEFT JOIN matriculaperiodo ON (matriculaperiodo.matricula = matricula.matricula) ");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matricula = matricula.matricula");
		sqlStr.append(" LEFT JOIN Turma ON ( Turma.codigo = matriculaperiodoturmadisciplina.turma ) ");
		sqlStr.append(" WHERE matriculaperiodo.turma = ").append(turma).append(" AND curso.codigo = ").append(curso);
		if (unidadeEnsino != null  && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		if (ano != null && !ano.equals("") && semestre != null &&  !semestre.equals("") ) {
			sqlStr.append(" AND (matriculaperiodo.ano = '").append(ano).append("' ").append(" AND matriculaperiodo.semestre = '").append(semestre).append("' ").append(" ");
			sqlStr.append(" AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ").append(" AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ").append(") ");
		} else if (ano != null && !ano.equals("")) {
			sqlStr.append(" AND (matriculaperiodo.ano = '").append(ano).append("' ");
			sqlStr.append(" AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ").append(") ");
		} else if (semestre != null && !semestre.equals("")) {
			sqlStr.append(" AND (matriculaperiodo.semestre = '").append(semestre).append("' ");
			sqlStr.append(" AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ").append(") ");
		}
		if (gradeCurricular != null && gradeCurricular != 0) {
			sqlStr.append(" AND turma.gradecurricular = '").append(gradeCurricular).append("' ");
		}
		sqlStr.append(" AND (matriculaperiodoturmadisciplina.reposicao = false OR matriculaperiodoturmadisciplina.reposicao is null) ");
		sqlStr.append(" ORDER BY pessoa.nome; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		matriculaVOs = montarDadosConsultaBasica(tabelaResultado);
		return matriculaVOs;
	}

	public List<MatriculaVO> consultaRapidaPorTurmaAnoSemestreTipoAlunoEntregaDocumento(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<TurmaAgrupadaVO> listaTurmaAgrupadas, String ano, String semestre, Integer unidadeEnsino, ConfiguracaoFinanceiroVO configuracaoFinanceiro, String tipoAluno, UsuarioVO usuario) throws Exception {
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaDistinct();
		sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" and matriculaperiodo.codigo  = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
		sqlStr.append(" order by (mp.ano ||'/' || mp.semestre) desc , case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1)  ");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matricula = matricula.matricula and matriculaperiodoturmadisciplina.turmaPratica is null and matriculaperiodoturmadisciplina.turmaTeorica is null ");
		if (listaTurmaAgrupadas.size() > 0) {
			if (tipoAluno.equals("TODOS")) {
				sqlStr.append(" LEFT JOIN Turma ON (Turma.codigo = matriculaperiodoturmadisciplina.turma) ");
				boolean virgula = false;
				sqlStr.append(" WHERE ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo")).append(" AND turma.codigo in(");
				for (TurmaAgrupadaVO turmaAgrupada : listaTurmaAgrupadas) {
					if (!virgula) {
						sqlStr.append(turmaAgrupada.getTurma().getCodigo());
					} else {
						sqlStr.append(", ").append(turmaAgrupada.getTurma().getCodigo());
					}
					virgula = true;
				}
				sqlStr.append(") ");
			} else if (tipoAluno.equals("NORMAL")) {
				sqlStr.append(" LEFT JOIN Turma ON (Turma.codigo = matriculaPeriodo.turma) ");
				boolean virgula = false;
				sqlStr.append(" WHERE ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo")).append(" AND turma.codigo in(");
				for (TurmaAgrupadaVO turmaAgrupada : listaTurmaAgrupadas) {
					if (!virgula) {
						sqlStr.append(turmaAgrupada.getTurma().getCodigo());
					} else {
						sqlStr.append(", ").append(turmaAgrupada.getTurma().getCodigo());
					}
					virgula = true;
				}
				sqlStr.append(") ");

			} else {
				sqlStr.append(" LEFT JOIN Turma ON (Turma.codigo = matriculaperiodoturmadisciplina.turma) ");

				boolean virgula = false;
				sqlStr.append(" WHERE ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo")).append(" AND turma.codigo in(");
				for (TurmaAgrupadaVO turmaAgrupada : listaTurmaAgrupadas) {
					if (!virgula) {
						sqlStr.append(turmaAgrupada.getTurma().getCodigo());
					} else {
						sqlStr.append(", ").append(turmaAgrupada.getTurma().getCodigo());
					}
					virgula = true;
				}
				sqlStr.append(") ");
				sqlStr.append(" AND matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma ");
			}

			if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
				sqlStr.append(" AND turma.unidadeensino = ").append(unidadeEnsino);
			}
			if (ano != null && !ano.equals("") ) {
				sqlStr.append(" AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ");
			}
			if (semestre != null && !semestre.equals("")) {
				sqlStr.append(" AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ");
			}
		}
		sqlStr.append(" AND matriculaperiodoturmadisciplina.reposicao = false ");
		sqlStr.append(" ORDER BY pessoa.nome; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		matriculaVOs = montarDadosConsultaBasica(tabelaResultado);
		return matriculaVOs;
	}

	public List<MatriculaVO> consultaRapidaPorTurmaCursoGradeCurricularAnoSemestreTipoAluno(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, TurmaVO turma, Integer curso, String ano, String semestre, Integer gradeCurricular, Integer unidadeEnsino, String tipoAluno, UsuarioVO usuario) throws Exception {
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaDistinct();
		sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" and matriculaperiodo.codigo  = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
		sqlStr.append(" order by (mp.ano ||'/' || mp.semestre) desc , case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1)  ");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matriculaPeriodo = matriculaPeriodo.codigo ");
		if (turma.getTurmaAgrupada() && !turma.getSubturma()) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.turma in(select turmaAgrupada.turma FROM turmaAgrupada WHERE turmaOrigem = ").append(turma.getCodigo()).append(") and matriculaPeriodoTurmaDisciplina.turmaPratica is null and matriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
		} else if(turma.getSubturma()) {
			if(turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)){
				sqlStr.append(" AND matriculaperiodoturmadisciplina.turmaPratica = ").append(turma.getCodigo());
			}else if(turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)){
				sqlStr.append(" AND matriculaperiodoturmadisciplina.turmaTeorica = ").append(turma.getCodigo());
			}else{
				sqlStr.append(" AND matriculaperiodoturmadisciplina.turma = ").append(turma.getCodigo()).append(" and matriculaperiodoturmadisciplina.turmaPratica is null and matriculaperiodoturmadisciplina.turmaTeorica is null ");
			}
		} else {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.turma = ").append(turma.getCodigo()).append(" and matriculaperiodoturmadisciplina.turmaPratica is null and matriculaperiodoturmadisciplina.turmaTeorica is null ");
		}
		if (tipoAluno.equals("TODOS")) {
			sqlStr.append(" LEFT JOIN Turma ON (Turma.codigo = matriculaperiodoturmadisciplina.turma) ");
			sqlStr.append(" WHERE ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaPeriodo"));
		} else if (tipoAluno.equals("NORMAL")) {
			sqlStr.append(" LEFT JOIN Turma ON (Turma.codigo = matriculaPeriodo.turma) ");
			sqlStr.append(" WHERE ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaPeriodo"));
		} else {
			sqlStr.append(" LEFT JOIN Turma ON (Turma.codigo = matriculaperiodoturmadisciplina.turma) ");
			sqlStr.append(" WHERE ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaPeriodo")).append(" AND matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma");
		}
		if (!curso.equals(0) && !Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" AND curso.codigo = ").append(curso);
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND turma.unidadeensino = ").append(unidadeEnsino);
		}
		if (!ano.equals("") && ano != null && !semestre.equals("") && semestre != null) {
			sqlStr.append(" AND (matriculaPeriodo.ano = '").append(ano).append("' ").append(" AND matriculaPeriodo.semestre = '").append(semestre).append("' ").append(" OR curso.liberarregistroaulaentreperiodo = true) ");
		} else if (!ano.equals("") && ano != null) {
			sqlStr.append(" AND (matriculaPeriodo.ano = '").append(ano).append("' ").append(" OR curso.liberarregistroaulaentreperiodo = true) ");
		} else if (!semestre.equals("") && semestre != null) {
			sqlStr.append(" AND (matriculaPeriodo.semestre = '").append(semestre).append("' ").append(" OR curso.liberarregistroaulaentreperiodo = true) ");
		}
		if (gradeCurricular != 0 && gradeCurricular != null) {
			sqlStr.append(" AND turma.gradecurricular = '").append(gradeCurricular).append("' ");
		}
		// sqlStr.append(" AND (matriculaperiodoturmadisciplina.reposicao = false OR matriculaperiodoturmadisciplina.reposicao is null) ");
		sqlStr.append(" ORDER BY pessoa.nome; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		matriculaVOs = montarDadosConsultaBasica(tabelaResultado);
		return matriculaVOs;
	}

	public List<MatriculaVO> consultaRapidaPorTurmaAnoSemestre(List<TurmaAgrupadaVO> listaTurmaAgrupadas, String ano, String semestre, Integer unidadeEnsino, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaDistinct();
		sqlStr.append("LEFT JOIN Turma ON (Turma.curso = curso.codigo) ");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matricula = matricula.matricula");
		if (listaTurmaAgrupadas.size() > 0) {
			sqlStr.append(" WHERE ");
			String condicao = "";
			for (TurmaAgrupadaVO turmaAgrupada : listaTurmaAgrupadas) {
				sqlStr.append(condicao).append(" matriculaperiodoturmadisciplina.turma = ").append(turmaAgrupada.getTurma().getCodigo());
				condicao = " OR ";
			}
			if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
				sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
			}
			if (!ano.equals("") && ano != null) {
				sqlStr.append(" AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ");
			}
			if (!semestre.equals("") && semestre != null) {
				sqlStr.append(" AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ");
			}
		}
		sqlStr.append(" AND matriculaperiodoturmadisciplina.reposicao = false ");
		sqlStr.append(" ORDER BY pessoa.nome; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		matriculaVOs = montarDadosConsultaBasica(tabelaResultado);
		return matriculaVOs;
	}

	public List<MatriculaVO> consultaRapidaPorTurmaAnoSemestrePeriodoLetivo(List<TurmaAgrupadaVO> listaTurmaAgrupadas, Integer periodoLetivo, String ano, String semestre, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaDistinct();
		sqlStr.append("LEFT JOIN Turma ON (Turma.curso = curso.codigo) ");
		sqlStr.append("INNER JOIN PeriodoLetivo ON (PeriodoLetivo.codigo = turma.periodoLetivo) ");
		sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sqlStr.append("INNER JOIN matriculaperiodo ON matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo AND matriculaperiodo.periodoletivomatricula = periodoletivo.codigo ");
		if (listaTurmaAgrupadas.size() > 0) {
			sqlStr.append(" WHERE ");
			String condicao = "";
			for (TurmaAgrupadaVO turmaAgrupada : listaTurmaAgrupadas) {
				sqlStr.append(condicao).append(" matriculaperiodoturmadisciplina.turma = ").append(turmaAgrupada.getTurma().getCodigo());
				condicao = " OR ";
			}
			if (periodoLetivo.intValue() != 0 && periodoLetivo != null) {
				sqlStr.append(" AND periodoletivo.periodoletivo = ").append(periodoLetivo);
			}
			if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
				sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
			}
			if (!ano.equals("") && ano != null) {
				sqlStr.append(" AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ");
			}
			if (!semestre.equals("") && semestre != null) {
				sqlStr.append(" AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ");
			}
		}
		sqlStr.append(" ORDER BY pessoa.nome; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		matriculaVOs = montarDadosConsultaBasica(tabelaResultado);
		return matriculaVOs;
	}

	public List<MatriculaVO> consultaRapidaPorTurmaAnoSemestrePeriodoLetivoBoletim(List<TurmaAgrupadaVO> listaTurmaAgrupadas, Integer periodoLetivo, String ano, String semestre, Integer unidadeEnsino, UsuarioVO usuario, BimestreEnum bimestre, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota, FiltroRelatorioAcademicoVO filtroAcademicoVO) throws Exception {
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaBoletim();
		sqlStr.append("LEFT JOIN Turma ON (Turma.curso = curso.codigo) ");
		sqlStr.append("INNER JOIN PeriodoLetivo ON (PeriodoLetivo.codigo = turma.periodoLetivo) ");
		sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sqlStr.append("INNER JOIN matriculaperiodo ON matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo AND matriculaperiodo.periodoletivomatricula = periodoletivo.codigo ");
		if (listaTurmaAgrupadas.size() > 0) {
			sqlStr.append(" WHERE 1 = 1 ");
			String condicao = " and ( ";
			for (TurmaAgrupadaVO turmaAgrupada : listaTurmaAgrupadas) {
				sqlStr.append(condicao).append(" matriculaperiodoturmadisciplina.turma = ").append(turmaAgrupada.getTurma().getCodigo());
				condicao = " ) OR ( ";
			}
			sqlStr.append(") ");
			if (periodoLetivo.intValue() != 0 && periodoLetivo != null) {
				sqlStr.append(" AND periodoletivo.codigo = ").append(periodoLetivo);
			}
			if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
				sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
			}
			if (!ano.equals("") && ano != null) {
				sqlStr.append(" AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ");
			}
			if (!semestre.equals("") && semestre != null) {
				sqlStr.append(" AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ");
			}
			sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
			sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		}
		if (situacaoRecuperacaoNota != null && !situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.TODAS)) {
			if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.SEM_RECUPERACAO)) {
				sqlStr.append(" and matricula.matricula not in ( ");
			} else {
				sqlStr.append(" and matricula.matricula in ( ");
			}
			sqlStr.append(" select his.matricula from historiconota ");
			sqlStr.append(" inner join historico his on his.codigo = historiconota.historico ");
			sqlStr.append(" inner join configuracaoacademiconota on configuracaoacademiconota.configuracaoacademico = his.configuracaoacademico ");
			sqlStr.append(" and historiconota.tiponota = configuracaoacademiconota.nota ");
			sqlStr.append(" where configuracaoacademiconota.notaRecuperacao ");
			sqlStr.append(" and his.matricula = matricula.matricula ");
			sqlStr.append(" and his.anohistorico = matriculaperiodoturmadisciplina.ano ");
			sqlStr.append(" and his.semestrehistorico = matriculaperiodoturmadisciplina.semestre ");
			sqlStr.append(" and his.matrizcurricular = matricula.gradecurricularatual ");
			sqlStr.append(" and (his.gradedisciplina is not null or his.gradeCurricularGrupoOptativaDisciplina is not null or his.historicoDisciplinaForaGrade = true or his.gradedisciplinacomposta is not null)");
			sqlStr.append(" and (his.historicoporequivalencia is null or his.historicoporequivalencia = false)");
			if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.NOTA_NAO_RECUPERADA)) {
				sqlStr.append(" and historiconota.situacaorecuperacaonota = 'NOTA_NAO_RECUPERADA' ");
			} else if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.NOTA_RECUPERADA)) {
				sqlStr.append(" and historiconota.situacaorecuperacaonota = 'NOTA_RECUPERADA' ");
			} else {
				sqlStr.append(" and historiconota.situacaorecuperacaonota in ('NOTA_RECUPERADA', 'NOTA_NAO_RECUPERADA', 'EM_RECUPERACAO') ");
			}
			if (bimestre != null) {
				sqlStr.append(" and configuracaoacademiconota.agrupamentoNota = ('").append(bimestre.name()).append("') ");
			}
			sqlStr.append(" order by replace(tiponota, 'NOTA_', '')::INT desc limit 1 ");
			sqlStr.append(" ) ");
		}
		sqlStr.append(" ORDER BY pessoa.nome; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		matriculaVOs = montarDadosConsultaBasicaBoletim(tabelaResultado);
		return matriculaVOs;
	}

	/**
	 * Para consultar somente por nomePessoa passar no atributo filtro
	 * NOMEPESSOA, filtrar somente por matrícula passar no atributo filtro
	 * MATRICULA, para usar os dois passar no atributo filtro
	 * NOMEPESSOAMATRICULA ou para não usar nenhum filtro, passar em branco.
	 */
	public List<MatriculaVO> consultaRapidaPorNomePessoaMatriculaCursoTurmaTurno(String matricula, String nomePessoa, String filtro, Integer curso, Integer turma, Integer turno, Integer unidadeEnsino, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		String condicao = " WHERE ";
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaDistinct();
		sqlStr.append("LEFT JOIN Turma ON (Turma.curso = curso.codigo) ");
		if (filtro.equals("NOMEPESSOA") || filtro.equals("NOMEPESSOAMATRICULA")) {
			sqlStr.append(condicao).append(" sem_acentos(pessoa.nome) ilike (sem_acentos(?)) ");
			condicao = " AND ";
		}
		if (filtro.equals("MATRICULA") || filtro.equals("NOMEPESSOAMATRICULA")) {
			sqlStr.append(condicao).append(" matricula.matricula ilike '").append(matricula).append("%' ");
			condicao = " AND ";
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(condicao).append(" matricula.unidadeensino = ").append(unidadeEnsino);
			condicao = " AND ";
		}
		if (curso.intValue() != 0 && curso != null) {
			sqlStr.append(condicao).append(" matricula.curso = ").append(curso);
			condicao = " AND ";
		}
		if (turno.intValue() != 0 && turno != null) {
			sqlStr.append(condicao).append(" matricula.turno = ").append(turno);
			condicao = " AND ";
		}
		if (turma != 0 && turma != null) {
			sqlStr.append(condicao).append(" turma.codigo = ").append(turma);
		}
		sqlStr.append(" ORDER BY pessoa.nome; ");
		SqlRowSet tabelaResultado = null;
		if (filtro.equals("NOMEPESSOA") || filtro.equals("NOMEPESSOAMATRICULA")) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomePessoa+"%");
		}else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		}
		matriculaVOs = montarDadosConsultaBasica(tabelaResultado);
		return matriculaVOs;
	}

	public List<MatriculaVO> consultaRapidaPorTurmaAnoSemestrePeriodoLetivoGradeCurricular(Integer turma, Integer curso, String ano, String semestre, Integer gradeCurricular, Integer periodoLetivo, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaDistinct();
		sqlStr.append("LEFT JOIN Turma ON (Turma.curso = curso.codigo) ");
		sqlStr.append("INNER JOIN PeriodoLetivo ON (PeriodoLetivo.codigo = turma.periodoLetivo) ");
		sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sqlStr.append("INNER JOIN matriculaperiodo ON matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo AND matriculaperiodo.periodoletivomatricula = periodoletivo.codigo ");
		sqlStr.append(" WHERE matriculaperiodoturmadisciplina.turma = ").append(turma);
		if (curso.intValue() != 0 && curso != null) {
			sqlStr.append(" AND curso.codigo = ").append(curso);
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		if (periodoLetivo.intValue() != 0 && periodoLetivo != null) {
			sqlStr.append(" AND periodoletivo.periodoletivo = ").append(periodoLetivo);
		}
		if (!ano.equals("") && ano != null) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("") && semestre != null) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ");
		}
		if (gradeCurricular != 0 && gradeCurricular != null) {
			sqlStr.append(" AND turma.gradecurricular = '").append(gradeCurricular).append("' ");
		}
		sqlStr.append(" ORDER BY pessoa.nome; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		matriculaVOs = montarDadosConsultaBasica(tabelaResultado);
		return matriculaVOs;
	}

	public List<MatriculaVO> consultaRapidaPorTurmaAnoSemestrePeriodoLetivoGradeCurricularBoletim(TurmaVO turma, Integer curso, String ano, String semestre, Integer gradeCurricular, Integer periodoLetivo, Integer unidadeEnsino, UsuarioVO usuario, BimestreEnum bimestre, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota, FiltroRelatorioAcademicoVO filtroAcademicoVO) throws Exception {
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaBoletim();
		sqlStr.append("LEFT JOIN Turma ON (Turma.curso = curso.codigo) ");
		sqlStr.append("INNER JOIN PeriodoLetivo ON (PeriodoLetivo.codigo = turma.periodoLetivo) ");
		sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sqlStr.append("INNER JOIN matriculaperiodo ON matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo AND matriculaperiodo.periodoletivomatricula = periodoletivo.codigo ");
		sqlStr.append("INNER JOIN historico ON historico.matricula = matricula.matricula AND historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		if(turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
			sqlStr.append(" WHERE matriculaperiodoturmadisciplina.turmapratica = ").append(turma.getCodigo());
		}else if(turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
			sqlStr.append(" WHERE matriculaperiodoturmadisciplina.turmateorica = ").append(turma.getCodigo());
		}else {
			sqlStr.append(" WHERE matriculaperiodoturmadisciplina.turma = ").append(turma.getCodigo());
		}
//		if (curso.intValue() != 0 && curso != null && !Uteis.isAtributoPreenchido(turma)) {
//			sqlStr.append(" AND curso.codigo = ").append(curso);
//		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		if (periodoLetivo.intValue() != 0 && periodoLetivo != null) {
			sqlStr.append(" AND periodoletivo.codigo = ").append(periodoLetivo);
		}
		if (!ano.equals("") && ano != null) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("") && semestre != null) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ");
		}
		if (gradeCurricular != 0 && gradeCurricular != null) {
			sqlStr.append(" AND historico.matrizcurricular = '").append(gradeCurricular).append("' ");
		}
		sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		if (situacaoRecuperacaoNota != null && !situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.TODAS)) {
			if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.SEM_RECUPERACAO)) {
				sqlStr.append(" and matricula.matricula not in ( ");
			} else {
				sqlStr.append(" and matricula.matricula in ( ");
			}
			sqlStr.append(" select his.matricula from historiconota ");
			sqlStr.append(" inner join historico his on his.codigo = historiconota.historico ");
			sqlStr.append(" inner join configuracaoacademiconota on configuracaoacademiconota.configuracaoacademico = his.configuracaoacademico ");
			sqlStr.append(" and historiconota.tiponota = configuracaoacademiconota.nota ");
			sqlStr.append(" where configuracaoacademiconota.notaRecuperacao ");
			sqlStr.append(" and his.matricula = matricula.matricula ");
			sqlStr.append(" and his.anohistorico = matriculaperiodoturmadisciplina.ano ");
			sqlStr.append(" and his.semestrehistorico = matriculaperiodoturmadisciplina.semestre ");
			sqlStr.append(" and his.matrizcurricular = matricula.gradecurricularatual ");
			sqlStr.append(" and (his.gradedisciplina is not null or his.gradeCurricularGrupoOptativaDisciplina is not null or his.historicoDisciplinaForaGrade = true or his.gradedisciplinacomposta is not null)");
			sqlStr.append(" and (his.historicoporequivalencia is null or his.historicoporequivalencia = false)");
			if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.NOTA_NAO_RECUPERADA)) {
				sqlStr.append(" and historiconota.situacaorecuperacaonota = 'NOTA_NAO_RECUPERADA' ");
			} else if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.NOTA_RECUPERADA)) {
				sqlStr.append(" and historiconota.situacaorecuperacaonota = 'NOTA_RECUPERADA' ");
			} else {
				sqlStr.append(" and historiconota.situacaorecuperacaonota in ('NOTA_RECUPERADA', 'NOTA_NAO_RECUPERADA', 'EM_RECUPERACAO') ");
			}
			if (bimestre != null) {
				sqlStr.append(" and configuracaoacademiconota.agrupamentoNota = ('").append(bimestre.name()).append("') ");
			}
			sqlStr.append(" order by replace(tiponota, 'NOTA_', '')::INT desc limit 1 ");
			sqlStr.append(" ) ");
		}
		sqlStr.append(" ORDER BY pessoa.nome; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		matriculaVOs = montarDadosConsultaBasicaBoletim(tabelaResultado);
		return matriculaVOs;
	}

	public List consultarPorMatriculaNivelEducacional(String valorConsulta, Integer unidadeEnsino, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT * FROM matricula");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso");
		sqlStr.append(" WHERE Upper (matricula.matricula) like('").append(valorConsulta.toUpperCase()).append("%') ");
		if (!nivelEducacional.equals("")) {
			sqlStr.append("AND curso.niveleducacional = '").append(nivelEducacional).append("' ");
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public MatriculaVO consultarPorObjetoMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE matricula = ? ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and matricula.unidadeEnsino =").append(unidadeEnsino).append(" ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta);
		if (!tabelaResultado.next()) {
			return new MatriculaVO();
		} else {
			MatriculaVO obj = new MatriculaVO();
			montarDadosBasico(obj, tabelaResultado);
			return obj;
		}
	}

	public MatriculaVO consultarPorObjetoMatricula(String valorConsulta, Integer unidadeEnsino, boolean validarUnidadeEnsinoFinanceira, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorObjetoMatricula(valorConsulta, unidadeEnsino, 0, validarUnidadeEnsinoFinanceira, controlarAcesso, nivelMontarDados, usuario);
	}

	public MatriculaVO consultarPorObjetoMatricula(String valorConsulta, Integer unidadeEnsino, Integer parceiro, boolean validarUnidadeEnsinoFinanceira, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if(!validarUnidadeEnsinoFinanceira){
			return consultarPorObjetoMatricula(valorConsulta, unidadeEnsino, controlarAcesso, nivelMontarDados, null, usuario);
		}else{
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuffer sqlStr = getSQLPadraoConsultaBasica();
			if(Uteis.isAtributoPreenchido(parceiro)){
				sqlStr.append("INNER JOIN contareceber cr ON cr.matriculaaluno = Matricula.matricula");
			}
			List<String> listaParametrosConsulta = new ArrayList<String>(0);
			listaParametrosConsulta.add(valorConsulta);
			sqlStr.append(" WHERE matricula = ? ");
			if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
				listaParametrosConsulta.add(valorConsulta);
				sqlStr.append(" and Matricula.unidadeEnsino in (  ");
				sqlStr.append(" select distinct unidadeensino from contareceber  where matriculaaluno = ? ");
				sqlStr.append(" and unidadeensinofinanceira =").append(unidadeEnsino).append(" ");
				if (Uteis.isAtributoPreenchido(usuario.getUnidadeEnsinoLogado())) {
					sqlStr.append(" and unidadeensino =").append(usuario.getUnidadeEnsinoLogado().getCodigo()).append(" ");
				}
				sqlStr.append(" ) ");
			}
			if(Uteis.isAtributoPreenchido(parceiro)){
				sqlStr.append(" and cr.situacao = 'AR' and cr.parceiro =").append(parceiro);
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), listaParametrosConsulta.toArray());
			if (!tabelaResultado.next()) {
				return new MatriculaVO();
			} else {
				MatriculaVO obj = new MatriculaVO();
				montarDadosBasico(obj, tabelaResultado);
				return obj;
			}
		}
	}

	public MatriculaVO consultarPorObjetoMatriculaListaUnidadeEnsinoVOs(String valorConsulta, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE matricula = '").append(valorConsulta).append("' ");
		if (!listaUnidadeEnsinoVOs.isEmpty()) {
			sqlStr.append(" and matricula.unidadeensino in (");
			for (UnidadeEnsinoVO unidadeEnsino : listaUnidadeEnsinoVOs) {
				if (unidadeEnsino.getFiltrarUnidadeEnsino()) {
					sqlStr.append(unidadeEnsino.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new MatriculaVO();
		} else {
			MatriculaVO obj = new MatriculaVO();
			montarDadosBasico(obj, tabelaResultado);
			return obj;
		}
	}

	public MatriculaVO consultarPorObjetoMatriculaSituacaoMatricula(String matricula, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Matricula WHERE lower (matricula) = '" + matricula.toLowerCase() + "' and situacao = '" + situacao + "'";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr += " and unidadeEnsino = " + unidadeEnsino.intValue();
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new MatriculaVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public MatriculaVO consultarPorObjetoMatriculaParaCancelamentoQuandoSituacaoMatriculaPeriodoAtiva(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		try {
			sqlStr.append("select matricula.* from matricula ");
			sqlStr.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
			sqlStr.append(" where lower(matricula.matricula) like('");
			sqlStr.append(valorConsulta.toLowerCase());
			sqlStr.append("%') ");
			sqlStr.append(" and matriculaPeriodo.situacao = 'AT' and matricula.situacao = 'AT' ");
			if (unidadeEnsino != 0) {
				sqlStr.append(" and matricula.unidadeEnsino =  ");
				sqlStr.append(unidadeEnsino);
			}

			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				return new MatriculaVO();
			}
			return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
		} catch (Exception e) {
			throw e;
		} finally {
			sqlStr = null;
		}

	}

	public MatriculaVO consultarPorObjetoMatriculaDigitadasCompleta(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Matricula WHERE lower (matricula) = '" + valorConsulta.toLowerCase() + "'";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT * FROM Matricula WHERE lower (matricula) like '" + valorConsulta.toLowerCase() + "' and unidadeEnsino = " + unidadeEnsino.intValue();
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new Exception("Por favor, digite a Matrícula completa.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	public MatriculaVO consultarPorObjetoMatriculaAtivaOuTrancada(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		if (unidadeEnsino.intValue() == 0 || unidadeEnsino == null) {
			sqlStr.append(" WHERE UPPER (matricula) = '").append(valorConsulta).append("' and ((matricula.situacao = 'AT') or (matricula.situacao = 'TR') or (matricula.situacao = 'CF') or (matricula.situacao = 'FO')) ");
		} else if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" WHERE UPPER (matricula) = '").append(valorConsulta).append("' and ((matricula.situacao = 'AT') or (matricula.situacao = 'TR') or (matricula.situacao = 'CF') or (matricula.situacao = 'FO')) and unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			montarDadosBasico(obj, tabelaResultado);
			return obj;
		}

		return new MatriculaVO();
		// String sqlStr = "SELECT * FROM Matricula WHERE UPPER (matricula) = '"
		// + valorConsulta.toUpperCase() +
		// "' and ((situacao = 'AT') or (situacao = 'TR') or (situacao = 'CF') or (situacao = 'FO')) ";
		// if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
		// sqlStr = "SELECT * FROM Matricula WHERE UPPER (matricula) = '" +
		// valorConsulta.toUpperCase() +
		// "' and ((situacao = 'AT') or (situacao = 'TR') or (situacao = 'CF') or (situacao = 'FO')) and unidadeEnsino = "
		// + unidadeEnsino.intValue();
		// }
		// SqlRowSet tabelaResultado =
		// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		// if (!tabelaResultado.next()) {
		// return new MatriculaVO();
		// }
		// return (montarDados(tabelaResultado, nivelMontarDados,
		// configuracaoFinanceiro, usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MatriculaVO consultarPorMatriculaCurso(Integer curso, Integer aluno, String matricula, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		// String sqlStr = "SELECT * FROM Matricula WHERE aluno = " +
		// aluno.intValue() + " and curso = " + curso.intValue() +
		// " and lower (matricula) <> '" + matricula.toLowerCase() + "'";
		String sqlStr = "SELECT * FROM Matricula WHERE aluno = " + aluno.intValue() + " and curso = " + curso.intValue() + " and (situacao = 'AT' or situacao = 'AC') and tipomatricula <> 'EX'";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			// sqlStr = "SELECT * FROM Matricula WHERE aluno = " +
			// aluno.intValue() + " and curso = " + curso.intValue() +
			// " and lower (matricula) <> '" + matricula.toLowerCase()
			sqlStr = "SELECT * FROM Matricula WHERE aluno = " + aluno.intValue() + " and curso = " + curso.intValue() + " and unidadeEnsino = " + unidadeEnsino.intValue() + " and (situacao = 'AT' or situacao = 'AC') and tipomatricula <> 'EX'";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe <code>MatriculaVO</code>
	 *         resultantes da consulta.
	 */
	public static List<MatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		List<MatriculaVO> vetResultado = new ArrayList<MatriculaVO>(0);
		while (tabelaResultado.next()) {

			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>MatriculaVO</code>.
	 *
	 * @return O objeto da classe <code>MatriculaVO</code> com os dados
	 *         devidamente montados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public static MatriculaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		MatriculaVO obj = new MatriculaVO();
		obj.setNovoObj(false);
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.getAluno().setCodigo(dadosSQL.getInt("aluno"));
		obj.getCurso().setCodigo(dadosSQL.getInt("curso"));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			montarDadosAluno(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			return obj;
		}

		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setDataBaseSuspensao(dadosSQL.getDate("dataBaseSuspensao"));
		obj.setMatriculaSuspensa(dadosSQL.getBoolean("matriculaSuspensa"));
		obj.setMatriculaSerasa(dadosSQL.getBoolean("matriculaSerasa"));
		obj.setQtdDisciplinasExtensao(dadosSQL.getInt("qtdDisciplinasExtensao"));
		obj.setSituacaoFinanceira(dadosSQL.getString("situacaoFinanceira"));
		obj.getInscricao().setCodigo(dadosSQL.getInt("inscricao"));
		obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
		obj.getTurno().setCodigo(dadosSQL.getInt("turno"));
		obj.getTipoMidiaCaptacao().setCodigo(dadosSQL.getInt("tipoMidiaCaptacao"));
		obj.setDataLiberacaoPendenciaFinanceira(dadosSQL.getDate("dataLiberacaoPendenciaFinanceira"));
		obj.getResponsavelLiberacaoPendenciaFinanceira().setCodigo(dadosSQL.getInt("responsavelLiberacaoPendenciaFinanceira"));
		obj.getTransferenciaEntrada().setCodigo(dadosSQL.getInt("tranferenciaEntrada"));
		obj.getUsuarioLiberacaoDesconto().setCodigo(dadosSQL.getInt("responsavelLiberacaoDesconto"));
		obj.setFormaIngresso(dadosSQL.getString("formaingresso"));
		obj.setProgramaReservaVaga(dadosSQL.getString("programareservavaga"));
		obj.setFinanciamentoEstudantil(dadosSQL.getString("financiamentoestudantil"));
		obj.setApoioSocial(dadosSQL.getString("apoiosocial"));
		obj.setAtividadeComplementar(dadosSQL.getString("atividadecomplementar"));
		obj.setAnoIngresso(dadosSQL.getString("anoingresso"));
		obj.setMesIngresso(dadosSQL.getString("mesingresso"));
		obj.setSemestreIngresso(dadosSQL.getString("semestreingresso"));
		obj.setAnoConclusao(dadosSQL.getString("anoconclusao"));
		obj.setSemestreConclusao(dadosSQL.getString("semestreconclusao"));
		obj.setDisciplinasProcSeletivo(dadosSQL.getString("disciplinasprocseletivo"));
		obj.setTotalPontoProcSeletivo(dadosSQL.getDouble("totalPontoProcSeletivo"));
		obj.setFezEnade(dadosSQL.getBoolean("fezenade"));
		obj.setDataEnade(dadosSQL.getDate("dataEnade"));
		obj.setNotaEnade(dadosSQL.getDouble("notaEnade"));
		obj.setNotaEnem(dadosSQL.getDouble("notaEnem"));
		obj.setObservacaoDiploma(dadosSQL.getString("observacaoDiploma"));
		obj.setHorasComplementares(dadosSQL.getDouble("horasComplementares"));
		obj.getEnadeVO().setCodigo(dadosSQL.getInt("enade"));
		obj.setTituloMonografia(dadosSQL.getString("nomeMonografia"));
		obj.setUpdated(dadosSQL.getTimestamp("updated"));
		obj.getFormacaoAcademica().setCodigo(dadosSQL.getInt("formacaoAcademica"));
		obj.getAutorizacaoCurso().setCodigo(dadosSQL.getInt("autorizacaoCurso"));
		obj.getConsultor().setCodigo(dadosSQL.getInt("consultor"));
		obj.setTipoMatricula(dadosSQL.getString("tipoMatricula"));
		obj.setNaoEnviarMensagemCobranca(dadosSQL.getBoolean("naoEnviarMensagemCobranca"));
		obj.setAlunoConcluiuDisciplinasRegulares(dadosSQL.getBoolean("alunoConcluiuDisciplinasRegulares"));
		obj.setDataColacaoGrau(dadosSQL.getDate("dataColacaoGrau"));
		obj.setAdesivoInstituicaoEntregue(dadosSQL.getBoolean("adesivoInstituicaoEntregue"));
		obj.setTipoTrabalhoConclusaoCurso(dadosSQL.getString("tipoTrabalhoConclusaoCurso"));
		obj.setBloquearEmissaoBoletoMatMenVisaoAluno(dadosSQL.getBoolean("bloquearEmissaoBoletoMatMenVisaoAluno"));
		if (dadosSQL.getObject("notaMonografia") == null) {
			obj.setNotaMonografia((Double) dadosSQL.getObject("notaMonografia"));
		} else {
			obj.setNotaMonografia(dadosSQL.getDouble("notaMonografia"));
		}
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("diaSemanaAula"))) {
        	obj.setDiaSemanaAula(DiaSemana.valueOf(dadosSQL.getString("diaSemanaAula")));
        }else {
        	obj.setDiaSemanaAula(DiaSemana.NENHUM);
        }
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("turnoAula"))) {
        	obj.setTurnoAula(NomeTurnoCensoEnum.valueOf(dadosSQL.getString("turnoAula")));
        }else {
        	obj.setTurnoAula(NomeTurnoCensoEnum.NENHUM);
        }
		obj.setAlunoAbandonouCurso(dadosSQL.getBoolean("alunoAbandonouCurso"));
		obj.setDataInicioCurso(dadosSQL.getDate("datainiciocurso"));
		obj.setDataConclusaoCurso(dadosSQL.getDate("dataconclusaocurso"));
		obj.setLocalArmazenamentoDocumentosMatricula(dadosSQL.getString("localArmazenamentoDocumentosMatricula"));
		obj.getGradeCurricularAtual().setCodigo(dadosSQL.getInt("gradecurricularatual"));
		obj.setUpdated(dadosSQL.getTimestamp("updated"));
		obj.setNovoObj(false);
		obj.setCanceladoFinanceiro(dadosSQL.getBoolean("canceladoFinanceiro"));
		obj.setCodigoFinanceiroMatricula(dadosSQL.getString("codigoFinanceiroMatricula"));
		obj.setBloqueioPorSolicitacaoLiberacaoMatricula(dadosSQL.getBoolean("bloqueioPorSolicitacaoLiberacaoMatricula"));
		obj.setEscolaPublica(dadosSQL.getBoolean("escolaPublica"));
		obj.setAutodeclaracaoPretoPardoIndigena(dadosSQL.getBoolean("autodeclaracaoPretoPardoIndigena"));
		obj.setBolsasAuxilios(dadosSQL.getBoolean("bolsasAuxilios"));
		montarDadosAluno(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosTurno(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosEnade(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosUnidadeEnsino(obj, nivelMontarDados);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setTitulacaoOrientadorMonografia(dadosSQL.getString("titulacaoOrientadorMonografia"));
		obj.setCargaHorariaMonografia(dadosSQL.getInt("cargaHorariaMonografia"));
		montarDadosFormacaoAcademica(obj, nivelMontarDados, usuario);
		obj.setDocumetacaoMatriculaVOs(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculas(obj.getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, false, usuario));
		obj.setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaMatriculaUnico(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setMatriculaPeriodoVOs(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodos(obj.getMatricula(), false, nivelMontarDados, configuracaoFinanceiroVO, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}

		montarDadosInscricao(obj, nivelMontarDados, usuario);
		montarDadosUsuario(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosResponsavelLiberacaoDesconto(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosTipoMidiaCaptacao(obj, nivelMontarDados, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>TipoMidiaCaptacaoVO</code> relacionado ao objeto
	 * <code>MatriculaVO</code>. Faz uso da chave primária da classe
	 * <code>TipoMidiaCaptacaoVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosTipoMidiaCaptacao(MatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTipoMidiaCaptacao().getCodigo().intValue() == 0) {
			obj.setTipoMidiaCaptacao(new TipoMidiaCaptacaoVO());
			return;
		}
		obj.setTipoMidiaCaptacao(getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorChavePrimaria(obj.getTipoMidiaCaptacao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>UsuarioVO</code> relacionado ao objeto <code>MatriculaVO</code>.
	 * Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar
	 * a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavelLiberacaoDesconto(MatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUsuarioLiberacaoDesconto().getCodigo().intValue() == 0) {
			obj.setUsuarioLiberacaoDesconto(new UsuarioVO());
			return;
		}
		obj.setUsuarioLiberacaoDesconto(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuarioLiberacaoDesconto().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>TurnoVO</code> relacionado ao objeto <code>MatriculaVO</code>. Faz
	 * uso da chave primária da classe <code>TurnoVO</code> para realizar a
	 * consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosTurno(MatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurno().getCodigo().intValue() == 0) {
			return;
		}
		obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto <code>MatriculaVO</code>. Faz
	 * uso da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosUsuario(MatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUsuario().getCodigo().intValue() == 0) {
			obj.setUsuario(new UsuarioVO());
			return;
		}
		obj.setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuario().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>InscricaoVO</code> relacionado ao objeto <code>MatriculaVO</code>.
	 * Faz uso da chave primária da classe <code>InscricaoVO</code> para
	 * realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosInscricao(MatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getInscricao().getCodigo().intValue() == 0) {
			obj.setInscricao(new InscricaoVO());
			return;
		}
		obj.setInscricao(getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(obj.getInscricao().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>CursoVO</code> relacionado ao objeto <code>MatriculaVO</code>. Faz
	 * uso da chave primária da classe <code>CursoVO</code> para realizar a
	 * consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public static void montarDadosCurso(MatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCurso().getCodigo().intValue() == 0) {
			return;
		}
		obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
	}

	public static void montarDadosCurso(MatriculaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCurso().getCodigo().intValue() == 0) {
			return;
		}
		if (obj.getCurso().getNivelMontarDados().equals(nivelMontarDados)) {
			return;
		}
		obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>UnidadeEnsinoVO</code> relacionado ao objeto
	 * <code>MatriculaVO</code>. Faz uso da chave primária da classe
	 * <code>UnidadeEnsinoVO</code> para realizar a consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosUnidadeEnsino(MatriculaVO obj, int nivelMontarDados) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, null));
	}

	public static void montarDadosFormacaoAcademica(MatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFormacaoAcademica().getCodigo().intValue() == 0) {
			return;
		}
		obj.setFormacaoAcademica(getFacadeFactory().getFormacaoAcademicaFacade().consultarPorChavePrimaria(obj.getFormacaoAcademica().getCodigo(), usuario));
	}

	public static void montarDadosUnidadeEnsino(MatriculaVO obj, NivelMontarDados nivelMontarDados) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			return;
		}
		if (obj.getUnidadeEnsino().getNivelMontarDados().equals(nivelMontarDados)) {
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto <code>MatriculaVO</code>. Faz
	 * uso da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosAluno(MatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getAluno().getCodigo().intValue() == 0) {
			return;
		}
		// obj.setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getAluno().getCodigo(),
		// false, nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			obj.setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getAluno().getCodigo(), false, nivelMontarDados, usuario));
		} else {
			obj.setAluno(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(obj.getAluno().getCodigo(), false, true, false, usuario));
		}
	}

	public static void montarDadosAluno(MatriculaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getAluno().getCodigo().intValue() == 0) {
			return;
		}
		if (obj.getAluno().getNivelMontarDados().equals(nivelMontarDados)) {
			// Nao precisa montar os dados novamente, pois o nivel de montagem
			// já está igual ao que foi determinado via parametro
			return;
		}
		// obj.setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getAluno().getCodigo(),
		// false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		obj.setAluno(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(obj.getAluno().getCodigo(), false, true, false, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto <code>MatriculaVO</code>. Faz
	 * uso da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 *
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosEnade(MatriculaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getEnadeVO().getCodigo().intValue() == 0) {
			return;
		}
		obj.setEnadeVO(getFacadeFactory().getEnadeFacade().consultarPorChavePrimaria(obj.getEnadeVO().getCodigo(), nivelMontarDados, usuario));
	}

	private void montarDadosBasicoParaExpedicaoDiploma(MatriculaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados da Matrícula
		obj.setNovoObj(false);
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setDataBaseSuspensao(dadosSQL.getDate("dataBaseSuspensao"));
		obj.setMatriculaSuspensa(dadosSQL.getBoolean("matriculaSuspensa"));
		obj.setMatriculaSerasa(dadosSQL.getBoolean("matriculaSerasa"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setSituacaoFinanceira(dadosSQL.getString("situacaoFinanceira"));
		obj.setDataConclusaoCurso(dadosSQL.getDate("dataConclusaoCurso"));
		obj.setDataColacaoGrau(dadosSQL.getDate("dataColacaoGrau"));
		obj.setDataInicioCurso(dadosSQL.getDate("dataInicioCurso"));
		obj.setTipoMatricula(dadosSQL.getString("tipoMatricula"));
		obj.setNotaEnem(dadosSQL.getDouble("notaEnem"));
		obj.setObservacaoDiploma(dadosSQL.getString("observacaoDiploma"));
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Aluno
		obj.getAluno().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
		obj.getAluno().setNome(dadosSQL.getString("Pessoa.nome"));
		obj.getAluno().setSexo(dadosSQL.getString("sexo"));
		obj.getAluno().setCPF(dadosSQL.getString("Pessoa.cpf"));
		obj.getAluno().setRG(dadosSQL.getString("Pessoa.rg"));
		obj.getAluno().setOrgaoEmissor(dadosSQL.getString("Pessoa.orgaoEmissor"));
		obj.getAluno().setDataNasc(dadosSQL.getDate("Pessoa.dataNasc"));
		obj.getAluno().getNacionalidade().setCodigo(dadosSQL.getInt("nacionalidade.codigo"));
		obj.getAluno().getNacionalidade().setNacionalidade(dadosSQL.getString("nacionalidade"));
		obj.getAluno().getNaturalidade().setNome(dadosSQL.getString("naturalidade"));
		obj.getAluno().getNaturalidade().setCodigo(dadosSQL.getInt("naturalidade.codigo"));
		obj.getAluno().setOrgaoEmissor(dadosSQL.getString("Pessoa.orgaoEmissor"));
		obj.getAluno().setEstadoEmissaoRG(dadosSQL.getString("Pessoa.estadoEmissaoRg"));
		obj.getAluno().setDataEmissaoRG(dadosSQL.getDate("Pessoa.dataEmissaoRg"));
		obj.getAluno().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Curso
		obj.getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
		obj.getCurso().setNome(dadosSQL.getString("Curso.nome"));
		obj.getCurso().setNivelEducacional(dadosSQL.getString("Curso.nivelEducacional"));
		obj.getCurso().setQuantidadeDisciplinasOptativasExpedicaoDiploma(dadosSQL.getInt("Curso.QuantidadeDisciplinasOptativasExpedicaoDiploma"));
		// obj.getCurso().setBaseLegal(dadosSQL.getString("baseLegal"));
		obj.getCurso().setHabilitacao(dadosSQL.getString("habilitacao"));
		obj.getCurso().setTitulo(dadosSQL.getString("titulo"));
		obj.getCurso().setTitulacaoDoFormando(dadosSQL.getString("Curso.titulacaoDoFormando"));
		obj.getCurso().setTitulacaoDoFormandoFeminino(dadosSQL.getString("Curso.titulacaoDoFormandoFeminino"));
		obj.getCurso().setQuantidadeDisciplinasOptativasExpedicaoDiploma(dadosSQL.getInt("Curso.QuantidadeDisciplinasOptativasExpedicaoDiploma"));
		obj.getCurso().setAbreviatura(dadosSQL.getString("Curso.abreviatura"));
		obj.getCurso().setPeriodicidade(dadosSQL.getString("Curso.periodicidade"));
		obj.getCurso().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Unidade
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("UnidadeEnsino.codigo"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("UnidadeEnsino.nome"));
		obj.getUnidadeEnsino().setCredenciamentoPortaria(dadosSQL.getString("credenciamentoPortaria"));
		obj.getUnidadeEnsino().setDataPublicacaoDO(dadosSQL.getDate("dataPublicacaoDO"));
		obj.getUnidadeEnsino().getCidade().setNome(dadosSQL.getString("nomeCidadeUnidadeEnsino"));
		obj.getUnidadeEnsino().setMantenedora(dadosSQL.getString("mantenedora"));
		obj.getUnidadeEnsino().setRazaoSocial(dadosSQL.getString("razaoSocial"));
		obj.getUnidadeEnsino().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Turno
		obj.getTurno().setCodigo(dadosSQL.getInt("Turno.codigo"));
		obj.getTurno().setNome(dadosSQL.getString("Turno.nome"));
		obj.getGradeCurricularAtual().setCodigo(dadosSQL.getInt("gradeCurricularAtual"));
	}

	/**
	 * Consulta que espera um ResultSet com os campos mínimos para uma consulta
	 * rápida e intelegente. Desta maneira, a mesma será sempre capaz de montar
	 * os atributos básicos do objeto e alguns atributos relacionados de
	 * relevância para o contexto da aplicação.
	 *
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosBasicoBoletim(MatriculaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados da Matrícula
		obj.setNovoObj(false);
		obj.setMatricula(dadosSQL.getString("matricula"));
		// Dados do Aluno
		obj.getAluno().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
		obj.getAluno().setNome(dadosSQL.getString("Pessoa.nome"));
		obj.getAluno().setCPF(dadosSQL.getString("Pessoa.cpf"));
		obj.getAluno().setRG(dadosSQL.getString("Pessoa.rg"));
		obj.getAluno().setOrgaoEmissor(dadosSQL.getString("Pessoa.orgaoEmissor"));
		obj.getAluno().setEstadoEmissaoRG(dadosSQL.getString("Pessoa.estadoEmissaoRG"));
		obj.getAluno().setSexo(dadosSQL.getString("Pessoa.sexo"));
		obj.getAluno().setDataNasc(dadosSQL.getDate("Pessoa.dataNasc"));
		obj.getAluno().getNaturalidade().setNome(dadosSQL.getString("naturalidade.nome"));
		obj.getAluno().getNaturalidade().getEstado().setSigla(dadosSQL.getString("estadoNaturalidade.sigla"));
		// Dados do Curso
		obj.getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
		obj.getCurso().setNome(dadosSQL.getString("Curso.nome"));
		obj.getTurno().setCodigo(dadosSQL.getInt("Turno.codigo"));
		obj.getTurno().setNome(dadosSQL.getString("Turno.nome"));
		obj.getGradeCurricularAtual().setSistemaAvaliacao(dadosSQL.getString("sistemaAvaliacao"));
		obj.getObservacaoComplementarHistoricoAlunoVO().setObservacao(dadosSQL.getString("observacaohistorico"));
	}

	private void montarDadosBasico(MatriculaVO obj, SqlRowSet dadosSQL) throws Exception {
		/**
		 * Toda vez que adicionar um campo neste montar dados favor adicionar o campo no getSqlConsultaBasicaDistinct
		 */
		// Dados da Matrícula
		obj.setNovoObj(false);
		obj.setMatricula(dadosSQL.getString("matricula"));
		GradeCurricularVO gradeCurricularAtual = new GradeCurricularVO();
		gradeCurricularAtual.setCodigo(dadosSQL.getInt("gradeCurricularAtual"));
		obj.setGradeCurricularAtual(gradeCurricularAtual);
		obj.setData(dadosSQL.getDate("data"));
		obj.setDataBaseSuspensao(dadosSQL.getDate("dataBaseSuspensao"));
		obj.setMatriculaSuspensa(dadosSQL.getBoolean("matriculaSuspensa"));
		obj.setNotaEnem(dadosSQL.getDouble("notaEnem"));
		obj.setObservacaoDiploma(dadosSQL.getString("observacaoDiploma"));
		obj.setMatriculaSerasa(dadosSQL.getBoolean("matriculaSerasa"));
		obj.setQtdDisciplinasExtensao(dadosSQL.getInt("qtdDisciplinasExtensao"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setClassificacaoIngresso(dadosSQL.getInt("classificacaoIngresso"));
		obj.setSituacaoFinanceira(dadosSQL.getString("situacaoFinanceira"));
		obj.setDataConclusaoCurso(dadosSQL.getDate("dataConclusaoCurso"));
		obj.setDataInicioCurso(dadosSQL.getDate("dataInicioCurso"));
		obj.setUpdated(dadosSQL.getTimestamp("updated"));
		obj.getFormacaoAcademica().setCodigo(dadosSQL.getInt("formacaoAcademica"));
		obj.getAutorizacaoCurso().setCodigo(dadosSQL.getInt("autorizacaoCurso"));
		obj.getConsultor().setCodigo(dadosSQL.getInt("consultor"));
		obj.setFormaIngresso(dadosSQL.getString("formaIngresso"));
		obj.setTipoMatricula(dadosSQL.getString("tipoMatricula"));
		obj.setTituloMonografia(dadosSQL.getString("nomeMonografia"));
                obj.setAnoConclusao(dadosSQL.getString("matricula.anoConclusao"));
                obj.setSemestreConclusao(dadosSQL.getString("matricula.semestreConclusao"));
		obj.setNotaMonografia(dadosSQL.getDouble("notaMonografia"));
		obj.setTitulacaoOrientadorMonografia(dadosSQL.getString("titulacaoorientadormonografia"));
		obj.setCargaHorariaMonografia(dadosSQL.getInt("cargahorariamonografia"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("diaSemanaAula"))) {
        	obj.setDiaSemanaAula(DiaSemana.valueOf(dadosSQL.getString("diaSemanaAula")));
        }else {
        	obj.setDiaSemanaAula(DiaSemana.NENHUM);
        }
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("turnoAula"))) {
        	obj.setTurnoAula(NomeTurnoCensoEnum.valueOf(dadosSQL.getString("turnoAula")));
        }else {
        	obj.setTurnoAula(NomeTurnoCensoEnum.NENHUM);
        }
		if(dadosSQL.getString("orientadormonografia") != null) {
			obj.setOrientadorMonografia(dadosSQL.getString("orientadormonografia"));
		}
		if(dadosSQL.getString("tipotrabalhoconclusaocurso") != null) {
			obj.setTipoTrabalhoConclusaoCurso(dadosSQL.getString("tipotrabalhoconclusaocurso"));
		}

		obj.setNaoEnviarMensagemCobranca(dadosSQL.getBoolean("naoEnviarMensagemCobranca"));
		obj.setAlunoConcluiuDisciplinasRegulares(dadosSQL.getBoolean("alunoConcluiuDisciplinasRegulares"));
		obj.setDataColacaoGrau(dadosSQL.getDate("matricula.dataColacaoGrau"));
		obj.setQtdDiasAdiarBloqueio(dadosSQL.getInt("qtdDiasAdiarBloqueio"));
		obj.setLocalArmazenamentoDocumentosMatricula(dadosSQL.getString("localarmazenamentodocumentosmatricula"));
		obj.setDataProcessoSeletivo(dadosSQL.getDate("dataProcessoSeletivo"));
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		obj.setCanceladoFinanceiro(dadosSQL.getBoolean("canceladoFinanceiro"));
		obj.setDataEmissaoHistorico(dadosSQL.getDate("dataEmissaoHistorico"));
		obj.setCodigoFinanceiroMatricula(dadosSQL.getString("codigofinanceiromatricula"));
		obj.setBloqueioPorSolicitacaoLiberacaoMatricula(dadosSQL.getBoolean("bloqueioPorSolicitacaoLiberacaoMatricula"));
		obj.setAnoIngresso(dadosSQL.getString("anoIngresso"));
		obj.setSemestreIngresso(dadosSQL.getString("semestreIngresso"));
		obj.setMesIngresso(dadosSQL.getString("mesIngresso"));
		obj.setDisciplinasProcSeletivo(dadosSQL.getString("disciplinasprocseletivo"));
		// Dados do Aluno
		obj.getAluno().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
		obj.getAluno().setTelefoneRes(dadosSQL.getString("Pessoa.telefoneres"));
		obj.getAluno().setCelular(dadosSQL.getString("Pessoa.celular"));
		obj.getAluno().setNome(dadosSQL.getString("Pessoa.nome"));
		obj.getAluno().setCPF(dadosSQL.getString("Pessoa.cpf"));
		obj.getAluno().setRG(dadosSQL.getString("Pessoa.rg"));
		obj.getAluno().setDataNasc(dadosSQL.getDate("Pessoa.dataNasc"));
		obj.getAluno().setSexo(dadosSQL.getString("Pessoa.sexo"));
		obj.getAluno().setRegistroAcademico(dadosSQL.getString("Pessoa.registroAcademico"));
		// Dados do Arquivo do Aluno
		obj.getAluno().getArquivoImagem().setCodigo(dadosSQL.getInt("codArquivo"));
		obj.getAluno().getArquivoImagem().setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivo"));
		obj.getAluno().getArquivoImagem().setNome(dadosSQL.getString("nomeArquivo"));
		obj.getAluno().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Curso
		obj.getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
		obj.getCurso().setNome(dadosSQL.getString("Curso.nome"));
		obj.getCurso().setNomeDocumentacao(dadosSQL.getString("Curso.nomeDocumentacao"));
        obj.getCurso().setNivelEducacional(dadosSQL.getString("Curso.nivelEducacional"));
		obj.getCurso().setPeriodicidade(dadosSQL.getString("Curso.periodicidade"));
		obj.getCurso().setPerfilEgresso(dadosSQL.getString("Curso.perfilEgresso"));
		obj.getCurso().getAreaConhecimento().setCodigo(dadosSQL.getInt("Curso.areaconhecimento"));
		obj.getCurso().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("Curso.configuracaoAcademico"));
		obj.getCurso().setUtilizarRecursoAvaTerceiros(dadosSQL.getBoolean("Curso.utilizarRecursoAvaTerceiros"));
		obj.getCurso().setAbreviatura(dadosSQL.getString("Curso.abreviatura"));
		obj.getCurso().setPermitirAssinarContratoPendenciaDocumentacao(dadosSQL.getBoolean("Curso.permitirAssinarContratoPendenciaDocumentacao"));
		obj.getCurso().setAtivarPreMatriculaAposEntregaDocumentosObrigatorios(dadosSQL.getBoolean("Curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios"));
		obj.getCurso().setAtivarMatriculaAposAssinaturaContrato(dadosSQL.getBoolean("Curso.ativarMatriculaAposAssinaturaContrato"));	
		obj.getCurso().setTitulo(dadosSQL.getString("curso.titulo"));
		obj.getCurso().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Unidade
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("UnidadeEnsino.codigo"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("UnidadeEnsino.nome"));
		obj.getUnidadeEnsino().setNomeExpedicaoDiploma(dadosSQL.getString("UnidadeEnsino.nomeExpedicaoDiploma"));
		obj.getUnidadeEnsino().setCNPJ(dadosSQL.getString("UnidadeEnsino.cnpj"));
		obj.getUnidadeEnsino().setRazaoSocial(dadosSQL.getString("UnidadeEnsino.razaoSocial"));
		obj.getUnidadeEnsino().setCaminhoBaseLogo(dadosSQL.getString("UnidadeEnsino.caminhoBaseLogo"));
		obj.getUnidadeEnsino().setCaminhoBaseLogoIndex(dadosSQL.getString("UnidadeEnsino.caminhoBaseLogoIndex"));
		obj.getUnidadeEnsino().setNomeArquivoLogoIndex(dadosSQL.getString("UnidadeEnsino.nomeArquivoLogoIndex"));
		obj.getUnidadeEnsino().setNomeArquivoLogo(dadosSQL.getString("UnidadeEnsino.nomeArquivoLogo"));
		obj.getUnidadeEnsino().getCidade().setCodigo(dadosSQL.getInt("UnidadeEnsino.cidade"));
		obj.getUnidadeEnsino().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Conta Corrente UnidadeEnsino
		//obj.getUnidadeEnsino().getContaCorrentePadraoVO().setCodigo(dadosSQL.getInt("contaCorrente.codigo"));
		obj.getUnidadeEnsino().setContaCorrentePadraoMensalidade(dadosSQL.getInt("contaCorrentePadraoMensalidade"));
		obj.getUnidadeEnsino().setContaCorrentePadraoMatricula(dadosSQL.getInt("contaCorrentePadraoMatricula"));
		obj.getUnidadeEnsino().setContaCorrentePadraoBiblioteca(dadosSQL.getInt("contaCorrentePadraoBiblioteca"));
		obj.getUnidadeEnsino().setContaCorrentePadraoDevolucaoCheque(dadosSQL.getInt("contaCorrentePadraoDevolucaoCheque"));
		obj.getUnidadeEnsino().setContaCorrentePadraoMaterialDidatico(dadosSQL.getInt("contaCorrentePadraoMaterialDidatico"));
		obj.getUnidadeEnsino().setContaCorrentePadraoNegociacao(dadosSQL.getInt("contaCorrentePadraoNegociacao"));
		obj.getUnidadeEnsino().setContaCorrentePadraoProcessoSeletivo(dadosSQL.getInt("contaCorrentePadraoProcessoSeletivo"));
//		obj.getUnidadeEnsino().getContaCorrentePadraoVO().setNumero(dadosSQL.getString("contacorrente.numero"));
//		obj.getUnidadeEnsino().getContaCorrentePadraoVO().setDigito(dadosSQL.getString("contacorrente.digito"));
//		obj.getUnidadeEnsino().getContaCorrentePadraoVO().getAgencia().setCodigo(dadosSQL.getInt("contacorrente.agencia"));
//		obj.getUnidadeEnsino().getContaCorrentePadraoVO().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Configurações da UnidadeEnsino
		obj.getUnidadeEnsino().getConfiguracoes().setCodigo(dadosSQL.getInt("Configuracoes.codigo"));
		obj.getUnidadeEnsino().getConfiguracoes().setNome(dadosSQL.getString("Configuracoes.nome"));
		// Dados do Turno
		obj.getTurno().setCodigo(dadosSQL.getInt("Turno.codigo"));
		obj.getTurno().setNome(dadosSQL.getString("Turno.nome"));
		obj.getUsuario().setCodigo(dadosSQL.getInt("Matricula.responsavelMatricula"));
		obj.getMatriculaPeriodoVO().setSituacaoMatriculaPeriodo(dadosSQL.getString("situacaoMatriculaPeriodo"));

	}

	/**
	 * Consulta que espera um ResultSet com os campos mínimos para uma consulta
	 * rápida e intelegente. Desta maneira, a mesma será sempre capaz de montar
	 * os atributos básicos do objeto e alguns atributos relacionados de
	 * relevância para o contexto da aplicação.
	 *
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosBasicoSemTabelaArquivo(MatriculaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados da Matrícula
		obj.setNovoObj(false);
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setSituacaoFinanceira(dadosSQL.getString("situacaoFinanceira"));
		obj.setDataConclusaoCurso(dadosSQL.getDate("dataConclusaoCurso"));
		obj.setDataInicioCurso(dadosSQL.getDate("dataInicioCurso"));
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Aluno
		obj.getAluno().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
		obj.getAluno().setNome(dadosSQL.getString("Pessoa.nome"));
		obj.getAluno().setCPF(dadosSQL.getString("Pessoa.cpf"));
		obj.getAluno().setRG(dadosSQL.getString("Pessoa.rg"));
		obj.getAluno().setDataNasc(dadosSQL.getDate("Pessoa.dataNasc"));
		// Dados do Curso
		obj.getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
		obj.getCurso().setNome(dadosSQL.getString("Curso.nome"));
		obj.getCurso().setNivelEducacional(dadosSQL.getString("Curso.nivelEducacional"));
		obj.getCurso().setPeriodicidade(dadosSQL.getString("Curso.periodicidade"));
		obj.getCurso().setPerfilEgresso(dadosSQL.getString("Curso.perfilEgresso"));
		obj.getCurso().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Unidade
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("UnidadeEnsino.codigo"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("UnidadeEnsino.nome"));
		obj.getUnidadeEnsino().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Turno
		obj.getTurno().setCodigo(dadosSQL.getInt("Turno.codigo"));
		obj.getTurno().setNome(dadosSQL.getString("Turno.nome"));
	}

	/**
	 * Consulta que espera um ResultSet com todos os campos e dados de objetos
	 * relacionados, Para reconstituir o objeto por completo, de uma determinada
	 * entidade.
	 *
	 * @param obj
	 * @throws Exception
	 */
	private void montarDadosCompleto(MatriculaVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		// Dados da Matrícula
		obj.setNovoObj(false);
		obj.setMatricula(dadosSQL.getString("matricula"));
		GradeCurricularVO gradeCurricularAtual = new GradeCurricularVO();
		gradeCurricularAtual.setCodigo(dadosSQL.getInt("gradeCurricularAtual"));
		obj.setGradeCurricularAtual(gradeCurricularAtual);
		obj.getAluno().setCodigo(dadosSQL.getInt("aluno"));
		obj.getCurso().setCodigo(dadosSQL.getInt("curso"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setNotaEnem(dadosSQL.getDouble("notaEnem"));
		obj.setObservacaoDiploma(dadosSQL.getString("observacaoDiploma"));
		obj.setDataBaseSuspensao(dadosSQL.getDate("dataBaseSuspensao"));
		obj.setMatriculaSuspensa(dadosSQL.getBoolean("matriculaSuspensa"));
		obj.setMatriculaSerasa(dadosSQL.getBoolean("matriculaSerasa"));
		obj.setQtdDisciplinasExtensao(dadosSQL.getInt("qtdDisciplinasExtensao"));
		obj.setDataEnvioNotificacao1(dadosSQL.getDate("DataEnvioNotificacao1"));
		obj.setDataEnvioNotificacao2(dadosSQL.getDate("DataEnvioNotificacao2"));
		obj.setDataEnvioNotificacao3(dadosSQL.getDate("DataEnvioNotificacao3"));
		obj.setSituacaoFinanceira(dadosSQL.getString("situacaoFinanceira"));
		obj.setNaoEnviarMensagemCobranca(dadosSQL.getBoolean("naoEnviarMensagemCobranca"));
		obj.setConsiderarParcelasMaterialDidaticoReajustePreco(dadosSQL.getBoolean("considerarParcelasMaterialDidaticoReajustePreco"));
		obj.setClassificacaoIngresso(dadosSQL.getInt("classificacaoIngresso"));
		obj.setAlunoConcluiuDisciplinasRegulares(dadosSQL.getBoolean("alunoConcluiuDisciplinasRegulares"));
		obj.getInscricao().setCodigo(dadosSQL.getInt("inscricao"));
		obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
		obj.getTurno().setCodigo(dadosSQL.getInt("turno"));
		obj.getTipoMidiaCaptacao().setCodigo(dadosSQL.getInt("tipoMidiaCaptacao"));
		obj.setDataLiberacaoPendenciaFinanceira(dadosSQL.getDate("dataLiberacaoPendenciaFinanceira"));
		obj.getResponsavelLiberacaoPendenciaFinanceira().setCodigo(dadosSQL.getInt("responsavelLiberacaoPendenciaFinanceira"));
		obj.getTransferenciaEntrada().setCodigo(dadosSQL.getInt("tranferenciaEntrada"));
		obj.getUsuarioLiberacaoDesconto().setCodigo(dadosSQL.getInt("responsavelLiberacaoDesconto"));
		obj.setFormaIngresso(dadosSQL.getString("formaingresso"));
		obj.setProgramaReservaVaga(dadosSQL.getString("programareservavaga"));
		obj.setFinanciamentoEstudantil(dadosSQL.getString("financiamentoestudantil"));
		obj.setApoioSocial(dadosSQL.getString("apoiosocial"));
		obj.setAtividadeComplementar(dadosSQL.getString("atividadecomplementar"));
		obj.setLocalProcessoSeletivo(dadosSQL.getString("localProcessoSeletivo"));
		obj.setAnoIngresso(dadosSQL.getString("anoingresso"));
		obj.setMesIngresso(dadosSQL.getString("mesingresso"));
		obj.setSemestreIngresso(dadosSQL.getString("semestreingresso"));
		obj.setAnoConclusao(dadosSQL.getString("anoconclusao"));
		obj.setSemestreConclusao(dadosSQL.getString("semestreconclusao"));
		obj.setDisciplinasProcSeletivo(dadosSQL.getString("disciplinasprocseletivo"));
		obj.setTotalPontoProcSeletivo(dadosSQL.getDouble("totalPontoProcSeletivo"));
		// obj.setFezEnade(dadosSQL.getBoolean("fezenade"));
		// obj.setDataEnade(dadosSQL.getDate("dataEnade"));
		// obj.setNotaEnade(dadosSQL.getDouble("notaEnade"));
		obj.setHorasComplementares(dadosSQL.getDouble("horasComplementares"));
		// obj.getEnadeVO().setCodigo(dadosSQL.getInt("enade"));
		obj.setTituloMonografia(dadosSQL.getString("nomeMonografia"));
		obj.setUpdated(dadosSQL.getTimestamp("updated"));
		obj.getFormacaoAcademica().setCodigo(dadosSQL.getInt("formacaoAcademica"));
		obj.getAutorizacaoCurso().setCodigo(dadosSQL.getInt("autorizacaoCurso"));
		obj.getConsultor().setCodigo(dadosSQL.getInt("consultor"));
		obj.setTipoMatricula(dadosSQL.getString("tipoMatricula"));
		obj.setObservacaoComplementar(dadosSQL.getString("observacaoComplementar"));
		obj.setLiberarBloqueioAlunoInadimplente(dadosSQL.getBoolean("liberarBloqueioAlunoInadimplente"));
		obj.setTipoTrabalhoConclusaoCurso(dadosSQL.getString("tipoTrabalhoConclusaoCurso"));
		obj.setBloquearEmissaoBoletoMatMenVisaoAluno(dadosSQL.getBoolean("bloquearEmissaoBoletoMatMenVisaoAluno"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("diaSemanaAula"))) {
        	obj.setDiaSemanaAula(DiaSemana.valueOf(dadosSQL.getString("diaSemanaAula")));
        }else {
        	obj.setDiaSemanaAula(DiaSemana.NENHUM);
        }
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("turnoAula"))) {
        	obj.setTurnoAula(NomeTurnoCensoEnum.valueOf(dadosSQL.getString("turnoAula")));
        }else {
        	obj.setTurnoAula(NomeTurnoCensoEnum.NENHUM);
        }
		if (dadosSQL.getObject("notaMonografia") == null) {
			obj.setNotaMonografia((Double) dadosSQL.getObject("notaMonografia"));
		} else {
			obj.setNotaMonografia(dadosSQL.getDouble("notaMonografia"));
		}

		obj.setAlunoAbandonouCurso(dadosSQL.getBoolean("alunoAbandonouCurso"));
		obj.setDataInicioCurso(dadosSQL.getDate("datainiciocurso"));
		obj.setDataConclusaoCurso(dadosSQL.getDate("dataconclusaocurso"));
		obj.setDataColacaoGrau(dadosSQL.getDate("dataColacaoGrau"));
		obj.setLocalArmazenamentoDocumentosMatricula(dadosSQL.getString("localArmazenamentoDocumentosMatricula"));
		obj.setQtdDiasAdiarBloqueio(dadosSQL.getInt("qtdDiasAdiarBloqueio"));
		obj.setNivelMontarDados(NivelMontarDados.TODOS);
		obj.setCanceladoFinanceiro(dadosSQL.getBoolean("canceladoFinanceiro"));
		obj.setCodigoFinanceiroMatricula(dadosSQL.getString("codigofinanceiromatricula"));
		obj.getRenovacaoReconhecimentoVO().setCodigo(dadosSQL.getInt("renovacaoReconhecimento"));
		obj.setDataEmissaoHistorico(dadosSQL.getDate("dataEmissaoHistorico"));
		obj.setOrientadorMonografia(dadosSQL.getString("orientadorMonografia"));
		obj.setProficienciaLinguaEstrangeira(dadosSQL.getString("proficienciaLinguaEstrangeira"));
		obj.setSituacaoProficienciaLinguaEstrangeira(dadosSQL.getString("situacaoProficienciaLinguaEstrangeira"));
		obj.setBloqueioPorSolicitacaoLiberacaoMatricula(dadosSQL.getBoolean("bloqueioPorSolicitacaoLiberacaoMatricula"));
		// Dados do Aluno
		obj.getAluno().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
		obj.getAluno().setNome(dadosSQL.getString("Pessoa.nome"));
		obj.getAluno().setNomeBatismo(dadosSQL.getString("Pessoa.nomebatismo"));
		obj.getAluno().setCPF(dadosSQL.getString("Pessoa.cpf"));
		obj.getAluno().setRG(dadosSQL.getString("Pessoa.rg"));
		obj.getAluno().setOrgaoEmissor(dadosSQL.getString("Pessoa.orgaoEmissor"));
		obj.getAluno().setEstadoEmissaoRG(dadosSQL.getString("Pessoa.estadoEmissaoRg"));
		obj.getAluno().setDataEmissaoRG(dadosSQL.getDate("Pessoa.dataEmissaoRg"));
		obj.getAluno().setDataNasc(dadosSQL.getDate("Pessoa.dataNasc"));
		obj.getAluno().getNacionalidade().setCodigo(dadosSQL.getInt("nacionalidade.codigo"));
		obj.getAluno().getNacionalidade().setNacionalidade(dadosSQL.getString("nacionalidade.nome"));
		obj.getAluno().getNaturalidade().setCodigo(dadosSQL.getInt("naturalidade.codigo"));
		obj.getAluno().getNaturalidade().setNome(dadosSQL.getString("naturalidade.nome"));
		obj.getAluno().getNaturalidade().setCodigoIBGE(dadosSQL.getString("naturalidade.codigoIBGE"));
		obj.getAluno().setNivelMontarDados(NivelMontarDados.BASICO);
		obj.getAluno().setEndereco(dadosSQL.getString("Pessoa.endereco"));
		obj.getAluno().setNumero(dadosSQL.getString("Pessoa.numero"));
		obj.getAluno().setComplemento(dadosSQL.getString("Pessoa.complemento"));
		obj.getAluno().setSetor(dadosSQL.getString("Pessoa.setor"));
		obj.getAluno().getCidade().getEstado().setSigla(dadosSQL.getString("Estado.sigla"));
		obj.getAluno().getCidade().getEstado().setCodigoIBGE(dadosSQL.getString("Estado.codigoibge"));
		obj.getAluno().getCidade().setNome(dadosSQL.getString("Cidade.nome"));
		obj.getAluno().setCEP(dadosSQL.getString("Pessoa.cep"));
		obj.getAluno().setTelefoneRes(dadosSQL.getString("Pessoa.telefoneres"));
		obj.getAluno().setCelular(dadosSQL.getString("Pessoa.celular"));
		obj.getAluno().setEmail(dadosSQL.getString("Pessoa.email"));
		obj.getAluno().setEstadoCivil(dadosSQL.getString("Pessoa.estadocivil"));
		obj.getAluno().setRegistroAcademico(dadosSQL.getString("Pessoa.registroAcademico"));
		obj.getAluno().setSexo(dadosSQL.getString("Pessoa.sexo"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("Pessoa.tipoAssinaturaDocumentoEnum"))){
			obj.getAluno().setTipoAssinaturaDocumentoEnum(TipoAssinaturaDocumentoEnum.valueOf(dadosSQL.getString("Pessoa.tipoAssinaturaDocumentoEnum")));
		}

		// Dados do Curso
		obj.getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
		obj.getCurso().setNome(dadosSQL.getString("Curso.nome"));
		obj.getCurso().setNomeDocumentacao(dadosSQL.getString("Curso.nomeDocumentacao"));
		obj.getCurso().setNivelEducacional(dadosSQL.getString("Curso.nivelEducacional"));
		obj.getCurso().setPeriodicidade(dadosSQL.getString("Curso.periodicidade"));
		obj.getCurso().setNrPeriodoLetivo(dadosSQL.getInt("Curso.nrPeriodoLetivo"));
		obj.getCurso().setPerfilEgresso(dadosSQL.getString("Curso.perfilEgresso"));
		obj.getCurso().setPublicoAlvo(dadosSQL.getString("Curso.publicoAlvo"));
		obj.getCurso().setTitulo(dadosSQL.getString("Curso.titulo"));
		obj.getCurso().setTitulacaoDoFormando(dadosSQL.getString("Curso.titulacaoDoFormando"));
		obj.getCurso().setTitulacaoDoFormandoFeminino(dadosSQL.getString("Curso.titulacaoDoFormandoFeminino"));
		obj.getCurso().setTitulacaoMasculinoApresentarDiploma(dadosSQL.getString("Curso.titulacaoMasculinoApresentarDiploma"));
		obj.getCurso().setTitulacaoFemininoApresentarDiploma(dadosSQL.getString("Curso.titulacaoFemininoApresentarDiploma"));
		obj.getCurso().setDataPublicacaoDO(dadosSQL.getDate("Curso.dataPublicacaoDO"));
		obj.getCurso().setNrRegistroInterno(dadosSQL.getString("curso.nrRegistroInterno"));
		obj.getCurso().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("Curso.configuracaoAcademico"));
		obj.getCurso().setPreposicaoNomeCurso(dadosSQL.getString("Curso.preposicaoNomeCurso"));
		obj.getCurso().setAtivarPreMatriculaAposEntregaDocumentosObrigatorios(dadosSQL.getBoolean("Curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios"));
		obj.getCurso().setPermitirAssinarContratoPendenciaDocumentacao(dadosSQL.getBoolean("Curso.permitirAssinarContratoPendenciaDocumentacao"));
		obj.getCurso().setAtivarMatriculaAposAssinaturaContrato(dadosSQL.getBoolean("Curso.ativarMatriculaAposAssinaturaContrato"));		
		obj.getCurso().setAbreviatura(dadosSQL.getString("Curso.abreviatura"));
		obj.getCurso().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("Curso.configuracaoAcademico"));
		obj.getCurso().setDataCredenciamento(dadosSQL.getDate("Curso.dataCredenciamento"));
		obj.getCurso().setAutorizacaoResolucaoEmTramitacao(dadosSQL.getBoolean("Curso.autorizacaoresolucaoemtramitacao"));
		obj.getCurso().setNumeroProcessoAutorizacaoResolucao(dadosSQL.getString("Curso.numeroprocessoautorizacaoresolucao"));
		obj.getCurso().setTipoProcessoAutorizacaoResolucao(dadosSQL.getString("Curso.tipoprocessoautorizacaoresolucao"));
		obj.getCurso().setDataCadastroAutorizacaoResolucao(dadosSQL.getDate("Curso.datacadastroautorizacaoresolucao"));
		obj.getCurso().setDataProtocoloAutorizacaoResolucao(dadosSQL.getDate("Curso.dataprotocoloautorizacaoresolucao"));
		obj.getCurso().setTipoAutorizacaoCursoEnum(Uteis.isAtributoPreenchido(dadosSQL.getString("Curso.tipoautorizacaocursoenum")) ? TipoAutorizacaoCursoEnum.valueOf(dadosSQL.getString("Curso.tipoautorizacaocursoenum")) : null);
		obj.getCurso().setNumeroAutorizacao(dadosSQL.getString("Curso.numeroautorizacao"));
		obj.getCurso().setPossuiCodigoEMEC(dadosSQL.getBoolean("Curso.possuicodigoemec"));
		obj.getCurso().setCodigoEMEC(dadosSQL.getInt("Curso.codigoemec"));
		obj.getCurso().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Unidade
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("UnidadeEnsino.codigo"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("UnidadeEnsino.nome"));
		obj.getUnidadeEnsino().setCNPJ(dadosSQL.getString("UnidadeEnsino.CNPJ"));
		obj.getUnidadeEnsino().setNomeExpedicaoDiploma(dadosSQL.getString("UnidadeEnsino.nomeExpedicaoDiploma"));
		obj.getUnidadeEnsino().setCredenciamentoPortaria(dadosSQL.getString("UnidadeEnsino.credenciamentoPortaria"));
		obj.getUnidadeEnsino().setDataPublicacaoDO(dadosSQL.getDate("UnidadeEnsino.dataPublicacaoDo"));
		obj.getUnidadeEnsino().setObservacao(dadosSQL.getString("UnidadeEnsino.observacao"));
		obj.getUnidadeEnsino().setRazaoSocial(dadosSQL.getString("UnidadeEnsino.razaosocial"));
		obj.getUnidadeEnsino().setEnderecoCompleto(dadosSQL.getString("UnidadeEnsino.endereco_completo"));
		obj.getUnidadeEnsino().getCidade().setCodigo(dadosSQL.getInt("UnidadeEnsino.cidade"));
		obj.getUnidadeEnsino().getCidade().setNome(dadosSQL.getString("UnidadeEnsino.cidade.nome"));
		obj.getUnidadeEnsino().getCidade().getEstado().setCodigo(dadosSQL.getInt("UnidadeEnsino.cidade.estado"));
		if(dadosSQL.getInt("pess_orientadorPadraoEstagio.codigo") != 0) {
			obj.getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().setCodigo(dadosSQL.getInt("pess_orientadorPadraoEstagio.codigo"));
			obj.getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().setNome(dadosSQL.getString("pess_orientadorPadraoEstagio.nome"));
			obj.getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().setCPF(dadosSQL.getString("pess_orientadorPadraoEstagio.cpf"));
			obj.getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().setTelefoneRes(dadosSQL.getString("pess_orientadorPadraoEstagio.telefoneRes"));
			obj.getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().setTelefoneComer(dadosSQL.getString("pess_orientadorPadraoEstagio.telefoneComer"));
			obj.getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().setCelular(dadosSQL.getString("pess_orientadorPadraoEstagio.celular"));
//			obj.getUnidadeEnsino().getOrientadorPadraoEstagio().getPessoa().setListaPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarListaPessoaEmailInstitucionalPorPessoa(dadosSQL.getInt("pess_orientadorPadraoEstagio.codigo"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));	
		}
		obj.getUnidadeEnsino().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Conta Corrente UnidadeEnsino
//		obj.getUnidadeEnsino().getContaCorrentePadraoVO().setCodigo(dadosSQL.getInt("contacorrente.codigo"));
		obj.getUnidadeEnsino().setContaCorrentePadraoMensalidade(dadosSQL.getInt("contaCorrentePadraoMensalidade"));
		obj.getUnidadeEnsino().setContaCorrentePadraoMatricula(dadosSQL.getInt("contaCorrentePadraoMatricula"));
		obj.getUnidadeEnsino().setContaCorrentePadraoBiblioteca(dadosSQL.getInt("contaCorrentePadraoBiblioteca"));
		obj.getUnidadeEnsino().setContaCorrentePadraoDevolucaoCheque(dadosSQL.getInt("contaCorrentePadraoDevolucaoCheque"));
		obj.getUnidadeEnsino().setContaCorrentePadraoMaterialDidatico(dadosSQL.getInt("contaCorrentePadraoMaterialDidatico"));
		obj.getUnidadeEnsino().setContaCorrentePadraoNegociacao(dadosSQL.getInt("contaCorrentePadraoNegociacao"));
		obj.getUnidadeEnsino().setContaCorrentePadraoProcessoSeletivo(dadosSQL.getInt("contaCorrentePadraoProcessoSeletivo"));
		obj.setPermiteExecucaoReajustePreco(dadosSQL.getBoolean("permiteexecucaoreajustepreco"));
//		obj.getUnidadeEnsino().getContaCorrentePadraoVO().setNumero(dadosSQL.getString("contacorrente.numero"));
//		obj.getUnidadeEnsino().getContaCorrentePadraoVO().setDigito(dadosSQL.getString("contacorrente.digito"));
//		obj.getUnidadeEnsino().getContaCorrentePadraoVO().getAgencia().setCodigo(dadosSQL.getInt("contacorrente.agencia"));
//		obj.getUnidadeEnsino().getContaCorrentePadraoVO().setNivelMontarDados(NivelMontarDados.BASICO);
		obj.setPermitirInclusaoExclusaoDisciplinasRenovacao(dadosSQL.getBoolean("Matricula.permitirInclusaoExclusaoDisciplinasRenovacao"));


		// Dados do Turno
		obj.getTurno().setCodigo(dadosSQL.getInt("Turno.codigo"));
		obj.getTurno().setNome(dadosSQL.getString("Turno.nome"));
		// Dados do Funcionário
		obj.getConsultor().getPessoa().setCodigo(dadosSQL.getInt("Funcionario.codigo"));
		obj.getConsultor().getPessoa().setNome(dadosSQL.getString("Funcionario.nome"));

		// Turma
		obj.setTurma(dadosSQL.getString("Turma.identificadorTurma"));

		// Documentao da Matrícula
		obj.setDocumetacaoMatriculaVOs(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculas(obj.getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, false, usuario));

		// Dados Filiacao
		obj.getAluno().setFiliacaoVOs(getFacadeFactory().getFiliacaoFacade().consultarFiliacaos(obj.getAluno().getCodigo(), false, usuario));

		// TODO (SEI CA37.1) Modificado para não carregar o Plano Financeiro do
		// Aluno baseado na Matrícula, e sim na última matricula periodo do
		// aluno
		// mais a frente.
		// Plano Financeiro do Aluno
		// obj.setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaMatriculaUnico(obj.getMatricula(),
		// false, Uteis.NIVELMONTARDADOS_DADOSBASICOS));

		MatriculaPeriodoVO matriculaPeriodoVO = null;
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("MatriculaPeriodo.codigo"))) {
			String matricula = "";
			do {
				if (Uteis.isAtributoPreenchido(matricula) && !matricula.equals(dadosSQL.getString("MatriculaPeriodo.matricula"))) {
					dadosSQL.previous();
					break;
				}
				matriculaPeriodoVO = new MatriculaPeriodoVO();
				matriculaPeriodoVO.setCodigo(dadosSQL.getInt("MatriculaPeriodo.codigo"));
				matriculaPeriodoVO.setData(dadosSQL.getDate("MatriculaPeriodo.data"));
				matriculaPeriodoVO.setSituacao(dadosSQL.getString("MatriculaPeriodo.situacao"));
				matriculaPeriodoVO.setSituacaoMatriculaPeriodo(dadosSQL.getString("MatriculaPeriodo.situacaoMatriculaPeriodo"));
				matriculaPeriodoVO.getGradeCurricular().setCodigo(dadosSQL.getInt("MatriculaPeriodo.gradeCurricular"));
				matriculaPeriodoVO.getPeridoLetivo().setCodigo(dadosSQL.getInt("MatriculaPeriodo.periodoLetivoMatricula"));
				matriculaPeriodoVO.getTurma().setCodigo(dadosSQL.getInt("MatriculaPeriodo.turma"));
				matriculaPeriodoVO.setSemestre(dadosSQL.getString("MatriculaPeriodo.semestre"));
				matriculaPeriodoVO.setAno(dadosSQL.getString("MatriculaPeriodo.ano"));
				matriculaPeriodoVO.getUnidadeEnsinoCursoVO().setCodigo(dadosSQL.getInt("MatriculaPeriodo.unidadeEnsinoCurso"));
				matriculaPeriodoVO.setUnidadeEnsinoCurso(dadosSQL.getInt("MatriculaPeriodo.unidadeEnsinoCurso"));
				matriculaPeriodoVO.setCarneEntregue(dadosSQL.getBoolean("MatriculaPeriodo.carneEntregue"));
				matriculaPeriodoVO.setMatricula(dadosSQL.getString("MatriculaPeriodo.matricula"));
				matriculaPeriodoVO.setMatriculaVO(obj);
				matriculaPeriodoVO.getTurma().setDigitoTurma(dadosSQL.getString("Turma.digitoTurma"));

				matricula = matriculaPeriodoVO.getMatricula();
				// str.append("MatriculaPeriodo.responsavelRenovacaoMatricula as \"MatriculaPeriodo.responsavelRenovacaoMatricula\", MatriculaPeriodo.responsavelLiberacaoMatricula as \"MatriculaPeriodo.responsavelLiberacaoMatricula\", ");
				// str.append("MatriculaPeriodo.responsavelEmissaoBoletoMatricula as \"MatriculaPeriodo.responsavelEmissaoBoletoMatricula\", MatriculaPeriodo.dataEmissaoBoletoMatricula as \"MatriculaPeriodo.dataEmissaoBoletoMatricula\", ");
				// str.append("MatriculaPeriodo.responsavelMatriculaForaPrazo as \"MatriculaPeriodo.responsavelMatriculaForaPrazo\", ");
				// str.append("MatriculaPeriodo.turma as \"\", MatriculaPeriodo.dataLiberacaoMatricula as \"MatriculaPeriodo.dataLiberacaoMatricula\", ");
				// str.append("MatriculaPeriodo.nrDocumento as \"MatriculaPeriodo.nrDocumento\", ");
				// MatriculaPeriodo.turmaPeriodo as
				// \"MatriculaPeriodo.turmaPeriodo\", ");
				// MatriculaPeriodo.processoMatricula as
				// \"MatriculaPeriodo.processoMatricula\", ");
				// str.append("MatriculaPeriodo.transferenciaEntrada as \"MatriculaPeriodo.transferenciaEntrada\", MatriculaPeriodo.contratoMatricula as \"MatriculaPeriodo.contratoMatricula\", ");
				// str.append("MatriculaPeriodo.matriculaPeriodoPreMatricula as \"MatriculaPeriodo.matriculaPeriodoPreMatricula\", MatriculaPeriodo.nrDisciplinasIncluidas as \"MatriculaPeriodo.nrDisciplinasIncluidas\", ");
				// str.append("MatriculaPeriodo.nrDisciplinasExcluidas as \"MatriculaPeriodo.nrDisciplinasExcluidas\", MatriculaPeriodo.financeiroManual as \"MatriculaPeriodo.financeiroManual\", ");
				// str.append("MatriculaPeriodo.contratoFiador as \"MatriculaPeriodo.contratoFiador\", ");
				// str.append("MatriculaPeriodo.planoFinanceiroCurso as \"MatriculaPeriodo.planoFinanceiroCurso\", MatriculaPeriodo.condicaoPagamentoPlanoFinanceiroCurso as \"MatriculaPeriodo.condicaoPagamentoPlanoFinanceiroCurso\", ");
				MatriculaPeriodo.montarDadosPeriodoLetivoMatricula(matriculaPeriodoVO, NivelMontarDados.BASICO, usuario);
				obj.getMatriculaPeriodoVOs().add(matriculaPeriodoVO);
				if (dadosSQL.isLast()) {
					return;
				}
			} while (dadosSQL.next());
		}
	}

	private void montarDadosMatricula(MatriculaVO obj, SqlRowSet dadosSQL) throws Exception {
		// Dados da Matrícula
		obj.setNovoObj(false);
		obj.setMatricula(dadosSQL.getString("matricula"));

		GradeCurricularVO gradeCurricularAtual = new GradeCurricularVO();
		gradeCurricularAtual.setCodigo(dadosSQL.getInt("gradeCurricularAtual"));
		obj.setGradeCurricularAtual(gradeCurricularAtual);
		obj.setNotaEnem(dadosSQL.getDouble("notaEnem"));
		obj.setObservacaoDiploma(dadosSQL.getString("observacaoDiploma"));
		obj.getAluno().setCodigo(dadosSQL.getInt("aluno"));
		obj.getCurso().setCodigo(dadosSQL.getInt("curso"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.setData(dadosSQL.getDate("data"));
		obj.setSituacaoFinanceira(dadosSQL.getString("situacaoFinanceira"));
		obj.getInscricao().setCodigo(dadosSQL.getInt("inscricao"));
		obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
		obj.getTurno().setCodigo(dadosSQL.getInt("turno"));
		obj.getTipoMidiaCaptacao().setCodigo(dadosSQL.getInt("tipoMidiaCaptacao"));
		obj.setDataLiberacaoPendenciaFinanceira(dadosSQL.getDate("dataLiberacaoPendenciaFinanceira"));
		obj.getResponsavelLiberacaoPendenciaFinanceira().setCodigo(dadosSQL.getInt("responsavelLiberacaoPendenciaFinanceira"));
		obj.getTransferenciaEntrada().setCodigo(dadosSQL.getInt("tranferenciaEntrada"));
		obj.getUsuarioLiberacaoDesconto().setCodigo(dadosSQL.getInt("responsavelLiberacaoDesconto"));
		obj.setFormaIngresso(dadosSQL.getString("formaingresso"));
		obj.setProgramaReservaVaga(dadosSQL.getString("programareservavaga"));
		obj.setFinanciamentoEstudantil(dadosSQL.getString("financiamentoestudantil"));
		obj.setApoioSocial(dadosSQL.getString("apoiosocial"));
		obj.setAtividadeComplementar(dadosSQL.getString("atividadecomplementar"));
		obj.setAnoIngresso(dadosSQL.getString("anoingresso"));
		obj.setMesIngresso(dadosSQL.getString("mesingresso"));
		obj.setSemestreIngresso(dadosSQL.getString("semestreingresso"));
		obj.setAnoConclusao(dadosSQL.getString("anoconclusao"));
		obj.setSemestreConclusao(dadosSQL.getString("semestreconclusao"));
		obj.setDisciplinasProcSeletivo(dadosSQL.getString("disciplinasprocseletivo"));
		obj.setTotalPontoProcSeletivo(dadosSQL.getDouble("totalPontoProcSeletivo"));
		obj.setFezEnade(dadosSQL.getBoolean("fezenade"));
		obj.setDataEnade(dadosSQL.getDate("dataEnade"));
		obj.setNotaEnade(dadosSQL.getDouble("notaEnade"));
		obj.setHorasComplementares(dadosSQL.getDouble("horasComplementares"));
		obj.getEnadeVO().setCodigo(dadosSQL.getInt("enade"));
		obj.setTituloMonografia(dadosSQL.getString("nomeMonografia"));
		obj.setNaoEnviarMensagemCobranca(dadosSQL.getBoolean("naoEnviarMensagemCobranca"));
		obj.setAlunoConcluiuDisciplinasRegulares(dadosSQL.getBoolean("alunoConcluiuDisciplinasRegulares"));
		if (dadosSQL.getObject("notaMonografia") == null) {
			obj.setNotaMonografia((Double) dadosSQL.getObject("notaMonografia"));
		} else {
			obj.setNotaMonografia(dadosSQL.getDouble("notaMonografia"));
		}

		obj.setAlunoAbandonouCurso(dadosSQL.getBoolean("alunoAbandonouCurso"));
		obj.setDataInicioCurso(dadosSQL.getDate("datainiciocurso"));
		obj.setDataConclusaoCurso(dadosSQL.getDate("dataconclusaocurso"));
		obj.setLocalArmazenamentoDocumentosMatricula(dadosSQL.getString("localArmazenamentoDocumentosMatricula"));
		obj.setCanceladoFinanceiro(dadosSQL.getBoolean("canceladoFinanceiro"));
	}

	public MatriculaVO consultaRapidaFichaAlunoPorMatricula(MatriculaVO obj, String matricula, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct matricula.matricula, matricula.localArmazenamentoDocumentosMatricula, ");
		sqlStr.append("pessoa.nome AS \"aluno.nome\", pessoa.cpf AS \"aluno.cpf\", pessoa.rg AS \"aluno.rg\", pessoa.codigo AS \"aluno.codigo\", ");
		sqlStr.append("UnidadeEnsino.codigo AS \"unidadeEnsino.codigo\", UnidadeEnsino.nome AS \"unidadeEnsino.nome\", ");
		sqlStr.append("curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", ");
		sqlStr.append("turno.codigo AS \"turno.codigo\", turno.nome AS \"turno.nome\", ");
		sqlStr.append("usuario.codigo AS \"usuario.codigo\", usuario.nome AS \"usuario.nome\", ");
		sqlStr.append("processoMatricula.codigo AS \"processoMatricula.codigo\", processoMatricula.descricao AS \"processoMatricula.descricao\", ");
		sqlStr.append("gradeCurricular.codigo AS \"gradeCurricular.codigo\", gradeCurricular.nome AS \"gradeCurricular.nome\", ");
		sqlStr.append("turma.codigo AS \"turma.codigo\", turma.identificadorTurma AS \"turma.identificadorTurma\", ");
		sqlStr.append("condicaoPagamentoPlanoFinanceiroCurso.codigo AS \"condicaoPagamentoPlanoFinanceiroCurso.codigo\", ");
		sqlStr.append("periodoLetivo.codigo AS \"periodoLetivo.codigo\", periodoLetivo.periodoletivo  AS \"periodoLetivo.periodoLetivo\", periodoLetivo.descricao AS \"periodoLetivo.descricao\", ");
		sqlStr.append("matriculaPeriodo.codigo AS \"matriculaPeriodo.codigo\", matriculaPeriodo.data AS \"matriculaPeriodo.data\", matriculaPeriodo.ano AS \"matriculaPeriodo.ano\", matriculaPeriodo.carneEntregue AS \"matriculaPeriodo.carneEntregue\", ");
		sqlStr.append("matriculaPeriodo.semestre AS \"matriculaPeriodo.semestre\", matriculaPeriodo.situacaoMatriculaPeriodo AS \"matriculaPeriodo.situacaoMatriculaPeriodo\", matriculaPeriodo.reconheceuDivida AS \"matriculaPeriodo.reconheceuDivida\" ");
		sqlStr.append("from matricula ");
		sqlStr.append("");
		sqlStr.append("left join pessoa on pessoa.codigo = matricula.aluno ");
		sqlStr.append("left join UnidadeEnsino on unidadeEnsino.codigo = matricula.UnidadeEnsino ");
		sqlStr.append("left join Curso on curso.codigo = matricula.curso ");
		sqlStr.append("left join Turno on turno.codigo = matricula.turno ");
		sqlStr.append("left join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append("left join processoMatricula on processoMatricula.codigo = matriculaPeriodo.processoMatricula ");
		sqlStr.append("left join gradeCurricular on gradeCurricular.codigo = matriculaPeriodo.gradeCurricular ");
		sqlStr.append("left join periodoLetivo on periodoLetivo.codigo = matriculaPeriodo.periodoletivomatricula ");
		sqlStr.append("left join Turma on turma.codigo = matriculaPeriodo.turma ");
		sqlStr.append("left join CondicaoPagamentoPlanoFinanceiroCurso on condicaoPagamentoPlanoFinanceiroCurso.codigo = matriculaPeriodo.condicaoPagamentoPlanoFinanceiroCurso ");
		sqlStr.append("left join Usuario on usuario.codigo = matricula.usuario ");
		sqlStr.append("where matricula.matricula = '");
		sqlStr.append(matricula);
		sqlStr.append("'");
		sqlStr.append(" ORDER BY matriculaPeriodo.ano desc, matriculaPeriodo.semestre desc");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		tabelaResultado.next();
		return montarDadosAcademicoFichaAluno(obj, tabelaResultado, usuario);
	}

	private static MatriculaVO montarDadosAcademicoFichaAluno(MatriculaVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		// Dados da Matrícula
		obj.setNovoObj(false);
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setLocalArmazenamentoDocumentosMatricula(dadosSQL.getString("localArmazenamentoDocumentosMatricula"));
		// Dados do Aluno
		obj.getAluno().setCodigo(dadosSQL.getInt("aluno.codigo"));
		obj.getAluno().setNome(dadosSQL.getString("aluno.nome"));
		obj.getAluno().setCPF(dadosSQL.getString("aluno.cpf"));
		obj.getAluno().setRG(dadosSQL.getString("aluno.rg"));
		// Dados da Unidade Ensino
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));
		// Dados do Curso
		obj.getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getCurso().setNome(dadosSQL.getString("curso.nome"));
		// Dados do Turno
		obj.getTurno().setCodigo(dadosSQL.getInt("turno.codigo"));
		obj.getTurno().setNome(dadosSQL.getString("turno.nome"));

		MatriculaPeriodoVO matriculaPeriodoVO = null;
		do {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
			matriculaPeriodoVO.setCodigo(dadosSQL.getInt("matriculaPeriodo.codigo"));
			matriculaPeriodoVO.setData(dadosSQL.getDate("matriculaPeriodo.data"));
			matriculaPeriodoVO.setAno(dadosSQL.getString("matriculaPeriodo.ano"));
			matriculaPeriodoVO.setSemestre(dadosSQL.getString("matriculaPeriodo.semestre"));
			matriculaPeriodoVO.setSituacaoMatriculaPeriodo(dadosSQL.getString("matriculaPeriodo.situacaoMatriculaPeriodo"));
			matriculaPeriodoVO.getGradeCurricular().setCodigo(dadosSQL.getInt("gradeCurricular.codigo"));
			matriculaPeriodoVO.getGradeCurricular().setNome(dadosSQL.getString("gradeCurricular.nome"));
			matriculaPeriodoVO.getPeridoLetivo().setCodigo(dadosSQL.getInt("periodoLetivo.codigo"));
			matriculaPeriodoVO.getPeridoLetivo().setPeriodoLetivo(dadosSQL.getInt("periodoLetivo.periodoLetivo"));
			matriculaPeriodoVO.getPeridoLetivo().setDescricao(dadosSQL.getString("periodoLetivo.descricao"));
			matriculaPeriodoVO.getTurma().setCodigo(dadosSQL.getInt("turma.codigo"));
			matriculaPeriodoVO.getTurma().setIdentificadorTurma(dadosSQL.getString("turma.identificadorTurma"));
			matriculaPeriodoVO.getProcessoMatriculaVO().setCodigo(dadosSQL.getInt("processoMatricula.codigo"));
			matriculaPeriodoVO.getProcessoMatriculaVO().setDescricao(dadosSQL.getString("processoMatricula.descricao"));
			matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().setCodigo(dadosSQL.getInt("condicaoPagamentoPlanoFinanceiroCurso.codigo"));
			matriculaPeriodoVO.setReconheceuDivida(dadosSQL.getBoolean("matriculaPeriodo.reconheceuDivida"));
			matriculaPeriodoVO.setPossuiDivida(getFacadeFactory().getContaReceberFacade().consultarPendenciaFinanceiraPorCodMatriculaPeriodoFichaAluno(dadosSQL.getInt("matriculaPeriodo.codigo"), usuario));
			matriculaPeriodoVO.setCarneEntregue(dadosSQL.getBoolean("matriculaPeriodo.carneEntregue"));
			MatriculaPeriodo.montarDadosPeriodoLetivoMatricula(matriculaPeriodoVO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			obj.getMatriculaPeriodoVOs().add(matriculaPeriodoVO);
		} while (dadosSQL.next());

		return obj;
	}

	public List<MatriculaVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<MatriculaVO> vetResultado = new ArrayList<MatriculaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<MatriculaVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception {
		List<MatriculaVO> vetResultado = new ArrayList<MatriculaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			montarDadosBasico(obj, tabelaResultado);
			//verificarBloqueioMatricula(obj, usuario, conf);
			if (!obj.getMatriculaBloqueada()) {
				vetResultado.add(obj);
			}
		}
		return vetResultado;
	}

	public List<MatriculaVO> montarDadosConsultaBasicaVerificarBloqueio(SqlRowSet tabelaResultado, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception {
		List<MatriculaVO> vetResultado = new ArrayList<MatriculaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			montarDadosBasico(obj, tabelaResultado);
			verificarBloqueioMatricula(obj, usuario, conf);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public MatriculaVO verificarBloqueioMatricula(MatriculaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception {
			ConfiguracaoGeralSistemaVO confGeralSistemaVO = getAplicacaoControle().getConfiguracaoGeralSistemaVO(obj.getUnidadeEnsino().getCodigo(), usuario);
			if (confGeralSistemaVO == null || confGeralSistemaVO.getCodigo().equals(0)) {
				confGeralSistemaVO = conf;
			}
		if (obj.getMatriculaSuspensa() && !obj.getSituacao().equals(SituacaoVinculoMatricula.CANCELADA.getValor()) 
				&& !obj.getSituacao().equals(SituacaoVinculoMatricula.TRANCADA.getValor()) 
				&& !obj.getSituacao().equals(SituacaoVinculoMatricula.ABANDONO_CURSO.getValor())
				&& !obj.getSituacao().equals(SituacaoVinculoMatricula.TRANSFERENCIA_INTERNA.getValor())
				&& !obj.getSituacao().equals(SituacaoVinculoMatricula.TRANSFERIDA.getValor())
				&& !obj.getSituacao().equals(SituacaoVinculoMatricula.FORMADO.getValor())) {
			getFacadeFactory().getMatriculaFacade().consultarDataAdiamentoSuspensaoMatricula(obj, usuario);
				Boolean bloqueioMatriculaAdiado = consultarVerificandoMatriculaEstaAdiada(obj.getMatricula(), usuario);
//				if (bloqueioMatriculaAdiado) {
//					return obj;
//				}

				obj.setMatriculaBloqueada(!bloqueioMatriculaAdiado);

				Boolean matriculaSuspensaPorInadimplencia = consultarAlunoInadimplentePossuiBloqueioMatricula(obj.getMatricula(), usuario);
				Boolean matriculaSuspensaPorDocumentacao = consultarAlunoInadimplentePossuiBloqueioMatricula(obj.getMatricula(), usuario);

				if (matriculaSuspensaPorInadimplencia || matriculaSuspensaPorDocumentacao) {
					if (matriculaSuspensaPorInadimplencia) {
						obj.setMotivoMatriculaBloqueada("Matrícula Suspensa devido Inadimplência.");
					}
					if (matriculaSuspensaPorDocumentacao) {
						obj.setMotivoMatriculaBloqueada("Matrícula Suspensa devido Documentação Pendente.");
					}
				} else if(!obj.getMotivoMatriculaBloqueada().trim().isEmpty()) {
					obj.setMotivoMatriculaBloqueada(obj.getMotivoMatriculaBloqueada());
				} else {
					obj.setMotivoMatriculaBloqueada("Matrícula Suspensa - Verifique o mapa de suspensão de matrícula!");
				}
		} else if (obj.getSituacao().equals(SituacaoVinculoMatricula.CANCELADA.getValor()) && !confGeralSistemaVO.getPermitirAcessoAlunoEvasao()) {
				obj.setMatriculaBloqueada(Boolean.TRUE);
				obj.setMotivoMatriculaBloqueada("Matrícula Cancelada!");
		} else if (obj.getSituacao().equals(SituacaoVinculoMatricula.TRANCADA.getValor()) && !confGeralSistemaVO.getPermitirAcessoAlunoEvasao()) {
				obj.setMatriculaBloqueada(Boolean.TRUE);
				obj.setMotivoMatriculaBloqueada("Matrícula Trancada!");
		} else if (obj.getSituacao().equals(SituacaoVinculoMatricula.ABANDONO_CURSO.getValor()) && !confGeralSistemaVO.getPermitirAcessoAlunoEvasao()) {
				obj.setMatriculaBloqueada(Boolean.TRUE);
				obj.setMotivoMatriculaBloqueada("Matrícula com Abandono de Curso!");
		} else if (obj.getSituacao().equals(SituacaoVinculoMatricula.TRANSFERIDA.getValor()) && !confGeralSistemaVO.getPermitirAcessoAlunoEvasao()) {
				obj.setMatriculaBloqueada(Boolean.TRUE);
				obj.setMotivoMatriculaBloqueada("Matrícula com Transferência de Saída!");
		} else if (obj.getSituacao().equals(SituacaoVinculoMatricula.TRANSFERENCIA_INTERNA.getValor()) && !confGeralSistemaVO.getPermitirAcessoAlunoEvasao()) {
				obj.setMatriculaBloqueada(Boolean.TRUE);
				obj.setMotivoMatriculaBloqueada("Matrícula com Transferência Interna!");
			} else {
				if(!confGeralSistemaVO.getPermitirAcessoAlunoFormado()){
				Date dataUltimaAula = null;
				if (obj.getTipoMatricula().equals("EX") || obj.getSituacao().equals("FO") || obj.getSituacao().equals("AT")) {
					MatriculaPeriodoVO matPer = (MatriculaPeriodoVO) getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoAtivaPorMatricula(obj.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
					if ((obj.getTipoMatricula().equals("EX") && confGeralSistemaVO.getQtdDiasAcessoAlunoExtensao().equals(0))
							|| (obj.getSituacao().equals("FO") && confGeralSistemaVO.getQtdDiasAcessoAlunoFormado().equals(0))
							|| (obj.getSituacao().equals("AT") && confGeralSistemaVO.getQtdDiasAcessoAlunoAtivo().equals(0))) {
							obj.setMatriculaBloqueada(Boolean.FALSE);
							obj.setMotivoMatriculaBloqueada("");
					}else {
					dataUltimaAula = getFacadeFactory().getHorarioTurmaFacade().consultarUltimaDataAulaPorMatriculaConsiderandoReposicao(matPer.getMatricula());
					if (dataUltimaAula != null) {
						if (dataUltimaAula.before(new Date())) {
							long qtdDias = Uteis.nrDiasEntreDatas(new Date(), dataUltimaAula);
							if (obj.getTipoMatricula().equals("EX")) {
								if (confGeralSistemaVO.getQtdDiasAcessoAlunoExtensao() == 0 || qtdDias <= confGeralSistemaVO.getQtdDiasAcessoAlunoExtensao()) {
									obj.setMatriculaBloqueada(Boolean.FALSE);
									obj.setMotivoMatriculaBloqueada("");
								} else {
									obj.setMatriculaBloqueada(Boolean.TRUE);
									obj.setMotivoMatriculaBloqueada("Matrícula de extensão com prazo para acesso excecido! Dias para acesso após última aula: " + confGeralSistemaVO.getQtdDiasAcessoAlunoExtensao() + " - Data última aula: " + Uteis.getData(dataUltimaAula));
								}
							} else if (obj.getSituacao().equals("FO")) {
								if (confGeralSistemaVO.getQtdDiasAcessoAlunoFormado() == 0 || qtdDias <= confGeralSistemaVO.getQtdDiasAcessoAlunoFormado()) {
									obj.setMatriculaBloqueada(Boolean.FALSE);
									obj.setMotivoMatriculaBloqueada("");
								} else {
									obj.setMatriculaBloqueada(Boolean.TRUE);
									obj.setMotivoMatriculaBloqueada("Matrícula com situação FORMADO, prazo para acesso excecido! Dias para acesso após última aula: " + confGeralSistemaVO.getQtdDiasAcessoAlunoFormado() + " - Data última aula: " + Uteis.getData(dataUltimaAula));
								}
							} else if (obj.getSituacao().equals("AT")) {
								if (confGeralSistemaVO.getQtdDiasAcessoAlunoAtivo() == 0 || qtdDias <= (confGeralSistemaVO.getQtdDiasAcessoAlunoAtivo() + obj.getQtdDiasAdiarBloqueio())) {
									obj.setMatriculaBloqueada(Boolean.FALSE);
									obj.setMotivoMatriculaBloqueada("");
								} else {
									obj.setMatriculaBloqueada(Boolean.TRUE);
									obj.setMotivoMatriculaBloqueada("Matrícula com situação ATIVO, prazo para acesso excecido! Dias para acesso após última aula: " + confGeralSistemaVO.getQtdDiasAcessoAlunoAtivo() + " - Data última aula: " + Uteis.getData(dataUltimaAula));
								}
							}
						} else {
							obj.setMatriculaBloqueada(Boolean.FALSE);
							obj.setMotivoMatriculaBloqueada("");
						}
					} else {
						if (obj.getSituacao().equals("FO")) {
							if (confGeralSistemaVO.getQtdDiasAcessoAlunoFormado() == 0) {
								obj.setMatriculaBloqueada(Boolean.FALSE);
								obj.setMotivoMatriculaBloqueada("");
							} else {
								obj.setMatriculaBloqueada(Boolean.TRUE);
								obj.setMotivoMatriculaBloqueada("Matrícula com situação FORMADO, prazo para acesso excecido!");
							}
						} else {
							obj.setMatriculaBloqueada(Boolean.FALSE);
							obj.setMotivoMatriculaBloqueada("");
						}
					}
				}
				}
				} else {
					obj.setMatriculaBloqueada(Boolean.FALSE);
					obj.setMotivoMatriculaBloqueada("");
				}
			}
			return obj;
	}

	public List<MatriculaVO> montarDadosConsultaBasicaBoletim(SqlRowSet tabelaResultado) throws Exception {
		List<MatriculaVO> vetResultado = new ArrayList<MatriculaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			montarDadosBasicoBoletim(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<MatriculaVO> montarDadosConsultaBasicaSemTabelaArquivo(SqlRowSet tabelaResultado) throws Exception {
		List<MatriculaVO> vetResultado = new ArrayList<MatriculaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			montarDadosBasicoSemTabelaArquivo(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<MatriculaVO> montarDadosConsultaBasicaSemTabelaArquivoComSituacaoIntegralizado(SqlRowSet tabelaResultado, Boolean curriculaIntegralizacao, UsuarioVO usuario) throws Exception {
		try {
			List<MatriculaVO> vetResultado = new ArrayList<MatriculaVO>(0);
			while (tabelaResultado.next()) {
				MatriculaVO obj = new MatriculaVO();
				montarDadosBasicoSemTabelaArquivo(obj, tabelaResultado);
				obj.setDataAtualizacaoMatriculaFormada(tabelaResultado.getDate("dataAtualizacaoMatriculaFormada"));
				obj.getResponsavelAtualizacaoMatriculaFormada().setCodigo(tabelaResultado.getInt("responsavelAtualizacaoMatriculaFormada"));
				obj.getResponsavelAtualizacaoMatriculaFormada().setNome(tabelaResultado.getString("nomeResponsavelAtualizacaoMatriculaFormada"));
				obj.getGradeCurricularAtual().setCodigo(tabelaResultado.getInt("gradeCurricularAtual"));
				if (curriculaIntegralizacao) {
					if (this.isMatriculaIntegralizada(obj, obj.getGradeCurricularAtual().getCodigo(), usuario, null)) {
						vetResultado.add(obj);
					}
				} else {
					vetResultado.add(obj);
				}
			}
			return vetResultado;
		} catch (Exception e) {
			return new ArrayList<MatriculaVO>(0);
		}
	}

	public List<MatriculaVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<MatriculaVO> vetResultado = new ArrayList<MatriculaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			montarDadosCompleto(obj, tabelaResultado, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private StringBuilder getSQLPadraoConsultaBasicaStringBuilder() {
		return new StringBuilder(getSQLPadraoConsultaBasica().toString());
	}

	private StringBuilder getSQLPadraoTotalConsultaBasica(Boolean versaoAplicativo) {
		StringBuilder str = new StringBuilder();
		str.append(" SELECT COUNT(matricula.matricula) AS qtde ");
		if(!versaoAplicativo) {
			str.append(" FROM Matricula ");
			str.append(" LEFT JOIN Pessoa ON (Matricula.aluno = pessoa.codigo) ");
			str.append(" LEFT JOIN Arquivo ON (pessoa.arquivoImagem = arquivo.codigo) ");
			str.append(" LEFT JOIN UnidadeEnsino ON (Matricula.unidadeEnsino = unidadeEnsino.codigo) ");
			str.append(" LEFT JOIN Curso ON (Matricula.curso = curso.codigo) ");
			str.append(" LEFT JOIN Turno ON (Matricula.turno = turno.codigo) ");
			str.append(" LEFT JOIN AreaConhecimento ON (Curso.areaconhecimento = AreaConhecimento.codigo) ");
			str.append(" LEFT JOIN Configuracoes ON ((unidadeEnsino.Configuracoes is not null and Configuracoes.codigo = unidadeEnsino.Configuracoes) or (unidadeEnsino.Configuracoes is null and Configuracoes.padrao)) ");
		}else {
			str.append(" FROM Pessoa ");
			str.append("	LEFT JOIN Arquivo ON (pessoa.arquivoImagem = arquivo.codigo) ");
			str.append("	LEFT JOIN filiacao on filiacao.pais = pessoa.codigo");
			str.append("	LEFT JOIN pessoa aluno on aluno.codigo = filiacao.aluno");
			str.append("	INNER JOIN Matricula ON (Matricula.aluno = pessoa.codigo or Matricula.aluno = aluno.codigo) ");
			str.append("	LEFT JOIN UnidadeEnsino ON (Matricula.unidadeEnsino = unidadeEnsino.codigo) ");
			str.append("	LEFT JOIN Curso ON (Matricula.curso = curso.codigo) ");
			str.append("	LEFT JOIN Turno ON (Matricula.turno = turno.codigo) ");
			str.append("	LEFT JOIN AreaConhecimento ON (Curso.areaconhecimento = AreaConhecimento.codigo) ");
			str.append("	LEFT JOIN Configuracoes ON (Configuracoes.codigo = unidadeEnsino.Configuracoes)");
			str.append("    left join configuracaogeralsistema on configuracaogeralsistema.Configuracoes = Configuracoes.codigo ");
			str.append("    inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and ");
			str.append("    matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' order by ano desc, semestre desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1) ");
		}
		return str;
	}

	private StringBuffer getSQLPadraoConsultaBasicaParaExpedicaoDiploma() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT matricula.matricula, matricula.data, matricula.dataBaseSuspensao, matricula.matriculaSuspensa, matricula.matriculaSerasa, matricula.situacao, matricula.situacaoFinanceira, matricula.dataInicioCurso, matricula.dataConclusaoCurso, matricula.dataColacaoGrau, matricula.tipoMatricula, ");
		str.append(" matricula.notaEnem, matricula.observacaoDiploma,  ");
		str.append("Pessoa.nome as \"Pessoa.nome\", Pessoa.codigo as \"Pessoa.codigo\", Pessoa.cpf as \"Pessoa.cpf\", Pessoa.rg as \"Pessoa.rg\", Pessoa.dataNasc as \"Pessoa.dataNasc\", ");
		str.append("Pessoa.sexo, Pessoa.codigo as \"Pessoa.codigo\", Pessoa.cpf as \"Pessoa.cpf\", Pessoa.rg as \"Pessoa.rg\", Pessoa.dataNasc as \"Pessoa.dataNasc\", ");
		str.append("Curso.nome as \"Curso.nome\", Curso.QuantidadeDisciplinasOptativasExpedicaoDiploma AS \"Curso.QuantidadeDisciplinasOptativasExpedicaoDiploma\", Curso.codigo as \"Curso.codigo\", Curso.nivelEducacional as \"Curso.nivelEducacional\", Curso.titulacaoDoFormando as \"Curso.titulacaoDoFormando\", Curso.titulacaoDoFormandoFeminino as \"Curso.titulacaoDoFormandoFeminino\", ");
		str.append("Curso.habilitacao, Curso.titulo, Curso.abreviatura as \"Curso.abreviatura\", Curso.periodicidade as \"Curso.periodicidade\", Pessoa.orgaoEmissor as \"Pessoa.orgaoEmissor\", Pessoa.estadoEmissaoRg as \"Pessoa.estadoEmissaoRg\", Pessoa.dataEmissaoRg as \"Pessoa.dataEmissaoRg\", ");
		str.append("Nacionalidade.codigo AS \"nacionalidade.codigo\", ");
		str.append("Nacionalidade.nacionalidade, ");
		str.append("Naturalidade.nome AS \"naturalidade\", Naturalidade.codigo AS \"naturalidade.codigo\", UnidadeEnsino.razaoSocial, ");
		str.append("UnidadeEnsino.codigo as \"UnidadeEnsino.codigo\", UnidadeEnsino.nome as \"UnidadeEnsino.nome\", Turno.codigo as \"Turno.codigo\", Turno.nome as \"Turno.nome\", ");
		str.append("UnidadeEnsino.credenciamentoPortaria , UnidadeEnsino.dataPublicacaoDO, UnidadeEnsino.mantenedora, CidadeUnidadeEnsino.nome AS \"nomeCidadeUnidadeEnsino\", matricula.gradeCurricularAtual ");
		str.append(" FROM Matricula ");
		str.append(" LEFT JOIN Pessoa ON (Matricula.aluno = pessoa.codigo) ");
		str.append("      LEFT JOIN UnidadeEnsino ON (Matricula.unidadeEnsino = unidadeEnsino.codigo) ");
		str.append("      LEFT JOIN Curso ON (Matricula.curso = curso.codigo) ");
		str.append("      LEFT JOIN Turno ON (Matricula.turno = turno.codigo) ");
		str.append("      LEFT JOIN Cidade CidadeUnidadeEnsino ON (UnidadeEnsino.cidade = CidadeUnidadeEnsino.codigo) ");
		str.append("      LEFT JOIN Paiz Nacionalidade ON (Pessoa.nacionalidade = Nacionalidade.codigo) ");
		str.append("      LEFT JOIN Cidade Naturalidade ON (Pessoa.naturalidade = Naturalidade.codigo) ");
		return str;
	}

	private StringBuffer getSQLPadraoConsultaBasica() {
		return getSQLPadraoConsultaBasica(false, false, false);
	}

	private StringBuffer getSQLPadraoConsultaBasica(Boolean versaoAplicativo, boolean isLogin, boolean visaoPais) {
		StringBuffer str = new StringBuffer();
		str.append(" SELECT distinct case when matricula.situacao = 'AT' then 0 else 1 end as ordenacao, matricula.matricula, matricula.gradeCurricularAtual, matricula.data, matricula.dataBaseSuspensao, matricula.matriculaSuspensa, matricula.matriculaSerasa, matricula.situacao, matricula.situacaoFinanceira, matricula.dataInicioCurso, matricula.dataConclusaoCurso, matricula.updated, matricula.naoEnviarMensagemCobranca, matricula.alunoConcluiuDisciplinasRegulares, matricula.qtdDisciplinasExtensao, matricula.qtdDiasAdiarBloqueio, ");
		str.append(" matricula.formacaoAcademica, matricula.autorizacaoCurso, matricula.consultor, matricula.classificacaoIngresso, matricula.formaIngresso, matricula.tipoMatricula, matricula.nomeMonografia, matricula.notaMonografia, matricula.orientadormonografia, matricula.tipotrabalhoconclusaocurso,  matricula.dataColacaoGrau AS \"matricula.dataColacaoGrau\", matricula.dataProcessoSeletivo, matricula.diaSemanaAula, matricula.turnoAula, ");
		str.append(" matricula.anoConclusao as \"matricula.anoConclusao\", matricula.semestreConclusao as \"matricula.semestreConclusao\", matricula.notaEnem, matricula.observacaoDiploma, matricula.localarmazenamentodocumentosmatricula, matricula.canceladoFinanceiro, matricula.dataemissaohistorico, matricula.codigofinanceiromatricula,  matricula.anoIngresso ,matricula.semestreIngresso , matricula.mesIngresso, matricula.disciplinasprocseletivo, matricula.cargahorariamonografia, matricula.titulacaoorientadormonografia,");
		if (versaoAplicativo && isLogin && visaoPais) {
			str.append(" aluno.nome as \"Pessoa.nome\", aluno.codigo as \"Pessoa.codigo\", aluno.sexo as \"Pessoa.sexo\", aluno.cpf as \"Pessoa.cpf\", aluno.rg as \"Pessoa.rg\",  aluno.orgaoEmissor as \"Pessoa.orgaoEmissor\", aluno.dataNasc as \"Pessoa.dataNasc\", aluno.sexo as \"Pessoa.sexo\",  aluno.registroAcademico as \"Pessoa.registroAcademico\" , ");
			str.append(" aluno.arquivoImagem as codArquivo, arquivo.pastaBaseArquivo, arquivo.nome AS nomeArquivo, ");
			str.append(" Curso.nomeDocumentacao as \"Curso.nomeDocumentacao\", Curso.nome as \"Curso.nome\", Curso.abreviatura as \"Curso.abreviatura\", aluno.telefoneres AS \"pessoa.telefoneres\", aluno.celular AS \"pessoa.celular\", Curso.areaconhecimento as \"Curso.areaconhecimento\", AreaConhecimento.nome as \"AreaConhecimento.nome\", Curso.codigo as \"Curso.codigo\", Curso.nivelEducacional as \"Curso.nivelEducacional\", Curso.periodicidade as \"Curso.periodicidade\", Curso.perfilEgresso as \"Curso.perfilEgresso\", Curso.utilizarRecursoAvaTerceiros as \"Curso.utilizarRecursoAvaTerceiros\",");
		} else {
			str.append(" Pessoa.nome as \"Pessoa.nome\", Pessoa.codigo as \"Pessoa.codigo\", Pessoa.sexo as \"Pessoa.sexo\", Pessoa.cpf as \"Pessoa.cpf\", Pessoa.rg as \"Pessoa.rg\",  Pessoa.orgaoEmissor as \"Pessoa.orgaoEmissor\", Pessoa.dataNasc as \"Pessoa.dataNasc\", Pessoa.sexo as \"Pessoa.sexo\",  Pessoa.registroAcademico as \"Pessoa.registroAcademico\" , ");
			str.append(" Pessoa.arquivoImagem as codArquivo, arquivo.pastaBaseArquivo, arquivo.nome AS nomeArquivo, ");
			str.append(" Curso.nomeDocumentacao as \"Curso.nomeDocumentacao\", Curso.nome as \"Curso.nome\", Curso.abreviatura as \"Curso.abreviatura\", pessoa.telefoneres AS \"pessoa.telefoneres\", pessoa.celular AS \"pessoa.celular\", Curso.areaconhecimento as \"Curso.areaconhecimento\", AreaConhecimento.nome as \"AreaConhecimento.nome\", Curso.codigo as \"Curso.codigo\", Curso.nivelEducacional as \"Curso.nivelEducacional\", Curso.periodicidade as \"Curso.periodicidade\", Curso.perfilEgresso as \"Curso.perfilEgresso\", Curso.utilizarRecursoAvaTerceiros as \"Curso.utilizarRecursoAvaTerceiros\",");
		}
		str.append(" Curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios as \"Curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios\" , Curso.permitirAssinarContratoPendenciaDocumentacao as \"Curso.permitirAssinarContratoPendenciaDocumentacao\" , Curso.ativarMatriculaAposAssinaturaContrato   as  \"Curso.ativarMatriculaAposAssinaturaContrato\" , ");
		str.append(" UnidadeEnsino.codigo as \"UnidadeEnsino.codigo\", UnidadeEnsino.nome as \"UnidadeEnsino.nome\", UnidadeEnsino.razaoSocial AS \"UnidadeEnsino.razaoSocial\", UnidadeEnsino.cnpj AS \"UnidadeEnsino.cnpj\", UnidadeEnsino.cidade as \"UnidadeEnsino.cidade\", Turno.codigo as \"Turno.codigo\", Turno.nome as \"Turno.nome\", ");
		str.append(" UnidadeEnsino.caminhoBaseLogo as \"UnidadeEnsino.caminhoBaseLogo\", UnidadeEnsino.nomeArquivoLogo as \"UnidadeEnsino.nomeArquivoLogo\",  ");
		str.append(" UnidadeEnsino.caminhoBaseLogoIndex as \"UnidadeEnsino.caminhoBaseLogoIndex\", UnidadeEnsino.nomeArquivoLogoIndex as \"UnidadeEnsino.nomeArquivoLogoIndex\", UnidadeEnsino.nomeExpedicaoDiploma as \"UnidadeEnsino.nomeExpedicaoDiploma\", ");
		str.append(" unidadeensino.contaCorrentePadraoMensalidade, unidadeensino.contaCorrentePadraoMatricula, unidadeensino.contaCorrentePadraoBiblioteca, ");
		str.append(" unidadeensino.contaCorrentePadraoDevolucaoCheque, unidadeensino.contaCorrentePadraoMaterialDidatico, unidadeensino.contaCorrentePadraoNegociacao, ");
		str.append(" unidadeensino.contaCorrentePadraoProcessoSeletivo, ");
		str.append(" Configuracoes.codigo AS \"Configuracoes.codigo\", Configuracoes.nome AS \"Configuracoes.nome\", curso.titulo as \"curso.titulo\", ");
		str.append(" Curso.configuracaoAcademico as \"Curso.configuracaoAcademico\", Matricula.usuario as \"Matricula.responsavelMatricula\", Matricula.bloqueioPorSolicitacaoLiberacaoMatricula, ");
		str.append(" (case ");
		str.append(" when matricula.situacao = 'AT' then '0' ");
		str.append(" when matricula.situacao =  'PR' then '1'	");
		str.append(" when matricula.situacao  in ('AC', 'TR', 'CA', 'FI', 'FO', 'JU', 'PC') then '2' ");
		str.append(" when matricula.situacao in ('TS', 'TI', 'ER') then '3' ");
		str.append(" end ||  ");
		str.append(" case ");
		str.append(" when curso.niveleducacional = 'MT' then '0' ");
		str.append(" when curso.niveleducacional =  'PO' then '1'	");
		str.append(" when curso.niveleducacional in ('SU', 'GT') then '2' ");
		str.append(" when curso.niveleducacional = 'ME' then '3' ");
		str.append(" when curso.niveleducacional = 'BA' then '4' ");
		str.append(" when curso.niveleducacional = 'IN' then '5' ");
		str.append(" when curso.niveleducacional = 'PR' then '6' ");
		str.append(" when curso.niveleducacional in ('EX', 'SE') then '7' ");
		str.append(" end) as situacaoMatriculaOrdenar,  ");
		str.append(" count(*) over() as qtde_total_registros,  ");
		if(!versaoAplicativo) {
			str.append(" '' as situacaoMatriculaPeriodo FROM Matricula ");
			str.append(" LEFT JOIN Pessoa ON (Matricula.aluno = pessoa.codigo) ");
			str.append(" LEFT JOIN Arquivo ON (pessoa.arquivoImagem = arquivo.codigo) ");
			str.append(" LEFT JOIN UnidadeEnsino ON (Matricula.unidadeEnsino = unidadeEnsino.codigo) ");
			str.append(" LEFT JOIN Curso ON (Matricula.curso = curso.codigo) ");
			str.append(" LEFT JOIN Turno ON (Matricula.turno = turno.codigo) ");
			str.append(" LEFT JOIN AreaConhecimento ON (Curso.areaconhecimento = AreaConhecimento.codigo) ");
			str.append(" LEFT JOIN Configuracoes ON ((unidadeEnsino.Configuracoes is not null and Configuracoes.codigo = unidadeEnsino.Configuracoes) or (unidadeEnsino.Configuracoes is null and Configuracoes.padrao)) ");
		}else {
			str.append(" matriculaperiodo.situacaoMatriculaPeriodo as situacaoMatriculaPeriodo FROM Pessoa ");
			if (isLogin) {
				if(visaoPais){
					str.append(" LEFT JOIN filiacao on filiacao.pais = pessoa.codigo");
					str.append(" LEFT JOIN pessoa aluno on aluno.codigo = filiacao.aluno");	
					str.append(" INNER JOIN Matricula on Matricula.aluno = aluno.codigo");	
				} else {
					str.append(" INNER JOIN pessoa aluno on aluno.codigo = pessoa.codigo");
					str.append(" INNER JOIN Matricula on Matricula.aluno = pessoa.codigo");
				}
			}
			str.append("	LEFT JOIN Arquivo ON (pessoa.arquivoImagem = arquivo.codigo) ");
			if (!isLogin) {
				str.append("	LEFT JOIN filiacao on filiacao.pais = pessoa.codigo");
				str.append("	LEFT JOIN pessoa aluno on aluno.codigo = filiacao.aluno");
				str.append("	INNER JOIN Matricula ON (Matricula.aluno = pessoa.codigo or Matricula.aluno = aluno.codigo) ");
			}
			str.append("	LEFT JOIN UnidadeEnsino ON (Matricula.unidadeEnsino = unidadeEnsino.codigo) ");
			str.append("	LEFT JOIN Curso ON (Matricula.curso = curso.codigo) ");
			str.append("	LEFT JOIN Turno ON (Matricula.turno = turno.codigo) ");
			str.append("	LEFT JOIN AreaConhecimento ON (Curso.areaconhecimento = AreaConhecimento.codigo) ");
			str.append("	LEFT JOIN Configuracoes ON (Configuracoes.codigo = unidadeEnsino.Configuracoes)");
			str.append("    left join configuracaogeralsistema on configuracaogeralsistema.Configuracoes = Configuracoes.codigo ");
			str.append("    inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and ");
			str.append("    matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' order by ano desc, semestre desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1) ");
		}
		return str;
	}

	private StringBuffer getSQLPadraoConsultaBasicaDistinct() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT distinct matricula.matricula, matricula.gradeCurricularAtual, matricula.data, matricula.naoEnviarMensagemCobranca, matricula.alunoConcluiuDisciplinasRegulares, matricula.dataBaseSuspensao, matricula.matriculaSuspensa, matricula.matriculaSerasa, matricula.situacao, matricula.situacaoFinanceira, matricula.dataInicioCurso, matricula.dataConclusaoCurso, matricula.updated, matricula.qtdDisciplinasExtensao, matricula.qtdDiasAdiarBloqueio, matricula.diaSemanaAula, matricula.turnoAula, ");
		str.append("matricula.anoConclusao as \"matricula.anoConclusao\", matricula.semestreConclusao as \"matricula.semestreConclusao\", matricula.canceladoFinanceiro, matricula.formacaoAcademica, matricula.autorizacaoCurso, matricula.consultor, matricula.classificacaoIngresso, matricula.formaingresso, matricula.tipoMatricula, matricula.nomeMonografia, matricula.notaMonografia, matricula.dataColacaoGrau AS \"matricula.dataColacaoGrau\", matricula.notaEnem, matricula.observacaoDiploma, matricula.localarmazenamentodocumentosmatricula, matricula.dataProcessoSeletivo, matricula.dataemissaohistorico, matricula.codigofinanceiromatricula, matricula.anoingresso, matricula.semestreingresso, matricula.mesingresso, matricula.disciplinasprocseletivo , matricula.orientadorMonografia,matricula.tipotrabalhoconclusaocurso, matricula.cargahorariamonografia, matricula.titulacaoorientadormonografia, ");
		str.append("Pessoa.nome as \"Pessoa.nome\", Pessoa.codigo as \"Pessoa.codigo\", Pessoa.telefoneres as \"Pessoa.telefoneres\", Pessoa.celular as \"Pessoa.celular\", Pessoa.cpf as \"Pessoa.cpf\", Pessoa.rg as \"Pessoa.rg\", Pessoa.dataNasc as \"Pessoa.dataNasc\", Pessoa.sexo as \"Pessoa.sexo\", Pessoa.orgaoEmissor as \"Pessoa.orgaoEmissor\", Pessoa.registroAcademico as \"Pessoa.registroAcademico\" ,");
		str.append("Pessoa.arquivoImagem as codArquivo, arquivo.pastaBaseArquivo, arquivo.nome AS nomeArquivo, ");
		str.append("Curso.nomeDocumentacao as \"Curso.nomeDocumentacao\", Curso.nome as \"Curso.nome\", Curso.codigo as \"Curso.codigo\", Curso.abreviatura as \"Curso.abreviatura\", Curso.nivelEducacional as \"Curso.nivelEducacional\", Curso.periodicidade as \"Curso.periodicidade\", Curso.perfilEgresso as \"Curso.perfilEgresso\", Curso.areaconhecimento as \"Curso.areaconhecimento\", Curso.configuracaoAcademico as \"Curso.configuracaoAcademico\", Curso.utilizarRecursoAvaTerceiros as \"Curso.utilizarRecursoAvaTerceiros\",");
		str.append("Curso.permitirAssinarContratoPendenciaDocumentacao as \"Curso.permitirAssinarContratoPendenciaDocumentacao\",  Curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios as \"Curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios\", Curso.ativarMatriculaAposAssinaturaContrato as  \"Curso.ativarMatriculaAposAssinaturaContrato\" , ");
		str.append("Curso.titulo as \"Curso.titulo\", ");
		str.append("UnidadeEnsino.codigo as \"UnidadeEnsino.codigo\", UnidadeEnsino.cnpj as \"UnidadeEnsino.cnpj\", UnidadeEnsino.razaoSocial as \"UnidadeEnsino.razaoSocial\", UnidadeEnsino.nome as \"UnidadeEnsino.nome\",UnidadeEnsino.cidade as \"UnidadeEnsino.cidade\", Turno.codigo as \"Turno.codigo\", Turno.nome as \"Turno.nome\", ");
		str.append("UnidadeEnsino.caminhoBaseLogo as \"UnidadeEnsino.caminhoBaseLogo\", UnidadeEnsino.caminhoBaseLogoIndex \"UnidadeEnsino.caminhoBaseLogoIndex\", ");
		str.append("UnidadeEnsino.nomeArquivoLogoIndex as \"UnidadeEnsino.nomeArquivoLogoIndex\", UnidadeEnsino.nomeArquivoLogo \"UnidadeEnsino.nomeArquivoLogo\", ");
		str.append("UnidadeEnsino.nomeExpedicaoDiploma as \"UnidadeEnsino.nomeExpedicaoDiploma\", ");
		str.append("unidadeensino.contaCorrentePadraoMensalidade, unidadeensino.contaCorrentePadraoMatricula, unidadeensino.contaCorrentePadraoBiblioteca, ");
		str.append("unidadeensino.contaCorrentePadraoDevolucaoCheque, unidadeensino.contaCorrentePadraoMaterialDidatico, unidadeensino.contaCorrentePadraoNegociacao, unidadeensino.contaCorrentePadraoProcessoSeletivo, ");
		str.append("contacorrente.codigo as \"contacorrente.codigo\", contacorrente.numero as \"contacorrente.numero\", contacorrente.digito as \"contacorrente.digito\", contacorrente.agencia as \"contacorrente.agencia\", ");
		str.append("Configuracoes.codigo AS \"Configuracoes.codigo\", Configuracoes.nome AS \"Configuracoes.nome\", Matricula.usuario as \"Matricula.responsavelMatricula\", Matricula.bloqueioPorSolicitacaoLiberacaoMatricula, '' as situacaoMatriculaPeriodo ");
		str.append("FROM Matricula ");
		str.append("LEFT JOIN Pessoa ON (Matricula.aluno = pessoa.codigo) ");
		str.append("LEFT JOIN Arquivo ON (pessoa.arquivoImagem = arquivo.codigo) ");
		str.append("LEFT JOIN UnidadeEnsino ON (Matricula.unidadeEnsino = unidadeEnsino.codigo) ");
		str.append("LEFT JOIN ContaCorrente ON (ContaCorrente.codigo = unidadeEnsino.contaCorrentePadrao) ");
		str.append("LEFT JOIN Curso ON (Matricula.curso = curso.codigo) ");
		str.append("LEFT JOIN Turno ON (Matricula.turno = turno.codigo) ");
		str.append("LEFT JOIN Configuracoes ON (Configuracoes.codigo = unidadeEnsino.Configuracoes) ");
		return str;
	}

	private StringBuffer getSQLPadraoConsultaBasicaBoletim() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT distinct matricula.matricula, ");
		str.append(" Pessoa.nome as \"Pessoa.nome\", Pessoa.codigo as \"Pessoa.codigo\", Pessoa.cpf as \"Pessoa.cpf\", Pessoa.rg as \"Pessoa.rg\", ");
		str.append(" Pessoa.orgaoEmissor as \"Pessoa.orgaoEmissor\", Pessoa.estadoEmissaoRG as \"Pessoa.estadoEmissaoRG\", Pessoa.sexo as \"Pessoa.sexo\", ");
		str.append(" Pessoa.datanasc as \"Pessoa.datanasc\", naturalidade.nome as \"naturalidade.nome\", estadoNaturalidade.sigla as \"estadoNaturalidade.sigla\", ");
		str.append(" Curso.nomeDocumentacao as \"Curso.nomeDocumentacao\", Curso.nome as \"Curso.nome\", Curso.codigo as \"Curso.codigo\", ");
		str.append(" Turno.nome as \"Turno.nome\", Turno.codigo as \"Turno.codigo\", ");
		str.append(" gradeCurricular.sistemaAvaliacao, ");
		str.append(" observacaocomplementarhistoricoaluno.observacao as observacaohistorico ");
		str.append(" FROM Matricula ");
		str.append(" LEFT JOIN Pessoa ON (Matricula.aluno = pessoa.codigo) ");
		str.append(" LEFT JOIN Cidade as naturalidade ON (naturalidade.codigo = pessoa.naturalidade) ");
		str.append(" LEFT JOIN estado as estadoNaturalidade ON (estadoNaturalidade.codigo = naturalidade.estado) ");
		str.append(" LEFT JOIN Curso ON (Matricula.curso = curso.codigo) ");
		str.append(" LEFT JOIN Turno ON (Matricula.turno = Turno.codigo) ");
		str.append(" LEFT JOIN gradeCurricular ON (Matricula.gradeCurricularAtual = gradeCurricular.codigo) ");
		str.append(" LEFT JOIN observacaocomplementarhistoricoaluno ON Matricula.matricula = observacaocomplementarhistoricoaluno.matricula ");
		str.append(" and observacaocomplementarhistoricoaluno.gradecurricular = matricula.gradeCurricularAtual ");
		return str;
	}

	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer str = new StringBuffer();
		str.append("SELECT Matricula.*, ");
		str.append("Pessoa.nome as \"Pessoa.nome\", Pessoa.nomebatismo as \"Pessoa.nomebatismo\", Pessoa.codigo as \"Pessoa.codigo\", Pessoa.cpf as \"Pessoa.cpf\", Pessoa.rg as \"Pessoa.rg\", Pessoa.dataNasc as \"Pessoa.dataNasc\", ");
		str.append("Pessoa.orgaoEmissor as \"Pessoa.orgaoEmissor\", Pessoa.estadoEmissaoRg as \"Pessoa.estadoEmissaoRg\", Pessoa.dataEmissaoRg as \"Pessoa.dataEmissaoRg\", pessoa.estadoCivil AS \"pessoa.estadoCivil\", pessoa.registroAcademico as \"pessoa.registroAcademico\" ,");
		str.append(" Pessoa.endereco as \"Pessoa.endereco\", Pessoa.numero as \"Pessoa.numero\", Pessoa.complemento as \"Pessoa.complemento\", Pessoa.setor as \"Pessoa.setor\", estado.sigla as \"Estado.sigla\", ");
		str.append(" cidade.nome as \"Cidade.nome\", Pessoa.cep as \"Pessoa.cep\", Pessoa.telefoneres as \"Pessoa.telefoneres\", Pessoa.celular as \"Pessoa.celular\", Pessoa.email as \"Pessoa.email\", Pessoa.tipoAssinaturaDocumentoEnum as \"Pessoa.tipoAssinaturaDocumentoEnum\", ");
		str.append("paiz.codigo AS \"nacionalidade.codigo\", paiz.nacionalidade AS \"nacionalidade.nome\", naturalidade.codigo AS \"naturalidade.codigo\", naturalidade.nome AS \"naturalidade.nome\", naturalidade.codigoIBGE AS \"naturalidade.codigoIBGE\", Estado.codigoibge as \"Estado.codigoibge\", ");
		// CURSO...
		str.append("Curso.nomeDocumentacao as \"Curso.nomeDocumentacao\", Curso.nome as \"Curso.nome\", Curso.codigo as \"Curso.codigo\", Curso.nivelEducacional as \"Curso.nivelEducacional\", curso.periodicidade as \"Curso.periodicidade\", curso.perfilEgresso as \"Curso.perfilEgresso\", Curso.publicoAlvo as \"Curso.publicoAlvo\", ");
		str.append("Curso.titulo as \"Curso.titulo\", curso.titulacaoDoFormando as \"Curso.titulacaoDoFormando\", curso.titulacaoDoFormandoFeminino as \"Curso.titulacaoDoFormandoFeminino\", curso.titulacaoMasculinoApresentarDiploma as \"Curso.titulacaoMasculinoApresentarDiploma\", curso.titulacaoFemininoApresentarDiploma as \"Curso.titulacaoFemininoApresentarDiploma\", curso.dataPublicacaoDO as \"Curso.dataPublicacaoDO\", curso.nrRegistroInterno as \"curso.nrRegistroInterno\", ");
		str.append("Curso.configuracaoAcademico as \"Curso.configuracaoAcademico\", Curso.preposicaoNomeCurso as \"Curso.preposicaoNomeCurso\",  Curso.abreviatura as \"Curso.abreviatura\", Curso.nrPeriodoLetivo as \"Curso.nrPeriodoLetivo\", ");
		str.append(" Curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios as \"Curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios\" , Curso.permitirAssinarContratoPendenciaDocumentacao as \"Curso.permitirAssinarContratoPendenciaDocumentacao\" ,  Curso.ativarMatriculaAposAssinaturaContrato   as  \"Curso.ativarMatriculaAposAssinaturaContrato\" , Curso.configuracaoAcademico as \"Curso.configuracaoAcademico\", ");
		str.append("Curso.autorizacaoresolucaoemtramitacao AS \"Curso.autorizacaoresolucaoemtramitacao\", Curso.numeroprocessoautorizacaoresolucao AS \"Curso.numeroprocessoautorizacaoresolucao\", Curso.tipoprocessoautorizacaoresolucao AS \"Curso.tipoprocessoautorizacaoresolucao\", Curso.datacadastroautorizacaoresolucao AS \"Curso.datacadastroautorizacaoresolucao\", Curso.dataprotocoloautorizacaoresolucao AS \"Curso.dataprotocoloautorizacaoresolucao\",");
		str.append("Curso.dataCredenciamento as \"Curso.dataCredenciamento\", Curso.tipoautorizacaocursoenum AS \"Curso.tipoautorizacaocursoenum\", Curso.numeroautorizacao AS \"Curso.numeroautorizacao\", Curso.possuicodigoemec AS \"Curso.possuicodigoemec\", Curso.codigoemec AS \"Curso.codigoemec\", ");		

		// UNIDADE ENSINO
		str.append("UnidadeEnsino.codigo as \"UnidadeEnsino.codigo\", UnidadeEnsino.nome as \"UnidadeEnsino.nome\", UnidadeEnsino.dataPublicacaoDo as \"UnidadeEnsino.dataPublicacaoDo\", ");
		str.append("UnidadeEnsino.credenciamentoPortaria as \"UnidadeEnsino.credenciamentoPortaria\", UnidadeEnsino.cidade as \"UnidadeEnsino.cidade\", UnidadeEnsino.nomeExpedicaoDiploma as \"UnidadeEnsino.nomeExpedicaoDiploma\", ");
		str.append("UnidadeEnsino.CNPJ as \"UnidadeEnsino.CNPJ\", UnidadeEnsino.observacao as \"UnidadeEnsino.observacao\",  UnidadeEnsino.razaosocial as \"UnidadeEnsino.razaosocial\", ");
		str.append("cidadeUnidadeEnsino.nome as \"UnidadeEnsino.cidade.nome\", cidadeUnidadeEnsino.estado as \"UnidadeEnsino.cidade.estado\", ");
		str.append("agruparCamposEndereco(unidadeensino.endereco, unidadeensino.numero, unidadeensino.setor, unidadeensino.cep) as \"UnidadeEnsino.endereco_completo\", ");

		str.append("pess_orientadorPadraoEstagio.codigo as \"pess_orientadorPadraoEstagio.codigo\", pess_orientadorPadraoEstagio.nome as \"pess_orientadorPadraoEstagio.nome\", pess_orientadorPadraoEstagio.cpf as \"pess_orientadorPadraoEstagio.cpf\", ");
		str.append("pess_orientadorPadraoEstagio.telefoneRes as \"pess_orientadorPadraoEstagio.telefoneRes\", pess_orientadorPadraoEstagio.telefoneComer as \"pess_orientadorPadraoEstagio.telefoneComer\", pess_orientadorPadraoEstagio.celular as \"pess_orientadorPadraoEstagio.celular\", ");

		// Matricula Período
		str.append("MatriculaPeriodo.codigo as \"MatriculaPeriodo.codigo\", MatriculaPeriodo.data as \"MatriculaPeriodo.data\", MatriculaPeriodo.situacao as \"MatriculaPeriodo.situacao\", ");
		str.append("MatriculaPeriodo.matricula as \"MatriculaPeriodo.matricula\", ");
		str.append("MatriculaPeriodo.gradeCurricular as \"MatriculaPeriodo.gradeCurricular\", MatriculaPeriodo.periodoLetivoMatricula as \"MatriculaPeriodo.periodoLetivoMatricula\", ");
		str.append("MatriculaPeriodo.responsavelRenovacaoMatricula as \"MatriculaPeriodo.responsavelRenovacaoMatricula\", MatriculaPeriodo.responsavelLiberacaoMatricula as \"MatriculaPeriodo.responsavelLiberacaoMatricula\", ");
		str.append("MatriculaPeriodo.responsavelEmissaoBoletoMatricula as \"MatriculaPeriodo.responsavelEmissaoBoletoMatricula\", MatriculaPeriodo.dataEmissaoBoletoMatricula as \"MatriculaPeriodo.dataEmissaoBoletoMatricula\", ");
		str.append("MatriculaPeriodo.responsavelMatriculaForaPrazo as \"MatriculaPeriodo.responsavelMatriculaForaPrazo\", ");
		str.append("MatriculaPeriodo.turma as \"MatriculaPeriodo.turma\", MatriculaPeriodo.dataLiberacaoMatricula as \"MatriculaPeriodo.dataLiberacaoMatricula\", ");
		str.append("MatriculaPeriodo.nrDocumento as \"MatriculaPeriodo.nrDocumento\", MatriculaPeriodo.unidadeEnsinoCurso as \"MatriculaPeriodo.unidadeEnsinoCurso\", ");
		str.append("MatriculaPeriodo.ano as \"MatriculaPeriodo.ano\", MatriculaPeriodo.semestre as \"MatriculaPeriodo.semestre\", MatriculaPeriodo.turmaPeriodo as \"MatriculaPeriodo.turmaPeriodo\", ");
		str.append("MatriculaPeriodo.situacaoMatriculaPeriodo as \"MatriculaPeriodo.situacaoMatriculaPeriodo\", MatriculaPeriodo.processoMatricula as \"MatriculaPeriodo.processoMatricula\", ");
		str.append("MatriculaPeriodo.transferenciaEntrada as \"MatriculaPeriodo.transferenciaEntrada\", MatriculaPeriodo.contratoMatricula as \"MatriculaPeriodo.contratoMatricula\", ");
		str.append("MatriculaPeriodo.matriculaPeriodoPreMatricula as \"MatriculaPeriodo.matriculaPeriodoPreMatricula\", MatriculaPeriodo.nrDisciplinasIncluidas as \"MatriculaPeriodo.nrDisciplinasIncluidas\", ");
		str.append("MatriculaPeriodo.nrDisciplinasExcluidas as \"MatriculaPeriodo.nrDisciplinasExcluidas\", MatriculaPeriodo.financeiroManual as \"MatriculaPeriodo.financeiroManual\", ");
		str.append("MatriculaPeriodo.contratoFiador as \"MatriculaPeriodo.contratoFiador\", matriculaPeriodo.carneEntregue AS \"matriculaPeriodo.carneEntregue\", ");
		str.append("MatriculaPeriodo.planoFinanceiroCurso as \"MatriculaPeriodo.planoFinanceiroCurso\", MatriculaPeriodo.condicaoPagamentoPlanoFinanceiroCurso as \"MatriculaPeriodo.condicaoPagamentoPlanoFinanceiroCurso\", ");

		// Grade Curricular
		str.append("GradeCurricular.nome as \"GradeCurricular.nome\", ");
		// Periodo Letivo
		str.append("PeriodoLetivo.descricao as \"PeriodoLetivo.descricao\", PeriodoLetivo.periodoLetivo as \"PeriodoLetivo.periodoLetivo\", ");
		// Usuario ResponsavelRenovacaoMatricula
		str.append("ResponsavelRenovacaoMatricula.nome as \"ResponsavelRenovacaoMatricula.nome\", ");
		// Usuario ResponsavelLiberacaoMatricula
		str.append("ResponsavelLiberacaoMatricula.nome as \"ResponsavelLiberacaoMatricula.nome\", ");
		// Usuario ResponsavelEmissaoBoletoMatricula
		str.append("ResponsavelEmissaoBoletoMatricula.nome as \"ResponsavelEmissaoBoletoMatricula.nome\", ");
		// Usuario ResponsavelMatriculaForaPrazo
		str.append("ResponsavelMatriculaForaPrazo.nome as \"ResponsavelMatriculaForaPrazo.nome\", ");
		// Turma
		str.append("Turma.identificadorTurma as \"Turma.identificadorTurma\", ");
		str.append("Turma.sala as \"Turma.sala\", ");
		str.append("Turma.digitoTurma as \"Turma.digitoTurma\", ");
		// UnidadeEnsinoCurso
		str.append("UnidadeEnsinoCurso.turno as \"UnidadeEnsinoCurso.turno\", ");
		// Turno
		str.append("Turno.codigo as \"Turno.codigo\", Turno.nome as \"Turno.nome\", ");
		// PlanoFinanceiroCurso
		str.append("PlanoFinanceiroCurso.descricao as \"PlanoFinanceiroCurso.descricao\", ");
		// ProcessoMatricula
		str.append("ProcessoMatricula.descricao as \"ProcessoMatricula.descricao\", ");
		// CondicaoPagamentoPlanoFinanceiroCurso
		str.append("CondicaoPagamentoPlanoFinanceiroCurso.descricao as \"CondicaoPagamentoPlanoFinanceiroCurso.descricao\", ");
		// Funcionário(Consultor)
		str.append("p1.codigo as \"Funcionario.codigo\", p1.nome as \"Funcionario.nome\", ");
		// ContaCorrente UnidadeEnsino
//		str.append("contacorrente.codigo as \"contacorrente.codigo\", contacorrente.numero as \"contacorrente.numero\", contacorrente.digito as \"contacorrente.digito\", contacorrente.agencia as \"contacorrente.agencia\" ");
		str.append("unidadeensino.contaCorrentePadraoMensalidade, unidadeensino.contaCorrentePadraoMatricula, unidadeensino.contaCorrentePadraoBiblioteca, ");
		str.append("unidadeensino.contaCorrentePadraoDevolucaoCheque, unidadeensino.contaCorrentePadraoMaterialDidatico, unidadeensino.contaCorrentePadraoNegociacao, unidadeensino.contaCorrentePadraoProcessoSeletivo, Matricula.permitirInclusaoExclusaoDisciplinasRenovacao as \"Matricula.permitirInclusaoExclusaoDisciplinasRenovacao\", ");
		str.append("Pessoa.sexo as \"Pessoa.sexo\" ");
		str.append(" FROM Matricula ");
		str.append("      LEFT JOIN Pessoa ON (Matricula.aluno = pessoa.codigo) ");
		str.append("      LEFT JOIN cidade naturalidade ON pessoa.naturalidade = naturalidade.codigo ");
		str.append("      LEFT JOIN paiz ON pessoa.nacionalidade = paiz.codigo ");
		str.append("      LEFT JOIN UnidadeEnsino ON (Matricula.unidadeEnsino = unidadeEnsino.codigo) ");
		str.append("      LEFT JOIN funcionario func_orientadorPadraoEstagio ON (unidadeEnsino.orientadorPadraoEstagio = func_orientadorPadraoEstagio.codigo) ");
		str.append("      LEFT JOIN pessoa pess_orientadorPadraoEstagio ON (func_orientadorPadraoEstagio.pessoa = pess_orientadorPadraoEstagio.codigo) ");
		str.append("      LEFT JOIN cidade cidadeUnidadeEnsino ON (cidadeUnidadeEnsino.codigo = unidadeEnsino.cidade) ");
		str.append("      LEFT JOIN Curso ON (Matricula.curso = curso.codigo) ");
		str.append("      LEFT JOIN MatriculaPeriodo ON (MatriculaPeriodo.matricula = Matricula.matricula) ");
		str.append("      LEFT JOIN GradeCurricular ON (MatriculaPeriodo.gradeCurricular = GradeCurricular.codigo) ");
		str.append("      LEFT JOIN PeriodoLetivo ON (MatriculaPeriodo.periodoLetivoMatricula = PeriodoLetivo.codigo) ");
		str.append("      LEFT JOIN Usuario as ResponsavelRenovacaoMatricula ON (MatriculaPeriodo.responsavelRenovacaoMatricula = ResponsavelRenovacaoMatricula.codigo) ");
		str.append("      LEFT JOIN Usuario as ResponsavelLiberacaoMatricula ON (MatriculaPeriodo.responsavelLiberacaoMatricula = ResponsavelLiberacaoMatricula.codigo) ");
		str.append("      LEFT JOIN Usuario as ResponsavelEmissaoBoletoMatricula ON (MatriculaPeriodo.responsavelEmissaoBoletoMatricula = ResponsavelEmissaoBoletoMatricula.codigo) ");
		str.append("      LEFT JOIN Usuario as ResponsavelMatriculaForaPrazo ON (MatriculaPeriodo.responsavelMatriculaForaPrazo = ResponsavelMatriculaForaPrazo.codigo) ");
		str.append("      LEFT JOIN Turma ON (MatriculaPeriodo.turma = Turma.codigo) ");
		str.append("      LEFT JOIN UnidadeEnsinoCurso ON (MatriculaPeriodo.unidadeEnsinoCurso = UnidadeEnsinoCurso.codigo) ");
		str.append("      LEFT JOIN Turno ON (matricula.turno = Turno.codigo) ");
		str.append("      LEFT JOIN PlanoFinanceiroCurso ON (MatriculaPeriodo.planoFinanceiroCurso = PlanoFinanceiroCurso.codigo) ");
		str.append("      LEFT JOIN ProcessoMatricula ON (MatriculaPeriodo.processoMatricula = ProcessoMatricula.codigo) ");
		str.append("      LEFT JOIN Funcionario f1 ON (f1.codigo = matricula.consultor) ");
		str.append("      LEFT JOIN Pessoa p1 ON (p1.codigo = f1.pessoa) ");
		str.append("      LEFT JOIN CondicaoPagamentoPlanoFinanceiroCurso ON (MatriculaPeriodo.condicaoPagamentoPlanoFinanceiroCurso = CondicaoPagamentoPlanoFinanceiroCurso.codigo) ");
		str.append("      LEFT JOIN ContaCorrente ON (ContaCorrente.codigo = unidadeEnsino.contaCorrentePadrao) ");
		str.append(" LEFT JOIN Cidade on cidade.codigo = pessoa.cidade ");
		str.append(" LEFT JOIN Estado on estado.codigo = cidade.estado ");
		return str;
	}

	public List<MatriculaVO> consultaRapidaPorTurma(Integer turma, String parcela, String ano, String semestre, Optional<Date> dataInicio, Optional<Date> dataFim, UsuarioVO usuarioVO, String tipoAluno,String situacaoContratoDigital) throws Exception {
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaDistinct();
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula");
		//sqlStr.append(" INNER JOIN matriculaPeriodoVencimento mpv ON mpv.matriculaPeriodo = matriculaPeriodo.codigo ");
		sqlStr.append(" WHERE Matricula.matricula = MatriculaPeriodo.matricula AND Pessoa.codigo = Matricula.aluno");
		sqlStr.append(" AND MatriculaPeriodo.turma = ").append(turma).append(" ");
		if (!parcela.equals("") && !parcela.equals("TO")) {
			sqlStr.append(" AND mpv.parcela = '").append(parcela).append("' ");
		}
		if (!ano.equals("")) {
			sqlStr.append(" AND matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" AND matriculaPeriodo.semestre = '").append(semestre).append("' ");
		}
		dataInicio.ifPresent(dt -> sqlStr.append(" AND matriculaperiodo.data >= '").append(dt).append("' "));
		dataFim.ifPresent(dt -> sqlStr.append(" AND matriculaperiodo.data <= '").append(dt).append("' "));
		if (tipoAluno.equals("VETERANO")) {
			// Alunos veteranos
			sqlStr.append("AND").append(" (0 < (select count(matper2.codigo) from matriculaperiodo matper2 ");
			sqlStr.append(" where matper2.matricula = MatriculaPeriodo.matricula  and matper2.situacaomatriculaperiodo != 'PC' ");
			sqlStr.append(" and case when curso.periodicidade = 'IN' then matper2.data < matriculaperiodo.data else (matper2.ano||'/'||matper2.semestre) < (matriculaperiodo.ano||'/'||matriculaperiodo.semestre) end )  ) ");
		}
		if (tipoAluno.equals("CALOURO")) {
			// Alunos calouros //
			sqlStr.append("AND ").append(" ( formaingresso not in ('TI' , 'TE') ");
			sqlStr.append(" and matricula.matricula not in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN', 'EX') and matricula is not null and transferenciaentrada.situacao = 'EF' )  ");
			sqlStr.append(" and  0 = (select count(matper2.codigo) from matriculaperiodo matper2 where matper2.matricula = matriculaperiodo.matricula  ");
			sqlStr.append(" and matper2.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then matper2.data < matriculaperiodo.data else (matper2.ano||'/'||matper2.semestre) < (matriculaperiodo.ano||'/'||matriculaperiodo.semestre) end ");
			sqlStr.append(" ) ) ");
		}
		
		if(Uteis.isAtributoPreenchido(situacaoContratoDigital)  && situacaoContratoDigital.equals("CONTRATONAOGERADO")) {
			sqlStr.append(" and not exists (select doc.codigo from documentoassinado doc where doc.tipoorigemdocumentoassinado = 'CONTRATO' ");
			sqlStr.append(" and doc.matricula = matricula.matricula and doc.matriculaperiodo = matriculaperiodo.codigo  limit 1 )");
		}
        if(Uteis.isAtributoPreenchido(situacaoContratoDigital)  && situacaoContratoDigital.equals("CONTRATOPENDENTE")) {
        	sqlStr.append(" and  exists (select doc.codigo from documentoassinado doc  ");
        	sqlStr.append(" inner join documentoassinadopessoa docap on docap.documentoassinado = doc.codigo ");
        	sqlStr.append("  where doc.tipoorigemdocumentoassinado = 'CONTRATO' ");
			sqlStr.append(" and doc.matricula = matricula.matricula and doc.matriculaperiodo = matriculaperiodo.codigo ");
			sqlStr.append(" and docap.tipopessoa =  'ALUNO'  ");
			sqlStr.append(" and docap.situacaodocumentoassinadopessoa ='PENDENTE' limit 1 ) ");
		}
        if(Uteis.isAtributoPreenchido(situacaoContratoDigital)  && situacaoContratoDigital.equals("CONTRATOASSINADO")) {
        	sqlStr.append(" and  exists (select doc.codigo from documentoassinado doc  ");
        	sqlStr.append(" inner join documentoassinadopessoa docap on docap.documentoassinado = doc.codigo ");
        	sqlStr.append("  where doc.tipoorigemdocumentoassinado = 'CONTRATO' ");
			sqlStr.append(" and doc.matricula = matricula.matricula and doc.matriculaperiodo = matriculaperiodo.codigo ");
			sqlStr.append(" and docap.tipopessoa =  'ALUNO'  ");
			sqlStr.append(" and docap.situacaodocumentoassinadopessoa ='ASSINADO' limit 1 ) ");
		}
		sqlStr.append(" ORDER BY Pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		matriculaVOs = montarDadosConsultaBasica(tabelaResultado);
		return matriculaVOs;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(String matricula, Integer codUnidadeEnsino, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Matricula.matricula= ? )");
		if (codUnidadeEnsino != null && codUnidadeEnsino != 0) {
			sqlStr.append(" and Matricula.unidadeEnsino = ").append(codUnidadeEnsino).append("");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados(Matrícula).");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicosPorCoordenador(String matricula, Integer codigoCoordenador, Integer codUnidadeEnsino, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN cursoCoordenador ON cursoCoordenador.curso = curso.codigo");
		sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cursocoordenador.funcionario ");
		sqlStr.append(" WHERE (Matricula.matricula= '").append(matricula).append("')");
		sqlStr.append(" AND funcionario.pessoa = ").append(codigoCoordenador);
		if (codUnidadeEnsino != null && codUnidadeEnsino != 0) {
			sqlStr.append(" and Matricula.unidadeEnsino = ").append(codUnidadeEnsino).append("");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados(Matrícula).");
		}
		return tabelaResultado;
	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(String matricula, Integer codUnidadeEnsino, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (Matricula.matricula= ? )");
		if (codUnidadeEnsino != null && codUnidadeEnsino != 0) {
			sqlStr.append(" and Matricula.unidadeEnsino = ").append(codUnidadeEnsino).append(" AND MatriculaPeriodo.situacaoMatriculaPeriodo <> 'PC' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados(Matrícula).");
		}
		return tabelaResultado;
	}

	public MatriculaVO consultarPorChavePrimariaSituacaoDadosCompletos(String matricula, Integer codUnidadeEnsino, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (Matricula.matricula= ?)");
		if (codUnidadeEnsino != null && codUnidadeEnsino != 0) {
			sqlStr.append(" and Matricula.unidadeEnsino = ").append(codUnidadeEnsino).append("");
		}
		if (situacao != null && !situacao.equals("")) {
			if(situacao.contains(",")) {
				sqlStr.append(" AND (Matricula.situacao in (").append(situacao).append(")) ");
			}else {
				sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
			}
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		MatriculaVO matriculaVO = new MatriculaVO();
		if (!tabelaResultado.next()) {
			return matriculaVO;
		}
		montarDadosCompleto(matriculaVO, tabelaResultado, usuario);
		return matriculaVO;
	}

	public MatriculaVO consultarPorChavePrimariaSituacaoDadosCompletosDocumentacaoPendente(String matricula, Integer codUnidadeEnsino, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" inner join documetacaomatricula on matricula.matricula = documetacaomatricula.matricula ");
		sqlStr.append(" inner join arquivo on arquivo.codigo = documetacaomatricula.arquivo ");
		sqlStr.append(" WHERE (Matricula.matricula= '").append(matricula).append("')");
		if (codUnidadeEnsino != null && codUnidadeEnsino != 0) {
			sqlStr.append(" and Matricula.unidadeEnsino = ").append(codUnidadeEnsino).append("");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		sqlStr.append(" and arquivoAprovadoPeloDep = false ");
		sqlStr.append(" and arquivo.codigo is not null ");
		sqlStr.append(" and arquivo.codigo <> 0 ");
		sqlStr.append(" and (respnegardocdep is null or respnegardocdep = 0)");
		sqlStr.append(" order by turma.identificadorturma, pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		MatriculaVO matriculaVO = new MatriculaVO();
		if (!tabelaResultado.next()) {
			return matriculaVO;
		}
		montarDadosCompleto(matriculaVO, tabelaResultado, usuario);
		return matriculaVO;
	}

	public MatriculaVO consultaRapidaPorMatriculaUnica(String matricula, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorMatriculaUnica(matricula, unidadeEnsino, controlarAcesso, "", usuario);
	}

	public MatriculaVO consultaRapidaPorMatriculaUnica(String matricula, Integer unidadeEnsino, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE Matricula.matricula = ? ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		MatriculaVO matriculaVO = new MatriculaVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(matriculaVO, tabelaResultado);
		}
		return matriculaVO;
	}

	public MatriculaVO consultaRapidaPorMatriculaUnicaRemovendoCaracterEspecialMatricula(String matricula, String caracterEspecial, Integer unidadeEnsino, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE REPLACE(Matricula.matricula, '").append(caracterEspecial).append("', '') = '").append(matricula).append("' ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		MatriculaVO matriculaVO = new MatriculaVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(matriculaVO, tabelaResultado);
		}
		return matriculaVO;
	}


	public MatriculaVO consultaRapidaPorMatriculaUnicaUnidadeEnsinoBiblioteca(String matricula, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE Matricula.matricula = '").append(matricula).append("' ");
		if (unidadeEnsinoBibliotecaVOs != null && !unidadeEnsinoBibliotecaVOs.isEmpty()) {
			boolean virgula = false;
			sqlStr.append(" AND (unidadeEnsino.codigo in(");
			for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBiblioteca : unidadeEnsinoBibliotecaVOs) {
				if (!virgula) {
					sqlStr.append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
				} else {
					sqlStr.append(", ").append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
				}
				virgula = true;
			}
			sqlStr.append(") or unidadeEnsino.codigo is null) ");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		MatriculaVO matriculaVO = new MatriculaVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(matriculaVO, tabelaResultado);
		}
		return matriculaVO;
	}

	@Override
	public MatriculaVO consultaRapidaPorCPFUnidadeEnsinoBiblioteca(String matricula, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE pessoa.cpf = '").append(matricula).append("' ");
		if (unidadeEnsinoBibliotecaVOs != null && !unidadeEnsinoBibliotecaVOs.isEmpty()) {
			boolean virgula = false;
			sqlStr.append(" AND (unidadeEnsino.codigo in(");
			for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBiblioteca : unidadeEnsinoBibliotecaVOs) {
				if (!virgula) {
					sqlStr.append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
				} else {
					sqlStr.append(", ").append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
				}
				virgula = true;
			}
			sqlStr.append(") or unidadeEnsino.codigo is null) ");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		MatriculaVO matriculaVO = new MatriculaVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(matriculaVO, tabelaResultado);
		}
		return matriculaVO;
	}

	public MatriculaVO consultaRapidaPorMatriculaUnicaParaExpedicaoDiploma(String matricula, Integer unidadeEnsino, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaParaExpedicaoDiploma();
		sqlStr.append(" WHERE Matricula.matricula = '").append(matricula).append("' ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		MatriculaVO matriculaVO = new MatriculaVO();
		if (tabelaResultado.next()) {
			montarDadosBasicoParaExpedicaoDiploma(matriculaVO, tabelaResultado);
		}
		return matriculaVO;
	}

	public List<MatriculaVO> consultaRapidaPorMatricula(String matricula, Integer unidadeEnsino, boolean validarUnidadeEnsinoFinanceira, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorMatricula(matricula, unidadeEnsino, 0, validarUnidadeEnsinoFinanceira, controlarAcesso, usuario);
	}


	public List<MatriculaVO> consultaRapidaPorMatricula(String matricula, Integer unidadeEnsino,  Integer parceiro, boolean validarUnidadeEnsinoFinanceira, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		if(!validarUnidadeEnsinoFinanceira){
			return 	consultaRapidaPorMatricula(matricula, unidadeEnsino, controlarAcesso, usuario);
		}else{
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuffer sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE Matricula.matricula like(?) ");
			if(Uteis.isAtributoPreenchido(usuario.getUnidadeEnsinoLogado().getCodigo())) {
				sqlStr.append(" and matricula.unidadeEnsino = ").append(usuario.getUnidadeEnsinoLogado().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(unidadeEnsino.intValue()) && !Uteis.isAtributoPreenchido(parceiro)) {
				sqlStr.append(" and exists (  ");
				sqlStr.append(" select distinct unidadeensino from contareceber  where matriculaaluno = matricula.matricula ");
				sqlStr.append(" and unidadeensinofinanceira =").append(unidadeEnsino).append(" and contareceber.unidadeensino = matricula.unidadeEnsino limit 1 ");
				sqlStr.append(" ) ");
			}
			if(Uteis.isAtributoPreenchido(parceiro)){
				sqlStr.append(" and exists (  ");
				sqlStr.append(" select distinct unidadeensino from contareceber  where matriculaaluno = matricula.matricula ");
				if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
					sqlStr.append(" and unidadeensinofinanceira =").append(unidadeEnsino);
			}
				sqlStr.append(" and contareceber.unidadeensino = matricula.unidadeEnsino ");
				sqlStr.append(" and contareceber.situacao = 'AR' and contareceber.parceiro =").append(parceiro);
				sqlStr.append(" limit 1 ) ");
			}
			sqlStr.append(" ORDER BY Matricula.matricula ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula+"%");
			return montarDadosConsultaBasica(tabelaResultado);
		}
	}

	public List<MatriculaVO> consultaRapidaPorMatricula(String matricula, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidadeEnsino));
		}
		return consultaRapidaPorMatriculaPorUnidadeEnsino(matricula, unidadeEnsinoVOs, controlarAcesso, usuario);
	}

	@Override
	public List<MatriculaVO> consultaRapidaPorMatriculaPorUnidadeEnsino(String matricula, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Matricula.matricula like(?)) ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr.append(" AND (UnidadeEnsino.codigo IN (").append(unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(")) ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorMatriculaPorCoordenador(String matricula, Integer codigoCoordenador, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN cursoCoordenador ON cursoCoordenador.curso = curso.codigo ");
		sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cursocoordenador.funcionario ");
		sqlStr.append(" WHERE (Matricula.matricula like(?)) ");
		sqlStr.append(" AND funcionario.pessoa = ").append(codigoCoordenador);
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorMatriculaSemProgramacaoFormatura(String matricula, List<ProgramacaoFormaturaUnidadeEnsinoVO> prograFormaturaUnidadeEnsinoVOs, List<TurnoVO> turnoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (matricula.matricula like(?)) ");
		sqlStr.append(" AND (matricula.matricula not in(select distinct matricula from programacaoformaturaaluno ) or ");
		sqlStr.append(" (select case when (programacaoformatura is null) then true else false end FROM programacaoFormaturaAluno p WHERE p.matricula = matricula.matricula)) ");
		if (Uteis.isAtributoPreenchido(turnoVOs)) {
			sqlStr.append(" AND turno.codigo in (");
			for (TurnoVO turno : turnoVOs) {
				if (!turnoVOs.get(turnoVOs.size() - 1).getCodigo().equals(turno.getCodigo())) {
					sqlStr.append(turno.getCodigo()+", ");
				} else {
					sqlStr.append(turno.getCodigo());
		}
		}
			sqlStr.append(") ");
		}
		if (Uteis.isAtributoPreenchido(prograFormaturaUnidadeEnsinoVOs)) {
			sqlStr.append(" AND UnidadeEnsino.codigo in (");
			for (ProgramacaoFormaturaUnidadeEnsinoVO programacaoFormaturaUnidadeEnsinoVO : prograFormaturaUnidadeEnsinoVOs) {
				if (!prograFormaturaUnidadeEnsinoVOs.get(prograFormaturaUnidadeEnsinoVOs.size() - 1).getUnidadeEnsinoVO().getCodigo().equals(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo())) {
					sqlStr.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo()+", ");
				} else {
					sqlStr.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo());
				}
			}
			sqlStr.append(") ");
		}
		sqlStr.append(" ORDER BY matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorMatriculaUnidadeEnsinoBiblioteca(String matricula, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Matricula.matricula like('").append(matricula).append("%')) ");
		if (unidadeEnsinoBibliotecaVOs != null && !unidadeEnsinoBibliotecaVOs.isEmpty()) {
			boolean virgula = false;
			sqlStr.append(" AND matricula.unidadeEnsino in(");
			for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBiblioteca : unidadeEnsinoBibliotecaVOs) {
				if (!virgula) {
					sqlStr.append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
				} else {
					sqlStr.append(", ").append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
				}
				virgula = true;
			}
			sqlStr.append(") ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorMatriculaUnicaParaHistoricoAluno(String matricula, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Matricula.matricula = '").append(matricula).append("') ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorPessoaESituacoes(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (sem_acentos(Pessoa.nome) ilike('").append(valorConsulta).append("%')) ");
		sqlStr.append(" and ((matricula.situacao = 'AT') or (matricula.situacao = 'TR') or (matricula.situacao = 'CF') or (matricula.situacao = 'FO')) ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorCursoESituacoes(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (lower(Curso.nome) like('").append(valorConsulta.toLowerCase()).append("%')) ");
		sqlStr.append(" and ((matricula.situacao = 'AT') or (matricula.situacao = 'TR') or (matricula.situacao = 'CF') or (matricula.situacao = 'FO')) ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public MatriculaVO consultaRapidaPorMatriculaSituacoes(String matricula, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Matricula.matricula like('").append(matricula).append("%')) ");
		sqlStr.append(" and ((matricula.situacao = 'AT') or (matricula.situacao = 'TR') or (matricula.situacao = 'CF') or (matricula.situacao = 'FO')) ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		MatriculaVO obj = new MatriculaVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(obj, tabelaResultado);
		}
		return obj;
	}

	public List<MatriculaVO> consultaRapidaPorMatriculaCursoTurma(String matricula, Integer unidadeEnsino, Integer curso, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Matricula.matricula like('").append(matricula).append("%')) ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino).append(") ");
		}
		if (curso != null && curso.intValue() != 0) {
			sqlStr.append(" AND (Curso.codigo = ").append(curso.intValue()).append(") ");
		}
		if ((turma != null) && (turma.intValue() != 0)) {
			sqlStr.append("AND matricula.matricula IN(SELECT DISTINCT mp.matricula FROM matriculaperiodo mp WHERE mp.turma = ").append(turma).append(") ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorDescontoMatriculaUnidadeEnsino(String tipoDesconto, Integer codigoPlanoDesconto, Integer codigoDescontoConvenio, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN planoFinanceiroAluno on planoFinanceiroAluno.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN itemPlanofinanceiroAluno on itemPlanofinanceiroAluno.planoFinanceiroAluno = planoFinanceiroAluno.codigo ");
		if (tipoDesconto.equals("PF")) {
			sqlStr.append(" INNER JOIN planoDesconto on itemPlanofinanceiroAluno.planoDesconto = planoDesconto.codigo AND planoDesconto.codigo = ");
			sqlStr.append(codigoPlanoDesconto);
		} else {
			sqlStr.append(" INNER JOIN convenio on itemPlanofinanceiroAluno.convenio = convenio.codigo and convenio.codigo = ");
			sqlStr.append(codigoDescontoConvenio);
		}
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorTurmaCursoAnoSemestre(Integer turma, CursoVO curso, String ano, String semestre, Integer unidadeEnsino, String situacaoMatricula) throws Exception {
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT distinct matricula.matricula, matricula.data, matricula.situacao, matricula.situacaoFinanceira, ");
		sqlStr.append("matricula.dataConclusaoCurso, matricula.dataInicioCurso,  ");
		sqlStr.append("Pessoa.nome as \"Pessoa.nome\", Pessoa.codigo as \"Pessoa.codigo\", Pessoa.cpf as \"Pessoa.cpf\", Pessoa.rg as \"Pessoa.rg\",  ");
		sqlStr.append("Pessoa.dataNasc as \"Pessoa.dataNasc\", Curso.nome as \"Curso.nome\", Curso.codigo as \"Curso.codigo\", Curso.nivelEducacional as \"Curso.nivelEducacional\",  ");
		sqlStr.append("Curso.periodicidade as \"Curso.periodicidade\", Curso.perfilEgresso as \"Curso.perfilEgresso\", UnidadeEnsino.codigo as \"UnidadeEnsino.codigo\", UnidadeEnsino.nome as \"UnidadeEnsino.nome\", Turno.codigo as \"Turno.codigo\",  ");
		sqlStr.append("Turno.nome as \"Turno.nome\" FROM Matricula ");
		sqlStr.append("LEFT JOIN Pessoa ON (Matricula.aluno = pessoa.codigo) ");
		sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sqlStr.append("INNER JOIN matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula and matriculaperiodoturmadisciplina.matriculaPeriodo = matriculaPeriodo.codigo ");
		sqlStr.append("INNER JOIN UnidadeEnsino ON (Matricula.unidadeEnsino = unidadeEnsino.codigo)  ");
		sqlStr.append("INNER JOIN Curso ON (Matricula.curso = curso.codigo) ");
		sqlStr.append("INNER JOIN Turno ON (Matricula.turno = turno.codigo) ");
		sqlStr.append("INNER JOIN Turma ON (Turma.codigo = matriculaperiodoturmadisciplina.turma) ");
		sqlStr.append("INNER JOIN historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" WHERE matriculaperiodoturmadisciplina.turma = ").append(turma);
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		if (!ano.equals("") && ano != null) {
			sqlStr.append(" AND matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("") && semestre != null) {
			sqlStr.append(" AND matriculaPeriodo.semestre = '").append(semestre).append("' ");
		}
		// sqlStr.append(" AND matricula.situacao <> 'CA' ");
		sqlStr.append(" AND matricula.situacao not in ('CA', 'AC', 'TR', 'TS', 'TI') ");
		if (!situacaoMatricula.equals("")) {
			if (situacaoMatricula.equals("ativo")) {
				if (!curso.getNivelEducacionalPosGraduacao()) {
					sqlStr.append("AND matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' ");
				}
			} else if (situacaoMatricula.endsWith("inativo")) {
				if (!curso.getNivelEducacionalPosGraduacao()) {
					// sqlStr.append("AND matriculaPeriodo.situacaoMatriculaPeriodo <> 'AT' ");
					sqlStr.append("AND matriculaPeriodo.situacaoMatriculaPeriodo not in ('AT', 'PC') ");
				}
			}
		} else {
			if (!curso.getNivelEducacionalPosGraduacao()) {
				sqlStr.append("AND matriculaPeriodo.situacaoMatriculaPeriodo not in ('AT', 'PC') ");
			}
		}
		sqlStr.append("ORDER BY pessoa.nome; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		matriculaVOs = montarDadosConsultaBasicaSemTabelaArquivo(tabelaResultado);
		return matriculaVOs;
	}


	/**
	 *  *IMPORTANTE*
	 * O Metodo getSQLPadraoConsultaBasica deve seguir
	 * as mesmas regras e campos do metodo getSQLPadraoConsultaBasicaAplicativo
	 **/
	private StringBuilder getSQLPadraoConsultaBasicaSemTabelaArquivoComSituacaoIntegralizado() {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT distinct matricula.matricula, matricula.data, matricula.situacao, matricula.gradecurricularatual, matricula.situacaoFinanceira, ");
		sqlStr.append(" matricula.dataConclusaoCurso, matricula.dataInicioCurso, matricula.dataAtualizacaoMatriculaFormada, ");
		sqlStr.append(" Pessoa.nome as \"Pessoa.nome\", Pessoa.codigo as \"Pessoa.codigo\", Pessoa.cpf as \"Pessoa.cpf\", Pessoa.rg as \"Pessoa.rg\",  ");
		sqlStr.append(" Pessoa.dataNasc as \"Pessoa.dataNasc\", Curso.nome as \"Curso.nome\", Curso.codigo as \"Curso.codigo\", Curso.nivelEducacional as \"Curso.nivelEducacional\",  ");
		sqlStr.append(" Curso.periodicidade as \"Curso.periodicidade\", Curso.perfilEgresso as \"Curso.perfilEgresso\", UnidadeEnsino.codigo as \"UnidadeEnsino.codigo\", UnidadeEnsino.nome as \"UnidadeEnsino.nome\", Turno.codigo as \"Turno.codigo\",  ");
		sqlStr.append(" Turno.nome as \"Turno.nome\"  ");
		return sqlStr;
	}

	public List<MatriculaVO> consultaRapidaPorTurma(Integer turma, String ano, String semestre, Integer unidadeEnsino, Boolean curriculaIntegralizacao, String situacaoRegistroFormada, UsuarioVO usuario) throws Exception {
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		StringBuilder sqlStr = getSQLPadraoConsultaBasicaSemTabelaArquivoComSituacaoIntegralizado();
		sqlStr.append(" , responsavelAtualizacaoMatriculaFormada.codigo as responsavelAtualizacaoMatriculaFormada, responsavelAtualizacaoMatriculaFormada.nome as nomeResponsavelAtualizacaoMatriculaFormada ");
		sqlStr.append(" FROM Matricula ");
		sqlStr.append(" LEFT JOIN Pessoa ON (Matricula.aluno = pessoa.codigo) ");
		sqlStr.append(" INNER JOIN matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN UnidadeEnsino ON (Matricula.unidadeEnsino = unidadeEnsino.codigo)  ");
		sqlStr.append(" INNER JOIN Curso ON (Matricula.curso = curso.codigo) ");
		sqlStr.append(" INNER JOIN Turno ON (Matricula.turno = turno.codigo) ");
		sqlStr.append(" INNER JOIN Turma ON (Turma.codigo = matriculaPeriodo.turma) ");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.turma = turma.codigo ");
		sqlStr.append(" INNER JOIN historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" LEFT JOIN usuario as responsavelAtualizacaoMatriculaFormada on responsavelAtualizacaoMatriculaFormada.codigo = matricula.responsavelAtualizacaoMatriculaFormada ");
		sqlStr.append(" WHERE matriculaperiodoturmadisciplina.turma = ").append(turma);
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		if (!ano.equals("")) {
			sqlStr.append(" AND matriculaperiodo.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" AND matriculaperiodo.semestre = '").append(semestre).append("' ");
		}
		if (!situacaoRegistroFormada.equals("TO")) {
			if (situacaoRegistroFormada.equals("RE")) {
				sqlStr.append(" AND matricula.dataAtualizacaoMatriculaFormada is not null ");
				sqlStr.append(" AND matricula.responsavelAtualizacaoMatriculaFormada is not null ");
			} else {
				sqlStr.append(" AND matricula.dataAtualizacaoMatriculaFormada is null ");
				sqlStr.append(" AND matricula.responsavelAtualizacaoMatriculaFormada is null ");
			}
		}
		sqlStr.append(" AND matricula.situacao <> 'CA' and matricula.situacao <> 'TR' ");
		//sqlStr.append(" and matricula.situacao <> 'FO' ");
		sqlStr.append(" ORDER BY pessoa.nome; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		matriculaVOs = montarDadosConsultaBasicaSemTabelaArquivoComSituacaoIntegralizado(tabelaResultado, curriculaIntegralizacao, usuario);
		return matriculaVOs;
	}

	@Override
	public List<MatriculaVO> consultaRapidaPorAlteracaoDataBaseTurma(Integer turma, Integer unidadeEnsino, Boolean curriculaIntegralizacao, Boolean utilizarControleGeracaoTurma, Boolean validarDataBaseGeracaoParcelaMatricula, UsuarioVO usuario) throws Exception {
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		StringBuilder sqlStr = getSQLPadraoConsultaBasicaSemTabelaArquivoComSituacaoIntegralizado();
		sqlStr.append(" , responsavelAtualizacaoMatriculaFormada.codigo as responsavelAtualizacaoMatriculaFormada, responsavelAtualizacaoMatriculaFormada.nome as nomeResponsavelAtualizacaoMatriculaFormada ");
		sqlStr.append(" FROM Matricula ");
		sqlStr.append(" INNER JOIN matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN UnidadeEnsino ON (Matricula.unidadeEnsino = unidadeEnsino.codigo)  ");
		sqlStr.append(" INNER JOIN Curso ON (Matricula.curso = curso.codigo) ");
		sqlStr.append(" INNER JOIN Turno ON (Matricula.turno = turno.codigo) ");
		sqlStr.append(" INNER JOIN processomatricula on processomatricula.codigo = matriculaPeriodo.processomatricula ");
		sqlStr.append(" INNER JOIN processomatriculacalendario on processomatriculacalendario.processomatricula = processomatricula.codigo ");
		sqlStr.append(" LEFT JOIN controlegeracaoparcelaturma on controlegeracaoparcelaturma.codigo = processomatriculacalendario.controlegeracaoparcelaturma ");
		sqlStr.append(" LEFT JOIN Pessoa ON (Matricula.aluno = pessoa.codigo) ");
		sqlStr.append(" LEFT JOIN usuario as responsavelAtualizacaoMatriculaFormada on responsavelAtualizacaoMatriculaFormada.codigo = matricula.responsavelAtualizacaoMatriculaFormada ");
		sqlStr.append(" WHERE matriculaPeriodo.turma  = ").append(turma);
		sqlStr.append(" AND matricula.situacao <> 'CA' and matricula.situacao <> 'TR' and matricula.situacao <> 'FO' ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		if(utilizarControleGeracaoTurma && !validarDataBaseGeracaoParcelaMatricula){
			sqlStr.append(" AND controlegeracaoparcelaturma.mesdatabasegeracaoparcelas ");
			sqlStr.append(" and not exists (");
			sqlStr.append(" select matriculaperiodovencimento.codigo from matriculaperiodovencimento ");
			sqlStr.append(" where matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo ");
			sqlStr.append(" AND matriculaperiodovencimento.situacao <> 'NG' ");
			sqlStr.append(" and matriculaperiodovencimento.tipoorigemmatriculaperiodovencimento IN ('MENSALIDADE', 'MATERIAL_DIDATICO')) ");

		}else if (!validarDataBaseGeracaoParcelaMatricula){
			sqlStr.append(" AND (controlegeracaoparcelaturma.mesdatabasegeracaoparcelas is null or controlegeracaoparcelaturma.mesdatabasegeracaoparcelas = false) ");
			sqlStr.append(" and not exists (");
			sqlStr.append(" select matriculaperiodovencimento.codigo from matriculaperiodovencimento ");
			sqlStr.append(" where matriculaperiodovencimento.matriculaperiodo = matriculaperiodo.codigo ");
			sqlStr.append(" AND matriculaperiodovencimento.situacao <> 'NG' ");
			sqlStr.append(" and matriculaperiodovencimento.tipoorigemmatriculaperiodovencimento IN ('MENSALIDADE', 'MATERIAL_DIDATICO')) ");
		}
		if (!validarDataBaseGeracaoParcelaMatricula) {
			sqlStr.append(" and matriculaPeriodo.databasegeracaoparcelas is null ");
		} else {
			sqlStr.append(" and matriculaPeriodo.databasegeracaoparcelas is not null ");
		}
		sqlStr.append(" ORDER BY pessoa.nome; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		matriculaVOs = montarDadosConsultaBasicaSemTabelaArquivoComSituacaoIntegralizado(tabelaResultado, curriculaIntegralizacao, usuario);
		return matriculaVOs;
	}

	public List<MatriculaVO> consultaRapidaPorCurso(Integer curso, String ano, String semestre, Integer unidadeEnsino, Boolean curriculaIntegralizacao, String situacaoRegistroFormada, UsuarioVO usuario, String tipoAluno,String situacaoContratoDigital) throws Exception {
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		StringBuilder sqlStr = getSQLPadraoConsultaBasicaSemTabelaArquivoComSituacaoIntegralizado();
		sqlStr.append(" , responsavelAtualizacaoMatriculaFormada.codigo as responsavelAtualizacaoMatriculaFormada, responsavelAtualizacaoMatriculaFormada.nome as nomeResponsavelAtualizacaoMatriculaFormada ");
		sqlStr.append(" FROM Matricula ");
		sqlStr.append(" LEFT JOIN Pessoa ON (Matricula.aluno = pessoa.codigo) ");
		sqlStr.append(" INNER JOIN matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		sqlStr.append(" INNER JOIN UnidadeEnsino ON (Matricula.unidadeEnsino = unidadeEnsino.codigo)  ");
		sqlStr.append(" INNER JOIN Curso ON (Matricula.curso = curso.codigo) ");
		sqlStr.append(" INNER JOIN Turno ON (Matricula.turno = turno.codigo) ");
		sqlStr.append(" INNER JOIN Turma ON (Turma.codigo = matriculaPeriodo.turma) ");
		sqlStr.append(" LEFT JOIN usuario as responsavelAtualizacaoMatriculaFormada on responsavelAtualizacaoMatriculaFormada.codigo = matricula.responsavelAtualizacaoMatriculaFormada ");
		sqlStr.append(" WHERE matricula.curso = ").append(curso);
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		if (!ano.equals("")) {
			sqlStr.append(" AND matriculaperiodo.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("")) {
			sqlStr.append(" AND matriculaperiodo.semestre = '").append(semestre).append("' ");
		}
		if (!situacaoRegistroFormada.equals("TO")) {
			if (situacaoRegistroFormada.equals("RE")) {
				sqlStr.append(" AND matricula.dataAtualizacaoMatriculaFormada is not null ");
				sqlStr.append(" AND matricula.responsavelAtualizacaoMatriculaFormada is not null ");
			} else {
				sqlStr.append(" AND matricula.dataAtualizacaoMatriculaFormada is null ");
				sqlStr.append(" AND matricula.responsavelAtualizacaoMatriculaFormada is null ");
			}
		}
		sqlStr.append(" AND matricula.situacao <> 'CA' and matricula.situacao <> 'TR'  ");
		if (tipoAluno.equals("VETERANO")) {
			// Alunos veteranos
			sqlStr.append("AND").append(" (0 < (select count(matper2.codigo) from matriculaperiodo matper2 ");
			sqlStr.append(" where matper2.matricula = MatriculaPeriodo.matricula  and matper2.situacaomatriculaperiodo != 'PC' ");
			sqlStr.append(" and case when curso.periodicidade = 'IN' then matper2.data < matriculaperiodo.data else (matper2.ano||'/'||matper2.semestre) < (matriculaperiodo.ano||'/'||matriculaperiodo.semestre) end )  ) ");
		}
		if (tipoAluno.equals("CALOURO")) {
			// Alunos calouros //
			sqlStr.append("AND ").append(" ( formaingresso not in ('TI' , 'TE') ");
			sqlStr.append(" and matricula.matricula not in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN', 'EX') and matricula is not null and transferenciaentrada.situacao = 'EF' )  ");
			sqlStr.append(" and  0 = (select count(matper2.codigo) from matriculaperiodo matper2 where matper2.matricula = matriculaperiodo.matricula  ");
			sqlStr.append(" and matper2.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then matper2.data < matriculaperiodo.data else (matper2.ano||'/'||matper2.semestre) < (matriculaperiodo.ano||'/'||matriculaperiodo.semestre) end ");
			sqlStr.append(" ) ) ");
		}
		if(Uteis.isAtributoPreenchido(situacaoContratoDigital)  && situacaoContratoDigital.equals("CONTRATONAOGERADO")) {
			sqlStr.append(" and not exists (select doc.codigo from documentoassinado doc where doc.tipoorigemdocumentoassinado = 'CONTRATO' ");
			sqlStr.append(" and doc.matricula = matricula.matricula and doc.matriculaperiodo = matriculaperiodo.codigo  limit 1 )");
		}
        if(Uteis.isAtributoPreenchido(situacaoContratoDigital)  && situacaoContratoDigital.equals("CONTRATOPENDENTE")) {
        	sqlStr.append(" and  exists (select doc.codigo from documentoassinado doc  ");
        	sqlStr.append(" inner join documentoassinadopessoa docap on docap.documentoassinado = doc.codigo ");
        	sqlStr.append("  where doc.tipoorigemdocumentoassinado = 'CONTRATO' ");
			sqlStr.append(" and doc.matricula = matricula.matricula and doc.matriculaperiodo = matriculaperiodo.codigo ");
			sqlStr.append(" and docap.tipopessoa =  'ALUNO'  ");
			sqlStr.append(" and docap.situacaodocumentoassinadopessoa ='PENDENTE' limit 1 ) ");
		}
        if(Uteis.isAtributoPreenchido(situacaoContratoDigital)  && situacaoContratoDigital.equals("CONTRATOASSINADO")) {
        	sqlStr.append(" and  exists (select doc.codigo from documentoassinado doc  ");
        	sqlStr.append(" inner join documentoassinadopessoa docap on docap.documentoassinado = doc.codigo ");
        	sqlStr.append("  where doc.tipoorigemdocumentoassinado = 'CONTRATO' ");
			sqlStr.append(" and doc.matricula = matricula.matricula and doc.matriculaperiodo = matriculaperiodo.codigo ");
			sqlStr.append(" and docap.tipopessoa =  'ALUNO'  ");
			sqlStr.append(" and docap.situacaodocumentoassinadopessoa ='ASSINADO' limit 1 ) ");
		}
		sqlStr.append(" ORDER BY pessoa.nome; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		matriculaVOs = montarDadosConsultaBasicaSemTabelaArquivoComSituacaoIntegralizado(tabelaResultado, curriculaIntegralizacao, usuario);
		return matriculaVOs;
	}

	public List<MatriculaVO> consultaRapidaPorUnicaMatricula(String matricula, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Matricula.matricula like('").append(matricula).append("')) ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorCPF(String cpfPessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (lower (Pessoa.cpf) like('").append(cpfPessoa.toLowerCase()).append("%')) ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY Pessoa.cpf ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	/**
	 * Responsável por realizar uma consulta de <code>Matricula</code> através
	 * do valor do atributo <code>Date data</code>. Retorna os objetos com
	 * valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>MatriculaVO</code>
	 *         resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<MatriculaVO> consultaRapidaPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (data between '").append(Uteis.getDataJDBC(prmIni)).append("' AND '").append(Uteis.getDataJDBC(prmFim)).append("') ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY data ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorNomePessoa(String nomePessoa, Integer unidadeEnsino, boolean validarUnidadeEnsinoFinanceira, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorNomePessoa(nomePessoa, unidadeEnsino, 0, validarUnidadeEnsinoFinanceira, controlarAcesso,  usuario);

	}

	public List<MatriculaVO> consultaRapidaPorNomePessoa(String nomePessoa, Integer unidadeEnsino, Integer parceiro, boolean validarUnidadeEnsinoFinanceira, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		if(!validarUnidadeEnsinoFinanceira){
			return consultaRapidaPorNomePessoa(nomePessoa, unidadeEnsino, controlarAcesso, "","", usuario);
		}else {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuffer sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE sem_acentos(Pessoa.nome) ilike(sem_acentos(?)) ");
			if(Uteis.isAtributoPreenchido(usuario.getUnidadeEnsinoLogado().getCodigo())) {
				sqlStr.append(" and matricula.unidadeEnsino = ").append(usuario.getUnidadeEnsinoLogado().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(unidadeEnsino) && !Uteis.isAtributoPreenchido(parceiro)) {
				sqlStr.append(" and exists (  ");
				sqlStr.append(" select contareceber.codigo from contareceber  ");
				sqlStr.append(" WHERE contareceber.matriculaaluno = matricula.matricula ");
				sqlStr.append(" and contareceber.unidadeensinofinanceira =").append(unidadeEnsino).append(" ");
				sqlStr.append(" and contareceber.unidadeensino = matricula.unidadeensino ");
				sqlStr.append(" limit 1) ");
			}else if(Uteis.isAtributoPreenchido(parceiro)){
				sqlStr.append(" and exists (  ");
				sqlStr.append(" select contareceber.codigo from contareceber  ");
				sqlStr.append(" WHERE contareceber.matriculaaluno = matricula.matricula ");
				if (Uteis.isAtributoPreenchido(unidadeEnsino) ) {
					sqlStr.append(" and contareceber.unidadeensinofinanceira =").append(unidadeEnsino).append(" ");
			}
				sqlStr.append(" and contareceber.unidadeensino = matricula.unidadeensino ");
				sqlStr.append(" and contareceber.situacao = 'AR' and contareceber.parceiro =").append(parceiro);
				sqlStr.append(" limit 1) ");
			}
			sqlStr.append(" ORDER BY Pessoa.nome ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomePessoa+"%");
			return montarDadosConsultaBasica(tabelaResultado);
		}

	}

	public List<MatriculaVO> consultaRapidaPorNomePessoa(String nomePessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidadeEnsino));
		}
		return consultaRapidaPorNomePessoaPorUnidadeEnsino(nomePessoa, unidadeEnsinoVOs, controlarAcesso, "","", usuario);
	}
	
	@Override
	public List<MatriculaVO> consultaRapidaPorNomePessoaPorUnidadeEnsino(String nomePessoa, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorNomePessoaPorUnidadeEnsino(nomePessoa, unidadeEnsinoVOs, controlarAcesso, "","", usuario);
	}

	public List<MatriculaVO> consultaRapidaPorRegistroAcademico(String registroAcademico, Integer unidadeEnsino, boolean controlarAcesso , UsuarioVO usuario) throws Exception {
		return consultaRapidaPorRegistroAcademico(registroAcademico, unidadeEnsino, controlarAcesso, "","", usuario);
	}

	public List<MatriculaVO> consultaRapidaPorNomePessoa(String nomePessoa, Integer unidadeEnsino, boolean controlarAcesso, String situacao, String nivelEducacional, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidadeEnsino));
		}
		return consultaRapidaPorNomePessoaPorUnidadeEnsino(nomePessoa, unidadeEnsinoVOs, controlarAcesso, situacao, nivelEducacional, usuario);
	}
	
	public List<MatriculaVO> consultaRapidaPorNomePessoaPorUnidadeEnsino(String nomePessoa, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, String situacao, String nivelEducacional, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(Pessoa.nome) ilike sem_acentos(?) ");

		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" AND (Curso.niveleducacional = '").append(nivelEducacional).append("')");
		}

		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr.append(" AND (UnidadeEnsino.codigo IN (").append(unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(")) ");
		}
		if (situacao != null && !situacao.equals("")) {
			if(situacao.contains(",")) {
				sqlStr.append(" AND (Matricula.situacao in (").append(situacao).append(")) ");
			}else {
				sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
			}
		}
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomePessoa+"%");
		return montarDadosConsultaBasica(tabelaResultado);
	}
	
	
	public List<MatriculaVO> consultaRapidaPorRegistroAcademico(String registroAcademico, Integer unidadeEnsino, boolean controlarAcesso,  String situacao, String nivelEducacional,  UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE  UPPER (Pessoa.registroAcademico) ilike (?) ");	
		
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" AND (Curso.niveleducacional = '").append(nivelEducacional).append("')");
		}
		
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), registroAcademico+"%");
		return montarDadosConsultaBasica(tabelaResultado);
	}
	
	public List<MatriculaVO> consultaRapidaPorNomePessoa(String nomePessoa, List<UnidadeEnsinoVO> unidades, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorNomePessoa(nomePessoa, unidades, controlarAcesso, "", usuario);
	}

	public List<MatriculaVO> consultaRapidaPorNomePessoa(String nomePessoa, List<UnidadeEnsinoVO> unidades, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(Pessoa.nome) ilike sem_acentos(?) ");
		if (Uteis.isAtributoPreenchido(unidades)) {
			sqlStr.append(" AND unidadeensino.codigo  IN (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidades) {
				if (x > 0) {
					sqlStr.append(", ");
				}
				sqlStr.append(unidadeEnsinoVO.getCodigo());
				x++;
			}
			sqlStr.append(" ) ");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomePessoa+"%");
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorNomePessoaListaUnidadeEnsino(String nomePessoa, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(Pessoa.nome) ilike(sem_acentos(?)) ");
		if (!listaUnidadeEnsinoVOs.isEmpty()) {
			sqlStr.append(" and matricula.unidadeensino in (");
			for (UnidadeEnsinoVO ue : listaUnidadeEnsinoVOs) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomePessoa+"%");
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorNomePessoaPorCoordenador(String nomePessoa, Integer codigoCoordenador, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN cursoCoordenador ON cursoCoordenador.curso = curso.codigo ");
		sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cursocoordenador.funcionario ");
		sqlStr.append(" WHERE sem_acentos(Pessoa.nome) ilike(sem_acentos(?)) ");
		sqlStr.append(" AND funcionario.pessoa = ").append(codigoCoordenador);
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomePessoa+"%");
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorNomePessoaSemProgramacaoFormatura(String nomePessoa, List<ProgramacaoFormaturaUnidadeEnsinoVO> prograFormaturaUnidadeEnsinoVOs, List<TurnoVO> turnoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(Pessoa.nome) ilike(sem_acentos(?)) ");
		if (Uteis.isAtributoPreenchido(turnoVOs)) {
			sqlStr.append(" AND turno.codigo in (");
			for (TurnoVO turno : turnoVOs) {
				if (!turnoVOs.get(turnoVOs.size() - 1).getCodigo().equals(turno.getCodigo())) {
					sqlStr.append(turno.getCodigo()+", ");
				} else {
					sqlStr.append(turno.getCodigo());
		}
		}
			sqlStr.append(") ");
		}
		if (Uteis.isAtributoPreenchido(prograFormaturaUnidadeEnsinoVOs)) {
			sqlStr.append(" AND UnidadeEnsino.codigo in (");
			for (ProgramacaoFormaturaUnidadeEnsinoVO programacaoFormaturaUnidadeEnsinoVO : prograFormaturaUnidadeEnsinoVOs) {
				if (!prograFormaturaUnidadeEnsinoVOs.get(prograFormaturaUnidadeEnsinoVOs.size() - 1).getUnidadeEnsinoVO().getCodigo().equals(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo())) {
					sqlStr.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo()+", ");
				} else {
					sqlStr.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo());
				}
			}
			sqlStr.append(") ");
		}
		sqlStr.append(" AND (matricula.matricula not in(select distinct matricula from programacaoformaturaaluno ) or ");
		sqlStr.append(" (select case when (programacaoformatura is null) then true else false end FROM programacaoFormaturaAluno p WHERE p.matricula = matricula.matricula)) ");
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomePessoa+"%");
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorNomeResponsavel(String nomeResponsavel, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN usuario ON usuario.codigo = matricula.usuario ");
		sqlStr.append(" WHERE (usuario.nome ilike('").append(nomeResponsavel).append("%')) ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY usuario.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorSituacao(String situacao, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Matricula.situacao = '").append(situacao).append("') ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY Matricula.situacao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorSituacaoFinanceira(String situacaoFinanceira, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Matricula.situacaoFinanceira = '").append(situacaoFinanceira).append("') ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY Matricula.situacaoFinanceira ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorNomePessoa(String nomePessoa, Integer unidadeEnsino, Integer curso, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(Pessoa.nome) ilike('").append(Uteis.removerAcentos(nomePessoa)).append("%') ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		if (curso != null && curso.intValue() != 0) {
			sqlStr.append(" AND (Curso.codigo = ").append(curso.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorNomePessoa(String nomePessoa, List<UnidadeEnsinoVO> unidades, List<CursoVO> cursos, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(Pessoa.nome) ilike('").append(Uteis.removerAcentos(nomePessoa)).append("%') ");
		if (Uteis.isAtributoPreenchido(cursos)) {
			sqlStr.append(" AND curso.codigo  IN (");
			int x = 0;
			for (CursoVO cursoVO : cursos) {
				if (x > 0) {
					sqlStr.append(", ");
				}
				sqlStr.append(cursoVO.getCodigo());
				x++;
			}
			sqlStr.append(" ) ");
		}
		if (Uteis.isAtributoPreenchido(unidades)) {
			sqlStr.append(" AND unidadeensino.codigo  IN (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidades) {
				if (x > 0) {
					sqlStr.append(", ");
				}
				sqlStr.append(unidadeEnsinoVO.getCodigo());
				x++;
			}
			sqlStr.append(" ) ");
		}
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorNomePessoaCursoTurma(String nomePessoa, Integer unidadeEnsino, Integer curso, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE ( sem_acentos(Pessoa.nome) ilike('").append(Uteis.removerAcentos(nomePessoa)).append("%')) ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		if (curso != null && curso.intValue() != 0) {
			sqlStr.append(" AND (Curso.codigo = ").append(curso.intValue()).append(") ");
		}
		if ((turma != null) && (turma.intValue() != 0)) {
			sqlStr.append("AND matricula.matricula IN(SELECT DISTINCT mp.matricula FROM matriculaperiodo mp WHERE mp.turma = ").append(turma).append(") ");
		}
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorNomePessoaCursoTurma(String nomePessoa, List<UnidadeEnsinoVO> unidades, List<CursoVO> cursos, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE ( sem_acentos(Pessoa.nome) ilike('").append(Uteis.removerAcentos(nomePessoa)).append("%')) ");

		if (Uteis.isAtributoPreenchido(cursos)) {
			sqlStr.append(" AND curso.codigo  IN (");
			int x = 0;
			for (CursoVO cursoVO : cursos) {
				if (x > 0) {
					sqlStr.append(", ");
				}
				sqlStr.append(cursoVO.getCodigo());
				x++;
			}
			sqlStr.append(" ) ");
		}
		if (Uteis.isAtributoPreenchido(unidades)) {
			sqlStr.append(" AND unidadeensino.codigo  IN (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidades) {
				if (x > 0) {
					sqlStr.append(", ");
				}
				sqlStr.append(unidadeEnsinoVO.getCodigo());
				x++;
			}
			sqlStr.append(" ) ");
		}
		if ((turma != null) && (turma.intValue() != 0)) {
			sqlStr.append("AND matricula.matricula IN(SELECT DISTINCT mp.matricula FROM matriculaperiodo mp WHERE mp.turma = ").append(turma).append(") ");
		}
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);

	}

	public List<MatriculaVO> consultaRapidaPorNomeCurso(String nomePessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidadeEnsino));
		}
		return consultaRapidaPorNomeCursoPorUnidadeEnsino(nomePessoa, unidadeEnsinoVOs, controlarAcesso, "", usuario);
	}
	
	@Override
	public List<MatriculaVO> consultaRapidaPorNomeCursoPorUnidadeEnsino(String nomePessoa, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorNomeCursoPorUnidadeEnsino(nomePessoa, unidadeEnsinoVOs, controlarAcesso, "", usuario);
	}

	public List<MatriculaVO> consultaRapidaPorNomeCurso(String nomePessoa, Integer unidadeEnsino, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidadeEnsino));
		}
		return consultaRapidaPorNomeCursoPorUnidadeEnsino(nomePessoa, unidadeEnsinoVOs, controlarAcesso, situacao, usuario);
	}
	
	public List<MatriculaVO> consultaRapidaPorNomeCursoPorUnidadeEnsino(String nomePessoa, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE upper(sem_acentos(Curso.nome)) ilike upper(sem_acentos(?))");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr.append(" AND (UnidadeEnsino.codigo IN (").append(unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(")) ");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		sqlStr.append(" ORDER BY Curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomePessoa + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorNomeCursoListaUnidadeEnsinoVOs(String nomePessoa, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, boolean controlarAcesso, String situacao, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE sem_acentos (Curso.nome) ilike('").append(Uteis.removerAcentos(nomePessoa)).append("%') ");
		if (!listaUnidadeEnsinoVOs.isEmpty()) {
			sqlStr.append(" and matricula.unidadeensino in (");
			for (UnidadeEnsinoVO ue : listaUnidadeEnsinoVOs) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		sqlStr.append(" ORDER BY Curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorNomeCursoPorCoordenador(String nomePessoa, Integer codigoCoordenador, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN cursoCoordenador ON cursoCoordenador.curso = curso.codigo ");
		sqlStr.append(" INNER JOIN funcionario ON funcionario.codigo = cursocoordenador.funcionario ");
		sqlStr.append(" WHERE sem_acentos (Curso.nome) ilike sem_acentos(?) ");
		sqlStr.append(" AND funcionario.pessoa = ").append(codigoCoordenador);

		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY Curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomePessoa + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorNomeCursoSemProgramacaoFormatura(String nomeCurso, List<ProgramacaoFormaturaUnidadeEnsinoVO> prograFormaturaUnidadeEnsinoVOs, List<TurnoVO> turnoVOs,  boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE sem_acentos (Curso.nome) ilike sem_acentos(?) ");
		if (Uteis.isAtributoPreenchido(turnoVOs)) {
			sqlStr.append(" AND turno.codigo in (");
			for (TurnoVO turno : turnoVOs) {
				if (!turnoVOs.get(turnoVOs.size() - 1).getCodigo().equals(turno.getCodigo())) {
					sqlStr.append(turno.getCodigo()+", ");
				} else {
					sqlStr.append(turno.getCodigo());
		}
		}
			sqlStr.append(") ");
		}
		if (Uteis.isAtributoPreenchido(prograFormaturaUnidadeEnsinoVOs)) {
			sqlStr.append(" AND UnidadeEnsino.codigo in (");
			for (ProgramacaoFormaturaUnidadeEnsinoVO programacaoFormaturaUnidadeEnsinoVO : prograFormaturaUnidadeEnsinoVOs) {
				if (!prograFormaturaUnidadeEnsinoVOs.get(prograFormaturaUnidadeEnsinoVOs.size() - 1).getUnidadeEnsinoVO().getCodigo().equals(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo())) {
					sqlStr.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo()+", ");
				} else {
					sqlStr.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo());
				}
			}
			sqlStr.append(") ");
		}
		sqlStr.append(" AND (matricula.matricula not in(select distinct matricula from programacaoformaturaaluno ) or ");
		sqlStr.append(" (select case when (programacaoformatura is null) then true else false end FROM programacaoFormaturaAluno p WHERE p.matricula = matricula.matricula)) ");
		sqlStr.append(" ORDER BY Curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomeCurso + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultaRapidaPorNomeCurso(String nomePessoa, Integer unidadeEnsino, Integer curso, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE  (Curso.nome) ilike('").append(nomePessoa).append("%') ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		if (curso != null && curso.intValue() != 0) {
			sqlStr.append(" AND (Curso.codigo = ").append(curso.intValue()).append(") ");
		}
		sqlStr.append(" ORDER BY Curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<MatriculaVO> consultarPorColacaoGrau(Integer Colacao, String ColouGrau, Integer curso, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT matricula.* FROM programacaoformaturaaluno ");
		sql.append("INNER JOIN matricula ON programacaoformaturaaluno.matricula = matricula.matricula ");
		sql.append("WHERE programacaoformaturaaluno.colacaograu = '").append(Colacao).append("' ");
		if (!ColouGrau.equals("ambos")) {
			if (ColouGrau.equals(SituacaoColouGrauProgramacaoFormaturaAluno.COLOU_GRAU.getValor())) {
				sql.append("AND programacaoformaturaaluno.colougrau = 'SI' ");
			} else if (ColouGrau.equals(SituacaoColouGrauProgramacaoFormaturaAluno.NAO_COLOU.getValor())) {
				sql.append("AND programacaoformaturaaluno.colougrau = 'NO' ");
			} else if (ColouGrau.equals(SituacaoColouGrauProgramacaoFormaturaAluno.NAO_INFORMADO.getValor())) {
				sql.append("AND programacaoformaturaaluno.colougrau = 'NI' ");
			}
		}
		if(Uteis.isAtributoPreenchido(curso)) {
			sql.append(" and matricula.curso = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sql.append(" and Matricula.unidadeEnsino in (");
			for (ControleLivroRegistroDiplomaUnidadeEnsinoVO unidade : unidadeEnsinoVOs) {
				if (unidade.equals(unidadeEnsinoVOs.get(unidadeEnsinoVOs.size() -1))) {
					sql.append(unidade.getUnidadeEnsino().getCodigo()).append(")");
				} else {
					sql.append(unidade.getUnidadeEnsino().getCodigo()).append(",");
				}
			}			
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<MatriculaVO> lista = montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
		if (!Uteis.isAtributoPreenchido(lista)) {
			throw new ConsistirException("A Colacão de grau informada não possui matricula(s).");
		}
		return lista;
	}

	public void carregarDados(MatriculaVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((MatriculaVO) obj, NivelMontarDados.BASICO, usuario);
	}

	public void carregarDados(MatriculaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if (obj.getIsDadosBasicosDevemSerCarregados(nivelMontarDados)) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), usuario);
			montarDadosBasico((MatriculaVO) obj, resultado);
		}
		if (obj.getIsDadosCompletosDevemSerCarregados(nivelMontarDados)) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), usuario);
			montarDadosCompleto((MatriculaVO) obj, resultado, usuario);
		}
	}

	public void carregarDadosPorCoordenador(MatriculaVO obj, Integer codigoCoordenador, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if (obj.getIsDadosBasicosDevemSerCarregados(nivelMontarDados)) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicosPorCoordenador(obj.getMatricula(), codigoCoordenador, obj.getUnidadeEnsino().getCodigo(), usuario);
			montarDadosBasico((MatriculaVO) obj, resultado);
		}
	}

	public MatriculaVO consultarPorCodigoFinanceiroMatricula(String codigoFinanceiroMatricula, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select matricula, unidadeensino from matricula WHERE Matricula.codigofinanceiromatricula = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), codigoFinanceiroMatricula);
		if (!tabelaResultado.next()) {
			return null;
		}
		MatriculaVO matriculaVO = new MatriculaVO();
		matriculaVO.setNovoObj(false);
		matriculaVO.setMatricula(tabelaResultado.getString("matricula"));
		matriculaVO.getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeensino"));
		carregarDados(matriculaVO, NivelMontarDados.BASICO, usuarioVO);
		return matriculaVO;
	}

	public MatriculaVO consultarPorChavePrimaria(String matriculaPrm, Integer unidadeEnsino, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		MatriculaVO obj = new MatriculaVO();
		obj.setNovoObj(false);
		obj.setMatricula(matriculaPrm);
		obj.getUnidadeEnsino().setCodigo(unidadeEnsino);
		carregarDados(obj, nivelMontarDados, usuario);
		return obj;
	}

	@Override
	public MatriculaVO consultarPorChavePrimaria(String matricula) throws Exception {
		String sql = getSQLPadraoConsultaBasica() + "where matricula = '"+matricula + "'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (tabelaResultado.next()) {
			MatriculaVO matriculaVO = new MatriculaVO();
			montarDadosBasico(matriculaVO, tabelaResultado);
			return matriculaVO;
		}
		return null;
	}

	public MatriculaVO consultarPorChavePrimariaPorCoordenador(String matriculaPrm, Integer codigoCoordenador, Integer unidadeEnsino, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		MatriculaVO obj = new MatriculaVO();
		obj.setNovoObj(false);
		obj.setMatricula(matriculaPrm);
		obj.getUnidadeEnsino().setCodigo(unidadeEnsino);
		carregarDadosPorCoordenador(obj, codigoCoordenador, nivelMontarDados, usuario);
		return obj;
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>MatriculaVO</code> através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public MatriculaVO consultarPorChavePrimaria(String matriculaPrm, Integer unidadeEnsino, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM Matricula WHERE matricula= '" + matriculaPrm + "'";
		if ((unidadeEnsino != null) && (unidadeEnsino.intValue() != 0)) {
			sqlStr = "SELECT * FROM Matricula WHERE matricula = '" + matriculaPrm + "' and unidadeEnsino = " + unidadeEnsino;
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public MatriculaVO consultarPorMatriculaReceitaDW(String matriculaPrm, Integer unidadeEnsino, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT matricula.matricula, turno.codigo AS \"turno.codigo\", curso.codigo AS \"curso.codigo\", curso.niveleducacional, ");
		sqlStr.append(" areaconhecimento.codigo AS \"areaconhecimento.codigo\" FROM matricula ");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" INNER JOIN turno ON turno.codigo = matricula.turno ");
		sqlStr.append(" LEFT JOIN areaconhecimento ON areaconhecimento.codigo = curso.areaconhecimento ");
		sqlStr.append(" WHERE matricula.matricula = '");
		sqlStr.append(matriculaPrm);
		sqlStr.append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDadosReceitaDW(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public static MatriculaVO montarDadosReceitaDW(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		MatriculaVO obj = new MatriculaVO();
		obj.setNovoObj(false);
		obj.setMatricula(dadosSQL.getString("matricula"));
		// Dados Turno
		obj.getTurno().setCodigo(dadosSQL.getInt("turno.codigo"));
		// Dados Curso
		obj.getCurso().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getCurso().setNivelEducacional(dadosSQL.getString("niveleducacional"));
		obj.getCurso().getAreaConhecimento().setCodigo(dadosSQL.getInt("areaconhecimento.codigo"));
		return obj;

	}

	public MatriculaVO consultarPorChavePrimaria(String matriculaPrm, Integer unidadeEnsino, Integer curso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM Matricula WHERE matricula= '" + matriculaPrm + "'";
		if ((unidadeEnsino != null) && (unidadeEnsino.intValue() != 0)) {
			sqlStr = "SELECT * FROM Matricula WHERE matricula = '" + matriculaPrm + "' and unidadeEnsino = " + unidadeEnsino;
		}
		if ((curso != null) && (curso.intValue() != 0)) {
			sqlStr += " and curso=" + curso;
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

	public MatriculaVO consultarPorChavePrimariaCursoTurma(String matriculaPrm, Integer unidadeEnsino, Integer curso, Integer turma, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT matricula.* FROM matricula ");
		sqlStr.append("WHERE matricula.matricula = '").append(matriculaPrm).append("' ");
		if ((unidadeEnsino != null) && (unidadeEnsino.intValue() != 0)) {
			sqlStr.append("AND matricula.unidadeEnsino = ").append(unidadeEnsino).append(" ");
		}
		if ((curso != null) && (curso.intValue() != 0)) {
			sqlStr.append("AND matricula.curso = ").append(curso).append(" ");
		}
		if ((turma != null) && (turma.intValue() != 0)) {
			sqlStr.append("AND matricula.matricula IN(SELECT DISTINCT mp.matricula FROM matriculaperiodo mp WHERE mp.turma = ").append(turma).append(") ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return Matricula.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Matricula.idEntidade = idEntidade;
	}

	public static String preencherComZerosPosicoesVagas(String padrao, int tamanhoGeracao) {
		if (tamanhoGeracao > padrao.length()) {
			int nrPosicoesPreencher = tamanhoGeracao - padrao.length();
			while (nrPosicoesPreencher > 0) {
				padrao = "0" + padrao;
				nrPosicoesPreencher--;
			}
		}
		return padrao;
	}

	/*public String obterNumeroMatriculasDeAcordoFiltros(Integer unidadeEnsino, Integer curso, Integer ano, int posInicialAno, int posFinalAno, Integer semestre, int posInicialPeriodo, int posFinalPeriodo, UsuarioVO usuario) throws Exception {
		Matricula.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT MAX(matricula) as ultimaMatricula FROM Matricula";
		String condicionalAno = "";
		String condicionalUnidadeEnsino = "";
		String condicionalCurso = "";
		String condicionalSemestre = "";
		if (ano != null) {
			condicionalAno = "((select substr(matricula, " + String.valueOf(posInicialAno) + ", " + String.valueOf(posFinalAno) + ")) = '" + (String.valueOf(ano)).substring(2) + "')";
		}
		if (unidadeEnsino != null) {
			condicionalUnidadeEnsino = "(unidadeensino = " + String.valueOf(unidadeEnsino) + ")";
		}
		if (curso != null) {
			condicionalCurso = "(curso = " + String.valueOf(curso) + ")";
		}
		if (semestre != null) {
			condicionalSemestre = "((select substr(matricula, " + String.valueOf(posInicialPeriodo) + ", " + String.valueOf(posFinalPeriodo) + ") ) = '" + semestre + "')";
		}

		String clausula = " WHERE ";
		String andStr = "";
		if (!condicionalAno.equals("")) {
			sqlStr = sqlStr + clausula + andStr + condicionalAno;
			clausula = "";
			andStr = " and ";
		}
		if (!condicionalUnidadeEnsino.equals("")) {
			sqlStr = sqlStr + clausula + andStr + condicionalUnidadeEnsino;
			clausula = "";
			andStr = " and ";
		}
		if (!condicionalCurso.equals("")) {
			sqlStr = sqlStr + clausula + andStr + condicionalCurso;
			clausula = "";
			andStr = " and ";
		}
		if (!condicionalSemestre.equals("")) {
			sqlStr = sqlStr + clausula + andStr + condicionalSemestre;
			clausula = "";
			andStr = " and ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return null;
		}
		String ultimaLinha = tabelaResultado.getString("ultimaMatricula");
		if (ultimaLinha == null) {
			return "";
		}
		return (ultimaLinha);
	}*/

	//@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public synchronized String gerarNumeroMatricula(MatriculaVO matriculaVO, int anoData, UsuarioVO usuario) throws Exception {
		if(Uteis.isAtributoPreenchido(matriculaVO.getMatricula())) {
			matriculaVO.setGeracaoMatriculaAutomatica(false);
			return matriculaVO.getMatricula().trim();
		}else {
			matriculaVO.setGeracaoMatriculaAutomatica(true);
			String novaMatricula = NumeroMatricula.gerarNumeroMatricula(matriculaVO, anoData);
			Boolean existeMatricula = validarUnicidade(novaMatricula);
			if(existeMatricula != null && existeMatricula) {
				NumeroMatriculaVO obj = new NumeroMatriculaVO();
				obj.setCodigo(matriculaVO.getCodigoNumeroMatricula());
				obj.setIncremental(matriculaVO.getNumeroMatriculaIncrimental());
				//getFacadeFactory().getNumeroMatriculaFacade().alterarIncremental(obj, obj.getIncremental(), usuario);
				return gerarNumeroMatricula(matriculaVO, anoData, usuario);
			}
			return novaMatricula;
		}
	}
	
	private class GerarNumeroMatricula implements Runnable {
		MatriculaVO matriculaVO;
		UsuarioVO usuarioVO;
		int anoData;
		
		public GerarNumeroMatricula(MatriculaVO  matriculaVO, int anoData, UsuarioVO usuario){
			this.matriculaVO = matriculaVO;
			this.usuarioVO = usuario;
			this.anoData = anoData;			
		}
		
		public void run()  {
			try {
				matriculaVO.setMatricula(gerarNumeroMatricula(matriculaVO, anoData, usuarioVO));
			} catch (Exception e) {
				e.printStackTrace();
				matriculaVO.setMatricula("");
			}			
		}
	}

	public String removerZeroEsquerda(String valor) {
		int tamanho = valor.length();
		int cont = 0;
		while (cont < tamanho) {
			char a = valor.charAt(0);
			if (a == '0') {
				valor = valor.substring(1);
			} else {
				valor = valor.substring(0);
				return valor;
			}
			cont++;
		}
		return valor;
	}

	public void liberarMatricula(MatriculaVO obj, UsuarioVO usuarioResponsavel) throws Exception {
		// Matricula.verificarPermissaoUsuarioFuncionalidade("Matricula_LiberarMatricula");
		// obj.setSituacaoFinanceira("QI");
		// obj.setDataLiberacaoMatricula(new Date());
		// obj.setResponsavelLiberacaoMatricula(usuarioResponsavel);
	}

	public void emitirBoletoMatricula(MatriculaVO obj, UsuarioVO usuarioResponsavel) throws Exception {
		// Matricula.verificarPermissaoUsuarioFuncionalidade("Matricula_EmitirBoletoMatricula");
		// obj.setDataEmissaoBoletoMatricula(new Date());
		// obj.setResponsavelEmissaoBoletoMatricula(usuarioResponsavel);
	}

	public void inicializarTextoContratoPlanoFinanceiroAluno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, boolean forcarTrocarContrato, UsuarioVO usuario) throws Exception {
		if (matriculaPeriodoVO.getTurma().getCodigo() == 0) {
			throw new Exception("Turma não selecionada, por favor escolha uma turma!");
		}
		if (Uteis.isAtributoPreenchido(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso())) {
			CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamento = getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo().intValue(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
			matriculaPeriodoVO.setCondicaoPagamentoPlanoFinanceiroCurso(condicaoPagamento);
		}
		getFacadeFactory().getMatriculaPeriodoFacade().realizarDefinicaoContratoPadraoSerUsadoMatriculaPeriodo(TipoContratoMatriculaEnum.getEnumPorValor(matriculaVO.getTipoMatricula()), matriculaPeriodoVO, forcarTrocarContrato, false, usuario);
	}

	public void inicializarPlanoFinanceiroMatriculaPeriodo(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception {
		if ((matriculaPeriodoVO == null) || (matriculaVO == null) || (matriculaPeriodoVO.getTurma().getCodigo().equals(0))) {
			throw new Exception("Dados da matrícula não disponíveis para determinar Plano Financeiro!");
		}
		if (Uteis.isAtributoPreenchido(matriculaPeriodoVO)) {
			if (!matriculaPeriodoVO.getAlterarPlanoCondicacaoPagamentoPersistido() && Uteis.isAtributoPreenchido(matriculaPeriodoVO.getPlanoFinanceiroCurso())) {
				// se estamos editando uma matriculaPeriodo, não existe uma
				// razão para inicializar o
				// plano financeiro do aluno, pois já existe um que foi
				// persistido. A não ser que a
				// variavel
				// matriculaPeriodoVO.getAlterarPlanoCondicacaoPagamentoPersistido()
				// este true
				// (o que significaria que o usuário deseja alterar o plano
				// financeiro) temos que sair
				// deste método sem fazer nada para que o plano/conficao
				// financeiro do aluno seja
				// mantido na edição.
				// Assim, vamos somente carregar os dados do plano financeiro
				// atual.
				matriculaPeriodoVO.setPlanoFinanceiroCurso(getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getPlanoFinanceiroCurso().getCodigo(), "", Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
				return;
			}
		}
		TurmaVO turmaPF = matriculaPeriodoVO.getTurma();

		if (Uteis.isAtributoPreenchido(turmaPF.getPlanoFinanceiroCurso().getCodigo())) {
			PlanoFinanceiroCursoVO planoFinanceiroCursoVO = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(turmaPF.getPlanoFinanceiroCurso().getCodigo(), "AT", Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			planoFinanceiroCursoVO.setNivelMontarDados(NivelMontarDados.TODOS);
			matriculaPeriodoVO.setPlanoFinanceiroCurso(planoFinanceiroCursoVO);
			if (matriculaPeriodoVO.getFinanceiroManual()) {
				matriculaPeriodoVO.setCondicaoPagamentoPlanoFinanceiroCurso(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCursoPersistido());
			}
			matriculaPeriodoVO.setCategoriaCondicaoPagamento(matriculaPeriodoVO.getTurma().getCategoriaCondicaoPagamento());
			matriculaPeriodoVO.setContratoFiador(planoFinanceiroCursoVO.getTextoPadraoContratoFiador());
			matriculaPeriodoVO.setContratoExtensao(planoFinanceiroCursoVO.getTextoPadraoContratoExtensao());
		} else {
			UnidadeEnsinoCursoVO unidadeCursoVO = matriculaPeriodoVO.getUnidadeEnsinoCursoVO();
			if(!Uteis.isAtributoPreenchido(unidadeCursoVO)) {
				unidadeCursoVO = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(matriculaVO.getCurso().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo(), matriculaVO.getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			}
			if (Uteis.isAtributoPreenchido(unidadeCursoVO.getPlanoFinanceiroCurso().getCodigo())) {
				PlanoFinanceiroCursoVO planoFinanceiroCursoVO = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(unidadeCursoVO.getPlanoFinanceiroCurso().getCodigo(), "AT", Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				planoFinanceiroCursoVO.setNivelMontarDados(NivelMontarDados.TODOS);
				matriculaPeriodoVO.setPlanoFinanceiroCurso(planoFinanceiroCursoVO);
				matriculaPeriodoVO.setContratoFiador(unidadeCursoVO.getPlanoFinanceiroCurso().getTextoPadraoContratoFiador());
				matriculaPeriodoVO.setContratoExtensao(unidadeCursoVO.getPlanoFinanceiroCurso().getTextoPadraoContratoExtensao());
			}
		}
	}

	public PlanoFinanceiroCursoVO obterPlanoFinanceiroCursoMatriculaVO(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception {
		try {
			PlanoFinanceiroCursoVO planoCursoFinanceiro = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarUnidadeEnsinoCursoTurno(matriculaVO.getUnidadeEnsino().getCodigo(), matriculaVO.getCurso().getCodigo(), matriculaVO.getTurno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			return planoCursoFinanceiro;
		} catch (Exception e) {
			return null;
		}
	}

	public void atualizarListaDocumentosEntregarMatricula(MatriculaVO matricula, UsuarioVO usuario) throws Exception {
		matricula.getCurso().setDocumentacaoCursoVOs(DocumentacaoCurso.consultarDocumentacaoCursos(matricula, false, usuario));
		Iterator i = matricula.getCurso().getDocumentacaoCursoVOs().iterator();
		while (i.hasNext()) {
			DocumentacaoCursoVO docObrigatorio = (DocumentacaoCursoVO) i.next();
			DocumetacaoMatriculaVO docMat = matricula.consultarObjDocumetacaoMatriculaVO(docObrigatorio.getTipoDeDocumentoVO().getCodigo());
			if (docMat == null) {
				docMat = new DocumetacaoMatriculaVO();
				docMat.setMatricula(matricula.getMatricula());
				docMat.setTipoDeDocumentoVO(docObrigatorio.getTipoDeDocumentoVO());
				docMat.setGerarSuspensaoMatricula(docObrigatorio.getGerarSuspensaoMatricula());
				matricula.adicionarObjDocumetacaoMatriculaVOs(docMat);
			}
		}

	}
	
	public boolean isMatriculaIntegralizada(MatriculaVO matricula, Integer gradeCurricular, UsuarioVO usuario, ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO) throws Exception {
		if (Uteis.isAtributoPreenchido(matricula.getMatricula()) && Uteis.isAtributoPreenchido(gradeCurricular)) {
			/**
			 * Valida se existe alguma disciplina obrigatório não cumprida
			 */
			boolean integralizado = true;
			GradeCurricularVO gradeCurricularVO =  getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(gradeCurricular, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			List<GradeDisciplinaVO> gradeDisciplinaVOs = getFacadeFactory().getGradeDisciplinaFacade().consultarDisciplinasObrigatoriasNaoCumpridasDaGrade(matricula.getMatricula(), gradeCurricular, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario, 0);
			if(programacaoFormaturaAlunoVO != null) {
				programacaoFormaturaAlunoVO.setListaGradeDisciplinaObrigatorioPendente(gradeDisciplinaVOs);
			}
			if (Uteis.isAtributoPreenchido(gradeDisciplinaVOs)) {
				integralizado = false;
				matricula.setListaGradeDisciplinaObrigatorioPendente(gradeDisciplinaVOs);
			}
			/**
			 * Valida se o aluno cumpriu a carga horária optativa minima obrigatória
			 */
			if(!getFacadeFactory().getHistoricoFacade().realizarVerificacaoAlunoCumpriuCargaHorariaDisciplinaOptativa(matricula, gradeCurricular, programacaoFormaturaAlunoVO)) {
				integralizado = false;
			}
			/**
			 * Valida se na grade exige atividade complementar e se o aluno cumpriu a carga horária do mesmo
			 */
			Integer cargaHorariaObrigatoriaAtividadeComplementar = getFacadeFactory().getGradeCurricularTipoAtividadeComplementarFacade().consultarCargaHorariaObrigatoria(gradeCurricular);
			if(Uteis.isAtributoPreenchido(cargaHorariaObrigatoriaAtividadeComplementar)){
				Boolean atividadeComplementarIntegralizada = getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().realizarValidacaoIntegracaoAtividadeComplementar(matricula.getMatricula(), gradeCurricular);
				Integer cargaHorariaRealizadaAtividadeComplementar = getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarCargaHorariaRealizada(matricula.getMatricula(), gradeCurricular,false);
				if(programacaoFormaturaAlunoVO != null) {
					programacaoFormaturaAlunoVO.setHorasAtividadeComplementarExigida(cargaHorariaObrigatoriaAtividadeComplementar);
					programacaoFormaturaAlunoVO.setHorasAtividadeComplementarCumprida(cargaHorariaRealizadaAtividadeComplementar);
				} else {
					matricula.setHorasAtividadeComplementarExigida(cargaHorariaObrigatoriaAtividadeComplementar);
					matricula.setHorasAtividadeComplementarCumprida(cargaHorariaRealizadaAtividadeComplementar);
				}
				if(!atividadeComplementarIntegralizada){
					integralizado = false;
				}
			}
			/**
			 * Valida se na grade exige estágio e se o aluno cumpriu a carga horária do estágio
			 */
			Integer cargaHorariaObrigatoriaEstagio = getFacadeFactory().getGradeCurricularFacade().consultarCargaHorariaObrigatoriaEstagioPorMatrizCurricular(gradeCurricular);
			if(Uteis.isAtributoPreenchido(cargaHorariaObrigatoriaEstagio)){
				Integer cargaHorariaRealizadaEstagio = getFacadeFactory().getEstagioFacade().consultarCargaHorariaRealizadaEstagioMatricula(matricula.getMatricula());
				if(programacaoFormaturaAlunoVO != null) {
					programacaoFormaturaAlunoVO.setHorasEstagioExigida(cargaHorariaObrigatoriaEstagio);
					programacaoFormaturaAlunoVO.setHorasEstagioCumprida(cargaHorariaRealizadaEstagio);
				} else {
					matricula.setHorasEstagioExigida(cargaHorariaObrigatoriaEstagio);
					matricula.setHorasEstagioCumprida(cargaHorariaRealizadaEstagio);
				}
				if(cargaHorariaRealizadaEstagio < cargaHorariaObrigatoriaEstagio){
					integralizado = false;
				}
			}
			if(programacaoFormaturaAlunoVO != null) {
				matricula.setTotalCargaHorariaCumprido(programacaoFormaturaAlunoVO.getHorasEstagioCumprida() +
						programacaoFormaturaAlunoVO.getHorasAtividadeComplementarCumprida()+
						programacaoFormaturaAlunoVO.getHorasDisciplinaOptativaCumprida() +
						(gradeCurricularVO.getTotalCargaHorariaDisciplinasObrigatorias() - gradeDisciplinaVOs.stream().flatMapToInt(g -> IntStream.of(g.getCargaHoraria())).sum()));
				if(gradeCurricularVO.getCargaHoraria() < matricula.getTotalCargaHorariaCumprido()) {
					matricula.setPercentualCumprido(100.0);
				}else {
					matricula.setPercentualCumprido(Uteis.arrendondarForcando2CadasDecimais((matricula.getTotalCargaHorariaCumprido() * 100) / gradeCurricularVO.getCargaHoraria()));
				}
			} else {
				matricula.setTotalCargaHorariaCumprido(matricula.getHorasEstagioCumprida() +
						matricula.getHorasAtividadeComplementarCumprida()+
						matricula.getHorasDisciplinaOptativaCumprida() +
						(gradeCurricularVO.getTotalCargaHorariaDisciplinasObrigatorias() - gradeDisciplinaVOs.stream().flatMapToInt(g -> IntStream.of(g.getCargaHoraria())).sum()));
				if(gradeCurricularVO.getCargaHoraria() < matricula.getTotalCargaHorariaCumprido()) {
					matricula.setPercentualCumprido(100.0);
				}else {
					matricula.setPercentualCumprido(Uteis.arrendondarForcando2CadasDecimais((matricula.getTotalCargaHorariaCumprido() * 100) / gradeCurricularVO.getCargaHoraria()));
				}
			}
			return integralizado;
		} else {
			throw new ConsistirException("É necessário informar a Matricula e a Grade Curricular do aluno para verificação");
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarEnade(List<MatriculaVO> lista) throws Exception {
		if (lista.isEmpty()) {
			throw new Exception("A lista de ALUNOS encontra-se vazia. Favor consultar.");
		}
		try {
			for (MatriculaVO matricula : lista) {
				if (!matricula.getFezEnade() && Uteis.isAtributoPreenchido(matricula.getNotaEnade())) {
					matricula.setNotaEnade(null);
				}
				this.alterarHorasComplementaresFormaIngressoEnadeAluno(matricula);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarLiberarBloqueioAlunoInadimplente(final MatriculaVO matricula) {
		final String sql = "UPDATE matricula SET liberarBloqueioAlunoInadimplente=?, matriculasuspensa = false WHERE ((matricula = ?)) ";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAtualizar = arg0.prepareStatement(sql);
				sqlAtualizar.setBoolean(1, matricula.getLiberarBloqueioAlunoInadimplente());
				sqlAtualizar.setString(2, matricula.getMatricula());
				return sqlAtualizar;
			}
		});

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterarHorasComplementaresFormaIngressoEnadeAluno(final MatriculaVO matricula) throws Exception {
		final String sql = "UPDATE matricula SET fezenade=?, dataEnade=?, notaEnade=?, horasComplementares=?, formaIngresso=? , enade=?, nomeMonografia=?, notaMonografia=?, disciplinasProcSeletivo=?, totalPontoProcSeletivo = ?, tipoTrabalhoConclusaoCurso=? WHERE ((matricula = ?)) ";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAtualizar = arg0.prepareStatement(sql);
				sqlAtualizar.setBoolean(1, matricula.getFezEnade());
				if (Uteis.isAtributoPreenchido(matricula.getDataEnade())) {
					sqlAtualizar.setDate(2, Uteis.getDataJDBC(matricula.getDataEnade()));
				} else {
					sqlAtualizar.setNull(2, 0);
				}
				if (Uteis.isAtributoPreenchido(matricula.getNotaEnade())) {
					sqlAtualizar.setDouble(3, matricula.getNotaEnade());
				} else {
					sqlAtualizar.setNull(3, 0);
				}
				if (Uteis.isAtributoPreenchido(matricula.getHorasComplementares())) {
					sqlAtualizar.setDouble(4, matricula.getHorasComplementares());
				} else {
					sqlAtualizar.setNull(4, 0);
				}
				if (Uteis.isAtributoPreenchido(matricula.getFormaIngresso())) {
					sqlAtualizar.setString(5, matricula.getFormaIngresso());
				} else {
					sqlAtualizar.setNull(5, 0);
				}
				if (Uteis.isAtributoPreenchido(matricula.getEnadeVO().getCodigo())) {
					sqlAtualizar.setInt(6, matricula.getEnadeVO().getCodigo());
				} else {
					sqlAtualizar.setNull(6, 0);
				}
				sqlAtualizar.setString(7, matricula.getTituloMonografia());

				if (matricula.getNotaMonografia() == null) {
					sqlAtualizar.setNull(8, 0);
				} else {
					sqlAtualizar.setDouble(8, matricula.getNotaMonografia());
				}
				sqlAtualizar.setString(9, matricula.getDisciplinasProcSeletivo());
				if (matricula.getTotalPontoProcSeletivo() != null) {
					sqlAtualizar.setDouble(10, matricula.getTotalPontoProcSeletivo());
				} else {
					sqlAtualizar.setNull(10, 0);
				}
				sqlAtualizar.setString(11, matricula.getTipoTrabalhoConclusaoCurso());
				sqlAtualizar.setString(12, matricula.getMatricula());
				return sqlAtualizar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarFormaIngressoAluno(final String matricula, final String formaIngresso) throws Exception {
		final String sql = "UPDATE matricula SET formaIngresso=? WHERE ((matricula = ?)) ";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAtualizar = arg0.prepareStatement(sql);
				if (Uteis.isAtributoPreenchido(formaIngresso)) {
					sqlAtualizar.setString(1, formaIngresso);
				} else {
					sqlAtualizar.setNull(1, 0);
				}
				sqlAtualizar.setString(2, matricula);
				return sqlAtualizar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDadosAlteracoesCadastraisMatricula(final MatriculaVO matricula, UsuarioVO usuario)
			throws Exception {
      try{
  		final String sql = "UPDATE matricula SET dataInicioCurso=?, dataConclusaoCurso=?, anoConclusao=?, semestreConclusao=?, anoIngresso=?, semestreIngresso=?,  "
  				+ "dataColacaoGrau=?, adesivoInstituicaoEntregue=?, mesIngresso = ?, dataProcessoSeletivo=?, totalPontoProcSeletivo=?,naoApresentarCenso=?, notaEnem=?, "
  				+ "observacaoDiploma=?, localarmazenamentodocumentosmatricula=?, codigoFinanceiroMatricula=?, inscricao=?, classificacaoIngresso=?, semestreAnoIngressoCenso=?, "
  				+ "autorizacaoCurso=?, renovacaoReconhecimento=?, bloquearEmissaoBoletoMatMenVisaoAluno=?, codigoInscricaoOVG=?,localprocessoseletivo = ?, "
  				+ "permiteExecucaoReajustePreco=?, orientadorMonografia=?, formacaoAcademica=?, proficienciaLinguaEstrangeira=?, situacaoProficienciaLinguaEstrangeira=?, "
  				+ " naoenviarmensagemcobranca=?, considerarParcelasMaterialDidaticoReajustePreco=?, permitirInclusaoExclusaoDisciplinasRenovacao=?, titulacaoOrientadorMonografia=?, cargaHorariaMonografia=?, diaSemanaAula = ?, turnoAula = ?, "
  				+ "financiamentoEstudantilCenso=?, justificativaCenso=?, tipoMobilidadeAcademica=?, mobilidadeAcademicaNacionalIESDestino=?, mobilidadeAcademicaInternacionalPaisDestino=?, informacoescensorelativoano=? WHERE ((matricula = ?)) " 
  				+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
  		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
  			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
  				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
  				if (Uteis.isAtributoPreenchido(matricula.getDataInicioCurso())) {
  					sqlAlterar.setDate(1, Uteis.getDataJDBC(matricula.getDataInicioCurso()));
  				} else {
  					sqlAlterar.setNull(1, 0);
  				}
  				if (Uteis.isAtributoPreenchido(matricula.getDataConclusaoCurso())) {
  					sqlAlterar.setDate(2, Uteis.getDataJDBC(matricula.getDataConclusaoCurso()));
  				} else {
  					sqlAlterar.setNull(2, 0);
  				}
  				if (Uteis.isAtributoPreenchido(matricula.getDataConclusaoCurso())) {
  					sqlAlterar.setString(3, String.valueOf(Uteis.getAnoData(matricula.getDataConclusaoCurso())));
  				} else {
  					sqlAlterar.setNull(3, 0);
  				}
  				if (Uteis.isAtributoPreenchido(matricula.getDataConclusaoCurso())) {
  					sqlAlterar.setString(4, Uteis.getSemestreData(matricula.getDataConclusaoCurso()));
  				} else {
  					sqlAlterar.setNull(4, 0);
  				}
  				sqlAlterar.setString(5, matricula.getAnoIngresso());
  				sqlAlterar.setString(6, matricula.getSemestreIngresso());

  				if (Uteis.isAtributoPreenchido(matricula.getDataColacaoGrau())) {
  					sqlAlterar.setDate(7, Uteis.getDataJDBC(matricula.getDataColacaoGrau()));
  				} else {
  					sqlAlterar.setNull(7, 0);
  				}
  				sqlAlterar.setBoolean(8, matricula.getAdesivoInstituicaoEntregue());
  				sqlAlterar.setString(9, matricula.getMesIngresso());

  				if (Uteis.isAtributoPreenchido(matricula.getDataProcessoSeletivo())) {
  					sqlAlterar.setDate(10, Uteis.getDataJDBC(matricula.getDataProcessoSeletivo()));
  				} else {
  					sqlAlterar.setNull(10, 0);
  				}
  				if (matricula.getTotalPontoProcSeletivo() != null) {
  					sqlAlterar.setDouble(11, matricula.getTotalPontoProcSeletivo());
  				} else {
  					sqlAlterar.setNull(11, 0);
  				}
  				sqlAlterar.setBoolean(12, matricula.getNaoApresentarCenso());
  				sqlAlterar.setDouble(13, matricula.getNotaEnem());
  				sqlAlterar.setString(14, matricula.getObservacaoDiploma());
  				sqlAlterar.setString(15, matricula.getLocalArmazenamentoDocumentosMatricula());
  				sqlAlterar.setString(16, matricula.getCodigoFinanceiroMatricula());
  				if (Uteis.isAtributoPreenchido(matricula.getInscricao())) {
  					sqlAlterar.setInt(17, matricula.getInscricao().getCodigo());
  				} else {
  					sqlAlterar.setNull(17, 0);
  				}				
  				if (matricula.getClassificacaoIngresso() != null){
  					sqlAlterar.setInt(18, matricula.getClassificacaoIngresso());
  				} else {
  					sqlAlterar.setNull(18, 0);
  				}
  				sqlAlterar.setString(19, matricula.getSemestreAnoIngressoCenso());
  				if (Uteis.isAtributoPreenchido(matricula.getAutorizacaoCurso())) {
  					sqlAlterar.setInt(20, matricula.getAutorizacaoCurso().getCodigo());
  				} else {
  					sqlAlterar.setNull(20, 0);
  				}
  				if (Uteis.isAtributoPreenchido(matricula.getRenovacaoReconhecimentoVO())) {
  					sqlAlterar.setInt(21, matricula.getRenovacaoReconhecimentoVO().getCodigo());
  				} else {
  					sqlAlterar.setNull(21, 0);
  				}
  				sqlAlterar.setBoolean(22, matricula.getBloquearEmissaoBoletoMatMenVisaoAluno());
  				sqlAlterar.setString(23, matricula.getCodigoInscricaoOVG());
  				sqlAlterar.setString(24, matricula.getLocalProcessoSeletivo());
  				sqlAlterar.setBoolean(25, matricula.getPermiteExecucaoReajustePreco());
  				sqlAlterar.setString(26, matricula.getOrientadorMonografia());
  				if (Uteis.isAtributoPreenchido(matricula.getFormacaoAcademica().getCodigo())) {
  					sqlAlterar.setInt(27, matricula.getFormacaoAcademica().getCodigo());
  				} else {
  					sqlAlterar.setNull(27, 0);
  				}
  				sqlAlterar.setString(28, matricula.getProficienciaLinguaEstrangeira());
  				sqlAlterar.setString(29, matricula.getSituacaoProficienciaLinguaEstrangeira());
  				sqlAlterar.setBoolean(30, matricula.getNaoEnviarMensagemCobranca());
  				int x = 31;
				Uteis.setValuePreparedStatement(matricula.getConsiderarParcelasMaterialDidaticoReajustePreco(), x++, sqlAlterar);
				Uteis.setValuePreparedStatement(matricula.getPermitirInclusaoExclusaoDisciplinasRenovacao(), x++, sqlAlterar);
				Uteis.setValuePreparedStatement(matricula.getTitulacaoOrientadorMonografia(), x++, sqlAlterar);
				Uteis.setValuePreparedStatement(matricula.getCargaHorariaMonografia(), x++, sqlAlterar);
				Uteis.setValuePreparedStatement(matricula.getDiaSemanaAula().name(), x++, sqlAlterar);
				Uteis.setValuePreparedStatement(matricula.getTurnoAula().name(), x++, sqlAlterar);				
				if (Uteis.isAtributoPreenchido(matricula.getListaFinanciamentoEstudantilVOs())) {
					sqlAlterar.setString(x++, matricula.getListaFinanciamentoEstudantilVOs().stream().collect(Collectors.joining(";")));
				} else {
					sqlAlterar.setString(x++, "");
				}
				sqlAlterar.setString(x++, matricula.getJustificativaCensoEnum().name());
				sqlAlterar.setString(x++, matricula.getTipoMobilidadeAcademicaEnum().name());
				if (matricula.getTipoMobilidadeAcademicaEnum().isNacional()) {
					sqlAlterar.setString(x++, matricula.getMobilidadeAcademicaNacionalIESDestino());
					sqlAlterar.setString(x++, "");
				} else if (matricula.getTipoMobilidadeAcademicaEnum().isInternacional()) {
					sqlAlterar.setString(x++, "");
					sqlAlterar.setString(x++, matricula.getMobilidadeAcademicaInternacionalPaisDestino());
				} else {
					sqlAlterar.setString(x++, "");
					sqlAlterar.setString(x++, "");
				}
				sqlAlterar.setString(x++, matricula.getInformacoesCensoRelativoAno());
				sqlAlterar.setString(x++, matricula.getMatricula());
  				return sqlAlterar;
  			}
  		});
      }catch (Exception e) {
			if(e.getMessage().contains("check_matricula_codigofinanceiromatricula")){
				throw new Exception(UteisJSF.internacionalizar("msg_Matricula_codigoFinanceiroMatriculaDuplicado"));
			}
			throw e;
		}

	}

	/**
	 * Método verifica se existe algum documento marcado para impedir a
	 * renovação da matrícula. Adicionalmente, este método coloca true no
	 * atributo DocumetacaoMatriculaVO.documentoImpedindoRenovacao para os
	 * documentos que estão gerando este bloqueio
	 */
	public void verificarDocumentaoImpediRenovacaoEstaPendente(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception {
		StringBuilder mensagemErro = new StringBuilder();
		mensagemErro.append("Não é possível realizar a renovação desta matrícula, pois os seguintes documentos obrigatórios para renovação estão Pendentes (");
		String virgula = "";
		boolean algumeDocumentoImpediRenovacao = false;
		if (matriculaVO.getCurso().getDocumentacaoCursoVOs().isEmpty()) {
			matriculaVO.getCurso().setDocumentacaoCursoVOs(getFacadeFactory().getDocumentacaoCursoFacade().consultarPorCurso(matriculaVO.getCurso().getCodigo(), false, usuario));
		}

		for (DocumentacaoCursoVO documentacaoCursoVO : matriculaVO.getCurso().getDocumentacaoCursoVOs()) {
			if (documentacaoCursoVO.getImpedirRenovacaoMatricula()) {
				for (DocumetacaoMatriculaVO documetacaoMatriculaVO : matriculaVO.getDocumetacaoMatriculaVOs()) {
					if (documetacaoMatriculaVO.getTipoDeDocumentoVO().getCodigo().equals(documentacaoCursoVO.getTipoDeDocumentoVO().getCodigo())) {
						// achamos o controle na matricula do documento que pode
						// impedir a renovação da matrícula
						if ((!documetacaoMatriculaVO.getEntregue()) || (documetacaoMatriculaVO.getSituacao().equals("EI"))) {
							algumeDocumentoImpediRenovacao = true;
							documetacaoMatriculaVO.setDocumentoImpedindoRenovacao(Boolean.TRUE);
							mensagemErro.append(virgula).append(documentacaoCursoVO.getTipoDeDocumentoVO().getNome());
							virgula = ", ";
						}
						break;
					}
				}
			}
		}
		mensagemErro.append(").");
		if ((algumeDocumentoImpediRenovacao) && (!matriculaVO.getLiberarRenovacaoDocumentaoImpediRenovacaoPendente())) {
			// acima, testa-se o usuario já liberou renovacao, via permissao na
			// tela de renovacao
			// neste caso nao temos por que gerar excecao...
			matriculaVO.setExisteDocumentoPendenteImpediRenovacao(Boolean.TRUE);
			throw new Exception(mensagemErro.toString());
		}
	}

	public void gerenciarEntregaDocumentoMatricula(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, usuario);
		if (matriculaVO.getDocumetacaoMatriculaVOs().isEmpty()) {
			inicializarDocumentacaoMatriculaCurso(matriculaVO, usuario);
		}
	}

	public void validarMatriculaPodeSerRenovada(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO) throws Exception {
		if(matriculaVO.getIsTrancada()) {
			matriculaVO.setSituacao("AT");
			if(!Uteis.isAtributoPreenchido(matriculaPeriodoVO)) {
				
			}
		}
		if (!matriculaVO.getIsAtiva()) {
			throw new Exception("Esta matrícula não pode ser renovada pois a mesma encontra-se: " + matriculaVO.getSituacao_Apresentar().toUpperCase());
		}
		
		// if (matriculaVO.getSituacaoFinanceira().equals("PF")) {
		// throw new
		// Exception("Esta matrícula não pode ser renovada pois a mesma encontra-se: "
		// + matriculaVO.getSituacaoFinanceira_Apresentar().toUpperCase());
		// }
	}

	public List<MatriculaVO> consultarPorMatriculaCodigoCurso(String valorConsulta, Integer curso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM matricula WHERE matricula LIKE '" + valorConsulta + "%' AND curso = " + curso + " ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	public List<MatriculaVO> consultarPorNomePessoaCodigoCurso(String valorConsulta, Integer curso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM matricula LEFT JOIN pessoa ON matricula.aluno = pessoa.codigo ";
		sqlStr += "WHERE sem_acentos(pessoa.nome) iLIKE (sem_acentos(?)) AND matricula.curso = " + curso + " ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta+"%");
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario);
	}

	public static void montarDadosDescontoProgressivo(MatriculaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPlanoFinanceiroAluno().getDescontoProgressivo().getCodigo() == 0) {
			obj.getPlanoFinanceiroAluno().setDescontoProgressivo(new DescontoProgressivoVO());
			return;
		}
		if (obj.getPlanoFinanceiroAluno().getDescontoProgressivo().getIsDadosDevemSerCarregados(nivelMontarDados)) {
			Integer codigoDescontoProgressivo = obj.getPlanoFinanceiroAluno().getDescontoProgressivo().getCodigo();
			obj.getPlanoFinanceiroAluno().setDescontoProgressivo(getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(codigoDescontoProgressivo, usuario));
			obj.getPlanoFinanceiroAluno().getDescontoProgressivo().setNivelMontarDados(NivelMontarDados.TODOS);
		}
	}

	public void processarRotinaSetarTurmaMatricula(UsuarioVO usuario) throws Exception {
		List<TurnoVO> listaTurno = getFacadeFactory().getTurnoFacade().consultarPorCodigo(0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		List<MatriculaVO> listaAlunos = realizarConsultaAlunosSemTurmaNaMatricula(listaTurno);
		for (MatriculaVO matri : listaAlunos) {
			this.alterarTurnoMatricula(matri.getMatricula(), matri.getTurno().getCodigo());
		}
	}

	public List<MatriculaVO> realizarConsultaAlunosSemTurmaNaMatricula(List<TurnoVO> listaTurnos) throws Exception {
		List listaFinal = new ArrayList(0);
		for (TurnoVO turno : listaTurnos) {
			String sqlStr = "select distinct matricula.* from matriculaPeriodo inner join turma on matriculaPeriodo.turma = turma.codigo ";
			sqlStr += " inner join matricula on matriculaperiodo.matricula = matricula.matricula ";
			sqlStr += "WHERE matricula.turno <> " + turno.getCodigo() + " AND turma.turno = " + turno.getCodigo() + " ";
			sqlStr += " and matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' ";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
			while (tabelaResultado.next()) {
				MatriculaVO obj = new MatriculaVO();
				montarDadosMatricula(obj, tabelaResultado);
				obj.setTurno(turno);
				listaFinal.add(obj);
			}
		}
		return listaFinal;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTurnoMatricula(String matricula, Integer turno) throws Exception {
		String sqlStr = "UPDATE Matricula set turno=? WHERE (matricula = ?)";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { turno, matricula });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void regerarBoletos(MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		try { // Alterado para não regerar nossoNumero quando o financeiro
				// manual estiver ativado
			if (!matriculaPeriodoVO.getFinanceiroManual()) {
				for (MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO : matriculaPeriodoVO.getMatriculaPeriodoVencimentoVOs()) {
					if (!matriculaPeriodoVencimentoVO.getContaReceber().getCodigo().equals(0)) {
						if (matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getNaoRegerarParcelaVencida() &&
								matriculaPeriodoVencimentoVO.getContaReceber().getDataVencimento().before(new Date())) {
							continue;
						}
						if (matriculaPeriodoVencimentoVO.getContaReceber().getSituacaoAReceber()) {
							getFacadeFactory().getContaReceberFacade().gerarDadosBoleto(matriculaPeriodoVencimentoVO.getContaReceber(), configuracaoFinanceiroVO, false, usuario);
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public boolean executarVerificacaoPermissaoAlterarOrdemDesconto() {
		try {
			alterar("MatriculaAlterarOrdemDescontos");
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void calcularTotalDesconto(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List ordemDesconto, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception {
		Matricula.montarDadosDescontoProgressivo(matriculaVO, NivelMontarDados.TODOS, usuario);
		realizarCalculosValoresAplicadosMatricula(matriculaVO, matriculaPeriodoVO, ordemDesconto, usuario, configuracaoFinanceiro);
		realizarCalculosValoresAplicadosMaterialDidatico(matriculaVO, matriculaPeriodoVO, ordemDesconto, usuario, configuracaoFinanceiro);
		realizarCalculosValoresAplicadosMensalidade(matriculaVO, matriculaPeriodoVO, ordemDesconto, usuario, configuracaoFinanceiro);

	}

	private void realizarCalculosValoresAplicadosMatricula(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List ordemDesconto, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception {
		DescontoProgressivoVO descontoprogressivoVO = new DescontoProgressivoVO();
		if (!matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getDescontoProgressivoPadraoMatricula().getCodigo().equals(0)) {
			descontoprogressivoVO = getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getDescontoProgressivoPadraoMatricula().getCodigo(), usuario);
			// descontoprogressivoVO =
			// matriculaVO.getPlanoFinanceiroAluno().getDescontoProgressivo();
			// TODO Acrescentado DescontoProgressivoMatricula Alberto 02/12/2010
		}
		/**
		 * Responsável por definir a data base de vencimento da matrícula
		 * seguindo o mesmo padrao do método gerarVencimentoMatricula
		 * (MatriculaPeriodo).
		 */
		Date dataVencimentoMatricula = matriculaPeriodoVO.getData();
		if(Uteis.isAtributoPreenchido(matriculaPeriodoVO.getDataVencimentoMatriculaEspecifico())) {
			dataVencimentoMatricula = matriculaPeriodoVO.getDataVencimentoMatriculaEspecifico();
		}else if (matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().getUsarDataVencimentoDataMatricula()) {
			if (matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().getQtdeDiasAvancarDataVencimentoMatricula() > 0)
				dataVencimentoMatricula = Uteis.obterDataAvancada(matriculaPeriodoVO.getData(), matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().getQtdeDiasAvancarDataVencimentoMatricula());
		} else if (Uteis.isAtributoPreenchido(matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().getDataVencimentoMatricula())) {
			dataVencimentoMatricula = matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().getDataVencimentoMatricula();
		}
		/**
		 * Responsável por definir a data base de vencimento da parcela
		 */
		matriculaPeriodoVO.setListaDescontosMatricula(getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(true, matriculaPeriodoVO.getValorMatriculaCheio(), matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoMatricula(), matriculaVO.getPlanoFinanceiroAluno().getPercDescontoMatricula(), matriculaVO.getPlanoFinanceiroAluno().getValorDescontoMatricula(), matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela(), dataVencimentoMatricula, dataVencimentoMatricula, ordemDesconto, descontoprogressivoVO, matriculaVO.getPlanoFinanceiroAluno().getPlanoDescontoInstitucionalVOs(), matriculaVO.getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs(), 0, configuracaoFinanceiro.getUsaDescontoCompostoPlanoDesconto(), configuracaoFinanceiro, Boolean.FALSE, null, matriculaVO.getMatricula(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getAplicarCalculoComBaseDescontosCalculados(), matriculaVO.getUnidadeEnsino().getCidade().getCodigo()));
		/**
		 * Responsável por percorrer a lista de
		 * PlanoFinanceiroAlunoDescricaoDescontosVO de forma a remover aqueles
		 * cujo valor do desconto seja 0.
		 */
		for (Iterator<PlanoFinanceiroAlunoDescricaoDescontosVO> iterator = matriculaPeriodoVO.getListaDescontosMatricula().iterator(); iterator.hasNext();) {
			PlanoFinanceiroAlunoDescricaoDescontosVO obj = iterator.next();
			if (obj.executarCalculoValorTotalDesconto().equals(0.0) && obj.getDiaNrAntesVencimento() > 0) {
				iterator.remove();
			}
		}
		matriculaPeriodoVO.setMatriculaPeriodoMatriculaCalculadaVO(new MatriculaPeriodoMensalidadeCalculadaVO("Matricula", matriculaPeriodoVO.getValorMatriculaCheio(), 1, 1));
		matriculaPeriodoVO.getMatriculaPeriodoMatriculaCalculadaVO().setVencimentoPrimeiraParcela(dataVencimentoMatricula);
		matriculaPeriodoVO.getMatriculaPeriodoMatriculaCalculadaVO().getListaDescontosMensalidade().addAll(matriculaPeriodoVO.getListaDescontosMatricula());
		List<MatriculaPeriodoMensalidadeCalculadaVO> matriculaPeriodoMensalidadeCalculadaVOs = new ArrayList<MatriculaPeriodoMensalidadeCalculadaVO>(0);
		matriculaPeriodoMensalidadeCalculadaVOs.add(matriculaPeriodoVO.getMatriculaPeriodoMatriculaCalculadaVO());
	}

	private void realizarCalculosValoresAplicadosMensalidade(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List ordemDesconto, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception {
		realizarCalculosValoresAplicadosMensalidadeEntrada(matriculaPeriodoVO, usuario);
		Map<Integer, MatriculaPeriodoMensalidadeCalculadaVO> parcelasCalcular = new HashMap<Integer, MatriculaPeriodoMensalidadeCalculadaVO>(0);
		parcelasCalcular.put(1, new MatriculaPeriodoMensalidadeCalculadaVO("Parcelas", matriculaPeriodoVO.getValorMensalidadeCheio(), 1, matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getNrParcelasPeriodo()));
		List<PlanoDescontoVO> planoDescontoBaseVOs = matriculaVO.getPlanoFinanceiroAluno().getPlanoDescontoInstitucionalVOs();
		Ordenacao.ordenarLista(planoDescontoBaseVOs, "aplicarDescontoApartirParcela");

		for(PlanoDescontoVO planoDescontoVO : planoDescontoBaseVOs){
			if(planoDescontoVO.getAplicarDescontoApartirParcela() > 1 && planoDescontoVO.getAplicarDescontoApartirParcela() <= matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getNrParcelasPeriodo()
					&&  !parcelasCalcular.containsKey(planoDescontoVO.getAplicarDescontoApartirParcela())){
				parcelasCalcular.put(planoDescontoVO.getAplicarDescontoApartirParcela(), new MatriculaPeriodoMensalidadeCalculadaVO("Parcelas",matriculaPeriodoVO.getValorMensalidadeCheio(), planoDescontoVO.getAplicarDescontoApartirParcela(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getNrParcelasPeriodo()));
				for(Integer key:parcelasCalcular.keySet()){
					if(key < planoDescontoVO.getAplicarDescontoApartirParcela() && parcelasCalcular.get(key).getParcelaFinal() >=  planoDescontoVO.getAplicarDescontoApartirParcela()){
						parcelasCalcular.get(key).setParcelaFinal(planoDescontoVO.getAplicarDescontoApartirParcela() - 1);
					}
				}
			}
		}

		matriculaPeriodoVO.getMatriculaPeriodoMensalidadeCalculadaVOs().clear();
		for(Integer key:parcelasCalcular.keySet()){
			Integer nrMensalidade = key;
			List<PlanoDescontoVO> planoDescontoVOs = new ArrayList<PlanoDescontoVO>(0);
			if(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isGerarParcelaMaterialDidatico() && matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getTipoGeracaoMaterialDidatico().isAntesParcelaAcademico()){
				nrMensalidade = nrMensalidade + matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getQuantidadeParcelasMaterialDidatico().intValue();
			}
			if(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isUsaValorPrimeiraParcela()){
				nrMensalidade = nrMensalidade + 1;
			}
			Date dataVencimentoParcela = getFacadeFactory().getMatriculaPeriodoFacade().montarDataVencimento(nrMensalidade, matriculaPeriodoVO, usuario);
			if(dataVencimentoParcela == null){
				throw new Exception("Não foi encontrado uma data base de geração da parcela de mensalidade do aluno.");
			}
			for(PlanoDescontoVO planoDescontoVO : planoDescontoBaseVOs){
				if(planoDescontoVO.getAplicarDescontoApartirParcela() <= 1 || (planoDescontoVO.getAplicarDescontoApartirParcela() > 1
						&& planoDescontoVO.getAplicarDescontoApartirParcela() <= key)){
					planoDescontoVOs.add(planoDescontoVO);
				}
			}
			matriculaPeriodoVO.setListaDescontosMensalidade(getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(false, matriculaPeriodoVO.getValorMensalidadeCheio(), matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoParcela(), matriculaVO.getPlanoFinanceiroAluno().getPercDescontoParcela(), matriculaVO.getPlanoFinanceiroAluno().getValorDescontoParcela(), matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela(), dataVencimentoParcela, dataVencimentoParcela, ordemDesconto, matriculaVO.getPlanoFinanceiroAluno().getDescontoProgressivo(), planoDescontoVOs, matriculaVO.getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs(), 0, configuracaoFinanceiro.getUsaDescontoCompostoPlanoDesconto(), configuracaoFinanceiro, Boolean.FALSE, null, matriculaVO.getMatricula(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getAplicarCalculoComBaseDescontosCalculados(), matriculaVO.getUnidadeEnsino().getCidade().getCodigo()));
			/**
	    	 * Criado por Rodrigo Wind 30/04/15
	    	 * Adicionado esta regra abaixo pois quando no plano financeiro do aluno o desconto do aluno é valido até a data de vencimento e o mesmo possua desconto
	    	 * após o vencimento e não possua um plano de desconto até o vencimento é necessário adicionar na lista listaPlanoFinanceiroAlunoDescricaoDescontos mais um
	    	 * objeto que refere ao desconto após o vencimento sem este desconto não é apresentado no boleto bancário a linha na descrição dos desconto "Após Vencimento"
	    	 */
			if(!matriculaPeriodoVO.getListaDescontosMensalidade().isEmpty()
					&& matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela()
					&& !matriculaPeriodoVO.getListaDescontosMensalidade().get(matriculaPeriodoVO.getListaDescontosMensalidade().size()-1).getReferentePlanoFinanceiroAposVcto()
					&& matriculaPeriodoVO.getListaDescontosMensalidade().get(matriculaPeriodoVO.getListaDescontosMensalidade().size()-1).getValorDescontoAluno()>0){
				// Abaixo chamamos o método que gera a lista de descontos, de forma a garantir que gere
	            // a condição de pagamento que será aplicada para o aluno após o vencimento do boleto
	            // Esta descrição é útil quando existem descontos que vencem na data de vencimento. Ou seja,
	            // nestes casos é importante que o SEI imprima os valores a serem adotados após o pagamento
	            // do boleto.
	            Date dataReferenciaConsiderarBaixaTituloAposVcto = Uteis.getDataFutura(dataVencimentoParcela, Calendar.DAY_OF_MONTH, 1);
	            List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaComDescontoAposVcto = getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAlunoPadrao(false, matriculaPeriodoVO.getValorMensalidadeCheio(), matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoParcela(),
	            		matriculaVO.getPlanoFinanceiroAluno().getPercDescontoParcela(), matriculaVO.getPlanoFinanceiroAluno().getValorDescontoParcela(), matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela(), dataVencimentoParcela, dataVencimentoParcela, ordemDesconto,
	            		null, matriculaVO.getPlanoFinanceiroAluno().getPlanoDescontoInstitucionalVOs(), matriculaVO.getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs(), 0, configuracaoFinanceiro.getUsaDescontoCompostoPlanoDesconto(), configuracaoFinanceiro.getVencimentoDescontoProgressivoDiaUtil(), Boolean.TRUE, dataReferenciaConsiderarBaixaTituloAposVcto,
	            		matriculaVO.getMatricula(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getAplicarCalculoComBaseDescontosCalculados(), matriculaVO.getUnidadeEnsino().getCidade().getCodigo());
	            if ((listaComDescontoAposVcto != null) && (listaComDescontoAposVcto.size() > 0)) {
	            	if(!matriculaPeriodoVO.getListaDescontosMensalidade().isEmpty() && matriculaPeriodoVO.getListaDescontosMensalidade().get(matriculaPeriodoVO.getListaDescontosMensalidade().size()-1).getDiaNrAntesVencimento().equals(0)){
	            		matriculaPeriodoVO.getListaDescontosMensalidade().get(matriculaPeriodoVO.getListaDescontosMensalidade().size()-1).setReferentePlanoFinanceiroAteVencimento(true);
	            	}
	                PlanoFinanceiroAlunoDescricaoDescontosVO planoAposVcto = listaComDescontoAposVcto.get(listaComDescontoAposVcto.size() - 1);
	                planoAposVcto.setReferentePlanoFinanceiroAposVcto(Boolean.TRUE);
	                matriculaPeriodoVO.getListaDescontosMensalidade().add(planoAposVcto);
	            }
			}
				/**
			 * Responsável por percorrer a lista de
			 * PlanoFinanceiroAlunoDescricaoDescontosVO de forma a remover aqueles
			 * cujo valor do desconto seja 0.
			 */
			for (Iterator<PlanoFinanceiroAlunoDescricaoDescontosVO> iterator = matriculaPeriodoVO.getListaDescontosMensalidade().iterator(); iterator.hasNext();) {
				PlanoFinanceiroAlunoDescricaoDescontosVO obj = iterator.next();
				if(matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela() && obj.getDiaNrAntesVencimento() == 0 && obj.getReferentePlanoFinanceiroAposVcto()){
					obj.setValorDescontoAluno(0.0);
				}
				if (obj.executarCalculoValorTotalDesconto().equals(0.0) && obj.getDiaNrAntesVencimento() > 0) {
					iterator.remove();
				}
			}
			parcelasCalcular.get(key).setListaDescontosMensalidade(matriculaPeriodoVO.getListaDescontosMensalidade());
			parcelasCalcular.get(key).setVencimentoPrimeiraParcela(dataVencimentoParcela);
			matriculaPeriodoVO.getMatriculaPeriodoMensalidadeCalculadaVOs().add(parcelasCalcular.get(key));
		}
		Ordenacao.ordenarLista(matriculaPeriodoVO.getMatriculaPeriodoMensalidadeCalculadaVOs(), "parcelaInicial");
	}

	private void realizarCalculosValoresAplicadosMensalidadeEntrada(MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception {
		if(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isUsaValorPrimeiraParcela()){
			MatriculaPeriodoMensalidadeCalculadaVO mpmc = new MatriculaPeriodoMensalidadeCalculadaVO("Parcela Entrada",  matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getValorPrimeiraParcela(), 1, 1);
			matriculaPeriodoVO.getMatriculaPeriodoMensalidadeEntradaCalculadaVOs().clear();
			Integer nrMensalidade = 1;
			if(!matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isVencimentoPrimeiraParcelaAntesMaterialDidatico()
					&& matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isGerarParcelaMaterialDidatico()
					&& matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getTipoGeracaoMaterialDidatico().isAntesParcelaAcademico()){
				nrMensalidade = nrMensalidade + matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getQuantidadeParcelasMaterialDidatico().intValue();
			}
			Date dataVencimentoParcela = getFacadeFactory().getMatriculaPeriodoFacade().montarDataVencimento(nrMensalidade, matriculaPeriodoVO, usuario);
			if(dataVencimentoParcela == null){
				throw new Exception("Não foi encontrado uma data base de geração da parcela de Parcela Entrada do aluno.");
			}
			PlanoFinanceiroAlunoDescricaoDescontosVO pfadd = new PlanoFinanceiroAlunoDescricaoDescontosVO();
			pfadd.setDataLimiteAplicacaoDesconto(dataVencimentoParcela);
			pfadd.setValorBase(mpmc.getValorMensalidadeCheio());
			pfadd.setValorBaseComDescontosJaCalculadosAplicados(mpmc.getValorMensalidadeCheio());
			pfadd.setValorParaPagamentoDentroDataLimiteDesconto(mpmc.getValorMensalidadeCheio());
			pfadd.setTipoOrigemDesconto(PlanoFinanceiroAlunoDescricaoDescontosVO.TIPODESCONTOPADRAO);
			mpmc.getListaDescontosMensalidade().add(pfadd);
			mpmc.setVencimentoPrimeiraParcela(dataVencimentoParcela);
			matriculaPeriodoVO.getMatriculaPeriodoMensalidadeEntradaCalculadaVOs().add(mpmc);
		}else {
			matriculaPeriodoVO.getMatriculaPeriodoMensalidadeEntradaCalculadaVOs().clear();
		}
	}

	private void realizarCalculosValoresAplicadosMaterialDidatico(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, List ordemDesconto, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiro) throws Exception {

		if(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isGerarParcelaMaterialDidatico()){
			MatriculaPeriodoMensalidadeCalculadaVO mpmc = new MatriculaPeriodoMensalidadeCalculadaVO("Material Didático",  matriculaPeriodoVO.getValorMaterialDidaticoCheio(), 1, matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getQuantidadeParcelasMaterialDidatico().intValue());
			matriculaPeriodoVO.getMatriculaPeriodoMaterialDidaticoCalculadaVOs().clear();
			matriculaPeriodoVO.getListaDescontosMaterialDidatico().clear();
			Integer nrMensalidade = 1;
			if(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isUsaValorPrimeiraParcela()
					&& matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isVencimentoPrimeiraParcelaAntesMaterialDidatico()){
				nrMensalidade = nrMensalidade + 1;
			}
			Date dataVencimentoParcela = getFacadeFactory().getMatriculaPeriodoFacade().montarDataVencimento(nrMensalidade, matriculaPeriodoVO, usuario);
			if(dataVencimentoParcela == null){
				throw new Exception("Não foi encontrado uma data base de geração da parcela de material didático do aluno.");
			}
			if(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isControlaDiaBaseVencimentoParcelaMaterialDidatico()){
				dataVencimentoParcela = Uteis.getDataVencimentoPadrao(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getDiaBaseVencimentoParcelaOutraUnidade().intValue(), dataVencimentoParcela, 0);
			}

			if(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isAplicarDescontosParcelasNoMaterialDidatico()){
				List<PlanoDescontoVO> planoDescontoVOs = new ArrayList<PlanoDescontoVO>(0);
				List<ConvenioVO> convenioVOs = new ArrayList<ConvenioVO>();
				DescontoProgressivoVO descontoProgressivo = new DescontoProgressivoVO();
				String tipoDescontoParcela= "";
				Double percDescontoParcela=0.0;
				Double valorDescontoParcela=0.0;

				if(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isAplicarDescontoMaterialDidaticoDescontoInstitucional()){
					List<PlanoDescontoVO> planoDescontoBaseVOs = matriculaVO.getPlanoFinanceiroAluno().getPlanoDescontoInstitucionalVOs();
					Ordenacao.ordenarLista(planoDescontoBaseVOs, "aplicarDescontoApartirParcela");
					for(PlanoDescontoVO planoDescontoVO : planoDescontoBaseVOs){
						if(planoDescontoVO.getAplicarDescontoApartirParcela() <= 1 ||
								(planoDescontoVO.getAplicarDescontoApartirParcela() > 1 && planoDescontoVO.getAplicarDescontoApartirParcela() <= 1)){
							planoDescontoVOs.add(planoDescontoVO);
						}
					}
				}

				if(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isAplicarDescontoMaterialDidaticoDescontoConvenio()){
					for (ConvenioVO convenioVO : matriculaVO.getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs()) {
						if(convenioVO.getParceiro().isCusteaParcelasMaterialDidatico()){
							convenioVOs.add(convenioVO);
						}
					}
				}

				if(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isAplicarDescontoMaterialDidaticoDescontoProgressivo()){
					descontoProgressivo = matriculaVO.getPlanoFinanceiroAluno().getDescontoProgressivo();
				}

				if(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isAplicarDescontoMaterialDidaticoDescontoAluno()){
					tipoDescontoParcela = matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoParcela();
					percDescontoParcela = matriculaVO.getPlanoFinanceiroAluno().getPercDescontoParcela();
					valorDescontoParcela = matriculaVO.getPlanoFinanceiroAluno().getValorDescontoParcela();
				}

				matriculaPeriodoVO.setListaDescontosMaterialDidatico(getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(false, matriculaPeriodoVO.getValorMaterialDidaticoCheio(), tipoDescontoParcela, percDescontoParcela, valorDescontoParcela, matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela(), dataVencimentoParcela, dataVencimentoParcela, ordemDesconto, descontoProgressivo, planoDescontoVOs, convenioVOs, 0, configuracaoFinanceiro.getUsaDescontoCompostoPlanoDesconto(), configuracaoFinanceiro, Boolean.FALSE, null, matriculaVO.getMatricula(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getAplicarCalculoComBaseDescontosCalculados(), matriculaVO.getUnidadeEnsino().getCidade().getCodigo()));

				if(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isAplicarDescontosDesconsiderandosVencimento()){
					for (PlanoFinanceiroAlunoDescricaoDescontosVO pfadd : matriculaPeriodoVO.getListaDescontosMaterialDidatico()) {
						planoDescontoVOs.clear();
						descontoProgressivo = new DescontoProgressivoVO();
						tipoDescontoParcela = TipoDescontoAluno.VALOR.getValor();
						percDescontoParcela = 0.0;
						valorDescontoParcela = pfadd.getValorDescontoAluno() +  pfadd.getValorDescontoProgressivo() + pfadd.getValorDescontoInstituicao() ;
						matriculaPeriodoVO.setListaDescontosMaterialDidatico(getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAluno(false, matriculaPeriodoVO.getValorMaterialDidaticoCheio(), tipoDescontoParcela, percDescontoParcela, valorDescontoParcela, matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela(), dataVencimentoParcela, dataVencimentoParcela, ordemDesconto, descontoProgressivo, planoDescontoVOs, convenioVOs, 0, configuracaoFinanceiro.getUsaDescontoCompostoPlanoDesconto(), configuracaoFinanceiro, Boolean.FALSE, null, matriculaVO.getMatricula(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getAplicarCalculoComBaseDescontosCalculados(), matriculaVO.getUnidadeEnsino().getCidade().getCodigo()));
						break;
					}
				}


				/**
			     * Criado por Rodrigo Wind 30/04/15
			     * Adicionado esta regra abaixo pois quando no plano financeiro do aluno o desconto do aluno é valido até a data de vencimento e o mesmo possua desconto
			     * após o vencimento e não possua um plano de desconto até o vencimento é necessário adicionar na lista listaPlanoFinanceiroAlunoDescricaoDescontos mais um
			     * objeto que refere ao desconto após o vencimento sem este desconto não é apresentado no boleto bancário a linha na descrição dos desconto "Após Vencimento"
			     */
				if(!matriculaPeriodoVO.getListaDescontosMaterialDidatico().isEmpty()
						&& matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela()
						&& !matriculaPeriodoVO.getListaDescontosMaterialDidatico().get(matriculaPeriodoVO.getListaDescontosMaterialDidatico().size()-1).getReferentePlanoFinanceiroAposVcto()
						&& matriculaPeriodoVO.getListaDescontosMaterialDidatico().get(matriculaPeriodoVO.getListaDescontosMaterialDidatico().size()-1).getValorDescontoAluno()>0){
					// Abaixo chamamos o método que gera a lista de descontos, de forma a garantir que gere
			           // a condição de pagamento que será aplicada para o aluno após o vencimento do boleto
			           // Esta descrição é útil quando existem descontos que vencem na data de vencimento. Ou seja,
			           // nestes casos é importante que o SEI imprima os valores a serem adotados após o pagamento
			           // do boleto.
			           Date dataReferenciaConsiderarBaixaTituloAposVcto = Uteis.getDataFutura(dataVencimentoParcela, Calendar.DAY_OF_MONTH, 1);
			           List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaComDescontoAposVcto = getFacadeFactory().getContaReceberFacade().executarGeracaoDescontosAplicaveisPlanoFinanceiroAlunoPadrao(false, matriculaPeriodoVO.getValorMaterialDidaticoCheio(), matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoParcela(),
			           		matriculaVO.getPlanoFinanceiroAluno().getPercDescontoParcela(), matriculaVO.getPlanoFinanceiroAluno().getValorDescontoParcela(), matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela(), dataVencimentoParcela, dataVencimentoParcela, ordemDesconto,
			           		null, matriculaVO.getPlanoFinanceiroAluno().getPlanoDescontoInstitucionalVOs(), matriculaVO.getPlanoFinanceiroAluno().getPlanoFinanceiroConvenioVOs(), 0, configuracaoFinanceiro.getUsaDescontoCompostoPlanoDesconto(), configuracaoFinanceiro.getVencimentoDescontoProgressivoDiaUtil(), Boolean.TRUE, dataReferenciaConsiderarBaixaTituloAposVcto,
			           		matriculaVO.getMatricula(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getAplicarCalculoComBaseDescontosCalculados(), matriculaVO.getUnidadeEnsino().getCidade().getCodigo());
			           if ((listaComDescontoAposVcto != null) && (listaComDescontoAposVcto.size() > 0)) {
			           	if(!matriculaPeriodoVO.getListaDescontosMaterialDidatico().isEmpty() && matriculaPeriodoVO.getListaDescontosMaterialDidatico().get(matriculaPeriodoVO.getListaDescontosMaterialDidatico().size()-1).getDiaNrAntesVencimento().equals(0)){
			           		matriculaPeriodoVO.getListaDescontosMaterialDidatico().get(matriculaPeriodoVO.getListaDescontosMaterialDidatico().size()-1).setReferentePlanoFinanceiroAteVencimento(true);
			           	}
			               PlanoFinanceiroAlunoDescricaoDescontosVO planoAposVcto = listaComDescontoAposVcto.get(listaComDescontoAposVcto.size() - 1);
			               planoAposVcto.setReferentePlanoFinanceiroAposVcto(Boolean.TRUE);
			               matriculaPeriodoVO.getListaDescontosMaterialDidatico().add(planoAposVcto);
			           }
				}
				/**
				 * Responsável por percorrer a lista de
				 * PlanoFinanceiroAlunoDescricaoDescontosVO de forma a remover aqueles
				 * cujo valor do desconto seja 0.
				 */
				for (Iterator<PlanoFinanceiroAlunoDescricaoDescontosVO> iterator = matriculaPeriodoVO.getListaDescontosMaterialDidatico().iterator(); iterator.hasNext();) {
					PlanoFinanceiroAlunoDescricaoDescontosVO obj = iterator.next();
					if(matriculaVO.getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela() && obj.getDiaNrAntesVencimento() == 0 && obj.getReferentePlanoFinanceiroAposVcto()){
						obj.setValorDescontoAluno(0.0);
					}
					if (obj.executarCalculoValorTotalDesconto().equals(0.0) && obj.getDiaNrAntesVencimento() > 0) {
						iterator.remove();
					}
				}
			} else {
				PlanoFinanceiroAlunoDescricaoDescontosVO pfadd = new PlanoFinanceiroAlunoDescricaoDescontosVO();
				pfadd.setDataLimiteAplicacaoDesconto(dataVencimentoParcela);
				pfadd.setValorBase(mpmc.getValorMensalidadeCheio());
				pfadd.setValorBaseComDescontosJaCalculadosAplicados(mpmc.getValorMensalidadeCheio());
				pfadd.setValorParaPagamentoDentroDataLimiteDesconto(mpmc.getValorMensalidadeCheio());
				pfadd.setTipoOrigemDesconto(PlanoFinanceiroAlunoDescricaoDescontosVO.TIPODESCONTOPADRAO);
				matriculaPeriodoVO.getListaDescontosMaterialDidatico().add(pfadd);
			}
			mpmc.setListaDescontosMensalidade(matriculaPeriodoVO.getListaDescontosMaterialDidatico());
			mpmc.setVencimentoPrimeiraParcela(dataVencimentoParcela);
			matriculaPeriodoVO.getMatriculaPeriodoMaterialDidaticoCalculadaVOs().add(mpmc);
		}else{
			matriculaPeriodoVO.getMatriculaPeriodoMaterialDidaticoCalculadaVOs().clear();
			matriculaPeriodoVO.getListaDescontosMaterialDidatico().clear();
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarTurnoMatricula(String matricula, Integer codigoTurno) throws Exception {
		String sqlStr = "UPDATE matricula SET turno = ? WHERE matricula = ?";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { codigoTurno, matricula });
	}

	public Boolean verificarAlunoEstudouSomenteNoPrimeiroPeriodo(String matricula) throws Exception {
		String sqlStr = "SELECT COUNT(*) = 0 AS RESULTADO FROM MATRICULAPERIODO MP " + "INNER JOIN PERIODOLETIVO PL ON MP.PERIODOLETIVOMATRICULA = PL.CODIGO " + "WHERE MP.MATRICULA = ? AND PERIODOLETIVO > 1";
		try {
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { matricula });
			tabelaResultado.next();
			return tabelaResultado.getBoolean("RESULTADO");
		} finally {
			sqlStr = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarTransferenciaEntrada(MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		matriculaVO.getTransferenciaEntrada().setSituacao("EF");
		getFacadeFactory().getTransferenciaEntradaFacade().verificarTransferenciaEntrada(matriculaVO.getTransferenciaEntrada(), matriculaVO.getTransferenciaEntrada().getSituacao(), configuracaoFinanceiroVO, usuario);

		// Método que cria a frequencia de Aula para alunos que realizou a
		// transferencia interna para não perder os registros da turma anterior
		if (!matriculaVO.getTransferenciaEntrada().getCodigo().equals(0)) {
			getFacadeFactory().getRegistroAulaFacade().realizarCriacaoRegistroAulaAPartirTransferenciaEntrada(matriculaVO.getMatricula(), matriculaVO.getTransferenciaEntrada().getTransferenciaEntradaRegistroAulaFrequenciaVOs(), usuario);
		}
	}

	@Override
	public void verificarAlunoPosGraduacaoFormadoExpedicaoDiploma(MatriculaVO obj) throws Exception {
		try {
			if (obj.getCurso().getNivelEducacionalPosGraduacao()) {
				if (!obj.getSituacao().equals("FO")) {
					throw new Exception(UteisJSF.internacionalizar("msg_ExpedicaoDiploma_erroPosGraduacao"));
				}
			}
		} catch (Exception e) {
			throw e;
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirMatriculaERegistrosRelacionados(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, String motivoExclusao, ConfiguracaoFinanceiroVO confFinanVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioLogado) throws Exception {
		if (!Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada())) {
			carregarDados(matriculaPeriodoVO.getMatriculaVO(), NivelMontarDados.TODOS, usuarioLogado);
		}
		getFacadeFactory().getRequerimentoFacade().excluirRequerimentoPorMatricula(matriculaPeriodoVO.getMatriculaVO().getMatricula(), matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), confFinanVO, configuracaoGeralSistemaVO, usuarioLogado);
		getFacadeFactory().getReservaFacade().realizarExclusaoReservaPorMatricula(matriculaVO.getMatricula(), usuarioLogado);
		//getFacadeFactory().getEstagioFacade().realizarExclusaoEstagioPorMatricula(matriculaVO.getMatricula(), usuarioLogado);
		getFacadeFactory().getEmprestimoFacade().excluirEmprestimosFinalizadosPorMatricula(matriculaVO.getMatricula(), matriculaVO.getAluno().getCodigo(), usuarioLogado);
//		getFacadeFactory().getMatriculaPeriodoVencimentoFacade().excluirComBaseNaMatricula(matriculaVO.getMatricula(), confFinanVO, usuarioLogado);
//		getFacadeFactory().getControleRemessaContaReceberFacade().removerVinculoContaReceber(matriculaPeriodoVO.getCodigo(), usuarioLogado);
//		getFacadeFactory().getProcessamentoArquivoRetornoParceiroExcelFacade().removerVinculoContaReceber(matriculaPeriodoVO.getCodigo(), null, usuarioLogado);
//		getFacadeFactory().getContaReceberFacade().excluirComBaseNaMatricula(matriculaVO.getMatricula(), confFinanVO, usuarioLogado);
//		getFacadeFactory().getContaReceberFacade().excluirNossoNumeroContaReceberComBaseNaMatricula(matriculaPeriodoVO.getMatricula(), confFinanVO, usuarioLogado);
		getFacadeFactory().getHistoricoFacade().excluirComBaseNaMatricula(matriculaVO.getMatricula(), confFinanVO, usuarioLogado);
		getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().excluirPorMatricula(matriculaVO.getMatricula(), usuarioLogado);
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().excluirComBaseNaMatricula(matriculaVO.getMatricula(), confFinanVO, usuarioLogado);
		getFacadeFactory().getPlanoFinanceiroAlunoFacade().excluirComBaseNaMatricula(matriculaVO.getMatricula(), confFinanVO, usuarioLogado);
		getFacadeFactory().getHistoricoGradeFacade().excluirComBaseNaMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), confFinanVO, usuarioLogado);
		getFacadeFactory().getHistoricoGradeMigradaEquivalenteFacade().excluirComBaseNaMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), confFinanVO, usuarioLogado);
		getFacadeFactory().getMatriculaPeriodoFacade().excluirComBaseNaMatricula(matriculaVO.getMatricula(), confFinanVO, usuarioLogado);
		getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().excluirComBaseNaMatricula(matriculaVO.getMatricula(), confFinanVO, usuarioLogado);
		getFacadeFactory().getAproveitamentoDisciplinaFacade().excluirComBaseNaMatricula(matriculaPeriodoVO.getMatricula(), confFinanVO, usuarioLogado);
		getFacadeFactory().getFrequenciaAulaFacade().excluirComBaseNaMatricula(matriculaVO.getMatricula(), confFinanVO, usuarioLogado);
		getFacadeFactory().getAtividadeComplementarMatriculaFacade().excluirComBaseNaMatricula(matriculaPeriodoVO.getMatricula(), usuarioLogado);
		if(Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getCodigo())){
			matriculaPeriodoVO.getMatriculaVO().setTransferenciaEntrada(getFacadeFactory().getTransferenciaEntradaFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, confFinanVO, usuarioLogado));
			if(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.EXTERNA.getValor())){
				getFacadeFactory().getTransferenciaEntradaFacade().removerVinculoTransferenciaEntradaMatricula(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getCodigo(), false, usuarioLogado);
			}else{
				if(matriculaPeriodoVO.getMatriculaVO().getCurso().getCodigo().equals(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getCurso().getCodigo())) {			
					getFacadeFactory().getEstagioFacade().realizarAproveitamentoEstagioTransferenciaUnidadeEnsino(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getMatricula(), matriculaPeriodoVO.getMatriculaVO(), usuarioLogado);
				}
				getFacadeFactory().getTransferenciaEntradaFacade().alterarSituacao(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getCodigo(), SituacaoTransferenciaEntrada.EM_AVALIACAO.getValor());
			}
		}
		getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().excluirComBaseNaMatricula(matriculaVO.getMatricula(), usuarioLogado);
		getFacadeFactory().getImpressaoDeclaracaoFacade().excluirImpressaoDeclaracaoPorMatricula(matriculaPeriodoVO.getMatriculaVO(), configuracaoGeralSistemaVO, usuarioLogado);
		getFacadeFactory().getDocumentacaoGEDInterfaceFacade().excluirDocumentacaoGEDPorMatricula(matriculaPeriodoVO.getMatriculaVO(), false, usuarioLogado, configuracaoGeralSistemaVO);		
		excluirMatricula(matriculaVO.getMatricula(), confFinanVO, usuarioLogado);
		if(Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getCodigo()) && matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.EXTERNA.getValor())){
			getFacadeFactory().getTransferenciaEntradaFacade().excluir(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada(), false, usuarioLogado);
		}
		getFacadeFactory().getExclusaoMatriculaFacade().incluirLogExclusaoMatricula(matriculaVO, matriculaPeriodoVO, motivoExclusao, usuarioLogado);
		getFacadeFactory().getMatriculaPeriodoFacade().realizarExecucaoRegrasContaIntegracaoPorMatricula(matriculaVO, matriculaPeriodoVO, usuarioLogado, false);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPreMatriculaERegistrosRelacionados(MatriculaPeriodoVO matriculaPeriodoVO, String motivoExclusao, ConfiguracaoFinanceiroVO confFinanVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioLogado) throws Exception {
		if (getFacadeFactory().getMatriculaPeriodoFacade().consultarExistenciaMaisDeUmaMatriculaPeriodo(matriculaPeriodoVO.getMatricula())) {
			MatriculaPeriodoVO ultimaMP = getFacadeFactory().getMatriculaPeriodoFacade().consultaUltimaMatriculaPeriodoPorMatricula(matriculaPeriodoVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, confFinanVO, usuarioLogado);
			getFacadeFactory().getMatriculaPeriodoVencimentoFacade().excluirComBaseNaMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), confFinanVO, usuarioLogado);
			getFacadeFactory().getControleRemessaContaReceberFacade().removerVinculoContaReceber(matriculaPeriodoVO.getCodigo(), usuarioLogado);
			getFacadeFactory().getContaReceberFacade().removerVinculoPeriodoContasBibOut(matriculaPeriodoVO.getCodigo(), usuarioLogado);
			getFacadeFactory().getProcessamentoArquivoRetornoParceiroExcelFacade().removerVinculoContaReceber(matriculaPeriodoVO.getCodigo(), null, usuarioLogado);
			getFacadeFactory().getContaReceberFacade().excluirComBaseNaMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), confFinanVO, usuarioLogado);
			getFacadeFactory().getContaReceberFacade().excluirNossoNumeroContaReceberComBaseNaMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), confFinanVO, usuarioLogado);
			getFacadeFactory().getHistoricoFacade().excluirComBaseNaMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), confFinanVO, usuarioLogado);
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().excluirComBaseNaMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), usuarioLogado);
			getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().excluirPorMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), matriculaPeriodoVO.getAno()+"/"+matriculaPeriodoVO.getSemestre(), matriculaPeriodoVO.getMatricula(), usuarioLogado);
			getFacadeFactory().getPlanoFinanceiroAlunoFacade().excluirComBaseNaMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), confFinanVO, usuarioLogado);
			getFacadeFactory().getHistoricoGradeFacade().excluirComBaseNaMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), confFinanVO, usuarioLogado);
			getFacadeFactory().getHistoricoGradeMigradaEquivalenteFacade().excluirComBaseNaMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), confFinanVO, usuarioLogado);
			getFacadeFactory().getAproveitamentoDisciplinaFacade().excluirComBaseNaMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), confFinanVO, usuarioLogado);
			getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().removerVinculoTransacaoCartaoOnlineMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), usuarioLogado);
			getFacadeFactory().getRequerimentoFacade().excluirRequerimentoPorMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), null, confFinanVO, configuracaoGeralSistemaVO, usuarioLogado);
			getFacadeFactory().getMatriculaPeriodoFacade().excluir(matriculaPeriodoVO, usuarioLogado);
			getFacadeFactory().getExclusaoMatriculaFacade().incluirLogExclusaoMatricula(matriculaPeriodoVO.getMatriculaVO(), matriculaPeriodoVO, motivoExclusao, usuarioLogado);
			if(ultimaMP.getCodigo().equals(matriculaPeriodoVO.getCodigo())){
				ultimaMP = getFacadeFactory().getMatriculaPeriodoFacade().consultaUltimaMatriculaPeriodoPorMatricula(matriculaPeriodoVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, confFinanVO, usuarioLogado);
				if(ultimaMP.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FINALIZADA.getValor()) || ultimaMP.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.ATIVA.getValor())
						|| ultimaMP.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA.getValor()) || ultimaMP.getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA_CANCELADA.getValor())){
					if(!matriculaPeriodoVO.getMatriculaVO().getSituacao().equals("AT")){
						getFacadeFactory().getMatriculaFacade().alterarSituacaoMatricula(matriculaPeriodoVO.getMatriculaVO().getMatricula(), "AT", usuarioLogado);
					}
				} else if(!ultimaMP.getSituacaoMatriculaPeriodo().equals(matriculaPeriodoVO.getMatriculaVO().getSituacao())){
					getFacadeFactory().getMatriculaFacade().alterarSituacaoMatricula(matriculaPeriodoVO.getMatriculaVO().getMatricula(), ultimaMP.getSituacaoMatriculaPeriodo(), usuarioLogado);
				}
			}
		} else {
			if(getFacadeFactory().getEmprestimoFacade().consultaExistenciaEmprestimosEmAbertoPorMatricula(matriculaPeriodoVO.getMatriculaVO().getMatricula(), matriculaPeriodoVO.getMatriculaVO().getAluno().getCodigo())){
	        	throw new Exception("Não é possível excluir esta matrícula, pois existem emprestimos pendentes na biblioteca para o aluno "+matriculaPeriodoVO.getMatriculaVO().getAluno().getNome()+".");
			}
			if (!Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada())) {
				matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().setCodigo(0);
				carregarDados(matriculaPeriodoVO.getMatriculaVO(), NivelMontarDados.TODOS, usuarioLogado);
			}
			getFacadeFactory().getRequerimentoFacade().excluirRequerimentoPorMatricula(matriculaPeriodoVO.getMatriculaVO().getMatricula(), matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo(), confFinanVO, configuracaoGeralSistemaVO, usuarioLogado);
			//getFacadeFactory().getEstagioFacade().realizarExclusaoEstagioPorMatricula(matriculaPeriodoVO.getMatriculaVO().getMatricula(), usuarioLogado);
			getFacadeFactory().getReservaFacade().realizarExclusaoReservaPorMatricula(matriculaPeriodoVO.getMatriculaVO().getMatricula(), usuarioLogado);
			getFacadeFactory().getEmprestimoFacade().excluirEmprestimosFinalizadosPorMatricula(matriculaPeriodoVO.getMatriculaVO().getMatricula(), matriculaPeriodoVO.getMatriculaVO().getAluno().getCodigo(), usuarioLogado);
			getFacadeFactory().getMatriculaPeriodoVencimentoFacade().excluirComBaseNaMatricula(matriculaPeriodoVO.getMatricula(), confFinanVO, usuarioLogado);
			getFacadeFactory().getProcessamentoArquivoRetornoParceiroExcelFacade().removerVinculoContaReceber(matriculaPeriodoVO.getCodigo(), null, usuarioLogado);
			getFacadeFactory().getControleRemessaContaReceberFacade().removerVinculoContaReceber(matriculaPeriodoVO.getCodigo(), usuarioLogado);
			getFacadeFactory().getContaReceberFacade().excluirComBaseNaMatricula(matriculaPeriodoVO.getMatricula(), confFinanVO, usuarioLogado);
			getFacadeFactory().getContaReceberFacade().excluirNossoNumeroContaReceberComBaseNaMatricula(matriculaPeriodoVO.getMatricula(), confFinanVO, usuarioLogado);
			getFacadeFactory().getHistoricoFacade().excluirComBaseNaMatricula(matriculaPeriodoVO.getMatricula(), confFinanVO, usuarioLogado);
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().excluirComBaseNaMatricula(matriculaPeriodoVO.getMatricula(), confFinanVO, usuarioLogado);
			getFacadeFactory().getTransferenciaMatrizCurricularMatriculaFacade().excluirPorMatricula(matriculaPeriodoVO.getMatricula(), usuarioLogado);
			getFacadeFactory().getPlanoFinanceiroAlunoFacade().excluirComBaseNaMatricula(matriculaPeriodoVO.getMatricula(), confFinanVO, usuarioLogado);
			getFacadeFactory().getHistoricoGradeFacade().excluirComBaseNaMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), confFinanVO, usuarioLogado);
			getFacadeFactory().getHistoricoGradeMigradaEquivalenteFacade().excluirComBaseNaMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), confFinanVO, usuarioLogado);
			getFacadeFactory().getTransacaoCartaoOnlineInterfaceFacade().removerVinculoTransacaoCartaoOnlineMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), usuarioLogado);
			getFacadeFactory().getMatriculaPeriodoFacade().excluirComBaseNaMatricula(matriculaPeriodoVO.getMatricula(), confFinanVO, usuarioLogado);
			getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().excluirComBaseNaMatricula(matriculaPeriodoVO.getMatricula(), usuarioLogado);
			getFacadeFactory().getLiberacaoFinanceiroCancelamentoTrancamentoFacade().excluirComBaseNaMatricula(matriculaPeriodoVO.getMatricula(), confFinanVO, usuarioLogado);
			getFacadeFactory().getFrequenciaAulaFacade().excluirComBaseNaMatricula(matriculaPeriodoVO.getMatricula(), confFinanVO, usuarioLogado);
			getFacadeFactory().getAproveitamentoDisciplinaFacade().excluirComBaseNaMatricula(matriculaPeriodoVO.getMatricula(), confFinanVO, usuarioLogado);
			getFacadeFactory().getAtividadeComplementarMatriculaFacade().excluirComBaseNaMatricula(matriculaPeriodoVO.getMatricula(), usuarioLogado);
			getFacadeFactory().getContaReceberRegistroArquivoFacade().removerVinculoComMatricula(matriculaPeriodoVO.getMatricula(), usuarioLogado);
			if(Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getCodigo())){
				matriculaPeriodoVO.getMatriculaVO().setTransferenciaEntrada(getFacadeFactory().getTransferenciaEntradaFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, confFinanVO, usuarioLogado));
				if(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getTipoTransferenciaEntrada().equals(TipoTransferenciaEntrada.EXTERNA.getValor())){
					getFacadeFactory().getTransferenciaEntradaFacade().removerVinculoTransferenciaEntradaMatricula(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getCodigo(), false, usuarioLogado);
				}else{
					if(matriculaPeriodoVO.getMatriculaVO().getCurso().getCodigo().equals(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getCurso().getCodigo())) {			
						getFacadeFactory().getEstagioFacade().realizarAproveitamentoEstagioTransferenciaUnidadeEnsino(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getMatricula(), matriculaPeriodoVO.getMatriculaVO(), usuarioLogado);
					}
					getFacadeFactory().getTransferenciaEntradaFacade().alterarSituacao(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getCodigo(), SituacaoTransferenciaEntrada.EM_AVALIACAO.getValor());
				}
			}
			getFacadeFactory().getMatriculaEnadeFacade().excluirEnadeMatricula(matriculaPeriodoVO.getMatriculaVO(), usuarioLogado);
			getFacadeFactory().getImpressaoDeclaracaoFacade().excluirImpressaoDeclaracaoPorMatricula(matriculaPeriodoVO.getMatriculaVO(), configuracaoGeralSistemaVO, usuarioLogado);
			getFacadeFactory().getDocumentacaoGEDInterfaceFacade().excluirDocumentacaoGEDPorMatricula(matriculaPeriodoVO.getMatriculaVO(), false, usuarioLogado, configuracaoGeralSistemaVO);
			if(Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada())) {
				if(!Uteis.isAtributoPreenchido(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getCurso().getCodigo())) {
					matriculaPeriodoVO.getMatriculaVO().setTransferenciaEntrada(getFacadeFactory().getTransferenciaEntradaFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getMatriculaVO().getTransferenciaEntrada().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, confFinanVO, usuarioLogado));
				}
			}
			excluirMatricula(matriculaPeriodoVO.getMatricula(), confFinanVO, usuarioLogado);			
			getFacadeFactory().getExclusaoMatriculaFacade().incluirLogExclusaoMatricula(matriculaPeriodoVO.getMatriculaVO(), matriculaPeriodoVO, motivoExclusao, usuarioLogado);
			getFacadeFactory().getMatriculaPeriodoFacade().realizarExecucaoRegrasContaIntegracaoPorMatricula(matriculaPeriodoVO.getMatriculaVO(), matriculaPeriodoVO, usuarioLogado, false);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void excluirMatricula(String matricula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("DELETE FROM matricula WHERE matricula = '").append(matricula).append("' ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado));
		try {
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		} finally {
			sqlStr = null;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirLogExclusaoMatricula(final MatriculaVO obj, final String motivoExclusao, ConfiguracaoFinanceiroVO configuracaoFinanceiro, final UsuarioVO usuario) throws Exception {
		try {
			final String sql = "INSERT INTO logExclusaoMatricula( matricula, aluno, unidadeEnsino, curso, turno, dataMatricula, dataExclusao, nomeResponsavelExclusao, motivoExclusao )" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getMatricula());
					sqlInserir.setString(2, obj.getAluno().getNome());
					sqlInserir.setString(3, obj.getUnidadeEnsino().getNome());
					sqlInserir.setString(4, obj.getCurso().getNome());
					sqlInserir.setString(5, obj.getTurno().getNome());
					sqlInserir.setTimestamp(6, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlInserir.setTimestamp(7, Uteis.getDataJDBCTimestamp(new Date()));
					sqlInserir.setString(8, usuario.getNome());
					sqlInserir.setString(9, motivoExclusao);

					return sqlInserir;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirLogAlteracaoConsultorMatricula(final String matricula, final Integer responsavelAlteracao, final Integer consultorSubstituido, final Integer consultorSubstituto, final Date data, final UsuarioVO usuario) throws Exception {
		try {
			final String sql = "INSERT INTO logConsultorMatricula( matricula, responsavelAlteracao, consultorSubstituido, consultorSubstituto, data )" + "VALUES (?, ?, ?, ?, ?)";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, matricula);
					sqlInserir.setInt(2, responsavelAlteracao.intValue());
					sqlInserir.setInt(3, consultorSubstituido);
					sqlInserir.setInt(4, consultorSubstituto);
					sqlInserir.setDate(5, Uteis.getDataJDBC(data));
					return sqlInserir;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List consultarMatriculaPorCodigoPessoaParaComboBox(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct matricula.matricula, curso.nome as curso, matricula.situacao FROM Matricula ,Pessoa, curso WHERE pessoa.codigo = matricula.aluno and curso.codigo = matricula.curso and pessoa.codigo = " + pessoa.intValue() + " order by matricula";
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr = "SELECT distinct   matricula.matricula, curso.nome as curso, matricula.situacao FROM Matricula ,Pessoa, curso WHERE Matricula.aluno = Pessoa.codigo and curso.codigo = matricula.curso and pessoa.codigo = " + pessoa.intValue() + " order by matricula";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			obj.setNovoObj(false);
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.getCurso().setNome(tabelaResultado.getString("curso"));
			obj.setSituacao(tabelaResultado.getString("situacao"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}


	public List consultarAlunosPossuemParceiroDadosComboBox(List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, Integer codigoParceiro, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT DISTINCT m.matricula, pessoa.codigo, pessoa.nome FROM matricula m ");
		sqlStr.append("INNER JOIN contareceber cr ON cr.matriculaaluno = m.matricula ");
		sqlStr.append(" INNER JOIN pessoa on pessoa.codigo = m.aluno ");
		sqlStr.append(" WHERE cr.parceiro = ").append(codigoParceiro);
		if (!listaUnidadeEnsinoVOs.isEmpty()) {
			sqlStr.append(" and cr.unidadeensino in (");
			for (UnidadeEnsinoVO ue : listaUnidadeEnsinoVOs) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}
		sqlStr.append(" ORDER BY pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<MatriculaVO> listaMatriculaVOs = new ArrayList<MatriculaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			obj.setNovoObj(false);
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.getAluno().setCodigo(tabelaResultado.getInt("codigo"));
			obj.getAluno().setNome(tabelaResultado.getString("nome"));
			listaMatriculaVOs.add(obj);
		}
		return listaMatriculaVOs;
	}

	@Override
	public List<MatriculaVO> consultaRapidaBasicaPorCodigoResponsavelFinanceiro(Integer codigoResponsavelFinanceiro, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" INNER JOIN Filiacao on Filiacao.aluno = Pessoa.codigo and responsavelFinanceiro = true ");
		sqlStr.append(" WHERE (Filiacao.pais = ").append(codigoResponsavelFinanceiro).append(") ");
		sqlStr.append(" ORDER BY matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public Boolean consultarMatriculaAtivaPorRegistroAula(Integer registroAula, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct matricula.matricula from matricula  ");
		sb.append(" inner join frequenciaaula on frequenciaaula.matricula = matricula.matricula ");
		sb.append(" inner join registroaula on registroaula.codigo = frequenciaaula.registroaula ");
		sb.append(" where matricula.situacao = 'AT' ");
		sb.append(" and registroaula.codigo = ").append(registroAula).append(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void realizarAtualizarAutomaticaAlunoConcluiuDisciplinasRegulares() {
		realizarRegistroAlunoConcluiuDisciplinasRegulares();
		realizarRegistroAlunoNaoConcluiuDisciplinasRegulares();
	}

	/**
	 * Este sql marcada o campos alunoConcluiuDisciplinasRegulares como true nas
	 * matriculas seguindo as seguintes regras 1 - A matrícula deve ser ativa; 2
	 * - O campo alunoConcluiuDisciplinasRegulares deve estar false; 3 - Não
	 * deve existir nenhum histórico da matrícula com a situação cursando (CS) 4
	 * - A quantidade de disciplinas no histórico deve ser Maior ou Igual ao
	 * número de disciplinas da Grade Curricular Vinculado a Ultima Matricula
	 * Periodo
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarRegistroAlunoConcluiuDisciplinasRegulares() {
		StringBuilder sql = new StringBuilder("");

		sql.append(" update matricula set dataAlunoConcluiuDisciplinasRegulares = current_date, alunoConcluiuDisciplinasRegulares =  true where matricula in (");
		sql.append(" select distinct matricula.matricula");
		sql.append(" from matricula");
		sql.append(" inner join matriculaperiodo   on matricula.matricula = matriculaperiodo.matricula");
		sql.append(" and matriculaperiodo.codigo = (select max(mp.codigo) from matriculaperiodo mp where matricula.matricula = mp.matricula)");
		sql.append(" where matricula.situacao = 'AT' and alunoConcluiuDisciplinasRegulares = false");
		sql.append(" and matricula.matricula not in (select matricula from historico where historico.matricula = matricula.matricula and historico.situacao ='CS')");
		sql.append(" and (select count(distinct disciplina) from periodoletivo");
		sql.append(" inner join gradedisciplina on gradedisciplina.periodoletivo  = periodoletivo.codigo");
		sql.append(" where periodoletivo.gradecurricular = matriculaperiodo.gradecurricular) <=");
		sql.append(" (select count(distinct disciplina) from historico where historico.matricula = matricula.matricula)");
		sql.append(" )");
	}

	/**
	 * Este sql marcada o campos alunoConcluiuDisciplinasRegulares como false
	 * nas matriculas seguindo as seguintes regras 1 - A matrícula deve ser
	 * ativa; 2 - O campo alunoConcluiuDisciplinasRegulares deve estar true; 3 -
	 * Deve existir ao menos um histórico da matrícula com a situação cursando
	 * (CS) ou 4 - A quantidade de disciplinas no histórico deve ser MENOR ao
	 * número de disciplinas da Grade Curricular Vinculado a Ultima Matricula
	 * Periodo
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void realizarRegistroAlunoNaoConcluiuDisciplinasRegulares() {
		StringBuilder sql = new StringBuilder("");

		sql.append(" update matricula set dataAlunoConcluiuDisciplinasRegulares =  null, alunoConcluiuDisciplinasRegulares =  false where matricula in ( ");
		sql.append(" select distinct matricula.matricula");
		sql.append(" from matricula");
		sql.append(" inner join matriculaperiodo   on matricula.matricula = matriculaperiodo.matricula");
		sql.append(" and matriculaperiodo.codigo = (select max(mp.codigo) from matriculaperiodo mp where matricula.matricula = mp.matricula)");
		sql.append(" where matricula.situacao = 'AT' and alunoConcluiuDisciplinasRegulares = true");
		sql.append(" and dataAlunoConcluiuDisciplinasRegulares < (select max(dataregistro)::DATE from historico where historico.matricula = matricula.matricula and historico.situacao ='CS') ");
		sql.append(" and (matricula.matricula in (select matricula from historico where historico.matricula = matricula.matricula and historico.situacao ='CS')");
		sql.append(" or (select count(distinct disciplina) from periodoletivo");
		sql.append(" inner join gradedisciplina on gradedisciplina.periodoletivo  = periodoletivo.codigo");
		sql.append(" where periodoletivo.gradecurricular = matriculaperiodo.gradecurricular) >");
		sql.append(" (select count(distinct disciplina) from historico where historico.matricula = matricula.matricula))");
		sql.append(" )");

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void atualizarDataEnvioNotificacaoPendenciaDocumento(Set<String> matriculas) throws Exception {
		StringBuilder sqlStr = new StringBuilder("UPDATE matricula SET dataEnvioNotificacaoPendenciaDocumento = current_date WHERE matricula in ('0' ");
		for (String matricula : matriculas) {
			sqlStr.append(",  '").append(matricula).append("' ");
		}
		sqlStr.append(") ");
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}

	@Override
	public List<MatriculaVO> consultarMatriculaProspectApresentarCompromisso(Integer prospect, Date dataCompromisso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" select matricula.matricula, unidadeEnsino.nome as unidadeEnsino, curso.nome as curso, turno.nome as turno, ");
		sqlStr.append(" matricula.data, matricula.situacao ");
		sqlStr.append(" from matricula   ");
		sqlStr.append(" inner join Pessoa on Pessoa.codigo = matricula.aluno ");
		sqlStr.append(" inner join prospects on prospects.pessoa = pessoa.codigo ");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append(" inner join turno on turno.codigo = matricula.turno ");
		sqlStr.append(" inner join unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sqlStr.append(" where prospects.codigo = ").append(prospect);
		if (dataCompromisso != null) {
			sqlStr.append(" and (( matricula.data::DATE <= '").append(Uteis.getDataJDBC(dataCompromisso)).append("' ) or (matricula.data > current_date and current_date <= '").append(Uteis.getDataJDBC(dataCompromisso)).append("') ) ");
		}
		sqlStr.append(" order by matricula.data desc ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		MatriculaVO matriculaVO = null;
		while (rs.next()) {
			matriculaVO = new MatriculaVO();
			matriculaVO.setNovoObj(false);
			matriculaVO.setMatricula(rs.getString("matricula"));
			matriculaVO.getUnidadeEnsino().setNome(rs.getString("unidadeEnsino"));
			matriculaVO.getCurso().setNome(rs.getString("curso"));
			matriculaVO.getTurno().setNome(rs.getString("turno"));
			matriculaVO.setSituacao(rs.getString("situacao"));
			matriculaVO.setData(rs.getDate("data"));
			matriculaVOs.add(matriculaVO);
		}
		return matriculaVOs;
	}

	@Override
	public Map<String, List<? extends SuperVO>> consultarDadosAlteracaoCadastral(String matricula, Integer unidadeEnsino, Integer curso, Integer turma, String ano, String semestre, ColacaoGrauVO colacaoGrauVO, ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("select matricula.matricula, matricula.bloquearemissaoboletomatmenvisaoaluno, matricula.classificacaoingresso, matricula.dataProcessoSeletivo, matricula.totalPontoProcSeletivo, matricula.liberarBloqueioAlunoInadimplente, pessoa.codigo as codAluno, pessoa.nome as aluno, ");
		sql.append(" matricula.codigoInscricaoOVG, horascomplementares, adesivoinstituicaoentregue, formaingresso, matricula.unidadeensino, estado.sigla , matricula.localarmazenamentodocumentosmatricula, tipoTrabalhoConclusaoCurso, ");
		sql.append(" disciplinasprocseletivo, fezenade, dataenade, nomemonografia, notamonografia, titulacaoOrientadorMonografia, cargaHorariaMonografia, datainiciocurso, ");
		sql.append(" matricula.dataconclusaocurso, ");
		sql.append(" case when (matricula.datacolacaograu is null or matricula.datacolacaograu is not null) and colacaograu.data is not null then colacaograu.data ");
		sql.append(" else matricula.datacolacaograu end as datacolacaograu,");
		sql.append(" matricula.inscricao, anoIngresso, mesIngresso, semestreIngresso, naoApresentarCenso, notaEnem, observacaoDiploma, matricula.codigoFinanceiroMatricula, matricula.semestreAnoIngressoCenso, ");
		sql.append("  inscricao, anoIngresso, mesIngresso, semestreIngresso, naoApresentarCenso, notaEnem, observacaoDiploma, codigoFinanceiroMatricula, matricula.semestreAnoIngressoCenso, ");
		sql.append(" curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", ");
		sql.append(" enade.codigo as \"enade.codigo\", curso.periodicidade as \"curso.periodicidade\", ");
		sql.append(" enade.textoenadefeito as \"enade.textoenadefeito\", enade.textodispensaenade as \"enade.textodispensaenade\", ");
		sql.append(" enade.datapublicacaoportariadou  as \"enade.datapublicacaoportariadou\", enade.dataportaria as \"enade.dataportaria\", ");
		sql.append(" enade.portarianormativa as \"enade.portarianormativa\", enade.tituloenade as \"enade.tituloenade\",  ");
		sql.append(" matriculaControleLivroRegistroDiploma.dataEntregaRecibo as \"matriculaControleLivroRegistroDiploma.dataEntregaRecibo\", ");
		sql.append(" matriculaControleLivroRegistroDiploma.certificadorecebido as \"matriculaControleLivroRegistroDiploma.certificadorecebido\",  ");
		sql.append(" matriculaControleLivroRegistroDiploma.responsavelEntregaRecibo as \"matriculaControleLivroRegistroDiploma.responsavelEntregaRecibo\", ");
		sql.append(" matriculaControleLivroRegistroDiploma.utilizouProcuracao as \"matriculaControleLivroRegistroDiploma.utilizouProcuracao\", ");
		sql.append(" matriculaControleLivroRegistroDiploma.nomePessoaProcuracao as \"matriculaControleLivroRegistroDiploma.nomePessoaProcuracao\", ");
		sql.append(" matriculaControleLivroRegistroDiploma.cpfPessoaProcuracao as \"matriculaControleLivroRegistroDiploma.cpfPessoaProcuracao\", ");
		sql.append(" matriculaControleLivroRegistroDiploma.controleLivroFolhaRecibo as \"matriculaControleLivroRegistroDiploma.controleLivroFolhaRecibo\", ");
		sql.append(" matriculaControleLivroRegistroDiploma.matricula as \"matriculaControleLivroRegistroDiploma.matricula\", ");
		sql.append(" ControleLivroFolhaRecibo.codigo as \"ControleLivroFolhaRecibo.codigo\", ");
		sql.append(" ControleLivroFolhaRecibo.controleLivroRegistroDiploma as \"ControleLivroFolhaRecibo.controleLivroRegistroDiploma\", ");
		sql.append(" ControleLivroFolhaRecibo.matricula as \"ControleLivroFolhaRecibo.matricula\", ");
		sql.append(" ControleLivroFolhaRecibo.folhaReciboAtual as \"ControleLivroFolhaRecibo.folhaReciboAtual\", ControleLivroFolhaRecibo.via as \"ControleLivroFolhaRecibo.via\", ControleLivroFolhaRecibo.situacao as \"ControleLivroFolhaRecibo.situacao\", ");
		sql.append(" ControleLivroFolhaRecibo.numeroRegistro as \"ControleLivroFolhaRecibo.numeroRegistro\", ControleLivroFolhaRecibo.dataPublicacao as \"ControleLivroFolhaRecibo.dataPublicacao\", ControleLivroFolhaRecibo.responsavel AS \"ControleLivroFolhaRecibo.responsavel\", ");
		sql.append(" ControleLivroRegistroDiploma.codigo as \"ControleLivroRegistroDiploma.codigo\", ");
//		sql.append(" ControleLivroRegistroDiploma.unidadeEnsino as \"ControleLivroRegistroDiploma.unidadeEnsino\", ");
		sql.append(" ControleLivroRegistroDiploma.curso as \"ControleLivroRegistroDiploma.curso\", ");
		sql.append(" ControleLivroRegistroDiploma.nrLivro as \"ControleLivroRegistroDiploma.nrLivro\", ");
		sql.append(" ControleLivroRegistroDiploma.nrFolhaRecibo as \"ControleLivroRegistroDiploma.nrFolhaRecibo\", ");
		sql.append(" ControleLivroRegistroDiploma.situacaoFechadoAberto as \"ControleLivroRegistroDiploma.situacaoFechadoAberto\", ");
		sql.append(" ControleLivroRegistroDiploma.nrMaximoFolhasLivro as \"ControleLivroRegistroDiploma.nrMaximoFolhasLivro\", ");
		sql.append(" ControleLivroRegistroDiploma.tipolivroregistrodiplomaenum as \"ControleLivroRegistroDiploma.tipolivroregistrodiplomaenum\", ");
		sql.append(" matricula.autorizacaoCurso, matricula.renovacaoReconhecimento,matricula.localprocessoseletivo, matricula.orientadorMonografia, matricula.proficienciaLinguaEstrangeira, matricula.situacaoProficienciaLinguaEstrangeira, configuracaoacademico.quantidadecasasdecimaispermitiraposvirgula, configuracaoacademico.quantidadecasasdecimaispermitiraposvirgula, matricula.formacaoAcademica as \"matricula.formacaoAcademica\", ");
		sql.append(" matricula.permiteexecucaoreajustepreco as \"matricula.permiteexecucaoreajustepreco\", matricula.naoenviarmensagemcobranca as \"matricula.naoenviarmensagemcobranca\", matricula.permitirInclusaoExclusaoDisciplinasRenovacao, ");
		sql.append(" matricula.diaSemanaAula, matricula.turnoAula, ");
		sql.append(" unidadeensino.mantenedora as \"unidadeensino.mantenedora\", unidadeensino.codigo as \"unidadeensino.codigo\", matricula.financiamentoEstudantilCenso, matricula.justificativaCenso, matricula.tipoMobilidadeAcademica, matricula.mobilidadeAcademicaNacionalIESDestino, matricula.mobilidadeAcademicaInternacionalPaisDestino, matricula.informacoescensorelativoano ");
		sql.append(" from matricula  ");
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno  ");
		sql.append(" inner join curso on curso.codigo = matricula.curso ");
		sql.append(" inner join configuracaoacademico on configuracaoacademico.codigo = curso.configuracaoacademico ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sql.append(" left join cidade on cidade.codigo = unidadeensino.cidade ");
		sql.append(" left join estado on estado.codigo = cidade.estado ");
		sql.append(" left join enade on enade.codigo = matricula.enade");
		sql.append(" left join matriculaControleLivroRegistroDiploma on matriculaControleLivroRegistroDiploma.matricula = matricula.matricula  ");
		sql.append(" left join ControleLivroFolhaRecibo on case when matriculaControleLivroRegistroDiploma.ControleLivroFolhaRecibo is null then ControleLivroFolhaRecibo.matricula = matricula.matricula else ControleLivroFolhaRecibo.codigo = matriculaControleLivroRegistroDiploma.ControleLivroFolhaRecibo end  ");
		sql.append(" left join ControleLivroRegistroDiploma on ControleLivroRegistroDiploma.codigo = ControleLivroFolhaRecibo.ControleLivroRegistroDiploma  ");
		sql.append(" left join programacaoformaturaaluno on programacaoformaturaaluno.codigo = ( select pfa.codigo from programacaoformaturaaluno pfa where pfa.matricula = matricula.matricula order by case when pfa.colougrau = 'SI' then 0 when pfa.colougrau = 'NI' then 1 else 2 end, pfa.codigo desc limit 1 ) ");
		sql.append("left join colacaograu on colacaograu.codigo = programacaoformaturaaluno.colacaograu  ");
		
		if (matricula != null && !matricula.trim().isEmpty()) {
			sql.append(" where matricula.matricula = '").append(matricula).append("' ");
			if (unidadeEnsino != null && unidadeEnsino > 0) {
				sql.append(" and matricula.unidadeensino =  ").append(unidadeEnsino);
			}
		} else {
			sql.append(" where 1=1 ");
			if (unidadeEnsino != null && unidadeEnsino > 0) {
				sql.append(" and matricula.unidadeensino =  ").append(unidadeEnsino);
			}
			if (curso != null && curso > 0) {
				sql.append(" and matricula.curso =  ").append(curso);
			}
			if (turma != null && turma > 0) {
				sql.append(" and matricula.matricula in ( ");
				sql.append(" SELECT distinct matriculaperiodo.matricula from matriculaperiodo ");
				sql.append(" where turma = ").append(turma);
				if (ano != null && ano.length() == 4) {
					sql.append(" and ano = '").append(ano).append("' ");
				}
				if (semestre != null && semestre.length() == 1) {
					sql.append(" and semestre = '").append(semestre).append("' ");
				}
				sql.append(" ) ");
			}

		}
		sql.append(" order by pessoa.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		Map<String, List<? extends SuperVO>> resultado = new HashMap<String, List<? extends SuperVO>>(0);
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		List<MatriculaControleLivroRegistroDiplomaVO> matriculaControleLivroRegistroDiplomaVOs = new ArrayList<MatriculaControleLivroRegistroDiplomaVO>(0);
		MatriculaVO matriculaVO = null;
		MatriculaControleLivroRegistroDiplomaVO matriculaControleLivroRegistroDiplomaVO = null;
		while (rs.next()) {
			matriculaVO = new MatriculaVO();
			matriculaVO.setNovoObj(false);
			matriculaVO.setNaoApresentarCenso(rs.getBoolean("naoApresentarCenso"));
			matriculaVO.setMatricula(rs.getString("matricula"));
			matriculaVO.setBloquearEmissaoBoletoMatMenVisaoAluno(rs.getBoolean("bloquearemissaoboletomatmenvisaoaluno"));
			matriculaVO.getCurso().setCodigo(rs.getInt("curso.codigo"));
			matriculaVO.getCurso().setNome(rs.getString("curso.nome"));
			matriculaVO.getCurso().setPeriodicidade(rs.getString("curso.periodicidade"));
			matriculaVO.getUnidadeEnsino().setMantenedora(rs.getString("unidadeensino.mantenedora"));
			matriculaVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeensino.codigo"));
			matriculaVO.getCurso().getConfiguracaoAcademico().setQuantidadeCasasDecimaisPermitirAposVirgula(rs.getInt("quantidadecasasdecimaispermitiraposvirgula"));
			matriculaVO.setCodigoInscricaoOVG(rs.getString("codigoInscricaoOVG"));
			matriculaVO.getUnidadeEnsino().getCidade().getEstado().setSigla(rs.getString("sigla"));
			matriculaVO.setLiberarBloqueioAlunoInadimplente(rs.getBoolean("liberarBloqueioAlunoInadimplente"));
			matriculaVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeensino"));
			matriculaVO.getAluno().setCodigo(rs.getInt("codAluno"));
			matriculaVO.getAluno().setNome(rs.getString("aluno"));
			matriculaVO.setNotaEnem(rs.getDouble("notaEnem"));
			matriculaVO.setObservacaoDiploma(rs.getString("observacaoDiploma"));
			matriculaVO.setHorasComplementares(rs.getDouble("horasComplementares"));
			matriculaVO.setFormaIngresso(rs.getString("formaIngresso"));
			matriculaVO.setAdesivoInstituicaoEntregue(rs.getBoolean("adesivoInstituicaoEntregue"));
			matriculaVO.setDisciplinasProcSeletivo(rs.getString("disciplinasProcSeletivo"));
			matriculaVO.setTotalPontoProcSeletivo(rs.getDouble("totalPontoProcSeletivo"));
			matriculaVO.setDataProcessoSeletivo(rs.getDate("dataProcessoSeletivo"));
			matriculaVO.setClassificacaoIngresso(rs.getInt("classificacaoIngresso"));
			matriculaVO.setTituloMonografia(rs.getString("nomemonografia"));
			matriculaVO.setNotaMonografia(rs.getDouble("notamonografia"));
			matriculaVO.setTitulacaoOrientadorMonografia(rs.getString("titulacaoOrientadorMonografia"));
			matriculaVO.setCargaHorariaMonografia(rs.getInt("cargaHorariaMonografia"));
			matriculaVO.setDataInicioCurso(rs.getDate("datainiciocurso"));
			matriculaVO.setDataConclusaoCurso(rs.getDate("dataConclusaoCurso"));
			matriculaVO.setDataColacaoGrau(rs.getDate("dataColacaoGrau"));
			matriculaVO.getInscricao().setCodigo(rs.getInt("inscricao"));
			matriculaVO.setAnoIngresso(rs.getString("anoIngresso"));
			matriculaVO.setMesIngresso(rs.getString("mesIngresso"));
			matriculaVO.setSemestreIngresso(rs.getString("semestreIngresso"));
			matriculaVO.setDataProcessoSeletivo(rs.getDate("dataProcessoSeletivo"));
			matriculaVO.setTipoTrabalhoConclusaoCurso(rs.getString("tipoTrabalhoConclusaoCurso"));
			matriculaVO.setLocalArmazenamentoDocumentosMatricula(rs.getString("localarmazenamentodocumentosmatricula"));
			matriculaVO.setCodigoFinanceiroMatricula(rs.getString("codigoFinanceiroMatricula"));
			matriculaVO.setSemestreAnoIngressoCenso(rs.getString("semestreAnoIngressoCenso"));
			matriculaVO.getAutorizacaoCurso().setCodigo(rs.getInt("autorizacaoCurso"));
			matriculaVO.getRenovacaoReconhecimentoVO().setCodigo(rs.getInt("renovacaoReconhecimento"));
			matriculaVO.setLocalProcessoSeletivo(rs.getString("localprocessoseletivo"));
			matriculaVO.setOrientadorMonografia(rs.getString("orientadorMonografia"));
			matriculaVO.setProficienciaLinguaEstrangeira(rs.getString("proficienciaLinguaEstrangeira"));
			matriculaVO.setSituacaoProficienciaLinguaEstrangeira(rs.getString("situacaoProficienciaLinguaEstrangeira"));
			matriculaVO.getFormacaoAcademica().setCodigo(rs.getInt("matricula.formacaoAcademica"));
			matriculaVO.setPermiteExecucaoReajustePreco(rs.getBoolean("matricula.permiteexecucaoreajustepreco"));
			matriculaVO.setNaoEnviarMensagemCobranca(rs.getBoolean("matricula.naoenviarmensagemcobranca"));
			matriculaVO.setPermitirInclusaoExclusaoDisciplinasRenovacao(rs.getBoolean("permitirInclusaoExclusaoDisciplinasRenovacao"));
			if (Uteis.isAtributoPreenchido(matriculaVO.getInscricao().getCodigo())) {
				realizarMontagemDadosProcSeletivoMatricula(matriculaVO, new MatriculaPeriodoVO(), usuarioVO);
			}
			if(Uteis.isAtributoPreenchido(rs.getString("diaSemanaAula"))) {
				matriculaVO.setDiaSemanaAula(DiaSemana.valueOf(rs.getString("diaSemanaAula")));
	        }else {
	        	matriculaVO.setDiaSemanaAula(DiaSemana.NENHUM);
	        }
	        if(Uteis.isAtributoPreenchido(rs.getString("turnoAula"))) {
	        	matriculaVO.setTurnoAula(NomeTurnoCensoEnum.valueOf(rs.getString("turnoAula")));
	        }else {
	        	matriculaVO.setTurnoAula(NomeTurnoCensoEnum.NENHUM);
	        }
			if(matriculaVO.getCurso().getIntegral()){
				MatriculaPeriodoVO mp = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoAtivaPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
				matriculaVO.setDataBaseGeracaoParcelas(mp.getDataBaseGeracaoParcelas());
				matriculaVO.setDataBaseGeracaoParcelasOriginal(mp.getDataBaseGeracaoParcelas());
				matriculaVO.getMatriculaPeriodoVOs().add(mp);
			}
			matriculaVO.setFinanciamentoEstudantilCenso(rs.getString("financiamentoEstudantilCenso"));
			if (Uteis.isAtributoPreenchido(matriculaVO.getFinanciamentoEstudantilCenso())) {
				List<String> listaFinanciamentoEstudantilVOs = Stream.of(matriculaVO.getFinanciamentoEstudantilCenso().split(";")).collect(Collectors.toList());
				matriculaVO.setListaFinanciamentoEstudantilVOs(listaFinanciamentoEstudantilVOs);
			}
			if (rs.getString("justificativaCenso") != null) {
				matriculaVO.setJustificativaCensoEnum(JustificativaCensoEnum.valueOf(rs.getString("justificativaCenso")));
			}
			if (rs.getString("tipoMobilidadeAcademica") != null) {
				matriculaVO.setTipoMobilidadeAcademicaEnum(TipoMobilidadeAcademicaEnum.valueOf(rs.getString("tipoMobilidadeAcademica")));
			}
			if (matriculaVO.getTipoMobilidadeAcademicaEnum().isNacional()) {
				matriculaVO.setMobilidadeAcademicaNacionalIESDestino(rs.getString("mobilidadeAcademicaNacionalIESDestino"));
			} else if (matriculaVO.getTipoMobilidadeAcademicaEnum().isInternacional()) {
				matriculaVO.setMobilidadeAcademicaInternacionalPaisDestino(rs.getString("mobilidadeAcademicaInternacionalPaisDestino"));
			}
			matriculaVO.setInformacoesCensoRelativoAno(rs.getString("informacoescensorelativoano"));
			matriculaVOs.add(matriculaVO);

			if ((rs.getString("matriculaControleLivroRegistroDiploma.matricula") != null && !rs.getString("matriculaControleLivroRegistroDiploma.matricula").trim().isEmpty()) || (rs.getString("ControleLivroFolhaRecibo.matricula") != null && !rs.getString("ControleLivroFolhaRecibo.matricula").trim().isEmpty())) {
				matriculaControleLivroRegistroDiplomaVO = new MatriculaControleLivroRegistroDiplomaVO();
				if (rs.getString("matriculaControleLivroRegistroDiploma.matricula") != null && !rs.getString("matriculaControleLivroRegistroDiploma.matricula").trim().isEmpty()) {
					matriculaControleLivroRegistroDiplomaVO.setNovoObj(false);
				} else {
					matriculaControleLivroRegistroDiplomaVO.setNovoObj(true);
				}
				matriculaControleLivroRegistroDiplomaVO.setMatricula(matriculaVO);
				matriculaControleLivroRegistroDiplomaVO.getMatricula().getUnidadeEnsino().setMantenedora(matriculaVO.getUnidadeEnsino().getMantenedora());
				matriculaControleLivroRegistroDiplomaVO.setDataEntregaRecibo(rs.getDate("matriculaControleLivroRegistroDiploma.dataEntregaRecibo"));
				matriculaControleLivroRegistroDiplomaVO.setCertificadoRecebido(rs.getBoolean("matriculaControleLivroRegistroDiploma.certificadoRecebido"));
				matriculaControleLivroRegistroDiplomaVO.setUtilizouProcuracao(rs.getBoolean("matriculaControleLivroRegistroDiploma.utilizouProcuracao"));
				matriculaControleLivroRegistroDiplomaVO.setResponsavelEntregaRecibo(rs.getInt("matriculaControleLivroRegistroDiploma.responsavelEntregaRecibo"));
				matriculaControleLivroRegistroDiplomaVO.setNomePessoaProcuracao(rs.getString("matriculaControleLivroRegistroDiploma.nomePessoaProcuracao"));
				matriculaControleLivroRegistroDiplomaVO.setCpfPessoaProcuracao(rs.getString("matriculaControleLivroRegistroDiploma.cpfPessoaProcuracao"));

				if (rs.getString("ControleLivroFolhaRecibo.matricula") != null && !rs.getString("ControleLivroFolhaRecibo.matricula").trim().isEmpty()) {
					matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().setNovoObj(false);
				} else {
					matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().setNovoObj(true);
				}
				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().setMatricula(matriculaVO);
				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().setCodigo(rs.getInt("ControleLivroFolhaRecibo.codigo"));
				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().setFolhaReciboAtual(rs.getInt("ControleLivroFolhaRecibo.folhaReciboAtual"));
				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().setNumeroRegistro(rs.getInt("ControleLivroFolhaRecibo.numeroRegistro"));
				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().setDataPublicacao(rs.getDate("ControleLivroFolhaRecibo.dataPublicacao"));
				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().setVia(rs.getString("ControleLivroFolhaRecibo.via"));
				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().setSituacao(rs.getString("ControleLivroFolhaRecibo.situacao"));
				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().getResponsavel().setCodigo(rs.getInt("ControleLivroFolhaRecibo.responsavel"));
				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().getControleLivroRegistroDiploma().setCodigo(rs.getInt("ControleLivroFolhaRecibo.controleLivroRegistroDiploma"));
//				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().getControleLivroRegistroDiploma().getUnidadeEnsino().setCodigo(rs.getInt("ControleLivroRegistroDiploma.unidadeEnsino"));
				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().getControleLivroRegistroDiploma().getCurso().setCodigo(rs.getInt("ControleLivroRegistroDiploma.curso"));
				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().getControleLivroRegistroDiploma().setNrLivro(rs.getInt("ControleLivroRegistroDiploma.nrLivro"));
				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().getControleLivroRegistroDiploma().setNrFolhaRecibo(rs.getInt("ControleLivroRegistroDiploma.nrFolhaRecibo"));
				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().getControleLivroRegistroDiploma().setNrMaximoFolhasLivro(rs.getInt("ControleLivroRegistroDiploma.nrMaximoFolhasLivro"));
				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().getControleLivroRegistroDiploma().setSituacaoFechadoAberto(rs.getString("ControleLivroRegistroDiploma.situacaoFechadoAberto"));
				matriculaControleLivroRegistroDiplomaVO.getControleLivroFolhaRecibo().getControleLivroRegistroDiploma().setTipoLivroRegistroDiplomaEnum(TipoLivroRegistroDiplomaEnum.valueOf(rs.getString("ControleLivroRegistroDiploma.tipolivroregistrodiplomaenum")));
				matriculaControleLivroRegistroDiplomaVO.getMatricula().getUnidadeEnsino().setMantenedora(rs.getString("unidadeensino.mantenedora"));
				matriculaControleLivroRegistroDiplomaVOs.add(matriculaControleLivroRegistroDiplomaVO);
			}

		}
		resultado.put("MATRICULA", matriculaVOs);
		resultado.put("MATRICULA_CONTROLE", matriculaControleLivroRegistroDiplomaVOs);
		return resultado;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarInformacoesCadastrais(List<MatriculaVO> matriculaVOs, List<MatriculaControleLivroRegistroDiplomaVO> matriculaControleLivroRegistroDiplomaVOs, UsuarioVO usuarioVO) throws Exception {
		validarDadosCensoMatricula(matriculaVOs);
		getFacadeFactory().getMatriculaFacade().alterarEnade(matriculaVOs);
		getFacadeFactory().getMatriculaControleLivroRegistroDiplomaFacade().gravarMatriculaRegistroLivro(matriculaControleLivroRegistroDiplomaVOs, usuarioVO);
		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO, null);
		for (MatriculaVO matriculaVO : matriculaVOs) {
			if (Uteis.isAtributoPreenchido(matriculaVO.getCodigoFinanceiroMatricula()) && configuracaoFinanceiroVO.getUtilizarIntegracaoFinanceira()) {
				matriculaVO.setCodigoFinanceiroMatricula(Uteis.removerZeroEsquerda(matriculaVO.getCodigoFinanceiroMatricula()));
			}
			if (configuracaoFinanceiroVO.getUtilizarIntegracaoFinanceira() && getFacadeFactory().getMatriculaFacade().consultarExistenciaCodigoFinanceiroMatricula(matriculaVO.getCodigoFinanceiroMatricula(), matriculaVO.getMatricula())) {
				throw new Exception("O Código de Integração Financeiro Matrícula (" + matriculaVO.getCodigoFinanceiroMatricula() + ") Já Está Vinculado a Matrícula (" + matriculaVO.getMatricula() + ").");
			}
			alterarDadosAlteracoesCadastraisMatricula(matriculaVO, usuarioVO);
			alterarLiberarBloqueioAlunoInadimplente(matriculaVO);
			if (Uteis.isAtributoPreenchido(matriculaVO.getDataBaseGeracaoParcelas()) && matriculaVO.getCurso().getIntegral()) {

				if (Uteis.isAtributoPreenchido(matriculaVO.getDataBaseGeracaoParcelasOriginal())) {
					if (Uteis.compararDatasSemConsiderarHoraMinutoSegundo(matriculaVO.getDataBaseGeracaoParcelas(), matriculaVO.getDataBaseGeracaoParcelasOriginal())){
						continue;
					}
				}
				realizarAtualizacaoFinanceiroDeAcordoDataBaseGeracaoParcela(matriculaVO, usuarioVO);
				matriculaVO.setDataBaseGeracaoParcelasOriginal(matriculaVO.getDataBaseGeracaoParcelas());
			} else if (!Uteis.isAtributoPreenchido(matriculaVO.getDataBaseGeracaoParcelas()) && matriculaVO.getCurso().getIntegral()) {
				if (Uteis.isAtributoPreenchido(matriculaVO.getDataBaseGeracaoParcelasOriginal())) {
					realizarAtualizacaoFinanceiroDeAcordoDataBaseGeracaoParcela(matriculaVO, usuarioVO);
					matriculaVO.setDataBaseGeracaoParcelasOriginal(matriculaVO.getDataBaseGeracaoParcelas());
				}
			}
		}
	}

	public void realizarAtualizacaoFinanceiroDeAcordoDataBaseGeracaoParcela(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getMatriculaPeriodoFacade().atualizarDataBaseGeracaoParcelaMatriculaPeriodo(matriculaVO.getUltimoMatriculaPeriodoVO().getCodigo(), matriculaVO.getDataBaseGeracaoParcelas());
		if (!getFacadeFactory().getMatriculaPeriodoVencimentoFacade().consultarExisteMatriculaPeriodoVencimentoGeradaPagaOuNegociada(matriculaVO.getUltimoMatriculaPeriodoVO().getCodigo(), false, usuarioVO)) {
			MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(matriculaVO.getUltimoMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO), usuarioVO);
			for (MatriculaPeriodoVencimentoVO matriculaPeriodoVencimentoVO : matriculaPeriodoVO.getMatriculaPeriodoVencimentoVOs()) {
				Integer nrMensalidade = 0;
				if (!matriculaPeriodoVencimentoVO.getParcela().contains("EX")) {
					if (matriculaPeriodoVencimentoVO.getParcela().contains("/")) {
						nrMensalidade = Integer.valueOf(matriculaPeriodoVencimentoVO.getParcela().substring(0, matriculaPeriodoVencimentoVO.getParcela().indexOf("/")));
						matriculaPeriodoVencimentoVO.setDataVencimento(getFacadeFactory().getMatriculaPeriodoFacade().montarDataVencimento(nrMensalidade, matriculaPeriodoVO, usuarioVO));
						matriculaPeriodoVencimentoVO.setDataCompetencia(getFacadeFactory().getMatriculaPeriodoFacade().montarDataCompetencia(nrMensalidade, matriculaPeriodoVencimentoVO.getDataVencimento(), matriculaPeriodoVO, usuarioVO));
						getFacadeFactory().getMatriculaPeriodoVencimentoFacade().atualizarDataVencimentoEDataCompetencia(matriculaPeriodoVencimentoVO, usuarioVO);
					}
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarVerificacaoELiberacaoSuspensaoMatricula(String matricula) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" (select distinct matricula.matricula, 'DOCUMENTACAO' as tipo from matricula ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sql.append(" INNER JOIN configuracoes on   configuracoes.codigo = unidadeensino.configuracoes ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" and matriculaperiodo.codigo  = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaoMatriculaPeriodo <> 'PC' ");
		sql.append(" order by (mp.ano ||'/' || mp.semestre) desc , case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1)  ");
		sql.append(" where matricula.matricula = '").append(matricula).append("' ");
		sql.append(" and ((configuracoes.controlarSuspensaoMatriculaPendenciaDocumentos  ");
		sql.append(" and nrDiasSuspenderMatriculaPendenciaDocumentos is not null and nrDiasSuspenderMatriculaPendenciaDocumentos>0 ");
		sql.append(" and matricula.matricula in (select dm.matricula from documetacaomatricula dm where matricula.matricula = dm.matricula ");
		sql.append(" and dm.entregue = false and dm.gerarsuspensaomatricula = true limit 1))  ");
		sql.append("  ) limit 1) ");
		sql.append(" union all  ");

		sql.append(" (select distinct matricula.matricula, 'CONTA_RECEBER' as tipo from contareceber ");
		sql.append(" INNER JOIN matricula ON matricula.matricula = matriculaaluno ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sql.append(" INNER JOIN configuracoes on   configuracoes.codigo = unidadeensino.configuracoes ");
		sql.append(" INNER JOIN configuracaofinanceiro ON configuracaofinanceiro.configuracoes = configuracoes.codigo ");
		sql.append(" WHERE contareceber.situacao = 'AR' and matricula.matricula = '").append(matricula).append("' ");
		sql.append(" AND ((configuracaofinanceiro.numerodiasbloquearacessoalunoinadimplente IS NOT NULL AND numerodiasbloquearacessoalunoinadimplente > 0 ");
		sql.append(" AND contareceber.datavencimento < current_date AND DATE_PART('day', current_date - datavencimento) >= numerodiasbloquearacessoalunoinadimplente)  ");
		sql.append(" or ( ");
		sql.append(" configuracaofinanceiro.quantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente IS NOT NULL ");
		sql.append(" AND quantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente > 0 ");
		sql.append(" AND contareceber.datavencimento < current_date AND DATE_PART('day', current_date - datavencimento) >= quantidadeDiasEnviarPrimeiraMensagemCobrancaInadimplente ");
		sql.append(" ) ");
		sql.append(" ) ");
		sql.append(" and (");
		sql.append("   (configuracaoFinanceiro.tipoOrigemMatriculaRotinaInadimplencia         and contareceber.tipoorigem = 'MAT') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemBibliotecaRotinaInadimplencia        and contareceber.tipoorigem = 'BIB') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemMensalidadeRotinaInadimplencia       and contareceber.tipoorigem = 'MEN') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemDevolucaoChequeRotinaInadimplencia   and contareceber.tipoorigem = 'DCH') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemNegociacaoRotinaInadimplencia        and contareceber.tipoorigem = 'NCR') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemContratoReceitaRotinaInadimplencia   and contareceber.tipoorigem = 'CTR') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemOutrosRotinaInadimplencia            and contareceber.tipoorigem = 'OUT') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemMaterialDidaticoRotinaInadimplencia  and contareceber.tipoorigem = 'MDI') ");
		sql.append(" or(configuracaoFinanceiro.tipoOrigemInclusaoReposicaoRotinaInadimplencia and contareceber.tipoorigem = 'IRE') ");
		sql.append(") ");
		sql.append(" AND (matricula.liberarbloqueioalunoinadimplente IS NULL OR matricula.liberarbloqueioalunoinadimplente = false) limit 1)  ");

		//sql.append(" AND matricula.situacao IN ('AT', 'CO')  ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		boolean existeInadimplencia = false;
		boolean existePendeciaDocumentacao = false;
		while (rs.next()) {
			if(rs.getString("tipo").equals("CONTA_RECEBER")){
				existeInadimplencia = true;
			}
			if(rs.getString("tipo").equals("DOCUMENTACAO")){
				existePendeciaDocumentacao = true;
			}
		}
		StringBuilder sqlUpdate;
		if (!existeInadimplencia && existePendeciaDocumentacao) {
			sqlUpdate = new StringBuilder();
			sqlUpdate.append("UPDATE matricula SET dataPrimeiraNotInadimplencia = null, dataSegundaNotInadimplencia = null, dataTerceiraNotInadimplencia = null, dataquartanotinadimplencia = null ");
			sqlUpdate.append("WHERE matricula = '").append(matricula).append("' and (dataPrimeiraNotInadimplencia is not null or dataSegundaNotInadimplencia is not null or dataTerceiraNotInadimplencia is not null or  dataquartanotinadimplencia is not null)");
			getConexao().getJdbcTemplate().update(sqlUpdate.toString());
		}else if (existeInadimplencia && !existePendeciaDocumentacao) {
			sqlUpdate = new StringBuilder();
			sqlUpdate.append("UPDATE matricula SET dataenvionotificacao1 = null, dataenvionotificacao2 = null, dataenvionotificacao3 = null, dataenvionotificacao4 = null, dataEnvioNotificacaoPendenciaDocumento = null ");
			sqlUpdate.append("WHERE matricula = '").append(matricula).append("' AND (dataenvionotificacao1 is not null or dataenvionotificacao2 is not null or dataenvionotificacao3 is not null or dataenvionotificacao4 is not null or dataEnvioNotificacaoPendenciaDocumento is not null)");
			getConexao().getJdbcTemplate().update(sqlUpdate.toString());
		} else if (!existeInadimplencia && !existePendeciaDocumentacao) {
			sqlUpdate = new StringBuilder();
			sqlUpdate.append("UPDATE matricula SET matriculasuspensa = false, dataenvionotificacao1 = null, dataenvionotificacao2 = null, dataenvionotificacao3 = null, dataenvionotificacao4 = null, dataEnvioNotificacaoPendenciaDocumento = null, ");
			sqlUpdate.append("dataPrimeiraNotInadimplencia = null, dataSegundaNotInadimplencia = null, dataTerceiraNotInadimplencia = null, dataquartanotinadimplencia = null ");
			sqlUpdate.append("WHERE matricula = '").append(matricula).append("' and (dataPrimeiraNotInadimplencia is not null or dataSegundaNotInadimplencia is not null or dataTerceiraNotInadimplencia is not null or  dataquartanotinadimplencia is not null or dataenvionotificacao1 is not null or dataenvionotificacao2 is not null or dataenvionotificacao3 is not null or dataenvionotificacao4 is not null or dataEnvioNotificacaoPendenciaDocumento is not null or matriculasuspensa =  true) ");
			getConexao().getJdbcTemplate().update(sqlUpdate.toString());
		}
	}

//	public String getFiltroSituacaoAcademica(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String campo) {
//		StringBuilder sqlStr = new StringBuilder();
//		campo = campo.trim();
//		sqlStr.append(" ").append(campo).append(".situacaomatriculaperiodo in ('FI', 'FO'");
//		if (filtroRelatorioAcademicoVO.getAtivo()) {
//			sqlStr.append(", 'AT'");
//		}
//		if (filtroRelatorioAcademicoVO.getPreMatricula()) {
//			sqlStr.append(", 'PR'");
//		}
//		if (filtroRelatorioAcademicoVO.getPreMatriculaCancelada()) {
//			sqlStr.append(", 'PC'");
//		}
//		if (filtroRelatorioAcademicoVO.getTrancado()) {
//			sqlStr.append(", 'TR'");
//		}
//		if (filtroRelatorioAcademicoVO.getTransferenciaExterna()) {
//			sqlStr.append(", 'TS'");
//		}
//		if (filtroRelatorioAcademicoVO.getTransferenciaInterna()) {
//			sqlStr.append(", 'TI'");
//		}
//		if (filtroRelatorioAcademicoVO.getAbandonado()) {
//			sqlStr.append(", 'AC'");
//		}
//		if (filtroRelatorioAcademicoVO.getCancelado()) {
//			sqlStr.append(", 'CA'");
//		}
//		sqlStr.append(") ");
//		return sqlStr.toString();
//	}

	@Override
	public Boolean validarMatriculaAlunoPosGraduacao(Integer codigoPessoa, Integer codigoUnidadeEnsino) throws Exception {
		try {
			Boolean bloquearMatriculaPosSemGraduacao = Boolean.FALSE;
			StringBuilder sql = new StringBuilder();
			sql.append(" select distinct configuracaogeralsistema.bloquearmatriculapossemgraduacao from unidadeensino ");
			sql.append(" inner join configuracoes on unidadeensino.configuracoes = configuracoes.codigo");
			sql.append(" inner join configuracaogeralsistema on configuracoes.codigo = configuracaogeralsistema.configuracoes");
			sql.append(" where  unidadeensino.codigo = ");
			sql.append(codigoUnidadeEnsino);
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				bloquearMatriculaPosSemGraduacao = tabelaResultado.getBoolean("bloquearmatriculapossemgraduacao");
			}
			if (bloquearMatriculaPosSemGraduacao) {
				StringBuilder sqlGraduacao = new StringBuilder();
				sqlGraduacao.append("select distinct escolaridade from formacaoacademica where pessoa = ");
				sqlGraduacao.append(codigoPessoa);
				sqlGraduacao.append(" and escolaridade = 'GR'");
				SqlRowSet tabelaResultadoGraduacao = getConexao().getJdbcTemplate().queryForRowSet(sqlGraduacao.toString());
				if (tabelaResultadoGraduacao.next()) {
					return Boolean.TRUE;
				} else {
					return Boolean.FALSE;
				}

			} else {
				return Boolean.TRUE;
			}
		} catch (Exception e) {
			throw e;
		}

	}

	public Integer consultaQuantidadeAlunoPorUnidadeEnsinoCursoESituacaoMatricula(Integer unidadeEnsino, Integer curso, String situacaoMatricula, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(matricula.matricula) AS qtde ");
		sb.append(" from matricula ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" where 1=1 ");
		if (!unidadeEnsino.equals(0)) {
			sb.append(" and unidadeEnsino = ").append(unidadeEnsino);
		}
		if (!curso.equals(0)) {
			sb.append(" and matricula.curso = ").append(curso);
		}
		sb.append(" and matricula.situacao = '").append(situacaoMatricula).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<MatriculaVO> consultaRapidaPorUnidadeEnsinoCursoESituacaoMatricula(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, String situacaoMatricula, Integer ano, UsuarioVO usuarioVO) throws InvalidResultSetAccessException, Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select matricula.matricula, matricula.gradecurricularAtual, matricula.anoIngresso, matricula.semestreIngresso, curso.nome AS \"curso.nome\", turma.identificadorturma AS \"turma.identificadorturma\", matriculaperiodo.situacaomatriculaperiodo AS \"matriculaperiodo.situacaomatriculaperiodo\", ");
		sb.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", pessoa.cpf AS \"pessoa.cpf\", pessoa.rg AS \"pessoa.rg\", pessoa.endereco AS \"pessoa.endereco\", pessoa.endereco AS \"pessoa.endereco\", pessoa.orgaoemissor AS \"pessoa.orgaoemissor\", pessoa.email AS \"pessoa.email\", pessoa.setor AS \"pessoa.setor\", pessoa.cep AS \"pessoa.cep\", cidade.nome AS \"cidade.nome\", pessoa.celular as \"pessoa.celular\",  ");
		sb.append(" gradeCurricular.codigo AS \"gradeCurricular.codigo\", gradeCurricular.nome AS \"gradeCurricular.nome\", gradeCurricular.cargaHoraria AS \"gradeCurricular.cargaHoraria\", gradeCurricular.TotalCargaHorariaAtividadeComplementar AS \"gradeCurricular.TotalCargaHorariaAtividadeComplementar\", ");
		sb.append(" matricula.turno AS \"matricula.turno\", (select fa.codigo as codigoFormacaoAcademica from formacaoacademica fa where fa.pessoa = matricula.aluno and fa.escolaridade = 'EM' order by fa.codigo desc limit 1), periodoletivo.codigo \"periodoletivo.codigo\", periodoletivo.descricao \"periodoletivo.descricao\", periodoletivo.periodoletivo \"periodoletivo.periodoletivo\", matriculaperiodo.ano as \"matriculaperiodo.ano\", matriculaperiodo.semestre AS \"matriculaperiodo.semestre\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", unidadeEnsino.codigoies AS \"unidadeEnsino.codigoies\", unidadeEnsino.cidade AS \"unidadeEnsino.cidade\" ");
		sb.append(" from matricula ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" inner join curso on curso.codigo = matricula.curso ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' and mp.ano = '").append(ano).append("'");
		sb.append("order by (mp.ano || '/' || mp.semestre) desc,  mp.codigo desc limit 1) ");
		sb.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		sb.append(" inner join gradeCurricular on gradeCurricular.codigo = matricula.gradeCurricularAtual ");
		sb.append(" left join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
		sb.append(" left join cidade on cidade.codigo = pessoa.cidade ");
		sb.append(" inner join unidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino ");
		sb.append(" where matricula.datacolacaograu is null ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs) && unidadeEnsinoVOs.stream().anyMatch(ue -> ue.getFiltrarUnidadeEnsino())) {
			sb.append(" AND matricula.unidadeensino IN (");
			for (UnidadeEnsinoVO ue : unidadeEnsinoVOs) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sb.append(ue.getCodigo()).append(", ");
				}
			}
			sb.append("0) ");
		}
		if (!curso.equals(0)) {
			sb.append(" and matricula.curso = ").append(curso);
		}
		sb.append(" and matricula.situacao = '").append(situacaoMatricula).append("' ");
		if(Uteis.isAtributoPreenchido(ano)) {
			sb.append(" and matriculaperiodo.ano = '").append(ano).append("' ");
		}
		sb.append(" order by matricula.unidadeensino, matricula.curso, matricula.turno, pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<MatriculaVO> listaMatriculaVOs = new ArrayList<MatriculaVO>(0);
		Map<Integer, TurnoVO> mapTurnoVO = new HashMap<>();
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			obj.setNovoObj(false);
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.setAnoIngresso(tabelaResultado.getString("anoIngresso"));
			obj.setSemestreIngresso(tabelaResultado.getString("semestreIngresso"));
			obj.getGradeCurricularAtual().setCodigo(tabelaResultado.getInt("gradecurricularAtual"));
			obj.getAluno().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getAluno().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.getAluno().setCPF(tabelaResultado.getString("pessoa.cpf"));
			obj.getAluno().setRG(tabelaResultado.getString("pessoa.rg"));
			obj.getAluno().setEndereco(tabelaResultado.getString("pessoa.endereco"));
			obj.getAluno().setCEP(tabelaResultado.getString("pessoa.cep"));
			obj.getAluno().setEmail(tabelaResultado.getString("pessoa.email"));
			obj.getGradeCurricularAtual().setCodigo(tabelaResultado.getInt("gradeCurricular.codigo"));
			obj.getGradeCurricularAtual().setNome(tabelaResultado.getString("gradeCurricular.nome"));
			obj.getGradeCurricularAtual().setCargaHoraria(tabelaResultado.getInt("gradeCurricular.cargaHoraria"));
			obj.getGradeCurricularAtual().setTotalCargaHorariaAtividadeComplementar(tabelaResultado.getInt("gradeCurricular.TotalCargaHorariaAtividadeComplementar"));
			obj.getCurso().setNome(tabelaResultado.getString("curso.nome"));
			obj.setTurma(tabelaResultado.getString("turma.identificadorturma"));
			obj.getAluno().getCidade().setNome(tabelaResultado.getString("cidade.nome"));
			obj.getAluno().setCelular(tabelaResultado.getString("pessoa.celular"));
			obj.getUltimoMatriculaPeriodoVO().setSituacaoMatriculaPeriodo(tabelaResultado.getString("matriculaperiodo.situacaomatriculaperiodo"));
			obj.getUltimoMatriculaPeriodoVO().getPeriodoLetivo().setCodigo(tabelaResultado.getInt("periodoletivo.codigo"));
			obj.getUltimoMatriculaPeriodoVO().getPeriodoLetivo().setDescricao(tabelaResultado.getString("periodoletivo.descricao"));
			obj.getUltimoMatriculaPeriodoVO().getPeriodoLetivo().setPeriodoLetivo(tabelaResultado.getInt("periodoletivo.periodoletivo"));
			obj.getMatriculaPeriodoVO().setSemestre(tabelaResultado.getString("matriculaperiodo.semestre"));
			obj.getMatriculaPeriodoVO().setAno(tabelaResultado.getString("matriculaperiodo.ano"));
			obj.getTurno().setCodigo(tabelaResultado.getInt("matricula.turno"));
			if (Uteis.isAtributoPreenchido(obj.getTurno().getCodigo())) {
				if(mapTurnoVO.containsKey(obj.getTurno().getCodigo())) {
					obj.setTurno(mapTurnoVO.get(obj.getTurno().getCodigo()));
				} else {
					obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("matricula.turno"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
					mapTurnoVO.put(obj.getTurno().getCodigo(), obj.getTurno());
				}
			}
			if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("codigoFormacaoAcademica"))) {
				obj.setFormacaoAcademica(getFacadeFactory().getFormacaoAcademicaFacade().consultarPorChavePrimaria(tabelaResultado.getInt("codigoFormacaoAcademica") , usuarioVO));
			}
			obj.getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeEnsino.nome"));
			obj.getUnidadeEnsino().setCodigoIES(tabelaResultado.getInt("unidadeEnsino.codigoies"));
//			obj.getUnidadeEnsino().getCidade().setCodigoIBGE(tabelaResultado.getInt("cidade.cidade"));
			listaMatriculaVOs.add(obj);
		}
		return listaMatriculaVOs;
	}
	
	@Override
	public Boolean consultarAlunoPendenciaEnadePorMatricula(Integer pessoa, String matricula) throws Exception {
		StringBuilder sql = new StringBuilder("select matriculaenade.codigo from matriculaenade where matricula = ? limit 1");		
		return !getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula).next();
	}

	@Override
	public MatriculaVO consultaRapidaPorCodigoPessoaUnicaMatricula(Integer pessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		MatriculaVO obj = new MatriculaVO();
		sqlStr.append(" WHERE (Pessoa.codigo = ").append(pessoa).append(") ");
		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sqlStr.append(" and matricula.situacao <> 'CA' and matricula.situacao <> 'TR' ");
		sqlStr.append(" ORDER BY matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			montarDadosBasico(obj, tabelaResultado);
		}
		return obj;
	}

	@Override
	public Boolean consultarAlunoSerasa(Integer codigoPessoa) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select matriculaserasa from matricula ");
			sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
			sql.append(" where pessoa.codigO = ");
			sql.append(codigoPessoa);
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getBoolean("matriculaserasa");
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public String consultarUltimaMatriculaAtivaPorContaReceber(Integer contaReceber) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select matricula  from matricula ");
			sql.append(" inner join contareceber on  contareceber.matriculaaluno = matricula.matricula ");
			sql.append(" where  contareceber.codigo = ").append(contaReceber);
			sql.append(" order by case matricula.situacao when 'AT' ");
			sql.append(" then 1 when 'PR'  then 2 else 3 end, matricula.data desc ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getString("matricula");
			}
			return "";
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public String consultarUltimaMatriculaAtivaPorCodigoPessoa(Integer codigoPessoa) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select matricula ");
			sql.append("  from matricula where aluno  =");
			sql.append(codigoPessoa);
			sql.append(" order by case situacao when 'AT' ");
			sql.append(" then 1 when 'PR'  then 2 else 3 end, data desc ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getString("matricula");
			}
			return "";
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public MatriculaVO consultarMatriculaMaiorNivelEducacionalMaiorDataPorCodigoPessoa(Integer pessoa, UsuarioVO usuario) throws Exception  {
		StringBuilder str = new StringBuilder();
		str.append("SELECT matricula.matricula, ");
		str.append(" unidadeensino.codigo as \"unidadeensino.codigo\", ");
		str.append(" unidadeensino.nome as \"unidadeensino.nome\" ");
		str.append(" FROM matricula ");
		str.append(" inner join curso on curso.codigo = matricula.curso ");
		str.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		str.append(" where matricula.aluno = ? ");
		str.append(" and matricula.situacao in('AT','PR') ");
		str.append(" order by ");
		str.append(" matricula.data desc , ");
		str.append(" (case when curso.niveleducacional = '").append(TipoNivelEducacional.MESTRADO.getValor()).append("' then  9 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.POS_GRADUACAO.getValor()).append("' then  8 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.SUPERIOR.getValor()).append("' then 7 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.GRADUACAO_TECNOLOGICA.getValor()).append("' then  6 ");
		str.append("       when curso.niveleducacional = '").append(TipoNivelEducacional.MEDIO.getValor()).append("'then 5 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.BASICO.getValor()).append("'then 4 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.INFANTIL.getValor()).append("' then 3 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.EXTENSAO.getValor()).append("' then 2 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.SEQUENCIAL.getValor()).append("' then 1 ");
		str.append(" 	   when curso.niveleducacional = '").append(TipoNivelEducacional.PROFISSIONALIZANTE.getValor()).append("' then  0 else -1 end ) desc ");
		str.append(" limit 1 ");
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(str.toString(), pessoa);
		MatriculaVO obj = new MatriculaVO();
		if (dadosSQL.next()) {
			obj.setNovoObj(false);
			obj.setMatricula(dadosSQL.getString("matricula"));
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino.codigo"));
			obj.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeensino.nome"));
		}
		return obj;
	}

	@Override
	public Date consultarDataInicioTurmaAgrupadaPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select min(dataMinima) as dataMinima from ((");
		sqlStr.append("select min(horarioturmadia.data) as dataMinima from matricula ");
		sqlStr.append("inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append(" and matriculaperiodo.codigo  = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaoMatriculaPeriodo <> 'PC' ");
		sqlStr.append(" order by (mp.ano ||'/' || mp.semestre) desc , case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1)  ");
		sqlStr.append("inner join turma on  matriculaperiodo.turma = turma.codigo ");
		sqlStr.append("INNER JOIN horarioturma ON horarioturma.turma = turma.codigo AND horarioturma.anovigente = matriculaPeriodo.ano AND horarioturma.semestrevigente = matriculaPeriodo.semestre ");
		sqlStr.append("INNER JOIN horarioturmadia ON horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append("where matricula.matricula = '").append(matricula).append("'");
		sqlStr.append(") UNION (");
		sqlStr.append("select min(horarioturmadia.data) as dataMinima from matricula ");
		sqlStr.append("inner join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append(" and matriculaperiodo.codigo  = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaoMatriculaPeriodo <> 'PC' ");
		sqlStr.append(" order by (mp.ano ||'/' || mp.semestre) desc , case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1)  ");
		sqlStr.append("inner join turma on  matriculaperiodo.turma = turma.codigo ");
		sqlStr.append("inner join turmaagrupada ta on ta.turma = turma.codigo ");
		sqlStr.append("INNER JOIN horarioturma ON horarioturma.turma = ta.turmaorigem ");
		sqlStr.append("AND horarioturma.anovigente = matriculaPeriodo.ano AND horarioturma.semestrevigente = matriculaPeriodo.semestre ");
		sqlStr.append("INNER JOIN horarioturmadia ON horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append("where matricula.matricula = '").append(matricula).append("'");
		sqlStr.append(")) as primeiraData");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return rs.getDate("dataMinima");
		}
		return null;
	}

	@Override
	public List<MatriculaVO> consultarMatriculaPorCodigoProspect(Integer codigoProspect, Integer unidadeEnsinoLogada,  UsuarioVO usuarioLogado) throws Exception {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" select matricula.matricula,matricula.aluno, matricula.curso, matricula.unidadeEnsino  ");
			sql.append("  from matricula ");
			sql.append(" inner join pessoa on matricula.aluno = pessoa.codigo ");
			sql.append(" inner join prospects on prospects.pessoa = pessoa.codigo ");
			sql.append(" where prospects.codigo =").append(codigoProspect);
			sql.append(" and matricula.situacao in ('AT')");
			if(Uteis.isAtributoPreenchido(unidadeEnsinoLogada)) {
				sql.append(" and matricula.unidadeensino = ").append(unidadeEnsinoLogada);
			}
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_COMBOBOX, null, usuarioLogado);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void alterarObservacaoDiplomaMatricula(final String matricula, final String observacao, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE matricula SET observacaoDiploma=? WHERE ((matricula = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);

				sqlAlterar.setString(1, observacao);
				sqlAlterar.setString(2, matricula);
				return sqlAlterar;
			}
		});
	}

	@Override
	public void alterarDataConclusaoCurso(final String matricula, final Date data, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE matricula SET dataConclusaoCurso=?, anoConclusao=?, semestreConclusao=? WHERE ((matricula = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if (Uteis.isAtributoPreenchido(data)) {
					sqlAlterar.setDate(1, Uteis.getDataJDBC(data));
				} else {
					sqlAlterar.setNull(1, 0);
				}
				if (Uteis.isAtributoPreenchido(data)) {
					sqlAlterar.setString(2, String.valueOf(Uteis.getAnoData(data)));
				} else {
					sqlAlterar.setNull(2, 0);
				}
				if (Uteis.isAtributoPreenchido(data)) {
					sqlAlterar.setString(3, Uteis.getSemestreData(data));
				} else {
					sqlAlterar.setNull(3, 0);
				}
				sqlAlterar.setString(4, matricula);
				return sqlAlterar;
			}
		});
	}

	@Override
	public MatriculaVO consultaRapidaPorMatriculaAnoSemestrePeriodoLetivoGradeCurricularBoletim(String matricula, String ano, String semestre, Integer gradeCurricular, Integer periodoLetivo, Integer unidadeEnsino, UsuarioVO usuario, BimestreEnum bimestre, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota) throws Exception {
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaBoletim();
		sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sqlStr.append("INNER JOIN matriculaperiodo ON matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sqlStr.append("INNER JOIN PeriodoLetivo ON (PeriodoLetivo.codigo = matriculaperiodo.periodoletivomatricula) ");
		sqlStr.append(" WHERE matriculaperiodoturmadisciplina.matricula = '").append(matricula).append("' ");

		if (unidadeEnsino.intValue() != 0 && unidadeEnsino != null) {
			sqlStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		if (periodoLetivo.intValue() != 0 && periodoLetivo != null) {
			sqlStr.append(" AND periodoletivo.codigo = ").append(periodoLetivo);
		}
		if (!ano.equals("") && ano != null) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("") && semestre != null) {
			sqlStr.append(" AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ");
		}
		if (gradeCurricular != 0 && gradeCurricular != null) {
			sqlStr.append(" AND PeriodoLetivo.gradecurricular = '").append(gradeCurricular).append("' ");
		}
		if (situacaoRecuperacaoNota != null && !situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.TODAS)) {
			if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.SEM_RECUPERACAO)) {
				sqlStr.append(" and matricula.matricula not in ( ");
			} else {
				sqlStr.append(" and matricula.matricula in ( ");
			}
			sqlStr.append(" select his.matricula from historiconota ");
			sqlStr.append(" inner join historico his on his.codigo = historiconota.historico ");
			sqlStr.append(" inner join configuracaoacademiconota on configuracaoacademiconota.configuracaoacademico = his.configuracaoacademico ");
			sqlStr.append(" and historiconota.tiponota = configuracaoacademiconota.nota ");
			sqlStr.append(" where configuracaoacademiconota.notaRecuperacao ");
			sqlStr.append(" and his.matricula = matricula.matricula ");
			sqlStr.append(" and his.anohistorico = matriculaperiodoturmadisciplina.ano ");
			sqlStr.append(" and his.semestrehistorico = matriculaperiodoturmadisciplina.semestre ");
			sqlStr.append(" and his.matrizcurricular = matricula.gradecurricularatual ");
			sqlStr.append(" and (his.gradedisciplina is not null or his.gradeCurricularGrupoOptativaDisciplina is not null or his.historicoDisciplinaForaGrade = true or his.gradedisciplinacomposta is not null)");
			sqlStr.append(" and (his.historicoporequivalencia is null or his.historicoporequivalencia = false)");
			if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.NOTA_NAO_RECUPERADA)) {
				sqlStr.append(" and historiconota.situacaorecuperacaonota = 'NOTA_NAO_RECUPERADA' ");
			} else if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.NOTA_RECUPERADA)) {
				sqlStr.append(" and historiconota.situacaorecuperacaonota = 'NOTA_RECUPERADA' ");
			} else {
				sqlStr.append(" and historiconota.situacaorecuperacaonota in ('NOTA_RECUPERADA', 'NOTA_NAO_RECUPERADA', 'EM_RECUPERACAO') ");
			}
			if (bimestre != null) {
				sqlStr.append(" and configuracaoacademiconota.agrupamentoNota = ('").append(bimestre.name()).append("') ");
			}
			sqlStr.append(" order by replace(tiponota, 'NOTA_', '')::INT desc limit 1 ");
			sqlStr.append(" ) ");
		}
		sqlStr.append(" ORDER BY pessoa.nome; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		MatriculaVO obj = new MatriculaVO();
		if (tabelaResultado.next()) {
			montarDadosBasicoBoletim(obj, tabelaResultado);
		}
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTransferenciaEntrada(final String matricula, final Integer transferenciaEntrada, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE matricula SET tranferenciaEntrada = ? WHERE ((matricula = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, transferenciaEntrada);
				sqlAlterar.setString(2, matricula);
				return sqlAlterar;
			}
		});
	}

	@Override
	public void alterarLocalArmazenamentoDocumentosMatricula(final String localArmazenamentoDocumentoMatricula, final String matricula, UsuarioVO usuarioVO) throws Exception {
		final StringBuilder sql = new StringBuilder();
		sql.append("UPDATE matricula SET localarmazenamentodocumentosmatricula=? WHERE (matricula = ?) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				if (Uteis.isAtributoPreenchido(localArmazenamentoDocumentoMatricula)) {
					sqlAlterar.setString(1, localArmazenamentoDocumentoMatricula);
				} else {
					sqlAlterar.setString(1, "");
				}
				sqlAlterar.setString(2, matricula);
				return sqlAlterar;
			}
		});
	}

	@Override
	public MatriculaPeriodoVO realizarGeracaoNovaMatriculaPeriodoParaRenovacao(MatriculaVO matriculaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {		
		getFacadeFactory().getMatriculaFacade().carregarDados(matriculaVO, NivelMontarDados.TODOS, usuarioVO);
		matriculaVO.setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(matriculaVO.getUltimoMatriculaPeriodoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		MatriculaPeriodoVO novoMatriculaPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().obterNovoMatriculaPeriodoBaseadoUltimoPeriodoLetivo(matriculaVO, configuracaoFinanceiroVO, usuarioVO);
		getFacadeFactory().getMatriculaFacade().validarMatriculaPodeSerRenovada(matriculaVO, novoMatriculaPeriodo);
		inicializarMatriculaComHistoricoAluno(matriculaVO, novoMatriculaPeriodo, true, usuarioVO);
		return novoMatriculaPeriodo;
	}


	@Override
	public void executarDefinicaoPeriodoLetivoNovaMatriculaAluno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, Integer matrizCurricular, Boolean realizandoNovaMatricula, UsuarioVO usuarioVO) throws Exception {
		GradeCurricularVO novaGradeInformada = matriculaVO.getGradeCurricularAtual();
		if ((matriculaVO.getGradeCurricularAtual().getCodigo().equals(0)) || (!matriculaVO.getGradeCurricularAtual().getCodigo().equals(matrizCurricular))) {
			// quando é uma matricula nova, temos que a gradeAtual ainda nao foi
			// setada, por isto
			// este método define para a matricula do aluno, a matrizcurricular
			// atual do mesmo
			// que acabou de ser selecionada pelo usuario.
			novaGradeInformada = new GradeCurricularVO();
			if (!matrizCurricular.equals(0)) {
				novaGradeInformada.setCodigo(matrizCurricular);
				matriculaPeriodoVO.setGradeCurricular(novaGradeInformada);
			} else {
				novaGradeInformada.setCodigo(matriculaPeriodoVO.getGradeCurricular().getCodigo());
			}
			matriculaVO.setGradeCurricularAtual(novaGradeInformada);
			inicializarMatriculaComHistoricoAluno(matriculaVO, matriculaPeriodoVO, true, usuarioVO);
			// getMatriculaPeriodoVO().setPeridoLetivo(new PeriodoLetivoVO());
		}
		if (realizandoNovaMatricula) {
			matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs().clear();
			if (!((matriculaVO.getMatriculaComHistoricoAlunoVO().getIsInicializado()))) {
				inicializarMatriculaComHistoricoAluno(matriculaVO, matriculaPeriodoVO, false, usuarioVO);
			}
		}
	}

	@Override
	public void inicializarMatriculaComHistoricoAluno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, Boolean forcarNovoCarregamentoDados, UsuarioVO usuarioVO) throws Exception {
		if (((matriculaVO.getMatriculaComHistoricoAlunoVO().getIsInicializado()) && (!forcarNovoCarregamentoDados))) {
			return;
		}
		if (matriculaVO.getMatricula().equals("")) {
			matriculaVO.setGradeCurricularAtual(matriculaPeriodoVO.getGradeCurricular());
		}
		if (matriculaVO.getGradeCurricularAtual().getCodigo().equals(0)) {
			// sem a definicao da grade, não é possível chamar o método de
			// inicialização.
			return;
		}
		ConfiguracaoAcademicoVO cfg = getAplicacaoControle().carregarDadosConfiguracaoAcademica(matriculaVO.getCurso().getConfiguracaoAcademico().getCodigo());
		matriculaVO.getCurso().setConfiguracaoAcademico(cfg);

		MatriculaComHistoricoAlunoVO matriculaComHistoricoAlunoVO = getFacadeFactory().getHistoricoFacade().carregarDadosMatriculaComHistoricoAlunoVO(matriculaVO, matriculaVO.getGradeCurricularAtual().getCodigo(), false, matriculaVO.getCurso().getConfiguracaoAcademico(), usuarioVO);
		matriculaVO.setMatriculaComHistoricoAlunoVO(matriculaComHistoricoAlunoVO);
		matriculaVO.setGradeCurricularAtual(matriculaComHistoricoAlunoVO.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
		matriculaPeriodoVO.setGradeCurricular(matriculaComHistoricoAlunoVO.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
		matriculaPeriodoVO.setPeridoLetivo(matriculaComHistoricoAlunoVO.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO().consultarObjPeriodoLetivoVOPorCodigo(matriculaPeriodoVO.getPeridoLetivo().getCodigo()));
		int unidadeEnsino = matriculaVO.getUnidadeEnsino().getCodigo();
		if (!Uteis.isAtributoPreenchido(unidadeEnsino)) {
			unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorMatricula(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO).getCodigo();
		}
		if (matriculaPeriodoVO.getIsNovaMatriculaPeriodo() || matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs().isEmpty()) {
			matriculaPeriodoVO.setMatriculaEspecial(getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unidadeEnsino, usuarioVO).getUtilizarIntegracaoFinanceira() ? false : matriculaComHistoricoAlunoVO.getAlunoEmSituacaoParaMatriculaEspecial());
		}
		getFacadeFactory().getMatriculaPeriodoFacade().obterListaPeriodosLetivosValidosParaRenovacaoMatriculaInicializandoPeriodoLetivoPadrao(matriculaVO, matriculaPeriodoVO, matriculaVO.getCurso().getConfiguracaoAcademico(), matriculaVO.getMatriculaComHistoricoAlunoVO(), usuarioVO);

	}

	@Override
	public void realizarDefinicaoDocumentacaoMatriculaAluno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, boolean simulandoRenovacao, UsuarioVO usuarioVO) throws Exception {

		if (matriculaVO.getCurso().getNivelEducacionalPosGraduacao()) {
			Boolean aptoMatricula = getFacadeFactory().getMatriculaFacade().validarMatriculaAlunoPosGraduacao(matriculaVO.getAluno().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo());
			if (!aptoMatricula) {
				throw new Exception("Não é possível realizar a MATRÍCULA deste aluno em um curso de Pós-Graduação pois o mesmo não possui uma formação acadêmica em Graduação");
			}
		}
		if (matriculaVO.getMatricula().equals("")) {
			inicializarMatriculaComHistoricoAluno(matriculaVO, matriculaPeriodoVO, false, usuarioVO);
		}
		if (matriculaPeriodoVO.getNovoObj()) {
			getFacadeFactory().getMatriculaPeriodoFacade().validarDadosRenovacaoProcessoMatriculaPeriodoLetivo(matriculaVO, matriculaPeriodoVO, usuarioVO, configuracaoGeralSistemaVO);
		}
		matriculaPeriodoVO.getResponsavelRenovacaoMatricula().setCodigo(usuarioVO.getCodigo());
		matriculaPeriodoVO.getResponsavelRenovacaoMatricula().setNome(usuarioVO.getNome());
		if(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo() > 0 && !simulandoRenovacao){
			getFacadeFactory().getMatriculaFacade().inicializarTextoContratoPlanoFinanceiroAluno(matriculaVO, matriculaPeriodoVO, true, usuarioVO);
		}
		if (!Uteis.isAtributoPreenchido(matriculaVO.getMatricula())) {
			matriculaVO.getUsuario().setCodigo(usuarioVO.getCodigo());
			matriculaVO.getUsuario().setNome(usuarioVO.getNome());
		}
		getFacadeFactory().getMatriculaPeriodoFacade().validarMatriculaPeriodoPodeSerRealizada(matriculaVO, matriculaPeriodoVO, usuarioVO);
		getFacadeFactory().getMatriculaFacade().gerenciarEntregaDocumentoMatricula(matriculaVO, usuarioVO);
		if (matriculaPeriodoVO.getIsPrimeiroPeriodoLetivo()) {
			matriculaPeriodoVO.setData(matriculaPeriodoVO.getData());
		}
	}

	@Override
	public void realizarDefinicaoDisciplinaMatriculaPeriodo(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean renovandoMatricula, boolean liberadoInclusaoTurmaOutroUnidadeEnsino, boolean liberadoInclusaoTurmaOutroCurso, boolean liberadoInclusaoTurmaOutroMatrizCurricular, UsuarioVO usuarioVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception {

		if (renovandoMatricula) {
			getFacadeFactory().getMatriculaFacade().verificarDocumentaoImpediRenovacaoEstaPendente(matriculaVO, usuarioVO);
		}

		getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosAnoSemestreMatricula(matriculaPeriodoVO, matriculaPeriodoVO.getProcessoMatriculaCalendarioVO(), false);
		if(!matriculaVO.getCurso().getConfiguracaoAcademico().isHabilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares()) {
			getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosDefinirDisciplinasMatriculaPeriodo(matriculaVO, matriculaPeriodoVO, usuarioVO, gradeDisciplinaCompostaVOs, false, false);
			if (configuracaoGeralSistemaVO.getControlaQtdDisciplinaExtensao().booleanValue()) {
				if (matriculaVO.getTipoMatricula().equals("EX")) {
					// obter lista disciplinas que devem ficar
					List<DisciplinaVO> lista = getFacadeFactory().getHorarioTurmaDiaFacade().consultarDisciplinaUltimaDataAulaProgramadaMenorDataAtual(matriculaPeriodoVO.getTurma().getCodigo(), matriculaVO.getQtdDisciplinasExtensao());
					// remover utilizando os metodos exclusão de disciplina
					List<MatriculaPeriodoTurmaDisciplinaVO> listaRemover = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>();
					Iterator<MatriculaPeriodoTurmaDisciplinaVO> i = matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs().iterator();
					if (matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs().size() > lista.size()) {
						while (i.hasNext()) {
							MatriculaPeriodoTurmaDisciplinaVO mptd = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
							boolean presenteLista = false;
							Iterator<DisciplinaVO> j = lista.iterator();
							while (j.hasNext()) {
								DisciplinaVO disc = (DisciplinaVO) j.next();
								if (disc.getCodigo().intValue() == mptd.getDisciplina().getCodigo().intValue()) {
									presenteLista = true;
								}
							}
							if (!presenteLista) {
								listaRemover.add(mptd);
							}
						}
						i = listaRemover.iterator();
						while (i.hasNext()) {
							MatriculaPeriodoTurmaDisciplinaVO obj = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
							getFacadeFactory().getMatriculaPeriodoFacade().removerMatriculaPeriodoTurmaDisciplinaComposta(matriculaPeriodoVO.getMatriculaPeriodoTumaDisciplinaVOs(), obj, usuarioVO);
							getFacadeFactory().getMatriculaPeriodoFacade().removerMatriculaPeriodoTurmaDisciplinaObjEspecifico(matriculaPeriodoVO, matriculaVO, obj, usuarioVO);
						}
					}
				}
			}
		}			
		Integer nrPeriodoLetivo = matriculaPeriodoVO.getPeriodoLetivo().getPeriodoLetivo() > 1 ? matriculaPeriodoVO.getPeriodoLetivo().getPeriodoLetivo() : 1;
		if(matriculaVO.getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoDisciplinaPeriodoFuturo()) {			
			if(Uteis.isAtributoPreenchido(matriculaVO.getCurso().getConfiguracaoAcademico().getNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina()) ) {				
				nrPeriodoLetivo = nrPeriodoLetivo + matriculaVO.getCurso().getConfiguracaoAcademico().getNumeroPeriodoLetivoPosteriorPermiteInclusaoDisciplina();				
				if(Uteis.isAtributoPreenchido(nrPeriodoLetivo) && nrPeriodoLetivo >= matriculaPeriodoVO.getGradeCurricular().getUltimoPeriodoLetivoGrade().getPeriodoLetivo()) {
					nrPeriodoLetivo = matriculaPeriodoVO.getGradeCurricular().getUltimoPeriodoLetivoGrade().getPeriodoLetivo();
				}
					
			}else if(Uteis.isAtributoPreenchido(matriculaPeriodoVO.getGradeCurricular().getUltimoPeriodoLetivoGrade().getPeriodoLetivo())){
				nrPeriodoLetivo  =   matriculaPeriodoVO.getGradeCurricular().getUltimoPeriodoLetivoGrade().getPeriodoLetivo();				
			}			
		}		
		List<GradeDisciplinaVO> listaDisciplinasPeriodoLetivoAlunoPendente = matriculaVO.getMatriculaComHistoricoAlunoVO().getGradeCurricularComHistoricoAlunoVO().getListaGradeDisciplinaVOsPendentesGradeCurricular(nrPeriodoLetivo);
		getFacadeFactory().getMatriculaPeriodoFacade().realizarVerificacaoDeDistribuicaoDisciplinaDependenciaAutomatica(matriculaVO, matriculaPeriodoVO,listaDisciplinasPeriodoLetivoAlunoPendente,liberadoInclusaoTurmaOutroUnidadeEnsino, liberadoInclusaoTurmaOutroCurso, liberadoInclusaoTurmaOutroMatrizCurricular, false, new ArrayList<HorarioAlunoTurnoVO>() ,usuarioVO);
		
		
	}

	@Override
	public void realizarCalculoValoresMatriculaMensalidadePeriodoAluno(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getMatriculaPeriodoFacade().inicializarPlanoFinanceiroAlunoMatriculaPeriodo(matriculaVO, matriculaPeriodoVO, matriculaVO.getPlanoFinanceiroAluno(), true, usuarioVO);
		getFacadeFactory().getMatriculaPeriodoFacade().realizarCalculoValorMatriculaEMensalidade(matriculaVO, matriculaPeriodoVO, usuarioVO);
		getFacadeFactory().getMatriculaFacade().calcularTotalDesconto(matriculaVO, matriculaPeriodoVO, matriculaVO.getPlanoFinanceiroAluno().obterOrdemAplicacaoDescontosPadraoAtual(), usuarioVO, configuracaoFinanceiroVO);
	}

	@Override
	public void realizarDefinicaoDadosFinanceiroMatriculaPeriodo(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean permitirRealizarMatriculaDisciplinaPreRequisito, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getMatriculaPeriodoFacade().validarDisponibilidadeVagasMatriculaPeriodoTurmaDisciplina(matriculaVO, matriculaPeriodoVO, false, usuarioVO);
		getFacadeFactory().getMatriculaPeriodoFacade().verificarPreRequisitoDisciplina(matriculaPeriodoVO, matriculaVO, permitirRealizarMatriculaDisciplinaPreRequisito, usuarioVO);
		realizarCalculoValoresMatriculaMensalidadePeriodoAluno(matriculaVO, matriculaPeriodoVO, configuracaoFinanceiroVO, usuarioVO);
	}

	@Override
	public void persistir(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		if (matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoParcela().equals("PO")) {
			if (matriculaVO.getPlanoFinanceiroAluno().getPercDescontoParcela() == 100) {
				matriculaPeriodoVO.setBolsista(Boolean.TRUE);
			} else {
				matriculaPeriodoVO.setBolsista(Boolean.FALSE);
			}
		} else if (matriculaVO.getPlanoFinanceiroAluno().getTipoDescontoParcela().equals("VA")) {
			if (matriculaVO.getPlanoFinanceiroAluno().getValorDescontoParcela() >= matriculaPeriodoVO.getValorMensalidadeCheio()) {
				matriculaPeriodoVO.setBolsista(Boolean.TRUE);
			} else {
				matriculaPeriodoVO.setBolsista(Boolean.FALSE);
			}
		}
		if (matriculaVO.getIsAtiva()) {
			getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, usuarioVO);
			matriculaPeriodoVO.getResponsavelRenovacaoMatricula().setCodigo(usuarioVO.getCodigo());
			matriculaPeriodoVO.getResponsavelRenovacaoMatricula().setNome(usuarioVO.getNome());
			if (!matriculaVO.getMatricula().equals("")) {
				matriculaPeriodoVO.setMatricula(matriculaVO.getMatricula());
			}
			matriculaVO.adicionarObjMatriculaPeriodoVOs(matriculaPeriodoVO);
			if (!matriculaVO.getMatriculaJaRegistrada()) {
				if (matriculaVO.getAluno().getCodProspect() != null && matriculaVO.getAluno().getCodProspect() != 0) {
					getFacadeFactory().getMatriculaFacade().incluir(matriculaVO, matriculaPeriodoVO, matriculaPeriodoVO.getProcessoMatriculaCalendarioVO(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso(), configuracaoFinanceiroVO, configuracaoGeralSistemaVO, false, false,false, true,  usuarioVO);
				} else {
					getFacadeFactory().getMatriculaFacade().incluir(matriculaVO, matriculaPeriodoVO, matriculaPeriodoVO.getProcessoMatriculaCalendarioVO(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso(), configuracaoFinanceiroVO, configuracaoGeralSistemaVO, false, true, false ,true , usuarioVO);
				}
			} else {
				matriculaVO.getUsuario().setCodigo(usuarioVO.getCodigo());
				matriculaVO.getUsuario().setNome(usuarioVO.getNome());
				matriculaVO.getPlanoFinanceiroAluno().getResponsavel().setCodigo(usuarioVO.getCodigo());
				matriculaVO.getPlanoFinanceiroAluno().getResponsavel().setNome(usuarioVO.getNome());
				getFacadeFactory().getMatriculaFacade().alterar(matriculaVO, matriculaPeriodoVO, matriculaPeriodoVO.getProcessoMatriculaCalendarioVO(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso(), configuracaoFinanceiroVO, configuracaoGeralSistemaVO, usuarioVO, false);

			}
		} else {
			throw new ConsistirException("Esta renovação não pode ser gravada pois refere-se a uma matrícula: " + matriculaVO.getSituacao_Apresentar().toUpperCase());
		}
	}

	/**
	 * @author Leonardo Riciolle
	 * Update usado na tela de ExpedicaoDiplomaControle especifico para
	 * alterar apenas a data Colacao de curso de um aluno especifico na hora de
	 * gravar a expedicao do diploma.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataColacaoGrauPorMatricula(final MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE matricula SET datacolacaograu=? WHERE ((matricula = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setDate(1, Uteis.getDataJDBC(matriculaVO.getDataColacaoGrau()));
				sqlAlterar.setString(2, matriculaVO.getMatricula());
				return sqlAlterar;
			}
		});
	}

	/**
	 * @author Leonardo Riciolle
	 * Update usado na tela de ExpedicaoDiplomaControle especifico para
	 * alterar apenas a conclusao de curso de um aluno especifico na hora de
	 * imprimir o diploma.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataInicioEDataConclusaoCursoPorMatricula(final MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE matricula SET dataConclusaoCurso= ?, dataInicioCurso= ? WHERE ((matricula = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);

				sqlAlterar.setDate(1, Uteis.getDataJDBC(matriculaVO.getDataConclusaoCurso()));
				sqlAlterar.setDate(2, Uteis.getDataJDBC(matriculaVO.getDataInicioCurso()));
				sqlAlterar.setString(3, matriculaVO.getMatricula());
				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarMatriculaCanceladoFinanceiro(String matricula, boolean canceladoFinanceiro) throws Exception {
		Date updated = new Date();
		try {
			String sqlStr = "UPDATE Matricula set canceladoFinanceiro=?, updated=? WHERE ((matricula = ?))";
			getConexao().getJdbcTemplate().update(sqlStr, new Object[] { canceladoFinanceiro, Uteis.getDataJDBCTimestamp(updated), matricula });
		} finally {
			updated = null;
		}
	}


	public List<MatriculaVO> consultarPorMatriculaConfiguracaoGabaritoProvaPresencial(GabaritoVO gabaritoVO, String situacao, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct matricula.matricula, pessoa.nome ");
		sb.append(" from matricula ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sb.append(" and matriculaperiodo.codigo in(");
		sb.append(" select mp.codigo from matriculaperiodo mp ");

		sb.append(" where mp.matricula = matricula.matricula ");
		if (!gabaritoVO.getAno().equals("")) {
			sb.append(" and mp.ano = '").append(gabaritoVO.getAno()).append("' ");
		}
		if (!gabaritoVO.getSemestre().equals("")) {
			sb.append(" and mp.semestre = '").append(gabaritoVO.getSemestre()).append("' ");
		}
		sb.append(" order by mp.codigo desc, (ano || semestre) desc limit 1 )");
		sb.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sb.append(" inner join historico on historico.matricula = matricula.matricula and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		sb.append(" left join periodoletivo on periodoletivo.codigo = historico.periodoletivomatrizcurricular ");
		sb.append(" where ");
		sb.append(" matricula.unidadeEnsino = ").append(gabaritoVO.getUnidadeEnsinoVO().getCodigo());
		sb.append(" and matricula.curso = ").append(gabaritoVO.getCursoVO().getCodigo());
		sb.append(" and matricula.situacao = '").append(situacao.toUpperCase()).append("' ");
		sb.append(" and matricula.gradecurricularAtual = ").append(gabaritoVO.getGradeCurricularVO().getCodigo());
		sb.append(" and historico.configuracaoacademico = ").append(gabaritoVO.getConfiguracaoAcademicoVO().getCodigo());
		if (!gabaritoVO.getAno().equals("")) {
			sb.append(" and matriculaperiodo.ano = '").append(gabaritoVO.getAno()).append("' ");
		}
		if (!gabaritoVO.getSemestre().equals("")) {
			sb.append(" and matriculaperiodo.semestre = '").append(gabaritoVO.getSemestre()).append("' ");
		}
		if(Uteis.isAtributoPreenchido(gabaritoVO.getPeriodoLetivoVO())) {
			sb.append(" and periodoletivo.codigo = ").append(gabaritoVO.getPeriodoLetivoVO().getCodigo());
		}
		sb.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		if(gabaritoVO.getTipoRespostaGabaritoEnum().equals(TipoRespostaGabaritoEnum.DISCIPLINA)) {
			StringBuilder disIn = new StringBuilder();
			List<Integer> dis = new ArrayList<Integer>(0);
			for(GabaritoRespostaVO gabaritoRespostaVO: gabaritoVO.getGabaritoRespostaVOs()) {
				if(Uteis.isAtributoPreenchido(gabaritoRespostaVO.getDisciplinaVO()) && !dis.contains(gabaritoRespostaVO.getDisciplinaVO().getCodigo())) {
					if(disIn.length() > 0) {
						disIn.append(", ");
					}
					disIn.append(gabaritoRespostaVO.getDisciplinaVO().getCodigo());
					dis.add(gabaritoRespostaVO.getDisciplinaVO().getCodigo());
				}
			}
			if(disIn.length() > 0) {
				sb.append(" and historico.disciplina in (").append(disIn.toString()).append(") ");
			}
		}
		sb.append(" order by pessoa.nome, matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<MatriculaVO> listaMatriculaVOs = new ArrayList<MatriculaVO>(0);
		MatriculaVO obj = null;
		while (tabelaResultado.next()) {
			obj = new MatriculaVO();
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.getAluno().setNome(tabelaResultado.getString("nome"));
			listaMatriculaVOs.add(obj);
		}
		return listaMatriculaVOs;
	}

	public Boolean consultarExistenciaMatriculaPorMatricula(String matricula, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select matricula from matricula where matricula = '").append(matricula).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Boolean consultarExistenciaMatriculaPorCodigoPessoaPorUnidadeEnsinoDiferenteMatricula(Integer pessoa, Integer unidadeEnsino, String matricula, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder(" select count(distinct matricula.matricula) as QTDE from matricula ");
		sql.append(" inner join configuracaoseigsuiteunidadeensino on configuracaoseigsuiteunidadeensino.unidadeensino = matricula.unidadeensino ");
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append(" inner join pessoagsuite on pessoagsuite.pessoa = pessoa.codigo ");
		sql.append(" where matricula.matricula != '").append(matricula).append("' ");
		sql.append(" and  matricula.aluno = '").append(pessoa).append("' ");
		sql.append(" and  (( pessoa.aluno and pessoagsuite.email  ilike '%'|| configuracaoseigsuiteunidadeensino.dominioemail ||'%' )  ");
		sql.append("  or ( pessoa.funcionario and pessoagsuite.email  ilike '%'|| configuracaoseigsuiteunidadeensino.dominioemailfuncionario ||'%' ))   ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return Uteis.isAtributoPreenchido(tabelaResultado, Uteis.QTDE, TipoCampoEnum.INTEIRO);
	}


	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarPessoaMatriculaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception {
		String sqlStr = "UPDATE Matricula set aluno=?, updated=? WHERE ((aluno = ?))";
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { pessoaNova, Uteis.getDataJDBCTimestamp(new Date()), pessoaAntigo });
	}

	/**
	 * Método utilizado apenas para correção da data de conclusão do curso após importação da UniRV.
	 * @return
	 * @throws Exception
	 * Autor: Alessandro Lima
	 */
	@Override
	public List<MatriculaVO> consultarMatriculasAlunosFormadosParaCorrecaoDataConclusaoCurso() throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select t.matricula, t.gradecurricularatual, substring(t.conclusao from 1 for 4) as anoconclusao, substring(t.conclusao from 5 for 1) as semestreconclusao from ( ")
			  .append("select h.matricula, m.gradecurricularatual, max(h.anohistorico||h.semestrehistorico) as conclusao from historico as h ")
			  .append("inner join matricula as m on m.matricula = h.matricula ")
			  .append("group by h.matricula, m.gradecurricularatual) as t");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<MatriculaVO> matriculas = new ArrayList<MatriculaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.getGradeCurricularAtual().setCodigo(tabelaResultado.getInt("gradecurricularatual"));
			obj.setAnoConclusao(tabelaResultado.getString("anoconclusao"));
			obj.setSemestreConclusao(tabelaResultado.getString("semestreconclusao"));
			matriculas.add(obj);
		}
		return matriculas;
	}

	/**
	 * Método utilizado apenas para correção da data de conclusão do curso após importação da UniRV.
	 * Autor: Alessandro Lima
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCorrecaoDataConclusaoCursoUniRV(UsuarioVO usuario) throws Exception {
		for (MatriculaVO matricula : getFacadeFactory().getMatriculaFacade().consultarMatriculasAlunosFormadosParaCorrecaoDataConclusaoCurso()) {
			if (isMatriculaIntegralizada(matricula, matricula.getGradeCurricularAtual().getCodigo(), usuario, null)) {
				Calendar c = Calendar.getInstance();
				c.set(Calendar.YEAR, Integer.parseInt(matricula.getAnoConclusao()));
		        c.set(Calendar.HOUR_OF_DAY, 0);
		        c.set(Calendar.MINUTE, 0);
		        c.set(Calendar.SECOND, 0);
		        c.set(Calendar.MILLISECOND, 0);
				if (matricula.getSemestreConclusao().equals("1")) {
					c.set(Calendar.MONTH, 6);
					c.set(Calendar.DAY_OF_MONTH, 25);
				} else if (matricula.getSemestreConclusao().equals("2")) {
					c.set(Calendar.MONTH, 11);
					c.set(Calendar.DAY_OF_MONTH, 31);
				}
				matricula.setDataConclusaoCurso(c.getTime());
				final String sql = "UPDATE matricula SET dataConclusaoCurso=?, anoConclusao=?, semestreConclusao=? WHERE ((matricula = ?)) " +
						adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
				getConexao().getJdbcTemplate().update(sql, Uteis.getDataJDBC(matricula.getDataConclusaoCurso()),
													  matricula.getAnoConclusao(), matricula.getSemestreConclusao(), matricula.getMatricula());
			}
		}
	}

	@Override
	public void adicionarObjMatriculaPeriodoVOs(MatriculaPeriodoVO obj, MatriculaVO matriculaVO) throws Exception {
		if (!matriculaVO.getMatricula().equals("")) {
			obj.setMatricula(matriculaVO.getMatricula());
		}
		int index = 0;
		Iterator i = matriculaVO.getMatriculaPeriodoVOs().iterator();
		while (i.hasNext()) {
			MatriculaPeriodoVO objExistente = (MatriculaPeriodoVO) i.next();
			if (objExistente.getGradeCurricular().getCodigo().equals(obj.getGradeCurricular().getCodigo())) {
				matriculaVO.getMatriculaPeriodoVOs().set(index, obj);
				return;
			}
			index++;
		}
		matriculaVO.getMatriculaPeriodoVOs().add(obj);
	}

	public void alterarSituacaoMatriculaEstornoCancelamento(final String matricula, final UsuarioVO usuarioVO) throws Exception {
		final String sqlStr = "update matricula set situacao = 'AT' where situacao = 'CA' and matricula = ?";

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr);
				sqlAlterar.setString(1, matricula);
				return sqlAlterar;
			}
		});
	}

	public void alterarSituacaoMatriculaEstornoTrancamento(final String matricula, final UsuarioVO usuarioVO) throws Exception {
		final String sqlStr = "update matricula set situacao = 'AT' where situacao in ('TR','AC','JU') and matricula = ?";

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr);
				sqlAlterar.setString(1, matricula);
				return sqlAlterar;
			}
		});
	}

	public MatriculaVO consultarPorObjetoMatricula(String valorConsulta, List<UnidadeEnsinoVO> unidadeVOs, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE matricula = '").append(valorConsulta).append("' ");
		if (Uteis.isAtributoPreenchido(unidadeVOs)) {
			sqlStr.append(" AND unidadeensino.codigo  IN (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeVOs) {
				if (x > 0) {
					sqlStr.append(", ");
				}
				sqlStr.append(unidadeEnsinoVO.getCodigo());
				x++;
			}
			sqlStr.append(" ) ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new MatriculaVO();
		} else {
			MatriculaVO obj = new MatriculaVO();
			montarDadosBasico(obj, tabelaResultado);
			return obj;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarGradeCurricularAtualPorTurma(TurmaVO turmaVO, String situacaoMatricula,  String situacaoMatriculaPeriodo, List<MatriculaPeriodoVO> listaMatriculaPeriodoVO, UsuarioVO usuarioVO, Boolean realizandoTransferenciaMatrizCurricularPelaTurma) throws Exception {
		StringBuilder sql = new StringBuilder("UPDATE Matricula set gradeCurricularAtual = ").append(turmaVO.getGradeCurricularVO().getCodigo());
		if (!listaMatriculaPeriodoVO.isEmpty()) {
			sql.append(listaMatriculaPeriodoVO.stream().map(l -> l.getMatriculaVO().getMatricula()).collect(Collectors.joining("', '", " WHERE matricula in ('",  "') ")));
		} else {
			sql.append(" WHERE matricula in (SELECT distinct Matricula.matricula FROM Matricula ");
			sql.append("INNER JOIN MatriculaPeriodo ON MatriculaPeriodo.matricula = Matricula.matricula ");
			sql.append("WHERE MatriculaPeriodo.turma = ").append(turmaVO.getCodigo());
			if (Uteis.isAtributoPreenchido(turmaVO.getUnidadeEnsino())) {
				sql.append(" AND Matricula.unidadeEnsino = ").append(turmaVO.getUnidadeEnsino().getCodigo());
			}
			if(Uteis.isAtributoPreenchido(situacaoMatricula)){
				sql.append(" AND Matricula.situacao = '").append(situacaoMatricula).append("' ");
			}
			if(Uteis.isAtributoPreenchido(situacaoMatriculaPeriodo)){
				sql.append(" AND MatriculaPeriodo.situacaoMatriculaPeriodo = '").append(situacaoMatriculaPeriodo).append("' ");
			}
			
			if (realizandoTransferenciaMatrizCurricularPelaTurma) {
//			sql.append(" and not exists (");
//			sql.append(" select  transferenciamatrizcurricularmatricula.codigo from transferenciamatrizcurricularmatricula where matriculaperiodo.codigo = transferenciamatrizcurricularmatricula.matriculaperiodo ");
//			sql.append(" )");
//			sql.append(" AND matriculaPeriodo.gradecurricular = matricula.gradecurricularatual");
			}
			
			sql.append(")");
		}
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	public Boolean consultarExistenciaCodigoFinanceiroMatricula(String codigoFinanceiroMatricula, String matricula) throws Exception {
		try {
			if(codigoFinanceiroMatricula.equals("99999999")){
				return false;
			}
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT COUNT(matricula.matricula) AS quantidade_registro FROM matricula ");
			sql.append("  WHERE matricula.codigofinanceiromatricula = '").append(codigoFinanceiroMatricula).append("'");
			sql.append("  AND matricula.matricula != '").append(matricula).append("' ;");

			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			tabelaResultado.next();
			if (tabelaResultado.getInt("quantidade_registro") > 0) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		} catch (Exception e) {
			throw e;
		}
	}

	public List<MatriculaVO> consultaRapidaPorMatriculaTurmaAnoSemestreDataVencimentoContaReceber(String tipoConsulta, Integer codUnidadeEnsino, String matricula, TurmaVO turma, String ano, String semestre, Date dataVencimentoInicio, Date dataVencimentoFinal, DiaSemana diaSemana, Boolean consultaContaVencida, String parcela, Optional<FiltroRelatorioFinanceiroVO> filtroRelatorioFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT distinct matricula.matricula, pessoa.codigo, trim(pessoa.nome) AS nome ");
		sqlStr.append(" FROM matricula ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ");
		sqlStr.append(" INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula");
		sqlStr.append(" INNER JOIN contaReceber ON contaReceber.matriculaAluno = matricula.matricula ");
		if (tipoConsulta.equals("turmaParcelaAnoSemestre")) {
			sqlStr.append(" INNER JOIN matriculaperiodovencimento mpv ON mpv.contaReceber = contareceber.codigo and mpv.matriculaperiodo = matriculaperiodo.codigo ");
		}
		sqlStr.append(" WHERE Matricula.matricula = MatriculaPeriodo.matricula AND Pessoa.codigo = Matricula.aluno");
		if (codUnidadeEnsino != null && codUnidadeEnsino != 0) {
			sqlStr.append(" and Matricula.unidadeEnsino = ").append(codUnidadeEnsino).append("");
		}
		if (!tipoConsulta.equals("turmaParcelaAnoSemestre") && Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append(" AND matricula.matricula = '").append(matricula).append("' ");
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			if(turma.getTurmaAgrupada()) {
				if (!turma.getTurmaAgrupadaVOs().isEmpty()) {
					boolean virgula = false;
					sqlStr.append(" AND matriculaPeriodo.turma IN(");
					for (TurmaAgrupadaVO turmaAgrupadaVO : turma.getTurmaAgrupadaVOs()) {

							if (!virgula) {
								sqlStr.append(turmaAgrupadaVO.getTurma().getCodigo());
							} else {
								sqlStr.append(", ").append(turmaAgrupadaVO.getTurma().getCodigo());
							}
							virgula = true;

					}
					sqlStr.append(") ");
				}else {
					sqlStr.append(" AND matriculaPeriodo.turma = ").append(turma.getCodigo());
				}

			}
			else {
				sqlStr.append(" AND matriculaPeriodo.turma = ").append(turma.getCodigo());
			}
		}
		if (!parcela.equals("") && !parcela.equals("TO")) {
			sqlStr.append(" AND contaReceber.parcela = '").append(parcela).append("' ");

		}
		if (tipoConsulta.equals("turmaParcelaAnoSemestre") && !ano.equals("")) {
			sqlStr.append(" AND matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		if (tipoConsulta.equals("turmaParcelaAnoSemestre") && !semestre.equals("")) {
			sqlStr.append(" AND matriculaPeriodo.semestre = '").append(semestre).append("' ");
		}

		if (!tipoConsulta.equals("turmaParcelaAnoSemestre") && !diaSemana.getValor().equals(DiaSemana.NENHUM.getValor())) {
			sqlStr.append(" AND TO_CHAR(contaReceber.dataVencimento, 'D') = '").append(diaSemana.getValor().replace("0", "")).append("' ");
		}
		sqlStr.append(" AND contareceber.situacao = 'AR' ");
		if (tipoConsulta.equals("turmaParcelaAnoSemestre")) {
			sqlStr.append(" AND mpv.situacao NOT IN('NG', 'GP', 'PP') ");
		}
		if (consultaContaVencida && dataVencimentoInicio != null && dataVencimentoFinal != null) {
			sqlStr.append(" AND ( contareceber.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataVencimentoInicio)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataVencimentoFinal)).append("' ) ");
		}else if (!consultaContaVencida && dataVencimentoInicio != null) {
			if(UteisData.getCompareData(new Date(), dataVencimentoInicio) > 0){
				dataVencimentoInicio = new Date();
			}
			sqlStr.append(" AND ( contareceber.datavencimento BETWEEN '").append(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataVencimentoInicio)).append("' AND '").append(Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataVencimentoFinal)).append("' ) ");
		}else if (!consultaContaVencida) {
			sqlStr.append(" AND contareceber.dataVencimento >= current_date ");
		}
		filtroRelatorioFinanceiroVO.ifPresent(filtro -> sqlStr.append(" AND ").append(adicionarFiltroTipoOrigemContaReceber(filtro, "contaReceber")));
		sqlStr.append(" AND contareceber.pagocomdcc = 'f'");
		sqlStr.append(" ORDER BY trim(pessoa.nome)");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			obj.setNovoObj(false);
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.getAluno().setCodigo(tabelaResultado.getInt("codigo"));
			obj.getAluno().setNome(tabelaResultado.getString("nome"));
			matriculaVOs.add(obj);
		}
		return matriculaVOs;
	}

	public List<MatriculaVO> consultaRapidaPorMatriculaListaUnidadeEnsino(String matricula, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (Matricula.matricula like('").append(matricula).append("%')) ");
		if (unidadeEnsinoVOs != null && !unidadeEnsinoVOs.isEmpty()) {
			sqlStr.append(" and UnidadeEnsino.codigo in(");
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					sqlStr.append(unidadeEnsinoVO.getCodigo()).append(", ");
				}
			}
			sqlStr.append("0) ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	private Boolean executarVerificacaoPessoaTipoAluno(Integer codigoPessoa) throws Exception{
	    return getFacadeFactory().getPessoaFacade().realizarVerificacaoPessoaTipoAluno(codigoPessoa);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAtivarMatriculaAlunoFormado(final MatriculaVO matriculaVO, final UsuarioVO usuarioVO) throws Exception {
		final StringBuilder sql = new StringBuilder();
		sql.append("UPDATE matricula SET situacao = 'AT', dataconclusaocurso = null, dataatualizacaomatriculaformada = null, ");
		sql.append("responsavelatualizacaomatriculaformada = null, anoconclusao = null, semestreconclusao = null, datacolacaograu = null  WHERE (matricula = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				sqlAlterar.setString(1, matriculaVO.getMatricula());
				return sqlAlterar;
			}
		});
		UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(matriculaVO.getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		if(Uteis.isAtributoPreenchido(usuario)) {
			JobExecutarSincronismoComLdapAoCancelarTransferirMatricula jobExecutarSincronismoComLdapAoCancelarTransferirMatricula = new JobExecutarSincronismoComLdapAoCancelarTransferirMatricula(usuario, matriculaVO, true,usuarioVO);
			Thread jobSincronizarCancelamento = new Thread(jobExecutarSincronismoComLdapAoCancelarTransferirMatricula);
			jobSincronizarCancelamento.start();
		}
		if (Uteis.isAtributoPreenchido(getAplicacaoControle().getConfiguracaoSeiBlackboardVO())) {
			PessoaEmailInstitucionalVO pessoaEmailInstitucionalVO =  getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(matriculaVO.getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			if(Uteis.isAtributoPreenchido(pessoaEmailInstitucionalVO)) {
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarAtivacaoPessoaBlack(pessoaEmailInstitucionalVO.getEmail(), usuarioVO);
			}		
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirObservacaoComplementarHistoricoAluno(final ObservacaoComplementarHistoricoAlunoVO obj, UsuarioVO usuario) throws Exception {
		try {
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					String sqlStr = "INSERT INTO observacaocomplementarhistoricoaluno (matricula, gradecurricular, observacao, observacaoTransferenciaMatrizCurricular) VALUES (?, ?, ?, ?)";
					PreparedStatement sqlInserir = arg0.prepareStatement(sqlStr);
					sqlInserir.setString(1, obj.getMatricula());
					sqlInserir.setInt(2, obj.getGradeCurricular());
					sqlInserir.setString(3, obj.getObservacao());
					sqlInserir.setString(4, obj.getObservacaoTransferenciaMatrizCurricular());
					return sqlInserir;
				}
			});
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirObservacaoComplementarHistoricoAluno(String matricula, Integer gradeCurricular, UsuarioVO usuario) throws Exception {
		try {
			String sql = "DELETE FROM observacaocomplementarhistoricoaluno WHERE ((matricula = ? and gradeCurricular=?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { matricula, gradeCurricular });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 *  *IMPORTANTE*
	 * O Metodo consultaRapidaBasicaPorCodigoPessoaAtivasMatriculasFilhoFuncionario deve seguir
	 * as mesmas regras do  consultaRapidaBasicaPorCodigoPessoaNaoCancelada
	 **/
	@Override
	public List<MatriculaVO> consultaRapidaBasicaPorCodigoPessoaAtivasMatriculasFilhoFuncionario(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica(true, true, Uteis.isAtributoPreenchido(usuario) ? usuario.getIsApresentarVisaoPais() : false);
		sqlStr.append(" WHERE (Pessoa.codigo = ").append(pessoa).append(") ");
		sqlStr.append(getWhereMatriculaLiberadasVisaoAlunoPais(true, true, true));		
		sqlStr.append(" ORDER BY ordemAnoSemestre, situacaoMatriculaOrdenar,  matricula.data desc ");	
		sqlStr.replace(sqlStr.indexOf("situacaoMatriculaOrdenar,"), sqlStr.indexOf("situacaoMatriculaOrdenar, ")+25, "situacaoMatriculaOrdenar, case when ((curso.periodicidade = 'AN' and extract(year from current_date)::varchar = matriculaperiodo.ano) or (curso.periodicidade = 'SE' and extract(year from current_date)::varchar = matriculaperiodo.ano and case when extract(month from current_date) > 7 then '2' else '1' end = matriculaperiodo.semestre) or (curso.periodicidade = 'IN')) then 0 else 1 end as ordemAnoSemestre,");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado, usuario, conf);
	}


	@Override
	public List<MatriculaVO> consultarPorCodigoAlunoDadosFichaAluno(Integer aluno, String filtroMatricula, ConfiguracaoGeralSistemaVO configuracaoGeralVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct pessoa.codigo, pessoa.nome, matricula.matricula, matricula.situacao, matricula.formaingresso, matricula.matriculaSuspensa, ");
		sb.append(" gradecurricular.nome AS \"gradecurricular.nome\", gradecurricular.codigo AS \"gradecurricular.codigo\", ");
		sb.append(" unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", ");
		sb.append(" curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", curso.periodicidade, ");
		sb.append(" turno.codigo AS \"turno.codigo\", turno.nome AS \"turno.nome\", matricula.data, matricula.bloqueioPorSolicitacaoLiberacaoMatricula , matriculaenade.codigo as \"matriculaenade.codigo\" ,	matriculaenade.dataenade as \"matriculaenade.dataenade\" ,	enade.tituloenade as \"enade.tituloenade\" ,");
		sb.append(" (case ");
		sb.append(" when matricula.situacao = 'AT' then '0' ");
		sb.append(" when matricula.situacao =  'PR' then '1'	");
		sb.append(" when matricula.situacao  in ('AC', 'TR', 'CA', 'FI', 'FO', 'JU', 'PC') then '2' ");
		sb.append(" when matricula.situacao in ('TS', 'TI', 'ER') then '3' ");
		sb.append(" end ||  ");
		sb.append(" case ");
		sb.append(" when curso.niveleducacional = 'MT' then '0' ");
		sb.append(" when curso.niveleducacional =  'PO' then '1'	");
		sb.append(" when curso.niveleducacional in ('SU', 'GT') then '2' ");
		sb.append(" when curso.niveleducacional = 'ME' then '3' ");
		sb.append(" when curso.niveleducacional = 'BA' then '4' ");
		sb.append(" when curso.niveleducacional = 'IN' then '5' ");
		sb.append(" when curso.niveleducacional = 'PR' then '6' ");
		sb.append(" when curso.niveleducacional in ('EX', 'SE') then '7' ");
		sb.append(" end) as situacaoMatriculaOrdenar ");
		sb.append(" from matricula ");
		sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sb.append(" inner join unidadeEnsino on unidadeensino.codigo = matricula.unidadeensino ");
		sb.append(" inner join curso on curso.codigo = matricula.curso ");
		sb.append(" inner join turno on turno.codigo = matricula.turno ");
		sb.append(" inner join gradecurricular on gradecurricular.codigo = matricula.gradecurricularatual ");
		sb.append(" left  join matriculaenade on matriculaenade.codigo = (select codigo from matriculaenade where matriculaenade.matricula = matricula.matricula order by codigo desc limit 1) ");
		sb.append(" left join enade on enade.codigo = matriculaenade.enade ");
		sb.append(" where matricula.aluno = ").append(aluno);
		if(Uteis.isAtributoPreenchido(filtroMatricula)){
			sb.append(" and matricula.matricula = '").append(filtroMatricula).append("' ");
		}
		sb.append(" order by situacaoMatriculaOrdenar");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<MatriculaVO> listaMatriculaVOs = new ArrayList<MatriculaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			obj.setNovoObj(false);
			obj.getAluno().setCodigo(tabelaResultado.getInt("codigo"));
			obj.getAluno().setNome(tabelaResultado.getString("nome"));
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.setSituacao(tabelaResultado.getString("situacao"));
			obj.setFormaIngresso(tabelaResultado.getString("formaIngresso"));
			obj.getGradeCurricularAtual().setCodigo(tabelaResultado.getInt("gradecurricular.codigo"));
			obj.getGradeCurricularAtual().setNome(tabelaResultado.getString("gradecurricular.nome"));

			obj.getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeEnsino.codigo"));
			obj.getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeEnsino.nome"));

			obj.getCurso().setCodigo(tabelaResultado.getInt("curso.codigo"));
			obj.getCurso().setNome(tabelaResultado.getString("curso.nome"));
			obj.getCurso().setPeriodicidade(tabelaResultado.getString("periodicidade"));

			obj.getTurno().setCodigo(tabelaResultado.getInt("turno.codigo"));
			obj.getTurno().setNome(tabelaResultado.getString("turno.nome"));
			obj.setMatriculaSuspensa(tabelaResultado.getBoolean("matriculaSuspensa"));
			obj.setBloqueioPorSolicitacaoLiberacaoMatricula(tabelaResultado.getBoolean("bloqueioPorSolicitacaoLiberacaoMatricula"));
			obj.getMatriculaEnadeVO().setCodigo(tabelaResultado.getInt("matriculaenade.codigo"));
			obj.getMatriculaEnadeVO().setDataEnade(tabelaResultado.getDate("matriculaenade.dataenade"));
			obj.getMatriculaEnadeVO().getEnadeVO().setTituloEnade(tabelaResultado.getString("enade.tituloenade"));
			verificarBloqueioMatricula(obj, usuarioVO, configuracaoGeralVO);
			obj.setMatriculaPeriodoVOs(getFacadeFactory().getMatriculaPeriodoFacade().consultarDadosFichaAlunoPorMatricula(obj, usuarioVO));
			Ordenacao.ordenarListaDecrescente(obj.getMatriculaPeriodoVOs(), "anoSemestreCodigo");
			listaMatriculaVOs.add(obj);
		}
		return listaMatriculaVOs;
	}

	@Override
	public List<MatriculaVO> inicializarDadosContaReceberAbaFinanceira(Integer aluno, List<MatriculaVO> listaMatriculaVOs, TipoOrigemContaReceber tipoOrigemContaReceber, SituacaoContaReceber situacaoContaReceber, String mesAno, UsuarioVO usuarioVO) throws Exception {
		List<MatriculaVO> listaMatriculaAbaFinanceiroVOs = new ArrayList<MatriculaVO>(0);
		for (MatriculaVO matriculaVO : listaMatriculaVOs) {
			MatriculaVO matriculaClonada = (MatriculaVO) matriculaVO.clone();
			matriculaClonada.setListaContaReceberVOs(getFacadeFactory().getContaReceberFacade().consultarPorMatriculaTipoOrigemSituacaoMesAnoFichaAluno(matriculaClonada.getMatricula(), tipoOrigemContaReceber, situacaoContaReceber, mesAno, usuarioVO));
			if (!matriculaClonada.getListaContaReceberVOs().isEmpty()) {
				listaMatriculaAbaFinanceiroVOs.add(matriculaClonada);
			}
		}
		return listaMatriculaAbaFinanceiroVOs;
	}

	@Override
	public List<SelectItem> inicializarDadosListaSelectItemMesAnoBaseadoContaReceber(Integer aluno, UsuarioVO usuarioVO) {
		List<SelectItem> itens = getFacadeFactory().getContaReceberFacade().consultarMesAnoContaReceberPorAluno(aluno, usuarioVO);
		return itens;
	}

	@Override
	public List<MatriculaVO> inicializarDadosRequerimentoFichaAluno(Integer aluno, List<MatriculaVO> listaMatriculaVOs, Integer tipoRequerimento, String situacao, String mesAno, UsuarioVO usuarioVO) throws Exception {
		List<MatriculaVO> listaMatriculaAbaFinanceiroVOs = new ArrayList<MatriculaVO>(0);
		for (MatriculaVO matriculaVO : listaMatriculaVOs) {
			MatriculaVO matriculaClonada = (MatriculaVO) matriculaVO.clone();
			matriculaClonada.setListaRequerimentoVOs(getFacadeFactory().getRequerimentoFacade().consultarPorMatriculaFichaAluno(matriculaClonada.getMatricula(), tipoRequerimento, situacao, mesAno, usuarioVO));
			if (!matriculaClonada.getListaRequerimentoVOs().isEmpty()) {
				listaMatriculaAbaFinanceiroVOs.add(matriculaClonada);
			}
		}
		return listaMatriculaAbaFinanceiroVOs;
	}

	@Override
	public List<SelectItem> inicializarDadosListaSelectItemMesAnoBaseadoRequerimento(Integer aluno, UsuarioVO usuarioVO) {
		List<SelectItem> itens = getFacadeFactory().getRequerimentoFacade().consultarMesAnoRequerimentoPorAlunoFichaAluno(aluno, usuarioVO);
		return itens;
	}

	@Override
	public Boolean consultarAlunoInadimplentePossuiBloqueioMatricula(String matricula, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT DISTINCT matriculaaluno FROM contareceber ");
		sql.append(" INNER JOIN matricula ON matricula.matricula = matriculaaluno ");
		sql.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino ");
		sql.append(" inner join configuracaoFinanceiro on configuracaoFinanceiro.configuracoes = (case when unidadeensino.configuracoes is not null then unidadeensino.configuracoes else (select c.codigo from configuracoes c where c.padrao = true ) end)");
		sql.append(" WHERE contareceber.situacao = 'AR' AND contareceber.tipoorigem IN ('MAT', 'MEN', 'NCR') ");
		sql.append(" AND configuracaofinanceiro.numerodiasbloquearacessoalunoinadimplente IS NOT NULL AND numerodiasbloquearacessoalunoinadimplente > 0 ");
		sql.append(" AND (matricula.liberarbloqueioalunoinadimplente IS NULL OR matricula.liberarbloqueioalunoinadimplente = false) ");
		sql.append(" AND contareceber.datavencimento < current_date AND DATE_PART('day', current_date - datavencimento) >= numerodiasbloquearacessoalunoinadimplente ");
		sql.append(" AND (matricula.matriculasuspensa = true) ");
		sql.append(" AND matricula.situacao IN ('AT', 'CO') ");
		sql.append(" AND matricula.matricula = '").append(matricula).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	@Override
	public Boolean consultarAlunoPossuiDocumentacaoPendente(String matricula,PersonalizacaoMensagemAutomaticaVO mensagemTemplate, ConfiguracaoGeralSistemaVO confEmail) throws Exception {
		StringBuilder sql = new StringBuilder("");

		sql.append(" SELECT  pessoa.codigo, pessoa.nome, pessoa.email, pessoa.email2, matricula.matricula, curso.nome as curso_nome, unidadeensino.nome as unidadeensino_nome,  ");
		sql.append(" ( ARRAY_TO_STRING( ARRAY(SELECT tipodocumento.nome from documetacaomatricula   ");
		sql.append("  inner join tipodocumento on tipodocumento.codigo = documetacaomatricula.tipodedocumento   ");
		sql.append("  where  documetacaomatricula.matricula = matricula.matricula   ");
		sql.append("  and documetacaomatricula.entregue = false), ';' )) AS documentos  ");
		sql.append("  from matricula  		 ");
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append(" inner join curso on curso.codigo = matricula.curso ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sql.append(" INNER JOIN configuracoes on   configuracoes.codigo = unidadeensino.configuracoes ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodo.codigo  = (select max(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula ) ");
		sql.append(" where configuracoes.controlarSuspensaoMatriculaPendenciaDocumentos ");
		sql.append(" and matricula.matricula = '").append(matricula).append("' ");
		sql.append(" and matricula.situacao = 'AT' and (matriculasuspensa  = false or  matriculasuspensa  is null ) ");
		sql.append(" and matricula.matricula in (select dm.matricula from documetacaomatricula dm where matricula.matricula = dm.matricula ");
		sql.append(" and dm.entregue = false and dm.gerarsuspensaomatricula = true limit 1) ");

		sql.append(" and (select horarioturmadia.data from horarioturmadia ");
		sql.append(" inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma ");
		sql.append(" where (case curso.periodicidade when 'SE' then horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre  ");
		sql.append(" when 'AN' then horarioturma.anovigente = matriculaperiodo.ano else true end )  ");
		sql.append(" and horarioturmadia.data >= matriculaperiodo.data ");
		sql.append(" and (horarioturma.turma = matriculaperiodo.turma or horarioturma.turma in (select turmaorigem from turmaagrupada where turmaagrupada.turma = matriculaperiodo.turma)) ");
		sql.append(" order by horarioturmadia.data desc limit 1) >= current_date ");

		sql.append(" and (select horarioturmadia.data from horarioturmadia");
		sql.append(" inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma ");
		sql.append(" where (case curso.periodicidade when 'SE' then horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre  ");
		sql.append(" when 'AN' then horarioturma.anovigente = matriculaperiodo.ano else true end )  ");
		sql.append(" and horarioturmadia.data >= matriculaperiodo.data ");
		sql.append(" and (horarioturma.turma = matriculaperiodo.turma or horarioturma.turma in (select turmaorigem from turmaagrupada where turmaagrupada.turma = matriculaperiodo.turma)) ");
		sql.append(" order by horarioturmadia.data limit 1) <= ");
		if (mensagemTemplate.getTemplateMensagemAutomaticaEnum().equals(TemplateMensagemAutomaticaEnum.MENSAGEM_1_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
			sql.append(" (current_date-nrDiasPrimeiroAvisoRiscoSuspensao )");
			sql.append(" and dataenvionotificacao1  is null and nrDiasPrimeiroAvisoRiscoSuspensao is not null and nrDiasPrimeiroAvisoRiscoSuspensao>0 ");
		} else if (mensagemTemplate.getTemplateMensagemAutomaticaEnum().equals(TemplateMensagemAutomaticaEnum.MENSAGEM_2_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
			sql.append(" (current_date-nrDiasSegundoAvisoRiscoSuspensao )");
			sql.append(" and dataenvionotificacao1  is not null  ");
			sql.append(" and dataenvionotificacao2  is null  ");
			sql.append(" and nrDiasSegundoAvisoRiscoSuspensao is not null and nrDiasSegundoAvisoRiscoSuspensao>0 ");
		} else if (mensagemTemplate.getTemplateMensagemAutomaticaEnum().equals(TemplateMensagemAutomaticaEnum.MENSAGEM_3_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
			sql.append(" (current_date-nrDiasTerceiroAvisoRiscoSuspensao )");
			sql.append(" and dataenvionotificacao2  is not null  ");
			sql.append(" and dataenvionotificacao3  is null  ");
			sql.append(" and nrDiasTerceiroAvisoRiscoSuspensao is not null and nrDiasTerceiroAvisoRiscoSuspensao>0 ");
		} else if (mensagemTemplate.getTemplateMensagemAutomaticaEnum().equals(TemplateMensagemAutomaticaEnum.MENSAGEM_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
			sql.append(" (current_date-nrDiasSuspenderMatriculaPendenciaDocumentos)");
			sql.append(" and dataenvionotificacao3  is not null  ");
			sql.append(" and nrDiasSuspenderMatriculaPendenciaDocumentos is not null and nrDiasSuspenderMatriculaPendenciaDocumentos>0 ");
		} else if (mensagemTemplate.getTemplateMensagemAutomaticaEnum().equals(TemplateMensagemAutomaticaEnum.MENSAGEM_4_SUSPENSAO_ALUNO_DOCUMENTACAO_PENDENTE)) {
			sql.append(" (current_date-nrDiasQuartoAvisoRiscoSuspensao )");
			sql.append(" and dataenvionotificacao3  is not null  ");
			sql.append(" AND ((dataenvionotificacao4  IS NULL ) OR  (current_date -periodicidadeQuartoAvisoRiscoSuspensao = dataenvionotificacao4))");
			sql.append(" and nrDiasTerceiroAvisoRiscoSuspensao is not null and nrDiasQuartoAvisoRiscoSuspensao > 0 ");
		}

		sql.append(" group by pessoa.codigo, pessoa.nome, pessoa.email, pessoa.email2, matricula.matricula, curso.nome, unidadeensino.nome ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	public Boolean consultarVerificandoMatriculaEstaAdiada(String matricula, UsuarioVO usuarioVO) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT distinct matricula.matricula ");
		sql.append(" FROM Matricula   ");
		sql.append(" WHERE matricula.matricula = '").append(matricula).append("' ");
		sql.append(" AND matricula.situacao not in ('CA', 'TR', 'AC', 'TS','TI') ");
		sql.append(" AND case when matricula.qtdDiasAdiarBloqueio is not null and matricula.qtdDiasAdiarBloqueio > 0 then ");
		sql.append(" (matricula.matriculasuspensa and (case when dataAdiamentoSuspensaoMatricula::DATE is not null then dataAdiamentoSuspensaoMatricula::DATE else matricula.dataBaseSuspensao end + matricula.qtdDiasAdiarBloqueio) >= current_date) ");
		sql.append(" else false end	");
		sql.append(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarDadosPendenciaLiberacaoMatricula(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuarioVO) throws Exception {
		if(matriculaVO.getBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira() || matriculaVO.getBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica()) {
			preencherDadosPendenciaLiberacaoMatricula(matriculaVO, usuarioVO);
			getFacadeFactory().getMatriculaFacade().realizarBloqueioMatricula(matriculaVO, usuarioVO);
			getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(matriculaVO.getUnidadeEnsino(), NivelMontarDados.BASICO, usuarioVO);
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoMatriculaPendenteAprovacaoAluno(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_MATRICULA_PENDENTE_APROVACAO_ALUNO, matriculaPeriodoVO, matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO);
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoMatriculaPendenteAprovacaoAprovador(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_MATRICULA_PENDENTE_APROVACAO_APROVADOR, matriculaPeriodoVO, matriculaVO.getBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira(),
					matriculaVO.getBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica(), matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void preencherDadosPendenciaLiberacaoMatricula(MatriculaVO matriculaVO,  UsuarioVO usuarioVO) throws Exception  {
		String motivoSuspensao = "";
		if(matriculaVO.getBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaFinanceira()) {
			PendenciaLiberacaoMatriculaVO pendenciaLiberacaoMatriculaVO = new PendenciaLiberacaoMatriculaVO();
			pendenciaLiberacaoMatriculaVO.getMatricula().setMatricula(matriculaVO.getMatricula());
			pendenciaLiberacaoMatriculaVO.setDataSolicitacao(new Date());
			pendenciaLiberacaoMatriculaVO.getUsuarioSolicitacao().setCodigo(usuarioVO.getCodigo());
			pendenciaLiberacaoMatriculaVO.setSituacao(SituacaoPendenciaLiberacaoMatriculaEnum.PENDENTE);
			pendenciaLiberacaoMatriculaVO.setMotivoSolicitacao(MotivoSolicitacaoLiberacaoMatriculaEnum.SOLICITAR_APROVACAO_LIBERACAO_FINANCEIRA);
			getFacadeFactory().getPendenciaLiberacaoMatriculaInterfaceFacade().persistir(pendenciaLiberacaoMatriculaVO, false, usuarioVO);
			motivoSuspensao = "Pendência liberação financeira matrícula";
		}
		if(matriculaVO.getBloqueioPorSolicitacaoLiberacaoMatriculaPendenciaAcademica()) {
			PendenciaLiberacaoMatriculaVO pendenciaLiberacaoMatriculaVO = new PendenciaLiberacaoMatriculaVO();
			pendenciaLiberacaoMatriculaVO.getMatricula().setMatricula(matriculaVO.getMatricula());
			pendenciaLiberacaoMatriculaVO.setDataSolicitacao(new Date());
			pendenciaLiberacaoMatriculaVO.getUsuarioSolicitacao().setCodigo(usuarioVO.getCodigo());
			pendenciaLiberacaoMatriculaVO.setSituacao(SituacaoPendenciaLiberacaoMatriculaEnum.PENDENTE);
			pendenciaLiberacaoMatriculaVO.setMotivoSolicitacao(MotivoSolicitacaoLiberacaoMatriculaEnum.SOLICITAR_LIBERACAO_MATRICULA_APOS_X_MODULOS);
			getFacadeFactory().getPendenciaLiberacaoMatriculaInterfaceFacade().persistir(pendenciaLiberacaoMatriculaVO, false, usuarioVO);
			if(motivoSuspensao.equals("")) {
				motivoSuspensao = "Pendência liberação matrícula após x módulos";
			}
			else {
				motivoSuspensao += " e Pendência liberação matrícula após x módulos";
			}
		}
		matriculaVO.setMotivoMatriculaBloqueada(motivoSuspensao);
		matriculaVO.setBloqueioPorSolicitacaoLiberacaoMatricula(Boolean.TRUE);
	}

	@Override
	public void realizarBloqueioMatricula(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
		matriculaVO.setMatriculaSuspensa(true);
		matriculaVO.setResponsavelSuspensaoMatricula(usuarioVO);
		if (matriculaVO.getMotivoMatriculaBloqueada().trim().equals("")) {
			throw new Exception("O campo MOTIVO SUSPENSÃO MATRÍCULA deve ser informado.");
		}
		if (matriculaVO.getResponsavelSuspensaoMatricula().getCodigo().equals(0)) {
			throw new Exception("O campo RESPONSÁVEL SUSPENSÃO MATRÍCULA deve ser informado.");
		}
		alterarMatriculaSuspensao(matriculaVO);
	}

	@Override
	public void realizarCancelamentoBloqueioMatricula(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
		try {
		matriculaVO.setResponsavelCancelamentoSuspensaoMatricula(usuarioVO);
		if (matriculaVO.getMotivoCancelamentoSuspensaoMatricula().trim().equals("")) {
			throw new Exception("O campo MOTIVO CANCELAMENTO SUSPENSÃO MATRÍCULA deve ser informado.");
		}
		if (matriculaVO.getResponsavelCancelamentoSuspensaoMatricula().getCodigo().equals(0)) {
			throw new Exception("O campo RESPONSÁVEL CANCELAMENTO SUSPENSÃO MATRÍCULA deve ser informado.");
		}
		matriculaVO.setMatriculaSuspensa(false);
		alterarMatriculaSuspensao(matriculaVO);
		}catch (Exception e) {
			matriculaVO.setMatriculaSuspensa(true);
			throw e;
		}
	}

	@Override
	public void realizarAdiamentoBloqueioMatricula(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
		matriculaVO.setResponsavelAdiamentoSuspensaoMatricula(usuarioVO);
		if (matriculaVO.getResponsavelAdiamentoSuspensaoMatricula().getCodigo().equals(0)) {
			throw new Exception("O campo RESPONSÁVEL ADIAMENTO SUSPENSÃO MATRÍCULA deve ser informado.");
		}
		if (matriculaVO.getQtdDiasAdiarBloqueio().equals(0)) {
			throw new Exception("O campo QUANT. DIAS ADIAR deve ser informado.");
		}
		if (matriculaVO.getMotivoAdiamentoSuspensaoMatricula().trim().equals("")) {
			throw new Exception("O campo MOTIVO ADIAMENTO SUSPENSÃO MATRÍCULA deve ser informado.");
		}
		alterarQuantidadeDiasAdiarSuspensaoMatricula(matriculaVO, usuarioVO);
	}

	@Override
	public void realizarLiberacaoBloqueioAlunoInadimplente(MatriculaVO matriculaVO, UsuarioVO usuarioVO) {
		matriculaVO.setLiberarBloqueioAlunoInadimplente(true);
		alterarLiberarBloqueioAlunoInadimplente(matriculaVO);
	}

	public void alterarQuantidadeDiasAdiarSuspensaoMatricula(final MatriculaVO obj, UsuarioVO usuarioVO) {
		final StringBuilder sql = new StringBuilder();
		sql.append("UPDATE Matricula set qtdDiasAdiarBloqueio=?, motivoAdiamentoSuspensaoMatricula=?, responsavelAdiamentoSuspensaoMatricula=?, dataAdiamentoSuspensaoMatricula=? WHERE ((matricula = ?))");

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				sqlAlterar.setInt(1, obj.getQtdDiasAdiarBloqueio());
				sqlAlterar.setString(2, obj.getMotivoAdiamentoSuspensaoMatricula());
				if (!obj.getResponsavelAdiamentoSuspensaoMatricula().getCodigo().equals(0)) {
					sqlAlterar.setInt(3, obj.getResponsavelAdiamentoSuspensaoMatricula().getCodigo());
				} else {
					sqlAlterar.setNull(3, 0);
				}
				sqlAlterar.setTimestamp(4, Uteis.getDataJDBCTimestamp(new Date()));
				sqlAlterar.setString(5, obj.getMatricula());
				return sqlAlterar;
			}
		});

	}

	@Override
	public List<MatriculaVO> inicializarDadosBibliotecaFichaAluno(Integer aluno, List<MatriculaVO> listaMatriculaVOs, String situacao, String mesAno, UsuarioVO usuarioVO) throws Exception {
		List<MatriculaVO> listaMatriculaAbaBibliotecaVOs = new ArrayList<MatriculaVO>(0);
		for (MatriculaVO matriculaVO : listaMatriculaVOs) {
			MatriculaVO matriculaClonada = (MatriculaVO) matriculaVO.clone();
			matriculaClonada.setListaItemEmprestimoVOs(getFacadeFactory().getItemEmprestimoFacade().consultarPorMatriculaFichaAluno(matriculaClonada.getMatricula(), situacao, mesAno, usuarioVO));
			if (!matriculaClonada.getListaItemEmprestimoVOs().isEmpty()) {
				listaMatriculaAbaBibliotecaVOs.add(matriculaClonada);
			}
		}
		return listaMatriculaAbaBibliotecaVOs;
	}

	@Override
	public List<SelectItem> inicializarDadosListaSelectItemMesAnoBaseadoItemEmprestimo(Integer aluno, UsuarioVO usuarioVO) {
		List<SelectItem> itens = getFacadeFactory().getItemEmprestimoFacade().consultarMesAnoItemEmprestimoPorAlunoFichaAluno(aluno, usuarioVO);
		return itens;
	}


	@Override
	public List<InteracaoWorkflowHistoricoVO> inicializarDadosCRMFichaAluno(Integer aluno, Integer responsavel, String mesAno, UsuarioVO usuarioVO) throws Exception {
		List<InteracaoWorkflowHistoricoVO> listaInteracaoWorkflowHistoricoVOs = new ArrayList<InteracaoWorkflowHistoricoVO>(0);
		listaInteracaoWorkflowHistoricoVOs.addAll(getFacadeFactory().getFollowUpFacade().consultarInteracaoWorkflowPorAlunoFichaAluno(aluno, responsavel, mesAno, usuarioVO));
		listaInteracaoWorkflowHistoricoVOs.addAll(getFacadeFactory().getFollowUpFacade().consultarHistoricoFollowUpPorAlunoFichaAluno(aluno, responsavel, mesAno, usuarioVO));
		Ordenacao.ordenarListaDecrescente(listaInteracaoWorkflowHistoricoVOs, "ordenacao");
		return listaInteracaoWorkflowHistoricoVOs;
	}

	@Override
	public List<SelectItem> inicializarDadosListaSelectItemMesAnoBaseadoInteracaoWorkflow(Integer aluno, UsuarioVO usuarioVO) {
		List<SelectItem> itens = getFacadeFactory().getFollowUpFacade().consultarMesAnoInteracaoWorkflowPorAlunoFichaAluno(aluno, usuarioVO);
		return itens;
	}

	@Override
	public List<SelectItem> inicializarDadosListaSelectItemMesAnoBaseadoComunicacaoInterna(Integer aluno, UsuarioVO usuarioVO) {
		List<SelectItem> itens = getFacadeFactory().getComunicacaoInternaFacade().consultarMesAnoComunicacaoInternaPorAlunoFichaAluno(aluno, usuarioVO);
		return itens;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataEmissaoHistorico(final String matricula, final Date data, UsuarioVO usuario, Boolean controlarAcesso) throws Exception {
		try {
			ExpedicaoDiploma.alterar(getIdEntidade(), controlarAcesso, usuario);
			final String sql = "UPDATE matricula set dataEmissaoHistorico=? WHERE ((matricula = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(data));
					sqlAlterar.setString(2, matricula);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public MatriculaVO consultarDataAdiamentoSuspensaoMatricula(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
		final String sql = "select dataadiamentosuspensaomatricula, qtdDiasAdiarBloqueio, motivomatriculabloqueada, responsaveladiamentosuspensaomatricula, motivoadiamentosuspensaomatricula, usuario.nome as responsaveladiamentosuspensaomatricula_nome  from matricula left join usuario on usuario.codigo = responsaveladiamentosuspensaomatricula where matricula = '" + matriculaVO.getMatricula() + "'";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (dadosSQL.next()) {
			matriculaVO.setDataAdiamentoSuspensaoMatricula(dadosSQL.getDate("dataadiamentosuspensaomatricula"));
			matriculaVO.setQtdDiasAdiarBloqueio(dadosSQL.getInt("qtdDiasAdiarBloqueio"));
			matriculaVO.setMotivoAdiamentoSuspensaoMatricula(dadosSQL.getString("motivoadiamentosuspensaomatricula"));
			matriculaVO.setMotivoMatriculaBloqueada(dadosSQL.getString("motivomatriculabloqueada"));
			matriculaVO.getResponsavelAdiamentoSuspensaoMatricula().setCodigo(dadosSQL.getInt("responsaveladiamentosuspensaomatricula"));
			matriculaVO.getResponsavelAdiamentoSuspensaoMatricula().setNome(dadosSQL.getString("responsaveladiamentosuspensaomatricula_nome"));

		}
		return matriculaVO;
	}

	public List<MatriculaVO> obterStatusMatricula(Integer unidadeEnsino) throws Exception {
		final String sql = "select matricula.matricula, matricula.situacao, pessoa.nome as nomeConsultor, negociacaorecebimento.data as datapagamento, contareceber.situacao as situacaoparcela from matricula "
				+ " left join funcionario on funcionario.codigo = matricula.consultor "
				+ " left join pessoa on pessoa.codigo = funcionario.pessoa "
				+ " left join contareceber on contareceber.matriculaaluno = matricula.matricula and contareceber.tipoorigem = 'MAT' "
				+ " left join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo "
				+ " left join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento "
				+ " where matricula.unidadeensino = " + unidadeEnsino
				+ " and negociacaorecebimento.data >= (now() - '24 hour'::interval) "

				+ " order by matricula.unidadeensino, matricula.matricula, contareceber.codigo desc ";
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sql);
		List<MatriculaVO> listaMatriculaVOs = new ArrayList<MatriculaVO>(0);
		while (dadosSQL.next()) {
			MatriculaVO matriculaVO = new MatriculaVO();
			matriculaVO.setSituacao(dadosSQL.getString("situacao"));
			matriculaVO.getConsultor().getPessoa().setNome(dadosSQL.getString("nomeConsultor"));
			matriculaVO.setDataPagamento(dadosSQL.getDate("dataPagamento"));
			matriculaVO.setSituacaoParcela(dadosSQL.getString("situacaoParcela"));
			matriculaVO.setMatricula(dadosSQL.getString("matricula"));
			//matriculaVO.setSituacaoPagamento(dadosSQL.getString("dataPagamento"));
			listaMatriculaVOs.add(matriculaVO);
		}
		return listaMatriculaVOs;
	}

	 @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	 public void alterarPlanoFinanceiroAlunoConformeDadosTurma(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, PlanoFinanceiroCursoVO planoFinanceiroCursoVO, String categoria, CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception{

		 try {
			 carregarDados(matriculaVO, usuarioVO);
			 condicaoPagamentoPlanoFinanceiroCursoVO = getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(condicaoPagamentoPlanoFinanceiroCursoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			 matriculaPeriodoVO.setPlanoFinanceiroCurso(planoFinanceiroCursoVO);
			 matriculaPeriodoVO.setCategoriaCondicaoPagamento(categoria);
			 matriculaPeriodoVO.setCondicaoPagamentoPlanoFinanceiroCurso(condicaoPagamentoPlanoFinanceiroCursoVO);
			 inicializarPlanoFinanceiroMatriculaPeriodo(matriculaVO, matriculaPeriodoVO, usuarioVO);
			 matriculaVO.setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(matriculaPeriodoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
			 matriculaPeriodoVO.setProcessoMatriculaCalendarioVO(getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getCurso().getCodigo(), matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getTurno().getCodigo(), matriculaPeriodoVO.getProcessoMatriculaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
			 realizarCalculoValoresMatriculaMensalidadePeriodoAluno(matriculaVO, matriculaPeriodoVO, configuracaoFinanceiroVO, usuarioVO);
			 getFacadeFactory().getMatriculaPeriodoFacade().alterar(matriculaPeriodoVO, matriculaVO, matriculaPeriodoVO.getProcessoMatriculaCalendarioVO(), configuracaoFinanceiroVO, usuarioVO);
			 matriculaVO.getPlanoFinanceiroAluno().setCondicaoPagamentoPlanoFinanceiroCursoVO(matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso());
			 matriculaVO.getPlanoFinanceiroAluno().setPlanoFinanceiroCursoVO(matriculaPeriodoVO.getPlanoFinanceiroCurso());
			 getFacadeFactory().getPlanoFinanceiroAlunoFacade().persistirLevandoEmContaMatriculaPeriodo(matriculaVO.getPlanoFinanceiroAluno(), matriculaPeriodoVO.getCodigo());
		  }catch (Exception e) {
			  throw new ConsistirException("Erro ao salvar matricula:"+e.getMessage());
		  }
	 }

	 @Override
		public void cancelarIndiceReajustePreco(MatriculaVO matriculaVO, UsuarioVO usuarioVO) throws Exception {
			if (matriculaVO.getMotivoCancelamentoReajustePreco().equals("")) {
				throw new Exception("O campo MOTIVO DE CANCELAMENTO deve ser informado!");
			}
			alterarDadosCancelamentoReajustePreco(matriculaVO.getMatricula(), matriculaVO.getMotivoCancelamentoReajustePreco(), matriculaVO.getDataCancelamentoReajustePreco(), matriculaVO.getResponsavelCancelamentoReajustePreco().getCodigo(), usuarioVO);
		}

		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
		public void alterarDadosCancelamentoReajustePreco(String matricula, String motivoCancelamentoReajustePreco, Date dataCancelamentoReajustePreco, Integer responsavelCancelamentoReajustePreco, UsuarioVO usuarioVO) throws Exception {
			String sqlStr = "UPDATE Matricula set motivoCancelamentoReajustePreco=?, dataCancelamentoReajustePreco=?, responsavelCancelamentoReajustePreco=? WHERE ((matricula = ?))";
			getConexao().getJdbcTemplate().update(sqlStr, new Object[] { motivoCancelamentoReajustePreco, dataCancelamentoReajustePreco, responsavelCancelamentoReajustePreco,  matricula });
		}

		@Override
		public void removerVinculoAutorizacaoCursoMatricula(Integer autorizacaocurso, UsuarioVO usuarioVO, boolean controlarAcesso) throws Exception {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("UPDATE Matricula SET autorizacaocurso = (CASE WHEN matricula.autorizacaocurso = ").append(autorizacaocurso).append(" THEN null ELSE matricula.autorizacaocurso END),");
			sqlStr.append("renovacaoreconhecimento=(CASE WHEN matricula.renovacaoreconhecimento = ").append(autorizacaocurso).append(" THEN null ELSE matricula.renovacaoreconhecimento END) ");
			sqlStr.append("WHERE autorizacaocurso = ").append(autorizacaocurso).append(" OR ").append("renovacaoreconhecimento = ").append(autorizacaocurso);
			getConexao().getJdbcTemplate().update(sqlStr.toString());
		}

		public SqlRowSet consultarPessoaLoginToken(String token) {

			StringBuilder sb = new StringBuilder();
			sb.append(" select pessoa.cpf, pessoa.nome, pessoa.email, usuario.senha  from pessoa ");
			sb.append(" inner join usuario on usuario.pessoa = pessoa.codigo ");
			sb.append(" where encode(digest(pessoa.codigo::text, 'sha256'::text), 'hex') = ")
			.append(" '").append(token).append("'");

			return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		}

		public String consultarTokenPessoaPorMatricula(String matricula) {

			StringBuilder sb = new StringBuilder();
			sb.append(" select encode(digest(pessoa.codigo::text, 'sha256'::text), 'hex') as token from matricula ");
			sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
			sb.append(" inner join usuario on usuario.pessoa = pessoa.codigo ");
			sb.append(" where matricula.matricula = ")
			.append(" '").append(matricula).append("'");

			SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			if (dadosSQL.next()) {
				return dadosSQL.getString("token");
			} else {
				return "";
			}

		}

		public String consultarTokenPessoaPorCodigo(Integer codPessoa) {

			StringBuilder sb = new StringBuilder();
			sb.append(" select encode(digest(pessoa.codigo::text, 'sha256'::text), 'hex') as token from pessoa ");
			sb.append(" inner join usuario on usuario.pessoa = pessoa.codigo ");
			sb.append(" where pessoa.codigo = ")
			.append(" ").append(codPessoa).append("");

			SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			if (dadosSQL.next()) {
				return dadosSQL.getString("token");
			} else {
				return "";
			}

		}

		@Override
		public List<MatriculaVO> consultaRapidaPorCodigoPessoaESituacao(Integer pessoa, String situacoesSqlIn, String matriculaIgnorar, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuffer sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE (Pessoa.codigo = ").append(pessoa).append(") ");
			if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
				sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
			}
			if(Uteis.isAtributoPreenchido(matriculaIgnorar)) {
				sqlStr.append(" and matricula.matricula != '").append(matriculaIgnorar).append("' ");
			}
			if(Uteis.isAtributoPreenchido(situacoesSqlIn)) {
				sqlStr.append(" and matricula.situacao in ( ").append(situacoesSqlIn).append(") ");
			}
			sqlStr.append(" ORDER BY matricula ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaBasica(tabelaResultado);
		}

		@Override
		public String consultarGruposLdapPorPessoa(Integer pessoa) throws Exception {
			String sqlStr = "select coalesce(array_to_string(array_agg(distinct c.nome),','),'') cursos from matricula m inner join curso c on c.codigo = m.curso where m.aluno = ? and m.situacao = 'AT'";
			SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] {pessoa});
			if (dadosSQL.next()) {
				return dadosSQL.getString("cursos");
			}
			return "";
		}

		@Override
		public String consultarPossuiFormacaoAcademicaVinculadaMatricula(Integer codigoFormacaoAcademica, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sb = new StringBuilder();
			sb.append(" SELECT matricula FROM matricula WHERE matricula.formacaoacademica =  ").append(codigoFormacaoAcademica);
			SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			if (dadosSQL.next()) {
				return dadosSQL.getString("matricula");
			} else {
				return "";
			}

		}
		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
		@Override
		public void alterarDadosMatriculaDeferimentoIndeferimento(MatriculaVO matriculaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
			//Matricula.incluir(getIdEntidade(), validarAcesso, usuarioVO);
			alterar(matriculaVO, "matricula",
					new AtributoPersistencia().add("tipoTrabalhoConclusaoCurso", matriculaVO.getTipoTrabalhoConclusaoCurso())
					.add("notaMonografia", matriculaVO.getNotaMonografia())
					.add("nomemonografia", matriculaVO.getTituloMonografia())
					.add("orientadorMonografia", matriculaVO.getOrientadorMonografia()),
					new AtributoPersistencia().add("matricula", matriculaVO.getMatricula()), usuarioVO);
		}

		public List<MatriculaVO> consultaRapidaPorNomePessoa(DataModelo dataModeloAluno, boolean controlarAcesso, String situacao, String nivelEducacional, Integer curso, UsuarioVO usuario) throws Exception {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);			
			StringBuilder sqlStr = new StringBuilder(" SELECT count(*) over() as qtde_total_registros, * FROM (");
		    sqlStr.append( getSQLPadraoConsultaBasicaStringBuilder());
		    sqlStr.append(" WHERE sem_acentos(Pessoa.nome) ilike sem_acentos(?) ");

			if(Uteis.isAtributoPreenchido(nivelEducacional)) {
				sqlStr.append(" AND (Curso.niveleducacional = '").append(nivelEducacional).append("')");
			}

			if (dataModeloAluno.getUnidadeEnsinoVO().getCodigo().intValue() != 0 && dataModeloAluno.getUnidadeEnsinoVO().getCodigo() != null) {
				sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(dataModeloAluno.getUnidadeEnsinoVO().getCodigo().intValue()).append(") ");
			}
			if (situacao != null && !situacao.equals("")) {
				sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
			}
			if (Uteis.isAtributoPreenchido(curso)) {
				sqlStr.append(" AND Curso.codigo = ").append(curso).append(" ");
			}
			sqlStr.append(" ) AS t ");
			sqlStr.append(" ORDER BY \"Pessoa.nome\" ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModeloAluno.getLimitePorPagina(), dataModeloAluno.getOffset());
			
			dataModeloAluno.setTotalRegistrosEncontrados(0);		
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(),  dataModeloAluno.getValorConsulta() +PERCENT);
			if (tabelaResultado.next()) {
				dataModeloAluno.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
			}
			tabelaResultado.beforeFirst();
			return montarDadosConsultaBasica(tabelaResultado);
			
		}


		public Integer consultaRapidaTotalPorNomePessoa(DataModelo dataModeloAluno, boolean controlarAcesso, String situacao, String nivelEducacional, Integer curso, UsuarioVO usuario) throws Exception {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoTotalConsultaBasica(false);
			sqlStr.append(" WHERE sem_acentos(Pessoa.nome) ilike sem_acentos(?) ");

			if(Uteis.isAtributoPreenchido(nivelEducacional)) {
				sqlStr.append(" AND (Curso.niveleducacional = '").append(nivelEducacional).append("')");
			}

			if (dataModeloAluno.getUnidadeEnsinoVO().getCodigo().intValue() != 0 && dataModeloAluno.getUnidadeEnsinoVO().getCodigo() != null) {
				sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(dataModeloAluno.getUnidadeEnsinoVO().getCodigo().intValue()).append(") ");
			}
			if (situacao != null && !situacao.equals("")) {
				sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
			}
			if (Uteis.isAtributoPreenchido(curso)) {
				sqlStr.append(" AND Curso.codigo = ").append(curso).append(" ");
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModeloAluno.getValorConsulta() +"%");
			return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
		}

		public List<String> consultarMatriculasPorCodigoPessoa(Integer pessoa) throws Exception {
			String sqlStr = "SELECT matricula FROM Matricula, Pessoa WHERE pessoa.codigo = matricula.aluno and pessoa.codigo = " + pessoa.intValue() + " order by matricula";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
			List<String> matriculas = new ArrayList<String>();
			while (tabelaResultado.next()) {

				MatriculaVO obj = new MatriculaVO();
				obj.setNovoObj(false);
				obj.setMatricula(tabelaResultado.getString("matricula"));
				matriculas.add(tabelaResultado.getString("matricula"));
			}
			return matriculas;
		}

		@Override
		public List<MatriculaVO> consultaRapidaPorDataModeloUnidadeEnsinoBiblioteca(DataModelo dataModelo, List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, String situacao, UsuarioVO usuario) throws Exception {
			StringBuilder sql = new StringBuilder(getSQLPadraoConsultaBasica().toString());
			dataModelo.getListaFiltros().clear();
			dataModelo.setLimitePorPagina(10);
			sql.append(" WHERE 1 = 1");
			switch (dataModelo.getCampoConsulta()) {
			case "matricula":
				sql.append(" and Matricula.matricula like(?) ");
				dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
				break;
			case "nome":
				sql.append(" and sem_acentos(Pessoa.nome) ilike(sem_acentos(?)) ");
				dataModelo.getListaFiltros().add(dataModelo.getValorConsulta() + PERCENT);
				break;
			case "cpf":
				sql.append(" and pessoa.cpf ilike(?) ");
				dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
				break;

			default:
				break;
			}
			montarSqlUnidadeEnsinoBiblioteca(unidadeEnsinoBibliotecaVOs, sql);
			if (situacao != null && !situacao.equals("")) {
				sql.append(" AND (Matricula.situacao = ?");
				dataModelo.getListaFiltros().add(situacao);
			}

			sql.append(" ORDER BY Pessoa.nome ");
			UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsultaBasica(tabelaResultado);
		}

		@Override
		public Integer consultaTotalRapidaPorDataModeloUnidadeEnsinoBiblioteca(DataModelo dataModelo,
				List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs, String situacao, UsuarioVO usuario) throws Exception {
			StringBuilder sql = new StringBuilder(getSQLPadraoTotalConsultaBasica(false).toString());
			dataModelo.getListaFiltros().clear();
			dataModelo.setLimitePorPagina(10);
			sql.append(" WHERE 1 = 1");
			switch (dataModelo.getCampoConsulta()) {
			case "matricula":
				sql.append(" and Matricula.matricula like(?) ");
				dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
				break;
			case "nome":
				sql.append(" and sem_acentos(Pessoa.nome) ilike(sem_acentos(?)) ");
				dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
				break;
			case "cpf":
				sql.append(" and pessoa.cpf ilike(?) ");
				dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
				break;

			default:
				break;
			}
			montarSqlUnidadeEnsinoBiblioteca(unidadeEnsinoBibliotecaVOs, sql);
			if (situacao != null && !situacao.equals("")) {
				sql.append(" AND (Matricula.situacao = ?");
				dataModelo.getListaFiltros().add(situacao);
			}

			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
		}

		private void montarSqlUnidadeEnsinoBiblioteca(List<UnidadeEnsinoBibliotecaVO> unidadeEnsinoBibliotecaVOs,
				StringBuilder sql) {
			if (unidadeEnsinoBibliotecaVOs != null && !unidadeEnsinoBibliotecaVOs.isEmpty()) {
				boolean virgula = false;
				sql.append(" AND matricula.unidadeEnsino in(");
				for (UnidadeEnsinoBibliotecaVO unidadeEnsinoBiblioteca : unidadeEnsinoBibliotecaVOs) {
					if (!virgula) {
						sql.append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
					} else {
						sql.append(", ").append(unidadeEnsinoBiblioteca.getUnidadeEnsino().getCodigo());
					}
					virgula = true;
				}
				sql.append(") ");
			}
		}

		@Override
		public List<MatriculaVO> consultarAlunoParticipaQuestaoOnline(Integer questao, UsuarioVO usuarioVO) {
			StringBuilder sb = new StringBuilder();
			sb.append("select distinct matricula.matricula, pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", matricula.situacao, curso.codigo as \"curso.codigo\", curso.nome as \"curso.nome\", ");
			sb.append(" avaliacaoonlinematricula.nota as notaAvaliacao, avaliacaoonlinematricula.quantidadeacertos, avaliacaoonlinematricula.quantidadeerros, ");
			sb.append(" avaliacaoonlinematricula.percentualacerto, avaliacaoonlinematricula.percentualerro, avaliacaoonlinematriculaquestao.situacaoatividaderesposta, avaliacaoonline.codigo AS \"avaliacaoonline.codigo\", avaliacaoonline.nome AS nomeAvaliacaoOnline ");
			sb.append(" from matricula ");
			sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
			sb.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
			sb.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula ");
			sb.append(" and mp.situacaomatriculaperiodo in('AT')  order by mp.ano || mp.semestre desc, mp.codigo desc limit 1) ");
			sb.append(" inner join matriculaperiodoturmadisciplina mptd on mptd.matricula = matricula.matricula ");
			sb.append(" inner join questao on questao.disciplina = mptd.disciplina and questao.codigo = ").append(questao);
			sb.append(" inner join disciplina on disciplina.codigo = questao.disciplina ");
			sb.append(" inner join historico on historico.matriculaperiodoturmadisciplina = mptd.codigo ");
			sb.append(" inner join configuracaoacademico on configuracaoacademico.codigo = historico.configuracaoacademico ");
			sb.append(" inner join curso on curso.codigo = matricula.curso ");
			sb.append(" inner join avaliacaoonlinematricula on avaliacaoonlinematricula.matricula = matricula.matricula ");
			sb.append(" inner join avaliacaoonline on avaliacaoonline.codigo = avaliacaoonlinematricula.avaliacaoonline");
			sb.append(" inner join avaliacaoonlinematriculaquestao on avaliacaoonlinematriculaquestao.avaliacaoonlinematricula = avaliacaoonlinematricula.codigo ");
			sb.append(" and avaliacaoonlinematriculaquestao.questao = ").append(questao);
			sb.append(" where 1=1 ");
			sb.append(" and matricula.situacao in('AT') ");
			sb.append(" and case when curso.periodicidade = 'SE' then matriculaperiodo.ano = '").append(Uteis.getAnoDataAtual4Digitos()).append("' ");
			sb.append(" and matriculaperiodo.semestre = '").append(Uteis.getSemestreAtual()).append("' ");
			sb.append(" when curso.periodicidade = 'AN' then matriculaperiodo.ano = '").append(Uteis.getAnoDataAtual4Digitos()).append("' else true end ");
			sb.append(" and (mptd.modalidadedisciplina = 'ON_LINE' or (mptd.modalidadedisciplina = 'PRESENCIAL' and configuracaoacademico.utilizarapoioeadparadisciplinasmodalidadepresencial)) ");
			sb.append(" order by nomeAvaliacaoOnline, pessoa.nome");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			List<MatriculaVO> listaMatriculaVOs = new ArrayList<MatriculaVO>(0);
			while (tabelaResultado.next()) {
				MatriculaVO obj = new MatriculaVO();
				obj.setMatricula(tabelaResultado.getString("matricula"));
				obj.getAluno().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
				obj.getAluno().setNome(tabelaResultado.getString("pessoa.nome"));
				obj.setSituacao(tabelaResultado.getString("situacao"));
				obj.getCurso().setCodigo(tabelaResultado.getInt("curso.codigo"));
				obj.getCurso().setNome(tabelaResultado.getString("curso.nome"));
				obj.getAvaliacaoOnlineMatriculaVO().setNota(tabelaResultado.getDouble("notaAvaliacao"));
				obj.getAvaliacaoOnlineMatriculaVO().setQuantidadeAcertos(tabelaResultado.getInt("quantidadeacertos"));
				obj.getAvaliacaoOnlineMatriculaVO().setQuantidadeErros(tabelaResultado.getInt("quantidadeErros"));
				obj.getAvaliacaoOnlineMatriculaVO().setPercentualAcerto(tabelaResultado.getDouble("percentualacerto"));
				obj.getAvaliacaoOnlineMatriculaVO().setPercentualErro(tabelaResultado.getDouble("percentualerro"));
				obj.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().setNome(tabelaResultado.getString("nomeAvaliacaoOnline"));
				obj.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().setCodigo(tabelaResultado.getInt("avaliacaoOnline.codigo"));
				if (tabelaResultado.getString("situacaoatividaderesposta") != null) {
					obj.setSituacaoAtividadeRespostaQuestao(SituacaoAtividadeRespostaEnum.valueOf(tabelaResultado.getString("situacaoatividaderesposta")));
				}
				listaMatriculaVOs.add(obj);
			}
			return listaMatriculaVOs;
		}


		@Override
		public List<MatriculaVO> consultaRapidaPorTurmaIntegralUnidadeEnsino(TurmaVO turmaVO, boolean controlarAcesso,  UsuarioVO usuario) throws Exception {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuffer sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");
			sqlStr.append("  AND  matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' order by ano desc, semestre desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1) ");
			sqlStr.append(" INNER JOIN turma 			ON matriculaperiodo.turma = turma.codigo ");
			sqlStr.append(" WHERE turma.semestral = false AND turma.anual = false  ");
			sqlStr.append(" AND (Matricula.unidadeensino = ").append(turmaVO.getUnidadeEnsino().getCodigo().intValue()).append(") ");
			sqlStr.append(" AND (turma.codigo = ").append(turmaVO.getCodigo().intValue()).append(") ");
			sqlStr.append(" ORDER BY Pessoa.nome ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaBasica(tabelaResultado);
		}

		@Override
		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
		public void alterarUnidadeEnsinoTurmaBase (TurmaVO turmaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
			 final StringBuilder sqlStr = new StringBuilder();
			 sqlStr.append("UPDATE matricula SET unidadeensino = t.unidadeensino FROM ( ");
			 sqlStr.append("SELECT ");
			 sqlStr.append(" matricula.matricula,turma.unidadeensino  ");
			 sqlStr.append("FROM matricula ");
			 sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.matricula = matricula.matricula ");
			 sqlStr.append(" INNER JOIN turma      		 ON matriculaperiodo.turma     = turma.codigo ");
			 sqlStr.append("WHERE turma.codigo = ? ");
			 sqlStr.append(") AS t WHERE matricula.matricula = t.matricula AND matricula.unidadeensino <> t.unidadeensino;");
			 sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(turmaVO.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			});
		}


		@Override
		public void consultarMatriculas(DataModelo dataModelo, UsuarioVO usuario) throws Exception {
			if (dataModelo == null) {
				dataModelo = new DataModelo();
				dataModelo.preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			}
			dataModelo.setListaConsulta(consultarPorNomePessoa(dataModelo, false, usuario));
			dataModelo.setTotalRegistrosEncontrados(consultarTotalMatriculaPorNome(dataModelo));
		}

		/**
		 * Consulta as matricular por nome {@link PessoaVO} utilizando o limit e offset
		 * do {@link DataModelo}.
		 *
		 * @param dataModelo
		 * @param unidadeEnsino
		 * @param controlarAcesso
		 * @param nivelMontarDados
		 * @param usuario
		 * @return
		 * @throws Exception
		 */
		public List<MatriculaVO> consultarPorNomePessoa(DataModelo dataModelo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
			StringBuilder sql = new StringBuilder();
			sql.append(" select pessoa.codigo, pessoa.nome, matricula.matricula, pessoa.cpf from pessoa ");
			sql.append(" left join matricula on matricula.aluno = pessoa.codigo");
			sql.append(" where lower(sem_acentos(pessoa.nome)) like lower((sem_acentos(?))) order by pessoa.nome ");

			UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getValorConsulta()+"%");
			List<MatriculaVO> matriculas = new ArrayList<>();
			MatriculaVO matriculaVO = null;
			while(tabelaResultado.next()) {
				matriculaVO = new MatriculaVO();
				matriculaVO.getAluno().setCodigo(tabelaResultado.getInt("codigo"));
				matriculaVO.getAluno().setNome(tabelaResultado.getString("nome"));
				matriculaVO.setMatricula(tabelaResultado.getString("matricula"));
				matriculaVO.getAluno().setCPF(tabelaResultado.getString("cpf"));

				matriculas.add(matriculaVO);
			}

			return matriculas;
		}

		/**
		 * Consultar total de aluno por nome
		 * @param dataModelo
		 * @param unidadeEnsino
		 * @return
		 */
		private Integer consultarTotalMatriculaPorNome(DataModelo dataModelo) {
	        StringBuilder sql = new StringBuilder();
	        sql.append(" select count(pessoa.codigo) as qtde from pessoa ");
			sql.append(" left join matricula on matricula.aluno = pessoa.codigo");
			sql.append(" where lower(sem_acentos(pessoa.nome)) like lower((sem_acentos(?))) ");

	        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getValorConsulta()+"%");
	        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	    }

		private StringBuilder getSQLPadraoTotalConsultaCompleta() {
			StringBuilder str = new StringBuilder();
			str.append(" SELECT COUNT(matricula.matricula) as qtde ");
			str.append(" FROM Matricula ");
			str.append("      LEFT JOIN Pessoa ON (Matricula.aluno = pessoa.codigo) ");
			str.append("      LEFT JOIN cidade naturalidade ON pessoa.naturalidade = naturalidade.codigo ");
			str.append("      LEFT JOIN paiz ON pessoa.nacionalidade = paiz.codigo ");
			str.append("      LEFT JOIN UnidadeEnsino ON (Matricula.unidadeEnsino = unidadeEnsino.codigo) ");
			str.append("      LEFT JOIN cidade cidadeUnidadeEnsino ON (cidadeUnidadeEnsino.codigo = unidadeEnsino.cidade) ");
			str.append("      LEFT JOIN Curso ON (Matricula.curso = curso.codigo) ");
			str.append("      LEFT JOIN MatriculaPeriodo ON (MatriculaPeriodo.matricula = Matricula.matricula) ");
			str.append("      LEFT JOIN GradeCurricular ON (MatriculaPeriodo.gradeCurricular = GradeCurricular.codigo) ");
			str.append("      LEFT JOIN PeriodoLetivo ON (MatriculaPeriodo.periodoLetivoMatricula = PeriodoLetivo.codigo) ");
			str.append("      LEFT JOIN Usuario as ResponsavelRenovacaoMatricula ON (MatriculaPeriodo.responsavelRenovacaoMatricula = ResponsavelRenovacaoMatricula.codigo) ");
			str.append("      LEFT JOIN Usuario as ResponsavelLiberacaoMatricula ON (MatriculaPeriodo.responsavelLiberacaoMatricula = ResponsavelLiberacaoMatricula.codigo) ");
			str.append("      LEFT JOIN Usuario as ResponsavelEmissaoBoletoMatricula ON (MatriculaPeriodo.responsavelEmissaoBoletoMatricula = ResponsavelEmissaoBoletoMatricula.codigo) ");
			str.append("      LEFT JOIN Usuario as ResponsavelMatriculaForaPrazo ON (MatriculaPeriodo.responsavelMatriculaForaPrazo = ResponsavelMatriculaForaPrazo.codigo) ");
			str.append("      LEFT JOIN Turma ON (MatriculaPeriodo.turma = Turma.codigo) ");
			str.append("      LEFT JOIN UnidadeEnsinoCurso ON (MatriculaPeriodo.unidadeEnsinoCurso = UnidadeEnsinoCurso.codigo) ");
			str.append("      LEFT JOIN Turno ON (matricula.turno = Turno.codigo) ");
			str.append("      LEFT JOIN PlanoFinanceiroCurso ON (MatriculaPeriodo.planoFinanceiroCurso = PlanoFinanceiroCurso.codigo) ");
			str.append("      LEFT JOIN ProcessoMatricula ON (MatriculaPeriodo.processoMatricula = ProcessoMatricula.codigo) ");
			str.append("      LEFT JOIN Funcionario f1 ON (f1.codigo = matricula.consultor) ");
			str.append("      LEFT JOIN Pessoa p1 ON (p1.codigo = f1.pessoa) ");
			str.append("      LEFT JOIN CondicaoPagamentoPlanoFinanceiroCurso ON (MatriculaPeriodo.condicaoPagamentoPlanoFinanceiroCurso = CondicaoPagamentoPlanoFinanceiroCurso.codigo) ");
			str.append("      LEFT JOIN ContaCorrente ON (ContaCorrente.codigo = unidadeEnsino.contaCorrentePadrao) ");
			str.append(" LEFT JOIN Cidade on cidade.codigo = pessoa.cidade ");
			str.append(" LEFT JOIN Estado on estado.codigo = cidade.estado ");
			return str;
		}

		@Override
		public Integer consultarQuantidadeMatriculaPorSituacaoMatrizCurricular(String situacao, Integer gradeCurricularAtual, UsuarioVO usuarioVO) {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT count(distinct matricula.matricula) AS quantidade FROM matricula ");
			sql.append(" WHERE gradeCurricularAtual = ?");
			sql.append(" AND matricula.situacao = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] {gradeCurricularAtual, situacao});
			if (tabelaResultado.next()) {
				return tabelaResultado.getInt("quantidade");
			}
			return 0;
		}

		@Override
		public List<MatriculaVO> consultarMatriculaPorSituacaoMatrizCurricular(String situacao, Integer gradeCurricularAtual, UsuarioVO usuarioVO) {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT distinct matricula.matricula, matricula.aluno, pessoa.nome, matricula.situacao FROM matricula ");
			sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
			sql.append(" WHERE gradeCurricularAtual = ?");
			sql.append(" AND matricula.situacao = ? ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] {gradeCurricularAtual, situacao});
			List<MatriculaVO> listaMatriculaVOs = new ArrayList<MatriculaVO>(0);
			while (tabelaResultado.next()) {
				MatriculaVO obj = new MatriculaVO();
				obj.setMatricula(tabelaResultado.getString("matricula"));
				obj.setSituacao(tabelaResultado.getString("situacao"));
				obj.getAluno().setCodigo(tabelaResultado.getInt("aluno"));
				obj.getAluno().setNome(tabelaResultado.getString("nome"));
				listaMatriculaVOs.add(obj);
			}
			return listaMatriculaVOs;
		}

		@Override
		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
		public void realizarAlteracaoSituacaoManual(MatriculaPeriodoVO matriculaPeriodoVO, Boolean alterarSituacaoManualMatricula, Boolean alterarSituacaoManualMatriculaPeriodo, SituacaoVinculoMatricula situacaoMatriculaAlterar, SituacaoMatriculaPeriodoEnum situacaoMatriculaPeriodoAlterar, UsuarioVO usuarioVO) throws Exception {
			realizarAlteracaoSituacaoMatriculaManual(matriculaPeriodoVO.getMatriculaVO(), alterarSituacaoManualMatricula, situacaoMatriculaAlterar, usuarioVO);
			getFacadeFactory().getMatriculaPeriodoFacade().realizarAlteracaoSituacaoMatriculaPeriodoManual(matriculaPeriodoVO, alterarSituacaoManualMatriculaPeriodo, situacaoMatriculaPeriodoAlterar, usuarioVO);
		}

		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
		public JSONObject inicializarDadosRowDataLogRegistroOperacaoMatricula(MatriculaVO matriculaVO, String situacaoMatricula) {
			JSONObject jsonObjectRowData = new JSONObject();
			jsonObjectRowData.put("Matricula", matriculaVO.getMatricula());
			jsonObjectRowData.put("Nome Aluno", matriculaVO.getAluno().getNome());
			jsonObjectRowData.put("Situacao", situacaoMatricula);
			return jsonObjectRowData;
		}

		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
		public JSONObject inicializarDadosChangedFieldsLogRegistroOperacaoMatricula(MatriculaVO matriculaVO, SituacaoVinculoMatricula situacaoMatriculaAlterar) {
			JSONObject jsonObjectChangedFields = new JSONObject();
			jsonObjectChangedFields.put("Situacao", situacaoMatriculaAlterar.getValor());
			return jsonObjectChangedFields;
		}

		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
		public void realizarAlteracaoSituacaoMatriculaManual(MatriculaVO matriculaVO, Boolean alterarSituacaoManualMatricula, SituacaoVinculoMatricula situacaoMatriculaAlterar, UsuarioVO usuarioVO) throws Exception {
			if (!alterarSituacaoManualMatricula) {
				return;
			}
			String situacaoMatricula = matriculaVO.getSituacao();

			if (!situacaoMatricula.equals(situacaoMatriculaAlterar.getValor())) {

				JSONObject jsonObjectRowData = inicializarDadosRowDataLogRegistroOperacaoMatricula(matriculaVO, situacaoMatricula);
				JSONObject jsonObjectChangedFields= inicializarDadosChangedFieldsLogRegistroOperacaoMatricula(matriculaVO, situacaoMatriculaAlterar);
				String observacao = "Alteração Manual de Matrícula/Matrícula Período";
				LogRegistroOperacoesVO logRegistroOperacoesVO = getFacadeFactory().getLogRegistroOperacoesFacade().inicializarDadosLogRegistroOperacoes(TabelaLogRegistroOperacoesEnum.MATRICULA, OperacaoLogRegistroOperacoesEnum.ALTERACAO_SITUACAO_MANUAL_MATRICULA, jsonObjectRowData, jsonObjectChangedFields, observacao, usuarioVO);
				getFacadeFactory().getLogRegistroOperacoesFacade().incluir(logRegistroOperacoesVO, usuarioVO);

				alterarSituacaoManualMatricula(matriculaVO.getMatricula(), situacaoMatriculaAlterar, usuarioVO);
				matriculaVO.setSituacao(situacaoMatriculaAlterar.getValor());
			}
		}

		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
		public void alterarSituacaoManualMatricula(String matricula, SituacaoVinculoMatricula situacao, UsuarioVO usuarioVO) throws Exception {
			try {
//				alterarSituacaoMatricula(matricula, situacao.getValor(), usuarioVO);
				String sqlStr = "UPDATE Matricula set situacao=? WHERE (matricula = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);;
				getConexao().getJdbcTemplate().update(sqlStr, new Object[] { situacao.getValor(), matricula });
			} finally {
			}
		}

		@Override
		public Boolean consultarEntregaTccAluno(Integer pessoa, String matricula) throws Exception {
			try {
				StringBuilder sql = new StringBuilder();
				sql.append(" select matricula from matricula ");
				sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
				sql.append(" where pessoa.codigO = ").append(pessoa);
				sql.append(" and matricula = '").append(matricula).append("' and notamonografia is not null");
				SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
				if (tabelaResultado.next()) {
					return Boolean.TRUE;
				}
				return Boolean.FALSE;
			} catch (Exception e) {
				throw e;
			}
		}

		@Override
		public List<MatriculaVO> consultaRapidaPorNomePessoaCursoTurmaUnidadeEnsinoFinanceira(String nomePessoa, Integer unidadeEnsinoFinanceira, Integer curso, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuffer sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" inner join contareceber on contareceber.matriculaaluno = matricula.matricula");
			sqlStr.append(" WHERE ( sem_acentos(Pessoa.nome) ilike('").append(Uteis.removerAcentos(nomePessoa)).append("%')) ");
			if (unidadeEnsinoFinanceira != null && unidadeEnsinoFinanceira.intValue() != 0) {
				sqlStr.append(" AND (contareceber.unidadeensinofinanceira = ").append(unidadeEnsinoFinanceira.intValue()).append(") ");
			}
			if (curso != null && curso.intValue() != 0) {
				sqlStr.append(" AND (Curso.codigo = ").append(curso.intValue()).append(") ");
			}
			if ((turma != null) && (turma.intValue() != 0)) {
				sqlStr.append("AND matricula.matricula IN(SELECT DISTINCT mp.matricula FROM matriculaperiodo mp WHERE mp.turma = ").append(turma).append(") ");
			}
			sqlStr.append(" ORDER BY Pessoa.nome ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaBasica(tabelaResultado);
		}

		@Override
		public List<MatriculaVO> consultaRapidaPorMatriculaCursoTurmaUnidadeEnsinoFinanceira(String matricula, Integer unidadeEnsinoFinanceira, Integer curso, Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuffer sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" inner join contareceber on contareceber.matriculaaluno = matricula.matricula");
			sqlStr.append(" WHERE (Matricula.matricula like('").append(matricula).append("%')) ");
			if (unidadeEnsinoFinanceira.intValue() != 0 && unidadeEnsinoFinanceira != null) {
				sqlStr.append(" AND (contareceber.unidadeensinofinanceira = ").append(unidadeEnsinoFinanceira.intValue()).append(") ");
			}
			if (curso != null && curso.intValue() != 0) {
				sqlStr.append(" AND (Curso.codigo = ").append(curso.intValue()).append(") ");
			}
			if ((turma != null) && (turma.intValue() != 0)) {
				sqlStr.append("AND matricula.matricula IN(SELECT DISTINCT mp.matricula FROM matriculaperiodo mp WHERE mp.turma = ").append(turma).append(") ");
			}
			sqlStr.append(" ORDER BY Matricula.matricula ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaBasica(tabelaResultado);
		}


		

		@Override
		public List<MatriculaVO> consultarPorUnidadeEnsinoCursoTurmaDisciplina(String campoConsulta, String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, Integer turma, Integer disciplina, UsuarioVO usuarioVO) throws Exception {
	        ControleAcesso.consultar(getIdEntidade(), false, usuarioVO);
	        StringBuffer sqlStr = getSQLPadraoConsultaBasicaDistinct();
	        sqlStr.append("LEFT JOIN Turma ON Turma.curso = curso.codigo ");
	        sqlStr.append("WHERE 1=1 ");

	        if (campoConsulta.equals("nome")) {
	            sqlStr.append("AND upper(sem_acentos(pessoa.nome)) like upper(sem_acentos('");
	            sqlStr.append(valorConsulta.toLowerCase());
	            sqlStr.append("%')) ");
	        }else if(campoConsulta.equals("registroAcademico")) {
	        	sqlStr.append("AND upper(sem_acentos(pessoa.registroAcademico)) like upper(sem_acentos('");
	            sqlStr.append(valorConsulta.toLowerCase());
	            sqlStr.append("%')) ");
	        } else if (campoConsulta.equals("matricula")) {
	            sqlStr.append("AND matricula.matricula = '").append(valorConsulta).append("'");
	        }

			if (!unidadeEnsinoVOs.isEmpty() && unidadeEnsinoVOs.stream().anyMatch(p-> p.getFiltrarUnidadeEnsino())) {
				sqlStr.append(" and matricula.unidadeensino in (");
				int x = 0;
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
						if (x > 0) {
							sqlStr.append(", ");
						}
						sqlStr.append(unidadeEnsinoVO.getCodigo());
						x++;
					}

				}
				sqlStr.append(" ) ");
			}

			if (curso != 0 && curso != null) {
				sqlStr.append(" AND curso.codigo = ").append(curso);
			}
//			if (turma != 0 && curso != null) {
//				sqlStr.append(" AND matriculaperiodoturmadisciplina.turma = ").append(turma);
//			}
//			if (disciplina != 0 && disciplina != null) {
//				sqlStr.append(" AND matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
//			}

	        sqlStr.append(" ORDER BY pessoa.nome; ");
	        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	        return montarDadosConsultaBasica(tabelaResultado);
		}
	
	@Override
	public List<MatriculaVO> consultaRapidaPorMatriculaAutoComplete(String valorConsulta, Integer codigoUnidadeEnsino, int limit, boolean contrarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(codigoUnidadeEnsino));
		}
		return consultaRapidaPorMatriculaAutoComplete(valorConsulta, unidadeEnsinoVOs, limit, contrarAcesso, nivelMontarDados, usuario);
	}
	
	@Override
	public List<MatriculaVO> consultaRapidaPorMatriculaAutoComplete(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, int limit, boolean contrarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), contrarAcesso, usuario);
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" SELECT distinct matricula.matricula, pessoa.nome ");
		sqlStr.append(" FROM matricula ");
		sqlStr.append(" LEFT JOIN pessoa ON pessoa.codigo = matricula.aluno");
		sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino");
		sqlStr.append(" WHERE upper(matricula.matricula) like upper(?) ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sqlStr.append(" AND matricula.unidadeensino IN (").append(unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(") ");
		}
		sqlStr.append(" ORDER BY pessoa.nome ");
		sqlStr.append(" LIMIT ").append(limit);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		List<MatriculaVO> listaMatricula = new ArrayList<MatriculaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			obj.setMatricula(tabelaResultado.getString("matricula"));
			obj.getAluno().setNome(tabelaResultado.getString("nome"));
			listaMatricula.add(obj);
		}
		return listaMatricula;
	}
	
	
	
	
	

	@Override
	public List<MatriculaVO> consultaRapidaPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE matricula.matricula ilike(?)");
		if (!(usuario.getUnidadeEnsinoLogado() == null || usuario.getUnidadeEnsinoLogado().getCodigo() == null || usuario.getUnidadeEnsinoLogado().getCodigo().intValue() == 0)) {
			sqlStr.append("AND unidadeEnsino.codigo = " + usuario.getUnidadeEnsinoLogado().getCodigo() + " ");
		}
		sqlStr.append("ORDER BY matricula.matricula ");
		sqlStr.append(" LIMIT 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
	}

	
	
	
	
	@Override
	public List consultarPorRegistroAcademicoMatriculaAutoComplete( String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados , UsuarioVO usuario) throws Exception {
		  ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);		
		  List<MatriculaVO> listaMatriculaVOs = new ArrayList<MatriculaVO>(0);
		  if (valorConsulta.length() >= 2) {
    	 
   	        StringBuffer sqlStr = new StringBuffer();   		
   		
	   		sqlStr.append("select distinct matricula.matricula,  pessoa.codigo AS \"pessoa.codigo\" ,  pessoa.nome AS \"pessoa.nome\",  pessoa.registroacademico AS \"pessoa.registroacademico\" , curso.codigo AS \"curso.codigo\",  curso.nome as \"curso.nome\", unidadeensino.codigo AS  \"unidadeensino.codigo\" , unidadeensino.nome AS \"unidadeensino.nome\" ");
	   		sqlStr.append(" FROM Matricula ");
	   		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
	   		sqlStr.append(" inner join curso on curso.codigo = matricula.curso ");
	   		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");			
		    sqlStr.append(" WHERE UPPER (matricula) like('" + valorConsulta.toUpperCase() + "%')  OR  UPPER (pessoa.registroAcademico) like('" + valorConsulta.toUpperCase() + "%')");
			if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
				sqlStr.append("  and unidadeEnsino =" + unidadeEnsino.intValue() +"");	
			}
			sqlStr.append(" ORDER BY matricula  , pessoa.registroacademico  limit 100 ");	
			
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());			
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();  //RA, Nome do Aluno, Curso e Unidade de Ensino
			obj.setMatricula(tabelaResultado.getString("matricula"));			
			obj.getAluno().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getAluno().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.getAluno().setRegistroAcademico(tabelaResultado.getString("pessoa.registroacademico"));
			obj.getCurso().setCodigo(tabelaResultado.getInt("curso.codigo"));
			obj.getCurso().setNome(tabelaResultado.getString("curso.nome"));
			obj.getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeensino.codigo"));
			obj.getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeensino.nome"));
			listaMatriculaVOs.add(obj);
		}
			
	  }
      return listaMatriculaVOs;	
	}
	
	
	@Override
	public MatriculaVO consultarMatriculaPorRegistroAcademico(String registroAcademico, Integer unidadeEnsino, Integer curso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT Matricula.* FROM Matricula  inner join pessoa on pessoa.codigo = matricula.aluno  "
				+ " WHERE pessoa.registroAcademico= '" + registroAcademico + "'";
		
		if ((unidadeEnsino != null) && (unidadeEnsino.intValue() != 0)) {
			sqlStr +=  " and Matricula.unidadeEnsino = " + unidadeEnsino;
		}
		if ((curso != null) && (curso.intValue() != 0)) {
			sqlStr += " and Matricula.curso=" + curso;
		}
		sqlStr += " order by case Matricula.situacao when 'AT' then 1 when 'PR' then 2 when 'TR' then 3 else 4 end,  Matricula.data desc limit 1 ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}
	
	
	@Override
	public List<MatriculaVO> consultaRapidaPorRegistroAcademicoSemProgramacaoFormatura(String registroAcademico, List<ProgramacaoFormaturaUnidadeEnsinoVO> prograFormaturaUnidadeEnsinoVOs, List<TurnoVO> turnoVOs, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE Pessoa.registroAcademico ilike ?  ");
		sqlStr.append(" AND (matricula.matricula not in(select distinct matricula from programacaoformaturaaluno ) or ");
		sqlStr.append(" (select case when (programacaoformatura is null) then true else false end FROM programacaoFormaturaAluno p WHERE p.matricula = matricula.matricula)) ");
		if (Uteis.isAtributoPreenchido(turnoVOs)) {
			sqlStr.append(" AND turno.codigo in (");
			for (TurnoVO turno : turnoVOs) {
				if (!turnoVOs.get(turnoVOs.size() - 1).getCodigo().equals(turno.getCodigo())) {
					sqlStr.append(turno.getCodigo()+", ");
				} else {
					sqlStr.append(turno.getCodigo());
		}
		}
			sqlStr.append(") ");
		}
		if (Uteis.isAtributoPreenchido(prograFormaturaUnidadeEnsinoVOs)) {
			sqlStr.append(" AND UnidadeEnsino.codigo in (");
			for (ProgramacaoFormaturaUnidadeEnsinoVO programacaoFormaturaUnidadeEnsinoVO : prograFormaturaUnidadeEnsinoVOs) {
				if (!prograFormaturaUnidadeEnsinoVOs.get(prograFormaturaUnidadeEnsinoVOs.size() - 1).getUnidadeEnsinoVO().getCodigo().equals(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo())) {
					sqlStr.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo()+", ");
				} else {
					sqlStr.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo());
				}
			}
			sqlStr.append(") ");
		}
		sqlStr.append(" ORDER BY Pessoa.registroAcademico ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), registroAcademico + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
		
	}
	
	
	@Override
	public List<MatriculaVO> consultaRapidaPorMatriculaAlunoPorDataModelo(String valorConsulta, DataModelo dataModeloAluno, boolean controlarAcesso, String situacao, String nivelEducacional, Integer curso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT count(*) over() as qtde_total_registros, * FROM (");
	    sqlStr.append( getSQLPadraoConsultaBasicaStringBuilder());
		sqlStr.append(" WHERE sem_acentos(matricula.matricula) ilike sem_acentos(?) ");

		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" AND (Curso.niveleducacional = '").append(nivelEducacional).append("')");
		}

		if (dataModeloAluno.getUnidadeEnsinoVO().getCodigo().intValue() != 0 && dataModeloAluno.getUnidadeEnsinoVO().getCodigo() != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(dataModeloAluno.getUnidadeEnsinoVO().getCodigo().intValue()).append(") ");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" AND Curso.codigo = ").append(curso).append(" ");
		}
		sqlStr.append(" ) AS t ");
		sqlStr.append(" ORDER BY \"Pessoa.nome\" ");
		UteisTexto.addLimitAndOffset(sqlStr, dataModeloAluno.getLimitePorPagina(), dataModeloAluno.getOffset());
		
		dataModeloAluno.setTotalRegistrosEncontrados(0);	
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta +PERCENT);
		if (tabelaResultado.next()) {
			dataModeloAluno.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		return montarDadosConsultaBasica(tabelaResultado);
	}
	
	@Override
	public List<MatriculaVO> consultaRapidaPorRegistroAcademicoAlunoPorDataModelo(String valorConsulta, DataModelo dataModeloAluno, boolean controlarAcesso, String situacao, String nivelEducacional, Integer curso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT count(*) over() as qtde_total_registros, * FROM (");
	    sqlStr.append( getSQLPadraoConsultaBasicaStringBuilder());
		sqlStr.append(" WHERE sem_acentos(Pessoa.registroAcademico) ilike sem_acentos(?) ");

		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" AND (Curso.niveleducacional = '").append(nivelEducacional).append("')");
		}

		if (dataModeloAluno.getUnidadeEnsinoVO().getCodigo().intValue() != 0 && dataModeloAluno.getUnidadeEnsinoVO().getCodigo() != null) {
			sqlStr.append(" AND (UnidadeEnsino.codigo = ").append(dataModeloAluno.getUnidadeEnsinoVO().getCodigo().intValue()).append(") ");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" AND Curso.codigo = ").append(curso).append(" ");
		}
		sqlStr.append(" ) AS t ");
		sqlStr.append(" ORDER BY \"Pessoa.registroAcademico\" ");
		UteisTexto.addLimitAndOffset(sqlStr, dataModeloAluno.getLimitePorPagina(), dataModeloAluno.getOffset());
		
		dataModeloAluno.setTotalRegistrosEncontrados(0);		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta +PERCENT);
		if (tabelaResultado.next()) {
			dataModeloAluno.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		return montarDadosConsultaBasica(tabelaResultado);
	}
	
	
	@Override
	public void realizarOperacaoInclusaoDadosBancarioAlunoMatriculaExterna(PessoaVO aluno, String bancoAluno, String agenciaAluno, String contaCorrenteAluno) throws Exception {
		
		if(Uteis.isAtributoPreenchido(bancoAluno) && Uteis.isAtributoPreenchido(agenciaAluno) && Uteis.isAtributoPreenchido(contaCorrenteAluno) ) {
			if(Uteis.isAtributoPreenchido(aluno.getBanco()) && Uteis.isAtributoPreenchido(aluno.getAgencia())  && Uteis.isAtributoPreenchido(aluno.getContaCorrente())) {
				if(aluno.getBanco().equals(bancoAluno) && aluno.getAgencia().equals(bancoAluno) && aluno.getContaCorrente().equals(contaCorrenteAluno)) {
					return;
				}
			}
			aluno.setBanco(bancoAluno);
			aluno.setAgencia(agenciaAluno);
			aluno.setContaCorrente( contaCorrenteAluno);
			getFacadeFactory().getPessoaFacade().alterarDadosBancariosAluno(bancoAluno,agenciaAluno, contaCorrenteAluno, aluno.getCodigo());
			
		}
	}
		
	private StringBuffer getSQLPadraoConsultaBasicaComEnade() {
		return getSQLPadraoConsultaBasicaComEnade(false);
	}

	private StringBuffer getSQLPadraoConsultaBasicaComEnade(Boolean versaoAplicativo) {
		StringBuffer str = new StringBuffer();
		str.append(" SELECT distinct case when matricula.situacao = 'AT' then 0 else 1 end as ordenacao, matricula.matricula, matricula.gradeCurricularAtual, matricula.data, matricula.dataBaseSuspensao, matricula.matriculaSuspensa, matricula.matriculaSerasa, matricula.situacao, matricula.situacaoFinanceira, matricula.dataInicioCurso, matricula.updated, matricula.naoEnviarMensagemCobranca, matricula.alunoConcluiuDisciplinasRegulares, matricula.qtdDisciplinasExtensao, matricula.qtdDiasAdiarBloqueio, ");
		str.append(" matricula.formacaoAcademica, matricula.autorizacaoCurso, matricula.consultor, matricula.classificacaoIngresso, matricula.formaIngresso, matricula.tipoMatricula, matricula.nomeMonografia, matricula.notaMonografia, matricula.orientadormonografia, matricula.tipotrabalhoconclusaocurso,  matricula.dataColacaoGrau AS \"matricula.dataColacaoGrau\", matricula.dataProcessoSeletivo, matricula.diaSemanaAula, matricula.turnoAula, ");
		str.append(" matricula.anoConclusao as \"matricula.anoConclusao\", matricula.semestreConclusao as \"matricula.semestreConclusao\", matricula.notaEnem, matricula.observacaoDiploma, matricula.localarmazenamentodocumentosmatricula, matricula.canceladoFinanceiro, matricula.dataemissaohistorico, matricula.codigofinanceiromatricula,  matricula.anoIngresso ,matricula.semestreIngresso , matricula.mesIngresso, matricula.disciplinasprocseletivo, matricula.cargahorariamonografia, matricula.titulacaoorientadormonografia,");
		str.append(" Pessoa.nome as \"Pessoa.nome\", Pessoa.codigo as \"Pessoa.codigo\", Pessoa.sexo as \"Pessoa.sexo\", Pessoa.cpf as \"Pessoa.cpf\", Pessoa.rg as \"Pessoa.rg\",  Pessoa.orgaoEmissor as \"Pessoa.orgaoEmissor\", Pessoa.dataNasc as \"Pessoa.dataNasc\", Pessoa.sexo as \"Pessoa.sexo\",  Pessoa.registroAcademico as \"Pessoa.registroAcademico\" , ");
		str.append(" Pessoa.arquivoImagem as codArquivo, arquivo.pastaBaseArquivo, arquivo.nome AS nomeArquivo, ");
		str.append(" Curso.nomeDocumentacao as \"Curso.nomeDocumentacao\", Curso.nome as \"Curso.nome\", Curso.abreviatura as \"Curso.abreviatura\", pessoa.telefoneres AS \"pessoa.telefoneres\", pessoa.celular AS \"pessoa.celular\", Curso.areaconhecimento as \"Curso.areaconhecimento\", AreaConhecimento.nome as \"AreaConhecimento.nome\", Curso.codigo as \"Curso.codigo\", Curso.nivelEducacional as \"Curso.nivelEducacional\", Curso.periodicidade as \"Curso.periodicidade\", Curso.perfilEgresso as \"Curso.perfilEgresso\", Curso.utilizarRecursoAvaTerceiros as \"Curso.utilizarRecursoAvaTerceiros\",");
		str.append(" Curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios as \"Curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios\" , Curso.permitirAssinarContratoPendenciaDocumentacao as \"Curso.permitirAssinarContratoPendenciaDocumentacao\" ,  Curso.ativarMatriculaAposAssinaturaContrato  as \"Curso.ativarMatriculaAposAssinaturaContrato\" , ");
		str.append(" UnidadeEnsino.codigo as \"UnidadeEnsino.codigo\", UnidadeEnsino.nome as \"UnidadeEnsino.nome\", UnidadeEnsino.razaoSocial AS \"UnidadeEnsino.razaoSocial\", UnidadeEnsino.cnpj AS \"UnidadeEnsino.cnpj\", UnidadeEnsino.cidade as \"UnidadeEnsino.cidade\", Turno.codigo as \"Turno.codigo\", Turno.nome as \"Turno.nome\", ");
		str.append(" UnidadeEnsino.caminhoBaseLogo as \"UnidadeEnsino.caminhoBaseLogo\", UnidadeEnsino.nomeArquivoLogo as \"UnidadeEnsino.nomeArquivoLogo\",  ");
		str.append(" UnidadeEnsino.caminhoBaseLogoIndex as \"UnidadeEnsino.caminhoBaseLogoIndex\", UnidadeEnsino.nomeArquivoLogoIndex as \"UnidadeEnsino.nomeArquivoLogoIndex\", UnidadeEnsino.nomeExpedicaoDiploma as \"UnidadeEnsino.nomeExpedicaoDiploma\", ");
		str.append(" unidadeensino.contaCorrentePadraoMensalidade, unidadeensino.contaCorrentePadraoMatricula, unidadeensino.contaCorrentePadraoBiblioteca, ");
		str.append(" unidadeensino.contaCorrentePadraoDevolucaoCheque, unidadeensino.contaCorrentePadraoMaterialDidatico, unidadeensino.contaCorrentePadraoNegociacao, ");
		str.append(" unidadeensino.contaCorrentePadraoProcessoSeletivo, ");
		str.append(" Configuracoes.codigo AS \"Configuracoes.codigo\", Configuracoes.nome AS \"Configuracoes.nome\", ");
		str.append(" Curso.configuracaoAcademico as \"Curso.configuracaoAcademico\", Matricula.usuario as \"Matricula.responsavelMatricula\", Matricula.bloqueioPorSolicitacaoLiberacaoMatricula, ");
		str.append(" COALESCE(matricula.dataconclusaocurso, concl.dataconclusaocurso) as dataconclusaocurso, ");
		str.append(" dados_matriculaenade.codigo_matriculaEnade, dados_matriculaenade.matriculaenade_dataenade,");
		str.append(" dados_matriculaenade.codigo_textoenade,dados_matriculaenade.textoenade_texto,dados_matriculaenade.textoenade_texto,");
		str.append(" dados_matriculaenade.textoenade_tipoTextoEnade,dados_matriculaenade.matriculaenade_matricula,dados_matriculaenade.codigo_enade,");
		str.append(" dados_matriculaenade.enade_tituloenada,dados_matriculaenade.enade_dataPortaria");
		str.append(" FROM Matricula ");
		str.append(" INNER JOIN Turno ON (Matricula.turno = turno.codigo) ");
		str.append(" INNER JOIN Curso ON (Matricula.curso = curso.codigo) ");
		str.append(" INNER JOIN UnidadeEnsino ON (Matricula.unidadeEnsino = unidadeEnsino.codigo) ");
		str.append(" INNER JOIN Pessoa ON (Matricula.aluno = pessoa.codigo) ");
		str.append(" LEFT JOIN Arquivo ON (pessoa.arquivoImagem = arquivo.codigo) ");
		str.append(" LEFT JOIN AreaConhecimento ON (Curso.areaconhecimento = AreaConhecimento.codigo) ");
		str.append(" LEFT JOIN Configuracoes ON ((unidadeEnsino.Configuracoes is not null and Configuracoes.codigo = unidadeEnsino.Configuracoes) or (unidadeEnsino.Configuracoes is null and Configuracoes.padrao)) ");
		str.append(" LEFT JOIN LATERAL ( ");
		str.append(" SELECT  ");
		str.append(" dados_matriculaenade.codigo as codigo_matriculaEnade ,dados_textoenade.codigo as codigo_textoenade, ");
		str.append(" dados_textoenade.texto,dados_textoenade.texto as textoenade_texto,  dados_textoenade.tipoTextoEnade as textoenade_tipoTextoEnade, ");
		str.append(" dados_matriculaenade.dataEnade as matriculaenade_dataenade, dados_matriculaenade.matricula as matriculaenade_matricula, ");
		str.append(" dados_enade.codigo as codigo_enade,dados_enade.tituloEnade as  enade_tituloenada,dados_enade.dataPortaria  as enade_dataPortaria ");
		str.append("FROM matriculaenade  dados_matriculaenade ");
		str.append("INNER JOIN enade dados_enade  					ON dados_matriculaenade.enade 		= dados_enade.codigo ");
		str.append("INNER JOIN textoenade dados_textoenade		    ON dados_matriculaenade.textoenade 	= dados_textoenade.codigo "); 
		str.append("WHERE matricula.matricula = dados_matriculaenade.matricula ");
		str.append(" ORDER BY dados_enade.dataprova DESC ,dados_matriculaenade.dataenade DESC ,dados_matriculaenade.codigo DESC LIMIT 1 ");
		str.append(") AS dados_matriculaenade ON 1=1 ");
		
		str.append(" LEFT JOIN LATERAL ( SELECT MAX(dataconclusaotemp)::date AS dataconclusaocurso FROM ( ");
		str.append(" SELECT CASE WHEN gd.bimestre = 1 THEN plauc.datafimperiodoletivoprimeirobimestre ELSE plauc.datafimperiodoletivosegundobimestre END AS dataconclusaotemp ");
		str.append(" FROM historico h JOIN gradedisciplina gd ON gd.codigo = h.gradedisciplina ");
		str.append(" JOIN matriculaperiodo hp ON hp.codigo = h.matriculaperiodo ");
		str.append(" JOIN processomatricula pm ON pm.codigo = hp.processomatricula ");
		str.append(" JOIN processomatriculacalendario pmc ON pmc.processomatricula = pm.codigo AND pmc.curso = matricula.curso AND pmc.turno = matricula.turno ");
		str.append(" JOIN periodoletivoativounidadeensinocurso plauc ON plauc.codigo = pmc.periodoletivoativounidadeensinocurso ");
		str.append(" WHERE h.situacao IN ('AA','AP','AE','IS','CH','CC') AND h.matricula = matricula.matricula AND h.matrizcurricular = matricula.gradecurricularatual ");
		str.append(" UNION ALL ");
		str.append(" SELECT e.datadeferimento AS dataconclusaotemp FROM estagio e ");
		str.append(" JOIN gradecurricularestagio ge ON ge.codigo = e.gradecurricularestagio ");
		str.append(" WHERE e.matricula = matricula.matricula AND e.situacaoestagioenum = 'DEFERIDO' AND ge.gradecurricular = matricula.gradecurricularatual AND e.datadeferimento IS NOT NULL ");
		str.append(" ) s(dataconclusaotemp) ) concl ON TRUE ");
		return str;
	}

	@Override
	public List<MatriculaVO> consultaCompletaSemProgramacaoFormatura(Integer programacaoFormaturaAtual, List<ProgramacaoFormaturaUnidadeEnsinoVO> prograFormaturaUnidadeEnsinoVOs, List<TurnoVO> turnoVOs, List<CursoVO> cursoVOs, Integer turma, String matricula, Integer codigoRequerimento, String nivelEducacional, Boolean enade, Boolean TCC, boolean semRequerimento, String tipoRequerimento, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasicaComEnade();
		sqlStr.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.matricula = matricula.matricula and  ");
		sqlStr.append(" matriculaPeriodo.codigo = (select mp.codigo from matriculaperiodo as mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo != 'PC' ");
		sqlStr.append(" order by mp.ano desc, mp.semestre desc, case when mp.situacaomatriculaperiodo in ('AT', 'FO') then 0 else 1 end, mp.data desc, mp.codigo desc limit 1 )"); 
		
		if ((semRequerimento && Uteis.isAtributoPreenchido(codigoRequerimento)) || (!semRequerimento)) {
			sqlStr.append(" INNER JOIN requerimento ON requerimento.matricula = matricula.matricula");
			sqlStr.append(" INNER JOIN tiporequerimento ON tiporequerimento.codigo = requerimento.tiporequerimento");	
			
		}
//		sqlStr.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.matricula = matricula.matricula");
		sqlStr.append(" WHERE (NOT EXISTS ( SELECT FROM programacaoformaturaaluno WHERE programacaoformaturaaluno.matricula = matricula.matricula) ");
		sqlStr.append(" OR (NOT EXISTS ( SELECT FROM programacaoformaturaaluno WHERE programacaoformaturaaluno.matricula = matricula.matricula AND programacaoformaturaaluno.colougrau IN ('SI', 'NI')))) ");
		sqlStr.append(" AND matricula.situacao in ('AT', 'FI') ");
		if (Uteis.isAtributoPreenchido(codigoRequerimento)) {
			sqlStr.append(" AND (requerimento.codigo =  ").append(codigoRequerimento).append(") ");
		}
		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append(" AND (Matricula.matricula like('").append(matricula).append("%')) ");
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" AND matriculaperiodo.turma = '").append(turma.intValue()).append("'");
		}
		if ((semRequerimento && Uteis.isAtributoPreenchido(codigoRequerimento)) || (!semRequerimento)) {
			sqlStr.append(" AND tiporequerimento.tipo = '").append(tipoRequerimento.toUpperCase()).append("'");
		}
		if(Uteis.isAtributoPreenchido(programacaoFormaturaAtual)) {
			sqlStr.append(" AND not exists (select pfa.codigo from programacaoformaturaaluno pfa where pfa.matricula = matricula.matricula and pfa.programacaoformatura = ").append(programacaoFormaturaAtual).append(" ) ");
		}
		if (Uteis.isAtributoPreenchido(prograFormaturaUnidadeEnsinoVOs)) {
			sqlStr.append(" AND UnidadeEnsino.codigo in (");
			for (ProgramacaoFormaturaUnidadeEnsinoVO programacaoFormaturaUnidadeEnsinoVO : prograFormaturaUnidadeEnsinoVOs) {
				if (!prograFormaturaUnidadeEnsinoVOs.get(prograFormaturaUnidadeEnsinoVOs.size() - 1).getUnidadeEnsinoVO().getCodigo().equals(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo())) {
					sqlStr.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo()+", ");
				} else {
					sqlStr.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo());
				}
			}
			sqlStr.append(") ");
		}
		if (Uteis.isAtributoPreenchido(cursoVOs)) {
			sqlStr.append(" AND Curso.codigo in (");
			for (CursoVO cursoVO : cursoVOs) {
				if (!cursoVOs.get(cursoVOs.size() - 1).getCodigo().equals(cursoVO.getCodigo())) {
					sqlStr.append(cursoVO.getCodigo()+", ");
				} else {
					sqlStr.append(cursoVO.getCodigo());
				}
			}
			sqlStr.append(") ");
		}
		if (Uteis.isAtributoPreenchido(turnoVOs)) {
			sqlStr.append(" AND matricula.turno in (");
			for (TurnoVO turno : turnoVOs) {
				if (!turnoVOs.get(turnoVOs.size() - 1).getCodigo().equals(turno.getCodigo())) {
					sqlStr.append(turno.getCodigo()+", ");
				} else {
					sqlStr.append(turno.getCodigo());
				}
			}
			sqlStr.append(") ");
		}
		if (enade) {
			sqlStr.append(" AND EXISTS(SELECT me.codigo FROM matriculaenade me INNER JOIN textoenade te ON te.codigo = me.textoenade WHERE me.matricula = matricula.matricula AND te.tipotextoenade IN ('REALIZACAO', 'DISPENSA')) ");
		}
		if (TCC) {
			sqlStr.append(" AND matricula.notamonografia != null AND matricula.notamonografia != 0 ");
		}
		if (Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" AND Curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		sqlStr.append(" ORDER BY Matricula.matricula");
		System.out.println(sqlStr.toString());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasicaComEnade(tabelaResultado);
		
	}



			
	public List<MatriculaVO> montarDadosConsultaBasicaComEnade(SqlRowSet tabelaResultado) throws Exception {
		List<MatriculaVO> vetResultado = new ArrayList<MatriculaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaVO obj = new MatriculaVO();
			montarDadosBasicoComEnade(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
			
	private void montarDadosBasicoComEnade(MatriculaVO obj, SqlRowSet dadosSQL) throws Exception {
		/**
		 * Toda vez que adicionar um campo neste montar dados favor adicionar o campo no getSqlConsultaBasicaDistinct
		 */
		// Dados da Matrícula
		obj.setNovoObj(false);
		obj.setMatricula(dadosSQL.getString("matricula"));
		GradeCurricularVO gradeCurricularAtual = new GradeCurricularVO();
		gradeCurricularAtual.setCodigo(dadosSQL.getInt("gradeCurricularAtual"));
		obj.setGradeCurricularAtual(gradeCurricularAtual);
		obj.setData(dadosSQL.getDate("data"));
		obj.setDataBaseSuspensao(dadosSQL.getDate("dataBaseSuspensao"));
		obj.setMatriculaSuspensa(dadosSQL.getBoolean("matriculaSuspensa"));
		obj.setNotaEnem(dadosSQL.getDouble("notaEnem"));
		obj.setObservacaoDiploma(dadosSQL.getString("observacaoDiploma"));
		obj.setMatriculaSerasa(dadosSQL.getBoolean("matriculaSerasa"));
		obj.setQtdDisciplinasExtensao(dadosSQL.getInt("qtdDisciplinasExtensao"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setClassificacaoIngresso(dadosSQL.getInt("classificacaoIngresso"));
		obj.setSituacaoFinanceira(dadosSQL.getString("situacaoFinanceira"));
		obj.setDataConclusaoCurso(dadosSQL.getDate("dataConclusaoCurso"));
		obj.setDataInicioCurso(dadosSQL.getDate("dataInicioCurso"));
		obj.setUpdated(dadosSQL.getTimestamp("updated"));
		obj.getFormacaoAcademica().setCodigo(dadosSQL.getInt("formacaoAcademica"));
		obj.getAutorizacaoCurso().setCodigo(dadosSQL.getInt("autorizacaoCurso"));
		obj.getConsultor().setCodigo(dadosSQL.getInt("consultor"));
		obj.setFormaIngresso(dadosSQL.getString("formaIngresso"));
		obj.setTipoMatricula(dadosSQL.getString("tipoMatricula"));
		obj.setTituloMonografia(dadosSQL.getString("nomeMonografia"));
        obj.setAnoConclusao(dadosSQL.getString("matricula.anoConclusao"));
        obj.setSemestreConclusao(dadosSQL.getString("matricula.semestreConclusao"));
		obj.setNotaMonografia(dadosSQL.getDouble("notaMonografia"));
		obj.setTitulacaoOrientadorMonografia(dadosSQL.getString("titulacaoorientadormonografia"));
		obj.setCargaHorariaMonografia(dadosSQL.getInt("cargahorariamonografia"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("diaSemanaAula"))) {
        	obj.setDiaSemanaAula(DiaSemana.valueOf(dadosSQL.getString("diaSemanaAula")));
        }else {
        	obj.setDiaSemanaAula(DiaSemana.NENHUM);
        }
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("turnoAula"))) {
        	obj.setTurnoAula(NomeTurnoCensoEnum.valueOf(dadosSQL.getString("turnoAula")));
        }else {
        	obj.setTurnoAula(NomeTurnoCensoEnum.NENHUM);
        }
		if(dadosSQL.getString("orientadormonografia") != null) {
			obj.setOrientadorMonografia(dadosSQL.getString("orientadormonografia"));
		}
		if(dadosSQL.getString("tipotrabalhoconclusaocurso") != null) {
			obj.setTipoTrabalhoConclusaoCurso(dadosSQL.getString("tipotrabalhoconclusaocurso"));
		}
		obj.setNaoEnviarMensagemCobranca(dadosSQL.getBoolean("naoEnviarMensagemCobranca"));
		obj.setAlunoConcluiuDisciplinasRegulares(dadosSQL.getBoolean("alunoConcluiuDisciplinasRegulares"));
		obj.setDataColacaoGrau(dadosSQL.getDate("matricula.dataColacaoGrau"));
		obj.setQtdDiasAdiarBloqueio(dadosSQL.getInt("qtdDiasAdiarBloqueio"));
		obj.setLocalArmazenamentoDocumentosMatricula(dadosSQL.getString("localarmazenamentodocumentosmatricula"));
		obj.setDataProcessoSeletivo(dadosSQL.getDate("dataProcessoSeletivo"));
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		obj.setCanceladoFinanceiro(dadosSQL.getBoolean("canceladoFinanceiro"));
		obj.setDataEmissaoHistorico(dadosSQL.getDate("dataEmissaoHistorico"));
		obj.setCodigoFinanceiroMatricula(dadosSQL.getString("codigofinanceiromatricula"));
		obj.setBloqueioPorSolicitacaoLiberacaoMatricula(dadosSQL.getBoolean("bloqueioPorSolicitacaoLiberacaoMatricula"));
		obj.setAnoIngresso(dadosSQL.getString("anoIngresso"));
		obj.setSemestreIngresso(dadosSQL.getString("semestreIngresso"));
		obj.setMesIngresso(dadosSQL.getString("mesIngresso"));
		obj.setDisciplinasProcSeletivo(dadosSQL.getString("disciplinasprocseletivo"));
		// Dados do Aluno
		obj.getAluno().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
		obj.getAluno().setTelefoneRes(dadosSQL.getString("Pessoa.telefoneres"));
		obj.getAluno().setCelular(dadosSQL.getString("Pessoa.celular"));
		obj.getAluno().setNome(dadosSQL.getString("Pessoa.nome"));
		obj.getAluno().setCPF(dadosSQL.getString("Pessoa.cpf"));
		obj.getAluno().setRG(dadosSQL.getString("Pessoa.rg"));
		obj.getAluno().setDataNasc(dadosSQL.getDate("Pessoa.dataNasc"));
		obj.getAluno().setSexo(dadosSQL.getString("Pessoa.sexo"));
		obj.getAluno().setRegistroAcademico(dadosSQL.getString("Pessoa.registroAcademico"));
		// Dados do Arquivo do Aluno
		obj.getAluno().getArquivoImagem().setCodigo(dadosSQL.getInt("codArquivo"));
		obj.getAluno().getArquivoImagem().setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivo"));
		obj.getAluno().getArquivoImagem().setNome(dadosSQL.getString("nomeArquivo"));
		obj.getAluno().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados do Curso
		obj.getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
		obj.getCurso().setNome(dadosSQL.getString("Curso.nome"));
		obj.getCurso().setNomeDocumentacao(dadosSQL.getString("Curso.nomeDocumentacao"));
        obj.getCurso().setNivelEducacional(dadosSQL.getString("Curso.nivelEducacional"));
		obj.getCurso().setPeriodicidade(dadosSQL.getString("Curso.periodicidade"));
		obj.getCurso().setPerfilEgresso(dadosSQL.getString("Curso.perfilEgresso"));
		obj.getCurso().getAreaConhecimento().setCodigo(dadosSQL.getInt("Curso.areaconhecimento"));
		obj.getCurso().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("Curso.configuracaoAcademico"));
		obj.getCurso().setUtilizarRecursoAvaTerceiros(dadosSQL.getBoolean("Curso.utilizarRecursoAvaTerceiros"));
		obj.getCurso().setAbreviatura(dadosSQL.getString("Curso.abreviatura"));
		obj.getCurso().setPermitirAssinarContratoPendenciaDocumentacao(dadosSQL.getBoolean("Curso.permitirAssinarContratoPendenciaDocumentacao"));
		obj.getCurso().setAtivarPreMatriculaAposEntregaDocumentosObrigatorios(dadosSQL.getBoolean("Curso.ativarPreMatriculaAposEntregaDocumentosObrigatorios"));
		obj.getCurso().setAtivarMatriculaAposAssinaturaContrato(dadosSQL.getBoolean("Curso.ativarMatriculaAposAssinaturaContrato"));	
		obj.getCurso().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Unidade
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("UnidadeEnsino.codigo"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("UnidadeEnsino.nome"));
		obj.getUnidadeEnsino().setNomeExpedicaoDiploma(dadosSQL.getString("UnidadeEnsino.nomeExpedicaoDiploma"));
		obj.getUnidadeEnsino().setCNPJ(dadosSQL.getString("UnidadeEnsino.cnpj"));
		obj.getUnidadeEnsino().setRazaoSocial(dadosSQL.getString("UnidadeEnsino.razaoSocial"));
		obj.getUnidadeEnsino().setCaminhoBaseLogo(dadosSQL.getString("UnidadeEnsino.caminhoBaseLogo"));
		obj.getUnidadeEnsino().setCaminhoBaseLogoIndex(dadosSQL.getString("UnidadeEnsino.caminhoBaseLogoIndex"));
		obj.getUnidadeEnsino().setNomeArquivoLogoIndex(dadosSQL.getString("UnidadeEnsino.nomeArquivoLogoIndex"));
		obj.getUnidadeEnsino().setNomeArquivoLogo(dadosSQL.getString("UnidadeEnsino.nomeArquivoLogo"));
		obj.getUnidadeEnsino().getCidade().setCodigo(dadosSQL.getInt("UnidadeEnsino.cidade"));
		obj.getUnidadeEnsino().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Conta Corrente UnidadeEnsino
		//obj.getUnidadeEnsino().getContaCorrentePadraoVO().setCodigo(dadosSQL.getInt("contaCorrente.codigo"));
		obj.getUnidadeEnsino().setContaCorrentePadraoMensalidade(dadosSQL.getInt("contaCorrentePadraoMensalidade"));
		obj.getUnidadeEnsino().setContaCorrentePadraoMatricula(dadosSQL.getInt("contaCorrentePadraoMatricula"));
		obj.getUnidadeEnsino().setContaCorrentePadraoBiblioteca(dadosSQL.getInt("contaCorrentePadraoBiblioteca"));
		obj.getUnidadeEnsino().setContaCorrentePadraoDevolucaoCheque(dadosSQL.getInt("contaCorrentePadraoDevolucaoCheque"));
		obj.getUnidadeEnsino().setContaCorrentePadraoMaterialDidatico(dadosSQL.getInt("contaCorrentePadraoMaterialDidatico"));
		obj.getUnidadeEnsino().setContaCorrentePadraoNegociacao(dadosSQL.getInt("contaCorrentePadraoNegociacao"));
		obj.getUnidadeEnsino().setContaCorrentePadraoProcessoSeletivo(dadosSQL.getInt("contaCorrentePadraoProcessoSeletivo"));
//		obj.getUnidadeEnsino().getContaCorrentePadraoVO().setNumero(dadosSQL.getString("contacorrente.numero"));
//		obj.getUnidadeEnsino().getContaCorrentePadraoVO().setDigito(dadosSQL.getString("contacorrente.digito"));
//		obj.getUnidadeEnsino().getContaCorrentePadraoVO().getAgencia().setCodigo(dadosSQL.getInt("contacorrente.agencia"));
//		obj.getUnidadeEnsino().getContaCorrentePadraoVO().setNivelMontarDados(NivelMontarDados.BASICO);
		// Dados da Configurações da UnidadeEnsino
		obj.getUnidadeEnsino().getConfiguracoes().setCodigo(dadosSQL.getInt("Configuracoes.codigo"));
		obj.getUnidadeEnsino().getConfiguracoes().setNome(dadosSQL.getString("Configuracoes.nome"));
		// Dados do Turno
		obj.getTurno().setCodigo(dadosSQL.getInt("Turno.codigo"));
		obj.getTurno().setNome(dadosSQL.getString("Turno.nome"));
		obj.getUsuario().setCodigo(dadosSQL.getInt("Matricula.responsavelMatricula"));
	    
		//montar dados matricula enade
		obj.getMatriculaEnadeVO().setCodigo(dadosSQL.getInt("codigo_matriculaEnade"));
		obj.setDataEnade(dadosSQL.getDate("matriculaenade_dataenade"));			
		obj.getMatriculaEnadeVO().getTextoEnade().setCodigo(dadosSQL.getInt("codigo_textoenade"));
		obj.getMatriculaEnadeVO().getTextoEnade().setTexto(dadosSQL.getString("textoenade_texto"));
		if(dadosSQL.getString("textoenade_texto") != null && !dadosSQL.getString("textoenade_texto").trim().isEmpty()){
			obj.getMatriculaEnadeVO().getTextoEnade().setTipoTextoEnade(TipoTextoEnadeEnum.valueOf(dadosSQL.getString("textoenade_tipoTextoEnade")));
		}
		obj.getMatriculaEnadeVO().getMatriculaVO().setMatricula(dadosSQL.getString("matriculaenade_matricula"));
		obj.getMatriculaEnadeVO().getMatriculaVO().getAluno().setCodigo(dadosSQL.getInt("Pessoa.codigo"));
		obj.getMatriculaEnadeVO().getMatriculaVO().getAluno().setNome(dadosSQL.getString("Pessoa.nome"));
		obj.getMatriculaEnadeVO().getEnadeVO().setCodigo(dadosSQL.getInt("codigo_enade"));
		obj.getMatriculaEnadeVO().getEnadeVO().setTituloEnade(dadosSQL.getString("enade_tituloenada"));
		obj.getMatriculaEnadeVO().getEnadeVO().setDataPortaria(dadosSQL.getDate("enade_dataPortaria"));
		
	}
		
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataColacaoGrauMatriculaPorColacaoGrau (Integer colacaoGrau, Date dataColacaoGrau, UsuarioVO usuarioVO) throws Exception{
		StringBuilder sqlStr = new StringBuilder(" update matricula set dataColacaoGrau = ? from programacaoformaturaaluno ");
		sqlStr.append(" WHERE programacaoformaturaaluno.matricula =  matricula.matricula and programacaoformaturaaluno.colacaograu = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());
				if(dataColacaoGrau != null) {
					sqlAlterar.setDate(1, Uteis.getDataJDBC(dataColacaoGrau));
				}else {
					sqlAlterar.setNull(1, 0);
	}
				sqlAlterar.setInt(2, colacaoGrau);
				return sqlAlterar;
			}
		});	
	}
@Override
	public Boolean verificarExisteMultiplasMatriculasAtivasDominioLDAP(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" select count(*) as qtde from matricula");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso");
		sqlStr.append(" inner join configuracaoldap on configuracaoldap.codigo = curso.configuracaoldap ");
		sqlStr.append(" where aluno = ").append(matriculaVO.getAluno().getCodigo());
		sqlStr.append(" and matricula.situacao IN ('AT', 'FI')");
		sqlStr.append(" and matricula.matricula <> '").append(matriculaVO.getMatricula()).append("'");
		sqlStr.append(" and configuracaoldap.dominio = '").append(matriculaVO.getCurso().getConfiguracaoLdapVO().getDominio()).append("'");
		sqlStr.append(" group by matricula.aluno having count (*) > 0");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			if (tabelaResultado.getInt("qtde") > 0) {
				return true;
			}
		}
		return false;
	}
	
	
	@Override
    public void realizarAlteracaoSituacaoMatriculaIntegralizada(UsuarioVO usuarioVO)  throws Exception { 		
		SqlRowSet tabelaResultado = 	getConexao().getJdbcTemplate().queryForRowSet(" select * from fn_alterarSituacaoMatriculaIntegralizada_finalizada("+(usuarioVO.getCodigo())+ ")" );
		if (tabelaResultado.next()) {
			if (!tabelaResultado.getBoolean("fn_alterarsituacaomatriculaintegralizada_finalizada")) {
				throw new Exception("Não foi possivel realizar a operação ");
			}
		}		
	}
	
	@Override
	public void realizarCriacaoMatriculaPorTransferenciaInternaOrigemRequerimento(TransferenciaEntradaVO transferenciaEntradaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean apresentarVisaoAluno, ProcessoMatriculaVO processoMatriculaVO, UsuarioVO usuario)throws Exception {
		    List<HistoricoVO> historicos = new ArrayList<HistoricoVO>(0);
			MatriculaVO novaMatricula = new MatriculaVO();
			MatriculaPeriodoVO novaMatriculaPeriodo = new MatriculaPeriodoVO();	
		    novaMatriculaPeriodo.setNovoObj(true);
		    novaMatricula.setNovoObj(true);		  
		    novaMatricula.setTransferenciaEntrada(transferenciaEntradaVO);
		    novaMatricula.setFormaIngresso(FormaIngresso.TRANSFERENCIA_INTERNA.getValor());
		    novaMatricula.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(transferenciaEntradaVO.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		    novaMatricula.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(transferenciaEntradaVO.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false,usuario));
		    novaMatriculaPeriodo.setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(transferenciaEntradaVO.getCurso().getCodigo(), transferenciaEntradaVO.getUnidadeEnsino().getCodigo(), transferenciaEntradaVO.getTurno().getCodigo(), usuario));			
		    novaMatriculaPeriodo.setUnidadeEnsinoCurso(novaMatriculaPeriodo.getUnidadeEnsinoCursoVO().getCodigo());
		    novaMatriculaPeriodo.getUnidadeEnsinoCursoVO().setCodigo(transferenciaEntradaVO.getUnidadeEnsinoCurso());
		    novaMatricula.setAluno(transferenciaEntradaVO.getPessoa());
		    novaMatricula.setTurno(transferenciaEntradaVO.getTurno());		
		    novaMatriculaPeriodo.setPeridoLetivo(transferenciaEntradaVO.getPeridoLetivo());		
		    novaMatriculaPeriodo.setGradeCurricular(transferenciaEntradaVO.getGradeCurricular());		    
		    novaMatriculaPeriodo.setTranferenciaEntrada(transferenciaEntradaVO.getCodigo());
		    novaMatricula.setGradeCurricularAtual(novaMatriculaPeriodo.getGradeCurricular());
		    novaMatriculaPeriodo.setTurma(transferenciaEntradaVO.getTurma());		
		    novaMatricula.setAnoIngresso(transferenciaEntradaVO.getMatricula().getAnoIngresso());
		    novaMatricula.setSemestreIngresso(transferenciaEntradaVO.getMatricula().getSemestreIngresso());
		    novaMatricula.setMesIngresso(transferenciaEntradaVO.getMatricula().getMesIngresso());
		    novaMatricula.setDataInicioCurso(transferenciaEntradaVO.getMatricula().getDataInicioCurso());
		    novaMatricula.setDiaSemanaAula(transferenciaEntradaVO.getMatricula().getDiaSemanaAula());
		    getFacadeFactory().getMatriculaFacade().verificaAlunoJaMatriculado(novaMatricula, false, configuracaoFinanceiroVO, usuario ,false, true);						
			getFacadeFactory().getTurmaFacade().processarDadosPermitinentesTurmaSelecionada(novaMatricula, novaMatriculaPeriodo, configuracaoFinanceiroVO, usuario);
			if(!Uteis.isAtributoPreenchido(processoMatriculaVO)) {
				novaMatriculaPeriodo.setAno(Uteis.getAnoDataAtual());
				novaMatriculaPeriodo.setSemestre(Uteis.getSemestreAtual());				
				List<ProcessoMatriculaVO> processoMatriculaVOs = getFacadeFactory().getProcessoMatriculaFacade().consultarProcessosMatriculaOnline(apresentarVisaoAluno , false,  novaMatricula.getUnidadeEnsino().getCodigo(), "","", novaMatricula.getCurso().getCodigo(), novaMatricula.getTurno().getCodigo(), usuario, 1, true,Uteis.NIVELMONTARDADOS_DADOSBASICOS,true, false);
				if(processoMatriculaVOs.isEmpty()){
					throw new Exception("Não foi encontrado nenhum CALENDÁRIO DE MATRÍCULA habilitada para a realização da matrícula.");
				}						
				processoMatriculaVO = processoMatriculaVOs.get(0);
			}
			novaMatriculaPeriodo.setSemestre(processoMatriculaVO.getSemestre());
			novaMatriculaPeriodo.setAno(processoMatriculaVO.getAno());
			novaMatriculaPeriodo.setProcessoMatriculaVO(getFacadeFactory().getProcessoMatriculaFacade().consultarPorChavePrimaria(processoMatriculaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			novaMatriculaPeriodo.setProcessoMatricula(novaMatriculaPeriodo.getProcessoMatriculaVO().getCodigo());
			novaMatriculaPeriodo.setProcessoMatriculaCalendarioVO(getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorChavePrimaria(novaMatriculaPeriodo.getUnidadeEnsinoCursoVO().getCurso().getCodigo(), novaMatriculaPeriodo.getUnidadeEnsinoCursoVO().getTurno().getCodigo(), processoMatriculaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			novaMatriculaPeriodo.getProcessoMatriculaCalendarioVO().setNivelMontarDados(NivelMontarDados.BASICO);
			getFacadeFactory().getMatriculaPeriodoFacade().definirSituacaoMatriculaPeriodoComBaseProcesso(novaMatricula, novaMatriculaPeriodo, configuracaoFinanceiroVO, usuario);
			getFacadeFactory().getMatriculaFacade().adicionarObjMatriculaPeriodoVOs(novaMatriculaPeriodo, novaMatricula);
			novaMatricula.getPlanoFinanceiroAluno().setResponsavel(usuario);
			novaMatricula.setUsuario(usuario);
			getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosAnoSemestreMatricula(novaMatriculaPeriodo,novaMatriculaPeriodo.getProcessoMatriculaCalendarioVO(), false);			
			getFacadeFactory().getMatriculaFacade().realizarDefinicaoGradeDisciplinaHistoricoAproveitarEspecificoTransferenciaInterna(transferenciaEntradaVO, usuario, novaMatricula, novaMatriculaPeriodo, historicos);
			
			getFacadeFactory().getMatriculaFacade().inicializarDocumentacaoMatriculaCurso(novaMatricula, usuario);
			getFacadeFactory().getMatriculaFacade().incluir(novaMatricula, novaMatriculaPeriodo, novaMatriculaPeriodo.getProcessoMatriculaCalendarioVO(), novaMatriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso(), configuracaoFinanceiroVO, configuracaoGeralSistema, true, false, false, false, usuario);
			getFacadeFactory().getHistoricoFacade().realizarInclusaoHistoricosAntigosNovaMatriculaPorMatriculaAnterior(novaMatricula, novaMatriculaPeriodo , transferenciaEntradaVO.getMatricula() ,historicos, usuario);
			getFacadeFactory().getRequerimentoFacade().alterarSituacao(transferenciaEntradaVO.getCodigoRequerimento().getCodigo(), SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor(), "", "",transferenciaEntradaVO.getCodigoRequerimento(), usuario);

	}

	@Override
	public List<MatriculaVO> consultaRapidaPorNomePessoa(DataModelo dataModeloAluno, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, boolean controlarAcesso, String situacao, String nivelEducacional, Integer curso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);			
		StringBuilder sqlStr = new StringBuilder(" SELECT count(*) over() as qtde_total_registros, * FROM (");
	    sqlStr.append( getSQLPadraoConsultaBasicaStringBuilder());
	    sqlStr.append(" WHERE sem_acentos(Pessoa.nome) ilike sem_acentos(?) ");

		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" AND (Curso.niveleducacional = '").append(nivelEducacional).append("')");
		}

		if (Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaUnidadeEnsinoVOs)) {
			sqlStr.append(" AND (UnidadeEnsino.codigo in (");
			for (ControleLivroRegistroDiplomaUnidadeEnsinoVO listaUnidade : controleLivroRegistroDiplomaUnidadeEnsinoVOs) {
				if (listaUnidade.equals(controleLivroRegistroDiplomaUnidadeEnsinoVOs.get(controleLivroRegistroDiplomaUnidadeEnsinoVOs.size() -1))) {
					sqlStr.append(listaUnidade.getUnidadeEnsino().getCodigo()).append(") ");
				} else {
					sqlStr.append(listaUnidade.getUnidadeEnsino().getCodigo()).append(", ");
				}
			}
			sqlStr.append(") ");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" AND Curso.codigo = ").append(curso).append(" ");
		}
		sqlStr.append(" ) AS t ");
		sqlStr.append(" ORDER BY \"Pessoa.nome\" ");
		UteisTexto.addLimitAndOffset(sqlStr, dataModeloAluno.getLimitePorPagina(), dataModeloAluno.getOffset());
		
		dataModeloAluno.setTotalRegistrosEncontrados(0);		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(),  dataModeloAluno.getValorConsulta() +PERCENT);
		if (tabelaResultado.next()) {
			dataModeloAluno.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		return montarDadosConsultaBasica(tabelaResultado);
	}
	
	@Override
	public List<MatriculaVO> consultaRapidaPorMatriculaAlunoPorDataModelo(String valorConsulta, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, DataModelo dataModeloAluno, boolean controlarAcesso, String situacao, String nivelEducacional, Integer curso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT count(*) over() as qtde_total_registros, * FROM (");
	    sqlStr.append( getSQLPadraoConsultaBasicaStringBuilder());
		sqlStr.append(" WHERE sem_acentos(matricula.matricula) ilike sem_acentos(?) ");

		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" AND (Curso.niveleducacional = '").append(nivelEducacional).append("')");
		}
		if (Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaUnidadeEnsinoVOs)) {
			sqlStr.append(" AND (UnidadeEnsino.codigo in (");
			for (ControleLivroRegistroDiplomaUnidadeEnsinoVO listaUnidade : controleLivroRegistroDiplomaUnidadeEnsinoVOs) {
				if (listaUnidade.equals(controleLivroRegistroDiplomaUnidadeEnsinoVOs.get(controleLivroRegistroDiplomaUnidadeEnsinoVOs.size() -1))) {
					sqlStr.append(listaUnidade.getUnidadeEnsino().getCodigo()).append(") ");
				} else {
					sqlStr.append(listaUnidade.getUnidadeEnsino().getCodigo()).append(", ");
				}
			}
			sqlStr.append(") ");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" AND Curso.codigo = ").append(curso).append(" ");
		}
		sqlStr.append(" ) AS t ");
		sqlStr.append(" ORDER BY \"Pessoa.nome\" ");
		UteisTexto.addLimitAndOffset(sqlStr, dataModeloAluno.getLimitePorPagina(), dataModeloAluno.getOffset());
		
		dataModeloAluno.setTotalRegistrosEncontrados(0);	
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta +PERCENT);
		if (tabelaResultado.next()) {
			dataModeloAluno.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		return montarDadosConsultaBasica(tabelaResultado);
	}
	
	@Override
	public List<MatriculaVO> consultaRapidaPorRegistroAcademicoAlunoPorDataModelo(String valorConsulta, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, DataModelo dataModeloAluno, boolean controlarAcesso, String situacao, String nivelEducacional, Integer curso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT count(*) over() as qtde_total_registros, * FROM (");
	    sqlStr.append( getSQLPadraoConsultaBasicaStringBuilder());
		sqlStr.append(" WHERE sem_acentos(Pessoa.registroAcademico) ilike sem_acentos(?) ");

		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" AND (Curso.niveleducacional = '").append(nivelEducacional).append("')");
		}
		if (Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaUnidadeEnsinoVOs)) {
			sqlStr.append(" AND (UnidadeEnsino.codigo in (");
			for (ControleLivroRegistroDiplomaUnidadeEnsinoVO listaUnidade : controleLivroRegistroDiplomaUnidadeEnsinoVOs) {
				if (listaUnidade.equals(controleLivroRegistroDiplomaUnidadeEnsinoVOs.get(controleLivroRegistroDiplomaUnidadeEnsinoVOs.size() -1))) {
					sqlStr.append(listaUnidade.getUnidadeEnsino().getCodigo()).append(") ");
				} else {
					sqlStr.append(listaUnidade.getUnidadeEnsino().getCodigo()).append(", ");
				}
			}
			sqlStr.append(") ");
		}
		if (situacao != null && !situacao.equals("")) {
			sqlStr.append(" AND (Matricula.situacao = '").append(situacao).append("') ");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" AND Curso.codigo = ").append(curso).append(" ");
		}
		sqlStr.append(" ) AS t ");
		sqlStr.append(" ORDER BY \"Pessoa.registroAcademico\" ");
		UteisTexto.addLimitAndOffset(sqlStr, dataModeloAluno.getLimitePorPagina(), dataModeloAluno.getOffset());
		
		dataModeloAluno.setTotalRegistrosEncontrados(0);		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta +PERCENT);
		if (tabelaResultado.next()) {
			dataModeloAluno.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		return montarDadosConsultaBasica(tabelaResultado);
	}
	
	@Override
	public MatriculaVO consultarPorChavePrimariaEUnidadesSelecionadas(String matriculaPrm, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, Integer curso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM Matricula WHERE matricula= '" + matriculaPrm + "'";
		if (Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaUnidadeEnsinoVOs)) {
			sqlStr = "SELECT * FROM Matricula WHERE matricula = '" + matriculaPrm + "' and unidadeEnsino in (";
			for (ControleLivroRegistroDiplomaUnidadeEnsinoVO unidade : controleLivroRegistroDiplomaUnidadeEnsinoVOs) {
				if (unidade.equals(controleLivroRegistroDiplomaUnidadeEnsinoVOs.get(controleLivroRegistroDiplomaUnidadeEnsinoVOs.size() -1))) {
					sqlStr += unidade.getUnidadeEnsino().getCodigo() + ")";
				} else {
					sqlStr += unidade.getUnidadeEnsino().getCodigo() + ",";
				}
			}
		}
		if ((curso != null) && (curso.intValue() != 0)) {
			sqlStr += " and curso=" + curso;
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}
	
	@Override
	public MatriculaVO consultarMatriculaPorRegistroAcademico(String registroAcademico, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, Integer curso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM Matricula   inner join pessoa on pessoa.codigo = matricula.aluno  "
				+ " WHERE pessoa.registroAcademico= '" + registroAcademico + "'";
		if (Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaUnidadeEnsinoVOs)) {
			sqlStr = "SELECT * FROM Matricula  inner join pessoa on pessoa.codigo = matricula.aluno  "
			      +"   WHERE  pessoa.registroAcademico= '" + registroAcademico + "' and unidadeEnsino in (";
			for (ControleLivroRegistroDiplomaUnidadeEnsinoVO unidade : controleLivroRegistroDiplomaUnidadeEnsinoVOs) {
				if (unidade.equals(controleLivroRegistroDiplomaUnidadeEnsinoVOs.get(controleLivroRegistroDiplomaUnidadeEnsinoVOs.size() -1))) {
					sqlStr += unidade.getUnidadeEnsino().getCodigo() + ")";
				} else {
					sqlStr += unidade.getUnidadeEnsino().getCodigo() + ",";
				}
			}
		}
		if ((curso != null) && (curso.intValue() != 0)) {
			sqlStr += " and curso=" + curso;
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
	}
	
	/*
	 * consulta especifica para ControleLivroRegistroDiploma
	 */
	
	@Override
	public MatriculaVO consultarPorChavePrimariaEUnidadesEnsinos(String matriculaPrm, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		MatriculaVO obj = new MatriculaVO();
		obj.setNovoObj(false);
		obj.setMatricula(matriculaPrm);
		carregarDados(obj, controleLivroRegistroDiplomaUnidadeEnsinoVOs, nivelMontarDados, usuario);
		return obj;
	}
	
	/*
	 * carregarDados especifico para lista de controleLivroRegistroDiplomaUnidadeEnsino
	 */
	
	public void carregarDados(MatriculaVO obj, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsino, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		resultado = consultaRapidaPorChavePrimariaEUnidadeEnsinosDadosCompletos(obj.getMatricula(), controleLivroRegistroDiplomaUnidadeEnsino, usuario);
		montarDadosCompleto((MatriculaVO) obj, resultado, usuario);
	}
	
	private SqlRowSet consultaRapidaPorChavePrimariaEUnidadeEnsinosDadosCompletos(String matricula, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsino, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (Matricula.matricula= ? )");
		if (Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaUnidadeEnsino)) {
			sqlStr.append(" and Matricula.unidadeEnsino in (");
			for (ControleLivroRegistroDiplomaUnidadeEnsinoVO unidade : controleLivroRegistroDiplomaUnidadeEnsino) {
				if (unidade.equals(controleLivroRegistroDiplomaUnidadeEnsino.get(controleLivroRegistroDiplomaUnidadeEnsino.size() -1))) {
					sqlStr.append(unidade.getUnidadeEnsino().getCodigo()).append(")");
				} else {
					sqlStr.append(unidade.getUnidadeEnsino().getCodigo()).append(",");
				}
			}
			sqlStr.append(" AND MatriculaPeriodo.situacaoMatriculaPeriodo <> 'PC' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados(Matrícula).");
		}
		return tabelaResultado;
	}

	@Override
	public List<MatriculaVO> consultarPorProgramacaoFormatura(Integer codigoProgramacao, String ColouGrau, Integer curso, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, int nivelmontardados, ConfiguracaoFinanceiroVO configuracaoFinanceiroPadraoSistema, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT matricula.matricula, integralizacao.integralizado integralizado, matricula.situacao situacaoMatricula, ");
		sql.append("pessoa.codigo codigoPessoa, pessoa.nome nomePessoa, pessoa.cpf cpfPessoa, pessoa.registroacademico, ");
		sql.append("curso.codigo codigoCurso, curso.nome nomeCurso, curso.niveleducacional, ");
		sql.append("unidadeensino.codigo codigoUnidadeEnsino, unidadeensino.nome nomeUnidadeEnsino, ");
		sql.append("turno.codigo codigoTurno, turno.nome nomeTurno, ");
		sql.append("EXISTS (SELECT FROM expedicaodiploma e WHERE e.matricula = matricula.matricula) existeDiploma ");
		sql.append("FROM programacaoformaturaaluno ");
		sql.append("INNER JOIN matricula ON programacaoformaturaaluno.matricula = matricula.matricula ");
		sql.append("INNER JOIN pessoa ON pessoa.codigo = matricula.aluno ");
		sql.append("INNER JOIN curso ON curso.codigo = matricula.curso ");
		sql.append("INNER JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino ");
		sql.append("INNER JOIN turno ON turno.codigo = matricula.turno ");
		sql.append("INNER JOIN LATERAL ( SELECT CASE WHEN intr.percentualintegralizado >= 100 THEN TRUE ELSE FALSE END integralizado FROM matriculaintegralizacaocurricular(matricula.matricula) intr) AS integralizacao ON TRUE ");
		sql.append("WHERE programacaoformaturaaluno.programacaoFormatura = " + codigoProgramacao);
		if (!ColouGrau.equals("ambos")) {
			if (ColouGrau.equals(SituacaoColouGrauProgramacaoFormaturaAluno.COLOU_GRAU.getValor())) {
				sql.append("AND programacaoformaturaaluno.colougrau = 'SI' ");
			} else if (ColouGrau.equals(SituacaoColouGrauProgramacaoFormaturaAluno.NAO_COLOU.getValor())) {
				sql.append("AND programacaoformaturaaluno.colougrau = 'NO' ");
			} else if (ColouGrau.equals(SituacaoColouGrauProgramacaoFormaturaAluno.NAO_INFORMADO.getValor())) {
				sql.append("AND programacaoformaturaaluno.colougrau = 'NI' ");
			}
		}
		if(Uteis.isAtributoPreenchido(curso)) {
			sql.append(" and matricula.curso = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sql.append(" and Matricula.unidadeEnsino in (");
			for (ControleLivroRegistroDiplomaUnidadeEnsinoVO unidade : unidadeEnsinoVOs) {
				if (unidade.equals(unidadeEnsinoVOs.get(unidadeEnsinoVOs.size() -1))) {
					sql.append(unidade.getUnidadeEnsino().getCodigo()).append(")");
				} else {
					sql.append(unidade.getUnidadeEnsino().getCodigo()).append(",");
				}
			}			
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<MatriculaVO> lista = new ArrayList<>();
		while (tabelaResultado.next()) {
			MatriculaVO matricula = new MatriculaVO();
			matricula.setMatricula(tabelaResultado.getString("matricula"));
			matricula.setSituacao(tabelaResultado.getString("situacaoMatricula"));
			matricula.setMatriculaIntegralizada(tabelaResultado.getBoolean("integralizado"));
			matricula.setExisteDiploma(tabelaResultado.getBoolean("existeDiploma"));
			matricula.getAluno().setCodigo(tabelaResultado.getInt("codigoPessoa"));
			matricula.getAluno().setRegistroAcademico(tabelaResultado.getString("registroacademico"));
			matricula.getAluno().setNome(tabelaResultado.getString("nomePessoa"));
			matricula.getAluno().setCPF(tabelaResultado.getString("cpfPessoa"));
			matricula.getCurso().setCodigo(tabelaResultado.getInt("codigoCurso"));
			matricula.getCurso().setNome(tabelaResultado.getString("nomeCurso"));
			matricula.getCurso().setNivelEducacional(tabelaResultado.getString("niveleducacional"));
			matricula.getUnidadeEnsino().setCodigo(tabelaResultado.getInt("codigoUnidadeEnsino"));
			matricula.getUnidadeEnsino().setNome(tabelaResultado.getString("nomeUnidadeEnsino"));
			matricula.getTurno().setCodigo(tabelaResultado.getInt("codigoTurno"));
			matricula.getTurno().setNome(tabelaResultado.getString("nomeTurno"));
			lista.add(matricula);
		}
		if (!Uteis.isAtributoPreenchido(lista)) {
			throw new ConsistirException("A Programação de Formatura informada não possui matricula(s).");
		}
		return lista;
	}

	@Override
	public List consultaRapidaResumidaSemProgramacaoFormatura(ControleConsulta controleConsulta, DataModelo controleConsultaOtimizado, List<ProgramacaoFormaturaUnidadeEnsinoVO> listaProgramacao, List<TurnoVO> listaTurnos, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		if (controleConsulta.getCampoConsulta().equals("matricula")) {
			sqlStr.append(" WHERE (matricula.matricula like(?)) ");
		} else if (controleConsulta.getCampoConsulta().equals("registroAcademico")) {
			sqlStr.append(" WHERE (Pessoa.registroAcademico ilike (?))  ");
		} else if (controleConsulta.getCampoConsulta().equals("nomePessoa")) {
			sqlStr.append(" WHERE (sem_acentos(Pessoa.nome) ilike(sem_acentos(?))) ");
		} else if (controleConsulta.getCampoConsulta().equals("nomeCurso")) {
			sqlStr.append(" WHERE (sem_acentos (Curso.nome) ilike sem_acentos(?)) ");
		}
		sqlStr.append(" AND (NOT EXISTS ( SELECT FROM programacaoformaturaaluno WHERE programacaoformaturaaluno.matricula = matricula.matricula) ");
		sqlStr.append(" OR (NOT EXISTS ( SELECT FROM programacaoformaturaaluno WHERE programacaoformaturaaluno.matricula = matricula.matricula AND programacaoformaturaaluno.colougrau IN ('SI', 'NI')))) ");
		sqlStr.append(" AND matricula.situacao in ('AT', 'FI') ");
		if (Uteis.isAtributoPreenchido(listaTurnos)) {
			sqlStr.append(" AND turno.codigo in (");
			for (TurnoVO turno : listaTurnos) {
				if (!listaTurnos.get(listaTurnos.size() - 1).getCodigo().equals(turno.getCodigo())) {
					sqlStr.append(turno.getCodigo()+", ");
				} else {
					sqlStr.append(turno.getCodigo());
				}	
			}
			sqlStr.append(") ");
		}
		if (Uteis.isAtributoPreenchido(listaProgramacao)) {
			sqlStr.append(" AND UnidadeEnsino.codigo in (");
			for (ProgramacaoFormaturaUnidadeEnsinoVO programacaoFormaturaUnidadeEnsinoVO : listaProgramacao) {
				if (!listaProgramacao.get(listaProgramacao.size() - 1).getUnidadeEnsinoVO().getCodigo().equals(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo())) {
					sqlStr.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo()+", ");
				} else {
					sqlStr.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo());
				}
			}
			sqlStr.append(") ");
		}
		if (controleConsulta.getCampoConsulta().equals("matricula")) {
			sqlStr.append(" ORDER BY matricula.matricula ");
		} else if (controleConsulta.getCampoConsulta().equals("registroAcademico")) {
			sqlStr.append(" ORDER BY Pessoa.registroAcademico ");
		} else if (controleConsulta.getCampoConsulta().equals("nomePessoa")) {
			sqlStr.append(" ORDER BY Pessoa.nome ");
		} else if (controleConsulta.getCampoConsulta().equals("nomeCurso")) {
			sqlStr.append(" ORDER BY Curso.nome ");
		}
		sqlStr.append(" LIMIT ").append(controleConsultaOtimizado.getLimitePorPagina());
		sqlStr.append(" OFFSET ").append(controleConsultaOtimizado.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), controleConsulta.getValorConsulta() + PERCENT);
		controleConsultaOtimizado.setTotalRegistrosEncontrados(tabelaResultado.next() ? tabelaResultado.getInt("qtde_total_registros") : 0);
		tabelaResultado.beforeFirst();
        return (montarDadosConsultaBasica(tabelaResultado));
	}
	
	
	

	@Override
	public Date obterDataColacaoGrauMatricula(String matricula) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT case when (matricula.datacolacaograu is null or matricula.datacolacaograu is not null) and colacaograu.data is not null then colacaograu.data else matricula.datacolacaograu end as datacolacaograu ");
		sql.append(" FROM matricula ");
		sql.append(" LEFT JOIN programacaoformaturaaluno ON programacaoformaturaaluno.matricula = matricula.matricula AND programacaoformaturaaluno.colougrau = 'SI' ");
		sql.append(" LEFT JOIN colacaograu ON colacaograu.codigo = programacaoformaturaaluno.colacaograu ");
		sql.append(" WHERE matricula.matricula = ? ");
		sql.append(" ORDER BY datacolacaograu DESC limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		return tabelaResultado.next() ? tabelaResultado.getDate("datacolacaograu") : null;
	}
	
	@Override
	public Boolean verificarBloqueioPorSolicitacaoLiberacaoMatricula(String matricula) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT bloqueioporsolicitacaoliberacaomatricula AS \"bloqueioporsolicitacaoliberacaomatricula\" FROM matricula ");
		sql.append("WHERE matricula = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		return tabelaResultado.next() ? tabelaResultado.getBoolean("bloqueioporsolicitacaoliberacaomatricula") : false;
	}

	@Override
	public List<MatriculaVO> consultarMatriculasAtivaOuTrancadaOutroCursoPermitiOutraMatriculaMesmoAluno(Integer curso, Integer aluno, Integer inscricao , Boolean controlarAcesso,  UsuarioVO usuarioVO) throws Exception {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append(" SELECT matricula.matricula , matricula.situacao  FROM matricula ");
			sqlStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso");
			sqlStr.append(" WHERE matricula.situacao in ('AT','TR') AND matricula.aluno = ?  and( matricula.inscricao is null or matricula.inscricao <> ? ) ");			
			sqlStr.append(" and curso.permitirOutraMatriculaMesmoAluno = false    ");	
			sqlStr.append(" and exists ( select c.codigo from curso c where c.codigo = ? and c.permitirOutraMatriculaMesmoAluno = false ) ");	
			sqlStr.append(" ORDER BY matricula.situacao  ");					

			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), aluno,inscricao, curso);		
			List<MatriculaVO> vetResultado = new ArrayList<MatriculaVO>(0);
			while (tabelaResultado.next()) {
				MatriculaVO obj = new MatriculaVO();
				obj.setMatricula(tabelaResultado.getString("matricula"));
				obj.setSituacao(tabelaResultado.getString("situacao"));				
				vetResultado.add(obj);
			}
			return vetResultado;		
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void verificarExistenciaPreencherDadosPertinentesMatriculaOnlineRealizadaPendendeAssinaturaContratoEletronicoEntregaDocumento(
			MatriculaRSVO matriculaRSVO, Boolean somenteGravarSessao, String actionNavegador ,  UsuarioVO usuarioVO, 
			boolean gerarContratoValidandoDocObrigatorio, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, 
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO ,RenovarMatriculaControle renovarMatriculaControle) throws Exception {
		 
		   MatriculaVO matricula = null ;			
		   InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().validarSessaoNavegadorHomeCandidatoProcessoSeletivoRetornandoCodigoAutenticacaoNavegador(matriculaRSVO.getCodigoInscricao() ,matriculaRSVO.getCodigoAutenticacaoNavegador(),matriculaRSVO.getNavegadorAcesso(), somenteGravarSessao ,actionNavegador, usuarioVO); 		
		   matriculaRSVO.setCodigoAutenticacaoNavegador(inscricaoVO.getCodigoAutenticacaoNavegador());		
		   PeriodoChamadaProcSeletivoVO periodoChamadaProcSeletivoVO = getFacadeFactory().getPeriodoChamadaProcSeletivoFacade().consultarPorCodigoProcessoSeletivoNumeroChamada(inscricaoVO.getProcSeletivo().getCodigo(), inscricaoVO.getChamada(),  Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		   Uteis.checkState(!Uteis.isAtributoPreenchido(periodoChamadaProcSeletivoVO), "Não foi encontrado nenhum PERÍODO DE CHAMADA DO PROCESSO SELETIVO para a chamada de número ( "+inscricaoVO.getChamada()+" ) habilitada para a realização da matrícula on-line.");			
		   try {
			try {
		  	   matricula = getFacadeFactory().getMatriculaFacade().consultarPorInscricao(matriculaRSVO.getCodigoInscricao(), false, Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuarioVO);		
			}catch (ConsistirException e) {}
			
			if(matricula != null && Uteis.isAtributoPreenchido(matricula.getMatricula())) { 		
				try {
					 getFacadeFactory().getMatriculaFacade().realizarMontagemDadosMatriculaOnlineRealizada(matriculaRSVO, matricula, matricula.getMatriculaPeriodoVOs().get(0), configuracaoGeralSistema, configuracaoFinanceiroVO,periodoChamadaProcSeletivoVO, gerarContratoValidandoDocObrigatorio, usuarioVO);
				} catch (Exception e) {}
				 	
	   		 }else if(renovarMatriculaControle != null){   	
	   			    Uteis.checkState(!periodoChamadaProcSeletivoVO.getDentroPrazoPeriodoChamada(), "Prezado Candidato a realização de matrícula para o curso ( "+matriculaRSVO.getCursoObject().getNome()+" ) esta fora do período de matrícula ("+periodoChamadaProcSeletivoVO.getPeriodoInicialChamada_Apresentar()+ " a " +periodoChamadaProcSeletivoVO.getPeriodoFinalChamada_Apresentar()+" ). ");
	   				matriculaRSVO.setPeriodoLetivoRSVO(getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivoMatriculaOnline(renovarMatriculaControle.getMatriculaVO().getAluno(),matriculaRSVO.getCursoObject().getGradeDisciplinaObject().getCodigo(),renovarMatriculaControle.getMatriculaPeriodoVO(),configuracaoFinanceiroVO,usuarioVO));			
	   				matriculaRSVO.getProcessoMatriculaRSVOs().addAll(getFacadeFactory().getProcessoMatriculaFacade().consultarProcessosMatriculaOnline(matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), inscricaoVO.getProcSeletivo().getAno() , inscricaoVO.getProcSeletivo().getSemestre(), matriculaRSVO.getCursoObject().getCodigo() , matriculaRSVO.getTurnoRSVO().getCodigo(),matriculaRSVO.getPeriodoLetivoRSVO().getPeriodoLetivo() <= 1 ,usuarioVO));
	   				Uteis.checkState(matriculaRSVO.getProcessoMatriculaRSVOs().isEmpty(), "Não foi encontrado nenhum CALENDÁRIO DE MATRÍCULA habilitada para a realização da matrícula on-line.");			
	   				matriculaRSVO.setProcessoMatriculaRSVO(matriculaRSVO.getProcessoMatriculaRSVOs().get(0));				
	   				matriculaRSVO.setPermiteAlunoIncluirExcluirDisciplina(matriculaRSVO.getProcessoMatriculaRSVO().getPermiteAlunoIncluirExcluirDisciplina());		
	   				matriculaRSVO.getTurmaRSVOs().addAll(getFacadeFactory().getTurmaFacade().consultarTurmaPorPeriodoLetivoUnidadeEnsinoCursoTurnoParaMatriculaOnlineProcessoSeletivo(matriculaRSVO.getPessoaObject().getCodigo() , matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), matriculaRSVO.getPeriodoLetivoRSVO().getCodigo()   ,matriculaRSVO.getPeriodoLetivoRSVO().getPeriodoLetivo(), matriculaRSVO.getCursoObject().getCodigo(), matriculaRSVO.getTurnoRSVO().getCodigo(), matriculaRSVO.getCursoObject().getGradeDisciplinaObject().getCodigo(), configuracaoFinanceiroVO, renovarMatriculaControle,usuarioVO));
	   				Uteis.checkState(matriculaRSVO.getTurmaRSVOs().isEmpty(), "Não foi encontrada nenhuma TURMA habilitada para a realização da matrícula on-line.");				
	   				matriculaRSVO.setTurmaRSVO(matriculaRSVO.getTurmaRSVOs().get(0));
	   				matriculaRSVO.setCodigoTurma(matriculaRSVO.getTurmaRSVOs().get(0).getCodigo());		
	   				matriculaRSVO.setExisteMatriculaAptaPendenteAssinaturaContrato(renovarMatriculaControle.getMatriculaVO().getCurso().getAtivarMatriculaAposAssinaturaContrato());	
	   				TextoPadraoVO contratoMatriculaCalouro = getFacadeFactory().getTextoPadraoFacade().consultarTextoPadraoContratoMatriculaPorCurso(matriculaRSVO.getCursoObject().getCodigo(), false ,Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
	   				Uteis.checkState(!Uteis.isAtributoPreenchido(contratoMatriculaCalouro), "Não foi encontrado nenhum CONTRATO MÁTRICULA vinculado ao curso ("+matriculaRSVO.getCursoObject().getNome()+") para a realização da matrícula on-line.");			
	   				List<MatriculaVO> matriculasAtivaOuTrancada = getFacadeFactory().getMatriculaFacade().consultarMatriculasAtivaOuTrancadaOutroCursoPermitiOutraMatriculaMesmoAluno(renovarMatriculaControle.getMatriculaVO().getCurso().getCodigo(),renovarMatriculaControle.getMatriculaVO().getAluno().getCodigo(),inscricaoVO.getCodigo(), false ,  usuarioVO);
	   				if(Uteis.isAtributoPreenchido(matriculasAtivaOuTrancada)) {
	   					matriculaRSVO.setPossuiMatriculaAtivaOuPreOutroCurso(Boolean.TRUE);
	   					if(Uteis.isAtributoPreenchido(configuracaoGeralSistema.getTextoOrientacaoCancelamentoPorOutraMatricula())) {
	   						matriculaRSVO.setTextoOrientacaoCancelamentoPorOutraMatricula(configuracaoGeralSistema.getTextoOrientacaoCancelamentoPorOutraMatricula());
	   					}else {
	   						StringBuilder str = new StringBuilder();
	   						Iterator<MatriculaVO> it = matriculasAtivaOuTrancada.iterator();	   						
	   						while (it.hasNext()) {
	   							MatriculaVO mat = (MatriculaVO) it.next();
								str.append(mat.getMatricula());
								if(it.hasNext()) {
									str.append(",");
								}								
							}
	   						String textoOrientacaoCancelamentoMatricula = 	matriculaRSVO.getPessoaObject().getNome() +"  Existe uma/ou mais  Matrícula (s) ("+str.toString()+") com situação  Ativa (s). Confirma o Cancelamento desta (s) Matrícula ? ";
    						matriculaRSVO.setTextoOrientacaoCancelamentoPorOutraMatricula(textoOrientacaoCancelamentoMatricula);
	   					}	
	   				}
	   				try {	   					
	   					if(contratoMatriculaCalouro.getAssinarDigitalmenteTextoPadrao()
						   && getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), usuarioVO).getConfiguracaoGedContratoVO().getAssinarDocumento()
						   && getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), usuarioVO).getConfiguracaoGedContratoVO().getProvedorAssinaturaPadraoEnum().isProvedorCertisign()) {
						  matriculaRSVO.setAssinarDigitalmenteContrato(Boolean.TRUE);
				        }
						if(contratoMatriculaCalouro.getAssinarDigitalmenteTextoPadrao()
								&& getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), usuarioVO).getConfiguracaoGedContratoVO().getAssinarDocumento()
								&& getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), usuarioVO).getConfiguracaoGedContratoVO().getProvedorAssinaturaPadraoEnum().isProvedorTechCert()) {
							matriculaRSVO.setAssinarDigitalmenteContrato(Boolean.TRUE);
						}
	   				 }catch (Exception e) {
	   					matriculaRSVO.setAssinarDigitalmenteContrato(Boolean.FALSE);
					 }
	   		 }			
			 if (Uteis.isAtributoPreenchido(inscricaoVO)) {
				getFacadeFactory().getInscricaoFacade().alterarCodigoAutenticacaoNavegador(inscricaoVO, usuarioVO);				
			 }
		   } catch (Exception e) {
			   throw e;
		}		
	}
	
	
	@Override
	public void realizarValidacaoControleDeVagasMatriculaPorEixoCursoUnidadeEnsino(MatriculaVO matriculaVO ,UsuarioVO usuarioVO)throws Exception {		
		// primeiro verificar se o curso tem eixo curso .
		if(Uteis.isAtributoPreenchido(matriculaVO.getCurso().getEixoCursoVO().getCodigo())) {
			// seguinte encontrar o processoseletivounidadeensinoeixocurso  atraves do codigo do eixo curso , da unidade ensino e codigo de inscriçao q encontrara o processo seletivo 
			// entao essa consulta traz o ProcSeletivoUnidadeEnsinoEixoCursoVO que possui o eixocurso igual o eixocurso informado no curso que estao na unidade de ensino que foi vinculado no processo seletivo da inscricao do candidato 
			
			ProcSeletivoUnidadeEnsinoEixoCursoVO obj = getFacadeFactory().getProcSeletivoUnidadeEnsinoEixoCursoFacade().consultarPorInscricaoUnidadeEnsinoEixoCurso(matriculaVO.getInscricao().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo() ,matriculaVO.getCurso().getEixoCursoVO().getCodigo() ,Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS  ,usuarioVO);
			// aqui e verificado se existe eixo curso , caso exista e verificado se o numero de vagas eixo curso e menor que a quantidade de alunos ja matriculados atraves desste processo seletivo na unidade de ensino 
			// caso seja menor o sistema  apresenta a mensagen de erro pois o processo seletivo gerencia as vagas das matriculas por eixo curso 
	    	 if(Uteis.isAtributoPreenchido(obj.getCodigo()) && (obj.getNrVagasEixoCurso() <= getFacadeFactory().getProcSeletivoFacade().verificarQuantidadeAlunosMatriculadosPorProcessoSeletivoUnidadeEnsino(obj.getProcSeletivoUnidadeEnsino().getProcSeletivo().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo(), matriculaVO.getCurso().getEixoCursoVO().getCodigo(),usuarioVO) )) {
	 		    throw new Exception("Não Foi Possível Realizar Matricula no Curso ( "+matriculaVO.getCurso().getNome()+") . Não Existe Vagas Disponíveis no Eixo Curso ("+obj.getEixoCurso().getNome()+") Informado no Processo seletivo para Unidade de Ensino ("+matriculaVO.getUnidadeEnsino().getCodigo()+").");
		     }			
			}			
	}	
	
	private void validarDadosCensoMatricula(List<MatriculaVO> matriculaVOs) throws Exception {
		for (MatriculaVO matriculaVO : matriculaVOs) {
			validarMobilidadeAcademica(matriculaVO);
			validarInformacoesCensoRelativoAno(matriculaVO);
		}
	}
	
	private void validarInformacoesCensoRelativoAno(MatriculaVO matriculaVO) throws Exception {
		if (matriculaVO.getInformacoesCensoRelativoAno().length() > 4) {
			throw new Exception("O campo Informações do Censo Relativo ao Ano possui mais de 4 dígitos.");
		}
		if ((Uteis.isAtributoPreenchido(matriculaVO.getListaFinanciamentoEstudantilVOs()) || !matriculaVO.getJustificativaCensoEnum().isNenhuma())
				&& (!Uteis.isAtributoPreenchido(matriculaVO.getInformacoesCensoRelativoAno()) || matriculaVO.getInformacoesCensoRelativoAno().length() < 4)) {
			throw new Exception("O campo Informações do Censo Relativo ao Ano deve ser informado corretamente.");
		}
		if (Uteis.isAtributoPreenchido(matriculaVO.getInformacoesCensoRelativoAno()) && !Uteis.getIsValorNumerico(matriculaVO.getInformacoesCensoRelativoAno())) {
			throw new Exception("O campo Informações do Censo Relativo ao Ano deve conter somente números.");
		}
	}
	
	private void validarMobilidadeAcademica(MatriculaVO matriculaVO) throws Exception {
		matriculaVO.setMobilidadeAcademicaInternacionalPaisDestino(matriculaVO.getMobilidadeAcademicaInternacionalPaisDestino().toUpperCase());
		if (matriculaVO.getTipoMobilidadeAcademicaEnum().isNacional() && !Uteis.isAtributoPreenchido(matriculaVO.getMobilidadeAcademicaNacionalIESDestino())) {
			throw new Exception("O campo IES Destino (Matrícula - Mobilidade Acadêmica) deve ser informado.");
		} 
		if (matriculaVO.getTipoMobilidadeAcademicaEnum().isInternacional() && !Uteis.isAtributoPreenchido(matriculaVO.getMobilidadeAcademicaInternacionalPaisDestino())) {
			throw new Exception("O campo País Destino (Matrícula - Mobilidade Acadêmica) deve ser informado.");
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarRenovacaoMatriculaAutomaticaAtravesTransferenciaInterna(TransferenciaEntradaVO transferenciaEntradaVO , PeriodoLetivoVO periodoLetivoCursar ,  Boolean apresentarVisaoAluno,  ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO,  UsuarioVO usuarioVO ) throws Exception {
		
		
		getFacadeFactory().getMatriculaFacade().carregarDados(transferenciaEntradaVO.getMatricula(), usuarioVO);		
		if (getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(transferenciaEntradaVO.getMatricula(), transferenciaEntradaVO.getMatricula().getGradeCurricularAtual().getCodigo(), usuarioVO, null) ) {
        	throw new Exception("Aluno com o histórico Integralizado.");
        }
		MatriculaPeriodoVO novaMatriculaPeriodo = getFacadeFactory().getMatriculaFacade().realizarGeracaoNovaMatriculaPeriodoParaRenovacao(transferenciaEntradaVO.getMatricula(), configuracaoFinanceiroVO, usuarioVO);
		novaMatriculaPeriodo.setData(new Date());
		getFacadeFactory().getMatriculaPeriodoFacade().validarDadosRenovacaoProcessoMatriculaPeriodoLetivo(transferenciaEntradaVO.getMatricula(), novaMatriculaPeriodo, usuarioVO, null);
		if (transferenciaEntradaVO.getMatricula().getCurso().getNivelEducacional().equals("GT") || transferenciaEntradaVO.getMatricula().getCurso().getNivelEducacional().equals("SU")) {
			getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatriculaPeriodoLetivoAtivoAnoSemestre(transferenciaEntradaVO.getMatricula().getMatricula(), novaMatriculaPeriodo.getAno(), novaMatriculaPeriodo.getSemestre(), novaMatriculaPeriodo.getCodigo(), usuarioVO);
		}
		
		novaMatriculaPeriodo.setPeridoLetivo(periodoLetivoCursar);
		
		
		List<TurmaVO> listaResultado = getFacadeFactory().getTurmaFacade().consultaRapidaPorNrPeriodoLetivoUnidadeEnsinoCursoTurno(transferenciaEntradaVO.getPeridoLetivo().getPeriodoLetivo(), transferenciaEntradaVO.getMatricula().getUnidadeEnsino().getCodigo(), transferenciaEntradaVO.getMatricula().getCurso().getCodigo(), transferenciaEntradaVO.getMatricula().getTurno().getCodigo(), transferenciaEntradaVO.getMatricula().getGradeCurricularAtual().getCodigo(), true, false,false, false, "",  "",   false, usuarioVO, configuracaoFinanceiroVO, false);
		if(listaResultado.isEmpty()) {
			throw new Exception("Não foi encontrada nenhuma TURMA habilitada para a realização da matrícula por Requerimento.");					
		}
		novaMatriculaPeriodo.setTurma(listaResultado.get(0));	
		novaMatriculaPeriodo.setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(transferenciaEntradaVO.getMatricula().getCurso().getCodigo(), transferenciaEntradaVO.getMatricula().getUnidadeEnsino().getCodigo(), transferenciaEntradaVO.getMatricula().getTurno().getCodigo(), usuarioVO));			
	    novaMatriculaPeriodo.setUnidadeEnsinoCurso(novaMatriculaPeriodo.getUnidadeEnsinoCursoVO().getCodigo());
	    getFacadeFactory().getTurmaFacade().processarDadosPermitinentesTurmaSelecionada(transferenciaEntradaVO.getMatricula(), novaMatriculaPeriodo, configuracaoFinanceiroVO, usuarioVO);
//		List<CondicaoPagamentoPlanoFinanceiroCursoVO> condicaoPagamentoPlanoFinanceiroCursoVOs =  new ArrayList<CondicaoPagamentoPlanoFinanceiroCursoVO>(0);
//		if (!configuracaoFinanceiroVO.getRealizarMatriculaComFinanceiroManualAtivo()) {
//			getFacadeFactory().getMatriculaFacade().inicializarPlanoFinanceiroMatriculaPeriodo(transferenciaEntradaVO.getMatricula(), novaMatriculaPeriodo, null);
//			condicaoPagamentoPlanoFinanceiroCursoVOs = getFacadeFactory().getPlanoFinanceiroCursoFacade().inicializarListaSelectItemPlanoFinanceiroCursoParaTurma(true, 0, novaMatriculaPeriodo, transferenciaEntradaVO.getMatricula(), usuarioVO);
//		}			
//		if(condicaoPagamentoPlanoFinanceiroCursoVOs.isEmpty()){
//			throw new Exception("Não foi encontrada nenhuma CONDIÇÃO FINANCEIRA habilitada para a Renovação  da matrícula . " +transferenciaEntradaVO.getMatricula().getMatricula());
//		}else {
//			novaMatriculaPeriodo.setCondicaoPagamentoPlanoFinanceiroCurso(getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(condicaoPagamentoPlanoFinanceiroCursoVOs.get(0).getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
//			
//		}	
	    List<ProcessoMatriculaVO> processoMatriculaVOs = new ArrayList<ProcessoMatriculaVO>(0);
		if(Uteis.isAtributoPreenchido(transferenciaEntradaVO.getProcessoMatriculaVO())) {
			processoMatriculaVOs.add(transferenciaEntradaVO.getProcessoMatriculaVO());
		}else {
			processoMatriculaVOs = getFacadeFactory().getProcessoMatriculaFacade().consultarProcessosMatriculaOnline(apresentarVisaoAluno , false,  transferenciaEntradaVO.getMatricula().getUnidadeEnsino().getCodigo(), "","", transferenciaEntradaVO.getMatricula().getCurso().getCodigo(), transferenciaEntradaVO.getMatricula().getTurno().getCodigo(), usuarioVO, 1, true,Uteis.NIVELMONTARDADOS_DADOSBASICOS,true, false);
			if(processoMatriculaVOs.isEmpty()){
				throw new Exception("Não foi encontrado nenhum CALENDÁRIO DE MATRÍCULA habilitada para a realização da matrícula."+transferenciaEntradaVO.getMatricula().getMatricula());
			}
		}
		novaMatriculaPeriodo.setProcessoMatriculaVO(getFacadeFactory().getProcessoMatriculaFacade().consultarPorChavePrimaria(processoMatriculaVOs.get(0).getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		novaMatriculaPeriodo.setProcessoMatricula(novaMatriculaPeriodo.getProcessoMatriculaVO().getCodigo());
		novaMatriculaPeriodo.setProcessoMatriculaCalendarioVO(getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorChavePrimaria(novaMatriculaPeriodo.getUnidadeEnsinoCursoVO().getCurso().getCodigo(), novaMatriculaPeriodo.getUnidadeEnsinoCursoVO().getTurno().getCodigo(), processoMatriculaVOs.get(0).getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		novaMatriculaPeriodo.getProcessoMatriculaCalendarioVO().setNivelMontarDados(NivelMontarDados.BASICO);		
		getFacadeFactory().getMatriculaPeriodoFacade().definirSituacaoMatriculaPeriodoComBaseProcesso(transferenciaEntradaVO.getMatricula(), novaMatriculaPeriodo, configuracaoFinanceiroVO, usuarioVO);
		getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosAnoSemestreMatricula(novaMatriculaPeriodo,novaMatriculaPeriodo.getProcessoMatriculaCalendarioVO(), false);	
			
		//getFacadeFactory().getMatriculaPeriodoFacade().inicializarPlanoFinanceiroAlunoMatriculaPeriodo(transferenciaEntradaVO.getMatricula(), novaMatriculaPeriodo, transferenciaEntradaVO.getMatricula().getPlanoFinanceiroAluno(), true, usuarioVO);
		getFacadeFactory().getMatriculaFacade().realizarDefinicaoDocumentacaoMatriculaAluno(transferenciaEntradaVO.getMatricula(), novaMatriculaPeriodo, configuracaoGeralSistemaVO, false, usuarioVO);
		getFacadeFactory().getMatriculaFacade().realizarDefinicaoDisciplinaMatriculaPeriodo(transferenciaEntradaVO.getMatricula(), novaMatriculaPeriodo, configuracaoGeralSistemaVO, true, false, false, false, usuarioVO, null);
		getFacadeFactory().getMatriculaFacade().persistir(transferenciaEntradaVO.getMatricula(), novaMatriculaPeriodo, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, usuarioVO);
		
		
	}
	
	@Override	
	public void realizarDefinicaoGradeDisciplinaHistoricoAproveitarEspecificoTransferenciaInterna(	TransferenciaEntradaVO transferenciaEntradaVO, UsuarioVO usuario, MatriculaVO novaMatricula,
			MatriculaPeriodoVO novaMatriculaPeriodo, List<HistoricoVO> historicos) throws Exception {
        if(novaMatricula.getCurso().getConfiguracaoAcademico().isHabilitarInclusaoDisciplinaDependenciaPrimeiroDepoisRegulares()) {        	
    		novaMatricula.getTransferenciaEntrada().setCarregarDisciplinasAproveitadas(true);
    		novaMatricula.getTransferenciaEntrada().getSituacao().equals("AV");    		    		
    		MatriculaComHistoricoAlunoVO matriculaComHistoricoAlunoVO = getFacadeFactory().getHistoricoFacade().carregarDadosMatriculaComHistoricoAlunoVO(novaMatricula, novaMatricula.getGradeCurricularAtual().getCodigo(), false, novaMatricula.getCurso().getConfiguracaoAcademico(), usuario);
    		novaMatricula.setMatriculaComHistoricoAlunoVO(matriculaComHistoricoAlunoVO);
    		novaMatriculaPeriodo.setGradeCurricular(matriculaComHistoricoAlunoVO.getGradeCurricularComHistoricoAlunoVO().getGradeCurricularVO());
    		PeriodoLetivoVO periodoAnterior = null;
			if(novaMatricula.getCurso().getConfiguracaoAcademico().getHabilitarControleInclusaoObrigatoriaDisciplinaDependencia() && novaMatricula.getCurso().getConfiguracaoAcademico().isHabilitarDistribuicaoDisciplinaDependenciaAutomatica()) {
				periodoAnterior = novaMatriculaPeriodo.getPeriodoLetivo();
			}else {
				periodoAnterior = getFacadeFactory().getMatriculaPeriodoFacade().executarObterPeriodoLetivoAnteriorAoPeriodoLetivoMatriculaPeriodo(novaMatricula, novaMatriculaPeriodo);
			}
			Boolean liberadoInclusaoTurmaOutroUnidadeEnsino = verificarPermissaoFuncionalidadeUsuario("IncluirDisciplinaApenasTurmaProprioUnidadeEnsino", usuario); 
			Boolean liberadoInclusaoTurmaOutroCurso = verificarPermissaoFuncionalidadeUsuario("IncluirDisciplinaApenasTurmaProprioCurso", usuario);
			Boolean liberadoInclusaoTurmaOutroMatrizCurricular = verificarPermissaoFuncionalidadeUsuario("IncluirDisciplinaApenasTurmaProprioMatrizCurricular", usuario);			
			getFacadeFactory().getMatriculaPeriodoFacade()
					.inicializarDadosDefinirDisciplinasDependenciaFuturasMatriculaPeriodo(novaMatricula,
							novaMatriculaPeriodo, new ArrayList<GradeDisciplinaVO>(),
							periodoAnterior, 0,
							liberadoInclusaoTurmaOutroUnidadeEnsino, liberadoInclusaoTurmaOutroCurso,
							liberadoInclusaoTurmaOutroMatrizCurricular,
							false, false, null,
							usuario);
			historicos.addAll(getFacadeFactory().getTransferenciaEntradaFacade().criarHistoricoTransferenciaEntrada(transferenciaEntradaVO, usuario, false));
			if(!novaMatricula.getMatriculaOnlineExterna()) {
				getFacadeFactory().getMatriculaPeriodoFacade().atualizarNrDisciplinasIncluidasExcluidas(novaMatriculaPeriodo, novaMatricula, usuario);
			}
        }else {
			List<HistoricoVO> listaHistoricoAlunoMatrizCurricular = getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatriculaSituacaoHistorico(transferenciaEntradaVO.getMatricula().getAluno().getCodigo(), transferenciaEntradaVO.getMatricula().getMatricula(), transferenciaEntradaVO.getMatricula().getGradeCurricularAtual().getCodigo(), new String[] { "'AP'", "'AA'" }, OrdemHistoricoDisciplina.MATRICULA_COM_HISTORICO_ALUNO.getValor(), false, false, NivelMontarDados.TODOS, usuario, null, false);
			List<GradeDisciplinaVO> listaGradeDisciplinaRelVOs = getFacadeFactory().getGradeDisciplinaFacade().consultarPorGradeCurricular(novaMatricula.getGradeCurricularAtual().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			Iterator<TurmaDisciplinaVO> iTurmaDisciplinaVO = novaMatriculaPeriodo.getTurma().getTurmaDisciplinaVOs().iterator();
			while (iTurmaDisciplinaVO.hasNext()) {
				TurmaDisciplinaVO objTurmaDisciplina = (TurmaDisciplinaVO) iTurmaDisciplinaVO.next();
				Boolean result = listaHistoricoAlunoMatrizCurricular.stream().anyMatch(p -> p.getDisciplina().getCodigo().equals(objTurmaDisciplina.getDisciplina().getCodigo()));
				if (result) {
					iTurmaDisciplinaVO.remove();
				}
			}
			getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosDefinirDisciplinasMatriculaPeriodo(novaMatricula, novaMatriculaPeriodo, usuario, null, false, false);
			for (GradeDisciplinaVO obj : listaGradeDisciplinaRelVOs) {
				Optional<HistoricoVO> result = listaHistoricoAlunoMatrizCurricular.stream().filter(p -> p.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())).findFirst();
				if (result.isPresent()) {
					HistoricoVO his = (HistoricoVO) result.get();
					historicos.add(his);
				}
			}
        }
	}
	
	@Override
	public List<MatriculaVO> consultarMatriculasAlunoDiferenteMatriculaAtualPorCodigoPessoaDadosMinimos(String matricula ,Integer pessoa,  boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT matricula.matricula , matricula.gradecurricularAtual  FROM Matricula where  matricula.aluno  = ?    order by matricula ";
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr ,pessoa.intValue());
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		while (tabelaResultado.next()) {
			MatriculaVO matriculaVO = new MatriculaVO();	
			matriculaVO.setMatricula(tabelaResultado.getString("matricula"));			
			matriculaVO.getGradeCurricularAtual().setCodigo(tabelaResultado.getInt("gradecurricularatual"));
			matriculaVOs.add(matriculaVO);
		}
		return matriculaVOs ;
	}
	
	@Override
	public List<HistoricoVO>  realizarCriacaoHistoricoApartirDisciplinasAprovadasMesmaGradeCurricularMatriculaOnline(String matricula ,Integer aluno , Integer gradeCurricularAtual  ,TurmaVO turmaVO , ConfiguracaoFinanceiroVO configFinanceiroVO , NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<HistoricoVO> historicosAproveitar = new ArrayList<HistoricoVO>(0);
		List<MatriculaVO> matriculaVOs =  getFacadeFactory().getMatriculaFacade().consultarMatriculasAlunoDiferenteMatriculaAtualPorCodigoPessoaDadosMinimos(matricula,aluno,  false, Uteis.NIVELMONTARDADOS_COMBOBOX, configFinanceiroVO, usuario);
		if(Uteis.isAtributoPreenchido(matriculaVOs)) {			
			List<DisciplinaVO> listaDisciplinasGrade = getFacadeFactory().getDisciplinaFacade().consultaRapidaDisciplinaPorGradeCurricular(gradeCurricularAtual, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			List<HistoricoVO> mapaHistoricos = new ArrayList<HistoricoVO>(0);
			for(MatriculaVO  objMatricula : matriculaVOs) {		
  				mapaHistoricos.addAll(getFacadeFactory().getHistoricoFacade().consultaRapidaPorDisciplinaGradeCurricular(objMatricula.getMatricula(),objMatricula.getGradeCurricularAtual().getCodigo(),new ArrayList<Integer>(0),SituacaoHistorico.getSituacoesDeAprovacao(), true, false, false, 0, 0, 0,nivelMontarDados , usuario)); 							
			}			
			if(Uteis.isAtributoPreenchido(mapaHistoricos)) {
				Iterator<HistoricoVO> iHistoricoRemoverDuplicada = mapaHistoricos.iterator();	
				while(iHistoricoRemoverDuplicada.hasNext()) {
					HistoricoVO histDuplicados = (HistoricoVO) iHistoricoRemoverDuplicada.next();
					if(mapaHistoricos.stream().anyMatch(hist -> !hist.getCodigo().equals(histDuplicados.getCodigo()) && hist.getDisciplina().getCodigo().equals(histDuplicados.getDisciplina().getCodigo()))) {
						iHistoricoRemoverDuplicada.remove();
					}				
				}	
				
				Iterator<HistoricoVO> iHistoricoParaGrade = mapaHistoricos.iterator();				
				// separar os historicos da grade q estao aprovados 
				// pois vao ser gravados direto . pegar a lista e adicionar na lista q tera os historicos validos
				while(iHistoricoParaGrade.hasNext()) {
					HistoricoVO hist = (HistoricoVO) iHistoricoParaGrade.next();
					if(realizarRemoverDisciplinasAprovadasDaGradeDisciplinas(listaDisciplinasGrade, hist.getDisciplina(),turmaVO)) {
						historicosAproveitar.add(hist);									
						iHistoricoParaGrade.remove();
					}					 
					if(listaDisciplinasGrade.isEmpty()) {
						break;
					}
				}				
			}
			 	
			 
			 if(Uteis.isAtributoPreenchido(mapaHistoricos) && Uteis.isAtributoPreenchido(listaDisciplinasGrade)) {	
				 for(DisciplinaVO obj : listaDisciplinasGrade) {
					 List<MapaEquivalenciaDisciplinaVO> mapaEquivalenciaDisciplinaVOs = getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().consultarPorMapaEquivalenciaMatrizCurricularDisciplinaMatrizConsiderandoSituacao(gradeCurricularAtual, obj.getCodigo(), NivelMontarDados.TODOS, true, false );
					 for(MapaEquivalenciaDisciplinaVO  mapa : mapaEquivalenciaDisciplinaVOs) {						 
						 if(Uteis.isAtributoPreenchido(mapaHistoricos) &&
								 mapa.getMapaEquivalenciaDisciplinaCursadaVOs().size() == 1 &&
								 mapa.getMapaEquivalenciaCodigoDisciplinaMatrizCurricularVOs().size() ==1 ) {
							 Iterator<HistoricoVO> iHistoricoParaGrade = mapaHistoricos.iterator();	
								while(iHistoricoParaGrade.hasNext()) {
									HistoricoVO hist = (HistoricoVO) iHistoricoParaGrade.next();									
									if(mapa.getMapaEquivalenciaDisciplinaCursadaVOs().get(0).getDisciplinaVO().getCodigo().equals(hist.getDisciplina().getCodigo())) {
										realizarRemoverTurmaDisciplinasParDisciplinasAproveitadas(turmaVO, obj);
										hist.setHistoricoEquivalente(Boolean.TRUE);
										hist.setMapaEquivalenciaDisciplina(mapa);
										hist.setMapaEquivalenciaDisciplinaCursada(mapa.getMapaEquivalenciaDisciplinaCursadaVOs().get(0));
										hist.setHistoricoAproveitamentoEquivalenciaMatriculaProcessoSeletivo(Boolean.TRUE);
										historicosAproveitar.add(hist);								   
										iHistoricoParaGrade.remove();
									}								
								}					 
						 }				 
					 }					 
				 } 
			 }	
		}
		return historicosAproveitar;				 
	}
	
	public Boolean realizarRemoverDisciplinasAprovadasDaGradeDisciplinas(List<DisciplinaVO> listaDisciplinasGrade, DisciplinaVO disciplina ,TurmaVO turmaVO ) {
		Iterator<DisciplinaVO> iDisciplina = listaDisciplinasGrade.iterator();			
		Boolean removeu = Boolean.FALSE;
		while(iDisciplina.hasNext()) {
			DisciplinaVO disciplinaAdicionadaRemoverDuplicidade  = (DisciplinaVO) iDisciplina.next();
			if(disciplina.getCodigo().equals(disciplinaAdicionadaRemoverDuplicidade.getCodigo())) { 
				iDisciplina.remove();
				realizarRemoverTurmaDisciplinasParDisciplinasAproveitadas(turmaVO, disciplina);
				removeu = Boolean.TRUE;
			}								
		}
		return removeu ;
	}
	
	
	public void  realizarRemoverTurmaDisciplinasParDisciplinasAproveitadas(TurmaVO turmaVO ,  DisciplinaVO disciplina) {
		Iterator<TurmaDisciplinaVO> iTurmaDisciplinaVO = turmaVO.getTurmaDisciplinaVOs().iterator();		 
		while(iTurmaDisciplinaVO.hasNext()) { 			
			 TurmaDisciplinaVO objTurmaDisciplina = (TurmaDisciplinaVO) iTurmaDisciplinaVO.next();					    	 
			 if(disciplina.getCodigo().equals(objTurmaDisciplina.getDisciplina().getCodigo())) {					    		 
				 iTurmaDisciplinaVO.remove();			 
			 }				    	
       }
		 
	}
	
	@Override
	public boolean realizarVerificacaoAlunoPossuiMatriculaAtivaOUPreMatriculaPorInscricao(Integer inscricao, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Matricula.matricula FROM Matricula WHERE matricula.situacao in ('AT','PR') and matricula.inscricao = ? ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, inscricao.intValue());
		if (tabelaResultado.next()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
		
	}
	
	public void realizarMontagemDadosProcSeletivoMatricula(MatriculaVO obj, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception {
		if (obj != null && (obj.getInscricao() != null && Uteis.isAtributoPreenchido(obj.getInscricao()))) {
			getFacadeFactory().getInscricaoFacade().carregarDados(obj.getInscricao(), usuario);
			if (obj.getInscricao() != null) {
				if(obj.getInscricao().getProcSeletivo() == null || (!Uteis.isAtributoPreenchido(obj.getInscricao().getProcSeletivo()))) {
					obj.setInscricao(getFacadeFactory().getInscricaoFacade().consultaRapidaInscricaoUnicaPorCodigoInscricaoCpf(obj.getInscricao().getCodigo(), null, null, false, usuario));
				}
				if ((obj.getDataProcessoSeletivo() == null || (!Uteis.isAtributoPreenchido(obj.getDataProcessoSeletivo()))) && obj.getInscricao().getProcSeletivo() != null && (obj.getInscricao().getItemProcessoSeletivoDataProva() != null && Uteis.isAtributoPreenchido(obj.getInscricao().getItemProcessoSeletivoDataProva().getCodigo()))) {
					obj.getInscricao().setItemProcessoSeletivoDataProva(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorChavePrimaria(obj.getInscricao().getItemProcessoSeletivoDataProva().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
					obj.setDataProcessoSeletivo(obj.getInscricao().getItemProcessoSeletivoDataProva().getDataProva());
				}
				if (obj.getTotalPontoProcSeletivo() == null || (!Uteis.isAtributoPreenchido(obj.getTotalPontoProcSeletivo()))) {
					obj.setTotalPontoProcSeletivo(getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPontosResultadoProcessoSeletivoPorInscricao(obj.getInscricao().getCodigo()).doubleValue());
				}
				if (obj.getDisciplinasProcSeletivo() == null || (!Uteis.isAtributoPreenchido(obj.getDisciplinasProcSeletivo().trim()))) {
					obj.setDisciplinasProcSeletivo(getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarResultadoProcessoSeletivoDescritivoPorInscricao(obj.getInscricao().getCodigo()));
				}
				if (obj.getClassificacaoIngresso() == null || (!Uteis.isAtributoPreenchido(obj.getClassificacaoIngresso()))){
					Integer classificacao = 0;
					if (Uteis.isAtributoPreenchido(obj.getInscricao().getClassificacao())) {
						classificacao = obj.getInscricao().getClassificacao();
					} else {
						classificacao = getFacadeFactory().getEstatisticaProcessoSeletivoRelFacade().verificarClassificacaoCandidado(obj.getInscricao());
					}
					obj.setClassificacaoIngresso(classificacao);
				}
				if (obj.getCodigoFinanceiroMatricula() == null || (!Uteis.isAtributoPreenchido(obj.getCodigoFinanceiroMatricula())) && (obj.getInscricao().getCodigoFinanceiroMatricula() != null && Uteis.isAtributoPreenchido(obj.getInscricao().getCodigoFinanceiroMatricula()))) {
					obj.setCodigoFinanceiroMatricula(obj.getInscricao().getCodigoFinanceiroMatricula());
				}
				if (obj.getUnidadeEnsino() == null || (!Uteis.isAtributoPreenchido(obj.getUnidadeEnsino())) && (obj.getInscricao().getUnidadeEnsino() != null && Uteis.isAtributoPreenchido(obj.getInscricao().getUnidadeEnsino()))) {
					obj.setUnidadeEnsino(obj.getInscricao().getUnidadeEnsino());
				}
				if (obj.getAluno() == null || (!Uteis.isAtributoPreenchido(obj.getAluno()))) {
					obj.setAluno(obj.getInscricao().getCandidato());
				}
				if ((!Uteis.isAtributoPreenchido(obj)) && (!Uteis.isAtributoPreenchido(obj.getMatricula()))) {
					if (obj.getInscricao().getResultadoProcessoSeletivoVO().getOpcaoCursoAprovadoProcessoSeletivo() == 1) {
						obj.getInscricao().setCursoOpcao1(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(obj.getInscricao().getCursoOpcao1().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
						if (obj.getCurso().getCodigo().equals(0)) {
							obj.setCurso(obj.getInscricao().getCursoOpcao1().getCurso());
							obj.setTurno(obj.getInscricao().getCursoOpcao1().getTurno());
							matriculaPeriodoVO.setUnidadeEnsinoCurso(obj.getInscricao().getCursoOpcao1().getCodigo());
						}
					} else {
						if (obj.getInscricao().getResultadoProcessoSeletivoVO().getOpcaoCursoAprovadoProcessoSeletivo() == 2) {
							obj.getInscricao().setCursoOpcao2(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(obj.getInscricao().getCursoOpcao2().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
							if (obj.getCurso().getCodigo().equals(0)) {
								obj.setCurso(obj.getInscricao().getCursoOpcao2().getCurso());
								obj.setTurno(obj.getInscricao().getCursoOpcao2().getTurno());
								matriculaPeriodoVO.setUnidadeEnsinoCurso(obj.getInscricao().getCursoOpcao2().getCodigo());
							}
						} else {
							if (obj.getInscricao().getResultadoProcessoSeletivoVO().getOpcaoCursoAprovadoProcessoSeletivo() == 3) {
								obj.getInscricao().setCursoOpcao3(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(obj.getInscricao().getCursoOpcao3().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
								if (obj.getCurso().getCodigo().equals(0)) {
									obj.setCurso(obj.getInscricao().getCursoOpcao3().getCurso());
									obj.setTurno(obj.getInscricao().getCursoOpcao3().getTurno());
									matriculaPeriodoVO.setUnidadeEnsinoCurso(obj.getInscricao().getCursoOpcao3().getCodigo());
								}
							}
						}
					}
				}
			}
		}
	}
	
	@Override
	public String consultarCPFalunoPreencherDocumentacaoMatriculaParaMatriculaOnline(String matricula) {
		String sqlStr = "SELECT pessoa.cpf as cpfaluno  FROM Matricula  inner join pessoa on pessoa.codigo = matricula.aluno   WHERE matricula.matricula = '" + matricula + "' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getString("cpfaluno");

		}
		return "";
	}
	

	@Override
	public StringBuilder getWhereMatriculaLiberadasVisaoAlunoPais(boolean validarSituacaoMatricula, boolean validarSuspensao, boolean validarCurso) {
		StringBuilder sqlStr = new StringBuilder(""); 
		if(validarSituacaoMatricula) {
			sqlStr.append(" AND ((matricula.situacao in ('CA', 'TR', 'AC', 'TS', 'TI', 'JU') and configuracaogeralsistema.permitirAcessoAlunoEvasao = true)");
			sqlStr.append(" or (matricula.situacao = 'FI' and (configuracaogeralsistema.permitirAcessoAlunoFormado = true or configuracaogeralsistema.permitirAcessoAlunoEvasao = true))");
			sqlStr.append(" or (matricula.situacao = 'FI' and ((curso.periodicidade =  'AN' and matriculaperiodo.ano = extract(year from current_date)::varchar) or (curso.periodicidade =  'SE' and matriculaperiodo.ano = extract(year from current_date)::varchar and matriculaperiodo.semestre = (case when extract(month from current_date) > 7 then '2' else '1' end)  ) or (curso.periodicidade =  'IN')) and configuracaogeralsistema.permitirAcessoAlunoFormado = false and configuracaogeralsistema.permitirAcessoAlunoEvasao = false)");
			sqlStr.append(" or (matricula.situacao = 'FO' and configuracaogeralsistema.permitirAcessoAlunoFormado = true)");
			sqlStr.append(" or (matricula.situacao = 'FO' and configuracaogeralsistema.permitirAcessoAlunoFormado = false and configuracaogeralsistema.qtddiasacessoalunoformado > 0 and matriculaperiodo.datafechamentomatriculaperiodo is not null and (matriculaperiodo.datafechamentomatriculaperiodo + (configuracaogeralsistema.qtddiasacessoalunoformado||' days')::INTERVAL)::DATE >= current_date )");
			sqlStr.append(" or (matricula.situacao in ('AT', 'PR') and  matriculaperiodo.situacaomatriculaperiodo = 'PR' and configuracaogeralsistema.permitirAcessoAlunoPreMatricula = true)");
			sqlStr.append(" or (matricula.situacao in ('AT', 'PR') and matriculaperiodo.situacaomatriculaperiodo = 'PR' and configuracaogeralsistema.permitirAcessoAlunoPreMatricula = false and exists(select mp.codigo from matriculaperiodo as mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo = 'AT'))");
			sqlStr.append(" or (matricula.situacao in ('AT','FI') and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO', 'FI')) ");
			sqlStr.append(" or (matricula.situacao in ('AT','FI') and matriculaperiodo.situacaomatriculaperiodo in ('CA', 'TR', 'AC', 'TS','TI') and configuracaogeralsistema.permitirAcessoAlunoEvasao = true) ");
			sqlStr.append(" )");
		}
		if(validarSuspensao) {
			sqlStr.append(" AND (matricula.matriculaSuspensa = false or matricula.matriculaSuspensa is null ");
			sqlStr.append(" or (case when matricula.qtdDiasAdiarBloqueio is not null and matricula.qtdDiasAdiarBloqueio > 0 then ");
			sqlStr.append(" (matricula.matriculasuspensa and (case when dataAdiamentoSuspensaoMatricula::DATE is not null ");
			sqlStr.append(" then dataAdiamentoSuspensaoMatricula::DATE else matricula.dataBaseSuspensao end + matricula.qtdDiasAdiarBloqueio) >= current_date) ");
			sqlStr.append(" else false end)) ");
		}		
		return sqlStr;
		
	}
	
	@Override
	public Boolean verificarPossuiMatriculaAtivaPessoa(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT matricula FROM Matricula WHERE upper (situacao)  = 'AT' and aluno = " + pessoa.intValue() + " ORDER BY situacao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}

	public void validarExtensaoArquivo(String name, DocumetacaoMatriculaVO documetacaoMatriculaVO, Boolean arquivoGED) throws Exception {
//		Responsável por verificar a extensão dos documentos na tela Entrega de Documentos Adm e Visão do aluno, Renovação
//		Caso seja um arquivo GED, só poderá ser upado PDF. Caso não seja GED, vai verificar se no Tipo Documento as extensões estão informadas, e irá considerá-las para verificação.
//		Caso não esteja preenchido no tipo documento, irá verificar com os tipos de extensão fixos no método.		
		Boolean invalido = true;
		if (!arquivoGED) {
			if (Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getTipoDeDocumentoVO().getExtensaoArquivo())) {
				String[] extensoes = documetacaoMatriculaVO.getTipoDeDocumentoVO().getExtensaoArquivo().split(",");
					for (String extensao : extensoes) {
						if (name.toLowerCase().trim().endsWith(extensao.toLowerCase().trim())) {
							invalido = false;
						}
					}
			} else if (name.endsWith(".jpeg") || name.endsWith(".JPEG") || name.endsWith(".jpg")
		        || name.endsWith(".JPG") || name.endsWith(".png") || name.endsWith(".PNG")
		        || name.endsWith(".gif") || name.endsWith(".GIF") || name.endsWith(".bmp")
		        || name.endsWith(".BMP") || name.endsWith(".ico") || name.endsWith(".ICO")
		        || name.endsWith(".pdf") || name.endsWith(".PDF")) {
				invalido = false;
			}
		} else if (name.endsWith(".pdf") || name.endsWith(".PDF")) {
			invalido = false;
		}
		if (invalido) {
			if(!arquivoGED && Uteis.isAtributoPreenchido(documetacaoMatriculaVO.getTipoDeDocumentoVO().getExtensaoArquivo())) {
				throw new Exception("Arquivo não Enviado. Formato inválido do arquivo, é aceito apenas o(s) formato(s) "+documetacaoMatriculaVO.getTipoDeDocumentoVO().getExtensaoArquivo()+".");
			}else if(!arquivoGED) {
				throw new Exception("Arquivo não Enviado. Formato inválido do arquivo, é aceito apenas os formatos .jpeg, .jpg, .gif, .bmp, .ico, .pdf.");
			}else{
				throw new Exception("Arquivo não Enviado. Formato inválido do arquivo, é aceito apena o formato .pdf.");
			}
	}
	
	}
	
	@Override
	public MatriculaIntegralizacaoCurricularVO consultarPercentuaisIntegralizacaoCurricularMatricula(String matricula) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * from matriculaIntegralizacaoCurricular('").append(matricula).append("')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if(!tabelaResultado.next()) {
			return null;
		}
		
		MatriculaIntegralizacaoCurricularVO obj = new MatriculaIntegralizacaoCurricularVO();
		
		obj.setMatricula(tabelaResultado.getString("matricula"));
		obj.setCargaHorariaTotal(tabelaResultado.getInt("cargaHorariaTotal"));
		obj.setCargaHorariaDisciplinaObrigatorioExigida(tabelaResultado.getInt("cargaHorariaDisciplinaObrigatorioExigida"));
		obj.setCargaHorariaDisciplinaObrigatorioCumprida(tabelaResultado.getInt("cargaHorariaDisciplinaObrigatorioCumprida"));
		obj.setCargaHorariaDisciplinaOptativaExigida(tabelaResultado.getInt("cargaHorariaDisciplinaOptativaExigida"));
		obj.setQuantidadeMinimaDisciplinaOptativaExigida(tabelaResultado.getInt("quantidadeMinimaDisciplinaOptativaExigida"));
		obj.setCargaHorariaDisciplinaOptativaCumprida(tabelaResultado.getInt("cargaHorariaDisciplinaOptativaCumprida"));
		obj.setQuantidadeDisciplinaOptativaCumprida(tabelaResultado.getInt("quantidadeDisciplinaOptativaCumprida"));
		obj.setCargaHorariaEstagioExigido(tabelaResultado.getInt("cargaHorariaEstagioExigido"));
		obj.setCargaHorariaEstagioCumprido(tabelaResultado.getInt("cargaHorariaEstagioCumprido"));
		obj.setCargaHorariaAtividadeComplementarExigido(tabelaResultado.getInt("cargaHorariaAtividadeComplementarExigido"));
		obj.setCargaHorariaAtividadeComplementarCumprido(tabelaResultado.getInt("cargaHorariaAtividadeComplementarCumprido"));
		obj.setCargaHorariaCumprido(tabelaResultado.getInt("cargaHorariaCumprido"));
		obj.setCargaHorariaPendente(tabelaResultado.getInt("cargaHorariaPendente"));
		obj.setPercentualIntegralizado(tabelaResultado.getDouble("percentualIntegralizado"));
		obj.setPercentualNaoIntegralizado(tabelaResultado.getDouble("percentualNaoIntegralizado"));
		obj.setPercentualPermitirIniciarEstagio(tabelaResultado.getDouble("percentualPermitirIniciarEstagio"));
		obj.setCargaHorariaExigidaLiberarEstagio(tabelaResultado.getInt("cargaHorariaExigidaLiberarEstagio"));
		obj.setCargaHorariaCumpridaLiberarEstagio(tabelaResultado.getInt("cargaHorariaCumpridaLiberarEstagio"));
		obj.setPercentualCumpridoLiberarEstagio(tabelaResultado.getDouble("percentualCumpridoLiberarEstagio"));
		obj.setPercentualPendenteLiberarEstagio(tabelaResultado.getDouble("percentualPendenteLiberarEstagio"));
		obj.setPercentualPermitirIniciarTcc(tabelaResultado.getDouble("percentualPermitirIniciarTcc"));
		obj.setCargaHorariaExigidaLiberarTcc(tabelaResultado.getInt("cargaHorariaExigidaLiberarTcc"));
		obj.setCargaHorariaCumpridaLiberarTcc(tabelaResultado.getInt("cargaHorariaCumpridaLiberarTcc"));
		obj.setPercentualCumpridoLiberarTcc(tabelaResultado.getDouble("percentualCumpridoLiberarTcc"));
		obj.setPercentualPendenteLiberarTcc(tabelaResultado.getDouble("percentualPendenteLiberarTcc"));
	
		return obj;
	}
	
	
	
	@Override
	public Boolean verificarExisteMultiplasMatriculasAluno(MatriculaVO matriculaVO, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" select count(*) as qtde from matricula");	
		sqlStr.append(" where aluno = ").append(matriculaVO.getAluno().getCodigo());	
		sqlStr.append(" and matricula.matricula <> '").append(matriculaVO.getMatricula()).append("'");		
		sqlStr.append(" group by matricula.aluno having count (*) > 0 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			if (tabelaResultado.getInt("qtde") > 0) {
				return true;
			}
		}
		return false;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarInativacaoCredenciasAlunosFormados(MatriculaVO  matriculaVO, UsuarioVO usuario) throws Exception{
		
		
		if(getAplicacaoControle().getConfiguracaoGeralSistemaVO(matriculaVO.getUnidadeEnsino().getCodigo(), usuario).getHabilitarRecursoInativacaoCredenciasAlunosFormados()){
			
			ConfiguracaoLdapVO configuracaoLdapPorCurso =  getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorCurso(matriculaVO.getCurso().getCodigo());
			
			if(!realizarVerificacaoAlunoPossuiOutraMatriculaAtivaPreMatriculaTrancadaUtilizaMesmoDominioLadpVinculadoCursoFormado(configuracaoLdapPorCurso.getCodigo(),matriculaVO.getAluno().getCodigo() , matriculaVO.getMatricula(), usuario)) {
				UsuarioVO usuarioAlteracao = getFacadeFactory().getUsuarioFacade().consultarUsuarioNaoVinculadoFuncionario(matriculaVO.getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario);
				matriculaVO.getAluno().setListaPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarListaPessoaEmailInstitucionalPorPessoa(matriculaVO.getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
				
				for (PessoaEmailInstitucionalVO obj : matriculaVO.getAluno().getListaPessoaEmailInstitucionalVO()) {
					if (obj.getStatusAtivoInativoEnum().isAtivo() && obj.getEmail().toLowerCase().endsWith(configuracaoLdapPorCurso.getDominio().toLowerCase())) {
						obj.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.INATIVO);
						getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarInativacaoPessoaBlack(obj.getEmail(), usuario);
						getFacadeFactory().getPessoaEmailInstitucionalFacade().persistir(matriculaVO.getAluno(), false,	usuario);
						if(!Uteis.isAtributoPreenchido(matriculaVO.getSalaAulaBlackboardVO().getCodigo())) {
							matriculaVO.setSalaAulaBlackboardVO(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardPorMatriculaPorTipoSalaBlackboard(matriculaVO.getMatricula(), TipoSalaAulaBlackboardEnum.ESTAGIO, usuario));
						}
						if(!Uteis.isAtributoPreenchido(matriculaVO.getSalaAulaBlackboardTcc().getCodigo())) {
							matriculaVO.setSalaAulaBlackboardTcc(getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardPorMatriculaPorTipoSalaBlackboard(matriculaVO.getMatricula(), TipoSalaAulaBlackboardEnum.TCC_AMBIENTACAO, usuario));
						}
						if(Uteis.isAtributoPreenchido(matriculaVO.getSalaAulaBlackboardVO().getCodigo())) {
							getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().excluirPessoaSalaBlack(matriculaVO.getSalaAulaBlackboardVO().getCodigo(), matriculaVO.getAluno().getCodigo(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, obj.getEmail(), usuario);
						}
						if(Uteis.isAtributoPreenchido(matriculaVO.getSalaAulaBlackboardTcc().getCodigo())) {
							getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().excluirPessoaSalaBlack(matriculaVO.getSalaAulaBlackboardTcc().getCodigo(), matriculaVO.getAluno().getCodigo(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, obj.getEmail(), usuario);
						}
					}
				}
				
				if(Uteis.isAtributoPreenchido(usuarioAlteracao)){
					JobExecutarSincronismoComLdapAoCancelarTransferirMatricula jobExecutarSincronismoComLdapAoCancelarTransferirMatricula = new JobExecutarSincronismoComLdapAoCancelarTransferirMatricula(usuarioAlteracao, matriculaVO, false,usuario);
					Thread jobSincronizarCancelamento = new Thread(jobExecutarSincronismoComLdapAoCancelarTransferirMatricula);
					jobSincronizarCancelamento.start();
					while(jobSincronizarCancelamento.isAlive()) {
						Thread.sleep(500);
					}
					usuarioAlteracao.setSenha(Uteis.removerMascara(matriculaVO.getAluno().getCPF()));
					usuarioAlteracao.setUsername(Uteis.removerMascara(matriculaVO.getAluno().getCPF()));
					getFacadeFactory().getUsuarioFacade().alterarUserNameSenhaAlteracaoSenhaAluno(usuarioAlteracao, usuarioAlteracao.getUsername(), usuarioAlteracao.getSenha() ,false, usuario);
					getFacadeFactory().getUsuarioFacade().alterarBooleanoResetouSenhaPrimeiroAcesso(false, usuarioAlteracao, false, usuario);											
				}
							
			}			
			
			
			
			
		}		
	}
	
	private boolean realizarVerificacaoAlunoPossuiOutraMatriculaAtivaPreMatriculaTrancadaUtilizaMesmoDominioLadpVinculadoCursoFormado(Integer codigoLdap , Integer codigoAluno , String matriculaAluno , UsuarioVO usuario) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select matriculaFormada.matricula  from matricula matriculaFormada ");	
		sql.append(" inner join curso on curso.codigo = matriculaFormada.curso ");		
		sql.append(" inner join configuracaoldap configldapvalidar on configldapvalidar.codigo = curso.configuracaoldap ");
		sql.append(" WHERE configldapvalidar.codigo  = ").append(codigoLdap);	
		sql.append(" and matriculaFormada.matricula = '").append(matriculaAluno).append("' ");			
		sql.append(" and exists( ");
		sql.append(" select configldap.codigo  from matricula ");
		sql.append(" inner join curso on curso.codigo = matricula.curso ");
		sql.append(" inner join configuracaoldap configldap on configldap.codigo = curso.configuracaoldap ");
		sql.append(" and   matricula.aluno = ").append(codigoAluno);
		sql.append(" and   matricula.matricula <> '").append(matriculaAluno ).append("' ");		
		sql.append(" and   matricula.situacao in ('AT','PR','TR') ");
		sql.append(" and   configldapvalidar.dominio = configldap.dominio) ");		
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next();
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarReativacaoCredenciasAlunosFormadosParaAtivo(MatriculaVO  matriculaVO, UsuarioVO usuario) throws Exception{
		if(getAplicacaoControle().getConfiguracaoGeralSistemaVO(matriculaVO.getUnidadeEnsino().getCodigo(), usuario).getHabilitarRecursoInativacaoCredenciasAlunosFormados()){
			
			ConfiguracaoLdapVO configuracaoLdapPorCurso =  getFacadeFactory().getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdapPorCurso(matriculaVO.getCurso().getCodigo());			
			if(!realizarVerificacaoAlunoPossuiOutraMatriculaAtivaPreMatriculaTrancadaUtilizaMesmoDominioLadpVinculadoCursoFormado(configuracaoLdapPorCurso.getCodigo(),matriculaVO.getAluno().getCodigo() , matriculaVO.getMatricula(), usuario)) {
				UsuarioVO usuarioAlteracao = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(matriculaVO.getAluno().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario);
				if(!usuarioAlteracao.getAtivoLdap() || usuarioAlteracao.getUsername().equals(Uteis.removerMascara(matriculaVO.getAluno().getCPF()))) {
					usuarioAlteracao.setUsername(Uteis.removerMascara(matriculaVO.getAluno().getRegistroAcademico()));
					JobExecutarSincronismoComLdapAoCancelarTransferirMatricula jobExecutarSincronismoComLdapAoCancelarTransferirMatricula = new JobExecutarSincronismoComLdapAoCancelarTransferirMatricula(usuarioAlteracao, matriculaVO, true,usuario);
					Thread jobSincronizarCancelamento = new Thread(jobExecutarSincronismoComLdapAoCancelarTransferirMatricula);
					jobSincronizarCancelamento.start();
					while(jobSincronizarCancelamento.isAlive()) {
						Thread.sleep(500);
					}
					getFacadeFactory().getUsuarioFacade().alterarUserNameSenhaAlteracaoSenhaAluno( usuarioAlteracao, usuarioAlteracao.getUsername(), usuarioAlteracao.getSenha() ,false,usuario);
					getFacadeFactory().getUsuarioFacade().alterarBooleanoResetouSenhaPrimeiroAcesso(false, usuarioAlteracao, false, usuario);					
				}
				matriculaVO.getAluno().setListaPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarListaPessoaEmailInstitucionalPorPessoa(matriculaVO.getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
				
				for (PessoaEmailInstitucionalVO obj : matriculaVO.getAluno().getListaPessoaEmailInstitucionalVO()) {
					if (obj.getStatusAtivoInativoEnum().isInativo() && obj.getEmail().toLowerCase().endsWith(configuracaoLdapPorCurso.getDominio().toLowerCase())) {
						obj.setStatusAtivoInativoEnum(StatusAtivoInativoEnum.ATIVO);
						getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().realizarAtivacaoPessoaBlack(obj.getEmail(), usuario);						
						getFacadeFactory().getPessoaEmailInstitucionalFacade().persistir(matriculaVO.getAluno(), false,	usuario);						
					}
				}							
			}						
		}		
	}
	
	@Override
	public Date consultarDataConclusaoCursoPorMatricula(String matricula) {
		StringBuilder sql = new StringBuilder("SELECT  ");
		sql.append(" case when matricula.dataconclusaocurso is not null then matricula.dataconclusaocurso else ( ");
		sql.append(" select case when gradedisciplina.bimestre = 1 then periodoletivoativounidadeensinocurso.datafimperiodoletivoprimeirobimestre else datafimperiodoletivosegundobimestre end from historico ");
		sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = historico.matriculaPeriodo ");
		sql.append(" inner join gradedisciplina on gradedisciplina.codigo = historico.gradedisciplina ");
		sql.append(" inner join processomatricula on processomatricula.codigo = matriculaPeriodo.processomatricula ");
		sql.append(" inner join processomatriculacalendario on processomatriculacalendario.processomatricula = processomatricula.codigo and processomatriculacalendario.curso = matricula.curso and processomatriculacalendario.turno = matricula.turno ");
		sql.append(" inner join periodoletivoativounidadeensinocurso on periodoletivoativounidadeensinocurso.codigo = processomatriculacalendario.periodoletivoativounidadeensinocurso ");
		sql.append(" where historico.situacao in ('AP','AE') ");
		sql.append(" and historico.matricula = matricula.matricula and historico.matrizcurricular = matricula.gradecurricularatual ");
		sql.append(" order by ");
		sql.append(" case when gradedisciplina.bimestre = 1 then periodoletivoativounidadeensinocurso.datafimperiodoletivoprimeirobimestre else datafimperiodoletivosegundobimestre end desc limit 1) end as \"matricula.dataConclusaoCurso\" ");
		sql.append(" FROM matricula ");
		sql.append(" where matricula.matricula = ? ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula);
		if(rs.next()) {
			return rs.getDate("matricula.dataConclusaoCurso");
		}
		return null;
	}
	
	@Override
	public void inicializarPlanoFinanceiroMatriculaPeriodoSemTurmaInicializada(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO, UsuarioVO usuario) throws Exception {
		if ((matriculaPeriodoVO == null) || (matriculaVO == null) ) {
			throw new Exception("Dados da matrícula não disponíveis para determinar Plano Financeiro!");
		}
		if (Uteis.isAtributoPreenchido(matriculaPeriodoVO)) {
			if (!matriculaPeriodoVO.getAlterarPlanoCondicacaoPagamentoPersistido() && Uteis.isAtributoPreenchido(matriculaPeriodoVO.getPlanoFinanceiroCurso())) {
				// se estamos editando uma matriculaPeriodo, não existe uma
				// razão para inicializar o
				// plano financeiro do aluno, pois já existe um que foi
				// persistido. A não ser que a
				// variavel
				// matriculaPeriodoVO.getAlterarPlanoCondicacaoPagamentoPersistido()
				// este true
				// (o que significaria que o usuário deseja alterar o plano
				// financeiro) temos que sair
				// deste método sem fazer nada para que o plano/conficao
				// financeiro do aluno seja
				// mantido na edição.
				// Assim, vamos somente carregar os dados do plano financeiro
				// atual.
				matriculaPeriodoVO.setPlanoFinanceiroCurso(getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getPlanoFinanceiroCurso().getCodigo(), "", Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
				return;
			}
		}
		
		
			UnidadeEnsinoCursoVO unidadeCursoVO = matriculaPeriodoVO.getUnidadeEnsinoCursoVO();
			if(!Uteis.isAtributoPreenchido(unidadeCursoVO)) {
				unidadeCursoVO = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(matriculaVO.getCurso().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo(), matriculaVO.getTurno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			}
			if (!unidadeCursoVO.getPlanoFinanceiroCurso().getCodigo().equals(0)) {
				PlanoFinanceiroCursoVO planoFinanceiroCursoVO = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(unidadeCursoVO.getPlanoFinanceiroCurso().getCodigo(), "AT", Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
				if (planoFinanceiroCursoVO.getCondicaoPagamentoPlanoFinanceiroCursoVOs().isEmpty()) {
					throw new Exception(UteisJSF.internacionalizar("msg_Matricula_planoFinanceiroCursoNaoEncontrado"));
				}
				planoFinanceiroCursoVO.setNivelMontarDados(NivelMontarDados.TODOS);
				matriculaPeriodoVO.setPlanoFinanceiroCurso(planoFinanceiroCursoVO);
				matriculaPeriodoVO.setContratoFiador(unidadeCursoVO.getPlanoFinanceiroCurso().getTextoPadraoContratoFiador());
				matriculaPeriodoVO.setContratoExtensao(unidadeCursoVO.getPlanoFinanceiroCurso().getTextoPadraoContratoExtensao());
			} else {
				if(!matriculaPeriodoVO.getFinanceiroManual()){
					throw new Exception(UteisJSF.internacionalizar("msg_Matricula_planoFinanceiroCursoNaoEncontradoTurmaCursoUnidadeEnsino"));
				}
			}
		
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarCriacaoNovaMatriculaProcessoSeletivo(MatriculaRSVO matriculaRSVO ,ConfiguracaoGeralSistemaVO configuracaoGeralSistema , UsuarioVO usuarioVO) throws Exception {
		    MatriculaVO matriculaVO = new MatriculaVO();
		    MatriculaPeriodoVO matriculaPeriodoVO = new MatriculaPeriodoVO();	
		    matriculaVO.setMatriculaOnlineProcSeletivo(true);
		    ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), usuarioVO);
		    realizarMontagemDadosMatriculaParaRealizacaoMatriculaOnlineProcessoSeletivo(matriculaRSVO, matriculaVO,matriculaPeriodoVO,configuracaoFinanceiroVO ,usuarioVO);
		    realizarGravarMatriculaOnlineProcessoSeletivo(matriculaRSVO, configuracaoGeralSistema, usuarioVO,matriculaVO, matriculaPeriodoVO, configuracaoFinanceiroVO); 		    
		    realizarMontagemDadosMatriculaOnlineRealizadaProcessoSeletivo(matriculaRSVO, matriculaVO, matriculaPeriodoVO,usuarioVO);	  
	}
	
	/**
	 * @param matriculaRSVO
	 * @param matriculaVO
	 * @param matriculaPeriodoVO
	 */
	public void realizarMontagemDadosMatriculaOnlineRealizadaProcessoSeletivo(MatriculaRSVO matriculaRSVO,
			MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,UsuarioVO usuarioVO)  {
		matriculaRSVO.setMensagem(null);
		matriculaRSVO.setMatricula(matriculaVO.getMatricula());
		matriculaRSVO.setCodigoMatriculaPeriodo(matriculaPeriodoVO.getCodigo());
		matriculaRSVO.getPeriodoLetivoRSVO().setCodigo(matriculaPeriodoVO.getPeridoLetivo().getCodigo());
		matriculaRSVO.getTurmaRSVO().setCodigo(matriculaPeriodoVO.getTurma().getCodigo());			
		matriculaVO.getDocumetacaoMatriculaVOs().stream().forEach((documento) -> { if (!documento.getEntregue()) { documento.setDataNegarDocDep(null); documento.setRespNegarDocDep(null); documento.setJustificativaNegacao(null); documento.setArquivoAprovadoPeloDep(Boolean.FALSE); }});				
		matriculaRSVO.setDocumentacaoMatriculaVOs(matriculaVO.getDocumetacaoMatriculaVOs().stream().filter(p-> p.getTipoDeDocumentoVO().getPermitirPostagemPortalAluno()).collect(Collectors.toList()));
		matriculaRSVO.setAno(matriculaPeriodoVO.getAno());
		matriculaRSVO.setSemestre(matriculaPeriodoVO.getSemestre());
		matriculaRSVO.setMatriculaRealizadaComSucesso(true);
		matriculaRSVO.setTextoFinalizacaoMatriculaOnline(matriculaRSVO.realizarSubstituicaoTagsMatriculaExterna(matriculaVO.getCurso().getTextoConfirmacaoNovaMatricula()));
		matriculaRSVO.setExisteMatriculaPendenteDocumento(matriculaVO.getDocumetacaoMatriculaVOs().stream().anyMatch(d -> !d.getDocumentoEntregue() && d.getTipoDeDocumentoVO().getPermitirPostagemPortalAluno() && (d.getArquivoVO().getCodigo().equals(0) || (!d.getArquivoVO().getCodigo().equals(0) &&	d.getTipoDeDocumentoVO().getDocumentoFrenteVerso() && d.getArquivoVOVerso().getCodigo().equals(0)))));
		Boolean existeDocObrigatorioPendenteUpload = matriculaVO.getDocumetacaoMatriculaVOs().stream().anyMatch(d -> !d.getDocumentoEntregue() && d.getTipoDeDocumentoVO().getPermitirPostagemPortalAluno() && d.getGerarSuspensaoMatricula() && (d.getArquivoVO().getCodigo().equals(0) || (!d.getArquivoVO().getCodigo().equals(0) &&	d.getTipoDeDocumentoVO().getDocumentoFrenteVerso() && d.getArquivoVOVerso().getCodigo().equals(0))));	
		Boolean existeDocumentoObrigatorioSerAprovado =	matriculaVO.getDocumetacaoMatriculaVOs().stream().anyMatch(d -> !d.getDocumentoEntregue() && d.getTipoDeDocumentoVO().getPermitirPostagemPortalAluno() && d.getGerarSuspensaoMatricula());
		if(!existeDocumentoObrigatorioSerAprovado || (!existeDocObrigatorioPendenteUpload &&  matriculaVO.getCurso().getPermitirAssinarContratoPendenciaDocumentacao())) {
			matriculaRSVO.setPermitirAssinarContratoPendenciaDocumentacao( Boolean.TRUE );
		}else {
			matriculaRSVO.setPermitirAssinarContratoPendenciaDocumentacao( Boolean.FALSE );			
		}
		try {
			if((Uteis.isAtributoPreenchido(matriculaRSVO.getDocumentoAssinadoPessoaVO()) &&  matriculaRSVO.getDocumentoAssinadoPessoaVO().getSituacaoDocumentoAssinadoPessoaEnum().isPendente() && Uteis.isAtributoPreenchido(	matriculaRSVO.getDocumentoAssinadoPessoaVO().getUrlAssinatura()))
					|| (!Uteis.isAtributoPreenchido(matriculaRSVO.getDocumentoAssinadoPessoaVO()) 
							&& matriculaVO.getCurso().getAtivarPreMatriculaAposEntregaDocumentosObrigatorios()
							&& Uteis.isAtributoPreenchido(matriculaPeriodoVO.getContratoMatricula().getCodigo())
							&& matriculaPeriodoVO.getContratoMatricula().getAssinarDigitalmenteTextoPadrao()
							&& getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO).getConfiguracaoGedContratoVO().getAssinarDocumento()
							&& getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO).getConfiguracaoGedContratoVO().getProvedorAssinaturaPadraoEnum().isProvedorCertisign())) {
			   matriculaRSVO.setExisteMatriculaAptaPendenteAssinaturaContrato(Boolean.TRUE);
			   matriculaRSVO.setAssinarDigitalmenteContrato(Boolean.TRUE);
			}
			if((Uteis.isAtributoPreenchido(matriculaRSVO.getDocumentoAssinadoPessoaVO())
					&&  matriculaRSVO.getDocumentoAssinadoPessoaVO().getSituacaoDocumentoAssinadoPessoaEnum().isPendente()
					&& Uteis.isAtributoPreenchido(	matriculaRSVO.getDocumentoAssinadoPessoaVO().getUrlAssinatura()))
					|| (!Uteis.isAtributoPreenchido(matriculaRSVO.getDocumentoAssinadoPessoaVO())
					&& matriculaVO.getCurso().getAtivarPreMatriculaAposEntregaDocumentosObrigatorios()
					&& Uteis.isAtributoPreenchido(matriculaPeriodoVO.getContratoMatricula().getCodigo())
					&& matriculaPeriodoVO.getContratoMatricula().getAssinarDigitalmenteTextoPadrao()
					&& getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO).getConfiguracaoGedContratoVO().getAssinarDocumento()
					&& getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo(), usuarioVO).getConfiguracaoGedContratoVO().getProvedorAssinaturaPadraoEnum().isProvedorTechCert())) {
				matriculaRSVO.setExisteMatriculaAptaPendenteAssinaturaContrato(Boolean.TRUE);
				matriculaRSVO.setAssinarDigitalmenteContrato(Boolean.TRUE);
			}
		} catch (Exception e) {
			 matriculaRSVO.setExisteMatriculaAptaPendenteAssinaturaContrato(Boolean.FALSE);
			 matriculaRSVO.setAssinarDigitalmenteContrato(Boolean.FALSE);
			matriculaRSVO.setPermitirAssinarContratoPendenciaDocumentacao( Boolean.FALSE );		
			e.printStackTrace();
		}
	}

	/**
	 * @param matriculaRSVO
	 * @param configuracaoGeralSistema
	 * @param usuarioVO
	 * @param matriculaVO
	 * @param matriculaPeriodoVO
	 * @param configuracaoFinanceiroVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGravarMatriculaOnlineProcessoSeletivo(MatriculaRSVO matriculaRSVO,
			ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO, MatriculaVO matriculaVO,
			MatriculaPeriodoVO matriculaPeriodoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		Uteis.checkState(getFacadeFactory().getMatriculaFacade().realizarVerificacaoAlunoPossuiMatriculaAtivaOUPreMatriculaPorInscricao(matriculaVO.getInscricao().getCodigo(), false ,  usuarioVO), (UteisJSF.internacionalizar("msg_alunoMatriculadoNesteCursoInscricao").replace("{0}", matriculaVO.getAluno().getNome()).replace("{1}", ""+ matriculaVO.getInscricao().getCodigo())));
		List<MatriculaVO> matriculasExistentes  =  getFacadeFactory().getMatriculaFacade().consultarMatriculasAtivaOuTrancadaOutroCursoPermitiOutraMatriculaMesmoAluno(matriculaVO.getCurso().getCodigo(),matriculaVO.getAluno().getCodigo(),matriculaVO.getInscricao().getCodigo(), false ,  usuarioVO);
		if(Uteis.isAtributoPreenchido(matriculasExistentes)) {
			Uteis.checkState(!matriculaRSVO.getConfirmouCancelamentoMatriculaAtivaOuPreOutroCurso(), "Aluno Possui uma/ou mais Matrícula (s) Ativa Em Outro Curso .");
			for(MatriculaVO matCancelar : matriculasExistentes) {
				 getFacadeFactory().getCancelamentoFacade().realizarCancelamentoMatriculaAtivaPorOutroCursoMesmoNivelEducacional(matCancelar ,configuracaoGeralSistema, configuracaoFinanceiroVO ,usuarioVO);
			}
			matriculaVO.setNrMatriculaCancelada(matriculasExistentes.stream().map(MatriculaVO::getMatricula).collect(Collectors.joining(";")));
			matriculaVO.setCriarNovoUsuario(Boolean.FALSE);
			//verificar se aluno possui email institucional ativo com dominio  da configuracao ldap do novo curso matriculado
			PessoaEmailInstitucionalVO emailInstitucional = getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoaPrivilegiandoRegistroAcademicoDominio(matriculaVO.getAluno().getCodigo(),  matriculaVO.getCurso().getConfiguracaoLdapVO().getCodigo(), true , Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			matriculaVO.setConsiderarValidacaoLdapBlackBoard(!Uteis.isAtributoPreenchido(emailInstitucional));
		}
		
		if(!matriculaPeriodoVO.getSituacaoAtiva() && (!matriculaVO.getDocumetacaoMatriculaVOs().stream().anyMatch(d -> !d.getDocumentoEntregue() && d.getTipoDeDocumentoVO().getPermitirPostagemPortalAluno() && d.getGerarSuspensaoMatricula() && (d.getArquivoVO().getCodigo().equals(0) || (!d.getArquivoVO().getCodigo().equals(0) &&	d.getTipoDeDocumentoVO().getDocumentoFrenteVerso() && d.getArquivoVOVerso().getCodigo().equals(0)))))) {
			matriculaPeriodoVO.setSituacaoMatriculaPeriodo(SituacaoMatriculaPeriodoEnum.ATIVA.getValor());
		}
		getFacadeFactory().getMatriculaFacade().incluir(matriculaVO, matriculaPeriodoVO, matriculaPeriodoVO.getProcessoMatriculaCalendarioVO(), matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso(), configuracaoFinanceiroVO, configuracaoGeralSistema, false, false, false,true, usuarioVO);
		if(matriculaRSVO.getInformarDadosBancarios()) {
			getFacadeFactory().getMatriculaFacade().realizarOperacaoInclusaoDadosBancarioAlunoMatriculaExterna(matriculaVO.getAluno(),matriculaRSVO.getBancoAluno(),matriculaRSVO.getAgenciaAluno(), matriculaRSVO.getContaCorrenteAluno());
		}
	}
	

	@Override
	public void realizarMontagenDadosDisciplinasMatriculaOnline(MatriculaRSVO matriculaRSVO,List<MatriculaPeriodoTurmaDisciplinaVO> disciplinasAlunoJaEstaEstudando ,UsuarioVO usuarioVO) throws Exception {		
	    ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), usuarioVO);
		MatriculaVO matriculaVOInicializadaDadosPadrao = new MatriculaVO();
		MatriculaPeriodoVO matriculaPeriodoVOInicializadaDadosPadrao = new MatriculaPeriodoVO();		
		getFacadeFactory().getMatriculaFacade().realizarMontagemDadosMatriculaParaRealizacaoMatriculaOnlineProcessoSeletivo(matriculaRSVO, matriculaVOInicializadaDadosPadrao,matriculaPeriodoVOInicializadaDadosPadrao,configuracaoFinanceiroVO,usuarioVO);
		getFacadeFactory().getMatriculaPeriodoFacade().realizarAdicionarObjetoDisciplinasMatricula(matriculaRSVO, disciplinasAlunoJaEstaEstudando,matriculaPeriodoVOInicializadaDadosPadrao);	
	}

	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarMontagemDadosMatriculaParaRealizacaoMatriculaOnlineProcessoSeletivo( MatriculaRSVO matriculaRSVO, MatriculaVO matriculaVO,MatriculaPeriodoVO matriculaPeriodoVO,	ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,UsuarioVO usuarioVO)throws Exception {	
		
		matriculaVO.setMatricula(matriculaRSVO.getMatricula());
		matriculaVO.setUsuario(usuarioVO);
		matriculaVO.setMatriculaOnlineExterna(true);
		matriculaVO.setMatriculaOnlineProcSeletivo(true);
		matriculaVO.setBolsasAuxilios(matriculaRSVO.getBolsasAuxilios());
		matriculaVO.setEscolaPublica(matriculaRSVO.getEscolaPublica());
		matriculaVO.setAutodeclaracaoPretoPardoIndigena(matriculaRSVO.getAutodeclaracaoPretoPardoIndigena());
		matriculaVO.setNormasMatricula(matriculaRSVO.getNormasMatricula());
		matriculaVO.setFormaIngresso(matriculaRSVO.getFormaIngresso());
		matriculaVO.getInscricao().setCodigo(matriculaRSVO.getCodigoInscricao());
		matriculaVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		matriculaVO.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(matriculaRSVO.getCursoObject().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuarioVO));
		matriculaVO.setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(matriculaRSVO.getPessoaObject().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		matriculaVO.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(matriculaRSVO.getTurnoRSVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		matriculaPeriodoVO.setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(matriculaVO.getCurso().getCodigo(),matriculaVO.getUnidadeEnsino().getCodigo(), matriculaVO.getTurno().getCodigo(), usuarioVO));			
		matriculaPeriodoVO.setGradeCurricular(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(matriculaRSVO.getCursoObject().getGradeDisciplinaObject().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		matriculaVO.setGradeCurricularAtual(matriculaPeriodoVO.getGradeCurricular());
		matriculaPeriodoVO.setPeridoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(matriculaRSVO.getPeriodoLetivoRSVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO));
		matriculaPeriodoVO.setUnidadeEnsinoCurso(matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getCodigo());		
		matriculaPeriodoVO.setProcessoMatriculaVO(getFacadeFactory().getProcessoMatriculaFacade().consultarPorChavePrimaria(matriculaRSVO.getProcessoMatriculaRSVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		matriculaPeriodoVO.setProcessoMatricula(matriculaPeriodoVO.getProcessoMatriculaVO().getCodigo());
		matriculaPeriodoVO.setProcessoMatriculaCalendarioVO(getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getCurso().getCodigo(), matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getTurno().getCodigo(),matriculaPeriodoVO.getProcessoMatriculaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		matriculaPeriodoVO.getProcessoMatriculaCalendarioVO().setNivelMontarDados(NivelMontarDados.BASICO);			
		getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosAnoSemestreMatricula(matriculaPeriodoVO,matriculaPeriodoVO.getProcessoMatriculaCalendarioVO(), false);
		if (!matriculaVO.getTipoMatricula().equalsIgnoreCase("EX") && !matriculaRSVO.getPossuiMatriculaAtivaOuPreOutroCurso() && matriculaRSVO.getValidarAlunoJaMatriculado()) {
			getFacadeFactory().getMatriculaFacade().verificaAlunoJaMatriculado(matriculaVO, false, configuracaoFinanceiroVO, usuarioVO,false,false);
		} 	

		matriculaPeriodoVO.getTurma().setCodigo(matriculaRSVO.getTurmaRSVO().getCodigo());		
		if(!Uteis.isAtributoPreenchido(matriculaPeriodoVO.getTurma().getCodigo())) {			
			TurmaVO turma = getFacadeFactory().getTurmaFacade().realizarCriacaoTurmaPadraoSemDisciplinaParaMatriculaProcessoSeletivoEmPeriodoLetivoEscolhaAutomatica(matriculaVO,matriculaPeriodoVO,false, usuarioVO);
			Uteis.checkState(!Uteis.isAtributoPreenchido(turma.getCodigo()) ,"Não foi encontrada nenhuma TURMA habilitada para a realização da matrícula on-line.");
			matriculaPeriodoVO.setTurma(turma);
		}
		getFacadeFactory().getTurmaFacade().processarDadosPermitinentesTurmaSelecionada(matriculaVO, matriculaPeriodoVO, configuracaoFinanceiroVO, usuarioVO);		
		getFacadeFactory().getTurmaDisciplinaFacade().removerTurmaDisciplinasNaoMarcadasParaEstudar(matriculaRSVO, matriculaPeriodoVO);	
		getFacadeFactory().getMatriculaFacade().adicionarObjMatriculaPeriodoVOs(matriculaPeriodoVO, matriculaVO);
		getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosDefinirDisciplinasMatriculaPeriodo(matriculaVO, matriculaPeriodoVO, usuarioVO, null, false, false);	 
		
	    TextoPadraoVO contratoMatriculaCalouro = getFacadeFactory().getTextoPadraoFacade().consultarTextoPadraoContratoMatriculaPorCurso(matriculaRSVO.getCursoObject().getCodigo(), false ,Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		Uteis.checkState(!Uteis.isAtributoPreenchido(contratoMatriculaCalouro), "Não foi encontrado nenhum CONTRATO MÁTRICULA vinculado ao curso ("+matriculaRSVO.getCursoObject().getNome()+") para a realização da matrícula on-line.");			
		matriculaPeriodoVO.setContratoMatricula(contratoMatriculaCalouro);
		matriculaVO.getPlanoFinanceiroAluno().setResponsavel(usuarioVO);	
	
		getFacadeFactory().getMatriculaFacade().inicializarDocumentacaoMatriculaCurso(matriculaVO, usuarioVO);	
		getFacadeFactory().getMatriculaFacade().realizarValidacaoControleDeVagasMatriculaPorEixoCursoUnidadeEnsino(matriculaVO, usuarioVO);
	}
	
	
	@Override
	   @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultarDadosParaRealizarMatriculaOnline(final Integer codigoInscricao, final Integer cursoAprovado,
			final String navegador, MatriculaRSVO matriculaRSVO, UsuarioVO usuarioVO) throws Exception {
		
		
		
		InscricaoVO inscricaoVO = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(codigoInscricao, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);			
		UnidadeEnsinoCursoVO unidadeEnsinoCursoVO = inscricaoVO.getCursoOpcao1().getCodigo().equals(cursoAprovado) ? inscricaoVO.getCursoOpcao1() : 
			 inscricaoVO.getCursoOpcao2().getCodigo().equals(cursoAprovado) ? inscricaoVO.getCursoOpcao2() : 
		     inscricaoVO.getCursoOpcao3().getCodigo().equals(cursoAprovado) ? inscricaoVO.getCursoOpcao3() : null;
		if(unidadeEnsinoCursoVO == null) {
			throw new Exception("Não foi encontrado o CURSO selecionado para realizar a matrícula.");
		}			
		RenovarMatriculaControle renovarMatriculaControle = new RenovarMatriculaControle(true);
		renovarMatriculaControle.setMatriculaVO(new MatriculaVO());
		renovarMatriculaControle.setMatriculaPeriodoVO(new MatriculaPeriodoVO());
		renovarMatriculaControle.getMatriculaPeriodoVO().setUnidadeEnsinoCurso(unidadeEnsinoCursoVO.getCodigo());
		renovarMatriculaControle.getMatriculaPeriodoVO().setUnidadeEnsinoCursoVO(unidadeEnsinoCursoVO);
		renovarMatriculaControle.getMatriculaPeriodoVO().setAno(inscricaoVO.getProcSeletivo().getAno());
		renovarMatriculaControle.getMatriculaPeriodoVO().setSemestre(inscricaoVO.getProcSeletivo().getSemestre());
		renovarMatriculaControle.setRealizandoNovaMatriculaAluno(true);
		renovarMatriculaControle.getMatriculaVO().setMatriculaOnlineExterna(true);
		renovarMatriculaControle.getMatriculaVO().setAluno(inscricaoVO.getCandidato());
		renovarMatriculaControle.getMatriculaVO().setCurso(unidadeEnsinoCursoVO.getCurso());		
		renovarMatriculaControle.getMatriculaVO().setTurno(unidadeEnsinoCursoVO.getTurno());				
		matriculaRSVO.setPessoaObject(matriculaRSVO.getPessoaObject().converterPessoaVO(renovarMatriculaControle.getMatriculaVO().getAluno()));
		matriculaRSVO.setCursoObject(getFacadeFactory().getCursoFacade().consultarPorChavePrimariaMatriculaExterna(unidadeEnsinoCursoVO.getCurso().getCodigo()));
		matriculaRSVO.setUnidadeEnsinoRSVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoMatriculaOnlineProcessoSeletivo(unidadeEnsinoCursoVO.getUnidadeEnsino(), usuarioVO, renovarMatriculaControle));
		TurnoRSVO turnoRSVO =  new TurnoRSVO();
		turnoRSVO.setCodigo(unidadeEnsinoCursoVO.getTurno().getCodigo());
		turnoRSVO.setNome(unidadeEnsinoCursoVO.getTurno().getNome());
		matriculaRSVO.setTurnoRSVO(turnoRSVO);
		matriculaRSVO.setNavegadorAcesso(navegador);
		matriculaRSVO.setFormaIngresso(inscricaoVO.getFormaIngresso());
		matriculaRSVO.setCodigoInscricao(inscricaoVO.getCodigo());		
		matriculaRSVO.setEscolaPublica(inscricaoVO.getEscolaPublica());
		matriculaRSVO.setAutodeclaracaoPretoPardoIndigena(inscricaoVO.getAutodeclaracaoPretoPardoOuIndigena());
		matriculaRSVO.setBolsasAuxilios(inscricaoVO.getSobreBolsasEAuxilios());
		matriculaRSVO.setInformarDadosBancarios(inscricaoVO.getProcSeletivo().getInformarDadosBancarios());		
		matriculaRSVO.setExisteMatriculaPendenteDocumento(!renovarMatriculaControle.getMatriculaVO().getCurso().getDocumentacaoCursoVOs().isEmpty());					
		matriculaRSVO.setAtivarPreMatriculaAposEntregaDocumentosObrigatorios( renovarMatriculaControle.getMatriculaVO().getCurso().getAtivarPreMatriculaAposEntregaDocumentosObrigatorios());
		matriculaRSVO.setPermitirAssinarContratoPendenciaDocumentacao(renovarMatriculaControle.getMatriculaVO().getCurso().getPermitirAssinarContratoPendenciaDocumentacao());
		matriculaRSVO.realizarValidacaoEcriacaoTextoDeclaracaoPertinentesMatriculaOnline(matriculaRSVO, renovarMatriculaControle.getMatriculaVO().getCurso());
		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), null);
		ConfiguracaoGeralSistemaVO configuracaoGeralSistema = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsadaUnidadEnsino(matriculaRSVO.getUnidadeEnsinoRSVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, new UsuarioVO());						
		getFacadeFactory().getConfiguracaoGeralSistemaFacade().carrregarDadosConfiguracaoPadraoCancelamentoMatriculaPorOutraMatriculaPorCodigoConfiguracao(configuracaoGeralSistema, usuarioVO);
	    getFacadeFactory().getMatriculaFacade().verificarExistenciaPreencherDadosPertinentesMatriculaOnlineRealizadaPendendeAssinaturaContratoEletronicoEntregaDocumento(matriculaRSVO, true ,"MATRICULA"  , usuarioVO, false, configuracaoGeralSistema, configuracaoFinanceiroVO,renovarMatriculaControle);

	}
	
	
	
	@Override
	public boolean verificarMatriculaGeradaDaTransferenciaPossuiDisciplinaNaGradeCurricularParaAproveitamento(String matricula,Integer disciplina,  UsuarioVO usuarioVO) throws Exception {	
		try {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select 	disciplina.codigo  from	matricula mat ");
		sqlStr.append(" inner join transferenciaentrada tse on tse.codigo =  mat.tranferenciaentrada  ");
		sqlStr.append(" inner join matricula matriculaTransferida on matriculaTransferida.matricula = tse.matricula ");
		sqlStr.append(" inner join periodoletivo on periodoletivo.gradecurricular = mat.gradecurricularatual ");
		sqlStr.append(" inner join gradedisciplina gd on gd.periodoletivo = periodoletivo.codigo ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = gd.disciplina ");
		sqlStr.append(" where matriculaTransferida.situacao ='TI' ");
		sqlStr.append(" and   matriculaTransferida.matricula ='").append(matricula).append("' ");
		sqlStr.append(" and   disciplina.codigo =").append(disciplina).append(" ");	
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return dadosSQL.next();
		}catch (Exception e) {
			throw e;
		}
		
		
	}

	@Override
	public MatriculaVO consultarMatriculaTransferidaDestinoPorMatriculaTransferidaOrigem(String matricula ,int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select 	mat.*  from	matricula mat ");
		sqlStr.append(" inner join transferenciaentrada tse on tse.codigo =  mat.tranferenciaentrada  ");
		sqlStr.append(" inner join matricula matriculaTransferida on matriculaTransferida.matricula = tse.matricula ");	
		sqlStr.append(" where matriculaTransferida.situacao ='TI' ");
		sqlStr.append(" and   matriculaTransferida.matricula ='").append(matricula).append("' ");		
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());		
		if (!dadosSQL.next()) {
			new MatriculaVO();
		}
		return montarDados(dadosSQL, nivelMontarDados, configuracaoFinanceiro, usuario);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarEnvioMensagemConfirmacaoMatriculaValidandoRegrasEntregaDocumentacao(MatriculaVO obj, UsuarioVO usuario) throws Exception {		
		
		    PersonalizacaoMensagemAutomaticaVO mensagemTemplate;		
			mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplateCurso(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CONFIRMACAO_NOVA_MATRICULA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, obj.getUnidadeEnsino().getCodigo(), obj.getCurso().getCodigo(), usuario);
			if (mensagemTemplate == null) {
				mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplateCurso(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_CONFIRMACAO_NOVA_MATRICULA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, obj.getUnidadeEnsino().getCodigo(), 0, usuario);
			}
			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica() 
			    && validarSeMatriculaEstaAptaEnviarMensagemConfirmacaoMatricula(obj.getMatricula(),usuario)) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemConfirmacaoMatricula(obj, mensagemTemplate ,usuario);
				getFacadeFactory().getMatriculaFacade().alterarDataNotificacaoConfirmacaoMatricula(obj.getMatricula() , usuario);
			}	
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Boolean validarSeMatriculaEstaAptaEnviarMensagemConfirmacaoMatricula(String matricula, UsuarioVO usuario) throws Exception {	
		
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select 	mat.matricula  from	matricula mat ");
		sqlStr.append(" inner join inscricao on inscricao.codigo = mat.inscricao ");
		sqlStr.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = mat.matricula and  matriculaperiodo.ano = procseletivo.ano and matriculaperiodo.semestre = procseletivo.semestre");
		sqlStr.append(" where  mat.matricula = ?  ");	
		sqlStr.append(" and mat.tranferenciaentrada is null  ");				
		sqlStr.append(" and mat.dataenvionotificacaoconfirmacaomatricula is null  ");				
		sqlStr.append(" and  (SELECT COUNT(codigo) as nrMatriculaPeriodo FROM matriculaPeriodo  WHERE matricula = ? ");				
		sqlStr.append("       and (situacaoMatriculaPeriodo = 'PR' or situacaoMatriculaPeriodo = 'AT' or situacaoMatriculaPeriodo = 'FI') ) <= 1	 ");				
		sqlStr.append(" and  not exists (select dm.codigo from documetacaomatricula dm  ");		
		sqlStr.append("                   inner join tipodocumento on dm.tipodedocumento = tipodocumento.codigo ");
		sqlStr.append("    where  dm.matricula = mat.matricula  and entregue = false  and gerarsuspensaomatricula =  true  and (arquivo is null or arquivo = 0) )");	
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString() ,matricula,matricula);		
		if (dadosSQL.next()) {
			return true;
		}
		return false;
		
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataNotificacaoConfirmacaoMatricula(String matricula , UsuarioVO usuario) throws Exception {
		String sql = "UPDATE Matricula set dataenvionotificacaoconfirmacaomatricula=? WHERE (matricula = ?)"+  adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { new Date(), matricula });		
	}

	@Override
	public void consultarMatriculaParaGeracaoRelatorioLivroRegistro(ControleLivroFolhaReciboVO obj, ControleLivroRegistroDiplomaRelVO controleLivroRegistroDiplomaRelVO, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT matricula.matricula, matricula.dataconclusaocurso, matricula.datacolacaograu, matricula.anoconclusao, matricula.semestreconclusao, ");
		sql.append("aluno.codigo codigoaluno, aluno.nome nomealuno, aluno.cpf cpfaluno, aluno.rg rgaluno, aluno.orgaoemissor orgaoemissoraluno, aluno.estadoemissaorg estadoemissaorgaluno, aluno.datanasc datanascaluno, aluno.telefoneres telefoneresaluno, aluno.telefonecomer telefonecomeraluno, aluno.celular celularaluno, aluno.sexo sexoaluno, ");
		sql.append("naturalidade.codigo naturalidadealuno, naturalidade.nome nomenaturalidadealuno, ");
		sql.append("estadonaturalidade.codigo estadonaturalidadealuno, estadonaturalidade.nome nomeestadonaturalidadealuno, estadonaturalidade.sigla siglaestadonaturalidadealuno, ");
		sql.append("nacionalizadadealuno.codigo codigonacionalizadadealuno, nacionalizadadealuno.nacionalidade nacionalidadenacionalizadadealuno, ");
		sql.append("curso.codigo codigocurso, curso.nome nomecurso, curso.nomedocumentacao nomedocumentacaocurso, curso.titulo titulocurso, curso.habilitacao habilitacaocurso, curso.idcursoinep idcursoinepcurso, curso.titulacaodoformandofeminino titulacaodoformandofemininocurso, curso.titulacaodoformando titulacaodoformandocurso, curso.periodicidade periodicidadecurso, curso.nrregistrointerno nrregistrointernocurso, ");
		sql.append("unidadeensino.codigo codigounidadeensino, unidadeensino.nome nomeunidadeensino, unidadeensino.mantenedora mantenedoraunidadeensino, unidadeensino.nomeexpedicaodiploma nomeexpedicaodiplomaunidadeensino, unidadeensino.abreviatura abreviaturaunidadeensino, unidadeensino.cnpj cnpjunidadeensino, unidadeensino.codigoies codigoiesunidadeensino, ");
		sql.append("cidadeunidade.codigo codigocidadeunidade, cidadeunidade.nome nomecidadeunidade, ");
		sql.append("estadounidade.codigo codigoestadounidade, estadounidade.nome nomeestadounidade, estadounidade.sigla siglaestadounidade, ");
		sql.append("expedicaodiploma.*, reconhecimentocurso.*, renovacaoreconhecimentocurso.*, ");
		sql.append("filiacao.* ");
		sql.append("FROM matricula ");
		sql.append("INNER JOIN pessoa aluno ON aluno.codigo = matricula.aluno ");
		sql.append("INNER JOIN curso ON curso.codigo = matricula.curso ");
		sql.append("INNER JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino ");
		sql.append("LEFT JOIN cidade cidadeunidade ON cidadeunidade.codigo = unidadeensino.cidade ");
		sql.append("LEFT JOIN estado estadounidade ON estadounidade.codigo = cidadeunidade.estado ");
		sql.append("LEFT JOIN cidade naturalidade ON naturalidade.codigo = aluno.naturalidade ");
		sql.append("LEFT JOIN estado estadonaturalidade ON estadonaturalidade.codigo = naturalidade.estado ");
		sql.append("LEFT JOIN paiz nacionalizadadealuno ON nacionalizadadealuno.codigo = aluno.nacionalidade ");
		sql.append("LEFT JOIN LATERAL ( SELECT autorizacaocurso.codigo codigoreconhecimento, autorizacaocurso.nome nomereconhecimento FROM autorizacaocurso WHERE (autorizacaocurso.codigo = matricula.autorizacaocurso) OR (matricula.autorizacaocurso IS NULL AND autorizacaocurso.curso = matricula.curso AND autorizacaocurso.\"data\" < current_date) ORDER BY autorizacaocurso.data ASC LIMIT 1) AS reconhecimentocurso ON TRUE ");
		sql.append("LEFT JOIN LATERAL ( SELECT autorizacaocurso.codigo codigorenovacaoreconhecimentocurso, autorizacaocurso.nome nomerenovacaoreconhecimentocurso FROM autorizacaocurso WHERE (autorizacaocurso.codigo = matricula.renovacaoreconhecimento) OR (matricula.renovacaoreconhecimento IS NULL AND autorizacaocurso.curso = matricula.curso AND autorizacaocurso.data < (CASE WHEN matricula.dataconclusaocurso IS NOT NULL THEN matricula.dataconclusaocurso ELSE matricula.\"data\" END)) ORDER BY autorizacaocurso.data DESC LIMIT 1) AS renovacaoreconhecimentocurso ON TRUE ");
		sql.append("LEFT JOIN LATERAL ( SELECT json_agg(json_build_object('TIPO', f.tipo, 'NOME', trim(p.nome))) jsonFiliacao FROM filiacao f INNER JOIN pessoa p ON p.codigo = f.pais WHERE f.aluno = aluno.codigo AND f.tipo IN ('PA', 'MA')) AS filiacao ON TRUE ");
		sql.append("LEFT JOIN LATERAL ( SELECT expedicaodiploma.codigo codigoexpedicaodiploma, expedicaodiploma.dataexpedicao dataexpedicaoexpedicaodiploma, expedicaodiploma.numeroprocesso numeroprocessoexpedicaodiploma, expedicaodiploma.via viaexpedicaodiploma, expedicaodiploma.numeroprocessoviaanterior numeroprocessoviaanteriorexpedicaodiploma, expedicaodiploma.numeroregistrodiplomaviaanterior numeroregistrodiplomaviaanteriorexpedicaodiploma, expedicaodiploma.dataregistrodiplomaviaanterior dataregistrodiplomaviaanteriorexpedicaodiploma, expedicaodiploma.titulofuncionarioprincipal titulofuncionarioprincipalexpedicaodiploma, expedicaodiploma.titulofuncionariosecundario funcionariosecundarioexpedicaodiploma, expedicaodiploma.matricula matriculaexpedicaodiploma, funcionarioprimario.codigo codigofuncionarioprimario, pessoafuncionarioprimario.codigo codigopessoafuncionarioprimario, pessoafuncionarioprimario.nome nomepessoafuncionarioprimario, funcionariosecundario.codigo codigofuncionariosecundario, pessoafuncionariosecundario.codigo codigopessoafuncionariosecundario, pessoafuncionariosecundario.nome nomepessoafuncionariosecundario, cargoprimario.codigo codigocargoprimario, cargoprimario.nome nomecargoprimario, cargosecundario.codigo codigocargosecundario, cargosecundario.nome nomecargosecundario, reitorregistrodiplomaviaanterior.codigo codigoreitorregistrodiplomaviaanterior, pessoareitorviaanterior.codigo codigopessoareitorviaanterior, pessoareitorviaanterior.nome nomepessoareitorviaanterior, cargoreitoviaanterior.codigo codigocargoreitoviaanterior, cargoreitoviaanterior.nome nomecargoreitoviaanterior, secretariaviaanterior.codigo codigosecretariaviaanterior, pessoasecretariaviaanterior.codigo codigopessoasecretariaviaanterior, pessoasecretariaviaanterior.nome nomepessoasecretariaviaanterior FROM expedicaodiploma LEFT JOIN funcionario funcionarioprimario ON funcionarioprimario.codigo = expedicaodiploma.funcionarioprimario LEFT JOIN pessoa pessoafuncionarioprimario ON pessoafuncionarioprimario.codigo = funcionarioprimario.pessoa LEFT JOIN cargo cargoprimario ON cargoprimario.codigo = expedicaodiploma.cargofuncionarioprincipal LEFT JOIN funcionario funcionariosecundario ON funcionariosecundario.codigo = expedicaodiploma.funcionariosecundario LEFT JOIN pessoa pessoafuncionariosecundario ON pessoafuncionariosecundario.codigo = funcionariosecundario.pessoa LEFT JOIN cargo cargosecundario ON cargosecundario.codigo = expedicaodiploma.cargofuncionariosecundario LEFT JOIN funcionario reitorregistrodiplomaviaanterior ON reitorregistrodiplomaviaanterior.codigo = expedicaodiploma.reitorregistrodiplomaviaanterior LEFT JOIN pessoa pessoareitorviaanterior ON pessoareitorviaanterior.codigo = reitorregistrodiplomaviaanterior.pessoa LEFT JOIN cargo cargoreitoviaanterior ON cargoreitoviaanterior.codigo = expedicaodiploma.cargoreitorregistrodiplomaviaanterior LEFT JOIN funcionario secretariaviaanterior ON secretariaviaanterior.codigo = expedicaodiploma.secretariaregistrodiplomaviaanterior LEFT JOIN pessoa pessoasecretariaviaanterior ON pessoasecretariaviaanterior.codigo = secretariaviaanterior.pessoa WHERE expedicaodiploma.matricula = matricula.matricula ORDER BY expedicaodiploma.via DESC, expedicaodiploma.dataexpedicao DESC, expedicaodiploma.codigo DESC LIMIT 1) AS expedicaodiploma ON TRUE ");
		sql.append("WHERE matricula.matricula = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getMatricula().getMatricula());
		if (!tabelaResultado.next()) {
			throw new Exception("Dados Não Encontrados (Matricula)");
		}
		MatriculaVO matricula = new MatriculaVO();
		matricula.setMatricula(tabelaResultado.getString("matricula"));
		matricula.setDataConclusaoCurso(tabelaResultado.getDate("dataconclusaocurso"));
		matricula.setDataColacaoGrau(tabelaResultado.getDate("datacolacaograu"));
		matricula.setAnoConclusao(tabelaResultado.getString("anoconclusao"));
		matricula.setSemestreConclusao(tabelaResultado.getString("semestreconclusao"));
		matricula.getAluno().setCodigo(tabelaResultado.getInt("codigoaluno"));
		matricula.getAluno().setNome(tabelaResultado.getString("nomealuno"));
		matricula.getAluno().setCPF(tabelaResultado.getString("cpfaluno"));
		matricula.getAluno().setRG(tabelaResultado.getString("rgaluno"));
		matricula.getAluno().setOrgaoEmissor(tabelaResultado.getString("orgaoemissoraluno"));
		matricula.getAluno().setEstadoEmissaoRG(tabelaResultado.getString("estadoemissaorgaluno"));
		matricula.getAluno().setDataNasc(tabelaResultado.getDate("datanascaluno"));
		matricula.getAluno().setTelefoneRes(tabelaResultado.getString("telefoneresaluno"));
		matricula.getAluno().setTelefoneComer(tabelaResultado.getString("telefonecomeraluno"));
		matricula.getAluno().setCelular(tabelaResultado.getString("celularaluno"));
		matricula.getAluno().setSexo(tabelaResultado.getString("sexoaluno"));
		matricula.getAluno().getNaturalidade().setCodigo(tabelaResultado.getInt("naturalidadealuno"));
		matricula.getAluno().getNaturalidade().setNome(tabelaResultado.getString("nomenaturalidadealuno"));
		matricula.getAluno().getNaturalidade().getEstado().setCodigo(tabelaResultado.getInt("estadonaturalidadealuno"));
		matricula.getAluno().getNaturalidade().getEstado().setNome(tabelaResultado.getString("nomeestadonaturalidadealuno"));
		matricula.getAluno().getNaturalidade().getEstado().setSigla(tabelaResultado.getString("siglaestadonaturalidadealuno"));
		matricula.getAluno().getNacionalidade().setCodigo(tabelaResultado.getInt("codigonacionalizadadealuno"));
		matricula.getAluno().getNacionalidade().setNacionalidade(tabelaResultado.getString("nacionalidadenacionalizadadealuno"));
		matricula.getCurso().setCodigo(tabelaResultado.getInt("codigocurso"));
		matricula.getCurso().setNome(tabelaResultado.getString("nomecurso"));
		matricula.getCurso().setNomeDocumentacao(tabelaResultado.getString("nomedocumentacaocurso"));
		matricula.getCurso().setTitulo(tabelaResultado.getString("titulocurso"));
		matricula.getCurso().setHabilitacao(tabelaResultado.getString("habilitacaocurso"));
		matricula.getCurso().setIdCursoInep(tabelaResultado.getInt("idcursoinepcurso"));
		matricula.getCurso().setTitulacaoDoFormandoFeminino(tabelaResultado.getString("titulacaodoformandofemininocurso"));
		matricula.getCurso().setTitulacaoDoFormando(tabelaResultado.getString("titulacaodoformandocurso"));
		matricula.getCurso().setPeriodicidade(tabelaResultado.getString("periodicidadecurso"));
		matricula.getCurso().setNrRegistroInterno(tabelaResultado.getString("nrregistrointernocurso"));
		matricula.getUnidadeEnsino().setCodigo(tabelaResultado.getInt("codigounidadeensino"));
		matricula.getUnidadeEnsino().setNome(tabelaResultado.getString("nomeunidadeensino"));
		matricula.getUnidadeEnsino().setMantenedora(tabelaResultado.getString("mantenedoraunidadeensino"));
		matricula.getUnidadeEnsino().setNomeExpedicaoDiploma(tabelaResultado.getString("nomeexpedicaodiplomaunidadeensino"));
		matricula.getUnidadeEnsino().setAbreviatura(tabelaResultado.getString("abreviaturaunidadeensino"));
		matricula.getUnidadeEnsino().setCNPJ(tabelaResultado.getString("cnpjunidadeensino"));
		matricula.getUnidadeEnsino().setCodigoIES(tabelaResultado.getInt("codigoiesunidadeensino"));
		matricula.getUnidadeEnsino().getCidade().setCodigo(tabelaResultado.getInt("codigocidadeunidade"));
		matricula.getUnidadeEnsino().getCidade().setNome(tabelaResultado.getString("nomecidadeunidade"));
		matricula.getUnidadeEnsino().getCidade().getEstado().setCodigo(tabelaResultado.getInt("codigoestadounidade"));
		matricula.getUnidadeEnsino().getCidade().getEstado().setNome(tabelaResultado.getString("nomeestadounidade"));
		matricula.getUnidadeEnsino().getCidade().getEstado().setSigla(tabelaResultado.getString("siglaestadounidade"));
		ExpedicaoDiplomaVO expedicaoDiploma = new ExpedicaoDiplomaVO();
		expedicaoDiploma.setCodigo(tabelaResultado.getInt("codigoexpedicaodiploma"));
		expedicaoDiploma.setMatricula(matricula);
		expedicaoDiploma.setDataExpedicao(tabelaResultado.getDate("dataexpedicaoexpedicaodiploma"));
		expedicaoDiploma.setNumeroProcesso(tabelaResultado.getString("numeroprocessoexpedicaodiploma"));
		expedicaoDiploma.setVia(tabelaResultado.getString("viaexpedicaodiploma"));
		expedicaoDiploma.setNumeroProcessoViaAnterior(tabelaResultado.getString("numeroprocessoviaanteriorexpedicaodiploma"));
		expedicaoDiploma.setNumeroRegistroDiplomaViaAnterior(tabelaResultado.getString("numeroregistrodiplomaviaanteriorexpedicaodiploma"));
		expedicaoDiploma.setDataRegistroDiplomaViaAnterior(tabelaResultado.getDate("dataregistrodiplomaviaanteriorexpedicaodiploma"));
		expedicaoDiploma.setTituloFuncionarioPrincipal(tabelaResultado.getString("titulofuncionarioprincipalexpedicaodiploma"));
		expedicaoDiploma.setTituloFuncionarioSecundario(tabelaResultado.getString("funcionariosecundarioexpedicaodiploma"));
		expedicaoDiploma.getFuncionarioPrimarioVO().setCodigo(tabelaResultado.getInt("codigofuncionarioprimario"));
		expedicaoDiploma.getFuncionarioPrimarioVO().getPessoa().setCodigo(tabelaResultado.getInt("codigopessoafuncionarioprimario"));
		expedicaoDiploma.getFuncionarioPrimarioVO().getPessoa().setNome(tabelaResultado.getString("nomepessoafuncionarioprimario"));
		expedicaoDiploma.getFuncionarioSecundarioVO().setCodigo(tabelaResultado.getInt("codigofuncionariosecundario"));
		expedicaoDiploma.getFuncionarioSecundarioVO().getPessoa().setCodigo(tabelaResultado.getInt("codigopessoafuncionariosecundario"));
		expedicaoDiploma.getFuncionarioSecundarioVO().getPessoa().setNome(tabelaResultado.getString("nomepessoafuncionariosecundario"));
		expedicaoDiploma.getCargoFuncionarioPrincipalVO().setCodigo(tabelaResultado.getInt("codigocargoprimario"));
		expedicaoDiploma.getCargoFuncionarioPrincipalVO().setNome(tabelaResultado.getString("nomecargoprimario"));
		expedicaoDiploma.getCargoFuncionarioSecundarioVO().setCodigo(tabelaResultado.getInt("codigocargosecundario"));
		expedicaoDiploma.getCargoFuncionarioSecundarioVO().setNome(tabelaResultado.getString("nomecargosecundario"));
		expedicaoDiploma.getReitorRegistroDiplomaViaAnterior().setCodigo(tabelaResultado.getInt("codigoreitorregistrodiplomaviaanterior"));
		expedicaoDiploma.getReitorRegistroDiplomaViaAnterior().getPessoa().setCodigo(tabelaResultado.getInt("codigopessoareitorviaanterior"));
		expedicaoDiploma.getReitorRegistroDiplomaViaAnterior().getPessoa().setNome(tabelaResultado.getString("nomepessoareitorviaanterior"));
		expedicaoDiploma.getCargoReitorRegistroDiplomaViaAnterior().setCodigo(tabelaResultado.getInt("codigocargoreitoviaanterior"));
		expedicaoDiploma.getCargoReitorRegistroDiplomaViaAnterior().setNome(tabelaResultado.getString("nomecargoreitoviaanterior"));
		expedicaoDiploma.getSecretariaRegistroDiplomaViaAnterior().setCodigo(tabelaResultado.getInt("codigosecretariaviaanterior"));
		expedicaoDiploma.getSecretariaRegistroDiplomaViaAnterior().getPessoa().setCodigo(tabelaResultado.getInt("codigopessoasecretariaviaanterior"));
		expedicaoDiploma.getSecretariaRegistroDiplomaViaAnterior().getPessoa().setNome(tabelaResultado.getString("nomepessoasecretariaviaanterior"));
		AutorizacaoCursoVO reconhecimento = new AutorizacaoCursoVO();
		reconhecimento.setCodigo(tabelaResultado.getInt("codigoreconhecimento"));
		reconhecimento.setNome(tabelaResultado.getString("nomereconhecimento"));
		AutorizacaoCursoVO renovacaoReconhecimento = new AutorizacaoCursoVO();
		if (Uteis.isAtributoPreenchido(reconhecimento) && Uteis.isAtributoPreenchido(tabelaResultado.getInt("codigorenovacaoreconhecimentocurso")) && !Objects.equals(reconhecimento.getCodigo(), tabelaResultado.getInt("codigorenovacaoreconhecimentocurso"))) {
			renovacaoReconhecimento.setCodigo(tabelaResultado.getInt("codigorenovacaoreconhecimentocurso"));
			renovacaoReconhecimento.setNome(tabelaResultado.getString("nomerenovacaoreconhecimentocurso"));
		}
		String jsonFiliacao = tabelaResultado.getString("jsonFiliacao");
		if (Uteis.isAtributoPreenchido(jsonFiliacao)) {
			JSONArray jsonArray = new JSONArray(jsonFiliacao);
			if (jsonArray != null && jsonArray.length() > 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
					FiliacaoVO filiacaoVO = new FiliacaoVO();
					Object tipoFiliacao = jsonObject.get("TIPO");
					Object nomeFiliacao = jsonObject.get("NOME");
					if (Uteis.isAtributoPreenchido(tipoFiliacao) && Uteis.isAtributoPreenchido(nomeFiliacao)) {
						filiacaoVO.setTipo((String) tipoFiliacao);
						filiacaoVO.getPais().setNome((String) nomeFiliacao);
						matricula.getAluno().getFiliacaoVOs().add(filiacaoVO);
					}
				}
			}
		}
		matricula.setAutorizacaoCurso(reconhecimento);
		matricula.setRenovacaoReconhecimentoVO(renovacaoReconhecimento);
		controleLivroRegistroDiplomaRelVO.setMatricula(matricula);
		controleLivroRegistroDiplomaRelVO.setExpedicaoDiploma(expedicaoDiploma);
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarDataEnvioNotificacaoMensagemAtivacaoPreMatricula(List<MatriculaVO> listaAlunos, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("UPDATE matricula SET dataenvionotificacaoativacaomatricula = current_date WHERE matricula in ('0' ");
		for (MatriculaVO matricula : listaAlunos) {
			sqlStr.append(",  '").append(matricula.getMatricula()).append("' ");
		}
		sqlStr.append(") ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}

	@Override
	public List<MatriculaVO> consultarMatriculasAtivasAptasRealizarNotificacaoMensagemAtivacaoPreMatriculaPorChamadaProcessoSeletivo()throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select  matricula.matricula as matriculaAluno , ");
		sqlStr.append(" pessoa.codigo as codigoAluno , ");
		sqlStr.append(" pessoa.nome as nomeAluno , ");
		sqlStr.append(" pessoa.email as emailAluno , ");
		sqlStr.append(" pessoa.email2 as email2Aluno , ");
		sqlStr.append(" curso.codigo as codigoCurso ,  ");
		sqlStr.append(" curso.nome as nomeCurso ,  ");
		sqlStr.append(" matriculaperiodo.codigo as codigoMatriculaPeriodo , ");
		sqlStr.append(" matriculaperiodo.ano as anoMatriculaperiodo ,  ");
		sqlStr.append(" matriculaperiodo.semestre as semestreMatriculaperiodo  from matricula ");
		sqlStr.append(" inner join inscricao on inscricao.codigo = matricula.inscricao ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = inscricao.candidato and pessoa.codigo = matricula.aluno ");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sqlStr.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sqlStr.append(" inner join periodochamadaprocseletivo on periodochamadaprocseletivo.procseletivo = procseletivo.codigo and periodochamadaprocseletivo.nrchamada = inscricao.chamada ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and  matriculaperiodo.ano = procseletivo.ano and matriculaperiodo.semestre = procseletivo.semestre");
		sqlStr.append(" where matriculaperiodo.situacaomatriculaperiodo ='AT' ");
		sqlStr.append(" and  matricula.situacao ='AT' ");
		sqlStr.append(" and (SELECT COUNT(codigo) as nrMatriculaPeriodo FROM matriculaPeriodo where matriculaPeriodo.matricula = matricula.matricula) <=1 ");
		sqlStr.append(" and matricula.dataenvionotificacaoativacaomatricula is null  ");		
		sqlStr.append(" and periodochamadaprocseletivo.dataenviomensagemativacaomatricula = current_date  ");		
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<MatriculaVO> matriculaVOs = new ArrayList<MatriculaVO>(0);
		while (dadosSQL.next()) {
			MatriculaVO matriculaVO = new MatriculaVO();	
			matriculaVO.setNovoObj(false);
			matriculaVO.setMatricula(dadosSQL.getString("matriculaAluno"));	
			matriculaVO.getAluno().setCodigo(dadosSQL.getInt("codigoAluno"));
			matriculaVO.getAluno().setNome(dadosSQL.getString("nomeAluno"));
			matriculaVO.getAluno().setEmail(dadosSQL.getString("emailAluno"));
			matriculaVO.getAluno().setEmail2(dadosSQL.getString("email2Aluno"));
			matriculaVO.getCurso().setCodigo(dadosSQL.getInt("codigoCurso"));
			matriculaVO.getCurso().setNome(dadosSQL.getString("nomeCurso"));
			matriculaVO.getMatriculaPeriodoVO().setCodigo(dadosSQL.getInt("codigoMatriculaPeriodo"));
			matriculaVO.getMatriculaPeriodoVO().setAno(dadosSQL.getString("anoMatriculaperiodo"));
			matriculaVO.getMatriculaPeriodoVO().setSemestre(dadosSQL.getString("semestreMatriculaperiodo"));
			matriculaVOs.add(matriculaVO);
		}
		return matriculaVOs ;		
	}
	
	
	@Override
	public void realizarMontagemDadosMatriculaOnlineRealizada(MatriculaRSVO matriculaRSVO,MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo , ConfiguracaoGeralSistemaVO configuracaoGeralSistema, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO ,PeriodoChamadaProcSeletivoVO periodoChamadaProcSeletivoVO , Boolean gerarContratoValidandoDocObrigatorio,  UsuarioVO usuarioVO) throws Exception {

		matriculaRSVO.setMatricula(matricula.getMatricula());
		matriculaRSVO.setCodigoMatriculaPeriodo(matriculaPeriodo.getCodigo());
		PeriodoLetivoRSVO periodoLetivoCursar = new PeriodoLetivoRSVO();
		periodoLetivoCursar.setCodigo(matriculaPeriodo.getPeriodoLetivo().getCodigo());
		periodoLetivoCursar.setNome(matriculaPeriodo.getPeriodoLetivo().getDescricao());
		periodoLetivoCursar.setPeriodoLetivo(matriculaPeriodo.getPeriodoLetivo().getPeriodoLetivo());
		matriculaRSVO.setPeriodoLetivoRSVO(periodoLetivoCursar);

		TurmaRSVO turmaRSVO = new TurmaRSVO();
		turmaRSVO.setCodigo(matriculaPeriodo.getTurma().getCodigo());
		turmaRSVO.setNome(matriculaPeriodo.getTurma().getIdentificadorTurma());
		matriculaRSVO.setTurmaRSVO(turmaRSVO);
		matriculaRSVO.setCodigoTurma(turmaRSVO.getCodigo());
		matriculaRSVO.setAtivarPreMatriculaAposEntregaDocumentosObrigatorios(matricula.getCurso().getAtivarPreMatriculaAposEntregaDocumentosObrigatorios());
		matriculaRSVO.realizarValidacaoEcriacaoTextoDeclaracaoPertinentesMatriculaOnline(matriculaRSVO,	matricula.getCurso());		
		
		// referente regra de entrega documentos indeferido.					
		List<DocumetacaoMatriculaVO>  possuiDocumentoIndeferido = matricula.getDocumetacaoMatriculaVOs().stream().filter(p-> p.getTipoDeDocumentoVO().getPermitirPostagemPortalAluno() &&  !p.getDocumentoEntregue() &&  (p.getDataNegarDocDep() != null && Uteis.isAtributoPreenchido(p.getRespNegarDocDep().getCodigo()))).collect(Collectors.toList());			

		if(periodoChamadaProcSeletivoVO.getDentroPrazoPeriodoChamada()) {						
			ProcessoMatriculaVO processoMatriculaVO = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorMatriculaPeriodo(matriculaPeriodo.getCodigo(),usuarioVO);
				if(Uteis.isAtributoPreenchido(processoMatriculaVO)) {	   					
					ProcessoMatriculaRSVO processoMatriculaRSVO = new ProcessoMatriculaRSVO();
					processoMatriculaRSVO.setCodigo(processoMatriculaVO.getCodigo());
					processoMatriculaRSVO.setNome(processoMatriculaVO.getDescricao());
					try {
						processoMatriculaRSVO.setPermiteAlunoIncluirExcluirDisciplina(processoMatriculaVO.getPermiteIncluirExcluirDisciplinaVisaoAluno() && matriculaPeriodo.getPermitirIncluirExcluirDisciplinas() && matriculaPeriodo.getSituacaoPreMatricula());
					}catch (Exception e) {
						processoMatriculaRSVO.setPermiteAlunoIncluirExcluirDisciplina(false);	
					}	   		   			
					matriculaRSVO.setProcessoMatriculaRSVO(processoMatriculaRSVO);	
					matriculaRSVO.setPermiteAlunoIncluirExcluirDisciplina(processoMatriculaRSVO.getPermiteAlunoIncluirExcluirDisciplina());
					
					if(matriculaRSVO.getPermiteAlunoIncluirExcluirDisciplina() && !gerarContratoValidandoDocObrigatorio){	   		
						matriculaRSVO.setValidarAlunoJaMatriculado(false);
						getFacadeFactory().getMatriculaFacade().realizarMontagenDadosDisciplinasMatriculaOnline(matriculaRSVO,matriculaPeriodo.getMatriculaPeriodoTumaDisciplinaVOs(),usuarioVO);
						matriculaRSVO.getTurmaRSVO().setPossuiDisciplinasNaTurma(!matriculaPeriodo.getTurma().getTurmaDisciplinaVOs().isEmpty() || !matriculaRSVO.getDisciplinasMatricula().isEmpty());
					}	   					
				}   				
				
   			// referente a entrega documentos .               
	   	    matriculaRSVO.setDocumentacaoMatriculaVOs(matricula.getDocumetacaoMatriculaVOs().stream().filter(p-> p.getTipoDeDocumentoVO().getPermitirPostagemPortalAluno()).collect(Collectors.toList()));
			matriculaRSVO.setExisteMatriculaPendenteDocumento(matriculaRSVO.getDocumentacaoMatriculaVOs().stream().anyMatch(d -> !d.getDocumentoEntregue() &&  d.getTipoDeDocumentoVO().getPermitirPostagemPortalAluno() && ((d.getArquivoVO().getCodigo().equals(0) || (d.getTipoDeDocumentoVO().getDocumentoFrenteVerso() && d.getArquivoVOVerso().getCodigo().equals(0)))  ||   (d.getDataNegarDocDep() != null && Uteis.isAtributoPreenchido(d.getRespNegarDocDep().getCodigo())))));				
			getFacadeFactory().getDocumetacaoMatriculaFacade().realizarPreencherCaminhoAnexoImagemDocumentacaoMatricula(matriculaRSVO.getDocumentacaoMatriculaVOs(),configuracaoGeralSistema);
					
			// referente a contrato de matricula .
			try {							
				
				if(Uteis.isAtributoPreenchido(matricula.getUltimoMatriculaPeriodoVO().getContratoMatricula().getCodigo())
								&& matricula.getUltimoMatriculaPeriodoVO().getContratoMatricula().getAssinarDigitalmenteTextoPadrao()
								&& getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matricula.getUnidadeEnsino().getCodigo(), usuarioVO).getConfiguracaoGedContratoVO().getAssinarDocumento()
								&& getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matricula.getUnidadeEnsino().getCodigo(), usuarioVO).getConfiguracaoGedContratoVO().getProvedorAssinaturaPadraoEnum().isProvedorCertisign()) {
						matriculaRSVO.setAssinarDigitalmenteContrato(Boolean.TRUE);
				}

				if (Uteis.isAtributoPreenchido(matricula.getUltimoMatriculaPeriodoVO().getContratoMatricula().getCodigo())
						&& matricula.getUltimoMatriculaPeriodoVO().getContratoMatricula().getAssinarDigitalmenteTextoPadrao()
						&& getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matricula.getUnidadeEnsino().getCodigo(), usuarioVO).getConfiguracaoGedContratoVO().getAssinarDocumento()
						&& getAplicacaoControle().getConfiguracaoGEDPorUnidadeEnsino(matricula.getUnidadeEnsino().getCodigo(), usuarioVO).getConfiguracaoGedContratoVO().getProvedorAssinaturaPadraoEnum().isProvedorTechCert()) {
					matriculaRSVO.setAssinarDigitalmenteContrato(Boolean.TRUE);
				}
				
				matriculaRSVO.setDocumentoAssinadoPessoaVO(getFacadeFactory().getDocumentoAssinadoPessoaFacade().consultarDocumentosAssinadoPessoaPorMatriculaMatriculaPeriodo(matriculaRSVO.getMatricula() , matriculaRSVO.getCodigoMatriculaPeriodo()));
				
				if(Uteis.isAtributoPreenchido(matriculaRSVO.getDocumentoAssinadoPessoaVO())
						&&  matriculaRSVO.getDocumentoAssinadoPessoaVO().getSituacaoDocumentoAssinadoPessoaEnum().isPendente() 
						&& Uteis.isAtributoPreenchido(	matriculaRSVO.getDocumentoAssinadoPessoaVO().getUrlAssinatura())){						
					matriculaRSVO.setExisteMatriculaAptaPendenteAssinaturaContrato(Boolean.TRUE);								
				}							
				
				if(matriculaRSVO.getAssinarDigitalmenteContrato() && !Uteis.isAtributoPreenchido(matriculaRSVO.getDocumentoAssinadoPessoaVO()) && matricula.getCurso().getAtivarMatriculaAposAssinaturaContrato()) {
					matricula.setMatriculaOnlineExterna(true);
                    new DocumentacaoMatriculaRelControle().gerarContratoPDFMatriculaExterna(matricula,matricula.getUltimoMatriculaPeriodoVO(), configuracaoGeralSistema, configuracaoFinanceiroVO, usuarioVO,matriculaRSVO);
					DocumentoAssinadoPessoaVO documentoAssinado = getFacadeFactory().getDocumentoAssinadoPessoaFacade().consultarDocumentosAssinadoPessoaPorMatriculaMatriculaPeriodo(matriculaRSVO.getMatricula() , matriculaRSVO.getCodigoMatriculaPeriodo())  ;   
					if(Uteis.isAtributoPreenchido(documentoAssinado) && documentoAssinado.getSituacaoDocumentoAssinadoPessoaEnum().isPendente() ) {
						matriculaRSVO.setExisteMatriculaAptaPendenteAssinaturaContrato(Boolean.TRUE);
						matriculaRSVO.setDocumentoAssinadoPessoaVO(documentoAssinado);						
					}								
				}							
			} catch (Exception e) {
				matriculaRSVO.setExisteMatriculaAptaPendenteAssinaturaContrato(Boolean.FALSE);
				e.printStackTrace();
			}
			
			Boolean possuiDocObrigatorioPendente = (matriculaRSVO.getDocumentacaoMatriculaVOs().stream().anyMatch(d -> !d.getDocumentoEntregue() &&  d.getTipoDeDocumentoVO().getPermitirPostagemPortalAluno() &&  d.getGerarSuspensaoMatricula() &&	((d.getArquivoVO().getCodigo().equals(0) || (!d.getArquivoVO().getCodigo().equals(0) &&	d.getTipoDeDocumentoVO().getDocumentoFrenteVerso() && d.getArquivoVOVerso().getCodigo().equals(0))) || 	(d.getDataNegarDocDep() != null && Uteis.isAtributoPreenchido(d.getRespNegarDocDep().getCodigo())))));
			Boolean faltaDocumentoObrigatorioSerAprovado =	matriculaRSVO.getDocumentacaoMatriculaVOs().stream().anyMatch(d -> !d.getDocumentoEntregue() && d.getTipoDeDocumentoVO().getPermitirPostagemPortalAluno() && d.getGerarSuspensaoMatricula());
			if(!faltaDocumentoObrigatorioSerAprovado || (!possuiDocObrigatorioPendente &&  matricula.getCurso().getPermitirAssinarContratoPendenciaDocumentacao())) {
				matriculaRSVO.setPermitirAssinarContratoPendenciaDocumentacao( Boolean.TRUE );
			}			
			
		}else if(Uteis.isAtributoPreenchido(possuiDocumentoIndeferido) && periodoChamadaProcSeletivoVO.getAptoLiberarUploadDocumentoIndeferido()){						
			    matriculaRSVO.setDocumentacaoMatriculaVOs(possuiDocumentoIndeferido);					    
			    matriculaRSVO.setExisteMatriculaPendenteDocumento(matricula.getDocumetacaoMatriculaVOs().stream().anyMatch(d -> !d.getDocumentoEntregue() &&  d.getTipoDeDocumentoVO().getPermitirPostagemPortalAluno() && ((d.getArquivoVO().getCodigo().equals(0) || (d.getTipoDeDocumentoVO().getDocumentoFrenteVerso() && d.getArquivoVOVerso().getCodigo().equals(0)))  ||   (d.getDataNegarDocDep() != null && Uteis.isAtributoPreenchido(d.getRespNegarDocDep().getCodigo())))));				
			    getFacadeFactory().getDocumetacaoMatriculaFacade().realizarPreencherCaminhoAnexoImagemDocumentacaoMatricula(matriculaRSVO.getDocumentacaoMatriculaVOs(),configuracaoGeralSistema);
				matriculaRSVO.setPermiteAlunoIncluirExcluirDisciplina(false);
				matriculaRSVO.setPermitirAssinarContratoPendenciaDocumentacao(false);	           
		}else{
			throw new ConsistirException("Prezado Candidato a realização de matrícula para o curso ( "+matricula.getCurso().getNome()+" ) esta fora do período de matrícula ("+periodoChamadaProcSeletivoVO.getPeriodoInicialChamada_Apresentar()+ " a " +periodoChamadaProcSeletivoVO.getPeriodoFinalChamada_Apresentar()+" ). ");
		}   							
	    matriculaRSVO.setMatriculaRealizadaComSucesso(true);	       	
          
	}
	
	@Override
	public boolean verificarSeMatriculaEstaAptaReceberNotificacaoAtivacaoPreMatricula(String matricula,UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();		
		sqlStr.append(" select matricula.matricula from matricula  ");		
		sqlStr.append(" where matricula.matricula  = ? ");
		sqlStr.append(" and   matricula.situacao ='AT' ");
		sqlStr.append(" and   matricula.dataenvionotificacaoativacaomatricula is null  ");		
		sqlStr.append(" and   (matricula.inscricao is null  or  ");		
		sqlStr.append("  exists ( select inscricao.codigo from inscricao   ");
		sqlStr.append(" inner join procseletivo on procseletivo.codigo = inscricao.procseletivo ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and  matriculaperiodo.ano = procseletivo.ano and matriculaperiodo.semestre = procseletivo.semestre");
		sqlStr.append(" inner join periodochamadaprocseletivo on periodochamadaprocseletivo.procseletivo = procseletivo.codigo and periodochamadaprocseletivo.nrchamada = inscricao.chamada ");
		sqlStr.append(" where inscricao.codigo = matricula.inscricao ");				
		sqlStr.append(" and   matriculaperiodo.situacaomatriculaperiodo ='AT'   ");				
		sqlStr.append(" and   (periodochamadaprocseletivo.dataenviomensagemativacaomatricula is null or   periodochamadaprocseletivo.dataenviomensagemativacaomatricula <= current_date  ) )) ");		
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		if(dadosSQL.next()) {
			return true;
		}
		return false ;
	}
	
	@Override
	public MatriculaVO consultarPorObjetoMatriculaFacilitador(String valorConsulta, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE matricula = ? ");
		getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().montarFiltroTipoSupervisorFacilitador(ano, semestre, TipoSalaAulaBlackboardPessoaEnum.FACILITADOR.getValorApresentar());
		sqlStr.append(" and exists ( ");
		sqlStr.append(" select from calendariorelatoriofinalfacilitador ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sqlStr.append(" where matriculaperiodoturmadisciplina.matricula = matricula.matricula "); 
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = calendariorelatoriofinalfacilitador.disciplina ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.ano = calendariorelatoriofinalfacilitador.ano ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.semestre = calendariorelatoriofinalfacilitador.semestre) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta);
		if (!tabelaResultado.next()) {
			return new MatriculaVO();
		} else {
			MatriculaVO obj = new MatriculaVO();
			montarDadosBasico(obj, tabelaResultado);
			return obj;
		}
	}
	
	@Override
	public List<MatriculaVO> consultaRapidaPorNomeFacilitador(String nomePessoa, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE sem_acentos(Pessoa.nome) ilike sem_acentos(?) ");
		getFacadeFactory().getRelatorioFinalFacilitadorInterfaceFacade().montarFiltroTipoSupervisorFacilitador(ano, semestre, TipoSalaAulaBlackboardPessoaEnum.FACILITADOR.getValorApresentar());
		sqlStr.append(" and exists ( ");
		sqlStr.append(" select from calendariorelatoriofinalfacilitador ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sqlStr.append(" where matriculaperiodoturmadisciplina.matricula = matricula.matricula "); 
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = calendariorelatoriofinalfacilitador.disciplina ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.ano = calendariorelatoriofinalfacilitador.ano ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.semestre = calendariorelatoriofinalfacilitador.semestre) ");
		sqlStr.append(" ORDER BY Pessoa.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), nomePessoa+"%");
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public void consultaControleConsultaOtimizadoMatricula(DataModelo dataModelo, Integer unidadeEnsino, boolean controlarAcesso) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, dataModelo.getUsuario());
		StringBuffer sqlStr = getSQLPadraoConsultaBasica();
		dataModelo.getListaFiltros().clear();
		dataModelo.getListaConsulta().clear();
		if (dataModelo.getCampoConsulta().equals("nomePessoa")) {
			sqlStr.append(" WHERE sem_acentos(pessoa.nome) ilike(sem_acentos(?)) ");
			dataModelo.getListaFiltros().add(dataModelo.getValorConsulta() + PERCENT);
		} else if (dataModelo.getCampoConsulta().equals("matricula")) {
			sqlStr.append(" WHERE matricula.matricula ilike(?) ");
			dataModelo.getListaFiltros().add(dataModelo.getValorConsulta() + PERCENT);
		} else if (dataModelo.getCampoConsulta().equals("cpfAluno")) {
			sqlStr.append(" WHERE (replace(replace((pessoa.cpf),'.',''),'-','')) LIKE(?) ");
			dataModelo.getListaFiltros().add(Uteis.retirarMascaraCPF(dataModelo.getValorConsulta()));
		} else {
			sqlStr.append(" WHERE sem_acentos(curso.nome) ilike(sem_acentos(?)) ");
			dataModelo.getListaFiltros().add(dataModelo.getValorConsulta() + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND unidadeEnsino.codigo = ? ");
			dataModelo.getListaFiltros().add(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY Matricula.matricula ");
		if (dataModelo != null && Uteis.isAtributoPreenchido(dataModelo.getLimitePorPagina())) {
			sqlStr.append(" limit ").append(dataModelo.getLimitePorPagina()).append(" offset ").append(dataModelo.getOffset());
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
		if (dataModelo != null && tabelaResultado.next()) {
			dataModelo.setTotalRegistrosEncontrados(0);
			dataModelo.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
		}
		tabelaResultado.beforeFirst();
		dataModelo.setListaConsulta(montarDadosConsultaBasica(tabelaResultado));
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluirBloqueioMatricula(MatriculaVO obj, UsuarioVO usuarioVO) throws Exception {
	    try {
	        obj.setCodigoBloqueioMatriculaRenovacao((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
	            public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
	                StringBuilder sql = new StringBuilder("INSERT INTO bloqueiomatricula(matricula) VALUES (?)");
	                sql.append(" RETURNING codigo ");
	                sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

	                final PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
	                int i = 0;
	                Uteis.setValuePreparedStatement(obj.getMatricula(), ++i, sqlInserir);

	                return sqlInserir;
	            }
	        }, new ResultSetExtractor() {
	            public Object extractData(final ResultSet arg0) throws SQLException, DataAccessException {
	                if (arg0.next()) {
	                    obj.setNovoObj(Boolean.FALSE);
	                    return arg0.getInt("codigo");
	                }
	                return null;
	            }
	        }));
	    } catch (Exception e) {
	        obj.setNovoObj(Boolean.TRUE); 
	        throw e; 
	    }
	}


	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluirBolqueioMatricula(MatriculaVO obj, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("DELETE FROM bloqueiomatricula WHERE ((matricula = ?)) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { obj.getMatricula() });

	}
	
	
	
	@Override
	public MatriculaVO montarDadosBloqueioMatricula(SqlRowSet tabelaResultado) throws Exception {
		MatriculaVO obj = new MatriculaVO();
		obj.setMatricula(tabelaResultado.getString("matricula"));
		return obj;
	}
	
	@Override
	public MatriculaVO consultarBloqueioMatriculaPorMatricula(MatriculaVO matricula) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from bloqueiomatricula ");
		sql.append(" WHERE bloqueiomatricula.matricula = '").append( matricula.getMatricula()).append("'");
		sql.append(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return null;
		}
		return (montarDadosBloqueioMatricula(tabelaResultado));
	}
	
	
	@Override
	public String consultarSituacaoMatriculaPorMatricula(MatriculaVO matricula) throws Exception {
	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT situacao FROM matricula ");
	    sql.append("WHERE matricula.matricula = '").append(matricula.getMatricula()).append("' ");
	    sql.append("LIMIT 1");
	    
	    SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	    
	    if (!tabelaResultado.next()) {
	        return null;
	    }
	    
	    return tabelaResultado.getString("situacao");
	}
	
	@Override
	public MatriculaVO consultarMatriculaNrMatriculaCancelada(String matricula, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT * FROM matricula ");
		sqlStr.append(" WHERE matricula = ? ");
		sqlStr.append(" LIMIT 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), matricula);
		MatriculaVO matriculaVO = new MatriculaVO();
		while (tabelaResultado.next()) {
			matriculaVO.setMatricula(tabelaResultado.getString("matricula"));
			matriculaVO.getAluno().setCodigo(tabelaResultado.getInt("aluno"));;
			matriculaVO.setNrMatriculaCancelada(tabelaResultado.getString("nrmatriculacancelada"));
		}
		return matriculaVO;
	}
	
}