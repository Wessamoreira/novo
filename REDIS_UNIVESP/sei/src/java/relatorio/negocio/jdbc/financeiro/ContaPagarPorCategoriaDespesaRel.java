package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoSacado;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.financeiro.ContaPagarCategoriaDespesaRelVO;
import relatorio.negocio.comuns.financeiro.ContaPagarPorCategoriaDespesaRelVO;
import relatorio.negocio.interfaces.financeiro.ContaPagarPorCategoriaDespesaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 * 
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class ContaPagarPorCategoriaDespesaRel extends SuperRelatorio implements ContaPagarPorCategoriaDespesaRelInterfaceFacade {

	private static final long serialVersionUID = 778571846507555159L;

	public ContaPagarPorCategoriaDespesaRel() {
	}

	public void validarDados(Date dataInicio, Date dataFim, String filtroContaAPagar, String filtroContaPaga, String filtroContaPagaParcialmente) throws Exception {
		if (dataInicio == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaRecebidaVersoContaReceber_dataInicio"));
		}
		if (dataFim == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaRecebidaVersoContaReceber_dataTermino"));
		}
		if (dataFim.before(dataInicio)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaRecebidaVersoContaReceber_dataTerminoMaiorDataInicio"));
		}
		if (filtroContaAPagar.equals("naoFiltrar") && filtroContaPaga.equals("naoFiltrar") ) {
			throw new ConsistirException(UteisJSF.internacionalizar("Deve ser informado pelo menos um filtro de CONTA A PAGAR/CONTA PAGA diferente de NÃO FILTRAR."));
		}
	}

	@Override
	public List<ContaPagarPorCategoriaDespesaRelVO> criarObjeto(Integer categoriaDespesa, Integer unidadeEnsino, Date dataInicio, Date dataFim, String filtroContaAPagar, String filtroContaPaga, String filtroContaPagaParcialmente, String filtroContaCancelada, String possuiConta, Integer codigoContaCorrente , Boolean trazerContasSubcategoria ) throws Exception {
		validarDados(dataInicio, dataFim, filtroContaAPagar, filtroContaPaga, filtroContaPagaParcialmente);
		List<ContaPagarPorCategoriaDespesaRelVO> contaPagarRelVOs = new ArrayList<ContaPagarPorCategoriaDespesaRelVO>(0);
		SqlRowSet dadosSQL = executarConsultaParametrizada(categoriaDespesa, unidadeEnsino, dataInicio, dataFim, filtroContaAPagar, filtroContaPaga, filtroContaPagaParcialmente, filtroContaCancelada, possuiConta, codigoContaCorrente , trazerContasSubcategoria);
		while (dadosSQL.next()) {
			contaPagarRelVOs.add(montarDados(dadosSQL));
		}
		if (!contaPagarRelVOs.isEmpty()) {
			Map<String, ContaPagarCategoriaDespesaRelVO> mapaTemp = realizarCriacaoMapContaPagarCategoriaDespesaVO();
			for (ContaPagarPorCategoriaDespesaRelVO contaPagarRelVO2 : contaPagarRelVOs) {
				if (mapaTemp.containsKey(contaPagarRelVO2.getIdentificadorCategoriaDespesa())) {
					ContaPagarCategoriaDespesaRelVO categoriaDespesaRelVO = mapaTemp.get(contaPagarRelVO2.getIdentificadorCategoriaDespesa());
					if(contaPagarRelVO2.getSituacaoContaPagar().equals("PA")) {
						categoriaDespesaRelVO.setValor(categoriaDespesaRelVO.getValor() + contaPagarRelVO2.getValorCategoriaDespesa());
				}
					categoriaDespesaRelVO.setValorTotal(categoriaDespesaRelVO.getValorTotal() + contaPagarRelVO2.getValorCategoriaDespesa());			
			}
		}


			ContaPagarPorCategoriaDespesaRelVO obj = contaPagarRelVOs.get(contaPagarRelVOs.size() - 1);
			obj.setListaContaPagarCategoriaDespesaRelVO(getFacadeFactory().getContaPagarRelFacade().realizarCriacaoResumoPorCategoriaDespesa(mapaTemp));
		}
		return contaPagarRelVOs;
	}

	public List<ContaPagarCategoriaDespesaRelVO> realizarOrdenacaoCategoriaDespesaVO(Map<String, ContaPagarCategoriaDespesaRelVO> mapa) throws Exception {
		List<ContaPagarCategoriaDespesaRelVO> listaOrdenada = new ArrayList<ContaPagarCategoriaDespesaRelVO>(0);
		for (ContaPagarCategoriaDespesaRelVO obj : mapa.values()) {
			if (obj.getCodigoCategoriaDespesaPrincipal() > 0) {
				listaOrdenada = adicionarObjContaPagarCategoriaDespesaRelVOs(obj, listaOrdenada);
			} else {
				listaOrdenada.add(obj);
			}
		}
		return listaOrdenada;
	}

	public List<ContaPagarCategoriaDespesaRelVO> adicionarObjContaPagarCategoriaDespesaRelVOs(ContaPagarCategoriaDespesaRelVO obj, List<ContaPagarCategoriaDespesaRelVO> listaOrdenada) {
		for (ContaPagarCategoriaDespesaRelVO contaPagarCategoriaDespesaRelVO : listaOrdenada) {
			if (contaPagarCategoriaDespesaRelVO.getCodigoCategoriaDespesa().equals(obj.getCodigoCategoriaDespesaPrincipal())) {
				contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs().add(obj);
				return listaOrdenada;
			} else if (contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs() != null && contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs().size() > 0) {
				adicionarObjContaPagarCategoriaDespesaRelVOs(obj, contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs());
			}
		}
		return listaOrdenada;
	}

	public SqlRowSet executarConsultaParametrizada(Integer categoriaDespesa, Integer unidadeEnsino, Date dataInicio, Date dataFim, String filtroContaAPagar, String filtroContaPaga, String filtroContaPagaParcialmente, String filtroContaCancelada, String possuiConta, Integer codigoContaCorrente , Boolean trazerContasSubcategoria) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select contapagar.codigo, contapagar.datavencimento, categoriadespesa.identificadorCategoriaDespesa AS \"identificadorCategoriaDespesa\", categoriadespesa.codigo AS \"categoriadespesa.codigo\", categoriadespesa.descricao AS \"categoriaDespesa\",  fornecedor.nome AS \"fornecedor\", contapagar.banco, contapagar.funcionario, contapagar.nrdocumento, ");
		sqlStr.append(" turma.identificadorturma, banco.nome AS \"banco.nome\", contapagar.valor::NUMERIC(20, 2) AS \"valor\", contapagar.juro::NUMERIC(20, 2) AS \"juro\", ");
		sqlStr.append(" contapagar.multa::NUMERIC(20, 2) AS \"multa\", contapagar.desconto::NUMERIC(20, 2) AS \"desconto\", ");
		sqlStr.append(" contapagar.valorpago::NUMERIC(20, 2) AS \"valorPago\", contapagar.data, ");
		sqlStr.append(" aluno.nome as \"aluno.nome\", pessoaFuncionario.nome as \"pessoaFuncionario.nome\", ");
		sqlStr.append(" responsavelFinanceiro.nome as \"responsavelFinanceiro.nome\", parceiro.nome as \"parceiro.nome\", operadoracartao.nome as \"operadoracartao.nome\", ");
		sqlStr.append(" contapagar.responsavelFinanceiro, contapagar.parceiro, contapagar.situacao as situacaoContaPagar, ");
		sqlStr.append(" contapagar.pessoa, contapagar.tipoSacado, sum(centroresultadoorigem.valor) as valorCategoriaDespesa ");
		sqlStr.append(" from contapagar ");
		sqlStr.append(" inner join centroresultadoorigem on centroresultadoorigem.tipoCentroResultadoOrigem ='").append(TipoCentroResultadoOrigemEnum.CONTA_PAGAR).append("' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ");
		sqlStr.append(" inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa ");
		sqlStr.append(" left join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
		sqlStr.append(" left join turma on turma.codigo = centroresultadoorigem.turma ");
		sqlStr.append(" LEFT JOIN contapagarnegociacaopagamento ON (contapagarnegociacaopagamento.contapagar = contapagar.codigo) and contapagarnegociacaopagamento.codigo = (select max(codigo) from contapagarnegociacaopagamento cpnp where cpnp.contapagar = contapagar.codigo ) ");
		sqlStr.append(" LEFT JOIN negociacaopagamento ON (contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo) ");
		sqlStr.append(" left join banco on banco.codigo = contapagar.banco ");
		sqlStr.append(" left join pessoa aluno on aluno.codigo = contapagar.pessoa ");
		sqlStr.append(" left join parceiro on parceiro.codigo = contapagar.parceiro ");
		sqlStr.append(" left join operadoracartao on operadoracartao.codigo = contapagar.operadoracartao ");
		sqlStr.append(" left join pessoa responsavelFinanceiro on responsavelFinanceiro.codigo = contapagar.responsavelFinanceiro ");
		sqlStr.append(" left join funcionario on funcionario.codigo = contapagar.funcionario ");
		sqlStr.append(" left join pessoa pessoaFuncionario on pessoaFuncionario.codigo = funcionario.pessoa ");
		sqlStr.append(" WHERE contapagar.situacao != 'NE' ");
		if (filtroContaAPagar.equals("naoFiltrar")) {
			sqlStr.append(" and (contapagar.situacao <> 'AP') ");
		}
		if (filtroContaPaga.equals("naoFiltrar")) {
			sqlStr.append(" and (contapagar.situacao <> 'PA') ");
		}
		if (filtroContaCancelada.equals("naoFiltrar")) {
			sqlStr.append(" and (contapagar.situacao <> 'CF') ");
		}
//		if (filtroContaPagaParcialmente.equals("naoFiltrar")) {
//			sqlStr.append(" and (contapagar.situacao <> 'PP') ");
//		}
		StringBuilder filtroData = new StringBuilder("");

		if (!filtroContaAPagar.equals("naoFiltrar")) {
			filtroData.append(" ( ( contaPagar.situacao = 'AP' and ").append(filtroContaAPagar).append("::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			filtroData.append(" and ").append(filtroContaAPagar).append("::DATE <= '").append(Uteis.getDataJDBC(dataFim)).append("') ");
		}
		if (!filtroContaPaga.equals("naoFiltrar")) {
			if (!filtroData.toString().trim().isEmpty()) {
				filtroData.append(" or ");
			} else {
				filtroData.append(" ( ");
			}
			filtroData.append(" ( contaPagar.situacao = 'PA' and ").append(filtroContaPaga).append("::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			filtroData.append(" and ").append(filtroContaPaga).append("::DATE <= '").append(Uteis.getDataJDBC(dataFim)).append("') ");
		}
		if (!filtroContaCancelada.equals("naoFiltrar")) {
			if (!filtroData.toString().trim().isEmpty()) {
				filtroData.append(" or ");
			} else {
				filtroData.append(" ( ");
			}
			filtroData.append(" ( contaPagar.situacao = 'CF' and ").append(filtroContaCancelada).append("::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
			filtroData.append(" and ").append(filtroContaCancelada).append("::DATE <= '").append(Uteis.getDataJDBC(dataFim)).append("') ");
		}
//		if (!filtroContaPagaParcialmente.equals("naoFiltrar")) {
//			if (!filtroData.toString().trim().isEmpty()) {
//				filtroData.append(" or ");
//			} else {
//				filtroData.append(" ( ");
//			}
//			filtroData.append("( contaPagar.situacao = 'PP' and ").append(filtroContaPagaParcialmente).append("::DATE >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
//			filtroData.append(" and ").append(filtroContaPagaParcialmente).append("::DATE <= '").append(Uteis.getDataJDBC(dataFim)).append("') ");
//		}
		filtroData.append(" ) ");
		sqlStr.append(" AND ").append(filtroData.toString());
		if (categoriaDespesa != 0) {
			sqlStr.append(" AND (categoriadespesa.codigo = ");
			sqlStr.append(categoriaDespesa);
		if (trazerContasSubcategoria) {
			sqlStr.append(" or categoriaDespesa.categoriaDespesaPrincipal = ");
			sqlStr.append(categoriaDespesa);
		}
		sqlStr.append(" )");
		}
		if (!unidadeEnsino.equals(0)) {
			sqlStr.append(" and contaPagar.unidadeEnsino = ").append(unidadeEnsino);
		}
		if (possuiConta.equals("FILTRAR")) {
			sqlStr.append(" and exists (select 1 from formapagamentonegociacaopagamento fpnp where fpnp.negociacaocontapagar = negociacaopagamento.codigo and fpnp.contacorrente = ").append(codigoContaCorrente).append(" ) ");
		}
		sqlStr.append(" GROUP BY contapagar.codigo, categoriadespesa.codigo, contapagar.datavencimento, banco.nome, identificadorCategoriaDespesa, categoriadespesa.descricao, fornecedor.nome, contapagar.banco, contapagar.funcionario, contapagar.nrdocumento, ");
		sqlStr.append(" turma.identificadorturma, contapagar.valor, contapagar.juro, contapagar.multa, contapagar.desconto, contapagar.valorpago, contapagar.data, ");
		sqlStr.append(" aluno.nome, pessoaFuncionario.nome, operadoracartao.nome, ");
		sqlStr.append(" responsavelFinanceiro.nome, parceiro.nome, ");
		sqlStr.append(" contapagar.responsavelFinanceiro, contapagar.parceiro, ");
		sqlStr.append(" contapagar.pessoa, contapagar.tipoSacado, contapagar.situacao ");
		sqlStr.append(" ORDER BY categoriadespesa.identificadorCategoriaDespesa, categoriadespesa.descricao, contapagar.dataVencimento, ");
		sqlStr.append(" aluno.nome, pessoaFuncionario.nome, banco.nome, ");
		sqlStr.append(" responsavelFinanceiro.nome, parceiro.nome, operadoracartao.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado;
	}

	private ContaPagarPorCategoriaDespesaRelVO montarDados(SqlRowSet dadosSQL) {
		ContaPagarPorCategoriaDespesaRelVO contaPagarRelVO = new ContaPagarPorCategoriaDespesaRelVO();
		contaPagarRelVO.setCategoriaDespesa(dadosSQL.getString("categoriaDespesa"));
		contaPagarRelVO.setIdentificadorCategoriaDespesa(String.valueOf(dadosSQL.getInt("categoriadespesa.codigo")));
		contaPagarRelVO.setDataVencimento(dadosSQL.getDate("dataVencimento"));
		contaPagarRelVO.setData(dadosSQL.getDate("data"));
		contaPagarRelVO.setNrDocumento(dadosSQL.getString("nrDocumento"));
		contaPagarRelVO.setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
		contaPagarRelVO.setValor(dadosSQL.getDouble("valorCategoriaDespesa"));
//		contaPagarRelVO.setJuro(dadosSQL.getDouble("juro"));
//		contaPagarRelVO.setMulta(dadosSQL.getDouble("multa"));
//		contaPagarRelVO.setDesconto(dadosSQL.getDouble("desconto"));
		if(dadosSQL.getString("situacaoContaPagar").equals("PA")) {
			contaPagarRelVO.setValorPago(dadosSQL.getDouble("valorCategoriaDespesa"));
		}else {
			contaPagarRelVO.setValorPago(0.0);
		}
		contaPagarRelVO.setValorCategoriaDespesa(dadosSQL.getDouble("valorCategoriaDespesa"));
		contaPagarRelVO.setSituacaoContaPagar(dadosSQL.getString("situacaoContaPagar"));
		if (dadosSQL.getString("tipoSacado").equals(TipoSacado.ALUNO.getValor())) {
			contaPagarRelVO.setFavorecido(dadosSQL.getString("aluno.nome"));
		} else if (dadosSQL.getString("tipoSacado").equals(TipoSacado.BANCO.getValor())) {
			contaPagarRelVO.setFavorecido(dadosSQL.getString("banco.nome"));
		} else if (dadosSQL.getString("tipoSacado").equals(TipoSacado.FORNECEDOR.getValor())) {
			contaPagarRelVO.setFavorecido(dadosSQL.getString("fornecedor"));
		} else if (dadosSQL.getString("tipoSacado").equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor())) {
			contaPagarRelVO.setFavorecido(dadosSQL.getString("pessoaFuncionario.nome"));
		} else if (dadosSQL.getString("tipoSacado").equals(TipoSacado.PARCEIRO.getValor())) {
			contaPagarRelVO.setFavorecido(dadosSQL.getString("parceiro.nome"));
		} else if (dadosSQL.getString("tipoSacado").equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor())) {
			contaPagarRelVO.setFavorecido(dadosSQL.getString("responsavelFinanceiro.nome"));
		} else if (dadosSQL.getString("tipoSacado").equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
			contaPagarRelVO.setFavorecido(dadosSQL.getString("operadoracartao.nome"));
		}
		return contaPagarRelVO;
	}

	public Map<String, ContaPagarCategoriaDespesaRelVO> realizarCriacaoMapContaPagarCategoriaDespesaVO() throws Exception {
		Map<String, ContaPagarCategoriaDespesaRelVO> lista = new LinkedHashMap<String, ContaPagarCategoriaDespesaRelVO>(0);
		StringBuilder sql = new StringBuilder();
		sql.append(" select identificadorcategoriadespesa, descricao, codigo, categoriadespesaprincipal ");
		sql.append(" from categoriadespesa order by identificadorcategoriadespesa ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			ContaPagarCategoriaDespesaRelVO categoriaDespesaRelVO = montarDadosCategoriaDespesaVO(tabelaResultado);
			lista.put(categoriaDespesaRelVO.getCodigoCategoriaDespesa().toString(), categoriaDespesaRelVO);
		}
		return lista;
	}

	public ContaPagarCategoriaDespesaRelVO montarDadosCategoriaDespesaVO(SqlRowSet dadosSQL) {
		ContaPagarCategoriaDespesaRelVO categoriaDespesaRelVO = new ContaPagarCategoriaDespesaRelVO();
		categoriaDespesaRelVO.setIdentificadorCategoriaDespesa(dadosSQL.getString("identificadorcategoriadespesa"));
		categoriaDespesaRelVO.setDescricaoCategoriaDespesa(dadosSQL.getString("descricao"));
		categoriaDespesaRelVO.setCodigoCategoriaDespesa(dadosSQL.getInt("codigo"));
		categoriaDespesaRelVO.setCodigoCategoriaDespesaPrincipal(dadosSQL.getInt("categoriadespesaprincipal"));
		return categoriaDespesaRelVO;
	}

	@Override
	public String designIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}

	@Override
	public String designIReportRelatorioSintetico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeSintetico() + ".jrxml");
	}

	@Override
	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return ("ContaPagarPorCategoriaDespesaRel");
	}

	public static String getIdEntidadeSintetico() {
		return ("ContaPagarPorCategoriaDespesaSinteticoRel");
	}
}
