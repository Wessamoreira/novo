/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.administrativo;

/**
 *
 * @author Rodrigo
 */
import java.io.Serializable;

public class PainelGestorMonitoramentoDescontoInstituicaoVO implements Serializable {

    private String nomeTipoDesconto;
    private Double totalDesconto;
    private Double valorFaturado;
    private Double percentualDescontoRelacaoTotalDesconto;
    private Double percentualDescontoRelacaoFaturado;
    private Integer numeroAlunoComDesconto;
    public static final long serialVersionUID = 1L;

    public Double getValorFaturado() {
        if (valorFaturado == null) {
            valorFaturado = 0.0;
        }
        return valorFaturado;
    }

    public void setValorFaturado(Double valorFaturado) {
        this.valorFaturado = valorFaturado;
    }

    public String getNomeTipoDesconto() {
        if (nomeTipoDesconto == null) {
            nomeTipoDesconto = "";
        }
        return nomeTipoDesconto;
    }

    public void setNomeTipoDesconto(String nomeTipoDesconto) {
        this.nomeTipoDesconto = nomeTipoDesconto;
    }

    public Integer getNumeroAlunoComDesconto() {
        if (numeroAlunoComDesconto == null) {
            numeroAlunoComDesconto = 0;
        }
        return numeroAlunoComDesconto;
    }

    public void setNumeroAlunoComDesconto(Integer numeroAlunoComDesconto) {
        this.numeroAlunoComDesconto = numeroAlunoComDesconto;
    }

    public Double getPercentualDescontoRelacaoFaturado() {
        if (percentualDescontoRelacaoFaturado == null) {
            percentualDescontoRelacaoFaturado = 0.0;
        }
        return percentualDescontoRelacaoFaturado;
    }

    public void setPercentualDescontoRelacaoFaturado(Double percentualDescontoRelacaoFaturado) {
        this.percentualDescontoRelacaoFaturado = percentualDescontoRelacaoFaturado;
    }

    public Double getPercentualDescontoRelacaoTotalDesconto() {
        if (percentualDescontoRelacaoTotalDesconto == null) {
            percentualDescontoRelacaoTotalDesconto = 0.0;
        }
        return percentualDescontoRelacaoTotalDesconto;
    }

    public void setPercentualDescontoRelacaoTotalDesconto(Double percentualDescontoRelacaoTotalDesconto) {
        this.percentualDescontoRelacaoTotalDesconto = percentualDescontoRelacaoTotalDesconto;
    }

    public Double getTotalDesconto() {
        if (totalDesconto == null) {
            totalDesconto = 0.0;
        }
        return totalDesconto;
    }

    public void setTotalDesconto(Double totalDesconto) {
        this.totalDesconto = totalDesconto;
    }
}
