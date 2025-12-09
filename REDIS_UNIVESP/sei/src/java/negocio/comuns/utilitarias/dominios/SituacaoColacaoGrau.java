/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum SituacaoColacaoGrau {
    //Situação do evento
    
    ABERTO("AB", "Aberto"),
    FECHADO("FE", "Fechado");
       
    
    String valor;
    String descricao;

    SituacaoColacaoGrau(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoColacaoGrau getEnum(String valor) {
        SituacaoColacaoGrau[] valores = values();
        for (SituacaoColacaoGrau obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoColacaoGrau obj = getEnum(valor);
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
