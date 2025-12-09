package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoAlteracaoPlanoFinanceiroEnum {

	COM_PARCELA_GERADA("COM_PARCELA_GERADA", "enum_SituacaoAlteracaoPlanoFinanceiro_COM_PARCELA_GERADA"), 
	VALOR_INFERIOR_NOVO_PLANO("VALOR_INFERIOR_NOVO_PLANO", "enum_SituacaoAlteracaoPlanoFinanceiro_VALOR_INFERIOR_NOVO_PLANO"), 
	CONDICAO_PAGAMENTO_NAO_ENCONTRADO("CONDICAO_PAGAMENTO_NAO_ENCONTRADO", "enum_SituacaoAlteracaoPlanoFinanceiro_CONDICAO_PAGAMENTO_NAO_ENCONTRADO"), 
	COM_UMA_CONDICAO_PAGAMENTO("COM_UMA_CONDICAO_PAGAMENTO", "enum_SituacaoAlteracaoPlanoFinanceiro_COM_UMA_CONDICAO_PAGAMENTO"), 	
	CONDICAO_PAGAMENTO_EM_DUPLICIDADE("CONDICAO_PAGAMENTO_EM_DUPLICIDADE", "enum_SituacaoAlteracaoPlanoFinanceiro_CONDICAO_PAGAMENTO_EM_DUPLICIDADE"),
	CONDICAO_PAGAMENTO_EM_CONFORMIDADE("CONDICAO_PAGAMENTO_EM_CONFORMIDADE", "enum_SituacaoAlteracaoPlanoFinanceiro_CONDICAO_PAGAMENTO_EM_CONFORMIDADE");
	
    String valor;
    String descricao;

    SituacaoAlteracaoPlanoFinanceiroEnum(String valor, String descricao) {
        this.valor = valor;
        this.descricao = descricao;
    }

    public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_SituacaoControleRemessaContaReceberEnum_"+this.name());
	}
    
    public static SituacaoAlteracaoPlanoFinanceiroEnum getEnum(String valor) {
    	SituacaoAlteracaoPlanoFinanceiroEnum[] valores = values();
        for (SituacaoAlteracaoPlanoFinanceiroEnum obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
    	SituacaoAlteracaoPlanoFinanceiroEnum obj = getEnum(valor);
        if (obj != null) {
            return UteisJSF.internacionalizar(obj.getDescricao());
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
        return UteisJSF.internacionalizar(descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }	
}