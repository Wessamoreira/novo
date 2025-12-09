/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoDisciplina {

    
	OBRIGATORIA("OB", "Obrigatória"),
	LABORATORIAL_OBRIGATORIA("LG", "Laboratorial Obrigatória"),
	LABORATORIAL_OPTATIVA("LO", "Laboratorial Optativa"),
	OPTATIVA("OP", "Optativa");
    
    String valor;
    String descricao;

    TipoDisciplina(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoDisciplina getEnum(String  valor) {
        TipoDisciplina[] valores = values();
        for (TipoDisciplina obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoDisciplina obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return "";
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
