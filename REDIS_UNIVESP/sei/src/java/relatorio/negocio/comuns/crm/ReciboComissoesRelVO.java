package relatorio.negocio.comuns.crm;

import java.util.ArrayList;
import java.util.List;
import negocio.comuns.utilitarias.Extenso;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class ReciboComissoesRelVO {

    private Integer codigo;
    private String funcionario;
    private Double valorTotal;
    private List<ReciboComissoesTurmaRelVO> reciboComissoesTurmaRelVOs;
    private String valorTotalPorExtenso;
    private String unidadeEnsino;


    public ReciboComissoesRelVO() {
        setCodigo(0);
        setValorTotal(0.0);
        setFuncionario("");
        setReciboComissoesTurmaRelVOs(new ArrayList(0));
        setUnidadeEnsino("");
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(String funcionario) {
        this.funcionario = funcionario;
    }

    public Double getValorTotal() {
        for (ReciboComissoesTurmaRelVO reciboComissoesTurmaRelVO : getReciboComissoesTurmaRelVOs()) {
            if (valorTotal == null) {
                valorTotal = 0.0;
            }
            setValorTotal(valorTotal + reciboComissoesTurmaRelVO.getValorTurma());
        }
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<ReciboComissoesTurmaRelVO> getReciboComissoesTurmaRelVOs() {
        return reciboComissoesTurmaRelVOs;
    }

    public void setReciboComissoesTurmaRelVOs(List<ReciboComissoesTurmaRelVO> reciboComissoesTurmaRelVOs) {
        this.reciboComissoesTurmaRelVOs = reciboComissoesTurmaRelVOs;
    }

    public String getValorTotalPorExtenso() {
        if (valorTotalPorExtenso == null) {
            valorTotalPorExtenso = "";
        }
        Extenso ext = new Extenso();
        ext.setNumber(getValorTotal());
        valorTotalPorExtenso = "R$ " + ext.toString();
        return valorTotalPorExtenso;
    }

    public void setValorTotalPorExtenso(String valorTotalPorExtenso) {
        this.valorTotalPorExtenso = valorTotalPorExtenso;
    }

    public JRDataSource getReciboComissoesTurmaRelVOsJR() {
        JRDataSource jr = new JRBeanArrayDataSource(getReciboComissoesTurmaRelVOs().toArray());
        return jr;
    }

    public String getUnidadeEnsino() {
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(String unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }
}
