package negocio.facade.jdbc.compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
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

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.CompraItemVO;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.CondicaoPagamentoVO;
import negocio.comuns.compras.CotacaoHistoricoVO;
import negocio.comuns.compras.CotacaoVO;
import negocio.comuns.compras.EstatisticaCompraVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.compras.ItemSumarioUnidadeEstatisticaVO;
import negocio.comuns.compras.ParcelaCondicaoPagamentoVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.RecebimentoCompraItemVO;
import negocio.comuns.compras.RecebimentoCompraVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.enumeradores.TipoCriacaoContaPagarEnum;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.CompraInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class Compra extends ControleAcesso implements CompraInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4283041161788287441L;
	protected static String idEntidade;

	public Compra() {
		super();
		setIdEntidade("Compra");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CompraVO obj, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		try {
			CompraVO.validarDados(obj);
			Compra.incluir(getIdEntidade(), controlarAcesso, usuario);
			obj.realizarUpperCaseDados();
			final String sql = "INSERT INTO Compra( fornecedor, situacaoFinanceira, situacaoRecebimento, cotacao, responsavel, data, formaPagamento, condicaoPagamento, tipoCriacaoContaPagar, unidadeEnsino, contaCorrente, categoriaProduto, dataPrevisaoEntrega ) " 
			+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					sqlInserir.setInt(1, obj.getFornecedor().getCodigo().intValue());
					sqlInserir.setString(2, obj.getSituacaoFinanceira());
					sqlInserir.setString(3, obj.getSituacaoRecebimento());
					if (obj.getCotacao().getCodigo().intValue() != 0) {
						sqlInserir.setInt(4, obj.getCotacao().getCodigo().intValue());
					} else {
						sqlInserir.setNull(4, 0);
					}
					if (obj.getResponsavel().getCodigo().intValue() != 0) {
						sqlInserir.setInt(5, obj.getResponsavel().getCodigo().intValue());
					} else {
						sqlInserir.setNull(5, 0);
					}
					sqlInserir.setTimestamp(6, Uteis.getDataJDBCTimestamp(obj.getData()));
					
					sqlInserir.setInt(7, obj.getFormaPagamento().getCodigo().intValue());
					sqlInserir.setInt(8, obj.getCondicaoPagamento().getCodigo().intValue());
					sqlInserir.setString(9, obj.getTipoCriacaoContaPagarEnum().name());
					sqlInserir.setInt(10, obj.getUnidadeEnsino().getCodigo().intValue());
					if (obj.getContaCorrente().getCodigo().intValue() != 0) {
						sqlInserir.setInt(11, obj.getContaCorrente().getCodigo().intValue());
					} else {
						sqlInserir.setNull(11, 0);
					}
					int i = 11;
					Uteis.setValuePreparedStatement(obj.getCategoriaProduto(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataPrevisaoEntrega(), ++i, sqlInserir);

					return sqlInserir;
				}
			}, new ResultSetExtractor<Object>() {

				public Object extractData(ResultSet arg0) throws SQLException {
					if (arg0.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return arg0.getInt("codigo");
					}
					return null;
				}
			}));
			validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getCompraItemVOs(), "compraitem", "compra", obj.getCodigo(), usuario);
			getFacadeFactory().getCompraItemFacade().persistir(obj.getCompraItemVOs(), usuario);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().persistir(obj.getListaCentroResultadoOrigemVOs(), obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.COMPRA,false, usuario, false);
			validarDadosParaCriarContaPagar(obj, usuario);
			criarRecebimentoCompra(obj, controlarAcesso, usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setCodigo(0);
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	private void criarRecebimentoCompra(CompraVO obj, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		if (obj.getCompraItemVOs().stream().anyMatch(p -> p.getProduto().getTipoProdutoServicoEnum().isServico())) {
			RecebimentoCompraVO recebimentoCompraVO = new RecebimentoCompraVO("PR", obj);
			obj.getCompraItemVOs()
			.stream()
			.filter(p -> p.getProduto().getTipoProdutoServicoEnum().isServico())
			.forEach(compraItem ->criarRecebimentoCompraItem(recebimentoCompraVO, compraItem));
			getFacadeFactory().getRecebimentoCompraFacade().persistir(recebimentoCompraVO, controlarAcesso, usuario);	
		}
		if (obj.getCompraItemVOs().stream().anyMatch(p -> p.getProduto().getTipoProdutoServicoEnum().isProduto())) {
			RecebimentoCompraVO recebimentoCompraVO = new RecebimentoCompraVO("PR", obj);
			obj.getCompraItemVOs()
			.stream()
			.filter(p -> p.getProduto().getTipoProdutoServicoEnum().isProduto())
			.forEach(compraItem ->criarRecebimentoCompraItem(recebimentoCompraVO, compraItem));
			getFacadeFactory().getRecebimentoCompraFacade().persistir(recebimentoCompraVO, controlarAcesso, usuario);	
		}
	}

	private void criarRecebimentoCompraItem(RecebimentoCompraVO recebimentoCompraVO, CompraItemVO compraItem) {
		if (compraItem.getQuantidade() - compraItem.getQuantidadeRecebida() > 0.0) {
			RecebimentoCompraItemVO recebimentoItem = new RecebimentoCompraItemVO();
			recebimentoItem.setRecebimentoCompraVO(recebimentoCompraVO);
			recebimentoItem.setCompraItem(compraItem);
			recebimentoItem.setQuantidadeRecebida(compraItem.getQuantidade() - compraItem.getQuantidadeRecebida());
			recebimentoItem.setValorUnitario(compraItem.getPrecoUnitario());
			recebimentoItem.setValorTotal(compraItem.getPrecoUnitario() * recebimentoItem.getQuantidadeRecebida());
			recebimentoCompraVO.setValorTotal(recebimentoCompraVO.getValorTotal() + recebimentoItem.getValorTotal());
			recebimentoCompraVO.getRecebimentoCompraItemVOs().add(recebimentoItem);
		}
	}
	
	private void validarDadosParaCriarContaPagar(CompraVO obj, UsuarioVO usuario) {
		try {
			if (obj.getTipoCriacaoContaPagarEnum().isCompra()) {
				List<ParcelaCondicaoPagamentoVO> listaParcelaCondicaoPagamento = getFacadeFactory().getParcelaCondicaoPagamentoFacade().consultarParcelaCondicaoPagamentos(obj.getCondicaoPagamento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS);
				if (!Uteis.isAtributoPreenchido(listaParcelaCondicaoPagamento)) {
					throw new StreamSeiException("Não foi encontrada a condição de pagamento para compra de Número : " +obj.getCodigo());
				}
				preencherContaPagar(listaParcelaCondicaoPagamento, obj, 1, listaParcelaCondicaoPagamento.size(), usuario);	
				
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void preencherContaPagar(List<ParcelaCondicaoPagamentoVO> parcelaCondicaoPagamento, CompraVO obj, int parcela, int totalParcela, UsuarioVO usuarioVO) {
		Double valorTotal = 0.0;
		try {
			for (ParcelaCondicaoPagamentoVO pcp : parcelaCondicaoPagamento) {
				double valorParcela = 0;
				ContaPagarVO contaPagar = new ContaPagarVO();
				contaPagar.setParcela(parcela + "/" + totalParcela);
				if (pcp.getIntervalo().intValue() > 0) {
					contaPagar.setDataVencimento(Uteis.obterDataFuturaMesContabilConsiderandoDiaInicial(new Date(), pcp.getIntervalo()));
				} else {
					contaPagar.setDataVencimento(new Date());
				}
				valorParcela = Uteis.arrendondarForcando2CadasDecimais(obj.getPrecoTotal() * (pcp.getPercentualValor() / 100));
				valorTotal = valorTotal + valorParcela;
				if (parcela == totalParcela) {
					contaPagar.setValor(Uteis.arrendondarForcando2CadasDecimais(valorParcela + (obj.getPrecoTotal() - valorTotal)));
				} else {
					contaPagar.setValor(valorParcela);
				}
				contaPagar.setFornecedor(obj.getFornecedor());
				contaPagar.setTipoSacado(TipoSacado.FORNECEDOR.getValor());
				contaPagar.setSituacao("AP");
				contaPagar.setUnidadeEnsino(obj.getUnidadeEnsino());
				contaPagar.setCodOrigem(String.valueOf(obj.getCodigo()));
				contaPagar.setTipoOrigem(OrigemContaPagar.COMPRA.getValor());
				contaPagar.setNrDocumento(parcela + "." + obj.getCodigo());
				contaPagar.setDescricao("Pagamento gerado automático por Compra.");
				getFacadeFactory().getContaPagarFacade().atualizarCentroResultadoOrigemContaPagar(obj.getListaCentroResultadoOrigemVOs(), contaPagar, usuarioVO);
				getFacadeFactory().getContaPagarFacade().incluir(contaPagar, false, true, usuarioVO);
				parcela++;
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoEntrega(final Integer codigo, final String situacaoRecebimento) throws Exception {
		final String sql = "UPDATE Compra set situacaoRecebimento=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				int i = 0;
				Uteis.setValuePreparedStatement(situacaoRecebimento, ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(codigo, ++i, sqlAlterar);
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoFinanceira(final Integer codigo, final String situacaoFinanceira) throws Exception {
		final String sql = "UPDATE Compra set situacaoFinanceira=? WHERE ((codigo = ?))";
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement sqlAlterar = con.prepareStatement(sql);
				int i = 0;
				Uteis.setValuePreparedStatement(situacaoFinanceira, ++i, sqlAlterar);
				Uteis.setValuePreparedStatement(codigo, ++i, sqlAlterar);
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CompraVO obj, String motivoExclusao, Boolean controlarAcesso, UsuarioVO usuarioVO, ConfiguracaoGeralSistemaVO conf) {
		try {
			Compra.excluir(getIdEntidade(), controlarAcesso, usuarioVO);
			Uteis.checkState(!Uteis.isAtributoPreenchido(motivoExclusao), "O Motivo Exclusão deve ser informado.");
			String notaFiscal = getFacadeFactory().getNotaFiscalEntradaFacade().consultarNumeroDasNotasFiscaisPorCompra(obj.getCodigo(), usuarioVO);
			if (Uteis.isAtributoPreenchido(notaFiscal)) {
				throw new StreamSeiException("Não é possível excluir essa compra, pois a mesma já esta vinculadas as seguintes notas fiscais de entrada de número: " + notaFiscal);
			}
			if (getFacadeFactory().getRecebimentoCompraFacade().consultarSeExisteRecebimentoCompraPorCompraPorSituacao(obj.getCodigo(), "EF", usuarioVO)) {
				throw new StreamSeiException("Não é possível excluir essa compra, pois a mesma já tem recebimento de compra efetivado. ");
			}
			if (obj.getTipoCriacaoContaPagarEnum().isCompra() && getFacadeFactory().getContaPagarFacade().consultarSeExisteContaPagarPagarOuParcialmentePagaPorCodOrigemPorTipoOrigem(obj.getCodigo().toString(), OrigemContaPagar.COMPRA.getValor(), usuarioVO)) {
				throw new StreamSeiException("Existe uma conta pagar para essa compra e a mesma esta paga, para que seja feita a exclusão é necessário primeiramente realizar o estorno da conta pagar.");
			}
			getFacadeFactory().getRequisicaoItemFacade().anularVinculoRequisicaoItemComCompraItem(obj.getCompraItemVOs(), usuarioVO);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().excluidoRegistroNaoExistenteListaPorCodOrigemPorTipoCentroResultadoOrigemEnum(null, obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.COMPRA, usuarioVO);
			getFacadeFactory().getRecebimentoCompraFacade().excluirPorCodigoCompra(obj, false, usuarioVO);
			if(obj.getTipoCriacaoContaPagarEnum().isCompra()){
				getFacadeFactory().getContaPagarFacade().excluirContaPagarPorTipoOrigemPorCodigoOrigem(OrigemContaPagar.COMPRA.getValor(), obj.getCodigo().toString(), usuarioVO);
			}
			if(Uteis.isAtributoPreenchido(obj.getCotacao().getCodigo()) && !consultarSeExisteCompraPorCotacao(obj.getCotacao().getCodigo(), obj.getCodigo(), controlarAcesso, usuarioVO)){
				obj.getCotacao().setSituacao("IN");
				obj.getCotacao().setMotivoRevisao(motivoExclusao+ "\n\r Usuario - "+ usuarioVO.getNome() + "\n\r data - " + Uteis.getData(new Date()));
				CotacaoHistoricoVO cotHistorico = getFacadeFactory().getCotacaoHistoricoInterfaceFacade().consultarPorCotacao(obj.getCotacao(), false, usuarioVO);
				getFacadeFactory().getCotacaoFacade().indeferirCotacao(obj.getCotacao(), cotHistorico, usuarioVO, conf);
			}
			getConexao().getJdbcTemplate().update("DELETE FROM Compra WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	@Override
	public void preencherDadosPorCategoriaDespesa(CompraItemVO obj, UsuarioVO usuario) throws Exception {
		try {
			CentroResultadoVO crAdministrativo = null;
			CursoVO cursoFiltro = null;
			TurmaVO turmaFiltro = null;
			if (obj.getTipoNivelCentroResultadoEnum().isUnidadeEnsino() && Uteis.isAtributoPreenchido(obj.getCompra().getUnidadeEnsino())) {
				crAdministrativo = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorUnidadeEnsino(obj.getCompra().getUnidadeEnsino().getCodigo(), usuario);
			}
			if (obj.getTipoNivelCentroResultadoEnum().isDepartamento() && Uteis.isAtributoPreenchido(obj.getDepartamentoVO())) {
				crAdministrativo = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorDepartamento(obj.getDepartamentoVO().getCodigo(), usuario);
			}
			if ((obj.getTipoNivelCentroResultadoEnum().isCurso() || obj.getTipoNivelCentroResultadoEnum().isCursoTurno()) && Uteis.isAtributoPreenchido(obj.getCompra().getUnidadeEnsino()) && Uteis.isAtributoPreenchido(obj.getCursoVO())) {
				crAdministrativo = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorUnidadeEnsinoCurso(obj.getCompra().getUnidadeEnsino().getCodigo(), obj.getCursoVO().getCodigo(), usuario);
				cursoFiltro = obj.getCursoVO();
				turmaFiltro = null;
			}
			if (obj.getTipoNivelCentroResultadoEnum().isTurma() && Uteis.isAtributoPreenchido(obj.getTurma())) {
				crAdministrativo = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorTurma(obj.getTurma().getCodigo(), usuario);
				turmaFiltro = obj.getTurma();
				cursoFiltro = null;
			}
			if(Uteis.isAtributoPreenchido(crAdministrativo) && getFacadeFactory().getCentroResultadoFacade().validarRestricaoUsoCentroResultado(crAdministrativo, obj.getDepartamentoVO(), cursoFiltro, turmaFiltro, usuario)){
				obj.setCentroResultadoAdministrativo(crAdministrativo);	
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	public void adicionarObjCompraItemVOs(CompraVO obj, CompraItemVO ci, UsuarioVO usuario) {
		ci.setCompra(obj);
		CompraItemVO.validarDados(ci);
		int index = 0;
		for (CompraItemVO objExistente : obj.getCompraItemVOs()) {
			if (objExistente.getProduto().getCodigo().equals(ci.getProduto().getCodigo())) {
				obj.getCompraItemVOs().set(index, ci);
				gerarCentroResultadoOrigemPorCompraItem(obj, usuario);
				return;
			}
			index++;
		}
		obj.getCompraItemVOs().add(ci);
		obj.getCompraItemVOs().sort((c1, c2) -> c1.getProduto().getNome().compareTo(c2.getProduto().getNome()));
		gerarCentroResultadoOrigemPorCompraItem(obj, usuario);
	}

	private CompraItemVO consultarObjCompraItemVOs(CompraVO obj, ProdutoServicoVO produto) {
		for (CompraItemVO objExistente : obj.getCompraItemVOs()) {
			if (objExistente.getProduto().getCodigo().equals(produto.getCodigo())) {
				return objExistente;
			}
		}
		return new CompraItemVO();
	}

	@Override
	public void adicionarRequisicaoCompraDireta(CompraVO obj, List<RequisicaoVO> listaRequisicao, UsuarioVO usuario) {
		try {
			//obj.getListaRequisicaoVOs().clear();
			//obj.getCompraItemVOs().clear();
			for (RequisicaoVO requisicaoVO : listaRequisicao) {
				if (requisicaoVO.isSelecionado()) {
					//requisicaoVO = getFacadeFactory().getRequisicaoFacade().consultarRapidaPorCodigo(requisicaoVO.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);
					adicionarRequisicao(obj, requisicaoVO);
					for (RequisicaoItemVO ri : requisicaoVO.getRequisicaoItemVOs()) {
						if(Uteis.isAtributoPreenchido(ri.getQuantidadeAutorizada()) && (!Uteis.isAtributoPreenchido(ri.getQuantidadeEntregue()) || !ri.getQuantidadeAutorizada().equals(ri.getQuantidadeEntregue()))){
							gerarCompraItemPorRequisicao(obj, ri);	
						}
					}
				}
			}
			gerarCentroResultadoOrigemPorCompraItem(obj, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void adicionarRequisicao(CompraVO obj, RequisicaoVO requisicao) {
		int index = 0;
		for (RequisicaoVO objExistente : obj.getListaRequisicaoVOs()) {
			if (objExistente.getCodigo().equals(requisicao.getCodigo())) {
				obj.getListaRequisicaoVOs().set(index, requisicao);
				return;
			}
			index++;
		}
		obj.getListaRequisicaoVOs().add(requisicao);
	}

	private void gerarCompraItemPorRequisicao(CompraVO obj, RequisicaoItemVO ri) {
		int index = 0;
		CompraItemVO ci = consultarObjCompraItemVOs(obj, ri.getProdutoServico());
		ri.setCompraItemVO(ci);
		if (Uteis.isAtributoPreenchido(ci.getProduto())) {
			ci.setQuantidadeRequisicao(ci.getQuantidadeRequisicao() + ri.getQuantidadeAutorizada());
			ci.getListaRequisicaoItem().add(ri);
		} else {
			ci.setCompra(obj);
			ci.setProduto(ri.getProdutoServico());
			ci.setPrecoUnitario(ri.getValorUnitario());
			ci.setQuantidadeRequisicao(ri.getQuantidadeAutorizada());
			ci.getListaRequisicaoItem().add(ri);
			CompraItemVO.validarDados(ci);
		}
		for (CompraItemVO objExistente : obj.getCompraItemVOs()) {
			if (objExistente.getProduto().getCodigo().equals(ci.getProduto().getCodigo())) {
				obj.getCompraItemVOs().set(index, ci);
				return;
			}
			index++;
		}
		obj.getCompraItemVOs().add(ci);
	}

	
	@Override
	public void gerarCentroResultadoOrigemPorCompraItem(CompraVO obj, UsuarioVO usuario) {
		obj.getListaCentroResultadoOrigemVOs().clear();
		for (CompraItemVO ci : obj.getCompraItemVOs()) {
			for (RequisicaoItemVO ri : ci.getListaRequisicaoItem()) {
				CentroResultadoOrigemVO cro = new CentroResultadoOrigemVO();
				cro.setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR);
				cro.setCategoriaDespesaVO(ri.getRequisicaoVO().getCategoriaDespesa());
				cro.setUnidadeEnsinoVO(ri.getRequisicaoVO().getUnidadeEnsino());
				cro.setDepartamentoVO(ri.getRequisicaoVO().getDepartamento());
				cro.setFuncionarioCargoVO(ri.getRequisicaoVO().getFuncionarioCargoVO());
				cro.setCursoVO(ri.getRequisicaoVO().getCurso());
				cro.setTurnoVO(ri.getRequisicaoVO().getTurno());
				cro.setTurmaVO(ri.getRequisicaoVO().getTurma());
				cro.setTipoNivelCentroResultadoEnum(ri.getRequisicaoVO().getTipoNivelCentroResultadoEnum());
				cro.setCentroResultadoAdministrativo(ri.getRequisicaoVO().getCentroResultadoAdministrativo());
				cro.setQuantidade(ri.getQuantidadeAutorizada());
				cro.setValor(ci.getPrecoUnitario() * ri.getQuantidadeAutorizada());
				cro.calcularPorcentagem(obj.getPrecoTotal());
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigemAgrupado(cro, obj.getListaCentroResultadoOrigemVOs());
			}
			if (Uteis.isAtributoPreenchido(ci.getQuantidadeAdicional())) {
				CentroResultadoOrigemVO cro = new CentroResultadoOrigemVO();
				cro.setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR);
				cro.setCategoriaDespesaVO(ci.getCategoriaDespesa());
				cro.setUnidadeEnsinoVO(ci.getCompra().getUnidadeEnsino());
				cro.setDepartamentoVO(ci.getDepartamentoVO());
				cro.setCursoVO(ci.getCursoVO());
				cro.setTurnoVO(ci.getTurnoVO());
				cro.setTurmaVO(ci.getTurma());
				cro.setTipoNivelCentroResultadoEnum(ci.getTipoNivelCentroResultadoEnum());
				cro.setCentroResultadoAdministrativo(ci.getCentroResultadoAdministrativo());
				cro.setQuantidade(ci.getQuantidadeAdicional());
				cro.setValor(ci.getPrecoAdicionalTotal());
				cro.calcularPorcentagem(obj.getPrecoTotal());
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigemAgrupado(cro, obj.getListaCentroResultadoOrigemVOs());
			}
		}
		getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarValidacaoPorcentagem(obj.getListaCentroResultadoOrigemVOs(), obj.getPrecoTotal().doubleValue(), usuario);
	}	

	@Override
	public void removerRequisicaoCompraDireta(CompraVO obj, RequisicaoVO requisicaoVO, UsuarioVO usuario) {
		try {
			removerRequisicao(obj, requisicaoVO);
			removerRequisicaoDoCentroResultadoOrigem(obj, requisicaoVO);
			for (RequisicaoItemVO ri : requisicaoVO.getRequisicaoItemVOs()) {
				validarRemocaoCompraItem(obj, ri);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void removerRequisicao(CompraVO obj, RequisicaoVO requisicao) {
		Iterator<RequisicaoVO> i = obj.getListaRequisicaoVOs().iterator();
		while (i.hasNext()) {
			RequisicaoVO objExistente = i.next();
			if (objExistente.getCodigo().equals(requisicao.getCodigo())) {
				i.remove();
				return;
			}
		}
	}

	private void validarRemocaoCompraItem(CompraVO obj, RequisicaoItemVO ri) {
		Iterator<CompraItemVO> i = obj.getCompraItemVOs().iterator();
		while (i.hasNext()) {
			CompraItemVO objExistente = i.next();
			if (objExistente.getProduto().getCodigo().equals(ri.getProdutoServico().getCodigo()) 
					&& objExistente.getQuantidadeRequisicao().equals(ri.getQuantidadeAutorizada()) 
					&& !Uteis.isAtributoPreenchido(objExistente.getQuantidadeAdicional())) {
				i.remove();
				return;
			} else if (objExistente.getProduto().getCodigo().equals(ri.getProdutoServico().getCodigo())) {
				objExistente.setQuantidadeRequisicao(objExistente.getQuantidadeRequisicao() - ri.getQuantidadeAutorizada());
				removerRequisicaoItemDaCompraItem(objExistente, ri);
				return;
			}
		}
	}
	
	private void removerRequisicaoItemDaCompraItem(CompraItemVO obj, RequisicaoItemVO ri) {
		Iterator<RequisicaoItemVO> i = obj.getListaRequisicaoItem().iterator();
		while (i.hasNext()) {
			RequisicaoItemVO objExistente = i.next();
			if (objExistente.getCodigo().equals(ri.getCodigo())) {
				i.remove();
				return;
			}
		}
	}

	private void removerRequisicaoDoCentroResultadoOrigem(CompraVO obj, RequisicaoVO requisicao) {
		Iterator<CentroResultadoOrigemVO> i = obj.getListaCentroResultadoOrigemVOs().iterator();
		while (i.hasNext()) {
			CentroResultadoOrigemVO objExistente = i.next();
			if (objExistente.getCategoriaDespesaVO().getCodigo().equals(requisicao.getCategoriaDespesa().getCodigo())
					&& objExistente.getCentroResultadoAdministrativo().getCodigo().equals(requisicao.getCentroResultadoAdministrativo().getCodigo()) 
					&& objExistente.getQuantidade().equals(requisicao.getQuantidadeTotalAutorizada())) {
				i.remove();
				return;
			} else if (objExistente.getCategoriaDespesaVO().getCodigo().equals(requisicao.getCategoriaDespesa().getCodigo())
					&& objExistente.getCentroResultadoAdministrativo().getCodigo().equals(requisicao.getCentroResultadoAdministrativo().getCodigo())) {
				objExistente.setQuantidade(objExistente.getQuantidade() - requisicao.getQuantidadeTotalAutorizada());
				objExistente.setValor(objExistente.getValor() - requisicao.getValorTotalRequisicao());
				return;
			}
		}
	}

	public List consultarPorDataCompra(Date prmIni, Date prmFim, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Compra WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + " 00:00') and (data <= '" + Uteis.getDataJDBC(prmFim) + " 23:59')) ";
		sqlStr = montarFiltrosConsultaPorDataCompra(situacaoFinanceira, pendente, parcial, entregue, unidadeEnsino, richmodal, sqlStr);
		sqlStr += " ORDER BY data";
		StringBuilder sb = new StringBuilder(sqlStr);
		UteisTexto.addLimitAndOffset(sb, limite, offset);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public Integer consultarTotalPorDataCompra(Date prmIni, Date prmFim, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT count(Compra.codigo) as qtde FROM Compra WHERE ((data >= '" + Uteis.getDataJDBC(prmIni) + " 00:00') and (data <= '" + Uteis.getDataJDBC(prmFim) + " 23:59')) ";
		sqlStr = montarFiltrosConsultaPorDataCompra(situacaoFinanceira, pendente, parcial, entregue, unidadeEnsino, richmodal, sqlStr);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private String montarFiltrosConsultaPorDataCompra(String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, String sqlStr) {
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += "  and unidadeEnsino = " + unidadeEnsino.intValue();
		}
		if (!richmodal) {
			if (Uteis.isAtributoPreenchido(situacaoFinanceira)) {
				sqlStr += " and situacaoFinanceira = '" + situacaoFinanceira.toUpperCase() + "' ";
			}
			String andOr = " and";
			if (Uteis.isAtributoPreenchido(pendente) && "PE".equals(pendente)) {
				sqlStr += andOr + " situacaoRecebimento = '" + pendente.toUpperCase() + "' ";
				andOr = " or";
			}
			if (Uteis.isAtributoPreenchido(parcial) && "PA".equals(parcial)) {
				sqlStr += andOr + " situacaoRecebimento = '" + parcial.toUpperCase() + "' ";
				andOr = " or";
			}
			if (Uteis.isAtributoPreenchido(entregue) && "FI".equals(entregue)) {
				sqlStr += andOr + " situacaoRecebimento = '" + entregue.toUpperCase() + "' ";
			}
		}
		return sqlStr;
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<CompraVO> consultarPorCodigoCotacao(Integer valorConsulta, String situacaoFinanceira, String situacaoRecebimento, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringJoiner sql = new StringJoiner(" ");
		sql.add("select * from compra where 1=1");
		sql.add(String.format("and cotacao = %d", valorConsulta));
		sql.add(String.format("and situacaofinanceira like '%%%s%%'", situacaoFinanceira));
		sql.add(String.format("and situacaorecebimento like '%%%s%%'", situacaoRecebimento));
		StringBuilder sb = new StringBuilder(sql.toString());
		UteisTexto.addLimitAndOffset(sb, limite, offset);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public Integer consultarTotalPorCodigoCotacao(Integer valorConsulta, String situacaoFinanceira, String situacaoRecebimento, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringJoiner sql = new StringJoiner(" ");
		sql.add("select count(Compra.codigo) as qtde from compra where 1=1");
		sql.add(String.format("and cotacao = %d", valorConsulta));
		sql.add(String.format("and situacaofinanceira like '%%%s%%'", situacaoFinanceira));
		sql.add(String.format("and situacaorecebimento like '%%%s%%'", situacaoRecebimento));
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	@SuppressWarnings("static-access")
	public List consultarPorCodigoCotacao(Integer valorConsulta, Date dataIni, Date dataFim, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Compra.* FROM Compra, Cotacao WHERE Compra.cotacao = Cotacao.codigo and Cotacao.codigo = " + valorConsulta.intValue() + " ";
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += "  and unidadeEnsino = " + unidadeEnsino.intValue();
		}
		if (!richmodal) {
			if (Uteis.isAtributoPreenchido(situacaoFinanceira)) {
				sqlStr += " and situacaoFinanceira = '" + situacaoFinanceira.toUpperCase() + "' ";
			}
			String andOr = " and";
			if (Uteis.isAtributoPreenchido(pendente) && "PE".equals(pendente)) {
				sqlStr += andOr + " Compra.situacaoRecebimento = '" + pendente.toUpperCase() + "' ";
				andOr = " or";
			}
			if (Uteis.isAtributoPreenchido(parcial) && "PA".equals(parcial)) {
				sqlStr += andOr + " Compra.situacaoRecebimento = '" + parcial.toUpperCase() + "' ";
				andOr = " or";
			}
			if (Uteis.isAtributoPreenchido(entregue) && "FI".equals(entregue)) {
				sqlStr += andOr + " Compra.situacaoRecebimento = '" + entregue.toUpperCase() + "' ";
			}
			sqlStr += " AND Compra.data between '" + Uteis.getDataJDBC(dataIni) + " 00:00' and '" + Uteis.getDataJDBC(dataFim) + " 23:59'";
		}
		sqlStr += " ORDER BY Cotacao.codigo";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}

	public List consultarPorNomeFornecedor(String valorConsulta, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Compra.* FROM Compra, Fornecedor WHERE Compra.fornecedor = Fornecedor.codigo and sem_acentos( Fornecedor.nome ) ilike sem_acentos(?) ";
		sqlStr = montarFiltrosConsultaPorNomeFornecedor(situacaoFinanceira, pendente, parcial, entregue, unidadeEnsino, richmodal, sqlStr);
		sqlStr += " ORDER BY Fornecedor.nome";
		StringBuilder sb = new StringBuilder(sqlStr);
		UteisTexto.addLimitAndOffset(sb, limite, offset);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta + PERCENT);
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public Integer consultarTotalPorNomeFornecedor(String valorConsulta, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT count(Compra.codigo) as qtde FROM Compra, Fornecedor WHERE Compra.fornecedor = Fornecedor.codigo and sem_acentos( Fornecedor.nome ) ilike sem_acentos('" + valorConsulta.toUpperCase() + "%') ";
		sqlStr = montarFiltrosConsultaPorNomeFornecedor(situacaoFinanceira, pendente, parcial, entregue, unidadeEnsino, richmodal, sqlStr);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private String montarFiltrosConsultaPorNomeFornecedor(String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, String sqlStr) {
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += "  and unidadeEnsino = " + unidadeEnsino.intValue();
		}
		if (!richmodal) {
			if (Uteis.isAtributoPreenchido(situacaoFinanceira)) {
				sqlStr += " and situacaoFinanceira = '" + situacaoFinanceira.toUpperCase() + "' ";
			}
			String andOr = " and";
			if (Uteis.isAtributoPreenchido(pendente) && "PE".equals(pendente)) {
				sqlStr += andOr + " Compra.situacaoRecebimento = '" + pendente.toUpperCase() + "' ";
				andOr = " or";
			}
			if (Uteis.isAtributoPreenchido(parcial) && "PA".equals(parcial)) {
				sqlStr += andOr + " Compra.situacaoRecebimento = '" + parcial.toUpperCase() + "' ";
				andOr = " or";
			}
			if (Uteis.isAtributoPreenchido(entregue) && "FI".equals(entregue)) {
				sqlStr += andOr + " Compra.situacaoRecebimento = '" + entregue.toUpperCase() + "' ";
			}
		}
		return sqlStr;
	}

	@SuppressWarnings("static-access")
	public List consultarPorCodigo(Integer valorConsulta, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT * FROM Compra WHERE codigo = " + valorConsulta.intValue() + " ";
		sqlStr = montarFiltrosConsultaPorCodigo(situacaoFinanceira, pendente, parcial, entregue, unidadeEnsino, richmodal, sqlStr);
		sqlStr += " ORDER BY codigo";
		StringBuilder sb = new StringBuilder(sqlStr);
		UteisTexto.addLimitAndOffset(sb, limite, offset);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	public Integer consultarTotalPorCodigo(Integer valorConsulta, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT count(Compra.codigo) as qtde FROM Compra WHERE codigo = " + valorConsulta.intValue() + " ";
		sqlStr = montarFiltrosConsultaPorCodigo(situacaoFinanceira, pendente, parcial, entregue, unidadeEnsino, richmodal, sqlStr);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	private String montarFiltrosConsultaPorCodigo(String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, String sqlStr) {
		if (unidadeEnsino.intValue() != 0) {
			sqlStr += "  and unidadeEnsino = " + unidadeEnsino.intValue();
		}
		if (!richmodal) {
			if (Uteis.isAtributoPreenchido(situacaoFinanceira)) {
				sqlStr += " and situacaoFinanceira = '" + situacaoFinanceira.toUpperCase() + "' ";
			}
			String andOr = " and";
			if (Uteis.isAtributoPreenchido(pendente) && "PE".equals(pendente)) {
				sqlStr += andOr + " situacaoRecebimento = '" + pendente.toUpperCase() + "' ";
				andOr = " or";
			}
			if (Uteis.isAtributoPreenchido(parcial) && "PA".equals(parcial)) {
				sqlStr += andOr + " Compra.situacaoRecebimento = '" + parcial.toUpperCase() + "' ";
				andOr = " or";
			}
			if (Uteis.isAtributoPreenchido(entregue) && "FI".equals(entregue)) {
				sqlStr += andOr + " Compra.situacaoRecebimento = '" + entregue.toUpperCase() + "' ";
			}
		}
		return sqlStr;
	}

	private  List<CompraVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<CompraVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}
	
	private CompraVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CompraVO obj = new CompraVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setTipoCriacaoContaPagarEnum(TipoCriacaoContaPagarEnum.valueOf(dadosSQL.getString("tipoCriacaoContaPagar")));
		obj.setSituacaoFinanceira(dadosSQL.getString("situacaoFinanceira"));
		obj.setSituacaoRecebimento(dadosSQL.getString("situacaoRecebimento"));
		obj.getFornecedor().setCodigo((dadosSQL.getInt("fornecedor")));
		obj.getCotacao().setCodigo((dadosSQL.getInt("cotacao")));
		obj.setData(dadosSQL.getTimestamp("data"));
		obj.getResponsavel().setCodigo((dadosSQL.getInt("responsavel")));
		obj.getFormaPagamento().setCodigo((dadosSQL.getInt("formaPagamento")));
		obj.getCondicaoPagamento().setCodigo((dadosSQL.getInt("condicaoPagamento")));
		obj.getUnidadeEnsino().setCodigo((dadosSQL.getInt("unidadeEnsino")));
		obj.getContaCorrente().setCodigo((dadosSQL.getInt("contaCorrente")));
		obj.getCategoriaProduto().setCodigo((dadosSQL.getInt("categoriaProduto")));
		obj.setDataPrevisaoEntrega(dadosSQL.getDate("dataPrevisaoEntrega"));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			return obj;
		}
		obj.setCompraItemVOs(getFacadeFactory().getCompraItemFacade().consultarCompraItems(obj, nivelMontarDados, usuario));
		obj.setListaCentroResultadoOrigemVOs(getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.COMPRA, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		montarDadosCotacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosFornecedor(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosCondicaoPagamento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosFormaPagamento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		montarDadosContaCorrente(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		obj.setCategoriaProduto(Uteis.montarDadosVO(dadosSQL.getInt("categoriaProduto"), CategoriaProdutoVO.class, p -> getFacadeFactory().getCategoriaProdutoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario)));
		montarDadosRequisicao(usuario, obj);
		return obj;
	}

	private void montarDadosRequisicao(UsuarioVO usuario, CompraVO obj) throws Exception {
		Map<Integer,List<RequisicaoItemVO>> mapa = obj.getCompraItemVOs().stream().flatMap(p->p.getListaRequisicaoItem().stream()).collect(Collectors.groupingBy(p -> p.getRequisicaoVO().getCodigo()));
		for (Map.Entry<Integer, List<RequisicaoItemVO>> mapaRequisicao : mapa.entrySet()) {
			RequisicaoVO requisicaoVO = getFacadeFactory().getRequisicaoFacade().consultarRapidaPorCodigo(mapaRequisicao.getKey(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, usuario);
			requisicaoVO.setRequisicaoItemVOs(mapaRequisicao.getValue());
			obj.getListaRequisicaoVOs().add(requisicaoVO);
		}
	}

	public static void montarDadosResponsavel(CompraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavel().getCodigo().intValue() == 0) {
			obj.setResponsavel(new UsuarioVO());
			return;
		}
		obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosCotacao(CompraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCotacao().getCodigo().intValue() == 0) {
			obj.setCotacao(new CotacaoVO());
			return;
		}
		obj.setCotacao(getFacadeFactory().getCotacaoFacade().consultarPorChavePrimaria(obj.getCotacao().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosFornecedor(CompraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFornecedor().getCodigo().intValue() == 0) {
			obj.setFornecedor(new FornecedorVO());
			return;
		}
		obj.setFornecedor(getFacadeFactory().getFornecedorFacade().consultarPorChavePrimaria(obj.getFornecedor().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosFormaPagamento(CompraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getFormaPagamento().getCodigo().intValue() == 0) {
			obj.setFormaPagamento(new FormaPagamentoVO());
			return;
		}
		obj.setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(obj.getFormaPagamento().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosCondicaoPagamento(CompraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCondicaoPagamento().getCodigo().intValue() == 0) {
			obj.setCondicaoPagamento(new CondicaoPagamentoVO());
			return;
		}
		obj.setCondicaoPagamento(getFacadeFactory().getCondicaoPagamentoFacade().consultarPorChavePrimaria(obj.getCondicaoPagamento().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosUnidadeEnsino(CompraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			obj.setUnidadeEnsino(new UnidadeEnsinoVO());
			return;
		}
		obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosContaCorrente(CompraVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getContaCorrente().getCodigo().intValue() == 0) {
			obj.setContaCorrente(new ContaCorrenteVO());
			return;
		}
		obj.setContaCorrente(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(obj.getContaCorrente().getCodigo(), false, nivelMontarDados, usuario));
	}

	public CompraVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM Compra WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm );
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Compra ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public boolean consultarSeExisteCompraPorCotacao(Integer cotacao, Integer compra, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder("SELECT count(codigo) as qtd FROM Compra WHERE cotacao = ? ");
		if(Uteis.isAtributoPreenchido(compra)){
			sql.append(" and codigo != ").append(compra);
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), cotacao );
		return Uteis.isAtributoPreenchido(tabelaResultado, "qtd", TipoCampoEnum.INTEIRO);
		
	}

	@Override
	public CompraVO consultarPorCompraPorCodOrigemContaPagar(String codOrigem, OrigemContaPagar origemContaPagar, UsuarioVO usuario) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct compra.codigo, compra.situacaorecebimento  ");
		sql.append(" from compra ");
		sql.append(" inner join recebimentocompra on recebimentocompra.compra = compra.codigo ");
		sql.append(" left join notafiscalentradarecebimentocompra on notafiscalentradarecebimentocompra.recebimentocompra = recebimentocompra.codigo ");
		sql.append(" left join notafiscalentrada on notafiscalentradarecebimentocompra.notafiscalentrada = notafiscalentrada.codigo ");
		sql.append(" inner join contapagar on  ");
		sql.append(" codorigem::text =  (case when compra.tipocriacaocontapagar = '").append(TipoCriacaoContaPagarEnum.RECEBIMENTO_COMPRA.name()).append("' then recebimentocompra.codigo::text when compra.tipocriacaocontapagar = '").append(TipoCriacaoContaPagarEnum.COMPRA.name()).append("' then compra.codigo::text else notafiscalentrada.codigo::text end) ");
		sql.append(" and tipoorigem  =  (case when compra.tipocriacaocontapagar = '").append(TipoCriacaoContaPagarEnum.RECEBIMENTO_COMPRA.name()).append("' then 'RC' when compra.tipocriacaocontapagar = '").append(TipoCriacaoContaPagarEnum.COMPRA.name()).append("' then 'CO' else 'NE' end) ");
		sql.append(" where ");
		if (origemContaPagar.isRecebimentoCompra()) {
			sql.append("  recebimentocompra.codigo = ").append(codOrigem);
		} else if (origemContaPagar.isCompra()) {
			sql.append("   compra.codigo = ").append(codOrigem);
		} else if (origemContaPagar.isNotaFiscalEntrada()) {
			sql.append("   notafiscalentrada.codigo = ").append(codOrigem);
		}
		SqlRowSet dadosSQL = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		if (dadosSQL.next()) {
			CompraVO obj = new CompraVO();
			obj.setNovoObj(Boolean.FALSE);
			obj.setCodigo(dadosSQL.getInt("codigo"));
			obj.setSituacaoRecebimento(dadosSQL.getString("situacaoRecebimento"));
			return obj;
		}
		return new CompraVO();
	}

	public static String getIdEntidade() {
		return Compra.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		Compra.idEntidade = idEntidade;
	}

	@SuppressWarnings("static-access")
	private List<ItemSumarioUnidadeEstatisticaVO> obterItemSumarioUnidadeEstatisticaCompraVO(int nrUnidadeApresentar, UsuarioVO usuario) {
		List<ItemSumarioUnidadeEstatisticaVO> resultado = new ArrayList<ItemSumarioUnidadeEstatisticaVO>(0);
		try {
			ControleAcesso.consultar(getIdEntidade(), false, usuario);
			StringBuffer sql = new StringBuffer();
			sql.append("select count(compra.codigo) as quantidade, compra.unidadeEnsino, unidadeEnsino.nome ");
			sql.append("FROM Compra INNER JOIN UnidadeEnsino ON (UnidadeEnsino.codigo = Compra.unidadeEnsino) ");
			sql.append("where situacaoRecebimento = 'PE' ");
			sql.append("group by compra.unidadeEnsino, unidadeEnsino.nome ");
			sql.append("order by quantidade DESC;");

			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			while (tabelaResultado.next()) {
				ItemSumarioUnidadeEstatisticaVO sumario = new ItemSumarioUnidadeEstatisticaVO();
				sumario.setCodigo(tabelaResultado.getInt("unidadeEnsino"));
				sumario.setNome(tabelaResultado.getString("nome"));
				sumario.setQuantidade(tabelaResultado.getInt("quantidade"));
				resultado.add(sumario);
			}

			if (resultado.size() > nrUnidadeApresentar) {
				int posSomarRemover = resultado.size() - 1;
				ItemSumarioUnidadeEstatisticaVO sumarioSoma = new ItemSumarioUnidadeEstatisticaVO();
				sumarioSoma.setCodigo(0);
				sumarioSoma.setNome("Outras");
				sumarioSoma.setQuantidade(0);
				while (posSomarRemover >= nrUnidadeApresentar) {
					ItemSumarioUnidadeEstatisticaVO itemRemover = resultado.remove(posSomarRemover);
					sumarioSoma.setQuantidade(sumarioSoma.getQuantidade() + itemRemover.getQuantidade());
					posSomarRemover--;
				}
				resultado.add(sumarioSoma);
			}
		} catch (Exception e) {
			//// System.out.println("Comprar Erro:" + e.getMessage());
		}
		return resultado;
	}

	public EstatisticaCompraVO consultarEstatisticaRecebimentoCompraAtualizada(UsuarioVO usuario) throws Exception {
		EstatisticaCompraVO estatisticas = new EstatisticaCompraVO();

		List<ItemSumarioUnidadeEstatisticaVO> sumario = obterItemSumarioUnidadeEstatisticaCompraVO(3, usuario);
		estatisticas.setSumarioPorUnidade(sumario);

		return estatisticas;
	}
	
	public List consultarPorCnpjFornecedor(String valorConsulta, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, Integer limite, Integer offset, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT Compra.* FROM Compra, Fornecedor WHERE Compra.fornecedor = Fornecedor.codigo and fornecedor.CNPJ like('" + valorConsulta + "%') ";
		sqlStr = montarFiltrosConsultaPorNomeFornecedor(situacaoFinanceira, pendente, parcial, entregue, unidadeEnsino, richmodal, sqlStr);
		sqlStr += " ORDER BY Fornecedor.nome";
		StringBuilder sb = new StringBuilder(sqlStr);
		UteisTexto.addLimitAndOffset(sb, limite, offset);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public Integer consultarTotalPorCnpjFornecedor(String valorConsulta, String situacaoFinanceira, String pendente, String parcial, String entregue, Integer unidadeEnsino, boolean richmodal, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT count(Compra.codigo) as qtde FROM Compra, Fornecedor WHERE Compra.fornecedor = Fornecedor.codigo and fornecedor.CNPJ like('" + valorConsulta + "%')  ";
		sqlStr = montarFiltrosConsultaPorNomeFornecedor(situacaoFinanceira, pendente, parcial, entregue, unidadeEnsino, richmodal, sqlStr);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}
}