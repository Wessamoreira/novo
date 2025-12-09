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

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.enumerador.OperadoraCartaoCreditoEnum;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.financeiro.enumerador.TipoFormaArredondamentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoConsultaComboOperadoraCartaoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.OperadoraCartaoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>OperadoraCartaoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>OperadoraCartaoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see OperadoraCartaoVO
 * @see SuperEntidade
 */
@Repository
@Scope("singleton")
@Lazy
public class OperadoraCartao extends ControleAcesso implements OperadoraCartaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public OperadoraCartao() throws Exception {
		super();
		setIdEntidade("OperadoraCartao");
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>OperadoraCartaoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>OperadoraCartaoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final OperadoraCartaoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			validarDados(obj);
			OperadoraCartao.incluir(getIdEntidade(), true, usuario);
			final String sql = "INSERT INTO OperadoraCartao( nome, tipo, operadoraCartaoCredito, formaPagamentoPadraoRecebimentoOnline, tipoFinanciamento, regraAplicarDiferencaValorReceberPrimeiraParcela, tipoFormaArredondamentoEnum ) VALUES ( ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setString(2, obj.getTipo());
					sqlInserir.setString(3, obj.getOperadoraCartaoCreditoEnum().getName());
					if (Uteis.isAtributoPreenchido(obj.getFormaPagamentoPadraoRecebimentoOnline())) {
						sqlInserir.setInt(4, obj.getFormaPagamentoPadraoRecebimentoOnline().getCodigo());
					} else {
						sqlInserir.setNull(4, 0);
					}
                    if(Uteis.isAtributoPreenchido(obj.getTipoFinanciamentoEnum())) {
                    	sqlInserir.setString(5, obj.getTipoFinanciamentoEnum().getName());
                    } else {
						sqlInserir.setNull(5, 0);
					}
                    sqlInserir.setBoolean(6, obj.isRegraAplicarDiferencaValorReceberPrimeiraParcela());
                    sqlInserir.setString(7, obj.getTipoFormaArredondamentoEnum().name());
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
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>OperadoraCartaoVO</code>. Sempre utiliza a chave primária da classe
	 * como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>OperadoraCartaoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final OperadoraCartaoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			validarDados(obj);
			OperadoraCartao.alterar(getIdEntidade(), true, usuario);
			final String sql = "UPDATE OperadoraCartao set nome=?, tipo=?, operadoraCartaoCredito=?, formaPagamentoPadraoRecebimentoOnline=?, tipoFinanciamento=?, regraAplicarDiferencaValorReceberPrimeiraParcela=?, tipoFormaArredondamentoEnum=?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setString(2, obj.getTipo());
					sqlAlterar.setString(3, obj.getOperadoraCartaoCreditoEnum().getName());
					if (Uteis.isAtributoPreenchido(obj.getFormaPagamentoPadraoRecebimentoOnline())) {
						sqlAlterar.setInt(4, obj.getFormaPagamentoPadraoRecebimentoOnline().getCodigo());
					} else {
						sqlAlterar.setNull(4, 0);
					}
                    if(Uteis.isAtributoPreenchido(obj.getTipoFinanciamentoEnum())) {
                    	sqlAlterar.setString(5, obj.getTipoFinanciamentoEnum().getName());
                    } else {
						sqlAlterar.setNull(5, 0);
					}
                    sqlAlterar.setBoolean(6, obj.isRegraAplicarDiferencaValorReceberPrimeiraParcela());
					sqlAlterar.setString(7, obj.getTipoFormaArredondamentoEnum().name());
					sqlAlterar.setInt(8, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>OperadoraCartaoVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>OperadoraCartaoVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(OperadoraCartaoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
		try {
			OperadoraCartao.excluir(getIdEntidade(), true, usuario);
			String sql = "DELETE FROM OperadoraCartao WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Método responsavel por verificar se ira incluir ou alterar o objeto.
	 * 
	 * @param OperadoraCartaoVO
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(OperadoraCartaoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuarioVO) throws Exception {
		if (obj.isNovoObj().booleanValue()) {
			incluir(obj, usuario, configuracaoGeralSistema);
		} else {
			alterar(obj, usuario, configuracaoGeralSistema);
		}
		getAplicacaoControle().removerOperadoraCartao(obj.getCodigo());
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>OperadoraCartaoVO</code>. Todos os tipos de consistência de dados
	 * são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public void validarDados(OperadoraCartaoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		ConsistirException consistir = new ConsistirException();
		if (obj.getNome().equals("")) {
			consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_OperadoraCartao_nome"));
		}
		if (obj.getTipo().equals("")) {
			consistir.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_OperadoraCartao_tipo"));
		}
		if (consistir.existeErroListaMensagemErro()) {
			throw consistir;
		}

	}
	
	/**
	 * Rotina responsavel por executar as consultas disponiveis na Tela
	 * OperadoraCartaoCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public List<OperadoraCartaoVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		if (campoConsulta.equals(TipoConsultaComboOperadoraCartaoEnum.CODIGO.toString())) {
			if (valorConsulta.trim().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("Digite ao menos 2 caracteres para consultar."));
			}
			if (valorConsulta.equals("")) {
				valorConsulta = "0";
			}
			Integer valorInteger = Integer.parseInt(valorConsulta);
			return consultarPorCodigo(valorInteger, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		if (campoConsulta.equals(TipoConsultaComboOperadoraCartaoEnum.NOME.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("Digite ao menos 2 caracteres para consultar."));
			}
			return consultarPorNome(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		if (campoConsulta.equals(TipoConsultaComboOperadoraCartaoEnum.TIPO.toString())) {
			if (valorConsulta.trim().length() < qtdCaracteresValidacao) {
				throw new Exception(UteisJSF.internacionalizar("Digite ao menos 2 caracteres para consultar."));
			}
			return consultarPorTipo(valorConsulta, controlarAcesso, Uteis.NIVELMONTARDADOS_TODOS, usuario);
		}
		return new ArrayList(0);
	}

	/**
	 * Responsável por realizar uma consulta de <code>OperadoraCartao</code>
	 * através do valor do atributo <code>String nome</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>OperadoraCartaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<OperadoraCartaoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		String sqlStr = "SELECT * FROM OperadoraCartao WHERE upper( nome ) like(?) ORDER BY nome";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase()), nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>OperadoraCartao</code>
	 * através do valor do atributo <code>String tipo</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>OperadoraCartaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<OperadoraCartaoVO> consultarPorTipo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		valorConsulta += "%";
		String sqlStr = "SELECT * FROM OperadoraCartao WHERE upper( tipo ) like(?) ORDER BY nome";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.toUpperCase()), nivelMontarDados, usuario));
	}

	/**
	 * Responsável por realizar uma consulta de <code>OperadoraCartao</code>
	 * através do valor do atributo <code>Long codigo</code>. Retorna os objetos
	 * com valores iguais ou superiores ao parâmetro fornecido. Faz uso da
	 * operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>OperadoraCartaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<OperadoraCartaoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM OperadoraCartao WHERE codigo >= ?  ORDER BY codigo";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>OperadoraCartaoVO</code> resultantes da consulta.
	 */
	public List<OperadoraCartaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<OperadoraCartaoVO> vetResultado = new ArrayList<OperadoraCartaoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>OperadoraCartaoVO</code>.
	 * 
	 * @return O objeto da classe <code>OperadoraCartaoVO</code> com os dados
	 *         devidamente montados.
	 */
	public OperadoraCartaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		OperadoraCartaoVO obj = new OperadoraCartaoVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setTipo(dadosSQL.getString("tipo"));
		
		if (obj.getTipo().equals("CARTAO_CREDITO")) {
			if(Uteis.isAtributoPreenchido(dadosSQL.getString("operadoraCartaoCredito"))) {
				obj.setOperadoraCartaoCreditoEnum(OperadoraCartaoCreditoEnum.valueOf(dadosSQL.getString("operadoraCartaoCredito")));				
			}
		}
		obj.getFormaPagamentoPadraoRecebimentoOnline().setCodigo(dadosSQL.getInt("formaPagamentoPadraoRecebimentoOnline"));
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipofinanciamento"))) {
        	obj.setTipoFinanciamentoEnum(TipoFinanciamentoEnum.valueOf(dadosSQL.getString("tipofinanciamento")));        	
        }
		obj.setRegraAplicarDiferencaValorReceberPrimeiraParcela(dadosSQL.getBoolean("regraAplicarDiferencaValorReceberPrimeiraParcela"));
		obj.setTipoFormaArredondamentoEnum(TipoFormaArredondamentoEnum.valueOf(dadosSQL.getString("tipoFormaArredondamentoEnum")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			montarDadosFormaPagamento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			return obj;
		}
		montarDadosFormaPagamento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		return obj;
	}
	
	public void montarDadosFormaPagamento(OperadoraCartaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFormaPagamentoPadraoRecebimentoOnline().getCodigo().intValue() == 0) {
			return;
		}
		obj.setFormaPagamentoPadraoRecebimentoOnline(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamentoPadraoRecebimentoOnline().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>OperadoraCartaoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public OperadoraCartaoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return getAplicacaoControle().getOperadoraCartaoVO(codigoPrm, usuario);
	}
	
	@Override
	public OperadoraCartaoVO consultarPorChavePrimariaUnica(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM OperadoraCartao WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( OperadoraCartao ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<OperadoraCartaoVO> consultarUnicidade(OperadoraCartaoVO obj, boolean alteracao, UsuarioVO usuario) throws Exception {
		super.verificarPermissaoConsultar(getIdEntidade(), false, usuario);
		return new ArrayList(0);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return OperadoraCartao.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		OperadoraCartao.idEntidade = idEntidade;
	}

	public OperadoraCartaoVO consultarPorFormaPagamentoNegociacaoRecebimento(Integer formaPagamentoNegociacaoRecebimento, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT OperadoraCartao.* FROM OperadoraCartao ");
		sb.append(" inner join formaPagamentoNegociacaoRecebimento on formaPagamentoNegociacaoRecebimento.operadoraCartao = operadoraCartao.codigo ");
		sb.append(" where formaPagamentoNegociacaoRecebimento.codigo = ").append(formaPagamentoNegociacaoRecebimento);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (!tabelaResultado.next()) {
			return new OperadoraCartaoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	
	@Override
	public OperadoraCartaoVO consultarPorCodigoConfiguracaoFinanceiroCartao(Integer codigoConfiguracaoFinanceiroCartao, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT OperadoraCartao.* FROM OperadoraCartao ");
		sb.append(" inner join configuracaofinanceirocartao on  configuracaofinanceirocartao.operadoracartao = operadoracartao.codigo ");
		sb.append(" where configuracaofinanceirocartao.codigo = ").append(codigoConfiguracaoFinanceiroCartao);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (!tabelaResultado.next()) {
			return new OperadoraCartaoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}


}