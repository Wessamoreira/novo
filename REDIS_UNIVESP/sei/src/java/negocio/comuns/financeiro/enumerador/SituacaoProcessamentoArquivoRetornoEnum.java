package negocio.comuns.financeiro.enumerador;

import negocio.comuns.utilitarias.UteisJSF;

public enum SituacaoProcessamentoArquivoRetornoEnum {
	AGUARDANDO_PROCESSAMENTO, EM_PROCESSAMENTO, PROCESSAMENTO_INTERROMPIDO, ERRO_PROCESSAMENTO, PROCESSAMENTO_CONCLUIDO, ARQUIVO_PROCESSADO_CONTAS_NAO_BAIXADAS, PROCESSAMENTO_COM_ERRO;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_SituacaoProcessamentoArquivoRetornoEnum_"+this.name());
	}
	
	public boolean isAguardandoProcessamento() {
		return this.name().equals(SituacaoProcessamentoArquivoRetornoEnum.AGUARDANDO_PROCESSAMENTO.name());
	}
	
	public boolean isEmProcessamento() {
		return this.name().equals(SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO.name());
	}
	
	public boolean isProcessamentoConcluido() {
		return this.name().equals(SituacaoProcessamentoArquivoRetornoEnum.PROCESSAMENTO_CONCLUIDO.name());
	}
	
	public boolean isErroProcessamento() {
		return this.name().equals(SituacaoProcessamentoArquivoRetornoEnum.ERRO_PROCESSAMENTO.name());
	}
	
	public boolean isInterrompidoProcessamento() {
		return this.name().equals(SituacaoProcessamentoArquivoRetornoEnum.PROCESSAMENTO_INTERROMPIDO.name());
	}
	
	public boolean isArquivoProcessado() {
		return this.name().equals(SituacaoProcessamentoArquivoRetornoEnum.ARQUIVO_PROCESSADO_CONTAS_NAO_BAIXADAS.name());
	}

	public boolean isProcessadoComErro() {
		return this.name().equals(SituacaoProcessamentoArquivoRetornoEnum.PROCESSAMENTO_COM_ERRO.name());
	}
	
}
