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

public class EstatisticaCotacaoVO implements Serializable {

    private List<ItemCronologicoEstatisticaVO> resumoCronologico;
    private List<ItemSumarioUnidadeEstatisticaVO> sumarioPorClassificacao;
    private DefaultCategoryDataset barraSumarioPorClassificacao;
    private DefaultPieDataset pizzaSumarioPorClassificacao;
    public static final long serialVersionUID = 1L;

    public EstatisticaCotacaoVO() {
    }

    public void atualizarGraficoBarraSumarioPorUnidade() {
        setBarraSumarioPorClassificacao(new DefaultCategoryDataset());
        setPizzaSumarioPorClassificacao(new DefaultPieDataset());
        Iterator i = this.getSumarioPorUnidade().iterator();
        while (i.hasNext()) {
            ItemSumarioUnidadeEstatisticaVO itemSumario = (ItemSumarioUnidadeEstatisticaVO) i.next();
            getBarraSumarioPorClassificacao().addValue(itemSumario.getQuantidade(), itemSumario.getNome(), itemSumario.getNome());
            getPizzaSumarioPorClassificacao().setValue(itemSumario.getNome(), itemSumario.getQuantidade());
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
        if (sumarioPorClassificacao == null) {
            sumarioPorClassificacao = new ArrayList<ItemSumarioUnidadeEstatisticaVO>(0);
        }
        return sumarioPorClassificacao;
    }

    /**
     * @param sumarioPorUnidade
     *            the sumarioPorUnidade to set
     */
    public void setSumarioPorUnidade(List<ItemSumarioUnidadeEstatisticaVO> sumarioPorUnidade) {
        this.sumarioPorClassificacao = sumarioPorUnidade;
    }

    /**
     * @return the barraSumarioPorUnidade
     */
    public DefaultCategoryDataset getBarraSumarioPorClassificacao() {
        if (barraSumarioPorClassificacao == null) {
            barraSumarioPorClassificacao = new DefaultCategoryDataset();
        }
        return barraSumarioPorClassificacao;
    }

    /**
     * @param barraSumarioPorUnidade
     *            the barraSumarioPorUnidade to set
     */
    public void setBarraSumarioPorClassificacao(DefaultCategoryDataset barraSumario) {
        this.barraSumarioPorClassificacao = barraSumario;
    }

    /**
     * @return the pizzaSumarioPorUnidade
     */
    public DefaultPieDataset getPizzaSumarioPorUnidade() {
        return getPizzaSumarioPorClassificacao();
    }

    /**
     * @param pizzaSumarioPorUnidade
     *            the pizzaSumarioPorUnidade to set
     */
    public void setPizzaSumarioPorUnidade(DefaultPieDataset pizzaSumarioPorUnidade) {
        this.setPizzaSumarioPorClassificacao(pizzaSumarioPorUnidade);
    }

    /**
     * @return the pizzaSumarioPorClassificacao
     */
    public DefaultPieDataset getPizzaSumarioPorClassificacao() {
        return pizzaSumarioPorClassificacao;
    }

    /**
     * @param pizzaSumarioPorClassificacao
     *            the pizzaSumarioPorClassificacao to set
     */
    public void setPizzaSumarioPorClassificacao(DefaultPieDataset pizzaSumarioPorClassificacao) {
        this.pizzaSumarioPorClassificacao = pizzaSumarioPorClassificacao;
    }
}
