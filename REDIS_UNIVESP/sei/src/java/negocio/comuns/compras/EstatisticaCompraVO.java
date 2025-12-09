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
import java.util.Iterator;
import java.util.List;

import org.jfree.data.category.DefaultCategoryDataset;

public class EstatisticaCompraVO implements Serializable {

    private List<ItemSumarioUnidadeEstatisticaVO> sumarioPorUnidade;
    private DefaultCategoryDataset barraSumarioPorUnidade;
    public static final long serialVersionUID = 1L;

    public EstatisticaCompraVO() {
    }

    public void atualizarGraficoBarraSumarioPorUnidade() {
        barraSumarioPorUnidade = new DefaultCategoryDataset();
        Iterator i = this.getSumarioPorUnidade().iterator();
        while (i.hasNext()) {
            ItemSumarioUnidadeEstatisticaVO itemSumario = (ItemSumarioUnidadeEstatisticaVO) i.next();
            barraSumarioPorUnidade.addValue(itemSumario.getQuantidade(), itemSumario.getNome(), itemSumario.getNome());
        }
    }

    /**
     * @return the sumarioPorUnidade
     */
    public List<ItemSumarioUnidadeEstatisticaVO> getSumarioPorUnidade() {
        if (sumarioPorUnidade == null) {
            sumarioPorUnidade = new ArrayList<ItemSumarioUnidadeEstatisticaVO>(0);
        }
        return sumarioPorUnidade;
    }

    /**
     * @param sumarioPorUnidade
     *            the sumarioPorUnidade to set
     */
    public void setSumarioPorUnidade(List<ItemSumarioUnidadeEstatisticaVO> sumarioPorUnidade) {
        this.sumarioPorUnidade = sumarioPorUnidade;
    }

    /**
     * @return the barraSumarioPorUnidade
     */
    public DefaultCategoryDataset getBarraSumarioPorUnidade() {
        if (barraSumarioPorUnidade == null) {
            barraSumarioPorUnidade = new DefaultCategoryDataset();
        }
        return barraSumarioPorUnidade;
    }

    /**
     * @param barraSumarioPorUnidade
     *            the barraSumarioPorUnidade to set
     */
    public void setBarraSumarioPorUnidade(DefaultCategoryDataset barraSumarioPorUnidade) {
        this.barraSumarioPorUnidade = barraSumarioPorUnidade;
    }
}
