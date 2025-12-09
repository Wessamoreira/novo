package negocio.facade.jdbc.extensao;

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
import negocio.comuns.extensao.ProfessorCursoExtensaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.extensao.ProfessorCursoExtensaoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ProfessorCursoExtensaoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>ProfessorCursoExtensaoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see ProfessorCursoExtensaoVO
 * @see ControleAcesso
 * @see CursoExtensao
 */
@Repository
@Scope("singleton")
@Lazy 
public class ProfessorCursoExtensao extends ControleAcesso implements ProfessorCursoExtensaoInterfaceFacade{

	protected static String idEntidade;

	public ProfessorCursoExtensao() throws Exception {
		super();
		setIdEntidade("ProfessorCursoExtensao");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ProfessorCursoExtensaoVO</code>.
	 */
	public ProfessorCursoExtensaoVO novo() throws Exception {
		ProfessorCursoExtensao.incluir(getIdEntidade());
		ProfessorCursoExtensaoVO obj = new ProfessorCursoExtensaoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ProfessorCursoExtensaoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProfessorCursoExtensaoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ProfessorCursoExtensaoVO obj) throws Exception {
		try {
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 29/10/2014
			  *  Classe Subordinada
			  */
			// ProfessorCursoExtensao.incluir(getIdEntidade());
			ProfessorCursoExtensaoVO.validarDados(obj);
			final String sql = "INSERT INTO ProfessorCursoExtensao( tipoProfessor, cursoExtensao, cargaHoraria, pessoaProfessorCursoExtensao ) VALUES ( ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getTipoProfessor());
					sqlInserir.setInt(2, obj.getCursoExtensao().intValue());
					sqlInserir.setInt(3, obj.getCargaHoraria().intValue());
					sqlInserir.setInt(4, obj.getPessoaProfessorCursoExtensao().getCodigo().intValue());
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
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ProfessorCursoExtensaoVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProfessorCursoExtensaoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ProfessorCursoExtensaoVO obj) throws Exception {
		try {
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 29/10/2014
			  *  Classe Subordinada
			  */
			//ProfessorCursoExtensao.alterar(getIdEntidade());
			ProfessorCursoExtensaoVO.validarDados(obj);
			final String sql = "UPDATE ProfessorCursoExtensao set tipoProfessor=?, cursoExtensao=?, cargaHoraria=?, pessoaProfessorCursoExtensao=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getTipoProfessor());
					sqlAlterar.setInt(2, obj.getCursoExtensao().intValue());
					sqlAlterar.setInt(3, obj.getCargaHoraria().intValue());
					sqlAlterar.setInt(4, obj.getPessoaProfessorCursoExtensao().getCodigo().intValue());
					sqlAlterar.setInt(5, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ProfessorCursoExtensaoVO</code>. Sempre localiza
	 * o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ProfessorCursoExtensaoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ProfessorCursoExtensaoVO obj) throws Exception {
		try {
			/**
			  * @author Leonardo Riciolle 
			  * Comentado 28/10/2014
			  *  Classe Subordinada
			*/
			//ProfessorCursoExtensao.excluir(getIdEntidade());
			String sql = "DELETE FROM ProfessorCursoExtensao WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>ProfessorCursoExtensao</code> através do valor do atributo
	 * <code>Integer cargaHoraria</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ProfessorCursoExtensaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCargaHoraria(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ProfessorCursoExtensao WHERE cargaHoraria >= " + valorConsulta.intValue() + " ORDER BY cargaHoraria";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ProfessorCursoExtensao</code> através do valor do atributo
	 * <code>nome</code> da classe <code>CursoExtensao</code> Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProfessorCursoExtensaoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeCursoExtensao(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ProfessorCursoExtensao.* FROM ProfessorCursoExtensao, CursoExtensao WHERE ProfessorCursoExtensao.cursoExtensao = CursoExtensao.codigo and CursoExtensao.nome like('"
				+ valorConsulta + "%') ORDER BY CursoExtensao.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ProfessorCursoExtensao</code> através do valor do atributo
	 * <code>String tipoProfessor</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ProfessorCursoExtensaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoProfessor(String valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ProfessorCursoExtensao WHERE tipoProfessor like('" + valorConsulta + "%') ORDER BY tipoProfessor";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ProfessorCursoExtensao</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ProfessorCursoExtensaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ProfessorCursoExtensao WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado,usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ProfessorCursoExtensaoVO</code> resultantes da consulta.
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
	 * objeto da classe <code>ProfessorCursoExtensaoVO</code>.
	 * 
	 * @return O objeto da classe <code>ProfessorCursoExtensaoVO</code> com os dados devidamente montados.
	 */
	public static ProfessorCursoExtensaoVO montarDados(SqlRowSet dadosSQL,UsuarioVO usuario) throws Exception {
		ProfessorCursoExtensaoVO obj = new ProfessorCursoExtensaoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setTipoProfessor(dadosSQL.getString("tipoProfessor"));
		obj.setCursoExtensao(new Integer(dadosSQL.getInt("cursoExtensao")));
		obj.setCargaHoraria(new Integer(dadosSQL.getInt("cargaHoraria")));
		obj.getPessoaProfessorCursoExtensao().setCodigo(new Integer(dadosSQL.getInt("pessoaProfessorCursoExtensao")));
		obj.setNovoObj(Boolean.FALSE);
		montarDadosPessoaProfessorCursoExtensao(obj,usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
	 * <code>ProfessorCursoExtensaoVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPessoaProfessorCursoExtensao(ProfessorCursoExtensaoVO obj,UsuarioVO usuario) throws Exception {
		if (obj.getPessoaProfessorCursoExtensao().getCodigo().intValue() == 0) {
			obj.setPessoaProfessorCursoExtensao(new PessoaVO());
			return;
		}
		obj.setPessoaProfessorCursoExtensao(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoaProfessorCursoExtensao().getCodigo(), false,
				Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario));
	}

	/**
	 * Operação responsável por excluir todos os objetos da <code>ProfessorCursoExtensaoVO</code> no BD. Faz uso da
	 * operação <code>excluir</code> disponível na classe <code>ProfessorCursoExtensao</code>.
	 * 
	 * @param <code>cursoExtensao</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirProfessorCursoExtensaos(Integer cursoExtensao,UsuarioVO usuario) throws Exception {
		try {
			ProfessorCursoExtensao.excluir(getIdEntidade());
			String sql = "DELETE FROM ProfessorCursoExtensao WHERE (cursoExtensao = ?)";
			getConexao().getJdbcTemplate().update(sql, new Object[] { cursoExtensao.intValue() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar todos os objetos da <code>ProfessorCursoExtensaoVO</code> contidos em um
	 * Hashtable no BD. Faz uso da operação <code>excluirProfessorCursoExtensaos</code> e
	 * <code>incluirProfessorCursoExtensaos</code> disponíveis na classe <code>ProfessorCursoExtensao</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarProfessorCursoExtensaos(Integer cursoExtensao, List objetos,UsuarioVO usuario) throws Exception {
		excluirProfessorCursoExtensaos(cursoExtensao,usuario);
		incluirProfessorCursoExtensaos(cursoExtensao, objetos,usuario);
	}

	/**
	 * Operação responsável por incluir objetos da <code>ProfessorCursoExtensaoVO</code> no BD. Garantindo o
	 * relacionamento com a entidade principal <code>extensao.CursoExtensao</code> através do atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirProfessorCursoExtensaos(Integer cursoExtensaoPrm, List objetos,UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			ProfessorCursoExtensaoVO obj = (ProfessorCursoExtensaoVO) e.next();
			obj.setCursoExtensao(cursoExtensaoPrm);
			incluir(obj);
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>ProfessorCursoExtensaoVO</code> relacionados a um objeto da
	 * classe <code>extensao.CursoExtensao</code>.
	 * 
	 * @param cursoExtensao
	 *            Atributo de <code>extensao.CursoExtensao</code> a ser utilizado para localizar os objetos da classe
	 *            <code>ProfessorCursoExtensaoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>ProfessorCursoExtensaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public static List consultarProfessorCursoExtensaos(Integer cursoExtensao, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
		ProfessorCursoExtensao.consultar(getIdEntidade(), controlarAcesso,usuario);
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM ProfessorCursoExtensao WHERE cursoExtensao = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { cursoExtensao });
		while (resultado.next()) {
			objetos.add(ProfessorCursoExtensao.montarDados(resultado,usuario));
		}
		return objetos;
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ProfessorCursoExtensaoVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ProfessorCursoExtensaoVO consultarPorChavePrimaria(Integer codigoPrm,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT * FROM ProfessorCursoExtensao WHERE codigo = ?";
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
		return ProfessorCursoExtensao.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ProfessorCursoExtensao.idEntidade = idEntidade;
	}
}
