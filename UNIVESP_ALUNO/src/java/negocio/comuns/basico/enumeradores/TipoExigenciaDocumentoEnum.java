package negocio.comuns.basico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoExigenciaDocumentoEnum {
	
	EXIGENCIA_ALUNO, EXIGENCIA_CURSO, EXIGENCIA_INSTITUCIONAL;

	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoExigenciaDocumentoEnum_"+this.name());
	}
}

