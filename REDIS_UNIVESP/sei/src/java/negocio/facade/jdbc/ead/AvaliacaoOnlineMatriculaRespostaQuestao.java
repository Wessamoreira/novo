package negocio.facade.jdbc.ead;

import java.io.Serializable;
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
import negocio.comuns.ead.AvaliacaoOnlineMatriculaQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaRespostaQuestaoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.AvaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade;

/**
 * @author Victor Hugo 10/10/2014
 */
@Repository
@Scope("singleton")
@Lazy
public class AvaliacaoOnlineMatriculaRespostaQuestao extends ControleAcesso implements AvaliacaoOnlineMatriculaRespostaQuestaoInterfaceFacade, Serializable {

	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		AvaliacaoOnlineMatriculaRespostaQuestao.idEntidade = idEntidade;
	}

	public AvaliacaoOnlineMatriculaRespostaQuestao() throws Exception {
		super();
		setIdEntidade("AvaliacaoOnlineMatriculaRespostaQuestao");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final AvaliacaoOnlineMatriculaRespostaQuestaoVO avaliacaoOnlineMatriculaRespostaQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(avaliacaoOnlineMatriculaRespostaQuestaoVO);
			AvaliacaoOnlineMatriculaRespostaQuestao.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO avaliacaoonlinematricularespostaquestao(" + " avaliacaoonlinematriculaquestao, opcaorespostaquestao, marcada)" + " VALUES (?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			avaliacaoOnlineMatriculaRespostaQuestaoVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);

					sqlInserir.setInt(1, avaliacaoOnlineMatriculaRespostaQuestaoVO.getAvaliacaoOnlineMatriculaQuestaoVO().getCodigo());
					sqlInserir.setInt(2, avaliacaoOnlineMatriculaRespostaQuestaoVO.getOpcaoRespostaQuestaoVO().getCodigo());
					sqlInserir.setBoolean(3, avaliacaoOnlineMatriculaRespostaQuestaoVO.getMarcada());

					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						avaliacaoOnlineMatriculaRespostaQuestaoVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			avaliacaoOnlineMatriculaRespostaQuestaoVO.setNovoObj(Boolean.TRUE);
			avaliacaoOnlineMatriculaRespostaQuestaoVO.setCodigo(0);
			throw e;
		}
	}

	@Override
	public void validarDados(AvaliacaoOnlineMatriculaRespostaQuestaoVO avaliacaoOnlineMatriculaRespostaQuestaoVO) throws Exception {

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(AvaliacaoOnlineMatriculaRespostaQuestaoVO avaliacaoOnlineMatriculaRespostaQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (avaliacaoOnlineMatriculaRespostaQuestaoVO.getCodigo() == 0) {
			incluir(avaliacaoOnlineMatriculaRespostaQuestaoVO, verificarAcesso, usuarioVO);
		} else {
			alterar(avaliacaoOnlineMatriculaRespostaQuestaoVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final AvaliacaoOnlineMatriculaRespostaQuestaoVO avaliacaoOnlineMatriculaRespostaQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			AvaliacaoOnlineMatriculaRespostaQuestao.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE avaliacaoonlinematricularespostaquestao" + " SET avaliacaoonlinematriculaquestao=?, opcaorespostaquestao=?, marcada = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);

					sqlAlterar.setInt(1, avaliacaoOnlineMatriculaRespostaQuestaoVO.getAvaliacaoOnlineMatriculaQuestaoVO().getCodigo());
					sqlAlterar.setInt(2, avaliacaoOnlineMatriculaRespostaQuestaoVO.getOpcaoRespostaQuestaoVO().getCodigo());
					sqlAlterar.setBoolean(3, avaliacaoOnlineMatriculaRespostaQuestaoVO.getMarcada());
					sqlAlterar.setInt(4, avaliacaoOnlineMatriculaRespostaQuestaoVO.getCodigo());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final AvaliacaoOnlineMatriculaRespostaQuestaoVO avaliacaoOnlineMatriculaRespostaQuestaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			AvaliacaoOnlineMatriculaRespostaQuestao.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM avaliacaoonlinematricularespostaquestao WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, avaliacaoOnlineMatriculaRespostaQuestaoVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public AvaliacaoOnlineMatriculaRespostaQuestaoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		AvaliacaoOnlineMatriculaRespostaQuestaoVO obj = new AvaliacaoOnlineMatriculaRespostaQuestaoVO();

		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getAvaliacaoOnlineMatriculaQuestaoVO().setCodigo(tabelaResultado.getInt("avaliacaoonlinematriculaquestao"));
		obj.getOpcaoRespostaQuestaoVO().setCodigo(tabelaResultado.getInt("opcaorespostaquestao"));
		obj.setMarcada(tabelaResultado.getBoolean("marcada"));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setOpcaoRespostaQuestaoVO(getFacadeFactory().getOpcaoRespostaQuestaoFacade().consultarPorChavePrimaria(obj.getOpcaoRespostaQuestaoVO().getCodigo(), 0, usuarioLogado));
			return obj;
		}
		return obj;
	}

	@Override
	public List<AvaliacaoOnlineMatriculaRespostaQuestaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List vetResultado = new ArrayList<AvaliacaoOnlineMatriculaRespostaQuestaoVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}

		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public AvaliacaoOnlineMatriculaRespostaQuestaoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM avaliacaoonlinematricularespostaquestao WHERE codigo = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new AvaliacaoOnlineMatriculaRespostaQuestaoVO();
	}

	@Override
	public void persistirRepostasQuestoesAvaliacaoOnlineMatricula(AvaliacaoOnlineMatriculaQuestaoVO avaliacaoOnlineMatriculaQuestaoVO, UsuarioVO usuarioVO) throws Exception {
		for (AvaliacaoOnlineMatriculaRespostaQuestaoVO avaliacaoOnlineMatriculaRespostaQuestaoVO : avaliacaoOnlineMatriculaQuestaoVO.getAvaliacaoOnlineMatriculaRespostaQuestaoVOs()) {
			avaliacaoOnlineMatriculaRespostaQuestaoVO.setAvaliacaoOnlineMatriculaQuestaoVO(avaliacaoOnlineMatriculaQuestaoVO);
			persistir(avaliacaoOnlineMatriculaRespostaQuestaoVO, false, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List consultarPorAvaliacaoOnlineMatriculaQuestao(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM avaliacaoonlinematricularespostaquestao WHERE avaliacaoonlinematriculaquestao = ? order by codigo";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		return montarDadosConsulta(rs, nivelMontarDados, usuarioLogado);
	}
}
