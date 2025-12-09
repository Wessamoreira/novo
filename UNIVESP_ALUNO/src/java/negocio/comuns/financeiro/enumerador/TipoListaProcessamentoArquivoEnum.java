package negocio.comuns.financeiro.enumerador;

public enum TipoListaProcessamentoArquivoEnum {
	NENHUM, CONTAS_RECEBER, CONTAS_RECEBIDAS, CONTAS_VALORES_DIVERGENTE, NAO_LOCALIZADA_ARQUIVO, NAO_LOCALIZADA_SISTEMA;

	public boolean isNenhum() {
		return name().equals(TipoListaProcessamentoArquivoEnum.NENHUM.name());
	}
	public boolean isContaReceber() {
		return name().equals(TipoListaProcessamentoArquivoEnum.CONTAS_RECEBER.name());
	}

	public boolean isContaRecebidas() {
		return name().equals(TipoListaProcessamentoArquivoEnum.CONTAS_RECEBIDAS.name());
	}

	public boolean isContaValoresDivergente() {
		return name().equals(TipoListaProcessamentoArquivoEnum.CONTAS_VALORES_DIVERGENTE.name());
	}

	public boolean isNaoLocalizadaArquivo() {
		return name().equals(TipoListaProcessamentoArquivoEnum.NAO_LOCALIZADA_ARQUIVO.name());
	}
	
	public boolean isNaoLocalizadaSistema() {
		return name().equals(TipoListaProcessamentoArquivoEnum.NAO_LOCALIZADA_SISTEMA.name());
	}
}
