package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Victor Hugo de Paula Costa - 30 de jun de 2016
 *
 */
public enum SituacaoPBLEnum {
	/**
	 * @author Victor Hugo de Paula Costa - 30 de jun de 2016 
	 */
	LIBERADO, PENDENTE, REALIZADO;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_SituacaoPBLEnum_" + this.name());
	}
	
	public String getName() {
		return this.name();
	}
	
	public boolean isLiberado() {
		return name().equals(SituacaoPBLEnum.LIBERADO.name()) ? true : false;
	}
	
	public boolean isPendente() {
		return name().equals(SituacaoPBLEnum.PENDENTE.name()) ? true : false;
	}
	
	public boolean isRealizado() {
		return name().equals(SituacaoPBLEnum.REALIZADO.name()) ? true : false;
	}
}
