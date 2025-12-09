/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoComunicadoInterno {

        MURAL("MU", "Mural"),
        SOMENTE_LEITURA("LE", "Somente Leitura"),
        EXIGE_RESPOSTA("RE", "Exige Resposta");
    
    String valor;
    String descricao;

    TipoComunicadoInterno(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoComunicadoInterno getEnum(String valor) {
        TipoComunicadoInterno[] valores = values();
        for (TipoComunicadoInterno obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoComunicadoInterno obj = getEnum(valor);
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
