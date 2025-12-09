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
import java.util.ArrayList;
import java.util.List;

public class EstatisticaEstoqueVO implements Serializable {

    private Integer quantidadeProdutosAbaixoMinimo;
    private Integer necessidadeCompraSuprirEstoqueMinimo;
    private List<EstatisticaEstoqueCategoriaProdutoVO> resumoEstatisticaEstoqueCategoriaProdutoVO;
    public static final long serialVersionUID = 1L;

    public EstatisticaEstoqueVO() {
        quantidadeProdutosAbaixoMinimo = 0;
        necessidadeCompraSuprirEstoqueMinimo = 0;
    }

    /**
     * @return the quantidadeProdutosAbaixoMinimo
     */
    public Integer getQuantidadeProdutosAbaixoMinimo() {
        return quantidadeProdutosAbaixoMinimo;
    }

    /**
     * @param quantidadeProdutosAbaixoMinimo
     *            the quantidadeProdutosAbaixoMinimo to set
     */
    public void setQuantidadeProdutosAbaixoMinimo(Integer quantidadeProdutosAbaixoMinimo) {
        this.quantidadeProdutosAbaixoMinimo = quantidadeProdutosAbaixoMinimo;
    }

    /**
     * @return the resumoEstatisticaEstoqueCategoriaProdutoVO
     */
    public List<EstatisticaEstoqueCategoriaProdutoVO> getResumoEstatisticaEstoqueCategoriaProdutoVO() {
        if (resumoEstatisticaEstoqueCategoriaProdutoVO == null) {
            resumoEstatisticaEstoqueCategoriaProdutoVO = new ArrayList<EstatisticaEstoqueCategoriaProdutoVO>(0);
        }
        return resumoEstatisticaEstoqueCategoriaProdutoVO;
    }

    /**
     * @param resumoEstatisticaEstoqueCategoriaProdutoVO
     *            the resumoEstatisticaEstoqueCategoriaProdutoVO to set
     */
    public void setResumoEstatisticaEstoqueCategoriaProdutoVO(List<EstatisticaEstoqueCategoriaProdutoVO> resumoEstatisticaEstoqueCategoriaProdutoVO) {
        this.resumoEstatisticaEstoqueCategoriaProdutoVO = resumoEstatisticaEstoqueCategoriaProdutoVO;
    }

    /**
     * @return the necessidadeCompraSuprirEstoqueMinimo
     */
    public Integer getNecessidadeCompraSuprirEstoqueMinimo() {
        return necessidadeCompraSuprirEstoqueMinimo;
    }

    /**
     * @param necessidadeCompraSuprirEstoqueMinimo
     *            the necessidadeCompraSuprirEstoqueMinimo to set
     */
    public void setNecessidadeCompraSuprirEstoqueMinimo(Integer necessidadeCompraSuprirEstoqueMinimo) {
        this.necessidadeCompraSuprirEstoqueMinimo = necessidadeCompraSuprirEstoqueMinimo;
    }
}
