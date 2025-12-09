package negocio.facade.jdbc.processosel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.processosel.OpcaoRespostaQuestaoProcessoSeletivoVO;
import negocio.comuns.processosel.QuestaoProcessoSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.processosel.QuestaoProcessoSeletivoInterfaceFacade;

@Repository
public class QuestaoProcessoSeletivo extends ControleAcesso implements QuestaoProcessoSeletivoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarDados(questaoProcessoSeletivoVO);
		if (questaoProcessoSeletivoVO.isNovoObj()) {
			incluir(questaoProcessoSeletivoVO, controlarAcesso, usuarioVO);
		} else {
			alterar(questaoProcessoSeletivoVO, controlarAcesso, usuarioVO);
		}
		for (OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO : questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
			opcaoRespostaQuestaoProcessoSeletivoVO.setEditar(false);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		incluir(getIdEntidade(), controlarAcesso, usuarioVO);
		questaoProcessoSeletivoVO.setDataCriacao(new Date());
		questaoProcessoSeletivoVO.setResponsavelCriacao(usuarioVO);
		try {
			final StringBuilder sql = new StringBuilder("INSERT INTO questaoProcessoSeletivo ");
			sql.append(" ( enunciado, justificativa, ajuda, situacaoQuestaoEnum, ");
			sql.append(" dataCriacao, responsavelCriacao, nivelComplexidadeQuestao, disciplinaProcSeletivo");
			sql.append(" ) VALUES (?,?,?,?,?,?,?,?) ");
			sql.append(" returning codigo ");
			questaoProcessoSeletivoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setString(x++, questaoProcessoSeletivoVO.getEnunciado());
					ps.setString(x++, questaoProcessoSeletivoVO.getJustificativa());
					ps.setString(x++, questaoProcessoSeletivoVO.getAjuda());
					ps.setString(x++, questaoProcessoSeletivoVO.getSituacaoQuestaoEnum().name());
					ps.setDate(x++, Uteis.getDataJDBC(questaoProcessoSeletivoVO.getDataCriacao()));
					ps.setInt(x++, questaoProcessoSeletivoVO.getResponsavelCriacao().getCodigo());
					ps.setString(x++, questaoProcessoSeletivoVO.getNivelComplexidadeQuestao().name());
					ps.setInt(x++, questaoProcessoSeletivoVO.getDisciplinaProcSeletivo().getCodigo());
					return ps;
				}
			}, new ResultSetExtractor<Integer>() {

				@Override
				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						return arg0.getInt("codigo");
					}
					return null;
				}

			}));
			getFacadeFactory().getOpcaoRespostaQuestaoProcessoSeletivoFacade().incluirOpcaoRespostaQuestao(questaoProcessoSeletivoVO);
			questaoProcessoSeletivoVO.setNovoObj(false);
		} catch (Exception e) {
			questaoProcessoSeletivoVO.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		alterar(getIdEntidade(), controlarAcesso, usuarioVO);
		questaoProcessoSeletivoVO.setDataAlteracao(new Date());
		questaoProcessoSeletivoVO.setResponsavelAlteracao(usuarioVO);
		try {
			final StringBuilder sql = new StringBuilder("UPDATE questaoProcessoSeletivo set ");
			sql.append(" enunciado = ?,");
			sql.append(" justificativa = ?, ajuda = ?, situacaoQuestaoEnum = ?, ");
			sql.append(" nivelComplexidadeQuestao = ?, disciplinaProcSeletivo = ?, ");
			sql.append(" dataAlteracao = ?, responsavelAlteracao = ?, motivoCancelamento = ? ");
			sql.append(" where codigo = ? ");
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setString(x++, questaoProcessoSeletivoVO.getEnunciado());
					ps.setString(x++, questaoProcessoSeletivoVO.getJustificativa());
					ps.setString(x++, questaoProcessoSeletivoVO.getAjuda());
					ps.setString(x++, questaoProcessoSeletivoVO.getSituacaoQuestaoEnum().name());
					ps.setString(x++, questaoProcessoSeletivoVO.getNivelComplexidadeQuestao().name());
					ps.setInt(x++, questaoProcessoSeletivoVO.getDisciplinaProcSeletivo().getCodigo());
					ps.setDate(x++, Uteis.getDataJDBC(questaoProcessoSeletivoVO.getDataAlteracao()));
					ps.setInt(x++, questaoProcessoSeletivoVO.getResponsavelAlteracao().getCodigo());
					ps.setString(x++, questaoProcessoSeletivoVO.getMotivoCancelamento());
					ps.setInt(x++, questaoProcessoSeletivoVO.getCodigo());
					return ps;
				}
			}) == 0) {
				incluir(questaoProcessoSeletivoVO, controlarAcesso, usuarioVO);
				return;
			}
			getFacadeFactory().getOpcaoRespostaQuestaoProcessoSeletivoFacade().alterarOpcaoRespostaQuestao(questaoProcessoSeletivoVO);
			questaoProcessoSeletivoVO.setNovoObj(false);
		} catch (Exception e) {
			questaoProcessoSeletivoVO.setNovoObj(false);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(final QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (questaoProcessoSeletivoVO.getSituacaoQuestaoEnum().equals(SituacaoQuestaoEnum.EM_ELABORACAO)) {
			excluir(getIdEntidade(), controlarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder("DELETE FROM questaoProcessoSeletivo ");
			sql.append(" where codigo = ? ");
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					ps.setInt(1, questaoProcessoSeletivoVO.getCodigo());
					return ps;
				}
			}) == 0) {
				return;
			}
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void ativarQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		SituacaoQuestaoEnum situacaoQuestaoProcessoSeletivoEnum = questaoProcessoSeletivoVO.getSituacaoQuestaoEnum();
		try {
			questaoProcessoSeletivoVO.setDataAlteracao(new Date());
			questaoProcessoSeletivoVO.setResponsavelAlteracao(usuarioVO);
			questaoProcessoSeletivoVO.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.ATIVA);
			persistir(questaoProcessoSeletivoVO, controlarAcesso, usuarioVO);
		} catch (Exception e) {
			questaoProcessoSeletivoVO.setSituacaoQuestaoEnum(situacaoQuestaoProcessoSeletivoEnum);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void inativarQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		SituacaoQuestaoEnum situacaoQuestaoProcessoSeletivoEnum = questaoProcessoSeletivoVO.getSituacaoQuestaoEnum();
		try {
			questaoProcessoSeletivoVO.setDataAlteracao(new Date());
			questaoProcessoSeletivoVO.setResponsavelAlteracao(usuarioVO);
			questaoProcessoSeletivoVO.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.INATIVA);
			persistir(questaoProcessoSeletivoVO, controlarAcesso, usuarioVO);
		} catch (Exception e) {
			questaoProcessoSeletivoVO.setSituacaoQuestaoEnum(situacaoQuestaoProcessoSeletivoEnum);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void cancelarQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, Boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		SituacaoQuestaoEnum situacaoQuestaoProcessoSeletivoEnum = questaoProcessoSeletivoVO.getSituacaoQuestaoEnum();
		try {
			questaoProcessoSeletivoVO.setDataAlteracao(new Date());
			questaoProcessoSeletivoVO.setResponsavelAlteracao(usuarioVO);
			questaoProcessoSeletivoVO.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.CANCELADA);
			persistir(questaoProcessoSeletivoVO, controlarAcesso, usuarioVO);
		} catch (Exception e) {
			questaoProcessoSeletivoVO.setSituacaoQuestaoEnum(situacaoQuestaoProcessoSeletivoEnum);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public QuestaoProcessoSeletivoVO clonarQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO) throws Exception {
		return questaoProcessoSeletivoVO.clone();
	}

	public String getSelectDadosBasicos() {
		StringBuilder sb = new StringBuilder("SELECT questaoProcessoSeletivo.*, ");
		sb.append(" disciplinasProcSeletivo.nome as \"disciplinaProcSeletivo.nome\", ");
		sb.append(" responsavelCriacao.nome as \"responsavelCriacao.nome\", ");
		sb.append(" responsavelAlteracao.nome as \"responsavelAlteracao.nome\" ");
		sb.append(" FROM questaoProcessoSeletivo ");
		sb.append(" inner join disciplinasProcSeletivo on  disciplinasProcSeletivo.codigo = questaoProcessoSeletivo.disciplinaProcSeletivo ");
		sb.append(" inner join usuario as responsavelCriacao on  responsavelCriacao.codigo = questaoProcessoSeletivo.responsavelCriacao ");
		sb.append(" left join usuario as responsavelAlteracao on  responsavelAlteracao.codigo = questaoProcessoSeletivo.responsavelAlteracao ");
		return sb.toString();
	}

	public String getSelectDadosCompleto() {
		StringBuilder sb = new StringBuilder("SELECT questaoProcessoSeletivo.*, ");
		sb.append(" disciplinasProcSeletivo.nome as \"disciplinaProcSeletivo.nome\", ");
		sb.append(" responsavelCriacao.nome as \"responsavelCriacao.nome\", ");
		sb.append(" responsavelAlteracao.nome as \"responsavelAlteracao.nome\", ");
		sb.append(" orp.codigo as \"orp.codigo\", orp.opcaoResposta as \"orp.opcaoResposta\", orp.correta as \"orp.correta\", ");
		sb.append(" orp.questaoProcessoSeletivo as \"orp.questaoProcessoSeletivo\", orp.ordemApresentacao as \"orp.ordemApresentacao\" ");
		sb.append(" FROM questaoProcessoSeletivo ");
		sb.append(" inner join disciplinasProcSeletivo on  disciplinasProcSeletivo.codigo = questaoProcessoSeletivo.disciplinaProcSeletivo ");
		sb.append(" inner join usuario as responsavelCriacao on  responsavelCriacao.codigo = questaoProcessoSeletivo.responsavelCriacao ");
		sb.append(" left join usuario as responsavelAlteracao on  responsavelAlteracao.codigo = questaoProcessoSeletivo.responsavelAlteracao ");
		sb.append(" left join opcaoRespostaQuestaoProcessoSeletivo as orp on  orp.questaoProcessoSeletivo = questaoProcessoSeletivo.codigo ");
		return sb.toString();
	}

	@Override
	public List<QuestaoProcessoSeletivoVO> consultar(String enunciado, Integer disciplinaProcSeletivo, SituacaoQuestaoEnum situacaoQuestaoProcessoSeletivoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoProcessoSeletivoEnum, Boolean controleAcesso, UsuarioVO usuario, Integer limite, Integer pagina) throws Exception {
		consultar(getIdEntidade(), controleAcesso, usuario);
		StringBuilder sb = new StringBuilder(getSelectDadosBasicos());
		sb.append(" WHERE 0=0 ");
		if (enunciado != null && !enunciado.trim().isEmpty()) {
			sb.append(" and remover_tags_html(sem_acentos(sem_acentos_html(questaoProcessoSeletivo.enunciado))) ilike(sem_acentos('%").append(enunciado.trim()).append("%')) ");
		}
		if (disciplinaProcSeletivo != null && disciplinaProcSeletivo > 0) {
			sb.append(" and disciplinasProcSeletivo.codigo = ").append(disciplinaProcSeletivo);
		}
		if (situacaoQuestaoProcessoSeletivoEnum != null) {
			sb.append(" and questaoProcessoSeletivo.situacaoQuestaoEnum = '").append(situacaoQuestaoProcessoSeletivoEnum.name()).append("' ");
		}
		if (nivelComplexidadeQuestaoProcessoSeletivoEnum != null) {
			sb.append(" and questaoProcessoSeletivo.nivelComplexidadeQuestao = '").append(nivelComplexidadeQuestaoProcessoSeletivoEnum.name()).append("' ");
		}
		sb.append(" order by remover_tags_html(sem_acentos(sem_acentos_html(questaoProcessoSeletivo.enunciado))) ");
		if (limite != null && limite > 0) {
			sb.append(" limit ").append(limite).append(" offset ").append(pagina);
		}

		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	private List<QuestaoProcessoSeletivoVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados) throws Exception {
		List<QuestaoProcessoSeletivoVO> questaoProcessoSeletivoVOs = new ArrayList<QuestaoProcessoSeletivoVO>(0);
		while (rs.next()) {
			questaoProcessoSeletivoVOs.add(montarDados(rs, nivelMontarDados));
		}
		return questaoProcessoSeletivoVOs;
	}

	private List<QuestaoProcessoSeletivoVO> montarDadosConsultaCompleta(SqlRowSet rs) throws Exception {
		List<QuestaoProcessoSeletivoVO> questaoProcessoSeletivoVOs = new ArrayList<QuestaoProcessoSeletivoVO>(0);
		Map<Integer, QuestaoProcessoSeletivoVO> qMap = new HashMap<Integer, QuestaoProcessoSeletivoVO>(0);
		QuestaoProcessoSeletivoVO obj = null;
		while (rs.next()) {
			if (!qMap.containsKey(rs.getInt("codigo"))) {
				obj = montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
				qMap.put(obj.getCodigo(), obj);
			} else {
				obj = qMap.get(rs.getInt("codigo"));
			}
			if (rs.getInt("orp.codigo") > 0) {
				obj.getOpcaoRespostaQuestaoProcessoSeletivoVOs().add(getFacadeFactory().getOpcaoRespostaQuestaoProcessoSeletivoFacade().montarDados(rs, "orp."));
			}
		}
		questaoProcessoSeletivoVOs.addAll(qMap.values());
		return questaoProcessoSeletivoVOs;
	}

	private QuestaoProcessoSeletivoVO montarDados(SqlRowSet rs, int nivelMontarDados) throws Exception {
		QuestaoProcessoSeletivoVO obj = new QuestaoProcessoSeletivoVO();
		obj.setNovoObj(false);
		obj.setCodigo(rs.getInt("codigo"));
		obj.setDataCriacao(rs.getDate("dataCriacao"));
		obj.setDataAlteracao(rs.getDate("dataAlteracao"));
		obj.getDisciplinaProcSeletivo().setCodigo(rs.getInt("disciplinaProcSeletivo"));
		obj.getDisciplinaProcSeletivo().setNome(rs.getString("disciplinaProcSeletivo.nome"));
		if (rs.getInt("responsavelAlteracao") > 0) {
			obj.setResponsavelAlteracao(new UsuarioVO());
			obj.getResponsavelAlteracao().setCodigo(rs.getInt("responsavelAlteracao"));
			obj.getResponsavelAlteracao().setNome(rs.getString("responsavelAlteracao.nome"));
		}
		obj.setResponsavelCriacao(new UsuarioVO());
		obj.getResponsavelCriacao().setCodigo(rs.getInt("responsavelCriacao"));
		obj.getResponsavelCriacao().setNome(rs.getString("responsavelCriacao.nome"));
		obj.setEnunciado(rs.getString("enunciado"));
		obj.setAjuda(rs.getString("ajuda"));
		obj.setJustificativa(rs.getString("justificativa"));
		obj.setMotivoCancelamento(rs.getString("motivoCancelamento"));
		obj.setNivelComplexidadeQuestao(NivelComplexidadeQuestaoEnum.valueOf(rs.getString("nivelComplexidadeQuestao")));
		obj.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.valueOf(rs.getString("situacaoQuestaoEnum")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setOpcaoRespostaQuestaoProcessoSeletivoVOs(getFacadeFactory().getOpcaoRespostaQuestaoProcessoSeletivoFacade().consultarPorQuestao(obj.getCodigo()));
		return obj;
	}

	@Override
	public Integer consultarTotalResgistro(String enunciado, Integer disciplinaProcSeletivo, SituacaoQuestaoEnum situacaoQuestaoProcessoSeletivoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoProcessoSeletivoEnum) throws Exception {
		StringBuilder sb = new StringBuilder("Select count(codigo) as qtde from questaoProcessoSeletivo ");
		sb.append(" WHERE 0=0 ");
		if (enunciado != null && !enunciado.trim().isEmpty()) {
//			sb.append(" and upper(sem_acentos(questaoProcessoSeletivo.enunciado)) like(upper(sem_acentos('%").append(enunciado.trim()).append("%'))) ");
			sb.append(" and remover_tags_html(sem_acentos(sem_acentos_html(questaoProcessoSeletivo.enunciado))) ilike(sem_acentos('%").append(enunciado.trim()).append("%')) ");
		}
		if (disciplinaProcSeletivo != null && disciplinaProcSeletivo > 0) {
			sb.append(" and disciplinaProcSeletivo = ").append(disciplinaProcSeletivo);
		}
		if (situacaoQuestaoProcessoSeletivoEnum != null) {
			sb.append(" and questaoProcessoSeletivo.situacaoQuestaoEnum = '").append(situacaoQuestaoProcessoSeletivoEnum.name()).append("' ");
		}
		if (nivelComplexidadeQuestaoProcessoSeletivoEnum != null) {
			sb.append(" and questaoProcessoSeletivo.nivelComplexidadeQuestao = '").append(nivelComplexidadeQuestaoProcessoSeletivoEnum.name()).append("' ");
		}

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public void novo(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO) {
		questaoProcessoSeletivoVO.setNovoObj(true);
		questaoProcessoSeletivoVO.setCodigo(0);
		questaoProcessoSeletivoVO.setEnunciado("");
		questaoProcessoSeletivoVO.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.EM_ELABORACAO);
		questaoProcessoSeletivoVO.setNivelComplexidadeQuestao(NivelComplexidadeQuestaoEnum.MEDIO);
		questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs().clear();

		OpcaoRespostaQuestaoProcessoSeletivoVO opc = new OpcaoRespostaQuestaoProcessoSeletivoVO();
		opc.setOrdemApresentacao(1);
		opc.setEditar(false);
		questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs().add(opc);
		opc = new OpcaoRespostaQuestaoProcessoSeletivoVO();
		opc.setOrdemApresentacao(2);
		opc.setEditar(false);
		questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs().add(opc);
		opc = new OpcaoRespostaQuestaoProcessoSeletivoVO();
		opc.setOrdemApresentacao(3);
		opc.setEditar(false);
		questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs().add(opc);
		opc = new OpcaoRespostaQuestaoProcessoSeletivoVO();
		opc.setOrdemApresentacao(4);
		opc.setEditar(false);
		questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs().add(opc);
		opc = new OpcaoRespostaQuestaoProcessoSeletivoVO();
		opc.setOrdemApresentacao(5);
		opc.setEditar(false);
		questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs().add(opc);

	}

	@Override
	public void adicionarOpcaoRespostaQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, Boolean validarDados, OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO) throws Exception {
		if (validarDados) {
			getFacadeFactory().getOpcaoRespostaQuestaoProcessoSeletivoFacade().validarDados(opcaoRespostaQuestaoProcessoSeletivoVO);
		}
		if (opcaoRespostaQuestaoProcessoSeletivoVO.getOrdemApresentacao() == null || opcaoRespostaQuestaoProcessoSeletivoVO.getOrdemApresentacao() == 0) {
			opcaoRespostaQuestaoProcessoSeletivoVO.setOrdemApresentacao(questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs().size() + 1);
			opcaoRespostaQuestaoProcessoSeletivoVO.setEditar(true);
			questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs().add(opcaoRespostaQuestaoProcessoSeletivoVO);
		} else {
			questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs().set(opcaoRespostaQuestaoProcessoSeletivoVO.getOrdemApresentacao() - 1, opcaoRespostaQuestaoProcessoSeletivoVO);
		}
		int x = 1;
		for (OpcaoRespostaQuestaoProcessoSeletivoVO orq : questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
			orq.setLetraCorrespondente(null);
			orq.setOrdemApresentacao(x++);
		}

	}

	@Override
	public void removerOpcaoRespostaQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO) throws Exception {
		if(questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs().size() > 2) {
			questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs().remove(opcaoRespostaQuestaoProcessoSeletivoVO.getOrdemApresentacao() - 1);
			int x = 1;
			for (OpcaoRespostaQuestaoProcessoSeletivoVO orq : questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
				orq.setLetraCorrespondente(null);
				orq.setOrdemApresentacao(x++);
			}
		}else {
			throw new Exception("Não possível Excluir. é necessário ter no mínimo 2 alternativas de resposta!");
		}
		
	}

	@Override
	public void validarDados(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO) throws ConsistirException {
		ConsistirException ce = null;
		if (questaoProcessoSeletivoVO.getDisciplinaProcSeletivo() == null || questaoProcessoSeletivoVO.getDisciplinaProcSeletivo().getCodigo() == null || questaoProcessoSeletivoVO.getDisciplinaProcSeletivo().getCodigo() == 0) {
			ce = ce == null ? new ConsistirException() : ce;
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_QuestaoProcessoSeletivo_disciplinaProcSeletivo"));
		}
		if (Uteis.retiraTags(questaoProcessoSeletivoVO.getEnunciado()).trim().isEmpty()) {
			ce = ce == null ? new ConsistirException() : ce;
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_QuestaoProcessoSeletivo_enunciado"));
		}

		for (OpcaoRespostaQuestaoProcessoSeletivoVO orq : questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
			if (Uteis.retiraTags(orq.getOpcaoResposta()).trim().isEmpty()) {
				ce = ce == null ? new ConsistirException() : ce;
				ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_OpcaoRespostaQuestaoProcessoSeletivo_opcaoReposta").replace("{0}", orq.getLetraCorrespondente()));
			}
		}
		int x = 0;
		for (OpcaoRespostaQuestaoProcessoSeletivoVO orq : questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
			if (orq.getCorreta()) {
				x++;
			}
		}
		if (x == 0) {
			ce = ce == null ? new ConsistirException() : ce;
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_QuestaoProcessoSeletivo_semOpcaoCorreta"));
		}
		if (x > 1) {
			ce = ce == null ? new ConsistirException() : ce;
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_QuestaoProcessoSeletivo_nrOpcaoRespostaCorretaInvalido"));
		}

		if (ce != null) {
			throw ce;
		}

	}

	@Override
	public QuestaoProcessoSeletivoVO consultarPorChavePrimaria(Integer codigo) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectDadosCompleto());
		sb.append(" where questaoProcessoSeletivo.codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<QuestaoProcessoSeletivoVO> questaoProcessoSeletivoVOs = montarDadosConsultaCompleta(rs);
		if (questaoProcessoSeletivoVOs.isEmpty()) {
			throw new Exception("Dados não encontrados(Questão Processo Seletivo).");
		}
		return questaoProcessoSeletivoVOs.get(0);
	}

	@Override
	public void alterarOrdemOpcaoRespostaQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO, OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivo1, OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivo2) throws Exception {
		int ordem1 = opcaoRespostaQuestaoProcessoSeletivo1.getOrdemApresentacao();
		opcaoRespostaQuestaoProcessoSeletivo1.setOrdemApresentacao(opcaoRespostaQuestaoProcessoSeletivo2.getOrdemApresentacao());
		opcaoRespostaQuestaoProcessoSeletivo2.setOrdemApresentacao(ordem1);
		opcaoRespostaQuestaoProcessoSeletivo2.setLetraCorrespondente(null);
		opcaoRespostaQuestaoProcessoSeletivo1.setLetraCorrespondente(null);
		Ordenacao.ordenarLista(questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs(), "ordemApresentacao");
	}

	@Override
	public List<QuestaoProcessoSeletivoVO> consultarQuestoesPorDisciplinaRandimicamente(Integer disciplinaProcSeletivo, Integer qtdeComplexidadeQuestoesFacil, Integer qtdeComplexidadeQuestoesMedio, Integer qtdeComplexidadeQuestoesDificil, Integer qtdeQualquerComplexidade) throws Exception {
		List<QuestaoProcessoSeletivoVO> questaoProcessoSeletivoVOs = new ArrayList<QuestaoProcessoSeletivoVO>(0);

		StringBuilder sb = null;
		SqlRowSet rs = null;
		if ((qtdeComplexidadeQuestoesFacil != null && qtdeComplexidadeQuestoesFacil > 0) || (qtdeComplexidadeQuestoesMedio != null && qtdeComplexidadeQuestoesMedio > 0) || (qtdeComplexidadeQuestoesDificil != null && qtdeComplexidadeQuestoesDificil > 0)) {
			sb = new StringBuilder(getSelectDadosCompleto());
			sb.append(" where questaoProcessoSeletivo.codigo in (");
			String unionAll = "";
			sb.append(" SELECT codigo FROM ( ");
			if ((qtdeComplexidadeQuestoesFacil != null && qtdeComplexidadeQuestoesFacil > 0)) {
				sb.append(" (select codigo from questaoProcessoSeletivo  where situacaoquestaoProcessoSeletivoenum = 'ATIVA' and nivelcomplexidadequestaoProcessoSeletivo = 'FACIL'");
				sb.append(" and disciplinaProcSeletivo = ").append(disciplinaProcSeletivo).append(" order by random() limit ").append(qtdeComplexidadeQuestoesFacil).append(" ) ");
				unionAll = " union all ";
			}
			if ((qtdeComplexidadeQuestoesMedio != null && qtdeComplexidadeQuestoesMedio > 0)) {
				sb.append(unionAll);
				sb.append(" (select codigo from questaoProcessoSeletivo  where situacaoquestaoProcessoSeletivoenum = 'ATIVA' and nivelcomplexidadequestaoProcessoSeletivo = 'MEDIO'");
				sb.append(" and disciplinaProcSeletivo = ").append(disciplinaProcSeletivo).append(" order by random() limit ").append(qtdeComplexidadeQuestoesMedio).append(" ) ");
				unionAll = " union all ";
			}
			if ((qtdeComplexidadeQuestoesDificil != null && qtdeComplexidadeQuestoesDificil > 0)) {
				sb.append(unionAll);
				sb.append(" (select codigo from questaoProcessoSeletivo  where situacaoquestaoProcessoSeletivoenum = 'ATIVA' and nivelcomplexidadequestaoProcessoSeletivo = 'DIFICIL'");
				sb.append(" and disciplinaProcSeletivo = ").append(disciplinaProcSeletivo).append(" order by random() limit ").append(qtdeComplexidadeQuestoesDificil).append(" ) ");
			}
			unionAll = null;
			sb.append(" ) as t order by random() ");
			sb.append(" ) ");
			rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			questaoProcessoSeletivoVOs.addAll(montarDadosConsultaCompleta(rs));
		}
		if ((qtdeQualquerComplexidade != null && qtdeQualquerComplexidade > 0)) {
			sb = new StringBuilder(getSelectDadosCompleto());
			sb.append(" where questaoProcessoSeletivo.codigo in (");
			sb.append(" (select codigo from questaoProcessoSeletivo  where situacaoquestaoProcessoSeletivoenum = 'ATIVA'");
			sb.append(" and codigo not in ( 0 ");
			for (QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO : questaoProcessoSeletivoVOs) {
				sb.append(", ").append(questaoProcessoSeletivoVO.getCodigo());
			}
			sb.append(" ) ");
			sb.append(" and disciplinaProcSeletivo = ").append(disciplinaProcSeletivo).append(" order by random() limit ").append(qtdeQualquerComplexidade).append(" ) ");
			sb.append(" )");
			questaoProcessoSeletivoVOs.addAll(montarDadosConsultaCompleta(rs));
		}

		return questaoProcessoSeletivoVOs;
	}

	@Override
	public void realizarCorrecaoQuestao(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO) {
		questaoProcessoSeletivoVO.setAcertouQuestao(true);
		questaoProcessoSeletivoVO.setErrouQuestao(false);
		questaoProcessoSeletivoVO.setQuestaoNaoRespondida(true);
		for (OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO : questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
			if (opcaoRespostaQuestaoProcessoSeletivoVO.getCorreta() != opcaoRespostaQuestaoProcessoSeletivoVO.getMarcada()) {
				questaoProcessoSeletivoVO.setAcertouQuestao(false);
				questaoProcessoSeletivoVO.setErrouQuestao(true);
			}
			if (opcaoRespostaQuestaoProcessoSeletivoVO.getMarcada()) {
				questaoProcessoSeletivoVO.setQuestaoNaoRespondida(false);
			}
		}
		if (questaoProcessoSeletivoVO.getQuestaoNaoRespondida()) {
			questaoProcessoSeletivoVO.setAcertouQuestao(false);
			questaoProcessoSeletivoVO.setErrouQuestao(false);
		}

	}

	@Override
	public void realizarVerificacaoQuestaoRespondida(QuestaoProcessoSeletivoVO questaoProcessoSeletivoVO) {
		questaoProcessoSeletivoVO.setQuestaoNaoRespondida(false);
		for (OpcaoRespostaQuestaoProcessoSeletivoVO opcaoRespostaQuestaoProcessoSeletivoVO : questaoProcessoSeletivoVO.getOpcaoRespostaQuestaoProcessoSeletivoVOs()) {
			if (opcaoRespostaQuestaoProcessoSeletivoVO.getMarcada()) {
				questaoProcessoSeletivoVO.setQuestaoNaoRespondida(false);
				return;
			}
		}
		questaoProcessoSeletivoVO.setQuestaoNaoRespondida(true);
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "QuestaoProcessoSeletivo";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		QuestaoProcessoSeletivo.idEntidade = idEntidade;
	}

	@Override
	public List<QuestaoProcessoSeletivoVO> consultarPoGrupoDisciplinaProcSeletivo(String enunciado, Integer disciplinaProcSeletivo, Integer grupoDisciplinaProcSeletivo, SituacaoQuestaoEnum situacaoQuestaoProcessoSeletivoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoProcessoSeletivoEnum, Boolean controleAcesso, UsuarioVO usuario, Integer limite, Integer pagina) throws Exception {
		consultar(getIdEntidade(), controleAcesso, usuario);
		StringBuilder sb = new StringBuilder("SELECT distinct questaoProcessoSeletivo.*, ");
		sb.append("disciplinasProcSeletivo.nome as \"disciplinaProcSeletivo.nome\", ");
		sb.append("responsavelCriacao.nome as \"responsavelCriacao.nome\", ");
		sb.append("responsavelAlteracao.nome as \"responsavelAlteracao.nome\" ");
		sb.append("FROM questaoProcessoSeletivo ");
		sb.append("inner join disciplinasProcSeletivo on  disciplinasProcSeletivo.codigo = questaoProcessoSeletivo.disciplinaProcSeletivo ");
		sb.append("inner join disciplinasGrupoDisciplinaProcSeletivo on  disciplinasProcSeletivo.codigo = disciplinasGrupoDisciplinaProcSeletivo.disciplinasProcSeletivo ");
		sb.append("inner join grupoDisciplinaProcSeletivo on  grupoDisciplinaProcSeletivo.codigo = disciplinasGrupoDisciplinaProcSeletivo.grupoDisciplinaProcSeletivo ");
		sb.append("inner join usuario as responsavelCriacao on  responsavelCriacao.codigo = questaoProcessoSeletivo.responsavelCriacao ");
		sb.append("left join usuario as responsavelAlteracao on  responsavelAlteracao.codigo = questaoProcessoSeletivo.responsavelAlteracao ");
		sb.append(" WHERE 0=0 ");
		sb.append("and grupoDisciplinaProcSeletivo.codigo = ").append(grupoDisciplinaProcSeletivo);
		if (enunciado != null && !enunciado.trim().isEmpty()) {
//			sb.append(" and upper(sem_acentos(questaoProcessoSeletivo.enunciado)) like(upper(sem_acentos('%").append(enunciado.trim()).append("%'))) ");
			sb.append(" and remover_tags_html(sem_acentos(sem_acentos_html(questaoProcessoSeletivo.enunciado))) ilike(sem_acentos('%").append(enunciado.trim()).append("%')) ");			
		}
		if (disciplinaProcSeletivo != null && disciplinaProcSeletivo > 0) {
			sb.append(" and disciplinasProcSeletivo.codigo = ").append(disciplinaProcSeletivo);
		}
		if (situacaoQuestaoProcessoSeletivoEnum != null) {
			sb.append(" and questaoProcessoSeletivo.situacaoQuestaoEnum = '").append(situacaoQuestaoProcessoSeletivoEnum.name()).append("' ");
		}
		if (nivelComplexidadeQuestaoProcessoSeletivoEnum != null) {
			sb.append(" and questaoProcessoSeletivo.nivelComplexidadeQuestao = '").append(nivelComplexidadeQuestaoProcessoSeletivoEnum.name()).append("' ");
		}
		if (limite != null && limite > 0) {
			sb.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	@Override
	public Integer consultarTotalResgistroPoGrupoDisciplinaProcSeletivo(String enunciado, Integer disciplinaProcSeletivo, Integer grupoDisciplinaProcSeletivo, SituacaoQuestaoEnum situacaoQuestaoProcessoSeletivoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoProcessoSeletivoEnum) throws Exception {
		StringBuilder sb = new StringBuilder("Select count(distinct questaoProcessoSeletivo.codigo) as qtde from questaoProcessoSeletivo ");
		sb.append("inner join disciplinasProcSeletivo on  disciplinasProcSeletivo.codigo = questaoProcessoSeletivo.disciplinaProcSeletivo ");
		sb.append("inner join disciplinasGrupoDisciplinaProcSeletivo on  disciplinasProcSeletivo.codigo = disciplinasGrupoDisciplinaProcSeletivo.disciplinasProcSeletivo ");
		sb.append("inner join grupoDisciplinaProcSeletivo on  grupoDisciplinaProcSeletivo.codigo = disciplinasGrupoDisciplinaProcSeletivo.grupoDisciplinaProcSeletivo ");
		sb.append("WHERE 0=0 ");
		sb.append("and grupoDisciplinaProcSeletivo.codigo = ").append(grupoDisciplinaProcSeletivo);
		if (enunciado != null && !enunciado.trim().isEmpty()) {
//			sb.append(" and upper(sem_acentos(questaoProcessoSeletivo.enunciado)) like(upper(sem_acentos('%").append(enunciado.trim()).append("%'))) ");
			sb.append(" and remover_tags_html(sem_acentos(sem_acentos_html(questaoProcessoSeletivo.enunciado))) ilike(sem_acentos('%").append(enunciado.trim()).append("%')) ");
		}
		if (disciplinaProcSeletivo != null && disciplinaProcSeletivo > 0) {
			sb.append(" and disciplinaProcSeletivo = ").append(disciplinaProcSeletivo);
		}
		if (situacaoQuestaoProcessoSeletivoEnum != null) {
			sb.append(" and questaoProcessoSeletivo.situacaoQuestaoEnum = '").append(situacaoQuestaoProcessoSeletivoEnum.name()).append("' ");
		}
		if (nivelComplexidadeQuestaoProcessoSeletivoEnum != null) {
			sb.append(" and questaoProcessoSeletivo.nivelComplexidadeQuestao = '").append(nivelComplexidadeQuestaoProcessoSeletivoEnum.name()).append("' ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

}
