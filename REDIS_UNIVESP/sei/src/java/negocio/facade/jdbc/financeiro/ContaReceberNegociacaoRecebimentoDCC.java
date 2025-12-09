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
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaReceberNegociacaoRecebimentoDCCInterfaceFacade;

/**
 * @author Victor Hugo de Paula Costa - 27 de abr de 2016
 *
 */
@Lazy
@Repository
@Scope("singleton")
public class ContaReceberNegociacaoRecebimentoDCC extends ControleAcesso implements ContaReceberNegociacaoRecebimentoDCCInterfaceFacade {
	/**
	 * @author Victor Hugo de Paula Costa - 27 de abr de 2016
	 */
	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

	public ContaReceberNegociacaoRecebimentoDCC() throws Exception {
		super();
		setIdEntidade("ContaReceberNegociacaoRecebimento");
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return ContaReceberNegociacaoRecebimentoDCC.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste
	 * identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ContaReceberNegociacaoRecebimentoDCC.idEntidade = idEntidade;
	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code>. Primeiramente valida
	 * os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code> que será gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluir(final ContaReceberNegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ContaReceberNegociacaoRecebimentoVO.validarDados(obj);
		obj.realizarUpperCaseDados();
		final String sql = "INSERT INTO ContaReceberNegociacaoRecebimentodcc( negociacaoRecebimentodcc, contaReceber, valorTotal, contaReceberTerceiro ) VALUES ( ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				if (obj.getNegociacaoRecebimento().intValue() != 0) {
					sqlInserir.setInt(1, obj.getNegociacaoRecebimento().intValue());
				} else {
					sqlInserir.setNull(1, 0);
				}
				if (obj.getContaReceber().getCodigo().intValue() != 0) {
					sqlInserir.setInt(2, obj.getContaReceber().getCodigo().intValue());
				} else {
					sqlInserir.setNull(2, 0);
				}
				sqlInserir.setDouble(3, obj.getValorTotal().doubleValue());
				sqlInserir.setBoolean(4, obj.getContaReceberTerceiro());
				return sqlInserir;
			}
		}, new ResultSetExtractor<Integer>() {
			public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
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
	 * Operação responsável por incluir objetos da <code>ContaReceberNegociacaoRecebimentoVO</code> no BD. Garantindo o relacionamento com a entidade
	 * principal <code>financeiro.NegociacaoRecebimentoDCC</code> através do atributo de vínculo.
	 * 
	 * @param objetos
	 *            List contendo os objetos a serem gravados no BD da classe.
	 * @exception Exception
	 *                Erro de conexão com o BD ou restrição de acesso a esta operação.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirContaReceberNegociacaoRecebimentos(Integer negociacaoRecebimentoPrm, List<ContaReceberNegociacaoRecebimentoVO> objetos, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		for (ContaReceberNegociacaoRecebimentoVO obj : objetos) {
			obj.setNegociacaoRecebimento(negociacaoRecebimentoPrm);
			incluir(obj, configuracaoFinanceiro, usuario);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public List<ContaReceberNegociacaoRecebimentoVO> consultarPorNegociacaoRecebimentoDCC(Integer codigoNegociacaoRecebimento, int nilveMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuarioVO) throws Exception {
		String sql = "SELECT * FROM ContaReceberNegociacaoRecebimentodcc WHERE negociacaoRecebimentodcc = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoNegociacaoRecebimento });
		return montarDadosConsulta(resultado, nilveMontarDados, configuracaoFinanceiro, usuarioVO);
	}

	/**
	 * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
	 * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
	 * 
	 * @return List Contendo vários objetos da classe <code>ContaReceberNegociacaoRecebimentoVO</code> resultantes da consulta.
	 */
	public List<ContaReceberNegociacaoRecebimentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		List<ContaReceberNegociacaoRecebimentoVO> vetResultado = new ArrayList<ContaReceberNegociacaoRecebimentoVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiro, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>ContaReceberNegociacaoRecebimentoVO</code>.
	 * 
	 * @return O objeto da classe <code>ContaReceberNegociacaoRecebimentoVO</code> com os dados devidamente montados.
	 */
	public ContaReceberNegociacaoRecebimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		ContaReceberNegociacaoRecebimentoVO obj = new ContaReceberNegociacaoRecebimentoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNegociacaoRecebimento(new Integer(dadosSQL.getInt("negociacaoRecebimentodcc")));
		obj.setValorTotal(new Double(dadosSQL.getDouble("valorTotal")));
		obj.getContaReceber().setCodigo(new Integer(dadosSQL.getInt("contaReceber")));
		obj.setContaReceberTerceiro(dadosSQL.getBoolean("contaReceberTerceiro"));
		obj.setFormaPagamentoNegociacaoRecebimentoVOs(getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoDCCFacade().consultarPorCodigoCondicaoPagamentoNegociacaoRecebimento(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		montarDadosContaReceber(obj, nivelMontarDados, configuracaoFinanceiro, usuario);
		return obj;
	}

	public void montarDadosContaReceber(ContaReceberNegociacaoRecebimentoVO obj, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception {
		if (obj.getContaReceber().getCodigo().intValue() == 0) {
			obj.setContaReceber(new ContaReceberVO());
			return;
		}
		obj.setContaReceber(getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(obj.getContaReceber().getCodigo(), false, nivelMontarDados, configuracaoFinanceiro, usuario));
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public List<ContaReceberNegociacaoRecebimentoVO> consultarPorCodigoContaReceber(Integer codigoContaReceber, int nilveMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuarioVO) throws Exception {
		String sql = "SELECT * FROM ContaReceberNegociacaoRecebimentodcc WHERE contareceber = ?";
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoContaReceber });
		return montarDadosConsulta(resultado, nilveMontarDados, configuracaoFinanceiro, usuarioVO);
	}
	
    @Override
    public List<ContaReceberNegociacaoRecebimentoVO> consultarContasAReceberNegociacaoRecebimentoQuePossuemContasAReceberDCC(int nilveMontarDados) throws Exception {
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append("	select contarecebernegociacaorecebimentodcc.* from contarecebernegociacaorecebimentodcc");
    	sqlStr.append("	inner join negociacaorecebimentodcc on negociacaorecebimentodcc.codigo = contarecebernegociacaorecebimentodcc.negociacaorecebimentodcc");
    	sqlStr.append("	inner join formapagamentonegociacaorecebimentodcc on formapagamentonegociacaorecebimentodcc.negociacaorecebimentodcc = negociacaorecebimentodcc.codigo");
    	sqlStr.append("	inner join formapagamentonegociacaorecebimentocartaocredito on formapagamentonegociacaorecebimentocartaocredito.codigo = formapagamentonegociacaorecebimentodcc.formapagamentonegociacaorecebimentocartaocredito");
    	sqlStr.append("	where formapagamentonegociacaorecebimentocartaocredito.situacao = 'AR'");
    	sqlStr.append("	and formapagamentonegociacaorecebimentocartaocredito.datavencimento = now()::date");
    	SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	return (montarDadosConsulta(rowSet, nilveMontarDados,  null, null));
    }
}
