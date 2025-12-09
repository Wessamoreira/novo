package negocio.facade.jdbc.ead;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

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

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineProfessorVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.ead.enumeradores.SituacaoProgramacaoTutoriaOnlineEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.ProgramacaoTutoriaOnlineProfessorInterfaceFacade;

/**
 * @author Victor Hugo 11/11/2014
 */
@Repository
@Scope("singleton")
@Lazy
public class ProgramacaoTutoriaOnlineProfessor extends ControleAcesso implements ProgramacaoTutoriaOnlineProfessorInterfaceFacade, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6344105863080744606L;
	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ProgramacaoTutoriaOnlineProfessor.idEntidade = idEntidade;
	}

	public ProgramacaoTutoriaOnlineProfessor() throws Exception {
		super();
		setIdEntidade("ProgramacaoTutoriaOnlineProfessor");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(programacaoTutoriaOnlineProfessorVO);
			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO programacaotutoriaonlineprofessor(ordemprioridade, professor, qtdealunostutoria, dateinativacao, responsavelinativacao, programacaotutoriaonline, situacaoprogramacaotutoriaonline)VALUES (?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			programacaoTutoriaOnlineProfessorVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setInt(1, programacaoTutoriaOnlineProfessorVO.getOrdemPrioridade());
					sqlInserir.setInt(2, programacaoTutoriaOnlineProfessorVO.getProfessor().getCodigo());
					sqlInserir.setInt(3, programacaoTutoriaOnlineProfessorVO.getQtdeAlunosTutoria());
					if (programacaoTutoriaOnlineProfessorVO.getSituacaoProgramacaoTutoriaOnline().equals(SituacaoProgramacaoTutoriaOnlineEnum.INATIVO)) {
						sqlInserir.setTimestamp(4, Uteis.getDataJDBCTimestamp(programacaoTutoriaOnlineProfessorVO.getDateInativacao()));
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (programacaoTutoriaOnlineProfessorVO.getResponsavelInativacao().getCodigo() != 0) {
						sqlInserir.setInt(5, programacaoTutoriaOnlineProfessorVO.getResponsavelInativacao().getCodigo());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (programacaoTutoriaOnlineProfessorVO.getProgramacaoTutoriaOnlineVO().getCodigo() != 0) {
						sqlInserir.setInt(6, programacaoTutoriaOnlineProfessorVO.getProgramacaoTutoriaOnlineVO().getCodigo());
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setString(7, programacaoTutoriaOnlineProfessorVO.getSituacaoProgramacaoTutoriaOnline().name());

					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						programacaoTutoriaOnlineProfessorVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			programacaoTutoriaOnlineProfessorVO.setNovoObj(Boolean.TRUE);
			programacaoTutoriaOnlineProfessorVO.setCodigo(0);
			throw e;
		}
	}

	@Override
	public void validarDados(ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO) throws Exception {

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (programacaoTutoriaOnlineProfessorVO.getCodigo() == 0) {
			incluir(programacaoTutoriaOnlineProfessorVO, verificarAcesso, usuarioVO);
		} else {
			alterar(programacaoTutoriaOnlineProfessorVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE programacaotutoriaonlineprofessor SET ordemprioridade=?, professor=?, qtdealunostutoria=?, dateinativacao=?, responsavelinativacao=?, programacaotutoriaonline=?, situacaoprogramacaotutoriaonline=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);

					sqlAlterar.setInt(1, programacaoTutoriaOnlineProfessorVO.getOrdemPrioridade());
					sqlAlterar.setInt(2, programacaoTutoriaOnlineProfessorVO.getProfessor().getCodigo());
					sqlAlterar.setInt(3, programacaoTutoriaOnlineProfessorVO.getQtdeAlunosTutoria());
					if (programacaoTutoriaOnlineProfessorVO.getSituacaoProgramacaoTutoriaOnline().equals(SituacaoProgramacaoTutoriaOnlineEnum.INATIVO)) {
						sqlAlterar.setTimestamp(4, Uteis.getDataJDBCTimestamp(programacaoTutoriaOnlineProfessorVO.getDateInativacao()));
					} else {
						sqlAlterar.setNull(4, 0);
					}
					if (programacaoTutoriaOnlineProfessorVO.getResponsavelInativacao().getCodigo() != 0) {
						sqlAlterar.setInt(5, programacaoTutoriaOnlineProfessorVO.getResponsavelInativacao().getCodigo());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (programacaoTutoriaOnlineProfessorVO.getProgramacaoTutoriaOnlineVO().getCodigo() != 0) {
						sqlAlterar.setInt(6, programacaoTutoriaOnlineProfessorVO.getProgramacaoTutoriaOnlineVO().getCodigo());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setString(7, programacaoTutoriaOnlineProfessorVO.getSituacaoProgramacaoTutoriaOnline().name());
					sqlAlterar.setInt(8, programacaoTutoriaOnlineProfessorVO.getCodigo());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atutalizarQtdAlunosTutoriaProgramacaoTutoriaOnlineProfessor(final ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE programacaotutoriaonlineprofessor SET qtdealunostutoria = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setInt(1, programacaoTutoriaOnlineProfessorVO.getQtdeAlunosTutoria());
					sqlAlterar.setInt(2, programacaoTutoriaOnlineProfessorVO.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM programacaotutoriaonlineprofessor WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, programacaoTutoriaOnlineProfessorVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ProgramacaoTutoriaOnlineProfessorVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		ProgramacaoTutoriaOnlineProfessorVO obj = new ProgramacaoTutoriaOnlineProfessorVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setOrdemPrioridade(tabelaResultado.getInt("ordemprioridade"));
		obj.getProfessor().setCodigo(tabelaResultado.getInt("professor"));
		obj.setQtdeAlunosTutoria(tabelaResultado.getInt("qtdealunostutoria"));
		obj.setDateInativacao(tabelaResultado.getTimestamp("dateInativacao"));
		obj.getResponsavelInativacao().setCodigo(tabelaResultado.getInt("responsavelinativacao"));
		obj.getProgramacaoTutoriaOnlineVO().setCodigo(tabelaResultado.getInt("programacaotutoriaonline"));
		obj.setSituacaoProgramacaoTutoriaOnline(SituacaoProgramacaoTutoriaOnlineEnum.valueOf(tabelaResultado.getString("situacaoprogramacaotutoriaonline")));
		obj.setQtdeAlunosAtivos(tabelaResultado.getInt("qtdealunoativos"));
		obj.setQtdeAlunosInativos(tabelaResultado.getInt("qtdealunoinativos"));
		
//		if(Uteis.isAtributoPreenchido(tabelaResultado.getInt("classroomGoogle.codigo"))) {
//			obj.getClassroomGoogleVO().setCodigo((tabelaResultado.getInt("classroomGoogle.codigo")));
//			obj.getClassroomGoogleVO().setAno(tabelaResultado.getString("classroomGoogle.ano"));
//			obj.getClassroomGoogleVO().setSemestre(tabelaResultado.getString("classroomGoogle.semestre"));			
//			obj.getClassroomGoogleVO().setLinkClassroom(tabelaResultado.getString("classroomGoogle.linkClassroom"));
//			obj.getClassroomGoogleVO().setIdClassRoomGoogle(tabelaResultado.getString("classroomGoogle.idClassRoomGoogle"));
//			obj.getClassroomGoogleVO().setIdTurma(tabelaResultado.getString("classroomGoogle.idTurma"));
//			obj.getClassroomGoogleVO().setEmailTurma(tabelaResultado.getString("classroomGoogle.emailTurma"));
//			obj.getClassroomGoogleVO().setIdCalendario(tabelaResultado.getString("classroomGoogle.idCalendario"));
//			obj.getClassroomGoogleVO().getProfessorEad().setCodigo((tabelaResultado.getInt("classroomGoogle.professoread")));
//			obj.getClassroomGoogleVO().getTurmaVO().setCodigo((tabelaResultado.getInt("classroomGoogle.turma")));
//			obj.getClassroomGoogleVO().getDisciplinaVO().setCodigo((tabelaResultado.getInt("classroomGoogle.disciplina")));
//		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setProfessor(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getProfessor().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
			return obj;
		}
		return obj;
	}

	@Override
	public List<ProgramacaoTutoriaOnlineProfessorVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<ProgramacaoTutoriaOnlineProfessorVO> vetResultado = new ArrayList<ProgramacaoTutoriaOnlineProfessorVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ProgramacaoTutoriaOnlineProfessorVO> consultarPorProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO obj, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = getSqlConsultaBasica(obj);
		sqlStr.append(" where programacaotutoriaonlineprofessor.programacaotutoriaonline =  ").append(obj.getCodigo());
		sqlStr.append(" order by ordemprioridade ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioLogado);
	}

	@Override
	public void persistirProgramacaoTutoriaOnlineProfessores(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception {
		for (ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO : programacaoTutoriaOnlineVO.getProgramacaoTutoriaOnlineProfessorVOs()) {
			programacaoTutoriaOnlineProfessorVO.setProgramacaoTutoriaOnlineVO(programacaoTutoriaOnlineVO);
			persistir(programacaoTutoriaOnlineProfessorVO, false, usuarioVO);
		}
	}

	@Override
	public List<ProgramacaoTutoriaOnlineProfessorVO> consultarPorProfessoresAtivosAlunosAtivosEQtdeAlunosAtivosMenorQtdeAlunosTutoria(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlConsultaBasica = getSqlConsultaBasica(programacaoTutoriaOnlineVO);
		sqlConsultaBasica.append(" WHERE programacaotutoriaonlineprofessor.programacaotutoriaonline = ").append(programacaoTutoriaOnlineVO.getCodigo());
		StringBuilder sqlStr = new StringBuilder("select * from ( ");
		sqlStr.append(sqlConsultaBasica.toString());
		sqlStr.append(" ) as t where qtdealunoativos < qtdealunostutoria ");
		sqlStr.append(" and situacaoprogramacaotutoriaonline =  '").append(SituacaoProgramacaoTutoriaOnlineEnum.ATIVO.name()).append("'");
		sqlStr.append(" order by ordemprioridade");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<ProgramacaoTutoriaOnlineProfessorVO> lista = new ArrayList<ProgramacaoTutoriaOnlineProfessorVO>();
		while (rs.next()) {
			ProgramacaoTutoriaOnlineProfessorVO obj = new ProgramacaoTutoriaOnlineProfessorVO();
			obj = montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			lista.add(obj);
		}
		return lista;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarAlteracaoTutor(ProgramacaoTutoriaOnlineVO obj, Integer codigoTutor, List<MatriculaPeriodoTurmaDisciplinaVO> lista, UsuarioVO usuarioVO) throws Exception {
		ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO = consultarPorProgramacaoTutoriaOnlineEProfessor(obj, codigoTutor, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
		if (Uteis.isAtributoPreenchido(programacaoTutoriaOnlineProfessorVO.getCodigo())) {
			Iterator<MatriculaPeriodoTurmaDisciplinaVO> i = lista.iterator();
			while (i.hasNext()) {
				MatriculaPeriodoTurmaDisciplinaVO object = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
				if (object.getSelecionarMatricula() && programacaoTutoriaOnlineProfessorVO.isQtdAlunoTutoriaMaiorQueAlunoAtivos()) {
					object.getProfessor().setCodigo(codigoTutor);
					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().incluirProfessorMatriculaPeriodoTurmaDisciplina(object);
					getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().incluirProgramacaoTutoriaOnlineProfessorMatriculaPeriodoTurmaDisciplina(object, programacaoTutoriaOnlineProfessorVO.getCodigo());
					programacaoTutoriaOnlineProfessorVO.setQtdeAlunosAtivos(programacaoTutoriaOnlineProfessorVO.getQtdeAlunosAtivos() + 1);
					i.remove();
				} else if (object.getSelecionarMatricula()) {
					throw new Exception(UteisJSF.internacionalizar("msg_ProgramacaoTutoriaOnline_validarDistribuicaoTutores"));
				}
			}
		} else {
			throw new Exception("Não foi localizado o professor para essa programação de tutoria online.");
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public ProgramacaoTutoriaOnlineProfessorVO consultarPorProgramacaoTutoriaOnlineEProfessor(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, Integer codigoProfessor, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = getSqlConsultaBasica(programacaoTutoriaOnlineVO);
		sqlStr.append(" WHERE programacaotutoriaonlineprofessor.programacaotutoriaonline = ").append(programacaoTutoriaOnlineVO.getCodigo());
		sqlStr.append(" and programacaotutoriaonlineprofessor.professor = ").append(codigoProfessor);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new ProgramacaoTutoriaOnlineProfessorVO();
	}

	public StringBuilder getSqlConsultaBasica(ProgramacaoTutoriaOnlineVO obj) {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select ");
		//sqlStr.append(" programacaotutoriaonlineprofessor.*, ");
		sqlStr.append(" programacaotutoriaonlineprofessor.codigo, ");
		sqlStr.append(" programacaotutoriaonlineprofessor.ordemprioridade, ");
		sqlStr.append(" programacaotutoriaonlineprofessor.professor, ");
		sqlStr.append(" programacaotutoriaonlineprofessor.qtdealunostutoria, ");
		sqlStr.append(" programacaotutoriaonlineprofessor.dateinativacao, ");
		sqlStr.append(" programacaotutoriaonlineprofessor.responsavelinativacao, ");
		sqlStr.append(" programacaotutoriaonlineprofessor.programacaotutoriaonline, ");
		sqlStr.append(" programacaotutoriaonlineprofessor.situacaoprogramacaotutoriaonline, ");
		
		sqlStr.append(" ( select count(matriculaperiodoturmadisciplina.codigo) from matriculaperiodoturmadisciplina  ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo and matriculaperiodo.situacaomatriculaperiodo = 'AT' ");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula and matricula.situacao = 'AT' ");
		sqlStr.append(" left join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" where matriculaperiodoturmadisciplina.professor = programacaotutoriaonlineprofessor.professor ");
		if (Uteis.isAtributoPreenchido(obj.getTurmaVO().getCodigo())) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma = ").append(obj.getTurmaVO().getCodigo());
			;
		}
		if (Uteis.isAtributoPreenchido(obj.getDisciplinaVO().getCodigo())) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(obj.getDisciplinaVO().getCodigo());
			;
		}
		if (Uteis.isAtributoPreenchido(obj.getCursoVO().getCodigo())) {
			sqlStr.append(" and ((turma.turmaagrupada = false and turma.curso = ").append(obj.getCursoVO().getCodigo()).append(" ) ");
			sqlStr.append("     or  ");
			sqlStr.append("     (turma.turmaagrupada and ").append(obj.getCursoVO().getCodigo()).append(" in (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo ))) ");
		}
		if (!Uteis.isAtributoPreenchido(obj.getTurmaVO().getCodigo()) && Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo())) {
			sqlStr.append(" and matricula.unidadeensino = ").append(obj.getUnidadeEnsinoVO().getCodigo());
		}
		sqlStr.append(" ) as qtdealunoativos, ");
		
		sqlStr.append(" ( select count(matriculaperiodoturmadisciplina.codigo) from matriculaperiodoturmadisciplina  ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo and matriculaperiodo.situacaomatriculaperiodo <> 'AT' ");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula");
		sqlStr.append(" left join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" where matriculaperiodoturmadisciplina.professor = programacaotutoriaonlineprofessor.professor ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.programacaotutoriaonlineprofessor = programacaotutoriaonlineprofessor.codigo ");
		if (Uteis.isAtributoPreenchido(obj.getTurmaVO().getCodigo())) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.turma = ").append(obj.getTurmaVO().getCodigo());
			;
		}
		if (Uteis.isAtributoPreenchido(obj.getDisciplinaVO().getCodigo())) {
			sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(obj.getDisciplinaVO().getCodigo());
			;
		}
		if (Uteis.isAtributoPreenchido(obj.getCursoVO().getCodigo())) {
			sqlStr.append(" and ((turma.turmaagrupada = false and turma.curso = ").append(obj.getCursoVO().getCodigo()).append(" ) ");
			sqlStr.append("     or  ");
			sqlStr.append("     (turma.turmaagrupada and ").append(obj.getCursoVO().getCodigo()).append(" in (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo ))) ");
		}
		if (!Uteis.isAtributoPreenchido(obj.getTurmaVO().getCodigo()) && Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO().getCodigo())) {
			sqlStr.append(" and matricula.unidadeensino = ").append(obj.getUnidadeEnsinoVO().getCodigo());
		}
		sqlStr.append(" ) as qtdealunoinativos ");
		
		sqlStr.append(" from programacaotutoriaonlineprofessor   ");
		sqlStr.append(" inner join programacaotutoriaonline on programacaotutoriaonlineprofessor.programacaotutoriaonline = programacaotutoriaonline.codigo ");
		sqlStr.append(" left join classroomgoogle  on classroomGoogle.professoread = programacaotutoriaonlineprofessor.professor "); 
		sqlStr.append(" and classroomGoogle.ano  = programacaotutoriaonline.ano  and classroomGoogle.semestre  = programacaotutoriaonline.semestre ");  
		sqlStr.append(" and classroomGoogle.disciplina = programacaotutoriaonline.disciplina and classroomGoogle.turma  = programacaotutoriaonline.turma  ");
		return sqlStr;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<ProgramacaoTutoriaOnlineProfessorVO> consultarTutorSemProgramacaoTutoriaOnline(ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, int nivelMontarDados, UsuarioVO usuarioVO)  throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select null as codigo,  0 as \"classroomGoogle.codigo\", null as ordemprioridade, null as dateInativacao, null as responsavelinativacao, ");
		sqlStr.append(" programacaotutoriaonline.qtdealunos as qtdealunostutoria, programacaotutoriaonline.codigo as programacaotutoriaonline, programacaotutoriaonline.situacao as situacaoprogramacaotutoriaonline,");
		sqlStr.append(" count (*) as qtdealunoativos, 0 as qtdealunoinativos, matriculaperiodoturmadisciplina.professor");
		sqlStr.append(" from matriculaperiodoturmadisciplina");
		sqlStr.append(" LEFT JOIN programacaotutoriaonlineprofessor on matriculaperiodoturmadisciplina.professor = programacaotutoriaonlineprofessor.professor");
		sqlStr.append(" INNER JOIN matriculaperiodo ON matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
		sqlStr.append(" INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula");
		sqlStr.append(" INNER join pessoa as professor on matriculaperiodoturmadisciplina.professor = professor.codigo");
		sqlStr.append(" INNER JOIN turmadisciplina ON turmadisciplina.turma = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" INNER JOIN programacaotutoriaonline on programacaotutoriaonline.codigo = ").append(programacaoTutoriaOnlineVO.getCodigo()).append(" AND turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" where 1=1");
		sqlStr.append(" and programacaotutoriaonlineprofessor.professor is null");
		sqlStr.append(" AND matriculaperiodoturmadisciplina.turma = ").append(programacaoTutoriaOnlineVO.getTurmaVO().getCodigo());
		sqlStr.append(" AND matriculaperiodoturmadisciplina.disciplina = ").append(programacaoTutoriaOnlineVO.getDisciplinaVO().getCodigo());
		sqlStr.append(" and matriculaperiodoturmadisciplina.professor is not null ");
		sqlStr.append(" AND matricula.unidadeensino =  ").append(programacaoTutoriaOnlineVO.getUnidadeEnsinoVO().getCodigo());
		sqlStr.append(" GROUP BY programacaotutoriaonline.qtdealunos,programacaotutoriaonline.codigo,matriculaperiodoturmadisciplina.professor");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<ProgramacaoTutoriaOnlineProfessorVO> programacaoTutoriaOnlineProfessorVOs = new ArrayList<ProgramacaoTutoriaOnlineProfessorVO>();
		programacaoTutoriaOnlineProfessorVOs.addAll(montarDadosConsulta(rs, nivelMontarDados, usuarioVO));
		if (!programacaoTutoriaOnlineProfessorVOs.isEmpty()) {
			int ordem = programacaoTutoriaOnlineVO.getProgramacaoTutoriaOnlineProfessorVOs().size();
			for (ProgramacaoTutoriaOnlineProfessorVO obj : programacaoTutoriaOnlineProfessorVOs) {
				obj.setOrdemPrioridade(++ordem);
				incluir(obj, false, usuarioVO);
			}
			return programacaoTutoriaOnlineProfessorVOs;
		}
		return null;
	}

	public void realizarDefinicaoOrdemPrioridadeProgramacaoTutoriaOnlineProfessores(List<ProgramacaoTutoriaOnlineProfessorVO> programacaoTutoriaOnlineProfessorVOs) {
		if (Uteis.isAtributoPreenchido(programacaoTutoriaOnlineProfessorVOs)) {
			programacaoTutoriaOnlineProfessorVOs.sort(Comparator.comparing(ProgramacaoTutoriaOnlineProfessorVO::getOrdemPrioridade));
			IntStream.range(0, programacaoTutoriaOnlineProfessorVOs.size()).forEach(i -> programacaoTutoriaOnlineProfessorVOs.get(i).setOrdemPrioridade(i + 1));
		}
	}

	public void alterarOrdemPrioridadeProgramacaoTutoriaOnlineProfessores(List<ProgramacaoTutoriaOnlineProfessorVO> programacaoTutoriaOnlineProfessorVOs) throws Exception {
		if (Uteis.isAtributoPreenchido(programacaoTutoriaOnlineProfessorVOs)) {
			programacaoTutoriaOnlineProfessorVOs.forEach(this::alterarOrdemPrioridadeProgramacaoTutoriaOnlineProfessor);
		}
	}

	private void alterarOrdemPrioridadeProgramacaoTutoriaOnlineProfessor(ProgramacaoTutoriaOnlineProfessorVO programacaoTutoriaOnlineProfessorVO) throws StreamSeiException {
		if (Uteis.isAtributoPreenchido(programacaoTutoriaOnlineProfessorVO)) {
			try {
				StringBuilder sqlAlterar = new StringBuilder("UPDATE programacaotutoriaonlineprofessor SET ordemprioridade = ? WHERE codigo = ? ");
				getConexao().getJdbcTemplate().update(sqlAlterar.toString(), new Object[] { programacaoTutoriaOnlineProfessorVO.getOrdemPrioridade(), programacaoTutoriaOnlineProfessorVO.getCodigo() });
			} catch (Exception e) {
				throw new StreamSeiException(e);
			}
		}
	}
}
