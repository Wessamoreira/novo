package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ExtratoContaCorrenteVO;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoSacadoExtratoContaCorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import relatorio.negocio.comuns.financeiro.ExtratoContaCorrenteCartaoRelVO;
import relatorio.negocio.comuns.financeiro.ExtratoContaCorrenteRelVO;
import relatorio.negocio.interfaces.financeiro.ExtratoContaCorrenteRelInterfaceFacade;

@Service
@Lazy
public class ExtratoContaCorrenteRel extends ControleAcesso implements ExtratoContaCorrenteRelInterfaceFacade {

	/**
     * 
     */
	private static final long serialVersionUID = 7386501644059874759L;

	@Override
	public void validarDados(Date dataInicio, Date dataTermino, Integer contaCorrente, boolean consultaTela) throws ConsistirException {			
		if (dataInicio == null) {
			throw new ConsistirException("O campo PERÍODO INÍCIO deve ser informado.");
		}
		if (dataTermino == null) {
			throw new ConsistirException("O campo PERÍODO TÉRMINO deve ser informado.");
		}

		if (dataTermino.compareTo(dataInicio) < 0) {
			throw new ConsistirException("O campo PERÍODO TÉRMINO deve ser maior que o campo PERÍODO INÍCIO informado.");
		}
		if(consultaTela && !Uteis.isAtributoPreenchido(contaCorrente)) {
    		throw new ConsistirException("Para o recurso de consulta em tela deve ser informado especificamente uma conta corrente.");
    	}
		if(consultaTela && Uteis.getObterDiferencaDiasEntreDuasData(dataTermino, dataInicio) > 31) {
			throw new ConsistirException("Por favor informe um perído máximo de 30 dias.");
		}
	}

	@Override
	public List<ExtratoContaCorrenteRelVO> consultarDadosGeracaoRelatorio(Integer unidadeEnsino, Integer contaCorrente, Date dataInicio, Date dataTermino, Boolean listaTela) throws Exception {
		validarDados(dataInicio, dataTermino, contaCorrente, false);
		StringBuilder sql = new StringBuilder("");
		sql.append(" select banco.nome as banco, agencia.numeroAgencia || '-' || agencia.digito as agencia,contacorrente.nomeApresentacaoSistema, ");
		sql.append(" contacorrente.numero || '-' || contacorrente.digito as numerocontacorrente , ");
		sql.append(" extratocontacorrente.*, formaPagamento.nome as nomeFormaPagamento, formaPagamento.tipo AS tipoformapagamento, unidadeEnsino.nome as unidadeEnsino, ");
		sql.append(" case extratocontacorrente.origemExtratoContaCorrente ");
		sql.append(" when '").append(OrigemExtratoContaCorrenteEnum.RECEBIMENTO.toString()).append("' then exists (select codigo from negociacaorecebimento where codigo = extratocontacorrente.codigoOrigem ) ");
		sql.append(" when '").append(OrigemExtratoContaCorrenteEnum.PAGAMENTO.toString()).append("' then exists (select codigo from negociacaopagamento where codigo = extratocontacorrente.codigoOrigem ) ");
		sql.append(" when '").append(OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA.toString()).append("' then exists (select codigo from movimentacaofinanceira where codigo = extratocontacorrente.codigoOrigem ) ");
		sql.append(" when '").append(OrigemExtratoContaCorrenteEnum.DEVOLUCAO_CHEQUE.toString()).append("' then exists (select codigo from devolucaocheque where codigo = extratocontacorrente.codigoOrigem ) ");
		sql.append(" when '").append(OrigemExtratoContaCorrenteEnum.COMPENSACAO_CHEQUE.toString()).append("' then exists (select codigo from negociacaorecebimento where codigo = extratocontacorrente.codigoOrigem ) ");
		sql.append(" when '").append(OrigemExtratoContaCorrenteEnum.COMPENSACAO_CARTAO.toString()).append("' then exists (select codigo from negociacaorecebimento where codigo = extratocontacorrente.codigoOrigem ) ");
		sql.append(" else false end as possuiOrigem, ");
		
		sql.append(" case extratocontacorrente.origemExtratoContaCorrente ");
		sql.append(" when '").append(OrigemExtratoContaCorrenteEnum.RECEBIMENTO.toString()).append("' then exists (select codigo from negociacaorecebimento where codigo = extratocontacorrente.codigoOrigem and  recebimentoBoletoAutomatico = false ) ");
		sql.append(" else false end as lancamentoManual ");
		
		sql.append(" from extratocontacorrente  ");
		sql.append(" inner join contaCorrente on contaCorrente.codigo = extratocontacorrente.contaCorrente ");
		sql.append(" inner join agencia on agencia.codigo = contaCorrente.agencia ");
		sql.append(" inner join banco on banco.codigo = agencia.banco ");
		sql.append(" inner join formaPagamento on formaPagamento.codigo = extratocontacorrente.formaPagamento");
		sql.append(" left join unidadeEnsino on unidadeEnsino.codigo = extratocontacorrente.unidadeEnsino");
		sql.append(" where ");
		sql.append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "extratocontacorrente.data", true));
		sql.append(" and (extratocontacorrente.desconsiderarconciliacaobancaria is null or extratocontacorrente.desconsiderarconciliacaobancaria = false )");
		if (Uteis.isAtributoPreenchido(contaCorrente)) {
			if(Uteis.isAtributoPreenchido(unidadeEnsino)){
				sql.append(" and contaCorrente.codigo in (select contaCorrente from unidadeensinocontacorrente where unidadeEnsino = ").append(unidadeEnsino).append(" and contaCorrente = ").append(contaCorrente).append(" ) ");
			}else{
				sql.append(" and contaCorrente.codigo = ").append(contaCorrente);
			}
		} 
		if (Uteis.isAtributoPreenchido(unidadeEnsino) && !Uteis.isAtributoPreenchido(contaCorrente)) {
			sql.append(" and contaCorrente.codigo in (select contaCorrente from unidadeensinocontacorrente where unidadeEnsino = ").append(unidadeEnsino).append(") ");
		}
		sql.append(" order by banco.nome, agencia, numerocontacorrente, extratocontacorrente.data, extratocontacorrente.codigoOrigem ");

		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), contaCorrente, dataInicio, dataTermino, listaTela);
	}

	private List<ExtratoContaCorrenteRelVO> montarDadosConsulta(SqlRowSet rs, Integer contaCorrente, Date dataInicio, Date dataFim, Boolean listaTela) throws Exception {
		List<ExtratoContaCorrenteRelVO> extratoContaCorrenteRelVOs = new ArrayList<ExtratoContaCorrenteRelVO>(0);
		ExtratoContaCorrenteRelVO extratoContaCorrenteRelVO = null;
		while (rs.next()) {
			if (extratoContaCorrenteRelVO == null || extratoContaCorrenteRelVO.getCodigoContaCorrente().intValue() != rs.getInt("contaCorrente")) {
				extratoContaCorrenteRelVO = new ExtratoContaCorrenteRelVO();
				extratoContaCorrenteRelVO.setCodigoContaCorrente(rs.getInt("contaCorrente"));
				extratoContaCorrenteRelVO.setDataInicio(dataInicio);
				extratoContaCorrenteRelVO.setDataFinal(dataFim);
				extratoContaCorrenteRelVO.setBanco(rs.getString("banco"));
				extratoContaCorrenteRelVO.setAgencia(rs.getString("agencia"));
				if(Uteis.isAtributoPreenchido(rs.getString("nomeApresentacaoSistema"))){
					extratoContaCorrenteRelVO.setContaCorrente(rs.getString("nomeApresentacaoSistema"));
				}else{
					extratoContaCorrenteRelVO.setContaCorrente(rs.getString("numerocontacorrente"));
				}
				extratoContaCorrenteRelVO.setUnidadeEnsino(rs.getString("unidadeEnsino"));
				extratoContaCorrenteRelVO.setSaldoAnterior(consultarSaldoAnterior(rs.getInt("contaCorrente"), dataInicio));
				if(!listaTela) {
					extratoContaCorrenteRelVO.setChequeEntradaVOs(getFacadeFactory().getChequeFacade().consultarChequePendenteCompesacaoRecebimentoPorContaCorrente(rs.getInt("contaCorrente"), dataInicio, dataFim, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
					extratoContaCorrenteRelVO.setTotalChequeEntrada(getFacadeFactory().getChequeFacade().consultarTotalChequePendenteCompesacaoRecebimentoPorContaCorrente(rs.getInt("contaCorrente"), dataInicio, dataFim, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
				
					extratoContaCorrenteRelVO.setChequeSaidaVOs(getFacadeFactory().getChequeFacade().consultarChequePendenteCompesacaoChequeProprioPagamentoPorContaCorrente(rs.getInt("contaCorrente"), dataInicio, dataFim, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
					extratoContaCorrenteRelVO.setTotalChequeSaida(getFacadeFactory().getChequeFacade().consultarTotalChequePendenteCompesacaoChequeProprioPagamentoPorContaCorrente(rs.getInt("contaCorrente"), dataInicio, dataFim, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
				
					extratoContaCorrenteRelVO.setExtratoContaCorrenteCartaoRelVOs(consultarExtratoContaCorrenteCartao(rs.getInt("contaCorrente")));
					extratoContaCorrenteRelVO.setTotalCartao(consultarTotalExtratoContaCorrenteCartao(rs.getInt("contaCorrente")));
				
					extratoContaCorrenteRelVO.setChequeDevolvidoVOs(getFacadeFactory().getChequeFacade().consultarChequeDevolvidoPorContaCorrente(rs.getInt("contaCorrente"), dataInicio, dataFim, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
					extratoContaCorrenteRelVO.setTotalChequeDevolvido(getFacadeFactory().getChequeFacade().consultarTotalChequeDevolvidoPorContaCorrente(rs.getInt("contaCorrente"), dataInicio, dataFim, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
				}
				extratoContaCorrenteRelVOs.add(extratoContaCorrenteRelVO);
			}
			ExtratoContaCorrenteVO extratoContaCorrenteVO = montarDadosExtratoContaCorrente(rs);
			if (extratoContaCorrenteVO.getTipoMovimentacaoFinanceira().equals(TipoMovimentacaoFinanceira.ENTRADA)) {
				extratoContaCorrenteRelVO.setTotalEntrada(extratoContaCorrenteRelVO.getTotalEntrada() + extratoContaCorrenteVO.getValor());
			} else {
				extratoContaCorrenteRelVO.setTotalSaida(extratoContaCorrenteRelVO.getTotalSaida() + extratoContaCorrenteVO.getValor());
			}
			extratoContaCorrenteRelVO.getExtratoContaCorrenteVOs().add(extratoContaCorrenteVO);
		}
		if (extratoContaCorrenteRelVOs.isEmpty() && Uteis.isAtributoPreenchido(contaCorrente)) {
			extratoContaCorrenteRelVO = new ExtratoContaCorrenteRelVO();
			ContaCorrenteVO contaCorrenteVO = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(contaCorrente, false, Uteis.NIVELMONTARDADOS_TODOS, null);
			extratoContaCorrenteRelVO.setCodigoContaCorrente(contaCorrenteVO.getCodigo());
			extratoContaCorrenteRelVO.setDataInicio(dataInicio);
			extratoContaCorrenteRelVO.setDataFinal(dataFim);
			extratoContaCorrenteRelVO.setBanco(contaCorrenteVO.getAgencia().getBanco().getNome());
			extratoContaCorrenteRelVO.setAgencia(contaCorrenteVO.getAgencia().getNumeroAgencia()+" - "+contaCorrenteVO.getAgencia().getDigito());
			if(Uteis.isAtributoPreenchido(contaCorrenteVO.getNomeApresentacaoSistema())){
				extratoContaCorrenteRelVO.setContaCorrente(contaCorrenteVO.getNomeApresentacaoSistema());
			}else{
				extratoContaCorrenteRelVO.setContaCorrente(contaCorrenteVO.getNumero()+ " - "+contaCorrenteVO.getDigito());
			}
			extratoContaCorrenteRelVO.setSaldoAnterior(consultarSaldoAnterior(contaCorrenteVO.getCodigo(), dataInicio));
			if (!listaTela) {
				extratoContaCorrenteRelVO.setChequeEntradaVOs(getFacadeFactory().getChequeFacade().consultarChequePendenteCompesacaoRecebimentoPorContaCorrente(contaCorrente, dataInicio, dataFim, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
				extratoContaCorrenteRelVO.setTotalChequeEntrada(getFacadeFactory().getChequeFacade().consultarTotalChequePendenteCompesacaoRecebimentoPorContaCorrente(contaCorrente, dataInicio, dataFim, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));

				extratoContaCorrenteRelVO.setChequeSaidaVOs(getFacadeFactory().getChequeFacade().consultarChequePendenteCompesacaoChequeProprioPagamentoPorContaCorrente(contaCorrente, dataInicio, dataFim, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
				extratoContaCorrenteRelVO.setTotalChequeSaida(getFacadeFactory().getChequeFacade().consultarTotalChequePendenteCompesacaoChequeProprioPagamentoPorContaCorrente(contaCorrente, dataInicio, dataFim, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));

				extratoContaCorrenteRelVO.setChequeDevolvidoVOs(getFacadeFactory().getChequeFacade().consultarChequeDevolvidoPorContaCorrente(contaCorrente, dataInicio, dataFim, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
				extratoContaCorrenteRelVO.setTotalChequeDevolvido(getFacadeFactory().getChequeFacade().consultarTotalChequeDevolvidoPorContaCorrente(contaCorrente, dataInicio, dataFim, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));

				extratoContaCorrenteRelVO.setExtratoContaCorrenteCartaoRelVOs(consultarExtratoContaCorrenteCartao(contaCorrente));
				extratoContaCorrenteRelVO.setTotalCartao(consultarTotalExtratoContaCorrenteCartao(contaCorrente));			
			}
			extratoContaCorrenteRelVOs.add(extratoContaCorrenteRelVO);
			
		}
		realizarCalculoSaldoDiaDia(extratoContaCorrenteRelVOs);
		return extratoContaCorrenteRelVOs;
	}

	public ExtratoContaCorrenteVO montarDadosExtratoContaCorrente(SqlRowSet rs) {
		ExtratoContaCorrenteVO extratoContaCorrenteVO = new ExtratoContaCorrenteVO();
		extratoContaCorrenteVO.setCodigo(rs.getInt("codigo"));
		extratoContaCorrenteVO.setData(rs.getTimestamp("data"));
		extratoContaCorrenteVO.setValor(rs.getDouble("valor"));
		extratoContaCorrenteVO.setOrigemExtratoContaCorrente(OrigemExtratoContaCorrenteEnum.valueOf(rs.getString("origemExtratoContaCorrente")));
		extratoContaCorrenteVO.setTipoMovimentacaoFinanceira(TipoMovimentacaoFinanceira.valueOf(rs.getString("tipoMovimentacaoFinanceira")));
		extratoContaCorrenteVO.setCodigoOrigem(rs.getInt("codigoOrigem"));
		extratoContaCorrenteVO.setCodigoCheque(rs.getInt("codigoCheque"));
		extratoContaCorrenteVO.setSacadoCheque(rs.getString("sacadoCheque"));
		extratoContaCorrenteVO.setNumeroCheque(rs.getString("numeroCheque"));
		extratoContaCorrenteVO.setContaCorrenteCheque(rs.getString("contaCorrenteCheque"));
		extratoContaCorrenteVO.setBancoCheque(rs.getString("bancoCheque"));
		extratoContaCorrenteVO.setAgenciaCheque(rs.getString("agenciaCheque"));
		extratoContaCorrenteVO.setDataPrevisaoCheque(rs.getDate("dataPrevisaoCheque"));
		extratoContaCorrenteVO.setNomeSacado(rs.getString("nomeSacado"));
		extratoContaCorrenteVO.setCodigoSacado(rs.getInt("codigoSacado"));
		extratoContaCorrenteVO.setTipoSacado(TipoSacadoExtratoContaCorrenteEnum.valueOf(rs.getString("tipoSacado")));
		extratoContaCorrenteVO.getContaCorrente().setCodigo(rs.getInt("contaCorrente"));
		extratoContaCorrenteVO.getUnidadeEnsino().setCodigo(rs.getInt("unidadeEnsino"));
		extratoContaCorrenteVO.getResponsavel().setCodigo(rs.getInt("responsavel"));
		extratoContaCorrenteVO.getFormaPagamento().setCodigo(rs.getInt("formaPagamento"));
		extratoContaCorrenteVO.getFormaPagamento().setNome(rs.getString("nomeFormaPagamento"));
		extratoContaCorrenteVO.getFormaPagamento().setTipo(rs.getString("tipoformapagamento"));
		extratoContaCorrenteVO.getConciliacaoContaCorrenteDiaExtratoVO().setCodigo(rs.getInt("conciliacaocontacorrentediaextrato"));
		extratoContaCorrenteVO.setPossuiOrigem(rs.getBoolean("possuiOrigem"));
		extratoContaCorrenteVO.setLancamentoManual(rs.getBoolean("lancamentoManual"));
		extratoContaCorrenteVO.setValorTaxaBancaria(rs.getDouble("valorTaxaBancaria"));
		return extratoContaCorrenteVO;
	}

	public Double consultarSaldoAnterior(Integer contaCorrente, Date dataInicio) {
		if (dataInicio == null) {
			return 0.0;
		}
		StringBuilder sql = new StringBuilder("");
		sql.append(" select (sum(entrada) - sum(saida)) AS total from ( ");
		sql.append(" select contaCorrente, case when tipomovimentacaofinanceira = 'ENTRADA' then sum(valor-(case when valortaxabancaria is null then 0.0 else valortaxabancaria end)) else  0 end as entrada, ");
		sql.append(" case when tipomovimentacaofinanceira = 'SAIDA' then sum(valor-(case when valortaxabancaria is null then 0.0 else valortaxabancaria end)) else 0 end as saida ");
		sql.append(" from extratocontacorrente   ");
		sql.append(" where data < '").append(Uteis.getDataJDBCTimestamp(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataInicio))).append("' ");
		sql.append(" and contaCorrente = ").append(contaCorrente);
		sql.append(" and (extratocontacorrente.desconsiderarconciliacaobancaria is null or extratocontacorrente.desconsiderarconciliacaobancaria = false )");
		sql.append(" group by contaCorrente, tipomovimentacaofinanceira ");
		sql.append(" ) as t group by contacorrente ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getDouble("total");
		}
		return 0.0;
	}

	public Double consultarTotalExtratoContaCorrenteCartao(Integer contaCorrente) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select sum(valorparcela) as valor from formapagamentonegociacaorecebimentocartaocredito ");
		sql.append(" inner join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.codigo = formapagamentonegociacaorecebimentocartaocredito.formapagamentonegociacaorecebimento ");
		sql.append(" inner join OperadoraCartao on formapagamentonegociacaorecebimento.OperadoraCartao = OperadoraCartao.codigo ");
		sql.append(" where formapagamentonegociacaorecebimentocartaocredito.situacao = 'AR' ");
		if (!contaCorrente.equals(0)) {
			sql.append(" and formapagamentonegociacaorecebimento.contaCorrenteOperadoraCartao = ").append(contaCorrente);
		}
		sql.append(" group by formapagamentonegociacaorecebimentocartaocredito.datavencimento, OperadoraCartao.nome ");
		sql.append(" order by formapagamentonegociacaorecebimentocartaocredito.datavencimento, OperadoraCartao.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (rs.next()) {
			return rs.getDouble("valor");
		}
		return 0.0;
	}

	public List<ExtratoContaCorrenteCartaoRelVO> consultarExtratoContaCorrenteCartao(Integer contaCorrente) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select OperadoraCartao.nome as operadoraCartao, formapagamentonegociacaorecebimentocartaocredito.datavencimento, sum(valorparcela) as valor from formapagamentonegociacaorecebimentocartaocredito ");
		sql.append(" inner join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.codigo = formapagamentonegociacaorecebimentocartaocredito.formapagamentonegociacaorecebimento ");
		sql.append(" inner join OperadoraCartao on formapagamentonegociacaorecebimento.OperadoraCartao = OperadoraCartao.codigo ");
		sql.append(" where formapagamentonegociacaorecebimentocartaocredito.situacao = 'AR' ");
		if (!contaCorrente.equals(0)) {
			sql.append(" and formapagamentonegociacaorecebimento.contaCorrenteOperadoraCartao = ").append(contaCorrente);
		}
		sql.append(" group by formapagamentonegociacaorecebimentocartaocredito.datavencimento, OperadoraCartao.nome ");
		sql.append(" order by formapagamentonegociacaorecebimentocartaocredito.datavencimento, OperadoraCartao.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosExtratoContaCorrenteCartao(rs);
	}
	
	public List<ExtratoContaCorrenteCartaoRelVO> montarDadosExtratoContaCorrenteCartao(SqlRowSet rs) {
		List<ExtratoContaCorrenteCartaoRelVO> extratoContaCorrenteCartaoRelVOs = new ArrayList<ExtratoContaCorrenteCartaoRelVO>(0);
		while (rs.next()) {
			ExtratoContaCorrenteCartaoRelVO obj = new ExtratoContaCorrenteCartaoRelVO();
			obj.setOperadoraCartao(rs.getString("operadoraCartao"));
			obj.setDataVencimento(rs.getDate("datavencimento"));
			obj.setValor(rs.getDouble("valor"));
			extratoContaCorrenteCartaoRelVOs.add(obj);
		}

		return extratoContaCorrenteCartaoRelVOs;
	}

	public static String designIReportRelatorio() {
		return (caminhoBaseRelatorio() + getIdEntidade() + ".jrxml");
	}

	public static String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	public static String getIdEntidade() {
		return "ExtratoContaCorrenteRel";
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarTodosExtratoContaCorrente(ExtratoContaCorrenteRelVO extratoContaCorrenteRelVO, UsuarioVO usuarioVO) throws Exception {
		
			boolean teveAlteracao = false;
			for(ExtratoContaCorrenteVO extratoContaCorrenteVO: extratoContaCorrenteRelVO.getExtratoContaCorrenteVOs()) {
				if(!Uteis.getData(extratoContaCorrenteVO.getData()).equals(Uteis.getData(extratoContaCorrenteVO.getDataAlterar()))){
					teveAlteracao = true;
					getFacadeFactory().getExtratoContaCorrenteFacade().alterar(extratoContaCorrenteVO, usuarioVO);
				}
			}
		
		
			if(teveAlteracao) {
				realizarCalculoValorAlteracaoTodosExtratos(extratoContaCorrenteRelVO, usuarioVO);
				realizarCalculoSaldoDiaDia(extratoContaCorrenteRelVO);
				Ordenacao.ordenarLista(extratoContaCorrenteRelVO.getExtratoContaCorrenteVOs(), "data");
			}		
	}
	
	public void realizarCalculoValorAlteracaoTodosExtratos(ExtratoContaCorrenteRelVO extratoContaCorrenteRelVO, UsuarioVO usuarioVO) {
		for(ExtratoContaCorrenteVO extratoContaCorrenteVO: extratoContaCorrenteRelVO.getExtratoContaCorrenteVOs()) {
			if(!Uteis.getData(extratoContaCorrenteVO.getData()).equals(Uteis.getData(extratoContaCorrenteVO.getDataAlterar()))){
				extratoContaCorrenteVO.setData(extratoContaCorrenteVO.getDataAlterar());
				if(extratoContaCorrenteVO.getData().compareTo(extratoContaCorrenteRelVO.getDataInicio()) < 0 || 
						extratoContaCorrenteVO.getData().compareTo(extratoContaCorrenteRelVO.getDataFinal()) > 0) {
							getFacadeFactory().getExtratoContaCorrenteRelFacade().removerExtratoContaCorrente(extratoContaCorrenteRelVO, extratoContaCorrenteVO);							
							realizarCalculoValorAlteracaoTodosExtratos(extratoContaCorrenteRelVO, usuarioVO);
							return;
				}
			}
		}

	}
	
	@Override
	public void removerExtratoContaCorrente(List<ExtratoContaCorrenteRelVO> extratoContaCorrenteRelVOs, ExtratoContaCorrenteVO extratoContaCorrente) {
		for(ExtratoContaCorrenteRelVO extratoContaCorrenteRelVO: extratoContaCorrenteRelVOs) {
			if(removerExtratoContaCorrente(extratoContaCorrenteRelVO, extratoContaCorrente)) {
				break;
			}
		}
	}
	
	@Override
	public boolean removerExtratoContaCorrente(ExtratoContaCorrenteRelVO extratoContaCorrenteRelVO, ExtratoContaCorrenteVO extratoContaCorrente) {		
			if(extratoContaCorrenteRelVO.getCodigoContaCorrente().equals(extratoContaCorrente.getContaCorrente().getCodigo())) {
				int x = 0;
				for(ExtratoContaCorrenteVO extratoContaCorrenteVO: extratoContaCorrenteRelVO.getExtratoContaCorrenteVOs()) {
					if(extratoContaCorrenteVO.getCodigo().equals(extratoContaCorrente.getCodigo())) {
						if(extratoContaCorrenteVO.getData().compareTo(extratoContaCorrenteRelVO.getDataInicio()) < 0) {
							if(extratoContaCorrenteVO.getTipoMovimentacaoFinanceira().equals(TipoMovimentacaoFinanceira.ENTRADA)) {
								extratoContaCorrenteRelVO.setSaldoAnterior(extratoContaCorrenteRelVO.getSaldoAnterior()+extratoContaCorrenteVO.getValor());
							}else {
								extratoContaCorrenteRelVO.setSaldoAnterior(extratoContaCorrenteRelVO.getSaldoAnterior()-extratoContaCorrenteVO.getValor());
							}
							extratoContaCorrenteRelVO.setSaldoAnteriorApresentar(extratoContaCorrenteRelVO.getSaldoAnterior());							
						}
						if(extratoContaCorrente.getTipoMovimentacaoFinanceira().equals(TipoMovimentacaoFinanceira.SAIDA)) {
							extratoContaCorrenteRelVO.setTotalSaida(extratoContaCorrenteRelVO.getTotalSaida()-extratoContaCorrente.getValor());
						}else {
							extratoContaCorrenteRelVO.setTotalEntrada(extratoContaCorrenteRelVO.getTotalEntrada()-extratoContaCorrente.getValor());
						}
						extratoContaCorrenteRelVO.setSaldoFinalApresentar(null);
						extratoContaCorrenteRelVO.getExtratoContaCorrenteVOs().remove(x);
						realizarCalculoSaldoDiaDia(extratoContaCorrenteRelVO);
						return true;
					}
					x++;
				}
			}
			return false;
		
	}
	
	
	@Override
	public void adicionarExtratoContaCorrente(List<ExtratoContaCorrenteRelVO> extratoContaCorrenteRelVOs, ExtratoContaCorrenteVO extratoContaCorrente) {
		for (ExtratoContaCorrenteRelVO extratoContaCorrenteRelVO : extratoContaCorrenteRelVOs) {
			if (extratoContaCorrenteRelVO.getCodigoContaCorrente().equals(extratoContaCorrente.getContaCorrente().getCodigo())) {
				if (extratoContaCorrente.getData().compareTo(extratoContaCorrenteRelVO.getDataInicio()) >= 0 && extratoContaCorrente.getData().compareTo(extratoContaCorrenteRelVO.getDataFinal()) <= 0) {
					if (extratoContaCorrente.getTipoMovimentacaoFinanceira().equals(TipoMovimentacaoFinanceira.SAIDA)) {
						extratoContaCorrenteRelVO.setTotalSaida(extratoContaCorrenteRelVO.getTotalSaida() + extratoContaCorrente.getValor());
					} else {
						extratoContaCorrenteRelVO.setTotalEntrada(extratoContaCorrenteRelVO.getTotalEntrada() + extratoContaCorrente.getValor());
					}
					extratoContaCorrenteRelVO.getExtratoContaCorrenteVOs().add(extratoContaCorrente);
					Ordenacao.ordenarLista(extratoContaCorrenteRelVO.getExtratoContaCorrenteVOs(), "data");
					extratoContaCorrenteRelVO.setSaldoFinalApresentar(null);
				}
				realizarCalculoSaldoDiaDia(extratoContaCorrenteRelVO);
				break;

			}
		}

	}
	
	@Override
	public void realizarCalculoSaldoDiaDia(List<ExtratoContaCorrenteRelVO> extratoContaCorrenteRelVOs) {
		for (ExtratoContaCorrenteRelVO extratoContaCorrenteRelVO : extratoContaCorrenteRelVOs) {
			realizarCalculoSaldoDiaDia(extratoContaCorrenteRelVO);
		}
	}
	
	@Override
	public void realizarCalculoSaldoDiaDia(ExtratoContaCorrenteRelVO extratoContaCorrenteRelVO) {
		Double saldo = extratoContaCorrenteRelVO.getSaldoAnterior();
		for(ExtratoContaCorrenteVO extratoContaCorrenteVO: extratoContaCorrenteRelVO.getExtratoContaCorrenteVOs()) {
			if(extratoContaCorrenteVO.getTipoMovimentacaoFinanceira().equals(TipoMovimentacaoFinanceira.ENTRADA)) {
				saldo = Uteis.arrendondarForcando2CadasDecimais(saldo + extratoContaCorrenteVO.getValor());
			}else {
				saldo =  Uteis.arrendondarForcando2CadasDecimais(saldo - extratoContaCorrenteVO.getValor());
			}
			if(saldo.equals(-0.0)) {
				saldo = 0.0; 
			}
			extratoContaCorrenteVO.setSaldo(saldo);
		}
	}

}
