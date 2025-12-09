package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import relatorio.negocio.comuns.financeiro.ContasRecebimentoRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.financeiro.RecebimentoCentroReceitaRelVO;
import relatorio.negocio.comuns.financeiro.RecebimentoPorCursoCentroReceitaRelVO;
import relatorio.negocio.comuns.financeiro.RecebimentoRelVO;
import relatorio.negocio.interfaces.financeiro.RecebimentoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class RecebimentoRel extends SuperRelatorio implements RecebimentoRelInterfaceFacade {

	/**
	 *
	 */
	private static final long serialVersionUID = 1816660570983031953L;

	public List<RecebimentoRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, Integer curso, Integer turma, Integer turno, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String tipoPessoa, Integer codigoPessoa, Integer codigoParceiro, Date dataInicio, Date dataFim, Integer codigoContaCorrente, String tipoOrdenacao, String parcela, UsuarioVO usuarioLogado, Integer centroReceita, Integer planoFinanceiroCurso, Integer condicaoPagamentoPlanoFinanceiroCurso, Integer fornecedor, String layout, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean apresentarQuadroResumoCalouroVeterano, boolean considerarUnidadeEnsinoFinanceira, AgenteNegativacaoCobrancaContaReceberVO agente, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, String filtrarPeriodoPor, MatriculaVO matriculaVO) throws Exception {
		List<RecebimentoRelVO> recebimentoRelVOs = new ArrayList<RecebimentoRelVO>(0);
		Map<Integer, RecebimentoRelVO> mapRecebimentos = new HashMap<Integer, RecebimentoRelVO>();
		try {
			SqlRowSet tabelaResultado = executarConsultaParametrizada(listaUnidadeEnsino, curso, turma, turno, filtroRelatorioFinanceiroVO, tipoPessoa, codigoPessoa, codigoParceiro, dataInicio, dataFim, codigoContaCorrente, tipoOrdenacao, parcela, usuarioLogado, centroReceita, planoFinanceiroCurso, condicaoPagamentoPlanoFinanceiroCurso, fornecedor, layout, dataInicioCompetencia, dataFimCompetencia, apresentarQuadroResumoCalouroVeterano, considerarUnidadeEnsinoFinanceira, agente, tipoAgente, filtrarPeriodoPor, matriculaVO);
			while (tabelaResultado.next()) {
				if(mapRecebimentos.containsKey(tabelaResultado.getInt("contareceber_codigo"))) {
					RecebimentoRelVO recebimentoRelVO = mapRecebimentos.get(tabelaResultado.getInt("contareceber_codigo"));
					montarDadosContaRecebimentoRel(recebimentoRelVO, tabelaResultado);
				}else {
					RecebimentoRelVO recebimentoRelVO = montarDados(tabelaResultado, dataInicio, dataFim, listaUnidadeEnsino, dataInicioCompetencia, dataFimCompetencia, apresentarQuadroResumoCalouroVeterano, usuarioLogado);
					mapRecebimentos.put(tabelaResultado.getInt("contareceber_codigo"), recebimentoRelVO);
					recebimentoRelVOs.add(recebimentoRelVO);
				}				
			}			
			return recebimentoRelVOs;
		} finally {
			//soma = null;
		}
	}

	@Override
	public List<RecebimentoCentroReceitaRelVO> realizarGeracaoListaRecebimentoCentroReceita(List<RecebimentoRelVO> recebimentoRelVOs) {
		Map<String, RecebimentoCentroReceitaRelVO> recebimentoCentroReceitaRelVOs = new HashMap<String, RecebimentoCentroReceitaRelVO>(0);
		for (RecebimentoRelVO recebimentoRelVO : recebimentoRelVOs) {		
			recebimentoRelVO.setCentroResultadoOrigemVOs(consultarDadosCentroReceita(recebimentoRelVO.getCodigoContaReceber()));
			Double valorUsado = 0.0;
			if(!recebimentoRelVO.getCentroResultadoOrigemVOs().isEmpty()) {
			for(CentroResultadoOrigemVO centroResultadoOrigemVO: recebimentoRelVO.getCentroResultadoOrigemVOs()) {
				Double valorConsiderar = centroResultadoOrigemVO.getValor(); // Uteis.arrendondarForcando2CadasDecimais(((recebimentoRelVO.getValorTotalRecebido()*centroResultadoOrigemVO.getPorcentagem())/100.0));
				if (recebimentoCentroReceitaRelVOs.containsKey(centroResultadoOrigemVO.getCentroReceitaVO().getDescricao())) {
					double valor = recebimentoCentroReceitaRelVOs.get(centroResultadoOrigemVO.getCentroReceitaVO().getDescricao()).getValor();
					valor += valorConsiderar;
					recebimentoCentroReceitaRelVOs.get(centroResultadoOrigemVO.getCentroReceitaVO().getDescricao()).setValor(valor);
				} else {
					RecebimentoCentroReceitaRelVO recebimentoCentroReceitaRelVO = new RecebimentoCentroReceitaRelVO();
					recebimentoCentroReceitaRelVO.setCentroReceita(centroResultadoOrigemVO.getCentroReceitaVO().getDescricao());
					recebimentoCentroReceitaRelVO.setValor(valorConsiderar);
					recebimentoCentroReceitaRelVOs.put(centroResultadoOrigemVO.getCentroReceitaVO().getDescricao(), recebimentoCentroReceitaRelVO);
				}
				valorUsado += valorConsiderar;
			}
			if(valorUsado > recebimentoRelVO.getValorTotalRecebido()) {
				double valor = recebimentoCentroReceitaRelVOs.get(recebimentoRelVO.getCentroResultadoOrigemVOs().get(0).getCentroReceitaVO().getDescricao()).getValor();
				valor -= (valorUsado - recebimentoRelVO.getValorTotalRecebido());
				recebimentoCentroReceitaRelVOs.get(recebimentoRelVO.getCentroResultadoOrigemVOs().get(0).getCentroReceitaVO().getDescricao()).setValor(valor);
			}else if(valorUsado < recebimentoRelVO.getValorTotalRecebido()) {
				double valor = recebimentoCentroReceitaRelVOs.get(recebimentoRelVO.getCentroResultadoOrigemVOs().get(0).getCentroReceitaVO().getDescricao()).getValor();
				valor += (recebimentoRelVO.getValorTotalRecebido() - valorUsado);
				recebimentoCentroReceitaRelVOs.get(recebimentoRelVO.getCentroResultadoOrigemVOs().get(0).getCentroReceitaVO().getDescricao()).setValor(valor);
			}
			}
		}
		List<RecebimentoCentroReceitaRelVO> rcrVOs = new ArrayList<RecebimentoCentroReceitaRelVO>(0);
		rcrVOs.addAll(recebimentoCentroReceitaRelVOs.values());
		return rcrVOs;
	}

	private RecebimentoRelVO montarDados(SqlRowSet dadosSQL, Date dataInicio, Date dataFim, List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean apresentarQuadroResumoCalouroVeterano, UsuarioVO usuarioLogado) throws Exception {
		RecebimentoRelVO recebimentoRelVO = new RecebimentoRelVO();
		if (!listaUnidadeEnsino.isEmpty()) {
			for (UnidadeEnsinoVO ue : listaUnidadeEnsino) {
				recebimentoRelVO.setNomeUnidadeEnsino(recebimentoRelVO.getNomeUnidadeEnsino() + ";" + ue.getAbreviatura());
			}
		} else {
			recebimentoRelVO.setNomeUnidadeEnsino("Todas");
		}
		recebimentoRelVO.setDataInicio(Uteis.getDataJDBC(dataInicio));
		recebimentoRelVO.setDataFim(Uteis.getDataJDBC(dataFim));
		recebimentoRelVO.setDataInicioCompetencia(dataInicioCompetencia);
		recebimentoRelVO.setDataFimCompetencia(dataFimCompetencia);
		recebimentoRelVO.setData(Uteis.getDataJDBC(dadosSQL.getDate("negociacaorecebimento_data")));
		recebimentoRelVO.setDataVencimento(Uteis.getDataJDBC(dadosSQL.getDate("contareceber_dataVencimento")));
		recebimentoRelVO.setDesconto(dadosSQL.getDouble("contareceber_desconto"));
		recebimentoRelVO.setDigito(dadosSQL.getString("contacorrente_digito"));
		recebimentoRelVO.setJuro(dadosSQL.getDouble("contareceber_juro"));
		recebimentoRelVO.setMulta(dadosSQL.getDouble("contareceber_multa"));
		recebimentoRelVO.setNomeParceiro(dadosSQL.getString("parceiro_nome"));
		recebimentoRelVO.setCentroReceita(dadosSQL.getString("centroreceita_descricao"));
		recebimentoRelVO.setCodigoContaReceber(dadosSQL.getInt("contareceber_codigo"));
		if (!recebimentoRelVO.getNomeParceiro().equals("")) {
			if (dadosSQL.getString("pessoa_nome") != null) {
				recebimentoRelVO.setNomePessoa(dadosSQL.getString("pessoa_nome") + "  /  " + dadosSQL.getString("parceiro_nome"));
			} else {
				recebimentoRelVO.setNomePessoa(dadosSQL.getString("parceiro_nome"));
			}
		} else {
			if (dadosSQL.getString("filho") != null) {
				if (dadosSQL.getString("filho").trim().isEmpty()) {
					recebimentoRelVO.setNomePessoa(dadosSQL.getString("pessoa_nome"));
				} else {
					recebimentoRelVO.setNomePessoa(dadosSQL.getString("pessoa_nome") + " (" + dadosSQL.getString("filho") + ")");
				}
			} else {
				recebimentoRelVO.setNomePessoa(dadosSQL.getString("pessoa_nome"));
			}
		}
		if (dadosSQL.getString("negociacaorecebimento_tipopessoa").equals("FO")) {
			recebimentoRelVO.setNomePessoa(dadosSQL.getString("fornecedor_nome"));
		}
		recebimentoRelVO.setNrDocumento(dadosSQL.getString("contareceber_nrdocumento"));
		recebimentoRelVO.setNumeroContaCorrente(dadosSQL.getString("contacorrente_numero"));
		recebimentoRelVO.setParcela(dadosSQL.getString("contareceber_parcela"));
		recebimentoRelVO.setTipoOrigem(dadosSQL.getString("contareceber_tipoorigem"));
		recebimentoRelVO.setTipoPessoa(dadosSQL.getString("negociacaorecebimento_tipopessoa"));
		recebimentoRelVO.setValor(dadosSQL.getDouble("contareceber_valor"));
		recebimentoRelVO.setValorRecebido(dadosSQL.getDouble("contareceber_valorrecebidogeral"));
		recebimentoRelVO.setNomeResponsavel(Uteis.getNomeResumidoPessoa(dadosSQL.getString("usuario_responsavel")));
		recebimentoRelVO.setRecebimentoBancario(dadosSQL.getString("contareceber_recebimentobancario"));
		recebimentoRelVO.setAcrescimo(dadosSQL.getDouble("acrescimo"));
		if (dadosSQL.getDouble("valorIndiceReajustePorAtraso") >= 0.0) {
			recebimentoRelVO.setAcrescimo(recebimentoRelVO.getAcrescimo() + dadosSQL.getDouble("valorIndiceReajustePorAtraso"));
		}else if (dadosSQL.getDouble("valorIndiceReajustePorAtraso") < 0.0){
			recebimentoRelVO.setDesconto(recebimentoRelVO.getDesconto() - dadosSQL.getDouble("valorIndiceReajustePorAtraso"));
		}
		if(dadosSQL.getDouble("valorreajustediferencaparcelarecebidaouenviadaremessa") > 0.0) {
			recebimentoRelVO.setAcrescimo(recebimentoRelVO.getAcrescimo() + dadosSQL.getDouble("valorreajustediferencaparcelarecebidaouenviadaremessa"));
		}
		if (dadosSQL.getBoolean("negociacaorecebimento_recebimentoBoletoAutomatico")) {
			recebimentoRelVO.setRecebimentoBoleto("Automático");
		} else {
			recebimentoRelVO.setRecebimentoBoleto("Manual");
		}
		if (apresentarQuadroResumoCalouroVeterano) {
			recebimentoRelVO.setTipoCalouroVeterano(dadosSQL.getString("tipoCalouroVeterano"));
		}
		recebimentoRelVO.setCpf(dadosSQL.getString("pessoa_cpf"));
		montarDadosContaRecebimentoRel(recebimentoRelVO, dadosSQL);
		return recebimentoRelVO;
	}
	
	private void montarDadosContaRecebimentoRel(RecebimentoRelVO recebimentoRelVO, SqlRowSet dadosSQL) {
		ContasRecebimentoRelVO contasRecebimentoRelVO = null;
		recebimentoRelVO.setValorTotalRecebido(recebimentoRelVO.getValorTotalRecebido() + dadosSQL.getDouble("contareceber_valorrecebido"));
		contasRecebimentoRelVO = new ContasRecebimentoRelVO();
		if (dadosSQL.getString("contacorrente_numero") == null) {
			contasRecebimentoRelVO.setContaCorrente("");
		} else {
			if (Uteis.isAtributoPreenchido(dadosSQL.getString("nomeApresentacaoSistema"))) {
				contasRecebimentoRelVO.setContaCorrente(dadosSQL.getString("nomeApresentacaoSistema"));
			} else {
				contasRecebimentoRelVO.setContaCorrente(dadosSQL.getString("contacorrente_numero") + " - " + dadosSQL.getString("contacorrente_digito"));
			}
		}
		if (dadosSQL.getString("forma_tipo") != null && dadosSQL.getString("forma_tipo").equals("BO")) {
			contasRecebimentoRelVO.setFormaPagamento("Boleto (" + recebimentoRelVO.getRecebimentoBoleto() + ")");
		} else {
			contasRecebimentoRelVO.setFormaPagamento(dadosSQL.getString("forma_pagamento"));
		}
		contasRecebimentoRelVO.setValorRecebido(dadosSQL.getDouble("contareceber_valorrecebido"));		
		recebimentoRelVO.getContasRecebimentoRelVOs().add(contasRecebimentoRelVO);		
	}

	public SqlRowSet executarConsultaParametrizada(List<UnidadeEnsinoVO> listaUnidadeEnsino, Integer curso, Integer turma, Integer turno, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String tipoPessoa, Integer codigoPessoa, Integer codigoParceiro, Date dataInicio, Date dataFim, Integer codigoContaCorrente, String tipoOrdenacao, String parcela, UsuarioVO usuarioLogado, Integer centroReceita, Integer planoFinanceiroCurso, Integer condicaoPagamentoPlanoFinanceiroCurso, Integer fornecedor, String layout, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean apresentarQuadroResumoCalouroVeterano, boolean considerarUnidadeEnsinoFinanceira, AgenteNegativacaoCobrancaContaReceberVO agente, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, String filtrarPeriodoPor, MatriculaVO matriculaVO) throws Exception {
		StringBuilder selectStr = getSqlConsultaBase(listaUnidadeEnsino, curso, turma, turno, filtroRelatorioFinanceiroVO, tipoPessoa, codigoPessoa, codigoParceiro, dataInicio, dataFim, codigoContaCorrente, tipoOrdenacao, parcela, usuarioLogado, centroReceita, planoFinanceiroCurso, condicaoPagamentoPlanoFinanceiroCurso, fornecedor, layout, dataInicioCompetencia, dataFimCompetencia, apresentarQuadroResumoCalouroVeterano, considerarUnidadeEnsinoFinanceira, agente, tipoAgente, filtrarPeriodoPor, matriculaVO);
		return getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
	}

	private StringBuilder getSqlConsultaBase(List<UnidadeEnsinoVO> listaUnidadeEnsino, Integer curso, Integer turma, Integer turno, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String tipoPessoa, Integer codigoPessoa, Integer codigoParceiro, Date dataInicio, Date dataFim, Integer codigoContaCorrente, String tipoOrdenacao, String parcela, UsuarioVO usuarioLogado, Integer centroReceita, Integer planoFinanceiroCurso, Integer condicaoPagamentoPlanoFinanceiroCurso, Integer fornecedor, String layout, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean apresentarQuadroResumoCalouroVeterano, boolean considerarUnidadeEnsinoFinanceira, AgenteNegativacaoCobrancaContaReceberVO agente, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, String filtrarPeriodoPor, MatriculaVO matriculaVO) throws Exception {
		StringBuilder selectStr = new StringBuilder();
		selectStr.append("SELECT negociacaorecebimento.tipopessoa as negociacaorecebimento_tipopessoa, ");
		selectStr.append("negociacaorecebimento.data as negociacaorecebimento_data, negociacaorecebimento.recebimentoBoletoAutomatico AS negociacaorecebimento_recebimentoBoletoAutomatico, ");
		selectStr.append("contareceber.tipoorigem as contareceber_tipoorigem, contareceber.nrdocumento as contareceber_nrdocumento, ");
		selectStr.append("contareceber.valor as contareceber_valor, contareceber.dataVencimento as contareceber_dataVencimento, contareceber.valorIndiceReajustePorAtraso, contareceber.valorreajustediferencaparcelarecebidaouenviadaremessa, ");
		selectStr.append("case when contareceber.tipopessoa = 'RF' then p2.nome else '' end as filho, ");
		selectStr.append("contareceber.valorrecebido as contareceber_valorrecebidogeral, ");
		selectStr.append("case when crr.codigo is not null then crr.valorrecebimento else contareceber.valorrecebido end as contareceber_valorrecebido, contareceber.multa as contareceber_multa, contareceber.juro as contareceber_juro, ");
		selectStr.append("case when (valordescontoalunojacalculado is null) then 0.0 else valordescontoalunojacalculado end + ");
		selectStr.append("case when (descontoconvenio is null) then 0.0 else descontoconvenio end + ");
		selectStr.append("case when (descontoinstituicao is null) then 0.0 else descontoinstituicao end + ");
		// selectStr.append("case when (valordescontoprogressivo is null or descontoprogressivoutilizado = 'Nenhum') then 0.0 else valordescontoprogressivo::NUMERIC(20,2) end + ");
		selectStr.append("case when (valordescontoprogressivo is null) then 0.0 else valordescontoprogressivo end + ");
		selectStr.append("case when (valordescontorateio is null) then 0.0 else valordescontorateio end + ");
		selectStr.append("case when (valorCalculadoDescontoLancadoRecebimento is null) then 0.0 else valorCalculadoDescontoLancadoRecebimento end as contareceber_desconto, ");
		selectStr.append("contareceber.parcela as contareceber_parcela, contacorrente.numero as contacorrente_numero, ");
		selectStr.append("contacorrente.digito as contacorrente_digito, CASE WHEN (parceiro.nome  is null) then pessoa.nome else p2.nome END as pessoa_nome, parceiro.nome as parceiro_nome, usuario.nome as usuario_responsavel, ");
		selectStr.append("CASE WHEN contareceber.recebimentobancario IS TRUE THEN 'Sim' WHEN contareceber.recebimentobancario IS FALSE THEN 'Não' ELSE 'Não Informado' END as contareceber_recebimentobancario, ");
		selectStr.append("fp.nome AS forma_pagamento, fp.tipo AS forma_tipo, contareceber.codigo AS contareceber_codigo, contareceber.acrescimo, crr.datarecebimento, fornecedor.nome as fornecedor_nome, unidadeensino.nome as nomeUnidadeEnsino, ");
		selectStr.append("curso.nome as \"curso.nome\", turma.identificadorturma as \"turma.identificadorturma\", responsavelfinanceiro.nome AS nome_responsavelFinanceiro, contacorrente.nomeApresentacaoSistema, ");
		selectStr.append(" (case when parceiro.nome is not null and parceiro.tipoempresa = 'FI' then parceiro.cpf else ");
		selectStr.append(" case when parceiro.nome is not null and parceiro.tipoempresa = 'JU' then parceiro.cnpj else pessoa.cpf end end ) as pessoa_cpf, ");
		if(layout.contains("centroReceita") || layout.contains("sinteticoCentroReceita")) {
			selectStr.append(" (select array_to_string(array_agg(distinct centroreceita.descricao order by centroreceita.descricao), ', ') as centroreceita_descricao ");
			selectStr.append(" from CentroResultadoOrigem ");
			selectStr.append(" inner JOIN centroreceita ON (CentroResultadoOrigem.centroreceita = centroreceita.codigo) ");		
			selectStr.append(" where CentroResultadoOrigem.tipoCentroResultadoOrigem = 'CONTA_RECEBER' and CentroResultadoOrigem.codOrigem = contareceber.codigo::varchar ) as centroreceita_descricao   ");
		}else {
			selectStr.append(" '' as centroreceita_descricao ");
		}
		
		if (apresentarQuadroResumoCalouroVeterano) {
			selectStr.append(", case when (");
			selectStr.append(" select count(matper.codigo) from matriculaperiodo matper ");
			selectStr.append(" where matper.codigo = contareceber.matriculaperiodo ");
			selectStr.append(" and matper.matricula = matricula.matricula ");
			selectStr.append(" and ((curso.periodicidade = 'SE' and matper.ano = (");
			selectStr.append(" select matriculaperiodo.ano from matriculaperiodo where matriculaperiodo.matricula = matricula.matricula ");
			selectStr.append(" and matriculaperiodo.situacaomatriculaperiodo != 'PC' ");
			selectStr.append(" order by matriculaperiodo.data desc, matriculaperiodo.codigo desc limit 1) ");
			selectStr.append(" and matper.semestre = (");
			selectStr.append(" select matriculaperiodo.semestre from matriculaperiodo where matriculaperiodo.matricula = matricula.matricula ");
			selectStr.append(" and matriculaperiodo.situacaomatriculaperiodo != 'PC'");
			selectStr.append(" order by matriculaperiodo.data desc, matriculaperiodo.codigo desc limit 1) ");
			selectStr.append(" )  or (curso.periodicidade = 'AN' and matper.ano = (");
			selectStr.append(" select matriculaperiodo.ano from matriculaperiodo where matriculaperiodo.matricula = matricula.matricula ");
			selectStr.append(" and matriculaperiodo.situacaomatriculaperiodo != 'PC' ");
			selectStr.append(" order by matriculaperiodo.data desc, matriculaperiodo.codigo desc limit 1)) or (curso.periodicidade = 'IN' and  matper.codigo = (");
			selectStr.append(" select codigo from matriculaperiodo where matriculaperiodo.matricula = matricula.matricula ");
			selectStr.append(" and matriculaperiodo.situacaomatriculaperiodo != 'PC' ");
			selectStr.append(" order by matriculaperiodo.data desc limit 1))) and (( formaingresso not in ('TI' , 'TE') ");
			selectStr.append(" and matricula.matricula not in (");
			selectStr.append(" select distinct transferenciaentrada.matricula from transferenciaentrada ");
			selectStr.append(" where tipotransferenciaentrada in ('IN', 'EX') and matricula is not null) and  0 = (");
			selectStr.append(" select count(matper2.codigo) from matriculaperiodo matper2 where matper2.matricula = matper.matricula ");
			selectStr.append(" and matper2.situacaomatriculaperiodo != 'PC' ");
			selectStr.append(" and case when curso.periodicidade = 'IN' then matper2.data < matper.data else (matper2.ano||'/'||matper2.semestre) < (matper.ano||'/'||matper.semestre) end  ");
			selectStr.append(" ))) ) > 0 then 'CALOURO' ELSE 'VETERANO' end AS tipoCalouroVeterano ");
		}
		selectStr.append(getSqlFrom(considerarUnidadeEnsinoFinanceira, filtrarPeriodoPor, dataInicio, dataFim, listaUnidadeEnsino));		 
		selectStr.append(montarFiltrosRelatorio(parcela, listaUnidadeEnsino, curso, turma, turno, filtroRelatorioFinanceiroVO, tipoPessoa, codigoPessoa, codigoParceiro, dataInicio, dataFim, codigoContaCorrente, usuarioLogado, centroReceita, planoFinanceiroCurso, condicaoPagamentoPlanoFinanceiroCurso, fornecedor, dataInicioCompetencia, dataFimCompetencia, agente, tipoAgente, filtrarPeriodoPor, matriculaVO));

		selectStr.append(" GROUP BY unidadeensino.nome, crr.codigo, p2.nome, negociacaorecebimento_tipopessoa, negociacaorecebimento_data, negociacaorecebimento_recebimentoBoletoAutomatico, contareceber_tipoorigem, contareceber_nrdocumento, contareceber_valor, contareceber_valorrecebidogeral, ");
		selectStr.append("contareceber_valorrecebido, contareceber_multa, contareceber_juro, contareceber_desconto, contareceber_parcela, contacorrente_numero, fornecedor.nome, contareceber.valorreajustediferencaparcelarecebidaouenviadaremessa, ");
		selectStr.append("contacorrente_digito, pessoa_nome, parceiro_nome, contareceber_recebimentobancario, usuario_responsavel, forma_pagamento, forma_tipo, contareceber_codigo, contareceber.acrescimo, crr.datarecebimento, contareceber.dataVencimento, ");
		selectStr.append("curso.nome, turma.identificadorturma, contareceber.tipopessoa, responsavelfinanceiro.nome, matricula.matricula, curso.periodicidade,contacorrente.nomeApresentacaoSistema, pessoa.cpf, parceiro.tipoempresa, parceiro.cpf, parceiro.cnpj ");
		// selectStr.append(" ORDER BY contareceber_codigo, ");
		if (!layout.equals("sinteticoFormaRecebimento")) {

			if (layout.equals("padrao")) {
				if (tipoOrdenacao.equals("data")) {
					selectStr.append("ORDER BY cast(negociacaorecebimento.data AS date), pessoa_nome, parceiro_nome, fornecedor_nome ");
				} else if (tipoOrdenacao.equals("nomeResponsavelFinanceiro")) {
					selectStr.append("ORDER BY nome_responsavelFinanceiro, filho, pessoa_nome,  parceiro_nome, fornecedor_nome ");
				} else if (tipoOrdenacao.equals("nomeAluno")) {
					selectStr.append("ORDER BY filho, pessoa_nome, parceiro_nome, fornecedor_nome ");
				} else {
					selectStr.append("ORDER BY pessoa_nome, parceiro_nome, fornecedor_nome ");
				}
			} else if (layout.equals("diadia")) {
				selectStr.append("order by negociacaorecebimento.data");
			} else {
				if (tipoOrdenacao.equals("data")) {
					selectStr.append("ORDER BY centroreceita_descricao, negociacaorecebimento.data");
				} else {
					selectStr.append("ORDER BY centroreceita_descricao, pessoa_nome, parceiro_nome, fornecedor_nome");
				}
			}
		}
		return selectStr;
	}

        /**
         * Método responsável por gerar o relatórios de recebimento, considerando uma data base de faturamento.
         * Layout´s: faturamentoSinteticoCursoCentroReceita e faturamentoSinteticoCurso
         * Este relatório tem como características principal, que se for informado uma dataBaseFaturamento,
         * então o sistema irá considerar como recebidas (valor pra faturamento) as contas recebidas até esta
         * data. Por exemplo, se este relatório estiver sendo emitido para o mes de agosto (dia 01 ao dia 31).
         * E a dataBase de faturamento for dia 22. Então teremos que uma conta que foi recebida no dia 25, deverá
         * ser contabilizada como a receber. Pois, o relatório deverá apresentar a situação da conta no dia 22,
         * data na qual a conta estava em aberto.
         * Para o layout que totaliza por tipo de Origem (denominado de centro de receita, abaixo) também teremos
         * que utilizar este campo para agrupamento e totalização.
         */
	@Override
	public List<RecebimentoPorCursoCentroReceitaRelVO> criarRelatorioFaturamentoSinteticoPorCursoTipoOrigem(
                List<UnidadeEnsinoVO> listaUnidadeEnsino, Integer curso, Integer turma, Integer turno,
                FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String tipoPessoa, Integer codigoPessoa,
                Integer codigoParceiro, Date dataInicio, Date dataFim, Integer codigoContaCorrente, String tipoOrdenacao,
                String parcela, UsuarioVO usuarioLogado, Integer centroReceita, Integer planoFinanceiroCurso, Integer condicaoPagamentoPlanoFinanceiroCurso,
                Integer fornecedor, String layout, Boolean controlarDataBaseFaturamento, Date dataBaseFaturamento, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean apresentarQuadroResumoCalouroVeterano,
                boolean considerarUnidadeEnsinoFinanceira, AgenteNegativacaoCobrancaContaReceberVO agente, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, String filtrarPeriodoPor, MatriculaVO matriculaVO) throws Exception {
            Date dataBaseFaturamentoAplicar = dataFim;
            if (controlarDataBaseFaturamento) {
                dataBaseFaturamentoAplicar = dataBaseFaturamento;
            }
            StringBuilder selectStr = new StringBuilder(" SELECT");
            selectStr.append(" curso.codigo as \"curso.codigo\",");
            selectStr.append(" curso.nome as \"curso.nome\",");
            selectStr.append(" count(contaReceber.codigo) as \"nrTitulos\",");
            if (layout.equals("faturamentoSinteticoCursoCentroReceita")) {
                selectStr.append(" contaReceber.tipoOrigem as \"tipoOrigem\",");
            }
            selectStr.append(" EXTRACT(month from contaReceber.dataCompetencia) as \"mesCompetencia\",");
            selectStr.append(" EXTRACT(year from contaReceber.dataCompetencia) as \"anoCompetencia\",");
            selectStr.append(" sum(contareceber.valorBaseContaReceber::NUMERIC(20,2)) as \"totalValorBase\", ");
            selectStr.append(" sum(coalesce(descontoinstituicao, 0)) as \"totalDescontoInstitucional\",");
            selectStr.append(" sum((contareceber.valorBaseContaReceber - coalesce(descontoinstituicao, 0))) as \"totalValorBaseDeduzidoDescontoInstitucional\",");
            selectStr.append(" sum((coalesce(contareceber.multa, 0) + coalesce(contareceber.juro, 0) + coalesce(contareceber.acrescimo, 0) + coalesce(contareceber.valorreajustediferencaparcelarecebidaouenviadaremessa, 0) + case when valorindicereajusteporatraso > 0 then valorindicereajusteporatraso else 0.0 end)) as \"totalJurosMultasAcrescimos\",");
            selectStr.append(" sum((coalesce(contareceber.valordescontoprogressivo, 0))) as \"totalDescontoProgressivo\",");
            selectStr.append(" sum((coalesce(contareceber.descontoConvenio, 0))) as \"totalDescontoConvenio\",");
            selectStr.append(" sum((coalesce(contareceber.valorCalculadoDescontoLancadoRecebimento, 0) + coalesce(valordescontorateio, 0)) + case when valorindicereajusteporatraso < 0 then valorindicereajusteporatraso else 0 end) as \"totalDescontoRecebimento\",");
            selectStr.append(" sum((coalesce(contareceber.valorCusteadoContaReceber, 0))) as \"totalValorCusteadoConvenio\",");
            selectStr.append(" sum(( case when ((negociacaorecebimento.data::Date <= '").append(Uteis.getDataBD0000(dataBaseFaturamentoAplicar)).append("'::DATE) and (contareceber.situacao = 'RE')) ");
            selectStr.append("            then (contareceber.valorBaseContaReceber - descontoinstituicao)::NUMERIC(20,2) else 0.0 end )::NUMERIC(20,2)) as \"totalValorRecebidoAteDataBaseFaturamento\",");
            selectStr.append(" sum(( case when ((negociacaorecebimento.data::Date > '").append(Uteis.getDataBD0000(dataBaseFaturamentoAplicar)).append("'::DATE) or (contareceber.situacao = 'AR')) ");
            selectStr.append("            then (contareceber.valorBaseContaReceber - descontoinstituicao)::NUMERIC(20,2) else 0.0 end )::NUMERIC(20,2)) as \"totalValorAReceberDataBaseFaturamento\",");
            selectStr.append(" sum(( case when ((negociacaorecebimento.data::Date <= '").append(Uteis.getDataBD0000(dataBaseFaturamentoAplicar)).append("'::DATE) and (contareceber.situacao = 'NE'))");
            selectStr.append("            then (contareceber.valorBaseContaReceber - descontoinstituicao)::NUMERIC(20,2) else 0.0 end )::NUMERIC(20,2)) as \"totalValorRenegociadoAteDataBaseFaturamento\",");
            selectStr.append(" sum(( case when ((negociacaorecebimento.data::Date > '").append(Uteis.getDataBD0000(dataBaseFaturamentoAplicar)).append("'::DATE) and (contareceber.situacao = 'NE'))");
            selectStr.append("            then (contareceber.valorBaseContaReceber - descontoinstituicao)::NUMERIC(20,2) else 0.0 end )::NUMERIC(20,2)) as \"totalValorRenegociadoAposDataBaseFaturamento\"");
            selectStr.append(getSqlFrom(considerarUnidadeEnsinoFinanceira, filtrarPeriodoPor, dataInicio, dataFim, listaUnidadeEnsino));            
            selectStr.append(montarFiltrosRelatorio(parcela, listaUnidadeEnsino, curso, turma, turno, filtroRelatorioFinanceiroVO, tipoPessoa, codigoPessoa, codigoParceiro, dataInicio, dataFim, codigoContaCorrente, usuarioLogado, centroReceita, planoFinanceiroCurso, condicaoPagamentoPlanoFinanceiroCurso, fornecedor, dataInicioCompetencia, dataFimCompetencia, agente, tipoAgente, filtrarPeriodoPor, matriculaVO));

            selectStr.append(" GROUP BY curso.codigo, curso.nome, ");
            if (layout.equals("faturamentoSinteticoCursoCentroReceita")) {
                selectStr.append(" contaReceber.tipoOrigem, ");
            }
            selectStr.append(" EXTRACT(MONTH from contaReceber.dataCompetencia),");
            selectStr.append(" EXTRACT(YEAR from contaReceber.dataCompetencia)");
            selectStr.append(" ORDER BY curso.nome, ");
            if (layout.equals("faturamentoSinteticoCursoCentroReceita")) {
                selectStr.append(" contaReceber.tipoOrigem, ");
            }
            selectStr.append(" EXTRACT(YEAR from contaReceber.dataCompetencia),");
            selectStr.append(" EXTRACT(MONTH from contaReceber.dataCompetencia)");
            SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
            List<RecebimentoPorCursoCentroReceitaRelVO> listaRetorno = new ArrayList<RecebimentoPorCursoCentroReceitaRelVO>(0);
            HashMap<Integer, List<RecebimentoPorCursoCentroReceitaRelVO>> map = new HashMap<Integer, List<RecebimentoPorCursoCentroReceitaRelVO>>();
            while (rs.next()) {
                RecebimentoPorCursoCentroReceitaRelVO obj1 = new RecebimentoPorCursoCentroReceitaRelVO();
                obj1.getCurso().setCodigo(rs.getInt("curso.codigo"));
                if (!map.containsKey(rs.getInt("curso.codigo"))) {
                	map.put(rs.getInt("curso.codigo"), consultarQuadroResumoCurso(listaUnidadeEnsino, rs.getInt("curso.codigo"), turma, turno, filtroRelatorioFinanceiroVO, tipoPessoa, codigoPessoa, codigoParceiro, dataInicio, dataFim, codigoContaCorrente, tipoOrdenacao, parcela, usuarioLogado, centroReceita, planoFinanceiroCurso, condicaoPagamentoPlanoFinanceiroCurso, fornecedor, layout, controlarDataBaseFaturamento, dataBaseFaturamentoAplicar, dataInicioCompetencia, dataFimCompetencia, apresentarQuadroResumoCalouroVeterano, considerarUnidadeEnsinoFinanceira, agente, tipoAgente, filtrarPeriodoPor, matriculaVO));
                }
                obj1.getCurso().setNome(rs.getString("curso.nome"));
                if (layout.equals("faturamentoSinteticoCursoCentroReceita")) {
                    obj1.setTipoOrigem(TipoOrigemContaReceber.getDescricao(rs.getString("tipoOrigem")));
                }
                String mesStr = rs.getString("mesCompetencia");
                mesStr = mesStr.substring(0, mesStr.indexOf(".")); // removendo ponto do mes gerado pelo EXTRACT do sql
                if (mesStr.length() == 1) {
                    mesStr = "0" + mesStr; // colocando zero a esquerda para alinhamento
                }
                obj1.setMesCompetencia(mesStr);
                String anoStr = rs.getString("anoCompetencia");
                anoStr = anoStr.substring(0, anoStr.indexOf(".")); // removendo ponto do mes gerado pelo EXTRACT do sql
                obj1.setAnoCompetencia(anoStr);
                obj1.setNrTitulos(rs.getInt("nrTitulos"));
                obj1.setTotalValorBase(rs.getDouble("totalValorBase"));
                obj1.setTotalDescontoInstitucional(rs.getDouble("totalDescontoInstitucional"));
                obj1.setTotalValorBaseDeduzidoDescontoInstitucional(rs.getDouble("totalValorBaseDeduzidoDescontoInstitucional"));
                obj1.setTotalJurosMultasAcrescimos(rs.getDouble("totalJurosMultasAcrescimos"));
                obj1.setTotalDescontoProgressivo(rs.getDouble("totalDescontoProgressivo"));
                obj1.setTotalDescontoConvenio(rs.getDouble("totalDescontoConvenio"));
                obj1.setTotalValorCusteadoConvenio(rs.getDouble("totalValorCusteadoConvenio"));
                obj1.setTotalDescontoRecebimento(rs.getDouble("totalDescontoRecebimento"));
                obj1.setTotalValorRecebidoAteDataBaseFaturamento(rs.getDouble("totalValorRecebidoAteDataBaseFaturamento"));
                obj1.setTotalValorAReceberDataBaseFaturamento(rs.getDouble("totalValorAReceberDataBaseFaturamento"));
                obj1.setTotalValorRenegociadoAteDataBaseFaturamento(rs.getDouble("totalValorRenegociadoAteDataBaseFaturamento"));
                obj1.setTotalValorRenegociadoAposDataBaseFaturamento(rs.getDouble("totalValorRenegociadoAposDataBaseFaturamento"));
                listaRetorno.add(obj1);
            }
            Iterator i = listaRetorno.iterator();
            while (i.hasNext()) {
            	RecebimentoPorCursoCentroReceitaRelVO rel = (RecebimentoPorCursoCentroReceitaRelVO)i.next();
            	if (map.containsKey(rel.getCurso().getCodigo())) {
            		rel.setListaQuadroResumo((List<RecebimentoPorCursoCentroReceitaRelVO>)map.get(rel.getCurso().getCodigo()));
            	}
            }
            return listaRetorno;
	}


	public List<RecebimentoPorCursoCentroReceitaRelVO> consultarQuadroResumoCurso(
            List<UnidadeEnsinoVO> listaUnidadeEnsino, Integer curso, Integer turma, Integer turno,
            FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String tipoPessoa, Integer codigoPessoa,
            Integer codigoParceiro, Date dataInicio, Date dataFim, Integer codigoContaCorrente, String tipoOrdenacao,
            String parcela, UsuarioVO usuarioLogado, Integer centroReceita, Integer planoFinanceiroCurso, Integer condicaoPagamentoPlanoFinanceiroCurso,
            Integer fornecedor, String layout, Boolean controlarDataBaseFaturamento, Date dataBaseFaturamento, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean apresentarQuadroResumoCalouroVeterano, boolean considerarUnidadeEnsinoFinanceira, AgenteNegativacaoCobrancaContaReceberVO agente, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, String filtrarPeriodoPor, MatriculaVO matriculaVO) throws Exception {
        Date dataBaseFaturamentoAplicar = dataFim;
        if (controlarDataBaseFaturamento) {
            dataBaseFaturamentoAplicar = dataBaseFaturamento;
        }
        StringBuilder selectStr = new StringBuilder(" SELECT");
        selectStr.append(" fp.nome as \"formapagamento\",");
        selectStr.append(" curso.codigo as \"curso.codigo\",");
        selectStr.append(" curso.nome as \"curso.nome\",");
        if (layout.equals("faturamentoSinteticoCursoCentroReceita")) {
            selectStr.append(" contaReceber.tipoOrigem as \"tipoOrigem\",");
        }
        selectStr.append(" sum(contareceber.valorBaseContaReceber::NUMERIC(20,2)) as \"totalValorBase\", ");
        selectStr.append(" sum(descontoinstituicao::NUMERIC(20,2)) as \"totalDescontoInstitucional\",");
        selectStr.append(" sum((contareceber.valorBaseContaReceber - descontoinstituicao)::NUMERIC(20,2)) as \"totalValorBaseDeduzidoDescontoInstitucional\",");
        selectStr.append(" sum((contareceber.multa + contareceber.juro + contareceber.acrescimo)::NUMERIC(20,2)) as \"totalJurosMultasAcrescimos\",");
        selectStr.append(" sum((contareceber.valordescontoprogressivo)::NUMERIC(20,2)) as \"totalDescontoProgressivo\",");
        selectStr.append(" sum((contareceber.descontoConvenio)::NUMERIC(20,2)) as \"totalDescontoConvenio\",");
        selectStr.append(" sum((contareceber.valorCalculadoDescontoLancadoRecebimento)::NUMERIC(20,2)) as \"totalDescontoRecebimento\",");
        selectStr.append(" sum((contareceber.valorCusteadoContaReceber)::NUMERIC(20,2)) as \"totalValorCusteadoConvenio\",");
        selectStr.append(" sum(( case when ((negociacaorecebimento.data::Date <= '").append(Uteis.getDataBD0000(dataBaseFaturamentoAplicar)).append("'::DATE) and (contareceber.situacao = 'RE')) ");
        selectStr.append("            then (contareceber.valorBaseContaReceber - descontoinstituicao)::NUMERIC(20,2) else 0.0 end )::NUMERIC(20,2)) as \"totalValorRecebidoAteDataBaseFaturamento\",");
        selectStr.append(" sum(( case when ((negociacaorecebimento.data::Date > '").append(Uteis.getDataBD0000(dataBaseFaturamentoAplicar)).append("'::DATE) or (contareceber.situacao = 'AR')) ");
        selectStr.append("            then (contareceber.valorBaseContaReceber - descontoinstituicao)::NUMERIC(20,2) else 0.0 end )::NUMERIC(20,2)) as \"totalValorAReceberDataBaseFaturamento\",");
        selectStr.append(" sum(( case when ((negociacaorecebimento.data::Date <= '").append(Uteis.getDataBD0000(dataBaseFaturamentoAplicar)).append("'::DATE) and (contareceber.situacao = 'NE'))");
        selectStr.append("            then (contareceber.valorBaseContaReceber - descontoinstituicao)::NUMERIC(20,2) else 0.0 end )::NUMERIC(20,2)) as \"totalValorRenegociadoAteDataBaseFaturamento\",");
        selectStr.append(" sum(( case when ((negociacaorecebimento.data::Date > '").append(Uteis.getDataBD0000(dataBaseFaturamentoAplicar)).append("'::DATE) and (contareceber.situacao = 'NE'))");
        selectStr.append("            then (contareceber.valorBaseContaReceber - descontoinstituicao)::NUMERIC(20,2) else 0.0 end )::NUMERIC(20,2)) as \"totalValorRenegociadoAposDataBaseFaturamento\"");
        selectStr.append(getSqlFrom(considerarUnidadeEnsinoFinanceira, filtrarPeriodoPor, dataInicio, dataFim, listaUnidadeEnsino));        
        selectStr.append(montarFiltrosRelatorio(parcela, listaUnidadeEnsino, curso, turma, turno, filtroRelatorioFinanceiroVO, tipoPessoa, codigoPessoa, codigoParceiro, dataInicio, dataFim, codigoContaCorrente, usuarioLogado, centroReceita, planoFinanceiroCurso, condicaoPagamentoPlanoFinanceiroCurso, fornecedor, dataInicioCompetencia, dataFimCompetencia, agente, tipoAgente, filtrarPeriodoPor, matriculaVO));
        selectStr.append(" GROUP BY fp.nome, curso.codigo, curso.nome ");
        if (layout.equals("faturamentoSinteticoCursoCentroReceita")) {
            selectStr.append(", contaReceber.tipoOrigem ");
        }
        selectStr.append(" ORDER BY curso.nome, ");
        if (layout.equals("faturamentoSinteticoCursoCentroReceita")) {
            selectStr.append(" contaReceber.tipoOrigem, ");
        }
        selectStr.append(" fp.nome ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
        List<RecebimentoPorCursoCentroReceitaRelVO> listaRetorno = new ArrayList<RecebimentoPorCursoCentroReceitaRelVO>(0);
        while (rs.next()) {
            RecebimentoPorCursoCentroReceitaRelVO obj1 = new RecebimentoPorCursoCentroReceitaRelVO();
            obj1.setFormaPagamento(rs.getString("formaPagamento"));
            obj1.getCurso().setCodigo(rs.getInt("curso.codigo"));
            obj1.getCurso().setNome(rs.getString("curso.nome"));
            obj1.setTotalValorBase(rs.getDouble("totalValorBase"));
            obj1.setTotalDescontoInstitucional(rs.getDouble("totalDescontoInstitucional"));
            obj1.setTotalValorBaseDeduzidoDescontoInstitucional(rs.getDouble("totalValorBaseDeduzidoDescontoInstitucional"));
            obj1.setTotalJurosMultasAcrescimos(rs.getDouble("totalJurosMultasAcrescimos"));
            obj1.setTotalDescontoProgressivo(rs.getDouble("totalDescontoProgressivo"));
            obj1.setTotalDescontoConvenio(rs.getDouble("totalDescontoConvenio"));
            obj1.setTotalValorCusteadoConvenio(rs.getDouble("totalValorCusteadoConvenio"));
            obj1.setTotalDescontoRecebimento(rs.getDouble("totalDescontoRecebimento"));
            obj1.setTotalValorRecebidoAteDataBaseFaturamento(rs.getDouble("totalValorRecebidoAteDataBaseFaturamento"));
            obj1.setTotalValorAReceberDataBaseFaturamento(rs.getDouble("totalValorAReceberDataBaseFaturamento"));
            obj1.setTotalValorRenegociadoAteDataBaseFaturamento(rs.getDouble("totalValorRenegociadoAteDataBaseFaturamento"));
            obj1.setTotalValorRenegociadoAposDataBaseFaturamento(rs.getDouble("totalValorRenegociadoAposDataBaseFaturamento"));
            listaRetorno.add(obj1);
        }
        return listaRetorno;
	}

	@Override
	public List<FormaPagamentoNegociacaoRecebimentoVO> criarRelatorioSinteticoPorFormaRecebimento(List<UnidadeEnsinoVO> listaUnidadeEnsino, Integer curso, Integer turma, Integer turno, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String tipoPessoa, Integer codigoPessoa, Integer codigoParceiro, Date dataInicio, Date dataFim, Integer codigoContaCorrente, String tipoOrdenacao, String parcela, UsuarioVO usuarioLogado, Integer centroReceita, Integer planoFinanceiroCurso, Integer condicaoPagamentoPlanoFinanceiroCurso, Integer fornecedor, String layout, Date dataInicioCompetencia, Date dataFimCompetencia, Boolean apresentarQuadroResumoCalouroVeterano, boolean considerarUnidadeEnsinoFinanceira, AgenteNegativacaoCobrancaContaReceberVO agente, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, String filtrarPeriodoPor, MatriculaVO matriculaVO) throws Exception {
		StringBuilder selectStr = new StringBuilder("SELECT sum(contareceber_valorrecebido) as contareceber_valorrecebido, forma_pagamento, forma_tipo  from (");
		selectStr.append(getSqlConsultaBase(listaUnidadeEnsino, curso, turma, turno, filtroRelatorioFinanceiroVO, tipoPessoa, codigoPessoa, codigoParceiro, dataInicio, dataFim, codigoContaCorrente, tipoOrdenacao, parcela, usuarioLogado, centroReceita, planoFinanceiroCurso, condicaoPagamentoPlanoFinanceiroCurso, fornecedor, layout, dataInicioCompetencia, dataFimCompetencia, apresentarQuadroResumoCalouroVeterano, considerarUnidadeEnsinoFinanceira, agente, tipoAgente, filtrarPeriodoPor, matriculaVO));
		selectStr.append(" ) as t group by forma_pagamento, forma_tipo");
		selectStr.append(" ORDER BY forma_pagamento");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
		List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs = new ArrayList<FormaPagamentoNegociacaoRecebimentoVO>(0);
		while (rs.next()) {
			FormaPagamentoNegociacaoRecebimentoVO obj1 = new FormaPagamentoNegociacaoRecebimentoVO();
			obj1.getFormaPagamento().setNome(rs.getString("forma_pagamento"));
			obj1.getFormaPagamento().setTipo(rs.getString("forma_tipo"));
			obj1.setValorRecebimento(rs.getDouble("contareceber_valorrecebido"));
			formaPagamentoNegociacaoRecebimentoVOs.add(obj1);
		}
		return formaPagamentoNegociacaoRecebimentoVOs;
	}

	private String montarFiltrosRelatorio(String parcela, List<UnidadeEnsinoVO> listaUnidadeEnsino, Integer curso, Integer turma, Integer turno, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String tipoPessoa, Integer codigoPessoa, Integer codigoParceiro, Date dataInicio, Date dataFim, Integer codigoContaCorrente, UsuarioVO usuarioLogado, Integer centroReceita, Integer planoFinanceiroCurso, Integer condicaoPagamentoPlanoFinanceiroCurso, Integer fornecedor, Date dataInicioCompetencia, Date dataFimCompetencia, AgenteNegativacaoCobrancaContaReceberVO agente, TipoAgenteNegativacaoCobrancaContaReceberEnum tipoAgente, String filtrarPeriodoPor, MatriculaVO matriculaVO) throws Exception {
		StringBuilder selectStr = new StringBuilder(" where ");
		selectStr.append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "contaReceber"));
        //selectStr.append(" AND ").append("(contaReceber.situacao != '").append(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor()).append("')");
		if ((tipoPessoa != null) && (!tipoPessoa.equals(""))) {
			if (tipoPessoa.equals("aluno")) {
				tipoPessoa = "AL";
			} else if (tipoPessoa.equals("funcionario")) {
				tipoPessoa = "FU";
			} else if (tipoPessoa.equals("candidato")) {
				tipoPessoa = "CA";
			} else if (tipoPessoa.equals("parceiro")) {
				tipoPessoa = "PA";
			} else if (tipoPessoa.equals("responsavelFinanceiro")) {
				tipoPessoa = "RF";
			} else if (tipoPessoa.equals("fornecedor")) {
				tipoPessoa = "FO";
			}
			if (tipoPessoa.equals(TipoPessoa.ALUNO.getValor())) {
				selectStr.append(" AND ").append("( negociacaorecebimento.tipoPessoa in('").append(TipoPessoa.ALUNO.getValor()).append("', '").append(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()).append("' ))");
			} else {
				selectStr.append(" AND ").append("( negociacaorecebimento.tipoPessoa = '").append(tipoPessoa).append("')");
			}

		}
		if ((tipoPessoa.equals(TipoPessoa.PARCEIRO.getValor()) || tipoPessoa.equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor())) && Uteis.isAtributoPreenchido(matriculaVO.getMatricula()) && Uteis.isAtributoPreenchido(matriculaVO.getAluno())){
			selectStr.append(" AND ").append("(matricula.matricula = '").append(matriculaVO.getMatricula()).append("') ");
		}
		if (!tipoPessoa.equals(TipoPessoa.PARCEIRO.getValor()) && !tipoPessoa.equals(TipoPessoa.FORNECEDOR.getValor()) && codigoPessoa.intValue() != 0) {
			if (tipoPessoa.equals(TipoPessoa.ALUNO.getValor())) {
				selectStr.append(" AND ").append("(contaReceber.pessoa = '").append(codigoPessoa.intValue()).append("')");
			} else {
				selectStr.append(" AND ").append("(negociacaorecebimento.pessoa = '").append(codigoPessoa.intValue()).append("')");
			}

		}
		if (tipoPessoa.equals(TipoPessoa.PARCEIRO.getValor()) && codigoParceiro.intValue() != 0) {
			selectStr.append(" AND ").append("( negociacaorecebimento.parceiro = '").append(codigoParceiro.intValue()).append("')");

		}
		if (tipoPessoa.equals(TipoPessoa.FORNECEDOR.getValor()) && fornecedor.intValue() != 0) {
			selectStr.append(" AND ").append("( negociacaorecebimento.fornecedor = '").append(fornecedor.intValue()).append("')");
		}
		if (codigoContaCorrente.intValue() != 0) {
			selectStr.append(" AND ").append("( fpnr.contacorrente = '").append(codigoContaCorrente.intValue()).append("')");
			selectStr.append(" and (fp.tipo <> 'IS') ");
		}
		if(filtrarPeriodoPor == null || filtrarPeriodoPor.trim().isEmpty() || filtrarPeriodoPor.equals("DATA_RECEBIMENTO")) {
			selectStr.append(" AND ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "negociacaorecebimento.data", false));			
		}else if(filtrarPeriodoPor.equals("DATA_VENCIMENTO")) {
			selectStr.append(" AND ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "contareceber.dataVencimento", false));			
		}else if(filtrarPeriodoPor.equals("DATA_COMPENSACAO")) {			
			selectStr.append(" AND ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "formapagamentocompensado.datacompensacao", false));	
		}

		if (dataInicioCompetencia != null) {
			selectStr.append(" AND ").append("( contareceber.datacompetencia::Date >= '").append(Uteis.getDataBD0000(dataInicioCompetencia)).append("'::DATE)");

		}
		if (dataFimCompetencia != null) {
			selectStr.append(" AND ").append("( contareceber.datacompetencia::Date <= '").append(Uteis.getDataBD2359(dataFimCompetencia)).append("'::DATE)");

		}
		if (usuarioLogado != null && usuarioLogado.getUnidadeEnsinoLogado() != null && usuarioLogado.getUnidadeEnsinoLogado().getCodigo() != null && usuarioLogado.getUnidadeEnsinoLogado().getCodigo().intValue() > 0) {
			selectStr.append(" AND ").append("unidadeensino.codigo = ").append(usuarioLogado.getUnidadeEnsinoLogado().getCodigo()).append(" ");

		}
		if (!listaUnidadeEnsino.isEmpty()) {
			selectStr.append(" AND ").append(realizarGeracaoWhereUnidadeEnsinoSelecionada(listaUnidadeEnsino, "unidadeensino.codigo"));
		}

		if ((centroReceita != null) && (centroReceita != 0)) {
			selectStr.append(" and exists (select CentroResultadoOrigem.codigo from CentroResultadoOrigem ");
		    selectStr.append(" inner JOIN centroreceita ON (CentroResultadoOrigem.centroreceita = centroreceita.codigo) ");			
			selectStr.append(" where CentroResultadoOrigem.tipoCentroResultadoOrigem = 'CONTA_RECEBER' and CentroResultadoOrigem.codOrigem = contareceber.codigo::varchar and centroreceita.codigo = ").append(centroReceita).append(") ");
		}

		if ((planoFinanceiroCurso != null) && (planoFinanceiroCurso != 0)) {
			selectStr.append(" AND ").append("matriculaPeriodo.planofinanceirocurso = ").append(planoFinanceiroCurso).append(" ");
		}

		if ((condicaoPagamentoPlanoFinanceiroCurso != null) && (condicaoPagamentoPlanoFinanceiroCurso != 0)) {
			selectStr.append(" AND ").append("matriculaPeriodo.condicaopagamentoplanofinanceirocurso = ").append(condicaoPagamentoPlanoFinanceiroCurso).append(" ");
		}

		if (!parcela.equals("")) {
			selectStr.append(" AND ").append("contareceber.parcela = '").append(parcela).append("' ");
		}
		if (curso != 0) {
			selectStr.append(" AND ").append("curso.codigo = ").append(curso).append(" ");

		}
		if (turma != 0) {
			selectStr.append(" AND ").append("turma.codigo = ").append(turma).append(" ");

		}
		if (turno != 0) {
			selectStr.append(" AND ").append("turma.turno = ").append(turno).append(" ");

		}
		if (Uteis.isAtributoPreenchido(agente.getCodigo())) {
			selectStr.append(" AND ").append("regNeg.agente = ").append(agente.getCodigo()).append(" ");
			selectStr.append(" AND ").append("regNeg.tipoAgente = '").append(tipoAgente).append("' ");

		}
		return selectStr.toString();
	}

	public static String getDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}

	public static String getDesignIReportRelatorioPorCentroReceita() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "RecebimentoPorCentroReceitaRel.jrxml");
	}

	public static String getDesignIReportRelatorioFaturamentoPorCursoTipoOrigem() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "RecebimentoFaturamentoPorCursoTipoOrigemRel.jrxml");
	}

    public static String getDesignIReportRelatorioFaturamentoPorCurso() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "RecebimentoFaturamentoPorCursoRel.jrxml");
	}

	public static String getDesignIReportRelatorioSinteticoPorFormaPagamento() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "RecebimentoSinteticoPorFormaPagamentoRel.jrxml");
	}

	public static String getDesignIReportRelatorioPorCentroReceitaExcel() {

		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "RecebimentoPorCentroReceitaExcelRel.jrxml");
	}

	public static String getDesignIReportRelatorioDiaDia() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "RecebimentoDiaDiaRel.jrxml");
	}
	public static String getDesignIReportRelatorioDiaDiaCompensacao() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "RecebimentoDiaDiaCompensacaoRel.jrxml");
	}

	public static String getDesignIReportRelatorioExcel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeExcel() + ".jrxml");
	}

	public static String getCaminhoBaseDesignIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return ("Recebimento2Rel");
	}

	public static String getIdEntidadeExcel() {
		return ("Recebimento2ExcelRel");
	}
	
	private StringBuilder  getSqlFrom(Boolean considerarUnidadeEnsinoFinanceira, String filtrarPeriodoPor, Date dataInicio, Date dataTermino, List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		StringBuilder selectStr = new StringBuilder("");
	selectStr.append("FROM contareceber ");
	 if(considerarUnidadeEnsinoFinanceira){
		selectStr.append("inner JOIN unidadeensino ON (contareceber.unidadeensinofinanceira = unidadeensino.codigo) ");
	}else{
		selectStr.append("inner JOIN unidadeensino ON (contareceber.unidadeensino = unidadeensino.codigo) ");
	}			
	selectStr.append("INNER JOIN contareceberrecebimento as crr ON (contareceber.codigo = crr.contareceber) ");
	selectStr.append("INNER JOIN negociacaorecebimento negociacaorecebimento ON  (negociacaorecebimento.codigo = crr.negociacaorecebimento) ");	
	selectStr.append("INNER JOIN formapagamentonegociacaorecebimento fpnr ON  negociacaorecebimento.codigo = fpnr.negociacaorecebimento and fpnr.codigo = crr.formapagamentonegociacaorecebimento ");
	selectStr.append("INNER join formapagamento fp ON crr.formapagamento = fp.codigo ");
	if(filtrarPeriodoPor.equals("DATA_COMPENSACAO") && (dataInicio != null || dataTermino != null)) {
		selectStr.append(" inner join ( ");
		selectStr.append(" select negociacaorecebimento, formapagamentonegociacaorecebimento, datacompensacao, sum(valorcompensado) as valorcompensado from (");
		selectStr.append(" select nr.codigo as negociacaorecebimento, formapagamentonegociacaorecebimento.codigo as formapagamentonegociacaorecebimento,  ");
		selectStr.append(" formapagamentonegociacaorecebimento.valorrecebimento as valorcompensado,");
		selectStr.append(" case when cheque.pago = true then cheque.databaixa else cheque.dataprevisao end as datacompensacao");
		selectStr.append(" from cheque");
		selectStr.append(" inner join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.cheque = cheque.codigo");
		selectStr.append(" inner join negociacaorecebimento nr on formapagamentonegociacaorecebimento.negociacaorecebimento = nr.codigo");
		selectStr.append(" where ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "(case when cheque.pago = true then cheque.databaixa else cheque.dataprevisao end)", false));
//		selectStr.append(" and ").append(realizarGeracaoWhereUnidadeEnsinoSelecionada(unidadeEnsinoVOs, "nr.unidadeensino"));
		selectStr.append(" union all");
		selectStr.append(" select nr.codigo as negociacaorecebimento, formapagamentonegociacaorecebimento.codigo,");
		selectStr.append(" fpnrcc.valorparcela as valorcompensado, case when fpnrcc.situacao = 'RE'  and fpnrcc.datarecebimento is not null then  fpnrcc.datarecebimento else fpnrcc.datavencimento end as datacompensacao ");
		selectStr.append(" from formapagamentonegociacaorecebimentocartaocredito as fpnrcc");
		selectStr.append(" inner join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.codigo  = fpnrcc.formapagamentonegociacaorecebimento");
		selectStr.append(" inner join negociacaorecebimento nr on formapagamentonegociacaorecebimento.negociacaorecebimento = nr.codigo");
		selectStr.append(" where fpnrcc.formapagamentonegociacaorecebimento is not null and formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito is null ");
		selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "(case when fpnrcc.situacao = 'RE' and fpnrcc.datarecebimento is not null then  fpnrcc.datarecebimento else fpnrcc.datavencimento end)", false));
//		selectStr.append(" and ").append(realizarGeracaoWhereUnidadeEnsinoSelecionada(unidadeEnsinoVOs, "nr.unidadeensino"));
		selectStr.append(" union all ");
		selectStr.append(" select nr.codigo as negociacaorecebimento, formapagamentonegociacaorecebimento.codigo,");
		selectStr.append(" fpnrcc.valorparcela as valorcompensado, case when fpnrcc.situacao = 'RE'  and fpnrcc.datarecebimento is not null then  fpnrcc.datarecebimento else fpnrcc.datavencimento end as datacompensacao ");
		selectStr.append(" from formapagamentonegociacaorecebimentocartaocredito as fpnrcc");
		selectStr.append(" inner join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito  = fpnrcc.codigo");
		selectStr.append(" inner join negociacaorecebimento nr on formapagamentonegociacaorecebimento.negociacaorecebimento = nr.codigo");
		selectStr.append(" where formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito is not null ");
		selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "(case when fpnrcc.situacao = 'RE' and fpnrcc.datarecebimento is not null then  fpnrcc.datarecebimento else fpnrcc.datavencimento end)", false));
//		
		selectStr.append(" union all");
		selectStr.append(" select formapagamentonegociacaorecebimento.negociacaorecebimento as negociacaorecebimento, formapagamentonegociacaorecebimento.codigo as formapagamentonegociacaorecebimento,");
		selectStr.append(" formapagamentonegociacaorecebimento.valorrecebimento as valorcompensado, formapagamentonegociacaorecebimento.datacredito as datacompensacao");
		selectStr.append(" from formapagamentonegociacaorecebimento");
		selectStr.append(" inner join formapagamento on formapagamento.codigo = formapagamentonegociacaorecebimento.formapagamento");
		selectStr.append(" left join negociacaorecebimento nr on formapagamentonegociacaorecebimento.negociacaorecebimento = nr.codigo ");
		selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, " nr.data", false));
		selectStr.append(" where formapagamento.tipo = 'CD' ");		
		selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "(case when formapagamentonegociacaorecebimento.datacredito is not null then formapagamentonegociacaorecebimento.datacredito else nr.data end)", false));
//				
		selectStr.append(" union all");
		selectStr.append(" select formapagamentonegociacaorecebimento.negociacaorecebimento as negociacaorecebimento, formapagamentonegociacaorecebimento.codigo as formapagamentonegociacaorecebimento,");
		selectStr.append(" formapagamentonegociacaorecebimento.valorrecebimento as valorcompensado, formapagamentonegociacaorecebimento.datacredito as datacompensacao");
		selectStr.append(" from formapagamentonegociacaorecebimento");
		selectStr.append(" inner join formapagamento on formapagamento.codigo = formapagamentonegociacaorecebimento.formapagamento");
		selectStr.append(" left join negociacaorecebimento nr on formapagamentonegociacaorecebimento.negociacaorecebimento = nr.codigo ");
		selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, " nr.data", false));
		selectStr.append(" where formapagamento.tipo = 'BO' ");		
		selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "(case when formapagamentonegociacaorecebimento.datacredito is not null then formapagamentonegociacaorecebimento.datacredito else nr.data end)", false));
		selectStr.append(" union all");
		selectStr.append(" select formapagamentonegociacaorecebimento.negociacaorecebimento as negociacaorecebimento, formapagamentonegociacaorecebimento.codigo as formapagamentonegociacaorecebimento,");
		selectStr.append(" formapagamentonegociacaorecebimento.valorrecebimento as valorcompensado, formapagamentonegociacaorecebimento.datacredito as datacompensacao");
		selectStr.append(" from formapagamentonegociacaorecebimento");
		selectStr.append(" inner join formapagamento on formapagamento.codigo = formapagamentonegociacaorecebimento.formapagamento");
		selectStr.append(" left join negociacaorecebimento nr on formapagamentonegociacaorecebimento.negociacaorecebimento = nr.codigo ");
		selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, " nr.data", false));
		selectStr.append(" where (formapagamento.tipo not in ('CA', 'CH', 'CD', 'BO') ");
		selectStr.append(" or (formapagamento.tipo = 'CH' and formapagamentonegociacaorecebimento.cheque is null) ");
		selectStr.append(" or (formapagamento.tipo = 'CA' and formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito is null and not exists (select fpnrcc.codigo from formapagamentonegociacaorecebimentocartaocredito as fpnrcc where fpnrcc.formapagamentonegociacaorecebimento = formapagamentonegociacaorecebimento.codigo limit 1 ) )) ");
		selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "(case when formapagamentonegociacaorecebimento.datacredito is not null then formapagamentonegociacaorecebimento.datacredito else nr.data end)", false));
		selectStr.append(" )as t group by negociacaorecebimento, formapagamentonegociacaorecebimento, datacompensacao) as formapagamentocompensado on formapagamentocompensado.negociacaorecebimento = negociacaorecebimento.codigo and formapagamentocompensado.formapagamentonegociacaorecebimento = fpnr.codigo ");		
	}
	selectStr.append("LEFT JOIN matricula ON (matricula.matricula = contareceber.matriculaaluno) ");
	selectStr.append("LEFT JOIN matriculaperiodo on ((contareceber.matriculaperiodo is not null and contareceber.matriculaperiodo = matriculaperiodo.codigo ) ");
	selectStr.append(" or (contareceber.matriculaperiodo is null and matricula.matricula = matriculaperiodo.matricula 	and matriculaperiodo.codigo = ( ");
	selectStr.append(" select codigo		from			matriculaperiodo mp		where			mp.matricula = matricula.matricula		order by			(mp.ano || '/' || mp.semestre )desc		limit 1 ))) ");
	selectStr.append("LEFT JOIN turma on ((contareceber.turma is null and turma.codigo = matriculaperiodo.turma) or (contareceber.turma is not null and turma.codigo = contareceber.turma)) ");
	selectStr.append("LEFT JOIN usuario ON negociacaorecebimento.responsavel = usuario.codigo ");
	selectStr.append("LEFT JOIN pessoa ON (negociacaorecebimento.pessoa = pessoa.codigo) ");
	selectStr.append("LEFT JOIN pessoa p2 on (p2.codigo = matricula.aluno) ");
	selectStr.append("LEFT JOIN parceiro ON (negociacaorecebimento.parceiro = parceiro.codigo) ");
	selectStr.append("LEFT JOIN fornecedor ON (negociacaorecebimento.fornecedor = fornecedor.codigo) ");
	selectStr.append("LEFT JOIN contacorrente ON (fpnr.contacorrente = contacorrente.codigo) ");		
	selectStr.append("LEFT JOIN curso on curso.codigo = matricula.curso ");
	selectStr.append("LEFT JOIN pessoa responsavelFinanceiro on responsavelFinanceiro.codigo = contareceber.responsavelFinanceiro ");
	selectStr.append("LEFT JOIN registroNegativacaoCobrancaContaReceberItem neg on neg.contareceber = contareceber.codigo and neg.dataExclusao is null ");
	selectStr.append("LEFT JOIN registroNegativacaoCobrancaContaReceber regNeg on neg.registroNegativacaoCobrancaContaReceber = regNeg.codigo ");
	return selectStr;
	}

	private List<CentroResultadoOrigemVO> consultarDadosCentroReceita(Integer codigoConta){
		StringBuilder selectStr = new StringBuilder("");
		selectStr.append(" select CentroResultadoOrigem.valor, CentroResultadoOrigem.porcentagem, centroreceita.descricao as centroreceita_descricao, centroreceita.codigo as centroreceita_codigo, centroreceita.identificadorcentroreceita as centroreceita_identificadorcentroreceita ");
		selectStr.append(" from CentroResultadoOrigem ");
		selectStr.append(" inner JOIN centroreceita ON (CentroResultadoOrigem.centroreceita = centroreceita.codigo) ");		
		selectStr.append(" where CentroResultadoOrigem.tipoCentroResultadoOrigem = 'CONTA_RECEBER' and CentroResultadoOrigem.codOrigem = '").append(codigoConta).append("' order by centroreceita_descricao   ");
		
		List<CentroResultadoOrigemVO> centroResultadoOrigemVOs =  new ArrayList<CentroResultadoOrigemVO>(0);
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
		while(rs.next()) {
			CentroResultadoOrigemVO centroResultadoOrigemVO = new CentroResultadoOrigemVO();
			centroResultadoOrigemVO.getCentroReceitaVO().setCodigo(rs.getInt("centroreceita_codigo"));
			centroResultadoOrigemVO.getCentroReceitaVO().setDescricao(rs.getString("centroreceita_descricao"));
			centroResultadoOrigemVO.getCentroReceitaVO().setIdentificadorCentroReceita(rs.getString("centroreceita_identificadorcentroreceita"));
			centroResultadoOrigemVO.setValor(rs.getDouble("valor"));
			centroResultadoOrigemVO.setPorcentagem(rs.getDouble("porcentagem"));
			centroResultadoOrigemVOs.add(centroResultadoOrigemVO);
		}
		return centroResultadoOrigemVOs;
	}
}
