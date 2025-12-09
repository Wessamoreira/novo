/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.List;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Philippe
 */
public class TermoReconhecimentoDividaRelSub3VO {

    private Integer totalParcelasNovaContaReceber;
    private Double totalValorNovaContaReceber;
    private List<TermoReconhecimentoDividaParcelasRelVO> parcelasNovaContaReceber;

    public Integer getTotalParcelasNovaContaReceber() {
        if (totalParcelasNovaContaReceber == null) {
            totalParcelasNovaContaReceber = 0;
        }
        return totalParcelasNovaContaReceber;
    }

    public void setTotalParcelasNovaContaReceber(Integer totalParcelasNovaContaReceber) {
        this.totalParcelasNovaContaReceber = totalParcelasNovaContaReceber;
    }

    public String getTotalValorNovaContaReceber_Apresentar() {
        return Uteis.formatarDecimalDuasCasas(getTotalValorNovaContaReceber()).toString().replace(".", ",");
    }

    public Double getTotalValorNovaContaReceber() {
        if (totalValorNovaContaReceber == null) {
            totalValorNovaContaReceber = 0.0;
        }
        return totalValorNovaContaReceber;
    }

    public void setTotalValorNovaContaReceber(Double totalValorNovaContaReceber) {
        this.totalValorNovaContaReceber = totalValorNovaContaReceber;
    }

    public List<TermoReconhecimentoDividaParcelasRelVO> getParcelasNovaContaReceber() {
        if (parcelasNovaContaReceber == null) {
            parcelasNovaContaReceber = new ArrayList<TermoReconhecimentoDividaParcelasRelVO>(0);
        }
        return parcelasNovaContaReceber;
    }

    public void setParcelasNovaContaReceber(List<TermoReconhecimentoDividaParcelasRelVO> parcelasNovaContaReceber) {
        this.parcelasNovaContaReceber = parcelasNovaContaReceber;
    }

    public String getTotalParcelasNovaContaReceber_Apresentar() {
        return getTotalParcelasNovaContaReceber().toString().replace('.', ',');
    }

    public JRDataSource getParcelasNovaContaReceberJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getParcelasNovaContaReceber().toArray());
        return jr;
    }
}
