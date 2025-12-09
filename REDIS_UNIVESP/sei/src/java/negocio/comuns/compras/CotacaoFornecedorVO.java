package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.compras.enumeradores.TipoCriacaoContaPagarEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class CotacaoFornecedorVO extends SuperVO {

	private Integer codigo;
	private CotacaoVO cotacao;
	private FornecedorVO fornecedor;
	private FormaPagamentoVO formaPagamento;
	private CondicaoPagamentoVO condicaoPagamento;
	private TipoCriacaoContaPagarEnum tipoCriacaoContaPagarEnum;
	private Double valorTotal;
	private List<ItemCotacaoVO> itemCotacaoVOs;
	private Date dataPrevisaoEntrega;
	public static final long serialVersionUID = 1L;
	/**
	 * Campos transient
	 */
	private List<CondicaoPagamentoCotacaoVO> listaCondicaoPagamento;

	public CotacaoFornecedorVO() {
		super();
		inicializarDados();
	}

	public static void validarDados(CotacaoFornecedorVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if ((obj.getFornecedor() == null) || (obj.getFornecedor().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo FORNECEDOR (Fornecedores da Cotação ) deve ser informado.");
		}

	}

	public static void validarDadosAutorizacao(CotacaoFornecedorVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if ((obj.getFormaPagamento() == null) || (obj.getFormaPagamento().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo FORMA DE PAGAMENTO do Fornecedor (" + obj.getFornecedor().getNome() + ") deve ser informado.");
		}
		if ((obj.getCondicaoPagamento() == null) || (obj.getCondicaoPagamento().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo CONDIÇÃO DE PAGAMENTO do Fornecedor (" + obj.getFornecedor().getNome() + ") deve ser informado.");
		}
	}

	public void inicializarDados() {
		setCodigo(0);
		setValorTotal(0.0);

	}

	public void atualizarObjItemCotacaoVOs(ItemCotacaoVO obj) {
		for (ItemCotacaoVO objExistente : getItemCotacaoVOs()) {
			if (!objExistente.getCotacaoFornecedor().getFornecedor().getCodigo().equals(obj.getCotacaoFornecedor().getFornecedor().getCodigo())
					&& objExistente.getProduto().getCodigo().equals(obj.getProduto().getCodigo())) {
				atualizarItemCotacaoUnidadeEnsino(obj, objExistente);
				return;
			}
		}
	}

	private void atualizarItemCotacaoUnidadeEnsino(ItemCotacaoVO obj, ItemCotacaoVO objExistente) {
		icueBusca:
		for (ItemCotacaoUnidadeEnsinoVO icueExistente : objExistente.getListaItemCotacaoUnidadeEnsinoVOs()) {
			for (ItemCotacaoUnidadeEnsinoVO icue : obj.getListaItemCotacaoUnidadeEnsinoVOs()) {
				if (icueExistente.getUnidadeEnsinoVO().getCodigo().equals(icue.getUnidadeEnsinoVO().getCodigo())) {
					icueExistente.setListaRequisicaoItemVOs(icue.getListaRequisicaoItemVOs());
					icueExistente.setQtdAdicional(icue.getQtdAdicional());
					continue icueBusca;
				}
			}
		}
	}

	public ItemCotacaoVO consultarObjItemCotacaoVOs(ItemCotacaoVO obj) {
		for (ItemCotacaoVO objExistente : getItemCotacaoVOs()) {
			if (objExistente.getProduto().getCodigo().equals(obj.getProduto().getCodigo())) {
				return objExistente;
			}
		}
		return new ItemCotacaoVO();
	}

	public void adicionarObjItemCotacaoVOs(ItemCotacaoVO obj, boolean validarAdicionarRequisicaoItem) {				
		obj.setCotacaoFornecedor(this);
		if(getItemCotacaoVOs().stream().anyMatch(i -> i.getProduto().getCodigo().equals(obj.getProduto().getCodigo())))	{
			if(validarAdicionarRequisicaoItem) {
				ItemCotacaoVO itemCotacaoVO = getItemCotacaoVOs().stream().filter(i -> i.getProduto().getCodigo().equals(obj.getProduto().getCodigo())).findFirst().get();
				for (ItemCotacaoUnidadeEnsinoVO itemCotacaoUnidadeEnsinoObj :  obj.getListaItemCotacaoUnidadeEnsinoVOs().stream().filter(u -> !u.getListaRequisicaoItemVOs().isEmpty()).collect(Collectors.toList())) {
					if(itemCotacaoVO.getListaItemCotacaoUnidadeEnsinoVOs().stream().anyMatch(u -> u.getUnidadeEnsinoVO().getCodigo().equals(itemCotacaoUnidadeEnsinoObj.getUnidadeEnsinoVO().getCodigo()))) {
						ItemCotacaoUnidadeEnsinoVO itemCotacaoUnidadeEnsinoObjExistente = itemCotacaoVO.getListaItemCotacaoUnidadeEnsinoVOs().stream().filter(u -> u.getUnidadeEnsinoVO().getCodigo().equals(itemCotacaoUnidadeEnsinoObj.getUnidadeEnsinoVO().getCodigo())).findFirst().get();
						for(RequisicaoItemVO requisicaoItemObj : itemCotacaoUnidadeEnsinoObj.getListaRequisicaoItemVOs()) {
							if(!itemCotacaoUnidadeEnsinoObjExistente.getListaRequisicaoItemVOs().stream().anyMatch(r -> r.getCodigo().equals(requisicaoItemObj.getCodigo()))) {
								itemCotacaoUnidadeEnsinoObjExistente.getListaRequisicaoItemVOs().add(requisicaoItemObj);
							}
						}
					}							
				}
			}
			return;	
			
		}
		getItemCotacaoVOs().add(obj);
	}

	public void excluirObjItemCotacaoVOs(Integer produto) {
		Iterator<ItemCotacaoVO> i = getItemCotacaoVOs().iterator();
		while (i.hasNext()) {
			ItemCotacaoVO objExistente = i.next();
			if (objExistente.getProduto().getCodigo().equals(produto)) {
				i.remove();
				return;
			}
		}
	}

	public void montarListaCondicaoPagamento(Date data, List<ParcelaCondicaoPagamentoVO> obj) {
		if (!Uteis.isAtributoPreenchido(obj)) {
			return;
		}
		getListaCondicaoPagamento().clear();
		for (ParcelaCondicaoPagamentoVO parcela : obj) {
			CondicaoPagamentoCotacaoVO condPgto = new CondicaoPagamentoCotacaoVO();
			condPgto.setNrParcela(parcela.getNumeroParcela());
			condPgto.setDataVencimento(Uteis.obterDataFutura(data, parcela.getIntervalo()));
			condPgto.setValorParcela(Uteis.arrendondarForcando2CadasDecimais(getValorTotalSelecionado() * (parcela.getPercentualValor() / 100)));
			getListaCondicaoPagamento().add(condPgto);
		}
	}

	public CondicaoPagamentoVO getCondicaoPagamento() {
		if (condicaoPagamento == null) {
			condicaoPagamento = new CondicaoPagamentoVO();
		}
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(CondicaoPagamentoVO condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	public FormaPagamentoVO getFormaPagamento() {
		if (formaPagamento == null) {
			formaPagamento = new FormaPagamentoVO();
		}
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamentoVO formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public FornecedorVO getFornecedor() {
		if (fornecedor == null) {
			fornecedor = new FornecedorVO();
		}
		return (fornecedor);
	}

	public void setFornecedor(FornecedorVO obj) {
		this.fornecedor = obj;
	}

	public Double getQuantidadeTotal() {
		return getItemCotacaoVOs().stream().mapToDouble(ItemCotacaoVO::getQtdTotalItemCotacaoUnidadeEnsino).sum();
	}
	
	public Double getQuantidadeTotalSelecionado() {
		return getItemCotacaoVOs()
				.stream()
				.filter(ItemCotacaoVO::getCompraAutorizadaFornecedor)
				.mapToDouble(ItemCotacaoVO::getQtdTotalItemCotacaoUnidadeEnsino)
				.sum();
	}
	
	public Double getValorTotal() {
		return getItemCotacaoVOs().stream().mapToDouble(ItemCotacaoVO::getPrecoTotal).sum();
	}
	
	public Double getValorTotalSelecionado() {
		return getItemCotacaoVOs()
				.stream()
				.filter(ItemCotacaoVO::getCompraAutorizadaFornecedor)
				.mapToDouble(ItemCotacaoVO::getPrecoTotal)
				.sum();
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public CotacaoVO getCotacao() {
		cotacao = Optional.ofNullable(cotacao).orElse(new CotacaoVO());
		return cotacao;
	}

	public void setCotacao(CotacaoVO cotacao) {
		this.cotacao = cotacao;
	}

	public Integer getCodigo() {
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public List<ItemCotacaoVO> getItemCotacaoVOs() {
		if (itemCotacaoVOs == null) {
			itemCotacaoVOs = new ArrayList<>();
		}
		return itemCotacaoVOs;
	}

	public void setItemCotacaoVOs(List<ItemCotacaoVO> itemCotacaoVOs) {
		this.itemCotacaoVOs = itemCotacaoVOs;
	}

	public List<CondicaoPagamentoCotacaoVO> getListaCondicaoPagamento() {
		if (listaCondicaoPagamento == null) {
			listaCondicaoPagamento = new ArrayList<>();
		}
		return listaCondicaoPagamento;
	}

	public void setListaCondicaoPagamento(List<CondicaoPagamentoCotacaoVO> listaCondicaoPagamento) {
		this.listaCondicaoPagamento = listaCondicaoPagamento;
	}

	public TipoCriacaoContaPagarEnum getTipoCriacaoContaPagarEnum() {
		if (tipoCriacaoContaPagarEnum == null) {
			tipoCriacaoContaPagarEnum = TipoCriacaoContaPagarEnum.COMPRA;
		}
		return tipoCriacaoContaPagarEnum;
	}

	public void setTipoCriacaoContaPagarEnum(TipoCriacaoContaPagarEnum tipoCriacaoContaPagarEnum) {
		this.tipoCriacaoContaPagarEnum = tipoCriacaoContaPagarEnum;
	}

	public Date getDataPrevisaoEntrega() {
		return dataPrevisaoEntrega;
	}

	public void setDataPrevisaoEntrega(Date dataPrevisaoEntrega) {
		this.dataPrevisaoEntrega = dataPrevisaoEntrega;
	}		

}
