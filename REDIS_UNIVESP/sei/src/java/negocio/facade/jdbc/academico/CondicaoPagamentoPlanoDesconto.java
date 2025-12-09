package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.CondicaoPagamentoPlanoDescontoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CondicaoPagamentoPlanoDescontoInterfaceFacade;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class CondicaoPagamentoPlanoDesconto extends ControleAcesso implements CondicaoPagamentoPlanoDescontoInterfaceFacade {
	
	protected static String idEntidade;

	public CondicaoPagamentoPlanoDesconto() throws Exception {
		super();
		setIdEntidade("CondicaoPagamentoPlanoDesconto");
	}

	/**
	 * Operação responsável por retornar um novo objeto da classe
	 * <code>PlanoDescontoPadraoVO</code>.
	 */
	public CondicaoPagamentoPlanoDescontoVO novo() throws Exception {
		CondicaoPagamentoPlanoDesconto.incluir(getIdEntidade());
		CondicaoPagamentoPlanoDescontoVO obj = new CondicaoPagamentoPlanoDescontoVO();
		return obj;
	}

	@Override
	public void incluir(final CondicaoPagamentoPlanoDescontoVO obj, UsuarioVO usuario) throws Exception {
		try {
			final StringBuilder sqlStr = new StringBuilder("");
			sqlStr.append(" INSERT INTO condicaoPagamentoPlanoDesconto (condicaopagamentoplanofinanceirocurso, planodesconto) VALUES (?, ?) ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sqlStr.toString());
					sqlInserir.setInt(1, obj.getCondicaoPagamentoPlanoFinanceiroCurso());
					sqlInserir.setInt(2, obj.getPlanoDescontoVO().getCodigo());
					return sqlInserir;
				}
			});
			
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void alterar(final CondicaoPagamentoPlanoDescontoVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE condicaoPagamentoPlanoDesconto SET condicaopagamentoplanofinanceirocurso=?, planodesconto=? WHERE codigo = ? "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getCondicaoPagamentoPlanoFinanceiroCurso());
					sqlAlterar.setInt(2, obj.getPlanoDescontoVO().getCodigo());
					sqlAlterar.setInt(3, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void excluir(CondicaoPagamentoPlanoDescontoVO obj, UsuarioVO usuario) throws Exception {
		try {
			CondicaoPagamentoPlanoDesconto.excluir(getIdEntidade());
			String sql = "DELETE FROM condicaoPagamentoPlanoDesconto WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<CondicaoPagamentoPlanoDescontoVO> consultarPorCondicaoPagamentoPlanoFinanceiroCurso(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		String sqlStr = "SELECT * FROM condicaoPagamentoPlanoDesconto WHERE condicaopagamentoplanofinanceirocurso = " + valorConsulta;
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, usuario));
	}
	
	public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			CondicaoPagamentoPlanoDescontoVO obj = new CondicaoPagamentoPlanoDescontoVO();
			obj = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}
	
	public static CondicaoPagamentoPlanoDescontoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CondicaoPagamentoPlanoDescontoVO obj = new CondicaoPagamentoPlanoDescontoVO();
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setCondicaoPagamentoPlanoFinanceiroCurso(dadosSQL.getInt("condicaopagamentoplanofinanceirocurso"));
		obj.setPlanoDescontoVO(getFacadeFactory().getPlanoDescontoFacade().consultarPorChavePrimaria(dadosSQL.getInt("planodesconto"), nivelMontarDados, usuario));
		obj.setNovoObj(Boolean.FALSE);
		return obj;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorCondicaoPagamento(Integer condicaoPagamento, List<CondicaoPagamentoPlanoDescontoVO> planoDescontoPadraoVOs, UsuarioVO usuario) throws Exception {
		String sqlStr = "DELETE FROM condicaoPagamentoPlanoDesconto WHERE ((condicaoPagamentoPlanoFinanceiroCurso = ?)) ";
		if (planoDescontoPadraoVOs != null && !planoDescontoPadraoVOs.isEmpty()) {
			for (CondicaoPagamentoPlanoDescontoVO planoDescontoPadrao : planoDescontoPadraoVOs) {
				sqlStr += " AND codigo != " + planoDescontoPadrao.getCodigo().intValue();
			}
		}
		getConexao().getJdbcTemplate().update(sqlStr, new Object[] { condicaoPagamento });
	}
	
	public void alterarPlanoDescontoPadraoCondicaoPagamentoVOs(Integer condicaoPagamento, List<CondicaoPagamentoPlanoDescontoVO> condicaoPagamentoPlanoDescontoVOs, UsuarioVO usuarioVO) throws Exception {
		excluirPorCondicaoPagamento(condicaoPagamento, condicaoPagamentoPlanoDescontoVOs, usuarioVO);
		for (CondicaoPagamentoPlanoDescontoVO condicaoPagamentoPlanoDescontoVO : condicaoPagamentoPlanoDescontoVOs) {
			condicaoPagamentoPlanoDescontoVO.setCondicaoPagamentoPlanoFinanceiroCurso(condicaoPagamento);
			if (condicaoPagamentoPlanoDescontoVO.getCodigo().equals(0)) {
				incluir(condicaoPagamentoPlanoDescontoVO, usuarioVO);
			} else {
				alterar(condicaoPagamentoPlanoDescontoVO, usuarioVO);
			}
		}
	}

	
	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return CondicaoPagamentoPlanoDesconto.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		CondicaoPagamentoPlanoDesconto.idEntidade = idEntidade;
	}

}
