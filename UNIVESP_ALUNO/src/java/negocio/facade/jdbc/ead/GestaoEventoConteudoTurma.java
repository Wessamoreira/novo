package negocio.facade.jdbc.ead;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jakarta.el.ValueExpression;
import jakarta.faces. component.UIOutput;
import jakarta.faces. component.html.HtmlOutputText;
import jakarta.faces. component.html.HtmlPanelGroup;
import jakarta.faces. context.FacesContext;

import org.primefaces.component.column.Column;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;

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

import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.enumeradores.MomentoApresentacaoRecursoEducacionalEnum;
import negocio.comuns.academico.enumeradores.SituacaoConteudoEnum;
import negocio.comuns.academico.enumeradores.TipoGraficoEnum;
import negocio.comuns.academico.enumeradores.TipoRecursoEducacionalEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaAvaliacaoPBLVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaInteracaoAtaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaResponsavelAtaVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;
import negocio.comuns.ead.enumeradores.SituacaoPBLEnum;
import negocio.comuns.ead.enumeradores.TipoAvaliacaoPBLEnum;
import negocio.comuns.ead.enumeradores.TipoRecursoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.GestaoEventoConteudoTurmaInterfaceFacade;

/**
 * @author Victor Hugo de Paula Costa - 14 de jul de 2016
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class GestaoEventoConteudoTurma extends ControleAcesso implements GestaoEventoConteudoTurmaInterfaceFacade {

	private static final long serialVersionUID = 1L;
	/**
	 * @author Victor Hugo de Paula Costa - 14 de jul de 2016
	 */

	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		GestaoEventoConteudoTurma.idEntidade = idEntidade;
	}

	public GestaoEventoConteudoTurma() throws Exception {
		super();
		setIdEntidade("GestaoEventoConteudoTurma");
	}

	@Override
	public void validarDados(GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO) throws ConsistirException {
		ConsistirException consistirException = new ConsistirException();

		if (!consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(gestaoEventoConteudoTurmaVO);
			incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO gestaoeventoconteudoturma(turma, conteudo, disciplina, ano, semestre, tiporecurso, ");
			sql.append("            unidadeconteudo, conteudounidadepagina, conteudounidadepaginarecursoeducacional, ");
			sql.append("            situacao, dateliberacao, formulaCalculoNotaFinalGeral, tipoVariavelNota)");
			sql.append("    VALUES (?, ?, ?, ?, ?, ?, ");
			sql.append("            ?, ?, ?, ");
			sql.append("            ?, ?, ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			gestaoEventoConteudoTurmaVO.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					sqlInserir.setInt(1, gestaoEventoConteudoTurmaVO.getTurmaVO().getCodigo());
					sqlInserir.setInt(2, gestaoEventoConteudoTurmaVO.getConteudoVO().getCodigo());
					sqlInserir.setInt(3, gestaoEventoConteudoTurmaVO.getDisciplinaVO().getCodigo());
					sqlInserir.setString(4, gestaoEventoConteudoTurmaVO.getAno());
					sqlInserir.setString(5, gestaoEventoConteudoTurmaVO.getSemestre());
					sqlInserir.setString(6, gestaoEventoConteudoTurmaVO.getTipoRecurso().getName());
					if (gestaoEventoConteudoTurmaVO.getUnidadeConteudoVO().getCodigo().equals(0)) {
						sqlInserir.setNull(7, 0);
					} else {
						sqlInserir.setInt(7, gestaoEventoConteudoTurmaVO.getUnidadeConteudoVO().getCodigo());
					}
					if (gestaoEventoConteudoTurmaVO.getConteudoUnidadePaginaVO().getCodigo().equals(0)) {
						sqlInserir.setNull(8, 0);
					} else {
						sqlInserir.setInt(8, gestaoEventoConteudoTurmaVO.getConteudoUnidadePaginaVO().getCodigo());
					}
					if (gestaoEventoConteudoTurmaVO.getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo().equals(0)) {
						sqlInserir.setNull(9, 0);
					} else {
						sqlInserir.setInt(9, gestaoEventoConteudoTurmaVO.getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo());
					}
					sqlInserir.setString(10, gestaoEventoConteudoTurmaVO.getSituacao().getName());
					if (!Uteis.isAtributoPreenchido(gestaoEventoConteudoTurmaVO.getDateLiberacao())) {
						sqlInserir.setNull(11, 0);
					} else {
						sqlInserir.setTimestamp(11, Uteis.getDataJDBCTimestamp(gestaoEventoConteudoTurmaVO.getDateLiberacao()));
					}
					sqlInserir.setString(12, gestaoEventoConteudoTurmaVO.getFormulaCalculoNotaFinalGeral());
					sqlInserir.setString(13, gestaoEventoConteudoTurmaVO.getTipoVariavelNota());
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException, DataAccessException {
					if (rs.next()) {
						gestaoEventoConteudoTurmaVO.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
		} catch (Exception e) {
			gestaoEventoConteudoTurmaVO.setNovoObj(Boolean.TRUE);
			gestaoEventoConteudoTurmaVO.setCodigo(0);
			throw e;
		}
	}

	public GestaoEventoConteudoTurmaVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		GestaoEventoConteudoTurmaVO obj = new GestaoEventoConteudoTurmaVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getTurmaVO().setCodigo(tabelaResultado.getInt("turma"));
		obj.getConteudoVO().setCodigo(tabelaResultado.getInt("conteudo"));
		obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina"));
		obj.setAno(tabelaResultado.getString("ano"));
		obj.setSemestre(tabelaResultado.getString("semestre"));
		obj.setTipoRecurso(TipoRecursoEnum.valueOf(tabelaResultado.getString("tiporecurso")));
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("unidadeconteudo"))) {
			obj.getUnidadeConteudoVO().setCodigo(tabelaResultado.getInt("unidadeconteudo"));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("conteudounidadepagina"))) {
			obj.getConteudoUnidadePaginaVO().setCodigo(tabelaResultado.getInt("conteudounidadepagina"));
		}
		if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("conteudounidadepaginarecursoeducacional"))) {
			obj.getConteudoUnidadePaginaRecursoEducacionalVO().setCodigo(tabelaResultado.getInt("conteudounidadepaginarecursoeducacional"));
		}
		obj.setSituacao(SituacaoPBLEnum.valueOf(tabelaResultado.getString("situacao")));
		obj.setDateLiberacao(tabelaResultado.getDate("dateliberacao"));
		obj.setFormulaCalculoNotaFinalGeral(tabelaResultado.getString("formulaCalculoNotaFinalGeral"));
		obj.setTipoVariavelNota(tabelaResultado.getString("tipoVariavelNota"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado));
			// obj.setGestaoEventoConteudoTurmaAvaliacaoPBLVOs(getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().consultarPorCodigoGestaoEventoConteudoTurmaVO(obj.getCodigo(),
			// Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
			return obj;
		}
		return obj;
	}

	public List<GestaoEventoConteudoTurmaVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<GestaoEventoConteudoTurmaVO> vetResultado = new ArrayList<GestaoEventoConteudoTurmaVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
		}
		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GestaoEventoConteudoTurmaVO> consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM GestaoEventoConteudoTurma WHERE codigo = ?";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql, codigo), nivelMontarDados, usuarioLogado));
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (gestaoEventoConteudoTurmaVO.getCodigo() == 0) {
			incluir(gestaoEventoConteudoTurmaVO, verificarAcesso, usuarioVO);
		} else {
			alterar(gestaoEventoConteudoTurmaVO, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(gestaoEventoConteudoTurmaVO);
			alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE gestaoeventoconteudoturma");
			sql.append("   SET turma=?, conteudo=?, disciplina=?, ano=?, semestre=?, ");
			sql.append("       tiporecurso=?, unidadeconteudo=?, conteudounidadepagina=?, ");
			sql.append("       conteudounidadepaginarecursoeducacional=?, situacao=?, dateliberacao=?, formulaCalculoNotaFinalGeral=?, tipoVariavelNota=?");
			sql.append(" WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, gestaoEventoConteudoTurmaVO.getTurmaVO().getCodigo());
					sqlAlterar.setInt(2, gestaoEventoConteudoTurmaVO.getConteudoVO().getCodigo());
					sqlAlterar.setInt(3, gestaoEventoConteudoTurmaVO.getDisciplinaVO().getCodigo());
					sqlAlterar.setString(4, gestaoEventoConteudoTurmaVO.getAno());
					sqlAlterar.setString(5, gestaoEventoConteudoTurmaVO.getSemestre());
					sqlAlterar.setString(6, gestaoEventoConteudoTurmaVO.getTipoRecurso().getName());
					if (gestaoEventoConteudoTurmaVO.getUnidadeConteudoVO().getCodigo().equals(0)) {
						sqlAlterar.setNull(7, 0);
					} else {
						sqlAlterar.setInt(7, gestaoEventoConteudoTurmaVO.getUnidadeConteudoVO().getCodigo());
					}
					if (gestaoEventoConteudoTurmaVO.getConteudoUnidadePaginaVO().getCodigo().equals(0)) {
						sqlAlterar.setNull(8, 0);
					} else {
						sqlAlterar.setInt(8, gestaoEventoConteudoTurmaVO.getConteudoUnidadePaginaVO().getCodigo());
					}
					if (gestaoEventoConteudoTurmaVO.getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo().equals(0)) {
						sqlAlterar.setNull(9, 0);
					} else {
						sqlAlterar.setInt(9, gestaoEventoConteudoTurmaVO.getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo());
					}
					sqlAlterar.setString(10, gestaoEventoConteudoTurmaVO.getSituacao().getName());
					if (Uteis.isAtributoPreenchido(gestaoEventoConteudoTurmaVO.getDateLiberacao())) {
						sqlAlterar.setTimestamp(11, Uteis.getDataJDBCTimestamp(gestaoEventoConteudoTurmaVO.getDateLiberacao()));
					} else {
						sqlAlterar.setNull(11, 0);
					}
					sqlAlterar.setString(12, gestaoEventoConteudoTurmaVO.getFormulaCalculoNotaFinalGeral());
					sqlAlterar.setString(13, gestaoEventoConteudoTurmaVO.getTipoVariavelNota());
					sqlAlterar.setInt(14, gestaoEventoConteudoTurmaVO.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(gestaoEventoConteudoTurmaVO, false, usuarioVO);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarSituacaoGestaoEventoConteudoTurma(final GestaoEventoConteudoTurmaVO obj, final SituacaoPBLEnum situacao, boolean atualizarAvaliacoes, UsuarioVO usuario) throws Exception {
		try {
			final StringBuilder sb = new StringBuilder(" UPDATE gestaoeventoconteudoturma set ");
			sb.append(" situacao = ? ");
			sb.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sb.toString());
					int i = 0;
					sqlAlterar.setString(++i, situacao.name());
					sqlAlterar.setInt(++i, obj.getCodigo());
					return sqlAlterar;
				}
			});
			if(atualizarAvaliacoes){
				getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().atualizarSituacaoPorGestaoEventoConteudoTurma(obj.getCodigo(), situacao, usuario);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarFormulaGestaoEventoConteudoTurma(final GestaoEventoConteudoTurmaVO obj,  UsuarioVO usuario) throws Exception {
		try {
			final StringBuilder sb = new StringBuilder(" UPDATE gestaoeventoconteudoturma set ");
			sb.append(" formulaCalculoNotaFinalGeral = ? ");
			sb.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sb.toString());
					int i = 0;
					sqlAlterar.setString(++i, obj.getFormulaCalculoNotaFinalGeral());
					sqlAlterar.setInt(++i, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public GestaoEventoConteudoTurmaVO consultarPorCodigoConteudoConteudoUnidadePaginaRecursoEducacionalAnoSemestre(Integer conteudo, Integer codigoTurma, Integer codigoDisciplina, Integer codigoConteudoUnidadePaginaRecursoEducacional, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT * FROM GestaoEventoConteudoTurma WHERE ConteudoUnidadePaginaRecursoEducacional = ").append(codigoConteudoUnidadePaginaRecursoEducacional);
		sqlStr.append(" AND ano = '").append(ano).append("'");
		sqlStr.append(" AND semestre = '").append(semestre).append("'");
		sqlStr.append(" AND conteudo = ").append(conteudo);
		sqlStr.append(" AND turma = ").append(codigoTurma);
		sqlStr.append(" AND disciplina = ").append(codigoDisciplina);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return (montarDados(rs, nivelMontarDados, usuarioLogado));
		}
		return new GestaoEventoConteudoTurmaVO();
	}

	@Override
	/**
	 * 
	 * @author Victor Hugo de Paula Costa - 27 de jul de 2016
	 * @param gestaoEventoConteudoTurmaVO
	 * @param requerLiberacaoProfessor
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Boolean consultarSeGestaoEventoConteudoLiberado(GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO, Boolean requerLiberacaoProfessor, UsuarioVO usuarioVO) throws Exception {
		if (!gestaoEventoConteudoTurmaVO.getCodigo().equals(0)) {
			if (gestaoEventoConteudoTurmaVO.getSituacao().equals(SituacaoPBLEnum.LIBERADO)) {
				return true;
			} else if (gestaoEventoConteudoTurmaVO.getSituacao().equals(SituacaoPBLEnum.PENDENTE) && Uteis.isAtributoPreenchido(gestaoEventoConteudoTurmaVO.getDateLiberacao()) && gestaoEventoConteudoTurmaVO.getDateLiberacao().compareTo(new Date()) > 0) {
				return false;
			} else if (gestaoEventoConteudoTurmaVO.getSituacao().equals(SituacaoPBLEnum.REALIZADO)) {
				return true;
			}
		} else {
			if (requerLiberacaoProfessor) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @author Victor Hugo de Paula Costa - 28 de jul de 2016
	 * @param conteudoUnidadePaginaRecursoEducacionalVO
	 * @param gestaoEventoConteudoTurmaVO
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @throws Exception
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void inicializarGestaoEventoConteudoTurma(ConteudoUnidadePaginaRecursoEducacionalVO obj, UsuarioVO usuarioVO) throws Exception {
		if (obj.getTipoRecursoEducacional().isTipoRecursoAvaliacaoPbl()) {
			verificarTipoAvaliacaoPBLAlunoProfessorAutoAvaliacaoNaoInicializado(obj, usuarioVO);
			if (obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_GestaoEventoConteudoTurma_gestaoEventoConteudoSemAlunoNaTurma"));
			}
		}
		obj.getGestaoEventoConteudoTurmaVO().setTipoRecurso(TipoRecursoEnum.CONTEUDO_UNIDADE_PAGINA_RECURSO_EDUCACIONAL);
		obj.getGestaoEventoConteudoTurmaVO().setConteudoUnidadePaginaRecursoEducacionalVO(obj);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void verificarTipoAvaliacaoPBLAlunoProfessorAutoAvaliacaoNaoInicializado(ConteudoUnidadePaginaRecursoEducacionalVO obj, UsuarioVO usuarioVO) throws Exception {
		obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().addAll(getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().consultarColegasAvaliacaoPBL(obj.getGestaoEventoConteudoTurmaVO().getDisciplinaVO().getCodigo(), obj.getGestaoEventoConteudoTurmaVO().getTurmaVO().getCodigo(), obj.getGestaoEventoConteudoTurmaVO().getAno(), obj.getGestaoEventoConteudoTurmaVO().getSemestre(),  Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));		
		for (GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoAvaliacao : obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
			if (obj.getAlunoAvaliaAluno()) {
				gestaoEventoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().addAll(montarListaAlunoAvaliaAluno(obj.getGestaoEventoConteudoTurmaVO(), gestaoEventoAvaliacao.getAvaliado(), obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs(), gestaoEventoAvaliacao.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO().getCodigo(), false));	
			}
			if (obj.getProfessorAvaliaAluno()) {
				if (usuarioVO.getIsApresentarVisaoProfessor()) {
					gestaoEventoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setAvaliador(usuarioVO.getPessoa());
				} else if (usuarioVO.getIsApresentarVisaoAluno() && Uteis.isAtributoPreenchido(obj.getGestaoEventoConteudoTurmaVO().getProfessor())) {
					gestaoEventoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setAvaliador(obj.getGestaoEventoConteudoTurmaVO().getProfessor());
				}
				gestaoEventoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setAvaliado(gestaoEventoAvaliacao.getAvaliado());
				gestaoEventoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setGestaoEventoConteudoTurmaVO(obj.getGestaoEventoConteudoTurmaVO());
				gestaoEventoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setMatriculaPeriodoTurmaDisciplinaAvaliadoVO(gestaoEventoAvaliacao.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO());
				gestaoEventoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setTipoAvaliacao(TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO);
			}
			if (usuarioVO.getIsApresentarVisaoProfessor()) {
				gestaoEventoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setAvaliador(usuarioVO.getPessoa());
			} else if (usuarioVO.getIsApresentarVisaoAluno() && Uteis.isAtributoPreenchido(obj.getGestaoEventoConteudoTurmaVO().getProfessor())) {
				gestaoEventoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setAvaliador(obj.getGestaoEventoConteudoTurmaVO().getProfessor());
			}
			gestaoEventoAvaliacao.setGestaoEventoConteudoTurmaVO(obj.getGestaoEventoConteudoTurmaVO());
			gestaoEventoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setGestaoEventoConteudoTurmaVO(obj.getGestaoEventoConteudoTurmaVO());
			gestaoEventoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setAvaliado(gestaoEventoAvaliacao.getAvaliado());
			gestaoEventoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setTipoAvaliacao(TipoAvaliacaoPBLEnum.RESULTADO_FINAL);
			gestaoEventoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setMatriculaPeriodoTurmaDisciplinaAvaliadoVO(gestaoEventoAvaliacao.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO());

		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> montarListaAlunoAvaliaAluno(GestaoEventoConteudoTurmaVO obj, PessoaVO avaliado, List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> listaAvaliadores, Integer codigoMatriculaPeriodoTurmaDisciplinaAvaliado, boolean isPreencherSituacao) throws Exception {
		List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> listaDeAvaliadores = new ArrayList<GestaoEventoConteudoTurmaAvaliacaoPBLVO>();
		for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliador : listaAvaliadores) {
			GestaoEventoConteudoTurmaAvaliacaoPBLVO novoAvaliador = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
			novoAvaliador = avaliador.clone();
			novoAvaliador.setGestaoEventoConteudoTurmaVO(obj);
			novoAvaliador.setTipoAvaliacao(TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO);
			novoAvaliador.setAvaliado(avaliado);
			novoAvaliador.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO().setCodigo(codigoMatriculaPeriodoTurmaDisciplinaAvaliado);
			if(isPreencherSituacao){
				novoAvaliador.setSituacao(obj.getSituacao());	
			}
			listaDeAvaliadores.add(novoAvaliador);
		}
		return listaDeAvaliadores;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void verificarSePossuiNovasMatriculas(ConteudoUnidadePaginaRecursoEducacionalVO obj, Integer turma, Integer disciplina, String ano, String semestre, Integer codigoProfessor, Integer unidadeEnsino, Boolean verificarAcesso,UsuarioVO usuarioVO) throws ConsistirException, Exception {
		if(Uteis.isAtributoPreenchido(obj.getGestaoEventoConteudoTurmaVO())){
			List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> listaNovosAvaliados = getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().consultarColegasAvaliacaoPBLNovasMatriculas(obj.getGestaoEventoConteudoTurmaVO().getCodigo(), disciplina, turma, ano, semestre, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			if (!listaNovosAvaliados.isEmpty()) {
				obj.getGestaoEventoConteudoTurmaVO().getProfessor().setCodigo(codigoProfessor);
				verificarTipoAvaliacaoPBLAlunoProfessorAutoAvaliacaoInicializado(obj, listaNovosAvaliados, usuarioVO);
				getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().persistirGestaoEventoConteudoTurmaAvaliacaoPBLVOS(obj, verificarAcesso, usuarioVO);  
			}	
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void verificarTipoAvaliacaoPBLAlunoProfessorAutoAvaliacaoInicializado(ConteudoUnidadePaginaRecursoEducacionalVO obj, List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> listaNovosAvaliados, UsuarioVO usuarioVO) throws Exception {
		for (GestaoEventoConteudoTurmaAvaliacaoPBLVO novoAvaliado : listaNovosAvaliados) {
			if (obj.getAlunoAvaliaAluno()) {	
				novoAvaliado.setSituacao(obj.getGestaoEventoConteudoTurmaVO().getSituacao());
				novoAvaliado.setGestaoEventoConteudoTurmaVO(obj.getGestaoEventoConteudoTurmaVO());
				novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().addAll(montarListaAlunoAvaliaAluno(obj.getGestaoEventoConteudoTurmaVO(), novoAvaliado.getAvaliado(), listaNovosAvaliados, novoAvaliado.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO().getCodigo(), true));
				novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().addAll(montarListaAlunoAvaliaAluno(obj.getGestaoEventoConteudoTurmaVO(), novoAvaliado.getAvaliado(), obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs(), novoAvaliado.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO().getCodigo(), false));
				for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avalidorExistente : obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
					GestaoEventoConteudoTurmaAvaliacaoPBLVO novoAvaliador = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
					novoAvaliador = novoAvaliado.clone();
					novoAvaliador.setGestaoEventoConteudoTurmaVO(obj.getGestaoEventoConteudoTurmaVO());
					novoAvaliador.setTipoAvaliacao(TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO);
					novoAvaliador.setAvaliado(avalidorExistente.getAvaliado());
					novoAvaliador.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO().setCodigo(avalidorExistente.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO().getCodigo());
					novoAvaliador.setSituacao(obj.getGestaoEventoConteudoTurmaVO().getSituacao());
					avalidorExistente.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().add(novoAvaliador);
				}
			}
			if (obj.getProfessorAvaliaAluno()) {
				if (usuarioVO.getIsApresentarVisaoProfessor()) {
					novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setAvaliador(usuarioVO.getPessoa());
				} else if (usuarioVO.getIsApresentarVisaoAluno()) {
					novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setAvaliador(obj.getGestaoEventoConteudoTurmaVO().getProfessor());
				}
				novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setSituacao(obj.getGestaoEventoConteudoTurmaVO().getSituacao());
				novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setGestaoEventoConteudoTurmaVO(obj.getGestaoEventoConteudoTurmaVO());
				novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setAvaliado(novoAvaliado.getAvaliado());
				novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setTipoAvaliacao(TipoAvaliacaoPBLEnum.PROFESSOR_AVALIA_ALUNO);
				novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setMatriculaPeriodoTurmaDisciplinaAvaliadoVO(novoAvaliado.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO());
				
			}
			if (usuarioVO.getIsApresentarVisaoProfessor()) {
				novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setAvaliador(usuarioVO.getPessoa());
			} else if (usuarioVO.getIsApresentarVisaoAluno()) {
				novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setAvaliador(obj.getGestaoEventoConteudoTurmaVO().getProfessor());
			}
			novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setSituacao(obj.getGestaoEventoConteudoTurmaVO().getSituacao());
			novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setGestaoEventoConteudoTurmaVO(obj.getGestaoEventoConteudoTurmaVO());
			novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setAvaliado(novoAvaliado.getAvaliado());
			novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setTipoAvaliacao(TipoAvaliacaoPBLEnum.RESULTADO_FINAL);
			novoAvaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setMatriculaPeriodoTurmaDisciplinaAvaliadoVO(novoAvaliado.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO());
		}
		obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().addAll(listaNovosAvaliados);
	}	

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void finalizarGestaoEventoConteudoTurma(ConteudoUnidadePaginaRecursoEducacionalVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		validarSeGestaoEventoConteudoTurmaNaoPossuiNotasNaoLancadas(obj);
		obj.getGestaoEventoConteudoTurmaVO().setSituacao(SituacaoPBLEnum.REALIZADO);
		persistirGestaoEventoConteudoTurma(obj, verificarAcesso, usuarioVO);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Boolean validarSeGestaoEventoConteudoTurmaNaoPossuiNotasNaoLancadas(ConteudoUnidadePaginaRecursoEducacionalVO obj) throws Exception {
		for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao : obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
			
			if (obj.getAutoAvaliacao() && ((obj.getUtilizarNotaConceito() && avaliacao.getNotaConceitoAvaliacaoPBLVO().getCodigo().equals(0)) || (!obj.getUtilizarNotaConceito() && avaliacao.getNota() == null))) {
				return false;
			}			
			if (obj.getProfessorAvaliaAluno() && ((obj.getUtilizarNotaConceito() && avaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().getNotaConceitoAvaliacaoPBLVO().getCodigo().equals(0)) || (!obj.getUtilizarNotaConceito() && avaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().getNota()== null))) {
				return false;
			}			
			if(obj.getAlunoAvaliaAluno()){
				for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacaoAluno : avaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
					if ((!obj.getUtilizarNotaConceito() && avaliacaoAluno.getNota() == null) || (avaliacaoAluno.getNotaConceitoAvaliacaoPBLVO().getCodigo().equals(0) && obj.getUtilizarNotaConceito())) {
						return false;
					}
				}	
			}
		}
		return true;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public GestaoEventoConteudoTurmaVO consultarGestaoEventoConteudoTurmaPorCodigoConteudoTurmaDisciplinaAnoSemestre(Integer codigoConteudo, Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("SELECT * FROM GestaoEventoConteudoTurma");
		sqlStr.append(" WHERE Conteudo = ").append(codigoConteudo);
		sqlStr.append(" AND Turma = ").append(codigoTurma);
		sqlStr.append(" AND Disciplina = ").append(codigoDisciplina);
		sqlStr.append(" AND Ano = '").append(ano).append("'");
		sqlStr.append(" AND Semestre = '").append(semestre).append("'");
		sqlStr.append(" AND tiporecurso = '").append(TipoRecursoEnum.CONTEUDO).append("'");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return montarDados(rs, nivelMontarDados, usuarioVO);
		}
		return new GestaoEventoConteudoTurmaVO();
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void montarGestaoEventoConteudoTurmaNotaFinalGeralNaoInicializado(ConteudoVO conteudoVO, UsuarioVO usuarioVO) throws Exception {
		
		conteudoVO.getGestaoEventoConteudoTurmaVO().setTipoRecurso(TipoRecursoEnum.CONTEUDO);
		conteudoVO.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().addAll(getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().consultarColegasAvaliacaoPBL(conteudoVO.getGestaoEventoConteudoTurmaVO().getDisciplinaVO().getCodigo(), conteudoVO.getGestaoEventoConteudoTurmaVO().getTurmaVO().getCodigo(), conteudoVO.getGestaoEventoConteudoTurmaVO().getAno(), conteudoVO.getGestaoEventoConteudoTurmaVO().getSemestre(),  Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao : conteudoVO.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
			avaliacao.setGestaoEventoConteudoTurmaVO(conteudoVO.getGestaoEventoConteudoTurmaVO());
			avaliacao.setTipoAvaliacao(TipoAvaliacaoPBLEnum.RESULTADO_FINAL_GERAL);
			avaliacao.setAvaliador(usuarioVO.getPessoa());
		}
		conteudoVO.getGestaoEventoConteudoTurmaVO().setFormulaCalculoNotaFinalGeral(gerarFormulaGestaoEventoConteudoTurmaNotaFinalGeralNaonicializado(conteudoVO, usuarioVO));
		persistir(conteudoVO.getGestaoEventoConteudoTurmaVO(), false, usuarioVO);
		for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliado : conteudoVO.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().persistir(avaliado, false, usuarioVO);
		}
		
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private String gerarFormulaGestaoEventoConteudoTurmaNotaFinalGeralNaonicializado(ConteudoVO conteudo, UsuarioVO usuario) throws Exception {
		Integer quantidadeREAs = getFacadeFactory().getConteudoUnidadePaginaRecursoEducacionalFacade().consultarQuantidadeDeAvaliacaoExistenteNoConteudoUnidadePaginaRecursoEducacional(conteudo, usuario);
		StringBuilder formula = new StringBuilder("(");
		boolean primeiro = true;
		for (int i = 1; i <= quantidadeREAs; i++) {
			if (!primeiro) {
				formula.append(" + ");
			}
			formula.append("P" + i);
			primeiro = false;
		}
		formula.append(") / " + quantidadeREAs);
		return formula.toString();
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarValidacaoDaFormulaParaNotaFinal(GestaoEventoConteudoTurmaVO obj, UsuarioVO usuario) throws Exception  {
		String formula= obj.getFormulaCalculoNotaFinalGeral();	
		int tamanhoLista = obj.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().get(0).getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().size();
		for (int i = 1; i <=tamanhoLista; i++) {
			formula = formula.replace("P" + i, "1");
		}
		if(!Uteis.isAtributoPreenchido(formula) || !Uteis.verificarFormulaEstaCorreta(formula, false)){
			throw new Exception(UteisJSF.internacionalizar("msg_GestaoEventoConteudoTurma_formulaCalculoInvalido"));	
		}
		atualizarFormulaGestaoEventoConteudoTurma(obj, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarCalculoNotaFinalGeralAvaliacaoPBL(GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO, List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> gestaoEventoConteudoTurmaAvaliacaoPBLVO, UsuarioVO usuarioVO) throws Exception {
		int index;
		String formula;
		boolean realizarCalculo = false;
		realizarValidacaoDaFormulaParaNotaFinal(gestaoEventoConteudoTurmaVO, usuarioVO);
		for (GestaoEventoConteudoTurmaAvaliacaoPBLVO obj : gestaoEventoConteudoTurmaAvaliacaoPBLVO) {
			realizarCalculo = false;
			formula = gestaoEventoConteudoTurmaVO.getFormulaCalculoNotaFinalGeral();
			index = 1;
			for (GestaoEventoConteudoTurmaAvaliacaoPBLVO obj2 : obj.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
				if(obj2.getNota() != null){
					formula = formula.replace("P" + index, obj2.getNota().toString());
					realizarCalculo = true;
				}else{
					formula = formula.replace("P" + index, "0");
				}
				index++;
			}
			if(realizarCalculo){
				obj.setNota(Uteis.realizarCalculoFormula(formula));
				getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().persistir(obj, false, usuarioVO);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirGestaoEventoConteudoTurma(ConteudoUnidadePaginaRecursoEducacionalVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		validarGestaoEventoConteudoTurmaLiberada(obj);
		persistir(obj.getGestaoEventoConteudoTurmaVO(), verificarAcesso, usuario);
		if (obj.getTipoRecursoEducacional().isTipoRecursoAvaliacaoPbl()) {
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs(), "gestaoeventoconteudoturmaavaliacaopbl", "gestaoeventoconteudoturma", obj.getGestaoEventoConteudoTurmaVO().getCodigo(), usuario);
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().persistirGestaoEventoConteudoTurmaAvaliacaoPBLVOS(obj, verificarAcesso, usuario);
		} else if (obj.getTipoRecursoEducacional().isTipoRecursoAtaPbl()) {
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaResponsavelAtaVOs(), "gestaoeventoconteudoturmaresponsavelata", "gestaoeventoconteudoturma", obj.getGestaoEventoConteudoTurmaVO().getCodigo(), usuario);
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaInteracaoAtaVOs(), "gestaoeventoconteudoturmainteracaoata", "gestaoeventoconteudoturma", obj.getGestaoEventoConteudoTurmaVO().getCodigo(), usuario);
			getFacadeFactory().getGestaoEventoConteudoTurmaResponsavelAtaFacade().persistir(obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaResponsavelAtaVOs(), verificarAcesso, usuario);
			getFacadeFactory().getGestaoEventoConteudoTurmaInteracaoAtaFacade().persistir(obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaInteracaoAtaVOs(), verificarAcesso, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void validarGestaoEventoConteudoTurmaLiberada(ConteudoUnidadePaginaRecursoEducacionalVO obj) {
		if (obj.getGestaoEventoConteudoTurmaVO().getSituacao().isPendente() && (!obj.getRequerLiberacaoProfessor() || (Uteis.isAtributoPreenchido(obj.getGestaoEventoConteudoTurmaVO().getDateLiberacao()) && obj.getGestaoEventoConteudoTurmaVO().getDateLiberacao().compareTo(new Date()) <= 0))) {
			preencherDadosGestaoEventoConteudoTurmaLiberadaParaLiberacao(obj);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void preencherDadosGestaoEventoConteudoTurmaLiberadaParaLiberacao(ConteudoUnidadePaginaRecursoEducacionalVO obj) {
		obj.getGestaoEventoConteudoTurmaVO().setDateLiberacao(new Date());
		obj.getGestaoEventoConteudoTurmaVO().setSituacao(SituacaoPBLEnum.LIBERADO);
		if (obj.getTipoRecursoEducacional().isTipoRecursoAvaliacaoPbl()) {
			for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliado : obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
				avaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO().setSituacao(SituacaoPBLEnum.LIBERADO);
				if (obj.getAutoAvaliacao()) {
					avaliado.setSituacao(SituacaoPBLEnum.LIBERADO);
				}
				if (obj.getProfessorAvaliaAluno()) {
					avaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO().setSituacao(SituacaoPBLEnum.LIBERADO);
				}
				if (obj.getAlunoAvaliaAluno()) {
					for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliadores : avaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
						avaliadores.setSituacao(SituacaoPBLEnum.LIBERADO);
					}
				}
			}
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public GestaoEventoConteudoTurmaVO consultarGestaoEventoConteudoTurmaVOEstaPendente(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer turma, Integer disciplina, String ano, String semestre, Integer conteudo,  UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select  ");
		sb.append(" gect.codigo, gect.situacao, gect.dateliberacao ");
		sb.append(" from gestaoeventoconteudoturma as gect    ");
		sb.append(" inner join conteudounidadepaginarecursoeducacional as cupre on cupre.codigo = gect.conteudounidadepaginarecursoeducacional ");
		sb.append("	WHERE cupre.codigo = ").append(codigoConteudoUnidadePaginaRecursoEducacional);
		sb.append(" AND gect.turma = ").append(turma);
		sb.append(" AND gect.disciplina = ").append(disciplina);
		sb.append(" AND gect.ano = '").append(ano).append("'");
		sb.append(" AND gect.semestre = '").append(semestre).append("'");
		sb.append(" AND gect.conteudo = ").append(conteudo);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		GestaoEventoConteudoTurmaVO obj = null;
		if(rs.next()){
			obj = new GestaoEventoConteudoTurmaVO();
			obj.setCodigo(rs.getInt("codigo"));
			obj.setSituacao(SituacaoPBLEnum.valueOf(rs.getString("situacao")));
			if (Uteis.isAtributoPreenchido(rs.getDate("dateliberacao"))) {
				obj.setDateLiberacao(rs.getDate("dateliberacao"));
			}
		}
		return obj;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Boolean consultarGestaoEventoConteudoTurmaVOEstaRealizado(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer turma, Integer disciplina, String ano, String semestre, Integer conteudo,  UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select  ");
		sb.append(" (case when gect.situacao = 'REALIZADO' then true else false end) validacao  ");
		sb.append(" from gestaoeventoconteudoturma as gect    ");
		sb.append(" inner join conteudounidadepaginarecursoeducacional as cupre on cupre.codigo = gect.conteudounidadepaginarecursoeducacional ");
		sb.append("	WHERE cupre.codigo = ").append(codigoConteudoUnidadePaginaRecursoEducacional);
		sb.append(" AND gect.turma = ").append(turma);
		sb.append(" AND gect.disciplina = ").append(disciplina);
		sb.append(" AND gect.ano = '").append(ano).append("'");
		sb.append(" AND gect.semestre = '").append(semestre).append("'");
		sb.append(" AND gect.conteudo = ").append(conteudo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		Boolean existeGestao = null;
		if(tabelaResultado.next()){
			existeGestao = tabelaResultado.getBoolean("validacao");	
		}
		return existeGestao == null || !existeGestao ? false:true;
	}

	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Boolean consultarGestaoEventoConteudoTurmaExistePorCodigoConteudoUnidadePaginaRecursoEducacionalPorDisciplinaDiferente(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer disciplina, Integer conteudo) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select  ");
		sb.append(" gect.codigo  ");
		sb.append(" from gestaoeventoconteudoturma as gect    ");
		sb.append("	WHERE gect.conteudounidadepaginarecursoeducacional = ").append(codigoConteudoUnidadePaginaRecursoEducacional);
		sb.append(" AND gect.conteudo = ").append(conteudo);
		sb.append(" AND gect.disciplina != ").append(disciplina);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		Integer existeGestao = null;
		if(tabelaResultado.next()){
			existeGestao = tabelaResultado.getInt("codigo");	
		}
		return existeGestao != null && existeGestao > 0 ? true:false;
	}


	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public GestaoEventoConteudoTurmaVO consultarRapidaGestaoEventoConteudo(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer codigoConteudo, Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sb = getSelectConsultaRapidaGestaoEventoConteudo();
		sb.append(" FROM gestaoeventoconteudoturma gect   ");
		sb.append(" inner join conteudounidadepaginarecursoeducacional cupre on gect.conteudounidadepaginarecursoeducacional = cupre.codigo");
		sb.append(" inner join conteudounidadepagina cup on cup.codigo = cupre.conteudounidadepagina ");
		sb.append(" inner join unidadeconteudo uc on uc.codigo = cup.unidadeconteudo ");
		sb.append(" inner join conteudo on conteudo.codigo = uc.conteudo ");
		sb.append(" left join turma on turma.codigo = gect.turma ");
		sb.append(" left join disciplina on disciplina.codigo = gect.disciplina ");
		sb.append(" where cupre.codigo = ").append(codigoConteudoUnidadePaginaRecursoEducacional);
		sb.append(" AND gect.turma = ").append(codigoTurma);
		sb.append(" AND gect.disciplina = ").append(codigoDisciplina);
		sb.append(" AND gect.ano = '").append(ano).append("'");
		sb.append(" AND gect.semestre = '").append(semestre).append("'");
		sb.append(" order by cupre.ordemapresentacao");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return montarDadosRapido(rs, nivelMontarDados, usuarioVO);
		}
		return new GestaoEventoConteudoTurmaVO();
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private StringBuilder getSelectConsultaRapidaGestaoEventoConteudo() {

		StringBuilder sb = new StringBuilder("SELECT gect.codigo as \"gect.codigo\", gect.turma as \"gect.turma\", gect.conteudo as \"gect.conteudo\", ");
		sb.append(" gect.disciplina as \"gect.disciplina\", gect.ano as \"gect.ano\", gect.semestre as \"gect.semestre\", gect.formulaCalculoNotaFinalGeral as \"gect.formulaCalculoNotaFinalGeral\", ");
		sb.append(" gect.tiporecurso as \"gect.tiporecurso\", gect.situacao as \"gect.situacao\", gect.dateliberacao as \"gect.dateliberacao\", ");
		sb.append(" cupre.codigo as \"cupre.codigo\", cupre.tiporecursoeducacional as \"cupre.tiporecursoeducacional\", cupre.titulo as \"cupre.titulo\", ");
		sb.append(" cupre.texto as \"cupre.texto\", cupre.descricao as \"cupre.descricao\", cupre.caminhobaserepositorio as \"cupre.caminhobaserepositorio\", cupre.nomerealarquivo as \"cupre.nomerealarquivo\", cupre.nomefisicoarquivo as \"cupre.nomefisicoarquivo\", ");
		sb.append(" cupre.recursoeducacional as \"cupre.recursoeducacional\", cupre.manterrecursodisponivelpagina as \"cupre.manterrecursodisponivelpagina\",  ");
		sb.append(" cupre.ordemapresentacao as \"cupre.ordemapresentacao\", cupre.momentoapresentacaorecursoeducacional as \"cupre.momentoapresentacaorecursoeducacional\", cupre.datacadastro as \"cupre.datacadastro\", ");
		sb.append(" cupre.usuariocadastro as \"cupre.usuariocadastro\", cupre.dataalteracao as \"cupre.dataalteracao\", cupre.usuarioalteracao as \"cupre.usuarioalteracao\", cupre.requerliberacaoprofessor as \"cupre.requerliberacaoprofessor\", ");
		sb.append(" cupre.altura as \"cupre.altura\", cupre.largura as \"cupre.largura\", cupre.apresentarlegenda as \"cupre.apresentarlegenda\", cupre.tipografico as \"cupre.tipografico\", ");
		sb.append(" cupre.autoavaliacao as \"cupre.autoavaliacao\", cupre.alunoavaliaaluno as \"cupre.alunoavaliaaluno\", cupre.professoravaliaaluno as \"cupre.professoravaliaaluno\", cupre.formulacalculonotafinal as \"cupre.formulacalculonotafinal\", ");
		sb.append(" cupre.utilizarnotaconceito as \"cupre.utilizarnotaconceito\", cupre.permitealunoavancarconteudosemlancarnota as \"cupre.permitealunoavancarconteudosemlancarnota\", cupre.faixaminimanotaautoavaliacao as \"cupre.faixaminimanotaautoavaliacao\",  ");
		sb.append(" cupre.faixamaximanotaautoavaliacao as \"cupre.faixamaximanotaautoavaliacao\", cupre.faixaminimanotaalunoavaliaaluno as \"cupre.faixaminimanotaalunoavaliaaluno\", cupre.faixamaximanotaalunoavaliaaluno as \"cupre.faixamaximanotaalunoavaliaaluno\",  ");
		sb.append(" cupre.faixaminimanotaprofessoravaliaaluno as \"cupre.faixaminimanotaprofessoravaliaaluno\", cupre.faixamaximanotaprofessoravaliaaluno as \"cupre.faixamaximanotaprofessoravaliaaluno\",  ");
		sb.append(" cupre.tituloEixoX as \"cupre.tituloEixoX\", cupre.tituloEixoY as \"cupre.tituloEixoY\", cupre.valorGrafico as \"cupre.valorGrafico\", ");
		sb.append(" cupre.categoriaGrafico as \"cupre.categoriaGrafico\", ");
		sb.append(" cup.codigo as \"cup.codigo\", cup.tiporecursoeducacional as \"cup.tiporecursoeducacional\", cup.titulo as \"cup.titulo\", cup.texto as \"cup.texto\", ");
		sb.append(" uc.codigo as \"uc.codigo\", uc.titulo as \"uc.titulo\", uc.ordem as \"uc.ordem\", ");
		sb.append(" ((select count(codigo) from ConteudoUnidadePagina where ConteudoUnidadePagina.unidadeConteudo = uc.codigo)) as \"uc.paginas\", ");
		sb.append(" conteudo.codigo as \"conteudo.codigo\", conteudo.descricao as \"conteudo.descricao\", conteudo.situacaoconteudo as \"conteudo.situacaoconteudo\", ");
		sb.append(" conteudo.controlartempo as \"conteudo.controlartempo\", conteudo.controlarponto as \"conteudo.controlarponto\", conteudo.usoexclusivoprofessor as \"conteudo.usoexclusivoprofessor\", ");
		sb.append(" turma.identificadorTurma as \"turma.identificadorTurma\", disciplina.nome as \"disciplina.nome\" ");
		
		return sb;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public GestaoEventoConteudoTurmaVO consultarGestaoEventoConteudoComAvaliacaoPbl(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer codigoConteudo, Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = getSelectConsultaRapidaGestaoEventoConteudoComAvaliacaoPBL();
		sb.append(" where cupre.codigo = ").append(codigoConteudoUnidadePaginaRecursoEducacional);
		sb.append(" AND gect.turma = ").append(codigoTurma);
		sb.append(" AND gect.disciplina = ").append(codigoDisciplina);
		sb.append(" AND gect.ano = '").append(ano).append("'");
		sb.append(" AND gect.semestre = '").append(semestre).append("'");
		sb.append(" order by ordemtipoavaliacao, avaliado.nome, avaliador.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<GestaoEventoConteudoTurmaVO> lista = montarDadosGestaoEventoConteudoComAvaliacaoPBL(rs, nivelMontarDados, usuarioLogado);
		if (lista.isEmpty()) {
			return new GestaoEventoConteudoTurmaVO();
		}
		return lista.get(0);

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private StringBuilder getSelectConsultaRapidaGestaoEventoConteudoComAvaliacaoPBL() {
		StringBuilder sb = getSelectConsultaRapidaGestaoEventoConteudo();
		sb.append(" , gectap.codigo as \"gectap.codigo\", gectap.tipoavaliacao as \"gectap.tipoavaliacao\", gectap.nota as \"gectap.nota\", ");
		sb.append(" gectap.notaconceitoavaliacaopbl as \"gectap.notaconceitoavaliacaopbl\", gectap.situacao as \"gectap.situacao\", gectap.matricula as \"gectap.matricula\", ");
		sb.append(" gectap.notalancada as \"gectap.notalancada\", gectap.matriculaperiodoturmadisciplinaavaliado as \"gectap.matriculaperiodoturmadisciplinaavaliado\",  ");
		sb.append(" gectap.gestaoeventoconteudoturma as \"gectap.gestaoeventoconteudoturma\", ");
		sb.append(" avaliador.codigo as \"avaliador.codigo\", avaliador.nome as \"avaliador.nome\", ");
		sb.append(" avaliado.codigo as \"avaliado.codigo\", avaliado.nome as \"avaliado.nome\", ");
		sb.append(" nca.codigo as \"nca.codigo\", nca.conceito as \"nca.conceito\", ");
		sb.append(" nca.notacorrespondente as \"nca.notacorrespondente\", nca.tipoavaliacao as \"nca.tipoavaliacao\", ");
		sb.append(" nca.conteudounidadepaginarecursoeducacional as \"nca.conteudounidadepaginarecursoeducacional\",");
		sb.append(" (case when gectap.tipoavaliacao = 'AUTO_AVALIACAO' then 1 ");
		sb.append(" when gectap.tipoavaliacao = 'ALUNO_AVALIA_ALUNO' then 2 ");
		sb.append(" when gectap.tipoavaliacao = 'PROFESSOR_AVALIA_ALUNO' then 3");
		sb.append(" when gectap.tipoavaliacao = 'RESULTADO_FINAL' then 4");
		sb.append(" else 5 end ) as ordemtipoavaliacao ");
		sb.append(" FROM gestaoeventoconteudoturma gect   ");
		sb.append(" inner join conteudounidadepaginarecursoeducacional cupre on gect.conteudounidadepaginarecursoeducacional = cupre.codigo");
		sb.append(" inner join conteudounidadepagina cup on cup.codigo = cupre.conteudounidadepagina ");
		sb.append(" inner join unidadeconteudo uc on uc.codigo = cup.unidadeconteudo ");
		sb.append(" inner join conteudo on conteudo.codigo = uc.conteudo ");
		sb.append(" left join gestaoeventoconteudoturmaavaliacaopbl as gectap on gectap.gestaoeventoconteudoturma = gect.codigo ");
		sb.append(" left join pessoa as avaliador on avaliador.codigo = gectap.avaliador ");
		sb.append(" left join pessoa as avaliado on avaliado.codigo = gectap.avaliado ");
		sb.append(" left join notaconceitoavaliacaopbl as nca on nca.codigo = gectap.notaconceitoavaliacaopbl ");
		sb.append(" left join turma on turma.codigo = gect.turma ");
		sb.append(" left join disciplina on disciplina.codigo = gect.disciplina ");

		return sb;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private GestaoEventoConteudoTurmaVO consultarGestaoEventoConteudoComAvaliacaoResultadoFinalGeral(ConteudoVO conteudo, Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = getSelectConsultaRapidaGestaoEventoConteudoComAvaliacaoResultadoFinalGeral();
		sb.append(" FROM gestaoeventoconteudoturma gect   ");
		sb.append(" INNER JOIN conteudo ON conteudo.codigo = gect.conteudo ");
		sb.append(" INNER join turma on turma.codigo = gect.turma ");
		sb.append(" INNER join disciplina on disciplina.codigo = gect.disciplina ");
		sb.append(" inner join gestaoeventoconteudoturmaavaliacaopbl as gectap on gectap.gestaoeventoconteudoturma = gect.codigo ");
		sb.append(" inner join pessoa as avaliador on avaliador.codigo = gectap.avaliador ");
		sb.append(" inner join pessoa as avaliado on avaliado.codigo = gectap.avaliado ");
		sb.append(" where gect.conteudo = ").append(conteudo.getCodigo());
		sb.append(" AND gect.turma = ").append(codigoTurma);
		sb.append(" AND gect.disciplina = ").append(codigoDisciplina);
		sb.append(" AND gect.ano = '").append(ano).append("'");
		sb.append(" AND gect.semestre = '").append(semestre).append("'");
		sb.append(" AND gect.tiporecurso = '").append(TipoRecursoEnum.CONTEUDO).append("'");
		sb.append(" order by avaliado.nome, gectap.gestaoeventoconteudoturma ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<GestaoEventoConteudoTurmaVO> lista = montarDadosGestaoEventoConteudoComAvaliacaoResultadoFinalGeral(rs, usuarioLogado);
		if (lista.isEmpty()) {
			return new GestaoEventoConteudoTurmaVO();
		}
		return lista.get(0);

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private StringBuilder getSelectConsultaRapidaGestaoEventoConteudoComAvaliacaoResultadoFinalGeral() {
		StringBuilder sb = new StringBuilder("");
		sb.append(" SELECT gect.codigo as \"gect.codigo\",  ");
		sb.append(" gect.turma as \"gect.turma\", gect.conteudo as \"gect.conteudo\", ");
		sb.append(" gect.disciplina as \"gect.disciplina\", gect.ano as \"gect.ano\", gect.semestre as \"gect.semestre\", ");
		sb.append(" gect.tiporecurso as \"gect.tiporecurso\", gect.situacao as \"gect.situacao\", gect.dateliberacao as \"gect.dateliberacao\", ");
		sb.append(" gect.formulaCalculoNotaFinalGeral as \"gect.formulaCalculoNotaFinalGeral\", ");
		sb.append(" 0 AS \"cupre.codigo\", '' AS \"cupre.descricao\", '' as \"temaassunto.nome\", ");
		sb.append(" conteudo.codigo as \"conteudo.codigo\", conteudo.descricao as \"conteudo.descricao\", conteudo.situacaoconteudo as \"conteudo.situacaoconteudo\", ");
		sb.append(" turma.identificadorTurma as \"turma.identificadorTurma\", disciplina.nome as \"disciplina.nome\", ");
		sb.append(" gectap.codigo as \"gectap.codigo\", gectap.tipoavaliacao as \"gectap.tipoavaliacao\", gectap.nota as \"gectap.nota\", ");
		sb.append(" gectap.situacao as \"gectap.situacao\", gectap.matricula as \"gectap.matricula\", ");
		sb.append(" gectap.notalancada as \"gectap.notalancada\", gectap.matriculaperiodoturmadisciplinaavaliado as \"gectap.matriculaperiodoturmadisciplinaavaliado\",  ");
		sb.append(" gectap.notaconceitoavaliacaopbl as \"gectap.notaconceitoavaliacaopbl\", gectap.gestaoeventoconteudoturma as \"gectap.gestaoeventoconteudoturma\", ");
		sb.append(" avaliador.codigo as \"avaliador.codigo\", avaliador.nome as \"avaliador.nome\", ");
		sb.append(" avaliado.codigo as \"avaliado.codigo\", avaliado.nome as \"avaliado.nome\"  ");
		return sb;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private Boolean consultarSeExisteGestaoEventoConteudoTurmaNaoRealizado(Integer conteudo, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder(" select (case when count(cupre.codigo) = count(gect.codigo) then false else true end) validacao ");
		sb.append(" from conteudo  ");
		sb.append(" inner join unidadeconteudo uc on uc.conteudo = conteudo.codigo    ");
		sb.append(" inner join conteudounidadepagina cup on cup.unidadeconteudo = uc.codigo    ");
		sb.append(" inner join conteudounidadepaginarecursoeducacional cupre on cupre.conteudounidadepagina = cup.codigo  ");
		sb.append(" left join gestaoeventoconteudoturma gect on gect.conteudounidadepaginarecursoeducacional = cupre.codigo and gect.situacao = 'REALIZADO'  ");
		sb.append(" where cupre.tiporecursoeducacional = 'AVALIACAO_PBL'   ");
		sb.append(" and conteudo.codigo =  ").append(conteudo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		Boolean existe = null;
		if (tabelaResultado.next()) {
			existe = tabelaResultado.getBoolean("validacao");
		}
		return existe == null || existe ? true : false;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private List<GestaoEventoConteudoTurmaVO> montarDadosGestaoEventoConteudoComAvaliacaoResultadoFinalGeral(SqlRowSet rs, UsuarioVO usuario) throws Exception {
		List<GestaoEventoConteudoTurmaVO> vetResultado = new ArrayList<GestaoEventoConteudoTurmaVO>(0);
		while (rs.next()) {
			GestaoEventoConteudoTurmaVO obj = consultarSeExisteGestaoEventoConteudoTurmaVOJaCarregado(vetResultado, rs.getInt("gect.codigo"));
			if (!Uteis.isAtributoPreenchido(obj)) {
				obj = montarDadosRapido(rs, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			}
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().carregarEstruturaDeDadosDaAvaliacaoPBLVO(obj, rs, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			adicionarGestaoEventoConteudoTurmaVOJaCarregado(vetResultado, obj);
		}
		return vetResultado;

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private List<GestaoEventoConteudoTurmaVO> montarDadosGestaoEventoConteudoComAvaliacaoPBL(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<GestaoEventoConteudoTurmaVO> vetResultado = new ArrayList<GestaoEventoConteudoTurmaVO>(0);
		while (rs.next()) {
			GestaoEventoConteudoTurmaVO obj = consultarSeExisteGestaoEventoConteudoTurmaVOJaCarregado(vetResultado, rs.getInt("gect.codigo"));
			if (!Uteis.isAtributoPreenchido(obj)) {
				obj = montarDadosRapido(rs, nivelMontarDados, usuario);
			}
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().carregarEstruturaDeDadosDaAvaliacaoPBLVO(obj, rs, nivelMontarDados, usuario);
			adicionarGestaoEventoConteudoTurmaVOJaCarregado(vetResultado, obj);
		}
		return vetResultado;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private GestaoEventoConteudoTurmaVO consultarSeExisteGestaoEventoConteudoTurmaVOJaCarregado(List<GestaoEventoConteudoTurmaVO> vetResultado, Integer codigo) {
		for (GestaoEventoConteudoTurmaVO obj : vetResultado) {
			if (obj.getCodigo().equals(codigo)) {
				return obj;
			}
		}
		return new GestaoEventoConteudoTurmaVO();
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void adicionarGestaoEventoConteudoTurmaVOJaCarregado(List<GestaoEventoConteudoTurmaVO> vetResultado, GestaoEventoConteudoTurmaVO obj) {
		int index = 0;
		for (GestaoEventoConteudoTurmaVO objExistente : vetResultado) {
			if (objExistente.getCodigo().equals(obj.getCodigo())) {
				vetResultado.set(index, obj);
				return;
			}
			index++;
		}
		vetResultado.add(obj);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private GestaoEventoConteudoTurmaVO montarDadosRapido(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		GestaoEventoConteudoTurmaVO obj = new GestaoEventoConteudoTurmaVO();
		obj.setNovoObj(false);
		obj.setCodigo(rs.getInt("gect.codigo"));
		obj.getTurmaVO().setCodigo(rs.getInt("gect.turma"));
		obj.getTurmaVO().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
		obj.getConteudoVO().setCodigo(rs.getInt("gect.conteudo"));
		obj.getDisciplinaVO().setCodigo(rs.getInt("gect.disciplina"));
		obj.getDisciplinaVO().setNome(rs.getString("disciplina.nome"));
		obj.setAno(rs.getString("gect.ano"));
		obj.setSemestre(rs.getString("gect.semestre"));
		obj.setFormulaCalculoNotaFinalGeral(rs.getString("gect.formulacalculonotafinalgeral"));
		obj.setTipoRecurso(TipoRecursoEnum.valueOf(rs.getString("gect.tiporecurso")));
		obj.setSituacao(SituacaoPBLEnum.valueOf(rs.getString("gect.situacao")));
		if (Uteis.isAtributoPreenchido(rs.getDate("gect.dateliberacao"))) {
			obj.setDateLiberacao(rs.getDate("gect.dateliberacao"));
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}

		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setCodigo(rs.getInt("cupre.codigo"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setTipoRecursoEducacional(TipoRecursoEducacionalEnum.valueOf(rs.getString("cupre.tiporecursoeducacional")));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setTitulo(rs.getString("cupre.titulo"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setTexto(rs.getString("cupre.texto"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setDescricao(rs.getString("cupre.descricao"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setCaminhoBaseRepositorio(rs.getString("cupre.caminhobaserepositorio"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setNomeFisicoArquivo(rs.getString("cupre.nomefisicoarquivo"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setNomeRealArquivo(rs.getString("cupre.nomerealarquivo"));

		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getRecursoEducacional().setCodigo(rs.getInt("cupre.recursoeducacional"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setManterRecursoDisponivelPagina(rs.getBoolean("cupre.manterrecursodisponivelpagina"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setOrdemApresentacao(rs.getInt("cupre.ordemapresentacao"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setMomentoApresentacaoRecursoEducacional(MomentoApresentacaoRecursoEducacionalEnum.valueOf(rs.getString("cupre.momentoapresentacaorecursoeducacional")));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setDataCadastro(rs.getDate("cupre.datacadastro"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getUsuarioAlteracao().setCodigo(rs.getInt("cupre.usuarioalteracao"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getUsuarioCadastro().setCodigo(rs.getInt("cupre.usuariocadastro"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setDataAlteracao(rs.getDate("cupre.dataalteracao"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setRequerLiberacaoProfessor(rs.getBoolean("cupre.requerliberacaoprofessor"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setAltura(rs.getInt("cupre.altura"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setLargura(rs.getInt("cupre.largura"));

		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setAltura(rs.getInt("cupre.altura"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setLargura(rs.getInt("cupre.largura"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setTipoGrafico(TipoGraficoEnum.valueOf(rs.getString("cupre.tipografico")));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setTituloEixoX(rs.getString("cupre.tituloEixoX"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setTituloEixoY(rs.getString("cupre.tituloEixoY"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setValorGrafico(rs.getString("cupre.valorGrafico"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setCategoriaGrafico(rs.getString("cupre.categoriaGrafico"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setApresentarLegenda(rs.getBoolean("cupre.apresentarlegenda"));

		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setAutoAvaliacao(rs.getBoolean("cupre.autoAvaliacao"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setAlunoAvaliaAluno(rs.getBoolean("cupre.alunoAvaliaAluno"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setProfessorAvaliaAluno(rs.getBoolean("cupre.professorAvaliaAluno"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setFormulaCalculoNotaFinal(rs.getString("cupre.formulaCalculoNotaFinal"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setUtilizarNotaConceito(rs.getBoolean("cupre.utilizarNotaConceito"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setPermiteAlunoAvancarConteudoSemLancarNota(rs.getBoolean("cupre.permiteAlunoAvancarConteudoSemLancarNota"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setFaixaMinimaNotaAutoAvaliacao(rs.getDouble("cupre.faixaMinimaNotaAutoAvaliacao"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setFaixaMaximaNotaAutoAvaliacao(rs.getDouble("cupre.faixaMaximaNotaAutoAvaliacao"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setFaixaMinimaNotaAlunoAvaliaAluno(rs.getDouble("cupre.faixaMinimaNotaAlunoAvaliaAluno"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setFaixaMaximaNotaAlunoAvaliaAluno(rs.getDouble("cupre.faixaMaximaNotaAlunoAvaliaAluno"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setFaixaMinimaNotaProfessorAvaliaAluno(rs.getDouble("cupre.faixaMinimaNotaProfessorAvaliaAluno"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().setFaixaMaximaNotaProfessorAvaliaAluno(rs.getDouble("cupre.faixaMaximaNotaProfessorAvaliaAluno"));

		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().setCodigo(rs.getInt("cup.codigo"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().setTipoRecursoEducacional(TipoRecursoEducacionalEnum.valueOf(rs.getString("cup.tiporecursoeducacional")));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().setTitulo(rs.getString("cup.titulo"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().setTexto(rs.getString("cup.texto"));

		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().getUnidadeConteudo().setCodigo(rs.getInt("uc.codigo"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().getUnidadeConteudo().setTitulo(rs.getString("uc.titulo"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().getUnidadeConteudo().setOrdem(rs.getInt("uc.ordem"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().getUnidadeConteudo().setPaginas(rs.getInt("uc.paginas"));

		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().setCodigo(rs.getInt("conteudo.codigo"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().setDescricao(rs.getString("conteudo.descricao"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().setSituacaoConteudo(SituacaoConteudoEnum.valueOf(rs.getString("conteudo.situacaoconteudo")));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().setControlarTempo(rs.getBoolean("conteudo.controlartempo"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().setControlarPonto(rs.getBoolean("conteudo.controlarponto"));
		obj.getConteudoUnidadePaginaRecursoEducacionalVO().getConteudoUnidadePagina().getUnidadeConteudo().getConteudo().setUsoExclusivoProfessor(rs.getBoolean("conteudo.usoexclusivoprofessor"));

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS || nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
			obj.setGestaoEventoConteudoTurmaResponsavelAtaVOs(getFacadeFactory().getGestaoEventoConteudoTurmaResponsavelAtaFacade().consultarPorCodigoGestaoEventoConteudoTurmaVO(obj.getCodigo(), nivelMontarDados, usuario));
			obj.setGestaoEventoConteudoTurmaInteracaoAtaVOs(getFacadeFactory().getGestaoEventoConteudoTurmaInteracaoAtaFacade().consultarPorCodigoGestaoEventoConteudoTurmaVO(obj.getCodigo(), nivelMontarDados, usuario));
		}
		return obj;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void carregarDadosParaFechamentoNotasDosEventos(ConteudoVO conteudo, GestaoEventoConteudoTurmaVO filtroConsulta, UsuarioVO usuarioLogado) throws Exception {
		if (consultarSeExisteGestaoEventoConteudoTurmaNaoRealizado(conteudo.getCodigo(), usuarioLogado)) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_aluno"));
		}
		GestaoEventoConteudoTurmaVO obj = consultarGestaoEventoConteudoComAvaliacaoResultadoFinalGeral(conteudo, filtroConsulta.getTurmaVO().getCodigo(), filtroConsulta.getDisciplinaVO().getCodigo(), filtroConsulta.getAno(), filtroConsulta.getSemestre(), usuarioLogado);
		if (Uteis.isAtributoPreenchido(obj)) {
			conteudo.setGestaoEventoConteudoTurmaVO(obj);
			getFacadeFactory().getGestaoEventoConteudoTurmaAvaliacaoPBLFacade().montarDadosGestaoEventoConteudoAvaliacaoResultadoFinal(conteudo, usuarioLogado);
		} else {
			conteudo.setGestaoEventoConteudoTurmaVO(filtroConsulta);
			montarGestaoEventoConteudoTurmaNotaFinalGeralNaoInicializado(conteudo, usuarioLogado);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDadosGestaoEventoConteudoTurmaResponsavelAtaVO(GestaoEventoConteudoTurmaResponsavelAtaVO ata) throws Exception {

		if (!Uteis.isAtributoPreenchido(ata.getAluno())) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_aluno"));
		}
		if (!Uteis.isAtributoPreenchido(ata.getFuncao())) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_papel"));
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void adicionarGestaoEventoConteudoTurmaResponsavelAtaVO(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaResponsavelAtaVO ata, UsuarioVO usuario) throws Exception {
		int index = 0;
		validarDadosGestaoEventoConteudoTurmaResponsavelAtaVO(ata);
		ata.setAluno(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(ata.getAluno().getCodigo(), false, false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		ata.setGestaoEventoConteudoTurmaVO(turma);
		for (GestaoEventoConteudoTurmaResponsavelAtaVO objExistente : turma.getGestaoEventoConteudoTurmaResponsavelAtaVOs()) {
			if (objExistente.equalsGestaoEventoConteudoAta(ata)) {
				turma.getGestaoEventoConteudoTurmaResponsavelAtaVOs().set(index, ata);
				return;
			}
			index++;
		}
		turma.getGestaoEventoConteudoTurmaResponsavelAtaVOs().add(ata);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removerGestaoEventoConteudoTurmaResponsavelAtaVO(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaResponsavelAtaVO ata) throws Exception {
		Iterator<GestaoEventoConteudoTurmaResponsavelAtaVO> i = turma.getGestaoEventoConteudoTurmaResponsavelAtaVOs().iterator();
		while (i.hasNext()) {
			GestaoEventoConteudoTurmaResponsavelAtaVO objExistente = (GestaoEventoConteudoTurmaResponsavelAtaVO) i.next();
			if (objExistente.equalsGestaoEventoConteudoAta(ata)) {
				i.remove();
				return;
			}
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDadosGestaoEventoConteudoTurmaInteracaoAtaVO(GestaoEventoConteudoTurmaInteracaoAtaVO ata) throws Exception {

		if (!Uteis.isAtributoPreenchido(ata.getInteracao())) {
			throw new Exception(UteisJSF.internacionalizar("msg_ConteudoUnidadePagina_aluno"));
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void adicionarGestaoEventoConteudoTurmaInteracaoAtaVO(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaInteracaoAtaVO ata, UsuarioVO usuario) throws Exception {
		int index = 0;
		validarDadosGestaoEventoConteudoTurmaInteracaoAtaVO(ata);
		ata.setGestaoEventoConteudoTurmaVO(turma);
		for (GestaoEventoConteudoTurmaInteracaoAtaVO objExistente : turma.getGestaoEventoConteudoTurmaInteracaoAtaVOs()) {
			if (objExistente.equalsGestaoEventoConteudoInteracaoAta(ata)) {
				turma.getGestaoEventoConteudoTurmaInteracaoAtaVOs().set(index, ata);
				return;
			}
			index++;
		}
		turma.getGestaoEventoConteudoTurmaInteracaoAtaVOs().add(ata);
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removerGestaoEventoConteudoTurmaInteracaoAtaVO(GestaoEventoConteudoTurmaVO turma, GestaoEventoConteudoTurmaInteracaoAtaVO ata) throws Exception {
		Iterator<GestaoEventoConteudoTurmaInteracaoAtaVO> i = turma.getGestaoEventoConteudoTurmaInteracaoAtaVOs().iterator();
		while (i.hasNext()) {
			GestaoEventoConteudoTurmaInteracaoAtaVO objExistente = (GestaoEventoConteudoTurmaInteracaoAtaVO) i.next();
			if (objExistente.equalsGestaoEventoConteudoInteracaoAta(ata)) {
				i.remove();
				return;
			}
		}
	}
	
	
	
	public DataTable criarTabelaDinanica(FacesContext facesContext, GestaoEventoConteudoTurmaVO obj) {
	    DataTable data = new DataTable();
	    
	    ValueExpression lista = facesContext.getApplication().getExpressionFactory()
	        .createValueExpression(facesContext.getELContext(), 
	        "#{GestaoEventoConteudoTurmaControle.conteudoUnidadePaginaRecursoEducacionalVO.gestaoEventoConteudoTurmaVO.gestaoEventoConteudoTurmaAvaliacaoPBLVOs}", 
	        Object.class); 
	    data.setValueExpression("value", lista);
	    data.setId("data");
	    data.setVar("gestaoEventoConteudoItem");
	    // Remove this line: data.setRowKeyVar("rowAluno");
	    data.setRowIndexVar("rowAluno");

	    
	    int posicao = 1;
	    colunaID(facesContext, data, posicao);    
	    colunaAlunos(facesContext, data, posicao);
	    posicao++;
	    
	    for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao : obj.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
	        criarColunaDinamica(facesContext, data, avaliacao, posicao);
	        posicao++;
	    }
	    
	    return data;
	}


	public void colunaID(FacesContext facesContext, DataTable data, int posicao) {
	    HtmlOutputText output0 = new HtmlOutputText();
	    output0.setId("outputID");
	    output0.setStyleClass("tituloCampos");
	    ValueExpression textve = facesContext.getApplication().getExpressionFactory()
	        .createValueExpression(facesContext.getELContext(), "A#{rowAluno+1}", Object.class); 
	    output0.setValueExpression("value", textve);
	    
	    HtmlPanelGroup panelGroup = new HtmlPanelGroup();
	    panelGroup.setId("panelGroupID");
	    panelGroup.getChildren().add(output0);
	    
	    UIOutput header = new UIOutput();
	    header.setValue("ID");
	    
	    Column column0 = new Column();
	    column0.setId("columnID");
	    column0.setHeader(header);
	    column0.getChildren().add(panelGroup);
	    data.getChildren().add(column0);
	}

	public void colunaAlunos(FacesContext facesContext, DataTable data, int posicao) {
	    HtmlOutputText output0 = new HtmlOutputText();
	    output0.setId("outputAlunos");
	    ValueExpression textve = facesContext.getApplication().getExpressionFactory()
	        .createValueExpression(facesContext.getELContext(), 
	        "#{gestaoEventoConteudoItem.avaliador.nome}", Object.class); 
	    output0.setValueExpression("value", textve);
	    output0.setStyleClass("tituloCampos");
	    
	    HtmlPanelGroup panelGroup = new HtmlPanelGroup();
	    panelGroup.setId("panelGroupAlunos");
	    panelGroup.getChildren().add(output0);
	    
	    UIOutput header = new UIOutput();
	    header.setValue("Alunos");
	    
	    Column column0 = new Column();
	    column0.setId("columnAlunos");
	    column0.setHeader(header);
	    column0.getChildren().add(panelGroup);
	    data.getChildren().add(column0);
	}

	private void criarColunaDinamica(FacesContext facesContext, DataTable data, 
	                                 GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao, int posicao) {
	    HtmlOutputText output = new HtmlOutputText();
	    output.setId("output" + posicao);
	    output.setStyleClass("tituloCampos");
	    
	    String expression = "#{gestaoEventoConteudoItem.nota}"; // Ajuste conforme necessário
	    ValueExpression ve = facesContext.getApplication().getExpressionFactory()
	        .createValueExpression(facesContext.getELContext(), expression, Object.class);
	    output.setValueExpression("value", ve);
	    
	    HtmlPanelGroup panelGroup = new HtmlPanelGroup();
	    panelGroup.setId("panelGroup" + posicao);
	    panelGroup.getChildren().add(output);
	    
	    UIOutput header = new UIOutput();
	    header.setValue(avaliacao.getAvaliado().getNome()); // Ou outro texto apropriado
	    
	    Column column = new Column();
	    column.setId("column" + posicao);
	    column.setHeader(header);
	    column.getChildren().add(panelGroup);
	    data.getChildren().add(column);
	}

}
