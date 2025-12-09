/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TituloCursoMedio {

    NORMAL("NO", "Normal"),                 
    TECNICO("TE", "Técnico");
       
    
    String valor;
    String descricao;

    TituloCursoMedio(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TituloCursoMedio getEnum(String valor) {
        TituloCursoMedio[] valores = values();
        for (TituloCursoMedio obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TituloCursoMedio obj = getEnum(valor);
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
