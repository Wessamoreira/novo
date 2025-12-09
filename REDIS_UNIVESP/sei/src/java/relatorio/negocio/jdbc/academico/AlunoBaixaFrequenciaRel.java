package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.academico.AlunoBaixaFrequenciaRelVO;
import relatorio.negocio.interfaces.academico.AlunoBaixaFrequenciaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Service
@Scope
@Lazy
public class AlunoBaixaFrequenciaRel extends SuperRelatorio implements AlunoBaixaFrequenciaRelInterfaceFacade {
	
	private static final long serialVersionUID = -4317116806576730449L;
	
	protected static String idEntidade;
	
	public AlunoBaixaFrequenciaRel() {
		super();
		setIdEntidade("AlunoBaixaFrequenciaRel");
	}

	@Override
	public List<AlunoBaixaFrequenciaRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, List<CursoVO> listaCurso, TurmaVO turmaVO, Integer percentualFrequencia, FiltroAlunoBaixaFrequenciaVO filtroAlunoBaixaFrequenciaVO, Integer idPessoa ,boolean isVisaoCoordenador) throws Exception  {
		StringBuilder sql = new StringBuilder("");
		sql.append("with cte_turma as ( ");
		sql.append(" select turma.codigo as turma, turma.turmaagrupada, turma.subturma, turma.tiposubturma,  disciplina.codigo as disciplina, count(horarioturmadiaitem.codigo) as qtdeAula from horarioturma ");
		sql.append("inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo ");
		sql.append("inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia ");
		sql.append("inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina ");
		sql.append("inner join turma on turma.codigo = horarioturma.turma ");
		sql.append("where ");
		
		if(filtroAlunoBaixaFrequenciaVO.getFiltrarPorAnoSemestre()) {
			sql.append(" anovigente = '").append(filtroAlunoBaixaFrequenciaVO.getAno()).append("' ");
			sql.append(" and semestrevigente = '").append(filtroAlunoBaixaFrequenciaVO.getSemestre()).append("' ");
		}else if(filtroAlunoBaixaFrequenciaVO.getFiltrarPorAno()) {
			sql.append(" anovigente = '").append(filtroAlunoBaixaFrequenciaVO.getAno()).append("' ");
		}
		
		sql.append("group by  turma.codigo, turma.turmaagrupada, turma.subturma, turma.tiposubturma,  disciplina.codigo ) ");
		
		sql.append("select matricula.matricula, unidadeensino.nome as unidadeensino, curso.nome as curso, coordenador.codigo as codigo_coordenador, coordenador.nome as nome_coordenador, coordenador.email as email_coordenador, ");
		sql.append("aluno.nome as aluno, historico.totalfalta, sum(cte_turma.qtdeAula) as totalAulasProgramadas, ");
		sql.append("(historico.totalfalta*100/ sum(cte_turma.qtdeAula))::numeric(20,2) as porcentagemFaltas, ");
		sql.append("turma.codigo, turma.identificadorturma,  disciplina.codigo as codigo_disciplina,  disciplina.nome as disciplina ");
		sql.append("from matriculaperiodoturmadisciplina ");
		sql.append("inner join matricula on matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sql.append("inner join unidadeensino on matricula.unidadeEnsino = unidadeensino.codigo ");
		sql.append("inner join curso on curso.codigo = matricula.curso ");
		sql.append("inner join disciplina on matriculaperiodoturmadisciplina.disciplina = disciplina.codigo ");
		sql.append("inner join pessoa as aluno on aluno.codigo = matricula.aluno ");
		sql.append("inner join historico on historico.matricula = matricula.matricula ");
		sql.append("and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		sql.append("inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		
		sql.append("inner join cursocoordenador on cursocoordenador.curso = curso.codigo and unidadeensino.codigo = cursocoordenador.unidadeensino ");
		sql.append("inner join funcionario on cursocoordenador.funcionario = funcionario.codigo ");
		sql.append("inner join pessoa as coordenador on coordenador.codigo = funcionario.pessoa ");
		if(isVisaoCoordenador) {
			sql.append("and funcionario.pessoa =").append(idPessoa).append(" ");
		}
		
		sql.append("inner join cte_turma on ((cte_turma.turmaagrupada = false and cte_turma.turma = matriculaperiodoturmadisciplina.turma ");
		sql.append("and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turmateorica is null and cte_turma.disciplina = disciplina.codigo) ");
		sql.append("or (cte_turma.subturma = true and cte_turma.tiposubturma = 'PRATICA' and cte_turma.turma = matriculaperiodoturmadisciplina.turmapratica  and cte_turma.disciplina = disciplina.codigo) ");
		sql.append("or (cte_turma.subturma = true and cte_turma.tiposubturma = 'TEORICA' and cte_turma.turma = matriculaperiodoturmadisciplina.turmateorica  and cte_turma.disciplina = disciplina.codigo) ) ");
		sql.append("where historico.totalfalta > 0 ");
		
		if(filtroAlunoBaixaFrequenciaVO.getFiltrarPorAnoSemestre()) {
			sql.append(" and matriculaperiodoturmadisciplina.ano = '").append(filtroAlunoBaixaFrequenciaVO.getAno()).append("' ");
			sql.append(" and matriculaperiodoturmadisciplina.semestre = '").append(filtroAlunoBaixaFrequenciaVO.getSemestre()).append("' ");
		}else if(filtroAlunoBaixaFrequenciaVO.getFiltrarPorAno()) {
			sql.append(" and matriculaperiodoturmadisciplina.ano = '").append(filtroAlunoBaixaFrequenciaVO.getAno()).append("' ");
		}
		
		if(!listaUnidadeEnsino.isEmpty()) {
			sql.append(adicionarFiltroUnidadeEnsino(listaUnidadeEnsino));
		}
		
		if(listaCurso != null && !listaCurso.isEmpty()) {
			sql.append(adicionarFiltroCurso(listaCurso));
		}
		
		if(Uteis.isAtributoPreenchido(turmaVO.getIdentificadorTurma())) {
			sql.append(" and turma.identificadorturma =  '").append(turmaVO.getIdentificadorTurma()).append("' ");
		}
		
		sql.append("group by matricula.matricula, unidadeensino.nome, coordenador.codigo ,coordenador.nome, coordenador.email, curso.nome, aluno.nome, historico.totalfalta, turma.codigo, turma.identificadorturma,  ");
		sql.append("disciplina.codigo,  disciplina.nome, matriculaperiodoturmadisciplina.ano, matriculaperiodoturmadisciplina.semestre, ");
		sql.append("matriculaperiodoturmadisciplina.turmateorica, matriculaperiodoturmadisciplina.turmapratica, matriculaperiodoturmadisciplina.turma ");
		sql.append("having historico.totalfalta * 100 / sum(cte_turma.qtdeAula) > ");
		
		if(!Uteis.isAtributoPreenchido(percentualFrequencia)) {
			sql.append("(select percentualbaixafrequencia from configuracaogeralsistema inner join configuracoes on configuracoes.codigo = configuracaogeralsistema.configuracoes and configuracoes.padrao = true) ");
		}else {
			sql.append(percentualFrequencia).append(" ");
		}
		
		sql.append("union all ");
		
		sql.append("select matricula.matricula, unidadeensino.nome as unidadeensino, curso.nome as curso, coordenador.codigo as codigo_coordenador, coordenador.nome as nome_coordenador , coordenador.email as email_coordenador, ");
		sql.append("aluno.nome as aluno, historico.totalfalta,   sum(cte_turma.qtdeAula) as totalAulasProgramadas, ");
		sql.append("(historico.totalfalta*100/ sum(cte_turma.qtdeAula))::numeric(20,2) as porcentagemFaltas, ");
		sql.append("turma.codigo, turma.identificadorturma,  disciplina.codigo as codigo_disciplina,  disciplina.nome as disciplina ");
		sql.append("from matriculaperiodoturmadisciplina ");
		sql.append("inner join matricula on matriculaperiodoturmadisciplina.matricula = matricula.matricula ");
		sql.append("inner join unidadeensino on matricula.unidadeEnsino = unidadeensino.codigo ");
		sql.append("inner join curso on curso.codigo = matricula.curso ");
		sql.append("inner join disciplina on matriculaperiodoturmadisciplina.disciplina = disciplina.codigo ");
		sql.append("inner join pessoa as aluno on aluno.codigo = matricula.aluno ");
		sql.append("inner join historico on historico.matricula = matricula.matricula ");
		sql.append("and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina ");
		sql.append("inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma ");
		
		sql.append("inner join cursocoordenador on cursocoordenador.curso = curso.codigo and unidadeensino.codigo = cursocoordenador.unidadeensino ");
		sql.append("inner join funcionario on cursocoordenador.funcionario = funcionario.codigo ");
		sql.append("inner join pessoa as coordenador on coordenador.codigo = funcionario.pessoa ");
		if(isVisaoCoordenador) {
			sql.append("and funcionario.pessoa =").append(idPessoa).append(" ");
		}
		 
		sql.append("inner join cte_turma on  cte_turma.turmaagrupada = true and cte_turma.subturma = false and matriculaperiodoturmadisciplina.turmateorica is null and matriculaperiodoturmadisciplina.turmapratica is null ");
		sql.append("and exists (select turmaagrupada.codigo from turmaagrupada where turmaagrupada.turma = matriculaperiodoturmadisciplina.turma and turmaagrupada.turmaorigem = cte_turma.turma) ");
		sql.append("and cte_turma.disciplina in ( ");
		sql.append("select disciplina.codigo ");
		sql.append("union all ");
		sql.append("select disciplinaequivalente.equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = cte_turma.disciplina and disciplina.codigo = disciplinaequivalente.equivalente ");
		sql.append("union all ");
		sql.append("select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = cte_turma.disciplina and disciplina.codigo = disciplinaequivalente.disciplina ) ");
		sql.append("where historico.totalfalta > 0  ");
		
		if(filtroAlunoBaixaFrequenciaVO.getFiltrarPorAnoSemestre()) {
			sql.append(" and matriculaperiodoturmadisciplina.ano = '").append(filtroAlunoBaixaFrequenciaVO.getAno()).append("' ");
			sql.append(" and matriculaperiodoturmadisciplina.semestre = '").append(filtroAlunoBaixaFrequenciaVO.getSemestre()).append("' ");
		}else if(filtroAlunoBaixaFrequenciaVO.getFiltrarPorAno()) {
			sql.append(" and matriculaperiodoturmadisciplina.ano = '").append(filtroAlunoBaixaFrequenciaVO.getAno()).append("' ");
		}
		
		if(!listaUnidadeEnsino.isEmpty()) {
			sql.append(adicionarFiltroUnidadeEnsino(listaUnidadeEnsino));
		}
		
		if(!listaCurso.isEmpty()) {
			sql.append(adicionarFiltroCurso(listaCurso));
		}
		
		if(Uteis.isAtributoPreenchido(turmaVO.getIdentificadorTurma())) {
			sql.append(" and turma.identificadorturma =  '").append(turmaVO.getIdentificadorTurma()).append("' ");
		}
		
		sql.append("group by matricula.matricula,unidadeensino.nome, curso.nome, coordenador.codigo , coordenador.nome, coordenador.email, aluno.nome, historico.totalfalta, turma.codigo, turma.identificadorturma, ");
		sql.append("disciplina.codigo,  disciplina.nome, matriculaperiodoturmadisciplina.ano, matriculaperiodoturmadisciplina.semestre, ");
		sql.append("matriculaperiodoturmadisciplina.turmateorica, matriculaperiodoturmadisciplina.turmapratica, matriculaperiodoturmadisciplina.turma ");
		sql.append("having historico.totalfalta * 100 / sum(cte_turma.qtdeAula) > ");
		
		if(!Uteis.isAtributoPreenchido(percentualFrequencia)) {
			sql.append("(select percentualbaixafrequencia from configuracaogeralsistema inner join configuracoes on configuracoes.codigo = configuracaogeralsistema.configuracoes and configuracoes.padrao = true)");
		}else {
			sql.append(percentualFrequencia).append(" ");
		}
		
		
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosAlunosComBaixaFrequencia(rs);
	}
	
	
	
	private List<AlunoBaixaFrequenciaRelVO> montarDadosAlunosComBaixaFrequencia(SqlRowSet rs) throws Exception {
		List<AlunoBaixaFrequenciaRelVO> alunosBaixaFrequencia = new ArrayList<AlunoBaixaFrequenciaRelVO>(0);
		AlunoBaixaFrequenciaRelVO alunoBaixaFrequenciaRelVO = null;
		while (rs.next()) {
			alunoBaixaFrequenciaRelVO = new AlunoBaixaFrequenciaRelVO();
			alunoBaixaFrequenciaRelVO.setMatricula(rs.getString("matricula"));
			alunoBaixaFrequenciaRelVO.setNomeAluno(rs.getString("aluno"));
			
			
			alunoBaixaFrequenciaRelVO.setNomeUnidadeEnsino(rs.getString("unidadeensino"));
			
			alunoBaixaFrequenciaRelVO.getCoordenador().setCodigo(rs.getInt("codigo_coordenador"));
			alunoBaixaFrequenciaRelVO.getCoordenador().setNome(rs.getString("nome_coordenador"));
			alunoBaixaFrequenciaRelVO.getCoordenador().setEmail(rs.getString("email_coordenador"));
			
			alunoBaixaFrequenciaRelVO.setCurso(rs.getString("curso"));
			alunoBaixaFrequenciaRelVO.setDisciplina(rs.getString("disciplina"));
			alunoBaixaFrequenciaRelVO.setTurma(rs.getString("identificadorturma"));
		
			
			alunoBaixaFrequenciaRelVO.setTotalAulasProgramadas(rs.getInt("totalAulasProgramadas"));
			alunoBaixaFrequenciaRelVO.setPercentualFaltas(rs.getDouble("porcentagemfaltas"));
			alunoBaixaFrequenciaRelVO.setTotalFaltas(rs.getInt("totalfalta"));
			
			alunosBaixaFrequencia.add(alunoBaixaFrequenciaRelVO);
		}
		return alunosBaixaFrequencia;
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
	
	public static String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getDesignIReportRelatorio() {
		return (caminhoBaseRelatorio() + "AlunosBaixaFrequenciaRel" + ".jrxml");
	}
	
	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		AlunoBaixaFrequenciaRel.idEntidade = idEntidade;
	}
}
