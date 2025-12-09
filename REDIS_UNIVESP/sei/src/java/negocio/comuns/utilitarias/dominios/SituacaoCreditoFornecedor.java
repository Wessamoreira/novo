/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum SituacaoCreditoFornecedor {

    ABERTO("AB", "Aberto"),
    USADO_PARCIAL("UP", "Usado Parcial"),
    FINALIZADO("FI", "Finalizado");
    
    String valor;
    String descricao;

    SituacaoCreditoFornecedor(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoCreditoFornecedor getEnum(String valor) {
        SituacaoCreditoFornecedor[] valores = values();
        for (SituacaoCreditoFornecedor obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoCreditoFornecedor obj = getEnum(valor);
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
