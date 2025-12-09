/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoContratosDespesas {

    ANUAL("AN", "Anual"),
    MENSAL("ME", "Mensal"),
    ESPECIFICO("ES", "Específico");
    
    String valor;
    String descricao;

    TipoContratosDespesas(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoContratosDespesas getEnum(String valor) {
        TipoContratosDespesas[] valores = values();
        for (TipoContratosDespesas obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoContratosDespesas obj = getEnum(valor);
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
