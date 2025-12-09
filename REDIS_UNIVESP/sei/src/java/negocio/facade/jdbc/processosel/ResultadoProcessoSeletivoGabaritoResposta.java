package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.GabaritoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoGabaritoRespostaVO;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.ResultadoProcessoSeletivoGabaritoRespostaInterfaceFacade;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class ResultadoProcessoSeletivoGabaritoResposta extends ControleAcesso implements ResultadoProcessoSeletivoGabaritoRespostaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	
	public void validarDados(ResultadoProcessoSeletivoGabaritoRespostaVO obj) throws Exception {
		if (obj.getRespostaCorreta().trim().equals("")) {
			throw new Exception("A Questão "+obj.getNrQuestao()+"º deve ser informada.");
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final ResultadoProcessoSeletivoGabaritoRespostaVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		final String sql = "INSERT INTO ResultadoProcessoSeletivoGabaritoResposta( nrQuestao, respostaCorreta, resultadoProcessoSeletivo ) VALUES ( ?, ?, ? )"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setInt(1, obj.getNrQuestao());
				sqlInserir.setString(2, obj.getRespostaCorreta());
				if (obj.getResultadoProcessoSeletivoVO().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(3, obj.getResultadoProcessoSeletivoVO().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(3, 0);
                }
				return sqlInserir;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ResultadoProcessoSeletivoGabaritoRespostaVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		final String sql = "UPDATE ResultadoProcessoSeletivoGabaritoResposta set nrQuestao=?, respostaCorreta=? WHERE (codigo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getNrQuestao());
				sqlAlterar.setString(2, obj.getRespostaCorreta());
				if (obj.getResultadoProcessoSeletivoVO().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(3, obj.getResultadoProcessoSeletivoVO().getCodigo().intValue());
                } else {
                	sqlAlterar.setNull(3, 0);
                }
				sqlAlterar.setInt(4, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirResultadoProcessoSeletivoGabaritoResposta(Integer gabarito, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM ResultadoProcessoSeletivoGabaritoResposta WHERE (resultadoProcessoSeletivo = ?)"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { gabarito });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarResultadoGabaritoRespostaVOs(Integer resultadoProcessoSeletivo, List objetos, UsuarioVO usuario) throws Exception {
		excluirResultadoProcessoSeletivoGabaritoResposta(resultadoProcessoSeletivo, usuario);
		incluirResultadoProcessoSeletivoGabaritoRespostaVOs(resultadoProcessoSeletivo, objetos, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirResultadoProcessoSeletivoGabaritoRespostaVOs(Integer resultadoProcessoSeletivo, List objetos, UsuarioVO usuario) throws Exception {
		Iterator e = objetos.iterator();
		while (e.hasNext()) {
			ResultadoProcessoSeletivoGabaritoRespostaVO obj = (ResultadoProcessoSeletivoGabaritoRespostaVO) e.next();
			obj.getResultadoProcessoSeletivoVO().setCodigo(resultadoProcessoSeletivo);
			incluir(obj, usuario);
		}
	}
	
	public List<ResultadoProcessoSeletivoGabaritoRespostaVO> consultaRapidaPorResultadoProcessoSeletivo(Integer resultadoProcessoSeletivo, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ResultadoProcessoSeletivoGabaritoResposta where resultadoProcessoSeletivo = ").append(resultadoProcessoSeletivo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<ResultadoProcessoSeletivoGabaritoRespostaVO> listaGabaritoRespostaVOs = new ArrayList<ResultadoProcessoSeletivoGabaritoRespostaVO>(0);
		while (tabelaResultado.next()) {
			ResultadoProcessoSeletivoGabaritoRespostaVO obj = new ResultadoProcessoSeletivoGabaritoRespostaVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.getResultadoProcessoSeletivoVO().setCodigo(tabelaResultado.getInt("resultadoProcessoSeletivo"));
			obj.setNrQuestao(tabelaResultado.getInt("nrQuestao"));
			obj.setRespostaCorreta(tabelaResultado.getString("respostaCorreta"));
			listaGabaritoRespostaVOs.add(obj);
		}
		return listaGabaritoRespostaVOs;
	}
	
	public GabaritoVO consultarCodigoGabaritoPorInscricao(Integer inscricao, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("Select gabarito.codigo, gabarito.descricao, gabarito.quantidadeQuestao, gabarito.responsavel, gabarito.quantidadeQuestao  ");
		sql.append(" from inscricao ");
		sql.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo and inscricao.itemProcessoSeletivoDataProva ");
		sql.append(" inner join procseletivoGabaritodata on procseletivoGabaritodata.ItemProcSeletivoDataProva =  ItemProcSeletivoDataProva.codigo  ");
		sql.append(" and ((procseletivoGabaritodata.disciplinaidioma =  inscricao.opcaolinguaestrangeira) or (procseletivoGabaritodata.disciplinaidioma is null and inscricao.opcaolinguaestrangeira is null)) ");
		sql.append(" inner join gabarito on gabarito.codigo = procseletivoGabaritodata.gabarito ");
		sql.append(" where  inscricao.codigo = ").append(inscricao);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		GabaritoVO obj = new GabaritoVO();
		if (rs.next()) {
			obj.setCodigo(rs.getInt("codigo"));
			obj.setDescricao(rs.getString("descricao"));
			obj.getResponsavel().setCodigo(rs.getInt("responsavel"));
			obj.setQuantidadeQuestao(rs.getInt("quantidadeQuestao"));
		}
		return obj;
	}

}
