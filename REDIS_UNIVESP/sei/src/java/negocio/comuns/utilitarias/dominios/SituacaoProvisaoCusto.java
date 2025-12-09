/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum SituacaoProvisaoCusto {

    ATIVO("AT", "Ativo"),
    FINALIZADO("FI", "Finalizado");
    
    String valor;
    String descricao;

    SituacaoProvisaoCusto(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoProvisaoCusto getSituacao(String valor) {
        SituacaoProvisaoCusto[] valores = values();
        for (SituacaoProvisaoCusto obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoProvisaoCusto obj = getSituacao(valor);
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
