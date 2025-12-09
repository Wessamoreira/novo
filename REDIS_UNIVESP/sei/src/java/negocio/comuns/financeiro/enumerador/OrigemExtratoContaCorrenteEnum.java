package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.Uteis;

public enum OrigemExtratoContaCorrenteEnum {

    PAGAMENTO("Pagamento"), 
    RECEBIMENTO("Recebimento"),
    COMPENSACAO_CHEQUE("Compensação de Cheque"), 
    COMPENSACAO_CARTAO("Compensação Cartão"), 
    MOVIMENTACAO_FINANCEIRA("Movimentação Financeira"), 
    DEVOLUCAO_CHEQUE("Devolução de Cheque"),
    MANUAL("Registro Manual");
    
    String descricao;
    
    OrigemExtratoContaCorrenteEnum(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public boolean isPagamento(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemExtratoContaCorrenteEnum.PAGAMENTO.name());
    }
    
    public boolean isCompensacaoCheque(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemExtratoContaCorrenteEnum.COMPENSACAO_CHEQUE.name());
    }
    
    public boolean isCompensacaoCartao(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemExtratoContaCorrenteEnum.COMPENSACAO_CARTAO.name());
    }
    
    public boolean isRecebimento(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemExtratoContaCorrenteEnum.RECEBIMENTO.name());
    }
    
    public boolean isMovimentacaoFinanceira(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA.name());
    }
    
    
    
}
