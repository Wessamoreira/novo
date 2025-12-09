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

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CondicaoRenegociacaoUnidadeEnsinoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ItemCondicaoRenegociacaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.CondicaoRenegociacaoUnidadeEnsinoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>ItemCondicaoRenegociacaoVO</code>. Responsável por
 * implementar operações como incluir, alterar, excluir e consultar pertinentes
 * a classe <code>ItemCondicaoRenegociacaoVO</code>. Encapsula toda a interação
 * com o banco de dados.
 * 
 * @see ItemCondicaoRenegociacaoVO
 * @see SuperEntidade
 * @see CondicaoRenegociacao
 */

@Repository
@Scope("singleton")
@Lazy
public class CondicaoRenegociacaoUnidadeEnsino extends ControleAcesso implements CondicaoRenegociacaoUnidadeEnsinoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public CondicaoRenegociacaoUnidadeEnsino() throws Exception {
		super();
		setIdEntidade("CondicaoRenegociacaoUnidadeEnsino");
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ItemCondicaoRenegociacaoVO</code>. Primeiramente valida os dados
	 * (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade. Isto,
	 * através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItemCondicaoRenegociacaoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CondicaoRenegociacaoUnidadeEnsinoVO obj) throws Exception {
		validarDados(obj);

		final String sql = "INSERT INTO CondicaoRenegociacaoUnidadeEnsino( condicaoRenegociacao, unidadeEnsino, contaCorrente) VALUES ( ?, ?, ?) returning codigo";
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);

				if (obj.getCondicaoRenegociacaoVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(1, obj.getCondicaoRenegociacaoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(1, 0);
				}
				if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(2, obj.getUnidadeEnsinoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(2, 0);
				}
				if (obj.getContaCorrenteVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(3, obj.getContaCorrenteVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(3, 0);
				}

				return sqlInserir;
			}
		}, new ResultSetExtractor() {
			public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					// obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		// obj.setNovoObj(Boolean.FALSE);
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ItemCondicaoRenegociacaoVO</code>. Sempre utiliza a chave primária da
	 * classe como atributo para localização do registro a ser alterado.
	 * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica
	 * a conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItemCondicaoRenegociacaoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CondicaoRenegociacaoUnidadeEnsinoVO obj) throws Exception {
		validarDados(obj);

		final String sql = "UPDATE CondicaoRenegociacaoUnidadeEnsino set condicaoRenegociacao=?, unidadeEnsino=?, contaCorrente=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				if (obj.getCondicaoRenegociacaoVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(1, obj.getCondicaoRenegociacaoVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(2, obj.getUnidadeEnsinoVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(2, 0);
				}
				if (obj.getContaCorrenteVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(3, obj.getContaCorrenteVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(3, 0);
				}
				sqlAlterar.setInt(4, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ItemCondicaoRenegociacaoVO</code>. Sempre localiza o registro a ser
	 * excluído através da chave primária da entidade. Primeiramente verifica a
	 * conexão com o banco de dados e a permissão do usuário para realizar esta
	 * operacão na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ItemCondicaoRenegociacaoVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public void excluir(CondicaoRenegociacaoUnidadeEnsinoVO obj) throws Exception {
		CondicaoRenegociacaoUnidadeEnsino.excluir(getIdEntidade());
		String sql = "DELETE FROM CondicaoRenegociacaoUnidadeEnsino WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });

	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ItemCondicaoRenegociacaoVO</code>. Todos os tipos de consistência de
	 * dados são e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para os
	 * atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é gerada
	 *                uma exceção descrevendo o atributo e o erro ocorrido.
	 */
	public void validarDados(CondicaoRenegociacaoUnidadeEnsinoVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		ConsistirException consistir = new ConsistirException();

		if (consistir.existeErroListaMensagemErro()) {
			throw consistir;
		}

	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis na Tela
	 * ItemCondicaoRenegociacaoCons.jsp. Define o tipo de consulta a ser executada,
	 * por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na sessao
	 * da pagina.
	 */
	public List<CondicaoRenegociacaoUnidadeEnsinoVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		if (campoConsulta.equals("codigo")) {
			if (valorConsulta.trim().equals("")) {
				throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
			}
			if (valorConsulta.equals("")) {
				valorConsulta = "0";
			}
			int valorInt = Integer.parseInt(valorConsulta);
			return consultarPorCodigo(valorInt, controlarAcesso, usuario, Uteis.NIVELMONTARDADOS_TODOS);
		}
		return new ArrayList(0);
	}

	/**
	 * Responsável por realizar uma consulta de
	 * <code>ItemCondicaoRenegociacao</code> através do valor do atributo
	 * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou
	 * superiores ao parâmetro fornecido. Faz uso da operação
	 * <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
	 * resultante.
	 * 
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>ItemCondicaoRenegociacaoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario, int nivelMontarDados) throws Exception {
		CondicaoRenegociacaoUnidadeEnsino.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM CondicaoRenegociacaoUnidadeEnsino WHERE codigo = ?  ORDER BY codigo";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados, usuario));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>ItemCondicaoRenegociacaoVO</code> resultantes da consulta.
	 */
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados
	 * (<code>ResultSet</code>) em um objeto da classe
	 * <code>ItemCondicaoRenegociacaoVO</code>.
	 * 
	 * @return O objeto da classe <code>ItemCondicaoRenegociacaoVO</code> com os
	 *         dados devidamente montados.
	 */
	public static CondicaoRenegociacaoUnidadeEnsinoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		CondicaoRenegociacaoUnidadeEnsinoVO obj = new CondicaoRenegociacaoUnidadeEnsinoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.getCondicaoRenegociacaoVO().setCodigo(dadosSQL.getInt("condicaoRenegociacao"));
		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(true);
		obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("contaCorrente"));

		obj.setNovoObj(new Boolean(false));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
		montarDadosContaCorrente(obj, nivelMontarDados, usuarioVO);
		obj.getUnidadeEnsinoVO().setFiltrarUnidadeEnsino(true);
		return obj;
	}
	
	public static void montarDadosUnidadeEnsino(CondicaoRenegociacaoUnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsinoVO(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoVO().getCodigo(), false, nivelMontarDados, usuario));
    }
	
	public static void montarDadosContaCorrente(CondicaoRenegociacaoUnidadeEnsinoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getContaCorrenteVO().getCodigo().intValue() == 0) {
            obj.setContaCorrenteVO(new ContaCorrenteVO());
            return;
        }
        obj.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrenteVO().getCodigo(), false, nivelMontarDados, usuario));
    }

	/**
	 * Operação responsável por excluir todos os objetos da
	 * <code>ItemCondicaoRenegociacaoVO</code> no BD. Faz uso da operação
	 * <code>excluir</code> disponível na classe
	 * <code>ItemCondicaoRenegociacao</code>.
	 * 
	 * @param <code>condicaoRenegociacao</code>
	 *            campo chave para exclusão dos objetos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirCondicaoRenegociacaoUnidadeEnsinoVOs(Integer condicaoRenegociacao, List<CondicaoRenegociacaoUnidadeEnsinoVO> objetos) throws Exception {
		if (objetos != null && !objetos.isEmpty()) {
			String sql = "DELETE FROM CondicaoRenegociacaoUnidadeEnsino WHERE (condicaoRenegociacao = ?)";
			sql += " and codigo not in(";
			boolean primeiro = true;
			for (CondicaoRenegociacaoUnidadeEnsinoVO obj : objetos) {
				if (obj.getUnidadeEnsinoVO().getFiltrarUnidadeEnsino()) {
					if (primeiro) {
						sql += obj.getCodigo();
						primeiro = false;
					} else {
						sql += ", " + obj.getCodigo();
					}
				}
			}
			sql += " )";
			getConexao().getJdbcTemplate().update(sql, new Object[] { condicaoRenegociacao });
		}
	}

	/**
	 * Operação responsável por alterar todos os objetos da
	 * <code>ItemCondicaoRenegociacaoVO</code> contidos em um Hashtable no BD. Faz
	 * uso da operação <code>excluirItemCondicaoRenegociacaos</code> e
	 * <code>incluirItemCondicaoRenegociacaos</code> disponíveis na classe
	 * <code>ItemCondicaoRenegociacao</code>.
	 * 
	 * @param objetos
	 *            List com os objetos a serem alterados ou incluídos no BD.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Override
	public void alterarCondicaoRenegociacaoUnidadeEnsinoVOs(Integer condicaoRenegociacao, List<CondicaoRenegociacaoUnidadeEnsinoVO> objetos) throws Exception {
		excluirCondicaoRenegociacaoUnidadeEnsinoVOs(condicaoRenegociacao, objetos);
		for (CondicaoRenegociacaoUnidadeEnsinoVO obj : objetos) {
			if (obj.getUnidadeEnsinoVO().getFiltrarUnidadeEnsino()) {
				obj.getCondicaoRenegociacaoVO().setCodigo(condicaoRenegociacao);
				if (obj.getNovoObj()) {
					incluir(obj);
				} else {
					alterar(obj);
				}
			}
		}
	}

	/**
	 * Operação responsável por incluir objetos da
	 * <code>ItemCondicaoRenegociacaoVO</code> no BD. Garantindo o relacionamento
	 * com a entidade principal <code>financeiro.CondicaoRenegociacao</code> através
	 * do atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta
	 *                operação.
	 */
	@Override
	public void incluirCondicaoRenegociacaoUnidadeEnsinoVOs(Integer condicaoRenegociacaoPrm, List<CondicaoRenegociacaoUnidadeEnsinoVO> objetos) throws Exception {
		for (CondicaoRenegociacaoUnidadeEnsinoVO obj : objetos) {
			if (obj.getUnidadeEnsinoVO().getFiltrarUnidadeEnsino()) {
				obj.getCondicaoRenegociacaoVO().setCodigo(condicaoRenegociacaoPrm);
				incluir(obj);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public CondicaoRenegociacaoUnidadeEnsinoVO consultarCondicaoRenegociacaoUnidadeEnsinoPorCondicaoRenegociacaoPorUnidadeEnsino(Integer condicaoRenegociacao, Integer unidadeensino, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		CondicaoRenegociacaoUnidadeEnsino.consultar(getIdEntidade());
		String sql = "SELECT * FROM CondicaoRenegociacaoUnidadeEnsino WHERE condicaoRenegociacao = " + condicaoRenegociacao + " and unidadeEnsino = "+ unidadeensino;
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (!resultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( CondicaoRenegociacaoUnidadeEnsino ).");
		}
		return (montarDados(resultado, nivelMontarDados, usuarioVO));
	}
	
	@Override
	public List consultarCondicaoRenegociacaoUnidadeEnsinoPorCondicaoRenegociacao(Integer condicaoRenegociacao, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		CondicaoRenegociacaoUnidadeEnsino.consultar(getIdEntidade());
		List objetos = new ArrayList();
		String sql = "SELECT * FROM CondicaoRenegociacaoUnidadeEnsino WHERE condicaoRenegociacao = " + condicaoRenegociacao;
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		while (resultado.next()) {
			CondicaoRenegociacaoUnidadeEnsinoVO novoObj = new CondicaoRenegociacaoUnidadeEnsinoVO();
			novoObj = montarDados(resultado, nivelMontarDados, usuarioVO);
			objetos.add(novoObj);
		}
		return objetos;
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>ItemCondicaoRenegociacaoVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public CondicaoRenegociacaoUnidadeEnsinoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CondicaoRenegociacaoUnidadeEnsino.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM CondicaoRenegociacaoUnidadeEnsino WHERE codigo = " + codigoPrm;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( CondicaoRenegociacaoUnidadeEnsino ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return CondicaoRenegociacaoUnidadeEnsino.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de negócio
	 * pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o
	 * controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		CondicaoRenegociacaoUnidadeEnsino.idEntidade = idEntidade;
	}
}