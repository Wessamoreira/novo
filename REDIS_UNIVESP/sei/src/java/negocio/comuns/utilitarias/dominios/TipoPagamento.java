/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoPagamento {

    DEBITO("DE", "Débito"),
    CREDITO("CR", "Crédito");
    
    String valor;
    String descricao;

    TipoPagamento(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoPagamento getEnum(String valor) {
        TipoPagamento[] valores = values();
        for (TipoPagamento obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoPagamento obj = getEnum(valor);
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
