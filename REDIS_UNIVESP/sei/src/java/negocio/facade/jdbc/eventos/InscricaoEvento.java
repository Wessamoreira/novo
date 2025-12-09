package negocio.facade.jdbc.eventos;

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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.eventos.EventoVO;
import negocio.comuns.eventos.InscricaoEventoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.eventos.InscricaoEventoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>InscricaoEventoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>InscricaoEventoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see InscricaoEventoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class InscricaoEvento extends ControleAcesso implements InscricaoEventoInterfaceFacade {

	protected static String idEntidade;

	public InscricaoEvento() throws Exception {
		super();
		setIdEntidade("InscricaoEvento");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>InscricaoEventoVO</code>.
	 */
	public InscricaoEventoVO novo() throws Exception {
		InscricaoEvento.incluir(getIdEntidade());
		InscricaoEventoVO obj = new InscricaoEventoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>InscricaoEventoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InscricaoEventoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final InscricaoEventoVO obj) throws Exception {
		try {
			InscricaoEvento.incluir(getIdEntidade());
			InscricaoEventoVO.validarDados(obj);
			final String sql = "INSERT INTO InscricaoEvento( evento, data, hora, valorTotal, tipoInscricao, pessoaInscricao ) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setNrInscricao((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getEvento().getCodigo().intValue());
					sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getData()));
					sqlInserir.setString(3, obj.getHora());
					sqlInserir.setDouble(4, obj.getValorTotal().doubleValue());
					sqlInserir.setString(5, obj.getTipoInscricao());
					sqlInserir.setInt(6, obj.getPessoaInscricao().getCodigo().intValue());
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
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>InscricaoEventoVO</code>. Sempre
	 * utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InscricaoEventoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final InscricaoEventoVO obj) throws Exception {
		try {
			InscricaoEvento.alterar(getIdEntidade());
			InscricaoEventoVO.validarDados(obj);
			final String sql = "UPDATE InscricaoEvento set evento=?, data=?, hora=?, valorTotal=?, tipoInscricao=?, pessoaInscricao=? WHERE ((nrInscricao = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getEvento().getCodigo().intValue());
					sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getData()));
					sqlAlterar.setString(3, obj.getHora());
					sqlAlterar.setDouble(4, obj.getValorTotal().doubleValue());
					sqlAlterar.setString(5, obj.getTipoInscricao());
					sqlAlterar.setInt(6, obj.getPessoaInscricao().getCodigo().intValue());
					sqlAlterar.setInt(7, obj.getNrInscricao().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>InscricaoEventoVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>InscricaoEventoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(InscricaoEventoVO obj) throws Exception {
		try {
			InscricaoEvento.excluir(getIdEntidade());
			String sql = "DELETE FROM InscricaoEvento WHERE ((nrInscricao = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getNrInscricao() });
			getFacadeFactory().getInscricaoCursoEventoFacade().excluirInscricaoCursoEventos(obj.getNrInscricao());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoEvento</code> através do valor do atributo
	 * <code>String tipoInscricao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
	 * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorTipoInscricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM InscricaoEvento WHERE tipoInscricao like('" + valorConsulta + "%') ORDER BY tipoInscricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoEvento</code> através do valor do atributo
	 * <code>Double valorTotal</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorValorTotal(Double valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM InscricaoEvento WHERE valorTotal >= " + valorConsulta.doubleValue() + " ORDER BY valorTotal";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoEvento</code> através do valor do atributo
	 * <code>Date data</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM InscricaoEvento WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + "') and (data <= '" + Uteis.getDataJDBC(prmFim) + "')) ORDER BY data";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoEvento</code> através do valor do atributo
	 * <code>nome</code> da classe <code>Evento</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza
	 * o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>InscricaoEventoVO</code> resultantes da consulta.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNomeEvento(String valorConsulta, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true,usuario);
		String sqlStr = "SELECT InscricaoEvento.* FROM InscricaoEvento, Evento WHERE InscricaoEvento.evento = Evento.codigo and Evento.nome like('" + valorConsulta + "%') ORDER BY Evento.nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>InscricaoEvento</code> através do valor do atributo
	 * <code>Integer nrInscricao</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz
	 * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>InscricaoEventoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorNrInscricao(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM InscricaoEvento WHERE nrInscricao >= " + valorConsulta.intValue() + " ORDER BY nrInscricao";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>InscricaoEventoVO</code> resultantes da consulta.
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
	 * objeto da classe <code>InscricaoEventoVO</code>.
	 * 
	 * @return O objeto da classe <code>InscricaoEventoVO</code> com os dados devidamente montados.
	 */
	public static InscricaoEventoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		InscricaoEventoVO obj = new InscricaoEventoVO();
		obj.setNrInscricao(new Integer(dadosSQL.getInt("nrInscricao")));
		obj.getEvento().setCodigo(new Integer(dadosSQL.getInt("evento")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setHora(dadosSQL.getString("hora"));
		obj.setValorTotal(new Double(dadosSQL.getDouble("valorTotal")));
		obj.setTipoInscricao(dadosSQL.getString("tipoInscricao"));
		obj.getPessoaInscricao().setCodigo(new Integer(dadosSQL.getInt("pessoaInscricao")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setInscricaoCursoEventoVOs(InscricaoCursoEvento.consultarInscricaoCursoEventos(obj.getNrInscricao(), nivelMontarDados,usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosEvento(obj, nivelMontarDados, usuario);
		montarDadosPessoaInscricao(obj, nivelMontarDados,usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>PessoaVO</code> relacionado ao objeto
	 * <code>InscricaoEventoVO</code>. Faz uso da chave primária da classe <code>PessoaVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosPessoaInscricao(InscricaoEventoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		if (obj.getPessoaInscricao().getCodigo().intValue() == 0) {
			obj.setPessoaInscricao(new PessoaVO());
			return;
		}
		obj.setPessoaInscricao(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoaInscricao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>EventoVO</code> relacionado ao objeto
	 * <code>InscricaoEventoVO</code>. Faz uso da chave primária da classe <code>EventoVO</code> para realizar a
	 * consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosEvento(InscricaoEventoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getEvento().getCodigo().intValue() == 0) {
			obj.setEvento(new EventoVO());
			return;
		}
		obj.setEvento(getFacadeFactory().getEventoFacade().consultarPorChavePrimaria(obj.getEvento().getCodigo(), nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>InscricaoEventoVO</code> através de sua chave
	 * primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public InscricaoEventoVO consultarPorChavePrimaria(Integer nrInscricaoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT * FROM InscricaoEvento WHERE nrInscricao = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { nrInscricaoPrm });
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
		return InscricaoEvento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		InscricaoEvento.idEntidade = idEntidade;
	}
}
