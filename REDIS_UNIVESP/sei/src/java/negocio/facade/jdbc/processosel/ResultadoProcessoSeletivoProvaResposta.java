package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.OpcaoRespostaQuestaoProcessoSeletivoVO;
import negocio.comuns.processosel.QuestaoProcessoSeletivoVO;
import negocio.comuns.processosel.QuestaoProvaProcessoSeletivoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoProvaRespostaVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.ResultadoProcessoSeletivoProvaRespostaInterfaceFacade;

@Repository
public class ResultadoProcessoSeletivoProvaResposta extends ControleAcesso implements ResultadoProcessoSeletivoProvaRespostaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ResultadoProcessoSeletivoProvaRespostaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			ResultadoProcessoSeletivoProvaResposta.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "INSERT INTO ResultadoProcessoSeletivoProvaResposta (resultadoProcessoSeletivo, opcaoRespostaQuestaoProcessoSeletivoMarcada) VALUES (?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					try {
						sqlInserir.setInt(1, obj.getResultadoProcessoSeletivoVO().getCodigo());
						sqlInserir.setInt(2, obj.getOpcaoRespostaQuestaoProcessoSeletivoMarcadaVO().getCodigo());
					} catch (final Exception x) {
						return null;
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {
				public Integer extractData(final ResultSet rs) throws SQLException, DataAccessException {
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
	public void incluirResultadoProcessoSeletivoProvaRespostaVOs(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, UsuarioVO usuario) throws Exception {
		Iterator<ResultadoProcessoSeletivoProvaRespostaVO> iterator = resultadoProcessoSeletivoVO.getResultadoProcessoSeletivoProvaRespostaVOs().iterator();
		while (iterator.hasNext()) {
			ResultadoProcessoSeletivoProvaRespostaVO obj = (ResultadoProcessoSeletivoProvaRespostaVO) iterator.next();
			obj.setResultadoProcessoSeletivoVO(resultadoProcessoSeletivoVO);
			incluir(obj, false, usuario);
		}
	}

	@Override
	public List<ResultadoProcessoSeletivoProvaRespostaVO> consultarPorResultadoProcessoSeletivo(Integer resultadoProcessoSeletivo, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		ResultadoProcessoSeletivoProvaResposta.consultar(idEntidade, verificarAcesso, usuarioVO);
		String sql = "SELECT * FROM ResultadoProcessoSeletivoProvaResposta WHERE resultadoProcessoSeletivo = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { resultadoProcessoSeletivo });
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}

	private List<ResultadoProcessoSeletivoProvaRespostaVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<ResultadoProcessoSeletivoProvaRespostaVO> resultadoProcessoSeletivoProvaRespostaVOs = new ArrayList<ResultadoProcessoSeletivoProvaRespostaVO>(0);
		while (rs.next()) {
			resultadoProcessoSeletivoProvaRespostaVOs.add(montarDados(rs, nivelMontarDados, usuarioVO));
		}
		return resultadoProcessoSeletivoProvaRespostaVOs;
	}

	private ResultadoProcessoSeletivoProvaRespostaVO montarDados(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		ResultadoProcessoSeletivoProvaRespostaVO obj = new ResultadoProcessoSeletivoProvaRespostaVO();
		obj.setCodigo(rs.getInt("codigo"));
		obj.getResultadoProcessoSeletivoVO().setCodigo(rs.getInt("resultadoProcessoSeletivo"));
		obj.getOpcaoRespostaQuestaoProcessoSeletivoMarcadaVO().setCodigo(rs.getInt("opcaoRespostaQuestaoProcessoSeletivoMarcada"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarGeracaoResultadoProcessoSeletivoProvaRespostaVO(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO) throws Exception {
		if(!resultadoProcessoSeletivoVO.getInscricao().getProvaProcessoSeletivoVO().getQuestaoProvaProcessoSeletivoVOs().isEmpty()) {			
			resultadoProcessoSeletivoVO.setResultadoProcessoSeletivoProvaRespostaVOs(new ArrayList<ResultadoProcessoSeletivoProvaRespostaVO>(0));
			for (QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO : resultadoProcessoSeletivoVO.getInscricao().getProvaProcessoSeletivoVO().getQuestaoProvaProcessoSeletivoVOs()) {
				for (OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO : questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
					if (opcaoRespostaQuestaoProcessoSeletivoVO.getMarcada()) {
						ResultadoProcessoSeletivoProvaRespostaVO obj = new ResultadoProcessoSeletivoProvaRespostaVO();
						obj.setResultadoProcessoSeletivoVO(resultadoProcessoSeletivoVO);
						obj.setOpcaoRespostaQuestaoProcessoSeletivoMarcadaVO(opcaoRespostaQuestaoProcessoSeletivoVO);
						resultadoProcessoSeletivoVO.getResultadoProcessoSeletivoProvaRespostaVOs().add(obj);
					}
				}
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarExclusaoGeracaoResultadoProcessoSeletivoProvaRespostaVO(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO, UsuarioVO usuarioVO) throws Exception {
	resultadoProcessoSeletivoVO.setResultadoProcessoSeletivoProvaRespostaVOs(new ArrayList<ResultadoProcessoSeletivoProvaRespostaVO>(0));
	excluirPorQuestaoProcessoSeletivo(questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getCodigo(), resultadoProcessoSeletivoVO.getCodigo(), usuarioVO);
		for (OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO : questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
			if (opcaoRespostaQuestaoProcessoSeletivoVO.getMarcada()) {
				ResultadoProcessoSeletivoProvaRespostaVO obj = new ResultadoProcessoSeletivoProvaRespostaVO();
				obj.setResultadoProcessoSeletivoVO(resultadoProcessoSeletivoVO);
				obj.setOpcaoRespostaQuestaoProcessoSeletivoMarcadaVO(opcaoRespostaQuestaoProcessoSeletivoVO);
				resultadoProcessoSeletivoVO.getResultadoProcessoSeletivoProvaRespostaVOs().add(obj);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorResultadoProcessoSeletivo(Integer resultadoProcessoSeletivo, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM ResultadoProcessoSeletivoProvaResposta WHERE resultadoProcessoSeletivo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { resultadoProcessoSeletivo });
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorQuestaoProcessoSeletivo(Integer codigoQuestaoProcessoSeletivo, Integer resultadoProcessoSeletivo, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("delete from ResultadoProcessoSeletivoProvaResposta where ResultadoProcessoSeletivoProvaResposta.codigo in ( ");
		sql.append("select ResultadoProcessoSeletivoProvaResposta.codigo ");
		sql.append("from ResultadoProcessoSeletivoProvaResposta ");
		sql.append("inner join opcaorespostaquestaoprocessoseletivo on ResultadoProcessoSeletivoProvaResposta.opcaorespostaquestaoprocessoseletivomarcada = opcaorespostaquestaoprocessoseletivo.codigo ");
		sql.append("where opcaorespostaquestaoprocessoseletivo.questaoprocessoseletivo  =  ? ");
		sql.append("and ResultadoProcessoSeletivoProvaResposta.resultadoProcessoSeletivo  =  ?");
		sql.append(")");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sql.toString(), new Object[] { codigoQuestaoProcessoSeletivo, resultadoProcessoSeletivo });
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarResultadoProcessoSeletivoProvaRespostaVOs(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, UsuarioVO usuario) throws Exception {
		 executarGeracaoResultadoProcessoSeletivoProvaRespostaVO(resultadoProcessoSeletivoVO);	
	   	 excluirPorResultadoProcessoSeletivo(resultadoProcessoSeletivoVO.getCodigo(), usuario);
		 incluirResultadoProcessoSeletivoProvaRespostaVOs(resultadoProcessoSeletivoVO, usuario);
		
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = ResultadoProcessoSeletivoProvaResposta.class.getSimpleName();
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		ResultadoProcessoSeletivoProvaResposta.idEntidade = idEntidade;
	}
	
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarResultadoProcessoSeletivoProvaRespostaVOs(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO, QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO, UsuarioVO usuarioVO) throws Exception {
    	executarExclusaoGeracaoResultadoProcessoSeletivoProvaRespostaVO(resultadoProcessoSeletivoVO, questaoProvaProcessoSeletivoVO, usuarioVO);
    	incluirResultadoProcessoSeletivoProvaRespostaVOs(resultadoProcessoSeletivoVO, usuarioVO);
    }
	
}
