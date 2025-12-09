package negocio.comuns.financeiro.enumerador;
/**
 * 
 * @author Pedro Otimize
 *
 */
public enum SituacaoConcialiacaoContaCorrenteEnum {
	ABERTA, FINALIZADA;
	
	
	public boolean isAberta(){
		return name().equals(SituacaoConcialiacaoContaCorrenteEnum.ABERTA.name());
	}
	
	public boolean isFinalizada(){
		return name().equals(SituacaoConcialiacaoContaCorrenteEnum.FINALIZADA.name());
	}

}
