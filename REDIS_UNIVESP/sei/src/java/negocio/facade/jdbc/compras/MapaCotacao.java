/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.compras;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.arquitetura.DataModelo;
import controle.compras.CotacaoControle.EnumSituacaoTramitacao;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoComprasEnum;
import negocio.comuns.compras.CompraItemVO;
import negocio.comuns.compras.CompraVO;
import negocio.comuns.compras.CotacaoFornecedorVO;
import negocio.comuns.compras.CotacaoRelVO;
import negocio.comuns.compras.CotacaoVO;
import negocio.comuns.compras.EstatisticaCotacaoVO;
import negocio.comuns.compras.ItemCotacaoVO;
import negocio.comuns.compras.ItemCronologicoEstatisticaVO;
import negocio.comuns.compras.ItemSumarioUnidadeEstatisticaVO;
import negocio.comuns.compras.MapaCotacaoVO;
import negocio.comuns.compras.ParcelaCondicaoPagamentoVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.MapaCotacaoInterfaceFacade;

/**
 *
 * @author Rodrigo
 */
@Repository
@Scope("singleton")
@Lazy
public class MapaCotacao extends ControleAcesso implements MapaCotacaoInterfaceFacade {

	private static final long serialVersionUID = 3462291244426484936L;
	public static String idEntidade;

	public MapaCotacao() throws Exception {
		super();
		setIdEntidade("MapaCotacao");
	}

	
	public void validarPermissao(UsuarioVO usuario) throws Exception {
		MapaCotacao.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.AUTORIZAR_COTACAO, usuario);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void aprovarCotacao(MapaCotacaoVO obj, String situacaoCotacaoAnterior, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception {
		try {
			validarPermissao(usuario);
			validarDadosParaAlterarSituacaoCotacao(obj, usuario);
			for (CompraVO compra : obj.getCompraVOs()) {
				compra.getResponsavel().setCodigo(usuario.getCodigo());				
				getFacadeFactory().getCompraFacade().incluir(compra, false, usuario);
			}			
			obj.getCotacaoVO().setResponsavelAutorizacao(usuario);
			obj.getCotacaoVO().setDataAutorizacao(new Date());
			getFacadeFactory().getCotacaoFacade().alterarSituacaoCotacao(obj.getCotacaoVO().getCodigo(), obj.getCotacaoVO().getSituacao(), obj.getCotacaoVO().getDataAutorizacao()	, obj.getCotacaoVO().getResponsavelAutorizacao(), "");
			getFacadeFactory().getCotacaoFornecedorFacade().alterarCotacaoFornecedors(obj.getCotacaoVO(), obj.getCotacaoVO().getCotacaoFornecedorVOs(), usuario);
			if (Uteis.isAtributoPreenchido(obj.getCotacaoHistorico())) {
				getFacadeFactory().getCotacaoHistoricoInterfaceFacade().finalizarTramiteCotacao(obj.getCotacaoHistorico(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS,usuario, conf);
			}
//			if(Uteis.isAtributoPreenchido(situacaoCotacaoAnterior) && situacaoCotacaoAnterior.equals("IN")){
//				getFacadeFactory().getRequisicaoFacade().atualizarSituacaoRequisicaoPorOperacaoEmCotacao(obj.getCotacaoVO(), usuario);	
//			}
		} catch (Exception e) {
			obj.getCotacaoVO().setResponsavelAutorizacao(new UsuarioVO());
			obj.getCotacaoVO().setDataAutorizacao(null);
			throw e;
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void indeferirCotacao(MapaCotacaoVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO conf) throws Exception {
		try {
			validarPermissao(usuario);
			validarDadosParaAlterarSituacaoCotacao(obj, usuario);
			getFacadeFactory().getCotacaoFacade().indeferirCotacao(obj.getCotacaoVO(), obj.getCotacaoHistorico(), usuario, conf);
		} catch (Exception e) {
			throw e;
		}
	}	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void revisarCotacao(MapaCotacaoVO obj, UsuarioVO usuario) throws Exception {
		try {
			validarPermissao(usuario);
			validarDadosParaAlterarSituacaoCotacao(obj, usuario);
			getFacadeFactory().getCotacaoFacade().alterarSituacaoCotacao(obj.getCotacaoVO().getCodigo(), obj.getCotacaoVO().getSituacao(), new Date(), usuario, obj.getCotacaoVO().getMotivoRevisao());
		} catch (Exception e) {
			throw e;
		}
	}	
	private void validarDadosParaAlterarSituacaoCotacao(MapaCotacaoVO obj, UsuarioVO usuario) throws Exception {
		Uteis.checkState((obj.getCotacaoVO().isIndeferido()||obj.getCotacaoVO().isRevisarCotacao()) && !Uteis.isAtributoPreenchido(obj.getCotacaoVO().getMotivoRevisao()), "O Campo Motivo Revisão deve ser informado.");
		if(getFacadeFactory().getCompraFacade().consultarSeExisteCompraPorCotacao(obj.getCotacaoVO().getCodigo(), null, false, usuario)){
			throw new StreamSeiException("Já existem compras dessa cotação. É necessário excluir as mesmas para realizar essa operação.");	
		}
	}

	@Override
	public List<CotacaoRelVO> getListaCotacaoRelatorio(MapaCotacaoVO mapaCotacaoVO, UsuarioVO usuario) throws Exception {
		List<CompraVO> compraVOs = mapaCotacaoVO.getCompraVOs();
		List<CotacaoRelVO> listaCotacaoRelatorio = new ArrayList<>();
		for (CompraVO compraVO : compraVOs) {
			for (CompraItemVO compraItemVO : compraVO.getCompraItemVOs()) {
				listaCotacaoRelatorio.add(new CotacaoRelVO(compraVO, mapaCotacaoVO.getCotacaoVO(), compraVO.getFornecedor(), compraItemVO.getProduto(), compraVO.getFormaPagamento().getNome(), compraVO.getUnidadeEnsino().getNome(), compraItemVO.getQuantidade().longValue(), BigDecimal.valueOf(compraItemVO.getPrecoUnitario()), BigDecimal.valueOf(compraItemVO.getPrecoTotal())));
			}
		}
		return listaCotacaoRelatorio;
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
			String sql = "SELECT DATE_PART('YEAR',Cotacao.dataCotacao) as ano, " + "DATE_PART('MONTH',Cotacao.dataCotacao) as mes, " + "DATE_PART('DAY',Cotacao.dataCotacao) as dia, " + "COUNT(MapaCotacao.codigo) as nrCotacoes " + "FROM MapaCotacao LEFT JOIN Cotacao ON (MapaCotacao.cotacao = Cotacao.codigo) " + "WHERE (DATE_PART('YEAR',Cotacao.dataCotacao) = ?) and " + "(DATE_PART('MONTH',Cotacao.dataCotacao) = ?) and " + "(DATE_PART('DAY',Cotacao.dataCotacao) = ?) " + "GROUP BY ano, mes, dia " + "ORDER BY ano, mes, dia;";
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
			String sql = "SELECT CategoriaProduto.codigo, CategoriaProduto.nome, COUNT(Cotacao.codigo) as nrCotacoes " + "FROM MapaCotacao INNER JOIN Cotacao ON (MapaCotacao.cotacao = Cotacao.codigo) " + "INNER JOIN CategoriaProduto ON (CategoriaProduto.codigo = Cotacao.categoriaProduto) " + "GROUP BY CategoriaProduto.codigo, CategoriaProduto.nome " + "ORDER BY nrCotacoes DESC;";
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
			String sql = "SELECT COUNT(MapaCotacao.codigo) as nrCotacoes " + "FROM MapaCotacao LEFT JOIN Cotacao ON (MapaCotacao.cotacao = Cotacao.codigo) " + "WHERE (Cotacao.dataCotacao < ?) ";
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

	@Override
	public void preencherDadosCompraCotacao(MapaCotacaoVO mapa, UsuarioVO usuario) throws Exception {
		gerarCompraCotacao(mapa, usuario);
		gerarCompraCotacaoRejeitada(mapa, usuario);
		for (CompraVO compra : mapa.getCompraVOs()) {
			getFacadeFactory().getCompraFacade().gerarCentroResultadoOrigemPorCompraItem(compra, usuario);
		}
	}

	private void gerarCompraCotacao(MapaCotacaoVO mapa, UsuarioVO usuario) throws Exception {
		final List<UnidadeEnsinoVO> listaUnidadeEnsinoCotacao = new ArrayList<>();
		listaUnidadeEnsinoCotacao.addAll(getFacadeFactory().getCotacaoFacade().consultarUnidadeEnsinoCotacao(mapa.getCotacaoVO().getCodigo(), false, usuario));
		mapa.getCotacaoVO().getCotacaoFornecedorVOs()
				.stream()
				.filter(p -> p.getItemCotacaoVOs().stream().anyMatch(ItemCotacaoVO::getCompraAutorizadaFornecedor))
				.forEach(p -> criarCompraPorUnidadeEnsino(mapa, p, listaUnidadeEnsinoCotacao, usuario));

	}

	public void gerarCompraCotacaoRejeitada(MapaCotacaoVO mapa, UsuarioVO usuario) throws Exception {
		for (CompraVO compraVencedora : mapa.getCompraVOs()) {
			for (CotacaoFornecedorVO fornecedor : mapa.getCotacaoVO().getCotacaoFornecedorVOs()) {
				if (!fornecedor.getFornecedor().getCodigo().equals(compraVencedora.getFornecedor().getCodigo())) {
					criarCompraRejeitadaPorUnidadeEnsino(mapa, fornecedor, usuario, compraVencedora);
				}
			}
			if (!mapa.getCompraRejeitadaTemporariaVOs().isEmpty()) {
				if (mapa.getHashCompraRejeitadaVOs().isEmpty()) {
					mapa.setCompraRejeitadaVOs(mapa.getCompraRejeitadaTemporariaVOs());
				}
				mapa.getHashCompraRejeitadaVOs().put(compraVencedora.getCodigoItensCompra(), mapa.getCompraRejeitadaTemporariaVOs());
				mapa.setCompraRejeitadaTemporariaVOs(new ArrayList<CompraVO>(0));
			}
		}
	}

	private void criarCompraPorUnidadeEnsino(MapaCotacaoVO mapa, CotacaoFornecedorVO obj, List<UnidadeEnsinoVO> listaUnidadeEnsinoCotacao, UsuarioVO usuario) {
		try {
			List<ParcelaCondicaoPagamentoVO> objs = getFacadeFactory().getParcelaCondicaoPagamentoFacade().consultarParcelaCondicaoPagamentos(obj.getCondicaoPagamento().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS);
			for (UnidadeEnsinoVO unidadeEnsino : listaUnidadeEnsinoCotacao) {
				CompraVO compra = new CompraVO();
				gerarItemCompra(mapa, compra, obj, unidadeEnsino);
				if (!compra.getCompraItemVOs().isEmpty()) {
					compra.getCotacao().setCodigo(mapa.getCotacaoVO().getCodigo());
					if(mapa.getCotacaoVO().isTramiteCotacaoCompraInformada()){
						compra.setUnidadeEnsino(mapa.getCotacaoVO().getUnidadeEnsinoResponsavelTramitacao());
					}else{
						compra.setUnidadeEnsino(unidadeEnsino);	
					}
					compra.setResponsavel(usuario);
					compra.setTipoCriacaoContaPagarEnum(obj.getTipoCriacaoContaPagarEnum());
					compra.setCategoriaProduto(mapa.getCotacaoVO().getCategoriaProduto());
					compra.setFornecedor(obj.getFornecedor());
					compra.setFormaPagamento(obj.getFormaPagamento());
					compra.setCondicaoPagamento(obj.getCondicaoPagamento());
					compra.setDataPrevisaoEntrega(obj.getDataPrevisaoEntrega());
					compra.montarListaCondicaoPagamento(objs);
					compra.montarCodigoItensCompra();
					adicionarCompra(mapa, compra);
				}
			}
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}

	}

	private void adicionarCompra(MapaCotacaoVO mapa, CompraVO compra) {
		int index = 0;
		for (CompraVO obj : mapa.getCompraVOs()) {
			if (obj.getUnidadeEnsino().getCodigo().equals(compra.getUnidadeEnsino().getCodigo())
					&& obj.getFornecedor().getCodigo().equals(compra.getFornecedor().getCodigo())) {
				mapa.getCompraVOs().set(index, compra);
				return;
			}
			index++;
		}
		mapa.getCompraVOs().add(compra);
	}

	private void gerarItemCompra(MapaCotacaoVO mapa, CompraVO compra, CotacaoFornecedorVO obj, UnidadeEnsinoVO unidadeEnsino) {
		for (ItemCotacaoVO item : obj.getItemCotacaoVOs()) {
			if (item.getCompraAutorizadaFornecedor()
					&& (item.getQtdTotalItemDaUnidadeEnsino(unidadeEnsino) > 0.0)) {
				CompraItemVO compraItem = new CompraItemVO();
				compraItem.setPrecoUnitario(item.getPrecoUnitario());
				compraItem.setProduto(item.getProduto());
				compraItem.setQuantidadeRequisicao(item.getQtdRequisicaoItemDaUnidadeEnsino(unidadeEnsino));
				compraItem.setListaRequisicaoItem(item.getListaItemCotacaoUnidadeEnsinoVOs().stream().filter(p -> p.getUnidadeEnsinoVO().getCodigo().equals(unidadeEnsino.getCodigo())).flatMap(p -> p.getListaRequisicaoItemVOs().stream()).collect(Collectors.toList()));
				compraItem.setQuantidadeAdicional(item.getQtdAdicionalItemDaUnidadeEnsino(unidadeEnsino));
				if (Uteis.isAtributoPreenchido(compraItem.getQuantidadeAdicional())) {
					compraItem.setCategoriaDespesa(mapa.getCotacaoVO().getCategoriaDespesa());
					compraItem.setDepartamentoVO(mapa.getCotacaoVO().getDepartamentoCategoriaDespesa());
					compraItem.setCursoVO(mapa.getCotacaoVO().getCursoCategoriaDespesa());
					compraItem.setTurnoVO(mapa.getCotacaoVO().getTurnoCategoriaDespesa());
					compraItem.setTurma(mapa.getCotacaoVO().getTurmaCategoriaDespesa());
					compraItem.setTipoNivelCentroResultadoEnum(mapa.getCotacaoVO().getTipoNivelCentroResultadoEnum());
					compraItem.setCentroResultadoAdministrativo(mapa.getCotacaoVO().getCentroResultadoAdministrativo());
				}
				compraItem.setCompra(compra);
				compra.getCompraItemVOs().add(compraItem);
			}
		}

	}

	private void criarCompraRejeitadaPorUnidadeEnsino(MapaCotacaoVO mapa, CotacaoFornecedorVO obj, UsuarioVO usuario, CompraVO compraVencedora) throws Exception {
		CompraVO compraRejeitada = new CompraVO();
		gerarItemCompraRejeitada(mapa, compraRejeitada, obj, compraVencedora);
		if (!compraRejeitada.getCompraItemVOs().isEmpty()) {
			compraRejeitada.setCotacao(mapa.getCotacaoVO());
			compraRejeitada.setUnidadeEnsino(compraVencedora.getUnidadeEnsino());
			compraRejeitada.setCategoriaProduto(mapa.getCotacaoVO().getCategoriaProduto());
			compraRejeitada.setResponsavel(usuario);
			compraRejeitada.setTipoCriacaoContaPagarEnum(obj.getTipoCriacaoContaPagarEnum());
			compraRejeitada.setFornecedor(obj.getFornecedor());
			compraRejeitada.setFormaPagamento(obj.getFormaPagamento());
			compraRejeitada.setCondicaoPagamento(obj.getCondicaoPagamento());
			adicionarCompraRejeitada(mapa, compraRejeitada);
		}
	}

	private void adicionarCompraRejeitada(MapaCotacaoVO mapa, CompraVO compraRejeitada) {
		int index = 0;
		for (CompraVO obj : mapa.getCompraRejeitadaTemporariaVOs()) {
			if (obj.getUnidadeEnsino().getCodigo().equals(compraRejeitada.getUnidadeEnsino().getCodigo()) && obj.getFornecedor().getCodigo().equals(compraRejeitada.getFornecedor().getCodigo())) {
				mapa.getCompraRejeitadaTemporariaVOs().set(index, compraRejeitada);
				return;
			}
			index++;
		}
		mapa.getCompraRejeitadaTemporariaVOs().add(compraRejeitada);
	}

	private void gerarItemCompraRejeitada(MapaCotacaoVO mapa, CompraVO compraRejeitada, CotacaoFornecedorVO obj, CompraVO compraVencedora) {
		for (CompraItemVO compraItemVencedoraVO : compraVencedora.getCompraItemVOs()) {
			obj.getItemCotacaoVOs()
					.stream()
					.filter(p -> compraItemVencedoraVO.getProduto().getCodigo().equals(p.getProduto().getCodigo()))
					.forEach(p -> {
						if (!p.getCompraAutorizadaFornecedor()) {
							CompraItemVO compraItem = new CompraItemVO();
							compraItem.setPrecoUnitario(p.getPrecoUnitario());
							compraItem.setProduto(p.getProduto());
							compraItem.setQuantidadeRequisicao(p.getQtdRequisicaoItemDaUnidadeEnsino(compraVencedora.getUnidadeEnsino()));
							compraItem.setListaRequisicaoItem(p.getListaItemCotacaoUnidadeEnsinoVOs().stream().filter(u -> u.getUnidadeEnsinoVO().getCodigo().equals(compraVencedora.getUnidadeEnsino().getCodigo())).flatMap(u -> u.getListaRequisicaoItemVOs().stream()).collect(Collectors.toList()));
							compraItem.setQuantidadeAdicional(p.getQtdAdicionalItemDaUnidadeEnsino(compraVencedora.getUnidadeEnsino()));							
							if (Uteis.isAtributoPreenchido(compraItem.getQuantidadeAdicional())) {
								compraItem.setCategoriaDespesa(mapa.getCotacaoVO().getCategoriaDespesa());
								compraItem.setCursoVO(mapa.getCotacaoVO().getCursoCategoriaDespesa());
								compraItem.setTurnoVO(mapa.getCotacaoVO().getTurnoCategoriaDespesa());
								compraItem.setTurma(mapa.getCotacaoVO().getTurmaCategoriaDespesa());
								compraItem.setDepartamentoVO(mapa.getCotacaoVO().getDepartamentoCategoriaDespesa());
								compraItem.setCentroResultadoAdministrativo(mapa.getCotacaoVO().getCentroResultadoAdministrativo());
								compraItem.setTipoNivelCentroResultadoEnum(mapa.getCotacaoVO().getTipoNivelCentroResultadoEnum());
							}
							compraItem.setCompra(compraRejeitada);
							compraRejeitada.getCompraItemVOs().add(compraItem);
						}
					});

		}
	}
	
	
	@Override
	public void consultar(CotacaoVO obj, Integer departamentoFiltro, String produtoFiltro, EnumSituacaoTramitacao enumSituacaoTramitacaoFiltro, DataModelo dataModelo, boolean permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino, boolean permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento) {
		try {
			ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
			dataModelo.setControlarAcesso(false);
			getFacadeFactory().getCotacaoFacade().consultar(obj, departamentoFiltro, produtoFiltro, null, enumSituacaoTramitacaoFiltro, dataModelo ,  permiteConsultarCotacoesOutrosResponsaveisTodasUnidadeEnsino,  permiteConsultarCotacoesOutrosResponsaveisMesmaUnidadeEnsino,  permiteConsultarCotacoesOutrosResponsaveisMesmoDepartamento);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	
	

	public static void setIdEntidade(String idEntidade) {
		MapaCotacao.idEntidade = idEntidade;
	}

	public static String getIdEntidade() {
		return MapaCotacao.idEntidade;
	}

}
