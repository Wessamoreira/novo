/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum SituacaoAcademicaProgramacaoFormaturaAluno {

    DISCIPLINAS_OK("OK", "Integralizado"),
    DISCIPLINAS_PENDENTES("PE", "Não Integralizado");
    
    String valor;
    String descricao;

    SituacaoAcademicaProgramacaoFormaturaAluno(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static SituacaoAcademicaProgramacaoFormaturaAluno getEnum(String valor) {
        SituacaoAcademicaProgramacaoFormaturaAluno[] valores = values();
        for (SituacaoAcademicaProgramacaoFormaturaAluno obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        SituacaoAcademicaProgramacaoFormaturaAluno obj = getEnum(valor);
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
