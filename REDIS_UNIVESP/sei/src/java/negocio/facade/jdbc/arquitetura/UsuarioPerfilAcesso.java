package negocio.facade.jdbc.arquitetura;

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

import negocio.comuns.arquitetura.Cliente;
import negocio.comuns.arquitetura.UsuarioPerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.arquitetura.UsuarioPerfilAcessoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>UsuarioPerfilAcessoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>UsuarioPerfilAcessoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see UsuarioPerfilAcessoVO
 * @see ControleAcesso
 * @see Cliente
 */
@Repository
@Scope("singleton")
@Lazy
public class UsuarioPerfilAcesso extends ControleAcesso implements UsuarioPerfilAcessoInterfaceFacade {

	protected static String idEntidade;

	public UsuarioPerfilAcesso() throws Exception {
		super();
		setIdEntidade("Usuario");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>UsuarioPerfilAcessoVO</code>.
	 */
	public UsuarioPerfilAcessoVO novo() throws Exception {
		UsuarioPerfilAcesso.incluir(getIdEntidade());
		UsuarioPerfilAcessoVO obj = new UsuarioPerfilAcessoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>UsuarioPerfilAcessoVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>UsuarioPerfilAcessoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final UsuarioPerfilAcessoVO obj, UsuarioVO usuarioLogado) throws Exception {
		UsuarioPerfilAcessoVO.validarDados(obj);
		final String sql = "INSERT INTO UsuarioPerfilAcesso( usuario, unidadeEnsino, perfilAcesso) VALUES ( ?, ?, ?) returning codigo"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlInserir = con.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getUsuario().intValue());
				if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(2, obj.getUnidadeEnsinoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(2, 0);
				}
				sqlInserir.setInt(3, obj.getPerfilAcesso().getCodigo().intValue());
				return sqlInserir;
			}
		}, new ResultSetExtractor() {

			public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return rs.getInt("codigo");
				}
				return null;
			}
		}));
		obj.setNovoObj(Boolean.FALSE);
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>UsuarioPerfilAcessoVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a
	 * ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>UsuarioPerfilAcessoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final UsuarioPerfilAcessoVO obj, UsuarioVO usuarioLogado) throws Exception {
		UsuarioPerfilAcessoVO.validarDados(obj);
		UsuarioPerfilAcesso.alterar(getIdEntidade());
		final String sql = "UPDATE UsuarioPerfilAcesso set usuario=?, unidadeEnsino=?, perfilAcesso=? WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getUsuario().intValue());
				if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(2, obj.getUnidadeEnsinoVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(2, 0);
				}
				sqlAlterar.setInt(3, obj.getPerfilAcesso().getCodigo().intValue());
				sqlAlterar.setInt(4, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>UsuarioPerfilAcessoVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente
	 * verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>UsuarioPerfilAcessoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(UsuarioPerfilAcessoVO obj, UsuarioVO usuarioLogado) throws Exception {
		UsuarioPerfilAcesso.excluir(getIdEntidade());
		String sql = "DELETE FROM UsuarioPerfilAcesso WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	/**
	 * Responsável por realizar uma consulta de <code>UsuarioPerfilAcesso</code> através do valor do atributo <code>nome</code> da classe <code>Cidade</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>UsuarioPerfilAcessoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeUnidadeEnsino(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT UsuarioPerfilAcesso.* FROM UsuarioPerfilAcesso, UnidadeEnsino WHERE UsuarioPerfilAcesso.unidadeEnsino = unidadeensino.codigo and upper( unidadeensino.nome) like('"
				+ valorConsulta.toUpperCase() + "%') ORDER BY UnidadeEnsino.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>UsuarioPerfilAcesso</code> através do valor do atributo <code>nome</code> da classe <code>Cliente</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>UsuarioPerfilAcessoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeUsuario(String valorConsulta, boolean verificaPermissao, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), verificaPermissao, usuario);
		String sqlStr = "SELECT UsuarioPerfilAcesso.* FROM UsuarioPerfilAcesso, Usuario WHERE UsuarioPerfilAcesso.usuario = usuario.codigo and upper( usuario.nome ) like('"
				+ valorConsulta.toUpperCase() + "%') ORDER BY usuario.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>UsuarioPerfilAcesso</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao
	 * parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>UsuarioPerfilAcessoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM UsuarioPerfilAcesso WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho
	 * para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>UsuarioPerfilAcessoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>UsuarioPerfilAcessoVO</code>.
	 * 
	 * @return O objeto da classe <code>UsuarioPerfilAcessoVO</code> com os dados devidamente montados.
	 */
	public static UsuarioPerfilAcessoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		UsuarioPerfilAcessoVO obj = new UsuarioPerfilAcessoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setUsuario(new Integer(dadosSQL.getInt("usuario")));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.getPerfilAcesso().setCodigo(new Integer(dadosSQL.getInt("perfilAcesso")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosPerfilAcesso(obj, nivelMontarDados, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>CidadeVO</code> relacionado ao objeto <code>UsuarioPerfilAcessoVO</code>. Faz uso da chave primária da classe
	 * <code>CidadeVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosUnidadeEnsino(UsuarioPerfilAcessoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
			return;
		}
		obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>CidadeVO</code> relacionado ao objeto <code>UsuarioPerfilAcessoVO</code>. Faz uso da chave primária da classe
	 * <code>CidadeVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPerfilAcesso(UsuarioPerfilAcessoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getPerfilAcesso().getCodigo().intValue() == 0) {
			return;
		}
		obj.setPerfilAcesso(getFacadeFactory().getPerfilAcessoFacade().consultarPorChavePrimaria(obj.getPerfilAcesso().getCodigo(), usuario));
	}

	/**
	 * Operação responsável por excluir todos os objetos da <code>UsuarioPerfilAcessoVO</code> no BD. Faz uso da operação <code>excluir</code> disponível na classe <code>UsuarioPerfilAcesso</code>.
	 * 
	 * @param <code>cliente</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirUsuarioPerfilAcesso(Integer usuario, UsuarioVO usuarioLogado) throws Exception {
		String sql = "DELETE FROM UsuarioPerfilAcesso WHERE (usuario = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioLogado);
		getConexao().getJdbcTemplate().update(sql, new Object[] { usuario.intValue() });
	}

	/**
	 * Operação responsável por alterar todos os objetos da <code>UsuarioPerfilAcessoVO</code> contidos em um Hashtable no BD. Faz uso da operação <code>excluirUsuarioPerfilAcessos</code> e
	 * <code>incluirUsuarioPerfilAcessos</code> disponíveis na classe <code>UsuarioPerfilAcesso</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarUsuarioPerfilAcesso(Integer usuario, List<UsuarioPerfilAcessoVO> objetos, UsuarioVO usuarioLogado) throws Exception {
		excluirUsuarioPerfilAcesso(usuario, usuarioLogado);
		incluirUsuarioPerfilAcesso(usuario, objetos, usuarioLogado);
	}

	/**
	 * Operação responsável por incluir objetos da <code>UsuarioPerfilAcessoVO</code> no BD. Garantindo o relacionamento com a entidade principal <code>cadastro.Cliente</code> através do atributo de
	 * vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirUsuarioPerfilAcesso(Integer usuarioprm, List<UsuarioPerfilAcessoVO> objetos, UsuarioVO usuarioLogado) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			UsuarioPerfilAcessoVO obj = (UsuarioPerfilAcessoVO) e.next();
			obj.setUsuario(usuarioprm);
			incluir(obj, usuarioLogado);
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>UsuarioPerfilAcessoVO</code> relacionados a um objeto da classe <code>cadastro.Cliente</code>.
	 * 
	 * @param cliente
	 *            Atributo de <code>cadastro.Cliente</code> a ser utilizado para localizar os objetos da classe <code>UsuarioPerfilAcessoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>UsuarioPerfilAcessoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public List consultarUsuarioPerfilAcesso(Integer usuario, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		UsuarioPerfilAcesso.consultar(getIdEntidade());
		List objetos = new ArrayList();
		String sql = "SELECT * FROM UsuarioPerfilAcesso WHERE usuario = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, usuario.intValue());
		while (resultado.next()) {
			objetos.add(UsuarioPerfilAcesso.montarDados(resultado, nivelMontarDados, usuarioVO));
		}
		return objetos;
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>UsuarioPerfilAcessoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public UsuarioPerfilAcessoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM UsuarioPerfilAcesso WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( UsuarioPerfilAcesso ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return UsuarioPerfilAcesso.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos.
	 * Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		UsuarioPerfilAcesso.idEntidade = idEntidade;
	}

}
