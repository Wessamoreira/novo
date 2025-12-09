package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import controle.arquitetura.AssuntoDebugEnum;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.academico.RenovarMatriculaControle;
import controle.arquitetura.ControleConsultaTurma;
import controle.arquitetura.DataModelo;
//import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaUnidadeEnsinoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.ForumVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.ProgramacaoFormaturaUnidadeEnsinoVO;
import negocio.comuns.academico.TurmaAberturaVO;
import negocio.comuns.academico.TurmaAgrupadaVO;
import negocio.comuns.academico.TurmaAtualizacaoDisciplinaLogVO;
import negocio.comuns.academico.TurmaContratoVO;
import negocio.comuns.academico.TurmaDisciplinaInclusaoSugeridaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaUnidadeEnsinoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.VagaTurmaDisciplinaVO;
import negocio.comuns.academico.VagaTurmaVO;
import negocio.comuns.academico.enumeradores.BimestreEnum;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.GrupoDestinatariosVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
//import negocio.comuns.financeiro.CategoriaDespesaVO;
//import negocio.comuns.financeiro.CentroResultadoVO;
//import negocio.comuns.financeiro.ChancelaVO;
//import negocio.comuns.financeiro.CondicaoRenegociacaoUnidadeEnsinoVO;
//
//import negocio.comuns.financeiro.ContaCorrenteVO;
//import negocio.comuns.planoorcamentario.UnidadesPlanoOrcamentarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.TurmaInterfaceFacade;
import webservice.servicos.TurmaRSVO;

/**
 * Classe de persistencia que encapsula todas as operacoes de manipulacao dos dados da classe <code>TurmaVO</code>. responsavel por implementar operacoes como incluir, alterar, excluir e consultar pertinentes a classe <code>TurmaVO</code>. Encapsula toda a interacao com o banco de dados.
 * 
 * @see TurmaVO
 * @see ControleAcesso
 */
@SuppressWarnings({ "serial", "unchecked", "rawtypes" })
@Repository
@Scope("singleton")
@Lazy
public class Turma extends ControleAcesso implements TurmaInterfaceFacade {

	protected static String idEntidade;

	public Turma() throws Exception {
		super();
		setIdEntidade("Turma");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>TurmaVO</code>.
	 */
	public TurmaVO novo() throws Exception {
		Turma.incluir(getIdEntidade());
		TurmaVO obj = new TurmaVO();
		return obj;
	}

	public void validarUnicidadeIdentificadorTurma(TurmaVO obj) throws Exception {
		TurmaVO turma = this.consultaRapidaUnicidadeTurmaPorIdentificador(obj.getIdentificadorTurma(), obj.getCodigo());
		if (turma.getCodigo() != 0) {
			throw new ConsistirException("Já existe uma Turma com identificador igual a ( " + obj.getIdentificadorTurma() + " )");
		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>TurmaVO</code>. Primeiramente valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissÃ¯Â¿Â½o do usuÃ¡rio para realizar esta operacÃ¯Â¿Â½o na entidade. Isto, através da Operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TurmaVO</code> que serÃ¯Â¿Â½ gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restriÃ§Ã£oo de acesso ou validaÃ§Ã£oo de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TurmaVO obj, UsuarioVO usuario) throws Exception {
//		Integer centroResultado = obj.getCentroResultadoVO().getCodigo();
		try {
			validarCentroResultadoExistentePorTurma(obj, usuario);
			TurmaVO.validarDados(obj);
			incluir(getIdEntidade(), true, usuario);
			validarUnicidadeIdentificadorTurma(obj);
			final StringBuilder sql = new StringBuilder("INSERT INTO Turma ")
					.append("(identificadorTurma, nrVagas, nrMaximoMatricula, nrMinimoMatricula, curso, periodoLetivo, unidadeEnsino, turno, situacao, gradeCurricular, ") // 10 em 10
					.append("sala, turmaAgrupada, semestral, anual, planoFinanceiroCurso, turmaPreMatricula, dataBaseGeracaoParcelas, grupoDestinatarios, chancela, tipoChancela, ")
					.append("porcentagemChancela, valorFixoChancela, valorPorAluno, contaCorrente, qtdAlunosEstimado, custoMedioAluno, receitaMediaAluno, utilizarDadosMatrizBoleto, dataPrevisaoFinalizacao, observacao, ")
					.append("subturma, turmaPrincipal, configuracaoead, avaliacaoonline, tipoSubTurma, apresentarRenovacaoOnline, abreviaturaCurso, nrVagasInclusaoReposicao, categoriaCondicaoPagamento, dataultimaalteracao, indiceReajuste, datacriacao, ")
					.append(" centroresultado, digitoTurma, codigoTurnoApresentarCenso, considerarTurmaAvaliacaoInstitucional ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ")
					.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ")
					.append("?, ?, ?, ?, ?, ? ) ")
					.append("returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setString(1, obj.getIdentificadorTurma().trim());
					sqlInserir.setInt(2, obj.getNrVagas().intValue());
					sqlInserir.setInt(3, obj.getNrMaximoMatricula().intValue());
					sqlInserir.setInt(4, obj.getNrMinimoMatricula().intValue());
					if (obj.getCurso().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getCurso().getCodigo().intValue());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (obj.getPeridoLetivo().getCodigo().intValue() != 0) {
						sqlInserir.setInt(6, obj.getPeridoLetivo().getCodigo().intValue());
					} else {
						sqlInserir.setNull(6, 0);
					}
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(7, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(7, 0);
					}
					if (obj.getTurno().getCodigo().intValue() != 0) {
						sqlInserir.setInt(8, obj.getTurno().getCodigo().intValue());
					} else {
						sqlInserir.setNull(8, 0);
					}
					sqlInserir.setString(9, obj.getSituacao());
					if (obj.getGradeCurricularVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(10, obj.getGradeCurricularVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(10, 0);
					}
					sqlInserir.setString(11, obj.getSala());
					sqlInserir.setBoolean(12, obj.getTurmaAgrupada());
					sqlInserir.setBoolean(13, obj.getSemestral());
					sqlInserir.setBoolean(14, obj.getAnual());
//					if (obj.getPlanoFinanceiroCurso().getCodigo().intValue() != 0) {
//						sqlInserir.setInt(15, obj.getPlanoFinanceiroCurso().getCodigo().intValue());
//					} else {
//						sqlInserir.setNull(15, 0);
//					}
					sqlInserir.setBoolean(16, Boolean.FALSE);
					sqlInserir.setDate(17, Uteis.getDataJDBC(obj.getDataBaseGeracaoParcelas()));
					if (obj.getGrupoDestinatarios().getCodigo().intValue() != 0) {
						sqlInserir.setInt(18, obj.getGrupoDestinatarios().getCodigo().intValue());
					} else {
						sqlInserir.setNull(18, 0);
					}
//					if (obj.getChancelaVO().getCodigo().intValue() != 0) {
//						sqlInserir.setInt(19, obj.getChancelaVO().getCodigo().intValue());
//					} else {
//						sqlInserir.setNull(19, 0);
//					}
					sqlInserir.setString(20, obj.getTipoChancela());
					sqlInserir.setDouble(21, obj.getPorcentagemChancela());
					sqlInserir.setDouble(22, obj.getValorFixoChancela());
					sqlInserir.setBoolean(23, obj.getValorPorAluno());
//					if (obj.getContaCorrente().getCodigo().intValue() != 0) {
//						sqlInserir.setInt(24, obj.getContaCorrente().getCodigo().intValue());
//					} else {
//						sqlInserir.setNull(24, 0);
//					}
					sqlInserir.setInt(25, obj.getQtdAlunosEstimado().intValue());
					sqlInserir.setDouble(26, obj.getCustoMedioAluno().doubleValue());
					sqlInserir.setDouble(27, obj.getReceitaMediaAluno().doubleValue());
					sqlInserir.setBoolean(28, obj.getUtilizarDadosMatrizBoleto().booleanValue());
					sqlInserir.setDate(29, Uteis.getDataJDBC(obj.getDataPrevisaoFinalizacao()));
					sqlInserir.setString(30, obj.getObservacao());
					sqlInserir.setBoolean(31, obj.getSubturma());
					if (obj.getTurmaPrincipal() != 0) {
						sqlInserir.setInt(32, obj.getTurmaPrincipal());
					} else {
						sqlInserir.setNull(32, 0);
					}

					if (obj.getConfiguracaoEADVO().getCodigo() != 0) {
						sqlInserir.setInt(33, obj.getConfiguracaoEADVO().getCodigo());
					} else {
						sqlInserir.setNull(33, 0);
					}

					if (obj.getAvaliacaoOnlineVO().getCodigo() != 0) {
						sqlInserir.setInt(34, obj.getAvaliacaoOnlineVO().getCodigo());
					} else {
						sqlInserir.setNull(34, 0);
					}
					sqlInserir.setString(35, obj.getTipoSubTurma().name());
					sqlInserir.setBoolean(36, obj.getApresentarRenovacaoOnline());
					sqlInserir.setString(37, obj.getAbreviaturaCurso());
					sqlInserir.setInt(38, obj.getNrVagasInclusaoReposicao());
					sqlInserir.setString(39, obj.getCategoriaCondicaoPagamento());
					sqlInserir.setTimestamp(40, Uteis.getDataJDBCTimestamp(new Date()));
//					if (obj.getIndiceReajusteVO().getCodigo() != 0) {
//						sqlInserir.setInt(41, obj.getIndiceReajusteVO().getCodigo());
//					} else {
//						sqlInserir.setNull(41, 0);
//					}
					sqlInserir.setTimestamp(42, Uteis.getDataJDBCTimestamp(new Date()));
//					Uteis.setValuePreparedStatement(obj.getCentroResultadoVO(), 43, sqlInserir);
					sqlInserir.setString(44, obj.getDigitoTurma());
					sqlInserir.setInt(45, obj.getCodigoTurnoApresentarCenso());
					sqlInserir.setBoolean(46, obj.getConsiderarTurmaAvaliacaoInstitucional());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet arg0) throws SQLException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getTurmaDisciplinaFacade().incluirTurmaDisciplinas(obj, obj.getTurmaDisciplinaVOs(), usuario);
			getFacadeFactory().getTurmaAgrupadaFacade().incluirTurmaAgrupadas(obj, obj.getTurmaAgrupadaVOs());
			getFacadeFactory().getTurmaAberturaFacade().incluirTurmaAberturas(obj, obj.getTurmaAberturaVOs(), usuario);
			getFacadeFactory().getTurmaDisciplinaInclusaoSugeridaInterfaceFacade().incluirTurmaDisciplinaInclusaoSugeridaVOs(obj, obj.getTurmaDisciplinaInclusaoSugeridaVOs(), usuario);
			getFacadeFactory().getTurmaContratoFacade().incluirTurmaContratoVOs(obj, usuario);
			getFacadeFactory().getArquivoFacade().atualizarDataDisponibilizacao(obj.getCodigo(), usuario);
			
//			if(Uteis.isAtributoPreenchido(obj.getVagaTurmaVO().getVagaTurmaDisciplinaVOs())) {
//				obj.getVagaTurmaVO().setTurmaVO(obj);
//				getFacadeFactory().getVagaTurmaFacade().persistir(obj.getVagaTurmaVO(), usuario);
//			}
			// atualizarTurmaCentroResultado(obj, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
//			if(!Uteis.isAtributoPreenchido(centroResultado)) {
//				obj.setCentroResultadoVO(new CentroResultadoVO());
//			}
			throw e;
		}
	}

	/**
	 * Operacao responsavel por alterar no BD os dados de um objeto da classe <code>TurmaVO</code>. Sempre utiliza a chave primaria da classe como atributo para localizacao do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexao com o banco de dados e a permissao do usuario para realizar esta operacao na entidade. Isto, atraves da operacao <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TurmaVO</code> que sera alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexao, restricao de acesso ou validacao de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TurmaVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarCentroResultadoExistentePorTurma(obj, usuario);
			TurmaVO.validarDados(obj);
			alterar(getIdEntidade(), true, usuario);
			validarUnicidadeIdentificadorTurma(obj);
			final StringBuilder sql = new StringBuilder("UPDATE Turma set identificadorTurma=?, nrVagas=?, nrMaximoMatricula=?, nrMinimoMatricula=?, curso=?, ")
					.append(" periodoLetivo=?, unidadeEnsino=?, turno=?, situacao=?, gradeCurricular=?, ") /////// separado de 5 em 5
					.append("sala=?, turmaAgrupada=?, planoFinanceiroCurso=?, turmaPreMatricula=?, semestral=?, ")
					.append("anual=?, dataBaseGeracaoParcelas=?, grupoDestinatarios=?, chancela=?, tipoChancela=?, ")
					.append("porcentagemChancela=?, valorFixoChancela=?, valorPorAluno=?, contaCorrente=?, qtdAlunosEstimado=?, ")
					.append("custoMedioAluno=?, receitaMediaAluno=?, utilizarDadosMatrizBoleto=?, dataPrevisaoFinalizacao=?, observacao=?, ")
					.append("subturma=?, turmaPrincipal=?, configuracaoead = ?, avaliacaoonline = ?, tipoSubTurma = ?, ")
					.append("apresentarRenovacaoOnline=?, abreviaturaCurso = ?, nrVagasInclusaoReposicao=?, categoriaCondicaoPagamento=?, dataultimaalteracao=?, indiceReajuste=?,   ")
					.append("centroresultado=?, digitoTurma=?, codigoTurnoApresentarCenso=?, considerarTurmaAvaliacaoInstitucional =? WHERE ((codigo = ?))").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setString(1, obj.getIdentificadorTurma().trim());
					sqlAlterar.setInt(2, obj.getNrVagas().intValue());
					sqlAlterar.setInt(3, obj.getNrMaximoMatricula().intValue());
					sqlAlterar.setInt(4, obj.getNrMinimoMatricula().intValue());
					if (obj.getCurso().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(5, obj.getCurso().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (obj.getPeridoLetivo().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(6, obj.getPeridoLetivo().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(7, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					if (obj.getTurno().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(8, obj.getTurno().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(8, 0);
					}
					sqlAlterar.setString(9, obj.getSituacao());
					if (obj.getGradeCurricularVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(10, obj.getGradeCurricularVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(10, 0);
					}
					sqlAlterar.setString(11, obj.getSala());
					sqlAlterar.setBoolean(12, obj.getTurmaAgrupada());

//					if (obj.getPlanoFinanceiroCurso().getCodigo().intValue() != 0) {
//						sqlAlterar.setInt(13, obj.getPlanoFinanceiroCurso().getCodigo().intValue());
//					} else {
//						sqlAlterar.setNull(13, 0);
//					}

					sqlAlterar.setBoolean(14, Boolean.FALSE);
					sqlAlterar.setBoolean(15, obj.getSemestral());
					sqlAlterar.setBoolean(16, obj.getAnual());
					sqlAlterar.setDate(17, Uteis.getDataJDBC(obj.getDataBaseGeracaoParcelas()));
					if (obj.getGrupoDestinatarios().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(18, obj.getGrupoDestinatarios().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(18, 0);
					}
//					if (obj.getChancelaVO().getCodigo().intValue() != 0) {
//						sqlAlterar.setInt(19, obj.getChancelaVO().getCodigo().intValue());
//					} else {
//						sqlAlterar.setNull(19, 0);
//					}
					sqlAlterar.setString(20, obj.getTipoChancela());
					sqlAlterar.setDouble(21, obj.getPorcentagemChancela());
					sqlAlterar.setDouble(22, obj.getValorFixoChancela());
					sqlAlterar.setBoolean(23, obj.getValorPorAluno());
//					if (obj.getContaCorrente().getCodigo().intValue() != 0) {
//						sqlAlterar.setInt(24, obj.getContaCorrente().getCodigo().intValue());
//					} else {
//						sqlAlterar.setNull(24, 0);
//					}
					sqlAlterar.setInt(25, obj.getQtdAlunosEstimado().intValue());
					sqlAlterar.setDouble(26, obj.getCustoMedioAluno().doubleValue());
					sqlAlterar.setDouble(27, obj.getReceitaMediaAluno().doubleValue());
					sqlAlterar.setBoolean(28, obj.getUtilizarDadosMatrizBoleto().booleanValue());
					sqlAlterar.setDate(29, Uteis.getDataJDBC(obj.getDataPrevisaoFinalizacao()));
					sqlAlterar.setString(30, obj.getObservacao());
					sqlAlterar.setBoolean(31, obj.getSubturma());
					if (obj.getTurmaPrincipal() != 0) {
						sqlAlterar.setInt(32, obj.getTurmaPrincipal());
					} else {
						sqlAlterar.setNull(32, 0);
					}

					if (obj.getConfiguracaoEADVO().getCodigo() != 0) {
						sqlAlterar.setInt(33, obj.getConfiguracaoEADVO().getCodigo());
						obj.getConfiguracaoEADVO().setNovoObj(false);
					} else {
						obj.getConfiguracaoEADVO().setNovoObj(true);
						sqlAlterar.setNull(33, 0);
					}

					if (obj.getAvaliacaoOnlineVO().getCodigo() != 0) {
						sqlAlterar.setInt(34, obj.getAvaliacaoOnlineVO().getCodigo());
						obj.getAvaliacaoOnlineVO().setNovoObj(false);
					} else {
						sqlAlterar.setNull(34, 0);
						obj.getAvaliacaoOnlineVO().setNovoObj(true);
					}
					sqlAlterar.setString(35, obj.getTipoSubTurma().name());
					sqlAlterar.setBoolean(36, obj.getApresentarRenovacaoOnline());
					sqlAlterar.setString(37, obj.getAbreviaturaCurso());
					sqlAlterar.setInt(38, obj.getNrVagasInclusaoReposicao());
					sqlAlterar.setString(39, obj.getCategoriaCondicaoPagamento());
					sqlAlterar.setTimestamp(40, Uteis.getDataJDBCTimestamp(new Date()));
//					if (obj.getIndiceReajusteVO().getCodigo() != 0) {
//						sqlAlterar.setInt(41, obj.getIndiceReajusteVO().getCodigo());
//					} else {
//						sqlAlterar.setNull(41, 0);
//					}
//					Uteis.setValuePreparedStatement(obj.getCentroResultadoVO(), 42, sqlAlterar);
					sqlAlterar.setString(43, obj.getDigitoTurma());
					sqlAlterar.setInt(44, obj.getCodigoTurnoApresentarCenso());
					sqlAlterar.setBoolean(45, obj.getConsiderarTurmaAvaliacaoInstitucional());
					sqlAlterar.setInt(46, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getTurmaDisciplinaFacade().alterarTurmaDisciplinas(obj, obj.getTurmaDisciplinaVOs(), usuario);
			getFacadeFactory().getTurmaAgrupadaFacade().alterarTurmaAgrupadas(obj, obj.getTurmaAgrupadaVOs());
			getFacadeFactory().getTurmaAberturaFacade().alterarTurmaAberturas(obj, obj.getTurmaAberturaVOs(), usuario);
			getFacadeFactory().getTurmaDisciplinaInclusaoSugeridaInterfaceFacade().alterarTurmaDisciplinaInclusaoSugeridaVOs(obj, obj.getTurmaDisciplinaInclusaoSugeridaVOs(), usuario);
			getFacadeFactory().getTurmaContratoFacade().alterarTurmaContratoVOs(obj, usuario);
			// código FOI COMENTADO A PEDIDO DO RODIRO
			// PELO FATO DE ESTAR ALTERANDO A GRADE CURRICULAR DE ALUNOS QUE VIERAM DE IMPORTAï¿½ï¿½O ERRONEAMENTE
			// if (!obj.getTurmaAgrupada()) {
			// getFacadeFactory().getMatriculaPeriodoFacade().verificarGradeAlunosMatriculadosTurma(obj.getGradeCurricularVO().getCodigo(), obj.getPeridoLetivo().getCodigo(), obj.getCodigo(), usuario);
			// }
			getFacadeFactory().getArquivoFacade().atualizarDataDisponibilizacao(obj.getCodigo(), usuario);
			// atualizarTurmaCentroResultado(obj, usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	private void validarCentroResultadoExistentePorTurma(TurmaVO turma, UsuarioVO usuarioVO) {
//		if (!Uteis.isAtributoPreenchido(turma.getCentroResultadoVO())) {
//			Integer curso = turma.getCurso().getCodigo();
//			if(turma.getTurmaAgrupada() && !turma.getTurmaAgrupadaVOs().isEmpty()) {
//				curso = turma.getTurmaAgrupadaVOs().get(0).getTurma().getCurso().getCodigo();
//			}
//			CentroResultadoVO obj = getFacadeFactory().getCentroResultadoFacade().validarGeracaoDoCentroResultadoAutomatico(turma.getIdentificadorTurma(), turma.getUnidadeEnsino().getCodigo(), null, curso, true, usuarioVO);
//			turma.setCentroResultadoVO(obj);
//		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataPrevisaoFinalizacao(final Integer turma, final Date data, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE Turma set dataPrevisaoFinalizacao=?, dataultimaalteracao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (data != null) {
						sqlAlterar.setDate(1, Uteis.getDataJDBC(data));
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
					sqlAlterar.setInt(3, turma);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
		System.out.println();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataBaseGeracaoTurmaParcela(final TurmaVO turma, Date dataBaseGeracaoParcelas, List<MatriculaVO> listaMatriculaComControleGeracaoParcelaDataBase, UsuarioVO usuario) throws Exception {
		try {
			alterar(getIdEntidade(), true, usuario);
			Uteis.checkState(!Uteis.isAtributoPreenchido(dataBaseGeracaoParcelas), "O campo Data Base Geração Parcela deve ser informado.");
//			if (Uteis.isAtributoPreenchido(listaMatriculaComControleGeracaoParcelaDataBase)) {
//				getFacadeFactory().getMatriculaPeriodoVencimentoFacade().realizarAlteracaoDataVencimentoPorDataBaseGeracaoTurma(turma, dataBaseGeracaoParcelas, listaMatriculaComControleGeracaoParcelaDataBase, usuario);
//			}
			final String sql = "UPDATE Turma set databasegeracaoparcelas=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(dataBaseGeracaoParcelas));
					sqlAlterar.setInt(2, turma.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operacao responsavel por excluir no BD um objeto da classe <code>TurmaVO</code>. Sempre localiza o registro a ser excluido atraves da chave primaria da entidade. Primeiramente verifica a conexao com o banco de dados e a permissao do usuario para realizar esta operacao na entidade. Isto, atraves da operacao <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TurmaVO</code> que sera removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexao ou restricao de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TurmaVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), true, usuarioVO);
			getFacadeFactory().getTurmaDisciplinaFacade().excluirTurmaDisciplinas(obj.getCodigo(), usuarioVO);
			getFacadeFactory().getTurmaAgrupadaFacade().excluirTurmaAgrupadas(obj.getCodigo());
			getFacadeFactory().getTurmaAberturaFacade().excluirTurmaAberturas(obj.getCodigo());
			getFacadeFactory().getTurmaDisciplinaInclusaoSugeridaInterfaceFacade().excluirPorTurma(obj, null);
			String sql = "DELETE FROM Turma WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
//			if(Uteis.isAtributoPreenchido(obj.getCentroResultadoVO())) {
//				getFacadeFactory().getCentroResultadoFacade().excluir(obj.getCentroResultadoVO(), false, false, usuarioVO);
//			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAtualizacaoGradeCurricularCursoIntegral(TurmaVO turmaAtual, TurmaVO turmaNovaGradeCurricular,  UsuarioVO usuarioVO, List<MatriculaPeriodoVO> listaMatriculaPeriodo) {
		List<TurmaDisciplinaVO> listaAtualGradeCurricular = turmaAtual.getTurmaDisciplinaVOs().stream().filter(p1 -> !p1.isAlterarMatrizCurricular()).collect(Collectors.toList());
		try {
			validarDadosParaAtualizacaoGradeCurricularCursoIntegral(turmaAtual, turmaNovaGradeCurricular, listaAtualGradeCurricular);
			turmaNovaGradeCurricular.setCodigo(turmaAtual.getCodigo());
			turmaNovaGradeCurricular.setUnidadeEnsino(turmaAtual.getUnidadeEnsino());
			turmaNovaGradeCurricular.setCurso(turmaAtual.getCurso());
			if (turmaAtual.isExisteAlunosMatriculados()) {
				adicionarGradeCurricularCursoIntegralParaTurma(listaMatriculaPeriodo, turmaAtual, turmaNovaGradeCurricular, listaAtualGradeCurricular, usuarioVO);
				verificarOperacoesAlteracaoGradeCurricularCursoIntegralParaTurmaBase(listaMatriculaPeriodo, turmaAtual, turmaNovaGradeCurricular, listaAtualGradeCurricular, usuarioVO);
				atualizarHistoricoForaGradeNaoExistenteTurmaDisciplina(listaMatriculaPeriodo, turmaAtual, turmaNovaGradeCurricular, usuarioVO);
				executarAlteracaoPeriodoLetivoGradeCurricularMatriculaPeriodo(turmaNovaGradeCurricular, "", "", listaMatriculaPeriodo, usuarioVO, true);
			}
			if (turmaAtual.isExisteAlunosReposicao()) {
				verificarOperacoesAlteracaoGradeCurricularCursoIntegralParaReposicao(turmaAtual, turmaNovaGradeCurricular, listaAtualGradeCurricular, usuarioVO);
			}
			gerarLogDasAlteracoesGradeCurricular(turmaAtual, turmaNovaGradeCurricular, usuarioVO, listaAtualGradeCurricular);
			turmaAtual.setGradeCurricularVO((GradeCurricularVO) turmaNovaGradeCurricular.getGradeCurricularVO().clone());
			turmaAtual.setPeridoLetivo((PeriodoLetivoVO) turmaNovaGradeCurricular.getPeridoLetivo().clone());
			StringBuilder sql = new StringBuilder("UPDATE Turma set ");
			sql.append(" gradeCurricular=?, periodoLetivo=? ");
			sql.append(" WHERE codigo = ?");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, turmaAtual.getGradeCurricularVO().getCodigo());
					sqlAlterar.setInt(2, turmaAtual.getPeridoLetivo().getCodigo());
					sqlAlterar.setInt(3, turmaAtual.getCodigo());
					return sqlAlterar;
				}
			});
			turmaAtual.setTurmaDisciplinaVOs(new ArrayList<>());
			List<GradeDisciplinaVO> listaGradeDisciplina = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(turmaAtual.getPeridoLetivo().getCodigo(), false, usuarioVO, null);
			getFacadeFactory().getTurmaFacade().executarGeracaoTurmaDisciplinaVOs(turmaAtual, listaGradeDisciplina, usuarioVO);
			turmaAtual.atualizarTotalCreditoCargaHorariaDisciplinasOptativasERegulares();
			for (TurmaDisciplinaVO turmaDisciplinaVO : turmaAtual.getTurmaDisciplinaVOs()) {
				getFacadeFactory().getTurmaFacade().obterLocalSalaTurmaDisciplinaLog(turmaDisciplinaVO, turmaAtual.getCodigo());
			}
			getFacadeFactory().getTurmaDisciplinaFacade().montarDadosListaSelectItemModalidade(turmaAtual.getTurmaDisciplinaVOs());
//			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAtualizacaoDisciplinaTurmaRealizado(turmaAtual, usuarioVO);
			getFacadeFactory().getTurmaDisciplinaFacade().alterarTurmaDisciplinas(turmaAtual, turmaAtual.getTurmaDisciplinaVOs(), usuarioVO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void atualizarHistoricoForaGradeNaoExistenteTurmaDisciplina(List<MatriculaPeriodoVO> listaMatriculaPeriodo, TurmaVO turmaAtual, TurmaVO turmaNovaGradeCurricular, UsuarioVO usuarioVO) throws Exception {
		List<MatriculaPeriodoVO>listaMpHistorioForaGrade = getFacadeFactory().getHistoricoFacade().verificarSeExisteHistoricoForaGradePorAlteracaoGradeCurricularCursoIntegral(listaMatriculaPeriodo, null, turmaAtual.getGradeCurricularVO().getCodigo(), usuarioVO);
		if(Uteis.isAtributoPreenchido(listaMpHistorioForaGrade)){
			getFacadeFactory().getHistoricoFacade().alterarHistoricoForaDaGradePorAlteracaoGradeCurricularCursoIntegral(listaMpHistorioForaGrade, turmaNovaGradeCurricular, null, turmaAtual.getGradeCurricularVO().getCodigo(), true, usuarioVO);
		}
	}

	private void gerarLogDasAlteracoesGradeCurricular(TurmaVO turmaAtual, TurmaVO turmaNovaGradeCurricular, UsuarioVO usuarioVO, List<TurmaDisciplinaVO> listaAtualGradeCurricular) throws Exception {
		TurmaAtualizacaoDisciplinaLogVO tadLog = new TurmaAtualizacaoDisciplinaLogVO();
		getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarLinhaTracejada(tadLog, true);
		getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, "Log Alteração de Troca de Matriz  Curricular", true);
		getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, "Antiga Matriz  - "+turmaAtual.getGradeCurricularVO().getCodigo() +"-"+ turmaAtual.getGradeCurricularVO().getNome(), true);
		getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, "Nova Matriz    - "+turmaNovaGradeCurricular.getGradeCurricularVO().getCodigo() +"-"+ turmaNovaGradeCurricular.getGradeCurricularVO().getNome(), true);

		if(turmaAtual.isExisteAlunosMatriculados()){
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, "Qtd Alunos Turma base - "+	turmaAtual.getQtdMatriculados(), true);	
		}
		if(turmaAtual.isExisteAlunosReposicao()){
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, "Qtd Alunos Reposição - "+	turmaAtual.getQtdReposicao(), true);	
		}
		
		if(turmaNovaGradeCurricular.getTurmaDisciplinaVOs().stream().anyMatch(p->p.isAlterarMatrizCurricular())){
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarLinhaTracejada(tadLog, true);
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, "Disciplinas Mantidas", true);
			turmaNovaGradeCurricular.getTurmaDisciplinaVOs().stream().filter(p->p.isAlterarMatrizCurricular()).forEach(p->getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, +p.getDisciplina().getCodigo() +"-"+p.getDisciplina().getNome() + ".", true));
		}
		
		if(turmaNovaGradeCurricular.getTurmaDisciplinaVOs().stream().anyMatch(p->!p.isAlterarMatrizCurricular() && !p.getDisciplinaReferenteAUmGrupoOptativa() && p.getOperacaoMatrizCurricular().equals(-2))){
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarLinhaTracejada(tadLog, true);
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, "Disciplinas Incluidas", true);
			turmaNovaGradeCurricular.getTurmaDisciplinaVOs().stream().filter(p->!p.isAlterarMatrizCurricular() && !p.getDisciplinaReferenteAUmGrupoOptativa() && p.getOperacaoMatrizCurricular().equals(-2)).forEach(p->getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, +p.getDisciplina().getCodigo() +"-"+p.getDisciplina().getNome() + ".", true));
		}
		
		if(listaAtualGradeCurricular.stream().anyMatch(p->p.getOperacaoMatrizCurricular().equals(-2))){
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarLinhaTracejada(tadLog, true);
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, "Disciplinas Excluidas", true);
			listaAtualGradeCurricular.stream().filter(p->p.getOperacaoMatrizCurricular().equals(-2)).forEach(p->getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, +p.getDisciplina().getCodigo() +"-"+p.getDisciplina().getNome() + ".", true));
		}
		
		if(listaAtualGradeCurricular.stream().anyMatch(p->p.getOperacaoMatrizCurricular().equals(-1))){
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarLinhaTracejada(tadLog, true);
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, "Disciplinas Mantida Fora Da Grade", true);
			listaAtualGradeCurricular.stream().filter(p->p.getOperacaoMatrizCurricular().equals(-1)).forEach(p->getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, +p.getDisciplina().getCodigo() +"-"+p.getDisciplina().getNome() + ".", true));
		}
		
		if(listaAtualGradeCurricular.stream().anyMatch(p->!p.getOperacaoMatrizCurricular().equals(-2) && !p.getOperacaoMatrizCurricular().equals(-1))){
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarLinhaTracejada(tadLog, true);
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, "Disciplinas Realizado Correspondência", true);
			listaAtualGradeCurricular.stream().filter(p->!p.getOperacaoMatrizCurricular().equals(-2) && !p.getOperacaoMatrizCurricular().equals(-1))
			.forEach(p->{
				TurmaDisciplinaVO tdCorrespodente = geObterTurmaDisciplinaAlteracaoGradeCurricular(turmaNovaGradeCurricular, p.getOperacaoMatrizCurricular());
			    if(p.getDisciplinaReferenteAUmGrupoOptativa() && tdCorrespodente.getDisciplinaReferenteAUmGrupoOptativa()){
			    	getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, p.getDisciplina().getCodigo() +"- Grupo Optativa -"+p.getDisciplina().getNome() + " - para - "+tdCorrespodente.getDisciplina().getCodigo()+"- Grupo Optativa -" + tdCorrespodente.getDisciplina().getNome(), true);
			    }else if(tdCorrespodente.getDisciplinaReferenteAUmGrupoOptativa()){
					getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, p.getDisciplina().getCodigo() +"-"+p.getDisciplina().getNome() + " - para - "+tdCorrespodente.getDisciplina().getCodigo()+"- Grupo Optativa -" + tdCorrespodente.getDisciplina().getNome(), true);
				} else if(p.getDisciplinaReferenteAUmGrupoOptativa()){
					getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, p.getDisciplina().getCodigo() +"- Grupo Optativa -"+p.getDisciplina().getNome() + " - para - "+tdCorrespodente.getDisciplina().getCodigo()+"-" + tdCorrespodente.getDisciplina().getNome(), true);					
				}else{
					getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, p.getDisciplina().getCodigo() +"-"+p.getDisciplina().getNome() + " - para - "+tdCorrespodente.getDisciplina().getCodigo()+"-"+ tdCorrespodente.getDisciplina().getNome(), true);
				}	
			});
		}
		if(listaAtualGradeCurricular.stream().anyMatch(p->p.getQtdAlunosReposicao() > 0 && p.getOperacaoMatrizCurricularReposicao().equals(-2))){
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarLinhaTracejada(tadLog, true);
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, "Disciplinas Reposição Excluida", true);
			listaAtualGradeCurricular.stream().filter(p->p.getQtdAlunosReposicao() > 0 && p.getOperacaoMatrizCurricularReposicao().equals(-2)).forEach(p->getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, +p.getDisciplina().getCodigo() +"-"+p.getDisciplina().getNome() + " -("+p.getQtdAlunosReposicao()+") alunos", true));
		}
		if(listaAtualGradeCurricular.stream().anyMatch(p->p.getQtdAlunosReposicao() > 0 && p.getOperacaoMatrizCurricularReposicao().equals(-1))){
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarLinhaTracejada(tadLog, true);
			getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, "Disciplinas Mantida/Equivalencia", true);
			listaAtualGradeCurricular.stream().filter(p->p.getQtdAlunosReposicao() > 0 && p.getOperacaoMatrizCurricularReposicao().equals(-1)).forEach(p->{
				TurmaDisciplinaVO tdCorrespodente = geObterTurmaDisciplinaAlteracaoGradeCurricular(turmaNovaGradeCurricular, p.getOperacaoMatrizCurricular());
			    if(p.getDisciplinaReferenteAUmGrupoOptativa() && tdCorrespodente.getDisciplinaReferenteAUmGrupoOptativa()){
			    	getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, p.getDisciplina().getCodigo() +"- Grupo Optativa -"+p.getDisciplina().getNome() + " - para - "+tdCorrespodente.getDisciplina().getCodigo()+"- Grupo Optativa -" + tdCorrespodente.getDisciplina().getNome()+ " -("+p.getQtdAlunosReposicao()+") alunos", true);
			    }else if(tdCorrespodente.getDisciplinaReferenteAUmGrupoOptativa()){
					getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, p.getDisciplina().getCodigo() +"-"+p.getDisciplina().getNome() + " - para - "+tdCorrespodente.getDisciplina().getCodigo()+"- Grupo Optativa -" + tdCorrespodente.getDisciplina().getNome()+ " -("+p.getQtdAlunosReposicao()+") alunos", true);
				} else if(p.getDisciplinaReferenteAUmGrupoOptativa()){
					getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, p.getDisciplina().getCodigo() +"- Grupo Optativa -"+p.getDisciplina().getNome() + " - para - "+tdCorrespodente.getDisciplina().getCodigo()+"-" + tdCorrespodente.getDisciplina().getNome()+ " -("+p.getQtdAlunosReposicao()+") alunos", true);					
				}else{
					getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().registrarInstrucaoLog(tadLog, p.getDisciplina().getCodigo() +"-"+p.getDisciplina().getNome() + " - para - "+tdCorrespodente.getDisciplina().getCodigo()+"-"+ tdCorrespodente.getDisciplina().getNome()+ " -("+p.getQtdAlunosReposicao()+") alunos", true);
				}
			});
		}
		getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().executarInclusaoLog(tadLog, turmaAtual, usuarioVO);
		
		turmaAtual.getListaTurmaAtualizacaoDisciplinaLog().add(tadLog);
		Ordenacao.ordenarListaDecrescente(turmaAtual.getListaTurmaAtualizacaoDisciplinaLog(), "data");
	}

	private void validarDadosParaAtualizacaoGradeCurricularCursoIntegral(TurmaVO turmaAtual, TurmaVO turmaNovaGradeCurricular, List<TurmaDisciplinaVO> listaAtualGradeCurricular)  {
		Uteis.checkState(!Uteis.isAtributoPreenchido(turmaNovaGradeCurricular.getGradeCurricularVO()), "O campo Nova Matriz Curricular deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(turmaNovaGradeCurricular.getPeridoLetivo()), "O campo Novo Período Letivo deve ser informado.");
		Uteis.checkState(turmaNovaGradeCurricular.getGradeCurricularVO().getCodigo().equals(turmaAtual.getGradeCurricularVO().getCodigo()), "O campo Nova Matriz Curricular deve ser diferente da Matriz Curricular Atual.");
		Uteis.checkState(turmaNovaGradeCurricular.getPeridoLetivo().getCodigo().equals(turmaAtual.getPeridoLetivo().getCodigo()), "O campo Novo Período Letivo deve ser diferente do Período Letivo Atual.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(turmaNovaGradeCurricular.getTurmaDisciplinaVOs()), "Não existe uma grade disciplina para a Nova Matriz Curricular.");
		turmaAtual.getTurmaDisciplinaVOs().stream()
				.filter(p -> !p.getOperacaoMatrizCurricular().equals(-2) && !p.getOperacaoMatrizCurricular().equals(-1))
				.forEach(p -> 
					Uteis.checkState(turmaAtual.getTurmaDisciplinaVOs().stream()
							.anyMatch(p1 -> !p1.getDisciplina().getCodigo().equals(p.getDisciplina().getCodigo()) && p1.getOperacaoMatrizCurricular().equals(p.getOperacaoMatrizCurricular())),
							"Não foi possível escolher a correspondência de código  " + p.getOperacaoMatrizCurricular() + " para a disciplina " + p.getDisciplina().getNome() + ", pois ela já esta sendo utilizada para outra discipina.")
				);
		listaAtualGradeCurricular.stream()
				.filter(p -> p.getOperacaoMatrizCurricular().equals(-2) && getFacadeFactory().getRegistroAulaFacade().existeRegistroAula(turmaAtual, p.getDisciplina()))
				.forEach(p -> {
					throw new StreamSeiException("Não foi possível excluir essa disciplina " + p.getDisciplina().getNome() + " da grade, pois a mesma tem aula registrada para essa turma.");
				});
	}

	private void adicionarGradeCurricularCursoIntegralParaTurma(List<MatriculaPeriodoVO> listaMatriculaPeriodo, TurmaVO turmaAtual, TurmaVO turmaNovaGradeCurricular, List<TurmaDisciplinaVO> listaAtualGradeCurricular,  UsuarioVO usuarioVO) throws Exception {
		for (TurmaDisciplinaVO td : turmaNovaGradeCurricular.getTurmaDisciplinaVOs()) {			
			if (td.isAlterarMatrizCurricular()) {
				getFacadeFactory().getTurmaDisciplinaFacade().alterarTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegral(turmaAtual.getCodigo(), td.getDisciplina().getCodigo(), td.getGradeDisciplinaVO().getCodigo(), td.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), usuarioVO);
				if (Uteis.isAtributoPreenchido(listaMatriculaPeriodo)) {
					getFacadeFactory().getHistoricoFacade().alterarHistoricoPorAlteracaoGradeCurricularCursoIntegral(turmaNovaGradeCurricular, td.getDisciplina(), td.getDisciplina().getCodigo(), td.getGradeDisciplinaVO(), td.getGradeCurricularGrupoOptativaDisciplinaVO(), listaMatriculaPeriodo, usuarioVO);
					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarMatriculaPeriodoTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegral(turmaAtual.getCodigo(), td.getDisciplina().getCodigo(), td.getDisciplina().getCodigo(), td.getGradeDisciplinaVO().getCodigo(), td.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), listaMatriculaPeriodo, usuarioVO);
				}
				if(td.getQtdAlunosReposicao() > 0){					
					manterDisciplinaParaGradeCurricularCursoParaAlunosReposicao(turmaAtual, turmaNovaGradeCurricular, td, usuarioVO);
				}
			} else if (!td.isAlterarMatrizCurricular() && !td.getDisciplinaReferenteAUmGrupoOptativa() && td.getOperacaoMatrizCurricular().equals(-2)) { // nesse caso considero a operacao -2 pois é o valor inicial e como ela nao foi encontrada na grade atual e nem escolhida uma operacao ela sera adicionada
				List<MatriculaPeriodoVO>listaMpAptarIncluir = new ArrayList<>();
				List<MatriculaPeriodoVO>listaMpHistorioForaGrade = getFacadeFactory().getHistoricoFacade().verificarSeExisteHistoricoForaGradePorAlteracaoGradeCurricularCursoIntegral(listaMatriculaPeriodo, td.getDisciplina().getCodigo(), turmaAtual.getGradeCurricularVO().getCodigo(), usuarioVO);
				if(Uteis.isAtributoPreenchido(listaMpHistorioForaGrade)){
					getFacadeFactory().getHistoricoFacade().alterarHistoricoForaDaGradePorAlteracaoGradeCurricularCursoIntegral(listaMpHistorioForaGrade, turmaNovaGradeCurricular, td.getDisciplina().getCodigo(), turmaAtual.getGradeCurricularVO().getCodigo(), false, usuarioVO);
					listaMpAptarIncluir.addAll(listaMatriculaPeriodo.stream().filter(p-> listaMpHistorioForaGrade.stream().allMatch(p1-> !p.getCodigo().equals(p1.getCodigo()))).collect(Collectors.toList()));
				}else{
					listaMpAptarIncluir.addAll(listaMatriculaPeriodo);
				}
				if(Uteis.isAtributoPreenchido(listaMpAptarIncluir)){
					for (MatriculaPeriodoVO mp : listaMpAptarIncluir) {
						MatriculaPeriodoTurmaDisciplinaVO mptd = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().gerarMatriculaPeriodoTurmaDiscipinaPorAlteracaoMatrizCurricular(turmaAtual, td, mp);
						getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().incluir(mptd, turmaNovaGradeCurricular.getGradeCurricularVO(), usuarioVO);
					}
					
				}
				gerarDadosDependenteParaMatriculaPeriodoTurmaDisciplina(listaMatriculaPeriodo, turmaAtual, usuarioVO, td);
			}
		}
	}
	
	private void manterDisciplinaParaGradeCurricularCursoParaAlunosReposicao(TurmaVO turmaAtual, TurmaVO turmaNovaGradeCurricular, TurmaDisciplinaVO td, UsuarioVO usuarioVO) throws Exception {
		List<MatriculaPeriodoTurmaDisciplinaVO> listaMptd = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaMatriculaPeriodoTurmaDisciplinaReposicaoAlunoTurma(turmaAtual.getGradeCurricularVO().getCodigo(), turmaNovaGradeCurricular.getCodigo(), td.getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		for (MatriculaPeriodoTurmaDisciplinaVO mptd : listaMptd) {
			GradeCurricularVO gradeCurricular = geObterMatrizCurricularAtualAluno(turmaNovaGradeCurricular, usuarioVO, mptd);			
			if (Uteis.isAtributoPreenchido(mptd.getMapaEquivalenciaDisciplinaVOIncluir())) {
				realizarOperacoesMantendoDisciplinaParaReposicaoPorMapaEquivalencia(turmaNovaGradeCurricular, td, gradeCurricular, mptd, usuarioVO);
			} else {
				realizarOperacoesMantendoDisciplinaParaReposicaoPorCorrespondencia(turmaNovaGradeCurricular, td, gradeCurricular, mptd, usuarioVO);
			}
		}
	}
	
	private void realizarOperacoesMantendoDisciplinaParaReposicaoPorMapaEquivalencia(TurmaVO turmaNovaGradeCurricular, TurmaDisciplinaVO td,  GradeCurricularVO gradeCurricular, MatriculaPeriodoTurmaDisciplinaVO mptd, UsuarioVO usuarioVO) throws Exception {
		Integer periodoLetivo = geObterPeriodoLetivoOperacaoMatrizCurricular(td, turmaNovaGradeCurricular.getGradeCurricularVO(), usuarioVO) ;
		HistoricoVO hisMatrizCurricular = getFacadeFactory().getHistoricoFacade().consultarHistoricoDoMapaDisciplinaEquivalenteMatrizCurricular(mptd.getCodigo(), usuarioVO);
		if(Uteis.isAtributoPreenchido(hisMatrizCurricular)
				&& turmaNovaGradeCurricular.getCodigo().equals(mptd.getTurma().getCodigo())){
			TurmaDisciplinaVO tdMatrizCurricular = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorMatriculaPeriodoCodigoDisciplina(hisMatrizCurricular.getMatriculaPeriodo().getCodigo(), hisMatrizCurricular.getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			Uteis.checkState(!Uteis.isAtributoPreenchido(tdMatrizCurricular), "Não foi encontado uma Turma Disciplina da Matriz Curricular para a operação de correspondencia  " + td.getDisciplina().getNome() + " da Matrícula Período Turma Disciplina "+mptd.getCodigo()+"  de Reposição.");
			Map<String, Integer> map = getObterMapaEquivalenciaParaOperacaoMatrizCurricular(turmaNovaGradeCurricular, tdMatrizCurricular, td, mptd, gradeCurricular, usuarioVO);
			Integer numeroAgrupamento = getFacadeFactory().getHistoricoFacade().consultarUltimoNumeroAgrupamentoEquivalenciaDisciplina(mptd.getMatricula(), map.get("mapaequivalenciadisciplina")) + 1;
			hisMatrizCurricular.setNumeroAgrupamentoEquivalenciaDisciplina(numeroAgrupamento);
			getFacadeFactory().getHistoricoFacade().alterarHistoricoPorAlteracaoGradeCurricularCursoIntegralComReposicao(true, hisMatrizCurricular.getCodigo(), null, null,null, null, null, null, map.get("mapaequivalenciadisciplina"), null, map.get("mapaequivalenciadisciplinamatrizcurricular"), hisMatrizCurricular.getNumeroAgrupamentoEquivalenciaDisciplina(), false, usuarioVO);			
			getFacadeFactory().getHistoricoFacade().alterarHistoricoPorAlteracaoGradeCurricularCursoIntegralComReposicao(false, mptd.getCodigo(),  null, null, periodoLetivo, null, td.getGradeDisciplinaVO(), td.getGradeCurricularGrupoOptativaDisciplinaVO(), map.get("mapaequivalenciadisciplina"), map.get("mapaequivalenciadisciplinacursada"), null, hisMatrizCurricular.getNumeroAgrupamentoEquivalenciaDisciplina(), true, usuarioVO);
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarMatriculaPeriodoTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegralComReposicao(mptd.getCodigo(), null, td.getGradeDisciplinaVO().getCodigo(), td.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), map.get("mapaequivalenciadisciplina"), map.get("mapaequivalenciadisciplinacursada"), true, usuarioVO);
		}else if(Uteis.isAtributoPreenchido(hisMatrizCurricular)
				&& !turmaNovaGradeCurricular.getCodigo().equals(mptd.getTurma().getCodigo())){			
			TurmaDisciplinaVO tdTurmaReposicao = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorTurmaCodigoDisciplinaComGradeDiscilina(mptd.getTurma().getCodigo(), mptd.getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			Uteis.checkState(!Uteis.isAtributoPreenchido(tdTurmaReposicao), "Não foi encontado uma Turma Disciplina da Turma Reposição para a operação de correspondencia   " + td.getDisciplina().getNome() + " da Matrícula Período Turma Disciplina "+mptd.getCodigo()+"  de Reposição.");
			Map<String, Integer> map = getObterMapaEquivalenciaParaOperacaoMatrizCurricular(turmaNovaGradeCurricular, td, tdTurmaReposicao, mptd, gradeCurricular, usuarioVO);
			Integer numeroAgrupamento = getFacadeFactory().getHistoricoFacade().consultarUltimoNumeroAgrupamentoEquivalenciaDisciplina(mptd.getMatricula(), map.get("mapaequivalenciadisciplina")) + 1;
			hisMatrizCurricular.setNumeroAgrupamentoEquivalenciaDisciplina(numeroAgrupamento);
			getFacadeFactory().getHistoricoFacade().alterarHistoricoPorAlteracaoGradeCurricularCursoIntegralComReposicao(true, hisMatrizCurricular.getCodigo(), gradeCurricular.getCodigo(), periodoLetivo, periodoLetivo, null, td.getGradeDisciplinaVO(), td.getGradeCurricularGrupoOptativaDisciplinaVO(), map.get("mapaequivalenciadisciplina"), null, map.get("mapaequivalenciadisciplinamatrizcurricular"), hisMatrizCurricular.getNumeroAgrupamentoEquivalenciaDisciplina(), false, usuarioVO);
			getFacadeFactory().getHistoricoFacade().alterarHistoricoPorAlteracaoGradeCurricularCursoIntegralComReposicao(false, mptd.getCodigo(), gradeCurricular.getCodigo(), periodoLetivo, null, null, null, null, map.get("mapaequivalenciadisciplina"), map.get("mapaequivalenciadisciplinacursada"), null, hisMatrizCurricular.getNumeroAgrupamentoEquivalenciaDisciplina(), true, usuarioVO);
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarMatriculaPeriodoTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegralComReposicao(mptd.getCodigo(), null, null, null, map.get("mapaequivalenciadisciplina"), map.get("mapaequivalenciadisciplinacursada"), true, usuarioVO);
		}else if(!Uteis.isAtributoPreenchido(hisMatrizCurricular)){
			throw new StreamSeiException("Não foi encontado um Histórico da Matriz Curricular para a operação de correspondência " + td.getDisciplina().getNome() + " da Matrícula Período Turma disciplina "+mptd.getCodigo()+" de Reposição.");	
		}	
	}
	
	private void realizarOperacoesMantendoDisciplinaParaReposicaoPorCorrespondencia(TurmaVO turmaNovaGradeCurricular, TurmaDisciplinaVO td, GradeCurricularVO gradeCurricular, MatriculaPeriodoTurmaDisciplinaVO mptd, UsuarioVO usuarioVO) throws Exception {
		if(!turmaNovaGradeCurricular.getCodigo().equals(mptd.getTurma().getCodigo())){
			Integer periodoLetivo = geObterPeriodoLetivoOperacaoMatrizCurricular(td, turmaNovaGradeCurricular.getGradeCurricularVO(), usuarioVO) ;
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarMatriculaPeriodoTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegralComReposicao(mptd.getCodigo(), null, td.getGradeDisciplinaVO().getCodigo(), td.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), null, null, true, usuarioVO);
			getFacadeFactory().getHistoricoFacade().alterarHistoricoPorAlteracaoGradeCurricularCursoIntegralComReposicao(false, mptd.getCodigo(), gradeCurricular.getCodigo(), null, periodoLetivo, null, td.getGradeDisciplinaVO(), td.getGradeCurricularGrupoOptativaDisciplinaVO(), null, null, null, null, true, usuarioVO);
		}
	}	
	

	private void gerarDadosDependenteParaMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoVO> listaMatriculaPeriodo, TurmaVO turmaAtual, UsuarioVO usuarioVO, TurmaDisciplinaVO td) throws Exception {
		td.setTurma(turmaAtual.getCodigo());
		td.setNrMaximoMatricula(turmaAtual.getNrMaximoMatricula());
		getFacadeFactory().getTurmaDisciplinaFacade().incluir(td, usuarioVO);
//		VagaTurmaVO vagaTurma = getFacadeFactory().getVagaTurmaFacade().consultaRapidaPorTurma(turmaAtual.getCodigo(), usuarioVO);
		VagaTurmaDisciplinaVO vtd = new VagaTurmaDisciplinaVO();
		vtd.setDisciplina(td.getDisciplina());
		vtd.setNrMaximoMatricula(listaMatriculaPeriodo.size());
		vtd.setNrVagasMatricula(vtd.getNrMaximoMatricula());
		vtd.setNrVagasMatriculaReposicao(0);
//		if (Uteis.isAtributoPreenchido(vagaTurma.getCodigo())) {
//			vtd.setVagaTurma(vagaTurma.getCodigo());
//			getFacadeFactory().getVagaTurmaDisciplinaFacade().incluir(vtd);
//		} else {
//			vagaTurma.setTurmaVO(turmaAtual);
//			vagaTurma.getVagaTurmaDisciplinaVOs().add(vtd);
//			getFacadeFactory().getVagaTurmaFacade().persistir(vagaTurma, usuarioVO);
//		}
	}

	private void verificarOperacoesAlteracaoGradeCurricularCursoIntegralParaTurmaBase(List<MatriculaPeriodoVO> listaMatriculaPeriodo, TurmaVO turmaAtual, TurmaVO turmaNovaGradeCurricular, List<TurmaDisciplinaVO> listaAtualGradeCurricular,  UsuarioVO usuarioVO) throws Exception {		
		for (TurmaDisciplinaVO td : listaAtualGradeCurricular) {
			if (td.getOperacaoMatrizCurricular().equals(-2)) {// -2 excluir
//				getFacadeFactory().getVagaTurmaDisciplinaFacade().excluirPorCodigoDisciplinaTurma(turmaAtual.getCodigo(), td.getDisciplina().getCodigo());
//				getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().excluirPorCodigoDisciplinaTurma(turmaAtual.getCodigo(), td.getDisciplina().getCodigo(), "", "", usuarioVO);
//				getFacadeFactory().getHorarioTurmaFacade().excluirHorarioTurmaPorAlteracaoGradeCurricularCursoIntegral(turmaAtual, td.getDisciplina(), usuarioVO);
				getFacadeFactory().getTurmaDisciplinaFacade().excluirPorCodigoDisciplinaTurma(turmaAtual.getCodigo(), td.getDisciplina().getCodigo(), usuarioVO);
				if (Uteis.isAtributoPreenchido(listaMatriculaPeriodo)) {
					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().excluirMatriculaPeriodoTurmaDisciplinasPorListaMatriculaPeriodo(listaMatriculaPeriodo, td.getDisciplina().getCodigo(), turmaAtual.getCodigo(), turmaAtual.getGradeCurricularVO().getCodigo(), usuarioVO);
				}
			} else if (td.getOperacaoMatrizCurricular().equals(-1)) {// -1 Fora Grade
				if (Uteis.isAtributoPreenchido(listaMatriculaPeriodo)) {
					getFacadeFactory().getHistoricoFacade().alterarHistoricoForaDaGradePorAlteracaoGradeCurricularCursoIntegral(listaMatriculaPeriodo, turmaNovaGradeCurricular, td.getDisciplina().getCodigo(), turmaAtual.getGradeCurricularVO().getCodigo(), true, usuarioVO);
					getFacadeFactory().getTurmaDisciplinaFacade().excluirPorCodigoDisciplinaTurma(turmaAtual.getCodigo(), td.getDisciplina().getCodigo(), usuarioVO);
				}
			} else {// -disciplina correspondente
//				getFacadeFactory().getVagaTurmaDisciplinaFacade().alterarVagaTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegral(turmaAtual.getCodigo(), td.getDisciplina().getCodigo(), td.getOperacaoMatrizCurricular(), usuarioVO);
//				getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().alterarProfessorTitularDisciplinaTurmaPorAlteracaoGradeCurricularCursoIntegral(turmaAtual.getCodigo(), td.getDisciplina().getCodigo(), "", "", td.getOperacaoMatrizCurricular(), usuarioVO);
//				getFacadeFactory().getHorarioTurmaFacade().alterarHorarioTurmaPorAlteracaoGradeCurricularCursoIntegral(turmaAtual, td, usuarioVO);
				getFacadeFactory().getTurmaDisciplinaFacade().excluirPorCodigoDisciplinaTurma(turmaAtual.getCodigo(), td.getDisciplina().getCodigo(), usuarioVO);
				TurmaDisciplinaVO tdCorrespodente = geObterTurmaDisciplinaAlteracaoGradeCurricular(turmaNovaGradeCurricular, td.getOperacaoMatrizCurricular());
				tdCorrespodente.setTurma(turmaAtual.getCodigo());
				tdCorrespodente.setNrMaximoMatricula(turmaAtual.getNrMaximoMatricula());
				getFacadeFactory().getTurmaDisciplinaFacade().incluir(tdCorrespodente, usuarioVO);
				if (Uteis.isAtributoPreenchido(listaMatriculaPeriodo)) {
					getFacadeFactory().getHistoricoFacade().alterarHistoricoPorAlteracaoGradeCurricularCursoIntegral(turmaNovaGradeCurricular, tdCorrespodente.getDisciplina(), td.getDisciplina().getCodigo(), tdCorrespodente.getGradeDisciplinaVO(), tdCorrespodente.getGradeCurricularGrupoOptativaDisciplinaVO(), listaMatriculaPeriodo, usuarioVO);
					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarMatriculaPeriodoTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegral(turmaNovaGradeCurricular.getCodigo(), tdCorrespodente.getDisciplina().getCodigo(), td.getDisciplina().getCodigo(), tdCorrespodente.getGradeDisciplinaVO().getCodigo(), tdCorrespodente.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), listaMatriculaPeriodo, usuarioVO);
				}				
			}
		}
	}

	private void verificarOperacoesAlteracaoGradeCurricularCursoIntegralParaReposicao(TurmaVO turmaAtual, TurmaVO turmaNovaGradeCurricular, List<TurmaDisciplinaVO> listaAtualGradeCurricular, UsuarioVO usuarioVO) throws Exception {
		
		//Primeiro deve ser realizado as trocas de reposicao e so depois ser excluidas as que ficaram
		listaAtualGradeCurricular.stream()
		.filter(td->td.getQtdAlunosReposicao() > 0 && td.getOperacaoMatrizCurricularReposicao().equals(-1))
		.forEach(td->{
			try {
				List<MatriculaPeriodoTurmaDisciplinaVO> listaMptd = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaMatriculaPeriodoTurmaDisciplinaReposicaoAlunoTurma(turmaAtual.getGradeCurricularVO().getCodigo(), turmaNovaGradeCurricular.getCodigo(), td.getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
				realizarTrocaDisciplinaParaGradeCurricularCursoParaAlunosReposicao(listaMptd, turmaNovaGradeCurricular, td, usuarioVO);
			} catch (Exception e) {
				throw new StreamSeiException(e); 
			}
		});
		listaAtualGradeCurricular.stream()
		.filter(td->td.getQtdAlunosReposicao() > 0 && td.getOperacaoMatrizCurricularReposicao().equals(-2))
		.forEach(td->{
			try {
				List<MatriculaPeriodoTurmaDisciplinaVO> listaMptd = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaMatriculaPeriodoTurmaDisciplinaReposicaoAlunoTurma(turmaAtual.getGradeCurricularVO().getCodigo(),  turmaNovaGradeCurricular.getCodigo(), td.getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
				if (Uteis.isAtributoPreenchido(listaMptd)) {
					for (MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO : listaMptd) {
						if (matriculaPeriodoTurmaDisciplinaVO.getDisciplinaEquivale() || matriculaPeriodoTurmaDisciplinaVO.getDisciplinaPorEquivalencia()) {
							HistoricoVO obj = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoTurmaDisciplinaHistoricoEquivalente(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), false, false, usuarioVO);
							getFacadeFactory().getHistoricoFacade().excluirHistoricoForaPrazoVerificandoHistoricoPorEquivalencia(obj,  usuarioVO);
						} else {
							getFacadeFactory().getHistoricoFacade().excluirPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), usuarioVO);
						}
					}
					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().excluirMatriculaPeriodoTurmaDisciplinas(listaMptd, usuarioVO);
				}
			} catch (Exception e) {
				throw new StreamSeiException(e); 
			}
		});
	}	
	
	private void realizarTrocaDisciplinaParaGradeCurricularCursoParaAlunosReposicao(List<MatriculaPeriodoTurmaDisciplinaVO> listaMptd, TurmaVO turmaNovaGradeCurricular, TurmaDisciplinaVO td, UsuarioVO usuarioVO) throws Exception {
				for (MatriculaPeriodoTurmaDisciplinaVO mptd : listaMptd) {
					TurmaDisciplinaVO tdCorrespodente = geObterTurmaDisciplinaAlteracaoGradeCurricular(turmaNovaGradeCurricular, td.getOperacaoMatrizCurricular());
			GradeCurricularVO gradeCurricular = geObterMatrizCurricularAtualAluno(turmaNovaGradeCurricular, usuarioVO, mptd);			
					if (Uteis.isAtributoPreenchido(mptd.getMapaEquivalenciaDisciplinaVOIncluir())) {
				realizarOperacoesParaReposicaoPorMapaEquivalencia(turmaNovaGradeCurricular, td, tdCorrespodente, gradeCurricular, mptd, usuarioVO);
					} else {
				realizarOperacoesParaReposicaoPerdendoCorrespondencia(turmaNovaGradeCurricular, td,tdCorrespodente, gradeCurricular, mptd, usuarioVO);
					}
				}
			}

	private void realizarOperacoesParaReposicaoPerdendoCorrespondencia(TurmaVO turmaNovaGradeCurricular, TurmaDisciplinaVO td, TurmaDisciplinaVO tdCorrespodente, GradeCurricularVO gradeCurricular, MatriculaPeriodoTurmaDisciplinaVO mptd, UsuarioVO usuarioVO) throws Exception {
		Map<String, Integer> map = null;
		if (turmaNovaGradeCurricular.getCodigo().equals(mptd.getTurma().getCodigo())) {
			map = getObterMapaEquivalenciaParaOperacaoMatrizCurricular(turmaNovaGradeCurricular, td, tdCorrespodente, mptd, gradeCurricular, usuarioVO);	
		}	else{
			map = getObterMapaEquivalenciaParaOperacaoMatrizCurricular(turmaNovaGradeCurricular, tdCorrespodente, td, mptd, gradeCurricular, usuarioVO);
	}
		TurmaDisciplinaVO tdReposicao = turmaNovaGradeCurricular.getCodigo().equals(mptd.getTurma().getCodigo()) ? tdCorrespodente : td;
		Integer periodoLetivo = geObterPeriodoLetivoOperacaoMatrizCurricular(tdReposicao, turmaNovaGradeCurricular.getGradeCurricularVO(), usuarioVO);
		Integer numeroAgrupamento = getFacadeFactory().getHistoricoFacade().consultarUltimoNumeroAgrupamentoEquivalenciaDisciplina(mptd.getMatricula(), map.get("mapaequivalenciadisciplina")) + 1;
		HistoricoVO his = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaPeriodoTurmaDisciplina(mptd.getCodigo(), false, false, usuarioVO);		
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarMatriculaPeriodoTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegralComReposicao(mptd.getCodigo(), tdReposicao.getDisciplina().getCodigo(), tdReposicao.getGradeDisciplinaVO().getCodigo(), tdReposicao.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), map.get("mapaequivalenciadisciplina"), map.get("mapaequivalenciadisciplinacursada"), true, usuarioVO);
		getFacadeFactory().getHistoricoFacade().alterarHistoricoPorAlteracaoGradeCurricularCursoIntegralComReposicao(false, mptd.getCodigo(), gradeCurricular.getCodigo(), null, periodoLetivo, tdReposicao.getDisciplina(), tdReposicao.getGradeDisciplinaVO(), tdReposicao.getGradeCurricularGrupoOptativaDisciplinaVO(), map.get("mapaequivalenciadisciplina"), map.get("mapaequivalenciadisciplinacursada"), null, numeroAgrupamento, true, usuarioVO);
		his.setCodigo(0);
		his.setMatriculaPeriodoTurmaDisciplina(new MatriculaPeriodoTurmaDisciplinaVO());
		his.getMapaEquivalenciaDisciplina().setCodigo(map.get("mapaequivalenciadisciplina"));
		his.getMapaEquivalenciaDisciplinaMatrizCurricular().setCodigo(map.get("mapaequivalenciadisciplinamatrizcurricular"));
		his.setNumeroAgrupamentoEquivalenciaDisciplina(numeroAgrupamento);
		his.setHistoricoPorEquivalencia(true);
		his.setHistoricoEquivalente(false);
		if (!turmaNovaGradeCurricular.getCodigo().equals(mptd.getTurma().getCodigo())) {
			his.setMatrizCurricular(gradeCurricular);
			his.getPeriodoLetivoMatrizCurricular().setCodigo(geObterPeriodoLetivoOperacaoMatrizCurricular(tdCorrespodente, gradeCurricular, usuarioVO));
			his.setDisciplina(tdCorrespodente.getDisciplina());
			his.setNomeDisciplina(tdCorrespodente.getDisciplina().getNome());
			his.setGradeDisciplinaVO(tdCorrespodente.getGradeDisciplinaVO());
			his.setGradeCurricularGrupoOptativaDisciplinaVO(tdCorrespodente.getGradeCurricularGrupoOptativaDisciplinaVO());
			his.setDisciplinaReferenteAUmGrupoOptativa(tdCorrespodente.getDisciplinaReferenteAUmGrupoOptativa());
			his.setCargaHorariaDisciplina(tdCorrespodente.getDisciplinaReferenteAUmGrupoOptativa() ? tdCorrespodente.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria() : tdCorrespodente.getGradeDisciplinaVO().getCargaHoraria());
			his.setCreditoDisciplina(tdCorrespodente.getDisciplinaReferenteAUmGrupoOptativa() ? tdCorrespodente.getGradeCurricularGrupoOptativaDisciplinaVO().getNrCreditos() : tdCorrespodente.getGradeDisciplinaVO().getNrCreditos());
			
		}
		SituacaoHistorico situacao = SituacaoHistorico.getEnum(his.getSituacao());
		if (situacao.isAprovada() || situacao.isAprovadaAproveitamento()) {
			his.setSituacao(SituacaoHistorico.APROVADO_POR_EQUIVALENCIA.getValor());
		} else if (situacao.isCursando()) {
			his.setSituacao(SituacaoHistorico.CURSANDO_POR_EQUIVALENCIA.getValor());
		}
		getFacadeFactory().getHistoricoFacade().incluir(his, usuarioVO);
	}	

	private void realizarOperacoesParaReposicaoPorMapaEquivalencia(TurmaVO turmaNovaGradeCurricular, TurmaDisciplinaVO td,  TurmaDisciplinaVO tdCorrespodente,  GradeCurricularVO gradeCurricular, MatriculaPeriodoTurmaDisciplinaVO mptd, UsuarioVO usuarioVO) throws Exception {
		Integer periodoLetivo = geObterPeriodoLetivoOperacaoMatrizCurricular(tdCorrespodente, turmaNovaGradeCurricular.getGradeCurricularVO(), usuarioVO);
		Integer cargaHoraria = getObterCargaHorarioAlteracaoMatrizCurricular(turmaNovaGradeCurricular, td, tdCorrespodente, mptd, usuarioVO);
		HistoricoVO hisMatrizCurricular = getFacadeFactory().getHistoricoFacade().consultarHistoricoDoMapaDisciplinaEquivalenteMatrizCurricular(mptd.getCodigo(), usuarioVO);
		if(Uteis.isAtributoPreenchido(hisMatrizCurricular)
				&& hisMatrizCurricular.getCargaHorariaDisciplina().equals(cargaHoraria)
				&& hisMatrizCurricular.getDisciplina().getCodigo().equals(tdCorrespodente.getDisciplina().getCodigo())
				&& turmaNovaGradeCurricular.getCodigo().equals(mptd.getTurma().getCodigo())){
			getFacadeFactory().getHistoricoFacade().excluir(hisMatrizCurricular, false, usuarioVO);
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarMatriculaPeriodoTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegralComReposicao(mptd.getCodigo(), tdCorrespodente.getDisciplina().getCodigo(), tdCorrespodente.getGradeDisciplinaVO().getCodigo(), tdCorrespodente.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), null, null, false, usuarioVO);
			getFacadeFactory().getHistoricoFacade().alterarHistoricoPorAlteracaoGradeCurricularCursoIntegralComReposicao(false, mptd.getCodigo(),  gradeCurricular.getCodigo(), null, periodoLetivo, tdCorrespodente.getDisciplina(), tdCorrespodente.getGradeDisciplinaVO(), tdCorrespodente.getGradeCurricularGrupoOptativaDisciplinaVO(), null, null, null, null, false, usuarioVO);
		}else if(Uteis.isAtributoPreenchido(hisMatrizCurricular)				
				&& hisMatrizCurricular.getDisciplina().getCodigo().equals(td.getDisciplina().getCodigo())
				&& mptd.getDisciplina().getCodigo().equals(tdCorrespodente.getDisciplina().getCodigo())
				&& hisMatrizCurricular.getCargaHorariaDisciplina().equals(cargaHoraria)
				&& !turmaNovaGradeCurricular.getCodigo().equals(mptd.getTurma().getCodigo())){
			getFacadeFactory().getHistoricoFacade().excluir(hisMatrizCurricular, false, usuarioVO);
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarMatriculaPeriodoTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegralComReposicao(mptd.getCodigo(), null, null, null, null, null, false, usuarioVO);
			getFacadeFactory().getHistoricoFacade().alterarHistoricoPorAlteracaoGradeCurricularCursoIntegralComReposicao(false, mptd.getCodigo(),  gradeCurricular.getCodigo(), null, periodoLetivo, null, null, null, null, null, null, null, false, usuarioVO);
		}else if(Uteis.isAtributoPreenchido(hisMatrizCurricular)
				&& turmaNovaGradeCurricular.getCodigo().equals(mptd.getTurma().getCodigo())){ 			
			TurmaDisciplinaVO tdMatrizCurricular = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorMatriculaPeriodoCodigoDisciplina(hisMatrizCurricular.getMatriculaPeriodo().getCodigo(), hisMatrizCurricular.getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			Uteis.checkState(!Uteis.isAtributoPreenchido(tdMatrizCurricular), "Não foi encontado uma Turma Disciplina da Matriz Curricular para a operação de correspondencia  " + td.getDisciplina().getNome() + " da Matrícula Período Turma Disciplina "+mptd.getCodigo()+"  de Reposição.");
			Map<String, Integer> map = getObterMapaEquivalenciaParaOperacaoMatrizCurricular(turmaNovaGradeCurricular, tdMatrizCurricular, tdCorrespodente, mptd, gradeCurricular, usuarioVO);
			Integer numeroAgrupamento = getFacadeFactory().getHistoricoFacade().consultarUltimoNumeroAgrupamentoEquivalenciaDisciplina(mptd.getMatricula(), map.get("mapaequivalenciadisciplina")) + 1;
			hisMatrizCurricular.setNumeroAgrupamentoEquivalenciaDisciplina(numeroAgrupamento);
			getFacadeFactory().getHistoricoFacade().alterarHistoricoPorAlteracaoGradeCurricularCursoIntegralComReposicao(true, hisMatrizCurricular.getCodigo(), null, null,null, null, null, null, map.get("mapaequivalenciadisciplina"), null, map.get("mapaequivalenciadisciplinamatrizcurricular"), hisMatrizCurricular.getNumeroAgrupamentoEquivalenciaDisciplina(), false, usuarioVO);
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarMatriculaPeriodoTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegralComReposicao(mptd.getCodigo(), tdCorrespodente.getDisciplina().getCodigo(), tdCorrespodente.getGradeDisciplinaVO().getCodigo(), tdCorrespodente.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), map.get("mapaequivalenciadisciplina"), map.get("mapaequivalenciadisciplinacursada"), true, usuarioVO);
			getFacadeFactory().getHistoricoFacade().alterarHistoricoPorAlteracaoGradeCurricularCursoIntegralComReposicao(false, mptd.getCodigo(),  gradeCurricular.getCodigo(), null, periodoLetivo, tdCorrespodente.getDisciplina(), tdCorrespodente.getGradeDisciplinaVO(), tdCorrespodente.getGradeCurricularGrupoOptativaDisciplinaVO(), map.get("mapaequivalenciadisciplina"), map.get("mapaequivalenciadisciplinacursada"), null,hisMatrizCurricular.getNumeroAgrupamentoEquivalenciaDisciplina(), true, usuarioVO);
		}else if(Uteis.isAtributoPreenchido(hisMatrizCurricular)
				&& !turmaNovaGradeCurricular.getCodigo().equals(mptd.getTurma().getCodigo())){
			TurmaDisciplinaVO tdTurmaReposicao = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorTurmaCodigoDisciplinaComGradeDiscilina(mptd.getTurma().getCodigo(), mptd.getDisciplina().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			Uteis.checkState(!Uteis.isAtributoPreenchido(tdTurmaReposicao), "Não foi encontado uma Turma Disciplina da Turma Reposição para a operação de correspondencia   " + td.getDisciplina().getNome() + " da Matrícula Período Turma Disciplina "+mptd.getCodigo()+"  de Reposição.");
			Map<String, Integer> map = getObterMapaEquivalenciaParaOperacaoMatrizCurricular(turmaNovaGradeCurricular, tdCorrespodente, tdTurmaReposicao, mptd, gradeCurricular, usuarioVO);
			Integer numeroAgrupamento = getFacadeFactory().getHistoricoFacade().consultarUltimoNumeroAgrupamentoEquivalenciaDisciplina(mptd.getMatricula(), map.get("mapaequivalenciadisciplina")) + 1;
			hisMatrizCurricular.setNumeroAgrupamentoEquivalenciaDisciplina(numeroAgrupamento);
			getFacadeFactory().getHistoricoFacade().alterarHistoricoPorAlteracaoGradeCurricularCursoIntegralComReposicao(true, hisMatrizCurricular.getCodigo(), gradeCurricular.getCodigo(), null, periodoLetivo, tdCorrespodente.getDisciplina(), tdCorrespodente.getGradeDisciplinaVO(), tdCorrespodente.getGradeCurricularGrupoOptativaDisciplinaVO(), map.get("mapaequivalenciadisciplina"), null, map.get("mapaequivalenciadisciplinamatrizcurricular"), hisMatrizCurricular.getNumeroAgrupamentoEquivalenciaDisciplina(), false, usuarioVO);
			getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().alterarMatriculaPeriodoTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegralComReposicao(mptd.getCodigo(), null, null, null, map.get("mapaequivalenciadisciplina"), map.get("mapaequivalenciadisciplinacursada"), true, usuarioVO);
			getFacadeFactory().getHistoricoFacade().alterarHistoricoPorAlteracaoGradeCurricularCursoIntegralComReposicao(false, mptd.getCodigo(),  gradeCurricular.getCodigo(), null, null, null, null, null, map.get("mapaequivalenciadisciplina"), map.get("mapaequivalenciadisciplinacursada"), null, hisMatrizCurricular.getNumeroAgrupamentoEquivalenciaDisciplina(), true, usuarioVO);			
		}else{
			throw new StreamSeiException("Não foi encontado um Histórico da Matriz Curricular para a operação de correspondência " + td.getDisciplina().getNome() + " da Matrícula Período Turma Disciplina "+mptd.getCodigo()+"  de Reposição.");
		}
	}

	private Integer getObterCargaHorarioAlteracaoMatrizCurricular(TurmaVO turmaNovaGradeCurricular, TurmaDisciplinaVO td, TurmaDisciplinaVO tdCorrespodente ,MatriculaPeriodoTurmaDisciplinaVO mptd, UsuarioVO usuarioVO) throws Exception {
		TurmaDisciplinaVO tdReposicao = tdCorrespodente;
		if(!turmaNovaGradeCurricular.getCodigo().equals(mptd.getTurma().getCodigo())){
			tdReposicao = td;
		}
		return tdReposicao.getDisciplinaReferenteAUmGrupoOptativa() ? tdReposicao.getGradeCurricularGrupoOptativaDisciplinaVO().getCargaHoraria() : tdReposicao.getGradeDisciplinaVO().getCargaHoraria();
	}

	private Map<String, Integer> getObterMapaEquivalenciaParaOperacaoMatrizCurricular(TurmaVO turmaNovaGradeCurricular, TurmaDisciplinaVO td, TurmaDisciplinaVO tdCorrespodente, MatriculaPeriodoTurmaDisciplinaVO mptd, GradeCurricularVO gc, UsuarioVO usuarioVO) throws Exception {
		Map<String, Integer> map = getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().validarSeExisteEquivalenciaParaReposicaoComTrocaAlteracaoGradeCurricular(gc.getCodigo(), td.getDisciplina().getCodigo(), tdCorrespodente.getDisciplina().getCodigo());
		if (!map.containsKey("mapaequivalenciadisciplina") || (map.containsKey("mapaequivalenciadisciplina") && !Uteis.isAtributoPreenchido(map.get("mapaequivalenciadisciplina")))) {
			getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().gerarNovoMapaEquivalenciaMatrizCurricularPorAtualizacaoGradeCurricularCursoIntegral(map, turmaNovaGradeCurricular.getCurso(), gc, td, tdCorrespodente, usuarioVO);
		}
		Uteis.checkState(!Uteis.isAtributoPreenchido(map.get("mapaequivalenciadisciplina")), "Não foi encontrado um mapa de equivalência para matriz curricular "+gc.getNome());
		return map;
	}

	public TurmaDisciplinaVO geObterTurmaDisciplinaAlteracaoGradeCurricular(TurmaVO turmaNovaGradeCurricular, Integer operacaoMatrizCurricular) {
		Optional<TurmaDisciplinaVO> tdCorrespodente = turmaNovaGradeCurricular.getTurmaDisciplinaVOs().stream().filter(p -> p.getOperacaoMatrizCurricular().equals(operacaoMatrizCurricular)).findFirst();
		if (!tdCorrespodente.isPresent() || !Uteis.isAtributoPreenchido(tdCorrespodente.get().getDisciplina().getCodigo())) {
			throw new StreamSeiException("Não foi encontrado a disciplina de correspondência de código " + operacaoMatrizCurricular + " na nova matriz curricular.");
		}
		return tdCorrespodente.get();
	}
	
	private Integer geObterPeriodoLetivoOperacaoMatrizCurricular(TurmaDisciplinaVO tdReposicao, GradeCurricularVO gc, UsuarioVO usuarioVO) throws Exception {
		Integer pl = tdReposicao.getGradeDisciplinaVO().getPeriodoLetivoVO().getCodigo();
		if(tdReposicao.getDisciplinaReferenteAUmGrupoOptativa()){
			pl =getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPrimeiroPeriodoLetivoComDisciplinaGrupoOptativaMatrizCurricular(tdReposicao.getDisciplina().getCodigo(), gc.getCodigo(), 0, usuarioVO);				
			}
		Uteis.checkState(!Uteis.isAtributoPreenchido(pl), "Não foi encontrado um período letivo para a disciplina de correspondência de código " + tdReposicao.getDisciplina().getCodigo());
		return pl;
	}
	
	private GradeCurricularVO geObterMatrizCurricularAtualAluno(TurmaVO turmaNovaGradeCurricular, UsuarioVO usuarioVO, MatriculaPeriodoTurmaDisciplinaVO mptd) throws Exception {
		GradeCurricularVO gradeCurricular = turmaNovaGradeCurricular.getGradeCurricularVO();
		if (turmaNovaGradeCurricular.getCodigo().equals(mptd.getTurma().getCodigo())) {
			gradeCurricular = getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularAtualMatricula(mptd.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}
		Uteis.checkState(!Uteis.isAtributoPreenchido(gradeCurricular), "Não foi encontrado a Matriz Curricular Atual para o aluno de matrícula " + mptd.getMatricula());
		return gradeCurricular;
	}

	/**
	 * responsável por realizar uma consulta de <code>Turma</code> através do valor do atributo <code>nome</code> da classe <code>Turno</code> Faz uso da Operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vÃ¯Â¿Â½rios objetos da classe <code>TurmaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restriÃ§Ã£oo de acesso.
	 */
	public List<TurmaVO> consultarPorNomeTurno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			StringBuilder sql = getSQLPadraoConsultaBasica();
			sql.append(" WHERE upper(sem_acentos(turno.nome)) ilike(sem_acentos('");
			sql.append(valorConsulta.toUpperCase());
			sql.append("%')");
			sql.append(" )");
			if (unidadeEnsino.intValue() != 0) {
				sql.append(" AND (turma.unidadeensino = ").append(unidadeEnsino.intValue()).append(") ");
			}
			sql.append(" ORDER BY turno.nome ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			return montarDadosConsultaBasica(tabelaResultado);
		} else {
			String sqlStr = "SELECT Turma.* FROM Turma, Turno WHERE Turma.turno = Turno.codigo and lower (Turno.nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY Turno.nome";
			if (unidadeEnsino.intValue() != 0) {
				sqlStr = "SELECT Turma.* FROM Turma, Turno WHERE Turma.turno = Turno.codigo and lower (Turno.nome) like('" + valorConsulta.toLowerCase() + "%') and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY Turno.nome";
			}

			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
			return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		}
	}

	public List<TurmaVO> consultaRapidaPorTurno(String valorConsulta, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorTurno(valorConsulta, 0, unidadeEnsino, nivelEducacionalPosGraduacao, nivelEducacionalDiferentePosGraduacao, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultaRapidaPorTurno(String valorConsulta, Integer turmaOrigem, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE (sem_acentos(turno.nome)) ilike (sem_acentos(?)) ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX', 'MT'))");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND curso.nivelEducacional != 'PO'");
		}
		if (turmaOrigem.intValue() != 0) {
			sql.append(" and turma.codigo <> ");
			sql.append(turmaOrigem.intValue());
		}
		sql.append(" ORDER BY turno.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);

	}

	public List<TurmaVO> consultaRapidaPorTurnoPorCodigoUnidadeEnsinoTurmaPrincipalECodigoCursoTurmaPrincipal(String valorConsulta, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE lower(sem_acentos(turno.nome)) ilike (sem_acentos('");
		sql.append(valorConsulta.toLowerCase());
		sql.append("%'))");
		if (curso.intValue() != 0) {
			sql.append(" and turma.curso = ");
			sql.append(curso.intValue());
		}
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		sql.append(" ORDER BY turno.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);

	}

	public List<TurmaVO> consultaRapidaPorTurnoNivelEducacional(String valorConsulta, Integer unidadeEnsino, String nivelEducacional, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE lower(sem_acentos(turno.nome)) ilike (sem_acentos('");
		sql.append(valorConsulta.toLowerCase());
		sql.append("%'))");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		if (!nivelEducacional.equals("")) {
			sql.append(" AND curso.nivelEducacional in ('").append(nivelEducacional.toUpperCase()).append("') ");
		}
		sql.append(" ORDER BY turno.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);

	}

	public List<TurmaVO> consultaRapidaPorSituacaoTurma(String valorConsulta, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorSituacaoTurma(valorConsulta, 0, unidadeEnsino, nivelEducacionalPosGraduacao, nivelEducacionalDiferentePosGraduacao, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultaRapidaPorSituacaoTurma(String valorConsulta, Integer turmaOrigem, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE turma.situacao = ('");
		sql.append(valorConsulta);
		sql.append("')");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND curso.nivelEducacional = 'PO'");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND curso.nivelEducacional != 'PO'");
		}
		if (turmaOrigem.intValue() != 0) {
			sql.append(" and turma.codigo <> ");
			sql.append(turmaOrigem.intValue());
		}
		sql.append(" ORDER BY turno.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorTurnoCurso(String valorConsulta, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE lower(turno.nome) like ('");
		sql.append(valorConsulta.toLowerCase());
		sql.append("%')");
		if (curso != 0) {
			sql.append(" AND (turma.curso = ").append(curso).append(" OR turma.curso is null) ");
		}
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		sql.append(" ORDER BY turno.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorTurnoCurso(String valorConsulta, List<CursoVO> cursos, List<UnidadeEnsinoVO> unidades, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE lower(turno.nome) like (?) ");
		if (Uteis.isAtributoPreenchido(cursos)) {
			sql.append(" AND curso.codigo  IN (");
			int x = 0;
			for (CursoVO cursoVO : cursos) {
				if (x > 0) {
					sql.append(", ");
				}
				sql.append(cursoVO.getCodigo());
				x++;
			}
			sql.append(" ) ");
		}
		if (Uteis.isAtributoPreenchido(unidades)) {
			sql.append(" AND unidadeensino.codigo  IN (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidades) {
				if (x > 0) {
					sql.append(", ");
				}
				sql.append(unidadeEnsinoVO.getCodigo());
				x++;
			}
			sql.append(" ) ");
		}
		sql.append(" ORDER BY turno.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta.toLowerCase() + "%");
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorCodigoCursoTurno(Integer turma, Integer curso, Integer turno, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE turma.codigo = ").append(turma);
		if (curso != 0) {
			sql.append(" AND turma.curso = ").append(curso);
		}
		if (turno != 0) {
			sql.append(" AND turma.turno = ").append(turno);
		}
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		sql.append(" ORDER BY turno.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaTurmasNasQuaisTurmaParticipaDeAgrupamento(Integer codigoTurma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE Turma.codigo IN (SELECT turma FROM TurmaAgrupada WHERE turmaOrigem = ").append(codigoTurma).append(")");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);

	}

	public List<TurmaVO> consultaRapidaTurmasNasQuaisTurmaParticipaDeAgrupamentoPossuemAlunos(Integer codigoTurma, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE Turma.codigo IN (SELECT turma FROM TurmaAgrupada WHERE turmaOrigem = ").append(codigoTurma).append(")");
		sql.append(" and exists (SELECT distinct matriculaperiodoturmadisciplina.turma FROM matriculaPeriodoturmadisciplina ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
		sql.append(" inner join curso on curso.codigo = matricula.curso ");
		sql.append(" where  ( (curso.periodicidade = 'AN' and '").append(ano).append("' = matriculaperiodo.ano)   ");
		sql.append(" or (curso.periodicidade = 'SE' and '").append(ano).append("' = matriculaperiodo.ano and '").append(semestre).append("' = matriculaperiodo.semestre)  ");
		sql.append(" or (curso.periodicidade = 'IN' and matriculaperiodoturmadisciplina.ano = '' and matriculaperiodoturmadisciplina.semestre = ''))  ");
		sql.append(" and ((matriculaPeriodoturmadisciplina.turma = turma.codigo) or (matriculaPeriodoturmadisciplina.turmaPratica = turma.codigo  and turma.subturma and turma.tiposubturma = 'PRATICA') or (matriculaPeriodoturmadisciplina.turmateorica = turma.codigo and turma.subturma and turma.tiposubturma = 'TEORICA')) limit 1) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultarTurmaPorPessoa(Integer codigoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "Select Turma.* from Turma, MatriculaPeriodo, Matricula, Pessoa where MatriculaPeriodo.turma = Turma.codigo " + "and MatriculaPeriodo.matricula = Matricula.matricula and Matricula.aluno = Pessoa.codigo and Pessoa.codigo = " + codigoPessoa.intValue() + " Order By Turma.codigo";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "Select Turma.* from UnidadeEnsino,Turma, MatriculaPeriodo, Matricula, Pessoa where MatriculaPeriodo.turma = Turma.codigo " + "and MatriculaPeriodo.matricula = Matricula.matricula and Matricula.aluno = Pessoa.codigo and Pessoa.codigo = " + codigoPessoa.intValue() + " and Matricula.UnidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.codigo = " + unidadeEnsino.intValue() + " Order By Turma.codigo";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultarTurmaPorProfessor(Integer codigoPessoa, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "Select distinct Turma.* from UnidadeEnsino " + "inner join turma on turma.unidadeEnsino = unidadeEnsino.codigo " + "inner join registroAula on registroAula.turma = turma.codigo " + "inner join pessoa on registroAula.professor = Pessoa.codigo  WHERE Pessoa.codigo = " + codigoPessoa.intValue() + " ORDER BY turma.identificadorTurma";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "Select distinct Turma.* from UnidadeEnsino " + "inner join turma on turma.unidadeEnsino = unidadeEnsino.codigo " + "inner join registroAula on registroAula.turma = turma.codigo " + "inner join pessoa on registroAula.professor = Pessoa.codigo WHERE Pessoa.codigo = " + codigoPessoa.intValue() + " and Turma.UnidadeEnsino = UnidadeEnsino.codigo and UnidadeEnsino.codigo = " + unidadeEnsino.intValue() + " ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultarTurmaPorProfessorAnoSemestre(Integer codigoProfessor, Integer unidadeEnsino, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT DISTINCT turma.* FROM turma");
		sqlStr.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = turma.unidadeEnsino");
		sqlStr.append(" INNER JOIN horarioturmaprofessordisciplina ON horarioturmaprofessordisciplina.turma = turma.codigo");
		sqlStr.append(" INNER JOIN funcionario ON funcionario.pessoa = horarioturmaprofessordisciplina.professor");
		sqlStr.append(" INNER JOIN curso ON curso.codigo = turma.curso");
		sqlStr.append(" INNER JOIN pessoa AS pr ON pr.codigo = funcionario.pessoa");
		sqlStr.append(" INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.turma = turma.codigo");
		sqlStr.append(" WHERE pr.codigo = ").append(codigoProfessor).append(" AND");
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append(" unidadeensino.codigo = ").append(unidadeEnsino).append(" AND");
		}
		sqlStr.append(" ((curso.periodicidade = 'IN' AND matriculaperiodoturmadisciplina.ano = '' AND matriculaperiodoturmadisciplina.semestre = '' ) OR");
		sqlStr.append(" (curso.periodicidade = 'SE' AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' AND matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("') OR");
		sqlStr.append(" (curso.periodicidade = 'AN' AND matriculaperiodoturmadisciplina.ano = '").append(ano).append("' AND matriculaperiodoturmadisciplina.semestre = ''))");
		sqlStr.append(" ORDER BY turma.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultaRapidaTurmaPorProfessor(Integer codigoPessoa, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "Select distinct Turma.codigo from UnidadeEnsino,Turma, Pessoa, HorarioTurmaProfessorDisciplina where HorarioTurmaProfessorDisciplina.turma = Turma.codigo and HorarioTurmaProfessorDisciplina.professor = Pessoa.codigo and Pessoa.codigo = " + codigoPessoa.intValue() + " ";
		sqlStr += "  and Turma.UnidadeEnsino = UnidadeEnsino.codigo ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and UnidadeEnsino.codigo = " + unidadeEnsino.intValue() + " ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			carregarDados(obj, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<TurmaVO> consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(Integer codigoPessoa, String semestre, String ano, String situacao, Integer unidadeEnsino, Boolean visaoProfessor, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		// DATA ALTERAÇÃO SQL 13/05/016 - FOI REALIZADA ALTERAÇÃO PELO RODRIGO
		// PORQUE NÃO ESTAVA TRAZENDO ALUNOS
		// DEVIDO O ALUNO ESTAR EM UMA TURMA DE TURNO DIFERENTE DO TURNO DA
		// TURMA PADRÃO DO ALUNO.
		sqlStr.append("select distinct Turma.codigo, turma.identificadorTurma, turno.nome as turno, turma.subturma, ");
		sqlStr.append("case when turma.turmaagrupada then '' else curso.nome end as curso, case when  turma.turmaagrupada then '' else curso.nivelEducacional end as nivelEducacional, turma.turmaagrupada, ");
		sqlStr.append("(select liberarRegistroaulaentreperiodo from curso c2 where liberarRegistroaulaentreperiodo and c2.codigo = case when turma.turmaagrupada then t2.curso else turma.curso end limit 1 ) is not null as liberarRegistroaulaentreperiodo ");
		sqlStr.append(" from horarioturma ");
		sqlStr.append("inner join turma on horarioturma.turma = turma.codigo ");
		sqlStr.append("inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo  ");
		sqlStr.append("inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sqlStr.append("inner join pessoa ON horarioturmadiaitem.professor = Pessoa.codigo  ");
		sqlStr.append("inner join unidadeensino ON Turma.UnidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append("left join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
		sqlStr.append("left join turma t2 on t2.codigo = turmaagrupada.turma ");
		sqlStr.append("left join curso on curso.codigo = case when turma.turmaagrupada then t2.curso else turma.curso end ");
		sqlStr.append("left join turno on turno.codigo = case when turma.turmaagrupada then t2.turno else turma.turno end ");
		if (visaoProfessor) {
			sqlStr.append("where Pessoa.codigo = ").append(codigoPessoa.intValue());
			sqlStr.append(" and ((turma.semestral and HorarioTurma.semestrevigente = '").append(semestre).append("' and HorarioTurma.anovigente = '").append(ano).append("') ");
			sqlStr.append(" or (turma.anual and HorarioTurma.anovigente = '").append(ano).append("') ");
			sqlStr.append(" or (turma.anual = false and turma.semestral = false)) ");
		} else {
			sqlStr.append(" where Pessoa.codigo = ").append(codigoPessoa.intValue());
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue());
		}
		if (Uteis.isAtributoPreenchido(situacao)) {
			if (situacao.equals("AT")) {
				situacao = "AB";
			}
			if (situacao.equals("FI")) {
				situacao = "FE";
			}
			sqlStr.append(" AND turma.situacao = '").append(situacao).append("' ");
		}
		sqlStr.append(" order by identificadorTurma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
			obj.setTurmaAgrupada(tabelaResultado.getBoolean("turmaagrupada"));
			obj.setSubturma(tabelaResultado.getBoolean("subturma"));
			obj.getTurno().setNome(tabelaResultado.getString("turno"));
			obj.getCurso().setNome(tabelaResultado.getString("curso"));
			obj.getCurso().setNivelEducacional(tabelaResultado.getString("nivelEducacional"));
			obj.getCurso().setLiberarRegistroAulaEntrePeriodo(tabelaResultado.getBoolean("liberarRegistroAulaEntrePeriodo"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	public List<TurmaVO> consultarTurmaPorProfessorAnoSemestreEturmaEadNivelDadosCombobox(Integer codigoPessoa, String identificadorTurma, String semestre, String ano, String situacao, Integer unidadeEnsino, Boolean visaoProfessor, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		// DATA ALTERAï¿½ï¿½O SQL 13/05/016 - FOI REALIZADA ALTERAï¿½ï¿½O PELO RODRIGO
		// PORQUE Nï¿½O ESTAVA TRAZENDO ALUNOS
		// DEVIDO O ALUNO ESTAR EM UMA TURMA DE TURNO DIFERENTE DO TURNO DA
		// TURMA PADRï¿½O DO ALUNO.
		sqlStr.append("select distinct Turma.codigo, turma.identificadorTurma, turno.nome as turno, turma.subturma, ");
		sqlStr.append("case when turma.turmaagrupada then '' else curso.nome end as curso, case when  turma.turmaagrupada then '' else curso.nivelEducacional end as nivelEducacional, turma.turmaagrupada, ");
		sqlStr.append("(select liberarRegistroaulaentreperiodo from curso c2 where liberarRegistroaulaentreperiodo and c2.codigo = case when turma.turmaagrupada then t2.curso else turma.curso end limit 1 ) is not null as liberarRegistroaulaentreperiodo ");
		sqlStr.append(" from horarioturma ");
		sqlStr.append("inner join turma on horarioturma.turma = turma.codigo ");
		sqlStr.append("inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo  ");
		sqlStr.append("inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sqlStr.append("inner join pessoa ON horarioturmadiaitem.professor = Pessoa.codigo  ");
		sqlStr.append("inner join unidadeensino ON Turma.UnidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append("left join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
		sqlStr.append("left join turma t2 on t2.codigo = turmaagrupada.turma ");
		sqlStr.append("left join curso on curso.codigo = case when turma.turmaagrupada then t2.curso else turma.curso end ");
		sqlStr.append("left join turno on turno.codigo = case when turma.turmaagrupada then t2.turno else turma.turno end ");
		if (visaoProfessor) {
			sqlStr.append("where Pessoa.codigo = ").append(codigoPessoa.intValue());
			sqlStr.append(" AND (sem_acentos(turma.identificadorturma)) ilike (sem_acentos(?)) ");
			sqlStr.append(" and ((turma.semestral and HorarioTurma.semestrevigente = '").append(semestre).append("' and HorarioTurma.anovigente = '").append(ano).append("') ");
			sqlStr.append(" or (turma.anual and HorarioTurma.anovigente = '").append(ano).append("') ");
			sqlStr.append(" or (turma.anual = false and turma.semestral = false)) ");
		} else {
			sqlStr.append(" where Pessoa.codigo = ").append(codigoPessoa.intValue());
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue());
		}
		if (Uteis.isAtributoPreenchido(situacao)) {
			if (situacao.equals("AT")) {
				situacao = "AB";
			}
			if (situacao.equals("FI")) {
				situacao = "FE";
			}
			sqlStr.append(" AND turma.situacao = '").append(situacao).append("' ");
		}
				
		sqlStr.append(" union ");
		
		sqlStr.append(" select distinct Turma.codigo, turma.identificadorTurma, turno.nome as turno, turma.subturma, ");
		sqlStr.append(" case when turma.turmaagrupada then '' else curso.nome end as curso, case when  turma.turmaagrupada then '' else curso.nivelEducacional end as nivelEducacional, turma.turmaagrupada, ");
		sqlStr.append(" (select liberarRegistroaulaentreperiodo from curso c2 where liberarRegistroaulaentreperiodo and c2.codigo = case when turma.turmaagrupada then t2.curso else turma.curso end limit 1 ) is not null as liberarRegistroaulaentreperiodo ");
		sqlStr.append(" from programacaotutoriaonline ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma=programacaotutoriaonline.turma and turmadisciplina.disciplina=programacaotutoriaonline.disciplina ");
		sqlStr.append(" inner join turma on turma.codigo = programacaotutoriaonline.turma ");
		sqlStr.append(" left join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
		sqlStr.append(" left join turma t2 on t2.codigo = turmaagrupada.turma ");
		sqlStr.append(" Inner join curso on curso.codigo = case when turma.turmaagrupada then t2.curso else turma.curso end ");
		sqlStr.append(" Inner join turno on turno.codigo = case when turma.turmaagrupada then t2.turno else turma.turno end ");
		sqlStr.append(" inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo ");
		sqlStr.append(" where  programacaotutoriaonlineprofessor.professor = ").append(codigoPessoa.intValue());
		sqlStr.append(" and  programacaotutoriaonline.situacao = 'ATIVO' ");
		sqlStr.append(" and turmadisciplina.modalidadedisciplina = 'ON_LINE' ");
		sqlStr.append(" and turmadisciplina.definicoestutoriaonline = 'DINAMICA' ");

		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and (programacaotutoriaonline.unidadeensino is null or programacaotutoriaonline.unidadeensino = ").append(unidadeEnsino).append(" )");
			sqlStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino).append(" ");

		}
		sqlStr.append(" order by identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), identificadorTurma + PERCENT);
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
			obj.setTurmaAgrupada(tabelaResultado.getBoolean("turmaagrupada"));
			obj.setSubturma(tabelaResultado.getBoolean("subturma"));
			obj.getTurno().setNome(tabelaResultado.getString("turno"));
			obj.getCurso().setNome(tabelaResultado.getString("curso"));
			obj.getCurso().setNivelEducacional(tabelaResultado.getString("nivelEducacional"));
			obj.getCurso().setLiberarRegistroAulaEntrePeriodo(tabelaResultado.getBoolean("liberarRegistroAulaEntrePeriodo"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Override
	public List<TurmaVO> consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(Integer codigoPessoa, String semestre, String ano, Boolean buscarTurmasAnteriores, String situacao, Integer unidadeEnsino, Boolean visaoProfessor, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, Boolean consultarTurmasEAD) throws Exception {

		return consultarTurmaPorProfessorAnoSemestreTurmaAnteriorCursoNivelDadosCombobox(codigoPessoa, semestre, ano, buscarTurmasAnteriores, situacao, unidadeEnsino, 0, visaoProfessor, nivelEducacionalPosGraduacao, nivelEducacionalDiferentePosGraduacao, consultarTurmasEAD, null , false, null);
	}

	@Override
	public List<TurmaVO> consultarTurmaPorProfessorAnoSemestreTurmaAnteriorCursoNivelDadosCombobox(Integer codigoPessoa, String semestre, String ano, Boolean buscarTurmasAnteriores, String situacao, Integer unidadeEnsino, Integer curso, Boolean visaoProfessor, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, Boolean consultarTurmasEAD, PeriodicidadeEnum periodicidadeEnum , Boolean consultaTurmaForum, Integer disciplina) throws Exception {
		StringBuilder sqlStr = new StringBuilder("Select distinct Turma.codigo, turma.identificadorTurma, turno.nome as turno, curso.nome as curso, curso.nivelEducacional, curso.liberarRegistroAulaEntrePeriodo, turma.turmaagrupada from Turma");
		sqlStr.append(" INNER JOIN HorarioTurma on (HorarioTurma.turma = turma.codigo) ");
		sqlStr.append(" INNER JOIN HorarioTurmadia on (HorarioTurmadia.HorarioTurma = horarioTurma.codigo) ");
		sqlStr.append(" INNER JOIN HorarioTurmadiaitem on (HorarioTurmadiaitem.HorarioTurmadia = HorarioTurmadia.codigo) ");
		sqlStr.append(" Inner Join pessoa ON HorarioTurmadiaitem.professor = Pessoa.codigo ");
		sqlStr.append(" Inner join unidadeensino ON Turma.UnidadeEnsino = UnidadeEnsino.codigo");
		sqlStr.append(" left join turno on turno.codigo = turma.turno");
		sqlStr.append(" left join curso on curso.codigo = turma.curso");
		if (visaoProfessor && !buscarTurmasAnteriores) {
			sqlStr.append(" where Pessoa.codigo = ").append(codigoPessoa.intValue());
			if (!buscarTurmasAnteriores && Uteis.isAtributoPreenchido(ano)) {
				sqlStr.append(" and ((turma.semestral and HorarioTurma.semestrevigente = '").append(semestre).append("' and  HorarioTurma.anovigente = '").append(ano).append("') ");
				sqlStr.append(" or (turma.anual and  HorarioTurma.anovigente = '").append(ano).append("') ");
				sqlStr.append(" or (turma.anual = false and  turma.semestral = false)) ");
			}

		} else {
			sqlStr.append(" where Pessoa.codigo = ").append(codigoPessoa.intValue());
			if (consultaTurmaForum && (!Uteis.isAtributoPreenchido(semestre) || !Uteis.isAtributoPreenchido(ano))) {
				if (Uteis.isAtributoPreenchido(ano)) {
					sqlStr.append(" and ((turma.semestral  and  HorarioTurma.anovigente = '").append(ano).append("') ");
					sqlStr.append(" or (turma.anual and  HorarioTurma.anovigente = '").append(ano).append("') ");
					sqlStr.append(" or (turma.anual = false and  turma.semestral = false)) ");	
				}
			} else {
				sqlStr.append(" and ((turma.semestral and HorarioTurma.semestrevigente = '").append(semestre).append("' and  HorarioTurma.anovigente = '").append(ano).append("') ");
				sqlStr.append(" or (turma.anual and  HorarioTurma.anovigente = '").append(ano).append("') ");
				sqlStr.append(" or (turma.anual = false and  turma.semestral = false)) ");
			}
			
		}
		if (curso != null && curso.intValue() != 0) {
			sqlStr.append(" and curso.codigo = ").append(curso.intValue());
		}
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue());
		}
		if (disciplina != null && disciplina.intValue() != 0) {
			sqlStr.append(" AND HorarioTurmadiaitem.disciplina = ").append(disciplina);
		}
		sqlStr.append(" AND turma.situacao = 'AB' ");
		
		if(Uteis.isAtributoPreenchido(periodicidadeEnum)) {
			if (periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL)) {
				sqlStr.append(" and (turma.anual is null or turma.anual is false) and (turma.semestral is null or turma.semestral is false) ");
			}
			if (periodicidadeEnum.equals(PeriodicidadeEnum.ANUAL)) {
				sqlStr.append(" and turma.anual is true");
			}
			if (periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL)) {
				sqlStr.append(" and turma.semestral is true");				
			}
			
		}

		if (consultarTurmasEAD) {
			sqlStr.append(" union ");
			getSqlPadraoConsultaTurmaEad(codigoPessoa, unidadeEnsino, curso, periodicidadeEnum, sqlStr);

		}
		
		if (visaoProfessor) {
			sqlStr.append(" union all");
			sqlStr.append(" select Turma.codigo, turma.identificadorTurma, turno.nome as turno, curso.nome as curso, curso.nivelEducacional, curso.liberarRegistroAulaEntrePeriodo, turma.turmaagrupada from turma");
			sqlStr.append(" inner join calendariolancamentonota on calendariolancamentonota.turma = turma.codigo");
			sqlStr.append(" left join turno on turno.codigo = turma.turno");
			sqlStr.append(" left join curso on curso.codigo = turma.curso");
			sqlStr.append(" where calendariolancamentonota.professorexclusivolancamentodenota = ").append(codigoPessoa);
			sqlStr.append(" and calendariopor = 'TU' and calendariolancamentonota.ano = '").append(ano).append("'");
			sqlStr.append(" and calendariolancamentonota.semestre = '").append(semestre).append("'");
		}

		sqlStr.append(" order by identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
			obj.setTurmaAgrupada(tabelaResultado.getBoolean("turmaagrupada"));
			obj.getTurno().setNome(tabelaResultado.getString("turno"));
			obj.getCurso().setNome(tabelaResultado.getString("curso"));
			obj.getCurso().setNivelEducacional(tabelaResultado.getString("nivelEducacional"));
			obj.getCurso().setLiberarRegistroAulaEntrePeriodo(tabelaResultado.getBoolean("liberarRegistroAulaEntrePeriodo"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void getSqlPadraoConsultaTurmaEad(Integer codigoPessoa, Integer unidadeEnsino, Integer curso, PeriodicidadeEnum periodicidadeEnum, StringBuilder sqlStr) {		
		sqlStr.append(" select distinct Turma.codigo, turma.identificadorTurma, turno.nome as turno, curso.nome as curso,");
		sqlStr.append(" curso.nivelEducacional, curso.liberarRegistroAulaEntrePeriodo, turma.turmaagrupada");
		sqlStr.append(" from programacaotutoriaonline");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.disciplina = programacaotutoriaonline.disciplina and turmadisciplina.definicoestutoriaonline = 'DINAMICA' ");
		sqlStr.append(" inner join turma on turma.codigo = turmadisciplina.turma ");
		sqlStr.append(" left join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
		sqlStr.append(" left join turma t2 on t2.codigo = turmaagrupada.turma ");
		sqlStr.append(" Inner join curso on curso.codigo = case when turma.turmaagrupada then t2.curso else turma.curso end ");
		sqlStr.append(" Inner join turno on turno.codigo = case when turma.turmaagrupada then t2.turno else turma.turno end ");
		sqlStr.append(" inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo ");
		sqlStr.append(" where  programacaotutoriaonlineprofessor.professor =  ").append(codigoPessoa);
		sqlStr.append(" and  programacaotutoriaonline.situacao = 'ATIVO' ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and (programacaotutoriaonline.unidadeensino is null or programacaotutoriaonline.unidadeensino = ").append(unidadeEnsino).append(" )");
			sqlStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino).append(" ");

		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and programacaotutoriaonline.turma is null and programacaotutoriaonline.curso is null and curso.codigo = ").append(curso);
		} else {
			sqlStr.append(" and programacaotutoriaonline.turma is null ");
		}
		
		if(Uteis.isAtributoPreenchido(periodicidadeEnum)) {
			if (periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL)) {
				sqlStr.append(" and (turma.anual is null or turma.anual is false) and (turma.semestral is null or turma.semestral is false) ");
			}
			if (periodicidadeEnum.equals(PeriodicidadeEnum.ANUAL)) {
				sqlStr.append(" and turma.anual is true");
			}
			if (periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL)) {
				sqlStr.append(" and turma.semestral is true");				
			}
			
		}

		sqlStr.append(" union ");
		sqlStr.append(" select distinct Turma.codigo, turma.identificadorTurma, turno.nome as turno, curso.nome as curso,");
		sqlStr.append(" curso.nivelEducacional, curso.liberarRegistroAulaEntrePeriodo, turma.turmaagrupada");
		sqlStr.append(" from programacaotutoriaonline");
		sqlStr.append(" inner join turma on turma.codigo = programacaotutoriaonline.turma");
		sqlStr.append(" left join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
		sqlStr.append(" left join turma t2 on t2.codigo = turmaagrupada.turma ");
		sqlStr.append(" Inner join curso on curso.codigo = case when turma.turmaagrupada then t2.curso else turma.curso end ");
		sqlStr.append(" Inner join turno on turno.codigo = case when turma.turmaagrupada then t2.turno else turma.turno end ");
		sqlStr.append(" inner join programacaotutoriaonlineprofessor on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo");
		sqlStr.append(" where  programacaotutoriaonlineprofessor.professor =  ").append(codigoPessoa);
		sqlStr.append(" and  programacaotutoriaonline.situacao = 'ATIVO' ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and (programacaotutoriaonline.unidadeensino is null or programacaotutoriaonline.unidadeensino = ").append(unidadeEnsino).append(" )");
			sqlStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino).append(" ");
		}
		sqlStr.append(" and programacaotutoriaonline.turma is not null ");
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append("and programacaotutoriaonline.curso is null and curso.codigo = ").append(curso);
		}
		
		if(Uteis.isAtributoPreenchido(periodicidadeEnum)) {
			if (periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL)) {
				sqlStr.append(" and (turma.anual is null or turma.anual is false) and (turma.semestral is null or turma.semestral is false) ");
			}
			if (periodicidadeEnum.equals(PeriodicidadeEnum.ANUAL)) {
				sqlStr.append(" and turma.anual is true");
			}
			if (periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL)) {
				sqlStr.append(" and turma.semestral is true");				
			}
			
		}
	}

	public Boolean consultarLiberarRegistroAulaEnterPeriodoTurmaAgrupada(Integer turma, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct case when (turmaagrupada = false) then curso.liberarRegistroAulaEntrePeriodo else ( ");
		sb.append(" select distinct liberarregistroaulaentreperiodo from turmaagrupada  ");
		sb.append(" inner join turma t1 on t1.codigo = turmaagrupada.turma ");
		sb.append(" inner join curso on curso.codigo = t1.curso ");
		sb.append(" where turmaorigem = turma.codigo limit 1) end as liberarRegistroAulaEntrePeriodo ");
		sb.append(" from turma ");
		sb.append(" LEFT JOIN curso ON curso.codigo = turma.curso ");
		sb.append(" where turma.codigo = ").append(turma);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("liberarRegistroAulaEntrePeriodo");
		}
		return false;
	}

	public Boolean consultarTurmaAgrupadaPorCodigoTurma(Integer codigoTurma) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select turmaagrupada from turma where codigo = ").append(codigoTurma);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("turmaagrupada");
		}
		return false;
	}

	public List consultarNotasPorCursoTurma(Integer codigoTurma) throws Exception {
		StringBuilder sqlStr = new StringBuilder(" select ");
		for (int contador =1; contador <= 30; contador++ ) {
			sqlStr.append("utilizarnota"+contador+",");
			sqlStr.append("tituloNota"+contador+",");
			sqlStr.append("bimestreNota"+contador).append(contador ==30 ? "" : ",");
		}
		sqlStr.append(" from turma ");
		sqlStr.append(" inner join curso on curso.codigo = turma.curso ");
		sqlStr.append(" inner join configuracaoacademico on configuracaoacademico.codigo = curso.configuracaoacademico ");
		sqlStr.append(" where turma.codigo = ").append(codigoTurma);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List lista = new ArrayList(0);

		if (tabelaResultado.next()) {
			for (int i = 1; i <= 30; i++) {
				if (tabelaResultado.getBoolean("utilizarnota" + i)) {
					lista.add(tabelaResultado.getString("tituloNota" + i) + " - " + BimestreEnum.valueOf(tabelaResultado.getString("bimestreNota"+i)).getValorApresentar());
				}
			}
		}

		return lista;
	}

	public List<TurmaVO> consultarTurmaAgrupadaPorProfessorAnoSemestreNivelDadosCombobox(Integer codigoPessoa, String semestre, String ano, String situacao, Integer unidadeEnsino, Boolean visaoProfessor, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, Boolean permitirRegistrarAulaRetroativa) throws Exception {
		StringBuilder sqlStr = new StringBuilder("(Select distinct Turma.codigo, turma.identificadorTurma, turno.nome as turno, curso.nome as curso, curso.nivelEducacional, turma.turmaagrupada from Turma ");
		sqlStr.append("Inner join HorarioTurmaProfessorDisciplina on HorarioTurmaProfessorDisciplina.turma = Turma.codigo ");
		sqlStr.append("INNER JOIN HorarioTurma on (HorarioTurmaProfessorDisciplina.horarioturma = horarioTurma.codigo) ");
		sqlStr.append("Inner Join pessoa ON HorarioTurmaProfessorDisciplina.professor = Pessoa.codigo ");
		sqlStr.append("Inner join unidadeensino ON Turma.UnidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append("left join curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada and curso.codigo = (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo limit 1 ) ) )");
		sqlStr.append("left join turno on turno.codigo = turma.turno ");
		if (visaoProfessor && (permitirRegistrarAulaRetroativa == null || !permitirRegistrarAulaRetroativa)) {
			sqlStr.append("Inner join unidadeensinocurso ON ( turma.unidadeensino = unidadeensinocurso.unidadeensino) and unidadeensinocurso.curso = curso.codigo and unidadeensinocurso.turno = turno.codigo ");
			sqlStr.append("Inner join processomatriculaunidadeensino ON unidadeensinocurso.unidadeensino = processomatriculaunidadeensino.unidadeensino ");
			sqlStr.append("Inner join processomatriculacalendario ON processomatriculacalendario.processomatricula = processomatriculaunidadeensino.processomatricula  ");
			sqlStr.append(" AND processomatriculacalendario.curso = unidadeensinocurso.curso ");
			sqlStr.append(" AND processomatriculacalendario.turno = unidadeensinocurso.turno ");
			sqlStr.append("Inner Join periodoletivoativounidadeensinocurso ON processomatriculacalendario.periodoletivoativounidadeensinocurso = periodoletivoativounidadeensinocurso.codigo ");
			sqlStr.append("where ((turma.anual and HorarioTurma.anovigente = '").append(ano).append("' and periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo='").append(ano).append("') ");
			sqlStr.append(" or (turma.semestral and HorarioTurma.anovigente = '").append(ano).append("' and periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo='").append(ano).append("' and HorarioTurma.semestrevigente = '").append(semestre).append("' and periodoletivoativounidadeensinocurso.semestrereferenciaperiodoletivo='").append(semestre).append("') ");
			sqlStr.append(" or (turma.anual = false and turma.semestral =  false )) ");
			sqlStr.append(" AND periodoletivoativounidadeensinocurso.situacao = '").append(situacao).append("' and Pessoa.codigo = ").append(codigoPessoa);
			sqlStr.append(" AND periodoletivoativounidadeensinocurso.curso = unidadeensinocurso.curso ");
			sqlStr.append(" AND periodoletivoativounidadeensinocurso.turno = unidadeensinocurso.turno ");
		} else {
			sqlStr.append("where Pessoa.codigo = ").append(codigoPessoa);
		}
		sqlStr.append(" and turma.situacao = 'AB'");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		if (visaoProfessor) {
			if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
				sqlStr.append(" AND ((curso.nivelEducacional in ('PO','EX', 'MT')))");
			}
			if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
				sqlStr.append(" AND (curso.nivelEducacional != 'PO')");
			}
		}
		sqlStr.append(")");
		sqlStr.append(" order by identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
			obj.setTurmaAgrupada(tabelaResultado.getBoolean("turmaagrupada"));
			obj.getTurno().setNome(tabelaResultado.getString("turno"));
			obj.getCurso().setNome(tabelaResultado.getString("curso"));
			obj.getCurso().setNivelEducacional(tabelaResultado.getString("nivelEducacional"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<TurmaVO> consultaRapidaTurmaPorProfessorSemestreAnoSituacao(Integer codigoPessoa, String semestre, String ano, String situacao, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario, boolean nivelEducacionalPosGraduacao, boolean nivelEducacionalDiferentePosGraduacao) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select distinct Turma.codigo from (");
		sqlStr.append(" Select distinct turma.codigo, ue.codigo unidadeensino ");
		sqlStr.append(" from HorarioTurma  ");
		sqlStr.append(" Inner join HorarioTurmaProfessorDisciplina on HorarioTurmaProfessorDisciplina.horarioTurma = HorarioTurma.codigo ");
		sqlStr.append(" Inner join Turma on turma.codigo = HorarioTurma.turma ");
		sqlStr.append(" Inner Join pessoa ON HorarioTurmaProfessorDisciplina.professor = Pessoa.codigo ");
		sqlStr.append(" left join Curso on curso.codigo = turma.curso ");
		sqlStr.append(" left join turmaagrupada ta on turma.turmaagrupada and ta.turmaorigem = turma.codigo ");
		sqlStr.append(" left join turma t on ta.turma = t.codigo ");
		sqlStr.append(" left join unidadeensino ue on (t.unidadeensino = ue.codigo or turma.unidadeensino = ue.codigo) ");
		sqlStr.append(" where ((turma.semestral and HorarioTurma.semestrevigente = '").append(semestre).append("' and HorarioTurma.anovigente = '").append(ano).append("') ");
		sqlStr.append(" or (turma.anual and HorarioTurma.anovigente = '").append(ano).append("')");
		sqlStr.append(" or (coalesce(turma.anual,FALSE) IS FALSE AND coalesce(turma.semestral,FALSE) IS FALSE))");
		sqlStr.append(" and pessoa.codigo = ").append(codigoPessoa).append(" and Turma.situacao = 'AB') as turma");
		sqlStr.append(" Inner join unidadeensino ON Turma.UnidadeEnsino = UnidadeEnsino.codigo ");
		sqlStr.append(" Inner join unidadeensinocurso ON ( turma.unidadeensino = unidadeensinocurso.unidadeensino) ");
		sqlStr.append(" Inner join processomatriculaunidadensino ON unidadeensinocurso.unidadeensino = processomatriculaunidadensino.unidadeensino ");
		sqlStr.append(" Inner join processomatriculacalendario ON processomatriculacalendario.processomatricula = processomatriculaunidadensino.processomatricula  ");
		sqlStr.append(" AND processomatriculacalendario.curso = unidadeensinocurso.curso ");
		sqlStr.append(" AND processomatriculacalendario.turno = unidadeensinocurso.turno ");
		sqlStr.append(" inner Join periodoletivoativounidadeensinocurso ON processomatriculacalendario.periodoletivoativounidadeensinocurso = periodoletivoativounidadeensinocurso.codigo ");
		sqlStr.append(" AND periodoletivoativounidadeensinocurso.curso = unidadeensinocurso.curso ");
		sqlStr.append(" AND periodoletivoativounidadeensinocurso.turno = unidadeensinocurso.turno ");
		sqlStr.append(" Inner Join curso ON unidadeEnsinocurso.curso = curso.codigo ");
		sqlStr.append(" WHERE periodoletivoativounidadeensinocurso.situacao = '").append(situacao).append("' ");
		sqlStr.append(" AND periodoletivoativounidadeensinocurso.anoreferenciaperiodoletivo in ('").append(ano).append("','')");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and UnidadeEnsino.codigo = ").append(unidadeEnsino.intValue());
		}
		if (nivelEducacionalPosGraduacao) {
			sqlStr.append(" AND curso.nivelEducacional = 'PO'");
		}
		if (nivelEducacionalDiferentePosGraduacao) {
			sqlStr.append(" AND curso.nivelEducacional != 'PO'");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			carregarDados(obj, NivelMontarDados.BASICO, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}	

	public List<TurmaVO> consultarTurmaPorDisciplina(String nomeDisciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT turma.* FROM matriculaperiodoturmadisciplina " + "INNER JOIN disciplina ON matriculaperiodoturmadisciplina.disciplina = disciplina.codigo " + "INNER JOIN turma ON matriculaperiodoturmadisciplina.turma = turma.codigo " + "WHERE disciplina.nome = '" + nomeDisciplina + "' ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * responsável por realizar uma consulta de <code>Turma</code> através do valor do atributo <code>nome</code> da classe <code>UnidadeEnsino</code> Faz uso da Operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vÃ¯Â¿Â½rios objetos da classe <code>TurmaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restriÃ§Ã£oo de acesso.
	 */
	public List<TurmaVO> consultarPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS || nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			StringBuilder sql = getSQLPadraoConsultaBasica();
			sql.append(" WHERE upper(sem_acentos(UnidadeEnsino.nome)) ilike(sem_acentos('");
			sql.append(valorConsulta.toUpperCase());
			sql.append("%')");
			sql.append(" )");
			if (unidadeEnsino.intValue() != 0) {
				sql.append(" AND (turma.unidadeensino = ").append(unidadeEnsino.intValue()).append(") ");
			}
			sql.append(" ORDER BY turno.nome ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			return montarDadosConsultaBasica(tabelaResultado);
		} else {
			String sqlStr = "SELECT Turma.* FROM Turma, UnidadeEnsino WHERE Turma.unidadeEnsino = UnidadeEnsino.codigo and lower (UnidadeEnsino.nome) like('" + valorConsulta.toLowerCase() + "%') ORDER BY UnidadeEnsino.nome";
			if (unidadeEnsino.intValue() != 0) {
				sqlStr = "SELECT Turma.* FROM Turma, UnidadeEnsino WHERE Turma.unidadeEnsino = UnidadeEnsino.codigo and lower (UnidadeEnsino.nome) like('" + valorConsulta.toLowerCase() + "%') and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY UnidadeEnsino.nome";
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
			return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		}
	}

	public List<TurmaVO> consultaRapidaPorUnidadeEnsinoCurso(String valorConsulta, List<CursoVO> cursos, List<UnidadeEnsinoVO> unidades, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE lower(unidadeensino.nome) LIKE ('");
		sql.append(valorConsulta.toLowerCase());
		sql.append("%')");
		if (Uteis.isAtributoPreenchido(cursos)) {
			sql.append(" AND curso.codigo  IN (");
			int x = 0;
			for (CursoVO cursoVO : cursos) {
				if (x > 0) {
					sql.append(", ");
				}
				sql.append(cursoVO.getCodigo());
				x++;
			}
			sql.append(" ) ");
		}

		if (Uteis.isAtributoPreenchido(unidades)) {
			sql.append(" AND unidadeensino.codigo  IN (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidades) {
				if (x > 0) {
					sql.append(", ");
				}
				sql.append(unidadeEnsinoVO.getCodigo());
				x++;
			}
			sql.append(" ) ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorUnidadeEnsinoCurso(String valorConsulta, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, Integer limite, Integer offset) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE sem_acentos(turma.identificadorTurma) ilike (sem_acentos(?))");
		if (curso != 0) {
			sql.append(" AND (turma.curso = ").append(curso).append(" OR (turma.turmaagrupada and exists (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma and turmaagrupada.turmaorigem = turma.codigo and t.curso = ").append(curso).append(" limit 1 ))) ");
		}
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		sql.append(" ORDER BY unidadeensino.nome, turma.identificadorTurma ");
		if(Uteis.isAtributoPreenchido(limite)) {
			sql.append(" limit ").append(limite).append(" offset ").append(offset);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta+"%" );
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorUnidadeEnsino(valorConsulta, 0, unidadeEnsino, nivelEducacionalPosGraduacao, nivelEducacionalDiferentePosGraduacao, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultaRapidaPorUnidadeEnsino(String valorConsulta, Integer turmaOrigem, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE (sem_acentos(unidadeensino.nome)) ilike (sem_acentos(?)) ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX', 'MT'))");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND curso.nivelEducacional != 'PO'");
		}
		if (turmaOrigem != 0) {
			sql.append(" AND turma.codigo <> ").append(turmaOrigem.intValue());
		}
		sql.append(" ORDER BY unidadeensino.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorNomeUnidadeEnsinoCodigoUnidadeEnsinoTurmaPrincipalECodigoCursoTurmaPrincipal(String valorConsulta, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE lower(sem_acentos(unidadeensino.nome)) ilike (sem_acentos('");
		sql.append(valorConsulta);
		sql.append("%'))");
		if (curso.intValue() != 0) {
			sql.append(" and turma.curso = ");
			sql.append(curso.intValue());
		}
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		sql.append(" ORDER BY unidadeensino.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorUnidadeEnsinoNivelEducacional(String valorConsulta, Integer unidadeEnsino, String nivelEducacional, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE lower(sem_acentos(unidadeensino.nome)) ilike (sem_acentos(?))");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		if (!nivelEducacional.equals("")) {
			sql.append(" AND curso.nivelEducacional in ('").append(nivelEducacional.toUpperCase()).append("') ");
		}
		if (Uteis.isAtributoPreenchido(turno)) {
			sql.append(" AND turno.codigo = ").append(turno);
		}
		sql.append(" ORDER BY unidadeensino.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public TurmaVO consultarPorCodigoUnidadeEnsinoIdentificadorTurma(Integer unidadeEnsino, String identificadorTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Turma.* FROM Turma, UnidadeEnsino WHERE Turma.unidadeEnsino = UnidadeEnsino.codigo ";
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr += " and UnidadeEnsino.codigo =" + unidadeEnsino.intValue();
		}
		sqlStr += " and lower (Turma.identificadorTurma) like lower(?)  ORDER BY UnidadeEnsino.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, identificadorTurma);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<TurmaVO> consultarPorUnidadeEnsinoIdentificadorTurma(Integer unidadeEnsino, String identificadorTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> lista = new ArrayList<UnidadeEnsinoVO>();
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			lista.add(unidadeEnsinoVO);
			return this.consultarPorListaUnidadeEnsinoIdentificadorTurma(lista, identificadorTurma, controlarAcesso, nivelMontarDados, usuario);
		}
		return new ArrayList<TurmaVO>(0);
	}

	@Override
	public List<TurmaVO> consultarPorListaUnidadeEnsinoIdentificadorTurma(List<UnidadeEnsinoVO> unidadeEnsinos, String identificadorTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Turma.* FROM Turma, UnidadeEnsino WHERE Turma.unidadeEnsino = UnidadeEnsino.codigo ";
		sqlStr += " AND turma.unidadeensino in (";
		for (UnidadeEnsinoVO unidade : unidadeEnsinos) {
			if (unidadeEnsinos.get(unidadeEnsinos.size() -1).equals(unidade)) {
				sqlStr += unidade.getCodigo() + ") ";
			} else {
				sqlStr += unidade.getCodigo() + ", ";
			}
		}
//		for (String unidadeVO : unidade) {
//			if (!lista.get(lista.size() -1).equals(unidadeVO)) {
//				unidades += unidadeVO+", ";
//			} else {
//				unidades += unidadeVO;
//			}
//		}
		sqlStr += " and lower (Turma.identificadorTurma) like lower(?)  ORDER BY UnidadeEnsino.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, identificadorTurma + PERCENT);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(String valorConsulta, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica().append(" WHERE lower (sem_acentos(turma.identificadorTurma)) like lower(sem_acentos(?))  ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append(" AND unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		if (curso != null && curso.intValue() != 0) {
			sqlStr.append(" AND ((turma.turmaagrupada = false and curso.codigo = ").append(curso).append(")");
			sqlStr.append(" or (turma.turmaagrupada = true and turma.codigo in (select ta.turmaorigem from turmaagrupada ta inner join turma as t on t.codigo = ta.turma where ta.turmaorigem = turma.codigo and t.curso =  ").append(curso).append("))) ");
		}
		if (turno != null && turno.intValue() != 0) {
			sqlStr.append(" AND turno.codigo = ").append(turno);
		}
		sqlStr.append(" ORDER BY  turma.identificadorTurma");
		// String sqlStr =
		// "SELECT Turma.* FROM Turma, UnidadeEnsino WHERE Turma.unidadeEnsino =
		// UnidadeEnsino.codigo and lower (Turma.identificadorTurma) like('"
		// + valorConsulta.toLowerCase() + "%') ";
		// if (unidadeEnsino.intValue() != 0) {
		// sqlStr += " and UnidadeEnsino.codigo = " + unidadeEnsino.intValue() +
		// " ";
		// }
		// sqlStr += "ORDER BY Turma.identificadorTurma";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCurso(String valorConsulta, Integer unidadeEnsino, Integer curso, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica().append(" WHERE lower (sem_acentos(turma.identificadorTurma)) like(sem_acentos('").append(valorConsulta.toLowerCase()).append("%'))  ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sqlStr.append("AND unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		if (curso != null && curso.intValue() != 0) {
			sqlStr.append("AND curso.codigo = ").append(curso);
		}
		sqlStr.append("ORDER BY  turma.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurma(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica().append(" WHERE lower (sem_acentos(turma.identificadorTurma)) like(sem_acentos('").append(valorConsulta.toLowerCase()).append("%'))  ");
		sqlStr.append("ORDER BY  turma.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica().append(" WHERE lower (sem_acentos(turma.identificadorTurma)) like(sem_acentos('").append(valorConsulta.toLowerCase()).append("%'))  ");
		if (!unidadeEnsino.equals(0)) {
			sqlStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino);
		}
		sqlStr.append(" ORDER BY  turma.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultarPorIdentificadorTurmaUnidadeEnsinoCursoTurno(String valorConsulta, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, int nivelMontarDados, String nivelEducacional, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append("WHERE sem_acentos(lower (turma.identificadorturma)) like(sem_acentos('").append(valorConsulta.toLowerCase()).append("%'))  ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sql.append(" AND turma.unidadeEnsino = " + unidadeEnsino);
		}
		if (curso != null && curso.intValue() != 0) {
			sql.append(" AND turma.curso = " + curso);
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sql.append(" AND (Curso.niveleducacional = '").append(nivelEducacional).append("')");
		}
		if (turno != null && turno.intValue() != 0) {
			sql.append(" AND turma.turno = " + turno);
		}
		sql.append(" ORDER BY  turma.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultarPorIdentificadorTurmaCodigoCurso(String valorConsulta, Integer curso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT Turma.* from turma ");
		sqlStr.append("INNER JOIN Curso on (curso.codigo = turma.curso or curso.codigo = (select t.curso from turma t where t.codigo = turma.turmaprincipal))");
		sqlStr.append("WHERE LOWER(sem_acentos(identificadorTurma)) LIKE (sem_acentos('").append(valorConsulta.toLowerCase()).append("%'))");
		sqlStr.append("AND curso.codigo = ").append(curso);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultaRapidaPorNrPeriodoLetivoUnidadeEnsinoCursoTurno(Integer nrPeridoLetivo, Integer unidadeEnsino, Integer curso, Integer turno, Integer gradeCurricular, boolean novaMatricula, boolean renovandoMatricula, boolean editandoMatricula, boolean trazerApenasTurmaComAulaProgramada, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario,  Boolean trazerApenasTurmasApresentarRenovacaoOnline) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE (turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(") ");
		sql.append("   and (Periodoletivo.periodoLetivo = ").append(nrPeridoLetivo.intValue()).append(") ");
		sql.append("   and (Curso.codigo = ").append(curso.intValue()).append(") ");
		sql.append("   and (Turno.codigo = ").append(turno.intValue()).append(") ");
		sql.append("   and (Turma.situacao = 'AB')");
		if (novaMatricula || renovandoMatricula || editandoMatricula) {

			sql.append(" and (Turma.gradeCurricular = ").append(gradeCurricular.intValue()).append(")");
//			sql.append(" and (Turma.planoFinanceiroCurso is not null  ");
//			sql.append(" or  (select planoFinanceiroCurso from unidadeensinocurso where unidadeensinocurso.curso = curso.codigo and unidadeensinocurso.turno = Turno.codigo and unidadeensinocurso.unidadeensino = unidadeensino.codigo limit 1) is not null  ");
//			sql.append(" or (select realizarmatriculacomfinanceiromanualativo is true from configuracaofinanceiro where codigo = ").append(configuracaoFinanceiro.getCodigo()).append("))");
		}
		sql.append(" and Turma.subturma = false ");
		if (trazerApenasTurmaComAulaProgramada) {
			sql.append(" and exists (select ofertadisciplina.codigo from ofertadisciplina inner join turmadisciplina on turmadisciplina.turma = turma.codigo where ofertadisciplina.disciplina = turmadisciplina.disciplina and ofertadisciplina.ano = '").append(ano).append("' and semestre =  '").append(semestre).append("' ) ");
//			sql.append(" and exists ( ");
//			sql.append(" select horarioturma.turma from horarioturma inner join horarioturmadia htd on htd.horarioturma = horarioturma.codigo ");
//			sql.append(" inner join horarioturmadiaitem htdi on htdi.horarioturmadia = htd.codigo and htdi.disciplina is not null ");
//			sql.append(" where  horarioturma.turma = turma.codigo ");
//			sql.append(" and anovigente = '").append(ano).append("' ");
//			if(Uteis.isAtributoPreenchido(semestre)) {
//				sql.append(" and semestrevigente = '").append(semestre).append("' ");
//			}
//			sql.append(" union all ");
//			sql.append(" select t.turmaprincipal from horarioturma  inner join turma as t on t.codigo = horarioturma.turma ");
//			sql.append(" inner join horarioturmadia htd on htd.horarioturma = horarioturma.codigo ");
//			sql.append(" inner join horarioturmadiaitem htdi on htdi.horarioturmadia = htd.codigo and htdi.disciplina is not null ");
//			sql.append(" and anovigente = '").append(ano).append("' ");
//			if(Uteis.isAtributoPreenchido(semestre)) {
//				sql.append(" and semestrevigente = '").append(semestre).append("' ");
//			}
//			sql.append(" and t.turmaprincipal = turma.codigo and t.situacao = 'AB'");
//			sql.append(" union all ");
//			sql.append(" select tag.turma from horarioturma inner join turma as t on t.codigo = horarioturma.turma ");
//			sql.append(" inner join turmaagrupada as tag on tag.turmaOrigem = t.codigo ");
//			sql.append(" inner join horarioturmadia htd on htd.horarioturma = horarioturma.codigo ");
//			sql.append(" inner join horarioturmadiaitem htdi on htdi.horarioturmadia = htd.codigo and htdi.disciplina is not null ");
//			sql.append(" and anovigente = '").append(ano).append("' ");
//			if(Uteis.isAtributoPreenchido(semestre)) {
//				sql.append(" and semestrevigente = '").append(semestre).append("' ");
//			}
//			sql.append(" and tag.turma = turma.codigo and t.situacao = 'AB' ");
//			sql.append(" union all ");
//			sql.append(" select tag.turma from horarioturma inner join turma as t on t.codigo = horarioturma.turma ");
//			sql.append(" inner join turmaagrupada as tag on tag.turmaOrigem = t.codigo ");
//			sql.append(" inner join horarioturmadia htd on htd.horarioturma = horarioturma.codigo ");
//			sql.append(" inner join horarioturmadiaitem htdi on htdi.horarioturmadia = htd.codigo and htdi.disciplina is not null ");
//			sql.append(" and anovigente = '").append(ano).append("' ");
//			if(Uteis.isAtributoPreenchido(semestre)) {
//				sql.append(" and semestrevigente = '").append(semestre).append("' ");
//			}
//			sql.append(" and tag.turma in ( select tsub.codigo from turma as tsub where tsub.subturma = true and tsub.tiposubturma in ('PRATICA', 'TEORICA') and tsub.turmaprincipal = turma.codigo and tsub.situacao = 'AB') and t.situacao = 'AB' limit 1 ");
//			sql.append(" ) ");
		}
		
//		if ((novaMatricula || renovandoMatricula) && (usuario == null || (usuario != null && !usuario.getIsApresentarVisaoAdministrativa())) && !configuracaoFinanceiro.getUtilizarIntegracaoFinanceira()) {
//			sql.append(" and ((Turma.planoFinanceiroCurso is not null  and exists (select condicaopagamentoplanofinanceirocurso.codigo from condicaopagamentoplanofinanceirocurso ");
//			sql.append(" where condicaopagamentoplanofinanceirocurso.planoFinanceiroCurso = Turma.planoFinanceiroCurso and condicaopagamentoplanofinanceirocurso.situacao = 'AT' ");
//			if (usuario != null && (usuario.getIsApresentarVisaoAluno() || usuario.getIsApresentarVisaoPais())) {
//				sql.append(" and condicaopagamentoplanofinanceirocurso.apresentarCondicaoVisaoAluno ");
//				sql.append(" and (turma.categoriacondicaopagamento = '' or turma.categoriacondicaopagamento is null or turma.categoriaCondicaoPagamento = condicaopagamentoplanofinanceirocurso.categoria) ");
//			} else if (usuario != null && (usuario.getIsApresentarVisaoProfessor())) {
//				sql.append(" and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineProfessor ");
//				sql.append(" and (turma.categoriacondicaopagamento = '' or turma.categoriacondicaopagamento is null or turma.categoriaCondicaoPagamento = condicaopagamentoplanofinanceirocurso.categoria) ");
//			} else if (usuario != null && (usuario.getIsApresentarVisaoCoordenador())) {
//				sql.append(" and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineCoordenador ");
//				sql.append(" and (turma.categoriacondicaopagamento = '' or turma.categoriacondicaopagamento is null or turma.categoriaCondicaoPagamento = condicaopagamentoplanofinanceirocurso.categoria) ");
//			} else if (!Uteis.isAtributoPreenchido(usuario) || (usuario != null && !Uteis.isAtributoPreenchido(usuario.getVisaoLogar()))) {
//				sql.append(" and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineExterna ");
//				sql.append(" and (turma.categoriacondicaopagamento = '' or turma.categoriacondicaopagamento is null or turma.categoriaCondicaoPagamento = condicaopagamentoplanofinanceirocurso.categoria) ");
//			}
//			sql.append(" limit 1))  ");
//			sql.append(" or (Turma.planoFinanceiroCurso is null and  exists ");
//			sql.append(" (select planoFinanceiroCurso from unidadeensinocurso where unidadeensinocurso.curso = curso.codigo and unidadeensinocurso.turno = Turno.codigo ");
//			sql.append(" and unidadeensinocurso.unidadeensino = unidadeensino.codigo ");
//			sql.append(" and exists (select condicaopagamentoplanofinanceirocurso.codigo from condicaopagamentoplanofinanceirocurso ");
//			sql.append(" where condicaopagamentoplanofinanceirocurso.planoFinanceiroCurso = unidadeensinocurso.planoFinanceiroCurso and condicaopagamentoplanofinanceirocurso.situacao = 'AT' ");
//			if (usuario != null && (usuario.getIsApresentarVisaoAluno() || usuario.getIsApresentarVisaoPais())) {
//				sql.append(" and condicaopagamentoplanofinanceirocurso.apresentarCondicaoVisaoAluno ");
//				sql.append(" and (turma.categoriacondicaopagamento = '' or turma.categoriacondicaopagamento is null or turma.categoriaCondicaoPagamento = condicaopagamentoplanofinanceirocurso.categoria) ");
//			} else if (usuario != null && (usuario.getIsApresentarVisaoProfessor())) {
//				sql.append(" and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineProfessor ");
//				sql.append(" and (turma.categoriacondicaopagamento = '' or turma.categoriacondicaopagamento is null or turma.categoriaCondicaoPagamento = condicaopagamentoplanofinanceirocurso.categoria) ");
//			} else if (usuario != null && (usuario.getIsApresentarVisaoCoordenador())) {
//				sql.append(" and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineCoordenador ");
//				sql.append(" and (turma.categoriacondicaopagamento = '' or turma.categoriacondicaopagamento is null or turma.categoriaCondicaoPagamento = condicaopagamentoplanofinanceirocurso.categoria) ");
//			} else if (!Uteis.isAtributoPreenchido(usuario) || (usuario != null && !Uteis.isAtributoPreenchido(usuario.getVisaoLogar()))) {
//				sql.append(" and condicaopagamentoplanofinanceirocurso.apresentarMatriculaOnlineExterna ");
//				sql.append(" and (turma.categoriacondicaopagamento = '' or turma.categoriacondicaopagamento is null or turma.categoriaCondicaoPagamento = condicaopagamentoplanofinanceirocurso.categoria) ");
//			}
//			sql.append(" limit 1)  ");
//			sql.append(" limit 1))");
//			sql.append(" ) ");
			if (trazerApenasTurmasApresentarRenovacaoOnline) {
				sql.append(" and turma.apresentarrenovacaoonline ");				
				
			}
//		}
		sql.append(" ORDER BY turma.identificadorTurma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorPeriodoLetivoUnidadeEnsinoCursoTurno(Integer peridoLetivo, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE periodoletivo.codigo = ").append(peridoLetivo.intValue()).append(" ");
		sql.append(" and Turma.situacao = 'AB' ");
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
    		sql.append(" and unidadeensino.codigo = ").append(unidadeEnsino);
    	}
    	if(Uteis.isAtributoPreenchido(curso)){
    		sql.append(" and Curso.codigo = ").append(curso);
    	}
    	if(Uteis.isAtributoPreenchido(turno)){
    		sql.append(" and Turno.codigo = ").append(turno);
    	}
		sql.append(" and Turma.subturma = false ");
		sql.append(" ORDER BY curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
		// "SELECT Turma.codigo, Turma.semestral, Turma.anual,
		// Turma.identificadorturma, Turma.turmaagrupada, Turma.sala, ");
		// sql.append("Unidadeensino.codigo as \"Unidadeensino.codigo\" ,
		// Unidadeensino.nome as \"Unidadeensino.nome\", ");
		// sql.append("Curso.codigo as \"Curso.codigo\", Curso.nome as
		// \"Curso.nome\", ");
		// sql.append("Turno.codigo as \"Turno.codigo\", Turno.nome as
		// \"Turno.nome\", ");
		// sql.append("Periodoletivo.descricao as
		// \"Periodoletivo.descricao\",Periodoletivo.codigo as
		// \"Periodoletivo.codigo\"

		// String sqlStr = "SELECT Turma.* FROM Turma Where periodoLetivo = " +
		// peridoLetivo.intValue() + " and curso = " + curso.intValue() +
		// " and turno = " + turno.intValue() + "";
		// if (unidadeEnsino.intValue() != 0) {
		// sqlStr += " and unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		// }
		//
		// SqlRowSet tabelaResultado =
		// getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		// return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorGradeCurricular(Integer gradeCurricular, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE (turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(") ");
		sql.append("   and (Turma.gradeCurricular = ").append(gradeCurricular.intValue()).append(") ");
		sql.append("   and (Curso.codigo = ").append(curso.intValue()).append(") ");
		sql.append("   and (Turno.codigo = ").append(turno.intValue()).append(") ");
		sql.append(" ORDER BY curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultarPorPeriodoLetivoUnidadeEnsinoCursoTurno(Integer peridoLetivo, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Turma.* FROM Turma " + " Where periodoLetivo = " + peridoLetivo.intValue() + " and curso = " + curso.intValue() + " and turno = " + turno.intValue() + "";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultarPorPeriodoLetivoPorIdentificadorPeriodoLetivoEUnidadeEnsinoCursoTurno(Integer peridoLetivo, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Turma.* FROM Turma " + " inner join PeriodoLetivo on turma.periodoLetivo = periodoLetivo.codigo " + " Where periodoLetivo.periodoLetivo = '" + peridoLetivo + "' and curso = " + curso.intValue() + " and turno = " + turno.intValue() + "";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultarPorPeriodoLetivoUnidadeEnsinoCurso(Integer peridoLetivo, Integer unidadeEnsino, Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Turma.* FROM Turma Where periodoLetivo = " + peridoLetivo.intValue() + " and curso = " + curso.intValue() + "";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultarPorPeriodoLetivoUnidadeEnsinoCurso2(Integer peridoLetivo, Integer unidadeEnsino, Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Turma.* FROM Turma" + " inner join periodoLetivo on turma.periodoLetivo = periodoLetivo.codigo " + " Where periodoLetivo.periodoLetivo = " + peridoLetivo.intValue() + " and curso = " + curso.intValue() + "";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultarPorPeriodoLetivoGradeCurricularUnidadeEnsinoCurso(Integer peridoLetivo, Integer unidadeEnsino, Integer curso, Integer gradecurricular, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Turma.* FROM Turma" + " inner join periodoLetivo on turma.periodoLetivo = periodoLetivo.codigo " + " Where periodoLetivo.periodoLetivo = " + peridoLetivo.intValue() + " and curso = " + curso.intValue() + " " + " and periodoletivo.gradecurricular = " + gradecurricular.intValue() + " ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultarPorDisciplinaEquivalenteSituacaoUnidadeEnsinoCurso(Integer disciplina, String situacao, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT turma.* FROM turma LEFT JOIN gradedisciplina ON gradedisciplina.periodoletivo = turma.periodoletivo";
		sqlStr += " LEFT JOIN disciplina ON disciplina.codigo = gradedisciplina.disciplina";
		sqlStr += " WHERE disciplina.codigo = " + disciplina + " AND turma.situacao = '" + situacao + "'";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " AND unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += " ORDER BY turma.identificadorturma;";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * responsável por realizar uma consulta de <code>Turma</code> através do valor do atributo <code>nome</code> da classe <code>Curso</code> Faz uso da Operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vÃ¯Â¿Â½rios objetos da classe <code>TurmaVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restriÃ§Ã£oo de acesso.
	 */
	public List<TurmaVO> consultarPorNomeCurso(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Turma.* FROM Turma, Curso WHERE turma.curso = curso.codigo and lower (sem_acentos(Curso.nome)) ilike(sem_acentos('" + valorConsulta.toLowerCase() + "%')) ORDER BY Curso.nome";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT Turma.* FROM Turma, Curso WHERE turma.curso = curso.codigo and lower (sem_acentos(Curso.nome)) ilike(sem_acentos('" + valorConsulta.toLowerCase() + "%')) and turma.unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY Curso.nome";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultaRapidaNomeCurso(String valorConsulta, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaNomeCurso(valorConsulta, 0, unidadeEnsino, nivelEducacionalPosGraduacao, nivelEducacionalDiferentePosGraduacao, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultaRapidaNomeCurso(String valorConsulta, List<UnidadeEnsinoVO> unidades, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaNomeCurso(valorConsulta, 0, unidades, nivelEducacionalPosGraduacao, nivelEducacionalDiferentePosGraduacao, controlarAcesso, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultaRapidaNomeCurso(String valorConsulta, Integer turmaOrigem, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE (sem_acentos(curso.nome)) ilike (sem_acentos(?)) ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX', 'MT'))");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND curso.nivelEducacional != 'PO'");
		}
		if (turmaOrigem.intValue() != 0) {
			sql.append(" and turma.codigo <> ");
			sql.append(turmaOrigem.intValue());
		}
		sql.append(" ORDER BY curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaNomeCurso(String valorConsulta, Integer turmaOrigem, List<UnidadeEnsinoVO> unidades, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE lower(sem_acentos(curso.nome)) ilike (sem_acentos('");
		sql.append(valorConsulta.toLowerCase());
		sql.append("%'))");
		StringBuilder sqlUnidade = new StringBuilder();
		sqlUnidade.append(" AND (turma.unidadeensino in (");
		String auxUnidade = "";
		for (UnidadeEnsinoVO unidade : unidades) {
			if (unidade.getFiltrarUnidadeEnsino()) {
				sqlUnidade.append(auxUnidade).append(unidade.getCodigo());
				auxUnidade = ",";
			}
		}
		sqlUnidade.append(")) ");
		if (auxUnidade.equals(",")) {
			sql.append(sqlUnidade);
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX')  OR turma.turmaAgrupada)");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND curso.nivelEducacional != 'PO'");
		}
		if (turmaOrigem.intValue() != 0) {
			sql.append(" and turma.codigo <> ");
			sql.append(turmaOrigem.intValue());
		}
		sql.append(" ORDER BY curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaNomeCursoPorCodigoUnidadeEnsinoTurmaPrincipalECodigoCursoTurmaPrincipal(String valorConsulta, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE lower(sem_acentos(curso.nome)) ilike (sem_acentos('");
		sql.append(valorConsulta.toLowerCase());
		sql.append("%'))");
		if (curso.intValue() != 0) {
			sql.append(" and turma.curso = ");
			sql.append(curso.intValue());
		}
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		sql.append(" ORDER BY curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaNomeCursoNivelEducacional(String valorConsulta, List<ProgramacaoFormaturaUnidadeEnsinoVO> prograFormaturaUnidadeEnsinoVOs, String nivelEducacional, List<TurnoVO> turnoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE sem_acentos(curso.nome) ilike (sem_acentos(?)) ");
		if (Uteis.isAtributoPreenchido(prograFormaturaUnidadeEnsinoVOs)) {
			sql.append(" AND turma.unidadeEnsino in (");
			for (ProgramacaoFormaturaUnidadeEnsinoVO programacaoFormaturaUnidadeEnsinoVO : prograFormaturaUnidadeEnsinoVOs) {
				if (!prograFormaturaUnidadeEnsinoVOs.get(prograFormaturaUnidadeEnsinoVOs.size() - 1).getUnidadeEnsinoVO().getCodigo().equals(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo())) {
					sql.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo()+", ");
				} else {
					sql.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo());
				}
			}
			sql.append(") ");
		}
		if (Uteis.isAtributoPreenchido(turnoVOs)) {
			sql.append(" AND turno.codigo in (");
			for (TurnoVO turno : turnoVOs) {
				if (!turnoVOs.get(turnoVOs.size() - 1).getCodigo().equals(turno.getCodigo())) {
					sql.append(turno.getCodigo()+", ");
				} else {
					sql.append(turno.getCodigo());
				}
			}
			sql.append(") ");
		}
		if (!nivelEducacional.equals("")) {
			sql.append(" AND curso.nivelEducacional in ('").append(nivelEducacional.toUpperCase()).append("') ");
		}
		sql.append(" ORDER BY curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultarPorCurso(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT turma.* FROM turma inner join curso on turma.curso = curso.codigo WHERE 1=1 ";
		if (valorConsulta != 0) {
			sqlStr += " and curso.codigo = " + valorConsulta.intValue();
		}
		if (unidadeEnsino != 0) {
			sqlStr += " and turma.unidadeensino = " + unidadeEnsino.intValue();
		}
		sqlStr += " ORDER BY curso.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultarPorNomeCursoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> lista = new ArrayList<UnidadeEnsinoVO>();
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			lista.add(unidadeEnsinoVO);
			return this.consultarPorNomeCursoUnidadeEnsino(valorConsulta, lista, controlarAcesso, nivelMontarDados, usuario);
		}
		return new ArrayList<TurmaVO>(0);
	}

	public List<TurmaVO> consultarPorNomeCursoUnidadeEnsino(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinos, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			StringBuilder sql = getSQLPadraoConsultaBasica();
			sql.append(" WHERE upper(sem_acentos(curso.nome)) like upper(sem_acentos(?))");
			if (!unidadeEnsinos.isEmpty()) {
				sql.append(" AND turma.unidadeensino in (");
				for (UnidadeEnsinoVO unidade : unidadeEnsinos) {
					sql.append(unidade.getCodigo() + ", ");
				}
				sql.append(" 0)");
			}
			sql.append(" ORDER BY curso.nome ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta + PERCENT);
			return montarDadosConsultaBasica(tabelaResultado);
		} else {
			String sqlStr = "SELECT distinct Turma.* FROM Turma, Curso, UnidadeEnsino WHERE turma.curso = curso.codigo  " + " and lower (sem_acentos(Curso.nome)) like lower(sem_acentos(?)) "; 
			if (!unidadeEnsinos.isEmpty()) {
				sqlStr += " AND turma.unidadeensino in (";
				for (UnidadeEnsinoVO unidade : unidadeEnsinos) {
					sqlStr += (unidade.getCodigo() + ", ");
				}
				sqlStr += (" 0)");
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
			return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		}
	}

	public List<TurmaVO> consultarPorUnidadeEnsinoCursoTurno(Integer codigoUnidadeEnsino, Integer codigoCurso, Integer codigoTurno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Turma.* FROM Turma WHERE curso = " + codigoCurso.intValue() + " and turno = " + codigoTurno.intValue() + " ";
		if (codigoUnidadeEnsino.intValue() != 0) {
			sqlStr += " and unidadeEnsino = " + codigoUnidadeEnsino.intValue();
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultarPorUnidadeEnsinoCursoTurnoPeriodoLetivo(Integer codigoUnidadeEnsino, Integer codigoCurso, Integer codigoTurno, Integer codigoPeriodoLetivo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Turma.* FROM Turma WHERE curso = " + codigoCurso.intValue() + " and turno = " + codigoTurno.intValue() + " AND periodoletivo = " + codigoPeriodoLetivo + " ";
		if (codigoUnidadeEnsino.intValue() != 0) {
			sqlStr += " and unidadeEnsino = " + codigoUnidadeEnsino.intValue();
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultarPorUnidadeEnsinoCurso(Integer codigoUnidadeEnsino, Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT distinct t1.* FROM Turma as t1 " + "left join turmaAgrupada on turmaAgrupada.turmaOrigem = t1.codigo " + "left join turma as t2 on t2.codigo = turmaAgrupada.turma ";
		if (codigoUnidadeEnsino.intValue() != 0) {
			sqlStr += "where (T1.unidadeEnsino = " + codigoUnidadeEnsino.intValue() + " and T1.Curso = " + codigoCurso.intValue() + " ) or (T2.unidadeEnsino = " + codigoUnidadeEnsino.intValue() + " and T2.Curso = " + codigoCurso.intValue() + ") ";
		} else {
			sqlStr += "where (T1.Curso = " + codigoCurso.intValue() + " ) or (T2.Curso = " + codigoCurso.intValue() + ") ";
		}
		sqlStr += "order by t1.identificadorTurma";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultarRapidaUnidadeEnsinoCursoComboBox(Integer codigoUnidadeEnsino, Integer codigoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT distinct t1.codigo, t1.identificadorTurma FROM Turma as t1 ");
		sqlStr.append("left join turmaAgrupada on turmaAgrupada.turmaOrigem = t1.codigo ");
		sqlStr.append("left join turma as t2 on t2.codigo = turmaAgrupada.turma ");
		if (codigoUnidadeEnsino.intValue() != 0) {
			sqlStr.append(" where (T1.unidadeEnsino = ");
			sqlStr.append(codigoUnidadeEnsino.intValue());
			sqlStr.append(" and T1.Curso = ");
			sqlStr.append(codigoCurso.intValue());
			sqlStr.append(" ) or (T2.unidadeEnsino = ");
			sqlStr.append(codigoUnidadeEnsino.intValue());
			sqlStr.append(" and T2.Curso = ");
			sqlStr.append(codigoCurso.intValue());
			sqlStr.append(" )");
		} else {
			sqlStr.append(" where (T1.Curso = ");
			sqlStr.append(codigoCurso.intValue());
			sqlStr.append(") or (T2.Curso = ");
			sqlStr.append(codigoCurso.intValue());
			sqlStr.append(")");
		}
		sqlStr.append(" order by t1.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaComboBox(tabelaResultado, nivelMontarDados);
	}

	public List<TurmaVO> consultarPorCodigoUnidadeEnsinoCurso(Integer codigoUnidadeEnsinoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT turma.* FROM turma ";
		sqlStr += "INNER JOIN unidadeEnsinoCurso ON unidadeEnsinoCurso.unidadeEnsino = turma.unidadeEnsino AND unidadeEnsinoCurso.curso = Turma.curso AND unidadeEnsinoCurso.turno = Turma.turno ";
		if (codigoUnidadeEnsinoCurso.intValue() != 0) {
			sqlStr += "WHERE unidadeEnsinoCurso.codigo = " + codigoUnidadeEnsinoCurso.intValue();
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<TurmaVO> consultarPorCodigoCurso(Integer codigoUnidadeEnsinoCurso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT turma.* FROM turma ";
		sqlStr += "INNER JOIN unidadeEnsinoCurso ON unidadeEnsinoCurso.unidadeEnsino = turma.unidadeEnsino AND unidadeEnsinoCurso.curso = Turma.curso AND unidadeEnsinoCurso.turno = Turma.turno ";
		if (codigoUnidadeEnsinoCurso.intValue() != 0) {
			sqlStr += "WHERE unidadeEnsinoCurso.curso = " + codigoUnidadeEnsinoCurso.intValue();
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultarPorCodigoUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Turma.* FROM Turma WHERE unidadeEnsino = " + codigoUnidadeEnsino.intValue();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultarPorDisciplinaSituacaoNrVagas(Integer disciplina, String situacao, Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT Turma.* FROM Turma " + " LEFT JOIN turmadisciplina ON turmadisciplina.turma = turma.codigo" + " WHERE turmadisciplina.disciplina = " + disciplina.intValue() + " AND turma.situacao = '" + situacao + "' AND turma.nrvagas >=1 ";
		if (codigoUnidadeEnsino.intValue() != 0 && codigoUnidadeEnsino != null) {
			sqlStr += " AND unidadeEnsino = " + codigoUnidadeEnsino.intValue();
		}
		sqlStr += " ORDER BY turma.identificadorturma;";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultarPorDisciplinaSituacaoNrVagasIncluindoTurmasLotadas(Integer disciplina, String situacao, Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = " SELECT DISTINCT Turma.* FROM Turma " + " LEFT JOIN turmadisciplina ON turmadisciplina.turma = turma.codigo" + " WHERE turmadisciplina.disciplina = " + disciplina.intValue() + " AND turma.situacao = '" + situacao + "' ";
		if (codigoUnidadeEnsino.intValue() != 0 && codigoUnidadeEnsino != null) {
			sqlStr += " AND unidadeEnsino = " + codigoUnidadeEnsino.intValue();
		}
		sqlStr += "  ORDER BY turma.identificadorturma;";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * responsável por realizar uma consulta de <code>Turma</code> através do valor do atributo <code>String identificadorTurma</code>. Retorna os objetos, com inÃ¯Â¿Â½cio do valor do atributo idÃ¯Â¿Â½ntico ao parÃ¯Â¿Â½metro fornecido. Faz uso da Operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicaÃ§Ã£oo deverÃ¯Â¿Â½ verificar se o usuÃ¡rio possui permissÃ¯Â¿Â½o para esta consulta ou nÃ£o.
	 * @return List Contendo vÃ¯Â¿Â½rios objetos da classe <code>TurmaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restriÃ§Ã£oo de acesso.
	 */
	public List<TurmaVO> consultarPorIdentificadorTurma(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Turma WHERE lower (identificadorTurma) like('" + valorConsulta.toLowerCase() + "%') ORDER BY identificadorTurma";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM Turma WHERE lower (identificadorTurma) like('" + valorConsulta.toLowerCase() + "%') and unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY identificadorTurma";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<TurmaVO> consultarPorDisciplinaMatricula(Integer codDisciplina, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select distinct(turma.*) from matriculaperiodoturmadisciplina, turma " + " where matriculaperiodoturmadisciplina.turma = turma.codigo " + " and matriculaperiodoturmadisciplina.disciplina = " + codDisciplina + " and matriculaperiodoturmadisciplina.matricula = '" + matricula + "'";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public TurmaVO consultaRapidaPorDisciplinaMatriculaPeriodo(Integer codDisciplina, Integer matriculaPeriodo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select turma.codigo, turma.identificadorTurma from matriculaperiodoturmadisciplina ");
		sqlStr.append("inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		sqlStr.append("where matriculaperiodoturmadisciplina.disciplina = ").append(codDisciplina);
		sqlStr.append(" and matriculaperiodoturmadisciplina.matriculaPeriodo = ").append(matriculaPeriodo);
		sqlStr.append(" order by matriculaperiodoturmadisciplina.codigo desc limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		TurmaVO obj = new TurmaVO();
		if (tabelaResultado.next()) {
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
		}
		return obj;
	}

	public List<TurmaVO> consultaRapidaPorDisciplinaMatricula(Integer codDisciplina, String matricula, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select distinct(turma.codigo) ");
		sqlStr.append(" from matriculaperiodoturmadisciplina, turma ");
		sqlStr.append(" where matriculaperiodoturmadisciplina.turma = turma.codigo ");
		sqlStr.append(" and (matriculaperiodoturmadisciplina.disciplina = ").append(codDisciplina);
		sqlStr.append(" or matriculaperiodoturmadisciplina.disciplina in (select distinct disciplina from disciplinacomposta where composta = ").append(codDisciplina).append("limit 1)");
		sqlStr.append(" or matriculaperiodoturmadisciplina.disciplina in (select distinct equivalente from disciplinaequivalente where disciplina = ").append(codDisciplina).append("limit 1))");
		sqlStr.append(" and matriculaperiodoturmadisciplina.matricula = '").append(matricula).append("' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			carregarDados(obj, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<TurmaVO> consultarPorDisciplina(Integer codDisciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "select distinct(turma.*) from matriculaperiodoturmadisciplina, turma " + " where matriculaperiodoturmadisciplina.turma = turma.codigo " + " and matriculaperiodoturmadisciplina.disciplina = " + codDisciplina;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<TurmaVO> consultaRapidaPorDisciplina(Integer codDisciplina, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT turma.codigo, turma.identificadorturma FROM Turma " + "INNER JOIN TurmaDisciplina ON (TurmaDisciplina.turma = Turma.codigo) " + "WHERE TurmaDisciplina.disciplina = " + codDisciplina + " AND turma.situacao = '" + situacao + "' ";
		if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
			sqlStr += " and turma.unidadeEnsino = " + unidadeEnsino;
		}
		sqlStr += " order by turma.identificadorTurma ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			carregarDados(obj, NivelMontarDados.BASICO, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<TurmaVO> consultaRapidaPorDisciplinaEquivalenteUnidadeEnsino(Integer codDisciplina, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT(turma.codigo), identificadorturma FROM Turma " + "INNER JOIN TurmaDisciplina ON (TurmaDisciplina.turma = Turma.codigo) " + "WHERE (TurmaDisciplina.disciplina = " + codDisciplina + ")";
		if ((unidadeEnsino != null) && (!unidadeEnsino.equals(0))) {
			sqlStr += " AND (turma.unidadeEnsino = " + unidadeEnsino + ") order by identificadorturma";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			carregarDados(obj, NivelMontarDados.BASICO, usuario);
			Date dataPrimeiraAula = consultaRapidaHorarioTurmaDiaPorDisciplinaTurma(codDisciplina, obj.getCodigo(), "AB", controlarAcesso, usuario);
			if (dataPrimeiraAula != null) {
				int qtdDias = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuario).getQtdDiasMaximoAntecedenciaRemarcarAulaReposicao();
				if (qtdDias > 0) {
					Date dataMaxima = Uteis.getDataPassada(dataPrimeiraAula, qtdDias);
					if (dataMaxima.after(new Date())) {
						obj.setDataPrimeiraAulaProgramada(Uteis.getData(dataPrimeiraAula));
						vetResultado.add(obj);
					}
				} else {
					obj.setDataPrimeiraAulaProgramada(Uteis.getData(dataPrimeiraAula));
					vetResultado.add(obj);
				}
			} else {
				obj.setDataPrimeiraAulaProgramada("");
			}
		}
		return vetResultado;
	}

	public List<TurmaVO> consultaRapidaPorDisciplinaDataAula(Integer codDisciplina, Integer cargaHoraria, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT turma.codigo, turma.identificadorturma , " + "case when TurmaDisciplina.GradeCurricularGrupoOptativaDisciplina !=0 then GradeCurricularGrupoOptativaDisciplina.cargaHoraria else GradeDisciplina.cargaHoraria end as cargaHoraria FROM Turma " + "INNER JOIN TurmaDisciplina ON (TurmaDisciplina.turma = Turma.codigo)  " + "LEFT JOIN GradeDisciplina ON (TurmaDisciplina.gradeDisciplina = GradeDisciplina.codigo)  " + "LEFT JOIN GradeCurricularGrupoOptativaDisciplina ON (TurmaDisciplina.gradeCurricularGrupoOptativaDisciplina = GradeCurricularGrupoOptativaDisciplina.codigo)   " + "WHERE TurmaDisciplina.disciplina = " + codDisciplina + " AND turma.situacao = '" + situacao + "' and turmadisciplina.permiteReposicao = true ";
		if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
			sqlStr += " and turma.unidadeEnsino = " + unidadeEnsino;
		}
		sqlStr += " order by turma.identificadorTurma ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			Integer cargaHorariaTurma = tabelaResultado.getInt("cargaHoraria");
			if (cargaHorariaTurma.equals(cargaHoraria)) {
				TurmaVO obj = new TurmaVO();
				obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
				carregarDados(obj, NivelMontarDados.BASICO, usuario);
				Date dataPrimeiraAula = consultaRapidaHorarioTurmaDiaPorDisciplinaTurma(codDisciplina, obj.getCodigo(), situacao, controlarAcesso, usuario);
				if (dataPrimeiraAula != null) {
					int qtdDias = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(usuario).getQtdDiasMaximoAntecedenciaRemarcarAulaReposicao();
					if (qtdDias > 0) {
						Date dataMaxima = Uteis.getDataPassada(dataPrimeiraAula, qtdDias);
						if (dataMaxima.after(new Date())) {
							obj.setDataPrimeiraAulaProgramada(Uteis.getData(dataPrimeiraAula));
							vetResultado.add(obj);
						}
					} else {
						obj.setDataPrimeiraAulaProgramada(Uteis.getData(dataPrimeiraAula));
						vetResultado.add(obj);
					}
				} else {
					obj.setDataPrimeiraAulaProgramada("");
				}
			}
		}
		return vetResultado;
	}

	/*
	 * Consultar para preencher dados da turma como data inicio, data fim, qtd alunos matricula, pre-matriculados e confirmados.
	 */
	public TurmaVO consultaRapidaDadosIntegracao(TurmaVO turma) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT DISTINCT min(horarioturmadia.data) AS dataInicio, max(horarioturmadia.data) AS dataFinal, ");
		sqlStr.append(" (select count(matricula) from matriculaperiodo where turma = ").append(turma.getCodigo()).append(" and situacaomatriculaperiodo not in ('TR', 'FO', 'FI', 'CA', 'TI', 'PC', 'AC') ) as matriculados, ");
		sqlStr.append(" (select count(matricula) from matriculaperiodo where turma = ").append(turma.getCodigo()).append(" and situacao in ('CO', 'AT') and situacaomatriculaperiodo not in ('TR', 'FO', 'FI', 'CA', 'TI', 'PC', 'AC')) as confirmados, ");
		sqlStr.append(" (select count(matricula) from matriculaperiodo where matriculaperiodo.situacao in ('AT' , 'PF', 'CO')and matriculaperiodo.situacaomatriculaperiodo = 'PR' and matriculaperiodo.turma = ").append(turma.getCodigo()).append(") as prematriculados ");
		sqlStr.append(" FROM turma ");
		sqlStr.append(" INNER JOIN LATERAL ( ");
		sqlStr.append(" SELECT  turmanormal.codigo AS turma FROM turma AS turmanormal  WHERE turmanormal.codigo = ").append(turma.getCodigo());
		sqlStr.append(" UNION ALL ");
		sqlStr.append(" SELECT  ta.codigo AS turma  FROM turma AS ta  ");
		sqlStr.append("  WHERE ta.codigo IN (SELECT turmaagrupada.turmaorigem FROM turmaagrupada WHERE turmaagrupada.turma =  ").append(turma.getCodigo()).append(" )");
		sqlStr.append("  ) AS turmas_temp 		ON turma.codigo = turmas_temp.turma  ");
		sqlStr.append(" INNER JOIN TurmaDisciplina  ON TurmaDisciplina.turma 		= Turma.codigo  ");
		sqlStr.append(" INNER JOIN horarioturma 	ON horarioturma.turma 			= turma.codigo ");
		sqlStr.append(" INNER JOIN horarioturmadia  ON horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append(" WHERE turma.situacao = 'AB' AND horarioturmadia.data IS NOT NULL ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			turma.setDataPrimeiraAulaProgramada(Uteis.getData(tabelaResultado.getDate("dataInicio")));
			turma.setDataPrevisaoFinalizacao(tabelaResultado.getDate("dataFinal"));
			turma.setQtdConfirmados(tabelaResultado.getInt("confirmados"));
			turma.setQtdMatriculados(tabelaResultado.getInt("matriculados"));
			turma.setQtdPreMatriculados(tabelaResultado.getInt("prematriculados"));
		}
		return turma;
	}

	public Integer consultaCargaHorariaRapidaPorDisciplinaTurmaDataAula(Integer turma, Integer codDisciplina, Integer cargaHoraria, Integer unidadeEnsino, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT turma.codigo, turma.identificadorturma, " + "case when TurmaDisciplina.GradeCurricularGrupoOptativaDisciplina !=0 then GradeCurricularGrupoOptativaDisciplina.cargaHoraria else GradeDisciplina.cargaHoraria end as cargaHoraria FROM Turma " + "INNER JOIN TurmaDisciplina ON (TurmaDisciplina.turma = Turma.codigo)  " + "LEFT JOIN GradeDisciplina ON (TurmaDisciplina.gradeDisciplina = GradeDisciplina.codigo)  " + "LEFT JOIN GradeCurricularGrupoOptativaDisciplina ON (TurmaDisciplina.gradeCurricularGrupoOptativaDisciplina = GradeCurricularGrupoOptativaDisciplina.codigo)   " + "WHERE TurmaDisciplina.disciplina = " + codDisciplina + " AND turma.situacao = '" + situacao + "' and turma.codigo = " + turma;
		if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
			sqlStr += " and turma.unidadeEnsino = " + unidadeEnsino;
		}
		sqlStr += " order by turma.identificadorTurma limit 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("cargaHoraria");
		}
		return 0;
	}

	public Date consultaRapidaHorarioTurmaDiaPorDisciplinaTurma(Integer codDisciplina, Integer turma, String situacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT DISTINCT min(horarioturmadia.data) AS data FROM Turma ");
		sqlStr.append(" INNER JOIN TurmaDisciplina ON TurmaDisciplina.turma = Turma.codigo ");
		sqlStr.append(" INNER JOIN horarioturma on horarioturma.turma = turma.codigo ");
		sqlStr.append(" INNER JOIN horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append(" INNER JOIN horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sqlStr.append(" WHERE TurmaDisciplina.disciplina = ").append(codDisciplina).append(" AND turma.situacao = 'AB' and horarioturmadia.data is not null ");
		sqlStr.append(" AND horarioturmadiaitem.disciplina  = ").append(codDisciplina).append(" ");
		sqlStr.append(" AND horarioturmadia.data >= '").append(Uteis.getDataJDBC(new Date())).append("' ");
		sqlStr.append(" AND ((turma.turmaagrupada = false and turma.codigo = ").append(turma).append(") or (turma.turmaagrupada = true and exists (select turmaorigem from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and turmaagrupada.turma = ").append(turma).append(")))"); 
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			return tabelaResultado.getDate("data");
		}
		return null;
	}

	public List<TurmaVO> consultaRapidaPorDisciplina(Integer codDisciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		// String sqlStr =
		// "select distinct(turma.codigo) from matriculaperiodoturmadisciplina,
		// turma "
		// + " where matriculaperiodoturmadisciplina.turma = turma.codigo "
		// + " and matriculaperiodoturmadisciplina.disciplina = " +
		// codDisciplina;
		String sqlStr = "SELECT DISTINCT(turma.codigo) FROM Turma " + "INNER JOIN TurmaDisciplina ON (TurmaDisciplina.turma = Turma.codigo) " + "WHERE TurmaDisciplina.disciplina = " + codDisciplina;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			carregarDados(obj, NivelMontarDados.BASICO, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCodigoDisciplina(String nomeTurma, Integer codDisciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT(turma.codigo) FROM Turma ");
		sqlStr.append("INNER JOIN TurmaDisciplina ON (TurmaDisciplina.turma = Turma.codigo) ");
		sqlStr.append("WHERE TurmaDisciplina.disciplina = ").append(codDisciplina).append(" ");
		sqlStr.append("AND LOWER(turma.identificadorturma) like '").append(nomeTurma.toLowerCase()).append("%' ");
		if (Uteis.isAtributoPreenchido(usuario.getUnidadeEnsinoLogado())) {
		sqlStr.append(" AND turma.unidadeensino = ").append(usuario.getUnidadeEnsinoLogado().getCodigo());	
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			carregarDados(obj, NivelMontarDados.BASICO, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<TurmaVO> consultaRapidaPorDisciplinaUnidadeEnsino(Integer codDisciplina, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DISTINCT(turma.codigo), identificadorturma FROM Turma " + "INNER JOIN TurmaDisciplina ON (TurmaDisciplina.turma = Turma.codigo) " + "WHERE (TurmaDisciplina.disciplina = " + codDisciplina + ")";
		if ((unidadeEnsino != null) && (!unidadeEnsino.equals(0))) {
			sqlStr += " AND (turma.unidadeEnsino = " + unidadeEnsino + ") order by identificadorturma";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			carregarDados(obj, NivelMontarDados.BASICO, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Override
	public List<TurmaVO> consultaRapidaPorDisciplinaUnidadeEnsinoSituacao(Integer codDisciplina, Integer cargaHoraria, Integer unidadeEnsino, String situacaoTurma, boolean visaoAluno, Integer curso, boolean trazerSomenteTurmaComProgramacaoAula, String ano, String semestre, boolean trazerSubTurmaGeral, boolean trazerSomenteTurmaAulaNaoOcorreu, boolean trazerApenasTurmaReposicao, Boolean trazerSomenteTurmaComTutoriaOnline, boolean controlarAcesso, UsuarioVO usuario, Boolean validarAulaProgramadaCursoIntegral) throws Exception {
		return consultaRapidaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao(codDisciplina, cargaHoraria, unidadeEnsino, situacaoTurma, visaoAluno, curso, null, trazerSomenteTurmaComProgramacaoAula, ano, semestre, trazerSubTurmaGeral, trazerSomenteTurmaAulaNaoOcorreu, trazerApenasTurmaReposicao, trazerSomenteTurmaComTutoriaOnline, controlarAcesso, usuario, validarAulaProgramadaCursoIntegral, false, null);
	}
	
	@Override
	public boolean consultaSeExisteTurmaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao(Integer codDisciplina, Integer cargaHoraria, Integer unidadeEnsino, String situacaoTurma, boolean visaoAluno, Integer curso, Integer matrizCurricular, boolean trazerSomenteTurmaComProgramacaoAula, String ano, String semestre, boolean trazerSubTurmaGeral, boolean trazerSomenteTurmaAulaNaoOcorreu, boolean trazerApenasTurmaReposicao, Boolean trazerSomenteTurmaComTutoriaOnline, boolean controlarAcesso, UsuarioVO usuario, Boolean validarAulaProgramadaCursoIntegral ,Boolean apresentarRenovacaoOnline, MatriculaVO matriculaVO) throws Exception {
		getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "consultaSeExisteTurmaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao"+ matriculaVO.getMatricula());
		StringBuilder sqlStr = new StringBuilder("select * from (");
		sqlStr.append(getSqlConsultaRapidaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao(codDisciplina, cargaHoraria, unidadeEnsino, situacaoTurma, curso, matrizCurricular, trazerSomenteTurmaComProgramacaoAula, ano, semestre, trazerSubTurmaGeral, trazerSomenteTurmaAulaNaoOcorreu, trazerApenasTurmaReposicao, trazerSomenteTurmaComTutoriaOnline, apresentarRenovacaoOnline, usuario, matriculaVO));
		sqlStr.append(") as t limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "consultaSeExisteTurmaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao1"+ matriculaVO.getMatricula());
		return tabelaResultado.next();
	}
	
	@Override
	public List<TurmaVO> consultaTurmaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao(Integer codDisciplina, Integer cargaHoraria, Integer unidadeEnsino, String situacaoTurma, boolean visaoAluno, Integer curso, Integer matrizCurricular, boolean trazerSomenteTurmaComProgramacaoAula, String ano, String semestre, boolean trazerSubTurmaGeral, boolean trazerSomenteTurmaAulaNaoOcorreu, boolean trazerApenasTurmaReposicao, Boolean trazerSomenteTurmaComTutoriaOnline, boolean controlarAcesso, UsuarioVO usuario, Boolean validarAulaProgramadaCursoIntegral, Boolean apresentarRenovacaoOnline, MatriculaVO matriculaVO) throws Exception {
		getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "consultaTurmaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao"+ matriculaVO.getMatricula());
		List<TurmaVO> listaTurmaOfertada = new ArrayList<>();
		String key = "D" + codDisciplina + "CH" + cargaHoraria + "UN" + unidadeEnsino + "ST" + situacaoTurma + "CU" + curso + "MC" + matrizCurricular + "AN" + ano + "SE" + semestre +"TURMACOMPROGRAMACAOAULA"+trazerSomenteTurmaComProgramacaoAula+"RENOVACAOONLINE"+apresentarRenovacaoOnline;
		listaTurmaOfertada = getAplicacaoControle().obterAdicionarRemoverTurmaOfertada(key, listaTurmaOfertada, false, false);
		if (listaTurmaOfertada != null) {
			getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "consultaTurmaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao1"+ matriculaVO.getMatricula());
			return listaTurmaOfertada;
		} else {
			StringBuilder sqlStr = new StringBuilder(" WITH cte_turma AS ( ");
			sqlStr.append(" select codigo from (");
			sqlStr.append(getSqlConsultaRapidaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao(codDisciplina, cargaHoraria, unidadeEnsino, situacaoTurma, curso, matrizCurricular, trazerSomenteTurmaComProgramacaoAula, ano, semestre, trazerSubTurmaGeral, trazerSomenteTurmaAulaNaoOcorreu, trazerApenasTurmaReposicao, trazerSomenteTurmaComTutoriaOnline, apresentarRenovacaoOnline, usuario, matriculaVO));
			sqlStr.append(") as t ) ");
			sqlStr.append("SELECT Turma.codigo, Turma.semestral, Turma.anual, Turma.situacao, Turma.identificadorturma, Turma.turmaagrupada, Turma.sala, Turma.gradeCurricular, Turma.chancela, Turma.turmaPrincipal, ");
			sqlStr.append("Turma.nrvagas, Turma.nrmaximomatricula, Turma.nrminimomatricula, Turma.tipochancela, Turma.porcentagemchancela, Turma.valorfixochancela, Turma.observacao, Turma.valorporaluno, Turma.qtdAlunosEstimado, Turma.custoMedioAluno, Turma.receitaMediaAluno, chancela.instituicaoChanceladora, turma.utilizarDadosMatrizBoleto, turma.subturma, ");
			sqlStr.append("Unidadeensino.codigo as \"Unidadeensino.codigo\" , Unidadeensino.nome as \"Unidadeensino.nome\", turma.abreviaturaCurso, turma.nrVagasInclusaoReposicao, ");
			sqlStr.append("Curso.codigo as \"Curso.codigo\", Curso.nome as \"Curso.nome\", Curso.periodicidade as \"Curso.periodicidade\", Curso.nivelEducacional as \"Curso.nivelEducacional\", Curso.liberarregistroaulaentreperiodo AS \"Curso.liberarregistroaulaentreperiodo\", Curso.configuracaoacademico AS \"Curso.configuracaoacademico\", ");
			sqlStr.append("Turno.codigo as \"Turno.codigo\", Turno.nome as \"Turno.nome\", ");
			sqlStr.append("Periodoletivo.descricao as \"Periodoletivo.descricao\",Periodoletivo.codigo as \"Periodoletivo.codigo\",Periodoletivo.periodoLetivo as \"Periodoletivo.periodoLetivo\", ");
			sqlStr.append("Periodoletivo.nomeCertificacao as \"Periodoletivo.nomeCertificacao\",  ");
			sqlStr.append("gradecurricular.nome as \"gradecurricular.nome\", turma.tipoSubTurma, turma.apresentarRenovacaoOnline, turma.categoriaCondicaoPagamento, turma.considerarTurmaAvaliacaoInstitucional,  ");
			sqlStr.append("case when turma.subturma then turmaPrincipal.identificadorTurma::VARCHAR||'ZZZZ'||turma.identificadorTurma ");
			sqlStr.append("else turma.identificadorTurma end as ordenacao, turma.digitoTurma ");
			sqlStr.append("from cte_turma INNER JOIN turma ON cte_turma.codigo = turma.codigo  ");
			sqlStr.append("left join unidadeensino on turma.unidadeensino = unidadeensino.codigo ");
			sqlStr.append("left join curso on curso.codigo = turma.curso ");
			sqlStr.append("left join turno on turma.turno = turno.codigo ");
			sqlStr.append("left join periodoletivo on turma.periodoletivo = periodoletivo.codigo ");
			sqlStr.append("left join chancela on turma.chancela = chancela.codigo ");
			sqlStr.append("left join gradecurricular on turma.gradecurricular = gradecurricular.codigo ");
			sqlStr.append(" left join turma as turmaprincipal on turmaprincipal.codigo = turma.turmaprincipal ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			listaTurmaOfertada = montarDadosConsultaBasica(tabelaResultado);			
			getAplicacaoControle().obterAdicionarRemoverTurmaOfertada(key, listaTurmaOfertada, true, false);
		}
		getAplicacaoControle().realizarEscritaTextoDebug(AssuntoDebugEnum.GERAL, "consultaTurmaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao2"+ matriculaVO.getMatricula());
		return listaTurmaOfertada;

	}

	@Override
	public List<TurmaVO> consultaRapidaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao(Integer codDisciplina, Integer cargaHoraria, Integer unidadeEnsino, String situacaoTurma, boolean visaoAluno, Integer curso, Integer matrizCurricular, boolean trazerSomenteTurmaComProgramacaoAula, String ano, String semestre, boolean trazerSubTurmaGeral, boolean trazerSomenteTurmaAulaNaoOcorreu, boolean trazerApenasTurmaReposicao, Boolean trazerSomenteTurmaComTutoriaOnline, boolean controlarAcesso, UsuarioVO usuario, Boolean validarAulaProgramadaCursoIntegral, Boolean apresentarRenovacaoOnline, MatriculaVO matriculaVO) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);		
		StringBuilder sqlStr = new StringBuilder(" WITH cte_turma AS ( ");
		sqlStr.append(" select codigo from (");
		sqlStr.append(getSqlConsultaRapidaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao(codDisciplina, cargaHoraria, unidadeEnsino, situacaoTurma, curso, matrizCurricular, trazerSomenteTurmaComProgramacaoAula, ano, semestre, trazerSubTurmaGeral, trazerSomenteTurmaAulaNaoOcorreu, trazerApenasTurmaReposicao, trazerSomenteTurmaComTutoriaOnline, apresentarRenovacaoOnline, usuario, matriculaVO));
		sqlStr.append(") as t ) ");
		sqlStr.append("SELECT Turma.codigo, Turma.semestral, Turma.anual, Turma.situacao, Turma.identificadorturma, Turma.turmaagrupada, Turma.sala, Turma.gradeCurricular, Turma.chancela, Turma.turmaPrincipal, ");
		sqlStr.append("Turma.nrvagas, Turma.nrmaximomatricula, Turma.nrminimomatricula, Turma.tipochancela, Turma.porcentagemchancela, Turma.valorfixochancela, Turma.observacao, Turma.valorporaluno, Turma.qtdAlunosEstimado, Turma.custoMedioAluno, Turma.receitaMediaAluno, chancela.instituicaoChanceladora, turma.utilizarDadosMatrizBoleto, turma.subturma, ");
		sqlStr.append("Unidadeensino.codigo as \"Unidadeensino.codigo\" , Unidadeensino.nome as \"Unidadeensino.nome\", turma.abreviaturaCurso, turma.nrVagasInclusaoReposicao, ");
		sqlStr.append("Curso.codigo as \"Curso.codigo\", Curso.nome as \"Curso.nome\", Curso.periodicidade as \"Curso.periodicidade\", Curso.nivelEducacional as \"Curso.nivelEducacional\", Curso.liberarregistroaulaentreperiodo AS \"Curso.liberarregistroaulaentreperiodo\", Curso.configuracaoacademico AS \"Curso.configuracaoacademico\", ");
		sqlStr.append("Turno.codigo as \"Turno.codigo\", Turno.nome as \"Turno.nome\", ");
		sqlStr.append("Periodoletivo.descricao as \"Periodoletivo.descricao\",Periodoletivo.codigo as \"Periodoletivo.codigo\",Periodoletivo.periodoLetivo as \"Periodoletivo.periodoLetivo\", ");
		sqlStr.append("Periodoletivo.nomeCertificacao as \"Periodoletivo.nomeCertificacao\",  ");
		sqlStr.append("gradecurricular.nome as \"gradecurricular.nome\", turma.tipoSubTurma, turma.apresentarRenovacaoOnline, turma.categoriaCondicaoPagamento, turma.considerarTurmaAvaliacaoInstitucional,  ");
		sqlStr.append("case when turma.subturma then turmaPrincipal.identificadorTurma::VARCHAR||'ZZZZ'||turma.identificadorTurma ");
		sqlStr.append("else turma.identificadorTurma end as ordenacao, turma.digitoTurma ");
		sqlStr.append("from cte_turma INNER JOIN turma ON cte_turma.codigo = turma.codigo  ");
		sqlStr.append("left join unidadeensino on turma.unidadeensino = unidadeensino.codigo ");
		sqlStr.append("left join curso on curso.codigo = turma.curso ");
		sqlStr.append("left join turno on turma.turno = turno.codigo ");
		sqlStr.append("left join periodoletivo on turma.periodoletivo = periodoletivo.codigo ");
		sqlStr.append("left join chancela on turma.chancela = chancela.codigo ");
		sqlStr.append("left join gradecurricular on turma.gradecurricular = gradecurricular.codigo ");
		sqlStr.append(" left join turma as turmaprincipal on turmaprincipal.codigo = turma.turmaprincipal ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<TurmaVO> vetResultado = montarDadosConsultaBasica(tabelaResultado);
		return vetResultado;
	}

	private StringBuilder getSqlConsultaRapidaPorDisciplinaPorUnidadeEnsinoPorCursoPorMatrizCurricularPorSituacao(Integer codDisciplina, Integer cargaHoraria, Integer unidadeEnsino, String situacaoTurma, Integer curso, Integer matrizCurricular, boolean trazerSomenteTurmaComProgramacaoAula, String ano, String semestre, boolean trazerSubTurmaGeral, boolean trazerSomenteTurmaAulaNaoOcorreu, boolean trazerApenasTurmaReposicao, Boolean trazerSomenteTurmaComTutoriaOnline, Boolean apresentarRenovacaoOnline ,UsuarioVO usuario, MatriculaVO matriculaVO) throws Exception {
		Integer qtdDiasMaximoAntecedenciaRemarcarAulaReposicao = getAplicacaoControle().getConfiguracaoGeralSistemaVO(unidadeEnsino, usuario).getQtdDiasMaximoAntecedenciaRemarcarAulaReposicao();
		Date dataBase =  new Date();
		if(Uteis.isAtributoPreenchido(qtdDiasMaximoAntecedenciaRemarcarAulaReposicao)) {
			dataBase = Uteis.obterDataAvancada(dataBase, qtdDiasMaximoAntecedenciaRemarcarAulaReposicao);
		}
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM (");
		StringBuilder regra1 = new StringBuilder()
				.append(" AND ((TurmaDisciplina.disciplina = ").append(codDisciplina).append(" and gradedisciplina.cargahoraria = ").append(cargaHoraria).append(") ")
				.append(" or (TurmaDisciplina.disciplina = ").append(codDisciplina).append(" and GradeCurricularGrupoOptativaDisciplina.cargahoraria = ").append(cargaHoraria).append(")) ");
		getSQLPadraoConsultaTurma(codDisciplina, cargaHoraria, unidadeEnsino, situacaoTurma, curso, matrizCurricular, trazerSomenteTurmaComProgramacaoAula, ano, semestre, trazerSubTurmaGeral, trazerSomenteTurmaAulaNaoOcorreu, trazerApenasTurmaReposicao, apresentarRenovacaoOnline, sqlStr, regra1.toString());
		sqlStr.append(" UNION ");
		
		StringBuilder regra2 = new StringBuilder()
				.append(" AND (gdcgd.disciplina = ").append(codDisciplina).append(" and gdcgd.cargahoraria = ").append(cargaHoraria).append(") ");
		getSQLPadraoConsultaTurma(codDisciplina, cargaHoraria, unidadeEnsino, situacaoTurma, curso, matrizCurricular, trazerSomenteTurmaComProgramacaoAula, ano, semestre, trazerSubTurmaGeral, trazerSomenteTurmaAulaNaoOcorreu, trazerApenasTurmaReposicao, apresentarRenovacaoOnline, sqlStr, regra2.toString());
		sqlStr.append(" UNION ");
		
		StringBuilder regra3 = new StringBuilder()
				.append(" AND (gdcgo.disciplina = ").append(codDisciplina).append(" and gdcgo.cargahoraria = ").append(cargaHoraria).append(") ");
		getSQLPadraoConsultaTurma(codDisciplina, cargaHoraria, unidadeEnsino, situacaoTurma, curso, matrizCurricular, trazerSomenteTurmaComProgramacaoAula, ano, semestre, trazerSubTurmaGeral, trazerSomenteTurmaAulaNaoOcorreu, trazerApenasTurmaReposicao, apresentarRenovacaoOnline, sqlStr, regra3.toString());
		sqlStr.append(" UNION ");
		
		StringBuilder regra4 = new StringBuilder()
				.append(" AND (GradeDisciplinaComposta.disciplina = ").append(codDisciplina).append(" and GradeDisciplinaComposta.cargahoraria = ").append(cargaHoraria).append(") ");
		getSQLPadraoConsultaTurma(codDisciplina, cargaHoraria, unidadeEnsino, situacaoTurma, curso, matrizCurricular, trazerSomenteTurmaComProgramacaoAula, ano, semestre, trazerSubTurmaGeral, trazerSomenteTurmaAulaNaoOcorreu, trazerApenasTurmaReposicao, apresentarRenovacaoOnline, sqlStr, regra4.toString());
		
		sqlStr.append(" ) t ");
		if(matriculaVO != null) {
			sqlStr.append(" order by case when t.unidadeensino = "+matriculaVO.getUnidadeEnsino().getCodigo()+" then 0 else 1 end, case when t.curso = "+matriculaVO.getCurso().getCodigo()+" then 0 else 1 end, case when t.turno = "+matriculaVO.getTurno().getCodigo()+" then 0 else 1 end, case when t.gradecurricular = "+matriculaVO.getGradeCurricularAtual().getCodigo()+" then 0 else 1 end,  unidadeEnsino_nome, identificadorturma");
		}else {			
			sqlStr.append(" order by unidadeEnsino_nome, identificadorturma");
		}
		return sqlStr;
	}

	private void getSQLPadraoConsultaTurma(Integer codDisciplina, Integer cargaHoraria, Integer unidadeEnsino, String situacaoTurma, Integer curso, Integer matrizCurricular, boolean trazerSomenteTurmaComProgramacaoAula, String ano, String semestre, boolean trazerSubTurmaGeral, boolean trazerSomenteTurmaAulaNaoOcorreu, boolean trazerApenasTurmaReposicao, Boolean apresentarRenovacaoOnline, StringBuilder sqlStr, String regraLocalizarDisciplina) {
		sqlStr.append("SELECT Turma.codigo, Turma.identificadorTurma, unidadeEnsino.nome as unidadeEnsino_nome, Turma.curso, Turma.unidadeensino, turma.gradecurricular, turma.turno, ");
		sqlStr.append(" array_to_string(array_agg(distinct case when GradeDisciplinaComposta.disciplina is not null then GradeDisciplinaComposta.disciplina else ");
		sqlStr.append(" case when gdcgd.disciplina is not null then gdcgd.disciplina else ");
		sqlStr.append(" case when gdcgo.disciplina is not null then gdcgo.disciplina else ");
		sqlStr.append(" case when GradeDisciplina.disciplina is not null then GradeDisciplina.disciplina else ");
		sqlStr.append(" case when GradeCurricularGrupoOptativaDisciplina.disciplina is not null then GradeCurricularGrupoOptativaDisciplina.disciplina else ");
		sqlStr.append(codDisciplina);
		sqlStr.append(" end end end end end), ',') as disciplinas ");
		sqlStr.append("FROM Turma ");
		sqlStr.append("INNER JOIN TurmaDisciplina ON (TurmaDisciplina.turma = Turma.codigo) ");
		sqlStr.append("LEFT JOIN GradeDisciplina ON (TurmaDisciplina.gradeDisciplina = GradeDisciplina.codigo) ");
		sqlStr.append("LEFT JOIN GradeCurricularGrupoOptativaDisciplina ON (TurmaDisciplina.gradeCurricularGrupoOptativaDisciplina = GradeCurricularGrupoOptativaDisciplina.codigo) ");
		sqlStr.append("LEFT JOIN TurmaDisciplinaComposta on TurmaDisciplinaComposta.turmaDisciplina = TurmaDisciplina.codigo ");
		sqlStr.append("LEFT JOIN GradeDisciplinaComposta on GradeDisciplinaComposta.codigo = TurmaDisciplinaComposta.gradeDisciplinaComposta ");
		sqlStr.append("LEFT JOIN GradeDisciplinaComposta gdcgd ON (gdcgd.gradeDisciplina = GradeDisciplina.codigo)  ");
		sqlStr.append("and not exists (select turmadisciplinacomposta.codigo from turmadisciplinacomposta where turmadisciplinacomposta.TurmaDisciplina = TurmaDisciplina.codigo limit 1) ");
		sqlStr.append("LEFT JOIN GradeDisciplinaComposta gdcgo ON (gdcgo.GradeCurricularGrupoOptativaDisciplina = GradeCurricularGrupoOptativaDisciplina.codigo )  ");
		sqlStr.append("and not exists (select turmadisciplinacomposta.codigo from turmadisciplinacomposta where turmadisciplinacomposta.TurmaDisciplina = TurmaDisciplina.codigo limit 1) ");
		sqlStr.append("LEFT JOIN unidadeEnsino on unidadeEnsino.codigo = turma.unidadeensino ");
//		sqlStr.append("LEFT JOIN configuracoes on unidadeEnsino.configuracoes = configuracoes.codigo ");
//		sqlStr.append("LEFT JOIN configuracaogeralsistema on configuracaogeralsistema.configuracoes = configuracoes.codigo ");
		if (trazerApenasTurmaReposicao) {
//			sqlStr.append(" left join vagaturma on 	vagaturma.turma = turma.codigo ");
//			sqlStr.append(" and ((turma.semestral and vagaturma.ano = '").append(ano).append("'");
//			sqlStr.append(" and vagaturma.semestre = '").append(semestre).append("')");
//			sqlStr.append(" or (turma.anual and vagaturma.ano = '").append(ano).append("') ");
//			sqlStr.append(" or (turma.semestral = false and turma.anual = false))");
//			sqlStr.append(" left join vagaturmadisciplina on 	vagaturmadisciplina.vagaturma = vagaturma.codigo ");
//			sqlStr.append(" and vagaturmadisciplina.disciplina = ").append(codDisciplina);

		}
		if ((trazerSomenteTurmaComProgramacaoAula || trazerSomenteTurmaAulaNaoOcorreu)) {
		sqlStr.append(" inner join lateral ( "); 
		sqlStr.append(" 	select ofertadisciplina.codigo from ofertadisciplina where ofertadisciplina.ano = '").append(ano).append("' and ofertadisciplina.semestre = '").append(semestre).append("' and ofertadisciplina.disciplina = ").append(codDisciplina).append(" ");
//			sqlStr.append(" 	select distinct ht.turma from horarioturma  ht ");
//			sqlStr.append(" 	inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
//			sqlStr.append(" 	inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
//			sqlStr.append(" 	where  ht.turma = turma.codigo");
//			if (trazerSomenteTurmaAulaNaoOcorreu) {
//				sqlStr.append(" 	and htd.data >= '").append(Uteis.getDataJDBC(dataBase)).append("' ");
//			}
//			sqlStr.append(" 	and ((turma.semestral and ht.anovigente = '").append(ano).append("'");
//			sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
//			sqlStr.append(" 	or (turma.anual and ht.anovigente = '").append(ano).append("') ");
//			sqlStr.append(" 	or (turma.semestral = false and turma.anual = false))");
//			sqlStr.append(" 	and ((htdi.disciplina = ").append(codDisciplina).append(") ");
//			sqlStr.append(" 	or (GradeDisciplinaComposta.disciplina = htdi.disciplina) ");
//			sqlStr.append(" 	or (gdcgd.disciplina = htdi.disciplina) ");
//			sqlStr.append(" 	or (gdcgo.disciplina = htdi.disciplina) ");
//
//			sqlStr.append(" 	) ");
//			
//			sqlStr.append(" union  ");
//			sqlStr.append(" 	select distinct t.codigo from horarioturma  ht 	 ");
//			sqlStr.append(" 	inner join turma as t on t.codigo = ht.turma");
//			sqlStr.append(" 	inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
//			sqlStr.append(" 	inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
//			sqlStr.append(" 	where  t.turmaprincipal = turma.codigo and t.subturma and t.turmaagrupada = false ");
//			if (trazerSomenteTurmaAulaNaoOcorreu) {
//				sqlStr.append(" 	and htd.data >= '").append(Uteis.getDataJDBC(dataBase)).append("' ");
//			}
//			if (Uteis.isAtributoPreenchido(situacaoTurma)) {
//				sqlStr.append(" 	and t.situacao = '").append(situacaoTurma).append("'");
//			}
//			sqlStr.append(" 	and ((turma.semestral and ht.anovigente = '").append(ano).append("'");
//			sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
//			sqlStr.append(" 	or (turma.anual and ht.anovigente = '").append(ano).append("') ");
//			sqlStr.append(" 	or (turma.semestral = false and turma.anual = false))");
//			sqlStr.append(" 	and ((htdi.disciplina = ").append(codDisciplina).append(") ");
//			sqlStr.append(" 	or (GradeDisciplinaComposta.disciplina = htdi.disciplina) ");
//			sqlStr.append(" 	or (gdcgd.disciplina = htdi.disciplina) ");
//			sqlStr.append(" 	or (gdcgo.disciplina = htdi.disciplina) ");
//
//			sqlStr.append(" 	) ");
//			
//			
//			
//			sqlStr.append(" union all ");
//			sqlStr.append(" 	select distinct t.codigo from horarioturma  ht");
//			sqlStr.append(" 	inner join turma as t on t.codigo = ht.turma");
//			sqlStr.append(" 	inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo	");
//			sqlStr.append(" 	inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
//			sqlStr.append(" 	inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
//			sqlStr.append(" 	where  turmaagrupada.turma = turma.codigo");
//			if (trazerSomenteTurmaAulaNaoOcorreu) {
//				sqlStr.append(" 	and htd.data >= '").append(Uteis.getDataJDBC(dataBase)).append("' ");
//			}
//			if (Uteis.isAtributoPreenchido(situacaoTurma)) {
//				sqlStr.append(" 	and t.situacao = '").append(situacaoTurma).append("'");
//			}
//			sqlStr.append(" 	and ((turma.semestral and ht.anovigente = '").append(ano).append("'");
//			sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
//			sqlStr.append(" 	or (turma.anual and ht.anovigente = '").append(ano).append("') ");
//			sqlStr.append(" 	or (turma.semestral = false and turma.anual = false))");
//			sqlStr.append(" 	and ((htdi.disciplina = ").append(codDisciplina).append(") ");
//			sqlStr.append(" 	or (GradeDisciplinaComposta.disciplina = htdi.disciplina) ");
//			sqlStr.append(" 	or (gdcgd.disciplina = htdi.disciplina) ");
//			sqlStr.append(" 	or (gdcgo.disciplina = htdi.disciplina) ");
//
//			sqlStr.append(" 	) ");
//			
//			sqlStr.append(" union ");
//			sqlStr.append(" 	select distinct t.codigo from horarioturma  ht");
//			sqlStr.append(" 	inner join turma as t on t.codigo = ht.turma");
//			sqlStr.append(" 	inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo	");
//			sqlStr.append(" 	inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
//			sqlStr.append(" 	inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
////			sqlStr.append(" 	inner join disciplinaequivalente de on de.equivalente = htdi.disciplina ");
//			sqlStr.append(" 	where  turmaagrupada.turma = turma.codigo");
//			if (trazerSomenteTurmaAulaNaoOcorreu) {
//				sqlStr.append(" 	and htd.data >= '").append(Uteis.getDataJDBC(dataBase)).append("' ");
//			}
//			if (Uteis.isAtributoPreenchido(situacaoTurma)) {
//				sqlStr.append(" 	and t.situacao = '").append(situacaoTurma).append("'");
//			}
//			sqlStr.append(" 	and ((turma.semestral and ht.anovigente = '").append(ano).append("'");
//			sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
//			sqlStr.append(" 	or (turma.anual and ht.anovigente = '").append(ano).append("') ");
//			sqlStr.append(" 	or (turma.semestral = false and turma.anual = false))");
////			sqlStr.append(" 	and ((de.disciplina = ").append(codDisciplina).append(") ");
////			sqlStr.append(" 	or (GradeDisciplinaComposta.disciplina = de.disciplina) ");
////			sqlStr.append(" 	or (gdcgd.disciplina = de.disciplina) ");
////			sqlStr.append(" 	or (gdcgo.disciplina = de.disciplina) ");
////
////			sqlStr.append(" 	) ");
//			sqlStr.append(" and (exists (select 1 from disciplinaequivalente d2 where d2.equivalente = htdi.disciplina ");
//			sqlStr.append(" and (d2.disciplina = ").append(codDisciplina);
//			sqlStr.append(" or GradeDisciplinaComposta.disciplina = d2.disciplina");
//			sqlStr.append(" or gdcgd.disciplina = d2.disciplina");
//			sqlStr.append(" or gdcgo.disciplina = d2.disciplina))");
//			sqlStr.append(" or exists (select 1 from disciplinaequivalente d2 where d2.disciplina = htdi.disciplina ");
//			sqlStr.append(" and (d2.equivalente = ").append(codDisciplina);
//			sqlStr.append(" or GradeDisciplinaComposta.disciplina = d2.equivalente");
//			sqlStr.append(" or gdcgd.disciplina = d2.equivalente");
//			sqlStr.append(" or gdcgo.disciplina = d2.equivalente)))");
//			
//
//			
//
//			sqlStr.append("  union  ");
//			sqlStr.append(" 				select distinct t.codigo from horarioturma  ht");
//			sqlStr.append(" 					inner join turma as t on t.codigo = ht.turma");
//			sqlStr.append(" 					inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo	");
//			sqlStr.append(" 					inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
//			sqlStr.append(" 					inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
//			sqlStr.append(" 					inner join disciplinaequivalente de on de.equivalente = htdi.disciplina ");
//			sqlStr.append(" 					where  turmaagrupada.turma ");
//
//			sqlStr.append(" 					in ( select tsub.codigo from turma as tsub where tsub.subturma = true and tsub.tiposubturma in ('PRATICA', 'TEORICA') ");
//			sqlStr.append(" 					and tsub.turmaprincipal = turma.codigo ");
//			if (Uteis.isAtributoPreenchido(situacaoTurma)) {
//				sqlStr.append(" 					and tsub.situacao = 'AB' ");
//			}
//			sqlStr.append(" 					) ");
//			if (trazerSomenteTurmaAulaNaoOcorreu) {
//				sqlStr.append(" 	and htd.data >= '").append(Uteis.getDataJDBC(dataBase)).append("' ");
//			}
//			if (Uteis.isAtributoPreenchido(situacaoTurma)) {
//				sqlStr.append(" 					and t.situacao = 'AB'			");
//			}
//			sqlStr.append(" 					and ((turma.semestral and ht.anovigente = '").append(ano).append("'");
//			sqlStr.append(" 					and ht.semestrevigente = '").append(semestre).append("')");
//			sqlStr.append(" 					or (turma.anual and ht.anovigente = '").append(ano).append("') ");
//			sqlStr.append(" 					or (turma.semestral = false and turma.anual = false))			");
//			sqlStr.append(" 					and ((de.disciplina = ").append(codDisciplina).append(")");
//			sqlStr.append(" 				   )");
//			sqlStr.append(" 			 	");
//			sqlStr.append(" 			 union  ");
//			sqlStr.append(" 				select t.codigo from horarioturma  ht");
//			sqlStr.append(" 					inner join turma as t on t.codigo = ht.turma");
//			sqlStr.append(" 					inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo	");
//			sqlStr.append(" 					inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
//			sqlStr.append(" 					inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia					");
//			sqlStr.append(" 					where  turmaagrupada.turma ");
//			sqlStr.append(" 					in ( select tsub.codigo from turma as tsub where tsub.subturma = true and tsub.tiposubturma in ('PRATICA', 'TEORICA') ");
//			sqlStr.append(" 					and tsub.turmaprincipal = turma.codigo ");
//			if (Uteis.isAtributoPreenchido(situacaoTurma)) {
//				sqlStr.append(" 					and tsub.situacao = 'AB' ");
//			}
//			sqlStr.append(" 					) ");
//			if (trazerSomenteTurmaAulaNaoOcorreu) {
//				sqlStr.append(" 	and htd.data >= '").append(Uteis.getDataJDBC(dataBase)).append("' ");
//			}
//			if (Uteis.isAtributoPreenchido(situacaoTurma)) {
//				sqlStr.append(" 					and t.situacao = 'AB'			");
//			}
//			sqlStr.append(" 					and ((turma.semestral and ht.anovigente = '").append(ano).append("'");
//			sqlStr.append(" 					and ht.semestrevigente = '").append(semestre).append("')");
//			sqlStr.append(" 					or (turma.anual and ht.anovigente = '").append(ano).append("') ");
//			sqlStr.append(" 					or (turma.semestral = false and turma.anual = false))			");
//			sqlStr.append(" 					and ((htdi.disciplina = ").append(codDisciplina).append(")");
//			sqlStr.append(" 				   )");
//			
//
//			if (trazerSomenteTurmaComTutoriaOnline) {
//				sqlStr.append(" union  ");
//				sqlStr.append(" 	select programacaotutoriaonline.turma from programacaotutoriaonline ");
//				sqlStr.append(" 	inner join turmadisciplina on turmadisciplina.turma=programacaotutoriaonline.turma and turmadisciplina.disciplina=programacaotutoriaonline.disciplina");
//				sqlStr.append(" 	where programacaotutoriaonline.turma = turma.codigo ");
//				sqlStr.append(" 	and turmadisciplina.modalidadedisciplina = 'ON_LINE'");
//				sqlStr.append(" 	and turmadisciplina.definicoestutoriaonline = 'DINAMICA'");
//				sqlStr.append(" 	and programacaotutoriaonline.definirperiodoaulaonline is true ");
//				sqlStr.append(" 					and ((turma.semestral and programacaotutoriaonline.ano = '").append(ano).append("'");
//				sqlStr.append(" 					and programacaotutoriaonline.semestre = '").append(semestre).append("')");
//				sqlStr.append(" 					or (turma.anual and programacaotutoriaonline.ano = '").append(ano).append("') ");
//				sqlStr.append(" 					or (turma.semestral = false and turma.anual = false)) ");
//				sqlStr.append(" 	and programacaotutoriaonline.disciplina = ").append(codDisciplina).append(" ");
//				if (trazerSomenteTurmaAulaNaoOcorreu) {
//					sqlStr.append(" 	and programacaotutoriaonline.datainicioaula >= '").append(Uteis.getDataJDBC(dataBase)).append("' ");					
//				}
//				sqlStr.append(" ");
//			}

			
			sqlStr.append(" limit 1) as turma_temp on 1=1 "); 
		}
		sqlStr.append("WHERE ((Turma.subturma = false ");
		sqlStr.append("AND Turma.turmaagrupada = false) ");
		if (trazerSubTurmaGeral) {
			sqlStr.append(" or (Turma.subturma = true and Turma.tipoSubturma = 'GERAL') ");
		}
		sqlStr.append(" ) ");
		if (trazerApenasTurmaReposicao) {
//			sqlStr.append(" AND TurmaDisciplina.permiteReposicao  ");
//			sqlStr.append(" and ((vagaturmadisciplina.codigo is not null and vagaturmadisciplina.nrVagasMatriculaReposicao is not null and vagaturmadisciplina.nrVagasMatriculaReposicao > 0) ");
//			sqlStr.append(" or (vagaturmadisciplina.codigo is null and turma.nrVagasInclusaoReposicao is not null and turma.nrVagasInclusaoReposicao > 0) ");
//			sqlStr.append(" )  and (");
//			sqlStr.append(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSqlPadraoConsultarVagasOcupadas("turma.codigo", codDisciplina, ano, semestre, true, null, "", trazerApenasTurmaReposicao, false)).append(" ");
//			sqlStr.append(" + (").append(getFacadeFactory().getRequerimentoFacade().getSqlQtdeRequerimentoReposicaoNaoFinalizadosNaoVencidosParaContablizacaoVaga("turma.codigo", null, codDisciplina, ano, semestre)).append("))");
//			sqlStr.append(" < (case when vagaturmadisciplina.nrVagasMatriculaReposicao is not null then vagaturmadisciplina.nrVagasMatriculaReposicao else turma.nrVagasInclusaoReposicao end )");

		}
		sqlStr.append(regraLocalizarDisciplina);

		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(situacaoTurma)) {
			sqlStr.append(" AND turma.situacao = '").append(situacaoTurma).append("'");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" AND turma.curso = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(matrizCurricular)) {
			sqlStr.append(" AND turma.gradecurricular = ").append(matrizCurricular);
		}
		if (Uteis.isAtributoPreenchido(apresentarRenovacaoOnline) && apresentarRenovacaoOnline) {
			sqlStr.append(" AND turma.apresentarRenovacaoOnline " );
		}
		sqlStr.append(" group by Turma.codigo, Turma.identificadorTurma, unidadeEnsino.nome,  Turma.curso, Turma.unidadeensino, turma.gradecurricular, turma.turno ");
	}

	public TurmaVO consultarTurmaPorIdentificadorTurma(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql =  new StringBuilder("SELECT * FROM Turma WHERE upper (identificadorTurma) like ?");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		sql.append(" ORDER BY identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta.toUpperCase() + "%");
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_DadosNaoEncontrado").replace("{0}", "TURMA"));
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public TurmaVO consultarTurmaPorIdentificadorTurmaEspecifico(String valorConsulta, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT turma.* FROM Turma");
		sqlStr.append(" left join curso on ((turma.turmaAgrupada = false and curso.codigo = turma.curso) or (turma.turmaAgrupada and curso.codigo = (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo limit 1)))");
		sqlStr.append(" WHERE sem_acentos(identificadorTurma) ilike sem_acentos(?)");
		if (unidadeEnsino.intValue() != 0) {
			sqlStr.append(" and turma.unidadeEnsino = ").append(unidadeEnsino.intValue());
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sqlStr.append(" AND (curso.nivelEducacional in ('PO', 'EX', 'MT') OR turma.turmaAgrupada)");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sqlStr.append(" AND (curso.nivelEducacional != 'PO')");
		}
		sqlStr.append(" ORDER BY identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_DadosNaoEncontrado").replace("{0}", "TURMA"));
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public TurmaVO consultarTurmaPorIdentificadorTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Turma WHERE lower (identificadorTurma) like '" + valorConsulta.toLowerCase() + "%' ORDER BY identificadorTurma";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_DadosNaoEncontrado").replace("{0}", "TURMA"));
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * responsável por realizar uma consulta de <code>Turma</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parÃ¯Â¿Â½metro fornecido. Faz uso da Operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicaÃ§Ã£oo deverÃ¯Â¿Â½ verificar se o usuÃ¡rio possui permissÃ¯Â¿Â½o para esta consulta ou nÃ£o.
	 * @return List Contendo vÃ¯Â¿Â½rios objetos da classe <code>TurmaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restriÃ§Ã£oo de acesso.
	 */
	public List<TurmaVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Turma WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM Turma WHERE codigo >= " + valorConsulta.intValue() + " and unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY codigo";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<TurmaVO> consultarPorCodigoNivelEducacionalCurso(Integer valorConsulta, Integer unidadeEnsino, String nivelEducacional, Integer curso, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT turma.* FROM turma inner join curso on turma.curso = curso.codigo ");
		sql.append(" WHERE turma.codigo >= ");
		sql.append(valorConsulta);
		sql.append(" and curso.niveleducacional = '");
		sql.append(nivelEducacional);
		sql.append("' ");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and unidadeEnsino = ");
			sql.append(unidadeEnsino);
		}
		if (curso.intValue() != 0) {
			sql.append(" and curso.codigo = ");
			sql.append(curso);
		}
		sql.append(" ORDER BY turma.codigo ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * responsável por montar os dados de vÃ¯Â¿Â½rios objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da Operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vÃ¯Â¿Â½rios objetos da classe <code>TurmaVO</code> resultantes da consulta.
	 */
	public  List<TurmaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>TurmaVO</code>.
	 * 
	 * @return O objeto da classe <code>TurmaVO</code> com os dados devidamente montados.
	 */
	public  TurmaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		TurmaVO obj = new TurmaVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
		obj.setAnual(tabelaResultado.getBoolean("anual"));
		obj.setSemestral(tabelaResultado.getBoolean("semestral"));
		obj.getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeEnsino"));
		obj.getCurso().setCodigo(tabelaResultado.getInt("curso"));
		obj.setSubturma(tabelaResultado.getBoolean("subturma"));
		obj.setTurmaPrincipal(tabelaResultado.getInt("turmaPrincipal"));
		obj.setTurmaAgrupada(tabelaResultado.getBoolean("turmaagrupada"));
		obj.setTipoSubTurma(TipoSubTurmaEnum.valueOf(tabelaResultado.getString("tipoSubTurma")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosTurno(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			return obj;
		}
		obj.getPeridoLetivo().setCodigo(tabelaResultado.getInt("periodoletivo"));
		obj.getTurno().setCodigo(tabelaResultado.getInt("turno"));
		// obj.setTurmaPreMatricula(tabelaResultado.getBoolean("turmaPreMatricula"));
//		obj.getPlanoFinanceiroCurso().setCodigo(tabelaResultado.getInt("planoFinanceiroCurso"));
		obj.setDataBaseGeracaoParcelas(tabelaResultado.getDate("dataBaseGeracaoParcelas"));
		obj.setDataPrevisaoFinalizacao(tabelaResultado.getDate("dataprevisaofinalizacao"));
		obj.setQtdAlunosEstimado(tabelaResultado.getInt("qtdAlunosEstimado"));
		obj.setCustoMedioAluno(tabelaResultado.getDouble("custoMedioAluno"));
		obj.setReceitaMediaAluno(tabelaResultado.getDouble("receitaMediaAluno"));
//		obj.getChancelaVO().setCodigo(tabelaResultado.getInt("chancela"));
//		obj.getContaCorrente().setCodigo(tabelaResultado.getInt("contaCorrente"));
		obj.setObservacao(tabelaResultado.getString("observacao"));
		obj.getGrupoDestinatarios().setCodigo(tabelaResultado.getInt("grupoDestinatarios"));
		obj.setUtilizarDadosMatrizBoleto(tabelaResultado.getBoolean("utilizarDadosMatrizBoleto"));
		obj.setNrVagas(tabelaResultado.getInt("nrVagas"));
		obj.setNrMaximoMatricula(tabelaResultado.getInt("nrMaximoMatricula"));
		obj.setNrMinimoMatricula(tabelaResultado.getInt("nrMinimoMatricula"));
		obj.getGradeCurricularVO().setCodigo(tabelaResultado.getInt("gradeCurricular"));
		obj.setSituacao(tabelaResultado.getString("situacao"));
		obj.setSala(tabelaResultado.getString("sala"));
		obj.setConsiderarTurmaAvaliacaoInstitucional(tabelaResultado.getBoolean("considerarTurmaAvaliacaoInstitucional"));
		obj.getAvaliacaoOnlineVO().setCodigo(tabelaResultado.getInt("avaliacaoonline"));
		if (Uteis.isAtributoPreenchido(obj.getAvaliacaoOnlineVO().getCodigo())) {
			obj.getAvaliacaoOnlineVO().setNovoObj(false);
		}
		obj.getConfiguracaoEADVO().setCodigo(tabelaResultado.getInt("configuracaoead"));
		if (Uteis.isAtributoPreenchido(obj.getConfiguracaoEADVO().getCodigo())) {
			obj.setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().consultarPorChavePrimaria(obj.getConfiguracaoEADVO().getCodigo()));
			obj.getConfiguracaoEADVO().setNovoObj(false);
		}
		obj.setAbreviaturaCurso(tabelaResultado.getString("abreviaturaCurso"));
		obj.setNrVagasInclusaoReposicao(tabelaResultado.getInt("nrVagasInclusaoReposicao"));
		obj.setApresentarRenovacaoOnline(tabelaResultado.getBoolean("apresentarRenovacaoOnline"));
		obj.setCategoriaCondicaoPagamento(tabelaResultado.getString("categoriaCondicaoPagamento"));
		obj.setDigitoTurma(tabelaResultado.getString("digitoTurma"));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
//			montarDadosContaCorrente(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
//			montarDadosChancela(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosGrupoDestinatarios(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosTurno(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosPeriodoLetivo(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
//			montarDadosPlanoFinanceiroCurso(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosGradeCurricular(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_PROCESSAMENTO) {
			obj.setTurmaAgrupadaVOs(getFacadeFactory().getTurmaAgrupadaFacade().consultarTurmaAgrupadas(obj.getCodigo(), false, nivelMontarDados, usuario));
			return obj;
		}
		obj.setTurmaDisciplinaVOs(getFacadeFactory().getTurmaDisciplinaFacade().consultarTurmaDisciplinas(obj.getCodigo(), false, nivelMontarDados, usuario));
//		obj.setTurmaAberturaVOs(TurmaAbertura.consultarTurmaAberturas(obj.getCodigo(), false, nivelMontarDados, usuario));
		obj.setTurmaAgrupadaVOs(getFacadeFactory().getTurmaAgrupadaFacade().consultarTurmaAgrupadas(obj.getCodigo(), false, nivelMontarDados, usuario));
		obj.setTurmaContratoVOs(getFacadeFactory().getTurmaContratoFacade().consultarPorTurma(obj.getCodigo(), usuario));
		montarDadosGrupoDestinatarios(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosTurno(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosPeriodoLetivo(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
//		montarDadosPlanoFinanceiroCurso(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosGradeCurricular(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		if (Uteis.isAtributoPreenchido(obj.getCurso().getConfiguracaoAcademico().getCodigo())) {
			obj.getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimariaDadosMinimos(obj.getCurso().getConfiguracaoAcademico().getCodigo(), usuario));
		}
		if (Uteis.isAtributoPreenchido(obj.getAvaliacaoOnlineVO().getCodigo())) {
			obj.setAvaliacaoOnlineVO(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarPorChavePrimaria(obj.getAvaliacaoOnlineVO().getCodigo(), 0, usuario));
		}
		return obj;
	}

	public static List<TurmaVO> montarDadosConsultaComboBox(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosComboBox(tabelaResultado, nivelMontarDados));
		}
		return vetResultado;
	}

	public static TurmaVO montarDadosComboBox(SqlRowSet tabelaResultado, int nivelMontarDados) {
		TurmaVO obj = new TurmaVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
		return obj;
	}



	public  void montarDadosTurno(TurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurno().getCodigo().intValue() == 0) {
			obj.setTurno(new TurnoVO());
			return;
		}
		obj.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(obj.getTurno().getCodigo(), nivelMontarDados, usuario));
	}

	public  void montarDadosGrupoDestinatarios(TurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getGrupoDestinatarios().getCodigo().intValue() == 0) {
			obj.setGrupoDestinatarios(new GrupoDestinatariosVO());
			return;
		}
//		obj.setGrupoDestinatarios(getFacadeFactory().getGrupoDestinatariosFacade().consultarPorChavePrimaria(obj.getGrupoDestinatarios().getCodigo(), false, nivelMontarDados, usuario));
	}

	
	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>UnidadeEnsinoVO</code> relacionado ao objeto <code>TurmaVO</code>. Faz uso da chave primária da classe <code>UnidadeEnsinoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual serÃ¯Â¿Â½ montado os dados consultados.
	 */
	public  void montarDadosUnidadeEnsino(TurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}

		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PeriodoLetivoVO</code> relacionado ao objeto <code>TurmaVO</code>. Faz uso da chave primária da classe <code>PeriodoLetivoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual serÃ¯Â¿Â½ montado os dados consultados.
	 */
	public  void montarDadosCurso(TurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCurso().getCodigo().intValue() == 0) {
			obj.setCurso(new CursoVO());
			return;
		}
		obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
	}

	public  void montarDadosPeriodoLetivo(TurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPeridoLetivo().getCodigo().intValue() == 0) {
			obj.setPeridoLetivo(new PeriodoLetivoVO());
			return;
		}
		obj.setPeridoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(obj.getPeridoLetivo().getCodigo(), nivelMontarDados, usuario));
	}

	public  void montarDadosGradeCurricular(TurmaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getGradeCurricularVO().getCodigo().intValue() == 0) {
			obj.setGradeCurricularVO(new GradeCurricularVO());
			return;
		}

		obj.setGradeCurricularVO(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(obj.getGradeCurricularVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>TurmaVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public TurmaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM Turma WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (tabelaResultado.next()) {
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		throw new ConsistirException("Dados Não Encontrados (Turma).");
	
	}	

	public TurmaVO consultarTurmaDoAlunoPorMatriculaPeriodo(Integer matriculaPeriodo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = new StringBuilder("SELECT * FROM Turma ");
		sql.append("INNER JOIN matriculaPeriodo mp on mp.turma = turma.codigo ");
		sql.append("WHERE mp.codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { matriculaPeriodo });
		if (!tabelaResultado.next()) {
			return new TurmaVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public TurmaVO consultarPorChavePrimaria(Integer codTurma, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		TurmaVO obj = new TurmaVO();
		obj.setCodigo(codTurma);
		carregarDados(obj, nivelMontarDados, usuario);
		return obj;
	}

	public List consultarTurmaDoAluno(Integer unidadeEnsino, Integer aluno, Boolean somenteMatriculaPeriodoAtiva, int nivelMontarDados, UsuarioVO usuario) throws SQLException, Exception {
		StringBuilder selectStr = new StringBuilder();
		selectStr.append("select distinct turma.*");
		selectStr.append(" from matricula inner join matriculaPeriodo on matriculaPeriodo.matricula =  matricula.matricula");
		selectStr.append(" inner join matriculaPeriodoTurmaDisciplina on matriculaPeriodoTurmaDisciplina.matriculaPeriodo = matriculaPeriodo.codigo");
		selectStr.append(" inner join Turma on matriculaPeriodoTurmaDisciplina.turma = turma.codigo");
		selectStr.append(" where matricula.aluno = '").append(aluno).append("'");
		if (somenteMatriculaPeriodoAtiva) {
			selectStr.append(" AND (matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' or matriculaPeriodo.situacaoMatriculaPeriodo = 'FI')");
		}
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			selectStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		selectStr.append(" union ");
		selectStr.append(" 	select distinct tu.* from matriculaPeriodoTurmaDisciplina");
		selectStr.append(" 	inner join matriculaPeriodo on matriculaPeriodoTurmaDisciplina.matriculaPeriodo = matriculaPeriodo.codigo  ");
		selectStr.append(" 	inner join matricula on matricula.matricula = matriculaPeriodo.matricula");
		selectStr.append(" 	inner join curso on curso.codigo = matricula.curso");
		selectStr.append(" 	inner join Turma on matriculaPeriodoTurmaDisciplina.Turma = Turma.codigo ");
		selectStr.append(" 	inner join TurmaAgrupada on Turma.codigo = TurmaAgrupada.turma");
		selectStr.append(" 	inner join Turma as tu on tu.codigo = TurmaAgrupada.turmaorigem ");
		selectStr.append(" 	inner join HorarioTurma on HorarioTurma.turma = TurmaAgrupada.turmaorigem ");
		
		selectStr.append(" 	where matricula.aluno = ").append(aluno);
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			selectStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino);
		}
		if (somenteMatriculaPeriodoAtiva) {
			selectStr.append(" AND (matriculaPeriodo.situacaoMatriculaPeriodo = 'AT' or matriculaPeriodo.situacaoMatriculaPeriodo = 'FI')");
		}
		selectStr.append(" 	and case when curso.periodicidade = 'SE' then ");
		selectStr.append(" 	 horarioturma.anovigente = matriculaPeriodo.ano and horarioturma.semestrevigente = matriculaPeriodo.semestre");
		selectStr.append(" 	 else case when curso.periodicidade = 'AN' then ");
		selectStr.append(" 	 horarioturma.anovigente = matriculaPeriodo.ano");
		selectStr.append(" 	 else true end end");
		selectStr.append(" 	and exists (select HorarioTurmadia.codigo from HorarioTurmadia inner join HorarioTurmadiaitem on HorarioTurmadia.codigo = HorarioTurmadiaitem.HorarioTurmadia where HorarioTurmadia.HorarioTurma = HorarioTurma.codigo and HorarioTurmadiaitem.disciplina = matriculaPeriodoTurmaDisciplina.disciplina limit 1)");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
		List<TurmaVO> listaResultado = montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		return listaResultado;
	}

	public List<TurmaVO> consultarTurmaDoAlunoPorMatriculaAtivaData(Integer unidadeEnsino, Integer aluno, int nivelMontarDados, UsuarioVO usuario) throws SQLException, Exception {
		StringBuilder selectStr = new StringBuilder();
		selectStr.append("SELECT DISTINCT turma.* FROM matricula ");
		selectStr.append("LEFT JOIN matriculaperiodo ON (matricula.matricula = matriculaperiodo.matricula) ");
		selectStr.append("LEFT JOIN matriculaperiodoturmadisciplina ON (matriculaperiodo.matricula = matriculaperiodoturmadisciplina.matricula) ");
		selectStr.append("LEFT JOIN turma ON turma.codigo = matriculaperiodoturmadisciplina.turma ");
		selectStr.append("WHERE matriculaperiodo.situacaomatriculaperiodo = 'AT' ");
		selectStr.append("AND matricula.aluno = '").append(aluno).append("' ");
		selectStr.append("AND matriculaperiodoturmadisciplina.semestre = '").append(Uteis.getSemestreAtual()).append("' ");
		selectStr.append("AND matriculaperiodoturmadisciplina.ano = '").append(Uteis.getAnoDataAtual4Digitos()).append("' ");

		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			selectStr.append(" AND matricula.unidadeensino = ").append(unidadeEnsino).append(";");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
		List<TurmaVO> listaResultado = montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
		return listaResultado;
	}

	/**
	 * Operação resonsável por retornar o identificador desta classe. Este identificar Ã¯Â¿Â½ utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return Turma.idEntidade;
	}

	/**
	 * Operação resonsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possÃ¯Â¿Â½vel, pois, uma mesma classe de negÃ¯Â¿Â½cio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso Ã¯Â¿Â½ realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		Turma.idEntidade = idEntidade;
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct Turma.codigo, Turma.semestral, Turma.anual, Turma.situacao, Turma.identificadorturma, Turma.turmaagrupada, Turma.sala, Turma.gradeCurricular, Turma.chancela, Turma.turmaPrincipal, ");
		sql.append("Turma.nrvagas, Turma.nrmaximomatricula, Turma.nrminimomatricula, Turma.tipochancela, Turma.porcentagemchancela, Turma.valorfixochancela, Turma.observacao, Turma.valorporaluno, Turma.qtdAlunosEstimado, Turma.custoMedioAluno, Turma.receitaMediaAluno, chancela.instituicaoChanceladora, turma.utilizarDadosMatrizBoleto, turma.subturma, ");
		sql.append("Unidadeensino.codigo as \"Unidadeensino.codigo\" , Unidadeensino.nome as \"Unidadeensino.nome\", turma.abreviaturaCurso, turma.nrVagasInclusaoReposicao, ");
		sql.append("Curso.codigo as \"Curso.codigo\", Curso.nome as \"Curso.nome\", Curso.periodicidade as \"Curso.periodicidade\", Curso.nivelEducacional as \"Curso.nivelEducacional\", Curso.liberarregistroaulaentreperiodo AS \"Curso.liberarregistroaulaentreperiodo\", Curso.configuracaoacademico AS \"Curso.configuracaoacademico\", ");
		sql.append("Turno.codigo as \"Turno.codigo\", Turno.nome as \"Turno.nome\", ");
		sql.append("Periodoletivo.descricao as \"Periodoletivo.descricao\",Periodoletivo.codigo as \"Periodoletivo.codigo\",Periodoletivo.periodoLetivo as \"Periodoletivo.periodoLetivo\", ");
		sql.append("Periodoletivo.nomeCertificacao as \"Periodoletivo.nomeCertificacao\",  ");
		sql.append("gradecurricular.nome as \"gradecurricular.nome\", turma.tipoSubTurma, turma.apresentarRenovacaoOnline, turma.categoriaCondicaoPagamento, turma.considerarTurmaAvaliacaoInstitucional,  ");
		sql.append("case when turma.subturma then turmaPrincipal.identificadorTurma::VARCHAR||'ZZZZ'||turma.identificadorTurma ");
		sql.append("else turma.identificadorTurma end as ordenacao, turma.digitoTurma ");
		sql.append("from turma ");
		sql.append("left join unidadeensino on turma.unidadeensino = unidadeensino.codigo ");
//		sql.append("left join curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada = false and curso.codigo = (select t.curso from turma t where t.codigo = turma.turmaprincipal limit 1)) ");
//		sql.append("or (turma.turmaAgrupada and curso.codigo = (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo limit 1)) )");
		sql.append("left join curso on curso.codigo = turma.curso ");
		sql.append("left join turno on turma.turno = turno.codigo ");
		sql.append("left join periodoletivo on turma.periodoletivo = periodoletivo.codigo ");
		sql.append("left join chancela on turma.chancela = chancela.codigo ");
		sql.append("left join gradecurricular on turma.gradecurricular = gradecurricular.codigo ");
		sql.append(" left join turma as turmaprincipal on turmaprincipal.codigo = turma.turmaprincipal ");
		return sql;
	}
	
	private StringBuilder getSQLPadraoConsultaBasicaComTotalizador() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct Turma.codigo, Turma.semestral, Turma.anual, Turma.situacao, Turma.identificadorturma, Turma.turmaagrupada, Turma.sala, Turma.gradeCurricular, Turma.chancela, Turma.turmaPrincipal, ");
		sql.append("Turma.nrvagas, Turma.nrmaximomatricula, Turma.nrminimomatricula, Turma.tipochancela, Turma.porcentagemchancela, Turma.valorfixochancela, Turma.observacao, Turma.valorporaluno, Turma.qtdAlunosEstimado, Turma.custoMedioAluno, Turma.receitaMediaAluno, chancela.instituicaoChanceladora, turma.utilizarDadosMatrizBoleto, turma.subturma, ");
		sql.append("Unidadeensino.codigo as \"Unidadeensino.codigo\" , Unidadeensino.nome as \"Unidadeensino.nome\", turma.abreviaturaCurso, turma.nrVagasInclusaoReposicao, ");
		sql.append("Curso.codigo as \"Curso.codigo\", Curso.nome as \"Curso.nome\", Curso.periodicidade as \"Curso.periodicidade\", Curso.nivelEducacional as \"Curso.nivelEducacional\", Curso.liberarregistroaulaentreperiodo AS \"Curso.liberarregistroaulaentreperiodo\", Curso.configuracaoacademico AS \"Curso.configuracaoacademico\", ");
		sql.append("Turno.codigo as \"Turno.codigo\", Turno.nome as \"Turno.nome\", ");
		sql.append("Periodoletivo.descricao as \"Periodoletivo.descricao\",Periodoletivo.codigo as \"Periodoletivo.codigo\",Periodoletivo.periodoLetivo as \"Periodoletivo.periodoLetivo\", ");
		sql.append("Periodoletivo.nomeCertificacao as \"Periodoletivo.nomeCertificacao\",  ");
		sql.append("gradecurricular.nome as \"gradecurricular.nome\", turma.tipoSubTurma, turma.apresentarRenovacaoOnline, turma.categoriaCondicaoPagamento, turma.considerarTurmaAvaliacaoInstitucional,  ");
		sql.append("case when turma.subturma then turmaPrincipal.identificadorTurma::VARCHAR||'ZZZZ'||turma.identificadorTurma ");
		sql.append("else turma.identificadorTurma end as ordenacao, turma.digitoTurma, ");
		sql.append("count(*) over() as qtde_total_registros ");
		sql.append("from turma ");
		sql.append("left join unidadeensino on turma.unidadeensino = unidadeensino.codigo ");
//		sql.append("left join curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada = false and curso.codigo = (select t.curso from turma t where t.codigo = turma.turmaprincipal limit 1)) ");
//		sql.append("or (turma.turmaAgrupada and curso.codigo = (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo limit 1)) )");
		sql.append(" left join curso on curso.codigo = turma.curso ");
		sql.append("left join turno on turma.turno = turno.codigo ");
		sql.append("left join periodoletivo on turma.periodoletivo = periodoletivo.codigo ");
		sql.append("left join chancela on turma.chancela = chancela.codigo ");
		sql.append("left join gradecurricular on turma.gradecurricular = gradecurricular.codigo ");
		sql.append(" left join turma as turmaprincipal on turmaprincipal.codigo = turma.turmaprincipal ");
		return sql;
	}

	private StringBuffer getSQLPadraoConsultaCompleta() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT Turma.*, Gradecurricular.codigo as \"Gradecurricular.codigo\", Periodoletivo.codigo as \"Periodoletivo.codigo\", Unidadeensino.nome as \"Unidadeensino.nome\" , ");
		sql.append(" Curso.nome as \"Curso.nome\",  Curso.nivelEducacional as \"Curso.nivelEducacional\", Turno.nome as \"Turno.nome\" , Turno.descricaoTurnoContrato as \"Turno.descricaoTurnoContrato\" , Periodoletivo.descricao as \"Periodoletivo.descricao\",Periodoletivo.nomeCertificacao \"Periodoletivo.nomeCertificacao\" , Periodoletivo.periodoLetivo as \"Periodoletivo.periodoLetivo\"  ,Turno.codigo as \"Turno.codigo\", ");
		sql.append(" Turno.finalintervalo as \"Turno.finalintervalo\", Turno.iniciointervalo as \"Turno.iniciointervalo\", Turno.duracaoaula as \"Turno.duracaoaula\", ");
		sql.append(" Turno.nraulas as \"Turno.nraulas\", Turno.horafinal as \"Turno.horafinal\", Turno.horainicio as \"Turno.horainicio\", ");
		sql.append(" Turno.duracaointervalo as \"Turno.duracaointervalo\", Turno.nraulasantes as \"Turno.nraulasantes\", Turno.considerarhoraaulasessentaminutosgeracaodiario as \"Turno.considerarhoraaulasessentaminutosgeracaodiario\", ");
		sql.append(" Turma.tipochancela, Turma.porcentagemchancela, Turma.observacao, Turma.valorfixochancela, Turma.valorporaluno, chancela.instituicaoChanceladora, contacorrente.numero, contacorrente.digito, ");
		sql.append(" Curso.codigo as \"Curso.codigo\" , Curso.periodicidade as \"Curso.periodicidade\", curso.liberarRegistroAulaEntrePeriodo as \"curso.liberarRegistroAulaEntrePeriodo\",  ");
		sql.append(" Disciplina.nome as \"Disciplina.nome\" , Disciplina.codigo as \"Disciplina.codigo\", Disciplina.abreviatura as \"Disciplina.abreviatura\", ");
		// DADOS DE Gradedisciplina
		sql.append(" gradedisciplina.cargahoraria as \"gradedisciplina.cargahoraria\", gradedisciplina.nrcreditos as \"gradedisciplina.nrcreditos\", ");
		sql.append(" gradedisciplina.nrCreditoFinanceiro as \"gradedisciplina.nrCreditoFinanceiro\", gradedisciplina.tipoDisciplina as \"gradedisciplina.tipoDisciplina\", ");
		sql.append(" gradedisciplina.modalidadeDisciplina as \"gradedisciplina.modalidadeDisciplina\", ");
		sql.append(" gradedisciplina.disciplina as \"gradedisciplina.disciplina\", gradedisciplina.disciplinaComposta as \"gradedisciplina.disciplinaComposta\", gradedisciplina.tipoControleComposicao, gradedisciplina.periodoLetivo as \"gradedisciplina.periodoLetivo\",");
		// DADOS DE gradeCurricularGrupoOptativaDisciplina
		sql.append(" gradeCurricularGrupoOptativaDisciplina.cargahoraria as \"gradeCurricularGrupoOptativaDisciplina.cargahoraria\", gradeCurricularGrupoOptativaDisciplina.nrcreditos as \"gradeCurricularGrupoOptativaDisciplina.nrcreditos\", ");
		sql.append(" gradeCurricularGrupoOptativaDisciplina.nrCreditoFinanceiro as \"gradeCurricularGrupoOptativaDisciplina.nrCreditoFinanceiro\", gradeCurricularGrupoOptativaDisciplina.gradecurriculargrupooptativa as \"gcgod.gradecurriculargrupooptativa\", ");
		sql.append(" gradeCurricularGrupoOptativaDisciplina.modalidadeDisciplina as \"gradeCurricularGrupoOptativaDisciplina.modalidadeDisciplina\", ");
		sql.append(" gradeCurricularGrupoOptativaDisciplina.disciplina as \"gradeCurricularGrupoOptativaDisciplina.disciplina\", gradeCurricularGrupoOptativaDisciplina.disciplinaComposta as \"gradeCurricularGrupoOptativaDisciplina.disciplinaComposta\", ");
		// VINCULO DE TURMADISCIPLINA COM Gradedisciplina E
		// GradeCurricularGrupoOptativaDisciplina
		sql.append(" turmaDisciplina.gradeDisciplina as \"turmaDisciplina.gradeDisciplina\", ");
		sql.append(" turmaDisciplina.permiteReposicao as \"turmaDisciplina.permiteReposicao\", ");
		sql.append(" turmaDisciplina.DisciplinaReferenteAUmGrupoOptativa as \"turmaDisciplina.disciplinaReferenteAUmGrupoOptativa\", ");
		sql.append(" turmaDisciplina.permiteApoioPresencial as \"turmaDisciplina.permiteApoioPresencial\", ");
		sql.append(" turmaDisciplina.gradeCurricularGrupoOptativaDisciplina as \"turmaDisciplina.gradeCurricularGrupoOptativaDisciplina\", ");

		sql.append(" TurmaDisciplina.nralunosmatriculados as \"TurmaDisciplina.nralunosmatriculados\", TurmaDisciplina.definicoestutoriaonline as \"TurmaDisciplina.definicoestutoriaonline\", TurmaDisciplina.ordemestudoonline as \"TurmaDisciplina.ordemestudoonline\", GradeCurricular.nome as grade, turma.situacao as situacao,   ");
		sql.append(" TurmaDisciplina.codigo as \"TurmaDisciplina.codigo\",TurmaDisciplina.modalidadeDisciplina as \"TurmaDisciplina.modalidadeDisciplina\", UnidadeEnsino.codigo as \"UnidadeEnsino.codigo\", Planofinanceirocurso.codigo as \"Planofinanceirocurso.codigo\", Planofinanceirocurso.descricao as \"Planofinanceirocurso.descricao\", ");
		sql.append(" TurmaDisciplina.localAula as \"TurmaDisciplina.localAula\",TurmaDisciplina.salaLocalAula as \"TurmaDisciplina.salaLocalAula\", TurmaDisciplina.configuracaoAcademico as \"TurmaDisciplina.configuracaoAcademico\", ");
		sql.append(" disciplinaEquivalenteTurmaAgrupada.codigo AS \"disciplinaEquivalenteTurmaAgrupada.codigo\", disciplinaEquivalenteTurmaAgrupada.nome AS \"disciplinaEquivalenteTurmaAgrupada.nome\" ");
		sql.append(" ,Turma.categoriaCondicaoPagamento, ");
		sql.append(" centroResultado.codigo AS \"centroResultado.codigo\", centroResultado.identificadorCentroResultado AS \"centroResultado.identificadorCentroResultado\", centroResultado.descricao AS \"centroResultado.descricao\", turma.digitoTurma, turma.codigoTurnoApresentarCenso ");

		sql.append(" from turma ");
		sql.append(" left join unidadeensino on turma.unidadeensino = unidadeensino.codigo   ");
		sql.append(" left join curso on turma.curso = curso.codigo   ");
		sql.append(" left join turno on turma.turno = turno.codigo  ");
		sql.append(" left join periodoletivo on turma.periodoletivo = periodoletivo.codigo   ");
		// sql.append(" left join gradedisciplina on
		// gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sql.append(" left join gradecurricular on turma.gradecurricular = gradecurricular.codigo 	");
		sql.append(" left join turmadisciplina on  turmadisciplina.turma = turma.codigo ");
		sql.append(" left join disciplina on turmadisciplina.disciplina = disciplina.codigo ");
		sql.append(" left join gradedisciplina on gradedisciplina.codigo = turmaDisciplina.gradedisciplina ");
		sql.append(" left join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmaDisciplina.gradeCurricularGrupoOptativaDisciplina ");
		sql.append(" left join disciplina disciplinaEquivalenteTurmaAgrupada on turmadisciplina.disciplinaEquivalenteTurmaAgrupada = disciplinaEquivalenteTurmaAgrupada.codigo ");
		sql.append(" left join planofinanceirocurso on turma.planofinanceirocurso = planofinanceirocurso.codigo ");
		sql.append(" left join chancela on turma.chancela = chancela.codigo ");
		sql.append(" left join contacorrente on turma.contacorrente = contacorrente.codigo ");
		sql.append(" left join centroresultado on turma.centroresultado = centroresultado.codigo ");
		return sql;
	}

	private void montarDadosBasico(TurmaVO obj, SqlRowSet dadosSQL) throws Exception {
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setSemestral(dadosSQL.getBoolean("semestral"));
		obj.setAnual(dadosSQL.getBoolean("anual"));
		obj.setNrVagas(dadosSQL.getInt("nrvagas"));
		obj.setNrMaximoMatricula(dadosSQL.getInt("nrmaximomatricula"));
		obj.setNrMinimoMatricula(dadosSQL.getInt("nrminimomatricula"));
		obj.setIdentificadorTurma(dadosSQL.getString("identificadorturma"));
		obj.setTurmaAgrupada(dadosSQL.getBoolean("turmaagrupada"));
		obj.setSala(dadosSQL.getString("sala"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setConsiderarTurmaAvaliacaoInstitucional(dadosSQL.getBoolean("considerarTurmaAvaliacaoInstitucional"));
		obj.getGradeCurricularVO().setCodigo(dadosSQL.getInt("gradeCurricular"));
		obj.getGradeCurricularVO().setNome(dadosSQL.getString("gradeCurricular.nome"));
		obj.setAbreviaturaCurso(dadosSQL.getString("abreviaturaCurso"));
		obj.setNrVagasInclusaoReposicao(dadosSQL.getInt("nrVagasInclusaoReposicao"));
//		obj.getChancelaVO().setCodigo(dadosSQL.getInt("chancela"));
//		obj.getChancelaVO().setInstituicaoChanceladora(dadosSQL.getString("instituicaoChanceladora"));

		obj.setTipoChancela(dadosSQL.getString("tipochancela"));
		obj.setValorFixoChancela(dadosSQL.getDouble("valorFixoChancela"));
		obj.setPorcentagemChancela(dadosSQL.getDouble("porcentagemChancela"));
		obj.setValorPorAluno(dadosSQL.getBoolean("valorPorAluno"));

		obj.setObservacao(dadosSQL.getString("observacao"));

		obj.setCategoriaCondicaoPagamento(dadosSQL.getString("categoriaCondicaoPagamento"));

		obj.setQtdAlunosEstimado(dadosSQL.getInt("qtdAlunosEstimado"));
		obj.setCustoMedioAluno(dadosSQL.getDouble("custoMedioAluno"));
		obj.setReceitaMediaAluno(dadosSQL.getDouble("receitaMediaAluno"));
		obj.setUtilizarDadosMatrizBoleto(dadosSQL.getBoolean("utilizarDadosMatrizBoleto"));
		obj.setSubturma(dadosSQL.getBoolean("subturma"));
		obj.setTurmaPrincipal(dadosSQL.getInt("turmaPrincipal"));

		// Unidade Ensino
		obj.getUnidadeEnsino().setCodigo((dadosSQL.getInt("Unidadeensino.codigo")));
		obj.getUnidadeEnsino().setNome((dadosSQL.getString("Unidadeensino.nome")));
		// Curso
		obj.getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
		obj.getCurso().setNome(dadosSQL.getString("Curso.nome"));
		obj.getCurso().setPeriodicidade(dadosSQL.getString("Curso.periodicidade"));
		obj.getCurso().setNivelEducacional(dadosSQL.getString("Curso.nivelEducacional"));
		obj.getCurso().setLiberarRegistroAulaEntrePeriodo(dadosSQL.getBoolean("Curso.liberarregistroaulaentreperiodo"));
		obj.getCurso().getConfiguracaoAcademico().setCodigo(dadosSQL.getInt("Curso.configuracaoacademico"));
		// Turno
		obj.getTurno().setCodigo((dadosSQL.getInt("Turno.codigo")));
		obj.getTurno().setNome((dadosSQL.getString("Turno.nome")));
		// PeriodoLetivo
		obj.getPeridoLetivo().setDescricao(dadosSQL.getString("Periodoletivo.descricao"));
		obj.getPeridoLetivo().setNomeCertificacao(dadosSQL.getString("Periodoletivo.nomeCertificacao"));
		obj.getPeridoLetivo().setCodigo(dadosSQL.getInt("Periodoletivo.codigo"));
		obj.getPeridoLetivo().setPeriodoLetivo(dadosSQL.getInt("Periodoletivo.periodoLetivo"));
		obj.setTipoSubTurma(TipoSubTurmaEnum.valueOf(dadosSQL.getString("tipoSubTurma")));
		obj.setApresentarRenovacaoOnline(dadosSQL.getBoolean("apresentarRenovacaoOnline"));

		obj.setCategoriaCondicaoPagamento(dadosSQL.getString("categoriaCondicaoPagamento"));
		obj.setDigitoTurma(dadosSQL.getString("digitoTurma"));
	}

	public void executarInicializacaoDisciplinasTurma(TurmaVO turmaVO, UsuarioVO usuario) throws Exception {
		if (turmaVO.getTurmaDisciplinaVOs().isEmpty()) {
			List listaRetorno = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(turmaVO.getPeridoLetivo().getCodigo(), false, usuario, null);
			executarGeracaoTurmaDisciplinaVOs(turmaVO, listaRetorno, usuario);
		}
	}

	private void montarDadosCompleto(TurmaVO obj, SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		obj.setNivelMontarDados(NivelMontarDados.TODOS);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setIdentificadorTurma(dadosSQL.getString("identificadorturma"));
		obj.setNrVagas(dadosSQL.getInt("nrvagas"));
		obj.setNrMaximoMatricula(dadosSQL.getInt("nrmaximomatricula"));
		obj.setNrMinimoMatricula(dadosSQL.getInt("nrminimomatricula"));
		obj.setSemestral(dadosSQL.getBoolean("semestral"));
		obj.setAnual(dadosSQL.getBoolean("anual"));
		obj.getTurno().setCodigo(dadosSQL.getInt("Turno.codigo"));
		obj.setSala(dadosSQL.getString("sala"));
		obj.setTurmaAgrupada(dadosSQL.getBoolean("turmaAgrupada"));
		obj.setAbreviaturaCurso(dadosSQL.getString("abreviaturaCurso"));
		obj.setNrVagasInclusaoReposicao(dadosSQL.getInt("nrVagasInclusaoReposicao"));
		// obj.setTurmaPreMatricula(dadosSQL.getBoolean("turmaPreMatricula"));
		obj.setDataBaseGeracaoParcelas(dadosSQL.getDate("dataBaseGeracaoParcelas"));
		obj.setDataPrevisaoFinalizacao(dadosSQL.getDate("dataPrevisaoFinalizacao"));
		obj.setSubturma(dadosSQL.getBoolean("subturma"));
		obj.setTurmaPrincipal(dadosSQL.getInt("turmaPrincipal"));
		obj.setConsiderarTurmaAvaliacaoInstitucional(dadosSQL.getBoolean("considerarTurmaAvaliacaoInstitucional"));

		obj.setQtdAlunosEstimado(dadosSQL.getInt("qtdAlunosEstimado"));
		obj.setCustoMedioAluno(dadosSQL.getDouble("custoMedioAluno"));
		obj.setReceitaMediaAluno(dadosSQL.getDouble("receitaMediaAluno"));
		obj.setObservacao(dadosSQL.getString("observacao"));

		obj.getGrupoDestinatarios().setCodigo(dadosSQL.getInt("grupoDestinatarios"));
		obj.setUtilizarDadosMatrizBoleto(dadosSQL.getBoolean("utilizarDadosMatrizBoleto"));

		obj.setCategoriaCondicaoPagamento(dadosSQL.getString("categoriaCondicaoPagamento"));
		obj.setDigitoTurma(dadosSQL.getString("digitoTurma"));
		obj.setCodigoTurnoApresentarCenso(dadosSQL.getInt("codigoTurnoApresentarCenso"));

		montarDadosGrupoDestinatarios(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);

//		obj.getChancelaVO().setCodigo(dadosSQL.getInt("chancela"));
//		obj.getChancelaVO().setInstituicaoChanceladora(dadosSQL.getString("instituicaoChanceladora"));
//
//		obj.getContaCorrente().setCodigo(dadosSQL.getInt("contaCorrente"));
//		obj.getContaCorrente().setNumero(dadosSQL.getString("numero"));
//		obj.getContaCorrente().setDigito(dadosSQL.getString("digito"));

		obj.setTipoChancela(dadosSQL.getString("tipochancela"));
		obj.setValorFixoChancela(dadosSQL.getDouble("valorFixoChancela"));
		obj.setPorcentagemChancela(dadosSQL.getDouble("porcentagemChancela"));
		obj.setValorPorAluno(dadosSQL.getBoolean("valorPorAluno"));

		// Unidade Ensino
		obj.getUnidadeEnsino().setCodigo((dadosSQL.getInt("Unidadeensino.codigo")));
		obj.getUnidadeEnsino().setNome((dadosSQL.getString("Unidadeensino.nome")));
		// Curso
		obj.getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
		obj.getCurso().setNome(dadosSQL.getString("Curso.nome"));
		obj.getCurso().setPeriodicidade(dadosSQL.getString("Curso.periodicidade"));
		obj.getCurso().setNivelEducacional(dadosSQL.getString("Curso.nivelEducacional"));
		obj.getCurso().setLiberarRegistroAulaEntrePeriodo(dadosSQL.getBoolean("curso.liberarRegistroAulaEntrePeriodo"));
		// Turno
		obj.getTurno().setCodigo((dadosSQL.getInt("Turno.codigo")));
		obj.getTurno().setNome((dadosSQL.getString("Turno.nome")));
		obj.getTurno().setDescricaoTurnoContrato((dadosSQL.getString("Turno.descricaoTurnoContrato")));
		obj.getTurno().setFinalIntervalo((dadosSQL.getString("Turno.finalintervalo")));
		obj.getTurno().setInicioIntervalo((dadosSQL.getString("Turno.iniciointervalo")));
		obj.getTurno().setDuracaoAula((dadosSQL.getInt("Turno.duracaoaula")));
		obj.getTurno().setNrAulas((dadosSQL.getInt("Turno.nraulas")));
		obj.getTurno().setHoraFinal((dadosSQL.getString("Turno.horafinal")));
		obj.getTurno().setHoraInicio((dadosSQL.getString("Turno.horainicio")));
		obj.getTurno().setDuracaoIntervalo((dadosSQL.getInt("Turno.duracaointervalo")));
		obj.getTurno().setNrAulasAntes((dadosSQL.getInt("Turno.nraulasantes")));
		obj.getTurno().setConsiderarHoraAulaSessentaMinutosGeracaoDiario((dadosSQL.getBoolean("Turno.considerarhoraaulasessentaminutosgeracaodiario")));
		// PeriodoLetivo
		obj.getPeridoLetivo().setDescricao(dadosSQL.getString("Periodoletivo.descricao"));
		obj.getPeridoLetivo().setCodigo(dadosSQL.getInt("Periodoletivo.codigo"));
		obj.getPeridoLetivo().setPeriodoLetivo(dadosSQL.getInt("Periodoletivo.periodoLetivo"));
		obj.getPeridoLetivo().setNomeCertificacao(dadosSQL.getString("Periodoletivo.nomeCertificacao"));
		// Plano Financeiro
//		obj.getPlanoFinanceiroCurso().setCodigo(dadosSQL.getInt("Planofinanceirocurso.codigo"));
//		obj.getPlanoFinanceiroCurso().setDescricao(dadosSQL.getString("Planofinanceirocurso.descricao"));
//
//		obj.getCentroResultadoVO().setCodigo((dadosSQL.getInt("centroResultado.codigo")));
//		obj.getCentroResultadoVO().setDescricao(dadosSQL.getString("centroResultado.descricao"));
//		obj.getCentroResultadoVO().setIdentificadorCentroResultado(dadosSQL.getString("centroResultado.identificadorCentroResultado"));

		if (!obj.getTurmaAgrupada()) {
			obj.getGradeCurricularVO().setCodigo(dadosSQL.getInt("Gradecurricular.codigo"));
			obj.getCurso().setNome(dadosSQL.getString("Curso.nome"));
			obj.getGradeCurricularVO().setCurso(dadosSQL.getInt("Curso.codigo"));
			obj.getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
			obj.getPeridoLetivo().setCodigo(dadosSQL.getInt("Periodoletivo.codigo"));
		} else {
			getFacadeFactory().getTurmaAgrupadaFacade().carregarDadosTurmaAgrupada(obj, usuario);
		}
		/**
		 * @author Victor Hugo 22/10/2014
		 */
		obj.getAvaliacaoOnlineVO().setCodigo(dadosSQL.getInt("avaliacaoonline"));
		obj.getConfiguracaoEADVO().setCodigo(dadosSQL.getInt("configuracaoead"));
		if (Uteis.isAtributoPreenchido(obj.getCurso().getCodigo())) {
			obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, false, usuario));
			obj.getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimariaDadosMinimos(obj.getCurso().getConfiguracaoAcademico().getCodigo(), usuario));
		}
		if (Uteis.isAtributoPreenchido(obj.getAvaliacaoOnlineVO().getCodigo())) {
			obj.setAvaliacaoOnlineVO(getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarPorChavePrimaria(obj.getAvaliacaoOnlineVO().getCodigo(), 0, usuario));
		}
		if (Uteis.isAtributoPreenchido(obj.getConfiguracaoEADVO().getCodigo())) {
			obj.setConfiguracaoEADVO(getFacadeFactory().getConfiguracaoEADFacade().consultarPorChavePrimaria(obj.getConfiguracaoEADVO().getCodigo()));
		}
		obj.setTipoSubTurma(TipoSubTurmaEnum.valueOf(dadosSQL.getString("tipoSubTurma")));
		obj.setApresentarRenovacaoOnline(dadosSQL.getBoolean("apresentarRenovacaoOnline"));
//		obj.getIndiceReajusteVO().setCodigo(dadosSQL.getInt("indiceReajuste"));
		obj.setTurmaContratoVOs(getFacadeFactory().getTurmaContratoFacade().consultarPorTurma(obj.getCodigo(), usuario));
		/*
		 * 
		 */
		if (dadosSQL.getInt("TurmaDisciplina.codigo") != 0) {
			TurmaDisciplinaVO turmaDisciplinaVO = null;
			obj.getTurmaDisciplinaVOs().clear();
			do {
				turmaDisciplinaVO = new TurmaDisciplinaVO();
				turmaDisciplinaVO.setCodigo(dadosSQL.getInt("TurmaDisciplina.codigo"));
				turmaDisciplinaVO.setPermiteReposicao(dadosSQL.getBoolean("TurmaDisciplina.permiteReposicao"));
				turmaDisciplinaVO.getDisciplina().setCodigo(dadosSQL.getInt("Disciplina.codigo"));
				turmaDisciplinaVO.setPermiteApoioPresencial(dadosSQL.getBoolean("TurmaDisciplina.permiteApoioPresencial"));				
				turmaDisciplinaVO.getDisciplina().setNome(dadosSQL.getString("Disciplina.nome"));
				turmaDisciplinaVO.getDisciplina().setAbreviatura(dadosSQL.getString("Disciplina.abreviatura"));
				turmaDisciplinaVO.getDisciplinaEquivalenteTurmaAgrupada().setCodigo(dadosSQL.getInt("disciplinaEquivalenteTurmaAgrupada.codigo"));
				turmaDisciplinaVO.getDisciplinaEquivalenteTurmaAgrupada().setNome(dadosSQL.getString("disciplinaEquivalenteTurmaAgrupada.nome"));
//				turmaDisciplinaVO.getSalaLocalAula().setCodigo(dadosSQL.getInt("TurmaDisciplina.salaLocalAula"));
//				turmaDisciplinaVO.getLocalAula().setCodigo(dadosSQL.getInt("TurmaDisciplina.localAula"));
				if (!turmaDisciplinaVO.getDisciplinaEquivalenteTurmaAgrupada().getCodigo().equals(0)) {
					turmaDisciplinaVO.setMensagemDisciplinaEquivalenteTurmaAgrupada("Disciplina incluída por causa da Equivalência com a disciplina: Código = " + turmaDisciplinaVO.getDisciplinaEquivalenteTurmaAgrupada().getCodigo() + ", Nome = " + turmaDisciplinaVO.getDisciplinaEquivalenteTurmaAgrupada().getNome().toUpperCase() + ".");
				}
				turmaDisciplinaVO.setTurma(obj.getCodigo());
				// MONTANDO OS DADOS DA GRADEDISCIPLINA - PARA DISCIPLINAS
				// REGULARES DA MATRIZ
				turmaDisciplinaVO.getGradeDisciplinaVO().setCodigo(dadosSQL.getInt("turmaDisciplina.gradedisciplina"));
				turmaDisciplinaVO.getGradeDisciplinaVO().setCargaHoraria(dadosSQL.getInt("gradedisciplina.cargahoraria"));
				turmaDisciplinaVO.getGradeDisciplinaVO().getDisciplina().setCodigo(dadosSQL.getInt("gradedisciplina.disciplina"));
				turmaDisciplinaVO.getGradeDisciplinaVO().getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
				turmaDisciplinaVO.getGradeDisciplinaVO().getDisciplina().setAbreviatura(dadosSQL.getString("Disciplina.abreviatura"));
				turmaDisciplinaVO.getGradeDisciplinaVO().setNrCreditos(dadosSQL.getInt("gradedisciplina.nrcreditos"));
				turmaDisciplinaVO.getGradeDisciplinaVO().setNrCreditoFinanceiro(dadosSQL.getDouble("gradedisciplina.nrCreditoFinanceiro"));
				turmaDisciplinaVO.getGradeDisciplinaVO().setTipoDisciplina(dadosSQL.getString("gradedisciplina.tipoDisciplina"));
				turmaDisciplinaVO.getGradeDisciplinaVO().setDisciplinaComposta(dadosSQL.getBoolean("gradedisciplina.disciplinaComposta"));
				turmaDisciplinaVO.getGradeDisciplinaVO().getPeriodoLetivoVO().setCodigo(dadosSQL.getInt("gradedisciplina.periodoLetivo"));
				turmaDisciplinaVO.getGradeDisciplinaVO().setPeriodoLetivo(dadosSQL.getInt("gradedisciplina.periodoLetivo"));
				if (dadosSQL.getString("tipoControleComposicao") != null) {
					turmaDisciplinaVO.getGradeDisciplinaVO().setTipoControleComposicao(TipoControleComposicaoEnum.valueOf(dadosSQL.getString("tipoControleComposicao")));
				}
				if (dadosSQL.getString("gradedisciplina.modalidadeDisciplina") != null) {
					turmaDisciplinaVO.getGradeDisciplinaVO().setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(dadosSQL.getString("gradedisciplina.modalidadeDisciplina")));
				}
				turmaDisciplinaVO.setDisciplinaReferenteAUmGrupoOptativa(dadosSQL.getBoolean("turmaDisciplina.DisciplinaReferenteAUmGrupoOptativa"));
				// MONTANDO OS DADOS DA gradeCurricularGrupoOptativaDisciplina -
				// PARA DISCIPLINAS REFERENTES A UM GRUPO DE OPTATIVAS
				turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().setCodigo(dadosSQL.getInt("turmaDisciplina.gradeCurricularGrupoOptativaDisciplina"));
				turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().setCodigo(dadosSQL.getInt("gradeCurricularGrupoOptativaDisciplina.disciplina"));
				turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
				turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().setAbreviatura(dadosSQL.getString("Disciplina.abreviatura"));
				turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().setCargaHoraria(dadosSQL.getInt("gradeCurricularGrupoOptativaDisciplina.cargahoraria"));
				turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().setNrCreditos(dadosSQL.getInt("gradeCurricularGrupoOptativaDisciplina.nrcreditos"));
				turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().setNrCreditoFinanceiro(dadosSQL.getDouble("gradeCurricularGrupoOptativaDisciplina.nrCreditoFinanceiro"));
				turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().setDisciplinaComposta(dadosSQL.getBoolean("gradeCurricularGrupoOptativaDisciplina.disciplinaComposta"));
				turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getGradeCurricularGrupoOptativa().setCodigo(dadosSQL.getInt("gcgod.gradeCurricularGrupoOptativa"));
				if (dadosSQL.getString("gradeCurricularGrupoOptativaDisciplina.modalidadeDisciplina") != null) {
					turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(dadosSQL.getString("gradeCurricularGrupoOptativaDisciplina.modalidadeDisciplina")));
				}

				turmaDisciplinaVO.setNrAlunosMatriculados(dadosSQL.getInt("Turmadisciplina.nralunosmatriculados"));
				if (dadosSQL.getString("Turmadisciplina.modalidadeDisciplina") != null) {
					turmaDisciplinaVO.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(dadosSQL.getString("Turmadisciplina.modalidadeDisciplina")));
				}
				turmaDisciplinaVO.getConfiguracaoAcademicoVO().setCodigo(dadosSQL.getInt("TurmaDisciplina.configuracaoAcademico"));
				/**
				 * @author Victor Hugo 05/01/2015
				 */
				if (Uteis.isAtributoPreenchido(dadosSQL.getString("TurmaDisciplina.definicoestutoriaonline"))) {
					turmaDisciplinaVO.setDefinicoesTutoriaOnline(DefinicoesTutoriaOnlineEnum.valueOf(dadosSQL.getString("TurmaDisciplina.definicoestutoriaonline")));
				}
				if (Uteis.isAtributoPreenchido(dadosSQL.getInt("TurmaDisciplina.ordemestudoonline"))) {
					turmaDisciplinaVO.setOrdemEstudoOnline(dadosSQL.getInt("TurmaDisciplina.ordemestudoonline"));
				}
				turmaDisciplinaVO.setTurmaDisciplinaCompostaVOs(getFacadeFactory().getTurmaDisciplinaCompostaFacade().consultarPorTurmaDisciplina(turmaDisciplinaVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, usuario));
				obj.getTurmaDisciplinaVOs().add(turmaDisciplinaVO);
			} while (dadosSQL.next());
		}
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCurso(String identificador, Integer curso, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<>(0);
		if (Uteis.isAtributoPreenchido(unidade)) {
			unidadeEnsinoVOs.add(new UnidadeEnsinoVO(unidade));
		}
		return consultaRapidaPorIdentificadorTurmaCursoPorUnidadeEnsino(identificador, curso, unidadeEnsinoVOs, controlarAcesso, nivelMontarDados, usuario);
	}
	
	@Override
	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCursoPorUnidadeEnsino(String identificador, Integer curso, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper( turma.identificadorturma )ilike( ? ) ");
		if (Uteis.isAtributoPreenchido(curso)) {
			sql.append(" AND (turma.curso = ").append(curso).append(" OR turma.curso is null) ");
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
			sql.append(" AND (turma.unidadeensino IN (").append(unidadeEnsinoVOs.stream().map(u -> u.getCodigo().toString()).collect(Collectors.joining(", "))).append(")) ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), identificador + "%");
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCurso(String identificador, List<CursoVO> cursos, List<UnidadeEnsinoVO> unidades, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE UPPER( turma.identificadorturma ) LIKE (?) ");

		if (Uteis.isAtributoPreenchido(cursos)) {
			sql.append(" AND curso.codigo  IN (");
			int x = 0;
			for (CursoVO cursoVO : cursos) {
				if (x > 0) {
					sql.append(", ");
				}
				sql.append(cursoVO.getCodigo());
				x++;
			}
			sql.append(" ) ");
		}
		if (Uteis.isAtributoPreenchido(unidades)) {
			sql.append(" AND unidadeensino.codigo  IN (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidades) {
				if (x > 0) {
					sql.append(", ");
				}
				sql.append(unidadeEnsinoVO.getCodigo());
				x++;
			}
			sql.append(" ) ");
		}
		sql.append(" ORDER BY turma.identificadorturma; ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), identificador.toUpperCase() + "%");
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCursoPeriodicidade(String identificador, Integer curso, Integer unidade, String periodicidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper( turma.identificadorturma )like('");
		sql.append(identificador.toUpperCase());
		sql.append("%') ");
		if (curso != 0) {
			sql.append(" AND (turma.curso = ").append(curso).append(" OR turma.curso is null) ");
		}
		if (unidade.intValue() != 0) {
			sql.append(" AND (turma.unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		if (!periodicidade.equals("")) {
			sql.append(" AND ((curso.periodicidade = '").append(periodicidade).append("') OR turma.curso is null) ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCursoDisciplina(String identificadorTurma, Integer curso, Integer disciplina, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT(turma.codigo) FROM Turma ");
		sqlStr.append("WHERE sem_acentos(turma.identificadorturma) ilike sem_acentos(?) ");
		if (curso != null && !curso.equals(0)) {
			sqlStr.append("AND turma.curso = ").append(curso).append(" ");
		}
		if (disciplina != null && !disciplina.equals(0)) {
			sqlStr.append("AND exists( ");
			sqlStr.append(" select turmaDisciplina.codigo from turmaDisciplina where turmaDisciplina.turma = turma.codigo and TurmaDisciplina.disciplina = ").append(disciplina).append(" ");
			sqlStr.append(" union all ");
			sqlStr.append(" select turmaDisciplina.codigo from turmaDisciplina where turmaDisciplina.turma = turma.codigo and turma.turmaagrupada and TurmaDisciplina.disciplina != ").append(disciplina).append(" ");
			sqlStr.append(" and exists (select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.disciplina = TurmaDisciplina.disciplina and disciplinaequivalente.equivalente = ").append(disciplina).append(" )");
			sqlStr.append(" union all ");
			sqlStr.append(" select turmaDisciplina.codigo from turmaDisciplina where turmaDisciplina.turma = turma.codigo and turma.turmaagrupada and TurmaDisciplina.disciplina != ").append(disciplina).append(" ");
			sqlStr.append(" and exists (select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = TurmaDisciplina.disciplina and disciplinaequivalente.disciplina = ").append(disciplina).append(" )");
			sqlStr.append(" union all ");
			sqlStr.append(" select turmaDisciplina.codigo from turmaDisciplina inner join turmadisciplinacomposta on turmadisciplinacomposta.turmaDisciplina = turmaDisciplina.codigo inner join gradedisciplinacomposta on turmadisciplinacomposta.gradedisciplinacomposta = gradedisciplinacomposta.codigo  where turmaDisciplina.turma = turma.codigo and GradeDisciplinaComposta.disciplina = ").append(disciplina).append(" ");
			sqlStr.append(" union all ");
			sqlStr.append(" select turmaDisciplina.codigo from turmaDisciplina inner join gradedisciplinacomposta on turmaDisciplina.gradedisciplina = gradedisciplinacomposta.gradedisciplina  where turmaDisciplina.turma = turma.codigo and  GradeDisciplinaComposta.disciplina = ").append(disciplina).append(" ");
			sqlStr.append(" and not exists(select codigo from turmadisciplinacomposta where turmadisciplinacomposta.turmaDisciplina = turmaDisciplina.codigo) ");
			sqlStr.append(" union all ");
			sqlStr.append(" select turmaDisciplina.codigo from turmaDisciplina inner join gradedisciplinacomposta on turmaDisciplina.gradecurriculargrupooptativadisciplina = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina  where turmaDisciplina.turma = turma.codigo and GradeDisciplinaComposta.disciplina = ").append(disciplina).append(" ");
			sqlStr.append(" and not exists(select codigo from turmadisciplinacomposta where turmadisciplinacomposta.turmaDisciplina = turmaDisciplina.codigo) ");
			sqlStr.append(" limit 1) ");
			
		}
		if (unidadeEnsino != null && !unidadeEnsino.equals(0)) {
			sqlStr.append("AND turma.unidadeEnsino = ").append(unidadeEnsino).append(" ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), identificadorTurma.toLowerCase()+"%");
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
			carregarDados(obj, NivelMontarDados.BASICO, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCursoTurno(String identificador, Integer curso, Integer turno, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorIdentificadorTurmaCursoTurno(identificador, curso, turno, unidade, controlarAcesso, nivelMontarDados, usuario, false);
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCursoTurno(String identificador, Integer curso, Integer turno, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, boolean filtroPorTabelaCurso) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper( turma.identificadorturma )like('");
		sql.append(identificador.toUpperCase());
		sql.append("%') ");
		if (curso != 0) {
			if (filtroPorTabelaCurso) {
				sql.append(" AND curso.codigo = ").append(curso);
			} else {
				sql.append(" AND (turma.curso = ").append(curso).append(" OR (turma.turmaagrupada and exists (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma and turmaagrupada.turmaorigem = turma.codigo and t.curso = ").append(curso).append(" limit 1 ))) ");
			}
		}
		if (turno != 0) {
			sql.append(" AND turma.turno = ").append(turno);
		}
		if (unidade.intValue() != 0) {
			sql.append(" AND (turma.unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurma(String identificador, Integer unidade, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorIdentificadorTurma(identificador, 0, unidade, nivelEducacionalPosGraduacao, nivelEducacionalDiferentePosGraduacao, "", controlarAcesso, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurma(String identificador, Integer unidade, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorIdentificadorTurma(identificador, 0, unidade, nivelEducacionalPosGraduacao, nivelEducacionalDiferentePosGraduacao, "", controlarAcesso, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurma(String identificador, Integer turmaOrigem, Integer unidade, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultaRapidaPorIdentificadorTurma(identificador, turmaOrigem, unidade, nivelEducacionalPosGraduacao, nivelEducacionalDiferentePosGraduacao, "", controlarAcesso, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurma(String identificador, Integer turmaOrigem, Integer unidade, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper( turma.identificadorturma )like upper(?) ");
		if (unidade.intValue() != 0) {
			sql.append(" AND (turma.unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX', 'MT'))");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND (curso.nivelEducacional != 'PO')");
		}
		if (turmaOrigem != 0) {
			sql.append(" AND turma.codigo <> ").append(turmaOrigem.intValue()).append(" ");
		}
		if (situacao != null && !situacao.isEmpty()) {
			sql.append(" AND turma.situacao = '").append(situacao).append("' ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), identificador + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaUnidadeEnsinoECurso(String identificador, Integer curso, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper( turma.identificadorturma )like('");
		sql.append(identificador.toUpperCase());
		sql.append("%'");
		sql.append(" )");
		if (curso.intValue() != 0) {
			sql.append(" AND (curso.codigo = ").append(curso.intValue()).append(") ");
		}
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND (unidadeensino.codigo = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaNivelEducacional(String identificador, List<ProgramacaoFormaturaUnidadeEnsinoVO> prograFormaturaUnidadeEnsinoVOs, String nivelEducacional, List<TurnoVO> turnoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper( turma.identificadorturma ) like upper(?) ");
		if (Uteis.isAtributoPreenchido(prograFormaturaUnidadeEnsinoVOs)) {
			sql.append(" AND turma.unidadeEnsino in (");
			for (ProgramacaoFormaturaUnidadeEnsinoVO programacaoFormaturaUnidadeEnsinoVO : prograFormaturaUnidadeEnsinoVOs) {
				if (!prograFormaturaUnidadeEnsinoVOs.get(prograFormaturaUnidadeEnsinoVOs.size() - 1).getUnidadeEnsinoVO().getCodigo().equals(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo())) {
					sql.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo()+", ");
				} else {
					sql.append(programacaoFormaturaUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo());
				}
			}
			sql.append(") ");
		}
		if (Uteis.isAtributoPreenchido(turnoVOs)) {
			sql.append(" AND turno.codigo in (");
			for (TurnoVO turno : turnoVOs) {
				if (!turnoVOs.get(turnoVOs.size() - 1).getCodigo().equals(turno.getCodigo())) {
					sql.append(turno.getCodigo()+", ");
				} else {
					sql.append(turno.getCodigo());
				}
			}
			sql.append(") ");
		}
		if (!nivelEducacional.equals("")) {
			sql.append(" AND curso.nivelEducacional in ('").append(nivelEducacional.toUpperCase()).append("') ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), identificador + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public TurmaVO consultaRapidaPorMatriculaPeriodoDadosContaCorrente(Integer matriculaPeriodo, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT turma.codigo, turma.identificadorTurma, turma.databasegeracaoparcelas, contaCorrente.codigo AS \"contaCorrente\" ");
		sql.append(" FROM turma ");
		sql.append(" LEFT JOIN contaCorrente ON contaCorrente.codigo = turma.contaCorrente ");
		sql.append(" INNER JOIN matriculaPeriodo ON matriculaPeriodo.turma = turma.codigo ");
		sql.append(" WHERE matriculaPeriodo.codigo = ").append(matriculaPeriodo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		TurmaVO obj = new TurmaVO();
		if (!tabelaResultado.next()) {
			return new TurmaVO();
		}
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
		obj.setDataBaseGeracaoParcelas(tabelaResultado.getDate("databasegeracaoparcelas"));
//		obj.getContaCorrente().setCodigo(tabelaResultado.getInt("contaCorrente"));
		return obj;
	}

	public TurmaDisciplinaVO obterLocalSalaTurmaDisciplinaLog(TurmaDisciplinaVO turmaDisc, Integer turma) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from logturmadisciplina  where turma is not null and localaula <> '' and salalocalAula <> '' ");
		sql.append(" and turma = ").append(turma);
		sql.append(" and disciplina ilike '").append(turmaDisc.getDisciplina().getCodigo()).append("%'");
		sql.append(" order by codigo desc limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return turmaDisc;
		}
		String local = tabelaResultado.getString("localAula").replaceAll(" ", "");
		String salalocal = tabelaResultado.getString("salalocalAula").replaceAll(" ", "");
		Double avaliacao = tabelaResultado.getDouble("avaliacao");
//		turmaDisc.getLocalAula().setCodigo(Integer.parseInt(local.substring(0, local.indexOf("-"))));
//		turmaDisc.getSalaLocalAula().setCodigo(Integer.parseInt(salalocal.substring(0, salalocal.indexOf("-"))));
		turmaDisc.setAvaliacao(avaliacao);
		return turmaDisc;
	}

	public TurmaVO consultaRapidaPorIdentificadorTurma(TurmaVO obj, String identificador, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper(turma.identificadorturma) like upper(?) ");
		if (unidade.intValue() != 0) {
			sql.append(" AND (turma.unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), identificador + PERCENT);
		if (!tabelaResultado.next()) {
			throw new Exception("Dados não encontrados (Turma)");
		}
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}

	public TurmaVO consultaRapidaPorIdentificadorTurmaEspecifico(TurmaVO obj, String identificador, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper(turma.identificadorturma) = upper(?) ");
		if (unidade.intValue() != 0) {
			sql.append(" AND (turma.unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), identificador);
		if (!tabelaResultado.next()) {
			throw new Exception("Dados não encontrados (Turma)");
		}
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}

	public TurmaVO consultaRapidaUnicidadeTurmaPorIdentificador(String identificador, Integer codigo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT Turma.codigo, Turma.identificadorturma ");
		sql.append("from turma ");
		sql.append(" WHERE upper(trim(identificadorturma)) = upper(trim('").append(identificador.toUpperCase()).append("')) ");
		sql.append(" and codigo != ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		TurmaVO obj = new TurmaVO();
		if (!tabelaResultado.next()) {
			return obj;
		}
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setIdentificadorTurma(tabelaResultado.getString("identificadorturma"));
		return obj;
	}

	@Override
	public TurmaVO consultaRapidaPorIdentificadorTurmaEspecificoProfessorUnidadeEnsino(TurmaVO obj, String identificador, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE turma.identificadorturma = '").append(identificador).append("'");
		if (professor.intValue() != 0) {
			sql.append(" AND turma.codigo in (select distinct turma.codigo from horarioturmaprofessordisciplina inner join turma on turma.codigo = horarioturmaprofessordisciplina.turma where professor = ").append(professor.intValue()).append(" and turma.identificadorTurma = '" + identificador + "') ");
		}
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" AND (turma.unidadeensino = ").append(unidadeEnsino.intValue()).append(") ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			throw new Exception("Dados não encontrados (Turma)");
		}
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}

	public List<TurmaVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			montarDadosBasico(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public List<TurmaVO> montarDadosConsultaCompleta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			montarDadosCompleto(obj, tabelaResultado, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public void carregarDados(TurmaVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((TurmaVO) obj, NivelMontarDados.TODOS, usuario);
	}

	public TurmaVO carregarDadosTurmaAgrupada(TurmaVO obj, UsuarioVO usuario) throws Exception {
		carregarDados((TurmaVO) obj, NivelMontarDados.BASICO, usuario);
		return obj;
	}

	public void carregarDados(TurmaVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		SqlRowSet resultado = null;
		if ((nivelMontarDados.equals(NivelMontarDados.BASICO)) && (obj.getIsNivelMontarDadosNaoInicializado())) {
			resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getCodigo(), usuario);
			montarDadosBasico(obj, resultado);
		}
		if (((nivelMontarDados.equals(NivelMontarDados.TODOS)) && (!obj.getIsNivelMontarDadosTodos())) || (nivelMontarDados.equals(NivelMontarDados.FORCAR_RECARGATODOSOSDADOS))) {
			resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getCodigo(), usuario);
			montarDadosCompleto(obj, resultado, usuario);
		}
	}

	public SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer turma, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (turma.codigo= '").append(turma).append("')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados não Encontrados. (Turma)");
		}
		return tabelaResultado;
	}

	public TurmaVO consultaRapidaPorChavePrimariaDadosBasicosTurmaAgrupada(Integer turma, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE (turma.codigo= '").append(turma).append("')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet((sqlStr.toString()));
		tabelaResultado.next();
		TurmaVO obj = new TurmaVO();
		montarDadosBasico(obj, tabelaResultado);
		return obj;

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public TurmaVO consultaRapidaPorMatriculaUltimaMatriculaPeriodo(String matricula, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select turma.* from turma ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.turma = turma.codigo ");
		sqlStr.append(" and matriculaperiodo.codigo = (select codigo from matriculaperiodo where matricula = '");
		sqlStr.append(matricula);
		sqlStr.append("' order by ano||semestre desc, codigo desc limit 1 )");
		sqlStr.append(" where matricula = '");
		sqlStr.append(matricula);
		sqlStr.append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return null;
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);

	}

	public TurmaVO consultaRapidaPorMatriculaUltimaMatriculaPeriodoPorAnoSemestrePeriodoLetivo(String matricula, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select turma.* from turma ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.turma = turma.codigo ");
		sqlStr.append(" and matriculaperiodo.codigo = (select codigo from matriculaperiodo where matricula = '").append(matricula).append("' ");
		sqlStr.append(" order by (MatriculaPeriodo.ano||'/'|| MatriculaPeriodo.semestre) desc, MatriculaPeriodo.codigo desc limit 1) ");
		sqlStr.append(" where matricula = '");
		sqlStr.append(matricula);
		sqlStr.append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new TurmaVO();
		}
		return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);

	}

	private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer turma, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
		sqlStr.append(" WHERE (turma.codigo= '").append(turma).append("')");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados não Encontrados (Turma).");
		}
		return tabelaResultado;
	}

	@Override
	public List<TurmaVO> consultar(String campoConsultaTurma, String valorConsultaTurma, Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelmontardadosDadosminimos, UsuarioVO usuario) throws Exception {
		List<TurmaVO> objs = new ArrayList<>(0);
		if (campoConsultaTurma.equals("identificadorTurma")) {
			objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(valorConsultaTurma, codigoUnidadeEnsino, false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}
		if (campoConsultaTurma.equals("nomeUnidadeEnsino")) {
			objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(valorConsultaTurma, codigoUnidadeEnsino, false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}
		if (campoConsultaTurma.equals("nomeTurno")) {
			objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(valorConsultaTurma, codigoUnidadeEnsino, false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}
		if (campoConsultaTurma.equals("nomeCurso")) {
			objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(valorConsultaTurma, codigoUnidadeEnsino, false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		}
		return objs;
	}

	public List<TurmaVO> consultarPorCursoOrdenadoPorTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		String sqlStr = "SELECT turma.* FROM turma inner join curso on turma.curso = curso.codigo WHERE curso.codigo = " + valorConsulta + " ORDER BY turma.identificadorturma";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuarioVO);
	}

	public List<TurmaVO> consultarPorNomeTurnoCurso(String valorConsulta, Integer curso, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Turma.* FROM Turma, Turno WHERE Turma.turno = Turno.codigo and lower (Turno.nome) like('" + valorConsulta.toLowerCase() + "%') AND curso = " + curso + " ORDER BY Turno.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultarPorTurnoCursoUnidadeEnsino(Integer turno, Integer curso, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT Turma.* FROM Turma WHERE 1=1");
		if (turno != null && !turno.equals(0)) {
			sqlStr.append("AND Turma.turno = ").append(turno);
		}
		if (curso != null && !curso.equals(0)) {
			sqlStr.append(" AND Turma.curso = ").append(curso);
		}
		if (unidade != null && !unidade.equals(0)) {
			sqlStr.append(" AND Turma.unidadeEnsino = ").append(unidade);
		}
		sqlStr.append(" ORDER BY Turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}



	public List<TurmaVO> consultaRapidaPorCodigoTurmaCursoUnidadeEnsino(Integer turma, Integer curso, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append(" WHERE 1=1");
		if (turma != null && turma != 0) {
			sqlStr.append(" AND (turma.codigo= '").append(turma).append("') ");
		}
		if (curso != null && curso != 0) {
			sqlStr.append(" AND curso.codigo = ").append(curso).append(" ");
		}
		if (unidadeEnsino != null && unidadeEnsino != 0) {
			sqlStr.append(" AND unidadeensino.codigo = ").append(unidadeEnsino).append(" ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public TurmaVO consultaRapidaPorIdentificadorTurma(TurmaVO obj, String identificador, Integer curso, Integer unidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper( turma.identificadorturma )like('");
		sql.append(identificador.toUpperCase());
		sql.append("%'");
		sql.append(" )");
		if (unidade.intValue() != 0) {
			sql.append(" AND (turma.unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		if (curso.intValue() != 0) {
			sql.append(" AND (turma.curso = ").append(curso.intValue()).append(") ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			throw new Exception("Dados não encontrados (Turma)");
		}
		montarDadosBasico(obj, tabelaResultado);
		return obj;
	}

	public Boolean consultarExisteMatriculaVinculadaTurma(TurmaVO turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" SELECT distinct turma.codigo AS \"turma.codigo\" FROM turma ");
		sqlStr.append(" WHERE turma.codigo = ").append(turma.getCodigo()).append(" and (");
		if (!turma.getTurmaAgrupada() && (!turma.getSubturma() || (turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.GERAL)))) {
			sqlStr.append(" (exists (select codigo from matriculaperiodoturmadisciplina mptd where mptd.turma = turma.codigo limit 1)");
			sqlStr.append(" or exists (select codigo from matriculaperiodo mp where mp.turma = turma.codigo limit 1))");
		} else if (turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
			sqlStr.append(" (exists (select codigo from matriculaperiodoturmadisciplina mptd where mptd.turmapratica = turma.codigo limit 1))");
		} else if (turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
			sqlStr.append(" (exists (select codigo from matriculaperiodoturmadisciplina mptd where mptd.turmateorica = turma.codigo limit 1))");
		} else if (turma.getTurmaAgrupada()) {
			sqlStr.append(" (exists (select codigo from matriculaperiodoturmadisciplina mptd where exists (select turmaagrupada.turma from turmaagrupada where turmaorigem = turma.codigo and mptd.turma = turmaagrupada.turma ) ");
			sqlStr.append(" and (exists (select turmadisciplina.codigo from turmadisciplina where turmadisciplina.turma = turma.codigo and turmadisciplina.disciplina = mptd.disciplina) ");
			sqlStr.append(" or exists (select turmadisciplina.codigo from turmadisciplina inner join disciplinaequivalente on disciplinaequivalente.disciplina = turmadisciplina.disciplina where turmadisciplina.turma = turma.codigo and disciplinaequivalente.equivalente = mptd.disciplina) ");
			sqlStr.append(" or exists (select turmadisciplina.codigo from turmadisciplina inner join disciplinaequivalente on disciplinaequivalente.equivalente = turmadisciplina.disciplina where turmadisciplina.turma = turma.codigo and disciplinaequivalente.disciplina = mptd.disciplina)) ");
			sqlStr.append(" and exists (select registroaula.codigo from frequenciaaula inner join registroaula on registroaula.codigo = frequenciaaula.registroaula where registroaula.turma = turma.codigo and frequenciaaula.matriculaperiodoturmadisciplina = mptd.codigo limit 1) ");
			sqlStr.append(" limit 1)) ");
		}
		sqlStr.append(" or (exists (select hpdi.codigo from horarioprofessordiaitem hpdi where hpdi.turma = turma.codigo limit 1)) )");
		sqlStr.append(" LIMIT 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado.next();
	}

	public List<TurmaVO> consultarExisteSubTurmaCadastradaTurmaPrincipal(Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" select turma.codigo, turma.identificadorTurma, turma.situacao, turma.tipoSubTurma, '' as turmaPrincipal, turma.turmaagrupada, ");
		sqlStr.append(" array_to_string(array_agg(disciplina.nome order by disciplina.nome), ';') as disciplinas ");
		sqlStr.append(" from turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sqlStr.append(" inner join disciplina on turmadisciplina.disciplina = disciplina.codigo ");
		sqlStr.append(" where turma.subturma and turma.turmaPrincipal = ").append(turma);
		sqlStr.append(" group by turma.codigo, turma.identificadorTurma, turma.situacao, turma.tipoSubTurma, turma.turmaagrupada ");
		sqlStr.append(" union all ");
		sqlStr.append(" select turma.codigo, turma.identificadorTurma, turma.situacao, turma.tipoSubTurma, turmaPrincipal.identificadorTurma as turmaPrincipal, turma.turmaagrupada, ");
		sqlStr.append(" array_to_string(array_agg(distinct disciplina.nome order by disciplina.nome), ';') as disciplinas ");
		sqlStr.append(" from turma ");
		sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo and turmaagrupada.turma = ").append(turma);
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sqlStr.append(" inner join turmadisciplina td2 on td2.turma = ").append(turma);
		sqlStr.append(" and (td2.disciplina = turmadisciplina.disciplina or td2.disciplina = turmadisciplina.disciplinaEquivalenteTurmaAgrupada)");
		sqlStr.append(" inner join disciplina on turmadisciplina.disciplina = disciplina.codigo ");
		sqlStr.append(" inner join turma as turmaPrincipal on turmaPrincipal.codigo = turma.turmaPrincipal ");
		sqlStr.append(" where turma.turmaagrupada and turma.subturma ");
		sqlStr.append(" group by turma.codigo, turma.identificadorTurma, turma.situacao, turma.tipoSubTurma, turmaPrincipal.identificadorTurma, turma.turmaagrupada ");
		sqlStr.append(" order by situacao, identificadorTurma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<TurmaVO> listaTurma = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
			obj.setSituacao(tabelaResultado.getString("situacao"));
			obj.setTipoSubTurma(TipoSubTurmaEnum.valueOf(tabelaResultado.getString("tipoSubTurma")));
			obj.setIdentificadorTurmaPrincipal(tabelaResultado.getString("turmaPrincipal"));
			obj.setTurmaAgrupada(tabelaResultado.getBoolean("turmaAgrupada"));
			for (String disciplina : tabelaResultado.getString("disciplinas").split(";")) {
				TurmaDisciplinaVO turmaDisciplinaVO = new TurmaDisciplinaVO();
				turmaDisciplinaVO.getDisciplina().setNome(disciplina);
				obj.getTurmaDisciplinaVOs().add(turmaDisciplinaVO);
			}
			listaTurma.add(obj);
		}
		return listaTurma;
	}

	public List<TurmaVO> consultarExisteTurmaAgrupadaEnvolvendoTurmaPrincipal(Integer turma, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" select turma.codigo, turma.identificadorTurma, turma.situacao,  ");
		sqlStr.append(" array_to_string(array_agg(distinct disciplina.nome order by disciplina.nome), ';') as disciplinas ");
		sqlStr.append(" from turma ");
		sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo and turmaagrupada.turma = ").append(turma);
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sqlStr.append(" inner join turmadisciplina td2 on td2.turma = ").append(turma);
		sqlStr.append(" and (td2.disciplina = turmadisciplina.disciplina or td2.disciplina = turmadisciplina.disciplinaEquivalenteTurmaAgrupada)");
		sqlStr.append(" inner join disciplina on turmadisciplina.disciplina = disciplina.codigo ");
		sqlStr.append(" where turma.turmaagrupada and turma.subturma = false ");
		sqlStr.append(" group by turma.codigo, turma.identificadorTurma, turma.situacao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<TurmaVO> listaTurma = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
			obj.setSituacao(tabelaResultado.getString("situacao"));
			for (String disciplina : tabelaResultado.getString("disciplinas").split(";")) {
				TurmaDisciplinaVO turmaDisciplinaVO = new TurmaDisciplinaVO();
				turmaDisciplinaVO.getDisciplina().setNome(disciplina);
				obj.getTurmaDisciplinaVOs().add(turmaDisciplinaVO);
			}
			listaTurma.add(obj);
		}
		return listaTurma;
	}

	public List<TurmaVO> consultaRapidaPorCoordenador(Integer coordenador, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, Boolean trazerTurmaAgrupada, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(")) OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
		sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(" AND cc.turma IS NULL))");

		if (trazerTurmaAgrupada) {
			// Esse OR ele vai procurar por turma agrupada
			sql.append(" OR (turma.codigo IN (");
			sql.append(" SELECT distinct turmaagrupada.turmaorigem FROM turmaagrupada ");
			sql.append(" INNER JOIN turma ON turma.codigo = turmaagrupada.turma ");
			sql.append(" WHERE turma.codigo in(SELECT cc.turma FROM cursoCoordenador cc ");
			sql.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario  ");
			sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa  ");
			sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(") ");
			sql.append(" or turma.curso in(");
			sql.append(" SELECT DISTINCT cc.curso FROM cursoCoordenador cc  ");
			sql.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario  ");
			sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
			sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(" AND cc.turma IS NULL ))))");
		} else {
			sql.append(") ");
		}

		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sql.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX', 'MT')) ");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND (curso.nivelEducacional != 'PO') ");
		}
		sql.append(" AND turma.situacao = 'AB' ");
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaCoordenador(Integer coordenador, String identificadorTurma, Integer unidadeEnsino, boolean controlarAcesso, boolean buscarApenasPorSituacaoAberto, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		List<Object> parametros = new ArrayList<>();
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(")) OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
		sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(" AND cc.turma IS NULL))");
		sql.append(") ");
		if (identificadorTurma != null && !identificadorTurma.equals("")) {
			sql.append(" AND upper(sem_acentos(turma.identificadorTurma)) like upper(sem_acentos(?)) ");
			parametros.add(PERCENT + identificadorTurma + PERCENT);
		}
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sql.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino);
		}
		if(buscarApenasPorSituacaoAberto) {
			sql.append(" AND turma.situacao = 'AB' ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), parametros.toArray());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public List<TurmaVO> consultaRapidaPorCoordenadorAnoSemestre(Integer coordenador, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, Boolean trazerTurmaAgrupada, Boolean buscarTurmasAnteriores, String ano, String semestre, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" LEFT JOIN horarioturma on horarioturma.turma = turma.codigo ");
		sql.append(" WHERE ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(")) OR (curso.codigo IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
		sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(" AND cc.turma IS NULL))");
		if (trazerTurmaAgrupada) {
			// Esse OR ele vai procurar por turma agrupada
			sql.append(" OR (turma.codigo IN (");
			sql.append(" SELECT distinct turmaagrupada.turmaorigem FROM turmaagrupada ");
			sql.append(" INNER JOIN turma ON turma.codigo = turmaagrupada.turma ");
			sql.append(" WHERE turma.codigo in(SELECT cc.turma FROM cursoCoordenador cc ");
			sql.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario  ");
			sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa  ");
			sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(") ");
			sql.append(" or turma.curso in(");
			sql.append(" SELECT DISTINCT cc.curso FROM cursoCoordenador cc  ");
			sql.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario  ");
			sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
			sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(" AND cc.turma IS NULL ))))");
		} else {
			sql.append(") ");
		}
		
			if (!Uteis.isAtributoPreenchido(semestre) || !Uteis.isAtributoPreenchido(ano)) {
				if (Uteis.isAtributoPreenchido(ano)) {
					sql.append(" and ((turma.semestral  and  HorarioTurma.anovigente = '").append(ano).append("') ");
					sql.append(" or (turma.anual and  HorarioTurma.anovigente = '").append(ano).append("') ");
					sql.append(" or (turma.anual = false and  turma.semestral = false)) ");	
				}
			} else {
				sql.append(" and ((turma.semestral and HorarioTurma.semestrevigente = '").append(semestre).append("' and  HorarioTurma.anovigente = '").append(ano).append("') ");
				sql.append(" or (turma.anual and  HorarioTurma.anovigente = '").append(ano).append("') ");
				sql.append(" or (turma.anual = false and  turma.semestral = false)) ");
			}
		
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sql.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX') OR turma.turmaagrupada = true) ");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND (curso.nivelEducacional != 'PO' OR turma.turmaagrupada = true  or turma.subturma = true) ");
		}
		sql.append(" AND turma.situacao = 'AB' ");
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultaRapidaPorProfessorCoordenador(Integer professor, Integer coordenador, Integer unidadeEnsino, String ano, String semestre, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append("INNER JOIN horarioTurmaProfessorDisciplina htpd on turma.codigo = htpd.turma ");
		sql.append("INNER JOIN horarioturma ON horarioturma.codigo = htpd.horarioturma ");
		sql.append("WHERE ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sql.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sql.append("WHERE pessoa.codigo = ").append(coordenador).append(")) OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
		sql.append("INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sql.append("INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sql.append("WHERE pessoa.codigo = ").append(coordenador).append(" AND cc.turma IS NULL))) ");
		sql.append("AND htpd.professor = ").append(professor).append(" ");
		if (unidadeEnsino != null) {
			sql.append("AND turma.unidadeEnsino = ").append(unidadeEnsino).append(" ");
		}
		sql.append("AND ((curso.periodicidade = 'IN' AND horarioturma.anoVigente = '' AND horarioturma.semestreVigente = '' ) OR ");
		sql.append(" (curso.periodicidade = 'SE' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '").append(semestre).append("') OR ");
		sql.append(" (curso.periodicidade = 'AN' AND horarioturma.anoVigente = '").append(ano).append("' AND horarioturma.semestreVigente = '')) ");
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public List<TurmaVO> consultaTurmaDoProfessor(Integer professor, Integer unidadeEnsino, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder("select distinct Turma.* from Turma ");
		sql.append("INNER JOIN horarioTurmaProfessorDisciplina htpd on turma.codigo = htpd.turma ");
		sql.append("INNER JOIN horarioturma ON horarioturma.codigo = htpd.horarioturma ");
		sql.append("WHERE htpd.professor = ").append(professor).append(" ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sql.append("AND turma.unidadeEnsino = ").append(unidadeEnsino).append(" ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaVO> consultaRapidaPorCoordenadorComMatriculaPeriodoAtiva(Integer coordenador, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc INNER JOIN funcionario ON funcionario.codigo = cc.funcionario");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
		sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(")) OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
		sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(" AND cc.turma IS NULL)))");
		sql.append(" AND turma.codigo IN(SELECT DISTINCT turma.codigo FROM turma");
		sql.append(" INNER JOIN matriculaPeriodo mp ON mp.turma = turma.codigo");
		sql.append(" WHERE mp.situacaoMatriculaPeriodo = 'AT')");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sql.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino);
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public void validarDadosAtualizacaoDisciplinaAlunosTurma(List<MatriculaPeriodoVO> listaMatriculaPeriodoVO) throws Exception {
		if (listaMatriculaPeriodoVO.isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_Turma_alunoNaoEncontradoAtualizacaoDisciplinaAlunoTurma"));
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarAtualizacaoDisciplinaAlunosTurma(Boolean atualizarDisciplinaAlunos, List<MatriculaPeriodoVO> listaMatriculaPeriodoVO, TurmaVO turmaVO,  UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario) throws Exception {
		try {
			if (atualizarDisciplinaAlunos) {
				validarDadosAtualizacaoDisciplinaAlunosTurma(listaMatriculaPeriodoVO);
				// listaMatriculaPeriodoVO =
				// getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaMatriculaPeriodoUnicaPorTurma(listaMatricula,
				// 0, turmaVO.getCodigo(),
				// true, usuario);
				if (!listaMatriculaPeriodoVO.isEmpty()) {
					executarCriacaoMatriculaPeriodoTurmaDisciplina(turmaVO, listaMatriculaPeriodoVO, usuario);
				}
				executarAlteracaoPeriodoLetivoGradeCurricularMatriculaPeriodo(turmaVO, "AT", "AT", listaMatriculaPeriodoVO, usuario, false);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	// AtualizaÃ§Ã£oo lenta
	// @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED,
	// rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	// public void executarAtualizacaoDisciplinaAlunosTurma(Boolean
	// atualizarDisciplinaAlunos, List<MatriculaVO> listaMatricula, TurmaVO
	// turmaVO
	//  UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO
	// usuario) throws Exception {
	//
	// if (atualizarDisciplinaAlunos) {
	// MatriculaPeriodoVO matriculaPeriodoVO = new MatriculaPeriodoVO();
	// List<MatriculaPeriodoTurmaDisciplinaVO>
	// listaMatriculaPeriodoTurmaDisciplinaVOs = new ArrayList(0);
	// validarDadosAtualizacaoDisciplinaAlunosTurma(listaMatricula);
	// try {
	// for (MatriculaVO obj : listaMatricula) {
	// // matriculaPeriodoVO =
	// getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatricula(obj.getMatricula(),
	// true, Uteis.NIVELMONTARDADOS_COMBOBOX,
	//  usuario);
	// matriculaPeriodoVO =
	// getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaMatriculaPeriodoUnica(obj.getMatricula(),
	// 0, true, usuario);
	// executarCriacaoMatriculaPeriodoTurmaDisciplina(obj.getMatricula(),
	// matriculaPeriodoVO, turmaVO.getTurmaDisciplinaVOs(),
	//  usuario);
	// executarAlteracaoPeriodoLetivoGradeCurricularMatriculaPeriodo(matriculaPeriodoVO,
	// turmaVO);
	// }
	// } finally {
	// listaMatriculaPeriodoTurmaDisciplinaVOs.clear();
	// }
	// }
	// }
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarCriacaoMatriculaPeriodoTurmaDisciplina(TurmaVO turmaVO, List<MatriculaPeriodoVO> listaMatriculaPeriodoVO,  UsuarioVO usuario) throws Exception {
		try {
			List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVO = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorMatriculaDisciplinaSemRegistro(turmaVO, turmaVO.getTurmaDisciplinaVOs(), false, usuario);
			HashMap<String, MatriculaPeriodoVO> hashMapMatriculaPeriodoVO = new HashMap<String, MatriculaPeriodoVO>(0);
			HashMap<Integer, TurmaDisciplinaVO> hashMapDisciplinaVO = new HashMap<Integer, TurmaDisciplinaVO>(0);
			montarHashMapsMatriculaPeriodoDisciplina(listaMatriculaPeriodoVO, turmaVO.getTurmaDisciplinaVOs(), hashMapMatriculaPeriodoVO, hashMapDisciplinaVO);
			MatriculaPeriodoVO matriculaPeriodoVO = null;
			TurmaDisciplinaVO turmaDisciplinaVO = null;
			for (MatriculaPeriodoTurmaDisciplinaVO mptd : listaMatriculaPeriodoTurmaDisciplinaVO) {
				if (hashMapMatriculaPeriodoVO.containsKey(mptd.getMatricula()) && hashMapDisciplinaVO.containsKey(mptd.getDisciplina().getCodigo())) {
					matriculaPeriodoVO = hashMapMatriculaPeriodoVO.get(mptd.getMatricula());
					turmaDisciplinaVO = hashMapDisciplinaVO.get(mptd.getDisciplina().getCodigo());
					inicializarDadosMatriculaPeriodoTurmaDisciplina(mptd, matriculaPeriodoVO, turmaDisciplinaVO, turmaVO);
					if (!mptd.getMatriculaPeriodo().equals(0)) {
						getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().incluir(mptd, turmaVO.getGradeCurricularVO(), usuario);
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	// AtualizaÃ§Ã£oo lenta
	// @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED,
	// rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	// public void executarCriacaoMatriculaPeriodoTurmaDisciplina(String
	// matricula, MatriculaPeriodoVO matriculaPeriodo, List<TurmaDisciplinaVO>
	// listaTurmaDisciplinaVOs,
	//  UsuarioVO usuario)
	// throws Exception {
	// MatriculaPeriodoTurmaDisciplinaVO mptd = new
	// MatriculaPeriodoTurmaDisciplinaVO();
	// for (TurmaDisciplinaVO turmaDisciplinaVO : listaTurmaDisciplinaVOs) {
	// MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO =
	// getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorMatriculaDisciplina(matricula,
	// turmaDisciplinaVO.getDisciplina().getCodigo(), false,
	// Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
	// if (matriculaPeriodoTurmaDisciplinaVO.getCodigo() == 0) {
	// mptd = inicializarDadosMatriculaPeriodoTurmaDisciplina(matricula,
	// matriculaPeriodo, turmaDisciplinaVO.getDisciplina(),
	// turmaDisciplinaVO.getTurma());
	// if (mptd.getMatriculaPeriodo() != 0) {
	// getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().incluir(mptd,
	//  usuario);
	// }
	// }
	// }
	// }
	public void inicializarDadosMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO mptd, MatriculaPeriodoVO matriculaPeriodo, TurmaDisciplinaVO turmaDisciplinaVO, TurmaVO turmaVO) {
		mptd.setMatriculaPeriodo(matriculaPeriodo.getCodigo());
		mptd.setDisciplina(turmaDisciplinaVO.getDisciplina());
		mptd.getGradeDisciplinaVO().setCodigo(turmaDisciplinaVO.getGradeDisciplinaVO().getCodigo());
		mptd.getGradeCurricularGrupoOptativaDisciplinaVO().setCodigo(turmaDisciplinaVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo());
		mptd.setModalidadeDisciplina(turmaDisciplinaVO.getModalidadeDisciplina());
		mptd.getTurma().setCodigo(turmaVO.getCodigo());
		mptd.setAno(matriculaPeriodo.getAno());
		mptd.setSemestre(matriculaPeriodo.getSemestre());
		mptd.setDisciplinaIncluida(Boolean.TRUE);
		
		}

	public Integer inicializarGradeDisciplinaTurmaDisciplina(TurmaVO turmaVO, Integer disciplina) {
		for (TurmaDisciplinaVO turmaDisciplinaVO : turmaVO.getTurmaDisciplinaVOs()) {
			if (turmaDisciplinaVO.getDisciplina().getCodigo().equals(disciplina) && !turmaDisciplinaVO.getGradeDisciplinaVO().getCodigo().equals(0)) {
				return turmaDisciplinaVO.getGradeDisciplinaVO().getCodigo();
			}
		}
		return 0;
	}

	public void montarHashMapsMatriculaPeriodoDisciplina(List<MatriculaPeriodoVO> listaMatriculaPeriodoVO, List<TurmaDisciplinaVO> turmaDisciplinaVOs, HashMap<String, MatriculaPeriodoVO> hashMapMatriculaPeriodoVO, HashMap<Integer, TurmaDisciplinaVO> hashMapDisciplinaVO) {
		for (MatriculaPeriodoVO matriculaPeriodoVO : listaMatriculaPeriodoVO) {
			hashMapMatriculaPeriodoVO.put(matriculaPeriodoVO.getMatricula(), matriculaPeriodoVO);
		}
		for (TurmaDisciplinaVO turmaDisciplinaVO : turmaDisciplinaVOs) {
			hashMapDisciplinaVO.put(turmaDisciplinaVO.getDisciplina().getCodigo(), turmaDisciplinaVO);
		}
	}

	// InicializaÃ§Ã£oo da consulta Lenta
	// public MatriculaPeriodoTurmaDisciplinaVO
	// inicializarDadosMatriculaPeriodoTurmaDisciplina(String matricula,
	// MatriculaPeriodoVO matriculaPeriodo, DisciplinaVO disciplina,
	// Integer turma) {
	// MatriculaPeriodoTurmaDisciplinaVO mptd = new
	// MatriculaPeriodoTurmaDisciplinaVO();
	// mptd.setMatricula(matricula);
	// mptd.setMatriculaPeriodo(matriculaPeriodo.getCodigo());
	// mptd.setDisciplina(disciplina);
	// mptd.getTurma().setCodigo(turma);
	// mptd.setAno(matriculaPeriodo.getAno());
	// mptd.setSemestre(matriculaPeriodo.getSemestre());
	// mptd.setDisciplinaIncluida(Boolean.TRUE);
	// return mptd;
	// }
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarAlteracaoPeriodoLetivoGradeCurricularMatriculaPeriodo(TurmaVO turmaVO, String situacaoMatricula, String situacaoMatriculaPeriodo, List<MatriculaPeriodoVO> listaMatriculaPeriodoVO, UsuarioVO usuarioVO, Boolean realizandoTransferenciaMatrizCurricularPelaTurma) throws Exception {
		if (!turmaVO.getPeridoLetivo().getCodigo().equals(0) && !turmaVO.getGradeCurricularVO().getCodigo().equals(0)) {
			getFacadeFactory().getMatriculaPeriodoFacade().alterarPeriodoLetivoGradeCurricularPorMatriculaPeriodoTurma(turmaVO, situacaoMatricula, situacaoMatriculaPeriodo, listaMatriculaPeriodoVO, usuarioVO, realizandoTransferenciaMatrizCurricularPelaTurma);
			getFacadeFactory().getMatriculaFacade().alterarGradeCurricularAtualPorTurma(turmaVO, situacaoMatricula, situacaoMatriculaPeriodo, listaMatriculaPeriodoVO, usuarioVO, realizandoTransferenciaMatrizCurricularPelaTurma);
		}
	}

	// AlteraÃ¯Â¿Â½ao Lenta
	// @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED,
	// rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	// public void
	// executarAlteracaoPeriodoLetivoGradeCurricularMatriculaPeriodo(MatriculaPeriodoVO
	// matriculaPeriodoVO, TurmaVO turmaVO) throws Exception {
	// if (!turmaVO.getPeridoLetivo().getCodigo().equals(0) &&
	// !turmaVO.getGradeCurricularVO().getCodigo().equals(0)) {
	// matriculaPeriodoVO.setPeridoLetivo(turmaVO.getPeridoLetivo());
	// matriculaPeriodoVO.setGradeCurricular(turmaVO.getGradeCurricularVO());
	// getFacadeFactory().getMatriculaPeriodoFacade().alterarPeriodoLetivoGradeCurricular(matriculaPeriodoVO.getCodigo(),
	// matriculaPeriodoVO.getPeridoLetivo().getCodigo(),
	// matriculaPeriodoVO.getGradeCurricular().getCodigo());
	// }
	// }
	public void adicionarObjTurmaAberturaVOs(TurmaVO turmaVO, TurmaAberturaVO obj) throws Exception {
		TurmaAberturaVO.validarDados(obj);
		int index = 0;
		Iterator i = turmaVO.getTurmaAberturaVOs().iterator();
		while (i.hasNext()) {
			TurmaAberturaVO objExistente = (TurmaAberturaVO) i.next();
			if (objExistente.getTurma().getCodigo().equals(obj.getTurma().getCodigo()) && objExistente.getSituacao().equals(obj.getSituacao())) {
				turmaVO.getTurmaAberturaVOs().set(index, obj);
				return;
			}
			index++;
		}
		turmaVO.getTurmaAberturaVOs().add(obj);
	}

	public Integer consultarQuantidadeTurmasEncerrandoPeriodo(Date dataInicio, Date dataFim) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select count(distinct turma.codigo) as qtde from turma ");
		sqlStr.append(" inner join horarioturma on horarioturma.turma = turma.codigo ");
		sqlStr.append(" where horarioturma.codigo in(");
		sqlStr.append(" select case when (horarioturmadia.data >= '");
		sqlStr.append(dataInicio);
		sqlStr.append("' AND horarioturmadia.data <= '");
		sqlStr.append(dataFim);
		sqlStr.append("') then horarioturma.codigo else null end from horarioturmadia ");
		sqlStr.append(" where horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append(" order by horarioturmadia.data desc limit 1 )");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	public Integer consultarQuantidadeTurmasNaoEncerrandoPeriodo(Date dataInicio, Date dataFim) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select count(distinct turma.codigo) as qtde from turma ");
		sqlStr.append(" inner join horarioturma on horarioturma.turma = turma.codigo ");
		sqlStr.append(" where horarioturma.codigo not in(");
		sqlStr.append(" select case when (horarioturmadia.data >= '");
		sqlStr.append(dataInicio);
		sqlStr.append("' AND horarioturmadia.data <= '");
		sqlStr.append(dataFim);
		sqlStr.append("') then horarioturma.codigo else null end from horarioturmadia ");
		sqlStr.append(" where horarioturmadia.horarioturma = horarioturma.codigo ");
		sqlStr.append(" order by horarioturmadia.data desc limit 1 )");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	public Double consultarReceitaPrevistaPeriodo(Date dataInicio, Date dataFim) {

		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT turma.qtdAlunosEstimado, turma.receitamediaaluno FROM turma ");
		sqlStr.append(" INNER JOIN turmaAbertura ON turmaAbertura.turma = turma.codigo ");
		sqlStr.append(" WHERE turmaAbertura.situacao = 'AC' ");
		sqlStr.append(" AND turmaAbertura.data between '");
		sqlStr.append(dataInicio);
		sqlStr.append("'  and '");
		sqlStr.append(dataFim);
		sqlStr.append("'");

//		if (!unidadesPlanoOrcamentarioVOs.isEmpty()) {
//			sqlStr.append(" and turma.unidadeensino in (");
//			int x = 0;
//			for (UnidadesPlanoOrcamentarioVO unidadePlanoOrcamentarioVO : unidadesPlanoOrcamentarioVOs) {
//				if (x > 0) {
//					sqlStr.append(", ");
//				}
//				sqlStr.append(unidadePlanoOrcamentarioVO.getUnidadeEnsino().getCodigo());
//				x++;
//			}
//			sqlStr.append(") ");
//		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		Integer qtdeAlunosEstimado = 0;
		Double receitaMediaAluno = 0.0;

		while (tabelaResultado.next()) {
			qtdeAlunosEstimado = qtdeAlunosEstimado + tabelaResultado.getInt("qtdAlunosEstimado");
			receitaMediaAluno = receitaMediaAluno + tabelaResultado.getDouble("receitaMediaAluno");
		}
		return qtdeAlunosEstimado * receitaMediaAluno;
	}

	public Double consultarDespesaPrevistaPeriodo(Date dataInicio, Date dataFim) {

		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT turma.qtdAlunosEstimado, turma.customedioaluno FROM turma ");
		sqlStr.append(" INNER JOIN turmaAbertura ON turmaAbertura.turma = turma.codigo ");
		sqlStr.append(" WHERE turmaAbertura.situacao = 'AC' ");
		sqlStr.append(" AND turmaAbertura.data between '");
		sqlStr.append(dataInicio);
		sqlStr.append("'  and '");
		sqlStr.append(dataFim);
		sqlStr.append("'");

//		if (!unidadesPlanoOrcamentarioVOs.isEmpty()) {
//			sqlStr.append(" and turma.unidadeensino in (");
//			int x = 0;
//			for (UnidadesPlanoOrcamentarioVO unidadePlanoOrcamentarioVO : unidadesPlanoOrcamentarioVOs) {
//				if (x > 0) {
//					sqlStr.append(", ");
//				}
//				sqlStr.append(unidadePlanoOrcamentarioVO.getUnidadeEnsino().getCodigo());
//				x++;
//			}
//			sqlStr.append(") ");
//		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		Integer qtdeAlunosEstimado = 0;
		Double despesaMediaAluno = 0.0;
		while (tabelaResultado.next()) {
			qtdeAlunosEstimado = qtdeAlunosEstimado + tabelaResultado.getInt("qtdAlunosEstimado");
			despesaMediaAluno = despesaMediaAluno + tabelaResultado.getDouble("customedioaluno");
		}
		return qtdeAlunosEstimado * despesaMediaAluno;
	}

	public Integer consultarQuantidadeNovasTurmasPrevistaPeriodo(Date dataInicio, Date dataFim) {

		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT count(turma.codigo) as qtde FROM turma  ");
		sqlStr.append(" INNER JOIN turmaAbertura ON turmaAbertura.turma = turma.codigo ");
		sqlStr.append(" WHERE turmaAbertura.situacao = 'AC' ");
		sqlStr.append(" AND turmaAbertura.data between '");
		sqlStr.append(dataInicio);
		sqlStr.append("'  and '");
		sqlStr.append(dataFim);
		sqlStr.append("'");

//		if (!unidadesPlanoOrcamentarioVOs.isEmpty()) {
//			sqlStr.append(" and turma.unidadeensino in (");
//			int x = 0;
//			for (UnidadesPlanoOrcamentarioVO unidadePlanoOrcamentarioVO : unidadesPlanoOrcamentarioVOs) {
//				if (x > 0) {
//					sqlStr.append(", ");
//				}
//				sqlStr.append(unidadePlanoOrcamentarioVO.getUnidadeEnsino().getCodigo());
//				x++;
//			}
//			sqlStr.append(") ");
//		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		while (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}

	public boolean verificarTurmaAgrupada(Integer turma) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT turmaAgrupada FROM turma WHERE codigo = ").append(turma);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("turmaAgrupada");
		}
		return false;
	}

	@Override
	public List<TurmaVO> consultarPorNomeTurmaCursoEUnidadeEnsino(String valorConsulta, Integer codigoCurso, Integer unidadeEnsinoCodigo, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), b, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT distinct turma.* from turma ");
		sqlStr.append("inner join curso on (curso.codigo = turma.curso or curso.codigo = (select t.curso from turma t where t.codigo = turma.turmaprincipal)) ");
		sqlStr.append("inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append("where sem_acentos(turma.identificadorTurma) ilike(sem_acentos('").append(valorConsulta).append("%'))");
		if (codigoCurso != 0) {
			sqlStr.append(" and curso.codigo = ").append(codigoCurso);
		}
		if (unidadeEnsinoCodigo != 0) {
			sqlStr.append(" and unidadeensinocurso.unidadeensino = ").append(unidadeEnsinoCodigo.intValue());
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<TurmaVO> consultarPorCodigoTurmaCursoEUnidadeEnsino(Integer valorConsulta, Integer codigoCurso, Integer unidadeEnsinoCodigo, boolean b, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), b, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT distinct turma.* from turma ");
		sqlStr.append("inner join curso on (curso.codigo = turma.curso or curso.codigo = (select t.curso from turma t where t.codigo = turma.turmaprincipal)) ");
		sqlStr.append("inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo ");
		sqlStr.append("where turma.codigo = ").append(valorConsulta);
		if (codigoCurso != 0) {
			sqlStr.append(" and curso.codigo = ").append(codigoCurso);
		}
		if (unidadeEnsinoCodigo != 0) {
			sqlStr.append(" and unidadeensinocurso.unidadeensino = ").append(unidadeEnsinoCodigo.intValue());
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public List<TurmaVO> consultaRapidaPorIdentificadorTurma(String identificador, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper( sem_acentos(turma.identificadorturma) )like(sem_acentos('");
		sql.append(identificador.toUpperCase());
		sql.append("%'");
		sql.append(" ))");
		if (!unidadeEnsinoVOs.isEmpty()) {
			sql.append(" and unidadeensino.codigo in (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (x > 0) {
					sql.append(", ");
				}
				sql.append(unidadeEnsinoVO.getCodigo());
				x++;
			}
			sql.append(" ) ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public List<TurmaVO> consultarRapidaPorNomeCurso(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE lower (sem_acentos(Curso.nome)) like(sem_acentos('");
		sql.append(valorConsulta.toLowerCase());
		sql.append("%'");
		sql.append(" ))");
		if (!unidadeEnsinoVOs.isEmpty()) {
			sql.append(" and unidadeensino.codigo in (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (x > 0) {
					sql.append(", ");
				}
				sql.append(unidadeEnsinoVO.getCodigo());
				x++;
			}
			sql.append(" ) ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public List<TurmaVO> consultarPorNomeCursoUnidadeEnsinoCursoTurno(String valorConsulta, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append("WHERE sem_acentos(lower (curso.nome)) like(sem_acentos('").append(valorConsulta.toLowerCase()).append("%'))  ");
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sql.append(" AND turma.unidadeEnsino = " + unidadeEnsino);
		}
		if (curso != null && curso.intValue() != 0) {
			sql.append(" AND turma.curso = " + curso);
		}
		if (turno != null && turno.intValue() != 0) {
			sql.append(" AND turma.turno = " + turno);
		}
		sql.append(" ORDER BY  turma.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public TurmaVO consultaRapidaPorIdentificadorTurmaUnicoCursoTurno(String identificador, Integer curso, Integer turno, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper(sem_acentos(turma.identificadorturma)) = upper(sem_acentos(?)) ");
		if (curso != 0) {
			sql.append(" AND turma.curso = ").append(curso);
		}
		if (turno != 0) {
			sql.append(" AND turma.turno = ").append(turno);
		}
		if (unidade.intValue() != 0) {
			sql.append(" AND (turma.unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), identificador);
		TurmaVO turma = null;
		if (tabelaResultado.next()) {
			turma = new TurmaVO();
			montarDadosBasico(turma, tabelaResultado);
			return turma;
		}
		throw new Exception("Dados não encontrado(TURMA)");
	}

	@Override
	public List<TurmaVO> consultarPorSubTurma(TurmaVO turma, Integer disciplina, boolean subturma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario, TipoSubTurmaEnum tipoSubTurma, Boolean trazerApenasTurmaComProgramacaoAula, String ano, String semestre, String situacao) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT distinct Turma.* FROM Turma ");
		sqlStr.append(" INNER JOIN TurmaDisciplina on TurmaDisciplina.turma = Turma.codigo ");
		sqlStr.append(" left JOIN GradeDisciplina on GradeDisciplina.codigo = TurmaDisciplina.GradeDisciplina ");
		sqlStr.append(" left JOIN GradeCurricularGrupoOptativaDisciplina on GradeCurricularGrupoOptativaDisciplina.codigo = TurmaDisciplina.GradeCurricularGrupoOptativaDisciplina ");
		sqlStr.append(" left join TurmaDisciplinaComposta on TurmaDisciplinaComposta.turmaDisciplina = TurmaDisciplina.codigo ");
		sqlStr.append(" left join GradeDisciplinaComposta on (case when TurmaDisciplinaComposta.codigo is not null then GradeDisciplinaComposta.codigo = TurmaDisciplinaComposta.gradeDisciplinaComposta else GradeDisciplinaComposta.gradeDisciplina = GradeDisciplina.codigo end) ");
		sqlStr.append(" inner join disciplina on ((turma.turmaagrupada and disciplina.codigo = turmadisciplina.disciplina) ");
		sqlStr.append(" or (turma.turmaagrupada =  false and disciplina.codigo = GradeDisciplinaComposta.disciplina ) ");
		sqlStr.append(" or (turma.turmaagrupada =  false and disciplina.codigo = GradeDisciplina.disciplina ) ");
		sqlStr.append(" or (turma.turmaagrupada =  false and disciplina.codigo = GradeCurricularGrupoOptativaDisciplina.disciplina )) ");
		sqlStr.append(" WHERE Turma.subturma = ").append(subturma);
		if (!turma.getCodigo().equals(0)) {
			sqlStr.append(" AND (Turma.turmaPrincipal = ").append(turma.getCodigo()).append(" or turma.turmaprincipal in ( ");
			sqlStr.append(" select turmaagrupada.turmaorigem from turmaagrupada ");
			sqlStr.append(" inner join turma as t on t.codigo = turmaagrupada.turmaorigem");
			sqlStr.append(" where turmaagrupada.turma = ").append(turma.getCodigo()).append(" and t.subturma = false");
			if (Uteis.isAtributoPreenchido(situacao)) {
				sqlStr.append(" AND t.situacao = '").append(situacao).append("'");
			}
			sqlStr.append(" ))");
		}
		sqlStr.append(" AND (disciplina.codigo = ").append(disciplina);
		sqlStr.append(" or (turma.turmaagrupada and disciplina.codigo in (select disciplina from disciplinaequivalente where equivalente = ").append(disciplina).append(" ))");
		sqlStr.append(" or (turma.turmaagrupada and disciplina.codigo in (select equivalente from disciplinaequivalente where disciplina = ").append(disciplina).append(" ))");
		sqlStr.append(" ) ");
		if (Uteis.isAtributoPreenchido(situacao)) {
			sqlStr.append(" AND Turma.situacao = '").append(situacao).append("'");
		}
		sqlStr.append(" AND tipoSubTurma = '").append(tipoSubTurma.name()).append("'");
		if (trazerApenasTurmaComProgramacaoAula) {
			sqlStr.append(" and exists ( select ofertadisciplina.codigo from ofertadisciplina where ofertadisciplina.disciplina = ").append(disciplina).append(" and ofertadisciplina.ano = '").append(ano).append("' and ofertadisciplina.semestre = '").append(semestre).append("' ");
//			sqlStr.append(" exists (");
//			sqlStr.append(" 	select ht.codigo from horarioturma  ht ");
//			sqlStr.append(" 	inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
//			sqlStr.append(" 	inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
//			sqlStr.append(" 	where  ht.turma = turma.codigo");
//			sqlStr.append(" 	and ((turma.semestral and ht.anovigente = '").append(ano).append("'");
//			sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
//			sqlStr.append(" 	or (turma.anual and ht.anovigente = '").append(ano).append("') ");
//			sqlStr.append(" 	or (turma.semestral = false and turma.anual = false))");
//			sqlStr.append(" 	and htdi.disciplina = disciplina.codigo ");
//			sqlStr.append(" )");
//			sqlStr.append(" or");
//			sqlStr.append(" exists (");
//			sqlStr.append(" 	select t.codigo from horarioturma  ht");
//			sqlStr.append(" 	inner join turma as t on t.codigo = ht.turma");
//			sqlStr.append(" 	inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo	");
//			sqlStr.append(" 	inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
//			sqlStr.append(" 	inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
//			sqlStr.append(" 	where  turmaagrupada.turma = turma.codigo");
//			if (Uteis.isAtributoPreenchido(situacao)) {
//				sqlStr.append(" AND t.situacao = '").append(situacao).append("'");
//			}
//			sqlStr.append(" 	and ((turma.semestral and ht.anovigente = '").append(ano).append("'");
//			sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
//			sqlStr.append(" 	or (turma.anual and ht.anovigente = '").append(ano).append("') ");
//			sqlStr.append(" 	or (turma.semestral = false and turma.anual = false))");
//			sqlStr.append(" 	and (htdi.disciplina = disciplina.codigo ").append(")");
//			sqlStr.append(" 	limit 1)");
//			sqlStr.append(" or exists (");
//			sqlStr.append(" 	select t.codigo from horarioturma  ht");
//			sqlStr.append(" 	inner join turma as t on t.codigo = ht.turma");
//			sqlStr.append(" 	inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo	");
//			sqlStr.append(" 	inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
//			sqlStr.append(" 	inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
//			sqlStr.append(" 	inner join disciplinaequivalente de on de.equivalente = htdi.disciplina ");
//			sqlStr.append(" 	where  turmaagrupada.turma = turma.codigo");
//			if (Uteis.isAtributoPreenchido(situacao)) {
//				sqlStr.append(" AND t.situacao = '").append(situacao).append("'");
//			}
//			sqlStr.append(" 	and ((turma.semestral and ht.anovigente = '").append(ano).append("'");
//			sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
//			sqlStr.append(" 	or (turma.anual and ht.anovigente = '").append(ano).append("') ");
//			sqlStr.append(" 	or (turma.semestral = false and turma.anual = false))");
//			sqlStr.append(" 	and (de.disciplina = disciplina.codigo ").append(")");
//			sqlStr.append(" 	limit 1)");
			sqlStr.append(" )");

		}

		sqlStr.append(" ORDER BY Turma.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	@Override
	public String consultarPeriodicidadePorCodigoTurma(Integer turma) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select semestral, anual from turma where codigo = ").append(turma);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			if (rs.getBoolean("anual")) {
				return "AN";
			}
			if (rs.getBoolean("semestral")) {
				return "SE";
			}
			return "IN";
		}
		return "";
	}

	@Override
	public Boolean consultarLiberarRegistroAulaEntrePeriodoConsiderandoTodosCursosTurmaAgrupada(TurmaVO turmaVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select case when turma.turmaagrupada then case when (select min(case when c.liberarregistroaulaentreperiodo is null or c.liberarregistroaulaentreperiodo = false then 0 else 1 end ) ");
		sqlStr.append(" from turmaagrupada ");
		sqlStr.append(" inner join turma t2 on t2.codigo = turmaagrupada.turma ");
		sqlStr.append(" inner join curso c on c.codigo = t2.curso ");
		sqlStr.append(" where turmaagrupada.turmaorigem = turma.codigo) = 1 then true else false end ");
		sqlStr.append(" else case when curso.liberarregistroaulaentreperiodo is null or curso.liberarregistroaulaentreperiodo = false then false else true end ");
		sqlStr.append(" end as liberarregistroaulaentreperiodo ");
		sqlStr.append(" from turma ");
		sqlStr.append(" left join curso on curso.codigo = turma.curso ");
		sqlStr.append(" where turma.codigo = ").append(turmaVO.getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("liberarregistroaulaentreperiodo");
		}

		return false;
	}

	@Override
	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaSubturma(String identificador, Integer turmaOrigem, boolean subturma, Integer unidade, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper( sem_acentos(turma.identificadorturma) )like('");
		sql.append(identificador.toUpperCase());
		sql.append("%'");
		sql.append(" )");
		if (unidade.intValue() != 0) {
			sql.append(" AND (turma.unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX') OR turma.turmaAgrupada)");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND (curso.nivelEducacional != 'PO' OR turma.turmaAgrupada)");
		}
		if (turmaOrigem != 0) {
			sql.append(" AND turma.codigo <> ").append(turmaOrigem.intValue()).append(" ");
		}
		sql.append(" and turma.subturma = ").append(subturma);
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public TurmaVO consultarTurmaPorIdentificadorTurmaSubturma(String identificador, Integer turmaOrigem, boolean subturma, Integer unidade, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper( sem_acentos(turma.identificadorturma) )like(sem_acentos('").append(identificador.toUpperCase()).append("%'").append(" ))");
		if (unidade.intValue() != 0) {
			sql.append(" AND (turma.unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX') OR turma.turmaAgrupada)");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND (curso.nivelEducacional != 'PO' OR turma.turmaAgrupada)");
		}
		if (turmaOrigem != 0) {
			sql.append(" AND turma.codigo <> ").append(turmaOrigem.intValue()).append(" ");
		}
		sql.append(" and turma.subturma = ").append(subturma);
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		TurmaVO obj = new TurmaVO();
		if (tabelaResultado.next()) {
			montarDadosBasico(obj, tabelaResultado);
			return obj;
		}
		return obj;
	}

	@Override
	public List<TurmaVO> consultaRapidaPorUnidadeEnsinoSubturma(String valorConsulta, Integer turmaOrigem, boolean subturma, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE lower(sem_acentos(unidadeensino.nome)) ilike (sem_acentos('");
		sql.append(valorConsulta);
		sql.append("%'))");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX')  OR turma.turmaAgrupada)");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND curso.nivelEducacional != 'PO'");
		}
		if (turmaOrigem != 0) {
			sql.append(" AND turma.codigo <> ").append(turmaOrigem.intValue());
		}
		sql.append(" and turma.subturma = ").append(subturma);
		sql.append(" ORDER BY unidadeensino.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public List<TurmaVO> consultaRapidaPorTurnoSubturma(String valorConsulta, Integer turmaOrigem, boolean subturma, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE lower(sem_acentos(turno.nome)) ilike (sem_acentos('");
		sql.append(valorConsulta.toLowerCase());
		sql.append("%'))");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX')  OR turma.turmaAgrupada)");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND curso.nivelEducacional != 'PO'");
		}
		if (turmaOrigem.intValue() != 0) {
			sql.append(" and turma.codigo <> ");
			sql.append(turmaOrigem.intValue());
		}
		sql.append(" and turma.subturma = ").append(subturma);
		sql.append(" ORDER BY turno.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public List<TurmaVO> consultaRapidaNomeCursoSubturma(String valorConsulta, Integer turmaOrigem, boolean subturma, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE lower(sem_acentos(curso.nome)) ilike (sem_acentos('");
		sql.append(valorConsulta.toLowerCase());
		sql.append("%'))");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX')  OR turma.turmaAgrupada)");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND curso.nivelEducacional != 'PO'");
		}
		if (turmaOrigem.intValue() != 0) {
			sql.append(" and turma.codigo <> ");
			sql.append(turmaOrigem.intValue());
		}
		sql.append(" and turma.subturma = ").append(subturma);
		sql.append(" ORDER BY curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public List<TurmaVO> consultarTurmaPorDisciplinaMatriculaPeriodoTransferenciaTurma(Integer codDisciplina, Integer matriculaPeriodo, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select distinct(turma.*) from turma ");
		sqlStr.append("inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sqlStr.append("where turmadisciplina.disciplina = ").append(codDisciplina);
		sqlStr.append(" and turma.codigo not in (");
		sqlStr.append(" select mptd.turma from matriculaperiodoturmadisciplina mptd");
		sqlStr.append(" where mptd.matriculaperiodo = ").append(matriculaPeriodo);
		sqlStr.append(" and mptd.disciplina = ").append(codDisciplina);
		sqlStr.append(" and mptd.ano = '").append(ano).append("'");
		sqlStr.append(" and mptd.semestre = '").append(semestre).append("'");
		sqlStr.append(") ");
		sqlStr.append("and turma.situacao = 'AB' ");
		sqlStr.append("order by identificadorturma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<TurmaVO> consultaRapidaPorIdentificadorTurma(String identificador, Integer turmaOrigem, Integer unidade, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean trazerSubturma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper(sem_acentos(turma.identificadorturma) )like(sem_acentos('");
		sql.append(identificador.toUpperCase());
		sql.append("%'");
		sql.append(" ))");
		if (unidade.intValue() != 0) {
			sql.append(" AND (turma.unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX') OR turma.turmaAgrupada)");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND (curso.nivelEducacional != 'PO' OR turma.turmaAgrupada)");
		}
		if (turmaOrigem != 0) {
			sql.append(" AND turma.codigo <> ").append(turmaOrigem.intValue()).append(" ");
		}
		if (!trazerSubturma) {
			sql.append(" and turma.subturma = false");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public String verificaQtdAlunoAtivoPreReposicao(Integer turma, Integer gradeDisciplina, Integer codDisciplina) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct (select count(mptd2.codigo) from matriculaperiodoturmadisciplina mptd2 inner join matriculaperiodo on matriculaperiodo.codigo = mptd2.matriculaperiodo ");
		sql.append(" where mptd2.turma = mptd.turma and mptd2.disciplina = mptd.disciplina and mptd2.reposicao = true and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO')) as reposicao, ");
		sql.append(" (select count(mptd2.codigo) from matriculaperiodoturmadisciplina mptd2 inner join matriculaperiodo on matriculaperiodo.codigo = mptd2.matriculaperiodo ");
		sql.append(" where mptd2.turma = mptd.turma and mptd2.disciplina = mptd.disciplina and mptd2.reposicao <> true and matriculaperiodo.situacaomatriculaperiodo in ('AT', 'CO')) as ativo, ");
		sql.append(" (select count(mptd2.codigo) from matriculaperiodoturmadisciplina mptd2 inner join matriculaperiodo on matriculaperiodo.codigo = mptd2.matriculaperiodo ");
		sql.append(" where mptd2.turma = mptd.turma and mptd2.disciplina = mptd.disciplina and matriculaperiodo.situacaomatriculaperiodo in ('PR')) as preMatricula ");
		sql.append(" from matriculaperiodoturmadisciplina mptd where 1=1 ");
		if (turma != 0) {
			sql.append(" and turma = ").append(turma.intValue()).append(" ");
		}
		if (gradeDisciplina != 0 && codDisciplina == 0) {
			sql.append(" and disciplina = (select disciplina from gradedisciplina where codigo = ").append(gradeDisciplina.intValue()).append(") ");
		} else {
			sql.append(" and disciplina = ").append(codDisciplina.intValue()).append(" ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		int ativo = 0;
		int preMatricula = 0;
		int reposicao = 0;
		while (tabelaResultado.next()) {
			ativo = tabelaResultado.getInt("ativo");
			preMatricula = tabelaResultado.getInt("prematricula");
			reposicao = tabelaResultado.getInt("reposicao");
		}
		String msg = "A turma selecionada para reposição possui " + ativo + " aluno(s) ativo(s), " + preMatricula + " pré-matrículas e outras " + reposicao + " reposições agendadas. Deseja continuar com o procedimento ?";
		return msg;
	}

	public TurmaVO consultaRapidaUnicidadeTurmaPorIdentificador(String identificador) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT Turma.codigo, Turma.identificadorturma ");
		sql.append("from turma ");
		sql.append(" WHERE upper(sem_acentos(identificadorturma)) = upper(sem_acentos('").append(identificador.toUpperCase()).append("')) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		TurmaVO obj = new TurmaVO();
		if (!tabelaResultado.next()) {
			return obj;
		}
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setIdentificadorTurma(tabelaResultado.getString("identificadorturma"));
		return obj;
	}

	/**
	 * @author Victor Hugo 17/12/2014
	 * 
	 */
	@Override
	public List<TurmaVO> consultarTurmasEADProfessor(Integer codigoPessoa, Integer unidadeEnsino, Integer curso,  PeriodicidadeEnum periodicidadeEnum) throws Exception {
		StringBuilder sqlStr =new StringBuilder(""); 
		getSqlPadraoConsultaTurmaEad(codigoPessoa, unidadeEnsino, curso, periodicidadeEnum, sqlStr);
		sqlStr.append(" order by identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
			obj.setTurmaAgrupada(tabelaResultado.getBoolean("turmaagrupada"));
			obj.getTurno().setNome(tabelaResultado.getString("turno"));
			obj.getCurso().setNome(tabelaResultado.getString("curso"));
			obj.getCurso().setNivelEducacional(tabelaResultado.getString("nivelEducacional"));
			obj.getCurso().setLiberarRegistroAulaEntrePeriodo(tabelaResultado.getBoolean("liberarRegistroAulaEntrePeriodo"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	@Override
	public List<TurmaVO> consultaRapidaPorNrPeriodoLetivoUnidadeEnsinoCursoTurnoDisciplina(Integer nrPeridoLetivo, Integer unidadeEnsino, Integer curso, Integer turno, Integer gradeCurricular, Integer disciplina, boolean novaMatricula, boolean renovandoMatricula, boolean editandoMatricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append("INNER JOIN turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sql.append("WHERE (turma.unidadeEnsino = ").append(unidadeEnsino).append(") ");
		if (Uteis.isAtributoPreenchido(nrPeridoLetivo)) {
			sql.append("and (Periodoletivo.periodoLetivo = ").append(nrPeridoLetivo).append(") ");
		}
		sql.append("and (Curso.codigo = ").append(curso).append(") ");
		sql.append("and (Turno.codigo = ").append(turno).append(") ");
		sql.append("and (Turma.situacao = 'AB') ");
		sql.append("and turmadisciplina.disciplina = ").append(disciplina);
		if (novaMatricula || renovandoMatricula || editandoMatricula) {
			sql.append(" and (Turma.gradeCurricular = ").append(gradeCurricular).append(")");
			sql.append(" and (Turma.planoFinanceiroCurso is not null  ");
			sql.append(" or  (select planoFinanceiroCurso from unidadeensinocurso where unidadeensinocurso.curso = curso.codigo and unidadeensinocurso.turno = Turno.codigo and unidadeensinocurso.unidadeensino = unidadeensino.codigo limit 1) is not null  )");
		}
		sql.append(" and Turma.subturma = false ");
		sql.append(" ORDER BY turma.identificadorTurma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurnoConsiderandoTurmaAgrupada(String valorConsulta, Integer unidadeEnsino, Integer curso, Integer turno, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT Turma.codigo, Turma.semestral, Turma.anual, Turma.situacao, Turma.identificadorturma, Turma.turmaagrupada, Turma.sala, Turma.gradeCurricular, Turma.chancela, Turma.turmaPrincipal, ");
		sqlStr.append("Turma.nrvagas, Turma.nrmaximomatricula, Turma.nrminimomatricula, Turma.tipochancela, Turma.porcentagemchancela, Turma.valorfixochancela, Turma.observacao, Turma.valorporaluno, Turma.qtdAlunosEstimado, Turma.custoMedioAluno, Turma.receitaMediaAluno, chancela.instituicaoChanceladora, turma.utilizarDadosMatrizBoleto, turma.subturma, ");
		sqlStr.append("Unidadeensino.codigo as \"Unidadeensino.codigo\" , Unidadeensino.nome as \"Unidadeensino.nome\", ");
		sqlStr.append("Curso.codigo as \"Curso.codigo\", Curso.nome as \"Curso.nome\", Curso.periodicidade as \"Curso.periodicidade\", Curso.nivelEducacional as \"Curso.nivelEducacional\", Curso.liberarregistroaulaentreperiodo AS \"Curso.liberarregistroaulaentreperiodo\", Curso.configuracaoacademico AS \"Curso.configuracaoacademico\",  ");
		sqlStr.append("Turno.codigo as \"Turno.codigo\", Turno.nome as \"Turno.nome\", ");
		sqlStr.append("Periodoletivo.descricao as \"Periodoletivo.descricao\",Periodoletivo.codigo as \"Periodoletivo.codigo\",Periodoletivo.periodoLetivo as \"Periodoletivo.periodoLetivo\", ");
		sqlStr.append("gradecurricular.nome as \"gradecurricular.nome\" ");
		sqlStr.append("from turma ");
		sqlStr.append("inner join curso on (curso.codigo = turma.curso or curso.codigo = (select t.curso from turma t where t.codigo = turma.turmaprincipal) or curso.codigo in (select t.curso from turmaagrupada ta inner join turma t on t.codigo = ta.turma where ta.turmaorigem = turma.codigo)) ");
		sqlStr.append("left join unidadeensino on turma.unidadeensino = unidadeensino.codigo ");
		sqlStr.append("left join turno on turma.turno = turno.codigo ");
		sqlStr.append("left join periodoletivo on turma.periodoletivo = periodoletivo.codigo ");
		sqlStr.append("left join chancela on turma.chancela = chancela.codigo ");
		sqlStr.append("left join gradecurricular on turma.gradecurricular = gradecurricular.codigo ");
		sqlStr.append("WHERE lower (turma.identificadorTurma) like('").append(valorConsulta.toLowerCase()).append("%')  ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" AND unidadeEnsino.codigo = ").append(unidadeEnsino);
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" AND curso.codigo = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(turno)) {
			sqlStr.append(" AND turno.codigo = ").append(turno);
		}
		sqlStr.append(" ORDER BY turma.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public void processarDadosPermitinentesTurmaSelecionada(MatriculaVO matriculaVO, MatriculaPeriodoVO matriculaPeriodoVO,  UsuarioVO usuarioVO) throws Exception {
		
//		if (matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo() != 0) {
//			matriculaPeriodoVO.setCondicaoPagamentoPlanoFinanceiroCurso(new CondicaoPagamentoPlanoFinanceiroCursoVO());
//		}
		getFacadeFactory().getTurmaFacade().carregarDados(matriculaPeriodoVO.getTurma(), NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, usuarioVO);
		getFacadeFactory().getMatriculaPeriodoFacade().atualizarSituacaoMatriculaPeriodo(matriculaVO, matriculaPeriodoVO, usuarioVO);
	}

	@Override
	public List<TurmaVO> consultarTurmaComAtividadeDiscursivaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(UsuarioVO usuarioVO, String semestre, String ano, Boolean buscarTurmasAnteriores, Integer unidadeEnsino) throws Exception {
		StringBuilder sql = new StringBuilder("select distinct turma.codigo, turma.identificadorTurma, curso.nome as curso, turno.nome as turno from atividadeDiscursiva ");
		sql.append(" inner join turma on  atividadeDiscursiva.turma = turma.codigo");
		sql.append(" left join curso on  curso.codigo = turma.curso");
		sql.append(" left join turno on  turno.codigo = turma.turno");
		sql.append(" where atividadeDiscursiva.responsavelCadastro = ").append(usuarioVO.getCodigo()).append(" ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" and turma.unidadeensino = ").append(unidadeEnsino);
		}
		sql.append(" and turma.situacao = 'AB' ");
		if (!buscarTurmasAnteriores) {
			sql.append(" and ((turma.semestral and  atividadeDiscursiva.ano = '").append(ano).append("' and  atividadeDiscursiva.semestre = '").append(semestre).append("') or ");
			sql.append("  (turma.anual and  atividadeDiscursiva.ano = '").append(ano).append("') or (turma.semestral = false and turma.anual = false))");
		} else {
			// sql.append(" and ((turma.semestral and atividadeDiscursiva.ano =
			// '").append(ano).append("' and atividadeDiscursiva.semestre =
			// '").append(semestre).append("') or ");
			// sql.append(" (turma.anual and atividadeDiscursiva.ano =
			// '").append(ano).append("') or (turma.integral))");
		}
		sql.append(" order by identificadorTurma ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<TurmaVO> turmaVOs = new ArrayList<TurmaVO>(0);
		TurmaVO turma = null;
		while (rs.next()) {
			turma = new TurmaVO();
			turma.setIdentificadorTurma(rs.getString("identificadorTurma"));
			turma.setCodigo(rs.getInt("codigo"));
			turma.getCurso().setNome(rs.getString("curso"));
			turma.getTurno().setNome(rs.getString("turno"));
			turmaVOs.add(turma);
		}
		return turmaVOs;
	}

	@Override
	public List<TurmaVO> consultarTurmaProgramacaoAula(String campoConsulta, String valorConsulta, String situacaoTurma, String tipoTurma, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
		if (valorConsulta.length() < 2) {
			throw new Exception(UteisJSF.internacionalizar("msg_ParametroConsulta_vazio"));
		}
		StringBuilder sql = getSQLPadraoConsultaBasica();
		if (campoConsulta.equals("identificadorTurma")) {
			sql.append(" where (sem_acentos(turma.identificadorTurma) ilike (sem_acentos('").append(valorConsulta).append("%')) or (turma.subturma and turmaPrincipal.identificadorTurma ilike ('").append(valorConsulta).append("%')))");
		} else if (campoConsulta.equals("nomeUnidadeEnsino")) {
			sql.append(" where (sem_acentos(unidadeEnsino.nome) ilike sem_acentos(('").append(valorConsulta).append("%')))");
		} else if (campoConsulta.equals("nomeTurno")) {
			sql.append(" where (sem_acentos(turno.nome) ilike sem_acentos(('").append(valorConsulta).append("%')))");
		} else if (campoConsulta.equals("nomeCurso")) {
			sql.append(" where (sem_acentos(curso.nome) ilike sem_acentos(('").append(valorConsulta).append("%')))");
		}
		if (!situacaoTurma.isEmpty()) {
			sql.append(" and turma.situacao = '").append(situacaoTurma).append("'");
		}
		if (!tipoTurma.isEmpty()) {
			if (tipoTurma.equals("agrupada")) {
				sql.append(" and turma.turmaagrupada = true ");
			}
			if (tipoTurma.equals("subturma")) {
				sql.append(" and turma.subturma = true ");
			}
			if (tipoTurma.equals("normal")) {
				sql.append(" and turma.subturma = false and turma.turmaagrupada = false ");
			}
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" and turma.unidadeensino =  ").append(unidadeEnsino);
		}

		sql.append(" order by ordenacao, turma.identificadorTurma, turma.codigo ");
		return montarDadosConsultaBasica(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()));
	}

	@Override
	public StringBuilder getSqlConsultaSubTurmaParaDistribuicaoAutomatica(Integer turmaPrincipal, Integer disciplina, Boolean disciplinaFazParteComposicao, TipoSubTurmaEnum tipoSubturma, String ano, String semestre, Boolean considerarVagaPreMatricula, Boolean considerarSubTurmaAgrupada, Boolean considerarApenasTurmaAulaProgramada, String matriculaDesconsiderar, Boolean considerarVagasReposicao) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" (select t.turmaprincipal, t.disciplina, tiposubturma as tiposubturma,  ");
		sql.append(" ARRAY_TO_STRING(array_agg( t.codigo order by case when (vagaturma - matriculas) > 0 then 0 else 1 end, case when (vagaturma - matriculas) > 0 then vagaturma - matriculas else matriculas end, case when turmaagrupada then 1 else 0 end, codigo ), ',') as turmas ");
		sql.append(" from ( ");
		sql.append(" SELECT distinct turma.codigo, turmaprincipal, turmaagrupada, turmadisciplina.disciplina, tiposubturma,");
		if (considerarVagasReposicao) {
			sql.append(" case when vagaturma.nrVagasMatriculaReposicao is not null then vagaturma.nrVagasMatriculaReposicao else nrVagasInclusaoReposicao end as vagaturma, ");
			sql.append(" case when vagaturma.nrVagasMatriculaReposicao is not null then vagaturma.nrVagasMatriculaReposicao else turma.nrVagasInclusaoReposicao end as nrmaximomatricula, ");
		} else {
			sql.append(" case when vagaturma.nrvagasmatricula is not null then vagaturma.nrvagasmatricula else nrvagas end as vagaturma, ");
			sql.append(" case when vagaturma.nrmaximomatricula is not null then vagaturma.nrmaximomatricula else turma.nrmaximomatricula end as nrmaximomatricula, ");
		}
		sql.append(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSqlPadraoConsultarVagasOcupadas("turma.codigo", disciplina, ano, semestre, considerarVagaPreMatricula, tipoSubturma, matriculaDesconsiderar, considerarVagasReposicao, false)).append(" as matriculas ");
		sql.append(" from turma");
		sql.append(" inner join turmadisciplina on turma.codigo = turmadisciplina.turma");
		if (disciplinaFazParteComposicao != null && disciplinaFazParteComposicao) {
			sql.append(" inner join gradedisciplinacomposta on ");
			sql.append(" (");
			sql.append(" 	(turmadisciplina.gradedisciplina is not null and gradedisciplinacomposta.gradedisciplina = turmadisciplina.gradedisciplina) ");
			sql.append(" 	or");
			sql.append(" 	(turmadisciplina.gradecurriculargrupooptativadisciplina is not null and gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = turmadisciplina.gradecurriculargrupooptativadisciplina) ");
			sql.append(" )");
			sql.append(" inner join disciplina on disciplina.codigo = gradedisciplinacomposta.disciplina");
		} else {
			sql.append(" inner join disciplina on disciplina.codigo = turmadisciplina.disciplina");
		}
		sql.append(" left join (select vagaturma.turma, vagaturmadisciplina.disciplina, vagaturmadisciplina.nrvagasmatricula, vagaturmadisciplina.nrmaximomatricula, vagaturmadisciplina.nrVagasMatriculaReposicao from vagaturma");
		sql.append(" inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo");
		sql.append(" inner join turma t on t.codigo = vagaturma.turma");
		sql.append(" where (vagaturmadisciplina.disciplina = ").append(disciplina);
		sql.append(" or (t.turmaagrupada and vagaturmadisciplina.disciplina in (select disciplina from disciplinaequivalente where equivalente = ").append(disciplina).append(" ))");
		sql.append(" or (t.turmaagrupada and vagaturmadisciplina.disciplina in (select equivalente from disciplinaequivalente where disciplina = ").append(disciplina).append(" ))");
		sql.append(" ) ");

		sql.append(" and ((t.semestral and vagaturma.ano = '").append(ano).append("' and vagaturma.semestre = '").append(semestre).append("') ");
		sql.append(" or (t.anual and vagaturma.ano = '").append(ano).append("' ) ");
		sql.append(" or (t.semestral = false and t.anual = false )) ");
		sql.append(") as vagaturma on  vagaturma.turma = turma.codigo and disciplina.codigo = vagaturma.disciplina");
		sql.append(" where turma.subturma and turma.situacao = 'AB' and turma.tiposubturma = '").append(tipoSubturma.getName()).append("'");
		sql.append(" AND (disciplina.codigo = ").append(disciplina);
		sql.append(" or (turma.turmaagrupada and disciplina.codigo in (select disciplina from disciplinaequivalente where equivalente = ").append(disciplina).append(" ))");
		sql.append(" or (turma.turmaagrupada and disciplina.codigo in (select equivalente from disciplinaequivalente where disciplina = ").append(disciplina).append(" ))");
		sql.append(" ) ");
		// sql.append(" and case when vagaturma.nrvagasmatricula is not null
		// then vagaturma.nrvagasmatricula else nrvagas end > 0 ");
		if (disciplinaFazParteComposicao != null && disciplinaFazParteComposicao) {
			sql.append(" and (");
			sql.append("      not exists (select turmadisciplinacomposta.turmadisciplina from turmadisciplinacomposta where turmadisciplinacomposta.turmadisciplina =  turmadisciplina.codigo ) ");
			sql.append("      or exists (select turmadisciplinacomposta.turmadisciplina from turmadisciplinacomposta ");
			sql.append("          inner join gradedisciplinacomposta on gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta");
			sql.append("          where turmadisciplinacomposta.turmadisciplina =  turmadisciplina.codigo");
			sql.append("          and gradedisciplinacomposta.disciplina = disciplina.codigo");
			sql.append("");
			sql.append("          )");
			sql.append("    )");
		}
		sql.append(" and (");
		sql.append("      (turmaagrupada = false and turmaprincipal = ").append(turmaPrincipal).append(" ) ");
		if (considerarSubTurmaAgrupada != null && considerarSubTurmaAgrupada) {
			sql.append("      or (turmaagrupada and turmaprincipal in (select turmaagrupada.turmaorigem from turmaagrupada   ");
			sql.append("       inner join turmadisciplina td on td.turma = turmaagrupada.turmaorigem");
			sql.append("       where turmaagrupada.turma = ").append(turmaPrincipal).append(" and td.disciplina  = turmadisciplina.disciplina ");
			if (disciplinaFazParteComposicao != null && disciplinaFazParteComposicao) {
				sql.append("       and ( not exists (select turmadisciplinacomposta.turmadisciplina from turmadisciplinacomposta where turmadisciplinacomposta.turmadisciplina =  td.codigo ) ");
				sql.append(" 	   or exists (select turmadisciplinacomposta.turmadisciplina from turmadisciplinacomposta ");
				sql.append("          inner join gradedisciplinacomposta on gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta");
				sql.append("          where turmadisciplinacomposta.turmadisciplina =  td.codigo");
				sql.append("          and gradedisciplinacomposta.disciplina = disciplina.codigo");
				sql.append("          ) ");
				sql.append(" 	   ) ");
			}
			sql.append("       )) ");
		}
		sql.append("    ) ");
		if (considerarApenasTurmaAulaProgramada != null && considerarApenasTurmaAulaProgramada) {
			sql.append(" and exists ( select htdi.codigo from horarioturmadiaitem htdi ");
			sql.append(" inner join horarioturmadia htd on htd.codigo = htdi.horarioturmadia ");
			sql.append(" inner join horarioturma on htd.horarioturma = horarioturma.codigo ");
			sql.append(" where (turma.codigo = horarioturma.turma or horarioturma.turma in (select turmaorigem from turmaagrupada  where turmaagrupada.turma = turma.codigo)) and htdi.disciplina  = disciplina.codigo ");
			sql.append(" and ((turma.semestral and horarioturma.anovigente = '").append(ano).append("' and ").append(" horarioturma.semestrevigente = '").append(semestre).append("') ");
			sql.append(" or (turma.anual and horarioturma.anovigente = '").append(ano).append("' ) ");
			sql.append(" or (turma.anual = false and turma.semestral = false )) ");
			sql.append(" ) ");
		}
		sql.append(" ) as t ");
		sql.append(" group by t.turmaprincipal, t.disciplina, tiposubturma order by t.turmaprincipal, t.disciplina ) ");
		return sql;
	}

	@Override
	public void consultarNumeroVagasDisponivelParaMatriculaPeriodoTurmaDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, DisciplinaVO disciplina, String ano, String semestre, Boolean considerarAlunosPreMatriculados, Boolean considerarVagasReposicao) throws Exception {
		StringBuilder sql = getSqlConsultaNumeroVagaDisponivel(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), disciplina.getCodigo(), ano, semestre, considerarAlunosPreMatriculados, null, matriculaPeriodoTurmaDisciplinaVO.getMatricula(), considerarVagasReposicao, false);
		if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica())) {
			sql.append(" union ");
			sql.append(getSqlConsultaNumeroVagaDisponivel(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getCodigo(), disciplina.getCodigo(), ano, semestre, considerarAlunosPreMatriculados, TipoSubTurmaEnum.PRATICA, matriculaPeriodoTurmaDisciplinaVO.getMatricula(), considerarVagasReposicao, matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getTurmaAgrupada()));
		}
		if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica())) {
			sql.append(" union ");
			sql.append(getSqlConsultaNumeroVagaDisponivel(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getCodigo(), disciplina.getCodigo(), ano, semestre, considerarAlunosPreMatriculados, TipoSubTurmaEnum.TEORICA, matriculaPeriodoTurmaDisciplinaVO.getMatricula(), considerarVagasReposicao, matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getTurmaAgrupada()));
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (rs.next()) {
			Integer nrVagas = rs.getInt("vagaturma") - rs.getInt("matriculas");
			if (nrVagas < 0) {
				nrVagas = 0;
			}
			if (rs.getBoolean("subturma") && rs.getString("tiposubturma").equals(TipoSubTurmaEnum.PRATICA.getName())) {
				matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().setNrVagas(rs.getInt("vagaturma"));
				matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().setNrMaximoMatricula(rs.getInt("nrmaximomatricula"));
				matriculaPeriodoTurmaDisciplinaVO.setNrAlunosMatriculadosTurmaPratica(rs.getInt("matriculas"));
				matriculaPeriodoTurmaDisciplinaVO.setNrVagasDisponiveisTurmaPratica(nrVagas);
			} else if (rs.getBoolean("subturma") && rs.getString("tiposubturma").equals(TipoSubTurmaEnum.TEORICA.getName())) {
				matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().setNrVagas(rs.getInt("vagaturma"));
				matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().setNrMaximoMatricula(rs.getInt("nrmaximomatricula"));
				matriculaPeriodoTurmaDisciplinaVO.setNrAlunosMatriculadosTurmaTeorica(rs.getInt("matriculas"));
				matriculaPeriodoTurmaDisciplinaVO.setNrVagasDisponiveisTurmaTeorica(nrVagas);
			} else {
				matriculaPeriodoTurmaDisciplinaVO.getTurma().setNrVagas(rs.getInt("vagaturma"));
				matriculaPeriodoTurmaDisciplinaVO.getTurma().setNrMaximoMatricula(rs.getInt("nrmaximomatricula"));
				matriculaPeriodoTurmaDisciplinaVO.setNrAlunosMatriculados(rs.getInt("matriculas"));
				matriculaPeriodoTurmaDisciplinaVO.setNrVagasDisponiveis(nrVagas);
			}
		}
	}

	private StringBuilder getSqlConsultaNumeroVagaDisponivel(Integer turma, Integer disciplina, String ano, String semestre, Boolean considerarPreMatricula, TipoSubTurmaEnum tipoSubturma, String matriculaDesconsiderar, Boolean considerarVagasReposicao, Boolean turmaAgrupada) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" (SELECT turma.codigo, turmaprincipal, turmaagrupada, subturma, tiposubturma,");
		if (considerarVagasReposicao) {
			sql.append(" case when vagaturma.nrVagasMatriculaReposicao is not null then vagaturma.nrVagasMatriculaReposicao else nrVagasInclusaoReposicao end as vagaturma, ");
			sql.append(" case when vagaturma.nrVagasMatriculaReposicao is not null then vagaturma.nrVagasMatriculaReposicao else turma.nrVagasInclusaoReposicao end as nrmaximomatricula, ");
		} else {
			sql.append(" case when vagaturma.nrvagasmatricula is not null then vagaturma.nrvagasmatricula else nrvagas end as vagaturma, ");
			sql.append(" case when vagaturma.nrmaximomatricula is not null then vagaturma.nrmaximomatricula else turma.nrmaximomatricula end as nrmaximomatricula, ");
		}
		sql.append(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().getSqlPadraoConsultarVagasOcupadas("turma.codigo", disciplina, ano, semestre, considerarPreMatricula, tipoSubturma, matriculaDesconsiderar, considerarVagasReposicao, turmaAgrupada)).append(" as matriculas");
		sql.append(" from turma");
		sql.append(" left join (select vagaturma.codigo, vagaturma.turma,  vagaturmadisciplina.disciplina, vagaturmadisciplina.nrvagasmatricula, vagaturmadisciplina.nrmaximomatricula, vagaturmadisciplina.nrVagasMatriculaReposicao from vagaturma");
		sql.append(" inner join vagaturmadisciplina on vagaturmadisciplina.vagaturma = vagaturma.codigo");
		sql.append(" inner join turma t on t.codigo = vagaturma.turma");
		sql.append(" and ((t.semestral and vagaturma.ano = '").append(ano).append("' and vagaturma.semestre = '").append(semestre).append("') ");
		sql.append(" or (t.anual and vagaturma.ano = '").append(ano).append("' ) ");
		sql.append(" or (t.semestral = false and t.anual = false )) ");
		sql.append(" and vagaturma.turma = ").append(turma).append(" and (vagaturmadisciplina.disciplina =  ").append(disciplina);
		// este or verifica se não estiva vaga para a disciplina mae
		sql.append(" or (");
		sql.append(" vagaturmadisciplina.disciplina = (  ");
		sql.append(" select gradedisciplina.disciplina from turmadisciplina ");
		sql.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina  ");
		sql.append(" inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina  ");
		sql.append(" where gradedisciplinacomposta.disciplina =  ").append(disciplina).append(" and  turmadisciplina.turma = ").append(turma);
		sql.append(" union ");
		sql.append(" select gradecurriculargrupooptativadisciplina.disciplina from turmadisciplina ");
		sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = turmadisciplina.gradecurriculargrupooptativadisciplina  ");
		sql.append(" inner join gradedisciplinacomposta on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina  ");
		sql.append(" where gradedisciplinacomposta.disciplina =  ").append(disciplina).append(" and  turmadisciplina.turma = ").append(turma);
		sql.append(" ) ");
		sql.append(" and not exists (select vd.codigo from  vagaturmadisciplina vd where vd.vagaturma = vagaturma.codigo and vd.disciplina =  ").append(disciplina).append(" ) ");
		sql.append(" ))");
		sql.append(") as vagaturma on  vagaturma.turma = turma.codigo and (vagaturma.disciplina= ").append(disciplina);
		sql.append(" or (");
		sql.append(" vagaturma.disciplina = (  ");
		sql.append(" select gradedisciplina.disciplina from turmadisciplina ");
		sql.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina  ");
		sql.append(" inner join gradedisciplinacomposta on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina  ");
		sql.append(" where gradedisciplinacomposta.disciplina =  ").append(disciplina).append(" and  turmadisciplina.turma = ").append(turma);
		sql.append(" union ");
		sql.append(" select gradecurriculargrupooptativadisciplina.disciplina from turmadisciplina ");
		sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = turmadisciplina.gradecurriculargrupooptativadisciplina  ");
		sql.append(" inner join gradedisciplinacomposta on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina  ");
		sql.append(" where gradedisciplinacomposta.disciplina =  ").append(disciplina).append(" and  turmadisciplina.turma = ").append(turma);
		sql.append(" ) ");
		sql.append(" and not exists (select vd.codigo from  vagaturmadisciplina vd where vd.vagaturma = vagaturma.codigo and vd.disciplina =  ").append(disciplina).append(" ) ");
		sql.append(" ))");

		sql.append(" where turma.codigo = ").append(turma).append(" limit 1) ");
		// sql.append(" and case when vagaturma.nrvagasmatricula is not null
		// then vagaturma.nrvagasmatricula else nrvagas end > 0 ");
		// System.out.println(sql.toString());
		return sql;
	}

	@Override
	public List<TurmaVO> consultarSubturmasRealizarTransferencia(Integer turmaPrincipal, Integer disciplina, Boolean disciplinaFazParteComposicao, TipoSubTurmaEnum tipoSubturma, String ano, String semestre, Boolean considerarVagaReposicao, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = getSqlConsultaSubTurmaParaDistribuicaoAutomatica(turmaPrincipal, disciplina, disciplinaFazParteComposicao, tipoSubturma, ano, semestre, true, true, false, "", considerarVagaReposicao);
		SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<TurmaVO> turmaVOs = new ArrayList<TurmaVO>(0);
		while (rowSet.next()) {
			String[] turmas = rowSet.getString("turmas").split(",");
			q: for (String turma : turmas) {
				for (TurmaVO turmaVO : turmaVOs) {
					if (turmaVO.getCodigo().equals(Integer.valueOf(turma))) {
						continue q;
					}
				}
				turmaVOs.add(consultarPorChavePrimaria(Integer.valueOf(turma), nivelMontarDados, usuarioVO));
			}
		}
		return turmaVOs;
	}

	@Override
	public List<TurmaVO> consultaRapidaNomeCurso(String valorConsulta, List<CursoVO> cursoVOs, Integer turmaOrigem, List<UnidadeEnsinoVO> unidadeVOs, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE lower(sem_acentos(curso.nome)) like (sem_acentos('");
		sql.append(valorConsulta.toLowerCase());
		sql.append("%'))");
		if (Uteis.isAtributoPreenchido(cursoVOs)) {
			sql.append(" AND curso.codigo  IN (");
			int x = 0;
			for (CursoVO cursoVO : cursoVOs) {
				if (x > 0) {
					sql.append(", ");
				}
				sql.append(cursoVO.getCodigo());
				x++;
			}
			sql.append(" ) ");
		}
		if (Uteis.isAtributoPreenchido(unidadeVOs)) {
			sql.append(" AND unidadeensino.codigo  IN (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeVOs) {
				if (x > 0) {
					sql.append(", ");
				}
				sql.append(unidadeEnsinoVO.getCodigo());
				x++;
			}
			sql.append(" ) ");
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX')  OR turma.turmaAgrupada)");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND curso.nivelEducacional != 'PO'");
		}
		if (turmaOrigem.intValue() != 0) {
			sql.append(" and turma.codigo <> ");
			sql.append(turmaOrigem.intValue());
		}
		sql.append(" ORDER BY curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public boolean consultarExistenciaMatriculaEProgramacaoAulaVinculadaTurmaDisciplina(Integer turma, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select distinct * from (");
		sqlStr.append("	select 1 from matriculaperiodoturmadisciplina");
		sqlStr.append("	where turma = ").append(turma);
		sqlStr.append("	and disciplina = ").append(disciplina);
		sqlStr.append("	union");
		sqlStr.append("	select 1 from horarioturma");
		sqlStr.append("	inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
		sqlStr.append("	inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");
		sqlStr.append("	where turma = ").append(turma);
		sqlStr.append("	and disciplina = ").append(disciplina);
		sqlStr.append(") as t ");
		sqlStr.append("limit 1");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
	}

	@Override
	public void executarVerificacaoTurmaDisciplinaManterAtualizacaoDisciplina(TurmaVO turmaVO, List<TurmaDisciplinaVO> turmaDisciplinaVOs, boolean verificarDisciplinasExcluir, UsuarioVO usuarioVO) throws Exception {
		List<TurmaDisciplinaVO> turmaDisciplinaManterVOs = new ArrayList<TurmaDisciplinaVO>(0);
		getFacadeFactory().getTurmaDisciplinaFacade().realizarVerificacaoExistenciaAlunoMatriculaOUAulaProgramadaTurmaDisciplina(turmaVO);
		for (TurmaDisciplinaVO tdVO : turmaVO.getTurmaDisciplinaVOs()) {
			if (tdVO.getPossuiRestricao()) {
				turmaDisciplinaManterVOs.add(tdVO);
			}
		}
		List<TurmaDisciplinaVO> turmaDisciplinaAdicionarVOs = new ArrayList<TurmaDisciplinaVO>(0);
		q: for (TurmaDisciplinaVO turmaDisciplinaGerada : turmaDisciplinaVOs) {
			for (TurmaDisciplinaVO turmaDisciplinaManter : turmaDisciplinaManterVOs) {
				if (turmaDisciplinaManter.getDisciplina().getCodigo().equals(turmaDisciplinaGerada.getDisciplina().getCodigo())) {
					// Atualizar as disciplina equivalente caso foi criado apois o cadastro da turma agrupada
					turmaDisciplinaManter.setDisciplinaEquivalenteTurmaAgrupada(turmaDisciplinaGerada.getDisciplinaEquivalenteTurmaAgrupada());
					turmaDisciplinaManter.setMensagemDisciplinaEquivalenteTurmaAgrupada(turmaDisciplinaGerada.getMensagemDisciplinaEquivalenteTurmaAgrupada());
					continue q;
				}
			}
			for (TurmaDisciplinaVO tdVO : turmaVO.getTurmaDisciplinaVOs()) {
				if (tdVO.getDisciplina().getCodigo().equals(turmaDisciplinaGerada.getDisciplina().getCodigo())) {
					// Atualizar as disciplina equivalente caso foi criado apois o cadastro da turma agrupada
					tdVO.setDisciplinaEquivalenteTurmaAgrupada(turmaDisciplinaGerada.getDisciplinaEquivalenteTurmaAgrupada());
					tdVO.setMensagemDisciplinaEquivalenteTurmaAgrupada(turmaDisciplinaGerada.getMensagemDisciplinaEquivalenteTurmaAgrupada());
					continue q;
				}
			}
			turmaDisciplinaGerada.setCodigo(0);
			turmaDisciplinaGerada.setNovoObj(true);
			turmaDisciplinaGerada.setTurma(turmaVO.getCodigo());
			turmaDisciplinaAdicionarVOs.add(turmaDisciplinaGerada);
		}
		turmaVO.getTurmaDisciplinaVOs().addAll(turmaDisciplinaAdicionarVOs);
	}

	@Override
	public void executarGeracaoTurmaDisciplinaVOs(TurmaVO turmaVO, List<GradeDisciplinaVO> gradeDisciplinaVOs, UsuarioVO usuarioVO) throws Exception {
		if (!turmaVO.isNovoObj() && !turmaVO.getGradeCurricularAtiva().equals(turmaVO.getGradeCurricularVO().getCodigo())) {
			turmaVO.setDisciplinasAtualizadasAlteracaoMatrizCurricular(true);
			turmaVO.setTurmaDisciplinaVOs(new ArrayList<TurmaDisciplinaVO>(0));
		}
		List<Integer> discNaoPermiteRepo = new ArrayList<Integer>();
		List<Integer> discManter = new ArrayList<Integer>();
		Iterator<TurmaDisciplinaVO> j = turmaVO.getTurmaDisciplinaVOs().iterator();
		while (j.hasNext()) {
			TurmaDisciplinaVO turmaDisc = j.next();
			if (!turmaDisc.getPermiteReposicao()) {
				discNaoPermiteRepo.add(turmaDisc.getDisciplina().getCodigo());
			}
			discManter.add(turmaDisc.getCodigo());
		}
		List<TurmaDisciplinaVO> turmaDisciplinaVOs = new ArrayList<TurmaDisciplinaVO>(0);
		Iterator<GradeDisciplinaVO> i = gradeDisciplinaVOs.iterator();
		q: while (i.hasNext()) {
			Boolean permiteReposicao = true;
			GradeDisciplinaVO grade = i.next();
			if (discManter.contains(grade.getDisciplina().getCodigo())) {
				continue q;
			}
			Iterator<Integer> o = discNaoPermiteRepo.iterator();
			while (o.hasNext()) {
				Integer disciplina = (Integer) o.next();
				if (disciplina.intValue() == grade.getDisciplina().getCodigo().intValue()) {
					permiteReposicao = false;
				}
			}
			turmaDisciplinaVOs.add(getFacadeFactory().getTurmaDisciplinaFacade().executarGeracaoTurmaDisciplinaVO(grade.getDisciplina(), grade.getModalidadeDisciplina(), false, grade, null, grade.getConfiguracaoAcademico(), permiteReposicao, turmaVO.getNrMaximoMatricula(), turmaVO.getNrVagas()));
		}
		executarVerificacaoTurmaDisciplinaManterAtualizacaoDisciplina(turmaVO, turmaDisciplinaVOs, true, usuarioVO);
	}

	@Override
	public void executarGeracaoTurmaDisciplinaGradeCurricularGrupoOptativaDisciplina(TurmaVO turmaVO, GradeCurricularGrupoOptativaDisciplinaVO obj, UsuarioVO usuarioVO) throws Exception {
		List<TurmaDisciplinaVO> turmaDisciplinaVOs = new ArrayList<TurmaDisciplinaVO>(0);
		turmaDisciplinaVOs.add(getFacadeFactory().getTurmaDisciplinaFacade().executarGeracaoTurmaDisciplinaVO(obj.getDisciplina(), obj.getModalidadeDisciplina(), true, null, obj, obj.getConfiguracaoAcademico(), true, turmaVO.getNrMaximoMatricula(), turmaVO.getNrVagas()));
		executarVerificacaoTurmaDisciplinaManterAtualizacaoDisciplina(turmaVO, turmaDisciplinaVOs, false, usuarioVO);
	}

	@Override
	public boolean executarVerificacaoDisciplinaCursandoEProgramacaoAula(TurmaDisciplinaVO tdVO, TurmaVO turmaVO, UsuarioVO usuarioVO) throws Exception {
		boolean manter = false;
		if (tdVO.getGradeDisciplinaVO().getDisciplinaComposta()) {
			List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs = new ArrayList<GradeDisciplinaCompostaVO>(0);
			if (Uteis.isAtributoPreenchido(tdVO.getGradeCurricularGrupoOptativaDisciplinaVO())) {
				gradeDisciplinaCompostaVOs = getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorGrupoOptativaDisciplina(tdVO.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			} else {
				gradeDisciplinaCompostaVOs = getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorGradeDisciplina(tdVO.getGradeDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			}
			for (GradeDisciplinaCompostaVO gdcVO : gradeDisciplinaCompostaVOs) {
				if (getFacadeFactory().getTurmaFacade().consultarExistenciaMatriculaEProgramacaoAulaVinculadaTurmaDisciplina(turmaVO.getCodigo(), gdcVO.getDisciplina().getCodigo(), false, usuarioVO)) {
					manter = true;
				}
			}
		} else {
			manter = getFacadeFactory().getTurmaFacade().consultarExistenciaMatriculaEProgramacaoAulaVinculadaTurmaDisciplina(turmaVO.getCodigo(), tdVO.getDisciplina().getCodigo(), false, usuarioVO);
		}
		return manter;
	}

	public List<TurmaVO> consultaRapidaNivelComboboxPorListaUnidadeEnsinoIdentificadorTurma(List<UnidadeEnsinoVO> unidadeEnsinoVOs, String identificadorTurma, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select turma.codigo AS \"turma.codigo\", turma.identificadorturma, curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", turno.codigo AS \"turno.codigo\", turno.nome AS \"turno.nome\" ");
		sb.append(" from turma ");
		sb.append(" inner join curso on curso.codigo = turma.curso ");
		sb.append(" inner join turno on turno.codigo = turma.turno ");
		sb.append(" where lower (Turma.identificadorTurma) like('").append(identificadorTurma.toLowerCase()).append("%') ");
		sb.append(" and turma.unidadeEnsino in(");

		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				sb.append(unidadeEnsinoVO.getCodigo()).append(", ");
			}
		}
		sb.append("0) ");
		sb.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<TurmaVO> listaTurmaVO = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(tabelaResultado.getInt("turma.codigo"));
			obj.setIdentificadorTurma(tabelaResultado.getString("identificadorturma"));
			obj.getCurso().setCodigo(tabelaResultado.getInt("curso.codigo"));
			obj.getCurso().setNome(tabelaResultado.getString("curso.nome"));
			obj.getTurno().setCodigo(tabelaResultado.getInt("turno.codigo"));
			obj.getTurno().setNome(tabelaResultado.getString("turno.nome"));
			listaTurmaVO.add(obj);
		}
		return listaTurmaVO;
	}

	public List<TurmaVO> consultaRapidaNivelComboboxPorNomeCursoListaUnidadeEnsino(String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select turma.codigo AS \"turma.codigo\", turma.identificadorturma, curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", turno.codigo AS \"turno.codigo\", turno.nome AS \"turno.nome\" ");
		sb.append(" from turma ");
		sb.append(" inner join curso on curso.codigo = turma.curso ");
		sb.append(" inner join turno on turno.codigo = turma.turno ");
		sb.append(" WHERE upper(sem_acentos(curso.nome)) like(sem_acentos('");
		sb.append(valorConsulta.toUpperCase());
		sb.append("%')");
		sb.append(" )");
		sb.append(" and turma.unidadeEnsino in(");
		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				sb.append(unidadeEnsinoVO.getCodigo()).append(", ");
			}
		}
		sb.append("0) ");
		sb.append(" ORDER BY curso.nome, turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<TurmaVO> listaTurmaVO = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(tabelaResultado.getInt("turma.codigo"));
			obj.setIdentificadorTurma(tabelaResultado.getString("identificadorturma"));
			obj.getCurso().setCodigo(tabelaResultado.getInt("curso.codigo"));
			obj.getCurso().setNome(tabelaResultado.getString("curso.nome"));
			obj.getTurno().setCodigo(tabelaResultado.getInt("turno.codigo"));
			obj.getTurno().setNome(tabelaResultado.getString("turno.nome"));
			listaTurmaVO.add(obj);
		}
		return listaTurmaVO;
	}

	@Override
	public Boolean consultarExistenciaAulaProgramadaTurmaConsiderandoTurmaAgrupada(Integer turma, Integer codDisciplina, String ano, String semestre, String situacaoTurma) throws Exception {
		
		
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select ofertadisciplina.codigo from ofertadisciplina where disciplina = ").append(codDisciplina).append(" and ano = '").append(ano).append("' and semestre = '").append(semestre).append("' ");
		
//		sqlStr.append("SELECT DISTINCT(Turma.codigo), Turma.identificadorTurma ");
//		sqlStr.append("FROM Turma ");
//		sqlStr.append("inner JOIN TurmaDisciplina ON (TurmaDisciplina.turma = Turma.codigo) ");
//		sqlStr.append("LEFT JOIN GradeDisciplina ON (TurmaDisciplina.gradeDisciplina = GradeDisciplina.codigo) ");
//		sqlStr.append("LEFT JOIN GradeCurricularGrupoOptativaDisciplina ON (TurmaDisciplina.gradeCurricularGrupoOptativaDisciplina = GradeCurricularGrupoOptativaDisciplina.codigo) ");
//		sqlStr.append("LEFT JOIN unidadeEnsino on unidadeEnsino.codigo = turma.unidadeensino ");
//		sqlStr.append("inner JOIN disciplina on ((disciplina.codigo = turmaDisciplina.disciplina and disciplina.codigo = " + codDisciplina + ") ");
//		sqlStr.append(" or (gradeDisciplina.disciplinacomposta and disciplina.codigo = (select gradedisciplinacomposta.disciplina from gradedisciplinacomposta where  gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo and gradedisciplinacomposta.disciplina = " + codDisciplina + " limit 1)) ");
//		sqlStr.append(" or (GradeCurricularGrupoOptativaDisciplina.disciplinacomposta and disciplina.codigo = (select gradedisciplinacomposta.disciplina from gradedisciplinacomposta where  gradedisciplinacomposta.GradeCurricularGrupoOptativaDisciplina = GradeCurricularGrupoOptativaDisciplina.codigo and gradedisciplinacomposta.disciplina = " + codDisciplina + " limit 1))) ");
//		sqlStr.append(" WHERE Turma.subturma = false ");
//		sqlStr.append(" AND Turma.turmaagrupada = false ");
//		sqlStr.append(" AND turma.codigo = ").append(turma);
//		sqlStr.append(" and (");
//		sqlStr.append(" exists (");
//		sqlStr.append(" 	select ht.codigo from horarioturma  ht ");
//		sqlStr.append(" 	inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
//		sqlStr.append(" 	inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
//		sqlStr.append(" 	where  ht.turma = turma.codigo");
//		sqlStr.append(" 	and ((turma.semestral and ht.anovigente = '").append(ano).append("'");
//		sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
//		sqlStr.append(" 	or (turma.anual and ht.anovigente = '").append(ano).append("') ");
//		sqlStr.append(" 	or (turma.semestral = false and turma.anual = false))");
//		sqlStr.append(" 	and ((htdi.disciplina = ").append(codDisciplina).append(") ");
//		sqlStr.append(" 	) ");
//		sqlStr.append(" )");
//		sqlStr.append(" or ");
//		sqlStr.append(" exists (");
//		sqlStr.append(" 	select t.codigo from horarioturma  ht 	 ");
//		sqlStr.append(" 	inner join turma as t on t.codigo = ht.turma");
//		sqlStr.append(" 	inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
//		sqlStr.append(" 	inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
//		sqlStr.append(" 	where  t.turmaprincipal = turma.codigo and t.subturma and t.turmaagrupada = false ");
//		if (Uteis.isAtributoPreenchido(situacaoTurma)) {
//			sqlStr.append(" 	and t.situacao = '").append(situacaoTurma).append("'");
//		}
//		sqlStr.append(" 	and ((turma.semestral and ht.anovigente = '").append(ano).append("'");
//		sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
//		sqlStr.append(" 	or (turma.anual and ht.anovigente = '").append(ano).append("') ");
//		sqlStr.append(" 	or (turma.semestral = false and turma.anual = false))");
//		sqlStr.append(" 	and ((htdi.disciplina = ").append(codDisciplina).append(") ");
//		sqlStr.append(" 	) ");
//		sqlStr.append(" 	limit 1");
//		sqlStr.append(" )");
//		sqlStr.append(" or");
//		sqlStr.append(" exists (");
//		sqlStr.append(" 	select t.codigo from horarioturma  ht");
//		sqlStr.append(" 	inner join turma as t on t.codigo = ht.turma");
//		sqlStr.append(" 	inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo	");
//		sqlStr.append(" 	inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
//		sqlStr.append(" 	inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
//		sqlStr.append(" 	where  turmaagrupada.turma = turma.codigo");
//		if (Uteis.isAtributoPreenchido(situacaoTurma)) {
//			sqlStr.append(" 	and t.situacao = '").append(situacaoTurma).append("'");
//		}
//		sqlStr.append(" 	and ((turma.semestral and ht.anovigente = '").append(ano).append("'");
//		sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
//		sqlStr.append(" 	or (turma.anual and ht.anovigente = '").append(ano).append("') ");
//		sqlStr.append(" 	or (turma.semestral = false and turma.anual = false))");
//		sqlStr.append(" 	and ((htdi.disciplina = ").append(codDisciplina).append(") ");
//		sqlStr.append(" 	) ");
//		sqlStr.append(" 	limit 1)");
//		sqlStr.append(" or exists (");
//		sqlStr.append(" 	select t.codigo from horarioturma  ht");
//		sqlStr.append(" 	inner join turma as t on t.codigo = ht.turma");
//		sqlStr.append(" 	inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo	");
//		sqlStr.append(" 	inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
//		sqlStr.append(" 	inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
//		sqlStr.append(" 	inner join disciplinaequivalente de on de.equivalente = htdi.disciplina ");
//		sqlStr.append(" 	where  turmaagrupada.turma = turma.codigo");
//		if (Uteis.isAtributoPreenchido(situacaoTurma)) {
//			sqlStr.append(" 	and t.situacao = '").append(situacaoTurma).append("'");
//		}
//		sqlStr.append(" 	and ((turma.semestral and ht.anovigente = '").append(ano).append("'");
//		sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
//		sqlStr.append(" 	or (turma.anual and ht.anovigente = '").append(ano).append("') ");
//		sqlStr.append(" 	or (turma.semestral = false and turma.anual = false))");
//		sqlStr.append(" 	and ((de.disciplina = ").append(codDisciplina).append(") ");
//		sqlStr.append(" 	) ");
//
//		sqlStr.append(" 	limit 1)");
//
//		sqlStr.append("  or exists (");
//		sqlStr.append(" 				(select t.codigo from horarioturma  ht");
//		sqlStr.append(" 					inner join turma as t on t.codigo = ht.turma");
//		sqlStr.append(" 					inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo	");
//		sqlStr.append(" 					inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
//		sqlStr.append(" 					inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
//		sqlStr.append(" 					inner join disciplinaequivalente de on de.equivalente = htdi.disciplina ");
//		sqlStr.append(" 					where  turmaagrupada.turma ");
//		sqlStr.append(" 					in ( select tsub.codigo from turma as tsub where tsub.subturma = true and tsub.tiposubturma in ('PRATICA', 'TEORICA') ");
//		sqlStr.append(" 					and tsub.turmaprincipal = turma.codigo ");
//		if (Uteis.isAtributoPreenchido(situacaoTurma)) {
//			sqlStr.append(" 					and tsub.situacao = 'AB' ");
//		}
//		sqlStr.append(" 					) ");
//		if (Uteis.isAtributoPreenchido(situacaoTurma)) {
//			sqlStr.append(" 					and t.situacao = 'AB'			");
//		}
//		sqlStr.append(" 					and ((turma.semestral and ht.anovigente = '").append(ano).append("'");
//		sqlStr.append(" 					and ht.semestrevigente = '").append(semestre).append("')");
//		sqlStr.append(" 					or (turma.anual and ht.anovigente = '").append(ano).append("') ");
//		sqlStr.append(" 					or (turma.semestral = false and turma.anual = false))			");
//		sqlStr.append(" 					and ((de.disciplina = ").append(codDisciplina).append(")");
//		sqlStr.append(" 				   ) limit 1)");
//		sqlStr.append(" 			 	)");
//		sqlStr.append(" 			 or exists (");
//		sqlStr.append(" 				(select t.codigo from horarioturma  ht");
//		sqlStr.append(" 					inner join turma as t on t.codigo = ht.turma");
//		sqlStr.append(" 					inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo	");
//		sqlStr.append(" 					inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
//		sqlStr.append(" 					inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia					");
//		sqlStr.append(" 					where  turmaagrupada.turma ");
//		sqlStr.append(" 					in ( select tsub.codigo from turma as tsub where tsub.subturma = true and tsub.tiposubturma in ('PRATICA', 'TEORICA') ");
//		sqlStr.append(" 					and tsub.turmaprincipal = turma.codigo ");
//		if (Uteis.isAtributoPreenchido(situacaoTurma)) {
//			sqlStr.append(" 					and tsub.situacao = 'AB' ");
//		}
//		sqlStr.append(" 					) ");
//		if (Uteis.isAtributoPreenchido(situacaoTurma)) {
//			sqlStr.append(" 					and t.situacao = 'AB'			");
//		}
//		sqlStr.append(" 					and ((turma.semestral and ht.anovigente = '").append(ano).append("'");
//		sqlStr.append(" 					and ht.semestrevigente = '").append(semestre).append("')");
//		sqlStr.append(" 					or (turma.anual and ht.anovigente = '").append(ano).append("') ");
//		sqlStr.append(" 					or (turma.semestral = false and turma.anual = false))			");
//		sqlStr.append(" 					and ((htdi.disciplina = ").append(codDisciplina).append(")");
//		sqlStr.append(" 				   ) limit 1)");
//		sqlStr.append(" 			 	)");
//
//		sqlStr.append(" )");

		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
	}

	@Override
	public List<TurmaVO> consultaRapidaNivelComboboxPorForum(ForumVO forum, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		SqlRowSet tabelaResultado = null;
		List<TurmaVO> listaTurmaVO = new ArrayList<TurmaVO>(0);
		TurmaVO obj = new TurmaVO();
		try {
			sb.append(" select distinct turma.codigo AS \"turma.codigo\", turma.identificadorturma ");
			sb.append(" from foruminteracao  ");
			sb.append(" inner join forum on forum.codigo = foruminteracao.forum ");
			sb.append(" inner join usuario on usuario.codigo = foruminteracao.usuariointeracao ");
			sb.append(" inner join pessoa on pessoa.codigo = usuario.pessoa and pessoa.aluno = true ");
			sb.append(" inner join matricula on matricula.aluno = pessoa.codigo ");
			sb.append(" inner join matriculaperiodoturmadisciplina mptd on mptd.matricula = matricula.matricula and mptd.disciplina = forum.disciplina ");
			sb.append(" inner join turma on turma.codigo  = mptd.turma ");
			sb.append(" where  1=1 ");
			if (Uteis.isAtributoPreenchido(forum.getCodigo())) {
				sb.append(" and forum.codigo =  ").append(forum.getCodigo());
			}
			if (Uteis.isAtributoPreenchido(forum.getDisciplina().getCodigo())) {
				sb.append(" and forum.disciplina = ").append(forum.getDisciplina().getCodigo());
			}
			sb.append(" ORDER BY turma.identificadorturma ");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			while (tabelaResultado.next()) {
				obj = new TurmaVO();
				obj.setCodigo(tabelaResultado.getInt("turma.codigo"));
				obj.setIdentificadorTurma(tabelaResultado.getString("identificadorturma"));
				listaTurmaVO.add(obj);
			}
		} finally {
			obj = null;
			tabelaResultado = null;
			sb = null;
		}
		return listaTurmaVO;
	}

	@Override
	public List<TurmaVO> consultaRapidaNomeCursoSituacaoTruma(String valorConsulta, Integer turmaOrigem, Integer unidadeEnsino, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, boolean controlarAcesso, int nivelMontarDados, String situacao, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE lower(sem_acentos(curso.nome)) ilike (sem_acentos('");
		sql.append(valorConsulta.toLowerCase());
		sql.append("%'))");
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX')  OR turma.turmaAgrupada)");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND curso.nivelEducacional != 'PO'");
		}
		if (turmaOrigem.intValue() != 0) {
			sql.append(" and turma.codigo <> ");
			sql.append(turmaOrigem.intValue());
		}
		if (situacao != null && !situacao.isEmpty()) {
			sql.append(" AND turma.situacao = '").append(situacao).append("' ");
		}
		sql.append(" ORDER BY curso.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Override
	public List<TurmaVO> consultaRapidaPorUnidadeIdentificadorTurmaCurso(String identificador, Integer curso, Integer unidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE upper( turma.identificadorturma )like('");
		sql.append(identificador.toUpperCase());
		sql.append("%') ");
		if (curso != 0) {
			sql.append(" AND (turma.curso = ").append(curso).append(") ");
		}
		if (unidade.intValue() != 0) {
			sql.append(" AND (turma.unidadeensino = ").append(unidade.intValue()).append(") ");
		}
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	@Override
	public void alterarSituacaoTurma(final TurmaVO obj, UsuarioVO usuario) throws Exception {
		try {
			alterar(getIdEntidade(), true, usuario);
			final String sql = "UPDATE Turma set  situacao = ?, dataultimaalteracao = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getSituacao().trim());
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(new Date()));
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void validarDadosRemocaoTurmaAgrupada(TurmaVO turmaVO, TurmaAgrupadaVO turmaAgrupadaVO, UsuarioVO usuarioVO) throws ConsistirException {
		if (Uteis.isAtributoPreenchido(turmaAgrupadaVO)) {
			StringBuilder sql = new StringBuilder("select distinct disciplina.codigo, disciplina.nome from turmadisciplina ");
			sql.append(" inner join turmadisciplina as td2 on td2.turma =  ").append(turmaAgrupadaVO.getTurma().getCodigo());
			sql.append(" and (td2.disciplina = turmadisciplina.disciplina or td2.disciplina in (select disciplina from disciplinaequivalente where equivalente =turmadisciplina.disciplina) or td2.disciplina in (select equivalente from disciplinaequivalente where disciplina = turmadisciplina.disciplina)) ");
			sql.append(" inner join disciplina on disciplina.codigo =  turmadisciplina.disciplina ");
			sql.append(" where turmadisciplina.turma = ").append(turmaVO.getCodigo());
			sql.append(" and exists (select htdi.codigo from horarioturma ht ");
			sql.append(" inner join horarioturmadia as htd on htd.horarioturma = ht.codigo ");
			sql.append(" inner join horarioturmadiaitem as htdi on htdi.horarioturmadia = htd.codigo ");
			sql.append(" inner join registroaula on registroaula.turma = ht.turma and registroaula.data = htdi.data and registroaula.disciplina = htdi.disciplina ");
			sql.append(" and registroaula.nraula = htdi.nraula ");
			sql.append(" where htdi.disciplina = disciplina.codigo and  ht.turma =  ").append(turmaVO.getCodigo()).append(" limit 1) ");
			StringBuilder disciplinas = new StringBuilder();
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			while (rs.next()) {
				if (disciplinas.length() > 0) {
					disciplinas.append(", ");
				}
				disciplinas.append(rs.getString("nome"));
			}
			if (disciplinas.length() > 0) {
				throw new ConsistirException("Não foi possivel remover a turma \"" + turmaAgrupadaVO.getTurma().getIdentificadorTurma() + "\", pois já existe(m) aula(s) registrada(s) para a(s) disciplina(s) (" + disciplinas + ") que são compartilhadas por esta turma.");
			}
		}

	}

	public List<TurmaVO> consultaRapidaTurmasDRE(List<UnidadeEnsinoVO> unidadeEnsinoVOs, CursoVO cursoVO, TurmaVO turmaParamVO, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct turma.codigo, turma.identificadorturma from turma ");
		sql.append("left join contapagar on contapagar.turma = turma.codigo ");
		sql.append("left join contareceber on contareceber.turma = turma.codigo ");
		sql.append(" left join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		sql.append(" left join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento ");
		sql.append(" left join categoriadespesa on categoriadespesa.codigo = contapagar.centrodespesa ");
		sql.append(" left join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo ");
		sql.append(" left join negociacaopagamento on negociacaopagamento.codigo = contapagarnegociacaopagamento.negociacaocontapagar ");

		sql.append(" where 1=1  ");

		if (filtrarDataFatoGerador) {
			sql.append(" AND ((contareceber.datavencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND contareceber.datavencimento <= '").append(Uteis.getDataJDBC(dataFinal)).append("' and contareceber.situacao = 'RE') or ( ");
			sql.append(" contapagar.datavencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND contapagar.datavencimento <= '").append(Uteis.getDataJDBC(dataFinal)).append("' and contapagar.situacao = 'PA')) ");
		} else {
			sql.append(" AND ((negociacaorecebimento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND negociacaorecebimento.data <= '").append(Uteis.getDataJDBC(dataFinal)).append("') or ( ");
			sql.append(" negociacaopagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND negociacaopagamento.data <= '").append(Uteis.getDataJDBC(dataFinal)).append("')) ");
		}
		sql.append(" and turma.unidadeEnsino in (");
		String aux = "";
		for (UnidadeEnsinoVO unidadeEnsino : unidadeEnsinoVOs) {
			if (unidadeEnsino.getFiltrarUnidadeEnsino()) {
				sql.append(aux).append(unidadeEnsino.getCodigo());
				aux = ",";
			}
		}
		sql.append(") ");
		if (turmaParamVO != null && turmaParamVO.getCodigo() > 0) {
			sql.append(" and turma.codigo = ").append(turmaParamVO.getCodigo());
		}
		if (cursoVO != null && cursoVO.getCodigo() > 0) {
			sql.append(" and turma.curso = ").append(cursoVO.getCodigo());
		}

		sql.append(" order by turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setIdentificadorTurma(tabelaResultado.getString("identificadorturma"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public Double consultaRapidaMatriculaDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(contareceber.valor) as valor from turma  ");
		// sql.append("select sum(contareceber.valorrecebido) as valor from turma ");
		sql.append(" inner join contareceber on contareceber.turma = turma.codigo ");
		sql.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		sql.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento ");
		if (turma > 0) {
			sql.append(" where turma.codigo = ").append(turma);
		} else {
			sql.append(" where turma.codigo is not null ");
		}
		if (filtrarDataFatoGerador) {
			sql.append(" AND contareceber.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND contareceber.dataVencimento <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
		} else {
			sql.append(" AND negociacaorecebimento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND negociacaorecebimento.data <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
		}
		sql.append(" and contareceber.tipoorigem = 'MAT' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("valor");
		} else {
			return 0.0;
		}
	}

	public Double consultaRapidaMensalidadeDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(contareceber.valor) as valor from turma  ");
		// sql.append("select sum(contareceber.valorrecebido) as valor from turma ");
		sql.append(" inner join contareceber on contareceber.turma = turma.codigo ");
		sql.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		sql.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento ");
		if (turma > 0) {
			sql.append(" where turma.codigo = ").append(turma);
		} else {
			sql.append(" where turma.codigo is not null ");
		}
		if (filtrarDataFatoGerador) {
			sql.append(" AND contareceber.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND contareceber.dataVencimento <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
		} else {
			sql.append(" AND negociacaorecebimento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND negociacaorecebimento.data <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
		}

		sql.append(" and contareceber.tipoorigem = 'MEN' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("valor");
		} else {
			return 0.0;
		}
	}

	public Double consultaRapidaTributosDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select sum(valorpago) as valor   from contapagar ");
		sql.append(" inner join categoriadespesa on categoriadespesa.codigo = contapagar.centrodespesa ");
		sql.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo ");
		sql.append(" inner join negociacaopagamento on negociacaopagamento.codigo = contapagarnegociacaopagamento.negociacaocontapagar ");
		if (turma > 0) {
			sql.append(" where contapagar.turma = ").append(turma);
		} else {
			sql.append(" where contapagar.turma is not null ");
		}
		sql.append(" and contapagar.valorpago > 0 ");
		if (filtrarDataFatoGerador) {
			sql.append(" AND contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
		} else {
			sql.append(" AND negociacaopagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND negociacaopagamento.data <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
		}

		sql.append(" and categoriadespesa.tributo = true ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("valor");
		} else {
			return 0.0;
		}
	}

	public Double consultaRapidaDescontosDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select (sum(contareceber.valordescontocalculado) + sum (contareceber.valordescontolancadorecebimento)) as valor from turma  ");
		sql.append(" inner join contareceber on contareceber.turma = turma.codigo ");
		sql.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		sql.append(" inner join negociacaorecebimento on negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento ");
		if (turma > 0) {
			sql.append(" where turma.codigo = ").append(turma);
		} else {
			sql.append(" where turma.codigo is not null ");
		}
		if (filtrarDataFatoGerador) {
			sql.append(" AND contareceber.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND contareceber.dataVencimento <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
		} else {
			sql.append(" AND negociacaorecebimento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND negociacaorecebimento.data <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("valor");
		} else {
			return 0.0;
		}
	}

	public Double consultaRapidaCancelamentosDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select sum(valorpago) as valor   from contapagar ");
		sql.append(" inner join categoriadespesa on categoriadespesa.codigo = contapagar.centrodespesa ");
		sql.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo ");
		sql.append(" inner join negociacaopagamento on negociacaopagamento.codigo = contapagarnegociacaopagamento.negociacaocontapagar ");
		if (turma > 0) {
			sql.append(" where contapagar.turma = ").append(turma);
		} else {
			sql.append(" where contapagar.turma is not null ");
		}
		sql.append(" and contapagar.valorpago > 0 ");
		if (filtrarDataFatoGerador) {
			sql.append(" AND contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
		} else {
			sql.append(" AND negociacaopagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND negociacaopagamento.data <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
		}

		sql.append(" and categoriadespesa.cancelamento = true ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("valor");
		} else {
			return 0.0;
		}
	}

	public Double consultaRapidaReceitaLiquidaDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct turma.codigo, turma.identificadorturma from turma ");
		sql.append(" order by turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("codigo");
		} else {
			return 0.0;
		}
	}

//	public Double consultaRapidaCustosDespesaVariavelDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal) throws Exception {
//		String despesas = "('' ";
//		List<CategoriaDespesaVO> categorias = getFacadeFactory().getCategoriaDespesaFacade().consultaRapidaCategoriaDespDRE();
//		Iterator<CategoriaDespesaVO> c = categorias.iterator();
//		while (c.hasNext()) {
//			CategoriaDespesaVO categoria = c.next();
//			despesas += ",'" + categoria.getDescricao() + "' ";
//		}
//		despesas += ")";
//
//		StringBuilder sql = new StringBuilder();
//		sql.append(" select sum(valorpago) as valor   from contapagar ");
//		sql.append(" inner join categoriadespesa on categoriadespesa.codigo = contapagar.centrodespesa ");
//		sql.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo ");
//		sql.append(" inner join negociacaopagamento on negociacaopagamento.codigo = contapagarnegociacaopagamento.negociacaocontapagar ");
//		if (turma > 0) {
//			sql.append(" where contapagar.turma = ").append(turma);
//		} else {
//			sql.append(" where contapagar.turma is not null ");
//		}
//		sql.append(" and contapagar.valorpago > 0 ");
//
//		if (filtrarDataFatoGerador) {
//			sql.append(" AND contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
//			sql.append(" AND contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
//		} else {
//			sql.append(" AND negociacaopagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
//			sql.append(" AND negociacaopagamento.data <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
//		}
//
//		sql.append(" and categoriadespesa.descricao in ").append(despesas);
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
//		if (tabelaResultado.next()) {
//			return tabelaResultado.getDouble("valor");
//		} else {
//			return 0.0;
//		}
//	}

	public Double consultaRapidaMargemContribuicaoDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct turma.codigo, turma.identificadorturma from turma ");
		sql.append(" order by turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("codigo");
		} else {
			return 0.0;
		}
	}

	public Double consultaRapidaDespesasFixasDRE(Integer turma, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal) throws Exception {
		if (turma == 0) {
			StringBuilder sql = new StringBuilder();
			sql.append(" select sum(valorpago) as valor   from contapagar ");
			sql.append(" inner join categoriadespesa on categoriadespesa.codigo = contapagar.centrodespesa ");
			sql.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo ");
			sql.append(" inner join negociacaopagamento on negociacaopagamento.codigo = contapagarnegociacaopagamento.negociacaocontapagar ");
			sql.append(" where  contapagar.turma  is null ");
			sql.append(" and contapagar.valorpago > 0 ");
			if (filtrarDataFatoGerador) {
				sql.append(" AND contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
				sql.append(" AND contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
			} else {
				sql.append(" AND negociacaopagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
				sql.append(" AND negociacaopagamento.data <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
			}

			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			if (tabelaResultado.next()) {
				return tabelaResultado.getDouble("valor");
			} else {
				return 0.0;
			}
		} else {
			return 0.0;
		}
	}

	public Double consultaRapidaCategoriaDespesaDRE(Integer turma, String identificadorCategoriaDespesa, Boolean filtrarDataFatoGerador, Date dataInicio, Date dataFinal) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select sum(valorpago) as valor   from contapagar ");
		sql.append(" inner join categoriadespesa on categoriadespesa.codigo = contapagar.centrodespesa ");
		sql.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo ");
		sql.append(" inner join negociacaopagamento on negociacaopagamento.codigo = contapagarnegociacaopagamento.negociacaocontapagar ");
		if (turma > 0) {
			sql.append(" where contapagar.turma = ").append(turma);
		} else {
			sql.append(" where contapagar.turma is not null ");
		}
		sql.append(" and contapagar.valorpago > 0 ");

		if (filtrarDataFatoGerador) {
			sql.append(" AND contapagar.dataVencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND contapagar.dataVencimento <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
		} else {
			sql.append(" AND negociacaopagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			sql.append(" AND negociacaopagamento.data <= '").append(Uteis.getDataJDBC(dataFinal)).append("' ");
		}

		sql.append(" and categoriadespesa.descricao = '").append(identificadorCategoriaDespesa).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getDouble("valor");
		} else {
			return 0.0;
		}
	}

	@Override
	public Boolean consultarExistenciaTurmaVinculadaIndiceReajustePreco(Integer indiceReajuste, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct case when codigo is not null then true else false end AS existeTurmaParaIndiceReajuste from turma where indiceReajuste = ").append(indiceReajuste).append(" limit 1 ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return tabelaResultado.getBoolean("existeTurmaParaIndiceReajuste");
		}
		return false;
	}

	public List<TurmaVO> consultaRapidaPorIdentificadorTurmaUnidadeEnsinoPeriodicidadeCurso(String identificador, Integer unidadeEnsino, String periodicidade, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder("");
		sql.append("SELECT Turma.* FROM Turma inner join curso on turma.curso = curso.codigo WHERE identificadorturma ilike ? ");
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" AND unidadeensino = ").append(unidadeEnsino.intValue());
		}
		if (Uteis.isAtributoPreenchido(periodicidade)) {
			sql.append(" AND curso.periodicidade = ? ");
		}
		sql.append(" ORDER BY identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), identificador.toLowerCase() + "%", periodicidade);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public void carregarDadosHorarioAulaTurmaEDisciplina(TurmaVO turmaVO, Integer codDisciplina, String ano, String semestre, String situacaoTurma, Boolean trazerSomenteTurmaComTutoriaOnline) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");

		// Busca horario na turma base
		sqlStr.append(" 	select min(htd.data) as dataInicio, max(htd.data) as dataTermino, max(case when sala.codigo is null then 0 else sala.codigo end) as sala, min(htdi.professor) as professor, turma.codigo as codigo,  turma.identificadorturma as identificadorTurma ");
		sqlStr.append(" 	from horarioturma  ht ");
		sqlStr.append(" 	inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
		sqlStr.append(" 	inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
		sqlStr.append(" 	left join salalocalaula as sala on sala.codigo = htdi.sala ");
		sqlStr.append(" 	inner join turma on turma.codigo = ht.turma ");
		sqlStr.append(" 	where  ht.turma = ").append(turmaVO.getCodigo());
		if (turmaVO.getSemestral()) {
			sqlStr.append(" 	and (ht.anovigente = '").append(ano).append("'");
			sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
		} else if (turmaVO.getAnual()) {
			sqlStr.append(" and (ht.anovigente = '").append(ano).append("') ");
		}

		sqlStr.append(" 	and htdi.disciplina = ").append(codDisciplina).append(" ");
		sqlStr.append(" 	group by turma.codigo");
		sqlStr.append(" 	having bool_and(htd.data is not null); ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			montarDadosHorarioAulaTurmaEDisciplina(rs, turmaVO, false);
			if (turmaVO.getDataPrimeiraAula() != null) {
				return;
			}
		}
		// Busca horario na turma subturma
		sqlStr = new StringBuilder("");
		sqlStr.append(" 	select min(htd.data) as dataInicio, max(htd.data) as dataTermino, max(case when sala.codigo is null then 0 else sala.codigo end) as sala, min(htdi.professor) as professor, t.codigo as codigo,  t.identificadorturma as identificadorTurma ");
		sqlStr.append(" 	from horarioturma  ht ");
		sqlStr.append(" 	inner join turma as t on t.codigo = ht.turma");
		sqlStr.append(" 	inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
		sqlStr.append(" 	inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
		sqlStr.append(" 	left join salalocalaula as sala on sala.codigo = htdi.sala ");
		sqlStr.append(" 	where  t.turmaprincipal = ").append(turmaVO.getCodigo()).append(" and t.subturma and t.turmaagrupada = false ");
		if (Uteis.isAtributoPreenchido(situacaoTurma)) {
			sqlStr.append(" 					and t.situacao = 'AB'			");
		}
		if (turmaVO.getSemestral()) {
			sqlStr.append(" 	and (ht.anovigente = '").append(ano).append("'");
			sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
		} else if (turmaVO.getAnual()) {
			sqlStr.append(" and ( ht.anovigente = '").append(ano).append("') ");
		}
		sqlStr.append(" and htdi.disciplina = ").append(codDisciplina).append(" ");
		sqlStr.append(" group by t.codigo");
		sqlStr.append(" having bool_and(htd.data is not null);");
		rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			montarDadosHorarioAulaTurmaEDisciplina(rs, turmaVO, false);
			if (turmaVO.getDataPrimeiraAula() != null) {
				return;
			}
		}
		// Busca horario na turma agrupada
		sqlStr = new StringBuilder("");
		sqlStr.append(" 	select min(htd.data) as dataInicio, max(htd.data) as dataTermino, max(case when sala.codigo is null then 0 else sala.codigo end) as sala, min(htdi.professor) as professor, t.codigo as codigo,  t.identificadorturma as identificadorTurma ");
		sqlStr.append(" 	from horarioturma  ht ");
		sqlStr.append(" 	inner join turma as t on t.codigo = ht.turma");
		sqlStr.append(" 	inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo	");
		sqlStr.append(" 	inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
		sqlStr.append(" 	inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
		sqlStr.append(" 	left join salalocalaula as sala on sala.codigo = htdi.sala ");
		sqlStr.append(" 	where  turmaagrupada.turma = ").append(turmaVO.getCodigo());
		if (Uteis.isAtributoPreenchido(situacaoTurma)) {
			sqlStr.append(" 					and t.situacao = 'AB'			");
		}
		if (turmaVO.getSemestral()) {
			sqlStr.append(" 	and (ht.anovigente = '").append(ano).append("'");
			sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
		} else if (turmaVO.getAnual()) {
			sqlStr.append(" and ( ht.anovigente = '").append(ano).append("') ");
		}
		sqlStr.append(" 	and htdi.disciplina = ").append(codDisciplina).append(" ");
		sqlStr.append(" 	group by t.codigo");
		sqlStr.append(" having bool_and(htd.data is not null);");
		rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			montarDadosHorarioAulaTurmaEDisciplina(rs, turmaVO, true);
			if (turmaVO.getDataPrimeiraAula() != null) {
				return;
			}
		}
		sqlStr = new StringBuilder("");
		// Busca horario na turma agrupada considerando disciplina equivalente
		sqlStr.append(" 	select min(htd.data) as dataInicio, max(htd.data) as dataTermino, max(case when sala.codigo is null then 0 else sala.codigo end) as sala, min(htdi.professor) as professor, t.codigo as codigo,  t.identificadorturma as identificadorTurma  ");
		sqlStr.append(" 	from horarioturma  ht ");
		sqlStr.append(" 	inner join turma as t on t.codigo = ht.turma");
		sqlStr.append(" 	inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo	");
		sqlStr.append(" 	inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
		sqlStr.append(" 	inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
//		sqlStr.append(" 	inner join disciplinaequivalente de on de.equivalente = htdi.disciplina ");
		sqlStr.append(" 	left join salalocalaula as sala on sala.codigo = htdi.sala ");
		sqlStr.append(" 	where  turmaagrupada.turma = ").append(turmaVO.getCodigo());
		if (Uteis.isAtributoPreenchido(situacaoTurma)) {
			sqlStr.append(" 					and t.situacao = 'AB'			");
		}
		if (turmaVO.getSemestral()) {
			sqlStr.append(" 	and (ht.anovigente = '").append(ano).append("'");
			sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
		} else if (turmaVO.getAnual()) {
			sqlStr.append(" and ( ht.anovigente = '").append(ano).append("') ");
		}
//		sqlStr.append(" 	and de.disciplina = ").append(codDisciplina).append(" ");
		sqlStr.append(" and ( (exists ( select 1 from disciplinaequivalente d2 ");
		sqlStr.append(" where d2.equivalente = htdi.disciplina)) ");
		sqlStr.append(" or exists (	select 1 from disciplinaequivalente d2 ");
		sqlStr.append(" where d2.disciplina = htdi.disciplina ");
		sqlStr.append(" and (d2.equivalente = ").append(codDisciplina).append(" )))");
		sqlStr.append(" 	group by t.codigo");
		sqlStr.append(" having bool_and(htd.data is not null);");
		rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			montarDadosHorarioAulaTurmaEDisciplina(rs, turmaVO, true);
			if (turmaVO.getDataPrimeiraAula() != null) {
				return;
			}
		}
		// Busca horario na turma agrupada considerando disciplina equivalente considerando subturma
		sqlStr = new StringBuilder("");
		sqlStr.append(" select min(htd.data) as dataInicio, max(htd.data) as dataTermino, max(case when sala.codigo is null then 0 else sala.codigo end) as sala, min(htdi.professor) as professor, t.codigo as codigo,  t.identificadorturma as identificadorTurma ");
		sqlStr.append(" from horarioturma  ht ");
		sqlStr.append(" inner join turma as t on t.codigo = ht.turma");
		sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo	");
		sqlStr.append(" inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
		sqlStr.append(" inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia");
//		sqlStr.append(" inner join disciplinaequivalente de on de.equivalente = htdi.disciplina ");
		sqlStr.append(" 	left join salalocalaula as sala on sala.codigo = htdi.sala ");
		sqlStr.append(" where  turmaagrupada.turma ");
		sqlStr.append(" in ( select tsub.codigo from turma as tsub where tsub.subturma = true and tsub.tiposubturma in ('PRATICA', 'TEORICA') ");
		sqlStr.append("    and tsub.turmaprincipal =  ").append(turmaVO.getCodigo());
		sqlStr.append(" ) ");
		if (Uteis.isAtributoPreenchido(situacaoTurma)) {
			sqlStr.append(" and t.situacao = 'AB' ");
		}
		if (turmaVO.getSemestral()) {
			sqlStr.append(" 	and (ht.anovigente = '").append(ano).append("'");
			sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
		} else if (turmaVO.getAnual()) {
			sqlStr.append(" and ( ht.anovigente = '").append(ano).append("') ");
		}
//		sqlStr.append(" and de.disciplina = ").append(codDisciplina).append(" ");
		sqlStr.append(" and ( (exists ( select 1 from disciplinaequivalente d2 ");
		sqlStr.append(" where d2.equivalente = htdi.disciplina)) ");
		sqlStr.append(" or exists (	select 1 from disciplinaequivalente d2 ");
		sqlStr.append(" where d2.disciplina = htdi.disciplina ");
		sqlStr.append(" and (d2.equivalente = ").append(codDisciplina).append(" )))");
		sqlStr.append(" 	group by t.codigo");
		sqlStr.append(" having bool_and(htd.data is not null);");
		rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			montarDadosHorarioAulaTurmaEDisciplina(rs, turmaVO, true);
			if (turmaVO.getDataPrimeiraAula() != null) {
				return;
			}
		}
		// Busca horario na turma agrupada considerando disciplina equivalente considerando subturma
		sqlStr = new StringBuilder("");
		sqlStr.append(" select min(htd.data) as dataInicio, max(htd.data) as dataTermino, max(case when sala.codigo is null then 0 else sala.codigo end) as sala, min(htdi.professor) as professor, t.codigo as codigo,  t.identificadorturma as identificadorTurma ");
		sqlStr.append(" from horarioturma  ht ");
		sqlStr.append(" inner join turma as t on t.codigo = ht.turma");
		sqlStr.append(" inner join turmaagrupada on turmaagrupada.turmaorigem = t.codigo	");
		sqlStr.append(" inner join horarioturmadia htd on htd.horarioturma = ht.codigo");
		sqlStr.append(" inner join horarioturmadiaitem htdi on htd.codigo = htdi.horarioturmadia					");
		sqlStr.append(" 	left join salalocalaula as sala on sala.codigo = htdi.sala ");
		sqlStr.append(" where  turmaagrupada.turma ");
		sqlStr.append(" in ( select tsub.codigo from turma as tsub where tsub.subturma = true and tsub.tiposubturma in ('PRATICA', 'TEORICA') ");
		sqlStr.append(" 	and tsub.turmaprincipal =  ").append(turmaVO.getCodigo());
		sqlStr.append(" ) ");
		if (Uteis.isAtributoPreenchido(situacaoTurma)) {
			sqlStr.append(" 					and t.situacao = 'AB'			");
		}
		if (turmaVO.getSemestral()) {
			sqlStr.append(" 	and (ht.anovigente = '").append(ano).append("'");
			sqlStr.append(" 	and ht.semestrevigente = '").append(semestre).append("')");
		} else if (turmaVO.getAnual()) {
			sqlStr.append(" and ( ht.anovigente = '").append(ano).append("') ");
		}
		sqlStr.append(" and htdi.disciplina = ").append(codDisciplina).append(" ");
		sqlStr.append(" 	group by t.codigo");
		sqlStr.append(" having bool_and(htd.data is not null);");
		rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			montarDadosHorarioAulaTurmaEDisciplina(rs, turmaVO, true);
			if (turmaVO.getDataPrimeiraAula() != null) {
				return;
			}
		}

		// Busca horario na tutoria online
		if (trazerSomenteTurmaComTutoriaOnline) {
			sqlStr = new StringBuilder("");
			sqlStr.append(" 	select datainicioaula as dataInicio, dataterminoaula as dataTermino, -1 as sala, '' as professor");
			sqlStr.append(" 	from programacaotutoriaonline ");
			sqlStr.append(" 	where programacaotutoriaonline.definirperiodoaulaonline is true and  programacaotutoriaonline.turma = ").append(turmaVO.getCodigo());
			if (turmaVO.getSemestral()) {
				sqlStr.append(" 	and (programacaotutoriaonline.ano = '").append(ano).append("'");
				sqlStr.append(" 	and programacaotutoriaonline.semestre = '").append(semestre).append("')");
			} else if (turmaVO.getAnual()) {
				sqlStr.append(" and (programacaotutoriaonline.ano = '").append(ano).append("') ");
			}

			sqlStr.append(" 	and programacaotutoriaonline.disciplina = ").append(codDisciplina).append(" ");
		}

		rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			montarDadosHorarioAulaTurmaEDisciplina(rs, turmaVO, false);
			if (turmaVO.getDataPrimeiraAula() != null) {
				return;
			}
		}

	}

	public void montarDadosHorarioAulaTurmaEDisciplina(SqlRowSet rs, TurmaVO turmaVO, Boolean turmaAgrupada) throws Exception {
		if (turmaAgrupada) {
			if (rs.getObject("codigo") != null && rs.getObject("codigo") instanceof Integer) {
				turmaVO.setCodigoTurmaBase(rs.getInt("codigo"));
			}
			if (rs.getString("identificadorturma") != null) {
				turmaVO.setIdentificadorTurmaBase(rs.getString("identificadorturma"));
			}
		}
		turmaVO.setDataPrimeiraAula(rs.getDate("dataInicio"));
		turmaVO.setDataPrimeiraAulaProgramada(Uteis.getData(rs.getDate("dataInicio")));
		turmaVO.setDataUltimaAula(rs.getDate("dataTermino"));

//		turmaVO.getSalaLocalAulaVO().setCodigo(rs.getInt("sala"));
//
//		if (rs.getObject("sala") != null && rs.getObject("sala") instanceof Integer && rs.getInt("sala") == -1) {
//			turmaVO.getSalaLocalAulaVO().setSala("Online");
//			turmaVO.getSalaLocalAulaVO().getLocalAula().setLocal("Ambiente Virtual de Aprendizagem");
//		}
//		if (Uteis.isAtributoPreenchido(turmaVO.getSalaLocalAulaVO().getCodigo()) && turmaVO.getSalaLocalAulaVO().getCodigo() > 0) {
//			turmaVO.setSalaLocalAulaVO(getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(turmaVO.getSalaLocalAulaVO().getCodigo()));
//		}
		if (Uteis.isAtributoPreenchido(turmaVO.getProfessor())) {
			turmaVO.setProfessor(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(turmaVO.getProfessor().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null));
		}
	}

	public List<TurmaVO> consultarPorIdentificadorTurmaUnidadeEnsinoCondicaoRenegociacao(String valorConsulta,  Integer curso, Integer turno, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append("WHERE sem_acentos(lower (turma.identificadorturma)) like(sem_acentos('").append(valorConsulta.toLowerCase()).append("%'))  ");
		// if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
		// sql.append(" AND turma.unidadeEnsino = " + unidadeEnsino);
		// }
//		if (!listaCondicaoUnidadeEnsinoVOs.isEmpty()) {
//			sql.append(" AND ").append("turma.unidadeEnsino in (");
////			for (CondicaoRenegociacaoUnidadeEnsinoVO condicaoUnidadeEnsinoVO : listaCondicaoUnidadeEnsinoVOs) {
////				if (condicaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getFiltrarUnidadeEnsino()) {
////					sql.append(condicaoUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo()).append(", ");
////				}
////			}
//			sql.append("0) ");
//		}
		if (curso != null && curso.intValue() != 0) {
			sql.append(" AND turma.curso = " + curso);
		}
		if (turno != null && turno.intValue() != 0) {
			sql.append(" AND turma.turno = " + turno);
		}
		sql.append(" ORDER BY  turma.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}

	public List<TurmaVO> consultarTurmaPorCursoVOsEUnidadeEnsinoVOs(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder(getSQLPadraoConsultaBasica());
		sb.append("WHERE 1=1");
		if (!unidadeEnsinoVOs.isEmpty()) {
			sb.append(" and turma.unidadeensino in (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (x > 0) {
					sb.append(", ");
				}
				sb.append(unidadeEnsinoVO.getCodigo());
				x++;
			}
			sb.append(" ) ");
		}

		if (!cursoVOs.isEmpty()) {
			sb.append(" and turma.curso in (");
			int x = 0;
			for (CursoVO cursoVO : cursoVOs) {
				if (x > 0) {
					sb.append(", ");
				}
				sb.append(cursoVO.getCodigo());
				x++;
			}
			sb.append(" ) ");
		}
		sb.append(" order by turma.identificadorturma");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsultaBasica(tabelaResultado);

	}
	
	public Integer consultaRapidaTotalRegistroPorUnidadeEnsinoCurso(String valorConsulta, Integer curso, Integer unidadeEnsino) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(distinct Turma.codigo) as qtde ");
		sql.append("from turma ");
		sql.append("left join unidadeensino on turma.unidadeensino = unidadeensino.codigo ");
		sql.append(" WHERE sem_acentos(turma.identificadorTurma) ilike (sem_acentos(?))");
		if (curso != 0) {
			sql.append(" AND (turma.curso = ").append(curso).append(" OR (turma.turmaagrupada and exists (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma and turmaagrupada.turmaorigem = turma.codigo and t.curso = ").append(curso).append(" limit 1 ))) ");
		}
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and turma.unidadeEnsino = ");
			sql.append(unidadeEnsino.intValue());
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta+"%" );
		if(tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}
	
	public List<TurmaVO> montarDadosTurmaDiarioCoordenador(SqlRowSet tabelaResultado) throws Exception {
		List<TurmaVO> vetResultado = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			montarDadosBasicoTurmaVisaoCoordenador(obj, tabelaResultado);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	private void montarDadosBasicoTurmaVisaoCoordenador(TurmaVO obj, SqlRowSet dadosSQL) throws Exception {
		obj.setNivelMontarDados(NivelMontarDados.BASICO);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setSemestral(dadosSQL.getBoolean("semestral"));
		obj.setAnual(dadosSQL.getBoolean("anual"));
		obj.setNrVagas(dadosSQL.getInt("nrvagas"));
		obj.setNrMaximoMatricula(dadosSQL.getInt("nrmaximomatricula"));
		obj.setNrMinimoMatricula(dadosSQL.getInt("nrminimomatricula"));
		obj.setIdentificadorTurma(dadosSQL.getString("identificadorturma"));
		obj.setTurmaAgrupada(dadosSQL.getBoolean("turmaagrupada"));
		obj.setSala(dadosSQL.getString("sala"));
		obj.setConsiderarTurmaAvaliacaoInstitucional(dadosSQL.getBoolean("considerarTurmaAvaliacaoInstitucional"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.getGradeCurricularVO().setCodigo(dadosSQL.getInt("gradeCurricular"));
		obj.getGradeCurricularVO().setNome(dadosSQL.getString("gradeCurricular.nome"));
		obj.setAbreviaturaCurso(dadosSQL.getString("abreviaturaCurso"));
		obj.setNrVagasInclusaoReposicao(dadosSQL.getInt("nrVagasInclusaoReposicao"));
//		obj.getChancelaVO().setCodigo(dadosSQL.getInt("chancela"));
//		obj.getChancelaVO().setInstituicaoChanceladora(dadosSQL.getString("instituicaoChanceladora"));
	}
	
	
	public void getSQLPadraoJoinCursoTurma(String join, String turma, String curso, StringBuilder stringBuilder) {
		stringBuilder.append(" ").append(join).append(" JOIN ").append(curso).append(" ON ((").append(turma).append(".turmaagrupada = false AND ")
			.append(curso).append(".codigo = ").append(turma).append(".curso) OR (").append(turma).append(".turmaagrupada = false AND ")
			.append(curso).append(".codigo IN (select DISTINCT t.curso from turma t where t.codigo =").append(turma).append(".turmaprincipal)) ")
			.append(" OR (").append(turma).append(".turmaAgrupada AND ").append(curso)
			.append(".codigo IN (select DISTINCT t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = ")
			.append(turma).append(".codigo))) ");
	}

	@Override
	public TurmaVO clonar(TurmaVO turmaVO, UsuarioVO usuarioLogado) throws Exception {
		TurmaVO turmaVO2 = turmaVO.clonar(); 
		turmaVO2.setIdentificadorTurma("RE"+turmaVO.getCurso().getAbreviatura()+turmaVO.getUnidadeEnsino().getAbreviatura());
		turmaVO2.setCodigo(0);
		turmaVO2.setNovoObj(true);
//		turmaVO2.setCentroResultadoVO(new CentroResultadoVO());
		
		
//		if(turmaVO.getTurmaUnidadeEnsino().getRegraDefinicaoVagas().equals("VM")) {
//				VagaTurmaVO  vagaTurma = new VagaTurmaVO();
//				if(!turmaVO.getCurso().getPeriodicidade().equals("IN")) {
//					vagaTurma.setAno(String.valueOf(turmaVO.getTurmaUnidadeEnsino().getAno()));
//				}
//				if(turmaVO.getSemestral()) {
//					vagaTurma.setSemestre(String.valueOf(turmaVO.getTurmaUnidadeEnsino().getSemestre()));
//				}
//				vagaTurma.setNovoObj(true);
//				vagaTurma.setTurmaVO(turmaVO2);
////				List<VagaTurmaDisciplinaVO> vagaTurmaDisciplinaVOs = getFacadeFactory().getVagaTurmaDisciplinaFacade().consultaRapidaPorTurma(turmaVO.getCodigo(), turmaVO.getCodigo(), usuarioLogado);
//				for(VagaTurmaDisciplinaVO obj: vagaTurmaDisciplinaVOs){
//					incluirNrVagas(obj, turmaVO.getTurmaUnidadeEnsino());
//					vagaTurma.getVagaTurmaDisciplinaVOs().add(obj);
//				}
//				turmaVO2.setVagaTurmaVO(vagaTurma);
//		}else {
////			VagaTurmaVO vagaTurma = getFacadeFactory().getVagaTurmaFacade().consultaRapidaUnicidadeTurmaPorIdentificador(turmaVO.getCodigo(), "", "", 0);
//			if(Uteis.isAtributoPreenchido(vagaTurma.getCodigo())) {
////				vagaTurma = getFacadeFactory().getVagaTurmaFacade().consultarPorChavePrimaria(vagaTurma.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
//				vagaTurma.setCodigo(0);
//				vagaTurma.setNovoObj(true);
//				turmaVO2.setVagaTurmaVO(vagaTurma);
//				turmaVO2.getVagaTurmaVO().getVagaTurmaDisciplinaVOs().forEach(vagaTurmaDisciplina -> zerarVagaTurmaDisciplina(vagaTurmaDisciplina));
//			}
//		}
		
		
		if(Uteis.isAtributoPreenchido(turmaVO2.getTurmaDisciplinaVOs())) {
			turmaVO2.getTurmaDisciplinaVOs().stream().forEach(turmaDisciplina -> zerarCodigoTurmaDiscliplina(turmaDisciplina));
		}
		if(Uteis.isAtributoPreenchido(turmaVO2.getTurmaAgrupadaVOs())) {
			turmaVO2.getTurmaAgrupadaVOs().stream().forEach(turmaAgrupada -> zerarCodigoTurmaAgrupada(turmaAgrupada));
		}
		if(Uteis.isAtributoPreenchido(turmaVO2.getTurmaAberturaVOs())) {
			turmaVO2.getTurmaAberturaVOs().stream().forEach(turmaAberta -> zerarCodigoTurmaAberta(turmaAberta));
		}
		if(Uteis.isAtributoPreenchido(turmaVO2.getTurmaContratoVOs())) {
			turmaVO2.getTurmaContratoVOs().stream().forEach(turmaContrato -> zerarCodigoTurmaContrato(turmaContrato));
		}
		if(Uteis.isAtributoPreenchido(turmaVO2.getTurmaDisciplinaInclusaoSugeridaVOs())) {
			turmaVO2.getTurmaDisciplinaInclusaoSugeridaVOs().forEach(turmaDisciplinasSugeridas -> zerarCodigoTurmaDisciplinaSugeridas(turmaDisciplinasSugeridas));
		}
		return turmaVO2;
	}

	private void zerarVagaTurmaDisciplina(VagaTurmaDisciplinaVO vagaTurmaDisciplina) {
		vagaTurmaDisciplina.setCodigo(0);
		vagaTurmaDisciplina.setNovoObj(true);
		vagaTurmaDisciplina.setVagaTurma(0);
	}

	private void zerarCodigoTurmaDisciplinaSugeridas(TurmaDisciplinaInclusaoSugeridaVO turmaDisciplinasSugeridas) {
		turmaDisciplinasSugeridas.setCodigo(0);
		turmaDisciplinasSugeridas.setNovoObj(true);
	}

	private void zerarCodigoTurmaContrato(TurmaContratoVO turmaContrato) {
		turmaContrato.setCodigo(0);
		turmaContrato.setNovoObj(true);
	}

	private void zerarCodigoTurmaAberta(TurmaAberturaVO turmaAberta) {
		turmaAberta.setCodigo(0);
		turmaAberta.setNovoObj(true);
	}

	private void zerarCodigoTurmaAgrupada(TurmaAgrupadaVO turmaAgrupada) {
		turmaAgrupada.setCodigo(0);
		turmaAgrupada.setNovoObj(true);
	}

	private void zerarCodigoTurmaDiscliplina(TurmaDisciplinaVO turmaDisciplina) {
		turmaDisciplina.setCodigo(0);
		turmaDisciplina.setNovoObj(true);
	}

	@Override
	public void inicializarDadosUnidadeEnsinoSelecionada(TurmaVO turmaVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs,UsuarioVO usuarioVO) {
		turmaVO.getListaTurmaUnidadeEnsino().clear();
		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				TurmaUnidadeEnsinoVO turmaUnidadeEnsino = new TurmaUnidadeEnsinoVO();
				turmaUnidadeEnsino.setUnidadeEnsinoVO(unidadeEnsinoVO);
				if(turmaVO.getUnidadeEnsino().getCodigo().equals(unidadeEnsinoVO.getCodigo())) {
					turmaUnidadeEnsino.getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(false);
				}else {
					turmaUnidadeEnsino.getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(true);
				}
				turmaUnidadeEnsino.setCategoriaCondicaoPagamento(turmaVO.getCategoriaCondicaoPagamento());
//				turmaUnidadeEnsino.setPlanoFinanceiroCurso(turmaVO.getPlanoFinanceiroCurso());
				turmaUnidadeEnsino.setIdentificadorTurma("RE"+turmaVO.getCurso().getAbreviatura()+unidadeEnsinoVO.getAbreviatura());
				turmaVO.getListaTurmaUnidadeEnsino().add(turmaUnidadeEnsino);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirTurmaClone(TurmaVO turmaVO, UsuarioVO usuarioLogado) throws Exception {
		Integer codigoTurmaAgrupada =  turmaVO.getTurmaUnidadeEnsino().getTurmaAgrupadaVO().getCodigo();
		try {
		validarDadosTurmaClone(turmaVO);
		vincularDadosTurmaBase(turmaVO, usuarioLogado);
		for(TurmaUnidadeEnsinoVO turmaUnidadeEnsinoVO : turmaVO.getListaTurmaUnidadeEnsino()) {
			if(turmaUnidadeEnsinoVO.getUnidadeEnsinoVO().getFiltrarUnidadeEnsino()) {
				TurmaVO turmaVO2 = clonar(turmaVO, usuarioLogado);
				turmaVO2.setUnidadeEnsino(turmaUnidadeEnsinoVO.getUnidadeEnsinoVO());
//				turmaVO2.setPlanoFinanceiroCurso(turmaUnidadeEnsinoVO.getPlanoFinanceiroCurso());
				turmaVO2.setCategoriaCondicaoPagamento(turmaUnidadeEnsinoVO.getCategoriaCondicaoPagamento());
				turmaVO2.setIdentificadorTurma(turmaUnidadeEnsinoVO.getIdentificadorTurma().trim());
//				turmaVO2.setChancelaVO(turmaUnidadeEnsinoVO.getUnidadeEnsinoVO().getChancelaVO());
				turmaVO2.setTipoChancela(turmaUnidadeEnsinoVO.getUnidadeEnsinoVO().getTipoChancela());
				turmaVO2.setPorcentagemChancela(turmaUnidadeEnsinoVO.getUnidadeEnsinoVO().getPorcentagemChancela());
				turmaVO2.setValorFixoChancela(turmaUnidadeEnsinoVO.getUnidadeEnsinoVO().getValorFixoChancela());
				incluir(turmaVO2, usuarioLogado);
				getFacadeFactory().getTurmaAtualizacaoDisciplinaLogFacade().executarLogAtualizacaoDisciplinaTurma(turmaVO2, turmaVO2.getTurmaDisciplinaVOs(),usuarioLogado);
				
				if(turmaVO.getTurmaUnidadeEnsino().getVincularTurmasCloneNaTurmaAgrupada() && !turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
					if(Uteis.isAtributoPreenchido(turmaVO.getTurmaUnidadeEnsino().getTurmaAgrupadaVO().getCodigo())) {
						TurmaAgrupadaVO turmaAgrupadaVO = new TurmaAgrupadaVO();
						turmaAgrupadaVO.setTurmaOrigem(turmaVO.getTurmaUnidadeEnsino().getTurmaAgrupadaVO().getTurmaOrigem());
						turmaAgrupadaVO.setTurma(turmaVO2);
						getFacadeFactory().getTurmaAgrupadaFacade().incluir(turmaAgrupadaVO);
					}
				}
			}
		}
		}catch (Exception e) {
			turmaVO.getTurmaUnidadeEnsino().getTurmaAgrupadaVO().setCodigo(codigoTurmaAgrupada);
			throw e;
		}
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void vincularDadosTurmaBase(TurmaVO turmaVO, UsuarioVO usuarioLogado) throws Exception {
		if(Uteis.isAtributoPreenchido(turmaVO.getTurmaUnidadeEnsino().getTurmaAberturaVO().getSituacao())) {
			turmaVO.getTurmaUnidadeEnsino().getTurmaAberturaVO().setUsuario(usuarioLogado);
			turmaVO.getTurmaAberturaVOs().add(turmaVO.getTurmaUnidadeEnsino().getTurmaAberturaVO());
			alterar(turmaVO, usuarioLogado);
		}
		if(turmaVO.getTurmaUnidadeEnsino().getVincularTurmasCloneNaTurmaAgrupada() && !Uteis.isAtributoPreenchido(turmaVO.getTurmaUnidadeEnsino().getTurmaAgrupadaVO().getCodigo())) {
			TurmaVO turma = montarDadosTurmaAgrupadaClone(turmaVO);
			incluir(turma, usuarioLogado);
			turmaVO.getTurmaUnidadeEnsino().setTurmaAgrupadaVO(turma.getTurmaAgrupadaVOs().iterator().next());
		}
		
//		if(turmaVO.getTurmaUnidadeEnsino().getRegraDefinicaoVagas().equals("VM")) {
//			VagaTurmaVO  vagaTurma = new VagaTurmaVO();
//			vagaTurma = getFacadeFactory().getVagaTurmaFacade().consultaRapidaUnicidadeTurmaPorIdentificador(turmaVO.getCodigo(), "", "", 0);
//			if(!Uteis.isAtributoPreenchido(vagaTurma.getCodigo())) {
//				if(!turmaVO.getCurso().getPeriodicidade().equals("IN")) {
//					vagaTurma.setAno(String.valueOf(turmaVO.getTurmaUnidadeEnsino().getAno()));
//				}
//				if(turmaVO.getSemestral()) {
//					vagaTurma.setSemestre(String.valueOf(turmaVO.getTurmaUnidadeEnsino().getSemestre()));
//				}
//				vagaTurma.setNovoObj(true);
//				vagaTurma.setTurmaVO(turmaVO);
//				List<VagaTurmaDisciplinaVO> vagaTurmaDisciplinaVOs = getFacadeFactory().getVagaTurmaDisciplinaFacade().consultaRapidaPorTurma(turmaVO.getCodigo(), turmaVO.getCodigo(), usuarioLogado);
//				for(VagaTurmaDisciplinaVO obj: vagaTurmaDisciplinaVOs){
//					incluirNrVagas(obj, turmaVO.getTurmaUnidadeEnsino());
//					vagaTurma.getVagaTurmaDisciplinaVOs().add(obj);
//				}
//				getFacadeFactory().getVagaTurmaFacade().persistir(vagaTurma, usuarioLogado);
//			}	
//		}
	}
	
	private void incluirNrVagas(VagaTurmaDisciplinaVO vagaTurmaDisciplina, TurmaUnidadeEnsinoVO turmaUnidadeEnsino) {
		vagaTurmaDisciplina.setNrVagasMatricula(turmaUnidadeEnsino.getNrVagas());
		vagaTurmaDisciplina.setNrMaximoMatricula(turmaUnidadeEnsino.getNrVagasMaxima());
		vagaTurmaDisciplina.setNrVagasMatriculaReposicao(turmaUnidadeEnsino.getNrVagasReposicao());
	}

	private TurmaVO montarDadosTurmaAgrupadaClone(TurmaVO turmaVO) {
		TurmaVO turma = new TurmaVO();
		turma.setIdentificadorTurma(turmaVO.getTurmaUnidadeEnsino().getTurmaAgrupadaVO().getTurma().getIdentificadorTurma());
		turma.setUnidadeEnsino(turmaVO.getTurmaUnidadeEnsino().getTurmaAgrupadaVO().getTurma().getUnidadeEnsino());
		turma.setTurmaAgrupada(true);
		turma.setValidarDadosTurmaAgrupada(false);
		turma.setTurno(turmaVO.getTurno());
		turma.setSituacao(turmaVO.getSituacao());
		turma.setTurmaDisciplinaVOs(turmaVO.getTurmaDisciplinaVOs());
		TurmaAgrupadaVO turmaAgrupada = new TurmaAgrupadaVO();
		turmaAgrupada.setTurma(turmaVO);
		turma.getTurmaAgrupadaVOs().add(turmaAgrupada);
		return turma;
	}

	private void validarDadosTurmaClone(TurmaVO turmaVO) throws Exception {
		if(turmaVO.getTurmaUnidadeEnsino().getRegraDefinicaoVagas().equals("VC")) {
//			VagaTurmaVO vagaTurma = getFacadeFactory().getVagaTurmaFacade().consultaRapidaUnicidadeTurmaPorIdentificador(turmaVO.getCodigo(), "", "", 0);
//			if(!Uteis.isAtributoPreenchido(vagaTurma.getCodigo())) {
//				throw new Exception("Não foi encontrado um controle de Vagas para a turma clonada, sendo assim informe as vagas manualmente ou cadastre o controle de vagas para a turma clonada.");
//			}
		}
		if(turmaVO.getTurmaUnidadeEnsino().getRegraDefinicaoVagas().equals("VM")) {
			if(!Uteis.isAtributoPreenchido(turmaVO.getTurmaUnidadeEnsino().getNrVagas())) {
				throw new Exception("Campo Nrº de Vagas deve ser Informado.");
			}
			if(!Uteis.isAtributoPreenchido(turmaVO.getTurmaUnidadeEnsino().getNrVagasMaxima())) {
				throw new Exception("Campo Nrº de Vagas Máxima deve ser Informado.");
			}
			if(!Uteis.isAtributoPreenchido(turmaVO.getTurmaUnidadeEnsino().getNrVagasReposicao())) {
				throw new Exception("Campo Nrº Vagas Inclusão/Reposição deve ser Informado.");
			}
			if(!turmaVO.getPeriodicidade().equals("IN") && !Uteis.isAtributoPreenchido(turmaVO.getTurmaUnidadeEnsino().getAno())) {
				throw new Exception("Campo Ano deve ser Informado.");
			}
			
			if(turmaVO.getSemestral() && !Uteis.isAtributoPreenchido(turmaVO.getTurmaUnidadeEnsino().getSemestre())) {
				throw new Exception("Campo Semestre deve ser Informado.");
			}
		}
		if(turmaVO.getTurmaUnidadeEnsino().getRegistrarAberturaConfirmacao()) {
			if(!Uteis.isAtributoPreenchido(turmaVO.getTurmaUnidadeEnsino().getTurmaAberturaVO().getSituacao())) {
				throw new Exception("Campo Situação deve ser Informado.");
			}
			if(!Uteis.isAtributoPreenchido(turmaVO.getTurmaUnidadeEnsino().getTurmaAberturaVO().getData())) {
				throw new Exception("Campo Data deve ser Informado.");
			}
		}
		if(turmaVO.getTurmaUnidadeEnsino().getVincularTurmasCloneNaTurmaAgrupada()) {
			if(!Uteis.isAtributoPreenchido(turmaVO.getTurmaUnidadeEnsino().getTurmaAgrupadaVO().getTurma().getUnidadeEnsino().getCodigo())) {
				throw new Exception("Campo Unidade Ensino deve ser Informado.");
			}
			if(!Uteis.isAtributoPreenchido(turmaVO.getTurmaUnidadeEnsino().getTurmaAgrupadaVO().getTurma().getIdentificadorTurma())) {
				throw new Exception("Campo Identificador Turma deve ser informado.");
			}
			
		}
		
	}
	
	@Override
	public void consultarTurmaBaseVinculadaTurmaBase(TurmaVO turmaVO, UsuarioVO usuarioVO) throws Exception {
		List<TurmaAgrupadaVO> listaTurmaAgrupada = getFacadeFactory().getTurmaAgrupadaFacade().consultarPorTurma(turmaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		if(Uteis.isAtributoPreenchido(listaTurmaAgrupada)) {
			TurmaAgrupadaVO turmaAgrupadaVO = listaTurmaAgrupada.stream().max(Comparator.comparingInt(TurmaAgrupadaVO::getCodigo)).get();
			TurmaVO turma = consultarPorChavePrimariaTurmaAgrupada(turmaAgrupadaVO.getTurmaOrigem(), usuarioVO);
			turmaAgrupadaVO.setTurma(turma);
			turmaVO.getTurmaUnidadeEnsino().setTurmaAgrupadaVO(turmaAgrupadaVO);
		}
	}
	
	public TurmaVO consultarPorChavePrimariaTurmaAgrupada(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT unidadeensino.codigo as unidadeensino_codigo , unidadeensino.nome as unidadeensino_nome, Turma.* FROM Turma inner join unidadeensino on Turma.unidadeensino = unidadeensino.codigo  WHERE Turma.codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		TurmaVO obj = new TurmaVO();
		if (tabelaResultado.next()) {
			obj.setNovoObj(Boolean.FALSE);
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
			obj.setAnual(tabelaResultado.getBoolean("anual"));
			obj.setSemestral(tabelaResultado.getBoolean("semestral"));
			obj.getCurso().setCodigo(tabelaResultado.getInt("curso"));
			obj.getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeensino_codigo"));
			obj.getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeensino_nome"));
		}
		return obj;
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirAlteracaoUnidadeEnsinoTurma(TurmaVO turmaVO,UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuarioVO, Boolean permitirAgruparTurmasUnidadeEnsinoDiferente) throws Exception {
		if (!turmaVO.getTurmaAgrupada()) {
			this.validarDadosAlteracaoUnidadeEnsinoTurma(turmaVO, unidadeEnsinoVO);
			this.alterarUnidadeEnsinoturma(turmaVO, unidadeEnsinoVO, usuarioVO);
			getFacadeFactory().getMatriculaPeriodoFacade().alterarUnidadeEnsinoCursoPorTurma(turmaVO,usuarioVO);
			getFacadeFactory().getMatriculaFacade().alterarUnidadeEnsinoTurmaBase(turmaVO, false, usuarioVO);
			getFacadeFactory().getRequerimentoFacade().alterarUnidadeEnsinoTurmaBaseMatriculaPeriodo(turmaVO, false, usuarioVO);
			this.alterarUnidadeEnsinoNotificacaoRegistroAulaNota(turmaVO,usuarioVO);
//			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().alterarUnidadeEnsinoCentroResultadoOrigem(turmaVO, false, usuarioVO);
//			getFacadeFactory().getContaReceberFacade().alterarUnidadeEnsino(turmaVO, false, usuarioVO);
//			getFacadeFactory().getContaReceberRegistroArquivoFacade().alterarUnidadeEnsino(turmaVO, false, usuarioVO);
//			getFacadeFactory().getNegociacaoRecebimentoFacade().alterarUnidadeEnsino(turmaVO, false, usuarioVO);
			getFacadeFactory().getMatriculaPeriodoFacade().alterarProcessoMatriculaAlteracaoUnidadeEnsinoTurma(turmaVO, false, usuarioVO);
			this.incluirUnidadeEnsinoContaCorrenteAlteracaoUnidadeTurma(turmaVO, usuarioVO);
		} else if (turmaVO.getTurmaAgrupada() && permitirAgruparTurmasUnidadeEnsinoDiferente){
			this.alterarUnidadeEnsinoturma(turmaVO, unidadeEnsinoVO, usuarioVO);
		} else {
			throw new Exception("O Usuário Não Possui Permissão Para Alterar a Unidade de Ensino Desta Turma (Permitir Agrupar Turmas de Unidade de Ensino Diferente)");
		}
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarUnidadeEnsinoturma(TurmaVO turmaVO,UnidadeEnsinoVO unidadeEnsinoVO,UsuarioVO usuario) {
		 final String sqlStr = "UPDATE turma SET unidadeensino = ? WHERE turma.codigo = ?;"  + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());
				int i = 0;
				Uteis.setValuePreparedStatement(unidadeEnsinoVO.getCodigo(), ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(turmaVO.getCodigo(), ++i, sqlAlterar);
				return sqlAlterar;
			}
		});

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarUnidadeEnsinoNotificacaoRegistroAulaNota (TurmaVO turmaVO,  UsuarioVO usuarioVO) throws Exception {
		 final StringBuilder sqlStr = new StringBuilder();
		 sqlStr.append("UPDATE notificacaoregistroaulanota SET unidadeensino = t.unidadeensino FROM ( ");
		 sqlStr.append("SELECT ");
		 sqlStr.append("  notificacaoregistroaulanota.codigo as notificacaoregistroaulanota,turma.unidadeensino  ");
		 sqlStr.append("FROM notificacaoregistroaulanota ");
		 sqlStr.append(" INNER JOIN turma 		ON notificacaoregistroaulanota.turma = turma.codigo ");
		 sqlStr.append("WHERE turma.codigo = ? ");
		 sqlStr.append(") AS t WHERE notificacaoregistroaulanota.codigo = t.notificacaoregistroaulanota AND notificacaoregistroaulanota.unidadeensino <> t.unidadeensino; ");
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
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirUnidadeEnsinoContaCorrenteAlteracaoUnidadeTurma (TurmaVO turmaVO,  UsuarioVO usuarioVO) throws Exception{
		 final StringBuilder sqlStr = new StringBuilder();
		 sqlStr.append("INSERT INTO unidadeensinocontacorrente (unidadeensino,contacorrente ) ");
		 sqlStr.append("SELECT DISTINCT turma.unidadeensino,contareceber.contacorrente  ");
		 sqlStr.append("FROM contareceber   ");
		 sqlStr.append(" INNER JOIN turma ON contareceber.turma = turma.codigo ");
		 sqlStr.append("WHERE turma.codigo = ? ");
		 sqlStr.append("AND NOT EXISTS (SELECT 1 FROM unidadeensinocontacorrente WHERE unidadeensinocontacorrente.unidadeensino = contareceber.unidadeensino AND contareceber.contacorrente =  unidadeensinocontacorrente.contacorrente); ");
		 sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		 getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlIncluir = arg0.prepareStatement(sqlStr.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(turmaVO.getCodigo(), ++i, sqlIncluir);
					return sqlIncluir;
				}
			});
	}
	
	public void validarDadosAlteracaoUnidadeEnsinoTurma(TurmaVO turmaVO,UnidadeEnsinoVO unidadeEnsinoVO) throws Exception {
		if (!getFacadeFactory().getUnidadeEnsinoCursoFacade().verificarUnidadeEnsinoCursoExistente(unidadeEnsinoVO.getCodigo(), turmaVO.getTurno().getCodigo(), turmaVO.getCurso().getCodigo())) {
			throw new Exception( "A Unidade de Ensino " + unidadeEnsinoVO.getNome() + " não está vinculado ao curso " + turmaVO.getCurso().getNome()+ ".Faça a inclusão da UNIDADE DE ENSINO no cadastro de CURSO.");
		}
//	   if (!getFacadeFactory().getProcessoMatriculaCalendarioFacade().verificarProcessoMatriculaCalendarioExistenteUnidadeEnsinoCurso(unidadeEnsinoVO.getCodigo(), turmaVO.getTurno().getCodigo(), turmaVO.getCurso().getCodigo())) { 
//		   throw new Exception( "Não Foi encontrado um processo de matrícula para a Unidade de Ensino, Curso e Turno. Realize a inclusão deste processo de matrícula e depois realize a alteração da unidade de ensino na turma.");
//	    }
	}
	
	@Override
	public List<TurmaVO> consultarTurmaPorGradeDisciplina(Integer gradeDisciplina, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct turma.codigo, turma.identificadorTurma from turma ");
		sb.append(" inner join turmaDisciplina on turmaDisciplina.turma = turma.codigo ");
		sb.append(" where turmaDisciplina.gradedisciplina = ? ");
		sb.append(" order by turma.identificadorTurma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), new Object[] {gradeDisciplina});
		List<TurmaVO> listaTurmaVOs = new ArrayList<TurmaVO>(0);
		while (tabelaResultado.next()) {
			TurmaVO obj = new TurmaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
			listaTurmaVOs.add(obj);
		}
		return listaTurmaVOs;
	}
	
	@Override
	public void montarListaApresentacaoTurmaAgrupada(Map<Integer, List<TurmaVO>> mapTurmas, List<TurmaVO> turmaVOs, List<TurmaVO> listaTurmaIncluir, UsuarioVO usuarioVO) throws Exception {
		try {
			Iterator i = turmaVOs.iterator();
			while (i.hasNext()) {
				TurmaVO turmaVO = (TurmaVO) i.next();
				if (Uteis.isAtributoPreenchido(turmaVO.getIdentificadorTurmaBase())) {
					if (!mapTurmas.containsKey(turmaVO.getCodigoTurmaBase())) {
						TurmaVO turmaIncluir = consultarPorChavePrimaria(turmaVO.getCodigoTurmaBase(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
						turmaIncluir.setDataPrimeiraAula(turmaVO.getDataPrimeiraAula());
						turmaIncluir.setDataPrimeiraAulaProgramada(turmaVO.getDataPrimeiraAulaProgramada());
						turmaIncluir.setDataUltimaAula(turmaVO.getDataUltimaAula());
//						turmaIncluir.setSalaLocalAulaVO(turmaVO.getSalaLocalAulaVO());
						turmaIncluir.setProfessor(turmaVO.getProfessor());
						listaTurmaIncluir.add(turmaIncluir);
					}
					List<TurmaVO> turmas = mapTurmas.get(turmaVO.getCodigoTurmaBase());
				    if (turmas == null) {
				    	turmas = new ArrayList<>();
				    	mapTurmas.put(turmaVO.getCodigoTurmaBase(), turmas);
				    }
				    turmas.add(turmaVO);
				    mapTurmas.put(turmaVO.getCodigoTurmaBase(), turmas);
				} else {
					listaTurmaIncluir.add(turmaVO);
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		
	}

	@Override
	public List<TurmaVO> consultarPorUnidadeEnsinoCurso(String campoConsulta, String valorConsulta, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuarioVO);
		StringBuilder sqlStr = getSQLPadraoConsultaBasica();
		sqlStr.append("WHERE 1=1 ");
		if (campoConsulta.equals("identificadorTurma")) {
			sqlStr.append("AND lower (sem_acentos(turma.identificadorTurma)) like (sem_acentos('");
			sqlStr.append(valorConsulta.toLowerCase());
			sqlStr.append("%')) ");
		}
		if (curso != null && curso != 0) {
			sqlStr.append(" AND curso.codigo = ").append(curso).append(" ");
		}
		if (!unidadeEnsinoVOs.isEmpty() && unidadeEnsinoVOs.stream().anyMatch(p-> p.getFiltrarUnidadeEnsino())) {
			sqlStr.append(" and unidadeensino.codigo in (");
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
		sqlStr.append("ORDER BY  turma.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}
	
	@Override
	public List consultaRapidaResumidaPorIdentificador(ControleConsulta controleConsulta, DataModelo controleConsultaOtimizado, List<UnidadeEnsinoVO> unidadeEnsinoVOs, int nivelmontardadosDadosminimos, UsuarioVO usuarioLogado) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuarioLogado);
		StringBuilder sql = getSQLPadraoConsultaBasicaComTotalizador();
		sql.append(" WHERE Turma.unidadeEnsino = UnidadeEnsino.codigo ");
		if (controleConsulta.getCampoConsulta().equals("identificadorTurma")) {
			sql.append(" AND lower (Turma.identificadorTurma) like lower(?)");
		} else if (controleConsulta.getCampoConsulta().equals("nomeCurso")) {
			sql.append(" AND upper(sem_acentos(curso.nome)) like upper(sem_acentos(?))");
		}
		sql.append(" AND turma.unidadeensino in (");
		for (UnidadeEnsinoVO unidade : unidadeEnsinoVOs) {
			if (unidade.equals(unidadeEnsinoVOs.get(unidadeEnsinoVOs.size() -1))) {
				sql.append(unidade.getCodigo() + ") ");
			} else {
				sql.append(unidade.getCodigo() + ", ");
			}
		}
		sql.append(" ORDER BY turma.identificadorTurma desc");
		sql.append(" LIMIT ").append(controleConsultaOtimizado.getLimitePorPagina());
		sql.append(" OFFSET ").append(controleConsultaOtimizado.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { Uteis.removerAcentuacao(controleConsulta.getValorConsulta().toLowerCase()) + "%" });
		controleConsultaOtimizado.setTotalRegistrosEncontrados(0);
        if(tabelaResultado.next()) {
        	controleConsultaOtimizado.setTotalRegistrosEncontrados(tabelaResultado.getInt("qtde_total_registros"));
        	tabelaResultado.beforeFirst();
        	return (montarDadosConsultaBasica(tabelaResultado));
        }
        return new ArrayList<>();
	}

	@Override
	public List<TurmaVO> consultarPorIdentificadorTurmaUnidadeEnsinoCursoTurno(String valorConsulta, List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs, Integer curso, Integer turno, boolean controlarAcesso, int nivelMontarDados, String nivelEducacional, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append("WHERE sem_acentos(lower (turma.identificadorturma)) like(sem_acentos('").append(valorConsulta.toLowerCase()).append("%'))  ");
		if (Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaUnidadeEnsinoVOs)) {
			sql.append(" AND turma.unidadeEnsino in (");
			for (ControleLivroRegistroDiplomaUnidadeEnsinoVO listaUnidade : controleLivroRegistroDiplomaUnidadeEnsinoVOs) {
				if (listaUnidade.equals(controleLivroRegistroDiplomaUnidadeEnsinoVOs.get(controleLivroRegistroDiplomaUnidadeEnsinoVOs.size() -1))) {
					sql.append(listaUnidade.getUnidadeEnsino().getCodigo()).append(") ");
				} else {
					sql.append(listaUnidade.getUnidadeEnsino().getCodigo()).append(", ");
				}
			}
		}
		if (curso != null && curso.intValue() != 0) {
			sql.append(" AND turma.curso = " + curso);
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sql.append(" AND (Curso.niveleducacional = '").append(nivelEducacional).append("')");
		}
		if (turno != null && turno.intValue() != 0) {
			sql.append(" AND turma.turno = " + turno);
		}
		sql.append(" ORDER BY  turma.identificadorTurma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaBasica(tabelaResultado);
	}
	
	@Override
	public List<TurmaVO> consultaPorCoordenadorParametizada(Integer coordenador, String valorConsulta, String campoConsulta, Boolean nivelEducacionalPosGraduacao, Boolean nivelEducacionalDiferentePosGraduacao, Boolean trazerTurmaAgrupada, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSQLPadraoConsultaBasica();
		sql.append(" WHERE ((turma.codigo IN(SELECT cc.turma FROM cursoCoordenador cc INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
		sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(")) OR (turma.curso IN (SELECT DISTINCT cc.curso FROM cursoCoordenador cc ");
		sql.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario ");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
		sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(" AND cc.turma IS NULL))");
		if (campoConsulta.equals("nome")) {
			sql.append(" AND (sem_acentos(turma.identificadorturma)) ilike (sem_acentos(?)) ");
		} else if (campoConsulta.equals("curso")) {
			sql.append(" AND (sem_acentos(curso.nome)) ilike (sem_acentos(?)) ");
		} else if (campoConsulta.equals("turno")) {
			sql.append(" AND (sem_acentos(turno.nome)) ilike (sem_acentos(?)) ");
		}
		if (trazerTurmaAgrupada) {
			// Esse OR ele vai procurar por turma agrupada
			sql.append(" OR (turma.codigo IN (");
			sql.append(" SELECT distinct turmaagrupada.turmaorigem FROM turmaagrupada ");
			sql.append(" INNER JOIN turma ON turma.codigo = turmaagrupada.turma ");
			sql.append(" WHERE turma.codigo in(SELECT cc.turma FROM cursoCoordenador cc ");
			sql.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario  ");
			sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa  ");
			sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(") ");
			sql.append(" or turma.curso in(");
			sql.append(" SELECT DISTINCT cc.curso FROM cursoCoordenador cc  ");
			sql.append(" INNER JOIN funcionario ON funcionario.codigo = cc.funcionario  ");
			sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa ");
			sql.append(" WHERE pessoa.codigo = ").append(coordenador).append(" AND cc.turma IS NULL ))))");
		} else {
			sql.append(") ");
		}
		if (unidadeEnsino != null && unidadeEnsino.intValue() != 0) {
			sql.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (nivelEducacionalPosGraduacao != null && nivelEducacionalPosGraduacao) {
			sql.append(" AND (curso.nivelEducacional in ('PO', 'EX', 'MT')) ");
		}
		if (nivelEducacionalDiferentePosGraduacao != null && nivelEducacionalDiferentePosGraduacao) {
			sql.append(" AND (curso.nivelEducacional != 'PO') ");
		}
		sql.append(" AND turma.situacao = 'AB' ");
		sql.append(" ORDER BY turma.identificadorturma ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta + PERCENT);
		return montarDadosConsultaBasica(tabelaResultado);
	}
	
	
	@Override
	public TurmaVO realizarCriacaoIdentificadorTurmaPadraoSemDisciplinaParaMatriculaProcessoSeletivoEmPeriodoLetivoEscolhaAutomatica(Integer unidadeensino, Integer curso , Integer turno , Integer gradeCurricular , Integer periodoLetivo , String ano , String semestre, Integer planofinanceirocurso, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSqlConsultaCriacaoTurmaSemDisciplinaParaMatriculaProcessoSeletivoEmPeriodoLetivoEscolhaAutomatica(unidadeensino, curso, turno, gradeCurricular, periodoLetivo, ano, semestre, planofinanceirocurso);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		TurmaVO obj = new TurmaVO();
		if(tabelaResultado.next()) {		
			obj.setIdentificadorTurma(tabelaResultado.getString("turma"));
		}	
		return obj;
	}	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public TurmaVO realizarCriacaoTurmaPadraoSemDisciplinaParaMatriculaProcessoSeletivoEmPeriodoLetivoEscolhaAutomatica(MatriculaVO matriculaVO , MatriculaPeriodoVO matriculaPeriodoVO , boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		TurmaVO turma = new TurmaVO();		
	    try {
		     getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		     StringBuilder sql = getSqlInsertCriacaoTurmaSemDisciplinaParaMatriculaProcessoSeletivoEmPeriodoLetivoEscolhaAutomatica(
		    		 matriculaVO.getUnidadeEnsino().getCodigo(),
		    		 matriculaVO.getCurso().getCodigo(),
		    		 matriculaVO.getTurno().getCodigo(),
		    		 matriculaVO.getGradeCurricularAtual().getCodigo(),
		    		 matriculaPeriodoVO.getPeridoLetivo().getCodigo(),
		    		 matriculaPeriodoVO.getAno(), 
		    		 matriculaPeriodoVO.getSemestre(),
		    		 matriculaPeriodoVO.getUnidadeEnsinoCursoVO().getCodigo());
		     
		     turma.setCodigo(	
				(Integer) getConexao().getJdbcTemplate().query(connection -> {
					return connection.prepareStatement(sql.toString());	     
				}, new ResultSetExtractor() {
		            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
		                if (rs.next()) {
		                	turma.setNovoObj(Boolean.FALSE);
		                    return rs.getInt("codigo");
		                }
		                return null;
		            }
		       }));
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return turma;		
	}	
	
	@Override 
	public StringBuilder getSqlConsultaCriacaoTurmaSemDisciplinaParaMatriculaProcessoSeletivoEmPeriodoLetivoEscolhaAutomatica(Integer unidadeensino, Integer curso , Integer turno , Integer gradeCurricular , Integer periodoLetivo , String ano , String semestre, Integer planofinanceirocurso) throws Exception {
		StringBuilder sql = new StringBuilder();	
    	
		sql.append(" select 'AB', curso, codigo_periodoletivo, turno, unidadeensino, gradecurricular, 0, 10000, 10000, case when (select codigo from turma where identificadorturma = turma limit 1 ) is not null then turma || '-' || 1 else turma end as turma ,  true, false, planofinanceirocurso, false, null, null, null, 'GERAL', true, '', 1000, now(), '', now(), ");
	    sql.append(" turnocenso, true, now(), 1, 'Otimize-TI', true, false, '").append(ano).append("' , '").append(semestre).append("' from (  ");
		sql.append(" select distinct unidadeensino.codigo as unidadeensino, curso.codigo as curso, turno.codigo as turno, gradecurricular.codigo  as gradecurricular, ");
		sql.append(" periodoletivo.codigo as codigo_periodoletivo, periodoletivo.periodoletivo, ");		
		sql.append(planofinanceirocurso).append(" as planofinanceirocurso, ");		
		sql.append(" (case when turno.nome = 'NOTURNO' then 3 else case when turno.nome = 'MATUTINO' then 1 else  case when turno.nome = 'VESPERTINO' then 2 else 0 end end end ) as turnocenso, ");
		sql.append(" sem_acentos(unidadeensino.abreviatura ||'-'||curso.abreviatura||'-'||gradecurricular.nome||'-'||periodoletivo.periodoletivo||'P-'||(case when turno.nome = 'NOTURNO' then 'NOT' else case when turno.nome = 'MATUTINO' then 'MAT' else  case when turno.nome = 'VESPERTINO' then 'VEP' else '' end end end )||'-").append(ano).append("/").append(semestre).append("') as turma ");
		sql.append(" from (select  ").append(unidadeensino).append(" as unidadeensino, ").append(curso).append(" as curso ,  ").append(turno).append(" as turno, ").append(gradeCurricular).append(" as gradecurricular, ").append(periodoLetivo).append(" as periodoletivo  ) as tabelaAuxiliar   ");  
    	sql.append(" inner join unidadeensino on unidadeensino.codigo = tabelaAuxiliar.unidadeensino");
		sql.append(" inner join turno on turno.codigo = tabelaAuxiliar.turno ");
		sql.append(" inner join curso on curso.codigo = tabelaAuxiliar.curso ");
	    sql.append(" inner join gradecurricular on gradecurricular.codigo = tabelaAuxiliar.gradecurricular ");
		sql.append(" inner join periodoletivo  on periodoletivo.codigo = tabelaAuxiliar.periodoletivo ");		
		sql.append(" 	order by turma 	) as t ");
		return sql;
	}
	
	@Override 
	public StringBuilder getSqlInsertCriacaoTurmaSemDisciplinaParaMatriculaProcessoSeletivoEmPeriodoLetivoEscolhaAutomatica(Integer unidadeensino, Integer curso , Integer turno , Integer gradeCurricular , Integer periodoLetivo , String ano , String semestre, Integer planofinanceirocurso) throws Exception {
		StringBuilder sql = new StringBuilder();		
		sql.append(" INSERT INTO public.turma  ");
		sql.append(" (situacao, curso, periodoletivo, turno, unidadeensino, gradecurricular, nrminimomatricula, nrmaximomatricula, nrvagas, identificadorturma,   semestral, anual,   ");
		sql.append(" planofinanceirocurso, subturma, turmaprincipal, configuracaoead, avaliacaoonline, tiposubturma, apresentarrenovacaoonline, abreviaturacurso, nrvagasinclusaoreposicao, datacriacao,  ");
		sql.append(" categoriacondicaopagamento, dataultimaalteracao, codigoturnoapresentarcenso, considerarturmaavaliacaoinstitucional, created, codigocreated, nomecreated, incluidosql, turmaagrupada, ano, semestre)  ");
		sql.append(" ( ");			
		sql.append(getSqlConsultaCriacaoTurmaSemDisciplinaParaMatriculaProcessoSeletivoEmPeriodoLetivoEscolhaAutomatica(unidadeensino, curso, turno, gradeCurricular, periodoLetivo, ano, semestre, planofinanceirocurso));
		sql.append(" ) returning codigo ; ");
		return sql;
	}
	
	@Override
	public List<TurmaRSVO> consultarTurmaPorPeriodoLetivoUnidadeEnsinoCursoTurnoParaMatriculaOnlineProcessoSeletivo(Integer aluno,Integer codigoUnidadeEnsino, Integer codigoPeriodoLetivo,  Integer numeroPeriodoletivo , Integer codigoCurso, Integer codigoTurno, Integer codigoGradeCurricular,  RenovarMatriculaControle renovarMatriculaControle ,UsuarioVO usuarioVO) {
		TurmaRSVO turmaRSVO = null;
		List<TurmaRSVO> turmaRSVOs = new ArrayList<TurmaRSVO>();
		try {
			List<TurmaVO> turmaVOs = getFacadeFactory().getTurmaFacade().consultaRapidaPorNrPeriodoLetivoUnidadeEnsinoCursoTurno(numeroPeriodoletivo, codigoUnidadeEnsino, codigoCurso, codigoTurno, codigoGradeCurricular, true, false, false, renovarMatriculaControle.getMatriculaVO().getCurso().getConfiguracaoAcademico().getMatricularApenasDisciplinaAulaProgramada(), renovarMatriculaControle.getMatriculaPeriodoVO().getAno(), renovarMatriculaControle.getMatriculaPeriodoVO().getSemestre(),  false, null, true);
			if(turmaVOs.isEmpty() && numeroPeriodoletivo != null &&  numeroPeriodoletivo > 1) {
				TurmaVO turmaVO =  getFacadeFactory().getTurmaFacade().realizarCriacaoIdentificadorTurmaPadraoSemDisciplinaParaMatriculaProcessoSeletivoEmPeriodoLetivoEscolhaAutomatica(
						codigoUnidadeEnsino, codigoCurso, codigoTurno, codigoGradeCurricular,codigoPeriodoLetivo,renovarMatriculaControle.getMatriculaPeriodoVO().getAno(), renovarMatriculaControle.getMatriculaPeriodoVO().getSemestre(),0, false, usuarioVO) ;				
				turmaRSVO = new TurmaRSVO();
				turmaRSVO.setNome(turmaVO.getIdentificadorTurma());
				turmaRSVO.setPossuiDisciplinasNaTurma(false);
				turmaRSVOs.add(turmaRSVO);				
				
			}else if(!turmaVOs.isEmpty()) {
				for (TurmaVO turmaVO : turmaVOs) {
					if(renovarMatriculaControle != null && !Uteis.isAtributoPreenchido(renovarMatriculaControle.getMatriculaPeriodoVO().getTurma())) {
						renovarMatriculaControle.getMatriculaPeriodoVO().setTurma(turmaVO);
					}
					turmaRSVO = new TurmaRSVO();
					turmaRSVO.setCodigo(turmaVO.getCodigo());
					turmaRSVO.setNome(turmaVO.getIdentificadorTurma());				
					turmaRSVO.setPossuiDisciplinasNaTurma(validarExistenciaDisplinasTurmaDisponivelEHabilitadaParaEstudar(turmaVO,aluno ,codigoGradeCurricular ,usuarioVO));
					turmaRSVOs.add(turmaRSVO);
					
				}
				getFacadeFactory().getTurmaFacade().processarDadosPermitinentesTurmaSelecionada(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO(), usuarioVO);

			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return turmaRSVOs;
	}
	
	
	private Boolean validarExistenciaDisplinasTurmaDisponivelEHabilitadaParaEstudar(TurmaVO  turmaVO , Integer aluno,  Integer gradeCurricular ,  UsuarioVO usuarioVO) {
	
		try {
			turmaVO.setTurmaDisciplinaVOs(getFacadeFactory().getTurmaDisciplinaFacade().consultarPorCodigoTurma(turmaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
		   getFacadeFactory().getMatriculaFacade().realizarCriacaoHistoricoApartirDisciplinasAprovadasMesmaGradeCurricularMatriculaOnline("",aluno ,gradeCurricular, turmaVO , NivelMontarDados.TODOS ,usuarioVO);
            return !turmaVO.getTurmaDisciplinaVOs().isEmpty();
		} catch (Exception e) {
			return false ;
		}
		
	}
	
	@Override
	public void consultarTurma(ControleConsultaTurma controleConsultaTurma, Integer unidadeEnsino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, List<CursoVO> cursoVOs, Integer turno, List<TurnoVO> turnoVOs, Integer gradeCurricularVO, Integer codDisciplina, UsuarioVO usuarioVO) throws Exception{
		consultarTurma(controleConsultaTurma, unidadeEnsino, unidadeEnsinoVOs, curso, cursoVOs, turno, turnoVOs, gradeCurricularVO, codDisciplina, null, null, usuarioVO);
	}
	
	@Override
	public void consultarTurma(ControleConsultaTurma controleConsultaTurma, Integer unidadeEnsino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, List<CursoVO> cursoVOs, Integer turno, List<TurnoVO> turnoVOs, Integer gradeCurricularVO, Integer codDisciplina,  PeriodicidadeEnum[] periodicidadeEnuns, TipoNivelEducacional[] nivelEducacionalEnuns, UsuarioVO usuarioVO) throws Exception{
		
		consultarTurma(controleConsultaTurma, unidadeEnsino, unidadeEnsinoVOs, curso, cursoVOs, turno, turnoVOs, gradeCurricularVO, codDisciplina, periodicidadeEnuns, nivelEducacionalEnuns, null, usuarioVO);
	}
	
	@Override
	public void consultarTurma(ControleConsultaTurma controleConsultaTurma, Integer unidadeEnsino, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, List<CursoVO> cursoVOs, Integer turno, List<TurnoVO> turnoVOs, Integer gradeCurricularVO, Integer codDisciplina,  PeriodicidadeEnum[] periodicidadeEnuns, TipoNivelEducacional[] nivelEducacionalEnuns, List<TurmaVO> turmasDesconsiderar, UsuarioVO usuarioVO) throws Exception{
		StringBuilder sql = getSQLPadraoConsultaBasicaComTotalizador();
		List<String> filtros = new ArrayList<String>(0);
		sql.append(" where 1 = 1 ");
		if(controleConsultaTurma.isIdentificadorTurma() && !controleConsultaTurma.getValorConsulta().trim().isEmpty()) {
			sql.append(" and sem_acentos(turma.identificadorTurma) ilike sem_acentos(?) ");
			filtros.add(controleConsultaTurma.getValorConsulta()+PERCENT);
		}
		if(!controleConsultaTurma.getValorConsultaCurso().trim().isEmpty()) {
			sql.append(" and sem_acentos(curso.nome) ilike sem_acentos(?) ");
			filtros.add(controleConsultaTurma.getValorConsultaCurso()+PERCENT);
		}
		if(controleConsultaTurma.isCurso()) {
			sql.append(" and sem_acentos(curso.nome) ilike sem_acentos(?) ");
			filtros.add(controleConsultaTurma.getValorConsulta()+PERCENT);
		}
		if(controleConsultaTurma.isAberta() || controleConsultaTurma.isFechada()) {
			sql.append(" and turma.situacao = ? ");
			filtros.add(controleConsultaTurma.getSituacaoTurma());
		}
		if(controleConsultaTurma.isNormal()) {
			sql.append(" and turma.subturma = false and turma.turmaagrupada =  false ");			
		}
		if(controleConsultaTurma.isSubTurmaGeral()) {
			sql.append(" and turma.subturma = true and turma.turmaagrupada = false and turma.tiposubturma =  'GERAL' ");			
		}
		if(controleConsultaTurma.isSubTurmaPratica()) {
			sql.append(" and turma.subturma = true and turma.turmaagrupada = false and turma.tiposubturma =  'PRATICA' ");			
		}
		if(controleConsultaTurma.isSubTurmaTeorica()) {
			sql.append(" and turma.subturma = true and turma.turmaagrupada = false and turma.tiposubturma =  'TEORICA' ");			
		}
		if(controleConsultaTurma.isAgrupada()) {
			sql.append(" and turma.turmaagrupada = true and turma.subturma = false  ");			
		}
		if(controleConsultaTurma.isAgrupadaPratica()) {
			sql.append(" and turma.turmaagrupada = true and turma.subturma = true and turma.tiposubturma =  'PRATICA' ");			
		}
		if(controleConsultaTurma.isSubTurmaTeorica()) {
			sql.append(" and turma.turmaagrupada = true and turma.subturma = true and turma.tiposubturma =  'TEORICA' ");			
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sql.append(" and unidadeensino.codigo = ").append(unidadeEnsino);
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsinoVOs) && unidadeEnsinoVOs.stream().anyMatch(u -> u.getFiltrarUnidadeEnsino())) {
			sql.append(" and ").append(realizarGeracaoWhereUnidadeEnsinoSelecionada(unidadeEnsinoVOs, "unidadeensino.codigo"));
		}
		if(Uteis.isAtributoPreenchido(curso)) {
			sql.append(" and curso.codigo = ").append(curso);
		}
		if(Uteis.isAtributoPreenchido(cursoVOs) && cursoVOs.stream().anyMatch(c ->  c.getFiltrarCursoVO())) {
			sql.append(" and  ").append(realizarGeracaoWhereCursoSelecionada(cursoVOs, "curso.codigo"));
		}
		if(Uteis.isAtributoPreenchido(turno)) {
			sql.append(" and turno.codigo = ").append(turno);
		}
		if(Uteis.isAtributoPreenchido(turnoVOs) && turnoVOs.stream().anyMatch(t -> t.getFiltrarTurnoVO())) {
			sql.append(" and ").append(realizarGeracaoWhereTurnoSelecionada(turnoVOs, "turno.codigo"));
		}
		if(Uteis.isAtributoPreenchido(controleConsultaTurma.getPeriodoLetivoTurma()) && controleConsultaTurma.getPeriodoLetivoTurma() > 0) {
			sql.append(" and periodoletivo.periodoletivo = ").append(controleConsultaTurma.getPeriodoLetivoTurma());
		}		
		if(Uteis.isAtributoPreenchido(controleConsultaTurma.getDigitoTurma())) {
			sql.append(" and turma.digitoTurma = ? ");
			filtros.add(controleConsultaTurma.getDigitoTurma());
		}
		if(Uteis.isAtributoPreenchido(gradeCurricularVO)) {
			sql.append(" and gradecurricular.codigo = ").append(gradeCurricularVO);
		}
		if(Uteis.isAtributoPreenchido(codDisciplina)) {
			sql.append(" and ( exists (select td from turmadisciplina as td where td.turma = turma.codigo and td.disciplina = ").append(codDisciplina).append(") ");
			sql.append(" or exists(select td from turmadisciplina as td inner join gradedisciplinacomposta gdc on gdc.gradedisciplina = td.gradedisciplina  where td.turma = turma.codigo and gdc.disciplina = ").append(codDisciplina).append(" )  ");
			sql.append(" or exists(select td from turmadisciplina as td inner join gradedisciplinacomposta gdc on gdc.gradecurriculargrupooptativadisciplina = td.gradecurriculargrupooptativadisciplina  where td.turma = turma.codigo and gdc.disciplina = ").append(codDisciplina).append(" ) ) ");
		}
		if(Uteis.isAtributoPreenchido(turmasDesconsiderar)) {
			sql.append(" and turma.codigo not in (0");
			for(TurmaVO turma: turmasDesconsiderar) {
				sql.append(", ").append(turma.getCodigo());	
			}
			sql.append(") ");
		}
		if(periodicidadeEnuns != null) {
			int x = 0;
			sql.append(" and (");
			for(PeriodicidadeEnum periodicidadeEnum: periodicidadeEnuns) {
				if(x > 0) {
					sql.append(" or ");
				}
				switch(periodicidadeEnum) {
					case ANUAL:
						sql.append(" turma.anual ");
						x++;
						break;
					case SEMESTRAL:
						sql.append(" turma.semestral ");
						x++;
						break;
					case INTEGRAL:
						sql.append(" (turma.anual is false and turma.semestral is false) ");
						x++;
						break;
				}
			}
			sql.append(" ) ");
		}
		if(nivelEducacionalEnuns != null) {
			int x = 0;
			sql.append(" and curso.niveleducacional in (");
			for(TipoNivelEducacional tipoNivelEducacional: nivelEducacionalEnuns) {
				filtros.add(tipoNivelEducacional.getValor());
				if(x > 0) {
					sql.append(", ");
				}
				sql.append(" ? ");				
				x++;
			}
			sql.append(" ) ");
		}
		if(Uteis.isAtributoPreenchido(usuarioVO) && usuarioVO.getIsApresentarVisaoCoordenador()){
			sql.append(" and exists ( ");
			sql.append(" select cursocoordenador.codigo from cursocoordenador ");
			sql.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
			sql.append(" where funcionario.pessoa = ").append(usuarioVO.getPessoa().getCodigo());
			sql.append(" and ((cursocoordenador.turma is not null and cursocoordenador.turma = turma.codigo) ");
			sql.append(" or (turma.turmaagrupada is false and cursocoordenador.turma is null and cursocoordenador.curso = turma.curso ");
			sql.append(" and cursocoordenador.unidadeensino = unidadeensino.codigo) ");
			sql.append(" or (turma.turmaagrupada and cursocoordenador.turma is null and cursocoordenador.curso in ( ");
			sql.append(" select tu.curso from turmaagrupada t ");
			sql.append(" inner join turma as tu on tu.codigo = t.turma ");
			sql.append(" where t.turmaorigem = turma.codigo ");
			sql.append(" and tu.unidadeensino = unidadeensino.codigo ");
			sql.append(")))");
			sql.append(")");
		}
		sql.append(" order by turma.identificadorTurma, unidadeensino.nome, curso.nome, turno.nome ");
		sql.append(" LIMIT ").append(controleConsultaTurma.getLimitePorPagina());
		sql.append(" OFFSET ").append(controleConsultaTurma.getOffset());		
		//System.out.println(sql.toString());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
		controleConsultaTurma.setTotalRegistrosEncontrados(0);
        if(rs.next()) {
        	controleConsultaTurma.setTotalRegistrosEncontrados(rs.getInt("qtde_total_registros"));
        	rs.beforeFirst();
        	controleConsultaTurma.setListaConsulta(montarDadosConsultaBasica(rs));
        }else {
        	controleConsultaTurma.setListaConsulta(new ArrayList(0));
        }        
		
	}
}
