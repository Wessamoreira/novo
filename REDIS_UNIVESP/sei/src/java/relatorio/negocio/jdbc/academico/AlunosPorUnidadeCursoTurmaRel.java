package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.TipoSubTurmaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import relatorio.negocio.comuns.academico.AlunosPorUnidadeCursoTurmaRelVO;
import relatorio.negocio.interfaces.academico.AlunosPorUnidadeCursoTurmaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
public class AlunosPorUnidadeCursoTurmaRel extends SuperRelatorio implements AlunosPorUnidadeCursoTurmaRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public static void validarDados(List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws ConsistirException {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @seerelatorio.negocio.jdbc.academico. AlunosPorUnidadeCursoTurmaRelInterfaceFacade#criarObjeto(negocio.comuns. administrativo.UnidadeEnsinoVO,
	 * negocio.comuns.academico.CursoVO, negocio.comuns.academico.TurmaVO, java.lang.String, java.lang.String)
	 */
	public List<AlunosPorUnidadeCursoTurmaRelVO> criarObjeto(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turma, DisciplinaVO disciplina, String ano, String semestre, String tipoMatricula, Boolean trazerSomenteAlunosAtivos, Boolean trazerFiliacao, String tipoAluno, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, FiltroRelatorioAcademicoVO filtroAcademicoVO, UsuarioVO usuarioVO, String situacaoAlunoCurso, Boolean trazerAlunoTransferencia, String periodicidade) throws Exception {
		SqlRowSet rowSet = null;
		if (tipoAluno.equals("reposicao")) {
			rowSet = consultarPorCursoTurmaDisciplinaAnoSemestreSituacaoReposicaoInclusao(unidadeEnsinoVOs, cursoVOs, turnoVOs, turma, disciplina, ano, semestre, SituacaoVinculoMatricula.ATIVA.getValor(), tipoMatricula, false, configuracaoFinanceiroVO, filtroAcademicoVO, usuarioVO, situacaoAlunoCurso, trazerAlunoTransferencia, periodicidade);
		} else if (tipoAluno.equals("todos")) {
			rowSet = consultarPorCursoTurmaDisciplinaAnoSemestreSituacaoNormalReposicaoInclusao(unidadeEnsinoVOs, cursoVOs, turnoVOs, turma, disciplina, ano, semestre, SituacaoVinculoMatricula.ATIVA.getValor(), tipoMatricula, false, configuracaoFinanceiroVO, filtroAcademicoVO, usuarioVO, situacaoAlunoCurso, trazerAlunoTransferencia, periodicidade);
		} else {
			rowSet = consultarPorCursoTurmaDisciplinaAnoSemestreSituacao(unidadeEnsinoVOs, cursoVOs, turnoVOs, turma, disciplina, ano, semestre, SituacaoVinculoMatricula.ATIVA.getValor(), tipoMatricula, false, configuracaoFinanceiroVO, filtroAcademicoVO, usuarioVO, situacaoAlunoCurso, trazerAlunoTransferencia, periodicidade);
		}
		return montarDadosConsulta(rowSet, trazerFiliacao);
	}

	private List<AlunosPorUnidadeCursoTurmaRelVO> montarDadosConsulta(SqlRowSet rowSet, Boolean trazerFiliacao) throws Exception {
		List<AlunosPorUnidadeCursoTurmaRelVO> alunosPorUnidadeCursoTurmaRelVOs = new ArrayList<AlunosPorUnidadeCursoTurmaRelVO>(0);
		while (rowSet.next()) {
			alunosPorUnidadeCursoTurmaRelVOs.add(montarDados(rowSet, trazerFiliacao));
		}
		return alunosPorUnidadeCursoTurmaRelVOs;
	}

	public SqlRowSet consultarPorCursoTurmaDisciplinaAnoSemestreSituacao(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, DisciplinaVO disciplina, String ano, String semestre, String situacao, String tipoMatricula, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, FiltroRelatorioAcademicoVO filtroAcademicoVO, UsuarioVO usuario, String situacaoAlunoCurso, Boolean trazerAlunoTransferencia, String periodicidade) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT DISTINCT unidadeensino.codigo as unidadeensino_codigo, unidadeensino.nome as unidadeensino_nome, curso.codigo as curso_codigo, curso.nome as curso_nome, turno.codigo as turno_codigo, turno.nome as turno_nome, ");
		sqlStr.append("Matricula.matricula, matricula.situacao, matriculaperiodo.alunoTransferidoUnidade, matricula.alunoabandonoucurso, ");
		sqlStr.append("pessoa.codigo AS pessoa_codigo, pessoa.nome AS pessoanome, pessoa.dataNasc, pessoa.cpf, pessoa.telefoneRes, pessoa.celular, pessoa.email, pessoa.rg, pessoa.orgaoEmissor, pessoa.estadoemissaorg, pessoa.dataEmissaoRg, ");
		sqlStr.append("pessoa.dataNasc, pessoa.endereco, pessoa.setor, pessoa.cep, cidade.nome AS cidade, ");
		sqlStr.append("turma.identificadorturma,  matriculaperiodo.situacaomatriculaperiodo, ");
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append("turmaBase.identificadorturma as turmabase, turmateorica.identificadorturma as turmateorica, turmapratica.identificadorturma as turmapratica ");
		}else{
			sqlStr.append(" '' as turmabase, '' as turmateorica, '' as turmapratica ");
		}
		sqlStr.append("FROM Matricula ");
		sqlStr.append("INNER JOIN curso on curso.codigo = matricula.curso ");
		sqlStr.append("INNER JOIN matriculaperiodo ON Matricula.matricula = ");
		sqlStr.append(" matriculaperiodo.matricula AND case when curso.periodicidade = 'IN' then (matriculaperiodo.codigo =  ");
		sqlStr.append(" (SELECT mp.codigo from matriculaperiodo as mp where mp.matricula = matricula.matricula  ");
		sqlStr.append(" order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, ");
		sqlStr.append(" mp.codigo desc LIMIT 1) ) else true end ");
		sqlStr.append("INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno ");
		sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = MatriculaPeriodo.codigo and mptd.turma = MatriculaPeriodo.turma ");
		sqlStr.append("INNER JOIN historico on mptd.codigo = historico.matriculaperiodoturmadisciplina ");
		if (turmaVO.getSubturma() && (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA) || turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA))) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("INNER JOIN turma on turma.codigo = mptd.turmaTeorica ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("INNER JOIN turma on turma.codigo = mptd.turmaPratica ");
			}
		} else {
			sqlStr.append("INNER JOIN turma on turma.codigo = mptd.turma ");
		}
		sqlStr.append("INNER JOIN turno on turno.codigo = turma.turno ");
		sqlStr.append("INNER JOIN unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sqlStr.append("INNER JOIN disciplina on disciplina.codigo = mptd.disciplina ");
		sqlStr.append("left JOIN turma as turmabase on turmabase.codigo = mptd.turma ");
		sqlStr.append("left JOIN turma as turmapratica on turmapratica.codigo = mptd.turmapratica ");
		sqlStr.append("left JOIN turma as turmateorica on turmateorica.codigo = mptd.turmateorica ");
		sqlStr.append("LEFT JOIN cidade ON cidade.codigo = pessoa.cidade ");
		sqlStr.append("WHERE 1 = 1 ");
		if (turmaVO.getCodigo() != 0 && turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			sqlStr.append(" AND (turma.codigo in (select turmaagrupada.turma from turmaagrupada ");
			if (disciplina != null && !disciplina.getCodigo().equals(0)) {
				sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turmaagrupada.turmaorigem ");
				sqlStr.append(" and (turmadisciplina.disciplina = ").append(disciplina.getCodigo()).append(" or turmadisciplina.disciplina in ( ");
				sqlStr.append(" 	select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.getCodigo());
				sqlStr.append(" 	union ");
				sqlStr.append(" 	select disciplinaequivalente.equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.getCodigo());
				sqlStr.append("))");
			}
			sqlStr.append(" where turmaorigem = ").append(turmaVO.getCodigo()).append(")");
//			sqlStr.append("or (mptd.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
//			sqlStr.append("or (mptd.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			sqlStr.append(") ");
			sqlStr.append(" and mptd.turmapratica is null and  mptd.turmateorica is null ");
			if(!Uteis.isAtributoPreenchido(disciplina)) {
				sqlStr.append("and mptd.disciplina in ( select turmadisciplina.disciplina from turmadisciplina where turmadisciplina.turma = ").append(turmaVO.getCodigo()).append(")");
			}
		} else if (turmaVO.getCodigo() != 0) {
			sqlStr.append(" AND turma.codigo = ").append(turmaVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			if (turmaVO.getCodigo() != 0 && !turmaVO.getSubturma() && turmaVO.getTurmaAgrupada()) {
				sqlStr.append(" AND (disciplina.codigo = ").append(disciplina.getCodigo()).append(" or disciplina.codigo in ( ");
				sqlStr.append(" select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.getCodigo());
				sqlStr.append(" union ");
				sqlStr.append(" select disciplinaequivalente.equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.getCodigo()).append("))");
			} else {
				sqlStr.append(" AND mptd.disciplina = ").append(disciplina.getCodigo());
			}
		}
		sqlStr.append(adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs));
		if (!Uteis.isAtributoPreenchido(turmaVO)) {
			sqlStr.append(adicionarFiltroCurso(cursoVOs));
		}
		sqlStr.append(adicionarFiltroTurno(turnoVOs));
		if (!tipoMatricula.equals("")) {
			if (tipoMatricula.equals("NO")) {
				sqlStr.append(" AND (matricula.tipoMatricula is null OR matricula.tipoMatricula = '").append(tipoMatricula).append("' OR matricula.tipoMatricula = '')");
			} else {
				sqlStr.append(" AND matricula.tipoMatricula = '").append(tipoMatricula).append("'");
			}
		}
		if (Uteis.isAtributoPreenchido(filtroAcademicoVO.getDataInicio())) {
			sqlStr.append(" AND matriculaperiodo.data >= '").append(Uteis.getDataJDBC(filtroAcademicoVO.getDataInicio())).append("'");
		}
		if (Uteis.isAtributoPreenchido(filtroAcademicoVO.getDataTermino())) {
			sqlStr.append(" AND matriculaperiodo.data <= '").append(Uteis.getDataJDBC(filtroAcademicoVO.getDataTermino())).append("'");
		}
		adicionarFiltroCalouroVeterano(sqlStr, situacaoAlunoCurso);
		executarDefinicaoPeriodicidadeCursoFiltroAnoSemestreRealizarConsulta(sqlStr, ano, semestre, periodicidade);	
		if(!trazerAlunoTransferencia) {
			sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		}
		sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		sqlStr.append(" ORDER BY unidadeensino.nome, curso.nome, turno.nome, identificadorturma, pessoanome");		
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}

	public SqlRowSet consultarPorCursoTurmaDisciplinaAnoSemestreSituacaoReposicaoInclusao(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, DisciplinaVO disciplina, String ano, String semestre, String situacao, String tipoMatricula, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, FiltroRelatorioAcademicoVO filtroAcademicoVO, UsuarioVO usuario, String situacaoAlunoCurso, Boolean trazerAlunoTransferencia, String periodicidade) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT DISTINCT unidadeensino.codigo as unidadeensino_codigo, unidadeensino.nome as unidadeensino_nome, curso.codigo as curso_codigo, curso.nome as curso_nome, turno.codigo as turno_codigo, turno.nome as turno_nome, ");
		sqlStr.append("Matricula.matricula, matricula.situacao, matriculaperiodo.alunoTransferidoUnidade, matricula.alunoabandonoucurso, ");
		sqlStr.append("pessoa.codigo AS pessoa_codigo, pessoa.nome AS pessoanome, pessoa.dataNasc, pessoa.cpf, pessoa.telefoneRes, pessoa.celular, pessoa.email, pessoa.rg, pessoa.orgaoEmissor, pessoa.estadoemissaorg, pessoa.dataEmissaoRg, ");
		sqlStr.append("pessoa.dataNasc, pessoa.endereco, pessoa.setor, pessoa.cep, cidade.nome AS cidade, ");
		sqlStr.append("turma.identificadorturma,  matriculaperiodo.situacaomatriculaperiodo, ");
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append("turmaBase.identificadorturma as turmabase, turmateorica.identificadorturma as turmateorica, turmapratica.identificadorturma as turmapratica ");
		}else{
			sqlStr.append(" '' as turmabase, '' as turmateorica, '' as turmapratica ");
		}
		sqlStr.append("FROM Matricula ");
		sqlStr.append("INNER JOIN curso on curso.codigo = matricula.curso ");
		sqlStr.append("INNER JOIN matriculaperiodo ON Matricula.matricula = ");
		sqlStr.append(" matriculaperiodo.matricula AND case when curso.periodicidade = 'IN' then (matriculaperiodo.codigo = ");
		sqlStr.append(" (SELECT mp.codigo from matriculaperiodo as mp where mp.matricula = matricula.matricula  ");
		sqlStr.append(" order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, ");
		sqlStr.append(" mp.codigo desc LIMIT 1) ) else true end ");
		sqlStr.append("INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno ");
		sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo and mptd.turma <> matriculaperiodo.turma ");
		sqlStr.append("INNER JOIN historico on mptd.codigo = historico.matriculaperiodoturmadisciplina ");
		if (turmaVO.getSubturma() && (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA) || turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA))) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("INNER JOIN turma on turma.codigo = mptd.turmaTeorica ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("INNER JOIN turma on turma.codigo = mptd.turmaPratica ");
			}
		} else {
			sqlStr.append("INNER JOIN turma on turma.codigo = mptd.turma ");
		}
		sqlStr.append("INNER JOIN turno on turno.codigo = turma.turno ");
		sqlStr.append("INNER JOIN unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sqlStr.append("INNER JOIN disciplina on disciplina.codigo = mptd.disciplina ");
		sqlStr.append("left JOIN turma as turmabase on turmabase.codigo = mptd.turma ");
		sqlStr.append("left JOIN turma as turmapratica on turmapratica.codigo = mptd.turmapratica ");
		sqlStr.append("left JOIN turma as turmateorica on turmateorica.codigo = mptd.turmateorica ");
		sqlStr.append("LEFT JOIN cidade ON cidade.codigo = pessoa.cidade ");
		sqlStr.append("WHERE 1 = 1 ");
		if (turmaVO.getCodigo() != 0 && turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
			sqlStr.append(" AND (turma.codigo in (select turmaagrupada.turma from turmaagrupada ");
			if (disciplina != null && !disciplina.getCodigo().equals(0)) {
				sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turmaagrupada.turmaorigem ");
				sqlStr.append(" and (turmadisciplina.disciplina = ").append(disciplina.getCodigo()).append(" or turmadisciplina.disciplina in ( ");
				sqlStr.append(" 	select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.getCodigo());
				sqlStr.append(" 	union ");
				sqlStr.append(" 	select disciplinaequivalente.equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.getCodigo());
				sqlStr.append("))");
			}
			sqlStr.append(" where turmaorigem = ").append(turmaVO.getCodigo()).append(")");
//			sqlStr.append("or (mptd.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
//			sqlStr.append("or (mptd.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			sqlStr.append(") ");
			sqlStr.append(" and mptd.turmapratica is null and  mptd.turmateorica is null ");
			if(!Uteis.isAtributoPreenchido(disciplina)) {
				sqlStr.append("and mptd.disciplina in ( select turmadisciplina.disciplina from turmadisciplina where turmadisciplina.turma = ").append(turmaVO.getCodigo()).append(")");
			}
		} else if (turmaVO.getCodigo() != 0) {
			sqlStr.append(" AND turma.codigo = ").append(turmaVO.getCodigo());
		}
		if (disciplina != null && !disciplina.getCodigo().equals(0)) {
			if (turmaVO.getCodigo() != 0 && !turmaVO.getSubturma() && turmaVO.getTurmaAgrupada()) {
				sqlStr.append(" AND (disciplina.codigo = ").append(disciplina.getCodigo()).append(" or disciplina.codigo in ( ");
				sqlStr.append(" select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.getCodigo());
				sqlStr.append(" union ");
				sqlStr.append(" select disciplinaequivalente.equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.getCodigo()).append("))");
			} else {
				sqlStr.append(" AND mptd.disciplina = ").append(disciplina.getCodigo());
			}
		}
		sqlStr.append(adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs));
		if (!Uteis.isAtributoPreenchido(turmaVO)) {
			sqlStr.append(adicionarFiltroCurso(cursoVOs));
		}
		sqlStr.append(adicionarFiltroTurno(turnoVOs));
		if (!tipoMatricula.equals("")) {
			if (tipoMatricula.equals("NO")) {
				sqlStr.append(" AND (matricula.tipoMatricula is null or matricula.tipoMatricula = '").append(tipoMatricula).append("' or matricula.tipoMatricula = '')");
			} else {
				sqlStr.append(" AND matricula.tipoMatricula = '").append(tipoMatricula).append("'");
			}
		}
		if (Uteis.isAtributoPreenchido(filtroAcademicoVO.getDataInicio())) {
			sqlStr.append(" AND matriculaperiodo.data >= '").append(Uteis.getDataJDBC(filtroAcademicoVO.getDataInicio())).append("'");
		}
		if (Uteis.isAtributoPreenchido(filtroAcademicoVO.getDataTermino())) {
			sqlStr.append(" AND matriculaperiodo.data <= '").append(Uteis.getDataJDBC(filtroAcademicoVO.getDataTermino())).append("'");
		}
		adicionarFiltroCalouroVeterano(sqlStr, situacaoAlunoCurso);
		if(!trazerAlunoTransferencia) {
			sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		}
		sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		if (Uteis.isAtributoPreenchido(periodicidade) && (!periodicidade.equals("IN"))) {
			executarDefinicaoPeriodicidadeCursoFiltroAnoSemestreRealizarConsulta(sqlStr, ano, semestre, periodicidade);	
		}
		if(Uteis.isAtributoPreenchido(periodicidade) && periodicidade.equals("integral")) {
			sqlStr.append(" AND curso.periodicidade = 'IN' ");
		}
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		sqlStr.append(" ORDER BY unidadeensino.nome, curso.nome, turno.nome, identificadorturma, pessoanome");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}

	public SqlRowSet consultarPorCursoTurmaDisciplinaAnoSemestreSituacaoNormalReposicaoInclusao(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, List<TurnoVO> turnoVOs, TurmaVO turmaVO, DisciplinaVO disciplina, String ano, String semestre, String situacao, String tipoMatricula, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, FiltroRelatorioAcademicoVO filtroAcademicoVO, UsuarioVO usuario, String situacaoAlunoCurso, Boolean trazerAlunoTransferencia, String periodicidade) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT DISTINCT unidadeensino.codigo as unidadeensino_codigo, unidadeensino.nome as unidadeensino_nome, curso.codigo as curso_codigo, curso.nome as curso_nome, turno.codigo as turno_codigo, turno.nome as turno_nome, ");
		sqlStr.append("Matricula.matricula, matricula.situacao, matriculaperiodo.alunoTransferidoUnidade, matricula.alunoabandonoucurso, ");
		sqlStr.append("pessoa.codigo AS pessoa_codigo, pessoa.nome AS pessoanome, pessoa.dataNasc, pessoa.cpf, pessoa.telefoneRes, pessoa.celular, pessoa.email, pessoa.rg, pessoa.orgaoEmissor, pessoa.estadoemissaorg, pessoa.dataEmissaoRg, ");
		sqlStr.append("pessoa.dataNasc, pessoa.endereco, pessoa.setor, pessoa.cep, cidade.nome AS cidade, ");
		sqlStr.append("turma.identificadorturma,  matriculaperiodo.situacaomatriculaperiodo, ");
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append("turmaBase.identificadorturma as turmabase, turmateorica.identificadorturma as turmateorica, turmapratica.identificadorturma as turmapratica ");
		}else{
			sqlStr.append(" '' as turmabase, '' as turmateorica, '' as turmapratica ");
		}
		sqlStr.append("FROM Matricula ");
		sqlStr.append("INNER JOIN curso on curso.codigo = matricula.curso ");
		sqlStr.append("INNER JOIN matriculaperiodo ON Matricula.matricula = ");
		sqlStr.append(" matriculaperiodo.matricula AND case when curso.periodicidade = 'IN' then (matriculaperiodo.codigo = ");
		sqlStr.append(" (SELECT mp.codigo from matriculaperiodo as mp where mp.matricula = matricula.matricula  ");
		sqlStr.append(" order by (mp.ano || '/' || mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, ");
		sqlStr.append(" mp.codigo desc LIMIT 1) ) else true end ");
		sqlStr.append("INNER JOIN pessoa ON Pessoa.codigo = Matricula.aluno ");
		sqlStr.append("INNER JOIN matriculaperiodoturmadisciplina mptd on mptd.matriculaperiodo = matriculaperiodo.codigo ");
		sqlStr.append("INNER JOIN historico on mptd.codigo = historico.matriculaperiodoturmadisciplina ");
		if (turmaVO.getSubturma() && (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA) || turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA))) {
			if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.TEORICA)) {
				sqlStr.append("INNER JOIN turma on turma.codigo = mptd.turmaTeorica ");
			} else if (turmaVO.getTipoSubTurma().equals(TipoSubTurmaEnum.PRATICA)) {
				sqlStr.append("INNER JOIN turma on turma.codigo = mptd.turmaPratica ");
			}
		} else {
			sqlStr.append("INNER JOIN turma on turma.codigo = mptd.turma ");
		}
		sqlStr.append("INNER JOIN turno on turno.codigo = turma.turno ");
		sqlStr.append("INNER JOIN unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
		sqlStr.append("INNER JOIN disciplina on disciplina.codigo = mptd.disciplina ");
		sqlStr.append("left JOIN turma as turmabase on turmabase.codigo = mptd.turma ");
		sqlStr.append("left JOIN turma as turmapratica on turmapratica.codigo = mptd.turmapratica ");
		sqlStr.append("left JOIN turma as turmateorica on turmateorica.codigo = mptd.turmateorica ");
		sqlStr.append("LEFT JOIN cidade ON cidade.codigo = pessoa.cidade ");
		// sqlStr.append(" WHERE (mptd.turma <> matriculaperiodo.turma OR
		// mptd.turma = matriculaperiodo.turma)");
		sqlStr.append("WHERE 1=1 ");
		if (turmaVO.getCodigo() != 0 && !turmaVO.getSubturma() && turmaVO.getTurmaAgrupada()) {
			sqlStr.append(" AND (turma.codigo in (select turmaagrupada.turma from turmaagrupada ");
			if (disciplina != null && !disciplina.getCodigo().equals(0)) {
				sqlStr.append(" inner join turmadisciplina on turmadisciplina.turma = turmaagrupada.turmaorigem ");
				sqlStr.append(" and (turmadisciplina.disciplina = ").append(disciplina.getCodigo()).append(" or turmadisciplina.disciplina in ( ");
				sqlStr.append(" 	select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.getCodigo());
				sqlStr.append(" 	union ");
				sqlStr.append(" 	select disciplinaequivalente.equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.getCodigo());
				sqlStr.append("))");
			}
			sqlStr.append(" where turmaorigem = ").append(turmaVO.getCodigo()).append(")");
//			sqlStr.append("or (mptd.turmaPratica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'PRATICA'))");
//			sqlStr.append("or (mptd.turmaTeorica in (select TurmaAgrupada.turma from TurmaAgrupada inner join turma as turmaOrigem on turmaOrigem.codigo = TurmaAgrupada.turmaOrigem inner join turma on turma.codigo = turmaagrupada.turma where turmaOrigem.codigo = ").append(turmaVO.getCodigo()).append(" and turmaOrigem.subturma = false and turma.tiposubturma = 'TEORICA'))");
			sqlStr.append(") ");
			sqlStr.append(" and mptd.turmapratica is null and  mptd.turmateorica is null ");
			if(!Uteis.isAtributoPreenchido(disciplina)) {
				sqlStr.append("and mptd.disciplina in ( select turmadisciplina.disciplina from turmadisciplina where turmadisciplina.turma = ").append(turmaVO.getCodigo()).append(")");
			}
			
		} else if (turmaVO.getCodigo() != 0) {
			sqlStr.append(" AND turma.codigo = ").append(turmaVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			if (turmaVO.getCodigo() != 0 && turmaVO.getTurmaAgrupada()) {
				sqlStr.append(" AND (disciplina.codigo = ").append(disciplina.getCodigo()).append(" or disciplina.codigo in ( ");
				sqlStr.append(" select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina.getCodigo());
				sqlStr.append(" union ");
				sqlStr.append(" select disciplinaequivalente.equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina.getCodigo()).append("))");
			} else {
				sqlStr.append(" AND mptd.disciplina = ").append(disciplina.getCodigo());
			}
		}
		sqlStr.append(adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs));
		if (!Uteis.isAtributoPreenchido(turmaVO)) {
			sqlStr.append(adicionarFiltroCurso(cursoVOs));
		}
		sqlStr.append(adicionarFiltroTurno(turnoVOs));
		if (!tipoMatricula.equals("")) {
			if (tipoMatricula.equals("NO")) {
				sqlStr.append(" AND (matricula.tipoMatricula is null or matricula.tipoMatricula = '").append(tipoMatricula).append("' or matricula.tipoMatricula = '')");
			} else {
				sqlStr.append(" AND matricula.tipoMatricula = '").append(tipoMatricula).append("'");
			}
		}
		if (Uteis.isAtributoPreenchido(filtroAcademicoVO.getDataInicio())) {
			sqlStr.append(" AND matriculaperiodo.data >= '").append(Uteis.getDataJDBC(filtroAcademicoVO.getDataInicio())).append("'");
		}
		if (Uteis.isAtributoPreenchido(filtroAcademicoVO.getDataTermino())) {
			sqlStr.append(" AND matriculaperiodo.data <= '").append(Uteis.getDataJDBC(filtroAcademicoVO.getDataTermino())).append("'");
		}
		adicionarFiltroCalouroVeterano(sqlStr, situacaoAlunoCurso);
		if(!trazerAlunoTransferencia) {
			sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		}
		if (Uteis.isAtributoPreenchido(periodicidade) && (!periodicidade.equals("IN"))) {
			executarDefinicaoPeriodicidadeCursoFiltroAnoSemestreRealizarConsulta(sqlStr, ano, semestre, periodicidade);	
		}
		if(Uteis.isAtributoPreenchido(periodicidade) && periodicidade.equals("IN")) {
			sqlStr.append(" AND curso.periodicidade = 'IN' ");
		}
		sqlStr.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		sqlStr.append(" ORDER BY unidadeensino.nome, curso.nome, turno.nome, identificadorturma, pessoanome");
		return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
	}

	private AlunosPorUnidadeCursoTurmaRelVO montarDados(SqlRowSet dadosSQL, Boolean trazerFiliacao) throws Exception {
		AlunosPorUnidadeCursoTurmaRelVO obj = new AlunosPorUnidadeCursoTurmaRelVO();
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeensino_nome"));
		obj.setCurso(dadosSQL.getString("curso_nome"));
		obj.setTurno(dadosSQL.getString("turno_nome"));
		obj.setTurma(dadosSQL.getString("identificadorturma"));
//		obj.setDisciplina(dadosSQL.getString("disciplina"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setSituacaoMatriculaPeriodo(dadosSQL.getString("situacaoMatriculaPeriodo"));
		obj.setNomeAluno(dadosSQL.getString("pessoanome"));
		obj.setCodigoAluno(dadosSQL.getInt("pessoa_codigo"));
		obj.setDataNascimento(Uteis.getData(dadosSQL.getDate("dataNasc")));
		obj.setTelefoneRes(dadosSQL.getString("telefoneRes"));
		obj.setCelular(dadosSQL.getString("celular"));
		obj.setCpf(dadosSQL.getString("cpf"));
		obj.setEmail(dadosSQL.getString("email"));
		obj.setRg(dadosSQL.getString("rg"));
		obj.setOrgaoEmissor(dadosSQL.getString("orgaoEmissor"));
		obj.setEstadoEmissaoRg(dadosSQL.getString("estadoemissaorg"));
		obj.setDataEmissaoRg(Uteis.getData(dadosSQL.getDate("dataEmissaoRg"), "dd/MM/yyyy"));
		obj.setEndereco(dadosSQL.getString("endereco"));
		obj.setSetor(dadosSQL.getString("setor"));
		obj.setCep(dadosSQL.getString("cep"));
		obj.setCidade(dadosSQL.getString("cidade"));
		obj.setTurmaBase(dadosSQL.getString("turmabase"));
		obj.setTurmaPratica(dadosSQL.getString("turmapratica"));
		obj.setTurmaTeorica(dadosSQL.getString("turmateorica"));
		obj.setTurmaReposicao(dadosSQL.getString("identificadorturma"));
//		obj.setDisciplinaReposicao(dadosSQL.getString("disciplina"));

		obj.setAlunoAbandonouCurso(dadosSQL.getBoolean("alunoabandonoucurso"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getBoolean("alunoTransferidoUnidade")) && dadosSQL.getBoolean("alunoTransferidoUnidade")) {
			obj.setSituacao("TU");
		}
		if (trazerFiliacao) {
			obj.setFiliacaoVOs(getFacadeFactory().getFiliacaoFacade().consultarPorCodigoPessoaTipo(obj.getCodigoAluno(), "", false, null));
		}
		return obj;
	}

	public String getDesignIReportRelatorioPerfilTurma() {
		return getCaminhoBaseRelatorio() + getIdEntidadePerfilTurmaRel() + ".jrxml";
	}

	public static String getDesignIReportRelatorio() {
		return getCaminhoBaseRelatorio() + getIdEntidade() + ".jrxml";
	}

	public static String getDesignIReportRelatorioExcel() {
		return getCaminhoBaseRelatorio() + getIdEntidadeExcel() + ".jrxml";
	}

	public static String getDesignIReportRelatorioSintetico() {
		return getCaminhoBaseRelatorio() + getIdEntidadeSintetico() + ".jrxml";
	}

	public static String getDesignIReportRelatorioSinteticoExcel() {
		return getCaminhoBaseRelatorio() + getIdEntidadeSinteticoExcel() + ".jrxml";
	}

	public static String getCaminhoBaseRelatorio() {
		return "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator;
	}

	public String getIdEntidadePerfilTurmaRel() {
		return ("PerfilTurmaRel");
	}

	public static String getIdEntidade() {
		return ("AlunosPorUnidadeCursoTurmaRel");
	}

	public static String getIdEntidadeSintetico() {
		return ("AlunosPorUnidadeCursoTurmaSinteticoRel");
	}

	public static String getIdEntidadeExcel() {
		return ("AlunosPorUnidadeCursoTurmaRelExcel");
	}

	public static String getIdEntidadeSinteticoExcel() {
		return ("AlunosPorUnidadeCursoTurmaSinteticoRelExcel");
	}

	private void executarDefinicaoPeriodicidadeCursoFiltroAnoSemestreRealizarConsulta(StringBuilder sqlStr, String ano, String semestre, String periodicidade) throws Exception {
		if (Uteis.isAtributoPreenchido(periodicidade) && periodicidade.equals("SE")) {
			sqlStr.append(" and curso.periodicidade = 'SE' ");
			sqlStr.append(" AND MatriculaPeriodo.ano = '").append(ano).append("'");
			sqlStr.append(" AND MatriculaPeriodo.semestre = '").append(semestre).append("'");
		}
		if(Uteis.isAtributoPreenchido(periodicidade) && periodicidade.equals("AN")) {
			sqlStr.append("and curso.periodicidade = 'AN' ");
			sqlStr.append(" AND MatriculaPeriodo.ano = '").append(ano).append("'");
		}
	}

	private String adicionarFiltroUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		StringBuilder sql = new StringBuilder("");
		if(Uteis.isAtributoPreenchido(unidadeEnsinoVOs )){
		sql.append(" AND unidadeensino.codigo in (0");
		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				sql.append(", ").append(unidadeEnsinoVO.getCodigo());
			}
		}
		sql.append(") ");
		}
		return sql.toString();
	}

	private String adicionarFiltroCurso(List<CursoVO> cursoVOs) {
		boolean encontrado = false;
		StringBuilder sql = new StringBuilder("");
		if(Uteis.isAtributoPreenchido(cursoVOs )){
		sql.append(" AND curso.codigo in (0");
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
		}
		return sql.toString();
	}

	private String adicionarFiltroTurno(List<TurnoVO> turnoVOs) {
		boolean encontrado = false;
		StringBuilder sql = new StringBuilder("");
		if(Uteis.isAtributoPreenchido(turnoVOs )){
		sql.append(" AND turno.codigo in (0");
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
		}
		return sql.toString();
	}

	private void adicionarFiltroCalouroVeterano(StringBuilder sql, String situacaoAlunoCurso) throws Exception {
		if (situacaoAlunoCurso.equals("veterano")) {
			sql.append(" and (0 < (select count(matper2.codigo) from matriculaperiodo matper2 ");
			sql.append(" where matper2.matricula = matriculaperiodo.matricula  and matper2.situacaomatriculaperiodo != 'PC' ");
			sql.append(" and case when curso.periodicidade = 'IN' then matper2.data < matriculaperiodo.data else (matper2.ano||'/'||matper2.semestre) < (matriculaperiodo.ano||'/'||matriculaperiodo.semestre) end)) ");
		} else if (situacaoAlunoCurso.equals("calouro")) {
			sql.append(" and ( formaingresso not in ('TI' , 'TE') ");
			sql.append(" and matricula.matricula not in (select distinct matricula from transferenciaentrada where tipotransferenciaentrada in ('IN', 'EX') and matricula is not null )  ");
			sql.append(" and  0 = (select count(matper2.codigo) from matriculaperiodo matper2 where matper2.matricula = matriculaperiodo.matricula  ");
			sql.append(" and matper2.situacaomatriculaperiodo != 'PC' and case when curso.periodicidade = 'IN' then matper2.data < matriculaperiodo.data else (matper2.ano||'/'||matper2.semestre) < (matriculaperiodo.ano||'/'||matriculaperiodo.semestre) end ");
			sql.append(" ) ) ");
		}
	}
	public void validarDadosAnoSemestre(PeriodicidadeEnum periodicidadeEnum, String ano, String semestre, String filtrarPor) throws Exception {
		
		if (periodicidadeEnum.equals(PeriodicidadeEnum.SEMESTRAL) && semestre.trim().isEmpty()) {
			throw new ConsistirException("O campo SEMESTRE deve ser informado.");
		}
		if (!periodicidadeEnum.equals(PeriodicidadeEnum.INTEGRAL) && (ano.trim().isEmpty() || ano.length() != 4)) {
			throw new ConsistirException("O campo ANO deve ser informado.");
		}
	}
}