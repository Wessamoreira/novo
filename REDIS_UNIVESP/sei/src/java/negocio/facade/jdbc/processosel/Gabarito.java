package negocio.facade.jdbc.processosel;

import java.math.BigDecimal;
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

import negocio.comuns.academico.enumeradores.TipoCalculoGabaritoEnum;
import negocio.comuns.academico.enumeradores.TipoGabaritoEnum;
import negocio.comuns.academico.enumeradores.TipoRespostaGabaritoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.ColunaGabaritoVO;
import negocio.comuns.processosel.GabaritoRespostaVO;
import negocio.comuns.processosel.GabaritoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.GabaritoInterfaceFacade;

@Repository
public class Gabarito extends ControleAcesso implements GabaritoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public Gabarito() {
		super();
		setIdEntidade("Gabarito");
	}

	public void validarDados(GabaritoVO obj) throws Exception {
		if (obj.getDescricao().equals("")) {
			throw new Exception("O campo DESCRIÇÃO deve ser informado.");
		}
		if (obj.getTipoGabaritoEnum().name().equals(TipoGabaritoEnum.PROVA_PRESENCIAL.name())) {
			if (obj.getUnidadeEnsinoVO().getCodigo().equals(0)) {
				throw new Exception("O campo UNIDADE DE ENSINO deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL.");
			}
			if (obj.getCursoVO().getCodigo().equals(0)) {
				throw new Exception("O campo CURSO deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL");
			}
			if (obj.getGradeCurricularVO().getCodigo().equals(0)) {
				throw new Exception("O campo MATRIZ CURRICULAR deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL");
			}
			/*if (obj.getPeriodoLetivoVO().getCodigo().equals(0)) {
				throw new Exception("O campo PERÍODO LETIVO deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL");
			}*/
			if (obj.getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
				throw new Exception("O campo CONFIGURAÇÃO ACADÊMICO deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL");
			}
			if (obj.getVariavelNota().equals("")) {
				throw new Exception("O campo VARIÁVEL NOTA deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL");
			}
			if ((obj.getCursoVO().getPeriodicidade().equals("SE") || obj.getCursoVO().getPeriodicidade().equals("AN")) && obj.getAno().trim().equals("")) {
				throw new Exception("O campo ANO deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL");
			}
			if ((obj.getCursoVO().getPeriodicidade().equals("SE")) && obj.getSemestre().trim().equals("")) {
				throw new Exception("O campo SEMESTRE deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL");
			}
		} else {
			if (obj.getControlarGabaritoPorDisciplina() && !Uteis.isAtributoPreenchido(obj.getGrupoDisciplinaProcSeletivoVO())) {
				throw new Exception("O campo GRUPO DISCIPLINA PROCESSO SELETIVO deve ser informado.");
			}
		}
		if (obj.getQuantidadeQuestao().equals(0)) {
			throw new Exception("O campo QUANTIDADE DE QUESTÃO deve ser informado.");
		}
		if (obj.getGabaritoRespostaVOs().isEmpty()) {
			throw new Exception("É preciso adicionar a(s) resposta(s) ao Gabarito.");
		}
		
		if (obj.getTipoGabaritoEnum().name().equals(TipoGabaritoEnum.PROVA_PRESENCIAL.name())) {
			if (obj.getUnidadeEnsinoVO().getCodigo().equals(0)) {
				throw new Exception("O campo UNIDADE DE ENSINO deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL.");
			}
			if (obj.getCursoVO().getCodigo().equals(0)) {
				throw new Exception("O campo CURSO deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL");
			}
			if (obj.getGradeCurricularVO().getCodigo().equals(0)) {
				throw new Exception("O campo MATRIZ CURRICULAR deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL");
			}
			/*if (obj.getPeriodoLetivoVO().getCodigo().equals(0)) {
				throw new Exception("O campo PERÍODO LETIVO deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL");
			}*/
			if (obj.getConfiguracaoAcademicoVO().getCodigo().equals(0)) {
				throw new Exception("O campo CONFIGURAÇÃO ACADÊMICO deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL");
			}
			if (obj.getVariavelNota().equals("")) {
				throw new Exception("O campo VARIÁVEL NOTA deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL");
			}
			if ((obj.getCursoVO().getPeriodicidade().equals("SE") || obj.getCursoVO().getPeriodicidade().equals("AN")) && obj.getAno().trim().equals("")) {
				throw new Exception("O campo ANO deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL");
			}
			if ((obj.getCursoVO().getPeriodicidade().equals("SE")) && obj.getSemestre().trim().equals("")) {
				throw new Exception("O campo SEMESTRE deve ser informado quando o tipo do gabarito for PROVA PRESENCIAL");
			}

			
		}
	}

	public void persistir(GabaritoVO obj, UsuarioVO usuarioVO) throws Exception {
		iniciarlizarDadosListaGabaritoResposta(obj, usuarioVO);
		if (obj.isNovoObj().booleanValue()) {
			incluir(obj, usuarioVO);
		} else {
			alterar(obj, usuarioVO);
		}
	}

	public void iniciarlizarDadosListaGabaritoResposta(GabaritoVO obj, UsuarioVO usuarioVO) {
		obj.getGabaritoRespostaVOs().clear();
		for (ColunaGabaritoVO colunaGabaritoVO : obj.getColunaGabaritoVOs()) {
			for (GabaritoRespostaVO gabaritoRespostaVO : colunaGabaritoVO.getGabaritoRespostaVOs()) {
				gabaritoRespostaVO.setGabaritoVO(obj);
				obj.getGabaritoRespostaVOs().add(gabaritoRespostaVO);
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final GabaritoVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		obj.setResponsavel(usuario);
		Gabarito.incluir(getIdEntidade(), true, usuario);
		final String sql = "INSERT INTO Gabarito( descricao, quantidadeQuestao, responsavel, unidadeEnsino, curso, gradeCurricular, periodoLetivo, turno, configuracaoAcademico, variavelNota, tipoGabarito, realizarCalculoMediaLancamentoNota, tipoRespostaGabarito, ano, semestre, tipoCalculoGabarito, tamanhoNrMatriculaArquivo, controlarGabaritoPorDisciplina, grupoDisciplinaProcSeletivo) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		
		obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setString(1, obj.getDescricao());
				sqlInserir.setInt(2, obj.getQuantidadeQuestao());
				if (obj.getResponsavel().getCodigo().intValue() != 0) {
					sqlInserir.setInt(3, obj.getResponsavel().getCodigo().intValue());
				} else {
					sqlInserir.setNull(3, 0);
				}

				if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(4, obj.getUnidadeEnsinoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(4, 0);
				}
				if (obj.getCursoVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(5, obj.getCursoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(5, 0);
				}
				if (obj.getGradeCurricularVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(6, obj.getGradeCurricularVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(6, 0);
				}
				if (obj.getPeriodoLetivoVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(7, obj.getPeriodoLetivoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(7, 0);
				}
				if (obj.getTurnoVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(8, obj.getTurnoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(8, 0);
				}
				if (obj.getConfiguracaoAcademicoVO().getCodigo().intValue() != 0) {
					sqlInserir.setInt(9, obj.getConfiguracaoAcademicoVO().getCodigo().intValue());
				} else {
					sqlInserir.setNull(9, 0);
				}
				sqlInserir.setString(10, obj.getVariavelNota());
				sqlInserir.setString(11, obj.getTipoGabaritoEnum().name());
				sqlInserir.setBoolean(12, obj.getRealizarCalculoMediaLancamentoNota());
				sqlInserir.setString(13, obj.getTipoRespostaGabaritoEnum().name());
				sqlInserir.setString(14, obj.getAno());
				sqlInserir.setString(15, obj.getSemestre());
				sqlInserir.setString(16, obj.getTipoCalculoGabaritoEnum().name());
				sqlInserir.setInt(17, obj.getTamanhoNrMatriculaArquivo());
				sqlInserir.setBoolean(18, obj.getControlarGabaritoPorDisciplina());
				if (Uteis.isAtributoPreenchido(obj.getGrupoDisciplinaProcSeletivoVO()) && obj.getControlarGabaritoPorDisciplina()) {
					sqlInserir.setInt(19, obj.getGrupoDisciplinaProcSeletivoVO().getCodigo());
				} else {
					sqlInserir.setNull(19, 0);
				}
				return sqlInserir;
			}
		}, new ResultSetExtractor<Object>() {
			public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
				if (arg0.next()) {
					obj.setNovoObj(Boolean.FALSE);
					return arg0.getInt("codigo");
				}
				return null;
			}
		}));
		getFacadeFactory().getGabaritoRespostaFacade().incluirGabaritoRespostaVOs(obj, usuario);
		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final GabaritoVO obj, UsuarioVO usuario) throws Exception {
		validarDados(obj);
		obj.setResponsavel(usuario);
		Gabarito.incluir(getIdEntidade(), true, usuario);
		final String sql = "UPDATE Gabarito set descricao=?, quantidadeQuestao=?, responsavel=?, unidadeEnsino=?, curso=?, gradeCurricular=?, periodoLetivo=?, turno=?, configuracaoAcademico=?, variavelNota=?, tipoGabarito=?, realizarCalculoMediaLancamentoNota=?, tipoRespostaGabarito=?, ano=?, semestre=?, tipoCalculoGabarito=?, tamanhoNrMatriculaArquivo=?, controlarGabaritoPorDisciplina=?, grupoDisciplinaProcSeletivo=?  WHERE (codigo = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, obj.getDescricao());
				sqlAlterar.setInt(2, obj.getQuantidadeQuestao());
				if (obj.getResponsavel().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(3, obj.getResponsavel().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(3, 0);
				}

				if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(4, obj.getUnidadeEnsinoVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(4, 0);
				}
				if (obj.getCursoVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(5, obj.getCursoVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(5, 0);
				}
				if (obj.getGradeCurricularVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(6, obj.getGradeCurricularVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(6, 0);
				}
				if (obj.getPeriodoLetivoVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(7, obj.getPeriodoLetivoVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(7, 0);
				}
				if (obj.getTurnoVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(8, obj.getTurnoVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(8, 0);
				}
				if (obj.getConfiguracaoAcademicoVO().getCodigo().intValue() != 0) {
					sqlAlterar.setInt(9, obj.getConfiguracaoAcademicoVO().getCodigo().intValue());
				} else {
					sqlAlterar.setNull(9, 0);
				}
				sqlAlterar.setString(10, obj.getVariavelNota());
				sqlAlterar.setString(11, obj.getTipoGabaritoEnum().name());
				sqlAlterar.setBoolean(12, obj.getRealizarCalculoMediaLancamentoNota());
				sqlAlterar.setString(13, obj.getTipoRespostaGabaritoEnum().name());
				sqlAlterar.setString(14, obj.getAno());
				sqlAlterar.setString(15, obj.getSemestre());
				sqlAlterar.setString(16, obj.getTipoCalculoGabaritoEnum().name());
				sqlAlterar.setInt(17, obj.getTamanhoNrMatriculaArquivo());
				sqlAlterar.setBoolean(18, obj.getControlarGabaritoPorDisciplina());
				if (Uteis.isAtributoPreenchido(obj.getGrupoDisciplinaProcSeletivoVO()) && obj.getControlarGabaritoPorDisciplina()) {
					sqlAlterar.setInt(19, obj.getGrupoDisciplinaProcSeletivoVO().getCodigo());
				} else {
					sqlAlterar.setNull(19, 0);
				}
				sqlAlterar.setInt(20, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
		getFacadeFactory().getGabaritoRespostaFacade().alterarGabaritoRespostaVOs(obj, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(GabaritoVO obj, UsuarioVO usuario) throws Exception {
		Gabarito.excluir(getIdEntidade(), true, usuario);
		getFacadeFactory().getGabaritoRespostaFacade().excluirGabaritoResposta(obj.getCodigo(), usuario);
		String sql = "DELETE FROM Gabarito WHERE (codigo = ?)";
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
	}

	public GabaritoVO consultaRapidaPorChavePrimaria(Integer codigo, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("select gabarito.codigo, gabarito.descricao, gabarito.quantidadeQuestao, responsavel, gabarito.tamanhoNrMatriculaArquivo, ");
		sb.append(" gabaritoResposta.codigo AS \"gabaritoResposta.codigo\", gabaritoResposta.nrQuestao AS \"gabaritoResposta.nrQuestao\",  ");
		sb.append(" gabaritoResposta.respostaCorreta AS \"gabaritoResposta.respostaCorreta\", gabaritoResposta.valorNota AS \"gabaritoResposta.valorNota\", ");
		sb.append(" gabaritoResposta.anulado AS \"gabaritoResposta.anulado\", gabaritoResposta.historicoanulado AS \"gabaritoResposta.historicoanulado\", ");
		sb.append("gabarito.controlarGabaritoPorDisciplina, gabarito.grupoDisciplinaProcSeletivo, gabaritoResposta.disciplinasProcSeletivo, ");
		sb.append(" unidadeEnsino.codigo AS \"unidadeEnsino.codigo\", unidadeEnsino.nome AS \"unidadeEnsino.nome\", ");
		sb.append(" curso.codigo AS \"curso.codigo\", curso.nome AS \"curso.nome\", curso.periodicidade AS \"curso.periodicidade\", ");
		sb.append(" gradeCurricular.codigo AS \"gradeCurricular.codigo\", gradeCurricular.nome AS \"gradeCurricular.nome\", ");
		sb.append(" periodoLetivo.codigo AS \"periodoLetivo.codigo\", periodoLetivo.descricao AS \"periodoLetivo.descricao\", periodoLetivo.periodoLetivo AS \"periodoLetivo.periodoLetivo\", ");
		sb.append(" turno.codigo AS \"turno.codigo\", turno.nome AS \"turno.nome\", ");
		sb.append(" configuracaoAcademico.codigo AS \"configuracaoAcademico.codigo\", configuracaoAcademico.nome AS \"configuracaoAcademico.nome\", ");
		sb.append(" variavelNota, tipoGabarito, realizarCalculoMediaLancamentoNota, tipoRespostaGabarito, tipoCalculoGabarito, ");
		sb.append(" disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", ");
		sb.append(" areaConhecimento.codigo AS \"areaConhecimento.codigo\", areaConhecimento.nome AS \"areaConhecimento.nome\", gabarito.ano, gabarito.semestre ");

		sb.append(" from gabarito ");
		sb.append(" inner join gabaritoResposta on gabaritoResposta.gabarito = gabarito.codigo");
		sb.append(" LEFT JOIN disciplina ON disciplina.codigo = gabaritoResposta.disciplina ");
		sb.append(" LEFT JOIN areaConhecimento ON areaConhecimento.codigo = gabaritoResposta.areaConhecimento ");
		sb.append("");

		sb.append(" left join unidadeEnsino on unidadeEnsino.codigo = gabarito.unidadeEnsino ");
		sb.append(" left join curso on curso.codigo = gabarito.curso ");
		sb.append(" left join gradeCurricular on gradeCurricular.codigo = gabarito.gradeCurricular ");
		sb.append(" left join periodoLetivo on periodoLetivo.codigo = gabarito.periodoLetivo ");
		sb.append(" left join turno on turno.codigo = gabarito.turno ");
		sb.append(" left join configuracaoAcademico on configuracaoAcademico.codigo = gabarito.configuracaoAcademico ");

		sb.append(" where gabarito.codigo = ").append(codigo);
		sb.append(" order by gabaritoResposta.nrQuestao ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		GabaritoVO obj = new GabaritoVO();
		if (tabelaResultado.next()) {
			montarDados(tabelaResultado, obj, usuarioVO);
		}
		return obj;
	}

	public GabaritoVO consultarCodigoGabaritoPorInscricao(Integer inscricao, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql = new StringBuilder("Select gabarito.codigo, gabarito.descricao, gabarito.quantidadeQuestao, gabarito.responsavel, gabarito.quantidadeQuestao, controlarGabaritoPorDisciplina ");
		sql.append(" from inscricao ");
		sql.append(" inner join ItemProcSeletivoDataProva on ItemProcSeletivoDataProva.codigo =  inscricao.itemProcessoSeletivoDataProva");
		sql.append(" inner join procseletivoGabaritodata on procseletivoGabaritodata.ItemProcSeletivoDataProva =  ItemProcSeletivoDataProva.codigo  ");
		sql.append(" and (procseletivoGabaritodata.disciplinaidioma =  inscricao.opcaolinguaestrangeira or (procseletivoGabaritodata.disciplinaidioma is null and inscricao.opcaolinguaestrangeira is null)) ");
		sql.append(" inner join gabarito on gabarito.codigo = procseletivoGabaritodata.gabarito ");
		sql.append(" where  inscricao.codigo = ").append(inscricao);
		sql.append(" and gabarito.tipoGabarito = '").append(TipoGabaritoEnum.PROCESSO_SELETIVO.name()).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		GabaritoVO obj = new GabaritoVO();
		if (rs.next()) {
			obj.setCodigo(rs.getInt("codigo"));
			obj.setDescricao(rs.getString("descricao"));
			obj.getResponsavel().setCodigo(rs.getInt("responsavel"));
			obj.setQuantidadeQuestao(rs.getInt("quantidadeQuestao"));
			obj.setControlarGabaritoPorDisciplina(rs.getBoolean("controlarGabaritoPorDisciplina"));
		}
		return obj;
	}

	public List<GabaritoVO> consultaRapidaPorDescricao(String descricao, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo, descricao, quantidadeQuestao ");
		sb.append(" from gabarito ");
		sb.append(" where descricao ilike('").append(descricao).append("%') order by descricao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<GabaritoVO> listaGabaritoVOs = new ArrayList<GabaritoVO>(0);
		while (tabelaResultado.next()) {
			GabaritoVO obj = new GabaritoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDescricao(tabelaResultado.getString("descricao"));
			obj.setQuantidadeQuestao(tabelaResultado.getInt("quantidadeQuestao"));
			listaGabaritoVOs.add(obj);
		}
		return listaGabaritoVOs;
	}

	public List<GabaritoVO> consultaRapidaPorDescricaoTipoGabarito(String descricao, TipoGabaritoEnum tipoGabaritoEnum, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select codigo, descricao, quantidadeQuestao ");
		sb.append(" from gabarito ");
		sb.append(" where descricao ilike('").append(descricao).append("%') ");
		if (tipoGabaritoEnum != null) {
			sb.append(" and gabarito.tipoGabarito = '").append(tipoGabaritoEnum.name()).append("' ");
		}
		sb.append(" order by descricao");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<GabaritoVO> listaGabaritoVOs = new ArrayList<GabaritoVO>(0);
		while (tabelaResultado.next()) {
			GabaritoVO obj = new GabaritoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDescricao(tabelaResultado.getString("descricao"));
			obj.setQuantidadeQuestao(tabelaResultado.getInt("quantidadeQuestao"));
			listaGabaritoVOs.add(obj);
		}
		return listaGabaritoVOs;
	}

	public List<GabaritoVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuarioVO) throws Exception {
		List<GabaritoVO> vetResultado = new ArrayList<GabaritoVO>(0);
		while (tabelaResultado.next()) {
			GabaritoVO obj = new GabaritoVO();
			montarDados(tabelaResultado, obj, usuarioVO);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	public void montarDados(SqlRowSet dadosSQL, GabaritoVO obj, UsuarioVO usuarioVO) throws Exception {
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setQuantidadeQuestao(dadosSQL.getInt("quantidadeQuestao"));
		obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));

		obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
		obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeEnsino.nome"));

		obj.getCursoVO().setCodigo(dadosSQL.getInt("curso.codigo"));
		obj.getCursoVO().setNome(dadosSQL.getString("curso.nome"));
		obj.getCursoVO().setPeriodicidade(dadosSQL.getString("curso.periodicidade"));

		obj.getGradeCurricularVO().setCodigo(dadosSQL.getInt("gradeCurricular.codigo"));
		obj.getGradeCurricularVO().setNome(dadosSQL.getString("gradeCurricular.nome"));

		obj.getPeriodoLetivoVO().setCodigo(dadosSQL.getInt("periodoLetivo.codigo"));
		obj.getPeriodoLetivoVO().setDescricao(dadosSQL.getString("periodoLetivo.descricao"));
		obj.getPeriodoLetivoVO().setPeriodoLetivo(dadosSQL.getInt("periodoLetivo.periodoLetivo"));

		obj.getTurnoVO().setCodigo(dadosSQL.getInt("turno.codigo"));
		obj.getTurnoVO().setNome(dadosSQL.getString("turno.nome"));

		obj.getConfiguracaoAcademicoVO().setCodigo(dadosSQL.getInt("configuracaoAcademico.codigo"));
		obj.getConfiguracaoAcademicoVO().setNome(dadosSQL.getString("configuracaoAcademico.nome"));

		obj.setVariavelNota(dadosSQL.getString("variavelNota"));
		if (dadosSQL.getString("tipoGabarito") != null) {
			obj.setTipoGabaritoEnum(TipoGabaritoEnum.valueOf(dadosSQL.getString("tipoGabarito")));
		}
		if (dadosSQL.getString("tipoRespostaGabarito") != null) {
			obj.setTipoRespostaGabaritoEnum(TipoRespostaGabaritoEnum.valueOf(dadosSQL.getString("tipoRespostaGabarito")));
		}
		if (dadosSQL.getString("tipoCalculoGabarito") != null) {
			obj.setTipoCalculoGabaritoEnum(TipoCalculoGabaritoEnum.valueOf(dadosSQL.getString("tipoCalculoGabarito")));
		}
		obj.setRealizarCalculoMediaLancamentoNota(dadosSQL.getBoolean("realizarCalculoMediaLancamentoNota"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.setTamanhoNrMatriculaArquivo(dadosSQL.getInt("tamanhoNrMatriculaArquivo"));
		obj.setControlarGabaritoPorDisciplina(dadosSQL.getBoolean("controlarGabaritoPorDisciplina"));
		obj.getGrupoDisciplinaProcSeletivoVO().setCodigo(dadosSQL.getInt("grupoDisciplinaProcSeletivo"));
		if (Uteis.isAtributoPreenchido(obj.getGrupoDisciplinaProcSeletivoVO())) {
			obj.setGrupoDisciplinaProcSeletivoVO(getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().consultarPorChavePrimaria(obj.getGrupoDisciplinaProcSeletivoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null));
		}
		obj.setNovoObj(Boolean.FALSE);
		GabaritoRespostaVO gabaritoRespostaVO = null;
		obj.getGabaritoRespostaVOs().clear();
		do {
			gabaritoRespostaVO = new GabaritoRespostaVO();
			gabaritoRespostaVO.setCodigo(dadosSQL.getInt("gabaritoResposta.codigo"));
			gabaritoRespostaVO.setNrQuestao(dadosSQL.getInt("gabaritoResposta.nrQuestao"));
			gabaritoRespostaVO.setRespostaCorreta(dadosSQL.getString("gabaritoResposta.respostaCorreta"));
			gabaritoRespostaVO.setValorNota(dadosSQL.getBigDecimal("gabaritoResposta.valorNota"));
			gabaritoRespostaVO.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina.codigo"));
			gabaritoRespostaVO.getDisciplinaVO().setNome(dadosSQL.getString("disciplina.nome"));

			gabaritoRespostaVO.getAreaConhecimentoVO().setCodigo(dadosSQL.getInt("areaConhecimento.codigo"));
			gabaritoRespostaVO.getAreaConhecimentoVO().setNome(dadosSQL.getString("areaConhecimento.nome"));
			gabaritoRespostaVO.getDisciplinasProcSeletivoVO().setCodigo(dadosSQL.getInt("disciplinasProcSeletivo"));
			gabaritoRespostaVO.setAnulado(dadosSQL.getBoolean("gabaritoResposta.anulado"));
			gabaritoRespostaVO.setHistoricoAnulado(dadosSQL.getString("gabaritoResposta.historicoanulado"));
			
			obj.getGabaritoRespostaVOs().add(gabaritoRespostaVO);
			if (dadosSQL.isLast() || (obj.getCodigo() != (dadosSQL.getInt("codigo")))) {
				return;
			}
		} while (dadosSQL.next());

	}

	public List<GabaritoVO> consultar(String campoConsulta, String valorConsulta, UsuarioVO usuarioVO) {
		List<GabaritoVO> objs = new ArrayList<GabaritoVO>(0);
		if (campoConsulta.equals("descricao")) {
			objs = consultaRapidaPorDescricao(valorConsulta, usuarioVO);
		}
		return objs;
	}

	public void adicionarGabaritoResposta(GabaritoVO obj, BigDecimal valorNotaPorQuestao, UsuarioVO usuarioVO) {
		if(obj.getQuantidadeQuestao() < obj.getGabaritoRespostaVOs().size()){
			for (Iterator<GabaritoRespostaVO> iterator = obj.getGabaritoRespostaVOs().iterator(); iterator.hasNext();) {
				GabaritoRespostaVO gabaritoRespostaVO = (GabaritoRespostaVO) iterator.next();
				if(gabaritoRespostaVO.getNrQuestao() > obj.getQuantidadeQuestao()){
					iterator.remove();
				}
			}
		}
		//obj.getGabaritoRespostaVOs().clear();
		obj.getColunaGabaritoVOs().clear();
		int cont = 1;
		obj.setQtdeColuna(1);
		int qtdeColunas = Uteis.arredondarParaMais(obj.getQuantidadeQuestao()/4);
		ColunaGabaritoVO colunaGabaritoVO = null;
		q:
		for (int i = 1; i <= obj.getQuantidadeQuestao(); i++) {
			if (cont == 1) {
				colunaGabaritoVO = new ColunaGabaritoVO();
				colunaGabaritoVO.setColuna(obj.getQtdeColuna());
			}
			for (Iterator<GabaritoRespostaVO> iterator = obj.getGabaritoRespostaVOs().iterator(); iterator.hasNext();) {
				GabaritoRespostaVO gabaritoRespostaVO = (GabaritoRespostaVO) iterator.next();
				if(gabaritoRespostaVO.getNrQuestao() == i){
					colunaGabaritoVO.getGabaritoRespostaVOs().add(gabaritoRespostaVO);
					if (cont == qtdeColunas || (i == obj.getQuantidadeQuestao())) {
						obj.setQtdeColuna(obj.getQtdeColuna() + 1);
						cont = 1;
						obj.getColunaGabaritoVOs().add(colunaGabaritoVO);
					} else {
						cont++;
					}
					continue q;
				}
			}
			
			GabaritoRespostaVO gabaritoRespostaVO = new GabaritoRespostaVO();
			gabaritoRespostaVO.setNrQuestao(i);
			gabaritoRespostaVO.setRespostaCorreta("");
			gabaritoRespostaVO.setValorNota(Uteis.trunc(valorNotaPorQuestao, 2));
			colunaGabaritoVO.getGabaritoRespostaVOs().add(gabaritoRespostaVO);
			obj.getGabaritoRespostaVOs().add(gabaritoRespostaVO);
			if (cont == 10 || (i == obj.getQuantidadeQuestao())) {
				obj.setQtdeColuna(obj.getQtdeColuna() + 1);
				cont = 1;
				obj.getColunaGabaritoVOs().add(colunaGabaritoVO);
			} else {
				cont++;
			}
		}
	}

	public void editarColunaGabaritoResposta(GabaritoVO obj, UsuarioVO usuarioVO) {
		obj.getColunaGabaritoVOs().clear();
		int cont = 1;
		int qtdeColuna = 1;
		int qtdeQuestao = 1;
		ColunaGabaritoVO colunaGabaritoVO = null;
		for (GabaritoRespostaVO gabaritoRespostaVO : obj.getGabaritoRespostaVOs()) {
			if (cont == 1) {
				colunaGabaritoVO = new ColunaGabaritoVO();
				colunaGabaritoVO.setColuna(qtdeColuna);
			}
			if (cont == 10 || qtdeQuestao == obj.getQuantidadeQuestao()) {
				obj.setQtdeColuna(qtdeColuna + 1);
				colunaGabaritoVO.getGabaritoRespostaVOs().add(gabaritoRespostaVO);
				obj.getColunaGabaritoVOs().add(colunaGabaritoVO);
				cont = 1;
			} else {
				colunaGabaritoVO.getGabaritoRespostaVOs().add(gabaritoRespostaVO);
				cont++;
			}
			qtdeQuestao++;
		}
		// }
	}

	public static String getIdEntidade() {
		return Gabarito.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		Gabarito.idEntidade = idEntidade;
	}
	
	/**
	 * Responsável por executar a definição do gabarito a ser utilizado levando em consideração se o mesmo está vínculado a um
	 * grupoDisciplinaProcSeletivo, se o ProcSeletivoCurso também está vínculado a um grupoDisciplinaProcSeletivo e se os dois tem o
	 * grupoDisciplinaProcSeletivo em comum.
	 * 
	 * @author Wellington - 5 de jan de 2016
	 * @param inscricao
	 * @return
	 * @throws Exception
	 */
	@Override
	public Integer consultarCodigoGabaritoPorInscricaoPrivilegiandoGrupoDisciplinaProcSeletivo(Integer inscricao) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append("select gabarito.codigo as gabarito ");
		sql.append("from inscricao ");
		sql.append("inner join ItemProcSeletivoDataProva on itemprocseletivodataprova.codigo =  inscricao.itemProcessoSeletivoDataProva ");
		sql.append("inner join procseletivoGabaritodata on procseletivoGabaritodata.ItemProcSeletivoDataProva = ItemProcSeletivoDataProva.codigo ");
		sql.append("	and ( ");
		sql.append("		(inscricao.opcaolinguaestrangeira is null and procseletivoGabaritodata.disciplinaidioma is null) ");
		sql.append("		or (procseletivoGabaritodata.disciplinaidioma = inscricao.opcaolinguaestrangeira) ");
		sql.append("	) ");
		sql.append("inner join gabarito on gabarito.codigo = procseletivoGabaritodata.gabarito ");
		sql.append("left join procSeletivoUnidadeEnsino on procSeletivoUnidadeEnsino.procseletivo = inscricao.procseletivo ");
		sql.append("left join ProcSeletivoCurso on ProcSeletivoCurso.procSeletivoUnidadeEnsino = procSeletivoUnidadeEnsino.codigo and ( ");
		sql.append("	ProcSeletivoCurso.unidadeensinocurso = inscricao.cursoopcao1 or ProcSeletivoCurso.unidadeensinocurso = inscricao.cursoopcao2 or ProcSeletivoCurso.unidadeensinocurso = inscricao.cursoopcao3 ");
		sql.append(") ");
		sql.append("where inscricao.codigo = ").append(inscricao);
		sql.append(" order by ( ");
		sql.append("	case when ProcSeletivoCurso.grupodisciplinaprocseletivo is not null ");
		sql.append("	and gabarito.grupodisciplinaprocseletivo is not null ");
		sql.append("	and ProcSeletivoCurso.grupodisciplinaprocseletivo = gabarito.grupodisciplinaprocseletivo then 1 ");
		sql.append("	else 2 end ");
		sql.append(") ");
		sql.append("limit 1");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getInt("gabarito");
		}
		return 0;
	}

}
