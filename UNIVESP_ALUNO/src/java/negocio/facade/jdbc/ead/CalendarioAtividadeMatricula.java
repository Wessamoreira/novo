package negocio.facade.jdbc.ead;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import jobs.JobAvaliacaoOnline;
import negocio.comuns.academico.CalendarioLancamentoNotaVO;
import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.DefinicoesTutoriaOnlineEnum;
import negocio.comuns.academico.enumeradores.ModalidadeDisciplinaEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.TipoCoordenadorCursoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.ead.AtividadeDiscursivaRespostaAlunoVO;
import negocio.comuns.ead.AtividadeDiscursivaVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.AvaliacaoOnlineVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.ConfiguracaoEADVO;
import negocio.comuns.ead.GraficoAproveitamentoAvaliacaoVO;
import negocio.comuns.ead.ProgramacaoTutoriaOnlineVO;
import negocio.comuns.ead.enumeradores.RegraDefinicaoPeriodoAvaliacaoOnlineEnum;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeEnum;
import negocio.comuns.ead.enumeradores.SituacaoAvaliacaoOnlineMatriculaEnum;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.ead.enumeradores.TipoControleLiberacaoAvaliacaoOnlineEnum;
import negocio.comuns.ead.enumeradores.TipoControleLiberarAcessoProximaDisciplinaEnum;
import negocio.comuns.ead.enumeradores.TipoNivelProgramacaoTutoriaEnum;
import negocio.comuns.ead.enumeradores.TipoOrigemEnum;
import negocio.comuns.ead.enumeradores.TipoUsoEnum;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.academico.Historico;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.CalendarioAtividadeMatriculaInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import webservice.servicos.objetos.DataEventosRSVO;

/**
 * @author Victor Hugo 17/09/2014
 */
@Repository
@Scope("singleton")
@Lazy
public class CalendarioAtividadeMatricula extends ControleAcesso implements CalendarioAtividadeMatriculaInterfaceFacade, Serializable {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		CalendarioAtividadeMatricula.idEntidade = idEntidade;
	}

	public CalendarioAtividadeMatricula() throws Exception {
		super();
		setIdEntidade("CalendarioAtividadeMatricula");
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(calendarioAtividadeMatriculaVO);
//			CalendarioAtividadeMatricula.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = "insert into calendarioatividadematricula(descricao, historicomodificacao, datacadastro, responsavelcadastro, datainicio, datafim, tipocalendarioatividade," + "tipoorigem, codorigem, matricula, matriculaperiodoturmadisciplina, datarealizado, nrvezesperiodoprorrogado, situacaoatividade, avaliacaoonlinematricula, matriculaperiodo)" + "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			calendarioAtividadeMatriculaVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql);
					sqlInserir.setString(1, calendarioAtividadeMatriculaVO.getDescricao());
					sqlInserir.setString(2, calendarioAtividadeMatriculaVO.getHistoricoModificacao());
					sqlInserir.setTimestamp(3, Uteis.getDataJDBCTimestamp(calendarioAtividadeMatriculaVO.getDataCadastro()));
					if (calendarioAtividadeMatriculaVO.getResponsavelCadastro().getCodigo() != 0) {
						sqlInserir.setInt(4, calendarioAtividadeMatriculaVO.getResponsavelCadastro().getCodigo());
					} else {
						sqlInserir.setNull(4, 0);
					}
					if(calendarioAtividadeMatriculaVO.getDataInicio() != null) {
						sqlInserir.setTimestamp(5, Uteis.getDataJDBCTimestamp(calendarioAtividadeMatriculaVO.getDataInicio()));						
					}else {
						sqlInserir.setNull(5, 0);
					}
					if(calendarioAtividadeMatriculaVO.getDataFim() != null) {
					sqlInserir.setTimestamp(6, Uteis.getDataJDBCTimestamp(calendarioAtividadeMatriculaVO.getDataFim()));
					}else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setString(7, calendarioAtividadeMatriculaVO.getTipoCalendarioAtividade().name());
					sqlInserir.setString(8, calendarioAtividadeMatriculaVO.getTipoOrigem().name());
					if(Uteis.isAtributoPreenchido(calendarioAtividadeMatriculaVO.getCodOrigem())){
						sqlInserir.setString(9, calendarioAtividadeMatriculaVO.getCodOrigem());	
					}else{
						sqlInserir.setNull(9, 0);
					}
					sqlInserir.setString(10, calendarioAtividadeMatriculaVO.getMatriculaVO().getMatricula());
					if (Uteis.isAtributoPreenchido(calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO())) {
						sqlInserir.setInt(11, calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());						
					} else {
						sqlInserir.setNull(11, 0);
					}
					if (calendarioAtividadeMatriculaVO.getSituacaoAtividade().equals(SituacaoAtividadeEnum.CONCLUIDA)) {
						sqlInserir.setTimestamp(12, Uteis.getDataJDBCTimestamp(calendarioAtividadeMatriculaVO.getDateRealizado()));
					} else {
						sqlInserir.setNull(12, 0);
					}
					sqlInserir.setInt(13, calendarioAtividadeMatriculaVO.getNrVezesProrrogado());
					sqlInserir.setString(14, calendarioAtividadeMatriculaVO.getSituacaoAtividade().name());
					if (!calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().getCodigo().equals(0)) {
						sqlInserir.setInt(15, calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().getCodigo());
					} else {
						sqlInserir.setNull(15, 0);
					}
					if (calendarioAtividadeMatriculaVO.getMatriculaPeriodoVO().getCodigo() != 0) {
						sqlInserir.setInt(16, calendarioAtividadeMatriculaVO.getMatriculaPeriodoVO().getCodigo());
					} else {
						sqlInserir.setNull(16, 0);
					}

					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						calendarioAtividadeMatriculaVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			calendarioAtividadeMatriculaVO.setNovoObj(Boolean.TRUE);
			calendarioAtividadeMatriculaVO.setCodigo(0);
			throw e;
		}
	}

	@Override
	public void validarDados(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO) throws Exception {

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (calendarioAtividadeMatriculaVO.getCodigo() == 0) {
			incluir(calendarioAtividadeMatriculaVO, verificarAcesso, usuarioVO);
		} else {
			alterar(calendarioAtividadeMatriculaVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = " UPDATE calendarioatividadematricula set descricao = ?, historicomodificacao = ?, datacadastro = ?, responsavelcadastro = ?, " + "datainicio = ?, datafim = ?, tipocalendarioatividade = ?,  tipoorigem = ?, codorigem = ?, " + "matricula = ?, matriculaperiodoturmadisciplina = ?, datarealizado = ?, nrvezesperiodoprorrogado = ?, situacaoatividade = ?, avaliacaoonlinematricula = ?, matriculaperiodo = ? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setString(1, calendarioAtividadeMatriculaVO.getDescricao());
					sqlAlterar.setString(2, calendarioAtividadeMatriculaVO.getHistoricoModificacao());
					sqlAlterar.setTimestamp(3, Uteis.getDataJDBCTimestamp(calendarioAtividadeMatriculaVO.getDataCadastro()));
					if (calendarioAtividadeMatriculaVO.getResponsavelCadastro().getCodigo() != 0) {
						sqlAlterar.setInt(4, calendarioAtividadeMatriculaVO.getResponsavelCadastro().getCodigo());
					} else {
						sqlAlterar.setNull(4, 0);
					}
					if(calendarioAtividadeMatriculaVO.getDataInicio() != null) {
						sqlAlterar.setTimestamp(5, Uteis.getDataJDBCTimestamp(calendarioAtividadeMatriculaVO.getDataInicio()));						
					}else {
						sqlAlterar.setNull(5, 0);
					}
					if(calendarioAtividadeMatriculaVO.getDataFim() != null) {
						sqlAlterar.setTimestamp(6, Uteis.getDataJDBCTimestamp(calendarioAtividadeMatriculaVO.getDataFim()));
					}else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setString(7, calendarioAtividadeMatriculaVO.getTipoCalendarioAtividade().name());
					sqlAlterar.setString(8, calendarioAtividadeMatriculaVO.getTipoOrigem().name());
					if(Uteis.isAtributoPreenchido(calendarioAtividadeMatriculaVO.getCodOrigem())){
						sqlAlterar.setString(9, calendarioAtividadeMatriculaVO.getCodOrigem());	
					}else{
						sqlAlterar.setNull(9, 0);
					}
					sqlAlterar.setString(10, calendarioAtividadeMatriculaVO.getMatriculaVO().getMatricula());					
					if (Uteis.isAtributoPreenchido(calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO())) {
						sqlAlterar.setInt(11, calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());						
					} else {
						sqlAlterar.setNull(11, 0);
					}
					sqlAlterar.setTimestamp(12, Uteis.getDataJDBCTimestamp(calendarioAtividadeMatriculaVO.getDateRealizado()));
					sqlAlterar.setInt(13, calendarioAtividadeMatriculaVO.getNrVezesProrrogado());
					sqlAlterar.setString(14, calendarioAtividadeMatriculaVO.getSituacaoAtividade().name());
					if (!calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().getCodigo().equals(0)) {
						sqlAlterar.setInt(15, calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().getCodigo());
					} else {
						sqlAlterar.setNull(15, 0);
					}
					if (calendarioAtividadeMatriculaVO.getMatriculaPeriodoVO().getCodigo() != 0) {
						sqlAlterar.setInt(16, calendarioAtividadeMatriculaVO.getMatriculaPeriodoVO().getCodigo());
					} else {
						sqlAlterar.setNull(16, 0);
					}
					sqlAlterar.setInt(17, calendarioAtividadeMatriculaVO.getCodigo());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
//			CalendarioAtividadeMatricula.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM calendarioatividadematricula WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, calendarioAtividadeMatriculaVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirCalendarioTipoAvaliacaoOnlineReaPorConteudoUnidadePaginaRecursoEducacional(final ConteudoUnidadePaginaRecursoEducacionalVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		excluir(getIdEntidade(), verificarAcesso, usuarioVO);
		String sqlStr = "SELECT count(codigo) as qtd FROM calendarioatividadematricula WHERE situacaoatividade ='"+SituacaoAtividadeEnum.CONCLUIDA+ "' and tipoorigem = '"+TipoOrigemEnum.REA+ "' and codorigem = '" +obj.getCodigo()+"' " ;
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next() && rs.getInt("qtd") >0) {
			throw new Exception("Não é possivel excluir este Rea de Avaliação Online, pois já existe avaliações respondidas.");
		}
		String sql = "DELETE FROM calendarioatividadematricula WHERE tipoorigem = '"+TipoOrigemEnum.REA+ "' and codorigem = '" +obj.getCodigo()+"' " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().execute(sql);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void exluirPorAvaliacaoOnlineTipoUsoTurma(String codOrigem,  boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		excluir(getIdEntidade(), verificarAcesso, usuarioVO);
		StringBuilder sb = new StringBuilder("");
		sb.append("DELETE FROM calendarioatividadematricula where codigo in (");
		sb.append(" select calendarioatividadematricula.codigo from calendarioatividadematricula ");
		sb.append(" left join avaliacaoonlinematricula  on avaliacaoonlinematricula.codigo  = calendarioatividadematricula.avaliacaoonlinematricula  ");		
		sb.append(" where  ");
		sb.append(" tipocalendarioatividade = '").append(TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE.name()).append("' ");
		sb.append(" and tipoorigem = '").append(TipoOrigemEnum.AVALIACAO_ONLINE_TURMA.name()).append("' ");
		sb.append(" and codorigem = '").append(codOrigem).append("' ");
		sb.append(" and ((calendarioatividadematricula.avaliacaoonlinematricula is null and situacaoatividade = '").append(SituacaoAtividadeEnum.NAO_CONCLUIDA.name()).append("') ");
		sb.append(" or (calendarioatividadematricula.avaliacaoonlinematricula is not null and avaliacaoonlinematricula.situacaoavaliacaoonlinematricula = '").append(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO.name()).append("')) ");
		sb.append(" )");
		sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().update(sb.toString());
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCalendarioAtividadeMatriculaAndExlcuirAvaliacaoOnlineMatricula(List<CalendarioAtividadeMatriculaVO> listaCalendario, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		excluir("GestaoAvaliacaoOnline", verificarAcesso, usuarioVO);
		StringBuilder sb = new StringBuilder(" UPDATE calendarioatividadematricula set ");
		sb.append(" situacaoatividade = '").append(SituacaoAtividadeEnum.NAO_CONCLUIDA).append("', ");
		sb.append(" datarealizado = null, ");
		sb.append(" avaliacaoonlinematricula=null ");
		sb.append(" WHERE codigo in (  ").append(UteisTexto.converteListaEntidadeCampoCodigoParaString(listaCalendario)).append(" ) ");
		sb.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
		getConexao().getJdbcTemplate().execute(sb.toString());
		getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().excluirPorCalendarioAtividadeMatricula(listaCalendario, usuarioVO);
	}

	@Override
	public CalendarioAtividadeMatriculaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		CalendarioAtividadeMatriculaVO obj = new CalendarioAtividadeMatriculaVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.setDescricao(tabelaResultado.getString("descricao"));
		obj.setDataInicio(tabelaResultado.getTimestamp("datainicio"));
		obj.setDataFim(tabelaResultado.getTimestamp("datafim"));
		obj.setCodOrigem(tabelaResultado.getString("codorigem"));
		obj.setTipoCalendarioAtividade(TipoCalendarioAtividadeMatriculaEnum.valueOf(tabelaResultado.getString("tipocalendarioatividade")));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("tipoorigem"))) {
			obj.setTipoOrigem(TipoOrigemEnum.valueOf(tabelaResultado.getString("tipoorigem")));
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		obj.setHistoricoModificacao(tabelaResultado.getString("historicomodificacao"));
		obj.setDataCadastro(tabelaResultado.getDate("datacadastro"));
		obj.getResponsavelCadastro().setCodigo(tabelaResultado.getInt("responsavelcadastro"));
		obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
		obj.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(tabelaResultado.getInt("matriculaperiodoturmadisciplina"));
		obj.setDateRealizado(tabelaResultado.getTimestamp("datarealizado"));
		obj.setNrVezesProrrogado(tabelaResultado.getInt("nrvezesperiodoprorrogado"));
		obj.setSituacaoAtividade(SituacaoAtividadeEnum.valueOf(tabelaResultado.getString("situacaoatividade")));
		obj.getAvaliacaoOnlineMatriculaVO().setCodigo(tabelaResultado.getInt("avaliacaoonlinematricula"));
		if(obj.getTipoOrigem() != null && obj.getTipoOrigem().equals(TipoOrigemEnum.REA) && Uteis.isAtributoPreenchido(tabelaResultado.getString("codorigem")) && Uteis.getIsValorNumerico(tabelaResultado.getString("codorigem"))) {
			obj.getConteudoUnidadePaginaRecursoEducacionalVO().setCodigo(Integer.valueOf(tabelaResultado.getString("codorigem")));			
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setAvaliacaoOnlineMatriculaVO(getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().consultarPorChavePrimaria(obj.getAvaliacaoOnlineMatriculaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			obj.setMatriculaPeriodoTurmaDisciplinaVO(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorChavePrimaria(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
		}

		return obj;
	}

	@Override
	public List<CalendarioAtividadeMatriculaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<CalendarioAtividadeMatriculaVO> vetResultado = new ArrayList<CalendarioAtividadeMatriculaVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}

		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CalendarioAtividadeMatriculaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM calendarioatividadematricula WHERE codigo = ?";
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql, codigo);
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new CalendarioAtividadeMatriculaVO();
	}

	@Override
	public List<CalendarioAtividadeMatriculaVO> consultarCalendarioAtividadeMatricula(String matricula, Integer curso, TurmaVO turmaVO, Integer disciplina, String ano, String semestre, Boolean realizado, Date datainicio, Date datatermino, String situacaoAtividade, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividade, String tipoFiltroPeriodo, String tipoFiltroConsulta, String tituloRea, Integer codigoAtividadeDiscursiva, UsuarioVO usuario) throws Exception {
		List<Object> parametros = new ArrayList<>();
		validarDados(matricula, curso, turmaVO, disciplina, tipoFiltroConsulta);
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select  calendarioatividadematricula.codigo as codigocalendarioatividadematricula, matricula.matricula as codigomatricula, pessoa.codigo as codigoaluno, curso.codigo as codigocurso, disciplina.codigo as codigodisciplina, matriculaperiodoturmadisciplina.codigo as codigomatriculaperiodoturmadisciplina, turma.codigo as codigoturma,");
		sqlStr.append(" calendarioatividadematricula.matricula, pessoa.nome as aluno, calendarioatividadematricula.datainicio, calendarioatividadematricula.datafim, tipoorigem, nrvezesperiodoprorrogado, ");
		sqlStr.append(" turma.identificadorturma as turma,disciplina.nome as disciplina, calendarioatividadematricula.tipocalendarioatividade, situacaoatividade, conteudoUnidadePaginaRecursoEducacional.titulo as cupre_titulo , calendarioatividadematricula.codOrigem as codOrigem , avaliacaoonlinematricula.codigo as avaliacaoonlinematricula, avaliacaoonlinematricula.situacaoavaliacaoonlinematricula as situacaoavaliacaoonlinematricula ");
		sqlStr.append(" from calendarioatividadematricula");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on calendarioatividadematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sqlStr.append(" inner join historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		if (Uteis.isAtributoPreenchido(turmaVO) && turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" inner join turma on ");
			sqlStr.append(" ((MatriculaPeriodoTurmaDisciplina.turma in (select turma from turmaAgrupada where turmaOrigem =  ").append(turmaVO.getCodigo()).append(") and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null )");
			sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaPratica is not null and MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
			sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaTeorica is not null and MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			sqlStr.append(") ");
			sqlStr.append(" inner join disciplina on (matriculaperiodoturmadisciplina.disciplina = disciplina.codigo or (matriculaperiodoturmadisciplina.disciplina IN (");
			sqlStr.append(" select disciplinaequivalente.equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = matriculaperiodoturmadisciplina.disciplina ");
			sqlStr.append(" union select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = matriculaperiodoturmadisciplina.disciplina ");
			sqlStr.append(" union select disciplinaequivalenteturmaagrupada from turmadisciplina where disciplinaequivalenteturmaagrupada is not null and turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina and turmadisciplina.turma = ").append(turmaVO.getCodigo()).append(" ))) ");
		} else {
			sqlStr.append(" inner join turma on matriculaperiodoturmadisciplina.turma = turma.codigo ");
			sqlStr.append(" inner join disciplina on matriculaperiodoturmadisciplina.disciplina = disciplina.codigo ");
		}
		sqlStr.append(" inner join matricula on calendarioatividadematricula.matricula = matricula.matricula");
		sqlStr.append(" inner join matriculaperiodo on historico.matriculaperiodo = matriculaperiodo.codigo");
		sqlStr.append(" inner join curso on matricula.curso = curso.codigo");
		sqlStr.append(" inner join pessoa on matricula.aluno = pessoa.codigo");
		sqlStr.append(" left join conteudoUnidadePaginaRecursoEducacional on calendarioatividadematricula.tipoorigem = '").append(TipoOrigemEnum.REA.name()).append("' and  calendarioatividadematricula.codorigem = conteudoUnidadePaginaRecursoEducacional.codigo::varchar");
		sqlStr.append(" left join atividadediscursiva on calendarioatividadematricula.tipoorigem = '").append(TipoOrigemEnum.ATIVIDADE_DISCURSIVA.name()).append("' and calendarioatividadematricula.codorigem::int = atividadediscursiva.codigo ");
		sqlStr.append(" left join avaliacaoonlinematricula on calendarioatividadematricula.avaliacaoonlinematricula = avaliacaoonlinematricula.codigo ");
		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append(" where calendarioatividadematricula.matricula = '").append(matricula).append("'");
		} else if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" where turma.curso =").append(curso);
		}		
		if (Uteis.isAtributoPreenchido(turmaVO)) {
			sqlStr.append(" where turma.codigo = ").append(turmaVO.getCodigo()).append("");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and disciplina.codigo = ").append(disciplina);
		}
		
		sqlStr.append(" and ((turma.anual and matriculaperiodoturmadisciplina.ano = '").append(ano).append("' )");
		sqlStr.append(" or (turma.semestral and matriculaperiodoturmadisciplina.ano = '").append(ano).append("' ");
		sqlStr.append(" and matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("' ) ");
		sqlStr.append(" or (turma.semestral = false and turma.anual = false )) ");
		if (Uteis.isAtributoPreenchido(situacaoAtividade)) {
			sqlStr.append(" and calendarioatividadematricula.situacaoatividade = '").append(situacaoAtividade).append("'");
		}
		if (Uteis.isAtributoPreenchido(tipoCalendarioAtividade)) {
			sqlStr.append(" and calendarioatividadematricula.tipocalendarioatividade = '").append(tipoCalendarioAtividade).append("'");
			if(tipoCalendarioAtividade.isPeriodoRealizacaoAvaliacaoOnlineRea() && Uteis.isAtributoPreenchido(tituloRea)) {
				sqlStr.append(" and sem_acentos(conteudoUnidadePaginaRecursoEducacional.titulo) ilike sem_acentos(?)");
				parametros.add(tituloRea + PERCENT);
			}
			if (tipoCalendarioAtividade.isPeriodoMaximoAtividadeDiscursiva() && Uteis.isAtributoPreenchido(codigoAtividadeDiscursiva)) {
				sqlStr.append(" and atividadediscursiva.codigo = ? ");
				parametros.add(codigoAtividadeDiscursiva);
			}
		}
		if (!tipoFiltroPeriodo.equals("todos")) {
			if (tipoFiltroPeriodo.equals("comecaNoPeriodo")) {
				sqlStr.append(" and calendarioatividadematricula.datainicio between '").append(Uteis.getDataJDBC(datainicio)).append("' and '").append(Uteis.getDataJDBC(datatermino)).append("'");
			}
			if (tipoFiltroPeriodo.equals("terminaNoPeriodo")) {
				sqlStr.append(" and calendarioatividadematricula.datafim between '").append(Uteis.getDataJDBC(datainicio)).append("' and '").append(Uteis.getDataJDBC(datatermino)).append("'");
			}
			if (tipoFiltroPeriodo.equals("comecaOuTerminaNoPeriodo")) {
				sqlStr.append(" and calendarioatividadematricula.datainicio >= '").append(Uteis.getDataJDBC(datainicio)).append("'");
				sqlStr.append(" and calendarioatividadematricula.datafim <= '").append(Uteis.getDataJDBC(datatermino)).append("'");
			}
		}
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		sqlStr.append(" order by pessoa.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), parametros.toArray());
		List<CalendarioAtividadeMatriculaVO> lista = new ArrayList<CalendarioAtividadeMatriculaVO>();
		CalendarioAtividadeMatriculaVO obj = new CalendarioAtividadeMatriculaVO();
		while (rs.next()) {

			obj.setCodigo(rs.getInt("codigocalendarioatividadematricula"));
			obj.getMatriculaVO().setMatricula(rs.getString("codigomatricula"));
			obj.getMatriculaVO().getAluno().setCodigo(rs.getInt("codigoaluno"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().setCodigo(rs.getInt("codigodisciplina"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(rs.getInt("codigomatriculaperiodoturmadisciplina"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setCodigo(rs.getInt("codigoturma"));
			obj.getResponsavelCadastro().setCodigo(usuario.getCodigo());
			obj.getMatriculaVO().getAluno().setNome(rs.getString("aluno"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setIdentificadorTurma(rs.getString("turma"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().setNome(rs.getString("disciplina"));
			obj.setTipoCalendarioAtividade(TipoCalendarioAtividadeMatriculaEnum.valueOf(rs.getString("tipocalendarioatividade")));
			obj.getConteudoUnidadePaginaRecursoEducacionalVO().setTitulo(rs.getString("cupre_titulo"));
			obj.setSituacaoAtividade(SituacaoAtividadeEnum.valueOf(rs.getString("situacaoatividade")));
			obj.setDataInicio(Uteis.getDataJDBCTimestamp(rs.getTimestamp("datainicio")));
			obj.setDataFim(Uteis.getDataJDBCTimestamp(rs.getTimestamp("datafim")));
			obj.setTipoOrigem(TipoOrigemEnum.valueOf(rs.getString("tipoorigem")));
			obj.setNrVezesProrrogado(rs.getInt("nrvezesperiodoprorrogado"));
			obj.setCodOrigem(rs.getString("codOrigem"));
			obj.getAvaliacaoOnlineMatriculaVO().setCodigo(rs.getInt("avaliacaoOnlineMatricula"));
			if (rs.getString("situacaoavaliacaoonlinematricula") != null) {
				obj.getAvaliacaoOnlineMatriculaVO().setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.valueOf(rs.getString("situacaoavaliacaoonlinematricula")));
			}
			lista.add(obj);

			obj = new CalendarioAtividadeMatriculaVO();
		}
		return lista;

	}

	private void validarDados(String matricula, Integer curso, TurmaVO turmaVO, Integer disciplina,	String tipoFiltroConsulta) throws Exception {
		if (tipoFiltroConsulta.equals("matricula")) {
			if (matricula.equals("")) {
				throw new Exception("Informe uma Matrícula");
			}
		} else if (tipoFiltroConsulta.equals("curso")) {
			if (curso == 0) {
				throw new Exception("Informe um Curso");
			}
		} else if (tipoFiltroConsulta.equals("turma")) {
			if (!Uteis.isAtributoPreenchido(turmaVO)) {
				throw new Exception("Informe uma Turma");
			}
		} 
		if (!Uteis.isAtributoPreenchido(disciplina)) {
			throw new Exception("Informe uma Disciplina");
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public List<CalendarioAtividadeMatriculaVO> consultarCalendarioAtividadeMatriculaVisaoAluno(Integer codigoMatriculaPeriodoTurmaDisciplina, UsuarioVO usuario) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select distinct  avaliacaoonlinematricula.codigo as codigoavaliacaoonlinematricula, avaliacaoonlinematricula.situacaoavaliacaoonlinematricula, ");
		sqlStr.append(" matriculaperiodoturmadisciplina.codigo as codigomatriculaperiodoturmadisciplina, calendarioatividadematricula.codigo as codigocalendarioatividadematricula, ");
		sqlStr.append(" calendarioatividadematricula.datainicio, calendarioatividadematricula.datafim, calendarioatividadematricula.responsavelcadastro, calendarioatividadematricula.matricula as codigomatricula, ");
		sqlStr.append(" calendarioatividadematricula.tipocalendarioatividade, calendarioatividadematricula.situacaoatividade as calendarioatividadematricula_situacao, calendarioatividadematricula.nrvezesperiodoprorrogado, ");
		sqlStr.append(" calendarioatividadematricula.tipoorigem as \"calendarioatividadematricula.tipoorigem\", calendarioatividadematricula.codorigem as \"calendarioatividadematricula.codorigem\",  ");
		sqlStr.append(" calendarioatividadematricula.descricao as \"calendarioatividadematricula.descricao\",  ");
		sqlStr.append(" avaliacaoonlinematricula.datatermino, avaliacaoonlinematricula.nota, avaliacaoonlinematricula.avaliacaoonline, disciplina.nome as nomedisciplina, ");
		sqlStr.append(" conteudounidadepaginarecursoeducacional.codigo as conteudounidadepaginarecursoeducacional_codigo,  conteudounidadepaginarecursoeducacional.titulo as conteudounidadepaginarecursoeducacional_titulo,  ");
		sqlStr.append(" conteudoUnidadePagina.codigo as conteudoUnidadePagina_codigo, conteudoUnidadePagina.titulo as conteudoUnidadePagina_titulo, conteudoUnidadePagina.pagina as conteudoUnidadePagina_pagina, ");
		sqlStr.append(" unidadeConteudo.ordem as unidadeConteudo_ordem, conteudounidadepaginarecursoeducacional.avaliacaoOnline as conteudounidadepaginarecursoeducacional_avaliacaoOnline  ");
		sqlStr.append(" from calendarioatividadematricula ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = calendarioatividadematricula.matriculaperiodoturmadisciplina ");
		sqlStr.append(" inner join disciplina on disciplina.codigo  = matriculaperiodoturmadisciplina.disciplina ");
		sqlStr.append(" left join avaliacaoonlinematricula on avaliacaoonlinematricula.codigo = calendarioatividadematricula.avaliacaoonlinematricula ");
		sqlStr.append(" left join conteudounidadepaginarecursoeducacional on conteudounidadepaginarecursoeducacional.codigo = calendarioatividadematricula.codorigem::numeric and calendarioatividadematricula.tipoorigem = 'REA' ");
		sqlStr.append(" left join conteudoUnidadePagina on conteudoUnidadePagina.codigo = conteudounidadepaginarecursoeducacional.conteudoUnidadePagina ");		
		sqlStr.append(" left join unidadeConteudo on unidadeConteudo.codigo  = conteudoUnidadePagina.unidadeConteudo ");
		sqlStr.append(" where matriculaperiodoturmadisciplina.codigo = ").append(codigoMatriculaPeriodoTurmaDisciplina);
		sqlStr.append(" and calendarioatividadematricula.tipocalendarioatividade in ('PERIODO_REALIZACAO_AVALIACAO_ONLINE', 'PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA' )");
		sqlStr.append(" order by calendarioatividadematricula.tipocalendarioatividade, unidadeConteudo.ordem,  conteudoUnidadePagina.pagina ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<CalendarioAtividadeMatriculaVO> lista = new ArrayList<CalendarioAtividadeMatriculaVO>();
		CalendarioAtividadeMatriculaVO obj = new CalendarioAtividadeMatriculaVO();

		while (rs.next()) {
			obj.setCodigo(rs.getInt("codigocalendarioatividadematricula"));
			obj.setDescricao(rs.getString("calendarioatividadematricula.descricao"));
			obj.setTipoCalendarioAtividade(TipoCalendarioAtividadeMatriculaEnum.valueOf(rs.getString("tipocalendarioatividade")));
			obj.setSituacaoAtividade(SituacaoAtividadeEnum.valueOf(rs.getString("calendarioatividadematricula_situacao")));
			obj.setNrVezesProrrogado(rs.getInt("nrvezesperiodoprorrogado"));
			if(Uteis.isAtributoPreenchido(rs.getString("calendarioatividadematricula.tipoorigem"))) {
				obj.setCodOrigem(rs.getString("calendarioatividadematricula.codorigem"));
				obj.setTipoOrigem(TipoOrigemEnum.valueOf(rs.getString("calendarioatividadematricula.tipoorigem")));	
			}
			obj.getAvaliacaoOnlineMatriculaVO().setCodigo(rs.getInt("codigoavaliacaoonlinematricula"));
			if (Uteis.isAtributoPreenchido(rs.getString("situacaoavaliacaoonlinematricula"))) {
				obj.getAvaliacaoOnlineMatriculaVO().setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.valueOf(rs.getString("situacaoavaliacaoonlinematricula")));
			}
			obj.getAvaliacaoOnlineMatriculaVO().setNota(rs.getDouble("nota"));
			obj.getMatriculaVO().setMatricula(rs.getString("codigomatricula"));
			obj.getAvaliacaoOnlineMatriculaVO().setDataTermino(rs.getTimestamp("datatermino"));
			obj.getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(rs.getInt("codigomatriculaperiodoturmadisciplina"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(rs.getInt("codigomatriculaperiodoturmadisciplina"));
			obj.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().setCodigo(rs.getInt("avaliacaoonline"));
			obj.setDataInicio(rs.getTimestamp("datainicio"));
			obj.setDataFim(rs.getTimestamp("datafim"));
			obj.getResponsavelCadastro().setCodigo(rs.getInt("responsavelcadastro"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().setNome(rs.getString("nomedisciplina"));
			if (!obj.getAvaliacaoOnlineMatriculaVO().getCodigo().equals(0)) {
				List<GraficoAproveitamentoAvaliacaoVO> graficoAproveitamentoAvaliacaoVOs = new ArrayList<GraficoAproveitamentoAvaliacaoVO>();
				graficoAproveitamentoAvaliacaoVOs = getFacadeFactory().getGraficoAproveitamentoAvaliacaoFacade().consultarAproveitamentoAvaliacaoOnlineAluno(obj.getAvaliacaoOnlineMatriculaVO().getCodigo());
				obj.getAvaliacaoOnlineMatriculaVO().setGraficoAproveitamentoAvaliacaoVOs(getFacadeFactory().getGraficoAproveitamentoAvaliacaoFacade().realizarParametrosGraficoAvaliacaoOnlineMatricula(graficoAproveitamentoAvaliacaoVOs, obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getNome(), 250));
			}
			if(obj.getTipoCalendarioAtividade().isPeriodoRealizacaoAvaliacaoOnlineRea()){
				obj.getConteudoUnidadePaginaRecursoEducacionalVO().setCodigo(rs.getInt("conteudounidadepaginarecursoeducacional_codigo"));
				obj.getConteudoUnidadePaginaRecursoEducacionalVO().setTitulo(rs.getString("conteudounidadepaginarecursoeducacional_titulo"));
				obj.getConteudoUnidadePaginaRecursoEducacionalVO().getAvaliacaoOnlineVO().setCodigo(rs.getInt("conteudounidadepaginarecursoeducacional_avaliacaoOnline"));
				obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().setCodigo(rs.getInt("conteudoUnidadePagina_codigo"));
				obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().setPagina(rs.getInt("conteudoUnidadePagina_pagina"));
				obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().setTitulo(rs.getString("conteudoUnidadePagina_titulo"));
				obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().getUnidadeConteudo().setOrdem(rs.getInt("unidadeconteudo_ordem"));
			}
			realizarDefinicaoDataInicioTerminoCalendarioAtividadeMatriculaAvaliacaoOnline(obj, usuario);
			if (obj.getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO) && UteisData.validarDataInicialMaiorFinalComHora(new Date() , obj.getDataFim())) {
				obj.getAvaliacaoOnlineMatriculaVO().setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.EXPIRADO);	
			}
			lista.add(obj);
			obj = new CalendarioAtividadeMatriculaVO();
		}
		return lista;
	}

	/**
	 * 
	 * 
	 * @see negocio.interfaces.ead.CalendarioAtividadeMatriculaInterfaceFacade#
	 *      realizarCriacaoCalendarioAtividadeMatriculaAvaliacaoOnline
	 *      (negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO,
	 *      negocio.comuns.ead.ConfiguracaoEADVO)
	 * 
	 *      Método responsável por criar um novo calandário caso o
	 *      NrVezesPodeRepetirAvaliacaoOnline seja maior que 0 e o mesmo não
	 *      tenha estourado o limite para realizar uma nova Avaliação On-line
	 *      feito atraves de uma consulta no qual seu retorno é a quantidade de
	 *      calendários daquela MatriculaPeriodoTurmaDisciplina.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarCriacaoCalendarioAtividadeMatriculaAvaliacaoOnline(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO,  CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuarioVO) throws Exception {
		if (!(avaliacaoOnlineMatriculaVO.getConfiguracaoEADVO().getTipoControleLiberacaoAvaliacaoOnline().equals(TipoControleLiberacaoAvaliacaoOnlineEnum.NAO_APLICAR_AVALIACAO_ONLINE)) || avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoUso().isRea() || avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoUso().isTurma()) {
			Integer retornoConsulta = consultarQuantidadeCalendariosPeriodoRealizacaoAvaliacaoOnlineMatriculaPeriodoTurmaDisciplina(avaliacaoOnlineMatriculaVO,calendarioAtividadeMatriculaVO);
			if(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoUso().isRea() || avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoUso().isTurma()) {
				if (retornoConsulta > avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getNrVezesPodeRepetirAvaliacaoOnline()) {
					return;
				}
			}else {
				if (retornoConsulta >= avaliacaoOnlineMatriculaVO.getConfiguracaoEADVO().getNrVezesPodeRepetirAvaliacaoOnline()) {
					return;
				}
			}
			CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO2 = new CalendarioAtividadeMatriculaVO();
			if(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoUso().isRea()) {				
				calendarioAtividadeMatriculaVO2.setCodOrigem(String.valueOf(calendarioAtividadeMatriculaVO.getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo()));
			}else if(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoUso().isTurma()) {
				calendarioAtividadeMatriculaVO2.setCodOrigem(String.valueOf( avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getCodigo()));

			}
			calendarioAtividadeMatriculaVO2.setTipoOrigem(calendarioAtividadeMatriculaVO.getTipoOrigem());
			calendarioAtividadeMatriculaVO2.setDataCadastro(new Date());
			if(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoUso().isRea()) {
				calendarioAtividadeMatriculaVO2.setDescricao(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getNome()  +" - " +(retornoConsulta+1)+  " - PERÍODO REALIZAÇÃO AVALIAÇÃO ON-LINE REA - CÓD "+ calendarioAtividadeMatriculaVO.getCodOrigem());
			}else if(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoUso().isTurma()) {
				calendarioAtividadeMatriculaVO2.setDescricao(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getNome()  +" - " +(retornoConsulta+1)+  " - PERÍODO REALIZAÇÃO AVALIAÇÃO ON-LINE TURMA - CÓD "+ calendarioAtividadeMatriculaVO.getCodOrigem());
			}else {			
			   calendarioAtividadeMatriculaVO2.setDescricao(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getNome()  +" - " +(retornoConsulta+1)+ " - PERÍODO REALIZAÇÃO AVALIAÇÃO ON-LINE ");
			}
			calendarioAtividadeMatriculaVO2.setResponsavelCadastro(usuarioVO);
			if(calendarioAtividadeMatriculaVO.getTipoOrigem().equals(TipoOrigemEnum.REA)) {
				calendarioAtividadeMatriculaVO2.setTipoCalendarioAtividade(TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA);
			}else {
		    	calendarioAtividadeMatriculaVO2.setTipoCalendarioAtividade(TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE);
			}
			int valordias = 0 ;
			if(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoUso().equals(TipoUsoEnum.REA) || avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoUso().equals(TipoUsoEnum.TURMA)) {
				valordias = avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getNrDiasEntreAvalicaoOnline();
			}else {
				valordias =avaliacaoOnlineMatriculaVO.getConfiguracaoEADVO().getNrDiasEntreAvalicaoOnline(); 
			}
			calendarioAtividadeMatriculaVO2.setDataInicio(UteisData.obterDataFuturaUsandoCalendar(new Date(),valordias ));
			if (calendarioAtividadeMatriculaVO.getDataFim().compareTo(calendarioAtividadeMatriculaVO2.getDataInicio()) < 0) {
				calendarioAtividadeMatriculaVO2.setDataFim(calendarioAtividadeMatriculaVO2.getDataInicio());
			} else {
				calendarioAtividadeMatriculaVO2.setDataFim(calendarioAtividadeMatriculaVO.getDataFim());
			}
			calendarioAtividadeMatriculaVO2.setMatriculaVO(calendarioAtividadeMatriculaVO.getMatriculaVO());
			calendarioAtividadeMatriculaVO2.setMatriculaPeriodoTurmaDisciplinaVO(avaliacaoOnlineMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO());
			calendarioAtividadeMatriculaVO2.setNrVezesProrrogado(0);
			calendarioAtividadeMatriculaVO2.setSituacaoAtividade(SituacaoAtividadeEnum.NAO_CONCLUIDA);
//			getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().incluir(calendarioAtividadeMatriculaVO2, false, usuarioVO);
		}
	}

	public Integer consultarQuantidadeCalendariosPeriodoRealizacaoAvaliacaoOnlineMatriculaPeriodoTurmaDisciplina(AvaliacaoOnlineMatriculaVO avaliacaoOnlineMatriculaVO ,CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO) {
		StringBuilder sqlStr = new StringBuilder("");

		sqlStr.append("	select count (calendarioatividadematricula.codigo) as qtdr from calendarioatividadematricula ");
		sqlStr.append(" inner join matriculaPeriodoTurmaDisciplina on matriculaPeriodoTurmaDisciplina.codigo = calendarioatividadematricula.matriculaPeriodoTurmaDisciplina");
		sqlStr.append(" where matriculaPeriodoTurmaDisciplina.codigo = ").append(avaliacaoOnlineMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
		if(avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoUso().isRea() ) {
			sqlStr.append(" and calendarioatividadematricula.tipoOrigem = 'REA'");
			sqlStr.append(" and calendarioatividadematricula.codOrigem ='"+calendarioAtividadeMatriculaVO.getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo()+"'");
		}else if( avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getTipoUso().isTurma()){
			sqlStr.append(" and calendarioatividadematricula.tipoOrigem = 'AVALIACAO_ONLINE_TURMA'");
			sqlStr.append(" and calendarioatividadematricula.codOrigem ='"+avaliacaoOnlineMatriculaVO.getAvaliacaoOnlineVO().getCodigo()+"'");
		}else {		
		    sqlStr.append(" and calendarioatividadematricula.tipocalendarioatividade = 'PERIODO_REALIZACAO_AVALIACAO_ONLINE'");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		Integer count = 0;

		if (rs.next()) {
			count = rs.getInt("qtdr");
		}

		return count;
	}

	@Override
	public Integer consultarQuantidadeCalendariosPeriodoRealizacaoAvaliacaoOnlineNaoConcluidosMatriculaPeriodoTurmaDisciplina(Integer codigoMatriculaPeriodoTurmaDisciplinaVO) {
		StringBuilder sqlStr = new StringBuilder("");

		sqlStr.append("	select count (calendarioatividadematricula.codigo) as qtdr from calendarioatividadematricula ");
		sqlStr.append(" inner join matriculaPeriodoTurmaDisciplina on matriculaPeriodoTurmaDisciplina.codigo = calendarioatividadematricula.matriculaPeriodoTurmaDisciplina");
		sqlStr.append(" where matriculaPeriodoTurmaDisciplina.codigo = ").append(codigoMatriculaPeriodoTurmaDisciplinaVO);
		sqlStr.append(" and calendarioatividadematricula.tipocalendarioatividade = 'PERIODO_REALIZACAO_AVALIACAO_ONLINE'");
		sqlStr.append(" and calendarioatividadematricula.situacaoatividade = 'NAO_CONCLUIDA'");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		Integer count = 0;

		if (rs.next()) {
			count = rs.getInt("qtdr");
		}

		return count;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.interfaces.ead.CalendarioAtividadeMatriculaInterfaceFacade#
	 * consultarCalendarioAtividadeMatriculasOndeSituacaoAvaliacaoOnlineMatriculaEmRealizacao
	 * ()
	 * 
	 * Método responsável por consultar todas as avaliações on-lines que estão
	 * com a situação EM_REALIZACAO e excecutar uma thread para cada uma
	 * verificando se a mesma já expirou o prazo para que o aluno possa
	 * continua-la e caso tenha expirado, a thread ficará responsável por
	 * finaliza-la e gerar o resultado.
	 */
	@Override
	public void consultarCalendarioAtividadeMatriculasOndeSituacaoAvaliacaoOnlineMatriculaEmRealizacao() throws Exception {

		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select calendarioatividadematricula.codigo, avaliacaoonlinematricula.codigo as codigoavaliacaoonlinematricula, avaliacaoonlinematricula.datalimitetermino from calendarioatividadematricula");
		sqlStr.append(" inner join avaliacaoonlinematricula on avaliacaoonlinematricula.codigo = calendarioatividadematricula.avaliacaoonlinematricula");
		sqlStr.append(" where avaliacaoonlinematricula.situacaoavaliacaoonlinematricula like 'EM_REALIZACAO' ");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = null;
		while (rs.next()) {
			calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
			calendarioAtividadeMatriculaVO.setCodigo(rs.getInt("codigo"));
			calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().setCodigo(rs.getInt("codigoavaliacaoonlinematricula"));
			calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().setDataLimiteTermino(rs.getTimestamp("datalimitetermino"));
			Thread threadAvalOn = new Thread(new JobAvaliacaoOnline(calendarioAtividadeMatriculaVO.getCodigo(), calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO().getDataLimiteTermino().getTime()));
			threadAvalOn.start();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.interfaces.ead.CalendarioAtividadeMatriculaInterfaceFacade#
	 * consultarCalendarioAtividadeMatriculaTelaGestaoAvaliacaoOnline
	 * (java.lang.String, java.lang.Integer, java.lang.String,
	 * java.lang.Integer, java.lang.String, java.lang.String, java.util.Date,
	 * java.util.Date, java.lang.String, negocio.comuns.arquitetura.UsuarioVO)
	 * 
	 * Método responsável por realizar uma consulta filtrando de acordo com o
	 * especificado pelo usuário na tela gestaoAvaliacaoOnlineCons.jsp
	 */
	@Override
	public List<CalendarioAtividadeMatriculaVO> consultarCalendarioAtividadeMatriculaTelaGestaoAvaliacaoOnline(String matricula, Integer unidadeEnsino,  Integer curso, Integer turma, Integer disciplina, String ano, String semestre, 
			Date dataInicio, Date dataFim, String situacaoAvaliacaoOnline, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatriculaEnum, String tituloRea, UsuarioVO usuario) throws Exception {
		List<Object> parametros = new ArrayList<>();
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" select distinct calendarioatividadematricula.codigo as codigocalendarioatividadematricula, matriculaperiodo.situacao as situacaoacademico, ");
		sqlStr.append(" matricula.matricula as matriculaaluno, pessoa.nome as nomealuno, pessoa.codigo as codigopessoa, disciplina.codigo as disciplinacodigo, disciplina.nome as nomedisciplina, ");
		sqlStr.append("	avaliacaoonlinematricula.situacaoavaliacaoonlinematricula, matriculaperiodoturmadisciplina.codigo as codigomatriculaperiodoturmadisciplina, ");
		sqlStr.append("	avaliacaoonlinematricula.nota, avaliacaoonlinematricula.datainicio, avaliacaoonlinematricula.codigo as codigoavaliacaoonlinematricula,  ");
		sqlStr.append(" turma.codigo as codigoturma, turma.identificadorturma, calendarioatividadematricula.tipocalendarioatividade, calendarioatividadematricula.descricao as \"calendarioatividadematricula.descricao\", ");
		sqlStr.append(" conteudounidadepaginarecursoeducacional.codigo as conteudounidadepaginarecursoeducacional_codigo,  conteudounidadepaginarecursoeducacional.titulo as conteudounidadepaginarecursoeducacional_titulo,  ");
		sqlStr.append(" conteudoUnidadePagina.codigo as conteudoUnidadePagina_codigo, conteudoUnidadePagina.titulo as conteudoUnidadePagina_titulo, conteudoUnidadePagina.pagina as conteudoUnidadePagina_pagina, ");
		sqlStr.append(" unidadeConteudo.ordem as unidadeConteudo_ordem  ");
		sqlStr.append(" from calendarioatividadematricula ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on calendarioatividadematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sqlStr.append(" inner join turma on matriculaperiodoturmadisciplina.turma = turma.codigo");
		sqlStr.append(" inner join disciplina on matriculaperiodoturmadisciplina.disciplina = disciplina.codigo");
		sqlStr.append(" inner join matricula on calendarioatividadematricula.matricula = matricula.matricula");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append(" inner join curso on matricula.curso = curso.codigo");
		sqlStr.append(" inner join pessoa on matricula.aluno = pessoa.codigo");
		sqlStr.append(" inner join historico on  historico.matricula = matricula.matricula and  historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" left join avaliacaoonlinematricula on avaliacaoonlinematricula.codigo = calendarioatividadematricula.avaliacaoonlinematricula");
		sqlStr.append(" left join conteudounidadepaginarecursoeducacional on conteudounidadepaginarecursoeducacional.codigo = calendarioatividadematricula.codorigem::numeric and calendarioatividadematricula.tipoorigem = 'REA' ");
		sqlStr.append(" left join conteudoUnidadePagina on conteudoUnidadePagina.codigo = conteudounidadepaginarecursoeducacional.conteudoUnidadePagina ");		
		sqlStr.append(" left join unidadeConteudo on unidadeConteudo.codigo  = conteudoUnidadePagina.unidadeConteudo ");		
		sqlStr.append(" where matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
		
		if(Uteis.isAtributoPreenchido(ano) || Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and ((curso.periodicidade = 'AN' and matriculaPeriodo.ano = '").append(ano).append("') ");
			sqlStr.append(" or  (curso.periodicidade = 'SE' and matriculaPeriodo.ano = '").append(ano).append("' and matriculaPeriodo.semestre = '").append(semestre).append("') ");
			sqlStr.append(" or  (curso.periodicidade = 'IN')) ");	
		}
		if (Uteis.isAtributoPreenchido(tipoCalendarioAtividadeMatriculaEnum)) {
			sqlStr.append(" and calendarioatividadematricula.tipocalendarioatividade in ('").append(tipoCalendarioAtividadeMatriculaEnum).append("') ");
			if (tipoCalendarioAtividadeMatriculaEnum.equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA) && Uteis.isAtributoPreenchido(tituloRea)) {
				sqlStr.append(" and lower(sem_acentos(conteudoUnidadePaginaRecursoEducacional.titulo)) like lower(sem_acentos(?)) ");
				parametros.add(tituloRea + "%");
			}
		} else {
			sqlStr.append(" and calendarioatividadematricula.tipocalendarioatividade in ('PERIODO_REALIZACAO_AVALIACAO_ONLINE', 'PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA') ");
		}
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		if(usuario.getIsApresentarVisaoProfessor()){
			sqlStr.append(Historico.getWhereAlunoCursaDisciplinaTurmaProfessor(usuario.getPessoa().getCodigo(), "historico", "matriculaperiodoturmadisciplina",  false));					
		}
		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append(" and calendarioatividadematricula.matricula = '").append(matricula).append("'");
		} 
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and matricula.unidadeEnsino =").append(unidadeEnsino);
		}
		
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and matricula.curso =").append(curso);
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" and turma.codigo = ").append(turma).append("");
		}
		if (situacaoAvaliacaoOnline.equals("APROVADO") || situacaoAvaliacaoOnline.equals("REPROVADO")) {
			sqlStr.append(" and avaliacaoonlinematricula.situacaoavaliacaoonlinematricula = '").append(situacaoAvaliacaoOnline).append("'");
		} else if (situacaoAvaliacaoOnline.equals("NAO_CONCLUIDA")) {
			sqlStr.append(" and (calendarioatividadematricula.situacaoatividade = '").append(situacaoAvaliacaoOnline).append("'").append("or avaliacaoonlinematricula.situacaoavaliacaoonlinematricula is null) ");
		}
		sqlStr.append(" and case when avaliacaoonlinematricula.codigo is null then ");
		sqlStr.append(" calendarioatividadematricula.datainicio between '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and '").append(Uteis.getDataJDBC(dataFim)).append(" 23:59:59' ");
		sqlStr.append(" else");
		sqlStr.append(" avaliacaoonlinematricula.datainicio between '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and '").append(Uteis.getDataJDBC(dataFim)).append(" 23:59:59' ");
		sqlStr.append(" end");
		sqlStr.append(" order by pessoa.nome, calendarioatividadematricula.tipocalendarioatividade, unidadeConteudo.ordem,  conteudoUnidadePagina.pagina");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), parametros.toArray());
		List<CalendarioAtividadeMatriculaVO> lista = new ArrayList<CalendarioAtividadeMatriculaVO>();
		CalendarioAtividadeMatriculaVO obj = new CalendarioAtividadeMatriculaVO();
		while (rs.next()) {

			obj.setCodigo(rs.getInt("codigocalendarioatividadematricula"));
			obj.setTipoCalendarioAtividade(TipoCalendarioAtividadeMatriculaEnum.valueOf(rs.getString("tipocalendarioatividade")));
			obj.setDescricao(rs.getString("calendarioatividadematricula.descricao"));
			obj.getMatriculaVO().setMatricula(rs.getString("matriculaaluno"));
			obj.getMatriculaVO().getAluno().setCodigo(rs.getInt("codigopessoa"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().setCodigo(rs.getInt("disciplinacodigo"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(rs.getInt("codigomatriculaperiodoturmadisciplina"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setCodigo(rs.getInt("codigoturma"));
			obj.getMatriculaVO().getAluno().setNome(rs.getString("nomealuno"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setIdentificadorTurma(rs.getString("identificadorturma"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().setNome(rs.getString("nomedisciplina"));
			obj.getAvaliacaoOnlineMatriculaVO().setCodigo(rs.getInt("codigoavaliacaoonlinematricula"));
			if (Uteis.isAtributoPreenchido(rs.getString("situacaoavaliacaoonlinematricula"))) {
				obj.getAvaliacaoOnlineMatriculaVO().setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.valueOf(rs.getString("situacaoavaliacaoonlinematricula")));
			}
			obj.getAvaliacaoOnlineMatriculaVO().setNota(rs.getDouble("nota"));
			obj.getAvaliacaoOnlineMatriculaVO().setDataInicio(rs.getTimestamp("datainicio"));
			obj.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaPeriodoObjetoVO().setSituacao(rs.getString("situacaoacademico"));
			if(obj.getTipoCalendarioAtividade().isPeriodoRealizacaoAvaliacaoOnlineRea()){
				obj.getConteudoUnidadePaginaRecursoEducacionalVO().setCodigo(rs.getInt("conteudounidadepaginarecursoeducacional_codigo"));
				obj.getConteudoUnidadePaginaRecursoEducacionalVO().setTitulo(rs.getString("conteudounidadepaginarecursoeducacional_titulo"));
				obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().setCodigo(rs.getInt("conteudoUnidadePagina_codigo"));
				obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().setPagina(rs.getInt("conteudoUnidadePagina_pagina"));
				obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().setTitulo(rs.getString("conteudoUnidadePagina_titulo"));
				obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().getUnidadeConteudo().setOrdem(rs.getInt("unidadeconteudo_ordem"));
			}

			lista.add(obj);

			obj = new CalendarioAtividadeMatriculaVO();
		}
		return lista;

	}


	/**
	 * @author Victor Hugo 20/11/2014
	 * 
	 *         Regras de Negócio:
	 * 
	 *         Acesso Conteudo Estudo - Data de início - Deve ser observado a
	 *         regra da ordem de estudos definida na configuração do EAD
	 *         vinculado a turma, sendo elas:
	 * 
	 *         Sequenciada - Neste o aluno só poderá estudar uma disciplina por
	 *         vez até que seja cumprida a regra de liberação de acesso a
	 *         próxima disciplina (Ver regra de liberação de acesso proxima
	 *         disciplina- item 2 regras gerais), neste caso a primerira
	 *         disciplina a ser liberada deverá ser a que tiver a menor ordem de
	 *         estudo definida na matriz curricular e a data de início deve ser
	 *         considerada a data da liberação da matrícula (Ver item 1 das
	 *         regas gerais), no caso de ser a segunda disciplina usar a regra
	 *         definida no item 2 das regras gerais.
	 * 
	 *         Simultânia - Neste caso o aluno poderá estudar simultaneamente a
	 *         qtde de disciplina definida no campo Nr. Máximo Disciplinas
	 *         Simultâneas definida na configuração do EAD, onde ser for 0 é
	 *         definidido com ilimitado, caso seja um valor maior que zero dever
	 *         ser liberado inicialmente a quantidade de disciplinas
	 *         configuradas tendo como data de início base a data de liberação
	 *         da matrícula (item 1 regras gerais), a medita em que o aluno
	 *         estudas as disciplinas liberadas só devem liberar novas
	 *         disciplinas de acordo com o item 2 das regras gerais.
	 * 
	 *         Data de témino - Deve ser considerado a regra de tempo para
	 *         estudo conforme o campo Tipo Controle Tempo Limite Conclusão
	 *         Disciplina da Configuração EAD definida na turma, sendo elas:
	 * 
	 *         Nr de dias fixos - considerar a data de inicio + a qtde de dias
	 *         definada na configuração do EAD (Tempo Limite Conlusão Disciplina
	 *         (nr. dias)).
	 * 
	 *         Nr.Dias por hora disciplina - considerar a data de início + a
	 *         qtde de dias definida na configuração do ead * o nr de horas da
	 *         disciplina. Ex: ser carga horaria for 30 e a qtde de dias
	 *         definida for de 1,5 então 30*1,5 = 45 + data de inicio.
	 * 
	 *         Nr Dias por crédito - Ex: ser crédito for 10 e a qtde de dias
	 *         definida for de 2,5 então 10*2,5 = 25 + data de inicio.
	 * 
	 *         Acesso Conteudo Consulta - Deve ser considerada as regras
	 *         definidas na configuração do EAD, sendo elas:
	 * 
	 *         Se marcado a opção de liberar acesso a consulto ao conteudo até o
	 *         prazo máximo de conclusão de curso então, deve ser considerado a
	 *         data de inicio do modulo e a data de termino do Módulo (ver regra
	 *         definida em Periodo Máximo Conclusão Curso ).
	 * 
	 *         Se definido no campo Qtde de Dias Liberar Acesso a Consulta
	 *         Modulo Após Aprovação - endtaõ deve ser considerado a data de
	 *         iníco a data de termino do módulo + a qtde de dias definida neste
	 *         campo.
	 * 
	 *         Período de Realização Avaliação On-line - Deve seguir as regras
	 *         definidas na Configuração do EAD no campo Tipo Controle Liberação
	 *         Avaliação Online, sendo elas: % Pontuação Alcançado Estudo - ou
	 *         seja quando o aluno estiver visualizando o conteudo do módulo e
	 *         atingir total de pontos que corresponda o percentual definido na
	 *         campo.
	 * 
	 *         % Pontuação Alcançados Estudo
	 *         (valorControleLiberacaoAvalicaoOnline) então deve ser liberado o
	 *         calendario da avaliação on-line. % Conteudo Lido - ou seja quando
	 *         o aluno estiver visualizando o conteudo do módulo e atingir o
	 *         percentual definido na campo.
	 * 
	 *         % Pontuação Alcançados Estudo
	 *         (valorControleLiberacaoAvalicaoOnline) então deve ser liberado o
	 *         calendario da avaliação on-line Imediato Após o Início da
	 *         Disciplina - Neste caso ao liberar a disciplina já deve ser
	 *         liberado a avaliação on-line.
	 * 
	 *         Nr. Dias após o início da disciplina - Neste caso deve ser gerado
	 *         o calendário de atividade com a data de início a data de início
	 *         de estudo no módulo +  a qtde de dias liberar avaliação on-line
	 *         definida na configuração do EAD
	 *         (valorControleLiberacaoAvalicaoOnline).
	 * 
	 *         Perído Máximo Conlusão de Curso - Data Início - Ver item 1 das
	 *         regras gerais. Data de término - é a data de início + a qtde de
	 *         dias definido no campo.
	 * 
	 *         Limite Conclusão Curso da Configuração EAD definida na turma Para
	 *         este tipo deve ser vinculado apenas a matricula periodo, pois o
	 *         mesmo não depende da disciplina.
	 * 
	 *         Período Máximo Conclusão Disciplina -  Data Início - Ver item 1
	 *         das regras gerais.
	 * 
	 *         Data de término - a data de pagamento de matrícula + a qtde de
	 *         dias definido no campo.
	 * 
	 *         Limite Conclusão Todas Disciplinas da Configuração EAD definida
	 *         na turma Para este tipo deve ser vinculado apenas a matricula
	 *         periodo, pois o mesmo não depende da disciplina.
	 * 
	 *         Agenda Prova Presencial - Deve seguir as regras definidas na
	 *         configuração do EAD conforme o campo.
	 * 
	 *         Tipo Controle Liberação Prova Presencial, sendo elas:
	 * 
	 *         Aprovação Avaliação Online - Quando o aluno for aprovado na
	 *         avaliação on-line então deverá ser gerado o calendário com o
	 *         período de agendamento da prova presencial, tendo como data de
	 *         início da data do ocorrido e a data de termino deve ser
	 *         considerado a data de início + o valor informado na configuração
	 *         do ead (valorControleLiberacaoProvaPresencial).
	 * 
	 *         % Pontuação Alcançado Estudo - ou seja quando o aluno estiver
	 *         visualizando o conteudo do módulo e atingir total de pontos que
	 *         corresponda o percentual definido na campo
	 * 
	 *         % Pontuação Alcançados Estudo
	 *         (valorControleLiberacaoProvaPresencial) então deve ser liberado o
	 *         calendario da prova presencial.
	 * 
	 *         % Conteudo Lido - ou seja quando o aluno estiver visualizando o
	 *         conteudo do módulo e atingir o percentual definido na campo
	 * 
	 *         % Pontuação Alcançados Estudo
	 *         (valorControleLiberacaoProvaPresencial) então deve ser liberado o
	 *         calendario da prova presencial.
	 * 
	 *         Nr. Dias após o início da disciplina - Neste caso deve ser gerado
	 *         o calendário de atividade com a data de início a data de início
	 *         de estudo no módulo +  a qtde de dias liberar prova presencial
	 *         definida na configuração do EAD
	 *         (valorControleLiberacaoProvaPresencial)
	 * 
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarExistenciaCalendariosAtividadesMatriculas(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Boolean forcarLiberacao, UsuarioVO usuarioVO) throws Exception {
		ConfiguracaoEADVO configuracaoEADVO = getFacadeFactory().getConfiguracaoEADFacade().consultarConfiguracaoEADPorTurma(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo());
		if (Uteis.isAtributoPreenchido(configuracaoEADVO.getCodigo())) {
			if (verificarExistenciaPorCodigoMatriculaPeriodoTurmaDisciplinaTipoCalendarioAtividade(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO, TipoOrigemEnum.NENHUM, "", Uteis.NIVELMONTARDADOS_TODOS, usuarioVO) == false) {
				realizarConfiguracoesParaCalendarioAtividadeMatriculaAcessoConteudo(configuracaoEADVO, matriculaPeriodoTurmaDisciplinaVO, usuarioVO);
			} else {
				verificarSeDataFimCalendarioExpirouAcessoDisciplina(matriculaPeriodoTurmaDisciplinaVO, usuarioVO);
				realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAvaliacaoOnline(matriculaPeriodoTurmaDisciplinaVO, null, null, configuracaoEADVO, usuarioVO);
				realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAvaliacaoOnlinePorTurma(matriculaPeriodoTurmaDisciplinaVO, null, false, usuarioVO);
				realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAtividadeDiscursiva(matriculaPeriodoTurmaDisciplinaVO, configuracaoEADVO, usuarioVO);
				
				CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
				calendarioAtividadeMatriculaVO = consultarPorCodigoMatriculaPeriodoTurmaDisciplinaTipoCalendarioAtividade(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
				realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoConclusaoDisciplina(matriculaPeriodoTurmaDisciplinaVO, configuracaoEADVO, calendarioAtividadeMatriculaVO, usuarioVO);
				realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoConclusaoCurso(matriculaPeriodoTurmaDisciplinaVO, configuracaoEADVO, calendarioAtividadeMatriculaVO, usuarioVO);
				realizarGeracaoCalendarioAtividadeMatriculaAcessoConteudoConsulta(matriculaPeriodoTurmaDisciplinaVO, configuracaoEADVO, calendarioAtividadeMatriculaVO.getDataFim(), usuarioVO);
			}
		}else {
			throw new Exception("Não foi encontrado uma configuração do ead habilitada para geração dos calendários de estudo on-line.");
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarConfiguracoesParaCalendarioAtividadeMatriculaAcessoConteudo(ConfiguracaoEADVO configuracaoEADVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		Map<String, Integer> auxiliar = new HashMap<String, Integer>();
		getFacadeFactory().getMatriculaPeriodoFacade().verificarOrdemEQtdeEstudandoMatriculaPeriodo(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo(), auxiliar);
		if (configuracaoEADVO.getOrdemEstudoDisciplinasOnline().isSequenciadas()) {
			realizarConfiguracoesParaCalendarioAtividadeMatriculaComOrdemEmSequencia(auxiliar, configuracaoEADVO, matriculaPeriodoTurmaDisciplinaVO, usuarioVO);
		} else {
			realizarConfiguracoesParaCalendarioAtividadeMatriculaComOrdemSimultanea(auxiliar, configuracaoEADVO, matriculaPeriodoTurmaDisciplinaVO, usuarioVO);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarConfiguracoesParaCalendarioAtividadeMatriculaComOrdemEmSequencia(Map<String, Integer> auxiliar, ConfiguracaoEADVO configuracaoEADVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = null;
		MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaAnterior = null;
		TurmaDisciplinaVO turmaDisciplinaVO = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), false, usuarioVO);
		if (auxiliar.get("ordemestudo") == 0) {
			calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
			realizarConfiguracoesParaCalendarioAtividadePrimeiraOrdemEstudo(auxiliar, configuracaoEADVO, turmaDisciplinaVO, matriculaPeriodoTurmaDisciplinaVO, calendarioAtividadeMatriculaVO, usuarioVO);
		} else if (turmaDisciplinaVO.getOrdemEstudoOnline() >= auxiliar.get("ordemestudo") && (auxiliar.get("proximaordem").equals(turmaDisciplinaVO.getOrdemEstudoOnline()))) {
			matriculaPeriodoTurmaDisciplinaAnterior = new MatriculaPeriodoTurmaDisciplinaVO();
			matriculaPeriodoTurmaDisciplinaAnterior = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorTurmaMatriculaOrdemEstudoOnlineMatriculaPeriodoTurmaDisciplinaAnterior(matriculaPeriodoTurmaDisciplinaVO.getMatricula(), matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), auxiliar.get("ordemestudo"), usuarioVO);
			if(configuracaoEADVO.getTipoControleLiberarAcessoProximaDisciplina().isConteudoLido() || configuracaoEADVO.getTipoControleLiberarAcessoProximaDisciplina().isPontuacaoAlcancadaEstudo()) {
				realizarVerificacaoProximaDisciplinaSequencialConteudo(matriculaPeriodoTurmaDisciplinaVO, matriculaPeriodoTurmaDisciplinaAnterior, configuracaoEADVO, usuarioVO);		
			}else if(configuracaoEADVO.getTipoControleLiberarAcessoProximaDisciplina().isAposAprovacaoReprovacaoDisciplina()) {
				realizarVerificacaoProximaDisciplinaSequencialDisciplina(matriculaPeriodoTurmaDisciplinaVO, matriculaPeriodoTurmaDisciplinaAnterior, configuracaoEADVO, usuarioVO);
			}
		} else {
			throw new Exception(UteisJSF.internacionalizar("msg_AcessoEstudoOnline_disciplinaNaoDisponivel"));
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarVerificacaoProximaDisciplinaSequencialConteudo(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaAnterior, ConfiguracaoEADVO configuracaoEADVO, UsuarioVO usuarioLogado) throws Exception {
		if (matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo() == 0 && !configuracaoEADVO.isPermitirAcessoEadSemConteudo()) {
			throw new Exception(UteisJSF.internacionalizar("msg_AcessoEstudoOnline_disciplinaSemConteudo"));
		}
		CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
		if (!matriculaPeriodoTurmaDisciplinaVO.isExisteConteudo() && configuracaoEADVO.isPermitirAcessoEadSemConteudo()){
			executarInicializacaoDataInicioCalendarioAtividadeMatricula(configuracaoEADVO, calendarioAtividadeMatriculaVO, matriculaPeriodoTurmaDisciplinaVO, null, usuarioLogado);
		}else {
			double totalPontoAtingido;
			double totalPontos;
			totalPontoAtingido = getFacadeFactory().getConteudoRegistroAcessoFacade().consultarTotalPontosAlunoAtingiuConteudo(matriculaPeriodoTurmaDisciplinaAnterior.getMatricula(), matriculaPeriodoTurmaDisciplinaAnterior.getCodigo(), matriculaPeriodoTurmaDisciplinaAnterior.getConteudo().getCodigo());
			totalPontos = getFacadeFactory().getConteudoFacade().consultarPontuacaoTotalConteudo(matriculaPeriodoTurmaDisciplinaAnterior.getConteudo().getCodigo());
			if (configuracaoEADVO.getTipoControleLiberarAcessoProximaDisciplina().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.PONTUACAO_ALCANCADA_ESTUDOS)) {
				if (((totalPontoAtingido / totalPontos) * 100) < configuracaoEADVO.getValorControleLiberarAcessoProximaDisciplina()) {
					throw new Exception(UteisJSF.internacionalizar("msg_AcessoEstudoOnline_disciplinaNaoDisponivel"));
				}		
				executarInicializacaoDataInicioCalendarioAtividadeMatricula(configuracaoEADVO, calendarioAtividadeMatriculaVO, matriculaPeriodoTurmaDisciplinaVO, null, usuarioLogado);
			} else if (configuracaoEADVO.getTipoControleLiberarAcessoProximaDisciplina().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.CONTEUDO_LIDO)) {
				int quantidadeAcessoConteudo = getFacadeFactory().getConteudoRegistroAcessoFacade().consultarTotalAcessoAlunoRealizouConteudo(matriculaPeriodoTurmaDisciplinaAnterior.getMatricula(), matriculaPeriodoTurmaDisciplinaAnterior.getCodigo(), matriculaPeriodoTurmaDisciplinaAnterior.getConteudo().getCodigo());
				if (quantidadeAcessoConteudo < configuracaoEADVO.getValorControleLiberarAcessoProximaDisciplina()) {
					throw new Exception(UteisJSF.internacionalizar("msg_AcessoEstudoOnline_disciplinaNaoDisponivel"));
				}
				executarInicializacaoDataInicioCalendarioAtividadeMatricula(configuracaoEADVO, calendarioAtividadeMatriculaVO, matriculaPeriodoTurmaDisciplinaVO, null, usuarioLogado);
			} else {
				throw new Exception(UteisJSF.internacionalizar("msg_AcessoEstudoOnline_disciplinaLiberarAvaliacaoOnline"));
			}
		}
		calendarioAtividadeMatriculaVO.setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarDataInicioDataFimPeriodoPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), usuarioLogado));
		realizarGeracaoTodosCalendariosAtividadeMatricula(matriculaPeriodoTurmaDisciplinaVO, calendarioAtividadeMatriculaVO, configuracaoEADVO, usuarioLogado);
	}
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarVerificacaoProximaDisciplinaSequencialDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaAnterior, ConfiguracaoEADVO configuracaoEADVO, UsuarioVO usuarioLogado) throws Exception {
		CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
		Integer qtdSituacaoCursandoAnterior = getFacadeFactory().getHistoricoFacade().consultarQtdHistorioCursandoPorCalendarioAtividadeMatriculaPorPeriodo(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo(), matriculaPeriodoTurmaDisciplinaAnterior.getCodigo(),  TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO ,false, usuarioLogado);
		if (qtdSituacaoCursandoAnterior.equals(1)) {
			throw new Exception(UteisJSF.internacionalizar("msg_AcessoEstudoOnline_disciplinaNaoDisponivel"));
		} else {
			executarInicializacaoDataInicioCalendarioAtividadeMatricula(configuracaoEADVO, calendarioAtividadeMatriculaVO, matriculaPeriodoTurmaDisciplinaVO, null, usuarioLogado);
			calendarioAtividadeMatriculaVO.setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarDataInicioDataFimPeriodoPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), usuarioLogado));
			realizarGeracaoTodosCalendariosAtividadeMatricula(matriculaPeriodoTurmaDisciplinaVO, calendarioAtividadeMatriculaVO, configuracaoEADVO, usuarioLogado);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void verificarRealizacaoProximaDisciplinaAprovadoAvaliacaoOnline(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Date dataRealizacaoAvaliacao, ConfiguracaoEADVO configuracaoEADVO, UsuarioVO usuarioLogado) throws Exception {
		CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
		Map<String, Integer> auxiliar = new HashMap<String, Integer>();
		getFacadeFactory().getMatriculaPeriodoFacade().verificarOrdemEQtdeEstudandoMatriculaPeriodo(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo(), auxiliar);
		MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaPosterior = new MatriculaPeriodoTurmaDisciplinaVO();
		matriculaPeriodoTurmaDisciplinaPosterior = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarPorTurmaMatriculaOrdemEstudoOnlineMatriculaPeriodoTurmaDisciplinaPosterior(matriculaPeriodoTurmaDisciplinaVO.getMatricula(), matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), 
				matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), usuarioLogado);
		if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaPosterior)) {
			calendarioAtividadeMatriculaVO.setDataInicio(dataRealizacaoAvaliacao);
			calendarioAtividadeMatriculaVO.setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarDataInicioDataFimPeriodoPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), usuarioLogado));
			realizarGeracaoTodosCalendariosAtividadeMatricula(matriculaPeriodoTurmaDisciplinaPosterior, calendarioAtividadeMatriculaVO, configuracaoEADVO, usuarioLogado);
		}
	}

	public void realizarConfiguracoesParaCalendarioAtividadePrimeiraOrdemEstudo(Map<String, Integer> auxiliar, ConfiguracaoEADVO configuracaoEADVO, TurmaDisciplinaVO turmaDisciplinaVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, UsuarioVO usuarioVO) throws Exception {
		if (turmaDisciplinaVO.getOrdemEstudoOnline().equals(auxiliar.get("primeiraordemestudo"))) {
			Date dataInicio = realizarGeracaoDataInicioCalendarioAtividadeMatricula(configuracaoEADVO, calendarioAtividadeMatriculaVO, matriculaPeriodoTurmaDisciplinaVO, null, usuarioVO);
			if(dataInicio != null) {
				calendarioAtividadeMatriculaVO.setDataInicio(dataInicio);
			}else if(!Uteis.isAtributoPreenchido(calendarioAtividadeMatriculaVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO()) && dataInicio == null){
				throw new Exception(UteisJSF.internacionalizar("msg_AcessoEstudoOnline_periodoLetivoAtivoUnidadeEnsinoCurso"));
			}
			else if (calendarioAtividadeMatriculaVO.getDataInicio() == null) {
				throw new Exception(UteisJSF.internacionalizar("msg_AcessoEstudoOnline_disciplinaNaoDisponivel"));
			}			
			realizarGeracaoTodosCalendariosAtividadeMatricula(matriculaPeriodoTurmaDisciplinaVO, calendarioAtividadeMatriculaVO, configuracaoEADVO, usuarioVO);
		} else {
			throw new Exception(UteisJSF.internacionalizar("msg_AcessoEstudoOnline_disciplinaNaoDisponivel"));
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarConfiguracoesParaCalendarioAtividadeMatriculaComOrdemSimultanea(Map<String, Integer> auxiliar, ConfiguracaoEADVO configuracaoEADVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioVO) throws Exception {
		CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
		if (configuracaoEADVO.getNrMaximoDisciplinasSimultaneas() != 0) {
			if (auxiliar.get("qtdeestudando") >= configuracaoEADVO.getNrMaximoDisciplinasSimultaneas()) {
				if(configuracaoEADVO.getTipoControleLiberarAcessoProximaDisciplina().isConteudoLido() || configuracaoEADVO.getTipoControleLiberarAcessoProximaDisciplina().isPontuacaoAlcancadaEstudo()) {
					verificarSeAptoEstudoProximaDisciplinaConteudoTipoOrdemEstudoDisciplinasOnlineSimultanea(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo(), matriculaPeriodoTurmaDisciplinaVO, configuracaoEADVO, usuarioVO);		
				}else if(configuracaoEADVO.getTipoControleLiberarAcessoProximaDisciplina().isAposAprovacaoReprovacaoDisciplina()) {
					verificarSeAptoEstudoProximaDisciplinaAposAprovacaoReprovacaoOnlineSimultanea(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo(), matriculaPeriodoTurmaDisciplinaVO, configuracaoEADVO, usuarioVO);
				}
				return;
			} else {
				executarInicializacaoDataInicioCalendarioAtividadeMatricula(configuracaoEADVO, calendarioAtividadeMatriculaVO, matriculaPeriodoTurmaDisciplinaVO, null, usuarioVO);
				calendarioAtividadeMatriculaVO.setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarDataInicioDataFimPeriodoPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), usuarioVO));
			}
		}
		realizarGeracaoTodosCalendariosAtividadeMatricula(matriculaPeriodoTurmaDisciplinaVO, calendarioAtividadeMatriculaVO, configuracaoEADVO, usuarioVO);
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void verificarSeDataFimCalendarioExpirouAcessoDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, UsuarioVO usuarioLogado) throws Exception {
		if (consultarPorCodigoMatriculaPeriodoTurmaDisciplinaOuMatriculaPeriodoETipoCalendarioAtividadeCalendarioExpirado(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo(), TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_CURSO, true, usuarioLogado)) {
			throw new Exception(UteisJSF.internacionalizar("msg_CalendarioAtividadeMatricula_prezadoAlunoPrazoEncerradoAcessoConteudo"));
		}
	}

	public void verificarSeAptoEstudoProximaDisciplinaConteudoTipoOrdemEstudoDisciplinasOnlineSimultanea(Integer matriculaPeriodoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, ConfiguracaoEADVO configuracaoEADVO, UsuarioVO usuarioLogado) throws Exception {
		List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs = new ArrayList<CalendarioAtividadeMatriculaVO>(0);
		CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = null;
		Integer contAptoEstudoProximaDisciplina = 0;
		calendarioAtividadeMatriculaVOs = consultarPorCodigoMatriculaPerido(matriculaPeriodoVO, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		if(!matriculaPeriodoTurmaDisciplinaVO.isExisteConteudo() && configuracaoEADVO.isPermitirAcessoEadSemConteudo()) {
			contAptoEstudoProximaDisciplina = calendarioAtividadeMatriculaVOs.size();
		}else {
			for (CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO2 : calendarioAtividadeMatriculaVOs) {
				double totalPontoAtingido = 0.0;
				double totalPontos = 0.0;
				totalPontoAtingido = getFacadeFactory().getConteudoRegistroAcessoFacade().consultarTotalPontosAlunoAtingiuConteudo(calendarioAtividadeMatriculaVO2.getMatriculaPeriodoTurmaDisciplinaVO().getMatricula(), calendarioAtividadeMatriculaVO2.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(),calendarioAtividadeMatriculaVO2.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().getCodigo());
				totalPontos = getFacadeFactory().getConteudoFacade().consultarPontuacaoTotalConteudo(calendarioAtividadeMatriculaVO2.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().getCodigo());
				if (configuracaoEADVO.getTipoControleLiberarAcessoProximaDisciplina().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.PONTUACAO_ALCANCADA_ESTUDOS)) {
					if (((totalPontoAtingido / totalPontos) * 100) >= configuracaoEADVO.getValorControleLiberarAcessoProximaDisciplina()) {
						contAptoEstudoProximaDisciplina++;
						break;
					}
				} else if (configuracaoEADVO.getTipoControleLiberarAcessoProximaDisciplina().equals(TipoControleLiberarAcessoProximaDisciplinaEnum.CONTEUDO_LIDO)) {
					if (Uteis.arrendondarForcando2CadasDecimais((totalPontoAtingido * 100) / totalPontos) >= configuracaoEADVO.getValorControleLiberarAcessoProximaDisciplina()) {
						contAptoEstudoProximaDisciplina++;
						break;
					}
				} else {
					throw new Exception(UteisJSF.internacionalizar("msg_AcessoEstudoOnline_disciplinaLiberarAvaliacaoOnline"));
				}
			}
		}
		if (contAptoEstudoProximaDisciplina != 0) {
			calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
			executarInicializacaoDataInicioCalendarioAtividadeMatricula(configuracaoEADVO, calendarioAtividadeMatriculaVO, matriculaPeriodoTurmaDisciplinaVO, null, usuarioLogado);
			calendarioAtividadeMatriculaVO.setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarDataInicioDataFimPeriodoPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), usuarioLogado));
			realizarGeracaoTodosCalendariosAtividadeMatricula(matriculaPeriodoTurmaDisciplinaVO, calendarioAtividadeMatriculaVO, configuracaoEADVO, usuarioLogado);
		} else {
			throw new Exception(UteisJSF.internacionalizar("msg_AcessoEstudoOnline_disciplinaNaoDisponivel"));
		}
	}
	
	public void verificarSeAptoEstudoProximaDisciplinaAposAprovacaoReprovacaoOnlineSimultanea(Integer matriculaPeriodoVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, ConfiguracaoEADVO configuracaoEADVO, UsuarioVO usuarioLogado) throws Exception {		
		CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = null;
		Integer contAptoEstudoProximaDisciplina = getFacadeFactory().getHistoricoFacade().consultarQtdHistorioCursandoPorCalendarioAtividadeMatriculaPorPeriodo(matriculaPeriodoVO,  null, TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO ,false, usuarioLogado);
		if (contAptoEstudoProximaDisciplina >= configuracaoEADVO.getNrMaximoDisciplinasSimultaneas()) {
			throw new Exception(UteisJSF.internacionalizar("msg_AcessoEstudoOnline_disciplinaNaoDisponivel"));
		} else {
			calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
			executarInicializacaoDataInicioCalendarioAtividadeMatricula(configuracaoEADVO, calendarioAtividadeMatriculaVO, matriculaPeriodoTurmaDisciplinaVO, null, usuarioLogado);
			calendarioAtividadeMatriculaVO.setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarDataInicioDataFimPeriodoPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), usuarioLogado));
			realizarGeracaoTodosCalendariosAtividadeMatricula(matriculaPeriodoTurmaDisciplinaVO, calendarioAtividadeMatriculaVO, configuracaoEADVO, usuarioLogado);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarGeracaoCalendarioAtividadeMatriculaAcessoConteudoEstudo(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, ConfiguracaoEADVO configuracaoEADVO, UsuarioVO usuarioLogado, ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO) throws Exception {
		// Data de Término
		if(programacaoTutoriaOnlineVO != null && programacaoTutoriaOnlineVO.getDefinirPeriodoAulaOnline()) {
			calendarioAtividadeMatriculaVO.setDataFim(programacaoTutoriaOnlineVO.getDataTerminoAula());
		}else {		
			calendarioAtividadeMatriculaVO.setDataFim(realizarGeracaoDataTerminoCalendarioAtividadeMatricula(configuracaoEADVO, calendarioAtividadeMatriculaVO, matriculaPeriodoTurmaDisciplinaVO, TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO, usuarioLogado));
		}
		calendarioAtividadeMatriculaVO = montarCalendarioAtividadeMatriculaParaInclusao(matriculaPeriodoTurmaDisciplinaVO, calendarioAtividadeMatriculaVO, TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO, usuarioLogado);
		incluir(calendarioAtividadeMatriculaVO, false, usuarioLogado);
		getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAcessoConteudoEstudo(calendarioAtividadeMatriculaVO, usuarioLogado);

	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarGeracaoTodosCalendariosAtividadeMatricula(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, ConfiguracaoEADVO configuracaoEADVO, UsuarioVO usuarioLogado) throws Exception {
		//Calendário Acesso Conteudo Estudo
		realizarGeracaoCalendarioAtividadeMatriculaAcessoConteudoEstudo(matriculaPeriodoTurmaDisciplinaVO, calendarioAtividadeMatriculaVO, configuracaoEADVO, usuarioLogado,  null);
		
		// Outros Calendários
		realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAtividadeDiscursiva(matriculaPeriodoTurmaDisciplinaVO, configuracaoEADVO, usuarioLogado);
		realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoConclusaoDisciplina(matriculaPeriodoTurmaDisciplinaVO, configuracaoEADVO, calendarioAtividadeMatriculaVO, usuarioLogado);
		realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoConclusaoCurso(matriculaPeriodoTurmaDisciplinaVO, configuracaoEADVO, calendarioAtividadeMatriculaVO, usuarioLogado);
		realizarGeracaoCalendarioAtividadeMatriculaAcessoConteudoConsulta(matriculaPeriodoTurmaDisciplinaVO, configuracaoEADVO, calendarioAtividadeMatriculaVO.getDataFim(), usuarioLogado);
		realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAvaliacaoOnline(matriculaPeriodoTurmaDisciplinaVO, null, null, configuracaoEADVO, usuarioLogado);
		realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAvaliacaoOnlinePorTurma(matriculaPeriodoTurmaDisciplinaVO, null, false, usuarioLogado);
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAtividadeDiscursiva(MatriculaPeriodoTurmaDisciplinaVO mptd, ConfiguracaoEADVO configuracaoEADVO, UsuarioVO usuarioLogado) throws Exception {
		List<AtividadeDiscursivaVO> lista = getFacadeFactory().getAtividadeDiscursivaInterfaceFacade().consultarAtividadeDiscursivaParaGeracaoCalendarioAtividade(mptd.getCodigo(), mptd.getAno(), mptd.getSemestre(), usuarioLogado);
		CalendarioAtividadeMatriculaVO calendarioAcessoConteudo = consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividade(mptd.getCodigo(), TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO, TipoOrigemEnum.NENHUM, "", Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
		for (AtividadeDiscursivaVO atividadeDiscursivaVO : lista) {
			CalendarioAtividadeMatriculaVO novoObj = new CalendarioAtividadeMatriculaVO();
			if (atividadeDiscursivaVO.getDefinicaoDataEntregaAtividadeDiscursivaEnum().isInicioEstudoOnline()) {
				novoObj.setDataInicio(Uteis.obterDataAvancada(calendarioAcessoConteudo.getDataInicio(), atividadeDiscursivaVO.getQtdDiasAposInicioLiberar()));
				novoObj.setDataFim(Uteis.obterDataAvancada(novoObj.getDataInicio(), atividadeDiscursivaVO.getQtdDiasParaConclusao()));
			} else {
				novoObj.setDataInicio(atividadeDiscursivaVO.getDataLiberacao());
				novoObj.setDataFim(atividadeDiscursivaVO.getDataLimiteEntrega());
			}
			novoObj.setAtividadeDiscursivaVO(atividadeDiscursivaVO);
			novoObj.setTipoOrigem(TipoOrigemEnum.ATIVIDADE_DISCURSIVA);
			novoObj.setCodOrigem(atividadeDiscursivaVO.getCodigo().toString());
			novoObj.getMatriculaPeriodoVO().setCodigo(mptd.getMatriculaPeriodo());
			novoObj = montarCalendarioAtividadeMatriculaParaInclusao(mptd, novoObj, TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_ATIVIDADE_DISCURSIVA, usuarioLogado);
			incluir(novoObj, false, usuarioLogado);
			if (UteisData.getCompareDataComHora(new Date(), novoObj.getDataInicio()) >= 0) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemPeriodoMaximoAtividadeDiscursiva(novoObj, usuarioLogado);
			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoConclusaoCurso(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, ConfiguracaoEADVO configuracaoEADVO, CalendarioAtividadeMatriculaVO calendarioAcessoConteudo, UsuarioVO usuarioLogado) throws Exception {
		if (!consultarPorCodigoMatriculaPeriodo(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo(), TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_CURSO, usuarioLogado)) {
			CalendarioAtividadeMatriculaVO calendarioPeriodoMaximoConclusaoCurso = new CalendarioAtividadeMatriculaVO();
			if (!configuracaoEADVO.getTipoControleTempoLimiteConclusaoDisciplina().isPeriodoCalendarioLetivo()) {
				calendarioPeriodoMaximoConclusaoCurso.setDataInicio(calendarioAcessoConteudo.getDataInicio());
				calendarioPeriodoMaximoConclusaoCurso.setDataFim(Uteis.obterDataAvancada(calendarioPeriodoMaximoConclusaoCurso.getDataInicio(), configuracaoEADVO.getTempoLimiteConclusaoCursoIncluindoTCC()));
			} else {
				calendarioPeriodoMaximoConclusaoCurso.setDataInicio(calendarioAcessoConteudo.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivo());
				calendarioPeriodoMaximoConclusaoCurso.setDataFim(calendarioAcessoConteudo.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataFimPeriodoLetivo());
			}
			calendarioPeriodoMaximoConclusaoCurso.getMatriculaPeriodoVO().setCodigo(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo());
			calendarioPeriodoMaximoConclusaoCurso = montarCalendarioAtividadeMatriculaParaInclusao(matriculaPeriodoTurmaDisciplinaVO, calendarioPeriodoMaximoConclusaoCurso, TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_CURSO, usuarioLogado);
			calendarioPeriodoMaximoConclusaoCurso.setMatriculaPeriodoTurmaDisciplinaVO(null);
			incluir(calendarioPeriodoMaximoConclusaoCurso, false, usuarioLogado);
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemPeriodoMaximoConclusaoCurso(calendarioPeriodoMaximoConclusaoCurso, usuarioLogado);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoConclusaoDisciplina(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, ConfiguracaoEADVO configuracaoEADVO, CalendarioAtividadeMatriculaVO calendarioAcessoConteudo, UsuarioVO usuarioLogado) throws Exception {
		if (!consultarPorCodigoMatriculaPeriodo(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo(), TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_DISCIPLINAS, usuarioLogado)) {
			CalendarioAtividadeMatriculaVO calendarioPeriodoMaximoConclusaoDisciplina = new CalendarioAtividadeMatriculaVO();
			if (!configuracaoEADVO.getTipoControleTempoLimiteConclusaoDisciplina().isPeriodoCalendarioLetivo()) {
				calendarioPeriodoMaximoConclusaoDisciplina.setDataInicio(calendarioAcessoConteudo.getDataInicio());
			} else {
				calendarioPeriodoMaximoConclusaoDisciplina.setDataInicio(calendarioAcessoConteudo.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivo());
			}
			calendarioPeriodoMaximoConclusaoDisciplina.setDataFim(realizarGeracaoDataTerminoCalendarioAtividadeMatricula(configuracaoEADVO, calendarioAcessoConteudo, matriculaPeriodoTurmaDisciplinaVO, TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_DISCIPLINAS, usuarioLogado));
			calendarioPeriodoMaximoConclusaoDisciplina.getMatriculaPeriodoVO().setCodigo(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo());
			calendarioPeriodoMaximoConclusaoDisciplina = montarCalendarioAtividadeMatriculaParaInclusao(matriculaPeriodoTurmaDisciplinaVO, calendarioPeriodoMaximoConclusaoDisciplina, TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_DISCIPLINAS, usuarioLogado);
			calendarioPeriodoMaximoConclusaoDisciplina.setMatriculaPeriodoTurmaDisciplinaVO(null);
			incluir(calendarioPeriodoMaximoConclusaoDisciplina, false, usuarioLogado);
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemPeriodoMaximoConclusaoDisciplinas(calendarioPeriodoMaximoConclusaoDisciplina, usuarioLogado);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void realizarGeracaoCalendarioAtividadeMatriculaAcessoConteudoConsulta(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, ConfiguracaoEADVO configuracaoEADVO, Date dataFim, UsuarioVO usuarioLogado) throws Exception {
		if (configuracaoEADVO.getPermitirAcessoConsultaConteudoDisciplinaConclusaoCurso() && !consultarPorCodigoMatriculaPeriodo(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo(), TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_CONSULTA, usuarioLogado)) {
			CalendarioAtividadeMatriculaVO calendarioConsultarConteudo = new CalendarioAtividadeMatriculaVO();
			calendarioConsultarConteudo.setDataInicio(dataFim);
			calendarioConsultarConteudo.setDataFim(Uteis.obterDataAvancada(calendarioConsultarConteudo.getDataInicio(), (configuracaoEADVO.getTempoLimiteConclusaoCursoIncluindoTCC() + configuracaoEADVO.getTempoLimiteAcessoConsultaConteudoDisciplinaAposConclusao())));
			calendarioConsultarConteudo = montarCalendarioAtividadeMatriculaParaInclusao(matriculaPeriodoTurmaDisciplinaVO, calendarioConsultarConteudo, TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_CONSULTA, usuarioLogado);
			incluir(calendarioConsultarConteudo, false, usuarioLogado);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAvaliacaoOnline(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Double totalPontoAtingido, Double porcentagemConteudo, ConfiguracaoEADVO configuracaoEADVO, UsuarioVO usuarioLogado) throws Exception {		
		if (!configuracaoEADVO.getTipoControleLiberacaoAvaliacaoOnline().isNaoAplicarAvaliacaoOnline()) {
			CalendarioAtividadeMatriculaVO calendarioAvaliacaoOnline = new CalendarioAtividadeMatriculaVO();
			if (!verificarExistenciaPorCodigoMatriculaPeriodoTurmaDisciplinaTipoCalendarioAtividade(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE, TipoOrigemEnum.NENHUM, "", Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado)) {
				boolean gerarAvaliacao = false;				
				if (configuracaoEADVO.getTipoControleLiberacaoAvaliacaoOnline().isPontuacaoAlcancadaEstudo() && totalPontoAtingido != null && totalPontoAtingido >= configuracaoEADVO.getValorControleLiberacaoAvalicaoOnline()) {
					calendarioAvaliacaoOnline.setDataInicio(new Date());
					gerarAvaliacao = true;
				}else if (configuracaoEADVO.getTipoControleLiberacaoAvaliacaoOnline().isConteudoLido() && porcentagemConteudo != null && porcentagemConteudo >= configuracaoEADVO.getValorControleLiberacaoAvalicaoOnline()) {
					calendarioAvaliacaoOnline.setDataInicio(new Date());
					gerarAvaliacao = true;
				}else if (configuracaoEADVO.getTipoControleLiberacaoAvaliacaoOnline().isImediatoAposInicioCurso()) {
					calendarioAvaliacaoOnline.setDataInicio(new Date());
					gerarAvaliacao = true;
				} else if (configuracaoEADVO.getTipoControleLiberacaoAvaliacaoOnline().isNrDiasAposInicioCurso()) {
					calendarioAvaliacaoOnline.setDataInicio(Uteis.obterDataAvancada(new Date(), configuracaoEADVO.getValorControleLiberacaoAvalicaoOnline()));
					gerarAvaliacao = true;
				}
				if(gerarAvaliacao){
					AvaliacaoOnlineVO avaliacaoOnlineVO = getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarAvaliacaoOnlineMatriculaAlunoDeveResponder(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), "", usuarioLogado);
					calendarioAvaliacaoOnline = montarCalendarioAtividadeMatriculaParaInclusao(matriculaPeriodoTurmaDisciplinaVO, calendarioAvaliacaoOnline, TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE, usuarioLogado);
					realizarDefinicaoDataInicioTerminoAvaliacaoOnlineMatricula(matriculaPeriodoTurmaDisciplinaVO, avaliacaoOnlineVO, calendarioAvaliacaoOnline, usuarioLogado);
					incluir(calendarioAvaliacaoOnline, false, usuarioLogado);
					new ExecutarEnvioMensagemAcessoAvaliacaoOnline(calendarioAvaliacaoOnline, usuarioLogado).enviar();	
				}
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<CalendarioAtividadeMatriculaVO> realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAvaliacaoOnlineRea(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, ConfiguracaoEADVO configuracaoEADVO, ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacional, UsuarioVO usuarioLogado) throws Exception {
		List<CalendarioAtividadeMatriculaVO> calendariosAvaliacaoOnline = consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividades(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA, TipoOrigemEnum.REA, conteudoUnidadePaginaRecursoEducacional.getCodigo().toString(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		if (!Uteis.isAtributoPreenchido(calendariosAvaliacaoOnline)) {
			CalendarioAtividadeMatriculaVO calendarioAvaliacaoOnline  = new CalendarioAtividadeMatriculaVO();
			calendarioAvaliacaoOnline.setTipoOrigem(TipoOrigemEnum.REA);
			calendarioAvaliacaoOnline.setCodOrigem(conteudoUnidadePaginaRecursoEducacional.getCodigo().toString());			
			calendarioAvaliacaoOnline = montarCalendarioAtividadeMatriculaParaInclusao(matriculaPeriodoTurmaDisciplinaVO, calendarioAvaliacaoOnline, TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA, usuarioLogado);
			realizarDefinicaoDataInicioTerminoAvaliacaoOnlineMatricula(matriculaPeriodoTurmaDisciplinaVO, conteudoUnidadePaginaRecursoEducacional.getAvaliacaoOnlineVO(), calendarioAvaliacaoOnline, usuarioLogado);			
			incluir(calendarioAvaliacaoOnline, false, usuarioLogado);
			calendariosAvaliacaoOnline.add(calendarioAvaliacaoOnline);
			new ExecutarEnvioMensagemAcessoAvaliacaoOnline(calendarioAvaliacaoOnline, usuarioLogado).enviar();				
		}else {
			for(CalendarioAtividadeMatriculaVO calendarioAvaliacaoOnline  : calendariosAvaliacaoOnline) {
				calendarioAvaliacaoOnline.setConteudoUnidadePaginaRecursoEducacionalVO(conteudoUnidadePaginaRecursoEducacional);
				realizarDefinicaoDataInicioTerminoCalendarioAtividadeMatriculaAvaliacaoOnline(calendarioAvaliacaoOnline, usuarioLogado);
			
			}
			
		}
		return calendariosAvaliacaoOnline;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarGeracaoCalendarioAtividadeMatriculaPeriodoRealizacaoAvaliacaoOnlinePorTurma(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO,  AvaliacaoOnlineVO avaliacaoOnlineVO, boolean forcarAlteracao, UsuarioVO usuarioLogado) throws Exception {
		List<AvaliacaoOnlineVO> listaAvaliacao = new ArrayList<>();
		if(!Uteis.isAtributoPreenchido(avaliacaoOnlineVO)) {
			listaAvaliacao =  getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarAvaliacaoOnlinePorTurmaAlunoDeveResponder(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), usuarioLogado);
		}else {
			listaAvaliacao.add(avaliacaoOnlineVO);
		}
		for (AvaliacaoOnlineVO avaliacaoOnlineteExistente : listaAvaliacao) {
			CalendarioAtividadeMatriculaVO calendarioAvaliacaoOnline = consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividade(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE, TipoOrigemEnum.AVALIACAO_ONLINE_TURMA, avaliacaoOnlineteExistente.getCodigo().toString(), Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
			if (Uteis.isAtributoPreenchido(calendarioAvaliacaoOnline) 
					&& (!Uteis.isAtributoPreenchido(calendarioAvaliacaoOnline.getAvaliacaoOnlineMatriculaVO()) || (Uteis.isAtributoPreenchido(calendarioAvaliacaoOnline.getAvaliacaoOnlineMatriculaVO()) &&  calendarioAvaliacaoOnline.getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO))) 
					&& ((avaliacaoOnlineteExistente.getDataInicioAvaliacaoFixo() != null && UteisData.getCompareDataComHora(avaliacaoOnlineteExistente.getDataInicioAvaliacaoFixo(), calendarioAvaliacaoOnline.getDataInicio()) != 0) || (avaliacaoOnlineteExistente.getDataTerminoAvaliacaoFixo() != null && UteisData.getCompareDataComHora(avaliacaoOnlineteExistente.getDataTerminoAvaliacaoFixo(), calendarioAvaliacaoOnline.getDataFim()) != 0))
					&& (forcarAlteracao ? forcarAlteracao : calendarioAvaliacaoOnline.getNrVezesProrrogado().equals(0))) {
					calendarioAvaliacaoOnline.setDataInicio(avaliacaoOnlineteExistente.getDataInicioAvaliacaoFixo());
					calendarioAvaliacaoOnline.setDataFim(avaliacaoOnlineteExistente.getDataTerminoAvaliacaoFixo());
					if (forcarAlteracao) {
						realizarAlteracaoSituacaoCalendarioAtividadeMatricula(calendarioAvaliacaoOnline);
					}
					alterarDataCalendarioAtividadeMatricula(calendarioAvaliacaoOnline, false, usuarioLogado);
			} else 
				if (!Uteis.isAtributoPreenchido(calendarioAvaliacaoOnline) && Uteis.isAtributoPreenchido(avaliacaoOnlineteExistente)) {
				calendarioAvaliacaoOnline = montarCalendarioAtividadeMatriculaParaInclusao(matriculaPeriodoTurmaDisciplinaVO, calendarioAvaliacaoOnline, TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE, usuarioLogado);
				calendarioAvaliacaoOnline.setDescricao(avaliacaoOnlineteExistente.getNome() + " - "+calendarioAvaliacaoOnline.getDescricao());
				calendarioAvaliacaoOnline.setTipoOrigem(TipoOrigemEnum.AVALIACAO_ONLINE_TURMA);
				calendarioAvaliacaoOnline.setCodOrigem(avaliacaoOnlineteExistente.getCodigo().toString());
				realizarDefinicaoDataInicioTerminoAvaliacaoOnlineMatricula(matriculaPeriodoTurmaDisciplinaVO, avaliacaoOnlineteExistente, calendarioAvaliacaoOnline, usuarioLogado);			
				incluir(calendarioAvaliacaoOnline, false, usuarioLogado);
				new ExecutarEnvioMensagemAcessoAvaliacaoOnline(calendarioAvaliacaoOnline, usuarioLogado).enviar();
			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private Date realizarGeracaoDataTerminoCalendarioAtividadeMatricula(ConfiguracaoEADVO configuracaoEADVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatriculaEnum, UsuarioVO usuarioLogado) throws Exception {
		if (configuracaoEADVO.getTipoControleTempoLimiteConclusaoDisciplina().isNrDiasFixo()) {
			if(tipoCalendarioAtividadeMatriculaEnum.equals(TipoCalendarioAtividadeMatriculaEnum.PERIODO_MAXIMO_CONCLUSAO_DISCIPLINAS)) {
				return Uteis.obterDataAvancada(calendarioAtividadeMatriculaVO.getDataInicio(), Uteis.arredondarParaMais(configuracaoEADVO.getTempoLimiteConclusaoTodasDisciplinas()));
			}else {
				return Uteis.obterDataAvancada(calendarioAtividadeMatriculaVO.getDataInicio(), Uteis.arredondarParaMais(configuracaoEADVO.getTempoLimiteConclusaoDisciplina()));
			}
		}
		if (configuracaoEADVO.getTipoControleTempoLimiteConclusaoDisciplina().isPeriodoCalendarioLetivo()) {
			return calendarioAtividadeMatriculaVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataFimPeriodoLetivo();
		}
		Map<String, Integer> auxiliar = new HashMap<String, Integer>();
		getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarCargaHorariaENrCreditosDisciplinaPorCodigoMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO, auxiliar, usuarioLogado);
		if (configuracaoEADVO.getTipoControleTempoLimiteConclusaoDisciplina().isNrDiasPorCargaHorariaDisciplina()) {
			Double qtdeDiasProgredir = (Double) (auxiliar.get("cargaHoraria") * configuracaoEADVO.getTempoLimiteConclusaoDisciplina());
			return Uteis.obterDataAvancada(calendarioAtividadeMatriculaVO.getDataInicio(), Uteis.arredondarParaMais(qtdeDiasProgredir));
		}
		if (configuracaoEADVO.getTipoControleTempoLimiteConclusaoDisciplina().isNrDiasPorCreditoDisciplina()) {
			Double qtdeDiasProgredir = (Double) (auxiliar.get("nrcreditos") * configuracaoEADVO.getTempoLimiteConclusaoDisciplina());
			return Uteis.obterDataAvancada(calendarioAtividadeMatriculaVO.getDataInicio(), Uteis.arredondarParaMais(qtdeDiasProgredir));
		}
		return null;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private CalendarioAtividadeMatriculaVO montarCalendarioAtividadeMatriculaParaInclusao(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, UsuarioVO usuarioVO) throws Exception {
		if (tipoCalendarioAtividadeMatricula.isAcessoConteudoEstudo()) {
			calendarioAtividadeMatriculaVO.setDescricao("ACESSO CONTEÚDO ESTUDO");
		} else if (tipoCalendarioAtividadeMatricula.isPeriodoMaximoConclusaoCurso()) {
			calendarioAtividadeMatriculaVO.setDescricao("PERÍODO MÁXIMO CONCLUSÃO CURSO");
		} else if (tipoCalendarioAtividadeMatricula.isAcessoConteudoConsulta()) {
			calendarioAtividadeMatriculaVO.setDescricao("ACESSO CONTEÚDO CONSULTA");
		} else if (tipoCalendarioAtividadeMatricula.isPeriodoMaximoConclusaoDisciplina()) {
			calendarioAtividadeMatriculaVO.setDescricao("PERÍODO MÁXIMO CONCLUSÃO DISCIPLINAS");
		} else if (tipoCalendarioAtividadeMatricula.isPeriodoRealizacaoAvaliacaoOnline()) {
			calendarioAtividadeMatriculaVO.setDescricao("PERÍODO REALIZAÇÃO AVALIAÇÃO ON-LINE");
		} else if (tipoCalendarioAtividadeMatricula.isPeriodoRealizacaoAvaliacaoOnlineRea()) {
			calendarioAtividadeMatriculaVO.setDescricao("PERÍODO REALIZAÇÃO AVALIAÇÃO ON-LINE REA - CÓD "+ calendarioAtividadeMatriculaVO.getCodOrigem());
		} else if (tipoCalendarioAtividadeMatricula.isPeriodoMaximoAtividadeDiscursiva()) {
			calendarioAtividadeMatriculaVO.setDescricao("PERÍODO REALIZAÇÃO MÁXIMO ATIVIDADE DISCURSIVA - CÓD " + calendarioAtividadeMatriculaVO.getAtividadeDiscursivaVO().getCodigo());
		}
		calendarioAtividadeMatriculaVO.setDataCadastro(new Date());
		calendarioAtividadeMatriculaVO.setResponsavelCadastro(usuarioVO);
		calendarioAtividadeMatriculaVO.setTipoCalendarioAtividade(tipoCalendarioAtividadeMatricula);
		calendarioAtividadeMatriculaVO.getMatriculaVO().setMatricula(matriculaPeriodoTurmaDisciplinaVO.getMatricula());
		calendarioAtividadeMatriculaVO.setMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO);
		calendarioAtividadeMatriculaVO.setSituacaoAtividade(SituacaoAtividadeEnum.NAO_CONCLUIDA);
		calendarioAtividadeMatriculaVO.getMatriculaPeriodoVO().setCodigo(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo());	

		return calendarioAtividadeMatriculaVO;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Boolean verificarExistenciaPorCodigoMatriculaPeriodoTurmaDisciplinaTipoCalendarioAtividade(Integer codigoMatriculaPeriodoTurmaDisciplina, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, TipoOrigemEnum tipoOrigem, String codigoOrigem, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append("SELECT * FROM calendarioatividadematricula WHERE");
		sqlStr.append(" matriculaperiodoturmadisciplina = ").append(codigoMatriculaPeriodoTurmaDisciplina);
		sqlStr.append(" and tipoCalendarioAtividade = '").append(tipoCalendarioAtividadeMatricula).append("' ");
		if(!tipoOrigem.equals(TipoOrigemEnum.NENHUM)){
			sqlStr.append(" and tipoOrigem = '").append(tipoOrigem).append("' ");
			sqlStr.append(" and codOrigem = '").append(codigoOrigem).append("' ");
		}else {
			sqlStr.append(" and tipoOrigem = '").append(TipoOrigemEnum.NENHUM).append("' ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		if (rs.next()) {
			return true;
		}
		return false;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Boolean consultarSeExisteCalendarioAtividadeParaAluno(Integer codigoMatriculaPeriodoTurmaDisciplina, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, SituacaoAtividadeEnum situacaoAtividade,  TipoOrigemEnum tipoOrigem, String codigoOrigem, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		
		sqlStr.append("SELECT * FROM calendarioatividadematricula WHERE ");
		sqlStr.append(" matriculaperiodoturmadisciplina = ").append(codigoMatriculaPeriodoTurmaDisciplina);
		sqlStr.append(" and tipoCalendarioAtividade = '").append(tipoCalendarioAtividadeMatricula).append("' ");
		sqlStr.append(" and situacaoatividade = '").append(situacaoAtividade).append("' ");
		if(!tipoOrigem.equals(TipoOrigemEnum.NENHUM)){
			sqlStr.append(" and tipoOrigem = '").append(tipoOrigem).append("' ");
			sqlStr.append(" and codOrigem = '").append(codigoOrigem).append("' ");
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		
		if (rs.next()) {
			return true;
		}
		return false;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Boolean consultarPorCodigoMatriculaPeriodoTurmaDisciplinaOuMatriculaPeriodoETipoCalendarioAtividadeCalendarioExpirado(Integer codigoMatriculaPeriodoTurmaDisciplina, Integer codigoMatriculaPeriodo, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, Boolean consultarPorMatriculaPeriodo, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" select ( select max(datafim)");
		sqlStr.append(" from calendarioatividadematricula ");
		sqlStr.append(" where tipocalendarioatividade in('ACESSO_CONTEUDO_ESTUDO','PERIODO_MAXIMO_CONCLUSAO_DISCIPLINAS','PERIODO_MAXIMO_CONCLUSAO_CURSO', 'ACESSO_CONTEUDO_CONSULTA')");
		sqlStr.append(" and (matriculaperiodoturmadisciplina = ").append(codigoMatriculaPeriodoTurmaDisciplina).append(" or matriculaperiodo = ").append(codigoMatriculaPeriodo).append(" )");
		sqlStr.append(" ) <= current_date as dataexpirou");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		if (rs.next()) {
			return rs.getBoolean("dataexpirou");
		}
		return rs.getBoolean("dataexpirou");
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Boolean consultarPorCodigoMatriculaPeriodo(Integer codigoMatriculaPeriodo, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append("SELECT * FROM calendarioatividadematricula WHERE");
		sqlStr.append(" matriculaperiodo = ").append(codigoMatriculaPeriodo);
		sqlStr.append(" and tipoCalendarioAtividade = ").append("'" + tipoCalendarioAtividadeMatricula + "'");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		if (rs.next()) {
			return true;
		}
		return false;
	}

	/*@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Boolean consultarPorCodigoMatriculaPeriodoPorTipoCalendarioAtividadeEnumPorCodigoOrigem(Integer codigoMatriculaPeriodo, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, String codigoOrigem, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT * FROM calendarioatividadematricula WHERE");
		sqlStr.append(" matriculaperiodo = ").append(codigoMatriculaPeriodo);
		sqlStr.append(" and tipoCalendarioAtividade = '").append(tipoCalendarioAtividadeMatricula).append("' ");
		sqlStr.append(" and codorigem = '").append(codigoOrigem).append("' ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return true;
		}
		return false;
	}*/

	

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CalendarioAtividadeMatriculaVO consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividade(Integer codigoMatriculaPeriodoTurmaDisciplina, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, TipoOrigemEnum tipoOrigem, String codOrigem, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<CalendarioAtividadeMatriculaVO>  calendarioAtividadeMatriculaVOs =   consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividades( codigoMatriculaPeriodoTurmaDisciplina,  tipoCalendarioAtividadeMatricula,  tipoOrigem,  codOrigem,  nivelMontarDados,  usuarioLogado) ;

		if (!calendarioAtividadeMatriculaVOs.isEmpty()) {
			return calendarioAtividadeMatriculaVOs.get(0);
		}
		return new CalendarioAtividadeMatriculaVO();
	}
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CalendarioAtividadeMatriculaVO consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividadeUltimo(Integer codigoMatriculaPeriodoTurmaDisciplina, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, TipoOrigemEnum tipoOrigem, String codOrigem, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<CalendarioAtividadeMatriculaVO>  calendarioAtividadeMatriculaVOs =   consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividades( codigoMatriculaPeriodoTurmaDisciplina,  tipoCalendarioAtividadeMatricula,  tipoOrigem,  codOrigem,  nivelMontarDados,  usuarioLogado) ;
		Iterator it = calendarioAtividadeMatriculaVOs.iterator();
		CalendarioAtividadeMatriculaVO  calendario  = new CalendarioAtividadeMatriculaVO();
		while(it.hasNext()) {
			calendario = (CalendarioAtividadeMatriculaVO) it.next();
        }
		
		return calendario;
	}
	
	
	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CalendarioAtividadeMatriculaVO> consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividades(Integer codigoMatriculaPeriodoTurmaDisciplina, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, TipoOrigemEnum tipoOrigem, String codOrigem, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
	
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append("SELECT * FROM calendarioatividadematricula WHERE");
		sqlStr.append(" matriculaperiodoturmadisciplina = ").append(codigoMatriculaPeriodoTurmaDisciplina);
		sqlStr.append(" and tipoCalendarioAtividade = '").append(tipoCalendarioAtividadeMatricula).append("' ");
		if(!tipoOrigem.equals(TipoOrigemEnum.NENHUM)){
			sqlStr.append(" and tipoOrigem = '").append(tipoOrigem).append("' ");
			sqlStr.append(" and codOrigem = '").append(codOrigem).append("' ");
			sqlStr.append(" order by codigo asc  "); 
		}

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		List<CalendarioAtividadeMatriculaVO>  calendarioAtividadeMatriculaVOs = new ArrayList<CalendarioAtividadeMatriculaVO>(0);
		while(rs.next()) {
			 calendarioAtividadeMatriculaVOs.add(montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return calendarioAtividadeMatriculaVOs;
	}
	

	/*
	 * 
	 * 
	 * 
	 */

	@Override
	public List<CalendarioAtividadeMatriculaVO> consultarCalendariosDoDiaPorMatriculaPeriodoTurmaDisciplina(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs, Date data) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if (data == null) {
			return new ArrayList<CalendarioAtividadeMatriculaVO>();
		}
		Date dataInicioComecaMeiaNoite = Uteis.getDateSemHora(data);
		Date dataInicioAteMeiaNoite = Uteis.getDateComHoraAntesDiaSeguinte(data);

		sqlStr.append(" select calendarioatividadematricula.codigo,  calendarioatividadematricula.descricao, calendarioatividadematricula.datainicio, calendarioatividadematricula.datafim, ");
		sqlStr.append(" calendarioatividadematricula.tipocalendarioatividade, calendarioatividadematricula.codorigem, ");
		sqlStr.append(" matriculaperiodoturmadisciplina.codigo as \"matriculaperiodoturmadisciplina.codigo\",  ");
		sqlStr.append(" matriculaperiodoturmadisciplina.conteudo as \"matriculaperiodoturmadisciplina.conteudo\",  ");
		sqlStr.append(" disciplina.codigo as \"disciplina.codigo\", disciplina.nome as \"disciplina.nome\",  ");
		sqlStr.append(" turma.codigo as \"turma.codigo\", turma.identificadorturma as \"turma.identificadorturma\"  ");
		sqlStr.append(" from calendarioatividadematricula ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on  matriculaperiodoturmadisciplina.codigo = calendarioatividadematricula.matriculaperiodoturmadisciplina ");
		sqlStr.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sqlStr.append(" where matriculaperiodoturmadisciplina in (").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(matriculaPeriodoTurmaDisciplinaVOs)).append(")");
		sqlStr.append(" and (datainicio between '").append(dataInicioComecaMeiaNoite + "'").append(" and '" + dataInicioAteMeiaNoite + "'");
		sqlStr.append(" or datafim between '").append(dataInicioComecaMeiaNoite + "'").append(" and '" + dataInicioAteMeiaNoite + "')");

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs = new ArrayList<CalendarioAtividadeMatriculaVO>(0);
		CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = null;
		while (rs.next()) {
			calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
			calendarioAtividadeMatriculaVO.setCodigo(rs.getInt("codigo"));
			calendarioAtividadeMatriculaVO.setTipoCalendarioAtividade(TipoCalendarioAtividadeMatriculaEnum.valueOf(rs.getString("tipocalendarioatividade")));
			calendarioAtividadeMatriculaVO.setCodOrigem(rs.getString("codOrigem"));
			calendarioAtividadeMatriculaVO.setDescricao(rs.getString("descricao"));
			calendarioAtividadeMatriculaVO.setDataInicio(rs.getTimestamp("datainicio"));
			calendarioAtividadeMatriculaVO.setDataFim(rs.getTimestamp("datafim"));
			calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(rs.getInt("matriculaperiodoturmadisciplina.codigo"));
			calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().setCodigo(rs.getInt("disciplina.codigo"));
			calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().setNome(rs.getString("disciplina.nome"));
			calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setCodigo(rs.getInt("turma.codigo"));
			calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
			calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().setCodigo(rs.getInt("matriculaperiodoturmadisciplina.conteudo"));
			
			calendarioAtividadeMatriculaVOs.add(calendarioAtividadeMatriculaVO);
		}

		return calendarioAtividadeMatriculaVOs;
	}
	
	@Override
	public List<CalendarioAtividadeMatriculaVO> consultarCalendariosDoDia(Integer codigoMatriculaPeriodoTurmaDisciplina, Integer codigoMatriculaPeriodo, Date data) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if (data == null) {
			return new ArrayList<CalendarioAtividadeMatriculaVO>();
		}
		Date dataInicioComecaMeiaNoite = Uteis.getDateSemHora(data);
		Date dataInicioAteMeiaNoite = Uteis.getDateComHoraAntesDiaSeguinte(data);
		
		sqlStr.append(" select codigo, tipocalendarioatividade, descricao, datainicio, datafim from calendarioatividadematricula where (matriculaperiodoturmadisciplina = ").append(codigoMatriculaPeriodoTurmaDisciplina).append(" or matriculaperiodo = ").append(codigoMatriculaPeriodo + ")");
		sqlStr.append(" and (datainicio between '").append(dataInicioComecaMeiaNoite + "'").append(" and '" + dataInicioAteMeiaNoite + "'");
		sqlStr.append(" or datafim between '").append(dataInicioComecaMeiaNoite + "'").append(" and '" + dataInicioAteMeiaNoite + "')");
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs = new ArrayList<CalendarioAtividadeMatriculaVO>(0);
		CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = null;
		while (rs.next()) {
			calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
			calendarioAtividadeMatriculaVO.setCodigo(rs.getInt("codigo"));
			calendarioAtividadeMatriculaVO.setTipoCalendarioAtividade(TipoCalendarioAtividadeMatriculaEnum.valueOf(rs.getString("tipocalendarioatividade")));
			calendarioAtividadeMatriculaVO.setDescricao(rs.getString("descricao"));
			calendarioAtividadeMatriculaVO.setDataInicio(rs.getTimestamp("datainicio"));
			calendarioAtividadeMatriculaVO.setDataFim(rs.getTimestamp("datafim"));
			calendarioAtividadeMatriculaVOs.add(calendarioAtividadeMatriculaVO);
		}
		
		return calendarioAtividadeMatriculaVOs;
	}

	/**
	 * @author Victor Hugo 11/12/2014
	 * 
	 *         Método responsável por consultar os alunos que estudam EAD.
	 * 
	 */
	@Override
	public List<CalendarioAtividadeMatriculaVO> consultarAlunosDoTutorPorTurmaCursoDisciplinaUnidadeEnsinoMonitoramentoEAD(TipoNivelProgramacaoTutoriaEnum tipoNivelProgramacaoTutoria, TurmaVO turmaVO, Integer codigoUnidadeEnsino, DisciplinaVO disciplina, Integer codigoCurso, Integer codigoProfessor, Boolean estudando, Boolean concluiram, boolean validarAno, String ano, String semestre, Integer codigoTemaAssunto, Integer codigoItemParametro, PeriodicidadeEnum periodicidade, Date dataInicio, Date dataFim, FiltroRelatorioAcademicoVO filtroAcademicoVO, MatriculaVO matriculaVO, Boolean situacaoAvaliacaoOnlineAprovado, Boolean situacaoAvaliacaoOnlineReprovado,  Boolean situacaoAvaliacaoOnlineAguardandoExecucao, Boolean situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado, Boolean situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada, Integer codigoAvaliacaoOnlineRea, Boolean situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor, Boolean situacaoAtividadeDiscursivaAguardandoRespostaAluno, Boolean situacaoAtividadeDiscursivaAvaliada, Integer codigoAtividadeDiscursiva, Boolean situacaoDuvidaTutorAguardandoRespostaProfessor, Boolean situacaoDuvidaTutorAguardandoRespostaAluno, Integer codigoTutor, Double percentualInicio, Double percentualFim, Integer limit, Integer offSet, UsuarioVO usuarioVO) throws Exception {

		if (tipoNivelProgramacaoTutoria.isTurma() && !Uteis.isAtributoPreenchido(turmaVO.getCodigo())) {
			throw new Exception(UteisJSF.internacionalizar("msg_MonitoramentoAlunosEAD_turmaDeveSerInfomrada"));
		}

		if (tipoNivelProgramacaoTutoria.isCurso() && !Uteis.isAtributoPreenchido(codigoCurso)) {
			throw new Exception(UteisJSF.internacionalizar("msg_MonitoramentoAlunosEAD_cursoDeveSerInfomrada"));
		}

		if (tipoNivelProgramacaoTutoria.isUnidadeEnsino() && !Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
			throw new Exception(UteisJSF.internacionalizar("msg_MonitoramentoAlunosEAD_unidadeEnsinoDeveSerInfomrada"));
		}

		if (!Uteis.isAtributoPreenchido(disciplina.getCodigo())) {
			throw new Exception(UteisJSF.internacionalizar("msg_MonitoramentoAlunosEAD_aDisciplinaDeveSerInformada"));
		}

		if (validarAno && !Uteis.isAtributoPreenchido(ano)) {
			throw new Exception(UteisJSF.internacionalizar("msg_MonitoramentoAlunosEAD_anoDeveSerInfomrada"));
		}

		StringBuilder sqlStr = new StringBuilder();
		if (usuarioVO.getIsApresentarVisaoAdministrativa()) {
			sqlStr.append(getQueryAlunosVisaoAdministrativa(disciplina.getCodigo()));
		} else if (usuarioVO.getIsApresentarVisaoProfessor()) {
			sqlStr.append(getQueryAlunosDoTutor(codigoProfessor));
		} else if (usuarioVO.getIsApresentarVisaoCoordenador()) {
			sqlStr.append(getQueryAlunosVisaoAdministrativa(disciplina.getCodigo()));
		}
		sqlStr.append(getSqlWereMonitoramentoAlunosEAD(tipoNivelProgramacaoTutoria, turmaVO, codigoUnidadeEnsino, disciplina.getCodigo(), codigoCurso, codigoProfessor, estudando, concluiram, validarAno, ano, semestre, codigoTemaAssunto, codigoItemParametro, periodicidade, dataInicio, dataFim, filtroAcademicoVO, matriculaVO, situacaoAvaliacaoOnlineAprovado, situacaoAvaliacaoOnlineReprovado, situacaoAvaliacaoOnlineAguardandoExecucao, situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado, situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada, codigoAvaliacaoOnlineRea, situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor, situacaoAtividadeDiscursivaAguardandoRespostaAluno, situacaoAtividadeDiscursivaAvaliada, codigoAtividadeDiscursiva, situacaoDuvidaTutorAguardandoRespostaProfessor, situacaoDuvidaTutorAguardandoRespostaAluno, codigoTutor, percentualInicio, percentualFim, usuarioVO));
					
		sqlStr.append(" order by pessoa.nome ");
		sqlStr.append(" limit ").append(limit);
		sqlStr.append(" offset ").append(offSet);
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs = new ArrayList<CalendarioAtividadeMatriculaVO>(0);
		CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = null;
		while (rs.next()) {
			calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
			calendarioAtividadeMatriculaVO.setCodigo(rs.getInt("acessoconteudoestudocodigo"));
			calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(rs.getInt("matriculaperiodoturmadisciplina"));
			calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().setMatricula(rs.getString("matriculaaluno"));
			calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().setCodigo(rs.getInt("pessoa"));
			calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatriculaObjetoVO().getAluno().setNome(rs.getString("nomealuno"));
			calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getProfessor().setNome(rs.getString("professor"));
			calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getConfiguracaoEADVO().setTipoControleLiberacaoAvaliacaoOnline(TipoControleLiberacaoAvaliacaoOnlineEnum.valueOf(rs.getString("tipocontroleliberacaoavaliacaoonline")));
			calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().setModalidadeDisciplina(ModalidadeDisciplinaEnum.valueOf(rs.getString("modalidadedisciplina")));
			if (Uteis.isAtributoPreenchido(rs.getInt("codigoconteudo"))) {
				calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().setCodigo(rs.getInt("codigoconteudo"));
			}
			if (Uteis.isAtributoPreenchido(rs.getString("descricaoconteudo"))) {
				calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().setDescricao(rs.getString("descricaoconteudo"));
			}			
			calendarioAtividadeMatriculaVO.setDataInicio(rs.getTimestamp("datainicio"));
			calendarioAtividadeMatriculaVO.setDataFim(rs.getTimestamp("datafim"));
			calendarioAtividadeMatriculaVO.setQtdeAvaliacaoRealizada(rs.getString("qtdeavaliacaorealizada"));
			calendarioAtividadeMatriculaVO.setQtdeAvaliacaoAguardandoRealizacao(rs.getString("qtdeavaliacaoaguardandorealizacao"));
			calendarioAtividadeMatriculaVO.setQtdeAtividadeDiscursivasAvaliadas(rs.getString("qtdeatividadediscursivasavaliadas"));
			calendarioAtividadeMatriculaVO.setQtdeAtividadeDiscursivasAguardando(rs.getString("qtdeatividadediscursivasaguardando"));
			calendarioAtividadeMatriculaVO.setQtdeAtividadeDiscursivasAguardandoAluno(rs.getString("qtdeatividadediscursivasaguardandoaluno"));
			calendarioAtividadeMatriculaVO.setDataInicioAcessoConteudoEstudo(rs.getDate("acessoconteudoestudodataini"));
			calendarioAtividadeMatriculaVO.setDataFimAcessoConteudoEstudo(rs.getDate("acessoconteudoestudodatafim"));
			calendarioAtividadeMatriculaVO.setQtdeDuvidasProfessor(rs.getString("qtdeduvidasprofessor"));
			calendarioAtividadeMatriculaVO.setQtdeDuvidasAguardandoRespostaProfessor(rs.getString("qtdeduvidasaguardandorespostaprofessor"));
			calendarioAtividadeMatriculaVO.setQtdeDuvidasAguardandoRespostaAluno(rs.getString("qtdeduvidasaguardandorespostaaluno"));
			calendarioAtividadeMatriculaVO.setQtdeAvaliacaoOnline(rs.getString("qtdeAvaliacaoOnline"));
			if (calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().getCodigo().equals(0)) {
				if(calendarioAtividadeMatriculaVO.getCodigo().equals(0)) {
					calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().setGraficoDesempenhoAluno("</br>O Aluno não Realizou Nenhum Acesso Ambiente do EAD.");
				}else {
					calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().setGraficoDesempenhoAluno("</br>Não Existe Conteúdo Cadastrado Para Esta Disciplina ou o Aluno não Realizou Nenhum Acesso Ambiente do EAD.");
				}
			} else {
				Map<String, Object> auxiliar = new HashMap<String, Object>();
				getFacadeFactory().getConteudoFacade().gerarCalculosDesempenhoAlunoEstudosOnline(auxiliar, calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().getCodigo(), calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getMatricula(), calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getModalidadeDisciplina(), null);
				if (Uteis.isAtributoPreenchido(auxiliar.get("percentARealizar"))) {
					calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().setPercentARealizar(Uteis.arrendondarForcando2CadasDecimais((Double) auxiliar.get("percentARealizar")));
				}
				if (Uteis.isAtributoPreenchido(auxiliar.get("percentEstudado"))) {
					calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().setPercentEstudado(Uteis.arrendondarForcando2CadasDecimais((Double) auxiliar.get("percentEstudado")));
				}
				if (Uteis.isAtributoPreenchido(auxiliar.get("percentAtrasado"))) {
					calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().setPercentAtrasado(Uteis.arrendondarForcando2CadasDecimais((Double) auxiliar.get("percentAtrasado")));
				}
			}
			
			if(calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().getPercentARealizar().equals(0.0) && calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().getPercentEstudado().equals(0.0) && calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().getPercentAtrasado().equals(0.0)) {
				calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().setPercentARealizar(100.0);
			}
			if (codigoTemaAssunto.equals(0)) {
				calendarioAtividadeMatriculaVO.setGraficoAproveitamentoAvaliacaoVO(getFacadeFactory().getGraficoAproveitamentoAvaliacaoFacade().consultarAproveitamentoAvaliacaoOnlineAlunoPorDisciplina(getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().consultarUltimaAvalicaoOnlineRealizadaPorMatriculaPeriodoTurmaDisciplina(calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo())));
			} else {
				calendarioAtividadeMatriculaVO.setGraficoAproveitamentoAvaliacaoVO(getFacadeFactory().getGraficoAproveitamentoAvaliacaoFacade().consultarAproveitamentoAvaliacaoOnlineAlunoPorAssunto(getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().consultarUltimaAvalicaoOnlineRealizadaPorMatriculaPeriodoTurmaDisciplina(calendarioAtividadeMatriculaVO.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo()), codigoTemaAssunto));
			}			

			calendarioAtividadeMatriculaVOs.add(calendarioAtividadeMatriculaVO);


			
		}

		return calendarioAtividadeMatriculaVOs;
	}
	
	public StringBuilder getSqlWereMonitoramentoAlunosEAD(TipoNivelProgramacaoTutoriaEnum tipoNivelProgramacaoTutoria, TurmaVO turmaVO, Integer codigoUnidadeEnsino, Integer disciplina, Integer codigoCurso, Integer codigoProfessor, Boolean estudando, Boolean concluiram, boolean validarAno, String ano, String semestre, Integer codigoTemaAssunto, Integer codigoItemParametro, PeriodicidadeEnum periodicidade, Date dataInicio, Date dataFim, FiltroRelatorioAcademicoVO filtroAcademicoVO, MatriculaVO matriculaVO, Boolean situacaoAvaliacaoOnlineAprovado, Boolean situacaoAvaliacaoOnlineReprovado,  Boolean situacaoAvaliacaoOnlineAguardandoExecucao, Boolean situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado, Boolean situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada, Integer codigoAvaliacaoOnlineRea, Boolean situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor, Boolean situacaoAtividadeDiscursivaAguardandoRespostaAluno, Boolean situacaoAtividadeDiscursivaAvaliada, Integer codigoAtividadeDiscursiva, Boolean situacaoDuvidaTutorAguardandoRespostaProfessor, Boolean situacaoDuvidaTutorAguardandoRespostaAluno, Integer codigoTutor, Double percentualInicio, Double percentualFim, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr =  new StringBuilder("");
		sqlStr.append(" where  1=1 ");
		sqlStr.append(" and ( disciplina.codigo in ( select ").append(disciplina);
		sqlStr.append(" union (select distinct equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.intValue()).append(") ");
		sqlStr.append(" union (select distinct disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.intValue()).append(") ");
		if (turmaVO.getCodigo() != 0 && !turmaVO.getSubturma() && turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" union (select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turmadisciplina.turma = ").append(turmaVO.getCodigo()).append(" and turmadisciplina.disciplina = ").append(disciplina).append(") ");	
		}
		sqlStr.append("))" );
		if(periodicidade.equals(PeriodicidadeEnum.INTEGRAL)) {
			sqlStr.append(" and curso.periodicidade = 'IN'");
			if(Uteis.isAtributoPreenchido(dataInicio)) {
				sqlStr.append(" and matricula.data >= '").append(dataInicio).append("'");
			}
			if(Uteis.isAtributoPreenchido(dataFim)) {
				sqlStr.append(" and matricula.data <= '").append(dataFim).append("'");
			}
			
		}else if(periodicidade.equals(PeriodicidadeEnum.ANUAL)) {
			sqlStr.append(" and curso.periodicidade = 'AN'");
		}else if(periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)) {
			sqlStr.append(" and curso.periodicidade = 'SE'");
		}
		
		
		if (turmaVO.getCodigo() != 0) {
		if (!turmaVO.getSubturma() && turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" AND (turma.codigo in (select turmaagrupada.turma from turmaagrupada ");
			sqlStr.append(" where turmaorigem = ").append(turmaVO.getCodigo()).append(")");
			sqlStr.append("or (matriculaperiodoturmadisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
			sqlStr.append("or (matriculaperiodoturmadisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			sqlStr.append(") ");
		} else if (!turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" AND turma.codigo = ").append(turmaVO.getCodigo());
		}
		
		} else if (codigoCurso != 0) {
			sqlStr.append(" and curso.codigo = ").append(codigoCurso);
		} else if (codigoUnidadeEnsino != 0) {
			sqlStr.append(" and unidadeensino.codigo = ").append(codigoUnidadeEnsino);
		}
		if (usuarioVO.getIsApresentarVisaoProfessor()) {
			sqlStr.append(" and ((turmadisciplina.definicoesTutoriaOnline = '").append(DefinicoesTutoriaOnlineEnum.DINAMICA.getName()).append("' and matriculaperiodoturmadisciplina.professor = ").append(codigoProfessor).append(") ");
			sqlStr.append(" or (turmadisciplina.definicoesTutoriaOnline is null or turmadisciplina.definicoesTutoriaOnline = '").append(DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA.getName()).append("') ");			
			sqlStr.append(" ) ");
		} else if (usuarioVO.getIsApresentarVisaoCoordenador()) {
			sqlStr.append(" and exists (select cursocoordenador.codigo from cursocoordenador inner join funcionario on funcionario.codigo = cursocoordenador.funcionario where funcionario.pessoa = ").append(usuarioVO.getPessoa().getCodigo()).append(" ");
			sqlStr.append(" and cursocoordenador.unidadeensino =  matricula.unidadeensino ");
			sqlStr.append(" and cursocoordenador.curso =  matricula.curso and cursocoordenador.tipoCoordenadorCurso in ('").append(TipoCoordenadorCursoEnum.AMBOS).append("', '").append(TipoCoordenadorCursoEnum.GERAL).append("') ");
			sqlStr.append(" and (cursocoordenador.turma is null or  cursocoordenador.turma = matriculaperiodoturmadisciplina.turma )) ");
		}
		if (estudando && !concluiram) {
			sqlStr.append(" and ( calendarioatividadematricula.codigo is null or  calendarioatividadematricula.situacaoatividade = 'NAO_CONCLUIDA') ");
		}
		if (concluiram && !estudando) {
			sqlStr.append(" and calendarioatividadematricula.situacaoatividade = 'CONCLUIDA'");
		}
		if(!periodicidade.equals(PeriodicidadeEnum.INTEGRAL)) {
			if (!ano.isEmpty()) {
				sqlStr.append(" and matriculaperiodoturmadisciplina.ano = '").append(ano + "'");
			}
			if (!semestre.isEmpty() && periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)) {
				sqlStr.append(" and matriculaperiodoturmadisciplina.semestre = '").append(semestre + "'");
			}
		}
		if (!codigoTemaAssunto.equals(0) || !codigoItemParametro.equals(0)) {
			sqlStr.append(" and (select itemParametrosMonitoramentoAvaliacaoOnline.codigo from (");
			sqlStr.append(" 		select 'GERAL' as nome, 0 as codigo, sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ACERTOU', 'CANCELADA') then 1 else  0 end) as acertou,");
			sqlStr.append(" 		sum(case when avaliacaoonlinematriculaquestao.situacaoAtividadeResposta in ('ERROU', 'NAO_RESPONDIDA') then 1 else  0 end) as errou,");
			sqlStr.append(" 		count(avaliacaoonlinematriculaquestao.codigo) as qtdequestao, avaliacaoonline.parametromonitoramentoavaliacaoonline");
			sqlStr.append(" 		from avaliacaoonlinematriculaquestao ");
			sqlStr.append(" 		inner join avaliacaoonlinematricula on avaliacaoonlinematricula.codigo = avaliacaoonlinematriculaquestao.avaliacaoonlinematricula");
			sqlStr.append(" 		inner join avaliacaoonline on avaliacaoonline.codigo = avaliacaoonlinematricula.avaliacaoonline	");

			if (!codigoTemaAssunto.equals(0)) {
				sqlStr.append(" 		inner join unidadeconteudo on unidadeconteudo.conteudo = matriculaperiodoturmadisciplina.conteudo");
				sqlStr.append(" 		inner join questao on questao.codigo = avaliacaoonlinematriculaquestao.questao");
				sqlStr.append(" 		inner join questaoassunto on questao.codigo = questaoassunto.questao and unidadeconteudo.temaassunto = questaoassunto.temaassunto");
				sqlStr.append(" 		inner join temaassunto on temaassunto.codigo = questaoassunto.temaassunto");

			}
			sqlStr.append(" 		where avaliacaoonlinematricula = (select max(aom.codigo) from avaliacaoonlinematricula aom  where   aom.situacaoAvaliacaoOnlineMatricula in ('APROVADO', 'REPROVADO') and  aom.matriculaperiodoturmadisciplina  = matriculaperiodoturmadisciplina.codigo)");
			if (!codigoTemaAssunto.equals(0)) {
				sqlStr.append(" 		and temaassunto.codigo = ").append(codigoTemaAssunto);
			}
			sqlStr.append(" 		group by avaliacaoonline.parametromonitoramentoavaliacaoonline");
			sqlStr.append(" 		) as estatistica");
			sqlStr.append(" 		inner join itemParametrosMonitoramentoAvaliacaoOnline on itemParametrosMonitoramentoAvaliacaoOnline.parametrosMonitoramentoAvaliacaoOnline = estatistica.parametromonitoramentoavaliacaoonline ");
			sqlStr.append(" 	and itemParametrosMonitoramentoAvaliacaoOnline.percentualAcertosDe <= (acertou*100/qtdequestao) and percentualAcertosAte >= (acertou*100/qtdequestao))  ");
			if (codigoItemParametro.equals(0)) {
				sqlStr.append(" is not null ");
			} else {
				sqlStr.append(" = ").append(codigoItemParametro);
			}
		}
		sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		
		if(Uteis.isAtributoPreenchido(matriculaVO.getMatricula())) {
			sqlStr.append("and matriculaperiodoturmadisciplina.matricula = '").append(matriculaVO.getMatricula()).append("'");
		}
		
		if(situacaoAvaliacaoOnlineAprovado || situacaoAvaliacaoOnlineReprovado || situacaoAvaliacaoOnlineAguardandoExecucao || situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado || situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada || Uteis.isAtributoPreenchido(codigoAvaliacaoOnlineRea)) {
			String andOrSituacaoAvaliacaoOnline = " and (";
			sqlStr.append(" and exists (select conteudounidadepaginarecursoeducacional.codigo ");
			sqlStr.append(" from conteudounidadepagina ");
			sqlStr.append(" inner join unidadeconteudo on unidadeconteudo.codigo = conteudounidadepagina.unidadeconteudo ");
			sqlStr.append(" inner join conteudounidadepaginarecursoeducacional on conteudounidadepaginarecursoeducacional.conteudounidadepagina = conteudounidadepagina.codigo ");
			sqlStr.append(" inner join avaliacaoonline on avaliacaoonline.codigo = conteudounidadepaginarecursoeducacional.avaliacaoonline ");
			sqlStr.append(" left join avaliacaoonlinematricula on avaliacaoonlinematricula.avaliacaoonline = avaliacaoonline.codigo ");
			sqlStr.append(" and avaliacaoonlinematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
			sqlStr.append(" where unidadeconteudo.conteudo = matriculaperiodoturmadisciplina.conteudo ");			
			
			if(situacaoAvaliacaoOnlineAprovado || situacaoAvaliacaoOnlineReprovado || situacaoAvaliacaoOnlineAguardandoExecucao || situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado) {
				boolean virgulaSituacaoAvaliacaoOnline = false;
				sqlStr.append(andOrSituacaoAvaliacaoOnline);
				sqlStr.append("avaliacaoonlinematricula.situacaoavaliacaoonlinematricula IN(");
				if(situacaoAvaliacaoOnlineAprovado) {
					sqlStr.append("'APROVADO'");
					virgulaSituacaoAvaliacaoOnline = true;
				}
				if(situacaoAvaliacaoOnlineReprovado) {
					if (!virgulaSituacaoAvaliacaoOnline) {
						sqlStr.append("'REPROVADO'");
					} else {
						sqlStr.append(", ").append("'REPROVADO'");
					}
					virgulaSituacaoAvaliacaoOnline = true;
				}
				if(situacaoAvaliacaoOnlineAguardandoExecucao) {
					if (!virgulaSituacaoAvaliacaoOnline) {
						sqlStr.append("'AGUARDANDO_REALIZACAO'");
					} else {
						sqlStr.append(", ").append("'AGUARDANDO_REALIZACAO'");
					}
					virgulaSituacaoAvaliacaoOnline = true;
				}
				if(situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado) {
					if (!virgulaSituacaoAvaliacaoOnline) {
						sqlStr.append("'EXPIRADO'");
					} else {
						sqlStr.append(", ").append("'EXPIRADO'");
					}
					virgulaSituacaoAvaliacaoOnline = true;
				}
				sqlStr.append(")");
				
				andOrSituacaoAvaliacaoOnline = " or ";
				
			}
			
			if(situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada) {
				sqlStr.append(andOrSituacaoAvaliacaoOnline);
				sqlStr.append(" avaliacaoonlinematricula.situacaoavaliacaoonlinematricula is null");
				if(!andOrSituacaoAvaliacaoOnline.equals(" or ")) {
					sqlStr.append("  )");
				}	
				
			}
			if(andOrSituacaoAvaliacaoOnline.equals(" or ")) {
				sqlStr.append("  )");	
			}
						
			if(Uteis.isAtributoPreenchido(codigoAvaliacaoOnlineRea)) {
				sqlStr.append(" and avaliacaoonline.codigo = ").append(codigoAvaliacaoOnlineRea);
			}
			sqlStr.append(" )"); 
		}
		if(situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor || situacaoAtividadeDiscursivaAguardandoRespostaAluno || situacaoAtividadeDiscursivaAvaliada || Uteis.isAtributoPreenchido(codigoAtividadeDiscursiva)) {
				
				sqlStr.append(" and exists (");
				sqlStr.append(" select atividadediscursiva.codigo  ");
				sqlStr.append(" from ");
				sqlStr.append(" atividadediscursiva ");
				sqlStr.append(" LEFT JOIN atividadediscursivarespostaaluno on atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
				sqlStr.append(" AND atividadediscursivarespostaaluno.atividadediscursiva = atividadediscursiva.codigo ");
				sqlStr.append(" where ((turma.turmaagrupada = false and atividadediscursiva.disciplina = matriculaperiodoturmadisciplina.disciplina) or (turma.turmaagrupada and ( ");
				sqlStr.append(" matriculaperiodoturmadisciplina.disciplina in (select equivalente from disciplinaequivalente where disciplina = atividadediscursiva.disciplina) ");
				sqlStr.append(" or atividadediscursiva.disciplina = matriculaperiodoturmadisciplina.disciplina ))) ");
				sqlStr.append(" AND atividadediscursiva.ano  = matriculaperiodoturmadisciplina.ano and atividadediscursiva.semestre  = matriculaperiodoturmadisciplina.semestre ");
				
				
				if(situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor || situacaoAtividadeDiscursivaAguardandoRespostaAluno || situacaoAtividadeDiscursivaAvaliada) {
					boolean virgulaSituacaoAtividadeDiscursiva = false;
					sqlStr.append(" and situacaorespostaatividadediscursiva IN(");
					if(situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor) {
						sqlStr.append("'AGUARDANDO_AVALIACAO_PROFESSOR'");
						virgulaSituacaoAtividadeDiscursiva = true;
					}
					if(situacaoAtividadeDiscursivaAguardandoRespostaAluno) {
						if (!virgulaSituacaoAtividadeDiscursiva) {
							sqlStr.append("'AGUARDANDO_RESPOSTA', 'AGUARDANDO_NOVA_RESPOSTA'");
						} else {
							sqlStr.append(", ").append("'AGUARDANDO_RESPOSTA', 'AGUARDANDO_NOVA_RESPOSTA'");
						}
						virgulaSituacaoAtividadeDiscursiva = true;
					}
					if(situacaoAtividadeDiscursivaAvaliada) {
						if (!virgulaSituacaoAtividadeDiscursiva) {
							sqlStr.append("'AVALIADO'");
						} else {
							sqlStr.append(", ").append("'AVALIADO'");
						}
						virgulaSituacaoAtividadeDiscursiva = true;
					}

					sqlStr.append(")");
					
				}
										
				if(Uteis.isAtributoPreenchido(codigoAtividadeDiscursiva)) {
					sqlStr.append("and atividadediscursiva.codigo = ").append(codigoAtividadeDiscursiva);
				}
				sqlStr.append(" )"); 
			}
			
			if(situacaoDuvidaTutorAguardandoRespostaProfessor || situacaoDuvidaTutorAguardandoRespostaAluno) {
								
				sqlStr.append(" and exists ( ");
				sqlStr.append(" select duvidaprofessor.codigo ");
				sqlStr.append(" from ");
				sqlStr.append(" duvidaprofessorinteracao ");
				sqlStr.append(" inner join duvidaprofessor on ");
				sqlStr.append(" duvidaprofessor.codigo = duvidaprofessorinteracao.duvidaprofessor ");
				sqlStr.append(" where");
				sqlStr.append(" duvidaprofessor.matricula = matriculaperiodoturmadisciplina.matricula");
				sqlStr.append(" and duvidaprofessor.disciplina = ").append(disciplina);

				boolean virgulaTipopessoainteracaoduvidaprofessor = false;
				StringBuilder tipopessoainteracaoduvidaprofessor = new StringBuilder();
				StringBuilder situacaoduvidaprofessor = new StringBuilder();
				
				
				if(situacaoDuvidaTutorAguardandoRespostaProfessor) {
					tipopessoainteracaoduvidaprofessor.append("'ALUNO'");
					situacaoduvidaprofessor.append("'AGUARDANDO_RESPOSTA_PROFESSOR'");
					virgulaTipopessoainteracaoduvidaprofessor = true;
				}
				if(situacaoDuvidaTutorAguardandoRespostaAluno) {
					if (!virgulaTipopessoainteracaoduvidaprofessor) {
						tipopessoainteracaoduvidaprofessor = new StringBuilder();
						situacaoduvidaprofessor = new StringBuilder();
						tipopessoainteracaoduvidaprofessor.append("'PROFESSOR'");
						situacaoduvidaprofessor.append("'AGUARDANDO_RESPOSTA_ALUNO'"); 
					} else {
						tipopessoainteracaoduvidaprofessor.append(", 'PROFESSOR'");
						situacaoduvidaprofessor.append(", 'AGUARDANDO_RESPOSTA_ALUNO'");
					}

				}		

				sqlStr.append(" and tipopessoainteracaoduvidaprofessor in ( ").append(tipopessoainteracaoduvidaprofessor).append(")");
				sqlStr.append(" and duvidaprofessor.situacaoduvidaprofessor in( ").append(situacaoduvidaprofessor).append(")");
				
				sqlStr.append(" )"); 		
													
			}
			
			if(Uteis.isAtributoPreenchido(codigoTutor)) {
				sqlStr.append(" and professor.codigo = ").append(codigoTutor);
			}
			
			if(Uteis.isAtributoPreenchido(percentualInicio) || Uteis.isAtributoPreenchido(percentualFim)) {
				sqlStr.append(" and  (( ");
				sqlStr.append(" select case when conteudo.pontototal > 0 then ((coalesce(registroacesso.pontototal, 0.0) * 100.0) /conteudo.pontototal)::numeric(20,2) ");
				sqlStr.append(" else ((coalesce(registroacesso.totalpagina, 0.0) * 100.0) /conteudo.totalpagina)::numeric(20,2) end  as percentualacesso ");
				sqlStr.append(" from ( ");
				sqlStr.append(" select matriculaperiodoturmadisciplina.conteudo as conteudo, sum(ponto) as pontototal, count(conteudounidadepagina.codigo) as totalpagina from conteudounidadepagina ");
				sqlStr.append(" inner join unidadeconteudo on unidadeconteudo.codigo = conteudounidadepagina.unidadeconteudo ");
				sqlStr.append(" where unidadeconteudo.conteudo = matriculaperiodoturmadisciplina.conteudo ");
				sqlStr.append(" ) as conteudo, ");
				sqlStr.append(" ( ");
				sqlStr.append(" select matriculaperiodoturmadisciplina.conteudo as conteudo, sum(ponto) as pontototal, count(conteudounidadepagina.codigo) as totalpagina from conteudounidadepagina ");
				sqlStr.append(" where codigo in ( ");
				sqlStr.append(" select distinct ConteudoRegistroAcesso.conteudoUnidadePagina from ConteudoRegistroAcesso ");
				sqlStr.append(" where ConteudoRegistroAcesso.conteudo = matriculaperiodoturmadisciplina.conteudo ");
				sqlStr.append(" and matriculaperiodoturmadisciplina.codigo = ConteudoRegistroAcesso.matriculaperiodoturmadisciplina");
				sqlStr.append(" ) ");
				sqlStr.append(" ) as registroacesso ");
				sqlStr.append(" where registroacesso.conteudo = conteudo.conteudo ");
				sqlStr.append(" ) ");
				if(!Uteis.isAtributoPreenchido(percentualInicio)) {
					sqlStr.append("between 0 and ").append(percentualFim);
				}
				else if(!Uteis.isAtributoPreenchido(percentualFim)) {
					sqlStr.append("between ").append(percentualInicio).append(" and 100");
				}
				else {
					sqlStr.append("between ").append(percentualInicio).append(" and ").append(percentualFim);
				}
				if(!Uteis.isAtributoPreenchido(percentualInicio)) {
					sqlStr.append(" or calendarioatividadematricula.codigo is null ");
				}
				sqlStr.append(" ) ");
				
			}
			return sqlStr;
	}
	@Override
	public Integer consultarTotalResgistro(TipoNivelProgramacaoTutoriaEnum tipoNivelProgramacaoTutoria, TurmaVO turmaVO, Integer codigoUnidadeEnsino, Integer codigoDisciplina, Integer codigoCurso, Integer codigoProfessor, Boolean estudando, Boolean concluiram, boolean validarAno, String ano, String semestre, Integer codigoTemaAssunto, Integer codigoItemParametro, PeriodicidadeEnum periodicidade, Date dataInicio, Date dataFim, FiltroRelatorioAcademicoVO filtroAcademicoVO, MatriculaVO matriculaVO, Boolean situacaoAvaliacaoOnlineAprovado, Boolean situacaoAvaliacaoOnlineReprovado,  Boolean situacaoAvaliacaoOnlineAguardandoExecucao, Boolean situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado, Boolean situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada, Integer codigoAvaliacaoOnlineRea, Boolean situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor, Boolean situacaoAtividadeDiscursivaAguardandoRespostaAluno, Boolean situacaoAtividadeDiscursivaAvaliada, Integer codigoAtividadeDiscursiva, Boolean situacaoDuvidaTutorAguardandoRespostaProfessor, Boolean situacaoDuvidaTutorAguardandoRespostaAluno, Integer codigoTutor, Double percentualInicio, Double percentualFim, UsuarioVO usuarioVO) throws Exception {

		if (tipoNivelProgramacaoTutoria.isTurma() && !Uteis.isAtributoPreenchido(turmaVO.getCodigo())) {
			throw new Exception(UteisJSF.internacionalizar("msg_MonitoramentoAlunosEAD_turmaDeveSerInfomrada"));
		}

		if (tipoNivelProgramacaoTutoria.isCurso() && !Uteis.isAtributoPreenchido(codigoCurso)) {
			throw new Exception(UteisJSF.internacionalizar("msg_MonitoramentoAlunosEAD_cursoDeveSerInfomrada"));
		}

		if (tipoNivelProgramacaoTutoria.isUnidadeEnsino() && !Uteis.isAtributoPreenchido(codigoUnidadeEnsino)) {
			throw new Exception(UteisJSF.internacionalizar("msg_MonitoramentoAlunosEAD_unidadeEnsinoDeveSerInfomrada"));
		}

		if (!Uteis.isAtributoPreenchido(codigoDisciplina)) {
			throw new Exception(UteisJSF.internacionalizar("msg_MonitoramentoAlunosEAD_aDisciplinaDeveSerInformada"));
		}

		if (validarAno && !Uteis.isAtributoPreenchido(ano)) {
			throw new Exception(UteisJSF.internacionalizar("msg_MonitoramentoAlunosEAD_anoDeveSerInfomrada"));
		}

		StringBuilder sqlStr = getQueryTotalResgistro();

		sqlStr.append(getSqlWereMonitoramentoAlunosEAD(tipoNivelProgramacaoTutoria, turmaVO, codigoUnidadeEnsino, codigoDisciplina, codigoCurso, codigoProfessor, estudando, concluiram, validarAno, ano, semestre, codigoTemaAssunto, codigoItemParametro, periodicidade, dataInicio, dataFim, filtroAcademicoVO, matriculaVO, situacaoAvaliacaoOnlineAprovado, situacaoAvaliacaoOnlineReprovado, situacaoAvaliacaoOnlineAguardandoExecucao, situacaoAvaliacaoOnlinePeriodoRealizacaoEncerrado, situacaoAvaliacaoOnlineAvaliacaoOnlineNaoGerada, codigoAvaliacaoOnlineRea, situacaoAtividadeDiscursivaAguardandoAvaliacaoProfessor, situacaoAtividadeDiscursivaAguardandoRespostaAluno, situacaoAtividadeDiscursivaAvaliada, codigoAtividadeDiscursiva, situacaoDuvidaTutorAguardandoRespostaProfessor, situacaoDuvidaTutorAguardandoRespostaAluno, codigoTutor, percentualInicio, percentualFim, usuarioVO));
			
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}

	public StringBuilder getQueryAlunosDoTutor(Integer codigoProfessor) {
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" select distinct matriculaperiodoturmadisciplina.matricula as matriculaaluno, matriculaperiodoturmadisciplina.modalidadedisciplina as modalidadedisciplina ,configuracaoead.tipocontroleliberacaoavaliacaoonline as tipocontroleliberacaoavaliacaoonline,");
		sqlStr.append(" pessoa.nome as nomealuno, pessoa.codigo as pessoa, matriculaperiodoturmadisciplina.conteudo as codigoconteudo, calendarioatividadematricula.datainicio,");
		sqlStr.append(" calendarioatividadematricula.datafim, matriculaperiodoturmadisciplina.codigo as matriculaperiodoturmadisciplina,");
		sqlStr.append(" calendarioatividadematricula.tipocalendarioatividade,");
		sqlStr.append(" calendarioatividadematricula.datainicio as acessoconteudoestudodataini, calendarioatividadematricula.datafim as acessoconteudoestudodatafim, calendarioatividadematricula.codigo as acessoconteudoestudocodigo,");
		sqlStr.append(" (select count(aval.codigo)||' Realizada(s)' from calendarioatividadematricula aval where aval.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sqlStr.append(" and aval.tipocalendarioatividade in ('PERIODO_REALIZACAO_AVALIACAO_ONLINE', 'PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA' ) and aval.situacaoatividade = 'CONCLUIDA'  ) as qtdeavaliacaorealizada,");
		sqlStr.append(" (select 'Próxima '||to_char(aval.datainicio,'DD/MM/YYYY HH:mm')||' - '||to_char(aval.datafim,'DD/MM/YYYY HH:mm')  from calendarioatividadematricula aval where aval.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sqlStr.append(" and aval.tipocalendarioatividade in ('PERIODO_REALIZACAO_AVALIACAO_ONLINE', 'PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA' ) and aval.situacaoatividade = 'NAO_CONCLUIDA' limit 1 ) as qtdeavaliacaoaguardandorealizacao, ");
		
		sqlStr.append(" (select count(atividadediscursivarespostaaluno.codigo)||' Avaliadas' from atividadediscursivarespostaaluno ");
		sqlStr.append(" where atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina =  matriculaperiodoturmadisciplina.codigo ");		
		sqlStr.append(" and ((turmadisciplina.definicoesTutoriaOnline = '").append(DefinicoesTutoriaOnlineEnum.DINAMICA.getName()).append("' and matriculaperiodoturmadisciplina.professor = ").append(codigoProfessor).append(") ");
		sqlStr.append(" or (turmadisciplina.definicoesTutoriaOnline is null or turmadisciplina.definicoesTutoriaOnline = '").append(DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA.getName()).append("') ");			
		sqlStr.append(" ) ");
		sqlStr.append(" and situacaorespostaatividadediscursiva = 'AVALIADO') as qtdeatividadediscursivasavaliadas, ");

		sqlStr.append(" (select count(atividadediscursivarespostaaluno.codigo)||' Aguardando Avaliação Professor' from atividadediscursivarespostaaluno ");
		sqlStr.append(" where atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina =  matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" and ((turmadisciplina.definicoesTutoriaOnline = '").append(DefinicoesTutoriaOnlineEnum.DINAMICA.getName()).append("' and matriculaperiodoturmadisciplina.professor = ").append(codigoProfessor).append(") ");
		sqlStr.append(" or (turmadisciplina.definicoesTutoriaOnline is null or turmadisciplina.definicoesTutoriaOnline = '").append(DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA.getName()).append("') ");			
		sqlStr.append(" ) ");
		sqlStr.append(" and situacaorespostaatividadediscursiva = 'AGUARDANDO_AVALIACAO_PROFESSOR') as qtdeatividadediscursivasaguardando,");

		sqlStr.append(" (select count(atividadediscursivarespostaaluno.codigo)||' Aguardando Resposta Aluno' from atividadediscursivarespostaaluno ");
		sqlStr.append(" where atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina =  matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" and ((turmadisciplina.definicoesTutoriaOnline = '").append(DefinicoesTutoriaOnlineEnum.DINAMICA.getName()).append("' and matriculaperiodoturmadisciplina.professor = ").append(codigoProfessor).append(") ");
		sqlStr.append(" or (turmadisciplina.definicoesTutoriaOnline is null or turmadisciplina.definicoesTutoriaOnline = '").append(DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA.getName()).append("') ");			
		sqlStr.append(" ) ");
		
		sqlStr.append(" and situacaorespostaatividadediscursiva in ('AGUARDANDO_RESPOSTA', 'AGUARDANDO_NOVA_RESPOSTA')) as qtdeatividadediscursivasaguardandoAluno,");

		sqlStr.append(" (select count(distinct duvidaprofessor.codigo) from duvidaprofessorinteracao");
		sqlStr.append(" inner join duvidaprofessor on duvidaprofessor.codigo = duvidaprofessorinteracao.duvidaprofessor");
		sqlStr.append(" where duvidaprofessor.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = duvidaprofessor.disciplina");
		sqlStr.append(" and ((turmadisciplina.definicoesTutoriaOnline = '").append(DefinicoesTutoriaOnlineEnum.DINAMICA.getName()).append("' and matriculaperiodoturmadisciplina.professor = ").append(codigoProfessor).append(") ");
		sqlStr.append(" or (turmadisciplina.definicoesTutoriaOnline is null or turmadisciplina.definicoesTutoriaOnline = '").append(DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA.getName()).append("') ");			
		sqlStr.append(" ) ");
		sqlStr.append(" and tipopessoainteracaoduvidaprofessor = 'ALUNO'");
		sqlStr.append(" and duvidaprofessor.situacaoduvidaprofessor = 'AGUARDANDO_RESPOSTA_PROFESSOR'");
		sqlStr.append(" ) as qtdeduvidasaguardandorespostaprofessor,");
		sqlStr.append(" (select count(duvidaprofessor.codigo) from duvidaprofessor");
		sqlStr.append(" where duvidaprofessor.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = disciplina.codigo");
		sqlStr.append(" and ((turmadisciplina.definicoesTutoriaOnline = '").append(DefinicoesTutoriaOnlineEnum.DINAMICA.getName()).append("' and matriculaperiodoturmadisciplina.professor = ").append(codigoProfessor).append(") ");
		sqlStr.append(" or (turmadisciplina.definicoesTutoriaOnline is null or turmadisciplina.definicoesTutoriaOnline = '").append(DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA.getName()).append("') ");			
		sqlStr.append(" ) ");
		sqlStr.append(" ) as qtdeduvidasprofessor,");
		sqlStr.append(" (select count(distinct duvidaprofessor.codigo) from duvidaprofessorinteracao");
		sqlStr.append(" inner join duvidaprofessor on duvidaprofessor.codigo = duvidaprofessorinteracao.duvidaprofessor");
		sqlStr.append(" where duvidaprofessor.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" and matriculaperiodoturmadisciplina.disciplina = duvidaprofessor.disciplina");
		sqlStr.append(" and ((turmadisciplina.definicoesTutoriaOnline = '").append(DefinicoesTutoriaOnlineEnum.DINAMICA.getName()).append("' and matriculaperiodoturmadisciplina.professor = ").append(codigoProfessor).append(") ");
		sqlStr.append(" or (turmadisciplina.definicoesTutoriaOnline is null or turmadisciplina.definicoesTutoriaOnline = '").append(DefinicoesTutoriaOnlineEnum.PROGRAMACAO_DE_AULA.getName()).append("') ");			
		sqlStr.append(" ) ");
		sqlStr.append(" and tipopessoainteracaoduvidaprofessor = 'PROFESSOR'");
		sqlStr.append(" and duvidaprofessor.situacaoduvidaprofessor = 'AGUARDANDO_RESPOSTA_ALUNO'");
		sqlStr.append(" ) as qtdeduvidasaguardandorespostaaluno, ");
		
		sqlStr.append(" (select count(a.codigo)  from avaliacaoonlinematricula as a where a.matriculaperiodoturmadisciplina  = matriculaperiodoturmadisciplina.codigo) as qtdeAvaliacaoOnline, ");
		sqlStr.append(" (select array_to_string(array_agg('REA: '||conteudounidadepaginarecursoeducacional.titulo || ' - situação: '|| ");
		sqlStr.append(" case when avaliacaoonlinematricula.codigo is null then 'Avaliação On-line Não Gerada' else avaliacaoonlinematricula.situacaoavaliacaoonlinematricula end ");
		sqlStr.append(" order by unidadeconteudo.ordem, conteudounidadepagina.pagina), '</br> ') ");
		sqlStr.append(" from conteudounidadepagina ");
		sqlStr.append(" inner join unidadeconteudo on unidadeconteudo.codigo = conteudounidadepagina.unidadeconteudo ");
		sqlStr.append(" inner join conteudounidadepaginarecursoeducacional on conteudounidadepaginarecursoeducacional.conteudounidadepagina = conteudounidadepagina.codigo ");
		sqlStr.append(" inner join avaliacaoonline on avaliacaoonline.codigo = conteudounidadepaginarecursoeducacional.avaliacaoonline ");
		sqlStr.append(" left join avaliacaoonlinematricula on avaliacaoonlinematricula.avaliacaoonline = avaliacaoonline.codigo ");
		sqlStr.append(" and avaliacaoonlinematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" and avaliacaoonlinematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" where unidadeconteudo.conteudo = matriculaperiodoturmadisciplina.conteudo ");
		sqlStr.append(" ) as avaliacaoOnlineReas, ");
		sqlStr.append(" professor.nome as professor, ");	
		sqlStr.append(" matriculaperiodoturmadisciplina.conteudo, ");
		sqlStr.append(" conteudo.descricao as descricaoconteudo ");
		
		sqlStr.append(" from matriculaperiodoturmadisciplina ");
		sqlStr.append(" left join calendarioatividadematricula on matriculaperiodoturmadisciplina.codigo = calendarioatividadematricula.matriculaperiodoturmadisciplina and calendarioatividadematricula.tipocalendarioatividade = 'ACESSO_CONTEUDO_ESTUDO'	");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" inner join historico on matricula.matricula = historico.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" left join pessoa as professor on professor.codigo = matriculaperiodoturmadisciplina.professor ");	
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sqlStr.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina ");
		sqlStr.append(" inner join configuracaoead on configuracaoead.codigo = turma.configuracaoead");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sqlStr.append(" left join conteudo on matriculaperiodoturmadisciplina.conteudo = conteudo.codigo ");

		return sqlStr;
	}

	public StringBuilder getQueryAlunosVisaoAdministrativa(Integer codigoDisciplina) {
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append(" select distinct matriculaperiodoturmadisciplina.matricula as matriculaaluno, matriculaperiodoturmadisciplina.modalidadedisciplina as modalidadedisciplina ,configuracaoead.tipocontroleliberacaoavaliacaoonline as tipocontroleliberacaoavaliacaoonline,");
		sqlStr.append(" pessoa.nome as nomealuno, pessoa.codigo as pessoa, matriculaperiodoturmadisciplina.conteudo as codigoconteudo, calendarioatividadematricula.datainicio,");
		sqlStr.append(" calendarioatividadematricula.datafim, matriculaperiodoturmadisciplina.codigo as matriculaperiodoturmadisciplina,");
		sqlStr.append(" calendarioatividadematricula.tipocalendarioatividade,");
		sqlStr.append(" calendarioatividadematricula.datainicio as acessoconteudoestudodataini, calendarioatividadematricula.datafim as acessoconteudoestudodatafim, calendarioatividadematricula.codigo as acessoconteudoestudocodigo,");
		sqlStr.append(" (select count(aval.codigo)||' Realizada(s)' from calendarioatividadematricula aval where aval.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sqlStr.append(" and aval.tipocalendarioatividade in ('PERIODO_REALIZACAO_AVALIACAO_ONLINE', 'PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA' ) and aval.situacaoatividade = 'CONCLUIDA'  ) as qtdeavaliacaorealizada,");
		sqlStr.append(" (select 'Próxima Avaliação '||to_char(aval.datainicio,'DD/MM/YYYY HH:mm')||' - '||to_char(aval.datafim,'DD/MM/YYYY HH:mm')  from calendarioatividadematricula aval where aval.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sqlStr.append(" and aval.tipocalendarioatividade in ('PERIODO_REALIZACAO_AVALIACAO_ONLINE', 'PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA' ) and aval.situacaoatividade = 'NAO_CONCLUIDA' limit 1 ) as qtdeavaliacaoaguardandorealizacao, ");
		sqlStr.append(" (select count(atividadediscursivarespostaaluno.codigo)||' Avaliadas' from atividadediscursivarespostaaluno ");
		sqlStr.append(" where atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina =  matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" and situacaorespostaatividadediscursiva = 'AVALIADO') as qtdeatividadediscursivasavaliadas, ");

		sqlStr.append(" (select count(atividadediscursivarespostaaluno.codigo)||' Aguardando Avaliação Professor' from atividadediscursivarespostaaluno ");
		sqlStr.append(" where atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina =  matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" and situacaorespostaatividadediscursiva = 'AGUARDANDO_AVALIACAO_PROFESSOR') as qtdeatividadediscursivasaguardando,");

		sqlStr.append(" (select count(atividadediscursivarespostaaluno.codigo)||' Aguardando Resposta Aluno' from atividadediscursivarespostaaluno ");
		sqlStr.append(" where atividadediscursivarespostaaluno.matriculaperiodoturmadisciplina =  matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" and situacaorespostaatividadediscursiva in ('AGUARDANDO_RESPOSTA', 'AGUARDANDO_NOVA_RESPOSTA')) as qtdeatividadediscursivasaguardandoAluno,");

		sqlStr.append(" (select count(distinct duvidaprofessor.codigo) from duvidaprofessorinteracao");
		sqlStr.append(" inner join duvidaprofessor on duvidaprofessor.codigo = duvidaprofessorinteracao.duvidaprofessor");
		sqlStr.append(" where duvidaprofessor.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" and duvidaprofessor.disciplina = ").append(codigoDisciplina);
		sqlStr.append(" and tipopessoainteracaoduvidaprofessor = 'ALUNO'");
		sqlStr.append(" and duvidaprofessor.situacaoduvidaprofessor = 'AGUARDANDO_RESPOSTA_PROFESSOR'");
		sqlStr.append(" ) as qtdeduvidasaguardandorespostaprofessor,");
		sqlStr.append(" (select count(duvidaprofessor.codigo) from duvidaprofessor");
		sqlStr.append(" where duvidaprofessor.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" and duvidaprofessor.disciplina = ").append(codigoDisciplina);
		sqlStr.append(" ) as qtdeduvidasprofessor,");
		sqlStr.append(" (select count(distinct duvidaprofessor.codigo) from duvidaprofessorinteracao");
		sqlStr.append(" inner join duvidaprofessor on duvidaprofessor.codigo = duvidaprofessorinteracao.duvidaprofessor");
		sqlStr.append(" where duvidaprofessor.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" and duvidaprofessor.disciplina = ").append(codigoDisciplina);
		sqlStr.append(" and tipopessoainteracaoduvidaprofessor = 'PROFESSOR'");
		sqlStr.append(" and duvidaprofessor.situacaoduvidaprofessor = 'AGUARDANDO_RESPOSTA_ALUNO'");
		sqlStr.append(" ) as qtdeduvidasaguardandorespostaaluno, ");
		sqlStr.append(" (select count(a.codigo)  from avaliacaoonlinematricula as a where a.matriculaperiodoturmadisciplina  = matriculaperiodoturmadisciplina.codigo) as qtdeAvaliacaoOnline, ");
		sqlStr.append(" (select array_to_string(array_agg('REA: '||conteudounidadepaginarecursoeducacional.titulo || ' - situação: '|| ");
		sqlStr.append(" case when avaliacaoonlinematricula.codigo is null then 'Avaliação On-line Não Gerada' else avaliacaoonlinematricula.situacaoavaliacaoonlinematricula end ");
		sqlStr.append(" order by unidadeconteudo.ordem, conteudounidadepagina.pagina), '</br> ') ");
		sqlStr.append(" from conteudounidadepagina ");
		sqlStr.append(" inner join unidadeconteudo on unidadeconteudo.codigo = conteudounidadepagina.unidadeconteudo ");
		sqlStr.append(" inner join conteudounidadepaginarecursoeducacional on conteudounidadepaginarecursoeducacional.conteudounidadepagina = conteudounidadepagina.codigo ");
		sqlStr.append(" inner join avaliacaoonline on avaliacaoonline.codigo = conteudounidadepaginarecursoeducacional.avaliacaoonline ");
		sqlStr.append(" left join avaliacaoonlinematricula on avaliacaoonlinematricula.avaliacaoonline = avaliacaoonline.codigo ");
		sqlStr.append(" and avaliacaoonlinematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" and avaliacaoonlinematricula.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" where unidadeconteudo.conteudo = matriculaperiodoturmadisciplina.conteudo ");
		sqlStr.append(" ) as avaliacaoOnlineReas, ");
		sqlStr.append(" professor.nome as professor, ");	
		sqlStr.append(" matriculaperiodoturmadisciplina.conteudo, ");
		sqlStr.append(" conteudo.descricao as descricaoconteudo ");
		sqlStr.append(" from matriculaperiodoturmadisciplina ");
		sqlStr.append(" left join calendarioatividadematricula on matriculaperiodoturmadisciplina.codigo = calendarioatividadematricula.matriculaperiodoturmadisciplina and calendarioatividadematricula.tipocalendarioatividade = 'ACESSO_CONTEUDO_ESTUDO'	");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" inner join historico on matricula.matricula = historico.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" left join pessoa as professor on professor.codigo = matriculaperiodoturmadisciplina.professor ");		
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sqlStr.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" inner join configuracaoead on configuracaoead.codigo = turma.configuracaoead");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sqlStr.append(" left join conteudo on matriculaperiodoturmadisciplina.conteudo = conteudo.codigo ");

		return sqlStr;
	}
	
	public StringBuilder getQueryTotalResgistro() {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select count(*) as qtde ");
		sqlStr.append(" from matriculaperiodoturmadisciplina");
		sqlStr.append(" left join calendarioatividadematricula on matriculaperiodoturmadisciplina.codigo = calendarioatividadematricula.matriculaperiodoturmadisciplina and calendarioatividadematricula.tipocalendarioatividade = 'ACESSO_CONTEUDO_ESTUDO' ");
		sqlStr.append(" inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
		sqlStr.append(" inner join historico on matricula.matricula = historico.matricula and historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
		sqlStr.append(" left join pessoa as professor on professor.codigo = matriculaperiodoturmadisciplina.professor ");	
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sqlStr.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma");
		sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = matriculaperiodoturmadisciplina.turma and turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" inner join configuracaoead on configuracaoead.codigo = turma.configuracaoead");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino");
		sqlStr.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
		sqlStr.append(" left join conteudo on matriculaperiodoturmadisciplina.conteudo = conteudo.codigo ");
		return sqlStr;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoCalendarioAtividadeMatriculaQuandoPrazoEncerrado() throws Exception {
		final String sql = "update calendarioatividadematricula set situacaoatividade = 'CONCLUIDA', finalizadoautomaticamente = 'TRUE' where situacaoatividade = 'NAO_CONCLUIDA' and datafim < current_date";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);

				return sqlAlterar;
			}
		});
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void executarAntecipacaoOuProrrogacaoCalendarioAtividadeMatricula(List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs, String tipoOperacao, String tipoDataAlteracao, String tipoContagem, Integer nrDias, boolean movimentarPeriodoAutomaticamente,  UnidadeEnsinoVO unidadeEnsinoLogado, Date dataInicio, Date dataTermino, UsuarioVO usuarioVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(tipoOperacao)) {
			throw new Exception(UteisJSF.internacionalizar("msg_CalendarioMatriculaAtividade_tipoOperacao"));
		}
		if (!tipoOperacao.equals("periodoEspecifico")  && nrDias == 0) {
			throw new Exception(UteisJSF.internacionalizar("msg_CalendarioMatriculaAtividade_informeAQuantidadeDeDias"));
		}
		if (tipoOperacao.equals("periodoEspecifico")) {
			if(!Uteis.isAtributoPreenchido(dataInicio)) {
				throw new Exception(UteisJSF.internacionalizar("msg_CalendarioMatriculaAtividade_dataInicio"));
			}
			if(!Uteis.isAtributoPreenchido(dataTermino)) {
				throw new Exception(UteisJSF.internacionalizar("msg_CalendarioMatriculaAtividade_dataFinal"));
			}
			if(dataInicio.compareTo(dataTermino) > 0) {
				throw new Exception(UteisJSF.internacionalizar("msg_CalendarioMatriculaAtividade_dataInicioAnteriorFinal"));
			}
		}
		if (tipoOperacao.equals("antecipar")) {
			nrDias = (nrDias - (nrDias * 2));
		}
				
		for (CalendarioAtividadeMatriculaVO object : calendarioAtividadeMatriculaVOs) {
			if (object.getSelecionarAtividade()) {
				if(tipoOperacao.equals("periodoEspecifico")) {
					object.setDataInicio(dataInicio);
					object.setDataFim(dataTermino);
				}else {
					
				if (tipoDataAlteracao.equals("inicio")) {
					object.setDataInicio(realizarAlteracaoPeriodoCalendarioAtividadeMatricula(object.getDataInicio(), tipoContagem, nrDias, unidadeEnsinoLogado));
					if(movimentarPeriodoAutomaticamente){
						object.setDataFim(realizarAlteracaoPeriodoCalendarioAtividadeMatricula(object.getDataFim(), tipoContagem, nrDias, unidadeEnsinoLogado));
					}
				} else {
					object.setDataFim(realizarAlteracaoPeriodoCalendarioAtividadeMatricula(object.getDataFim(), tipoContagem, nrDias, unidadeEnsinoLogado));
					if(movimentarPeriodoAutomaticamente){
						object.setDataInicio(realizarAlteracaoPeriodoCalendarioAtividadeMatricula(object.getDataInicio(), tipoContagem, nrDias, unidadeEnsinoLogado));
					}
				}
				}
				realizarAlteracaoSituacaoCalendarioAtividadeMatricula(object);
				object.setNrVezesProrrogado(object.getNrVezesProrrogado() + 1);
				alterarDataCalendarioAtividadeMatricula(object, false, usuarioVO);
				if (object.getTipoOrigem().equals(TipoOrigemEnum.ATIVIDADE_DISCURSIVA)) {
					
					AtividadeDiscursivaRespostaAlunoVO atividadeDiscursivaRespostaAlunoVO = new AtividadeDiscursivaRespostaAlunoVO();
					
					atividadeDiscursivaRespostaAlunoVO = (getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().consultarAtividadeRespostaAlunoPorCodAtividadediscursivaMatriculaperiodoturmadisciplina(object.getCodOrigem(), object.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), usuarioVO));
					
					if (Uteis.isAtributoPreenchido(atividadeDiscursivaRespostaAlunoVO)) {
						
						atividadeDiscursivaRespostaAlunoVO.setDataInicioAtividade(object.getDataInicio());
						atividadeDiscursivaRespostaAlunoVO.setDataLimiteEntrega(object.getDataFim());
						
						getFacadeFactory().getAtividadeDiscursivaRespostaAlunoInterfaceFacade().atualizarPeriodoAtividadeDiscursivaRespostaAlunoPorCodigo(atividadeDiscursivaRespostaAlunoVO, usuarioVO);
					}
				}
			}
		}
	}
	
	public Date realizarAlteracaoPeriodoCalendarioAtividadeMatricula(Date data, String tipoContagem, Integer nrDias,  UnidadeEnsinoVO unidadeEnsinoLogado) throws Exception{
		if(data == null) {
			data =  new Date();
		}
		if (tipoContagem.equals("diasCorridos")) {
			return UteisData.obterDataFuturaUsandoCalendar(data, nrDias);
		} else {
			return getFacadeFactory().getFeriadoFacade().obterDataFuturaOuRetroativaApenasDiasUteis(data, nrDias, unidadeEnsinoLogado.getCidade().getCodigo(), false, false, ConsiderarFeriadoEnum.NENHUM);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDataCalendarioAtividadeMatricula(final CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final String sql = " UPDATE calendarioatividadematricula set datainicio = ?, datafim = ?, situacaoAtividade=?, nrvezesperiodoprorrogado = ?  WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql);
					sqlAlterar.setTimestamp(1, Uteis.getDataJDBCTimestamp(calendarioAtividadeMatriculaVO.getDataInicio()));
					sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(calendarioAtividadeMatriculaVO.getDataFim()));
					sqlAlterar.setString(3, calendarioAtividadeMatriculaVO.getSituacaoAtividade().name());
					sqlAlterar.setInt(4, calendarioAtividadeMatriculaVO.getNrVezesProrrogado());
					sqlAlterar.setInt(5, calendarioAtividadeMatriculaVO.getCodigo());

					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<CalendarioAtividadeMatriculaVO> consultarPorCodigoMatriculaPerido(Integer codigoMatriculaPeriodo, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select * from calendarioatividadematricula ");
		sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = calendarioatividadematricula.matriculaperiodoturmadisciplina");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
		sqlStr.append(" where calendarioatividadematricula.tipocalendarioatividade = 'ACESSO_CONTEUDO_ESTUDO'");
		sqlStr.append(" and matriculaperiodo.codigo = ").append(codigoMatriculaPeriodo);

		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(rs, nivelMontarDados, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CalendarioAtividadeMatriculaVO consultarPorCodigoMatriculaPeriodoTurmaDisciplinaTipoCalendarioAtividade(Integer codigoMatriculaPeriodoTurmaDisciplina, TipoCalendarioAtividadeMatriculaEnum tipoCalendarioAtividadeMatricula, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();

		sqlStr.append("SELECT * FROM calendarioatividadematricula WHERE");
		sqlStr.append(" matriculaperiodoturmadisciplina = ").append(codigoMatriculaPeriodoTurmaDisciplina);
		sqlStr.append(" and tipoCalendarioAtividade = ").append("'" + tipoCalendarioAtividadeMatricula + "'");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		if (rs.next()) {
			return montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado);
		}
		return new CalendarioAtividadeMatriculaVO();
	}

	@Override
	public List<DataEventosRSVO> consultarCalendariosEADAplicativo(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT datainicio, datafim from calendarioatividadematricula  ");
		sqlStr.append(" LEFT JOIN matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = calendarioatividadematricula.matriculaperiodoturmadisciplina");
		sqlStr.append(" where matriculaperiodoturmadisciplina in (").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(matriculaPeriodoTurmaDisciplinaVOs)).append(")");
		sqlStr.append(" and datainicio is not null and datafim is not null ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<DataEventosRSVO> dataEventosRSVOs = new ArrayList<>();
		DataEventosRSVO dataEventosRSVO = null;
		while (rs.next()) {
			Calendar data = Calendar.getInstance();
			data.setTime(rs.getDate("datainicio"));
			dataEventosRSVO = new DataEventosRSVO();
			dataEventosRSVO.setAno(data.get(Calendar.YEAR));
			dataEventosRSVO.setMes(data.get(Calendar.MONTH));
			dataEventosRSVO.setDia(data.get(Calendar.DAY_OF_MONTH));
			dataEventosRSVO.setColor("#b8b8b8");
			dataEventosRSVO.setTextColor("#000000");
			dataEventosRSVO.setStyleClass("horarioRegistroLancado");
			dataEventosRSVO.setData(rs.getDate("datainicio"));
			dataEventosRSVOs.add(dataEventosRSVO);
			dataEventosRSVO = new DataEventosRSVO();
			data.setTime(rs.getDate("datafim"));
			dataEventosRSVO = new DataEventosRSVO();
			dataEventosRSVO.setAno(data.get(Calendar.YEAR));
			dataEventosRSVO.setMes(data.get(Calendar.MONTH));
			dataEventosRSVO.setDia(data.get(Calendar.DAY_OF_MONTH));
			dataEventosRSVO.setColor("#b8b8b8");
			dataEventosRSVO.setTextColor("#000000");
			dataEventosRSVO.setStyleClass("horarioRegistroLancado");
			dataEventosRSVO.setData(rs.getDate("datafim"));
			dataEventosRSVOs.add(dataEventosRSVO);
		}
		return dataEventosRSVOs;
	}
	
	public void realizarDefinicaoDataInicioTerminoAvaliacaoOnlineMatricula(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, AvaliacaoOnlineVO avaliacaoOnlineVO, CalendarioAtividadeMatriculaVO calendarioAvaliacaoOnline, UsuarioVO usuarioVO) throws Exception{
		CalendarioAtividadeMatriculaVO calendarioAcessoConteudo = consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividade(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), TipoCalendarioAtividadeMatriculaEnum.ACESSO_CONTEUDO_ESTUDO, TipoOrigemEnum.NENHUM, "", Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO);
		if(Uteis.isAtributoPreenchido(avaliacaoOnlineVO) || (avaliacaoOnlineVO.getRegraDefinicaoPeriodoAvaliacaoOnline().isCalendarioLancamentoNota() && !avaliacaoOnlineVO.getVariavelNotaCfgPadraoAvaliacaoOnlineRea().isEmpty())){			
			if(avaliacaoOnlineVO.getRegraDefinicaoPeriodoAvaliacaoOnline().isPeriodoAcessoDisciplina()) {
				calendarioAvaliacaoOnline.setDataInicio(calendarioAcessoConteudo.getDataInicio());	
				calendarioAvaliacaoOnline.setDataFim(calendarioAcessoConteudo.getDataFim());					
			}else if(avaliacaoOnlineVO.getRegraDefinicaoPeriodoAvaliacaoOnline().isNumeroDiaEspecifico()) {
				calendarioAvaliacaoOnline.setDataInicio(new Date());
				calendarioAvaliacaoOnline.setDataFim(Uteis.obterDataAvancada(calendarioAvaliacaoOnline.getDataInicio(), avaliacaoOnlineVO.getQtdDiasLimiteResponderAvaliacaOnline()));
			}else if(avaliacaoOnlineVO.getRegraDefinicaoPeriodoAvaliacaoOnline().isPeriodoDiaFixo()) {
				calendarioAvaliacaoOnline.setDataInicio(avaliacaoOnlineVO.getDataInicioAvaliacaoFixo());
				calendarioAvaliacaoOnline.setDataFim(avaliacaoOnlineVO.getDataTerminoAvaliacaoFixo());
			}else if(avaliacaoOnlineVO.getRegraDefinicaoPeriodoAvaliacaoOnline().isCalendarioLancamentoNota() && !avaliacaoOnlineVO.getVariavelNotaCfgPadraoAvaliacaoOnlineRea().isEmpty()) {				
				HistoricoVO historicoVO = getFacadeFactory().getHistoricoFacade().consultaRapidaPorCodigoMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), null, false, usuarioVO);
				boolean calendarioLocalizado = false;
				if(historicoVO != null) {
				CalendarioLancamentoNotaVO calendarioLancamentoNotaVO = getFacadeFactory().getCalendarioLancamentoNotaFacade().consultarPorCalendarioAcademicoUtilizar(matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getUnidadeEnsino().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getTurma().getTurmaAgrupada(), matriculaPeriodoTurmaDisciplinaVO.getProfessor().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo(), historicoVO.getConfiguracaoAcademico().getCodigo(), matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO().getCurso().getPeriodicidade(), matriculaPeriodoTurmaDisciplinaVO.getAno(), matriculaPeriodoTurmaDisciplinaVO.getSemestre(), false, usuarioVO);
				if(Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO)) {
					for(int x = 1; x<=40; x++) {
						if((Boolean)UtilReflexao.invocarMetodoGet(calendarioLancamentoNotaVO.getConfiguracaoAcademico(), "utilizarNota"+x)
							&& ((String)UtilReflexao.invocarMetodoGet(calendarioLancamentoNotaVO.getConfiguracaoAcademico(), "tituloNota"+x)).equals(avaliacaoOnlineVO.getVariavelNotaCfgPadraoAvaliacaoOnlineRea())) {
							calendarioAvaliacaoOnline.setDataInicio(Uteis.getDateHoraInicialDia((Date) UtilReflexao.invocarMetodoGet(calendarioLancamentoNotaVO, "dataInicioNota"+x)));
							calendarioAvaliacaoOnline.setDataFim(Uteis.getDateHoraFinalDia((Date) UtilReflexao.invocarMetodoGet(calendarioLancamentoNotaVO, "dataTerminoNota"+x)));
							calendarioLocalizado = true;
							break;
						}
					}
				}
				}
				if(!calendarioLocalizado) {
					throw new ConsistirException("Não foi possível gerar a Avaliação On-line. Procure a Secretaria Acadêmica.");
				}
			}else {
				calendarioAvaliacaoOnline.setDataInicio(null);
				calendarioAvaliacaoOnline.setDataFim(null);
			}
		}else{
			calendarioAvaliacaoOnline.setDataInicio(null);
			calendarioAvaliacaoOnline.setDataFim(null);
		}
	}
	
	class ExecutarEnvioMensagemAcessoAvaliacaoOnline implements Runnable{
		
		CalendarioAtividadeMatriculaVO calendarioAvaliacaoOnline; 
		UsuarioVO usuarioLogado;
		

		public ExecutarEnvioMensagemAcessoAvaliacaoOnline(CalendarioAtividadeMatriculaVO calendarioAvaliacaoOnline, UsuarioVO usuarioLogado) {
			super();
			this.calendarioAvaliacaoOnline = calendarioAvaliacaoOnline;
			this.usuarioLogado = usuarioLogado;
			
		}
		
		
		public void enviar() {
			Thread thread = new Thread(this);
			thread.start();
		}


		@Override
		public void run() {			
			try {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAcessoAvaliacaoOnline(calendarioAvaliacaoOnline, usuarioLogado);
			} catch (Exception e) {
				RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
				registroExecucaoJobVO.setErro(e.getMessage());
				registroExecucaoJobVO.setDataInicio(new Date());
				registroExecucaoJobVO.setDataTermino(new Date());
				registroExecucaoJobVO.setTempoExecucao(1);
				registroExecucaoJobVO.setErro(e.getMessage());
				registroExecucaoJobVO.setNome(TemplateMensagemAutomaticaEnum.MENSAGEM_ACESSO_AVALIACAO_ONLINE.name());				
				
			}
		}
		
	}
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void consultarEVincularCalendarioAtividadeMatriculaAvaliacaoOnlineSemPeriodoLiberacaoPorDependenciaCalendarioLancamentoNota(CalendarioLancamentoNotaVO calendarioLancamentoNotaVO, UsuarioVO usuarioVO, Boolean atualizarCalendarioAtividadeMatriculaComPeriodo) throws Exception {
		StringBuilder variaveis = new StringBuilder("");
		final Map<String, Map<String, Date>> mapVariavelPeriodo = new HashMap<String, Map<String, Date>>(0);
		for(int x  = 1; x <= 40; x++) {
			if((Boolean)UtilReflexao.invocarMetodoGet(calendarioLancamentoNotaVO.getConfiguracaoAcademico(), "utilizarNota"+x) &&
					UtilReflexao.invocarMetodoGet(calendarioLancamentoNotaVO, "dataInicioNota"+x) != null) {
				HashMap<String, Date> mapPeriodo = new HashMap<String, Date>(0);
				mapPeriodo.put("INICIO", (Uteis.getDateHoraInicialDia((Date)UtilReflexao.invocarMetodoGet(calendarioLancamentoNotaVO, "dataInicioNota"+x))));
				mapPeriodo.put("FIM", (Uteis.getDateHoraFinalDia((Date)UtilReflexao.invocarMetodoGet(calendarioLancamentoNotaVO, "dataTerminoNota"+x))));
				mapVariavelPeriodo.put((String)UtilReflexao.invocarMetodoGet(calendarioLancamentoNotaVO.getConfiguracaoAcademico(), "tituloNota"+x), mapPeriodo);
				if(variaveis.length() > 0) {
					variaveis.append(", ");
				}
				variaveis.append("'").append((String)UtilReflexao.invocarMetodoGet(calendarioLancamentoNotaVO.getConfiguracaoAcademico(), "tituloNota"+x)).append("' ");
			}
		}
		if(variaveis.length() > 0) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" 	select calendarioatividadematricula.*, avaliacaoonline.variavelnotacfgpadraoavaliacaoonlinerea as variavel from calendarioatividadematricula  ");
		sql.append(" 		inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo  = calendarioatividadematricula.matriculaperiodoturmadisciplina");
		sql.append(" 		inner join historico on matriculaperiodoturmadisciplina.codigo  = historico.matriculaperiodoturmadisciplina");
		sql.append(" 		inner join matricula on matriculaperiodoturmadisciplina.matricula  = matricula.matricula");
		sql.append(" 		inner join avaliacaoonline on avaliacaoonline.codigo = (");
		sql.append(" 			select codigo from (");
		sql.append(" 			(select 1 as ordem, avaliacaoonline.codigo from turmadisciplinaconteudo");
		sql.append(" 			inner join avaliacaoonline on avaliacaoonline.codigo = turmadisciplinaconteudo.avaliacaoonline");
		sql.append(" 			where matriculaperiodoturmadisciplina.turma = turmadisciplinaconteudo.turma and matriculaperiodoturmadisciplina.disciplina = turmadisciplinaconteudo.disciplina");
		sql.append(" 			and matriculaperiodoturmadisciplina.conteudo = turmadisciplinaconteudo.conteudo	");
		sql.append(" 			and (turmadisciplinaconteudo.ano ||'/'||turmadisciplinaconteudo.semestre) <= (matriculaperiodoturmadisciplina.ano||'/'||matriculaperiodoturmadisciplina.semestre) limit 1)");
		sql.append(" 			union");
		sql.append(" 			select 2 as ordem, avaliacaoonline.codigo from turma ");
		sql.append(" 			inner join avaliacaoonline on avaliacaoonline.codigo = turma.avaliacaoonline	");
		sql.append(" 			where turma.codigo = matriculaperiodoturmadisciplina.turma");
		sql.append(" 			");
		sql.append(" 			order by ordem limit 1");
		sql.append(" 			) as t");
		sql.append(" 		)");
		sql.append(" 		where tipocalendarioatividade  = 'PERIODO_REALIZACAO_AVALIACAO_ONLINE' and tipoorigem = 'NENHUM' and avaliacaoonline.regraDefinicaoPeriodoAvaliacaoOnline = '").append(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.CALENDARIO_LANCAMENTO_NOTA.name()).append("' ");
//		sql.append(" 		and (calendarioatividadematricula.situacaoatividade = '").append(SituacaoAtividadeEnum.NAO_CONCLUIDA.name()).append("') ");
		sql.append(" 		and (calendarioatividadematricula.avaliacaoonlinematricula is null) ");
		if (!atualizarCalendarioAtividadeMatriculaComPeriodo) {
			sql.append(" 		and ((calendarioatividadematricula.dataInicio is null) ");
			sql.append(" 		or (calendarioatividadematricula.dataFim is null)) ");			
		}
		sql.append(" 		and avaliacaoonline.variavelnotacfgpadraoavaliacaoonlinerea in (").append(variaveis).append(") ");
		if(calendarioLancamentoNotaVO.getIsApresentarAnoSemestre()) {
			sql.append(" 		and matriculaperiodoturmadisciplina.ano = '").append(calendarioLancamentoNotaVO.getAno()).append("'");
		}
		if(calendarioLancamentoNotaVO.getIsApresentarCampoSemestre()) {
			sql.append(" 		and matriculaperiodoturmadisciplina.semestre = '").append(calendarioLancamentoNotaVO.getSemestre()).append("'");
		}
		sql.append(" and historico.configuracaoacademico = ").append(calendarioLancamentoNotaVO.getConfiguracaoAcademico().getCodigo());
		if(Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO.getUnidadeEnsino())) {
			sql.append(" and matricula.unidadeensino = ").append(calendarioLancamentoNotaVO.getUnidadeEnsino().getCodigo());
		}
		if(Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO.getUnidadeEnsinoCurso().getCurso())) {
			sql.append(" and matricula.curso = ").append(calendarioLancamentoNotaVO.getUnidadeEnsinoCurso().getCurso().getCodigo());
		}
		if(Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO.getDisciplina())) {
			sql.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(calendarioLancamentoNotaVO.getDisciplina().getCodigo());
		}
		if(Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO.getProfessor())) {
			sql.append(" and matriculaperiodoturmadisciplina.professor = ").append(calendarioLancamentoNotaVO.getProfessor().getCodigo());
		}
		
		sql.append(" 		union all ");
		sql.append(" 		select calendarioatividadematricula.*, avaliacaoonline.variavelnotacfgpadraoavaliacaoonlinerea as variavel from calendarioatividadematricula  ");
		sql.append(" 		inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo  = calendarioatividadematricula.matriculaperiodoturmadisciplina");
		sql.append(" 		inner join historico on matriculaperiodoturmadisciplina.codigo  = historico.matriculaperiodoturmadisciplina");
		sql.append(" 		inner join matricula on matriculaperiodoturmadisciplina.matricula  = matricula.matricula");
		sql.append(" 		inner join conteudounidadepaginarecursoeducacional on conteudounidadepaginarecursoeducacional.codigo::VARCHAR  = calendarioatividadematricula.codorigem");
		sql.append(" 		inner join avaliacaoonline on avaliacaoonline.codigo = conteudounidadepaginarecursoeducacional.avaliacaoonline");
		sql.append(" 		where tipocalendarioatividade  = 'PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA' and tipoorigem = 'REA'");
		sql.append(" 		and avaliacaoonline.regraDefinicaoPeriodoAvaliacaoOnline = '").append(RegraDefinicaoPeriodoAvaliacaoOnlineEnum.CALENDARIO_LANCAMENTO_NOTA.name()).append("' ");
//		sql.append(" 		and (calendarioatividadematricula.situacaoatividade = '").append(SituacaoAtividadeEnum.NAO_CONCLUIDA.name()).append("') ");
		sql.append(" 		and (calendarioatividadematricula.avaliacaoonlinematricula is null) ");
		if (!atualizarCalendarioAtividadeMatriculaComPeriodo) {
			sql.append(" 		and ((calendarioatividadematricula.dataInicio is null) ");
			sql.append(" 		or (calendarioatividadematricula.dataFim is null)) ");			
		}
		sql.append(" 		and avaliacaoonline.variavelnotacfgpadraoavaliacaoonlinerea in (").append(variaveis).append(") ");
		if(calendarioLancamentoNotaVO.getIsApresentarAnoSemestre()) {
			sql.append(" 		and matriculaperiodoturmadisciplina.ano = '").append(calendarioLancamentoNotaVO.getAno()).append("'");
		}
		if(calendarioLancamentoNotaVO.getIsApresentarCampoSemestre()) {
			sql.append(" 		and matriculaperiodoturmadisciplina.semestre = '").append(calendarioLancamentoNotaVO.getSemestre()).append("'");
		}
		sql.append(" and historico.configuracaoacademico = ").append(calendarioLancamentoNotaVO.getConfiguracaoAcademico().getCodigo());
		if(Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO.getUnidadeEnsino())) {
			sql.append(" and matricula.unidadeensino = ").append(calendarioLancamentoNotaVO.getUnidadeEnsino().getCodigo());
		}
		if(Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO.getUnidadeEnsinoCurso().getCurso())) {
			sql.append(" and matricula.curso = ").append(calendarioLancamentoNotaVO.getUnidadeEnsinoCurso().getCurso().getCodigo());
		}
		if(Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO.getDisciplina())) {
			sql.append(" and matriculaperiodoturmadisciplina.disciplina = ").append(calendarioLancamentoNotaVO.getDisciplina().getCodigo());
		}
		if(Uteis.isAtributoPreenchido(calendarioLancamentoNotaVO.getProfessor())) {
			sql.append(" and matriculaperiodoturmadisciplina.professor = ").append(calendarioLancamentoNotaVO.getProfessor().getCodigo());
		}
		sql.append(" 		and length(avaliacaoonline.variavelnotacfgpadraoavaliacaoonlinerea) != 0");
		final SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		final StringBuilder update = new StringBuilder("update calendarioatividadematricula set situacaoatividade = (case when ? >= now() then '").append(SituacaoAtividadeEnum.NAO_CONCLUIDA.name()).append("' else '").append(SituacaoAtividadeEnum.CONCLUIDA.name()).append("' end  ) , dataInicio = ?,  dataFim = ? where codigo = ? ");
		List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs = new ArrayList<CalendarioAtividadeMatriculaVO>(0);
		while(rs.next()) {	
			final CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = montarDados(rs, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			calendarioAtividadeMatriculaVO.setDataInicio(mapVariavelPeriodo.get(rs.getString("variavel").trim()).get("INICIO"));
			calendarioAtividadeMatriculaVO.setDataFim( mapVariavelPeriodo.get(rs.getString("variavel").trim()).get("FIM"));
			calendarioAtividadeMatriculaVOs.add(calendarioAtividadeMatriculaVO);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {				
				@Override
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement ps = arg0.prepareStatement(update.toString());
					ps.setTimestamp(1, Uteis.getDataJDBCTimestamp(calendarioAtividadeMatriculaVO.getDataFim()));
					ps.setTimestamp(2, Uteis.getDataJDBCTimestamp(calendarioAtividadeMatriculaVO.getDataInicio()));
					ps.setTimestamp(3, Uteis.getDataJDBCTimestamp(calendarioAtividadeMatriculaVO.getDataFim()));
					ps.setInt(4,calendarioAtividadeMatriculaVO.getCodigo());					
					return ps;
				}
			});
		}
		new notificarCalendarioAtividadeMatriculaAvaliacaoOnline().enviarEmail(calendarioAtividadeMatriculaVOs, usuarioVO);
		}

	}
	
	public class notificarCalendarioAtividadeMatriculaAvaliacaoOnline implements Runnable{
		
		private List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs;
		private UsuarioVO usuarioVO;
		
		public void enviarEmail(List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs, UsuarioVO usuarioVO){
			this.calendarioAtividadeMatriculaVOs = calendarioAtividadeMatriculaVOs; 
			this.usuarioVO = usuarioVO;
			Thread job = new Thread(this);
			job.start();
		}

		
		public void run() {
			if(calendarioAtividadeMatriculaVOs != null && calendarioAtividadeMatriculaVOs.size() > 0) {
			ProcessarParalelismo.executar(0, calendarioAtividadeMatriculaVOs.size(), new ConsistirException(), new ProcessarParalelismo.Processo() {			
				@Override
				public void run(int i) {
					try {
						getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemAcessoAvaliacaoOnline(calendarioAtividadeMatriculaVOs.get(i), usuarioVO);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
				
			
		}
		}
		
	}
		
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void realizarDefinicaoDataInicioTerminoCalendarioAtividadeMatriculaAvaliacaoOnline(CalendarioAtividadeMatriculaVO obj, UsuarioVO usuario) throws Exception {
		if(obj.getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_REALIZACAO) && (obj.getDataInicio() == null  || obj.getDataFim() == null)) {
			MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorChavePrimariaTrazendoMatricula(obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
			AvaliacaoOnlineVO avaliacaoOnlineVO = null;
			if(Uteis.isAtributoPreenchido(obj.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO())) {
				avaliacaoOnlineVO = getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarPorChavePrimaria(obj.getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			}else if(obj.isApresentarConteudoUnidadePaginaRecursoEducacional()) {
				avaliacaoOnlineVO = getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarAvaliacaoOnlineMatriculaAlunoDeveResponder(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), obj.getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo().toString(), usuario);
			}else {
				avaliacaoOnlineVO = getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarAvaliacaoOnlineMatriculaAlunoDeveResponder(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), "", usuario);
			}
			if(Uteis.isAtributoPreenchido(avaliacaoOnlineVO)) {
				realizarDefinicaoDataInicioTerminoAvaliacaoOnlineMatricula(matriculaPeriodoTurmaDisciplinaVO, avaliacaoOnlineVO, obj, usuario);
			}
			if(obj.getDataInicio() == null  || obj.getDataFim() == null) {
				obj.getAvaliacaoOnlineMatriculaVO().setSituacaoAvaliacaoOnlineMatriculaEnum(SituacaoAvaliacaoOnlineMatriculaEnum.AGUARDANDO_DATA_LIBERACAO);
			}else {
				alterarDataCalendarioAtividadeMatricula(obj, false, usuario);
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void executarInicializacaoDataInicioCalendarioAtividadeMatricula(ConfiguracaoEADVO configuracaoEADVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioLogado) throws Exception {
		Date dataInicio = realizarGeracaoDataInicioCalendarioAtividadeMatricula(configuracaoEADVO, calendarioAtividadeMatriculaVO, matriculaPeriodoTurmaDisciplinaVO, programacaoTutoriaOnlineVO, usuarioLogado);
		if( dataInicio != null) {
			calendarioAtividadeMatriculaVO.setDataInicio(dataInicio);
		}else {
			calendarioAtividadeMatriculaVO.setDataInicio(new Date());
		}
	}
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private Date realizarGeracaoDataInicioCalendarioAtividadeMatricula(ConfiguracaoEADVO configuracaoEADVO, CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, ProgramacaoTutoriaOnlineVO programacaoTutoriaOnlineVO, UsuarioVO usuarioLogado) throws Exception {
		if(programacaoTutoriaOnlineVO != null && programacaoTutoriaOnlineVO.getDefinirPeriodoAulaOnline()) {
			return programacaoTutoriaOnlineVO.getDataInicioAula();
		}else {
			if (!configuracaoEADVO.getTipoControleTempoLimiteConclusaoDisciplina().isPeriodoCalendarioLetivo()) {
				return getFacadeFactory().getMatriculaPeriodoFacade().realizarConsultaDataParcelaMatricula(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo());
			}else {
				calendarioAtividadeMatriculaVO.setPeriodoLetivoAtivoUnidadeEnsinoCursoVO(getFacadeFactory().getPeriodoLetivoAtivoUnidadeEnsinoCursoFacade().consultarDataInicioDataFimPeriodoPorMatriculaPeriodoTurmaDisciplina(matriculaPeriodoTurmaDisciplinaVO.getCodigo(), usuarioLogado));
				if (Uteis.isAtributoPreenchido(calendarioAtividadeMatriculaVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO())) {
					return calendarioAtividadeMatriculaVO.getPeriodoLetivoAtivoUnidadeEnsinoCursoVO().getDataInicioPeriodoLetivo();
				} 
				return null;
			}
		}
		
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<CalendarioAtividadeMatriculaVO> consultarAtividadeDiscursivaCodOrigem(Integer codOrigem, TipoOrigemEnum tipoOrigem,int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM calendarioatividadematricula WHERE codOrigem = '").append(codOrigem).append("' and tipoOrigem = '").append(tipoOrigem).append("'");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (montarDadosConsulta(rs, nivelMontarDados, usuarioLogado));
		
	}
	
	@Override
	public Date consultarDataFimAtividadeDiscursiva(Integer codOrigem, String matriculaAluno,UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT dataFim FROM calendarioatividadematricula WHERE codOrigem = '").append(codOrigem).append("' and tipocalendarioatividade = '").append(TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE).append("'").append(" and matricula = '").append(matriculaAluno).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		
		while (tabelaResultado.next()) {
			return Uteis.getDataJDBCTimestamp(tabelaResultado.getTimestamp("dataFim"));
		}
		
		return null;
		
		
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public CalendarioAtividadeMatriculaVO consultarPorAvaliacaoOnlineMatricula(Integer avaliacaoOnlineMatricula, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT * FROM calendarioatividadematricula ");
		sqlStr.append(" WHERE avaliacaoonlinematricula = ").append(avaliacaoOnlineMatricula);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());

		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, usuarioLogado);
		}
		return new CalendarioAtividadeMatriculaVO();
	}
	private void realizarAlteracaoSituacaoCalendarioAtividadeMatricula(CalendarioAtividadeMatriculaVO calendarioAvaliacaoOnline) throws ParseException {
		if ((UteisData.getCompareData(calendarioAvaliacaoOnline.getDataFim(), new Date()) > 0) && !Uteis.isAtributoPreenchido(calendarioAvaliacaoOnline.getAvaliacaoOnlineMatriculaVO())) {
			calendarioAvaliacaoOnline.setSituacaoAtividade(SituacaoAtividadeEnum.NAO_CONCLUIDA);
		} else if (UteisData.getCompareData(calendarioAvaliacaoOnline.getDataFim(), new Date()) <= 0) {
			calendarioAvaliacaoOnline.setSituacaoAtividade(SituacaoAtividadeEnum.CONCLUIDA);
		}
	}
}
