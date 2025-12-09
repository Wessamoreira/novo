package negocio.comuns.financeiro.enumerador;

import static java.util.Arrays.asList;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum TipoLancamentoContaPagarEnum {
	
	
	CREDITO_CONTA_CORRENTE(asList("033","237","104", "756","341"), "01", "Crédito em Conta Corrente"), 
	TRANSFERENCIA_OUTRO_BANCO(asList("033","237","104" ,"756","341"), "03", "Transferências para outros bancos (DOC, TED CIP e TED STR)"), 
	CREDITO_CONTA_POUPANCA(asList("033","237","104" ,"756","341"), "05", "Crédito em Conta Poupança"), 
	CREDITO_CONTA_CORRENTE_MESMA_TITULARIDADE(asList("341"), "06", "Crédito em Conta Corrente Mesma Titularidade"), 
	PAGAMENTO_CONTAS_TRIBUTOS_COM_CODIGO_BARRA(asList("033","237" ,"756"), "11", " Pagamento de Contas e Tributos com Código de Barras"),
	PAGAMENTO_CONTAS_CONCES_TRIBUTOS_COM_CODIGO_BARRA(asList("341"), "13", " Pagamento de Contas Concessionárias e Tributos com Código de Barras"), 
	DARF_NORMAL_SEM_CODIGO_BARRA(asList("033","237" ,"756","341"), "16", "DARF Normal / sem código de barras"),
	GPS_SEM_CODIGO_BARRA(asList("033","237", "756","341"), "17", " GPS / Guia da Previdência Social / sem código de barras"),
	DARF_SIMPLES_SEM_CODIGO_BARRA(asList("033","237" ,"756","341"), "18", "DARF Simples / sem código de barras"),
	LIQUIDACAO_TITULO_PROPRIO_BANCO(asList("033","237","104","756","341"),"30", "Liquidação de Títulos do Próprio Banco"),
	LIQUIDACAO_TITULO_OUTRO_BANCO(asList("033","237","104","756","341"), "31", "Liquidação de títulos outros Bancos"),
	TED_OUTRA_TITULARIDADE(asList("237","104" ,"756","341"),"41", "TED - Outra Titularidade"),
	TED_MESMA_TITULARIDADE(asList("237","341"),"43", "TED - Mesma Titularidade"),
	PIXTRANSFERÊNCIA(asList("341","237"),"45", "Pix Transferência"),
	PIX_QRCODE(asList("341", "237"),"47", "Pix QR-CODE"),		
	ITAU_TRANSFERENCIA_DOC_D(asList("341"), "07", "Transferências para outros bancos Mesma Titularidade(DOC D)"),	
	
	
	//Depreciados
	CAIXA_ECONOMICA_CREDITO_CONTA_CORRENTE(asList("104"), "01", "Crédito em Conta Corrente"),
	CAIXA_ECONOMICA_DOC(asList("104"), "03", "DOC"),
	CAIXA_ECONOMICA_CREDITO_CONTA_POUPANCA(asList("104"), "05", "Crédito em Conta Poupança"),
	CAIXA_ECONOMICA_LIQUIDACAO_TITULO_PROPRIO_BANCO(asList("104"), "30", "Liquidação Títulos do Próprio Banco"),
	CAIXA_ECONOMICA_PAGAMENTO_TITULO_OUTRO_BANCO(asList("104"), "31", "Pagamento de Títulos de Outros Bancos"),
	CAIXA_ECONOMICA_TED(asList("104"), "41", "TED"),
	BRADESCO_CREDITOCONTA(asList("237"), "01", "Crédito em C/C"),	
	BRADESCO_CREDITOREALTIMECONTA(asList("237"), "05", "Crédito em C/C real time"),
	BRADESCO_DOCCOMP(asList("237"), "03", "DOC COMP"),
	BRADESCO_TED(asList("237"), "08", "TED"),
	BRADESCO_PGCONTAS_TRIBUTOS(asList("237"), "11", "Pagamento de Contas e Tributos com Código de Barras"),	
	BRADESCO_TITULOTERCEIROS(asList("237"), "31", "Títulos Terceiros"),
	LIQUIDACAO_TITULO_CARTEIRA_COBRANCA_SANTANDER(asList("033"), "30", " Liquidação de títulos em carteira de cobrança próprio Santander"),
	SICOOB_LIQUIDACAO_OUTRO_BANCO(asList("756"),"31", "Pagamento de Títulos de Outros Bancos"),
	SICOOB_LIQUIDACAO_TITULO_SICOOB(asList("756"),"30", "Liquidação de Títulos do Próprio Banco"),
	SICOOB_TED_OUTRA_TITULARIDADE(asList("756"),"41", "TED"),
	SICOOB_DARF_SIMPLES_SEM_CODIGO_BARRA(asList("756"),"18","Tributo - DARF Simples"),
	SICOOB_GPS_SEM_CODIGO_BARRA(asList("756"),"17","Tributo - Guia da Previdência Social"),
	SICOOB_DARF_NORMAL_SEM_CODIGO_BARRA(asList("756"),"16","Tributo - DARF Normal"),
	SICOOB_PAGAMENTO_CONTAS_TRIBUTOS_COM_CODIGO_BARRA(asList("756"),"11", " Pagamento de Contas e Tributos com Código de Barras"),
	SICOOB_CREDITO_CONTA_POUPANCA(asList("756"), "05", "Crédito em Conta Poupança"),
	SICOOB_DOC(asList("756"), "03", "DOC"),
	SICOOB_CREDITO_CONTA_CORRENTE(asList("756"), "01", "Crédito em Conta Corrente"),
	ITAU_CREDITO_CONTA_CORRENTE(asList("341"), "01", "Crédito em Conta Corrente no Itaú"), 
	ITAU_CREDITO_CONTA_POUPANCA(asList("341"), "05", "Crédito em Conta Poupança no Itaú"),	
	ITAU_DARF_NORMAL_SEM_CODIGO_BARRA(asList("341"), "16", "DARF Normal / sem código de barras"),
	ITAU_GPS_SEM_CODIGO_BARRA(asList("341"), "17", " GPS / Guia da Previdência Social / sem código de barras"),
	ITAU_DARF_SIMPLES_SEM_CODIGO_BARRA(asList("341"), "18", "DARF Simples / sem código de barras"),
	ITAU_LIQUIDACAO_TITULO_OUTRO_BANCO(asList("341"), "31", "Liquidação de títulos outros Bancos"),
	ITAU_LIQUIDACAO__TITULO_ITAU(asList("341"), "30", "Liquidação de Títulos do Próprio Banco Itaú"), 
	ITAU_TED_OUTRA_TITULARIDADE(asList("341"),"41", "TED outro Titular"),
	ITAU_TED_MESMA_TITULARIDADE(asList("341"),"43", "TED mesmo Titular"),
	ITAU_PIXTRANSFERÊNCIA(asList("341"),"45", "Pix Transferência"),
	ITAU_PIX_QRCODE(asList("341"),"47", "Pix QR-CODE"),
	ITAU_TRANSFERENCIA_DOC_C(asList("341"), "03", "Transferências para outros bancos (DOC C)");
	
	//CAIXA_AUTENTICACAO("033", "20", "Caixa - Autenticação"),
	//ORDEM_CREDITO_TELEPROCESSAMENTO("033", "35", " Ordem de Crédito por Teleprocessamento / OCT"),	
	//SICOOB_TED_MESMA_TITULARIDADE("756","43", "TED - Mesma Titularidade"),
	//ORDEM_PAGAMENTO("033", "10", " Ordem de Pagamento / Recibo"),	
	//GARE_ICMS_SEM_CODIGO_BARRA("033", "22", "GARE SP ICMS / sem código de barras"), 
	//GARE_DR_SEM_CODIGO_BARRA("033", "23", "GARE SP DR / sem código de barras"), 
	//GARE_ITCMD_SEM_CODIGO_BARRA("033", "24", "GARE SP ITCMD / sem código de barras"), 
	//IPVA_COM_RENAVAM("033", "25","IPVA SP e MG / Pagamento com o RENAVAM"), 
	//LICENCIAMENTO_COM_RENAVAM("033", "26", "LICENCIAMENTO SP e MG / Pagamento com o RENAVAM"), 
	//DPVAT_RENAVAM("033", "27", "DPVAT SP e MG / Pagamento com o RENAVAM"),  
	
	
	

	private final List<String> nrBancos;
	private final String valor;
	private final String descricao;

	private TipoLancamentoContaPagarEnum(List<String> nrBancos, String valor, String descricao) {
		this.nrBancos = nrBancos;		
		this.valor = valor;
		this.descricao = descricao;
	}

	public String getValor() {
		return this.valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public List<String> getNrBancos() {
		return this.nrBancos;
	}

	public boolean isTipoLancamentoPorNrBanco(String nrBanco) {
		return Uteis.isAtributoPreenchido(nrBanco) ? this.getNrBancos().stream().anyMatch(nrBanco::equals) : false;
	}
	
	

	public boolean isCreditoContaCorrente(){
		return equals(CREDITO_CONTA_CORRENTE) 
				|| equals(CREDITO_CONTA_CORRENTE_MESMA_TITULARIDADE) 
				|| equals(CAIXA_ECONOMICA_CREDITO_CONTA_CORRENTE) 
				|| equals(SICOOB_CREDITO_CONTA_CORRENTE)
				|| equals(ITAU_CREDITO_CONTA_CORRENTE);
	}
	
	public boolean isTransferencia(){
		return equals(TRANSFERENCIA_OUTRO_BANCO) 
				|| equals(TED_MESMA_TITULARIDADE) 
				|| equals(TED_OUTRA_TITULARIDADE) 
				|| equals(CAIXA_ECONOMICA_TED) 
				|| equals(CAIXA_ECONOMICA_DOC)
				|| equals(SICOOB_DOC)
				|| equals(SICOOB_TED_OUTRA_TITULARIDADE)
				|| equals(TipoLancamentoContaPagarEnum.ITAU_TRANSFERENCIA_DOC_C)
				|| equals(TipoLancamentoContaPagarEnum.ITAU_TRANSFERENCIA_DOC_D)			   
			    || equals(TipoLancamentoContaPagarEnum.ITAU_TED_OUTRA_TITULARIDADE)
		        || equals(TipoLancamentoContaPagarEnum.ITAU_TED_MESMA_TITULARIDADE)
		        ||equals(TipoLancamentoContaPagarEnum.PIXTRANSFERÊNCIA)  
				||  equals(TipoLancamentoContaPagarEnum.PIX_QRCODE);
	}
	
	
	public boolean isTransferenciaTed(){
		   return  equals(TED_MESMA_TITULARIDADE)||
			   equals(TED_OUTRA_TITULARIDADE)    || 
			   equals(CAIXA_ECONOMICA_TED)	     ||			 
			   equals(SICOOB_TED_OUTRA_TITULARIDADE);
		   }
	
	
	
	public boolean isTransferenciaDoc(){
		return equals(TRANSFERENCIA_OUTRO_BANCO) ||			 
			   equals(CAIXA_ECONOMICA_DOC)  	 || 
			   equals(BRADESCO_DOCCOMP)          || 
			   equals(SICOOB_DOC);	
	}
	
	public boolean isCreditoContaPoupanca(){
		return equals(CREDITO_CONTA_POUPANCA) 
				|| equals(CAIXA_ECONOMICA_CREDITO_CONTA_POUPANCA) 
				|| equals(SICOOB_CREDITO_CONTA_POUPANCA)
				|| equals(ITAU_CREDITO_CONTA_POUPANCA);
	}
	
	public boolean isOrdemPagamento(){
		return false;
	}
	
	public boolean isPagamentoContasTributosComCodigoBarra(){
		return equals(PAGAMENTO_CONTAS_TRIBUTOS_COM_CODIGO_BARRA) 
				|| equals(BRADESCO_PGCONTAS_TRIBUTOS) 
				|| equals(SICOOB_PAGAMENTO_CONTAS_TRIBUTOS_COM_CODIGO_BARRA)
				|| equals(PAGAMENTO_CONTAS_CONCES_TRIBUTOS_COM_CODIGO_BARRA);
	}
	
	public boolean isDarfNormalSemCodigoBarra(){
		return equals(DARF_NORMAL_SEM_CODIGO_BARRA) 
				|| equals(SICOOB_DARF_NORMAL_SEM_CODIGO_BARRA)
				|| equals(ITAU_DARF_NORMAL_SEM_CODIGO_BARRA) ;
	}
	
	public boolean isGpsSemCodigoBarra(){
		return equals(GPS_SEM_CODIGO_BARRA)
				|| equals(SICOOB_GPS_SEM_CODIGO_BARRA)
				|| equals(ITAU_GPS_SEM_CODIGO_BARRA);
	}
	
	public boolean isDarfSimplesSemCodigoBarra(){
		return equals(DARF_SIMPLES_SEM_CODIGO_BARRA)
				|| equals(SICOOB_DARF_SIMPLES_SEM_CODIGO_BARRA)
				|| equals(ITAU_DARF_SIMPLES_SEM_CODIGO_BARRA);
	}
	
	public boolean isCaixaAutenticacao(){
		//return name().equals(TipoLancamentoContaPagarEnum.CAIXA_AUTENTICACAO.name());
		return false;
	}
	
	public boolean isGareIcmsSemCodigoBarra(){
		//return name().equals(TipoLancamentoContaPagarEnum.GARE_ICMS_SEM_CODIGO_BARRA.name());
		return false;
	}
	
	public boolean isGareDrSemCodigoBarra(){
		//return name().equals(TipoLancamentoContaPagarEnum.GARE_DR_SEM_CODIGO_BARRA.name());
		return false;
	}
	
	public boolean isGareItcmdSemCodigoBarra(){
		//return name().equals(TipoLancamentoContaPagarEnum.GARE_ITCMD_SEM_CODIGO_BARRA.name());
		return false;
	}
	
	public boolean isIpvaComRenavam(){
		//return name().equals(TipoLancamentoContaPagarEnum.IPVA_COM_RENAVAM.name());
		return false;
	}
	
	public boolean isLicenciamentoComRenavam(){
		//return name().equals(TipoLancamentoContaPagarEnum.LICENCIAMENTO_COM_RENAVAM.name());
		return false;
	}
	
	public boolean isDpvatRenavam(){
		//return name().equals(TipoLancamentoContaPagarEnum.DPVAT_RENAVAM.name());
		return false;
	}
	
	public boolean isLiquidacaoTituloCarteiraCobrancaSantander(){
		return equals(LIQUIDACAO_TITULO_CARTEIRA_COBRANCA_SANTANDER);
	}
	
	public boolean isLiquidacaoTituloCarteiraCobrancaSicoob() {
		return equals(SICOOB_LIQUIDACAO_TITULO_SICOOB);
	}
		
	public boolean isLiquidacaoTituloOutroBanco(){
		return equals(LIQUIDACAO_TITULO_OUTRO_BANCO) 
				|| equals(CAIXA_ECONOMICA_LIQUIDACAO_TITULO_PROPRIO_BANCO) 
				|| equals(CAIXA_ECONOMICA_PAGAMENTO_TITULO_OUTRO_BANCO)
				|| equals(ITAU_LIQUIDACAO_TITULO_OUTRO_BANCO)
				|| equals(SICOOB_LIQUIDACAO_OUTRO_BANCO);
	}
	
	public boolean isLiquidacaoTituloProprioBanco(){
		return equals(LIQUIDACAO_TITULO_PROPRIO_BANCO);
	}
	
	public boolean isOrdemCreditoTeleprocessamento(){
		//return name().equals(TipoLancamentoContaPagarEnum.ORDEM_CREDITO_TELEPROCESSAMENTO.name());
		return false;
	}

	public boolean isPagamentoSalario() {
		return equals(CAIXA_ECONOMICA_TED);
	}
	
	public boolean isPixTransferencia(){
		 return  equals(TipoLancamentoContaPagarEnum.PIXTRANSFERÊNCIA)  
				 ||  equals(TipoLancamentoContaPagarEnum.PIX_QRCODE);
				 
	
		
	}
	
	
	public boolean isDepreciado() {
		return  equals(CAIXA_ECONOMICA_CREDITO_CONTA_CORRENTE) ||
				equals(CAIXA_ECONOMICA_DOC) ||
				equals(CAIXA_ECONOMICA_CREDITO_CONTA_POUPANCA) ||
				equals(CAIXA_ECONOMICA_LIQUIDACAO_TITULO_PROPRIO_BANCO) ||
				equals(CAIXA_ECONOMICA_PAGAMENTO_TITULO_OUTRO_BANCO) ||
				equals(CAIXA_ECONOMICA_TED) ||
				equals(BRADESCO_CREDITOCONTA) ||	
				equals(BRADESCO_CREDITOREALTIMECONTA) ||
				equals(BRADESCO_DOCCOMP) ||
				equals(BRADESCO_TED) ||
				equals(BRADESCO_PGCONTAS_TRIBUTOS) ||	
				equals(BRADESCO_TITULOTERCEIROS) ||
				equals(LIQUIDACAO_TITULO_CARTEIRA_COBRANCA_SANTANDER) ||
				equals(SICOOB_LIQUIDACAO_OUTRO_BANCO) ||
				equals(SICOOB_LIQUIDACAO_TITULO_SICOOB) ||
				equals(SICOOB_TED_OUTRA_TITULARIDADE) ||
				equals(SICOOB_DARF_SIMPLES_SEM_CODIGO_BARRA) ||
				equals(SICOOB_GPS_SEM_CODIGO_BARRA) ||
				equals(SICOOB_DARF_NORMAL_SEM_CODIGO_BARRA) ||
				equals(SICOOB_PAGAMENTO_CONTAS_TRIBUTOS_COM_CODIGO_BARRA) ||
				equals(SICOOB_CREDITO_CONTA_POUPANCA) ||
				equals(SICOOB_DOC) ||
				equals(SICOOB_CREDITO_CONTA_CORRENTE) ||
				equals(ITAU_CREDITO_CONTA_CORRENTE) ||
				equals(ITAU_CREDITO_CONTA_POUPANCA) ||
				equals(ITAU_DARF_NORMAL_SEM_CODIGO_BARRA) ||
				equals(ITAU_GPS_SEM_CODIGO_BARRA) ||
				equals(ITAU_DARF_SIMPLES_SEM_CODIGO_BARRA) ||
				equals(ITAU_LIQUIDACAO_TITULO_OUTRO_BANCO) ||
				equals(ITAU_LIQUIDACAO__TITULO_ITAU) ||
				equals(ITAU_TED_OUTRA_TITULARIDADE) ||
				equals(ITAU_TED_MESMA_TITULARIDADE) ||
				equals(ITAU_PIXTRANSFERÊNCIA) ||
				equals(ITAU_PIX_QRCODE) ||
				equals(ITAU_TRANSFERENCIA_DOC_C);
	}
	 
	
	
	
}
