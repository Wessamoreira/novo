package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoComunicadoInternoComunicacaoInternaEnum {
	
	MU, LE, RE;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoComunicadoInternoComunicacaoInternaEnum_"+this.name());
	}

}
