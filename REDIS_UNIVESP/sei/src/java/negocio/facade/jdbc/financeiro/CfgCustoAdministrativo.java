package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.financeiro.CfgCustoAdministrativoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.CfgCustoAdministrativoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>CfgCustoAdministrativoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>CfgCustoAdministrativoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see CfgCustoAdministrativoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class CfgCustoAdministrativo extends ControleAcesso implements CfgCustoAdministrativoInterfaceFacade {

	protected static String idEntidade;

	public CfgCustoAdministrativo() throws Exception {
		super();
		setIdEntidade("CfgCustoAdministrativo");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>CfgCustoAdministrativoVO</code>.
	 */
	public CfgCustoAdministrativoVO novo() throws Exception {
		CfgCustoAdministrativo.incluir(getIdEntidade());
		CfgCustoAdministrativoVO obj = new CfgCustoAdministrativoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>CfgCustoAdministrativoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CfgCustoAdministrativoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CfgCustoAdministrativoVO obj, UsuarioVO usuario) throws Exception {
		try {
			CfgCustoAdministrativoVO.validarDados(obj);
			final String sql = "INSERT INTO CfgCustoAdministrativo( data, responsavel, curso, tipoCusto, valorHoraAulaNaoGraduado, valorHoraAulaNaoGraduadoComImpostos, valorHoraAulaGraduado, valorHoraAulaGraduadoComImpostos, valorHoraAulaEspecialista, valorHoraAulaEspecialistaComImpostos, valorHoraAulaMestre, valorHoraAulaMestreComImpostos, valorHoraAulaDoutor, valorHoraAulaDoutorComImpostos, valorHoraAulaConvidado, valorHoraAulaConvidadoComImpostos, custoAdministrativoAluno, configuracoes ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getData()));
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(2, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (obj.getCurso().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getCurso().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setString(4, obj.getTipoCusto());
					sqlInserir.setDouble(5, obj.getValorHoraAulaNaoGraduado().doubleValue());
					sqlInserir.setDouble(6, obj.getValorHoraAulaNaoGraduadoComImpostos().doubleValue());
					sqlInserir.setDouble(7, obj.getValorHoraAulaGraduado().doubleValue());
					sqlInserir.setDouble(8, obj.getValorHoraAulaGraduadoComImpostos().doubleValue());
					sqlInserir.setDouble(9, obj.getValorHoraAulaEspecialista().doubleValue());
					sqlInserir.setDouble(10, obj.getValorHoraAulaEspecialistaComImpostos().doubleValue());
					sqlInserir.setDouble(11, obj.getValorHoraAulaMestre().doubleValue());
					sqlInserir.setDouble(12, obj.getValorHoraAulaMestreComImpostos().doubleValue());
					sqlInserir.setDouble(13, obj.getValorHoraAulaDoutor().doubleValue());
					sqlInserir.setDouble(14, obj.getValorHoraAulaDoutorComImpostos().doubleValue());
					sqlInserir.setDouble(15, obj.getValorHoraAulaConvidado().doubleValue());
					sqlInserir.setDouble(16, obj.getValorHoraAulaConvidadoComImpostos().doubleValue());
					sqlInserir.setDouble(17, obj.getCustoAdministrativoAluno().doubleValue());
					if (obj.getConfiguracoesVO().getCodigo().intValue() != 0) {
						sqlInserir.setInt(18, obj.getConfiguracoesVO().getCodigo());
					} else {
						sqlInserir.setNull(18, 0);
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
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>CfgCustoAdministrativoVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CfgCustoAdministrativoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CfgCustoAdministrativoVO obj, UsuarioVO usuario) throws Exception {
		try {
			CfgCustoAdministrativoVO.validarDados(obj);
			final String sql = "UPDATE CfgCustoAdministrativo set data=?, responsavel=?, curso=?, tipoCusto=?, valorHoraAulaNaoGraduado=?, valorHoraAulaNaoGraduadoComImpostos=?, valorHoraAulaGraduado=?, valorHoraAulaGraduadoComImpostos=?, valorHoraAulaEspecialista=?, valorHoraAulaEspecialistaComImpostos=?, valorHoraAulaMestre=?, valorHoraAulaMestreComImpostos=?, valorHoraAulaDoutor=?, valorHoraAulaDoutorComImpostos=?, valorHoraAulaConvidado=?, valorHoraAulaConvidadoComImpostos=?, custoAdministrativoAluno=?, configuracoes=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(2, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(2, 0);
					}
					if (obj.getCurso().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getCurso().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setString(4, obj.getTipoCusto());
					sqlAlterar.setDouble(5, obj.getValorHoraAulaNaoGraduado().doubleValue());
					sqlAlterar.setDouble(6, obj.getValorHoraAulaNaoGraduadoComImpostos().doubleValue());
					sqlAlterar.setDouble(7, obj.getValorHoraAulaGraduado().doubleValue());
					sqlAlterar.setDouble(8, obj.getValorHoraAulaGraduadoComImpostos().doubleValue());
					sqlAlterar.setDouble(9, obj.getValorHoraAulaEspecialista().doubleValue());
					sqlAlterar.setDouble(10, obj.getValorHoraAulaEspecialistaComImpostos().doubleValue());
					sqlAlterar.setDouble(11, obj.getValorHoraAulaMestre().doubleValue());
					sqlAlterar.setDouble(12, obj.getValorHoraAulaMestreComImpostos().doubleValue());
					sqlAlterar.setDouble(13, obj.getValorHoraAulaDoutor().doubleValue());
					sqlAlterar.setDouble(14, obj.getValorHoraAulaDoutorComImpostos().doubleValue());
					sqlAlterar.setDouble(15, obj.getValorHoraAulaConvidado().doubleValue());
					sqlAlterar.setDouble(16, obj.getValorHoraAulaConvidadoComImpostos().doubleValue());
					sqlAlterar.setDouble(17, obj.getCustoAdministrativoAluno().doubleValue());
					if (obj.getConfiguracoesVO().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(18, obj.getConfiguracoesVO().getCodigo());
					} else {
						sqlAlterar.setNull(18, 0);
					}
					sqlAlterar.setInt(19, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>CfgCustoAdministrativoVO</code>. Sempre localiza
	 * o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CfgCustoAdministrativoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CfgCustoAdministrativoVO obj, UsuarioVO usuario) throws Exception {
		try {
			String sql = "DELETE FROM CfgCustoAdministrativo WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>CfgCustoAdministrativo</code> através do valor do atributo
	 * <code>String tipoCusto</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CfgCustoAdministrativoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoCusto(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CfgCustoAdministrativo WHERE upper( tipoCusto ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY tipoCusto";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>CfgCustoAdministrativo</code> através do valor do atributo
	 * <code>nome</code> da classe <code>Curso</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>CfgCustoAdministrativoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeCurso(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT CfgCustoAdministrativo.* FROM CfgCustoAdministrativo, Curso WHERE CfgCustoAdministrativo.curso = Curso.codigo and upper( Curso.nome ) like('"
				+ valorConsulta.toUpperCase() + "%') ORDER BY Curso.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>CfgCustoAdministrativo</code> através do valor do atributo
	 * <code>codigo</code> da classe <code>Funcionario</code> Faz uso da operação <code>montarDadosConsulta</code> que
	 * realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>CfgCustoAdministrativoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoFuncionario(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT CfgCustoAdministrativo.* FROM CfgCustoAdministrativo, Funcionario WHERE CfgCustoAdministrativo.responsavel = Funcionario.codigo and Funcionario.codigo >= "
				+ valorConsulta.intValue() + " ORDER BY Funcionario.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>CfgCustoAdministrativo</code> através do valor do atributo
	 * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CfgCustoAdministrativoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CfgCustoAdministrativo WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>CfgCustoAdministrativo</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>CfgCustoAdministrativoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CfgCustoAdministrativo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public CfgCustoAdministrativoVO consultarPorCodigoConfiguracoes(Integer codigoConfiguracoes, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM cfgCustoAdministrativo WHERE configuracoes = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { codigoConfiguracoes });
		if (resultado.next()) {
			return montarDados(resultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		return new CfgCustoAdministrativoVO();
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>CfgCustoAdministrativoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
	 * objeto da classe <code>CfgCustoAdministrativoVO</code>.
	 * 
	 * @return O objeto da classe <code>CfgCustoAdministrativoVO</code> com os dados devidamente montados.
	 */
	public static CfgCustoAdministrativoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CfgCustoAdministrativoVO obj = new CfgCustoAdministrativoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
		obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
		obj.setTipoCusto(dadosSQL.getString("tipoCusto"));
		obj.setValorHoraAulaNaoGraduado(new Double(dadosSQL.getDouble("valorHoraAulaNaoGraduado")));
		obj.setValorHoraAulaNaoGraduadoComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaNaoGraduadoComImpostos")));
		obj.setValorHoraAulaGraduado(new Double(dadosSQL.getDouble("valorHoraAulaGraduado")));
		obj.setValorHoraAulaGraduadoComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaGraduadoComImpostos")));
		obj.setValorHoraAulaEspecialista(new Double(dadosSQL.getDouble("valorHoraAulaEspecialista")));
		obj.setValorHoraAulaEspecialistaComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaEspecialistaComImpostos")));
		obj.setValorHoraAulaMestre(new Double(dadosSQL.getDouble("valorHoraAulaMestre")));
		obj.setValorHoraAulaMestreComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaMestreComImpostos")));
		obj.setValorHoraAulaDoutor(new Double(dadosSQL.getDouble("valorHoraAulaDoutor")));
		obj.setValorHoraAulaDoutorComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaDoutorComImpostos")));
		obj.setValorHoraAulaConvidado(new Double(dadosSQL.getDouble("valorHoraAulaConvidado")));
		obj.setValorHoraAulaConvidadoComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaConvidadoComImpostos")));
		obj.setCustoAdministrativoAluno(new Double(dadosSQL.getDouble("custoAdministrativoAluno")));
		obj.setNovoObj(Boolean.FALSE);
		obj.getConfiguracoesVO().setCodigo(dadosSQL.getInt("configuracoes"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosConfiguracoes(obj, nivelMontarDados, usuario);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCurso(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		return obj;
	}

	public static void montarDadosConfiguracoes(CfgCustoAdministrativoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getConfiguracoesVO().getCodigo().intValue() == 0) {
			obj.setConfiguracoesVO(new ConfiguracoesVO());
			return;
		}
		obj.setConfiguracoesVO(getFacadeFactory().getConfiguracoesFacade().consultarPorChavePrimaria(obj.getConfiguracoesVO().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>CursoVO</code> relacionado ao objeto
	 * <code>CfgCustoAdministrativoVO</code>. Faz uso da chave primária da classe <code>CursoVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCurso(CfgCustoAdministrativoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCurso().getCodigo().intValue() == 0) {
			obj.setCurso(new CursoVO());
			return;
		}
		obj.setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(), nivelMontarDados, false, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>FuncionarioVO</code> relacionado ao objeto
	 * <code>CfgCustoAdministrativoVO</code>. Faz uso da chave primária da classe <code>FuncionarioVO</code> para
	 * realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavel(CfgCustoAdministrativoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>CfgCustoAdministrativoVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public CfgCustoAdministrativoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM CfgCustoAdministrativo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( CfgCustoAdministrativo ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
	 * permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return CfgCustoAdministrativo.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		CfgCustoAdministrativo.idEntidade = idEntidade;
	}
	
	
	
	
	
////////////////////////////////////////////////////////////////////////////////////////////

    public CfgCustoAdministrativoVO consultaRapidaConfiguracaoASerUsada(int configuracao , NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		CfgCustoAdministrativoVO obj = new CfgCustoAdministrativoVO();
		if (configuracao!= 0){
    		obj.getConfiguracoesVO().setCodigo(configuracao);
	    	carregarDados(obj, nivelMontarDados, usuario);
		}
		return (obj);
    }    
    
    public void carregarDados(CfgCustoAdministrativoVO obj, NivelMontarDados basico, UsuarioVO usuario) throws Exception {
        SqlRowSet resultado = null;
        if (obj.getIsDadosBasicosDevemSerCarregados(basico)) {
            resultado = consultaRapidaPorChavePrimariaDadosBasicos(obj.getConfiguracoesVO().getCodigo(), usuario);
            if(resultado.next()) {
            	montarDadosBasico((CfgCustoAdministrativoVO) obj, resultado);
            }
        }
        if (obj.getIsDadosCompletosDevemSerCarregados(basico)) {
            resultado = consultaRapidaPorChavePrimariaDadosCompletos(obj.getConfiguracoesVO().getCodigo(), usuario);
            if(resultado.next()) {
            	montarDadosCompleto((CfgCustoAdministrativoVO) obj, resultado);
            }
        }
    }

    private SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer configuracao, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaBasica();
        sqlStr.append(" WHERE (cfgcustoadministrativo.configuracoes= '" + configuracao + "')");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        
        return tabelaResultado;
    }
    
    private SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer configuracao, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuffer sqlStr = getSQLPadraoConsultaCompleta();
        sqlStr.append(" WHERE (cfgcustoadministrativo.configuracoes= '" + configuracao + "')");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        
        return tabelaResultado;
    }    

    private StringBuffer getSQLPadraoConsultaCompleta() {
        StringBuffer str = new StringBuffer();
        str.append("select cfgcustoadministrativo.*,");
        str.append("Curso.nome as \"Curso.nome\", Curso.codigo as \"Curso.codigo\",  ");
        str.append("Usuario.nome as \"Usuario.nome\", Usuario.codigo as \"Usuario.codigo\",  ");
        str.append("Configuracoes.codigo as \"Configuracoes.codigo\", Configuracoes.nome as \"Configuracoes.nome\",  ");
        str.append("Configuracoes.padrao as \"Configuracoes.padrao\", Configuracoes.mensagemplanodeestudo as \"Configuracoes.mensagemplanodeestudo\",  ");
        str.append("Configuracoes.mensagemboletimacademico as \"Configuracoes.mensagemboletimacademico\"   ");
        str.append(" FROM cfgcustoadministrativo left join curso as Curso on cfgcustoadministrativo.curso = Curso.codigo");
        str.append(" left join usuario as Usuario on cfgcustoadministrativo.responsavel = Usuario.codigo");
        str.append(" left join configuracoes as Configuracoes on cfgcustoadministrativo.configuracoes = Configuracoes.codigo");          
        return str;
    } 
    
    
    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer str = new StringBuffer();
        str.append("select cfgcustoadministrativo.*,");
        str.append("Curso.codigo as \"Curso.codigo\",  ");
        str.append("Usuario.codigo as \"Usuario.codigo\",  ");
        str.append("Configuracoes.codigo as \"Configuracoes.codigo\"  ");
        str.append(" FROM cfgcustoadministrativo left join curso as Curso on cfgcustoadministrativo.curso = Curso.codigo");
        str.append(" left join usuario as Usuario on cfgcustoadministrativo.responsavel = Usuario.codigo");        
        str.append(" left join configuracoes as Configuracoes on cfgcustoadministrativo.configuracoes = Configuracoes.codigo");        
        return str;
    }
    
    private void montarDadosCompleto(CfgCustoAdministrativoVO obj, SqlRowSet dadosSQL) throws Exception {
    	
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
		obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
		obj.setTipoCusto(dadosSQL.getString("tipoCusto"));
		obj.setValorHoraAulaNaoGraduado(new Double(dadosSQL.getDouble("valorHoraAulaNaoGraduado")));
		obj.setValorHoraAulaNaoGraduadoComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaNaoGraduadoComImpostos")));
		obj.setValorHoraAulaGraduado(new Double(dadosSQL.getDouble("valorHoraAulaGraduado")));
		obj.setValorHoraAulaGraduadoComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaGraduadoComImpostos")));
		obj.setValorHoraAulaEspecialista(new Double(dadosSQL.getDouble("valorHoraAulaEspecialista")));
		obj.setValorHoraAulaEspecialistaComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaEspecialistaComImpostos")));
		obj.setValorHoraAulaMestre(new Double(dadosSQL.getDouble("valorHoraAulaMestre")));
		obj.setValorHoraAulaMestreComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaMestreComImpostos")));
		obj.setValorHoraAulaDoutor(new Double(dadosSQL.getDouble("valorHoraAulaDoutor")));
		obj.setValorHoraAulaDoutorComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaDoutorComImpostos")));
		obj.setValorHoraAulaConvidado(new Double(dadosSQL.getDouble("valorHoraAulaConvidado")));
		obj.setValorHoraAulaConvidadoComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaConvidadoComImpostos")));
		obj.setCustoAdministrativoAluno(new Double(dadosSQL.getDouble("custoAdministrativoAluno")));
        obj.setNivelMontarDados(NivelMontarDados.TODOS);
        
        obj.getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
        obj.getCurso().setNome(dadosSQL.getString("Curso.nome"));
        obj.getCurso().setNivelMontarDados(NivelMontarDados.TODOS);
        
        obj.getResponsavel().setCodigo(dadosSQL.getInt("Usuario.codigo"));
        obj.getResponsavel().setNome(dadosSQL.getString("Usuario.nome"));
        obj.getResponsavel().setNivelMontarDados(NivelMontarDados.TODOS);
        
		obj.getConfiguracoesVO().setCodigo(dadosSQL.getInt("Configuracoes.codigo"));
		obj.getConfiguracoesVO().setNome(dadosSQL.getString("Configuracoes.nome"));
		obj.getConfiguracoesVO().setPadrao(dadosSQL.getBoolean("Configuracoes.padrao"));
		obj.getConfiguracoesVO().setMensagemBoletimAcademico("Configuracoes.mensagemboletimacademico");
		obj.getConfiguracoesVO().setMensagemPlanoDeEstudo(dadosSQL.getString("Configuracoes.mensagemplanodeestudo"));
        obj.getConfiguracoesVO().setNivelMontarDados(NivelMontarDados.TODOS);
		
		
		obj.setNovoObj(Boolean.FALSE);
    }
    
    private void montarDadosBasico(CfgCustoAdministrativoVO obj, SqlRowSet dadosSQL) throws Exception {

    	obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setData(dadosSQL.getDate("data"));
		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
		obj.getCurso().setCodigo(new Integer(dadosSQL.getInt("curso")));
		obj.setTipoCusto(dadosSQL.getString("tipoCusto"));
		obj.setValorHoraAulaNaoGraduado(new Double(dadosSQL.getDouble("valorHoraAulaNaoGraduado")));
		obj.setValorHoraAulaNaoGraduadoComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaNaoGraduadoComImpostos")));
		obj.setValorHoraAulaGraduado(new Double(dadosSQL.getDouble("valorHoraAulaGraduado")));
		obj.setValorHoraAulaGraduadoComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaGraduadoComImpostos")));
		obj.setValorHoraAulaEspecialista(new Double(dadosSQL.getDouble("valorHoraAulaEspecialista")));
		obj.setValorHoraAulaEspecialistaComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaEspecialistaComImpostos")));
		obj.setValorHoraAulaMestre(new Double(dadosSQL.getDouble("valorHoraAulaMestre")));
		obj.setValorHoraAulaMestreComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaMestreComImpostos")));
		obj.setValorHoraAulaDoutor(new Double(dadosSQL.getDouble("valorHoraAulaDoutor")));
		obj.setValorHoraAulaDoutorComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaDoutorComImpostos")));
		obj.setValorHoraAulaConvidado(new Double(dadosSQL.getDouble("valorHoraAulaConvidado")));
		obj.setValorHoraAulaConvidadoComImpostos(new Double(dadosSQL.getDouble("valorHoraAulaConvidadoComImpostos")));
		obj.setCustoAdministrativoAluno(new Double(dadosSQL.getDouble("custoAdministrativoAluno")));
        obj.setNivelMontarDados(NivelMontarDados.BASICO);

        obj.getCurso().setCodigo(dadosSQL.getInt("Curso.codigo"));
        obj.getCurso().setNivelMontarDados(NivelMontarDados.BASICO);
        
        obj.getResponsavel().setCodigo(dadosSQL.getInt("Usuario.codigo"));
        obj.getResponsavel().setNivelMontarDados(NivelMontarDados.BASICO);
        
		obj.getConfiguracoesVO().setCodigo(dadosSQL.getInt("Configuracoes.codigo"));
        obj.getConfiguracoesVO().setNivelMontarDados(NivelMontarDados.BASICO);
        
        obj.setNovoObj(Boolean.FALSE);
		
    }
	
	
}
