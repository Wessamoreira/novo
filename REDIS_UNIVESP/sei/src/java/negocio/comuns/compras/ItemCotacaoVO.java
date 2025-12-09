package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;

public class ItemCotacaoVO extends SuperVO implements Cloneable {

	private Integer codigo;
	private CotacaoFornecedorVO cotacaoFornecedor;
	private ProdutoServicoVO produto;
	private CotacaoVO cotacao;
	private Double precoUnitario;
	private Double precoAnterior;
	private Boolean compraAutorizadaFornecedor;
	private Boolean alterou;
	private String css;
	private List<ItemCotacaoUnidadeEnsinoVO> listaItemCotacaoUnidadeEnsinoVOs;
	private Double precoTotal;
	private Double quantidade;
	public static final long serialVersionUID = 1L;

	public ItemCotacaoVO() {
		super();
		inicializarDados();
	}

	public ItemCotacaoVO getClone() {
		ItemCotacaoVO clone = new ItemCotacaoVO();
		clone.setCotacaoFornecedor(new CotacaoFornecedorVO());
		clone.setProduto(getProduto());
		clone.setCotacao(getCotacao());
		for (ItemCotacaoUnidadeEnsinoVO icue : getListaItemCotacaoUnidadeEnsinoVOs()) {
			ItemCotacaoUnidadeEnsinoVO novo = icue.getClone();
			novo.setItemCotacao(clone);
			clone.getListaItemCotacaoUnidadeEnsinoVOs().add(novo);
		}
		return clone;
	}
	
	

	public static void validarDados(ItemCotacaoVO obj)  {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getCotacaoFornecedor()), "O campo COTAÇÃO FORNECEDOR (Itens da Cotação) deve ser informado.");
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getProduto()), "O campo Produto (Itens da Cotação) deve ser informado.");
	}

	

	public void adicionarObjItemCotacaoUnidadeEnsinoVO(ItemCotacaoUnidadeEnsinoVO obj) {
		int index = 0;
		Iterator<ItemCotacaoUnidadeEnsinoVO> i = getListaItemCotacaoUnidadeEnsinoVOs().iterator();
		while (i.hasNext()) {
			ItemCotacaoUnidadeEnsinoVO objExistente = i.next();
			if (objExistente.getUnidadeEnsinoVO().getCodigo().equals(obj.getUnidadeEnsinoVO().getCodigo())) {
				getListaItemCotacaoUnidadeEnsinoVOs().set(index, obj);
				return;
			}
			index++;
		}
		getListaItemCotacaoUnidadeEnsinoVOs().add(obj);
	}		

	
	public void inicializarDados() {
		setCodigo(0);
		setPrecoUnitario(0.0);
		css = "backgroud-color:transparent";
		setAlterou(false);
		setCompraAutorizadaFornecedor(Boolean.FALSE);
		setCotacao(new CotacaoVO());
		setPrecoTotal(0.0);
		setPrecoAnterior(0.0);
	}

	public Double getPrecoTotal() {
		return getPrecoUnitario() * getQuantidade();
	}

	public void setPrecoTotal(Double precoTotal) {
		this.precoTotal = precoTotal;
	}

	public ProdutoServicoVO getProduto() {
		if (produto == null) {
			produto = new ProdutoServicoVO();
		}
		return (produto);
	}

	public void setProduto(ProdutoServicoVO obj) {
		this.produto = obj;
	}	

	public CotacaoVO getCotacao() {
		cotacao = Optional.ofNullable(cotacao).orElse(new CotacaoVO());
		return cotacao;
	}

	public void setCotacao(CotacaoVO cotacao) {
		this.cotacao = cotacao;
	}

	public Boolean getCompraAutorizadaFornecedor() {
		if (compraAutorizadaFornecedor == null) {
			compraAutorizadaFornecedor = false;
		}
		return (compraAutorizadaFornecedor);
	}	

	public void setCompraAutorizadaFornecedor(Boolean compraAutorizadaFornecedor) {
		this.compraAutorizadaFornecedor = compraAutorizadaFornecedor;
	}

	public Double getQuantidade() {
		return getListaItemCotacaoUnidadeEnsinoVOs().stream().mapToDouble(ItemCotacaoUnidadeEnsinoVO::getTotalQtd).sum();
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public Double getPrecoUnitario() {
		if (precoUnitario == null) {
			precoUnitario = 0.0;
		}
		return (precoUnitario);
	}

	public void setPrecoUnitario(Double precoUnitario) {
		this.precoUnitario = precoUnitario;
		setPrecoTotal(getPrecoUnitario() * getQuantidade());
	}

	public CotacaoFornecedorVO getCotacaoFornecedor() {
		if (cotacaoFornecedor == null) {
			cotacaoFornecedor = new CotacaoFornecedorVO();
		}
		return (cotacaoFornecedor);
	}

	public void setCotacaoFornecedor(CotacaoFornecedorVO cotacaoFornecedor) {
		this.cotacaoFornecedor = cotacaoFornecedor;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}	

	public Double getPrecoAnterior() {
		if (precoAnterior == null) {
			precoAnterior = 0.0;
		}
		return precoAnterior;
	}

	public void setPrecoAnterior(Double precoAnterior) {
		this.precoAnterior = precoAnterior;
	}

	public List<ItemCotacaoUnidadeEnsinoVO> getListaItemCotacaoUnidadeEnsinoVOs() {
		if (listaItemCotacaoUnidadeEnsinoVOs == null) {
			listaItemCotacaoUnidadeEnsinoVOs = new ArrayList<>();
		}
		return listaItemCotacaoUnidadeEnsinoVOs;
	}

	public void setListaItemCotacaoUnidadeEnsinoVOs(List<ItemCotacaoUnidadeEnsinoVO> listaItemCotacaoUnidadeEnsinoVOs) {
		this.listaItemCotacaoUnidadeEnsinoVOs = listaItemCotacaoUnidadeEnsinoVOs;
	}

	public String getCss() {
		if (css == null) {
			css = "backgroud-color:transparent;";
		}
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public Boolean getAlterou() {
		if (alterou == null) {
			alterou = false;
		}
		return alterou;
	}

	public void setAlterou(Boolean alterou) {
		this.alterou = alterou;
	}

	public String getOrdenacao() {
		return getProduto().getNome();
	}

	public Double getQtdAdicionalItemDaUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		return getListaItemCotacaoUnidadeEnsinoVOs().stream().filter(p->p.getUnidadeEnsinoVO().getCodigo().equals(unidadeEnsino.getCodigo())).mapToDouble(ItemCotacaoUnidadeEnsinoVO::getQtdAdicional).reduce(0D, (a,b)-> Uteis.arrendondarForcando2CadasDecimais(a+b));
	}

	public Double getQtdRequisicaoItemDaUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		return getListaItemCotacaoUnidadeEnsinoVOs().stream().filter(p->p.getUnidadeEnsinoVO().getCodigo().equals(unidadeEnsino.getCodigo())).mapToDouble(ItemCotacaoUnidadeEnsinoVO::getQtdRequisicao).reduce(0D, (a,b)-> Uteis.arrendondarForcando2CadasDecimais(a+b));
	}
	
	public Double getQtdTotalItemDaUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		return getListaItemCotacaoUnidadeEnsinoVOs().stream().filter(p->p.getUnidadeEnsinoVO().getCodigo().equals(unidadeEnsino.getCodigo())).mapToDouble(ItemCotacaoUnidadeEnsinoVO::getTotalQtd).reduce(0D, (a,b)-> Uteis.arrendondarForcando2CadasDecimais(a+b));
	}
	
	public Double getQtdAdicionalItemCotacaoUnidadeEnsino() {
		return getListaItemCotacaoUnidadeEnsinoVOs().stream().mapToDouble(ItemCotacaoUnidadeEnsinoVO::getQtdAdicional).reduce(0D, (a,b)-> Uteis.arrendondarForcando2CadasDecimais(a+b));
	}
	
	public Double getQtdRequisicaoItemCotacaoUnidadeEnsino() {
		return getListaItemCotacaoUnidadeEnsinoVOs().stream().mapToDouble(ItemCotacaoUnidadeEnsinoVO::getQtdRequisicao).reduce(0D, (a,b)-> Uteis.arrendondarForcando2CadasDecimais(a+b));
	}

	public Double getQtdTotalItemCotacaoUnidadeEnsino() {
		return getListaItemCotacaoUnidadeEnsinoVOs().stream().mapToDouble(ItemCotacaoUnidadeEnsinoVO::getTotalQtd).reduce(0D, (a,b)-> Uteis.arrendondarForcando2CadasDecimais(a+b));
	}
	
	public boolean isExistePrecoAnterior(){
		return Uteis.isAtributoPreenchido(getPrecoAnterior());
	}

}
