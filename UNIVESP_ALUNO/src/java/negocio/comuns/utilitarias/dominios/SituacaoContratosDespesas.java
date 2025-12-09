/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum SituacaoContratosDespesas {

    INDEFERIDO("IN", "Indeferido"),
    APROVADO("AP", "Aprovado"),
    ATIVO("AT", "Ativo"),
    FINALIZADO("FI", "Finalizado"),
    AGUARDANDO_APROVACAO("AA", "Aguardando Aprovação");
    
    String valor;
    String descricao;

    SituacaoContratosDespesas(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoContratosDespesas getEnum(String valor) {
        SituacaoContratosDespesas[] valores = values();
        for (SituacaoContratosDespesas obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoContratosDespesas obj = getEnum(valor);
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
