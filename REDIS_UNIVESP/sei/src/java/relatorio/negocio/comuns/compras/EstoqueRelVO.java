/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.compras;

import java.util.Date;

/**
 * 
 * @author Manoel
 */
public class EstoqueRelVO {

	private String unidadeEnsino;
	private String produto;
	private Double estoqueMinimo;
	private Double estoqueMaximo;
	private Double quantidade;
	private Double precoUnitario;
	private Double custoTotal;
	private Date dataEntrada;
	private String unidade;
	private Integer codigoProduto;
	private String categoriaProdutoPai;
	private String categoriaProduto;
	private Double valorTotalPago;
	private Double custoMedio;

	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public String getProduto() {
		if (produto == null) {
			produto = "";
		}
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public Double getEstoqueMinimo() {
		if (estoqueMinimo == null) {
			estoqueMinimo = 0.0;
		}
		return estoqueMinimo;
	}

	public void setEstoqueMinimo(Double estoqueMinimo) {
		this.estoqueMinimo = estoqueMinimo;
	}

	public Double getEstoqueMaximo() {
		if (estoqueMaximo == null) {
			estoqueMaximo = 0.0;
		}
		return estoqueMaximo;
	}

	public void setEstoqueMaximo(Double estoqueMaximo) {
		this.estoqueMaximo = estoqueMaximo;
	}

	public Double getQuantidade() {
		if (quantidade == null) {
			quantidade = 0.0;
		}
		return quantidade;
	}

	public void setQuantidade(Double quantidade) {
		this.quantidade = quantidade;
	}

	public Double getPrecoUnitario() {
		if (precoUnitario == null) {
			precoUnitario = 0.0;
		}
		return precoUnitario;
	}

	public void setPrecoUnitario(Double precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public Double getCustoTotal() {
		if (custoTotal == null) {
			custoTotal = 0.0;
		}
		return custoTotal;
	}

	public void setCustoTotal(Double custoTotal) {
		this.custoTotal = custoTotal;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public String getUnidade() {
		if(unidade == null){
			unidade = "";
		}
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public Integer getCodigoProduto() {
		if (codigoProduto == null) {
			codigoProduto = 0;
		}
		return codigoProduto;
	}

	public void setCodigoProduto(Integer codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getCategoriaProdutoPai() {
		if(categoriaProdutoPai == null){
			categoriaProdutoPai = "";
		}
		return categoriaProdutoPai;
	}

	public void setCategoriaProdutoPai(String categoriaProdutoPai) {
		this.categoriaProdutoPai = categoriaProdutoPai;
	}

	public String getCategoriaProduto() {
		if(categoriaProduto == null){
			categoriaProduto = "";
		}
		return categoriaProduto;
	}

	public void setCategoriaProduto(String categoriaProduto) {
		this.categoriaProduto = categoriaProduto;
	}

	public Double getValorTotalPago() {
		if (valorTotalPago == null) {
			valorTotalPago = 0.0;
		}
		return valorTotalPago;
	}

	public void setValorTotalPago(Double valorTotalPago) {
		this.valorTotalPago = valorTotalPago;
	}

	public Double getCustoMedio() {
		if (custoMedio == null) {
			custoMedio = 0.0;
		}
		return custoMedio;
	}

	public void setCustoMedio(Double custoMedio) {
		this.custoMedio = custoMedio;
	}

}
