package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
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
import relatorio.negocio.comuns.financeiro.ContaPagarRelVO;
import relatorio.negocio.interfaces.financeiro.ContaPagarRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;
import relatorio.negocio.jdbc.financeiro.enumeradores.ContaPagarFiltrosEnum;

@Repository
@Scope("singleton")
@Lazy
public class ContaPagarRel extends SuperRelatorio implements ContaPagarRelInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1403067268910514833L;
	private static String idEntidade;

	public ContaPagarRel() {
		inicializarParametros();
	}

	@Override
	public List<ContaPagarRelVO> criarObjeto(ContaPagarRelVO contaPagarRelVO, Integer codigoTurma, String filtroContaAPagar, String filtroContaPaga, String filtroNegociada, String filtroCancelada, Integer codigoContaCorrente, Boolean apresentarContraCorrente, UsuarioVO usuarioVO) throws Exception {
		Double valorTotal = 0.0;

		if (contaPagarRelVO.getDataInicio() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaRecebidaVersoContaReceber_dataInicio"));
		}
		if (contaPagarRelVO.getDataFim() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaRecebidaVersoContaReceber_dataTermino"));
		}
		if (contaPagarRelVO.getDataFim().before(contaPagarRelVO.getDataInicio())) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_ContaRecebidaVersoContaReceber_dataTerminoMaiorDataInicio"));
		}

		if (filtroContaAPagar.equals("naoFiltrar") && filtroContaPaga.equals("naoFiltrar") && filtroNegociada.equals("naoFiltrar") && filtroCancelada.equals("naoFiltrar")) {
			throw new ConsistirException(UteisJSF.internacionalizar("Deve ser informado pelo menos um filtro de CONTA A PAGAR/CONTA PAGA/CONTA NEGOCIADA/CONTA CANCELADA diferente de NÃO FILTRAR."));
		}
		
		if((apresentarContraCorrente)&&(codigoContaCorrente.equals(0))){
			throw new ConsistirException(UteisJSF.internacionalizar("Deve ser informado uma conta corrente !"));
		}

		List<ContaPagarRelVO> contaPagarRelVOs = new ArrayList<ContaPagarRelVO>(0);
		SqlRowSet dadosSQL = executarConsultaParametrizada(contaPagarRelVO, codigoTurma, filtroContaAPagar, filtroContaPaga, filtroNegociada, filtroCancelada, codigoContaCorrente);
		while (dadosSQL.next()) {
			contaPagarRelVOs.add(montarDados(dadosSQL));
			valorTotal += dadosSQL.getDouble("contapagar_valorpago");
		}

		if (!contaPagarRelVOs.isEmpty()) {
			Map<String, ContaPagarCategoriaDespesaRelVO> mapaTemp = realizarCriacaoMapContaPagarCategoriaDespesaVO();

			for (ContaPagarRelVO contaPagarRelVO2 : contaPagarRelVOs) {
				if (!contaPagarRelVO2.getSituacao().equals("NE")) {
					List<CentroResultadoOrigemVO> centroResultadoOrigemVOs = getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(contaPagarRelVO2.getCodigoContaPagar().toString(), TipoCentroResultadoOrigemEnum.CONTA_PAGAR, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuarioVO);
					for (CentroResultadoOrigemVO centroResultadoOrigemVO : centroResultadoOrigemVOs) {
						if (mapaTemp.containsKey(centroResultadoOrigemVO.getCategoriaDespesaVO().getIdentificadorCategoriaDespesa())) {
							ContaPagarCategoriaDespesaRelVO categoriaDespesaRelVO = mapaTemp.get(centroResultadoOrigemVO.getCategoriaDespesaVO().getIdentificadorCategoriaDespesa());
							if (contaPagarRelVO2.getSituacao().equals("PA")) {
								categoriaDespesaRelVO.setValor(categoriaDespesaRelVO.getValor() + centroResultadoOrigemVO.getValor());
							}
							categoriaDespesaRelVO.setValorTotal(categoriaDespesaRelVO.getValorTotal() + centroResultadoOrigemVO.getValor());
				}
			}
				}
			}

								
			ContaPagarRelVO obj = contaPagarRelVOs.get(contaPagarRelVOs.size() - 1);
			obj.setListaContaPagarCategoriaDespesaRelVO(realizarCriacaoResumoPorCategoriaDespesa(mapaTemp));
		}

		return contaPagarRelVOs;
	}

	@Override
	public List<ContaPagarCategoriaDespesaRelVO> realizarCriacaoResumoPorCategoriaDespesa(Map<String, ContaPagarCategoriaDespesaRelVO> mapaTemp) throws Exception{
		List<ContaPagarCategoriaDespesaRelVO> listaOrdenada = realizarOrdenacaoCategoriaDespesaVO(mapaTemp);			
		listaOrdenada = realizarCalculoValoresContaPagarCategoriaDespesaRelVO(listaOrdenada, 1);
		return listaOrdenada;
	}

	public List<ContaPagarCategoriaDespesaRelVO> realizarCalculoValoresContaPagarCategoriaDespesaRelVO(List<ContaPagarCategoriaDespesaRelVO> listaOrdenada, Integer nivel) {		
		for (Iterator<ContaPagarCategoriaDespesaRelVO> iterator = listaOrdenada.iterator(); iterator.hasNext();) {
			ContaPagarCategoriaDespesaRelVO contaPagarCategoriaDespesaRelVO = (ContaPagarCategoriaDespesaRelVO) iterator.next();										
			executarCalculoValorCategoriaDespesaPrincipal(contaPagarCategoriaDespesaRelVO, nivel);		
			if(contaPagarCategoriaDespesaRelVO.getValorTotal().equals(0.0)) {
				iterator.remove();
			}else {
				realizarCalculoPorcentagemPaga(contaPagarCategoriaDespesaRelVO);
			}
		}
		return listaOrdenada;
	}

	public void executarCalculoValorCategoriaDespesaPrincipal(ContaPagarCategoriaDespesaRelVO contaPagarCategoriaDespesaRelVO, Integer nivel) {				
		Ordenacao.ordenarLista(contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs(), "identificadorCategoriaDespesa");
		for (Iterator<ContaPagarCategoriaDespesaRelVO> iterator = contaPagarCategoriaDespesaRelVO.getContaPagarCategoriaDespesaRelVOs().iterator(); iterator.hasNext();) {
			ContaPagarCategoriaDespesaRelVO contaPagarCategoriaDespesaRelVO2 = iterator.next();
			for (int x = 1; x <= nivel; x++) {
				contaPagarCategoriaDespesaRelVO2.setDescricaoCategoriaDespesa("     " + contaPagarCategoriaDespesaRelVO2.getDescricaoCategoriaDespesa());
				}
			executarCalculoValorCategoriaDespesaPrincipal(contaPagarCategoriaDespesaRelVO2, nivel+1);				
			if(contaPagarCategoriaDespesaRelVO2.getValorTotal().equals(0.0)) {
				iterator.remove();
			}else {
				contaPagarCategoriaDespesaRelVO.setValor(contaPagarCategoriaDespesaRelVO.getValor() + contaPagarCategoriaDespesaRelVO2.getValor());
				contaPagarCategoriaDespesaRelVO.setValorTotal(contaPagarCategoriaDespesaRelVO.getValorTotal() + contaPagarCategoriaDespesaRelVO2.getValorTotal());	
			}
		}
		realizarCalculoPorcentagemPaga(contaPagarCategoriaDespesaRelVO);
	}

	public void realizarCalculoPorcentagemPaga(ContaPagarCategoriaDespesaRelVO contaPagarCategoriaDespesaRelVO) {
		if(contaPagarCategoriaDespesaRelVO.getValor() > 0) {
			contaPagarCategoriaDespesaRelVO.setPorcentagemValorTotal((contaPagarCategoriaDespesaRelVO.getValor() * 100) / contaPagarCategoriaDespesaRelVO.getValorTotal());
				}
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
		Ordenacao.ordenarLista(listaOrdenada, "identificadorCategoriaDespesa");
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

	public Map<String, ContaPagarCategoriaDespesaRelVO> realizarCriacaoMapContaPagarCategoriaDespesaVO() throws Exception {
		Map<String, ContaPagarCategoriaDespesaRelVO> lista = new LinkedHashMap<String, ContaPagarCategoriaDespesaRelVO>(0);

		StringBuilder sql = new StringBuilder();
		sql.append("select identificadorcategoriadespesa, descricao, codigo, categoriadespesaprincipal ");
		sql.append("from categoriadespesa order by identificadorcategoriadespesa ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

		while (tabelaResultado.next()) {
			ContaPagarCategoriaDespesaRelVO categoriaDespesaRelVO = montarDadosCategoriaDespesaVO(tabelaResultado);
			lista.put(categoriaDespesaRelVO.getIdentificadorCategoriaDespesa(), categoriaDespesaRelVO);
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

	private ContaPagarRelVO montarDados(SqlRowSet dadosSQL) {
		ContaPagarRelVO contaPagarRelVO = new ContaPagarRelVO();
		contaPagarRelVO.setBanco(dadosSQL.getString("banco_nome"));
		contaPagarRelVO.setCodigoContaPagar(dadosSQL.getInt("contapagar_codigo"));
		contaPagarRelVO.setDataContaPagar(Uteis.getDataJDBC(dadosSQL.getDate("contapagar_data")));
		contaPagarRelVO.setDataVencimento(Uteis.getDataJDBC(dadosSQL.getDate("contapagar_datavencimento")));
		contaPagarRelVO.setDataPagamento(Uteis.getDataJDBC(dadosSQL.getDate("dataPagamento")));
		contaPagarRelVO.setDesconto(dadosSQL.getDouble("contapagar_desconto"));
		contaPagarRelVO.setNomeFornecedor(dadosSQL.getString("fornecedor_nome"));
		contaPagarRelVO.setNomeFuncionario(dadosSQL.getString("funcionario_nome"));
		contaPagarRelVO.setNomeBanco(dadosSQL.getString("banco_nome"));
		contaPagarRelVO.setNomeResponsavelFinanceiro(dadosSQL.getString("responsavelFinanceiro_nome"));
		contaPagarRelVO.setNomeParceiro(dadosSQL.getString("parceiro_nome"));
		contaPagarRelVO.setNomeAluno(dadosSQL.getString("aluno_nome"));
		contaPagarRelVO.setNomeOperadoraCartao(dadosSQL.getString("operadoracartao_nome"));

		contaPagarRelVO.setFornecedor(dadosSQL.getString("fornecedor_nome"));
		contaPagarRelVO.setFuncionario(dadosSQL.getString("funcionario_nome"));
		contaPagarRelVO.setBanco(dadosSQL.getString("banco_nome"));
		contaPagarRelVO.setResponsavelFinanceiro(dadosSQL.getString("responsavelFinanceiro_nome"));
		contaPagarRelVO.setParceiro(dadosSQL.getString("parceiro_nome"));
		contaPagarRelVO.setAluno(dadosSQL.getString("aluno_nome"));

		contaPagarRelVO.setJuro(dadosSQL.getDouble("contapagar_juro"));
		contaPagarRelVO.setMulta(dadosSQL.getDouble("contapagar_multa"));
		contaPagarRelVO.setNomeTurma(dadosSQL.getString("identificador_turma"));
		contaPagarRelVO.setNomeUnidadeEnsino(dadosSQL.getString("unidadeensino_nome"));
		contaPagarRelVO.setNumeroDocumento(dadosSQL.getString("contapagar_nrdocumento"));
		contaPagarRelVO.setSituacao(dadosSQL.getString("contapagar_situacao"));
		contaPagarRelVO.setTipoSacado(TipoSacado.getDescricao(dadosSQL.getString("contapagar_tiposacado")));
		contaPagarRelVO.setUnidadeEnsino(dadosSQL.getInt("contapagar_unidadeensino"));
		contaPagarRelVO.setValorContaPagar(dadosSQL.getDouble("contapagar_valor"));
		if(contaPagarRelVO.getSituacao().equals("PA")) {
			contaPagarRelVO.setValorPago(dadosSQL.getDouble("contapagar_valorpago"));
		}else {
			contaPagarRelVO.setValorPago(contaPagarRelVO.getValorContaPagar()+contaPagarRelVO.getJuro()+contaPagarRelVO.getMulta()-contaPagarRelVO.getDesconto());
		}
		contaPagarRelVO.setQuebra(dadosSQL.getString("contapagar_quebra"));
//		contaPagarRelVO.setNumeroContaCorrente(dadosSQL.getString("conta_corrente"));
		contaPagarRelVO.setListaContaPagarCategoriaDespesaRelVO(new ArrayList<ContaPagarCategoriaDespesaRelVO>(0));

		if (contaPagarRelVO.getTipoSacado().equals(TipoSacado.FORNECEDOR.getDescricao())) {
			contaPagarRelVO.setFavorecido(contaPagarRelVO.getNomeFornecedor());
			contaPagarRelVO.setCPF(dadosSQL.getString("fornecedor_cpf"));
			contaPagarRelVO.setCNPJ(dadosSQL.getString("fornecedor_cnpj"));
			contaPagarRelVO.setBancoFornecedor(dadosSQL.getString("fornecedor_nomeBanco"));
			contaPagarRelVO.setAgencia(dadosSQL.getString("fornecedor_agencia"));
			contaPagarRelVO.setContaCorrente(dadosSQL.getString("fornecedor_contaCorrenteRecebimento"));
		} else if (contaPagarRelVO.getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getDescricao())) {
			contaPagarRelVO.setFavorecido(contaPagarRelVO.getNomeFuncionario());
			contaPagarRelVO.setCPF(dadosSQL.getString("funcionario_cpf"));
			contaPagarRelVO.setCNPJ(dadosSQL.getString("funcionario_cnpj"));
			contaPagarRelVO.setAgencia(dadosSQL.getString("funcionario_agencia"));
			contaPagarRelVO.setBancoFornecedor(dadosSQL.getString("funcionario_nomeBanco"));
			contaPagarRelVO.setContaCorrente(dadosSQL.getString("funcionario_contaCorrente"));
		} else if (contaPagarRelVO.getTipoSacado().equals(TipoSacado.ALUNO.getDescricao())) {
			contaPagarRelVO.setFavorecido(contaPagarRelVO.getNomeAluno());
			contaPagarRelVO.setCPF(dadosSQL.getString("aluno_cpf"));
		} else if (contaPagarRelVO.getTipoSacado().equals(TipoSacado.BANCO.getDescricao())) {
			contaPagarRelVO.setFavorecido(contaPagarRelVO.getNomeBanco());
		} else if (contaPagarRelVO.getTipoSacado().equals(TipoSacado.PARCEIRO.getDescricao())) {
			contaPagarRelVO.setFavorecido(contaPagarRelVO.getNomeParceiro());
			contaPagarRelVO.setCNPJ(dadosSQL.getString("parceiro_cnpj"));
		} else if (contaPagarRelVO.getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getDescricao())) {
			contaPagarRelVO.setFavorecido(contaPagarRelVO.getNomeOperadoraCartao());			
		} else if (contaPagarRelVO.getTipoSacado().equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getDescricao())) {
			if (contaPagarRelVO.getNomeAluno() != null && !contaPagarRelVO.getNomeAluno().trim().isEmpty()) {
				contaPagarRelVO.setFavorecido(contaPagarRelVO.getNomeAluno());
			} else {
				contaPagarRelVO.setFavorecido(contaPagarRelVO.getNomeResponsavelFinanceiro());
			}
		}

		return contaPagarRelVO;
	}

	public SqlRowSet executarConsultaParametrizada(ContaPagarRelVO contaPagarRelVO, Integer codigoTurma, String filtroContaAPagar, String filtroContaPaga, String filtroNegociada, String filtroCancelada, Integer codigoContaCorrente) throws Exception {
		StringBuilder selectStr = new StringBuilder(" SELECT max(negociacaopagamento.data :: Date) as datapagamento, contapagar.dataFatoGerador,  ");
		selectStr.append(" aluno.cpf as aluno_cpf,pessoa.cpf as funcionario_cpf, ");
		selectStr.append(" contapagar.codigo AS contapagar_codigo, contapagar.data AS contapagar_data, contapagar.nrdocumento AS contapagar_nrdocumento, ");
		selectStr.append(" contapagar.situacao AS contapagar_situacao, contapagar.datavencimento AS contapagar_datavencimento, contapagar.valor::numeric(20,2) AS contapagar_valor, ");
		selectStr.append(" contapagar.juro::numeric(20,2) AS contapagar_juro, contapagar.multa::numeric(20,2) AS contapagar_multa, contapagar.desconto::numeric(20,2) AS contapagar_desconto, contapagar.valorpago::numeric(20,2) AS contapagar_valorpago, ");
		selectStr.append(" contapagar.tiposacado AS contapagar_tiposacado, contapagar.unidadeensino AS contapagar_unidadeensino, ");
		selectStr.append(" contapagar.turma AS contapagar_turma, contapagar.funcionario AS contapagar_funcionario, contapagar.fornecedor AS contapagar_fornecedor, ");
		selectStr.append(" contapagar.banco AS contapagar_banco, unidadeensino.nome AS unidadeensino_nome, turma.identificadorturma AS identificador_turma, pessoa.nome AS funcionario_nome,funcionario.cnpjEmpresaRecebimento as funcionario_cnpj,funcionario.numeroAgenciaRecebimento as funcionario_agencia, ");
		selectStr.append(" parceiro.cnpj as parceiro_cnpj,fornecedor.contaCorrenteRecebimento as fornecedor_contaCorrenteRecebimento,funcionario.contacorrenterecebimento as funcionario_contaCorrente,funcionario.nomebanco as funcionario_nomeBanco, ");
		selectStr.append(" fornecedor.nome AS fornecedor_nome,fornecedor.cpf as fornecedor_cpf,fornecedor.cnpj as fornecedor_cnpj,fornecedor.nomeBanco as fornecedor_nomeBanco, fornecedor.numeroAgenciaRecebimento as fornecedor_agencia, banco.nome AS banco_nome, responsavelFinanceiro.nome as responsavelFinanceiro_nome, aluno.nome as aluno_nome, parceiro.nome as parceiro_nome,  (CASE WHEN (contapagar.tiposacado = 'FO') THEN contapagar.tiposacado || contapagar.fornecedor ELSE ");
		selectStr.append(" CASE WHEN (contapagar.tiposacado = 'FU') THEN contapagar.tiposacado || contapagar.funcionario ELSE ");
		selectStr.append(" CASE WHEN (contapagar.tiposacado = 'AL') THEN contapagar.tiposacado || contapagar.pessoa ELSE ");
		selectStr.append(" CASE WHEN (contapagar.tiposacado = 'RF') THEN contapagar.tiposacado || contapagar.responsavelFinanceiro ELSE ");
		selectStr.append(" CASE WHEN (contapagar.tiposacado = 'PA') THEN contapagar.tiposacado || contapagar.parceiro ELSE ");
		selectStr.append(" CASE WHEN (contapagar.tiposacado = 'OC') THEN contapagar.tiposacado || contapagar.operadoracartao ELSE ");
		selectStr.append(" CASE WHEN (contapagar.tiposacado = 'BA') THEN contapagar.tiposacado || contapagar.banco ELSE contapagar.tiposacado || contapagar.fornecedor END END END END END END END) AS contapagar_quebra, ");
		selectStr.append(" operadoracartao.nome as operadoracartao_nome ");
//		selectStr.append(" , contacorrente.numero as conta_corrente ");
		selectStr.append(" FROM ContaPagar AS contapagar ");
		selectStr.append(" LEFT JOIN fornecedor ON (contapagar.fornecedor = fornecedor.codigo) LEFT JOIN funcionario ON (contapagar.funcionario = funcionario.codigo) ");
		selectStr.append(" LEFT JOIN pessoa ON (funcionario.pessoa = pessoa.codigo) LEFT JOIN unidadeensino ON (contapagar.unidadeensino = unidadeensino.codigo) ");
		selectStr.append(" LEFT JOIN turma ON (contapagar.turma = turma.codigo) LEFT JOIN banco ON (contapagar.banco = banco.codigo) ");
		selectStr.append(" LEFT JOIN pessoa as aluno ON (contapagar.pessoa = aluno.codigo) ");
		selectStr.append(" LEFT JOIN parceiro ON (contapagar.parceiro = parceiro.codigo) ");
		selectStr.append(" LEFT JOIN operadoracartao ON (contapagar.operadoracartao = operadoracartao.codigo) ");
		selectStr.append(" LEFT JOIN Pessoa as responsavelFinanceiro ON (contapagar.responsavelFinanceiro = responsavelFinanceiro.codigo) ");
		selectStr.append(" LEFT JOIN contapagarnegociacaopagamento ON (contapagarnegociacaopagamento.contapagar = contapagar.codigo) and contapagarnegociacaopagamento.codigo = (select max(codigo) from contapagarnegociacaopagamento cpnp where cpnp.contapagar = contapagar.codigo ) ");
		selectStr.append(" LEFT JOIN negociacaopagamento ON (contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo) ");
		selectStr.append(" LEFT JOIN formapagamentonegociacaopagamento ON (formapagamentonegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo)");
		selectStr.append(" LEFT JOIN contacorrente ON (contacorrente.codigo = formapagamentonegociacaopagamento.contacorrente)");

		selectStr = new StringBuilder(montarFiltrosRelatorio(selectStr.toString(), contaPagarRelVO, codigoTurma, filtroContaAPagar, filtroContaPaga, filtroNegociada, filtroCancelada, codigoContaCorrente));
		selectStr.append(" GROUP BY contapagar_codigo, contapagar_data, contapagar_nrdocumento, contapagar_situacao, contapagar_datavencimento, contapagar_valor, ");
		selectStr.append(" contapagar_juro, contapagar_multa, contapagar_desconto, contapagar_valorpago, contapagar_tiposacado, contapagar_quebra, ");
		selectStr.append(" unidadeensino_nome, identificador_turma, contapagar_unidadeensino, contapagar_turma, contapagar_funcionario, contapagar_fornecedor, ");
		selectStr.append(" contapagar_banco, funcionario_nome, fornecedor_nome, banco_nome, parceiro.nome, responsavelFinanceiro.nome, ");
		selectStr.append(" aluno.nome, contapagar.dataFatoGerador,fornecedor_cpf,fornecedor_cnpj,aluno_cpf,funcionario_cpf, ");
		selectStr.append(" fornecedor_agencia,fornecedor_nomeBanco,funcionario_cnpj,funcionario_agencia,parceiro_cnpj,fornecedor_contaCorrenteRecebimento,funcionario_contaCorrente,funcionario_nomeBanco, operadoracartao_nome "); 
//		selectStr.append(" , contacorrente.numero ");
		// if (contaPagarRelVO.getFiltroData().equals("dataPagamento")) {
		// selectStr += " ,contapagarpagamento.data ";
		// }

		selectStr.append("  order by unidadeensino.nome, ");
		if (contaPagarRelVO.getFiltroData().equals("dataPagamento")) {
			selectStr.append("  datapagamento, ");
		} else if (contaPagarRelVO.getFiltroData().equals("dataVencimento")) {
			selectStr.append("  contapagar_datavencimento, ");
		} else {
			selectStr.append(" contapagar.dataFatoGerador, ");
		}
		selectStr.append(" contapagar.data, contapagar.fornecedor ");

		selectStr.append(" , fornecedor.nome, aluno.nome, responsavelFinanceiro.nome, pessoa.nome, banco.nome, parceiro.nome, operadoracartao_nome ");

		// selectStr = montarOrdenacaoRelatorio(selectStr);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
		return tabelaResultado;
	}

	private String montarFiltrosRelatorio(String selectStr, ContaPagarRelVO contaPagarRelVO, Integer codigoTurma, String filtroContaAPagar, String filtroContaPaga, String filtroNegociada, String filtroCancelada, Integer codigoContaCorrente) {
		String filtros = "";
		boolean adicionarAND = true;
		if ((contaPagarRelVO.getCodigoCategoriaDespesa() != null) && (contaPagarRelVO.getCodigoCategoriaDespesa().intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( exists ( select centroresultadoorigem.codigo from centroresultadoorigem inner join categoriadespesa on categoriadespesa.codigo = centroresultadoorigem.categoriadespesa where centroresultadoorigem.tipoCentroResultadoOrigem = '"+TipoCentroResultadoOrigemEnum.CONTA_PAGAR+"' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar and categoriadespesa.codigo = " + contaPagarRelVO.getCodigoCategoriaDespesa().intValue() + "))", adicionarAND);			
			adicionarAND = true;
		}

		if ((codigoContaCorrente != null) && (codigoContaCorrente.intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( formapagamentonegociacaopagamento.contacorrente = " + codigoContaCorrente + ")", adicionarAND);
			adicionarAND = true;
		} 

		if ((contaPagarRelVO.getUnidadeEnsino() != null) && (contaPagarRelVO.getUnidadeEnsino().intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.unidadeensino = " + contaPagarRelVO.getUnidadeEnsino() + ")", adicionarAND);
			adicionarAND = true;
		}
		if ((codigoTurma != null) && (codigoTurma.intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.turma = " + codigoTurma.intValue() + ")", adicionarAND);
			adicionarAND = true;
		}
		if (contaPagarRelVO.getFornecedor().equals(ContaPagarFiltrosEnum.NENHUM.getDescricao())) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.tiposacado <> 'FO' )", adicionarAND);
			adicionarAND = true;
		} else if ((contaPagarRelVO.getFornecedor().equals(ContaPagarFiltrosEnum.FILTRAR.getDescricao())) && (contaPagarRelVO.getCodigoFornecedor() != null) && (contaPagarRelVO.getCodigoFornecedor().intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.fornecedor = " + contaPagarRelVO.getCodigoFornecedor().intValue() + ")", adicionarAND);
			adicionarAND = true;
		}
		if (contaPagarRelVO.getAluno().equals(ContaPagarFiltrosEnum.NENHUM.getDescricao())) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.tiposacado <> 'AL' )", adicionarAND);
			adicionarAND = true;
		} else if ((contaPagarRelVO.getAluno().equals(ContaPagarFiltrosEnum.FILTRAR.getDescricao())) && (contaPagarRelVO.getCodigoAluno() != null) && (contaPagarRelVO.getCodigoAluno().intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.pessoa = " + contaPagarRelVO.getCodigoAluno().intValue() + ")", adicionarAND);
			adicionarAND = true;
		}
		if (contaPagarRelVO.getParceiro().equals(ContaPagarFiltrosEnum.NENHUM.getDescricao())) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.tiposacado <> 'PA' )", adicionarAND);
			adicionarAND = true;
		} else if ((contaPagarRelVO.getParceiro().equals(ContaPagarFiltrosEnum.FILTRAR.getDescricao())) && (contaPagarRelVO.getCodigoParceiro() != null) && (contaPagarRelVO.getCodigoParceiro().intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.parceiro = " + contaPagarRelVO.getCodigoParceiro().intValue() + ")", adicionarAND);
			adicionarAND = true;
		}
		if (contaPagarRelVO.getResponsavelFinanceiro().equals(ContaPagarFiltrosEnum.NENHUM.getDescricao())) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.tiposacado <> 'RF' )", adicionarAND);
			adicionarAND = true;
		} else if ((contaPagarRelVO.getResponsavelFinanceiro().equals(ContaPagarFiltrosEnum.FILTRAR.getDescricao())) && (contaPagarRelVO.getCodigoResponsavelFinanceiro() != null) && (contaPagarRelVO.getCodigoResponsavelFinanceiro().intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.responsavelFinanceiro = " + contaPagarRelVO.getCodigoResponsavelFinanceiro().intValue() + ")", adicionarAND);
			adicionarAND = true;
		}
		if (contaPagarRelVO.getFuncionario().equals(ContaPagarFiltrosEnum.NENHUM.getDescricao())) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.tiposacado <> 'FU' )", adicionarAND);
		} else if ((contaPagarRelVO.getFuncionario().equals(ContaPagarFiltrosEnum.FILTRAR.getDescricao())) && (contaPagarRelVO.getCodigoFuncionario() != null) && (contaPagarRelVO.getCodigoFuncionario().intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.funcionario = " + contaPagarRelVO.getCodigoFuncionario().intValue() + ")", adicionarAND);
			adicionarAND = true;
		}
		if (contaPagarRelVO.getBanco().equals(ContaPagarFiltrosEnum.NENHUM.getDescricao())) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.tiposacado <> 'BA' )", adicionarAND);
		} else if ((contaPagarRelVO.getBanco().equals(ContaPagarFiltrosEnum.FILTRAR.getDescricao())) && (contaPagarRelVO.getCodigoBanco() != null) && (contaPagarRelVO.getCodigoBanco().intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.banco = " + contaPagarRelVO.getCodigoBanco().intValue() + ")", adicionarAND);
			adicionarAND = true;
		}
		if (contaPagarRelVO.getOperadoraCartao().equals(ContaPagarFiltrosEnum.NENHUM.getDescricao())) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.tiposacado <> 'OC' )", adicionarAND);
		} else if ((contaPagarRelVO.getOperadoraCartao().equals(ContaPagarFiltrosEnum.FILTRAR.getDescricao())) && (contaPagarRelVO.getCodigoOperadoraCartao() != null) && (contaPagarRelVO.getCodigoOperadoraCartao().intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( contapagar.operadoracartao = " + contaPagarRelVO.getCodigoOperadoraCartao().intValue() + ")", adicionarAND);
			adicionarAND = true;
		}
		if (contaPagarRelVO.getDepartamento().equals(ContaPagarFiltrosEnum.NENHUM.getDescricao())) {
			filtros = adicionarCondicionalWhere(filtros, " ( not exists ( select centroresultadoorigem.codigo from centroresultadoorigem inner join departamento on departamento.codigo = centroresultadoorigem.departamento where centroresultadoorigem.tipoCentroResultadoOrigem = '"+TipoCentroResultadoOrigemEnum.CONTA_PAGAR+"' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar ))", adicionarAND);
		} else if ((contaPagarRelVO.getDepartamento().equals(ContaPagarFiltrosEnum.FILTRAR.getDescricao())) && (contaPagarRelVO.getCodigoDepartamento() != null) && (contaPagarRelVO.getCodigoDepartamento().intValue() != 0)) {
			filtros = adicionarCondicionalWhere(filtros, "( exists ( select centroresultadoorigem.codigo from centroresultadoorigem inner join departamento on departamento.codigo = centroresultadoorigem.departamento where centroresultadoorigem.tipoCentroResultadoOrigem = '"+TipoCentroResultadoOrigemEnum.CONTA_PAGAR+"' and centroresultadoorigem.codOrigem = contapagar.codigo::varchar and departamento.codigo = " + contaPagarRelVO.getCodigoDepartamento().intValue() + "))", adicionarAND);
			adicionarAND = true;
		}

		if (filtroContaAPagar.equals("naoFiltrar")) {
			filtros = adicionarCondicionalWhere(filtros, "(contapagar.situacao <> 'AP')", adicionarAND);
			adicionarAND = true;
		}
		if (filtroContaPaga.equals("naoFiltrar")) {
			filtros = adicionarCondicionalWhere(filtros, "(contapagar.situacao <> 'PA')", adicionarAND);
			adicionarAND = true;
		}
		if (filtroNegociada.equals("naoFiltrar")) {
			filtros = adicionarCondicionalWhere(filtros, "(contapagar.situacao <> 'NE')", adicionarAND);
			adicionarAND = true;
		}
		if (filtroCancelada.equals("naoFiltrar")) {
			filtros = adicionarCondicionalWhere(filtros, "(contapagar.situacao <> 'CF')", adicionarAND);
			adicionarAND = true;
		}
		StringBuilder filtroData = new StringBuilder("");

		if (contaPagarRelVO.getDataInicio() != null && contaPagarRelVO.getDataFim() != null) {
			if (!filtroContaAPagar.equals("naoFiltrar")) {
				filtroData.append("( ( contaPagar.situacao = 'AP' and ").append(filtroContaAPagar).append("::DATE >= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataInicio())).append("' ");
				filtroData.append(" and ").append(filtroContaAPagar).append("::DATE <= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataFim())).append("') ");
			}
			if (!filtroContaPaga.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'PA' and ").append(filtroContaPaga).append("::DATE >= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataInicio())).append("' ");
				filtroData.append(" and ").append(filtroContaPaga).append("::DATE <= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataFim())).append("') ");
			}
			if (!filtroNegociada.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'NE' and ").append(filtroNegociada).append("::DATE >= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataInicio())).append("' ");
				filtroData.append(" and ").append(filtroNegociada).append("::DATE <= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataFim())).append("') ");
			}
			if (!filtroCancelada.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'CF' and ").append(filtroCancelada).append("::DATE >= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataInicio())).append("' ");
				filtroData.append(" and ").append(filtroCancelada).append("::DATE <= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataFim())).append("') ");
			}
			filtroData.append(" ) ");

		} else if (contaPagarRelVO.getDataInicio() != null) {
			if (!filtroContaAPagar.equals("naoFiltrar")) {
				filtroData.append("( ( contaPagar.situacao = 'AP' and ").append(filtroContaAPagar).append("::DATE >= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataInicio())).append("') ");

			}
			if (!filtroContaPaga.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'PA' and ").append(filtroContaPaga).append("::DATE >= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataInicio())).append("') ");

			}
			if (!filtroNegociada.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'NE' and ").append(filtroNegociada).append("::DATE >= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataInicio())).append("') ");

			}
			if (!filtroCancelada.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'CF' and ").append(filtroCancelada).append("::DATE >= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataInicio())).append("') ");
				
			}
			filtroData.append(" ) ");

		} else if (contaPagarRelVO.getDataFim() != null) {

			if (!filtroContaAPagar.equals("naoFiltrar")) {
				filtroData.append("( ( contaPagar.situacao = 'AP'  and ").append(filtroContaAPagar).append("::DATE <= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataFim())).append("') ");
			}
			if (!filtroContaPaga.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'PA' and ").append(filtroContaPaga).append("::DATE <= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataFim())).append("') ");
			}
			if (!filtroNegociada.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'NE' and ").append(filtroNegociada).append("::DATE <= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataFim())).append("') ");
			}
			if (!filtroCancelada.equals("naoFiltrar")) {
				if (!filtroData.toString().trim().isEmpty()) {
					filtroData.append(" or ");
				} else {
					filtroData.append(" ( ");
				}
				filtroData.append("( contaPagar.situacao = 'CF' and ").append(filtroCancelada).append("::DATE <= '").append(Uteis.getDataJDBC(contaPagarRelVO.getDataFim())).append("') ");
			}
			filtroData.append(" ) ");

		}
		if (!filtroData.toString().trim().isEmpty()) {
			filtros = adicionarCondicionalWhere(filtros, filtroData.toString(), adicionarAND);
		}
		filtros = filtros.replaceFirst("AND", "WHERE");
		selectStr += filtros;
		return selectStr;
	}

	protected String montarOrdenacaoRelatorio(String selectStr) {
		String ordenacao = (String) getOrdenacoesRelatorio().get(getOrdenarPor());

		if (ordenacao.equals("Nome")) {
			ordenacao = "fornecedor.nome";
		}
		if (ordenacao.equals("Data")) {
			ordenacao = "contapagar.data";
		}
		if (ordenacao.equals("Situação")) {
			ordenacao = "contapagar.situacao";
		}
		if (ordenacao.equals("Categoria de Despesa")) {
			ordenacao = "contapagar.centrodespesa";
		}
		if (ordenacao.equals("Unidade de Ensino")) {
			ordenacao = "unidadeensino.nome";
		}
		if (ordenacao.equals("Funcionário")) {
			ordenacao = "pessoa.nome";
		}
		if (ordenacao.equals("Fornecedor")) {
			ordenacao = "fornecedor.nome";
		}
		if (ordenacao.equals("Responsável Financeiro")) {
			ordenacao = "responsavelFinanceiro.nome";
		}
		if (ordenacao.equals("Parceiro")) {
			ordenacao = "parceiro.nome";
		}
		if (ordenacao.equals("Aluno")) {
			ordenacao = "aluno.nome";
		}
		if (ordenacao.equals("Banco")) {
			ordenacao = "banco.nome";
		}
		if (!ordenacao.equals("")) {
			if ("ContaPagarResumidaPorDataRel".equals(this.getIdEntidade())) {
				selectStr += " ORDER BY contapagar_datavencimento, contapagar_quebra, " + ordenacao;
			} else if ("ContaPagarResumidaPorFornecedorRel".equals(this.getIdEntidade())) {
				selectStr += " ORDER BY contapagar_quebra, contapagar_datavencimento, " + ordenacao;
			}
		}
		return selectStr;

	}

	@Override
	public String designIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}
	

	public String designIReportRelatorioExcel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeExcel() + ".jrxml");
	}

	@Override
	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return ("ContaPagarResumidaPorDataRel");
	}

	public static String getIdEntidadeExcel() {
		return ("ContaPagarResumidaPorDataExcelRel");
	}

	public String getNomeRelatorio() {
		return idEntidade;
	}

	public void setNomeRelatorio(String nome) {
		this.idEntidade = nome;
	}

	public void inicializarParametros() {
	}

	@Override
	public void validarDados(ContaPagarRelVO contaPagarRelVO) throws ConsistirException {
		if (contaPagarRelVO.getUnidadeEnsino() == null || contaPagarRelVO.getUnidadeEnsino() == 0) {
			throw new ConsistirException("A Unidade De Ensino deve ser informada para a geração do relatório.");
		}
	}
	
	public static String getIdEntidadeEspecifica() {
		return ("ContaPagarResumidaEspecifica");
	}

	@Override
	public String designIReportRelatorioEspecifica() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeEspecifica() + ".jrxml");
	}
}
