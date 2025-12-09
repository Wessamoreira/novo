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

import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.HistoricoGradeAnteriorAlteradaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.HistoricoGradeVO;
import negocio.comuns.secretaria.TransferenciaMatrizCurricularVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.HistoricoGradeAnteriorAlteradaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class HistoricoGradeAnteriorAlterada extends ControleAcesso implements HistoricoGradeAnteriorAlteradaInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public HistoricoGradeAnteriorAlterada() {
		super();
	}

	public void inicializarDados(HistoricoGradeAnteriorAlteradaVO obj, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuarioVO) {
		obj.setResponsavel(usuarioVO);
		obj.setMatriculaVO(matriculaVO);
		obj.setGradeCurricularVO(gradeCurricularVO);
	}

	public void persistir(List<HistoricoGradeAnteriorAlteradaVO> historicoGradeAnteriorAlteradaVOs, MatriculaVO matriculaVO, GradeCurricularVO gradeCurricularVO, UsuarioVO usuarioVO) throws Exception {
		for (HistoricoGradeAnteriorAlteradaVO obj : historicoGradeAnteriorAlteradaVOs) {
			inicializarDados(obj, matriculaVO, gradeCurricularVO, usuarioVO);
			validarDados(obj);
			incluir(obj, usuarioVO);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final HistoricoGradeAnteriorAlteradaVO obj, UsuarioVO usuario) throws Exception {
		final String sql = "INSERT INTO HistoricoGradeAnteriorAlterada( matricula, disciplina, gradeCurricular, mediaFinal, frequencia, ano, semestre, cidade, instituicao, dataAlteracao, responsavel, situacao, cargaHorariaCursada, matriculaPeriodo, historicoGrade, acao, isentarMediaFinal ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlInserir = arg0.prepareStatement(sql);
				sqlInserir.setString(1, obj.getMatriculaVO().getMatricula());
				sqlInserir.setInt(2, obj.getDisciplinaVO().getCodigo());
				sqlInserir.setInt(3, obj.getGradeCurricularVO().getCodigo());
				sqlInserir.setDouble(4, obj.getMediaFinal());
				sqlInserir.setDouble(5, obj.getFrequencia());
				sqlInserir.setString(6, obj.getAno());
				sqlInserir.setString(7, obj.getSemestre());
				sqlInserir.setInt(8, obj.getCidadeVO().getCodigo());
				sqlInserir.setString(9, obj.getInstituicao());
				sqlInserir.setTimestamp(10, Uteis.getDataJDBCTimestamp(obj.getDataAlteracao()));
				sqlInserir.setInt(11, obj.getResponsavel().getCodigo());
				sqlInserir.setString(12, obj.getSituacao());
				sqlInserir.setInt(13, obj.getCargaHorariaCursada());
				sqlInserir.setInt(14, obj.getMatriculaPeriodoVO().getCodigo());
				sqlInserir.setInt(15, obj.getHistoricoGradeVO().getCodigo());
				sqlInserir.setString(16, obj.getAcao());
				sqlInserir.setBoolean(17, obj.getIsentarMediaFinal());
				return sqlInserir;
			}
		});
		obj.setNovoObj(Boolean.FALSE);
		alterarHistoricoGrade(obj, usuario);
	}

	public HistoricoGradeVO inicializarDadosHistoricoGradeInclusao(HistoricoGradeAnteriorAlteradaVO historicoGradeAnteriorAlteradaVO, UsuarioVO usuarioVO) throws Exception {
		HistoricoGradeVO obj = new HistoricoGradeVO();
		obj.getDisciplinaVO().setCodigo(historicoGradeAnteriorAlteradaVO.getDisciplinaVO().getCodigo());
		obj.getMatriculaPeriodoApresentarHistoricoVO().setCodigo(historicoGradeAnteriorAlteradaVO.getMatriculaPeriodoVO().getCodigo());
		obj.getMatriculaPeriodoVO().setCodigo(historicoGradeAnteriorAlteradaVO.getMatriculaPeriodoVO().getCodigo());
		obj.setSituacao(historicoGradeAnteriorAlteradaVO.getSituacao());
		obj.setMediaFinal(historicoGradeAnteriorAlteradaVO.getMediaFinal());
		obj.setFrequencia(historicoGradeAnteriorAlteradaVO.getFrequencia());
		obj.setAnoHistorico(historicoGradeAnteriorAlteradaVO.getAno());
		obj.setSemestreHistorico(historicoGradeAnteriorAlteradaVO.getSemestre());
		obj.setCargaHorariaCursada(historicoGradeAnteriorAlteradaVO.getCargaHorariaCursada());
		obj.setInstituicao(historicoGradeAnteriorAlteradaVO.getInstituicao());
		obj.getCidade().setCodigo(historicoGradeAnteriorAlteradaVO.getCidadeVO().getCodigo());
		obj.setIsentarMediaFinal(historicoGradeAnteriorAlteradaVO.getIsentarMediaFinal());
//		TransferenciaMatrizCurricularVO transferenciaMatrizCurricularVO = getFacadeFactory().getTransferenciaMatrizCurricularFacade().consultarPorMatriculaCodigoGradeOrigem(historicoGradeAnteriorAlteradaVO.getMatriculaVO().getMatricula(), historicoGradeAnteriorAlteradaVO.getGradeCurricularVO().getCodigo(), usuarioVO);
//		if (Uteis.isAtributoPreenchido(transferenciaMatrizCurricularVO)) {
//			obj.setTransferenciaMatrizCurricularVO(transferenciaMatrizCurricularVO);
//		} else {
//			throw new Exception("Problema ao buscar o código da transferência de matriz curricular, contate o administrador.");
//		}
		return obj;
	}

	public HistoricoVO inicializarDadosHistoricoInclusao(HistoricoGradeAnteriorAlteradaVO historicoGradeAnteriorAlteradaVO, UsuarioVO usuarioVO) throws Exception {
		HistoricoVO obj = new HistoricoVO();
		obj.setDisciplina(historicoGradeAnteriorAlteradaVO.getDisciplinaVO());
		obj.setMatriculaPeriodo(historicoGradeAnteriorAlteradaVO.getMatriculaPeriodoVO());
		obj.setMatricula(historicoGradeAnteriorAlteradaVO.getMatriculaVO());
		obj.setSituacao(historicoGradeAnteriorAlteradaVO.getSituacao());
		obj.setMediaFinal(historicoGradeAnteriorAlteradaVO.getMediaFinal());
		obj.setFreguencia(historicoGradeAnteriorAlteradaVO.getFrequencia());
		obj.setAnoHistorico(historicoGradeAnteriorAlteradaVO.getAno());
		obj.setSemestreHistorico(historicoGradeAnteriorAlteradaVO.getSemestre());
		obj.setCargaHorariaCursada(historicoGradeAnteriorAlteradaVO.getCargaHorariaCursada());
		obj.setInstituicao(historicoGradeAnteriorAlteradaVO.getInstituicao());
		obj.setCidadeVO(historicoGradeAnteriorAlteradaVO.getCidadeVO());
		obj.setCidade(historicoGradeAnteriorAlteradaVO.getCidadeVO().getNome());
		obj.setIsentarMediaFinal(historicoGradeAnteriorAlteradaVO.getIsentarMediaFinal());
		obj.setHistoricoDisciplinaFazParteComposicao(false);
		obj.setHistoricoEquivalente(false);
		obj.setMatrizCurricular(historicoGradeAnteriorAlteradaVO.getGradeCurricularVO());
		// TransferenciaMatrizCurricularVO transferencia =
		// getFacadeFactory().getTransferenciaMatrizCurricularFacade().consultarPorMatriculaCodigoGradeOrigem(historicoGradeAnteriorAlteradaVO.getMatriculaVO().getMatricula(),
		// historicoGradeAnteriorAlteradaVO.getGradeCurricularVO().getCodigo(),
		// usuarioVO);
		// obj.setTransferenciaMatrizCurricularVO(transferencia);
		return obj;
	}

	public void alterarHistoricoGrade(HistoricoGradeAnteriorAlteradaVO obj, UsuarioVO usuario) throws Exception {
		if (obj.getAcao().equals("ALTERACAO")) {
			if (Uteis.isAtributoPreenchido(obj.getHistoricoVO())) {
				getFacadeFactory().getHistoricoFacade().alterarHistoricoPorHistoricoGradeAnteriorAlterada(obj, usuario);
			} else {
//				getFacadeFactory().getHistoricoGradeFacade().alterarHistoricoGradePorHistoricoGradeAnteriorAlterada(obj, usuario);
			}
		} else if (obj.getAcao().equals("EXCLUSAO")) {
			if (Uteis.isAtributoPreenchido(obj.getHistoricoVO())) {
				getFacadeFactory().getHistoricoFacade().excluir(obj.getHistoricoVO(), false, usuario);
			} else {
//				getFacadeFactory().getHistoricoGradeFacade().excluir(obj.getHistoricoGradeVO(), usuario);
			}
		} else if (obj.getAcao().equals("INCLUSAO")) {
			boolean existeHistoricoVinculadoMatriculaMatrizCurricular = getFacadeFactory().getHistoricoFacade().verificarExisteHistoricoVinculadoMatriculaMatrizCurricular(obj.getMatriculaVO().getMatricula(), obj.getGradeCurricularVO().getCodigo(), usuario);
			if (existeHistoricoVinculadoMatriculaMatrizCurricular) {
				HistoricoVO historicoIncluirVO = inicializarDadosHistoricoInclusao(obj, usuario);
				/**
				 * Regra responsável por definir a configuração acadêmica, caso
				 * seja a inclusão de um novo histórico.
				 */
				GradeDisciplinaVO gradeDisciplinaVO = getFacadeFactory().getGradeDisciplinaFacade().consultarPorGradeCurricularEDisciplina(obj.getGradeCurricularVO().getCodigo(), obj.getDisciplinaVO().getCodigo(), usuario, null);
				if (Uteis.isAtributoPreenchido(gradeDisciplinaVO.getConfiguracaoAcademico())) {
					historicoIncluirVO.setConfiguracaoAcademico(gradeDisciplinaVO.getConfiguracaoAcademico());
				} else {
					historicoIncluirVO.setConfiguracaoAcademico(obj.getMatriculaVO().getCurso().getConfiguracaoAcademico());
				}
				getFacadeFactory().getHistoricoFacade().incluir(historicoIncluirVO, usuario);
				obj.setHistoricoVO(historicoIncluirVO);
			} else {
				HistoricoGradeVO historicoGradeIncluirVO = inicializarDadosHistoricoGradeInclusao(obj, usuario);
//				getFacadeFactory().getHistoricoGradeFacade().incluir(historicoGradeIncluirVO, usuario);
				obj.setHistoricoGradeVO(historicoGradeIncluirVO);
			}
		}
	}

	public List<HistoricoGradeAnteriorAlteradaVO> consultarHistoricoGradePorMatriculaGradeCurricular(String matricula, Integer gradeCurricular, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select distinct historicograde.codigo as historicograde_codigo, 0 as historico_codigo, disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, ");
		sb.append("historicograde.situacao, mediafinal, frequencia, anohistorico, semestrehistorico, instituicao, isentarMediaFinal, cidade.codigo as cidade_codigo, cidade.nome as cidade_nome, ");
		sb.append("cargahorariacursada, historicograde.matriculaperiodoapresentarhistorico as matriculaperiodo, transferenciamatrizcurricular.codigo as transferenciamatrizcurricular_codigo ");
		sb.append("from historicograde ");
		sb.append("inner join disciplina on disciplina.codigo = historicograde.disciplina ");
		sb.append("inner join transferenciamatrizcurricular on transferenciamatrizcurricular.codigo = historicograde.transferenciamatrizcurricular ");
		sb.append("left join cidade on cidade.codigo = historicograde.cidade ");
		sb.append("where transferenciamatrizcurricular.matricula = '").append(matricula).append("'");
		sb.append(" and gradeorigem = ").append(gradeCurricular);
		sb.append(" union ");
		sb.append("select distinct 0 as historicograde_codigo, historico.codigo as historico_codigo, disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, ");
		sb.append("historico.situacao, historico.mediafinal, historico.freguencia as frequencia, anohistorico, semestrehistorico, instituicao, isentarmediafinal, cidade.codigo as cidade_codigo, cidade.nome as cidade_nome, ");
		sb.append("cargahorariacursada, historico.matriculaperiodo, 0 as transferenciamatrizcurricular_codigo ");
		sb.append("from historico ");
		sb.append("inner join disciplina on disciplina.codigo = historico.disciplina ");
		sb.append("left join cidade on cidade.codigo = historico.cidade ");
		sb.append("where historico.matricula = '").append(matricula).append("'");
		sb.append(" and historico.matrizcurricular = ").append(gradeCurricular);
		sb.append(" and historico.situacao not in ('CS', 'CO')");
		sb.append(" and (historico.historicodisciplinafazpartecomposicao is null or historico.historicodisciplinafazpartecomposicao = false)");
		sb.append(" and (historico.historicoequivalente is null or historico.historicoequivalente = false)");
		sb.append(" order by disciplina_nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<HistoricoGradeAnteriorAlteradaVO> historicoGradeAnteriorAlteradaVOs = new ArrayList<HistoricoGradeAnteriorAlteradaVO>(0);
		while (tabelaResultado.next()) {
			HistoricoGradeAnteriorAlteradaVO obj = new HistoricoGradeAnteriorAlteradaVO();
			montarDados(tabelaResultado, obj, usuarioVO);
			historicoGradeAnteriorAlteradaVOs.add(obj);
		}
		return historicoGradeAnteriorAlteradaVOs;
	}

	public void montarDados(SqlRowSet rs, HistoricoGradeAnteriorAlteradaVO obj, UsuarioVO usuarioVO) {
		obj.getHistoricoGradeVO().setCodigo(rs.getInt("historicoGrade_codigo"));
		obj.getHistoricoVO().setCodigo(rs.getInt("historico_codigo"));
		obj.getDisciplinaVO().setCodigo(rs.getInt("disciplina_codigo"));
		obj.getDisciplinaVO().setNome(rs.getString("disciplina_nome"));
		obj.setSituacao(rs.getString("situacao"));
		obj.setMediaFinal(rs.getDouble("mediaFinal"));
		obj.setFrequencia(rs.getDouble("frequencia"));
		obj.setAno(rs.getString("anoHistorico"));
		obj.setSemestre(rs.getString("semestreHistorico"));
		obj.setInstituicao(rs.getString("instituicao"));
		obj.getCidadeVO().setCodigo(rs.getInt("cidade_codigo"));
		obj.getCidadeVO().setNome(rs.getString("cidade_nome"));
		obj.setCargaHorariaCursada(rs.getInt("cargaHorariaCursada"));
		obj.getMatriculaPeriodoVO().setCodigo(rs.getInt("matriculaPeriodo"));
		obj.getTransferenciaMatrizCurricularVO().setCodigo(rs.getInt("transferenciamatrizcurricular_codigo"));
		obj.setIsentarMediaFinal(rs.getBoolean("isentarMediaFinal"));
	}

	public void adicionarHistoricoGradeAnterior(List<HistoricoGradeAnteriorAlteradaVO> listaHistoricoGradeAnteriorVOs, HistoricoGradeAnteriorAlteradaVO obj, UsuarioVO usuarioVO) throws Exception {
		validarDadosInclusaoHistoricoGrade(obj);
		int index = 0;
		Iterator<HistoricoGradeAnteriorAlteradaVO> i = listaHistoricoGradeAnteriorVOs.iterator();
		while (i.hasNext()) {
			HistoricoGradeAnteriorAlteradaVO objExistente = (HistoricoGradeAnteriorAlteradaVO) i.next();
			if (objExistente.getDisciplinaVO().getCodigo().equals(obj.getDisciplinaVO().getCodigo()) && objExistente.getMatriculaPeriodoVO().getCodigo().equals(obj.getMatriculaPeriodoVO().getCodigo())) {
				listaHistoricoGradeAnteriorVOs.set(index, obj);
				return;
			}
			index++;
		}
		obj.setAcao("INCLUSAO");
		listaHistoricoGradeAnteriorVOs.add(obj);
		Ordenacao.ordenarLista(listaHistoricoGradeAnteriorVOs, "nomeDisciplina");
	}

	public void removerHistoricoGradeAnterior(List<HistoricoGradeAnteriorAlteradaVO> listaHistoricoGradeAnteriorVOs, HistoricoGradeAnteriorAlteradaVO obj) throws Exception {
		int index = 0;
		Iterator<HistoricoGradeAnteriorAlteradaVO> i = listaHistoricoGradeAnteriorVOs.iterator();
		while (i.hasNext()) {
			HistoricoGradeAnteriorAlteradaVO objExistente = (HistoricoGradeAnteriorAlteradaVO) i.next();
			if (objExistente.getDisciplinaVO().getCodigo().equals(obj.getDisciplinaVO().getCodigo()) && objExistente.getMatriculaPeriodoVO().getCodigo().equals(obj.getMatriculaPeriodoVO().getCodigo())) {
				listaHistoricoGradeAnteriorVOs.remove(index);
				return;
			}
			index++;
		}
		Ordenacao.ordenarLista(listaHistoricoGradeAnteriorVOs, "nomeDisciplina");
	}

	public void validarDados(HistoricoGradeAnteriorAlteradaVO obj) throws Exception {
		if (obj.getMatriculaVO().getMatricula().equals("")) {
			throw new Exception("O campo Matrícula deve ser informado!");
		}
		if (obj.getGradeCurricularVO().getCodigo().equals(0)) {
			throw new Exception("O campo Grade Curricular deve ser informado!");
		}
		if (obj.getDisciplinaVO().getCodigo().equals(0)) {
			throw new Exception("O campo Disciplina deve ser informado!");
		}
		if (obj.getSituacao().equals("")) {
			throw new Exception("O campo Situação deve ser informado!");
		}
	}

	public void validarDadosInclusaoHistoricoGrade(HistoricoGradeAnteriorAlteradaVO obj) throws Exception {
		if (obj.getDisciplinaVO().getCodigo().equals(0)) {
			throw new Exception("O campo Disciplina deve ser informado!");
		}
		if (obj.getSituacao().equals("")) {
			throw new Exception("O campo Situação deve ser informado!");
		}
	}

}
