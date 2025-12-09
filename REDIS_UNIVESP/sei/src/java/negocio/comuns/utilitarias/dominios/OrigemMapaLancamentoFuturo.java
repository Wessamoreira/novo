/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum OrigemMapaLancamentoFuturo {

    PROVISAO_CUSTO("PC", "Provisão de Custo"),
    MOVIMENTACAO_FINANCEIRA("MF", "Movimentação Financeira"),
    DEVOLUCAO_CHEQUE("DC", "Devolução de Cheque"),
    NEGOCIACAO_PAGAMENTO("NP", "Negociação de Pagamento"),
    MATRICULA("MA","Matrícula");
    
    String valor;
    String descricao;

    OrigemMapaLancamentoFuturo(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static OrigemMapaLancamentoFuturo getEnum(String valor) {
        OrigemMapaLancamentoFuturo[] valores = values();
        for (OrigemMapaLancamentoFuturo obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        OrigemMapaLancamentoFuturo obj = getEnum(valor);
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
