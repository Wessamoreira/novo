package negocio.facade.jdbc.contabil;

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
import negocio.comuns.contabil.HistoricoContabilVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.contabil.TipoEventoContabilVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.TipoEventoContabilInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>TipoEventoContabilVO</code>
 * . Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>TipoEventoContabilVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see TipoEventoContabilVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class TipoEventoContabil extends ControleAcesso implements TipoEventoContabilInterfaceFacade {

	protected static String idEntidade;

	public TipoEventoContabil() throws Exception {
		super();
		setIdEntidade("TipoEventoContabil");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>TipoEventoContabilVO</code>.
	 */
	public TipoEventoContabilVO novo() throws Exception {
		incluir(getIdEntidade());
		TipoEventoContabilVO obj = new TipoEventoContabilVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>TipoEventoContabilVO</code>. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TipoEventoContabilVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final TipoEventoContabilVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			TipoEventoContabil.incluir(getIdEntidade(), true, usuarioVO);
			TipoEventoContabilVO.validarDados(obj);
			final String sql = "INSERT INTO TipoEventoContabil( descricao, contaDebito, contaCredito, historico ) VALUES ( ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getDescricao());
					sqlInserir.setInt(2, obj.getContaDebito().getCodigo().intValue());
					sqlInserir.setInt(3, obj.getContaCredito().getCodigo().intValue());
					sqlInserir.setInt(4, obj.getHistorico().getCodigo().intValue());
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
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>TipoEventoContabilVO</code>. Sempre utiliza
	 * a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os
	 * dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TipoEventoContabilVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TipoEventoContabilVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			TipoEventoContabil.alterar(getIdEntidade(), true, usuarioVO);
			TipoEventoContabilVO.validarDados(obj);
			final String sql = "UPDATE TipoEventoContabil set descricao=?, contaDebito=?, contaCredito=?, historico=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getDescricao());
					sqlAlterar.setInt(2, obj.getContaDebito().getCodigo().intValue());
					sqlAlterar.setInt(3, obj.getContaCredito().getCodigo().intValue());
					sqlAlterar.setInt(4, obj.getHistorico().getCodigo().intValue());
					sqlAlterar.setInt(5, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>TipoEventoContabilVO</code>. Sempre localiza o registro
	 * a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>TipoEventoContabilVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(TipoEventoContabilVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			TipoEventoContabil.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM TipoEventoContabil WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>TipoEventoContabil</code> através do valor do atributo
	 * <code>descricao</code> da classe <code>Historico</code> Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>TipoEventoContabilVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDescricaoHistorico(String valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true,usuario);
		String sqlStr = "SELECT TipoEventoContabil.* FROM TipoEventoContabil, Historico WHERE TipoEventoContabil.historico = Historico.codigo and Lower( Historico.descricao ) like('" + valorConsulta.toLowerCase()
				+ "%') ORDER BY TipoEventoContabil.descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TipoEventoContabil</code> através do valor do atributo
	 * <code>identificadorPlanoConta</code> da classe <code>PlanoConta</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>TipoEventoContabilVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoPlanoContaTodos(String valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true,usuario);
		String sqlStr = "SELECT * FROM TipoEventoContabil";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TipoEventoContabil</code> através do valor do atributo
	 * <code>identificadorPlanoConta</code> da classe <code>PlanoConta</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>TipoEventoContabilVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoPlanoContaCredito(String valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true,usuario);
		String sqlStr = "SELECT TipoEventoContabil.* FROM TipoEventoContabil, PlanoConta WHERE TipoEventoContabil.contaCredito = PlanoConta.codigo and Lower( PlanoConta.descricao ) like('" + valorConsulta.toLowerCase()
				+ "%') ORDER BY PlanoConta.descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TipoEventoContabil</code> através do valor do atributo
	 * <code>identificadorPlanoConta</code> da classe <code>PlanoConta</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>TipoEventoContabilVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoPlanoContaDebito(String valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true,usuario);
		String sqlStr = "SELECT TipoEventoContabil.* FROM TipoEventoContabil, PlanoConta WHERE TipoEventoContabil.contaDebito = PlanoConta.codigo and Lower( PlanoConta.descricao ) like('" + valorConsulta.toLowerCase()
				+ "%') ORDER BY PlanoConta.descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TipoEventoContabil</code> através do valor do atributo
	 * <code>identificadorPlanoConta</code> da classe <code>PlanoConta</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>TipoEventoContabilVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorIdentificadorPlanoConta(String valorConsulta, String tipoConta, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true,usuario);
		String sqlStr = "SELECT TipoEventoContabil.* FROM TipoEventoContabil, PlanoConta WHERE TipoEventoContabil.contaDebito = PlanoConta.codigo and " + "TipoEventoContabil.contaCredito = PlanoConta.codigo and "
				+ "PlanoConta.identificadorPlanoConta = '" + valorConsulta + "' ";
		if (!tipoConta.equals("")) {
			sqlStr += "AND PlanoConta.tipoplanoconta = '" + tipoConta + "' ";
		}

		sqlStr += "ORDER BY PlanoConta.identificadorPlanoConta";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TipoEventoContabil</code> através do valor do atributo
	 * <code>String descricao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>TipoEventoContabilVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true,usuario);
		String sqlStr = "SELECT * FROM TipoEventoContabil WHERE Lower( descricao ) like('" + valorConsulta.toLowerCase() + "%') ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>TipoEventoContabil</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>TipoEventoContabilVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true,usuario);
		String sqlStr = "SELECT * FROM TipoEventoContabil WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY descricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>TipoEventoContabilVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>TipoEventoContabilVO</code>.
	 * 
	 * @return O objeto da classe <code>TipoEventoContabilVO</code> com os dados devidamente montados.
	 */
	public static TipoEventoContabilVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		TipoEventoContabilVO obj = new TipoEventoContabilVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.getContaDebito().setCodigo(new Integer(dadosSQL.getInt("contaDebito")));
		obj.getContaCredito().setCodigo(new Integer(dadosSQL.getInt("contaCredito")));
		obj.getHistorico().setCodigo(new Integer(dadosSQL.getInt("historico")));
		obj.setNovoObj(Boolean.FALSE);

		montarDadosContaDebito(obj, nivelMontarDados,usuario);
		montarDadosContaCredito(obj, nivelMontarDados,usuario);
		montarDadosHistorico(obj,usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>HistoricoVO</code> relacionado ao objeto
	 * <code>TipoEventoContabilVO</code>. Faz uso da chave primária da classe <code>HistoricoVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosHistorico(TipoEventoContabilVO obj,UsuarioVO usuario) throws Exception {
		if (obj.getHistorico().getCodigo().intValue() == 0) {
			obj.setHistorico(new HistoricoContabilVO());
			return;
		}
		obj.setHistorico(getFacadeFactory().getHistoricoContabilFacade().consultarPorChavePrimaria(obj.getHistorico().getCodigo(),usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PlanoContaVO</code> relacionado ao objeto
	 * <code>TipoEventoContabilVO</code>. Faz uso da chave primária da classe <code>PlanoContaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosContaDebito(TipoEventoContabilVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		if (obj.getContaDebito().getCodigo().intValue() == 0) {
			obj.setContaDebito(new PlanoContaVO());
			return;
		}
		obj.setContaDebito(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(obj.getContaDebito().getCodigo(), nivelMontarDados,usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PlanoContaVO</code> relacionado ao objeto
	 * <code>TipoEventoContabilVO</code>. Faz uso da chave primária da classe <code>PlanoContaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosContaCredito(TipoEventoContabilVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		if (obj.getContaCredito().getCodigo().intValue() == 0) {
			obj.setContaCredito(new PlanoContaVO());
			return;
		}
		obj.setContaCredito(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(obj.getContaCredito().getCodigo(), nivelMontarDados,usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>TipoEventoContabilVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public TipoEventoContabilVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true,usuario);
		String sql = "SELECT * FROM TipoEventoContabil WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( TipoEventoContabil ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados,usuario));
	}

	public List consultarPorNome(String valorConsulta, String ordenacao, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM TipoEventoContabil ORDER BY ";
		if (ordenacao.equals("descricao")) {
			sqlStr += "descricao";
		} else {
			sqlStr += "codigo";
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return TipoEventoContabil.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		TipoEventoContabil.idEntidade = idEntidade;
	}
}
