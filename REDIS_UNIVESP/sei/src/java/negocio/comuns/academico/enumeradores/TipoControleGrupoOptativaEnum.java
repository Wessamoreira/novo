package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoControleGrupoOptativaEnum {
	
	CREDITO, CARGA_HORARIA;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoControleGrupoOptativaEnum_"+this.name());
	}
	
	

}
