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

import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaMatriculaPeriodoVO;
import negocio.comuns.academico.RenovacaoMatriculaTurmaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoRenovacaoMatriculaPeriodoEnum;
import negocio.comuns.academico.enumeradores.SituacaoRenovacaoTurmaEnum;
import negocio.comuns.academico.enumeradores.TipoAlunoCalendarioMatriculaEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.RenovacaoMatriculaTurmaMatriculaPeriodoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class RenovacaoMatriculaTurmaMatriculaPeriodo extends ControleAcesso implements RenovacaoMatriculaTurmaMatriculaPeriodoInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public RenovacaoMatriculaTurmaMatriculaPeriodo() {
		super();
		setIdEntidade("RenovacaoMatriculaTurmaMatriculaPeriodo");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(final RenovacaoMatriculaTurmaMatriculaPeriodoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.getNovoObj()) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final RenovacaoMatriculaTurmaMatriculaPeriodoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {

			final String sql = "INSERT INTO RenovacaoMatriculaTurmaMatriculaPeriodo (renovacaoMatriculaTurma, matriculaPeriodo, situacao, condicaoPagamentoPlanoFinanceiroCurso, mensagemErro,  informacaoFinanceira, novamatriculaperiodo) VALUES (?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getRenovacaoTurmaVO().getCodigo());
					sqlInserir.setInt(2, obj.getMatriculaPeriodoVO().getCodigo());
					sqlInserir.setString(3, obj.getSituacao().name());
//					if (!obj.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo().equals(0)) {
//						sqlInserir.setInt(4, obj.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo());
//					} else {
						sqlInserir.setNull(4, 0);
//					}
					sqlInserir.setString(5, obj.getMensagemErro());
					sqlInserir.setString(6, "");
					if (!obj.getNovaMatriculaPeriodoVO().getCodigo().equals(0)) {
						sqlInserir.setInt(7, obj.getNovaMatriculaPeriodoVO().getCodigo());
					} else {
						sqlInserir.setNull(7, 0);
					}
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
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
	public void alterar(final RenovacaoMatriculaTurmaMatriculaPeriodoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			final String sql = "UPDATE RenovacaoMatriculaTurmaMatriculaPeriodo set renovacaoMatriculaTurma = ?, matriculaPeriodo = ?,  situacao = ?, condicaoPagamentoPlanoFinanceiroCurso=?, mensagemErro = ?, informacaoFinanceira = ?, novaMatriculaPeriodo=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setInt(1, obj.getRenovacaoTurmaVO().getCodigo());
					sqlAlterar.setInt(2, obj.getMatriculaPeriodoVO().getCodigo());
					sqlAlterar.setString(3, obj.getSituacao().name());
//					if (!obj.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo().equals(0)) {
//						sqlAlterar.setInt(4, obj.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo());
//					} else {
						sqlAlterar.setNull(4, 0);
//					}
					sqlAlterar.setString(5, obj.getMensagemErro());
					sqlAlterar.setString(6, "");
					if (!obj.getNovaMatriculaPeriodoVO().getCodigo().equals(0)) {
						sqlAlterar.setInt(7, obj.getNovaMatriculaPeriodoVO().getCodigo());
					} else {
						sqlAlterar.setNull(7, 0);
					}
					sqlAlterar.setInt(8, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0){
				incluir(obj, verificarAcesso, usuarioVO);
				return;
			};
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	public void incluirRenovacaoMatriculaTurmaMatriculaPeriodoVOs(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (RenovacaoMatriculaTurmaMatriculaPeriodoVO obj : renovacaoMatriculaTurmaVO.getRenovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs()) {
			if (obj.getSelecionado()) {
				obj.setRenovacaoTurmaVO(renovacaoMatriculaTurmaVO);
				getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().persistir(obj, verificarAcesso, usuarioVO);
			}
		}
		for (RenovacaoMatriculaTurmaMatriculaPeriodoVO obj : renovacaoMatriculaTurmaVO.getRenovacaoMatriculaTurmaMatriculaPeriodoErroVOs()) {
			obj.setRenovacaoTurmaVO(renovacaoMatriculaTurmaVO);
			obj.setSituacao(SituacaoRenovacaoMatriculaPeriodoEnum.REALIZADO_ERRO);
			getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().persistir(obj, verificarAcesso, usuarioVO);
		}
	}
	
	@Override
	public void alterarRenovacaoMatriculaTurmaMatriculaPeriodoVOs(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sql  = new StringBuilder("DELETE FROM RenovacaoMatriculaTurmaMatriculaPeriodo ");
		sql.append(" WHERE RenovacaoMatriculaTurma = ").append(renovacaoMatriculaTurmaVO.getCodigo()).append(" and codigo not in (0 ");
		for (RenovacaoMatriculaTurmaMatriculaPeriodoVO obj : renovacaoMatriculaTurmaVO.getRenovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs()) {
			if (obj.getSelecionado()) {
				sql.append(", ").append(obj.getCodigo());
				obj.setRenovacaoTurmaVO(renovacaoMatriculaTurmaVO);
				obj.setSituacao(SituacaoRenovacaoMatriculaPeriodoEnum.AGUARDANDO_REALIZACAO);
				getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().persistir(obj, verificarAcesso, usuarioVO);
			}
		}
		for (RenovacaoMatriculaTurmaMatriculaPeriodoVO obj : renovacaoMatriculaTurmaVO.getRenovacaoMatriculaTurmaMatriculaPeriodoSucessoVOs()) {
			sql.append(", ").append(obj.getCodigo());
		}
		for (RenovacaoMatriculaTurmaMatriculaPeriodoVO obj : renovacaoMatriculaTurmaVO.getRenovacaoMatriculaTurmaMatriculaPeriodoErroVOs()) {
			sql.append(", ").append(obj.getCodigo());
		}
		sql.append(") ");
		getConexao().getJdbcTemplate().update(sql.toString());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RenovacaoMatriculaTurmaMatriculaPeriodoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {

			String sql = "DELETE FROM RenovacaoMatriculaTurmaMatriculaPeriodo WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> consultarPorRenovacaoMatriculaTurma(Integer renovacaoMatriculaTurma, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {

		StringBuilder sqlStr = new StringBuilder(getSqlConsultaCompleta());
		sqlStr.append("WHERE renovacaoMatriculaTurma = ").append(renovacaoMatriculaTurma);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}

	private StringBuilder getSqlConsultaCompleta() {
		StringBuilder sql = new StringBuilder("SELECT RenovacaoMatriculaTurmaMatriculaPeriodo.*, ");
		sql.append(" matricula.matricula, pessoa.nome as aluno, matriculaperiodo.situacaoMatriculaPeriodo,  ");
		
		sql.append(" periodoletivo.descricao as \"periodoletivo.descricao\",  ");
		sql.append(" periodoLetivo.periodoLetivo as \"periodoLetivo.periodoLetivo\",  ");
		sql.append(" periodoLetivo.codigo as \"periodoLetivo.codigo\",  ");
		sql.append(" matriculaperiodo.planofinanceirocurso as \"matriculaperiodo.planofinanceirocurso\",  ");
		sql.append(" matriculaperiodo.condicaopagamentoplanofinanceirocurso as \"matriculaperiodo.condicaopagamentoplanofinanceirocurso\",  ");
		
		sql.append(" novaPL.descricao as \"novaPL.descricao\",  ");
		sql.append(" novaPL.periodoLetivo as \"novaPL.periodoLetivo\",  ");
		sql.append(" novaPL.codigo as \"novaPL.codigo\",  ");
		sql.append(" novaMP.planofinanceirocurso as \"novaMP.planofinanceirocurso\",  ");
		sql.append(" novaMP.condicaopagamentoplanofinanceirocurso as \"novaMP.condicaopagamentoplanofinanceirocurso\",  ");
		
		
		sql.append(" condicaoPagamentoPlanoFinanceiroCurso.descricao as \"condicaoPagamentoPlanoFinanceiroCurso.descricao\"  ");
		sql.append(" from RenovacaoMatriculaTurmaMatriculaPeriodo ");		
		sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = RenovacaoMatriculaTurmaMatriculaPeriodo.matriculaPeriodo ");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = matriculaPeriodo.periodoletivomatricula ");
		sql.append(" inner join matricula on matriculaPeriodo.matricula = matricula.matricula ");
		sql.append(" inner join pessoa on matricula.aluno = pessoa.codigo ");
		sql.append(" left join condicaoPagamentoPlanoFinanceiroCurso on condicaoPagamentoPlanoFinanceiroCurso.codigo = matriculaPeriodo.condicaoPagamentoPlanoFinanceiroCurso ");
		sql.append(" left join matriculaPeriodo as  novaMP on novaMP.codigo = RenovacaoMatriculaTurmaMatriculaPeriodo.novamatriculaPeriodo ");
		sql.append(" left join periodoletivo as novaPL on novaPL.codigo = novaMP.periodoletivomatricula ");
		return sql;
	}

	@Override
	public List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> consultarPorRenovacaoMatriculaTurmaESituacao(Integer renovacaoMatriculaTurma, SituacaoRenovacaoMatriculaPeriodoEnum situacao, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO, Integer limite, Integer offset) throws Exception {
		StringBuilder sqlStr = new StringBuilder(getSqlConsultaCompleta());
		sqlStr.append(" WHERE renovacaoMatriculaTurma = ").append(renovacaoMatriculaTurma);
		sqlStr.append(" and RenovacaoMatriculaTurmaMatriculaPeriodo.situacao = '").append(situacao.name()).append("' ");
		sqlStr.append(" order by matricula.matricula ");
		if (limite != null && limite > 0) {
			sqlStr.append(" limit ").append(limite).append(" offset ").append(offset);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}

	private List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<RenovacaoMatriculaTurmaMatriculaPeriodoVO> objs = new ArrayList<RenovacaoMatriculaTurmaMatriculaPeriodoVO>(0);
		while (rs.next()) {
			objs.add(montarDados(rs, nivelMontarDados, usuarioVO));
		}
		return objs;
	}

	private RenovacaoMatriculaTurmaMatriculaPeriodoVO montarDados(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		RenovacaoMatriculaTurmaMatriculaPeriodoVO obj = new RenovacaoMatriculaTurmaMatriculaPeriodoVO();
		obj.setNovoObj(false);
		obj.setCodigo(rs.getInt("codigo"));
		obj.getRenovacaoTurmaVO().setCodigo(rs.getInt("renovacaoMatriculaTurma"));
		
		obj.getMatriculaPeriodoVO().setCodigo(rs.getInt("matriculaperiodo"));
		obj.getMatriculaPeriodoVO().setMatricula(rs.getString("matricula"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().setMatricula(rs.getString("matricula"));
		obj.getMatriculaPeriodoVO().getMatriculaVO().getAluno().setNome(rs.getString("aluno"));
		obj.getMatriculaPeriodoVO().setSituacaoMatriculaPeriodo(rs.getString("situacaoMatriculaPeriodo"));
		obj.getMatriculaPeriodoVO().getPeriodoLetivo().setDescricao(rs.getString("periodoLetivo.descricao"));
		obj.getMatriculaPeriodoVO().getPeriodoLetivo().setPeriodoLetivo(rs.getInt("periodoLetivo.periodoLetivo"));
		obj.getMatriculaPeriodoVO().getPeriodoLetivo().setCodigo(rs.getInt("periodoLetivo.codigo"));
//		obj.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().setCodigo(rs.getInt("matriculaperiodo.planofinanceirocurso"));
//		obj.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().setCodigo(rs.getInt("matriculaperiodo.condicaopagamentoplanofinanceirocurso"));
//		obj.getCondicaoPagamentoPlanoFinanceiroCursoVO().setCodigo(rs.getInt("condicaoPagamentoPlanoFinanceiroCurso"));
//		obj.getCondicaoPagamentoPlanoFinanceiroCursoVO().setDescricao(rs.getString("condicaoPagamentoPlanoFinanceiroCurso.descricao"));
		obj.setSituacao(SituacaoRenovacaoMatriculaPeriodoEnum.valueOf(rs.getString("situacao")));
		obj.setMensagemErro(rs.getString("mensagemErro"));
//		obj.setInformacaoFinanceira(rs.getString("informacaoFinanceira"));
		
		if(rs.getInt("novamatriculaperiodo") != 0) {
			obj.getNovaMatriculaPeriodoVO().setCodigo(rs.getInt("novamatriculaperiodo"));
			obj.getNovaMatriculaPeriodoVO().setMatricula(rs.getString("matricula"));
			obj.getNovaMatriculaPeriodoVO().getMatriculaVO().setMatricula(rs.getString("matricula"));
			obj.getNovaMatriculaPeriodoVO().getMatriculaVO().getAluno().setNome(rs.getString("aluno"));
			obj.getNovaMatriculaPeriodoVO().setSituacaoMatriculaPeriodo(rs.getString("situacaoMatriculaPeriodo"));
			obj.getNovaMatriculaPeriodoVO().getPeriodoLetivo().setDescricao(rs.getString("novaPL.descricao"));
			obj.getNovaMatriculaPeriodoVO().getPeriodoLetivo().setPeriodoLetivo(rs.getInt("novaPL.periodoLetivo"));
			obj.getNovaMatriculaPeriodoVO().getPeriodoLetivo().setCodigo(rs.getInt("novaPL.codigo"));
//			obj.getNovaMatriculaPeriodoVO().getPlanoFinanceiroCurso().setCodigo(rs.getInt("novaMP.planofinanceirocurso"));
//			obj.getNovaMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().setCodigo(rs.getInt("novaMP.condicaopagamentoplanofinanceirocurso"));	
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(obj.getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS,  usuarioVO));
			if(rs.getInt("novamatriculaperiodo") != 0) {
				obj.setNovaMatriculaPeriodoVO(getFacadeFactory().getMatriculaPeriodoFacade().consultarPorChavePrimaria(obj.getNovaMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS,  usuarioVO));	
			}
		}
		return obj;
	}

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		RenovacaoMatriculaTurmaMatriculaPeriodo.idEntidade = idEntidade;
	}

	/**
	 * Responsável por inicializar os dados comuns a todas as matrícula perídos
	 * para o ganho de performance
	 * 
	 * @param renovacaoMatriculaTurmaVO
	 * @param usuario
	 * @throws Exception
	 */
	@Override
	public void realizarInicializacaoDadosPertinentesProcessamento(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, UsuarioVO usuario) throws Exception {
//		RenovacaoMatriculaTurma.validarDados(renovacaoMatriculaTurmaVO);
//		Integer unidadeEnsino = renovacaoMatriculaTurmaVO.getUnidadeEnsinoVO().getCodigo() > 0 ? renovacaoMatriculaTurmaVO.getUnidadeEnsinoVO().getCodigo() : renovacaoMatriculaTurmaVO.getTurmaVO().getUnidadeEnsino().getCodigo() > 0 ? renovacaoMatriculaTurmaVO.getTurmaVO().getUnidadeEnsino().getCodigo() : 0;
//		Integer curso = renovacaoMatriculaTurmaVO.getCursoVO().getCodigo() > 0 ? renovacaoMatriculaTurmaVO.getCursoVO().getCodigo() : renovacaoMatriculaTurmaVO.getTurmaVO().getCurso().getCodigo() > 0 ? renovacaoMatriculaTurmaVO.getTurmaVO().getCurso().getCodigo() : 0;
//		
//		if (Uteis.isAtributoPreenchido(renovacaoMatriculaTurmaVO.getTurmaVO()) && !renovacaoMatriculaTurmaVO.getTurmaVO().getCurso().getConfiguracaoAcademico().getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
//			renovacaoMatriculaTurmaVO.getTurmaVO().getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorCodigoCurso(renovacaoMatriculaTurmaVO.getTurmaVO().getCurso().getCodigo(), usuario));
//			renovacaoMatriculaTurmaVO.getTurmaVO().getCurso().getConfiguracaoAcademico().setNivelMontarDados(NivelMontarDados.TODOS);
//		}
//		if (Uteis.isAtributoPreenchido(renovacaoMatriculaTurmaVO.getTurmaRenovar()) && !renovacaoMatriculaTurmaVO.getTurmaRenovar().getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
//			getFacadeFactory().getTurmaFacade().carregarDados(renovacaoMatriculaTurmaVO.getTurmaRenovar(), NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, usuario);
//			renovacaoMatriculaTurmaVO.getTurmaRenovar().setNivelMontarDados(NivelMontarDados.TODOS);
//		}
//		if (Uteis.isAtributoPreenchido(renovacaoMatriculaTurmaVO.getTurmaVO())) {
//			renovacaoMatriculaTurmaVO.setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidadeTurno(curso, unidadeEnsino, renovacaoMatriculaTurmaVO.getTurmaVO().getTurno().getCodigo(), usuario));
//			renovacaoMatriculaTurmaVO.getUnidadeEnsinoCursoVO().setNivelMontarDados(NivelMontarDados.TODOS);	
//			
//			renovacaoMatriculaTurmaVO.setProcessoMatriculaCalendarioVO(getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorChavePrimaria(curso, renovacaoMatriculaTurmaVO.getTurmaVO().getTurno().getCodigo(), renovacaoMatriculaTurmaVO.getProcessoMatriculaRenovar().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
//			renovacaoMatriculaTurmaVO.getProcessoMatriculaCalendarioVO().setNivelMontarDados(NivelMontarDados.TODOS);	
//		} 
////		if (Uteis.isAtributoPreenchido(renovacaoMatriculaTurmaVO.getPlanoFinanceiroCursoRenovar())) {
////			renovacaoMatriculaTurmaVO.setPlanoFinanceiroCursoRenovar(getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(renovacaoMatriculaTurmaVO.getPlanoFinanceiroCursoRenovar().getCodigo(), "AT", Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
////			renovacaoMatriculaTurmaVO.getPlanoFinanceiroCursoRenovar().setNivelMontarDados(NivelMontarDados.TODOS);	
////		}
//		if (Uteis.isAtributoPreenchido(renovacaoMatriculaTurmaVO.getProcessoMatriculaRenovar())) {
//			getFacadeFactory().getProcessoMatriculaFacade().carregarDados(renovacaoMatriculaTurmaVO.getProcessoMatriculaRenovar(), NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, usuario);
//			renovacaoMatriculaTurmaVO.getProcessoMatriculaRenovar().setNivelMontarDados(NivelMontarDados.TODOS);	
//		}
//		if (Uteis.isAtributoPreenchido(renovacaoMatriculaTurmaVO.getPeriodoLetivoRenovar())) {
//			renovacaoMatriculaTurmaVO.setPeriodoLetivoRenovar(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(renovacaoMatriculaTurmaVO.getPeriodoLetivoRenovar().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));	
//		}	
//				
	}
	
	public void realizarInicializacaoMapadeDadosPertinentesProcessamento(RenovacaoMatriculaTurmaVO rmt, RenovacaoMatriculaTurmaMatriculaPeriodoVO rmtmp, String anoBase, String semestreBase, UsuarioVO usuario) throws Exception {
		String keyMapaPeriodo ="G"+rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getGradeCurricularAtual().getCodigo()+"C"+ rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getCurso().getCodigo()+"P"+(rmtmp.getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo() + 1);
		if (!Uteis.isAtributoPreenchido(rmt.getPeriodoLetivoRenovar()) && !rmt.getMapaPeriodoLetivo().containsKey(keyMapaPeriodo)) {
			PeriodoLetivoVO ultimoPeriodo = getFacadeFactory().getPeriodoLetivoFacade().consultarUltimoPeriodoLetivoGradeCurricular(rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getGradeCurricularAtual().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			if(ultimoPeriodo.getCodigo().equals(rmtmp.getMatriculaPeriodoVO().getPeridoLetivo().getCodigo())) {
				rmtmp.setPeriodoLetivoRenovarTemp(rmtmp.getMatriculaPeriodoVO().getPeridoLetivo());
			}else {
				rmtmp.setPeriodoLetivoRenovarTemp(getFacadeFactory().getPeriodoLetivoFacade().consultarProximoPeriodoLetivoCurso(rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getGradeCurricularAtual().getCodigo(), rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getCurso().getCodigo(), rmtmp.getMatriculaPeriodoVO().getPeridoLetivo().getPeriodoLetivo() + 1, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));	
			}
			Uteis.checkState(!Uteis.isAtributoPreenchido(rmtmp.getPeriodoLetivoRenovarTemp()), "Não foi localizado o próximo Período Letivo para a renovação do aluno - "+rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getNome());
			rmt.getMapaPeriodoLetivo().put(keyMapaPeriodo, rmtmp.getPeriodoLetivoRenovarTemp());
		}else if(rmt.getMapaPeriodoLetivo().containsKey(keyMapaPeriodo)){
			rmtmp.setPeriodoLetivoRenovarTemp(rmt.getMapaPeriodoLetivo().get(keyMapaPeriodo));
		}
		
		Integer periodoLetivoRenovar = Uteis.isAtributoPreenchido(rmt.getPeriodoLetivoRenovar().getCodigo()) ? rmt.getPeriodoLetivoRenovar().getCodigo() :  rmtmp.getPeriodoLetivoRenovarTemp().getCodigo();
		String keyMapaTurmaRenovar ="U"+rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()+"C"+ rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getCurso().getCodigo()+"T"+rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getTurno().getCodigo()+"P"+periodoLetivoRenovar;
		if (!Uteis.isAtributoPreenchido(rmt.getTurmaRenovar()) && !rmt.getMapaTurmaRenovar().containsKey(keyMapaTurmaRenovar)) {
			List<TurmaVO> listaConsultaTurma = getFacadeFactory().getTurmaFacade().consultaRapidaPorPeriodoLetivoUnidadeEnsinoCursoTurno(periodoLetivoRenovar, rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getCurso().getCodigo(), rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getTurno().getCodigo(), false, usuario);
			Uteis.checkState(listaConsultaTurma.isEmpty(), "Não foi localizada nenhuma Turma para a renovação do aluno - "+rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getNome());
			rmtmp.setTurmaRenovarTemp(listaConsultaTurma.get(0));
			getFacadeFactory().getTurmaFacade().carregarDados(rmtmp.getTurmaRenovarTemp(), NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, usuario);			
			rmt.getMapaTurmaRenovar().put(keyMapaTurmaRenovar, rmtmp.getTurmaRenovarTemp());
		}else if(rmt.getMapaTurmaRenovar().containsKey(keyMapaTurmaRenovar)){
			rmtmp.setTurmaRenovarTemp(rmt.getMapaTurmaRenovar().get(keyMapaTurmaRenovar));
		}
		
		TurmaVO turmaRenovar = Uteis.isAtributoPreenchido(rmt.getTurmaRenovar().getCodigo()) ? rmt.getTurmaRenovar():  rmtmp.getTurmaRenovarTemp();
//		if(!Uteis.isAtributoPreenchido(rmt.getPlanoFinanceiroCursoRenovar())) {
//			rmtmp.setPlanoFinanceiroCursoRenovarTemp(turmaRenovar.getPlanoFinanceiroCurso());
//		}
//		Uteis.checkState(!Uteis.isAtributoPreenchido(rmt.getPlanoFinanceiroCursoRenovar()) && !Uteis.isAtributoPreenchido(rmtmp.getPlanoFinanceiroCursoRenovarTemp()), "Não foi localizada nenhum Plano Financeiro Curso para a renovação do aluno - "+rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getNome());
		
//		String keyMapaCppfc ="P"+turmaRenovar.getPlanoFinanceiroCurso().getCodigo();
//		if (!Uteis.isAtributoPreenchido(rmt.getCondicaoPagamentoPlanoFinanceiroCursoRenovar()) && !rmt.getMapaCondicaoPagamentoPlanoFinanceiroCursoVO().containsKey(keyMapaCppfc)) {
//			List<CondicaoPagamentoPlanoFinanceiroCursoVO> listaCppfc = getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarPorCodigoPlanoFinanceiroCurso(turmaRenovar.getPlanoFinanceiroCurso().getCodigo(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
//			Uteis.checkState(listaCppfc.isEmpty(), "Não foi localizada nenhuma Condição de Pagamento do Plano Financeiro Curso para a renovação do aluno - "+rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getNome());
//			rmtmp.setCondicaoPagamentoPlanoFinanceiroCursoRenovarTemp(listaCppfc.get(0));
//			rmt.getMapaCondicaoPagamentoPlanoFinanceiroCursoVO().put(keyMapaCppfc, listaCppfc.get(0));
//		}else if(rmt.getMapaCondicaoPagamentoPlanoFinanceiroCursoVO().containsKey(keyMapaCppfc)){
//			rmtmp.setCondicaoPagamentoPlanoFinanceiroCursoRenovarTemp(rmt.getMapaCondicaoPagamentoPlanoFinanceiroCursoVO().get(keyMapaCppfc));
//		}
		
		String keyMapaPm ="U"+rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()+"C"+ rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getCurso().getCodigo()+"T"+rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getTurno().getCodigo();
		if (!Uteis.isAtributoPreenchido(rmt.getProcessoMatriculaRenovar()) && !rmt.getMapaProcessoMatriculaRenovar().containsKey(keyMapaPm)) {
			List<ProcessoMatriculaVO> processoMatriculaVOs = getFacadeFactory().getProcessoMatriculaFacade().consultaRapidaPorSituacaoUnidadeEnsinoCursoTurnoSituacaoPeriodoLetivoAtivoUnidadeEnsinoCurso(rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getTurno().getCodigo(), rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getCurso().getCodigo(), rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), "AT", "PR_AT", anoBase, semestreBase, false, Uteis.NIVELMONTARDADOS_TODOS, usuario, TipoAlunoCalendarioMatriculaEnum.VETERANO);
			Uteis.checkState(processoMatriculaVOs.isEmpty(), "Não foi localizado nenhum Processo de Matrícula  para a renovação do aluno - "+rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getAluno().getNome());
			rmtmp.setProcessoMatriculaRenovarTemp(processoMatriculaVOs.get(0));
			rmt.getMapaProcessoMatriculaRenovar().put(keyMapaPm, processoMatriculaVOs.get(0));
		}else if(rmt.getMapaProcessoMatriculaRenovar().containsKey(keyMapaPm)){
			rmtmp.setProcessoMatriculaRenovarTemp(rmt.getMapaProcessoMatriculaRenovar().get(keyMapaPm));
		}
		
		Integer processoMatriculaRenovar = Uteis.isAtributoPreenchido(rmt.getProcessoMatriculaRenovar().getCodigo()) ? rmt.getProcessoMatriculaRenovar().getCodigo() : rmtmp.getProcessoMatriculaRenovarTemp().getCodigo();
		String keyMapaPmc ="C"+ rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getCurso().getCodigo()+"T"+rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getTurno().getCodigo()+"P"+processoMatriculaRenovar;
//		if (!rmt.getMapaProcessoMatriculaCalendarioVO().containsKey(keyMapaPmc)) {
//			ProcessoMatriculaCalendarioVO pmc = getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarPorChavePrimaria(rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getCurso().getCodigo(), rmtmp.getMatriculaPeriodoVO().getMatriculaVO().getTurno().getCodigo(), processoMatriculaRenovar, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
//			rmt.getMapaProcessoMatriculaCalendarioVO().put(keyMapaPmc, pmc);	
//		}
		rmt.setProcessoMatriculaCalendarioVO(rmt.getMapaProcessoMatriculaCalendarioVO().get(keyMapaPmc));
		rmt.setUnidadeEnsinoCursoVO(rmtmp.getMatriculaPeriodoVO().getUnidadeEnsinoCursoVO());
//		rmt.getUnidadeEnsinoCursoVO().setPlanoFinanceiroCurso(rmtmp.getMatriculaPeriodoVO().getPlanoFinanceiroCurso());
		rmt.getUnidadeEnsinoCursoVO().setNivelMontarDados(NivelMontarDados.TODOS);
		
	}
	
	public void realizarConsultarMatriculaPeriodoAptaRenovar(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean permitirRealizarMatriculaDisciplinaPreRequisito, UsuarioVO usuarioVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception {
		renovacaoMatriculaTurmaVO.setQtdeRenovacaoAGerada(0);
//		if (!renovacaoMatriculaTurmaVO.getTurmaVO().getCurso().getConfiguracaoAcademico().getPermiteEvoluirPeriodoLetivoCasoReprovado() 
//				&& renovacaoMatriculaTurmaVO.getTurmaVO().getCurso().getConfiguracaoAcademico().getIsPossuiControleDisciplinaReprovacao()) {
//			renovacaoMatriculaTurmaVO.setQtdeRenovacaoAGerada(getFacadeFactory().getMatriculaPeriodoFacade().consultaQuantidadeAlunoPorTurmaTurmaSituacaoMatriculaPeriodoESituacao(renovacaoMatriculaTurmaVO, renovacaoMatriculaTurmaVO.getTurmaVO().getCurso().getConfiguracaoAcademico().getIsPossuiControleDisciplinaReprovacao(), renovacaoMatriculaTurmaVO.getTurmaVO().getCurso().getConfiguracaoAcademico().getNumeroDisciplinaConsiderarReprovadoPeriodoLetivo(), usuarioVO));
//		} else {
//			renovacaoMatriculaTurmaVO.setQtdeRenovacaoAGerada(getFacadeFactory().getMatriculaPeriodoFacade().consultaQuantidadeAlunoPorTurmaTurmaSituacaoMatriculaPeriodoESituacao(renovacaoMatriculaTurmaVO, false, 0, usuarioVO));
//		}
	}

	/**
	 * Responsável por buscar todas as matrículas períodos aptas a renovar
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarConsultarMatriculaPeriodoAptaRenovarPorTurma(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean permitirRealizarMatriculaDisciplinaPreRequisito, UsuarioVO usuarioVO, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs, ProgressBarVO progressBarVO) throws Exception {
		List<MatriculaPeriodoVO> matriculaPeriodoVOs = null;
		String anoBase = renovacaoMatriculaTurmaVO.getAno();
		String semestreBase = renovacaoMatriculaTurmaVO.getSemestre();
//		if (!renovacaoMatriculaTurmaVO.getTurmaVO().getCurso().getConfiguracaoAcademico().getPermiteEvoluirPeriodoLetivoCasoReprovado() && renovacaoMatriculaTurmaVO.getTurmaVO().getCurso().getConfiguracaoAcademico().getIsPossuiControleDisciplinaReprovacao()) {
//			matriculaPeriodoVOs = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorTurmaTurmaSituacaoMatriculaPeriodoESituacao(renovacaoMatriculaTurmaVO, renovacaoMatriculaTurmaVO.getTurmaVO().getCurso().getConfiguracaoAcademico().getIsPossuiControleDisciplinaReprovacao(), renovacaoMatriculaTurmaVO.getTurmaVO().getCurso().getConfiguracaoAcademico().getNumeroDisciplinaConsiderarReprovadoPeriodoLetivo(),  usuarioVO);
//		} else {
//			matriculaPeriodoVOs = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorTurmaTurmaSituacaoMatriculaPeriodoESituacao(renovacaoMatriculaTurmaVO, false, 0,  usuarioVO);
//		}
		if(progressBarVO != null) {
			realizarInicializacaoDadosPertinentesProcessamento(renovacaoMatriculaTurmaVO, usuarioVO);
			progressBarVO.setMaxValue(matriculaPeriodoVOs.size());
			renovacaoMatriculaTurmaVO.setQtdeRenovacaoAGerada(matriculaPeriodoVOs.size());
		}else {
			if (renovacaoMatriculaTurmaVO.getSemestre().equals("2")) {
				anoBase = "" + (Integer.valueOf(renovacaoMatriculaTurmaVO.getAno()) + 1);
				semestreBase = "1";
			}else if (renovacaoMatriculaTurmaVO.getSemestre().equals("1")) {
				semestreBase = "2";
			}	
		}
		renovacaoMatriculaTurmaVO.getRenovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs().clear();
		renovacaoMatriculaTurmaVO.getRenovacaoMatriculaTurmaMatriculaPeriodoErroVOs().clear();
		for (MatriculaPeriodoVO matriculaPeriodoVO : matriculaPeriodoVOs) {
			RenovacaoMatriculaTurmaMatriculaPeriodoVO obj = new RenovacaoMatriculaTurmaMatriculaPeriodoVO();			
			try {				
				renovacaoMatriculaTurmaVO.setQtdeRenovacaoGerada(renovacaoMatriculaTurmaVO.getQtdeRenovacaoGerada() + 1);
				obj.setMatriculaPeriodoVO(matriculaPeriodoVO);
				if(progressBarVO != null) {
					progressBarVO.setStatus("Processando "+renovacaoMatriculaTurmaVO.getQtdeRenovacaoGerada()+" de "+renovacaoMatriculaTurmaVO.getQtdeRenovacaoAGerada());
					obj.setSituacao(SituacaoRenovacaoMatriculaPeriodoEnum.AGUARDANDO_REALIZACAO);
				}else {
//					if (getFacadeFactory().getRenovacaoMatriculaTurmaFacade().realizarVerificacaoRenovacaoTurmaInterrompida(renovacaoMatriculaTurmaVO)) {
//						renovacaoMatriculaTurmaVO.setSituacao(SituacaoRenovacaoTurmaEnum.PROCESSAMENTO_INTERROMPIDO);
//						break;
//					}
					realizarInicializacaoMapadeDadosPertinentesProcessamento(renovacaoMatriculaTurmaVO, obj, anoBase, semestreBase, usuarioVO);
					obj.setSituacao(SituacaoRenovacaoMatriculaPeriodoEnum.REALIZADO_SUCESSO);
					obj.setSelecionado(true);
				}
				obj.setNovaMatriculaPeriodoVO(realizarRenovacaoAutomaticaAtravesRenovacaPorTurma(renovacaoMatriculaTurmaVO, obj,  configuracaoGeralSistemaVO, permitirRealizarMatriculaDisciplinaPreRequisito, usuarioVO, progressBarVO != null, gradeDisciplinaCompostaVOs));
				renovacaoMatriculaTurmaVO.getRenovacaoMatriculaTurmaMatriculaPeriodoNaoRealizadoVOs().add(obj);
			} catch (Exception e) {
				obj.setNovaMatriculaPeriodoVO(new MatriculaPeriodoVO());
				obj.setSituacao(SituacaoRenovacaoMatriculaPeriodoEnum.REALIZADO_ERRO);
				obj.setMensagemErro(e.getMessage());
				obj.setSelecionado(false);
				renovacaoMatriculaTurmaVO.getRenovacaoMatriculaTurmaMatriculaPeriodoErroVOs().add(obj);
			}
			if(progressBarVO == null) {
				obj.setRenovacaoTurmaVO(renovacaoMatriculaTurmaVO);
				try {
					getFacadeFactory().getRenovacaoMatriculaTurmaMatriculaPeriodoFacade().persistir(obj, false, usuarioVO);	
				} catch (Exception e) {
					e.printStackTrace();
				}					
			}else {
				progressBarVO.incrementar();
			}
		}
		//renovacaoMatriculaTurmaVO.setQtdeRenovacaoGerada(renovacaoMatriculaTurmaVO.getQtdeRenovacaoGerada() + 1);
		//progressBarVO.incrementar();
	}

	/*
	 * Este verifica se o período letivo selecionado pelo usuário possa ser
	 * aplicado para o aluno, caso não seja esta matricula não poderá ser
	 * renovada.
	 */
	private void realizarDefinicaoPeriodoLetivoMatriculaPeriodoAtravesRenovacaPorTurma(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, RenovacaoMatriculaTurmaMatriculaPeriodoVO rmtmp, MatriculaVO matriculaVO, MatriculaPeriodoVO novaMatriculaPeriodo, UsuarioVO usuarioVO) throws Exception {
		Integer codigoPeriodoLetivoRenovar = Uteis.isAtributoPreenchido(renovacaoMatriculaTurmaVO.getPeriodoLetivoRenovar().getCodigo()) ? renovacaoMatriculaTurmaVO.getPeriodoLetivoRenovar().getCodigo() : rmtmp.getPeriodoLetivoRenovarTemp().getCodigo();		
		getFacadeFactory().getMatriculaFacade().executarDefinicaoPeriodoLetivoNovaMatriculaAluno(matriculaVO, novaMatriculaPeriodo, matriculaVO.getGradeCurricularAtual().getCodigo(), true, usuarioVO);
		boolean permiteRenovarPeriodoLetivo = false;
		for (PeriodoLetivoVO periodo : novaMatriculaPeriodo.getListaPeriodosLetivosValidosParaMatriculaPeriodo()) {
			if (periodo.getCodigo().intValue() == codigoPeriodoLetivoRenovar) {
				permiteRenovarPeriodoLetivo = true;
				break;
			}
		}
		if (!permiteRenovarPeriodoLetivo) {
			if (novaMatriculaPeriodo.getListaPeriodosLetivosValidosParaMatriculaPeriodo().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_RenovacaoMatriculaTurma_periodoLetivoInvalido2"));
			} else {
				throw new Exception(UteisJSF.internacionalizar("msg_RenovacaoMatriculaTurma_periodoLetivoInvalido").replace("{0}", novaMatriculaPeriodo.getListaPeriodosLetivosValidosParaMatriculaPeriodo().get(novaMatriculaPeriodo.getListaPeriodosLetivosValidosParaMatriculaPeriodo().size() - 1).getDescricao()));
			}
		}
		novaMatriculaPeriodo.setPeridoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(codigoPeriodoLetivoRenovar, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
	}

	/**
	 * Este método é responsável em verificar se existe uma condição pagamento
	 * para uma determinado matricula periodo, seguindo a seguinte regra: 1º
	 * Busca todas as condições possíveis para este aluno 2º Verifica se a opção
	 * manter condição de pagamento atual do aluno = true então verifica se a
	 * condição da matricula periodo anterior do aluno está na lista das
	 * condições possíveis para o aluno 3º Se escolhido uma condição de
	 * pagamento fixa para todos os alunos então verifica se existe na lista da
	 * possíveis condições de pagamento para o aluno. 4º Caso não tenha
	 * escolhido um condição de pagamento especifica e nem foi marcado para
	 * manter a condição de pagamento atual do aluno então é verificado se
	 * existe apenas uma possível condição para este aluno, caso contrario este
	 * aluno não fica marcado com a opção selecionar marcado e o mesmo fica
	 * aguardando que o usuário informe esta situação.
	 * 
	 * Obs: Caso não exista condição de pagamento para o aluno ao a definida
	 * pelo usuário não seja aplicavel para o aluno então este não podera ser
	 * renovado
	 * 
	 * @param rmt
	 * @param rmtmp
	 * @param matriculaVO
	 * @param novaMatriculaPeriodo
	 * @param usuarioVO
	 * @param simularRenovacao
	 * @throws Exception
	 */
	private void realizarDefinicaoCondicaoPagamentoMatriculaPeriodoAtravesRenovacaPorTurma(RenovacaoMatriculaTurmaVO rmt, RenovacaoMatriculaTurmaMatriculaPeriodoVO rmtmp, MatriculaVO matriculaVO, MatriculaPeriodoVO novaMatriculaPeriodo, UsuarioVO usuarioVO, boolean simularRenovacao) throws Exception {
//		novaMatriculaPeriodo.setPlanoFinanceiroCurso(Uteis.isAtributoPreenchido(rmt.getPlanoFinanceiroCursoRenovar().getCodigo()) ? rmt.getPlanoFinanceiroCursoRenovar() : rmtmp.getPlanoFinanceiroCursoRenovarTemp());
//		novaMatriculaPeriodo.setPlanoFinanceiroCurso(null);
//		novaMatriculaPeriodo.setCondicaoPagamentoPlanoFinanceiroCurso(null);
//		novaMatriculaPeriodo.setContratoFiador(novaMatriculaPeriodo.getPlanoFinanceiroCurso().getTextoPadraoContratoFiador());
//		novaMatriculaPeriodo.setContratoExtensao(novaMatriculaPeriodo.getPlanoFinanceiroCurso().getTextoPadraoContratoExtensao());
		
		if (rmtmp.isNovoObj() || simularRenovacao) {
			rmtmp.setSelecionado(true);
		}
//			List<CondicaoPagamentoPlanoFinanceiroCursoVO> resultadoConsulta = getFacadeFactory().getMatriculaPeriodoFacade().executarMontagemComboCondicaoPagamentoPlanoFinanceiroCurso(novaMatriculaPeriodo.getPlanoFinanceiroCurso().getCodigo(), rmtmp.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo(), matriculaVO, novaMatriculaPeriodo, usuarioVO);
//			rmtmp.setListaSelectItemCondicaoPagamentoPlanoFinanceiroCurso(resultadoConsulta);
//			if (resultadoConsulta.isEmpty()) {
//				throw new Exception(UteisJSF.internacionalizar("msg_RenovacaoMatriculaTurma_condicaoPagamentoNaoEncontrado"));
//			}
//			if (rmt.getManterCondicaoPagamentoAtual()) {
//				if (rmtmp.getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo().intValue() == novaMatriculaPeriodo.getPlanoFinanceiroCurso().getCodigo()) {
//					rmtmp.getCondicaoPagamentoPlanoFinanceiroCursoVO().setCodigo(rmtmp.getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo());
//					
//				}
//			} else if (rmt.getCondicaoPagamentoPlanoFinanceiroCursoRenovar().getCodigo() > 0) {
//				rmtmp.getCondicaoPagamentoPlanoFinanceiroCursoVO().setCodigo(rmt.getCondicaoPagamentoPlanoFinanceiroCursoRenovar().getCodigo());
//			} else {
//				if (resultadoConsulta.size() == 1) {
//					rmtmp.getCondicaoPagamentoPlanoFinanceiroCursoVO().setCodigo(resultadoConsulta.get(0).getCodigo());
//				} else {
//					rmtmp.setCondicaoPagamentoPlanoFinanceiroCursoVO(null);
//					rmtmp.setMensagemErro(UteisJSF.internacionalizar("msg_RenovacaoMatriculaTurma_condicaoPagamentoInvaliado2"));
//				}
//			}
//			if (rmtmp.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo() > 0) {
//				boolean existeCondicaoParaAluno = false;
//				for (CondicaoPagamentoPlanoFinanceiroCursoVO obj : resultadoConsulta) {
//					if (obj.getCodigo().intValue() == rmtmp.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo().intValue()) {
//						existeCondicaoParaAluno = true;
//						break;
//					}
//				}
//				if (!existeCondicaoParaAluno) {
//					throw new Exception(UteisJSF.internacionalizar("msg_RenovacaoMatriculaTurma_condicaoPagamentoInvaliado"));
//				}
//				novaMatriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().setCodigo(rmtmp.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo());
				
//			} else {
//				rmtmp.setSelecionado(false);
//			}
//		} else {
//			novaMatriculaPeriodo.setCondicaoPagamentoPlanoFinanceiroCurso(rmtmp.getCondicaoPagamentoPlanoFinanceiroCursoVO());
//		}
	}

	/**
	 * Este método faz todas as operações que são realizadas ao renovar um aluno
	 * pela tela de renovação de matricula, sendo que o parâmetro
	 * simularRenovacao define se a mesma será gravada ou não (quando está
	 * apenas buscando os aluno o sistema apena irá simular para saber se o
	 * aluno se encaixa nos parâmetros definidos)
	 * 
	 * @param rmt
	 * @param rmtmp
	 * @param configuracaoFinanceiroVO
	 * @param configuracaoGeralSistemaVO
	 * @param permitirRealizarMatriculaDisciplinaPreRequisito
	 *            - Se TRUE irá ignorar se existir uma disciplina como
	 *            pré-requisito incluida para o aluno
	 * @param liberarRenovacaoComDebitosFinanceiros
	 *            -Se TRUE não irá validar a pendência financeira do aluno.
	 * @param usuarioVO
	 * @param simularRenovacao
	 *            - Se TRUE não irá gravar a renovação
	 * @throws Exception	 * 
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MatriculaPeriodoVO realizarRenovacaoAutomaticaAtravesRenovacaPorTurma(RenovacaoMatriculaTurmaVO rmt, RenovacaoMatriculaTurmaMatriculaPeriodoVO rmtmp, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean permitirRealizarMatriculaDisciplinaPreRequisito, UsuarioVO usuarioVO, boolean simularRenovacao, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception {
		getFacadeFactory().getMatriculaFacade().carregarDados(rmtmp.getMatriculaPeriodoVO().getMatriculaVO(), usuarioVO);
		MatriculaVO matriculaVO = rmtmp.getMatriculaPeriodoVO().getMatriculaVO();
		if (getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(matriculaVO, matriculaVO.getGradeCurricularAtual().getCodigo(), usuarioVO, null) ) {
        	throw new Exception("Aluno com o histórico Integralizado.");
        }
		MatriculaPeriodoVO novaMatriculaPeriodo = getFacadeFactory().getMatriculaFacade().realizarGeracaoNovaMatriculaPeriodoParaRenovacao(matriculaVO, usuarioVO);
		novaMatriculaPeriodo.setData(rmt.getDataRenovacao());
		rmtmp.getMatriculaPeriodoVO().setMatriculaVO(matriculaVO);		
		novaMatriculaPeriodo.setProcessoMatriculaVO(Uteis.isAtributoPreenchido(rmt.getProcessoMatriculaRenovar().getCodigo()) ? rmt.getProcessoMatriculaRenovar() :  rmtmp.getProcessoMatriculaRenovarTemp());
		novaMatriculaPeriodo.setProcessoMatricula(novaMatriculaPeriodo.getProcessoMatriculaVO().getCodigo());		
		getFacadeFactory().getMatriculaPeriodoFacade().validarDadosRenovacaoProcessoMatriculaPeriodoLetivo(matriculaVO, novaMatriculaPeriodo, usuarioVO, null);
		if (matriculaVO.getCurso().getNivelEducacional().equals("GT") || matriculaVO.getCurso().getNivelEducacional().equals("SU")) {
			getFacadeFactory().getMatriculaPeriodoFacade().consultarPorMatriculaPeriodoLetivoAtivoAnoSemestre(matriculaVO.getMatricula(), novaMatriculaPeriodo.getAno(), novaMatriculaPeriodo.getSemestre(), novaMatriculaPeriodo.getCodigo(), usuarioVO);
		}
		realizarDefinicaoPeriodoLetivoMatriculaPeriodoAtravesRenovacaPorTurma(rmt, rmtmp, matriculaVO, novaMatriculaPeriodo, usuarioVO);
		
		novaMatriculaPeriodo.setTurma(Uteis.isAtributoPreenchido(rmt.getTurmaRenovar().getCodigo()) ? rmt.getTurmaRenovar() :  rmtmp.getTurmaRenovarTemp());
		novaMatriculaPeriodo.setUnidadeEnsinoCurso(rmt.getUnidadeEnsinoCursoVO().getCodigo());
		novaMatriculaPeriodo.setUnidadeEnsinoCursoVO(rmt.getUnidadeEnsinoCursoVO());
		novaMatriculaPeriodo.setProcessoMatriculaCalendarioVO(rmt.getProcessoMatriculaCalendarioVO());
		
		getFacadeFactory().getMatriculaPeriodoFacade().definirSituacaoMatriculaPeriodoComBaseProcesso(matriculaVO, novaMatriculaPeriodo, usuarioVO);
//		realizarDefinicaoCondicaoPagamentoMatriculaPeriodoAtravesRenovacaPorTurma(rmt, rmtmp, matriculaVO, novaMatriculaPeriodo, usuarioVO, simularRenovacao);
		getFacadeFactory().getMatriculaFacade().realizarDefinicaoDocumentacaoMatriculaAluno(matriculaVO, novaMatriculaPeriodo, configuracaoGeralSistemaVO, simularRenovacao, usuarioVO);
		getFacadeFactory().getMatriculaFacade().realizarDefinicaoDisciplinaMatriculaPeriodo(matriculaVO, novaMatriculaPeriodo, configuracaoGeralSistemaVO, true, rmt.isLiberadoInclusaoTurmaOutroUnidadeEnsino(), rmt.isLiberadoInclusaoTurmaOutroCurso(), rmt.isLiberadoInclusaoTurmaOutroMatrizCurricular(), usuarioVO, gradeDisciplinaCompostaVOs);
//		realizarDefinicaoDadosFinanceiroMatriculaPeriodo(rmt, rmtmp, matriculaVO, novaMatriculaPeriodo, configuracaoFinanceiroVO, configuracaoGeralSistemaVO, permitirRealizarMatriculaDisciplinaPreRequisito, usuarioVO, simularRenovacao, gradeDisciplinaCompostaVOs);		
		if (!simularRenovacao) {
			getFacadeFactory().getMatriculaFacade().persistir(matriculaVO, novaMatriculaPeriodo, configuracaoGeralSistemaVO, usuarioVO);
		}
		return novaMatriculaPeriodo;
	}

	@Override
	public void realizarDefinicaoDadosFinanceiroMatriculaPeriodo(RenovacaoMatriculaTurmaVO renovacaoMatriculaTurmaVO, RenovacaoMatriculaTurmaMatriculaPeriodoVO renovacaoMatriculaTurmaMatriculaPeriodoVO, MatriculaVO matriculaVO, MatriculaPeriodoVO novaMatriculaPeriodo,   ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean permitirRealizarMatriculaDisciplinaPreRequisito, UsuarioVO usuarioVO, boolean simularRenovacao, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs) throws Exception {
		if (novaMatriculaPeriodo.getMatricula().trim().isEmpty()) {
			renovacaoMatriculaTurmaMatriculaPeriodoVO.setNovaMatriculaPeriodoVO(realizarRenovacaoAutomaticaAtravesRenovacaPorTurma(renovacaoMatriculaTurmaVO, renovacaoMatriculaTurmaMatriculaPeriodoVO,  configuracaoGeralSistemaVO, permitirRealizarMatriculaDisciplinaPreRequisito, usuarioVO, simularRenovacao, gradeDisciplinaCompostaVOs));
			return;
		}
//		if (renovacaoMatriculaTurmaMatriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo() > 0 || !simularRenovacao) {			
//			novaMatriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().setCodigo(renovacaoMatriculaTurmaMatriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCursoVO().getCodigo());
//			getFacadeFactory().getMatriculaFacade().realizarDefinicaoDadosFinanceiroMatriculaPeriodo(matriculaVO, novaMatriculaPeriodo, configuracaoFinanceiroVO, permitirRealizarMatriculaDisciplinaPreRequisito, usuarioVO);
//			StringBuilder informacao = new StringBuilder("");			
//			informacao.append("<table width=\"100%\">");
//			informacao.append("<tbody>");
//			informacao.append("<tr>");
//			informacao.append("<td><div style=\"width:10px;float-left\"></div></td>");
//			informacao.append("<td><span class=\"tituloCampos\">Valor Base</span></td>");
//			informacao.append("<td><span class=\"tituloCampos\">Valor com Descontos</span></td>");
//			informacao.append("</tr>");
//			informacao.append("<tr>");
//			informacao.append("<td><span class=\"tituloCampos\">Matrícula</span></td>");
//			informacao.append("<td><span class=\"tituloCampos\">").append(Uteis.getDoubleFormatado(novaMatriculaPeriodo.getValorMatriculaCheio())).append("</span><div style=\"width:10px;float-left\"></div></td>");
//			informacao.append("<td>");
//			informacao.append("<table class=\"rich-table\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
//			informacao.append("<colgroup span=\"1\"></colgroup>");
//			informacao.append("<tbody> ");
//			int x = 0;
//			for (PlanoFinanceiroAlunoDescricaoDescontosVO desc : novaMatriculaPeriodo.getListaDescontosMatricula()) {
//				informacao.append("<tr class=\"rich-table-row\">");
//				informacao.append("<td class=\"rich-table-cell colunaEsquerda\"> ");
//				if (desc.getValorCusteadoContaReceber() > 0) {
//					informacao.append("Valor Custeado Convênio: ").append(Uteis.getDoubleFormatado(desc.getValorCusteadoContaReceber())).append(" - ");
//				}
//				informacao.append(desc.getDescricaoResumida()).append(" ");				
//				informacao.append("<input id=\"form:descricaoDescontosMatricula:" + x + ":descricao\" name=\"form:descricaoDescontosMatricula:" + x + ":descricao\" ");
//				informacao.append("onclick=\"A4J.AJAX.Submit('form',event,{'similarityGroupingId':'form:descricaoDescontosMatricula:" + x + ":descricao', ");
//				informacao.append("'parameters':{'form:descricaoDescontosMatricula:" + x + ":descricao':'form:descricaoDescontosMatricula:" + x + ":descricao'} ,'containerId':'desc" + x + "'} );return false;\" ");
//				informacao.append("style=\"position:relative; left:+10px; top:0px\" title=\"").append(desc.getDescricaoDetalhada()).append("\" type=\"image\" src=\"../../resources/imagens/help.gif\">");
//				informacao.append("</td>");
//				informacao.append("</tr>");
//				x++;
//			}
//			informacao.append("</tbody>");
//			informacao.append("</table>");
//			informacao.append("</td>");
//			informacao.append("</tr>");
//			informacao.append("<tr>");
//			informacao.append("<td><span class=\"tituloCampos\">" + novaMatriculaPeriodo.getCondicaoPagamentoPlanoFinanceiroCurso().getNrParcelasPeriodo() + " Parcela(s)</span></td>");
//			informacao.append("<td><span class=\"tituloCampos\">").append(Uteis.getDoubleFormatado(novaMatriculaPeriodo.getValorMensalidadeCheio())).append("</span><div style=\"width:10px;float-left\"></div></td>");
//			informacao.append("<td>");
//			informacao.append("<table class=\"rich-table\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
//			informacao.append("<colgroup span=\"1\"></colgroup>");
//			informacao.append("<tbody>");
//			x = 0;
//			for (PlanoFinanceiroAlunoDescricaoDescontosVO desc : novaMatriculaPeriodo.getListaDescontosMensalidade()) {
//				informacao.append("<tr class=\"rich-table-row\">");
//				informacao.append("<td class=\"rich-table-cell colunaEsquerda\"> ");
//				if (desc.getValorCusteadoContaReceber() > 0) {
//					informacao.append("Valor Custeado Convênio: ").append(Uteis.getDoubleFormatado(desc.getValorCusteadoContaReceber())).append(" - ");
//				}
//				informacao.append(desc.getDescricaoResumida()).append(" ");				
//				informacao.append("<input id=\"form:descricaoDescontosMensalidade:" + x + ":descricao\" name=\"form:descricaoDescontosMensalidade:" + x + ":descricao\" ");
//				informacao.append("onclick=\"A4J.AJAX.Submit('form',event,{'similarityGroupingId':'form:descricaoDescontosMensalidade:" + x + ":descricao', ");
//				informacao.append("'parameters':{'form:descricaoDescontosMensalidade:" + x + ":descricao':'form:descricaoDescontosMensalidade:" + x + ":descricao'} ,'containerId':'desc" + x + "Mensalidade'} );return false;\" ");
//				informacao.append("style=\"position:relative; left:+10px; top:0px\" title=\"").append(desc.getDescricaoDetalhada()).append("\" type=\"image\" src=\"../../resources/imagens/help.gif\">");
//				informacao.append("</td>");
//				informacao.append("</tr>");
//				x++;
//			}
//			informacao.append("</tbody>");
//			informacao.append("</table>");
//			informacao.append("</td>");
//			informacao.append("</tr>");
//			informacao.append("");
//			informacao.append("</tbody>");
//			informacao.append("</table>");
//			renovacaoMatriculaTurmaMatriculaPeriodoVO.setInformacaoFinanceira(informacao.toString());
//
//		} else {
//			renovacaoMatriculaTurmaMatriculaPeriodoVO.setInformacaoFinanceira("");
//		}
	}

}
