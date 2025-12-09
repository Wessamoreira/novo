package negocio.facade.jdbc.contabil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import negocio.comuns.contabil.ConfiguracaoContabilRegraPlanoContaVO;
import negocio.comuns.contabil.ConfiguracaoContabilRegraVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.contabil.ConfiguracaoContabilRegraPlanoContaInterfaceFacade;


/**
 * 
 * @author PedroOtimize
 *
 */

@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoContabilRegraPlanoConta extends ControleAcesso implements ConfiguracaoContabilRegraPlanoContaInterfaceFacade{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3454891236902644513L;
	protected static String idEntidade;

	public ConfiguracaoContabilRegraPlanoConta() throws Exception {
		super();
		setIdEntidade("ConfiguracaoContabilRegraPlanoConta");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<ConfiguracaoContabilRegraPlanoContaVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (ConfiguracaoContabilRegraPlanoContaVO obj : lista) {
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
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
	public void incluir(final ConfiguracaoContabilRegraPlanoContaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ConfiguracaoContabilRegraPlanoConta.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO ConfiguracaoContabilRegraPlanoConta (ConfiguracaoContabilRegra, planoContaCredito, planoContaDebito  ) ");
			sql.append("    VALUES ( ?, ?, ?) ");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					sqlInserir.setInt(++i, obj.getConfiguracaoContabilRegraVO().getCodigo());
					sqlInserir.setInt(++i, obj.getPlanoContaCreditoVO().getCodigo());
					sqlInserir.setInt(++i, obj.getPlanoContaDebitoVO().getCodigo());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
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
	public void alterar(final ConfiguracaoContabilRegraPlanoContaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ConfiguracaoContabilRegraPlanoConta.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE ConfiguracaoContabilRegraPlanoConta SET ConfiguracaoContabilRegra=?, planoContaCredito=?, planoContaDebito=?");
			sql.append("       WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					sqlAlterar.setInt(++i, obj.getConfiguracaoContabilRegraVO().getCodigo());
					sqlAlterar.setInt(++i, obj.getPlanoContaCreditoVO().getCodigo());
					sqlAlterar.setInt(++i, obj.getPlanoContaDebitoVO().getCodigo());
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
	public void excluir(ConfiguracaoContabilRegraPlanoContaVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		try {
			ConfiguracaoContabilRegraPlanoConta.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM ConfiguracaoContabilRegraPlanoConta WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void montarDadosBasicoConfiguracaoContabilRegraPlanoConta(SqlRowSet dadosSQL, ConfiguracaoContabilRegraVO ccr) throws Exception {
		ConfiguracaoContabilRegraPlanoContaVO ccrpc = new ConfiguracaoContabilRegraPlanoContaVO();
		ccrpc.setNovoObj(Boolean.FALSE);
		ccrpc.setCodigo(dadosSQL.getInt("ccrpc.codigo"));
		
		ccrpc.getPlanoContaCreditoVO().setCodigo(dadosSQL.getInt("pcc.codigo"));
		ccrpc.getPlanoContaCreditoVO().setIdentificadorPlanoConta(dadosSQL.getString("pcc.identificadorplanoconta"));
		ccrpc.getPlanoContaCreditoVO().setDescricao(dadosSQL.getString("pcc.descricao"));
		

		ccrpc.getPlanoContaDebitoVO().setCodigo(dadosSQL.getInt("pcd.codigo"));
		ccrpc.getPlanoContaDebitoVO().setIdentificadorPlanoConta(dadosSQL.getString("pcd.identificadorplanoconta"));
		ccrpc.getPlanoContaDebitoVO().setDescricao(dadosSQL.getString("pcd.descricao"));
		
		ccrpc.setConfiguracaoContabilRegraVO(ccr);
		ccr.getListaConfiguracaoContabilRegraPlanoContaVO().add(ccrpc);
		
	
	}	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as
	 * operações desta classe.
	 */
	public static String getIdEntidade() {
		return ConfiguracaoContabilRegraPlanoConta.idEntidade;
	}

	/**
	 * Operação reponsável por definir um novo valor para o identificador desta
	 * classe. Esta alteração deve ser possível, pois, uma mesma classe de
	 * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
	 * que Como o controle de acesso é realizado com base neste identificador,
	 */
	public void setIdEntidade(String idEntidade) {
		ConfiguracaoContabilRegraPlanoConta.idEntidade = idEntidade;
	}

}
