package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.protocolo.TipoRequerimentoDepartamentoVO;
import negocio.comuns.protocolo.enumeradores.SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.academico.RequerimentoRelVO;
import relatorio.negocio.interfaces.academico.RequerimentoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
public class RequerimentoRel extends SuperRelatorio implements RequerimentoRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public RequerimentoRel() throws Exception {
	}

	public void validarDados(Date dataInicio, Date dataFim, List<UnidadeEnsinoVO> unidadeEnsinoVOs) throws Exception {
		if (dataFim == null || dataInicio == null) {
			throw new ConsistirException("O período deve ser informado para a geração do relatório.");
		}
		if (dataFim.before(dataInicio)) {
			throw new ConsistirException("A Data Fim está menor que a Data Início.");
		}
		boolean excessao = true;
		for (UnidadeEnsinoVO obj : unidadeEnsinoVOs) {
			if (obj.getFiltrarUnidadeEnsino()) {
				excessao = false;
				break;
			}
		}
		if (excessao) {
			throw new ConsistirException("Ao menos uma Unidade de Ensino deve ser selecionada!");
		}
	}

	public List<RequerimentoRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUniade, Boolean finalizadoDeferido, Boolean finalizadoIndeferido, Boolean emExecucao, Boolean pendente, Boolean aguardandoPagamento, Boolean aguardandoAutorizacaoPagamento, Boolean isento, Boolean pago, Boolean canceladoFinanceiro, Boolean solicitacaoIsencao, Boolean solicitacaoIsencaoDeferido, Boolean solicitacaoIsencaoIndeferido, Boolean prontoParaRetirada, Boolean atrasado, RequerimentoRelVO requerimentoRelVO, Date dataInicio, Date dataFim, Integer funcionario, Integer departamento, Integer disciplina, String layout, CursoVO curso, TurmaVO turma, List<CursoVO> cursoVOs, PessoaVO requerente, Integer situacaoRequerimentoDepartamento, PessoaVO coordenador, String filtrarPeriodoPor, TurmaVO turmaReposicao) throws Exception {
		List<RequerimentoRelVO> listaRequerimentoRelVO = new ArrayList<RequerimentoRelVO>(0);
		SqlRowSet tabelaResultado = executarConsultaParametrizada(listaUniade, finalizadoDeferido, finalizadoIndeferido, emExecucao, pendente, aguardandoPagamento, aguardandoAutorizacaoPagamento, isento, pago,  canceladoFinanceiro,  solicitacaoIsencao,  solicitacaoIsencaoDeferido,  solicitacaoIsencaoIndeferido, prontoParaRetirada, atrasado, requerimentoRelVO, dataInicio, dataFim, funcionario, departamento, disciplina, layout, curso, turma, cursoVOs, requerente, situacaoRequerimentoDepartamento, coordenador, filtrarPeriodoPor, turmaReposicao);
		while (tabelaResultado.next()) {
			requerimentoRelVO.getListaRequerimentoVO().add(montarDados(tabelaResultado, requerimentoRelVO, layout));
		}
		if (!requerimentoRelVO.getListaRequerimentoVO().isEmpty()) {
			if (requerimentoRelVO.getTipoRequerimentoVO().getCodigo().equals(0)) {
				requerimentoRelVO.getTipoRequerimentoVO().setNome("Todos");
			} else {
				requerimentoRelVO.setTipoRequerimentoVO(getFacadeFactory().getTipoRequerimentoFacade().consultarPorChavePrimaria(requerimentoRelVO.getTipoRequerimentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, null));
			}
			if (requerimentoRelVO.getUnidadeEnsinoVO().getCodigo().equals(0)) {
				requerimentoRelVO.getUnidadeEnsinoVO().setNome("Todas");
			} else {
				requerimentoRelVO.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(requerimentoRelVO.getUnidadeEnsinoVO().getCodigo(), false, null));
			}
			listaRequerimentoRelVO.add(requerimentoRelVO);
		}
		return listaRequerimentoRelVO;
	}
	
	
	public List<RequerimentoVO> criarObjetoRequerimentoTipoTCC(List<UnidadeEnsinoVO> listaUniade, Boolean finalizadoDeferido, Boolean finalizadoIndeferido, Boolean emExecucao, Boolean pendente, Boolean aguardandoPagamento, Boolean aguardandoAutorizacaoPagamento, Boolean isento, Boolean pago, Boolean canceladoFinanceiro, Boolean solicitacaoIsencao, Boolean solicitacaoIsencaoDeferido, Boolean solicitacaoIsencaoIndeferido, Boolean prontoParaRetirada, Boolean atrasado, Date dataInicio, Date dataFim, Integer funcionario, Integer departamento, Integer disciplina, String matricula, CursoVO curso, TurmaVO turma, List<CursoVO> cursoVOs, PessoaVO requerente, Integer situacaoRequerimentoDepartamento, PessoaVO coordenador, String filtrarPeriodoPor, TurmaVO turmaReposicao, Integer codTipoReq, UsuarioVO usuario) throws Exception {
		List<RequerimentoVO> listaRequerimentoVO = new ArrayList<RequerimentoVO>();
		SqlRowSet tabelaResultado = executarConsultaParametrizada(listaUniade, finalizadoDeferido, finalizadoIndeferido, emExecucao, pendente, aguardandoPagamento, aguardandoAutorizacaoPagamento, isento, pago,  canceladoFinanceiro,  solicitacaoIsencao,  solicitacaoIsencaoDeferido,  solicitacaoIsencaoIndeferido, prontoParaRetirada, atrasado, dataInicio, dataFim, funcionario, departamento, disciplina, curso, turma, cursoVOs, requerente, situacaoRequerimentoDepartamento, coordenador, filtrarPeriodoPor, turmaReposicao,codTipoReq,matricula);
		while (tabelaResultado.next()) {
			listaRequerimentoVO.add(montarDados(tabelaResultado, usuario));
		}
		return listaRequerimentoVO;
	}
	
	
	public SqlRowSet executarConsultaParametrizada(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Boolean finalizadoDeferido, Boolean finalizadoIndeferido, Boolean emExecucao, Boolean pendente, Boolean aguardandoPagamento, Boolean aguardandoAutorizacaoPagamento, Boolean isento, Boolean pago, Boolean canceladoFinanceiro, Boolean solicitacaoIsencao, Boolean solicitacaoIsencaoDeferido, Boolean solicitacaoIsencaoIndeferido, Boolean prontoRetirada, Boolean atrasado, Date dataInicio, Date dataFim, Integer funcionario, Integer departamento, Integer disciplina, CursoVO curso, TurmaVO turma, List<CursoVO> cursoVOs, PessoaVO requerente, Integer situacaoRequerimentoDepartamento, PessoaVO coordenador, String filtrarPeriodoPor, TurmaVO turmaReposicao, Integer codTipoReq, String matricula) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select distinct requerimento.situacao as \"requerimentoSituacao\", ");
		sqlStr.append("funcionario.codigo as funcionarioCodigo, pessoaFunc.nome as funcionarioNome, tr.codigo as \"trCodigo\", tr.nome as \"trNome\" ");
		sqlStr.append(", requerimento.codigo as \"requerimentoCodigo\", requerimento.data as \"requerimentoData\", pessoa.codigo as \"pessoaCodigo\", ");
		sqlStr.append("pessoa.nome as \"pessoaNome\", pessoa.email as \"pessoaEmail\", requerimento.data as \"requerimentoData\", requerimento.valor as \"requerimentoValor\", ");
		sqlStr.append("unidadeEnsino.codigo as \"unidadeEnsinoCodigo\", unidadeEnsino.nome as \"unidadeEnsinoNome\", turma.identificadorTurma  as identificadorTurma, ");
		
		sqlStr.append(" curso.codigo as \"cursoCodigo\", curso.nome as \"cursoNome\", ");
		sqlStr.append(" matricula.nomemonografia as \"matricula.nomemonografia\", matricula.matricula as \"matricula.matricula\", matricula.notamonografia as \"matricula.notamonografia\", ");
		
		sqlStr.append("disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, situacaorequerimentodepartamento.situacao as situacaoRequerimentoDepartamento, requerimento.dataUltimaAlteracao as dataUltimaAlteracaoRequerimento ");
		sqlStr.append(" , usuario.nome as \"nomeResponsavel\", unidadeEnsino.nome, unidadeEnsino.nome as \"unidadeEnsinoNome\" ");
				
		sqlStr.append(", coordenador.nome as coordenador ");
		sqlStr.append(", requerimentoHistorico.dataEntradaDepartamento as dataEntradaDepartamento, responsavelTramite.nome as responsavelTramite, departamentoResponsavelTramite.nome as departamentoResponsavelTramite ");
		sqlStr.append(", requerimento.dataFinalizacao as \"requerimentoDataFinalizacao\", requerimento.dataprevistafinalizacao as dataprevistafinalizacao ");
		
		sqlStr.append("from requerimento ");
		sqlStr.append("inner join tipoRequerimento tr on tr.codigo = requerimento.tipoRequerimento ");
		sqlStr.append("inner join pessoa on pessoa.codigo = requerimento.pessoa ");
		sqlStr.append(" left join unidadeEnsino on unidadeEnsino.codigo = requerimento.unidadeEnsino ");
		sqlStr.append(" left join matricula on matricula.matricula = requerimento.matricula ");
		sqlStr.append(" left join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append(" and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where  matricula.matricula = mp.matricula and mp.data<= requerimento.data order by mp.codigo desc limit 1) ");
		sqlStr.append(" left join turma on turma.codigo = matriculaperiodo.turma ");
		sqlStr.append(" left join disciplina on disciplina.codigo = requerimento.disciplina ");
		sqlStr.append(" left join funcionario on funcionario.codigo = requerimento.funcionario ");
		sqlStr.append(" left join pessoa as pessoaFunc on pessoaFunc.codigo = funcionario.pessoa ");
		sqlStr.append(" left join curso on matricula.curso = curso.codigo ");
		sqlStr.append(" left join situacaorequerimentodepartamento on requerimento.situacaorequerimentodepartamento = situacaorequerimentodepartamento.codigo ");		
		sqlStr.append(" left join cursocoordenador on cursocoordenador.curso = matricula.curso ");
		sqlStr.append(" and cursocoordenador.unidadeensino = matricula.unidadeensino ");
		sqlStr.append(" and cursocoordenador.tipoCoordenadorCurso = 'GERAL' ");
		sqlStr.append(" and cursocoordenador.codigo = ("); 
			sqlStr.append("	select cc.codigo from cursocoordenador as cc"); 
			sqlStr.append("	where  cc.curso = matricula.curso and cc.unidadeensino = matricula.unidadeensino"); 
			sqlStr.append("	and cc.tipoCoordenadorCurso = 'GERAL'" ); 
			sqlStr.append("	and ((cc.turma is not null and cc.turma = matriculaperiodo.turma)");
			sqlStr.append("	or (cc.turma is null and  cc.curso = matricula.curso)"); 
			sqlStr.append(	")" ); 
			sqlStr.append("	order by case when cc.turma is not null then 0 else 1 end limit 1" ); 
		sqlStr.append(	")");
		sqlStr.append(" left join usuario on requerimento.responsavel = usuario.codigo ");
		sqlStr.append(" left join funcionario as funcionariocoordenador on funcionariocoordenador.codigo = cursocoordenador.funcionario ");
		sqlStr.append(" left join pessoa as coordenador on funcionariocoordenador.pessoa = coordenador.codigo ");		
		sqlStr.append(" left join requerimentoHistorico on requerimentoHistorico.requerimento = requerimento.codigo ");
		
		sqlStr.append(" and RequerimentoHistorico.codigo = (select reqhistorico.codigo from RequerimentoHistorico reqhistorico  ");
		sqlStr.append("  where reqhistorico.requerimento = requerimento.codigo ");
		sqlStr.append("  order by reqhistorico.dataentradadepartamento desc limit 1 ) ");
		
		sqlStr.append(" left join pessoa as responsavelTramite on  requerimentoHistorico.responsavelrequerimentodepartamento = responsavelTramite.codigo ");
		sqlStr.append(" left join departamento as departamentoResponsavelTramite on requerimentoHistorico.departamento = departamentoResponsavelTramite.codigo ");
		
		
		if(filtrarPeriodoPor.equals("dtAbertura")) {
			sqlStr.append("where CAST(requerimento.data AS DATE) between '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		}else if(filtrarPeriodoPor.equals("dtConclusao")) {
			sqlStr.append("where CAST(requerimento.dataFinalizacao AS DATE) between '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		}else if(filtrarPeriodoPor.equals("dtAtendimento")) {
			sqlStr.append("where exists (select codigo from requerimentoHistorico where requerimentoHistorico.requerimento = requerimento.codigo and CAST(requerimentoHistorico.dataEntradaDepartamento AS DATE)  between '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("')");
		}else if(filtrarPeriodoPor.equals("dtPrevisaoConclusao")) {
			sqlStr.append("where CAST(requerimento.dataPrevistaFinalizacao AS DATE) between '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		}
		
		sqlStr.append(" and tr.tipo = 'TC' ");
		
		montarSqlSituacao(finalizadoDeferido, finalizadoIndeferido, emExecucao, pendente, prontoRetirada, atrasado, sqlStr);
		montarSqlSituacaoFinanceira(aguardandoPagamento, aguardandoAutorizacaoPagamento, isento, pago, canceladoFinanceiro,  solicitacaoIsencao,  solicitacaoIsencaoDeferido,  solicitacaoIsencaoIndeferido, sqlStr);

		sqlStr.append(adicionarFiltroCurso(cursoVOs));
		
		if (Uteis.isAtributoPreenchido(situacaoRequerimentoDepartamento)) {
			sqlStr.append("and requerimento.situacaoRequerimentoDepartamento = ").append(situacaoRequerimentoDepartamento).append(" ");
		}
		if (Uteis.isAtributoPreenchido(codTipoReq)) {
			sqlStr.append("and requerimento.tipoRequerimento = ").append(codTipoReq).append(" ");
		}
		if (Uteis.isAtributoPreenchido(requerente.getCodigo())) {
			sqlStr.append("and pessoa.codigo = ").append(requerente.getCodigo());
		}
		if (!unidadeEnsinoVOs.isEmpty()) {
			sqlStr.append(" and requerimento.unidadeensino in (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (x > 0) {
						sqlStr.append(", ");
					}
					sqlStr.append(unidadeEnsinoVO.getCodigo());
					x++;
				}

			}
			sqlStr.append(" ) ");
		}
		
		if (Uteis.isAtributoPreenchido(matricula)) {
			sqlStr.append("and matricula.matricula = '").append(matricula).append("' ");
		}
		
		if (funcionario != 0) {
			sqlStr.append("and requerimento.funcionario = ").append(funcionario).append(" ");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append("and curso.codigo = ").append(curso.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append("and turma.codigo = ").append(turma.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(turmaReposicao)) {
			sqlStr.append("and requerimento.turmareposicao = ").append(turmaReposicao.getCodigo());
		}
		if (departamento != 0) {
			sqlStr.append("and requerimento.departamentoresponsavel = ").append(departamento).append(" ");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {
			sqlStr.append("and requerimento.disciplina = ").append(disciplina).append(" ");
		}
		if(Uteis.isAtributoPreenchido(coordenador.getCodigo())) {
			sqlStr.append("and coordenador.codigo = ").append(coordenador.getCodigo());
		}
		sqlStr.append("order by requerimento.data, pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado;
	}

	public SqlRowSet executarConsultaParametrizada(List<UnidadeEnsinoVO> unidadeEnsinoVOs, Boolean finalizadoDeferido, Boolean finalizadoIndeferido, Boolean emExecucao, Boolean pendente, Boolean aguardandoPagamento, Boolean aguardandoAutorizacaoPagamento, Boolean isento, Boolean pago, Boolean canceladoFinanceiro, Boolean solicitacaoIsencao, Boolean solicitacaoIsencaoDeferido, Boolean solicitacaoIsencaoIndeferido, Boolean prontoRetirada, Boolean atrasado, RequerimentoRelVO requerimentoRelVO, Date dataInicio, Date dataFim, Integer funcionario, Integer departamento, Integer disciplina, String layout, CursoVO curso, TurmaVO turma, List<CursoVO> cursoVOs, PessoaVO requerente, Integer situacaoRequerimentoDepartamento, PessoaVO coordenador, String filtrarPeriodoPor, TurmaVO turmaReposicao) throws Exception {
		StringBuilder sqlStr = new StringBuilder("select distinct requerimento.situacao as \"requerimentoSituacao\", ");
		sqlStr.append("funcionario.codigo as funcionarioCodigo, pessoaFunc.nome as funcionarioNome, tr.codigo as \"trCodigo\", tr.nome as \"trNome\", ");
		if (!layout.equals("SI2")) {
			sqlStr.append("requerimento.codigo as \"requerimentoCodigo\", pessoa.codigo as \"pessoaCodigo\", ");
			sqlStr.append("pessoa.nome as \"pessoaNome\", requerimento.data as \"requerimentoData\", requerimento.valor as \"requerimentoValor\", ");
			sqlStr.append("unidadeEnsino.codigo as \"unidadeEnsinoCodigo\", unidadeEnsino.nome as \"unidadeEnsinoNome\", turma.identificadorTurma  as identificadorTurma, ");
			sqlStr.append("disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome, situacaorequerimentodepartamento.situacao as situacaoRequerimentoDepartamento, requerimento.dataUltimaAlteracao as dataUltimaAlteracaoRequerimento, ");
		}		
		if (layout.equals("SI2")) {
			sqlStr.append("count(*) as qtde ");
		
		} else {
			sqlStr.append("0 as qtde ");
		}
		if(layout.equals("AN3")) {
			sqlStr.append(", coordenador.nome as coordenador ");
		}
		if(layout.equals("AN5")) {
			sqlStr.append(", requerimentoHistorico.dataEntradaDepartamento as dataEntradaDepartamento, responsavelTramite.nome as responsavelTramite, departamentoResponsavelTramite.nome as departamentoResponsavelTramite ");
		}
		if(layout.equals("AN6")) {
			sqlStr.append(", requerimento.dataFinalizacao as \"requerimentoDataFinalizacao\" ");
		}
		sqlStr.append("from requerimento ");
		sqlStr.append("inner join tipoRequerimento tr on tr.codigo = requerimento.tipoRequerimento ");
		sqlStr.append("inner join pessoa on pessoa.codigo = requerimento.pessoa ");
		sqlStr.append(" left join unidadeEnsino on unidadeEnsino.codigo = requerimento.unidadeEnsino ");
		sqlStr.append(" left join matricula on matricula.matricula = requerimento.matricula ");
		sqlStr.append(" left join matriculaperiodo on matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append(" and matriculaperiodo.codigo = (select codigo from matriculaperiodo mp where  matricula.matricula = mp.matricula and mp.data<= requerimento.data order by mp.codigo desc limit 1) ");
		sqlStr.append(" left join turma on turma.codigo = matriculaperiodo.turma ");
		sqlStr.append(" left join disciplina on disciplina.codigo = requerimento.disciplina ");
		sqlStr.append(" left join funcionario on funcionario.codigo = requerimento.funcionario ");
		sqlStr.append(" left join pessoa as pessoaFunc on pessoaFunc.codigo = funcionario.pessoa ");
		sqlStr.append(" left join curso on matricula.curso = curso.codigo ");
		sqlStr.append(" left join situacaorequerimentodepartamento on requerimento.situacaorequerimentodepartamento = situacaorequerimentodepartamento.codigo ");		
		sqlStr.append(" left join cursocoordenador on cursocoordenador.curso = matricula.curso ");
		sqlStr.append(" and cursocoordenador.unidadeensino = matricula.unidadeensino ");
		sqlStr.append(" and cursocoordenador.tipoCoordenadorCurso = 'GERAL' ");
		sqlStr.append(" and cursocoordenador.codigo = ("); 
			sqlStr.append("	select cc.codigo from cursocoordenador as cc"); 
			sqlStr.append("	where  cc.curso = matricula.curso and cc.unidadeensino = matricula.unidadeensino"); 
			sqlStr.append("	and cc.tipoCoordenadorCurso = 'GERAL'" ); 
			sqlStr.append("	and ((cc.turma is not null and cc.turma = matriculaperiodo.turma)");
			sqlStr.append("	or (cc.turma is null and  cc.curso = matricula.curso)"); 
			sqlStr.append(	")" ); 
			sqlStr.append("	order by case when cc.turma is not null then 0 else 1 end limit 1" ); 
		sqlStr.append(	")");
		sqlStr.append(" left join usuario on requerimento.responsavel = usuario.codigo ");
		sqlStr.append(" left join funcionario as funcionariocoordenador on funcionariocoordenador.codigo = cursocoordenador.funcionario ");
		sqlStr.append(" left join pessoa as coordenador on funcionariocoordenador.pessoa = coordenador.codigo ");		
		if(layout.equals("AN5")) {
			sqlStr.append(" left join requerimento"
					+ "Historico on requerimentoHistorico.requerimento = requerimento.codigo ");
			sqlStr.append(" left join pessoa as responsavelTramite on  requerimentoHistorico.responsavelrequerimentodepartamento = responsavelTramite.codigo ");
			sqlStr.append(" inner join departamento as departamentoResponsavelTramite on requerimentoHistorico.departamento = departamentoResponsavelTramite.codigo ");
		}
		
		if(filtrarPeriodoPor.equals("dtAbertura")) {
			sqlStr.append("where CAST(requerimento.data AS DATE) between '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		}else if(filtrarPeriodoPor.equals("dtConclusao")) {
			sqlStr.append("where CAST(requerimento.dataFinalizacao AS DATE) between '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		}else if(filtrarPeriodoPor.equals("dtAtendimento")) {
			if(layout.equals("AN5")) {
				sqlStr.append("where CAST(requerimentoHistorico.dataEntradaDepartamento AS DATE) between '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
			}else {
				sqlStr.append("where exists (select codigo from requerimentoHistorico where requerimentoHistorico.requerimento = requerimento.codigo and CAST(requerimentoHistorico.dataEntradaDepartamento AS DATE)  between '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("')");
			}
		}else if(filtrarPeriodoPor.equals("dtPrevisaoConclusao")) {
			sqlStr.append("where CAST(requerimento.dataPrevistaFinalizacao AS DATE) between '").append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
		}
		
		
		montarSqlSituacao(finalizadoDeferido, finalizadoIndeferido, emExecucao, pendente, prontoRetirada, atrasado, sqlStr);
		montarSqlSituacaoFinanceira(aguardandoPagamento, aguardandoAutorizacaoPagamento, isento, pago, canceladoFinanceiro,  solicitacaoIsencao,  solicitacaoIsencaoDeferido,  solicitacaoIsencaoIndeferido, sqlStr);

		sqlStr.append(adicionarFiltroCurso(cursoVOs));

		if (!requerimentoRelVO.getTipoRequerimentoVO().getCodigo().equals(0)) {
			sqlStr.append("and requerimento.tipoRequerimento = ").append(requerimentoRelVO.getTipoRequerimentoVO().getCodigo()).append(" ");
		}
		if (Uteis.isAtributoPreenchido(situacaoRequerimentoDepartamento)) {
			sqlStr.append("and requerimento.situacaoRequerimentoDepartamento = ").append(situacaoRequerimentoDepartamento).append(" ");
		}
		if (Uteis.isAtributoPreenchido(requerente.getCodigo())) {
			sqlStr.append("and pessoa.codigo = ").append(requerente.getCodigo());
		}
		if (!unidadeEnsinoVOs.isEmpty()) {
			sqlStr.append(" and requerimento.unidadeensino in (");
			int x = 0;
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (x > 0) {
						sqlStr.append(", ");
					}
					sqlStr.append(unidadeEnsinoVO.getCodigo());
					x++;
				}

			}
			sqlStr.append(" ) ");
		}
		if (funcionario != 0) {
			sqlStr.append("and requerimento.funcionario = ").append(funcionario).append(" ");
		}
		if (Uteis.isAtributoPreenchido(curso)) {
			sqlStr.append("and curso.codigo = ").append(curso.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(turma)) {
			sqlStr.append("and turma.codigo = ").append(turma.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(turmaReposicao)) {
			sqlStr.append("and requerimento.turmareposicao = ").append(turmaReposicao.getCodigo());
		}
		if (departamento != 0) {
			sqlStr.append("and requerimento.departamentoresponsavel = ").append(departamento).append(" ");
		}
		if (Uteis.isAtributoPreenchido(disciplina)) {			
			sqlStr.append(" and ((tiporequerimento.tipo not in ('SEGUNDA_CHAMADA', 'AD') and requerimento.disciplina = ").append(disciplina).append(") ");
			sqlStr.append(" or (tiporequerimento.tipo = 'SEGUNDA_CHAMADA' and exists (select requerimentodisciplina.codigo from requerimentodisciplina where requerimento.codigo = requerimentodisciplina.requerimento and requerimentodisciplina.disciplina = ").append(disciplina).append(" limit 1)) ");
			sqlStr.append(" or (tiporequerimento.tipo = 'AD' and exists (select requerimentoDisciplinasAproveitadas.codigo from requerimentoDisciplinasAproveitadas where requerimento.codigo = requerimentoDisciplinasAproveitadas.requerimento and requerimentoDisciplinasAproveitadas.disciplina = ").append(disciplina).append(" limit 1))) ");
		}
		if(Uteis.isAtributoPreenchido(coordenador.getCodigo())) {
			sqlStr.append("and coordenador.codigo = ").append(coordenador.getCodigo());
		}
		if (layout.equals("SI2")) {
			sqlStr.append("group by funcionario.codigo, pessoaFunc.nome, tr.codigo, tr.nome, requerimento.situacao ");
		}
		if (layout.equals("AN2") ) {
			sqlStr.append("order by pessoaFunc.nome, requerimento.data, pessoa.nome");
		} else if (layout.equals("SI2")) {
			sqlStr.append("order by pessoaFunc.nome, tr.nome, requerimento.situacao");
		} else {
			sqlStr.append("order by requerimento.data, pessoa.nome");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado;
	}

	private void montarSqlSituacao(Boolean finalizadoDeferido, Boolean finalizadoIndeferido, Boolean emExecucao, Boolean pendente, Boolean prontoRetirada, Boolean atrasado, StringBuilder sqlStr) {
		if (finalizadoDeferido || finalizadoIndeferido || emExecucao || pendente || prontoRetirada || atrasado) {
			sqlStr.append("and (");
		}
		boolean possuiFiltroPorSituacao = false;
		if (finalizadoDeferido || finalizadoIndeferido || emExecucao || pendente || prontoRetirada) {
			possuiFiltroPorSituacao = true;
			sqlStr.append("requerimento.situacao in (");
			int filtros = 0;
			if (finalizadoDeferido) {
				sqlStr.append("'FD'");
				filtros++;
			}
			if (finalizadoIndeferido) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'FI'");
				filtros++;
			}
			if (emExecucao) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'EX'");
				filtros++;
			}
			if (pendente) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'PE'");
				filtros++;
			}
			if (prontoRetirada) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'PR'");
				filtros++;
			}
			sqlStr.append(")");
		}
		if (atrasado) {
			if (possuiFiltroPorSituacao) {
				sqlStr.append(" or ");
			}
			sqlStr.append(" ((requerimento.dataprevistafinalizacao < CURRENT_TIMESTAMP ");
			sqlStr.append(" and (requerimento.situacao = 'PE' or requerimento.situacao = 'EX' or requerimento.situacao = 'AP' or requerimento.situacao = 'PR')))");
		}
		if (finalizadoDeferido || finalizadoIndeferido || emExecucao || pendente || prontoRetirada || atrasado) {
			sqlStr.append(")");
		}
	}
	
	private void montarSqlSituacaoFinanceira(Boolean aguardandoPagamento, Boolean aguardandoAutorizacaoPagamento, Boolean isento, Boolean pago, Boolean canceladoFinanceiro, Boolean solicitacaoIsencao, Boolean solicitacaoIsencaoDeferido, Boolean solicitacaoIsencaoIndeferido, StringBuilder sqlStr) {
		String andOr = " AND ( ";
		if (aguardandoPagamento || isento || pago || aguardandoAutorizacaoPagamento || canceladoFinanceiro) {
			if((solicitacaoIsencao || solicitacaoIsencaoDeferido || solicitacaoIsencaoIndeferido)) {				
				sqlStr.append("and ( requerimento.situacaofinanceira in (");
			}else {
				sqlStr.append("and requerimento.situacaofinanceira in (");
			}
			int filtros = 0;
			if (aguardandoPagamento) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'PE'");
				filtros++;
			}
			if (aguardandoAutorizacaoPagamento) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'AP'");
				filtros++;
			}
			if (isento) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'IS'");
				filtros++;
			}
			if (pago) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'PG'");
				filtros++;
			}
			if (canceladoFinanceiro) {
				if (filtros > 0) {
					sqlStr.append(",");
				}
				sqlStr.append("'CA'");
				filtros++;
			}
			sqlStr.append(")");
			andOr = " or ";
		}
		
		if(solicitacaoIsencao || solicitacaoIsencaoDeferido || solicitacaoIsencaoIndeferido) {				
			if(solicitacaoIsencao) {
				sqlStr.append(andOr).append(" (requerimento.situacaoFinanceira not in ('PG', 'IS') ");
				sqlStr.append(" AND requerimento.situacaoIsencaoTaxa = '").append(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.AGUARDANDO_RESPOSTA.name()).append("') ");
				andOr = " or ";
			}
			if(solicitacaoIsencaoDeferido) {				
				sqlStr.append(andOr).append(" (requerimento.situacaoIsencaoTaxa = '").append(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.DEFERIDO.name()).append("') ");
				andOr = " or ";
			}
			if(solicitacaoIsencaoIndeferido) {				
				sqlStr.append(andOr).append(" (requerimento.situacaoIsencaoTaxa = '").append(SituacaoSolicitacaoIsencaoTaxaRequerimentoEnum.INDEFERIDO.name()).append("') ");
				andOr = " or ";
			}
			sqlStr.append(" ) ");
		}
	}

	private RequerimentoVO montarDados(SqlRowSet dadosSQL, RequerimentoRelVO requerimentoRelVO, String layout) {
		RequerimentoVO requerimento = new RequerimentoVO();
		requerimento.setSituacao(dadosSQL.getString("requerimentoSituacao"));
		requerimento.getTipoRequerimento().setCodigo(dadosSQL.getInt("trCodigo"));
		requerimento.getTipoRequerimento().setNome(dadosSQL.getString("trNome"));
		requerimento.getFuncionarioVO().setCodigo(dadosSQL.getInt("funcionarioCodigo"));
		requerimento.getFuncionarioVO().getPessoa().setNome(dadosSQL.getString("funcionarioNome"));
		requerimento.setQtde(dadosSQL.getInt("qtde"));
		if (!layout.equals("SI2")) {
			requerimento.setCodigo(dadosSQL.getInt("requerimentoCodigo"));
			requerimento.getPessoa().setCodigo(dadosSQL.getInt("pessoaCodigo"));
			requerimento.getPessoa().setNome(dadosSQL.getString("pessoaNome"));
			requerimento.setData(dadosSQL.getDate("requerimentoData"));
			requerimento.setValor(dadosSQL.getDouble("requerimentoValor"));
			requerimento.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsinoCodigo"));
			requerimento.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsinoNome"));
			requerimento.getTurma().setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
			requerimento.getDisciplina().setCodigo(dadosSQL.getInt("disciplina_codigo"));
			requerimento.getDisciplina().setNome(dadosSQL.getString("disciplina_nome"));
			requerimento.getSituacaoRequerimentoDepartamentoVO().setSituacao(dadosSQL.getString("situacaoRequerimentoDepartamento"));
			requerimento.setDataUltimaAlteracao(dadosSQL.getDate("dataUltimaAlteracaoRequerimento"));
		}
		if(layout.equals("AN3")) {
			requerimento.setCoordenador(dadosSQL.getString("coordenador"));
		}
		if(layout.equals("AN5")) {			
			requerimento.setDataEntradaDepartamento(dadosSQL.getDate("dataEntradaDepartamento"));
			requerimento.setResponsavelTramite(dadosSQL.getString("responsavelTramite"));
			requerimento.setDepartamentoResponsavelTramite(dadosSQL.getString("departamentoResponsavelTramite"));
		}
		if(layout.equals("AN6")) {
			requerimento.setDataFinalizacao(dadosSQL.getDate("requerimentoDataFinalizacao"));
		}
		contarRequerimentosSituacao(requerimento, requerimentoRelVO, layout);
		return requerimento;
	}
	
	
	private RequerimentoVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		RequerimentoVO requerimento = new RequerimentoVO();
		requerimento.setCodigo(dadosSQL.getInt("requerimentoCodigo"));
		requerimento.setSituacao(dadosSQL.getString("requerimentoSituacao"));
		requerimento.getTipoRequerimento().setCodigo(dadosSQL.getInt("trCodigo"));
		requerimento.getTipoRequerimento().setNome(dadosSQL.getString("trNome"));
		requerimento.getTipoRequerimento().setTipoRequerimentoDepartamentoVOs(getFacadeFactory().getTipoRequerimentoDepartamentoFacade().consultarPorCodigoTipoRequerimento(requerimento.getTipoRequerimento().getCodigo(), false, null));
		requerimento.getFuncionarioVO().setCodigo(dadosSQL.getInt("funcionarioCodigo"));
		requerimento.getFuncionarioVO().getPessoa().setNome(dadosSQL.getString("funcionarioNome"));
			requerimento.getPessoa().setCodigo(dadosSQL.getInt("pessoaCodigo"));
			requerimento.getPessoa().setNome(dadosSQL.getString("pessoaNome"));
			requerimento.getPessoa().setEmail(dadosSQL.getString("pessoaEmail"));
			requerimento.setData(dadosSQL.getDate("requerimentoData"));
			requerimento.setValor(dadosSQL.getDouble("requerimentoValor"));
			requerimento.getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsinoCodigo"));
			requerimento.getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsinoNome"));
			requerimento.getTurma().setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
			requerimento.getDisciplina().setCodigo(dadosSQL.getInt("disciplina_codigo"));
			requerimento.getDisciplina().setNome(dadosSQL.getString("disciplina_nome"));
			
			requerimento.getCurso().setCodigo(dadosSQL.getInt("cursoCodigo"));
			requerimento.getCurso().setNome(dadosSQL.getString("cursoNome"));
			requerimento.getMatricula().setTituloMonografia(dadosSQL.getString("matricula.nomemonografia"));
			
			if(dadosSQL.getObject("matricula.notamonografia") == null){
				requerimento.getMatricula().setNotaMonografia((Double) dadosSQL.getObject("matricula.notamonografia"));
			}else {
				requerimento.getMatricula().setNotaMonografia(dadosSQL.getDouble("matricula.notamonografia"));;
			}
			
			requerimento.setData(dadosSQL.getDate("requerimentoData"));
			requerimento.setRequerimentoHistoricoVOs(getFacadeFactory().getRequerimentoHistoricoFacade().consultarPorCodigoRequerimentoFiltrarPorUltimoHistoricoDepartamento(requerimento.getCodigo(), false, usuario));
			q:
			for(TipoRequerimentoDepartamentoVO tipoRequerimentoDepartamentoVO: requerimento.getTipoRequerimento().getTipoRequerimentoDepartamentoVOs()) {
				for(RequerimentoHistoricoVO requerimentoHistoricoVO: requerimento.getRequerimentoHistoricoVOs()) {
					if(tipoRequerimentoDepartamentoVO.getOrdemExecucao().equals(requerimentoHistoricoVO.getOrdemExecucaoTramite())) {
						continue q;
					}
				}
				RequerimentoHistoricoVO requerimentoHistoricoVO =  new RequerimentoHistoricoVO();
				requerimentoHistoricoVO.setOrdemExecucaoTramite(tipoRequerimentoDepartamentoVO.getOrdemExecucao());
				requerimentoHistoricoVO.setDepartamento(tipoRequerimentoDepartamentoVO.getDepartamento());				
				requerimento.getRequerimentoHistoricoVOs().add(requerimentoHistoricoVO);
			}
			requerimento.getSituacaoRequerimentoDepartamentoVO().setSituacao(dadosSQL.getString("situacaoRequerimentoDepartamento"));
			requerimento.setDataUltimaAlteracao(dadosSQL.getDate("dataUltimaAlteracaoRequerimento"));
			requerimento.setNomeResponsavel(dadosSQL.getString("nomeResponsavel"));
			
			requerimento.setDataPrevistaFinalizacao(dadosSQL.getDate("dataprevistafinalizacao"));
			requerimento.setDataFinalizacao(dadosSQL.getDate("requerimentoDataFinalizacao"));
			requerimento.setCoordenador(dadosSQL.getString("coordenador"));
			requerimento.setDataEntradaDepartamento(dadosSQL.getDate("dataEntradaDepartamento"));
			requerimento.setResponsavelTramite(dadosSQL.getString("responsavelTramite"));
			requerimento.setDepartamentoResponsavelTramite(dadosSQL.getString("departamentoResponsavelTramite"));
			requerimento.setDataFinalizacao(dadosSQL.getDate("requerimentoDataFinalizacao"));
		return requerimento;
	}

	public void contarRequerimentosSituacao(RequerimentoVO requerimentoVO, RequerimentoRelVO requerimentoRelVO, String layout) {
		if (layout.equals("SI2")) {
			if (requerimentoVO.getSituacao().equals("FD")) {
				requerimentoRelVO.setQtdeFinalizadoDeferido(requerimentoRelVO.getQtdeFinalizadoDeferido() + requerimentoVO.getQtde());
			} else if (requerimentoVO.getSituacao().equals("FI")) {
				requerimentoRelVO.setQtdeFinalizadoIndeferido(requerimentoRelVO.getQtdeFinalizadoIndeferido() + requerimentoVO.getQtde());
			} else if (requerimentoVO.getSituacao().equals("EX")) {
				requerimentoRelVO.setQtdeEmExecucao(requerimentoRelVO.getQtdeEmExecucao() + requerimentoVO.getQtde());
			} else if (requerimentoVO.getSituacao().equals("PE")) {
				requerimentoRelVO.setQtdePendente(requerimentoRelVO.getQtdePendente() + requerimentoVO.getQtde());
			} else if (requerimentoVO.getSituacao().equals("AP")) {
				requerimentoRelVO.setQtdeAguardandoPagamento(requerimentoRelVO.getQtdeAguardandoPagamento() + requerimentoVO.getQtde());
			} else if (requerimentoVO.getSituacao().equals("PR")) {
				requerimentoRelVO.setQtdeProntoParaRetirada(requerimentoRelVO.getQtdeProntoParaRetirada() + requerimentoVO.getQtde());
			}
		} else {
			if (requerimentoVO.getSituacao().equals("FD")) {
				requerimentoRelVO.setQtdeFinalizadoDeferido(requerimentoRelVO.getQtdeFinalizadoDeferido() + 1);
			} else if (requerimentoVO.getSituacao().equals("FI")) {
				requerimentoRelVO.setQtdeFinalizadoIndeferido(requerimentoRelVO.getQtdeFinalizadoIndeferido() + 1);
			} else if (requerimentoVO.getSituacao().equals("EX")) {
				requerimentoRelVO.setQtdeEmExecucao(requerimentoRelVO.getQtdeEmExecucao() + 1);
			} else if (requerimentoVO.getSituacao().equals("PE")) {
				requerimentoRelVO.setQtdePendente(requerimentoRelVO.getQtdePendente() + 1);
			} else if (requerimentoVO.getSituacao().equals("AP")) {
				requerimentoRelVO.setQtdeAguardandoPagamento(requerimentoRelVO.getQtdeAguardandoPagamento() + 1);
			} else if (requerimentoVO.getSituacao().equals("PR")) {
				requerimentoRelVO.setQtdeProntoParaRetirada(requerimentoRelVO.getQtdeProntoParaRetirada() + 1);
			}
		}
	}

	public static String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getDesignIReportRelatorio(String layout) {
		return (caminhoBaseRelatorio() + getIdEntidade(layout) + ".jrxml");
	}

	public static String getDesignIReportRelatorioAnalitico(String tipoLayout) {
		return (caminhoBaseRelatorio() + tipoLayout + ".jrxml");
	}

	public static String getIdEntidade(String layout) {
		if (layout.equals("AN2")) {
			return ("RequerimentoPorResponsavelAnaliticoRel");
		} else if (layout.equals("SI2")) {
			return ("RequerimentoPorResponsavelSinteticoRel");
		}else if (layout.equals("AN4")) {
			return ("RequerimentoAbertosAnaliticoRel");
		}else if (layout.equals("AN5")) {
			return ("RequerimentoAtendimentosAnaliticoRel");
		}else if (layout.equals("AN6")) {
			return ("RequerimentoConcluidosAnaliticoRel");
		}else if (layout.equals("AN7")) {
			return ("RequerimentoDataInteracoesAnaliticoRel");
		} else {
			return ("RequerimentoRel");
		}
		
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

	public static String getIdEntidadeAnalitico() {
		return ("RequerimentoAnaliticoRel");
	}
	
	public static String designIReportRelatorioTCCExcel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "RequerimentoTCCExcel" + ".jrxml");
	}
	
	public static String designIReportRelatorioExcel(String layout) {
		
		if (layout.equals("AN2")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "RequerimentoPorResponsavelAnaliticoRelExcel" + ".jrxml");
		} else if (layout.equals("SI2")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "RequerimentoPorResponsavelSinteticoRelExcel" + ".jrxml");
		} else if(layout.equals("AN3")){
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "RequerimentoAnaliticoComCoordenadorRelExcel" + ".jrxml");
		}
		else if (layout.equals("AN4")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "RequerimentoAbertosAnaliticoRelExcel" + ".jrxml");
		}else if (layout.equals("AN5")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "RequerimentoAtendimentosAnaliticoRelExcel" + ".jrxml");
		}else if (layout.equals("AN6")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "RequerimentoConcluidosAnaliticoRelExcel" + ".jrxml");
		}else if (layout.equals("AN7")) {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "RequerimentoDataInteracoesAnaliticoRelExcel" + ".jrxml");
		}
		else {		
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "RequerimentoRelExcel" + ".jrxml");
		}
	}
}
