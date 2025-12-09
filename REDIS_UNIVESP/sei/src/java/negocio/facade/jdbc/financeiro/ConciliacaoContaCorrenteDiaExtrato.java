package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConciliacaoContaCorrenteDiaExtratoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ConciliacaoContaCorrenteDiaExtratoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ConciliacaoContaCorrenteDiaExtrato extends ControleAcesso implements ConciliacaoContaCorrenteDiaExtratoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6343683734674040358L;
	protected static String idEntidade;

	public ConciliacaoContaCorrenteDiaExtrato() throws Exception {
		super();
		setIdEntidade("ConciliacaoContaCorrenteDiaExtrato");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<ConciliacaoContaCorrenteDiaExtratoVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (ConciliacaoContaCorrenteDiaExtratoVO obj : lista) {
			persistir(obj, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConciliacaoContaCorrenteDiaExtratoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
		validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaConciliacaoContaConjuntaVO(), "conciliacaoContaCorrenteDiaExtratoConjunta", "conciliacaoContaCorrenteDiaExtrato", obj.getCodigo(), usuarioVO);
		getFacadeFactory().getConciliacaoContaCorrenteDiaExtratoConjuntaFacade().persistir(obj.getListaConciliacaoContaConjuntaVO(), false, usuarioVO);
		if (Uteis.isAtributoPreenchido(obj.getCodigoSei())) {
			getFacadeFactory().getExtratoContaCorrenteFacade().atualizarConciliacaoContaCorrenteDiaExtrato(obj, usuarioVO);
		}

	}

	/**
	 * Operação responsável por incluir no banco de dados um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Primeiramente valida
	 * os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o
	 * banco de dados e a permissão do usuário para realizar esta operacão na
	 * entidade. Isto, através da operação <code>incluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            gravado no banco de dados.
	 * @exception Exception
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConciliacaoContaCorrenteDiaExtratoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ConciliacaoContaCorrenteDiaExtrato.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ConciliacaoContaCorrenteDiaExtrato (conciliacaoContaCorrenteDia, tipoTransacaoOFXEnum , codigoOfx, dataOfx, valorOfx , lancamentoOfx, ");
			sql.append("    documentoOfx, saldoRegistroOfx , codigoSei, valorSei, dataSei, lancamentoSei, ");// documentoSei,
			sql.append("    tipoMovimentacaoFinanceiraSei, saldoRegistroSei) ");
			sql.append("    VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setInt(++i, obj.getConciliacaoContaCorrenteDia().getCodigo());
					if (Uteis.isAtributoPreenchido(obj.getTipoTransacaoOFXEnum())) {
						sqlInserir.setString(++i, obj.getTipoTransacaoOFXEnum().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCodigoOfx())) {
						sqlInserir.setInt(++i, obj.getCodigoOfx());
					} else {
						sqlInserir.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataOfx())) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataOfx()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setDouble(++i, obj.getValorOfx());
					sqlInserir.setString(++i, obj.getLancamentoOfx());
					sqlInserir.setString(++i, obj.getDocumentoOfx());
					sqlInserir.setDouble(++i, obj.getSaldoRegistroOfx());
					sqlInserir.setString(++i, obj.getCodigoSei());
					sqlInserir.setDouble(++i, obj.getValorSei());
					if (Uteis.isAtributoPreenchido(obj.getDataSei())) {
						sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataSei()));
					} else {
						sqlInserir.setNull(++i, 0);
					}
					sqlInserir.setString(++i, obj.getLancamentoSei());
					if (Uteis.isAtributoPreenchido(obj.getTipoMovimentacaoFinanceiraSei())) {
						sqlInserir.setString(++i, obj.getTipoMovimentacaoFinanceiraSei().name());
					} else {
						sqlInserir.setNull(++i, 0);
					}

					sqlInserir.setDouble(++i, obj.getSaldoRegistroSei());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException  {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	/**
	 * Operação responsável por alterar no BD os dados de um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre utiliza a
	 * chave primária da classe como atributo para localização do registro a ser
	 * alterado. Primeiramente valida os dados (<code>validarDados</code>) do
	 * objeto. Verifica a conexão com o banco de dados e a permissão do usuário
	 * para realizar esta operacão na entidade. Isto, através da operação
	 * <code>alterar</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            alterada no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão, restrição de acesso ou
	 *                validação de dados.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConciliacaoContaCorrenteDiaExtratoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ConciliacaoContaCorrenteDiaExtrato.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE ConciliacaoContaCorrenteDiaExtrato ");
			sql.append("   SET conciliacaoContaCorrenteDia=?, tipoTransacaoOFXEnum=? , codigoOfx=?, dataOfx=?, valorOfx=? , lancamentoOfx=?, ");
			sql.append("    documentoOfx=?, saldoRegistroOfx=? , codigoSei=?, valorSei=?, dataSei=? , lancamentoSei=?, "); //documentoSei=?,
			sql.append("    tipoMovimentacaoFinanceiraSei=?, saldoRegistroSei=? ");
			sql.append("       WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					sqlAlterar.setInt(++i, obj.getConciliacaoContaCorrenteDia().getCodigo());
					if (Uteis.isAtributoPreenchido(obj.getTipoTransacaoOFXEnum())) {
						sqlAlterar.setString(++i, obj.getTipoTransacaoOFXEnum().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCodigoOfx())) {
						sqlAlterar.setInt(++i, obj.getCodigoOfx());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getDataOfx())) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataOfx()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setDouble(++i, obj.getValorOfx());
					sqlAlterar.setString(++i, obj.getLancamentoOfx());
					sqlAlterar.setString(++i, obj.getDocumentoOfx());
					sqlAlterar.setDouble(++i, obj.getSaldoRegistroOfx());
					sqlAlterar.setString(++i, obj.getCodigoSei());
					sqlAlterar.setDouble(++i, obj.getValorSei());
					if (Uteis.isAtributoPreenchido(obj.getDataSei())) {
						sqlAlterar.setDate(++i, Uteis.getDataJDBC(obj.getDataSei()));
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getLancamentoSei());
					if (Uteis.isAtributoPreenchido(obj.getTipoMovimentacaoFinanceiraSei())) {
						sqlAlterar.setString(++i, obj.getTipoMovimentacaoFinanceiraSei().name());
					} else {
						sqlAlterar.setNull(++i, 0);
					}

					sqlAlterar.setDouble(++i, obj.getSaldoRegistroSei());
					sqlAlterar.setInt(++i, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Operação responsável por excluir no BD um objeto da classe
	 * <code>ProcessamentoArquivoRetornoParceiroVO</code>. Sempre localiza o
	 * registro a ser excluído através da chave primária da entidade.
	 * Primeiramente verifica a conexão com o banco de dados e a permissão do
	 * usuário para realizar esta operacão na entidade. Isto, através da
	 * operação <code>excluir</code> da superclasse.
	 * 
	 * @param obj
	 *            Objeto da classe
	 *            <code>ProcessamentoArquivoRetornoParceiroVO</code> que será
	 *            removido no banco de dados.
	 * @exception Execption
	 *                Caso haja problemas de conexão ou restrição de acesso.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConciliacaoContaCorrenteDiaExtratoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			getFacadeFactory().getExtratoContaCorrenteFacade().anularConciliacaoContaCorrenteDiaExtratoPorConciliacaoExtratoDia(obj.getCodigo().toString(), usuario);
			String sql = "DELETE FROM ConciliacaoContaCorrenteDiaExtrato WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarConciliacaoContaCorrenteDiaExtratoCampoCodigoSei(Integer codigoConciliacaoDiaExtrato, UsuarioVO usuario) throws Exception {
		Integer codigoConciliacao = getFacadeFactory().getConciliacaoContaCorrenteFacade().validarSeExisteConciliacaoContaCorrenteParaEstorno(codigoConciliacaoDiaExtrato, usuario);
		Uteis.checkState(codigoConciliacao > 0, "Não foi possível realizar essa operação pois, existe um vinculo com a conciliação conta corrente de código "+codigoConciliacao+".");		
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" UPDATE conciliacaocontacorrentediaextrato  SET ");
		sqlStr.append(" codigosei = t.listaCodigo ,  ");
		sqlStr.append(" valorsei = (case when tipomovimentacaofinanceirasei = 'SAIDA' then  (t.totalSei * -1) else t.totalSei end) ");
		sqlStr.append(" from ( ");
		sqlStr.append(" select array_to_string(array_agg(codigo), ',') as listaCodigo, ") ;
		sqlStr.append(" SUM(valor) as totalSei  ") ;
		sqlStr.append(" from extratocontacorrente where conciliacaocontacorrentediaextrato  = ").append(codigoConciliacaoDiaExtrato).append("  ");
		sqlStr.append(" and desconsiderarConciliacaoBancaria  =  false ");
		sqlStr.append(" ) as t WHERE codigo = ").append(codigoConciliacaoDiaExtrato).append("  ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
		atualizarConciliacaoContaCorrenteDiaExtratoCampoLancamentoSei(codigoConciliacaoDiaExtrato, usuario);
		deletarConciliacaoContaCorrenteDiaExtratoEstorno(codigoConciliacaoDiaExtrato, usuario);
	}
	
	
	private void atualizarConciliacaoContaCorrenteDiaExtratoCampoLancamentoSei(Integer codigoConciliacaoDiaExtrato, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" UPDATE conciliacaocontacorrentediaextrato  SET ");
		sqlStr.append(" lancamentoSei = tt.listaLancamentoSei   ");
		sqlStr.append(" from ( ");
		sqlStr.append(" select array_to_string(array_agg(nomeFormaPagamento), ',') as listaLancamentoSei from ( ") ;
		sqlStr.append(" select distinct case when formapagamento.tipo = 'CA' then operadoracartao.nome || ' - Cartão de Crédito'  ") ;
		sqlStr.append(" when formapagamento.tipo = 'CD' then operadoracartao.nome || ' - Cartão de Débito'  ") ;
		sqlStr.append(" when formapagamento.tipo = 'DE' then 'Crédito/Débito em Conta Corrente'  ") ;
		sqlStr.append(" when formapagamento.tipo = 'DI' then 'Dinheiro'  ") ;
		sqlStr.append(" when formapagamento.tipo = 'CH' then 'Cheque'  ") ;
		sqlStr.append(" when formapagamento.tipo = 'DC' then 'Depósito'  ") ;
		sqlStr.append(" when formapagamento.tipo = 'BO' then 'Boleto Bancário'  ") ;
		sqlStr.append(" when formapagamento.tipo = 'IS' then 'Isenção'  ") ;
		sqlStr.append(" when formapagamento.tipo = 'PE' then 'Permuta'  ") ;
		sqlStr.append(" else '' end as nomeFormaPagamento  ") ;			
		sqlStr.append(" from extratocontacorrente  ");
		sqlStr.append(" inner join formapagamento on formapagamento.codigo = extratocontacorrente.formapagamento ") ;
		sqlStr.append(" left join operadoracartao on operadoracartao.codigo = extratocontacorrente.operadoracartao ") ;
		sqlStr.append(" where conciliacaocontacorrentediaextrato  = ").append(codigoConciliacaoDiaExtrato).append("  ");
		sqlStr.append(" and desconsiderarConciliacaoBancaria  =  false ");
		sqlStr.append(" ) as t ) as tt WHERE codigo = ").append(codigoConciliacaoDiaExtrato).append("  ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void deletarConciliacaoContaCorrenteDiaExtratoEstorno(Integer codigoConciliacaoDiaExtrato, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" delete from conciliacaocontacorrentediaextrato where codigo in ( ");
		sqlStr.append(" select case when ((codigoofx = 0 OR codigoofx IS NULL)  and (codigosei = '' OR codigosei is null)) then codigo else 0 end from conciliacaocontacorrentediaextrato where codigo =  ").append(codigoConciliacaoDiaExtrato);
		sqlStr.append(" )");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}	

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ConciliacaoContaCorrenteDiaExtrato.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ConciliacaoContaCorrenteDiaExtrato.idEntidade = idEntidade;
	}

}
