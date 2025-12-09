/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum ModeloGeracaoBoletoSicoob {

	NENHUM("", ""),
	NORMAL("NORMAL", "SICOOB - NORMAL"),
	FEBRABAN("FEBRABAN", "SICOB - FEBRABAN");
    
    String valor;
    String descricao;

    ModeloGeracaoBoletoSicoob(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static ModeloGeracaoBoletoSicoob getEnum(String valor) {
        ModeloGeracaoBoletoSicoob[] valores = values();
        for (ModeloGeracaoBoletoSicoob obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        ModeloGeracaoBoletoSicoob obj = getEnum(valor);
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
