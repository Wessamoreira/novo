package negocio.comuns.ead.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Victor Hugo 11/11/2014
 */
public enum RegraDistribuicaoTutoriaEnum {

	SEQUENCIADA, PRIORITARIA, QUANTITATIVA;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_RegraDistribuicaoTutoriaEnum_" + this.name());
	}
	
	public String getName() {
		return this.name();
	}
}
