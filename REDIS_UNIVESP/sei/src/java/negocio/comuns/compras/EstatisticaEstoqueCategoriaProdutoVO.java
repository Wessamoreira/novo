/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.compras;

/**
 * 
 * @author Edigar
 */
import java.io.Serializable;

public class EstatisticaEstoqueCategoriaProdutoVO implements Serializable {

    private String nomeCategoriaProduto;
    private Integer codigoCategoriaProduto;
    private Integer quantidadeUnitariaProdutoAbaixoMinimo;
    private Integer codigoUnidadeEnsino;
    private String nomeUnidadeEnsino;
    private Integer quantidadeProduto;
    public static final long serialVersionUID = 1L;

    public EstatisticaEstoqueCategoriaProdutoVO() {
        nomeCategoriaProduto = "";
        codigoCategoriaProduto = 0;
        quantidadeUnitariaProdutoAbaixoMinimo = 0;
        codigoUnidadeEnsino = 0;
        nomeUnidadeEnsino = "";
        quantidadeProduto = 0;
    }

    /**
     * @return the nomeCategoriaProduto
     */
    public String getNomeCategoriaProduto() {
        return nomeCategoriaProduto;
    }

    /**
     * @param nomeCategoriaProduto
     *            the nomeCategoriaProduto to set
     */
    public void setNomeCategoriaProduto(String nomeCategoriaProduto) {
        this.nomeCategoriaProduto = nomeCategoriaProduto;
    }

    /**
     * @return the codigoCategoriaProduto
     */
    public Integer getCodigoCategoriaProduto() {
        return codigoCategoriaProduto;
    }

    /**
     * @param codigoCategoriaProduto
     *            the codigoCategoriaProduto to set
     */
    public void setCodigoCategoriaProduto(Integer codigoCategoriaProduto) {
        this.codigoCategoriaProduto = codigoCategoriaProduto;
    }

    /**
     * @return the quantidadeUnitariaProdutoAbaixoMinimo
     */
    public Integer getQuantidadeUnitariaProdutoAbaixoMinimo() {
        return quantidadeUnitariaProdutoAbaixoMinimo;
    }

    /**
     * @param quantidadeUnitariaProdutoAbaixoMinimo
     *            the quantidadeUnitariaProdutoAbaixoMinimo to set
     */
    public void setQuantidadeUnitariaProdutoAbaixoMinimo(Integer quantidadeUnitariaProdutoAbaixoMinimo) {
        this.quantidadeUnitariaProdutoAbaixoMinimo = quantidadeUnitariaProdutoAbaixoMinimo;
    }

	public Integer getCodigoUnidadeEnsino() {
		return codigoUnidadeEnsino;
	}

	public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
		this.codigoUnidadeEnsino = codigoUnidadeEnsino;
	}

	public String getNomeUnidadeEnsino() {
		return nomeUnidadeEnsino;
	}

	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
	}

	public Integer getQuantidadeProduto() {
		return quantidadeProduto;
	}

	public void setQuantidadeProduto(Integer quantidadeProduto) {
		this.quantidadeProduto = quantidadeProduto;
	}
}
