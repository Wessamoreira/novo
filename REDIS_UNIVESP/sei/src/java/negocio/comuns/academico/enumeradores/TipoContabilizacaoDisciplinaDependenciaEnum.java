package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoContabilizacaoDisciplinaDependenciaEnum {
	QTDE_DISCIPLINA, CREDITO, CARGA_HORARIA;
	
	public String getValorApresentar(){
		return UteisJSF.internacionalizar("enum_TipoContabilizacaoDisciplinaDependeciaEnum_"+this.name());
	}
}
