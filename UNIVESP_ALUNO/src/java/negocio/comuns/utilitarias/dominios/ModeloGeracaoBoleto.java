/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum ModeloGeracaoBoleto {

	NENHUM("", ""),
	CAIXA("CEF", "CAIXA ECONÔMICA"),
    SICOB("SICOB", "SICOB - NORMAL"),
	SICOB15("SICOB15", "SICOB - 15 POSIÇÕES");
    
    String valor;
    String descricao;

    ModeloGeracaoBoleto(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static ModeloGeracaoBoleto getEnum(String valor) {
        ModeloGeracaoBoleto[] valores = values();
        for (ModeloGeracaoBoleto obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        ModeloGeracaoBoleto obj = getEnum(valor);
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
