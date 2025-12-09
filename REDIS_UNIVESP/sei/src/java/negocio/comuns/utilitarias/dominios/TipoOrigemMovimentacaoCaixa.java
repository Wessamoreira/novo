/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoOrigemMovimentacaoCaixa {

    MOVIMENTACAO_FINANCEIRA("MF", "Movimentação Financeira"),
    PAGAMENTO("PA", "Pagamento"),
    RECEBIMENTO("RE", "Recebimento"),
    TROCO("TR", "Troco"),
    RENEGOCIACAO_RECEBER("RCR", "Reneg. Conta Receber"),
    DEVOLUCAO_CHEQUE("DC", "Devolução de Cheque"),
    DEVOLUCAO_PAGAMENTO_ANTECIPADO("DPA", "Devolução de Pagamento Antecipado"),
    DEVOLUCAO_RECEBIMENTO_ANTECIPADO("DRA", "Devolução de Recebimento Antecipado"),
    PROVISAO_CUSTO("PC", "Provisão de Custo"),
    ESTORNO_CHEQUE("EC", "Estorno de Cheque"),
    ESTORNO_CARTAO("ECR", "Estorno de Cartão");
    
    String valor;
    String descricao;

    TipoOrigemMovimentacaoCaixa(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public static TipoOrigemMovimentacaoCaixa getEnum(String valor) {
        TipoOrigemMovimentacaoCaixa[] valores = values();
        for (TipoOrigemMovimentacaoCaixa obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoOrigemMovimentacaoCaixa obj = getEnum(valor);
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
