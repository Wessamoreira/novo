package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import negocio.comuns.financeiro.ContaPagarHistoricoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaPagarHistoricoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ContaPagarHistorico extends ControleAcesso implements ContaPagarHistoricoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 753582151863210689L;
	protected static String idEntidade;

	public ContaPagarHistorico() throws Exception {
		super();
		setIdEntidade("ContaPagarHistorico");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void criarContaPagarHistoricoPorBaixaAutomaticas(ContaPagarVO obj, ControleCobrancaPagarVO controleCobrancaPagar, UsuarioVO usuario) throws Exception {
		ContaPagarHistoricoVO crh = new ContaPagarHistoricoVO();
		crh.setContaPagar(obj.getCodigo());
		crh.setData(new Date());
		crh.setMotivo("Conta Pagar já processada por baixa convênio. Tentativa em duplicidade!");
		crh.setArquivo(controleCobrancaPagar.getArquivoRetornoContaPagar().getCodigo());
		crh.setNossoNumero(obj.getNossoNumero());
		crh.setResponsavel(usuario);
		crh.setValorPagamento(obj.getValorPago());
		crh.setControleCobrancaPagar(controleCobrancaPagar.getCodigo());
		incluir(crh, usuario);
		controleCobrancaPagar.getListaContaPagarHistorico().add(crh);
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluir(final ContaPagarHistoricoVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "INSERT INTO ContaPagarHistorico( contaPagar, valorPagamento, data, motivo, responsavel, nossoNumero, arquivo, controleCobrancaPagar) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getContaPagar().intValue());
					sqlInserir.setDouble(2, obj.getValorPagamento().doubleValue());
					sqlInserir.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlInserir.setString(4, obj.getMotivo());
					sqlInserir.setInt(5, obj.getResponsavel().getCodigo().intValue());
					sqlInserir.setLong(6, obj.getNossoNumero());
					sqlInserir.setInt(7, obj.getArquivo());
					sqlInserir.setInt(8, obj.getControleCobrancaPagar());
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
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ContaPagarHistoricoVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContaPagarHistoricoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void alterar(final ContaPagarHistoricoVO obj, UsuarioVO usuario) throws Exception {
		try {
			// obj.realizarUpperCaseDados();
			final String sql = "UPDATE ContaPagarHistorico set ContaPagar=?, valorPagamento=?, data=?, motivo=?, responsavel=?, nossoNumero=?, arquivo=?, controleCobrancaPagar=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
					PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getContaPagar().intValue());
					sqlAlterar.setDouble(2, obj.getValorPagamento().doubleValue());
					sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(obj.getData()));
					sqlAlterar.setString(4, obj.getMotivo());
					sqlAlterar.setInt(5, obj.getResponsavel().getCodigo().intValue());
					sqlAlterar.setLong(6, obj.getNossoNumero());
					sqlAlterar.setInt(7, obj.getArquivo());
					sqlAlterar.setInt(8, obj.getControleCobrancaPagar());
					sqlAlterar.setInt(9, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ContaPagarHistoricoVO obj, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM ContaPagarHistorico WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	
	public List consultarPorCodigoContaPagar(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM ContaPagarHistorico WHERE ContaPagar = " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	@Override
	public List consultarPorCodigoControleCobrancaPagar(Integer controleCobrancaPagar, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM ContaPagarHistorico WHERE controleCobrancaPagar = " + controleCobrancaPagar.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContaPagarHistorico WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	
	public static List<ContaPagarHistoricoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<ContaPagarHistoricoVO> vetResultado = new ArrayList<ContaPagarHistoricoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	
	public static ContaPagarHistoricoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ContaPagarHistoricoVO obj = new ContaPagarHistoricoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setContaPagar(new Integer(dadosSQL.getInt("ContaPagar")));
		obj.setControleCobrancaPagar(new Integer(dadosSQL.getInt("controleCobrancaPagar")));
		obj.setValorPagamento(new Double(dadosSQL.getDouble("valorPagamento")));
		obj.setData(dadosSQL.getTimestamp("data"));
		obj.setMotivo(dadosSQL.getString("motivo"));
		obj.setNossoNumero(dadosSQL.getLong("nossoNumero"));
		obj.getResponsavel().setCodigo(new Integer(dadosSQL.getInt("responsavel")));
		obj.setArquivo(new Integer(dadosSQL.getInt("arquivo")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	public static void montarDadosResponsavel(ContaPagarHistoricoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel() == null || obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirContaPagarHistoricos(Integer contaPagar, UsuarioVO usuario) throws Exception {
		
		String sql = "DELETE FROM ContaPagarHistorico WHERE (ContaPagar = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { contaPagar });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirContaPagarHistoricos(Integer contaPagar, List objeto, UsuarioVO usuario) throws Exception {
		// CtRPtRLog.excluir(getIdEntidade());
		String sql = "DELETE FROM ContaPagarHistorico WHERE (ContaPagar = ?) ";
		Iterator i = objeto.iterator();
		while (i.hasNext()) {
			ContaPagarHistoricoVO obj = (ContaPagarHistoricoVO) i.next();
			if (obj.getCodigo().intValue() != 0) {
				sql += " and codigo != " + obj.getCodigo();
			}
		}
		getConexao().getJdbcTemplate().update(sql + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), new Object[] { contaPagar });
	}

	/**
	 * Operação responsável por alterar todos os objetos da <code>ContaPagarHistoricoVO</code> contidos em um Hashtable no BD. Faz uso da operação <code>excluirContaPagarHistoricos</code> e <code>incluirContaPagarHistoricos</code> disponíveis na classe <code>ContaPagarHistorico</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void alterarContaPagarHistoricos(Integer ContaPagar, List objetos, UsuarioVO usuario) throws Exception {
		// excluirContaPagarHistoricos( ContaPagar );
		incluirContaPagarHistoricos(ContaPagar, objetos, usuario);
	}

	/**
	 * Operação responsável por incluir objetos da <code>ContaPagarHistoricoVO</code> no BD. Garantindo o relacionamento com a entidade principal <code>financeiro.ContaPagar</code> através do atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirContaPagarHistoricos(Integer ContaPagarPrm, List objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			ContaPagarHistoricoVO obj = (ContaPagarHistoricoVO) e.next();
			obj.setContaPagar(ContaPagarPrm);
			if (obj.getCodigo().intValue() == 0) {
				incluir(obj, usuario);
			}
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>ContaPagarHistoricoVO</code> relacionados a um objeto da classe <code>financeiro.ContaPagar</code>.
	 * 
	 * @param ContaPagar
	 *            Atributo de <code>financeiro.ContaPagar</code> a ser utilizado para localizar os objetos da classe <code>ContaPagarHistoricoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>ContaPagarHistoricoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public static List consultarContaPagarHistoricos(Integer contaPagar, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// ContaPagarHistorico.consultar(getIdEntidade());
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM ContaPagarHistorico WHERE ContaPagar = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { contaPagar });
		while (resultado.next()) {
			ContaPagarHistoricoVO novoObj = new ContaPagarHistoricoVO();
			novoObj = ContaPagarHistorico.montarDados(resultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ContaPagarHistoricoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ContaPagarHistoricoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ContaPagarHistorico WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ContaPagarHistorico ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return ContaPagarHistorico.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ContaPagarHistorico.idEntidade = idEntidade;
	}

}
