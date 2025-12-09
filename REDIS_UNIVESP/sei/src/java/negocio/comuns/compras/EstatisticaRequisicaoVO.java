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
import org.jfree.data.general.DefaultPieDataset;

public class EstatisticaRequisicaoVO implements Serializable {

    private List<ItemCronologicoEstatisticaVO> resumoCronologico;
    private List<ItemSumarioUnidadeEstatisticaVO> sumarioPorUnidade;
    private DefaultCategoryDataset barraSumarioPorUnidade;
    private DefaultPieDataset pizzaSumarioPorUnidade;
    public static final long serialVersionUID = 1L;

    public EstatisticaRequisicaoVO() {
    }

    public void atualizarGraficoBarraSumarioPorUnidade() {
        barraSumarioPorUnidade = new DefaultCategoryDataset();
        pizzaSumarioPorUnidade = new DefaultPieDataset();
        Iterator i = this.getSumarioPorUnidade().iterator();
        while (i.hasNext()) {
            ItemSumarioUnidadeEstatisticaVO itemSumario = (ItemSumarioUnidadeEstatisticaVO) i.next();
            barraSumarioPorUnidade.addValue(itemSumario.getQuantidade(), itemSumario.getNome(), itemSumario.getNome());
            pizzaSumarioPorUnidade.setValue(itemSumario.getNome(), itemSumario.getQuantidade());
        }
    }

    /**
     * @return the resumoCronologico
     */
    public List<ItemCronologicoEstatisticaVO> getResumoCronologico() {
        if (resumoCronologico == null) {
            resumoCronologico = new ArrayList<ItemCronologicoEstatisticaVO>(0);
        }
        return resumoCronologico;
    }

    /**
     * @param resumoCronologico
     *            the resumoCronologico to set
     */
    public void setResumoCronologico(List<ItemCronologicoEstatisticaVO> resumoCronologico) {
        this.resumoCronologico = resumoCronologico;
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

    /**
     * @return the pizzaSumarioPorUnidade
     */
    public DefaultPieDataset getPizzaSumarioPorUnidade() {
        return pizzaSumarioPorUnidade;
    }

    /**
     * @param pizzaSumarioPorUnidade
     *            the pizzaSumarioPorUnidade to set
     */
    public void setPizzaSumarioPorUnidade(DefaultPieDataset pizzaSumarioPorUnidade) {
        this.pizzaSumarioPorUnidade = pizzaSumarioPorUnidade;
    }
}
