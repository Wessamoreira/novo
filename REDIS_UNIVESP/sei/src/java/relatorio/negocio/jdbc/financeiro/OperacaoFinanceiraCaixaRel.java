/**
 * 
 */
package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoOrigemMovimentacaoCaixa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import relatorio.negocio.comuns.financeiro.OperacaoFinanceiraCaixaItemRelVO;
import relatorio.negocio.comuns.financeiro.OperacaoFinanceiraCaixaRelVO;
import relatorio.negocio.comuns.financeiro.OperacaoFinanceiraCaixaUnidadeEnsinoRelVO;
import relatorio.negocio.comuns.financeiro.OperacaoFinanceiraRelVO;
import relatorio.negocio.comuns.financeiro.OperacaoFinanceiraResumoFormaPagamentoRelVO;
import relatorio.negocio.comuns.financeiro.enumeradores.TipoValorOperacaoFinanceiraCaixaEnum;
import relatorio.negocio.interfaces.financeiro.OperacaoFinanceiraCaixaRelInterfaceFacade;

/**
 * @author Rodrigo Wind
 *
 */
@Service
@Lazy
public class OperacaoFinanceiraCaixaRel extends ControleAcesso implements OperacaoFinanceiraCaixaRelInterfaceFacade {

	
	private static final long serialVersionUID = -3766943213582823743L;

	
	@Override
	public List<OperacaoFinanceiraRelVO> realizarGeracaoRelatorio(Integer unidadeEnsino, Integer contaCaixa, Date dataInicio, Date dataTermino, String tipoLayout, UsuarioVO usuarioLogadoVO) throws Exception {
		OperacaoFinanceiraCaixaRel.consultar("OperacaoFinanceiraCaixaRel", true, usuarioLogadoVO);
		validarDados(dataInicio, dataTermino);
		SqlRowSet rs = realizarConsultaDadosGeracaoRelatorio(unidadeEnsino, contaCaixa, dataInicio, dataTermino, tipoLayout, usuarioLogadoVO);
		List<OperacaoFinanceiraRelVO> operacaoFinanceiraRelVOs =  montarDadosConsulta(rs, unidadeEnsino, contaCaixa, dataInicio, dataTermino, tipoLayout,usuarioLogadoVO);
		return operacaoFinanceiraRelVOs;
	}
	
	private void validarDados(Date dataInicio, Date dataTermino) throws ConsistirException{
		if(!Uteis.isAtributoPreenchido(dataInicio)){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_OperacaoFinanceiraRel_dataInicio"));
		}
		if(!Uteis.isAtributoPreenchido(dataTermino)){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_OperacaoFinanceiraRel_dataTermino"));
		}
		if(dataInicio.compareTo(dataTermino)> 0 && !Uteis.getData(dataInicio).equals(Uteis.getData(dataTermino))){
			throw new ConsistirException(UteisJSF.internacionalizar("msg_OperacaoFinanceiraRel_dataTermino_menor_dataInicio"));
		}
	}
	
	private SqlRowSet realizarConsultaDadosGeracaoRelatorio(Integer unidadeEnsino, Integer contaCaixa, Date dataInicio, Date dataTermino, String tipoLayout, UsuarioVO usuarioLogadoVO) throws Exception {
		StringBuilder sql  = new StringBuilder("");
		sql.append(realizarCriacaoSqlConsultaMonvimentacaoCaixaDinheiroCheque(unidadeEnsino, contaCaixa, dataInicio, dataTermino, tipoLayout, usuarioLogadoVO));
		sql.append(" union all ");
		sql.append(realizarCriacaoSqlConsultaRecebimentoDiferenteDinheiroCheque(unidadeEnsino, contaCaixa, dataInicio, dataTermino, tipoLayout, usuarioLogadoVO));
		sql.append(" union all ");
		sql.append(realizarCriacaoSqlConsultaPagamentoDiferenteDinheiroCheque(unidadeEnsino, contaCaixa, dataInicio, dataTermino, tipoLayout, usuarioLogadoVO));
		if (tipoLayout.equals("layout1")) {
			sql.append(" order by data, contaCaixa, unidadeensino, datacomhora ");			
		} else {
			sql.append(" order by unidadeensino, data, contaCaixa, datacomhora ");
		}
		System.out.print(sql.toString());
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
	}
	
	private StringBuilder realizarCriacaoSqlConsultaMonvimentacaoCaixaDinheiroCheque(Integer unidadeEnsino, Integer contaCaixa, Date dataInicio, Date dataTermino, String tipoLayout, UsuarioVO usuarioLogadoVO) throws Exception {
		StringBuilder sql  = new StringBuilder("");
		sql.append("  select contacorrente.nomeApresentacaoSistema, ");
		if (tipoLayout.equals("layout1")) {
			sql.append(" case when unidadeensinoorigem.codigo is not null then unidadeensinoorigem.nome else unidadeensino.nome end as unidadeensino, ");
		} else {
			sql.append(" unidadeensino.nome as unidadeensino, ");
		}
		sql.append("  movimentacaocaixa.tipoOrigem as tipoOrigem, movimentacaocaixa.tipoMovimentacao as tipoOperacao, fluxocaixa.dataabertura::DATE as data, contacorrente.codigo as codigoContaCaixa, contacorrente.numero as contacaixa,");
		sql.append("  formapagamento.codigo as codigoFormaPagamento, formapagamento.nome as formapagamento, formapagamento.tipo as tipoFormaPagamento,");
		sql.append("  cheque.codigo as codigoCheque, case when cheque.codigo is null then movimentacaocaixa.numerocheque else  cheque.numero end as numeroCheque, ");
		sql.append("  case when cheque.codigo is null then movimentacaocaixa.dataPrevisaoCheque else  cheque.dataPrevisao end as dataPrevisaoCheque, ");
		sql.append("  cheque.dataBaixa as dataCompensacaoCheque, case when cheque.codigo is null then movimentacaocaixa.banco else cheque.banco end as bancoCheque,");
		sql.append("  case when cheque.codigo is null then movimentacaocaixa.agenciacheque else cheque.agencia end as agenciaCheque, ");
		sql.append("  case when cheque.codigo is null then movimentacaocaixa.contaCorrenteCheque  else cheque.numeroContaCorrente end as contaCorrenteCheque, ");
		sql.append("  case when cheque.codigo is null then movimentacaocaixa.sacadocheque else cheque.sacado end as sacadoCheque, ");
		sql.append("  cheque.situacao as situacaoCheque,");
		sql.append("  case when movimentacaocaixa.pessoa is not null then  pessoa.nome else case when movimentacaocaixa.fornecedor is not null then fornecedor.nome else parceiro.nome end end as sacado, ");
		sql.append("  movimentacaocaixa.codigoorigem as codigoOrigem, movimentacaocaixa.valor as valor, null as dataPrevisaoCartao, movimentacaocaixa.data as datacomhora, null AS formapagamentonegociacaorecebimentocartaocredito, null AS formapagamentonegociacaorecebimento ");
		sql.append("  from fluxocaixa  ");
		sql.append("  inner join movimentacaocaixa on movimentacaocaixa.fluxocaixa = fluxocaixa.codigo");
		sql.append("  inner join unidadeensino on unidadeensino.codigo = fluxocaixa.unidadeensino ");
		sql.append("  left join unidadeensino as unidadeensinoorigem on ((movimentacaocaixa.tipoOrigem in('RE', 'TR') and unidadeensinoorigem.codigo = (select negociacaorecebimento.unidadeensino from negociacaorecebimento where negociacaorecebimento.codigo = movimentacaocaixa.codigoorigem )) or ");
		sql.append("  (movimentacaocaixa.tipoOrigem = 'PA' and unidadeensinoorigem.codigo = (select negociacaopagamento.unidadeensino from negociacaopagamento where negociacaopagamento.codigo = movimentacaocaixa.codigoorigem )) or ");
		sql.append("  (movimentacaocaixa.tipoOrigem = 'DC' and unidadeensinoorigem.codigo = (select devolucaocheque.unidadeensino from devolucaocheque where devolucaocheque.codigo = movimentacaocaixa.codigoorigem )) )");
		sql.append("  inner join contacorrente on fluxocaixa.contacaixa = contacorrente.codigo ");
		sql.append("  inner join formapagamento on formapagamento.codigo = movimentacaocaixa.formapagamento");
		sql.append("  left join cheque on  cheque.codigo = movimentacaocaixa.cheque");
		sql.append("  left join pessoa on pessoa.codigo = movimentacaocaixa.pessoa");
		sql.append("  left join parceiro on parceiro.codigo = movimentacaocaixa.parceiro");
		sql.append("  left join fornecedor on fornecedor.codigo = movimentacaocaixa.fornecedor");		
		sql.append(realizarGeracaoClausulaWhere(unidadeEnsino, contaCaixa, dataInicio, dataTermino, "fluxocaixa.dataabertura"));
		sql.append(" and formapagamento.tipo in ('CH', 'DI') ");
		return sql;
	}
	
	public StringBuilder realizarGeracaoClausulaWhere(Integer unidadeEnsino, Integer contaCaixa, Date dataInicio, Date dataTermino, String campoData){
		StringBuilder sql  = new StringBuilder("");
		sql.append(" WHERE ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, campoData, false));
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sql.append(" and unidadeensino.codigo = ").append(unidadeEnsino);
		}
		if(Uteis.isAtributoPreenchido(contaCaixa)){
			sql.append(" and contacorrente.codigo = ").append(contaCaixa);
		}
		sql.append(" and contacorrente.contacaixa ");
		return sql;
	}
	
	private StringBuilder realizarCriacaoSqlConsultaRecebimentoDiferenteDinheiroCheque(Integer unidadeEnsino, Integer contaCaixa, Date dataInicio, Date dataTermino, String tipoLayout, UsuarioVO usuarioLogadoVO) throws Exception {
		StringBuilder sql  = new StringBuilder("");
		if (tipoLayout.equals("layout2")) {
			sql.append("(select contacorrente.nomeApresentacaoSistema,unid.nome as unidadeensino, ");
		} else {
			sql.append("(select contacorrente.nomeApresentacaoSistema,unidadeensino.nome as unidadeensino, ");
		}
		sql.append("'RE' as tipoOrigem, 'EN' as tipoOperacao, negociacaorecebimento.data::DATE as data, contacorrente.codigo codigoContaCaixa, contacorrente.numero as contacaixa, ");
		sql.append("formapagamento.codigo as codigoFormaPagamento, formapagamento.nome as formapagamento, formapagamento.tipo as tipoFormaPagamento, ");
		sql.append("cheque.codigo as codigoCheque, cheque.numero as numeroCheque, cheque.dataPrevisao as dataPrevisaoCheque, cheque.dataBaixa as dataCompensacaoCheque, cheque.banco as bancoCheque, ");
		sql.append("cheque.agencia as agenciaCheque, cheque.numeroContaCorrente as contaCorrenteCheque, cheque.sacado as sacadoCheque, cheque.situacao as situacaoCheque, ");
		sql.append("case negociacaorecebimento.tipoPessoa when 'PA' then parceiro.nome when 'FO' then fornecedor.nome else pessoa.nome end as sacado, ");
		sql.append("negociacaorecebimento.codigo as codigoOrigem, ");
		sql.append("case when formapagamento.tipo = 'IS' then (select sum(contareceber.valorcalculadodescontolancadorecebimento) from contarecebernegociacaorecebimento inner join contareceber on contarecebernegociacaorecebimento.contareceber = contareceber.codigo ");
		sql.append("where contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo) else formapagamentonegociacaorecebimento.valorrecebimento end as valor, ");
		sql.append("formapagamentonegociacaorecebimentocartaocredito.datavencimento as dataPrevisaoCartao, negociacaorecebimento.data as datacomhora, formapagamentonegociacaorecebimentocartaocredito.codigo AS formapagamentonegociacaorecebimentocartaocredito, formapagamentonegociacaorecebimento.codigo ");
		sql.append("from negociacaorecebimento ");
		sql.append("inner join formapagamentonegociacaorecebimento on negociacaorecebimento.codigo = formapagamentonegociacaorecebimento.negociacaorecebimento ");
		if (tipoLayout.equals("layout2")) {
			sql.append("inner join movimentacaocaixa on movimentacaocaixa.codigoorigem = negociacaorecebimento.codigo ");
			sql.append("inner join fluxocaixa on fluxocaixa.codigo = movimentacaocaixa.fluxocaixa ");
			sql.append("inner join unidadeensino as unid on fluxocaixa.unidadeensino = unid.codigo ");
		}
		sql.append("inner join unidadeensino on unidadeensino.codigo = negociacaorecebimento.unidadeensino ");
		sql.append("inner join contacorrente on negociacaorecebimento.contacorrentecaixa = contacorrente.codigo ");
		sql.append("inner join formapagamento on formapagamento.codigo = formapagamentonegociacaorecebimento.formapagamento ");
		sql.append("left join cheque on  cheque.codigo = formapagamentonegociacaorecebimento.cheque ");
		sql.append("left join formapagamentonegociacaorecebimentocartaocredito on formapagamentonegociacaorecebimentocartaocredito.codigo = formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito ");
		sql.append("left join pessoa on pessoa.codigo = negociacaorecebimento.pessoa ");
		sql.append("left join parceiro on parceiro.codigo = negociacaorecebimento.parceiro ");
		sql.append("left join fornecedor on fornecedor.codigo = negociacaorecebimento.fornecedor ");		
		sql.append(realizarGeracaoClausulaWhere(unidadeEnsino, contaCaixa, dataInicio, dataTermino, "negociacaorecebimento.data"));
		sql.append(" and formapagamento.tipo not in ('CH', 'DI') ");
		sql.append("union ");
		sql.append("select contacorrente.nomeApresentacaoSistema,case when unidadeensinoorigem.codigo is not null then unidadeensinoorigem.nome else unidadeensino.nome end as unidadeensino, movimentacaocaixa.tipoOrigem as tipoOrigem, movimentacaocaixa.tipoMovimentacao as tipoOperacao, fluxocaixa.dataabertura::DATE as data, contacorrente.codigo as codigoContaCaixa, contacorrente.numero as contacaixa, ");
		sql.append("formapagamento.codigo as codigoFormaPagamento, formapagamento.nome as formapagamento, formapagamento.tipo as tipoFormaPagamento, ");
		sql.append("cheque.codigo as codigoCheque, case when cheque.codigo is null then movimentacaocaixa.numerocheque else  cheque.numero end as numeroCheque, ");
		sql.append("case when cheque.codigo is null then movimentacaocaixa.dataPrevisaoCheque else  cheque.dataPrevisao end as dataPrevisaoCheque, ");
		sql.append("cheque.dataBaixa as dataCompensacaoCheque, case when cheque.codigo is null then movimentacaocaixa.banco else cheque.banco end as bancoCheque, ");
		sql.append("case when cheque.codigo is null then movimentacaocaixa.agenciacheque else cheque.agencia end as agenciaCheque, ");
		sql.append("case when cheque.codigo is null then movimentacaocaixa.contaCorrenteCheque  else cheque.numeroContaCorrente end as contaCorrenteCheque, ");
		sql.append("case when cheque.codigo is null then movimentacaocaixa.sacadocheque else cheque.sacado end as sacadoCheque, cheque.situacao as situacaoCheque, ");
		sql.append("case when movimentacaocaixa.pessoa is not null then  pessoa.nome else case when movimentacaocaixa.fornecedor is null then fornecedor.nome else parceiro.nome end end as sacado, ");
		sql.append("movimentacaocaixa.codigoorigem as codigoOrigem, movimentacaocaixa.valor as valor, null as dataPrevisaoCartao, movimentacaocaixa.data as datacomhora, null AS formapagamentonegociacaorecebimentocartaocredito, null AS formapagamentonegociacaorecebimento ");
		sql.append("from fluxocaixa ");
		sql.append("inner join movimentacaocaixa on movimentacaocaixa.fluxocaixa = fluxocaixa.codigo ");
		sql.append("inner join unidadeensino on unidadeensino.codigo = fluxocaixa.unidadeensino ");
		sql.append("left join unidadeensino as unidadeensinoorigem on ((movimentacaocaixa.tipoOrigem in('RE', 'TR') and unidadeensinoorigem.codigo = (select negociacaorecebimento.unidadeensino from negociacaorecebimento where negociacaorecebimento.codigo = movimentacaocaixa.codigoorigem )) or ");
		sql.append("(movimentacaocaixa.tipoOrigem = 'PA' and unidadeensinoorigem.codigo = (select negociacaopagamento.unidadeensino from negociacaopagamento where negociacaopagamento.codigo = movimentacaocaixa.codigoorigem )) or ");
		sql.append("(movimentacaocaixa.tipoOrigem = 'DC' and unidadeensinoorigem.codigo = (select devolucaocheque.unidadeensino from devolucaocheque where devolucaocheque.codigo = movimentacaocaixa.codigoorigem )) ) ");
		sql.append("inner join contacorrente on fluxocaixa.contacaixa = contacorrente.codigo ");
		sql.append("inner join formapagamento on formapagamento.codigo = movimentacaocaixa.formapagamento ");
		sql.append("left join cheque on  cheque.codigo = movimentacaocaixa.cheque ");
		sql.append("left join pessoa on pessoa.codigo = movimentacaocaixa.pessoa ");
		sql.append("left join parceiro on parceiro.codigo = movimentacaocaixa.parceiro ");
		sql.append("left join fornecedor on fornecedor.codigo = movimentacaocaixa.fornecedor ");		
		sql.append(realizarGeracaoClausulaWhere(unidadeEnsino, contaCaixa, dataInicio, dataTermino, "fluxocaixa.dataabertura"));	
		sql.append(" and formapagamento.tipo not in ('CH', 'DI')");
		sql.append(" and tipoorigem = 'RE'");
		sql.append(" and  not exists (");
		sql.append("		select nr.codigo");
		sql.append("		from negociacaorecebimento nr");
		sql.append("		inner join formapagamentonegociacaorecebimento fpnr on fpnr.negociacaorecebimento = nr.codigo");
		sql.append("		inner join unidadeensino ue on ue.codigo = nr.unidadeensino");
		sql.append("		inner join contacorrente cc on nr.contacorrentecaixa = cc.codigo");
		sql.append("		inner join formapagamento fp on fp.codigo = fpnr.formapagamento");
		sql.append(" where movimentacaocaixa.codigoorigem = nr.codigo ");
		sql.append("		and fp.codigo = formapagamento.codigo ");
		sql.append("		and cc.codigo = contacorrente.codigo))");
		return sql;
	}
	
	private StringBuilder realizarCriacaoSqlConsultaPagamentoDiferenteDinheiroCheque(Integer unidadeEnsino, Integer contaCaixa, Date dataInicio, Date dataTermino, String tipoLayout, UsuarioVO usuarioLogadoVO) throws Exception {
		StringBuilder sql  = new StringBuilder("");
		sql.append("  select contacorrente.nomeApresentacaoSistema,unidadeensino.nome as unidadeensino, 'PA' as tipoOrigem, 'SA' as tipoOperacao, negociacaopagamento.data::DATE as data, contacorrente.codigo codigoContaCaixa, contacorrente.numero as contacaixa,");
		sql.append("  formapagamento.codigo as codigoFormaPagamento, formapagamento.nome as formapagamento, formapagamento.tipo as tipoFormaPagamento,");
		sql.append("  cheque.codigo as codigoCheque, cheque.numero as numeroCheque, cheque.dataPrevisao as dataPrevisaoCheque, cheque.dataBaixa as dataCompensacaoCheque, cheque.banco as bancoCheque,");
		sql.append("  cheque.agencia as agenciaCheque, cheque.numeroContaCorrente as contaCorrenteCheque, cheque.sacado as sacadoCheque, cheque.situacao as situacaoCheque,");
		sql.append("  case negociacaopagamento.tipoSacado when 'PA' then parceiro.nome when 'FO' then fornecedor.nome when 'AL' then aluno.nome when 'RF' then responsavelFinanceiro.nome ");
		sql.append("  when 'BA' then banco.nome else pessoa.nome end as sacado, negociacaopagamento.codigo as codigoOrigem, formapagamentonegociacaopagamento.valor as valor, ");
		sql.append("  null::date as dataPrevisaoCartao, negociacaopagamento.data as datacomhora, null AS formapagamentonegociacaorecebimentocartaocredito, formapagamentonegociacaopagamento.codigo ");
		sql.append("  from negociacaopagamento  ");
		sql.append("  inner join formapagamentonegociacaopagamento on negociacaopagamento.codigo = formapagamentonegociacaopagamento.negociacaocontapagar");
		sql.append("  inner join contacorrente on negociacaopagamento.caixa = contacorrente.codigo ");
		sql.append("  inner join formapagamento on formapagamento.codigo = formapagamentonegociacaopagamento.formapagamento");
		sql.append("  inner join unidadeensino on unidadeensino.codigo = negociacaopagamento.unidadeensino");
		sql.append("  left join cheque on  cheque.codigo = formapagamentonegociacaopagamento.cheque");
		sql.append("  left join pessoa as aluno on aluno.codigo = negociacaopagamento.aluno");
		sql.append("  left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = negociacaopagamento.responsavelFinanceiro");
		sql.append("  left join parceiro on parceiro.codigo = negociacaopagamento.parceiro");
		sql.append("  left join fornecedor on fornecedor.codigo = negociacaopagamento.fornecedor");
		sql.append("  left join banco on banco.codigo = negociacaopagamento.banco");
		sql.append("  left join funcionario on funcionario.codigo = negociacaopagamento.funcionario");
		sql.append("  left join pessoa on pessoa.codigo = funcionario.pessoa");		
		sql.append(realizarGeracaoClausulaWhere(unidadeEnsino, contaCaixa, dataInicio, dataTermino, "negociacaopagamento.data"));
		sql.append(" and (formapagamento.tipo not in ('CH', 'DI') or (cheque.chequeproprio)) ");
		return sql;
	}
			
	
	private List<OperacaoFinanceiraRelVO> montarDadosConsulta(SqlRowSet rs, Integer unidadeEnsino, Integer contaCaixa, Date dataInicio, Date dataTermino, String tipoLayout,UsuarioVO usuarioVO) throws Exception{
		List<OperacaoFinanceiraRelVO> operacaoFinanceiraRelVOs = new ArrayList<OperacaoFinanceiraRelVO>(0);
		OperacaoFinanceiraRelVO operacaoFinanceiraRelGeralVO  = new OperacaoFinanceiraRelVO();
		OperacaoFinanceiraRelVO operacaoFinanceiraRelDiaVO  = null;
		OperacaoFinanceiraCaixaRelVO caixa = null;
		OperacaoFinanceiraCaixaItemRelVO caixaItem = null;
		OperacaoFinanceiraCaixaUnidadeEnsinoRelVO caixaUnidadeEnsino = null;
		String dataBase = "";
		String nomeUnidadeEnsino = "";		
		Integer contaCaixaBase = 0; 
		Map<Integer, Double> mapCodigoOrigemTrocoVOs = new HashMap<Integer, Double>(0);
		Map<Integer, Map<String, Map<TipoValorOperacaoFinanceiraCaixaEnum, Double>>> caixaDataSaldos =  consultarSaldoCaixaDiaDia(unidadeEnsino, contaCaixa, dataInicio, dataTermino);
		String unidadeEnsinoContaCaixa = "";
		while(rs.next()){
			if (tipoLayout.equals("layout2")) {
				if (nomeUnidadeEnsino.equals("")) {
					nomeUnidadeEnsino = rs.getString("unidadeEnsino");
				}
				if (!rs.getString("unidadeEnsino").equals(nomeUnidadeEnsino)) {
					operacaoFinanceiraRelVOs.add(operacaoFinanceiraRelGeralVO);
					operacaoFinanceiraRelGeralVO = new OperacaoFinanceiraRelVO();
					nomeUnidadeEnsino = rs.getString("unidadeEnsino");
					dataBase = "";
				}
			}
			operacaoFinanceiraRelGeralVO.setUnidadeEnsino(rs.getString("unidadeEnsino"));		
			if(dataBase.trim().isEmpty() || !dataBase.equals(Uteis.getData(rs.getDate("data")))){
				operacaoFinanceiraRelDiaVO = montarDadosConsultaOperacao(rs);
				dataBase = Uteis.getData(rs.getDate("data"));
				contaCaixaBase = 0;
				unidadeEnsinoContaCaixa = "";
				operacaoFinanceiraRelGeralVO.getOperacaoFinanceiraRelVOs().add(operacaoFinanceiraRelDiaVO);
			}
			if(contaCaixaBase.equals(0) || !contaCaixaBase.equals(rs.getInt("codigoContaCaixa"))){
				Map<TipoValorOperacaoFinanceiraCaixaEnum, Double> saldoCaixa = null;
				if(caixaDataSaldos.containsKey(rs.getInt("codigoContaCaixa")) && caixaDataSaldos.get(rs.getInt("codigoContaCaixa")).containsKey(dataBase)){
					saldoCaixa = caixaDataSaldos.get(rs.getInt("codigoContaCaixa")).get(dataBase);
				}
				caixa = montarDadosConsultaOperacaoCaixa(rs, saldoCaixa);
				operacaoFinanceiraRelDiaVO.getOperacaoFinanceiraCaixaRelVOs().add(caixa);
				operacaoFinanceiraRelDiaVO.setSaldoDinheiro(operacaoFinanceiraRelDiaVO.getSaldoDinheiro()+caixa.getSaldoFinalDinheiro());
				operacaoFinanceiraRelDiaVO.setSaldoCheque(operacaoFinanceiraRelDiaVO.getSaldoCheque()+caixa.getSaldoFinalCheque());
				contaCaixaBase = rs.getInt("codigoContaCaixa");
			}	
			if (unidadeEnsinoContaCaixa.equals("") || !unidadeEnsinoContaCaixa.equals(rs.getString("unidadeEnsino"))) {
				caixaUnidadeEnsino = montarDadosConsultaOperacaoCaixaUnidadeEnsino(caixa, dataBase, rs);
				caixa.getOperacaoFinanceiraCaixaUnidadeEnsinoRelVOs().add(caixaUnidadeEnsino);
				unidadeEnsinoContaCaixa = rs.getString("unidadeEnsino");
			}
//			caixaItem = montarDadosConsultaOperacaoCaixaItem(caixa, dataBase, caixa.getMapResumoFormaPagamentoPorUnidadeEnsinoVOs(), rs);
//			caixa.getOperacaoFinanceiraCaixaItemRelVOs().add(caixaItem);
			caixaItem = montarDadosConsultaOperacaoCaixaItem(rs,usuarioVO);
			caixaUnidadeEnsino.getOperacaoFinanceiraCaixaItemRelVOs().add(caixaItem);
			if(caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.ENTRADA)){
				caixa.setQtdeEntrada(caixa.getQtdeEntrada()+1);
				operacaoFinanceiraRelDiaVO.setQtdeEntrada(operacaoFinanceiraRelDiaVO.getQtdeEntrada()+1);
				operacaoFinanceiraRelGeralVO.setQtdeEntrada(operacaoFinanceiraRelGeralVO.getQtdeEntrada()+1);
				caixaUnidadeEnsino.setQtdeEntrada(caixaUnidadeEnsino.getQtdeEntrada()+1);
				if(caixaItem.getFormaPagamentoVO().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor()) || caixaItem.getFormaPagamentoVO().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor())){
					caixa.setTotalEntradaDia(caixa.getTotalEntradaDia()+caixaItem.getValor());	
					operacaoFinanceiraRelDiaVO.setTotalEntradaDia(operacaoFinanceiraRelDiaVO.getTotalEntradaDia()+caixaItem.getValor());
					caixaUnidadeEnsino.setTotalEntradaDia(caixaUnidadeEnsino.getTotalEntradaDia()+caixaItem.getValor());
				}
			}else{
				caixa.setQtdeSaida(caixa.getQtdeSaida()+1);
				operacaoFinanceiraRelDiaVO.setQtdeSaida(operacaoFinanceiraRelDiaVO.getQtdeSaida()+1);
				operacaoFinanceiraRelGeralVO.setQtdeSaida(operacaoFinanceiraRelGeralVO.getQtdeSaida()+1);
				caixaUnidadeEnsino.setQtdeSaida(caixaUnidadeEnsino.getQtdeSaida()+1);
				if(caixaItem.getFormaPagamentoVO().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor()) || caixaItem.getFormaPagamentoVO().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor())){
					caixa.setTotalSaidaDia(caixa.getTotalSaidaDia()+caixaItem.getValor());	
					operacaoFinanceiraRelDiaVO.setTotalSaidaDia(operacaoFinanceiraRelDiaVO.getTotalSaidaDia()+caixaItem.getValor());	
					caixaUnidadeEnsino.setTotalSaidaDia(caixaUnidadeEnsino.getTotalSaidaDia()+caixaItem.getValor());
				}
			}
			if(caixaItem.getTipoOrigem().equals(TipoOrigemMovimentacaoCaixa.PAGAMENTO)){
				adicionarOperacaoFinanceiraResumoFormaPagamentoRel(caixa.getOperacaoFinanceiraResumoFormaPagamentoRelVOs(), caixaItem.getFormaPagamentoVO(), caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.ENTRADA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.SAIDA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOrigem(), caixaItem.getTipoOperacao(), caixaItem.getCodigoOrigem(), mapCodigoOrigemTrocoVOs);
				adicionarOperacaoFinanceiraResumoFormaPagamentoRel(operacaoFinanceiraRelDiaVO.getOperacaoFinanceiraResumoFormaPagamentoRelVOs(), caixaItem.getFormaPagamentoVO(), caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.ENTRADA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.SAIDA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOrigem(), caixaItem.getTipoOperacao(), caixaItem.getCodigoOrigem(), mapCodigoOrigemTrocoVOs);
				adicionarOperacaoFinanceiraResumoFormaPagamentoRel(operacaoFinanceiraRelGeralVO.getOperacaoFinanceiraResumoFormaPagamentoRelVOs(), caixaItem.getFormaPagamentoVO(), caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.ENTRADA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.SAIDA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOrigem(), caixaItem.getTipoOperacao(), caixaItem.getCodigoOrigem(), mapCodigoOrigemTrocoVOs);
				adicionarOperacaoFinanceiraResumoFormaPagamentoRel(caixaUnidadeEnsino.getOperacaoFinanceiraResumoFormaPagamentoRelVOs(), caixaItem.getFormaPagamentoVO(), caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.ENTRADA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.SAIDA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOrigem(), caixaItem.getTipoOperacao(), caixaItem.getCodigoOrigem(), mapCodigoOrigemTrocoVOs);
			}else{
				adicionarOperacaoFinanceiraResumoFormaPagamentoRel(caixa.getOperacaoFinanceiraResumoFormaRecebimentoRelVOs(), caixaItem.getFormaPagamentoVO(), caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.ENTRADA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.SAIDA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOrigem(), caixaItem.getTipoOperacao(), caixaItem.getCodigoOrigem(), mapCodigoOrigemTrocoVOs);				
				adicionarOperacaoFinanceiraResumoFormaPagamentoRel(operacaoFinanceiraRelDiaVO.getOperacaoFinanceiraResumoFormaRecebimentoRelVOs(), caixaItem.getFormaPagamentoVO(), caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.ENTRADA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.SAIDA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOrigem(), caixaItem.getTipoOperacao(), caixaItem.getCodigoOrigem(), mapCodigoOrigemTrocoVOs);				
				adicionarOperacaoFinanceiraResumoFormaPagamentoRel(operacaoFinanceiraRelGeralVO.getOperacaoFinanceiraResumoFormaRecebimentoRelVOs(), caixaItem.getFormaPagamentoVO(), caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.ENTRADA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.SAIDA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOrigem(), caixaItem.getTipoOperacao(), caixaItem.getCodigoOrigem(), mapCodigoOrigemTrocoVOs);
				adicionarOperacaoFinanceiraResumoFormaPagamentoRel(caixaUnidadeEnsino.getOperacaoFinanceiraResumoFormaRecebimentoRelVOs(), caixaItem.getFormaPagamentoVO(), caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.ENTRADA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOperacao().equals(TipoMovimentacaoFinanceira.SAIDA) ? caixaItem.getValor() : 0.0, caixaItem.getTipoOrigem(), caixaItem.getTipoOperacao(), caixaItem.getCodigoOrigem(), mapCodigoOrigemTrocoVOs);
			 }
		}
		operacaoFinanceiraRelVOs.add(operacaoFinanceiraRelGeralVO);
		return operacaoFinanceiraRelVOs;
	}
	
	private OperacaoFinanceiraRelVO montarDadosConsultaOperacao(SqlRowSet rs) throws Exception{
		OperacaoFinanceiraRelVO obj =  new OperacaoFinanceiraRelVO();
		obj.setData(rs.getDate("data"));		
		return obj;
	}
	
	private OperacaoFinanceiraCaixaRelVO montarDadosConsultaOperacaoCaixa(SqlRowSet rs, Map<TipoValorOperacaoFinanceiraCaixaEnum, Double> saldoCaixa) throws Exception{
		OperacaoFinanceiraCaixaRelVO obj =  new OperacaoFinanceiraCaixaRelVO();
		obj.setCodigoCaixa(rs.getInt("codigoContaCaixa"));			
		if(Uteis.isAtributoPreenchido(rs.getString("nomeApresentacaoSistema"))){
			obj.setContaCaixa(rs.getString("nomeApresentacaoSistema"));	
		}else{
			obj.setContaCaixa(rs.getString("contaCaixa"));			
		}		
		obj.setDataAberturaCaixa(rs.getDate("data"));		
		obj.setUnidadeEnsino(rs.getString("unidadeEnsino"));		
		if(saldoCaixa != null){
			obj.setSaldoInicialCaixa(saldoCaixa.get(TipoValorOperacaoFinanceiraCaixaEnum.SALDO_INICIAL));
			obj.setSaldoInicialCheque(saldoCaixa.get(TipoValorOperacaoFinanceiraCaixaEnum.SALDO_INICIAL_CHEQUE));
			obj.setSaldoInicialDinheiro(saldoCaixa.get(TipoValorOperacaoFinanceiraCaixaEnum.SALDO_INICIAL_DINHEIRO));
			obj.setSaldoFinalCaixa(saldoCaixa.get(TipoValorOperacaoFinanceiraCaixaEnum.SALDO_FINAL));
			obj.setSaldoFinalCheque(saldoCaixa.get(TipoValorOperacaoFinanceiraCaixaEnum.SALDO_FINAL_CHEQUE));
			obj.setSaldoFinalDinheiro(saldoCaixa.get(TipoValorOperacaoFinanceiraCaixaEnum.SALDO_FINAL_DINHEIRO));			
		}
		return obj;
	}
	
	private OperacaoFinanceiraCaixaUnidadeEnsinoRelVO montarDadosConsultaOperacaoCaixaUnidadeEnsino(OperacaoFinanceiraCaixaRelVO caixa, String dataBase, SqlRowSet rs) throws Exception{
		OperacaoFinanceiraCaixaUnidadeEnsinoRelVO obj =  new OperacaoFinanceiraCaixaUnidadeEnsinoRelVO();
		obj.setUnidadeEnsino(rs.getString("unidadeEnsino"));
		obj.setDataBase(dataBase);
		return obj;
	}
	
	private OperacaoFinanceiraCaixaItemRelVO montarDadosConsultaOperacaoCaixaItem(SqlRowSet rs,UsuarioVO usuarioLogadoVO) throws Exception{
		OperacaoFinanceiraCaixaItemRelVO obj =  new OperacaoFinanceiraCaixaItemRelVO();
		obj.setCodigoOrigem(rs.getInt("codigoOrigem"));
		obj.setSacado(rs.getString("sacado"));
		obj.setUnidadeEnsino(rs.getString("unidadeEnsino"));
		obj.getFormaPagamentoVO().setCodigo(rs.getInt("codigoFormaPagamento"));
		obj.getFormaPagamentoVO().setNome(rs.getString("formaPagamento"));
		obj.getFormaPagamentoVO().setTipo(rs.getString("tipoFormaPagamento"));
		obj.setTipoOrigem(TipoOrigemMovimentacaoCaixa.getEnum(rs.getString("tipoOrigem")));
		if (obj.getTipoOrigem().equals(TipoOrigemMovimentacaoCaixa.MOVIMENTACAO_FINANCEIRA)) {
			this.montarDadosContaCorrenteMovimentacaoFinanceira(obj,rs.getInt("codigoContaCaixa"),usuarioLogadoVO);
		}
		obj.setValor(rs.getDouble("valor"));
		obj.setTipoOperacao(TipoMovimentacaoFinanceira.getEnum(rs.getString("tipoOperacao")));
		obj.setDataPrevisao(obj.getFormaPagamentoVO().getTipo().equals(TipoFormaPagamento.CHEQUE.getValor())?rs.getDate("dataPrevisaoCheque"):rs.getDate("dataPrevisaoCartao"));
		obj.getChequeVO().setCodigo(rs.getInt("codigoCheque"));
		obj.getChequeVO().setNumero(rs.getString("numeroCheque"));
		obj.getChequeVO().setBanco(rs.getString("bancoCheque"));
		obj.getChequeVO().setAgencia(rs.getString("agenciaCheque"));
		obj.getChequeVO().setNumeroContaCorrente(rs.getString("contaCorrenteCheque"));
		obj.getChequeVO().setSacado(rs.getString("sacadoCheque"));
		obj.getChequeVO().setSituacao(rs.getString("situacaoCheque"));
		obj.getChequeVO().setDataBaixa(rs.getDate("dataCompensacaoCheque"));
		return obj;
	}
	
	private void adicionarOperacaoFinanceiraResumoFormaPagamentoRel(List<OperacaoFinanceiraResumoFormaPagamentoRelVO> resumoListaAdd, FormaPagamentoVO formaPagamentoVO, Double valorEntrada, Double valorSaida, TipoOrigemMovimentacaoCaixa tipoOrigemMovimentacaoCaixa, TipoMovimentacaoFinanceira tipoMovimentacaoFinanceira, Integer codigoOrigem, Map<Integer, Double> mapCodigoOrigemTrocoVOs){	
		for(OperacaoFinanceiraResumoFormaPagamentoRelVO resumoObj: resumoListaAdd){
			if(resumoObj.getFormaPagamentoVO().getCodigo().equals(formaPagamentoVO.getCodigo())){
				if (tipoOrigemMovimentacaoCaixa.equals(TipoOrigemMovimentacaoCaixa.TROCO)) {

					resumoObj.setTotalSaidaTroco(resumoObj.getTotalSaidaTroco() + valorSaida);
					if (!mapCodigoOrigemTrocoVOs.containsKey(codigoOrigem)) {
						mapCodigoOrigemTrocoVOs.put(codigoOrigem, valorSaida);
					} 
					
				} else {
					//Se encontrar dentro do Map de troco o codigo origem e o tipo de movimentação é de Saída
					//quer dizer que foi feito um estorno do recebimento e preciso calcular o valor do troco no total de saída.
					if (mapCodigoOrigemTrocoVOs.containsKey(codigoOrigem) && tipoMovimentacaoFinanceira.equals(TipoMovimentacaoFinanceira.SAIDA)) {
						resumoObj.setTotalEntradaTroco(resumoObj.getTotalEntradaTroco() + mapCodigoOrigemTrocoVOs.get(codigoOrigem));
						resumoObj.setTotalEntrada(resumoObj.getTotalEntrada()+valorEntrada);
						resumoObj.setTotalSaida((resumoObj.getTotalSaida() + valorSaida));
					} else {
						resumoObj.setTotalEntrada(resumoObj.getTotalEntrada()+valorEntrada);				
						resumoObj.setTotalSaida(resumoObj.getTotalSaida()+valorSaida);				
					}
				}
				return;
			}
		}
		OperacaoFinanceiraResumoFormaPagamentoRelVO resumoObj =  new OperacaoFinanceiraResumoFormaPagamentoRelVO();
		resumoObj.setFormaPagamentoVO(formaPagamentoVO);
		if (tipoOrigemMovimentacaoCaixa.equals(TipoOrigemMovimentacaoCaixa.TROCO)) {
			resumoObj.setTotalSaidaTroco(resumoObj.getTotalSaidaTroco() + valorEntrada);
			if (!mapCodigoOrigemTrocoVOs.containsKey(codigoOrigem)) {
				mapCodigoOrigemTrocoVOs.put(codigoOrigem, valorSaida);
			}
		} else {
			//Se encontrar dentro do Map de troco o codigo origem e o tipo de movimentação é de Saída
			//quer dizer que foi feito um estorno do recebimento e preciso calcular o valor do troco no total de saída.
			if (mapCodigoOrigemTrocoVOs.containsKey(codigoOrigem) && tipoMovimentacaoFinanceira.equals(TipoMovimentacaoFinanceira.SAIDA)) {
				resumoObj.setTotalEntradaTroco(resumoObj.getTotalEntradaTroco() + mapCodigoOrigemTrocoVOs.get(codigoOrigem));
				resumoObj.setTotalEntrada(resumoObj.getTotalEntrada()+valorEntrada);
				resumoObj.setTotalSaida((resumoObj.getTotalSaida() + valorSaida));
			} else {
				resumoObj.setTotalEntrada(resumoObj.getTotalEntrada()+valorEntrada);				
				resumoObj.setTotalSaida(resumoObj.getTotalSaida()+valorSaida);				
			}		
		}
		resumoListaAdd.add(resumoObj);
	}
	
	public Map<Integer, Map<String, Map<TipoValorOperacaoFinanceiraCaixaEnum, Double>>> consultarSaldoCaixaDiaDia(Integer unidadeEnsino, Integer contaCaixa, Date dataInicio, Date dataTermino){
		StringBuilder sql = new StringBuilder("");
		sql.append("  select fluxocaixa.contacaixa, fluxocaixa.dataabertura::date as data,");
		sql.append("  (sum(entradadinheiro) - sum(saidadinheiro)) + (sum(entradacheque) - sum(saidacheque)) ");
		sql.append("  - (sum(case when fluxocaixa.contacaixa = caixaanterior.contacaixa and fluxocaixa.dataabertura::date = caixaanterior.dataabertura::date then entradadinheiro else 0.0 end)) ");
		sql.append("  + (sum(case when fluxocaixa.contacaixa = caixaanterior.contacaixa and fluxocaixa.dataabertura::date = caixaanterior.dataabertura::date then saidadinheiro else 0.0 end))");
		sql.append("  - (sum(case when fluxocaixa.contacaixa = caixaanterior.contacaixa and fluxocaixa.dataabertura::date = caixaanterior.dataabertura::date then entradacheque else 0.0 end))");
		sql.append("  + (sum(case when fluxocaixa.contacaixa = caixaanterior.contacaixa and fluxocaixa.dataabertura::date = caixaanterior.dataabertura::date then saidacheque else 0.0 end)) as saldoinicialcaixa,");
		sql.append(" ");
		sql.append("  (sum(entradadinheiro) - sum(saidadinheiro)) - (sum(case when fluxocaixa.codigo = caixaanterior.codigo then entradadinheiro else 0.0 end)) ");
		sql.append("  + (sum(case when fluxocaixa.codigo = caixaanterior.codigo then saidadinheiro else 0.0 end)) as saldoinicialdinheirodia,");
		sql.append("  sum(case when fluxocaixa.codigo = caixaanterior.codigo then entradadinheiro else 0.0 end) entradadinheirodia,");
		sql.append("  sum(case when fluxocaixa.codigo = caixaanterior.codigo then saidadinheiro else 0.0 end) saidadinheirodia,");
		sql.append("  (sum(entradadinheiro) - sum(saidadinheiro)) as saldofinaldinheiro,");
		sql.append("  (sum(entradacheque) - sum(saidacheque)) ");
		sql.append("  - (sum(case when fluxocaixa.contacaixa = caixaanterior.contacaixa and fluxocaixa.dataabertura::date = caixaanterior.dataabertura::date then entradacheque else 0.0 end))");
		sql.append("  + (sum(case when fluxocaixa.contacaixa = caixaanterior.contacaixa and fluxocaixa.dataabertura::date = caixaanterior.dataabertura::date then saidacheque else 0.0 end)) as saldoinicialchequedia,");
		sql.append("  sum(case when fluxocaixa.codigo = caixaanterior.codigo then entradacheque else 0.0 end) entradachequedia,");
		sql.append("  sum(case when fluxocaixa.codigo = caixaanterior.codigo then saidacheque else 0.0 end) saidachequedia,");
		sql.append("  (sum(entradacheque) - sum(saidacheque)) as saldofinalcheque, ");
		sql.append("  (sum(entradadinheiro) - sum(saidadinheiro)) + (sum(entradacheque) - sum(saidacheque)) as saldofinalcaixa");
		sql.append("  from fluxocaixa");
		sql.append("  inner join (");
		sql.append("  select fluxocaixa.codigo, fluxocaixa.dataabertura, fluxocaixa.contacaixa, movimentacaocaixa.data::date as data, ");
		sql.append("  sum(case when formapagamento.tipo = 'DI' and tipomovimentacao = 'EN' then movimentacaocaixa.valor else 0.0 end ) as entradadinheiro,");
		sql.append("  sum(case when formapagamento.tipo = 'DI' and tipomovimentacao = 'SA' then movimentacaocaixa.valor else 0.0 end ) as saidadinheiro,");
		sql.append("  sum(case when formapagamento.tipo = 'CH' and tipomovimentacao = 'EN' then movimentacaocaixa.valor else 0.0 end ) as entradacheque,");
		sql.append("  sum(case when formapagamento.tipo = 'CH' and tipomovimentacao = 'SA' then movimentacaocaixa.valor else 0.0 end ) as saidacheque");
		sql.append("  from fluxocaixa ");
		sql.append("  left join movimentacaocaixa on fluxocaixa.codigo = movimentacaocaixa.fluxocaixa");
		sql.append("  left join formapagamento on formapagamento.codigo = movimentacaocaixa.formapagamento and formapagamento.tipo in ('DI', 'CH')");
		sql.append("  where fluxocaixa.dataabertura::DATE <= '").append(Uteis.getDataJDBC(dataTermino)).append("'");
		if(Uteis.isAtributoPreenchido(unidadeEnsino)){
			sql.append("  and fluxocaixa.unidadeensino = 	").append(unidadeEnsino);
		}
		if(Uteis.isAtributoPreenchido(contaCaixa)){
			sql.append("  and fluxocaixa.contacaixa = 	").append(contaCaixa);
		}
		sql.append("  group by fluxocaixa.codigo, fluxocaixa.contacaixa, movimentacaocaixa.data::date");		
		sql.append("  ) as caixaanterior on caixaanterior.contacaixa = fluxocaixa.contacaixa");
		sql.append("  and caixaanterior.codigo  <= fluxocaixa.codigo");
		sql.append("  where ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "fluxocaixa.dataabertura", false));
		sql.append("  group  by fluxocaixa.codigo, fluxocaixa.saldoinicial, fluxocaixa.saldofinal, fluxocaixa.dataabertura, fluxocaixa.datafechamento");
		sql.append("  order by fluxocaixa.contacaixa, fluxocaixa.dataabertura::DATE, fluxocaixa.codigo ");
		Map<Integer, Map<String, Map<TipoValorOperacaoFinanceiraCaixaEnum, Double>>> caixaDataSaldos =  new HashMap<Integer, Map<String,Map<TipoValorOperacaoFinanceiraCaixaEnum,Double>>>(0);
		SqlRowSet rs  =  getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while(rs.next()){
			if(!caixaDataSaldos.containsKey(rs.getInt("contacaixa"))){
				caixaDataSaldos.put(rs.getInt("contacaixa"), new HashMap<String, Map<TipoValorOperacaoFinanceiraCaixaEnum,Double>>(0));
			}
			String data = Uteis.getData(rs.getDate("data"));
			if(!caixaDataSaldos.get(rs.getInt("contacaixa")).containsKey(data)){
				caixaDataSaldos.get(rs.getInt("contacaixa")).put(data, new HashMap<TipoValorOperacaoFinanceiraCaixaEnum, Double>());
			}
			caixaDataSaldos.get(rs.getInt("contacaixa")).get(data).put(TipoValorOperacaoFinanceiraCaixaEnum.SALDO_INICIAL, rs.getDouble("saldoInicialCaixa"));
			caixaDataSaldos.get(rs.getInt("contacaixa")).get(data).put(TipoValorOperacaoFinanceiraCaixaEnum.SALDO_INICIAL_CHEQUE, rs.getDouble("saldoInicialChequeDia"));
			caixaDataSaldos.get(rs.getInt("contacaixa")).get(data).put(TipoValorOperacaoFinanceiraCaixaEnum.SALDO_INICIAL_DINHEIRO, rs.getDouble("saldoInicialDinheiroDia"));
			caixaDataSaldos.get(rs.getInt("contacaixa")).get(data).put(TipoValorOperacaoFinanceiraCaixaEnum.SALDO_FINAL, rs.getDouble("saldoFinalCaixa"));
			caixaDataSaldos.get(rs.getInt("contacaixa")).get(data).put(TipoValorOperacaoFinanceiraCaixaEnum.SALDO_FINAL_CHEQUE, rs.getDouble("saldoFinalCheque"));
			caixaDataSaldos.get(rs.getInt("contacaixa")).get(data).put(TipoValorOperacaoFinanceiraCaixaEnum.SALDO_FINAL_DINHEIRO, rs.getDouble("saldoFinalDinheiro"));
		}
		return caixaDataSaldos;
	}  
	
	public static String designIReportRelatorio() {

		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "OperacaoFinanceiraCaixaRel.jrxml");
	}

	public static String designIReportRelatorio2() {
		
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + "OperacaoFinanceiraCaixaRel2.jrxml");
	}

	public static String caminhoBaseIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}
	
	public void montarDadosContaCorrenteMovimentacaoFinanceira(OperacaoFinanceiraCaixaItemRelVO operacaoFinanceiraCaixaItemRelVO, int codigoContaCaixa,UsuarioVO usuarioLogadoVO) throws Exception {
		int codigoContaCorrenteConsultar = this.consultarCodigoContaCorrenteMovimentacaoFinanceira(codigoContaCaixa,operacaoFinanceiraCaixaItemRelVO.getCodigoOrigem());
		ContaCorrenteVO contaCorrenteVO = null;
		try {
		   contaCorrenteVO = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(codigoContaCorrenteConsultar, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioLogadoVO);
		} catch (Exception e) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		if (Uteis.isAtributoPreenchido(contaCorrenteVO) && Uteis.isAtributoPreenchido(contaCorrenteVO.getNomeApresentacaoSistema())) {
			operacaoFinanceiraCaixaItemRelVO.setNomeContaCorrenteMovimentacao(contaCorrenteVO.getNomeApresentacaoSistema());
		} else if(Uteis.isAtributoPreenchido(contaCorrenteVO)) {
			operacaoFinanceiraCaixaItemRelVO.setNomeContaCorrenteMovimentacao(contaCorrenteVO.getNumeroDigito());
		}else{
			operacaoFinanceiraCaixaItemRelVO.setNomeContaCorrenteMovimentacao("");
		}
	}
	
	public int consultarCodigoContaCorrenteMovimentacaoFinanceira(int contaCaixa,int codigoOrigemMovimentacao){
		StringBuilder sqlStr = new StringBuilder();
		int codigoContaCorrente = 0;
		 sqlStr.append("SELECT");
		 sqlStr.append(" contacorrenteorigem.codigo AS contacorrenteorigem,contacorrenteorigem.nomeapresentacaosistema AS contacorrenteorigem_nomeapresentacaosistema,");
		 sqlStr.append(" contacorrenteorigem.numero AS contacorrenteorigem_numero, contacorrentedestino.codigo AS contacorrentedestino,");
		 sqlStr.append(" contacorrentedestino.nomeapresentacaosistema AS contacorrentedestino_nomeapresentacaosistema,contacorrentedestino.numero  AS contacorrentedestino_numero ");
		 sqlStr.append("FROM movimentacaofinanceira ");
		 sqlStr.append("INNER JOIN contacorrente AS contacorrenteorigem   ON movimentacaofinanceira.contacorrenteorigem   = contacorrenteorigem.codigo ");
		 sqlStr.append("INNER JOIN contacorrente AS contacorrentedestino  ON movimentacaofinanceira.contacorrentedestino  = contacorrentedestino.codigo ");
		 sqlStr.append("WHERE movimentacaofinanceira.codigo =").append(codigoOrigemMovimentacao).append(";");
		 SqlRowSet rs  =  getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		 while (rs.next()) {
           if(rs.getInt("contacorrenteorigem") == contaCaixa && Uteis.isAtributoPreenchido(rs.getInt("contacorrentedestino"))){
        	   return rs.getInt("contacorrentedestino");
           }else if(rs.getInt("contacorrenteorigem") != contaCaixa && Uteis.isAtributoPreenchido(rs.getInt("contacorrenteorigem"))){
        	   return rs.getInt("contacorrenteorigem");
           }
		 }
	   return codigoContaCorrente;
    }
	
	
	
}
