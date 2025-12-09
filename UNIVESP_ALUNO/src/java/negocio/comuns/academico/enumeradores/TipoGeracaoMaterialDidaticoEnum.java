package negocio.comuns.academico.enumeradores;

/*
 * Pedro Andrade
 */
public enum TipoGeracaoMaterialDidaticoEnum {
	
	NENHUM, ANTES_PARCELA_ACADEMICO, JUNTO_PARCELA_ACADEMICO;
	
	public boolean isAntesParcelaAcademico(){
		return this.name() != null && this.name().equals(TipoGeracaoMaterialDidaticoEnum.ANTES_PARCELA_ACADEMICO.name()); 
	}
	public boolean isJuntoParcelaAcademico(){
		return this.name() != null && this.name().equals(TipoGeracaoMaterialDidaticoEnum.JUNTO_PARCELA_ACADEMICO.name()); 
	}

}
