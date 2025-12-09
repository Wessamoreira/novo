package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisciplinaEquivalenteVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.DisciplinaEquivalenteInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>DisciplinaEquivalenteVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>DisciplinaEquivalenteVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see DisciplinaEquivalenteVO
 * @see ControleAcesso
 * @see Disciplina
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy 
public class DisciplinaEquivalente extends ControleAcesso implements DisciplinaEquivalenteInterfaceFacade {

	protected static String idEntidade;

	public DisciplinaEquivalente() throws Exception {
		super();
		setIdEntidade("Disciplina");
	}

	public DisciplinaEquivalenteVO novo() throws Exception {
		DisciplinaEquivalente.incluir(getIdEntidade());
		DisciplinaEquivalenteVO obj = new DisciplinaEquivalenteVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final DisciplinaEquivalenteVO obj, UsuarioVO usuario) throws Exception {
		DisciplinaEquivalenteVO.validarDados(obj);
		final String sql = "INSERT INTO DisciplinaEquivalente( disciplina, equivalente ) VALUES ( ?, ? )"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getDisciplina().intValue());
				sqlInserir.setInt(2, obj.getEquivalente().getCodigo().intValue());
				return sqlInserir;
			}
		});

		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final DisciplinaEquivalenteVO obj, UsuarioVO usuario) throws Exception {
		DisciplinaEquivalenteVO.validarDados(obj);
		final String sql = "UPDATE DisciplinaEquivalente set  WHERE ((disciplina = ?) and (equivalente = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getDisciplina().intValue());
				sqlAlterar.setInt(2, obj.getEquivalente().getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(DisciplinaEquivalenteVO obj, UsuarioVO usuario) throws Exception {
		DisciplinaEquivalente.excluir(getIdEntidade());
		String sql = "DELETE FROM DisciplinaEquivalente WHERE ((disciplina = ?) and (equivalente = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getDisciplina(), obj.getEquivalente() });
	}

	public List consultarPorNomeDisciplina(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT DisciplinaEquivalente.* FROM DisciplinaEquivalente, Disciplina WHERE DisciplinaEquivalente.disciplina = Disciplina.codigo and Disciplina.nome like('" + valorConsulta
				+ "%') ORDER BY Disciplina.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, usuario);
	}
	
	public DisciplinaEquivalenteVO consultarDisciplinaEquivalentePorDisciplinaEEquivalente(Integer disciplina, Integer equivalente, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select equivalente.nome AS \"equivalente.nome\", equivalente.codigo AS \"equivalente.codigo\" ");
		sb.append(" from disciplinaequivalente ");
		sb.append(" inner join disciplina equivalente on equivalente.codigo = disciplinaequivalente.equivalente ");
		sb.append(" where disciplina = ").append(disciplina).append(" and equivalente = ").append(equivalente);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		DisciplinaEquivalenteVO obj = null;
		if (tabelaResultado.next()) {
			obj = new DisciplinaEquivalenteVO();
			obj.getEquivalente().setCodigo(tabelaResultado.getInt("equivalente.codigo"));
			obj.getEquivalente().setNome(tabelaResultado.getString("equivalente.nome"));
		}
		return obj;
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>DisciplinaEquivalenteVO</code> resultantes da consulta.
	 */
	public  List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>DisciplinaEquivalenteVO</code>.
	 * 
	 * @return O objeto da classe <code>DisciplinaEquivalenteVO</code> com os dados devidamente montados.
	 */
	public  DisciplinaEquivalenteVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		DisciplinaEquivalenteVO obj = new DisciplinaEquivalenteVO();
		obj.setDisciplina(new Integer(dadosSQL.getInt("disciplina")));
		obj.getEquivalente().setCodigo(new Integer(dadosSQL.getInt("equivalente")));
		obj.setNovoObj(Boolean.FALSE);
		montarDadosEquivalente(obj, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>DisciplinaVO</code> relacionado ao objeto
	 * <code>DisciplinaEquivalenteVO</code>. Faz uso da chave primária da classe <code>DisciplinaVO</code> para realizar
	 * a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public  void montarDadosEquivalente(DisciplinaEquivalenteVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getEquivalente().getCodigo().intValue() == 0) {
			obj.setEquivalente(new DisciplinaVO());
			return;
		}
		obj.setEquivalente(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimariaUnica(obj.getEquivalente().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirDisciplinaEquivalentes(Integer disciplina, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM DisciplinaEquivalente WHERE (disciplina = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { disciplina });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDisciplinaEquivalentes(Integer disciplina, List objetos, UsuarioVO usuario) throws Exception {
		excluirDisciplinaEquivalentes(disciplina, usuario);
		incluirDisciplinaEquivalentes(disciplina, objetos, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirDisciplinaEquivalentes(Integer disciplinaPrm, List objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			DisciplinaEquivalenteVO obj = (DisciplinaEquivalenteVO) e.next();
			obj.setDisciplina(disciplinaPrm);
			incluir(obj, usuario);
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>DisciplinaEquivalenteVO</code> relacionados a um objeto da
	 * classe <code>academico.Disciplina</code>.
	 * 
	 * @param disciplina
	 *            Atributo de <code>academico.Disciplina</code> a ser utilizado para localizar os objetos da classe
	 *            <code>DisciplinaEquivalenteVO</code>.
	 * @return List Contendo todos os objetos da classe <code>DisciplinaEquivalenteVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public List consultarDisciplinaEquivalentes(Integer disciplina, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM DisciplinaEquivalente WHERE disciplina = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { disciplina });
		while (resultado.next()) {
			objetos.add(montarDados(resultado, usuario));
		}
		return objetos;
	}

	public DisciplinaEquivalenteVO consultarPorChavePrimaria(Integer disciplinaPrm, Integer equivalentePrm, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM DisciplinaEquivalente WHERE disciplina = ?, equivalente = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { disciplinaPrm, equivalentePrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return DisciplinaEquivalente.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		DisciplinaEquivalente.idEntidade = idEntidade;
	}
	
	@Override
	public void adicionarDisciplinaEquivalente(DisciplinaVO disciplinaVO, DisciplinaVO disciplinaEquivalenteVO, UsuarioVO usuarioVO) throws Exception {
		if (disciplinaEquivalenteVO.getCodigo().equals(disciplinaVO.getCodigo())) {
			throw new Exception("Disciplina Equivalente Não Pode Ser Igual A Disciplina.");
		}
		if(!disciplinaVO.getDisciplinaEquivalenteVOs().stream().anyMatch(e -> e.getEquivalente().getCodigo().equals(disciplinaEquivalenteVO.getCodigo()))) {
			DisciplinaEquivalenteVO disciplinaEquivalenteVO2 = new DisciplinaEquivalenteVO();
			disciplinaEquivalenteVO2.setEquivalente(disciplinaEquivalenteVO);
			disciplinaVO.getDisciplinaEquivalenteVOs().add(disciplinaEquivalenteVO2);
		}
	}
}