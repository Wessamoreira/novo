package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.academico.ConfiguracaoAcademicaHistoricoVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ConfiguracaoAcademicaHistoricoInterface;

@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoAcademicaHistorico extends ControleAcesso implements ConfiguracaoAcademicaHistoricoInterface {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public ConfiguracaoAcademicaHistorico() throws Exception {
		super();
		setIdEntidade("ConfiguracaoAcademicaHistorico");
	}

	public ConfiguracaoAcademicaHistorico novo() throws Exception {
		ConfiguracaoAcademicaHistorico.incluir(getIdEntidade());
		ConfiguracaoAcademicaHistorico obj = new ConfiguracaoAcademicaHistorico();
		return obj;
	}

	/*
	 * Metodo que consulta no historico os alunos de acordo com os paramestros passado. 
	 * Percorre a lista setando em cada objeto historico um objeto da lista "historicoVOs".
	 * E já informa a configuração antiga do historico.
	 * @see negocio.interfaces.academico.ConfiguracaoAcademicaHistoricoInterface#consultaRapidaAlterarConfiguracaoAcadHistorico(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public List<ConfiguracaoAcademicaHistoricoVO> consultaRapidaAlterarConfiguracaoAcadHistorico(String matricula, Integer unidadeEnsino, Integer curso, Integer turma, Integer disciplina, Integer configuracaoAcademico, String ano, String semestre, UsuarioVO usuario) throws Exception {
		List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultaRapidaAlterarConfiguracaoAcadHistorico(matricula, unidadeEnsino, curso, turma, disciplina, configuracaoAcademico, ano, semestre, usuario);
		List<ConfiguracaoAcademicaHistoricoVO> alterarConfiguracaoAcadHistoricoVOs = new ArrayList<ConfiguracaoAcademicaHistoricoVO>(0);
		ConfiguracaoAcademicaHistoricoVO obj = null;
		for (HistoricoVO historicoVO : historicoVOs) {
			obj = new ConfiguracaoAcademicaHistoricoVO();
			obj.setHistoricoVO(historicoVO);
			obj.getConfiguracaoAntiga().setCodigo(historicoVO.getConfiguracaoAcademico().getCodigo());
			obj.getConfiguracaoAntiga().setNome(historicoVO.getConfiguracaoAcademico().getNome());
			alterarConfiguracaoAcadHistoricoVOs.add(obj);
		}
		return alterarConfiguracaoAcadHistoricoVOs;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoAcademicaHistoricoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			if (obj.getNovoObj()) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ConfiguracaoAcademicaHistoricoVO obj, boolean verificarAcesso, final UsuarioVO usuarioVO) throws Exception {
		try {
			ConfiguracaoAcademicaHistorico.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO configuracaoacademicahistorico(dataalteracao, usuario, historico, configuracaoantiga, configuracaoatualizada) VALUES (?, ? , ? , ? , ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					try {
						sqlInserir.setDate(1, Uteis.getDataJDBC(obj.getDataAlteracao()));
						sqlInserir.setInt(2, obj.getUsuarioVO().getCodigo());
						sqlInserir.setInt(3, obj.getHistoricoVO().getCodigo());
						sqlInserir.setInt(4, obj.getConfiguracaoAntiga().getCodigo());
						sqlInserir.setInt(5, obj.getConfiguracaoAtualizada().getCodigo());
					} catch (final Exception x) {
						return null;
					}
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

		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ConfiguracaoAcademicaHistoricoVO obj, boolean verificarAcesso, final UsuarioVO usuarioVO) throws Exception {
		try {
			ConfiguracaoAcademicaHistorico.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE configuracaoacademicahistorico SET  dataalteracao=?, usuario=?, historico=?, configuracaoantiga=?, configuracaoatualizada=? (codigo = " + obj.getCodigo() + ")" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getDataAlteracao()));
					sqlAlterar.setInt(2, obj.getUsuarioVO().getCodigo());
					sqlAlterar.setInt(3, obj.getHistoricoVO().getCodigo());
					sqlAlterar.setInt(4, obj.getConfiguracaoAntiga().getCodigo());
					sqlAlterar.setInt(5, obj.getConfiguracaoAtualizada().getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(ConfiguracaoAcademicaHistoricoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ConfiguracaoAcademicaHistorico.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM configuracaoacademicahistorico WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			getConexao().getJdbcTemplate().update(sql.toString(), obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	public List<ConfiguracaoAcademicaHistoricoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<ConfiguracaoAcademicaHistoricoVO> alterarConfiguracaoAcadHistoricoVOs = new ArrayList<ConfiguracaoAcademicaHistoricoVO>(0);
		while (tabelaResultado.next()) {
			alterarConfiguracaoAcadHistoricoVOs.add(montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
		}
		return alterarConfiguracaoAcadHistoricoVOs;
	}

	public ConfiguracaoAcademicaHistoricoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoAcademicaHistoricoVO obj = new ConfiguracaoAcademicaHistoricoVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDataAlteracao(tabelaResultado.getDate("dataalteracao"));
		obj.getUsuarioVO().setCodigo(tabelaResultado.getInt("usuario"));
		obj.getHistoricoVO().setCodigo(tabelaResultado.getInt("historico"));
		obj.getConfiguracaoAntiga().setCodigo(tabelaResultado.getInt("configuracaoantiga"));
		obj.getConfiguracaoAtualizada().setCodigo(tabelaResultado.getInt("configuracaoatualizada"));
		return obj;
	}

	@Override
	public ConfiguracaoAcademicaHistoricoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM configuracaoacademicahistorico WHERE codigo = ").append(codigo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, nivelMontarDados, usuarioVO);
		}
		return new ConfiguracaoAcademicaHistoricoVO();
	}

	public void validarDadosConfiguracaoAcadHistorico(ConfiguracaoAcademicaHistoricoVO configuracaoAcademicaHistoricoVO) throws Exception {
		if (configuracaoAcademicaHistoricoVO.getConfiguracaoAtualizada().getCodigo() == 0 || configuracaoAcademicaHistoricoVO.getConfiguracaoAtualizada().getCodigo() == null) {
			throw new Exception(UteisJSF.internacionalizar("Informe a Configuracao Academico a ser modificada."));
		}
		if (configuracaoAcademicaHistoricoVO.getSelecionarConfiguracaoAcademicaHistorico() == false) {
			throw new Exception(UteisJSF.internacionalizar("Não existe nenhuma selecão feita para a alteração."));
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarConfiguracaoAcademicaHistoricoPorMatricula(ConfiguracaoAcademicaHistoricoVO configuracaoAcademicaHistoricoVO, UsuarioVO usuarioVO) throws Exception {
		try {
			if (!configuracaoAcademicaHistoricoVO.getConfiguracaoAntiga().getCodigo().equals(configuracaoAcademicaHistoricoVO.getConfiguracaoAtualizada().getCodigo())) {
				Date dataAtual = new Date();
				validarDadosConfiguracaoAcadHistorico(configuracaoAcademicaHistoricoVO);
				getFacadeFactory().getHistoricoFacade().alterarConfiguracaoAcademicaHistorico(configuracaoAcademicaHistoricoVO.getHistoricoVO(), configuracaoAcademicaHistoricoVO.getConfiguracaoAtualizada().getCodigo(), usuarioVO);
				configuracaoAcademicaHistoricoVO.setDataAlteracao(dataAtual);
				incluir(configuracaoAcademicaHistoricoVO, true, usuarioVO);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarConfiguracaoAcademicaHistoricoVOs(List<ConfiguracaoAcademicaHistoricoVO> ConfiguracaoAcademicaHistoricoVOs, UsuarioVO usuarioVO) throws Exception {
		try {
			for (ConfiguracaoAcademicaHistoricoVO obj : ConfiguracaoAcademicaHistoricoVOs) {
				if (obj.getSelecionarConfiguracaoAcademicaHistorico() == true) {
					alterarConfiguracaoAcademicaHistoricoPorMatricula(obj, usuarioVO);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ConfiguracaoAcademicaHistorico.idEntidade = idEntidade;
	}

}
