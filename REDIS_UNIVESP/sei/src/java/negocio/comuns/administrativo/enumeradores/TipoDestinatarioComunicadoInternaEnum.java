package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoDestinatarioComunicadoInternaEnum {
	
	AL, AA, PR, FU, CO, DE, CA, TU, TD, TA, TP, TC, TR, TF, RL, TT ,ALAS, IP;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoDestinatarioComunicadoInternaEnum_"+this.name());
	}

}
