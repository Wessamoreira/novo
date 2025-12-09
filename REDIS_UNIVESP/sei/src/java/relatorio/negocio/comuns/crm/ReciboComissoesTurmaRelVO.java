/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package relatorio.negocio.comuns.crm;

/**
 *
 * @author Philippe
 */
public class ReciboComissoesTurmaRelVO {
    private String nomeTurma;
    private Double valorTurma;

    public String getNomeTurma() {
        if (nomeTurma == null) {
            nomeTurma = "";
        }
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public Double getValorTurma() {
        if (valorTurma == null) {
            valorTurma = 0.0;
        }
        return valorTurma;
    }

    public void setValorTurma(Double valorTurma) {
        this.valorTurma = valorTurma;
    }
    
}
