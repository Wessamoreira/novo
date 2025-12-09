package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.ConfiguracaoAcademicaNotaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoRecuperacaoNotaEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import relatorio.negocio.comuns.academico.MapaNotaPendenciaAlunoDisciplinaRelVO;
import relatorio.negocio.comuns.academico.MapaNotaPendenciaAlunoRelVO;
import relatorio.negocio.comuns.academico.MapaNotaPendenciaAlunoTurmaRelVO;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;
import relatorio.negocio.interfaces.academico.MapaNotaPendenciaAlunoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * @author Wellington Rodrigues - 16 de jul de 2015
 *
 */
@Repository
public class MapaNotaPendenciaAlunoRel extends SuperRelatorio implements MapaNotaPendenciaAlunoRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	@Override
	public List<MapaNotaPendenciaAlunoRelVO> executarCriacaoObjeto(Integer unidadeEnsino, String periodicidade, String ano, String semestre, Integer curso, TurmaVO turma, Integer disciplina, String situacaoAluno, Integer configuracaoAcademico, String situacaoNotaRecuperacao, String tipoLayout, String filtrarNota, List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaNaoRecuperacaoVOs, UsuarioVO usuarioVO) throws Exception {
		if (tipoLayout.equals("sintetico")) {
			return executarCriacaoObjetoSintetico(unidadeEnsino, periodicidade, ano, semestre, curso, turma, disciplina, situacaoAluno, configuracaoAcademico, situacaoNotaRecuperacao, filtrarNota, configuracaoAcademicaNotaVOs, filtroRelatorioAcademicoVO, usuarioVO);
		}
		return executarCriacaoObjetoAnalitico(unidadeEnsino, periodicidade, ano, semestre, curso, turma, disciplina, situacaoAluno, configuracaoAcademico, situacaoNotaRecuperacao, filtrarNota, configuracaoAcademicaNotaVOs, filtroRelatorioAcademicoVO, configuracaoAcademicaNotaNaoRecuperacaoVOs, usuarioVO);
	}

	private List<MapaNotaPendenciaAlunoRelVO> executarCriacaoObjetoSintetico(Integer unidadeEnsino, String periodicidade, String ano, String semestre, Integer curso, TurmaVO turma, Integer disciplina, String situacaoAluno, Integer configuracaoAcademico, String situacaoNotaRecuperacao, String filtrarNota, List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select matricula.matricula as matriculaAluno, curso.nome as nomeCurso, identificadorTurma, turno.nome as nomeTurno, aluno.nome as nomeAluno, sem_acentos(aluno.nome) as nomeAlunoSemAcentos, disciplina.codigo as codigoDisciplina, disciplina.nome as nomeDisciplina ");
		sqlStr.append("from historico ");
		sqlStr.append("inner join matricula on matricula.matricula = historico.matricula ");
		sqlStr.append("inner join pessoa aluno on aluno.codigo = matricula.aluno ");
		sqlStr.append("inner join disciplina on disciplina.codigo = historico.disciplina ");
		sqlStr.append("inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
		sqlStr.append("left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		if (turma.getSubturma() && (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA) || turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA))) {
			if (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("left join Turma ON turma.codigo = matriculaperiodoturmadisciplina.turmaTeorica ");
			} else if (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("left join Turma ON turma.codigo = matriculaperiodoturmadisciplina.turmaPratica ");
			}
		} else {
			sqlStr.append("left join turma on turma.codigo = case when matriculaperiodoturmadisciplina.turma is not null then matriculaperiodoturmadisciplina.turma else matriculaPeriodo.turma end ");
		}
		sqlStr.append("left join turno on turno.codigo = turma.turno ");
		sqlStr.append("left join curso on curso.codigo = turma.curso ");
		sqlStr.append("inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo and unidadeensinocurso.turno = turno.codigo and unidadeensinocurso.unidadeensino = turma.unidadeensino ");
		sqlStr.append("inner join configuracaoacademico on configuracaoacademico.codigo = historico.configuracaoAcademico ");
		sqlStr.append("left join gradedisciplina on gradedisciplina.codigo = historico.gradedisciplina ");
		sqlStr.append("left join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina ");
		sqlStr.append("left JOIN periodoletivo ON case when matriculaperiodo.codigo is not null then matriculaperiodo.periodoletivomatricula else case when historico.periodoletivocursada is not null then historico.periodoletivocursada else historico.periodoletivomatrizcurricular end end  = periodoletivo.codigo ");
		adicionarFiltroClausulaWhere(sqlStr, unidadeEnsino, periodicidade, ano, semestre, curso, turma, disciplina, situacaoAluno, configuracaoAcademico, situacaoNotaRecuperacao, filtrarNota, configuracaoAcademicaNotaVOs, filtroRelatorioAcademicoVO);
		SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDados(rowSet);
	}

	private List<MapaNotaPendenciaAlunoRelVO> montarDados(SqlRowSet rowSet) throws Exception {
		List<MapaNotaPendenciaAlunoRelVO> mapaNotaPendenciaAlunoRelVOs = new ArrayList<MapaNotaPendenciaAlunoRelVO>(0);
		while (rowSet.next()) {
			boolean novaMatricula = true;
			for (MapaNotaPendenciaAlunoRelVO mapaNotaPendenciaAlunoRelVO : mapaNotaPendenciaAlunoRelVOs) {
				if (mapaNotaPendenciaAlunoRelVO.getMatricula().equals(rowSet.getString("matriculaAluno"))) {
					mapaNotaPendenciaAlunoRelVO.setDisciplinas(mapaNotaPendenciaAlunoRelVO.getDisciplinas() + ", " + rowSet.getString("nomeDisciplina"));
					novaMatricula = false;
					break;
				}
			}
			if (novaMatricula) {
				MapaNotaPendenciaAlunoRelVO obj = new MapaNotaPendenciaAlunoRelVO();
				obj.setMatricula(rowSet.getString("matriculaAluno"));
				obj.setNomeCurso(rowSet.getString("nomeCurso"));
				obj.setIdentificadorTurma(rowSet.getString("identificadorTurma"));
				obj.setNomeTurno(rowSet.getString("nomeTurno"));
				obj.setNomeAluno(rowSet.getString("nomeAluno"));
				obj.setDisciplinas(rowSet.getString("nomeDisciplina"));
				mapaNotaPendenciaAlunoRelVOs.add(obj);
			}
		}
		return mapaNotaPendenciaAlunoRelVOs;
	}

	private List<MapaNotaPendenciaAlunoRelVO> executarCriacaoObjetoAnalitico(Integer unidadeEnsino, String periodicidade, String ano, String semestre, Integer curso, TurmaVO turma, Integer disciplina, String situacaoAluno, Integer configuracaoAcademico, String situacaoNotaRecuperacao, String filtrarNota, List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaNaoRecuperacaoVOs, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select matricula.matricula as matriculaAluno, curso.nome as nomeCurso, identificadorTurma, turno.nome as nomeTurno, aluno.nome as nomeAluno, sem_acentos(aluno.nome) as nomeAlunoSemAcentos, disciplina.codigo as codigoDisciplina, disciplina.nome as nomeDisciplina, ");
		sqlStr.append("historico.nota1, historico.nota2, historico.nota3, historico.nota4, historico.nota5, historico.nota6, historico.nota7, historico.nota8, historico.nota9, historico.nota10, ");
		sqlStr.append("historico.nota11, historico.nota12, historico.nota13, historico.nota14, historico.nota15, historico.nota16, historico.nota17, historico.nota18, historico.nota19, historico.nota20, ");
		sqlStr.append("historico.nota21, historico.nota22, historico.nota23, historico.nota24, historico.nota25, historico.nota26, historico.nota27, historico.nota28, historico.nota29, historico.nota30, ");
		sqlStr.append("historico.nota31, historico.nota32, historico.nota33, historico.nota34, historico.nota35, historico.nota36, historico.nota37, historico.nota38, historico.nota39, historico.nota40, ");
		sqlStr.append("notaConceito1.abreviaturaConceitoNota as notaConceito1, notaConceito2.abreviaturaConceitoNota as notaConceito2, notaConceito3.abreviaturaConceitoNota as notaConceito3, notaConceito4.abreviaturaConceitoNota as notaConceito4, notaConceito5.abreviaturaConceitoNota as notaConceito5, notaConceito6.abreviaturaConceitoNota as notaConceito6, notaConceito7.abreviaturaConceitoNota as notaConceito7, notaConceito8.abreviaturaConceitoNota as notaConceito8, notaConceito9.abreviaturaConceitoNota as notaConceito9, notaConceito10.abreviaturaConceitoNota as notaConceito10, ");
		sqlStr.append("notaConceito11.abreviaturaConceitoNota as notaConceito11, notaConceito12.abreviaturaConceitoNota as notaConceito12, notaConceito13.abreviaturaConceitoNota as notaConceito13, notaConceito14.abreviaturaConceitoNota as notaConceito14, notaConceito15.abreviaturaConceitoNota as notaConceito15, notaConceito16.abreviaturaConceitoNota as notaConceito16, notaConceito17.abreviaturaConceitoNota as notaConceito17, notaConceito18.abreviaturaConceitoNota as notaConceito18, notaConceito19.abreviaturaConceitoNota as notaConceito19, notaConceito20.abreviaturaConceitoNota as notaConceito20, ");
		sqlStr.append("notaConceito21.abreviaturaConceitoNota as notaConceito21, notaConceito22.abreviaturaConceitoNota as notaConceito22, notaConceito23.abreviaturaConceitoNota as notaConceito23, notaConceito24.abreviaturaConceitoNota as notaConceito24, notaConceito25.abreviaturaConceitoNota as notaConceito25, notaConceito26.abreviaturaConceitoNota as notaConceito26, notaConceito27.abreviaturaConceitoNota as notaConceito27, notaConceito28.abreviaturaConceitoNota as notaConceito28, notaConceito29.abreviaturaConceitoNota as notaConceito29, notaConceito30.abreviaturaConceitoNota as notaConceito30, ");
		sqlStr.append("notaConceito31.abreviaturaConceitoNota as notaConceito31, notaConceito32.abreviaturaConceitoNota as notaConceito32, notaConceito33.abreviaturaConceitoNota as notaConceito33, notaConceito34.abreviaturaConceitoNota as notaConceito34, notaConceito35.abreviaturaConceitoNota as notaConceito35, notaConceito36.abreviaturaConceitoNota as notaConceito36, notaConceito37.abreviaturaConceitoNota as notaConceito37, notaConceito38.abreviaturaConceitoNota as notaConceito38, notaConceito39.abreviaturaConceitoNota as notaConceito39, notaConceito40.abreviaturaConceitoNota as notaConceito40, ");
		sqlStr.append("quantidadeCasasDecimaisPermitirAposVirgula ");
		sqlStr.append("from historico ");
		sqlStr.append("inner join matricula on matricula.matricula = historico.matricula ");
		sqlStr.append("inner join pessoa aluno on aluno.codigo = matricula.aluno ");
		sqlStr.append("inner join disciplina on disciplina.codigo = historico.disciplina ");
		sqlStr.append("inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
		sqlStr.append("inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append("inner join turno on turno.codigo = matricula.turno ");
		sqlStr.append("inner join configuracaoacademico on configuracaoacademico.codigo = historico.configuracaoAcademico ");
	
		sqlStr.append("left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = matricula.matricula and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		if (turma.getSubturma() && (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA) || turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA))) {
			if (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("left join Turma ON turma.codigo = matriculaperiodoturmadisciplina.turmaTeorica ");
			} else if (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("left join Turma ON turma.codigo = matriculaperiodoturmadisciplina.turmaPratica ");
			}
		} else {
			sqlStr.append("left join turma on turma.codigo = case when matriculaperiodoturmadisciplina.turma is not null then matriculaperiodoturmadisciplina.turma else matriculaPeriodo.turma end ");
		}
		sqlStr.append("inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo and unidadeensinocurso.turno = turno.codigo and unidadeensinocurso.unidadeensino = turma.unidadeensino ");
		
		sqlStr.append("left join gradedisciplina on gradedisciplina.codigo = historico.gradedisciplina ");
		sqlStr.append("left join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina ");
		sqlStr.append("left JOIN periodoletivo ON case when matriculaperiodo.codigo is not null then matriculaperiodo.periodoletivomatricula else case when historico.periodoletivocursada is not null then historico.periodoletivocursada else historico.periodoletivomatrizcurricular end end  = periodoletivo.codigo ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito1 on notaConceito1.codigo = historico.nota1Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito2 on notaConceito2.codigo = historico.nota2Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito3 on notaConceito3.codigo = historico.nota3Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito4 on notaConceito4.codigo = historico.nota4Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito5 on notaConceito5.codigo = historico.nota5Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito6 on notaConceito6.codigo = historico.nota6Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito7 on notaConceito7.codigo = historico.nota7Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito8 on notaConceito8.codigo = historico.nota8Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito9 on notaConceito9.codigo = historico.nota9Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito10 on notaConceito10.codigo = historico.nota10Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito11 on notaConceito11.codigo = historico.nota11Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito12 on notaConceito12.codigo = historico.nota12Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito13 on notaConceito13.codigo = historico.nota13Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito14 on notaConceito14.codigo = historico.nota14Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito15 on notaConceito15.codigo = historico.nota15Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito16 on notaConceito16.codigo = historico.nota16Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito17 on notaConceito17.codigo = historico.nota17Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito18 on notaConceito18.codigo = historico.nota18Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito19 on notaConceito19.codigo = historico.nota19Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito20 on notaConceito20.codigo = historico.nota20Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito21 on notaConceito21.codigo = historico.nota21Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito22 on notaConceito22.codigo = historico.nota22Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito23 on notaConceito23.codigo = historico.nota23Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito24 on notaConceito24.codigo = historico.nota24Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito25 on notaConceito25.codigo = historico.nota25Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito26 on notaConceito26.codigo = historico.nota26Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito27 on notaConceito27.codigo = historico.nota27Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito28 on notaConceito28.codigo = historico.nota28Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito29 on notaConceito29.codigo = historico.nota29Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito30 on notaConceito30.codigo = historico.nota30Conceito ");
		
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito31 on notaConceito31.codigo = historico.nota31Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito32 on notaConceito32.codigo = historico.nota32Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito33 on notaConceito33.codigo = historico.nota33Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito34 on notaConceito34.codigo = historico.nota34Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito35 on notaConceito35.codigo = historico.nota35Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito36 on notaConceito36.codigo = historico.nota36Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito37 on notaConceito37.codigo = historico.nota37Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito38 on notaConceito38.codigo = historico.nota38Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito39 on notaConceito39.codigo = historico.nota39Conceito ");
		sqlStr.append("left join configuracaoacademiconotaconceito as notaConceito40 on notaConceito40.codigo = historico.nota40Conceito ");
		
		adicionarFiltroClausulaWhere(sqlStr, unidadeEnsino, periodicidade, ano, semestre, curso, turma, disciplina, situacaoAluno, configuracaoAcademico, situacaoNotaRecuperacao, filtrarNota, configuracaoAcademicaNotaVOs, filtroRelatorioAcademicoVO);
		//System.out.println(sqlStr);
		SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosAnalitico(rowSet, configuracaoAcademicaNotaVOs, configuracaoAcademicaNotaNaoRecuperacaoVOs);
	}

	private void adicionarFiltroClausulaWhere(StringBuilder sqlStr, Integer unidadeEnsino, String periodicidade, String ano, String semestre, Integer curso, TurmaVO turma, Integer disciplina, String situacaoAluno, Integer configuracaoAcademico, String situacaoNotaRecuperacao, String filtrarNota, List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) throws Exception {
		sqlStr.append("WHERE turma.unidadeensino = ").append(unidadeEnsino);
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));		
		sqlStr.append(" AND curso.periodicidade = '").append(periodicidade).append("'");
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" AND historico.anohistorico = '").append(ano).append("'");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" AND historico.semestrehistorico = '").append(semestre).append("'");
		}
		if (Uteis.isAtributoPreenchido(curso) && !Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append(" AND curso.codigo = ").append(curso);
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			if (turma.getTurmaAgrupada() && !turma.getSubturma()) {
				sqlStr.append(" AND ((turma.codigo = ").append(turma.getCodigo()).append(" or (MatriculaPeriodoTurmaDisciplina.turmaTeorica is null and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and  turma.codigo in (select turma from turmaAgrupada where turmaOrigem =  ").append(turma.getCodigo()).append("))) ");
				sqlStr.append(" or (MatriculaPeriodoTurmaDisciplina.turmaPratica is not null and MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turma.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
				sqlStr.append(" or (MatriculaPeriodoTurmaDisciplina.turmaTeorica is not null and MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turma.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
				sqlStr.append(") ");
			} else {
				sqlStr.append(" AND Turma.codigo = ").append(turma.getCodigo());
			}
			if (!turma.getSubturma() && !turma.getTurmaAgrupada()) {
				sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
			}
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" AND (disciplina.codigo = ").append(disciplina);
			if(Uteis.isAtributoPreenchido(turma) && turma.getTurmaAgrupada()) {
				sqlStr.append(" or Disciplina.codigo in (select distinct disciplinaequivalenteTurmaagrupada from turmadisciplina where turma = ").append(turma.getCodigo()).append(" and turmadisciplina.disciplina = ").append(disciplina.intValue()).append(") ");
				sqlStr.append(" or Disciplina.codigo in (select distinct equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.intValue()).append(") ");
				sqlStr.append(" or Disciplina.codigo in (select distinct disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.intValue()).append(") ");
			}
			sqlStr.append(" ) ");
		}
		if (Uteis.isAtributoPreenchido(configuracaoAcademico)) {
			sqlStr.append(" AND configuracaoAcademico.codigo = ").append(configuracaoAcademico);
		} else {
			sqlStr.append(" AND historico.configuracaoacademico is not null");
		}
		// Quando a periodicidade do curso for INTEGRAL,o sistema irá considerar a situação da matícula do aluno.	
		if (periodicidade.equals("IN")) {
			sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatricula(filtroRelatorioAcademicoVO, "matricula"));
		} else {
			sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		}
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" AND (historico.historicoDisciplinaFazParteComposicao is null or historico.historicoDisciplinaFazParteComposicao = false)");
		if (situacaoAluno.equals("recuperacao")) {
			adicionarFiltroSituacaoNotaRecuperacao(configuracaoAcademicaNotaVOs, situacaoNotaRecuperacao, sqlStr, filtrarNota);
		} else {
			adicionarFiltroSegundaChamada(configuracaoAcademicaNotaVOs, sqlStr, filtrarNota);
		}

		sqlStr.append(" and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null)");
		sqlStr.append(" and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false)");
		sqlStr.append(" order by nomeCurso, identificadorTurma, nomeAlunoSemAcentos, nomeDisciplina");
	}

	private void adicionarFiltroSituacaoNotaRecuperacao(List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs, String situacaoNotaRecuperacao, StringBuilder sqlStr, String filtrarNota) throws Exception {
		if (situacaoNotaRecuperacao.equals(SituacaoRecuperacaoNotaEnum.SEM_RECUPERACAO.name())) {
			sqlStr.append(" and NOT EXISTS (");
		} else {
			sqlStr.append(" and EXISTS (");
		}
		sqlStr.append("select his.codigo from historiconota ");
		sqlStr.append("inner join historico his on his.codigo = historiconota.historico ");
		sqlStr.append("inner join configuracaoacademiconota on configuracaoacademiconota.configuracaoacademico = his.configuracaoacademico ");
		sqlStr.append("and historiconota.tiponota = configuracaoacademiconota.nota ");
		sqlStr.append("where configuracaoacademiconota.notaRecuperacao ");
		sqlStr.append("and his.codigo = historico.codigo ");
		sqlStr.append("and his.matricula = historico.matricula ");
		sqlStr.append("and his.anohistorico = historico.anohistorico ");
		sqlStr.append("and his.semestrehistorico = historico.semestrehistorico ");
		sqlStr.append("and his.matrizcurricular = historico.matrizcurricular ");
		sqlStr.append("and (his.gradedisciplina is not null or his.gradeCurricularGrupoOptativaDisciplina is not null or his.historicoDisciplinaForaGrade = true or his.gradedisciplinacomposta is not null) ");
		sqlStr.append("and (his.historicoporequivalencia is null or his.historicoporequivalencia = false) ");
		if (situacaoNotaRecuperacao.equals(SituacaoRecuperacaoNotaEnum.NOTA_NAO_RECUPERADA.name())) {
			sqlStr.append(" and historiconota.situacaorecuperacaonota in ('NOTA_NAO_RECUPERADA', 'EM_RECUPERACAO') ");
			sqlStr.append(" and his.situacao not in ('AP', 'AA', 'AE', 'IS', 'CC', 'CH') ");
		} else if (situacaoNotaRecuperacao.equals(SituacaoRecuperacaoNotaEnum.NOTA_RECUPERADA.name())) {
			sqlStr.append(" and historiconota.situacaorecuperacaonota = 'NOTA_RECUPERADA' ");
		} else {
			sqlStr.append(" and historiconota.situacaorecuperacaonota in ('NOTA_NAO_RECUPERADA', 'EM_RECUPERACAO', 'NOTA_RECUPERADA') ");
			sqlStr.append(" and his.situacao not in ('AP', 'AA', 'AE', 'IS', 'CC', 'CH') ");
		}
		int qtdeNotaSelecionada = 0;
		for (ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO : configuracaoAcademicaNotaVOs) {
			if (configuracaoAcademicaNotaVO.getUtilizarNotaSegundaChamada()) {
				qtdeNotaSelecionada++;
			}
		}
		sqlStr.append(" and (configuracaoacademiconota.nota = ");
		int qtdeIterada = 1;
		for (ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO : configuracaoAcademicaNotaVOs) {
			if (configuracaoAcademicaNotaVO.getUtilizarNotaSegundaChamada()) {
				sqlStr.append("'").append(configuracaoAcademicaNotaVO.getNota()).append("'");
				if (qtdeIterada < qtdeNotaSelecionada) {
					if (filtrarNota.equals("combinada")) {
						sqlStr.append(" AND configuracaoacademiconota.nota = ");
					} else {
						sqlStr.append(" OR configuracaoacademiconota.nota = ");
					}
				}
				qtdeIterada++;
			}
		}
		sqlStr.append(")");
		sqlStr.append(" order by replace(tiponota, 'NOTA_', '')::INT) ");
	}

	public void adicionarFiltroSegundaChamada(List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs, StringBuilder sqlStr, String filtrarNota) throws Exception {
		int qtdeNotaSelecionada = 0;
		for (ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO : configuracaoAcademicaNotaVOs) {
			if (configuracaoAcademicaNotaVO.getUtilizarNotaSegundaChamada()) {
				qtdeNotaSelecionada++;
			}
		}
		sqlStr.append(" and (");
		int qtdeIterada = 1;
		for (ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO : configuracaoAcademicaNotaVOs) {
			if (configuracaoAcademicaNotaVO.getUtilizarNotaSegundaChamada()) {
				sqlStr.append("historico.nota").append(configuracaoAcademicaNotaVO.getNota().getNumeroNota()).append(" is null");
				if (qtdeIterada < qtdeNotaSelecionada) {
					if (filtrarNota.equals("combinada")) {
						sqlStr.append(" AND ");
					} else {
						sqlStr.append(" OR ");
					}
				}
				qtdeIterada++;
			}
		}
		sqlStr.append(")");
	}
	
	private List<MapaNotaPendenciaAlunoRelVO> montarDadosAnalitico(SqlRowSet rowSet, List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs, List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaNaoRecuperacaoVOs) throws Exception {
		List<MapaNotaPendenciaAlunoRelVO> mapaNotaPendenciaAlunoRelVOs = new ArrayList<MapaNotaPendenciaAlunoRelVO>(0);
		while (rowSet.next()) {
			boolean novaMatricula = true;
			for (MapaNotaPendenciaAlunoRelVO mapaNotaPendenciaAlunoRelVO : mapaNotaPendenciaAlunoRelVOs) {
				if (mapaNotaPendenciaAlunoRelVO.getMatricula().equals(rowSet.getString("matriculaAluno"))) {
					mapaNotaPendenciaAlunoRelVO.getCrosstabVOs().addAll(montarDadosConfiguracaoAcademicoCrosstab(configuracaoAcademicaNotaVOs, configuracaoAcademicaNotaNaoRecuperacaoVOs, rowSet));
					novaMatricula = false;
					break;
				}
			}
			if (novaMatricula) {
				MapaNotaPendenciaAlunoRelVO obj = new MapaNotaPendenciaAlunoRelVO();
				obj.setMatricula(rowSet.getString("matriculaAluno"));
				obj.setNomeCurso(rowSet.getString("nomeCurso"));
				obj.setIdentificadorTurma(rowSet.getString("identificadorTurma"));
				obj.setNomeTurno(rowSet.getString("nomeTurno"));
				obj.setNomeAluno(rowSet.getString("nomeAluno"));
				obj.setCrosstabVOs(montarDadosConfiguracaoAcademicoCrosstab(configuracaoAcademicaNotaVOs, configuracaoAcademicaNotaNaoRecuperacaoVOs, rowSet));
				mapaNotaPendenciaAlunoRelVOs.add(obj);
			}
		}
		return mapaNotaPendenciaAlunoRelVOs;
	}

	private List<CrosstabVO> montarDadosConfiguracaoAcademicoCrosstab(List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs, List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaNaoRecuperacaoVOs, SqlRowSet rowSet) throws Exception {
		List<CrosstabVO> crosstabVOs = new ArrayList<CrosstabVO>(0);

		configuracaoAcademicaNotaVOs.addAll(configuracaoAcademicaNotaNaoRecuperacaoVOs);
		//configuracaoAcademicaNotaVOs.s
		
		//Collections.sort(configuracaoAcademicaNotaVOs, Comparator.comparing(ConfiguracaoAcademicaNotaVO::getCodigo));

		for (ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO : configuracaoAcademicaNotaVOs) {
			if (configuracaoAcademicaNotaVO.getUtilizarNotaSegundaChamada()) {
				if ((configuracaoAcademicaNotaVO.getUtilizarNota() && configuracaoAcademicaNotaVO.getApresentarNota()) || (configuracaoAcademicaNotaVO.getUtilizarNota() && configuracaoAcademicaNotaVO.getUtilizarComoMediaFinal())) {
					CrosstabVO crosstab = new CrosstabVO();
					crosstab.setLabelLinha(rowSet.getString("nomeDisciplina"));
					crosstab.setOrdemColuna(configuracaoAcademicaNotaVO.getNota().getNumeroNota());
					crosstab.setLabelColuna(configuracaoAcademicaNotaVO.getTitulo());
					if (configuracaoAcademicaNotaVO.getUtilizarNotaPorConceito()) {
						crosstab.setValorString(rowSet.getString("notaConceito" + configuracaoAcademicaNotaVO.getNota().getNumeroNota()));
					} else {
						if (rowSet.getObject("nota" + configuracaoAcademicaNotaVO.getNota().getNumeroNota()) != null) {
							crosstab.setValorDouble(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(rowSet.getDouble("nota" + configuracaoAcademicaNotaVO.getNota().getNumeroNota()), rowSet.getInt("quantidadeCasasDecimaisPermitirAposVirgula")));
						} else {
							crosstab.setValorString("");
						}
					}
					crosstabVOs.add(crosstab);
				}
			}
		}
		return crosstabVOs;
	}

	public static String getCaminhoBaseRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator;
	}

	public static String getDesignIReportRelatorio(String tipoLayout) {
		if (tipoLayout.equals("analiticoPaisagem")) {
			return getCaminhoBaseRelatorio() + "MapaNotaPendenciaAlunoAnaliticoPaisagemRel.jrxml";
		} else if (tipoLayout.equals("analiticoRetrato")) {
			return getCaminhoBaseRelatorio() + "MapaNotaPendenciaAlunoAnaliticoRetratoRel.jrxml";
		}
		return getCaminhoBaseRelatorio() + "MapaNotaPendenciaAlunoRel.jrxml";
	}

	@Override
	public void validarDados(List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs, String ano, boolean apresentarCampoAno, String semestre, boolean apresentarCampoSemestre, String tipoAluno) throws Exception {
		if (apresentarCampoAno && !Uteis.isAtributoPreenchido(ano)) {
			throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_ano"));
		}
		if (apresentarCampoSemestre && !Uteis.isAtributoPreenchido(semestre)) {
			throw new Exception(UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_semestre"));
		}
		boolean selecionado = false;
		for (ConfiguracaoAcademicaNotaVO configuracaoAcademicaNotaVO : configuracaoAcademicaNotaVOs) {
			if (configuracaoAcademicaNotaVO.getUtilizarNotaSegundaChamada()) {
				selecionado = true;
				break;
			}
		}
		if (!selecionado) {
			if (tipoAluno.equals("recuperacao")) {
				throw new Exception(UteisJSF.internacionalizar("msg_MapaNotaPendenciaAlunoRel_notaRecuperacaoSelecionada"));
			}
			throw new Exception(UteisJSF.internacionalizar("msg_MapaNotaPendenciaAlunoRel_notaSegundaChamadaSelecionada"));
		}
	}

	/**
	 * Responsável por executar a montagem dos dados sintéticos
	 * 
	 * @author Wellington Rodrigues - 22 de jul de 2015
	 * @param unidadeEnsino
	 * @param periodicidade
	 * @param ano
	 * @param semestre
	 * @param curso
	 * @param turma
	 * @param disciplina
	 * @param situacaoAluno
	 * @param configuracaoAcademico
	 * @param situacaoNotaRecuperacao
	 * @param filtrarNota
	 * @param configuracaoAcademicaNotaVOs
	 * @param filtroRelatorioAcademicoVO
	 * @param usuarioVO
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<MapaNotaPendenciaAlunoTurmaRelVO> executarCriacaoMapaNotaPendenciaAlunoTurma(Integer unidadeEnsino, String periodicidade, String ano, String semestre, Integer curso, TurmaVO turma, Integer disciplina, String situacaoAluno, Integer configuracaoAcademico, String situacaoNotaRecuperacao, String filtrarNota, List<ConfiguracaoAcademicaNotaVO> configuracaoAcademicaNotaVOs, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select matricula.matricula as matriculaAluno, curso.nome as nomeCurso, identificadorTurma, turno.nome as nomeTurno, aluno.nome as nomeAluno,sem_acentos(aluno.nome) as nomeAlunoSemAcentos, disciplina.codigo as codigoDisciplina, disciplina.nome as nomeDisciplina ");
		sqlStr.append("from historico ");
		sqlStr.append("inner join matricula on matricula.matricula = historico.matricula ");
		sqlStr.append("inner join pessoa aluno on aluno.codigo = matricula.aluno ");
		sqlStr.append("inner join disciplina on disciplina.codigo = historico.disciplina ");
		sqlStr.append("inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append("inner join turno on turno.codigo = matricula.turno ");
		sqlStr.append("inner join unidadeensinocurso on unidadeensinocurso.curso = curso.codigo and unidadeensinocurso.turno = turno.codigo and unidadeensinocurso.unidadeensino = matricula.unidadeensino ");
		sqlStr.append("inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
		sqlStr.append("inner join configuracaoacademico on configuracaoacademico.codigo = historico.configuracaoAcademico ");
		sqlStr.append("left join gradedisciplina on gradedisciplina.codigo = historico.gradedisciplina ");
		sqlStr.append("left join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina ");
		sqlStr.append("left JOIN periodoletivo ON case when matriculaperiodo.codigo is not null then matriculaperiodo.periodoletivomatricula else case when historico.periodoletivocursada is not null then historico.periodoletivocursada else historico.periodoletivomatrizcurricular end end  = periodoletivo.codigo ");
		sqlStr.append("left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		sqlStr.append("left join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		adicionarFiltroClausulaWhere(sqlStr, unidadeEnsino, periodicidade, ano, semestre, curso, turma, disciplina, situacaoAluno, configuracaoAcademico, situacaoNotaRecuperacao, filtrarNota, configuracaoAcademicaNotaVOs, filtroRelatorioAcademicoVO);
		SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosMapaNotaPendenciaAlunoTurma(rowSet);
	}

	private List<MapaNotaPendenciaAlunoTurmaRelVO> montarDadosMapaNotaPendenciaAlunoTurma(SqlRowSet rowSet) throws Exception {
		List<MapaNotaPendenciaAlunoTurmaRelVO> mapaNotaPendenciaAlunoTurmaRelVOs = new ArrayList<MapaNotaPendenciaAlunoTurmaRelVO>(0);
		while (rowSet.next()) {
			boolean novoObj = true;
			for (MapaNotaPendenciaAlunoTurmaRelVO mapaNotaPendenciaAlunoTurmaRelVO : mapaNotaPendenciaAlunoTurmaRelVOs) {
				if (mapaNotaPendenciaAlunoTurmaRelVO.getIdentificadorTurma().equals(rowSet.getString("identificadorTurma"))) {
					for (MapaNotaPendenciaAlunoDisciplinaRelVO mapaNotaPendenciaAlunoDisciplinaRelVO : mapaNotaPendenciaAlunoTurmaRelVO.getMapaNotaPendenciaAlunoDisciplinaRelVOs()) {
						if (mapaNotaPendenciaAlunoDisciplinaRelVO.getCodigoDisciplina().equals(rowSet.getInt("codigoDisciplina"))) {
							mapaNotaPendenciaAlunoDisciplinaRelVO.setQtdeAlunoDisciplina(mapaNotaPendenciaAlunoDisciplinaRelVO.getQtdeAlunoDisciplina() + 1);
							novoObj = false;
							break;
						}
					}
					if (novoObj) {
						MapaNotaPendenciaAlunoDisciplinaRelVO mapaNotaPendenciaAlunoDisciplinaRelVO = new MapaNotaPendenciaAlunoDisciplinaRelVO();
						mapaNotaPendenciaAlunoDisciplinaRelVO.setCodigoDisciplina(rowSet.getInt("codigoDisciplina"));
						mapaNotaPendenciaAlunoDisciplinaRelVO.setDisciplina(rowSet.getString("nomeDisciplina"));
						mapaNotaPendenciaAlunoDisciplinaRelVO.setQtdeAlunoDisciplina(1);
						mapaNotaPendenciaAlunoTurmaRelVO.getMapaNotaPendenciaAlunoDisciplinaRelVOs().add(mapaNotaPendenciaAlunoDisciplinaRelVO);
					}
					novoObj = false;
					break;
				}
			}
			if (novoObj) {
				MapaNotaPendenciaAlunoTurmaRelVO mapaNotaPendenciaAlunoTurmaRelVO = new MapaNotaPendenciaAlunoTurmaRelVO();
				mapaNotaPendenciaAlunoTurmaRelVO.setIdentificadorTurma(rowSet.getString("identificadorTurma"));
				MapaNotaPendenciaAlunoDisciplinaRelVO mapaNotaPendenciaAlunoDisciplinaRelVO = new MapaNotaPendenciaAlunoDisciplinaRelVO();
				mapaNotaPendenciaAlunoDisciplinaRelVO.setCodigoDisciplina(rowSet.getInt("codigoDisciplina"));
				mapaNotaPendenciaAlunoDisciplinaRelVO.setDisciplina(rowSet.getString("nomeDisciplina"));
				mapaNotaPendenciaAlunoDisciplinaRelVO.setQtdeAlunoDisciplina(1);
				mapaNotaPendenciaAlunoTurmaRelVO.getMapaNotaPendenciaAlunoDisciplinaRelVOs().add(mapaNotaPendenciaAlunoDisciplinaRelVO);
				mapaNotaPendenciaAlunoTurmaRelVOs.add(mapaNotaPendenciaAlunoTurmaRelVO);
			}
		}
		return mapaNotaPendenciaAlunoTurmaRelVOs;
	}

}
