package negocio.facade.jdbc.ead;

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

import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.academico.ConteudoUnidadePaginaVO;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.academico.enumeradores.TipoRecursoEducacionalEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaAvaliacaoPBLVO;
import negocio.comuns.ead.GestaoEventoConteudoTurmaVO;
import negocio.comuns.ead.NotaConceitoAvaliacaoPBLVO;
import negocio.comuns.ead.enumeradores.SituacaoPBLEnum;
import negocio.comuns.ead.enumeradores.TipoAvaliacaoPBLEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.GestaoEventoConteudoTurmaAvaliacaoPBLInterfaceFacade;

/**
 * @author Victor Hugo de Paula Costa - 7 de jul de 2016
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class GestaoEventoConteudoTurmaAvaliacaoPBL extends ControleAcesso implements GestaoEventoConteudoTurmaAvaliacaoPBLInterfaceFacade {

	private static final long serialVersionUID = 1L;
	/**
	 * @author Victor Hugo de Paula Costa - 7 de jul de 2016
	 */

	protected static String idEntidade;

	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "";
		}
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		GestaoEventoConteudoTurmaAvaliacaoPBL.idEntidade = idEntidade;
	}

	public GestaoEventoConteudoTurmaAvaliacaoPBL() throws Exception {
		super();
		setIdEntidade("GestaoEventoConteudoTurmaAvaliacaoPBL");
	}

	@Override
	public void validarDados(GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO) throws ConsistirException {
		ConsistirException consistirException = new ConsistirException();

		if (!consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(GestaoEventoConteudoTurmaAvaliacaoPBLVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void incluir(final GestaoEventoConteudoTurmaAvaliacaoPBLVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			GestaoEventoConteudoTurmaAvaliacaoPBL.incluir(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO gestaoeventoconteudoturmaavaliacaopbl(");
			sql.append("            gestaoeventoconteudoturma, tipoavaliacao, avaliador, ");
			sql.append("            avaliado, nota, notaconceitoavaliacaopbl, situacao, matricula, matriculaperiodoturmadisciplinaavaliado,");
			sql.append("            notalancada)");
			sql.append("    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					sqlInserir.setInt(1, obj.getGestaoEventoConteudoTurmaVO().getCodigo());
					sqlInserir.setString(2, obj.getTipoAvaliacao().getName());
					sqlInserir.setInt(3, obj.getAvaliador().getCodigo());
					sqlInserir.setInt(4, obj.getAvaliado().getCodigo());
					if (obj.getNota() != null) {
						sqlInserir.setDouble(5, obj.getNota());
					} else {
						sqlInserir.setNull(5, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getNotaConceitoAvaliacaoPBLVO().getCodigo())) {
						sqlInserir.setInt(6, obj.getNotaConceitoAvaliacaoPBLVO().getCodigo());
					} else {
						sqlInserir.setNull(6, 0);
					}
					sqlInserir.setString(7, obj.getSituacao().getName());
					sqlInserir.setString(8, obj.getMatriculaVO().getMatricula());
					sqlInserir.setInt(9, obj.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO().getCodigo());
					sqlInserir.setBoolean(10, obj.getNotaLancada());
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
	public void alterar(final GestaoEventoConteudoTurmaAvaliacaoPBLVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			validarDados(obj);
			GestaoEventoConteudoTurmaAvaliacaoPBL.alterar(getIdEntidade(), verificarAcesso, usuarioVO);
			final StringBuilder sql = new StringBuilder();
			sql.append(" UPDATE gestaoeventoconteudoturmaavaliacaopbl");
			sql.append("   SET gestaoeventoconteudoturma=?, tipoavaliacao=?, avaliador=?, ");
			sql.append("       avaliado=?, nota=?, notaconceitoavaliacaopbl=?, situacao=?, matricula=?, matriculaperiodoturmadisciplinaavaliado=?, ");
			sql.append("       notalancada=? ");
			sql.append(" WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					sqlAlterar.setInt(1, obj.getGestaoEventoConteudoTurmaVO().getCodigo());
					sqlAlterar.setString(2, obj.getTipoAvaliacao().getName());
					sqlAlterar.setInt(3, obj.getAvaliador().getCodigo());
					sqlAlterar.setInt(4, obj.getAvaliado().getCodigo());
					if (obj.getNota() != null) {
						sqlAlterar.setDouble(5, obj.getNota());
					} else {
						sqlAlterar.setNull(5, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getNotaConceitoAvaliacaoPBLVO().getCodigo())) {
						sqlAlterar.setInt(6, obj.getNotaConceitoAvaliacaoPBLVO().getCodigo());
					} else {
						sqlAlterar.setNull(6, 0);
					}
					sqlAlterar.setString(7, obj.getSituacao().getName());
					sqlAlterar.setString(8, obj.getMatriculaVO().getMatricula());
					sqlAlterar.setInt(9, obj.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO().getCodigo());
					sqlAlterar.setBoolean(10, obj.getNotaLancada());
					sqlAlterar.setInt(11, obj.getCodigo());
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuarioVO);
			}
			;
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarNotaGestaoEventoConteudoTurmaAvaliacao(final GestaoEventoConteudoTurmaAvaliacaoPBLVO obj, UsuarioVO usuario) throws Exception {
		try {
			final StringBuilder sb = new StringBuilder(" UPDATE gestaoeventoconteudoturmaavaliacaopbl set ");
			sb.append(" nota=?, notaconceitoavaliacaopbl=?, situacao = ?, notalancada=? ");
			sb.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sb.toString());
					int i = 0;
					if (obj.getNota() != null) {
						sqlAlterar.setDouble(++i, obj.getNota());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getNotaConceitoAvaliacaoPBLVO().getCodigo())) {
						sqlAlterar.setInt(++i, obj.getNotaConceitoAvaliacaoPBLVO().getCodigo());
					} else {
						sqlAlterar.setNull(++i, 0);
					}
					sqlAlterar.setString(++i, obj.getSituacao().name());
					sqlAlterar.setBoolean(++i, obj.getNotaLancada());
					sqlAlterar.setInt(++i, obj.getCodigo());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarSituacaoGestaoEventoConteudoTurmaAvaliacao(final Integer codigo, final SituacaoPBLEnum situacao, final Boolean notaLancada, UsuarioVO usuario) throws Exception {
		try {
			final StringBuilder sb = new StringBuilder(" UPDATE gestaoeventoconteudoturmaavaliacaopbl set ");
			sb.append(" situacao = ?, notalancada=? ");
			sb.append(" WHERE codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sb.toString());
					int i = 0;
					sqlAlterar.setString(++i, situacao.name());
					sqlAlterar.setBoolean(++i, notaLancada);
					sqlAlterar.setInt(++i, codigo);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarSituacaoPorGestaoEventoConteudoTurma(final Integer codigo, final SituacaoPBLEnum situacao,  UsuarioVO usuario) throws Exception {
		try {
			final StringBuilder sb = new StringBuilder(" UPDATE gestaoeventoconteudoturmaavaliacaopbl set ");
			sb.append(" situacao = ? ");
			sb.append(" WHERE gestaoeventoconteudoturma = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sb.toString());
					int i = 0;
					sqlAlterar.setString(++i, situacao.name());
					sqlAlterar.setInt(++i, codigo);
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public GestaoEventoConteudoTurmaAvaliacaoPBLVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		GestaoEventoConteudoTurmaAvaliacaoPBLVO obj = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getGestaoEventoConteudoTurmaVO().setCodigo(tabelaResultado.getInt("gestaoeventoconteudoturma"));
		obj.setTipoAvaliacao(TipoAvaliacaoPBLEnum.valueOf(tabelaResultado.getString("tipoavaliacao")));
		obj.getAvaliador().setCodigo(tabelaResultado.getInt("avaliador"));
		obj.getAvaliado().setCodigo(tabelaResultado.getInt("avaliado"));
		obj.setNota(tabelaResultado.getDouble("nota"));
		if (tabelaResultado.getObject("nota") != null) {
			obj.setNota(tabelaResultado.getDouble("nota"));
		}
		obj.getNotaConceitoAvaliacaoPBLVO().setCodigo(tabelaResultado.getInt("notaconceitoavaliacaopbl"));
		obj.setSituacao(SituacaoPBLEnum.valueOf(tabelaResultado.getString("situacao")));
		obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
		obj.setAvaliado(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getAvaliado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
		obj.setAvaliador(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getAvaliador().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado));
		obj.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO().setCodigo(tabelaResultado.getInt("matriculaperiodoturmadisciplinaavaliado"));
		if (!obj.getNotaConceitoAvaliacaoPBLVO().getCodigo().equals(0)) {
			obj.setNotaConceitoAvaliacaoPBLVO(getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().consultarPorChavePrimaria(obj.getNotaConceitoAvaliacaoPBLVO().getCodigo(), nivelMontarDados, usuarioLogado));
		}
		return obj;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> vetResultado = new ArrayList<GestaoEventoConteudoTurmaAvaliacaoPBLVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		String sql = "SELECT * FROM gestaoeventoconteudoturmaavaliacaopbl WHERE codigo = ?";
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql, codigo), nivelMontarDados, usuarioLogado));
	}	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(final GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		try {
			NotaConceitoAvaliacaoPBL.excluir(getIdEntidade(), verificarAcesso, usuarioVO);
			String sql = "DELETE FROM gestaoeventoconteudoturmaavaliacaopbl WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, gestaoEventoConteudoTurmaAvaliacaoPBLVO.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}	

	/**
	 * 
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> consultarColegasAvaliacaoPBL(Integer disciplina, Integer turma, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT matricula.matricula, matricula.situacao, pessoa.codigo AS pessoa_codigo, ");
		sql.append("pessoa.nome AS pessoanome, turma.identificadorturma as identificadorturma, turma.codigo as codigoturma, disciplina.nome as nomedisciplina, ");
		sql.append("matriculaperiodo.situacaomatriculaperiodo, matriculaperiodoturmadisciplina.codigo as codigomatriculaturmadisciplina, matriculaperiodoturmadisciplina.ano as anomatriculaperiodoturmadisciplina, matriculaperiodoturmadisciplina.semestre as semestrematriculaperiodoturmadisciplina,");
		sql.append("matriculaperiodoturmadisciplina.disciplina as disciplinamatriculaperiodoturmadisciplina ");
		sql.append("FROM matricula ");
		sql.append("INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula ");
		sql.append("INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno ");
		sql.append("LEFT JOIN cidade ON cidade.codigo = pessoa.cidade ");
		sql.append("INNER JOIN matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = MatriculaPeriodo.codigo ");
		sql.append("INNER JOIN turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		sql.append("INNER JOIN disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" WHERE Matricula.matricula = MatriculaPeriodo.matricula ");
		sql.append(" and Pessoa.codigo = Matricula.aluno ");
		sql.append(" AND matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
		sql.append(" AND matriculaperiodoturmadisciplina.turma = ").append(turma);
		sql.append(" AND ((matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE')");
		sql.append("  or matriculaperiodoturmadisciplina.modalidadedisciplina != 'ON_LINE')");
		sql.append(" AND MatriculaPeriodo.ano = '").append(ano).append("'");
		sql.append(" AND MatriculaPeriodo.semestre = '").append(semestre).append("'");
		sql.append(" ORDER BY Pessoa.nome, turma.identificadorturma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> gestaoEventoConteudoTurmaAvaliacaoPBLVOs = new ArrayList<GestaoEventoConteudoTurmaAvaliacaoPBLVO>();
		GestaoEventoConteudoTurmaAvaliacaoPBLVO gestaoEventoConteudoTurmaAvaliacaoPBLVO = null;
		while (tabelaResultado.next()) {
			gestaoEventoConteudoTurmaAvaliacaoPBLVO = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
			gestaoEventoConteudoTurmaAvaliacaoPBLVO.getAvaliador().setCodigo(tabelaResultado.getInt("pessoa_codigo"));
			gestaoEventoConteudoTurmaAvaliacaoPBLVO.getAvaliador().setNome(tabelaResultado.getString("pessoanome"));
			gestaoEventoConteudoTurmaAvaliacaoPBLVO.getAvaliado().setCodigo(tabelaResultado.getInt("pessoa_codigo"));
			gestaoEventoConteudoTurmaAvaliacaoPBLVO.getAvaliado().setNome(tabelaResultado.getString("pessoanome"));
			gestaoEventoConteudoTurmaAvaliacaoPBLVO.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			gestaoEventoConteudoTurmaAvaliacaoPBLVO.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO().setCodigo(tabelaResultado.getInt("codigomatriculaturmadisciplina"));
			gestaoEventoConteudoTurmaAvaliacaoPBLVOs.add(gestaoEventoConteudoTurmaAvaliacaoPBLVO);
		}
		return gestaoEventoConteudoTurmaAvaliacaoPBLVOs;
	}	
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> consultarColegasAvaliacaoPBLNovasMatriculas(Integer codigoGestaoEventoConteudoTurma, Integer disciplina, Integer turma, String ano, String semestre, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT matricula.matricula, matricula.situacao, pessoa.codigo AS pessoa_codigo, ");
		sql.append("pessoa.nome AS pessoanome, turma.identificadorturma as identificadorturma, turma.codigo as codigoturma, disciplina.nome as nomedisciplina, ");
		sql.append("matriculaperiodo.situacaomatriculaperiodo, matriculaperiodoturmadisciplina.codigo as codigomatriculaturmadisciplina, matriculaperiodoturmadisciplina.ano as anomatriculaperiodoturmadisciplina, matriculaperiodoturmadisciplina.semestre as semestrematriculaperiodoturmadisciplina,");
		sql.append("matriculaperiodoturmadisciplina.disciplina as disciplinamatriculaperiodoturmadisciplina ");
		sql.append("FROM matricula ");
		sql.append("INNER JOIN matriculaperiodo ON Matricula.matricula = MatriculaPeriodo.matricula ");
		sql.append("INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno ");
		sql.append("LEFT JOIN cidade ON cidade.codigo = pessoa.cidade ");
		sql.append("INNER JOIN matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = MatriculaPeriodo.codigo ");
		sql.append("INNER JOIN turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		sql.append("INNER JOIN disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina ");
		sql.append(" WHERE Matricula.matricula = MatriculaPeriodo.matricula ");
		sql.append(" and Pessoa.codigo = Matricula.aluno ");
		sql.append(" AND matriculaperiodoturmadisciplina.disciplina = ").append(disciplina);
		sql.append(" AND matriculaperiodoturmadisciplina.turma = ").append(turma);
		sql.append(" AND ((matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE')");
		sql.append("  or matriculaperiodoturmadisciplina.modalidadedisciplina != 'ON_LINE')");
		sql.append(" AND MatriculaPeriodo.ano = '").append(ano).append("'");
		sql.append(" AND MatriculaPeriodo.semestre = '").append(semestre).append("'");
		sql.append(" AND NOT EXISTS (");
		sql.append("	select 1 from gestaoeventoconteudoturmaavaliacaopbl ");
		sql.append("	inner join gestaoeventoconteudoturma on gestaoeventoconteudoturma.codigo = gestaoeventoconteudoturmaavaliacaopbl.gestaoeventoconteudoturma");
		sql.append("	where gestaoeventoconteudoturma.codigo = ").append(codigoGestaoEventoConteudoTurma);
		sql.append("	and gestaoeventoconteudoturmaavaliacaopbl.avaliado = pessoa.codigo");
		sql.append(" )");
		sql.append(" ORDER BY Pessoa.nome, turma.identificadorturma");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> listaAvaliado = new ArrayList<GestaoEventoConteudoTurmaAvaliacaoPBLVO>();
		GestaoEventoConteudoTurmaAvaliacaoPBLVO obj = null;
		while (tabelaResultado.next()) {
			obj = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
			obj.getAvaliador().setCodigo(tabelaResultado.getInt("pessoa_codigo"));
			obj.getAvaliador().setNome(tabelaResultado.getString("pessoanome"));
			obj.getAvaliado().setCodigo(tabelaResultado.getInt("pessoa_codigo"));
			obj.getAvaliado().setNome(tabelaResultado.getString("pessoanome"));
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO().setCodigo(tabelaResultado.getInt("codigomatriculaturmadisciplina"));
			listaAvaliado.add(obj);
		}
		return listaAvaliado;
	}

	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> consultarPorCodigoGestaoEventoConteudoTurmaVOResultadoFinalGeral(Integer codigoGestaoEventoConteudoTurmaVO, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" SELECT gestaoeventoconteudoturmaavaliacaopbl.* FROM gestaoeventoconteudoturmaavaliacaopbl");
		sqlStr.append(" INNER JOIN pessoa as avaliador on avaliador.codigo = gestaoeventoconteudoturmaavaliacaopbl.avaliador");
		sqlStr.append(" INNER JOIN pessoa as avaliado on avaliado.codigo = gestaoeventoconteudoturmaavaliacaopbl.avaliado");
		sqlStr.append(" WHERE gestaoeventoconteudoturma = ").append(codigoGestaoEventoConteudoTurmaVO);
		sqlStr.append(" AND tipoavaliacao = '").append(TipoAvaliacaoPBLEnum.RESULTADO_FINAL_GERAL).append("'");
		sqlStr.append(" ORDER BY avaliado.nome, avaliador.nome");
		return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), nivelMontarDados, usuarioLogado));
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public GestaoEventoConteudoTurmaAvaliacaoPBLVO consultarPorCodigoGestaoEventoConteudoTurmaVOResultadoFinalRecursoEducacional(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer codigoAvaliado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = getSelectConsultaRapidaGestaoEventoConteudoTurmaAvaliacaoPBL();
		sqlStr.append("	WHERE gect.conteudounidadepaginarecursoeducacional = ").append(codigoConteudoUnidadePaginaRecursoEducacional);
		sqlStr.append(" AND gectap.avaliado = ").append(codigoAvaliado);
		sqlStr.append(" AND gectap.tipoavaliacao = '").append(TipoAvaliacaoPBLEnum.RESULTADO_FINAL.name()).append("'");
		sqlStr.append(" ORDER BY avaliado.nome, avaliador.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return (montarDadosRapido(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> consultarAlunosAvaliadorAlunoAvaliadoConteudoUnidadePaginaRecursoEducacional(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer avaliador, Integer turma, Integer disciplina, String ano, String semestre, Integer conteudo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("	SELECT gestaoeventoconteudoturmaavaliacaopbl.* FROM GestaoEventoConteudoTurmaAvaliacaoPBL");
		sqlStr.append("	INNER JOIN gestaoeventoconteudoturma on gestaoeventoconteudoturma.codigo = gestaoeventoconteudoturmaavaliacaopbl.gestaoeventoconteudoturma");
		sqlStr.append("	WHERE gestaoeventoconteudoturma.conteudounidadepaginarecursoeducacional = ").append(codigoConteudoUnidadePaginaRecursoEducacional);
		sqlStr.append("	AND gestaoeventoconteudoturmaavaliacaopbl.avaliador = ").append(avaliador);
		sqlStr.append(" AND gestaoeventoconteudoturmaavaliacaopbl.tipoavaliacao = 'ALUNO_AVALIA_ALUNO'");
		sqlStr.append(" AND gestaoeventoconteudoturmaavaliacaopbl.avaliado <> ").append(avaliador);
		sqlStr.append(" AND gestaoeventoconteudoturma.turma = ").append(turma);
		sqlStr.append(" AND gestaoeventoconteudoturma.disciplina = ").append(disciplina);
		sqlStr.append(" AND gestaoeventoconteudoturma.ano = '").append(ano).append("'");
		sqlStr.append(" AND gestaoeventoconteudoturma.semestre = '").append(semestre).append("'");
		sqlStr.append(" AND gestaoeventoconteudoturma.conteudo = ").append(conteudo);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> gestaoEventoConteudoTurmaAvaliacaoPBLVOs = new ArrayList<GestaoEventoConteudoTurmaAvaliacaoPBLVO>();
		while (tabelaResultado.next()) {
			gestaoEventoConteudoTurmaAvaliacaoPBLVOs.add(montarDados(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return gestaoEventoConteudoTurmaAvaliacaoPBLVOs;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private StringBuilder getSelectConsultaRapidaGestaoEventoConteudoTurmaAvaliacaoPBL() {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select gectap.codigo as \"gectap.codigo\", gectap.tipoavaliacao as \"gectap.tipoavaliacao\", gectap.nota as \"gectap.nota\", ");
		sb.append(" gectap.notaconceitoavaliacaopbl as \"gectap.notaconceitoavaliacaopbl\", gectap.situacao as \"gectap.situacao\", gectap.matricula as \"gectap.matricula\", ");
		sb.append(" gectap.notalancada as \"gectap.notalancada\", gectap.matriculaperiodoturmadisciplinaavaliado as \"gectap.matriculaperiodoturmadisciplinaavaliado\",  ");
		sb.append(" gectap.gestaoeventoconteudoturma as \"gectap.gestaoeventoconteudoturma\", ");
		sb.append(" avaliador.codigo as \"avaliador.codigo\", avaliador.nome as \"avaliador.nome\", ");
		sb.append(" avaliado.codigo as \"avaliado.codigo\", avaliado.nome as \"avaliado.nome\", ");
		sb.append(" nca.codigo as \"nca.codigo\", nca.conceito as \"nca.conceito\", ");
		sb.append(" nca.notacorrespondente as \"nca.notacorrespondente\", nca.tipoavaliacao as \"nca.tipoavaliacao\", ");
		sb.append(" nca.conteudounidadepaginarecursoeducacional as \"nca.conteudounidadepaginarecursoeducacional\" ");
		sb.append(" from gestaoeventoconteudoturmaavaliacaopbl gectap  ");
		sb.append(" INNER JOIN gestaoeventoconteudoturma as gect on gectap.gestaoeventoconteudoturma = gect.codigo ");
		sb.append(" left join pessoa as avaliador on avaliador.codigo = gectap.avaliador ");
		sb.append(" left join pessoa as avaliado on avaliado.codigo = gectap.avaliado ");
		sb.append(" left join notaconceitoavaliacaopbl as nca on nca.codigo = gectap.notaconceitoavaliacaopbl ");

		return sb;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> consultarGestaoEventoConteudoTurmaAvaliacaoComDadosAvaliador(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer avaliador, Integer turma, Integer disciplina, String ano, String semestre, Integer conteudo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = getSelectConsultaRapidaGestaoEventoConteudoTurmaAvaliacaoPBL();
		sqlStr.append("	WHERE gect.conteudounidadepaginarecursoeducacional = ").append(codigoConteudoUnidadePaginaRecursoEducacional);
		sqlStr.append(" AND gect.turma = ").append(turma);
		sqlStr.append(" AND gect.disciplina = ").append(disciplina);
		sqlStr.append(" AND gect.ano = '").append(ano).append("'");
		sqlStr.append(" AND gect.semestre = '").append(semestre).append("'");
		sqlStr.append(" AND gect.conteudo = ").append(conteudo);
		sqlStr.append("	AND gectap.avaliador = ").append(avaliador);
		sqlStr.append(" AND gectap.avaliado <> ").append(avaliador);
		sqlStr.append(" AND gectap.tipoavaliacao = '").append(TipoAvaliacaoPBLEnum.ALUNO_AVALIA_ALUNO.name()).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRapido(tabelaResultado, nivelMontarDados, usuarioLogado);
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public GestaoEventoConteudoTurmaAvaliacaoPBLVO consultarGestaoEventoConteudoTurmaAvaliacaoComDadosAutoAvaliacao(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer avaliador, Integer turma, Integer disciplina, String ano, String semestre, Integer conteudo, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = getSelectConsultaRapidaGestaoEventoConteudoTurmaAvaliacaoPBL();
		sqlStr.append("	WHERE gect.conteudounidadepaginarecursoeducacional = ").append(codigoConteudoUnidadePaginaRecursoEducacional);
		sqlStr.append(" AND gect.turma = ").append(turma);
		sqlStr.append(" AND gect.disciplina = ").append(disciplina);
		sqlStr.append(" AND gect.ano = '").append(ano).append("'");
		sqlStr.append(" AND gect.semestre = '").append(semestre).append("'");
		sqlStr.append(" AND gect.conteudo = ").append(conteudo);
		sqlStr.append("	AND gectap.avaliador = ").append(avaliador);
		sqlStr.append(" AND gectap.avaliado = ").append(avaliador);
		sqlStr.append(" AND gectap.tipoavaliacao = '").append(TipoAvaliacaoPBLEnum.AUTO_AVALIACAO.name()).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (tabelaResultado.next()) {
			return montarDadosRapido(tabelaResultado, nivelMontarDados, usuarioLogado);
		}
		return new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private StringBuilder getSelectConsultaRapidaGestaoEventoConteudoAvaliacaoResultadoFinal() {
		StringBuilder sb = new StringBuilder("");
		sb.append(" SELECT ");
		sb.append(" cupre.codigo AS \"cupre.codigo\", cupre.descricao AS \"cupre.descricao\",  ta.nome as \"temaassunto.nome\", ");
		sb.append(" conteudo.codigo as \"conteudo.codigo\", conteudo.descricao as \"conteudo.descricao\", conteudo.situacaoconteudo as \"conteudo.situacaoconteudo\", ");
		sb.append(" turma.identificadorTurma as \"turma.identificadorTurma\", disciplina.nome as \"disciplina.nome\", ");
		sb.append(" gectap.codigo as \"gectap.codigo\", gectap.tipoavaliacao as \"gectap.tipoavaliacao\", gectap.nota as \"gectap.nota\", ");
		sb.append(" gectap.situacao as \"gectap.situacao\", gectap.matricula as \"gectap.matricula\", ");
		sb.append(" gectap.notalancada as \"gectap.notalancada\", gectap.matriculaperiodoturmadisciplinaavaliado as \"gectap.matriculaperiodoturmadisciplinaavaliado\",  ");
		sb.append(" gectap.gestaoeventoconteudoturma as \"gectap.gestaoeventoconteudoturma\", ");
		sb.append(" avaliador.codigo as \"avaliador.codigo\", avaliador.nome as \"avaliador.nome\", ");
		sb.append(" avaliado.codigo as \"avaliado.codigo\", avaliado.nome as \"avaliado.nome\"  ");
		return sb;
	}
	
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private SqlRowSet consultarGestaoEventoConteudoComAvaliacaoResultadoFinalGeral(Integer conteudo, Integer codigoTurma, Integer codigoDisciplina, String ano, String semestre, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = getSelectConsultaRapidaGestaoEventoConteudoAvaliacaoResultadoFinal();
		sb.append(" FROM gestaoeventoconteudoturma gect   ");
		sb.append(" INNER JOIN conteudo ON conteudo.codigo = gect.conteudo ");		
		sb.append(" INNER join turma on turma.codigo = gect.turma ");
		sb.append(" INNER join disciplina on disciplina.codigo = gect.disciplina ");
		sb.append(" inner join gestaoeventoconteudoturmaavaliacaopbl as gectap on gectap.gestaoeventoconteudoturma = gect.codigo and gectap.tipoavaliacao = '").append(TipoAvaliacaoPBLEnum.RESULTADO_FINAL.name()).append("' ");
		sb.append(" inner join pessoa as avaliador on avaliador.codigo = gectap.avaliador ");
		sb.append(" inner join pessoa as avaliado on avaliado.codigo = gectap.avaliado ");
		sb.append(" inner join conteudounidadepaginarecursoeducacional cupre on cupre.codigo = gect.conteudounidadepaginarecursoeducacional  ");
		sb.append(" inner join conteudounidadepagina  cup on cupre.conteudounidadepagina = cup.codigo ");
		sb.append(" inner join unidadeconteudo uc on uc.codigo = cup.unidadeconteudo ");
		sb.append(" left join temaassunto ta on ta.codigo = uc.temaassunto ");
		sb.append(" where gect.conteudo = ").append(conteudo);
		sb.append(" AND gect.turma = ").append(codigoTurma);
		sb.append(" AND gect.disciplina = ").append(codigoDisciplina);
		sb.append(" AND gect.ano = '").append(ano).append("'");
		sb.append(" AND gect.semestre = '").append(semestre).append("'");
		sb.append(" AND cupre.tipoRecursoEducacional = '").append(TipoRecursoEducacionalEnum.AVALIACAO_PBL.name()).append("'");
		sb.append("  order by cupre.codigo, avaliado.nome  ");
		return getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Boolean consultarSeExisteAvaliacaoPblNaoRealizadaParaAvaliador(Integer codigoConteudoUnidadePaginaRecursoEducacional, Integer avaliador, Integer turma, Integer disciplina, String ano, String semestre, Integer conteudo, TipoAvaliacaoPBLEnum tipoAvaliacao, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select  ");
		sb.append(" (case when gectap.situacao <> 'REALIZADO' then true else false end) validacao  ");
		sb.append(" from gestaoeventoconteudoturmaavaliacaopbl gectap  ");
		sb.append(" INNER JOIN gestaoeventoconteudoturma as gect on gectap.gestaoeventoconteudoturma = gect.codigo   ");
		sb.append(" INNER JOIN conteudounidadepaginarecursoeducacional as cupre on cupre.codigo = gect.conteudounidadepaginarecursoeducacional  ");
		sb.append("	WHERE cupre.codigo = ").append(codigoConteudoUnidadePaginaRecursoEducacional);
		sb.append(" AND gect.turma = ").append(turma);
		sb.append(" AND gect.disciplina = ").append(disciplina);
		sb.append(" AND gect.ano = '").append(ano).append("'");
		sb.append(" AND gect.semestre = '").append(semestre).append("'");
		sb.append(" AND gect.conteudo = ").append(conteudo);
		sb.append("	AND gectap.avaliador = ").append(avaliador);
		if(tipoAvaliacao.isAutoAvaliacao()){
			sb.append(" AND gectap.avaliado = ").append(avaliador);
		}else if(tipoAvaliacao.isAvaliacaoAuno()){
			sb.append(" AND gectap.avaliado <> ").append(avaliador);
		}
		sb.append(" AND gectap.tipoavaliacao = '").append(tipoAvaliacao.name()).append("'");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		Boolean existeAvaliacao = null;
		if(tabelaResultado.next()){
			existeAvaliacao = tabelaResultado.getBoolean("validacao");	
		}
		return  existeAvaliacao == null || existeAvaliacao ? true:false;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void realizarLancamentoGestaoEventoConteudoTurmaAvaliacaNota(ConteudoUnidadePaginaRecursoEducacionalVO obj, GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao, TipoAvaliacaoPBLEnum tipoAvaliacaoPBLEnum, UsuarioVO usuario) throws Exception {
		if (tipoAvaliacaoPBLEnum.isAutoAvaliacao()) {
			getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().validarNotaMinimaMaximaConceitoAutoAvaliacao(avaliacao, obj);
		}
		if (tipoAvaliacaoPBLEnum.isAvaliacaoAuno()) {
			getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().validarNotaMinimaMaximaConceitoAlunoAvaliaAluno(avaliacao, obj);
		}
		if (tipoAvaliacaoPBLEnum.isProfessorAvaliacaoAuno()) {
			getFacadeFactory().getNotaConceitoAvaliacaoPBLFacade().validarNotaMinimaMaximaConceitoProfAvaliaAluno(avaliacao, obj);
		}	
		obterNotaAvaliacaoDeAcordoComConfiguracaoConteudoUnidadePagina(obj, avaliacao);
		atualizarNotaGestaoEventoConteudoTurmaAvaliacao(avaliacao, usuario);

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public Double obterNotaAvaliacaoDeAcordoComConfiguracaoConteudoUnidadePagina(ConteudoUnidadePaginaRecursoEducacionalVO obj, GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao) throws Exception {
		Double nota = null;
		if (obj.getUtilizarNotaConceito() && Uteis.isAtributoPreenchido(avaliacao.getNotaConceitoAvaliacaoPBLVO().getCodigo())) {
			for (NotaConceitoAvaliacaoPBLVO notaConceitoAvaliacaoPBLVO : obj.getNotaConceitoAvaliacaoPBLVOs()) {
				if (notaConceitoAvaliacaoPBLVO.getCodigo().equals(avaliacao.getNotaConceitoAvaliacaoPBLVO().getCodigo())) {
					nota = notaConceitoAvaliacaoPBLVO.getNotaCorrespondente();
					break;
				}
			}
			avaliacao.setSituacao(SituacaoPBLEnum.REALIZADO);
			avaliacao.setNotaLancada(true);
		} else if (!obj.getUtilizarNotaConceito() && avaliacao.getNota() != null) {
			nota = avaliacao.getNota();
			avaliacao.setSituacao(SituacaoPBLEnum.REALIZADO);
			avaliacao.setNotaLancada(true);
		} else {
			avaliacao.setSituacao(SituacaoPBLEnum.PENDENTE);
			avaliacao.setNotaLancada(false);
		}
		return nota;
	}
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.REQUIRED)
	public void persistirGestaoEventoConteudoTurmaAvaliacaNotaHistorico(ConteudoVO conteudo, UsuarioVO usuarioLogado) throws Exception {
		if (!Uteis.isAtributoPreenchido(conteudo.getGestaoEventoConteudoTurmaVO().getTipoVariavelNota())) {
			throw new Exception(UteisJSF.internacionalizar("msg_GestaoEventoConteudo_notaLancar"));
		}
		getFacadeFactory().getGestaoEventoConteudoTurmaFacade().atualizarFormulaGestaoEventoConteudoTurma(conteudo.getGestaoEventoConteudoTurmaVO(), usuarioLogado);
		for (GestaoEventoConteudoTurmaAvaliacaoPBLVO obj : conteudo.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
			getFacadeFactory().getHistoricoFacade().alterarNotasHistoricoGestaoEventoConteudoTurmaResultadoFinal(obj.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO().getCodigo(), conteudo.getGestaoEventoConteudoTurmaVO().getTipoVariavelNota(), obj.getNota(), usuarioLogado);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirGestaoEventoConteudoTurmaAvaliacaoPBLVOS(ConteudoUnidadePaginaRecursoEducacionalVO obj, Boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliado : obj.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
			//obterNotaAvaliacaoDeAcordoComConfiguracaoConteudoUnidadePagina(obj, avaliado);
			persistir(avaliado, verificarAcesso, usuarioVO);
			persistir(avaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO(), verificarAcesso, usuarioVO);
			if (obj.getProfessorAvaliaAluno()) {
				//obterNotaAvaliacaoDeAcordoComConfiguracaoConteudoUnidadePagina(obj, avaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO());
				persistir(avaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO(), verificarAcesso, usuarioVO);
			}
			if (obj.getAlunoAvaliaAluno()) {
				for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliadores : avaliado.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
					//obterNotaAvaliacaoDeAcordoComConfiguracaoConteudoUnidadePagina(obj, avaliadores);
					persistir(avaliadores, verificarAcesso, usuarioVO);
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistirGestaoEventoConteudoTurmaAvaliacaoVisaoAluno(ConteudoUnidadePaginaRecursoEducacionalVO obj, List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> listaAvalidos, GestaoEventoConteudoTurmaAvaliacaoPBLVO autoAvaliacao, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		if (obj.getAutoAvaliacao()) {
			verificarSeTodasNotasForamLancadasAvaliacaoPBLVisaoAluno(obj, autoAvaliacao, usuarioVO);
			obterNotaAvaliacaoDeAcordoComConfiguracaoConteudoUnidadePagina(obj, autoAvaliacao);
			persistir(autoAvaliacao, verificarAcesso, usuarioVO);
			obj.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().setExistePendenciaAutoAvaliacao(false);
		}
		if (obj.getAlunoAvaliaAluno()) {
			for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliado : listaAvalidos) {
				verificarSeTodasNotasForamLancadasAvaliacaoPBLVisaoAluno(obj, avaliado, usuarioVO);
				obterNotaAvaliacaoDeAcordoComConfiguracaoConteudoUnidadePagina(obj, avaliado);
				persistir(avaliado, verificarAcesso, usuarioVO);
			}
			obj.getGestaoEventoConteudoTurmaAvaliacaoPBLVO().setExistePendenciaAlunoAvaliaAluno(false);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void verificarSeTodasNotasForamLancadasAvaliacaoPBLVisaoAluno(ConteudoUnidadePaginaRecursoEducacionalVO obj, GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao, UsuarioVO usuario) throws Exception {
		if (!obj.getPermiteAlunoAvancarConteudoSemLancarNota()) {
			if ((obj.getUtilizarNotaConceito() && avaliacao.getNotaConceitoAvaliacaoPBLVO().getCodigo().equals(0)) || (!obj.getUtilizarNotaConceito() && avaliacao.getNota() == null)) {
				if (avaliacao.getTipoAvaliacao().isAutoAvaliacao()) {
					throw new Exception(UteisJSF.internacionalizar("msg_Conteudo_existeNotasASeremLancadasNaoPermiteAvancarConteudoAvancoProximaPaginaAutoAvaliacao").replace("{0}", obj.getDescricao()));
				}
				if (avaliacao.getTipoAvaliacao().isAvaliacaoAuno()) {
					throw new Exception(UteisJSF.internacionalizar("msg_Conteudo_existeNotasASeremLancadasNaoPermiteAvancarConteudoAvancoProximaPaginaVisaoAluno").replace("{0}", avaliacao.getAvaliado().getNome()).replace("{1}", obj.getDescricao()));
				}
			}
		}
		if (avaliacao.getTipoAvaliacao().isAutoAvaliacao() && avaliacao.getNota() != null && (avaliacao.getNota() < obj.getFaixaMinimaNotaAutoAvaliacao() || avaliacao.getNota() > obj.getFaixaMaximaNotaAutoAvaliacao())) {
			throw new Exception(UteisJSF.internacionalizar("msg_Conteudo_notaLancadaForaFaixaNotaPBLAutoAvaliacao").replace("{0}", obj.getDescricao()).replace("{1}", Uteis.formatarDecimalDuasCasas(obj.getFaixaMinimaNotaAlunoAvaliaAluno())).replace("{2}", Uteis.formatarDecimalDuasCasas(obj.getFaixaMaximaNotaAlunoAvaliaAluno())));
		}
		if (avaliacao.getTipoAvaliacao().isAvaliacaoAuno() && avaliacao.getNota() != null && (avaliacao.getNota() < obj.getFaixaMinimaNotaAlunoAvaliaAluno() || avaliacao.getNota() > obj.getFaixaMaximaNotaAlunoAvaliaAluno())) {
			throw new Exception(UteisJSF.internacionalizar("msg_Conteudo_notaLancadaForaFaixaNotaPBLAlunoAvaliaAluno").replace("{0}", avaliacao.getAvaliado().getNome()).replace("{1}", obj.getDescricao()).replace("{2}", Uteis.formatarDecimalDuasCasas(obj.getFaixaMinimaNotaAlunoAvaliaAluno())).replace("{3}", Uteis.formatarDecimalDuasCasas(obj.getFaixaMaximaNotaAlunoAvaliaAluno())));
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void montarDadosGestaoEventoConteudoAvaliacaoResultadoFinal(ConteudoVO conteudo, UsuarioVO usuario) throws Exception {
		SqlRowSet rs = consultarGestaoEventoConteudoComAvaliacaoResultadoFinalGeral(conteudo.getCodigo(), conteudo.getGestaoEventoConteudoTurmaVO().getTurmaVO().getCodigo(), conteudo.getGestaoEventoConteudoTurmaVO().getDisciplinaVO().getCodigo(), conteudo.getGestaoEventoConteudoTurmaVO().getAno(), conteudo.getGestaoEventoConteudoTurmaVO().getSemestre(), usuario);
		for (UnidadeConteudoVO unidadeConteudoVO : conteudo.getUnidadeConteudoVOs()) {
			for (ConteudoUnidadePaginaVO conteudoUnidadePaginaVO : unidadeConteudoVO.getConteudoUnidadePaginaVOs()) {
				for (ConteudoUnidadePaginaRecursoEducacionalVO cupre : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalAnteriorVOs()) {
					if (cupre.getTipoRecursoEducacional().isTipoRecursoAvaliacaoPbl()) {
						preencherDadosGestaoEventoConteudoAvaliacaoResultadoFinal(conteudo, unidadeConteudoVO, cupre, rs);
					}
				}
				for (ConteudoUnidadePaginaRecursoEducacionalVO cupre : conteudoUnidadePaginaVO.getConteudoUnidadePaginaRecursoEducacionalPosteriorVOs()) {
					if (cupre.getTipoRecursoEducacional().isTipoRecursoAvaliacaoPbl()) {
						preencherDadosGestaoEventoConteudoAvaliacaoResultadoFinal(conteudo, unidadeConteudoVO, cupre, rs);
					}
				}
			}
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private void preencherDadosGestaoEventoConteudoAvaliacaoResultadoFinal(ConteudoVO conteudo, UnidadeConteudoVO unidadeConteudoVO, ConteudoUnidadePaginaRecursoEducacionalVO cupre, SqlRowSet rs) {
		forAvaliacao: for (GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao : conteudo.getGestaoEventoConteudoTurmaVO().getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
			GestaoEventoConteudoTurmaAvaliacaoPBLVO obj = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
			rs.beforeFirst();
			while (rs.next()) {
				if (avaliacao.getAvaliado().getCodigo().equals(rs.getInt("avaliado.codigo")) && cupre.getCodigo().equals(rs.getInt("cupre.codigo"))) {
					obj.setNovoObj(false);
					obj.setCodigo(rs.getInt("gectap.codigo"));
					obj.getGestaoEventoConteudoTurmaVO().setCodigo(rs.getInt("gectap.gestaoeventoconteudoturma"));
					obj.setTipoAvaliacao(TipoAvaliacaoPBLEnum.valueOf(rs.getString("gectap.tipoavaliacao")));
					obj.setNotaLancada(rs.getBoolean("gectap.notalancada"));
					if (rs.getObject("gectap.nota") != null) {
						obj.setNota(rs.getDouble("gectap.nota"));
					}
					obj.setSituacao(SituacaoPBLEnum.valueOf(rs.getString("gectap.situacao")));
					obj.getMatriculaVO().setMatricula(rs.getString("gectap.matricula"));
					obj.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO().setCodigo(rs.getInt("gectap.matriculaperiodoturmadisciplinaavaliado"));
					obj.getAvaliador().setCodigo(rs.getInt("avaliador.codigo"));
					obj.getAvaliador().setNome(rs.getString("avaliador.nome"));
					obj.getAvaliado().setCodigo(rs.getInt("avaliado.codigo"));
					obj.getAvaliado().setNome(rs.getString("avaliado.nome"));
					obj.setNomeRecursoEducacional(cupre.getDescricao());
					obj.setNomeAssuntoUnidadeConteudo(unidadeConteudoVO.getTemaAssuntoVO().getNome());
					avaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().add(obj);
					continue forAvaliacao;
				}
			}
			if (!Uteis.isAtributoPreenchido(obj)) {
				avaliacao.setNomeRecursoEducacional(cupre.getDescricao());
				avaliacao.setNomeAssuntoUnidadeConteudo(unidadeConteudoVO.getTemaAssuntoVO().getNome());
				avaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().add(obj);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void carregarEstruturaDeDadosDaAvaliacaoPBLVO(GestaoEventoConteudoTurmaVO obj, SqlRowSet rs, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		TipoAvaliacaoPBLEnum tipoAvaliacao = TipoAvaliacaoPBLEnum.valueOf(rs.getString("gectap.tipoavaliacao"));
		GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao = null;
		if (tipoAvaliacao.isAutoAvaliacao() || tipoAvaliacao.isResultadoFinalGeral()) {
			avaliacao = consultarSeJaExisteAvaliacaoCarregadoParaConteudo(obj, rs.getInt("gectap.codigo"), tipoAvaliacao);
			if (!Uteis.isAtributoPreenchido(avaliacao)) {
				avaliacao = montarDadosRapido(rs, nivelMontarDados, usuario);
			}
			adicionarAvaliacaoParaConteudo(obj, avaliacao);
		} else if (tipoAvaliacao.isAvaliacaoAuno()) {
			GestaoEventoConteudoTurmaAvaliacaoPBLVO autoAvaliacao = consultarSeJaExisteAvaliacaoAlunoCarregadoParaAutoAvaliacao(obj, rs.getInt("avaliado.codigo"), TipoAvaliacaoPBLEnum.AUTO_AVALIACAO);
			avaliacao = montarDadosRapido(rs, nivelMontarDados, usuario);
			adicionarAvaliacaoAunoParaAutoAvaliacao(autoAvaliacao, avaliacao);
			adicionarAvaliacaoParaConteudo(obj, autoAvaliacao);
		} else if (tipoAvaliacao.isProfessorAvaliacaoAuno()) {
			GestaoEventoConteudoTurmaAvaliacaoPBLVO autoAvaliacao = consultarSeJaExisteAvaliacaoAlunoCarregadoParaAutoAvaliacao(obj, rs.getInt("avaliado.codigo"), TipoAvaliacaoPBLEnum.AUTO_AVALIACAO);
			avaliacao = montarDadosRapido(rs, nivelMontarDados, usuario);
			autoAvaliacao.setGestaoEventoConteudoTurmaAvaliacaoPBLRProfessorAvaliaAlunoVO(avaliacao);
			adicionarAvaliacaoParaConteudo(obj, autoAvaliacao);
		} else if (tipoAvaliacao.isResultadoFinal()) {
			GestaoEventoConteudoTurmaAvaliacaoPBLVO autoAvaliacao = consultarSeJaExisteAvaliacaoAlunoCarregadoParaAutoAvaliacao(obj, rs.getInt("avaliado.codigo"), TipoAvaliacaoPBLEnum.AUTO_AVALIACAO);
			avaliacao = montarDadosRapido(rs, nivelMontarDados, usuario);
			autoAvaliacao.setGestaoEventoConteudoTurmaAvaliacaoPBLResultadoFinalVO(avaliacao);
			adicionarAvaliacaoParaConteudo(obj, autoAvaliacao);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private GestaoEventoConteudoTurmaAvaliacaoPBLVO consultarSeJaExisteAvaliacaoCarregadoParaConteudo(GestaoEventoConteudoTurmaVO obj, Integer codigo, TipoAvaliacaoPBLEnum tipoAvaliacao) {
		for (GestaoEventoConteudoTurmaAvaliacaoPBLVO objExistente : obj.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
			if (objExistente.getCodigo().equals(codigo) && objExistente.getTipoAvaliacao().equals(tipoAvaliacao)) {
				return objExistente;
			}
		}
		return new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void adicionarAvaliacaoParaConteudo(GestaoEventoConteudoTurmaVO obj, GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao) {
		int index = 0;
		for (GestaoEventoConteudoTurmaAvaliacaoPBLVO objExistente : obj.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
			if (objExistente.getCodigo().equals(avaliacao.getCodigo())) {
				obj.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().set(index, avaliacao);
				return;
			}
			index++;
		}
		obj.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().add(avaliacao);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private GestaoEventoConteudoTurmaAvaliacaoPBLVO consultarSeJaExisteAvaliacaoAlunoCarregadoParaAutoAvaliacao(GestaoEventoConteudoTurmaVO obj, Integer codigo, TipoAvaliacaoPBLEnum tipoAvaliacao) {
		for (GestaoEventoConteudoTurmaAvaliacaoPBLVO objExistente : obj.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
			if (objExistente.getAvaliado().getCodigo().equals(codigo) && objExistente.getTipoAvaliacao().equals(tipoAvaliacao)) {
				return objExistente;
			}
		}
		return new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void adicionarAvaliacaoAunoParaAutoAvaliacao(GestaoEventoConteudoTurmaAvaliacaoPBLVO autoAvaliacao, GestaoEventoConteudoTurmaAvaliacaoPBLVO avaliacao) {
		int index = 0;
		for (GestaoEventoConteudoTurmaAvaliacaoPBLVO objExistente : autoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs()) {
			if (objExistente.getCodigo().equals(avaliacao.getCodigo())) {
				autoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().set(index, avaliacao);
				return;
			}
			index++;
		}
		autoAvaliacao.getGestaoEventoConteudoTurmaAvaliacaoPBLVOs().add(avaliacao);
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> montarDadosConsultaRapido(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		List<GestaoEventoConteudoTurmaAvaliacaoPBLVO> vetResultado = new ArrayList<GestaoEventoConteudoTurmaAvaliacaoPBLVO>();
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosRapido(tabelaResultado, nivelMontarDados, usuarioLogado));
		}
		return vetResultado;
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public GestaoEventoConteudoTurmaAvaliacaoPBLVO montarDadosRapido(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
		GestaoEventoConteudoTurmaAvaliacaoPBLVO obj = new GestaoEventoConteudoTurmaAvaliacaoPBLVO();
		obj.setNovoObj(false);
		obj.setCodigo(rs.getInt("gectap.codigo"));
		obj.getGestaoEventoConteudoTurmaVO().setCodigo(rs.getInt("gectap.gestaoeventoconteudoturma"));
		obj.setTipoAvaliacao(TipoAvaliacaoPBLEnum.valueOf(rs.getString("gectap.tipoavaliacao")));
		obj.setNotaLancada(rs.getBoolean("gectap.notalancada"));
		if (rs.getObject("gectap.nota") != null) {
			obj.setNota(rs.getDouble("gectap.nota"));
		}
		obj.getNotaConceitoAvaliacaoPBLVO().setCodigo(rs.getInt("gectap.notaconceitoavaliacaopbl"));
		obj.setSituacao(SituacaoPBLEnum.valueOf(rs.getString("gectap.situacao")));
		obj.getMatriculaVO().setMatricula(rs.getString("gectap.matricula"));
		obj.getMatriculaPeriodoTurmaDisciplinaAvaliadoVO().setCodigo(rs.getInt("gectap.matriculaperiodoturmadisciplinaavaliado"));
		obj.getAvaliador().setCodigo(rs.getInt("avaliador.codigo"));
		obj.getAvaliador().setNome(rs.getString("avaliador.nome"));
		obj.getAvaliado().setCodigo(rs.getInt("avaliado.codigo"));
		obj.getAvaliado().setNome(rs.getString("avaliado.nome"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		if (Uteis.isAtributoPreenchido(rs.getInt("nca.codigo"))) {
			obj.getNotaConceitoAvaliacaoPBLVO().setCodigo(rs.getInt("nca.codigo"));
			obj.getNotaConceitoAvaliacaoPBLVO().getConteudoUnidadePaginaRecursoEducacionalVO().setCodigo(rs.getInt("nca.conteudounidadepaginarecursoeducacional"));
			obj.getNotaConceitoAvaliacaoPBLVO().setConceito(rs.getString("nca.conceito"));
			obj.getNotaConceitoAvaliacaoPBLVO().setNotaCorrespondente(rs.getDouble("nca.notacorrespondente"));
			obj.getNotaConceitoAvaliacaoPBLVO().setTipoAvaliacao(TipoAvaliacaoPBLEnum.valueOf(rs.getString("nca.tipoavaliacao")));

		}
		return obj;
	}

	
}