package negocio.comuns.secretaria.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum FormaReplicarNotaOutraDisciplinaEnum {
	
	TODAS, NOTAS_LANCADAS, NOTAS_NAO_LANCADAS;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_FormaReplicarNotaOutraDisciplinaEnum_"+this.name());
	}
	
}
