/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum OrigemCreditoFornecedor {

    DEVOLUCAO_COMPRA("DC", "Devolução de Compra");
    
    String valor;
    String descricao;

    OrigemCreditoFornecedor(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static OrigemCreditoFornecedor getEnum(String valor) {
        OrigemCreditoFornecedor[] valores = values();
        for (OrigemCreditoFornecedor obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        OrigemCreditoFornecedor obj = getEnum(valor);
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
