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
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.FormaPagamentoNegociacaoRecebimentoDCCInterfaceFacade;

/**
 * @author Victor Hugo de Paula Costa - 20 de abr de 2016
 *
 */
@Lazy
@Repository
@Scope("singleton")
public class FormaPagamentoNegociacaoRecebimentoDCC extends ControleAcesso implements FormaPagamentoNegociacaoRecebimentoDCCInterfaceFacade {

	private static final long serialVersionUID = 1L;
	/**
	 * @author Victor Hugo de Paula Costa - 20 de abr de 2016
	 */
	
	protected static String idEntidade;

	public FormaPagamentoNegociacaoRecebimentoDCC() throws Exception {
		super();
		setIdEntidade("FormaPagamentoNegociacaoRecebimentoDCC");
	}
	
	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return NegociacaoRecebimento.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		NegociacaoRecebimentoDCC.idEntidade = idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluir(final FormaPagamentoNegociacaoRecebimentoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		FormaPagamentoNegociacaoRecebimentoDCC.incluir(getIdEntidade(), verificarAcesso, usuario);
		getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().persistir(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(), usuario);
		FormaPagamentoNegociacaoRecebimentoVO.validarDados(obj);
		final String sql = "INSERT INTO FormaPagamentoNegociacaoRecebimentodcc( negociacaoRecebimentodcc, formaPagamento, valorRecebimento, contaCorrente, qtdeParcelasCartaoCredito, operadoraCartao, taxaOperadora, taxaAntecipacao, categoriaDespesa, contaCorrenteOperadoraCartao, formaPagamentoNegociacaoRecebimentoCartaoCredito, usuarioDesbloqueouFormaRecebimentoNoRecebimento, dataUsuarioDesbloqueouFormaRecebimentoNoRecebimento, configuracaorecebimentocartaocredito) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
		obj.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				if (obj.getNegociacaoRecebimento().intValue() != 0) {
					sqlInserir.setInt(1, obj.getNegociacaoRecebimento().intValue());
				} else {
					sqlInserir.setNull(1, 0);
				}
				sqlInserir.setInt(2, obj.getFormaPagamento().getCodigo().intValue());
				sqlInserir.setDouble(3, obj.getValorRecebimento().doubleValue());
				if (obj.getContaCorrente().getCodigo().intValue() != 0) {
					sqlInserir.setInt(4, obj.getContaCorrente().getCodigo().intValue());
				} else {
					sqlInserir.setNull(4, 0);
				}
				if (!obj.getQtdeParcelasCartaoCredito().equals(0)) {
					sqlInserir.setInt(5, obj.getQtdeParcelasCartaoCredito());
				} else {
					sqlInserir.setNull(5, 0);
				}
				if (!obj.getOperadoraCartaoVO().getCodigo().equals(0)) {
					sqlInserir.setInt(6, obj.getOperadoraCartaoVO().getCodigo());
				} else {
					sqlInserir.setNull(6, 0);
				}
				if (!obj.getConfiguracaoFinanceiroCartaoVO().getTaxaDeOperacao().equals(0.0)) {
					sqlInserir.setDouble(7, obj.getConfiguracaoFinanceiroCartaoVO().getTaxaDeOperacao());
				} else {
					sqlInserir.setNull(7, 0);
				}
				if (!obj.getConfiguracaoFinanceiroCartaoVO().getTaxaDeAntecipacao().equals(0.0)) {
					sqlInserir.setDouble(8, obj.getConfiguracaoFinanceiroCartaoVO().getTaxaDeAntecipacao());
				} else {
					sqlInserir.setNull(8, 0);
				}
				if (!obj.getCategoriaDespesaVO().getCodigo().equals(0)) {
					sqlInserir.setInt(9, obj.getCategoriaDespesaVO().getCodigo());
				} else {
					sqlInserir.setNull(9, 0);
				}
				if (obj.getContaCorrenteOperadoraCartaoVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(10, obj.getContaCorrenteOperadoraCartaoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(10, 0);
				}
				if (obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(11, obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(11, 0);
				}
				if (obj.getUsuarioDesbloqueouFormaRecebimentoNoRecebimento() != null) {
					sqlInserir.setInt(12, obj.getUsuarioDesbloqueouFormaRecebimentoNoRecebimento().getCodigo().intValue());
				} else {
					sqlInserir.setNull(12, 0);
				}
				if (obj.getDataUsuarioDesbloqueouFormaRecebimentoNoRecebimento() != null) {
					sqlInserir.setDate(13, Uteis.getDataJDBC(obj.getDataUsuarioDesbloqueouFormaRecebimentoNoRecebimento()));
				} else {
					sqlInserir.setNull(13, 0);
				}	
				if(!obj.getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo().equals(0)) {
					sqlInserir.setInt(14, obj.getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo());
				} else {
					sqlInserir.setNull(14, 0);
				}
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
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirFormaPagamentoNegociacaoRecebimentos(NegociacaoRecebimentoVO negociacaoRecebimento, boolean controleAcesso, UsuarioVO usuario) throws Exception {
		negociacaoRecebimento.setContaCorrenteCaixa(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(negociacaoRecebimento.getContaCorrenteCaixa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		for (FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO : negociacaoRecebimento.getFormaPagamentoNegociacaoRecebimentoVOs()) {
			formaPagamentoNegociacaoRecebimentoVO.setNegociacaoRecebimento(negociacaoRecebimento.getCodigo());
			incluir(formaPagamentoNegociacaoRecebimentoVO, controleAcesso, usuario);
		}
	}
	
	@Override
	public List<FormaPagamentoNegociacaoRecebimentoVO> consultarPorCodigoCondicaoPagamentoNegociacaoRecebimento(Integer codigoCondicaoPagamentoNegocicaoRecebimento, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT formapagamentonegociacaorecebimentodcc.contacorrente, valorrecebimento, formapagamento, formapagamentonegociacaorecebimentodcc.negociacaorecebimentodcc, ");
		sqlStr.append("       formapagamentonegociacaorecebimentodcc.codigo, qtdeparcelascartaocredito, operadoracartao, taxaoperadora, ");
		sqlStr.append("       taxaantecipacao, categoriadespesa, contacorrenteoperadoracartao, ");
		sqlStr.append("       formapagamentonegociacaorecebimentocartaocredito, usuariodesbloqueouformarecebimentonorecebimento, ");
		sqlStr.append("       datausuariodesbloqueouformarecebimentonorecebimento, configuracaorecebimentocartaocredito");
		sqlStr.append(" FROM formapagamentonegociacaorecebimentodcc");
		sqlStr.append(" inner join formapagamentonegociacaorecebimentocartaocredito ");
		sqlStr.append(" on formapagamentonegociacaorecebimentodcc.formapagamentonegociacaorecebimentocartaocredito = formapagamentonegociacaorecebimentocartaocredito.codigo");
		sqlStr.append(" inner join contareceber on contareceber.codigo = formapagamentonegociacaorecebimentocartaocredito.contareceber");
		sqlStr.append(" inner join contarecebernegociacaorecebimentodcc on contarecebernegociacaorecebimentodcc.contareceber = contareceber.codigo");
		sqlStr.append(" and contarecebernegociacaorecebimentodcc.negociacaorecebimentodcc = formapagamentonegociacaorecebimentodcc.negociacaorecebimentodcc");
		sqlStr.append(" where contarecebernegociacaorecebimentodcc.codigo = ").append(codigoCondicaoPagamentoNegocicaoRecebimento);
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(resultado, nivelMontarDados, usuarioVO);
	}
	
    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da consulta.
     */
    public List<FormaPagamentoNegociacaoRecebimentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<FormaPagamentoNegociacaoRecebimentoVO> vetResultado = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }
    
    /**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>FormaPagamentoNegociacaoRecebimentoVO</code>.
	 * 
	 * @return O objeto da classe
	 *         <code>FormaPagamentoNegociacaoRecebimentoVO</code> com os dados
	 *         devidamente montados.
	 */
	public FormaPagamentoNegociacaoRecebimentoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		FormaPagamentoNegociacaoRecebimentoVO obj = new FormaPagamentoNegociacaoRecebimentoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setNegociacaoRecebimento(new Integer(dadosSQL.getInt("negociacaoRecebimentodcc")));
		obj.getFormaPagamento().setCodigo(new Integer(dadosSQL.getInt("formaPagamento")));
		obj.setValorRecebimento(new Double(dadosSQL.getDouble("valorRecebimento")));
		obj.getContaCorrente().setCodigo(new Integer(dadosSQL.getInt("contaCorrente")));
		obj.getContaCorrenteOperadoraCartaoVO().setCodigo(new Integer(dadosSQL.getInt("contaCorrenteOperadoraCartao")));
		obj.getOperadoraCartaoVO().setCodigo(dadosSQL.getInt("operadoraCartao"));
		obj.setQtdeParcelasCartaoCredito(dadosSQL.getInt("qtdeParcelasCartaoCredito"));
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setCodigo(dadosSQL.getInt("formaPagamentoNegociacaoRecebimentoCartaoCredito"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("configuracaorecebimentocartaocredito"))) {
			obj.getConfiguracaoRecebimentoCartaoOnlineVO().setCodigo(dadosSQL.getInt("configuracaorecebimentocartaocredito"));
			obj.setConfiguracaoRecebimentoCartaoOnlineVO(getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().consultarPorChavePrimaria(obj.getConfiguracaoRecebimentoCartaoOnlineVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setCodigo(dadosSQL.getInt("formapagamentonegociacaorecebimentocartaocredito"));
		obj.setFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO(getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoCartaoCreditoFacade().consultarPorChavePrimaria(obj.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		montarDadosFormaPagamento(obj, nivelMontarDados, usuario);
		montarDadosOperadoraCartao(obj, nivelMontarDados, usuario);
//		obj.setNegociacaoRecebimentoVO(getFacadeFactory().getNegociacaoRecebimentoDCCFacade().consultarPorChavePrimaria(obj.getNegociacaoRecebimento(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, usuario));
		return obj;
	}
	
	public void montarDadosFormaPagamento(FormaPagamentoNegociacaoRecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFormaPagamento().getCodigo().intValue() == 0) {
			obj.setFormaPagamento(new FormaPagamentoVO());
			return;
		}
		obj.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamento().getCodigo(), false, nivelMontarDados, usuario));
	}
	
	public void montarDadosOperadoraCartao(FormaPagamentoNegociacaoRecebimentoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getOperadoraCartaoVO().getCodigo().intValue() == 0) {
			obj.setOperadoraCartaoVO(new OperadoraCartaoVO());
			return;
		}
		obj.setOperadoraCartaoVO(getFacadeFactory().getOperadoraCartaoFacade().consultarPorChavePrimaria(obj.getOperadoraCartaoVO().getCodigo(), nivelMontarDados, usuario));
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirMotivoCancelamento(final FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, UsuarioVO usuario) throws Exception {
		final String sql = "UPDATE FormaPagamentoNegociacaoRecebimentodcc set motivocancelamento=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setString(1, formaPagamentoNegociacaoRecebimentoVO.getMotivoCancelamento());
				sqlAlterar.setInt(2, formaPagamentoNegociacaoRecebimentoVO.getCodigo());
				return sqlAlterar;
			}
		});
	}
	
	@Override
	public FormaPagamentoNegociacaoRecebimentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), false, usuario);
		String sql = "SELECT * FROM FormaPagamentoNegociacaoRecebimentodcc WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { codigoPrm });
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( FormaPagamentoNegociacaoRecebimento ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List<FormaPagamentoNegociacaoRecebimentoVO> consultarFormaPagamentoNegociacaoRecebimentoVOSDCCPorCodigoContaReceber(Integer codigoContaReceber, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("	select formapagamentonegociacaorecebimentodcc.* from formapagamentonegociacaorecebimentodcc");
		sqlStr.append(" inner join formapagamentonegociacaorecebimentocartaocredito on formapagamentonegociacaorecebimentocartaocredito.codigo = formapagamentonegociacaorecebimentodcc.formapagamentonegociacaorecebimentocartaocredito");
		sqlStr.append(" where formapagamentonegociacaorecebimentocartaocredito.contareceber = ").append(codigoContaReceber);
		sqlStr.append(" AND formapagamentonegociacaorecebimentocartaocredito.situacao <> 'CF'");
		SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(resultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
	}
	
	/**
	 * Responsável por realizar uma consulta de
	 * <code>FormaPagamentoNegociacaoRecebimento</code> através do valor do
	 * atributo <code>codigo</code> da classe <code>NegociacaoRecebimento</code>
	 * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
	 * trabalho de prerarar o List resultante.
	 * 
	 * @return List Contendo vários objetos da classe
	 *         <code>FormaPagamentoNegociacaoRecebimentoVO</code> resultantes da
	 *         consulta.
	 * @exception Exception
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	public List<FormaPagamentoNegociacaoRecebimentoVO> consultarPorCodigoNegociacaoRecebimento(Integer valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "SELECT FormaPagamentoNegociacaoRecebimentoDCC.* FROM FormaPagamentoNegociacaoRecebimentoDCC, NegociacaoRecebimentoDCC WHERE FormaPagamentoNegociacaoRecebimentoDCC.negociacaoRecebimentoDCC = NegociacaoRecebimentoDCC.codigo and NegociacaoRecebimentoDCC.codigo = " + valorConsulta.intValue() + " ORDER BY NegociacaoRecebimentoDCC.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
}
