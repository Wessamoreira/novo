package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.RenovacaoMatriculaTurmaGradeDisciplinaCompostaInterfaceFacade;

/**
 * @author Wellington - 2 de fev de 2016
 *
 */
@Repository
public class RenovacaoMatriculaTurmaGradeDisciplinaComposta extends ControleAcesso implements RenovacaoMatriculaTurmaGradeDisciplinaCompostaInterfaceFacade {

	private static final long serialVersionUID = 1L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		RenovacaoMatriculaTurmaGradeDisciplinaComposta.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
		final String sql = "INSERT INTO RenovacaoMatriculaTurmaGradeDisciplinaComposta (renovacaoMatriculaTurma, gradeDisciplinaComposta) VALUES (?, ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
				final PreparedStatement sqlInserir = con.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getRenovacaoMatriculaTurmaVO().getCodigo());
				sqlInserir.setInt(2, obj.getGradeDisciplinaCompostaVO().getCodigo());
				return sqlInserir;
			}
		});
	}

	@Override
	public List<RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO> consultarPorRenovacaoMatriculaTurma(Integer renovacaoMatriculaTurma, UsuarioVO usuario) throws Exception {
		String sql = "SELECT RenovacaoMatriculaTurmaGradeDisciplinaComposta.* FROM RenovacaoMatriculaTurmaGradeDisciplinaComposta WHERE renovacaoMatriculaTurma = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, renovacaoMatriculaTurma);
		return montarDadosConsulta(rs, usuario);
	}

	private List<RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuario) throws Exception {
		List<RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO> renovacaoMatriculaTurmaGradeDisciplinaCompostaVOs = new ArrayList<RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO>(0);
		while (rs.next()) {
			renovacaoMatriculaTurmaGradeDisciplinaCompostaVOs.add(montarDados(rs, usuario));
		}
		return renovacaoMatriculaTurmaGradeDisciplinaCompostaVOs;
	}

	private RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO montarDados(SqlRowSet rs, UsuarioVO usuario) throws Exception {
		RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO obj = new RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO();
		obj.getRenovacaoMatriculaTurmaVO().setCodigo(rs.getInt("renovacaoMatriculaTurma"));
		obj.getGradeDisciplinaCompostaVO().setCodigo(rs.getInt("gradeDisciplinaComposta"));
		obj.setGradeDisciplinaCompostaVO(getFacadeFactory().getGradeDisciplinaCompostaFacade().consultarPorChavePrimaria(obj.getGradeDisciplinaCompostaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
		return obj;
	}

	@Override
	public void executarGeracaoRenovacaoMatriculaTurmaGradeDisciplinaComposta(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception {
		renovacaoMatriculaTurmaVO.setRenovacaoMatriculaTurmaGradeDisciplinaCompostaVOs(new ArrayList<RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO>(0));
		for (GradeDisciplinaCompostaVO gdcVO : gradeDisciplinaCompostaVOs) {
			RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO obj = new RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO();
			obj.setGradeDisciplinaCompostaVO(gdcVO);
			obj.setRenovacaoMatriculaTurmaVO(renovacaoMatriculaTurmaVO);
			renovacaoMatriculaTurmaVO.getRenovacaoMatriculaTurmaGradeDisciplinaCompostaVOs().add(obj);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirPorRenovacaoMatriculaTurma(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		excluirPorRenovacaoMatriculaTurma(renovacaoMatriculaTurmaVO, verificarAcesso, usuarioVO);
		for (RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO obj : renovacaoMatriculaTurmaVO.getRenovacaoMatriculaTurmaGradeDisciplinaCompostaVOs()) {
			incluir(obj, false, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorRenovacaoMatriculaTurma(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		RenovacaoMatriculaTurmaGradeDisciplinaComposta.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("DELETE FROM RenovacaoMatriculaTurmaGradeDisciplinaComposta WHERE renovacaoMatriculaTurma = ? ");
		sqlStr.append("AND gradeDisciplinaComposta NOT IN (0");
		for (RenovacaoMatriculaTurmaGradeDisciplinaCompostaVO obj : renovacaoMatriculaTurmaVO.getRenovacaoMatriculaTurmaGradeDisciplinaCompostaVOs()) {
			sqlStr.append(", ").append(obj.getGradeDisciplinaCompostaVO().getCodigo());
		}
		sqlStr.append(") ");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sqlStr.toString(), renovacaoMatriculaTurmaVO.getCodigo());
	}

}
