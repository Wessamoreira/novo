package negocio.comuns.compras;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CompraAgrupador {

	private FornecedorVO fornecedor;
	private List<CompraVO> compras;
	private List<ProdutoAgrupador> produtoAgrupadores = new ArrayList<>();
	private long quantidadeTotal = 0;
	private double precoTotal = 0d;

	public CompraAgrupador(FornecedorVO fornecedor, List<CompraVO> list) {
		super();
		this.fornecedor = fornecedor;
		this.compras = list;		
		List<CompraItemVO> compraItens = compras.stream().flatMap(p -> p.getCompraItemVOs().stream()).collect(Collectors.toList());
		for (CompraItemVO vo : compraItens) {
			precoTotal += vo.getPrecoTotal();
			quantidadeTotal += vo.getQuantidade();
		}

		Map<ProdutoServicoVO, List<CompraItemVO>> collect = compraItens.stream().collect(Collectors.groupingBy(CompraItemVO::getProduto));
		collect.entrySet().forEach(p -> produtoAgrupadores.add(new ProdutoAgrupador(p.getKey(), p.getValue())));

	}

	public FornecedorVO getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(FornecedorVO fornecedor) {
		this.fornecedor = fornecedor;
	}

	public long getQuantidadeTotal() {

		return quantidadeTotal;
	}

	public double getPrecoTotal() {

		return precoTotal;
	}

	public List<ProdutoAgrupador> getProdutoAgrupadores() {
		return produtoAgrupadores;
	}

	public void setProdutoAgrupadores(List<ProdutoAgrupador> produtoAgrupadores) {
		this.produtoAgrupadores = produtoAgrupadores;
	}

	public List<CompraVO> getCompras() {
		return compras;
	}

}