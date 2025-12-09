/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum InformarTurma {

    NAO_SOLICITAR("NO", "Não Solicitar"),
    OBRIGATORIO("OB", "Obrigatório"),
    OPTATIVO("OP", "Optativo");
    
    String valor;
    String descricao;

    InformarTurma(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static InformarTurma getEnum(String valor) {
        InformarTurma[] valores = values();
        for (InformarTurma obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        InformarTurma obj = getEnum(valor);
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
