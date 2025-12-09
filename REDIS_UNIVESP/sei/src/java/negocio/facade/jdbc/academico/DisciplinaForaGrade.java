package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.DisciplinaAproveitadaAlteradaMatriculaVO;
import negocio.comuns.academico.DisciplinaForaGradeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.DisciplinaForaGradeInterfaceFacade;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class DisciplinaForaGrade extends ControleAcesso implements DisciplinaForaGradeInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public DisciplinaForaGrade() {

	}

	public void validarDados(DisciplinaForaGradeVO obj, String periodiocidadeCurso) throws Exception {
		if (obj.getDisciplina().equals("")) {
			throw new Exception("O campo DISCIPLINA deve ser informado.");
		}
		if (obj.getPeriodoLetivo().getCodigo() == 0) {
			throw new Exception("O campo PERIODO LETIVO deve ser informado.");
		}

		if ((periodiocidadeCurso.equals("AN") || periodiocidadeCurso.equals("SE"))) {
			if (obj.getAno().trim().equals("")) {
				throw new Exception("O campo ANO deve ser informado.");
			}
			if (obj.getAno().trim().length() != 4) {
				throw new Exception("O campo ANO deve possuir 4 dígitos.");
			}
		}
		if ((!periodiocidadeCurso.equals("AN") && !periodiocidadeCurso.equals("SE"))) {
			obj.setAno("");
		}

		if ((periodiocidadeCurso.equals("SE")) && obj.getSemestre().trim().equals("")) {
			throw new Exception("O campo SEMESTRE deve ser informado.");
		}
		if ((!periodiocidadeCurso.equals("SE"))) {
			obj.setSemestre("");
		}

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final DisciplinaForaGradeVO obj, String periodicidadeCurso, UsuarioVO usuario) throws Exception {
		validarDados(obj, periodicidadeCurso);
		final String sql = "INSERT INTO DisciplinaForaGrade( disciplina, nota, frequencia, inclusaoDisciplinaForaGrade, situacao, ano, semestre, cargaHoraria, instituicaoEnsino, cidade, periodoLetivo, usarNotaConceito, notaConceito, cargaHorariaCursada, disciplinaCadastrada, numeroCredito ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setString(1, obj.getDisciplina());
				sqlInserir.setDouble(2, obj.getNota());
				sqlInserir.setDouble(3, obj.getFrequencia());
				sqlInserir.setInt(4, obj.getInclusaoDisciplinaForaGradeVO().getCodigo().intValue());
				sqlInserir.setString(5, obj.getSituacao());
				sqlInserir.setString(6, obj.getAno());
				sqlInserir.setString(7, obj.getSemestre());
				sqlInserir.setInt(8, obj.getCargaHoraria());
				sqlInserir.setString(9, obj.getInstituicaoEnsino());
				if (obj.getCidade().getCodigo() > 0) {
					sqlInserir.setInt(10, obj.getCidade().getCodigo());
				} else {
					sqlInserir.setNull(10, 0);
				}
				sqlInserir.setInt(11, obj.getPeriodoLetivo().getCodigo());
				sqlInserir.setBoolean(12, obj.getUsarNotaConceito());
				sqlInserir.setString(13, obj.getNotaConceito());
				sqlInserir.setInt(14, obj.getCargaHorariaCursada());

				if (obj.getDisciplinaCadastrada().getCodigo() > 0) {
					sqlInserir.setInt(15, obj.getDisciplinaCadastrada().getCodigo());
				} else {
					sqlInserir.setNull(15, 0);
				}
				sqlInserir.setInt(16, obj.getNumeroCredito());
				return sqlInserir;
			}
		});

		obj.setNovoObj(Boolean.FALSE);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final DisciplinaForaGradeVO obj, String periodicidadeCurso, UsuarioVO usuario) throws Exception {
		validarDados(obj, periodicidadeCurso);
		final String sql = "UPDATE DisciplinaForaGrade set disciplina=?, nota=?, frequencia=?, inclusaoDisciplinaForaGrade=?, situacao=?, ano=?, semestre=?, cargaHoraria=?, instituicaoEnsino=?, cidade=?, periodoLetivo=?, usarNotaConceito=?, notaConceito=?, cargaHorariaCursada = ?, disciplinaCadastrada=?, numeroCredito=?  WHERE (codigo=?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setString(1, obj.getDisciplina());
				sqlAlterar.setDouble(2, obj.getNota());
				sqlAlterar.setDouble(3, obj.getFrequencia());
				sqlAlterar.setInt(4, obj.getInclusaoDisciplinaForaGradeVO().getCodigo().intValue());
				sqlAlterar.setString(5, obj.getSituacao());
				sqlAlterar.setString(6, obj.getAno());
				sqlAlterar.setString(7, obj.getSemestre());
				sqlAlterar.setInt(8, obj.getCargaHoraria());
				sqlAlterar.setString(9, obj.getInstituicaoEnsino());
				if (obj.getCidade().getCodigo() > 0) {
					sqlAlterar.setInt(10, obj.getCidade().getCodigo());
				} else {
					sqlAlterar.setNull(10, 0);
				}
				sqlAlterar.setInt(11, obj.getPeriodoLetivo().getCodigo());
				sqlAlterar.setBoolean(12, obj.getUsarNotaConceito());
				sqlAlterar.setString(13, obj.getNotaConceito());
				sqlAlterar.setInt(14, obj.getCargaHorariaCursada());

				if (obj.getDisciplinaCadastrada().getCodigo() > 0) {
					sqlAlterar.setInt(15, obj.getDisciplinaCadastrada().getCodigo());
				} else {
					sqlAlterar.setNull(15, 0);
				}
				sqlAlterar.setInt(16, obj.getNumeroCredito());
				sqlAlterar.setInt(17, obj.getCodigo().intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(DisciplinaForaGradeVO obj, UsuarioVO usuario) throws Exception {
		DisciplinaEquivalente.excluir(getIdEntidade());
		this.carregarDados(obj, false, usuario);
		String sql = "DELETE FROM DisciplinaForaGrade WHERE (codigo=?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { obj.getCodigo() });
		this.excluirInclusaoDisciplinaForaGradeOrfao(obj, false, usuario);
	}

	public static List<DisciplinaForaGradeVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
		List<DisciplinaForaGradeVO> vetResultado = new ArrayList<DisciplinaForaGradeVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	/**
	 * Responsável por montar os dados resultantes de uma consulta ao banco de
	 * dados (<code>ResultSet</code>) em um objeto da classe
	 * <code>DisciplinaEquivalenteVO</code>.
	 *
	 * @return O objeto da classe <code>DisciplinaEquivalenteVO</code> com os
	 *         dados devidamente montados.
	 */
	public static DisciplinaForaGradeVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		DisciplinaForaGradeVO obj = new DisciplinaForaGradeVO();
		obj.setDisciplina(dadosSQL.getString("disciplina"));

		obj.getDisciplinaCadastrada().setCodigo(dadosSQL.getInt("disciplinaCadastrada"));
		if (obj.getDisciplinaCadastrada().getCodigo() > 0) {
			obj.setDisciplinaCadastrada(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplinaCadastrada().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}

		obj.setNota(dadosSQL.getDouble("nota"));
		obj.setFrequencia(dadosSQL.getDouble("frequencia"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.getInclusaoDisciplinaForaGradeVO().setCodigo(dadosSQL.getInt("inclusaoDisciplinaForaGrade"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setCargaHoraria(dadosSQL.getInt("cargaHoraria"));
		obj.setCargaHorariaCursada(dadosSQL.getInt("cargaHorariaCursada"));
		obj.setNotaConceito(dadosSQL.getString("notaConceito"));
		obj.setUsarNotaConceito(dadosSQL.getBoolean("usarNotaConceito"));
		obj.setInstituicaoEnsino(dadosSQL.getString("instituicaoEnsino"));
		obj.getPeriodoLetivo().setCodigo(dadosSQL.getInt("periodoLetivo"));
		if (obj.getPeriodoLetivo().getCodigo() > 0) {
			obj.setPeriodoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(obj.getPeriodoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		obj.getCidade().setCodigo(dadosSQL.getInt("cidade"));
		if (obj.getCidade().getCodigo() > 0) {
			obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
		}
		return obj;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirDisciplinaForaGrade(Integer inclusaoDisciplinaForaGrade, UsuarioVO usuario) throws Exception {
		String sql = "DELETE FROM DisciplinaForaGrade WHERE (inclusaoDisciplinaForaGrade = ?)" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { inclusaoDisciplinaForaGrade });
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDisciplinaForaGrade(Integer inclusaoDisciplinaForaGrade, String periodicidadeCurso, List<DisciplinaForaGradeVO> objetos, UsuarioVO usuario) throws Exception {
		excluirDisciplinaForaGrade(inclusaoDisciplinaForaGrade, usuario);
		incluirDisciplinaForaGrade(inclusaoDisciplinaForaGrade, periodicidadeCurso, objetos, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirDisciplinaForaGrade(Integer inclusaoDisciplinaForaGrade, String periodicidadeCurso, List<DisciplinaForaGradeVO> objetos, UsuarioVO usuario) throws Exception {
		Iterator<DisciplinaForaGradeVO> e = objetos.iterator();
		while (e.hasNext()) {
			DisciplinaForaGradeVO obj = (DisciplinaForaGradeVO) e.next();
			obj.getInclusaoDisciplinaForaGradeVO().setCodigo(inclusaoDisciplinaForaGrade);
			incluir(obj, periodicidadeCurso, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarDisciplinaForaGradeAlteracaoAproveitamentoDisciplina(final Integer codigo, final DisciplinaAproveitadaAlteradaMatriculaVO obj, UsuarioVO usuario) throws Exception {
		try {
			final String sql = "UPDATE disciplinaforagrade set situacao=?, nota=?, frequencia=?, cargaHoraria=?, ano=?, semestre=?, instituicaoEnsino=?, cidade=?, cargaHorariaCursada=? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					sqlAlterar.setString(++i, obj.getSituacao());
					sqlAlterar.setDouble(++i, obj.getMedia());
					sqlAlterar.setDouble(++i, obj.getFrequencia());
					sqlAlterar.setInt(++i, obj.getCargaHoraria());
					sqlAlterar.setString(++i, obj.getAno());
					sqlAlterar.setString(++i, obj.getSemestre());
					sqlAlterar.setString(++i, obj.getInstituicao());
					sqlAlterar.setInt(++i, obj.getCidadeVO().getCodigo().intValue());
					sqlAlterar.setInt(++i, obj.getCargaHorariaCursada());
					sqlAlterar.setInt(++i, obj.getCodigoDisciplinaForaGrade());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	public DisciplinaForaGradeVO consultarPorChavePrimaria(Integer disciplinaForaGrade, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT * FROM disciplinaForaGrade WHERE codigo = ").append(disciplinaForaGrade);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return montarDados(rs, usuarioVO);
		}
		return new DisciplinaForaGradeVO();
	}

	public boolean verificarExisteDisciplinaForaGradeVOsVinculadaInclusaoDisciplinaForaGrade(DisciplinaForaGradeVO disciplinaForaGradeVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select count(codigo) from disciplinaforagrade");
		sqlStr.append(" where inclusaoDisciplinaForaGrade = ").append(disciplinaForaGradeVO.getInclusaoDisciplinaForaGradeVO().getCodigo());
		sqlStr.append(" and codigo not in (").append(disciplinaForaGradeVO.getCodigo()).append(")");
		return getConexao().getJdbcTemplate().queryForInt(sqlStr.toString()) > 0;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirInclusaoDisciplinaForaGradeOrfao(DisciplinaForaGradeVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
		if (!this.verificarExisteDisciplinaForaGradeVOsVinculadaInclusaoDisciplinaForaGrade(obj, verificarAcesso, usuario)) {
			getFacadeFactory().getInclusaoDisciplinaForaGradeFacade().excluir(obj.getInclusaoDisciplinaForaGradeVO(), usuario);
		}
	}

	public DisciplinaForaGradeVO carregarDados(DisciplinaForaGradeVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT * FROM disciplinaForaGrade WHERE codigo = ").append(obj.getCodigo());
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		if (rs.next()) {
			return carregarDados(rs, obj, usuarioVO);
		}
		return new DisciplinaForaGradeVO();
	}

	public static DisciplinaForaGradeVO carregarDados(SqlRowSet dadosSQL, DisciplinaForaGradeVO obj, UsuarioVO usuario) throws Exception {
		obj.setDisciplina(dadosSQL.getString("disciplina"));
		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.getDisciplinaCadastrada().setCodigo(dadosSQL.getInt("disciplinaCadastrada"));
		if (obj.getDisciplinaCadastrada().getCodigo() > 0) {
			obj.setDisciplinaCadastrada(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(obj.getDisciplinaCadastrada().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
		}
		obj.setNota(dadosSQL.getDouble("nota"));
		obj.setFrequencia(dadosSQL.getDouble("frequencia"));
		obj.setAno(dadosSQL.getString("ano"));
		obj.setSemestre(dadosSQL.getString("semestre"));
		obj.getInclusaoDisciplinaForaGradeVO().setCodigo(dadosSQL.getInt("inclusaoDisciplinaForaGrade"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setCargaHoraria(dadosSQL.getInt("cargaHoraria"));
		obj.setCargaHorariaCursada(dadosSQL.getInt("cargaHorariaCursada"));
		obj.setNotaConceito(dadosSQL.getString("notaConceito"));
		obj.setUsarNotaConceito(dadosSQL.getBoolean("usarNotaConceito"));
		obj.setInstituicaoEnsino(dadosSQL.getString("instituicaoEnsino"));
		obj.getPeriodoLetivo().setCodigo(dadosSQL.getInt("periodoLetivo"));
		if (obj.getPeriodoLetivo().getCodigo() > 0) {
			obj.setPeriodoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(obj.getPeriodoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		obj.getCidade().setCodigo(dadosSQL.getInt("cidade"));
		if (obj.getCidade().getCodigo() > 0) {
			obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
		}
		return obj;
	}

}
