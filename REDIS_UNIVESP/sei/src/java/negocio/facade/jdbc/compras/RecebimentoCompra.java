package negocio.facade.jdbc.compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.ParcelaCondicaoPagamentoVO;
import negocio.comuns.compras.RecebimentoCompraItemVO;
import negocio.comuns.compras.RecebimentoCompraVO;
import negocio.comuns.compras.enumeradores.OperacaoEstoqueEnum;
import negocio.comuns.compras.enumeradores.TipoCriacaoContaPagarEnum;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.SituacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.RecebimentoCompraInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>RecebimentoCompraVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>RecebimentoCompraVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see RecebimentoCompraVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class RecebimentoCompra extends ControleAcesso implements RecebimentoCompraInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5898323321948714065L;
	protected static String idEntidade = "RecebimentoCompra";

	public RecebimentoCompra() {
		super();
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(RecebimentoCompraVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
		obj.atualizarValorTotal();
		if (obj.getRecebimentoFinalizado()) {
			obj.setData(new Date());
		}
		if (obj.getCodigo() == 0) {
			incluir(obj, verificarAcesso, usuarioVO);
		} else {
			alterar(obj, verificarAcesso, usuarioVO);
		}
		validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getRecebimentoCompraItemVOs(), "RecebimentoCompraItem", idEntidade, obj.getCodigo(), usuarioVO);
		getFacadeFactory().getRecebimentoCompraItemFacade().persistir(obj.getRecebimentoCompraItemVOs(), false, usuarioVO);
		atualizarDadosCompraValidandoGeracaoNovoRecebimentoCompra(obj, usuarioVO);
		validarDadosParaCriarContaPagar(obj, usuarioVO);	
		
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final RecebimentoCompraVO obj, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {
			RecebimentoCompra.incluir(getIdEntidade(), controlarAcesso, usuario);
			RecebimentoCompraVO.validarDados(obj);
			obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO RecebimentoCompra( compra, data, responsavel, situacao, valorTotal, tipoCriacaoContaPagar, unidadeEnsino ) VALUES (?, ?, ?, ?, ?, ?, ? ) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection arg0) throws SQLException {
					final PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					if (obj.getCompra().getCodigo().intValue() != 0) {
						sqlInserir.setInt(1, obj.getCompra().getCodigo().intValue());
					} else {
						sqlInserir.setNull(1, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getCompra().getDataPrevisaoEntrega())) {
						sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getCompra().getDataPrevisaoEntrega()));
					} else {
						sqlInserir.setNull(2, 0);
					}
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(3, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(3, 0);
					}
					sqlInserir.setString(4, obj.getSituacao());
					sqlInserir.setDouble(5, obj.getValorTotal().doubleValue());
					sqlInserir.setString(6, obj.getTipoCriacaoContaPagarEnum().name());
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlInserir.setInt(7, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						sqlInserir.setNull(7, 0);
					}

					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {
				public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final RecebimentoCompraVO obj, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {

			RecebimentoCompraVO.validarDados(obj);
			RecebimentoCompra.alterar(getIdEntidade(), controlarAcesso, usuario);
			obj.realizarUpperCaseDados();
			final String sql = "UPDATE RecebimentoCompra set compra=?, data=?, responsavel=?, situacao=?, valorTotal=?, tipoCriacaoContaPagar=?, unidadeEnsino=? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					if (obj.getCompra().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(1, obj.getCompra().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(1, 0);
					}
					if (Uteis.isAtributoPreenchido(obj.getData())) {
						sqlAlterar.setDate(2, Uteis.getDataJDBC(obj.getData()));
					} else {
						sqlAlterar.setNull(2, 0);
					}
					
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(3, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlAlterar.setNull(3, 0);
					}
					sqlAlterar.setString(4, obj.getSituacao());
					sqlAlterar.setDouble(5, obj.getValorTotal().doubleValue());
					sqlAlterar.setString(6, obj.getTipoCriacaoContaPagarEnum().name());
					if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
						sqlAlterar.setInt(7, obj.getUnidadeEnsino().getCodigo().intValue());
					} else {
						throw new StreamSeiException("Unidade Ensino nula");
					}
					sqlAlterar.setInt(8, obj.getCodigo().intValue());
					return sqlAlterar;
				}
			});
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public RecebimentoCompraVO estornar(RecebimentoCompraVO obj, UsuarioVO usuarioVO) {
		try {
			RecebimentoCompraVO rc = consultarPorCompraPorSituacaoSemVinculoComNotaFiscalEntradaComCodigoDiferente(obj.getCodigo(), obj.getCompra().getCodigo(), "PR", Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			if (Uteis.isAtributoPreenchido(rc)) {
				excluir(obj, false, usuarioVO);		
				atualizacaoRecebimentoCompra(obj, rc, usuarioVO);						
				return new RecebimentoCompraVO();
			}else{
				prepararDadosParaExclusaoOuEstorno(obj, usuarioVO);
				obj.setSituacao("PR");
				if(Uteis.isAtributoPreenchido(obj.getCompra().getDataPrevisaoEntrega())) {
					obj.setData(obj.getCompra().getDataPrevisaoEntrega());
				}else {
					obj.setData(null);
				}
				
				getConexao().getJdbcTemplate().update("UPDATE RecebimentoCompra set situacao=?, data=? WHERE codigo = ? " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), obj.getSituacao(), obj.getData(), obj.getCodigo());
				return obj ;
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(RecebimentoCompraVO obj, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		RecebimentoCompra.excluir(getIdEntidade(), controlarAcesso, usuarioVO);
		prepararDadosParaExclusaoOuEstorno(obj, usuarioVO);
		String sql = "DELETE FROM RecebimentoCompra WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
	}

	private void prepararDadosParaExclusaoOuEstorno(RecebimentoCompraVO obj, UsuarioVO usuarioVO) throws Exception {
		if (obj.getTipoCriacaoContaPagarEnum().isRecebimentoCompra()) {
			if (getFacadeFactory().getContaPagarFacade().consultarSeExisteContaPagarPagarOuParcialmentePagaPorCodOrigemPorTipoOrigem(obj.getCodigo().toString(), OrigemContaPagar.RECEBIMENTO_COMPRA.getValor(), usuarioVO)) {
				throw new StreamSeiException("Existe uma conta pagar para esse recebimento de compra e a mesma esta paga, para que seja feita o estorno é necessário primeiramente realizar o estorno da conta pagar.");
			}
			getFacadeFactory().getContaPagarFacade().excluirContaPagarPorTipoOrigemPorCodigoOrigem(OrigemContaPagar.RECEBIMENTO_COMPRA.getValor(), obj.getCodigo().toString(), usuarioVO);
			boolean existeContaPagarCompra = getFacadeFactory().getContaPagarFacade().consultarContaPagarVinculadasACompraPorCodOrigemContaPagarPorSituacaoContaPagarComCodigoDiferenteContaPagar(obj.getCodigo().toString(), SituacaoFinanceira.PAGO.getValor(), 0, OrigemContaPagar.RECEBIMENTO_COMPRA, usuarioVO);
			if (!existeContaPagarCompra) {
				getFacadeFactory().getCompraFacade().alterarSituacaoFinanceira(obj.getCompra().getCodigo(), SituacaoFinanceira.A_PAGAR.getValor());
			} else {
				getFacadeFactory().getCompraFacade().alterarSituacaoFinanceira(obj.getCompra().getCodigo(), SituacaoFinanceira.PAGO_PARCIAL.getValor());
			}
		}
		for (RecebimentoCompraItemVO rci : obj.getRecebimentoCompraItemVOs()) {
			getFacadeFactory().getRecebimentoCompraItemFacade().atualizarCompraItem(rci, obj.getUnidadeEnsino().getCodigo(), OperacaoEstoqueEnum.EXCLUIR, usuarioVO);
		}
		if (consultarSeExisteRecebimentoCompraPorCompraPorSituacaoComCodigoDiferenteDoRecebimento(obj.getCompra().getCodigo(), "EF", obj.getCodigo(), usuarioVO)) {
			getFacadeFactory().getCompraFacade().alterarSituacaoEntrega(obj.getCompra().getCodigo(), "PA");
		} else {
			getFacadeFactory().getCompraFacade().alterarSituacaoEntrega(obj.getCompra().getCodigo(), "PE");
		}		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluirPorCodigoCompra(CompraVO compra, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		for (RecebimentoCompraVO rc : compra.getRecebimentoCompraVOs()) {
			if (rc.getTipoCriacaoContaPagarEnum().isRecebimentoCompra()) {
				if (getFacadeFactory().getContaPagarFacade().consultarSeExisteContaPagarPagarOuParcialmentePagaPorCodOrigemPorTipoOrigem(rc.getCodigo().toString(), OrigemContaPagar.RECEBIMENTO_COMPRA.getValor(), usuarioVO)) {
					throw new StreamSeiException("Existe uma conta pagar para esse recebimento de compra e a mesma esta paga, para que seja feita o estorno é necessário primeiramente realizar o estorno da conta pagar.");
				}
				getFacadeFactory().getContaPagarFacade().excluirContaPagarPorTipoOrigemPorCodigoOrigem(OrigemContaPagar.RECEBIMENTO_COMPRA.getValor(), rc.getCodigo().toString(), usuarioVO);
			}
			for (RecebimentoCompraItemVO rci : rc.getRecebimentoCompraItemVOs()) {
				getFacadeFactory().getRecebimentoCompraItemFacade().atualizarCompraItem(rci, rc.getUnidadeEnsino().getCodigo(), OperacaoEstoqueEnum.EXCLUIR, usuarioVO);
			}
			String sql = "DELETE FROM RecebimentoCompra WHERE ((codigo = ?)) " + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
			getConexao().getJdbcTemplate().update(sql, rc.getCodigo());
		}
	}

	private void atualizarDadosCompraValidandoGeracaoNovoRecebimentoCompra(RecebimentoCompraVO obj, UsuarioVO usuario) throws Exception {
		String situacao = null;
		validarGeracaoNovoRecebimentoCompraOuAtualizacao(obj, true, usuario);
		if (obj.getRecebimentoFinalizado()) {
			situacao = consultarSeExisteRecebimentoCompraPorCompraPorSituacao(obj.getCompra().getCodigo(), "PR", usuario) ? "PA" : "FI";
		} else {
			situacao = consultarSeExisteRecebimentoCompraPorCompraPorSituacao(obj.getCompra().getCodigo(), "EF", usuario) ? "PA" : "PE";
		}
		obj.getCompra().setSituacaoRecebimento(situacao);
		getFacadeFactory().getCompraFacade().alterarSituacaoEntrega(obj.getCompra().getCodigo(), obj.getCompra().getSituacaoRecebimento());
		obj.getRecebimentoCompraItemVOs().stream().forEach(p -> p.setQuantidadeRecebidaAntesAlteracao(p.getQuantidadeRecebida()));
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void validarGeracaoNovoRecebimentoCompraOuAtualizacao(RecebimentoCompraVO obj, boolean atualizarRecebimentoItem, UsuarioVO usuario) {
		try {
			RecebimentoCompraVO rc = consultarPorCompraPorSituacaoSemVinculoComNotaFiscalEntradaComCodigoDiferente(obj.getCodigo(), obj.getCompra().getCodigo(), "PR", Uteis.NIVELMONTARDADOS_TODOS, usuario);
			if (Uteis.isAtributoPreenchido(rc)) {
				atualizarRecebimentoCompra(rc, obj, atualizarRecebimentoItem, usuario);
			} else {
				preencherNovoRecebimentoCompra(obj, usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizacaoRecebimentoCompra(RecebimentoCompraVO obj, RecebimentoCompraVO recebimentoCompraNovo, UsuarioVO usuario) {
		try {
			atualizarRecebimentoCompra(recebimentoCompraNovo, obj, false, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void atualizarRecebimentoCompra(RecebimentoCompraVO rc, RecebimentoCompraVO obj, boolean atualizarRecebimentoItem, UsuarioVO usuario) throws Exception {
		boolean persistir = false;
		for (RecebimentoCompraItemVO objExistente : obj.getRecebimentoCompraItemVOs()) {
			if (!atualizarRecebimentoItem) {
				RecebimentoCompraItemVO clone = objExistente.getClone();
				adicionarRecebimentoCompraItemVO(rc, clone);
			} else {
				if (!objExistente.getQuantidadeRecebida().equals(objExistente.getQuantidadeRecebidaAntesAlteracao())) {
					atualizarRecebimentoCompraItemVO(rc, objExistente, Uteis.arrendondarForcando2CadasDecimais(objExistente.getQuantidadeRecebidaAntesAlteracao() - objExistente.getQuantidadeRecebida()));
					persistir = true;
				}
			}
		}
		if (!atualizarRecebimentoItem || persistir) {
			if (Uteis.isAtributoPreenchido(rc.getRecebimentoCompraItemVOs())) {
				persistir(rc, false, usuario);
			} else {
				excluir(rc, false, usuario);
			}
		}
	}

	private void preencherNovoRecebimentoCompra(RecebimentoCompraVO obj, UsuarioVO usuario) throws Exception {
		StringBuilder listaCodigoDeletar = new StringBuilder();
		RecebimentoCompraVO rc = new RecebimentoCompraVO("PR", obj.getCompra());
		for (RecebimentoCompraItemVO p : obj.getRecebimentoCompraItemVOs()) {
			RecebimentoCompraItemVO recebimentoItem = new RecebimentoCompraItemVO();
			// Vindo da tela de nota fiscal de entrada caso o recebimento de compra ainda nao foi efetivado tem que zera a quantidade recebida e guarda o codigo para deletar o mesmo desse recebimento de compra para os itens que nao foram selecionados - Pedro Andrade .
			if (!p.isSelecionado()) {
				p.setQuantidadeRecebida(0.0);
				listaCodigoDeletar.append(listaCodigoDeletar.toString().isEmpty() ? p.getCodigo() : "," + p.getCodigo());
			}
			Double quantidadeTotalRecebida = getFacadeFactory().getRecebimentoCompraItemFacade().consultarQuantidadeRecebibaPorCompraItemComCodigoDiferenteRecebimentoCompraItem(p.getCompraItem().getCodigo(), p.getCodigo(), usuario);
			recebimentoItem.setQuantidadeRecebida(p.getCompraItem().getQuantidade() - (p.getQuantidadeRecebida() + quantidadeTotalRecebida));
			if (recebimentoItem.getQuantidadeRecebida() > 0.0) {
				recebimentoItem.setRecebimentoCompraVO(rc);
				recebimentoItem.setCompraItem(p.getCompraItem());
				recebimentoItem.setValorUnitario(p.getCompraItem().getPrecoUnitario());
				recebimentoItem.setValorTotal(p.getCompraItem().getPrecoUnitario() * recebimentoItem.getQuantidadeRecebida());
				rc.setValorTotal(rc.getValorTotal() + recebimentoItem.getValorTotal());
				rc.getRecebimentoCompraItemVOs().add(recebimentoItem);

			} else if (p.getCompraItem().getQuantidade() - (p.getQuantidadeRecebida() + quantidadeTotalRecebida) < 0.0) {
				throw new StreamSeiException("A quantidade do produto " + p.getCompraItem().getProduto().getNome() + " não pode ser maior que " + (p.getCompraItem().getQuantidade() - quantidadeTotalRecebida) + ", pois o mesmo já existe em outros recebimentos de compras.");
			}
		}
		if (!listaCodigoDeletar.toString().isEmpty()) {
			getFacadeFactory().getRecebimentoCompraItemFacade().excluirRecebimentoCompraItem(listaCodigoDeletar.toString(), usuario);
		}
		if (Uteis.isAtributoPreenchido(rc.getRecebimentoCompraItemVOs())) {
			getFacadeFactory().getRecebimentoCompraFacade().persistir(rc, false, usuario);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarCampoValortotal(RecebimentoCompraVO obj, UsuarioVO usuario) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append(" UPDATE RecebimentoCompra SET valorTotal= ").append(obj.getValorTotal()).append(" ");
		sqlStr.append(" WHERE codigo = ").append(obj.getCodigo()).append("");
		sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
		getConexao().getJdbcTemplate().update(sqlStr.toString());
	}

	
	private void validarDadosParaCriarContaPagar(RecebimentoCompraVO recebimentoCompra, UsuarioVO usuario) {
		try {
			if (recebimentoCompra.getRecebimentoFinalizado() && recebimentoCompra.getValorTotal() > 0.0 && recebimentoCompra.getTipoCriacaoContaPagarEnum().isRecebimentoCompra()) {
				List<ParcelaCondicaoPagamentoVO> listaParcelaCondicaoPagamento = getFacadeFactory().getParcelaCondicaoPagamentoFacade().consultarParcelaCondicaoPagamentos(recebimentoCompra.getCompra().getCondicaoPagamento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS);
				if (!Uteis.isAtributoPreenchido(listaParcelaCondicaoPagamento)) {
					throw new StreamSeiException("Não foi encontrada a condição de pagamento para compra de Número : " + recebimentoCompra.getCompra().getCodigo());
				}
				recebimentoCompra.getCompra().setListaCentroResultadoOrigemVOs(getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(recebimentoCompra.getCompra().getCodigo().toString(), TipoCentroResultadoOrigemEnum.COMPRA, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
				if (!Uteis.isAtributoPreenchido(recebimentoCompra.getCompra().getListaCentroResultadoOrigemVOs())) {
					throw new StreamSeiException("Não foi encontrado o centro de resultado origem para compra de Número : " + recebimentoCompra.getCompra().getCodigo());
				}

				if (getFacadeFactory().getContaPagarFacade().consultarSeExisteContaPagarPagarOuParcialmentePagaPorCodOrigemPorTipoOrigem(recebimentoCompra.getCodigo().toString(), OrigemContaPagar.RECEBIMENTO_COMPRA.getValor(), usuario)) {
					throw new StreamSeiException("Existe uma conta pagar para esse recebimento de compra e a mesma esta paga, para que seja feita a operação é necessário primeiramente realizar o estorno da conta pagar.");
				}
				getFacadeFactory().getContaPagarFacade().excluirContaPagarPorTipoOrigemPorCodigoOrigem(OrigemContaPagar.RECEBIMENTO_COMPRA.getValor(), recebimentoCompra.getCodigo().toString(), usuario);
				preencherContaPagar(listaParcelaCondicaoPagamento, recebimentoCompra, 1, listaParcelaCondicaoPagamento.size(), usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void preencherContaPagar(List<ParcelaCondicaoPagamentoVO> parcelaCondicaoPagamento, RecebimentoCompraVO recebimentoCompra, int parcela, int totalParcela, UsuarioVO usuarioVO) {
		Double valorTotal = 0.0;
		try {
			recebimentoCompra.atualizarValorTotal();			
			for (ParcelaCondicaoPagamentoVO pcp : parcelaCondicaoPagamento) {
				double valorParcela = 0;
				ContaPagarVO contaPagar = new ContaPagarVO();
				contaPagar.setParcela(parcela + "/" + totalParcela);
				if (pcp.getIntervalo().intValue() > 0) {
					contaPagar.setDataVencimento(Uteis.obterDataFuturaMesContabilConsiderandoDiaInicial(new Date(), pcp.getIntervalo()));
				} else {
					contaPagar.setDataVencimento(new Date());
				}
				valorParcela = Uteis.arrendondarForcando2CadasDecimais(recebimentoCompra.getValorTotal() * (pcp.getPercentualValor() / 100));
				valorTotal = valorTotal + valorParcela;
				if (parcela == totalParcela) {
					contaPagar.setValor(Uteis.arrendondarForcando2CadasDecimais(valorParcela + (recebimentoCompra.getValorTotal() - valorTotal)));
				} else {
					contaPagar.setValor(valorParcela);
				}
				contaPagar.setFornecedor(recebimentoCompra.getCompra().getFornecedor());
				contaPagar.setTipoSacado(TipoSacado.FORNECEDOR.getValor());
				contaPagar.setSituacao("AP");
				contaPagar.setUnidadeEnsino(recebimentoCompra.getCompra().getUnidadeEnsino());
				contaPagar.setCodOrigem(String.valueOf(recebimentoCompra.getCodigo()));
				contaPagar.setTipoOrigem(OrigemContaPagar.RECEBIMENTO_COMPRA.getValor());
				contaPagar.setNrDocumento(parcela + recebimentoCompra.getNumeroDocumentoPorRecebimentoCompra());
				contaPagar.setDescricao("Pagamento gerado automático por Recebimento de Compra.");
				getFacadeFactory().getContaPagarFacade().atualizarCentroResultadoOrigemContaPagar(recebimentoCompra.getCompra().getListaCentroResultadoOrigemVOs(), contaPagar, usuarioVO);
				getFacadeFactory().getContaPagarFacade().incluir(contaPagar, false, true, usuarioVO);
				parcela++;
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void adicionarRecebimentoCompraItemVO(RecebimentoCompraVO obj, RecebimentoCompraItemVO rci) {
		int index = 0;
		rci.setRecebimentoCompraVO(obj);
		for (RecebimentoCompraItemVO objExistente : obj.getRecebimentoCompraItemVOs()) {
			if (objExistente.equalsCampoSelecaoLista(rci)) {
				objExistente.setQuantidadeRecebida(objExistente.getQuantidadeRecebida() + rci.getQuantidadeRecebida());	
				objExistente.getCompraItem().setQuantidadeRecebida(objExistente.getCompraItem().getQuantidadeRecebida() - rci.getQuantidadeRecebida());
				obj.getRecebimentoCompraItemVOs().set(index, objExistente);
				return;
			}
			index++;
		}
		obj.getRecebimentoCompraItemVOs().add(rci);
	}

	private void atualizarRecebimentoCompraItemVO(RecebimentoCompraVO obj, RecebimentoCompraItemVO rci, Double quantidadeAlterada) {
		int index = 0;
		Iterator<RecebimentoCompraItemVO> i = obj.getRecebimentoCompraItemVOs().iterator();
		while (i.hasNext()) {
			RecebimentoCompraItemVO objExistente = i.next();
			if (objExistente.equalsCampoSelecaoLista(rci)) {
				objExistente.setQuantidadeRecebida(objExistente.getQuantidadeRecebida() + quantidadeAlterada);
				if (!Uteis.isAtributoPreenchido(objExistente.getQuantidadeRecebida())) {
					i.remove();
					return;
				}
				obj.getRecebimentoCompraItemVOs().set(index, objExistente);
				return;
			}
			index++;
		}
		RecebimentoCompraItemVO clone = rci.getClone();
		clone.setRecebimentoCompraVO(obj);
		clone.setQuantidadeRecebida(quantidadeAlterada);
		obj.getRecebimentoCompraItemVOs().add(clone);
	}
	
	public List consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM RecebimentoCompra WHERE codigo = " + valorConsulta.intValue() + " ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		sqlStr += " ORDER BY codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	
	public List consultarPorNomeFornecedor(String valorConsulta, Integer unidadeEnsino, String situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RecebimentoCompra.* FROM RecebimentoCompra, Compra, Fornecedor " + " WHERE RecebimentoCompra.compra = Compra.codigo and Compra.fornecedor = Fornecedor.codigo and upper( Fornecedor.nome ) like('" + valorConsulta.toUpperCase() + "%') "; 
		sqlStr = montarFiltrosConsulta(unidadeEnsino, situacao, dataIni, dataFim, sqlStr);
		sqlStr += " ORDER BY Fornecedor.nome, compra.codigo desc ";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	
	public List consultarPorCodigoCompra(Integer valorConsulta, Integer unidadeEnsino, String situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT RecebimentoCompra.*, Compra.codigo FROM RecebimentoCompra, Compra WHERE RecebimentoCompra.compra = Compra.codigo and Compra.codigo " + ((valorConsulta.intValue() == 0) ? ">" : "") + "= " + valorConsulta.intValue() + " ";
		sqlStr = montarFiltrosConsulta(unidadeEnsino, situacao, dataIni, dataFim, sqlStr);
		sqlStr += " ORDER BY RecebimentoCompra.codigo, compra.codigo desc";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	private String montarFiltrosConsulta(Integer unidadeEnsino, String situacao, Date dataIni, Date dataFim, String sqlStr) {
		if (Uteis.isAtributoPreenchido(situacao) && situacao.equals("EF") && ((dataIni != null) || (dataFim != null))) {
			sqlStr += " and recebimentocompra.data between '" + Uteis.getDataJDBC(dataIni) + "' and '" + Uteis.getDataJDBC(dataFim) + "' ";
		}
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += " and RecebimentoCompra.unidadeEnsino = " + unidadeEnsino.intValue() + " ";
		}
		if (!situacao.equals("")) {
			sqlStr += " and RecebimentoCompra.situacao = '" + situacao + "'";
		}
		return sqlStr;
	}

	@Override
	public List<RecebimentoCompraVO> consultarPorNumeroNotaFiscal(Integer valorConsulta, Integer unidadeEnsino, String situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT RecebimentoCompra.*, Compra.codigo ");
		sql.append(" FROM RecebimentoCompra ");
		sql.append(" inner join compra on recebimentocompra.compra = Compra.codigo");
		sql.append(" inner join NotaFiscalEntradaRecebimentoCompra on NotaFiscalEntradaRecebimentoCompra.recebimentocompra = recebimentocompra.codigo");
		sql.append(" inner join notaFiscalEntrada on notaFiscalEntrada.codigo = NotaFiscalEntradaRecebimentoCompra.notaFiscalEntrada");
		sql.append(" WHERE notaFiscalEntrada.numero = ").append(valorConsulta);
		if (Uteis.isAtributoPreenchido(situacao) && situacao.equals("EF") && ((dataIni != null) || (dataFim != null))) {
			sql.append(" and recebimentocompra.data between '" + Uteis.getDataJDBC(dataIni) + "' and '" + Uteis.getDataJDBC(dataFim) + "' ");
		}
		if (unidadeEnsino.intValue() != 0) {
			sql.append(" and recebimentocompra.unidadeEnsino = " + unidadeEnsino.intValue() + " ");
		}
		if (!situacao.equals("")) {
			sql.append(" and recebimentocompra.situacao = '" + situacao + "'");
		}
		sql.append(" ORDER BY compra.codigo desc");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorCodigoCompra(Integer valorConsulta, Integer unidadeEnsino, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		return consultarPorCodigoCompra(valorConsulta, unidadeEnsino, situacao, null, null, controlarAcesso, nivelMontarDados, usuario);
	}	

	
	

	@SuppressWarnings("static-access")
	public RecebimentoCompraVO consultarPorSituacaoPrevisao(Integer compra, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM RecebimentoCompra WHERE situacao = 'PR' and compra = " + compra.intValue() + " ORDER BY codigo";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr = "SELECT * FROM RecebimentoCompra WHERE situacao = 'PR' and compra = " + compra.intValue() + " and unidadeEnsino = " + unidadeEnsino.intValue() + " ORDER BY codigo";
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		if (!tabelaResultado.next()) {
			return new RecebimentoCompraVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public static RecebimentoCompraVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		RecebimentoCompraVO obj = new RecebimentoCompraVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.getCompra().setCodigo((dadosSQL.getInt("compra")));
		obj.setData(dadosSQL.getDate("data"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.getResponsavel().setCodigo((dadosSQL.getInt("responsavel")));
		obj.getUnidadeEnsino().setCodigo((dadosSQL.getInt("unidadeEnsino")));
		obj.setValorTotal((dadosSQL.getDouble("valorTotal")));
		obj.setTipoCriacaoContaPagarEnum(TipoCriacaoContaPagarEnum.valueOf(dadosSQL.getString("tipoCriacaoContaPagar")));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosCompra(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			obj.setNotaFiscalEntradaCompraVO(getFacadeFactory().getNotaFiscalEntradaRecebimentoCompraFacade().consultarPorRecebimentoCompras(obj, usuario));
			return obj;
		}
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCompra(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setNotaFiscalEntradaCompraVO(getFacadeFactory().getNotaFiscalEntradaRecebimentoCompraFacade().consultarPorRecebimentoCompras(obj, usuario));
		obj.setRecebimentoCompraItemVOs(getFacadeFactory().getRecebimentoCompraItemFacade().consultarRecebimentoCompraItems(obj, Uteis.NIVELMONTARDADOS_TODOS, usuario));
		return obj;
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>UsuarioVO</code> relacionado ao objeto <code>RecebimentoCompraVO</code>. Faz uso da chave primária da classe <code>UsuarioVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosResponsavel(RecebimentoCompraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosUnidadeEnsino(RecebimentoCompraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	/**
	 * Operação responsável por montar os dados de um objeto da classe <code>CompraVO</code> relacionado ao objeto <code>RecebimentoCompraVO</code>. Faz uso da chave primária da classe <code>CompraVO</code> para realizar a consulta.
	 * 
	 * @param obj
	 *            Objeto no qual será montado os dados consultados.
	 */
	public static void montarDadosCompra(RecebimentoCompraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCompra().getCodigo().intValue() == 0) {
			obj.setCompra(new CompraVO());
			return;
		}
		obj.setCompra(getFacadeFactory().getCompraFacade().consultarPorChavePrimaria(obj.getCompra().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
	}

	/**
	 * Operação responsável por localizar um objeto da classe <code>RecebimentoCompraVO</code> através de sua chave primária.
	 * 
	 * @exception Exception
	 *                Caso haja problemas de conexão ou localização do objeto procurado.
	 */
	@SuppressWarnings("static-access")
	public RecebimentoCompraVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM RecebimentoCompra WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( RecebimentoCompra ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Boolean consultarSeExisteRecebimentoCompraPorRecebiemntoCompraPorSituacao(Integer recebimentoCompra, String situacaoRecebimentoCompra) {
		StringBuilder sql = new StringBuilder("select count(codigo) as qtd from recebimentocompra where codigo = ? and situacao = ?  ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), recebimentoCompra, situacaoRecebimentoCompra);
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
	}

	public Boolean consultarSeExisteRecebimentoCompraPorCompraPorSituacaoComCodigoDiferenteDoRecebimento(Integer compra, String situacaoRecebimentoCompra, Integer codigoDiferenteDoRecebimento, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("select count(codigo) as qtd from recebimentocompra where compra = ? and situacao = ? and codigo != ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), compra, situacaoRecebimentoCompra, codigoDiferenteDoRecebimento);
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
	}

	@Override
	public Boolean consultarSeExisteRecebimentoCompraPorCompraPorSituacao(Integer compra, String situacaoRecebimentoCompra, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder("select count(codigo) as qtd from recebimentocompra where compra = ? and situacao = ? ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), compra, situacaoRecebimentoCompra);
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
	}

	@Override
	public Boolean consultarSeExisteRecebimentoCompraPorCompraSemVinculoComNotaFiscalEntrada(Integer compra, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder("select count(codigo) as qtd from recebimentocompra where compra = ?  ");
		sql.append(" and NOT EXISTS ( ");
		sql.append(" select notafiscalentradarecebimentocompra.codigo  ");
		sql.append(" from notafiscalentradarecebimentocompra  ");
		sql.append(" where notafiscalentradarecebimentocompra.recebimentocompra = recebimentocompra.codigo  ");
		sql.append(" ) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), compra);
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
	}

	@Override
	public RecebimentoCompraVO consultarPorCompraPorSituacaoSemVinculoComNotaFiscalEntradaComCodigoDiferente(Integer recebimentoCompra, Integer compra, String situacao, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM RecebimentoCompra ");
		sql.append(" inner join  recebimentoCompraItem on recebimentoCompraItem.recebimentocompra = recebimentocompra.codigo ");
		sql.append(" inner join  compraItem on recebimentoCompraItem.compraItem = compraItem.codigo ");
		sql.append(" inner join  produtoservico on compraItem.produto = produtoservico.codigo " );
		sql.append(" WHERE recebimentocompra.compra = ").append(compra);
		sql.append(" and recebimentocompra.situacao = '").append(situacao).append("' ");
		sql.append(" and recebimentocompra.codigo != ").append(recebimentoCompra).append(" ");
		sql.append(" and produtoservico.tipoProdutoServico = ");
		sql.append(" (select tipoProdutoServico ");
		sql.append(" from produtoservico ");
		sql.append(" inner join compraitem on compraitem.produto = produtoservico.codigo ");
		sql.append(" inner join recebimentocompraitem on recebimentocompraitem.compraitem = compraitem.codigo and recebimentocompraitem.recebimentocompra = ").append(recebimentoCompra); 
		sql.append(" limit 1 )");
		sql.append(" and NOT EXISTS ( ");
		sql.append(" select notafiscalentradarecebimentocompra.codigo  ");
		sql.append(" from notafiscalentradarecebimentocompra  ");
		sql.append(" where notafiscalentradarecebimentocompra.recebimentocompra = recebimentocompra.codigo  ");
		sql.append(" ) ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (!tabelaResultado.next()) {
			return new RecebimentoCompraVO();
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return RecebimentoCompra.idEntidade;
	}
	

}