package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoProcessamentoGeracaoManualParcelaEnum {

	TODAS, AGUARDANDO_PROCESSAMENTO, EM_PROCESSAMENTO, PROCESSAMENTO_CONCLUIDO, ERRO_PROCESSAMENTO;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_SituacaoProcessamentoGeracaoManualParcelaEnum_"+this.name());
	}
}
