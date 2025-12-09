package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import relatorio.negocio.comuns.academico.AlunosPorDisciplinasRelVO;
import relatorio.negocio.comuns.academico.NrAlunosMatriculadosVO;
import relatorio.negocio.interfaces.academico.AlunosPorDisciplinasRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class AlunosPorDisciplinasRel extends SuperRelatorio implements AlunosPorDisciplinasRelInterfaceFacade {

	public AlunosPorDisciplinasRel() {
	}

	public void validarDados(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs) throws Exception {
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
		if (!selecionado) {
			throw new ConsistirException("O Curso deve ser informado para a geração do relatório.");
		}
	}
	
//	@Override
//	public List<AlunosPorDisciplinasRelVO> criarObjeto(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs,
//			String ano, String semestre, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String lyout,
//			TurmaVO turma, DisciplinaVO disciplina, Integer turno, Integer gradeCurricular, UsuarioVO usuarioLogado,
//			Boolean trazerAlunoTransferencia) throws Exception {
//		validarDados(unidadeEnsinoVOs, cursoVOs);
//		AlunosPorDisciplinasRel.emitirRelatorio(getIdEntidade(), false, null);
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	public List<AlunosPorDisciplinasRelVO> criarObjeto(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, String ano, String semestre, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String lyout, TurmaVO turma, DisciplinaVO disciplina, Integer turno, Integer gradeCurricular, UsuarioVO usuarioLogado, Boolean trazerAlunoTransferencia) throws Exception {
		List<AlunosPorDisciplinasRelVO> listaRelatorio = new ArrayList<AlunosPorDisciplinasRelVO>(0);
		AlunosPorDisciplinasRelVO alunosPorDisciplinasRelVO = new AlunosPorDisciplinasRelVO();
		alunosPorDisciplinasRelVO.setTurmaDisciplinaVOs(consultarTurmaDisciplina(unidadeEnsinoVOs, cursoVOs, ano, semestre, filtroRelatorioAcademicoVO, lyout, turma, turno, gradeCurricular, disciplina, trazerAlunoTransferencia));
		alunosPorDisciplinasRelVO.setUnidadeEnsino(consultarNomeUnidadeEnsino(gradeCurricular, unidadeEnsinoVOs));
		alunosPorDisciplinasRelVO.setCurso(consultarNomeCurso(cursoVOs));
		if (turma.getCodigo() > 0) {
			alunosPorDisciplinasRelVO.setTurma(turma.getIdentificadorTurma());
		} else {
			alunosPorDisciplinasRelVO.setTurma("Todas");
		}
		if (disciplina.getCodigo() > 0) {
			alunosPorDisciplinasRelVO.setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplina.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado).getNome());
		} else {
			alunosPorDisciplinasRelVO.setDisciplina("Todas");
		}
		if (gradeCurricular > 0) {
			alunosPorDisciplinasRelVO.setGradeCurricular(getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(gradeCurricular, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado).getNome());
		}
		if (turno > 0) {
			alunosPorDisciplinasRelVO.setTurno(getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(turno, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado).getNome());
		}
		montarSituacao(filtroRelatorioAcademicoVO, alunosPorDisciplinasRelVO);
		listaRelatorio.add(alunosPorDisciplinasRelVO);
		return listaRelatorio;
	}

	public void montarSituacao(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, AlunosPorDisciplinasRelVO alunosPorDisciplinasRelVO) {
		StringBuilder situacao = new StringBuilder("");
		if (filtroRelatorioAcademicoVO.getAtivo()) {
			situacao.append(SituacaoMatriculaPeriodoEnum.ATIVA.getDescricao()).append("; ");
		}
		if (filtroRelatorioAcademicoVO.getPreMatricula()) {
			situacao.append(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA.getDescricao()).append("; ");
		}
		if (filtroRelatorioAcademicoVO.getPreMatriculaCancelada()) {
			situacao.append(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA_CANCELADA.getDescricao()).append("; ");
		}
		if (filtroRelatorioAcademicoVO.getTrancado()) {
			situacao.append(SituacaoMatriculaPeriodoEnum.TRANCADA.getDescricao()).append("; ");
		}
		if (filtroRelatorioAcademicoVO.getAbandonado()) {
			situacao.append(SituacaoMatriculaPeriodoEnum.ABANDONO_CURSO.getDescricao()).append("; ");
		}
		if (filtroRelatorioAcademicoVO.getConcluido()) {
			situacao.append("Concluído").append("; ");
		}
		if (filtroRelatorioAcademicoVO.getTransferenciaExterna()) {
			situacao.append(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_SAIDA.getDescricao()).append("; ");
		}
		if (filtroRelatorioAcademicoVO.getTransferenciaInterna()) {
			situacao.append(SituacaoMatriculaPeriodoEnum.TRANFERENCIA_INTERNA.getDescricao()).append("; ");
		}
		if (filtroRelatorioAcademicoVO.getCancelado()) {
			situacao.append(SituacaoMatriculaPeriodoEnum.CANCELADA.getDescricao()).append("; ");
		}
		if (filtroRelatorioAcademicoVO.getFormado()) {
			situacao.append(SituacaoMatriculaPeriodoEnum.FORMADO.getDescricao()).append("; ");
		}		
		if (filtroRelatorioAcademicoVO.getJubilado()) {
			situacao.append(SituacaoMatriculaPeriodoEnum.JUBILADO.getDescricao()).append("; ");
		}	
		if (filtroRelatorioAcademicoVO.getPendenteFinanceiro()) {
			situacao.append("Pendente Finaceiramente").append("; ");
		}
		if (filtroRelatorioAcademicoVO.getConfirmado()) {
			situacao.append("Confirmado").append("; ");
		}
		alunosPorDisciplinasRelVO.setSituacaoAcademica(situacao.toString());
	}

	private List<NrAlunosMatriculadosVO> consultarTurmaDisciplina(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, String ano, String semestre, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, String layout, TurmaVO turma, Integer turno, Integer gradeCurricular, DisciplinaVO disciplina, Boolean trazerAlunoTransferencia) throws Exception {
		List<NrAlunosMatriculadosVO> lista = new ArrayList<NrAlunosMatriculadosVO>(0);
		if (layout.equals("layout1")) {
			lista = consultarNrAlunosMatriculados(unidadeEnsinoVOs, cursoVOs, ano, semestre, filtroRelatorioAcademicoVO, turma, turno, gradeCurricular, disciplina, trazerAlunoTransferencia);
		} else {
			lista = consultarAlunosMatriculadosLayout2(unidadeEnsinoVOs, cursoVOs, ano, semestre, filtroRelatorioAcademicoVO, turma, disciplina, turno, gradeCurricular, trazerAlunoTransferencia);
		}
		if (lista.isEmpty()) {
			throw new ConsistirException("Não existem dados a serem exibidos com os parâmetros de pesquisa acima.");
		}
		return lista;
	}
	
	private String adicionarFiltroUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" and matricula.unidadeEnsino in (0");
		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				sql.append(", ").append(unidadeEnsinoVO.getCodigo());
			}
		}
		sql.append(") ");
		return sql.toString();
	}
	
	private String adicionarFiltroCurso(List<CursoVO> cursoVOs) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" and matricula.curso in (0");
		for (CursoVO cursoVO : cursoVOs) {
			if (cursoVO.getFiltrarCursoVO()) {
				sql.append(", ").append(cursoVO.getCodigo());
			}
		}
		sql.append(") ");
		return sql.toString();
	}

	public List<NrAlunosMatriculadosVO> consultarNrAlunosMatriculados(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs , String ano, String semestre, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, TurmaVO turma, Integer turno, Integer gradeCurricular, DisciplinaVO disciplina, Boolean trazerAlunoTransferencia) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT turma.identificadorturma as turmanome, disciplina.nome as disciplinanome, count(distinct matriculaperiodo.codigo) nralunosmatriculados");
		sql.append(" FROM matriculaperiodoturmadisciplina");
		sql.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
		sql.append(" inner join gradecurricular on gradecurricular.codigo = turma.gradecurricular");
		sql.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sql.append(" inner join matricula ON matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sql.append(" inner join curso ON matricula.curso = curso.codigo");
		sql.append(" inner join historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");

		sql.append(" WHERE 1=1 ");
		if (!Uteis.isAtributoPreenchido(turma)) {
			sql.append(adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs));
			sql.append(adicionarFiltroCurso(cursoVOs));
		}
		if (!turno.equals(0)) {
			sql.append(" and turma.turno =").append(turno);
		}
		if (ano != null && !ano.equals("")) {
			sql.append(" and matriculaperiodoturmadisciplina.ano ='").append(ano).append("'");
		}
		if (semestre != null && !semestre.equals("")) {
			sql.append(" and matriculaperiodoturmadisciplina.semestre ='").append(semestre).append("'");
		}
		if (turma != null && turma.getCodigo() != 0) {
			sql.append(" and turma.codigo=").append(turma.getCodigo());
		}
		if (disciplina != null && disciplina.getCodigo() != 0) {
			sql.append(" and disciplina.codigo=").append(disciplina.getCodigo());
		}
		if (!gradeCurricular.equals(0)) {
			sql.append(" and turma.gradecurricular = ").append(gradeCurricular);
		}
		sql.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		sql.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		
		if (!trazerAlunoTransferencia) {
			sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		}
		
		sql.append(" group by turma.identificadorturma, matriculaperiodoturmadisciplina.disciplina, disciplina.nome");
		sql.append(" ORDER BY disciplina.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(tabelaResultado);
	}

	private List<NrAlunosMatriculadosVO> consultarAlunosMatriculadosLayout2(List<UnidadeEnsinoVO> unidadeEnsinoVOs, List<CursoVO> cursoVOs, String ano, String semestre, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, TurmaVO turma, DisciplinaVO disciplina, Integer turno, Integer gradeCurricular, Boolean trazerAlunoTransferencia) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT distinct turma.identificadorturma as turma, disciplina.nome as disciplina, pessoa.nome as nomePessoa, pessoa.telefoneres as residencial, pessoa.celular as celular, pessoa.email as email, pessoa.cpf as cpf, disciplina.nome as nomeDisciplina");
		sql.append(" FROM matriculaperiodoturmadisciplina ");
		sql.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
		sql.append(" inner join disciplina on disciplina.codigo = matriculaperiodoturmadisciplina.disciplina");
		sql.append(" INNER JOIN matricula ON matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno");
		sql.append(" INNER JOIN curso ON matricula.curso = curso.codigo ");
		sql.append(" inner join historico on historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo");
		sql.append(" WHERE 1=1 ");
		if (!Uteis.isAtributoPreenchido(turma)) {
			sql.append(adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs));
			sql.append(adicionarFiltroCurso(cursoVOs));
		}
		
		if (ano != null && !ano.equals("")) {
			sql.append(" and matriculaperiodoturmadisciplina.ano ='").append(ano).append("'");
		}
		if (semestre != null && !semestre.equals("")) {
			sql.append(" and matriculaperiodoturmadisciplina.semestre ='").append(semestre).append("'");
		}
		if (turma != null && turma.getCodigo() != 0) {
			sql.append(" and turma.codigo = ").append(turma.getCodigo());
		}
		if (disciplina != null && disciplina.getCodigo() != 0) {
			sql.append(" and disciplina.codigo = ").append(disciplina.getCodigo());
		}
		if (!gradeCurricular.equals(0)) {
			sql.append(" and turma.gradecurricular = ").append(gradeCurricular);
		}
		if (!turno.equals(0)) {
			sql.append(" and turma.turno = ").append(turno);
		}
		sql.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		sql.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaperiodo"));
		
		if (!trazerAlunoTransferencia) {
			sql.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		}		
		sql.append(" group by turma.identificadorturma, disciplina.nome, pessoa.nome, pessoa.telefoneres, pessoa.celular, pessoa.email, pessoa.cpf");
		sql.append(" ORDER BY turma, disciplina.nome, pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsultaLayout2(tabelaResultado);
	}

//	public void adicionarFiltroRelatorioAcademico(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, StringBuilder sqlStr) {
//		if (filtroRelatorioAcademicoVO.getAtivo() || filtroRelatorioAcademicoVO.getPreMatricula() || filtroRelatorioAcademicoVO.getPreMatriculaCancelada() || filtroRelatorioAcademicoVO.getTrancado() || filtroRelatorioAcademicoVO.getConcluido() || filtroRelatorioAcademicoVO.getTransferenciaExterna() || filtroRelatorioAcademicoVO.getTransferenciaInterna() || filtroRelatorioAcademicoVO.getAbandonado() || filtroRelatorioAcademicoVO.getCancelado()) {
//			sqlStr.append(" AND matricula.situacao in (''");
//		}
//		if (filtroRelatorioAcademicoVO.getAtivo()) {
//			sqlStr.append(", 'AT'");
//		}
//		if (filtroRelatorioAcademicoVO.getFormado()) {
//			sqlStr.append(", 'FO'");
//		}
//		if (filtroRelatorioAcademicoVO.getPreMatricula()) {
//			sqlStr.append(", 'PR'");
//		}
//		if (filtroRelatorioAcademicoVO.getPreMatriculaCancelada()) {
//			sqlStr.append(", 'PC'");
//		}
//		if (filtroRelatorioAcademicoVO.getTrancado()) {
//			sqlStr.append(", 'TR'");
//		}
//		if (filtroRelatorioAcademicoVO.getConcluido()) {
//			sqlStr.append(", 'FI'");
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
//		if (filtroRelatorioAcademicoVO.getAtivo() || filtroRelatorioAcademicoVO.getPreMatricula() || filtroRelatorioAcademicoVO.getPreMatriculaCancelada() || filtroRelatorioAcademicoVO.getTrancado() || filtroRelatorioAcademicoVO.getConcluido() || filtroRelatorioAcademicoVO.getTransferenciaExterna() || filtroRelatorioAcademicoVO.getTransferenciaInterna() || filtroRelatorioAcademicoVO.getAbandonado() || filtroRelatorioAcademicoVO.getCancelado()) {
//			sqlStr.append(") ");
//		}		
//				
//		if (filtroRelatorioAcademicoVO.getAtivo() || filtroRelatorioAcademicoVO.getPreMatricula() || filtroRelatorioAcademicoVO.getPreMatriculaCancelada() || filtroRelatorioAcademicoVO.getTrancado() || filtroRelatorioAcademicoVO.getConcluido() || filtroRelatorioAcademicoVO.getTransferenciaExterna() || filtroRelatorioAcademicoVO.getTransferenciaInterna() || filtroRelatorioAcademicoVO.getAbandonado() || filtroRelatorioAcademicoVO.getCancelado()) {
//			sqlStr.append(" AND matricula.situacao in (''");
//		}
//		if (filtroRelatorioAcademicoVO.getAtivo()) {
//			sqlStr.append(", 'AT'");
//		}
//		if (filtroRelatorioAcademicoVO.getFormado()) {
//			sqlStr.append(", 'FO'");
//		}
//		if (filtroRelatorioAcademicoVO.getPreMatricula()) {
//			sqlStr.append(", 'PR', 'AT'");
//		}
//		if (filtroRelatorioAcademicoVO.getPreMatriculaCancelada()) {
//			sqlStr.append(", 'PC', 'AT'");
//		}
//		if (filtroRelatorioAcademicoVO.getTrancado()) {
//			sqlStr.append(", 'TR'");
//		}
//		if (filtroRelatorioAcademicoVO.getConcluido()) {
//			sqlStr.append(", 'FI', 'AT'");
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
//		if (filtroRelatorioAcademicoVO.getAtivo() || filtroRelatorioAcademicoVO.getPreMatricula() || filtroRelatorioAcademicoVO.getPreMatriculaCancelada() || filtroRelatorioAcademicoVO.getTrancado() || filtroRelatorioAcademicoVO.getConcluido() || filtroRelatorioAcademicoVO.getTransferenciaExterna() || filtroRelatorioAcademicoVO.getTransferenciaInterna() || filtroRelatorioAcademicoVO.getAbandonado() || filtroRelatorioAcademicoVO.getCancelado()) {
//			sqlStr.append(") ");
//		}		
//		if (filtroRelatorioAcademicoVO.getAtivo() || filtroRelatorioAcademicoVO.getPreMatricula() || filtroRelatorioAcademicoVO.getPreMatriculaCancelada() || filtroRelatorioAcademicoVO.getTrancado() || filtroRelatorioAcademicoVO.getConcluido() || filtroRelatorioAcademicoVO.getTransferenciaExterna() || filtroRelatorioAcademicoVO.getTransferenciaInterna() || filtroRelatorioAcademicoVO.getAbandonado() || filtroRelatorioAcademicoVO.getCancelado()) {
//			sqlStr.append(" AND matriculaperiodo.situacaomatriculaperiodo in (''");
//		}
//		if (filtroRelatorioAcademicoVO.getAtivo()) {
//			sqlStr.append(", 'AT'");
//		}
//		if (filtroRelatorioAcademicoVO.getFormado()) {
//			sqlStr.append(", 'FO'");
//		}
//		if (filtroRelatorioAcademicoVO.getPreMatricula()) {
//			sqlStr.append(", 'PR'");
//		}
//		if (filtroRelatorioAcademicoVO.getPreMatriculaCancelada()) {
//			sqlStr.append(", 'PC'");
//		}
//		if (filtroRelatorioAcademicoVO.getTrancado()) {
//			sqlStr.append(", 'TR'");
//		}
//		if (filtroRelatorioAcademicoVO.getConcluido()) {
//			sqlStr.append(", 'FI'");
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
//		if (filtroRelatorioAcademicoVO.getAtivo() || filtroRelatorioAcademicoVO.getPreMatricula() || filtroRelatorioAcademicoVO.getPreMatriculaCancelada() || filtroRelatorioAcademicoVO.getTrancado() || filtroRelatorioAcademicoVO.getConcluido() || filtroRelatorioAcademicoVO.getTransferenciaExterna() || filtroRelatorioAcademicoVO.getTransferenciaInterna() || filtroRelatorioAcademicoVO.getAbandonado() || filtroRelatorioAcademicoVO.getCancelado()) {
//			sqlStr.append(") ");
//		}
//	}

	private List<NrAlunosMatriculadosVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<NrAlunosMatriculadosVO> nrAlunosMatriculadosVOs = new ArrayList<NrAlunosMatriculadosVO>(0);
		while (tabelaResultado.next()) {
			nrAlunosMatriculadosVOs.add(montarDados(tabelaResultado));
		}
		return nrAlunosMatriculadosVOs;
	}

	private NrAlunosMatriculadosVO montarDados(SqlRowSet tabelaResultado) throws Exception {
		NrAlunosMatriculadosVO nrAlunosMatriculadosVO = new NrAlunosMatriculadosVO();
		nrAlunosMatriculadosVO.setNomeDisciplina(tabelaResultado.getString("disciplinanome"));
		nrAlunosMatriculadosVO.setNomeTurma(tabelaResultado.getString("turmanome"));
		nrAlunosMatriculadosVO.setNrAlunos(tabelaResultado.getInt("nralunosmatriculados"));
		return nrAlunosMatriculadosVO;
	}

	private List<NrAlunosMatriculadosVO> montarDadosConsultaLayout2(SqlRowSet tabelaResultado) throws Exception {
		List<NrAlunosMatriculadosVO> nrAlunosMatriculadosVOs = new ArrayList<NrAlunosMatriculadosVO>(0);
		while (tabelaResultado.next()) {
			nrAlunosMatriculadosVOs.add(montarDadosLayout2(tabelaResultado));
		}
		return nrAlunosMatriculadosVOs;
	}

	private NrAlunosMatriculadosVO montarDadosLayout2(SqlRowSet tabelaResultado) throws Exception {
		NrAlunosMatriculadosVO nrAlunosMatriculadosVO = new NrAlunosMatriculadosVO();
		nrAlunosMatriculadosVO.setNomeTurma(tabelaResultado.getString("turma"));
		nrAlunosMatriculadosVO.setNomeDisciplina(tabelaResultado.getString("disciplina"));
		nrAlunosMatriculadosVO.setNomeAluno(tabelaResultado.getString("nomePessoa"));
		nrAlunosMatriculadosVO.setTelefoneFixo(tabelaResultado.getString("residencial"));
		nrAlunosMatriculadosVO.setTelefoneCelular(tabelaResultado.getString("celular"));
		nrAlunosMatriculadosVO.setEmail(tabelaResultado.getString("email"));
		nrAlunosMatriculadosVO.setCpf(tabelaResultado.getString("cpf"));
		nrAlunosMatriculadosVO.setObs("-----------------------------------");
		return nrAlunosMatriculadosVO;
	}

	private String consultarNomeUnidadeEnsino(Integer codigoUnidadeEnsino, List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception {
		StringBuilder unidades =  new StringBuilder("");
		unidadeEnsinoVOs.forEach(u -> {if(u.getFiltrarUnidadeEnsino()) {
			
			if(unidades.length() > 0) {
				unidades.append(", ");
			}
			unidades.append(u.getNome());
		}});
		if(unidades.length() > 200) {
			return unidades.substring(0, 199) + "...";				
		}
		return unidades.toString();
	}

	private String consultarNomeCurso(List<CursoVO> cursoVOs) throws Exception {
		StringBuilder cursos =  new StringBuilder("");
		cursoVOs.forEach(u -> {if(u.getFiltrarCursoVO()) {
			
			if(cursos.length() > 0) {
				cursos.append(", ");
			}
			cursos.append(u.getNome());
		}});
		if(cursos.length() > 200) {
			return cursos.substring(0, 199) + "...";				
		}
		return cursos.toString();
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("AlunosPorDisciplinasRel");
	}

	public static String getDesignIReportRelatorioLyout() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeLyout() + ".jrxml");
	}

	public static String getDesignIReportRelatorioLayoutExcel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeLayoutExcel() + ".jrxml");
	}

	public static String getIdEntidadeLyout() {
		return ("AlunosPorDisciplinasLayoutRel");
	}

	public static String getIdEntidadeLayoutExcel() {
		return ("AlunosPorDisciplinasLayoutExcelRel");
	}
	
	public static String getDesignIReportRelatorioExcel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "AlunosPorDisciplinasExcelRel" + ".jrxml");
	}

}
