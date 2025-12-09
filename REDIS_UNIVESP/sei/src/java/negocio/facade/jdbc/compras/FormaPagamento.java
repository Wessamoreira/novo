package negocio.facade.jdbc.compras;

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

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FormaPagamentoVO.enumCampoConsultaFormaPagamento;
import negocio.comuns.financeiro.enumerador.PermitirCartaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.FormaPagamentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>FormaPagamentoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>FormaPagamentoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see FormaPagamentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class FormaPagamento extends ControleAcesso implements FormaPagamentoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public FormaPagamento() throws Exception {
		super();
		setIdEntidade("FormaPagamento");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>FormaPagamentoVO</code>.
	 */
	public FormaPagamentoVO novo() throws Exception {
		FormaPagamento.incluir(getIdEntidade());
		FormaPagamentoVO obj = new FormaPagamentoVO();
		return obj;
	}

	public void verificarExistenciaFormaPagamento(String nomeCat, Integer codigo) throws Exception {
		String sqlStr = "SELECT * FROM FormaPagamento WHERE lower (nome) = ('" + nomeCat.toLowerCase() + "') ";
		if (codigo.intValue() != 0) {
			sqlStr += " and codigo != " + codigo.intValue();
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (tabelaResultado.next()) {
			throw new Exception("Já existe um Forma de Pagamento cadastrado com este nome.");
		}
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>FormaPagamentoVO</code>. Primeiramente valida os dados (
	 * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
	 * dados e a permissão do usuário para realizar esta operacão na entidade.
	 * Isto, através da operação <code>incluir</code> da superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>FormaPagamentoVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final FormaPagamentoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			FormaPagamento.incluir(getIdEntidade(), true, usuarioVO);
			validarDados(obj);
			verificarExistenciaFormaPagamento(obj.getNome(), 0);
			obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO FormaPagamento( nome, tipo, usaNoRecebimento ) VALUES ( ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setString(1, obj.getNome());
					sqlInserir.setString(2, obj.getTipo());
					sqlInserir.setBoolean(3, obj.getUsaNoRecebimento());
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
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>FormaPagamentoVO</code>. Sempre utiliza a chave primária da classe
	 * como atributo para localização do registro a ser alterado. Primeiramente
	 * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>alterar</code> da
	 * superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>FormaPagamentoVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final FormaPagamentoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			FormaPagamento.alterar(getIdEntidade(), true, usuarioVO);
			validarDados(obj);
			verificarExistenciaFormaPagamento(obj.getNome(), obj.getCodigo());
			obj.realizarUpperCaseDados();
			final String sql = "UPDATE FormaPagamento set nome=?, tipo=?, usaNoRecebimento=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setString(1, obj.getNome());
					sqlAlterar.setString(2, obj.getTipo());
					sqlAlterar.setBoolean(3, obj.getUsaNoRecebimento());
					sqlAlterar.setInt(4, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>FormaPagamentoVO</code>. Sempre localiza o registro a ser excluído
	 * através da chave primária da entidade. Primeiramente verifica a conexão
	 * com o banco de dados e a permissão do usuário para realizar esta operacão
	 * na entidade. Isto, através da operação <code>excluir</code> da
	 * superclasse.
	 *
	 * @param obj
	 *            Objeto da classe <code>FormaPagamentoVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(FormaPagamentoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			FormaPagamento.excluir(getIdEntidade(), true, usuarioVO);
			String sql = "DELETE FROM FormaPagamento WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>FormaPagamentoVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(FormaPagamentoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Forma de Pagamento) deve ser informado.");
        }
        if (obj.getTipo().equals("")) {
            throw new ConsistirException("O campo TIPO (Forma de Pagamento) deve ser informado.");
        }
    }


	/**
	 * Responsável por realizar uma consulta de <code>FormaPagamento</code>
	 * através do valor do atributo <code>String nome</code>. Retorna os
	 * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>FormaPagamentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */

	public List<FormaPagamentoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FormaPagamento WHERE upper( nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}
	
	public List<FormaPagamentoVO> consultarPorTipo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FormaPagamento WHERE tipo  = '" + valorConsulta.toUpperCase() + "' ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}
	
	public List<FormaPagamentoVO> consultarFormaPagamentoCartoesCredito(boolean controlarAcesso, Boolean usaNoRecebimento, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr =  new StringBuilder("SELECT * FROM FormaPagamento WHERE tipo  in ('CA')  ");
		if(Uteis.isAtributoPreenchido(usaNoRecebimento)){
			sqlStr.append(" and usanorecebimento =  ").append(usaNoRecebimento);	
		}
		sqlStr.append(" ORDER BY nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}
	
	public List<FormaPagamentoVO> consultarFormaPagamentoCartoes(boolean controlarAcesso, Boolean usaNoRecebimento, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr =  new StringBuilder("SELECT * FROM FormaPagamento WHERE tipo  in ('CA', 'CD')  ");
		if(Uteis.isAtributoPreenchido(usaNoRecebimento)){
			sqlStr.append(" and usanorecebimento =  ").append(usaNoRecebimento);	
		}
		sqlStr.append(" ORDER BY nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}
	
	public List<FormaPagamentoVO> consultarFormaPagamentoDaMovimentacaoFinanceira(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FormaPagamento WHERE tipo  in ('DI', 'CH') ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	public List<FormaPagamentoVO> consultarPorNomeUsaNoRecebimento(String valorConsulta, Boolean usaNoRecebimento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FormaPagamento WHERE usaNoRecebimento = " + usaNoRecebimento + " AND upper( nome ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nome";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}
	
	public List<FormaPagamentoVO> consultarPorTipoCartaoOnline(PermitirCartaoEnum tipo, Boolean usaNoRecebimento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM FormaPagamento WHERE usaNoRecebimento = ").append(usaNoRecebimento);
		if (PermitirCartaoEnum.CREDITO.equals(tipo)) {
			sqlStr.append(" AND tipo = 'CA'");
		} else if (PermitirCartaoEnum.DEBITO.equals(tipo)) {
			sqlStr.append(" AND tipo = 'CD'");
		} else {
			sqlStr.append(" AND tipo in ('CA','CD')");
		}
		sqlStr.append(" ORDER BY nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	/**
	 * Responsável por realizar uma consulta de <code>FormaPagamento</code>
	 * através do valor do atributo <code>Integer codigo</code>. Retorna os
	 * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
	 * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
	 * prerarar o List resultante.
	 *
	 * @param controlarAcesso
	 *            Indica se a aplicação deverá verificar se o usuário possui
	 *            permissão para esta consulta ou não.
	 * @return List Contendo vários objetos da classe
	 *         <code>FormaPagamentoVO</code> resultantes da consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	public List<FormaPagamentoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM FormaPagamento WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	public FormaPagamentoVO consultarPorTipoDinheiro(int nivelMontarDados) throws Exception {

		String sqlStr = "SELECT * FROM FormaPagamento WHERE tipo = 'DI' ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new FormaPagamentoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados));
	}

	public FormaPagamentoVO consultarPorTipo(String tipo, int nivelMontarDados) throws Exception {
		String sqlStr = "SELECT * FROM FormaPagamento WHERE tipo = ? ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[] { tipo });
		if (!tabelaResultado.next()) {
			return new FormaPagamentoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados));
	}

	public FormaPagamentoVO consultarPorTipoDebitoEmContaCorrente(int nivelMontarDados) throws Exception {
		String sqlStr = "SELECT * FROM FormaPagamento WHERE tipo = 'DE' ORDER BY codigo LIMIT 1";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new FormaPagamentoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados));
	}

	public FormaPagamentoVO consultarPorTipoNome(String tipo, String nome, int nivelMontarDados) throws Exception {
		String sqlStr = "SELECT * FROM FormaPagamento WHERE tipo = '" + tipo + "' and upper(FormaPagamento.nome) like('" + nome.toUpperCase() + "') ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new FormaPagamentoVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados));
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma
	 * consulta ao banco de dados ( <code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 *
	 * @return List Contendo vários objetos da classe
	 *         <code>FormaPagamentoVO</code> resultantes da consulta.
	 */
	public static List<FormaPagamentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
		List<FormaPagamentoVO> vetResultado = new ArrayList<FormaPagamentoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>FormaPagamentoVO</code>.
	 *
	 * @return O objeto da classe <code>FormaPagamentoVO</code> com os dados
	 *         devidamente montados.
	 */
	public static FormaPagamentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
		FormaPagamentoVO obj = new FormaPagamentoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNome(dadosSQL.getString("nome"));
		obj.setTipo(dadosSQL.getString("tipo"));
		obj.setUsaNoRecebimento(dadosSQL.getBoolean("usaNoRecebimento"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}

	/**
	 * Operação responsável por localizar um objeto da classe
	 * <code>FormaPagamentoVO</code> através de sua chave primária.
	 *
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto
	 *                procurado.
	 */
	public FormaPagamentoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		return getAplicacaoControle().getFormaPagamentoVO(codigoPrm, usuario);
//		String sql = "SELECT * FROM FormaPagamento WHERE codigo = ?";
//		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
//		if (!tabelaResultado.next()) {
//			throw new ConsistirException("Dados Não Encontrados ( FormaPagamento ).");
//		}
//		return (montarDados(tabelaResultado, nivelMontarDados));
	}
	
	@Override
	public FormaPagamentoVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM FormaPagamento WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( FormaPagamento ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados));
	}

	public FormaPagamentoVO consultarFormaPagamentoCheque(UsuarioVO usuario) throws Exception {
		List<FormaPagamentoVO> listaFormaPagamento = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		Iterator<FormaPagamentoVO> i = listaFormaPagamento.iterator();
		while (i.hasNext()) {
			FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
			if (obj.getTipo().equals("CH")) {
				return obj;
			}
		}
		return new FormaPagamentoVO();
	}

	@Override
	public boolean executarVerificacaoFormaPagamentoVinculadoNegociacaoRecebimento(Integer formaPagamento) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT codigo FROM formapagamentonegociacaorecebimento where formapagamento = ").append(formaPagamento);
		sqlStr.append(" union ");
		sqlStr.append("SELECT codigo FROM formapagamentonegociacaopagamento where formapagamento = ").append(formaPagamento);
		sqlStr.append(" limit 1");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()).next();
	}

	public List<FormaPagamentoVO> consultarFormaPagamentoFaltandoLista(List<FormaPagamentoVO> formaPagamentoVOs, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT * FROM FormaPagamento ");
		sb.append(" where 1=1 ");
		if (!formaPagamentoVOs.isEmpty()) {
			sb.append("and formaPagamento.codigo  not in ( ");
		}
		int x = 0;
		for (FormaPagamentoVO formaPagamentoVO : formaPagamentoVOs) {
			if (x > 0) {
				sb.append(", ");
			}
			sb.append(formaPagamentoVO.getCodigo());
			x++;
		}
		if (!formaPagamentoVOs.isEmpty()) {
			sb.append(" ) ");
		}
		sb.append("ORDER BY formaPagamento.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void consultarPorEnumCampoConsulta(DataModelo dataModelo) throws Exception {
		dataModelo.getListaFiltros().clear();

		dataModelo.setListaConsulta(consultarFormaPagamento(dataModelo));
		dataModelo.setTotalRegistrosEncontrados(consultarTotalFormaPagamento(dataModelo));	
	}

	private List<FormaPagamentoVO> consultarFormaPagamento(DataModelo dataModelo) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM formapagamento");
		sql.append(" WHERE 1 = 1");
		dataModelo.setLimitePorPagina(10);

		switch (enumCampoConsultaFormaPagamento.valueOf(dataModelo.getCampoConsulta())) {
		case NOME:
			dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);
			sql.append(" AND nome like UPPER(sem_acentos(?))");
			break;
		case CODIGO :
			dataModelo.getListaFiltros().add(Integer.parseInt(dataModelo.getValorConsulta()));
			sql.append(" AND codigo = ?");
			break;
		default:
			break;
		}

		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());

		return montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	private Integer consultarTotalFormaPagamento(DataModelo dataModelo) {
		StringBuilder sql = new StringBuilder("SELECT COUNT(codigo) as qtde FROM formapagamento");
        sql.append(" WHERE 1 = 1");

        switch (enumCampoConsultaFormaPagamento.valueOf(dataModelo.getCampoConsulta())) {
        case NOME:
			sql.append(" AND nome like UPPER(sem_acentos(?))");
			break;
		case CODIGO:
			sql.append(" AND codigo = ?");
			break;
		default:
			break;
		}

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return FormaPagamento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		FormaPagamento.idEntidade = idEntidade;
	}
}