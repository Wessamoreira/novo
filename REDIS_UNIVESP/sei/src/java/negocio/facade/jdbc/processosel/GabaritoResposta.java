package negocio.facade.jdbc.processosel;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.enumeradores.TipoGabaritoEnum;
import negocio.comuns.academico.enumeradores.TipoRespostaGabaritoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.GabaritoRespostaVO;
import negocio.comuns.processosel.GabaritoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.GabaritoRespostaInterfaceFacade;

@Repository
public class GabaritoResposta extends ControleAcesso implements GabaritoRespostaInterfaceFacade {

	private static final long serialVersionUID = 1L;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final GabaritoRespostaVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		StringBuilder sql = new StringBuilder();
		sql.append(" INSERT INTO GabaritoResposta( nrQuestao, respostaCorreta, gabarito, disciplina, areaConhecimento, valorNota, disciplinasprocseletivo, anulado, historicoAnulado)");
		sql.append(" VALUES ( ?, ?, ?, ?, ?, ? ,?, ?, ?)");
		sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
				sqlInserir.setInt(1, obj.getNrQuestao());
				sqlInserir.setString(2, obj.getRespostaCorreta());
				if (obj.getGabaritoVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(3, obj.getGabaritoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(3, 0);
				}
				if (obj.getDisciplinaVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(4, obj.getDisciplinaVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(4, 0);
				}
				if (obj.getAreaConhecimentoVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(5, obj.getAreaConhecimentoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(5, 0);
				}
				sqlInserir.setBigDecimal(6, obj.getValorNota());
				if (obj.getGabaritoVO().getControlarGabaritoPorDisciplina() && Uteis.isAtributoPreenchido(obj.getDisciplinasProcSeletivoVO())) {
					sqlInserir.setInt(7, obj.getDisciplinasProcSeletivoVO().getCodigo());
				} else {
					sqlInserir.setNull(7, 0);
				}
				sqlInserir.setBoolean(8, obj.getAnulado());
				sqlInserir.setString(9, obj.getHistoricoAnulado());
				return sqlInserir;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final GabaritoRespostaVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		final String sql = "UPDATE GabaritoResposta set nrQuestao=?, respostaCorreta=?, disciplina=?, areaConhecimento=?, valorNota=?, disciplinasprocseletivo=? WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setInt(1, obj.getNrQuestao());
				sqlAlterar.setString(2, obj.getRespostaCorreta());
				if (obj.getGabaritoVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(3, obj.getGabaritoVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(3, 0);
				}
				if (obj.getDisciplinaVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(4, obj.getDisciplinaVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(4, 0);
				}
				if (obj.getAreaConhecimentoVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(5, obj.getAreaConhecimentoVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(5, 0);
				}
				sqlAlterar.setBigDecimal(6, obj.getValorNota());
				if (obj.getGabaritoVO().getControlarGabaritoPorDisciplina() && Uteis.isAtributoPreenchido(obj.getDisciplinasProcSeletivoVO())) {
					sqlAlterar.setInt(7, obj.getDisciplinasProcSeletivoVO().getCodigo());
				} else {
					sqlAlterar.setNull(7, 0);
				}
				sqlAlterar.setBoolean(8, obj.getAnulado());
				sqlAlterar.setString(9, obj.getHistoricoAnulado());
				sqlAlterar.setInt(10, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirGabaritoResposta(Integer gabarito, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM GabaritoResposta WHERE (gabarito = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { gabarito });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarGabaritoRespostaVOs(GabaritoVO gabaritoVO, UsuarioVO usuario) throws Exception {
		excluirGabaritoResposta(gabaritoVO.getCodigo(), usuario);
		incluirGabaritoRespostaVOs(gabaritoVO, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirGabaritoRespostaVOs(GabaritoVO gabaritoVO, UsuarioVO usuario) throws Exception {
		Iterator<GabaritoRespostaVO> e = gabaritoVO.getGabaritoRespostaVOs().iterator();
		while (e.hasNext()) {
			GabaritoRespostaVO obj = (GabaritoRespostaVO) e.next();
			obj.getGabaritoVO().setCodigo(gabaritoVO.getCodigo());
			inicializarDadosTipoRespostaGabarito(obj);
			incluir(obj, usuario);
		}
	}

	public void inicializarDadosTipoRespostaGabarito(GabaritoRespostaVO obj) {
		if (obj.getGabaritoVO().getTipoGabaritoEnum().name().equals(TipoGabaritoEnum.PROVA_PRESENCIAL.name())) {
			if (obj.getGabaritoVO().getTipoRespostaGabaritoEnum().name().equals(TipoRespostaGabaritoEnum.DISCIPLINA.name())) {
				obj.setAreaConhecimentoVO(null);
			} else {
				obj.setDisciplinaVO(null);
			}
		}
	}

	public List<GabaritoRespostaVO> consultaRapidaPorGabarito(Integer gabarito, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT gabaritoResposta.codigo, gabaritoResposta.gabarito, gabaritoResposta.nrQuestao, gabaritoResposta.respostaCorreta, gabaritoResposta.valorNota, ");
		sb.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", ");
		sb.append(" areaConhecimento.codigo AS \"areaConhecimento.codigo\", areaConhecimento.nome AS \"areaConhecimento.nome\", gabaritoResposta.anulado, gabaritoResposta.historicoanulado ");
		sb.append(" FROM gabaritoResposta ");
		sb.append(" LEFT JOIN disciplina ON disciplina.codigo = gabaritoResposta.disciplina ");
		sb.append(" LEFT JOIN areaConhecimento ON areaConhecimento.codigo = gabaritoResposta.areaConhecimento ");
		sb.append(" where gabaritoResposta.gabarito = ").append(gabarito);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<GabaritoRespostaVO> listaGabaritoRespostaVOs = new ArrayList<>();

		while (tabelaResultado.next()) {
			listaGabaritoRespostaVOs.add(montarDados(tabelaResultado));
		}
		return listaGabaritoRespostaVOs;
	}
	
	public void validarDados(GabaritoRespostaVO obj) throws Exception {
		if (obj.getRespostaCorreta().equals("")) {
			throw new Exception("A Questão " + obj.getNrQuestao() + "º deve ser informada.");
		}
		if (obj.getGabaritoVO().getTipoGabaritoEnum().name().equals(TipoGabaritoEnum.PROVA_PRESENCIAL.name())) {
			if (obj.getValorNota().compareTo(BigDecimal.ZERO) == 0) {
				throw new Exception("O campo VALOR NOTA da " + obj.getNrQuestao() + "º questão deve ser informado.");
			}
			if (obj.getGabaritoVO().getTipoRespostaGabaritoEnum().name().equals(TipoRespostaGabaritoEnum.AREA_CONHECIMENTO.name())) {
				if (obj.getAreaConhecimentoVO().getCodigo().equals(0)) {
					throw new Exception("O campo ÁREA DE CONHECIMENTO da " + obj.getNrQuestao() + "º questão deve ser informado.");
				}
			}
			if (obj.getGabaritoVO().getTipoRespostaGabaritoEnum().name().equals(TipoRespostaGabaritoEnum.DISCIPLINA.name())) {
				if (obj.getDisciplinaVO().getCodigo().equals(0)) {
					throw new Exception("O campo DISCIPLINA da " + obj.getNrQuestao() + "º questão deve ser informado.");
				}
			}
		} else {
			if (obj.getGabaritoVO().getControlarGabaritoPorDisciplina() && !Uteis.isAtributoPreenchido(obj.getDisciplinasProcSeletivoVO())) {
				throw new Exception("O campo DISCIPLINA da " + obj.getNrQuestao() + "º questão deve ser informado.");
			}
		}
	}

	private GabaritoRespostaVO montarDados(SqlRowSet tabelaResultado) {
		GabaritoRespostaVO obj = new GabaritoRespostaVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getGabaritoVO().setCodigo(tabelaResultado.getInt("gabarito"));
		obj.setNrQuestao(tabelaResultado.getInt("nrQuestao"));
		obj.setRespostaCorreta(tabelaResultado.getString("respostaCorreta"));
		obj.setValorNota(tabelaResultado.getBigDecimal("valorNota"));

		obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina.codigo"));
		obj.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina.nome"));

		obj.getAreaConhecimentoVO().setCodigo(tabelaResultado.getInt("areaConhecimento.codigo"));
		obj.getAreaConhecimentoVO().setNome(tabelaResultado.getString("areaConhecimento.nome"));

		obj.setAnulado(tabelaResultado.getBoolean("anulado"));
		obj.setHistoricoAnulado(tabelaResultado.getString("historicoanulado"));
		return obj;
	}
}
