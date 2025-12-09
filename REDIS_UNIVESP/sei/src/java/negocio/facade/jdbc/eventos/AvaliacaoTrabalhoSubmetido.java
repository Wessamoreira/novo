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
import negocio.comuns.eventos.AvaliacaoTrabalhoSubmetidoVO;
import negocio.comuns.eventos.MembroComissaoCientificaVO;
import negocio.comuns.eventos.TrabalhoSubmetidoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.eventos.AvaliacaoTrabalhoSubmetidoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>AvaliacaoTrabalhoSubmetidoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e
 * consultar pertinentes a classe <code>AvaliacaoTrabalhoSubmetidoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see AvaliacaoTrabalhoSubmetidoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy 
public class AvaliacaoTrabalhoSubmetido extends ControleAcesso implements AvaliacaoTrabalhoSubmetidoInterfaceFacade {

	protected static String idEntidade;

	public AvaliacaoTrabalhoSubmetido() throws Exception {
		super();
		setIdEntidade("AvaliacaoTrabalhoSubmetido");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>AvaliacaoTrabalhoSubmetidoVO</code>.
	 */
	public AvaliacaoTrabalhoSubmetidoVO novo() throws Exception {
		AvaliacaoTrabalhoSubmetido.incluir(getIdEntidade());
		AvaliacaoTrabalhoSubmetidoVO obj = new AvaliacaoTrabalhoSubmetidoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>AvaliacaoTrabalhoSubmetidoVO</code>.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>AvaliacaoTrabalhoSubmetidoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final AvaliacaoTrabalhoSubmetidoVO obj) throws Exception {
		try {
			AvaliacaoTrabalhoSubmetido.incluir(getIdEntidade());
			AvaliacaoTrabalhoSubmetidoVO.validarDados(obj);
			final String sql = "INSERT INTO AvaliacaoTrabalhoSubmetido( parecerComissao, justificativa, membroComissaoCientifica, trabalhoSubmetido ) VALUES ( ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getParecerComissao());
					sqlInserir.setString(2, obj.getJustificativa());
					sqlInserir.setInt(3, obj.getMembroComissaoCientifica().getCodigo().intValue());
					sqlInserir.setInt(4, obj.getTrabalhoSubmetido().getCodigo().intValue());
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
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>AvaliacaoTrabalhoSubmetidoVO</code>.
	 * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
	 * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>AvaliacaoTrabalhoSubmetidoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AvaliacaoTrabalhoSubmetidoVO obj) throws Exception {
		try {
			AvaliacaoTrabalhoSubmetido.alterar(getIdEntidade());
			AvaliacaoTrabalhoSubmetidoVO.validarDados(obj);
			final String sql = "UPDATE AvaliacaoTrabalhoSubmetido set parecerComissao=?, justificativa=?, membroComissaoCientifica=?, trabalhoSubmetido=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getParecerComissao());
					sqlAlterar.setString(2, obj.getJustificativa());
					sqlAlterar.setInt(3, obj.getMembroComissaoCientifica().getCodigo().intValue());
					sqlAlterar.setInt(4, obj.getTrabalhoSubmetido().getCodigo().intValue());
					sqlAlterar.setInt(5, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>AvaliacaoTrabalhoSubmetidoVO</code>. Sempre
	 * localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
	 * <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>AvaliacaoTrabalhoSubmetidoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(AvaliacaoTrabalhoSubmetidoVO obj) throws Exception {
		try {
			AvaliacaoTrabalhoSubmetido.excluir(getIdEntidade());
			String sql = "DELETE FROM AvaliacaoTrabalhoSubmetido WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>AvaliacaoTrabalhoSubmetido</code> através do valor do atributo
	 * <code>evento</code> da classe <code>TrabalhoSubmetido</code> Faz uso da operação <code>montarDadosConsulta</code>
	 * que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>AvaliacaoTrabalhoSubmetidoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorEventoTrabalhoSubmetido(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT AvaliacaoTrabalhoSubmetido.* FROM AvaliacaoTrabalhoSubmetido, TrabalhoSubmetido WHERE AvaliacaoTrabalhoSubmetido.trabalhoSubmetido = TrabalhoSubmetido.codigo and TrabalhoSubmetido.evento >= "
				+ valorConsulta.intValue() + " ORDER BY TrabalhoSubmetido.evento";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>AvaliacaoTrabalhoSubmetido</code> através do valor do atributo
	 * <code>codigo</code> da classe <code>MembroComissaoCientifica</code> Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>AvaliacaoTrabalhoSubmetidoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoMembroComissaoCientifica(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT AvaliacaoTrabalhoSubmetido.* FROM AvaliacaoTrabalhoSubmetido, MembroComissaoCientifica WHERE AvaliacaoTrabalhoSubmetido.membroComissaoCientifica = MembroComissaoCientifica.codigo and MembroComissaoCientifica.codigo >= "
				+ valorConsulta.intValue() + " ORDER BY MembroComissaoCientifica.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>AvaliacaoTrabalhoSubmetido</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>AvaliacaoTrabalhoSubmetidoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM AvaliacaoTrabalhoSubmetido WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
	 * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
	 * vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>AvaliacaoTrabalhoSubmetidoVO</code> resultantes da consulta.
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
	 * objeto da classe <code>AvaliacaoTrabalhoSubmetidoVO</code>.
	 * 
	 * @return O objeto da classe <code>AvaliacaoTrabalhoSubmetidoVO</code> com os dados devidamente montados.
	 */
	public static AvaliacaoTrabalhoSubmetidoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		AvaliacaoTrabalhoSubmetidoVO obj = new AvaliacaoTrabalhoSubmetidoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setParecerComissao(dadosSQL.getString("parecerComissao"));
		obj.setJustificativa(dadosSQL.getString("justificativa"));
		obj.getMembroComissaoCientifica().setCodigo(new Integer(dadosSQL.getInt("membroComissaoCientifica")));
		obj.getTrabalhoSubmetido().setCodigo(new Integer(dadosSQL.getInt("trabalhoSubmetido")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosMembroComissaoCientifica(obj, nivelMontarDados,usuario);
		montarDadosTrabalhoSubmetido(obj, nivelMontarDados,usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>TrabalhoSubmetidoVO</code> relacionado ao
	 * objeto <code>AvaliacaoTrabalhoSubmetidoVO</code>. Faz uso da chave primária da classe
	 * <code>TrabalhoSubmetidoVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosTrabalhoSubmetido(AvaliacaoTrabalhoSubmetidoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		if (obj.getTrabalhoSubmetido().getCodigo().intValue() == 0) {
			obj.setTrabalhoSubmetido(new TrabalhoSubmetidoVO());
			return;
		}
		obj.setTrabalhoSubmetido(getFacadeFactory().getTrabalhoSubmetidoFacade().consultarPorChavePrimaria(obj.getTrabalhoSubmetido().getCodigo(),usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>MembroComissaoCientificaVO</code>
	 * relacionado ao objeto <code>AvaliacaoTrabalhoSubmetidoVO</code>. Faz uso da chave primária da classe
	 * <code>MembroComissaoCientificaVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosMembroComissaoCientifica(AvaliacaoTrabalhoSubmetidoVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		if (obj.getMembroComissaoCientifica().getCodigo().intValue() == 0) {
			obj.setMembroComissaoCientifica(new MembroComissaoCientificaVO());
			return;
		}
		obj.setMembroComissaoCientifica(getFacadeFactory().getMembroComissaoCientificaFacade().consultarPorChavePrimaria(obj.getMembroComissaoCientifica().getCodigo(), nivelMontarDados,usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>AvaliacaoTrabalhoSubmetidoVO</code> através de sua
	 * chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public AvaliacaoTrabalhoSubmetidoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados,UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false,usuario);
		String sql = "SELECT * FROM AvaliacaoTrabalhoSubmetido WHERE codigo = ?";
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
		return AvaliacaoTrabalhoSubmetido.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
	 * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
	 * Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		AvaliacaoTrabalhoSubmetido.idEntidade = idEntidade;
	}
}
