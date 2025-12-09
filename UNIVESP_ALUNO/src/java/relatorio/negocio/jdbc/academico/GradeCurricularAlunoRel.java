package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.GradeCurricularAlunoDisciplinaRelVO;
import relatorio.negocio.comuns.academico.GradeCurricularAlunoRelVO;
import relatorio.negocio.interfaces.academico.GradeCurricularAlunoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class GradeCurricularAlunoRel extends SuperRelatorio implements GradeCurricularAlunoRelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8947811112254235344L;

	public GradeCurricularAlunoRel() {
	}

	public void validarDados(MatriculaVO matriculaVO, TurmaVO turmaVO, String ano, String semestre, boolean filtrarPorAluno) throws Exception {
		if (filtrarPorAluno) {
			if (matriculaVO == null || matriculaVO.getMatricula().equals("")) {
				throw new ConsistirException("Por Favor informe uma matrícula para a geração do relatório.");
			}
		} else {
			if (turmaVO == null || turmaVO.getCodigo().equals(0)) {
				throw new ConsistirException("Por Favor informe uma turma para a geração do relatório.");
			}
			if(turmaVO.getIntegral() && (ano == null || ano.trim().isEmpty())){
				throw new ConsistirException("O campos ANO deve ser informado.");
			}
			if(turmaVO.getIntegral() && (ano.length() != 4)){
				throw new ConsistirException("O campos ANO deve ser informado com 4 dígitos.");
			}
			if(turmaVO.getSemestral() && (!semestre.equals("1") && !semestre.equals("2"))){
				throw new ConsistirException("O campos SEMESTRE deve ser informado.");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.GradeCurricularAlunoRelInterfaceFacade
	 * #getApresentarCampos()
	 */
	public GradeCurricularAlunoRelVO criarObjeto(GradeCurricularAlunoRelVO gradeCurricularAlunoRelVO, Boolean apresentarTodasDisciplinasGrade, MatriculaVO matriculaVO, String nomeTurma, String layout) throws Exception {
		GradeCurricularVO gradeCurricularVO = new GradeCurricularVO();
		gradeCurricularVO = getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularDaUltimaMatriculaPeriodo(matriculaVO.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
		try {
			if (apresentarTodasDisciplinasGrade) {
				return executarConsultaParametrizadaTodasDisciplinasGrade(gradeCurricularAlunoRelVO, matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), nomeTurma);
			}
			return executarConsultaParametrizada(gradeCurricularAlunoRelVO, matriculaVO.getMatricula(), gradeCurricularVO.getCodigo(), nomeTurma);
		} finally {
			gradeCurricularVO = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.GradeCurricularAlunoRelInterfaceFacade
	 * #executarConsultaParametrizada()
	 */
	public GradeCurricularAlunoRelVO executarConsultaParametrizada(GradeCurricularAlunoRelVO gradeCurricularAlunoRelVO, String matricula, Integer grade, String nomeTurma) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT DISTINCT ON (d.codigo) c.nome AS nomeCurso, (select distinct nome from gradecurricular where codigo = ").append(grade).append(") AS gradeCurricularNome, m.matricula, d.nome AS nomeDisc, d.abreviatura AS disciplinaAbreviatura, p.nome AS nomeAluno, p.cpf, p.rg, h.mediafinal, gd.cargahoraria, mp.ano, mp.semestre, ");
		// sqlStr.append("CASE WHEN h.situacao = 'AA' OR h.situacao = 'AP' then 'CURSADA' else  ");
		// sqlStr.append("CASE WHEN h.situacao = 'RE' OR h.situacao = 'RF' OR h.situacao = 'TR' then 'A CURSAR' else  ");
		// sqlStr.append("'CURSANDO' end end as situacao, h.freguencia ");
		sqlStr.append("h.situacao, h.freguencia, pl.periodoletivo as periodoletivo, pl.descricao AS periodoletivoDescricao, h.apresentarAprovadoHistorico ");
		sqlStr.append("FROM historico h ");
		sqlStr.append("INNER JOIN disciplina d ON h.disciplina = d.codigo AND 1 = (SELECT COUNT(codigo) FROM historico WHERE historico.disciplina = d.codigo AND historico.matricula = '").append(matricula).append("') ");
		sqlStr.append("inner join matriculaperiodo mp on mp.codigo = h.matriculaperiodo ");
		sqlStr.append("inner join gradedisciplina gd on gd.codigo = h.gradedisciplina ");
		sqlStr.append("INNER JOIN periodoletivo pl ON gd.periodoletivo = pl.codigo ");
		sqlStr.append("INNER JOIN matricula m ON h.matricula = m.matricula ");
		sqlStr.append("INNER JOIN curso c ON m.curso = c.codigo ");
		sqlStr.append("INNER JOIN pessoa p ON m.aluno = p.codigo ");
		// sqlStr.append("LEFT JOIN matriculaperiodoturmadisciplina mptd ON mptd.matriculaperiodo = h.matriculaperiodo AND mptd.disciplina = h.disciplina ");
		sqlStr.append("WHERE h.matricula = '").append(matricula).append("' AND d.codigo in  ");
		sqlStr.append("( ");
		sqlStr.append("SELECT d1.codigo FROM gradedisciplina gd ");
		sqlStr.append("LEFT join disciplina d1 ON gd.disciplina = d1.codigo ");
		sqlStr.append("INNER JOIN periodoletivo pl ON gd.periodoletivo = pl.codigo ");
		sqlStr.append("INNER JOIN gradecurricular gc ON pl.gradecurricular = gc.codigo ");
		sqlStr.append("WHERE gc.codigo = ").append(grade).append(" ORDER by d1.nome ");
		sqlStr.append(")  ");
		sqlStr.append(" ");
		sqlStr.append("UNION ALL ");
		sqlStr.append(" ");
		sqlStr.append("SELECT DISTINCT ON (d.codigo) c.nome AS nomeCurso, (select distinct nome from gradecurricular where codigo = ").append(grade).append(") AS gradeCurricularNome, m.matricula, d.nome AS nomeDisc, d.abreviatura AS disciplinaAbreviatura, p.nome AS nomeAluno, p.cpf, p.rg, h.mediafinal, gd.cargahoraria, mp.ano, mp.semestre, ");
		// sqlStr.append("CASE WHEN h.situacao = 'AA' OR h.situacao = 'AP' then 'CURSADA' else  ");
		// sqlStr.append("CASE WHEN h.situacao = 'RE' OR h.situacao = 'RF' OR h.situacao = 'TR' then 'A CURSAR' else  ");
		// sqlStr.append("'CURSANDO' END END AS situacao, h.freguencia ");
		sqlStr.append("'A Cursar' as situacao, h.freguencia, pl.periodoletivo as periodoletivo, pl.descricao AS periodoletivoDescricao, h.apresentarAprovadoHistorico  ");
		sqlStr.append("FROM historico h ");
		sqlStr.append("INNER JOIN disciplina d ON h.disciplina = d.codigo ");
		sqlStr.append("inner join matriculaperiodo mp on mp.codigo = h.matriculaperiodo ");
		sqlStr.append("inner join gradedisciplina gd on gd.codigo = h.gradedisciplina ");
		sqlStr.append("INNER JOIN periodoletivo pl ON gd.periodoletivo = pl.codigo ");
		sqlStr.append("INNER JOIN matricula m ON h.matricula = m.matricula ");
		sqlStr.append("INNER JOIN curso c ON m.curso = c.codigo ");
		sqlStr.append("INNER JOIN pessoa p ON m.aluno = p.codigo ");
		// sqlStr.append("LEFT JOIN matriculaperiodoturmadisciplina mptd ON mptd.matriculaperiodo = h.matriculaperiodo AND mptd.disciplina = h.disciplina ");
		sqlStr.append("LEFT join historico h2 ON h2.codigo <> h.codigo AND h.disciplina = h2.disciplina  ");
		sqlStr.append("WHERE h.matricula = '").append(matricula).append("' AND h2.matricula = '").append(matricula).append("' AND h.dataRegistro > h2.dataRegistro AND d.codigo in  ");
		sqlStr.append("( ");
		sqlStr.append("SELECT d1.codigo FROM gradedisciplina gd ");
		sqlStr.append("LEFT join disciplina d1 ON gd.disciplina = d1.codigo ");
		sqlStr.append("INNER JOIN periodoletivo pl ON gd.periodoletivo = pl.codigo ");
		sqlStr.append("INNER JOIN gradecurricular gc ON pl.gradecurricular = gc.codigo ");
		sqlStr.append("WHERE gc.codigo = ").append(grade).append(" ORDER by d1.nome ");
		sqlStr.append(") ");

		return (montarDados(gradeCurricularAlunoRelVO, getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), nomeTurma));
	}

	public GradeCurricularAlunoRelVO executarConsultaParametrizadaTodasDisciplinasGrade(GradeCurricularAlunoRelVO gradeCurricularAlunoRelVO, String matricula, Integer grade, String nomeTurma) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT * FROM ((SELECT DISTINCT gc.nome AS gradeCurricularNome, d.codigo, c.nome AS nomeCurso, m.matricula, d.nome AS nomeDisc, d.abreviatura AS disciplinaAbreviatura, p.nome AS nomeAluno, p.cpf, p.rg, h.mediafinal, gd.cargahoraria, mp.ano, mp.semestre, ");
		// sqlStr.append("CASE WHEN h.situacao = 'AA' OR h.situacao = 'AP' then 'CURSADA' else  CASE WHEN h.situacao = 'RE' OR h.situacao = 'RF' OR h.situacao = 'TR' then 'A CURSAR' else  'CURSANDO' end end as situacao, ");
		sqlStr.append("h.situacao, h.freguencia, pl.periodoletivo as periodoletivo, pl.descricao AS periodoletivoDescricao, h.apresentarAprovadoHistorico ");
		sqlStr.append("FROM gradedisciplina gd  ");
		sqlStr.append("INNER join disciplina d ON gd.disciplina = d.codigo ");
		sqlStr.append("INNER JOIN periodoletivo pl ON gd.periodoletivo = pl.codigo ");
		sqlStr.append("INNER JOIN gradecurricular gc ON pl.gradecurricular = gc.codigo ");
		sqlStr.append("INNER JOIN historico h ON h.disciplina = d.codigo ");
		sqlStr.append("INNER JOIN matricula m ON h.matricula = m.matricula ");
		sqlStr.append("INNER JOIN pessoa p ON m.aluno = p.codigo ");
		sqlStr.append("INNER JOIN curso c ON m.curso = c.codigo ");
		// sqlStr.append("LEFT JOIN matriculaperiodoturmadisciplina mptd ON mptd.matriculaperiodo = h.matriculaperiodo AND mptd.disciplina = h.disciplina ");
		sqlStr.append("inner join matriculaperiodo mp on mp.codigo = h.matriculaperiodo ");
		sqlStr.append("WHERE gc.codigo = ").append(grade).append(" AND m.matricula = '").append(matricula).append("' AND h.situacao <> 'TR' ORDER BY d.nome) ");
		sqlStr.append("UNION ALL ");
		sqlStr.append("(SELECT DISTINCT gc.nome AS gradeCurricularNome, d.codigo, c.nome AS nomeCurso, '', d.nome AS nomeDisc, d.abreviatura AS disciplinaAbreviatura, '', '', '', 0.0, gd.cargahoraria, '', '',  ");
		sqlStr.append("'A Cursar' as situacao, 0.0, pl.periodoletivo as periodoletivo, pl.descricao AS periodoletivoDescricao, false as apresentarAprovadoHistorico  ");
		sqlStr.append("FROM gradedisciplina gd ");
		sqlStr.append("INNER join disciplina d ON gd.disciplina = d.codigo ");
		sqlStr.append("INNER JOIN periodoletivo pl ON gd.periodoletivo = pl.codigo ");
		sqlStr.append("INNER JOIN gradecurricular gc ON pl.gradecurricular = gc.codigo ");
		sqlStr.append("INNER JOIN curso AS c ON  gc.curso = c.codigo ");
		sqlStr.append("WHERE gc.codigo = " + grade + " AND d.codigo not in ");
		sqlStr.append("(SELECT d.codigo FROM gradedisciplina gd ");
		sqlStr.append("INNER join disciplina d ON gd.disciplina = d.codigo   ");
		sqlStr.append("INNER JOIN periodoletivo pl ON gd.periodoletivo = pl.codigo ");
		sqlStr.append("INNER JOIN gradecurricular gc ON pl.gradecurricular = gc.codigo ");
		sqlStr.append("INNER JOIN historico h ON h.disciplina = d.codigo ");
		sqlStr.append("INNER JOIN matricula m ON h.matricula = m.matricula ");
		sqlStr.append("WHERE gc.codigo = ").append(grade).append(" ");
		sqlStr.append("AND m.matricula = '").append(matricula).append("'))) AS t ORDER BY t.ano DESC ");

		return (montarDados(gradeCurricularAlunoRelVO, getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), nomeTurma));
	}

	public static GradeCurricularAlunoRelVO montarDados(GradeCurricularAlunoRelVO obj, SqlRowSet dadosSQL, String nomeTurma) throws Exception {
		if (dadosSQL.next()) {
			obj.setCursoNome(dadosSQL.getString("nomeCurso"));
			obj.setMatricula(dadosSQL.getString("matricula"));
			obj.setAlunoNome(dadosSQL.getString("nomeAluno"));
			obj.setCpf(dadosSQL.getString("cpf"));
			obj.setRg(dadosSQL.getString("rg"));
			obj.setNomeTurma(nomeTurma);
			obj.setGradeCurricularNome(dadosSQL.getString("gradeCurricularNome"));
			do {
				GradeCurricularAlunoDisciplinaRelVO gradeCurricularAlunoDisciplinaRelVO = new GradeCurricularAlunoDisciplinaRelVO();
				gradeCurricularAlunoDisciplinaRelVO.setSemestre(dadosSQL.getString("semestre"));
				gradeCurricularAlunoDisciplinaRelVO.setAno(dadosSQL.getString("ano"));
				gradeCurricularAlunoDisciplinaRelVO.setSituacao(SituacaoHistorico.getDescricao(dadosSQL.getString("situacao")));
				gradeCurricularAlunoDisciplinaRelVO.setMedia(dadosSQL.getDouble("mediaFinal"));
				gradeCurricularAlunoDisciplinaRelVO.setFrequencia(dadosSQL.getDouble("freguencia"));
				gradeCurricularAlunoDisciplinaRelVO.setDisciplinaNome(dadosSQL.getString("nomeDisc"));
				gradeCurricularAlunoDisciplinaRelVO.setDisciplinaAbreviatura(dadosSQL.getString("disciplinaAbreviatura"));
				gradeCurricularAlunoDisciplinaRelVO.setDisciplinaCargaHoraria(dadosSQL.getInt("cargaHoraria"));
				gradeCurricularAlunoDisciplinaRelVO.setPeriodoLetivoDescricao(dadosSQL.getString("periodoletivoDescricao"));
				gradeCurricularAlunoDisciplinaRelVO.setPeriodoLetivo(dadosSQL.getInt("periodoLetivo"));
				gradeCurricularAlunoDisciplinaRelVO.setApresentarAprovadoHistorico(dadosSQL.getBoolean("apresentarAprovadoHistorico"));
				obj.getListaGradeCurricularAlunoDisciplinas().add(gradeCurricularAlunoDisciplinaRelVO);
				if (dadosSQL.isLast()) {
					return obj;
				}
			} while (dadosSQL.next());
			return obj;
		} else {
			return null;
		}
	}

	@Override
	public List<GradeCurricularAlunoRelVO> realizarGeracaoRelatorio(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Boolean filtrarPorAluno, MatriculaVO matricula, TurmaVO turmaVO, String ano, String semestre, String layout, String ordenarPor, Boolean apresentarTodasDisciplinasGrade, FuncionarioVO funcionarioPrincipal, CargoVO cargoFuncionarioPrincipal, FuncionarioVO funcionarioSecundario, CargoVO cargoFuncionarioSecundario,  UnidadeEnsinoVO unidadeEnsinoLogado, boolean controlarAcesso, UsuarioVO usuarioLogado) throws Exception {
		emitirRelatorio(getIdEntidade(), controlarAcesso, usuarioLogado);
		validarDados(matricula, turmaVO, ano, semestre, filtrarPorAluno);
		String clausulaWhere = realizarGeracaoClausulaWhere(filtroRelatorioAcademicoVO, apresentarTodasDisciplinasGrade, filtrarPorAluno, matricula.getMatricula(), turmaVO, ano, semestre, unidadeEnsinoLogado);
		String joinMatriculaPeriodo = realizarGeracaoJoinMatriculaPeriodo(filtrarPorAluno, turmaVO, ano, semestre);
		String joinHistorico = realizarGeracaoJoinHistorico();
		StringBuilder sql = new StringBuilder("select * from ( ");
		sql.append(realizarGeracaoSelectGradeDisciplina(joinMatriculaPeriodo, joinHistorico, clausulaWhere));
		if (layout.equals("layout2")) {
			sql.append(" union ");
			sql.append(realizarGeracaoSelectGradeDisciplinaComposta(joinMatriculaPeriodo, joinHistorico, clausulaWhere));
			sql.append(" union ");
			sql.append(realizarGeracaoSelectGrupoOptaticaDisciplina(joinMatriculaPeriodo, joinHistorico, clausulaWhere));
			sql.append(" union ");
			sql.append(realizarGeracaoSelectGrupoOptaticaDisciplinaComposta(joinMatriculaPeriodo, joinHistorico, clausulaWhere));
		}
		sql.append(") as t ");
		sql.append(realizarGeracaoOrdeBy(layout, ordenarPor));		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(rs, layout, funcionarioPrincipal, cargoFuncionarioPrincipal, funcionarioSecundario, cargoFuncionarioSecundario);
	}

	public String realizarGeracaoOrdeBy(String layout, String ordenacao) {
		StringBuilder sql  = new StringBuilder(" order by cursoNome, alunoNome ");
		if (layout.equals("layout2")) {
			sql.append(", periodoletivo, descricaoPeriodoLetivo, ordem, disciplinaPrincipalNome||disciplinaPrincipal::VARCHAR||disciplinaNome ");
		} else {
			if (ordenacao.equals("situacao")) {
				sql.append(", case when situacao in ('AA', 'IS', 'CC', 'CH', 'AP', 'AE', 'AD', 'AB') then 1 else case when situacao in ('CS', 'CE', 'CO') then 2 else 3 end end ,  periodoLetivo, ordem, disciplinaNome ");
			} else if (ordenacao.equals("periodoLetivo")) {
				sql.append(", periodoLetivo, ordem, disciplinaNome ");				
			} else {
				sql.append(", ano, semestre, periodoLetivo, ordem, disciplinaNome ");				
			}
		}
		return sql.toString();
	}

	public List<GradeCurricularAlunoRelVO> montarDadosConsulta(SqlRowSet rs, String layout, FuncionarioVO funcionarioPrincipal, CargoVO cargoFuncionarioPrincipal, FuncionarioVO funcionarioSecundario, CargoVO cargoFuncionarioSecundario ) throws Exception {
		List<GradeCurricularAlunoRelVO> gradeCurricularAlunoRelVOs = new ArrayList<GradeCurricularAlunoRelVO>(0);
		GradeCurricularAlunoRelVO gradeCurricularAlunoRelVO = null; 
        String matricula = "";
        Map<Integer, GradeCurricularAlunoDisciplinaRelVO> periodoLetivoCargaHorariaCumprido = null;
        Map<Integer, GradeCurricularAlunoDisciplinaRelVO> disciplinaCompostaVOs = null;
        while(rs.next()){
        	if(matricula.trim().isEmpty() || !matricula.equals(rs.getString("matricula"))){
        		gradeCurricularAlunoRelVO = montarDadosGradeCurricularAlunoRelVO(rs, layout, funcionarioPrincipal, cargoFuncionarioPrincipal, funcionarioSecundario, cargoFuncionarioSecundario);
        		matricula = rs.getString("matricula");
        		periodoLetivoCargaHorariaCumprido = new HashMap<Integer, GradeCurricularAlunoDisciplinaRelVO>(0);
        		disciplinaCompostaVOs = new HashMap<Integer, GradeCurricularAlunoDisciplinaRelVO>(0);
        		gradeCurricularAlunoRelVOs.add(gradeCurricularAlunoRelVO);
        	}
        	GradeCurricularAlunoDisciplinaRelVO gradeCurricularAlunoDisciplinaRelVO = montarDadosGradeCurricularAlunoDisciplinaRelVO(rs);
			if (!gradeCurricularAlunoDisciplinaRelVO.getDisciplinaFazParteComposicao()) {
				if(gradeCurricularAlunoDisciplinaRelVO.getDisciplinaComposta()){
					disciplinaCompostaVOs.put(gradeCurricularAlunoDisciplinaRelVO.getDisciplina(), gradeCurricularAlunoDisciplinaRelVO);
				}
				if (!periodoLetivoCargaHorariaCumprido.containsKey(gradeCurricularAlunoDisciplinaRelVO.getPeriodoLetivo())) {
					periodoLetivoCargaHorariaCumprido.put(gradeCurricularAlunoDisciplinaRelVO.getPeriodoLetivo(), gradeCurricularAlunoDisciplinaRelVO);
				}
				if (!gradeCurricularAlunoDisciplinaRelVO.getOptativa()) {
					gradeCurricularAlunoRelVO.setCargaHorariaObrigatoriaTotal(gradeCurricularAlunoRelVO.getCargaHorariaObrigatoriaTotal() + gradeCurricularAlunoDisciplinaRelVO.getDisciplinaCargaHoraria());
					if (gradeCurricularAlunoDisciplinaRelVO.getIsAprovado()) {
						gradeCurricularAlunoRelVO.setCargaHorariaObrigatoriaTotalCumprida(gradeCurricularAlunoRelVO.getCargaHorariaObrigatoriaTotalCumprida() + gradeCurricularAlunoDisciplinaRelVO.getDisciplinaCargaHoraria());
					}
				} else if (gradeCurricularAlunoDisciplinaRelVO.getOptativa() && gradeCurricularAlunoDisciplinaRelVO.getIsAprovado()) {
					gradeCurricularAlunoRelVO.setCargaHorariaOptativaObrigatoriaTotalCumprida(gradeCurricularAlunoRelVO.getCargaHorariaOptativaObrigatoriaTotalCumprida() + gradeCurricularAlunoDisciplinaRelVO.getDisciplinaCargaHoraria());
				}

				if (gradeCurricularAlunoDisciplinaRelVO.getIsAprovado()) {
					periodoLetivoCargaHorariaCumprido.get(gradeCurricularAlunoDisciplinaRelVO.getPeriodoLetivo()).setCargaHorariaCumpridaTotalPeriodo(periodoLetivoCargaHorariaCumprido.get(gradeCurricularAlunoDisciplinaRelVO.getPeriodoLetivo()).getCargaHorariaCumpridaTotalPeriodo() + gradeCurricularAlunoDisciplinaRelVO.getDisciplinaCargaHoraria());				
				}
				gradeCurricularAlunoDisciplinaRelVO.setCargaHorariaCumpridaTotalPeriodo(periodoLetivoCargaHorariaCumprido.get(gradeCurricularAlunoDisciplinaRelVO.getPeriodoLetivo()).getCargaHorariaCumpridaTotalPeriodo());
				gradeCurricularAlunoRelVO.getListaGradeCurricularAlunoDisciplinas().add(gradeCurricularAlunoDisciplinaRelVO);
			}else{
				gradeCurricularAlunoDisciplinaRelVO.setHistoricoPorEquivalencia(disciplinaCompostaVOs.get(rs.getInt("disciplinaPrincipal")).getHistoricoPorEquivalencia());
				gradeCurricularAlunoDisciplinaRelVO.setApresentarAprovadoHistorico(disciplinaCompostaVOs.get(rs.getInt("disciplinaPrincipal")).getApresentarAprovadoHistorico());
				if(gradeCurricularAlunoDisciplinaRelVO.getHistoricoPorEquivalencia() 
						|| (gradeCurricularAlunoDisciplinaRelVO.getSituacao().equals("A Cursar") 
								&& (disciplinaCompostaVOs.get(rs.getInt("disciplinaPrincipal")).getSituacao().equals("Aprovado") 
										|| disciplinaCompostaVOs.get(rs.getInt("disciplinaPrincipal")).getSituacao().equals("Aprov. por Aprov.")
										|| disciplinaCompostaVOs.get(rs.getInt("disciplinaPrincipal")).getSituacao().equals("Aprov. por Equiv.")
										|| disciplinaCompostaVOs.get(rs.getInt("disciplinaPrincipal")).getSituacao().equals("Aprov. c/ Dep.")
										|| disciplinaCompostaVOs.get(rs.getInt("disciplinaPrincipal")).getSituacao().equals("Cursando")))){
					gradeCurricularAlunoDisciplinaRelVO.setSituacao("--");
					gradeCurricularAlunoDisciplinaRelVO.setMedia(null);
					gradeCurricularAlunoDisciplinaRelVO.setAno("");
					gradeCurricularAlunoDisciplinaRelVO.setSemestre("");
				}
				disciplinaCompostaVOs.get(rs.getInt("disciplinaPrincipal")).getDisciplinaComposicaoVOs().add(gradeCurricularAlunoDisciplinaRelVO);
			}
        }
        alterarSituacaoDisciplinasOptativasLayout2(gradeCurricularAlunoRelVOs);
		return gradeCurricularAlunoRelVOs;
	}

	public GradeCurricularAlunoRelVO montarDadosGradeCurricularAlunoRelVO(SqlRowSet dadosSQL, String layout, FuncionarioVO funcionarioPrincipal, CargoVO cargoFuncionarioPrincipal, FuncionarioVO funcionarioSecundario, CargoVO cargoFuncionarioSecundario) throws Exception {
		GradeCurricularAlunoRelVO obj = new GradeCurricularAlunoRelVO();
		obj.setCursoNome(dadosSQL.getString("cursonome"));
		obj.setPeriodicidade(dadosSQL.getString("periodicidade"));
		obj.setMatricula(dadosSQL.getString("matricula"));
		obj.setAlunoNome(dadosSQL.getString("alunonome"));
		obj.setCpf(dadosSQL.getString("cpf"));
		obj.setRg(dadosSQL.getString("rg"));
		obj.setNomeTurma(dadosSQL.getString("nometurma"));
		obj.setGradeCurricularNome(dadosSQL.getString("gradeCurricularNome"));
		obj.setGradeCurricularResolucao(dadosSQL.getString("gradeCurricularResolucao"));
		obj.setGradeCurricular(dadosSQL.getInt("gradeCurricular"));
		obj.setCargaHorariaTotal(dadosSQL.getInt("cargaHorariaTotal"));
		obj.setCargaHorariaEstagioTotal(dadosSQL.getInt("cargaHorariaEstagioTotal"));
		obj.setCargaHorariaAtividadeComplementarTotal(dadosSQL.getInt("cargaHorariaAtividadeComplementarTotal"));
		obj.setCargoFuncionarioPrincipal(cargoFuncionarioPrincipal);
		obj.setFuncionarioPrincipalVO(funcionarioPrincipal);
		obj.setFuncionarioSecundarioVO(funcionarioSecundario);
		obj.setCargoFuncionarioSecundario(cargoFuncionarioSecundario);
		if(layout.equals("layout2")){
		if(obj.getCargaHorariaEstagioTotal() > 0){
			obj.setCargaHorariaEstagioTotalCumprida(getFacadeFactory().getEstagioFacade().consultarCargaHorariaRealizadaEstagioMatricula(obj.getMatricula()));
		}
		if(obj.getCargaHorariaAtividadeComplementarTotal() > 0){
			obj.setCargaHorariaAtividadeComplementarTotalCumprida(getFacadeFactory().getRegistroAtividadeComplementarMatriculaFacade().consultarCargaHorariaRealizada(obj.getMatricula(), obj.getGradeCurricular(), false));
		}
		}
		return obj;
	}

	public GradeCurricularAlunoDisciplinaRelVO montarDadosGradeCurricularAlunoDisciplinaRelVO(SqlRowSet dadosSQL) throws Exception {		
		GradeCurricularAlunoDisciplinaRelVO gradeCurricularAlunoDisciplinaRelVO = new GradeCurricularAlunoDisciplinaRelVO();
		gradeCurricularAlunoDisciplinaRelVO.setSemestre(dadosSQL.getString("semestre"));
		gradeCurricularAlunoDisciplinaRelVO.setAno(dadosSQL.getString("ano"));
		gradeCurricularAlunoDisciplinaRelVO.setApresentarAprovadoHistorico(dadosSQL.getBoolean("apresentarAprovadoHistorico"));	
		if(dadosSQL.getString("situacao") == null || dadosSQL.getString("situacao").equals("AC") || dadosSQL.getString("situacao").equals("TR")  || dadosSQL.getString("situacao").equals("TS") || dadosSQL.getString("situacao").equals("CA")){
			gradeCurricularAlunoDisciplinaRelVO.setSituacao("A Cursar"); 
		}
		else{
			SituacaoHistorico situacaoHistorico = SituacaoHistorico.getEnum(dadosSQL.getString("situacao"));
			if (situacaoHistorico.getValor().equals("AA")) {
				gradeCurricularAlunoDisciplinaRelVO.setSituacao(dadosSQL.getBoolean("apresentarAprovadoHistorico") ?  "Aprovado" : "Aprov. por Aprov.");
			}
			if (situacaoHistorico.getValor().equals("AE")) {
				gradeCurricularAlunoDisciplinaRelVO.setSituacao(dadosSQL.getBoolean("apresentarAprovadoHistorico") ?  "Aprovado" : "Aprov. por Equiv.");
			}else if(situacaoHistorico.getHistoricoReprovado()){
				gradeCurricularAlunoDisciplinaRelVO.setSituacao("Reprovado");
			}else if(situacaoHistorico.getHistoricoCursando()){
				gradeCurricularAlunoDisciplinaRelVO.setSituacao(dadosSQL.getBoolean("historicoPorEquivalencia") ? "Curs. por Equiv." :"Cursando");
			}else if(situacaoHistorico.getValor().equals("AD")){
				gradeCurricularAlunoDisciplinaRelVO.setSituacao(dadosSQL.getBoolean("apresentarAprovadoHistorico") ? "Aprovado" : "Aprov. c/ Dep.");
			}else if(situacaoHistorico.getHistoricoAprovado()){ 
				gradeCurricularAlunoDisciplinaRelVO.setSituacao("Aprovado");
			}
			
		}		
		gradeCurricularAlunoDisciplinaRelVO.setMedia(dadosSQL.getObject("mediaFinal") != null ? dadosSQL.getDouble("mediaFinal") : null);
		gradeCurricularAlunoDisciplinaRelVO.setFrequencia(dadosSQL.getDouble("frequencia"));
		gradeCurricularAlunoDisciplinaRelVO.setDisciplinaNome(dadosSQL.getString("disciplinanome"));
		gradeCurricularAlunoDisciplinaRelVO.setDisciplinaAbreviatura(dadosSQL.getString("disciplinaAbreviatura"));
		gradeCurricularAlunoDisciplinaRelVO.setDisciplina(dadosSQL.getInt("disciplina"));
		gradeCurricularAlunoDisciplinaRelVO.setDisciplinaPrincipalNome(dadosSQL.getString("disciplinaPrincipalNome"));
		gradeCurricularAlunoDisciplinaRelVO.setDisciplinaPrincipal(dadosSQL.getInt("disciplinaPrincipal"));
		gradeCurricularAlunoDisciplinaRelVO.setDisciplinaComposta(dadosSQL.getBoolean("disciplinaComposta"));
		gradeCurricularAlunoDisciplinaRelVO.setDisciplinaCargaHoraria(dadosSQL.getInt("disciplinacargaHoraria"));
		gradeCurricularAlunoDisciplinaRelVO.setDisciplinaCredito(dadosSQL.getInt("disciplinaCredito"));
		gradeCurricularAlunoDisciplinaRelVO.setPeriodoLetivoDescricao(dadosSQL.getString("descricaoPeriodoletivo"));
		gradeCurricularAlunoDisciplinaRelVO.setCargaHorariaTotalPeriodo(dadosSQL.getInt("cargaHorariaTotalPeriodo"));
		gradeCurricularAlunoDisciplinaRelVO.setPeriodoLetivo(dadosSQL.getInt("periodoLetivo"));
		gradeCurricularAlunoDisciplinaRelVO.setOptativa(dadosSQL.getBoolean("optativa"));
		gradeCurricularAlunoDisciplinaRelVO.setDisciplinaGrupoOptativa(dadosSQL.getBoolean("disciplinaGrupoOptativa"));
		gradeCurricularAlunoDisciplinaRelVO.setDisciplinaFazParteComposicao(dadosSQL.getBoolean("disciplinaFazParteComposicao"));
		gradeCurricularAlunoDisciplinaRelVO.setHistoricoPorEquivalencia(dadosSQL.getBoolean("historicoPorEquivalencia"));		
			
		return gradeCurricularAlunoDisciplinaRelVO;
	}

	public String realizarGeracaoJoinHistorico() {
		StringBuilder sql = new StringBuilder("");
		sql.append(" left join historico on historico.matricula =  matricula.matricula ");
		sql.append(" and historico.matrizcurricular = gradecurricular.codigo ");
		sql.append(" and (historico.historicoequivalente = false or historico.historicoequivalente is null) ");
		sql.append(" and historico.disciplina = disciplina.codigo and historico.codigo = ( ");
		sql.append(" select his.codigo from historico his  ");
		sql.append(" inner join matriculaperiodo mp on mp.codigo = historico.matriculaperiodo  ");
		sql.append(" where his.matricula =  matricula.matricula ");
		sql.append(" and (his.historicoequivalente = false or his.historicoequivalente is null) ");
		sql.append(" and his.matrizcurricular = gradecurricular.codigo  ");
		sql.append(" and (his.situacao in ('AA', 'IS', 'CC', 'CH', 'AP', 'AE', 'CE', 'CO', 'AD', 'AB') or ");
		sql.append(" (his.situacao = 'CS' and mp.situacaomatriculaperiodo in ('PR', 'AT', 'FI', 'FO') )) ");
		sql.append(" and his.disciplina = disciplina.codigo order by (his.anohistorico ||'/'||his.semestrehistorico) desc, his.codigo desc limit 1  ");
		sql.append(" ) ");
		return sql.toString();
	}

	public String realizarGeracaoJoinMatriculaPeriodo(Boolean filtrarPorAluno, TurmaVO turmaVO, String ano, String semestre) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.matricula = matricula.matricula ");
		sql.append(" and matriculaperiodo.codigo = (select mp.codigo from matriculaperiodo mp ");
		sql.append(" where mp.matricula = matricula.matricula and mp.situacaomatriculaperiodo not in ('PC') ");
		if (!filtrarPorAluno) {
			if (turmaVO.getAnual() || turmaVO.getSemestral()) {
				sql.append(" and mp.ano = '").append(ano).append("' ");
			}
			if (turmaVO.getSemestral()) {
				sql.append(" and mp.semestre = '").append(semestre).append("' ");
			}
		}
		sql.append(" order by (mp.ano || '/' || mp.semestre) desc, ");
		sql.append(" case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc limit 1) ");
		return sql.toString();
	}

	public StringBuilder realizarGeracaoSelectGradeDisciplina(String joinMatriculaPeriodo, String joinHistorico, String clausulaWhere) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append("  select gradedisciplina.ordem, gradecurricular.nome as gradeCurricularNome, gradecurricular.codigo as gradeCurricular, gradeCurricular.resolucao as gradeCurricularResolucao, curso.nome as cursoNome, curso.periodicidade, matricula.matricula, ");
		sql.append("  pessoa.nome as alunoNome, pessoa.cpf, pessoa.rg, turma.identificadorturma as nomeTurma, ");
		sql.append("  gradecurricular.cargahoraria as cargaHorariaTotal, ");
		sql.append("  gradecurricular.totalcargahorariaestagio as cargaHorariaEstagioTotal,  ");
		sql.append("  gradecurricular.totalcargahorariaatividadecomplementar as cargaHorariaAtividadeComplementarTotal, ");
		sql.append("  periodoletivo.periodoletivo, case when nomecertificacao is null or  length(trim(nomecertificacao)) = 0 then periodoletivo.descricao else periodoletivo.nomecertificacao end as descricaoPeriodoLetivo, ");
		sql.append("  periodoletivo.totalcargahoraria as cargaHorariaTotalPeriodo,  ");
		sql.append("  case when historico.situacao in ('AA', 'IS', 'CH', 'CC', 'AE', 'AP') then gradedisciplina.cargahoraria else 0 end as cargaHorariaCumpridaPeriodo, ");
		sql.append("  disciplina.nome as disciplinaNome, disciplina.abreviatura as disciplinaAbreviatura, gradedisciplina.disciplinaComposta, 0 as disciplinaPrincipal, ''::VARCHAR(200) as disciplinaPrincipalNome, case when historico.codigo is null then 'AC' else historico.situacao end as situacao, historico.mediafinal, historico.freguencia as frequencia,  ");
		sql.append("  gradedisciplina.cargahoraria as disciplinaCargaHoraria, gradedisciplina.nrcreditos as disciplinaCredito, disciplina.codigo as disciplina,");
		sql.append("  historico.anohistorico as ano, historico.semestrehistorico as semestre, false as disciplinaFazParteComposicao, false as disciplinaGrupoOptativa,  ");
		sql.append("  gradedisciplina.tipodisciplina in ('LO', 'OP') as optativa, historico.historicoporequivalencia, historico.apresentarAprovadoHistorico ");
		sql.append("  from matricula ");
		sql.append("  inner join pessoa on matricula.aluno = pessoa.codigo ");
		sql.append("  inner join curso on curso.codigo = matricula.curso ");
		sql.append("  inner join gradecurricular on matricula.gradecurricularatual = gradecurricular.codigo ");
		sql.append("  inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo ");
		sql.append("  inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo ");
		sql.append("  inner join disciplina on gradedisciplina.disciplina = disciplina.codigo ");
		sql.append(joinMatriculaPeriodo);
		sql.append("  left join turma on turma.codigo = matriculaperiodo.turma ");
		sql.append(joinHistorico);
		sql.append(clausulaWhere);
		return sql;
	}

	public StringBuilder realizarGeracaoSelectGradeDisciplinaComposta(String joinMatriculaPeriodo, String joinHistorico, String clausulaWhere) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select gradedisciplina.ordem, gradecurricular.nome as gradeCurricularNome, gradecurricular.codigo as gradeCurricular, gradeCurricular.resolucao as gradeCurricularResolucao, curso.nome as cursoNome, curso.periodicidade, matricula.matricula,");
		sql.append(" pessoa.nome as alunoNome, pessoa.cpf, pessoa.rg, turma.identificadorturma as nomeTurma,");
		sql.append(" gradecurricular.cargahoraria as cargaHorariaTotal,");
		sql.append(" gradecurricular.totalcargahorariaestagio as cargaHorariaEstagioTotal, ");
		sql.append(" gradecurricular.totalcargahorariaatividadecomplementar as cargaHorariaAtividadeComplementarTotal,");
		sql.append(" periodoletivo.periodoletivo, case when nomecertificacao is null or  length(trim(nomecertificacao)) = 0 then periodoletivo.descricao else periodoletivo.nomecertificacao end as descricaoPeriodoLetivo,");
		sql.append(" periodoletivo.totalcargahoraria as cargaHorariaTotalPeriodo, ");
		// sql.append(" case when historico.situacao in ('AA', 'IS', 'CH', 'CC', 'AE', 'AP') then gradedisciplina.cargahoraria else 0 end as cargaHorariaCumpridaPeriodo,");
		sql.append(" 0 as cargaHorariaCumpridaPeriodo,");
		sql.append(" disciplina.nome as disciplinaNome, disciplina.abreviatura as disciplinaAbreviatura, false as disciplinaComposta, disciplinaPrincipal.codigo as disciplinaPrincipal, disciplinaPrincipal.nome as disciplinaPrincipalNome, historico.situacao, historico.mediafinal, historico.freguencia as frequencia, ");
		sql.append(" gradedisciplinacomposta.cargahoraria as disciplinaCargaHoraria, gradedisciplinacomposta.nrcreditos as disciplinaCredito, disciplina.codigo as disciplina,");
		sql.append(" historico.anohistorico as ano, historico.semestrehistorico as semestre, true as disciplinaFazParteComposicao, false as disciplinaGrupoOptativa, ");
		sql.append(" gradedisciplina.tipodisciplina in ('LO', 'OP') as optativa, historico.historicoporequivalencia, historico.apresentarAprovadoHistorico ");
		sql.append(" from matricula");
		sql.append(" inner join pessoa on matricula.aluno = pessoa.codigo");
		sql.append(" inner join curso on curso.codigo = matricula.curso");
		sql.append(" inner join gradecurricular on matricula.gradecurricularatual = gradecurricular.codigo");
		sql.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
		sql.append(" inner join gradedisciplina on gradedisciplina.periodoletivo = periodoletivo.codigo");
		sql.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradedisciplina = gradedisciplina.codigo");
		sql.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
		sql.append(" inner join disciplina disciplinaPrincipal on gradedisciplina.disciplina = disciplinaPrincipal.codigo");
		sql.append(joinMatriculaPeriodo);
		sql.append(" left join turma on turma.codigo = matriculaperiodo.turma");
		sql.append(joinHistorico);
		sql.append(clausulaWhere);

		return sql;
	}

	public StringBuilder realizarGeracaoSelectGrupoOptaticaDisciplina(String joinMatriculaPeriodo, String joinHistorico, String clausulaWhere) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct 0 as ordem, gradecurricular.nome as gradeCurricularNome, gradecurricular.codigo as gradeCurricular, gradeCurricular.resolucao as gradeCurricularResolucao, curso.nome as cursoNome, curso.periodicidade, matricula.matricula,");
		sql.append(" pessoa.nome as alunoNome, pessoa.cpf, pessoa.rg, turma.identificadorturma as nomeTurma,");
		sql.append(" gradecurricular.cargahoraria as cargaHorariaTotal,");
		sql.append(" gradecurricular.totalcargahorariaestagio as cargaHorariaEstagioTotal, ");
		sql.append(" gradecurricular.totalcargahorariaatividadecomplementar as cargaHorariaAtividadeComplementarTotal,");
		sql.append(" 1000 periodoletivo, gradecurriculargrupooptativa.descricao as descricaoPeriodoLetivo,");
		sql.append(" 0 as cargaHorariaTotalPeriodo, ");
		sql.append(" case when historico.situacao in ('AA', 'IS', 'CH', 'CC', 'AE', 'AP') then gradecurriculargrupooptativadisciplina.cargahoraria else 0 end as cargaHorariaCumpridaPeriodo,");
		sql.append(" disciplina.nome as disciplinaNome, disciplina.abreviatura as disciplinaAbreviatura, gradecurriculargrupooptativadisciplina.disciplinaComposta, 0 as disciplinaPrincipal, ''::VARCHAR(200) as disciplinaPrincipalNome, historico.situacao, historico.mediafinal, historico.freguencia as frequencia, ");
		sql.append(" gradecurriculargrupooptativadisciplina.cargahoraria as disciplinaCargaHoraria, gradecurriculargrupooptativadisciplina.nrcreditos as disciplinaCredito, disciplina.codigo as disciplina,");
		sql.append(" historico.anohistorico as ano, historico.semestrehistorico as semestre, false as disciplinaFazParteComposicao, true as disciplinaGrupoOptativa, ");
		sql.append(" true as optativa, historico.historicoporequivalencia, historico.apresentarAprovadoHistorico ");
		sql.append(" from matricula");
		sql.append(" inner join pessoa on matricula.aluno = pessoa.codigo");
		sql.append(" inner join curso on curso.codigo = matricula.curso");
		sql.append(" inner join gradecurricular on matricula.gradecurricularatual = gradecurricular.codigo");
		sql.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
		sql.append(" inner join gradecurriculargrupooptativa on periodoletivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
		sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
		sql.append(" inner join disciplina on gradecurriculargrupooptativadisciplina.disciplina = disciplina.codigo");
		sql.append(joinMatriculaPeriodo);
		sql.append(" left join turma on turma.codigo = matriculaperiodo.turma");
		sql.append(joinHistorico);
		sql.append(clausulaWhere);
		return sql;
	}

	public StringBuilder realizarGeracaoSelectGrupoOptaticaDisciplinaComposta(String joinMatriculaPeriodo, String joinHistorico, String clausulaWhere) throws Exception {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct 0 as ordem, gradecurricular.nome as gradeCurricularNome, gradecurricular.codigo as gradeCurricular, gradeCurricular.resolucao as gradeCurricularResolucao, curso.nome as cursoNome, curso.periodicidade, matricula.matricula,");
		sql.append(" pessoa.nome as alunoNome, pessoa.cpf, pessoa.rg, turma.identificadorturma as nomeTurma,");
		sql.append(" gradecurricular.cargahoraria as cargaHorariaTotal,");
		sql.append(" gradecurricular.totalcargahorariaestagio as cargaHorariaEstagioTotal, ");
		sql.append(" gradecurricular.totalcargahorariaatividadecomplementar as cargaHorariaAtividadeComplementarTotal,");
		sql.append(" 1000 periodoletivo, gradecurriculargrupooptativa.descricao as descricaoPeriodoLetivo,");
		sql.append(" 0 as cargaHorariaTotalPeriodo, ");
		// sql.append(" case when historico.situacao in ('AA', 'IS', 'CH', 'CC', 'AE', 'AP') then gradecurriculargrupooptativadisciplina.cargahoraria else 0 end as cargaHorariaCumpridaPeriodo,");
		sql.append(" 0 as cargaHorariaCumpridaPeriodo,");
		sql.append(" disciplina.nome as disciplinaNome, disciplina.abreviatura as disciplinaAbreviatura, false as disciplinaComposta, disciplinaPrincipal.codigo as disciplinaPrincipal, disciplinaPrincipal.nome as disciplinaPrincipalNome, historico.situacao, historico.mediafinal, historico.freguencia as frequencia, ");
		sql.append(" gradecurriculargrupooptativadisciplina.cargahoraria as disciplinaCargaHoraria, gradecurriculargrupooptativadisciplina.nrcreditos as disciplinaCredito, disciplina.codigo as disciplina,");
		sql.append(" historico.anohistorico as ano, historico.semestrehistorico as semestre, true as disciplinaFazParteComposicao, true as disciplinaGrupoOptativa, ");
		sql.append(" true as optativa, historico.historicoporequivalencia, historico.apresentarAprovadoHistorico ");
		sql.append(" from matricula");
		sql.append(" inner join pessoa on matricula.aluno = pessoa.codigo");
		sql.append(" inner join curso on curso.codigo = matricula.curso");
		sql.append(" inner join gradecurricular on matricula.gradecurricularatual = gradecurricular.codigo");
		sql.append(" inner join periodoletivo on periodoletivo.gradecurricular = gradecurricular.codigo");
		sql.append(" inner join gradecurriculargrupooptativa on periodoletivo.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
		sql.append(" inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.gradecurriculargrupooptativa = gradecurriculargrupooptativa.codigo");
		sql.append(" inner join gradedisciplinacomposta on gradedisciplinacomposta.gradecurriculargrupooptativadisciplina = gradecurriculargrupooptativadisciplina.codigo");
		sql.append(" inner join disciplina on gradedisciplinacomposta.disciplina = disciplina.codigo");
		sql.append(" inner join disciplina disciplinaPrincipal on gradecurriculargrupooptativadisciplina.disciplina = disciplinaPrincipal.codigo");
		sql.append(joinMatriculaPeriodo);
		sql.append(" left join turma on turma.codigo = matriculaperiodo.turma");
		sql.append(joinHistorico);
		sql.append(clausulaWhere);
		return sql;
	}

	public String realizarGeracaoClausulaWhere(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Boolean trazerTodasDisciplinas, Boolean filtrarPorAluno, String matricula, TurmaVO turmaVO, String ano, String semestre, UnidadeEnsinoVO unidadeEnsinoVO) {
		StringBuilder sql = new StringBuilder("");
		if (filtrarPorAluno) {
			sql.append(" where matricula.matricula = '").append(matricula).append("'");
		} else {
			if (turmaVO.getTurmaAgrupada() && !turmaVO.getSubturma()) {
				sql.append(" where turma.codigo in (select turmaagrupada.turma from turmaagrupada where turmaagrupada.turmaorigem = ").append(turmaVO.getCodigo()).append(") ");			
			} else {
				sql.append(" where turma.codigo = ").append(turmaVO.getCodigo());
			}
			if (turmaVO.getAnual() || turmaVO.getSemestral()) {
				sql.append(" and matriculaperiodo.ano = '").append(ano).append("' ");
			}
			if (turmaVO.getSemestral()) {
				sql.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
			}
			sql.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
			sql.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		}
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCodigo())) {
			sql.append(" and matricula.unidadeEnsino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}
		if(!trazerTodasDisciplinas){
			sql.append(" and historico.situacao in ('AA', 'IS', 'CH', 'CC', 'AE', 'AP') ");
			sql.append(" and (historico.historicoequivalente = false or historico.historicoequivalente is null) ");
		}
		
		return sql.toString();
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("GradeCurricularAlunoRel");
	}

	private void alterarSituacaoDisciplinasOptativasLayout2(List<GradeCurricularAlunoRelVO> gradeCurricularAlunoRelVOs) {
		gradeCurricularAlunoRelVOs.forEach(gcarvo -> {
			gcarvo.getListaGradeCurricularAlunoDisciplinas().stream().filter(gcadrvo -> {
				return gcadrvo.getOptativa() || gcadrvo.getDisciplinaGrupoOptativa();
			}).forEach(gcadrvo -> {
				if (gcarvo.getCargaHorariaOptativaObrigatoriaTotalCumprida() >= gcarvo.getCargaHorariaOptativaObrigatoriaTotal() && gcadrvo.getSituacao().equals("A Cursar")) {
					gcadrvo.setSituacao("-----");
				}
			});
		});
	}
}
