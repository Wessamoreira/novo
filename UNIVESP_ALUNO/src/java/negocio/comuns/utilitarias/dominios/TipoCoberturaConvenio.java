/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoCoberturaConvenio {

    COLABORADOR_E_DEPENDENTE("CD", "Colaborador e Dependentes"),
    COLABORADOR("CO", "Colaborador"),
    INDICADO("ID", "Indicado");
    
    String valor;
    String descricao;

    TipoCoberturaConvenio(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoCoberturaConvenio getEnum(String valor) {
        TipoCoberturaConvenio[] valores = values();
        for (TipoCoberturaConvenio obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoCoberturaConvenio obj = getEnum(valor);
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
