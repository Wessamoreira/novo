package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import relatorio.negocio.comuns.academico.EstatisticaMatriculaRelVO;
import relatorio.negocio.comuns.academico.EstatisticaMatriculaTurnoRelVO;
import relatorio.negocio.interfaces.academico.EstatisticaMatriculaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
public class EstatisticaMatriculaRel extends SuperRelatorio implements EstatisticaMatriculaRelInterfaceFacade {

	private static final long serialVersionUID = -6057758177363546299L;

	private void validarDados(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception {
		if (filtroRelatorioAcademicoVO.getFiltrarPorPeriodoData()) {
			if (filtroRelatorioAcademicoVO.getDataInicio() == null) {
				throw new Exception("A DATA DE INÍCIO deve ser informado!");
			}
			if (filtroRelatorioAcademicoVO.getDataTermino() == null) {
				throw new Exception("A DATA FINAL deve ser informado!");
			}
			if ((filtroRelatorioAcademicoVO.getDataInicio().compareTo(filtroRelatorioAcademicoVO.getDataTermino()) >= 1)) {
				throw new Exception("A DATA DE INÍCIO não pode ser maior que a DATA FINAL!");
			}
		}
		if (filtroRelatorioAcademicoVO.getFiltrarPorAnoSemestre()) {			
			if (filtroRelatorioAcademicoVO.getAno().trim().isEmpty()) {
				throw new Exception("A ANO deve ser informado!");
			}			
		}else if (filtroRelatorioAcademicoVO.getFiltrarPorAnoSemestre()) {
		} else if (filtroRelatorioAcademicoVO.getFiltrarPorAnoSemestre()) {
			if (filtroRelatorioAcademicoVO.getSemestre().trim().isEmpty()) {
				throw new Exception("A SEMESTRE deve ser informado!");
			}
		}
		boolean selecionado = false;
		for (UnidadeEnsinoVO ueVO : unidadeEnsinoVOs) {
			if (ueVO.getFiltrarUnidadeEnsino()) {
				selecionado = true;
				break;
			}
		}
		if (!selecionado) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_AlunosPorUnidadeCursoTurnoTurmaRel_unidadeEnsino"));
		}
	}

	public List<EstatisticaMatriculaRelVO> criarObjeto(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<CursoVO> cursoVOs, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<TurnoVO> turnoVOs, Boolean exibirMatriculaCalouro, Boolean exibirMatriculaVeterano, Boolean exibirPreMatriculaCalouro, Boolean exibirPreMatriculaVeterano) throws Exception {
		validarDados(filtroRelatorioAcademicoVO, unidadeEnsinoVOs);
		return criarRelatorio(filtroRelatorioAcademicoVO, cursoVOs, unidadeEnsinoVOs, turnoVOs, exibirMatriculaCalouro, exibirMatriculaVeterano, exibirPreMatriculaCalouro, exibirPreMatriculaVeterano);
	}

	private List<EstatisticaMatriculaRelVO> criarRelatorio(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<CursoVO> cursoVOs, List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<TurnoVO> turnoVOs, Boolean exibirMatriculaCalouro, Boolean exibirMatriculaVeterano, Boolean exibirPreMatriculaCalouro, Boolean exibirPreMatriculaVeterano) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT unidadeensino_nome, curso_nome, turno_nome, tipo, qtdmatricula, data FROM ( ");
		if (exibirMatriculaCalouro) {
			// Calouros
			sqlStr.append("SELECT tipo, SUM(qtdmatricula) as qtdmatricula, data, unidadeensino_nome, curso_nome, turno_nome FROM ( ");
			sqlStr.append("SELECT cast ('CA' as character(2)) as tipo, ");
			sqlStr.append("count(distinct matricula.matricula) as qtdMatricula, ");
			sqlStr.append("to_char(matriculaperiodo.data, 'DD/MM/YYYY') as data, ");
			sqlStr.append("unidadeensino.nome as unidadeensino_nome, curso.nome as curso_nome, turno.nome as turno_nome ");
			sqlStr.append("FROM matricula ");
			sqlStr.append("inner join matriculaperiodo ON (matricula.matricula = matriculaperiodo.matricula) ");
			sqlStr.append("inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
			sqlStr.append("inner join curso on curso.codigo = matricula.curso ");
			sqlStr.append("inner join turno ON matricula.turno = turno.codigo ");
			sqlStr.append("where ").append(adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs));
			sqlStr.append(adicionarFiltroCurso(cursoVOs));
			sqlStr.append(adicionarFiltroTurno(turnoVOs));
			sqlStr.append(" and ").append(getFiltroPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
			sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
			sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
			sqlStr.append(" and matriculaperiodo.situacaomatriculaperiodo not in ('PR', 'PC') ");
			sqlStr.append(" and 0 = (select count(mp.codigo) from matriculaperiodo as mp where mp.matricula = matricula.matricula and  (mp.ano||'/'||mp.semestre) < (matriculaperiodo.ano ||'/'||matriculaperiodo.semestre) ) ");
			sqlStr.append("Group By unidadeensino.nome, curso.nome, turno.nome, matriculaperiodo.data) as t ");
			sqlStr.append("Group BY unidadeensino_nome, curso_nome, turno_nome, data, tipo ");
		}
		if (exibirMatriculaVeterano) {
			if (exibirMatriculaCalouro) {
				sqlStr.append("union all ");
			}
			// Veteranos
			sqlStr.append("SELECT tipo, SUM(qtdmatricula) as qtdmatricula, data, unidadeensino_nome, curso_nome, turno_nome FROM(SELECT cast ('VE' as character(2)) as tipo, ");
			sqlStr.append("count(distinct matricula.matricula) as qtdMatricula, ");
			sqlStr.append("to_char(matriculaperiodo.data, 'DD/MM/YYYY') as data, ");
			sqlStr.append("unidadeensino.nome as unidadeensino_nome, curso.nome as curso_nome, turno.nome as turno_nome ");
			sqlStr.append("FROM matricula ");
			sqlStr.append("inner join matriculaperiodo ON (matricula.matricula = matriculaperiodo.matricula) ");
			sqlStr.append("inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
			sqlStr.append("inner join curso on curso.codigo = matricula.curso ");
			sqlStr.append("inner join turno ON matricula.turno = turno.codigo ");
			sqlStr.append("where ").append(adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs));
			sqlStr.append(adicionarFiltroCurso(cursoVOs));
			sqlStr.append(adicionarFiltroTurno(turnoVOs));
			sqlStr.append(" and ").append(getFiltroPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
			sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
			sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
			sqlStr.append(" and matriculaperiodo.situacaomatriculaperiodo not in ('PR', 'PC') ");
			sqlStr.append("and 0 < (select count(mp.codigo) from matriculaperiodo mp where mp.matricula = matricula.matricula and (mp.ano||'/'||mp.semestre) < (matriculaperiodo.ano ||'/'||matriculaperiodo.semestre)) ");
			sqlStr.append("Group By unidadeensino.nome, curso.nome, turno.nome, matriculaperiodo.data) as t ");
			sqlStr.append("Group BY unidadeensino_nome, curso_nome, turno_nome, data, tipo ");
		}

		if (exibirPreMatriculaCalouro) {
			if (exibirMatriculaCalouro || exibirMatriculaVeterano) {
				sqlStr.append("union all ");
			}
			// Pre-Matriculados
			sqlStr.append("SELECT tipo, SUM(qtdmatricula) as qtdmatricula, data, unidadeensino_nome, curso_nome, turno_nome ");
			sqlStr.append("FROM(  SELECT cast ('PRC' as character(3)) as tipo,  count(distinct matricula.matricula) as qtdMatricula,  to_char(matriculaperiodo.data, 'DD/MM/YYYY') as data, ");
			sqlStr.append("unidadeensino.nome as unidadeensino_nome, curso.nome as curso_nome, turno.nome as turno_nome ");
			sqlStr.append("FROM matricula  ");
			sqlStr.append("inner JOIN matriculaperiodo ON (matricula.matricula = matriculaperiodo.matricula) ");
			sqlStr.append("inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
			sqlStr.append("inner join curso on curso.codigo = matricula.curso ");
			sqlStr.append("inner join turno ON matricula.turno = turno.codigo ");
			sqlStr.append("where ").append(adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs));
			sqlStr.append(adicionarFiltroCurso(cursoVOs));
			sqlStr.append(adicionarFiltroTurno(turnoVOs));
			sqlStr.append(" and ").append(getFiltroPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));//
			sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
			sqlStr.append(" and matriculaperiodo.situacaomatriculaperiodo in ('PR') ");
			sqlStr.append(" and 0 = (select count(mp.codigo) from matriculaperiodo as mp where mp.matricula = matricula.matricula and  (mp.ano||'/'||mp.semestre) < (matriculaperiodo.ano ||'/'||matriculaperiodo.semestre) ) ");
			sqlStr.append(" Group By unidadeensino.nome, curso.nome, turno.nome, matriculaperiodo.data ");
			sqlStr.append(") as t ");
			sqlStr.append("Group BY unidadeensino_nome, curso_nome, turno_nome, data, tipo ");
		}

		if (exibirPreMatriculaVeterano) {
			if (exibirMatriculaCalouro || exibirMatriculaVeterano || exibirPreMatriculaCalouro) {
				sqlStr.append("union all ");
			}
			sqlStr.append("SELECT tipo, SUM(qtdmatricula) as qtdmatricula, data, unidadeensino_nome, curso_nome, turno_nome ");
			sqlStr.append("FROM( SELECT cast ('PRV' as character(3)) as tipo,  count(distinct matricula.matricula) as qtdMatricula, to_char(matriculaperiodo.data, 'DD/MM/YYYY') as data, ");
			sqlStr.append("unidadeensino.nome as unidadeensino_nome, curso.nome as curso_nome, turno.nome as turno_nome ");
			sqlStr.append("FROM matricula ");
			sqlStr.append("inner JOIN matriculaperiodo ON (matricula.matricula = matriculaperiodo.matricula) ");
			sqlStr.append("inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
			sqlStr.append("inner join curso on curso.codigo = matricula.curso ");
			sqlStr.append("inner join turno ON matricula.turno = turno.codigo ");
			sqlStr.append("where ").append(adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs));
			sqlStr.append(adicionarFiltroCurso(cursoVOs));
			sqlStr.append(adicionarFiltroTurno(turnoVOs));
			sqlStr.append(" and ").append(getFiltroPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));//
			sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
			sqlStr.append(" and   matriculaperiodo.situacao != '' and matriculaperiodo.situacaomatriculaperiodo in ('PR') ");
			sqlStr.append(" and 0 <> (select count(mp.codigo) from matriculaperiodo as mp where mp.matricula = matricula.matricula and  (mp.ano||'/'||mp.semestre) < (matriculaperiodo.ano ||'/'||matriculaperiodo.semestre) ) ");
			sqlStr.append(" Group By unidadeensino.nome, curso.nome, turno.nome, matriculaperiodo.data ");
			sqlStr.append(" ) as t ");
			sqlStr.append("Group BY unidadeensino_nome, curso_nome, turno_nome, data, tipo ");
		}
		sqlStr.append(") as consulta ");
		sqlStr.append("order by unidadeensino_nome, curso_nome, turno_nome, case tipo when 'CA' then 1 when 'VE' then 2 when 'PRC' then 3 else 4 end, data ");
		return montarDados(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()));
	}

	private List<EstatisticaMatriculaRelVO> montarDados(SqlRowSet result) throws Exception {
		Map<String, EstatisticaMatriculaRelVO> map = new HashMap<String, EstatisticaMatriculaRelVO>(0);
		while (result.next()) {
			if (!map.containsKey(result.getString("curso_nome"))) {
				EstatisticaMatriculaRelVO obj = new EstatisticaMatriculaRelVO();
				obj.setUnidadeEnsino(result.getString("unidadeEnsino_nome"));
				obj.setCurso(result.getString("curso_nome"));
				map.put(obj.getCurso(), obj);
			}
			EstatisticaMatriculaRelVO obj = map.get(result.getString("curso_nome"));
			montarDadoEstatisticaMatriculaCursoRelVOs(result, obj);
		}
		SortedSet<String> keys = new TreeSet<String>(map.keySet());
		List<EstatisticaMatriculaRelVO> estatisticaMatriculaRelVOs = new ArrayList<EstatisticaMatriculaRelVO>();
		for (String key : keys) {
			estatisticaMatriculaRelVOs.add(map.get(key));
		}
		return estatisticaMatriculaRelVOs;
	}

	private void montarDadoEstatisticaMatriculaCursoRelVOs(SqlRowSet result, EstatisticaMatriculaRelVO estatisticaMatriculaRelVO) throws Exception {
		EstatisticaMatriculaTurnoRelVO obj = new EstatisticaMatriculaTurnoRelVO();
		obj.setTipo(result.getString("tipo"));
		obj.setTurno(result.getString("turno_nome"));
		obj.setQuantidadeMatricula(result.getInt("qtdmatricula"));
		obj.setData(Uteis.getData(result.getString("data"), "dd/MM/yyyy"));
		estatisticaMatriculaRelVO.setTotalPorUnidadeEnsino(estatisticaMatriculaRelVO.getTotalPorUnidadeEnsino() + obj.getQuantidadeMatricula());
		estatisticaMatriculaRelVO.getEstatisticaMatriculaTurnoRelVOs().add(obj);
	}

	public static String getDesignIReportRelatorio() {
		return getCaminhoBaseRelatorio() + getIdEntidade() + ".jrxml";
	}

	public static String getCaminhoBaseRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator;
	}

	public static String getIdEntidade() {
		return "EstatisticaMatriculaRel";
	}

	private String adicionarFiltroUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" unidadeEnsino.codigo in (0");
		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				sql.append(", ").append(unidadeEnsinoVO.getCodigo());
			}
		}
		sql.append(") ");
		return sql.toString();
	}

	private String adicionarFiltroCurso(List<CursoVO> cursoVOs) {
		boolean encontrado = false;
		StringBuilder sql = new StringBuilder("");
		sql.append(" and curso.codigo in (0");
		for (CursoVO cursoVO : cursoVOs) {
			if (cursoVO.getFiltrarCursoVO()) {
				sql.append(", ").append(cursoVO.getCodigo());
				encontrado = true;
			}
		}
		if (!encontrado) {
			return "";
		}
		sql.append(") ");
		return sql.toString();
	}

	private String adicionarFiltroTurno(List<TurnoVO> turnoVOs) {
		boolean encontrado = false;
		StringBuilder sql = new StringBuilder("");
		sql.append(" and turno.codigo in (0");
		for (TurnoVO turnoVO : turnoVOs) {
			if (turnoVO.getFiltrarTurnoVO()) {
				sql.append(", ").append(turnoVO.getCodigo());
				encontrado = true;
			}
		}
		if (!encontrado) {
			return "";
		}
		sql.append(") ");
		return sql.toString();
	}

}
