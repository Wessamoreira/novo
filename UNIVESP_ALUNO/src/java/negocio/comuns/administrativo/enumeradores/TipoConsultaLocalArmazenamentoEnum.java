package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoConsultaLocalArmazenamentoEnum {
	
	LOCAL, LOCAL_SUPERIOR, UNIDADE_ENSINO;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoConsultaLocalArmazenamentoEnum_"+this.name());
	}
}
