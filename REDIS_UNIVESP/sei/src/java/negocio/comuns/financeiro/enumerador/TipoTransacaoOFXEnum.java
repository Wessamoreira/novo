package negocio.comuns.financeiro.enumerador;

/**
 * 
 * @author Pedro Otimize
 *
 */
public enum TipoTransacaoOFXEnum {
	CREDITO("CREDIT", "Crédito"), 
	DEBITO("DEBIT", "Débito"), 
	JURO_VENCIDO_PAGOS ("INT", "Juros vencidos ou pagos"), 
	DIVIDENDO("DIV", "Dividendo"), 
	TAXA_FI("FEE", "Taxa FI"), 
	TAXA_SERVICO("SRVCHG", "Taxa de serviço"), 
	DEPOSITO("DEP", "Depósito"), 
	DEBITO_CREDITO_ATM("ATM", "Débito ou crédito ATM"), 
	PONTO_VENDA_CREDITO_DEBITO("POS", "Ponto de venda de débito ou crédito"), 
	TRANSFERIR("XFER", "Transferir"), 
	VERIFICAR("CHECK", "VERIFICAR"),   
	PAGAMENTO_ELETRONICO("PAYMENT", "Pagamento eletrônico"), 
	RETIRADA_DINHEIRO("CASH", "Retirada de dinheiro"), 
	DEPOSITO_DIRETO("DIRECTDEP", "Depósito direto"), 
	DEBITO_INICIADO_COMERCIANTE("DIRECTDEBIT", "Débito iniciado pelo comerciante"), 
	REPETINDO_PAGAMENTO("REPEATPMT", "Repetindo pagamento / ordem permanente"), 
	OUTROS("OTHER", "Outros");
	
	String valor;
    String descricao;
	
	TipoTransacaoOFXEnum(String valor, String descricao) {
		this.valor = valor;
	    this.descricao = descricao;
	}
	
	 public static TipoTransacaoOFXEnum getEnum(String valor) {
		 TipoTransacaoOFXEnum[] valores = values();
	        for (TipoTransacaoOFXEnum obj : valores) {
	            if (obj.getValor().equals(valor)) {
	                return obj;
	            }
	        }
	        return null;
	    }
	
	public String getValor() {
        if (valor == null) {
            valor = "";
        }
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public boolean isTipoDebito(){
    	return name().equals(TipoTransacaoOFXEnum.DEBITO.name());
    }
    
    public boolean isTipoCredito(){
    	return name().equals(TipoTransacaoOFXEnum.CREDITO.name());
    }
    
    public boolean isTipoDeposito(){
    	return name().equals(TipoTransacaoOFXEnum.DEPOSITO.name());
    }
    
    public boolean isTipoJuroVencidoPagos(){
    	return name().equals(TipoTransacaoOFXEnum.JURO_VENCIDO_PAGOS.name());
    }
    
    public boolean isTipoDividendo(){
    	return name().equals(TipoTransacaoOFXEnum.DIVIDENDO.name());
    }
    
    public boolean isTipoTaxaFinanceira(){
    	return name().equals(TipoTransacaoOFXEnum.TAXA_FI.name());
    }
    
    public boolean isTipoTaxaServico(){
    	return name().equals(TipoTransacaoOFXEnum.TAXA_SERVICO.name());
    }
    
    public boolean isTipoDebitoCreditoAtm(){
    	return name().equals(TipoTransacaoOFXEnum.DEBITO_CREDITO_ATM.name());
    }
    
    public boolean isTipoPontoVendaCreditoDebito(){
    	return name().equals(TipoTransacaoOFXEnum.PONTO_VENDA_CREDITO_DEBITO.name());
    }
    
    public boolean isTipoTransferir(){
    	return name().equals(TipoTransacaoOFXEnum.TRANSFERIR.name());
    }
    
    public boolean isTipoVerificar(){
    	return name().equals(TipoTransacaoOFXEnum.VERIFICAR.name());
    }
    
    public boolean isTipoPagamentoEletronico(){
    	return name().equals(TipoTransacaoOFXEnum.PAGAMENTO_ELETRONICO.name());
    }
    
    public boolean isTipoRetiradaDinheiro(){
    	return name().equals(TipoTransacaoOFXEnum.RETIRADA_DINHEIRO.name());
    }
    
    public boolean isTipoDepositoDireto(){
    	return name().equals(TipoTransacaoOFXEnum.DEPOSITO_DIRETO.name());
    }
    
    public boolean isTipoRepetindoPagamento(){
    	return name().equals(TipoTransacaoOFXEnum.REPETINDO_PAGAMENTO.name());
    }
    
    public boolean isTipoOutros(){
    	return name().equals(TipoTransacaoOFXEnum.OUTROS.name());
    }
    

}
