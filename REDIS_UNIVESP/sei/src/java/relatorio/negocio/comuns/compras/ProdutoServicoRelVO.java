/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.compras;

/**
 * 
 * @author Ana Claudia
 */
public class ProdutoServicoRelVO {

	private String categoriaProduto;
	private String  produto;
	private String tipoProduto;
	private String situacaoProduto;

	public ProdutoServicoRelVO() {
		setCategoriaProduto("");
		setProduto("");
		setTipoProduto("");
		setSituacaoProduto("");
	}

	public String getCategoriaProduto() {
		return categoriaProduto;
	}

	public void setCategoriaProduto(String categoriaProduto) {
		this.categoriaProduto = categoriaProduto;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public String getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(String tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public String getSituacaoProduto() {
		return situacaoProduto;
	}

	public void setSituacaoProduto(String situacaoProduto) {
		this.situacaoProduto = situacaoProduto;
	}

}