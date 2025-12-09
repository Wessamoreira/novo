package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.ConfiguracaoAcademicoNotaConceitoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.negocio.comuns.academico.FrequenciaAlunoMesDiaRelVO;
import relatorio.negocio.comuns.academico.FrequenciaAlunoRelVO;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;
import relatorio.negocio.interfaces.academico.FrequenciaAlunoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
public class FrequenciaAlunoRel extends SuperRelatorio implements FrequenciaAlunoRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	@Override
	public void validarDados(UnidadeEnsinoVO unidadeEnsinoVO, MatriculaVO matriculaVO, String ano, String semestre, CursoVO cursoVO, TurmaVO turmaVO) throws Exception {
		if (!Uteis.isAtributoPreenchido(unidadeEnsinoVO)) {
			throw new Exception(UteisJSF.internacionalizar("msg_InadimplenciaInstituicao_unidadeEnsino"));
		}
		if (!Uteis.isAtributoPreenchido(matriculaVO.getMatricula()) && !Uteis.isAtributoPreenchido(cursoVO) && !Uteis.isAtributoPreenchido(turmaVO)) {
			throw new Exception(UteisJSF.internacionalizar("msg_FrequenciaAlunoRelaotiro_dadosObrigatorios"));
		}
		if (!Uteis.isAtributoPreenchido(ano) && (matriculaVO.getCurso().getPeriodicidade().equals("AN") || matriculaVO.getCurso().getPeriodicidade().equals("SE"))) {
			throw new Exception(UteisJSF.internacionalizar("msg_CriterioAvaliacaoAluno_ano"));
		}
		if (!Uteis.isAtributoPreenchido(semestre) && matriculaVO.getCurso().getPeriodicidade().equals("SE")) {
			throw new Exception(UteisJSF.internacionalizar("msg_CriterioAvaliacaoAluno_semestre"));
		}
	}

	@Override
	public List<FrequenciaAlunoRelVO> executarCriacaoObjeto(MatriculaVO matriculaVO, DisciplinaVO disciplinaVO, String ano, String semestre, Date dataInicio,
			Date dataFim, boolean trazerAlunoTransferencia, UsuarioVO usuarioVO, CursoVO cursoVO, TurmaVO turmaVO, Integer quantidadeMinimaFaltas,
			Integer percentualMinimaFaltas, boolean considerarFaltaAulasNaoRegistradas) throws Exception {
		String slqStr = getSqlPadraoConsulta(matriculaVO, disciplinaVO, ano, semestre, dataInicio, dataFim, trazerAlunoTransferencia, usuarioVO, considerarFaltaAulasNaoRegistradas, cursoVO, turmaVO, quantidadeMinimaFaltas, percentualMinimaFaltas, 
				" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null ",
				" inner join turma on  matriculaperiodoturmadisciplina.turmapratica is not null and turma.codigo = matriculaperiodoturmadisciplina.turmapratica ",
				" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turmateorica and matriculaperiodoturmadisciplina.turmateorica is not null ",
				" inner join turmaagrupada on  matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null and turmaagrupada.turma =  matriculaperiodoturmadisciplina.turma inner join turma on turma.codigo = turmaagrupada.turmaorigem ");
		SqlRowSet rowSet = getConexao().getJdbcTemplate().queryForRowSet(slqStr);
		return executarMontagemDados(rowSet, usuarioVO, quantidadeMinimaFaltas, percentualMinimaFaltas);
	}

	private List<FrequenciaAlunoRelVO> executarMontagemDados(SqlRowSet rowSet, UsuarioVO usuarioVO, Integer quantidadeMinimaFaltas, Integer percentualMinimaFaltas) throws Exception {
		Map<Integer, FrequenciaAlunoRelVO> frequencias = new HashMap<Integer, FrequenciaAlunoRelVO>(0);
		while (rowSet.next()) {
			if (frequencias.containsKey(rowSet.getInt("disciplina_codigo"))) {
				montarDadosFrequenciaAlunoExistenteRel(rowSet, frequencias, quantidadeMinimaFaltas, percentualMinimaFaltas);
			} else {
				montarDadosFrequenciaAlunoRel(rowSet, frequencias, usuarioVO, quantidadeMinimaFaltas, percentualMinimaFaltas);
			}
		}
		List<FrequenciaAlunoRelVO> frenciaVOs = new ArrayList<FrequenciaAlunoRelVO>(frequencias.values());
		Ordenacao.ordenarLista(frenciaVOs, "nomeDisciplina");
		return frenciaVOs;
	}

	private void montarDadosFrequenciaAlunoExistenteRel(SqlRowSet rowSet, Map<Integer, FrequenciaAlunoRelVO> frequencias, Integer quantidadeMinimaFaltas, Integer percentualMinimaFaltas) throws Exception {
		boolean existeMes = false;
		FrequenciaAlunoRelVO obj = frequencias.get(rowSet.getInt("disciplina_codigo"));
		for (FrequenciaAlunoMesDiaRelVO diaRelVO : obj.getFrequenciaAlunoMesDiaRelVOs()) {
			if (diaRelVO.getOrdemMes().equals(rowSet.getInt("mes"))) {
				existeMes = true;
				for (int i = 1; i <= 31; i++) {
					if (rowSet.getInt("dia") == i) {
						if (rowSet.getInt("presenca") != 0 || rowSet.getInt("abono") != 0) {
							diaRelVO.getDiaPresenca().set(i - 1, rowSet.getInt("presenca") + rowSet.getInt("abono"));
						}
						diaRelVO.setPresencas(diaRelVO.getPresencas() + rowSet.getInt("presenca"));
						if (rowSet.getInt("falta") != 0) {
							diaRelVO.getDiaFalta().set(i - 1, rowSet.getInt("falta"));
						}
						diaRelVO.setFaltas(diaRelVO.getFaltas() + rowSet.getInt("falta"));
						diaRelVO.setAbonos(diaRelVO.getAbonos() + rowSet.getInt("abono"));
					}

					calcularPercentual(diaRelVO, i, quantidadeMinimaFaltas, percentualMinimaFaltas);
				}
			}
		}

		if (!existeMes) {
			obj.getFrequenciaAlunoMesDiaRelVOs().add(executarMontagemDadosFrequenciaAlunoMesDia(rowSet));
			for (FrequenciaAlunoMesDiaRelVO diaRelVO : obj.getFrequenciaAlunoMesDiaRelVOs()) {
				if (diaRelVO.getOrdemMes().equals(rowSet.getInt("mes"))) {
					for (int i = 1; i <= 31; i++) {
						if (rowSet.getInt("dia") == i) {
							if (rowSet.getInt("presenca") != 0 || rowSet.getInt("abono") != 0) {
								diaRelVO.getDiaPresenca().set(i - 1, rowSet.getInt("presenca") + rowSet.getInt("abono"));
							}
							diaRelVO.setPresencas(diaRelVO.getPresencas() + rowSet.getInt("presenca"));
							if (rowSet.getInt("falta") != 0) {
								diaRelVO.getDiaFalta().set(i - 1, rowSet.getInt("falta"));
							}
							diaRelVO.setFaltas(diaRelVO.getFaltas() + rowSet.getInt("falta"));
							diaRelVO.setAbonos(diaRelVO.getAbonos() + rowSet.getInt("abono"));
						}
						calcularPercentual(diaRelVO, i, quantidadeMinimaFaltas, percentualMinimaFaltas);
					}
				}
			}
		}

	}

	private void calcularPercentual(FrequenciaAlunoMesDiaRelVO diaRelVO, int i, Integer quantidadeMinimaFaltas, Integer percentualMinimaFaltas) {
		int quantidadeDiasMes = 31;
		if (i == quantidadeDiasMes) {
			if (Uteis.isAtributoPreenchido(diaRelVO.getPresencas()) && diaRelVO.getFaltas() > 0) {
				diaRelVO.setPorcentagemFaltas((diaRelVO.getFaltas() * 100) / diaRelVO.getPresencas());
			} else {
				diaRelVO.setPorcentagemFaltas(0);
			}
		}
	}

	private void montarDadosFrequenciaAlunoRel(SqlRowSet rowSet, Map<Integer, FrequenciaAlunoRelVO> frequencias, UsuarioVO usuarioVO, Integer quantidadeMinimaFaltas, Integer percentualMinimaFaltas) throws Exception {
		FrequenciaAlunoRelVO obj = new FrequenciaAlunoRelVO();
		obj.setNomeDisciplina(rowSet.getString("disciplina_nome"));
		obj.setPeriodoLetivo(rowSet.getString("periodoletivo_descricao"));
		obj.setCargaHoraria(rowSet.getInt("cargaHoraria"));
		obj.setNumeroAulas(rowSet.getInt("horaaula"));
		obj.setMatricula(rowSet.getString("matricula"));
		obj.setNomeAluno(rowSet.getString("aluno_nome"));
		obj.setAno(rowSet.getString("matriculaperiodoturmadisciplina_ano"));
		obj.setSemestre(rowSet.getString("matriculaperiodoturmadisciplina_semestre"));
		obj.getFrequenciaAlunoMesDiaRelVOs().add(executarMontagemDadosFrequenciaAlunoMesDia(rowSet));
		for (FrequenciaAlunoMesDiaRelVO diaRelVO : obj.getFrequenciaAlunoMesDiaRelVOs()) {
			if (diaRelVO.getOrdemMes().equals(rowSet.getInt("mes"))) {
				for (int i = 1; i <= 31; i++) {
					if (rowSet.getInt("dia") == i) {
						if (rowSet.getInt("presenca") != 0 || rowSet.getInt("abono") != 0) {
							diaRelVO.getDiaPresenca().set(i - 1, rowSet.getInt("presenca") + rowSet.getInt("abono"));
						}
						diaRelVO.setPresencas(diaRelVO.getPresencas() + rowSet.getInt("presenca"));
						if (rowSet.getInt("falta") != 0) {
							diaRelVO.getDiaFalta().set(i - 1, rowSet.getInt("falta"));
						}
						diaRelVO.setFaltas(diaRelVO.getFaltas() + rowSet.getInt("falta"));
						diaRelVO.setAbonos(diaRelVO.getAbonos() + rowSet.getInt("abono"));
						calcularPercentual(diaRelVO, i, quantidadeMinimaFaltas, percentualMinimaFaltas);
					}
				}
			}
		} 
		if (rowSet.getInt("historico_codigo") != 0) {
			HistoricoVO historicoVO = new HistoricoVO();
			historicoVO.setCodigo(rowSet.getInt("historico_codigo"));
			getFacadeFactory().getHistoricoFacade().carregarDados(historicoVO, NivelMontarDados.TODOS, usuarioVO);
			obj.getCrosstabVOs().addAll(montarDadosConfiguracaoAcademicoCrosstab(historicoVO, 0, 0));
		}

		frequencias.put(rowSet.getInt("disciplina_codigo"), obj);
	}

	private FrequenciaAlunoMesDiaRelVO executarMontagemDadosFrequenciaAlunoMesDia(SqlRowSet rowSet) throws Exception {
		FrequenciaAlunoMesDiaRelVO obj = new FrequenciaAlunoMesDiaRelVO();
		obj.setOrdemMes(rowSet.getInt("mes"));
		obj.setMes(Uteis.getMesReferenciaExtenso(String.valueOf(rowSet.getInt("mes"))));
		for (int i = 0; i <= 30; i++) {
			obj.getDiaFalta().add(null);
			obj.getDiaPresenca().add(null);
		}
		return obj;
	}

	private List<CrosstabVO> montarDadosConfiguracaoAcademicoCrosstab(HistoricoVO historicoVO, int ordemColuna, int ordemLinha) throws Exception {
		List<CrosstabVO> crosstabVOs = new ArrayList<CrosstabVO>(0);
		for (int x = 1; x <= 30; x++) {
			boolean utilizarNota = (Boolean) UtilReflexao.invocarMetodoGet(historicoVO.getConfiguracaoAcademico(), "utilizarNota" + x);
			boolean apresentarNota = (Boolean) UtilReflexao.invocarMetodoGet(historicoVO.getConfiguracaoAcademico(), "apresentarNota" + x);
			boolean notaMediaFinal = (Boolean) UtilReflexao.invocarMetodoGet(historicoVO.getConfiguracaoAcademico(), "nota" + x + "MediaFinal");
			ConfiguracaoAcademicoNotaConceitoVO notaConceito = (ConfiguracaoAcademicoNotaConceitoVO) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + x + "Conceito");
			if ((utilizarNota && apresentarNota) || (utilizarNota && notaMediaFinal)) {
				ordemColuna++;
				getFacadeFactory().getHistoricoFacade().realizarValidacaoNotaLancada(historicoVO, null, historicoVO.getConfiguracaoAcademico(), x);
				CrosstabVO crosstab = new CrosstabVO();
				crosstab.setOrdemColuna(ordemColuna);
				crosstab.setOrdemLinha(ordemLinha);
				crosstab.setLabelColuna((String) UtilReflexao.invocarMetodoGet(historicoVO.getConfiguracaoAcademico(), "tituloNotaApresentar" + x));
				if (Uteis.isAtributoPreenchido(notaConceito)) {
					crosstab.setValorString(notaConceito.getAbreviaturaConceitoNota());
				} else {
					if (UtilReflexao.invocarMetodoGet(historicoVO, "nota" + x) != null) {
						crosstab.setValorString(Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgula((Double) UtilReflexao.invocarMetodoGet(historicoVO, "nota" + x), historicoVO.getConfiguracaoAcademico().getQuantidadeCasasDecimaisPermitirAposVirgula()));
					}
				}
				crosstabVOs.add(crosstab);
			}
		}
		return crosstabVOs;
	}

	private String getSqlPadraoConsulta(MatriculaVO matriculaVO, DisciplinaVO disciplinaVO, String ano, String semestre, Date dataInicio, Date dataFim, 
			boolean trazerAlunoTransferencia, UsuarioVO usuarioVO , Boolean considerarFaltaAulasNaoRegistradas, CursoVO cursoVO, TurmaVO turmaVO , Integer quantidadeMinimaFaltas,
			Integer percentualMinimaFaltas, String... regrasTurma) {
		StringBuilder sqlStr = new StringBuilder();
		Integer indice = 1;
		sqlStr.append("select mes, dia, disciplina_codigo, disciplina_nome, codigo, periodoletivo_periodoletivo, periodoletivo_descricao, sum(presenca) presenca, sum(abono) abono, sum(falta) falta, cargahoraria, horaaula, historico_codigo, porcentagemFaltas, matricula, aluno_nome, matriculaperiodoturmadisciplina_ano, matriculaperiodoturmadisciplina_semestre from (");
		for (String st : regrasTurma) {
			sqlStr.append(" select extract(month from horarioturmadia.data)::int as mes, extract(day from horarioturmadia.data)::int as dia, disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, periodoletivo.codigo,");
			sqlStr.append(" (case when (sum(case when frequenciaaula.presente then 1 else 0 end)+ sum(case when frequenciaaula.abonado then 1 else 0 end)+ sum(case when (frequenciaaula.presente = false and frequenciaaula.abonado = false) then 1 else 0 end)) > 0 then (sum(case when (frequenciaaula.presente = false and frequenciaaula.abonado = false) then 1 else 0 end)* 100)/(sum(case when frequenciaaula.presente then 1 else 0 end)+ sum(case when frequenciaaula.abonado then 1 else 0 end)+ sum(case when (frequenciaaula.presente = false and frequenciaaula.abonado = false) then 1 else 0 end))  else 0 end ) as porcentagemFaltas, ");
			
			sqlStr.append(" periodoletivo.periodoletivo as periodoletivo_periodoletivo, periodoletivo.descricao as periodoletivo_descricao, ");
			sqlStr.append(" sum(case when frequenciaaula.presente then 1 else 0 end) as presenca, ");
			sqlStr.append(" sum(case when frequenciaaula.abonado then 1 else 0 end) as abono, ");
			sqlStr.append(" sum(case when ((frequenciaaula.presente = false and frequenciaaula.abonado = false) or (frequenciaaula.presente is null and frequenciaaula.abonado is null ");
			if (considerarFaltaAulasNaoRegistradas) {
				sqlStr.append(" and registroaula.codigo is null)");
			} else {
				sqlStr.append(" and registroaula.codigo is not null)");
			}
			sqlStr.append(" ) then 1 else 0 end) as falta, ");
			sqlStr.append(" (case when gradecurriculargrupooptativadisciplina.codigo is not null then gradecurriculargrupooptativadisciplina.cargahoraria ");
			sqlStr.append(" when gradedisciplinacomposta.codigo is not null then gradedisciplinacomposta.cargahoraria else gradedisciplina.cargahoraria end) as cargahoraria, ");
			sqlStr.append(" (case when gradecurriculargrupooptativadisciplina.codigo is not null then 0 ");
			sqlStr.append(" when gradedisciplinacomposta.codigo is not null then gradedisciplinacomposta.horaaula else gradedisciplina.horaaula end) as horaaula, ");
			sqlStr.append(" historico.codigo as historico_codigo, matricula.matricula, aluno.nome as aluno_nome, matriculaperiodoturmadisciplina.ano as matriculaperiodoturmadisciplina_ano, matriculaperiodoturmadisciplina.semestre as matriculaperiodoturmadisciplina_semestre ");
			sqlStr.append("  from matriculaperiodoturmadisciplina ");
			sqlStr.append("  inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula ");
			sqlStr.append("  inner join pessoa as aluno on matricula.aluno = aluno.codigo ");
			sqlStr.append("  inner join curso on curso.codigo = matricula.curso ");
			sqlStr.append(st);
			sqlStr.append(" inner join horarioturma on (turma.codigo = horarioturma.turma) ");		
			sqlStr.append(" and ((curso.periodicidade = 'IN') ");
			sqlStr.append(" or (curso.periodicidade = 'AN' and matriculaperiodoturmadisciplina.ano = horarioturma.anovigente) ");
			sqlStr.append(" or (curso.periodicidade = 'SE' and matriculaperiodoturmadisciplina.ano = horarioturma.anovigente and matriculaperiodoturmadisciplina.semestre = horarioturma.semestrevigente))");
			sqlStr.append(" inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo");
			sqlStr.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo");		
			sqlStr.append(" and (horarioturmadiaitem.disciplina = matriculaperiodoturmadisciplina.disciplina or (turma.turmaagrupada and horarioturmadiaitem.disciplina in (	");
			sqlStr.append(" select d.equivalente from disciplinaequivalente d where turma.turmaagrupada and d.disciplina = matriculaperiodoturmadisciplina.disciplina");
			sqlStr.append(" union");
			sqlStr.append(" select d.disciplina from disciplinaequivalente d where turma.turmaagrupada and d.equivalente = matriculaperiodoturmadisciplina.disciplina");
			sqlStr.append(" )))");
			sqlStr.append(" inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
			sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
			sqlStr.append(" inner join historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
			sqlStr.append(" left join periodoletivo on periodoletivo.codigo = historico.periodoletivomatrizcurricular ");
			sqlStr.append(" left join gradedisciplina on gradedisciplina.codigo = matriculaperiodoturmadisciplina.gradedisciplina");
			sqlStr.append(" left join gradedisciplinacomposta on gradedisciplinacomposta.codigo = matriculaperiodoturmadisciplina.gradedisciplinacomposta");
			sqlStr.append(" left join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = matriculaperiodoturmadisciplina.gradecurriculargrupooptativadisciplina");
			sqlStr.append(" left join registroaula on registroaula.turma = turma.codigo and registroaula.disciplina = disciplina.codigo");
			sqlStr.append(" and registroaula.data = horarioturmadia.data and registroaula.nraula = horarioturmadiaitem.nraula ");
			sqlStr.append(" left join frequenciaaula on registroaula.codigo = frequenciaaula.registroaula and frequenciaaula.matricula = matriculaperiodoturmadisciplina.matricula");
			sqlStr.append(" where 1=1");

			if (Uteis.isAtributoPreenchido(matriculaVO.getMatricula())) {
				sqlStr.append(" and matriculaperiodoturmadisciplina.matricula = '").append(matriculaVO.getMatricula()).append("'");
			}

			if (Uteis.isAtributoPreenchido(cursoVO) && !turmaVO.getTurmaAgrupada()) {
				sqlStr.append(" and curso.codigo = ").append(cursoVO.getCodigo());
			}

			if (Uteis.isAtributoPreenchido(turmaVO)) {
				if (indice == regrasTurma.length) {
					if (turmaVO.getTurmaAgrupada()) {
						sqlStr.append(" and matriculaperiodoturmadisciplina.turma in (select turma from turmaagrupada where turmaorigem =  ").append(turmaVO.getCodigo()).append(")");
					} else {
						sqlStr.append(" and matriculaperiodoturmadisciplina.turma = ").append(turmaVO.getCodigo());
					}
				} else {
					sqlStr.append(" and turma.codigo = ").append(turmaVO.getCodigo());
				}
			}

			if (Uteis.isAtributoPreenchido(disciplinaVO)) {
				sqlStr.append(" and (disciplina.codigo = ").append(disciplinaVO.getCodigo()).append(" or matriculaperiodoturmadisciplina.disciplina = ").append(disciplinaVO.getCodigo()).append(") ");
			}

			if (Uteis.isAtributoPreenchido(ano)) {
				sqlStr.append(" and matriculaperiodoturmadisciplina.ano = '").append(ano).append("'");
			}

			if (Uteis.isAtributoPreenchido(semestre)) {
				sqlStr.append(" and matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("'");
			}

			if (Uteis.isAtributoPreenchido(dataInicio)) {
				sqlStr.append(" and horarioturmadia.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("'");
			}

			if (Uteis.isAtributoPreenchido(dataFim)) {
				sqlStr.append(" and horarioturmadia.data <= '").append(Uteis.getDataJDBC(dataFim)).append("'");
			}

			if(!trazerAlunoTransferencia) {
				sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
			}
			sqlStr.append(" group by horarioturmadia.data, disciplina.codigo, periodoletivo.codigo, periodoletivo.periodoletivo, gradecurriculargrupooptativadisciplina.codigo, gradedisciplinacomposta.codigo, gradedisciplina.codigo, historico.codigo, matricula.matricula, aluno.nome, matriculaperiodoturmadisciplina.ano, matriculaperiodoturmadisciplina.semestre");
			if (indice < regrasTurma.length) {
				sqlStr.append(" union all ");
			}
			indice++;
		}
		sqlStr.append(" ) as t");

		sqlStr.append(" group by mes, dia, disciplina_codigo, disciplina_nome, codigo, periodoletivo_periodoletivo, periodoletivo_descricao, cargahoraria, horaaula, historico_codigo , porcentagemFaltas, matricula, aluno_nome, matriculaperiodoturmadisciplina_ano, matriculaperiodoturmadisciplina_semestre ");
		sqlStr.append(" order by disciplina_nome, periodoletivo_periodoletivo, mes, dia ");
		//System.out.println(sqlStr.toString());
		return sqlStr.toString();
	}

	public static String getCaminhoBaseRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator;
	}

	public static String getDesignIReportRelatorioLayout1() {
		return getCaminhoBaseRelatorio() + "FrequenciaAlunoRel.jrxml";
	}
	
	public static String getDesignIReportRelatorioLayout2() {
		return getCaminhoBaseRelatorio() + "FrequenciaAlunoRel2.jrxml";
	}
}