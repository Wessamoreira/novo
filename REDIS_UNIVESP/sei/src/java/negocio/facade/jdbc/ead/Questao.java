package negocio.facade.jdbc.ead;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.AvaliacaoOnlineTemaAssuntoVO;
import negocio.comuns.ead.OpcaoRespostaQuestaoVO;
import negocio.comuns.ead.QuestaoAssuntoVO;
import negocio.comuns.ead.QuestaoConteudoVO;
import negocio.comuns.ead.QuestaoVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.PoliticaSelecaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.RegraDistribuicaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeRespostaEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.TipoQuestaoEnum;
import negocio.comuns.ead.enumeradores.UsoQuestaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.QuestaoInterfaceFacade;

@Repository
@Lazy
public class Questao extends ControleAcesso implements QuestaoInterfaceFacade {

	/**
     * 
     */
	private static final long serialVersionUID = 3957949547553784963L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void persistir(QuestaoVO questaoVO, Boolean controlarAcesso, String idEntidade,  UsuarioVO usuarioVO) throws Exception {
		if (questaoVO.getCodigo().equals(0)) {
			incluir(questaoVO, controlarAcesso, idEntidade, usuarioVO);
		} else {
			alterar(questaoVO, controlarAcesso, idEntidade, usuarioVO);
		}
		for (OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO : questaoVO.getOpcaoRespostaQuestaoVOs()) {
			opcaoRespostaQuestaoVO.setEditar(false);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void incluir(final QuestaoVO questaoVO, Boolean controlarAcesso, String idEntidade,  UsuarioVO usuarioVO) throws Exception {
		validarDados(questaoVO, idEntidade, usuarioVO);
		incluir(idEntidade, controlarAcesso, usuarioVO);		
		try {
			final StringBuilder sql = new StringBuilder("INSERT INTO questao ");
			sql.append(" (usoOnline, usoPresencial, usoAtividadeDiscursiva, usoExercicio, enunciado,");
			sql.append(" justificativa, ajuda, situacaoQuestaoEnum, tipoQuestaoEnum,");
			sql.append(" dataCriacao, responsavelCriacao, nivelComplexidadeQuestao, disciplina");
			sql.append(" ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ");
			sql.append(" returning codigo ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			questaoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setBoolean(x++, questaoVO.getUsoOnline());
					ps.setBoolean(x++, questaoVO.getUsoPresencial());
					ps.setBoolean(x++, questaoVO.getUsoAtividadeDiscursiva());
					ps.setBoolean(x++, questaoVO.getUsoExercicio());
					ps.setString(x++, questaoVO.getEnunciado());
					ps.setString(x++, questaoVO.getJustificativa());
					ps.setString(x++, questaoVO.getAjuda());
					ps.setString(x++, questaoVO.getSituacaoQuestaoEnum().name());
					ps.setString(x++, questaoVO.getTipoQuestaoEnum().name());
					ps.setDate(x++, Uteis.getDataJDBC(questaoVO.getDataCriacao()));
					ps.setInt(x++, questaoVO.getResponsavelCriacao().getCodigo());
					ps.setString(x++, questaoVO.getNivelComplexidadeQuestao().name());
					ps.setInt(x++, questaoVO.getDisciplina().getCodigo());
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
			getFacadeFactory().getOpcaoRespostaQuestaoFacade().incluirOpcaoRespostaQuestao(questaoVO, usuarioVO);
			if (!questaoVO.getQuestaoConteudoVOs().isEmpty()) {
				getFacadeFactory().getQuestaoConteudoFacade().persistirQuestaoConteudoVOs(questaoVO, usuarioVO);
			}
			if (!questaoVO.getQuestaoAssuntoVOs().isEmpty()) {				
				getFacadeFactory().getQuestaoAssuntoFacade().persistirQuestaoAssuntoVOs(questaoVO, usuarioVO);
			}
			questaoVO.setNovoObj(false);
		} catch (Exception e) {
			questaoVO.setNovoObj(true);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void alterar(final QuestaoVO questaoVO, Boolean controlarAcesso, String idEntidade,  UsuarioVO usuarioVO) throws Exception {
		validarDados(questaoVO, idEntidade, usuarioVO);
		alterar(idEntidade, controlarAcesso, usuarioVO);
		questaoVO.setDataAlteracao(new Date());
		questaoVO.setResponsavelAlteracao(usuarioVO);
		try {
			final StringBuilder sql = new StringBuilder("UPDATE questao set ");
			sql.append(" usoOnline = ?, usoPresencial = ?, usoAtividadeDiscursiva = ?, usoExercicio = ?, enunciado = ?,");
			sql.append(" justificativa = ?, ajuda = ?, situacaoQuestaoEnum = ?, tipoQuestaoEnum = ?,");
			sql.append(" nivelComplexidadeQuestao = ?, disciplina = ?, ");
			sql.append(" dataAlteracao = ?, responsavelAlteracao = ?, motivoCancelamento = ? ");
			sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					int x = 1;
					ps.setBoolean(x++, questaoVO.getUsoOnline());
					ps.setBoolean(x++, questaoVO.getUsoPresencial());
					ps.setBoolean(x++, questaoVO.getUsoAtividadeDiscursiva());
					ps.setBoolean(x++, questaoVO.getUsoExercicio());
					ps.setString(x++, questaoVO.getEnunciado());
					ps.setString(x++, questaoVO.getJustificativa());
					ps.setString(x++, questaoVO.getAjuda());
					ps.setString(x++, questaoVO.getSituacaoQuestaoEnum().name());
					ps.setString(x++, questaoVO.getTipoQuestaoEnum().name());
					ps.setString(x++, questaoVO.getNivelComplexidadeQuestao().name());
					ps.setInt(x++, questaoVO.getDisciplina().getCodigo());
					ps.setDate(x++, Uteis.getDataJDBC(questaoVO.getDataAlteracao()));
					ps.setInt(x++, questaoVO.getResponsavelAlteracao().getCodigo());
					ps.setString(x++, questaoVO.getMotivoCancelamento());
					ps.setInt(x++, questaoVO.getCodigo());
					return ps;
				}
			}) == 0) {
				incluir(questaoVO, controlarAcesso, idEntidade, usuarioVO);
				return;
			}
			getFacadeFactory().getOpcaoRespostaQuestaoFacade().alterarOpcaoRespostaQuestao(questaoVO, usuarioVO);
			getFacadeFactory().getQuestaoConteudoFacade().excluirQuestaoConteudoVOs(questaoVO.getCodigo(), questaoVO.getQuestaoConteudoVOs(), false, usuarioVO);
			if (!questaoVO.getQuestaoConteudoVOs().isEmpty()) {
				getFacadeFactory().getQuestaoConteudoFacade().persistirQuestaoConteudoVOs(questaoVO, usuarioVO);
			}
			getFacadeFactory().getQuestaoAssuntoFacade().excluirQuestaoAssuntoVOs(questaoVO.getCodigo(), questaoVO.getQuestaoAssuntoVOs(), false, usuarioVO);
			if (!questaoVO.getQuestaoAssuntoVOs().isEmpty()) {
				getFacadeFactory().getQuestaoAssuntoFacade().persistirQuestaoAssuntoVOs(questaoVO, usuarioVO);
			}
			questaoVO.setNovoObj(false);
		} catch (Exception e) {
			questaoVO.setNovoObj(false);
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(final QuestaoVO questaoVO, Boolean controlarAcesso, String idEntidade, UsuarioVO usuarioVO) throws Exception {		
			excluir(idEntidade, controlarAcesso, usuarioVO);
			getFacadeFactory().getQuestaoAssuntoFacade().excluirQuestaoAssuntoVOs(questaoVO.getCodigo(), new ArrayList<QuestaoAssuntoVO>(), false, usuarioVO);
			getFacadeFactory().getQuestaoConteudoFacade().excluirQuestaoConteudoVOs(questaoVO.getCodigo(), new ArrayList<QuestaoConteudoVO>(), false, usuarioVO);
			final StringBuilder sql = new StringBuilder("DELETE FROM questao ");
			sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					ps.setInt(1, questaoVO.getCodigo());
					return ps;
				}
			}) == 0) {
				return;
			}
		

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void ativarQuestao(QuestaoVO questaoVO, Boolean controlarAcesso, String idEntidade,  UsuarioVO usuarioVO) throws Exception {
		SituacaoQuestaoEnum situacaoQuestaoEnum = questaoVO.getSituacaoQuestaoEnum();
		try {
			questaoVO.setDataAlteracao(new Date());
			questaoVO.setResponsavelAlteracao(usuarioVO);
			questaoVO.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.ATIVA);
			persistir(questaoVO, controlarAcesso, idEntidade, usuarioVO);
		} catch (Exception e) {
			questaoVO.setSituacaoQuestaoEnum(situacaoQuestaoEnum);
			throw e;
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void inativarQuestao(QuestaoVO questaoVO, Boolean controlarAcesso, String idEntidade,  UsuarioVO usuarioVO) throws Exception {
		SituacaoQuestaoEnum situacaoQuestaoEnum = questaoVO.getSituacaoQuestaoEnum();
		try {
			questaoVO.setDataAlteracao(new Date());
			questaoVO.setResponsavelAlteracao(usuarioVO);
			questaoVO.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.INATIVA);
			persistir(questaoVO, controlarAcesso, idEntidade, usuarioVO);
		} catch (Exception e) {
			questaoVO.setSituacaoQuestaoEnum(situacaoQuestaoEnum);
			throw e;
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void cancelarQuestao(QuestaoVO questaoVO, Boolean controlarAcesso, String idEntidade,  UsuarioVO usuarioVO) throws Exception {
		SituacaoQuestaoEnum situacaoQuestaoEnum = questaoVO.getSituacaoQuestaoEnum();
		try {
			questaoVO.setDataAlteracao(new Date());
			questaoVO.setResponsavelAlteracao(usuarioVO);
			questaoVO.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.CANCELADA);
			persistir(questaoVO, controlarAcesso, idEntidade, usuarioVO);
		} catch (Exception e) {
			questaoVO.setSituacaoQuestaoEnum(situacaoQuestaoEnum);
			throw e;
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public QuestaoVO clonarQuestao(QuestaoVO questaoVO, List<String> tipoCloneQuestao,  Boolean telaExercicio, Boolean telaOnline, Boolean telaPresencial, String idEntidade, UsuarioVO usuarioVO, Boolean clonarComoAtiva) throws Exception {
		if (tipoCloneQuestao.isEmpty()) {
			throw new Exception("Selecione uma opção para clonar.");
		}
		String aux = "";
		if (telaExercicio) {
			aux = "ex";
		} else if (telaOnline) {
			aux = "on";
		} else if (telaPresencial) {
			aux = "pr";
		}
		QuestaoVO questaoVO2 = null;
		Integer codigo = 0;
		for (String string : tipoCloneQuestao) {
			questaoVO2 = new QuestaoVO();
			questaoVO2 = questaoVO.clone();
			if (string.equals("on")) {
				questaoVO2.setUsoOnline(true);
				questaoVO2.setUsoPresencial(false);
				questaoVO2.setUsoExercicio(false);
				questaoVO2.setCodigo(0);
				if (clonarComoAtiva){
					questaoVO2.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.ATIVA);	
				} else {
					questaoVO2.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.EM_ELABORACAO);
				}
			} else if (string.equals("pr")) {
				questaoVO2.setUsoOnline(false);
				questaoVO2.setUsoPresencial(true);
				questaoVO2.setUsoExercicio(false);
				if (clonarComoAtiva){
					questaoVO2.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.ATIVA);	
				} else {
					questaoVO2.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.EM_ELABORACAO);
				}
				questaoVO2.setCodigo(0);
			} else if (string.equals("ex")) {
				questaoVO2.setUsoOnline(false);
				questaoVO2.setUsoPresencial(false);
				questaoVO2.setUsoExercicio(true);
				if (clonarComoAtiva){
					questaoVO2.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.ATIVA);	
				} else {
					questaoVO2.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.EM_ELABORACAO);
				}
				questaoVO2.setCodigo(0);
			}
//			persistir(questaoVO2, false, getIdEntidade(), usuarioVO);
			if (string.equals(aux)) {
				codigo = questaoVO2.getCodigo();
			}
		}
		if (!codigo.equals(0)) {
			questaoVO = new QuestaoVO();
			questaoVO = consultarPorChavePrimaria(codigo);
		}
		if (questaoVO2 != null) {
			persistir(questaoVO2, true, idEntidade, usuarioVO);
			return questaoVO2;
		}
		return questaoVO;
	}

	public String getSelectTotalRegistro() {
		StringBuilder sb = new StringBuilder("select count(distinct questao.codigo) as qtde ");
		sb.append(" FROM questao ");
		sb.append(" inner join disciplina on  disciplina.codigo = questao.disciplina ");
		return sb.toString();
	}
	
	public String getSelectDadosBasicos() {
		StringBuilder sb = new StringBuilder("SELECT questao.*, ");
		sb.append(" disciplina.nome as \"disciplina.nome\", ");
		sb.append(" responsavelCriacao.nome as \"responsavelCriacao.nome\", ");
		sb.append(" responsavelAlteracao.nome as \"responsavelAlteracao.nome\" ");
		sb.append(" FROM questao ");
		sb.append(" inner join disciplina on  disciplina.codigo = questao.disciplina ");
		sb.append(" inner join usuario as responsavelCriacao on  responsavelCriacao.codigo = questao.responsavelCriacao ");
		sb.append(" left join usuario as responsavelAlteracao on  responsavelAlteracao.codigo = questao.responsavelAlteracao ");
		return sb.toString();
	}

	public String getSelectDadosCompleto() {
		StringBuilder sb = new StringBuilder("SELECT questao.*, ");
		sb.append(" disciplina.nome as \"disciplina.nome\", ");
		sb.append(" responsavelCriacao.nome as \"responsavelCriacao.nome\", ");
		sb.append(" responsavelAlteracao.nome as \"responsavelAlteracao.nome\", ");
		sb.append(" orp.codigo as \"orp.codigo\", orp.opcaoResposta as \"orp.opcaoResposta\", orp.correta as \"orp.correta\", ");
		sb.append(" orp.questao as \"orp.questao\", orp.ordemApresentacao as \"orp.ordemApresentacao\" ");
		sb.append(" FROM questao ");
		sb.append(" inner join disciplina on  disciplina.codigo = questao.disciplina ");
		sb.append(" inner join usuario as responsavelCriacao on  responsavelCriacao.codigo = questao.responsavelCriacao ");
		sb.append(" left join usuario as responsavelAlteracao on  responsavelAlteracao.codigo = questao.responsavelAlteracao ");
		sb.append(" left join opcaoRespostaQuestao as orp on  orp.questao = questao.codigo ");
		return sb.toString();
	}

	@Override
	public List<QuestaoVO> consultar(String enunciado, TemaAssuntoVO temaAssuntoVO, Integer disciplina, SituacaoQuestaoEnum situacaoQuestaoEnum, Boolean usoOnline, Boolean usoPresencial, Boolean usoExercicio, Boolean usoAtividadeDiscursiva, TipoQuestaoEnum tipoQuestaoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum, Boolean controleAcesso, String idEntidade, UsuarioVO usuario, Integer limite, Integer pagina, Integer codigoConteudo, PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum,Boolean randomizarApenasQuestoesCadastradasPeloProfessorAluno, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, boolean simulandoAvaliacao) throws Exception {
		consultar(idEntidade, controleAcesso, usuario);
		//Uteis.checkState(disciplina == null || disciplina.equals(0), "Informe uma Disciplina");
		List<Object> param = new ArrayList<Object>(); 
		StringBuilder sb = new StringBuilder(getSelectDadosBasicos());
		if ((Uteis.isAtributoPreenchido(temaAssuntoVO.getCodigo()) || Uteis.isAtributoPreenchido(temaAssuntoVO.getNome()))  
				&& (politicaSelecaoQuestaoEnum == null || politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE))) {
			sb.append("inner join questaoassunto on questaoassunto.questao = questao.codigo ");
			sb.append("inner join temaassunto on questaoassunto.temaassunto = temaassunto.codigo ");
		}
		sb.append(" WHERE 0=0 ");
		montarFiltrosParaConsultar(enunciado, temaAssuntoVO, disciplina, situacaoQuestaoEnum, usoOnline, usoPresencial, usoExercicio, tipoQuestaoEnum, nivelComplexidadeQuestaoEnum, codigoConteudo, politicaSelecaoQuestaoEnum, param, randomizarApenasQuestoesCadastradasPeloProfessorAluno, matriculaPeriodoTurmaDisciplinaVO, avaliacaoOnlineTemaAssuntoVOs,usuario, sb, simulandoAvaliacao);
		sb.append(" order by  remover_tags_html(sem_acentos(sem_acentos_html(questao.enunciado))) ");
		if (limite != null && limite > 0) {
			sb.append(" limit ").append(limite).append(" offset ").append(pagina);
		}	
		SqlRowSet sqlRowSet = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), param.toArray());
		return montarDadosConsulta(sqlRowSet, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	private void montarFiltrosParaConsultar(String enunciado, TemaAssuntoVO temaAssuntoVO, Integer disciplina, SituacaoQuestaoEnum situacaoQuestaoEnum, Boolean usoOnline, Boolean usoPresencial, Boolean usoExercicio, TipoQuestaoEnum tipoQuestaoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum, Integer codigoConteudo, PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum, List<Object> param, Boolean randomizarApenasQuestoesCadastradasPeloProfessorAluno, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuario,  StringBuilder sb, boolean simulandoAvaliacao) throws Exception {
		if (!enunciado.trim().isEmpty()) {
			param.add("%"+enunciado.trim()+"%");
			sb.append(" and upper(sem_acentos(sem_acentos_html(questao.enunciado))) like(upper(sem_acentos((?)))) ");
		}
		if (!disciplina.equals(0)) {
			sb.append(" and disciplina.codigo = ").append(disciplina);
		} 
		if (Uteis.isAtributoPreenchido(situacaoQuestaoEnum)) {
			sb.append(" and questao.situacaoQuestaoEnum = '").append(situacaoQuestaoEnum.name()).append("' ");
		}
		if (Uteis.isAtributoPreenchido(nivelComplexidadeQuestaoEnum)) {
			sb.append(" and questao.nivelComplexidadeQuestao = '").append(nivelComplexidadeQuestaoEnum.name()).append("' ");
		}
		if (Uteis.isAtributoPreenchido(tipoQuestaoEnum)) {
			sb.append(" and questao.tipoQuestaoEnum = '").append(tipoQuestaoEnum.name()).append("' ");
		}
		if (Uteis.isAtributoPreenchido(temaAssuntoVO.getCodigo()) && (politicaSelecaoQuestaoEnum == null || politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE))) {
			sb.append(" AND temaassunto.codigo = ").append(temaAssuntoVO.getCodigo()).append(" ");
		}
		if (Uteis.isAtributoPreenchido(temaAssuntoVO.getNome())&& (politicaSelecaoQuestaoEnum == null || politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE))) {
			param.add(temaAssuntoVO.getNome().trim()+"%");
			sb.append(" AND sem_acentos(temaassunto.nome) ilike sem_acentos(?) ");
		}	
		
		// if (usoAtividadeDiscursiva != null && usoAtividadeDiscursiva) {
		// sb.append(" and questao.usoAtividadeDiscursiva = ").append(usoAtividadeDiscursiva);
		// }
		String andOr = " and ( ";
		if (usoOnline != null && usoOnline) {
			sb.append(andOr).append(" questao.usoOnline = true ");
			andOr = " or ";
		}

		if (usoExercicio != null && usoExercicio) {
			sb.append(andOr).append(" questao.usoExercicio = true ");
			andOr = " or ";
		}
		if (usoPresencial != null && usoPresencial) {
			sb.append(andOr).append(" questao.usoPresencial = true ");
			andOr = " or ";
		}
		if (andOr.contains("or")) {
			sb.append(") ");
		}
		andOr = null;
		/**
		 * @author Victor Hugo 09/01/2015
		 */
		if (Uteis.isAtributoPreenchido(codigoConteudo)) {
			sb.append(" and (");
			sb.append(" exists (select conteudo from questaoconteudo where questaoconteudo.questao = questao.codigo and questaoconteudo.conteudo = ").append(codigoConteudo).append(" limit 1) ");
			sb.append(" or");
			sb.append(" not exists (select codigo from questaoconteudo where questaoconteudo.questao = questao.codigo limit 1) )");
		}else if(usoOnline && !Uteis.isAtributoPreenchido(codigoConteudo) && simulandoAvaliacao) {
			sb.append(" and not exists (select codigo from questaoconteudo where questaoconteudo.questao = questao.codigo limit 1) ");
		}
		if(avaliacaoOnlineTemaAssuntoVOs != null && !avaliacaoOnlineTemaAssuntoVOs.isEmpty() && avaliacaoOnlineTemaAssuntoVOs.stream().anyMatch(t -> t.getSelecionado())) {
			sb.append(" and exists (select qa.codigo from questaoassunto as qa where qa.questao = questao.codigo and qa.temaAssunto in (0 ");
			for (AvaliacaoOnlineTemaAssuntoVO avaliacaoOnlineTemaAssuntoVO : avaliacaoOnlineTemaAssuntoVOs) {
				if(avaliacaoOnlineTemaAssuntoVO.getSelecionado()) {
					sb.append(", ").append(avaliacaoOnlineTemaAssuntoVO.getTemaAssuntoVO().getCodigo());
				}
			}
			sb.append(" )) ");
		}else if(Uteis.isAtributoPreenchido(codigoConteudo) && politicaSelecaoQuestaoEnum != null && (politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_ESTUDADOS) || politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUESTOES_TODOS_ASSUNTOS_CONTEUDO))) {
			sb.append(" and exists ");
			sb.append(" (select questaoassunto.codigo from questaoassunto where questaoassunto.questao = questao.codigo and questaoassunto.temaassunto in (select unidadeconteudo.temaassunto from  unidadeconteudo where unidadeconteudo.temaassunto is not null and conteudo = ").append(codigoConteudo).append(") limit 1) ");
		}else if(Uteis.isAtributoPreenchido(codigoConteudo) && politicaSelecaoQuestaoEnum != null && (politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO))) {
			sb.append(" and ( exists ");
			sb.append(" (select questaoassunto.codigo from questaoassunto where questaoassunto.questao = questao.codigo and questaoassunto.temaassunto in (select unidadeconteudo.temaassunto from  unidadeconteudo where unidadeconteudo.temaassunto is not null and conteudo = ").append(codigoConteudo).append(") limit 1) ");
			sb.append(" or not exists (select questaoassunto.codigo from questaoassunto where questaoassunto.questao = questao.codigo )) ");
			
		}
		if (randomizarApenasQuestoesCadastradasPeloProfessorAluno != null && randomizarApenasQuestoesCadastradasPeloProfessorAluno && matriculaPeriodoTurmaDisciplinaVO != null && Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getTurma())  && Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getDisciplina())) {
			sb.append(getSqlRegraRandomizarApenasQuestoesCadastradasPeloProfessor(matriculaPeriodoTurmaDisciplinaVO, disciplina, usuario));
	}

	}

	private List<QuestaoVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados) throws Exception {
		List<QuestaoVO> questaoVOs = new ArrayList<QuestaoVO>(0);
		while (rs.next()) {
			questaoVOs.add(montarDados(rs, nivelMontarDados));
		}
		return questaoVOs;
	}

	private List<QuestaoVO> montarDadosConsultaCompleta(SqlRowSet rs) throws Exception {
		List<QuestaoVO> questaoVOs = new ArrayList<QuestaoVO>(0);
		Map<Integer, QuestaoVO> qMap = new HashMap<Integer, QuestaoVO>(0);
		QuestaoVO obj = null;
		while (rs.next()) {
			if (!qMap.containsKey(rs.getInt("codigo"))) {
				obj = montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
				qMap.put(obj.getCodigo(), obj);
			} else {
				obj = qMap.get(rs.getInt("codigo"));
			}
			if (rs.getInt("orp.codigo") > 0) {
				obj.getOpcaoRespostaQuestaoVOs().add(getFacadeFactory().getOpcaoRespostaQuestaoFacade().montarDados(rs, "orp."));
			}
		}
		if(obj != null) {
			obj.setQuestaoConteudoVOs(getFacadeFactory().getQuestaoConteudoFacade().consultarPorCodigoQuestaoVO(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null));
			obj.setQuestaoAssuntoVOs(getFacadeFactory().getQuestaoAssuntoFacade().consultarPorCodigoQuestaoVO(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null));
			questaoVOs.addAll(qMap.values());			
		}		
		return questaoVOs;
	}

	private QuestaoVO montarDados(SqlRowSet rs, int nivelMontarDados) throws Exception {
		QuestaoVO obj = new QuestaoVO();
		obj.setNovoObj(false);
		obj.setCodigo(rs.getInt("codigo"));
		obj.setDataCriacao(rs.getDate("dataCriacao"));
		obj.setDataAlteracao(rs.getDate("dataAlteracao"));
		obj.getDisciplina().setCodigo(rs.getInt("disciplina"));
		obj.getDisciplina().setNome(rs.getString("disciplina.nome"));
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
		obj.setTipoQuestaoEnum(TipoQuestaoEnum.valueOf(rs.getString("tipoQuestaoEnum")));
		obj.setUsoAtividadeDiscursiva(rs.getBoolean("usoAtividadeDiscursiva"));
		obj.setUsoExercicio(rs.getBoolean("usoExercicio"));
		obj.setUsoOnline(rs.getBoolean("usoOnline"));
		obj.setUsoPresencial(rs.getBoolean("usoPresencial"));
		obj.setMotivoAnulacaoQuestao(rs.getString("motivoAnulacaoQuestao"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setOpcaoRespostaQuestaoVOs(getFacadeFactory().getOpcaoRespostaQuestaoFacade().consultarPorQuestao(obj.getCodigo()));
		obj.setQuestaoConteudoVOs(getFacadeFactory().getQuestaoConteudoFacade().consultarPorCodigoQuestaoVO(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null));
		obj.setQuestaoAssuntoVOs(getFacadeFactory().getQuestaoAssuntoFacade().consultarPorCodigoQuestaoVO(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null));
		return obj;
	}

	@Override
	public Integer consultarTotalResgistro(String enunciado, TemaAssuntoVO temaAssuntoVO, Integer disciplina, SituacaoQuestaoEnum situacaoQuestaoEnum, Boolean usoOnline, Boolean usoPresencial, Boolean usoExercicio, Boolean usoAtividadeDiscursiva, TipoQuestaoEnum tipoQuestaoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum, Integer codigoConteudo, PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum,Boolean randomizarApenasQuestoesCadastradasPeloProfessorAluno, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO, boolean simulandoAvaliacao) throws Exception {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder(getSelectTotalRegistro());
		if ((Uteis.isAtributoPreenchido(temaAssuntoVO.getCodigo()) || Uteis.isAtributoPreenchido(temaAssuntoVO.getNome())) 
				&& (politicaSelecaoQuestaoEnum == null || politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE))) {
			sb.append("inner join questaoassunto on questaoassunto.questao = questao.codigo ");
			sb.append("inner join temaassunto on questaoassunto.temaassunto = temaassunto.codigo ");
		}
		sb.append(" WHERE 0=0 ");
		montarFiltrosParaConsultar(enunciado, temaAssuntoVO, disciplina, situacaoQuestaoEnum, usoOnline, usoPresencial, usoExercicio, tipoQuestaoEnum, nivelComplexidadeQuestaoEnum, codigoConteudo, politicaSelecaoQuestaoEnum, param, randomizarApenasQuestoesCadastradasPeloProfessorAluno, matriculaPeriodoTurmaDisciplinaVO, avaliacaoOnlineTemaAssuntoVOs, usuarioVO,  sb, simulandoAvaliacao);		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(),  param.toArray());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0; 
		
	}

	@Override
	public void novo(QuestaoVO questaoVO, Boolean usoAtividadeDiscursiva, Boolean usoExercicio, Boolean usoOnline, Boolean usoPresencial) {
		questaoVO.setNovoObj(true);
		questaoVO.setCodigo(0);
		questaoVO.setEnunciado("");
		questaoVO.setUsoAtividadeDiscursiva(usoAtividadeDiscursiva);
		questaoVO.setUsoExercicio(usoExercicio);
		questaoVO.setUsoOnline(usoOnline);
		questaoVO.setUsoPresencial(usoPresencial);
		questaoVO.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.EM_ELABORACAO);
		questaoVO.setNivelComplexidadeQuestao(NivelComplexidadeQuestaoEnum.MEDIO);
		questaoVO.setQuestaoAssuntoVOs(new ArrayList<QuestaoAssuntoVO>());
		questaoVO.setQuestaoConteudoVOs(null);
		if (usoAtividadeDiscursiva) {
			questaoVO.setTipoQuestaoEnum(TipoQuestaoEnum.TEXTUAL);
		} else {
			questaoVO.getOpcaoRespostaQuestaoVOs().clear();
			questaoVO.setTipoQuestaoEnum(TipoQuestaoEnum.UNICA_ESCOLHA);
			OpcaoRespostaQuestaoVO opc = new OpcaoRespostaQuestaoVO();
			opc.setOrdemApresentacao(1);
			opc.setEditar(false);
			questaoVO.getOpcaoRespostaQuestaoVOs().add(opc);
			opc = new OpcaoRespostaQuestaoVO();
			opc.setOrdemApresentacao(2);
			opc.setEditar(false);
			questaoVO.getOpcaoRespostaQuestaoVOs().add(opc);
			opc = new OpcaoRespostaQuestaoVO();
			opc.setOrdemApresentacao(3);
			opc.setEditar(false);
			questaoVO.getOpcaoRespostaQuestaoVOs().add(opc);
			opc = new OpcaoRespostaQuestaoVO();
			opc.setOrdemApresentacao(4);
			opc.setEditar(false);
			questaoVO.getOpcaoRespostaQuestaoVOs().add(opc);
		}

	}

	@Override
	public void adicionarOpcaoRespostaQuestao(QuestaoVO questaoVO, Boolean validarDados, OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO) throws Exception {
		if (validarDados) {
			getFacadeFactory().getOpcaoRespostaQuestaoFacade().validarDados(opcaoRespostaQuestaoVO);
		}
		if (opcaoRespostaQuestaoVO.getOrdemApresentacao() == null || opcaoRespostaQuestaoVO.getOrdemApresentacao() == 0) {
			opcaoRespostaQuestaoVO.setOrdemApresentacao(questaoVO.getOpcaoRespostaQuestaoVOs().size() + 1);
			opcaoRespostaQuestaoVO.setEditar(true);
			questaoVO.getOpcaoRespostaQuestaoVOs().add(opcaoRespostaQuestaoVO);
		} else {
			questaoVO.getOpcaoRespostaQuestaoVOs().set(opcaoRespostaQuestaoVO.getOrdemApresentacao() - 1, opcaoRespostaQuestaoVO);
		}
		int x = 1;
		for (OpcaoRespostaQuestaoVO orq : questaoVO.getOpcaoRespostaQuestaoVOs()) {
			orq.setLetraCorrespondente(null);
			orq.setOrdemApresentacao(x++);
		}

	}

	@Override
	public void removerOpcaoRespostaQuestao(QuestaoVO questaoVO, OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO) throws Exception {
		questaoVO.getOpcaoRespostaQuestaoVOs().remove(opcaoRespostaQuestaoVO.getOrdemApresentacao() - 1);
		int x = 1;
		for (OpcaoRespostaQuestaoVO orq : questaoVO.getOpcaoRespostaQuestaoVOs()) {
			orq.setLetraCorrespondente(null);
			orq.setOrdemApresentacao(x++);
		}
	}

	@Override
	public void validarDados(QuestaoVO questaoVO, String idEntidade,  UsuarioVO usuarioVO) throws ConsistirException, Exception {
		ConsistirException ce = null;
		if (questaoVO.getDisciplina() == null || questaoVO.getDisciplina().getCodigo() == null || questaoVO.getDisciplina().getCodigo() == 0) {
			ce = ce == null ? new ConsistirException() : ce;
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Questao_disciplina"));
		}
		if (Uteis.retiraTags(questaoVO.getEnunciado()).trim().isEmpty()   && !questaoVO.getEnunciado().contains("<img")) {
			ce = ce == null ? new ConsistirException() : ce;
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Questao_enunciado") );
		}
		if (!questaoVO.getUsoAtividadeDiscursiva() && !questaoVO.getUsoExercicio() && !questaoVO.getUsoOnline() && !questaoVO.getUsoPresencial()) {
			ce = ce == null ? new ConsistirException() : ce;
			ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Questao_usoQuestao"));
		}

		if (!questaoVO.getUsoAtividadeDiscursiva()) {
			int x = 0;
			for (OpcaoRespostaQuestaoVO orq : questaoVO.getOpcaoRespostaQuestaoVOs()) {
				if (!Uteis.retiraTags(orq.getOpcaoResposta()).trim().isEmpty()  || orq.getOpcaoResposta().contains("<img")) {
					x++;
				}
			}
			if (x < 2) {
				ce = ce == null ? new ConsistirException() : ce;
				ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Questao_nrOpcaoRespostaInvalido"));
			}
			x = 0;
			for (OpcaoRespostaQuestaoVO orq : questaoVO.getOpcaoRespostaQuestaoVOs()) {
				if (orq.getCorreta()) {
					x++;
				}
			}
			if (x == 0) {
				ce = ce == null ? new ConsistirException() : ce;
				ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Questao_semOpcaoCorreta"));
			}
			if (questaoVO.getTipoQuestaoEnum().equals(TipoQuestaoEnum.UNICA_ESCOLHA) && x > 1) {
				ce = ce == null ? new ConsistirException() : ce;
				ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Questao_nrOpcaoRespostaCorretaInvalido"));
			}
		}

		if (usuarioVO.getIsApresentarVisaoProfessor()) {
			if (idEntidade.equals("QuestaoOnline")) {
				if (questaoVO.getQuestaoConteudoVOs().isEmpty()) {
					verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico("PermitirProfessorCadastrarQuestaoSemInformarConteudoOnline", usuarioVO);
				}
			} else if (idEntidade.equals("QuestaoPresencial")) {
				if (questaoVO.getQuestaoConteudoVOs().isEmpty()) {
					verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico("PermitirProfessorCadastrarQuestaoSemInformarConteudoPresencial", usuarioVO);
				}
			} else if (idEntidade.equals("QuestaoExercicio")) {
				if (questaoVO.getQuestaoConteudoVOs().isEmpty()) {
					verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico("PermitirProfessorCadastrarQuestaoSemInformarConteudoQuestaoExercicio", usuarioVO);
				}
			}
		}

		if (ce != null) {
			throw ce;
		}
		if(!Uteis.isAtributoPreenchido(questaoVO.getResponsavelCriacao())) {
			questaoVO.setResponsavelCriacao(usuarioVO);	
		}
	}

	@Override
	public QuestaoVO consultarPorChavePrimaria(Integer codigo) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectDadosCompleto());
		sb.append(" where questao.codigo = ").append(codigo);
		sb.append(" order by orp.ordemApresentacao");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<QuestaoVO> questaoVOs = montarDadosConsultaCompleta(rs);
		if (questaoVOs.isEmpty()) {
			throw new Exception("Dados não encontrados(Questão).");
		}
		return questaoVOs.get(0);
	}

	@Override
	public QuestaoVO consultarPorChavePrimaria(Integer codigo, Boolean usoOnline, Boolean usoPresencial, Boolean usoExercicio, Boolean usoAtividadeDiscursiva) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectDadosCompleto());
		sb.append(" where questao.codigo = ").append(codigo);
		String andOr = " and ( ";
		if (usoOnline != null && usoOnline) {
			sb.append(andOr).append(" questao.usoOnline = true ");
			andOr = " or ";
		}

		if (usoExercicio != null && usoExercicio) {
			sb.append(andOr).append(" questao.usoExercicio = true ");
			andOr = " or ";
		}
		if (usoPresencial != null && usoPresencial) {
			sb.append(andOr).append(" questao.usoPresencial = true ");
			andOr = " or ";
		}
		if (andOr.contains("or")) {
			sb.append(") ");
		}
		sb.append(" order by orp.ordemApresentacao");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<QuestaoVO> questaoVOs = montarDadosConsultaCompleta(rs);
		if (questaoVOs.isEmpty()) {
			throw new Exception("Dados não encontrados(Questão).");
		}
		return questaoVOs.get(0);
	}

	@Override
	public void alterarOrdemOpcaoRespostaQuestao(QuestaoVO questaoVO, OpcaoRespostaQuestaoVO opcaoRespostaQuestao1, OpcaoRespostaQuestaoVO opcaoRespostaQuestao2) throws Exception {
		int ordem1 = opcaoRespostaQuestao1.getOrdemApresentacao();
		opcaoRespostaQuestao1.setOrdemApresentacao(opcaoRespostaQuestao2.getOrdemApresentacao());
		opcaoRespostaQuestao2.setOrdemApresentacao(ordem1);
		opcaoRespostaQuestao2.setLetraCorrespondente(null);
		opcaoRespostaQuestao1.setLetraCorrespondente(null);
		Ordenacao.ordenarLista(questaoVO.getOpcaoRespostaQuestaoVOs(), "ordemApresentacao");
	}

	/**
	 * Método responsável por consultar as questão para geração da avaliação
	 * on-line ou lista de exercício. Este método é utilizado também na visão
	 * administrativa onde no cadastro de uma avaliação on-line caso: Disciplina
	 * e Questões Fixas geradas randomicamente: Por default, a politica de
	 * seleção será QUALQUER_QUESTAO para que as questões sejam geradas
	 * randomicamente, porém a politica de seleção para esta configuração não é
	 * persistida no banco.
	 * 
	 * Caso seja gerada Randomicamente independente do tipo, as seguintes regras
	 * devem ser levadas em consideração:
	 * 
	 * Qualquer Questão - Neste caso ao randomizar não serão considerados os
	 * assuntos do conteudo definido para o aluno na disciplina em questão.
	 * Questões de Todos Asuntos do Conteudo - Neste caso ao gerar a lista de
	 * exercício só randomizara as questões que possuem vinculos com os assuntos
	 * vinculados ao conteudo definido para o aluno na disciplina em questão.
	 * 
	 * Caso a Política de Seleção de Questão for Questões de Todos Asuntos do
	 * Conteudo:
	 * 
	 * Quantidade Fixa por Assunto: Neste caso a quantidade definida no campos e
	 * níveis de complexidade para randomizar as questões serão consideradas
	 * para cada assunto estudado, ou seja, se foi definido que deverá utilizar
	 * 3 questões de cada nivel e o aluno já estou 3 assuntos diferentes então o
	 * sistema irá gerar uma avaliação on-line com 27 ((3 Facil + 3 Médio + 3
	 * Dificil) * 3 Assuntos) questões.
	 * 
	 * Quantidade Distribuida Entre Assuntos: Neste caso a quantidade definida
	 * no campos e níveis de complexidade para randomizar as questões serão
	 * distribuidas igualitariamente entre os assuntos estudados, ou seja, se
	 * foi definido que deverá utilizar 3 questões de cada nivel e o aluno já
	 * estou 3 assuntos diferentes então o sistema irá gerar uma avaliação
	 * on-line com 9 questões, sendo 1 de nivel de complexidade de cada assunto
	 * 
	 * Permite Repetições de Questões a partir Segunda Avaliação On-line do
	 * Aluno: Neste caso quando marcado o sistema irá permitir que apartir da
	 * segunda avaliação on-line do aluno uma questão que já foi respondida pelo
	 * aluno em outra avalição on-line da mesma disciplina possa repetir.
	 * 
	 * 
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<QuestaoVO> consultarQuestoesPorDisciplinaRandomicamente(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Integer codigoTemaAssunto, Integer codigoUnidadeConteudo, Integer disciplina, Integer qtdeComplexidadeQuestoesFacil, Integer qtdeComplexidadeQuestoesMedio, Integer qtdeComplexidadeQuestoesDificil, Integer qtdeQualquerComplexidade, UsoQuestaoEnum usoQuestao, PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum, RegraDistribuicaoQuestaoEnum regraDistribuicaoQuestaoEnum, Boolean permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, Integer codigoConteudo, Integer codigoListaExercico, Boolean randomizarApenasQuestoesCadastradasPeloProfessorAluno, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO) throws Exception {
		List<QuestaoVO> questaoVOs = new ArrayList<QuestaoVO>();
		List<QuestaoVO> questoesQueNaoPodemRepetir = consultarQuestoesAvaliacaoOnlineMatriculaJaRealizada(matriculaPeriodoTurmaDisciplinaVO, permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, usuarioVO);
		String uso = validarQualUsoDaQuestao(usoQuestao);	
		validarQuestoesRandomicamentePorNivelDeComplexidade(questaoVOs, questoesQueNaoPodemRepetir, uso, matriculaPeriodoTurmaDisciplinaVO, codigoTemaAssunto, codigoUnidadeConteudo, disciplina, qtdeComplexidadeQuestoesFacil, qtdeComplexidadeQuestoesMedio, qtdeComplexidadeQuestoesDificil, politicaSelecaoQuestaoEnum, regraDistribuicaoQuestaoEnum, permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, codigoConteudo, codigoListaExercico, randomizarApenasQuestoesCadastradasPeloProfessorAluno, avaliacaoOnlineTemaAssuntoVOs, usuarioVO);
		validarQuestoesRandomicamentePorQualquerNivelDeComplexidade(questaoVOs, questoesQueNaoPodemRepetir, uso, matriculaPeriodoTurmaDisciplinaVO, codigoTemaAssunto, codigoUnidadeConteudo, disciplina, qtdeQualquerComplexidade, politicaSelecaoQuestaoEnum, regraDistribuicaoQuestaoEnum, permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, codigoConteudo, randomizarApenasQuestoesCadastradasPeloProfessorAluno, avaliacaoOnlineTemaAssuntoVOs, usuarioVO);
		return questaoVOs;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
	private String validarQualUsoDaQuestao(UsoQuestaoEnum usoQuestao) {
		if (usoQuestao.equals(UsoQuestaoEnum.EXERCICIO)) {
			return " and usoExercicio = true ";
		} else if (usoQuestao.equals(UsoQuestaoEnum.ONLINE)) {
			return " and usoOnline = true ";
		} else if (usoQuestao.equals(UsoQuestaoEnum.PRESENCIAL)) {
			return " and usoPresencial = true ";
		} else if (usoQuestao.equals(UsoQuestaoEnum.ATIVIDADE_DISCURSIVA)) {
			return " and usoAtividadeDiscursiva = true ";
		} else {
			return "";
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
	private void validarQuestoesRandomicamentePorNivelDeComplexidade(List<QuestaoVO> questaoVOs, List<QuestaoVO> questoesQueNaoPodemRepetir, String uso, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Integer codigoTemaAssunto, Integer codigoUnidadeConteudo, Integer disciplina, Integer qtdeComplexidadeQuestoesFacil, Integer qtdeComplexidadeQuestoesMedio, Integer qtdeComplexidadeQuestoesDificil, PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum, RegraDistribuicaoQuestaoEnum regraDistribuicaoQuestaoEnum, Boolean permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, Integer codigoConteudo, Integer codigoListaExercicio, Boolean randomizarApenasQuestoesCadastradasPeloProfessorAluno, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO) throws Exception{
		
		if ((qtdeComplexidadeQuestoesFacil != null && qtdeComplexidadeQuestoesFacil > 0) || (qtdeComplexidadeQuestoesMedio != null && qtdeComplexidadeQuestoesMedio > 0) || (qtdeComplexidadeQuestoesDificil != null && qtdeComplexidadeQuestoesDificil > 0)) {
			SqlRowSet rs = null;
			StringBuilder sb = new StringBuilder(getSelectDadosCompleto());
			sb.append(" where questao.codigo in ( ");
			sb.append("select codigo from ( ");
			
			if (politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO)) {
				validarQuestoesRandomicamenteComNivelDeComplexidadePorPoliticaDeSelecaoQualquerQuestao(sb, questoesQueNaoPodemRepetir, uso, matriculaPeriodoTurmaDisciplinaVO, codigoTemaAssunto, disciplina, qtdeComplexidadeQuestoesFacil, qtdeComplexidadeQuestoesMedio, qtdeComplexidadeQuestoesDificil, permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, codigoConteudo, randomizarApenasQuestoesCadastradasPeloProfessorAluno, avaliacaoOnlineTemaAssuntoVOs, usuarioVO);
			} else if (regraDistribuicaoQuestaoEnum.equals(RegraDistribuicaoQuestaoEnum.QUANTIDADE_FIXA_ASSUNTO)) {
				validarQuestoesRandomicamenteComNivelDeComplexidadeComRegraDeDistribuicaoQuantidadeFixa(sb, questoesQueNaoPodemRepetir, uso, matriculaPeriodoTurmaDisciplinaVO, politicaSelecaoQuestaoEnum, codigoTemaAssunto, codigoUnidadeConteudo, disciplina, qtdeComplexidadeQuestoesFacil, qtdeComplexidadeQuestoesMedio, qtdeComplexidadeQuestoesDificil, permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, codigoConteudo, codigoListaExercicio, randomizarApenasQuestoesCadastradasPeloProfessorAluno, avaliacaoOnlineTemaAssuntoVOs, usuarioVO);
			} else if (regraDistribuicaoQuestaoEnum.equals(RegraDistribuicaoQuestaoEnum.QUANTIDADE_DISTRUIBUIDA_ENTRE_ASSUNTOS)) {
				validarQuestoesRandomicamenteComNivelDeComplexidadeComRegraDeDistribuicaoQuantidadeDistribuida(sb, questoesQueNaoPodemRepetir, uso, matriculaPeriodoTurmaDisciplinaVO, politicaSelecaoQuestaoEnum, codigoTemaAssunto, codigoUnidadeConteudo, disciplina, qtdeComplexidadeQuestoesFacil, qtdeComplexidadeQuestoesMedio, qtdeComplexidadeQuestoesDificil, permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, codigoConteudo, randomizarApenasQuestoesCadastradasPeloProfessorAluno, avaliacaoOnlineTemaAssuntoVOs, usuarioVO);				
			}
			
			sb.append(" ) ");
			rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			questaoVOs.addAll(montarDadosConsultaCompleta(rs));
		}	
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
	private void validarQuestoesRandomicamenteComNivelDeComplexidadePorPoliticaDeSelecaoQualquerQuestao(StringBuilder sb, List<QuestaoVO> questoesQueNaoPodemRepetir, String uso, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Integer codigoTemaAssunto, Integer disciplina, Integer qtdeComplexidadeQuestoesFacil, Integer qtdeComplexidadeQuestoesMedio, Integer qtdeComplexidadeQuestoesDificil, Boolean permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno,  Integer codigoConteudo, Boolean randomizarApenasQuestoesCadastradasPeloProfessorAluno, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO) throws Exception{
		sb.append("	select row_number()over(partition by nivelcomplexidadequestao order by nivelcomplexidadequestao, random()) as ordem, ");
		sb.append("	questao.codigo, questao.nivelcomplexidadequestao from questao");
		if (!codigoTemaAssunto.equals(0) || (avaliacaoOnlineTemaAssuntoVOs != null && !avaliacaoOnlineTemaAssuntoVOs.isEmpty() && avaliacaoOnlineTemaAssuntoVOs.stream().anyMatch(t -> t.getSelecionado()))) {
			sb.append("	inner join questaoassunto on questaoassunto.questao = questao.codigo");
		}
		sb.append("	where (questao.disciplina = ").append(disciplina);
		sb.append(" or questao.disciplina in (select equivalente from disciplinaequivalente where disciplina = ").append(disciplina);
		sb.append("	)) and questao.situacaoquestaoenum = 'ATIVA' ");
		if (!codigoTemaAssunto.equals(0)) {
			sb.append("and  questaoassunto.temaassunto = ").append(codigoTemaAssunto);
		}
		if(avaliacaoOnlineTemaAssuntoVOs != null && !avaliacaoOnlineTemaAssuntoVOs.isEmpty() && avaliacaoOnlineTemaAssuntoVOs.stream().anyMatch(t -> t.getSelecionado())) {
			sb.append(" and questaoassunto.temaAssunto in (0 ");
			for (AvaliacaoOnlineTemaAssuntoVO avaliacaoOnlineTemaAssuntoVO : avaliacaoOnlineTemaAssuntoVOs) {
				if(avaliacaoOnlineTemaAssuntoVO.getSelecionado()) {
					sb.append(", ").append(avaliacaoOnlineTemaAssuntoVO.getTemaAssuntoVO().getCodigo());
				}
			}
			sb.append(") ");
		}
		if (usuarioVO.getIsApresentarVisaoAluno()) {
			if (permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno.equals(Boolean.FALSE)) {
				sb.append(" and questao.codigo not in ( 0 ");
				for (QuestaoVO questaoVO : questoesQueNaoPodemRepetir) {
					sb.append(", ").append(questaoVO.getCodigo());
				}
				sb.append(" ) ");
			}
		}
		sb.append(uso);
		if (randomizarApenasQuestoesCadastradasPeloProfessorAluno) {
			sb.append(getSqlRegraRandomizarApenasQuestoesCadastradasPeloProfessor(matriculaPeriodoTurmaDisciplinaVO, disciplina, usuarioVO));
		}
		sb.append("	and ((select conteudo from questaoconteudo where questaoconteudo.questao = questao.codigo and questaoconteudo.conteudo = ").append(codigoConteudo).append(" limit 1) is not null");
		sb.append("	or (select codigo from questaoconteudo where questaoconteudo.questao = questao.codigo limit 1) is null)");
		sb.append("	group by questao.codigo, questao.nivelcomplexidadequestao");
		sb.append(") as t ");
		sb.append("where ((nivelcomplexidadequestao = 'FACIL' and ordem <= ").append(qtdeComplexidadeQuestoesFacil).append(") or (nivelcomplexidadequestao = 'MEDIO' and ordem <= ").append(qtdeComplexidadeQuestoesMedio).append(") or (nivelcomplexidadequestao = 'DIFICIL' and ordem <= ").append(qtdeComplexidadeQuestoesDificil).append(")) ");
		sb.append("group by codigo, nivelcomplexidadequestao order by random(), nivelcomplexidadequestao");
	}
	
	public StringBuilder getSqlRegraRandomizarApenasQuestoesCadastradasPeloProfessor(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Integer disciplina, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		DefinicoesTutoriaOnlineEnum definicoes = getFacadeFactory().getTurmaDisciplinaFacade().consultarDefinicoesTutoriaOnlineTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
		if (definicoes == null || definicoes.isProgramacaoAula()) {
			sb.append("and exists (");
			sb.append(" select distinct horarioturmadiaitem.professor from horarioturma ");
			sb.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
			sb.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
			sb.append(" inner join disciplina on horarioturmadiaitem.disciplina = disciplina.codigo ");
			sb.append(" inner join turma on turma.codigo = horarioturma.turma ");
			sb.append(" where (turma.codigo in(").append(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo()).append(", ").append(matriculaPeriodoTurmaDisciplinaVO.getTurmaPratica().getCodigo()).append(", ").append(matriculaPeriodoTurmaDisciplinaVO.getTurmaTeorica().getCodigo()).append(")  ");
			sb.append(" or turma.codigo in(");
			sb.append(" select distinct turmaorigem from turmaagrupada where turma = ").append(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo());
			sb.append("))");
			sb.append(" and horarioturmadiaitem.disciplina = questao.disciplina ");
			sb.append(" and (horarioturmadiaitem.disciplina = ").append(disciplina).append(" or disciplina.codigo in (select disciplina from disciplinaequivalente where equivalente = ").append(disciplina).append(")) ");
			sb.append(" and horarioturma.anovigente = '").append(matriculaPeriodoTurmaDisciplinaVO.getAno()).append("' ");
			sb.append(" and horarioturma.semestrevigente = '").append(matriculaPeriodoTurmaDisciplinaVO.getSemestre()).append("' ");
			sb.append(" and horarioturmadiaitem.professor = (select distinct usuario.pessoa from usuario where usuario.codigo = questao.responsavelcriacao) ");
			sb.append(") ");
		} else {
			sb.append(" and (select distinct usuario.pessoa from usuario where usuario.codigo = questao.responsavelcriacao) = ").append(matriculaPeriodoTurmaDisciplinaVO.getProfessor().getCodigo());
		}
		return sb;
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
	private void validarQuestoesRandomicamenteComNivelDeComplexidadeComRegraDeDistribuicaoQuantidadeFixa(StringBuilder sb, List<QuestaoVO> questoesQueNaoPodemRepetir, String uso, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum, Integer codigoTemaAssunto, Integer codigoUnidadeConteudo, Integer disciplina, Integer qtdeComplexidadeQuestoesFacil, Integer qtdeComplexidadeQuestoesMedio, Integer qtdeComplexidadeQuestoesDificil, Boolean permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno,  Integer codigoConteudo, Integer codigoListaExercicio, Boolean randomizarApenasQuestoesCadastradasPeloProfessorAluno, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO) throws Exception{
		sb.append("	select row_number()over(partition by nivelcomplexidadequestao, unidadeconteudo.temaassunto order by nivelcomplexidadequestao, unidadeconteudo.temaassunto, random()) as ordem,  ");
		sb.append("	questao.codigo, questao.nivelcomplexidadequestao, unidadeconteudo.temaassunto from unidadeconteudo");
		sb.append("	inner join questaoassunto on questaoassunto.temaassunto = unidadeconteudo.temaassunto");
		sb.append("	inner join questao on questao.codigo = questaoassunto.questao ");
		sb.append("	where (questao.disciplina = ").append(disciplina);
		sb.append(" or questao.disciplina in (select equivalente from disciplinaequivalente where disciplina = ").append(disciplina);
		sb.append("	))");

		if(Uteis.isAtributoPreenchido(codigoTemaAssunto)) {
			sb.append("	and questaoassunto.temaassunto = ").append(codigoTemaAssunto);
		}
		sb.append(" and questao.situacaoquestaoenum = 'ATIVA' ");
		if (usuarioVO.getIsApresentarVisaoAluno()) {
			if (permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno.equals(Boolean.FALSE)) {
				sb.append(" and questao.codigo not in ( 0 ");
				for (QuestaoVO questaoVO : questoesQueNaoPodemRepetir) {
					sb.append(", ").append(questaoVO.getCodigo());
				}
				sb.append(" ) ");
			}
		}
		sb.append(uso);
		
		if (randomizarApenasQuestoesCadastradasPeloProfessorAluno) {
			sb.append(getSqlRegraRandomizarApenasQuestoesCadastradasPeloProfessor(matriculaPeriodoTurmaDisciplinaVO, disciplina, usuarioVO));
		}
		
		sb.append("	and unidadeconteudo.conteudo = ");
		if(Uteis.isAtributoPreenchido(codigoConteudo)) {
			sb.append(codigoConteudo);			
		}else {
		sb.append(" (select conteudo from matriculaperiodoturmadisciplina where codigo =").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo()).append(") ");
		}
		
		sb.append("	and ((select conteudo from questaoconteudo where questaoconteudo.questao = questao.codigo and questaoconteudo.conteudo = ");
		if(Uteis.isAtributoPreenchido(codigoConteudo)) {
			sb.append(codigoConteudo);			
		}
		else {
		sb.append(" (select conteudo from matriculaperiodoturmadisciplina where codigo =").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo()).append(") ");
		}
		sb.append(" limit 1) is not null");
		sb.append("	or (select codigo from questaoconteudo where questaoconteudo.questao = questao.codigo limit 1) is null)");
		
		sb.append("	group by questao.codigo, questao.nivelcomplexidadequestao, unidadeconteudo.temaassunto");
		sb.append(") as t ");
		sb.append("	where ((nivelcomplexidadequestao = 'FACIL' and ordem <= ").append(qtdeComplexidadeQuestoesFacil).append(") or (nivelcomplexidadequestao = 'MEDIO' and ordem <= ").append(qtdeComplexidadeQuestoesMedio).append(") or (nivelcomplexidadequestao = 'DIFICIL' and ordem <= ").append(qtdeComplexidadeQuestoesDificil).append(")) ");
		if(avaliacaoOnlineTemaAssuntoVOs != null && !avaliacaoOnlineTemaAssuntoVOs.isEmpty() && avaliacaoOnlineTemaAssuntoVOs.stream().anyMatch(t -> t.getSelecionado())) {
			sb.append(" and temaAssunto in (0 ");
			for (AvaliacaoOnlineTemaAssuntoVO avaliacaoOnlineTemaAssuntoVO : avaliacaoOnlineTemaAssuntoVOs) {
				if(avaliacaoOnlineTemaAssuntoVO.getSelecionado()) {
					sb.append(", ").append(avaliacaoOnlineTemaAssuntoVO.getTemaAssuntoVO().getCodigo());
				}
			}
			sb.append(") ");
		}else if (politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_ESTUDADOS)) {
			sb.append("  and temaassunto in (");
			sb.append(" select distinct temaassunto from unidadeconteudo");
			sb.append(" 		inner join conteudoregistroacesso on conteudoregistroacesso.unidadeconteudo = unidadeconteudo.codigo");
			sb.append(" 		where unidadeconteudo.conteudo = ");
			if(Uteis.isAtributoPreenchido(codigoConteudo)) {
				sb.append(codigoConteudo);			
			}
			else {
			sb.append(" (select conteudo from matriculaperiodoturmadisciplina where codigo =").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo()).append(") ");
			}
			sb.append(" 		and conteudoregistroacesso.dataacesso <= now()");
			sb.append(" 		and conteudoregistroacesso.matriculaperiodoturmadisciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
			sb.append(")");
		} else if (politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE)) {
			
			sb.append("  and temaassunto in (");
			if(Uteis.isAtributoPreenchido(codigoUnidadeConteudo)) {
				sb.append(" select temaassunto from unidadeconteudo where codigo = ").append(codigoUnidadeConteudo);				
			}
			else if(Uteis.isAtributoPreenchido(codigoListaExercicio)) {
				sb.append("	select unidadeconteudo.temaassunto from conteudounidadepaginarecursoeducacional ");
				sb.append("inner join conteudounidadepagina on conteudounidadepagina.codigo  = conteudounidadepaginarecursoeducacional.conteudounidadepagina ");
				sb.append("inner join unidadeconteudo on unidadeconteudo.codigo  = conteudounidadepagina.unidadeconteudo ");
				sb.append("where unidadeconteudo.conteudo  = (select conteudo from matriculaperiodoturmadisciplina where codigo = ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo()).append(") ");
				sb.append("and conteudounidadepaginarecursoeducacional.listaexercicio = ").append(codigoListaExercicio);
				sb.append("	union all ");
				sb.append("	select unidadeconteudo.temaassunto from conteudoregistroacesso");
				sb.append("	inner join unidadeconteudo on unidadeconteudo.codigo  = conteudoregistroacesso.unidadeconteudo");
				sb.append("	where conteudoregistroacesso.matriculaperiodoturmadisciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
				sb.append("	and not exists (");
				sb.append("		select unidadeconteudo.temaassunto from conteudounidadepaginarecursoeducacional");
				sb.append("		inner join conteudounidadepagina on conteudounidadepagina.codigo  = conteudounidadepaginarecursoeducacional.conteudounidadepagina");
				sb.append("		inner join unidadeconteudo on unidadeconteudo.codigo  = conteudounidadepagina.unidadeconteudo");
				sb.append("		where unidadeconteudo.conteudo  = (select conteudo from matriculaperiodoturmadisciplina where codigo =").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo()).append(") ");
				sb.append("		and conteudounidadepaginarecursoeducacional.listaexercicio = ").append(codigoListaExercicio);
				sb.append("	)");
			}
			else {
				if(Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO)) {
				sb.append(" select temaassunto from unidadeconteudo");
				sb.append(" 		inner join conteudoregistroacesso on conteudoregistroacesso.unidadeconteudo = unidadeconteudo.codigo");
				//sb.append(" 		where unidadeconteudo.conteudo = ").append(codigoConteudo);
				sb.append(" 		where conteudoregistroacesso.dataacesso <= now()");
				sb.append(" 		and conteudoregistroacesso.matriculaperiodoturmadisciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
				sb.append(" and unidadeconteudo.temaassunto is not null ");
				sb.append(" order by conteudoregistroacesso.dataacesso desc limit 1");
				}else if(Uteis.isAtributoPreenchido(codigoConteudo)) {
					sb.append(" select distinct temaassunto from unidadeconteudo where conteudo = ").append(codigoConteudo);
			}
			}
			sb.append(")");
		}
		
		
		sb.append("group by codigo, nivelcomplexidadequestao, temaassunto order by random(), nivelcomplexidadequestao, temaassunto");
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
	private void validarQuestoesRandomicamenteComNivelDeComplexidadeComRegraDeDistribuicaoQuantidadeDistribuida(StringBuilder sb, List<QuestaoVO> questoesQueNaoPodemRepetir, String uso, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum, Integer codigoTemaAssunto, Integer codigoUnidadeConteudo, Integer disciplina, Integer qtdeComplexidadeQuestoesFacil, Integer qtdeComplexidadeQuestoesMedio, Integer qtdeComplexidadeQuestoesDificil, Boolean permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno,  Integer codigoConteudo, Boolean randomizarApenasQuestoesCadastradasPeloProfessor, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO) throws Exception{
		boolean habilitarUnionAll =false;
		List<Integer> codigosTemasAssuntos = new ArrayList<Integer>(0);
		if(avaliacaoOnlineTemaAssuntoVOs == null || avaliacaoOnlineTemaAssuntoVOs.isEmpty() || !avaliacaoOnlineTemaAssuntoVOs.stream().anyMatch(t -> t.getSelecionado())) {
			codigosTemasAssuntos = getFacadeFactory().getTemaAssuntoFacade().consultarTemasAssuntosPorConteudo(codigoUnidadeConteudo, codigoConteudo, matriculaPeriodoTurmaDisciplinaVO.getCodigo(), politicaSelecaoQuestaoEnum, usuarioVO);
		}else {			
			codigosTemasAssuntos = avaliacaoOnlineTemaAssuntoVOs.parallelStream().filter(t -> t.getSelecionado()).map(AvaliacaoOnlineTemaAssuntoVO::getTemaAssuntoVO).map(TemaAssuntoVO::getCodigo).collect(Collectors.toList());
		}
		
		if(Uteis.isAtributoPreenchido(qtdeComplexidadeQuestoesFacil)){
			sb.append(getQueryConsultaQuestoesRegraDistribuicaoQuestaoQuantidadeDistribuidaEntreAssuntosQualquerNivel(qtdeComplexidadeQuestoesFacil, false, null, uso, codigoConteudo, disciplina, codigosTemasAssuntos, NivelComplexidadeQuestaoEnum.FACIL, questoesQueNaoPodemRepetir, permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, matriculaPeriodoTurmaDisciplinaVO, randomizarApenasQuestoesCadastradasPeloProfessor, usuarioVO));
			habilitarUnionAll =true;
		}
		if(Uteis.isAtributoPreenchido(qtdeComplexidadeQuestoesMedio)){
			if(habilitarUnionAll){
				sb.append(" union all ");	
			}
			sb.append(getQueryConsultaQuestoesRegraDistribuicaoQuestaoQuantidadeDistribuidaEntreAssuntosQualquerNivel(qtdeComplexidadeQuestoesMedio, false, null, uso, codigoConteudo, disciplina, codigosTemasAssuntos,  NivelComplexidadeQuestaoEnum.MEDIO, questoesQueNaoPodemRepetir, permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, matriculaPeriodoTurmaDisciplinaVO, randomizarApenasQuestoesCadastradasPeloProfessor, usuarioVO));
			habilitarUnionAll =true;
		}
		if(Uteis.isAtributoPreenchido(qtdeComplexidadeQuestoesDificil)){
			if(habilitarUnionAll){
				sb.append(" union all ");	
			}  
			sb.append(getQueryConsultaQuestoesRegraDistribuicaoQuestaoQuantidadeDistribuidaEntreAssuntosQualquerNivel(qtdeComplexidadeQuestoesDificil, false, null, uso, codigoConteudo, disciplina, codigosTemasAssuntos,  NivelComplexidadeQuestaoEnum.DIFICIL, questoesQueNaoPodemRepetir, permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, matriculaPeriodoTurmaDisciplinaVO, randomizarApenasQuestoesCadastradasPeloProfessor, usuarioVO));			
		}
		sb.append(" ) as t ");
	}
	
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
	private void validarQuestoesRandomicamentePorQualquerNivelDeComplexidade( List<QuestaoVO> questaoVOs, List<QuestaoVO> questoesQueNaoPodemRepetir, String uso, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Integer codigoUnidadeConteudo, Integer codigoTemaAssunto, Integer disciplina,Integer qtdeQualquerComplexidade, PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum, RegraDistribuicaoQuestaoEnum regraDistribuicaoQuestaoEnum, Boolean permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, Integer codigoConteudo, Boolean randomizarApenasQuestoesCadastradasPeloProfessor, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO) throws Exception{
		if ((qtdeQualquerComplexidade != null && qtdeQualquerComplexidade > 0)) {
			SqlRowSet rs = null;
			StringBuilder sb = new StringBuilder(getSelectDadosCompleto());
			sb.append(" where questao.codigo in (");
			sb.append("select codigo from (");
			if (politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUALQUER_QUESTAO)) {
				validarQuestoesRandomicamenteComQualquerNivelDeComplexidadePorPoliticaDeSelecaoQualquerQuestao(sb, questaoVOs, uso, disciplina, qtdeQualquerComplexidade, codigoConteudo, matriculaPeriodoTurmaDisciplinaVO, randomizarApenasQuestoesCadastradasPeloProfessor, avaliacaoOnlineTemaAssuntoVOs, usuarioVO);
			} else if (regraDistribuicaoQuestaoEnum.equals(RegraDistribuicaoQuestaoEnum.QUANTIDADE_FIXA_ASSUNTO)) {
				validarQuestoesRandomicamenteComQualquerNivelDeComplexidadeComRegraDeDistribuicaoQuantidadeFixa(sb, questaoVOs, codigoUnidadeConteudo, matriculaPeriodoTurmaDisciplinaVO, politicaSelecaoQuestaoEnum, uso, disciplina, qtdeQualquerComplexidade, codigoConteudo, randomizarApenasQuestoesCadastradasPeloProfessor, avaliacaoOnlineTemaAssuntoVOs, usuarioVO);
			} else if (regraDistribuicaoQuestaoEnum.equals(RegraDistribuicaoQuestaoEnum.QUANTIDADE_DISTRUIBUIDA_ENTRE_ASSUNTOS)) {
				List<Integer> codigosTemasAssuntos = new ArrayList<Integer>(0);
				if(avaliacaoOnlineTemaAssuntoVOs == null || avaliacaoOnlineTemaAssuntoVOs.isEmpty() || !avaliacaoOnlineTemaAssuntoVOs.stream().anyMatch(t -> t.getSelecionado())) {
					codigosTemasAssuntos =  getFacadeFactory().getTemaAssuntoFacade().consultarTemasAssuntosPorConteudo(codigoUnidadeConteudo, codigoConteudo, matriculaPeriodoTurmaDisciplinaVO.getCodigo(), politicaSelecaoQuestaoEnum, usuarioVO);
				}else {
					codigosTemasAssuntos = avaliacaoOnlineTemaAssuntoVOs.parallelStream().filter(t -> t.getSelecionado()).map(AvaliacaoOnlineTemaAssuntoVO::getTemaAssuntoVO).map(TemaAssuntoVO::getCodigo).collect(Collectors.toList());
				}
				sb.append(getQueryConsultaQuestoesRegraDistribuicaoQuestaoQuantidadeDistribuidaEntreAssuntosQualquerNivel(qtdeQualquerComplexidade, true, questaoVOs, uso, codigoConteudo, disciplina, codigosTemasAssuntos, null, questoesQueNaoPodemRepetir, permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, matriculaPeriodoTurmaDisciplinaVO, randomizarApenasQuestoesCadastradasPeloProfessor, usuarioVO));
				sb.append(") as t");
			}
			sb.append(" )");
			rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			questaoVOs.addAll(montarDadosConsultaCompleta(rs));
		}
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
	private void validarQuestoesRandomicamenteComQualquerNivelDeComplexidadeComRegraDeDistribuicaoQuantidadeFixa(StringBuilder sb, List<QuestaoVO> questaoVOs, Integer codigoUnidadeConteudo, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, PoliticaSelecaoQuestaoEnum politicaSelecaoQuestaoEnum, String uso, Integer disciplina, Integer qtdeQualquerComplexidade, Integer codigoConteudo, Boolean randomizarApenasQuestoesCadastradasPeloProfessorAluno, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO) throws Exception{
		sb.append("	select row_number()over(partition by unidadeconteudo.temaassunto order by nivelcomplexidadequestao, unidadeconteudo.temaassunto, random()) as ordem,  ");
		sb.append("	questao.codigo, questao.nivelcomplexidadequestao, unidadeconteudo.temaassunto from unidadeconteudo");
		sb.append("	inner join questaoassunto on questaoassunto.temaassunto = unidadeconteudo.temaassunto");
		sb.append("	inner join questao on questao.codigo = questaoassunto.questao ");
		sb.append("	where (questao.disciplina = ").append(disciplina);
		sb.append(" or questao.disciplina in (select equivalente from disciplinaequivalente where disciplina = ").append(disciplina);
		sb.append("	)) and questao.situacaoquestaoenum = 'ATIVA' ");
		sb.append(uso);
		if (randomizarApenasQuestoesCadastradasPeloProfessorAluno) {
			sb.append(getSqlRegraRandomizarApenasQuestoesCadastradasPeloProfessor(matriculaPeriodoTurmaDisciplinaVO, disciplina, usuarioVO));
		}
		sb.append(" and questao.codigo not in ( 0 ");
		for (QuestaoVO questaoVO : questaoVOs) {
			sb.append(", ").append(questaoVO.getCodigo());
		}
		sb.append(" ) ");
		sb.append("	and unidadeconteudo.conteudo = ").append(codigoConteudo);
		sb.append("	and ((select conteudo from questaoconteudo where questaoconteudo.questao = questao.codigo and questaoconteudo.conteudo = ").append(codigoConteudo).append(" limit 1) is not null");
		sb.append("	or (select codigo from questaoconteudo where questaoconteudo.questao = questao.codigo limit 1) is null)");
		sb.append("	group by questao.codigo, questao.nivelcomplexidadequestao, unidadeconteudo.temaassunto");
		sb.append(" ) as t ");
		sb.append("where (ordem <= ").append(qtdeQualquerComplexidade).append(") ");
		
		if(avaliacaoOnlineTemaAssuntoVOs != null && !avaliacaoOnlineTemaAssuntoVOs.isEmpty() && avaliacaoOnlineTemaAssuntoVOs.stream().anyMatch(t -> t.getSelecionado())) {
			sb.append(" and questaoassunto.temaAssunto in (0 ");
			for (AvaliacaoOnlineTemaAssuntoVO avaliacaoOnlineTemaAssuntoVO : avaliacaoOnlineTemaAssuntoVOs) {
				if(avaliacaoOnlineTemaAssuntoVO.getSelecionado()) {
					sb.append(", ").append(avaliacaoOnlineTemaAssuntoVO.getTemaAssuntoVO().getCodigo());
				}
			}
			sb.append(") ");
		}else if (politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_ESTUDADOS)) {
			sb.append("  and temaassunto in ( ");
			sb.append(" select distinct temaassunto from unidadeconteudo");
			sb.append(" 		inner join conteudoregistroacesso on conteudoregistroacesso.unidadeconteudo = unidadeconteudo.codigo");
			sb.append(" 		where unidadeconteudo.conteudo = ").append(codigoConteudo);
			sb.append(" 		and conteudoregistroacesso.dataacesso <= now()");
			sb.append(" 		and conteudoregistroacesso.matriculaperiodoturmadisciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
			sb.append(" )");
		} else if (politicaSelecaoQuestaoEnum.equals(PoliticaSelecaoQuestaoEnum.QUESTOES_ASSUNTO_UNIDADE)) {
			sb.append("  and temaassunto in ( ");
			sb.append(" select temaassunto from unidadeconteudo where codigo = ").append(codigoUnidadeConteudo);
			sb.append(" ) ");
		}
		
		sb.append(" group by codigo, temaassunto order by random(), temaassunto");
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
	private void validarQuestoesRandomicamenteComQualquerNivelDeComplexidadePorPoliticaDeSelecaoQualquerQuestao(StringBuilder sb, List<QuestaoVO> questaoVOs, String uso, Integer disciplina, Integer qtdeQualquerComplexidade, Integer codigoConteudo, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean randomizarApenasQuestoesCadastradasPeloProfessorAluno, List<AvaliacaoOnlineTemaAssuntoVO> avaliacaoOnlineTemaAssuntoVOs, UsuarioVO usuarioVO) throws Exception{
		sb.append("	select row_number()over(partition by nivelcomplexidadequestao order by nivelcomplexidadequestao, random()) as ordem, ");
		sb.append("	questao.codigo, questao.nivelcomplexidadequestao from questao");
		sb.append("	where (questao.disciplina = ").append(disciplina);
		sb.append(" or questao.disciplina in (select equivalente from disciplinaequivalente where disciplina = ").append(disciplina);
		sb.append(" )) and questao.situacaoquestaoenum = 'ATIVA' ");
		sb.append(uso);
		if (randomizarApenasQuestoesCadastradasPeloProfessorAluno) {
			sb.append(getSqlRegraRandomizarApenasQuestoesCadastradasPeloProfessor(matriculaPeriodoTurmaDisciplinaVO, disciplina, usuarioVO));
		}
		sb.append(" and questao.codigo not in ( 0 ");
		for (QuestaoVO questaoVO : questaoVOs) {
			sb.append(", ").append(questaoVO.getCodigo());
		}
		sb.append(" ) ");
		if(avaliacaoOnlineTemaAssuntoVOs != null && !avaliacaoOnlineTemaAssuntoVOs.isEmpty() && avaliacaoOnlineTemaAssuntoVOs.stream().anyMatch(t -> t.getSelecionado())) {
			sb.append(" and exists ( select questaoassunto.codigo from questaoassunto where  questaoassunto.questao = questao.codigo and questaoassunto.temaAssunto in (0 ");
			for (AvaliacaoOnlineTemaAssuntoVO avaliacaoOnlineTemaAssuntoVO : avaliacaoOnlineTemaAssuntoVOs) {
				if(avaliacaoOnlineTemaAssuntoVO.getSelecionado()) {
					sb.append(", ").append(avaliacaoOnlineTemaAssuntoVO.getTemaAssuntoVO().getCodigo());
				}
			}
			sb.append(")) ");
		}
		sb.append("	and ((select conteudo from questaoconteudo where questaoconteudo.questao = questao.codigo and questaoconteudo.conteudo = ").append(codigoConteudo).append(" limit 1) is not null");
		sb.append("	or (select codigo from questaoconteudo where questaoconteudo.questao = questao.codigo limit 1) is null)");
		sb.append("	group by questao.codigo, questao.nivelcomplexidadequestao");
		sb.append(") as t ");
		sb.append(" group by codigo, nivelcomplexidadequestao order by random(), nivelcomplexidadequestao");
		sb.append(" limit ").append(qtdeQualquerComplexidade);	
	}
	
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
	private StringBuilder getQueryConsultaQuestoesRegraDistribuicaoQuestaoQuantidadeDistribuidaEntreAssuntosQualquerNivel(Integer qtdeComplexidadeQuestoes, Boolean qualquerQuestao, List<QuestaoVO> questaoVOs, String uso, Integer codigoConteudo, Integer codigoDisciplina, List<Integer> codigosTemasAssunto,  NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum, List<QuestaoVO> questoesQueNaoPodemRepetir, Boolean permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean randomizarApenasQuestoesCadastradasPeloProfessorAluno, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = new StringBuilder();
		boolean addUnion = false;
		if(!Uteis.isAtributoPreenchido(codigosTemasAssunto)) {
			throw new Exception("Esta AVALIAÇÃO está parametrizada para randomizar questões por ASSUNTO, porém não foi encontrado nenhum assunto para localizar as questões.");
		}
		Collections.shuffle(codigosTemasAssunto);
		sb.append(" ( select distinct codigo from ( ");
		
		for (Integer codTemaAssunto : codigosTemasAssunto) {
			if (addUnion) {
				sb.append(" union ");
			}
			sb.append("(");
			sb.append("	select row_number()over(partition by unidadeconteudo.temaassunto order by unidadeconteudo.temaassunto, random()) as ordem,  ");
			sb.append("	questao.codigo, questao.nivelcomplexidadequestao, unidadeconteudo.temaassunto from unidadeconteudo");
			sb.append("	inner join questaoassunto on questaoassunto.temaassunto = unidadeconteudo.temaassunto");
			sb.append("	inner join questao on questao.codigo = questaoassunto.questao ");
			sb.append("	where (questao.disciplina = ").append(codigoDisciplina);
			sb.append(" or questao.disciplina in (select equivalente from disciplinaequivalente where disciplina = ").append(codigoDisciplina);
			sb.append("	)) and questao.situacaoquestaoenum = 'ATIVA' ");
			sb.append("	and unidadeconteudo.conteudo = ").append(codigoConteudo);
			sb.append(uso);
			if (randomizarApenasQuestoesCadastradasPeloProfessorAluno) {
				sb.append(getSqlRegraRandomizarApenasQuestoesCadastradasPeloProfessor(matriculaPeriodoTurmaDisciplinaVO, codigoDisciplina, usuarioVO));
			}
			if (Uteis.isAtributoPreenchido(nivelComplexidadeQuestaoEnum)) {
				sb.append("	and questao.nivelcomplexidadequestao = '").append(nivelComplexidadeQuestaoEnum).append("'");
			}
			sb.append("	and unidadeconteudo.temaassunto = ").append(codTemaAssunto);
			if (qualquerQuestao) {
				sb.append(" and questao.codigo not in ( 0 ");
				for (QuestaoVO questaoVO : questaoVOs) {
					sb.append(", ").append(questaoVO.getCodigo());
				}
				sb.append(" )");
			}
			if (!permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno) {
				sb.append(" and questao.codigo not in ( 0 ");
				for (QuestaoVO questaoVO : questoesQueNaoPodemRepetir) {
					sb.append(", ").append(questaoVO.getCodigo());
				}
				sb.append(" )");
			}
			sb.append("	and ((select conteudo from questaoconteudo where questaoconteudo.questao = questao.codigo and questaoconteudo.conteudo = ").append(codigoConteudo).append(" limit 1) is not null");
			sb.append("	or (select codigo from questaoconteudo where questaoconteudo.questao = questao.codigo limit 1) is null)");			
			sb.append(") ");
			addUnion = true;
		}
		sb.append(" ) as questaoDistribuida limit ").append(qtdeComplexidadeQuestoes).append(" ) ");
		return sb;
	}

	public List<QuestaoVO> consultarQuestoesAvaliacaoOnlineMatriculaJaRealizada(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno, UsuarioVO usuarioVO) throws Exception {
		List<QuestaoVO> questaoVOs = new ArrayList<QuestaoVO>();
		if (usuarioVO.getIsApresentarVisaoAluno() && !permitirRepeticoesQuestoeSegundaAvaliacaoOnlineAluno) {
			StringBuilder sqlStr = new StringBuilder();
			sqlStr.append("select questao.codigo as codigo from questao ");
			sqlStr.append("inner join avaliacaoonlinematriculaquestao on avaliacaoonlinematriculaquestao.questao = questao.codigo ");
			sqlStr.append("inner join avaliacaoonlinematricula on avaliacaoonlinematricula.codigo =  avaliacaoonlinematriculaquestao.avaliacaoonlinematricula ");
			sqlStr.append("where avaliacaoonlinematricula.matriculaperiodoturmadisciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getCodigo());
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			QuestaoVO questaoVO = null;
			while (rs.next()) {
				questaoVO = new QuestaoVO();
				questaoVO.setCodigo(rs.getInt("codigo"));
				questaoVOs.add(questaoVO);
			}
		}
		return questaoVOs;
	}

	// --Popular a base para teste, rode quantas vezes quiser
	// insert into questao (usoExercicio, enunciado, situacaoquestaoenum,
	// tipoquestaoenum, datacriacao, responsavelcriacao,
	// nivelcomplexidadequestao, disciplina)
	// values (true, 'Questão DIFICIL Randomica nº
	// '::VARCHAR||(random()::VARCHAR), 'ATIVA', 'UNICA_ESCOLHA',
	// current_date,17961, 'DIFICIL', 1622 );
	//
	// insert into questao (usoExercicio, enunciado, situacaoquestaoenum,
	// tipoquestaoenum, datacriacao, responsavelcriacao,
	// nivelcomplexidadequestao, disciplina)
	// values (true, 'Questão FACIL Randomica nº
	// '::VARCHAR||(random()::VARCHAR), 'ATIVA', 'UNICA_ESCOLHA',
	// current_date,17961, 'FACIL', 1622 );
	//
	// insert into questao (usoExercicio, enunciado, situacaoquestaoenum,
	// tipoquestaoenum, datacriacao, responsavelcriacao,
	// nivelcomplexidadequestao, disciplina)
	// values (true, 'Questão MEDIO Randomica nº
	// '::VARCHAR||(random()::VARCHAR), 'ATIVA', 'UNICA_ESCOLHA',
	// current_date,17961, 'MEDIO', 1622 );
	//
	// insert INTO opcaorespostaquestao (correta, opcaoresposta,
	// ordemapresentacao, questao) (
	// select true, 'Alternativa A', 1, codigo from questao where usoexercicio =
	// true and disciplina = 1622
	// and codigo not in (select questao from opcaorespostaquestao )
	// union all
	// select false, 'Alternativa B', 2, codigo from questao where usoexercicio
	// = true and disciplina = 1622
	// and codigo not in (select questao from opcaorespostaquestao )
	// union all
	// select false, 'Alternativa C', 3, codigo from questao where usoexercicio
	// = true and disciplina = 1622
	// and codigo not in (select questao from opcaorespostaquestao )
	// union all
	// select false, 'Alternativa D', 4, codigo from questao where usoexercicio
	// = true and disciplina = 1622
	// and codigo not in (select questao from opcaorespostaquestao ));

	@Override
	public void realizarCorrecaoQuestao(QuestaoVO questaoVO) {
		questaoVO.setErrouQuestao(true);
		questaoVO.setAcertouQuestao(false);
		questaoVO.setQuestaoNaoRespondida(true);
		int quantidadeOpcaoCorretaQuestao = 0;
		int quantidadeOpcaoCorretaMarcada = 0;
		
		if (questaoVO.getTipoQuestaoEnum().equals(TipoQuestaoEnum.MULTIPLA_ESCOLHA)) {
			for (OpcaoRespostaQuestaoVO objExistente : questaoVO.getOpcaoRespostaQuestaoVOs()) {
				if (objExistente.getCorreta()) {
					quantidadeOpcaoCorretaQuestao++;
				}
			}
		}
		
		for (OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO : questaoVO.getOpcaoRespostaQuestaoVOs()) {
			if (opcaoRespostaQuestaoVO.getCorreta() && opcaoRespostaQuestaoVO.getMarcada()) {
				
				if (questaoVO.getTipoQuestaoEnum().equals(TipoQuestaoEnum.MULTIPLA_ESCOLHA)) {
					quantidadeOpcaoCorretaMarcada++;
				} else {
					questaoVO.setAcertouQuestao(true);
					questaoVO.setErrouQuestao(false);
				}
				
				if ((quantidadeOpcaoCorretaQuestao == quantidadeOpcaoCorretaMarcada) && questaoVO.getTipoQuestaoEnum().equals(TipoQuestaoEnum.MULTIPLA_ESCOLHA)) {
					questaoVO.setAcertouQuestao(true);
					questaoVO.setErrouQuestao(false);					
				}
				
			}
			if (opcaoRespostaQuestaoVO.getMarcada()) {
				questaoVO.setQuestaoNaoRespondida(false);
			}
		}
		if (questaoVO.getQuestaoNaoRespondida()) {
			questaoVO.setAcertouQuestao(false);
			questaoVO.setErrouQuestao(false);
		}

	}

	@Override
	public void realizarVerificacaoQuestaoRespondida(QuestaoVO questaoVO) {
		questaoVO.setQuestaoNaoRespondida(false);
		for (OpcaoRespostaQuestaoVO opcaoRespostaQuestaoVO : questaoVO.getOpcaoRespostaQuestaoVOs()) {
			if (opcaoRespostaQuestaoVO.getMarcada()) {
				questaoVO.setQuestaoNaoRespondida(false);
				return;
			}
		}
		questaoVO.setQuestaoNaoRespondida(true);
	}

	@Override
	public List<QuestaoVO> consultarQuestoesPorUsuario(String enunciado, Integer disciplina, SituacaoQuestaoEnum situacaoQuestaoEnum, Boolean usoOnline, Boolean usoPresencial, Boolean usoExercicio, Boolean usoAtividadeDiscursiva, TipoQuestaoEnum tipoQuestaoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum, Boolean controleAcesso, String idEntidade, UsuarioVO usuario, Integer limite, Integer pagina, Integer codigoConteudo) throws Exception {
		consultar(idEntidade, controleAcesso, usuario);
		List<Object> param = new ArrayList<>();
		StringBuilder sb = new StringBuilder(getSelectDadosBasicos());
		sb.append(" WHERE 0=0 ");		
		if (!enunciado.trim().isEmpty()) {
			param.add(PERCENT + enunciado.trim() + PERCENT);
			sb.append(" and upper(sem_acentos(sem_acentos_html(questao.enunciado))) like(upper(sem_acentos((?)))) ");
		}
		if (disciplina != null && disciplina > 0) {
			sb.append(" and disciplina.codigo = ").append(disciplina);
		}
		if (situacaoQuestaoEnum != null) {
			sb.append(" and questao.situacaoQuestaoEnum = '").append(situacaoQuestaoEnum.name()).append("' ");
		}
		if (nivelComplexidadeQuestaoEnum != null) {
			sb.append(" and questao.nivelComplexidadeQuestao = '").append(nivelComplexidadeQuestaoEnum.name()).append("' ");
		}
		if (tipoQuestaoEnum != null) {
			sb.append(" and questao.tipoQuestaoEnum = '").append(tipoQuestaoEnum.name()).append("' ");
		}
		if (usoAtividadeDiscursiva != null && usoAtividadeDiscursiva) {
			sb.append(" and questao.usoAtividadeDiscursiva = ").append(usoAtividadeDiscursiva);
		}
		String andOr = " and ( ";
		if (usoOnline != null && usoOnline) {
			sb.append(andOr).append(" questao.usoOnline = true ");
			andOr = " or ";
		}

		if (usoExercicio != null && usoExercicio) {
			sb.append(andOr).append(" questao.usoExercicio = true ");
			andOr = " or ";
		}
		if (usoPresencial != null && usoPresencial) {
			sb.append(andOr).append(" questao.usoPresencial = true ");
			andOr = " or ";
		}
		if (andOr.contains("or")) {
			sb.append(") ");
		}
		andOr = null;

		sb.append(" and questao.responsavelCriacao = ").append(usuario.getCodigo());
		if (codigoConteudo != 0) {
			sb.append(" and (");
			sb.append(" (select conteudo from questaoconteudo where questaoconteudo.questao = questao.codigo and questaoconteudo.conteudo = ").append(codigoConteudo).append(" limit 1) is not null");
			sb.append(" or");
			sb.append(" (select codigo from questaoconteudo where questaoconteudo.questao = questao.codigo limit 1) is null)");
		}
		sb.append(" order by  remover_tags_html(sem_acentos(sem_acentos_html(questao.enunciado))) ");
		if (limite != null && limite > 0) {
			sb.append(" limit ").append(limite).append(" offset ").append(pagina);
		}

		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), param.toArray()), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	}

	@Override
	public Integer consultarTotalResgistroPorUsuario(String enunciado, Integer disciplina, SituacaoQuestaoEnum situacaoQuestaoEnum, Boolean usoOnline, Boolean usoPresencial, Boolean usoExercicio, Boolean usoAtividadeDiscursiva, TipoQuestaoEnum tipoQuestaoEnum, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum, Integer codigoConteudo, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("Select count(codigo) as qtde from questao ");
		sb.append(" WHERE 0=0 ");
		List<Object> param = new ArrayList<>();
		if (enunciado != null && !enunciado.trim().isEmpty()) {
			param.add(PERCENT + enunciado.trim() + PERCENT);
			sb.append(" and upper(sem_acentos(sem_acentos_html(questao.enunciado))) like(upper(sem_acentos((?)))) ");
		}
		if (disciplina != null && disciplina > 0) {
			sb.append(" and disciplina = ").append(disciplina);
		}
		if (situacaoQuestaoEnum != null) {
			sb.append(" and questao.situacaoQuestaoEnum = '").append(situacaoQuestaoEnum.name()).append("' ");
		}
		if (tipoQuestaoEnum != null) {
			sb.append(" and questao.tipoQuestaoEnum = '").append(tipoQuestaoEnum.name()).append("' ");
		}
		if (nivelComplexidadeQuestaoEnum != null) {
			sb.append(" and questao.nivelComplexidadeQuestao = '").append(nivelComplexidadeQuestaoEnum.name()).append("' ");
		}
		sb.append(" and questao.responsavelCriacao = ").append(usuario.getCodigo());
		String andOr = " and ( ";
		if (usoOnline != null && usoOnline) {
			sb.append(andOr).append(" questao.usoOnline = true ");
			andOr = " or ";
		}
		if (usoAtividadeDiscursiva != null && usoAtividadeDiscursiva) {
			sb.append(andOr).append(" questao.usoAtividadeDiscursiva = true ");
			andOr = " or ";
		}
		if (usoExercicio != null && usoExercicio) {
			sb.append(andOr).append(" questao.usoExercicio = true ");
			andOr = " or ";
		}
		if (usoPresencial != null && usoPresencial) {
			sb.append(andOr).append(" questao.usoPresencial = true ");
			andOr = " or ";
		}
		if (andOr.contains("or")) {
			sb.append(") ");
		}
		andOr = null;
		sb.append(" and questao.responsavelCriacao = ").append(usuario.getCodigo());
		if (codigoConteudo != 0) {
			sb.append(" and (");
			sb.append(" (select conteudo from questaoconteudo where questaoconteudo.questao = questao.codigo and questaoconteudo.conteudo = ").append(codigoConteudo).append(" limit 1) is not null");
			sb.append(" or");
			sb.append(" (select codigo from questaoconteudo where questaoconteudo.questao = questao.codigo limit 1) is null)");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), param.toArray());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public Integer consultarQuantidadeQuestoesPorDisciplinaNivelComplexidadeETemaAssunto(Integer codigoDisciplina, Integer codigoTemaAssunto, NivelComplexidadeQuestaoEnum nivelComplexidadeQuestaoEnum, SituacaoQuestaoEnum situacaoQuestaoEnum, Boolean usoOnline, Boolean usoPresencial, Boolean usoExercicio, Boolean usoAtividadeDiscursiva) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select count(*) from questao");
		if (!codigoTemaAssunto.equals(0)) {
			sqlStr.append(" inner join questaoassunto on questaoassunto.questao = questao.codigo");
		}
		sqlStr.append(" where questao.disciplina = ").append(codigoDisciplina);
		sqlStr.append(" and questao.nivelcomplexidadequestao = '").append(nivelComplexidadeQuestaoEnum).append("'");
		if (!codigoTemaAssunto.equals(0)) {
			sqlStr.append(" and questaoassunto.temaassunto = ").append(codigoTemaAssunto);
		}
		if (situacaoQuestaoEnum != null) {
			sqlStr.append(" and questao.situacaoQuestaoEnum = '").append(situacaoQuestaoEnum.name()).append("' ");
		}
		String andOr = " and ( ";
		if (usoOnline != null && usoOnline) {
			sqlStr.append(andOr).append(" questao.usoOnline = true ");
			andOr = " or ";
		}
		if (usoAtividadeDiscursiva != null && usoAtividadeDiscursiva) {
			sqlStr.append(andOr).append(" questao.usoAtividadeDiscursiva = true ");
			andOr = " or ";
		}
		if (usoExercicio != null && usoExercicio) {
			sqlStr.append(andOr).append(" questao.usoExercicio = true ");
			andOr = " or ";
		}
		if (usoPresencial != null && usoPresencial) {
			sqlStr.append(andOr).append(" questao.usoPresencial = true ");
			andOr = " or ";
		}
		if (andOr.contains("or")) {
			sqlStr.append(") ");
		}
		andOr = null;
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		Integer qtde = 0;
		if (rs.next()) {
			qtde = rs.getInt("count");
		}
		return qtde;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public QuestaoVO realizarClonagemReaOutraDisciplinaQuestao(ConteudoVO obj, QuestaoVO questaoVO, boolean isConsultaQuestao, UsuarioVO usuarioVO) throws Exception {
		QuestaoVO novaQuestao = new QuestaoVO();
		QuestaoVO questaoConsultada = new QuestaoVO();
		if(isConsultaQuestao){
			questaoConsultada = consultarPorChavePrimaria(questaoVO.getCodigo());
		}else{
			questaoConsultada = questaoVO;
		}	
		novaQuestao = questaoConsultada.clone();
		novaQuestao.setEnunciado(novaQuestao.getEnunciado().replace(" - Clone", ""));
		novaQuestao.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.ATIVA);
		novaQuestao.setDisciplina(obj.getDisciplina());
		for (QuestaoConteudoVO questaoConteudo : novaQuestao.getQuestaoConteudoVOs()) {
			questaoConteudo.setConteudoVO(obj);
		}
		persistir(novaQuestao, false, "Questao", usuarioVO);
		return novaQuestao;
	}
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<QuestaoVO> consultarQuestaoParaClonagemReaOutraDisciplina(ConteudoVO obj, ConteudoUnidadePaginaRecursoEducacionalVO cupre) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectDadosCompleto());
		sb.append(" where situacaoquestaoenum = 'ATIVA'");
		if(cupre.getTipoRecursoEducacional().isTipoAvaliacaoOnline()){
			sb.append(" and disciplina.codigo = ").append(cupre.getAvaliacaoOnlineVO().getDisciplinaVO().getCodigo());
			sb.append(" and usoonline = true ");
		}
		if(cupre.getTipoRecursoEducacional().isTipoRecursoExercicio()){
			sb.append(" and disciplina.codigo = ").append(cupre.getListaExercicio().getDisciplina().getCodigo());
			sb.append(" and usoexercicio = true ");	
		}
		sb.append(" and remover_tags_html(sem_acentos(sem_acentos_html(enunciado))) not in ( ");
		sb.append(" ( select remover_tags_html(sem_acentos(sem_acentos_html(enunciado))) from questao ");
		sb.append(" where situacaoquestaoenum = 'ATIVA' ");
		if(cupre.getTipoRecursoEducacional().isTipoAvaliacaoOnline()){
			sb.append(" and disciplina = ").append(obj.getDisciplina().getCodigo());
			sb.append(" and usoonline = true ");
			sb.append(" union ");
			sb.append(" select remover_tags_html(sem_acentos(sem_acentos_html(questao.enunciado)))  from avaliacaoonlinequestao ");
			sb.append(" inner join questao on avaliacaoonlinequestao.questao = questao.codigo and avaliacaoonlinequestao.avaliacaoonline =").append(cupre.getAvaliacaoOnlineVO().getCodigo()).append(" ) ");
		}
		if(cupre.getTipoRecursoEducacional().isTipoRecursoExercicio()){
			sb.append(" and disciplina = ").append(obj.getDisciplina().getCodigo());
			sb.append(" and usoexercicio = true ");	
			sb.append(" union ");
			sb.append(" select remover_tags_html(sem_acentos(sem_acentos_html(questao.enunciado)))  from questaolistaexercicio ");
			sb.append(" inner join questao on questaolistaexercicio.questao = questao.codigo and questaolistaexercicio.listaexercicio =").append(cupre.getListaExercicio().getCodigo()).append(" ) ");
		}
		sb.append(" ) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsultaCompleta(rs);
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public QuestaoVO consultarQuestaoExistenteParaClonagemPorCodigoQuestaoPorDisciplina(Integer questao, Integer disciplina, boolean isOnline, boolean isExercicio) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectDadosCompleto());
		sb.append(" where disciplina = ").append(disciplina);		
		sb.append(" and situacaoquestaoenum = 'ATIVA' ");		
		if(isOnline && !isExercicio){
			sb.append(" and usoonline = true ");
		}
		if(isExercicio && !isOnline){
			sb.append(" and usoexercicio = true ");	
		}
		if(isExercicio && isOnline){
			sb.append(" and ( usoexercicio = true or usoonline = true )");	
		}
		sb.append(" and remover_tags_html(sem_acentos(sem_acentos_html(enunciado))) in ( ");
		sb.append(" select remover_tags_html(sem_acentos(sem_acentos_html(enunciado))) from questao  where codigo = ").append(questao).append(" ) ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<QuestaoVO> questaoVOs = montarDadosConsultaCompleta(rs);
		if (questaoVOs.isEmpty()) {
			return new QuestaoVO();
		}
		return questaoVOs.get(0);
	}
	
	@Override
	public QuestaoVO consultarPorChavePrimariaUsuario(Integer codigo, Boolean usoOnline, Boolean usoPresencial, Boolean usoExercicio,
			Boolean usoAtividadeDiscursiva, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectDadosCompleto());
		sb.append(" where questao.codigo = ").append(codigo);
		sb.append(" and questao.responsavelCriacao = ").append(usuario.getCodigo());
		String andOr = " and ( ";
		if (usoOnline != null && usoOnline) {
			sb.append(andOr).append(" questao.usoOnline = true ");
			andOr = " or ";
		}

		if (usoExercicio != null && usoExercicio) {
			sb.append(andOr).append(" questao.usoExercicio = true ");
			andOr = " or ";
		}
		if (usoPresencial != null && usoPresencial) {
			sb.append(andOr).append(" questao.usoPresencial = true ");
			andOr = " or ";
		}
		if (andOr.contains("or")) {
			sb.append(") ");
		}
		sb.append(" order by orp.ordemApresentacao");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<QuestaoVO> questaoVOs = montarDadosConsultaCompleta(rs);
		if (questaoVOs.isEmpty()) {
			throw new Exception("Dados não encontrados(Questão).");
		}
		return questaoVOs.get(0);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoMotivoAnulacaoPorCodigo(final Integer questao, final SituacaoQuestaoEnum situacao, String motivoAnulacaoQuestao, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE questao set situacaoquestaoenum=?, motivoAnulacaoQuestao=? WHERE codigo = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setString(++i, situacao.toString());
					sqlAlterar.setString(++i, motivoAnulacaoQuestao.toString());
					sqlAlterar.setInt(++i, questao.intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAnulacaoQuestao(QuestaoVO questaoVO, List<MatriculaVO> listaMatriculaCorrigirNotaVOs, Boolean simularAnulacao, UsuarioVO usuarioVO) throws Exception {
		if (!simularAnulacao) {
			if (questaoVO.getMotivoAnulacaoQuestao().equals("")) {
				throw new Exception("O campo MOTIVO ANULAÇÃO deve ser informado.");
			}
			questaoVO.setSituacaoQuestaoEnum(SituacaoQuestaoEnum.ANULADA);
			alterarSituacaoMotivoAnulacaoPorCodigo(questaoVO.getCodigo(), SituacaoQuestaoEnum.ANULADA, questaoVO.getMotivoAnulacaoQuestao(), usuarioVO);
		}
		realizarAtribuicaoNotaAlunoResponderamQuestaoAnulada(questaoVO, listaMatriculaCorrigirNotaVOs, simularAnulacao, usuarioVO);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarAtribuicaoNotaAlunoResponderamQuestaoAnulada(QuestaoVO questaoVO, List<MatriculaVO> listaMatriculaCorrigirNotaVOs, Boolean simularAnulacao, UsuarioVO usuarioVO) throws Exception {
		for (MatriculaVO matriculaVO : listaMatriculaCorrigirNotaVOs) {
			AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO = getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().consultarPorMatriculaEQuestaoAvaliacaoOnline(matriculaVO.getMatricula(), questaoVO.getCodigo(), matriculaVO.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
			if (Uteis.isAtributoPreenchido(avaliacaoOnlineMatriculaVO.getCodigo())) {
				avaliacaoOnlineMatriculaVO.setTamanhoListaAvaliacaoOnlineMatriculaQuestao(getFacadeFactory().getAvaliacaoOnlineMatriculaQuestaoInterfaceFacade().consultarPorAvaliacaoOnlineMatricula(avaliacaoOnlineMatriculaVO.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO).size());
				getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().executarCorrecaoQuestaoAnuladaAvaliacaoOnline(avaliacaoOnlineMatriculaVO, simularAnulacao, usuarioVO);
			}
			if(simularAnulacao) {
				matriculaVO.setAvaliacaoOnlineMatriculaVO(avaliacaoOnlineMatriculaVO);
				matriculaVO.setSituacaoAtividadeRespostaQuestao(SituacaoAtividadeRespostaEnum.ANULADA);
			}
		}
	}
}
