package negocio.facade.jdbc.faturamento.nfe;

import static negocio.comuns.utilitarias.Uteis.converterDoubleParaBigDecimal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoNotaFiscalEnum;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.compras.RecebimentoCompraItemVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.enumeradores.OrdenarNotaFiscalEntradaRecebimentoCompraEnum;
import negocio.comuns.contabil.enumeradores.SituacaoLancamentoContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoOrigemHistoricoBloqueioEnum;
import negocio.comuns.contabil.enumeradores.TipoOrigemLancamentoContabilEnum;
import negocio.comuns.contabil.enumeradores.TipoPlanoContaEnum;
import negocio.comuns.contabil.enumeradores.TipoValorLancamentoContabilEnum;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.faturamento.nfe.NaturezaOperacaoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaImpostoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaRecebimentoCompraVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO;
import negocio.comuns.faturamento.nfe.enumeradores.TipoNotaFiscalEntradaEnum;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.ContaPagarAdiantamentoVO;
import negocio.comuns.financeiro.ContaPagarNegociacaoPagamentoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.NegociacaoPagamentoVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.NotaFiscalEntradaInterfaceFacade;

/**
 * 
 * @author PedroOtimize
 *
 */

@Repository
@Scope("singleton")
@Lazy
public class NotaFiscalEntrada extends ControleAcesso implements NotaFiscalEntradaInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7098334698686990849L;
	protected static String idEntidade = "NotaFiscalEntrada";

	public NotaFiscalEntrada() {
		super();
	}

	private void validarDados(NotaFiscalEntradaVO obj, UsuarioVO usuario) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO()), "O campo Unidade Ensino (Nota Fiscal Entrada) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getFornecedorVO()), "O campo Fornecedor (Nota Fiscal Entrada) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoNotaFiscalEntradaEnum()), "O campo Tipo Movimento (Nota Fiscal Entrada) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNumero()), "O campo Número (Nota Fiscal Entrada) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getSerie()), "O campo Série (Nota Fiscal Entrada) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDataEntrada()), "O campo Data Entrada (Nota Fiscal Entrada) não foi informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getDataEmissao()), "O campo Data Emissão (Nota Fiscal Entrada) não foi informado.");
		Uteis.checkState(validarUnicidade(obj), "Já existe uma Nota Fiscal de Entrada com esse Número: " + obj.getNumero() + ", Série: "+obj.getSerie()+" para esse Fornecedor: " + obj.getFornecedorVO().getNome());
		if (!Uteis.isAtributoPreenchido(obj.getListaNotaFiscalEntradaRecebimentoCompra())) {
			verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoNotaFiscalEnum.PERMITIR_NOTA_FISCAL_ENTRADA_SEM_ORDEM_COMPRA, usuario);
		}
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getListaNotaFiscalEntradaItem()), "Deve ser Informado ao menos um item da nota fiscal entrada.");
		if (Uteis.isAtributoPreenchido(obj.getConfiguracaoContabilVO())) {
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getListaLancamentoContabeisCredito()), "Os Lançamento Contábil de Crédito (Nota Fiscal Entrada) não foi informado.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getListaLancamentoContabeisDebito()), "Os Lançamento Contaáil de Dédito (Nota Fiscal Entrada) não foi informado.");
			Uteis.checkState(!obj.getTotalLancamentoContabeisCredito().equals(obj.getTotalLancamentoContabeisDebito()), "Os valores dos lançamentos contábeis não podem ser diferentes.");
			Uteis.checkState(!obj.getTotalLancamentoContabeisCredito().equals(obj.getTotalNotaEntrada()), "O Total do Lançamento de Crédito " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(obj.getTotalLancamentoContabeisCredito(), ",") + " não pode ser diferente do valor Total da Nota Fiscal Entrada " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(obj.getTotalNotaEntrada(), ",") + ".");
			Uteis.checkState(!obj.getTotalLancamentoContabeisDebito().equals(obj.getTotalNotaEntrada()), "O Total do Lançamento de Dédito " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(obj.getTotalLancamentoContabeisDebito(), ",") + " não pode ser diferente que do valor Total da Nota Fiscal Entrada " + Uteis.arrendondarForcando2CadasDecimaisStrComSepador(obj.getTotalNotaEntrada(), ",") + ".");
		}
		if (Uteis.isAtributoPreenchido(obj.getCodigo()) && getFacadeFactory().getContaPagarFacade().consultarSeExisteContaPagarPagarOuParcialmentePagaPorCodOrigemPorTipoOrigem(obj.getCodigo().toString(), OrigemContaPagar.NOTA_FISCAL_ENTRADA.getValor(), usuario)) {
			throw new StreamSeiException("Existe uma conta pagar para essa Nota Fiscal de Entrada e a mesma esta paga, para que seja feita a operação é necessário primeiramente realizar o estorno da conta pagar.");
		}
		obj.getUsuarioCadastro().setCodigo(usuario.getCodigo());
		obj.getUsuarioCadastro().setNome(usuario.getNome());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaInterfaceFacade#persistir(negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(NotaFiscalEntradaVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			validarDados(obj, usuarioVO);
			if (obj.getCodigo() == 0) {
				incluir(obj, verificarAcesso, usuarioVO);
			} else {
				alterar(obj, verificarAcesso, usuarioVO);
			}
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaNotaFiscalEntradaImposto(), "notaFiscalEntradaImposto", idEntidade, obj.getCodigo(), usuarioVO);
			getFacadeFactory().getNotaFiscalEntradaImpostoFacade().persistir(obj.getListaNotaFiscalEntradaImposto(), false, usuarioVO);
			getFacadeFactory().getNotaFiscalEntradaRecebimentoCompraFacade().removerNotaFiscalEntradaRecebimentoCompraVONaoSelecionados(obj, usuarioVO);
			getFacadeFactory().getNotaFiscalEntradaRecebimentoCompraFacade().persistir(obj.getListaNotaFiscalEntradaRecebimentoCompra(), false, usuarioVO);
			getFacadeFactory().getNotaFiscalEntradaItemFacade().excluidoRegistroNaoExistenteLista(obj, usuarioVO);
//			getFacadeFactory().getNotaFiscalEntradaItemFacade().persistir(obj.getListaNotaFiscalEntradaItem(), false, usuarioVO);
			persistirLancamentoContabil(obj, false, usuarioVO);
			persistirContaPagar(obj, usuarioVO);
			persistirCentroResultadoOrigem(obj, usuarioVO);
		} catch (Exception e) {
			if (e.getMessage() != null && e.getMessage().contains("check_notafiscalentrada_fornecedor_numero_serie")) {
				throw new StreamSeiException("Já existe uma Nota Fiscal de Entrada com esse Número: " + obj.getNumero() + ", para esse Fornecedor: " + obj.getFornecedorVO().getNome() + " e série: " +  obj.getSerie() + ".");
			}
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final NotaFiscalEntradaVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			NotaFiscalEntrada.incluir(getIdEntidade(), verificarAcesso, usuario);
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "INCLUIR", obj.getDataEntrada(), obj.getUnidadeEnsinoVO().getCodigo(), TipoOrigemHistoricoBloqueioEnum.NFENTRADA, usuario);
			
			final StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO NotaFiscalEntrada (unidadeEnsino, fornecedor, tipoNotaFiscalEntrada, naturezaOperacao, ");
			sql.append(" dataEntrada, dataEmissao, numero, serie,  ");
			sql.append(" totalNotaEntrada, totalContaPagar, totalImpostoRetido, liquidoPagar,  ");
			sql.append(" totalImpostoPorcentagem, totalImpostoValor, totalRecebimentoCompra, dataRegistro, usuarioCadastro )  ");
			sql.append("    VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? , ? )");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFornecedorVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoNotaFiscalEntradaEnum(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNaturezaOperacaoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataEntrada(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataEmissao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNumero(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSerie(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTotalNotaEntrada(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTotalContaPagar(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTotalImpostoRetido(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getLiquidoPagar(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTotalImpostoPorcentagem(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTotalImpostoValor(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTotalRecebimentoCompra(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataRegistro(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getUsuarioCadastro(), ++i, sqlInserir);
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
	public void alterar(final NotaFiscalEntradaVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			NotaFiscalEntrada.alterar(getIdEntidade(), verificarAcesso, usuario);
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "ALTERAR", obj.getDataEntrada(), obj.getDataEntradaAnterior(), obj.getUnidadeEnsinoVO().getCodigo(), null, TipoOrigemHistoricoBloqueioEnum.NFENTRADA, usuario );
			
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE NotaFiscalEntrada ");
			sql.append("   SET unidadeEnsino=?, fornecedor=?, tipoNotaFiscalEntrada=?, naturezaOperacao=?, ");
			sql.append("   dataEntrada=?, dataEmissao=?, numero=?, serie=?, ");
			sql.append("   totalNotaEntrada=?, totalContaPagar=?, totalImpostoRetido=?, liquidoPagar=?,  ");
			sql.append("   totalImpostoPorcentagem=?, totalImpostoValor=?, totalRecebimentoCompra=?  ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFornecedorVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoNotaFiscalEntradaEnum(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNaturezaOperacaoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDataEntrada(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDataEmissao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNumero(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSerie(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTotalNotaEntrada(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTotalContaPagar(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTotalImpostoRetido(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getLiquidoPagar(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTotalImpostoPorcentagem(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTotalImpostoValor(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTotalRecebimentoCompra(), ++i, sqlAlterar);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaInterfaceFacade#excluir(negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(NotaFiscalEntradaVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			NotaFiscalEntrada.excluir(getIdEntidade(), verificarAcesso, usuario);
			verificarCompetenciaBloqueadaParaRegistrosEntidade(obj, "EXCLUIR", obj.getDataEntrada(), obj.getUnidadeEnsinoVO().getCodigo(), TipoOrigemHistoricoBloqueioEnum.NFENTRADA, usuario);
			
			getFacadeFactory().getContaPagarFacade().excluirPorNotaFiscalEntrada(obj, usuario);
			obj.getListaNotaFiscalEntradaItem().forEach(nfei -> getFacadeFactory().getNotaFiscalEntradaItemFacade().excluir(nfei, false, usuario));
			getFacadeFactory().getNotaFiscalEntradaRecebimentoCompraFacade().estornaNotaFiscalEntradaRecebimentoCompra(obj, usuario);
			getFacadeFactory().getLancamentoContabilFacade().excluirPorCodOrigemTipoOrigem(obj.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.NOTA_FISCAL_ENTRADA, false, usuario);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().excluidoRegistroNaoExistenteListaPorCodOrigemPorTipoCentroResultadoOrigemEnum(null, obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.NOTA_FISCAL_ENTRADA, usuario);
			excluirCodigoENumeroNotaFiscalContaPagar(obj, usuario);
			getConexao().getJdbcTemplate().update("DELETE FROM NotaFiscalEntrada WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void preencherNotaFiscalEntradaCompraPorFornecedor(NotaFiscalEntradaVO obj, UsuarioVO usuario) {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO()), "O campo Unidade Ensino deve ser informado.");
			if (Uteis.isAtributoPreenchido(obj.getFornecedorVO())) {
				List<NotaFiscalEntradaRecebimentoCompraVO> lista = getFacadeFactory().getNotaFiscalEntradaRecebimentoCompraFacade().consultarTodasRecebimentoComprasPendenteFornecedorPorNotaFiscalEntrada(obj, false, usuario);
				lista.stream().forEach(p -> addNotaFiscalEntradaRecebimentoCompra(obj, p));
				obj.getListaNotaFiscalEntradaRecebimentoCompra().sort(OrdenarNotaFiscalEntradaRecebimentoCompraEnum.COMPRA_RECEBIMENTOCOMPRA);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void addNotaFiscalEntradaRecebimentoCompra(NotaFiscalEntradaVO obj, NotaFiscalEntradaRecebimentoCompraVO notaFiscalRecebimentoCompraVO) {
		int index = 0;
		for (NotaFiscalEntradaRecebimentoCompraVO objExistente : obj.getListaNotaFiscalEntradaRecebimentoCompra()) {
			if (objExistente.equalsCampoSelecaoLista(notaFiscalRecebimentoCompraVO)) {
				notaFiscalRecebimentoCompraVO.getRecebimentoCompraVO().setRecebimentoCompraItemVOs(objExistente.getRecebimentoCompraVO().getRecebimentoCompraItemVOs());
				notaFiscalRecebimentoCompraVO.setSelecionado(objExistente.isSelecionado());
				obj.getListaNotaFiscalEntradaRecebimentoCompra().set(index, notaFiscalRecebimentoCompraVO);
				return;
			}
			index++;
		}
		obj.getListaNotaFiscalEntradaRecebimentoCompra().add(notaFiscalRecebimentoCompraVO);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void gerarNotaFiscalEntradaItemPorCompra(NotaFiscalEntradaVO obj, NotaFiscalEntradaRecebimentoCompraVO nferc, UsuarioVO usuario) {
		try {
			Uteis.checkState(nferc.getRecebimentoCompraVO().getTipoCriacaoContaPagarEnum().isRecebimentoCompra() && !nferc.getRecebimentoCompraVO().getRecebimentoFinalizado(), "O Recebimento de Compra de Número " + nferc.getRecebimentoCompraVO().getCodigo() + " deve ser finalizado primeiro, pois o mesmo gera a conta pagar que será utilizada na nota fiscal de entrada.");
			for (RecebimentoCompraItemVO rci : nferc.getRecebimentoCompraVO().getRecebimentoCompraItemVOs()) {
				Uteis.checkState((rci.isSelecionado() && rci.getQuantidadeRecebida() == 0.0), "A Quantidade recebida do produto " + rci.getCompraItem().getProduto().getNome() + " deve ser informada para o Recebimento de compra de número " + nferc.getRecebimentoCompraVO().getCodigo());
				if (nferc.isSelecionado() && rci.isSelecionado()) {
					preencherItemNotaFiscalEntradaPorCompra(obj, nferc, rci, usuario);
				} else {
					removerItemNotaFiscalEntradaPorCompra(obj, rci, usuario);
				}
			}
			obj.getListaNotaFiscalEntradaItem().stream().forEach(p -> getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarValidacaoPorcentagem(p.getListaCentroResultadoOrigemVOs(), p.getValorTotal(), usuario));
			obj.setListaContaPagarOutrasOrigem(getFacadeFactory().getContaPagarFacade().consultarPorNotaFiscalEntradaOutrasOrigem(obj, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
			atualizarListasDaNotaFiscalEntrada(obj, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void preencherItemNotaFiscalEntradaPorCompra(NotaFiscalEntradaVO obj, NotaFiscalEntradaRecebimentoCompraVO nferc, RecebimentoCompraItemVO rci, UsuarioVO usuario) {
		Double quantidadeTotalRecebida = obj.getListaNotaFiscalEntradaRecebimentoCompra()
				.stream()
				.filter(nfercExistente -> nfercExistente.isSelecionado() && !nfercExistente.getRecebimentoCompraVO().getCodigo().equals(rci.getRecebimentoCompraVO().getCodigo()))
				.flatMap(nfercExistente -> nfercExistente.getRecebimentoCompraVO().getRecebimentoCompraItemVOs().stream())
				.filter(recebimentoItemExistente -> recebimentoItemExistente.isSelecionado() && recebimentoItemExistente.getCompraItem().getCodigo().equals(rci.getCompraItem().getCodigo()))
				.mapToDouble(RecebimentoCompraItemVO::getQuantidadeRecebida)
				.sum();
		if (!nferc.getRecebimentoCompraVO().getRecebimentoFinalizado() && (quantidadeTotalRecebida + rci.getQuantidadeRecebida() + rci.getCompraItem().getQuantidadeRecebida()) > rci.getCompraItem().getQuantidade()) {
			throw new StreamSeiException("A Quantidade Recebida somada com a Qtd. a Receber do produto " + rci.getCompraItem().getProduto().getNome() + " é maior que a quantidade " + rci.getCompraItem().getQuantidade() + " do Recebimento de compra de número " + nferc.getRecebimentoCompraVO().getCodigo());
		}
		NotaFiscalEntradaItemVO nfei = consultarNotaFiscalEntradaItem(obj, rci);
		if (!Uteis.isAtributoPreenchido(nfei.getRecebimentoCompraItemVO())) {
			nfei.setProdutoServicoVO(rci.getCompraItem().getProduto());
			nfei.setValorUnitario(rci.getValorUnitario());
		}
		nfei.setRecebimentoCompraItemVO(rci);
		nfei.setQuantidade(rci.getQuantidadeRecebida());
		getFacadeFactory().getNotaFiscalEntradaItemFacade().gerarNotaFiscalEntradaItemRecebimento(nfei, usuario);
		adicionarNotaFiscalEntradaItem(obj, nfei, false);
	}

	private void removerItemNotaFiscalEntradaPorCompra(NotaFiscalEntradaVO obj, RecebimentoCompraItemVO rci, UsuarioVO usuario) {
		NotaFiscalEntradaItemVO nfei = consultarNotaFiscalEntradaItem(obj, rci);
		if (Uteis.isAtributoPreenchido(nfei.getRecebimentoCompraItemVO())) {
			removerNotaFiscalEntradaItem(obj, nfei, false, usuario);
		}
	}

	private NotaFiscalEntradaItemVO consultarNotaFiscalEntradaItem(NotaFiscalEntradaVO obj, RecebimentoCompraItemVO rci) {
		for (NotaFiscalEntradaItemVO objExistente : obj.getListaNotaFiscalEntradaItem()) {
			if (Uteis.isAtributoPreenchido(objExistente.getRecebimentoCompraItemVO()) && Uteis.isAtributoPreenchido(rci)
					&& objExistente.getRecebimentoCompraItemVO().getCodigo().equals(rci.getCodigo())) {
				return objExistente;
			}
		}
		return new NotaFiscalEntradaItemVO();
	}

	@Override
	public void atualizarListasDaNotaFiscalEntrada(NotaFiscalEntradaVO obj, UsuarioVO usuario) {
		obj.atualizarTotalizadoresLista();
		obj.calcularValorNotaFiscalEntradaImposto();
		List<CentroResultadoOrigemVO> novaLista = new ArrayList<>();
		obj.getListaNotaFiscalEntradaItem()
				.stream()
				.flatMap(notaFiscalItem -> notaFiscalItem.getListaCentroResultadoOrigemVOs().stream())
				.forEach(centroResultadoOrigem -> {
					CentroResultadoOrigemVO clone = centroResultadoOrigem.getClone();
					clone.setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR);
					getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigemAgrupado(clone, novaLista);
				});
		if (Uteis.isAtributoPreenchido(novaLista)) {
			novaLista.forEach(p -> p.calcularPorcentagem(obj.getTotalNotaEntrada()));
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().atualizarListasDeCentroResultadoOrigem(obj.getListaCentroResultadoOrigemVOs(), novaLista);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarValidacaoPorcentagem(obj.getListaCentroResultadoOrigemVOs(), obj.getTotalNotaEntrada(), usuario);
		}
		if (Uteis.isAtributoPreenchido(obj.getListaNotaFiscalEntradaRecebimentoCompra()) && obj.getListaNotaFiscalEntradaItem().stream().noneMatch(nfeItem -> nfeItem.getRecebimentoCompraItemVO().getRecebimentoCompraVO().getCodigo().equals(0))) {
			getFacadeFactory().getNotaFiscalEntradaRecebimentoCompraFacade().validarDadosParaCriarContaPagar(obj, usuario);
		} else if (Uteis.isAtributoPreenchido(obj.getListaContaPagar())) {
			obj.getListaContaPagar().stream().forEach(contaPagar -> {
//				contaPagar.setValor(Uteis.arrendondarForcando2CadasDecimais(contaPagar.getValor() - ((contaPagar.getValor() * obj.getValorTotalImpostoRetidoPorcentagem()) / 100)));
				getFacadeFactory().getContaPagarFacade().atualizarCentroResultadoOrigemContaPagar(obj.getListaCentroResultadoOrigemVOs(), contaPagar, usuario);
			});
		}
		gerarLancamentoContabilPadrao(obj, usuario);
		obj.atualizarTotalizadoresLista();
	}

	private void validarDadosNotaFiscalEntradaImposto(NotaFiscalEntradaImpostoVO obj) {
		if (!Uteis.isAtributoPreenchido(obj.getNotaFiscalEntradaVO().getListaNotaFiscalEntradaItem())) {
			throw new StreamSeiException("Dever ser informado pelo menos um Item da Nota Fiscal, para que assim seja feito os cálculos automáticos do imposto.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getImpostoVO())) {
			throw new StreamSeiException("O campo Imposto (Nota Fiscal Entrada) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getPorcentagem())) {
			throw new StreamSeiException("O campo Porcentagem (Nota Fiscal Entrada) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getValor())) {
			throw new StreamSeiException("O campo Valor (Nota Fiscal Entrada) deve ser informado.");
		}
		Double totalPorcentagem = obj.getNotaFiscalEntradaVO().getListaNotaFiscalEntradaImposto().stream().filter(p -> !p.equalsCampoSelecaoLista(obj)).mapToDouble(NotaFiscalEntradaImpostoVO::getPorcentagem).sum();
		if (Uteis.arrendondarForcando2CadasDecimais(totalPorcentagem + obj.getPorcentagem()) > 100.00) {
			throw new StreamSeiException("O total de porcentagem do Imposto não pode ser maior que 100%.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaInterfaceFacade#addNotaFiscalEntradaImposto(negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO, negocio.comuns.faturamento.nfe.NotaFiscalEntradaImpostoVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void addNotaFiscalEntradaImposto(NotaFiscalEntradaVO obj, NotaFiscalEntradaImpostoVO notaFiscalImpostoVO, UsuarioVO usuario) {
		try {
			notaFiscalImpostoVO.setNotaFiscalEntradaVO(obj);
			validarDadosNotaFiscalEntradaImposto(notaFiscalImpostoVO);
			notaFiscalImpostoVO.setImpostoVO(getFacadeFactory().getImpostoFacade().consultarPorChavePrimaria(notaFiscalImpostoVO.getImpostoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			int index = 0;
			for (NotaFiscalEntradaImpostoVO objExistente : obj.getListaNotaFiscalEntradaImposto()) {
				if (objExistente.equalsCampoSelecaoLista(notaFiscalImpostoVO)) {
					obj.getListaNotaFiscalEntradaImposto().set(index, notaFiscalImpostoVO);
					atualizarListasDaNotaFiscalEntrada(obj, usuario);
					return;
				}
				index++;
			}
			obj.getListaNotaFiscalEntradaImposto().add(notaFiscalImpostoVO);
			atualizarListasDaNotaFiscalEntrada(obj, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaInterfaceFacade#removerNotaFiscalEntradaImposto(negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO, negocio.comuns.faturamento.nfe.NotaFiscalEntradaImpostoVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void removerNotaFiscalEntradaImposto(NotaFiscalEntradaVO obj, NotaFiscalEntradaImpostoVO notaFiscalImpostoVO, UsuarioVO usuario) {
		Iterator<NotaFiscalEntradaImpostoVO> i = obj.getListaNotaFiscalEntradaImposto().iterator();
		while (i.hasNext()) {
			NotaFiscalEntradaImpostoVO objExistente = i.next();
			if (objExistente.equalsCampoSelecaoLista(notaFiscalImpostoVO)) {
				i.remove();
				atualizarListasDaNotaFiscalEntrada(obj, usuario);
				return;
			}
		}
	}

	private void validarDadosNotaFiscalEntradaItem(NotaFiscalEntradaItemVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNotaFiscalEntradaVO().getUnidadeEnsinoVO()), "O campo Unidade Ensino deve ser informado antes de adicionar uma conta pagar.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNotaFiscalEntradaVO().getFornecedorVO()), "O campo Fornecedor deve ser informado antes de adicionar uma conta pagar.");
		if (!Uteis.isAtributoPreenchido(obj.getNotaFiscalEntradaVO().getTipoNotaFiscalEntradaEnum())) {
			throw new StreamSeiException("O campo Tipo Nota Fiscal (Nota Fiscal Entrada) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getProdutoServicoVO())) {
			throw new StreamSeiException("O campo Produto Serviço (Nota Fiscal Entrada) deve ser informado.");
		}
		if ((obj.getNotaFiscalEntradaVO().getTipoNotaFiscalEntradaEnum().isProduto() && !obj.getProdutoServicoVO().getTipoProdutoServicoEnum().isProduto()) || (obj.getNotaFiscalEntradaVO().getTipoNotaFiscalEntradaEnum().isServico() && !obj.getProdutoServicoVO().getTipoProdutoServicoEnum().isServico())) {
			throw new StreamSeiException("Não é possível adicionar o item do tipo " + UteisJSF.internacionalizarEnum(obj.getProdutoServicoVO().getTipoProdutoServicoEnum()) + " para a nota fiscal de entrada do tipo " + UteisJSF.internacionalizarEnum(obj.getNotaFiscalEntradaVO().getTipoNotaFiscalEntradaEnum()));
		}
		if (!Uteis.isAtributoPreenchido(obj.getQuantidade())) {
			throw new StreamSeiException("O campo Quantidade (Nota Fiscal Entrada) deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getValorUnitario())) {
			throw new StreamSeiException("O campo Preço Unitário (Nota Fiscal Entrada) deve ser informado.");
		}

	}

	@Override
	public void adicionarCentroResultadoOrigemNotaFiscalEntradaItem(NotaFiscalEntradaVO obj, NotaFiscalEntradaItemVO notaFiscalItemVO, CentroResultadoOrigemVO centroResultadoOrigemVO, UsuarioVO usuario) {
		if (!obj.getTipoNotaFiscalEntradaEnum().isProduto()) {
			centroResultadoOrigemVO.setQuantidade(1.0);
		}		
		centroResultadoOrigemVO.setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR);
		Uteis.checkState(obj.getTipoNotaFiscalEntradaEnum().isProduto() && Uteis.isAtributoPreenchido(centroResultadoOrigemVO.getQuantidade()) && centroResultadoOrigemVO.getQuantidade() > notaFiscalItemVO.getQuantidade(), "A Quantidade do Centro Resultado Movimentação não pode ser maior que a Quantidade do Item da Nota Fiscal de Entrada.");
		getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigem(notaFiscalItemVO.getListaCentroResultadoOrigemVOs(), centroResultadoOrigemVO, notaFiscalItemVO.getValorTotal(), true, usuario);
		getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarValidacaoPorcentagem(notaFiscalItemVO.getListaCentroResultadoOrigemVOs(), notaFiscalItemVO.getValorTotal(), usuario);
///     Comentado devido a solicitação do cliente para que os produtos possam ser rateados por mais de uma turma.		
//		if (obj.getTipoNotaFiscalEntradaEnum().isProduto() && (notaFiscalItemVO.getQuantidadeCentroResultadoTotal() > notaFiscalItemVO.getQuantidade())) {
//			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().removerCentroResultadoOrigem(notaFiscalItemVO.getListaCentroResultadoOrigemVOs(), centroResultadoOrigemVO, usuario);
//			throw new StreamSeiException("O total da quantidade do Centro Resultado Movimentação não pode ser maior que a Quantidade do Item da Nota Fiscal de Entrada.");
//		}
		atualizarListasDaNotaFiscalEntrada(obj, usuario);
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void adicionarNotaFiscalEntradaItemManual(NotaFiscalEntradaVO obj, NotaFiscalEntradaItemVO notaFiscalItemVO, UsuarioVO usuario) {
		adicionarNotaFiscalEntradaItem(obj, notaFiscalItemVO, true);
		if (Uteis.isAtributoPreenchido(notaFiscalItemVO.getListaCentroResultadoOrigemVOs())) {
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarDistribuicaoValoresCentroResultado(notaFiscalItemVO.getListaCentroResultadoOrigemVOs(), notaFiscalItemVO.getValorTotal(), usuario);
		}
		atualizarListasDaNotaFiscalEntrada(obj, usuario);
	}

	private void adicionarNotaFiscalEntradaItem(NotaFiscalEntradaVO obj, NotaFiscalEntradaItemVO notaFiscalItemVO, boolean isManual) {
		notaFiscalItemVO.setNotaFiscalEntradaVO(obj);
		validarDadosNotaFiscalEntradaItem(notaFiscalItemVO);
		int index = 0;
		for (NotaFiscalEntradaItemVO objExistente : obj.getListaNotaFiscalEntradaItem()) {
			if ((!isManual && objExistente.equalsCampoSelecaoLista(notaFiscalItemVO))
					|| (isManual && objExistente.equalsCampoSelecaoListaManual(notaFiscalItemVO) && objExistente.getRecebimentoCompraItemVO().getRecebimentoCompraVO().getCodigo().equals(0))) {
				obj.getListaNotaFiscalEntradaItem().set(index, notaFiscalItemVO);
				return;
			}
			index++;
		}
		obj.getListaNotaFiscalEntradaItem().add(notaFiscalItemVO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaInterfaceFacade#removerNotaFiscalEntradaItem(negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO, negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void removerNotaFiscalEntradaItem(NotaFiscalEntradaVO obj, NotaFiscalEntradaItemVO notaFiscalItemVO, boolean isManual, UsuarioVO usuario) {
		Iterator<NotaFiscalEntradaItemVO> i = obj.getListaNotaFiscalEntradaItem().iterator();
		while (i.hasNext()) {
			NotaFiscalEntradaItemVO objExistente = i.next();
			if ((!isManual && objExistente.equalsCampoSelecaoLista(notaFiscalItemVO))
					|| (isManual && objExistente.equalsCampoSelecaoListaManual(notaFiscalItemVO) && objExistente.getRecebimentoCompraItemVO().getRecebimentoCompraVO().getCodigo().equals(0))) {
				i.remove();
				obj.atualizarTotalizadoresLista();				
				return;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaInterfaceFacade#addNotaFiscalEntradaItem(negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO, negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void preencherDadosContaPagarPadrao(NotaFiscalEntradaVO obj, ContaPagarVO contaPagar, UsuarioVO usuario) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO()), "O campo Unidade Ensino deve ser informado antes de adicionar uma conta pagar.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getFornecedorVO()), "O campo Fornecedor deve ser informado antes de adicionar uma conta pagar.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(contaPagar.getDataVencimento()), "O campo DATA DE VENCIMENTO (Conta à Pagar) deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(contaPagar.getValor()), "O campo VALOR (Conta à Pagar) deve ser informado.");
		obj.atualizarTotalizadoresLista();		
		Uteis.checkState((!contaPagar.isEdicaoManual() && (contaPagar.getValor() + obj.getTotalContaPagar()) > obj.getLiquidoPagar()), "Não é possivel realizar essa operação, pois o valor total das contas a Pagar são maiores que o Total Liquido da nota fiscal de entrada.");
		contaPagar.setUnidadeEnsino(obj.getUnidadeEnsinoVO());
		contaPagar.setFornecedor(obj.getFornecedorVO());
		contaPagar.setTipoSacado(TipoSacado.FORNECEDOR.getValor());
		getFacadeFactory().getContaPagarFacade().adicionarContaPagarPorCentroResultadoOrigem(obj.getListaContaPagar(), obj.getListaCentroResultadoOrigemVOs(), contaPagar, usuario);
		obj.atualizarTotalizadoresLista();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaInterfaceFacade#removerNotaFiscalEntradaItem(negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO, negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemVO, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public void removerNotaFiscalEntradaContaPagar(NotaFiscalEntradaVO obj, ContaPagarVO contaPagar) {
		Iterator<ContaPagarVO> i = obj.getListaContaPagar().iterator();
		while (i.hasNext()) {
			ContaPagarVO objExistente = i.next();
			if (objExistente.equalsCampoSelecaoListaPorNotaFiscalEntrada(contaPagar)) {				
				i.remove();
				obj.getListaContaPagarExcluidas().add(contaPagar);
				obj.atualizarTotalizadoresLista();
				return;
			}
		}
	}

	private void persistirContaPagar1(NotaFiscalEntradaVO obj, UsuarioVO usuarioVO) {
		try {
			boolean validarCentroResultadoAposAlteracao = true;
			NegociacaoPagamentoVO negociacaoPagamentoVO = new NegociacaoPagamentoVO();
			negociacaoPagamentoVO.setTipoSacado(TipoSacado.FORNECEDOR.getValor());
			negociacaoPagamentoVO.setFornecedor(obj.getFornecedorVO());
			negociacaoPagamentoVO.setData(new Date());
			negociacaoPagamentoVO.setDataRegistro(new Date());
			negociacaoPagamentoVO.setUnidadeEnsino(obj.getUnidadeEnsinoVO());
			negociacaoPagamentoVO.setResponsavel(usuarioVO);
			Uteis.checkState(obj.getTotalRestanteContaPagar() != 0.0, "Não é possivel realizar essa operação, pois o Liquido a Pagar da nota fiscal de entrada é diferente do total da conta pagar.");
			for (ContaPagarVO contaPagar : obj.getListaContaPagarExcluidas()) {		
				getFacadeFactory().getContaPagarFacade().excluir(contaPagar, false, usuarioVO);
			}
			
			for (ContaPagarVO contaPagar : obj.getListaContaPagar()) {
				validarCentroResultadoAposAlteracao = preencherContaPagarNegociacaoPagamentoVOPorAdiantamento(negociacaoPagamentoVO, contaPagar, usuarioVO);				
				try {
					if (!Uteis.isAtributoPreenchido(contaPagar)) {
						contaPagar.setCodOrigem(obj.getCodigo().toString());
						contaPagar.setNumeroNotaFiscalEntrada(obj.getNumero().toString());			
						contaPagar.setCodigoNotaFiscalEntrada(obj.getCodigo().toString());
						getFacadeFactory().getContaPagarFacade().incluir(contaPagar, false, validarCentroResultadoAposAlteracao, usuarioVO);
						if (Uteis.isAtributoPreenchido(contaPagar.getListaContaPagarAdiantamentoVO())&& (contaPagar.getJuro()> 0|| contaPagar.getMulta() > 0)) {
							for (ContaPagarAdiantamentoVO cpa : contaPagar.getListaContaPagarAdiantamentoVO()) {
								getFacadeFactory().getContaPagarFacade().alterarValorUtilizadoAdiantamento(cpa.getContaPagarUtilizada(), cpa.getValorUtilizado(),  usuarioVO , true);	
							}	
						}
					} else {
						contaPagar.setNumeroNotaFiscalEntrada(obj.getNumero().toString());
						contaPagar.setCodigoNotaFiscalEntrada(obj.getCodigo().toString());
						getFacadeFactory().getContaPagarFacade().alterar(contaPagar, false, validarCentroResultadoAposAlteracao, usuarioVO);
					}
				} catch (Exception e) {
					if (e.getMessage().contains("competência fechada")) {
						obj.forcarControleBloqueioCompetencia(e.getMessage(), contaPagar.getFechamentoMesVOBloqueio(), contaPagar.getDataBloqueioVerificada());
						throw e;
					} else {
						throw e;
					}
					
				}
			}
			if(Uteis.isAtributoPreenchido(negociacaoPagamentoVO.getContaPagarNegociacaoPagamentoVOs())){
				getFacadeFactory().getNegociacaoPagamentoFacade().incluir(negociacaoPagamentoVO, true, usuarioVO);
			}
			obj.getListaContaPagarExcluidas().clear();				
			
			for (ContaPagarVO contaPagar : obj.getListaContaPagarOutrasOrigem()) {	
				StringBuilder codigoNota = new StringBuilder();
				StringBuilder nrNota = new StringBuilder();
				codigoNota.append(contaPagar.getCodigoNotaFiscalEntrada().trim());
				nrNota.append(contaPagar.getNumeroNotaFiscalEntrada().trim());				
				
				if(!contaPagar.getCodigoNotaFiscalEntrada().trim().isEmpty()) {					
					codigoNota.append(", ");								
				}
				
				codigoNota.append(obj.getCodigo().toString().trim());
				
				if(codigoNota.length() > 250 ) {
					codigoNota = new StringBuilder(codigoNota.substring(0, 250));
				}
				
				if(!contaPagar.getNumeroNotaFiscalEntrada().trim().isEmpty()) {					
					nrNota.append(", ");								
				}
				
				nrNota.append(obj.getNumero().toString().trim());
				
				if(nrNota.length() > 250 ) {
					nrNota = new StringBuilder(nrNota.substring(0, 250));
				}
								
				
				contaPagar.setCodigoNotaFiscalEntrada(codigoNota.toString());
				contaPagar.setNumeroNotaFiscalEntrada(nrNota.toString());
				getFacadeFactory().getContaPagarFacade().alterar(contaPagar, false, validarCentroResultadoAposAlteracao, usuarioVO);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	private void persistirContaPagar(NotaFiscalEntradaVO obj, UsuarioVO usuarioVO) {
		try {			
			Uteis.checkState(obj.getTotalRestanteContaPagar() != 0.0, "Não é possivel realizar essa operação, pois o Liquido a Pagar da nota fiscal de entrada é diferente do total da conta pagar.");
			NegociacaoPagamentoVO negociacaoPagamentoVO = new NegociacaoPagamentoVO();
			for (ContaPagarVO contaPagar : obj.getListaContaPagarExcluidas()) {		
				getFacadeFactory().getContaPagarFacade().excluir(contaPagar, false, usuarioVO);
			}
			for (ContaPagarVO contaPagar : obj.getListaContaPagar()) {							
				try {					
					if (!Uteis.isAtributoPreenchido(contaPagar)) {
						contaPagar.setCodOrigem(obj.getCodigo().toString());
						contaPagar.setNumeroNotaFiscalEntrada(obj.getNumero().toString());			
						contaPagar.setCodigoNotaFiscalEntrada(obj.getCodigo().toString());
						getFacadeFactory().getContaPagarFacade().incluir(contaPagar, false, true, usuarioVO);						
					} else {
						contaPagar.setNumeroNotaFiscalEntrada(obj.getNumero().toString());
						contaPagar.setCodigoNotaFiscalEntrada(obj.getCodigo().toString());
						getFacadeFactory().getContaPagarFacade().alterar(contaPagar, false, true, usuarioVO);
					}
					if (Uteis.isAtributoPreenchido(contaPagar.getListaContaPagarAdiantamentoVO()) && (contaPagar.getJuro() > 0 || contaPagar.getMulta() > 0)) {
						for (ContaPagarAdiantamentoVO cpa : contaPagar.getListaContaPagarAdiantamentoVO()) {
							getFacadeFactory().getContaPagarFacade().alterarValorUtilizadoAdiantamento(cpa.getContaPagarUtilizada(), cpa.getValorUtilizado(),  usuarioVO , true);	
						}
					}else if (Uteis.isAtributoPreenchido(contaPagar.getListaContaPagarAdiantamentoVO())
							&& (!getFacadeFactory().getNegociacaoPagamentoFacade().consultarSeExisteNegociacaoPagamentoPorContaPagar(contaPagar.getCodigo(), usuarioVO))) {
						ContaPagarNegociacaoPagamentoVO contaPagarNegociacaoPagamento = new ContaPagarNegociacaoPagamentoVO();
						contaPagarNegociacaoPagamento.setContaPagar(contaPagar);
						contaPagarNegociacaoPagamento.setNegociacaoContaPagar(negociacaoPagamentoVO.getCodigo());
						negociacaoPagamentoVO.getContaPagarNegociacaoPagamentoVOs().add(contaPagarNegociacaoPagamento);
						negociacaoPagamentoVO.calcularTotal();
						contaPagar.getListaLancamentoContabeisCredito().clear();
						contaPagar.getListaLancamentoContabeisDebito().clear();
					}
				} catch (Exception e) {
					if (e.getMessage().contains("competência fechada")) {
						obj.forcarControleBloqueioCompetencia(e.getMessage(), contaPagar.getFechamentoMesVOBloqueio(), contaPagar.getDataBloqueioVerificada());
						throw e;
					} else {
						throw e;
					}
					
				}
			}
			
			if(Uteis.isAtributoPreenchido(negociacaoPagamentoVO.getContaPagarNegociacaoPagamentoVOs())){
				negociacaoPagamentoVO.setTipoSacado(TipoSacado.FORNECEDOR.getValor());
				negociacaoPagamentoVO.setFornecedor(obj.getFornecedorVO());
				negociacaoPagamentoVO.setData(new Date());
				negociacaoPagamentoVO.setDataRegistro(new Date());
				negociacaoPagamentoVO.setUnidadeEnsino(obj.getUnidadeEnsinoVO());
				negociacaoPagamentoVO.setResponsavel(usuarioVO);
				getFacadeFactory().getNegociacaoPagamentoFacade().incluir(negociacaoPagamentoVO, true, usuarioVO);
			}
			obj.getListaContaPagarExcluidas().clear();				
			
			for (ContaPagarVO contaPagar : obj.getListaContaPagarOutrasOrigem()) {	
				StringBuilder codigoNota = new StringBuilder();
				StringBuilder nrNota = new StringBuilder();
				codigoNota.append(contaPagar.getCodigoNotaFiscalEntrada().trim());
				nrNota.append(contaPagar.getNumeroNotaFiscalEntrada().trim());				
				
				if(!contaPagar.getCodigoNotaFiscalEntrada().trim().isEmpty()) {					
					codigoNota.append(", ");								
				}
				
				codigoNota.append(obj.getCodigo().toString().trim());
				
				if(codigoNota.length() > 250 ) {
					codigoNota = new StringBuilder(codigoNota.substring(0, 250));
				}
				
				if(!contaPagar.getNumeroNotaFiscalEntrada().trim().isEmpty()) {					
					nrNota.append(", ");								
				}
				
				nrNota.append(obj.getNumero().toString().trim());
				
				if(nrNota.length() > 250 ) {
					nrNota = new StringBuilder(nrNota.substring(0, 250));
				}
				
				
				contaPagar.setCodigoNotaFiscalEntrada(codigoNota.toString());
				contaPagar.setNumeroNotaFiscalEntrada(nrNota.toString());
				getFacadeFactory().getContaPagarFacade().alterar(contaPagar, false, true, usuarioVO);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	
	private boolean preencherContaPagarNegociacaoPagamentoVOPorAdiantamento(NegociacaoPagamentoVO negociacaoPagamentoVO , ContaPagarVO contaPagar, UsuarioVO usuario) throws Exception {
		if(!getFacadeFactory().getNegociacaoPagamentoFacade().consultarSeExisteNegociacaoPagamentoPorContaPagar(contaPagar.getCodigo(), usuario) 
				&&  Uteis.isAtributoPreenchido(contaPagar.getListaContaPagarAdiantamentoVO()) 
				&& contaPagar.getJuro().equals(0.0) && contaPagar.getMulta().equals(0.0)){
			ContaPagarNegociacaoPagamentoVO contaPagarNegociacaoPagamento = new ContaPagarNegociacaoPagamentoVO();
			contaPagarNegociacaoPagamento.setContaPagar(contaPagar);
			contaPagarNegociacaoPagamento.setNegociacaoContaPagar(negociacaoPagamentoVO.getCodigo());
			negociacaoPagamentoVO.getContaPagarNegociacaoPagamentoVOs().add(contaPagarNegociacaoPagamento);
			negociacaoPagamentoVO.calcularTotal();
			contaPagar.getListaLancamentoContabeisCredito().clear();
			contaPagar.getListaLancamentoContabeisDebito().clear();
			return false;
		}
		
		
		return true;
	}
	

	private void persistirLancamentoContabil(NotaFiscalEntradaVO nfe, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			List<LancamentoContabilVO> listaTemp = new ArrayList<>();
			listaTemp.addAll(nfe.getListaLancamentoContabeisCredito());
			listaTemp.addAll(nfe.getListaLancamentoContabeisDebito());
			EnumMap<TipoOrigemLancamentoContabilEnum, String> mapaDeletar = new EnumMap<>(TipoOrigemLancamentoContabilEnum.class);
			mapaDeletar.put(TipoOrigemLancamentoContabilEnum.NOTA_FISCAL_ENTRADA, "'" + nfe.getCodigo().toString() + "'");
			getFacadeFactory().getLancamentoContabilFacade().validarSeLancamentoContabilFoiExcluido(listaTemp, mapaDeletar, usuarioVO);
			for (LancamentoContabilVO lc : listaTemp) {
				lc.setCodOrigem(nfe.getCodigo().toString());
				getFacadeFactory().getLancamentoContabilFacade().persistir(lc, verificarAcesso, usuarioVO);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void gerarLancamentoContabilPadrao(NotaFiscalEntradaVO obj, UsuarioVO usuario) {
		obj.getListaLancamentoContabeisCredito().clear();
		obj.getListaLancamentoContabeisDebito().clear();
		getFacadeFactory().getLancamentoContabilFacade().gerarLancamentoContabilPorNotaFiscalEntradaItem(obj, usuario);
		getFacadeFactory().getLancamentoContabilFacade().gerarLancamentoContabilPorNotaFiscalEntradaImposto(obj, usuario);
		validarDadosTotalizadoresLancamentoContabilPadrao(obj, usuario);
	}

	public void addLancamentoContabilVO(NotaFiscalEntradaVO nfe, LancamentoContabilVO lancamento, CategoriaProdutoVO categoriaProdutoFiltroVO, ImpostoVO impstoFiltroVO, UsuarioVO usuario) {
		try {
			if (!Uteis.isAtributoPreenchido(lancamento.getPlanoContaVO())) {
				throw new StreamSeiException("O campo Plano de Conta deve ser informado");
			}
			if (!Uteis.isAtributoPreenchido(lancamento.getValor())) {
				throw new StreamSeiException("O campo Valor deve ser informado");
			}
			if (!Uteis.isAtributoPreenchido(lancamento.getTipoOrigemLancamentoContabilEnum())) {
				throw new StreamSeiException("O campo Origem deve ser informado");
			}
			lancamento.setSituacaoLancamentoContabilEnum(SituacaoLancamentoContabilEnum.COMPENSADO);
			lancamento.setDataRegistro(nfe.getDataEntrada());
			lancamento.setDataCompensacao(nfe.getDataEmissao());
			lancamento.setPlanoContaVO(getFacadeFactory().getPlanoContaFacade().consultarPorChavePrimaria(lancamento.getPlanoContaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
			if (nfe.getTipoLancamentoContabilNotaFiscalEntradaEnum().isCategoriaProduto()) {
				addLancamentoContabilPorCategoriaProduto(nfe, lancamento, categoriaProdutoFiltroVO, usuario);
			} else if (nfe.getTipoLancamentoContabilNotaFiscalEntradaEnum().isImposto()) {
				addLancamentoContabilPorImposto(nfe, lancamento, impstoFiltroVO, usuario);
			}
			if (lancamento.getTipoPlanoConta().isCredito()) {
				getFacadeFactory().getLancamentoContabilFacade().preencherListaLancamentoContabilVO(nfe.getListaLancamentoContabeisCredito(), lancamento);
			} else {
				getFacadeFactory().getLancamentoContabilFacade().preencherListaLancamentoContabilVO(nfe.getListaLancamentoContabeisDebito(), lancamento);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}

	}

	private void addLancamentoContabilPorImposto(NotaFiscalEntradaVO nfe, LancamentoContabilVO lancamento, ImpostoVO impstoFiltroVO, UsuarioVO usuario) {
		lancamento.setTipoValorLancamentoContabilEnum(TipoValorLancamentoContabilEnum.NOTA_FISCAL_ENTRADA_IMPOSTO);
		if (!Uteis.isAtributoPreenchido(impstoFiltroVO)) {
			throw new StreamSeiException("O campo Imposto deve ser informado");
		}
		nfe.getListaNotaFiscalEntradaImposto()
				.stream()
				.filter(p -> p.getImpostoVO().getCodigo().equals(impstoFiltroVO.getCodigo()))
				.forEach(p -> getFacadeFactory().getLancamentoContabilFacade().preencherLancamentoContabilPorNotaFiscalEntradaImposto(lancamento, lancamento.getPlanoContaVO(), nfe, p, lancamento.getTipoPlanoConta(), usuario));
	}

	private void addLancamentoContabilPorCategoriaProduto(NotaFiscalEntradaVO nfe, LancamentoContabilVO lancamento, CategoriaProdutoVO categoriaProdutoFiltroVO, UsuarioVO usuario) {
		lancamento.setTipoValorLancamentoContabilEnum(TipoValorLancamentoContabilEnum.NOTA_FISCAL_ENTRADA_CATEGORIA_PRODUTO);
		if (!Uteis.isAtributoPreenchido(categoriaProdutoFiltroVO)) {
			throw new StreamSeiException("O campo Categoria Produto/Serviço deve ser informado");
		}
		for (Map.Entry<Integer, List<NotaFiscalEntradaItemVO>> mapaCategoriaProduto : nfe.getMapaCategoriaProdutoNotaFiscal().entrySet()) {
			if (categoriaProdutoFiltroVO.getCodigo().equals(mapaCategoriaProduto.getKey())) {
				getFacadeFactory().getLancamentoContabilFacade().preencherLancamentoContabilPorNotaFiscalEntradaItem(lancamento, lancamento.getPlanoContaVO(), nfe, mapaCategoriaProduto, lancamento.getTipoPlanoConta(), usuario);
				break;
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removeLancamentoContabilVO(NotaFiscalEntradaVO nfe, LancamentoContabilVO lancamento, UsuarioVO usuario) {
		try {
			if (lancamento.getTipoPlanoConta().isCredito()) {
				getFacadeFactory().getLancamentoContabilFacade().removeLancamentoContabilVO(nfe.getListaLancamentoContabeisCredito(), lancamento, usuario);
			} else {
				getFacadeFactory().getLancamentoContabilFacade().removeLancamentoContabilVO(nfe.getListaLancamentoContabeisDebito(), lancamento, usuario);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void persistirCentroResultadoOrigem(NotaFiscalEntradaVO obj, UsuarioVO usuarioVO) {
		try {
			Uteis.checkState(!obj.getPrecoCentroResultadoTotal().equals(obj.getTotalNotaEntrada()), "Não é possivel realizar essa operação, pois o valor total da nota fiscal de entrada é diferente do total do Centro Resultado Movimentações.");
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().persistir(obj.getListaCentroResultadoOrigemVOs(), obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.NOTA_FISCAL_ENTRADA, false, usuarioVO, false);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void verificarAdiantamentosDisponiveisParaAbatimentoContasPagar(NotaFiscalEntradaVO nfe, UsuarioVO usuario) {
		try {
			if (!Uteis.isAtributoPreenchido(nfe.getListaAdiantamentosUtilizadosAbaterContasPagar())) {
				nfe.setListaAdiantamentosUtilizadosAbaterContasPagar(getFacadeFactory().getContaPagarFacade().consultaRapidaContaPagarAdiantamentoPodemSerUtilizadasParaAbatimentoNegociacaoPagamento(nfe.getUnidadeEnsinoVO().getCodigo(), TipoSacado.FORNECEDOR.getValor(), nfe.getFornecedorVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			}
			getFacadeFactory().getNotaFiscalEntradaFacade().limparDistribuicaoAdiantamentosDisponiveisParaAbaterContasPagar(nfe, true);
			realizarDistribuicaoAutomaticaAdiantamentosDisponiveisNotaFiscalEntrada(nfe, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void limparDistribuicaoAdiantamentosDisponiveisParaAbaterContasPagar(NotaFiscalEntradaVO nfe, Boolean manterListaAdiantamentosParaNovoProcessamento) {
		for (ContaPagarVO adiantamento : nfe.getListaAdiantamentosUtilizadosAbaterContasPagar()) {
			// voltando o valor inicial do adiantamento, para novo processamento. uma vez que a lista esta sendo mantida.
			adiantamento.setValorUtilizadoAdiantamento(adiantamento.getValorUtilizadoAdiantamentoBackup());
		}
		if (!manterListaAdiantamentosParaNovoProcessamento) {
			nfe.setListaAdiantamentosUtilizadosAbaterContasPagar(new ArrayList<>());
		}
		for (ContaPagarVO contaPagar : nfe.getListaContaPagar()) {
			contaPagar.setDescontoPorUsoAdiantamento(0.0);
			contaPagar.setListaContaPagarAdiantamentoVO(new ArrayList<>());
		}
		nfe.setValorTotalAtiantamentosAbaterContasPagar(0.0);
	}
	

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void removerAdiantamentosDisponiveisParaAbaterContasPagar(NotaFiscalEntradaVO nfe, ContaPagarAdiantamentoVO cpa,UsuarioVO usuario) {
		try {
			for (ContaPagarVO contaPagar : nfe.getListaContaPagar()) {
				Iterator<ContaPagarAdiantamentoVO> i = contaPagar.getListaContaPagarAdiantamentoVO().iterator();
				while (i.hasNext()) {
					ContaPagarAdiantamentoVO objExistente = (ContaPagarAdiantamentoVO) i.next();
					if(objExistente.getCodigo().equals(cpa.getCodigo())) {						
						Uteis.checkState(getFacadeFactory().getContaPagarFacade().verificarExistenciaContaPagarRecebidaEmDuplicidade(contaPagar.getCodigo()), "Não é possível remover esse adiantamento, pois a conta pagar já foi paga.");
						contaPagar.setDescontoPorUsoAdiantamento(contaPagar.getDescontoPorUsoAdiantamento() - cpa.getValorUtilizado());
						getFacadeFactory().getContaPagarFacade().alterarValorUtilizadoAdiantamento(cpa.getContaPagarUtilizada(), cpa.getValorUtilizado(),  usuario , false);
						getFacadeFactory().getContaPagarAdiantamentoFacade().excluir(cpa, usuario);
						i.remove();
					}
				}
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * Método responsavel, por distribuir automaticamente todos os adiantamentos disponiveis para abatimento, para as contas a pagar selecionadas para a negociacao de pagamento. Assim, já teremos uma proposta automatica e ideal de distribuicao para o usuario. Permitindo, posteriormente ao mesmo alterar essa distribuicao da melhor forma que desejar.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarDistribuicaoAutomaticaAdiantamentosDisponiveisNotaFiscalEntrada(NotaFiscalEntradaVO nfe, UsuarioVO usuario) {
		try {
			if (!Uteis.isAtributoPreenchido(nfe.getListaAdiantamentosUtilizadosAbaterContasPagar()) || !Uteis.isAtributoPreenchido(nfe.getListaContaPagar())) {
				return; // nao existem adiantamentos para serem considerados
			}
			double valorTotalAdiantamentoAbatidoContasAPagar = 0.0;			
			for (ContaPagarVO contaPagar : nfe.getListaContaPagar()) {
				valorTotalAdiantamentoAbatidoContasAPagar = realizarDistribuicaoAutomaticaAdiantamentosDisponiveisParaAbaterContasPagar(nfe, contaPagar, valorTotalAdiantamentoAbatidoContasAPagar, usuario);
			}
			nfe.setValorTotalAtiantamentosAbaterContasPagar(valorTotalAdiantamentoAbatidoContasAPagar);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public double realizarDistribuicaoAutomaticaAdiantamentosDisponiveisParaAbaterContasPagar(NotaFiscalEntradaVO nfe, ContaPagarVO contaPagar, double valorTotalAdiantamentoAbatidoContasAPagar, UsuarioVO usuario) throws Exception {
		List<LancamentoContabilVO> listaTemp = new ArrayList<>();
		contaPagar.getListaLancamentoContabeisCredito().clear();
		contaPagar.getListaLancamentoContabeisDebito().clear();
		Double valorAdiantamentoAbatidoEmContaPagar = getFacadeFactory().getContaPagarFacade().realizarDistribuicaoAdiantamentosDisponiveisParaAbaterContaPagar(contaPagar, contaPagar.getValorPrevisaoPagamentoNotaFiscal(), nfe.getListaAdiantamentosUtilizadosAbaterContasPagar(), false);
		if(Uteis.isAtributoPreenchido(valorAdiantamentoAbatidoEmContaPagar) && contaPagar.getValorPrevisaoPagamentoNotaFiscal().equals(0.0)){
			if(contaPagar.getJuro().equals(0.0) && contaPagar.getMulta().equals(0.0)) {
				getFacadeFactory().getLancamentoContabilFacade().gerarLancamentoContabilPorContaPagar(listaTemp, new Date(), contaPagar, null, contaPagar.getValorPrevisaoPagamentoNotaFiscal(), true, usuario);
				if (Uteis.isAtributoPreenchido(listaTemp)) {
					contaPagar.setLancamentoContabil(true);
					listaTemp.stream().forEach(objExistente -> {
						if (objExistente.getTipoPlanoConta().isCredito()) {
							contaPagar.getListaLancamentoContabeisCredito().add(objExistente);
						} else {
							contaPagar.getListaLancamentoContabeisDebito().add(objExistente);
						}
					});
				}	
			}
			valorTotalAdiantamentoAbatidoContasAPagar = valorTotalAdiantamentoAbatidoContasAPagar + valorAdiantamentoAbatidoEmContaPagar;
		}else{
			contaPagar.getListaContaPagarAdiantamentoVO().clear();
			contaPagar.setDescontoPorUsoAdiantamento(0.0);
		}		
		return valorTotalAdiantamentoAbatidoContasAPagar;
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP PlanoContaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void consultar(DataModelo dataModelo, NotaFiscalEntradaVO obj, Date dataEntradaInicio, Date dataEntradaFim) {

		List<NotaFiscalEntradaVO> objs = new ArrayList<>();
		objs = consultaRapidaPorFiltros(obj, dataModelo,  dataEntradaInicio, dataEntradaFim);
		dataModelo.getListaFiltros().clear();
		dataModelo.setTotalRegistrosEncontrados(consultarTotalPorFiltros(obj, dataModelo, dataEntradaInicio, dataEntradaFim));
		/*switch (NotaFiscalEntradaVO.enumCampoConsultaNotaFiscalEntrada.valueOf(dataModelo.getCampoConsulta())) {
		case CODIGO:
			if (dataModelo.getValorConsulta().isEmpty()) {
				dataModelo.setValorConsulta("0");
			}
			int valorInt = Integer.parseInt(dataModelo.getValorConsulta());
			objs = getFacadeFactory().getNotaFiscalEntradaFacade().consultaRapidaPorCodigo(valorInt, dataModelo);
			dataModelo.getListaFiltros().clear();
			dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getNotaFiscalEntradaFacade().consultarTotalPorCodigo(valorInt, dataModelo));
			break;
		case NUMERO:
			if (dataModelo.getValorConsulta().isEmpty()) {
				dataModelo.setValorConsulta("0");
			}
			Long valorLong = Long.parseLong(dataModelo.getValorConsulta());
			objs = getFacadeFactory().getNotaFiscalEntradaFacade().consultaRapidaPorNumero(valorLong, dataModelo);
			dataModelo.getListaFiltros().clear();
			dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getNotaFiscalEntradaFacade().consultarTotalPorNumero(valorLong, dataModelo));
			break;
		case UNIDADEENSINO:
			objs = getFacadeFactory().getNotaFiscalEntradaFacade().consultaRapidaPorUnidadeEnsino(Uteis.getValorInteiro(dataModelo.getValorConsulta()), dataModelo);
			dataModelo.getListaFiltros().clear();
			dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getNotaFiscalEntradaFacade().consultarTotalPorUnidadeEnsino(Uteis.getValorInteiro(dataModelo.getValorConsulta()), dataModelo));
			break;
		case FORNECEDOR:
			objs = getFacadeFactory().getNotaFiscalEntradaFacade().consultaRapidaPorFornecedor(dataModelo.getValorConsulta(), dataModelo);
			dataModelo.getListaFiltros().clear();
			dataModelo.setTotalRegistrosEncontrados(getFacadeFactory().getNotaFiscalEntradaFacade().consultarTotalPorFornecedor(dataModelo.getValorConsulta(), dataModelo));
			break;
		default:
			break;
		}*/

		dataModelo.setListaConsulta(objs);

	}

	private StringBuilder getSQLPadraoConsultaTotalBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(nfe.codigo) as qtde FROM NotaFiscalEntrada as nfe ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = nfe.unidadeensino");
		sql.append(" inner join fornecedor on fornecedor.codigo = nfe.fornecedor");
		
		sql.append(" left JOIN naturezaOperacao ON naturezaOperacao.codigo = nfe.naturezaOperacao ");
		return sql;
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT nfe.codigo as \"nfe.codigo\", nfe.dataEntrada as \"nfe.dataEntrada\", ");
		sql.append(" nfe.dataEmissao as \"nfe.dataEmissao\", nfe.numero as \"nfe.numero\", ");
		sql.append(" nfe.serie as \"nfe.serie\", nfe.tipoNotaFiscalEntrada as \"nfe.tipoNotaFiscalEntrada\", ");
		sql.append(" nfe.totalNotaEntrada as \"nfe.totalNotaEntrada\", nfe.totalContaPagar as \"nfe.totalContaPagar\", ");
		sql.append(" nfe.totalImpostoRetido as \"nfe.totalImpostoRetido\", nfe.liquidoPagar as \"nfe.liquidoPagar\", ");
		sql.append(" nfe.totalImpostoPorcentagem as \"nfe.totalImpostoPorcentagem\", nfe.totalImpostoValor as \"nfe.totalImpostoValor\", ");
		sql.append(" nfe.totalRecebimentoCompra as \"nfe.totalRecebimentoCompra\",  ");

		sql.append(" unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\", ");

		sql.append(" fornecedor.codigo as \"fornecedor.codigo\", fornecedor.nome as \"fornecedor.nome\", fornecedor.CNPJ as \"fornecedor.CNPJ\", fornecedor.CPF as \"fornecedor.CPF\", fornecedor.tipoEmpresa as \"fornecedor.tipoEmpresa\", ");

		sql.append(" naturezaOperacao.codigo as \"naturezaOperacao.codigo\", naturezaOperacao.nome as \"naturezaOperacao.nome\" ");

		sql.append(" FROM NotaFiscalEntrada nfe");
		sql.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.codigo = nfe.unidadeEnsino ");
		sql.append(" INNER JOIN fornecedor ON fornecedor.codigo = nfe.fornecedor ");
		
		sql.append(" left JOIN naturezaOperacao ON naturezaOperacao.codigo = nfe.naturezaOperacao ");
		return sql;
	}
	
	
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	private List<NotaFiscalEntradaVO> consultaRapidaPorFiltros(NotaFiscalEntradaVO obj, DataModelo dataModelo, Date dataEntradaInicio, Date dataEntradaFim) {
		try {
			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr, dataEntradaInicio, dataEntradaFim);
			sqlStr.append(" ORDER BY nfe.dataEmissao desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsultaBasica(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private Integer consultarTotalPorFiltros(NotaFiscalEntradaVO obj, DataModelo dataModelo, Date dataEntradaInicio, Date dataEntradaFim) {
		try {
			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr, dataEntradaInicio, dataEntradaFim);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarFiltrosParaConsulta(NotaFiscalEntradaVO obj, DataModelo dataModelo, StringBuilder sqlStr, Date dataEntradaInicio, Date dataEntradaFim) {
		if(Uteis.isAtributoPreenchido(obj.getCodigo()) && !obj.getCodigo().equals(0)){
			sqlStr.append(" and nfe.codigo = ? ");
			dataModelo.getListaFiltros().add(obj.getCodigo());	
		}
		if(Uteis.isAtributoPreenchido(obj.getNumero())){
			sqlStr.append(" and nfe.numero = ? ");
			dataModelo.getListaFiltros().add(obj.getNumero());	
		}
		if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO())) {
			sqlStr.append(" and unidadeEnsino.codigo = ? ");
			dataModelo.getListaFiltros().add(obj.getUnidadeEnsinoVO().getCodigo());
		}
		if (Uteis.isAtributoPreenchido(obj.getFornecedorVO().getNome())) {
			sqlStr.append(" and lower(sem_acentos(fornecedor.nome)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getFornecedorVO().getNome().toLowerCase() + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(obj.getFornecedorVO().getCNPJ())) {
			sqlStr.append(" and fornecedor.CNPJ like(?)");
			dataModelo.getListaFiltros().add(PERCENT + obj.getFornecedorVO().getCNPJ() + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(obj.getFornecedorVO().getCPF())) {
			sqlStr.append(" and fornecedor.CPF like(?)");
			dataModelo.getListaFiltros().add(PERCENT + obj.getFornecedorVO().getCPF() + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(obj.getFornecedorVO().getCNPJ())) {
			sqlStr.append(" and fornecedor.cnpj like(?)");
			dataModelo.getListaFiltros().add(PERCENT + obj.getFornecedorVO().getCNPJ() + PERCENT);
		}
		if (Uteis.isAtributoPreenchido(dataModelo.getDataIni())) {
			sqlStr.append(" and nfe.dataEmissao >= '").append(Uteis.getDataBD0000(dataModelo.getDataIni())).append("' ");			
		}
		if (Uteis.isAtributoPreenchido(dataModelo.getDataFim())) {
			sqlStr.append(" and nfe.dataEmissao <= '").append(Uteis.getDataBD2359(dataModelo.getDataFim())).append("' ");
		}
		if (Uteis.isAtributoPreenchido(dataEntradaInicio)) {
			sqlStr.append(" and nfe.dataEntrada >= '").append(Uteis.getDataBD0000(dataEntradaInicio)).append("' ");
		}
		if (Uteis.isAtributoPreenchido(dataEntradaFim)) {
			sqlStr.append(" and nfe.dataEntrada <= '").append(Uteis.getDataBD2359(dataEntradaFim)).append("' ");
		}
		if(Uteis.isAtributoPreenchido(obj.getNaturezaOperacaoVO().getNome())) {
			sqlStr.append(" and lower(sem_acentos(naturezaOperacao.nome)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + obj.getNaturezaOperacaoVO().getNome().toLowerCase() + PERCENT);
		}
		if(Uteis.isAtributoPreenchido(obj.getTipoNotaFiscalEntradaEnum())) {
			sqlStr.append(" and nfe.tiponotafiscalentrada = ?");
			dataModelo.getListaFiltros().add(obj.getTipoNotaFiscalEntradaEnum().name());
		}
		
		if(Uteis.isAtributoPreenchido(obj.getTotalNotaEntradaIncio())) {
			sqlStr.append(" and nfe.totalnotaentrada >= ").append(obj.getTotalNotaEntradaIncio()).append(" ");
		}
		if(Uteis.isAtributoPreenchido(obj.getTotalNotaEntradaFim())) {
			sqlStr.append(" and nfe.totalnotaentrada <= ").append(obj.getTotalNotaEntradaFim()).append(" ");
		}
		if(Uteis.isAtributoPreenchido(obj.getNotaEntradaLiquidoAPagarInicio())) {
			sqlStr.append(" and nfe.liquidopagar >= ").append(obj.getNotaEntradaLiquidoAPagarInicio()).append(" ");
		}
		if(Uteis.isAtributoPreenchido(obj.getNotaEntradaLiquidoAPagarFim())) {
			sqlStr.append(" and nfe.liquidopagar <= ").append(obj.getNotaEntradaLiquidoAPagarFim()).append(" ");
		}
		
		if(Uteis.isAtributoPreenchido(obj.getProdutoServicoVO().getCodigo())) {
			sqlStr.append(" and exists (select notafiscalentradaitem.codigo from notafiscalentradaitem ");
			sqlStr.append(" INNER JOIN produtoservico on notafiscalentradaitem.produtoservico =  produtoservico.codigo ");
			sqlStr.append(" where notafiscalentradaitem.notafiscalentrada = nfe.codigo and  produtoservico.codigo = ").append(obj.getProdutoServicoVO().getCodigo()).append(") ");
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaInterfaceFacade#consultaRapidaPorCodigo(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaVO> consultaRapidaPorCodigo(Integer valorConsulta, DataModelo dataModelo) {
		try {

			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE nfe.codigo >= ? ");
			dataModelo.getListaFiltros().add(valorConsulta);
			sqlStr.append(" ORDER BY nfe.codigo desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsultaBasica(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalPorCodigo(Integer valorConsulta, DataModelo dataModelo) {
		try {

			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE nfe.codigo >= ? ");
			dataModelo.getListaFiltros().add(valorConsulta);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaInterfaceFacade#consultaRapidaPorNumero(java.lang.Long, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaVO> consultaRapidaPorNumero(Long numero, DataModelo dataModelo) {
		try {

			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE nfe.numero >= ? ");
			dataModelo.getListaFiltros().add(numero);
			sqlStr.append(" ORDER BY nfe.numero desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsultaBasica(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalPorNumero(Long valorConsulta, DataModelo dataModelo) {
		try {

			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE nfe.numero = ?");
			dataModelo.getListaFiltros().add(valorConsulta);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaInterfaceFacade#consultaRapidaPorUnidadeEnsino(java.lang.String, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaVO> consultaRapidaPorUnidadeEnsino(Integer valorConsulta, DataModelo dataModelo) {
		try {

			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE 1=1 ");
			if (Uteis.isAtributoPreenchido(valorConsulta)) {
				sqlStr.append(" and unidadeEnsino.codigo = ? ");
				dataModelo.getListaFiltros().add(valorConsulta);
			}
			sqlStr.append(" ORDER BY unidadeEnsino.nome desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsultaBasica(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalPorUnidadeEnsino(Integer valorConsulta, DataModelo dataModelo) {
		try {

			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE 1=1 ");
			if (Uteis.isAtributoPreenchido(valorConsulta)) {
				sqlStr.append(" and unidadeEnsino.codigo = ? ");
				dataModelo.getListaFiltros().add(valorConsulta);
			}
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaInterfaceFacade#consultaRapidaPorFornecedor(java.lang.String, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaVO> consultaRapidaPorFornecedor(String valorConsulta, DataModelo dataModelo) {
		try {

			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE lower(sem_acentos(fornecedor.nome)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + valorConsulta.toLowerCase() + PERCENT);
			sqlStr.append(" ORDER BY fornecedor.nome desc ");
			UteisTexto.addLimitAndOffset(sqlStr, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return montarDadosConsultaBasica(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public Integer consultarTotalPorFornecedor(String valorConsulta, DataModelo dataModelo) {
		try {

			StringBuilder sqlStr = getSQLPadraoConsultaTotalBasica();
			sqlStr.append(" WHERE lower(sem_acentos(fornecedor.nome)) like(lower(sem_acentos(?)))");
			dataModelo.getListaFiltros().add(PERCENT + valorConsulta.toLowerCase() + PERCENT);
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	public String consultarNumeroDasNotasFiscaisPorCompra(Integer compra, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder(" ");
		sql.append(" SELECT notafiscalentrada.numero FROM NotaFiscalEntrada ");
		sql.append(" inner join notafiscalentradarecebimentocompra on notafiscalentrada.codigo = notafiscalentradarecebimentocompra.notafiscalentrada");
		sql.append(" inner join recebimentocompra on recebimentocompra.codigo = notafiscalentradarecebimentocompra.recebimentocompra    ");
		sql.append(" where recebimentocompra.compra = ?");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), compra);
		StringBuilder numero = new StringBuilder("");
		while (rs.next()) {
			String campo = rs.getString("numero");
			numero.append(numero.toString().isEmpty() ? campo : ", " + campo);
		}
		return numero.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer, boolean, int, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public NotaFiscalEntradaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			String sql = "SELECT * FROM NotaFiscalEntrada WHERE codigo = ?";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
			if (!tabelaResultado.next()) {
				throw new StreamSeiException("Dados Não Encontrados ( NotaFiscalEntradaVO ).");
			}
			return (montarDados(tabelaResultado, nivelMontarDados, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private Boolean validarUnicidade(NotaFiscalEntradaVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT codigo FROM NotaFiscalEntrada ");
		sql.append(" WHERE numero = ? and fornecedor = ? and serie = ? ");
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" and codigo != ").append(obj.getCodigo()).append(" ");
		}
		return getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getNumero(), obj.getFornecedorVO().getCodigo(), obj.getSerie()).next();
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
		List<NotaFiscalEntradaVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			NotaFiscalEntradaVO obj = new NotaFiscalEntradaVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(NotaFiscalEntradaVO obj, SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		try {
			obj.setNovoObj(Boolean.FALSE);
			obj.setCodigo(dadosSQL.getInt("nfe.codigo"));
			obj.setNumero(dadosSQL.getLong("nfe.numero"));
			obj.setSerie(dadosSQL.getString("nfe.serie"));
			obj.setTipoNotaFiscalEntradaEnum(TipoNotaFiscalEntradaEnum.valueOf(dadosSQL.getString("nfe.tipoNotaFiscalEntrada")));
			obj.setDataEntrada(dadosSQL.getTimestamp("nfe.dataEntrada"));
			obj.setDataEmissao(dadosSQL.getTimestamp("nfe.dataEmissao"));
			obj.setTotalNotaEntrada(dadosSQL.getDouble("nfe.totalNotaEntrada"));
			obj.setTotalContaPagar(dadosSQL.getDouble("nfe.totalContaPagar"));
			obj.setTotalImpostoRetido(dadosSQL.getDouble("nfe.totalImpostoRetido"));
			obj.setLiquidoPagar(dadosSQL.getDouble("nfe.liquidoPagar"));
			obj.setTotalImpostoPorcentagem(dadosSQL.getDouble("nfe.totalImpostoPorcentagem"));
			obj.setTotalImpostoValor(dadosSQL.getDouble("nfe.totalImpostoValor"));
			obj.setTotalRecebimentoCompra(dadosSQL.getDouble("nfe.totalRecebimentoCompra"));
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
				return;
			}
			obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino.codigo"));
			obj.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeEnsino.nome"));
			obj.getFornecedorVO().setCodigo(dadosSQL.getInt("fornecedor.codigo"));
			obj.getFornecedorVO().setNome(dadosSQL.getString("fornecedor.nome"));
			obj.getFornecedorVO().setCNPJ(dadosSQL.getString("fornecedor.CNPJ"));
			obj.getFornecedorVO().setCPF(dadosSQL.getString("fornecedor.CPF"));
			obj.getFornecedorVO().setTipoEmpresa(dadosSQL.getString("fornecedor.tipoEmpresa"));
			obj.getNaturezaOperacaoVO().setCodigo(dadosSQL.getInt("naturezaOperacao.codigo"));
			obj.getNaturezaOperacaoVO().setNome(dadosSQL.getString("naturezaOperacao.nome"));
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
				return;
			}
			getFacadeFactory().getNotaFiscalEntradaRecebimentoCompraFacade().consultaRapidaPorNotaFiscalEntrada(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			getFacadeFactory().getNotaFiscalEntradaItemFacade().consultaRapidaPorNotaFiscalEntrada(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
			getFacadeFactory().getNotaFiscalEntradaImpostoFacade().consultaRapidaPorNotaFiscalEntrada(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);

		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private NotaFiscalEntradaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
		try {
			NotaFiscalEntradaVO obj = new NotaFiscalEntradaVO();
			obj.setNovoObj(Boolean.FALSE);
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setNumero(dadosSQL.getLong("numero"));
			obj.setSerie(dadosSQL.getString("serie"));
			obj.setTipoNotaFiscalEntradaEnum(TipoNotaFiscalEntradaEnum.valueOf(dadosSQL.getString("tipoNotaFiscalEntrada")));
			obj.setDataEntrada(dadosSQL.getTimestamp("dataEntrada"));
			obj.setDataEntradaAnterior(dadosSQL.getTimestamp("dataEntrada"));
			
			obj.setDataEmissao(dadosSQL.getTimestamp("dataEmissao"));
			obj.setTotalNotaEntrada(dadosSQL.getDouble("totalNotaEntrada"));
			obj.setTotalContaPagar(dadosSQL.getDouble("totalContaPagar"));
			obj.setTotalImpostoRetido(dadosSQL.getDouble("totalImpostoRetido"));
			obj.setLiquidoPagar(dadosSQL.getDouble("liquidoPagar"));
			obj.setTotalImpostoPorcentagem(dadosSQL.getDouble("totalImpostoPorcentagem"));
			obj.setTotalImpostoValor(dadosSQL.getDouble("totalImpostoValor"));
			obj.setTotalRecebimentoCompra(dadosSQL.getDouble("totalRecebimentoCompra"));
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
				return obj;
			}
			obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
			obj.getFornecedorVO().setCodigo(dadosSQL.getInt("fornecedor"));
			obj.getNaturezaOperacaoVO().setCodigo(dadosSQL.getInt("naturezaOperacao"));
			if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
				return obj;
			}
			obj.setUnidadeEnsinoVO(Uteis.montarDadosVO(dadosSQL.getInt("unidadeEnsino"), UnidadeEnsinoVO.class, p -> getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
			obj.setFornecedorVO(Uteis.montarDadosVO(dadosSQL.getInt("fornecedor"), FornecedorVO.class, p -> getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
			obj.setNaturezaOperacaoVO(Uteis.montarDadosVO(dadosSQL.getInt("naturezaOperacao"), NaturezaOperacaoVO.class, p -> getFacadeFactory().getNaturezaOperacaoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
			obj.setListaNotaFiscalEntradaRecebimentoCompra(getFacadeFactory().getNotaFiscalEntradaRecebimentoCompraFacade().consultaRapidaPorNotaFiscalEntrada(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			obj.setListaNotaFiscalEntradaItem(getFacadeFactory().getNotaFiscalEntradaItemFacade().consultaRapidaPorNotaFiscalEntrada(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			obj.setListaNotaFiscalEntradaImposto(getFacadeFactory().getNotaFiscalEntradaImpostoFacade().consultaRapidaPorNotaFiscalEntrada(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
			montarDadosContaPagar(obj, usuario);
			montarDadosLancamentoContabil(obj, usuario);
			montarDadosCentroResultadoOrigem(obj, usuario);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarDadosContaPagar(NotaFiscalEntradaVO obj, UsuarioVO usuario) {
		try {
			obj.getListaContaPagarExcluidas().clear();
			obj.setListaContaPagar(getFacadeFactory().getContaPagarFacade().consultarPorNotaFiscalEntrada(obj, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
			obj.setListaContaPagarOutrasOrigem(getFacadeFactory().getContaPagarFacade().consultarPorNotaFiscalEntradaOutrasOrigem(obj, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarDadosLancamentoContabil(NotaFiscalEntradaVO obj, UsuarioVO usuario) {
		try {
			obj.setLancamentoContabil(getFacadeFactory().getConfiguracaoContabilFacade().consultaSeExisteConfiguracaoContabilPorCodigoUnidadeEnsino(obj.getUnidadeEnsinoVO().getCodigo(), usuario));
			if (obj.isLancamentoContabil()) {
				obj.getListaLancamentoContabeisCredito().addAll(getFacadeFactory().getLancamentoContabilFacade().consultaRapidaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(obj.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.NOTA_FISCAL_ENTRADA, TipoPlanoContaEnum.CREDITO, false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
				obj.getListaLancamentoContabeisDebito().addAll(getFacadeFactory().getLancamentoContabilFacade().consultaRapidaPorCodOrigemPorTipoOrigemPorTipoPlanoConta(obj.getCodigo().toString(), TipoOrigemLancamentoContabilEnum.NOTA_FISCAL_ENTRADA, TipoPlanoContaEnum.DEBITO, false, Uteis.NIVELMONTARDADOS_TODOS, usuario));
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}

	}

	private void montarDadosCentroResultadoOrigem(NotaFiscalEntradaVO obj, UsuarioVO usuario) {
		try {
			obj.getListaCentroResultadoOrigemVOs().addAll(getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.NOTA_FISCAL_ENTRADA, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
	 */
	public static String getIdEntidade() {
		return NotaFiscalEntrada.idEntidade;
	}
	
	//Exclui o código e o número da nota fiscal da conta a pagar 
	public void excluirCodigoENumeroNotaFiscalContaPagar(NotaFiscalEntradaVO obj, UsuarioVO usuario) throws Exception {
		
		for (ContaPagarVO contaPagar : obj.getListaContaPagarOutrasOrigem()) {	
			StringBuilder codigoNota = new StringBuilder();
			StringBuilder nrNota = new StringBuilder();
			
			if(contaPagar.getCodigoNotaFiscalEntrada().contains(",")) {
				
				 String[] arrayCodigoNotaFiscal = contaPagar.getCodigoNotaFiscalEntrada().split(",");
			     List<String> itemListCodigoNotaFiscal = new ArrayList<String>(Arrays.asList(arrayCodigoNotaFiscal));

			     itemListCodigoNotaFiscal.remove(obj.getCodigo().toString());

			     codigoNota.append(String.join(", ", itemListCodigoNotaFiscal)); 
			     
			     contaPagar.setCodigoNotaFiscalEntrada(codigoNota.toString());
			}else {
				contaPagar.setCodigoNotaFiscalEntrada(null);
			}
			
			if(contaPagar.getNumeroNotaFiscalEntrada().contains(",")) {
				
				 String[] arrayNumeroNotaFiscal = contaPagar.getNumeroNotaFiscalEntrada().split(",");
			     List<String> itemListNumeroNotaFiscal = new ArrayList<String>(Arrays.asList(arrayNumeroNotaFiscal));

			     itemListNumeroNotaFiscal.remove(obj.getNumero().toString());

			     nrNota.append(String.join(", ", itemListNumeroNotaFiscal)); 
			     
			     contaPagar.setNumeroNotaFiscalEntrada(nrNota.toString());
			}else {
				contaPagar.setNumeroNotaFiscalEntrada(null);
			}
			
			getFacadeFactory().getContaPagarFacade().alterar(contaPagar, false, false, usuario);
		}
		
		
	}
	
	private StringBuilder getSQLPadraoConsultaTotalNotaFiscalEntrada() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT sum(nfe.totalNotaEntrada) as valorNotaEntrada FROM NotaFiscalEntrada as nfe ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = nfe.unidadeensino");
		sql.append(" inner join fornecedor on fornecedor.codigo = nfe.fornecedor");
		
		sql.append(" left JOIN naturezaOperacao ON naturezaOperacao.codigo = nfe.naturezaOperacao ");
		return sql;
	}

	public Double consultarTotalNotaFiscalEntradaPorFiltros(NotaFiscalEntradaVO obj, DataModelo dataModelo, Date dataEntradaInicio, Date dataEntradaFim) {
		try {
			dataModelo.getListaFiltros().clear();
			StringBuilder sqlStr = getSQLPadraoConsultaTotalNotaFiscalEntrada();
			sqlStr.append(" WHERE 1= 1 ");
			montarFiltrosParaConsulta(obj, dataModelo, sqlStr, dataEntradaInicio, dataEntradaFim);
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
			if (tabelaResultado.next()) {
				return tabelaResultado.getDouble("valorNotaEntrada");
			}
			return 0.0;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	private void validarDadosTotalizadoresLancamentoContabilPadrao(NotaFiscalEntradaVO obj, UsuarioVO usuario) {
		BigDecimal totalValorNotaEntrada = new BigDecimal(String.valueOf(obj.getTotalNotaEntrada()));
		if (obj.getListaLancamentoContabeisCredito().isEmpty() || obj.getListaLancamentoContabeisDebito().isEmpty() 
				|| obj.getValorTotalImpostoRetidoPorcentagem() <= 0.0 || totalValorNotaEntrada.doubleValue() <= 0.0) {
			return;
		}
		BigDecimal valorLancadoCredito = obj.getListaLancamentoContabeisCredito()
				.stream().map(LancamentoContabilVO::getValor).map(converterDoubleParaBigDecimal()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
		BigDecimal valorLancadoDebito = obj.getListaLancamentoContabeisDebito()
				.stream().map(LancamentoContabilVO::getValor).map(converterDoubleParaBigDecimal()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
		BigDecimal valorDiferencaCredito = totalValorNotaEntrada.subtract(valorLancadoCredito.setScale(2, RoundingMode.HALF_EVEN));
		if (valorDiferencaCredito.doubleValue() == 0.01 || valorDiferencaCredito.doubleValue() == -0.01) {
			Comparator<LancamentoContabilVO> comparador = (l1, l2) -> l1.getCategoriaProdutoVO().getCodigo().compareTo(l2.getCategoriaProdutoVO().getCodigo());
			LancamentoContabilVO lancamentoNaoImposto = obj.getListaLancamentoContabeisCredito().stream().sorted(comparador.reversed()).findFirst().get();
			lancamentoNaoImposto.setValor(Uteis.arrendondarForcando2CadasDecimais(valorDiferencaCredito.doubleValue() + lancamentoNaoImposto.getValor()));
		}
		BigDecimal valorDiferencaDebito = totalValorNotaEntrada.subtract(valorLancadoDebito.setScale(2, RoundingMode.HALF_EVEN));
		if (valorDiferencaDebito.doubleValue() == 0.01 || valorDiferencaDebito.doubleValue() == -0.01) {
			LancamentoContabilVO primeiro = obj.getListaLancamentoContabeisDebito().get(0);
			primeiro.setValor(Uteis.arrendondarForcando2CadasDecimais(valorDiferencaDebito.doubleValue() + primeiro.getValor()));
		}
	}
	
	@Override
	public void adicionarCentroResultadoOrigemNotaFiscalEntradaItemPorRateioCategoriaDespesa(NotaFiscalEntradaVO obj, NotaFiscalEntradaItemVO notaFiscalItemVO, CentroResultadoOrigemVO centroResultadoOrigemVO, UsuarioVO usuario) throws Exception{
		centroResultadoOrigemVO.setQuantidade(1.0);
		centroResultadoOrigemVO.setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR);
		getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigemPorRateioCategoriaDespesa(notaFiscalItemVO.getListaCentroResultadoOrigemVOs(), centroResultadoOrigemVO, notaFiscalItemVO.getValorTotal(), true, usuario);
		getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarValidacaoPorcentagem(notaFiscalItemVO.getListaCentroResultadoOrigemVOs(), notaFiscalItemVO.getValorTotal(), usuario);
		atualizarListasDaNotaFiscalEntrada(obj, usuario);
	}
}
