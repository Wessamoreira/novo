/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.PlanoDescontoContaReceberVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.FaixaDescontoProgressivo;
import negocio.facade.jdbc.financeiro.ContaReceber;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.HistoricoAlunoRelVO;
import relatorio.negocio.comuns.academico.MapaSituacaoAlunoRelVO;
import relatorio.negocio.comuns.academico.SituacaoFinanceiraAlunoRelVO;
import relatorio.negocio.comuns.biblioteca.EmprestimoFiltroRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.interfaces.academico.MapaSituacaoAlunoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * 
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class MapaSituacaoAlunoRel extends SuperRelatorio implements MapaSituacaoAlunoRelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7974864240285779075L;

	public MapaSituacaoAlunoRel() {
	}

	public void validarDados(MatriculaVO matricula, TurmaVO turma, CursoVO curso, Integer unidadeEnsino, String ano, String semestre, String layout) throws ConsistirException {
		if (unidadeEnsino == null || unidadeEnsino == 0) {
			throw new ConsistirException("A campo UNIDADE DE ENSINO deve ser informado para a geração do relatório.");
		}

		if ((curso.getCodigo().equals(0)) && (turma.getCodigo().equals(0)) && (matricula.getMatricula().equals(""))) {
			throw new ConsistirException("Pelo menos um dos três campos deve ser informado(CURSO, TURMA ou MATRÍCULA).");
		}
		
		if(Uteis.isAtributoPreenchido(turma) && (turma.getAnual() || turma.getSemestral())){
			if (ano.equals("")) {
				throw new ConsistirException("O campo ANO deve ser informado.");
			}			
		}else if(Uteis.isAtributoPreenchido(curso) && (curso.getAnual() || curso.getSemestral())){
			if (ano.equals("")) {
				throw new ConsistirException("O campo ANO deve ser informado.");
			}	
		}
		
		if(Uteis.isAtributoPreenchido(turma) && (turma.getSemestral())){
			if (semestre.equals("")) {
				throw new ConsistirException("O campo SEMESTRE deve ser informado.");
			}
		}else if(Uteis.isAtributoPreenchido(curso) && (curso.getSemestral())){
			if (semestre.equals("")) {
				throw new ConsistirException("O campo SEMESTRE deve ser informado.");
			}
		}
		

		if (layout.equals("")) {
			throw new ConsistirException("O campo tipo de Layout deve ser informado !");
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.SituacaoFinanceiraAlunoRelInterfaceFacade
	 * #criarObjeto(negocio.comuns.academico .MatriculaVO)
	 */
	@Override
	public List<MapaSituacaoAlunoRelVO> executarGeracaoRelatorioMapaAluno(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer unidadeEnsino, Integer curso, Integer turma, MatriculaVO matriculaVO, String ano, String semestre, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		List<MapaSituacaoAlunoRelVO> listaResultado = new ArrayList<MapaSituacaoAlunoRelVO>(0);

		listaResultado = consultarMatricula(filtroRelatorioAcademicoVO, unidadeEnsino, curso, turma, matriculaVO.getMatricula(), ano, semestre);
		if (listaResultado.isEmpty()) {
			throw new Exception("Não foi encontrado nenhum aluno com os parâmetros passados.");
		}

		List<ContaReceberVO> contaReceberVOs = gerarListaComDesconto(listaResultado, "VE", configuracaoFinanceiroVO, usuarioVO);
		listaResultado = realizarSeparacaoContaReceberMatricula(listaResultado, contaReceberVOs);
		for (MapaSituacaoAlunoRelVO mapaSituacaoAlunoRelVO : listaResultado) {
			Ordenacao.ordenarListaDecrescente(mapaSituacaoAlunoRelVO.getListaContaReceberVOs(), "dataVencimento");
		}
		contaReceberVOs.clear();

		List<HistoricoVO> historicoVOs = consultarHistoricoAluno(listaResultado, ano, semestre, true);
		listaResultado = realizarSeparacaoHistoricoMatricula(listaResultado, historicoVOs);
		historicoVOs.clear();

		List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs = consultarDocumentacaoMatriculaAluno(listaResultado);
		listaResultado = realizarSeparacaoDocumentacaoMatricula(listaResultado, documetacaoMatriculaVOs);
		documetacaoMatriculaVOs.clear();

		return listaResultado;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * relatorio.negocio.jdbc.academico.SituacaoFinanceiraAlunoRelInterfaceFacade
	 * #criarObjeto(negocio.comuns.academico .MatriculaVO)
	 */
	public List<MapaSituacaoAlunoRelVO> executarGeracaoRelatorio(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer unidadeEnsino, Integer curso, Integer turma, MatriculaVO matriculaVO, String ano, String semestre, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		List<MapaSituacaoAlunoRelVO> listaResultado = new ArrayList<MapaSituacaoAlunoRelVO>(0);

		listaResultado = consultarMatricula(filtroRelatorioAcademicoVO, unidadeEnsino, curso, turma, matriculaVO.getMatricula(), ano, semestre);
		if (listaResultado.isEmpty()) {
			throw new Exception("Não foi encontrado nenhum aluno com os parâmetros passados.");
		}

		List<ContaReceberVO> contaReceberVOs = consultarContaReceberAluno(listaResultado, "AR", configuracaoFinanceiroVO);
		realizarSeparacaoContaReceberMatricula(listaResultado, contaReceberVOs);
		contaReceberVOs.clear();

		List<HistoricoVO> historicoVOs = consultarHistoricoAluno(listaResultado, ano, semestre, false);
		listaResultado = realizarSeparacaoHistoricoMatricula(listaResultado, historicoVOs);
		historicoVOs.clear();

		List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs = consultarDocumentacaoMatriculaAluno(listaResultado);
		listaResultado = realizarSeparacaoDocumentacaoMatricula(listaResultado, documetacaoMatriculaVOs);
		documetacaoMatriculaVOs.clear();

		return listaResultado;
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

	public List<MapaSituacaoAlunoRelVO> consultarMatricula(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, Integer unidadeEnsino, Integer curso, Integer turma, String matricula, String ano, String semestre) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select distinct matricula.matricula, pessoa.nome AS \"aluno\", pessoa.cpf as \"cpf\",  curso.nome AS \"curso.nome\", ");
		sql.append("(select SUM(gd.cargaHoraria) from historico inner join disciplina on disciplina.codigo = historico.disciplina ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
		sql.append(" inner join gradedisciplina gd on gd.periodoletivo = periodoletivo.codigo and gd.disciplina = disciplina.codigo ");
		sql.append(" where historico.matricula = matricula.matricula) AS \"cargaHorariaCurso\", ");
		sql.append(" (select SUM(gd.cargaHoraria) from disciplina inner join historico on historico.disciplina = disciplina.codigo ");
		sql.append(" inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo ");
		sql.append(" inner join periodoletivo on periodoletivo.codigo = matriculaperiodo.periodoletivomatricula ");
		sql.append(" inner join gradedisciplina gd on gd.periodoletivo = periodoletivo.codigo and gd.disciplina = disciplina.codigo ");
		sql.append(" where (historico.situacao = 'AP' or historico.situacao = 'AA') and historico.matricula = matricula.matricula) as \"cargaHorariaCumprida\", ");
		sql.append(" turma2.identificadorturma AS \"turma2.identificadorTurma\" ");
		sql.append(" from matricula ");
		sql.append(" inner join matriculaPeriodo on matriculaPeriodo.matricula = matricula.matricula ");
		if (!ano.trim().equals("")) {
			sql.append(" and matriculaperiodo.ano = '").append(ano.toString()).append("'");
		}
		if (!semestre.equals("")) {
			sql.append(" and matriculaperiodo.semestre = '").append(semestre.toString()).append("'");
		}
		if (ano.trim().equals("") || semestre.trim().equals("")) {
			sql.append(" AND matriculaperiodo.codigo = (select codigo from matriculaPeriodo mp where mp.matricula = matricula.matricula ");
			if (!ano.trim().equals("")) {
				sql.append(" and mp.ano = '").append(ano.toString()).append("'");
			}
			if (!semestre.equals("")) {
				sql.append(" and mp.semestre = '").append(semestre.toString()).append("'");
			}
			sql.append(" order by (mp.ano||'/'||mp.semestre) desc, case when mp.situacaoMatriculaPeriodo in ('AT', 'PR', 'FI', 'FO') then 1 else 2 end, mp.codigo desc   limit 1) ");
		
		}
		sql.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
		sql.append(" inner join curso on curso.codigo = matricula.curso ");
		sql.append(" inner join gradeCurricular on gradeCurricular.codigo = matriculaPeriodo.gradeCurricular ");
		sql.append(" inner join periodoLetivo on periodoLetivo.gradeCurricular = gradeCurricular.codigo ");
		sql.append(" inner join gradeDisciplina on gradeDisciplina.periodoLetivo = periodoLetivo.codigo ");
		sql.append(" inner join disciplina on disciplina.codigo = gradeDisciplina.disciplina ");
		// if (turma.intValue() != 0) {
		// sql.append(" inner join matriculaPeriodoTurmaDisciplina mptd on mptd.matriculaPeriodo = matriculaPeriodo.codigo ");
		// sql.append(" inner join turma on turma.codigo = mptd.turma ");
		// sql.append(" left join turma turma2 on turma2.codigo = matriculaPeriodo.turma ");
		// } else {
		sql.append(" left join turma turma2 on turma2.codigo = matriculaPeriodo.turma ");
		// }
		sql.append(" where matricula.unidadeEnsino = ");
		sql.append(unidadeEnsino.intValue());
		if (!curso.equals(0)) {
			sql.append(" and curso.codigo = ").append(curso.intValue());
		}

		sql.append(" and ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaPeriodo"));
		sql.append(" and ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroRelatorioAcademicoVO, "matriculaPeriodo"));

		if (turma.intValue() != 0) {
			sql.append(" and turma2.codigo = ").append(turma.intValue());
		}
		if (!matricula.equals("")) {
			sql.append(" and matricula.matricula = '").append(matricula).append("'");
		}

		sql.append(" Group by curso.nome, turma2.identificadorTurma, pessoa.nome, matricula.matricula,pessoa.cpf ");
		sql.append(" order by curso.nome, turma2.identificadorTurma, pessoa.nome, matricula.matricula,pessoa.cpf ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarListaObjetoPrincipal(tabelaResultado);
	}

	public List<MapaSituacaoAlunoRelVO> realizarSeparacaoContaReceberMatricula(List<MapaSituacaoAlunoRelVO> mapaSituacaoAlunoRelVOs, List<ContaReceberVO> contaReceberVOs) {
		for (ContaReceberVO contaReceberVO : contaReceberVOs) {
			for (MapaSituacaoAlunoRelVO mapaSituacaoAlunoRelVO : mapaSituacaoAlunoRelVOs) {
				if (mapaSituacaoAlunoRelVO.getMatricula().equals(contaReceberVO.getMatriculaAluno().getMatricula())) {
					mapaSituacaoAlunoRelVO.getListaContaReceberVOs().add(contaReceberVO);
					mapaSituacaoAlunoRelVO.setPendenciaFinanceira(Boolean.TRUE);
					break;
				}
			}
		}

		return mapaSituacaoAlunoRelVOs;
	}

	public List<MapaSituacaoAlunoRelVO> realizarSeparacaoDocumentacaoMatricula(List<MapaSituacaoAlunoRelVO> mapaSituacaoAlunoRelVOs, List<DocumetacaoMatriculaVO> documetacaoMatriculaVOs) {
		for (DocumetacaoMatriculaVO documetacaoMatriculaVO : documetacaoMatriculaVOs) {
			for (MapaSituacaoAlunoRelVO mapaSituacaoAlunoRelVO : mapaSituacaoAlunoRelVOs) {
				if (mapaSituacaoAlunoRelVO.getMatricula().equals(documetacaoMatriculaVO.getMatricula())) {
					mapaSituacaoAlunoRelVO.getListaDocumentacaoVOs().add(documetacaoMatriculaVO);
					mapaSituacaoAlunoRelVO.setPendenciaDocumentacao(Boolean.TRUE);
					break;
				}
			}
		}

		return mapaSituacaoAlunoRelVOs;
	}

	public List<MapaSituacaoAlunoRelVO> realizarSeparacaoHistoricoMatricula(List<MapaSituacaoAlunoRelVO> mapaSituacaoAlunoRelVOs, List<HistoricoVO> historicoVOs) {
		for (HistoricoVO historicoVO : historicoVOs) {
			for (MapaSituacaoAlunoRelVO mapaSituacaoAlunoRelVO : mapaSituacaoAlunoRelVOs) {
				if (mapaSituacaoAlunoRelVO.getMatricula().equals(historicoVO.getMatricula().getMatricula())) {
					mapaSituacaoAlunoRelVO.getListaHistoricoVOs().add(historicoVO);
					break;
				}
			}
		}

		return mapaSituacaoAlunoRelVOs;
	}

	private List<ContaReceberVO> consultarContaReceberAluno(List<MapaSituacaoAlunoRelVO> mapaSituacaoAlunoRelVOs, String situacao, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select matricula.matricula, contaReceber.parcela, contaReceber.valor, contaReceber.dataVencimento, contaReceber.multa,  contaReceber.juro, juroPorcentagem, multaPorcentagem, acrescimo, valorDescontoCalculado as valorDesconto ");
		sql.append(" from contaReceber ");
		sql.append(" inner join matricula on matricula.matricula = contaReceber.matriculaAluno ");
		sql.append(" where matricula.matricula in (");
		int x = 0;
		for (MapaSituacaoAlunoRelVO mapaSituacaoAlunoRelVO : mapaSituacaoAlunoRelVOs) {
			if (x > 0) {
				sql.append(", ");
			}
			sql.append("'");
			sql.append(mapaSituacaoAlunoRelVO.getMatricula());
			sql.append("'");
			x++;
		}
		if (situacao.equals("AR")) {
			sql.append(") and contareceber.situacao = 'AR' ");
		} else if (situacao.equals("VE")) {
			sql.append(") and contareceber.situacao = 'AR' and contareceber.dataVencimento < current_date ");
		} else if (situacao.equals("RE")) {
			sql.append(") and contareceber.situacao = 'RE' ");
		}
		sql.append(" Group by matricula.matricula, contaReceber.parcela, contaReceber.valor, contaReceber.dataVencimento, contaReceber.multa,  contaReceber.juro, juroPorcentagem, multaPorcentagem, acrescimo, valorDescontoCalculado ");
		sql.append(" order by matricula.matricula,contareceber.datavencimento, contareceber.parcela");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarListaFinanceiroAluno(tabelaResultado, configuracaoFinanceiroVO);
	}

	private List<DocumetacaoMatriculaVO> consultarDocumentacaoMatriculaAluno(List<MapaSituacaoAlunoRelVO> mapaSituacaoAlunoRelVOs) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select documetacaomatricula.situacao, tipoDocumento.nome AS \"tipoDocumento.nome\", matricula.matricula  from matricula ");
		sql.append(" inner join documetacaoMatricula on documetacaoMatricula.matricula = matricula.matricula ");
		sql.append(" inner join tipoDocumento on documetacaoMatricula.tipodeDocumento = tipoDocumento.codigo ");
		sql.append(" where matricula.matricula in(");
		int x = 0;
		for (MapaSituacaoAlunoRelVO mapaSituacaoAlunoRelVO : mapaSituacaoAlunoRelVOs) {
			if (x > 0) {
				sql.append(", ");
			}
			sql.append("'");
			sql.append(mapaSituacaoAlunoRelVO.getMatricula());
			sql.append("'");
			x++;
		}
		sql.append(") and documetacaomatricula.situacao = 'PE'");
		sql.append(" Group by documetacaomatricula.situacao, tipoDocumento.nome, matricula.matricula  ");
		sql.append(" order by  matricula.matricula, documetacaomatricula.situacao ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarListaDocumentacaoMatricula(tabelaResultado);
	}

	private List<HistoricoVO> consultarHistoricoAluno(List<MapaSituacaoAlunoRelVO> mapaSituacaoAlunoRelVOs, String ano, String semestre, Boolean trazerPeriodoAula) throws Exception {
		StringBuilder sql = new StringBuilder("SELECT historico.matricula, disciplina.nome, gradedisciplina.cargaHoraria, historico.freguencia, ");
		sql.append("historico.mediaFinal, historico.situacao, matricula.situacao AS \"matricula.situacao\"  ");
		if (trazerPeriodoAula) {
			sql.append(", aula.datainicio AS datainicio, ");
			sql.append(" aula.datatermino AS datafim");
		}
		sql.append(" FROM historico ");
		sql.append("INNER JOIN disciplina ON disciplina.codigo = historico.disciplina ");
		sql.append("INNER JOIN matriculaperiodo ON matriculaperiodo.codigo = historico.matriculaperiodo ");
		sql.append(" INNER JOIN gradedisciplina on gradedisciplina.disciplina = disciplina.codigo and matriculaperiodo.periodoletivomatricula = gradedisciplina.periodoletivo ");
		sql.append("INNER JOIN matricula ON matricula.matricula = matriculaperiodo.matricula ");
		if (trazerPeriodoAula) {
			sql.append(" left join periodoauladisciplinaaluno(historico.codigo) as aula on aula.datainicio is not null ");
		}
		sql.append(" WHERE historico.matricula IN(");
		int x = 0;
		for (MapaSituacaoAlunoRelVO mapaSituacaoAlunoRelVO : mapaSituacaoAlunoRelVOs) {
			if (x > 0) {
				sql.append(", ");
			}
			sql.append("'");
			sql.append(mapaSituacaoAlunoRelVO.getMatricula());
			sql.append("'");
			x++;
		}
		sql.append(") ");
		if (!ano.equals("")) {
			sql.append(" AND matriculaperiodo.ano = '");
			sql.append(ano.toString());
			sql.append("' ");
		}
		if (!semestre.equals("")) {
			sql.append(" AND matriculaperiodo.semestre = '");
			sql.append(semestre.toString());
			sql.append("' ");
		}
		sql.append(" GROUP BY disciplina.nome, gradedisciplina.cargaHoraria, historico.freguencia, historico.mediaFinal, historico.situacao, historico.matricula, matricula.situacao ");
		if (trazerPeriodoAula) {
			sql.append(", aula.datainicio, ");
			sql.append(" aula.datatermino ");
		}
		if (trazerPeriodoAula) {
			sql.append(" ORDER BY historico.matricula, datainicio, disciplina.nome ");
		} else {
			sql.append(" ORDER BY historico.matricula, disciplina.nome ");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarListaHistoricoAluno(tabelaResultado, trazerPeriodoAula);
	}

	private List<MapaSituacaoAlunoRelVO> montarListaObjetoPrincipal(SqlRowSet tabelaResultado) throws Exception {
		List<MapaSituacaoAlunoRelVO> dadosSituacaoContaReceberVOs = new ArrayList<MapaSituacaoAlunoRelVO>(0);
		while (tabelaResultado.next()) {
			dadosSituacaoContaReceberVOs.add(montarDados(tabelaResultado));
		}
		return dadosSituacaoContaReceberVOs;
	}

	private List<HistoricoVO> montarListaHistoricoAluno(SqlRowSet tabelaResultado, Boolean trazerPeriodoAula) throws Exception {
		List<HistoricoVO> historicoVOs = new ArrayList<HistoricoVO>(0);
		while (tabelaResultado.next()) {
			historicoVOs.add(montarDadosHistoricoAluno(tabelaResultado, trazerPeriodoAula));
		}
		return historicoVOs;
	}

	private List<DocumetacaoMatriculaVO> montarListaDocumentacaoMatricula(SqlRowSet tabelaResultado) throws Exception {
		List<DocumetacaoMatriculaVO> documentacaoMatriculaVOs = new ArrayList<DocumetacaoMatriculaVO>(0);
		while (tabelaResultado.next()) {
			documentacaoMatriculaVOs.add(montarDadosDocumentacaoMatricula(tabelaResultado));
		}
		return documentacaoMatriculaVOs;
	}

	private List<ContaReceberVO> montarListaFinanceiroAluno(SqlRowSet tabelaResultado, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		List<ContaReceberVO> dadosSituacaoContaReceberVOs = new ArrayList<ContaReceberVO>(0);
		while (tabelaResultado.next()) {
			dadosSituacaoContaReceberVOs.add(montarDadosFinanceiroAluno(tabelaResultado, configuracaoFinanceiroVO));
		}
		return dadosSituacaoContaReceberVOs;
	}

	private MapaSituacaoAlunoRelVO montarDados(SqlRowSet tabelaResultado) throws Exception {
		MapaSituacaoAlunoRelVO obj = new MapaSituacaoAlunoRelVO();
		obj.setMatricula(tabelaResultado.getString("matricula"));
		obj.setAluno(tabelaResultado.getString("aluno"));
		obj.setCurso(tabelaResultado.getString("curso.nome"));
		obj.setCargaHorariaCurso(tabelaResultado.getInt("cargaHorariaCurso"));
		obj.setCargaHorariaCumprida(tabelaResultado.getInt("cargaHorariaCumprida"));
		obj.setTurma(tabelaResultado.getString("turma2.identificadorTurma"));
		obj.setCpf(tabelaResultado.getString("cpf"));
		return obj;
	}

	private ContaReceberVO montarDadosFinanceiroAluno(SqlRowSet tabelaResultado, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
		ContaReceberVO obj = new ContaReceberVO();
		obj.getMatriculaAluno().setMatricula(tabelaResultado.getString("matricula"));
		obj.setParcela(tabelaResultado.getString("parcela"));
		obj.setValor(tabelaResultado.getDouble("valor"));
		obj.setDataVencimento(tabelaResultado.getDate("dataVencimento"));
		obj.setMulta(tabelaResultado.getDouble("multa"));
		obj.setJuro(tabelaResultado.getDouble("juro"));
		obj.setAcrescimo(tabelaResultado.getDouble("acrescimo"));
		obj.setValorDesconto(tabelaResultado.getDouble("valorDesconto"));
		calcularJuroMulta(obj, tabelaResultado, configuracaoFinanceiroVO, tabelaResultado.getDouble("multaPorcentagem"), tabelaResultado.getDouble("juroPorcentagem"));
		return obj;
	}

	private DocumetacaoMatriculaVO montarDadosDocumentacaoMatricula(SqlRowSet tabelaResultado) throws Exception {
		DocumetacaoMatriculaVO documentacaoMatricula = new DocumetacaoMatriculaVO();
		documentacaoMatricula.setSituacao(tabelaResultado.getString("situacao"));
		documentacaoMatricula.setMatricula(tabelaResultado.getString("matricula"));
		documentacaoMatricula.getTipoDeDocumentoVO().setNome(tabelaResultado.getString("tipoDocumento.nome"));
		return documentacaoMatricula;
	}

	private HistoricoVO montarDadosHistoricoAluno(SqlRowSet tabelaResultado, Boolean trazerPeriodoAula) throws Exception {
		HistoricoVO historicoVO = new HistoricoVO();
		historicoVO.getMatricula().setMatricula(tabelaResultado.getString("matricula"));
		historicoVO.getMatricula().setSituacao(tabelaResultado.getString("matricula.situacao"));
		historicoVO.getDisciplina().setNome(tabelaResultado.getString("nome"));
		historicoVO.getGradeDisciplinaVO().setCargaHoraria(tabelaResultado.getInt("cargaHoraria"));
		historicoVO.setFreguencia(tabelaResultado.getDouble("freguencia"));
		if (trazerPeriodoAula) {
			historicoVO.setDataPrimeiraAula(tabelaResultado.getDate("datainicio"));
			historicoVO.setData(tabelaResultado.getDate("datafim"));
		}
		if (historicoVO.getMatricula().getSituacao().equals("CA")) {
			historicoVO.setSituacao("CA");
		} else {
			historicoVO.setSituacao(tabelaResultado.getString("situacao"));
		}
		historicoVO.setMediaFinal(tabelaResultado.getDouble("mediafinal"));
		return historicoVO;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getDesignIReportMapaAlunoCompleto() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "MapaAlunoCompletoRel" + ".jrxml");
	}

	public static String getCaminhoBaseRelatorioMapaAlunoCompleto() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("MapaSituacaoAlunoRel");
	}

	public List<ContaReceberVO> gerarListaComDesconto(List<MapaSituacaoAlunoRelVO> mapaSituacaoAlunoRelVOs, String situacao, ConfiguracaoFinanceiroVO configuracao, UsuarioVO usuarioVO) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("select ");
		sqlStr.append("cr.valor-((cr.valor*descontoprogressivo.percdescontolimite1)/100) as valordiaprimeirovencimento, ");
		// dados contareceber
		sqlStr.append("cr.tipoPessoa as \"cr.tipoPessoa\", cr.codigo as codigocontareceber, cr.data as \"cr.data\", cr.situacao as \"cr.situacao\", ");
		sqlStr.append("cr.dataVencimento as \"cr.dataVencimento\", cr.valor as \"cr.valor\", cr.nrDocumento as \"cr.nrDocumento\", ");
		sqlStr.append("cr.tipoOrigem as \"cr.tipoOrigem\", ");
		sqlStr.append("cr.matriculaAluno as \"cr.matriculaAluno\",  ");
		sqlStr.append("cr.valorRecebido as \"cr.valorRecebido\", cr.juro, ");
		sqlStr.append("cr.juroPorcentagem as \"cr.juroPorcentagem\", ");
		sqlStr.append("cr.multa, cr.acrescimo as \"cr.acrescimo\", cr.multaPorcentagem as \"cr.multaPorcentagem\", cr.parcela as \"cr.parcela\", ");
		sqlStr.append("cr.valorIndiceReajustePorAtraso as \"cr.valorIndiceReajustePorAtraso\", cr.valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa as \"cr.valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa\", ");
		sqlStr.append("cr.descontoProgressivo as \"cr.descontoProgressivo\", cr.valorDescontoRecebido as \"cr.valorDescontoRecebido\", ");
		sqlStr.append("cr.tipoDesconto as \"cr.tipoDesconto\", cr.dataArquivoRemessa as \"cr.dataArquivoRemessa\", ");
		sqlStr.append("cr.descontoprogressivoutilizado as \"cr.descontoprogressivoutilizado\", cr.ordemConvenio as \"cr.ordemConvenio\", ");
		sqlStr.append("cr.OrdemConvenioValorCheio as \"cr.OrdemConvenioValorCheio\", cr.OrdemDescontoAluno as \"cr.OrdemDescontoAluno\", ");
		sqlStr.append("cr.OrdemDescontoAlunoValorCheio as \"cr.OrdemDescontoAlunoValorCheio\", cr.OrdemDescontoProgressivo as \"cr.OrdemDescontoProgressivo\", ");
		sqlStr.append("cr.OrdemDescontoProgressivoValorCheio as \"cr.OrdemDescontoProgressivoValorCheio\", cr.OrdemPlanoDesconto as \"cr.OrdemPlanoDesconto\", ");
		sqlStr.append("cr.OrdemPlanoDescontoValorCheio as \"cr.OrdemPlanoDescontoValorCheio\", cr.valorDesconto as \"cr.valorDesconto\", ");
		sqlStr.append("cr.justificativaDesconto as \"cr.justificativaDesconto\", cr.valorDescontoLancadoRecebimento as \"cr.valorDescontoLancadoRecebimento\", ");
		sqlStr.append("cr.valorCalculadoDescontoLancadoRecebimento as \"cr.valorCalculadoDescontoLancadoRecebimento\", ");
		sqlStr.append("cr.tipoDescontoLancadoRecebimento as \"cr.tipoDescontoLancadoRecebimento\", ");
		sqlStr.append("cr.valorDescontoAlunoJaCalculado as \"cr.valorDescontoAlunoJaCalculado\", ");
		sqlStr.append("cr.impressaoBoletoRealizada as \"cr.impressaoBoletoRealizada\", ");
		sqlStr.append("cr.valorDescontoRateio as \"cr.valorDescontoRateio\", ");
		sqlStr.append("cr.valorDescontoCalculadoPrimeiraFaixaDescontos as \"cr.valorDescontoCalculadoPrimeiraFaixaDescontos\", ");
		sqlStr.append("cr.valorDescontoCalculadoSegundaFaixaDescontos as \"cr.valorDescontoCalculadoSegundaFaixaDescontos\", ");
		sqlStr.append("cr.valorDescontoCalculadoTerceiraFaixaDescontos as \"cr.valorDescontoCalculadoTerceiraFaixaDescontos\", ");
		sqlStr.append("cr.valorDescontoCalculadoQuartaFaixaDescontos as \"cr.valorDescontoCalculadoQuartaFaixaDescontos\", cr.updated as \"cr.updated\", ");
		sqlStr.append("cr.usaDescontoCompostoPlanoDesconto as \"cr.usaDescontoCompostoPlanoDesconto\", cr.descontoInstituicao as \"cr.descontoInstituicao\", ");
		sqlStr.append("cr.descontoConvenio as \"cr.descontoConvenio\", cr.valorDescontoProgressivo as \"cr.valorDescontoProgressivo\", cr.nossonumero as \"cr.nossonumero\", ");
		// dados planodescontocontareceber
		sqlStr.append("pdcr.codigo as \"pdcr.codigo\", pdcr.contaReceber as \"pdcr.contaReceber\", pdcr.planoDesconto as \"pdcr.planoDesconto\", ");
		sqlStr.append("pdcr.tipoItemPlanoFinanceiro as \"pdcr.tipoItemPlanoFinanceiro\", pdcr.convenio as \"pdcr.convenio\", ");
		sqlStr.append("pdcr.valorutilizadorecebimento as \"pdcr.valorutilizadorecebimento\", ");
		// dados planodesconto
		sqlStr.append("pd.codigo as \"pd.codigo\", pd.nome as \"pd.nome\", pd.percDescontoParcela as \"pd.percDescontoParcela\", ");
		sqlStr.append("pd.percDescontoMatricula as \"pd.percDescontoMatricula\", pd.requisitos as \"pd.requisitos\", pd.descricao as \"pd.descricao\", ");
		sqlStr.append("pd.somente1PeriodoLetivoParcela as \"pd.somente1PeriodoLetivoParcela\", pd.somente1PeriodoLetivoMatricula as \"pd.somente1PeriodoLetivoMatricula\", ");
		sqlStr.append("pd.tipoDescontoParcela as \"pd.tipoDescontoParcela\", pd.tipoDescontoMatricula as \"pd.tipoDescontoMatricula\", ");
		sqlStr.append("pd.diasValidadeVencimento as \"pd.diasValidadeVencimento\", pd.ativo as \"pd.ativo\", pd.dataAtivacao as \"pd.dataAtivacao\", ");
		sqlStr.append("pd.dataInativacao as \"pd.dataInativacao\", pd.responsavelAtivacao as \"pd.responsavelAtivacao\", ");
		sqlStr.append("pd.responsavelInativacao as \"pd.responsavelInativacao\", pd.descontoValidoAteDataVencimento as \"pd.descontoValidoAteDataVencimento\", ");
		// dados convenio
		sqlStr.append("c.codigo as \"c.codigo\", c.descricao as \"c.descricao\", c.dataAssinatura as \"c.dataAssinatura\", c.parceiro as \"c.parceiro\", ");
		sqlStr.append("c.ativo as \"c.ativo\", c.cobertura as \"c.cobertura\", c.preRequisitos as \"c.preRequisitos\", c.dataInicioVigencia as \"c.dataInicioVigencia\", ");
		sqlStr.append("c.dataFinalVigencia as \"c.dataFinalVigencia\", c.descontoMatricula as \"c.descontoMatricula\", ");
		sqlStr.append("c.tipoDescontoMatricula as \"c.tipoDescontoMatricula\", c.descontoParcela as \"c.descontoParcela\", ");
		sqlStr.append("c.tipoDescontoParcela as \"c.tipoDescontoParcela\", c.bolsaCusteadaParceiroMatricula as \"c.bolsaCusteadaParceiroMatricula\", ");
		sqlStr.append("c.tipoBolsaCusteadaParceiroMatricula as \"c.tipoBolsaCusteadaParceiroMatricula\", ");
		sqlStr.append("c.bolsaCusteadaParceiroParcela as \"c.bolsaCusteadaParceiroParcela\", c.tipoBolsaCusteadaParceiroParcela as \"c.tipoBolsaCusteadaParceiroParcela\", ");
		sqlStr.append("c.formaRecebimentoParceiro as \"c.formaRecebimentoParceiro\", c.diaBaseRecebimentoParceiro as \"c.diaBaseRecebimentoParceiro\", ");
		sqlStr.append("c.requisitante as \"c.requisitante\", c.dataRequisicao as \"c.dataRequisicao\", c.responsavelAutorizacao as \"c.responsavelAutorizacao\", ");
		sqlStr.append("c.dataAutorizacao as \"c.dataAutorizacao\", c.responsavelFinalizacao as \"c.responsavelFinalizacao\", ");
		sqlStr.append("c.dataFinalizacao as \"c.dataFinalizacao\", c.situacao as \"c.situacao\", c.validoParaTodoCurso as \"c.validoParaTodoCurso\", ");
		sqlStr.append("c.validoParaTodaUnidadeEnsino as \"c.validoParaTodaUnidadeEnsino\", c.validoParaTodoTurno as \"c.validoParaTodoTurno\", ");
		sqlStr.append("c.periodoIndeterminado as \"c.periodoIndeterminado\", c.dataAtivacao as \"c.dataAtivacao\", c.dataInativacao as \"c.dataInativacao\", ");
		sqlStr.append("c.responsavelAtivacao as \"c.responsavelAtivacao\", c.responsavelInativacao as \"c.responsavelInativacao\", ");
		sqlStr.append("c.descontoProgressivoParceiro as \"c.descontoProgressivoParceiro\", c.descontoProgressivoAluno as \"c.descontoProgressivoAluno\", ");
		sqlStr.append("c.possuiDescontoAntecipacao as \"c.possuiDescontoAntecipacao\", c.calculadoEmCimaValorLiquido as \"c.calculadoEmCimaValorLiquido\", ");
		sqlStr.append("c.aplicarDescontoProgressivoMatricula as \"c.aplicarDescontoProgressivoMatricula\", ");
		sqlStr.append("c.aplicarDescontoProgressivoMatriculaParceiro as \"c.aplicarDescontoProgressivoMatriculaParceiro\", ");
		// dados descontoprogressivoaluno
		sqlStr.append("dpa.codigo as \"dpa.codigo\", dpa.nome as \"dpa.nome\", dpa.diaLimite1 as \"dpa.diaLimite1\", ");
		sqlStr.append("dpa.percDescontoLimite1 as \"dpa.percDescontoLimite1\", dpa.diaLimite2 as \"dpa.diaLimite2\", ");
		sqlStr.append("dpa.percDescontoLimite2 as \"dpa.percDescontoLimite2\", dpa.diaLimite3 as \"dpa.diaLimite3\", ");
		sqlStr.append("dpa.percDescontoLimite3 as \"dpa.percDescontoLimite3\", dpa.diaLimite4 as \"dpa.diaLimite4\", ");
		sqlStr.append("dpa.percDescontoLimite4 as \"dpa.percDescontoLimite4\", dpa.valorDescontoLimite1 as \"dpa.valorDescontoLimite1\", ");
		sqlStr.append("dpa.valorDescontoLimite2 as \"dpa.valorDescontoLimite2\", dpa.valorDescontoLimite3 as \"dpa.valorDescontoLimite3\", ");
		sqlStr.append("dpa.valorDescontoLimite4 as \"dpa.valorDescontoLimite4\", dpa.ativado as \"dpa.ativado\", dpa.dataAtivacao as \"dpa.dataAtivacao\", ");
		sqlStr.append("dpa.responsavelAtivacao as \"dpa.responsavelAtivacao\", dpa.dataInativacao as \"dpa.dataInativacao\", ");
		sqlStr.append("dpa.responsavelInativacao as \"dpa.responsavelInativacao\", ");
		// dados descontoprogressivoparceiro
		sqlStr.append("dpp.codigo as \"dpp.codigo\", dpp.nome as \"dpp.nome\", dpp.diaLimite1 as \"dpp.diaLimite1\", ");
		sqlStr.append("dpp.percDescontoLimite1 as \"dpp.percDescontoLimite1\", dpp.diaLimite2 as \"dpp.diaLimite2\", ");
		sqlStr.append("dpp.percDescontoLimite2 as \"dpp.percDescontoLimite2\", dpp.diaLimite3 as \"dpp.diaLimite3\", ");
		sqlStr.append("dpp.percDescontoLimite3 as \"dpp.percDescontoLimite3\", dpp.diaLimite4 as \"dpp.diaLimite4\", ");
		sqlStr.append("dpp.percDescontoLimite4 as \"dpp.percDescontoLimite4\", dpp.valorDescontoLimite1 as \"dpp.valorDescontoLimite1\", ");
		sqlStr.append("dpp.valorDescontoLimite2 as \"dpp.valorDescontoLimite2\", dpp.valorDescontoLimite3 as \"dpp.valorDescontoLimite3\", ");
		sqlStr.append("dpp.valorDescontoLimite4 as \"dpp.valorDescontoLimite4\", dpp.ativado as \"dpp.ativado\", dpp.dataAtivacao as \"dpp.dataAtivacao\", ");
		sqlStr.append("dpp.responsavelAtivacao as \"dpp.responsavelAtivacao\", dpp.dataInativacao as \"dpp.dataInativacao\", ");
		sqlStr.append("dpp.responsavelInativacao as \"dpp.responsavelInativacao\" ");
		// //System.out.println(sqlStr.toString());
		sqlStr.append("from contareceber cr ");
		sqlStr.append("left join descontoprogressivo on descontoprogressivo.codigo = cr.descontoprogressivo ");
		sqlStr.append("inner join matricula on cr.matriculaaluno = matricula.matricula ");
		sqlStr.append("LEFT JOIN PlanoDescontoContaReceber pdcr ON pdcr.contareceber = cr.codigo ");
		sqlStr.append("LEFT JOIN PlanoDesconto pd ON pd.codigo = pdcr.planodesconto ");
		sqlStr.append("LEFT JOIN Convenio c ON c.codigo = pdcr.convenio ");
		sqlStr.append("LEFT JOIN DescontoProgressivo dpa ON dpa.codigo = c.descontoprogressivoaluno ");
		sqlStr.append("LEFT JOIN DescontoProgressivo dpp ON dpp.codigo = c.descontoprogressivoparceiro ");
		sqlStr.append("where cr.situacao = 'AR' and cr.valor > 0 ");

		sqlStr.append(" and matricula.matricula in (");
		int x = 0;
		for (MapaSituacaoAlunoRelVO mapaSituacaoAlunoRelVO : mapaSituacaoAlunoRelVOs) {
			if (x > 0) {
				sqlStr.append(", ");
			}
			sqlStr.append("'");
			sqlStr.append(mapaSituacaoAlunoRelVO.getMatricula());
			sqlStr.append("'");
			x++;
		}

		if (situacao.equals("AR")) {
			sqlStr.append(") and cr.situacao = 'AR' ");
		} else if (situacao.equals("VE")) {
			sqlStr.append(") and cr.situacao = 'AR' and cr.dataVencimento < current_date ");
		} else if (situacao.equals("RE")) {
			sqlStr.append(") and cr.situacao = 'RE' ");
		}

		sqlStr.append(" group by ");
		sqlStr.append("cr.matriculaaluno, cr.parcela,  ");
		sqlStr.append("cr.datavencimento, valordiaprimeirovencimento, valor, ");
		sqlStr.append("cr.codigo, ");
		sqlStr.append("cr.tipoorigem, multaPorcentagem, multa, juroPorcentagem, ");
		sqlStr.append("juro, cr.valordesconto, nossonumero, valorDescontoCalculadoPrimeiraFaixaDescontos, ");
		sqlStr.append("cr.tipoPessoa, cr.data, cr.situacao, cr.dataVencimento, cr.valorDescontoRateio, ");
		sqlStr.append("cr.valor, cr.pessoa, cr.nrDocumento, cr.tipoOrigem, cr.unidadeEnsino, cr.matriculaPeriodo, cr.matriculaAluno, cr.funcionario, cr.candidato, ");
		sqlStr.append("cr.convenio, cr.parceiro, cr.valorRecebido, cr.juro, cr.descricaoPagamento, cr.codOrigem, cr.juroPorcentagem, cr.multa, ");
		sqlStr.append("cr.acrescimo, cr.multaPorcentagem, cr.parcela, cr.descontoProgressivo, cr.valorDescontoRecebido, cr.tipoDesconto, ");
		sqlStr.append("cr.dataArquivoRemessa, cr.descontoprogressivoutilizado,cr.ordemConvenio, cr.OrdemConvenioValorCheio, cr.OrdemDescontoAluno, ");
		sqlStr.append("cr.OrdemDescontoAlunoValorCheio, cr.OrdemDescontoProgressivo, cr.OrdemDescontoProgressivoValorCheio, cr.OrdemPlanoDesconto, ");
		sqlStr.append("cr.OrdemPlanoDescontoValorCheio, cr.valorDesconto, cr.justificativaDesconto, cr.valorDescontoLancadoRecebimento, ");
		sqlStr.append("cr.valorCalculadoDescontoLancadoRecebimento, cr.tipoDescontoLancadoRecebimento, cr.valorDescontoAlunoJaCalculado, ");
		sqlStr.append("cr.impressaoBoletoRealizada, cr.valorDescontoCalculadoPrimeiraFaixaDescontos, cr.valorDescontoCalculadoSegundaFaixaDescontos, ");
		sqlStr.append("cr.valorDescontoCalculadoTerceiraFaixaDescontos, cr.valorDescontoCalculadoQuartaFaixaDescontos, cr.updated, cr.usaDescontoCompostoPlanoDesconto, ");
		sqlStr.append("cr.descontoInstituicao, cr.descontoConvenio, cr.valorDescontoProgressivo, cr.nossonumero, pdcr.codigo, pdcr.contaReceber, ");
		sqlStr.append("pdcr.planoDesconto, pdcr.tipoItemPlanoFinanceiro, pdcr.convenio, pdcr.valorutilizadorecebimento, ");
		sqlStr.append("pd.codigo, pd.nome, pd.percDescontoParcela, pd.percDescontoMatricula, pd.requisitos, pd.descricao, pd.somente1PeriodoLetivoParcela, ");
		sqlStr.append("pd.somente1PeriodoLetivoMatricula, pd.tipoDescontoParcela, pd.tipoDescontoMatricula, pd.diasValidadeVencimento, pd.ativo, ");
		sqlStr.append("pd.dataAtivacao, pd.dataInativacao, pd.responsavelAtivacao, pd.responsavelInativacao, pd.descontoValidoAteDataVencimento, ");
		sqlStr.append("c.codigo, c.descricao, c.dataAssinatura, c.parceiro, c.ativo, c.cobertura, c.preRequisitos, c.dataInicioVigencia, c.dataFinalVigencia, ");
		sqlStr.append("c.descontoMatricula, c.tipoDescontoMatricula, c.descontoParcela, c.tipoDescontoParcela, c.bolsaCusteadaParceiroMatricula, ");
		sqlStr.append("c.tipoBolsaCusteadaParceiroMatricula, c.bolsaCusteadaParceiroParcela, c.tipoBolsaCusteadaParceiroParcela, c.formaRecebimentoParceiro, ");
		sqlStr.append("c.diaBaseRecebimentoParceiro, c.requisitante, c.dataRequisicao, c.responsavelAutorizacao, c.dataAutorizacao, ");
		sqlStr.append("c.responsavelFinalizacao, c.dataFinalizacao, c.situacao, c.validoParaTodoCurso, c.validoParaTodaUnidadeEnsino, ");
		sqlStr.append("c.validoParaTodoTurno, c.periodoIndeterminado, c.dataAtivacao, c.dataInativacao, c.responsavelAtivacao, c.responsavelInativacao, ");
		sqlStr.append("c.descontoProgressivoParceiro, c.descontoProgressivoAluno, c.possuiDescontoAntecipacao, c.calculadoEmCimaValorLiquido, ");
		sqlStr.append("c.aplicarDescontoProgressivoMatricula, c.aplicarDescontoProgressivoMatriculaParceiro, dpa.codigo, dpa.nome, dpa.diaLimite1, ");
		sqlStr.append("dpa.percDescontoLimite1, dpa.diaLimite2, dpa.percDescontoLimite2, dpa.diaLimite3, dpa.percDescontoLimite3, dpa.diaLimite4, ");
		sqlStr.append("dpa.percDescontoLimite4, dpa.valorDescontoLimite1, dpa.valorDescontoLimite2, dpa.valorDescontoLimite3, dpa.valorDescontoLimite4, ");
		sqlStr.append("dpa.ativado, dpa.dataAtivacao , dpa.responsavelAtivacao, dpa.dataInativacao, dpa.responsavelInativacao, ");
		sqlStr.append("dpp.codigo, dpp.nome, dpp.diaLimite1, dpp.percDescontoLimite1, dpp.diaLimite2, dpp.percDescontoLimite2, dpp.diaLimite3, ");
		sqlStr.append("dpp.percDescontoLimite3, dpp.diaLimite4 , dpp.percDescontoLimite4, dpp.valorDescontoLimite1, dpp.valorDescontoLimite2, ");
		sqlStr.append("dpp.valorDescontoLimite3, dpp.valorDescontoLimite4, dpp.ativado, dpp.dataAtivacao , dpp.responsavelAtivacao, dpp.dataInativacao, ");
		sqlStr.append("dpp.responsavelInativacao, matriculaserasa, matricula.matricula ");

		sqlStr.append("order by cr.dataVencimento ");
		// //System.out.println(sqlStr.toString());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaRelatorioComDesconto(tabelaResultado, true, configuracao, usuarioVO);

	}

	public List<ContaReceberVO> montarDadosConsultaRelatorioComDesconto(SqlRowSet tabelaResultado, Boolean trazerValorFinalDescontoCalculado, ConfiguracaoFinanceiroVO configuracao, UsuarioVO usuario) throws Exception {
		List<ContaReceberVO> vetResultado = new ArrayList<ContaReceberVO>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosRelatorioComDesconto(tabelaResultado, trazerValorFinalDescontoCalculado, configuracao, usuario));
		}
		return vetResultado;
	}

	public ContaReceberVO montarDadosRelatorioComDesconto(SqlRowSet dadosSQL, Boolean trazerValorFinalDescontoCalculado, ConfiguracaoFinanceiroVO configuracao, UsuarioVO usuario) throws Exception {

		ContaReceberVO obj = new ContaReceberVO();
		obj.setDataVencimento(dadosSQL.getDate("cr.dataVencimento"));
		obj.setParcela(dadosSQL.getString("cr.parcela"));
		obj.setValor(dadosSQL.getDouble("cr.valor"));
		obj.setNossoNumero(dadosSQL.getString("cr.nossoNumero"));
		obj.setValorDescontoCalculadoPrimeiraFaixaDescontos(dadosSQL.getDouble("cr.valorDescontoCalculadoPrimeiraFaixaDescontos"));

		// contareceber novo
		obj.setCodigo(dadosSQL.getInt("codigocontareceber"));
		obj.setData(dadosSQL.getDate("cr.data"));
		obj.setSituacao(dadosSQL.getString("cr.situacao"));
		obj.setDataVencimento(dadosSQL.getDate("cr.dataVencimento"));
		obj.setValor(dadosSQL.getDouble("cr.valor"));
		obj.setNrDocumento(dadosSQL.getString("cr.nrDocumento"));
		obj.setTipoOrigem(dadosSQL.getString("cr.tipoOrigem"));
		obj.setNovoObj(Boolean.FALSE);
		obj.getMatriculaAluno().setMatricula(dadosSQL.getString("cr.matriculaAluno"));
		obj.setValorRecebido(dadosSQL.getDouble("cr.valorRecebido"));
		obj.setJuro(dadosSQL.getDouble("juro"));
		obj.setJuroPorcentagem(dadosSQL.getDouble("cr.juroPorcentagem"));
		obj.setMulta(dadosSQL.getDouble("multa"));
		obj.setAcrescimo(dadosSQL.getDouble("cr.acrescimo"));
		obj.setMultaPorcentagem(dadosSQL.getDouble("cr.multaPorcentagem"));
		obj.setParcela(dadosSQL.getString("cr.parcela"));
		obj.getDescontoProgressivo().setCodigo(dadosSQL.getInt("cr.descontoProgressivo"));
		obj.setValorDescontoRecebido(dadosSQL.getDouble("cr.valorDescontoRecebido"));
		obj.setTipoDesconto(dadosSQL.getString("cr.tipoDesconto"));
		obj.setDataArquivoRemessa(dadosSQL.getDate("cr.dataArquivoRemessa"));
		obj.setDescontoProgressivoUtilizado(FaixaDescontoProgressivo.getEnum(dadosSQL.getString("cr.descontoprogressivoutilizado")));
		obj.setOrdemConvenio(dadosSQL.getInt("cr.ordemConvenio"));
		obj.setOrdemConvenioValorCheio(dadosSQL.getBoolean("cr.OrdemConvenioValorCheio"));
		obj.setOrdemDescontoAluno(dadosSQL.getInt("cr.OrdemDescontoAluno"));
		obj.setOrdemDescontoAlunoValorCheio(dadosSQL.getBoolean("cr.OrdemDescontoAlunoValorCheio"));
		obj.setOrdemDescontoProgressivo(dadosSQL.getInt("cr.OrdemDescontoProgressivo"));
		obj.setOrdemDescontoProgressivoValorCheio(dadosSQL.getBoolean("cr.OrdemDescontoProgressivoValorCheio"));
		obj.setOrdemPlanoDesconto(dadosSQL.getInt("cr.OrdemPlanoDesconto"));
		obj.setOrdemPlanoDescontoValorCheio(dadosSQL.getBoolean("cr.OrdemPlanoDescontoValorCheio"));
		obj.setValorDesconto(dadosSQL.getDouble("cr.valorDesconto"));
		obj.setJustificativaDesconto(dadosSQL.getString("cr.justificativaDesconto"));
		obj.setValorDescontoLancadoRecebimento(dadosSQL.getDouble("cr.valorDescontoLancadoRecebimento"));
		obj.setValorCalculadoDescontoLancadoRecebimento(dadosSQL.getDouble("cr.valorCalculadoDescontoLancadoRecebimento"));
		obj.setValorDescontoRateio(dadosSQL.getDouble("cr.valorDescontoRateio"));
		obj.setTipoDescontoLancadoRecebimento(dadosSQL.getString("cr.tipoDescontoLancadoRecebimento"));
		obj.setValorDescontoAlunoJaCalculado(dadosSQL.getDouble("cr.valorDescontoAlunoJaCalculado"));
		obj.setImpressaoBoletoRealizada(dadosSQL.getBoolean("cr.impressaoBoletoRealizada"));
		obj.setValorDescontoCalculadoPrimeiraFaixaDescontos(dadosSQL.getDouble("cr.valorDescontoCalculadoPrimeiraFaixaDescontos"));
		obj.setValorDescontoCalculadoSegundaFaixaDescontos(dadosSQL.getDouble("cr.valorDescontoCalculadoSegundaFaixaDescontos"));
		obj.setValorDescontoCalculadoTerceiraFaixaDescontos(dadosSQL.getDouble("cr.valorDescontoCalculadoTerceiraFaixaDescontos"));
		obj.setValorDescontoCalculadoQuartaFaixaDescontos(dadosSQL.getDouble("cr.valorDescontoCalculadoQuartaFaixaDescontos"));
		obj.setUpdated(dadosSQL.getTimestamp("cr.updated"));
		obj.setUsaDescontoCompostoPlanoDesconto(dadosSQL.getBoolean("cr.usaDescontoCompostoPlanoDesconto"));

		calcularJuroMulta(obj, dadosSQL, configuracao, dadosSQL.getDouble("cr.multaPorcentagem"), dadosSQL.getDouble("cr.juroPorcentagem"));
		Optional<BigDecimal> valorIndiceReajustePorAtrasoOptional = Optional.ofNullable(dadosSQL.getBigDecimal("cr.valorIndiceReajustePorAtraso"));
		Optional<BigDecimal> valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessaOptional = Optional.ofNullable(dadosSQL.getBigDecimal("cr.valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa"));
		obj.setAcrescimo(obj.getAcrescimo() + valorIndiceReajustePorAtrasoOptional.orElse(BigDecimal.ZERO).doubleValue() + valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessaOptional.orElse(BigDecimal.ZERO).doubleValue());
		
		if (obj.getSituacaoEQuitada()) {
			// Se o titulo esta quitado temos que considerar os descontos
			// calculados,
			// na data da quitacao, nao chamando o método que monta a lista de
			// desconto
			// aplicaveis, considerando a quantidade de dias que falta para o
			// vencimento.
			obj.setValorDescontoInstituicao(dadosSQL.getDouble("cr.descontoInstituicao"));
			obj.setValorDescontoConvenio(dadosSQL.getDouble("cr.descontoConvenio"));
			obj.setValorDescontoProgressivo(dadosSQL.getDouble("cr.valorDescontoProgressivo"));
		}
		// DADOS PlanoDescontoContaReceberVO
		PlanoDescontoContaReceberVO planoDescontoContaReceberVO = null;

		// HASH PARA TRATAR LISTAS DE UM MESMO OBJETO
		HashMap<Integer, PlanoDescontoContaReceberVO> hashtablePlanoDescontoContaReceber = new HashMap<Integer, PlanoDescontoContaReceberVO>(0);

		do {
			if (!obj.getCodigo().equals(dadosSQL.getInt("codigocontareceber"))) {
				dadosSQL.previous();
				break;
			}
			planoDescontoContaReceberVO = new PlanoDescontoContaReceberVO();

			planoDescontoContaReceberVO.setCodigo(dadosSQL.getInt("pdcr.codigo"));
			planoDescontoContaReceberVO.setContaReceber(dadosSQL.getInt("pdcr.contaReceber"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setCodigo(dadosSQL.getInt("pdcr.planoDesconto"));
			planoDescontoContaReceberVO.setTipoItemPlanoFinanceiro(dadosSQL.getString("pdcr.tipoItemPlanoFinanceiro"));
			planoDescontoContaReceberVO.getConvenio().setCodigo(dadosSQL.getInt("pdcr.convenio"));
			planoDescontoContaReceberVO.setValorUtilizadoRecebimento(dadosSQL.getDouble("pdcr.valorutilizadorecebimento"));
			planoDescontoContaReceberVO.setNovoObj(Boolean.FALSE);

			planoDescontoContaReceberVO.getPlanoDescontoVO().setCodigo(new Integer(dadosSQL.getInt("pd.codigo")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setNome(dadosSQL.getString("pd.nome"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setPercDescontoParcela(new Double(dadosSQL.getDouble("pd.percDescontoParcela")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setPercDescontoMatricula(new Double(dadosSQL.getDouble("pd.percDescontoMatricula")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setRequisitos(dadosSQL.getString("pd.requisitos"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setDescricao(dadosSQL.getString("pd.descricao"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setSomente1PeriodoLetivoParcela(new Boolean(dadosSQL.getBoolean("pd.somente1PeriodoLetivoParcela")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setSomente1PeriodoLetivoMatricula(new Boolean(dadosSQL.getBoolean("pd.somente1PeriodoLetivoMatricula")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setTipoDescontoParcela(dadosSQL.getString("pd.tipoDescontoParcela"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setTipoDescontoMatricula(dadosSQL.getString("pd.tipoDescontoMatricula"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setDiasValidadeVencimento(new Integer(dadosSQL.getInt("pd.diasValidadeVencimento")));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setAtivo(dadosSQL.getBoolean("pd.ativo"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setDataAtivacao(dadosSQL.getDate("pd.dataAtivacao"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setDataInativacao(dadosSQL.getDate("pd.dataInativacao"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().getResponsavelAtivacao().setCodigo(dadosSQL.getInt("pd.responsavelAtivacao"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setDescontoValidoAteDataVencimento(dadosSQL.getBoolean("pd.descontoValidoAteDataVencimento"));
			planoDescontoContaReceberVO.getPlanoDescontoVO().setNovoObj(Boolean.FALSE);

			planoDescontoContaReceberVO.getConvenio().setCodigo(dadosSQL.getInt("c.codigo"));
			planoDescontoContaReceberVO.getConvenio().setDescricao(dadosSQL.getString("c.descricao"));
			planoDescontoContaReceberVO.getConvenio().setDataAssinatura(dadosSQL.getDate("c.dataAssinatura"));
			planoDescontoContaReceberVO.getConvenio().getParceiro().setCodigo(dadosSQL.getInt("c.parceiro"));
			planoDescontoContaReceberVO.getConvenio().setAtivo(dadosSQL.getBoolean("c.ativo"));
			planoDescontoContaReceberVO.getConvenio().setCobertura(dadosSQL.getString("c.cobertura"));
			planoDescontoContaReceberVO.getConvenio().setPreRequisitos(dadosSQL.getString("c.preRequisitos"));
			planoDescontoContaReceberVO.getConvenio().setDataInicioVigencia(dadosSQL.getDate("c.dataInicioVigencia"));
			planoDescontoContaReceberVO.getConvenio().setDataFinalVigencia(dadosSQL.getDate("c.dataFinalVigencia"));
			planoDescontoContaReceberVO.getConvenio().setDescontoMatricula(dadosSQL.getDouble("c.descontoMatricula"));
			planoDescontoContaReceberVO.getConvenio().setTipoDescontoMatricula(dadosSQL.getString("c.tipoDescontoMatricula"));
			planoDescontoContaReceberVO.getConvenio().setDescontoParcela(dadosSQL.getDouble("c.descontoParcela"));
			planoDescontoContaReceberVO.getConvenio().setTipoDescontoParcela(dadosSQL.getString("c.tipoDescontoParcela"));
			planoDescontoContaReceberVO.getConvenio().setBolsaCusteadaParceiroMatricula(dadosSQL.getDouble("c.bolsaCusteadaParceiroMatricula"));
			planoDescontoContaReceberVO.getConvenio().setTipoBolsaCusteadaParceiroMatricula(dadosSQL.getString("c.tipoBolsaCusteadaParceiroMatricula"));
			planoDescontoContaReceberVO.getConvenio().setBolsaCusteadaParceiroParcela(dadosSQL.getDouble("c.bolsaCusteadaParceiroParcela"));
			planoDescontoContaReceberVO.getConvenio().setTipoBolsaCusteadaParceiroParcela(dadosSQL.getString("c.tipoBolsaCusteadaParceiroParcela"));
			planoDescontoContaReceberVO.getConvenio().getFormaRecebimentoParceiro().setCodigo(dadosSQL.getInt("c.formaRecebimentoParceiro"));
			planoDescontoContaReceberVO.getConvenio().setDiaBaseRecebimentoParceiro(dadosSQL.getInt("c.diaBaseRecebimentoParceiro"));
			planoDescontoContaReceberVO.getConvenio().getRequisitante().setCodigo(dadosSQL.getInt("c.requisitante"));
			planoDescontoContaReceberVO.getConvenio().setDataRequisicao(dadosSQL.getDate("c.dataRequisicao"));
			planoDescontoContaReceberVO.getConvenio().getResponsavelAutorizacao().setCodigo(dadosSQL.getInt("c.responsavelAutorizacao"));
			planoDescontoContaReceberVO.getConvenio().setDataAutorizacao(dadosSQL.getDate("c.dataAutorizacao"));
			planoDescontoContaReceberVO.getConvenio().getResponsavelFinalizacao().setCodigo(dadosSQL.getInt("c.responsavelFinalizacao"));
			planoDescontoContaReceberVO.getConvenio().setDataFinalizacao(dadosSQL.getDate("c.dataFinalizacao"));
			planoDescontoContaReceberVO.getConvenio().setSituacao(dadosSQL.getString("c.situacao"));
			planoDescontoContaReceberVO.getConvenio().setValidoParaTodoCurso(dadosSQL.getBoolean("c.validoParaTodoCurso"));
			planoDescontoContaReceberVO.getConvenio().setValidoParaTodaUnidadeEnsino(dadosSQL.getBoolean("c.validoParaTodaUnidadeEnsino"));
			planoDescontoContaReceberVO.getConvenio().setValidoParaTodoTurno(dadosSQL.getBoolean("c.validoParaTodoTurno"));
			planoDescontoContaReceberVO.getConvenio().setPeriodoIndeterminado(dadosSQL.getBoolean("c.periodoIndeterminado"));
			planoDescontoContaReceberVO.getConvenio().setDataAtivacao(dadosSQL.getDate("c.dataAtivacao"));
			planoDescontoContaReceberVO.getConvenio().setDataInativacao(dadosSQL.getDate("c.dataInativacao"));
			planoDescontoContaReceberVO.getConvenio().getResponsavelAtivacao().setCodigo(dadosSQL.getInt("c.responsavelAtivacao"));
			planoDescontoContaReceberVO.getConvenio().getResponsavelInativacao().setCodigo(dadosSQL.getInt("c.responsavelInativacao"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setCodigo(dadosSQL.getInt("c.descontoProgressivoParceiro"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setCodigo(dadosSQL.getInt("c.descontoProgressivoAluno"));
			planoDescontoContaReceberVO.getConvenio().setPossuiDescontoAntecipacao(dadosSQL.getBoolean("c.possuiDescontoAntecipacao"));
			planoDescontoContaReceberVO.getConvenio().setCalculadoEmCimaValorLiquido(dadosSQL.getBoolean("c.calculadoEmCimaValorLiquido"));
			planoDescontoContaReceberVO.getConvenio().setAplicarDescontoProgressivoMatricula(dadosSQL.getBoolean("c.aplicarDescontoProgressivoMatricula"));
			planoDescontoContaReceberVO.getConvenio().setAplicarDescontoProgressivoMatriculaParceiro(dadosSQL.getBoolean("c.aplicarDescontoProgressivoMatriculaParceiro"));
			planoDescontoContaReceberVO.getConvenio().setNovoObj(Boolean.FALSE);

			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setCodigo(new Integer(dadosSQL.getInt("dpa.codigo")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setNome(dadosSQL.getString("dpa.nome"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDiaLimite1(new Integer(dadosSQL.getInt("dpa.diaLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setPercDescontoLimite1(new Double(dadosSQL.getDouble("dpa.percDescontoLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDiaLimite2(new Integer(dadosSQL.getInt("dpa.diaLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setPercDescontoLimite2(new Double(dadosSQL.getDouble("dpa.percDescontoLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDiaLimite3(new Integer(dadosSQL.getInt("dpa.diaLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setPercDescontoLimite3(new Double(dadosSQL.getDouble("dpa.percDescontoLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDiaLimite4(new Integer(dadosSQL.getInt("dpa.diaLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setPercDescontoLimite4(new Double(dadosSQL.getDouble("dpa.percDescontoLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setValorDescontoLimite1(new Double(dadosSQL.getDouble("dpa.valorDescontoLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setValorDescontoLimite2(new Double(dadosSQL.getDouble("dpa.valorDescontoLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setValorDescontoLimite3(new Double(dadosSQL.getDouble("dpa.valorDescontoLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setValorDescontoLimite4(new Double(dadosSQL.getDouble("dpa.valorDescontoLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setAtivado(dadosSQL.getBoolean("dpa.ativado"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDataAtivacao(dadosSQL.getDate("dpa.dataAtivacao"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().getResponsavelAtivacao().setCodigo(new Integer(dadosSQL.getInt("dpa.responsavelAtivacao")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setDataInativacao(dadosSQL.getDate("dpa.dataInativacao"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().getResponsavelInativacao().setCodigo(new Integer(dadosSQL.getInt("dpa.responsavelInativacao")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoAluno().setNovoObj(Boolean.FALSE);

			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setCodigo(new Integer(dadosSQL.getInt("dpp.codigo")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setNome(dadosSQL.getString("dpp.nome"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDiaLimite1(new Integer(dadosSQL.getInt("dpp.diaLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setPercDescontoLimite1(new Double(dadosSQL.getDouble("dpp.percDescontoLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDiaLimite2(new Integer(dadosSQL.getInt("dpp.diaLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setPercDescontoLimite2(new Double(dadosSQL.getDouble("dpp.percDescontoLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDiaLimite3(new Integer(dadosSQL.getInt("dpp.diaLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setPercDescontoLimite3(new Double(dadosSQL.getDouble("dpp.percDescontoLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDiaLimite4(new Integer(dadosSQL.getInt("dpp.diaLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setPercDescontoLimite4(new Double(dadosSQL.getDouble("dpp.percDescontoLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setValorDescontoLimite1(new Double(dadosSQL.getDouble("dpp.valorDescontoLimite1")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setValorDescontoLimite2(new Double(dadosSQL.getDouble("dpp.valorDescontoLimite2")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setValorDescontoLimite3(new Double(dadosSQL.getDouble("dpp.valorDescontoLimite3")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setValorDescontoLimite4(new Double(dadosSQL.getDouble("dpp.valorDescontoLimite4")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setAtivado(dadosSQL.getBoolean("dpp.ativado"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDataAtivacao(dadosSQL.getDate("dpp.dataAtivacao"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().getResponsavelAtivacao().setCodigo(new Integer(dadosSQL.getInt("dpp.responsavelAtivacao")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setDataInativacao(dadosSQL.getDate("dpp.dataInativacao"));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().getResponsavelInativacao().setCodigo(new Integer(dadosSQL.getInt("dpp.responsavelInativacao")));
			planoDescontoContaReceberVO.getConvenio().getDescontoProgressivoParceiro().setNovoObj(Boolean.FALSE);

			if (!hashtablePlanoDescontoContaReceber.containsKey(planoDescontoContaReceberVO.getCodigo()) && planoDescontoContaReceberVO.getCodigo() != 0) {
				obj.getPlanoDescontoContaReceberVOs().add(planoDescontoContaReceberVO);
			}
			hashtablePlanoDescontoContaReceber.put(planoDescontoContaReceberVO.getCodigo(), planoDescontoContaReceberVO);
			if (dadosSQL.isLast()) {
				break;
			}
		} while (dadosSQL.next());

		if (!obj.getSituacaoEQuitada()) {
			// Monta a lista de descontos validos para a conta a receber.
			// Adicionalmente, com base na quantidade
			// de dias que faltam para o vencimento do titulo a rotina tambem já
			// define quais descontos devem
			// ser aplicadas para a conta receber, inicializando os mesmos nos
			// devidos campos
			// descontoConvenio, descontoProgressivo, descontoAluno,
			// descontoInstituicao
			obj.setRealizandoRecebimento(true);
			obj.getCalcularValorFinal(new Date(), configuracao, false, new Date(), usuario);									
		}

		obj.setValorFinalCalculado(Uteis.arredondar((obj.getValor() + obj.getJuro() + obj.getMulta() - (obj.getValorDescontoConvenio() + obj.getValorDescontoInstituicao() + obj.getValorDescontoAlunoJaCalculado() + obj.getValorDescontoRateio())), 2, 0));
		// obj.setCont(1);
		return obj;
	}

	@Override
	public List<HistoricoAlunoRelVO> executarGeracaoDocumentoIntegralizacao(List<MatriculaVO> matriculaVOs, UsuarioVO usuario, List<UnidadeEnsinoVO> listaUnidadeEnsinoVOs, ConfiguracaoFinanceiroVO configuracaoFinanceiraVO, String ano, String semestre, FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) throws Exception {
		HistoricoAlunoRelVO historicoTemp = null;
		List<HistoricoAlunoRelVO> historicoAlunoRelVOs = new ArrayList<HistoricoAlunoRelVO>(0);
		
		for (MatriculaVO matricula : matriculaVOs) {
			HistoricoAlunoRelVO histAlunoRelVO = new HistoricoAlunoRelVO();
			getFacadeFactory().getMatriculaFacade().carregarDados(matricula, NivelMontarDados.TODOS, usuario);
			historicoTemp = getFacadeFactory().getHistoricoAlunoRelFacade().criarObjeto(histAlunoRelVO, matricula, matricula.getGradeCurricularAtual(), filtroRelatorioAcademicoVO, 3, "", "", "aluno", "DocumentoIntegralizacaoCurricularRel", Boolean.TRUE, new Date(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, usuario, Boolean.TRUE, false, false, "", false, false, false, "PROFESSOR_TURMA_BASE", false, null);
			historicoTemp.setApresentarFrequencia(Boolean.FALSE);
			historicoTemp.setListaDocumentacaoPendente(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculaPorMatriculaAlunoEntregue(matricula.getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, false, usuario));
			int aux = historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs().size();
			historicoTemp.setListaDisciplinasACursar(historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs());
			getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatriculaHistoricoDisciplinaACursar(matricula.getMatricula(), 3, historicoTemp.getListaDisciplinasACursar(), false, usuario);
			historicoTemp.setListaDisciplinasACursar(historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs().subList(aux, historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs().size()));
			historicoTemp.setHistoricoAlunoDisciplinaRelVOs(historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs().subList(0, aux));
			SituacaoFinanceiraAlunoRelVO situacaoFinanceiraAluno = (SituacaoFinanceiraAlunoRelVO) getFacadeFactory().getContaReceberAlunosRelFacade().criarObjeto(matricula, new SituacaoFinanceiraAlunoRelVO(), "aluno", new ParceiroVO(), usuario, listaUnidadeEnsinoVOs, configuracaoFinanceiraVO, matricula.getAluno(), ano, semestre, null, null, false, filtroRelatorioFinanceiroVO, null, "").get(0);
			historicoTemp.setSituacaoFinanceiraAluno(situacaoFinanceiraAluno);
			historicoTemp.setLivrosEmprestados(getFacadeFactory().getEmprestimoRelFacade().consultar(this.montaObjetoLivroEmprestado(matricula)));
			historicoAlunoRelVOs.add(historicoTemp);
		}
		return historicoAlunoRelVOs;

	}

	public EmprestimoFiltroRelVO montaObjetoLivroEmprestado(MatriculaVO matriculaVO) throws Exception {
		EmprestimoFiltroRelVO obj = new EmprestimoFiltroRelVO();
		obj.setSituacaoEmprestimo("EM");
		obj.setTipoPessoa("AL");
		obj.setMatriculaVO(matriculaVO);
		Calendar data = Calendar.getInstance();
		data.set(1800, 1, 1);
		obj.setDataInicio(data.getTime());
		obj.setDataFim(new Date());
		obj.setUnidadeEnsinoVO(matriculaVO.getUnidadeEnsino());
		obj.setOrdenarPor("dataEmprestimo");
		return obj;
	}

	public void calcularJuroMulta(ContaReceberVO obj, SqlRowSet dadosSQL, ConfiguracaoFinanceiroVO configuracao, Double multaPorcentagem, Double jurosPorcentagem) throws Exception {
		Double valorFinal = 0.0;
		valorFinal = obj.getValor();
		Long diasAtraso = 0L;
		diasAtraso = Uteis.nrDiasEntreDatas(new Date(), obj.getDataVencimento());
		if (diasAtraso > 0) {
			if (multaPorcentagem == 0) {
				multaPorcentagem = (configuracao.getPercentualMultaPadrao());
			}
			double valorComMulta = (valorFinal * (multaPorcentagem) / 100);
			obj.setMulta(valorComMulta);
			if (configuracao.getTipoCalculoJuro().equals("CO")) {
				if (jurosPorcentagem == 0) {
					jurosPorcentagem = (configuracao.getPercentualJuroPadrao());
				}
				double atraso = (diasAtraso.doubleValue() / 30);
				double valorComJuro = (valorFinal * Math.pow(((jurosPorcentagem / 100) + 1), Uteis.arredondar(atraso, 2, 0))) - valorFinal;
				obj.setJuro(Uteis.arredondar(valorComJuro, 2, 0));
			} else {
				if (jurosPorcentagem == 0) {
					jurosPorcentagem = (configuracao.getPercentualJuroPadrao());
				}
				double valorComJuro = (valorFinal * (jurosPorcentagem / 100) * 1);
				obj.setJuro(Uteis.arredondar(((valorComJuro / 30) * diasAtraso), 2, 0));
			}
			obj.setValorFinalCalculado(Uteis.arredondar((valorFinal + obj.getJuro() + obj.getMulta() + obj.getAcrescimo() - obj.getValorDesconto()), 2, 0));
		} else {
			obj.setJuro(dadosSQL.getDouble("juro"));
			obj.setMulta(dadosSQL.getDouble("multa"));
			obj.setValorFinalCalculado(Uteis.arredondar((obj.getValor() + obj.getJuro() + obj.getMulta() + obj.getAcrescimo() - obj.getValorDesconto()), 2, 0));
		}
	}

}
