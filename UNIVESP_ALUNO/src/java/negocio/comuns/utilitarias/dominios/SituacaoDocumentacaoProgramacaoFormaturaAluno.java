/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum SituacaoDocumentacaoProgramacaoFormaturaAluno {

    ENTREGUE("OK", "OK"),
    PENDENTE("PE", "PENDENTE");
    
    String valor;
    String descricao;

    SituacaoDocumentacaoProgramacaoFormaturaAluno(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoDocumentacaoProgramacaoFormaturaAluno getEnum(String valor) {
        SituacaoDocumentacaoProgramacaoFormaturaAluno[] valores = values();
        for (SituacaoDocumentacaoProgramacaoFormaturaAluno obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoDocumentacaoProgramacaoFormaturaAluno obj = getEnum(valor);
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
