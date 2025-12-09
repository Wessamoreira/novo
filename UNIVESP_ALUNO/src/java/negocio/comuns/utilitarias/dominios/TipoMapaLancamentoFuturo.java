/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.utilitarias.dominios;

/**
 *
 * @author Diego
 */
public enum TipoMapaLancamentoFuturo {

    PROVISAO_CUSTO("PC", "Provisão de Custo", "Provisões de Custo", "abaProvisaoCusto"),
    CHEQUE_A_RECEBER("CR", "Cheque a Receber", "Cheques a Receber", "abaChequesAReceber"),
    CHEQUE_DEVOLVIDO("CD", "Cheque devolvido", "Cheques Devolvidos", "abaChequesDevolvidos"),
    CHEQUE_A_PAGAR("CP", "Cheque à Pagar", "Cheques à Pagar", "abaChequesAPagar"),
    CONTA_PAGAR("DP", "Contas a Pagar", "Contas a Pagar", "contaPagarCons"),
    CARTOES_A_RECEBER("CC", "Cartões de Crédito", "Cartões de Crédito", "mapaPendenciaCartaoCredito"),
    MOVIMENTACAO_FINANCEIRA("MF", "Movimentações Financeiras", "Movimentações Financeiras", "mapaMovimentacaoFinanceira"),
    MATRICULA("MA", "Matrícula", "Matrículas", "abaMatricula");

    String valor;
    String descricao;
    String descricaoPlural;
    String metodoReferenteAoTipo;

    TipoMapaLancamentoFuturo(String valor, String descricao, String descricaoPlural, String metodoReferenteAoTipo) {
        this.valor = valor;
        this.descricao = descricao;
        this.descricaoPlural = descricaoPlural;
        this.metodoReferenteAoTipo = metodoReferenteAoTipo;
    }

    public static TipoMapaLancamentoFuturo getEnum(String valor) {
        TipoMapaLancamentoFuturo[] valores = values();
        for (TipoMapaLancamentoFuturo obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        TipoMapaLancamentoFuturo obj = getEnum(valor);
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

	public String getDescricaoPlural() {
		return descricaoPlural;
	}

	public void setDescricaoPlural(String descricaoPlural) {
		this.descricaoPlural = descricaoPlural;
	}

	public String getMetodoReferenteAoTipo() {
		return metodoReferenteAoTipo;
	}

	public void setMetodoReferenteAoTipo(String metodoReferenteAoTipo) {
		this.metodoReferenteAoTipo = metodoReferenteAoTipo;
	}

}
