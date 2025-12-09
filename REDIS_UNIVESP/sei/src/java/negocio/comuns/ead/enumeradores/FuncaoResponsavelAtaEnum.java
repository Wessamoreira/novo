package negocio.comuns.ead.enumeradores;


import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Victor Hugo de Paula Costa - 30 de jun de 2016
 *
 */
public enum FuncaoResponsavelAtaEnum {
	/**
	 * @author Victor Hugo de Paula Costa - 30 de jun de 2016 
	 */
	PALESTRANTE, MEDIADOR, REDATOR;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_FuncaoResponsavelAtaEnum_" + this.name());
	}
	
	public String getName() {
		return this.name();
	}
	
	public boolean isFuncaoPalestrante() {
		return getName() != null && getName().equals(FuncaoResponsavelAtaEnum.PALESTRANTE.name()) ? true : false;
	}
	
	public boolean isFuncaoMediador() {
		return getName() != null && getName().equals(FuncaoResponsavelAtaEnum.MEDIADOR.name()) ? true : false;
	}
	
	public boolean isFuncaoRedator() {
		return getName() != null && getName().equals(FuncaoResponsavelAtaEnum.REDATOR.name()) ? true : false;
	}
}
