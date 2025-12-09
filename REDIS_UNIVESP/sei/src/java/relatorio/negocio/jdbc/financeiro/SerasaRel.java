package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.SerasaRelVO;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class SerasaRel extends SuperRelatorio implements SerasaRelInterfaceFacade {

	public SerasaRel() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.financeiro.SerasaRelInterfaceFacade#consultarAlunosSerasa(java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public List<SerasaRelVO> consultarAlunosSerasa(String filtro, Integer unidadeEnsino, Integer curso, Integer turma, String matricula, UsuarioVO usuario, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
//		ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, null);
		StringBuilder selectStr = new StringBuilder("");
		selectStr.append("SELECT DISTINCT(contareceber.matriculaaluno), p.nome, contador.qtde, valortotal FROM contareceber INNER JOIN ( ");
		selectStr.append("SELECT count(codigo) AS qtde, matriculaaluno, SUM(cr.valor) as valortotal  FROM contareceber AS cr ");
		selectStr.append("WHERE cr.datavencimento < current_date - INTERVAL '").append(configuracaoFinanceiroVO.getQtdeMinimaDiasAntesNegativacaoSerasa()).append(" day' AND cr.datavencimento > current_date - INTERVAL '1794 day' AND cr.situacao = 'AR' GROUP BY matriculaaluno ");
		selectStr.append(") AS contador ON contareceber.matriculaaluno = contador.matriculaaluno ");
		selectStr.append("INNER JOIN matricula m ON contador.matriculaaluno = m.matricula ");
		selectStr.append("INNER JOIN pessoa p ON m.aluno = p.codigo ");
		selectStr.append("INNER JOIN matriculaperiodo mp ON m.matricula = mp.matricula ");
		selectStr.append("WHERE contareceber.situacao = 'AR' ");
		selectStr.append("AND contador.qtde > ").append(configuracaoFinanceiroVO.getQtdeParcelasNegativacaoSerasa()).append(" ");
		selectStr.append("AND contareceber.datavencimento < current_date - INTERVAL '").append(configuracaoFinanceiroVO.getQtdeMinimaDiasAntesNegativacaoSerasa()).append(" day' ");
		selectStr.append("AND contareceber.datavencimento > current_date - INTERVAL '1794 day' ");
		if (filtro.equals("unidadeEnsino")) {
			selectStr.append("AND m.unidadeEnsino = ").append(unidadeEnsino).append(" ");
		} else if (filtro.equals("curso")) {
			selectStr.append("AND m.curso = ").append(curso).append(" ");
		} else if (filtro.equals("turma")) {
			selectStr.append("AND mp.turma = ").append(turma).append(" ");
		} else if (filtro.equals("aluno")) {
			selectStr.append("AND m.matricula = '").append(matricula).append("' ");
		}
		selectStr.append("GROUP BY contareceber.matriculaaluno, contador.qtde, valortotal, p.nome ");
		selectStr.append("ORDER BY contareceber.matriculaaluno ");
		return montarDados(getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString()));
	}

	private List<SerasaRelVO> montarDados(SqlRowSet tabelaResultado) {
		List<SerasaRelVO> serasaRelVOs = new ArrayList<SerasaRelVO>(0);
		while (tabelaResultado.next()) {
			SerasaRelVO serasaRelVO = new SerasaRelVO();
			serasaRelVO.setMatriculaAluno(tabelaResultado.getString("matriculaAluno"));
			serasaRelVO.setNomeAluno(tabelaResultado.getString("nome"));
			serasaRelVO.setNumContas(tabelaResultado.getInt("qtde"));
			serasaRelVO.setValorTotal(Uteis.arrendondarForcando2CadasDecimais(tabelaResultado.getDouble("valortotal")));
			serasaRelVOs.add(serasaRelVO);
		}
		return serasaRelVOs;
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return ("SerasaRel");
	}

}