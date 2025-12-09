/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum PrioridadeComunicadoInterno {

        BAIXA("BA", "Baixa"),
        NORMAL("NO", "Normal"),
        ALTA("AL", "Alta");
    
    String valor;
    String descricao;

    PrioridadeComunicadoInterno(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static PrioridadeComunicadoInterno getEnum(String valor) {
        PrioridadeComunicadoInterno[] valores = values();
        for (PrioridadeComunicadoInterno obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        PrioridadeComunicadoInterno obj = getEnum(valor);
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
