package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.academico.DisciplinaPreRequisitoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.DisciplinaPreRequisitoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>DisciplinaPreRequisitoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>DisciplinaPreRequisitoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see DisciplinaPreRequisitoVO
 * @see ControleAcesso
 * @see Disciplina
 */
@Repository
@Scope("singleton")
@Lazy 
public class DisciplinaPreRequisito extends ControleAcesso implements DisciplinaPreRequisitoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	
	protected static String idEntidade;

	public DisciplinaPreRequisito() throws Exception {
		super();
		setIdEntidade("Curso");
	}
	
	public List<DisciplinaPreRequisitoVO> consultarDisciplinaPreRequisitos(Integer gradeDisciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		List<DisciplinaPreRequisitoVO> objetos = new ArrayList<DisciplinaPreRequisitoVO>(0);
		String sql = "SELECT * FROM DisciplinaPreRequisito WHERE gradeDisciplina = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { gradeDisciplina });
		while (resultado.next()) {
			objetos.add(montarDados(resultado, usuario));
		}
		return objetos;
	}

	public DisciplinaPreRequisitoVO novo() throws Exception {
		DisciplinaPreRequisito.incluir(getIdEntidade());
		DisciplinaPreRequisitoVO obj = new DisciplinaPreRequisitoVO();
		return obj;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final DisciplinaPreRequisitoVO obj, UsuarioVO usuario) throws Exception {
		DisciplinaPreRequisitoVO.validarDados(obj);
		final String sql = "INSERT INTO DisciplinaPreRequisito( gradeDisciplina, disciplina, gradeDisciplinaComposta, gradeCurricularGrupoOptativaDisciplina ) VALUES ( ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				if (Uteis.isAtributoPreenchido(obj.getGradeDisciplina())) {
					sqlInserir.setInt(1, obj.getGradeDisciplina());
				} else {
					sqlInserir.setNull(1, 0);
				}
				sqlInserir.setInt(2, obj.getDisciplina().getCodigo().intValue());
				if (Uteis.isAtributoPreenchido(obj.getGradeDisciplinaComposta())) {
					sqlInserir.setInt(3, obj.getGradeDisciplinaComposta());
				} else {
					sqlInserir.setNull(3, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getGradeCurricularGrupoOptativaDisciplina())) {
					sqlInserir.setInt(4, obj.getGradeCurricularGrupoOptativaDisciplina());
				} else {
					sqlInserir.setNull(4, 0);
				}
				return sqlInserir;
			}
		}, new ResultSetExtractor() {

			public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));

		obj.setNovoObj(Boolean.FALSE);

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final DisciplinaPreRequisitoVO obj, UsuarioVO usuario) throws Exception {
		DisciplinaPreRequisitoVO.validarDados(obj);
		final String sql = "UPDATE DisciplinaPreRequisito set gradeDisciplina=?, disciplina=?, gradeDisciplinaComposta=?, gradeCurricularGrupoOptativaDisciplina=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if (Uteis.isAtributoPreenchido(obj.getGradeDisciplina())) {
					sqlAlterar.setInt(1, obj.getGradeDisciplina());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				sqlAlterar.setInt(2, obj.getDisciplina().getCodigo().intValue());
				if (Uteis.isAtributoPreenchido(obj.getGradeDisciplinaComposta())) {
					sqlAlterar.setInt(3, obj.getGradeDisciplinaComposta());
				} else {
					sqlAlterar.setNull(3, 0);
				}
				if (Uteis.isAtributoPreenchido(obj.getGradeCurricularGrupoOptativaDisciplina())) {
					sqlAlterar.setInt(4, obj.getGradeCurricularGrupoOptativaDisciplina());
				} else {
					sqlAlterar.setNull(4, 0);
				}
				sqlAlterar.setInt(5, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		})== 0){
			incluir(obj, usuario);
			return;
		};
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(DisciplinaPreRequisitoVO obj, UsuarioVO usuario) throws Exception {
		DisciplinaPreRequisito.excluir(getIdEntidade());
		String sql = "DELETE FROM DisciplinaPreRequisito WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public List<DisciplinaPreRequisitoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM DisciplinaPreRequisito WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>DisciplinaPreRequisitoVO</code> resultantes da consulta.
	 */
	public  List<DisciplinaPreRequisitoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<DisciplinaPreRequisitoVO> vetResultado = new ArrayList<DisciplinaPreRequisitoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>DisciplinaPreRequisitoVO</code>.
	 * 
	 * @return O objeto da classe <code>DisciplinaPreRequisitoVO</code> com os dados devidamente montados.
	 */
	public  DisciplinaPreRequisitoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		DisciplinaPreRequisitoVO obj = new DisciplinaPreRequisitoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setGradeDisciplina(new Integer(dadosSQL.getInt("gradeDisciplina")));
		obj.setGradeDisciplinaComposta(new Integer(dadosSQL.getInt("gradeDisciplinaComposta")));
		obj.setGradeCurricularGrupoOptativaDisciplina(new Integer(dadosSQL.getInt("gradeCurricularGrupoOptativaDisciplina")));
		obj.getDisciplina().setCodigo(new Integer(dadosSQL.getInt("disciplina")));
		obj.setNovoObj(Boolean.FALSE);
		montarDadosPreRequisito(obj, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>DisciplinaVO</code> relacionado ao objeto
	 * <code>DisciplinaPreRequisitoVO</code>. Faz uso da chave primária da classe <code>DisciplinaVO</code> para
	 * realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosPreRequisito(DisciplinaPreRequisitoVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getDisciplina().getCodigo().intValue() == 0) {
			obj.setDisciplina(new DisciplinaVO());
			return;
		}
		obj.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirDisciplinaPreRequisitos(Integer gradeDisciplina, UsuarioVO usuario) throws Exception {
		DisciplinaPreRequisito.excluir(getIdEntidade());
		String sql = "DELETE FROM DisciplinaPreRequisito WHERE (gradeDisciplina = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { gradeDisciplina });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDisciplinaPreRequisitos(Integer gradeDisciplina, List objetos, UsuarioVO usuario) throws Exception {
		incluirDisciplinaPreRequisitos(gradeDisciplina, objetos, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirDisciplinaPreRequisitos(Integer gradeDisciplinaPrm, List objetos, UsuarioVO usuario) throws Exception {
		String existentes = "";
		String aux = "";
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			DisciplinaPreRequisitoVO obj = (DisciplinaPreRequisitoVO) e.next();
			obj.setGradeDisciplina(gradeDisciplinaPrm);
			if (obj.getCodigo().equals(0)) {
				incluir(obj, usuario);
			} else {
				alterar(obj, usuario);
			}
			existentes += (aux + obj.getCodigo());
			aux = ",";
		}
		String sql = "DELETE FROM DisciplinaPreRequisito WHERE gradeDisciplina = ?";
		if (!objetos.isEmpty()) {
			sql += " and codigo not in (" + existentes + ")";
		}
		getConexao().getJdbcTemplate().update(sql + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), new Object[] { gradeDisciplinaPrm });
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDisciplinaGrupoOptativaPreRequisitos(Integer gradeCurricularGrupoOptativaDisciplina, List objetos, UsuarioVO usuario) throws Exception {
		incluirDisciplinaGrupoOptativaPreRequisitos(gradeCurricularGrupoOptativaDisciplina, objetos, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirDisciplinaGrupoOptativaPreRequisitos(Integer gradeCurricularGrupoOptativaDisciplinaPrm, List objetos, UsuarioVO usuario) throws Exception {
		String existentes = "";
		String aux = "";
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			DisciplinaPreRequisitoVO obj = (DisciplinaPreRequisitoVO) e.next();
			obj.setGradeCurricularGrupoOptativaDisciplina(gradeCurricularGrupoOptativaDisciplinaPrm);
			if (obj.getCodigo().equals(0)) {
				incluir(obj, usuario);
			} else {
				alterar(obj, usuario);
			}
			existentes += (aux + obj.getCodigo());
			aux = ",";
		}
		String sql = "DELETE FROM DisciplinaPreRequisito WHERE gradeCurricularGrupoOptativaDisciplina = ?";
		if (!objetos.isEmpty()) {
			sql += " and codigo not in (" + existentes + ")";
		}
		getConexao().getJdbcTemplate().update(sql + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), new Object[] { gradeCurricularGrupoOptativaDisciplinaPrm });
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDisciplinaCompostaPreRequisitos(Integer gradeDisciplinaCompostaPrm, List<DisciplinaPreRequisitoVO> objetos, UsuarioVO usuario) throws Exception {
		incluirDisciplinaCompostaPreRequisitos(gradeDisciplinaCompostaPrm, objetos, usuario);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirDisciplinaCompostaPreRequisitos(Integer gradeDisciplinaCompostaPrm, List<DisciplinaPreRequisitoVO> objetos, UsuarioVO usuario) throws Exception {
		String existentes = "";
		String aux = "";	
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			DisciplinaPreRequisitoVO obj = (DisciplinaPreRequisitoVO) e.next();
			obj.setGradeDisciplinaComposta(gradeDisciplinaCompostaPrm);
			if (obj.getCodigo().equals(0)) {
				incluir(obj, usuario);
			} else {
				alterar(obj, usuario);
			}
			existentes += (aux + obj.getCodigo());
			aux = ",";
		}
		String sql = "DELETE FROM DisciplinaPreRequisito WHERE gradeDisciplinaComposta = ?";
		if (!objetos.isEmpty()) {
			sql += " and codigo not in (" + existentes + ")";
		}
		getConexao().getJdbcTemplate().update(sql + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), new Object[] { gradeDisciplinaCompostaPrm });
	}

	/**
	 * Operação responsável por consultar todos os <code>DisciplinaPreRequisitoVO</code> relacionados a um objeto da
	 * classe <code>academico.Disciplina</code>.
	 * 
	 * @param disciplina
	 *            Atributo de <code>academico.Disciplina</code> a ser utilizado para localizar os objetos da classe
	 *            <code>DisciplinaPreRequisitoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>DisciplinaPreRequisitoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	
	
	
	
	
	public DisciplinaPreRequisitoVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM DisciplinaPreRequisito WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, usuario));
	}
	
	@Override
	public Boolean consultarDisciplinaEPreRequisito(Integer disciplina, Integer gradeCurricular){
		StringBuilder sql = new StringBuilder("select disciplinaprerequisito.codigo from disciplinaprerequisito ");
		sql.append(" inner join gradedisciplina gradedisciplina1 on gradedisciplina1.codigo = disciplinaprerequisito.gradedisciplina ");
		sql.append(" inner join periodoLetivo on periodoLetivo.codigo = gradedisciplina1.periodoLetivo ");		
		sql.append(" where disciplinaprerequisito.disciplina =  ").append(disciplina);
		sql.append(" and periodoLetivo.gradeCurricular =  ").append(gradeCurricular);
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString()).next();
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return DisciplinaPreRequisito.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		DisciplinaPreRequisito.idEntidade = idEntidade;
	}
	
	public List<DisciplinaPreRequisitoVO> consultarDisciplinaCompostaPreRequisitos(Integer gradeDisciplinaComposta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		List<DisciplinaPreRequisitoVO> objetos = new ArrayList<DisciplinaPreRequisitoVO>(0);
		String sql = "SELECT * FROM DisciplinaPreRequisito WHERE gradeDisciplinaComposta = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { gradeDisciplinaComposta });
		while (resultado.next()) {
			objetos.add(montarDados(resultado, usuario));
		}
		return objetos;
	}
	
	@Override
	public List<DisciplinaPreRequisitoVO> consultarGrupoOptativaPreRequisitos(Integer gradeCurricularGrupoOptativaDisciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		consultar(getIdEntidade(), controlarAcesso, usuario);
		List<DisciplinaPreRequisitoVO> objetos = new ArrayList<DisciplinaPreRequisitoVO>(0);
		String sql = "SELECT * FROM DisciplinaPreRequisito WHERE gradeCurricularGrupoOptativaDisciplina = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, gradeCurricularGrupoOptativaDisciplina);
		while (resultado.next()) {
			objetos.add(montarDados(resultado, usuario));
		}
		return objetos;
	}
	
}
