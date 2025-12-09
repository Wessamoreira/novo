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
import negocio.comuns.eventos.CursosEventoVO;
import negocio.comuns.eventos.InscricaoCursoEventoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.eventos.InscricaoCursoEventoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>InscricaoCursoEventoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>InscricaoCursoEventoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see InscricaoCursoEventoVO
 * @see ControleAcesso
 * @see InscricaoEvento
 */
@Repository
@Scope("singleton")
@Lazy 
public class InscricaoCursoEvento extends ControleAcesso implements InscricaoCursoEventoInterfaceFacade{

	protected static String idEntidade;

	public InscricaoCursoEvento() throws Exception {
		super();
		setIdEntidade("InscricaoCursoEvento");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>InscricaoCursoEventoVO</code>.
	 */
	public InscricaoCursoEventoVO novo() throws Exception {
		InscricaoCursoEvento.incluir(getIdEntidade());
		InscricaoCursoEventoVO obj = new InscricaoCursoEventoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>InscricaoCursoEventoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InscricaoCursoEventoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final InscricaoCursoEventoVO obj) throws Exception {
		try {
			/**
             * @author Leonardo Riciolle
             * Comentado 28/10/2014
             * Classe Subordinada
             */
			//InscricaoCursoEvento.incluir(getIdEntidade());
			InscricaoCursoEventoVO.validarDados(obj);
			final String sql = "INSERT INTO InscricaoCursoEvento( inscricaoEvento, cursosEvento, valorInscricao ) VALUES ( ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getInscricaoEvento().intValue());
					sqlInserir.setInt(2, obj.getCursosEvento().getCodigo().intValue());
					sqlInserir.setDouble(3, obj.getValorInscricao().doubleValue());
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
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>InscricaoCursoEventoVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InscricaoCursoEventoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final InscricaoCursoEventoVO obj) throws Exception {
		try {
			/**
             * @author Leonardo Riciolle
             * Comentado 28/10/2014
             * Classe Subordinada
             */
			// InscricaoCursoEvento.alterar(getIdEntidade());
			InscricaoCursoEventoVO.validarDados(obj);
			final String sql = "UPDATE Paiz set nome=?, nacao=?, siglainep=?, nacionalidade=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getInscricaoEvento().intValue());
					sqlAlterar.setInt(2, obj.getCursosEvento().getCodigo().intValue());
					sqlAlterar.setDouble(3, obj.getValorInscricao().doubleValue());
					sqlAlterar.setInt(4, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>InscricaoCursoEventoVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InscricaoCursoEventoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(InscricaoCursoEventoVO obj) throws Exception {
		try {
			/**
             * @author Leonardo Riciolle
             * Comentado 28/10/2014
             * Classe Subordinada
             */
			// InscricaoCursoEvento.excluir(getIdEntidade());
			String sql = "DELETE FROM InscricaoCursoEvento WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoCursoEvento</code> através do valor do atributo
	 * <code>Double valorInscricao</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoCursoEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorInscricao(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM InscricaoCursoEvento WHERE valorInscricao >= " + valorConsulta.doubleValue() + " ORDER BY valorInscricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoCursoEvento</code> através do valor do atributo
	 * <code>codigo</code> da classe <code>CursosEvento</code> Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>InscricaoCursoEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoCursosEvento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT InscricaoCursoEvento.* FROM InscricaoCursoEvento, CursosEvento WHERE InscricaoCursoEvento.cursosEvento = CursosEvento.codigo and CursosEvento.codigo >= "
				+ valorConsulta.intValue() + " ORDER BY CursosEvento.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoCursoEvento</code> através do valor do atributo
	 * <code>nrInscricao</code> da classe <code>InscricaoEvento</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>InscricaoCursoEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNrInscricaoInscricaoEvento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT InscricaoCursoEvento.* FROM InscricaoCursoEvento, InscricaoEvento WHERE InscricaoCursoEvento.inscricaoEvento = InscricaoEvento.nrInscricao and InscricaoEvento.nrInscricao >= "
				+ valorConsulta.intValue() + " ORDER BY InscricaoEvento.nrInscricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoCursoEvento</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoCursoEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM InscricaoCursoEvento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>InscricaoCursoEventoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>InscricaoCursoEventoVO</code>.
	 * 
	 * @return O objeto da classe <code>InscricaoCursoEventoVO</code> com os dados devidamente montados.
	 */
	public static InscricaoCursoEventoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		InscricaoCursoEventoVO obj = new InscricaoCursoEventoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setInscricaoEvento(new Integer(dadosSQL.getInt("inscricaoEvento")));
		obj.getCursosEvento().setCodigo(new Integer(dadosSQL.getInt("cursosEvento")));
		obj.setValorInscricao(new Double(dadosSQL.getDouble("valorInscricao")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosCursosEvento(obj, nivelMontarDados,usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>CursosEventoVO</code> relacionado ao objeto
	 * <code>InscricaoCursoEventoVO</code>. Faz uso da chave primária da classe <code>CursosEventoVO</code> para
	 * realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCursosEvento(InscricaoCursoEventoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		if (obj.getCursosEvento().getCodigo().intValue() == 0) {
			obj.setCursosEvento(new CursosEventoVO());
			return;
		}
		obj.setCursosEvento(getFacadeFactory().getCursosEventoFacade().consultarPorChavePrimaria(obj.getCursosEvento().getCodigo(), nivelMontarDados,usuario));
	}

	/**
	 * Operação responsável por excluir todos os objetos da <code>InscricaoCursoEventoVO</code> no BD. Faz uso da
	 * operação <code>excluir</code> disponível na classe <code>InscricaoCursoEvento</code>.
	 * 
	 * @param <code>inscricaoEvento</code> campo chave para exclusão dos objetos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirInscricaoCursoEventos(Integer inscricaoEvento) throws Exception {
		try {
			InscricaoCursoEvento.excluir(getIdEntidade());
			String sql = "DELETE FROM InscricaoCursoEvento WHERE (inscricaoEvento = ?)";
			getConexao().getJdbcTemplate().update(sql, new Object[] { inscricaoEvento.intValue() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar todos os objetos da <code>InscricaoCursoEventoVO</code> contidos em um Hashtable
	 * no BD. Faz uso da operação <code>excluirInscricaoCursoEventos</code> e <code>incluirInscricaoCursoEventos</code>
	 * disponíveis na classe <code>InscricaoCursoEvento</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarInscricaoCursoEventos(Integer inscricaoEvento, List objetos) throws Exception {
		excluirInscricaoCursoEventos(inscricaoEvento);
		incluirInscricaoCursoEventos(inscricaoEvento, objetos);
	}

	/**
	 * Operação responsável por incluir objetos da <code>InscricaoCursoEventoVO</code> no BD. Garantindo o
	 * relacionamento com a entidade principal <code>eventos.InscricaoEvento</code> através do atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirInscricaoCursoEventos(Integer inscricaoEventoPrm, List objetos) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			InscricaoCursoEventoVO obj = (InscricaoCursoEventoVO) e.next();
			obj.setInscricaoEvento(inscricaoEventoPrm);
			incluir(obj);
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>InscricaoCursoEventoVO</code> relacionados a um objeto da
	 * classe <code>eventos.InscricaoEvento</code>.
	 * 
	 * @param inscricaoEvento
	 *            Atributo de <code>eventos.InscricaoEvento</code> a ser utilizado para localizar os objetos da classe
	 *            <code>InscricaoCursoEventoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>InscricaoCursoEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public static List consultarInscricaoCursoEventos(Integer inscricaoEvento, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		InscricaoCursoEvento.consultar(getIdEntidade());
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM InscricaoCursoEvento WHERE inscricaoEvento = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { inscricaoEvento });
		while (tabelaResultado.next()) {
			InscricaoCursoEventoVO novoObj = new InscricaoCursoEventoVO();
			novoObj = InscricaoCursoEvento.montarDados(tabelaResultado, nivelMontarDados,usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>InscricaoCursoEventoVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public InscricaoCursoEventoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT * FROM InscricaoCursoEvento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados.");
		}
		return (montarDados(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return InscricaoCursoEvento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		InscricaoCursoEvento.idEntidade = idEntidade;
	}
}
