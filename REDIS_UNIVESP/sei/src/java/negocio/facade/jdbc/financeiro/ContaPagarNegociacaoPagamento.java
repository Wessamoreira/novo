package negocio.facade.jdbc.financeiro;

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
import negocio.comuns.financeiro.ContaPagarNegociacaoPagamentoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaPagarNegociacaoPagamentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ContaPagarNegociacaoPagamentoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ContaPagarNegociacaoPagamentoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ContaPagarNegociacaoPagamentoVO
 * @see ControleAcesso
 * @see NegociacaoPagamento
 */
@Repository
@Scope("singleton")
@Lazy
public class ContaPagarNegociacaoPagamento extends ControleAcesso implements ContaPagarNegociacaoPagamentoInterfaceFacade {

	protected static String idEntidade;
	private String nomeResponsavel;
	private String nomeAluno;
	private String matricula;
	private String turma;
	private Integer codigoNegociacaoPagamento;
	private String valorTotalPagamentoPorExtenso;
	private Double valorTotalPagamento;

	public ContaPagarNegociacaoPagamento() throws Exception {
		super();
		setIdEntidade("Pagamento");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe <code>ContaPagarNegociacaoPagamentoVO</code>.
	 */
	public ContaPagarNegociacaoPagamentoVO novo() throws Exception {
		// ContaPagarNegociacaoPagamento.incluir(getIdEntidade());
		ContaPagarNegociacaoPagamentoVO obj = new ContaPagarNegociacaoPagamentoVO();
		return obj;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ContaPagarNegociacaoPagamentoVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContaPagarNegociacaoPagamentoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ContaPagarNegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception {
		ContaPagarNegociacaoPagamentoVO.validarDados(obj);
		// ContaPagarNegociacaoPagamento.incluir(getIdEntidade());
		obj.realizarUpperCaseDados();
		final String sql = "INSERT INTO ContaPagarNegociacaoPagamento( contaPagar, negociacaoContaPagar, valorContaPagar ) VALUES ( ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				if (obj.getContaPagar().getCodigo().intValue() != 0) {
					sqlInserir.setInt(1, obj.getContaPagar().getCodigo().intValue());
				} else {
					sqlInserir.setNull(1, 0);
				}
				if (obj.getNegociacaoContaPagar().intValue() != 0) {
					sqlInserir.setInt(2, obj.getNegociacaoContaPagar().intValue());
				} else {
					sqlInserir.setNull(2, 0);
				}
				sqlInserir.setDouble(3, obj.getValorContaPagar().doubleValue());
				return sqlInserir;
			}
		}, new ResultSetExtractor<Object>() {

			public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		obj.setNovoObj(Boolean.FALSE);
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ContaPagarNegociacaoPagamentoVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContaPagarNegociacaoPagamentoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ContaPagarNegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception {
		ContaPagarNegociacaoPagamentoVO.validarDados(obj);
		// ContaPagarNegociacaoPagamento.alterar(getIdEntidade());
		obj.realizarUpperCaseDados();
		final String sql = "UPDATE ContaPagarNegociacaoPagamento set contaPagar=?, negociacaoContaPagar=?, valorContaPagar=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if (obj.getContaPagar().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(1, obj.getContaPagar().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				if (obj.getNegociacaoContaPagar().intValue() != 0) {
					sqlAlterar.setInt(2, obj.getNegociacaoContaPagar().intValue());
				} else {
					sqlAlterar.setNull(2, 0);
				}
				sqlAlterar.setDouble(3, obj.getValorContaPagar().doubleValue());
				sqlAlterar.setInt(4, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ContaPagarNegociacaoPagamentoVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContaPagarNegociacaoPagamentoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ContaPagarNegociacaoPagamentoVO obj, UsuarioVO usuario) throws Exception {
		// ContaPagarNegociacaoPagamento.excluir(getIdEntidade());
		String sql = "DELETE FROM ContaPagarNegociacaoPagamento WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContaPagarNegociacaoPagamento</code> através do valor do atributo <code>codigo</code> da classe <code>NegociacaoPagamento</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ContaPagarNegociacaoPagamentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoNegociacaoPagamento(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// ControleAcesso.consultar(getIdEntidade(), true);
		String sqlStr = "SELECT ContaPagarNegociacaoPagamento.* FROM ContaPagarNegociacaoPagamento, NegociacaoPagamento WHERE ContaPagarNegociacaoPagamento.negociacaoContaPagar = NegociacaoPagamento.codigo and NegociacaoPagamento.codigo >= " + valorConsulta.intValue() + " ORDER BY NegociacaoPagamento.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContaPagarNegociacaoPagamento</code> através do valor do atributo <code>codigo</code> da classe <code>ContaPagar</code> Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe <code>ContaPagarNegociacaoPagamentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigoContaPagar(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// ControleAcesso.consultar(getIdEntidade(), true);
		String sqlStr = "SELECT ContaPagarNegociacaoPagamento.* FROM ContaPagarNegociacaoPagamento, ContaPagar WHERE ContaPagarNegociacaoPagamento.contaPagar = ContaPagar.codigo and ContaPagar.codigo >= " + valorConsulta.intValue() + " ORDER BY ContaPagar.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContaPagarNegociacaoPagamento</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ContaPagarNegociacaoPagamentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContaPagarNegociacaoPagamento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ContaPagarNegociacaoPagamentoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>ContaPagarNegociacaoPagamentoVO</code>.
	 * 
	 * @return O objeto da classe <code>ContaPagarNegociacaoPagamentoVO</code> com os dados devidamente montados.
	 */
	public static ContaPagarNegociacaoPagamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ContaPagarNegociacaoPagamentoVO obj = new ContaPagarNegociacaoPagamentoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getContaPagar().setCodigo(new Integer(dadosSQL.getInt("contaPagar")));
		obj.setNegociacaoContaPagar(new Integer(dadosSQL.getInt("negociacaoContaPagar")));
		obj.setValorContaPagar(new Double(dadosSQL.getDouble("valorContaPagar")));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}

		montarDadosContaPagar(obj, nivelMontarDados, usuario);
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>ContaPagarVO</code> relacionado ao objeto <code>ContaPagarNegociacaoPagamentoVO</code>. Faz uso da chave primária da classe <code>ContaPagarVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosContaPagar(ContaPagarNegociacaoPagamentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaPagar().getCodigo().intValue() == 0) {
			obj.setContaPagar(new ContaPagarVO());
			return;
		}
		obj.setContaPagar(getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(obj.getContaPagar().getCodigo(), false, nivelMontarDados, usuario));
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirContaPagarNegociacaoPagamentos(NegociacaoPagamentoVO negociacaoContaPagar, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM ContaPagarNegociacaoPagamento WHERE (negociacaoContaPagar = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { negociacaoContaPagar.getCodigo() });
	}

	/**
	 * Operação responsável por alterar todos os objetos da <code>ContaPagarNegociacaoPagamentoVO</code> contidos em um Hashtable no BD. Faz uso da operação <code>excluirContaPagarNegociacaoPagamentos</code> e <code>incluirContaPagarNegociacaoPagamentos</code> disponíveis na classe <code>ContaPagarNegociacaoPagamento</code>.
	 * 
	 * @param contaPagarNegociacaoPagamentoVOs
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public void alterarContaPagarNegociacaoPagamentos(Integer negociacaoContaPagar, List objetos, Boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		// String str =
		// "DELETE FROM ContaPagarNegociacaoPagamento WHERE negociacaoContaPagar = "
		// + negociacaoContaPagar;
		// Iterator i = objetos.iterator();
		// while (i.hasNext()) {
		// ContaPagarNegociacaoPagamentoVO objeto =
		// (ContaPagarNegociacaoPagamentoVO) i.next();
		// str += " AND codigo = " + objeto.getCodigo().intValue();
		// }
		// PreparedStatement sqlExcluir = con.prepareStatement(str);
		// sqlExcluir.execute();
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			ContaPagarNegociacaoPagamentoVO objeto = (ContaPagarNegociacaoPagamentoVO) e.next();
			if (objeto.getCodigo().equals(0)) {
				objeto.setNegociacaoContaPagar(negociacaoContaPagar);
				if (!objeto.getContaPagar().getSituacao().equals("AP")) {
					incluir(objeto, usuario);
					objeto.getContaPagar().liberarVerificacaoBloqueioFechamentoMes();
					getFacadeFactory().getContaPagarFacade().alterar(objeto.getContaPagar(), verificarAcesso, true, usuario);
				}
			} else {
				alterar(objeto, usuario);
			}
		}
	}

	public void incluirContaPagarNegociacaoPagamentos(Integer negociacaoContaPagarPrm, String tipoSacado, List<ContaPagarNegociacaoPagamentoVO> contaPagarNegociacaoPagamentoVOs, Boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		for (ContaPagarNegociacaoPagamentoVO obj : contaPagarNegociacaoPagamentoVOs) {
			obj.setNegociacaoContaPagar(negociacaoContaPagarPrm);
			if (!obj.getContaPagar().getSituacao().equals("AP")) {
				incluir(obj, usuario);
				obj.getContaPagar().setTipoSacado(tipoSacado);			
				obj.getContaPagar().liberarVerificacaoBloqueioFechamentoMes();
				getFacadeFactory().getContaPagarFacade().alterar(obj.getContaPagar(), verificarAcesso, true, usuario);
			}
		}
	}

	/**
	 * Operação responsável por consultar todos os <code>ContaPagarNegociacaoPagamentoVO</code> relacionados a um objeto da classe <code>financeiro.NegociacaoPagamento</code>.
	 * 
	 * @param negociacaoContaPagar
	 *            Atributo de <code>financeiro.NegociacaoPagamento</code> a ser utilizado para localizar os objetos da classe <code>ContaPagarNegociacaoPagamentoVO</code>.
	 * @return List Contendo todos os objetos da classe <code>ContaPagarNegociacaoPagamentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	public static List consultarContaPagarNegociacaoPagamentos(Integer negociacaoContaPagar, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		// ContaPagarNegociacaoPagamento.consultar(getIdEntidade());
		List objetos = new ArrayList(0);
		String sql = "SELECT * FROM ContaPagarNegociacaoPagamento WHERE negociacaoContaPagar = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { negociacaoContaPagar });
		while (resultado.next()) {
			ContaPagarNegociacaoPagamentoVO novoObj = new ContaPagarNegociacaoPagamentoVO();
			novoObj = ContaPagarNegociacaoPagamento.montarDados(resultado, nivelMontarDados, usuario);
			objetos.add(novoObj);
		}
		return objetos;
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>ContaPagarNegociacaoPagamentoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	public ContaPagarNegociacaoPagamentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ContaPagarNegociacaoPagamento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ContaPagarNegociacaoPagamento ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return ContaPagarNegociacaoPagamento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ContaPagarNegociacaoPagamento.idEntidade = idEntidade;
	}

	public String getNomeResponsavel() {
		return nomeResponsavel;
	}

	public String getNomeAluno() {
		return nomeAluno;
	}

	public String getMatricula() {
		return matricula;
	}

	public String getTurma() {
		return turma;
	}

	public Integer getCodigoNegociacaoPagamento() {
		return codigoNegociacaoPagamento;
	}

	public String getValorTotalPagamentoPorExtenso() {
		return valorTotalPagamentoPorExtenso;
	}

	public Double getValorTotalPagamento() {
		return valorTotalPagamento;
	}

	public void setNomeResponsavel(String nomeResponsavel) {
		this.nomeResponsavel = nomeResponsavel;
	}

	public void setNomeAluno(String nomeAluno) {
		this.nomeAluno = nomeAluno;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public void setTurma(String turma) {
		this.turma = turma;
	}

	public void setCodigoNegociacaoPagamento(Integer codigoNegociacaoPagamento) {
		this.codigoNegociacaoPagamento = codigoNegociacaoPagamento;
	}

	public void setValorTotalPagamentoPorExtenso(String valorTotalPagamentoPorExtenso) {
		this.valorTotalPagamentoPorExtenso = valorTotalPagamentoPorExtenso;
	}

	public void setValorTotalPagamento(Double valorTotalPagamento) {
		this.valorTotalPagamento = valorTotalPagamento;
	}
}
