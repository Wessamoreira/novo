package negocio.comuns.arquitetura.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * 
 * @author Pedro Andrade
 *
 */
public enum TipoDesigneTextoEnum {
	HTML, PDF;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoDesigneTextoEnum_" + this.name());
	}
	
	public boolean isHtml() {
		return equals(HTML);
	}
	
	public boolean isPdf() {
		return equals(PDF);
	}

}
