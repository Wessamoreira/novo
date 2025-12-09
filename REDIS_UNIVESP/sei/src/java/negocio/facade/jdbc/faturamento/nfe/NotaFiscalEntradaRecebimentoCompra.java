package negocio.facade.jdbc.faturamento.nfe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.ParcelaCondicaoPagamentoVO;
import negocio.comuns.compras.RecebimentoCompraVO;
import negocio.comuns.compras.enumeradores.TipoCriacaoContaPagarEnum;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaRecebimentoCompraVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO;
import negocio.comuns.faturamento.nfe.enumeradores.TipoNotaFiscalEntradaEnum;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.enumerador.OrdenarContaPagarEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.NotaFiscalEntradaRecebimentoCompraInterfaceFacade;

/**
 *
 * @author Pedro Otimize
 */
@Repository
@Scope("singleton")
@Lazy
public class NotaFiscalEntradaRecebimentoCompra extends ControleAcesso implements NotaFiscalEntradaRecebimentoCompraInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6991812374252641337L;
	protected static String idEntidade = "NotaFiscalEntradaRecebimentoCompra";

	public NotaFiscalEntradaRecebimentoCompra() {
		super();
	}

	private void validarDados(NotaFiscalEntradaRecebimentoCompraVO obj) {
		if (!Uteis.isAtributoPreenchido(obj.getRecebimentoCompraVO())) {
			throw new StreamSeiException("O campo Recebimento de Compra (NotaFiscalEntradaRecebimentoCompra) não foi informado.");
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<NotaFiscalEntradaRecebimentoCompraVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			for (NotaFiscalEntradaRecebimentoCompraVO obj : lista) {
				validarDados(obj);
				if (obj.getCodigo() == 0) {
					incluir(obj, verificarAcesso, usuarioVO);
				} else {
					alterar(obj, verificarAcesso, usuarioVO);
				}
				if (!obj.getRecebimentoCompraVO().getRecebimentoFinalizado()) {
					getFacadeFactory().getRecebimentoCompraFacade().persistir(obj.getRecebimentoCompraVO(), false, usuarioVO);
				}
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final NotaFiscalEntradaRecebimentoCompraVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			NotaFiscalEntradaRecebimentoCompra.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO NotaFiscalEntradaRecebimentoCompra (recebimentoCompra, notaFiscalEntrada) ");
			sql.append("    VALUES (?,?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getRecebimentoCompraVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNotaFiscalEntradaVO(), ++i, sqlInserir);
					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final NotaFiscalEntradaRecebimentoCompraVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			NotaFiscalEntradaRecebimentoCompra.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE NotaFiscalEntradaRecebimentoCompra ");
			sql.append("   SET recebimentoCompra=?, notaFiscalEntrada=? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getRecebimentoCompraVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNotaFiscalEntradaVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			}) == 0) {
				incluir(obj, false, usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(NotaFiscalEntradaRecebimentoCompraVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			NotaFiscalEntradaRecebimentoCompra.excluir(getIdEntidade(), verificarAcesso, usuario);
			String sql = "DELETE FROM NotaFiscalEntradaRecebimentoCompra WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}

	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDadosParaCriarContaPagar(NotaFiscalEntradaVO obj, UsuarioVO usuario) {
		if (Uteis.isAtributoPreenchido(obj.getCodigo()) && getFacadeFactory().getContaPagarFacade().consultarSeExisteContaPagarPagarOuParcialmentePagaPorCodOrigemPorTipoOrigem(obj.getCodigo().toString(), OrigemContaPagar.NOTA_FISCAL_ENTRADA.getValor(), usuario)) {
			throw new StreamSeiException("Existe uma conta pagar para essa Nota Fiscal de Entrada e a mesma esta paga, para que seja feita a operação é necessário primeiramente realizar o estorno da conta pagar.");
		}
		for (ContaPagarVO contaPagarVO : obj.getListaContaPagar()) {
			if (Uteis.isAtributoPreenchido(contaPagarVO)) {
				getFacadeFactory().getContaPagarFacade().validarSeContaPagarExisteVinculoComArquivoRemessa(contaPagarVO);
				obj.getListaContaPagarExcluidas().add(contaPagarVO);
			}
		}
		obj.getListaContaPagar().clear();
		List<ContaPagarVO> listaDeContaPagarPorRecebimentoCompra = new ArrayList<>();
		obj.getListaNotaFiscalEntradaRecebimentoCompra()
				.stream()
				.filter(NotaFiscalEntradaRecebimentoCompraVO::isExisteRecebimentoItemSelecionado)
				.forEach(p -> listaDeContaPagarPorRecebimentoCompra.addAll(gerarContaPagarPorNotaFiscalEntrada(obj, p)));
		if(Uteis.isAtributoPreenchido(listaDeContaPagarPorRecebimentoCompra)){
			preencherContaPagarAgrupandoPorVencimento(obj, usuario, listaDeContaPagarPorRecebimentoCompra);	
		}
		
	}

	private void preencherContaPagarAgrupandoPorVencimento(NotaFiscalEntradaVO obj, UsuarioVO usuario, List<ContaPagarVO> listaDeContaPagarPorRecebimentoCompra) {
		int parcela = 1;
		int totalParcela = 1;
		Map<Long, List<ContaPagarVO>> mapaContaPagarPorData = listaDeContaPagarPorRecebimentoCompra.stream().sorted(OrdenarContaPagarEnum.DATA_VENCIMENTO_LONG).collect(Collectors.groupingBy(ContaPagarVO::getDataVencimento_Time, LinkedHashMap::new, Collectors.toList()));
		totalParcela = mapaContaPagarPorData.entrySet().size();
		for (Map.Entry<Long, List<ContaPagarVO>> mapa : mapaContaPagarPorData.entrySet()) {
			for (ContaPagarVO p : mapa.getValue()) {
				p.setValor(Uteis.arrendondarForcando2CadasDecimais(p.getValor() - ((p.getValor() * obj.getValorTotalImpostoRetidoPorcentagem()) / 100)));
				p.setParcela(parcela + "/" + totalParcela);
				getFacadeFactory().getContaPagarFacade().adicionarContaPagarPorCentroResultadoOrigem(obj.getListaContaPagar(), obj.getListaCentroResultadoOrigemVOs(), p, usuario);
			}
			parcela++;
		}
	}

	@SuppressWarnings("unchecked")
	private List<ContaPagarVO> gerarContaPagarPorNotaFiscalEntrada(NotaFiscalEntradaVO obj, NotaFiscalEntradaRecebimentoCompraVO nferc) {
		try {
			if (nferc.getRecebimentoCompraVO().getTipoCriacaoContaPagarEnum().isNotaFiscalEntrada()) {
				nferc.getRecebimentoCompraVO().atualizarValorTotal();
				List<ParcelaCondicaoPagamentoVO> parcelaCondicaoPagamento = getFacadeFactory().getParcelaCondicaoPagamentoFacade().consultarParcelaCondicaoPagamentos(nferc.getRecebimentoCompraVO().getCompra().getCondicaoPagamento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS);
				if (!Uteis.isAtributoPreenchido(parcelaCondicaoPagamento)) {
					throw new StreamSeiException("Não foi encontrada a condição de pagamento para a Compra de Número : " + nferc.getRecebimentoCompraVO().getCompra().getCodigo());
				}
				return preencherContaPagar(parcelaCondicaoPagamento, obj, nferc);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
		return new ArrayList<>();
	}

	private List<ContaPagarVO> preencherContaPagar(List<ParcelaCondicaoPagamentoVO> listaCondicaoPagamento, NotaFiscalEntradaVO obj, NotaFiscalEntradaRecebimentoCompraVO nferc) {
		List<ContaPagarVO> listaContaPagar = new ArrayList<>();
		Double valorTotal = 0.0;
		try {
			for (int i = 0; i < listaCondicaoPagamento.size(); i++) {
				ParcelaCondicaoPagamentoVO pcp = listaCondicaoPagamento.get(i);
				ContaPagarVO contaPagar = new ContaPagarVO();
				contaPagar.getUnidadeEnsino().setCodigo(obj.getUnidadeEnsinoVO().getCodigo());
				contaPagar.getFornecedor().setCodigo(obj.getFornecedorVO().getCodigo());
				contaPagar.setTipoSacado(TipoSacado.FORNECEDOR.getValor());
				contaPagar.setSituacao("AP");
				contaPagar.setTipoOrigem(OrigemContaPagar.NOTA_FISCAL_ENTRADA.getValor());
				contaPagar.setDescricao("Pagamento gerado automático por Nota Fiscal de Entrada.");
				contaPagar.setNrDocumento(nferc.getRecebimentoCompraVO().getNumeroDocumentoPorRecebimentoCompra());
				if (Uteis.isAtributoPreenchido(pcp.getIntervalo())) {
					contaPagar.setDataVencimento(Uteis.obterDataFuturaMesContabilConsiderandoDiaInicial(new Date(), pcp.getIntervalo()));
				} else {
					contaPagar.setDataVencimento(new Date());
				}
				double valorParcela = Uteis.arrendondarForcando2CadasDecimais(nferc.getRecebimentoCompraVO().getValorTotalUtilizado() * (pcp.getPercentualValor() / 100));
				valorTotal = valorTotal + valorParcela;
				// Verificando se é a Ultima condicao de pagamento gerada
				if ((i + 1) == listaCondicaoPagamento.size()) {
					contaPagar.setValor(Uteis.arrendondarForcando2CadasDecimais(valorParcela + (nferc.getRecebimentoCompraVO().getValorTotalUtilizado() - valorTotal)));
				} else {
					contaPagar.setValor(valorParcela);
				}
				
				/*
				 * contaPagar.getCentroDespesa().setCodigo(nferc.getRecebimentoCompraVO().getCompra().getCategoriaDespesa().getCodigo()); contaPagar.getCentroDespesa().setDescricao(nferc.getRecebimentoCompraVO().getCompra().getCategoriaDespesa().getDescricao());
				 */
				
				
				
				
				listaContaPagar.add(contaPagar);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
		return listaContaPagar;
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void removerNotaFiscalEntradaRecebimentoCompraVONaoSelecionados(NotaFiscalEntradaVO obj, UsuarioVO usuario) {
		Iterator<NotaFiscalEntradaRecebimentoCompraVO> i = obj.getListaNotaFiscalEntradaRecebimentoCompra().iterator();
		while (i.hasNext()) {
			NotaFiscalEntradaRecebimentoCompraVO objExistente = i.next();
			if (!objExistente.isExisteRecebimentoItemSelecionado()) {
				if (Uteis.isAtributoPreenchido(objExistente)) {
					validarJuncaoRecebimentoCompraPorNotaFiscalEntrada(usuario, objExistente);
				}
				i.remove();
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void estornaNotaFiscalEntradaRecebimentoCompra(NotaFiscalEntradaVO obj, UsuarioVO usuario) {
		try {
			for (NotaFiscalEntradaRecebimentoCompraVO nferc : obj.getListaNotaFiscalEntradaRecebimentoCompra()) {
				validarJuncaoRecebimentoCompraPorNotaFiscalEntrada(usuario, nferc);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void validarJuncaoRecebimentoCompraPorNotaFiscalEntrada(UsuarioVO usuario, NotaFiscalEntradaRecebimentoCompraVO nferc) {
		try {
			excluir(nferc, false, usuario);
			if (!nferc.getRecebimentoCompraVO().getRecebimentoFinalizado()) {
				RecebimentoCompraVO rc = getFacadeFactory().getRecebimentoCompraFacade().consultarPorCompraPorSituacaoSemVinculoComNotaFiscalEntradaComCodigoDiferente(nferc.getRecebimentoCompraVO().getCodigo(), nferc.getRecebimentoCompraVO().getCompra().getCodigo(), "PR", Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
				if (Uteis.isAtributoPreenchido(rc)) {
					getFacadeFactory().getRecebimentoCompraFacade().excluir(nferc.getRecebimentoCompraVO(), false, usuario);
					getFacadeFactory().getRecebimentoCompraFacade().validarGeracaoNovoRecebimentoCompraOuAtualizacao(nferc.getRecebimentoCompraVO(), false, usuario);
				}
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT NotaFiscalEntradaRecebimentoCompra.codigo as \"NotaFiscalEntradaRecebimentoCompra.codigo\",  ");

		sql.append(" notaFiscalEntrada.codigo as \"notaFiscalEntrada.codigo\",  ");

		sql.append(" recebimentocompra.codigo as \"recebimentocompra.codigo\", recebimentocompra.data as \"recebimentocompra.data\", ");
		sql.append(" recebimentocompra.situacao as \"recebimentocompra.situacao\", recebimentocompra.valortotal as \"recebimentocompra.valortotal\", ");
		sql.append(" recebimentocompra.tipoCriacaoContaPagar as \"recebimentocompra.tipoCriacaoContaPagar\", ");

		sql.append(" compra.codigo as \"compra.codigo\", compra.data as \"compra.data\", compra.tipoCriacaoContaPagar as \"compra.tipoCriacaoContaPagar\", ");
		sql.append(" compra.situacaorecebimento as \"compra.situacaorecebimento\", compra.situacaofinanceira as \"compra.situacaofinanceira\", ");
		sql.append(" compra.cotacao as \"compra.cotacao\", compra.condicaopagamento as \"compra.condicaopagamento\",");

		sql.append(" fornecedor.codigo as \"fornecedor.codigo\", fornecedor.nome as \"fornecedor.nome\",  ");

		sql.append(" unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\",  ");

		sql.append(" unidadeEnsino_compra.codigo as \"unidadeEnsino_compra.codigo\", unidadeEnsino_compra.nome as \"unidadeEnsino_compra.nome\",  ");

		sql.append(" responsavel.codigo as \"responsavel.codigo\", responsavel.nome as \"responsavel.nome\",  ");

		sql.append(" responsavel_compra.codigo as \"responsavel_compra.codigo\", responsavel_compra.nome as \"responsavel_compra.nome\"  ");

		sql.append(" FROM NotaFiscalEntradaRecebimentoCompra ");
		sql.append(" inner join notaFiscalEntrada on notaFiscalEntrada.codigo = NotaFiscalEntradaRecebimentoCompra.notaFiscalEntrada");
		sql.append(" inner join recebimentoCompra on recebimentoCompra.codigo = NotaFiscalEntradaRecebimentoCompra.recebimentoCompra");
		sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = recebimentocompra.unidadeEnsino");
		sql.append(" inner join usuario as responsavel on responsavel.codigo = recebimentocompra.responsavel");
		sql.append(" inner join compra on compra.codigo = recebimentocompra.compra");
		sql.append(" inner join unidadeEnsino as unidadeEnsino_compra on unidadeEnsino_compra.codigo = compra.unidadeEnsino");
		sql.append(" inner join fornecedor on fornecedor.codigo = compra.fornecedor");
		sql.append(" inner join usuario as responsavel_compra on responsavel_compra.codigo = compra.responsavel");

		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaRecebimentoCompraVO> consultaRapidaPorCompra(Integer compra, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE NotaFiscalEntradaRecebimentoCompra.compra = ").append(compra).append(" ");
			sqlStr.append(" ORDER BY NotaFiscalEntradaRecebimentoCompra.codigo desc ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}

	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaRecebimentoCompraVO> consultaRapidaPorNotaFiscalEntrada(Integer notaFiscalEntrada, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE NotaFiscalEntradaRecebimentoCompra.notaFiscalEntrada = ").append(notaFiscalEntrada).append(" ");
			sqlStr.append(" ORDER BY NotaFiscalEntradaRecebimentoCompra.codigo ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public NotaFiscalEntradaRecebimentoCompraVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE NotaFiscalEntradaRecebimentoCompra.codigo = ").append(codigoPrm).append(" ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados ( NotaFiscalEntradaRecebimentoCompraVO ).");
			}
			NotaFiscalEntradaRecebimentoCompraVO obj = new NotaFiscalEntradaRecebimentoCompraVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private List<NotaFiscalEntradaRecebimentoCompraVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<NotaFiscalEntradaRecebimentoCompraVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			NotaFiscalEntradaRecebimentoCompraVO obj = new NotaFiscalEntradaRecebimentoCompraVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(NotaFiscalEntradaRecebimentoCompraVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		obj.setSelecionado(true);
		obj.setCodigo(dadosSQL.getInt("NotaFiscalEntradaRecebimentoCompra.codigo"));
		obj.getRecebimentoCompraVO().setCodigo(dadosSQL.getInt("recebimentoCompra.codigo"));
		obj.getNotaFiscalEntradaVO().setCodigo(dadosSQL.getInt("notaFiscalEntrada.codigo"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
		obj.getRecebimentoCompraVO().setCodigo(dadosSQL.getInt("recebimentocompra.codigo"));
		obj.getRecebimentoCompraVO().setData(dadosSQL.getTimestamp("recebimentocompra.data"));
		obj.getRecebimentoCompraVO().setSituacao(dadosSQL.getString("recebimentocompra.situacao"));
		obj.getRecebimentoCompraVO().setTipoCriacaoContaPagarEnum(TipoCriacaoContaPagarEnum.valueOf(dadosSQL.getString("recebimentocompra.tipoCriacaoContaPagar")));
		obj.getRecebimentoCompraVO().setValorTotal(dadosSQL.getDouble("recebimentocompra.valortotal"));

		obj.getRecebimentoCompraVO().getCompra().setCodigo(dadosSQL.getInt("compra.codigo"));
		obj.getRecebimentoCompraVO().getCompra().setData(dadosSQL.getTimestamp("compra.data"));
		obj.getRecebimentoCompraVO().getCompra().setSituacaoRecebimento(dadosSQL.getString("compra.situacaorecebimento"));
		obj.getRecebimentoCompraVO().getCompra().setSituacaoFinanceira(dadosSQL.getString("compra.situacaofinanceira"));
		obj.getRecebimentoCompraVO().getCompra().setTipoCriacaoContaPagarEnum(TipoCriacaoContaPagarEnum.valueOf(dadosSQL.getString("compra.tipoCriacaoContaPagar")));

		obj.getRecebimentoCompraVO().getCompra().getCotacao().setCodigo(dadosSQL.getInt("compra.cotacao"));
		obj.getRecebimentoCompraVO().getCompra().getCondicaoPagamento().setCodigo(dadosSQL.getInt("compra.condicaopagamento"));

		obj.getRecebimentoCompraVO().getCompra().getResponsavel().setCodigo(dadosSQL.getInt("responsavel_compra.codigo"));
		obj.getRecebimentoCompraVO().getCompra().getResponsavel().setNome(dadosSQL.getString("responsavel_compra.nome"));

		obj.getRecebimentoCompraVO().getCompra().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino_compra.codigo"));
		obj.getRecebimentoCompraVO().getCompra().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino_compra.nome"));

		obj.getRecebimentoCompraVO().getCompra().getFornecedor().setCodigo(dadosSQL.getInt("fornecedor.codigo"));
		obj.getRecebimentoCompraVO().getCompra().getFornecedor().setNome(dadosSQL.getString("fornecedor.nome"));

		obj.getRecebimentoCompraVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
		obj.getRecebimentoCompraVO().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));

		obj.getRecebimentoCompraVO().getResponsavel().setCodigo(dadosSQL.getInt("responsavel.codigo"));
		obj.getRecebimentoCompraVO().getResponsavel().setNome(dadosSQL.getString("responsavel.nome"));
		obj.getRecebimentoCompraVO().setRecebimentoCompraItemVOs(getFacadeFactory().getRecebimentoCompraItemFacade().consultaRapidaPorNotaFiscalEntradaRecebimentoCompraVO(obj, usuario));
		obj.getRecebimentoCompraVO().setNotaFiscalEntradaCompraVO(obj);
	}

	private StringBuilder getSQLPadraoConsultaCompra() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ");
		sql.append(" recebimentocompra.codigo as \"recebimentocompra.codigo\", recebimentocompra.data as \"recebimentocompra.data\", ");
		sql.append(" recebimentocompra.situacao as \"recebimentocompra.situacao\", recebimentocompra.valortotal as \"recebimentocompra.valortotal\", ");
		sql.append(" recebimentocompra.tipoCriacaoContaPagar as \"recebimentocompra.tipoCriacaoContaPagar\", ");

		sql.append(" compra.codigo as \"compra.codigo\", compra.data as \"compra.data\", compra.tipoCriacaoContaPagar as \"compra.tipoCriacaoContaPagar\",");
		sql.append(" compra.situacaorecebimento as \"compra.situacaorecebimento\", compra.situacaofinanceira as \"compra.situacaofinanceira\", ");
		sql.append(" compra.cotacao as \"compra.cotacao\", compra.condicaopagamento as \"compra.condicaopagamento\", ");

		sql.append(" fornecedor.codigo as \"fornecedor.codigo\", fornecedor.nome as \"fornecedor.nome\",  ");

		sql.append(" unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\",  ");

		sql.append(" unidadeEnsino_compra.codigo as \"unidadeEnsino_compra.codigo\", unidadeEnsino_compra.nome as \"unidadeEnsino_compra.nome\",  ");

		sql.append(" responsavel.codigo as \"responsavel.codigo\", responsavel.nome as \"responsavel.nome\",  ");

		sql.append(" responsavel_compra.codigo as \"responsavel_compra.codigo\", responsavel_compra.nome as \"responsavel_compra.nome\"  ");

		sql.append(" FROM recebimentocompra ");
		sql.append(" inner join usuario as responsavel on responsavel.codigo = recebimentocompra.responsavel");
		sql.append(" inner join unidadeEnsino on unidadeEnsino.codigo = recebimentocompra.unidadeEnsino");
		sql.append(" inner join compra on compra.codigo = recebimentocompra.compra");
		sql.append(" inner join unidadeEnsino as unidadeEnsino_compra on unidadeEnsino_compra.codigo = compra.unidadeEnsino");
		sql.append(" inner join fornecedor on fornecedor.codigo = compra.fornecedor");
		sql.append(" inner join usuario as responsavel_compra on responsavel_compra.codigo = compra.responsavel");

		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaRecebimentoCompraVO> consultarTodasRecebimentoComprasPendenteFornecedorPorNotaFiscalEntrada(NotaFiscalEntradaVO notaFiscalEntrada, boolean controlarAcesso, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaCompra();
			sqlStr.append(" WHERE fornecedor.codigo = ").append(notaFiscalEntrada.getFornecedorVO().getCodigo()).append(" ");
			sqlStr.append("and unidadeEnsino_compra.codigo = ").append(notaFiscalEntrada.getUnidadeEnsinoVO().getCodigo());
			sqlStr.append(" and NOT EXISTS ( ");
			sqlStr.append(" select notafiscalentradarecebimentocompra.codigo  ");
			sqlStr.append(" from notafiscalentradarecebimentocompra  ");
			sqlStr.append(" where notafiscalentradarecebimentocompra.recebimentocompra = recebimentocompra.codigo  ");
			sqlStr.append(" ) ");
			sqlStr.append(" ORDER BY recebimentocompra.codigo,  compra.codigo desc ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			List<NotaFiscalEntradaRecebimentoCompraVO> vetResultado = new ArrayList<>();
			while (tabelaResultado.next()) {
				NotaFiscalEntradaRecebimentoCompraVO obj = new NotaFiscalEntradaRecebimentoCompraVO();
				montarDadosBasicoCompra(obj, tabelaResultado);
				obj.setNotaFiscalEntradaVO(notaFiscalEntrada);
				obj.getRecebimentoCompraVO().getCompra().setFornecedor(notaFiscalEntrada.getFornecedorVO());
				vetResultado.add(obj);
			}
			return vetResultado;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}

	}

	private void montarDadosBasicoCompra(NotaFiscalEntradaRecebimentoCompraVO obj, SqlRowSet dadosSQL) {
		obj.getRecebimentoCompraVO().setCodigo(dadosSQL.getInt("recebimentocompra.codigo"));
		obj.getRecebimentoCompraVO().setData(dadosSQL.getTimestamp("recebimentocompra.data"));
		obj.getRecebimentoCompraVO().setSituacao(dadosSQL.getString("recebimentocompra.situacao"));
		obj.getRecebimentoCompraVO().setTipoCriacaoContaPagarEnum(TipoCriacaoContaPagarEnum.valueOf(dadosSQL.getString("recebimentocompra.tipoCriacaoContaPagar")));
		obj.getRecebimentoCompraVO().setValorTotal(dadosSQL.getDouble("recebimentocompra.valortotal"));

		obj.getRecebimentoCompraVO().getCompra().setCodigo(dadosSQL.getInt("compra.codigo"));
		obj.getRecebimentoCompraVO().getCompra().setData(dadosSQL.getTimestamp("compra.data"));
		obj.getRecebimentoCompraVO().getCompra().setSituacaoRecebimento(dadosSQL.getString("compra.situacaorecebimento"));
		obj.getRecebimentoCompraVO().getCompra().setSituacaoFinanceira(dadosSQL.getString("compra.situacaofinanceira"));
		obj.getRecebimentoCompraVO().getCompra().setTipoCriacaoContaPagarEnum(TipoCriacaoContaPagarEnum.valueOf(dadosSQL.getString("compra.tipoCriacaoContaPagar")));

		obj.getRecebimentoCompraVO().getCompra().getCotacao().setCodigo(dadosSQL.getInt("compra.cotacao"));
		obj.getRecebimentoCompraVO().getCompra().getCondicaoPagamento().setCodigo(dadosSQL.getInt("compra.condicaopagamento"));

		obj.getRecebimentoCompraVO().getCompra().getResponsavel().setCodigo(dadosSQL.getInt("responsavel_compra.codigo"));
		obj.getRecebimentoCompraVO().getCompra().getResponsavel().setNome(dadosSQL.getString("responsavel_compra.nome"));

		obj.getRecebimentoCompraVO().getCompra().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino_compra.codigo"));
		obj.getRecebimentoCompraVO().getCompra().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino_compra.nome"));

		obj.getRecebimentoCompraVO().getCompra().getFornecedor().setCodigo(dadosSQL.getInt("fornecedor.codigo"));
		obj.getRecebimentoCompraVO().getCompra().getFornecedor().setNome(dadosSQL.getString("fornecedor.nome"));

		obj.getRecebimentoCompraVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
		obj.getRecebimentoCompraVO().getUnidadeEnsino().setNome(dadosSQL.getString("unidadeEnsino.nome"));

		obj.getRecebimentoCompraVO().getResponsavel().setCodigo(dadosSQL.getInt("responsavel.codigo"));
		obj.getRecebimentoCompraVO().getResponsavel().setNome(dadosSQL.getString("responsavel.nome"));

		obj.getRecebimentoCompraVO().setNotaFiscalEntradaCompraVO(obj);

	}

	private StringBuilder getSQLPadraoConsultaBasicaPorRecebimentoCompra() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT NotaFiscalEntradaRecebimentoCompra.codigo as \"NotaFiscalEntradaRecebimentoCompra.codigo\",  ");

		sql.append(" notaFiscalEntrada.codigo as \"notaFiscalEntrada.codigo\", notaFiscalEntrada.dataEntrada as \"notaFiscalEntrada.dataEntrada\",  ");
		sql.append(" notaFiscalEntrada.dataEmissao as \"notaFiscalEntrada.dataEmissao\", notaFiscalEntrada.numero as \"notaFiscalEntrada.numero\", ");
		sql.append(" notaFiscalEntrada.serie as \"notaFiscalEntrada.serie\", notaFiscalEntrada.tipoNotaFiscalEntrada as \"notaFiscalEntrada.tipoNotaFiscalEntrada\" ");

		sql.append(" FROM NotaFiscalEntradaRecebimentoCompra ");
		sql.append(" inner join notaFiscalEntrada on notaFiscalEntrada.codigo = NotaFiscalEntradaRecebimentoCompra.notaFiscalEntrada");
		sql.append(" inner join recebimentoCompra on recebimentoCompra.codigo = NotaFiscalEntradaRecebimentoCompra.recebimentoCompra");

		return sql;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public NotaFiscalEntradaRecebimentoCompraVO consultarPorRecebimentoCompras(RecebimentoCompraVO recebimentoCompraVO, UsuarioVO usuario) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaBasicaPorRecebimentoCompra();
			sqlStr.append(" WHERE recebimentoCompra.codigo= ").append(recebimentoCompraVO.getCodigo()).append(" ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			NotaFiscalEntradaRecebimentoCompraVO obj = new NotaFiscalEntradaRecebimentoCompraVO();
			if (tabelaResultado.next()) {
				montarDadosBasicoPorRecebimentoCompra(obj, tabelaResultado);
				obj.getNotaFiscalEntradaVO().setUnidadeEnsinoVO(recebimentoCompraVO.getUnidadeEnsino());
				obj.getNotaFiscalEntradaVO().setFornecedorVO(recebimentoCompraVO.getCompra().getFornecedor());
				obj.setRecebimentoCompraVO(recebimentoCompraVO);
			}
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarDadosBasicoPorRecebimentoCompra(NotaFiscalEntradaRecebimentoCompraVO obj, SqlRowSet dadosSQL) {
		obj.setCodigo(dadosSQL.getInt("NotaFiscalEntradaRecebimentoCompra.codigo"));
		obj.getNotaFiscalEntradaVO().setCodigo(dadosSQL.getInt("notaFiscalEntrada.codigo"));
		obj.getNotaFiscalEntradaVO().setNumero(dadosSQL.getLong("notaFiscalEntrada.numero"));
		obj.getNotaFiscalEntradaVO().setSerie(dadosSQL.getString("notaFiscalEntrada.serie"));
		obj.getNotaFiscalEntradaVO().setTipoNotaFiscalEntradaEnum(TipoNotaFiscalEntradaEnum.valueOf(dadosSQL.getString("notaFiscalEntrada.tipoNotaFiscalEntrada")));
		obj.getNotaFiscalEntradaVO().setDataEntrada(dadosSQL.getTimestamp("notaFiscalEntrada.dataEntrada"));
		obj.getNotaFiscalEntradaVO().setDataEmissao(dadosSQL.getTimestamp("notaFiscalEntrada.dataEmissao"));

	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return idEntidade;
	}

}
