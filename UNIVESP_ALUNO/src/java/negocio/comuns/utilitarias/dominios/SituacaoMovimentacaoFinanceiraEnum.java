/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Alberto
 */
public enum SituacaoMovimentacaoFinanceiraEnum {

    PENDENTE("PE", "Pendente"),
    FINALIZADA("FI", "Finalizada"),
    RECUSADA("RE", "Recusada");
    
    String valor;
    String descricao;

    SituacaoMovimentacaoFinanceiraEnum(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoMovimentacaoFinanceiraEnum getEnum(String valor) {
        SituacaoMovimentacaoFinanceiraEnum[] valores = values();
        for (SituacaoMovimentacaoFinanceiraEnum obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoMovimentacaoFinanceiraEnum obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
