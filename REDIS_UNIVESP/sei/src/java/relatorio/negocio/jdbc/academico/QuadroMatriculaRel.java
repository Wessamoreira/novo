package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.QuadroMatriculaRelVO;
import relatorio.negocio.interfaces.academico.QuadroMatriculaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class QuadroMatriculaRel extends SuperRelatorio implements QuadroMatriculaRelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7987003561235708237L;

	public QuadroMatriculaRel() {
		inicializarParametros();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.QuadroMatriculaRelInterfaceFacade#
	 * inicializarParametros()
	 */
	public void inicializarParametros() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.QuadroMatriculaRelInterfaceFacade#
	 * criarObjeto(java.lang.String, java.lang.String, java.util.Date,
	 * java.util.Date, negocio.comuns.administrativo.UnidadeEnsinoVO)
	 */
	public List<QuadroMatriculaRelVO> criarObjeto(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, UnidadeEnsinoVO unidadeEnsino, List<QuadroMatriculaRelVO> listaQuadroMatriculaRelVOs) throws Exception {

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
			if (filtroRelatorioAcademicoVO.getSemestre().trim().isEmpty()) {
				throw new Exception("A SEMESTRE deve ser informado!");
			}
		}
		if (filtroRelatorioAcademicoVO.getFiltrarPorAno()) {
			if (filtroRelatorioAcademicoVO.getAno().trim().isEmpty()) {
				throw new Exception("A ANO deve ser informado!");
			}			
		}
		if (unidadeEnsino == null || unidadeEnsino.getCodigo() == 0) {
			throw new ConsistirException("É necessário informar a Unidade de Ensino.");
		}
		listaQuadroMatriculaRelVOs.clear();
		listaQuadroMatriculaRelVOs = consultaQuadroMatriculaPeriodoPorUnidadeEnsinoAnoSemestre(filtroRelatorioAcademicoVO, unidadeEnsino.getCodigo().intValue());
		return listaQuadroMatriculaRelVOs;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("QuadroMatriculaRel");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.QuadroMatriculaRelInterfaceFacade#
	 * consultaAnoSemestrePorUnidadeEnsino(java.lang .Integer)
	 */
	public List<SelectItem> consultaAnoSemestrePorUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
		StringBuilder selectStr = new StringBuilder();
		selectStr.append(" SELECT matriculaperiodo.ano, matriculaperiodo.semestre FROM matriculaperiodo");
		selectStr.append(" INNER JOIN unidadeensinocurso ON unidadeensinocurso.codigo = matriculaperiodo.unidadeensinocurso");
		selectStr.append(" WHERE unidadeensinocurso.unidadeensino = ").append(codigoUnidadeEnsino);
		selectStr.append(" GROUP BY matriculaperiodo.ano, matriculaperiodo.semestre");
		selectStr.append(" ORDER BY (matriculaperiodo.ano ||'/'|| matriculaperiodo.semestre) desc");

		return (montarDadosConsultaAnoSemestre(getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString())));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.QuadroMatriculaRelInterfaceFacade#
	 * montarDadosConsultaAnoSemestre(java.sql.ResultSet )
	 */
	public List<SelectItem> montarDadosConsultaAnoSemestre(SqlRowSet tabelaResultado) throws Exception {
		List<SelectItem> listaAnoSemestre = new ArrayList<SelectItem>();
		String anoSemestre = "";
		while (tabelaResultado.next()) {
			if ((tabelaResultado.getString("ano") != null && !tabelaResultado.getString("ano").equals("")) && tabelaResultado.getString("semestre") != null && !tabelaResultado.getString("semestre").equals("")) {
				anoSemestre = tabelaResultado.getString("ano") + "/0" + tabelaResultado.getString("semestre");
				listaAnoSemestre.add(new SelectItem(anoSemestre, anoSemestre));
			}

		}
		return listaAnoSemestre;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.academico.QuadroMatriculaRelInterfaceFacade#
	 * consultaQuadroMatriculaPeriodoPorUnidadeEnsinoAnoSemestre
	 * (java.lang.String, java.lang.String, java.util.Date, java.util.Date,
	 * java.lang.Integer)
	 */
	public List<QuadroMatriculaRelVO> consultaQuadroMatriculaPeriodoPorUnidadeEnsinoAnoSemestre(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer codigoUnidadeEnsino) throws Exception {
		StringBuilder selectStr = new StringBuilder();
		selectStr.append(" SELECT unidadeensino.nome AS unidadeensino, curso.nome AS curso, curso.codigo, turma.curso AS codigocurso, turma.identificadorturma AS turma,");
		selectStr.append(" matriculaperiodo.data::DATE as data, count(distinct matricula.matricula) AS QtdeMatriculas FROM matriculaperiodo");
		selectStr.append(" INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula");
		selectStr.append(" INNER JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino");
		selectStr.append(" INNER JOIN turma ON turma.codigo = matriculaperiodo.turma");
		selectStr.append(" INNER JOIN curso ON curso.codigo = matricula.curso");
		selectStr.append(" WHERE matricula.unidadeensino = ").append(codigoUnidadeEnsino.intValue());
		selectStr.append(" and ").append(getFiltroPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		selectStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		selectStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
//		if (!ano.equals("")) {
//			selectStr.append(" AND (matriculaperiodo.ano='").append(ano).append("' OR matriculaperiodo.ano = '' OR matriculaperiodo.ano is null) ");
//		}
//		if (!semestre.equals("")) {
//			selectStr.append(" AND (matriculaperiodo.semestre='").append(semestre).append("' or matriculaperiodo.semestre='' or matriculaperiodo.semestre is null )");
//		}
//		if (dataInicial != null && dataFinal != null) {
//			selectStr.append(" AND matriculaperiodo.data::DATE>='").append(Uteis.getDataJDBC(dataInicial)).append("' AND matriculaperiodo.data::DATE<='").append(Uteis.getDataJDBC(dataFinal)).append("'");
//		}
		selectStr.append(" GROUP BY curso.nome, matriculaperiodo.data::DATE, turma.identificadorturma, unidadeensino.nome, curso.codigo, turma.curso");
		selectStr.append(" ORDER BY curso.nome, turma, matriculaperiodo.data::DATE;");

		return (montarDadosConsultaQuadroMatriculaPeriodoPorUnidadeEnsinoAnoSemestre(filtroRelatorioAcademicoVO.getAno(), filtroRelatorioAcademicoVO.getSemestre(), getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString())));
	}

//	public String getFiltroSituacaoAcademica(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String campo) {
//		StringBuilder sqlStr = new StringBuilder();
//		campo = campo.trim();
//		sqlStr.append(" ").append(campo).append(".situacaomatriculaperiodo in ('AT', 'FI', 'FO'");
//		if (filtroRelatorioAcademicoVO.getPreMatricula()) {
//			sqlStr.append(", 'PR'");
//		}
//		if (filtroRelatorioAcademicoVO.getPreMatriculaCancelada()) {
//			sqlStr.append(", 'PC'");
//		}
//		if (filtroRelatorioAcademicoVO.getTrancado()) {
//			sqlStr.append(", 'TR'");
//		}
//		if (filtroRelatorioAcademicoVO.getTransferenciaExterna()) {
//			sqlStr.append(", 'TS'");
//		}
//		if (filtroRelatorioAcademicoVO.getTransferenciaInterna()) {
//			sqlStr.append(", 'TI'");
//		}
//		if (filtroRelatorioAcademicoVO.getAbandonado()) {
//			sqlStr.append(", 'AC'");
//		}
//		if (filtroRelatorioAcademicoVO.getCancelado()) {
//			sqlStr.append(", 'CA'");
//		}
//		sqlStr.append(") ");
//		return sqlStr.toString();		
//	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.academico.QuadroMatriculaRelInterfaceFacade#
	 * montarDadosConsultaQuadroMatriculaPeriodoPorUnidadeEnsinoAnoSemestre
	 * (java.lang.String, java.lang.String, java.sql.ResultSet)
	 */
	public List<QuadroMatriculaRelVO> montarDadosConsultaQuadroMatriculaPeriodoPorUnidadeEnsinoAnoSemestre(String ano, String semestre, SqlRowSet tabelaResultado) throws Exception {
		List<QuadroMatriculaRelVO> vetResultado = new ArrayList<QuadroMatriculaRelVO>(0);
		while (tabelaResultado.next()) {
			QuadroMatriculaRelVO quadroMatriculaRelVo = new QuadroMatriculaRelVO();
			quadroMatriculaRelVo.setNomeUnidadeEnsino(tabelaResultado.getString("unidadeensino"));
			quadroMatriculaRelVo.setNomeCurso(tabelaResultado.getString("curso"));
			quadroMatriculaRelVo.setNomeTurma(tabelaResultado.getString("turma"));
			quadroMatriculaRelVo.setData(tabelaResultado.getDate("data"));
			quadroMatriculaRelVo.setQtdeMatriculas(tabelaResultado.getInt("qtdeMatriculas"));
			if (!ano.equals("")) {
				quadroMatriculaRelVo.setAno(ano);
			}
			if (!semestre.equals("")) {
				quadroMatriculaRelVo.setSemestre("0" + semestre);
			}
			vetResultado.add(quadroMatriculaRelVo);
		}
		return vetResultado;
	}
}
