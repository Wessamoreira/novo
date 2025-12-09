package negocio.facade.jdbc.compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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
import controle.compras.CotacaoControle.EnumSituacaoTramitacao;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoComprasEnum;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.CotacaoFornecedorVO;
import negocio.comuns.compras.CotacaoHistoricoVO;
import negocio.comuns.compras.CotacaoVO;
import negocio.comuns.compras.DepartamentoTramiteCotacaoCompraVO;
import negocio.comuns.compras.EstatisticaCotacaoVO;
import negocio.comuns.compras.EstoqueVO;
import negocio.comuns.compras.ItemCotacaoUnidadeEnsinoVO;
import negocio.comuns.compras.ItemCotacaoVO;
import negocio.comuns.compras.ItemCronologicoEstatisticaVO;
import negocio.comuns.compras.ItemSumarioUnidadeEstatisticaVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.enumeradores.OrdenarItemCotacaoEnum;
import negocio.comuns.compras.enumeradores.TipoCriacaoContaPagarEnum;
import negocio.comuns.compras.enumeradores.TipoProdutoServicoEnum;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.CotacaoInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class Cotacao extends ControleAcesso implements CotacaoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 671912977636987776L;
	protected static String idEntidade;

	public Cotacao() {
		super();
		setIdEntidade("Cotacao");
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(final CotacaoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			Cotacao.incluir(getIdEntidade(), true, usuarioVO);
			CotacaoVO.validarDados(obj);
			final String sql = "INSERT INTO Cotacao(responsavelCotacao, dataCotacao, responsavelAutorizacao, dataAutorizacao, situacao, categoriaProduto, motivoRevisao, unidadeEnsinoResponsavelTramitacao, tramiteCotacaoCompra, categoriadespesa, cursoCategoriaDespesa, turnoCategoriaDespesa, turmaCategoriaDespesa, unidadeEnsinoCategoriaDespesa, departamentoCategoriaDespesa, funcionarioCategoriaDespesa, centroResultadoAdministrativo, tipoNivelCentroResultadoEnum ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";
			obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlInserir = arg0.prepareStatement(sql);
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getResponsavelCotacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataCotacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getResponsavelAutorizacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDataAutorizacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getSituacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCategoriaProduto(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getMotivoRevisao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoResponsavelTramitacao(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTramiteCotacaoCompra(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCategoriaDespesa(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCursoCategoriaDespesa(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTurnoCategoriaDespesa(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTurmaCategoriaDespesa(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoCategoriaDespesa(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getDepartamentoCategoriaDespesa(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getFuncionarioCategoriaDespesa(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getCentroResultadoAdministrativo(), ++i, sqlInserir);
					Uteis.setValuePreparedStatement(obj.getTipoNivelCentroResultadoEnum(), ++i, sqlInserir);
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
			getFacadeFactory().getCotacaoFornecedorFacade().incluirCotacaoFornecedors(obj, obj.getCotacaoFornecedorVOs(), usuarioVO);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().persistir(obj.getListaCentroResultadoOrigemVOs(), obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.COTACAO, false, usuarioVO, false);
			alterarItemRequisicao(obj, usuarioVO);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setCodigo(0);
			obj.setNovoObj(Boolean.TRUE);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final CotacaoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			Cotacao.alterar(getIdEntidade(), true, usuarioVO);
			CotacaoVO.validarDados(obj);
			final String sql = "UPDATE Cotacao set responsavelCotacao=?, dataCotacao=?, responsavelAutorizacao=?, dataAutorizacao=?, situacao=?, categoriaProduto=?, motivoRevisao=?, unidadeEnsinoResponsavelTramitacao=?, tramiteCotacaoCompra=?, categoriadespesa=?, cursoCategoriaDespesa=?, turnoCategoriaDespesa=?, turmaCategoriaDespesa=?, unidadeEnsinoCategoriaDespesa=?, departamentoCategoriaDespesa=?, funcionarioCategoriaDespesa=?, centroResultadoAdministrativo=?, tipoNivelCentroResultadoEnum =? WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
					int i = 0;
					Uteis.setValuePreparedStatement(obj.getResponsavelCotacao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDataCotacao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getResponsavelAutorizacao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDataAutorizacao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getSituacao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCategoriaProduto(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getMotivoRevisao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoResponsavelTramitacao(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTramiteCotacaoCompra(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCategoriaDespesa(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCursoCategoriaDespesa(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTurnoCategoriaDespesa(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTurmaCategoriaDespesa(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoCategoriaDespesa(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getDepartamentoCategoriaDespesa(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getFuncionarioCategoriaDespesa(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCentroResultadoAdministrativo(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getTipoNivelCentroResultadoEnum(), ++i, sqlAlterar);
					Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
					return sqlAlterar;
				}
			});

			getFacadeFactory().getCotacaoFornecedorFacade().alterarCotacaoFornecedors(obj, obj.getCotacaoFornecedorVOs(), usuarioVO);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().persistir(obj.getListaCentroResultadoOrigemVOs(), obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.COTACAO, false, usuarioVO, false);
			alterarItemRequisicao(obj, usuarioVO);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void indeferirCotacao(CotacaoVO obj, CotacaoHistoricoVO cotHistorico, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception{
		/**
		 * Regra comentada pois uma requisição poderá estar em mais de uma cotação sendo assim não poderá ser indeferida
		 */
//		getFacadeFactory().getRequisicaoFacade().atualizarSituacaoRequisicaoPorOperacaoEmCotacao(obj, usuario);
		getFacadeFactory().getCotacaoFacade().alterarSituacaoCotacao(obj.getCodigo(), obj.getSituacao(), new Date(), usuario, obj.getMotivoRevisao());
		if (Objects.nonNull(cotHistorico)) {
			getFacadeFactory().getCotacaoHistoricoInterfaceFacade().finalizarTramiteCotacao(cotHistorico, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario, conf);
		}
	}
	
	

	private void alterarItemRequisicao(CotacaoVO obj, UsuarioVO usuarioVO) throws Exception {
		getFacadeFactory().getRequisicaoItemFacade().anularVinculoRequisicaoItemComCotacao(obj.getCodigo(), usuarioVO);
		for (ItemCotacaoVO itemCotacao : obj.getItemCotacaoVOs()) {
			for (ItemCotacaoUnidadeEnsinoVO icue : itemCotacao.getListaItemCotacaoUnidadeEnsinoVOs()) {
				getFacadeFactory().getRequisicaoItemFacade().atualizarRequisitoItemPorCotacao(icue, usuarioVO);
			}
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void liberarCotacaoParaMapaComTramitacao(final CotacaoVO obj, UsuarioVO usuarioResponsavelDepartamento, UsuarioVO usuarioLogado) throws Exception {
		if (!Uteis.isAtributoPreenchido(obj)) {
			incluir(obj, usuarioLogado);
		} else {
			alterar(obj, usuarioLogado);
		}
		if (Uteis.isAtributoPreenchido(obj.getTramiteCotacaoCompra()) && !getFacadeFactory().getCotacaoHistoricoInterfaceFacade().isTramiteIniciado(obj)) {
			obj.getListaCotacaoHistoricoVOs().add(getFacadeFactory().getCotacaoHistoricoInterfaceFacade().iniciarTramiteCotacao(obj, usuarioResponsavelDepartamento, false, Uteis.NIVELMONTARDADOS_TODOS, usuarioLogado));
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoCotacao(final Integer codigo, final String situacao, final Date data, final UsuarioVO responsavel, final String motivoRevisao) throws Exception {
		final StringBuilder sql = new StringBuilder();
		sql.append("UPDATE Cotacao set responsavelAutorizacao=?, dataAutorizacao=?, situacao=?, motivoRevisao=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(responsavel));
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				if (responsavel.getCodigo().intValue() != 0) {
					sqlAlterar.setInt(1, responsavel.getCodigo().intValue());
				} else {
					sqlAlterar.setNull(1, 0);
				}
				sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(data));
				sqlAlterar.setString(3, situacao);
				sqlAlterar.setString(4, motivoRevisao);
				sqlAlterar.setInt(5, codigo.intValue());
				return sqlAlterar;
			}
		});
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CotacaoVO obj, UsuarioVO usuarioVO) throws Exception {
		try {
			Cotacao.excluir(getIdEntidade(), true, usuarioVO);
			getFacadeFactory().getRequisicaoItemFacade().anularVinculoRequisicaoItemComCotacao(obj.getCodigo(), usuarioVO);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().excluidoRegistroNaoExistenteListaPorCodOrigemPorTipoCentroResultadoOrigemEnum(null, obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.COTACAO, usuarioVO);
			String sql = "DELETE FROM Cotacao WHERE ((codigo = ?))";
			getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public void atualizarCotacaoFornecedorEscolha(CotacaoVO cotacaoVO, ItemCotacaoVO item, UsuarioVO usuarioLogado) {
		Stream<ItemCotacaoVO> flatMap = cotacaoVO.getCotacaoFornecedorVOs()
				.stream()
				.filter(p -> !p.getFornecedor().getCodigo().equals(item.getCotacaoFornecedor().getFornecedor().getCodigo()))
				.flatMap(p -> p.getItemCotacaoVOs().stream())
				.filter(itemExistente -> itemExistente.getProduto().getCodigo().equals(item.getProduto().getCodigo()));
		flatMap.forEach(p -> p.setCompraAutorizadaFornecedor(false));
		gerarCentroResultadoOrigem(cotacaoVO, usuarioLogado);
	}

	@Override
	public void adicionarNovaCotacaoFornecedor(CotacaoFornecedorVO obj, CotacaoVO cotacaoVO) throws Exception {
		try {
			Iterator<ItemCotacaoVO> i = cotacaoVO.getItemCotacaoVOs().iterator();
			while (i.hasNext()) {
				ItemCotacaoVO clone = (i.next()).getClone();
				clone.setPrecoAnterior(getFacadeFactory().getItemCotacaoFacade().consultarUltimoPrecoProdutoFornecedor(obj.getFornecedor().getCodigo(), clone.getProduto().getCodigo()));
				obj.adicionarObjItemCotacaoVOs(clone, true);
			}
			Collections.sort(obj.getItemCotacaoVOs(), OrdenarItemCotacaoEnum.PRODUTO.asc());
			adicionarObjCotacaoFornecedorVOs(cotacaoVO, obj);
		} catch (Exception e) {
			throw e;
		}
	}

	private void adicionarObjCotacaoFornecedorVOs(CotacaoVO cotacaoVO, CotacaoFornecedorVO obj) throws ConsistirException {
		CotacaoFornecedorVO.validarDados(obj);
		int index = 0;
		obj.setCotacao(cotacaoVO);
		for (CotacaoFornecedorVO objExistente : cotacaoVO.getCotacaoFornecedorVOs()) {
			if (objExistente.getFornecedor().getCodigo().equals(obj.getFornecedor().getCodigo())) {
				cotacaoVO.getCotacaoFornecedorVOs().set(index, obj);
				return;
			}
			index++;
		}
		cotacaoVO.getCotacaoFornecedorVOs().add(obj);		
	}

	@Override
	public void excluirObjCotacaoFornecedorVOs(CotacaoVO cotacaoVO, Integer fornecedor, UsuarioVO usuarioLogado) {
		Iterator<CotacaoFornecedorVO> i = cotacaoVO.getCotacaoFornecedorVOs().iterator();
		while (i.hasNext()) {
			CotacaoFornecedorVO objExistente = i.next();
			if (objExistente.getFornecedor().getCodigo().equals(fornecedor)) {
				i.remove();
				atualizarCssItemCotacao(cotacaoVO);
				gerarCentroResultadoOrigem(cotacaoVO, usuarioLogado);
				return;
			}
		}
	}

	public CotacaoVO montarListaCotacao(CotacaoVO cotacaoVO, ItemCotacaoVO itemCotacaoVO, boolean atualizar, UsuarioVO usuarioLogado) throws Exception {
		int index = 0;
		List<ItemCotacaoVO> listaItemCotacaoVOMenoPreco = new ArrayList<>();
		for (CotacaoFornecedorVO objExistente : cotacaoVO.getCotacaoFornecedorVOs()) {
			if (!atualizar) {
				ItemCotacaoVO item = itemCotacaoVO.getClone();
				item.setPrecoAnterior(getFacadeFactory().getItemCotacaoFacade().consultarUltimoPrecoProdutoFornecedor(objExistente.getFornecedor().getCodigo(), item.getProduto().getCodigo()));
				objExistente.adicionarObjItemCotacaoVOs(item, true);
			} else {
				objExistente.atualizarObjItemCotacaoVOs(itemCotacaoVO);
			}
			Collections.sort(objExistente.getItemCotacaoVOs(), OrdenarItemCotacaoEnum.PRODUTO.asc());
			listaItemCotacaoVOMenoPreco.add(objExistente.consultarObjItemCotacaoVOs(itemCotacaoVO));
			cotacaoVO.getCotacaoFornecedorVOs().set(index, objExistente);
			index++;
		}
		preencherCssItemCotacao(listaItemCotacaoVOMenoPreco);
		gerarCentroResultadoOrigem(cotacaoVO, usuarioLogado);
		return cotacaoVO;
	}

	@Override
	public CotacaoVO removerItemCotacaoVO(CotacaoVO cotacaoVO, ItemCotacaoVO itemCotacaoVO, UsuarioVO usuarioLogado) {
		Iterator<CotacaoFornecedorVO> i = cotacaoVO.getCotacaoFornecedorVOs().iterator();
		while (i.hasNext()) {
			CotacaoFornecedorVO objExistente = i.next();
			objExistente.excluirObjItemCotacaoVOs(itemCotacaoVO.getProduto().getCodigo());
		}
		gerarCentroResultadoOrigem(cotacaoVO, usuarioLogado);
		return cotacaoVO;
	}

	@Override
	public void removerUnidadeEnsinoVO(CotacaoVO cotacaoVO, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuarioLogado) {
		cotacaoVO.getCotacaoFornecedorVOs()
				.stream()
				.flatMap(p -> p.getItemCotacaoVOs().stream())
				.forEach(q -> q.getListaItemCotacaoUnidadeEnsinoVOs().removeIf(p -> p.getUnidadeEnsinoVO().getCodigo().equals(unidadeEnsinoVO.getCodigo())));
		gerarCentroResultadoOrigem(cotacaoVO, usuarioLogado);
	}

	@Override
	public void gerarCentroResultadoOrigem(CotacaoVO obj, UsuarioVO usuarioLogado) {
		if (Uteis.isAtributoPreenchido(obj)) {
			List<CentroResultadoOrigemVO> novaLista = preencherNovaListaCentroResultadoOrigem(obj);
			Iterator<CentroResultadoOrigemVO> i = obj.getListaCentroResultadoOrigemVOs().iterator();
			while (i.hasNext()) {
				CentroResultadoOrigemVO objExistente = i.next();
				if (removerCentroResultadoOrigem(objExistente, novaLista)) {
					i.remove();
				}
			}
			for (CentroResultadoOrigemVO novo : novaLista) {
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().atualizarCentroResultadoOrigemAgrupado(novo, obj.getListaCentroResultadoOrigemVOs(), false);
			}
		} else {
			obj.setListaCentroResultadoOrigemVOs(preencherNovaListaCentroResultadoOrigem(obj));
		}
		getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarValidacaoPorcentagem(obj.getListaCentroResultadoOrigemVOs(), obj.getValorTotalCompraCotacao().doubleValue(), usuarioLogado);
	}

	private boolean removerCentroResultadoOrigem(CentroResultadoOrigemVO objExistente, List<CentroResultadoOrigemVO> novaLista) {
		for (CentroResultadoOrigemVO novo : novaLista) {
			if (novo.equalsAgrupadoCentroResultadoOrigem(objExistente)) {
				return false;
			}
		}
		return true;
	}	

	private List<CentroResultadoOrigemVO> preencherNovaListaCentroResultadoOrigem(CotacaoVO obj) {
		List<CentroResultadoOrigemVO> lista = new ArrayList<>();
		Stream<ItemCotacaoVO> streamItemCotacao = obj.getCotacaoFornecedorVOs().stream().flatMap(p -> p.getItemCotacaoVOs().stream().filter(ItemCotacaoVO::getCompraAutorizadaFornecedor));
		streamItemCotacao.forEach(p -> {
			for (ItemCotacaoUnidadeEnsinoVO icueExistente : p.getListaItemCotacaoUnidadeEnsinoVOs()) {				
				for (RequisicaoItemVO ri : icueExistente.getListaRequisicaoItemVOs()) {
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
					cro.setValor(ri.getQuantidadeAutorizada() * p.getPrecoUnitario());
					cro.calcularPorcentagem(obj.getValorTotalCompraCotacao().doubleValue());
					getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigemAgrupado(cro, lista);
				}
				if (Uteis.isAtributoPreenchido(icueExistente.getQtdAdicional())) {
					CentroResultadoOrigemVO cro = new CentroResultadoOrigemVO();
					cro.setTipoMovimentacaoCentroResultadoOrigemEnum(TipoMovimentacaoCentroResultadoOrigemEnum.NAO_CONTABILIZAR);
					cro.setCategoriaDespesaVO(obj.getCategoriaDespesa());
					cro.setUnidadeEnsinoVO(obj.getUnidadeEnsinoCategoriaDespesa());
					cro.setDepartamentoVO(obj.getDepartamentoCategoriaDespesa());
					cro.setCursoVO(obj.getCursoCategoriaDespesa());
					cro.setTurnoVO(obj.getTurnoCategoriaDespesa());
					cro.setTurmaVO(obj.getTurmaCategoriaDespesa());
					cro.setTipoNivelCentroResultadoEnum(obj.getTipoNivelCentroResultadoEnum());
					cro.setCentroResultadoAdministrativo(obj.getCentroResultadoAdministrativo());
					cro.setQuantidade(icueExistente.getQtdAdicional());
					cro.setValor(icueExistente.getQtdAdicional() * p.getPrecoUnitario());
					cro.calcularPorcentagem(obj.getValorTotalCompraCotacao().doubleValue());
					getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigemAgrupado(cro, lista);
				}
			}
		});
		return lista;
	}

	@Override
	public void atualizarCssItemCotacao(CotacaoVO cotacaoVO) {
		for (ItemCotacaoVO itemExistente : cotacaoVO.getItemCotacaoVOs()) {
			List<ItemCotacaoVO> listaItemCotacaoVOMenoPreco = new ArrayList<>();
			for (CotacaoFornecedorVO objExistente : cotacaoVO.getCotacaoFornecedorVOs()) {
				listaItemCotacaoVOMenoPreco.add(objExistente.consultarObjItemCotacaoVOs(itemExistente));
			}
			preencherCssItemCotacao(listaItemCotacaoVOMenoPreco);
		}
	}

	private void preencherCssItemCotacao(List<ItemCotacaoVO> listaItemCotacaoVOMenoPreco) {
		Optional<ItemCotacaoVO> menoPreco = listaItemCotacaoVOMenoPreco.stream().filter(p -> Uteis.isAtributoPreenchido(p.getPrecoUnitario())).min(Comparator.comparingDouble(ItemCotacaoVO::getPrecoUnitario));
		if (menoPreco.isPresent() && Uteis.isAtributoPreenchido(menoPreco.get().getProduto())) {
			menoPreco.get().setCss("background-color:#91CC86");
			for (ItemCotacaoVO p : listaItemCotacaoVOMenoPreco) {
				if (Uteis.isAtributoPreenchido(p.getPrecoUnitario()) && p.getPrecoUnitario().equals(menoPreco.get().getPrecoUnitario()) && !p.getCotacaoFornecedor().getFornecedor().getCodigo().equals(menoPreco.get().getCotacaoFornecedor().getFornecedor().getCodigo())) {
					p.setCss("background-color:#FFFF99");
					menoPreco.get().setCss("background-color:#FFFF99");
				} else if (!Uteis.isAtributoPreenchido(p.getPrecoUnitario()) || !p.getPrecoUnitario().equals(menoPreco.get().getPrecoUnitario()) && !p.getCotacaoFornecedor().getFornecedor().getCodigo().equals(menoPreco.get().getCotacaoFornecedor().getFornecedor().getCodigo())) {
					p.setCss("backgroud-color:transparent;");
				}
			}
		}
	}

	@Override
	public void adicionarProdutoServicoNaCotacao(List<RequisicaoVO> listaRequisicao, CotacaoVO cotacaoVO, ItemCotacaoVO itemCotacaoVO, UsuarioVO usuario) throws Exception {
		for (UnidadeEnsinoVO un : cotacaoVO.getListaUnidadeEnsinoVOs()) {
			if (un.getEscolhidaParaFazerCotacao()) {
				ItemCotacaoUnidadeEnsinoVO itemCotacaoUnidadeEnsinoVO = new ItemCotacaoUnidadeEnsinoVO();
				itemCotacaoUnidadeEnsinoVO.setProdutoVO(itemCotacaoVO.getProduto());
				itemCotacaoUnidadeEnsinoVO.setUnidadeEnsinoVO(un);
				if (itemCotacaoVO.getProduto().getControlarEstoque()) {
					EstoqueVO estoque = getFacadeFactory().getEstoqueFacade().consultarEstoquerPorProdutoPorUnidadeValidandoEstoqueMinino(itemCotacaoVO.getProduto().getCodigo(), un.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS);
					itemCotacaoUnidadeEnsinoVO.setQtdMinimaUnidade(estoque.getEstoqueMinimo());
				}
				itemCotacaoUnidadeEnsinoVO.setListaRequisicaoItemVOs(getFacadeFactory().getRequisicaoItemFacade().consultarRequisicaoItemsPorItemCotacaoUnidadeEnsinoVO(listaRequisicao, itemCotacaoUnidadeEnsinoVO, Uteis.NIVELMONTARDADOS_TODOS, usuario));

				itemCotacaoVO.adicionarObjItemCotacaoUnidadeEnsinoVO(itemCotacaoUnidadeEnsinoVO);
				
				
			}
		}
		montarListaCotacao(cotacaoVO, itemCotacaoVO, false, usuario);
	}

	@Override
	public void preencherDadosPorCategoriaDespesa(CotacaoVO obj, UsuarioVO usuario) {
		try {
			CentroResultadoVO crAdministrativo = null;
			CursoVO cursoFiltro = null;
			TurmaVO turmaFiltro = null;
			if (obj.getTipoNivelCentroResultadoEnum().isUnidadeEnsino() && Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoCategoriaDespesa())) {
				crAdministrativo = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorUnidadeEnsino(obj.getUnidadeEnsinoCategoriaDespesa().getCodigo(), usuario);
			}
			if (obj.getTipoNivelCentroResultadoEnum().isDepartamento() && Uteis.isAtributoPreenchido(obj.getDepartamentoCategoriaDespesa())) {
				crAdministrativo =  getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorDepartamento(obj.getDepartamentoCategoriaDespesa().getCodigo(), usuario);
			}
			if ((obj.getTipoNivelCentroResultadoEnum().isCurso() || obj.getTipoNivelCentroResultadoEnum().isCursoTurno()) && Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoCategoriaDespesa()) && Uteis.isAtributoPreenchido(obj.getCursoCategoriaDespesa())) {
				crAdministrativo = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorUnidadeEnsinoCurso(obj.getUnidadeEnsinoCategoriaDespesa().getCodigo(), obj.getCursoCategoriaDespesa().getCodigo(), usuario);
				cursoFiltro = obj.getCursoCategoriaDespesa();
				turmaFiltro = null;
			}
			if (obj.getTipoNivelCentroResultadoEnum().isTurma() && Uteis.isAtributoPreenchido(obj.getTurmaCategoriaDespesa())) {
				crAdministrativo = getFacadeFactory().getCentroResultadoFacade().consultarCentroResultadoPorTurma(obj.getTurmaCategoriaDespesa().getCodigo(), usuario);
				turmaFiltro = obj.getTurmaCategoriaDespesa();
				cursoFiltro = null;
			}
			if(Uteis.isAtributoPreenchido(crAdministrativo) && getFacadeFactory().getCentroResultadoFacade().validarRestricaoUsoCentroResultado(crAdministrativo, obj.getDepartamentoCategoriaDespesa(), cursoFiltro, turmaFiltro, usuario)){
				obj.setCentroResultadoAdministrativo(crAdministrativo);	
			}
			gerarCentroResultadoOrigem(obj, usuario);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	public void consultar(CotacaoVO obj, Integer departamentoFiltro, String produtoFiltro, Integer requisicao, EnumSituacaoTramitacao enumSituacaoTramitacaoFiltro, DataModelo dataModelo, boolean permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento) {
		try {
			dataModelo.setListaConsulta(consultarPorTodosOsTermos(obj, departamentoFiltro, produtoFiltro, requisicao, enumSituacaoTramitacaoFiltro, dataModelo,  permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino,  permiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino,  permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento));
			dataModelo.getListaFiltros().clear();
			dataModelo.setTotalRegistrosEncontrados(consultarTotalTodosOsTermos(obj, departamentoFiltro, produtoFiltro, requisicao, enumSituacaoTramitacaoFiltro, dataModelo,  permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino,  permiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino,  permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento));
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private String getSqlConsultaBasica(CotacaoVO obj, Integer departamentoFiltro, String produtoFiltro, Integer requisicao, EnumSituacaoTramitacao enumSituacaoTramitacaoFiltro, DataModelo dataModelo, boolean permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento) {
		StringBuilder sql = new StringBuilder("");
		sql.append(" select distinct ");
		sql.append(" cotacao.dataautorizacao as \"cotacao.dataautorizacao\", cotacao.datacotacao as \"cotacao.datacotacao\", ");
		sql.append(" cotacao.codigo as \"cotacao.codigo\", cotacao.situacao as \"cotacao.situacao\", ");
		sql.append(" cotacao.motivorevisao as \"cotacao.motivorevisao\",  ");
		sql.append(" cotacao.unidadeEnsinoResponsavelTramitacao as \"cotacao.unidadeEnsinoResponsavelTramitacao\",  ");
		sql.append(" responsavel.codigo as \"responsavel.codigo\", responsavel.nome as \"responsavel.nome\",   ");

		sql.append(" responsavelautorizacao.codigo as \"responsavelautorizacao.codigo\", responsavelautorizacao.nome as \"responsavelautorizacao.nome\",   ");

		sql.append(" categoriaproduto.codigo as \"categoriaproduto.codigo\", categoriaproduto.nome as \"categoriaproduto.nome\",   ");

		sql.append(" tramite.codigo as \"tramite.codigo\", tramite.nome as \"tramite.nome\",   ");
		
		sql.append(" cotacaoHistorico.codigo as \"cotacaoHistorico.codigo\", ");
		sql.append(" responsavelcotacaoHistorico.codigo as \"responsavelcotacaoHistorico.codigo\", responsavelcotacaoHistorico.nome as \"responsavelcotacaoHistorico.nome\",   ");
		sql.append(" departamentotramitehistorico.codigo as \"departamentotramitehistorico.codigo\", ");
		sql.append(" departamento.codigo as \"departamento.codigo\", departamento.nome as \"departamento.nome\"   ");
		
		sql.append(" from cotacao ");
		sql.append(" inner join categoriaproduto ON cotacao.categoriaproduto = categoriaproduto.codigo");
		sql.append(" inner join usuario AS responsavel ON cotacao.responsavelcotacao = responsavel.codigo");
		sql.append(" left  join itemcotacaounidadeensino  ON cotacao.codigo = itemcotacaounidadeensino.cotacao");
		sql.append(" left  join usuario AS responsavelautorizacao ON cotacao.responsavelautorizacao = responsavelautorizacao.codigo");
		sql.append(" left  join tramite ON cotacao.tramiteCotacaoCompra = tramite.codigo");
		sql.append(" left  join cotacaoHistorico ON cotacao.codigo = cotacaoHistorico.cotacao and cotacaohistorico.codigo = ( ");
		sql.append(" select codigo from cotacaohistorico ch where ch.cotacao = cotacao.codigo order by ch.codigo desc limit 1 ) ");
		sql.append(" left  join usuario AS responsavelcotacaoHistorico ON responsavelcotacaoHistorico.codigo = cotacaoHistorico.usuario");
		sql.append(" left  join departamentotramite as departamentotramitehistorico ON cotacaoHistorico.departamentoTramiteCotacaoCompra = departamentotramitehistorico.codigo");
//		if (permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento) {
//			sql.append(" inner join funcionario on 	responsavelcotacaoHistorico.pessoa = funcionario.pessoa ");
//			sql.append(" left join funcionariocargo on funcionario.codigo = funcionariocargo.funcionario ");
//			sql.append(" left join departamento on funcionariocargo.departamento = departamento.codigo ");
//		}else {
			sql.append(" left  join departamento ON departamentotramitehistorico.departamento = departamento.codigo ");
//		}
		sql.append(" WHERE 1=1");
		montarFiltrosConsulta(obj, departamentoFiltro, produtoFiltro, requisicao, enumSituacaoTramitacaoFiltro, dataModelo, sql,  permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino,  permiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino,  permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento);
		return sql.toString();
	}

	private List<CotacaoVO> consultarPorTodosOsTermos(CotacaoVO obj, Integer departamentoFiltro, String produtoFiltro, Integer requisicao, EnumSituacaoTramitacao enumSituacaoTramitacaoFiltro, DataModelo dataModelo, boolean permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
		StringBuilder sql = new StringBuilder(getSqlConsultaBasica(obj, departamentoFiltro, produtoFiltro, requisicao, enumSituacaoTramitacaoFiltro, dataModelo,  permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino,  permiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino,  permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento));
		sql.append(" order by cotacao.datacotacao ");
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
		return montarDadosConsultaBasica(tabelaResultado, dataModelo.getUsuario());
	}

	private Integer consultarTotalTodosOsTermos(CotacaoVO obj, Integer departamentoFiltro, String produtoFiltro, Integer requisicao, EnumSituacaoTramitacao enumSituacaoTramitacaoFiltro, DataModelo dataModelo, boolean permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento) throws Exception {
		try {
			StringBuilder sql = new StringBuilder("select count(t.\"cotacao.codigo\") as qtde from ( ");
			sql.append(getSqlConsultaBasica(obj, departamentoFiltro, produtoFiltro, requisicao, enumSituacaoTramitacaoFiltro, dataModelo,  permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino,  permiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino,  permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento));
			sql.append(" ) as t ");
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), dataModelo.getListaFiltros().toArray());
			return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void montarFiltrosConsulta(CotacaoVO obj, Integer departamentoFiltro, String produtoFiltro, Integer requisicao, EnumSituacaoTramitacao enumSituacaoTramitacaoFiltro, DataModelo dataModelo, StringBuilder sql ,  boolean permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento) {
		
		if (permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento) {
			dataModelo.getListaFiltros().add(departamentoFiltro);
			dataModelo.getListaFiltros().add(PERCENT + obj.getAtualCotacaoHistoricoVO().getResponsavel().getNome().toLowerCase() + PERCENT);
			dataModelo.getListaFiltros().add(PERCENT + obj.getResponsavelCotacao().getNome().toLowerCase() + PERCENT);
			sql.append(" and (");
				sql.append("(departamento.codigo is null or departamento.codigo  = ? )");
				sql.append(" or (lower(sem_acentos(responsavelcotacaoHistorico.nome)) ilike sem_acentos(?))");
				sql.append("or (lower(sem_acentos(responsavel.nome)) ilike sem_acentos(?))");
			sql.append(" )");
		}else {		
			if ((Uteis.isAtributoPreenchido(departamentoFiltro)) && !Uteis.isAtributoPreenchido(obj.getCodigo())) {
				dataModelo.getListaFiltros().add(departamentoFiltro);
				sql.append(" and (departamento.codigo is null or departamento.codigo  = ? )");
			}
			if (Uteis.isAtributoPreenchido(obj.getAtualCotacaoHistoricoVO().getResponsavel().getNome()) && permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino == false) {
				dataModelo.getListaFiltros().add(PERCENT + obj.getAtualCotacaoHistoricoVO().getResponsavel().getNome().toLowerCase() + PERCENT);
				dataModelo.getListaFiltros().add(PERCENT + obj.getResponsavelCotacao().getNome().toLowerCase() + PERCENT);
				sql.append(" and (");
				sql.append("      (lower(sem_acentos(responsavelcotacaoHistorico.nome)) ilike sem_acentos(?))");
				sql.append("   or (lower(sem_acentos(responsavel.nome)) ilike sem_acentos(?))");
			sql.append(" )");
			}
		}
//			if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoResponsavelTramitacao()) && !permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino && !permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento) {
//				dataModelo.getListaFiltros().add(obj.getUnidadeEnsinoResponsavelTramitacao().getCodigo());
//				dataModelo.getListaFiltros().add(PERCENT + obj.getAtualCotacaoHistoricoVO().getResponsavel().getNome().toLowerCase() + PERCENT);
//				dataModelo.getListaFiltros().add(PERCENT + obj.getResponsavelCotacao().getNome().toLowerCase() + PERCENT);
//				sql.append(" and (");
//					sql.append("(ItemCotacaoUnidadeEnsino.unidadeensino = ?  or ItemCotacaoUnidadeEnsino.unidadeensino is null )");
//					sql.append(" or (lower(sem_acentos(responsavelcotacaoHistorico.nome)) ilike sem_acentos(?))");
//					sql.append("or (lower(sem_acentos(responsavel.nome)) ilike sem_acentos(?))");
//				sql.append(" )");
			 if (Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoResponsavelTramitacao())) {
				dataModelo.getListaFiltros().add(obj.getUnidadeEnsinoResponsavelTramitacao().getCodigo());
				sql.append("and (ItemCotacaoUnidadeEnsino.unidadeensino = ?  or ItemCotacaoUnidadeEnsino.unidadeensino is null )");
			}
		
		if (Uteis.isAtributoPreenchido(obj.getSituacao())) {
			if(obj.getSituacao().equals("MC")){//vindo do Mapa de cotacao apresentar somente 
				sql.append(" and cotacao.situacao in('AA','AU','IN') ");
			}else{
				dataModelo.getListaFiltros().add(obj.getSituacao());
				sql.append(" and cotacao.situacao = ? ");
			}
		}
		
		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			dataModelo.getListaFiltros().add(obj.getCodigo());
			sql.append(" and cotacao.codigo = ?");
			return;
		}
		
		if (Uteis.isAtributoPreenchido(obj.getCategoriaProduto().getNome())) {
			dataModelo.getListaFiltros().add(PERCENT + obj.getCategoriaProduto().getNome().toLowerCase() + PERCENT);
			sql.append(" and lower(sem_acentos(categoriaproduto.nome)) ilike sem_acentos(?)");
		}
		
		if (Uteis.isAtributoPreenchido(obj.getTramiteCotacaoCompra().getNome())) {
			dataModelo.getListaFiltros().add(PERCENT + obj.getTramiteCotacaoCompra().getNome().toLowerCase() + PERCENT);
			sql.append(" and lower(sem_acentos(tramite.nome)) ilike sem_acentos(?)");
		}
		
		if (Objects.nonNull(dataModelo.getDataIni()) && Objects.nonNull(dataModelo.getDataFim())) {
			sql.append(String.format(" and cotacao.datacotacao BETWEEN '%s'  AND '%s'", Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(dataModelo.getDataIni()), Uteis.getDataComHoraSetadaParaUltimoMinutoDia(dataModelo.getDataFim())));
		}

				

		if (Uteis.isAtributoPreenchido(produtoFiltro)) {
			sql.append(" and cotacao.codigo in (");
			sql.append(" select cotacao.codigo from cotacao");
			sql.append(" left join itemcotacao on itemcotacao.cotacao = cotacao.codigo");
			sql.append(" left join produtoservico on itemcotacao.produto = produtoservico.codigo");
			sql.append(String.format(" where lower(sem_acentos(produtoservico.nome)) ilike sem_acentos('%%%s%%'))", produtoFiltro.toLowerCase()));
		}
		
		if (enumSituacaoTramitacaoFiltro.isNoPrazo()) {
			sql.append(" and (cotacao.tramitecotacaocompra is null or ");
			sql.append(" ( ");
			sql.append("   ( ");
			sql.append("    select Sum(departamentotramite.prazoexecucao) from tramite ");
			sql.append("    INNER JOIN departamentotramite ON tramite.codigo = departamentotramite.tramite ");
			sql.append("    where cotacao.tramitecotacaocompra = tramite.codigo ");
			sql.append("   ) ");
			sql.append("   - ");
			sql.append("   (( Extract(epoch FROM (SELECT ( CURRENT_DATE - (SELECT datainicio FROM cotacaohistorico as ch ");
			sql.append("      WHERE  cotacao.codigo = ch.cotacao ORDER  BY ch.codigo LIMIT  1)))) / 86400 ");
			sql.append("     ) :: INT ");
			sql.append("   ) >= 0");
			sql.append(" )");
			sql.append(" )");
		}
		
		if (enumSituacaoTramitacaoFiltro.isAtrazado()) {
			sql.append(" and cotacao.tramitecotacaocompra is not null and ");
			sql.append(" ( ");
			sql.append("   ( ");
			sql.append("    select Sum(departamentotramite.prazoexecucao) from tramite ");
			sql.append("    INNER JOIN departamentotramite ON tramite.codigo = departamentotramite.tramite ");
			sql.append("    where cotacao.tramitecotacaocompra = tramite.codigo ");
			sql.append("   ) ");
			sql.append("   - ");
			sql.append("   (( Extract(epoch FROM (SELECT ( CURRENT_DATE - (SELECT datainicio FROM cotacaohistorico as ch ");
			sql.append("      WHERE  cotacao.codigo = ch.cotacao ORDER  BY ch.codigo LIMIT  1)))) / 86400 ");
			sql.append("     ) :: INT ");
			sql.append("   ) < 0");
			sql.append(" )");
		}
		
		if (Uteis.isAtributoPreenchido(requisicao)) {
			sql.append(" and exists (select 1 from requisicaoitem where requisicaoitem.cotacao = cotacao.codigo and requisicaoitem.requisicao = ").append(requisicao).append(") ");
		}
		
		/*if(!isPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino(dataModelo.getUsuario())){
			sql.append(" and uni ");	
		}
		if(!isPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino(dataModelo.getUsuario()) 
				&& !isPermiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino(dataModelo.getUsuario())){
			sql.append(" and (departamento.codigo is null or departamento.codigo in (select fun) )");
		}
		if(!isPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino(dataModelo.getUsuario()) 
				&& !isPermiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino(dataModelo.getUsuario())
				&& !isPermiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento(dataModelo.getUsuario())){
			sql.append(" and (responsavelcotacaoHistorico.codigo = ").append(dataModelo.getUsuario().getCodigo()).append(" or responsavelcotacaoHistorico.nome is null)");
		}*/
	}

	public List consultarPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), true, usuario);
		String sqlStr = "";
		if (valorConsulta.intValue() == 0) {
			sqlStr = "SELECT * FROM Cotacao WHERE codigo >= " + valorConsulta.intValue();
		} else {
			sqlStr = "SELECT * FROM Cotacao WHERE codigo = " + valorConsulta.intValue();
		}
		sqlStr += " AND Cotacao.dataCotacao >= '" + Uteis.getDataJDBC(dataIni) + " 00:00:00.000' ";
		sqlStr += " AND Cotacao.dataCotacao <= '" + Uteis.getDataJDBC(dataFim) + " 23:59:59.999' ";
		sqlStr += " ORDER BY codigo";

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoCotacao(Integer cotacao, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sqlStr = "SELECT unidadeEnsino.codigo as codigo, unidadeEnsino.nome as nome FROM ItemCotacaoUnidadeEnsino " + " left join Cotacao on Cotacao.codigo = ItemCotacaoUnidadeEnsino.cotacao" + " left join UnidadeEnsino on UnidadeEnsino.codigo = ItemCotacaoUnidadeEnsino.unidadeEnsino" + " WHERE Cotacao.codigo = " + cotacao.intValue() + " " + " group by unidadeEnsino.codigo, unidadeEnsino.nome, ItemCotacaoUnidadeEnsino.unidadeEnsino, ItemCotacaoUnidadeEnsino.cotacao  " + " ORDER BY ItemCotacaoUnidadeEnsino.unidadeEnsino";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
		List<UnidadeEnsinoVO> objs = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			UnidadeEnsinoVO obj = new UnidadeEnsinoVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNome(tabelaResultado.getString("nome"));
			objs.add(obj);
		}
		return objs;
	}

	public List<CotacaoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, UsuarioVO usuario) {
		List<CotacaoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDadosBasica(tabelaResultado, usuario));
		}
		return vetResultado;
	}

	public CotacaoVO montarDadosBasica(SqlRowSet dadosSQL, UsuarioVO usuario) {
		CotacaoVO obj = new CotacaoVO();
		obj.setCodigo((dadosSQL.getInt("cotacao.codigo")));
		obj.setDataCotacao(dadosSQL.getTimestamp("cotacao.dataCotacao"));
		obj.setSituacao(dadosSQL.getString("cotacao.situacao"));
		obj.setMotivoRevisao(dadosSQL.getString("cotacao.motivoRevisao"));

		obj.getResponsavelCotacao().setCodigo((dadosSQL.getInt("responsavel.codigo")));
		obj.getResponsavelCotacao().setNome((dadosSQL.getString("responsavel.nome")));

		obj.getResponsavelAutorizacao().setCodigo((dadosSQL.getInt("responsavelAutorizacao.codigo")));
		obj.getResponsavelAutorizacao().setNome((dadosSQL.getString("responsavelAutorizacao.nome")));

		obj.getCategoriaProduto().setCodigo((dadosSQL.getInt("categoriaProduto.codigo")));
		obj.getCategoriaProduto().setNome(dadosSQL.getString("categoriaProduto.nome"));

		obj.getTramiteCotacaoCompra().setCodigo((dadosSQL.getInt("tramite.codigo")));
		obj.getTramiteCotacaoCompra().setNome(dadosSQL.getString("tramite.nome"));
		
		obj.getAtualCotacaoHistoricoVO().setCodigo((dadosSQL.getInt("cotacaoHistorico.codigo")));
		obj.getAtualCotacaoHistoricoVO().setResponsavel(new UsuarioVO());
		obj.getAtualCotacaoHistoricoVO().getResponsavel().setCodigo((dadosSQL.getInt("responsavelcotacaoHistorico.codigo")));
		obj.getAtualCotacaoHistoricoVO().getResponsavel().setNome((dadosSQL.getString("responsavelcotacaoHistorico.nome")));
		obj.getAtualCotacaoHistoricoVO().setDepartamentoTramiteCotacaoCompra(new DepartamentoTramiteCotacaoCompraVO());
		obj.getAtualCotacaoHistoricoVO().getDepartamentoTramiteCotacaoCompra().setCodigo((dadosSQL.getInt("departamentotramitehistorico.codigo")));
		obj.getAtualCotacaoHistoricoVO().getDepartamentoTramiteCotacaoCompra().getDepartamentoVO().setCodigo((dadosSQL.getInt("departamento.codigo")));
		obj.getAtualCotacaoHistoricoVO().getDepartamentoTramiteCotacaoCompra().getDepartamentoVO().setNome((dadosSQL.getString("departamento.nome")));
		obj.getUnidadeEnsinoResponsavelTramitacao().setCodigo(dadosSQL.getInt("cotacao.unidadeEnsinoResponsavelTramitacao"));

		return obj;
	}

	public List<CotacaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		List<CotacaoVO> vetResultado = new ArrayList<>(0);
		while (tabelaResultado.next()) {
			vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
		}
		return vetResultado;
	}

	public CotacaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		CotacaoVO obj = new CotacaoVO();
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("codigo")));
		obj.setDataCotacao(dadosSQL.getTimestamp("dataCotacao"));
		obj.setSituacao(dadosSQL.getString("situacao"));
		obj.setMotivoRevisao(dadosSQL.getString("motivoRevisao"));
		obj.getResponsavelAutorizacao().setCodigo((dadosSQL.getInt("responsavelAutorizacao")));
		obj.getResponsavelCotacao().setCodigo((dadosSQL.getInt("responsavelCotacao")));
		obj.getCategoriaProduto().setCodigo((dadosSQL.getInt("categoriaProduto")));
		obj.setDataAutorizacao(dadosSQL.getTimestamp("dataAutorizacao"));
		obj.getUnidadeEnsinoResponsavelTramitacao().setCodigo(dadosSQL.getInt("unidadeEnsinoResponsavelTramitacao"));
		obj.getTramiteCotacaoCompra().setCodigo(dadosSQL.getInt("tramiteCotacaoCompra"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoNivelCentroResultadoEnum"))){
			obj.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.valueOf(dadosSQL.getString("tipoNivelCentroResultadoEnum")));	
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
			montarDadosResponsavelCotacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			return obj;
		}
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
			montarDadosResponsavelCotacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			montarDadosCategoriaProduto(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			return obj;
		}
		obj.setCotacaoFornecedorVOs(getFacadeFactory().getCotacaoFornecedorFacade().consultarCotacaoFornecedors(obj.getCodigo(), nivelMontarDados, usuario));
		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
			return obj;
		}
		obj.getCategoriaDespesa().setCodigo((dadosSQL.getInt("categoriadespesa")));
		obj.setCursoCategoriaDespesa(Uteis.montarDadosVO(dadosSQL.getInt("cursoCategoriaDespesa"), CursoVO.class, p -> getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(p, nivelMontarDados, false, usuario)));
		obj.setTurmaCategoriaDespesa(Uteis.montarDadosVO(dadosSQL.getInt("turmaCategoriaDespesa"), TurmaVO.class, p -> getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(p, nivelMontarDados, usuario)));
		obj.setTurnoCategoriaDespesa(Uteis.montarDadosVO(dadosSQL.getInt("turnoCategoriaDespesa"), TurnoVO.class, p -> getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(p, nivelMontarDados, usuario)));
		obj.setUnidadeEnsinoCategoriaDespesa(Uteis.montarDadosVO(dadosSQL.getInt("unidadeEnsinoCategoriaDespesa"), UnidadeEnsinoVO.class, p -> getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(p, false, nivelMontarDados, usuario)));
		obj.setDepartamentoCategoriaDespesa(Uteis.montarDadosVO(dadosSQL.getInt("departamentoCategoriaDespesa"), DepartamentoVO.class, p -> getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(p, false, nivelMontarDados, usuario)));
		montarDadosCategoriaDespesa(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosResponsavelCotacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosResponsavelAutorizacao(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		montarDadosCategoriaProduto(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
		return obj;
	}

	public static void montarDadosCategoriaDespesa(CotacaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCategoriaDespesa().getCodigo().intValue() == 0) {
			obj.setCategoriaDespesa(new CategoriaDespesaVO());
			return;
		}
		obj.setCategoriaDespesa(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCategoriaDespesa().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosResponsavelAutorizacao(CotacaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelAutorizacao().getCodigo().intValue() == 0) {
			obj.setResponsavelAutorizacao(new UsuarioVO());
			return;
		}
		obj.setResponsavelAutorizacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelAutorizacao().getCodigo(), nivelMontarDados, usuario));
	}

	public static void montarDadosCategoriaProduto(CotacaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getCategoriaProduto().getCodigo().intValue() == 0) {
			obj.setCategoriaProduto(new CategoriaProdutoVO());
			return;
		}
		obj.setCategoriaProduto(getFacadeFactory().getCategoriaProdutoFacade().consultarPorChavePrimaria(obj.getCategoriaProduto().getCodigo(), false, nivelMontarDados, usuario));
	}

	public static void montarDadosResponsavelCotacao(CotacaoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		if (obj.getResponsavelCotacao().getCodigo().intValue() == 0) {
			obj.setResponsavelCotacao(new UsuarioVO());
			return;
		}
		obj.setResponsavelCotacao(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavelCotacao().getCodigo(), nivelMontarDados, usuario));
	}

	public CotacaoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		String sql = "SELECT * FROM Cotacao WHERE codigo = ?";
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
		if (!tabelaResultado.next()) {
			throw new ConsistirException("Dados Não Encontrados ( Cotacao ).");
		}
		return (montarDados(tabelaResultado, nivelMontarDados, usuario));
	}

	@Override
	public CotacaoVO consultarCompletaPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sql = new StringBuilder(getSqlConsultaCompleta());
		sql.append(" WHERE Cotacao.codigo = ?");
		sql.append(" order by fornecedor.nome, produtoservico.nome ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoPrm);
		return (montarDadosConsultaCompleta(tabelaResultado, usuario));
	}

	private String getSqlConsultaCompleta() {
		StringBuilder sql = new StringBuilder("SELECT ");
		sql.append(" cotacao.codigo as \"cotacao.codigo\", cotacao.dataCotacao as \"cotacao.dataCotacao\", cotacao.dataAutorizacao as \"cotacao.dataAutorizacao\",  ");
		sql.append(" cotacao.motivoRevisao as \"cotacao.motivoRevisao\", cotacao.situacao as \"cotacao.situacao\", cotacao.tipoNivelCentroResultadoEnum as \"cotacao.tipoNivelCentroResultadoEnum\", ");

		sql.append(" responsavelCotacao.codigo as \"responsavelCotacao.codigo\", responsavelCotacao.nome as \"responsavelCotacao.nome\", ");

		sql.append(" responsavelAutorizacao.codigo as \"responsavelAutorizacao.codigo\", responsavelAutorizacao.nome as \"responsavelAutorizacao.nome\", ");

		sql.append(" categoriaProduto.codigo as \"categoriaProduto.codigo\", categoriaProduto.nome as \"categoriaProduto.nome\", ");
		sql.append(" tramiteCategoriaProduto.codigo as \"tramiteCategoriaProduto.codigo\", tramiteCategoriaProduto.nome as \"tramiteCategoriaProduto.nome\", ");

		sql.append(" unidadeEnsinoResponsavelTramitacao.codigo as \"unidadeEnsinoResponsavelTramitacao.codigo\", unidadeEnsinoResponsavelTramitacao.nome as \"unidadeEnsinoResponsavelTramitacao.nome\", ");

		sql.append(" tramiteCotacaoCompra.codigo as \"tramiteCotacaoCompra.codigo\", tramiteCotacaoCompra.nome as \"tramiteCotacaoCompra.nome\", ");

		sql.append(" categoriaDespesa.codigo as \"categoriaDespesa.codigo\", categoriaDespesa.descricao as \"categoriaDespesa.descricao\",   ");
		sql.append(" categoriaDespesa.informarTurma as \"categoriaDespesa.informarTurma\", categoriaDespesa.nivelCategoriaDespesa as \"categoriaDespesa.nivelCategoriaDespesa\",   ");
		

		sql.append(" departamentoCategoriaDespesa.codigo as \"departamentoCategoriaDespesa.codigo\", departamentoCategoriaDespesa.nome as \"departamentoCategoriaDespesa.nome\", ");
		sql.append(" unidadeEnsinoCategoriaDespesa.codigo as \"unidadeEnsinoCategoriaDespesa.codigo\", unidadeEnsinoCategoriaDespesa.nome as \"unidadeEnsinoCategoriaDespesa.nome\", ");
		sql.append(" cursoCategoriaDespesa.codigo as \"cursoCategoriaDespesa.codigo\", cursoCategoriaDespesa.nome as \"cursoCategoriaDespesa.nome\",  ");
		sql.append(" turnoCategoriaDespesa.codigo as \"turnoCategoriaDespesa.codigo\", turnoCategoriaDespesa.nome as \"turnoCategoriaDespesa.nome\",  ");
		sql.append(" turmaCategoriaDespesa.codigo as \"turmaCategoriaDespesa.codigo\", turmaCategoriaDespesa.identificadorturma as \"turmaCategoriaDespesa.identificadorturma\",  ");
		sql.append(" centroresultadoadministrativo.codigo as \"centroresultadoadministrativo.codigo\", centroresultadoadministrativo.descricao as \"centroresultadoadministrativo.descricao\", centroresultadoadministrativo.identificadorCentroResultado as \"centroresultadoadministrativo.identificadorCentroResultado\",   ");

		sql.append(" cotacaofornecedor.codigo as \"cotacaofornecedor.codigo\", cotacaofornecedor.tipocriacaocontapagar as \"cotacaofornecedor.tipocriacaocontapagar\", cotacaofornecedor.valortotal as \"cotacaofornecedor.valortotal\", cotacaofornecedor.dataPrevisaoEntrega as \"cotacaofornecedor.dataPrevisaoEntrega\", ");
		sql.append(" fornecedor.codigo as  \"fornecedor.codigo\", fornecedor.nome as  \"fornecedor.nome\", fornecedor.telComercial1 as \"fornecedor.telComercial1\", ");
		sql.append(" fornecedor.telComercial2 as  \"fornecedor.telComercial2\", fornecedor.telComercial3 as \"fornecedor.telComercial3\", ");
		sql.append(" fornecedor.email as  \"fornecedor.email\", fornecedor.email as \"fornecedor.email\", fornecedor.fax as \"fornecedor.fax\", ");
		sql.append(" fornecedor.email as  \"fornecedor.email\", fornecedor.email as \"fornecedor.email\", fornecedor.fax as \"fornecedor.fax\", ");
		sql.append(" fornecedor.endereco as  \"fornecedor.endereco\", fornecedor.setor as \"fornecedor.setor\", fornecedor.fax as \"fornecedor.fax\", ");
		sql.append(" fornecedor.cnpj as  \"fornecedor.cnpj\", fornecedor.cpf as \"fornecedor.cpf\", fornecedor.tipoEmpresa as \"fornecedor.tipoEmpresa\", ");
		sql.append(" cidade.codigo as  \"cidade.codigo\", cidade.nome as  \"cidade.nome\",  ");		
		sql.append(" estado.codigo as  \"estado.codigo\", estado.nome as  \"estado.nome\",  ");
		
		
		sql.append(" formaPagamento.codigo as  \"formaPagamento.codigo\", formaPagamento.nome as  \"formaPagamento.nome\",  ");
		sql.append(" condicaoPagamento.codigo as  \"condicaoPagamento.codigo\",  condicaoPagamento.nome as  \"condicaoPagamento.nome\", ");

		sql.append(" itemCotacao.codigo as \"itemCotacao.codigo\", itemCotacao.compraautorizadafornecedor as \"itemCotacao.compraautorizadafornecedor\", itemCotacao.quantidade as \"itemCotacao.quantidade\", ");
		sql.append(" itemCotacao.precoanterior as \"itemCotacao.precoanterior\", itemCotacao.precounitario as \"itemCotacao.precounitario\", itemCotacao.precototal as \"itemCotacao.precototal\", ");
		sql.append(" produtoservico.codigo as \"produtoservico.codigo\", produtoservico.nome as \"produtoservico.nome\", ");
		sql.append(" produtoservico.controlarEstoque as \"produtoservico.controlarEstoque\",  produtoservico.tipoProdutoServico as \"produtoservico.tipoProdutoServico\",");
		sql.append(" produtoservico.exigeCompraCotacao as \"produtoservico.exigeCompraCotacao\", produtoservico.valorUnitario as \"produtoservico.valorUnitario\", ");
		sql.append(" produtoservico.valorUltimaCompra as \"produtoservico.valorUltimaCompra\", produtoservico.categoriaProduto as \"produtoservico.categoriaProduto\", ");
		sql.append(" unidadeMedida.codigo as \"unidadeMedida.codigo\", unidadeMedida.nome as \"unidadeMedida.nome\", ");

		sql.append(" ItemCotacaoUnidadeEnsino.codigo as \"ItemCotacaoUnidadeEnsino.codigo\", ItemCotacaoUnidadeEnsino.qtdadicional as \"ItemCotacaoUnidadeEnsino.qtdadicional\",  ");
		sql.append(" unidadeensinoItemCotacao.codigo as \"unidadeensinoItemCotacao.codigo\", unidadeensinoItemCotacao.nome as \"unidadeensinoItemCotacao.nome\"  ");

		sql.append(" from Cotacao ");
		sql.append(" inner join categoriaProduto on categoriaProduto.codigo = cotacao.categoriaProduto ");
		sql.append(" left join tramite as tramiteCategoriaProduto  on tramiteCategoriaProduto.codigo = categoriaProduto.tramiteCotacaoCompra  ");
		sql.append(" inner join usuario as responsavelCotacao on responsavelCotacao.codigo = cotacao.responsavelCotacao ");
		sql.append(" inner join cotacaofornecedor on cotacaofornecedor.cotacao = cotacao.codigo ");
		sql.append(" inner join fornecedor on fornecedor.codigo = CotacaoFornecedor.fornecedor");
		sql.append(" left join cidade on fornecedor.cidade = cidade.codigo");
		sql.append(" left join estado on cidade.estado = estado.codigo");
		sql.append(" left join FormaPagamento on FormaPagamento.codigo = CotacaoFornecedor.FormaPagamento");
		sql.append(" left join CondicaoPagamento on CondicaoPagamento.codigo = CotacaoFornecedor.CondicaoPagamento");
		sql.append(" inner join itemcotacao on itemcotacao.cotacaofornecedor = cotacaofornecedor.codigo ");
		sql.append(" inner join ItemCotacaoUnidadeEnsino on ItemCotacaoUnidadeEnsino.itemcotacao = itemcotacao.codigo ");
		sql.append(" inner join unidadeensino as unidadeensinoItemCotacao on unidadeensinoItemCotacao.codigo = ItemCotacaoUnidadeEnsino.unidadeEnsino ");
		sql.append(" inner join produtoservico on itemcotacao.produto = produtoservico.codigo ");
		sql.append(" left join unidademedida on produtoservico.unidademedida = unidademedida.codigo ");

		sql.append(" left join usuario as responsavelAutorizacao on responsavelAutorizacao.codigo = cotacao.responsavelAutorizacao ");
		sql.append(" left join unidadeensino as unidadeEnsinoResponsavelTramitacao on unidadeEnsinoResponsavelTramitacao.codigo = cotacao.unidadeEnsinoResponsavelTramitacao ");
		sql.append(" left join tramite as tramiteCotacaoCompra  on tramiteCotacaoCompra.codigo = cotacao.tramiteCotacaoCompra ");
		sql.append(" left join categoriaDespesa on categoriaDespesa.codigo = cotacao.categoriaDespesa ");
		sql.append(" left join unidadeensino as unidadeEnsinoCategoriaDespesa on unidadeEnsinoCategoriaDespesa.codigo = cotacao.unidadeEnsinoCategoriaDespesa ");
		sql.append(" left join departamento as departamentoCategoriaDespesa on departamentoCategoriaDespesa.codigo = cotacao.departamentoCategoriaDespesa ");
		sql.append(" left join curso as cursoCategoriaDespesa on cursoCategoriaDespesa.codigo = cotacao.cursoCategoriaDespesa ");
		sql.append(" left join turno as turnoCategoriaDespesa on turnoCategoriaDespesa.codigo = cotacao.turnoCategoriaDespesa ");
		sql.append(" left join turma as turmaCategoriaDespesa on turmaCategoriaDespesa.codigo = cotacao.turmaCategoriaDespesa ");
		sql.append(" LEFT JOIN centroresultado AS centroresultadoadministrativo ON cotacao.centroresultadoadministrativo = centroresultadoadministrativo.codigo ");
		return sql.toString();
	}

	public CotacaoVO montarDadosConsultaCompleta(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
		CotacaoVO obj = new CotacaoVO();
		Map<String, List<RequisicaoItemVO>> mapa = new HashMap<>();
		while (dadosSQL.next()) {
			if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
				montarDadosCompletoCotacao(dadosSQL, obj, usuario);
			}

			CotacaoFornecedorVO cotacaoFornecedor = consultarCotacaoFornecedorVO(dadosSQL.getInt("cotacaofornecedor.codigo"), obj);
			if (!Uteis.isAtributoPreenchido(cotacaoFornecedor.getCodigo())) {
				montarDadosCompletoCotacaoFornecedor(dadosSQL, cotacaoFornecedor);
			}

			ItemCotacaoVO itemCotacao = consultarItemCotacaoVO(dadosSQL.getInt("itemCotacao.codigo"), cotacaoFornecedor);
			if (!Uteis.isAtributoPreenchido(itemCotacao.getCodigo())) {
				itemCotacao.setCotacao(obj);
				montarDadosCompletoItemCotacao(dadosSQL, itemCotacao);
			}

			ItemCotacaoUnidadeEnsinoVO itemCotacaoUnidadeEnsino = consultarItemCotacaoUnidadeEnsinoVO(dadosSQL.getInt("ItemCotacaoUnidadeEnsino.codigo"), itemCotacao);
			itemCotacaoUnidadeEnsino.setNovoObj(Boolean.FALSE);
			itemCotacaoUnidadeEnsino.setCotacao(obj);
			itemCotacaoUnidadeEnsino.setCodigo((dadosSQL.getInt("ItemCotacaoUnidadeEnsino.codigo")));
			itemCotacaoUnidadeEnsino.setQtdAdicional((dadosSQL.getDouble("ItemCotacaoUnidadeEnsino.qtdAdicional")));
			itemCotacaoUnidadeEnsino.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeensinoItemCotacao.codigo"));
			itemCotacaoUnidadeEnsino.getUnidadeEnsinoVO().setNome(dadosSQL.getString("unidadeensinoItemCotacao.nome"));
			itemCotacaoUnidadeEnsino.getProdutoVO().setCodigo((dadosSQL.getInt("produtoservico.codigo")));
			itemCotacaoUnidadeEnsino.getProdutoVO().setNome(dadosSQL.getString("produtoservico.nome"));
			itemCotacaoUnidadeEnsino.getProdutoVO().setValorUnitario(dadosSQL.getDouble("produtoservico.valorUnitario"));
			itemCotacaoUnidadeEnsino.getProdutoVO().setValorUltimaCompra(dadosSQL.getDouble("produtoservico.valorUltimaCompra"));
			itemCotacaoUnidadeEnsino.getProdutoVO().setControlarEstoque(dadosSQL.getBoolean("produtoservico.controlarEstoque"));
			itemCotacaoUnidadeEnsino.getProdutoVO().setExigeCompraCotacao(dadosSQL.getBoolean("produtoservico.exigeCompraCotacao"));
			itemCotacaoUnidadeEnsino.getProdutoVO().getCategoriaProduto().setCodigo(dadosSQL.getInt("produtoservico.categoriaProduto"));
			itemCotacaoUnidadeEnsino.getProdutoVO().getUnidadeMedida().setCodigo(dadosSQL.getInt("unidadeMedida.codigo"));
			itemCotacaoUnidadeEnsino.getProdutoVO().getUnidadeMedida().setNome(dadosSQL.getString("unidadeMedida.nome"));
			montarDadosCompletoRequisicaoItem(itemCotacaoUnidadeEnsino, mapa, usuario);
			itemCotacao.getListaItemCotacaoUnidadeEnsinoVOs().add(itemCotacaoUnidadeEnsino);
			cotacaoFornecedor.adicionarObjItemCotacaoVOs(itemCotacao, false);
			adicionarObjCotacaoFornecedorVOs(obj, cotacaoFornecedor);
		}
		return obj;
	}

	private void montarDadosCompletoRequisicaoItem(ItemCotacaoUnidadeEnsinoVO itemCotacaoUnidadeEnsino, Map<String, List<RequisicaoItemVO>> mapa, UsuarioVO usuario) throws Exception {
		String chaveMapa = itemCotacaoUnidadeEnsino.getProdutoVO().getCodigo() + "-" + itemCotacaoUnidadeEnsino.getUnidadeEnsinoVO().getCodigo();
		if (!mapa.containsKey(chaveMapa)) {
			List<RequisicaoItemVO> lista = getFacadeFactory().getRequisicaoItemFacade().consultarRequisicaoItemsPorItemCotacaoUnidadeEnsinoVO(null, itemCotacaoUnidadeEnsino, Uteis.NIVELMONTARDADOS_TODOS, usuario);
			mapa.put(chaveMapa, lista);
		}
		itemCotacaoUnidadeEnsino.setListaRequisicaoItemVOs(mapa.get(chaveMapa));
	}

	private void montarDadosCompletoItemCotacao(SqlRowSet dadosSQL, ItemCotacaoVO itemCotacao) {
		itemCotacao.setNovoObj(Boolean.FALSE);
		itemCotacao.setCodigo((dadosSQL.getInt("itemCotacao.codigo")));
		itemCotacao.setPrecoUnitario((dadosSQL.getDouble("itemCotacao.precoUnitario")));
		itemCotacao.setPrecoAnterior((dadosSQL.getDouble("itemCotacao.precoAnterior")));
		itemCotacao.setQuantidade((dadosSQL.getDouble("itemCotacao.quantidade")));
		itemCotacao.setCompraAutorizadaFornecedor(dadosSQL.getBoolean("itemCotacao.compraAutorizadaFornecedor"));
		itemCotacao.getProduto().setCodigo((dadosSQL.getInt("produtoservico.codigo")));
		itemCotacao.getProduto().setNome(dadosSQL.getString("produtoservico.nome"));
		itemCotacao.getProduto().setTipoProdutoServicoEnum(TipoProdutoServicoEnum.valueOf(dadosSQL.getString("produtoservico.tipoProdutoServico")));
		itemCotacao.getProduto().setValorUnitario(dadosSQL.getDouble("produtoservico.valorUnitario"));
		itemCotacao.getProduto().setValorUltimaCompra(dadosSQL.getDouble("produtoservico.valorUltimaCompra"));
		itemCotacao.getProduto().setControlarEstoque(dadosSQL.getBoolean("produtoservico.controlarEstoque"));
		itemCotacao.getProduto().setExigeCompraCotacao(dadosSQL.getBoolean("produtoservico.exigeCompraCotacao"));
		itemCotacao.getProduto().getCategoriaProduto().setCodigo(dadosSQL.getInt("produtoservico.categoriaProduto"));
		itemCotacao.getProduto().getUnidadeMedida().setCodigo(dadosSQL.getInt("unidadeMedida.codigo"));
		itemCotacao.getProduto().getUnidadeMedida().setNome(dadosSQL.getString("unidadeMedida.nome"));
	}

	private void montarDadosCompletoCotacaoFornecedor(SqlRowSet dadosSQL, CotacaoFornecedorVO cotacaoFornecedor) {
		cotacaoFornecedor.setNovoObj(Boolean.FALSE);
		cotacaoFornecedor.setCodigo((dadosSQL.getInt("cotacaofornecedor.codigo")));
		cotacaoFornecedor.setValorTotal((dadosSQL.getDouble("cotacaofornecedor.valorTotal")));
		cotacaoFornecedor.setTipoCriacaoContaPagarEnum(TipoCriacaoContaPagarEnum.valueOf(dadosSQL.getString("cotacaofornecedor.tipoCriacaoContaPagar")));
		cotacaoFornecedor.setDataPrevisaoEntrega(dadosSQL.getDate("cotacaofornecedor.dataPrevisaoEntrega"));

		cotacaoFornecedor.getFormaPagamento().setCodigo((dadosSQL.getInt("formaPagamento.codigo")));
		cotacaoFornecedor.getFormaPagamento().setNome((dadosSQL.getString("formaPagamento.nome")));

		cotacaoFornecedor.getCondicaoPagamento().setCodigo((dadosSQL.getInt("condicaoPagamento.codigo")));
		cotacaoFornecedor.getCondicaoPagamento().setNome((dadosSQL.getString("condicaoPagamento.nome")));

		cotacaoFornecedor.getFornecedor().setCodigo((dadosSQL.getInt("fornecedor.codigo")));
		cotacaoFornecedor.getFornecedor().setCNPJ((dadosSQL.getString("fornecedor.cnpj")));
		cotacaoFornecedor.getFornecedor().setCPF((dadosSQL.getString("fornecedor.cpf")));
		cotacaoFornecedor.getFornecedor().setNome((dadosSQL.getString("fornecedor.nome")));
		cotacaoFornecedor.getFornecedor().setFax((dadosSQL.getString("fornecedor.fax")));
		cotacaoFornecedor.getFornecedor().setEmail((dadosSQL.getString("fornecedor.email")));
		cotacaoFornecedor.getFornecedor().setTelComercial1((dadosSQL.getString("fornecedor.telComercial1")));
		cotacaoFornecedor.getFornecedor().setTelComercial2((dadosSQL.getString("fornecedor.telComercial2")));
		cotacaoFornecedor.getFornecedor().setTelComercial3((dadosSQL.getString("fornecedor.telComercial3")));
		cotacaoFornecedor.getFornecedor().setEndereco((dadosSQL.getString("fornecedor.endereco")));
		cotacaoFornecedor.getFornecedor().setSetor((dadosSQL.getString("fornecedor.setor")));
		cotacaoFornecedor.getFornecedor().setSetor((dadosSQL.getString("fornecedor.setor")));
		cotacaoFornecedor.getFornecedor().getCidade().setCodigo(dadosSQL.getInt("cidade.codigo"));
		cotacaoFornecedor.getFornecedor().getCidade().setNome(dadosSQL.getString("cidade.nome"));
		cotacaoFornecedor.getFornecedor().getCidade().getEstado().setCodigo(dadosSQL.getInt("estado.codigo"));
		cotacaoFornecedor.getFornecedor().getCidade().getEstado().setNome(dadosSQL.getString("estado.nome"));
		cotacaoFornecedor.getFornecedor().setTipoEmpresa(dadosSQL.getString("fornecedor.tipoEmpresa"));
	}

	private void montarDadosCompletoCotacao(SqlRowSet dadosSQL, CotacaoVO obj, UsuarioVO usuario) {
		obj.setNovoObj(Boolean.FALSE);
		obj.setCodigo((dadosSQL.getInt("cotacao.codigo")));
		obj.setDataCotacao(dadosSQL.getTimestamp("cotacao.dataCotacao"));
		obj.setSituacao(dadosSQL.getString("cotacao.situacao"));
		obj.setMotivoRevisao(dadosSQL.getString("cotacao.motivoRevisao"));
		obj.setDataAutorizacao(dadosSQL.getTimestamp("cotacao.dataAutorizacao"));
		if(Uteis.isAtributoPreenchido(dadosSQL.getString("cotacao.tipoNivelCentroResultadoEnum"))){
			obj.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.valueOf(dadosSQL.getString("cotacao.tipoNivelCentroResultadoEnum")));	
		}
		obj.getResponsavelCotacao().setCodigo((dadosSQL.getInt("responsavelCotacao.codigo")));
		obj.getResponsavelCotacao().setNome((dadosSQL.getString("responsavelCotacao.nome")));

		obj.getResponsavelAutorizacao().setCodigo((dadosSQL.getInt("responsavelAutorizacao.codigo")));
		obj.getResponsavelAutorizacao().setNome((dadosSQL.getString("responsavelAutorizacao.nome")));

		obj.getCategoriaProduto().setCodigo((dadosSQL.getInt("categoriaProduto.codigo")));
		obj.getCategoriaProduto().setNome(dadosSQL.getString("categoriaProduto.nome"));

		obj.getCategoriaProduto().getTramiteCotacaoCompra().setCodigo(dadosSQL.getInt("tramiteCategoriaProduto.codigo"));
		obj.getCategoriaProduto().getTramiteCotacaoCompra().setNome(dadosSQL.getString("tramiteCategoriaProduto.nome"));

		obj.getUnidadeEnsinoResponsavelTramitacao().setCodigo(dadosSQL.getInt("unidadeEnsinoResponsavelTramitacao.codigo"));
		obj.getUnidadeEnsinoResponsavelTramitacao().setNome(dadosSQL.getString("unidadeEnsinoResponsavelTramitacao.nome"));

		obj.getTramiteCotacaoCompra().setCodigo(dadosSQL.getInt("tramiteCotacaoCompra.codigo"));
		obj.getTramiteCotacaoCompra().setNome(dadosSQL.getString("tramiteCotacaoCompra.nome"));

		obj.getCategoriaDespesa().setCodigo((dadosSQL.getInt("categoriaDespesa.codigo")));
		obj.getCategoriaDespesa().setDescricao((dadosSQL.getString("categoriaDespesa.descricao")));
		obj.getCategoriaDespesa().setInformarTurma((dadosSQL.getString("categoriaDespesa.informarTurma")));
		obj.getCategoriaDespesa().setNivelCategoriaDespesa((dadosSQL.getString("categoriaDespesa.nivelCategoriaDespesa")));

		obj.getCursoCategoriaDespesa().setCodigo((dadosSQL.getInt("cursoCategoriaDespesa.codigo")));
		obj.getCursoCategoriaDespesa().setNome((dadosSQL.getString("cursoCategoriaDespesa.nome")));

		obj.getTurnoCategoriaDespesa().setCodigo((dadosSQL.getInt("turnoCategoriaDespesa.codigo")));
		obj.getTurnoCategoriaDespesa().setNome((dadosSQL.getString("turnoCategoriaDespesa.nome")));

		obj.getTurmaCategoriaDespesa().setCodigo((dadosSQL.getInt("turmaCategoriaDespesa.codigo")));
		obj.getTurmaCategoriaDespesa().setIdentificadorTurma((dadosSQL.getString("turmaCategoriaDespesa.identificadorturma")));

		obj.getUnidadeEnsinoCategoriaDespesa().setCodigo((dadosSQL.getInt("unidadeEnsinoCategoriaDespesa.codigo")));
		obj.getUnidadeEnsinoCategoriaDespesa().setNome((dadosSQL.getString("unidadeEnsinoCategoriaDespesa.nome")));

		obj.getDepartamentoCategoriaDespesa().setCodigo((dadosSQL.getInt("departamentoCategoriaDespesa.codigo")));
		obj.getDepartamentoCategoriaDespesa().setNome((dadosSQL.getString("departamentoCategoriaDespesa.nome")));

		obj.getCentroResultadoAdministrativo().setCodigo((dadosSQL.getInt("centroResultadoAdministrativo.codigo")));
		obj.getCentroResultadoAdministrativo().setDescricao((dadosSQL.getString("centroResultadoAdministrativo.descricao")));
		obj.getCentroResultadoAdministrativo().setIdentificadorCentroResultado((dadosSQL.getString("centroResultadoAdministrativo.identificadorCentroResultado")));
		
		obj.setListaCentroResultadoOrigemVOs(getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().consultaRapidaPorCodOrigemPorTipoCentroResultadoOrigemEnum(obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.COTACAO, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		if (Uteis.isAtributoPreenchido(obj.getTramiteCotacaoCompra())) {
			obj.setListaCotacaoHistoricoVOs(getFacadeFactory().getCotacaoHistoricoInterfaceFacade().consultaRapidaPorCotacao(obj, usuario));
		}
	}

	private CotacaoFornecedorVO consultarCotacaoFornecedorVO(Integer codigoCotacaoFornecedor, CotacaoVO obj) {
		for (CotacaoFornecedorVO objsExistente : obj.getCotacaoFornecedorVOs()) {
			if (objsExistente.getCodigo().equals(codigoCotacaoFornecedor)) {
				return objsExistente;
			}
		}
		return new CotacaoFornecedorVO();
	}

	private ItemCotacaoVO consultarItemCotacaoVO(Integer codigoItemCotacao, CotacaoFornecedorVO obj) {
		for (ItemCotacaoVO objsExistente : obj.getItemCotacaoVOs()) {
			if (objsExistente.getCodigo().equals(codigoItemCotacao)) {
				return objsExistente;
			}
		}
		return new ItemCotacaoVO();
	}

	private ItemCotacaoUnidadeEnsinoVO consultarItemCotacaoUnidadeEnsinoVO(Integer codigoItemCotacaoUnidadeEnsino, ItemCotacaoVO obj) {
		for (ItemCotacaoUnidadeEnsinoVO objsExistente : obj.getListaItemCotacaoUnidadeEnsinoVOs()) {
			if (objsExistente.getCodigo().equals(codigoItemCotacaoUnidadeEnsino)) {
				return objsExistente;
			}
		}
		return new ItemCotacaoUnidadeEnsinoVO();
	}
	
	private boolean isPermiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino(UsuarioVO usuario) {
		try {
			MapaCotacao.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_CONSULTAR_MAPA_COTACOES_OUTROS_RESPONSAVEIS_TODAS_UNIDADE_ENSINO, usuario);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isPermiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino(UsuarioVO usuario) {
		try {
			MapaCotacao.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_CONSULTAR_MAPA_COTACOES_OUTROS_RESPONSAVEIS_MESMA_UNIDADE_ENSINO, usuario);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean isPermiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento(UsuarioVO usuario) {
		try {
			MapaCotacao.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_CONSULTAR_MAPA_COTACOES_OUTROS_RESPONSAVEIS_MESMO_DEPARTAMENTO, usuario);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getIdEntidade() {
		return Cotacao.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		Cotacao.idEntidade = idEntidade;
	}
	
	
	public EstatisticaCotacaoVO consultarEstatisticaCotacoesAtualizada(UsuarioVO usuario) throws Exception {
		EstatisticaCotacaoVO estatisticas = new EstatisticaCotacaoVO();
		Date hoje = new Date();
		ItemCronologicoEstatisticaVO hojeEstatistica = obterItemCronologicoEstatisticaCotacaoVO(hoje, "Hoje", usuario);
		estatisticas.getResumoCronologico().add(hojeEstatistica);
		ItemCronologicoEstatisticaVO anterioresEstatistica = obterItemCronologicoEstatisticaCotacaoVOAnterioresData(hoje, "Anteriores", usuario);
		estatisticas.getResumoCronologico().add(anterioresEstatistica);
		List<ItemSumarioUnidadeEstatisticaVO> sumario = obterItemSumarioUnidadeEstatisticaCotacaoVO(3, usuario);
		estatisticas.setSumarioPorUnidade(sumario);
		return estatisticas;
	}
	
	
	private ItemCronologicoEstatisticaVO obterItemCronologicoEstatisticaCotacaoVO(Date dataBusca, String descricao, UsuarioVO usuario) throws Exception {
		ItemCronologicoEstatisticaVO diaEstatistica = new ItemCronologicoEstatisticaVO();
		diaEstatistica.setDescricao(descricao);
		diaEstatistica.setDataInicial(Uteis.getDateSemHora(dataBusca));
		diaEstatistica.setDataFinal(Uteis.getDateSemHora(dataBusca));
		try {
			ControleAcesso.consultar(getIdEntidade(), false, usuario);
			String sql = "SELECT DATE_PART('YEAR',Cotacao.dataCotacao) as ano, " + "DATE_PART('MONTH',Cotacao.dataCotacao) as mes, " + "DATE_PART('DAY',Cotacao.dataCotacao) as dia, " + "COUNT(Cotacao.codigo) as nrCotacoes " + "FROM Cotacao  " + "WHERE (DATE_PART('YEAR',Cotacao.dataCotacao) = ?) and " + "(DATE_PART('MONTH',Cotacao.dataCotacao) = ?) and " + "(DATE_PART('DAY',Cotacao.dataCotacao) = ?) and situacao = 'AA' " + "GROUP BY ano, mes, dia " + "ORDER BY ano, mes, dia;";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { Uteis.getAnoData(dataBusca), Uteis.getMesData(dataBusca), Uteis.getDiaMesData(dataBusca) });

			if (!tabelaResultado.next()) {
				diaEstatistica.setQuantidade(0);
			} else {
				diaEstatistica.setQuantidade(tabelaResultado.getInt("nrCotacoes"));
			}
		} catch (Exception e) {
			diaEstatistica.setQuantidade(0);
		}
		return diaEstatistica;
	}

	
	private List<ItemSumarioUnidadeEstatisticaVO> obterItemSumarioUnidadeEstatisticaCotacaoVO(int nrUnidadeApresentar, UsuarioVO usuario) throws Exception {
		List<ItemSumarioUnidadeEstatisticaVO> resultado = new ArrayList<ItemSumarioUnidadeEstatisticaVO>(0);

		try {
			ControleAcesso.consultar(getIdEntidade(), false, usuario);
			String sql = "SELECT CategoriaProduto.codigo, CategoriaProduto.nome, COUNT(Cotacao.codigo) as nrCotacoes FROM Cotacao  " + "INNER JOIN CategoriaProduto ON (CategoriaProduto.codigo = Cotacao.categoriaProduto) and situacao = 'AA'	" + "GROUP BY CategoriaProduto.codigo, CategoriaProduto.nome " + "ORDER BY nrCotacoes DESC;";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
			while (tabelaResultado.next()) {
				ItemSumarioUnidadeEstatisticaVO sumario = new ItemSumarioUnidadeEstatisticaVO();
				sumario.setCodigo(tabelaResultado.getInt("codigo"));
				sumario.setNome(tabelaResultado.getString("nome"));
				sumario.setQuantidade(tabelaResultado.getInt("nrCotacoes"));
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
			// //System.out.println("Mapa Cotacao Erro:" + e.getMessage());
		}
		return resultado;
	}

	
	private ItemCronologicoEstatisticaVO obterItemCronologicoEstatisticaCotacaoVOAnterioresData(Date dataBusca, String descricao, UsuarioVO usuario) throws Exception {
		ItemCronologicoEstatisticaVO diaEstatistica = new ItemCronologicoEstatisticaVO();
		diaEstatistica.setDescricao(descricao);
		diaEstatistica.setDataInicial(Uteis.getDate("01/01/1900"));
		diaEstatistica.setDataFinal(Uteis.getDateSemHora(Uteis.obterDataFutura(dataBusca, -1)));
		dataBusca = Uteis.getDateSemHora(dataBusca);
		try {
			ControleAcesso.consultar(getIdEntidade(), false, usuario);
			String sql = "SELECT COUNT(Cotacao.codigo) as nrCotacoes  FROM Cotacao WHERE (Cotacao.dataCotacao < ?) and situacao = 'AA' ";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[] { Uteis.getDataJDBC(dataBusca) });
			if (!tabelaResultado.next()) {
				diaEstatistica.setQuantidade(0);
			} else {
				diaEstatistica.setQuantidade(tabelaResultado.getInt("nrCotacoes"));
			}
		} catch (Exception e) {
			diaEstatistica.setQuantidade(0);
		}
		return diaEstatistica;
	}
}
