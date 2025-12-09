package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.MeritoAcademicoRelVO;
import relatorio.negocio.interfaces.academico.MeritoAcademicoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class MeritoAcademicoRel extends SuperRelatorio implements MeritoAcademicoRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public MeritoAcademicoRel() throws Exception { }

	@Override
	public void validarDados(TurmaVO turmaVO, UnidadeEnsinoCursoVO unidadeEnsinoCursoVO, UnidadeEnsinoVO unidadeEnsinoVO, boolean isFiltrarPorAno,
			boolean isFiltrarPorSemestre, String ano, String semestre, String campoFiltrarPor, Double primeiraNota, Double SegundaNota) throws Exception {

		if (!Uteis.isAtributoPreenchido(unidadeEnsinoVO)) {
			throw new ConsistirException("O campo UNIDADE ENSINO deve ser informado.");
		}
		if (campoFiltrarPor.equals("curso")) {
			if (!Uteis.isAtributoPreenchido(unidadeEnsinoCursoVO.getCurso())) {
				throw new ConsistirException("O campo CURSO deve ser informado.");
			}
		} else if (campoFiltrarPor.equals("turma")) {
			if (!Uteis.isAtributoPreenchido(turmaVO)) {
				throw new ConsistirException("O campo TURMA deve ser informado.");
			}
		}
		if (isFiltrarPorAno && !Uteis.isAtributoPreenchido(ano)) {
			throw new ConsistirException("O campo ANO deve ser informado.");
		}
		if (isFiltrarPorSemestre && !Uteis.isAtributoPreenchido(semestre)) {
			throw new ConsistirException("O campo SEMESTRE deve ser informado.");
		}

		if (Uteis.isAtributoPreenchido(primeiraNota) && Uteis.isAtributoPreenchido(SegundaNota)) {
			if (primeiraNota > SegundaNota) {
				throw new ConsistirException("O campo PRIMEIRA NOTA não deve ser maior que o campo SEGUNDA NOTA.");
			}
		}
	}

	@Override
	public List<MeritoAcademicoRelVO> criarObjeto(TurmaVO turma, Integer curso, Integer unidadeEnsino, String ano, String semestre,
			Integer gradeCurricular, Integer turno, Integer periodoLetivo, Integer disciplina, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO,
			boolean apresentarDisciplinaComposta, String tipoLayout, String tituloNota, String tipoAluno, boolean filtrarPorTurma,
			Double primeiraNota, Double SegundaNota , String rankingPor , boolean considerarNotasZeradas) throws Exception {
		List<MeritoAcademicoRelVO>  listaMerito = new ArrayList<MeritoAcademicoRelVO>(0);
		if (tipoLayout.equals("media")) {
			listaMerito = criarObjetoPorMedia(turma, curso, unidadeEnsino, ano, semestre, gradeCurricular, turno, periodoLetivo, disciplina,
					filtroRelatorioAcademicoVO, apresentarDisciplinaComposta, tipoAluno, filtrarPorTurma, primeiraNota, SegundaNota , rankingPor ,considerarNotasZeradas);  		
		
			
		} else {
			listaMerito = criarObjetoPorNota(turma, curso, unidadeEnsino, ano, semestre, gradeCurricular, turno, periodoLetivo, disciplina,
					filtroRelatorioAcademicoVO, apresentarDisciplinaComposta, tituloNota, tipoAluno, filtrarPorTurma, primeiraNota, SegundaNota , rankingPor ,considerarNotasZeradas);
		}		

		
		return listaMerito ;
	}

	public List<MeritoAcademicoRelVO> criarObjetoPorMedia(TurmaVO turma, Integer curso, Integer unidadeEnsino, String ano, String semestre, Integer gradeCurricular, Integer turno, Integer periodoLetivo, Integer disciplina, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, boolean apresentarDisciplinaComposta, String tipoAluno, boolean filtrarPorTurma, Double primeiraNota, Double SegundaNota ,String rankingPor , boolean considerarNotasZeradas) throws Exception {
		List<MeritoAcademicoRelVO> listaMeritoAcademicoRelVO = new ArrayList<MeritoAcademicoRelVO>(0);

		SqlRowSet dadosSQL = executarConsultaParametrizada(turma, curso, unidadeEnsino, ano, semestre, gradeCurricular, turno, periodoLetivo,
				disciplina, filtroRelatorioAcademicoVO, apresentarDisciplinaComposta, tipoAluno, filtrarPorTurma, primeiraNota, SegundaNota ,rankingPor ,considerarNotasZeradas);
		while (dadosSQL.next()) {
			listaMeritoAcademicoRelVO.add(montarDadosPorMedia(dadosSQL ,rankingPor  , disciplina));
		}
		return listaMeritoAcademicoRelVO;
	}

	public SqlRowSet executarConsultaParametrizada(TurmaVO turma, Integer curso, Integer unidadeEnsino, String ano, String semestre, Integer gradeCurricular, Integer turno, Integer periodoLetivo, Integer disciplina, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, boolean apresentarDisciplinaComposta, String tipoAluno, boolean filtrarPorTurma, Double primeiraNota, Double segundaNota ,String rankingPor , boolean considerarNotasZeradas) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		if(!rankingPor.equals("disciplina")) {
			sqlStr.append("select unidadeEnsino.nome as nomeunidadeensino ,	curso.nome as nomeCurso, 	pessoa.nome as nomeAluno  ,	matricula.matricula as matriculaAluno , ");
			if (Uteis.isAtributoPreenchido(disciplina)) {
				sqlStr.append(" disciplina.nome as disciplinaHistorico,");
			}
			if(!rankingPor.equals("curso")) {
				sqlStr.append("turma.identificadorturma as identificadorturma,");
			}
			
			sqlStr.append(" count (*) as quantidadeNotas , (sum(mediafinal) /count (*))  as mediaGlobal  ,  periodoletivo.descricao as periodoletivoHistorico , turno.nome as nomeTurno   ");
		}else {
			sqlStr.append(" select unidadeEnsino.nome as nomeunidadeensino, turno.nome as nomeTurno, curso.nome as nomeCurso, 	historico.matricula as matriculaAluno, 	pessoa.nome as nomeAluno ,   ");	
			sqlStr.append(" configuracaoAcademico.pesoMediaNotaMeritoAcademico, 	configuracaoAcademico.pesoMediaFrequenciaMeritoAcademico , periodoletivo.descricao as periodoletivoHistorico, 	disciplina.nome as disciplinaHistorico, turma.identificadorturma, ");
			sqlStr.append(" ((pesoMediaNotaMeritoAcademico + (sum(mediaFinal)/ count(historico.codigo))) + (pesoMediaFrequenciaMeritoAcademico * (sum(freguencia)/ count(historico.codigo)))) as mediaGlobal , quantidadeCasasDecimaisPermitirAposVirgula ");
				
		}	
		
		sqlStr.append(" from historico ");
		sqlStr.append(" inner join matricula on matricula.matricula = historico.matricula ");
		sqlStr.append(" inner join turno on turno.codigo = matricula.turno ");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and historico.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append("inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		if (tipoAluno.equals("normal")) {
			sqlStr.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		} else {
			if (Uteis.isAtributoPreenchido(turma) && turma.getSubturma()) {
				if (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
					sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaTeorica ");
				} else if (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
					sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaPratica ");
				} else {
					sqlStr.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
				}
			} else {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = matriculaPeriodoTurmaDisciplina.turma ");
			}
		}
		sqlStr.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
		sqlStr.append(" inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sqlStr.append(" left join configuracaoacademico on configuracaoacademico.codigo = historico.configuracaoacademico ");
		if (tipoAluno.equals("reposicao")) {
			sqlStr.append("WHERE matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma ");
		} else {
			sqlStr.append(" where 1 = 1 ");
		}
		
		if(!considerarNotasZeradas) {
			sqlStr.append(" and mediaFinal  is not null" ); 
		}
		
		if (Uteis.isAtributoPreenchido(primeiraNota)) {
			sqlStr.append(" and mediaFinal >= " + primeiraNota);
		}
		
		if (Uteis.isAtributoPreenchido(segundaNota)) {
			sqlStr.append(" and mediaFinal < " + segundaNota);
		}

		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and (matricula.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
	
		if (Uteis.isAtributoPreenchido(turno)) {
			sqlStr.append(" and (matricula.turno = ").append(turno).append(") ");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and (matricula.curso = ").append(curso).append(") ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and (matriculaperiodo.semestre = '").append(semestre).append("')");
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and (matriculaperiodo.ano = '").append(ano).append("')");
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			if (turma.getTurmaAgrupada() && !turma.getSubturma()) {
				sqlStr.append(" and ((turma.codigo = ").append(turma.getCodigo()).append(" or turma.codigo in (select turma from turmaAgrupada where turmaOrigem =  ").append(turma.getCodigo()).append("))");
				sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turma.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
				sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turma.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
				sqlStr.append(") ");
			} else {
				sqlStr.append(" AND turma.codigo = ").append(turma.getCodigo());
			}
			if (!turma.getSubturma() && !turma.getTurmaAgrupada()) {
				sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
			}
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and (disciplina.codigo = ").append(disciplina).append(") ");
		}
		if (Uteis.isAtributoPreenchido(periodoLetivo) && !filtrarPorTurma) {
			sqlStr.append(" and (periodoletivo.codigo = ").append(periodoLetivo).append(") ");
		}
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));		
		if (!Uteis.isAtributoPreenchido(disciplina)) {
			if (apresentarDisciplinaComposta) {
				sqlStr.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false)");
			} else {
				sqlStr.append(" and (historico.historicoDisciplinaFazParteComposicao is null or historico.historicoDisciplinaFazParteComposicao = false)");
			}
		}
		sqlStr.append(" and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null)");
		sqlStr.append(" and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false)");
	
		if(!rankingPor.equals("disciplina")) {
			
			sqlStr.append(" group by  nomeunidadeensino , nomeAluno , matriculaAluno ,nomeCurso , periodoletivoHistorico , nomeTurno    ");
			if(!rankingPor.equals("curso")) {
				sqlStr.append(" , identificadorturma  ");
			}
			if (Uteis.isAtributoPreenchido(disciplina)) {
				sqlStr.append(" , disciplinaHistorico");
			}
			if(!rankingPor.equals("curso")) {
				sqlStr.append(" order by   identificadorturma , mediaGlobal desc nulls last  , nomeAluno   ");
			}else {					
			   sqlStr.append(" order by    mediaGlobal desc nulls last   , nomeAluno  ");
			}
		}else {

			sqlStr.append(" group by nomeunidadeensino , nomeTurno, nomeCurso, matriculaAluno , nomeAluno , pesoMediaNotaMeritoAcademico, pesoMediaFrequenciaMeritoAcademico, periodoletivoHistorico , disciplinaHistorico, identificadorturma, quantidadeCasasDecimaisPermitirAposVirgula ");
			sqlStr.append(" order by identificadorturma, disciplinaHistorico, mediaGlobal desc nulls last, nomeAluno");
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado;
	}

	public MeritoAcademicoRelVO montarDadosPorMedia(SqlRowSet dadosSQL ,String rankingPor , Integer disciplina ) throws Exception {
		MeritoAcademicoRelVO meritoAcademicoRelVO = new MeritoAcademicoRelVO();
		meritoAcademicoRelVO.getMatriculaVO().setMatricula(dadosSQL.getString("matriculaAluno"));
		meritoAcademicoRelVO.getMatriculaVO().getAluno().setNome(dadosSQL.getString("nomeAluno"));	
		meritoAcademicoRelVO.getMatriculaVO().getCurso().setNome(dadosSQL.getString("nomeCurso"));
		
		meritoAcademicoRelVO.getMatriculaVO().getUnidadeEnsino().setNome(dadosSQL.getString("nomeunidadeensino"));
		if (dadosSQL.getObject("mediaGlobal") != null) {
			meritoAcademicoRelVO.setMediaFinalCurso(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(dadosSQL.getDouble("mediaGlobal"),  rankingPor.equals("disciplina") ? dadosSQL.getInt("quantidadeCasasDecimaisPermitirAposVirgula") : 1 ));
			meritoAcademicoRelVO.setMediaFinalCursoStr(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(dadosSQL.getDouble("mediaGlobal"),     rankingPor.equals("disciplina") ? dadosSQL.getInt("quantidadeCasasDecimaisPermitirAposVirgula") : 1));
		}
		if(!rankingPor.equals("curso")) {
			meritoAcademicoRelVO.setIdentificadorTurma(dadosSQL.getString("identificadorturma"));
			meritoAcademicoRelVO.setPeriodoLetivo(dadosSQL.getString("periodoletivoHistorico"));
			meritoAcademicoRelVO.getMatriculaVO().getTurno().setNome(dadosSQL.getString("nomeTurno"));
		}
		
		
		if (Uteis.isAtributoPreenchido(disciplina) || rankingPor.equals("disciplina")) {
			meritoAcademicoRelVO.setDisciplina(dadosSQL.getString("disciplinaHistorico"));
		}
		
		if(!rankingPor.equals("disciplina")) {			
		    meritoAcademicoRelVO.setQuantidadeNotas(dadosSQL.getInt("quantidadeNotas"));
		}
		
		
		return meritoAcademicoRelVO;
	}

	public List<MeritoAcademicoRelVO> criarObjetoPorNota(TurmaVO turma, Integer curso, Integer unidadeEnsino, String ano, String semestre, Integer gradeCurricular, Integer turno, Integer periodoLetivo, Integer disciplina, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, boolean apresentarDisciplinaComposta, String tituloNota, String tipoAluno, boolean filtrarPorTurma, Double primeiraNota, Double SegundaNota ,String rankingPor ,boolean considerarNotasZeradas) throws Exception {
		List<MeritoAcademicoRelVO> listaMeritoAcademicoRelVO = new ArrayList<MeritoAcademicoRelVO>(0);

		SqlRowSet dadosSQL = executarConsultaParametrizadaPorNota(turma, curso, unidadeEnsino, ano, semestre, gradeCurricular, turno,
				periodoLetivo, disciplina, filtroRelatorioAcademicoVO, apresentarDisciplinaComposta, tituloNota, tipoAluno, 
				filtrarPorTurma, primeiraNota, SegundaNota ,rankingPor ,considerarNotasZeradas);
		while (dadosSQL.next()) {
			listaMeritoAcademicoRelVO.add(montarDadosPorNota(dadosSQL, tituloNota, primeiraNota, SegundaNota , rankingPor ));
		}
		return listaMeritoAcademicoRelVO;
	}

	public MeritoAcademicoRelVO montarDadosPorNota(SqlRowSet dadosSQL, String tituloNota, Double primeiraNota, Double segundaNota ,String  rankingPor ) throws Exception {
		MeritoAcademicoRelVO meritoAcademicoRelVO = new MeritoAcademicoRelVO();
		meritoAcademicoRelVO.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
		meritoAcademicoRelVO.getMatriculaVO().getAluno().setNome(dadosSQL.getString("nomealuno"));
		meritoAcademicoRelVO.getMatriculaVO().getCurso().setNome(dadosSQL.getString("nomecurso"));
		meritoAcademicoRelVO.getMatriculaVO().getTurno().setNome(dadosSQL.getString("nometurno"));
		meritoAcademicoRelVO.getMatriculaVO().getUnidadeEnsino().setNome(dadosSQL.getString("nomeunidadeensino"));
		// meritoAcademicoRelVO.setMediaFinalCurso(dadosSQL.getDouble("mediaGlobal"));
		meritoAcademicoRelVO.setIdentificadorTurma(dadosSQL.getString("identificadorturma"));
		meritoAcademicoRelVO.setDisciplina(dadosSQL.getString("disciplina"));
		meritoAcademicoRelVO.setPeriodoLetivo(dadosSQL.getString("periodoletivo"));
		for (int i = 1; i <= 30; i++) {
			if (tituloNota.equals("nota" + i)) {
				if (dadosSQL.getObject("nota" + i) != null) {
					if (dadosSQL.getObject("nota" + i) != null) {
						meritoAcademicoRelVO.setNota(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(dadosSQL.getDouble("nota" + i), dadosSQL.getInt("quantidadeCasasDecimaisPermitirAposVirgula")));
						meritoAcademicoRelVO.setNotaStr(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(dadosSQL.getDouble("nota" + i), dadosSQL.getInt("quantidadeCasasDecimaisPermitirAposVirgula")));
					}
					meritoAcademicoRelVO.setNotaConceito(dadosSQL.getString("conceitonota" + i));
				}
				
				if ( dadosSQL.getObject("nota" + i) != null) {
					if (dadosSQL.getObject("nota" + i) != null) {
						meritoAcademicoRelVO.setNota(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(dadosSQL.getDouble("nota" + i), dadosSQL.getInt("quantidadeCasasDecimaisPermitirAposVirgula")));
						meritoAcademicoRelVO.setNotaStr(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(dadosSQL.getDouble("nota" + i), dadosSQL.getInt("quantidadeCasasDecimaisPermitirAposVirgula")));
					}
					meritoAcademicoRelVO.setNotaConceito(dadosSQL.getString("conceitonota" + i));
				}

				break;
			} else {
				if (dadosSQL.getObject("mediafinal") != null) {
					meritoAcademicoRelVO.setNota(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(dadosSQL.getDouble("mediafinal"), dadosSQL.getInt("quantidadeCasasDecimaisPermitirAposVirgula")));
					meritoAcademicoRelVO.setNotaStr(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula(dadosSQL.getDouble("mediafinal"), dadosSQL.getInt("quantidadeCasasDecimaisPermitirAposVirgula")));
				}
				meritoAcademicoRelVO.setNotaConceito(dadosSQL.getString("mediafinalconceito"));
			}
		}
		return meritoAcademicoRelVO;
	}

	public SqlRowSet executarConsultaParametrizadaPorNota(TurmaVO turma, Integer curso, Integer unidadeEnsino, String ano, String semestre, Integer gradeCurricular, Integer turno, Integer periodoLetivo, Integer disciplina, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, boolean apresentarDisciplinaComposta, String tituloNota, String tipoAluno, boolean filtrarPorTurma, Double primeiraNota, Double segundaNota ,String rankingPor , boolean considerarNotasZeradas) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" select unidadeEnsino.nome AS nomeunidadeensino, turno.nome AS nomeTurno, curso.nome AS nomeCurso,  historico.matricula, pessoa.nome AS nomeAluno, historico.mediaFinal, historico.codigo, ");
		sqlStr.append(" historico.freguencia, periodoletivo.descricao as periodoletivo, disciplina.nome as disciplina, turma.identificadorturma, historico.nota1, historico.nota2, ");
		sqlStr.append(" historico.nota3, historico.nota4, historico.nota5, historico.nota6, historico.nota7, historico.nota8, historico.nota9, historico.nota10, ");
		sqlStr.append(" historico.nota11, historico.nota12, historico.nota13, historico.nota14, historico.nota15, historico.nota16, historico.nota17, historico.nota18, ");
		sqlStr.append(" historico.nota19, historico.nota20, historico.nota21, historico.nota22, historico.nota23, historico.nota24, historico.nota25, historico.nota26, ");
		sqlStr.append(" historico.nota27, historico.nota28, historico.nota29, historico.nota30, historico.mediafinal as \"mediafinal\", ");
		sqlStr.append(" conceitoNota1.abreviaturaconceitonota as \"conceitonota1\", conceitoNota2.abreviaturaconceitonota as \"conceitonota2\",  ");
		sqlStr.append(" conceitoNota3.abreviaturaconceitonota as \"conceitonota3\", conceitoNota4.abreviaturaconceitonota as \"conceitonota4\", conceitoNota5.abreviaturaconceitonota as \"conceitonota5\", ");
		sqlStr.append(" conceitoNota6.abreviaturaconceitonota as \"conceitonota6\", conceitoNota7.abreviaturaconceitonota as \"conceitonota7\", conceitoNota8.abreviaturaconceitonota as \"conceitonota8\", ");
		sqlStr.append(" conceitoNota9.abreviaturaconceitonota as \"conceitonota9\", ");
		sqlStr.append(" conceitoNota10.abreviaturaconceitonota as \"conceitonota10\", conceitoNota11.abreviaturaconceitonota as \"conceitonota11\", conceitoNota12.abreviaturaconceitonota as \"conceitonota12\", ");
		sqlStr.append(" conceitoNota13.abreviaturaconceitonota as \"conceitonota13\", conceitoNota14.abreviaturaconceitonota as \"conceitonota14\", conceitoNota15.abreviaturaconceitonota as \"conceitonota15\", ");
		sqlStr.append(" conceitoNota16.abreviaturaconceitonota as \"conceitonota16\", conceitoNota17.abreviaturaconceitonota as \"conceitonota17\", conceitoNota18.abreviaturaconceitonota as \"conceitonota18\", ");
		sqlStr.append(" conceitoNota19.abreviaturaconceitonota as \"conceitonota22\", conceitoNota23.abreviaturaconceitonota as \"conceitonota23\", conceitoNota24.abreviaturaconceitonota as \"conceitonota24\", ");
		sqlStr.append(" conceitoNota25.abreviaturaconceitonota as \"conceitonota25\", conceitoNota26.abreviaturaconceitonota as \"conceitonota26\", conceitoNota27.abreviaturaconceitonota as \"conceitonota27\", ");
		sqlStr.append(" conceitoNota28.abreviaturaconceitonota as \"conceitonota28\", conceitoNota29.abreviaturaconceitonota as \"conceitonota29\", conceitoNota30.abreviaturaconceitonota as \"conceitonota30\", historico.mediafinalconceito, ");
		sqlStr.append("quantidadeCasasDecimaisPermitirAposVirgula ");
		sqlStr.append(" from historico ");
		sqlStr.append(" inner join matricula on matricula.matricula = historico.matricula ");
		sqlStr.append(" inner join turno on turno.codigo = matricula.turno ");
		sqlStr.append(" inner join curso on curso.codigo = matricula.curso ");
		sqlStr.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula and historico.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append("inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		if (tipoAluno.equals("normal")) {
			sqlStr.append(" inner join turma on turma.codigo = matriculaperiodo.turma ");
		} else {
			if (Uteis.isAtributoPreenchido(turma) && turma.getSubturma()) {
				if (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
					sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaTeorica ");
				} else if (turma.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
					sqlStr.append("INNER JOIN Turma ON turma.codigo = MatriculaPeriodoTurmaDisciplina.turmaPratica ");
				} else {
					sqlStr.append("INNER JOIN Turma ON MatriculaPeriodoTurmaDisciplina.turma = turma.codigo and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
				}
			} else {
				sqlStr.append("INNER JOIN Turma ON turma.codigo = matriculaPeriodoTurmaDisciplina.turma ");
			}
		}
		sqlStr.append(" inner join disciplina on disciplina.codigo = historico.disciplina ");
		sqlStr.append(" inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
		sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sqlStr.append(" left join configuracaoacademico on configuracaoacademico.codigo = historico.configuracaoacademico ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota1 on conceitoNota1.codigo = historico.nota1conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota2 on conceitoNota2.codigo = historico.nota2conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota3 on conceitoNota3.codigo = historico.nota3conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota4 on conceitoNota4.codigo = historico.nota4conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota5 on conceitoNota5.codigo = historico.nota5conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota6 on conceitoNota6.codigo = historico.nota6conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota7 on conceitoNota7.codigo = historico.nota7conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota8 on conceitoNota8.codigo = historico.nota8conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota9 on conceitoNota9.codigo = historico.nota9conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota10 on conceitoNota10.codigo = historico.nota10conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota11 on conceitoNota11.codigo = historico.nota11conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota12 on conceitoNota12.codigo = historico.nota12conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota13 on conceitoNota13.codigo = historico.nota13conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota14 on conceitoNota14.codigo = historico.nota14conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota15 on conceitoNota15.codigo = historico.nota15conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota16 on conceitoNota16.codigo = historico.nota16conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota17 on conceitoNota17.codigo = historico.nota17conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota18 on conceitoNota18.codigo = historico.nota18conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota19 on conceitoNota19.codigo = historico.nota19conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota20 on conceitoNota20.codigo = historico.nota20conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota21 on conceitoNota21.codigo = historico.nota21conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota22 on conceitoNota22.codigo = historico.nota22conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota23 on conceitoNota23.codigo = historico.nota23conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota24 on conceitoNota24.codigo = historico.nota24conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota25 on conceitoNota25.codigo = historico.nota25conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota26 on conceitoNota26.codigo = historico.nota26conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota27 on conceitoNota27.codigo = historico.nota27conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota28 on conceitoNota28.codigo = historico.nota28conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota29 on conceitoNota29.codigo = historico.nota29conceito ");
		sqlStr.append(" left join configuracaoacademiconotaconceito as conceitoNota30 on conceitoNota30.codigo = historico.nota30conceito ");
		if (tipoAluno.equals("reposicao")) {
			sqlStr.append("WHERE matriculaperiodoturmadisciplina.turma <> matriculaperiodo.turma ");
		} else {
			sqlStr.append(" where 1 = 1 ");
		}
		if(!considerarNotasZeradas) {
			sqlStr.append(" and historico." + tituloNota + " != 0.0" ); 
		}
		
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and (matricula.unidadeensino = ").append(unidadeEnsino).append(") ");
		}
		
		if (Uteis.isAtributoPreenchido(primeiraNota)) {
			sqlStr.append(" and historico." + tituloNota + ">= " + primeiraNota);
		}
		
		if (Uteis.isAtributoPreenchido(segundaNota)) {
			sqlStr.append(" and historico."+ tituloNota + "<= " + segundaNota);
		}

//		if (Uteis.isAtributoPreenchido(gradeCurricular)) {
//			sqlStr.append(" and (matricula.gradecurricularatual = ").append(gradeCurricular).append(") ");
//		}
		if (Uteis.isAtributoPreenchido(turno)) {
			sqlStr.append(" and (matricula.turno = ").append(turno).append(") ");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append(" and (matricula.curso = ").append(curso).append(") ");
		}
		if (Uteis.isAtributoPreenchido(semestre)) {
			sqlStr.append(" and (matriculaperiodo.semestre = '").append(semestre).append("')");
		}
		if (Uteis.isAtributoPreenchido(ano)) {
			sqlStr.append(" and (matriculaperiodo.ano = '").append(ano).append("')");
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			if (turma.getTurmaAgrupada() && !turma.getSubturma()) {
				sqlStr.append(" and ((turma.codigo = ").append(turma.getCodigo()).append(" or turma.codigo in (select turma from turmaAgrupada where turmaOrigem =  ").append(turma.getCodigo()).append("))");
				sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turma.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
				sqlStr.append("or (MatriculaPeriodoTurmaDisciplina.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turma.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
				sqlStr.append(") ");
			} else {
				sqlStr.append(" AND turma.codigo = ").append(turma.getCodigo());
			}
			if (!turma.getSubturma() && !turma.getTurmaAgrupada()) {
				sqlStr.append(" and MatriculaPeriodoTurmaDisciplina.turmaPratica is null and MatriculaPeriodoTurmaDisciplina.turmaTeorica is null ");
			}
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append(" and (disciplina.codigo = ").append(disciplina).append(") ");
		}
		if (Uteis.isAtributoPreenchido(periodoLetivo) && !filtrarPorTurma) {
			sqlStr.append(" and (periodoletivo.codigo = ").append(periodoLetivo).append(") ");
		}
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		/**
		 * Adicionada regra para resolver impactos relacionados a alunos que
		 * estão Cursando por Correspondência e que disciplinas saiam duplicadas
		 * no Boletim Acadêmico
		 */
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));		
		if (!Uteis.isAtributoPreenchido(disciplina)) {
			if (apresentarDisciplinaComposta) {
				sqlStr.append(" and (historico.historicoDisciplinaComposta is null or historico.historicoDisciplinaComposta = false)");
			} else {
				sqlStr.append(" and (historico.historicoDisciplinaFazParteComposicao is null or historico.historicoDisciplinaFazParteComposicao = false)");
			}
		}
		sqlStr.append(" and (historico.gradedisciplina is not null or historico.gradeCurricularGrupoOptativaDisciplina is not null or historico.historicoDisciplinaForaGrade = true or historico.gradedisciplinacomposta is not null)");
		sqlStr.append(" and (historico.historicoporequivalencia is null or historico.historicoporequivalencia = false)");
		if (tituloNota != null && !tituloNota.trim().isEmpty()) {
			// sqlStr.append(" and historico." + tituloNota + " is not null");
			sqlStr.append(" order by  historico." + tituloNota + " desc nulls last, pessoa.nome desc ");
		} else {
			// sqlStr.append(" and mediaFinal is not null");
			sqlStr.append(" order by turma.identificadorturma, disciplina.nome, mediaFinal desc nulls last, pessoa.nome desc");
		}
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		//System.out.println(sqlStr.toString());
		return tabelaResultado;
	}

	public static String getDesignIReportRelatorio() throws Exception {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("MeritoAcademicoRel");
	}

	public static String getDesignIReportRelatorioPorNota() throws Exception {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadePoNota() + ".jrxml");
	}

	public static String getIdEntidadePoNota() {
		return ("MeritoAcademicoPorNotaRel");
	}

	
	

}
