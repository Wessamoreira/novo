/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Rodrigo
 */
public enum TipoTransferenciaEntrada {

    INTERNA("IN", "INTERNA"),
    EXTERNA("EX", "EXTERNA");
    String valor;
    String descricao;

    TipoTransferenciaEntrada(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoTransferenciaEntrada getEnum(String valor) {
        TipoTransferenciaEntrada[] valores = values();
        for (TipoTransferenciaEntrada obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoTransferenciaEntrada obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public static String getValor(String valor) {
        TipoTransferenciaEntrada obj = getEnum(valor);
        if (obj != null) {
            return obj.getValor();
        }
        return "0";
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
