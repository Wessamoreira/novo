/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.financeiro;

/**
 *
 * @author Rodrigo
 */
import java.io.Serializable;

public class EstatisticaContaPagarReceberVO implements Serializable {

    private Integer unidadeEnsino;
    private String nomeUnidade;
    private Double valorVencidoContaPagar;
    private Double valorContaPagar;
    private Double valorVencerContaPagar;
    private Double valorVencidoContaReceber;
    private Double valorContaReceber;
    private Double valorVencerContaReceber;
    public static final long serialVersionUID = 1L;

    public String getNomeUnidade() {
        if (nomeUnidade == null) {
            nomeUnidade = "";
        }
        return nomeUnidade;
    }

    public void setNomeUnidade(String nomeUnidade) {
        this.nomeUnidade = nomeUnidade;
    }

    public Integer getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = 0;
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public Double getValorContaPagar() {
        if (valorContaPagar == null) {
            valorContaPagar = 0.0;
        }
        return valorContaPagar;
    }

    public void setValorContaPagar(Double valorContaPagar) {
        this.valorContaPagar = valorContaPagar;
    }

    public Double getValorContaReceber() {
        if (valorContaReceber == null) {
            valorContaReceber = 0.0;
        }
        return valorContaReceber;
    }

    public void setValorContaReceber(Double valorContaReceber) {
        this.valorContaReceber = valorContaReceber;
    }

    public Double getValorVencerContaPagar() {
        if (valorVencerContaPagar == null) {
            valorVencerContaPagar = 0.0;
        }
        return valorVencerContaPagar;
    }

    public void setValorVencerContaPagar(Double valorVencerContaPagar) {
        this.valorVencerContaPagar = valorVencerContaPagar;
    }

    public Double getValorVencerContaReceber() {
        if (valorVencerContaReceber == null) {
            valorVencerContaReceber = 0.0;
        }
        return valorVencerContaReceber;
    }

    public void setValorVencerContaReceber(Double valorVencerContaReceber) {
        this.valorVencerContaReceber = valorVencerContaReceber;
    }

    public Double getValorVencidoContaPagar() {
        if (valorVencidoContaPagar == null) {
            valorVencidoContaPagar = 0.0;
        }
        return valorVencidoContaPagar;
    }

    public void setValorVencidoContaPagar(Double valorVencidoContaPagar) {
        this.valorVencidoContaPagar = valorVencidoContaPagar;
    }

    public Double getValorVencidoContaReceber() {
        if (valorVencidoContaReceber == null) {
            valorVencidoContaReceber = 0.0;
        }
        return valorVencidoContaReceber;
    }

    public void setValorVencidoContaReceber(Double valorVencidoContaReceber) {
        this.valorVencidoContaReceber = valorVencidoContaReceber;
    }
}
