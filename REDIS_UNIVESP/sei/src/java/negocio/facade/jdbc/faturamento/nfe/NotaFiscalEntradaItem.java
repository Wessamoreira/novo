package negocio.facade.jdbc.faturamento.nfe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.richfaces.event.FileUploadEvent;
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
import negocio.comuns.compras.CompraItemVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.compras.enumeradores.TipoProdutoServicoEnum;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemRecebimentoVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemVO;
import negocio.comuns.faturamento.nfe.NotaFiscalEntradaVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProcessarParalelismo;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.faturamento.nfe.NotaFiscalEntradaItemInterfaceFacade;

/**
 *
 * @author Pedro Otimize
 */
@Repository
@Scope("singleton")
@Lazy
public class NotaFiscalEntradaItem extends ControleAcesso implements NotaFiscalEntradaItemInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1426675007514704892L;
	protected static String idEntidade = "NotaFiscalEntradaItem";

	public NotaFiscalEntradaItem() {
		super();
	}

	public void validarDados(NotaFiscalEntradaItemVO obj) {
		if (!Uteis.isAtributoPreenchido(obj.getProdutoServicoVO())) {
			throw new StreamSeiException("O campo Produto/Serviço (NotaFiscalEntradaItem) não foi informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getQuantidade())) {
			throw new StreamSeiException("O campo Quantidade (NotaFiscalEntradaItem) não foi informado.");
		}
		if (!Uteis.isAtributoPreenchido(obj.getValorUnitario())) {
			throw new StreamSeiException("O campo Valor Unitário (NotaFiscalEntradaItem) não foi informado.");
		}
		Uteis.checkState(!obj.getPorcentagemCentroResultadoTotal().equals(100.00), "A distribuição da porcentagem entre os Centros de Resultados para o Produto/Serviço: " + 
			obj.getProdutoServicoVO().getNome() + " deve ser igual a 100%.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaItemInterfaceFacade#
	 * persistir(java.util.List, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<NotaFiscalEntradaItemVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			for (NotaFiscalEntradaItemVO obj : lista) {
				validarDados(obj);
				if (obj.getCodigo() == 0) {
					incluir(obj, verificarAcesso, usuarioVO);
				} else {
					alterar(obj, verificarAcesso, usuarioVO);
				}
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().persistir(obj.getListaCentroResultadoOrigemVOs(), obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.NOTA_FISCAL_ENTRADA_ITEM, false, usuarioVO, false);
				validarSeRegistroForamExcluidoDasListaSubordinadas(obj.getListaNotaFiscalEntradaItemRecebimentoVOs(), "NotaFiscalEntradaItemRecebimento", "notaFiscalEntradaItem", obj.getCodigo(), usuarioVO);
				getFacadeFactory().getNotaFiscalEntradaItemRecebimentoFacade().persistir(obj.getListaNotaFiscalEntradaItemRecebimentoVOs(), false, usuarioVO);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final NotaFiscalEntradaItemVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			NotaFiscalEntradaItem.incluir(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append(
					"INSERT INTO NotaFiscalEntradaItem (produtoServico, recebimentoCompraItem, notaFiscalEntrada, quantidade, valorUnitario) ");
			sql.append("    VALUES (?,?,?,?,?)");
			sql.append(" returning codigo" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getProdutoServicoVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getRecebimentoCompraItemVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getNotaFiscalEntradaVO(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getQuantidade(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getValorUnitario(), ++i, sqlInserir);
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
	public void alterar(final NotaFiscalEntradaItemVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			NotaFiscalEntradaItem.alterar(getIdEntidade(), verificarAcesso, usuario);
			final StringBuilder sql = new StringBuilder();
			sql.append("UPDATE NotaFiscalEntradaItem ");
			sql.append(
					"   SET produtoServico= ?, recebimentoCompraItem=?, notaFiscalEntrada= ?, quantidade= ?, valorUnitario= ? ");
			sql.append("   WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
			if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getProdutoServicoVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getRecebimentoCompraItemVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getNotaFiscalEntradaVO(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getQuantidade(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getValorUnitario(), ++i, sqlAlterar);
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
	public void excluidoRegistroNaoExistenteLista(NotaFiscalEntradaVO nfe, UsuarioVO usuario) {
		try {
			StringBuilder sb = null;
			if (Uteis.isAtributoPreenchido(nfe.getListaNotaFiscalEntradaItem())) {
				sb = new StringBuilder("select codigo FROM notaFiscalEntradaItem where NotaFiscalEntrada = ")
						.append(nfe.getCodigo()).append(" ");
				sb.append(" and codigo not in ( 0  ");
				for (Object obj : nfe.getListaNotaFiscalEntradaItem()) {
					Integer codigo = (Integer) UtilReflexao.invocarMetodoGet(obj, "codigo");
					if (Uteis.isAtributoPreenchido(codigo)) {
						sb.append(", ").append(codigo);
					}
				}
				sb.append(") ");
			} else {
				sb = new StringBuilder("select codigo FROM notaFiscalEntradaItem where NotaFiscalEntrada = ")
						.append(nfe.getCodigo()).append(" ");
			}
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
			while (tabelaResultado.next()) {
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade()
						.excluidoRegistroNaoExistenteListaPorCodOrigemPorTipoCentroResultadoOrigemEnum(null,
								tabelaResultado.getString("codigo"),
								TipoCentroResultadoOrigemEnum.NOTA_FISCAL_ENTRADA_ITEM, usuario);
			}
			validarSeRegistroForamExcluidoDasListaSubordinadas(nfe.getListaNotaFiscalEntradaItem(),
					"notaFiscalEntradaItem", "NotaFiscalEntrada", nfe.getCodigo(), usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaItemInterfaceFacade#
	 * excluir(negocio.comuns.faturamento.nfe.NotaFiscalEntradaItemVO, boolean,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(NotaFiscalEntradaItemVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			NotaFiscalEntradaItem.excluir(getIdEntidade(), verificarAcesso, usuario);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade()
					.excluidoRegistroNaoExistenteListaPorCodOrigemPorTipoCentroResultadoOrigemEnum(null,
							obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.NOTA_FISCAL_ENTRADA_ITEM,
							usuario);
			String sql = "DELETE FROM NotaFiscalEntradaItem WHERE ((codigo = ?))"
					+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void gerarNotaFiscalEntradaItemRecebimento(NotaFiscalEntradaItemVO nfei, UsuarioVO usuario) {
		try {
			Double qtdRecebidaNotaFiscalEntrada = nfei.getRecebimentoCompraItemVO().getQuantidadeRecebida();
			if (Uteis.isAtributoPreenchido(
					nfei.getRecebimentoCompraItemVO().getCompraItem().getQuantidadeRequisicao())) {
				qtdRecebidaNotaFiscalEntrada = preencherNotaFiscalEntradaCentroResultadoPorRequisicaoItem(nfei,
						qtdRecebidaNotaFiscalEntrada, usuario);
			}
			if (Uteis
					.isAtributoPreenchido(nfei.getRecebimentoCompraItemVO().getCompraItem().getQuantidadeAdicional())) {
				preencherNotaFiscalEntradaCentroResultadoPorCompraItem(nfei, usuario, qtdRecebidaNotaFiscalEntrada);
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void preencherNotaFiscalEntradaCentroResultadoPorCompraItem(NotaFiscalEntradaItemVO nfei, UsuarioVO usuario,
			Double qtdRecebidaNotaFiscalEntrada) throws Exception {
		if (qtdRecebidaNotaFiscalEntrada > 0.0) {
			NotaFiscalEntradaItemRecebimentoVO nfer = consultarNotaFiscalEntradaItemRecebimentoVO(nfei,
					nfei.getRecebimentoCompraItemVO().getCompraItem());
			if (!Uteis.isAtributoPreenchido(nfer.getCompraItemVO())) {
				nfer.setCompraItemVO(nfei.getRecebimentoCompraItemVO().getCompraItem());
			}
			nfer.setValorUnitario(nfei.getRecebimentoCompraItemVO().getCompraItem().getPrecoUnitario());
			preencherQuantidadeNotaFiscalEntradaCentroResultado(nfei, nfer, qtdRecebidaNotaFiscalEntrada,
					nfer.getCompraItemVO().getQuantidadeAdicional(), usuario);
		} else {
			removerNotaFiscalEntradaItemRecebimentoVO(nfei, false,
					nfei.getRecebimentoCompraItemVO().getCompraItem().getCodigo());
		}
	}

	private Double preencherNotaFiscalEntradaCentroResultadoPorRequisicaoItem(NotaFiscalEntradaItemVO nfei,
			Double qtdRecebidaNotaFiscalEntrada, UsuarioVO usuario) throws Exception {
		for (RequisicaoItemVO ri : nfei.getRecebimentoCompraItemVO().getCompraItem().getListaRequisicaoItem()) {
			if (qtdRecebidaNotaFiscalEntrada > 0.0) {
				NotaFiscalEntradaItemRecebimentoVO nfer = consultarNotaFiscalEntradaItemRecebimentoVO(nfei, ri);
				if (!Uteis.isAtributoPreenchido(nfer.getRequisicaoItemVO())) {
					nfer.setRequisicaoItemVO(ri);
				}
				nfer.setValorUnitario(nfei.getRecebimentoCompraItemVO().getCompraItem().getPrecoUnitario());
				qtdRecebidaNotaFiscalEntrada = preencherQuantidadeNotaFiscalEntradaCentroResultado(nfei, nfer,
						qtdRecebidaNotaFiscalEntrada, nfer.getRequisicaoItemVO().getQuantidadeAutorizada(), usuario);
			} else {
				removerNotaFiscalEntradaItemRecebimentoVO(nfei, true, ri.getCodigo());
			}
		}
		return qtdRecebidaNotaFiscalEntrada;
	}

	private Double preencherQuantidadeNotaFiscalEntradaCentroResultado(NotaFiscalEntradaItemVO nfei,
			NotaFiscalEntradaItemRecebimentoVO nfer, Double qtdRecebidaNotaFiscalEntrada, Double qtdAutorizado,
			UsuarioVO usuario) throws Exception {
		Double qtdRestante = getFacadeFactory().getNotaFiscalEntradaItemRecebimentoFacade()
				.consultarQuantidadeNotaFiscalEntradaTotal(nfer, usuario);
		qtdRestante = qtdAutorizado - qtdRestante;
		if (qtdRestante > 0.0) {
			if (qtdRecebidaNotaFiscalEntrada >= qtdRestante) {
				nfer.setQuantidadeNotaFiscalEntrada(qtdRestante);
				qtdRecebidaNotaFiscalEntrada = qtdRecebidaNotaFiscalEntrada - qtdRestante;
			} else if (qtdRecebidaNotaFiscalEntrada > 0.0) {
				nfer.setQuantidadeNotaFiscalEntrada(qtdRecebidaNotaFiscalEntrada);
				qtdRecebidaNotaFiscalEntrada = 0.0;
			}
			CentroResultadoOrigemVO cro = new CentroResultadoOrigemVO();
			cro.setCategoriaDespesaVO(nfer.getCategoriaDespesa());
			cro.setCentroResultadoAdministrativo(nfer.getCentroResultadoAdministrativo());
			cro.setTipoNivelCentroResultadoEnum(nfer.getTipoNivelCentroResultadoEnum());
			cro.setFuncionarioCargoVO(nfer.getFuncionarioCargoVO());
			cro.setUnidadeEnsinoVO(nfer.getUnidadeEnsinoVO());
			cro.setDepartamentoVO(nfer.getDepartamentoVO());
			cro.setCursoVO(nfer.getCursoVO());
			cro.setTurnoVO(nfer.getTurnoVO());
			cro.setTurmaVO(nfer.getTurmaVO());
			cro.setQuantidade(nfer.getQuantidadeNotaFiscalEntrada());
			cro.setValor(nfer.getValorTotal());
			cro.setPorcentagem(Uteis.arrendondarForcando4CadasDecimais((cro.getValor() * 100) / nfei.getValorTotal()));
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().atualizarCentroResultadoOrigemAgrupado(cro,nfei.getListaCentroResultadoOrigemVOs(),true);
			adicionarNotaFiscalEntradaItemRecebimentoVO(nfei, nfer);
		}
		return qtdRecebidaNotaFiscalEntrada;
	}

	private NotaFiscalEntradaItemRecebimentoVO consultarNotaFiscalEntradaItemRecebimentoVO(NotaFiscalEntradaItemVO obj,
			RequisicaoItemVO ri) {
		for (NotaFiscalEntradaItemRecebimentoVO objExistente : obj.getListaNotaFiscalEntradaItemRecebimentoVOs()) {
			if (objExistente.getRequisicaoItemVO().getCodigo().equals(ri.getCodigo())) {
				return objExistente;
			}
		}
		return new NotaFiscalEntradaItemRecebimentoVO();
	}

	private NotaFiscalEntradaItemRecebimentoVO consultarNotaFiscalEntradaItemRecebimentoVO(NotaFiscalEntradaItemVO obj,
			CompraItemVO ci) {
		for (NotaFiscalEntradaItemRecebimentoVO objExistente : obj.getListaNotaFiscalEntradaItemRecebimentoVOs()) {
			if (objExistente.getCompraItemVO().getCodigo().equals(ci.getCodigo())) {
				return objExistente;
			}
		}
		return new NotaFiscalEntradaItemRecebimentoVO();
	}

	private void adicionarNotaFiscalEntradaItemRecebimentoVO(NotaFiscalEntradaItemVO obj,
			NotaFiscalEntradaItemRecebimentoVO nfer) {
		int index = 0;
		nfer.setNotaFiscalEntradaItemVO(obj);
		for (NotaFiscalEntradaItemRecebimentoVO objExistente : obj.getListaNotaFiscalEntradaItemRecebimentoVOs()) {
			if (objExistente.equalsCampoSelecaoLista(nfer)) {
				obj.getListaNotaFiscalEntradaItemRecebimentoVOs().set(index, objExistente);
				return;
			}
			index++;
		}
		obj.getListaNotaFiscalEntradaItemRecebimentoVOs().add(nfer);
	}

	private void removerNotaFiscalEntradaItemRecebimentoVO(NotaFiscalEntradaItemVO obj, boolean isRequisicao,
			Integer codigoCampoSelecao) {
		Iterator<NotaFiscalEntradaItemRecebimentoVO> i = obj.getListaNotaFiscalEntradaItemRecebimentoVOs().iterator();
		while (i.hasNext()) {
			NotaFiscalEntradaItemRecebimentoVO objExistente = i.next();
			if ((isRequisicao && objExistente.getRequisicaoItemVO().getCodigo().equals(codigoCampoSelecao))
					|| (!isRequisicao && objExistente.getCompraItemVO().getCodigo().equals(codigoCampoSelecao))) {
				CentroResultadoOrigemVO cro = new CentroResultadoOrigemVO();
				cro.setCategoriaDespesaVO(objExistente.getCategoriaDespesa());
				cro.setCentroResultadoAdministrativo(objExistente.getCentroResultadoAdministrativo());
				cro.setTipoNivelCentroResultadoEnum(objExistente.getTipoNivelCentroResultadoEnum());
				cro.setFuncionarioCargoVO(objExistente.getFuncionarioCargoVO());
				cro.setUnidadeEnsinoVO(objExistente.getUnidadeEnsinoVO());
				cro.setDepartamentoVO(objExistente.getDepartamentoVO());
				cro.setCursoVO(objExistente.getCursoVO());
				cro.setTurnoVO(objExistente.getTurnoVO());
				cro.setTurmaVO(objExistente.getTurmaVO());
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().removerCentroResultadoOrigemAgrupado(cro,
						obj.getListaCentroResultadoOrigemVOs());
				i.remove();
				return;
			}
		}
	}

	private StringBuilder getSQLPadraoConsultaBasica() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT NotaFiscalEntradaItem.codigo as \"NotaFiscalEntradaItem.codigo\",  ");
		sql.append(" NotaFiscalEntradaItem.quantidade as \"NotaFiscalEntradaItem.quantidade\",  ");
		sql.append(" NotaFiscalEntradaItem.valorUnitario as \"NotaFiscalEntradaItem.valorUnitario\",  ");

		sql.append(" notaFiscalEntrada.codigo as \"notaFiscalEntrada.codigo\",  ");

		sql.append(
				" produtoServico.codigo as \"produtoServico.codigo\", produtoServico.descricao as \"produtoServico.descricao\",  ");
		sql.append(
				" produtoServico.nome as \"produtoServico.nome\", produtoServico.controlarestoque as \"produtoServico.controlarestoque\",  ");
		sql.append(" produtoservico.tipoProdutoServico as \"produtoservico.tipoProdutoServico\", ");

		sql.append(" recebimentocompraitem.codigo as \"recebimentocompraitem.codigo\",  ");
		sql.append(" recebimentocompraitem.quantidaderecebida as \"recebimentocompraitem.quantidaderecebida\", ");
		sql.append(" recebimentocompraitem.recebimentocompra as \"recebimentocompraitem.recebimentocompra\", ");

		sql.append(
				" categoriaproduto.codigo as \"categoriaproduto.codigo\", categoriaproduto.nome as \"categoriaproduto.nome\"  ");
		sql.append(" FROM NotaFiscalEntradaItem ");
		sql.append(
				" inner join notaFiscalEntrada on notaFiscalEntrada.codigo = NotaFiscalEntradaItem.notaFiscalEntrada ");
		sql.append(" inner join produtoServico on produtoServico.codigo = NotaFiscalEntradaItem.produtoServico ");
		sql.append(" inner join categoriaproduto on categoriaproduto.codigo = produtoServico.categoriaproduto ");
		sql.append(
				" left join recebimentocompraitem on recebimentocompraitem.codigo = NotaFiscalEntradaItem.recebimentocompraitem ");
		return sql;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaItemInterfaceFacade#
	 * consultaRapidaPorProdutoServico(java.lang.Integer, boolean, int,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaItemVO> consultaRapidaPorProdutoServico(Integer compra, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE NotaFiscalEntradaItem.produtoServico = ").append(compra).append(" ");
			sqlStr.append(" ORDER BY NotaFiscalEntradaItem.codigo desc ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaItemInterfaceFacade#
	 * consultaRapidaPorNotaFiscalEntrada(java.lang.Integer, boolean, int,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaItemVO> consultaRapidaPorNotaFiscalEntrada(Integer notaFiscalEntrada,
			boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE NotaFiscalEntradaItem.notaFiscalEntrada = ").append(notaFiscalEntrada).append(" ");
			sqlStr.append(" ORDER BY NotaFiscalEntradaItem.codigo ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * negocio.facade.jdbc.faturamento.nfe.NotaFiscalEntradaItemInterfaceFacade#
	 * consultarPorChavePrimaria(java.lang.Integer, boolean, int,
	 * negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public NotaFiscalEntradaItemVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso,
			int nivelMontarDados, UsuarioVO usuario) {
		try {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sqlStr = getSQLPadraoConsultaBasica();
			sqlStr.append(" WHERE NotaFiscalEntradaItem.codigo = ").append(codigoPrm).append(" ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
			if (!tabelaResultado.next()) {
				throw new ConsistirException("Dados Não Encontrados ( NotaFiscalEntradaItemVO ).");
			}
			NotaFiscalEntradaItemVO obj = new NotaFiscalEntradaItemVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
	public List<NotaFiscalEntradaItemVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados,
			UsuarioVO usuario) {
		List<NotaFiscalEntradaItemVO> vetResultado = new ArrayList<>();
		while (tabelaResultado.next()) {
			NotaFiscalEntradaItemVO obj = new NotaFiscalEntradaItemVO();
			montarDadosBasico(obj, tabelaResultado, nivelMontarDados, usuario);
			vetResultado.add(obj);
		}
		return vetResultado;
	}

	private void montarDadosBasico(NotaFiscalEntradaItemVO obj, SqlRowSet dadosSQL, int nivelMontarDados,
			UsuarioVO usuario) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo(dadosSQL.getInt("NotaFiscalEntradaItem.codigo"));
		obj.setQuantidade(dadosSQL.getDouble("NotaFiscalEntradaItem.quantidade"));
		obj.setValorUnitario(dadosSQL.getDouble("NotaFiscalEntradaItem.valorUnitario"));

		obj.getRecebimentoCompraItemVO().setCodigo(dadosSQL.getInt("recebimentocompraitem.codigo"));
		obj.getRecebimentoCompraItemVO().getRecebimentoCompraVO()
				.setCodigo(dadosSQL.getInt("recebimentocompraitem.recebimentocompra"));
		obj.getRecebimentoCompraItemVO()
				.setQuantidadeRecebida((dadosSQL.getDouble("recebimentocompraitem.quantidadeRecebida")));
		obj.getRecebimentoCompraItemVO()
				.setQuantidadeRecebidaAntesAlteracao((dadosSQL.getDouble("recebimentocompraitem.quantidadeRecebida")));

		obj.getProdutoServicoVO().setCodigo(dadosSQL.getInt("produtoServico.codigo"));
		obj.getProdutoServicoVO().setNome(dadosSQL.getString("produtoServico.nome"));
		obj.getProdutoServicoVO().setDescricao(dadosSQL.getString("produtoServico.descricao"));
		obj.getProdutoServicoVO().setControlarEstoque(dadosSQL.getBoolean("produtoServico.controlarestoque"));
		obj.getProdutoServicoVO().setTipoProdutoServicoEnum(
				TipoProdutoServicoEnum.valueOf(dadosSQL.getString("produtoservico.tipoProdutoServico")));
		obj.getProdutoServicoVO().getCategoriaProduto().setCodigo(dadosSQL.getInt("categoriaProduto.codigo"));
		obj.getProdutoServicoVO().getCategoriaProduto().setNome(dadosSQL.getString("categoriaProduto.nome"));
		obj.getNotaFiscalEntradaVO().setCodigo(dadosSQL.getInt("notaFiscalEntrada.codigo"));
		obj.getListaCentroResultadoOrigemVOs()
				.addAll(getFacadeFactory().getCentroResultadoOrigemInterfaceFacade()
						.consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(obj.getCodigo().toString(),
								TipoCentroResultadoOrigemEnum.NOTA_FISCAL_ENTRADA_ITEM,
								Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		obj.getListaNotaFiscalEntradaItemRecebimentoVOs()
				.addAll(getFacadeFactory().getNotaFiscalEntradaItemRecebimentoFacade()
						.consultaRapidaPorNotaFiscalEntrada(obj, false, nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
			return;
		}
	}

	/**
	 * Operação reponsável por retornar o identificador desta classe. Este
	 * identificar é utilizado para verificar as permissões de acesso as operações
	 * desta classe.
	 */
	public static String getIdEntidade() {
		return NotaFiscalEntradaItem.idEntidade;
	}

	@Override
	public void adicionarCentroResultadoOrigemPorArquivoImportacao(FileUploadEvent upload,
			NotaFiscalEntradaVO notaFiscalEntradaVO, NotaFiscalEntradaItemVO notaFiscalEntradaItemVO,
			UsuarioVO usuarioVO) throws Exception {
		Double valorTotalCentroResultado = 0.0;

		ConsistirException consistirException = new ConsistirException();
		List<CentroResultadoOrigemVO> centroResultadoOrigemVOs = getFacadeFactory().getCentroResultadoFacade()
				.realizarLeituraArquivoExcel(upload, notaFiscalEntradaVO.getUnidadeEnsinoVO(), usuarioVO,
						consistirException);

		valorTotalCentroResultado = centroResultadoOrigemVOs.stream().mapToDouble(CentroResultadoOrigemVO::getValor)
				.sum();

		String valorNotaFiscalEntradaItemFormatado = Uteis
				.formatarDecimalDuasCasas(notaFiscalEntradaItemVO.getValorTotal());
		String valorTotalCentroResultadoFormatado = Uteis.formatarDecimalDuasCasas(valorTotalCentroResultado);
		if (!valorNotaFiscalEntradaItemFormatado.equals(valorTotalCentroResultadoFormatado)) {
			consistirException.adicionarListaMensagemErro(
					"- O valor total dos centros de resultado (R$ " + valorTotalCentroResultadoFormatado
							+ ") está dirvergente em relação ao valor total do produto (R$ "
							+ valorNotaFiscalEntradaItemFormatado + ").");
		}

		preencherDadosCentroResultadoOrigem(notaFiscalEntradaVO, notaFiscalEntradaItemVO, centroResultadoOrigemVOs,
				usuarioVO, consistirException);
		if (!consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		}
	}

	public void preencherDadosCentroResultadoOrigem(NotaFiscalEntradaVO notaFiscalEntradaVO,
			NotaFiscalEntradaItemVO notaFiscalEntradaItemVO,
			final List<CentroResultadoOrigemVO> centroResultadoOrigemVOs, UsuarioVO usuario,
			ConsistirException consitirException) throws Exception {
            boolean pararelismo = true ;
		try {
			if(pararelismo) {
			 ProcessarParalelismo.executar(0, centroResultadoOrigemVOs.size(), consitirException, new ProcessarParalelismo.Processo() {			
					@Override
					public void run(int i) {

						try {
							getFacadeFactory().getCentroResultadoFacade()
							.realizarCarregamentoDadosCentroResultadoOrigemPadraoCategoriaDespesa(
									centroResultadoOrigemVOs.get(i), (CategoriaDespesaVO) notaFiscalEntradaItemVO
											.getProdutoServicoVO().getCategoriaProduto().getCategoriaDespesa().clone(),
									usuario);

					
						adicionarCentroResultadoOrigemItemNotaFiscalEntrada(notaFiscalEntradaVO, notaFiscalEntradaItemVO,
								centroResultadoOrigemVOs.get(i), usuario);
					} catch (Exception e) {						
						consitirException.adicionarListaMensagemErro(e.getMessage());

					}
						
					}
				});
			 
			 
		}else {
			for (CentroResultadoOrigemVO centroResultadoOrigemVO : centroResultadoOrigemVOs) {
				
				getFacadeFactory().getCentroResultadoFacade()
						.realizarCarregamentoDadosCentroResultadoOrigemPadraoCategoriaDespesa(
								centroResultadoOrigemVO, (CategoriaDespesaVO) notaFiscalEntradaItemVO
										.getProdutoServicoVO().getCategoriaProduto().getCategoriaDespesa().clone(),
								usuario);

				try {
					adicionarCentroResultadoOrigemItemNotaFiscalEntrada(notaFiscalEntradaVO, notaFiscalEntradaItemVO,
							centroResultadoOrigemVO, usuario);
				} catch (Exception e) {
					consitirException.adicionarListaMensagemErro(e.getMessage());

				}
			}
		}
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().validarDadosTotalizadoresAposAlteracao(
					notaFiscalEntradaItemVO.getListaCentroResultadoOrigemVOs(), notaFiscalEntradaItemVO.getValorTotal(),
					true, usuario);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarValidacaoPorcentagem(
					notaFiscalEntradaItemVO.getListaCentroResultadoOrigemVOs(), notaFiscalEntradaItemVO.getValorTotal(),
					usuario);

			getFacadeFactory().getNotaFiscalEntradaFacade().atualizarListasDaNotaFiscalEntrada(notaFiscalEntradaVO,
					usuario);
		
		} catch (Exception e) {
			consitirException.adicionarListaMensagemErro(e.getMessage());
			throw e;
		}

	}

	public synchronized void adicionarCentroResultadoOrigemItemNotaFiscalEntrada(NotaFiscalEntradaVO notaFiscalEntradaVO,
			NotaFiscalEntradaItemVO notaFiscalEntradaItemVO, CentroResultadoOrigemVO centroResultadoOrigemVO,
			UsuarioVO usuarioVO) throws Exception {
		centroResultadoOrigemVO.setTipoMovimentacaoCentroResultadoOrigemEnum(
				TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR);
		Uteis.checkState(
				notaFiscalEntradaVO.getTipoNotaFiscalEntradaEnum().isProduto()
						&& Uteis.isAtributoPreenchido(centroResultadoOrigemVO.getQuantidade())
						&& centroResultadoOrigemVO.getQuantidade() > notaFiscalEntradaItemVO.getQuantidade(),
				"A Quantidade do Centro Resultado Movimentação não pode ser maior que a Quantidade do Item da Nota Fiscal de Entrada.");
		getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigem(
				notaFiscalEntradaItemVO.getListaCentroResultadoOrigemVOs(), centroResultadoOrigemVO,
				notaFiscalEntradaItemVO.getValorTotal(), false, true, usuarioVO);
		// getNotaFiscalEntradaItemVO().getListaCentroResultadoOrigemVOs().add(centroResultadoOrigemVO);
		// getFacadeFactory().getNotaFiscalEntradaFacade().adicionarCentroResultadoOrigemNotaFiscalEntradaItem(getNotaFiscalEntradaVO(),
		// getNotaFiscalEntradaItemVO(), centroResultadoOrigemVO, getUsuarioLogado());
	}

}
