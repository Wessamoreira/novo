package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.ForumPessoaVO;
import negocio.comuns.academico.ForumVO;
import negocio.comuns.academico.enumeradores.OpcaoOrdenacaoForumEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.PublicoAlvoForumEnum;
import negocio.comuns.academico.enumeradores.RestricaoPublicoAlvoForumEnum;
import negocio.comuns.academico.enumeradores.SituacaoForumEnum;
import negocio.comuns.academico.enumeradores.TipoPeriodoDisponibilidadeForumEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ForumInterfaceFacade;

import static negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoVisaoProfessorEnum.APRESENTAR_SOMENTE_FORUM_CRIADO_PROPRIO_PROFESSOR;

@Repository
@Lazy
public class Forum extends ControleAcesso implements ForumInterfaceFacade {

	/**
     * 
     */
	private static final long serialVersionUID = -2902348901081380195L;

	@Override
	public void persistir(ForumVO forum, String idEntidade, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		if (forum.isNovoObj()) {
			if (controlarAcesso) {
				incluir(idEntidade, controlarAcesso, usuario);
			}
			forum.setDataCriacao(new Date());
			forum.getResponsavelCriacao().setCodigo(usuario.getCodigo());
			forum.getResponsavelCriacao().setNome(usuario.getNome());
			forum.getResponsavelCriacao().getPessoa().setNome(usuario.getPessoa().getNome());
			forum.getResponsavelCriacao().getPessoa().setCodigo(usuario.getPessoa().getCodigo());
			forum.getResponsavelCriacao().getPessoa().setArquivoImagem(usuario.getPessoa().getArquivoImagem());
			incluir(forum, controlarAcesso, usuario);
		} else {
			if (controlarAcesso) {
				alterar(idEntidade, controlarAcesso, usuario);
			}
			alterar(forum, controlarAcesso, usuario);
		}

	}

	private void validarDados(ForumVO forumVO) throws ConsistirException {
		ConsistirException ex = new ConsistirException();
		if (forumVO.getTema().trim().isEmpty() || Uteis.retiraTags(forumVO.getTema()).trim().isEmpty()) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Forum_tema"));
		}
		if (!Uteis.isAtributoPreenchido(forumVO.getPublicoAlvoForumEnum())) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Forum_publicoAlvoForumEnum"));
		}
		if (forumVO.getPublicoAlvoForumEnum().isPublicoProfessor() && !Uteis.isAtributoPreenchido(forumVO.getRestricaoPublicoAlvoForumEnum())) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Forum_restricaoPublicoAlvoForumEnum"));
		}
		if (forumVO.getPublicoAlvoForumEnum().isPublicoProfessor() && forumVO.getRestricaoPublicoAlvoForumEnum().isRestricaoAreaConhecimento() && !Uteis.isAtributoPreenchido(forumVO.getAreaConhecimentoVO())) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Forum_areaConhecimento"));
		}
		if (forumVO.getPublicoAlvoForumEnum().isPublicoProfessor() && forumVO.getRestricaoPublicoAlvoForumEnum().isRestricaoCurso() && !Uteis.isAtributoPreenchido(forumVO.getCursoVO())) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Forum_curso"));
		}
		if (!Uteis.isAtributoPreenchido(forumVO.getDisciplina())) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Forum_disciplina"));
		}
		if (forumVO.getPublicoAlvoForumEnum().isPublicoProfessor() && forumVO.getRestricaoPublicoAlvoForumEnum().isRestricaoEspecificos() && !Uteis.isAtributoPreenchido(forumVO.getForumPessoaVOs())) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Forum_forumPessoa"));
		}
		if (forumVO.getTipoPeriodoDisponibilizacao().equals(TipoPeriodoDisponibilidadeForumEnum.ANO_SEMESTRE.name()) && (forumVO.getSemestre().equals("") || forumVO.getAno().equals(""))) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Forum_anoSemestre"));
		}
		if (forumVO.getTipoPeriodoDisponibilizacao().equals(TipoPeriodoDisponibilidadeForumEnum.ANO.name()) && (forumVO.getAno().equals(""))) {
			ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_Forum_ano"));
		}
		if (!ex.getListaMensagemErro().isEmpty()) {
			throw ex;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final ForumVO forumVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {
			validarDados(forumVO);
			final StringBuilder sql = new StringBuilder("INSERT INTO Forum ( ");
			sql.append(" responsavelCriacao, dataCriacao, disciplina, turma, conteudo, situacaoForum, tema, forumavaliado, publicoalvoforumenum, restricaopublicoalvoforumenum, ");
			sql.append(" curso, areaconhecimento, ano, semestre ");
			sql.append(" ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
			sql.append(" returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			forumVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
					sqlInserir.setInt(x++, forumVO.getResponsavelCriacao().getCodigo());
					sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(forumVO.getDataCriacao()));
					sqlInserir.setInt(x++, forumVO.getDisciplina().getCodigo());
					if (forumVO.getTurma().getCodigo() > 0) {
						sqlInserir.setInt(x++, forumVO.getTurma().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (forumVO.getConteudo().getCodigo() != null && forumVO.getConteudo().getCodigo() > 0) {
						sqlInserir.setInt(x++, forumVO.getConteudo().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setString(x++, forumVO.getSituacaoForum().name());
					sqlInserir.setString(x++, forumVO.getTema());
					sqlInserir.setBoolean(x++, forumVO.isForumAvaliado());
					sqlInserir.setString(x++, forumVO.getPublicoAlvoForumEnum().name());
					if (Uteis.isAtributoPreenchido(forumVO.getRestricaoPublicoAlvoForumEnum())) {
						sqlInserir.setString(x++, forumVO.getRestricaoPublicoAlvoForumEnum().name());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (forumVO.getCursoVO().getCodigo() > 0) {
						sqlInserir.setInt(x++, forumVO.getCursoVO().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					if (forumVO.getAreaConhecimentoVO().getCodigo() > 0) {
						sqlInserir.setInt(x++, forumVO.getAreaConhecimentoVO().getCodigo());
					} else {
						sqlInserir.setNull(x++, 0);
					}
					sqlInserir.setString(x++, forumVO.getAno());
					sqlInserir.setString(x++, forumVO.getSemestre());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Integer>() {

				public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						forumVO.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			getFacadeFactory().getForumPessoaFacade().persistirForumPessoa(forumVO, controlarAcesso, usuario);
		} catch (Exception e) {
			forumVO.setNovoObj(true);
			throw e;
		}
	}

	@Override
	public void inativar(ForumVO forumVO, String idEntidade, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {
			forumVO.setDataInativacao(new Date());
			forumVO.setResponsavelInativacao(usuario);
			forumVO.setSituacaoForum(SituacaoForumEnum.INATIVO);
			persistir(forumVO, idEntidade, controlarAcesso, usuario);
		} catch (Exception e) {
			forumVO.setSituacaoForum(SituacaoForumEnum.ATIVO);
			forumVO.setDataInativacao(null);
			forumVO.setResponsavelInativacao(null);
			throw e;
		}
	}

	@Override
	public void ativar(ForumVO forumVO, String idEntidade, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {
			forumVO.setSituacaoForum(SituacaoForumEnum.ATIVO);
			forumVO.setDataInativacao(null);
			forumVO.setResponsavelInativacao(null);
			persistir(forumVO, idEntidade, controlarAcesso, usuario);
		} catch (Exception e) {
			forumVO.setSituacaoForum(SituacaoForumEnum.INATIVO);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final ForumVO forumVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {
			validarDados(forumVO);
			final StringBuilder sql = new StringBuilder("UPDATE Forum SET ");
			sql.append(" disciplina = ?, turma = ?, conteudo = ?, situacaoForum = ?, tema = ?, forumavaliado= ?, publicoalvoforumenum= ?, restricaopublicoalvoforumenum= ?, dataInativacao = ?, responsavelInativacao = ?, ");
			sql.append("curso = ?, areaConhecimento = ?, ano=?, semestre=? ");
			sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					int x = 1;
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setInt(x++, forumVO.getDisciplina().getCodigo());
					if (forumVO.getTurma().getCodigo() > 0) {
						sqlAlterar.setInt(x++, forumVO.getTurma().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (forumVO.getConteudo().getCodigo() != null && forumVO.getConteudo().getCodigo() > 0) {
						sqlAlterar.setInt(x++, forumVO.getConteudo().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setString(x++, forumVO.getSituacaoForum().name());
					sqlAlterar.setString(x++, forumVO.getTema());
					sqlAlterar.setBoolean(x++, forumVO.isForumAvaliado());
					sqlAlterar.setString(x++, forumVO.getPublicoAlvoForumEnum().name());
					if (Uteis.isAtributoPreenchido(forumVO.getRestricaoPublicoAlvoForumEnum())) {
						sqlAlterar.setString(x++, forumVO.getRestricaoPublicoAlvoForumEnum().name());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (forumVO.getDataInativacao() != null) {
						sqlAlterar.setTimestamp(x++, Uteis.getDataJDBCTimestamp(forumVO.getDataInativacao()));
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (forumVO.getResponsavelInativacao() != null && forumVO.getResponsavelInativacao().getCodigo() > 0) {
						sqlAlterar.setInt(x++, forumVO.getResponsavelInativacao().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (forumVO.getCursoVO().getCodigo() > 0) {
						sqlAlterar.setInt(x++, forumVO.getCursoVO().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					if (forumVO.getAreaConhecimentoVO().getCodigo() > 0) {
						sqlAlterar.setInt(x++, forumVO.getAreaConhecimentoVO().getCodigo());
					} else {
						sqlAlterar.setNull(x++, 0);
					}
					sqlAlterar.setString(x++, forumVO.getAno());
					sqlAlterar.setString(x++, forumVO.getSemestre());
					sqlAlterar.setInt(x++, forumVO.getCodigo());
					return sqlAlterar;
				}
			}) <= 0) {
				incluir(forumVO, controlarAcesso, usuario);
				return;
			}
			getFacadeFactory().getForumPessoaFacade().persistirForumPessoa(forumVO, controlarAcesso, usuario);
			forumVO.setNovoObj(false);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void excluir(final ForumVO forum, UsuarioVO usuarioVO) throws Exception {
			final StringBuilder sql = new StringBuilder( "DELETE FROM forum ");
			sql.append(" where codigo = ? ");
			sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement ps = arg0.prepareStatement(sql.toString());
					ps.setInt(1, forum.getCodigo());
					return ps;
				}
			}) == 0) {
				return;
			}
		
	}

	private String getSelectCompleto(Integer usuario) {
		StringBuilder sb = new StringBuilder("SELECT forum.codigo as  \"forum.codigo\", forum.responsavelcriacao as  \"forum.responsavelcriacao\", forum.datacriacao as  \"forum.datacriacao\", forum.responsavelinativacao as  \"forum.responsavelinativacao\",");
		sb.append(" forum.datainativacao as  \"forum.datainativacao\", forum.disciplina as  \"forum.disciplina\", forum.turma as  \"forum.turma\", forum.conteudo as  \"forum.conteudo\", forum.situacaoforum as  \"forum.situacaoforum\",  ");
		sb.append(" forum.tema as  \"forum.tema\", forum.forumavaliado as  \"forum.forumavaliado\", forum.publicoalvoforumenum as  \"forum.publicoalvoforumenum\", forum.curso as  \"forum.curso\", ");
		sb.append(" forum.restricaopublicoalvoforumenum as  \"forum.restricaopublicoalvoforumenum\", forum.areaconhecimento as  \"forum.areaconhecimento\", forum.ano as \"forum.ano\", forum.semestre as \"forum.semestre\", ");
		sb.append(" disciplina.nome as \"disciplina.nome\", turma.identificadorTurma as \"turma.identificadorTurma\", ");
		sb.append(" responsavelCriacao.nome AS \"responsavelCriacao.nome\", ");
		sb.append(" pessoaCriacao.codigo AS \"pessoaCriacao.codigo\", pessoaCriacao.nome AS \"pessoaCriacao.nome\", ");
		sb.append(" arquivo.pastaBaseArquivo as \"arquivo.pastaBaseArquivo\", arquivo.codigo AS \"arquivo.codigo\", arquivo.nome AS \"arquivo.nome\",  ");
		sb.append(" responsavelInativacao.nome as \"responsavelInativacao.nome\", ");
		sb.append(" curso.nome as \"curso.nome\", ");
		sb.append(" areaConhecimento.nome as \"areaConhecimento.nome\", ");
		sb.append(" (select count(distinct ForumInteracao.codigo) from ForumInteracao ");
		sb.append(" left join ForumAcesso on ForumAcesso.forum = ForumInteracao.forum and ForumAcesso.usuarioAcesso =  ").append(usuario);
		sb.append(" and ForumAcesso.codigo = (select max(fa.codigo) from ForumAcesso fa where fa.forum = Forum.codigo and fa.usuarioAcesso = ").append(usuario).append(")");
		sb.append(" where ForumInteracao.forum = Forum.codigo and excluido = false and ForumInteracao.usuarioInteracao != ").append(usuario).append(" and (ForumAcesso.codigo is null or forumAcesso.dataAcesso < ForumInteracao.dataInteracao) )");
		sb.append(" as qtdeAtualizacao");
		sb.append(" FROM forum ");
		sb.append(" inner join disciplina on forum.disciplina = disciplina.codigo ");
		sb.append(" inner join Usuario  as responsavelCriacao on forum.responsavelCriacao = responsavelCriacao.codigo ");
		sb.append(" left join Usuario  as responsavelInativacao on forum.responsavelInativacao = responsavelInativacao.codigo ");
		sb.append(" left join Pessoa  as pessoaCriacao on pessoaCriacao.codigo = responsavelCriacao.pessoa ");
		sb.append(" left join Arquivo on Arquivo.codigo = pessoaCriacao.arquivoImagem ");
		sb.append(" left join turma on forum.turma = turma.codigo ");
		sb.append(" left join curso on forum.curso = curso.codigo ");
		sb.append(" left join areaConhecimento on forum.areaConhecimento = areaConhecimento.codigo ");
		return sb.toString();
	}

	@Override
	public Integer consultarTotalRegistroForumPorMatricula(String matricula, Integer disciplina, String tema, String anoSemestre, Integer usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT count(distinct forum.codigo) as qtde ");
		sb.append(" FROM forum ");
		sb.append(" left join turma on forum.turma = turma.codigo ");
		sb.append(" left join turmaagrupada on turmaagrupada.turmaorigem = forum.turma ");
		sb.append(" left join ForumAcesso on ForumAcesso.forum = Forum.codigo and ForumAcesso.usuarioAcesso = ").append(usuario);
		sb.append(" and ForumAcesso.codigo = (select max(fa.codigo) from ForumAcesso fa where fa.forum = Forum.codigo and fa.usuarioAcesso = ").append(usuario).append(")");
		sb.append(" where Forum.situacaoForum = '").append(SituacaoForumEnum.ATIVO.name()).append("' ");
		sb.append(" and Forum.publicoAlvoForumEnum =  '").append(PublicoAlvoForumEnum.ALUNO).append("' ");
		if(Uteis.isAtributoPreenchido(disciplina)) {
			sb.append("AND (forum.disciplina = ").append(disciplina);
			sb.append(" or forum.disciplina in (select equivalente from disciplinaequivalente where disciplina = ").append(disciplina);
			sb.append(" union select disciplina from disciplinaequivalente where equivalente = ").append(disciplina);
			sb.append(")) ");
		}
		sb.append(getSqlPeriodicidadeForum(matricula, anoSemestre));
		if (tema != null && !tema.trim().isEmpty()) {
			sb.append(" and remover_tags_html(sem_acentos(sem_acentos_html(tema))) ilike remover_tags_html(sem_acentos(sem_acentos_html(?))) ");	
		}
//		sb.append(" and case when ForumAcesso.codigo is null then ");
//		sb.append(" case when Forum.turma is null then ");
//		sb.append(" (select MatriculaPeriodoTurmaDisciplina.disciplina from MatriculaPeriodoTurmaDisciplina ");
//		sb.append(" inner join MatriculaPeriodo on MatriculaPeriodo.codigo = MatriculaPeriodoTurmaDisciplina.MatriculaPeriodo ");
//		sb.append(" where MatriculaPeriodo.matricula = '").append(matricula).append("' and MatriculaPeriodoTurmaDisciplina.disciplina = Forum.disciplina limit 1) ");
//		sb.append(" else  ");
//		sb.append(" (select MatriculaPeriodoTurmaDisciplina.disciplina from MatriculaPeriodoTurmaDisciplina ");
//		sb.append(" inner join MatriculaPeriodo on MatriculaPeriodo.codigo = MatriculaPeriodoTurmaDisciplina.MatriculaPeriodo ");
//		sb.append(" where MatriculaPeriodo.matricula = '").append(matricula).append("' and MatriculaPeriodoTurmaDisciplina.disciplina = Forum.disciplina ");
//		
//		sb.append(" and case when turma.turmaagrupada = false and MatriculaPeriodoTurmaDisciplina.turmapratica is null and MatriculaPeriodoTurmaDisciplina.turmateorica is null ");
//		sb.append(" then (MatriculaPeriodoTurmaDisciplina.turma = Forum.turma or  MatriculaPeriodo.turma = Forum.turma) ");
//		sb.append(" else ");
//		sb.append(" case when turma.turmaagrupada and MatriculaPeriodoTurmaDisciplina.turmapratica is null and MatriculaPeriodoTurmaDisciplina.turmateorica is null ");
//		sb.append(" then ");
//		sb.append(" (turmaagrupada.turmaorigem = Forum.turma) ");
//		sb.append(" else ");
//		sb.append(" case when MatriculaPeriodoTurmaDisciplina.turmapratica is not null ");
//		sb.append(" then (MatriculaPeriodoTurmaDisciplina.turmapratica = forum.turma) ");
//		sb.append(" else ");
//		sb.append(" case when MatriculaPeriodoTurmaDisciplina.turmateorica is not null ");
//		sb.append(" then (MatriculaPeriodoTurmaDisciplina.turmateorica = forum.turma) ");
//		sb.append(" end end end end limit 1) ");
//		sb.append(" end else 1 end > 0 ");
		SqlRowSet rs  = null;
		if (tema != null && !tema.trim().isEmpty()) {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), "%"+tema+"%");
		}else {
		rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		}
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;

	}
	
	@Override
	public List<ForumVO> consultarForumPorConteudoDisciplina(DisciplinaVO obj, String tema, OpcaoOrdenacaoForumEnum opcaoOrdenacaoForum, Integer usuario, Integer limite, Integer pagina, int nivelMontarDados) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectCompleto(usuario));
		sb.append(" where Forum.situacaoForum is not null ");
		if (tema != null && !tema.trim().isEmpty()) {
			sb.append(" and remover_tags_html(sem_acentos(sem_acentos_html(tema))) ilike remover_tags_html(sem_acentos(sem_acentos_html(?))) ");			
		}
		sb.append(" and disciplina.codigo = ").append(obj.getCodigo());
		if (opcaoOrdenacaoForum.equals(OpcaoOrdenacaoForumEnum.TEMA)) {
			sb.append(" order by tema ");
		}
		if (opcaoOrdenacaoForum.equals(OpcaoOrdenacaoForumEnum.ULTIMA_ATUALIZACAO)) {
			sb.append(" order by  case when (select max(dataInteracao) from ForumInteracao where ForumInteracao.codigo = Forum.codigo ) is not null then (select max(dataInteracao) from ForumInteracao where ForumInteracao.codigo = Forum.codigo ) else Forum.dataCriacao end ");
		}
		if (limite != null && limite > 0) {
			sb.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		if (tema != null && !tema.trim().isEmpty()) {
			return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), "%"+tema+"%"), nivelMontarDados);
		}else {
			return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados);
		}

	}

	@Override
	public Integer consultarTotalRegistroForumPorDisciplina(ForumVO obj, String tema, OpcaoOrdenacaoForumEnum opcaoOrdenacaoForum, Integer usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT count(distinct forum.codigo) as qtde ");
		sb.append(" FROM forum ");
		sb.append(" inner join disciplina on forum.disciplina = disciplina.codigo ");
		if (tema != null && !tema.trim().isEmpty()) {
			sb.append(" and remover_tags_html(sem_acentos(sem_acentos_html(tema))) ilike remover_tags_html(sem_acentos(sem_acentos_html(?))) ");			
		}
		sb.append(" where disciplina.codigo = ").append(obj.getDisciplina().getCodigo());		
		if (obj.getSituacaoForum() != null && !obj.getSituacaoForum().isSituacaoTodos()) {
			sb.append(" and Forum.situacaoForum = '").append(obj.getSituacaoForum().name()).append("' ");
		}
		SqlRowSet rs = null;
		if (tema != null && !tema.trim().isEmpty()) {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), "%"+tema+"%");
		}else {
		 rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		}
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	@Override
	public List<ForumVO> consultarForumPorDisciplina(ForumVO obj, String tema, OpcaoOrdenacaoForumEnum opcaoOrdenacaoForum, Integer usuario, Integer limite, Integer pagina, int nivelMontarDados) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectCompleto(usuario));
		sb.append(" where 1=1 ");
		if (obj.getSituacaoForum() != null && !obj.getSituacaoForum().isSituacaoTodos()) {
			sb.append(" and Forum.situacaoForum = '").append(obj.getSituacaoForum().name()).append("' ");
		}
		if (tema != null && !tema.trim().isEmpty()) {
			sb.append(" and remover_tags_html(sem_acentos(sem_acentos_html(tema))) ilike remover_tags_html(sem_acentos(sem_acentos_html(?))) ");
		}
		sb.append(" and disciplina.codigo = ").append(obj.getDisciplina().getCodigo());
		if (opcaoOrdenacaoForum.equals(OpcaoOrdenacaoForumEnum.TEMA)) {
			sb.append(" order by tema ");
		}
		if (opcaoOrdenacaoForum.equals(OpcaoOrdenacaoForumEnum.ULTIMA_ATUALIZACAO)) {
			sb.append(" order by  case when (select max(dataInteracao) from ForumInteracao where ForumInteracao.codigo = Forum.codigo ) is not null then (select max(dataInteracao) from ForumInteracao where ForumInteracao.codigo = Forum.codigo ) else Forum.dataCriacao end ");
		}
		if (opcaoOrdenacaoForum.equals(OpcaoOrdenacaoForumEnum.NOVO)) {
			sb.append(" order by forum.datacriacao desc ");
		}
		if (opcaoOrdenacaoForum.equals(OpcaoOrdenacaoForumEnum.ANTIGO)) {
			sb.append(" order by forum.datacriacao ");
		}
		if (limite != null && limite > 0) {
			sb.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		if (tema != null && !tema.trim().isEmpty()) {
			return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), "%"+tema+"%"), nivelMontarDados);
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados);

	}

	@Override
	public List<ForumVO> consultarForumPorMatricula(String matricula, Integer disciplina, String tema, OpcaoOrdenacaoForumEnum opcaoOrdenacaoForum, String anoSemestre, Integer usuario, Integer limite, Integer pagina, int nivelMontarDados) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectCompleto(usuario));
//		sb.append(" left join turmaagrupada on turmaagrupada.turmaorigem = turma.codigo ");
		sb.append(" left join ForumAcesso on ForumAcesso.forum = Forum.codigo and ForumAcesso.usuarioAcesso = ").append(usuario);
		sb.append(" and ForumAcesso.codigo = (select max(fa.codigo) from ForumAcesso fa where fa.forum = Forum.codigo and fa.usuarioAcesso = ").append(usuario).append(")");
		sb.append(" where Forum.situacaoForum =  '").append(SituacaoForumEnum.ATIVO).append("' ");
		sb.append(" and Forum.publicoAlvoForumEnum =  '").append(PublicoAlvoForumEnum.ALUNO).append("' ");
		if(Uteis.isAtributoPreenchido(disciplina)) {
			sb.append("AND (forum.disciplina = ").append(disciplina);
			sb.append(" or forum.disciplina in (select equivalente from disciplinaequivalente where disciplina = ").append(disciplina);
			sb.append(" union select disciplina from disciplinaequivalente where equivalente = ").append(disciplina);
			sb.append(")) ");
		}
		sb.append(getSqlPeriodicidadeForum(matricula, anoSemestre));
		if (tema != null && !tema.trim().isEmpty()) {
			sb.append(" and remover_tags_html(sem_acentos(sem_acentos_html(tema))) ilike remover_tags_html(sem_acentos(sem_acentos_html(?))) ");
		}
		/*Validando se o aluno esta participando da turma informada no forum*/
//		sb.append(" and case when ForumAcesso.codigo is null then ");
//		sb.append(" case when Forum.turma is null then ");
//		sb.append(" (select MatriculaPeriodoTurmaDisciplina.disciplina from MatriculaPeriodoTurmaDisciplina ");
//		sb.append(" inner join MatriculaPeriodo on MatriculaPeriodo.codigo = MatriculaPeriodoTurmaDisciplina.MatriculaPeriodo ");
//		sb.append(" where MatriculaPeriodo.matricula = '").append(matricula).append("' and MatriculaPeriodoTurmaDisciplina.disciplina = Forum.disciplina ");
//		sb.append("	and (MatriculaPeriodo.situacaomatriculaperiodo = 'AT' or MatriculaPeriodo.situacaomatriculaperiodo = 'PR') ");
//		if(Uteis.isAtributoPreenchido(anoSemestre)) {
//			if(anoSemestre.contains("/")) {
//				sb.append(" and (MatriculaPeriodoTurmaDisciplina.ano||'/'||MatriculaPeriodoTurmaDisciplina.semestre) = '").append(anoSemestre).append("'  ");
//			}else {
//				sb.append(" and MatriculaPeriodoTurmaDisciplina.ano = '").append(anoSemestre).append("'  ");
//			}
//		}
//		sb.append(" limit 1) ");
//		sb.append(" else  ");
//		sb.append(" (select MatriculaPeriodoTurmaDisciplina.disciplina from MatriculaPeriodoTurmaDisciplina ");
//		sb.append(" inner join MatriculaPeriodo on MatriculaPeriodo.codigo = MatriculaPeriodoTurmaDisciplina.MatriculaPeriodo ");
//		sb.append(" where MatriculaPeriodo.matricula = '").append(matricula).append("' and MatriculaPeriodoTurmaDisciplina.disciplina = Forum.disciplina ");
//		sb.append("	and (MatriculaPeriodo.situacaomatriculaperiodo = 'AT' or MatriculaPeriodo.situacaomatriculaperiodo = 'PR') ");
//		if(Uteis.isAtributoPreenchido(anoSemestre)) {
//			if(anoSemestre.contains("/")) {
//				sb.append(" and (MatriculaPeriodoTurmaDisciplina.ano||'/'||MatriculaPeriodoTurmaDisciplina.semestre) = '").append(anoSemestre).append("'  ");
//			}else {
//				sb.append(" and MatriculaPeriodoTurmaDisciplina.ano = '").append(anoSemestre).append("'  ");
//			}
//		}
//
//		sb.append(" and case when turma.turmaagrupada = false and MatriculaPeriodoTurmaDisciplina.turmapratica is null and MatriculaPeriodoTurmaDisciplina.turmateorica is null ");
//		sb.append(" then (MatriculaPeriodoTurmaDisciplina.turma = Forum.turma or  MatriculaPeriodo.turma = Forum.turma) ");
//		sb.append(" else ");
//		sb.append(" case when turma.turmaagrupada and MatriculaPeriodoTurmaDisciplina.turmapratica is null and MatriculaPeriodoTurmaDisciplina.turmateorica is null ");
//		sb.append(" then ");
//		sb.append(" (turmaagrupada.turmaorigem = Forum.turma) ");
//		sb.append(" else ");
//		sb.append(" case when MatriculaPeriodoTurmaDisciplina.turmapratica is not null ");
//		sb.append(" then (MatriculaPeriodoTurmaDisciplina.turmapratica = forum.turma) ");
//		sb.append(" else ");
//		sb.append(" case when MatriculaPeriodoTurmaDisciplina.turmateorica is not null ");
//		sb.append(" then (MatriculaPeriodoTurmaDisciplina.turmateorica = forum.turma) ");
//		sb.append(" end end end end limit 1) ");
//		sb.append(" end else ");
//		sb.append("( select MatriculaPeriodoTurmaDisciplina.disciplina ");				
//		sb.append("	from ");
//		sb.append(" MatriculaPeriodoTurmaDisciplina ");
//		sb.append(" inner join MatriculaPeriodo on ");
//		sb.append(" MatriculaPeriodo.codigo = MatriculaPeriodoTurmaDisciplina.MatriculaPeriodo ");
//		sb.append(" where MatriculaPeriodo.matricula = '").append(matricula).append("' and MatriculaPeriodoTurmaDisciplina.disciplina = Forum.disciplina ");
//		sb.append(" and (MatriculaPeriodo.situacaomatriculaperiodo = 'AT' or MatriculaPeriodo.situacaomatriculaperiodo = 'PR') ");
//		if(Uteis.isAtributoPreenchido(anoSemestre)) {
//			if(anoSemestre.contains("/")) {
//				sb.append(" and (MatriculaPeriodoTurmaDisciplina.ano||'/'||MatriculaPeriodoTurmaDisciplina.semestre) = '").append(anoSemestre).append("'  ");
//			}else {
//				sb.append(" and MatriculaPeriodoTurmaDisciplina.ano = '").append(anoSemestre).append("'  ");
//			}
//		}
//		sb.append(" limit 1) ");	
//		sb.append(" end > 0 ");

		sb.append(" group by forum.codigo, forum.responsavelcriacao, forum.datacriacao, forum.responsavelinativacao, forum.datainativacao, forum.disciplina, forum.turma, forum.conteudo, ");
		sb.append(" forum.situacaoforum, forum.tema, disciplina.nome, turma.identificadorturma, responsavelcriacao.nome, pessoacriacao.codigo, pessoaCriacao.nome, arquivo.pastabasearquivo, ");
		sb.append(" arquivo.codigo, arquivo.nome, responsavelinativacao.nome, curso.nome, areaConhecimento.nome, qtdeatualizacao ");

		if (opcaoOrdenacaoForum.equals(OpcaoOrdenacaoForumEnum.TEMA)) {
			sb.append(" order by remover_tags_html(sem_acentos(sem_acentos_html(tema))) ");
		}
		if (opcaoOrdenacaoForum.equals(OpcaoOrdenacaoForumEnum.ULTIMA_ATUALIZACAO)) {
			sb.append(" order by  case when (select max(dataInteracao) from ForumInteracao where ForumInteracao.codigo = Forum.codigo ) is not null then (select max(dataInteracao) from ForumInteracao where ForumInteracao.codigo = Forum.codigo ) else Forum.dataCriacao end desc");
		}
		if (limite != null && limite > 0) {
			sb.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		if (tema != null && !tema.trim().isEmpty()) {
			return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), "%"+tema+"%"), nivelMontarDados);
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados);

	}

	@Override
	public List<ForumVO> consultarForumPorDisciplinaTurma(Integer turma, Integer disciplina, SituacaoForumEnum situacaoForum, Integer limite, Integer pagina, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectCompleto(usuario.getCodigo()));
		sb.append(" where 1= 1 ");
		if (turma != null && turma > 0) {
			sb.append(" and turma.codigo = ").append(turma);
		}
		if (disciplina != null && disciplina > 0) {
			sb.append(" and disciplina.codigo = ").append(disciplina);
		}

		if (situacaoForum != null && !situacaoForum.isSituacaoTodos()) {
			sb.append(" and situacaoForum = '").append(situacaoForum.name()).append("' ");
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados);
	}
	@Override
	public List<ForumVO> consultarForumVisaoProfessor(ForumVO obj, OpcaoOrdenacaoForumEnum opcaoOrdenacaoForum, Integer limite, Integer pagina, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {

		StringBuilder sb = new StringBuilder(getSelectCompleto(usuario.getCodigo()));
		sb.append(" where 1=1 ");
		montarFiltrosConsultaForumVisaoProfessor(obj, periodicidadeEnum, ano, semestre, sb, usuario);

		if (opcaoOrdenacaoForum.equals(OpcaoOrdenacaoForumEnum.TEMA)) {
			sb.append(" order by tema ");
		}

		if (opcaoOrdenacaoForum.equals(OpcaoOrdenacaoForumEnum.ULTIMA_ATUALIZACAO)) {
			sb.append(" order by  case when (select max(dataInteracao) from ForumInteracao where ForumInteracao.codigo = Forum.codigo ) is not null then (select max(dataInteracao) from ForumInteracao where ForumInteracao.codigo = Forum.codigo ) else  Forum.dataCriacao end ");
		}

		if (limite != null && limite > 0) {
			sb.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados);
	}

	@Override
	public Integer consultarTotalRegistroForumVisaoProfessor(ForumVO obj, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT count(distinct forum.codigo) as qtde ");
		sb.append(" FROM forum ");
		sb.append(" inner join disciplina on forum.disciplina = disciplina.codigo ");
		sb.append(" left join turma on forum.turma = turma.codigo ");
		sb.append(" where 1 = 1 ");
		montarFiltrosConsultaForumVisaoProfessor(obj, periodicidadeEnum, ano, semestre, sb, usuario);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	public void montarFiltrosConsultaForumVisaoProfessor(ForumVO obj, PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, StringBuilder sb, UsuarioVO  usuarioLogado ) throws ConsistirException {

		if (!Uteis.isAtributoPreenchido(obj.getCursoVO()) && !Uteis.isAtributoPreenchido(obj.getTurma()) && !Uteis.isAtributoPreenchido(obj.getDisciplina())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_Forum_cursoTurmaDisciplina"));
		}
		if((Uteis.isAtributoPreenchido(periodicidadeEnum) && (periodicidadeEnum.equals(PeriodicidadeEnum.ANUAL) || periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL))) && !Uteis.isAtributoPreenchido(ano)) {
			throw new ConsistirException("Informe o ano!");
		}
		
		if (obj.getSituacaoForum() != null && !obj.getSituacaoForum().isSituacaoTodos()) {
			sb.append(" and Forum.situacaoForum = '").append(obj.getSituacaoForum().name()).append("' ");
		}

		if (Uteis.isAtributoPreenchido(obj.getAreaConhecimentoVO())) {
			sb.append(" and forum.areaconhecimento = ").append(obj.getAreaConhecimentoVO().getCodigo());
		}

		if (Uteis.isAtributoPreenchido(obj.getCursoVO())) {
			sb.append(" and forum.curso = ").append(obj.getCursoVO().getCodigo());			
		}

		if (Uteis.isAtributoPreenchido(obj.getTurma())) {
			sb.append(" and turma.codigo = ").append(obj.getTurma().getCodigo());									    
		}
		sb.append(" AND ");
		getSQLPadraoExistsDisciplinasForum(0, usuarioLogado.getPessoa().getCodigo(), sb);
		executarValidacaoPermissaoApresentarSomenteForumCriadoProprioProfessor(usuarioLogado.getCodigo(), usuarioLogado.getPerfilAcesso().getCodigo(), sb);

		if (Uteis.isAtributoPreenchido(obj.getDisciplina())) {
			sb.append(" and ((forum.disciplina = ").append(obj.getDisciplina().getCodigo()).append(") or (forum.disciplina = ").append(obj.getDisciplina().getCodigo()).append(" and forum.turma is null))");
		}

		if (Uteis.isAtributoPreenchido(obj.getFiltroForumAvaliado()) && Uteis.isAtributoPreenchido(obj.getFiltroForumAvaliado().getFiltroEnum())) {
			sb.append(" and forum.forumavaliado = ").append(obj.getFiltroForumAvaliado().getFiltroEnum()).append(" ");
		}
		if (!Uteis.isAtributoPreenchido(periodicidadeEnum) || periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL)) {
			sb.append(" and (forum.ano is null or forum.ano = '') and (forum.semestre is null or forum.semestre = '') ");
		}
		if (Uteis.isAtributoPreenchido(periodicidadeEnum) && periodicidadeEnum.equals(PeriodicidadeEnum.ANUAL)) {
			sb.append(" and (forum.ano is null or forum.ano = '' or forum.ano = '").append(ano).append("')");
		}
		if (Uteis.isAtributoPreenchido(periodicidadeEnum) && periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL)) {
			sb.append(" and (forum.ano is null or forum.ano = '' or forum.ano = '").append(ano).append("')");
			sb.append(" and (forum.semestre is null or forum.semestre = '' or forum.semestre = '").append(semestre).append("')");
		}
		
		if (Uteis.isAtributoPreenchido(obj.getPublicoAlvoForumEnum())) { 
			sb.append(" and forum.publicoalvoforumenum = '").append(obj.getPublicoAlvoForumEnum().getName()).append("' ");
		}

		if (Uteis.isAtributoPreenchido(obj.getRestricaoPublicoAlvoForumEnum())) {
			sb.append(" and forum.restricaopublicoalvoforumenum = '").append(obj.getRestricaoPublicoAlvoForumEnum().getName()).append("' ");
		}else{
			//Caso o filtro restricao publico nao esteja informado entao so posso traze forum especifico caso o usuario que esteja consultando faça parte das pessoas especificas.
			sb.append(" and ( forum.restricaopublicoalvoforumenum <> '").append(RestricaoPublicoAlvoForumEnum.PROFESSORES_ESPECIFICOS).append("' or forum.restricaopublicoalvoforumenum is null ) ");
			sb.append(" or forum.codigo in ( ");
			sb.append(" select forum.codigo from forum  ");
			sb.append(" inner join forumpessoa on forumpessoa.forum = forum.codigo ");
			sb.append(" where restricaopublicoalvoforumenum = '").append(RestricaoPublicoAlvoForumEnum.PROFESSORES_ESPECIFICOS).append("' ");
			sb.append(" and forumpessoa.pessoa = ").append(usuarioLogado.getPessoa().getCodigo());  
			sb.append("  ) ");
			
		}
		
	}

	private List<ForumVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados) throws Exception {
		List<ForumVO> forumVOs = new ArrayList<ForumVO>(0);
		while (rs.next()) {
			forumVOs.add(montarDados(rs, nivelMontarDados));
		}
		return forumVOs;
	}

	private ForumVO montarDados(SqlRowSet rs, int nivelMontarDados) throws Exception {
		ForumVO obj = new ForumVO();
		obj.setCodigo(rs.getInt("forum.codigo"));
		obj.getConteudo().setCodigo(rs.getInt("forum.conteudo"));
		obj.getCursoVO().setCodigo(rs.getInt("forum.curso"));
		obj.getCursoVO().setNome(rs.getString("curso.nome"));
		obj.getAreaConhecimentoVO().setCodigo(rs.getInt("forum.areaconhecimento"));
		obj.getAreaConhecimentoVO().setNome(rs.getString("areaconhecimento.nome"));
		obj.getTurma().setCodigo(rs.getInt("forum.turma"));
		obj.getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
		obj.getDisciplina().setCodigo(rs.getInt("forum.disciplina"));
		obj.getDisciplina().setNome(rs.getString("disciplina.nome"));
		obj.getResponsavelCriacao().setCodigo(rs.getInt("forum.responsavelCriacao"));
		obj.getResponsavelCriacao().setNome(rs.getString("responsavelCriacao.nome"));
		obj.getResponsavelCriacao().getPessoa().setCodigo(rs.getInt("pessoaCriacao.codigo"));
		obj.getResponsavelCriacao().getPessoa().setNome(rs.getString("pessoaCriacao.nome"));
		obj.getResponsavelCriacao().getPessoa().getArquivoImagem().setCodigo(rs.getInt("arquivo.codigo"));
		if (rs.getString("arquivo.pastaBaseArquivo") != null) {
			obj.getResponsavelCriacao().getPessoa().getArquivoImagem().setNome(rs.getString("arquivo.nome"));
			obj.getResponsavelCriacao().getPessoa().getArquivoImagem().setPastaBaseArquivo(rs.getString("arquivo.pastaBaseArquivo"));
		}

		obj.setDataCriacao(rs.getTimestamp("forum.dataCriacao"));
		obj.setDataInativacao(rs.getTimestamp("forum.dataInativacao"));
		if (rs.getInt("forum.responsavelInativacao") > 0) {
			obj.setResponsavelInativacao(new UsuarioVO());
			obj.getResponsavelInativacao().setCodigo(rs.getInt("forum.responsavelInativacao"));
			obj.getResponsavelInativacao().setNome(rs.getString("responsavelInativacao.nome"));
		}
		obj.setSituacaoForum(SituacaoForumEnum.valueOf(rs.getString("forum.situacaoForum")));
		obj.setTema(rs.getString("forum.tema"));
		obj.setForumAvaliado(rs.getBoolean("forum.forumavaliado"));
		obj.setPublicoAlvoForumEnum(PublicoAlvoForumEnum.getEnum(rs.getString("forum.publicoAlvoForumEnum")));
		obj.setRestricaoPublicoAlvoForumEnum(RestricaoPublicoAlvoForumEnum.getEnum(rs.getString("forum.restricaoPublicoAlvoForumEnum")));
		obj.setQtdeAtualizacao(rs.getInt("qtdeAtualizacao"));
		obj.setAno(rs.getString("forum.ano"));
		obj.setSemestre(rs.getString("forum.semestre"));
		if (Uteis.isAtributoPreenchido(obj.getAno()) && Uteis.isAtributoPreenchido(obj.getSemestre())) {
			obj.setTipoPeriodoDisponibilizacao(TipoPeriodoDisponibilidadeForumEnum.ANO_SEMESTRE.name());
		} else if (Uteis.isAtributoPreenchido(obj.getAno()) && !Uteis.isAtributoPreenchido(obj.getSemestre())) {
			obj.setTipoPeriodoDisponibilizacao(TipoPeriodoDisponibilidadeForumEnum.ANO.name());
		} else {
			obj.setTipoPeriodoDisponibilizacao(TipoPeriodoDisponibilidadeForumEnum.SEMPRE_DISPONIVEL.name());
		}
		obj.setNovoObj(false);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			obj.setForumPessoaVOs(getFacadeFactory().getForumPessoaFacade().consultarForumPessoa(obj.getCodigo(), false, nivelMontarDados, null));	
		}
		return obj;
	}

	@Override
	public ForumVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados) throws Exception {
		StringBuilder sb = new StringBuilder(getSelectCompleto(0));
		sb.append(" WHERE forum.codigo = ").append(codigo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados);
		}
		return null;
	}

	@Override
	public Integer consultarQtdeAtualizacaoForumPorUsuarioProfessor(Integer disciplina, Integer unidadeEnsino, UsuarioVO usuario) {
		try {
			StringBuilder sb = new StringBuilder("SELECT  sum(t.qtde) as qtde FROM (");
			
			sb.append(" SELECT count(distinct forum.codigo) as qtde from Forum ");
			sb.append(" LEFT JOIN turma ON turma.codigo = forum.turma ");
			sb.append(" left join ForumAcesso on ForumAcesso.forum = Forum.codigo and ForumAcesso.usuarioAcesso = ").append(usuario.getCodigo());
			sb.append(" and ForumAcesso.codigo = (select max(fa.codigo) from ForumAcesso fa where fa.forum = Forum.codigo and fa.usuarioAcesso = ").append(usuario.getCodigo()).append(")");
			sb.append(" where Forum.situacaoForum is not null and situacaoForum <> '").append(SituacaoForumEnum.INATIVO).append("' ");
			sb.append(" and (((forum.ano is null or forum.ano = '') and (forum.semestre is null or forum.semestre = '')) or "); 
			sb.append(" (forum.ano is not null and forum.semestre is null ) or ");
			sb.append(" (forum.ano is not null and forum.semestre is not null)) ");
			sb.append(" and (ForumAcesso.codigo is null or ForumAcesso.dataAcesso < forum.dataCriacao)");
			sb.append(" and forum.responsavelcriacao <> ").append(usuario.getCodigo());
			if (disciplina != null && disciplina > 0) {
				sb.append(" and Forum.disciplina = ").append(disciplina);
			} else {
				sb.append(" and (");
				getSQLPadraoExistsDisciplinasForum(unidadeEnsino, usuario.getPessoa().getCodigo(), sb);
				sb.append(" and forum.turma  is null) ");
			}
			executarValidacaoPermissaoApresentarSomenteForumCriadoProprioProfessor(usuario.getCodigo(), usuario.getPerfilAcesso().getCodigo(), sb);
			sb.append("UNION ALL");
			sb.append(" SELECT count(DISTINCT foruminteracao.codigo) AS qtde from Foruminteracao ");
			sb.append(" left join Forum ON foruminteracao.forum = forum.codigo ");
			sb.append(" LEFT JOIN turma ON turma.codigo = forum.turma ");
			sb.append(" left join ForumAcesso on ForumAcesso.forum = Forum.codigo and ForumAcesso.usuarioAcesso = ").append(usuario.getCodigo());
			sb.append(" and ForumAcesso.codigo = (select max(fa.codigo) from ForumAcesso fa where fa.forum = Forum.codigo and fa.usuarioAcesso = ").append(usuario.getCodigo()).append(")");
			sb.append(" where Forum.situacaoForum is not null and situacaoForum <> '").append(SituacaoForumEnum.INATIVO).append("' ");
			sb.append(" and (((forum.ano is null or forum.ano = '') and (forum.semestre is null or forum.semestre = '')) or "); 
			sb.append(" (forum.ano is not null and forum.semestre is null ) or ");
			sb.append(" (forum.ano is not null and forum.semestre is not null)) AND ");
			getSQLPadraoExistsDisciplinasForum(unidadeEnsino, usuario.getPessoa().getCodigo(), sb);
			executarValidacaoPermissaoApresentarSomenteForumCriadoProprioProfessor(usuario.getCodigo(), usuario.getPerfilAcesso().getCodigo(), sb);
			sb.append(" AND foruminteracao.usuariointeracao <> ").append(usuario.getCodigo());
			sb.append(" and (ForumAcesso.codigo is null or ForumAcesso.dataAcesso < forum.dataCriacao or ForumAcesso.dataAcesso < foruminteracao.datainteracao)");
			sb.append(") AS T");
			
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			if (rs.next()) {
				return rs.getInt("qtde");
			}
			return 0;
		} catch (Exception e) {
			return 0;
		}
	}

	@Override
	public Integer consultarQtdeAtualizacaoForumPorUsuarioAluno(String matricula, Integer disciplina, String anoSemestre, Integer usuario) {
		try {
			StringBuilder sb = new StringBuilder("SELECT  sum(t.qtde) as qtde FROM ( ");
			sb.append("SELECT count(DISTINCT foruminteracao.codigo) AS qtde FROM foruminteracao ");
			sb.append("LEFT JOIN Forum ON foruminteracao.forum = forum.codigo ");
			sb.append(" LEFT JOIN turma ON turma.codigo = forum.turma ");
			sb.append(" left join ForumAcesso on ForumAcesso.forum = Forum.codigo and ForumAcesso.usuarioAcesso = ").append(usuario);
			sb.append(" and ForumAcesso.codigo = (select max(fa.codigo) from ForumAcesso fa where fa.forum = Forum.codigo and fa.usuarioAcesso = ").append(usuario).append(")");
			sb.append(" where Forum.situacaoForum = '").append(SituacaoForumEnum.ATIVO.name()).append("' ");
			if(Uteis.isAtributoPreenchido(anoSemestre)) {				
				if(anoSemestre.contains("/")){
					sb.append(" and (Forum.ano is null or forum.ano =  '' or forum.ano = '").append(anoSemestre.substring(0, anoSemestre.lastIndexOf("/")).trim()).append("') ");
					sb.append(" and (Forum.semestre is null or forum.semestre =  '' or forum.semestre = '").append(anoSemestre.substring(anoSemestre.lastIndexOf("/")+1, anoSemestre.length()).trim()).append("') ");
				}else {
					sb.append(" and (Forum.ano is null or forum.ano =  '' or forum.ano = '").append(anoSemestre).append("') ");
				}
			}
			if(Uteis.isAtributoPreenchido(disciplina)) {
				sb.append(" AND (forum.disciplina = ").append(disciplina);
				sb.append(" or forum.disciplina in (select equivalente from disciplinaequivalente where disciplina = ").append(disciplina);
				sb.append(" union select disciplina from disciplinaequivalente where equivalente = ").append(disciplina);
				sb.append(")) ");
			}
			sb.append(getSqlPeriodicidadeForum(matricula, anoSemestre));
			sb.append(" AND foruminteracao.usuariointeracao <> ").append(usuario);
			sb.append(" and Forum.publicoAlvoForumEnum = '").append(PublicoAlvoForumEnum.ALUNO.name()).append("' ");
			sb.append(" and (ForumAcesso.codigo is null or ForumAcesso.dataAcesso < forum.dataCriacao or ForumAcesso.dataAcesso < foruminteracao.datainteracao)");
//			sb.append(" and case when ForumAcesso.codigo is null then ");
//			sb.append(" case when turma is null then ");
//			sb.append(" (select MatriculaPeriodoTurmaDisciplina.disciplina from MatriculaPeriodoTurmaDisciplina ");
//			sb.append(" inner join MatriculaPeriodo on MatriculaPeriodo.codigo = MatriculaPeriodoTurmaDisciplina.MatriculaPeriodo ");
//			sb.append(" where MatriculaPeriodo.matricula = '").append(matricula).append("' and MatriculaPeriodoTurmaDisciplina.disciplina = Forum.disciplina limit 1) ");
//			sb.append(" else  ");
//			sb.append(" (select MatriculaPeriodoTurmaDisciplina.disciplina from MatriculaPeriodoTurmaDisciplina ");
//			sb.append(" inner join MatriculaPeriodo on MatriculaPeriodo.codigo = MatriculaPeriodoTurmaDisciplina.MatriculaPeriodo ");
//			sb.append(" where MatriculaPeriodo.matricula = '").append(matricula).append("' and MatriculaPeriodoTurmaDisciplina.disciplina = Forum.disciplina ");
//			sb.append(" and (MatriculaPeriodoTurmaDisciplina.turma = Forum.turma or  MatriculaPeriodo.turma = Forum.turma ) limit 1) ");
//			sb.append(" end else 1 end > 0 ");
			
			sb.append("UNION ALL ");
			
			sb.append("SELECT count(distinct forum.codigo) as qtde from Forum ");
			sb.append(" LEFT JOIN turma ON turma.codigo = forum.turma ");
			sb.append(" left join ForumAcesso on ForumAcesso.forum = Forum.codigo and ForumAcesso.usuarioAcesso = ").append(usuario);
			sb.append(" and ForumAcesso.codigo = (select max(fa.codigo) from ForumAcesso fa where fa.forum = Forum.codigo and fa.usuarioAcesso = ").append(usuario).append(")");
			sb.append(" where Forum.situacaoForum = '").append(SituacaoForumEnum.ATIVO.name()).append("' ");
			if(Uteis.isAtributoPreenchido(anoSemestre)) {				
				if(anoSemestre.contains("/")){
					sb.append(" and (Forum.ano is null or forum.ano =  '' or forum.ano = '").append(anoSemestre.substring(0, anoSemestre.lastIndexOf("/")).trim()).append("') ");
					sb.append(" and (Forum.semestre is null or forum.semestre =  '' or forum.semestre = '").append(anoSemestre.substring(anoSemestre.lastIndexOf("/")+1, anoSemestre.length()).trim()).append("') ");
				}else {
					sb.append(" and (Forum.ano is null or forum.ano =  '' or forum.ano = '").append(anoSemestre).append("') ");
				}
			}
			if(Uteis.isAtributoPreenchido(disciplina)) {
				sb.append(" AND (forum.disciplina = ").append(disciplina);
				sb.append(" or forum.disciplina in (select equivalente from disciplinaequivalente where disciplina = ").append(disciplina);
				sb.append(")) ");
			}
			sb.append(getSqlPeriodicidadeForum(matricula, anoSemestre));
			sb.append(" and Forum.publicoAlvoForumEnum = '").append(PublicoAlvoForumEnum.ALUNO.name()).append("' ");
			sb.append(" and (ForumAcesso.codigo is null or ForumAcesso.dataAcesso < forum.dataCriacao or ForumAcesso.dataAcesso < (select max(dataInteracao) from ForumInteracao where ForumInteracao.codigo = Forum.codigo and excluido = false ))");
//			sb.append(" and case when ForumAcesso.codigo is null then ");
//			sb.append(" case when turma is null then ");
//			sb.append(" (select MatriculaPeriodoTurmaDisciplina.disciplina from MatriculaPeriodoTurmaDisciplina ");
//			sb.append(" inner join MatriculaPeriodo on MatriculaPeriodo.codigo = MatriculaPeriodoTurmaDisciplina.MatriculaPeriodo ");
//			sb.append(" where MatriculaPeriodo.matricula = '").append(matricula).append("' and MatriculaPeriodoTurmaDisciplina.disciplina = Forum.disciplina limit 1) ");
//			sb.append(" else  ");
//			sb.append(" (select MatriculaPeriodoTurmaDisciplina.disciplina from MatriculaPeriodoTurmaDisciplina ");
//			sb.append(" inner join MatriculaPeriodo on MatriculaPeriodo.codigo = MatriculaPeriodoTurmaDisciplina.MatriculaPeriodo ");
//			sb.append(" where MatriculaPeriodo.matricula = '").append(matricula).append("' and MatriculaPeriodoTurmaDisciplina.disciplina = Forum.disciplina ");
//			sb.append(" and (MatriculaPeriodoTurmaDisciplina.turma = Forum.turma or  MatriculaPeriodo.turma = Forum.turma ) limit 1) ");
//			sb.append(" end else 1 end > 0 ");
			sb.append(") AS T");

			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			if (rs.next()) {
				return rs.getInt("qtde");
			}
			return 0;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public void validarAdicaoForumPessoa(ForumPessoaVO forumPessoa) throws Exception {
		if (!Uteis.isAtributoPreenchido(forumPessoa.getPessoaVO())) {
			throw new Exception(UteisJSF.internacionalizar("msg_ForumPessoa_pessoa"));
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void adiconarForumPessoa(ForumVO forum, ForumPessoaVO forumPessoa) throws Exception {
		int index = 0;
		validarAdicaoForumPessoa(forumPessoa);
		forumPessoa.setForumVO(forum);
		for (ForumPessoaVO objExistente : forum.getForumPessoaVOs()) {
			if (objExistente.equalsForumPessoaVO(forumPessoa)) {
				forum.getForumPessoaVOs().set(index, forumPessoa);
				return;
			}
			index++;
		}
		forum.getForumPessoaVOs().add(forumPessoa);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removerForumPessoa(ForumVO forum, ForumPessoaVO forumPessoa) throws Exception {
		Iterator<ForumPessoaVO> i = forum.getForumPessoaVOs().iterator();
		while (i.hasNext()) {
			ForumPessoaVO objExistente = (ForumPessoaVO) i.next();
			if (objExistente.equalsForumPessoaVO(forumPessoa)) {
				i.remove();
				return;
			}
		}
	}
	/*
	 * 
	 */
	public String getSqlPeriodicidadeForum(String matricula, String anoSemestre) {
		StringBuilder sb = new StringBuilder();
		sb.append(" and exists (");
		sb.append("");
		sb.append(" 	select matriculaperiodoturmadisciplina.codigo from matriculaperiodoturmadisciplina");
		sb.append(" 	inner join matriculaperiodo on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo");
		sb.append(" 	inner join historico on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina");
		sb.append(" 	inner join matricula on matricula.matricula = matriculaperiodo.matricula");
		sb.append(" 	inner join curso on matricula.curso = curso.codigo");
		sb.append(" 	where 1 = 1");
		sb.append(" 	and MatriculaPeriodo.matricula = '").append(matricula).append("'");
		sb.append(" 	and (MatriculaPeriodoTurmaDisciplina.disciplina = Forum.disciplina");
		sb.append(" 	or ((turma.codigo is null or turma.turmaagrupada) AND MatriculaPeriodoTurmaDisciplina.disciplina in (select equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = Forum.disciplina ");
		sb.append("     union select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = Forum.disciplina)))  ");
		sb.append(" 	and (MatriculaPeriodo.situacaomatriculaperiodo = 'AT'	or MatriculaPeriodo.situacaomatriculaperiodo = 'PR')");
		if(Uteis.isAtributoPreenchido(anoSemestre)) {
			if(anoSemestre.contains("/")) {
				sb.append(" and (MatriculaPeriodoTurmaDisciplina.ano||'/'||MatriculaPeriodoTurmaDisciplina.semestre) = '").append(anoSemestre).append("'  ");
			}else {
				sb.append(" and MatriculaPeriodoTurmaDisciplina.ano = '").append(anoSemestre).append("'  ");
			}
		}else {
			sb.append(" and matriculaPeriodo.codigo = (select mp.codigo from matriculaperiodo as mp where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo in ('AT', 'PR') order by mp.ano desc, mp.semestre desc, mp.codigo desc limit 1) ");
		}
		/**
		 * trata se o forum é por tempo indeterminado ou seja sem ano e sem semestre
		 */
		sb.append("  	and ((forum.ano is null or  forum.ano = '')");
		/**
		 * Neste trata se o forum só controla o ano
		 */
		sb.append(" 	or (forum.ano is not null and  forum.ano != '' and (forum.semestre is null or  forum.semestre = '')");
		sb.append(" 	and forum.ano = MatriculaPeriodoTurmaDisciplina.ano)");
		/**
		 * Neste controla se o forum controla ano/semestre
		 */
		sb.append(" 	or (forum.ano is not null and  forum.ano != '' and forum.semestre is not null and  forum.semestre != ''");
		sb.append(" 		and forum.ano = MatriculaPeriodoTurmaDisciplina.ano and forum.semestre = MatriculaPeriodoTurmaDisciplina.semestre)");
		sb.append(" 	)");
		/**
		 * Neste controla se o forum tem turma, se a tuma é do tipo subturma ou agrupada
		 */
		sb.append(" 	and (forum.turma is null or (turma.codigo is not null and turma.turmaagrupada = false  and turma.codigo = MatriculaPeriodoTurmaDisciplina.turma)");
		sb.append(" 	or (turma.codigo is not null and turma.subturma and turma.tiposubturma = 'PRATICA' 	and forum.turma = MatriculaPeriodoTurmaDisciplina.turmapratica) ");
		sb.append(" 	or (turma.codigo is not null and turma.subturma and turma.tiposubturma = 'TEORICA' 	and forum.turma = MatriculaPeriodoTurmaDisciplina.turmateorica)");
		sb.append(" 	or (turma.codigo is not null and turma.turmaagrupada and turma.subturma = false and exists (select turmaagrupada.turma  from turmaagrupada where turmaagrupada.turmaorigem = turma.codigo and MatriculaPeriodoTurmaDisciplina.turma = turmaagrupada.turma))");
		sb.append("     )");
		/**
		 * Neste trata se o forum está vinculado ao curso
		 */
		sb.append(" 	and (forum.curso is null or forum.curso = matricula.curso)");
		/**
		 * Neste trata de o forum tem a mesma area de conhecimento do curso da matricula
		 */
		sb.append(" 	and (forum.areaconhecimento is null or forum.areaconhecimento = curso.areaconhecimento)");
		sb.append("");
		sb.append(" 	)");
		
//		sb.append(" AND (((forum.ano IS NULL OR forum.ano = '') AND (forum.semestre IS NULL OR forum.semestre = '' )) ");
//		sb.append("OR EXISTS  (SELECT MatriculaPeriodoTurmaDisciplina.disciplina ");
//		sb.append("FROM MatriculaPeriodoTurmaDisciplina ");
//		sb.append("INNER JOIN MatriculaPeriodo ON MatriculaPeriodo.codigo = MatriculaPeriodoTurmaDisciplina.MatriculaPeriodo ");
//		sb.append("WHERE MatriculaPeriodo.matricula = '").append(matricula).append("' ");
//		sb.append("AND MatriculaPeriodoTurmaDisciplina.disciplina = Forum.disciplina ");
//		sb.append("AND MatriculaPeriodoTurmaDisciplina.ano = forum.ano ");
//		sb.append("AND MatriculaPeriodoTurmaDisciplina.semestre = forum.semestre ");
//		sb.append("AND MatriculaPeriodo.codigo = ( ");
//		sb.append("SELECT mp.codigo AS matriculaperiodo  ");
//		sb.append("FROM matriculaperiodo as mp  ");
//		sb.append("WHERE mp.matricula = MatriculaPeriodo.matricula ");
//		sb.append("AND (mp.situacaomatriculaperiodo = 'AT' OR mp.situacaomatriculaperiodo = 'PR') ");
//		sb.append("AND mp.ano = forum.ano ");
//		sb.append("AND MP.semestre = forum.semestre ");
//		sb.append("ORDER BY mp.ano || '/' || mp.semestre DESC, mp.codigo DESC LIMIT 1)");
//		if(Uteis.isAtributoPreenchido(anoSemestre)) {
//			if(anoSemestre.contains("/")) {
//				sb.append(" and (MatriculaPeriodoTurmaDisciplina.ano||'/'||MatriculaPeriodoTurmaDisciplina.semestre) = '").append(anoSemestre).append("'  ");
//			}else {
//				sb.append(" and MatriculaPeriodoTurmaDisciplina.ano = '").append(anoSemestre).append("'  ");
//			}
//		}
//		sb.append(") ");
//		sb.append("OR ((forum.semestre IS NULL OR forum.semestre = '' ) and  EXISTS  (SELECT MatriculaPeriodoTurmaDisciplina.disciplina ");
//		sb.append("FROM MatriculaPeriodoTurmaDisciplina ");
//		sb.append("INNER JOIN MatriculaPeriodo ON MatriculaPeriodo.codigo = MatriculaPeriodoTurmaDisciplina.MatriculaPeriodo ");
//		sb.append("WHERE MatriculaPeriodo.matricula = '").append(matricula).append("' ");
//		sb.append("AND MatriculaPeriodoTurmaDisciplina.disciplina = Forum.disciplina ");
//		sb.append("AND MatriculaPeriodoTurmaDisciplina.ano = forum.ano ");
//		sb.append("AND MatriculaPeriodo.codigo = ( ");
//		sb.append("SELECT mp.codigo AS matriculaperiodo ");
//		sb.append("FROM matriculaperiodo  as mp ");
//		sb.append("WHERE mp.matricula = MatriculaPeriodo.matricula ");
//		sb.append("AND (mp.situacaomatriculaperiodo = 'AT' OR mp.situacaomatriculaperiodo = 'PR') ");
//		sb.append("AND mp.ano = forum.ano ");
//		sb.append("ORDER BY mp.ano || '/' || mp.semestre DESC, mp.codigo DESC LIMIT 1)");
//		if(Uteis.isAtributoPreenchido(anoSemestre)) {
//			if(anoSemestre.contains("/")) {
//				sb.append(" and (MatriculaPeriodoTurmaDisciplina.ano||'/'||MatriculaPeriodoTurmaDisciplina.semestre) = '").append(anoSemestre).append("'  ");
//			}else {
//				sb.append(" and MatriculaPeriodoTurmaDisciplina.ano = '").append(anoSemestre).append("'  ");
//			}
//		}
//		sb.append(")) ");
//		sb.append(") ");
		return sb.toString();
	}
	
	@Override
	public List<ForumVO> consultarAtualizacaoForumPorProfessor(Integer professor, Integer unidadeEnsino, Integer limite, Integer pagina, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT distinct forum.codigo as  \"forum.codigo\", forum.responsavelcriacao as  \"forum.responsavelcriacao\", forum.datacriacao as  \"forum.datacriacao\", forum.responsavelinativacao as  \"forum.responsavelinativacao\",");
		sb.append(" forum.datainativacao as  \"forum.datainativacao\", forum.disciplina as  \"forum.disciplina\", forum.turma as  \"forum.turma\", forum.conteudo as  \"forum.conteudo\", forum.situacaoforum as  \"forum.situacaoforum\",  ");
		sb.append(" forum.tema as  \"forum.tema\", forum.forumavaliado as  \"forum.forumavaliado\", forum.publicoalvoforumenum as  \"forum.publicoalvoforumenum\", forum.curso as  \"forum.curso\", ");
		sb.append(" forum.restricaopublicoalvoforumenum as  \"forum.restricaopublicoalvoforumenum\", forum.areaconhecimento as  \"forum.areaconhecimento\", forum.ano as \"forum.ano\", forum.semestre as \"forum.semestre\", ");
		sb.append(" disciplina.nome as \"disciplina.nome\", turma.identificadorTurma as \"turma.identificadorTurma\", ");
		sb.append(" responsavelCriacao.nome AS \"responsavelCriacao.nome\", ");
		sb.append(" pessoaCriacao.codigo AS \"pessoaCriacao.codigo\", pessoaCriacao.nome AS \"pessoaCriacao.nome\", ");
		sb.append(" arquivo.pastaBaseArquivo as \"arquivo.pastaBaseArquivo\", arquivo.codigo AS \"arquivo.codigo\", arquivo.nome AS \"arquivo.nome\",  ");
		sb.append(" responsavelInativacao.nome as \"responsavelInativacao.nome\", ");
		sb.append(" curso.nome as \"curso.nome\", ");
		sb.append(" areaConhecimento.nome as \"areaConhecimento.nome\", ");
		sb.append(" (select count(distinct ForumInteracao.codigo) from ForumInteracao ");
		sb.append(" left join ForumAcesso on ForumAcesso.forum = ForumInteracao.forum and ForumAcesso.usuarioAcesso =  ").append(usuario.getCodigo());
		sb.append(" and ForumAcesso.codigo = (select max(fa.codigo) from ForumAcesso fa where fa.forum = Forum.codigo and fa.usuarioAcesso = ").append(usuario.getCodigo()).append(")");
		sb.append(" where ForumInteracao.forum = Forum.codigo and excluido = false and ForumInteracao.usuarioInteracao != ").append(usuario.getCodigo()).append(" and (ForumAcesso.codigo is null or forumAcesso.dataAcesso < ForumInteracao.dataInteracao) )");
		sb.append(" as qtdeAtualizacao");
		sb.append(" FROM forum ");
		sb.append(" inner join disciplina on forum.disciplina = disciplina.codigo ");
		sb.append(" inner join Usuario  as responsavelCriacao on forum.responsavelCriacao = responsavelCriacao.codigo ");
		sb.append(" left join Usuario  as responsavelInativacao on forum.responsavelInativacao = responsavelInativacao.codigo ");
		sb.append(" left join Pessoa  as pessoaCriacao on pessoaCriacao.codigo = responsavelCriacao.pessoa ");
		sb.append(" left join Arquivo on Arquivo.codigo = pessoaCriacao.arquivoImagem ");
		sb.append(" left join turma on forum.turma = turma.codigo ");
		sb.append(" left join curso on forum.curso = curso.codigo ");
		sb.append(" left join areaConhecimento on forum.areaConhecimento = areaConhecimento.codigo ");
		sb.append(" left join foruminteracao ON foruminteracao.forum = forum.codigo ");
		sb.append(" left join ForumAcesso on ForumAcesso.forum = Forum.codigo and ForumAcesso.usuarioAcesso = ").append(usuario.getCodigo());
		sb.append(" and ForumAcesso.codigo = (select max(fa.codigo) from ForumAcesso fa where fa.forum = Forum.codigo and fa.usuarioAcesso = ").append(usuario.getCodigo()).append(")");
		sb.append(" where Forum.situacaoForum is not null AND Forum.situacaoForum <> '").append(SituacaoForumEnum.INATIVO).append("' AND ");
		getSQLPadraoExistsDisciplinasForum(unidadeEnsino, usuario.getPessoa().getCodigo(), sb);
		executarValidacaoPermissaoApresentarSomenteForumCriadoProprioProfessor(usuario.getCodigo(), usuario.getPerfilAcesso().getCodigo(), sb);
		sb.append(" AND COALESCE(foruminteracao.usuariointeracao, 0) <> ").append(usuario.getCodigo());
		sb.append(" and (ForumAcesso.dataAcesso < forum.dataCriacao or ForumAcesso.dataAcesso < foruminteracao.datainteracao or ForumAcesso.dataAcesso is null) ");
		sb.append(" and (((forum.ano is null or forum.ano = '') and (forum.semestre is null or forum.semestre = '')) or "); 
		sb.append(" (forum.ano is not null and forum.semestre is null ) or ");
		sb.append(" (forum.ano is not null and forum.semestre is not null ))");
		if (limite != null && limite > 0) {
			sb.append(" limit ").append(limite).append(" offset ").append(pagina);
		}
		System.out.println(sb.toString());
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
		
	}
	
	@Override
	public Integer consultarTotalRegistroAtualizacaoForumVisaoProfessor(ForumVO obj, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder("SELECT count(distinct forum.codigo) as qtde ");
		sb.append(" FROM forum ");
		sb.append(" left join foruminteracao ON foruminteracao.forum = forum.codigo ");
		sb.append(" left join ForumAcesso on ForumAcesso.forum = Forum.codigo and ForumAcesso.usuarioAcesso = ").append(usuario.getCodigo());
		sb.append(" and ForumAcesso.codigo = (select max(fa.codigo) from ForumAcesso fa where fa.forum = Forum.codigo and fa.usuarioAcesso = ").append(usuario.getCodigo()).append(")");
		sb.append(" where Forum.situacaoForum is not null AND situacaoForum <> '").append(SituacaoForumEnum.INATIVO).append("' AND ");
		getSQLPadraoExistsDisciplinasForum(unidadeEnsino, usuario.getPessoa().getCodigo(), sb);
		executarValidacaoPermissaoApresentarSomenteForumCriadoProprioProfessor(usuario.getCodigo(), usuario.getPerfilAcesso().getCodigo(), sb);
		sb.append(" AND COALESCE(foruminteracao.usuariointeracao, 0) <> ").append(usuario.getCodigo());
		sb.append(" and (ForumAcesso.codigo is null or ForumAcesso.dataAcesso < forum.dataCriacao or ForumAcesso.dataAcesso < foruminteracao.datainteracao)");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	private void getSQLPadraoExistsDisciplinasForum(int unidadeEnsino, int professor, StringBuilder sb) {
		sb.append(" EXISTS ( ");
		getFacadeFactory().getDisciplinaFacade().getSQLPadraoConsultaDisciplinasProfessorProgramacaoAula(professor, unidadeEnsino, null, null, sb);
		sb.append(" AND ((COALESCE(forum.ano, '') <> ''	AND HorarioTurma.anovigente = forum.ano) OR (COALESCE(forum.ano, '') = '')) ");
		sb.append(" AND ((COALESCE(forum.semestre, '') <> '' AND HorarioTurma.semestrevigente = forum.semestre)	OR (COALESCE(forum.semestre, '') = '')) ");
		sb.append(" AND (forum.disciplina = disciplina.codigo) ");
		sb.append(" AND ((forum.turma is null) or (HorarioTurma.turma = forum.turma)) ");
		sb.append(" UNION ");
		getFacadeFactory().getDisciplinaFacade().getSQLPadraoConsultaDisciplinasProfessorEad(professor, unidadeEnsino, null, null, sb);
		sb.append(" AND ((COALESCE(forum.ano, '') <> ''	AND programacaotutoriaonline.ano = forum.ano) OR (COALESCE(forum.ano, '') = '') OR (COALESCE(programacaotutoriaonline.ano, '') = '')) ");
		sb.append(" AND ((COALESCE(forum.semestre, '') <> '' AND programacaotutoriaonline.semestre = forum.semestre) OR (COALESCE(forum.semestre, '') = '') OR (COALESCE(programacaotutoriaonline.semestre, '') = '')) ");
		sb.append(" AND (forum.disciplina = disciplina.codigo) ");
		sb.append(" AND ((forum.turma is null) or (programacaotutoriaonline.turma is not null and programacaotutoriaonline.turma = forum.turma))) ");
	}
	
	private void executarValidacaoPermissaoApresentarSomenteForumCriadoProprioProfessor(int usuario, int perfilAcesso, StringBuilder sb) {
		sb.append(" AND ((((SELECT nomeentidade FROM permissao WHERE codperfilacesso = ").append(perfilAcesso);
		sb.append(" AND nomeentidade = '").append(APRESENTAR_SOMENTE_FORUM_CRIADO_PROPRIO_PROFESSOR.getValor()).append("' AND permissoes LIKE '%(").append(Uteis.CONSULTAR).append(")%' ) IS NOT NULL) ");
		sb.append(" AND forum.responsavelcriacao = ").append(usuario).append(") OR ");
		sb.append(" ((SELECT nomeentidade FROM permissao WHERE codperfilacesso = ").append(perfilAcesso);
		sb.append(" AND nomeentidade = '").append(APRESENTAR_SOMENTE_FORUM_CRIADO_PROPRIO_PROFESSOR.getValor()).append("' AND permissoes LIKE '%(").append(Uteis.CONSULTAR).append(")%') IS NULL)) ");
	}
}
