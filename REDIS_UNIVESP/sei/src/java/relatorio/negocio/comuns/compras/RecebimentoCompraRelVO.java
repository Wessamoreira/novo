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
public class RecebimentoCompraRelVO {

	private Date dataRecebimento;
	private String situacaoRecebimento;
	private Double valorTotal;
	private Date dataCompra;
	private String fornecedor;
	private String cnpjFornecedor;
	private String cpfFornecedor;
	private String tipoEmpresaFornecedor;
	private String unidadeEnsino;
	private Double totalRecebimento;
	private String categoriaDespesa;
	private String produto;
	private String categoriaProduto;
	private String formaPagamento;
	private String condicaoPagamento;
	private Integer linha;
	private Integer codigoCompra;
	private Double quantidadeRecebida;
	private Double precoUnitario;
	private Double quantidade;

	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public String getSituacaoRecebimento() {
		if (situacaoRecebimento == null) {
			situacaoRecebimento = "";
		}
		return situacaoRecebimento;
	}

	public void setSituacaoRecebimento(String situacaoRecebimento) {
		this.situacaoRecebimento = situacaoRecebimento;
	}

	public Double getValorTotal() {
		if (valorTotal == null) {
			valorTotal = 0.0;
		}
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Date getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(Date dataCompra) {
		this.dataCompra = dataCompra;
	}

	public String getFornecedor() {
		if (fornecedor == null) {
			fornecedor = "";
		}
		return fornecedor;
	}

	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = "";
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(String unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Double getTotalRecebimento() {
		if (totalRecebimento == null) {
			totalRecebimento = 0.0;
		}
		return totalRecebimento;
	}

	public void setTotalRecebimento(Double totalRecebimento) {
		this.totalRecebimento = totalRecebimento;
	}

	public String getCategoriaDespesa() {
		if (categoriaDespesa == null) {
			categoriaDespesa = "";
		}
		return categoriaDespesa;
	}

	public void setCategoriaDespesa(String categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
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

	public String getCategoriaProduto() {
		if (categoriaProduto == null) {
			categoriaProduto = "";
		}
		return categoriaProduto;
	}

	public void setCategoriaProduto(String categoriaProduto) {
		this.categoriaProduto = categoriaProduto;
	}

	public String getFormaPagamento() {
		if (formaPagamento == null) {
			formaPagamento = "";
		}
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public String getCondicaoPagamento() {
		if (condicaoPagamento == null) {
			condicaoPagamento = "";
		}
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(String condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	public Integer getLinha() {
		if (linha == null) {
			linha = 0;
		}
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
	}

	public Integer getCodigoCompra() {
		if (codigoCompra == null) {
			codigoCompra = 0;
		}
		return codigoCompra;
	}

	public void setCodigoCompra(Integer codigoCompra) {
		this.codigoCompra = codigoCompra;
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

	public Double getQuantidadeRecebida() {
		if (quantidadeRecebida == null) {
			quantidadeRecebida = 0.0;
		}
		return quantidadeRecebida;
	}

	public void setQuantidadeRecebida(Double quantidadeRecebida) {
		this.quantidadeRecebida = quantidadeRecebida;
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

	public String getCnpjFornecedor() {
		if(cnpjFornecedor == null) {
			cnpjFornecedor = "";
		}
		return cnpjFornecedor;
	}

	public void setCnpjFornecedor(String cnpjFornecedor) {
		this.cnpjFornecedor = cnpjFornecedor;
	}

	public String getCpfFornecedor() {
		if(cpfFornecedor == null) {
			cpfFornecedor = "";
		}
		return cpfFornecedor;
	}

	public void setCpfFornecedor(String cpfFornecedor) {
		this.cpfFornecedor = cpfFornecedor;
	}

	public String getTipoEmpresaFornecedor() {
		if(tipoEmpresaFornecedor == null) {
			tipoEmpresaFornecedor = "";
		}
		return tipoEmpresaFornecedor;
	}

	public void setTipoEmpresaFornecedor(String tipoEmpresaFornecedor) {
		this.tipoEmpresaFornecedor = tipoEmpresaFornecedor;
	}		

}
