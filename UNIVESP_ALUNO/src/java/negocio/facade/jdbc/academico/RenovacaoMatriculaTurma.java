package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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

import controle.arquitetura.AplicacaoControle;
import jobs.JobRenovacaoTurma;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaMatriculaPeriodoVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoRenovacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoRenovacaoTurmaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.AcessoException;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.RenovacaoMatriculaTurmaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RenovacaoMatriculaTurma extends ControleAcesso implements RenovacaoMatriculaTurmaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public RenovacaoMatriculaTurma() {
		super();
		setIdEntidade("RenovarMatriculaPorTurma");
	}

	@Override
	public void validarDadosInicioProcessamento(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, AplicacaoControle aplicacaoControle) throws ConsistirException, AcessoException {
		validarDados(renovacaoMatriculaTurmaVO);
		if(Uteis.isAtributoPreenchido(renovacaoMatriculaTurmaVO.getTurmaVO())) {
			boolean existeSelecao = false;
			int x = 0;
			for (RenovacaoMatriculaTurmaMatriculaPeriodoVO obj : renovacaoMatriculaTurmaVO.getRenovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs()) {
				if (obj.getSelecionado()) {
					existeSelecao = true;
					++x;
//					if (obj.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo() == null || obj.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo() == 0) {
//						throw new ConsistirException(UteisJSF.internacionalizar("msg_RenovarTurma_condicaoPagamento").replace("{0}", obj.getMatriculaPeriodoVO().getMatricula()));
//					}					
				}
			}
			if (!existeSelecao) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_RenovarTurma_selecionarMatricula"));
			}
			renovacaoMatriculaTurmaVO.setQtdeRenovacaoAGerada(x);
		}
		Uteis.checkState(!Uteis.isAtributoPreenchido(renovacaoMatriculaTurmaVO.getQtdeRenovacaoAGerada()), UteisJSF.internacionalizar("msg_RenovarTurma_selecionarMatricula"));
		renovacaoMatriculaTurmaVO.setQtdeRenovacaoGerada(0);
		renovacaoMatriculaTurmaVO.setQtdeRenovacaoErro(0);
	}

	public static void validarDados(RenovacaoMatriculaTurmaVO obj) throws ConsistirException {
		if (!Uteis.isAtributoPreenchido(obj.getAno())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_ano"));
		}
		if (!Uteis.isAtributoPreenchido(obj.getSemestre())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_semestre"));
		}		
		if (Uteis.isAtributoPreenchido(obj.getTurmaVO()) && !Uteis.isAtributoPreenchido(obj.getGradeCurricularAtual())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_RenovacaoMatriculaTurma_gradeCurricular"));
		}		
		if (Uteis.isAtributoPreenchido(obj.getTurmaVO()) && !Uteis.isAtributoPreenchido(obj.getProcessoMatriculaRenovar())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_RenovacaoMatriculaTurma_processoMatricula"));
		}
		if (Uteis.isAtributoPreenchido(obj.getTurmaVO()) && !Uteis.isAtributoPreenchido(obj.getPeriodoLetivoRenovar())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_RenovacaoMatriculaTurma_periodoLetivoRenovar"));
		}
		if ((Uteis.isAtributoPreenchido(obj.getTurmaVO()) ) && !Uteis.isAtributoPreenchido(obj.getTurmaRenovar())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_RenovacaoMatriculaTurma_turmaRenovar"));
		}
//		if (Uteis.isAtributoPreenchido(obj.getTurmaVO()) && !Uteis.isAtributoPreenchido(obj.getPlanoFinanceiroCursoRenovar())) {
//			throw new ConsistirException(UteisJSF.internacionalizar("msg_RenovacaoMatriculaTurma_planoFinanceiroRenovar"));
//		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(final RenovacaoMatriculaTurmaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception {
		if (obj.getNovoObj()) {
			incluir(obj, verificarAcesso, usuarioVO, gradeDisciplinaCompostaVOs);
		} else {
			alterar(obj, verificarAcesso, usuarioVO, gradeDisciplinaCompostaVOs);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RenovacaoMatriculaTurmaVO obj, boolean verificarAcesso, final UsuarioVO usuarioVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception {
		try {			
			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder("INSERT INTO RenovacaoMatriculaTurma ");
			sql.append(" (turma, responsavel, data, ano, semestre, aprovadoSemestre, ");
			sql.append(" dataInicioProcessamento, dataTerminoProcessamento, planoFinanceiroCursoAtual, ");
			sql.append(" condicaoPagamentoPlanoFinanceiroCursoAtual, turmaRenovar, processoMatriculaRenovar, manterCondicaoPagamentoAtual, condicaoPagamentoPlanoFinanceiroCursoRenovar, ");
			sql.append(" situacao, qtdeRenovacaoAGerada, mensagemErro, dataRenovacao, planoFinanceiroCursoRenovar, gradeCurricularAtual, periodoLetivoRenovar, ");
			sql.append(" renovarApenasAlunosRenovacaoAutomatica, unidadeensino, curso, liberadoInclusaoTurmaOutroUnidadeEnsino, liberadoInclusaoTurmaOutroCurso, liberadoInclusaoTurmaOutroMatrizCurricular ");
			sql.append(" ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int x = 1;
					sqlInserir.setInt(x++, obj.getTurmaVO().getCodigo());
					sqlInserir.setInt(x++, obj.getResponsavel().getCodigo());
					sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlInserir.setString(x++, obj.getAno());
					sqlInserir.setString(x++, obj.getSemestre());
					sqlInserir.setString(x++, obj.getAprovadoSemestre());
					if (obj.getDataInicioProcessamento() != null) {
						sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(obj.getDataInicioProcessamento()));
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getDataTerminoProcessamento() != null) {
						sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(obj.getDataTerminoProcessamento()));
					} else {
						sqlInserir.setNull(x++, 0);
					}					
//					if (obj.getPlanoFinanceiroCursoAtual() != null && obj.getPlanoFinanceiroCursoAtual().getCodigo() > 0) {
//						sqlInserir.setInt(x++, obj.getPlanoFinanceiroCursoAtual().getCodigo());
//					} else {
						sqlInserir.setNull(x++, 0);
//					}
//					if (obj.getCondicaoPagamentoPlanoFinanceiroCursoAtual() != null && obj.getCondicaoPagamentoPlanoFinanceiroCursoAtual().getCodigo() > 0) {
//						sqlInserir.setInt(x++, obj.getCondicaoPagamentoPlanoFinanceiroCursoAtual().getCodigo());
//					} else {
						sqlInserir.setNull(x++, 0);
//					}
					if (obj.getTurmaRenovar() != null && obj.getTurmaRenovar().getCodigo() > 0) {
						sqlInserir.setInt(x++, obj.getTurmaRenovar().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getProcessoMatriculaRenovar() != null && obj.getProcessoMatriculaRenovar().getCodigo() > 0) {
						sqlInserir.setInt(x++, obj.getProcessoMatriculaRenovar().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
//					sqlInserir.setBoolean(x++, obj.getManterCondicaoPagamentoAtual());
					sqlInserir.setBoolean(x++, false);
//					if (obj.getCondicaoPagamentoPlanoFinanceiroCursoRenovar() != null && obj.getCondicaoPagamentoPlanoFinanceiroCursoRenovar().getCodigo() > 0) {
//						sqlInserir.setInt(x++, obj.getCondicaoPagamentoPlanoFinanceiroCursoRenovar().getCodigo());
//					} else {
						sqlInserir.setNull(x++, 0);
//					}
					sqlInserir.setString(x++, obj.getSituacao().name());
					sqlInserir.setInt(x++, obj.getQtdeRenovacaoAGerada());					
					sqlInserir.setString(x++, obj.getMensagemErro());
					sqlInserir.setDate(x++, Uteis.getDataJDBC(obj.getDataRenovacao()));
//					if (obj.getPlanoFinanceiroCursoRenovar() != null && obj.getPlanoFinanceiroCursoRenovar().getCodigo() > 0) {
//						sqlInserir.setInt(x++, obj.getPlanoFinanceiroCursoRenovar().getCodigo());
//					} else {
						sqlInserir.setNull(x++, 0);
//					}
					if (obj.getGradeCurricularAtual() != null && obj.getGradeCurricularAtual().getCodigo() > 0) {
						sqlInserir.setInt(x++, obj.getGradeCurricularAtual().getCodigo());	
					}else {
						sqlInserir.setNull(x++, 0);
					}
					if (obj.getPeriodoLetivoRenovar() != null && obj.getPeriodoLetivoRenovar().getCodigo() > 0) {
						sqlInserir.setInt(x++, obj.getPeriodoLetivoRenovar().getCodigo());	
					}else {
						sqlInserir.setNull(x++, 0);
					}
					Uteis.setValuePreparedStatement(obj.getRenovarApenasAlunosRenovacaoAutomatica(), x++, sqlInserir, con);
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), x++, sqlInserir, con);
					Uteis.setValuePreparedStatement(obj.getCursoVO(), x++, sqlInserir,con);
					Uteis.setValuePreparedStatement(obj.isLiberadoInclusaoTurmaOutroUnidadeEnsino(), x++, sqlInserir,con);
					Uteis.setValuePreparedStatement(obj.isLiberadoInclusaoTurmaOutroCurso(), x++, sqlInserir,con);
					Uteis.setValuePreparedStatement(obj.isLiberadoInclusaoTurmaOutroMatrizCurricular(), x++, sqlInserir,con);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().incluirRenovacaoMatriculaTurmaMatriculaPeriodoVOs(obj, verificarAcesso, usuarioVO);
			getFacadeFactory().getRenovacaoMatriculaTurmaGradeDisciplinaCompostaFacade().executarGeracaoRenovacaoMatriculaTurmaGradeDisciplinaComposta(obj, gradeDisciplinaCompostaVOs);
			getFacadeFactory().getRenovacaoMatriculaTurmaGradeDisciplinaCompostaFacade().persistirPorRenovacaoMatriculaTurma(obj, false, usuarioVO);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final RenovacaoMatriculaTurmaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder(" UPDATE RenovacaoMatriculaTurma ");
			sql.append(" set responsavel = ?, dataInicioProcessamento = ?, dataTerminoProcessamento = ?, ");
			sql.append(" situacao = ?, mensagemErro = ?, qtdeRenovacaoAGerada = ? ");
			sql.append(" WHERE ((codigo = ?))").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int x = 1;
					sqlAlterar.setInt(x++, obj.getResponsavel().getCodigo());
					if (obj.getDataInicioProcessamento() != null) {
						sqlAlterar.setTimestamp(x++, Uteis.getDataJDBCTimestamp(obj.getDataInicioProcessamento()));
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getDataTerminoProcessamento() != null) {
						sqlAlterar.setTimestamp(x++, Uteis.getDataJDBCTimestamp(obj.getDataTerminoProcessamento()));
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setString(x++, obj.getSituacao().name());
					sqlAlterar.setString(x++, obj.getMensagemErro());
					sqlAlterar.setInt(x++, obj.getQtdeRenovacaoAGerada());
					sqlAlterar.setInt(x++, obj.getCodigo());					
					return sqlAlterar;
				}
			});
			getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().alterarRenovacaoMatriculaTurmaMatriculaPeriodoVOs(obj, verificarAcesso, usuarioVO);
			getFacadeFactory().getRenovacaoMatriculaTurmaGradeDisciplinaCompostaFacade().executarGeracaoRenovacaoMatriculaTurmaGradeDisciplinaComposta(obj, gradeDisciplinaCompostaVOs);
			getFacadeFactory().getRenovacaoMatriculaTurmaGradeDisciplinaCompostaFacade().persistirPorRenovacaoMatriculaTurma(obj, false, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDadosBasicos(final RenovacaoMatriculaTurmaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder(" UPDATE RenovacaoMatriculaTurma ");
			sql.append(" set responsavel = ?, dataInicioProcessamento = ?, dataTerminoProcessamento = ?, ");
			sql.append(" situacao = ?, mensagemErro = ? ");
			sql.append(" WHERE ((codigo = ?))").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int x = 1;
					sqlAlterar.setInt(x++, obj.getResponsavel().getCodigo());
					if (obj.getDataInicioProcessamento() != null) {
						sqlAlterar.setTimestamp(x++, Uteis.getDataJDBCTimestamp(obj.getDataInicioProcessamento()));
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (obj.getDataTerminoProcessamento() != null) {
						sqlAlterar.setTimestamp(x++, Uteis.getDataJDBCTimestamp(obj.getDataTerminoProcessamento()));
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setString(x++, obj.getSituacao().name());
					sqlAlterar.setString(x++, obj.getMensagemErro());
					sqlAlterar.setInt(x++, obj.getCodigo());
					return sqlAlterar;
				}
			});			
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarQuantitativo(final RenovacaoMatriculaTurmaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder(" UPDATE RenovacaoMatriculaTurma ");
			sql.append(" set mensagemErro = ? ");
			sql.append(" WHERE ((codigo = ?))").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int x = 1;
					sqlAlterar.setString(x++, obj.getMensagemErro());
					sqlAlterar.setInt(x++, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RenovacaoMatriculaTurmaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM RenovacaoMatriculaTurma WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	
	

	@Override
	public List<RenovacaoMatriculaTurmaVO> consultar(String campoConsulta, String valorConsulta, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO, Integer limit, Integer offset) throws Exception {
		consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaBasica());
		sqlStr.append(" where 1 = 1 ");
		realizarMontagemFiltrosConsulta(campoConsulta, valorConsulta, sqlStr, true);
		sqlStr.append(" RenovacaoMatriculaTurma.ano||'/'||RenovacaoMatriculaTurma.semestre desc, RenovacaoMatriculaTurma.data ");
		if(limit != null && limit > 0){
			sqlStr.append(" limit ").append(limit).append(" offset ").append(offset);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
		
		
	}

	
	
	@Override
	public Integer consultarTotalRegistro(String campoConsulta, String valorConsulta, UsuarioVO usuarioVO) throws Exception {		
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT count(RenovacaoMatriculaTurma.codigo) as qtde ");
		sqlStr.append(" FROM RenovacaoMatriculaTurma ");
		sqlStr.append(" left JOIN UnidadeEnsino on UnidadeEnsino.codigo = RenovacaoMatriculaTurma.UnidadeEnsino ");
		sqlStr.append(" left JOIN Curso on curso.codigo = RenovacaoMatriculaTurma.curso ");
		sqlStr.append(" left JOIN Turma on turma.codigo = RenovacaoMatriculaTurma.turma ");
		sqlStr.append(" where 1 = 1 ");
		realizarMontagemFiltrosConsulta(campoConsulta, valorConsulta, sqlStr, false);	
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(rs.next()){
			return rs.getInt("qtde");
		}
		return 0; 
		
	}
	
	private void realizarMontagemFiltrosConsulta(String campoConsulta, String valorConsulta, StringBuilder sqlStr, boolean isOrderby) {
		if (campoConsulta.equals("turma")) {
			if (valorConsulta != null && !valorConsulta.isEmpty()) {
				sqlStr.append(" and sem_acentos(Turma.identificadorTurma) ilike sem_acentos('%").append(valorConsulta).append("%') ");
			}
			if(isOrderby) {
				sqlStr.append(" ORDER BY Turma.identificadorTurma, ");	
			}
		} else if (campoConsulta.equals("curso")) {
			if (valorConsulta != null && !valorConsulta.isEmpty()) {
				sqlStr.append(" and sem_acentos(Curso.nome) ilike sem_acentos('%").append(valorConsulta).append("%') ");
			}
			if(isOrderby) {
				sqlStr.append(" ORDER BY Curso.nome, ");	
			}
		} else if (campoConsulta.equals("unidadeensino")) {
			if (valorConsulta != null && !valorConsulta.isEmpty()) {
				sqlStr.append(" and sem_acentos(UnidadeEnsino.nome) ilike sem_acentos('%").append(valorConsulta).append("%') ");
			}
			if(isOrderby) {
				sqlStr.append(" ORDER BY UnidadeEnsino.nome, ");	
			}			
		} else if (campoConsulta.equals("matricula")) {
			if (valorConsulta != null && !valorConsulta.isEmpty()) {
				sqlStr.append(" and RenovacaoMatriculaTurma.codigo IN ( ");
				sqlStr.append(" select distinct RenovacaoMatriculaTurmaMatriculaPeriodo.RenovacaoMatriculaTurma from  RenovacaoMatriculaTurmaMatriculaPeriodo ");
				sqlStr.append(" inner join matriculaperiodo on RenovacaoMatriculaTurmaMatriculaPeriodo.matriculaperiodo = matriculaperiodo.codigo ");
				sqlStr.append(" where  ");
				sqlStr.append(" sem_acentos(matriculaperiodo.matricula) = sem_acentos('").append(valorConsulta).append("')) ");
			}
			if(isOrderby) {
				sqlStr.append(" ORDER BY ");	
			}
		} else if (campoConsulta.equals("aluno")) {
			if (valorConsulta != null && !valorConsulta.isEmpty()) {
				sqlStr.append(" and RenovacaoMatriculaTurma.codigo IN ( ");
				sqlStr.append(" select distinct RenovacaoMatriculaTurmaMatriculaPeriodo.RenovacaoMatriculaTurma from  RenovacaoMatriculaTurmaMatriculaPeriodo ");
				sqlStr.append(" inner join matriculaperiodo on RenovacaoMatriculaTurmaMatriculaPeriodo.matriculaperiodo = matriculaperiodo.codigo ");
				sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
				sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
				sqlStr.append(" where  ");
				sqlStr.append(" sem_acentos(pessoa.nome) ilike sem_acentos('%").append(valorConsulta).append("%')) ");
			}
			if(isOrderby) {
				sqlStr.append(" ORDER BY ");	
			}
		} else if (campoConsulta.equals("registroAcademico")) {
			if (valorConsulta != null && !valorConsulta.isEmpty()) {
				sqlStr.append(" and RenovacaoMatriculaTurma.codigo IN ( ");
				sqlStr.append(" select distinct RenovacaoMatriculaTurmaMatriculaPeriodo.RenovacaoMatriculaTurma from  RenovacaoMatriculaTurmaMatriculaPeriodo ");
				sqlStr.append(" inner join matriculaperiodo on RenovacaoMatriculaTurmaMatriculaPeriodo.matriculaperiodo = matriculaperiodo.codigo ");
				sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
				sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
				sqlStr.append(" where  ");
				sqlStr.append(" sem_acentos(pessoa.registroAcademico) ilike sem_acentos('%").append(valorConsulta).append("%')) ");
			}
			if(isOrderby) {
				sqlStr.append(" ORDER BY ");	
			}
		}
	}

	public List<RenovacaoMatriculaTurmaVO> consultarPorTurma(String valorConsulta, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaBasica());
		sqlStr.append("WHERE Turma.identificadorTurma like '%").append(valorConsulta).append("%' ");
		sqlStr.append("ORDER BY Turma.identificadorTurma ");		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}

	@Override
	public List<RenovacaoMatriculaTurmaVO> consultarPorSituacao(SituacaoRenovacaoTurmaEnum situacaoRenovacaoTurmaEnum, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO, Integer limite, Integer offset) throws Exception {
		consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaBasica());
		sqlStr.append(" WHERE RenovacaoMatriculaTurma.situacao = '").append(situacaoRenovacaoTurmaEnum.name()).append("' ");
		if(usuarioVO != null && usuarioVO.getUnidadeEnsinoLogado().getCodigo() > 0){
			sqlStr.append(" and RenovacaoMatriculaTurma.unidadeensino = ").append(usuarioVO.getUnidadeEnsinoLogado().getCodigo());
		}		
		sqlStr.append(" ORDER BY RenovacaoMatriculaTurma.codigo ");
		if(limite != null && limite > 0){
			sqlStr.append(" limit ").append(limite).append(" offset ").append(offset);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}
	
	@Override
	public Integer consultarTotalRegistroPorSituacao(SituacaoRenovacaoTurmaEnum situacaoRenovacaoTurmaEnum, UsuarioVO usuarioVO) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("select count(RenovacaoMatriculaTurma.codigo) as qtde from RenovacaoMatriculaTurma ");		
		sqlStr.append(" WHERE RenovacaoMatriculaTurma.situacao = '").append(situacaoRenovacaoTurmaEnum.name()).append("' ");
		if(usuarioVO != null && usuarioVO.getUnidadeEnsinoLogado().getCodigo() > 0){
			sqlStr.append(" and RenovacaoMatriculaTurma.unidadeensino = ").append(usuarioVO.getUnidadeEnsinoLogado().getCodigo());
		}		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if(rs.next()){
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public RenovacaoMatriculaTurmaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaBasica());
		sqlStr.append(" WHERE RenovacaoMatriculaTurma.codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			RenovacaoMatriculaTurmaVO obj = new RenovacaoMatriculaTurmaVO();
			montarDados(obj, rs, nivelMontarDados, usuarioVO);
			return obj;
		}
		throw new Exception("Dados não encontrados(RenovacaoMatriculaTurma)");
	}

	public StringBuilder getSqlConsultaBasica() {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT RenovacaoMatriculaTurma.*, ");
		sqlStr.append(" (select count(codigo) from RenovacaoMatriculaTurmaMatriculaPeriodo where RenovacaoMatriculaTurmaMatriculaPeriodo.RenovacaoMatriculaTurma = RenovacaoMatriculaTurma.codigo and RenovacaoMatriculaTurmaMatriculaPeriodo.situacao = '").append(SituacaoRenovacaoMatriculaPeriodoEnum.REALIZADO_SUCESSO).append("' ) as qtdeRenovacaoGerada, ");
		sqlStr.append(" (select count(codigo) from RenovacaoMatriculaTurmaMatriculaPeriodo where RenovacaoMatriculaTurmaMatriculaPeriodo.RenovacaoMatriculaTurma = RenovacaoMatriculaTurma.codigo and RenovacaoMatriculaTurmaMatriculaPeriodo.situacao = '").append(SituacaoRenovacaoMatriculaPeriodoEnum.REALIZADO_ERRO).append("' ) as qtdeRenovacaoErro, ");
		sqlStr.append(" responsavel.nome as \"responsavel.nome\", ");		
		sqlStr.append(" Turma.identificadorTurma as \"Turma.identificadorTurma\", ");
		
		sqlStr.append(" unidadeEnsino.codigo as \"unidadeEnsino.codigo\", ");
		sqlStr.append(" unidadeEnsino.nome as \"unidadeEnsino.nome\", ");
		sqlStr.append(" Curso.nome as \"Curso.nome\", ");
		sqlStr.append(" Curso.codigo as \"Curso.codigo\", ");
		sqlStr.append(" Curso.periodicidade as \"Curso.periodicidade\", ");
		
		
		sqlStr.append(" turma_unidadeensino.codigo as \"turma_unidadeensino.codigo\", ");
		sqlStr.append(" turma_unidadeensino.nome as \"turma_unidadeensino.nome\", ");
		sqlStr.append(" turma_curso.nome as \"turma_curso.nome\", ");
		sqlStr.append(" turma_curso.codigo as \"turma_curso.codigo\", ");
		sqlStr.append(" turma_curso.periodicidade as \"turma_curso.periodicidade\", ");
		sqlStr.append(" Turno.codigo as \"Turno.codigo\", ");
		sqlStr.append(" Turno.nome as \"Turno.nome\", ");
		
		sqlStr.append(" planoFinanceiroCursoAtual.descricao as \"planoFinanceiroCursoAtual.descricao\", ");
		sqlStr.append(" planoFinanceiroCursoRenovar.descricao as \"planoFinanceiroCursoRenovar.descricao\", ");
		sqlStr.append(" condicaoPagamentoPlanoFinanceiroCursoAtual.descricao as \"condicaoPagamentoPlanoFinanceiroCursoAtual.descricao\", ");
		sqlStr.append(" turmaRenovar.identificadorTurma as \"turmaRenovar.identificadorTurma\", ");
		sqlStr.append(" processoMatriculaRenovar.descricao as \"processoMatriculaRenovar.descricao\", ");
		sqlStr.append(" condicaoPagamentoPlanoFinanceiroCursoRenovar.descricao as \"condicaoPagamentoPlanoFinanceiroCursoRenovar.descricao\", ");
		sqlStr.append(" periodoLetivo.periodoLetivo as \"periodoLetivo.periodoLetivo\", ");
		sqlStr.append(" periodoLetivo.descricao as \"periodoLetivo.descricao\", ");
		sqlStr.append(" gradecurricularatual.nome as \"gradecurricularatual.nome\" ");
		sqlStr.append(" FROM RenovacaoMatriculaTurma ");
		sqlStr.append(" left JOIN UnidadeEnsino on UnidadeEnsino.codigo = RenovacaoMatriculaTurma.UnidadeEnsino ");
		sqlStr.append(" left JOIN Curso on curso.codigo = RenovacaoMatriculaTurma.curso ");
		sqlStr.append(" left JOIN Turma on turma.codigo = RenovacaoMatriculaTurma.turma ");
		sqlStr.append(" left JOIN Curso turma_curso on turma_curso.codigo = Turma.curso ");
		sqlStr.append(" left JOIN UnidadeEnsino turma_unidadeensino on turma_unidadeensino.codigo = Turma.UnidadeEnsino ");
		sqlStr.append(" left JOIN Turno on Turno.codigo = Turma.Turno ");
		sqlStr.append(" left JOIN gradecurricular as gradecurricularatual on gradecurricularatual.codigo = RenovacaoMatriculaTurma.gradecurricularatual ");
		sqlStr.append(" left JOIN Usuario as responsavel on responsavel.codigo = RenovacaoMatriculaTurma.responsavel ");
		sqlStr.append(" left JOIN planoFinanceiroCurso as planoFinanceiroCursoAtual on planoFinanceiroCursoAtual.codigo = RenovacaoMatriculaTurma.planoFinanceiroCursoAtual ");
		sqlStr.append(" left JOIN planoFinanceiroCurso as planoFinanceiroCursoRenovar on planoFinanceiroCursoRenovar.codigo = RenovacaoMatriculaTurma.planoFinanceiroCursoRenovar ");
		sqlStr.append(" left JOIN condicaoPagamentoPlanoFinanceiroCurso as condicaoPagamentoPlanoFinanceiroCursoAtual on condicaoPagamentoPlanoFinanceiroCursoAtual.codigo = RenovacaoMatriculaTurma.condicaoPagamentoPlanoFinanceiroCursoAtual ");
		sqlStr.append(" left JOIN turma as turmaRenovar on turmaRenovar.codigo = RenovacaoMatriculaTurma.turmaRenovar ");
		sqlStr.append(" left JOIN processoMatricula as processoMatriculaRenovar on processoMatriculaRenovar.codigo = RenovacaoMatriculaTurma.processoMatriculaRenovar ");
		sqlStr.append(" left JOIN condicaoPagamentoPlanoFinanceiroCurso as condicaoPagamentoPlanoFinanceiroCursoRenovar on condicaoPagamentoPlanoFinanceiroCursoRenovar.codigo = RenovacaoMatriculaTurma.condicaoPagamentoPlanoFinanceiroCursoRenovar ");
		sqlStr.append(" left JOIN periodoLetivo on periodoLetivo.codigo = RenovacaoMatriculaTurma.periodoLetivoRenovar ");

		return sqlStr;
	}

	public List<RenovacaoMatriculaTurmaVO> consultarPorCurso(String valorConsulta, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		consultar(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaBasica());
		sqlStr.append(" WHERE Curso.nome like '%").append(valorConsulta).append("%' ");
		sqlStr.append(" ORDER BY Curso.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}

	private List<RenovacaoMatriculaTurmaVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<RenovacaoMatriculaTurmaVO> objs = new ArrayList<RenovacaoMatriculaTurmaVO>(0);
		while (rs.next()) {
			RenovacaoMatriculaTurmaVO obj = new RenovacaoMatriculaTurmaVO();
			montarDados(obj, rs, nivelMontarDados, usuarioVO);
			objs.add(obj);
		}
		return objs;
	}

	private void montarDados(RenovacaoMatriculaTurmaVO obj, SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		obj.setNovoObj(false);
		obj.setCodigo(rs.getInt("codigo"));
		obj.getGradeCurricularAtual().setCodigo(rs.getInt("gradeCurricularAtual"));
		obj.getGradeCurricularAtual().setNome(rs.getString("gradeCurricularAtual.nome"));
		obj.setAprovadoSemestre(rs.getString("aprovadoSemestre"));
		obj.setAno(rs.getString("ano"));
		obj.setSemestre(rs.getString("semestre"));
		obj.setData(rs.getTimestamp("data"));
		obj.setDataRenovacao(rs.getDate("dataRenovacao"));
		obj.setRenovarApenasAlunosRenovacaoAutomatica(rs.getBoolean("renovarApenasAlunosRenovacaoAutomatica"));
		obj.setLiberadoInclusaoTurmaOutroUnidadeEnsino(rs.getBoolean("liberadoInclusaoTurmaOutroUnidadeEnsino"));
		obj.setLiberadoInclusaoTurmaOutroCurso(rs.getBoolean("liberadoInclusaoTurmaOutroCurso"));
		obj.setLiberadoInclusaoTurmaOutroMatrizCurricular(rs.getBoolean("liberadoInclusaoTurmaOutroMatrizCurricular"));
		obj.getUnidadeEnsinoVO().setCodigo(rs.getInt("unidadeensino.codigo"));
		obj.getUnidadeEnsinoVO().setNome(rs.getString("unidadeensino.nome"));
		
		obj.getCursoVO().setCodigo(rs.getInt("curso.codigo"));
		obj.getCursoVO().setNome(rs.getString("curso.nome"));
		obj.getCursoVO().setPeriodicidade(rs.getString("curso.periodicidade"));
		
		obj.getTurmaVO().setCodigo(rs.getInt("turma"));
		obj.getResponsavel().setCodigo(rs.getInt("responsavel"));
		obj.getTurmaVO().setCodigo(rs.getInt("turma"));
		obj.getTurmaVO().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
		obj.getTurmaVO().getUnidadeEnsino().setCodigo(rs.getInt("turma_unidadeensino.codigo"));
		obj.getTurmaVO().getUnidadeEnsino().setNome(rs.getString("turma_unidadeensino.nome"));
		obj.getTurmaVO().getTurno().setCodigo(rs.getInt("turno.codigo"));
		obj.getTurmaVO().getTurno().setNome(rs.getString("turno.nome"));
		obj.getTurmaVO().getCurso().setCodigo(rs.getInt("turma_curso.codigo"));
		obj.getTurmaVO().getCurso().setNome(rs.getString("turma_curso.nome"));
		obj.getTurmaVO().getCurso().setPeriodicidade(rs.getString("turma_curso.periodicidade"));
		obj.getResponsavel().setCodigo(rs.getInt("responsavel"));
		obj.getResponsavel().setNome(rs.getString("responsavel.nome"));
		obj.setDataInicioProcessamento(rs.getDate("dataInicioProcessamento"));
		obj.setDataTerminoProcessamento(rs.getDate("dataTerminoProcessamento"));
//		obj.getPlanoFinanceiroCursoAtual().setCodigo(rs.getInt("planoFinanceiroCursoAtual"));
//		obj.getPlanoFinanceiroCursoAtual().setDescricao(rs.getString("planoFinanceiroCursoAtual.descricao"));
//		obj.getPlanoFinanceiroCursoRenovar().setCodigo(rs.getInt("planoFinanceiroCursoRenovar"));
//		obj.getPlanoFinanceiroCursoRenovar().setDescricao(rs.getString("planoFinanceiroCursoRenovar.descricao"));
//		obj.getCondicaoPagamentoPlanoFinanceiroCursoAtual().setCodigo(rs.getInt("condicaoPagamentoPlanoFinanceiroCursoAtual"));
//		obj.getCondicaoPagamentoPlanoFinanceiroCursoAtual().setDescricao(rs.getString("condicaoPagamentoPlanoFinanceiroCursoAtual.descricao"));
		obj.getTurmaRenovar().setCodigo(rs.getInt("turmaRenovar"));
		obj.getTurmaRenovar().setIdentificadorTurma(rs.getString("turmaRenovar.identificadorTurma"));
		obj.getPeriodoLetivoRenovar().setCodigo(rs.getInt("periodoLetivoRenovar"));
		obj.getPeriodoLetivoRenovar().setPeriodoLetivo(rs.getInt("periodoLetivo.periodoLetivo"));
		obj.getPeriodoLetivoRenovar().setDescricao(rs.getString("periodoLetivo.descricao"));
		obj.getProcessoMatriculaRenovar().setCodigo(rs.getInt("processoMatriculaRenovar"));
		obj.getProcessoMatriculaRenovar().setDescricao(rs.getString("processoMatriculaRenovar.descricao"));
//		obj.getCondicaoPagamentoPlanoFinanceiroCursoRenovar().setCodigo(rs.getInt("condicaoPagamentoPlanoFinanceiroCursoRenovar"));
//		obj.getCondicaoPagamentoPlanoFinanceiroCursoRenovar().setDescricao(rs.getString("condicaoPagamentoPlanoFinanceiroCursoRenovar.descricao"));
//		obj.setManterCondicaoPagamentoAtual(rs.getBoolean("manterCondicaoPagamentoAtual"));
		if (rs.getString("situacao") != null) {
			obj.setSituacao(SituacaoRenovacaoTurmaEnum.valueOf(rs.getString("situacao")));
		}
		obj.setQtdeRenovacaoAGerada(rs.getInt("qtdeRenovacaoAGerada"));
		obj.setQtdeRenovacaoErro(rs.getInt("qtdeRenovacaoErro"));
		obj.setQtdeRenovacaoGerada(rs.getInt("qtdeRenovacaoGerada"));
		obj.setMensagemErro(rs.getString("mensagemErro"));
		if(obj.getApresentarAcompanhamentoProcessamento()){
			obj.iniciar(Long.valueOf((obj.getQtdeRenovacaoGerada()+obj.getQtdeRenovacaoErro())), obj.getQtdeRenovacaoAGerada(), obj.getLabelProcessamento(), false, null, null);
		}		
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setRenovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs(getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().consultarPorRenovacaoMatriculaTurmaESituacao(obj.getCodigo(), SituacaoRenovacaoMatriculaPeriodoEnum.AGUARDANDO_REALIZACAO, Uteis.NIVELMONTARDADOS_TODOS, false, usuarioVO, 0, 0));
			obj.setRenovacaoMatriculaTurmaMatriculaPeriodoSucessoVOs(getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().consultarPorRenovacaoMatriculaTurmaESituacao(obj.getCodigo(), SituacaoRenovacaoMatriculaPeriodoEnum.REALIZADO_SUCESSO, Uteis.NIVELMONTARDADOS_TODOS, false, usuarioVO, 0, 0));
			obj.setRenovacaoMatriculaTurmaMatriculaPeriodoErroVOs(getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().consultarPorRenovacaoMatriculaTurmaESituacao(obj.getCodigo(), SituacaoRenovacaoMatriculaPeriodoEnum.REALIZADO_ERRO, Uteis.NIVELMONTARDADOS_TODOS, false, usuarioVO, 0, 0));
		}

	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "RenovarMatriculaPorTurma";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		RenovacaoMatriculaTurma.idEntidade = idEntidade;
	}
	

	@Override
	public void realizarInicializacaoThreadRenovacaoTurma(RenovacaoMatriculaTurmaVO obj, AplicacaoControle aplicacaoControle, UsuarioVO usuarioVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception {
		if(obj.getSituacao().equals(SituacaoRenovacaoTurmaEnum.EM_PROCESSAMENTO)){
			Thread jobRenovar = new Thread(new JobRenovacaoTurma(aplicacaoControle, obj, gradeDisciplinaCompostaVOs));
			aplicacaoControle.executarBloqueioTurmaNaRenovacaoTurma(obj.getCodigo(), jobRenovar, true, false, true, false);
		}
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarInicializacaoProcessamento(RenovacaoMatriculaTurmaVO obj, AplicacaoControle aplicacaoControle, boolean controlarAcesso, UsuarioVO usuarioVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception {
		SituacaoRenovacaoTurmaEnum situacao = obj.getSituacao();
		boolean isNovo = obj.getNovoObj();
		try {
			
			for (Iterator<RenovacaoMatriculaTurmaMatriculaPeriodoVO> iterator = obj.getRenovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs().iterator(); iterator.hasNext();) {
				RenovacaoMatriculaTurmaMatriculaPeriodoVO notaSaidaVO = (RenovacaoMatriculaTurmaMatriculaPeriodoVO) iterator.next();
				if (!notaSaidaVO.getSelecionado()) {
					iterator.remove();
				}
			}
			obj.setSituacao(SituacaoRenovacaoTurmaEnum.EM_PROCESSAMENTO);
			obj.getResponsavel().setCodigo(usuarioVO.getCodigo());
			obj.getResponsavel().setNome(usuarioVO.getNome());
			if (!obj.getNovoObj()) {
				alterar(obj, controlarAcesso, usuarioVO, gradeDisciplinaCompostaVOs);
			} else {				
				obj.setDataInicioProcessamento(new Date());
				incluir(obj, controlarAcesso, usuarioVO, gradeDisciplinaCompostaVOs);
			}			
			realizarInicializacaoThreadRenovacaoTurma(obj, aplicacaoControle, usuarioVO, gradeDisciplinaCompostaVOs);
		} catch (Exception e) {
			obj.setSituacao(situacao);
			obj.setNovoObj(isNovo);
			if (isNovo) {
				obj.setCodigo(0);
			}
			throw e;
		}
	}

	@Override
	public void realizarInterrupcaoProcessamento(RenovacaoMatriculaTurmaVO obj, AplicacaoControle aplicacaoControle, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		realizarAtualizacaoDadosProcessamento(obj, usuarioVO);
		if (obj.getSituacao().equals(SituacaoRenovacaoTurmaEnum.EM_PROCESSAMENTO)) {
			SituacaoRenovacaoTurmaEnum situacao = obj.getSituacao();
			try {
				obj.setSituacao(SituacaoRenovacaoTurmaEnum.PROCESSAMENTO_INTERROMPIDO);
				obj.getResponsavel().setCodigo(usuarioVO.getCodigo());
				obj.getResponsavel().setNome(usuarioVO.getNome());
				obj.setDataInicioProcessamento(new Date());
				obj.setDataTerminoProcessamento(new Date());
				alterarDadosBasicos(obj, controlarAcesso, usuarioVO);
				aplicacaoControle.executarBloqueioTurmaNaRenovacaoTurma(obj.getCodigo(), null, false, true, false, true);
			} catch (Exception e) {
				obj.setSituacao(situacao);
				throw e;
			}
		}
	}

	@Override
	public boolean realizarVerificacaoRenovacaoTurmaInterrompida(RenovacaoMatriculaTurmaVO obj) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT codigo from RenovacaoMatriculaTurma ");
		sqlStr.append(" WHERE RenovacaoMatriculaTurma.situacao = '").append(SituacaoRenovacaoTurmaEnum.PROCESSAMENTO_INTERROMPIDO.name()).append("' ");
		sqlStr.append(" and RenovacaoMatriculaTurma.codigo = ").append(obj.getCodigo()).append(" ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return rs.next();
	}

	@Override
	public void realizarAtualizacaoDadosProcessamento(RenovacaoMatriculaTurmaVO obj, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaBasica());
		sqlStr.append("WHERE RenovacaoMatriculaTurma.codigo = ").append(obj.getCodigo());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			montarDados(obj, rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		}
	}

}
