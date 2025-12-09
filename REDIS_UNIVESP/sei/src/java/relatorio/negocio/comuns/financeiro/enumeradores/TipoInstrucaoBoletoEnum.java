package relatorio.negocio.comuns.financeiro.enumeradores;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum TipoInstrucaoBoletoEnum {
	DESCONTO, 
	PLANO_DESCONTO_COM_VALORES_DIFERENTE, 
	PLANO_DESCONTO_SEM_VALORES_DIFERENTE, 
	SEM_DESCONTO_PROGRESSIVO_SEM_VALIDADE_COM_VALORES_DIFERENTE,
	SEM_DESCONTO_PROGRESSIVO_SEM_VALIDADE_SEM_VALORES_DIFERENTE,
	SEM_DESCONTO_PROGRESSIVO_COM_VALIDADE_COM_VALORES_DIFERENTE,
	SEM_DESCONTO_PROGRESSIVO_COM_VALIDADE_SEM_VALORES_DIFERENTE;
	
	public boolean isDesconto(){
		return name().equals(TipoInstrucaoBoletoEnum.DESCONTO.name());
	}
	
	public boolean isPlanoDescontoComValoresDiferente(){
		return name().equals(TipoInstrucaoBoletoEnum.PLANO_DESCONTO_COM_VALORES_DIFERENTE.name());
	}
	
	public boolean isPlanoDescontoSemValoresDiferente(){
		return name().equals(TipoInstrucaoBoletoEnum.PLANO_DESCONTO_SEM_VALORES_DIFERENTE.name());
	}
	
	public boolean isSemDescontoProgressivoSemValidadeComValoresDiferentes(){
		return name().equals(TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_SEM_VALIDADE_COM_VALORES_DIFERENTE.name());
	}
	
	public boolean isSemDescontoProgressivoSemValidadeSemValoresDiferentes(){
		return name().equals(TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_SEM_VALIDADE_SEM_VALORES_DIFERENTE.name());
	}
	
	public boolean isSemDescontoProgressivoComValidadeComValoresDiferentes(){
		return name().equals(TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_COM_VALIDADE_COM_VALORES_DIFERENTE.name());
	}
	
	public boolean isSemDescontoProgressivoComValidadeSemValoresDiferentes(){
		return name().equals(TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_COM_VALIDADE_SEM_VALORES_DIFERENTE.name());
	}

}
