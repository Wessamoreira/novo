package negocio.facade.jdbc.eventos;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.eventos.AutorTrabalhoSubmetidoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.eventos.AutorTrabalhoSubmetidoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>AutorTrabalhoSubmetidoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>AutorTrabalhoSubmetidoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see AutorTrabalhoSubmetidoVO
 * @see ControleAcesso
 * @see TrabalhoSubmetido
 */
@Repository
@Scope("singleton")
@Lazy 
public class AutorTrabalhoSubmetido extends ControleAcesso implements AutorTrabalhoSubmetidoInterfaceFacade{

	protected static String idEntidade;

	public AutorTrabalhoSubmetido() throws Exception {
		super();
		setIdEntidade("TrabalhoSubmetido");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>AutorTrabalhoSubmetidoVO</code>.
	 */
	public AutorTrabalhoSubmetidoVO novo() throws Exception {
		AutorTrabalhoSubmetido.incluir(getIdEntidade());
		AutorTrabalhoSubmetidoVO obj = new AutorTrabalhoSubmetidoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>AutorTrabalhoSubmetidoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>AutorTrabalhoSubmetidoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final AutorTrabalhoSubmetidoVO obj) throws Exception {

		try {
			/**
             * Comentado 22/10/2014
             * @author Leonardo Riciolle
             */
			//AutorTrabalhoSubmetido.incluir(getIdEntidade());
			AutorTrabalhoSubmetidoVO.validarDados(obj);
			final String sql = "INSERT INTO AutorTrabalhoSubmetido( trabalhoSubmetido, pessoaAutorTrabalhoSubmetido ) VALUES ( ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getTrabalhoSubmetido().intValue());
					sqlInserir.setInt(2, obj.getPessoaAutorTrabalhoSubmetido().getCodigo().intValue());
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
		} catch (Exception e) {
			obj.setNovoObj(true);
			throw e;
		}

	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>AutorTrabalhoSubmetidoVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>AutorTrabalhoSubmetidoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AutorTrabalhoSubmetidoVO obj) throws Exception {
		try {
			/**
             * Comentado 22/10/2014
             * @author Leonardo Riciolle
             */
			// AutorTrabalhoSubmetido.alterar(getIdEntidade());
			AutorTrabalhoSubmetidoVO.validarDados(obj);
			final String sql = "UPDATE AutorTrabalhoSubmetido set descricao=?, contaDebito=?, contaCredito=?, historico=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getTrabalhoSubmetido().intValue());
					sqlAlterar.setInt(2, obj.getPessoaAutorTrabalhoSubmetido().getCodigo().intValue());
					sqlAlterar.setInt(3, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>AutorTrabalhoSubmetidoVO</code>. Sempre localiza
	 * o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>AutorTrabalhoSubmetidoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(AutorTrabalhoSubmetidoVO obj) throws Exception {
		try {
			/**
             * Comentado 22/10/2014
             * @author Leonardo Riciolle
             */
			//AutorTrabalhoSubmetido.excluir(getIdEntidade());
			String sql = "DELETE FROM AutorTrabalhoSubmetido WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>AutorTrabalhoSubmetido</code> através do valor do atributo
	 * <code>evento</code> da classe <code>TrabalhoSubmetido</code> Faz uso da operação <code>montarDadosConsulta</code>
	 * que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>AutorTrabalhoSubmetidoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorEventoTrabalhoSubmetido(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT AutorTrabalhoSubmetido.* FROM AutorTrabalhoSubmetido, TrabalhoSubmetido WHERE AutorTrabalhoSubmetido.trabalhoSubmetido = TrabalhoSubmetido.codigo and TrabalhoSubmetido.evento >= "
				+ valorConsulta.intValue() + " ORDER BY TrabalhoSubmetido.evento";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>AutorTrabalhoSubmetido</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>AutorTrabalhoSubmetidoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM AutorTrabalhoSubmetido WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>AutorTrabalhoSubmetidoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado,UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado,usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>AutorTrabalhoSubmetidoVO</code>.
	 * 
	 * @return O objeto da classe <code>AutorTrabalhoSubmetidoVO</code> com os dados devidamente montados.
	 */
	public static AutorTrabalhoSubmetidoVO montarDados(SqlRowSet dadosSQL,UsuarioVO usuario) throws Exception {
		AutorTrabalhoSubmetidoVO obj = new AutorTrabalhoSubmetidoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setTrabalhoSubmetido(new Integer(dadosSQL.getInt("trabalhoSubmetido")));
		obj.getPessoaAutorTrabalhoSubmetido().setCodigo(new Integer(dadosSQL.getInt("pessoaAutorTrabalhoSubmetido")));
		obj.setNovoObj(Boolean.FALSE);

		montarDadosPessoaAutorTrabalhoSubmetido(obj,usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
	 * <code>AutorTrabalhoSubmetidoVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPessoaAutorTrabalhoSubmetido(AutorTrabalhoSubmetidoVO obj,UsuarioVO usuario) throws Exception {
		if (obj.getPessoaAutorTrabalhoSubmetido().getCodigo().intValue() == 0) {
			obj.setPessoaAutorTrabalhoSubmetido(new PessoaVO());
			return;
		}
		obj.setPessoaAutorTrabalhoSubmetido(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoaAutorTrabalhoSubmetido().getCodigo(), false,
				Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario));
	}

	/**
	 * Operação responsável por excluir todos os objetos da <code>AutorTrabalhoSubmetidoVO</code> no BD. Faz uso da
	 * operação <code>excluir</code> disponível na classe <code>AutorTrabalhoSubmetido</code>.
	 * 
	 * @param <code>trabalhoSubmetido</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirAutorTrabalhoSubmetidos(Integer trabalhoSubmetido) throws Exception {

		AutorTrabalhoSubmetido.excluir(getIdEntidade());
		String sql = "DELETE FROM AutorTrabalhoSubmetido WHERE (trabalhoSubmetido = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { trabalhoSubmetido });

	}

	/**
	 * Operação responsável por alterar todos os objetos da <code>AutorTrabalhoSubmetidoVO</code> contidos em um
	 * Hashtable no BD. Faz uso da operação <code>excluirAutorTrabalhoSubmetidos</code> e
	 * <code>incluirAutorTrabalhoSubmetidos</code> disponíveis na classe <code>AutorTrabalhoSubmetido</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAutorTrabalhoSubmetidos(Integer trabalhoSubmetido, List objetos) throws Exception {
		excluirAutorTrabalhoSubmetidos(trabalhoSubmetido);
		incluirAutorTrabalhoSubmetidos(trabalhoSubmetido, objetos);
	}

	/**
	 * Operação responsável por incluir objetos da <code>AutorTrabalhoSubmetidoVO</code> no BD. Garantindo o
	 * relacionamento com a entidade principal <code>eventos.TrabalhoSubmetido</code> através do atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirAutorTrabalhoSubmetidos(Integer trabalhoSubmetidoPrm, List objetos) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			AutorTrabalhoSubmetidoVO obj = (AutorTrabalhoSubmetidoVO) e.next();
			obj.setTrabalhoSubmetido(trabalhoSubmetidoPrm);
			incluir(obj);
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>AutorTrabalhoSubmetidoVO</code> relacionados a um objeto da
	 * classe <code>eventos.TrabalhoSubmetido</code>.
	 * 
	 * @param trabalhoSubmetido
	 *            Atributo de <code>eventos.TrabalhoSubmetido</code> a ser utilizado para localizar os objetos da classe
	 *            <code>AutorTrabalhoSubmetidoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>AutorTrabalhoSubmetidoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public static List consultarAutorTrabalhoSubmetidos(Integer trabalhoSubmetido,UsuarioVO usuario) throws Exception {

		List objetos = new ArrayList(0);
		ControleAcesso.consultar(getIdEntidade(), true,usuario);
		String sql = "SELECT * FROM AutorTrabalhoSubmetido WHERE trabalhoSubmetido = " + trabalhoSubmetido;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		while (tabelaResultado.next()) {
			AutorTrabalhoSubmetidoVO novoObj = new AutorTrabalhoSubmetidoVO();
			novoObj = AutorTrabalhoSubmetido.montarDados(tabelaResultado,usuario);
			objetos.add(novoObj);
		}
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>AutorTrabalhoSubmetidoVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public AutorTrabalhoSubmetidoVO consultarPorChavePrimaria(Integer codigoPrm,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT * FROM AutorTrabalhoSubmetido WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado,usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return AutorTrabalhoSubmetido.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		AutorTrabalhoSubmetido.idEntidade = idEntidade;
	}
}
