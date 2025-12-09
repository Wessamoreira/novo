package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.UnidadeEnsinoContaCorrenteVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.UnidadeEnsinoContaCorrenteInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class UnidadeEnsinoContaCorrente extends ControleAcesso implements UnidadeEnsinoContaCorrenteInterfaceFacade {

	private static final long serialVersionUID = 2322866999276121363L;

	protected static String idEntidade;

	public UnidadeEnsinoContaCorrente() throws Exception {
		super();
		setIdEntidade("ContaCorrente");
	}

	public UnidadeEnsinoContaCorrenteVO novo() throws Exception {
		UnidadeEnsinoContaCorrente.incluir(getIdEntidade());
		UnidadeEnsinoContaCorrenteVO obj = new UnidadeEnsinoContaCorrenteVO();
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluir(final UnidadeEnsinoContaCorrenteVO obj) throws Exception {

		final String sql = "INSERT INTO UnidadeEnsinoContaCorrente( unidadeEnsino, contaCorrente, utilizarRemessa, utilizarNegociacao, usarPorDefaultMovimentacaoFinanceira ) VALUES ( ?, ?, ?, ?, ? ) ";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getUnidadeEnsino().getCodigo());
				sqlInserir.setDouble(2, obj.getContaCorrente());
				sqlInserir.setBoolean(3, obj.getUtilizarRemessa().booleanValue());
				sqlInserir.setBoolean(4, obj.getUtilizarNegociacao().booleanValue());
				sqlInserir.setBoolean(5, obj.getUsarPorDefaultMovimentacaoFinanceira().booleanValue());
				return sqlInserir;
			}
		});
		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final UnidadeEnsinoContaCorrenteVO obj) throws Exception {
		final String sql = "UPDATE UnidadeEnsinoContaCorrente set utilizarRemessa=?, utilizarNegociacao=?,  usarPorDefaultMovimentacaoFinanceira=? where unidadeEnsino=? and contaCorrente=?";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
				PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
				sqlAlterar.setBoolean(1, obj.getUtilizarRemessa().booleanValue());
				sqlAlterar.setBoolean(2, obj.getUtilizarNegociacao().booleanValue());
				sqlAlterar.setBoolean(3, obj.getUsarPorDefaultMovimentacaoFinanceira().booleanValue());
				sqlAlterar.setInt(4, obj.getUnidadeEnsino().getCodigo());
				sqlAlterar.setDouble(5, obj.getContaCorrente());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(UnidadeEnsinoContaCorrenteVO obj) throws Exception {
		UnidadeEnsinoContaCorrente.excluir(getIdEntidade());
		String sql = "DELETE FROM UnidadeEnsinoContaCorrente WHERE unidadeEnsino = ? AND contacorrente = ?";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getUnidadeEnsino().getCodigo(), obj.getContaCorrente() });
	}

	public List<UnidadeEnsinoContaCorrenteVO> consultarPorUnidadeEnsino(Integer codigoUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT uecc.*, ue.nome as nomeunidadeensino FROM UnidadeEnsinoContaCorrente uecc " +
		"INNER JOIN unidadeensino ue ON uecc.unidadeensino = ue.codigo " +
		"WHERE WHERE unidadeEnsino = " + codigoUnidadeEnsino;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<UnidadeEnsinoContaCorrenteVO> consultarPorContaCorrente(Integer codigoContaCorrente, boolean controlarAcesso, int nivelMontarDados,
			UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT uecc.*, ue.nome as nomeunidadeensino FROM UnidadeEnsinoContaCorrente uecc " +
				"INNER JOIN unidadeensino ue ON uecc.unidadeensino = ue.codigo " +
				"WHERE contaCorrente = " + codigoContaCorrente;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public static List<UnidadeEnsinoContaCorrenteVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		List<UnidadeEnsinoContaCorrenteVO> vetResultado = new ArrayList<UnidadeEnsinoContaCorrenteVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		tabelaResultado = null;
		return vetResultado;
	}

	public static UnidadeEnsinoContaCorrenteVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		UnidadeEnsinoContaCorrenteVO obj = new UnidadeEnsinoContaCorrenteVO();
		obj.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.getUnidadeEnsino().setNome(dadosSQL.getString("nomeUnidadeEnsino"));
		obj.setContaCorrente(dadosSQL.getInt("contaCorrente"));
		obj.setUtilizarRemessa(dadosSQL.getBoolean("utilizarRemessa"));
		obj.setUtilizarNegociacao(dadosSQL.getBoolean("utilizarNegociacao"));
		obj.setUsarPorDefaultMovimentacaoFinanceira(dadosSQL.getBoolean("usarPorDefaultMovimentacaoFinanceira"));
		obj.setNovoObj(Boolean.FALSE);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirUnidadeEnsinoContaCorrentes(List<UnidadeEnsinoContaCorrenteVO> unidadeEnsinoContaCorrenteVOs) throws Exception {
		for (UnidadeEnsinoContaCorrenteVO unidadeEnsinoContaCorrenteVO : unidadeEnsinoContaCorrenteVOs) {
			excluir(unidadeEnsinoContaCorrenteVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarUnidadeEnsinoContaCorrentes(Integer codigoContaCorrente, List<UnidadeEnsinoContaCorrenteVO> unidadeEnsinoContaCorrenteVOs)
			throws Exception {
		String str = "DELETE FROM UnidadeEnsinoContaCorrente WHERE contaCorrente = " + codigoContaCorrente;
		for (UnidadeEnsinoContaCorrenteVO unidadeEnsinoContaCorrenteVO : unidadeEnsinoContaCorrenteVOs) {
			str += " AND unidadeensino <> " + unidadeEnsinoContaCorrenteVO.getUnidadeEnsino().getCodigo();
		}
		getConexao().getJdbcTemplate().update(str);
		for (UnidadeEnsinoContaCorrenteVO unidadeEnsinoContaCorrenteVO : unidadeEnsinoContaCorrenteVOs) {
			if (unidadeEnsinoContaCorrenteVO.getContaCorrente() == null || unidadeEnsinoContaCorrenteVO.getContaCorrente() == 0) {
				unidadeEnsinoContaCorrenteVO.setContaCorrente(codigoContaCorrente);
				incluir(unidadeEnsinoContaCorrenteVO);
			} else {
				alterar(unidadeEnsinoContaCorrenteVO);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
	public void incluirUnidadeEnsinoContaCorrentes(Integer codigoContaCorrente, List<UnidadeEnsinoContaCorrenteVO> unidadeEnsinoContaCorrenteVOs)
			throws Exception {
		for (UnidadeEnsinoContaCorrenteVO unidadeEnsinoContaCorrenteVO : unidadeEnsinoContaCorrenteVOs) {
			unidadeEnsinoContaCorrenteVO.setContaCorrente(codigoContaCorrente);
			incluir(unidadeEnsinoContaCorrenteVO);
		}
	}
	
	@Override
	public void adicionarTodasUnidadesEnsinoContaCorrente(ContaCorrenteVO contaCorrenteVO, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
       List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>(0);
       unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", unidadeEnsino, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioVO);
       for(UnidadeEnsinoContaCorrenteVO unidadeEnsinoContaCorrente : contaCorrenteVO.getUnidadeEnsinoContaCorrenteVOs()) {
    	   for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
    		   if(unidadeEnsinoVO.getCodigo().equals(unidadeEnsinoContaCorrente.getUnidadeEnsino().getCodigo())) {
    			   unidadeEnsinoVOs.remove(unidadeEnsinoVO);
    			   break;
        		  }
        	  }
         }
          if(Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
        	  UnidadeEnsinoContaCorrenteVO unidadeEnsinoContaCorrenteVO = new UnidadeEnsinoContaCorrenteVO();
              for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
                  unidadeEnsinoContaCorrenteVO.setUnidadeEnsino(unidadeEnsinoVO);
                  contaCorrenteVO.getUnidadeEnsinoContaCorrenteVOs().add(unidadeEnsinoContaCorrenteVO);
                  unidadeEnsinoContaCorrenteVO = new UnidadeEnsinoContaCorrenteVO();
              }
          }
	}

	public static String getIdEntidade() {
		return UnidadeEnsinoContaCorrente.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		UnidadeEnsinoContaCorrente.idEntidade = idEntidade;
	}
}