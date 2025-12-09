package negocio.facade.jdbc.biblioteca;

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
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.ItemRegistroSaidaAcervoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoSaidaAcervo;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.biblioteca.ItemRegistroSaidaAcervoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ItemRegistroSaidaAcervoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>ItemRegistroSaidaAcervoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see ItemRegistroSaidaAcervoVO
 * @see ControleAcesso
 * @see RegistroSaidaAcervo
 */
@Repository
@Scope("singleton")
@Lazy 
public class ItemRegistroSaidaAcervo extends ControleAcesso implements ItemRegistroSaidaAcervoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7998709245527233919L;
	protected static String idEntidade;

	public ItemRegistroSaidaAcervo() throws Exception {
		super();
		setIdEntidade("RegistroSaidaAcervo");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ItemRegistroSaidaAcervoVO</code>.
	 */
	public ItemRegistroSaidaAcervoVO novo() throws Exception {
		ItemRegistroSaidaAcervo.incluir(getIdEntidade());
		ItemRegistroSaidaAcervoVO obj = new ItemRegistroSaidaAcervoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ItemRegistroSaidaAcervoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItemRegistroSaidaAcervoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ItemRegistroSaidaAcervoVO obj) throws Exception {
		try {
			ItemRegistroSaidaAcervoVO.validarDados(obj);
			/**
			 * @author Leonardo Riciolle 
			 * Comentado 28/10/2014
			 *  Classe Subordinada
			 */
			// ItemRegistroSaidaAcervo.incluir(getIdEntidade());
			obj.realizarUpperCaseDados();

			final String sql = "INSERT INTO ItemRegistroSaidaAcervo( exemplar, registroSaidaAcervo, tipoSaida ) VALUES ( ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getExemplar().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getExemplar().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (obj.getRegistroSaidaAcervo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getRegistroSaidaAcervo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					sqlInserir.setString(3, obj.getTipoSaida());
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
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ItemRegistroSaidaAcervoVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItemRegistroSaidaAcervoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ItemRegistroSaidaAcervoVO obj) throws Exception {
		try {
			ItemRegistroSaidaAcervoVO.validarDados(obj);
			/**
			 * @author Leonardo Riciolle 
			 * Comentado 28/10/2014
			 *  Classe Subordinada
			 */
			// ItemRegistroSaidaAcervo.alterar(getIdEntidade());
			obj.realizarUpperCaseDados();

			final String sql = "UPDATE ItemRegistroSaidaAcervo set exemplar=?, registroSaidaAcervo=?, tipoSaida=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getExemplar().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getExemplar().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (obj.getRegistroSaidaAcervo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getRegistroSaidaAcervo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					sqlAlterar.setString(3, obj.getTipoSaida());
					sqlAlterar.setInt(4, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ItemRegistroSaidaAcervoVO</code>. Sempre
	 * localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItemRegistroSaidaAcervoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ItemRegistroSaidaAcervoVO obj) throws Exception {
		try {
			/**
			 * @author Leonardo Riciolle 
			 * Comentado 28/10/2014
			 *  Classe Subordinada
			 */
			// ItemRegistroSaidaAcervo.excluir(getIdEntidade());
			String sql = "DELETE FROM ItemRegistroSaidaAcervo WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>ItemRegistroSaidaAcervo</code> através do valor do atributo
	 * <code>String tipoSaida</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ItemRegistroSaidaAcervoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoSaida(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ItemRegistroSaidaAcervo WHERE upper( tipoSaida ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY tipoSaida";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ItemRegistroSaidaAcervo</code> através do valor do atributo
	 * <code>codigo</code> da classe <code>RegistroSaidaAcervo</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ItemRegistroSaidaAcervoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoRegistroSaidaAcervo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ItemRegistroSaidaAcervo.* FROM ItemRegistroSaidaAcervo, RegistroSaidaAcervo WHERE ItemRegistroSaidaAcervo.registroSaidaAcervo = RegistroSaidaAcervo.codigo and RegistroSaidaAcervo.codigo >= "
				+ valorConsulta.intValue() + " ORDER BY RegistroSaidaAcervo.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ItemRegistroSaidaAcervo</code> através do valor do atributo
	 * <code>codigo</code> da classe <code>Exemplar</code> Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ItemRegistroSaidaAcervoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoExemplar(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT ItemRegistroSaidaAcervo.* FROM ItemRegistroSaidaAcervo, Exemplar WHERE ItemRegistroSaidaAcervo.exemplar = Exemplar.codigo and Exemplar.codigo >= "
				+ valorConsulta.intValue() + " ORDER BY Exemplar.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>ItemRegistroSaidaAcervo</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ItemRegistroSaidaAcervoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ItemRegistroSaidaAcervo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ItemRegistroSaidaAcervoVO</code> resultantes da consulta.
	 */
	public static List<ItemRegistroSaidaAcervoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<ItemRegistroSaidaAcervoVO> vetResultado = new ArrayList<ItemRegistroSaidaAcervoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>ItemRegistroSaidaAcervoVO</code>.
	 * 
	 * @return O objeto da classe <code>ItemRegistroSaidaAcervoVO</code> com os dados devidamente montados.
	 */
	public static ItemRegistroSaidaAcervoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ItemRegistroSaidaAcervoVO obj = new ItemRegistroSaidaAcervoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getExemplar().setCodigo(dadosSQL.getInt("exemplar"));
		obj.setRegistroSaidaAcervo(dadosSQL.getInt("registroSaidaAcervo"));
		obj.setTipoSaida(dadosSQL.getString("tipoSaida"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosExemplar(obj, nivelMontarDados, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>ExemplarVO</code> relacionado ao objeto
	 * <code>ItemRegistroSaidaAcervoVO</code>. Faz uso da chave primária da classe <code>ExemplarVO</code> para realizar
	 * a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosExemplar(ItemRegistroSaidaAcervoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getExemplar().getCodigo().intValue() == 0) {
			obj.setExemplar(new ExemplarVO());
			return;
		}
		getFacadeFactory().getExemplarFacade().carregarDados(obj.getExemplar(), 0, NivelMontarDados.BASICO, usuario);
	}

	/**
	 * Operação responsável por excluir todos os objetos da <code>ItemRegistroSaidaAcervoVO</code> no BD. Faz uso da
	 * operação <code>excluir</code> disponível na classe <code>ItemRegistroSaidaAcervo</code>.
	 * 
	 * @param <code>registroSaidaAcervo</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirItemRegistroSaidaAcervos(Integer registroSaidaAcervo) throws Exception {
		try {
			ItemRegistroSaidaAcervo.excluir(getIdEntidade());
			String sql = "DELETE FROM ItemRegistroSaidaAcervo WHERE (registroSaidaAcervo = ?)";
			getConexao().getJdbcTemplate().update(sql, new Object[] { registroSaidaAcervo });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar todos os objetos da <code>ItemRegistroSaidaAcervoVO</code> contidos em um
	 * Hashtable no BD. Faz uso da operação <code>excluirItemRegistroSaidaAcervos</code> e
	 * <code>incluirItemRegistroSaidaAcervos</code> disponíveis na classe <code>ItemRegistroSaidaAcervo</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarItemRegistroSaidaAcervos(Integer registroSaidaAcervo, List objetos) throws Exception {
		try {

			String sql = "DELETE FROM ItemRegistroSaidaAcervo WHERE registroSaidaAcervo = " + registroSaidaAcervo;
			Iterator i = objetos.iterator();
			while (i.hasNext()) {
				ItemRegistroSaidaAcervoVO objeto = (ItemRegistroSaidaAcervoVO) i.next();
				sql += " AND codigo <> " + objeto.getCodigo().intValue();
			}
			getConexao().getJdbcTemplate().update(sql);
			Iterator e = objetos.iterator();
			while (e.hasNext()) {
				ItemRegistroSaidaAcervoVO objeto = (ItemRegistroSaidaAcervoVO) e.next();
				if (objeto.getCodigo().equals(0)) {
					incluir(objeto);
				} else {
					alterar(objeto);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por incluir objetos da <code>ItemRegistroSaidaAcervoVO</code> no BD. Garantindo o
	 * relacionamento com a entidade principal <code>biblioteca.RegistroSaidaAcervo</code> através do atributo de
	 * vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirItemRegistroSaidaAcervos(Integer registroSaidaAcervoPrm, List objetos) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			ItemRegistroSaidaAcervoVO obj = (ItemRegistroSaidaAcervoVO) e.next();
			obj.setRegistroSaidaAcervo(registroSaidaAcervoPrm);
			incluir(obj);
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>ItemRegistroSaidaAcervoVO</code> relacionados a um objeto da
	 * classe <code>biblioteca.RegistroSaidaAcervo</code>.
	 * 
	 * @param registroSaidaAcervo
	 *            Atributo de <code>biblioteca.RegistroSaidaAcervo</code> a ser utilizado para localizar os objetos da
	 *            classe <code>ItemRegistroSaidaAcervoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>ItemRegistroSaidaAcervoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public List consultarItemRegistroSaidaAcervos(Integer registroSaidaAcervo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ItemRegistroSaidaAcervo.consultar(getIdEntidade(), controlarAcesso, usuario);
		List objetos = new ArrayList(0);
		String sqlStr = "SELECT * FROM ItemRegistroSaidaAcervo WHERE registroSaidaAcervo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { registroSaidaAcervo });
		return (montarDadosConsulta(tabelaResultado, usuario));

	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ItemRegistroSaidaAcervoVO</code> através de sua
	 * chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ItemRegistroSaidaAcervoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sqlStr = "SELECT * FROM ItemRegistroSaidaAcervo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ItemRegistroSaidaAcervo ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return ItemRegistroSaidaAcervo.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ItemRegistroSaidaAcervo.idEntidade = idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorExemplar(Integer exemplar, UsuarioVO usuarioVO) throws Exception {
		try {			
			String sql = "DELETE FROM ItemRegistroSaidaAcervo WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, exemplar);			
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Override
	public String consultarUltimoTipoSaidaExemplar(Integer codigoExemplar) throws Exception{
		StringBuilder sql  = new StringBuilder("select ItemRegistroSaidaAcervo.tiposaida, registroSaidaAcervo.data from ItemRegistroSaidaAcervo inner join registroSaidaAcervo on registroSaidaAcervo.codigo = ItemRegistroSaidaAcervo.registroSaidaAcervo ");
		sql.append(" where exemplar = ").append(codigoExemplar).append(" order by registroSaidaAcervo.data desc limit 1");
		SqlRowSet rs  =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if(rs.next()){
			try{
				return TipoSaidaAcervo.getDescricao(rs.getString("tiposaida"));
			}catch(Exception e){
				return "";
			}
		}
		return "";
	}
}