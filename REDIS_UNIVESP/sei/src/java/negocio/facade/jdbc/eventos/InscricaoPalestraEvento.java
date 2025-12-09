package negocio.facade.jdbc.eventos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import negocio.comuns.eventos.InscricaoEventoVO;
import negocio.comuns.eventos.InscricaoPalestraEventoVO;
import negocio.comuns.eventos.PalestraEventoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.eventos.InscricaoPalestraEventoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>InscricaoPalestraEventoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>InscricaoPalestraEventoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see InscricaoPalestraEventoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class InscricaoPalestraEvento extends ControleAcesso implements InscricaoPalestraEventoInterfaceFacade {

	protected static String idEntidade;

	public InscricaoPalestraEvento() throws Exception {
		super();
		setIdEntidade("InscricaoPalestraEvento");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>InscricaoPalestraEventoVO</code>.
	 */
	public InscricaoPalestraEventoVO novo() throws Exception {
		InscricaoPalestraEvento.incluir(getIdEntidade());
		InscricaoPalestraEventoVO obj = new InscricaoPalestraEventoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>InscricaoPalestraEventoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InscricaoPalestraEventoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final InscricaoPalestraEventoVO obj) throws Exception {
		try {
			InscricaoPalestraEvento.incluir(getIdEntidade());
			InscricaoPalestraEventoVO.validarDados(obj);
			final String sql = "INSERT INTO InscricaoPalestraEvento( inscricaoEvento, palestraEvento, valorInscricao ) VALUES ( ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getInscricaoEvento().getNrInscricao().intValue());
					sqlInserir.setInt(2, obj.getPalestraEvento().getCodigo().intValue());
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
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>InscricaoPalestraEventoVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InscricaoPalestraEventoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final InscricaoPalestraEventoVO obj) throws Exception {
		try {
			InscricaoPalestraEvento.alterar(getIdEntidade());
			InscricaoPalestraEventoVO.validarDados(obj);
			final String sql = "UPDATE InscricaoPalestraEvento set inscricaoEvento=?, palestraEvento=?, valorInscricao=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getInscricaoEvento().getNrInscricao().intValue());
					sqlAlterar.setInt(2, obj.getPalestraEvento().getCodigo().intValue());
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
	 * Operação responsável por excluir no BD um objeto da classe <code>InscricaoPalestraEventoVO</code>. Sempre
	 * localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InscricaoPalestraEventoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(InscricaoPalestraEventoVO obj) throws Exception {
		try {
			InscricaoPalestraEvento.excluir(getIdEntidade());
			String sql = "DELETE FROM InscricaoPalestraEvento WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoPalestraEvento</code> através do valor do atributo
	 * <code>Double valorInscricao</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoPalestraEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorInscricao(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM InscricaoPalestraEvento WHERE valorInscricao >= " + valorConsulta.doubleValue() + " ORDER BY valorInscricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoPalestraEvento</code> através do valor do atributo
	 * <code>codigo</code> da classe <code>PalestraEvento</code> Faz uso da operação <code>montarDadosConsulta</code>
	 * que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>InscricaoPalestraEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoPalestraEvento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT InscricaoPalestraEvento.* FROM InscricaoPalestraEvento, PalestraEvento WHERE InscricaoPalestraEvento.palestraEvento = PalestraEvento.codigo and PalestraEvento.codigo >= "
				+ valorConsulta.intValue() + " ORDER BY PalestraEvento.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoPalestraEvento</code> através do valor do atributo
	 * <code>nrInscricao</code> da classe <code>InscricaoEvento</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>InscricaoPalestraEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNrInscricaoInscricaoEvento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT InscricaoPalestraEvento.* FROM InscricaoPalestraEvento, InscricaoEvento WHERE InscricaoPalestraEvento.inscricaoEvento = InscricaoEvento.nrInscricao and InscricaoEvento.nrInscricao >= "
				+ valorConsulta.intValue() + " ORDER BY InscricaoEvento.nrInscricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoPalestraEvento</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoPalestraEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM InscricaoPalestraEvento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>InscricaoPalestraEventoVO</code> resultantes da consulta.
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
	 * objeto da classe <code>InscricaoPalestraEventoVO</code>.
	 * 
	 * @return O objeto da classe <code>InscricaoPalestraEventoVO</code> com os dados devidamente montados.
	 */
	public static InscricaoPalestraEventoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		InscricaoPalestraEventoVO obj = new InscricaoPalestraEventoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getInscricaoEvento().setNrInscricao(new Integer(dadosSQL.getInt("inscricaoEvento")));
		obj.getPalestraEvento().setCodigo(new Integer(dadosSQL.getInt("palestraEvento")));
		obj.setValorInscricao(new Double(dadosSQL.getDouble("valorInscricao")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosInscricaoEvento(obj, nivelMontarDados,usuario);
		montarDadosPalestraEvento(obj, nivelMontarDados,usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PalestraEventoVO</code> relacionado ao
	 * objeto <code>InscricaoPalestraEventoVO</code>. Faz uso da chave primária da classe <code>PalestraEventoVO</code>
	 * para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPalestraEvento(InscricaoPalestraEventoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		if (obj.getPalestraEvento().getCodigo().intValue() == 0) {
			obj.setPalestraEvento(new PalestraEventoVO());
			return;
		}
		obj.setPalestraEvento(getFacadeFactory().getPalestraEventoFacade().consultarPorChavePrimaria(obj.getPalestraEvento().getCodigo(), nivelMontarDados,usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>InscricaoEventoVO</code> relacionado ao
	 * objeto <code>InscricaoPalestraEventoVO</code>. Faz uso da chave primária da classe <code>InscricaoEventoVO</code>
	 * para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosInscricaoEvento(InscricaoPalestraEventoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		if (obj.getInscricaoEvento().getNrInscricao().intValue() == 0) {
			obj.setInscricaoEvento(new InscricaoEventoVO());
			return;
		}
		obj.setInscricaoEvento(getFacadeFactory().getInscricaoEventoFacade().consultarPorChavePrimaria(obj.getInscricaoEvento().getNrInscricao(), nivelMontarDados,usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>InscricaoPalestraEventoVO</code> através de sua
	 * chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public InscricaoPalestraEventoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT * FROM InscricaoPalestraEvento WHERE codigo = ?";
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
		return InscricaoPalestraEvento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		InscricaoPalestraEvento.idEntidade = idEntidade;
	}
}
