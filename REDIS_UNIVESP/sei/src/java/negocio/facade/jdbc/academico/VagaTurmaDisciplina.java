package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.VagaTurmaDisciplinaVO;
import negocio.comuns.academico.VagaTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.VagaTurmaDisciplinaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>TurmaDisciplinaVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>TurmaDisciplinaVO</code>. Encapsula toda a interação
 * com o banco de dados.
 * 
 * @see TurmaDisciplinaVO
 * @see ControleAcesso
 * @see Turma
 */
@Repository
public class VagaTurmaDisciplina extends ControleAcesso implements VagaTurmaDisciplinaInterfaceFacade {

	private static final long serialVersionUID = -8460825620563677931L;
	protected static String idEntidade;

	public VagaTurmaDisciplina() throws Exception {
		super();
		setIdEntidade("VagaTurma");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>TurmaVO</code>.
	 */
	public VagaTurmaDisciplinaVO novo() throws Exception {
		VagaTurmaDisciplina.incluir(getIdEntidade());
		VagaTurmaDisciplinaVO obj = new VagaTurmaDisciplinaVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>TurmaDisciplinaVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TurmaDisciplinaVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final VagaTurmaDisciplinaVO obj) throws Exception {
		try {
			VagaTurmaDisciplinaVO.validarDados(obj);
			final String sql = "INSERT INTO VagaTurmaDisciplina( vagaturma, disciplina, nrMaximoMatricula, nrVagasMatricula, nrVagasMatriculaReposicao) VALUES ( ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getVagaTurma().intValue() != 0) {
						sqlInserir.setInt(1, obj.getVagaTurma().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					sqlInserir.setInt(2, obj.getDisciplina().getCodigo().intValue());
					sqlInserir.setInt(3, obj.getNrMaximoMatricula().intValue());
					sqlInserir.setInt(4, obj.getNrVagasMatricula().intValue());
					if(obj.getNrVagasMatriculaReposicao() == null) {
						sqlInserir.setNull(5,0);
					}else {
						sqlInserir.setInt(5, obj.getNrVagasMatriculaReposicao().intValue());
					}
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

	public void alterarTurmaDisciplinas(VagaTurmaVO vagaturma, List<VagaTurmaDisciplinaVO> objetos) throws Exception {
		excluirTurmaDisciplinas(vagaturma.getCodigo(), objetos);
		incluirTurmaDisciplinas(vagaturma, objetos);
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>TurmaDisciplinaVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica
	 * a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code>
	 * da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TurmaDisciplinaVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final VagaTurmaDisciplinaVO obj) throws Exception {

		try {
			VagaTurmaDisciplinaVO.validarDados(obj);
			final String sql = "UPDATE VagaTurmaDisciplina set vagaturma=?, disciplina=?, nrMaximoMatricula = ?, nrVagasMatricula =?, nrVagasMatriculaReposicao = ? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getVagaTurma().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getVagaTurma().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					sqlAlterar.setInt(2, obj.getDisciplina().getCodigo().intValue());
					sqlAlterar.setInt(3, obj.getNrMaximoMatricula().intValue());
					sqlAlterar.setInt(4, obj.getNrVagasMatricula().intValue());
					if(obj.getNrVagasMatriculaReposicao() == null) {
						sqlAlterar.setNull(5,0);
					}else {
						sqlAlterar.setInt(5, obj.getNrVagasMatriculaReposicao().intValue());
					}
					sqlAlterar.setInt(6, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>TurmaVO</code>. Sempre localiza o registro a ser excluído através da chave
	 * primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TurmaVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(VagaTurmaDisciplinaVO obj) throws Exception {
		TurmaDisciplina.excluir(getIdEntidade());
		String sql = "DELETE FROM VagaTurmaDisciplina WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarVagaTurmaDisciplinaPorAlteracaoGradeCurricularCursoIntegral(Integer turma, Integer disciplina, Integer novaDisciplina, UsuarioVO usuario) throws Exception {
		TurmaDisciplina.excluir(getIdEntidade());
		String sql = "UPDATE VagaTurmaDisciplina set disciplina =? WHERE disciplina = ? and vagaturma in (select codigo from vagaturma where turma = ?) "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] {novaDisciplina, disciplina, turma });
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorCodigoDisciplinaTurma(Integer turma, Integer disciplina) throws Exception {
		TurmaDisciplina.excluir(getIdEntidade());
		String sql = "DELETE FROM VagaTurmaDisciplina WHERE disciplina = ? and vagaturma in (select codigo from vagaturma where turma = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { disciplina, turma });
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>TurmaVO</code>. Sempre localiza o registro a ser excluído através da chave
	 * primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>Integer turma</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTurmaDisciplinas(Integer turma) throws Exception {
		TurmaDisciplina.excluir(getIdEntidade());
		String sql = "DELETE FROM VagaTurmaDisciplina WHERE (Vagaturma = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { turma });
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>TurmaDisciplinaVO</code>. Sempre localiza o registro a ser excluído através da
	 * chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>Integer turma, List objetos</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTurmaDisciplinas(Integer vagaturma, List<VagaTurmaDisciplinaVO> objetos) throws Exception {
		String sql = "DELETE FROM VagaTurmaDisciplina WHERE (vagaturma = ?)";
		Iterator<VagaTurmaDisciplinaVO> i = objetos.iterator();
		while (i.hasNext()) {
			VagaTurmaDisciplinaVO turmaDisciplina = (VagaTurmaDisciplinaVO) i.next();
			if (turmaDisciplina.getCodigo().intValue() != 0) {
				sql += " and codigo != " + turmaDisciplina.getCodigo().intValue();
			}
		}
		getConexao().getJdbcTemplate().update(sql, new Object[] { vagaturma });
	}

	/*
	 * Responsável por realizar uma consulta de <code>TurmaDisciplinaVO</code> através do valor do atributo <code>Integer valorConsulta</code>.
	 * Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * 
	 * @return List Contendo vários objetos da classe <code>TurmaVO</code> resultantes da consulta.
	 * 
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<VagaTurmaDisciplinaVO> consultarPorDisciplina(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM VagaTurmaDisciplina WHERE disciplina = " + valorConsulta.intValue() + " ORDER BY disciplina";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/*
	 * Responsável por realizar uma consulta de <code>TurmaDisciplinaVO</code> através do valor do atributo <code>Integer valorConsulta</code>.
	 * Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * 
	 * @return List Contendo vários objetos da classe <code>TurmaVO</code> resultantes da consulta.
	 * 
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<VagaTurmaDisciplinaVO> consultarPorCodigoTurma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT VagaTurmaDisciplina.*, turma.identificadorTurma as \"identificadorTurma\" " + " FROM VagaTurmaDisciplina " + " INNER JOIN VagaTurma ON (VagaTurma.codigo = VagaTurmaDisciplina.Vagaturma) " + " inner join turma on VagaTurma.turma =  turma.codigo " + " inner join Disciplina on Disciplina.codigo =  VagaTurmaDisciplina.disciplina " + " WHERE vagaturma.turma = " + valorConsulta.intValue() + " ORDER BY disciplina.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List<VagaTurmaDisciplinaVO> consultarPorCodigoUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT VagaTurmaDisciplina.*, turma.identificadorTurma as \"identificadorTurma\" " + " FROM VagaTurmaDisciplina " + " INNER JOIN VagaTurma ON (VagaTurma.codigo = VagaTurmaDisciplina.Vagaturma) " + " inner join turma on VagaTurma.turma =  turma.codigo " + " inner join Disciplina on Disciplina.codigo =  VagaTurmaDisciplina.disciplina " + " WHERE turma.unidadeEnsino = " + codigoUnidadeEnsino.intValue() + " ORDER BY turma.identificadoturma, disciplina.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/*
	 * Responsável por realizar uma consulta de <code>TurmaDisciplinaVO</code> através do valor do atributo <code>Integer turma</code>, <code>Integer
	 * disciplina</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * 
	 * @return TurmaDisciplinaVO.
	 * 
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public VagaTurmaDisciplinaVO consultarPorCodigoTurmaCodigoDisciplina(Integer turma, Integer disciplina,  String ano, String semestre,  boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT VagaTurmaDisciplina.* FROM VagaTurmaDisciplina ");
		sqlStr.append("INNER JOIN VagaTurma on VagaTurma.codigo = VagaTurmaDisciplina.vagaTurma ");
		sqlStr.append("INNER JOIN turma on VagaTurma.turma = turma.codigo ");
		sqlStr.append("WHERE turma.codigo = ").append(turma);		
		sqlStr.append(" AND ((turma.anual  and vagaTurma.ano = '").append(ano).append("' ) ");
		sqlStr.append(" or (turma.semestral and vagaTurma.ano = '").append(ano).append("' and vagaTurma.semestre = '").append(semestre).append("' ) ");
		sqlStr.append(" or (turma.semestral = false and  turma.anual = false )) ");
		sqlStr.append(" AND VagaTurmaDisciplina.disciplina = ").append(disciplina);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (!tabelaResultado.next()) {
			return new VagaTurmaDisciplinaVO();
		}
		return montarDados(tabelaResultado, nivelMontarDados, usuario);
	}

	/*
	 * Responsável por realizar uma consulta de <code>TurmaDisciplinaVO</code> através do valor do atributo <code>Integer valorConsulta</code>.
	 * Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * 
	 * @return List Contendo vários objetos da classe <code>TurmaVO</code> resultantes da consulta.
	 * 
	 * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<VagaTurmaDisciplinaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM VagaTurmaDisciplina WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}


	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>TurmaDisciplinaVO</code> resultantes da consulta.
	 */
	public List<VagaTurmaDisciplinaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<VagaTurmaDisciplinaVO> vetResultado = new ArrayList<VagaTurmaDisciplinaVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public List<VagaTurmaDisciplinaVO> consultaRapidaPorTurma(Integer turmaSugerida, Integer turmaPrincipal, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select * from (select turmadisciplina.codigo AS turmadisciplina, ");
		sb.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", gradedisciplina.disciplinacomposta, ");
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
		sb.append(" end end AS nrcreditos, false as disciplina_equivalente ");
		sb.append(" from turmadisciplina ");
		sb.append(" inner join turma on turma.codigo = turmadisciplina.turma ");
		sb.append(" inner join disciplina on disciplina.codigo = turmadisciplina.disciplina ");
		sb.append(" left join curso on curso.codigo = turma.curso ");
		sb.append(" inner join turno on turno.codigo = turma.turno ");
		sb.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sb.append(" left join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina ");
		sb.append(" left join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = turmadisciplina.gradecurriculargrupooptativadisciplina ");
		sb.append(" where turma.codigo = ").append(turmaSugerida);
		sb.append(" union ");
		sb.append(" select turmadisciplina.codigo AS turmadisciplina, ");
		sb.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", false as disciplinacomposta, ");
		// OBTEM A CARGA HORÁRIA
		sb.append(" gradedisciplinacomposta.cargaHoraria, ");
		// OBTEM O NRCREDITOS
		sb.append(" gradedisciplinacomposta.nrcreditos, false as disciplina_equivalente ");
		sb.append(" from turmadisciplina ");
		sb.append(" inner join turma on turma.codigo = turmadisciplina.turma ");
		sb.append(" inner join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina ");
		sb.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo ");
		sb.append(" inner join disciplina on disciplina.codigo = gradedisciplinacomposta.disciplina ");
		sb.append(" where turma.codigo = ").append(turmaSugerida);
		sb.append(" union ");
		sb.append(" select turmadisciplina.codigo AS turmadisciplina, ");
		sb.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", false as disciplinacomposta, ");
		// OBTEM A CARGA HORÁRIA
		sb.append(" gradedisciplinacomposta.cargaHoraria, ");
		// OBTEM O NRCREDITOS
		sb.append(" gradedisciplinacomposta.nrcreditos, false as disciplina_equivalente ");
		sb.append(" from turmadisciplina ");
		sb.append(" inner join turma on turma.codigo = turmadisciplina.turma ");
		sb.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = turmadisciplina.gradecurriculargrupooptativadisciplina ");
		sb.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo ");
		sb.append(" inner join disciplina on disciplina.codigo = gradedisciplinacomposta.disciplina ");
		sb.append(" where turma.codigo = ").append(turmaSugerida);		
		sb.append(" union ");
		sb.append("select turmadisciplinacomposta.turmaDisciplina, ");
		sb.append("disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", false as disciplinacomposta, ");
		sb.append("gradedisciplinacomposta.cargahoraria, gradedisciplinacomposta.nrcreditos, false as disciplina_equivalente ");
		sb.append("from turma ");
		sb.append("left join curso on curso.codigo = turma.curso ");
		sb.append("inner join turno on turno.codigo = turma.turno ");
		sb.append("inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
		sb.append("inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sb.append("inner join turmadisciplinacomposta on turmadisciplinacomposta.turmadisciplina = turmadisciplina.codigo ");
		sb.append("inner join gradedisciplinacomposta on gradedisciplinacomposta.codigo = turmadisciplinacomposta.gradedisciplinacomposta ");
		sb.append("inner join disciplina on disciplina.codigo = gradedisciplinacomposta.disciplina ");
		sb.append("where turma.codigo = ").append(turmaSugerida);
		
		sb.append(" union ");
		sb.append("select turmadisciplina.codigo as turmadisciplina, disciplina_consulta.codigo as \"disciplina.codigo\", disciplina_consulta.nome as \"disciplina.nome\",	gradedisciplina.disciplinacomposta, ");
		sb.append("case when gradedisciplina.cargaHoraria is not null then gradedisciplina.cargaHoraria else case when gradecurriculargrupooptativadisciplina.cargahoraria is not null then gradecurriculargrupooptativadisciplina.cargahoraria ");
		sb.append("else (select distinct gradedisciplina.cargahoraria from periodoletivo inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo	and gradedisciplina.disciplina = disciplina.codigo ");
		sb.append("where periodoletivo.codigo = turma.periodoletivo ) end end as cargaHoraria, ");
		sb.append("case when gradedisciplina.nrcreditos is not null then gradedisciplina.nrcreditos ");
		sb.append("else case when gradecurriculargrupooptativadisciplina.nrcreditos is not null then gradecurriculargrupooptativadisciplina.nrcreditos ");
		sb.append("else (select	distinct gradedisciplina.nrcreditos from periodoletivo ");
		sb.append("inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo 	and gradedisciplina.disciplina = disciplina.codigo ");
		sb.append("where periodoletivo.codigo = turma.periodoletivo ) end end as nrcreditos, true as disciplina_equivalente from turmadisciplina ");
		sb.append("inner join turma on turma.codigo = turmadisciplina.turma ");
		sb.append("inner join disciplina on disciplina.codigo = turmadisciplina.disciplina ");
		sb.append("full outer join ( ");
		sb.append("select disciplinaequivalente.equivalente codigo, disciplinaequivalente.disciplina as disciplina from disciplinaequivalente ");
		sb.append("union select disciplinaequivalente.disciplina codigo, disciplinaequivalente.equivalente as disciplina from disciplinaequivalente ");
		sb.append(") as disciplinas_equivalentes on disciplinas_equivalentes.disciplina = disciplina.codigo ");
		sb.append("inner join disciplina disciplina_consulta on disciplina_consulta.codigo = disciplinas_equivalentes.codigo ");
		sb.append("left join curso on curso.codigo = turma.curso ");
		sb.append("inner join turno on turno.codigo = turma.turno ");
		sb.append("inner join unidadeensino on	unidadeensino.codigo = turma.unidadeensino ");
		sb.append("left join gradedisciplina on gradedisciplina.codigo = turmadisciplina.gradedisciplina ");
		sb.append("left join gradecurriculargrupooptativadisciplina on	gradecurriculargrupooptativadisciplina.codigo = turmadisciplina.gradecurriculargrupooptativadisciplina ");
		sb.append("where turma.codigo = ").append(turmaSugerida);
		sb.append(" and exists (select 1 from turmaagrupada ta where ta.turma = turma.codigo) ");
		
		sb.append(") as t ");
		sb.append("ORDER BY turmadisciplina, case when disciplinacomposta then 1 else 2 end, \"disciplina.nome\"");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<VagaTurmaDisciplinaVO> turmaDisciplinaVOs = new ArrayList<VagaTurmaDisciplinaVO>(0);
		while (tabelaResultado.next()) {
			VagaTurmaDisciplinaVO obj = new VagaTurmaDisciplinaVO();
			obj.getDisciplina().setCodigo(tabelaResultado.getInt("disciplina.codigo"));
			obj.getDisciplina().setNome(tabelaResultado.getString("disciplina.nome"));
			obj.setDisciplinaEquivalente(tabelaResultado.getBoolean("disciplina_equivalente"));
			turmaDisciplinaVOs.add(obj);
		}
		return turmaDisciplinaVOs;
	}

	public VagaTurmaDisciplinaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		VagaTurmaDisciplinaVO obj = new VagaTurmaDisciplinaVO();
		obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
		obj.setVagaTurma(new Integer(tabelaResultado.getInt("vagaturma")));
		obj.setNrMaximoMatricula(new Integer(tabelaResultado.getInt("nrMaximoMatricula")));
		obj.setNrVagasMatricula(new Integer(tabelaResultado.getInt("nrVagasMatricula")));
		if(tabelaResultado.getObject("nrVagasMatriculaReposicao") != null){
			obj.setNrVagasMatriculaReposicao(new Integer(tabelaResultado.getInt("nrVagasMatriculaReposicao")));
		}
		obj.getDisciplina().setCodigo(new Integer(tabelaResultado.getInt("disciplina")));
		montarDadosDisciplina(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	public static void montarDadosDisciplina(VagaTurmaDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplina().getCodigo().intValue() == 0) {
			obj.setDisciplina(new DisciplinaVO());
			return;
		}
		obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenegocio.facade.jdbc.academico.TurmaDisciplinaInterfaceFacade# incluirTurmaDisciplinas(negocio.comuns.academico.TurmaVO, java.util.List)
	 */
	public void incluirTurmaDisciplinas(VagaTurmaVO vagaturma, List<VagaTurmaDisciplinaVO> objetos) throws Exception {
		Iterator<VagaTurmaDisciplinaVO> e = objetos.iterator();
		while (e.hasNext()) {
			VagaTurmaDisciplinaVO obj = (VagaTurmaDisciplinaVO) e.next();
			obj.setVagaTurma(vagaturma.getCodigo());
			// obj.setNrMaximoMatricula(obj.getNrMaximoMatricula());
			// obj.setNrVagasMatricula(obj.getNrVagasMatricula());
			if (obj.getCodigo().intValue() == 0) {
				incluir(obj);
			} else {
				alterar(obj);
			}
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>TurmaDisciplinaVO</code> relacionados a um objeto da classe <code>academico.Turma</code>.
	 * 
	 * @param turma
	 *            Atributo de <code>academico.Turma</code> a ser utilizado para localizar os objetos da classe <code>TurmaDisciplinaVO</code>.
	 * @return List Contendo todos os objetos da classe <code>TurmaDisciplinaVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public List<VagaTurmaDisciplinaVO> consultarTurmaDisciplinas(Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		VagaTurmaDisciplina.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT VagaTurmaDisciplina.* FROM VagaTurmaDisciplina WHERE vagaturma = ? order by codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { turma });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Operação responsável por consultar todos os <code>TurmaDisciplinaVO</code> relacionados a um objeto da classe <code>academico.Turma</code>.
	 * 
	 * @param turma
	 *            Atributo de <code>academico.Turma</code> a ser utilizado para localizar os objetos da classe <code>TurmaDisciplinaVO</code>.
	 * @return TurmaDisciplinaVO.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public VagaTurmaDisciplinaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
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
		sql.append("WHERE TurmaDisciplina.codigo = ?");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( TurmaDisciplina ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return VagaTurmaDisciplina.idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.academico.TurmaDisciplinaInterfaceFacade#setIdEntidade (java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		VagaTurmaDisciplina.idEntidade = idEntidade;
	}

	public VagaTurmaDisciplinaVO consultarPorTurmaDisciplina(Integer turma, Integer disciplina, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select turmadisciplina.* from turmadisciplina ");
		sqlStr.append(" where turmadisciplina.turma = ").append(turma);
		sqlStr.append(" and turmadisciplina.disciplina = ").append(disciplina);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		}
		return new VagaTurmaDisciplinaVO();
	}

	@Override
	public Integer consultarQtdeVagaDisciplinaPorGradeDisciplina(Integer gradeDisciplina, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct count(distinct vagaturmadisciplina.codigo) as qtde from vagaturmadisciplina ");
		sb.append(" inner join vagaturma on vagaturma.codigo = vagaturmadisciplina.vagaturma ");
		sb.append(" inner join turma on turma.codigo = vagaturma.turma ");
		sb.append(" inner join turmadisciplina on turmadisciplina.turma = turma.codigo ");
		sb.append(" where  turmadisciplina.gradedisciplina = ?");
		sb.append(" and turmadisciplina.disciplina = vagaturmadisciplina.disciplina ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), new Object[] {gradeDisciplina});
		if (tabelaResultado.next()) {
			return tabelaResultado.getInt("qtde");
		}
		return 0;
	}
}
