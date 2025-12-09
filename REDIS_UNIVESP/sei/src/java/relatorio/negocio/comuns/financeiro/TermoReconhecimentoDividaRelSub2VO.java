/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.financeiro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Extenso;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 *
 * @author Philippe
 */
public class TermoReconhecimentoDividaRelSub2VO extends SuperVO {

    private List<TermoReconhecimentoDividaDebitoRelVO> debito;
    private Integer totalParcelasDebito;
    private Double totalValorDebito;
    private String historico;
    private String nomeUtilizarInidiceReajustePorAtraso;

    public List getDebito() {
        if (debito == null) {
            debito = new ArrayList(0);
        }
        return debito;
    }

    public void setDebito(List debito) {
        this.debito = debito;
    }

    public String getTotalParcelasDebito_Apresentar() {
        return getTotalParcelasDebito().toString();
    }

    public Integer getTotalParcelasDebito() {
        if (totalParcelasDebito == null) {
            totalParcelasDebito = 0;
        }
        return totalParcelasDebito;
    }

    public void setTotalParcelasDebito(Integer totalParcelasDebito) {
        this.totalParcelasDebito = totalParcelasDebito;
    }

    public String getTotalValorDebito_Apresentar() {
        return Uteis.formatarDecimalDuasCasas(getTotalValorDebito()).toString().replace('.', ',');
    }

    public String getTotalValorDebito_Extenso() {
        Extenso extenso = new Extenso();
        extenso.setNumber(getTotalValorDebito());
        return extenso.toString();
    }

    public Double getTotalValorDebito() {
        if (totalValorDebito == null) {
            totalValorDebito = 0.0;
        }
        return totalValorDebito;
    }

    public void setTotalValorDebito(Double totalValorDebito) {
        this.totalValorDebito = totalValorDebito;
    }

    public String getHistorico() {
        if (historico == null) {
            historico = "";
        }
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public JRDataSource getDebitoJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getDebito().toArray());
        return jr;
    }

	public String getNomeUtilizarInidiceReajustePorAtraso() {
		if (nomeUtilizarInidiceReajustePorAtraso == null) {
			nomeUtilizarInidiceReajustePorAtraso = "";
		}
		return nomeUtilizarInidiceReajustePorAtraso;
	}

	public void setNomeUtilizarInidiceReajustePorAtraso(String nomeUtilizarInidiceReajustePorAtraso) {
		this.nomeUtilizarInidiceReajustePorAtraso = nomeUtilizarInidiceReajustePorAtraso;
	}
}
