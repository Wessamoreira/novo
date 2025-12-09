package negocio.comuns.contabil.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoOrigemHistoricoBloqueioEnum {
	ARECEBER, RECEBIMENTO, APAGAR, PAGAMENTO, NFENTRADA, NFSAIDA, MOVIMENTACAOFINANCEIRA, CAIXA, FECHAMENTOMES;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoOrigemHistoricoBloqueioEnum_"+this.name());
	}
	
	public boolean isAReceber(){
		return name().equals(TipoOrigemHistoricoBloqueioEnum.ARECEBER.name());
	}
	
	public boolean isRecebimento(){
		return name().equals(TipoOrigemHistoricoBloqueioEnum.RECEBIMENTO.name());
	}	
	
	public boolean isPagamento(){
		return name().equals(TipoOrigemHistoricoBloqueioEnum.PAGAMENTO.name());
	}
	
}
