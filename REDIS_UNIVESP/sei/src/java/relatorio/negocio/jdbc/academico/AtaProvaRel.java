package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoRecuperacaoNotaEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import relatorio.negocio.comuns.academico.AtaProvaRelVO;
import relatorio.negocio.interfaces.academico.AtaProvaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class AtaProvaRel extends SuperRelatorio implements AtaProvaRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public AtaProvaRel() {
	}

	public void validarDados(Integer turma, Integer disciplina, boolean gerandoRelatorio, String ano, boolean permitirGerarAtaProvaRetroativo) throws Exception {
		if (gerandoRelatorio) {
			if (turma.intValue() == 0) {
				throw new Exception("O campo TURMA deve ser informado para geração do relatório.");
			}
			if (disciplina.intValue() == 0) {
				throw new Exception("O campo DISCIPLINA deve ser informado para geração do relatório.");
			}
			if(turma.intValue() == 0 && permitirGerarAtaProvaRetroativo && !Uteis.isAtributoPreenchido(ano)) {
				throw new Exception("O campo ANO deve ser informado para geração do relatório.");
			}
		} else {
			if (turma.intValue() == 0) {
				throw new Exception("O campo TURMA deve ser informado para consultar as disciplinas.");
			}
		}
	}

	public void validarDadosVisaoCoordenador(TurmaVO turma, Integer disciplina, String ano, String semestre, boolean permitirGerarAtaProvaRetroativo) throws Exception {
		if (turma.getCodigo().intValue() == 0) {
			throw new Exception("O campo TURMA deve ser informado para geração do relatório.");
		}
		if (disciplina.intValue() == 0) {
			throw new Exception("O campo DISCIPLINA deve ser informado para geração do relatório.");
		}
		if(turma.getCodigo().intValue() == 0 && permitirGerarAtaProvaRetroativo && !Uteis.isAtributoPreenchido(ano)) {
			throw new Exception("O campo ANO deve ser informado para geração do relatório.");
		} 
		if (turma.getSemestral() || turma.getAnual()) {
			if (ano == null || ano.equals("")) {
				throw new Exception("O campo ANO deve ser informado para geração do relatório.");
			}
			if (turma.getSemestral()) {
				if (semestre == null || semestre.equals("")) {
					throw new Exception("O campo SEMESTRE deve ser informado para geração do relatório.");
				}
			}
		}
		

		
	}

	public List<AtaProvaRelVO> executarConsultaParametrizada(TurmaVO turmaVO, CursoVO cursoVO, Integer disciplina, Integer unidadeEnsino, String ano, String semestre, boolean trazerAlunoPendenteFinanceiro, boolean aprovados, boolean reprovados, boolean reprovadosPorFalta, boolean cursando, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota, boolean permitirRealizarLancamentoAlunosPreMatriculados, UsuarioVO usuario) throws Exception {
		StringBuilder selectStr = new StringBuilder("SELECT distinct pessoa.nome AS pessoa_nome, sem_acentos(pessoa.nome) as nomePessoaSemAcento, curso.nome AS curso_nome,  ");
		selectStr.append("matricula.matricula AS matricula_matricula, turma.identificadorturma as turma_nome, ");
		selectStr.append("disciplina.nome AS disciplina_nome, professor.nome AS professor_nome, disciplina.codigo AS disciplina_codigo, ");
		selectStr.append("configuracaoacademiconotaconceito.codigo as mediaFinalConceito,  configuracaoacademiconotaconceito.conceitonota as mediaConceito,  notafinalconceito, mediafinal, quantidadeCasasDecimaisPermitirAposVirgula, utilizaNotaFinalConceito ");
		selectStr.append("from historico ");
		selectStr.append("INNER JOIN MatriculaPeriodo on MatriculaPeriodo.codigo = Historico.matriculaPeriodo ");
		selectStr.append("INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		selectStr.append("INNER JOIN matricula ON matricula.matricula = historico.matricula ");
		selectStr.append("INNER JOIN curso ON matricula.curso = curso.codigo ");
		selectStr.append("INNER JOIN pessoa ON matricula.aluno = pessoa.codigo ");
		selectStr.append("INNER JOIN disciplina ON disciplina.codigo = historico.disciplina ");
		selectStr.append("INNER JOIN UnidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino ");
		selectStr.append("INNER JOIN funcionario on	funcionario.codigo in ( ");
		selectStr.append("select funcionario.codigo from professortitulardisciplinaturma	 INNER JOIN funcionario on professortitulardisciplinaturma.professor = funcionario.pessoa where professortitulardisciplinaturma.turma = ").append(turmaVO.getCodigo()).append(" and disciplina = ").append(disciplina);
		selectStr.append(")");
		selectStr.append("INNER JOIN pessoa AS professor ON professor.codigo = funcionario.pessoa ");
		selectStr.append("INNER JOIN configuracaoacademico ON configuracaoacademico.codigo = historico.configuracaoacademico ");
		selectStr.append("LEFT JOIN configuracaoacademiconotaconceito ON configuracaoacademiconotaconceito.codigo = mediafinalconceito ");
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				selectStr.append("INNER JOIN turma ON turma.codigo = matriculaperiodoturmadisciplina.turmaTeorica ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				selectStr.append("INNER JOIN turma ON turma.codigo = matriculaperiodoturmadisciplina.turmaPratica ");
			} else {
				selectStr.append("INNER JOIN turma ON turma.codigo = matriculaperiodoturmadisciplina.turma ");
			}
		} else {
			selectStr.append("INNER JOIN turma ON turma.codigo = matriculaperiodoturmadisciplina.turma ");
		}
		if (turmaVO.getTurmaAgrupada()) {
		selectStr.append("WHERE (disciplina.codigo = ").append(disciplina);
		selectStr.append(" or disciplina.codigo in(select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turma = ").append(turmaVO.getCodigo()).append(" and disciplina = ").append(disciplina).append(") ");
		selectStr.append(" or disciplina.codigo in(select distinct equivalente from disciplinaequivalente where disciplina = ").append(disciplina).append(") ");
		selectStr.append(" or disciplina.codigo in(select distinct disciplina from disciplinaequivalente where equivalente = ").append(disciplina).append(") ");
		selectStr.append(" ) ");
		}else {
		selectStr.append("WHERE disciplina.codigo = ").append(disciplina);
		}
		if (!cursoVO.getNivelEducacional().equals("PO")) {
			selectStr.append(" AND matricula.situacao = 'AT' ");
		} else {
			if (!ano.equals("") && !semestre.equals("")) {
				if (Uteis.getAnoDataAtual().equals(ano) && Uteis.getSemestreAtual().equals(semestre)) {
					selectStr.append(" AND matricula.situacao = 'AT' ");
				}
			}
		}
//		selectStr.append(" AND (professortitulardisciplinaturma.titular = true ");
//		selectStr.append(" and (professortitulardisciplinaturma.turma = ").append(turmaVO.getCodigo().intValue());
//		if (!turmaVO.getTurmaAgrupada()) {
//			selectStr.append(" or professortitulardisciplinaturma.turma in( select distinct turmaorigem from turmaagrupada where turma = ");
//			selectStr.append(turmaVO.getCodigo().intValue());
//			selectStr.append(") ");
//		}
//		selectStr.append(") and professortitulardisciplinaturma.disciplina = ");
//		selectStr.append(disciplina.intValue());
//		selectStr.append(")");
		if (!cursoVO.getNivelEducacional().equals("PO")) {
			if (!ano.equals("")) {
//				selectStr.append(" AND professortitulardisciplinaturma.ano = '").append(ano).append("'");
				selectStr.append(" AND matriculaperiodo.ano = '").append(ano).append("' ");
			}
			if (!semestre.equals("")) {
//				selectStr.append(" AND professortitulardisciplinaturma.semestre = '").append(semestre).append("'");
				selectStr.append(" AND matriculaperiodo.semestre = '").append(semestre).append("' ");
			}
		}
		if (turmaVO.getTurmaAgrupada()) {
//			selectStr.append(" AND (turma.codigo in ( select turma from turmaagrupada where turmaorigem = ").append(turmaVO.getCodigo()).append("))");
			selectStr.append(" and ((turma.codigo = ").append(turmaVO.getCodigo()).append(" or turma.codigo in (select turma from turmaAgrupada where turmaOrigem =  ").append(turmaVO.getCodigo()).append("))");
			selectStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaPratica is not null and MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
			selectStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaTeorica is not null and MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			selectStr.append(") ");
			
		} else {
			selectStr.append(" AND turma.codigo = ").append(turmaVO.getCodigo()).append(" ");
		}
		if (unidadeEnsino.intValue() != 0) {
			selectStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
		}
		if (!trazerAlunoPendenteFinanceiro) {
			selectStr.append(" AND matriculaPeriodo.situacao <> 'PF' ");
		}
		executarMontagemFiltroSituacaoDisciplina(selectStr, aprovados, reprovados, reprovadosPorFalta, cursando);
		selectStr.append(" AND historico.situacao not in ('AA', 'IS', 'AB', 'CC', 'CH') ");
		selectStr.append(" AND matriculaPeriodo.situacaoMatriculaPeriodo <> 'PR' AND matriculaPeriodo.situacaoMatriculaPeriodo <> 'PC' ");		
			selectStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));		
		if (situacaoRecuperacaoNota != null && !situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.TODAS)) {
			if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.SEM_RECUPERACAO)) {
				selectStr.append(" and historico.matricula not in ( ");
			} else {
				selectStr.append(" and historico.matricula in ( ");
			}
			selectStr.append(" select his.matricula from historiconota ");
			selectStr.append(" inner join historico his on his.codigo = historiconota.historico ");
			selectStr.append(" inner join configuracaoacademiconota on configuracaoacademiconota.configuracaoacademico = his.configuracaoacademico ");
			selectStr.append(" and historiconota.tiponota = configuracaoacademiconota.nota ");
			selectStr.append(" where configuracaoacademiconota.notaRecuperacao ");
			selectStr.append(" and his.matricula = historico.matricula ");
			selectStr.append(" and his.anohistorico = historico.anohistorico ");
			selectStr.append(" and his.semestrehistorico = historico.semestrehistorico ");
			selectStr.append(" and his.matrizcurricular = historico.matrizcurricular ");
			selectStr.append(" and (his.gradedisciplina is not null or his.gradeCurricularGrupoOptativaDisciplina is not null or his.historicoDisciplinaForaGrade = true or his.gradedisciplinacomposta is not null)");
			selectStr.append(" and (his.historicoporequivalencia is null or his.historicoporequivalencia = false)");
			if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.NOTA_NAO_RECUPERADA)) {
				selectStr.append(" and historiconota.situacaorecuperacaonota = 'NOTA_NAO_RECUPERADA' ");
			} else if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.NOTA_RECUPERADA)) {
				selectStr.append(" and historiconota.situacaorecuperacaonota = 'NOTA_RECUPERADA' ");
			} else {
				selectStr.append(" and historiconota.situacaorecuperacaonota in ('NOTA_RECUPERADA', 'NOTA_NAO_RECUPERADA', 'EM_RECUPERACAO') ");
			}
			
			selectStr.append(" order by replace(tiponota, 'NOTA_', '')::INT desc limit 1 ");
			selectStr.append(" ) ");
		}
		SqlRowSet tabelaResultado = null;
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			StringBuffer sql = new StringBuffer();
			sql.append("select t.* from ");
			sql.append("(").append(selectStr).append(") as t ");
			sql.append(" where  (exists (").append(selectStr).append(" and historico.disciplina = t.disciplina_codigo and historico.disciplina = ").append(disciplina).append(" and historico.matricula = t.matricula_matricula limit 1) ") ;
			sql.append(" or not exists (").append(selectStr).append(" and historico.disciplina = ").append(disciplina).append(" and historico.matricula = t.matricula_matricula limit 1)) ") ;			
			sql.append(" order by t.nomePessoaSemAcento ");
			System.out.println(sql.toString());
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		}else {		
			selectStr.append(" ORDER BY nomePessoaSemAcento");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
		}
		
		selectStr.append(" ORDER BY pessoa.nome");

		return (montarDadosConsulta(tabelaResultado, turmaVO.getTurmaAgrupada(), cursoVO.getNome(), turmaVO.getIdentificadorTurma()));
	}

	public List<AtaProvaRelVO> executarConsultaParametrizadaVisaoFuncionario(TurmaVO turmaVO, CursoVO cursoVO, Integer disciplina, Integer professorTitular, Integer unidadeEnsino, String ano, String semestre, FiltroRelatorioAcademicoVO filtroAcademicoVO, boolean aprovados, boolean reprovados, boolean reprovadosPorFalta, boolean cursando, Boolean trazerAlunoTransferenciaMatrizCurricular, SituacaoRecuperacaoNotaEnum situacaoRecuperacaoNota, UsuarioVO usuario, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		StringBuilder selectStr = new StringBuilder("SELECT distinct pessoa.nome AS pessoa_nome, sem_acentos(pessoa.nome) as nomePessoaSemAcento, curso.nome AS curso_nome, ");
		selectStr.append("matricula.matricula AS matricula_matricula, turma.identificadorturma as turma_nome, ");
		selectStr.append("disciplina.codigo AS disciplina_codigo, ");
		selectStr.append("disciplina.nome AS disciplina_nome, professor.nome AS professor_nome, ");
		selectStr.append("configuracaoacademiconotaconceito.codigo as mediaFinalConceito,  configuracaoacademiconotaconceito.conceitonota as mediaConceito,  notafinalconceito, mediafinal, quantidadeCasasDecimaisPermitirAposVirgula, utilizaNotaFinalConceito ");
		selectStr.append("from historico ");
		selectStr.append("INNER JOIN MatriculaPeriodo on MatriculaPeriodo.codigo = Historico.matriculaPeriodo ");
		selectStr.append("INNER JOIN matriculaperiodoturmadisciplina ON matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		selectStr.append("INNER JOIN matricula ON matricula.matricula = historico.matricula ");
		selectStr.append("INNER JOIN curso ON matricula.curso = curso.codigo ");
		selectStr.append("INNER JOIN pessoa ON matricula.aluno = pessoa.codigo ");
		selectStr.append("INNER JOIN disciplina ON disciplina.codigo = historico.disciplina ");
		selectStr.append("INNER JOIN UnidadeEnsino on unidadeEnsino.codigo = matricula.unidadeEnsino ");
		selectStr.append("INNER JOIN funcionario ON funcionario.codigo = ").append(professorTitular);
		selectStr.append("INNER JOIN pessoa AS professor ON professor.codigo = funcionario.pessoa ");
		selectStr.append("INNER JOIN configuracaoacademico ON configuracaoacademico.codigo = historico.configuracaoacademico ");
		selectStr.append("LEFT JOIN configuracaoacademiconotaconceito ON configuracaoacademiconotaconceito.codigo = mediafinalconceito ");
		if (turmaVO.getSubturma()) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				selectStr.append("INNER JOIN turma ON turma.codigo = matriculaperiodoturmadisciplina.turmaTeorica ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				selectStr.append("INNER JOIN turma ON turma.codigo = matriculaperiodoturmadisciplina.turmaPratica ");
			} else {
				selectStr.append("INNER JOIN turma ON turma.codigo = matriculaperiodoturmadisciplina.turma ");
			}
		} else {
			selectStr.append("INNER JOIN turma ON turma.codigo = matriculaperiodoturmadisciplina.turma ");
		}
		if (turmaVO.getTurmaAgrupada()) {
			selectStr.append("WHERE (disciplina.codigo = ").append(disciplina);
			selectStr.append(" or disciplina.codigo in(select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turma = ").append(turmaVO.getCodigo()).append(" and disciplina = ").append(disciplina).append(") ");
			selectStr.append(" or disciplina.codigo in(select distinct equivalente from disciplinaequivalente where disciplina = ").append(disciplina).append(") ");
			selectStr.append(" or disciplina.codigo in(select distinct disciplina from disciplinaequivalente where equivalente = ").append(disciplina).append(") ");
			selectStr.append(" ) ");
		} else {
			selectStr.append("WHERE disciplina.codigo = ").append(disciplina);
		}
		if (!cursoVO.getPeriodicidade().equals("IN")) {
			if (cursoVO.getPeriodicidade().equals("AN")) {
				selectStr.append(" AND matriculaperiodo.ano = '").append(ano).append("' ");
			} else {
				if (!ano.equals("")) {
					selectStr.append(" AND matriculaperiodo.ano = '").append(ano).append("' ");
				}
				if (!semestre.equals("")) {
					selectStr.append(" AND matriculaperiodo.semestre = '").append(semestre).append("' ");
				}
			}
		}
		
		if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			selectStr.append(" and ((turma.codigo = ").append(turmaVO.getCodigo()).append(" or turma.codigo in (select turma from turmaAgrupada where turmaOrigem =  ").append(turmaVO.getCodigo()).append("))");
			selectStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaPratica is not null and MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
			selectStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaTeorica is not null and MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			selectStr.append(") ");
		} else {
			selectStr.append(" and Turma.codigo = ").append(turmaVO.getCodigo());
		}
		if (!turmaVO.getSubturma() && !turmaVO.getTurmaAgrupada()) {
			selectStr.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
		}
//		if (unidadeEnsino.intValue() != 0) {
//			selectStr.append(" AND turma.unidadeEnsino = ").append(unidadeEnsino).append(" ");
//		}
		selectStr.append(" AND historico.situacao not in ('AA', 'IS', 'AB', 'CC', 'CH') ");
		executarMontagemFiltroSituacaoDisciplina(selectStr, aprovados, reprovados, reprovadosPorFalta, cursando);
		selectStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaPeriodo"));
		selectStr.append(" AND ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaPeriodo"));
		
		if ((usuario.getIsApresentarVisaoProfessor() || usuario.getIsApresentarVisaoCoordenador()) && !permitirRealizarLancamentoAlunosPreMatriculados) {
			selectStr.append(" and matriculaperiodo.situacaoMatriculaPeriodo <> 'PR' ");
		}
		
		
		if (!trazerAlunoTransferenciaMatrizCurricular) {
			selectStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual("and"));
		}
		
		if (situacaoRecuperacaoNota != null && situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.EM_RECUPERACAO)) {
			selectStr.append(" AND historico.situacao not in ('AA', 'AP', 'AE', 'IS', 'CH', 'AD', 'AB') ");
		}
		
		if (situacaoRecuperacaoNota != null && !situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.TODAS)) {
			if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.SEM_RECUPERACAO)) {
				selectStr.append(" and historico.matricula not in ( ");
			} else {
				selectStr.append(" and historico.matricula in ( ");
			}
			selectStr.append(" select his.matricula from historiconota ");
			selectStr.append(" inner join historico his on his.codigo = historiconota.historico ");
			selectStr.append(" inner join configuracaoacademiconota on configuracaoacademiconota.configuracaoacademico = his.configuracaoacademico ");
			selectStr.append(" and historiconota.tiponota = configuracaoacademiconota.nota ");
			selectStr.append(" where configuracaoacademiconota.notaRecuperacao ");
			selectStr.append(" and his.matricula = historico.matricula ");
			selectStr.append(" and his.anohistorico = historico.anohistorico ");
			selectStr.append(" and his.semestrehistorico = historico.semestrehistorico ");
			selectStr.append(" and his.matrizcurricular = historico.matrizcurricular ");
			selectStr.append(" and his.disciplina = historico.disciplina ");
			selectStr.append(" and (his.gradedisciplina is not null or his.gradeCurricularGrupoOptativaDisciplina is not null or his.historicoDisciplinaForaGrade = true or his.gradedisciplinacomposta is not null)");
			selectStr.append(" and (his.historicoporequivalencia is null or his.historicoporequivalencia = false)");
			if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.NOTA_NAO_RECUPERADA)) {
				selectStr.append(" and historiconota.situacaorecuperacaonota = 'NOTA_NAO_RECUPERADA' ");
			} else if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.NOTA_RECUPERADA)) {
				selectStr.append(" and historiconota.situacaorecuperacaonota = 'NOTA_RECUPERADA' ");
			} else if (situacaoRecuperacaoNota.equals(SituacaoRecuperacaoNotaEnum.EM_RECUPERACAO)) {
				selectStr.append(" and historiconota.situacaorecuperacaonota = 'EM_RECUPERACAO' ");
			} else {
				selectStr.append(" and historiconota.situacaorecuperacaonota in ('NOTA_RECUPERADA', 'NOTA_NAO_RECUPERADA', 'EM_RECUPERACAO') ");
			}
			
			selectStr.append(" order by replace(tiponota, 'NOTA_', '')::INT desc limit 1 ");
			selectStr.append(" ) ");
		}
		
		
		SqlRowSet tabelaResultado = null;
		if (turmaVO.getTurmaAgrupada()) {
			StringBuffer sql = new StringBuffer();
			sql.append("select t.* from ");
			sql.append("(").append(selectStr).append(") as t ");
			sql.append(" where  (exists (").append(selectStr).append(" and historico.disciplina = t.disciplina_codigo and historico.disciplina = ").append(disciplina).append(" and historico.matricula = t.matricula_matricula limit 1) ") ;
			sql.append(" or not exists (").append(selectStr).append(" and historico.disciplina = ").append(disciplina).append(" and historico.matricula = t.matricula_matricula limit 1)) ") ;			
			sql.append(" order by t.nomePessoaSemAcento ");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		}else {		
			selectStr.append(" ORDER BY nomePessoaSemAcento");
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
		}
		return montarDadosConsulta(tabelaResultado, turmaVO.getTurmaAgrupada(), cursoVO.getNome(), turmaVO.getIdentificadorTurma());
	}

	private List<AtaProvaRelVO> montarDadosConsulta(SqlRowSet tabelaResultado, Boolean turmaAgrupada, String nomeCurso, String identificadorTurma) throws Exception {
		List<AtaProvaRelVO> vetResultado = new ArrayList<AtaProvaRelVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, turmaAgrupada, nomeCurso, identificadorTurma));
		}
		return vetResultado;
	}

	public AtaProvaRelVO montarDados(SqlRowSet dadosSql, Boolean turmaAgrupada, String nomeCurso, String identificadorTurma) throws Exception {
		AtaProvaRelVO obj = new AtaProvaRelVO();
		obj.setPessoaNome(dadosSql.getString("pessoa_nome"));
		obj.setMatricula(dadosSql.getString("matricula_matricula"));
		obj.setDisciplinaNome(dadosSql.getString("disciplina_nome"));
		obj.setProfessorNome(dadosSql.getString("professor_nome"));
		obj.setCursoNome(nomeCurso);
		obj.setTurmaNome(identificadorTurma);
		if (dadosSql.getInt("mediaFinalConceito") > 0) {
			obj.setMediaFinal(dadosSql.getString("mediaConceito"));
		   } else if (dadosSql.getBoolean("utilizaNotaFinalConceito")) {
			   obj.setMediaFinal(dadosSql.getString("notaFinalConceito"));
		   } else {
		    if (dadosSql.getObject("mediaFinal") == null) {
		    	obj.setMediaFinal("");
		    } else {
		    	obj.setMediaFinal(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(dadosSql.getDouble("mediaFinal"), dadosSql.getInt("quantidadeCasasDecimaisPermitirAposVirgula")));
		    }
		    if (obj.getMediaFinal().trim().isEmpty()) {
		    	obj.setMediaFinal("----");
		    }
		   }
		return obj;
	}

	public static String getDesignIReportRelatorio(String layout) {
		if (layout.equals("layout1")) {
			return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml";
		}
		if (layout.equals("layout2")) {
			return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeLayout2() + ".jrxml";
		}
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeLayout3() + ".jrxml";
	}

	public static String getIdEntidade() {
		return "AtaProvaRel";
	}

	public static String getIdEntidadeLayout2() {
		return "AtaProvaRelLayout2";
	}

	private void executarMontagemFiltroSituacaoDisciplina(StringBuilder sqlStr, boolean aprovados, boolean reprovados, boolean reprovadosPorFalta, boolean cursando) throws Exception {
		if (aprovados || reprovados || reprovadosPorFalta || cursando) {
			sqlStr.append(" and historico.situacao in (''");
			if (aprovados) {
				for (SituacaoHistorico sh : SituacaoHistorico.getSituacoesDeAprovacao()) {
					sqlStr.append(", '").append(sh.getValor()).append("'");
				}
			}
			if (reprovados) {
				for (SituacaoHistorico sh : SituacaoHistorico.getSituacoesDeReprovacao()) {
					if(!sh.getValor().equals("RF")) {
						sqlStr.append(", '").append(sh.getValor()).append("'");
					}
				}
			}
			if (cursando) {
				for (SituacaoHistorico sh : SituacaoHistorico.getSituacoesDeCursando()) {
					sqlStr.append(", '").append(sh.getValor()).append("'");
				}
			}
			if (reprovadosPorFalta) {
				sqlStr.append(", '").append(SituacaoHistorico.REPROVADO_FALTA.getValor()).append("'");
			}
			sqlStr.append(") ");
		}
	}
	
	public static String getIdEntidadeLayout3() {
		return "AtaProvaRelLayout3";
	}

}
