package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.negocio.comuns.financeiro.ContasRecebimentoRelVO;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.financeiro.RecebimentoPorTurmaRelVO;
import relatorio.negocio.interfaces.financeiro.RecebimentoPorTurmaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
public class RecebimentoPorTurmaRel extends SuperRelatorio implements RecebimentoPorTurmaRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	@Override
	public List<RecebimentoPorTurmaRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, List<TipoRequerimentoVO> listaTipoReq, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, Date dataInicio, Date dataFim, TurmaVO turma, Integer ordenacao, String tipoLayout, boolean naoTrazerContasIsentas, String parcela, String filtrarPor,UsuarioVO usuarioVO) throws Exception {
		List<RecebimentoPorTurmaRelVO> recebimentoPorTurmaRelVOs = new ArrayList<RecebimentoPorTurmaRelVO>(0);
		boolean apresentarValorRecebidoComImpostosRetido = permitirApresentarValorRecebicoComImpostosRetidos(usuarioVO);
		SqlRowSet tabelaResultado = executarConsultaParametrizada(listaUnidadeEnsino, listaTipoReq, dataInicio, dataFim, turma, ordenacao, naoTrazerContasIsentas, parcela, filtroRelatorioFinanceiroVO, filtrarPor,apresentarValorRecebidoComImpostosRetido);
		Map<Integer, RecebimentoPorTurmaRelVO> hashMapCodigoConta = new HashMap<Integer, RecebimentoPorTurmaRelVO>();
		while (tabelaResultado.next()) {
			if(hashMapCodigoConta.containsKey(tabelaResultado.getInt("contareceber_codigo"))) {
				RecebimentoPorTurmaRelVO recebimentoPorTurmaRelVO = hashMapCodigoConta.get(tabelaResultado.getInt("contareceber_codigo"));
				recebimentoPorTurmaRelVO.setApresentarValorRecebidoComImpostosRetido(apresentarValorRecebidoComImpostosRetido);
				montarDadosRecebimento(recebimentoPorTurmaRelVO, tabelaResultado);
			}else {
				RecebimentoPorTurmaRelVO recebimentoPorTurmaRelVO = montarDados(tabelaResultado, tipoLayout,apresentarValorRecebidoComImpostosRetido);				
				recebimentoPorTurmaRelVOs.add(recebimentoPorTurmaRelVO);
				hashMapCodigoConta.put(tabelaResultado.getInt("contareceber_codigo"), recebimentoPorTurmaRelVO);
			}
		}
		return recebimentoPorTurmaRelVOs;
	}
	
	private boolean permitirApresentarValorRecebicoComImpostosRetidos(UsuarioVO usuarioVO) {
	   	try {
    		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.APRESENTAR_VALOR_RECEBIDO_COM_IMPOSTOS_RETIDOS, usuarioVO);
    		return true;
		} catch (Exception e) {
			return false;
		}
	}
	private RecebimentoPorTurmaRelVO montarDados(SqlRowSet dadosSQL, String tipoLayout, boolean apresentarValorRecebidoComImpostosRetido) throws Exception {
		RecebimentoPorTurmaRelVO recebimentoPorTurmaRelVO = new RecebimentoPorTurmaRelVO();
		recebimentoPorTurmaRelVO.setApresentarValorRecebidoComImpostosRetido(apresentarValorRecebidoComImpostosRetido);
		Double valorDescontoAlunoJaCalculado = 0.0;
		Double descontoInstituicao = 0.0;
		Double descontoConvenio = 0.0;
		Double valorDescontoRateio = 0.0;
		Double valorDescontoProgressivo = 0.0;
		Double valorCalculadoDescontoLancadoRecebimento = 0.0;
		recebimentoPorTurmaRelVO.setDescontoChancela(dadosSQL.getBoolean("descontochancela"));
		recebimentoPorTurmaRelVO.setData(Uteis.getDataJDBC(dadosSQL.getDate("datavencimento")));
		recebimentoPorTurmaRelVO.setDesconto(dadosSQL.getDouble("desconto"));
		recebimentoPorTurmaRelVO.setDigito(dadosSQL.getString("digito"));
		recebimentoPorTurmaRelVO.setIdentificadorTurma(dadosSQL.getString("identificadorturma"));
		recebimentoPorTurmaRelVO.setNossoNumero(dadosSQL.getString("nossonumero"));
		recebimentoPorTurmaRelVO.setNomeCurso(dadosSQL.getString("nomecurso"));
		recebimentoPorTurmaRelVO.setNomeParceiro(dadosSQL.getString("nomeparceiro"));
		if (recebimentoPorTurmaRelVO.getNomeParceiro() != null && !recebimentoPorTurmaRelVO.getNomeParceiro().equals("") && dadosSQL.getString("pessoaParceiro") != null) {
			recebimentoPorTurmaRelVO.setNomeParceiro(Uteis.getNomeResumidoPessoa(dadosSQL.getString("pessoaParceiro")) + " / " + recebimentoPorTurmaRelVO.getNomeParceiro());

		}
		recebimentoPorTurmaRelVO.setNomePessoa(dadosSQL.getString("nomepessoa"));
		recebimentoPorTurmaRelVO.setNrDocumento(dadosSQL.getString("nrdocumento"));
		recebimentoPorTurmaRelVO.setNumero(dadosSQL.getString("numero"));
		recebimentoPorTurmaRelVO.setParcela(dadosSQL.getString("parcela"));
		recebimentoPorTurmaRelVO.setTipoOrigem(dadosSQL.getString("tipoorigem"));
		recebimentoPorTurmaRelVO.setTipoPessoa(dadosSQL.getString("tipopessoa"));
		recebimentoPorTurmaRelVO.setValor(dadosSQL.getDouble("valor"));
		recebimentoPorTurmaRelVO.setValordescontorecebido(dadosSQL.getDouble("valordescontorecebido"));
		recebimentoPorTurmaRelVO.setJuro(dadosSQL.getDouble("valorjurocalculado"));
		recebimentoPorTurmaRelVO.setMulta(dadosSQL.getDouble("valormultacalculado"));
		recebimentoPorTurmaRelVO.setAcrescimo(dadosSQL.getDouble("acrescimo"));
		Optional<BigDecimal> valorIndiceReajustePorAtrasoOptional = Optional.ofNullable(dadosSQL.getBigDecimal("valorIndiceReajustePorAtraso"));
		Optional<BigDecimal> valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessaOptional = Optional.ofNullable(dadosSQL.getBigDecimal("valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa"));
		recebimentoPorTurmaRelVO.setValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa(valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessaOptional.orElse(BigDecimal.ZERO).doubleValue());
		recebimentoPorTurmaRelVO.setValorIndiceReajustePorAtraso(valorIndiceReajustePorAtrasoOptional.orElse(BigDecimal.ZERO).doubleValue());		
		recebimentoPorTurmaRelVO.setDataPagamento(Uteis.getDataJDBC(dadosSQL.getDate("datapagamento")));
		recebimentoPorTurmaRelVO.setValorDescontoCalculadoPrimeiraFaixaDescontos(dadosSQL.getDouble("valorDescontoCalculadoPrimeiraFaixaDescontos"));
		if (tipoLayout.equals("RecebimentoPorTurmaRelComObservacao")) {
			recebimentoPorTurmaRelVO.setLayoutComObservacao(Boolean.TRUE);
		} else {
			recebimentoPorTurmaRelVO.setLayoutComObservacao(Boolean.FALSE);
		}
		recebimentoPorTurmaRelVO.setObservacao(dadosSQL.getString("observacao"));
		recebimentoPorTurmaRelVO.setNomeUnidadeEnsino(dadosSQL.getString("nomeUnidadeEnsino"));
		recebimentoPorTurmaRelVO.setInstituicaoChanceladora(dadosSQL.getString("instituicaoChanceladora"));
		recebimentoPorTurmaRelVO.setNomeBanco(dadosSQL.getString("nomeBanco"));
		String formaPagamento = dadosSQL.getString("formapgto");
		if (formaPagamento != null) {
			recebimentoPorTurmaRelVO.setFormaPagamento(montarFormaPagamentos(formaPagamento));
		}
		if (dadosSQL.getString("tipochancela") != null && !dadosSQL.getString("tipochancela").equals("")) {
			Double porcentagem = dadosSQL.getDouble("porcentageminstituicaochancelado");
			Double valorInst = dadosSQL.getDouble("valorinstituicaochancelado");
			Boolean valorPorAluno = dadosSQL.getBoolean("valorporaluno");
			if (porcentagem != null && porcentagem > 0) {
				recebimentoPorTurmaRelVO.setPorcentagemInstChancela(Integer.valueOf(Uteis.formatarDecimal(dadosSQL.getDouble("porcentageminstituicaochancelado"), "0")));
			}
			if (valorInst != null && valorInst > 0) {
				recebimentoPorTurmaRelVO.setValorInstChancela(dadosSQL.getDouble("valorinstituicaochancelado"));
			}
			if (valorPorAluno != null) {
				recebimentoPorTurmaRelVO.setValorPorAluno(valorPorAluno);
			}
		}
		valorDescontoAlunoJaCalculado = dadosSQL.getDouble("valorDescontoAlunoJaCalculado");
		descontoInstituicao = dadosSQL.getDouble("descontoInstituicao");
		descontoConvenio = dadosSQL.getDouble("descontoConvenio");
		valorDescontoProgressivo = dadosSQL.getDouble("valorDescontoProgressivo");
		valorCalculadoDescontoLancadoRecebimento = dadosSQL.getDouble("valorcalculadodescontolancadorecebimento");
		valorDescontoRateio = dadosSQL.getDouble("valorDescontoRateio");
		calcularDescontoRecebido(recebimentoPorTurmaRelVO, valorDescontoAlunoJaCalculado, descontoInstituicao, descontoConvenio, valorDescontoProgressivo, valorCalculadoDescontoLancadoRecebimento, valorDescontoRateio);
		montarDadosRecebimento(recebimentoPorTurmaRelVO, dadosSQL);
		return recebimentoPorTurmaRelVO;
	}
	
	public void montarDadosRecebimento(RecebimentoPorTurmaRelVO recebimentoPorTurmaRelVO, SqlRowSet dadosSQL) throws Exception {
		ContasRecebimentoRelVO contasRecebimentoRelVO = new ContasRecebimentoRelVO();
		if(dadosSQL.getString("nomeapresentacaosistema") != null && dadosSQL.getString("nomeapresentacaosistema").length() > 2) {
			contasRecebimentoRelVO.setContaCorrente(dadosSQL.getString("nomeapresentacaosistema"));
		}else {
			contasRecebimentoRelVO.setContaCorrente(dadosSQL.getString("numero") + " - " + dadosSQL.getString("digito"));
		}
		if(recebimentoPorTurmaRelVO.getApresentarValorRecebidoComImpostosRetido()) {
			contasRecebimentoRelVO.setValorRecebido(dadosSQL.getDouble("valorrecebidocomimposto"));
		}else {
			contasRecebimentoRelVO.setValorRecebido(dadosSQL.getDouble("valorrecebido"));
		}
		contasRecebimentoRelVO.setFormaPagamento(montarFormaPagamentos(dadosSQL.getString("formapgto")));
		contasRecebimentoRelVO.setNomeBanco(dadosSQL.getString("nomeBanco"));
		recebimentoPorTurmaRelVO.getContasRecebimentoRelVOs().add(contasRecebimentoRelVO);
		recebimentoPorTurmaRelVO.setValorRecebido(Uteis.arrendondarForcando2CadasDecimais(recebimentoPorTurmaRelVO.getValorRecebido() + dadosSQL.getDouble("valorrecebido")));
	}

	public void calcularDescontoRecebido(RecebimentoPorTurmaRelVO recebimentoPorTurmaRelVO, Double valorDescontoAlunoJaCalculado, Double descontoInstituicao, Double descontoConvenio, Double valorDescontoProgressivo, Double valorCalculadoDescontoLancadoRecebimento, Double valorDescontoRateio) {
		if (valorDescontoAlunoJaCalculado != null) {
			recebimentoPorTurmaRelVO.setDescontoRecebido(recebimentoPorTurmaRelVO.getDescontoRecebido() + valorDescontoAlunoJaCalculado);
		}
		if (descontoInstituicao != null) {
			recebimentoPorTurmaRelVO.setDescontoRecebido(recebimentoPorTurmaRelVO.getDescontoRecebido() + descontoInstituicao);
		}
		if (descontoConvenio != null) {
			recebimentoPorTurmaRelVO.setDescontoRecebido(recebimentoPorTurmaRelVO.getDescontoRecebido() + descontoConvenio);
		}
		if (valorDescontoProgressivo != null) {
			recebimentoPorTurmaRelVO.setDescontoRecebido(recebimentoPorTurmaRelVO.getDescontoRecebido() + valorDescontoProgressivo);
		}
		if (valorCalculadoDescontoLancadoRecebimento != null) {
			recebimentoPorTurmaRelVO.setDescontoRecebido(recebimentoPorTurmaRelVO.getDescontoRecebido() + valorCalculadoDescontoLancadoRecebimento);
		}
		if (valorDescontoRateio != null) {
			recebimentoPorTurmaRelVO.setDescontoRecebido(recebimentoPorTurmaRelVO.getDescontoRecebido() + valorDescontoRateio);
		}
		recebimentoPorTurmaRelVO.setDescontoRecebido(Uteis.arredondar(recebimentoPorTurmaRelVO.getDescontoRecebido(), 2, 0));
	}

	private SqlRowSet executarConsultaParametrizada(List<UnidadeEnsinoVO> listaUnidadeEnsino, List<TipoRequerimentoVO> listaTipoReq, Date dataInicio, Date dataFim, TurmaVO turma, Integer ordenacao, boolean naoTrazerContasIsentas, String parcela, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, String filtrarPor, boolean apresentarValorRecebidoComImpostosRetido) throws Exception {
		StringBuilder selectStr = new StringBuilder();
		boolean usarFuncao = true;
		if (filtrarPor.equals("dataCompensacao") && usarFuncao) {
			selectStr.append(" with formapagamentocompensado as ( ");
			selectStr.append(" select t.* from consultarCompensacaoRecebimento(null, null, '").append(Uteis.getDataJDBC(dataInicio)).append("', '").append(Uteis.getDataJDBC(dataFim)).append("'::date, null) as t");
			selectStr.append(" 	)");
		}
		selectStr.append(" SELECT matricula.descontochancela, negociacaorecebimento.tipopessoa as tipopessoa,  negociacaorecebimento.data as data, negociacaorecebimento.observacao, contareceber.tipoorigem as tipoorigem, ");
		selectStr.append(" contareceber.nrdocumento as nrdocumento, contareceber.datavencimento, contareceber.valor as valor, coalesce(contareceber.acrescimo, 0) as acrescimo, ");
		selectStr.append(" crr.valorrecebimento as valorrecebido,  ");		
		selectStr.append(" contareceber.valordesconto, contareceber.valorcalculadodescontolancadorecebimento, contareceber.descontoinstituicao, ");
		selectStr.append(" contareceber.descontoconvenio, contareceber.valordescontoprogressivo,  contareceber.valordesconto as desconto, contareceber.valorDescontoAlunoJaCalculado,  contaReceber.valorDescontoRateio,  ");
		selectStr.append(" contareceber.nossonumero, curso.nome as nomecurso, unidadeEnsino.nome as nomeUnidadeEnsino,  contareceber.parcela as parcela, contareceber.valorIndiceReajustePorAtraso as valorIndiceReajustePorAtraso, contareceber.valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa as valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa, ");
		selectStr.append(" contareceber.valorDescontoCalculadoPrimeiraFaixaDescontos,contareceber.valordescontorecebido ,  contareceber.valormultacalculado  , contareceber.valorjurocalculado , banco.nome as nomeBanco,contacorrente.numero as numero, contacorrente.digito as digito, contacorrente.nomeapresentacaosistema, ");
		selectStr.append(" pessoa.nome as nomepessoa, parceiro.nome as nomeparceiro, turma.identificadorturma as identificadorturma, ");
		selectStr.append(" negociacaorecebimento.data::Date as datapagamento, formapagamento.tipo AS formapgto, turma.tipochancela as tipochancela, crr.contareceber as contareceber_codigo, ");
		selectStr.append(" turma.valorfixochancela as valorinstituicaochancelado, turma.porcentagemchancela as porcentageminstituicaochancelado, turma.valorporaluno as valorporaluno, chancela.instituicaoChanceladora as instituicaoChanceladora, ");
		selectStr.append(" p2.nome as pessoaParceiro, formapagamentonegociacaorecebimento.codigo as fpnr_codigo, case when pessoa.nome is null then coalesce(p2.nome, '') || ' ' || coalesce(parceiro.nome, '') else pessoa.nome end as nomeOrdenacao ");
		
		if(apresentarValorRecebidoComImpostosRetido) {
			selectStr.append(", contareceber.valorrecebido + coalesce((select sum(p.valorutilizadorecebimento) from  ");
			selectStr.append(" planodescontocontareceber p inner join imposto on imposto.codigo = p.imposto where p.contareceber = contareceber.codigo), 0.0) as valorrecebidocomimposto ");
		}

		selectStr.append(" FROM contareceber ");		
		if(filtroRelatorioFinanceiroVO.getConsiderarUnidadeFinanceira()) {
			selectStr.append(" inner JOIN unidadeEnsino ON unidadeEnsino.codigo = contareceber.unidadeensinofinanceira ");
		}else {
			selectStr.append(" inner JOIN unidadeEnsino ON unidadeEnsino.codigo = contareceber.unidadeEnsino ");
		}
		selectStr.append(" inner join contareceberrecebimento crr on crr.contareceber = contareceber.codigo ");
		selectStr.append(" inner join negociacaorecebimento on	 negociacaorecebimento.codigo = crr.negociacaorecebimento ");
		selectStr.append(" INNER JOIN formapagamentonegociacaorecebimento ON (formapagamentonegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo) and formapagamentonegociacaorecebimento.codigo = crr.formapagamentonegociacaorecebimento ");
		selectStr.append(" inner JOIN contacorrente ON (formapagamentonegociacaorecebimento.contacorrente = contacorrente.codigo)   ");		
		selectStr.append(" INNER JOIN formapagamento ON (formapagamento.codigo = formapagamentonegociacaorecebimento.formapagamento)  ");
		if (filtrarPor.equals("dataCompensacao")) {
			if(usarFuncao) {
				selectStr.append(" inner join formapagamentocompensado on formapagamentocompensado.negociacaorecebimento = negociacaorecebimento.codigo and formapagamentonegociacaorecebimento.codigo = formapagamentocompensado.formapagamentonegociacaorecebimento ");
				selectStr.append(" and ").append(realizarGeracaoWherePeriodo(Uteis.obterDataAntigaPorMes(dataInicio, 40), dataFim, "negociacaorecebimento.data", false));
			}else {
			selectStr.append(" inner join ( ");
			selectStr.append(" select negociacaorecebimento, formapagamentonegociacaorecebimento, datacompensacao, sum(valorcompensado) as valorcompensado from ( ");
			selectStr.append(" select nr.codigo as negociacaorecebimento, fpnr.codigo as formapagamentonegociacaorecebimento,  fpnr.valorrecebimento as valorcompensado, ");
			selectStr.append(" (case when cheque.pago = true and cheque.databaixa is not null then cheque.databaixa else cheque.dataprevisao end) as datacompensacao ");
			selectStr.append(" from cheque ");
			selectStr.append(" inner join formapagamentonegociacaorecebimento fpnr on fpnr.cheque = cheque.codigo ");
			selectStr.append(" inner join negociacaorecebimento nr on fpnr.negociacaorecebimento = nr.codigo ");
			selectStr.append(" where ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "(case when cheque.pago = true and cheque.databaixa is not null then cheque.databaixa else cheque.dataprevisao end)", false));			
							
			selectStr.append(" union all ");
			selectStr.append(" select nr.codigo as negociacaorecebimento, fpnr.codigo as formapagamentonegociacaorecebimento,  ");
			selectStr.append(" fpnrcc.valorparcela as valorcompensado, case when fpnrcc.situacao = 'RE' and fpnrcc.datarecebimento is not null then fpnrcc.datarecebimento else fpnrcc.datavencimento end as datacompensacao ");
			selectStr.append(" from formapagamentonegociacaorecebimentocartaocredito as fpnrcc  ");
			selectStr.append(" inner join formapagamentonegociacaorecebimento fpnr on fpnr.codigo  = fpnrcc.formapagamentonegociacaorecebimento ");
			selectStr.append(" inner join negociacaorecebimento nr on fpnr.negociacaorecebimento = nr.codigo ");
			selectStr.append(" where fpnrcc.formapagamentonegociacaorecebimento is not null and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "case when fpnrcc.situacao = 'RE' and fpnrcc.datarecebimento is not null then fpnrcc.datarecebimento else fpnrcc.datavencimento end", false));
			
			selectStr.append(" union all ");
			selectStr.append(" select nr.codigo as negociacaorecebimento, fpnr.codigo as formapagamentonegociacaorecebimento,  ");
			selectStr.append(" fpnrcc.valorparcela as valorcompensado, case when fpnrcc.situacao = 'RE' and fpnrcc.datarecebimento is not null then fpnrcc.datarecebimento else fpnrcc.datavencimento end as datacompensacao ");
			selectStr.append(" from formapagamentonegociacaorecebimentocartaocredito as fpnrcc  ");
			selectStr.append(" inner join formapagamentonegociacaorecebimento fpnr on fpnr.formapagamentonegociacaorecebimentocartaocredito  = fpnrcc.codigo ");
			selectStr.append(" inner join negociacaorecebimento nr on fpnr.negociacaorecebimento = nr.codigo ");
			selectStr.append(" where fpnr.formapagamentonegociacaorecebimentocartaocredito is not null and fpnrcc.formapagamentonegociacaorecebimento is null and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "case when fpnrcc.situacao = 'RE' and fpnrcc.datarecebimento is not null then fpnrcc.datarecebimento else fpnrcc.datavencimento end", false));
			
			selectStr.append(" union all ");
			selectStr.append(" select fpnr.negociacaorecebimento as negociacaorecebimento, fpnr.codigo as formapagamentonegociacaorecebimento,  ");
			selectStr.append(" fpnr.valorrecebimento as valorcompensado, case when fpnr.datacredito is not null then fpnr.datacredito else nr.data end as datacompensacao  ");
			selectStr.append(" from formapagamentonegociacaorecebimento fpnr ");
			selectStr.append(" inner join formapagamento fp on fp.codigo = fpnr.formapagamento ");
			selectStr.append(" left join negociacaorecebimento nr on fpnr.negociacaorecebimento = nr.codigo ");
			selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, " nr.data", false));
			selectStr.append(" where fp.tipo = 'CD'  ").append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "case when fpnr.datacredito is not null then fpnr.datacredito else nr.data end ", false));			
					
			selectStr.append(" union all ");
			selectStr.append(" select fpnr.negociacaorecebimento as negociacaorecebimento, fpnr.codigo as formapagamentonegociacaorecebimento,  ");
			selectStr.append(" fpnr.valorrecebimento as valorcompensado, case when fpnr.datacredito is not null then fpnr.datacredito else nr.data end as datacompensacao  ");
			selectStr.append(" from formapagamentonegociacaorecebimento fpnr ");
			selectStr.append(" inner join formapagamento fp on fp.codigo = fpnr.formapagamento ");
			selectStr.append(" left join negociacaorecebimento nr on fpnr.negociacaorecebimento = nr.codigo ");
			selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, " nr.data", false));
			selectStr.append(" where fp.tipo = 'BO' ").append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, " case when fpnr.datacredito is not null then fpnr.datacredito else nr.data end ", false));
				
			selectStr.append(" union all ");
			selectStr.append(" select fpnr.negociacaorecebimento as negociacaorecebimento, fpnr.codigo as formapagamentonegociacaorecebimento, fpnr.valorrecebimento as valorcompensado,  case when fpnr.datacredito is not null then fpnr.datacredito else nr.data end as datacompensacao ");
			selectStr.append(" from formapagamentonegociacaorecebimento fpnr ");
			selectStr.append(" inner join formapagamento fp on fp.codigo = fpnr.formapagamento ");
			selectStr.append(" left join negociacaorecebimento nr on fpnr.negociacaorecebimento = nr.codigo ");
			selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, " nr.data", false));
			selectStr.append(" where (fp.tipo not in ('CD', 'CA', 'CH', 'BO')  ");
			selectStr.append(" or (fp.tipo = 'CH' and fpnr.cheque is null) ");
			selectStr.append(" or (fp.tipo = 'CA' and fpnr.formapagamentonegociacaorecebimentocartaocredito is null and not exists (select fpnrcc.codigo from formapagamentonegociacaorecebimentocartaocredito as fpnrcc where fpnrcc.formapagamentonegociacaorecebimento = fpnr.codigo limit 1 ) )) ");
			selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, " case when fpnr.datacredito is not null then fpnr.datacredito else nr.data end ", false));
								
			selectStr.append(" ) as t group by formapagamentonegociacaorecebimento, negociacaorecebimento, datacompensacao ");
			selectStr.append(" ) as formapagamentocompensado on formapagamentocompensado.negociacaorecebimento = negociacaorecebimento.codigo and formapagamentocompensado.formapagamentonegociacaorecebimento = formapagamentonegociacaorecebimento.codigo ");
		}			
		}			
		selectStr.append(" LEFT JOIN matriculaperiodo on matriculaperiodo.codigo = contareceber.matriculaperiodo ");
		selectStr.append(" LEFT JOIN turma ON ((contareceber.turma is null and  matriculaperiodo.turma = turma.codigo) or (contareceber.turma is not null and contareceber.turma = turma.codigo)) ");
		selectStr.append(" LEFT JOIN matricula ON contareceber.matriculaaluno = matricula.matricula  ");
		selectStr.append(" LEFT JOIN curso ON matricula.curso = curso.codigo  ");
		selectStr.append(" LEFT JOIN parceiro ON (negociacaorecebimento.parceiro = parceiro.codigo)  ");		
		selectStr.append(" LEFT JOIN chancela ON turma.chancela = chancela.codigo  ");
		selectStr.append(" LEFT JOIN pessoa ON (negociacaorecebimento.pessoa = pessoa.codigo)  ");
		selectStr.append(" LEFT JOIN pessoa p2 ON p2.codigo = matricula.aluno   ");
		selectStr.append(" LEFT JOIN agencia ON (contacorrente.agencia = agencia.codigo)   ");
		selectStr.append(" LEFT JOIN banco ON (agencia.banco = banco.codigo)   ");
		selectStr.append(" where ");
		selectStr.append(adicionarFiltroTipoOrigemContaReceber(filtroRelatorioFinanceiroVO, "contaReceber"));		
		if (filtroRelatorioFinanceiroVO.getTipoOrigemRequerimento() && Uteis.isAtributoPreenchido(listaTipoReq) && listaTipoReq.stream().anyMatch(TipoRequerimentoVO::getFiltrarTipoReq)) {
			selectStr.append(" and case when contareceber.tipoOrigem = 'REQ' then contareceber.codorigem::integer in (");	
			selectStr.append(" select requerimento.codigo from requerimento inner join tiporequerimento on tiporequerimento.codigo = requerimento.tiporequerimento where tiporequerimento.codigo in (0");
			for (TipoRequerimentoVO tipoReq : listaTipoReq) {
				if (tipoReq.getFiltrarTipoReq()) {
					selectStr.append(", " + tipoReq.getCodigo());
				}
			}
			selectStr.append(" )) else 1=1 end ");	
		}
		if (turma.getCodigo() != null && turma.getCodigo() != 0) {
			selectStr.append(" and ( turma.codigo = ");
			selectStr.append(turma.getCodigo());
			selectStr.append(")");
		}
		if (!listaUnidadeEnsino.isEmpty()) {				
			selectStr.append(" and ").append(realizarGeracaoWhereUnidadeEnsinoSelecionada(listaUnidadeEnsino, "unidadeEnsino.codigo"));			
		}		
		if (filtrarPor.equals("dataVencimento")) {
			selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "contareceber.datavencimento", false));			
		} else if (filtrarPor.equals("dataRecebimento")) {
			selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "negociacaorecebimento.data", false));				
		} else if (filtrarPor.equals("dataCompetencia")) {
			selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "contareceber.datacompetencia", false));			
		}else {
			selectStr.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataFim, "formapagamentocompensado.datacompensacao", false));			
		}
		if (naoTrazerContasIsentas) {
			selectStr.append(" and crr.valorrecebimento > 0 ");
		}
		if (!parcela.equals("")) {
			selectStr.append(" AND ( contareceber.parcela = '").append(parcela).append("' )");
		}
		selectStr.append(" GROUP BY nomecurso,  identificadorturma, nomepessoa,  nomeparceiro, negociacaorecebimento.tipopessoa, negociacaorecebimento.data, negociacaorecebimento.observacao, tipoorigem, contareceber.nrdocumento, valor, ");
		selectStr.append(" desconto,  parcela, valorIndiceReajustePorAtraso, valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa, valorDescontoCalculadoPrimeiraFaixaDescontos,contareceber.valordescontorecebido ,  contareceber.valormultacalculado  , contareceber.valorjurocalculado ,banco.nome , contacorrente.numero, contacorrente.digito, datapagamento, formapgto, nossonumero, contareceber.datavencimento, nomeUnidadeEnsino, contareceber_codigo, ");		
		selectStr.append(" contareceber.valordesconto, contareceber.valorDescontoAlunoJaCalculado, contareceber.valorcalculadodescontolancadorecebimento, contareceber.descontoinstituicao, contareceber.descontoconvenio, contareceber.valordescontoprogressivo, contareceber.valorDescontoRateio, ");
		selectStr.append(" turma.tipochancela, turma.valorfixochancela, turma.porcentagemchancela, turma.valorporaluno, matricula.descontochancela, chancela.instituicaoChanceladora, ");
		selectStr.append(" crr.valorrecebimento, pessoaParceiro, formapagamentonegociacaorecebimento.codigo, contacorrente.nomeapresentacaosistema, contareceber.acrescimo,contareceber.valorTaxaRepasseCartao ");
		if(apresentarValorRecebidoComImpostosRetido) {
			selectStr.append(", contareceber.valorrecebido, contareceber.codigo  ");
		}
		// Ordenação
		selectStr.append(" ORDER BY nomeUnidadeEnsino, nomecurso, identificadorturma, ");
		if (ordenacao == 1) {
			selectStr.append(" negociacaorecebimento.data ");
		} else {
			selectStr.append(" nomeOrdenacao, nomeparceiro, contareceber.datavencimento ");
		}
		selectStr.append(" , contareceber_codigo ");
		//System.out.println(selectStr.toString());
		return getConexao().getJdbcTemplate().queryForRowSet(selectStr.toString());
	}

	private String montarFormaPagamentos(String enumFormaPgto) {
		if (enumFormaPgto.equals(TipoFormaPagamento.BOLETO_BANCARIO.getValor())) {
			return "Boleto";
		}
		if (enumFormaPgto.equals(TipoFormaPagamento.CHEQUE.getValor())) {
			return "Cheque";
		}
		if (enumFormaPgto.equals(TipoFormaPagamento.DINHEIRO.getValor())) {
			return "Dinheiro";
		}
		if (enumFormaPgto.equals(TipoFormaPagamento.CARTAO_DE_CREDITO.getValor())) {
			return "Cartão";
		}
		if (enumFormaPgto.equals(TipoFormaPagamento.DEBITO_EM_CONTA_CORRENTE.getValor())) {
			return "Débito";
		}
		if (enumFormaPgto.equals(TipoFormaPagamento.DEPOSITO.getValor())) {
			return "Depósito";
		}
		return "";
	}

	public String getDesignIReportRelatorio(String layout) {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + layout + ".jrxml");
	}

	public String getDesignIReportRelatorioExcel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidadeExcel() + ".jrxml");
	}

	public String caminhoBaseIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return ("RecebimentoPorTurma2Rel");
	}

	public static String getIdEntidadeExcel() {
		return ("RecebimentoPorTurmaExcelRel");
	}

	@Override
	public void validarDados(Integer codigoTurma, List<UnidadeEnsinoVO> listaUnidadeEnsino) throws ConsistirException {
		if (!listaUnidadeEnsino.isEmpty()) {
			if (codigoTurma == null || codigoTurma == 0) {
				throw new ConsistirException("A TURMA deve ser informada para a geração do relatório.");
			}
		}
	}

	public void validarDadosPeriodoRelatorioUnidadeEnsino(List<UnidadeEnsinoVO> listaUnidadeEnsino, Integer turma, Date dataInicio, Date dataFim) throws Exception {
		if (dataFim.before(dataInicio)) {
			throw new Exception("A Data Fim está menor que a Data Início.");
		}
		Integer qtdeMeses = 0;
		if (!listaUnidadeEnsino.isEmpty()) {
			if (turma == null || turma == 0) {
				qtdeMeses = Uteis.getDataQuantidadeMesesEntreData(dataInicio, dataFim);
			}
		}
		if (qtdeMeses > 6) {
			throw new Exception("Só é possível gerar o relatório sem informar a Turma por um período máximo de 6 meses.");
		}
	}
}