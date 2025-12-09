package negocio.comuns.compras;

import java.util.List;

public class ProdutoAgrupador {

	private ProdutoServicoVO produto;
	private List<CompraItemVO> compraItens;
	private double precoTotal = 0;
	private long quantidade = 0;

	public ProdutoAgrupador(ProdutoServicoVO produto, List<CompraItemVO> compraItens) {
		super();
		this.produto = produto;
		this.compraItens = compraItens;

		for (CompraItemVO vo : compraItens) {
			precoTotal += vo.getPrecoTotal();
			quantidade += vo.getQuantidade();
		}
	}

	public ProdutoServicoVO getProduto() {
		return produto;
	}

	public List<CompraItemVO> getCompraItens() {
		return compraItens;
	}

	public Double getPrecoTotal() {
		return precoTotal;
	}

	public long getQuantidade() {
		return quantidade;
	}

}