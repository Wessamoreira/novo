package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.financeiro.PrevisaoFaturamentoRelVO;
import relatorio.negocio.interfaces.financeiro.PrevisaoFaturamentoRelInterfaceFacade;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioContaReceberVO;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class PrevisaoFaturamentoRel extends SuperRelatorio implements PrevisaoFaturamentoRelInterfaceFacade {

	public void validarDados(List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicio, Date dataFim) throws Exception {
		if (dataInicio == null || dataFim == null) {
			throw new Exception("Os campos data início e data fim devem ser informados.");
		}
		if (listaUnidadeEnsino.isEmpty()) {
			throw new Exception("O campo Unidade de Ensino deve ser informado.");
		}

	}

	public List<PrevisaoFaturamentoRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, Boolean dataCompetencia, Integer curso, Integer turno, Integer turma, Date dataInicio, Date dataFim, String ordenador, FiltroRelatorioAcademicoVO filtroAcademicoVO, FiltroRelatorioContaReceberVO filtroRelatorioContaReceberVO, boolean considerarUnidadeEnsinoFinanceira, UsuarioVO usuarioLogado) throws Exception {
		List<PrevisaoFaturamentoRelVO> previsaoFaturamentoRelVOs = new ArrayList<PrevisaoFaturamentoRelVO>();
		try {
			SqlRowSet tabelaResultado = executarConsultaParametrizada(listaUnidadeEnsino, dataCompetencia, curso, turno, turma, dataInicio, dataFim, ordenador, filtroAcademicoVO, filtroRelatorioContaReceberVO, considerarUnidadeEnsinoFinanceira, usuarioLogado);
			while (tabelaResultado.next()) {
				previsaoFaturamentoRelVOs.add(montarDados(tabelaResultado, usuarioLogado));
			}
			return previsaoFaturamentoRelVOs;
		} finally {
		}
	}

	public SqlRowSet executarConsultaParametrizada(List<UnidadeEnsinoVO> listaUnidadeEnsino, Boolean dataCompetencia, Integer curso, Integer turno, Integer turma, Date dataInicio, Date dataFim, String ordenador, FiltroRelatorioAcademicoVO filtroAcademicoVO, FiltroRelatorioContaReceberVO filtroRelatorioContaReceberVO, boolean considerarUnidadeEnsinoFinanceira, UsuarioVO usuarioLogado) throws Exception {
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append(" SELECT DISTINCT contareceber.tipopessoa AS tipopessoa, pessoa.nome AS pessoa_nome, responsavelfinanceiro.nome AS responsavelfinanceiro_nome, parceiro.nome AS parceiro_nome, ");
		sqlStr.append(" fornecedor.nome AS fornecedor_nome, unidadeensino.nome AS unidadeensino_nome, turma.identificadorturma, matricula.matricula, curso.nome AS curso_nome, turno.nome AS turno_nome, contareceber.parcela, contareceber.tipoOrigem, ");
		sqlStr.append(" contareceber.dataVencimento, trunc(contareceber.valordescontocalculadoprimeirafaixadescontos::NUMERIC, 2) AS valorprimeirafaixadescontos, ");
		sqlStr.append(" case when contareceber.situacao = 'AR' then trunc(contareceber.valorrecebercalculado::NUMERIC, 2) ");
		sqlStr.append(" when contareceber.situacao = 'RE' then trunc(contareceber.valorrecebido::NUMERIC, 2) end AS valor ");
		sqlStr.append(" FROM contareceber ");
		sqlStr.append(" LEFT JOIN matricula ON contareceber.matriculaaluno = matricula.matricula ");
		sqlStr.append(" LEFT JOIN matriculaperiodo ON matricula.matricula = matriculaperiodo.matricula and case when contareceber.matriculaperiodo is null then matriculaperiodo.codigo = ");
		sqlStr.append(" (select codigo from matriculaperiodo mp where mp.matricula = matricula.matricula order by (mp.ano||'/'||mp.semestre )desc limit 1 ) else matriculaperiodo.codigo = contareceber.matriculaperiodo end ");
		sqlStr.append(" LEFT JOIN pessoa ON pessoa.codigo = matricula.aluno ");
		sqlStr.append(" LEFT JOIN pessoa responsavelfinanceiro ON contareceber.responsavelfinanceiro = responsavelfinanceiro.codigo ");
		sqlStr.append(" LEFT JOIN fornecedor ON contareceber.fornecedor = fornecedor.codigo ");
		sqlStr.append(" LEFT JOIN parceiro ON contareceber.parceiro = parceiro.codigo ");
		sqlStr.append(" LEFT JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" LEFT JOIN turno ON turno.codigo = matricula.turno ");
		sqlStr.append(" LEFT JOIN turma ON turma.codigo = matriculaperiodo.turma ");
		if(considerarUnidadeEnsinoFinanceira){
			sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensino.codigo = contareceber.unidadeensinofinanceira ");	
		}else{
			sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino ");
		}
		
		sqlStr.append(" WHERE 1=1 ");
		montarFiltrosRelatorio(sqlStr, "contareceber", listaUnidadeEnsino, dataCompetencia, curso, turno, turma, dataInicio, dataFim);
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));		
		sqlStr.append(" AND ").append(adicionarFiltroRelatorioContaReceberVO(filtroRelatorioContaReceberVO, "contareceber.tipoorigem", true));
		sqlStr.append(" AND contareceber.situacao IN ('AR', 'RE') ");
		sqlStr.append(" UNION ALL ");
		sqlStr.append(" SELECT DISTINCT 'AL' AS tipopessoa, pessoa.nome AS pessoa_nome, '' AS responsavelfinanceiro_nome, '' AS parceiro_nome, ");
		sqlStr.append(" '' AS fornecedor_nome, unidadeensino.nome AS unidadeensino_nome, turma.identificadorturma, matricula.matricula, curso.nome AS curso_nome, turno.nome AS turno_nome, matriculaperiodovencimento.parcela, ");
		sqlStr.append(" (case when tipoOrigemMatriculaPeriodoVencimento  = 'MATRICULA' then 'MAT' when tipoOrigemMatriculaPeriodoVencimento = 'MENSALIDADE' then 'MEN' else 'MDI' end)  AS tipoOrigem, ");
		sqlStr.append(" matriculaperiodovencimento.dataVencimento, trunc(matriculaperiodovencimento.valor::NUMERIC, 2) AS valor, trunc(matriculaperiodovencimento.valordescontocalculadoprimeirafaixadescontos::NUMERIC, 2) AS valorprimeirafaixadescontos ");
		sqlStr.append(" FROM matriculaperiodovencimento ");
		sqlStr.append(" LEFT JOIN matriculaperiodo ON matriculaperiodo.codigo = matriculaperiodovencimento.matriculaperiodo ");
		sqlStr.append(" LEFT JOIN matricula ON matricula.matricula = matriculaperiodo.matricula ");
		sqlStr.append(" LEFT JOIN unidadeensino ON unidadeensino.codigo = matricula.unidadeensino ");
		sqlStr.append(" LEFT JOIN pessoa ON pessoa.codigo = matricula.aluno ");
		sqlStr.append(" LEFT JOIN curso ON curso.codigo = matricula.curso ");
		sqlStr.append(" LEFT JOIN turno ON turno.codigo = matricula.turno ");
		sqlStr.append(" LEFT JOIN turma ON turma.codigo = matriculaperiodo.turma ");
		sqlStr.append(" WHERE 1=1 ");
		montarFiltrosRelatorio(sqlStr, "matriculaperiodovencimento", listaUnidadeEnsino, dataCompetencia, curso, turno, turma, dataInicio, dataFim);
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoAcademicaMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoFinanceiraMatriculaPeriodo(filtroAcademicoVO, "matriculaperiodo"));
		sqlStr.append(" AND matriculaperiodovencimento.situacao = 'NG' ");
		sqlStr.append(" AND ").append(adicionarFiltroRelatorioContaReceberVO(filtroRelatorioContaReceberVO, "matriculaperiodovencimento.tipoOrigemMatriculaPeriodoVencimento", false));
		sqlStr.append(realizarOrdenacaoConsulta(ordenador));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado;
	}
	
	public void montarFiltrosRelatorio(StringBuilder sqlStr, String tabela, List<UnidadeEnsinoVO> listaUnidadeEnsino, Boolean dataCompetencia, Integer curso, Integer turno, Integer turma, Date dataInicio, Date dataFim) {
		if (tabela.equals("contareceber")) {
			if (!dataCompetencia) {
				sqlStr.append(" AND contareceber.datavencimento BETWEEN '").append(Uteis.getDataJDBC(dataInicio)).append("' AND '").append(Uteis.getDataJDBC(dataFim)).append("' ");
			} else {
				sqlStr.append(" AND contareceber.datacompetencia BETWEEN '").append(Uteis.getDataPrimeiroDiaMes(dataInicio)).append("' AND '").append(Uteis.getDataUltimoDiaMes(dataFim)).append("' ");
			}
		} else {
			if (!dataCompetencia) {
				sqlStr.append(" AND matriculaperiodovencimento.datavencimento BETWEEN '").append(Uteis.getDataJDBC(dataInicio)).append("' AND '").append(Uteis.getDataJDBC(dataFim)).append("' ");
			} else {
				sqlStr.append(" AND matriculaperiodovencimento.datacompetencia BETWEEN '").append(Uteis.getDataPrimeiroDiaMes(dataInicio)).append("' AND '").append(Uteis.getDataUltimoDiaMes(dataFim)).append("' ");
			}			
		}
		if (!listaUnidadeEnsino.isEmpty()) {
			sqlStr.append(" AND unidadeensino.codigo in (");
			for (UnidadeEnsinoVO ue : listaUnidadeEnsino) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}
		if (curso != null && curso != 0) {
			sqlStr.append(" AND curso.codigo = ").append(curso);
		}
		if (turma != null && turma != 0) {
			sqlStr.append(" AND turma.codigo = ").append(turma);
		}
		if (turno != null && turno != 0) {
			sqlStr.append(" AND turno.codigo = ").append(turno);
		}
	}	


	public String realizarOrdenacaoConsulta(String ordenador) {
		if (ordenador.equals("nome")) {
			return " ORDER BY pessoa_nome";
		} else if (ordenador.equals("dataVencimento")) {
			return " ORDER BY dataVencimento";
		} else if (ordenador.equals("tipoOrigem")) {
			return " ORDER BY tipoOrigem";
		} else if (ordenador.equals("parcela")) {
			return " ORDER BY parcela";
		} else {
			return " ORDER BY pessoa_nome";
		}
	}

	private PrevisaoFaturamentoRelVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuarioLogado) throws Exception {
		PrevisaoFaturamentoRelVO previsaoFaturamentoRelVO = new PrevisaoFaturamentoRelVO();
		previsaoFaturamentoRelVO.setTipoPessoa(dadosSQL.getString("tipopessoa"));
		if (previsaoFaturamentoRelVO.getTipoPessoa().equals("AL")) {
			previsaoFaturamentoRelVO.setNomePessoa(dadosSQL.getString("pessoa_nome"));
		} else if (previsaoFaturamentoRelVO.getTipoPessoa().equals("RF")) {
			previsaoFaturamentoRelVO.setNomePessoa(dadosSQL.getString("responsavelfinanceiro_nome"));
		} else if (previsaoFaturamentoRelVO.getTipoPessoa().equals("FO")) {
			previsaoFaturamentoRelVO.setNomePessoa(dadosSQL.getString("fornecedor_nome"));
		} else if (previsaoFaturamentoRelVO.getTipoPessoa().equals("PA")) {
			previsaoFaturamentoRelVO.setNomePessoa(dadosSQL.getString("parceiro_nome"));
		}
		previsaoFaturamentoRelVO.setUnidadeEnsino(dadosSQL.getString("unidadeensino_nome"));
		previsaoFaturamentoRelVO.setMatricula(dadosSQL.getString("matricula"));
		previsaoFaturamentoRelVO.setCurso(dadosSQL.getString("curso_nome"));
		previsaoFaturamentoRelVO.setTurno(dadosSQL.getString("turno_nome"));
		previsaoFaturamentoRelVO.setTurma(dadosSQL.getString("identificadorturma"));
		previsaoFaturamentoRelVO.setParcela(dadosSQL.getString("parcela"));
		previsaoFaturamentoRelVO.setTipoOrigem(dadosSQL.getString("tipoorigem"));
		previsaoFaturamentoRelVO.setData(dadosSQL.getDate("datavencimento"));
		previsaoFaturamentoRelVO.setValorPrimeiraFaixa(dadosSQL.getDouble("valorprimeirafaixadescontos"));
		previsaoFaturamentoRelVO.setValor(dadosSQL.getDouble("valor"));
		return previsaoFaturamentoRelVO;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getDesignIReportRelatorioExcel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeExcel() + ".jrxml");
	}

	public static String getCaminhoBaseDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return ("PrevisaoFaturamentoRel");
	}

	public static String getIdEntidadeExcel() {
		return ("PrevisaoFaturamentoExcelRel");
	}
}
