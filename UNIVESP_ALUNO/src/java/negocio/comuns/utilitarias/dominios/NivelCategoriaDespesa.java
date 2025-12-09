/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum NivelCategoriaDespesa {

    UNIDADE_ENSINO("UE", "Unidade de Ensino"),
    DEPARTAMENTO("DE", "Departamento"),
    FUNCIONARIO("FU", "Funcionário");
    
    String valor;
    String descricao;

    NivelCategoriaDespesa(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static NivelCategoriaDespesa getEnum(String valor) {
        NivelCategoriaDespesa[] valores = values();
        for (NivelCategoriaDespesa obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        NivelCategoriaDespesa obj = getEnum(valor);
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
