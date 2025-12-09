package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jakarta.faces. model.SelectItem;

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

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
//import negocio.comuns.academico.LocalAulaVO;
//import negocio.comuns.academico.MapaLocalAulaTurmaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
//import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.academico.TurmaDisciplinaCompostaVO;
import negocio.comuns.academico.TurmaDisciplinaEstatisticaAlunoVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.academico.enumeradores.TipoEstatisticaTurmaDisciplinaEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TurmaDisciplinaInterfaceFacade;
import webservice.servicos.MatriculaRSVO;
import webservice.servicos.objetos.DisciplinaRSVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>TurmaDisciplinaVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>TurmaDisciplinaVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see TurmaDisciplinaVO
 * @see ControleAcesso
 * @see Turma
 */
@Repository
@Scope("singleton")
@Lazy
public class TurmaDisciplina extends ControleAcesso implements TurmaDisciplinaInterfaceFacade {

	/**
     * 
     */
	private static final long serialVersionUID = -8460825620563677931L;
	protected static String idEntidade;

	public TurmaDisciplina() throws Exception {
		super();
		setIdEntidade("Turma");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>TurmaVO</code>.
	 */
	public TurmaDisciplinaVO novo() throws Exception {
		TurmaDisciplina.incluir(getIdEntidade());
		TurmaDisciplinaVO obj = new TurmaDisciplinaVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>TurmaDisciplinaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TurmaDisciplinaVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TurmaDisciplinaVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			TurmaDisciplinaVO.validarDados(obj);
			final String sql = "INSERT INTO TurmaDisciplina( turma, disciplina, nrAlunosMatriculados, nrMaximoMatricula, " + "nrVagasMatricula, modalidadeDisciplina, localAula, salaLocalAula, " + "disciplinaEquivalenteTurmaAgrupada, configuracaoAcademico," + "gradeDisciplina, disciplinaReferenteAUmGrupoOptativa, gradeCurricularGrupoOptativaDisciplina, avaliacao, definicoesTutoriaOnline, ordemestudoonline, permiteReposicao, permiteApoioPresencial) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getTurma().intValue() != 0) {
						sqlInserir.setInt(1, obj.getTurma().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					sqlInserir.setInt(2, obj.getDisciplina().getCodigo().intValue());
					sqlInserir.setInt(3, obj.getNrAlunosMatriculados().intValue());
					sqlInserir.setInt(4, obj.getNrMaximoMatricula().intValue());
					sqlInserir.setInt(5, obj.getNrVagasMatricula().intValue());
					sqlInserir.setString(6, obj.getModalidadeDisciplina().name());
//					if (obj.getLocalAula().getCodigo() > 0) {
//						sqlInserir.setInt(7, obj.getLocalAula().getCodigo());
//					} else {
//						sqlInserir.setNull(7, 0);
//					}
//					if (obj.getSalaLocalAula().getCodigo() > 0) {
//						sqlInserir.setInt(8, obj.getSalaLocalAula().getCodigo());
//					} else {
//						sqlInserir.setNull(8, 0);
//					}
					if (obj.getDisciplinaEquivalenteTurmaAgrupada().getCodigo() > 0) {
						sqlInserir.setInt(9, obj.getDisciplinaEquivalenteTurmaAgrupada().getCodigo());
					} else {
						sqlInserir.setNull(9, 0);
					}
					if (obj.getConfiguracaoAcademicoVO().getCodigo() > 0) {
						sqlInserir.setInt(10, obj.getConfiguracaoAcademicoVO().getCodigo());
					} else {
						sqlInserir.setNull(10, 0);
					}

					if (obj.getGradeDisciplinaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(11, obj.getGradeDisciplinaVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(11, 0);
					}
					sqlInserir.setBoolean(12, obj.getDisciplinaReferenteAUmGrupoOptativa());
					if (obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(13, obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().intValue());
					} else {
						sqlInserir.setNull(13, 0);
					}
					sqlInserir.setDouble(14, obj.getAvaliacao());
					if(obj.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.PRESENCIAL)) {
						obj.setDefinicoesTutoriaOnline(DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA);
					}
					sqlInserir.setString(15, obj.getDefinicoesTutoriaOnline().name());
					sqlInserir.setInt(16, obj.getOrdemEstudoOnline());
					sqlInserir.setBoolean(17, obj.getPermiteReposicao().booleanValue());
					sqlInserir.setBoolean(18, obj.getPermiteApoioPresencial().booleanValue());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getTurmaDisciplinaCompostaFacade().persistirTurmaDisciplinaVOs(obj, false, usuarioVO);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>TurmaDisciplinaVO</code>. Sempre utiliza a chave primária da classe
	 * como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TurmaDisciplinaVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TurmaDisciplinaVO obj, UsuarioVO usuarioVO) throws Exception {

		try {
			TurmaDisciplinaVO.validarDados(obj);
			final String sql = "UPDATE TurmaDisciplina set turma=?, disciplina=?, nrAlunosMatriculados=?, " + "nrMaximoMatricula = ?, nrVagasMatricula =?, modalidadeDisciplina = ?, " + "disciplinaEquivalenteTurmaAgrupada=?, configuracaoAcademico=?," + "gradeDisciplina=?, disciplinaReferenteAUmGrupoOptativa=?, gradeCurricularGrupoOptativaDisciplina=?, avaliacao=?, definicoesTutoriaOnline = ?, ordemestudoonline = ?, permiteReposicao = ?, permiteApoioPresencial=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getTurma().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getTurma().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setInt(2, obj.getDisciplina().getCodigo().intValue());
					sqlAlterar.setInt(3, obj.getNrAlunosMatriculados().intValue());
					sqlAlterar.setInt(4, obj.getNrMaximoMatricula().intValue());
					sqlAlterar.setInt(5, obj.getNrVagasMatricula().intValue());
					sqlAlterar.setString(6, obj.getModalidadeDisciplina().name());
					// if (obj.getLocalAula().getCodigo() > 0) {
					// sqlAlterar.setInt(7, obj.getLocalAula().getCodigo());
					// } else {
					// sqlAlterar.setNull(7, 0);
					// }
					// if (obj.getSalaLocalAula().getCodigo() > 0) {
					// sqlAlterar.setInt(8, obj.getSalaLocalAula().getCodigo());
					// } else {
					// sqlAlterar.setNull(8, 0);
					// }
					if (obj.getDisciplinaEquivalenteTurmaAgrupada().getCodigo() > 0) {
						sqlAlterar.setInt(7, obj.getDisciplinaEquivalenteTurmaAgrupada().getCodigo());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					if (obj.getConfiguracaoAcademicoVO().getCodigo() > 0) {
						sqlAlterar.setInt(8, obj.getConfiguracaoAcademicoVO().getCodigo());
					} else {
						sqlAlterar.setNull(8, 0);
					}

					if (obj.getGradeDisciplinaVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(9, obj.getGradeDisciplinaVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(9, 0);
					}
					sqlAlterar.setBoolean(10, obj.getDisciplinaReferenteAUmGrupoOptativa());
					if (obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(11, obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(11, 0);
					}
					sqlAlterar.setDouble(12, 0.0); // TODO

					if(obj.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.PRESENCIAL)) {
						obj.setDefinicoesTutoriaOnline(DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA);
					}
					sqlAlterar.setString(13, obj.getDefinicoesTutoriaOnline().name());
					sqlAlterar.setInt(14, obj.getOrdemEstudoOnline());
					sqlAlterar.setBoolean(15, obj.getPermiteReposicao().booleanValue());
					sqlAlterar.setBoolean(16, obj.getPermiteApoioPresencial().booleanValue());
					sqlAlterar.setInt(17, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			getFacadeFactory().getTurmaDisciplinaCompostaFacade().persistirTurmaDisciplinaVOs(obj, false, usuarioVO);
			incluirLogAlteracaoLocalAula(obj, new UsuarioVO());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarLocalSala(final TurmaDisciplinaVO obj, UsuarioVO usuario) throws Exception {

		try {
			TurmaDisciplinaVO.validarDadosLocalAula(obj);
			final String sql = "UPDATE TurmaDisciplina set localAula = ?, salaLocalAula = ?, avaliacao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
//					if (obj.getLocalAula().getCodigo() > 0) {
//						sqlAlterar.setInt(1, obj.getLocalAula().getCodigo());
//					} else {
//						sqlAlterar.setNull(1, 0);
//					}
//					if (obj.getSalaLocalAula().getCodigo() > 0) {
//						sqlAlterar.setInt(2, obj.getSalaLocalAula().getCodigo());
//					} else {
//						sqlAlterar.setNull(2, 0);
//					}
					sqlAlterar.setDouble(3, obj.getAvaliacao());
					sqlAlterar.setInt(4, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
			incluirLogAlteracaoLocalAula(obj, usuario);
		} catch (Exception e) {
			throw e;
		}
	}

	public void incluirLogAlteracaoLocalAula(final TurmaDisciplinaVO obj, final UsuarioVO usuario) throws Exception {
		try {
			final String sql = "INSERT INTO LogTurmaDisciplina( localAula , salaLocalAula, usuario, data, turma, disciplina, avaliacao ) VALUES ( ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			Integer codigo = (getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
//					if (obj.getLocalAula().getCodigo() > 0) {
//						sqlInserir.setString(1, obj.getLocalAula().getCodigo() + " - " + obj.getLocalAula().getLocal());
//					} else {
//						sqlInserir.setNull(1, 0);
//					}
//					if (obj.getSalaLocalAula().getCodigo() > 0) {
//						sqlInserir.setString(2, obj.getSalaLocalAula().getCodigo() + " - " + obj.getSalaLocalAula().getSala());
//					} else {
//						sqlInserir.setNull(2, 0);
//					}
					sqlInserir.setInt(3, usuario.getCodigo());
					sqlInserir.setDate(4, Uteis.getDataJDBC(new Date()));
					if (obj.getTurma() != null) {
						sqlInserir.setInt(5, obj.getTurma());
					} else {
						sqlInserir.setNull(5, 0);
					}
					sqlInserir.setString(6, obj.getDisciplina().getCodigo() + " - " + obj.getDisciplina().getNome());
					sqlInserir.setDouble(7, obj.getAvaliacao());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarNrAlunosMatriculados(TurmaDisciplinaVO turmaDisciplina, UsuarioVO usuarioVO) throws Exception {
		String sqlStr = "UPDATE turmaDisciplina SET nrAlunosMatriculados = ? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { turmaDisciplina.getNrAlunosMatriculados(), turmaDisciplina.getCodigo() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.TurmaDisciplinaInterfaceFacade#
	 * zerarAlunosMatriculadosTurma(java.lang.Integer)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void zerarAlunosMatriculadosTurma(final Integer turma) throws Exception {
		try {
			final String sql = "UPDATE TurmaDisciplina set nrAlunosMatriculados=? WHERE ((turma = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, 0);
					sqlAlterar.setInt(2, turma);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegral(Integer turma, Integer disciplina, Integer gradeDisciplina, Integer gradeCurricularGrupoOptativaDisciplina, UsuarioVO usuario) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("update turmadisciplina set ");
		if(Uteis.isAtributoPreenchido(gradeDisciplina)){
			sqlStr.append(" gradeDisciplina = ").append(gradeDisciplina).append(", ");
			sqlStr.append(" disciplinaReferenteAUmGrupoOptativa = false , ");
			sqlStr.append(" gradeCurricularGrupoOptativaDisciplina = null ");	
		}
		if(Uteis.isAtributoPreenchido(gradeCurricularGrupoOptativaDisciplina)){
			sqlStr.append(" gradeCurricularGrupoOptativaDisciplina = ").append(gradeCurricularGrupoOptativaDisciplina).append(", ");
			sqlStr.append(" disciplinaReferenteAUmGrupoOptativa = true , ");
			sqlStr.append(" gradeDisciplina = null ");
		}
		sqlStr.append(" where turma =").append(turma).append(" ");
		sqlStr.append(" and disciplina = ").append(disciplina).append(" ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}


	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>TurmaVO</code>. Sempre localiza o registro a ser excluído através
	 * da chave primária da entidade. Primeiramente verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TurmaVO</code> que será removido no
	 *            banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TurmaDisciplinaVO obj) throws Exception {
		try {
			TurmaDisciplina.excluir(getIdEntidade());
			String sql = "DELETE FROM TurmaDisciplina WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
			incluirLogAlteracaoLocalAula(obj, new UsuarioVO());
		} catch (Exception e) {
			throw e;
		}
	}

	public void excluirPorCodigoDisciplinaTurma(Integer turma, Integer disciplina, UsuarioVO usuario) throws Exception {
		try {
			TurmaDisciplina.excluir(getIdEntidade());
			String sql = "DELETE FROM TurmaDisciplina WHERE (turma = ?) AND (disciplina = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { turma, disciplina });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>TurmaVO</code>. Sempre localiza o registro a ser excluído através
	 * da chave primária da entidade. Primeiramente verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>Integer turma</code> que será removido
	 *            no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTurmaDisciplinas(Integer turma, UsuarioVO usuario) throws Exception {
		try {
			TurmaDisciplina.excluir(getIdEntidade());
			String sql = "DELETE FROM TurmaDisciplina WHERE (turma = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { turma });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>TurmaDisciplinaVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>Integer turma, List objetos</code> que
	 *            será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTurmaDisciplinas(Integer turma, List<TurmaDisciplinaVO> objetos, UsuarioVO usuario) throws Exception {
		try {
			String sql = "DELETE FROM TurmaDisciplina WHERE (turma = ?)";
			Iterator<TurmaDisciplinaVO> i = objetos.iterator();
			while (i.hasNext()) {
				TurmaDisciplinaVO turmaDisciplina = (TurmaDisciplinaVO) i.next();
				if (turmaDisciplina.getCodigo().intValue() != 0) {
					sql += " and codigo != " + turmaDisciplina.getCodigo().intValue();
				}
				incluirLogAlteracaoLocalAula(turmaDisciplina, usuario);
			}
			getConexao().getJdbcTemplate().update(sql + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), new Object[] { turma });
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * Responsável por realizar uma consulta de <code>TurmaDisciplinaVO</code>
	 * através do valor do atributo <code>Integer valorConsulta</code>. Retorna
	 * os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso Indica se a aplicação deverá verificar se o
	 * usuário possui permissão para esta consulta ou não.
	 * 
	 * @return List Contendo vários objetos da classe <code>TurmaVO</code>
	 * resultantes da consulta.
	 * 
	 * @exception Exception Caso haja problemas de conexão ou restrição de
	 * acesso.
	 */
	public List<TurmaDisciplinaVO> consultarPorDisciplina(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TurmaDisciplina WHERE disciplina = " + valorConsulta.intValue() + " ORDER BY disciplina";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	
	/*
	 * Responsável por realizar uma consulta de <code>TurmaDisciplinaVO</code>
	 * através do valor do atributo <code>Integer valorConsulta</code>. Retorna
	 * os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso Indica se a aplicação deverá verificar se o
	 * usuário possui permissão para esta consulta ou não.
	 * 
	 * @return List Contendo vários objetos da classe <code>TurmaVO</code>
	 * resultantes da consulta.
	 * 
	 * @exception Exception Caso haja problemas de conexão ou restrição de
	 * acesso.
	 */
	public List<TurmaDisciplinaVO> consultarPorCodigoTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT TurmaDisciplina.*, turma.identificadorTurma as \"identificadorTurma\" " + " FROM TurmaDisciplina " + " INNER JOIN Turma ON (Turma.codigo = TurmaDisciplina.turma) " + " inner join Disciplina on Disciplina.codigo =  TurmaDisciplina.disciplina " + " WHERE turma = " + valorConsulta.intValue() + " ORDER BY disciplina.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<TurmaDisciplinaVO> consultarPorCodigoUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		//getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT TurmaDisciplina.*, turma.identificadorTurma as \"identificadorTurma\" " + " FROM TurmaDisciplina " + " INNER JOIN Turma ON (Turma.codigo = TurmaDisciplina.turma) " + " inner join Disciplina on Disciplina.codigo =  TurmaDisciplina.disciplina " + " WHERE turma.unidadeEnsino = " + codigoUnidadeEnsino.intValue() + " ORDER BY turma.identificadorturma, disciplina.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	/*
	 * Responsável por realizar uma consulta de <code>TurmaDisciplinaVO</code>
	 * através do valor do atributo <code>Integer turma</code>, <code>Integer
	 * disciplina</code>. Retorna os objetos, com início do valor do atributo
	 * idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso Indica se a aplicação deverá verificar se o
	 * usuário possui permissão para esta consulta ou não.
	 * 
	 * @return TurmaDisciplinaVO.
	 * 
	 * @exception Exception Caso haja problemas de conexão ou restrição de
	 * acesso.
	 */
	public TurmaDisciplinaVO consultarPorCodigoTurmaCodigoDisciplina(Integer turma, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT TurmaDisciplina.* FROM TurmaDisciplina, Turma, Disciplina WHERE TurmaDisciplina.turma = Turma.codigo and Turma.codigo = " + turma.intValue() + " and TurmaDisciplina.disciplina = Disciplina.codigo and Disciplina.codigo = " + disciplina.intValue() + " ORDER BY Turma.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new TurmaDisciplinaVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public TurmaDisciplinaVO consultarPorMatriculaPeriodoCodigoDisciplina(Integer matriculaPeriodo, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSqlConsultaComGradeDisciplina();
		sql.append(" INNER JOIN matriculaPeriodo on turma.codigo = matriculaPeriodo.turma ");
		sql.append("WHERE matriculaPeriodo.codigo = ").append(matriculaPeriodo);
		sql.append("and  disciplina.codigo = ").append(disciplina);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return new TurmaDisciplinaVO();
		}
		return (montarDadosComGradeDisciplina(tabelaResultado, nivelMontarDados, usuario));
		
	}
	
	public TurmaDisciplinaVO consultarPorTurmaCodigoDisciplinaComGradeDiscilina(Integer turma, Integer disciplina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = getSqlConsultaComGradeDisciplina();
		sql.append("WHERE turma.codigo = ").append(turma);
		sql.append("and  disciplina.codigo = ").append(disciplina);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return new TurmaDisciplinaVO();
		}
		return (montarDadosComGradeDisciplina(tabelaResultado, nivelMontarDados, usuario));
		
	}

	public TurmaDisciplinaVO consultarNrAlunosMatriculadosPelaTurmaEDisciplina(Integer turma, Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT TurmaDisciplina.codigo, nrAlunosMatriculados FROM TurmaDisciplina, Turma, Disciplina " + "WHERE TurmaDisciplina.turma = Turma.codigo and Turma.codigo = " + turma.intValue() + " and TurmaDisciplina.disciplina = Disciplina.codigo and Disciplina.codigo = " + disciplina.intValue() + " ORDER BY Turma.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new TurmaDisciplinaVO();
		}
		return montarDadosNrAlunosMatriculados(tabelaResultado);
	}

	private TurmaDisciplinaVO montarDadosNrAlunosMatriculados(SqlRowSet dadosSQL) {
		TurmaDisciplinaVO turmaDisciplinaVO = new TurmaDisciplinaVO();
		turmaDisciplinaVO.setCodigo(dadosSQL.getInt("codigo"));
		turmaDisciplinaVO.setNrAlunosMatriculados(dadosSQL.getInt("nrAlunosMatriculados"));
		return turmaDisciplinaVO;
	}

	/*
	 * Responsável por realizar uma consulta de <code>TurmaDisciplinaVO</code>
	 * através do valor do atributo <code>Integer valorConsulta</code>. Retorna
	 * os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso Indica se a aplicação deverá verificar se o
	 * usuário possui permissão para esta consulta ou não.
	 * 
	 * @return List Contendo vários objetos da classe <code>TurmaVO</code>
	 * resultantes da consulta.
	 * 
	 * @exception Exception Caso haja problemas de conexão ou restrição de
	 * acesso.
	 */
	public List<TurmaDisciplinaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TurmaDisciplina WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * Responsável por realizar uma consulta de <code>TurmaDisciplinaVO</code>
	 * através do valor do atributo <code>Integer turma, Integer disciplina,
	 * Integer nrVagas</code>. Retorna os objetos, com início do valor do
	 * atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o
	 * List resultante.
	 * 
	 * @param controlarAcesso Indica se a aplicação deverá verificar se o
	 * usuário possui permissão para esta consulta ou não.
	 * 
	 * @return TurmaDisciplinaVO.
	 * 
	 * @exception Exception Caso haja problemas de conexão ou restrição de
	 * acesso.
	 */
	public TurmaDisciplinaVO consultarExistenciaVagaTurmaDisciplina(Integer turma, Integer disciplina, Integer nrVagas, boolean controlarAcesso, int nivelMontaDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT TurmaDisciplina.* FROM TurmaDisciplina, Turma, Disciplina WHERE TurmaDisciplina.turma = Turma.codigo and Turma.codigo = " + turma.intValue() + " " + " and TurmaDisciplina.disciplina = Disciplina.codigo and Disciplina.codigo = " + disciplina.intValue() + " and TurmaDisciplina.nrAlunosMatriculados < " + nrVagas.intValue() + " ORDER BY Turma.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new TurmaDisciplinaVO();
		}
		return montarDados(tabelaResultado, nivelMontaDados, usuario);
	}

	@Override
	public ModalidadeDisciplinaEnum consultarModalidadeDisciplinaPorTurmaEDisciplina(Integer codigoTurma, Integer codigoDisciplina) {
		StringBuilder sql = new StringBuilder("SELECT modalidadeDisciplina from turmaDisciplina ");
		sql.append(" WHERE  turma = ").append(codigoTurma);
		sql.append(" and  disciplina = ").append(codigoDisciplina);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (tabelaResultado.next()) {
			return ModalidadeDisciplinaEnum.valueOf(tabelaResultado.getString("modalidadeDisciplina"));
		}
		return ModalidadeDisciplinaEnum.PRESENCIAL;
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>TurmaDisciplinaVO</code> resultantes da consulta.
	 */
	public  List<TurmaDisciplinaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<TurmaDisciplinaVO> vetResultado = new ArrayList<TurmaDisciplinaVO>(0);
		while (tabelaResultado.next()) {
			TurmaDisciplinaVO obj = new TurmaDisciplinaVO();
			obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	private StringBuilder getSQLPadraoConsultaCompleta() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  ");
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
		sql.append(" turmaDisciplina.permiteReposicao as \"turmaDisciplina.permiteReposicao\", ");
		sql.append(" turmaDisciplina.gradeCurricularGrupoOptativaDisciplina as \"turmaDisciplina.gradeCurricularGrupoOptativaDisciplina\", ");
		sql.append(" TurmaDisciplina.nralunosmatriculados as \"TurmaDisciplina.nralunosmatriculados\", TurmaDisciplina.definicoestutoriaonline as \"TurmaDisciplina.definicoestutoriaonline\", TurmaDisciplina.ordemestudoonline as \"TurmaDisciplina.ordemestudoonline\", GradeCurricular.nome as grade, turma.situacao as situacao,   ");
		sql.append(" TurmaDisciplina.codigo as \"TurmaDisciplina.codigo\",TurmaDisciplina.modalidadeDisciplina as \"TurmaDisciplina.modalidadeDisciplina\", ");
		sql.append(" TurmaDisciplina.localAula as \"TurmaDisciplina.localAula\",TurmaDisciplina.salaLocalAula as \"TurmaDisciplina.salaLocalAula\", TurmaDisciplina.configuracaoAcademico as \"TurmaDisciplina.configuracaoAcademico\", ");
		sql.append(" disciplinaEquivalenteTurmaAgrupada.codigo AS \"disciplinaEquivalenteTurmaAgrupada.codigo\", disciplinaEquivalenteTurmaAgrupada.nome AS \"disciplinaEquivalenteTurmaAgrupada.nome\", ");
		sql.append(" Disciplina.nome as \"Disciplina.nome\" , Disciplina.codigo as \"Disciplina.codigo\" ");

		sql.append(" from turma ");
		sql.append(" left join gradecurricular on turma.gradecurricular = gradecurricular.codigo 	");
		sql.append(" left join turmadisciplina on  turmadisciplina.turma = turma.codigo ");
		sql.append(" left join gradedisciplina on gradedisciplina.codigo = turmaDisciplina.gradedisciplina ");
		sql.append(" left join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmaDisciplina.gradeCurricularGrupoOptativaDisciplina ");
		sql.append(" left join disciplina disciplinaEquivalenteTurmaAgrupada on turmadisciplina.disciplinaEquivalenteTurmaAgrupada = disciplinaEquivalenteTurmaAgrupada.codigo ");
		sql.append(" left join disciplina on turmadisciplina.disciplina = disciplina.codigo ");
		return sql;
	}
	
	private TurmaDisciplinaVO montarDadosCompleto(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception  {
		TurmaDisciplinaVO turmaDisciplinaVO = new TurmaDisciplinaVO();
		turmaDisciplinaVO.setCodigo(dadosSQL.getInt("TurmaDisciplina.codigo"));
		turmaDisciplinaVO.setPermiteReposicao(dadosSQL.getBoolean("TurmaDisciplina.permiteReposicao"));
		turmaDisciplinaVO.getDisciplina().setCodigo(dadosSQL.getInt("Disciplina.codigo"));
		turmaDisciplinaVO.setPermiteReposicao(dadosSQL.getBoolean("TurmaDisciplina.permiteReposicao"));
		turmaDisciplinaVO.getDisciplina().setNome(dadosSQL.getString("Disciplina.nome"));
		turmaDisciplinaVO.getDisciplinaEquivalenteTurmaAgrupada().setCodigo(dadosSQL.getInt("disciplinaEquivalenteTurmaAgrupada.codigo"));
		turmaDisciplinaVO.getDisciplinaEquivalenteTurmaAgrupada().setNome(dadosSQL.getString("disciplinaEquivalenteTurmaAgrupada.nome"));
//		turmaDisciplinaVO.getSalaLocalAula().setCodigo(dadosSQL.getInt("TurmaDisciplina.salaLocalAula"));
//		turmaDisciplinaVO.getLocalAula().setCodigo(dadosSQL.getInt("TurmaDisciplina.localAula"));
		if (!turmaDisciplinaVO.getDisciplinaEquivalenteTurmaAgrupada().getCodigo().equals(0)) {
			turmaDisciplinaVO.setMensagemDisciplinaEquivalenteTurmaAgrupada("Disciplina incluída por causa da Equivalência com a disciplina: Código = " + turmaDisciplinaVO.getDisciplinaEquivalenteTurmaAgrupada().getCodigo() + ", Nome = " + turmaDisciplinaVO.getDisciplinaEquivalenteTurmaAgrupada().getNome().toUpperCase() + ".");
		}
		
		// MONTANDO OS DADOS DA GRADEDISCIPLINA - PARA DISCIPLINAS
		// REGULARES DA MATRIZ
		turmaDisciplinaVO.getGradeDisciplinaVO().setCodigo(dadosSQL.getInt("turmaDisciplina.gradedisciplina"));
		turmaDisciplinaVO.getGradeDisciplinaVO().setCargaHoraria(dadosSQL.getInt("gradedisciplina.cargahoraria"));
		turmaDisciplinaVO.getGradeDisciplinaVO().getDisciplina().setCodigo(dadosSQL.getInt("gradedisciplina.disciplina"));
		turmaDisciplinaVO.getGradeDisciplinaVO().getDisciplina().setNome(dadosSQL.getString("disciplina.nome"));
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
		return turmaDisciplinaVO;
	}
	
	
	@Override
	public void consultaTurmaDisciplinaCompletaPorTurma(TurmaVO obj, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = getSQLPadraoConsultaCompleta();
		sb.append(" where turmadisciplina.turma = ").append(obj.getCodigo());
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		while (dadosSQL.next()) {
			TurmaDisciplinaVO td = montarDadosCompleto(dadosSQL, usuarioVO);
			td.setTurma(obj.getCodigo());
			obj.getTurmaDisciplinaVOs().add(td);
		}
	}
	
	
	public List<TurmaDisciplinaVO> consultaRapidaPorTurma(Integer turmaSugerida, Integer turmaPrincipal, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select turmadisciplina.codigo AS \"turmadisciplina.codigo\", turma.codigo AS \"turma.codigo\", turma.identificadorturma AS \"turma.identificadorturma\", ");
		sb.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", ");
		sb.append(" unidadeensino.codigo AS \"unidadeensino.codigo\", unidadeensino.nome AS \"unidadeensino.nome\", ");
		sb.append(" curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", ");
		sb.append(" turno.codigo AS \"turno.codigo\", turno.nome AS \"turno.nome\", ");
		// OBTEM A CARGA HORÁRIA
		sb.append(" case when gradedisciplina.cargaHoraria is not null then gradedisciplina.cargaHoraria else ");
		sb.append(" case when gradecurriculargrupooptativadisciplina.cargahoraria is not null then gradecurriculargrupooptativadisciplina.cargahoraria else ");
		sb.append(" (");
		sb.append(" select distinct gradedisciplina.cargahoraria from periodoletivo ");
		sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo  and gradedisciplina.disciplina = disciplina.codigo ");
		sb.append(" where periodoletivo.codigo = turma.periodoletivo ");
		sb.append(" )");
		sb.append(" end end AS cargaHoraria, ");
		// OBTEM O NRCREDITOS
		sb.append(" case when gradedisciplina.nrcreditos is not null then gradedisciplina.nrcreditos else ");
		sb.append(" case when gradecurriculargrupooptativadisciplina.nrcreditos is not null then gradecurriculargrupooptativadisciplina.nrcreditos else ");
		sb.append(" (");
		sb.append(" select distinct gradedisciplina.nrcreditos from periodoletivo ");
		sb.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo  and gradedisciplina.disciplina = disciplina.codigo ");
		sb.append(" where periodoletivo.codigo = turma.periodoletivo ");
		sb.append(" )");
		sb.append(" end end AS nrcreditos ");
		sb.append(" from turmadisciplina ");
		sb.append(" inner join turma on turma.codigo = turmadisciplina.turma ");
		sb.append(" inner join disciplina on disciplina.codigo = turmadisciplina.disciplina ");
		sb.append(" inner join curso on curso.codigo = turma.curso ");
		sb.append(" inner join turno on turno.codigo = turma.turno ");
		sb.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sb.append(" left join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina ");
		sb.append(" left join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = turmadisciplina.gradecurriculargrupooptativadisciplina ");
		sb.append(" where turma.codigo = ").append(turmaSugerida);
		sb.append(" and disciplina.codigo not in(");
		sb.append(" select turmadisciplinaprincipal.disciplina from turmadisciplina turmadisciplinaprincipal ");
		sb.append(" where turmadisciplinaprincipal.turma = ").append(turmaPrincipal);
		sb.append(" ) ");
		sb.append(" order by disciplina.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<TurmaDisciplinaVO> turmaDisciplinaVOs = new ArrayList<TurmaDisciplinaVO>(0);
		while (tabelaResultado.next()) {
			TurmaDisciplinaVO obj = new TurmaDisciplinaVO();
			obj.setCodigo(tabelaResultado.getInt("turmadisciplina.codigo"));
			obj.setTurma(tabelaResultado.getInt("turma.codigo"));
			obj.getTurmaDescricaoVO().setCodigo(tabelaResultado.getInt("turma.codigo"));
			obj.getTurmaDescricaoVO().setIdentificadorTurma(tabelaResultado.getString("turma.identificadorturma"));
			obj.getDisciplina().setCodigo(tabelaResultado.getInt("disciplina.codigo"));
			obj.getDisciplina().setNome(tabelaResultado.getString("disciplina.nome"));
			obj.getTurmaDescricaoVO().getUnidadeEnsino().setCodigo(tabelaResultado.getInt("unidadeEnsino.codigo"));
			obj.getTurmaDescricaoVO().getUnidadeEnsino().setNome(tabelaResultado.getString("unidadeEnsino.nome"));
			obj.getTurmaDescricaoVO().getCurso().setCodigo(tabelaResultado.getInt("curso.codigo"));
			obj.getTurmaDescricaoVO().getCurso().setNome(tabelaResultado.getString("curso.nome"));
			obj.getTurmaDescricaoVO().getTurno().setCodigo(tabelaResultado.getInt("turno.codigo"));
			obj.getTurmaDescricaoVO().getTurno().setNome(tabelaResultado.getString("turno.nome"));
			obj.getGradeDisciplinaVO().setCargaHoraria(tabelaResultado.getInt("cargaHoraria"));
			obj.getGradeDisciplinaVO().setNrCreditos(tabelaResultado.getInt("nrCreditos"));
			turmaDisciplinaVOs.add(obj);
		}
		return turmaDisciplinaVOs;
	}

	public  TurmaDisciplinaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		TurmaDisciplinaVO obj = new TurmaDisciplinaVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.setTurma(new Integer(tabelaResultado.getInt("turma")));
		obj.getDisciplina().setCodigo(new Integer(tabelaResultado.getInt("disciplina")));
		obj.setNrAlunosMatriculados(new Integer(tabelaResultado.getInt("nrAlunosMatriculados")));
		obj.setNrVagasMatricula(new Integer(tabelaResultado.getInt("nrVagasMatricula")));
		obj.setNrMaximoMatricula(new Integer(tabelaResultado.getInt("nrMaximoMatricula")));
		obj.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(tabelaResultado.getString("modalidadeDisciplina")));
        obj.setPermiteReposicao(tabelaResultado.getBoolean("permiteReposicao"));
        obj.setPermiteApoioPresencial(tabelaResultado.getBoolean("permiteApoioPresencial"));		
//		obj.getLocalAula().setCodigo(tabelaResultado.getInt("localAula"));
//		obj.getSalaLocalAula().setCodigo(tabelaResultado.getInt("salaLocalAula"));
		obj.getConfiguracaoAcademicoVO().setCodigo(tabelaResultado.getInt("configuracaoAcademico"));
		obj.getDisciplinaEquivalenteTurmaAgrupada().setCodigo(tabelaResultado.getInt("disciplinaEquivalenteTurmaAgrupada"));

		// MONTANDO OS DADOS DA GRADEDISCIPLINA - PARA DISCIPLINAS REGULARES DA
		// MATRIZ
		obj.getGradeDisciplinaVO().setCodigo(tabelaResultado.getInt("gradeDisciplina"));
		obj.getGradeDisciplinaVO().getDisciplina().setCodigo(tabelaResultado.getInt("disciplina"));
		// MONTANDO OS DADOS DA gradeCurricularGrupoOptativaDisciplina - PARA
		// DISCIPLINAS REFERENTES A UM GRUPO DE OPTATIVAS
		obj.setDisciplinaReferenteAUmGrupoOptativa(tabelaResultado.getBoolean("disciplinaReferenteAUmGrupoOptativa"));
		obj.getGradeCurricularGrupoOptativaDisciplinaVO().setCodigo(tabelaResultado.getInt("gradeCurricularGrupoOptativaDisciplina"));
		obj.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().setCodigo(tabelaResultado.getInt("disciplina"));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("ordemestudoonline"))) {
			obj.setOrdemEstudoOnline(tabelaResultado.getInt("ordemestudoonline"));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("definicoesTutoriaOnline"))) {
			obj.setDefinicoesTutoriaOnline(DefinicoesTutoriaOnlineEnum.valueOf(tabelaResultado.getString("definicoesTutoriaOnline")));
		}
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosDisciplinaEquivalenteTurmaAgrupada(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			montarDadosLocalAula(obj, nivelMontarDados, usuario);
			montarDadosSalaLocalAula(obj, nivelMontarDados, usuario);
			if (tabelaResultado.getInt("configuracaoAcademico") > 0) {
				obj.setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("configuracaoAcademico"), usuario));
			}
		}
		return obj;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>TurmaDisciplinaVO</code>.
	 * 
	 * @return O objeto da classe <code>TurmaDisciplinaVO</code> com os dados
	 *         devidamente montados.
	 */
	public  TurmaDisciplinaVO montarDadosComGradeDisciplina(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		TurmaDisciplinaVO obj = new TurmaDisciplinaVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.setTurma(new Integer(tabelaResultado.getInt("turma")));
		TurmaVO turmaDados = new TurmaVO();
		turmaDados.setCodigo(tabelaResultado.getInt("turma"));
		turmaDados.setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
		obj.setTurmaDescricaoVO(turmaDados);
		obj.getDisciplina().setCodigo(new Integer(tabelaResultado.getInt("disciplina")));
		obj.setPermiteReposicao(tabelaResultado.getBoolean("permiteReposicao"));		
		obj.setPermiteApoioPresencial(tabelaResultado.getBoolean("permiteApoioPresencial"));
		obj.setNrAlunosMatriculados(new Integer(tabelaResultado.getInt("nrAlunosMatriculados")));
		obj.setNrVagasMatricula(new Integer(tabelaResultado.getInt("nrVagasMatricula")));
		obj.setNrMaximoMatricula(new Integer(tabelaResultado.getInt("nrMaximoMatricula")));
		obj.setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(tabelaResultado.getString("modalidadeDisciplina")));
//		obj.getLocalAula().setCodigo(tabelaResultado.getInt("localAula"));
//		obj.getSalaLocalAula().setCodigo(tabelaResultado.getInt("salaLocalAula"));
		obj.getDisciplinaEquivalenteTurmaAgrupada().setCodigo(tabelaResultado.getInt("disciplinaEquivalenteTurmaAgrupada"));
		obj.setOrdemEstudoOnline(tabelaResultado.getInt("ordemestudoonline"));

		// MONTANDO OS DADOS DA GRADEDISCIPLINA - PARA DISCIPLINAS REGULARES DA
		// MATRIZ
		obj.getGradeDisciplinaVO().setCodigo(tabelaResultado.getInt("gradeDisciplina"));
		obj.getGradeDisciplinaVO().setCargaHoraria(tabelaResultado.getInt("gradedisciplina.cargahoraria"));
		obj.getGradeDisciplinaVO().getDisciplina().setCodigo(tabelaResultado.getInt("gradedisciplina.disciplina"));
		obj.getGradeDisciplinaVO().getDisciplina().setNome(tabelaResultado.getString("disciplina.nome"));
		obj.getGradeDisciplinaVO().setNrCreditos(tabelaResultado.getInt("gradedisciplina.nrcreditos"));
		obj.getGradeDisciplinaVO().setNrCreditoFinanceiro(tabelaResultado.getDouble("gradedisciplina.nrCreditoFinanceiro"));
		obj.getGradeDisciplinaVO().setTipoDisciplina(tabelaResultado.getString("gradedisciplina.tipoDisciplina"));
		if (tabelaResultado.getString("gradedisciplina.modalidadeDisciplina") != null) {
			obj.getGradeDisciplinaVO().setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(tabelaResultado.getString("gradedisciplina.modalidadeDisciplina")));
		}
		// MONTANDO OS DADOS DA gradeCurricularGrupoOptativaDisciplina - PARA
		// DISCIPLINAS REFERENTES A UM GRUPO DE OPTATIVAS
		obj.setDisciplinaReferenteAUmGrupoOptativa(tabelaResultado.getBoolean("disciplinaReferenteAUmGrupoOptativa"));
		obj.getGradeCurricularGrupoOptativaDisciplinaVO().setCodigo(tabelaResultado.getInt("gradeCurricularGrupoOptativaDisciplina"));
		obj.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().setCodigo(tabelaResultado.getInt("gradeCurricularGrupoOptativaDisciplina.disciplina"));
		obj.getGradeCurricularGrupoOptativaDisciplinaVO().getDisciplina().setNome(tabelaResultado.getString("disciplina.nome"));
		obj.getGradeCurricularGrupoOptativaDisciplinaVO().setCargaHoraria(tabelaResultado.getInt("gradeCurricularGrupoOptativaDisciplina.cargahoraria"));
		obj.getGradeCurricularGrupoOptativaDisciplinaVO().setNrCreditos(tabelaResultado.getInt("gradeCurricularGrupoOptativaDisciplina.nrcreditos"));
		obj.getGradeCurricularGrupoOptativaDisciplinaVO().setNrCreditoFinanceiro(tabelaResultado.getDouble("gradeCurricularGrupoOptativaDisciplina.nrCreditoFinanceiro"));
		if (tabelaResultado.getString("gradeCurricularGrupoOptativaDisciplina.modalidadeDisciplina") != null) {
			obj.getGradeCurricularGrupoOptativaDisciplinaVO().setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(tabelaResultado.getString("gradeCurricularGrupoOptativaDisciplina.modalidadeDisciplina")));
		}
		if (tabelaResultado.getString("definicoesTutoriaOnline") != null) {
			obj.setDefinicoesTutoriaOnline(DefinicoesTutoriaOnlineEnum.valueOf(tabelaResultado.getString("definicoesTutoriaOnline")));
		}
		obj.setNovoObj(Boolean.FALSE);
		// montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
		// usuario);
		montarDadosDisciplinaEquivalenteTurmaAgrupada(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			montarDadosLocalAula(obj, nivelMontarDados, usuario);
			montarDadosSalaLocalAula(obj, nivelMontarDados, usuario);
			if (tabelaResultado.getInt("configuracaoAcademico") > 0) {
				obj.setConfiguracaoAcademicoVO(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(tabelaResultado.getInt("configuracaoAcademico"), usuario));
			}
		}
		return obj;
	}

	public static void montarDadosLocalAula(TurmaDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		if (obj.getLocalAula().getCodigo().intValue() == 0) {
//			obj.setLocalAula(new LocalAulaVO());
//			return;
//		}
//		obj.setLocalAula(getFacadeFactory().getLocalAulaFacade().consultarPorChavePrimaria(obj.getLocalAula().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	public static void montarDadosSalaLocalAula(TurmaDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//		if (obj.getSalaLocalAula().getCodigo().intValue() == 0) {
//			obj.setSalaLocalAula(new SalaLocalAulaVO());
//			return;
//		}
//		obj.setSalaLocalAula(getFacadeFactory().getSalaLocalAulaFacade().consultarPorChavePrimaria(obj.getSalaLocalAula().getCodigo()));
	}

	public  void montarDadosDisciplina(TurmaDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplina().getCodigo().intValue() == 0) {
			obj.setDisciplina(new DisciplinaVO());
			return;
		}
		obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), nivelMontarDados, usuario));
	}

	public  void montarDadosDisciplinaEquivalenteTurmaAgrupada(TurmaDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplinaEquivalenteTurmaAgrupada().getCodigo().intValue() == 0) {
			obj.setDisciplinaEquivalenteTurmaAgrupada(new DisciplinaVO());
			return;
		}
		obj.setDisciplinaEquivalenteTurmaAgrupada(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplinaEquivalenteTurmaAgrupada().getCodigo(), nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.TurmaDisciplinaInterfaceFacade#
	 * alterarTurmaDisciplinas(negocio.comuns.academico.TurmaVO, java.util.List)
	 */
	public void alterarTurmaDisciplinas(TurmaVO turma, List<TurmaDisciplinaVO> objetos, UsuarioVO usuarioVO) throws Exception {
		excluirTurmaDisciplinas(turma.getCodigo(), objetos, usuarioVO);
		incluirTurmaDisciplinas(turma, objetos, usuarioVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.TurmaDisciplinaInterfaceFacade#
	 * incluirTurmaDisciplinas(negocio.comuns.academico.TurmaVO, java.util.List)
	 */
	public void incluirTurmaDisciplinas(TurmaVO turma, List<TurmaDisciplinaVO> objetos, UsuarioVO usuarioVO) throws Exception {
		Iterator<TurmaDisciplinaVO> e = objetos.iterator();
		while (e.hasNext()) {
			TurmaDisciplinaVO obj = (TurmaDisciplinaVO) e.next();
			obj.setTurma(turma.getCodigo());
			obj.setNrMaximoMatricula(obj.getNrMaximoMatricula());
			obj.setNrVagasMatricula(obj.getNrVagasMatricula());
			if (obj.getCodigo().intValue() == 0) {
				incluir(obj, usuarioVO);
			} else {
				alterar(obj, usuarioVO);
			}
		}
	}

	/**
	 * Operação responsável por consultar todos os
	 * <code>TurmaDisciplinaVO</code> relacionados a um objeto da classe
	 * <code>academico.Turma</code>.
	 * 
	 * @param turma
	 *            Atributo de <code>academico.Turma</code> a ser utilizado para
	 *            localizar os objetos da classe <code>TurmaDisciplinaVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>TurmaDisciplinaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public List<TurmaDisciplinaVO> consultarTurmaDisciplinas(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		List<TurmaDisciplinaVO> objetos = new ArrayList<TurmaDisciplinaVO>(0);
		String sql = "SELECT TurmaDisciplina.* FROM TurmaDisciplina  WHERE turma = ? ";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { turma });
		while (tabelaResultado.next()) {
			TurmaDisciplinaVO novoObj = new TurmaDisciplinaVO();
			novoObj = montarDados(tabelaResultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	public List<TurmaDisciplinaVO> consultarDisciplinaDiferenteTurmaAgrupada(Integer turmaAgrupada, List<TurmaDisciplinaVO> listaTurmaDisciplinaAgrupadaVOs, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo, disciplina from turmadisciplina where disciplina in(");
		boolean virgula = false;
		for (TurmaDisciplinaVO turmaDisciplinaVO : listaTurmaDisciplinaAgrupadaVOs) {
			if (!virgula) {
				sb.append(turmaDisciplinaVO.getDisciplina().getCodigo());
			} else {
				sb.append(", ").append(turmaDisciplinaVO.getDisciplina().getCodigo());
			}
			virgula = true;
		}
		sb.append("and turma = ").append(turmaAgrupada);
		sb.append(" and disciplina not in(");
		sb.append(" select distinct turmadisciplina.disciplina from turmaagrupada turmaagrupada1 ");
		sb.append(" inner join turmaagrupada turmaagrupada2 on turmaagrupada2.codigo <> turmaagrupada1.codigo ");
		sb.append(" inner join turma turma1 on turma1.codigo = turmaagrupada1.turma ");
		sb.append(" inner join turma turma2 on turma2.codigo = turmaagrupada2.turma ");
		sb.append(" inner join turmadisciplina on turmadisciplina.turma = turma1.codigo ");
		sb.append(" inner join turmadisciplina td on td.turma = turma2.codigo ");
		sb.append(" where turmaagrupada1.turmaorigem = ").append(turmaAgrupada).append(" and turmaagrupada2.turmaorigem = ").append(turmaAgrupada);
		sb.append(" and turmadisciplina.codigo <> td.codigo ");
		sb.append(" and turmadisciplina.turma <> td.turma ");
		sb.append(" and turmadisciplina.disciplina = td.disciplina ");
		sb.append(" ) order by disciplina ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<TurmaDisciplinaVO> listaTurmaDisciplinaVOs = new ArrayList<TurmaDisciplinaVO>(0);
		while (tabelaResultado.next()) {
			TurmaDisciplinaVO obj = new TurmaDisciplinaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getDisciplina().setCodigo(tabelaResultado.getInt("disciplina"));
			listaTurmaDisciplinaVOs.add(obj);
		}
		return listaTurmaDisciplinaVOs;
	}
	
	public StringBuilder getSqlConsultaComGradeDisciplina(){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT TurmaDisciplina.*, ");
		// DADOS DA TURMA
		sql.append(" turma.identificadorTurma as \"identificadorTurma\",  ");
		 sql.append(" turma.nrMaximoMatricula as \"turma.nrMaximoMatricula\",  ");
        sql.append(" turma.nrMinimoMatricula as \"turma.nrMinimoMatricula\",  ");
        sql.append(" turma.nrVagas as \"turma.nrVagas\",  ");
		// DADOS DA DISCIPLINA
		sql.append(" disciplina.nome as \"disciplina.nome\",  ");

		// DADOS DE Gradedisciplina
		sql.append(" gradedisciplina.cargahoraria as \"gradedisciplina.cargahoraria\", gradedisciplina.nrcreditos as \"gradedisciplina.nrcreditos\", ");
		sql.append(" gradedisciplina.nrCreditoFinanceiro as \"gradedisciplina.nrCreditoFinanceiro\", gradedisciplina.tipoDisciplina as \"gradedisciplina.tipoDisciplina\", ");
		sql.append(" gradedisciplina.modalidadeDisciplina as \"gradedisciplina.modalidadeDisciplina\", ");
		sql.append(" gradedisciplina.disciplina as \"gradedisciplina.disciplina\", ");
		// DADOS DE gradeCurricularGrupoOptativaDisciplina
		sql.append(" gradeCurricularGrupoOptativaDisciplina.cargahoraria as \"gradeCurricularGrupoOptativaDisciplina.cargahoraria\", gradeCurricularGrupoOptativaDisciplina.nrcreditos as \"gradeCurricularGrupoOptativaDisciplina.nrcreditos\", ");
		sql.append(" gradeCurricularGrupoOptativaDisciplina.nrCreditoFinanceiro as \"gradeCurricularGrupoOptativaDisciplina.nrCreditoFinanceiro\", ");
		sql.append(" gradeCurricularGrupoOptativaDisciplina.modalidadeDisciplina as \"gradeCurricularGrupoOptativaDisciplina.modalidadeDisciplina\", ");
		sql.append(" gradeCurricularGrupoOptativaDisciplina.disciplina as \"gradeCurricularGrupoOptativaDisciplina.disciplina\" ");

		sql.append("FROM TurmaDisciplina ");
		sql.append(" INNER JOIN turma on turma.codigo = turmaDisciplina.turma ");
		sql.append(" left join disciplina on turmadisciplina.disciplina = disciplina.codigo ");

		sql.append(" left join gradedisciplina on gradedisciplina.codigo = turmaDisciplina.gradedisciplina ");
		sql.append(" left join gradeCurricularGrupoOptativaDisciplina on gradeCurricularGrupoOptativaDisciplina.codigo = turmaDisciplina.gradeCurricularGrupoOptativaDisciplina ");
		return sql;
	}

	/**
	 * Operação responsável por consultar todos os
	 * <code>TurmaDisciplinaVO</code> relacionados a um objeto da classe
	 * <code>academico.Turma</code>.
	 * 
	 * @param turma
	 *            Atributo de <code>academico.Turma</code> a ser utilizado para
	 *            localizar os objetos da classe <code>TurmaDisciplinaVO</code>.
	 * @return TurmaDisciplinaVO.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public TurmaDisciplinaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		StringBuilder sql = getSqlConsultaComGradeDisciplina();
		sql.append("WHERE TurmaDisciplina.codigo = ?");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( TurmaDisciplina ).");
		}
		return (montarDadosComGradeDisciplina(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return TurmaDisciplina.idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.TurmaDisciplinaInterfaceFacade#setIdEntidade
	 * (java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		TurmaDisciplina.idEntidade = idEntidade;
	}

	@Override
	public Integer consultarTotalDisciplinaTurma(Integer turma) {
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("SELECT count(codigo) as qtde from turmadisciplina where turma = " + turma);
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	private void validarDadosConsultaMapaLocalAulaTurma(Integer unidadeEnsino, Date dataInicio, Date dataTermino) throws ConsistirException {
		ConsistirException consistirException = new ConsistirException();
		if (unidadeEnsino == null || unidadeEnsino == 0) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_MapaLocalAulaTurma_unidadeEnsino"));
		}
		if (dataInicio == null) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_MapaLocalAulaTurma_dataInicio"));
		}
		if (dataTermino == null) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_MapaLocalAulaTurma_dataTermino"));
		}
		if (dataInicio != null && dataTermino != null && Uteis.getDataJDBC(dataInicio).compareTo(Uteis.getDataJDBC(dataTermino)) > 0) {
			consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_MapaLocalAulaTurma_dataInicio_maior_dataTermino"));
		}
		if (!consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		} else {
			consistirException = null;
		}
	}

//	@Override
//	public List<MapaLocalAulaTurmaVO> consultarMapaLocalAulaTurma(Integer unidadeEnsino, Date dataInicio, Date dataTermino, Integer curso, Integer turma, Integer disciplina) throws Exception {
//		validarDadosConsultaMapaLocalAulaTurma(unidadeEnsino, dataInicio, dataTermino);
//		StringBuilder sql = new StringBuilder(" select * from (SELECT   turma.codigo, turma.identificadorturma, min(horarioturmadia.data) as dataAula, ");
//		sql.append(" disciplina.codigo as disciplina, ");
//		sql.append(" disciplina.nome as \"disciplina.nome\", ");
//		sql.append(" professor.codigo as professor, ");
//		sql.append(" professor.nome as \"professor.nome\",");
//		sql.append(" professor.email as \"professor.email\",");
//		sql.append(" turmadisciplina.codigo as \"turmadisciplina.codigo\", ");
//		sql.append(" turmadisciplina.localAula as \"turmadisciplina.localAula\", ");
//		sql.append(" turmadisciplina.salaLocalAula as \"turmadisciplina.salaLocalAula\", ");
//		sql.append(" turmadisciplina.avaliacao as \"turmadisciplina.avaliacao\", ");
//		sql.append(" min(salalocalaula.codigo) as horarioturmadiaitem_sala, ");
//		sql.append(" min(localaula.codigo) as horarioturmadiaitem_local, ");
//		sql.append(" (select count(distinct m.matricula)  from matriculaperiodoturmadisciplina mptd inner join matricula m on m.matricula = mptd.matricula inner join matriculaperiodo mp on mp.codigo = mptd.matriculaperiodo ");
//		sql.append(" where ");
//		sql.append(" ((mptd.turma = turma.codigo) ");
//		sql.append(" or (mptd.turma in (select turma from turmaAgrupada where turmaOrigem = turma.codigo ) and mptd.turmaPratica is null and mptd.turmaTeorica is null ) ");
//		sql.append(" or (mptd.turmaPratica is not null and mptd.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = turma.codigo and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA')) ");
//		sql.append(" or (mptd.turmaTeorica is not null and mptd.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = turma.codigo and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA')) ");
//		sql.append(" or (mptd.turmaTeorica = turma.codigo) ");
//		sql.append(" or  (mptd.turmaPratica = turma.codigo) ");
//		sql.append(" ) ");					
//		sql.append(" and mptd.disciplina = disciplina.codigo and mptd.reposicao = false and m.situacao in ('AT') and (mp.situacaomatriculaperiodo = 'AT' or mp.situacaomatriculaperiodo = 'CO') and mp.situacaoMatriculaPeriodo not in ('PR') ) as qtdeAluno  , ");
//		sql.append(" (select count(distinct m.matricula)  from matriculaperiodoturmadisciplina mptd inner join matricula m on m.matricula = mptd.matricula inner join matriculaperiodo mp on mp.codigo = mptd.matriculaperiodo ");//		
//		sql.append(" where ");
//		sql.append(" ((mptd.turma = turma.codigo) ");
//		sql.append(" or (mptd.turma in (select turma from turmaAgrupada where turmaOrigem = turma.codigo ) and mptd.turmaPratica is null and mptd.turmaTeorica is null ) ");
//		sql.append(" or (mptd.turmaPratica is not null and mptd.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = turma.codigo and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA')) ");
//		sql.append(" or (mptd.turmaTeorica is not null and mptd.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = turma.codigo and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA')) ");
//		sql.append(" or (mptd.turmaTeorica = turma.codigo) ");
//		sql.append(" or  (mptd.turmaPratica = turma.codigo) ");
//		sql.append(" ) ");	
//		sql.append(" and mptd.disciplina = disciplina.codigo and mptd.reposicao = false and m.situacao = 'AT' and mp.situacaoMatriculaPeriodo in ('PR') ) as qtdeAlunoPre  , ");
//		sql.append(" (select count(distinct m.matricula)  from matriculaperiodoturmadisciplina mptd    inner join matricula m on m.matricula = mptd.matricula ");
//		sql.append(" where ");
//		sql.append(" ((mptd.turma = turma.codigo) ");
//		sql.append(" or (mptd.turma in (select turma from turmaAgrupada where turmaOrigem = turma.codigo ) and mptd.turmaPratica is null and mptd.turmaTeorica is null ) ");
//		sql.append(" or (mptd.turmaPratica is not null and mptd.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = turma.codigo and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA')) ");
//		sql.append(" or (mptd.turmaTeorica is not null and mptd.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = turma.codigo and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA')) ");
//		sql.append(" or (mptd.turmaTeorica = turma.codigo) ");
//		sql.append(" or  (mptd.turmaPratica = turma.codigo) ");
//		sql.append(" ) ");	
//		sql.append(" and mptd.disciplina = disciplina.codigo and m.situacao in ('AT') and (mptd.reposicao = true) ) as qtdeAlunoExtRep  ");
//		sql.append(" from horarioturma  ");
//		sql.append(" inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma");
//		sql.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");	
//		sql.append(" inner join turma on turma.codigo = horarioturma.turma ");
//		sql.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
//		sql.append(" inner join pessoa as professor on professor.codigo = horarioturmadiaitem.professor ");
//		sql.append(" inner join turmadisciplina on turmadisciplina.turma =  turma.codigo and turmadisciplina.disciplina = disciplina.codigo ");
//		sql.append(" left join salalocalaula on horarioturmadiaitem.sala = salalocalaula.codigo");
//		sql.append(" left join localaula on salalocalaula.localaula = localaula.codigo");		
//		sql.append(" where horarioturmadia.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' and horarioturmadia.data <= '").append(Uteis.getDataJDBC(dataTermino)).append("' ");
//		if (turma != null && turma > 0) {
//			sql.append(" and turma.codigo = ").append(turma);
//		}
//		if (unidadeEnsino != null && unidadeEnsino > 0) {
//			sql.append(" and turma.unidadeEnsino = ").append(unidadeEnsino);
//		}
//		if (curso != null && curso > 0) {
//			sql.append(" and turma.curso = ").append(curso);
//		}
//		if (disciplina != null && disciplina > 0) {
//			sql.append(" and horarioturmadiaitem.disciplina = ").append(disciplina).append(" ");
//		}
//		sql.append(" ");
//		sql.append(" group by turma.codigo, turma.identificadorturma, disciplina.codigo, professor.codigo, professor.nome, disciplina.nome, ");
//		sql.append(" turmadisciplina.localAula, turmadisciplina.salaLocalAula, turmadisciplina.codigo, professor.email, turmadisciplina.avaliacao ");
//		sql.append(" order by turma.identificadorturma, disciplina.nome ) as t where t.qtdeAluno + t.qtdeAlunoExtRep > 0 ");
//		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
////		List<MapaLocalAulaTurmaVO> mapaLocalAulaTurmaVOs = new ArrayList<MapaLocalAulaTurmaVO>(0);
////		MapaLocalAulaTurmaVO mapaLocalAulaTurmaVO = null;
//		while (rs.next()) {
//			mapaLocalAulaTurmaVO = new MapaLocalAulaTurmaVO();
//			mapaLocalAulaTurmaVO.getTurma().setCodigo(rs.getInt("codigo"));
//			mapaLocalAulaTurmaVO.getTurma().setIdentificadorTurma(rs.getString("identificadorturma"));
//			mapaLocalAulaTurmaVO.getTurmaDisciplina().setCodigo(rs.getInt("turmadisciplina.codigo"));		
//			mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().setCodigo(rs.getInt("turmadisciplina.localAula"));
//			mapaLocalAulaTurmaVO.getTurmaDisciplina().getSalaLocalAula().setCodigo(rs.getInt("turmadisciplina.salaLocalAula"));
//			mapaLocalAulaTurmaVO.getTurmaDisciplina().setAvaliacao(rs.getDouble("turmadisciplina.avaliacao"));
//			mapaLocalAulaTurmaVO.getTurmaDisciplina().getDisciplina().setCodigo(rs.getInt("disciplina"));
//			mapaLocalAulaTurmaVO.getTurmaDisciplina().getDisciplina().setNome(rs.getString("disciplina.nome"));
//			mapaLocalAulaTurmaVO.getProfessor().setCodigo(rs.getInt("professor"));
//			mapaLocalAulaTurmaVO.getProfessor().setNome(rs.getString("professor.nome"));
//			mapaLocalAulaTurmaVO.getProfessor().setEmail(rs.getString("professor.email"));
//			mapaLocalAulaTurmaVO.setQtdeAluno(rs.getInt("qtdeAluno"));
//			mapaLocalAulaTurmaVO.setQtdeAlunoExtRep(rs.getInt("qtdeAlunoExtRep"));
//			mapaLocalAulaTurmaVO.setQtdeAlunoPre(rs.getInt("qtdeAlunoPre"));
//			mapaLocalAulaTurmaVO.setDataAula(rs.getDate("dataAula"));
//			mapaLocalAulaTurmaVO.setDatasAulaStr(getFacadeFactory().getHorarioTurmaDiaFacade().consultarDataAulaTurmaDisciplina(mapaLocalAulaTurmaVO.getTurma().getCodigo(), mapaLocalAulaTurmaVO.getTurmaDisciplina().getDisciplina().getCodigo()));
//			mapaLocalAulaTurmaVO.setNrModulo(getFacadeFactory().getHorarioTurmaDiaFacade().consultarNrModuloDisciplina(mapaLocalAulaTurmaVO.getTurma().getCodigo(), mapaLocalAulaTurmaVO.getTurmaDisciplina().getDisciplina().getCodigo()));
//			
//			if(!Uteis.isAtributoPreenchido(mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getCodigo())) {
//				if(Uteis.isAtributoPreenchido(rs.getInt("horarioturmadiaitem_local"))){
//					mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().setCodigo(rs.getInt("horarioturmadiaitem_local"));
//				}
//			}
//			
//			if(!Uteis.isAtributoPreenchido(mapaLocalAulaTurmaVO.getTurmaDisciplina().getSalaLocalAula().getCodigo())) {
//				if(Uteis.isAtributoPreenchido(rs.getInt("horarioturmadiaitem_sala"))){
//					mapaLocalAulaTurmaVO.getTurmaDisciplina().getSalaLocalAula().setCodigo(rs.getInt("horarioturmadiaitem_sala"));
//				}
//			}
//			
//			mapaLocalAulaTurmaVOs.add(mapaLocalAulaTurmaVO);
//		}
//		return mapaLocalAulaTurmaVOs;
//
//	}

	@Override
	public TurmaDisciplinaVO consultarPorTurmaDisciplina(Integer turma, Integer disciplina, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select turmadisciplina.* from turmadisciplina ");
		sqlStr.append(" where turmadisciplina.turma = ").append(turma);
		sqlStr.append(" and turmadisciplina.disciplina = ").append(disciplina);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		}
		return new TurmaDisciplinaVO();
	}
	
	@Override
	public boolean validarTurmaDisciplinaEOnline(TurmaVO turma, DisciplinaVO disciplinaVO, UsuarioVO usuarioLogado) throws Exception {
			TurmaDisciplinaVO turmaDisciplinaVO = consultarPorTurmaDisciplina(turma.getCodigo(), disciplinaVO.getCodigo(), false, usuarioLogado);
			if(turmaDisciplinaVO.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE) && turmaDisciplinaVO.getDefinicoesTutoriaOnline().equals(DefinicoesTutoriaOnlineEnum.DINAMICA)) {
				return true;
			}else {
				return false;
			}
		}
	
	/**
	 * 
	 * Método responsável por consultar a definição referente a turmadisciplina incluido no momento do cadastro da turma 
	 * caso a Disciplina seja ambas ou on-line.
	 * 
	 */
	@Override
	public DefinicoesTutoriaOnlineEnum consultarDefinicoesTutoriaOnlineTurmaDisciplina(Integer codigoTurma, Integer codigoDisciplina) throws Exception {
		StringBuilder sqlStr = new StringBuilder()
				.append(" select coalesce(definicoestutoriaonline, 'PROGRAMACAO_DE_AULA') as definicoestutoriaonline ")
				.append(" from turmadisciplina ")
				.append(" left join gradedisciplina on turmadisciplina.gradedisciplina = gradedisciplina.codigo ")
				.append(" left join gradecurriculargrupooptativadisciplina on turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ")
				.append(" left join gradedisciplinacomposta on ((gradedisciplina.codigo is not null and gradedisciplina.disciplinacomposta and gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina) ")
				.append(" or (gradecurriculargrupooptativadisciplina.codigo is not null and gradecurriculargrupooptativadisciplina.disciplinacomposta and gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina)) ")
				.append(" where ")
				.append(" turmadisciplina.turma = ")
				.append(codigoTurma)
				.append(" and (gradedisciplina.disciplina = ")
				.append(codigoDisciplina)
				.append(" or gradecurriculargrupooptativadisciplina.disciplina = ")
				.append(codigoDisciplina)
				.append(" or gradedisciplinacomposta.disciplina = ")
				.append(codigoDisciplina)
				.append(" ) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		if(rs.next()) {
			return DefinicoesTutoriaOnlineEnum.valueOf(rs.getString("definicoestutoriaonline"));
		} else {
			return DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA;
		}
	}
	
	@Override
	public Boolean consultarSeTurmaDisciplinaSaoEad(Integer turma, Integer disciplina, UsuarioVO usuario) throws Exception {
		return consultarSeTurmaDisciplinaSaoEadPorUnidadeEnsinoPorCursoPorNivelEducacionalPorTurmaPorDisciplina(0, 0, "", turma, disciplina, usuario);
	}
	
	@Override
	public boolean consultarSeTurmaDisciplinaSaoEadPorUnidadeEnsinoPorCursoPorNivelEducacionalPorTurmaPorDisciplina(Integer unidadeEnsino, Integer curso, String nivelEducacional, Integer turma, Integer disciplina, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select turmadisciplina.codigo from turmadisciplina  ");
		sqlStr.append(" inner join turma on turma.codigo =  turmadisciplina.turma ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo =  turma.unidadeensino ");
		sqlStr.append(" inner join disciplina on disciplina.codigo =  turmadisciplina.disciplina ");
		sqlStr.append(" inner join curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso) or (turma.turmaagrupada and curso.codigo = (select t.curso from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo limit 1 ) ) )");
		sqlStr.append(" where definicoestutoriaonline  = 'DINAMICA' ");
		Uteis.checkState(!Uteis.isAtributoPreenchido(turma), "O Campo TURMA deve ser informado para verificação de TurmaDiscplina Ead");
		Uteis.checkState(!Uteis.isAtributoPreenchido(disciplina), "O Campo DISCIPLINA deve ser informado para verificação de TurmaDiscplina Ead");
		if(Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and turma.codigo = ").append(turma).append(" ");
		}
		if(Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina).append(" ");
		}
		if(Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and unidadeensino.codigo = ").append(unidadeEnsino).append(" ");
		}
		if(Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and curso.codigo = ").append(curso).append(" ");
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)) {
			sqlStr.append(" and curso.niveleducacional = '").append(nivelEducacional).append("' ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado.next();
	}
	
	/**
	 * @author Victor Hugo 10/02/2015
	 * 
	 * Método responsável por montar as modalidades da disciplina de acordo com a grade. Se Ambas, será montado na tela e o usuário terá a possibilidade de escolher entre
	 * Presencial ou On-line. Se Presencial ou On-line já definido na grade disciplinar do curso, o usuário não poderá alterar sua modalidade.
	 * Lembrando que uma vez que a Turma já tenha alunos matriculado, não há como mudar a modalidade.
	 */
	@Override
	public void montarDadosListaSelectItemModalidade(List<TurmaDisciplinaVO> turmaDisciplinaVOs) throws Exception {
		for (TurmaDisciplinaVO obj : turmaDisciplinaVOs) {
			obj.setListaSelectItemModalidadeDisciplina(null);
			if (!Uteis.isAtributoPreenchido(obj.getGradeCurricularGrupoOptativaDisciplinaVO().getCodigo())) {
				if (obj.getGradeDisciplinaVO().getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.PRESENCIAL)) {
					obj.setDefinicoesTutoriaOnline(DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA);
					obj.getListaSelectItemModalidadeDisciplina().add(new SelectItem(ModalidadeDisciplinaEnum.PRESENCIAL, ModalidadeDisciplinaEnum.PRESENCIAL.getValorApresentar()));
					if(!obj.getNovoObj() && obj.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE)){
						obj.getListaSelectItemModalidadeDisciplina().add(new SelectItem(ModalidadeDisciplinaEnum.ON_LINE, ModalidadeDisciplinaEnum.ON_LINE.getValorApresentar()));
					}else{
						obj.setModalidadeDisciplina(ModalidadeDisciplinaEnum.PRESENCIAL);
					}
				} else if (obj.getGradeDisciplinaVO().getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
					obj.getListaSelectItemModalidadeDisciplina().add(new SelectItem(ModalidadeDisciplinaEnum.ON_LINE, ModalidadeDisciplinaEnum.ON_LINE.getValorApresentar()));
					if(!obj.getNovoObj() && obj.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.PRESENCIAL)){
						obj.getListaSelectItemModalidadeDisciplina().add(new SelectItem(ModalidadeDisciplinaEnum.PRESENCIAL, ModalidadeDisciplinaEnum.PRESENCIAL.getValorApresentar()));
					}else{
						obj.setModalidadeDisciplina(ModalidadeDisciplinaEnum.ON_LINE);
					}
				} else if (obj.getGradeDisciplinaVO().getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.AMBAS)) {
					obj.getListaSelectItemModalidadeDisciplina().add(new SelectItem(ModalidadeDisciplinaEnum.PRESENCIAL, ModalidadeDisciplinaEnum.PRESENCIAL.getValorApresentar()));
					obj.getListaSelectItemModalidadeDisciplina().add(new SelectItem(ModalidadeDisciplinaEnum.ON_LINE, ModalidadeDisciplinaEnum.ON_LINE.getValorApresentar()));
				}
			} else {
				if (obj.getGradeCurricularGrupoOptativaDisciplinaVO().getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.PRESENCIAL)) {
					obj.setDefinicoesTutoriaOnline(DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA);
					obj.getListaSelectItemModalidadeDisciplina().add(new SelectItem(ModalidadeDisciplinaEnum.PRESENCIAL, ModalidadeDisciplinaEnum.PRESENCIAL.getValorApresentar()));
					if(!obj.getNovoObj() && obj.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE)){
						obj.getListaSelectItemModalidadeDisciplina().add(new SelectItem(ModalidadeDisciplinaEnum.ON_LINE, ModalidadeDisciplinaEnum.ON_LINE.getValorApresentar()));
					}else{
						obj.setModalidadeDisciplina(ModalidadeDisciplinaEnum.PRESENCIAL);
					}
				} else if (obj.getGradeCurricularGrupoOptativaDisciplinaVO().getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.ON_LINE)) {
					obj.getListaSelectItemModalidadeDisciplina().add(new SelectItem(ModalidadeDisciplinaEnum.ON_LINE, ModalidadeDisciplinaEnum.ON_LINE.getValorApresentar()));
					if(!obj.getNovoObj() && obj.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.PRESENCIAL)){
						obj.getListaSelectItemModalidadeDisciplina().add(new SelectItem(ModalidadeDisciplinaEnum.PRESENCIAL, ModalidadeDisciplinaEnum.PRESENCIAL.getValorApresentar()));
					}else{
						obj.setModalidadeDisciplina(ModalidadeDisciplinaEnum.ON_LINE);
					}
				} else if (obj.getGradeCurricularGrupoOptativaDisciplinaVO().getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.AMBAS)) {
					obj.getListaSelectItemModalidadeDisciplina().add(new SelectItem(ModalidadeDisciplinaEnum.PRESENCIAL, ModalidadeDisciplinaEnum.PRESENCIAL.getValorApresentar()));
					obj.getListaSelectItemModalidadeDisciplina().add(new SelectItem(ModalidadeDisciplinaEnum.ON_LINE, ModalidadeDisciplinaEnum.ON_LINE.getValorApresentar()));
				}
			}
		}
	}
	
	@Override
	public List<TurmaDisciplinaVO> consultarTurmaDisciplinaCalcularMedia(Integer unidadeEnsino, Integer curso, Integer turno, Integer turma, Integer disciplina, String ano, String semestre, Integer configuracaoAcademica, String situacoesMatriculaPeriodo, String nivelEducacional) throws Exception{
		StringBuilder sql  = new StringBuilder("");
		sql.append(" select distinct case when matriculaperiodoturmadisciplina.turmapratica is not null then matriculaperiodoturmadisciplina.turmapratica ");
		sql.append(" else case when matriculaperiodoturmadisciplina.turmateorica is not null then matriculaperiodoturmadisciplina.turmateorica else matriculaperiodoturmadisciplina.turma end end as turma, matriculaperiodoturmadisciplina.disciplina, disciplina.nome as nomeDisciplina from historico  ");
		sql.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina");
		sql.append(" inner join matriculaperiodo on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo");
		sql.append(" inner join matricula on matricula.matricula = historico.matricula");
		sql.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma");
		sql.append(" inner join curso on curso.codigo = matricula.curso");
		sql.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sql.append(" where  ");
		sql.append(" (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false) ");
		sql.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false) ");
//		sql.append(" and (historico.historicoDisciplinafazpartecomposicao) ");
		sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));		
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sql.append(" and matricula.unidadeEnsino = ").append(unidadeEnsino);
		}
		if(Uteis.isAtributoPreenchido(curso)){
			sql.append(" and matricula.curso = ").append(curso);
		}
		if(Uteis.isAtributoPreenchido(turno)){
			sql.append(" and matricula.turno = ").append(turno);
		}
		if(Uteis.isAtributoPreenchido(turma)){
			sql.append(" and (matriculaperiodoturmadisciplina.turma = ").append(turma);
			sql.append(" or matriculaperiodoturmadisciplina.turmapratica = ").append(turma);
			sql.append(" or matriculaperiodoturmadisciplina.turmateorica = ").append(turma);
			sql.append(" ) ");
		}
		if(Uteis.isAtributoPreenchido(disciplina)){
			sql.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
		}
		if(Uteis.isAtributoPreenchido(configuracaoAcademica)){
			sql.append(" and historico.configuracaoacademico = ").append(configuracaoAcademica);
		}
		if(Uteis.isAtributoPreenchido(ano)){
			sql.append(" and matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ");
		}
		if(Uteis.isAtributoPreenchido(semestre)){
			sql.append(" and matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ");
		}
		if(Uteis.isAtributoPreenchido(nivelEducacional)){
			sql.append(" and curso.nivelEducacional = '").append(nivelEducacional).append("' ");
		}
		if(Uteis.isAtributoPreenchido(situacoesMatriculaPeriodo)){
			sql.append(" and matriculaperiodo.situacaomatriculaperiodo in (").append(situacoesMatriculaPeriodo).append(") ");
		}	
		sql.append(" order by turma, disciplina ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<TurmaDisciplinaVO> turmaDisciplinaVOs = new ArrayList<TurmaDisciplinaVO>(0);
		TurmaDisciplinaVO turmaDisciplinaVO = null;
		while(rs.next()){
			turmaDisciplinaVO = new TurmaDisciplinaVO();
			turmaDisciplinaVO.getDisciplina().setCodigo(rs.getInt("disciplina"));
			turmaDisciplinaVO.getDisciplina().setNome(rs.getString("nomeDisciplina"));
			turmaDisciplinaVO.setTurma(rs.getInt("turma"));
			turmaDisciplinaVOs.add(turmaDisciplinaVO);
		}		
		return turmaDisciplinaVOs;
	}
	
    /**
     * Método responsável por adicionar uma TurmaDisciplinaVO com opcao
     * ou nao para Disciplina de um Grupo de Optativas. Caso seja uma
     * disciplina do grupo de optativa, então este registro estará vinculado
     * a um registro da entidade GradeCurricularGrupoOptativaDisciplinaVO. Caso
     * contrário estará vinculado um registro da entidade GradeDisciplina, que
     * trata-se de uma disciplina convencional. Se trata-se de uma optativa de um
     * grupo de optativa, então o atributo disciplinaReferenteAUmGrupoOptativa deverá ser true, caso contrário false.
     */
	@Override
	public TurmaDisciplinaVO executarGeracaoTurmaDisciplinaVO(DisciplinaVO disciplinaVO, ModalidadeDisciplinaEnum modalidade, Boolean disciplinaReferenteAUmGrupoOptativa, GradeDisciplinaVO gradeDisciplinaVO, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, ConfiguracaoAcademicoVO configuracaoAcademico, Boolean permiteReposicao, Integer nrMaximoMatricula, Integer nrVagas) throws Exception {
		TurmaDisciplinaVO obj = new TurmaDisciplinaVO();
		obj.setDisciplina(disciplinaVO);
		obj.setNrMaximoMatricula(nrMaximoMatricula);
		obj.setNrVagasMatricula(nrVagas);
		obj.setDisciplinaReferenteAUmGrupoOptativa(disciplinaReferenteAUmGrupoOptativa);
		if (gradeCurricularGrupoOptativaDisciplinaVO != null) {
			if (gradeCurricularGrupoOptativaDisciplinaVO.getModalidadeDisciplina().equals(ModalidadeDisciplinaEnum.AMBAS)) {
				obj.getListaSelectItemModalidadeDisciplina().add(new SelectItem(ModalidadeDisciplinaEnum.PRESENCIAL, ModalidadeDisciplinaEnum.PRESENCIAL.getValorApresentar()));
				obj.getListaSelectItemModalidadeDisciplina().add(new SelectItem(ModalidadeDisciplinaEnum.ON_LINE, ModalidadeDisciplinaEnum.ON_LINE.getValorApresentar()));
				obj.setModalidadeDisciplina(ModalidadeDisciplinaEnum.PRESENCIAL);
			} else {
				obj.getListaSelectItemModalidadeDisciplina().add(new SelectItem(gradeCurricularGrupoOptativaDisciplinaVO.getModalidadeDisciplina(), gradeCurricularGrupoOptativaDisciplinaVO.getModalidadeDisciplina().getValorApresentar()));
				obj.setModalidadeDisciplina(gradeCurricularGrupoOptativaDisciplinaVO.getModalidadeDisciplina());
			}
		}
		/**
		 * Comentado por Victor Hugo 12/02/2015
		 */
		// obj.setModalidadeDisciplina(modalidade);
		obj.setPermiteReposicao(permiteReposicao);
		if (disciplinaReferenteAUmGrupoOptativa) {
			obj.setGradeCurricularGrupoOptativaDisciplinaVO(gradeCurricularGrupoOptativaDisciplinaVO);
		} else {
			obj.setGradeDisciplinaVO(gradeDisciplinaVO);
		}
		obj.setConfiguracaoAcademicoVO(configuracaoAcademico);
		return obj;
	}
	
	public Boolean consultarSeExisteTurmaDisciplinaVinculadaAGradeCurricular(Integer gradeCurricular, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("select 1 from turmadisciplina td ");
			sqlStr.append("inner join gradedisciplina gd on gd.codigo = td.gradedisciplina ");
			sqlStr.append("inner join periodoletivo pl on pl.codigo = gd.periodoletivo ");
			sqlStr.append("inner join gradecurricular gc on gc.codigo = pl.gradecurricular and gc.codigo = ? ");
			sqlStr.append("limit 1");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[] { gradeCurricular });
		return tabelaResultado.next();
	}
	
	@Override
	public boolean consultarDisciplinaCompostaTurmaDisciplina(Integer turma, Integer disciplina){
		StringBuilder sql = new StringBuilder("SELECT case when turma.subturma then turma.turmaprincipal else turma.codigo end as turma from turma ");
		sql.append(" inner join turmadisciplina on turma.codigo = turmadisciplina.turma ");
		sql.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina ");
		sql.append(" where turma.codigo = ").append(turma);
		sql.append(" and gradedisciplina.disciplina = ").append(disciplina);
		sql.append(" and gradedisciplina.disciplinacomposta ");		
		sql.append(" union ");
		sql.append(" SELECT case when turma.subturma then turma.turmaprincipal else turma.codigo end as turma from turma ");
		sql.append(" inner join turmadisciplina on turma.codigo = turmadisciplina.turma ");
		sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = turmadisciplina.gradecurriculargrupooptativadisciplina ");
		sql.append(" where turma.codigo = ").append(turma);
		sql.append(" and gradecurriculargrupooptativadisciplina.disciplina = ").append(disciplina);
		sql.append(" and gradecurriculargrupooptativadisciplina.disciplinacomposta limit 1");
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next();			
	}

	@Override
	public List<TurmaDisciplinaEstatisticaAlunoVO> consultarPorAlunoComModalidadeDiferenteTurma(TurmaVO turmaVO, TurmaDisciplinaVO turmaDisciplinaVO, UsuarioVO usuario) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("SELECT MatriculaPeriodoTurmaDisciplina.ano, MatriculaPeriodoTurmaDisciplina.semestre, count(distinct MatriculaPeriodoTurmaDisciplina.matricula) as qtde FROM MatriculaPeriodoTurmaDisciplina ");		
		sqlStr.append("INNER JOIN Matricula ON (MatriculaPeriodoTurmaDisciplina.matricula = Matricula.matricula) ");		
		sqlStr.append("INNER JOIN matriculaPeriodo on matriculaPeriodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sqlStr.append("INNER JOIN historico ON (MatriculaPeriodoTurmaDisciplina.codigo = historico.MatriculaPeriodoTurmaDisciplina) ");
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaTeorica ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaPratica ");
			} else {
				sqlStr.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
			}
		} else {
			sqlStr.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
		}
		sqlStr.append(" WHERE ");
		if (turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" Turma.codigo  in ( select turma from turmaAgrupada where turmaOrigem =  " + turmaVO.getCodigo() + ")");
		} else {
			sqlStr.append(" Turma.codigo  = " + turmaVO.getCodigo());
		}
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.modalidadedisciplina <> '").append(turmaDisciplinaVO.getModalidadeDisciplina().name()).append("' ");
		sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.disciplina = " + turmaDisciplinaVO.getDisciplina().getCodigo().intValue());
		sqlStr.append(" group BY MatriculaPeriodoTurmaDisciplina.ano, MatriculaPeriodoTurmaDisciplina.semestre ");
		sqlStr.append(" ORDER BY MatriculaPeriodoTurmaDisciplina.ano desc, MatriculaPeriodoTurmaDisciplina.semestre desc");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<TurmaDisciplinaEstatisticaAlunoVO> turmaDisciplinaEstatisticaAlunoVOs =  new ArrayList<TurmaDisciplinaEstatisticaAlunoVO>(0);
		TurmaDisciplinaEstatisticaAlunoVO turmaDisciplinaEstatisticaAlunoVO =  null;
		while(rs.next()){
			turmaDisciplinaEstatisticaAlunoVO = new TurmaDisciplinaEstatisticaAlunoVO(TipoEstatisticaTurmaDisciplinaEnum.MODALIDADE);
			turmaDisciplinaEstatisticaAlunoVO.setAno(rs.getString("ano"));
			turmaDisciplinaEstatisticaAlunoVO.setSemestre(rs.getString("semestre"));
			turmaDisciplinaEstatisticaAlunoVO.setQtdeAluno(rs.getInt("qtde"));
			turmaDisciplinaEstatisticaAlunoVO.setTurmaDisciplinaVO(turmaDisciplinaVO);
			turmaDisciplinaEstatisticaAlunoVOs.add(turmaDisciplinaEstatisticaAlunoVO);
		}
		return turmaDisciplinaEstatisticaAlunoVOs;
	}
	
	
	@Override
	public List<TurmaDisciplinaVO> consultarTurmaDisciplinaTurmaBasePartindoTurmaAgrupadaEDisciplina(Integer turmaAgrupada, Integer disciplina) throws Exception{
		StringBuilder sql = new StringBuilder("select distinct turma.codigo as turma, disciplina.codigo as disciplina from turmaagrupada ");
		sql.append(" inner join turmadisciplina on turmadisciplina.turma = turmaagrupada.turmaorigem ");
		sql.append(" inner join turma on turma.codigo = turmaagrupada.turma ");
		sql.append(" inner join turmadisciplina as td on turma.codigo = td.turma ");
		sql.append(" inner join disciplina on disciplina.codigo = td.disciplina ");
		sql.append(" where turmaagrupada.turmaorigem = ").append(turmaAgrupada);
		sql.append(" and turmadisciplina.disciplina  = ").append(disciplina);
		sql.append(" and ((disciplina.codigo = turmadisciplina.disciplina) ");
		sql.append(" or (disciplina.codigo in (select disciplina from disciplinaequivalente where equivalente = ").append(disciplina).append(")) ");
		sql.append(" or (disciplina.codigo in (select equivalente from disciplinaequivalente where disciplina = ").append(disciplina).append(")) ");
		sql.append(" ) ");
		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<TurmaDisciplinaVO> turmaDisciplinaVOs = new ArrayList<TurmaDisciplinaVO>(0);
		TurmaDisciplinaVO turmaDisciplinaVO = null;  
		while(rs.next()){
			turmaDisciplinaVO =  new TurmaDisciplinaVO();
			turmaDisciplinaVO.setTurma(rs.getInt("turma"));
			turmaDisciplinaVO.getTurmaDescricaoVO().setCodigo(rs.getInt("turma"));
			turmaDisciplinaVO.getDisciplina().setCodigo(rs.getInt("disciplina"));
			turmaDisciplinaVOs.add(turmaDisciplinaVO);
		}
		return turmaDisciplinaVOs;
	}
	
	@Override
	public void realizarVerificacaoExistenciaAlunoMatriculaOUAulaProgramadaTurmaDisciplina(TurmaVO turma) {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select turmadisciplina.turmadisciplina from ( ");
		sqlStr.append(" SELECT distinct turmadisciplina.turma as turma, turmadisciplina.codigo as turmadisciplina, turmadisciplina.disciplina FROM turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");			
		sqlStr.append(" WHERE gradedisciplina is null and gradecurriculargrupooptativadisciplina is null and turma.codigo = ").append(turma.getCodigo());
		sqlStr.append(" union all ");
		sqlStr.append(" SELECT distinct turmadisciplina.turma as turma, turmadisciplina.codigo as turmadisciplina, gradedisciplina.disciplina FROM turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sqlStr.append(" inner join gradedisciplina on turmadisciplina.gradedisciplina = gradedisciplina.codigo ");		
		sqlStr.append(" WHERE turma.codigo = ").append(turma.getCodigo());
		sqlStr.append(" union all ");
		sqlStr.append(" SELECT distinct turmadisciplina.turma as turma, turmadisciplina.codigo as turmadisciplina, gradedisciplinacomposta.disciplina FROM turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sqlStr.append(" inner join gradedisciplina on turmadisciplina.gradedisciplina = gradedisciplina.codigo ");		
		sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo ");
		sqlStr.append(" WHERE gradedisciplina.disciplinacomposta = true and turma.codigo = ").append(turma.getCodigo());
		sqlStr.append(" and (not exists (select codigo from turmadisciplinacomposta where turmadisciplinacomposta.turmadisciplina = turmadisciplina.codigo) ");
		sqlStr.append(" or exists (select codigo from turmadisciplinacomposta where turmadisciplinacomposta.turmadisciplina = turmadisciplina.codigo and turmadisciplinacomposta.gradedisciplinacomposta = gradedisciplinacomposta.codigo)) ");
		sqlStr.append(" union all ");
		sqlStr.append(" SELECT distinct turmadisciplina.turma as turma, turmadisciplina.codigo as turmadisciplina, gradecurriculargrupooptativadisciplina.disciplina FROM turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");		
		sqlStr.append(" WHERE turma.codigo = ").append(turma.getCodigo());
		sqlStr.append(" union all ");
		sqlStr.append(" SELECT distinct turmadisciplina.turma as turma, turmadisciplina.codigo as turmadisciplina, gradedisciplinacomposta.disciplina FROM turma ");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sqlStr.append(" inner join gradecurriculargrupooptativadisciplina on turmadisciplina.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");		
		sqlStr.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
		sqlStr.append(" WHERE gradecurriculargrupooptativadisciplina.disciplinacomposta = true and turma.codigo = ").append(turma.getCodigo());
		sqlStr.append(" and (not exists (select codigo from turmadisciplinacomposta where turmadisciplinacomposta.turmadisciplina = turmadisciplina.codigo) ");
		sqlStr.append(" or exists (select codigo from turmadisciplinacomposta where turmadisciplinacomposta.turmadisciplina = turmadisciplina.codigo and turmadisciplinacomposta.gradedisciplinacomposta = gradedisciplinacomposta.codigo)) ");
		sqlStr.append(" ) as turmadisciplina where (");
		if(!turma.getTurmaAgrupada() && (!turma.getSubturma() || (turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.GERAL)))) {
			sqlStr.append(" exists (select codigo from matriculaperiodoturmadisciplina mptd where mptd.turma = turmadisciplina.turma and turmadisciplina.disciplina = mptd.disciplina limit 1)");
			sqlStr.append(" or exists(select t.codigo from turma as t inner join turmadisciplina as td on td.turma = t.codigo  where t.subturma and t.turmaprincipal = turmadisciplina.turma and turmadisciplina.disciplina = td.disciplina limit 1) ");
		}else if(turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
			sqlStr.append(" exists (select codigo from matriculaperiodoturmadisciplina mptd where mptd.turmapratica = turmadisciplina.turma and turmadisciplina.disciplina = mptd.disciplina limit 1)");
		}else if(turma.getSubturma() && turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
			sqlStr.append(" exists (select codigo from matriculaperiodoturmadisciplina mptd where mptd.turmateorica = turmadisciplina.turma and turmadisciplina.disciplina = mptd.disciplina limit 1)");
		}else if(turma.getTurmaAgrupada()) {
			sqlStr.append(" exists (select mptd.codigo from matriculaperiodoturmadisciplina mptd where exists (select turmaagrupada.turma from turmaagrupada where turmaorigem = turmadisciplina.turma and mptd.turma = turmaagrupada.turma ");
			sqlStr.append(" and ( mptd.disciplina = turmadisciplina.disciplina  ");
			sqlStr.append(" or exists (select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = mptd.disciplina and disciplinaequivalente.disciplina = turmadisciplina.disciplina ) ");
			sqlStr.append(" or exists (select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.disciplina = mptd.disciplina and disciplinaequivalente.equivalente = turmadisciplina.disciplina ) ");
			sqlStr.append(" )) ");
			sqlStr.append(" and exists (select registroaula.codigo from frequenciaaula inner join registroaula on registroaula.codigo = frequenciaaula.registroaula where frequenciaaula.matriculaperiodoturmadisciplina = mptd.codigo limit 1) ");
			sqlStr.append(" limit 1) ");			
		}
		sqlStr.append(" or ( exists (select hpdi.codigo from horarioprofessordiaitem hpdi where hpdi.turma = turmadisciplina.turma and hpdi.disciplina = turmadisciplina.disciplina limit 1)))");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		for(TurmaDisciplinaVO turmaDisciplinaVO: turma.getTurmaDisciplinaVOs()) {
			turmaDisciplinaVO.setPossuiRestricao(false);
		}
		while(tabelaResultado.next()) {
			for(TurmaDisciplinaVO turmaDisciplinaVO: turma.getTurmaDisciplinaVOs()) {
				if(turmaDisciplinaVO.getCodigo().equals(tabelaResultado.getInt("turmadisciplina"))) {
					turmaDisciplinaVO.setPossuiRestricao(true);
				}
			}
		}
	}
	
	
	@Override
	public List<TurmaDisciplinaEstatisticaAlunoVO> consultarPorAlunoComConfiguracaoAcademicaDiferenteTurma(TurmaVO turmaVO, TurmaDisciplinaVO turmaDisciplinaVO, TurmaDisciplinaCompostaVO turmaDisciplinaCompostaVO, ConfiguracaoAcademicoVO configuracaoAcademicoVO, DisciplinaVO disciplinaVO, UsuarioVO usuario) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("SELECT MatriculaPeriodoTurmaDisciplina.ano, MatriculaPeriodoTurmaDisciplina.semestre, count(distinct MatriculaPeriodoTurmaDisciplina.matricula) as qtde FROM MatriculaPeriodoTurmaDisciplina ");		
		sqlStr.append("INNER JOIN Matricula ON (MatriculaPeriodoTurmaDisciplina.matricula = Matricula.matricula) ");		
		sqlStr.append("INNER JOIN matriculaPeriodo on matriculaPeriodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sqlStr.append("INNER JOIN historico ON (MatriculaPeriodoTurmaDisciplina.codigo = historico.MatriculaPeriodoTurmaDisciplina) ");
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaTeorica ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaPratica ");
			} else {
				sqlStr.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
			}
		} else {
			sqlStr.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo ");
		}
		sqlStr.append(" WHERE ");
		if (turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" Turma.codigo  in ( select turma from turmaAgrupada where turmaOrigem =  " + turmaVO.getCodigo() + ")");
		} else {
			sqlStr.append(" Turma.codigo  = " + turmaVO.getCodigo());
		}
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		sqlStr.append(" and historico.configuracaoAcademico != ").append(configuracaoAcademicoVO.getCodigo()).append(" ");
		sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.disciplina = ").append(disciplinaVO.getCodigo());
		sqlStr.append(" group BY MatriculaPeriodoTurmaDisciplina.ano, MatriculaPeriodoTurmaDisciplina.semestre ");
		sqlStr.append(" ORDER BY MatriculaPeriodoTurmaDisciplina.ano desc, MatriculaPeriodoTurmaDisciplina.semestre desc");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<TurmaDisciplinaEstatisticaAlunoVO> turmaDisciplinaEstatisticaAlunoVOs =  new ArrayList<TurmaDisciplinaEstatisticaAlunoVO>(0);
		TurmaDisciplinaEstatisticaAlunoVO turmaDisciplinaEstatisticaAlunoVO =  null;
		while(rs.next()){
			turmaDisciplinaEstatisticaAlunoVO = new TurmaDisciplinaEstatisticaAlunoVO(TipoEstatisticaTurmaDisciplinaEnum.CONFIGURACAO_ACADEMICA);
			turmaDisciplinaEstatisticaAlunoVO.setAno(rs.getString("ano"));
			turmaDisciplinaEstatisticaAlunoVO.setSemestre(rs.getString("semestre"));
			turmaDisciplinaEstatisticaAlunoVO.setQtdeAluno(rs.getInt("qtde"));
			turmaDisciplinaEstatisticaAlunoVO.setTurmaDisciplinaVO(turmaDisciplinaVO);
			turmaDisciplinaEstatisticaAlunoVO.setTurmaDisciplinaCompostaVO(turmaDisciplinaCompostaVO);
			turmaDisciplinaEstatisticaAlunoVOs.add(turmaDisciplinaEstatisticaAlunoVO);
		}
		return turmaDisciplinaEstatisticaAlunoVOs;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorGradeDisciplina(Integer gradeDisciplina, UsuarioVO usuario) throws Exception {
		try {
			TurmaDisciplina.excluir(getIdEntidade());
			String sql = "DELETE FROM TurmaDisciplina WHERE (gradeDisciplina = ?) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { gradeDisciplina });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void removerDisciplinasTurmaMarcadasParaNaoEstudar(TurmaVO obj, List<DisciplinaRSVO> listaDisciplina)throws Exception {
		
		if(Uteis.isAtributoPreenchido(obj.getTurmaDisciplinaVOs()) && Uteis.isAtributoPreenchido(listaDisciplina)) {			
			Uteis.checkState(listaDisciplina.stream().allMatch(d -> d.getEstudar().equals(Boolean.FALSE)), "Deve ser informado ao menos 1 disciplina para realizar a Matrícula ");			
			listaDisciplina.stream().filter(disc -> !disc.getEstudar()).forEach(disciplinaRemover ->{
				Iterator<TurmaDisciplinaVO> iTurmaDisciplinaVO = obj.getTurmaDisciplinaVOs().iterator();
				 while(iTurmaDisciplinaVO.hasNext()) { 			
			    	 TurmaDisciplinaVO objTurmaDisciplina = (TurmaDisciplinaVO) iTurmaDisciplinaVO.next();					    	 
			    	 if(disciplinaRemover.getCodigo().equals(objTurmaDisciplina.getDisciplina().getCodigo())) {					    		 
			    		 iTurmaDisciplinaVO.remove();	
			    	 }				    	
			    }
				
		    });		
		}
	}
	
	
	@Override
	public void removerTurmaDisciplinasNaoMarcadasParaEstudar(MatriculaRSVO matriculaRSVO,MatriculaPeriodoVO matriculaPeriodoVO) {
		if(Uteis.isAtributoPreenchido(matriculaRSVO.getDisciplinasMatricula())) {
			Uteis.checkState(matriculaRSVO.getDisciplinasMatricula().stream().allMatch(d -> d.getEstudar().equals(Boolean.FALSE)), "Deve ser informado ao menos 1 disciplina para realizar a Matrícula ");			

			matriculaRSVO.getDisciplinasMatricula().stream().filter( d -> !d.getEstudar()).forEach( disc -> {				
				Iterator<TurmaDisciplinaVO> iTurmaDisciplinaVO = matriculaPeriodoVO.getTurma().getTurmaDisciplinaVOs().iterator();
				while(iTurmaDisciplinaVO.hasNext()) { 			
					 TurmaDisciplinaVO objTurmaDisciplina = (TurmaDisciplinaVO) iTurmaDisciplinaVO.next();					    	 
					 if(disc.getCodigo().equals(objTurmaDisciplina.getDisciplina().getCodigo())) {					    		 
						 iTurmaDisciplinaVO.remove();	
					 }				    	
		        }
				
			});
			
			
		}
	}
	
}
