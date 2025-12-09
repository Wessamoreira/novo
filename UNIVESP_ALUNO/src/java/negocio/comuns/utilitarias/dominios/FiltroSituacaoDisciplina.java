/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum FiltroSituacaoDisciplina {

    
    APROVADAS(2, "Aprovadas"),
    APROVADAS_REPROVADAS(3, "Aprovadas e Reprovadas"),
    APROVADAS_CURSANDO(4, "Aprovadas e Cursando"),
    APROVADAS_REPROVADAS_CURSANDO(5, "Aprovadas, Reprovadas e Cursando"),
    TODAS(1, "Todas as Situações");
    
    int valor;
    String descricao;

    FiltroSituacaoDisciplina(int valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static FiltroSituacaoDisciplina getEnum(int  valor) {
        FiltroSituacaoDisciplina[] valores = values();
        for (FiltroSituacaoDisciplina obj : valores) {
            if (obj.getValor() ==valor) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(int valor) {
        FiltroSituacaoDisciplina obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return "";
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
