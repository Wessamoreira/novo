package negocio.comuns.faturamento.nfe.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Victor Hugo de Paula Costa - 1 de set de 2016
 *
 */
public enum CSTPISCOFINSEnum {
	/**
	 * @author Victor Hugo de Paula Costa - 1 de set de 2016 
	 */
	UM, DOIS, QUATRO, CINCO, SEIS, SETE, OITO, QUARENTA_NOVE;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_CSTPISCOFINSEnum_" + this.name());
	}

	public String getName() {
		return this.name();
	}
}
