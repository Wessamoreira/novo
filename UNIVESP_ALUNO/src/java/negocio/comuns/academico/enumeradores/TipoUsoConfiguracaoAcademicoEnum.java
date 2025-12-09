package negocio.comuns.academico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;

public enum TipoUsoConfiguracaoAcademicoEnum {

	GERAL, COMPOSTA, FILHA_COMPOSICAO;
	
	public String getValorApresentar() {
		return UteisJSF.internacionalizar("enum_TipoUsoConfiguracaoAcademicoEnum_"+this.name());
	}
	
}
