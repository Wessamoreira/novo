/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.compras;

import java.util.Date;

/**
 * 
 * @author Ana Claudia
 */
public class CompraRelVO {

	private Integer numeroCompra;
	private Date dataCompra;
	private String nomeFornecedor;
	private String cnpjFornecedor;
	private String cpfFornecedor;
	private String enderecoFornecedor;
	private String nomeProduto;
	private Double quantidadeProduto;
	private Double precoUnitario;
	private Double valorTotal;
	private String condicaoPagamento;
	private String formaPagamento;
	private String unidadeMedida;
	private String contatoFornecedor;
	private String telefone1Fornecedor;
	private String telefone2Fornecedor;
	private String telefone3Fornecedor;
	private String tipoEmpresa;
	private String observacao;

	public CompraRelVO() {
		setNumeroCompra(0);
		setDataCompra(new Date());
		setNomeFornecedor("");
		setCnpjFornecedor("");
		setEnderecoFornecedor("");
		setNomeProduto("");
		setQuantidadeProduto(0.0);
		setPrecoUnitario(0.0);
		setValorTotal(0.0);
		setCondicaoPagamento("");
		setFormaPagamento("");
		setUnidadeMedida("");
	}

	public Integer getNumeroCompra() {
		return numeroCompra;
	}

	public void setNumeroCompra(Integer numeroCompra) {
		this.numeroCompra = numeroCompra;
	}

	public Date getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(Date dataCompra) {
		this.dataCompra = dataCompra;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public String getCnpjFornecedor() {
		return cnpjFornecedor;
	}

	public void setCnpjFornecedor(String cnpjFornecedor) {
		this.cnpjFornecedor = cnpjFornecedor;
	}

	public String getEnderecoFornecedor() {
		return enderecoFornecedor;
	}

	public void setEnderecoFornecedor(String enderecoFornecedor) {
		this.enderecoFornecedor = enderecoFornecedor;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Double getQuantidadeProduto() {
		return quantidadeProduto;
	}

	public void setQuantidadeProduto(Double quantidadeProduto) {
		this.quantidadeProduto = quantidadeProduto;
	}

	public Double getPrecoUnitario() {
		return precoUnitario;
	}

	public void setPrecoUnitario(Double precoUnitario) {
		this.precoUnitario = precoUnitario;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getCondicaoPagamento() {
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(String condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	public String getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public String getUnidadeMedida() {
		return unidadeMedida;
	}

	public void setUnidadeMedida(String unidadeMedida) {
		this.unidadeMedida = unidadeMedida;
	}

	public String getContatoFornecedor() {
		if(contatoFornecedor == null) {
			contatoFornecedor = "";
		}
		return contatoFornecedor;
	}

	public void setContatoFornecedor(String contatoFornecedor) {
		this.contatoFornecedor = contatoFornecedor;
	}

	public String getTelefone1Fornecedor() {
		if(telefone1Fornecedor == null) {
			telefone1Fornecedor = "";
		}
		return telefone1Fornecedor;
	}

	public void setTelefone1Fornecedor(String telefone1Fornecedor) {
		this.telefone1Fornecedor = telefone1Fornecedor;
	}

	public String getTelefone2Fornecedor() {
		if(telefone2Fornecedor == null) {
			telefone2Fornecedor = "";
		}
		return telefone2Fornecedor;
	}

	public void setTelefone2Fornecedor(String telefone2Fornecedor) {
		this.telefone2Fornecedor = telefone2Fornecedor;
	}

	public String getTelefone3Fornecedor() {
		if(telefone3Fornecedor == null) {
			telefone3Fornecedor = "";
		}
		return telefone3Fornecedor;
	}

	public void setTelefone3Fornecedor(String telefone3Fornecedor) {
		this.telefone3Fornecedor = telefone3Fornecedor;
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

	public String getTipoEmpresa() {
		if(tipoEmpresa == null) {
			tipoEmpresa = "";
		}
		return tipoEmpresa;
	}

	public void setTipoEmpresa(String tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}		
	
	public String getObservacao() {
		if(observacao == null) {
			observacao = "";
		}
		return observacao;
	}
	
}