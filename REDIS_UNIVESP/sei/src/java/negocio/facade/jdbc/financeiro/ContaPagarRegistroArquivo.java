package negocio.facade.jdbc.financeiro;

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
import negocio.comuns.financeiro.ContaPagarRegistroArquivoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.RegistroDetalhePagarVO;
import negocio.comuns.financeiro.enumerador.MensagensErroRetornoRemessaPagarEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaPagarRegistroArquivoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ContaPagarRegistroArquivo extends ControleAcesso implements ContaPagarRegistroArquivoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7269245071428407993L;
	protected static String idEntidade;

	public ContaPagarRegistroArquivo() throws Exception {
		super();
		setIdEntidade("ContaPagarRegistroArquivo");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<ContaPagarRegistroArquivoVO> lista, UsuarioVO usuario) throws Exception {
		for (ContaPagarRegistroArquivoVO objeto : lista) {
			if (objeto.getCodigo().equals(0)) {
				incluir(objeto, usuario);
			} else {
				alterar(objeto, usuario);
			}
		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ContaPagarRegistroArquivoVO</code>. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContaPagarRegistroArquivoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final ContaPagarRegistroArquivoVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "INSERT INTO ContaPagarRegistroArquivo(contapagar, controleCobrancaPagar, registroDetalhePagar, motivoRejeicao , valorPagamentoDivergente) VALUES(?, ?, ?, ? ,? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getContaPagarVO(), ++i, sqlInserir);
					sqlInserir.setInt(++i, obj.getControleCobrancaPagarVO().getCodigo().intValue());
					Uteis.setValuePreparedStatement(obj.getRegistroDetalhePagarVO(), ++i, sqlInserir);
					sqlInserir.setString(++i, obj.getMotivoRejeicao());
					sqlInserir.setBoolean(++i, obj.getValorPagamentoDivergente());					
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
	 * Operação responsável por alterar no BD os dados de um objeto da classe <code>ContaPagarRegistroArquivoVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContaPagarRegistroArquivoVO</code> que será alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final ContaPagarRegistroArquivoVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE ContaPagarRegistroArquivo set contapagar=?, controleCobrancaPagar=?, registroDetalhePagar=?, motivoRejeicao=?  , valorPagamentoDivergente=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getContaPagarVO(), ++i, sqlAlterar);
					sqlAlterar.setInt(++i, obj.getControleCobrancaPagarVO().getCodigo().intValue());
					Uteis.setValuePreparedStatement(obj.getRegistroDetalhePagarVO(), ++i, sqlAlterar);
					sqlAlterar.setString(++i, obj.getMotivoRejeicao());
					sqlAlterar.setBoolean(++i, obj.getValorPagamentoDivergente().booleanValue());
					sqlAlterar.setInt(++i, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe <code>ContaPagarRegistroArquivoVO</code>. Sempre localiza o registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContaPagarRegistroArquivoVO</code> que será removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ContaPagarRegistroArquivoVO obj, UsuarioVO usuario) throws Exception {
		try {
			String sql = "DELETE FROM ContaPagarRegistroArquivo WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Responsável por realizar uma consulta de <code>ContaPagarRegistroArquivo</code> através do valor do atributo <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe <code>ContaPagarRegistroArquivoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContaPagarRegistroArquivo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public List<ContaPagarRegistroArquivoVO> consultarPorControleCobrancaPagar(Integer controleCobrancaPagar, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM ContaPagarRegistroArquivo WHERE ControleCobrancaPagar = " + controleCobrancaPagar.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ContaPagarRegistroArquivoVO</code> resultantes da consulta.
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
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>ContaPagarRegistroArquivoVO</code>.
	 * 
	 * @return O objeto da classe <code>ContaPagarRegistroArquivoVO</code> com os dados devidamente montados.
	 */
	public static ContaPagarRegistroArquivoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ContaPagarRegistroArquivoVO obj = new ContaPagarRegistroArquivoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getControleCobrancaPagarVO().setCodigo(new Integer(dadosSQL.getInt("controlecobrancapagar")));
		obj.getContaPagarVO().setCodigo(new Integer(dadosSQL.getInt("contapagar")));
		obj.getRegistroDetalhePagarVO().setCodigo(new Integer(dadosSQL.getInt("registrodetalhepagar")));
		obj.setMotivoRejeicao(dadosSQL.getString("motivoRejeicao"));
		obj.setValorPagamentoDivergente(dadosSQL.getBoolean("valorPagamentoDivergente"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosContaPagar(obj, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario);
		montarDadosRegistroDetalhePagar(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if(obj.getMotivoRejeicao() != null && !obj.getMotivoRejeicao().isEmpty()){
			obj.setListaMotivoRejeicao(MensagensErroRetornoRemessaPagarEnum.getMensagem(obj.getMotivoRejeicao(), obj.getRegistroDetalhePagarVO().getCodigoBanco().toString()));
		}
		if(Uteis.isAtributoPreenchido(obj.getContaPagarVO())){
			obj.getContaPagarVO().setObservacao("Conta Localizada!");	
			if(!obj.isContaPagarEfetivado()){
				obj.getContaPagarVO().setObservacao(obj.getMotivoRejeicao_Apresentar());
			}else if(obj.getRegistroDetalhePagarVO().getValorPagamento().equals(0.0) || obj.getValorPagamentoDivergente() ){
				obj.getRegistroDetalhePagarVO().setValorPagamento(0.0);
				obj.getContaPagarVO().setObservacao("Conta com problemas no valor pago!");
			}else if (obj.getContaPagarVO().getQuitada()) {
				obj.getContaPagarVO().setObservacao("Conta já Paga!");
			}
		} else if(!obj.isContaPagarEfetivado()){
			obj.getContaPagarVO().setObservacao(obj.getMotivoRejeicao_Apresentar());
		}else{
			obj.getContaPagarVO().setObservacao("Conta Não Localizada!");
		}
		return obj;
	}
	
	public static void montarDadosContaPagar(ContaPagarRegistroArquivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaPagarVO().getCodigo().intValue() == 0) {
			obj.setContaPagarVO(new ContaPagarVO());
			return;
		}
		obj.setContaPagarVO(getFacadeFactory().getContaPagarFacade().consultarPorChavePrimaria(obj.getContaPagarVO().getCodigo(), false, nivelMontarDados, usuario));
	}
	
	public static void montarDadosRegistroDetalhePagar(ContaPagarRegistroArquivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getRegistroDetalhePagarVO().getCodigo().intValue() == 0) {
			obj.setRegistroDetalhePagarVO(new RegistroDetalhePagarVO());
			return;
		}
		obj.setRegistroDetalhePagarVO(getFacadeFactory().getRegistroDetalhePagarFacade().consultarPorChavePrimaria(obj.getRegistroDetalhePagarVO().getCodigo(), nivelMontarDados, usuario));
	}


	/**
	 * Operação responsável por localizar um objeto da classe <code>ContaPagarRegistroArquivoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	@Override
	public ContaPagarRegistroArquivoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM ContaPagarRegistroArquivo WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( ContaPagarRegistroArquivo ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return ContaPagarRegistroArquivo.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ContaPagarRegistroArquivo.idEntidade = idEntidade;
	}

}
