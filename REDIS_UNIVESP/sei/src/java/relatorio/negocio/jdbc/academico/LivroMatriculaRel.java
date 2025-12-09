package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.academico.LivroMatriculaRelInterfaceFacade;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.LivroMatriculaRelVO;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
public class LivroMatriculaRel extends SuperRelatorio implements LivroMatriculaRelInterfaceFacade {

	private static final long serialVersionUID = -6057758177363546299L;

	public List<LivroMatriculaRelVO> criarObjeto(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<CursoVO> cursoVOs,
			List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<TurnoVO> turnoVOs, TurmaVO turma, String ano, String semestre, String periodicidade, String tipoAluno) throws Exception {
		return criarRelatorio(filtroRelatorioAcademicoVO, cursoVOs, unidadeEnsinoVOs, turnoVOs, turma, ano, semestre, periodicidade, tipoAluno);
	}

	private List<LivroMatriculaRelVO> criarRelatorio(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, List<CursoVO> cursoVOs,
			List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, String ano, String semestre, String periodicidade, String tipoAluno) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		StringBuilder filtroCursoIN = new StringBuilder("");
		StringBuilder filtroUnidadeIN = new StringBuilder("");
		StringBuilder filtroTurnoIN = new StringBuilder("");
		if(!Uteis.isAtributoPreenchido(turmaVO)){
			if (Uteis.isAtributoPreenchido(cursoVOs)) {
				for (CursoVO cursoVO : cursoVOs) {
					if (cursoVO.getFiltrarCursoVO()) {
						filtroCursoIN.append(filtroCursoIN.toString().isEmpty() ? "" : ", ").append(cursoVO.getCodigo());
					}
				}
			}
			if (Uteis.isAtributoPreenchido(unidadeEnsinoVOs)) {
				for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
					if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
						filtroUnidadeIN.append(filtroUnidadeIN.toString().isEmpty() ? "" : ", ").append(unidadeEnsinoVO.getCodigo());
					}
				}
			}
			if (Uteis.isAtributoPreenchido(turnoVOs)) {
				for (TurnoVO turnoVO : turnoVOs) {
					if (turnoVO.getFiltrarTurnoVO()) {
						filtroTurnoIN.append(filtroTurnoIN.toString().isEmpty() ? "" : ", ").append(turnoVO.getCodigo());
					}
				}
	
			}
		}
		sqlStr.append("	SELECT distinct(m.matricula) AS matricula, pessoa.nome AS nome, pessoa.sexo AS sexo, mp.situacao AS situacao, mp.situacaoMatriculaPeriodo AS situacaoMatriculaPeriodo ");
		sqlStr.append(" FROM matricula m  ");
		sqlStr.append(" INNER JOIN pessoa ON pessoa.codigo = m.aluno ");
		sqlStr.append(" INNER JOIN matriculaperiodo mp ON mp.matricula = m.matricula ");
		sqlStr.append(" INNER JOIN turma tur on mp.turma = tur.codigo ");
		sqlStr.append(" INNER JOIN curso c ON c.codigo = m.curso ");					
		sqlStr.append(" WHERE  ");
		sqlStr.append(" c.periodicidade = '").append(periodicidade).append("' ");
		if (tipoAluno.equals("reposicao")) {
			sqlStr.append("	AND exists (select codigo from matriculaperiodoturmadisciplina mptd where mptd.matriculaperiodo = mp.codigo and  mptd.turma <> mp.turma) ");
		}else if (tipoAluno.equals("normal")) {
			sqlStr.append("	AND exists (select codigo from matriculaperiodoturmadisciplina mptd where mptd.matriculaperiodo = mp.codigo and  mptd.turma = mp.turma) ");
		}
		if (Uteis.isAtributoPreenchido(ano) && !periodicidade.equals("IN")) {	    	
	    	sqlStr.append(" AND mp.ano = '").append(ano).append("' ");
	    }
		if (Uteis.isAtributoPreenchido(semestre) && periodicidade.equals("SE")) {			
			sqlStr.append(" AND mp.semestre = '").append(semestre).append("' ");
		}
		
		if (turmaVO.getCodigo() != null && turmaVO.getCodigo() > 0) {
			sqlStr.append(" AND tur.codigo = ").append(turmaVO.getCodigo());
		}
		if (filtroUnidadeIN.length() > 0) {
			sqlStr.append(" and m.unidadeensino in (").append(filtroUnidadeIN).append(") ");
		}
		if (filtroTurnoIN.length() > 0) {
			sqlStr.append(" and m.turno in (").append(filtroTurnoIN).append(") ");
		}

		if (filtroCursoIN.length() > 0) {
			sqlStr.append(" and m.curso in (").append(filtroCursoIN).append(") ");			
		}
		if (periodicidade.equals("IN") && filtroRelatorioAcademicoVO.getDataInicio() != null && filtroRelatorioAcademicoVO.getDataTermino() != null){
			sqlStr.append(" and "+realizarGeracaoWherePeriodo(filtroRelatorioAcademicoVO.getDataInicio(), filtroRelatorioAcademicoVO.getDataTermino(), "mp.data", false));
		}
		sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "mp"));
		sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "mp"));
		sqlStr.append(" ORDER BY pessoa.nome");
		
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<LivroMatriculaRelVO> resultados = new ArrayList<LivroMatriculaRelVO>(0);
		while (tabelaResultado.next()) {
			LivroMatriculaRelVO obj =  montarDados(tabelaResultado);
			resultados.add(obj);
		}	
		return resultados;
	}

	private LivroMatriculaRelVO montarDados(SqlRowSet dadosSQL) {
		LivroMatriculaRelVO obj = new LivroMatriculaRelVO();
		obj.getMatriculaPeriodo().setMatricula((dadosSQL.getString("matricula")));
		obj.getPessoaVO().setNome(dadosSQL.getString("nome"));
		obj.getPessoaVO().setSexo(dadosSQL.getString("sexo"));
		obj.getMatriculaPeriodo().setSituacao((dadosSQL.getString("situacao")));
		obj.getMatriculaPeriodo().setSituacaoMatriculaPeriodo((dadosSQL.getString("situacaoMatriculaPeriodo")));
		return obj;
	}
	
	public static String getDesignIReportRelatorio() {
		return getCaminhoBaseRelatorio() + getIdEntidade() + ".jrxml";
	}
	
	public static String getIdEntidade() {
		return "LivroMatriculaRel";
	}
	
	public static String getCaminhoBaseRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator;
	}

}
