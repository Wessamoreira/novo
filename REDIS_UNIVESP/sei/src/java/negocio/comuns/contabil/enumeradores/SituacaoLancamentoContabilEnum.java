package negocio.comuns.contabil.enumeradores;

/**
 * 
 * @author PedroOtimize
 *
 */
public enum SituacaoLancamentoContabilEnum {
	COMPENSADO,
	CONTABILIZADO,
	AGUARDANDO_COMPENSACAO;
	
	public boolean isContabilizado(){
		return name().equals(SituacaoLancamentoContabilEnum.CONTABILIZADO.name());
	}
	
	public boolean isCompensado(){
		return name().equals(SituacaoLancamentoContabilEnum.COMPENSADO.name());
	}
	
	public boolean isAguardandoCompensacao(){
		return name().equals(SituacaoLancamentoContabilEnum.AGUARDANDO_COMPENSACAO.name());
	}
	

}
