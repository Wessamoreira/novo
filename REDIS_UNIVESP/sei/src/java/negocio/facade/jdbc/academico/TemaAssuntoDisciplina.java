package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TemaAssuntoDisciplinaVO;
import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TemaAssuntoDisciplinaInterfaceFacade;

/**
 * @author Victor Hugo 27/02/2015
 */
@Repository
@Scope("singleton")
@Lazy
public class TemaAssuntoDisciplina extends ControleAcesso implements TemaAssuntoDisciplinaInterfaceFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		TemaAssuntoDisciplina.idEntidade = idEntidade;
	}

	public TemaAssuntoDisciplina() throws Exception {
		super();
		setIdEntidade("TemaAssuntoDisciplina");
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final TemaAssuntoDisciplinaVO temaAssuntoDisciplinaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(temaAssuntoDisciplinaVO);
			TemaAssuntoDisciplina.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "insert into temaAssuntoDisciplina(temaassunto, disciplina) values(?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			temaAssuntoDisciplinaVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setInt(1, temaAssuntoDisciplinaVO.getTemaAssuntoVO().getCodigo());
					sqlInserir.setInt(2, temaAssuntoDisciplinaVO.getDisciplinaVO().getCodigo());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						temaAssuntoDisciplinaVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			temaAssuntoDisciplinaVO.setNovoObj(Boolean.TRUE);
			temaAssuntoDisciplinaVO.setCodigo(0);
			throw e;
		}
	}

	public void validarDados(TemaAssuntoDisciplinaVO temaAssuntoDisciplinaVO) throws Exception {

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final TemaAssuntoDisciplinaVO temaAssuntoDisciplinaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			TemaAssuntoDisciplina.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "UPDATE temaassuntodisciplina set temaassunto = ?, disciplina = ?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setInt(1, temaAssuntoDisciplinaVO.getTemaAssuntoVO().getCodigo());
					sqlAlterar.setInt(2, temaAssuntoDisciplinaVO.getDisciplinaVO().getCodigo());
					sqlAlterar.setInt(3, temaAssuntoDisciplinaVO.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final Integer codigoDisciplina, final Integer codigoTemaAssunto, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			TemaAssunto.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM temaAssuntoDisciplina WHERE disciplina = "+codigoDisciplina+" and temaassunto = "+codigoTemaAssunto+" "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirTodosTemaAssuntoDisciplina(Integer codigoDisciplina, List<TemaAssuntoVO> temaAssuntoVOs, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if(temaAssuntoVOs.isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_TemaAssunto_naoExisteAssuntosASeremExcluidos"));
		}
		for (TemaAssuntoVO temaAssuntoVO : temaAssuntoVOs) {
			excluir(codigoDisciplina, temaAssuntoVO.getCodigo(), false, usuarioVO);
		}
	}
	
	
	@Override
	public TemaAssuntoDisciplinaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		TemaAssuntoDisciplinaVO obj = new TemaAssuntoDisciplinaVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getTemaAssuntoVO().setCodigo(tabelaResultado.getInt("temaassunto"));
		obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina"));
		obj.setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogado));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setTemaAssuntoVO(getFacadeFactory().getTemaAssuntoFacade().consultarPorChavePrimaria(obj.getTemaAssuntoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
		}
		return obj;
	}

	public List<TemaAssuntoDisciplinaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<TemaAssuntoDisciplinaVO> vetResultado = new ArrayList<TemaAssuntoDisciplinaVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}
	
	@Override
	public void adicionarTemaAssuntoTemaAssuntoDisciplina(TemaAssuntoVO temaAssuntoVO, List<TemaAssuntoDisciplinaVO> temaAssuntoDisciplinaVOs, List<TemaAssuntoVO> temaAssuntoVOs, DisciplinaVO disciplinaVO, UsuarioVO usuarioVO) throws Exception {
		for (TemaAssuntoVO temaAssuntoVO2 : temaAssuntoVOs) {
			if(temaAssuntoVO2.getCodigo().equals(temaAssuntoVO.getCodigo())) {
				throw new Exception(UteisJSF.internacionalizar("msg_TemaAssunto_temaAssuntoJaAdicionado"));
			}			
		}
		TemaAssuntoDisciplinaVO temaAssuntoDisciplinaVO = new TemaAssuntoDisciplinaVO();
		temaAssuntoDisciplinaVO.setDisciplinaVO(disciplinaVO);
		temaAssuntoDisciplinaVO.setTemaAssuntoVO(temaAssuntoVO);
		if(temaAssuntoDisciplinaVO.getTemaAssuntoVO().getNome().equals("")) {
			throw new Exception("O Campo ASSUNTO Deve Ser Preenchido");
		}
		if(temaAssuntoDisciplinaVO.getTemaAssuntoVO().getAbreviatura().equals("")) {
			throw new Exception("O Campo ABREVIATURA Deve Ser Preenchido");
		}
		temaAssuntoDisciplinaVOs.add(temaAssuntoDisciplinaVO);
	}
	
	@Override
	public void incluirTemasAssuntoDisciplina(List<TemaAssuntoDisciplinaVO> temaAssuntoDisciplinaVOs, UsuarioVO usuarioVO) throws Exception {
		for (TemaAssuntoDisciplinaVO temaAssuntoDisciplinaVO : temaAssuntoDisciplinaVOs) {
			incluir(temaAssuntoDisciplinaVO, false, usuarioVO);
		}
	}
}
