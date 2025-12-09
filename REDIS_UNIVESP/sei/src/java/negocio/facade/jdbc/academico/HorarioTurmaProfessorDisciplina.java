package negocio.facade.jdbc.academico;





import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaProfessorDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>HorarioTurmaProfessorDisciplinaVO</code>. Responsável
 * por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>HorarioTurmaProfessorDisciplinaVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * 
 * @see HorarioTurmaProfessorDisciplinaVO
 * @see ControleAcesso
 * @see HorarioTuma
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class HorarioTurmaProfessorDisciplina extends ControleAcesso implements HorarioTurmaProfessorDisciplinaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5260431934383732537L;
	protected static String idEntidade;

	public HorarioTurmaProfessorDisciplina() throws Exception {
		super();
		setIdEntidade("ProgramacaoAula");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade
	 * #novo()
	 */
	public HorarioTurmaProfessorDisciplinaVO novo() throws Exception {
		HorarioTurmaProfessorDisciplina.incluir(getIdEntidade());
		HorarioTurmaProfessorDisciplinaVO obj = new HorarioTurmaProfessorDisciplinaVO();
		return obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade
	 * #incluir(negocio.comuns.academico.HorarioTurmaProfessorDisciplinaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final HorarioTurmaProfessorDisciplinaVO obj) throws Exception {
//		HorarioTurmaProfessorDisciplinaVO.validarDados(obj);
//		final String sql = "INSERT INTO HorarioTurmaProfessorDisciplina( turma, professor, disciplina, horarios, horarioTurma ) VALUES ( ?, ?, ?, ?, ? ) returning codigo";
//		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
//			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
//				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
//				if (obj.getTurma().getCodigo().intValue() != 0) {
//					sqlInserir.setInt(1, obj.getTurma().getCodigo().intValue());
//				} else {
//					sqlInserir.setNull(1, 0);
//				}
//				if (obj.getProfessor().getCodigo().intValue() != 0) {
//					sqlInserir.setInt(2, obj.getProfessor().getCodigo().intValue());
//				} else {
//					sqlInserir.setNull(2, 0);
//				}
//				if (obj.getDisciplina().getCodigo().intValue() != 0) {
//					sqlInserir.setInt(3, obj.getDisciplina().getCodigo().intValue());
//				} else {
//					sqlInserir.setNull(3, 0);
//				}
//				sqlInserir.setString(4, obj.getHorarios());
//				if (obj.getHorarioTurma().intValue() != 0) {
//					sqlInserir.setInt(5, obj.getHorarioTurma().intValue());
//				} else {
//					sqlInserir.setNull(5, 0);
//				}
//				return sqlInserir;
//			}
//		}, new ResultSetExtractor<Integer>() {
//
//			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
//				if (arg0.next()) {
//					obj.setNovoObj(Boolean.FALSE);
//					return arg0.getInt("codigo");
//				}
//				return null;
//			}
//		}));
//		obj.setNovoObj(Boolean.FALSE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade
	 * #alterar(negocio.comuns.academico.HorarioTurmaProfessorDisciplinaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final HorarioTurmaProfessorDisciplinaVO obj) throws Exception {
//		HorarioTurmaProfessorDisciplinaVO.validarDados(obj);
//		final String sql = "UPDATE HorarioTurmaProfessorDisciplina set turma=?, horarios=? WHERE (professor=? and disciplina=? and horarioTurma=?)";
//		if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
//			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
//				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
//				if (obj.getTurma().getCodigo().intValue() != 0) {
//					sqlAlterar.setInt(1, obj.getTurma().getCodigo().intValue());
//				} else {
//					sqlAlterar.setNull(1, 0);
//				}
//				sqlAlterar.setString(2, obj.getHorarios());
//				if (obj.getProfessor().getCodigo().intValue() != 0) {
//					sqlAlterar.setInt(3, obj.getProfessor().getCodigo().intValue());
//				} else {
//					sqlAlterar.setNull(3, 0);
//				}
//				if (obj.getDisciplina().getCodigo().intValue() != 0) {
//					sqlAlterar.setInt(4, obj.getDisciplina().getCodigo().intValue());
//				} else {
//					sqlAlterar.setNull(4, 0);
//				}
//				if (obj.getHorarioTurma().intValue() != 0) {
//					sqlAlterar.setInt(5, obj.getHorarioTurma().intValue());
//				} else {
//					sqlAlterar.setNull(5, 0);
//				}				
//				return sqlAlterar;
//			}
//		}) == 0) {
//			incluir(obj);
//			return;
//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade
	 * #excluir(negocio.comuns.academico.HorarioTurmaProfessorDisciplinaVO)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(HorarioTurmaProfessorDisciplinaVO obj) throws Exception {
//		HorarioTurmaProfessorDisciplina.excluir(getIdEntidade());
//		String sql = "DELETE FROM HorarioTurmaProfessorDisciplina WHERE ((codigo = ?))";
//		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade
	 * #consultarPorCodigoHorarioTuma(java.lang.Integer, boolean, int)
	 */
	public List consultarPorCodigoHorarioTuma(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT HorarioTurmaProfessorDisciplina.* FROM HorarioTurmaProfessorDisciplina, HorarioTurma WHERE HorarioTurmaProfessorDisciplina.horarioTurma = HorarioTurma.codigo and HorarioTurma.codigo = " + valorConsulta.intValue() + " ORDER BY HorarioTurma.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade
	 * #consultarPorNomeDisciplina(java.lang.String, boolean, int)
	 */
	public List consultarPorNomeDisciplina(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT HorarioTurmaProfessorDisciplina.* FROM HorarioTurmaProfessorDisciplina, Disciplina WHERE HorarioTurmaProfessorDisciplina.disciplina = Disciplina.codigo and upper( Disciplina.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Disciplina.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade
	 * #consultarPorNomePessoa(java.lang.String, boolean, int)
	 */
	public List consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT HorarioTurmaProfessorDisciplina.* FROM HorarioTurmaProfessorDisciplina, Pessoa WHERE HorarioTurmaProfessorDisciplina.professor = Pessoa.codigo and upper( Pessoa.nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade
	 * #consultarPorCodigoPessoaTurma(java.lang.Integer, java.lang.Integer,
	 * boolean, int)
	 */
	public List consultarPorCodigoPessoaTurma(Integer valorConsulta, Integer turma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT HorarioTurmaProfessorDisciplina.* FROM Disciplina, HorarioTurmaProfessorDisciplina, Pessoa, Turma WHERE HorarioTurmaProfessorDisciplina.professor = Pessoa.codigo " + " and Pessoa.codigo  = " + valorConsulta.intValue() + " and HorarioTurmaProfessorDisciplina.turma = Turma.codigo and HorarioTurmaProfessorDisciplina.disciplina = Disciplina.codigo" + " and Turma.codigo = " + turma.intValue() + " ORDER BY Pessoa.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade
	 * #consultarPorIdentificadorTurmaTurma(java.lang.String, boolean, int)
	 */
	public List consultarPorIdentificadorTurmaTurma(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT HorarioTurmaProfessorDisciplina.* FROM HorarioTurmaProfessorDisciplina, Turma WHERE HorarioTurmaProfessorDisciplina.turma = Turma.codigo and upper( Turma.identificadorTurma ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Turma.identificadorTurma";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade
	 * #consultarPorCodigo(java.lang.Integer, boolean, int)
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM HorarioTurmaProfessorDisciplina WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>HorarioTurmaProfessorDisciplinaVO</code> resultantes da
	 *         consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			HorarioTurmaProfessorDisciplinaVO obj = new HorarioTurmaProfessorDisciplinaVO();
			obj = montarDados(tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>HorarioTurmaProfessorDisciplinaVO</code>.
	 * 
	 * @return O objeto da classe <code>HorarioTurmaProfessorDisciplinaVO</code>
	 *         com os dados devidamente montados.
	 */
	public static HorarioTurmaProfessorDisciplinaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		HorarioTurmaProfessorDisciplinaVO obj = new HorarioTurmaProfessorDisciplinaVO();
		obj.getTurma().setCodigo(new Integer(dadosSQL.getInt("turma")));
		obj.getProfessor().setCodigo(new Integer(dadosSQL.getInt("professor")));
		obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
		obj.setHorarioTurma(new Integer(dadosSQL.getInt("horarioTurma")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}

		montarDadosTurma(obj, nivelMontarDados, usuario);
		montarDadosProfessor(obj, nivelMontarDados, usuario);
		montarDadosDisciplina(obj, nivelMontarDados, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>DisciplinaVO</code> relacionado ao objeto
	 * <code>HorarioTurmaProfessorDisciplinaVO</code>. Faz uso da chave primária
	 * da classe <code>DisciplinaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosDisciplina(HorarioTurmaProfessorDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplina().getCodigo().intValue() == 0) {
			obj.setDisciplina(new DisciplinaVO());
			return;
		}
		obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>PessoaVO</code> relacionado ao objeto
	 * <code>HorarioTurmaProfessorDisciplinaVO</code>. Faz uso da chave primária
	 * da classe <code>PessoaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosProfessor(HorarioTurmaProfessorDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getProfessor().getCodigo().intValue() == 0) {
			obj.setProfessor(new PessoaVO());
			return;
		}
		// obj.setProfessor(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getProfessor().getCodigo(),
		// false, nivelMontarDados, usuario));
		obj.setProfessor(getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(obj.getProfessor().getCodigo(), false, true, false, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe
	 * <code>TurmaVO</code> relacionado ao objeto
	 * <code>HorarioTurmaProfessorDisciplinaVO</code>. Faz uso da chave primária
	 * da classe <code>TurmaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosTurma(HorarioTurmaProfessorDisciplinaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getTurma().getCodigo().intValue() == 0) {
			obj.setTurma(new TurmaVO());
			return;
		}
		obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), nivelMontarDados, usuario));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade
	 * #excluirHorarioTurmaProfessorDisciplinas(java.lang.Integer)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirHorarioTurmaProfessorDisciplinas(Integer horarioTurma) throws Exception {
//		String sql = "DELETE FROM HorarioTurmaProfessorDisciplina WHERE (horarioTurma = ?)";
//		getConexao().getJdbcTemplate().update(sql, new Object[] { horarioTurma });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade
	 * #alterarHorarioTurmaProfessorDisciplinas(java.lang.Integer,
	 * java.util.List)
	 */

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarHorarioTurmaProfessorDisciplinas(Integer horarioTurma, List<HorarioTurmaProfessorDisciplinaVO> objetos) throws Exception {

//		StringBuilder sql = new StringBuilder("DELETE FROM HorarioTurmaProfessorDisciplina WHERE (horarioTurma = ?) ");
//		for (HorarioTurmaProfessorDisciplinaVO obj : objetos) {
//			sql.append(" and (professor != ").append(obj.getProfessor().getCodigo()).append(" and disciplina != ").append(obj.getDisciplina().getCodigo()).append(") ");
//		}
//		getConexao().getJdbcTemplate().update(sql.toString(), horarioTurma);
//		for (HorarioTurmaProfessorDisciplinaVO obj : objetos) {
//			obj.setHorarioTurma(horarioTurma);
//			alterar(obj);
//		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade
	 * #incluirHorarioTurmaProfessorDisciplinas(java.lang.Integer,
	 * java.util.List)
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirHorarioTurmaProfessorDisciplinas(Integer horarioTurmaPrm, List<HorarioTurmaProfessorDisciplinaVO> objetos) throws Exception {
//		if (objetos != null) {
//			Iterator e = objetos.iterator();
//			while (e.hasNext()) {
//				HorarioTurmaProfessorDisciplinaVO obj = (HorarioTurmaProfessorDisciplinaVO) e.next();
//				obj.setHorarioTurma(horarioTurmaPrm);
//				incluir(obj);
//			}
//		}
	}

	/**
	 * Operação responsável por consultar todos os
	 * <code>HorarioTurmaProfessorDisciplinaVO</code> relacionados a um objeto
	 * da classe <code>academico.HorarioTuma</code>.
	 * 
	 * @param horarioTurma
	 *            Atributo de <code>academico.HorarioTuma</code> a ser utilizado
	 *            para localizar os objetos da classe
	 *            <code>HorarioTurmaProfessorDisciplinaVO</code>.
	 * @return List Contendo todos os objetos da classe
	 *         <code>HorarioTurmaProfessorDisciplinaVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	public static List consultarHorarioTurmaProfessorDisciplinas(Integer horarioTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		HorarioTurmaProfessorDisciplina.consultar(getIdEntidade(), controlarAcesso, usuario);
		List objetos = new ArrayList(0);
		String sqlStr = "SELECT * FROM HorarioTurmaProfessorDisciplina WHERE horarioTurma = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { horarioTurma });
		while (tabelaResultado.next()) {
			HorarioTurmaProfessorDisciplinaVO novoObj = new HorarioTurmaProfessorDisciplinaVO();
			novoObj = HorarioTurmaProfessorDisciplina.montarDados(tabelaResultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade
	 * #consultarPorChavePrimaria(java.lang.Integer, boolean, int)
	 */
	public HorarioTurmaProfessorDisciplinaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM HorarioTurmaProfessorDisciplina WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( HorarioTurmaProfessorDisciplina ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return HorarioTurmaProfessorDisciplina.idEntidade;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.academico.HorarioTurmaProfessorDisciplinaInterfaceFacade
	 * #setIdEntidade(java.lang.String)
	 */
	public void setIdEntidade(String idEntidade) {
		HorarioTurmaProfessorDisciplina.idEntidade = idEntidade;
	}

	private List<Integer> montarListaProfessores(SqlRowSet tabelaResultado) throws Exception {
		List<Integer> professores = new ArrayList<Integer>(0);
		while (tabelaResultado.next()) {
			professores.add(tabelaResultado.getInt("professor"));
		}
		return professores;
	}

}